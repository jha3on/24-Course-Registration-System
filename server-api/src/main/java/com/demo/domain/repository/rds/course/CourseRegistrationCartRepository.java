package com.demo.domain.repository.rds.course;

import com.demo.domain.entity.course.CourseRegistrationCart;
import com.demo.domain.repository.rds.course.logic.CourseRegistrationCartRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRegistrationCartRepository extends JpaRepository<CourseRegistrationCart, Long>, CourseRegistrationCartRepositorySupport {
    // ...
}