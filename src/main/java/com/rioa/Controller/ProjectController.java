package com.rioa.Controller;

import com.rioa.Pojo.Project;
import com.rioa.Pojo.User;
import com.rioa.dao.ProjectRepository;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public Map<String, Long> createProject(@Valid @RequestBody Project project,
                                           Authentication authentication){
        project.setProjectManager(userRepository.findByUsername(authentication.getName()).get());
        Set<User> users = project.getUsers();
        users.add(project.getProjectManager());
        project.setUsers(users);
        projectRepository.save(project);
        return Collections.singletonMap("project_id", project.getProjectId());
    }

    @PutMapping("/update/{id}")
    public void updateProject(@Valid @RequestBody Project project, @PathVariable Long id,
                              Authentication authentication){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        Project oldProject = projectRepository.findByProjectId(id).get();

        if(!oldProject.getProjectManager().equals(userRepository.findByUsername(authentication.getName()).get())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this project");
        }

        oldProject.copyOf(project);
        oldProject.setProjectUpdatedDate(LocalDateTime.now());

        // positive lock to prevent concurrent update
        if (oldProject.getProjectUpdatedDate().isBefore(projectRepository.findByProjectId(id).get().getProjectUpdatedDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project is already updated");
        } else {
            projectRepository.save(oldProject);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id, Authentication authentication){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        Project project = projectRepository.findByProjectId(id).get();
        if (!project.getProjectManager().equals(userRepository.findByUsername(authentication.getName()).get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this project");
        }
        projectRepository.deleteById(id);
    }

    @GetMapping("/get/{id}")
    public Project getProject(@PathVariable Long id, Authentication authentication){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser) || !project.getUsers().contains(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get this project");
        }
        return projectRepository.findByProjectId(id).get();
    }

    //get all isolate projects
    @GetMapping("/get/all")
    public List<Project> getAllProject(@RequestParam String username,
                                       Authentication authentication) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        return projectRepository.findAllByProjectManagerOrUsers(authenticatedUser, authenticatedUser);
    }

    //get all manage project
    @GetMapping("/get/manage")
    public List<Project> getAllManageProject(@RequestParam String username,
                                             Authentication authentication) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        return projectRepository.findAllByProjectManager(authenticatedUser);
    }

    //generate invite code
//    @GetMapping("/get/invite/generate/{id}")
//    public String getInviteCode(@PathVariable Long id, Authentication authentication) {
//        if (projectRepository.findByProjectId(id).isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
//        }
//
//        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
//        Project project = projectRepository.findByProjectId(id).get();
//
//        if (!project.getProjectManager().equals(authenticatedUser)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get this project");
//        }
//
//        return project.getInviteCode();
//    }

    //invite user
    @PostMapping("/get/invite/{id}")
    public void inviteUser(@PathVariable Long id, @RequestParam String username, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the project manager");
        }

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite user not found");
        }

        User user = userRepository.findByUsername(username).get();
        project.getUsers().add(user);
        projectRepository.save(project);
    }

    //invite user by invite code
    @PostMapping("/get/invite/code")
    public void inviteUserByInviteCode(@RequestParam String inviteCode, Authentication authentication) {
        if (projectRepository.findByInviteCode(inviteCode).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByInviteCode(inviteCode).get();



        project.getUsers().add(authenticatedUser);
        projectRepository.save(project);
    }

    //delete invite user
    @DeleteMapping("/get/invite/{id}")
    public void deleteInviteUser(@PathVariable Long id, @RequestParam String username, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the project manager");
        }

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite user not found");
        }

        User user = userRepository.findByUsername(username).get();
        project.getUsers().remove(user);
        projectRepository.save(project);
    }

    //get all users in project including project manager
    @GetMapping("/get/users/{id}")
    public Set<User> getUsers(@PathVariable Long id, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getUsers().contains(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the project manager or a user in the project");
        }

        Set<User> users = project.getUsers();

        return users;
    }

}
