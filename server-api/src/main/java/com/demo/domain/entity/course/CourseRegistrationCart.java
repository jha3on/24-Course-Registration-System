package com.demo.domain.entity.course;

import com.demo.domain.entity.user.User;
import com.demo.global.common.entity.AbstractLogEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 예비 수강 신청
 */
@Getter
@Entity
@Table(
    name = "tb_course_registration_cart",
    indexes = {
        // @Index(name = "tb_course_registration_cart_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        // 예비 수강 신청은 동일한 강의를 중복 신청할 수 없다.
        @UniqueConstraint(name = "tb_course_registration_cart_uk_1", columnNames = {"user_id", "course_id"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseRegistrationCart extends AbstractLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예비 수강 신청, ID

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 예비 수강 신청, 학생

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course; // 예비 수강 신청, 강의

    @Builder(access = AccessLevel.PRIVATE)
    public CourseRegistrationCart(Long id, User user, Course course) {
        this.id = id;
        this.user = user;
        this.course = course;
    }

    public static CourseRegistrationCart createBy(User user, Course course) {
        return CourseRegistrationCart.builder()
                .user(user)
                .course(course)
                .build();
    }

    public CourseRegistrationCart updateByCreate() {
        this.course.updateByRegistrationCountCartPlus();
        return this;
    }

    public CourseRegistrationCart updateByDelete() {
        this.course.updateByRegistrationCountCartMinus();
        return this;
    }
}