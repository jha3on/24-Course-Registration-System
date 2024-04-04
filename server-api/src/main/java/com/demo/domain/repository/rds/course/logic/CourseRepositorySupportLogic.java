package com.demo.domain.repository.rds.course.logic;

import com.demo.domain.entity.college.QCollege;
import com.demo.domain.entity.college.QCollegeCourse;
import com.demo.domain.entity.college.QCollegeDepartment;
import com.demo.domain.entity.college.QCollegeDivision;
import com.demo.domain.entity.course.Course;
import com.demo.domain.entity.course.QCourse;
import com.demo.domain.entity.course.QCourseRegistration;
import com.demo.domain.payload.response.CourseQueryResultDto;
import com.demo.domain.payload.response.QCourseQueryResultDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CourseRepositorySupportLogic implements CourseRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCourse course = QCourse.course;
    private final QCollege college = QCollege.college;
    private final QCollegeCourse collegeCourse = QCollegeCourse.collegeCourse;
    private final QCollegeDivision collegeDivision = QCollegeDivision.collegeDivision;
    private final QCollegeDepartment collegeDepartment = QCollegeDepartment.collegeDepartment;
    private final QCourseRegistration courseRegistration = QCourseRegistration.courseRegistration;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String courseNumber) {
        Integer queryResult = queryFactory.selectOne().from(course)
                .where(course.number.eq(courseNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getBy(Long courseId) {
        Course queryResult = queryFactory.selectFrom(course)
                .where(course.id.eq(courseId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getBy(String courseNumber) {
        Course queryResult = queryFactory.selectFrom(course)
                .where(course.number.eq(courseNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getBy(Long courseId, LockModeType lockModeType) {
        Course queryResult = queryFactory.selectFrom(course)
                .where(course.id.eq(courseId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getsBy() {
        List<Course> queryResults = queryFactory.selectFrom(course)
                .join(courseRegistration).on(courseRegistration.course.id.eq(course.id))
                .distinct()
                .fetch();

        return queryResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseQueryResultDto> getProjectionsBy(String courseYear, String courseSemester, String courseNumber) {
        List<CourseQueryResultDto> queryResults = queryFactory.select(new QCourseQueryResultDto(course, college, collegeDivision, collegeDepartment, collegeCourse))
                .from(course)
                .leftJoin(college).on(college.id.eq(course.college.id))
                .leftJoin(collegeCourse).on(collegeCourse.id.eq(course.collegeCourse.id))
                .leftJoin(collegeDivision).on(collegeDivision.id.eq(course.collegeDivision.id))
                .leftJoin(collegeDepartment).on(collegeDepartment.id.eq(course.collegeDepartment.id))
                .where(eqCourseYear(courseYear),
                       eqCourseSemester(courseSemester),
                       eqCourseNumber(courseNumber))
                .orderBy(course.id.asc())
                .fetch();

        return queryResults;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseQueryResultDto> getProjectionsBy(String courseYear, String courseSemester, String courseType, Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        List<CourseQueryResultDto> queryResults = queryFactory.select(new QCourseQueryResultDto(course, college, collegeDivision, collegeDepartment, collegeCourse))
                .from(course)
                .leftJoin(college).on(college.id.eq(course.college.id))
                .leftJoin(collegeCourse).on(collegeCourse.id.eq(course.collegeCourse.id))
                .leftJoin(collegeDivision).on(collegeDivision.id.eq(course.collegeDivision.id))
                .leftJoin(collegeDepartment).on(collegeDepartment.id.eq(course.collegeDepartment.id))
                .where(eqCourseYear(courseYear),
                       eqCourseSemester(courseSemester),
                       eqCourseType(courseType),
                       eqCollegeIdFromCourse(collegeId),
                       eqCollegeDivisionIdFromCourse(collegeDivisionId),
                       eqCollegeDepartmentIdFromCourse(collegeDepartmentId))
                .orderBy(course.id.asc())
                .fetch();

        return queryResults;
    }

    private BooleanExpression eqCourseNumber(String courseNumber) {
        if (!StringUtils.hasText(courseNumber)) {
            return null;
        }

        return course.number.eq(courseNumber);
    }

    private BooleanExpression eqCourseYear(String courseYear) {
        if (!StringUtils.hasText(courseYear)) {
            return null;
        }

        return course.year.eq(courseYear);
    }

    private BooleanExpression eqCourseSemester(String courseSemester) {
        if (!StringUtils.hasText(courseSemester)) {
            return null;
        }

        return course.semester.eq(courseSemester);
    }

    private BooleanExpression eqCourseType(String courseType) {
        if (!StringUtils.hasText(courseType)) {
            return null;
        }

        return course.type.eq(courseType);
    }

    private BooleanExpression eqCollegeIdFromCourse(Long collegeId) {
        if (ObjectUtils.isEmpty(collegeId)) {
            return null;
        }

        return course.college.id.eq(collegeId);
    }

    private BooleanExpression eqCollegeDivisionIdFromCourse(Long collegeDivisionId) {
        if (ObjectUtils.isEmpty(collegeDivisionId)) {
            return null;
        }

        return course.collegeDivision.id.eq(collegeDivisionId);
    }

    private BooleanExpression eqCollegeDepartmentIdFromCourse(Long collegeDepartmentId) {
        if (ObjectUtils.isEmpty(collegeDepartmentId)) {
            return null;
        }

        return course.collegeDepartment.id.eq(collegeDepartmentId);
    }
}