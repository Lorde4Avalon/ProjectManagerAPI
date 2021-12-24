package com.rioa.dao;

import com.rioa.Pojo.Task;
import com.rioa.Pojo.TaskUserRole;
import com.rioa.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskUserRoleRepository extends JpaRepository<TaskUserRole, TaskUserRole.PK> {
    Optional<TaskUserRole> findAllByUserAndTask(User user, Task task);
    List<TaskUserRole> findAllByTask(Task task);
}
