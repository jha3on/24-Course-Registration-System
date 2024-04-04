package com.demo.domain.repository.rds.course;

import com.demo.domain.entity.course.Course;
import com.demo.domain.repository.rds.course.logic.CourseRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositorySupport {
    // ...
}