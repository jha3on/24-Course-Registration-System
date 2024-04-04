package com.demo.domain.repository.rds.college;

import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.repository.rds.college.logic.CollegeDepartmentRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeDepartmentRepository extends JpaRepository<CollegeDepartment, Long>, CollegeDepartmentRepositorySupport {
    // ...
}