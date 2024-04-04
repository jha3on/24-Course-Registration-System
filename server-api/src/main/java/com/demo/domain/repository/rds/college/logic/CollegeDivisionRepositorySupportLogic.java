package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.entity.college.QCollege;
import com.demo.domain.entity.college.QCollegeDivision;
import com.demo.domain.payload.response.CollegeDivisionQueryResultDto;
import com.demo.domain.payload.response.QCollegeDivisionQueryResultDto;
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
public class CollegeDivisionRepositorySupportLogic implements CollegeDivisionRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCollege college = QCollege.college;
    private final QCollegeDivision collegeDivision = QCollegeDivision.collegeDivision;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String collegeDivisionName, String collegeDivisionNumber) {
        Integer queryResult = queryFactory.selectOne().from(collegeDivision)
                .where(eqCollegeDivisionName(collegeDivisionName),
                       eqCollegeDivisionNumber(collegeDivisionNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDivision> getBy(Long collegeDivisionId) {
        CollegeDivision queryResult = queryFactory.selectFrom(collegeDivision)
                .where(eqCollegeDivisionId(collegeDivisionId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDivision> getBy(String collegeDivisionName, String collegeDivisionNumber) {
        CollegeDivision queryResult = queryFactory.selectFrom(collegeDivision)
                .where(eqCollegeDivisionName(collegeDivisionName),
                       eqCollegeDivisionNumber(collegeDivisionNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeDivisionQueryResultDto> getProjectionsBy(Long collegeId) {
        List<CollegeDivisionQueryResultDto> queryResults = queryFactory.select(new QCollegeDivisionQueryResultDto(collegeDivision))
                .from(collegeDivision)
                .leftJoin(college).on(college.id.eq(collegeDivision.college.id))
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

    private BooleanExpression eqCollegeDivisionId(Long collegeDivisionId) {
        if (ObjectUtils.isEmpty(collegeDivisionId)) {
            return null;
        }

        return collegeDivision.id.eq(collegeDivisionId);
    }

    private BooleanExpression eqCollegeDivisionName(String collegeDivisionName) {
        if (!StringUtils.hasText(collegeDivisionName)) {
            return null;
        }

        return collegeDivision.name.eq(collegeDivisionName);
    }

    private BooleanExpression eqCollegeDivisionNumber(String collegeDivisionNumber) {
        if (!StringUtils.hasText(collegeDivisionNumber)) {
            return null;
        }

        return collegeDivision.number.eq(collegeDivisionNumber);
    }
}