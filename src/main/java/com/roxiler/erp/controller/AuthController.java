package com.roxiler.erp.controller;

import java.net.URI;

import com.roxiler.erp.config.UserAuthenticationProvider;
import com.roxiler.erp.dto.auth.OauthCredentialsDto;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.auth.UserSignupDto;
import com.roxiler.erp.dto.users.CreateUsersDto;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.service.UsersService;
import jakarta.validation.Valid;
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

    public AuthController(UsersService usersService,
                          UserAuthenticationProvider userAuthenticationProvider) {
        this.usersService = usersService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user) {
        user.setToken(userAuthenticationProvider.createToken(user.getLogin(), user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/oauth/signIn")
    public ResponseEntity<UserDto> signInViaOauth(@AuthenticationPrincipal UserDto user) {
        user.setToken(userAuthenticationProvider.createToken(user.getLogin(), user));
        return ResponseEntity.ok(user);
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