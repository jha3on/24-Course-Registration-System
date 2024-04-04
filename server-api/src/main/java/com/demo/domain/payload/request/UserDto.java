package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"userName", "userNumber"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDto implements Serializable {
    private String userName;
    private String userNumber;
    private String userPassword;
    private Integer userRegistrationCredit;
    private Integer userRegistrationCreditLeft;

    @Builder(access = AccessLevel.PUBLIC)
    public UserDto(String userName, String userNumber, String userPassword, Integer userRegistrationCredit, Integer userRegistrationCreditLeft) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userPassword = userPassword;
        this.userRegistrationCredit = userRegistrationCredit;
        this.userRegistrationCreditLeft = userRegistrationCreditLeft;
    }

    public static UserDto of(String userName, String userNumber, String userPassword, Integer userRegistrationCredit, Integer userRegistrationCreditLeft) {
        return UserDto.builder()
                .userName(userName)
                .userNumber(userNumber)
                .userPassword(userPassword)
                .userRegistrationCredit(userRegistrationCredit)
                .userRegistrationCreditLeft(userRegistrationCreditLeft)
                .build();
    }
}