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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("current_user")
    public User currentUser(Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        user.setPassword("");
        return user;
    }

    @PostMapping("invite")
    public void inviteUser(@RequestParam String username,
                           @RequestParam Long taskId) {
        if (userRepository.findUserByUsername(username).isEmpty()
            || taskRepository.findById(taskId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findUserByUsername(username).get();
        Task task = taskRepository.findById(taskId).get();
        task.getUsers().add(user);
        taskRepository.save(task);
    }

    @PostMapping("invite/del")
    public void delInviteUser(@RequestParam String username,
                           @RequestParam Long taskId) {
        if (userRepository.findUserByUsername(username).isEmpty()
                || taskRepository.findById(taskId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findUserByUsername(username).get();
        Task task = taskRepository.findById(taskId).get();
        Set<User> n_users = task.getUsers();
        n_users.remove(user);
        task.setUsers(n_users);

        taskRepository.save(task);
    }


}
