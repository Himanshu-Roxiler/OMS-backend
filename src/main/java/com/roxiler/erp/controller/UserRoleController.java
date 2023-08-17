package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.roles.CreateUserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.UserRoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllUserRoles() {

        Iterable<UserRole> UserRoles = userRoleService.getAllUserRolesIterable();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched user roles");
        responseObject.setData(UserRoles);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> addUserRole(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody CreateUserRoleDto userRoleDto
    ) {
        UserRole UserRole2 = userRoleService.saveUserRole(userRoleDto, userDto.getId(), userDto.getOrgId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created UserRole.");
        responseObject.setData(UserRole2);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUserRole(@Valid @RequestBody UserRole UserRole, @PathVariable("id") Integer id) {

        UserRole UserRole2 = userRoleService.updatUserRole(UserRole, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated UserRole");
        responseObject.setData(UserRole2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUserRole(@PathVariable("id") Integer id) {

        userRoleService.deleteUserRole(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted UserRole.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }


}
