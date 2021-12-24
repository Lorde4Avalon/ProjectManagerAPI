package com.rioa.Controller;

import com.rioa.Pojo.User;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("login")
    public User login(Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName()).get();
        user.setPassword("");
        return user;
    }

}
