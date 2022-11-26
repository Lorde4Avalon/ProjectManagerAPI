package com.rioa.Controller;

import com.rioa.configurer.JwtTokenUtil;
import com.rioa.Pojo.UserDTO;
import com.rioa.Service.UserDetailsServiceImpl;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin

@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/authenticate")
    public Map<String, String> generateAuthenticationToken(@Valid @RequestBody UserDTO user) throws Exception {
        authenticate(user.getUsername(), user.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        return Collections.singletonMap("token", jwtTokenUtil.generateToken(userDetails));
    }

    @PostMapping(value = "/register")
    public void registerUser(@Valid @RequestBody UserDTO userDTO) throws ResponseStatusException {
        //check if user already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        userDetailsService.save(userDTO);
    }


    //Authenticate user
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}