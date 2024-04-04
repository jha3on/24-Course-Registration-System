package com.demo.domain.service.course.logic;

import com.demo.domain.entity.course.Course;
import com.demo.domain.entity.course.CourseRegistration;
import com.demo.domain.payload.response.CourseRegistrationQueryResultDto;
import com.demo.domain.repository.rds.course.CourseRegistrationRepository;
import com.demo.domain.repository.rds.course.CourseRepository;
import com.demo.domain.entity.user.User;
import com.demo.domain.repository.rds.user.UserRepository;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationLogic {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;

    /**
     * 수강 신청을 생성한다.
     */
    @Transactional
    public CourseRegistration create(Long userId, Long courseId) {
        User user = userRepository.getBy(userId).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));
        Course course = courseRepository.getBy(courseId, LockModeType.PESSIMISTIC_WRITE).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_NOT_EXISTED));

        CourseRegistration courseRegistration = courseRegistrationRepository.save(CourseRegistration.createBy(user, course));
        courseRegistration.updateByCreate();

        return courseRegistration;
    }

    /**
     * 수강 신청을 삭제한다.
     */
    @Transactional
    public void delete(Long userId, Long courseId) {
        User user = userRepository.getBy(userId).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));
        Course course = courseRepository.getBy(courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_NOT_EXISTED));

        CourseRegistration courseRegistration = courseRegistrationRepository.getByUserCourse(userId, courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_REGISTRATION_NOT_EXISTED));
        courseRegistration.updateByDelete();

        courseRegistrationRepository.delete(courseRegistration);
    }

    /**
     * 수강 신청 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CourseRegistrationQueryResultDto> getDtos(Long userId) {
        return courseRegistrationRepository.getProjectionsByUser(userId);
    }

    /**
     * 수강 신청 개수를 조회한다.
     */
    @Transactional(readOnly = true)
    public Integer getCount(Long courseId) {
        return courseRegistrationRepository.getCountByCourse(courseId);
    }


    @Transactional(readOnly = true)
    public Boolean duplicatedByCourse(Long userId, Long courseId) {
        return courseRegistrationRepository.existsByUserCourse(userId, courseId);
    }
    @Transactional(readOnly = true)
    public Boolean duplicatedByCollegeCourse(Long userId, Long collegeCourseId) {
        return courseRegistrationRepository.existsByUserCollegeCourse(userId, collegeCourseId);
    }
    @Transactional(readOnly = true)
    public Boolean duplicatedByCourseTimetable(Course course, List<CourseRegistration> courseRegistrations) {
        return duplicateBy(course, courseRegistrations);
    }

    @Transactional(readOnly = true)
    public void validate(Long userId, Long courseId) {
        User user = userRepository.getBy(userId).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));
        Course course = courseRepository.getBy(courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_NOT_EXISTED));
        List<CourseRegistration> courseRegistrations = courseRegistrationRepository.getsByUser(userId);

        // 1. 수강 신청 중복을 확인한다. (Course.id = 1을 2번 요청하는 경우)
        if (duplicatedByCourse(userId, courseId)) {
            throw ApiException.of409(ApiExceptionType.COURSE_REGISTRATION_DUPLICATED);
        }

        // 2. 강의 중복을 확인한다. (Course.id = 1, Course.id = 2를 요청하는 경우)
        if (duplicatedByCollegeCourse(userId, course.getCollegeCourse().getId())) {
            throw ApiException.of409(ApiExceptionType.COURSE_REGISTRATION_COURSE_DUPLICATED);
        }

        // 3. 강의 시간 중복을 확인한다. (Course.id = 1, Course.id = 17을 요청하는 경우)
        if (duplicatedByCourseTimetable(course, courseRegistrations)) {
            throw ApiException.of409(ApiExceptionType.COURSE_REGISTRATION_COURSE_TIMETABLE_DUPLICATED);
        }
    }

    /**
     * 강의 시간 중복을 확인한다.
     */
    private Boolean duplicateBy(Course course, List<CourseRegistration> courseRegistrations) {
        // 1. 사용자의 수강 신청 목록을 기반으로 요일별 강의 시간표 맵을 생성한다.
        Map<String, List<Duration>> timetableMap = new HashMap<>();

        // 2. 요일별 강의 시간표를 초기화한다.
        for (String day : new String[]{"월", "화", "수", "목", "금"}) {
            timetableMap.put(day, new ArrayList<>());
        }

        // 3. 사용자의 수강 신청 목록을 요일별 강의 시간표에 추가한다.
        for (String timetables : courseRegistrations.stream().map(e -> e.getCourse().getTimetable()).toList()) {
            for (String timetable : timetables.split(", ")) { // ex) "월 09:00~10:15", "수 09:00~10:15"
                String day = timetable.substring(0, 1); // ex) "월", "수"
                String dayTime = timetable.substring(2); // ex) "09:00~10:15", "09:00~10:15"

                timetableMap.get(day).add(of(dayTime));
            }
        }

        // 4. 요일별 강의 시간표에서 새롭게 신청하는 강의의 강의 시간 중복을 확인한다.
        for (String timetable : course.getTimetable().split(", ")) { // ex) "월 09:00~10:15", "수 09:00~10:15"
            String day = timetable.substring(0, 1); // ex) "월", "수"
            String dayTime = timetable.substring(2); // ex) "09:00~10:15", "09:00~10:15"

            for (Duration duration : timetableMap.get(day)) {
                if (compare(duration, of(dayTime))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 강의 시간을 숫자로 환산한다.
     */
    private Duration of(String time) {
        String from = time.split("~")[0]; // ex) "09:00"
        String to = time.split("~")[1]; // ex) "10:15"
        Integer fromDigit = (Integer.parseInt(from.split(":")[0]) * 60) + Integer.parseInt(from.split(":")[1]); // ex) 540
        Integer toDigit = (Integer.parseInt(to.split(":")[0]) * 60) + Integer.parseInt(to.split(":")[1]); // ex) 615

        return new Duration(fromDigit, toDigit);
    }

    /**
     * 강의 시간 중복을 비교한다.
     */
    private Boolean compare(Duration o1, Duration o2) {
        if (Objects.equals(o1.from(), o2.from())) {
            return true; // 1번
        } else {
            // ______[ o1 ]______
            // ______[ o2 ]______ 1번 (o1, o2의 시작 시간이 겹치기 때문에 중복됨)
            // ___[ o2 ]_________ 2번 (o1의 시작 시간이 o2의 종료 시간과 겹치기 때문에 중복됨)
            // _________[ o2 ]___ 3번 (o1의 종료 시간이 o2의 시작 시간과 겹치기 때문에 중복됨)
            // [ o2 ]____________ 4번 (o1의 시작 시간이 o2의 종료 시간과 겹치지 않기 때문에 중복되지 않음)
            // ____________[ o2 ] 5번 (o1의 종료 시간이 o2의 시작 시간과 겹치지 않기 때문에 중복되지 않음)
            if (o1.from() > o2.from() && o1.from() < o2.to()) return true; // 2번
            if (o1.from() < o2.from() && o1.to() > o2.from()) return true; // 3번
        }

        return false;
    }

    private record Duration(Integer from, Integer to) {}
}