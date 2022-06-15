package com.rioa.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rioa.Pojo.User;
import com.rioa.Pojo.UserDTO;
import com.rioa.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(
                () -> new UsernameNotFoundException(username + "Not Found.")
        );

        return user.map(UserDetailsImpl::new).get();
    }

    public User save(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setNickname((userDTO.getUsername() + Math.random() * 1000).toString().substring(0, 10));
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        user.setRoles("ROLE_USER");
        return userRepository.save(user);
    }
}
