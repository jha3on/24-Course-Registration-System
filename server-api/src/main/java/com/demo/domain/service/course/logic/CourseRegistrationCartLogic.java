package com.demo.domain.service.course.logic;

import com.demo.domain.entity.course.Course;
import com.demo.domain.entity.course.CourseRegistrationCart;
import com.demo.domain.payload.response.CourseRegistrationCartQueryResultDto;
import com.demo.domain.repository.rds.course.CourseRegistrationCartRepository;
import com.demo.domain.repository.rds.course.CourseRepository;
import com.demo.domain.entity.user.User;
import com.demo.domain.repository.rds.user.UserRepository;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationCartLogic {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseRegistrationCartRepository courseRegistrationCartRepository;

    /**
     * 예비 수강 신청 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(Long userId, Long courseId) {
        return courseRegistrationCartRepository.existsByUserCourse(userId, courseId);
    }

    /**
     * 예비 수강 신청을 생성한다.
     */
    @Transactional
    public CourseRegistrationCart create(Long userId, Long courseId) {
        User user = userRepository.getBy(userId).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));
        Course course = courseRepository.getBy(courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_NOT_EXISTED));

        CourseRegistrationCart courseRegistrationCart = courseRegistrationCartRepository.save(CourseRegistrationCart.createBy(user, course));
        courseRegistrationCart.updateByCreate();

        return courseRegistrationCart;
    }

    /**
     * 예비 수강 신청을 삭제한다.
     */
    @Transactional
    public void delete(Long userId, Long courseId) {
        User user = userRepository.getBy(userId).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));
        Course course = courseRepository.getBy(courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_NOT_EXISTED));

        CourseRegistrationCart courseRegistrationCart = courseRegistrationCartRepository.getByUserCourse(userId, courseId).orElseThrow(() -> ApiException.of404(ApiExceptionType.COURSE_REGISTRATION_CART_NOT_EXISTED));
        courseRegistrationCart.updateByDelete();

        courseRegistrationCartRepository.delete(courseRegistrationCart);
    }

    /**
     * 예비 수강 신청 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<CourseRegistrationCartQueryResultDto> getDtos(Long userId) {
        return courseRegistrationCartRepository.getProjectionsByUser(userId);
    }
}