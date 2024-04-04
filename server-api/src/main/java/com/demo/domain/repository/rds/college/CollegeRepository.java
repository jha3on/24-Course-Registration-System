package com.demo.domain.repository.rds.college;

import com.demo.domain.entity.college.College;
import com.demo.domain.repository.rds.college.logic.CollegeRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long>, CollegeRepositorySupport {
    // ...
}