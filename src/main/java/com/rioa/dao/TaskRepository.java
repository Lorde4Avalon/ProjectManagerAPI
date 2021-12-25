package com.rioa.dao;

import com.rioa.Pojo.Task;
import com.rioa.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);
    List<Task> findAllByUser(User user);
    List<Task> findAllByUsersContainsOrUser(User user, User user2);
}
