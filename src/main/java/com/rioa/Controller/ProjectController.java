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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        projectRepository.save(project);
        return Collections.singletonMap("id", project.getProjectId());
    }

    @PutMapping("/update/{id}")
    public void updateProject(@Valid @RequestBody Project project, @PathVariable Long id,
                              Authentication authentication){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        Project oldProject = projectRepository.findByProjectId(id).get();

        if(!oldProject.getProjectManager().equals(userRepository.findByUsername(authentication.getName()).get())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this project");
        }

        oldProject.copyOf(project);
        oldProject.setProjectUpdatedDate(LocalDateTime.now());
        projectRepository.save(oldProject);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id, Authentication authentication){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser) || !project.getUsers().contains(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get this project");
        }
        return projectRepository.findByProjectId(id).get();
    }

    //get all isolate
    @GetMapping("/get/all")
    public List<Project> getAllProject(@RequestParam String username,
                                       Authentication authentication) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        return projectRepository.findAllByProjectManagerOrUsers(authenticatedUser, authenticatedUser);
    }

    //generate invite code
    @GetMapping("/get/invite/generate/{id}")
    public String getInviteCode(@PathVariable Long id, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get this project");
        }

        return project.getInviteCode();
    }

    //invite user
    @PostMapping("/get/invite/{id}")
    public void inviteUser(@PathVariable Long id, @RequestParam String username, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the project manager");
        }

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invite user not found");
        }

        User user = userRepository.findByUsername(username).get();
        project.getUsers().add(user);
        projectRepository.save(project);
    }

    //get all users in project not including project manager
    @GetMapping("/get/users/{id}")
    public List<User> getUsers(@PathVariable Long id, Authentication authentication) {
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();
        Project project = projectRepository.findByProjectId(id).get();

        if (!project.getProjectManager().equals(authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the project manager");
        }

        return project.getUsers();
    }

}
