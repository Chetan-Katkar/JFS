package com.example.springbootproject1.service;

import com.example.springbootproject1.bean.UserConfig;

public class UserService {
    private final UserConfig userConfig;

    public UserService(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }
}
