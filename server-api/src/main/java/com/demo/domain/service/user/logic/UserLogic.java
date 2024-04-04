package com.demo.domain.service.user.logic;

import com.demo.domain.entity.user.User;
import com.demo.domain.repository.rds.user.UserRepository;
import com.demo.domain.payload.request.UserDto;
import com.demo.domain.payload.response.UserQueryResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogic {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 중복을 확인한다.
     */
    @Transactional(readOnly = true)
    public Boolean duplicated(String userNumber) {
        return userRepository.existsBy(userNumber);
    }

    /**
     * 사용자를 생성한다.
     */
    @Transactional
    public User create(UserDto userDto) {
        User user = User.createBy(userDto.getUserName(), userDto.getUserNumber(), passwordEncoder.encode(userDto.getUserPassword()), userDto.getUserRegistrationCredit(), userDto.getUserRegistrationCreditLeft());

        return userRepository.save(user);
    }

    /**
     * 사용자를 조회한다.
     */
    @Transactional(readOnly = true)
    public Optional<User> get(String userNumber) {
        return userRepository.getBy(userNumber);
    }

    /**
     * 사용자를 조회한다.
     */
    @Transactional(readOnly = true)
    public UserQueryResultDto getDto(Long userId) {
        return userRepository.getProjectionBy(userId);
    }

    /**
     * 사용자 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<UserQueryResultDto> getDtos() {
        return userRepository.getProjectionsBy();
    }
}