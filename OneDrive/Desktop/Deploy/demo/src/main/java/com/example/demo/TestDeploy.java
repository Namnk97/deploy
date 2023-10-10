package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestDeploy {

    @GetMapping("/hello/deploy")
    public String voice() {
        return "Đang tập deploy :)))))";
    }
}
