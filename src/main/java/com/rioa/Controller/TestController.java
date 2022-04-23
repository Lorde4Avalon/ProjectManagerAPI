package com.rioa.Controller;

import com.rioa.Pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping ("/test/admin")
    public String test(Authentication authentication){
        return "Hello ADMIN!!! " + authentication.getName();
    }

    @GetMapping ("/test/1")
    public String testInvite(Authentication authentication)  {
        return "Hi! You get the permission!!! " + authentication.getName();
    }

    @GetMapping ("/test/2")
    public String test1(Authentication authentication)  {
        return "Hi! You get the permission!!! " + authentication.getAuthorities();
    }
}
