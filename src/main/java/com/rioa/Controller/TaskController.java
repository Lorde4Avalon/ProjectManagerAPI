package com.rioa.Controller;

import com.rioa.Pojo.Project;
import com.rioa.Pojo.Task;
import com.rioa.Pojo.User;
import com.rioa.dao.ProjectRepository;
import com.rioa.dao.TaskRepository;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/create")
    public Map<String, Long> addTask(@Valid @RequestBody Task task,
                       @RequestParam Long projectId,
                       Authentication authentication) {
        User authenticateUser = userRepository.findByUsername(
                                    authentication.getName()).get();
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        task.setTaskOwner(authenticateUser);
        task.getUsers().add(authenticateUser);
        task.setProject(project.get());
        taskRepository.save(task);
        return Collections.singletonMap("task_id", task.getTaskId());
    }

    @PutMapping("/update/{id}")
    public void updateTask(@PathVariable Long id,
                           @RequestParam Long projectId,
                           @Valid @RequestBody Task task,
                           Authentication authentication) {
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        User projectManager = projectRepository.findById(projectId).get().getProjectManager();
        User authenticateUser = userRepository.findByUsername(authentication.getName()).get();
        // Check if the user is the project manager or the task Owner
        if (!(  Objects.equals(projectManager.getUserId(), authenticateUser.getUserId()) ||
                Objects.equals(task.getTaskOwner().getUserId(), authenticateUser.getUserId())
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
        }

        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        Task oldTask = taskRepository.findById(id).get();

        oldTask.copyOf(task);
        taskRepository.save(oldTask);
    }

    @GetMapping("/get/{id}")
    private Task getTaskById(@PathVariable Long id, Authentication authentication) {
        User authenticateUser = userRepository.findByUsername(authentication.getName()).get();
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var task = taskRepository.findById(id).get();
        // Check if the user is the project manager or the task Owner or a user of the task
        if (!(
                Objects.equals(task.getTaskOwner().getUserId(), authenticateUser.getUserId()) ||
                task.getUsers().contains(authenticateUser) ||
                Objects.equals(task.getProject().getProjectManager().getUserId(), authenticateUser.getUserId())
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this task");
        }
        return task;
    }

    //get all tasks of a project
    @GetMapping("/get/{projectId}")
    private List<Task> getTaskByProjectId(@PathVariable Long projectId, Authentication authentication) {
        User authenticateUser = userRepository.findByUsername(authentication.getName()).get();
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        Project project = projectRepository.findById(projectId).get();
        // Check if the user is the project manager or a user of the project
        if (!( Objects.equals(project.getProjectManager().getUserId(), authenticateUser.getUserId()) ||
                project.getUsers().contains(authenticateUser)
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this project");
        }

        return taskRepository.findAllByProject(project);
    }

    @DeleteMapping("/delete/{id}")
    private void deleteTask(@PathVariable Long id,
                            Authentication authentication) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found");
        }
        User authenticateUser = userRepository.findByUsername(authentication.getName()).get();
        Task oldTask = taskRepository.findById(id).get();

        // Check if the user is the project manager or the task Owner
        if (!( Objects.equals(oldTask.getTaskOwner().getUserId(), authenticateUser.getUserId()) ||
                Objects.equals(oldTask.getProject().getProjectManager().getUserId(), authenticateUser.getUserId())
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this task");
        }

        taskRepository.deleteById(id);
    }

    //get all users of a task
    @GetMapping("/get/users/{id}")
    public List<User> getAllInvitedUser(@PathVariable Long id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found");
        }
        Task task = taskRepository.findById(id).get();

        return new ArrayList<>(task.getUsers());
    }
}
