package com.demo.domain.repository.rds.college.logic;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.QCollege;
import com.demo.domain.payload.response.CollegeQueryResultDto;
import com.demo.domain.payload.response.QCollegeQueryResultDto;
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
public class CollegeRepositorySupportLogic implements CollegeRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCollege college = QCollege.college;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String collegeName, String collegeNumber) {
        Integer queryResult = queryFactory.selectOne().from(college)
                .where(eqCollegeName(collegeName),
                       eqCollegeNumber(collegeNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<College> getBy(Long collegeId) {
        College queryResult = queryFactory.selectFrom(college)
                .where(eqCollegeId(collegeId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<College> getBy(String collegeName, String collegeNumber) {
        College queryResult = queryFactory.selectFrom(college)
                .where(eqCollegeName(collegeName),
                       eqCollegeNumber(collegeNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollegeQueryResultDto> getProjectionsBy() {
        List<CollegeQueryResultDto> queryResults = queryFactory.select(new QCollegeQueryResultDto(college))
                .from(college)
                .fetch();

        return queryResults;
    }

    private BooleanExpression eqCollegeId(Long collegeId) {
        if (ObjectUtils.isEmpty(collegeId)) {
            return null;
        }

        return college.id.eq(collegeId);
    }

    private BooleanExpression eqCollegeName(String collegeName) {
        if (!StringUtils.hasText(collegeName)) {
            return null;
        }

        return college.name.eq(collegeName);
    }

    private BooleanExpression eqCollegeNumber(String collegeNumber) {
        if (!StringUtils.hasText(collegeNumber)) {
            return null;
        }

        return college.number.eq(collegeNumber);
    }
}