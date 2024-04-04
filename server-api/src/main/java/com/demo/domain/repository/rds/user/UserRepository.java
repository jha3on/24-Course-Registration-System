package com.demo.domain.repository.rds.user;

import com.demo.domain.entity.user.User;
import com.demo.domain.repository.rds.user.logic.UserRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositorySupport {
    // ...
}