package com.demo.domain.service.user;

import com.demo.domain.entity.user.User;
import com.demo.domain.service.user.logic.UserLogic;
import com.demo.domain.payload.request.UserDto;
import com.demo.domain.payload.response.UserQueryResultDto;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserLogic userLogic;

    /**
     * 사용자를 생성한다.
     */
    public User createUser(UserDto userDto) {
        if (userLogic.duplicated(userDto.getUserNumber())) {
            throw ApiException.of409(ApiExceptionType.USER_EXISTED);
        }

        return userLogic.create(userDto);
    }

    /**
     * 사용자를 조회한다.
     */
    public Optional<User> getUser(String userNumber) {
        return userLogic.get(userNumber);
    }

    /**
     * 사용자를 조회한다.
     */
    public UserQueryResultDto getUserDto(Long userId) {
        return userLogic.getDto(userId);
    }

    /**
     * 사용자 목록을 조회한다.
     */
    public List<UserQueryResultDto> getUserDtos() {
        return userLogic.getDtos();
    }
}