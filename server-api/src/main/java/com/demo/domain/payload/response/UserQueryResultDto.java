package com.demo.domain.payload.response;

import com.demo.domain.entity.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserQueryResultDto implements Serializable {
    private Long userId;
    private String userName;
    private String userNumber;
    private Integer userRegistrationCredit;
    private Integer userRegistrationCreditLeft;

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public UserQueryResultDto(Long userId, String userName, String userNumber, Integer userRegistrationCredit, Integer userRegistrationCreditLeft) {
        this.userId = userId;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userRegistrationCredit = userRegistrationCredit;
        this.userRegistrationCreditLeft = userRegistrationCreditLeft;
    }

    @QueryProjection
    @Builder(access = AccessLevel.PUBLIC)
    public UserQueryResultDto(User user) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.userNumber = user.getNumber();
        this.userRegistrationCredit = user.getRegistrationCredit();
        this.userRegistrationCreditLeft = user.getRegistrationCreditLeft();
    }
}