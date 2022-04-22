package com.rioa.Controller;

import com.rioa.Pojo.Task;
import com.rioa.Pojo.User;
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

    @PostMapping("new")
    public Map<String, Long> addTask(@Valid @RequestBody Task task,
                       Authentication authentication) {
        User user = userRepository.findByUsername(
                                    authentication.getName()).get();
        task.setUser(user);
        taskRepository.save(task);
        return Collections.singletonMap("id", task.getTaskId());
    }

    @PutMapping("{id}")
    public void updateTask(@PathVariable Long id,
                           @Valid @RequestBody Task task,
                           Authentication authentication) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Task oldTask = taskRepository.findById(id).get();
        User user = userRepository.findByUsername(authentication.getName()).get();

        if (!(
                Objects.equals(oldTask.getUser().getUserId(), user.getUserId())
                        || oldTask.getUsers().contains(user)
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        oldTask.copyOf(task);
        taskRepository.save(oldTask);
    }

    @GetMapping("{id}")
    private Task getTaskById(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(
                authentication.getName()).get();
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var task = taskRepository.findById(id).get();
        if (!(
                Objects.equals(task.getUser().getUserId(), user.getUserId())
            || task.getUsers().contains(user)
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return task;
    }

    @GetMapping("search")
    private List<Task> getTask(@RequestParam String username,
                               Authentication authentication) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(authentication.getName()).get();

        return taskRepository.findAllByUsersContainsOrUser(user, user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void deleteTask(@PathVariable Long id,
                            Authentication authentication) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Task oldTask = taskRepository.findById(id).get();

        if (oldTask.getUser() != userRepository.findByUsername(authentication.getName()).get()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this task");
        }

        taskRepository.deleteById(id);
    }

    //get all isolate user
    @GetMapping("{id}/invites")
    public List<User> getAllInvitedUser(@PathVariable Long id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found");
        }
        Task task = taskRepository.findById(id).get();

        return new ArrayList<>(task.getUsers());
    }
}
