package com.rioa.Controller;

import com.rioa.Pojo.User;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/invite")
    public void inviteUser(@RequestParam String username) {
        if (userRepository.findUserByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findUserByUsername(username).get();
        user.setRoles("ROLE_USER,ROLE_GUEST");
        userRepository.save(user);
    }
}
