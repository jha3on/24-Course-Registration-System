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
 * 수강 신청
 */
@Getter
@Entity
@Table(
    name = "tb_course_registration",
    indexes = {
        // @Index(name = "tb_course_registration_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        // 수강 신청은 동일한 강의 및 대학 강의를 중복 신청할 수 없다.
        @UniqueConstraint(name = "tb_course_registration_uk_1", columnNames = {"user_id", "course_id", "college_course_id"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseRegistration extends AbstractLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 수강 신청, ID

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 수강 신청, 학생

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course; // 수강 신청, 강의

    @Builder(access = AccessLevel.PRIVATE)
    public CourseRegistration(Long id, User user, Course course) {
        this.id = id;
        this.user = user;
        this.course = course;
    }

    public static CourseRegistration createBy(User user, Course course) {
        return CourseRegistration.builder()
                .user(user)
                .course(course)
                .build();
    }

    public CourseRegistration updateByCreate() {
        this.user.updateByRegistrationCreditPlus(this.course.getCredit());
        this.course.updateByRegistrationCountPlus();
        return this;
    }

    public CourseRegistration updateByDelete() {
        this.user.updateByRegistrationCreditMinus(this.course.getCredit());
        this.course.updateByRegistrationCountMinus();
        return this;
    }
}