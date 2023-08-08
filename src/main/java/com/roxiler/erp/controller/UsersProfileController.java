package com.roxiler.erp.controller;

import com.roxiler.erp.model.UserProfile;
import com.roxiler.erp.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/profile")
public class UsersProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/")
    public Iterable<UserProfile> getAllUserProfiles() {

        Iterable<UserProfile> userProfiles = userProfileService.getAllUsers();

        for(UserProfile userProfile: userProfiles) {
            System.out.println("USER: " + userProfile.getFirstName() + " " + userProfile.getLastName());
        }

        return userProfiles;
    }

    @PostMapping("/")
    public UserProfile addUserProfile(@Valid @RequestBody UserProfile userProfile) {

        UserProfile newUserProfile = userProfileService.saveUser(userProfile);

        return newUserProfile;
    }

    @PatchMapping("/{id}")
    public String updateUserProfile(@Valid @RequestBody UserProfile userProfile, @PathVariable("id") Integer id) {

        String result = userProfileService.updateUser(userProfile, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteUserProfile(@PathVariable("id") Integer id) {

        String result = userProfileService.deleteUser(id);

        return result;
    }

}
