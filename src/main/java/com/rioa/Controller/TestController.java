package com.rioa.Controller;

import com.rioa.Pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping ("/test")
    public String test(Authentication authentication) throws UsernameNotFoundException {
        return "Hello World!!!" + authentication.getName();
    }
}
