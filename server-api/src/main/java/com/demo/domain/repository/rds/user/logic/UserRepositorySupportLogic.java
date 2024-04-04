package com.demo.domain.repository.rds.user.logic;

import com.demo.domain.entity.user.QUser;
import com.demo.domain.entity.user.User;
import com.demo.domain.payload.response.QUserQueryResultDto;
import com.demo.domain.payload.response.UserQueryResultDto;
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
public class UserRepositorySupportLogic implements UserRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    @Override
    @Transactional(readOnly = true)
    public Boolean existsBy(String userNumber) {
        Integer queryResult = queryFactory.selectOne().from(user)
                .where(user.number.eq(userNumber))
                .fetchFirst();

        return queryResult != null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getBy(Long userId) {
        User queryResult = queryFactory.selectFrom(user)
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getBy(String userNumber) {
        User queryResult = queryFactory.selectFrom(user)
                .where(user.number.eq(userNumber))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    @Override
    @Transactional(readOnly = true)
    public UserQueryResultDto getProjectionBy(Long userId) {
        UserQueryResultDto queryResult = queryFactory.select(new QUserQueryResultDto(user))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();

        return queryResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserQueryResultDto> getProjectionsBy() {
        List<UserQueryResultDto> queryResults = queryFactory.select(new QUserQueryResultDto(user))
                .from(user)
                .fetch();

        return queryResults;
    }
}