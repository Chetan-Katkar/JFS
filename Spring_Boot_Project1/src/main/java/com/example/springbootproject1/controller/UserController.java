package com.example.springbootproject1.controller;

import com.example.springbootproject1.bean.UserConfig;
import com.example.springbootproject1.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-config")
    public UserConfig getUserConfig() {
        return userService.getUserConfig();
    }
}
