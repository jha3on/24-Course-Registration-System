package com.demo.global.configuration.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.RedisScript;

@Slf4j
@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<Boolean> redisScriptOfCourseSeatCountPlus() {
        Resource resource = new ClassPathResource("data/course_seat_count_plus.lua");
        return RedisScript.of(resource, Boolean.class);
    }

    @Bean
    public RedisScript<Boolean> redisScriptOfCourseSeatCountMinus() {
        Resource resource = new ClassPathResource("data/course_seat_count_minus.lua");
        return RedisScript.of(resource, Boolean.class);
    }
}