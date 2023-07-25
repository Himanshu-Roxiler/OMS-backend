package com.roxiler.erp.controller;

import com.roxiler.erp.model.UserProfile;
import com.roxiler.erp.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorld {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/")
    public String sayHelloWorld() {

        Iterable<UserProfile> users = userProfileService.getAllUsers();

        for(UserProfile user: users) {
            System.out.println("USER: " + user.getFirstName() + " " + user.getLastName());
        }

        return "Hello";
    }

    @PostMapping("/")
    public UserProfile addUser(@Valid @RequestBody UserProfile userProfile) {

        UserProfile user = userProfileService.saveUser(userProfile);

        return user;
    }

    @PatchMapping("/{id}")
    public String updateUser(@Valid @RequestBody UserProfile userProfile, @PathVariable("id") Integer id) {

        String result = userProfileService.updateUser(userProfile, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {

        String result = userProfileService.deleteUser(id);

        return result;
    }

}
