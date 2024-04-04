package com.demo.domain.payload.response;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CourseRegistrationResultDto implements Serializable {
    private Long userId;
    private Long courseId;
    private String message;

    @Builder(access = AccessLevel.PUBLIC)
    public CourseRegistrationResultDto(Long userId, Long courseId, String message) {
        this.userId = userId;
        this.courseId = courseId;
        this.message = message;
    }

    public static CourseRegistrationResultDto of(Long userId, Long courseId, String message) {
        return CourseRegistrationResultDto.builder()
                .userId(userId)
                .courseId(courseId)
                .message(message)
                .build();
    }
}