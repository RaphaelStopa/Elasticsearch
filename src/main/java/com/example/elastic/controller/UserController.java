package com.example.elastic.controller;

import com.example.elastic.domain.User;
import com.example.elastic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;

    }

    @PostMapping
    public void save(@RequestBody final User user){
        service.save(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable final Long id){
        return service.findById(id);
    }
}
