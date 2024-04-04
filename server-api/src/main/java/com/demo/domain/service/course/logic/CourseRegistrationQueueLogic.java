package com.demo.domain.service.course.logic;

import com.demo.domain.entity.course.Course;
import com.demo.domain.entity.course.CourseRegistration;
import com.demo.domain.payload.request.CourseRegistrationDto;
import com.demo.domain.payload.response.CourseRegistrationResultDto;
import com.demo.domain.repository.rds.course.CourseRepository;
import com.demo.domain.repository.redis.course.CourseRegistrationRedisRepository;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.demo.global.configuration.redis.enums.RedisKeyType;
import com.demo.global.configuration.redis.enums.RedisScriptType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationQueueLogic {
    private final ObjectMapper objectMapper;
    private final CourseRepository courseRepository;
    private final CourseRegistrationLogic courseRegistrationLogic;
    private final CourseRegistrationRedisRepository courseRegistrationRedisRepository;

    /**
     * 대기열에 수강 신청 결과를 생성한다.
     */
    public void setResult() {
        // 1. 대기열을 조회한다. (100개 조회)
        Set<String> elements = courseRegistrationRedisRepository.getRangeByZSetOps(RedisKeyType.getCourseRegistrationOrderKey(), 0L, 100L - 1L);

        // 2. 대기열을 확인한다.
        if (Objects.isNull(elements)) {
            // 2-1. empty 상태가 아닌 null 상태인 경우 로직을 수행할 수 없다.
            throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
        }

        // 3. 대기열 항목을 확인한다.
        for (String element : elements) {
            // ex) element: { userId: 1, courseId: 1 }
            Long queryResult = courseRegistrationRedisRepository.deleteByZSetOps(RedisKeyType.getCourseRegistrationOrderKey(), element);

            // 3-1. 대기열 항목이 삭제되었는지 확인한다.
            if (Objects.isNull(queryResult)) {
                throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
            // 3-2. 대기열 항목이 삭제된 경우 수강 신청을 생성한다.
            } else {
                try {
                    CourseRegistrationDto courseRegistrationDto = objectMapper.readValue(element, CourseRegistrationDto.class);
                    CourseRegistrationResultDto courseRegistrationResultDto = validate(courseRegistrationDto);
                    courseRegistrationRedisRepository.setByZSetOps(RedisKeyType.getCourseRegistrationKey(), objectMapper.writeValueAsString(courseRegistrationResultDto));
                } catch (JsonProcessingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_JSON_PROCESSING_FAILED);
                }
            }
        }
    }

    /**
     * 대기열에 수강 신청 순번 결과를 생성한다.
     */
    public void setOrderResult(Long userId, Long courseId) {
        try {
            CourseRegistrationDto courseRegistrationDto = CourseRegistrationDto.of(userId, courseId);
            courseRegistrationRedisRepository.setByZSetOps(RedisKeyType.getCourseRegistrationOrderKey(), objectMapper.writeValueAsString(courseRegistrationDto));
        } catch (JsonProcessingException e) {
            throw ApiException.of500(ApiExceptionType.COMMON_JSON_PROCESSING_FAILED);
        }
    }

    /**
     * 강의 좌석수에 따라 수강 신청 결과를 확인하고 신청 완료된 경우 강의 좌석수를 갱신한다.
     */
    private CourseRegistrationResultDto validate(CourseRegistrationDto courseRegistrationDto) {
        Long userId = courseRegistrationDto.getUserId();
        Long courseId = courseRegistrationDto.getCourseId();
        CourseRegistrationResultDto courseRegistrationResultDto = CourseRegistrationResultDto.of(userId, courseId, "");

        // 1. 커넥션을 확인한다.
        if (courseRegistrationRedisRepository.isShutdown()) {
            synchronizeCourseSeatCount();
        }

        // 2. 강의 좌석수가 캐싱되어 있는지 확인한다.
        if (courseRegistrationRedisRepository.hasKeyBy(RedisKeyType.getCourseSeatKey(courseId))) {
            // 2-1. 캐싱되어 있지만 강의 좌석수가 부족한 경우 CLOSED 상태를 세팅한다.
            if ((Integer.parseInt(courseRegistrationRedisRepository.getByValueOps(RedisKeyType.getCourseSeatKey(courseId))) <= 0)) {
                courseRegistrationResultDto.setMessage("CLOSED");
                return courseRegistrationResultDto;
            }
        }

        // 3. 수강 신청 로직을 실행한다. (로직을 실행하면 강의 좌석수는 1 차감된다.)
        CourseRegistration courseRegistration = courseRegistrationLogic.create(courseRegistrationDto.getUserId(), courseRegistrationDto.getCourseId());
        Course course = courseRegistration.getCourse();

        // 4. 강의 좌석수가 캐싱되어 있는지 확인한다.
        if (courseRegistrationRedisRepository.hasKeyBy(RedisKeyType.getCourseSeatKey(courseId))) {
            // 4-1. 캐싱되어 있는 경우 강의 좌석수를 갱신한다.
            setCourseSeatCountMinus(courseId);
        } else {
            // 4-2. 캐싱되어 있지 않은 경우 강의 좌석수를 캐싱한다.
            courseRegistrationRedisRepository.setByValueOps(RedisKeyType.getCourseSeatKey(courseId), String.valueOf(course.getLeftCount()));
        }

        // 5. COMPLETED 상태를 세팅한다.
        courseRegistrationResultDto.setMessage("COMPLETED");
        return courseRegistrationResultDto;
    }

    /**
     * 강의 좌석수를 1개 더한다.
     */
    public void setCourseSeatCountPlus(Long courseId) {
        courseRegistrationRedisRepository.executeByScript(RedisScriptType.COURSE_SEAT_COUNT_PLUS, RedisKeyType.getCourseSeatKey(courseId), String.valueOf(1));
    }

    /**
     * 강의 좌석수를 1개 뺀다.
     */
    public void setCourseSeatCountMinus(Long courseId) {
        courseRegistrationRedisRepository.executeByScript(RedisScriptType.COURSE_SEAT_COUNT_MINUS, RedisKeyType.getCourseSeatKey(courseId), String.valueOf(1));
    }

    /**
     * 강의 좌석수를 동기화한다. (RDB:REDIS)
     */
    private void synchronizeCourseSeatCount() {
        for (Course course : courseRepository.getsBy()) {
            String key = RedisKeyType.getCourseSeatKey(course.getId()); // course:seat:1
            String value = String.valueOf(course.getRegistrationCountLimit() - courseRegistrationLogic.getCount(course.getId()));

            courseRegistrationRedisRepository.resetByValueOps(key, value);
        }
    }
}