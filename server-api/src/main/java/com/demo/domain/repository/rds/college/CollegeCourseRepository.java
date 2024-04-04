package com.demo.domain.repository.rds.college;

import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.repository.rds.college.logic.CollegeCourseRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeCourseRepository extends JpaRepository<CollegeCourse, Long>, CollegeCourseRepositorySupport {
    // ...
}