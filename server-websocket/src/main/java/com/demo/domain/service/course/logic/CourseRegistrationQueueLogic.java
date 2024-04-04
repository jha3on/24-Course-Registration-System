package com.demo.domain.service.course.logic;

import com.demo.domain.payload.response.CourseRegistrationOrderResultDto;
import com.demo.domain.repository.redis.course.CourseRegistrationRedisRepository;
import com.demo.domain.payload.response.CourseRegistrationResultDto;
import com.demo.global.configuration.redis.enums.RedisKeyType;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationQueueLogic {
    private final ObjectMapper objectMapper;
    private final CourseRegistrationMessenger courseRegistrationMessenger;
    private final CourseRegistrationRedisRepository courseRegistrationRedisRepository;

    /**
     * 대기열에서 수강 신청 결과를 조회한다.
     */
    public void getResult() {
        // 1. 대기열을 조회한다. (전체 조회)
        Set<String> elements = courseRegistrationRedisRepository.getRangeByZSetOps(RedisKeyType.getCourseRegistrationKey(), 0L, -1L);

        // 2. 대기열을 확인한다.
        if (Objects.isNull(elements)) {
            // 2-1. empty 상태가 아닌 null 상태인 경우 로직을 수행할 수 없다.
            throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
        }

        // 3. 대기열 항목을 확인한다.
        for (String element : elements) { // ex) element: { userId: 1, courseId: 1, message: COMPLETED|CLOSED }
            Long queryResult = courseRegistrationRedisRepository.deleteByZSetOps(RedisKeyType.getCourseRegistrationKey(), element);

            if (Objects.isNull(queryResult)) {
                throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
            } else {
                try {
                    CourseRegistrationResultDto courseRegistrationResultDto = objectMapper.readValue(element, CourseRegistrationResultDto.class);
                    courseRegistrationMessenger.messageByGetResult(courseRegistrationResultDto.getUserId(), courseRegistrationResultDto.getMessage()); // ex) payload: COMPLETED|CLOSED (신청 완료: COMPLETED, 신청 마감: CLOSED)
                } catch (JsonMappingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_JSON_MAPPING_FAILED);
                } catch (JsonProcessingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_JSON_PROCESSING_FAILED);
                } catch (MessagingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_SOCKET_MESSAGE_SENDING_FAILED);
                }
            }
        }
    }

    /**
     * 대기열에서 수강 신청 순번 결과를 조회한다.
     */
    public void getOrderResult() {
        // 1. 대기열을 조회한다. (전체 조회)
        Set<String> elements = courseRegistrationRedisRepository.getRangeByZSetOps(RedisKeyType.getCourseRegistrationOrderKey(), 0L, -1L);

        // 2. 대기열을 확인한다.
        if (Objects.isNull(elements)) {
            // 2-1. empty 상태가 아닌 null 상태인 경우 로직을 수행할 수 없다.
            throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
        }

        // 3. 대기열 항목을 확인한다.
        for (String element : elements) { // ex) element: { userId: 1, courseId: 1 }
            Long queryResult = courseRegistrationRedisRepository.getRankByZSetOps(RedisKeyType.getCourseRegistrationOrderKey(), element);

            if (Objects.isNull(queryResult)) {
                throw ApiException.of500(ApiExceptionType.COMMON_QUERY_RESULT_NOT_VALID);
            } else {
                try {
                    CourseRegistrationOrderResultDto courseRegistrationOrderResultDto = objectMapper.readValue(element, CourseRegistrationOrderResultDto.class);
                    courseRegistrationMessenger.messageByGetOrderResult(courseRegistrationOrderResultDto.getUserId(), String.valueOf(queryResult)); // ex) payload: 10 (순번: 10번)
                } catch (JsonMappingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_JSON_MAPPING_FAILED);
                } catch (JsonProcessingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_JSON_PROCESSING_FAILED);
                } catch (MessagingException e) {
                    throw ApiException.of500(ApiExceptionType.COMMON_SOCKET_MESSAGE_SENDING_FAILED);
                }
            }
        }
    }
}