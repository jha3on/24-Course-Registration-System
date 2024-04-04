package com.demo.global.configuration.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableSchedulerLock(
    defaultLockAtLeastFor = "30s",
    defaultLockAtMostFor = "60s"
) // 스케줄러 락을 최소 30초, 최대 60초를 유지한다. (기본값)
public class SchedulerLockConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withTableName("tb_scheduler_lock") // -> SchedulerLock.class
                .withColumnNames(new JdbcTemplateLockProvider.ColumnNames("name", "stop_at", "start_at", "use_by")) // -> SchedulerLock.class
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime()
                .build());
    }
}