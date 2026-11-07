package com.example.springaop.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
        private String address;

    }

    private User user;

    public UserService() {
        user = new User("chetan katkar", 21, "mumbai, India");
    }
    public void logIn() {
        System.out.println("Logging user in");
    }

    public void logOut() throws Exception {
        System.out.println("Logging out user");
        throw new Exception("unable to logout user");
    }
}
