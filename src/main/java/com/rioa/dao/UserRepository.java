package com.rioa.dao;

import com.rioa.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
