package com.example.springaop.RestApis;

import com.example.springaop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String userLogin() {
        userService.logIn();
        return "successfull";
    }
}
