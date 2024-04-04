package com.demo.domain.payload.response;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CourseRegistrationOrderResultDto implements Serializable {
    private Long userId;
    private Long courseId;

    @Builder(access = AccessLevel.PUBLIC)
    public CourseRegistrationOrderResultDto(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public static CourseRegistrationOrderResultDto of(Long userId, Long courseId) {
        return CourseRegistrationOrderResultDto.builder()
                .userId(userId)
                .courseId(courseId)
                .build();
    }
}