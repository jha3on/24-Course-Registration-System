package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.QCollege;
import com.demo.domain.entity.college.QCollegeDepartment;
import com.demo.domain.payload.response.CollegeDepartmentQueryResultDto;
import com.demo.domain.payload.response.QCollegeDepartmentQueryResultDto;
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
public class CollegeDepartmentRepositorySupportLogic implements CollegeDepartmentRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCollege college = QCollege.college;
    private final QCollegeDepartment collegeDepartment = QCollegeDepartment.collegeDepartment;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String collegeDepartmentName, String collegeDepartmentNumber) {
        Integer queryResult = queryFactory.selectOne().from(collegeDepartment)
                .where(eqCollegeDepartmentName(collegeDepartmentName),
                       eqCollegeDepartmentNumber(collegeDepartmentNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDepartment> getBy(Long collegeDepartmentId) {
        CollegeDepartment queryResult = queryFactory.selectFrom(collegeDepartment)
                .where(eqCollegeDepartmentId(collegeDepartmentId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDepartment> getBy(String collegeDepartmentName, String collegeDepartmentNumber) {
        CollegeDepartment queryResult = queryFactory.selectFrom(collegeDepartment)
                .where(eqCollegeDepartmentName(collegeDepartmentName),
                       eqCollegeDepartmentNumber(collegeDepartmentNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeDepartmentQueryResultDto> getProjectionsBy(Long collegeId) {
        List<CollegeDepartmentQueryResultDto> queryResults = queryFactory.select(new QCollegeDepartmentQueryResultDto(collegeDepartment))
                .from(collegeDepartment)
                .leftJoin(college).on(college.id.eq(collegeDepartment.college.id))
                .where(eqCollegeId(collegeId))
                .fetch();

        return queryResults;
    }

    private BooleanExpression eqCollegeId(Long collegeId) {
        if (ObjectUtils.isEmpty(collegeId)) {
            return null;
        }

        return college.id.eq(collegeId);
    }

    private BooleanExpression eqCollegeDepartmentId(Long collegeDepartmentId) {
        if (ObjectUtils.isEmpty(collegeDepartmentId)) {
            return null;
        }

        return collegeDepartment.id.eq(collegeDepartmentId);
    }

    private BooleanExpression eqCollegeDepartmentName(String collegeDepartmentName) {
        if (!StringUtils.hasText(collegeDepartmentName)) {
            return null;
        }

        return collegeDepartment.name.eq(collegeDepartmentName);
    }

    private BooleanExpression eqCollegeDepartmentNumber(String collegeDepartmentNumber) {
        if (!StringUtils.hasText(collegeDepartmentNumber)) {
            return null;
        }

        return collegeDepartment.number.eq(collegeDepartmentNumber);
    }
}