package com.roxiler.erp.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.roxiler.erp.config.UserAuthenticationProvider;
import com.roxiler.erp.dto.auth.OauthCredentialsDto;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.auth.UserSignupDto;
import com.roxiler.erp.dto.users.CreateUsersDto;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UserOrganizationRoleRepository;
import com.roxiler.erp.repository.UserRoleRepository;
import com.roxiler.erp.repository.UsersRepository;
import com.roxiler.erp.service.AuthenticationService;
import com.roxiler.erp.service.UsersService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1")
public class AuthController {

    private final UsersService usersService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    public AuthController(UsersService usersService,
            UserAuthenticationProvider userAuthenticationProvider) {
        this.usersService = usersService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/signIn")
    public ResponseEntity<Map<String, Object>> signIn(@AuthenticationPrincipal UserDto userDto) {
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin(), userDto));
        return ResponseEntity.ok(authenticationService.getUsersRoles(userDto));
    }

    @PostMapping("/oauth/signIn")
    public ResponseEntity<Map<String, Object>> signInViaOauth(@AuthenticationPrincipal UserDto userDto) {
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin(), userDto));
        return ResponseEntity.ok(authenticationService.getUsersRoles(userDto));
    }

    @PostMapping("/signUp")
    public ResponseEntity<ResponseObject> signUp(@RequestBody @Valid UserSignupDto user) {
        Users createdUser = usersService.userSignUp(user);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created user");
        responseObject.setData(createdUser);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PostMapping("/oauth/signUp")
    public ResponseEntity<ResponseObject> signUpViaOauth(@RequestBody @Valid OauthCredentialsDto oauthCredentialsDto) {
        Users createdUser = usersService.userSignUpViaOauth(oauthCredentialsDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created user");
        responseObject.setData(createdUser);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}