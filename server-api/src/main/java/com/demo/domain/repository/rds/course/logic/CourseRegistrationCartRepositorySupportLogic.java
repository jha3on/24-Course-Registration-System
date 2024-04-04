package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.college.QCollege;
import com.demo.domain.entity.college.QCollegeCourse;
import com.demo.domain.entity.college.QCollegeDepartment;
import com.demo.domain.entity.college.QCollegeDivision;
import com.demo.domain.entity.course.CourseRegistrationCart;
import com.demo.domain.entity.course.QCourse;
import com.demo.domain.entity.course.QCourseRegistrationCart;
import com.demo.domain.entity.user.QUser;
import com.demo.domain.payload.response.CourseRegistrationCartQueryResultDto;
import com.demo.domain.payload.response.QCourseRegistrationCartQueryResultDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CourseRegistrationCartRepositorySupportLogic implements CourseRegistrationCartRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final QCourse course = QCourse.course;
    private final QCollege college = QCollege.college;
    private final QCollegeCourse collegeCourse = QCollegeCourse.collegeCourse;
    private final QCollegeDivision collegeDivision = QCollegeDivision.collegeDivision;
    private final QCollegeDepartment collegeDepartment = QCollegeDepartment.collegeDepartment;
    private final QCourseRegistrationCart courseRegistrationCart = QCourseRegistrationCart.courseRegistrationCart;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByUserCourse(Long userId, Long courseId) {
        Integer queryResult = queryFactory.selectOne().from(courseRegistrationCart)
                .join(user).on(user.id.eq(courseRegistrationCart.user.id))
                .join(course).on(course.id.eq(courseRegistrationCart.course.id))
                .where(user.id.eq(userId),
                       course.id.eq(courseId))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseRegistrationCart> getBy(Long courseRegistrationCartId) {
        CourseRegistrationCart queryResult = queryFactory.selectFrom(courseRegistrationCart)
                .where(courseRegistrationCart.id.eq(courseRegistrationCartId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseRegistrationCart> getByUser(Long userId, Long courseRegistrationCartId) {
        CourseRegistrationCart queryResult = queryFactory.selectFrom(courseRegistrationCart)
                .join(user).on(user.id.eq(courseRegistrationCart.user.id))
                .where(courseRegistrationCart.user.id.eq(userId),
                       courseRegistrationCart.id.eq(courseRegistrationCartId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseRegistrationCart> getByUserCourse(Long userId, Long courseId) {
        CourseRegistrationCart queryResult = queryFactory.selectFrom(courseRegistrationCart)
                .join(user).on(user.id.eq(courseRegistrationCart.user.id))
                .join(course).on(course.id.eq(courseRegistrationCart.course.id))
                .where(user.id.eq(userId),
                       course.id.eq(courseId))
                .fetchFirst();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseRegistrationCartQueryResultDto> getProjectionsByUser(Long userId) {
        List<CourseRegistrationCartQueryResultDto> queryResults = queryFactory.select(new QCourseRegistrationCartQueryResultDto(course, college, collegeDivision, collegeDepartment, collegeCourse))
                .from(courseRegistrationCart)
                .leftJoin(user).on(user.id.eq(courseRegistrationCart.user.id))
                .leftJoin(course).on(course.id.eq(courseRegistrationCart.course.id))
                .leftJoin(college).on(college.id.eq(courseRegistrationCart.course.college.id))
                .leftJoin(collegeDivision).on(collegeDivision.id.eq(courseRegistrationCart.course.collegeDivision.id))
                .leftJoin(collegeDepartment).on(collegeDepartment.id.eq(courseRegistrationCart.course.collegeDepartment.id))
                .leftJoin(collegeCourse).on(collegeCourse.id.eq(courseRegistrationCart.course.collegeCourse.id))
                .where(courseRegistrationCart.user.id.eq(userId))
                .orderBy(courseRegistrationCart.course.id.asc())
                .fetch();

        return queryResults;
    }
}