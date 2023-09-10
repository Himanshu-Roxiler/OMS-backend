package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.users.CreateUsersDto;
import com.roxiler.erp.dto.users.ListUsersDto;
import com.roxiler.erp.dto.users.UpdateUserDto;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUsers(
            @AuthenticationPrincipal UserDto userDto,
            @RequestBody ListUsersDto listUsersDto
    ) {

        Iterable<Users> users = usersService.getAllUsersWithPagination(userDto, listUsersDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched users");
        responseObject.setData(users);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addUser(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody CreateUsersDto user
    ) {
        Users newUser = usersService.saveUser(user, userDto.getEmail());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created user");
        responseObject.setData(newUser);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@Valid @RequestBody UpdateUserDto user, @PathVariable("id") Integer id) {

        Users updatedUser = usersService.updateUser(user, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated user");
        responseObject.setData(updatedUser);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable("id") Integer id) {

        usersService.deleteUser(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted user");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
