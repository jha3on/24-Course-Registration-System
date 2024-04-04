package com.demo.domain.entity.college;

import com.demo.global.common.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 대학 학부
 */
@Getter
@Entity
@Table(
    name = "tb_college_division",
    indexes = {
        // @Index(name = "tb_college_division_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        // 학부 이름이 동일하더라도 학부 번호가 다르면 사용할 수 있다.
        @UniqueConstraint(name = "tb_college_division_uk_1", columnNames = {"name", "number"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollegeDivision extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대학 학부, ID

    @Column(nullable = false)
    private String name; // 대학 학부, 이름

    @Column(nullable = false)
    private String number; // 대학 학부, 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private College college; // 대학 학부, 대학

    @Builder(access = AccessLevel.PRIVATE)
    public CollegeDivision(Long id, String name, String number, College college) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.college = college;
    }

    public static CollegeDivision createBy(College college, String collegeDivisionName, String collegeDivisionNumber) {
        return CollegeDivision.builder()
                .college(college)
                .name(collegeDivisionName)
                .number(collegeDivisionNumber)
                .build();
    }
}