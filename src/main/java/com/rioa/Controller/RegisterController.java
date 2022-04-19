package com.rioa.Controller;

import com.rioa.Pojo.User;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public Map<String, String> registerUser(@Valid @RequestBody User user) {
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return Collections.singletonMap("username", user.getUsername());
    }
}
