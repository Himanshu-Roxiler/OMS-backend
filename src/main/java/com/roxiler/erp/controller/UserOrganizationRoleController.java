package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.UserOrganizationRole;
import com.roxiler.erp.service.UserOrganizationRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/userOrgRole")
public class UserOrganizationRoleController {

    @Autowired
    private UserOrganizationRoleService userOrganizationRoleService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal UserDto user) {

        Iterable<UserOrganizationRole> userOrganizationRoles = userOrganizationRoleService.getAll();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched users, organizations and associated roles");
        responseObject.setData(userOrganizationRoles);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }
}
