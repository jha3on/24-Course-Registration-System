package com.demo.global.configuration.security.login;

import com.demo.domain.entity.user.User;
import com.demo.domain.repository.rds.user.UserRepository;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userNumber) {
        User user = userRepository.getBy(userNumber).orElseThrow(() -> ApiException.of404(ApiExceptionType.USER_NOT_EXISTED));

        return UserLoginDetails.of(user);
    }
}