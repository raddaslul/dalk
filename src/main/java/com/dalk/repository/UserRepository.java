package com.dalk.repository;

import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    List<User> findTop99ByOrderByExDesc();
    List<User> findTop3ByOrderByExDesc();
}