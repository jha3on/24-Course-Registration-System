package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CourseRegistrationDto implements Serializable {
    private Long userId;
    private Long courseId;

    @Builder(access = AccessLevel.PUBLIC)
    public CourseRegistrationDto(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public static CourseRegistrationDto of(Long userId, Long courseId) {
        return CourseRegistrationDto.builder()
                .userId(userId)
                .courseId(courseId)
                .build();
    }
}