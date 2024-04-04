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
 * 대학
 */
@Getter
@Entity
@Table(
    name = "tb_college",
    indexes = {
        // @Index(name = "tb_college_idx_1", columnList = "xxx ASC|DESC"),
    },
    uniqueConstraints = {
        // 대학 이름이 동일하더라도 대학 번호가 다르면 사용할 수 있다.
        @UniqueConstraint(name = "tb_college_uk_1", columnNames = {"name", "number"}),
    }
)
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class College extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대학, ID

    @Column(nullable = false)
    private String name; // 대학, 이름

    @Column(nullable = false)
    private String number; // 대학, 번호

    @Builder(access = AccessLevel.PRIVATE)
    public College(Long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public static College createBy(String collegeName, String collegeNumber) {
        return College.builder()
                .name(collegeName)
                .number(collegeNumber)
                .build();
    }
}