package com.savvycom.userservice.repository;

import com.savvycom.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByPasswordResetToken(String passwordResetToken);

    List<User> findByIdIn(List<Long> ids);

}
