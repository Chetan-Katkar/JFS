package com.example.springbootproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:application.xml")
public class SpringBootProject1Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject1Application.class, args);
    }
}
