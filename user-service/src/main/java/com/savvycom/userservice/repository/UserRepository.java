package com.savvycom.userservice.repository;

import com.savvycom.userservice.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    List<User> findByIdIn(List<Long> ids);

    Page<User> findByNameLike(String name, Pageable pageable);

    Long countUserByActive(boolean active);

}
