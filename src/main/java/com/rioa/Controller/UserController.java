package com.rioa.Controller;

import com.rioa.Pojo.Task;
import com.rioa.Pojo.User;
import com.rioa.dao.TaskRepository;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("current_user")
    public User currentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();
        user.setPassword("");
        return user;
    }

    //post user avatar
    @PostMapping("/avatar")
    public String postAvatar(@RequestParam("image") MultipartFile multipartFile,
                             Authentication authentication) throws IOException {
        User user = userRepository.findByUsername(authentication.getName()).get();
        System.out.println(multipartFile.getContentType());
        user.setAvatar(multipartFile.getBytes());
        userRepository.save(user);
        return "success";
    }

    //get user avatar
    @GetMapping("/avatar")
    public byte[] getAvatar(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();

        if (user.getAvatar() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found");
        }
        return user.getAvatar();
    }

}
