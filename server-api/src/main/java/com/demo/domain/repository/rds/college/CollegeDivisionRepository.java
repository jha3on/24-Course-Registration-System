package com.demo.domain.repository.rds.college;

import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.repository.rds.college.logic.CollegeDivisionRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeDivisionRepository extends JpaRepository<CollegeDivision, Long>, CollegeDivisionRepositorySupport {
    // ...
}