package com.codewithmosh.store.feature.users.service;

import com.codewithmosh.store.feature.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUsersByEmail(String email);

    Optional<User> findByEmail(String email);
}
