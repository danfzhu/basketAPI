package com.blackbean.api.controller;

import com.blackbean.api.entity.UserEntity;
import com.blackbean.api.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping(value="/{id}")
    private UserEntity findbyId (@PathVariable("id") int id){
        return userRepository.findById(id);
    };


}
