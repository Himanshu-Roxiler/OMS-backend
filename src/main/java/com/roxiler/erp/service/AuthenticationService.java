package com.roxiler.erp.service;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.CredentialsDto;
import com.roxiler.erp.dto.auth.OauthCredentialsDto;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UserOrganizationRoleRepository;
import com.roxiler.erp.repository.UserRoleRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Value("${spring.oauth.google.clientId}")
    private String googleClientId;

    @Value("${spring.oauth.outlook.clientId}")
    private String outlookClientId;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    @Autowired
    private UsersService usersService;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto authenticate(CredentialsDto credentialsDto) {
        Users user = usersRepository.readByEmail(credentialsDto.getEmail());
        // String encodedMasterPassword =
        // passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getActiveOrganization(), "login",
                    "token");
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDto oauthAuthentication(OauthCredentialsDto oauthCredentialsDto) {
        DecodedJWT decoded = JWT.decode(oauthCredentialsDto.getAccessToken());

        if (Objects.equals(oauthCredentialsDto.getOauthClient(), "google")) {
            UserDto user = findByGoogleId(oauthCredentialsDto);
            return user;
        } else if (Objects.equals(oauthCredentialsDto.getOauthClient(), "outlook")) {
            UserDto user = findByOutlookId(oauthCredentialsDto);
            return user;
        }
        throw new RuntimeException("Authentication invalid");
    }

    public UserDto findByLogin(String login, String email) {
        Users user = usersRepository.readByEmail(email);
        if ("login".equals(login)) {
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getActiveOrganization(), "login",
                    "token");
        }
        throw new RuntimeException("Invalid login");
    }

    public UserDto findByGoogleId(OauthCredentialsDto oauthCredentialsDto) {
        DecodedJWT decoded = JWT.decode(oauthCredentialsDto.getAccessToken());
        String aud = decoded.getAudience().get(0);
        System.out.println("\nAUDIENCE: " + aud);
        System.out.printf("\nGOOGLE CLIENT ID: " + googleClientId);
        String subject = decoded.getSubject();
        String email = decoded.getClaim("email").asString();
        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            usersService.userSignUpViaOauth(oauthCredentialsDto);
        } else {
            existingUser.get().setGoogleId(subject);
            usersRepository.save(existingUser.get());
        }
        Users user = usersRepository.readByGoogleId(subject);
        if (googleClientId.equals(aud)) {
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getActiveOrganization(), "login",
                    "token");
        }
        throw new RuntimeException("Invalid login");
    }

    public UserDto findByOutlookId(OauthCredentialsDto oauthCredentialsDto) {
        DecodedJWT decoded = JWT.decode(oauthCredentialsDto.getAccessToken());
        String aud = decoded.getAudience().get(0);
        String subject = decoded.getSubject();
        String email = decoded.getClaim("email").asString();
        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            usersService.userSignUpViaOauth(oauthCredentialsDto);
        } else {
            existingUser.get().setOutlookId(subject);
            usersRepository.save(existingUser.get());
        }
        Users user = usersRepository.readByOutlookId(subject);
        if (outlookClientId.equals(aud)) {
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getActiveOrganization(), "login",
                    "token");
        }
        throw new RuntimeException("Invalid login");
    }

    public Map<String, Object> getUsersRoles(UserDto userDto) {
        Optional<Users> optionalUser = usersRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (user.getOrganization() != null) {

                Optional<Organization> optionalOrganization = organizationRepository
                        .findById(user.getOrganization().getId());
                if (optionalOrganization.isPresent()) {
                    Iterable<UserRole> roles = userOrganizationRoleRepository.findUserRoleFromUserOrg(user,
                            optionalOrganization.get());
                    Map<String, Object> userRoleMap = new HashMap<>();
                    userRoleMap.put("user", userDto);
                    List<Object> customRoles = new ArrayList<>();
                    for (UserRole role : roles) {
                        Map<String, Object> userRole = new HashMap<>();
                        userRole.put("name", role.getName());
                        userRole.put("id", role.getId());
                        userRole.put("features", role.getFeatures());
                        customRoles.add(userRole);
                    }
                    userRoleMap.put("roles", customRoles);
                    // userRoleMap.put("designation", user.getDesignation());
                    // userRoleMap.put("department", user.getDepartment());

                    return userRoleMap;

                } else {
                    throw new EntityNotFoundException("Organization is not available");
                }
            } else {

                Map<String, Object> userRoleMap = new HashMap<>();
                userRoleMap.put("user", userDto);
                List<Map<String, Object>> roles = new ArrayList<>();
                UserRole role = userRoleRepository.readByName(PermissionConstants.ADMIN);
                Map<String, Object> userRole = new HashMap<>();
                userRole.put("name", role.getName());
                userRole.put("id", role.getId());
                userRole.put("features", role.getFeatures());
                roles.add(userRole);
                userRoleMap.put("roles", roles);

                return userRoleMap;
            }
        } else {
            throw new EntityNotFoundException("User is not available");
        }
    }
}
