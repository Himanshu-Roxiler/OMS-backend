package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.profile.CreateUserProfileDto;
import com.roxiler.erp.dto.profile.UpdateUserProfileDto;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.UserProfile;
import com.roxiler.erp.service.UserProfileService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/v1/profile")
public class UsersProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getUserProfile(@AuthenticationPrincipal UserDto userDto) {

        UserProfile userProfile = userProfileService.getUserProfile(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched user profiles");
        responseObject.setData(userProfile);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

//    @PostMapping("")
//    public ResponseEntity<ResponseObject> addUserProfile(
//            @AuthenticationPrincipal UserDto userDto,
//            @Valid @RequestBody CreateUserProfileDto userProfile
//    ) {
//        UserProfile newUserProfile = userProfileService.saveUser(userProfile, userDto.getId());
//        ResponseObject responseObject = new ResponseObject();
//        responseObject.setIs_success(true);
//        responseObject.setMessage("Successfully created user profile");
//        responseObject.setData(newUserProfile);
//        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
//
//        return response;
//    }

    @PatchMapping("")
    public ResponseEntity<ResponseObject> updateUserProfile(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody UpdateUserProfileDto userProfile,
            @PathVariable("id") Integer id
    ) {
        UserProfile updatedUserProfile = userProfileService.updateUser(userProfile, id, userDto.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated user profile");
        responseObject.setData(updatedUserProfile);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ResponseObject> deleteUserProfile(
//            @AuthenticationPrincipal UserDto userDto,
//            @PathVariable("id") Integer id
//    ) {
//        String result = userProfileService.deleteUser(id, userDto.getId());
//        ResponseObject responseObject = new ResponseObject();
//        responseObject.setIs_success(true);
//        responseObject.setMessage("Successfully deleted user profile");
//        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
//
//        return response;
//    }

}
