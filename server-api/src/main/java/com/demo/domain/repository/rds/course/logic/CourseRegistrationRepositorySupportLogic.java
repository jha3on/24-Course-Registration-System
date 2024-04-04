package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.college.QCollege;
import com.demo.domain.entity.college.QCollegeCourse;
import com.demo.domain.entity.college.QCollegeDepartment;
import com.demo.domain.entity.college.QCollegeDivision;
import com.demo.domain.entity.course.CourseRegistration;
import com.demo.domain.entity.course.QCourse;
import com.demo.domain.entity.course.QCourseRegistration;
import com.demo.domain.entity.user.QUser;
import com.demo.domain.payload.response.CourseRegistrationQueryResultDto;
import com.demo.domain.payload.response.QCourseRegistrationQueryResultDto;
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
public class CourseRegistrationRepositorySupportLogic implements CourseRegistrationRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final QCourse course = QCourse.course;
    private final QCollege college = QCollege.college;
    private final QCollegeCourse collegeCourse = QCollegeCourse.collegeCourse;
    private final QCollegeDivision collegeDivision = QCollegeDivision.collegeDivision;
    private final QCollegeDepartment collegeDepartment = QCollegeDepartment.collegeDepartment;
    private final QCourseRegistration courseRegistration = QCourseRegistration.courseRegistration;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByUserCourse(Long userId, Long courseId) {
        Integer queryResult = queryFactory.selectOne().from(courseRegistration)
                .join(user).on(user.id.eq(courseRegistration.user.id))
                .join(course).on(course.id.eq(courseRegistration.course.id))
                .where(user.id.eq(userId),
                       course.id.eq(courseId))
                .fetchFirst();

        return queryResult != null;
    }

    @Transactional(readOnly = true)
    public Boolean existsByUserCollegeCourse(Long userId, Long collegeCourseId) {
        Integer queryResult = queryFactory.selectOne().from(courseRegistration)
                .join(user).on(user.id.eq(courseRegistration.user.id))
                .join(course).on(course.id.eq(courseRegistration.course.id))
                .join(collegeCourse).on(collegeCourse.id.eq(course.collegeCourse.id))
                .where(user.id.eq(userId),
                       collegeCourse.id.eq(collegeCourseId))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCountByCourse(Long courseId) {
        Integer queryResult = queryFactory.selectOne().from(courseRegistration)
                .join(course).on(course.id.eq(courseRegistration.course.id))
                .where(courseRegistration.course.id.eq(courseId))
                .fetchFirst();

        return queryResult;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseRegistration> getByUser(Long userId, Long courseRegistrationId) {
        CourseRegistration queryResult = queryFactory.selectFrom(courseRegistration)
                .join(user).on(user.id.eq(courseRegistration.user.id))
                .where(courseRegistration.id.eq(courseRegistrationId),
                       courseRegistration.user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseRegistration> getByUserCourse(Long userId, Long courseId) {
        CourseRegistration queryResult = queryFactory.selectFrom(courseRegistration)
                .join(user).on(user.id.eq(courseRegistration.user.id))
                .join(course).on(course.id.eq(courseRegistration.course.id))
                .where(user.id.eq(userId),
                       course.id.eq(courseId))
                .fetchFirst();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseRegistration> getsByUser(Long userId) {
        List<CourseRegistration> queryResults = queryFactory.selectFrom(courseRegistration)
                .join(user).on(user.id.eq(courseRegistration.user.id))
                .where(user.id.eq(userId))
                .fetch();

        return queryResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseRegistrationQueryResultDto> getProjectionsByUser(Long userId) {
        List<CourseRegistrationQueryResultDto> queryResults = queryFactory.select(new QCourseRegistrationQueryResultDto(course, college, collegeDivision, collegeDepartment, collegeCourse))
                .from(courseRegistration)
                .leftJoin(user).on(user.id.eq(courseRegistration.user.id))
                .leftJoin(course).on(course.id.eq(courseRegistration.course.id))
                .leftJoin(college).on(college.id.eq(courseRegistration.course.college.id))
                .leftJoin(collegeDivision).on(collegeDivision.id.eq(courseRegistration.course.collegeDivision.id))
                .leftJoin(collegeDepartment).on(collegeDepartment.id.eq(courseRegistration.course.collegeDepartment.id))
                .leftJoin(collegeCourse).on(collegeCourse.id.eq(courseRegistration.course.collegeCourse.id))
                .where(courseRegistration.user.id.eq(userId))
                .orderBy(courseRegistration.course.id.asc())
                .fetch();

        return queryResults;
    }
}