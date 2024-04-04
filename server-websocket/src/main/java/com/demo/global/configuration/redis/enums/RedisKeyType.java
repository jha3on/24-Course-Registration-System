package com.demo.global.configuration.redis.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RedisKeyType {
    COURSE_SEAT("course:seat"),
    COURSE_REGISTRATION("course:registration"),
    COURSE_REGISTRATION_ORDER("course:registration:order");

    private final String value;

    public static String getCourseSeatKey(Long courseId) {
        return String.format("%s:%d", COURSE_SEAT.getValue(), courseId);
    }

    public static String getCourseRegistrationKey() {
        return COURSE_REGISTRATION.getValue();
    }

    public static String getCourseRegistrationOrderKey() {
        return COURSE_REGISTRATION_ORDER.getValue();
    }
}