package com.rioa.dao;

import com.rioa.Pojo.Project;
import com.rioa.Pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectId(Long projectId);
    List<Project> findAllByProjectManagerOrUsers(User manager, User user);
    Optional<Project> findByInviteCode(String inviteCode);
    List<Project> findAllByProjectManager(User manager);
}
