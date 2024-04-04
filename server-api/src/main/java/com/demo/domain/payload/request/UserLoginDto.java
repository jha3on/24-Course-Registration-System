package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserLoginDto implements Serializable {
    private String userNumber;
    private String userPassword;

    @Builder(access = AccessLevel.PUBLIC)
    public UserLoginDto(String userNumber, String userPassword) {
        this.userNumber = userNumber;
        this.userPassword = userPassword;
    }
}