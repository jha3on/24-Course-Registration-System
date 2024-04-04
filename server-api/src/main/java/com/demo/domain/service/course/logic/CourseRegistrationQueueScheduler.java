package com.demo.domain.service.course.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationQueueScheduler {
    private final CourseRegistrationQueueLogic courseRegistrationQueueLogic;

    /**
     * 수강 신청 결과를 생성한다.
     */
    @Scheduled(fixedDelay = 500)
    @SchedulerLock(
        name = "tb_course_registration_lock_1",
        lockAtLeastFor = "4s",
        lockAtMostFor = "8s"
    )
    public void setResult() {
        courseRegistrationQueueLogic.setResult();
    }
}