package com.rioa.dao;

import com.rioa.Pojo.Project;
import com.rioa.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectId(Long projectId);
    //find all projects by user
    List<Project> findAllByUsers(User user);
    List<Project> findDistinctByProjectManagerOrUsers(User manager, User user);
    Optional<Project> findByInviteCode(String inviteCode);
    List<Project> findAllByProjectManager(User manager);
}
