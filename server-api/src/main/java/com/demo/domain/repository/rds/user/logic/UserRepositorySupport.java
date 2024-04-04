package com.demo.domain.repository.rds.user.logic;

import com.demo.domain.entity.user.User;
import com.demo.domain.payload.response.UserQueryResultDto;
import java.util.List;
import java.util.Optional;

public interface UserRepositorySupport {

    /**
     * 사용자 중복을 확인한다.
     */
    Boolean existsBy(String userNumber);

    /**
     * 사용자를 조회한다.
     */
    Optional<User> getBy(Long userId);

    /**
     * 사용자를 조회한다.
     */
    Optional<User> getBy(String userNumber);

    /**
     * 사용자를 조회한다.
     */
    UserQueryResultDto getProjectionBy(Long userId);

    /**
     * 사용자 목록을 조회한다.
     */
    List<UserQueryResultDto> getProjectionsBy();
}