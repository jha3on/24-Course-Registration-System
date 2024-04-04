package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.*;
import com.demo.domain.payload.response.CollegeCourseQueryResultDto;
import com.demo.domain.payload.response.QCollegeCourseQueryResultDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class CollegeCourseRepositorySupportLogic implements CollegeCourseRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCollege college = QCollege.college;
    private final QCollegeCourse collegeCourse = QCollegeCourse.collegeCourse;
    private final QCollegeDivision collegeDivision = QCollegeDivision.collegeDivision;
    private final QCollegeDepartment collegeDepartment = QCollegeDepartment.collegeDepartment;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String collegeCourseName, String collegeCourseNumber) {
        Integer queryResult = queryFactory.selectOne().from(collegeCourse)
                .where(eqCollegeCourseName(collegeCourseName),
                       eqCollegeCourseNumber(collegeCourseNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeCourse> getBy(Long collegeCourseId) {
        CollegeCourse queryResult = queryFactory.selectFrom(collegeCourse)
                .where(eqCollegeCourseId(collegeCourseId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeCourse> getBy(String collegeCourseName, String collegeCourseNumber) {
        CollegeCourse queryResult = queryFactory.selectFrom(collegeCourse)
                .where(eqCollegeCourseName(collegeCourseName),
                       eqCollegeCourseNumber(collegeCourseNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeCourseQueryResultDto> getProjectionsBy(Long collegeId, Long collegeDivisionId, Long collegeDepartmentId) {
        List<CollegeCourseQueryResultDto> queryResults = queryFactory.select(new QCollegeCourseQueryResultDto(collegeCourse))
                .from(collegeCourse)
                .leftJoin(college).on(college.id.eq(collegeCourse.college.id))
                .leftJoin(collegeDivision).on(collegeDivision.college.id.eq(collegeCourse.college.id))
                .leftJoin(collegeDepartment).on(collegeDepartment.college.id.eq(collegeCourse.college.id))
                .where(eqCollegeIdFromCollegeCourse(collegeId),
                       eqCollegeDivisionIdFromCollegeCourse(collegeDivisionId),
                       eqCollegeDepartmentIdFromCollegeCourse(collegeDepartmentId))
                .distinct()
                .fetch();

        return queryResults;
    }

    private BooleanExpression eqCollegeCourseId(Long collegeCourseId) {
        if (ObjectUtils.isEmpty(collegeCourseId)) {
            return null;
        }

        return collegeCourse.id.eq(collegeCourseId);
    }

    private BooleanExpression eqCollegeCourseName(String collegeCourseName) {
        if (!StringUtils.hasText(collegeCourseName)) {
            return null;
        }

        return collegeCourse.name.eq(collegeCourseName);
    }

    private BooleanExpression eqCollegeCourseNumber(String collegeCourseNumber) {
        if (!StringUtils.hasText(collegeCourseNumber)) {
            return null;
        }

        return collegeCourse.number.eq(collegeCourseNumber);
    }

    private BooleanExpression eqCollegeIdFromCollegeCourse(Long collegeId) {
        if (ObjectUtils.isEmpty(collegeId)) {
            return null;
        }

        return collegeCourse.college.id.eq(collegeId);
    }

    private BooleanExpression eqCollegeDivisionIdFromCollegeCourse(Long collegeDivisionId) {
        if (ObjectUtils.isEmpty(collegeDivisionId)) {
            return null;
        }

        return collegeCourse.collegeDivision.id.eq(collegeDivisionId);
    }

    private BooleanExpression eqCollegeDepartmentIdFromCollegeCourse(Long collegeDepartmentId) {
        if (ObjectUtils.isEmpty(collegeDepartmentId)) {
            return null;
        }

        return collegeCourse.collegeDepartment.id.eq(collegeDepartmentId);
    }
}