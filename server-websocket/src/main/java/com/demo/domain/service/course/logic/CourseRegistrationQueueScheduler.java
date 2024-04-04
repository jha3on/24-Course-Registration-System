package com.demo.domain.service.course.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseRegistrationQueueScheduler {
    private final CourseRegistrationQueueLogic courseRegistrationQueueLogic;

    /**
     * 수강 신청 결과를 조회한다.
     */
    @Scheduled(fixedDelay = 100)
    public void getResult() {
        courseRegistrationQueueLogic.getResult();
    }

    /**
     * 수강 신청 순번 결과를 조회한다.
     */
    @Scheduled(fixedDelay = 1000)
    public void getOrderResult() {
        courseRegistrationQueueLogic.getOrderResult();
    }
}