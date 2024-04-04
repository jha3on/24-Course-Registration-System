package com.demo.global.configuration.redis.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RedisScriptType {
    COURSE_SEAT_COUNT_PLUS(),
    COURSE_SEAT_COUNT_MINUS(),
}