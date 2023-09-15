package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.users.*;
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
@RequestMapping("/v1/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUsers(
            @AuthenticationPrincipal UserDto userDto,
//            @RequestBody ListUsersDto listUsersDto
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortName", defaultValue = "id") String sortName,
            @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {

        Iterable<Users> users = usersService.getAllUsersWithPagination(userDto, pageNum, pageSize, sortName, sortOrder, search);
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

    @PostMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody ChangePasswordDto changePasswordDto
    ) throws Exception {
        usersService.changeUserPassword(userDto, changePasswordDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully changed password");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseObject> forgotPassword(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody ForgotPasswordDto forgotPasswordDto
    ) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Link to reset password sent successfully");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody ResetPasswordDto resetPasswordDto
    ) throws Exception {
        usersService.resetUserPassword(userDto, resetPasswordDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully reset password");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/assign-reporting-manager")
    public ResponseEntity<ResponseObject> assignReportingManager(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody AssignReportingManagerDto assignReportingManagerDto
    ) throws Exception {

        Users user = usersService.assignReportingManager(userDto, assignReportingManagerDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully assigned reporting manager");
        responseObject.setData(user);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/remove-reporting-manager")
    public ResponseEntity<ResponseObject> removeReportingManager(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody RemoveReportingManagerDto removeReportingManagerDto
    ) throws Exception {

        Users user = usersService.removeReportingManager(userDto, removeReportingManagerDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully removed reporting manager");
        responseObject.setData(user);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
