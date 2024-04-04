package com.demo.domain.entity.user;

import com.demo.global.common.entity.AbstractEntity;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 사용자
 */
@Getter
@Entity
@Table(
    name = "tb_user",
    indexes = {
        // @Index(name = "tb_user_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "tb_user_uk_1", columnNames = {"number"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자, ID

    @Column(nullable = false)
    private String name; // 사용자, 이름

    @Column(nullable = false)
    private String number; // 사용자, 학번

    @Column(nullable = false)
    private String password; // 사용자, 비밀번호

    @Column(nullable = false)
    private Integer registrationCredit; // 사용자, 신청 학점

    @Column(nullable = false)
    private Integer registrationCreditLeft; // 사용자, 잔여 신청 학점

    @Builder(access = AccessLevel.PRIVATE)
    public User(Long id, String name, String number, String password, Integer registrationCredit, Integer registrationCreditLeft) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.password = password;
        this.registrationCredit = registrationCredit;
        this.registrationCreditLeft = registrationCreditLeft;
    }

    public static User createBy(String userName, String userNumber, String userPassword, Integer userRegistrationCredit, Integer userRegistrationCreditLeft) {
        return User.builder()
                .name(userName)
                .number(userNumber)
                .password(userPassword)
                .registrationCredit(userRegistrationCredit)
                .registrationCreditLeft(userRegistrationCreditLeft)
                .build();
    }

    public User updateByRegistrationCreditPlus(int credit) {
        if (this.registrationCreditLeft < credit) {
            throw ApiException.of400(ApiExceptionType.COURSE_REGISTRATION_CREDIT_LEFT_NOT_VALID);
        }

        this.registrationCredit = this.registrationCredit + credit;
        this.registrationCreditLeft = this.registrationCreditLeft - credit;
        return this;
    }

    public User updateByRegistrationCreditMinus(int credit) {
        if (this.registrationCredit == 0) {
            throw ApiException.of400(ApiExceptionType.COURSE_REGISTRATION_CREDIT_NOT_VALID);
        }

        this.registrationCredit = this.registrationCredit - credit;
        this.registrationCreditLeft = this.registrationCreditLeft + credit;
        return this;
    }
}