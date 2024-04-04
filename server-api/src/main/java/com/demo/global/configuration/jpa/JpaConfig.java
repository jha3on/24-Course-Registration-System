package com.demo.global.configuration.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@EnableJpaAuditing
// @EnableJpaRepositories(basePackages = {"com.demo.domain.repository.rds.*"})
@EnableJpaRepositories(basePackages = {"com.demo.domain.repository.rds.*"}, repositoryImplementationPostfix = "Logic")
public class JpaConfig {
    // ...
}