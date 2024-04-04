package com.demo.domain.repository.rds.course;

import com.demo.domain.entity.course.CourseRegistration;
import com.demo.domain.repository.rds.course.logic.CourseRegistrationRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long>, CourseRegistrationRepositorySupport {
    // ...
}