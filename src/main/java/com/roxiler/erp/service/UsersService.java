package com.roxiler.erp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.OauthCredentialsDto;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.auth.UserSignupDto;
import com.roxiler.erp.dto.users.*;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UsersService {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${forget-password-url}")
    private String forgetPasswordURL;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserOrganizationRoleService userOrganizationRoleService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;


    @Transactional
    public Users userSignUp(UserSignupDto user) {
        System.out.println("\n\nNEW USER\n\n" + user + "\n");

        Users newUser = new Users();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hashedPassword);

        Users savedUser = usersRepository.save(newUser);
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setUser(savedUser);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        savedUser.setUserProfile(savedProfile);
        usersRepository.save(savedUser);
        System.out.println("\n\nNEW USER\n\n" + savedUser + "\n");
        return savedUser;
    }

    @Transactional
    public Users userSignUpViaOauth(OauthCredentialsDto oauthCredentialsDto) {
        DecodedJWT decoded = JWT.decode(oauthCredentialsDto.getAccessToken());
        String name = decoded.getClaim("name").asString();
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        String username = "";
        String email = "";
        if ("google".equals(oauthCredentialsDto.getOauthClient())) {
            username = decoded.getClaim("email").asString();
            email = decoded.getClaim("email").asString();
        } else if ("outlook".equals(oauthCredentialsDto.getOauthClient())) {
            username = decoded.getClaim("preferred_username").asString();
            email = decoded.getClaim("preferred_username").asString();
        }
        String password = "A2f9R7sGvNtE1DpYw";

        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            if ("google".equals(oauthCredentialsDto.getOauthClient())) {
                existingUser.get().setGoogleId(decoded.getSubject());
            } else if ("outlook".equals(oauthCredentialsDto.getOauthClient())) {
                existingUser.get().setOutlookId(decoded.getSubject());
            }
            usersRepository.save(existingUser.get());
            return existingUser.get();
        }

        Users newUser = new Users();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);
        if ("google".equals(oauthCredentialsDto.getOauthClient())) {
            newUser.setGoogleId(decoded.getSubject());
        } else if ("outlook".equals(oauthCredentialsDto.getOauthClient())) {
            newUser.setOutlookId(decoded.getSubject());
        }

        Users savedUser = usersRepository.save(newUser);
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userProfile.setUser(savedUser);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        savedUser.setUserProfile(savedProfile);
        usersRepository.save(savedUser);
        System.out.println("\n\nNEW USER\n\n" + savedUser + "\n");
        return savedUser;
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Iterable<Users> getAllUsers() {
        Iterable<Users> users = usersRepository.findAll();

        return users;
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Map<String, Object> getAllUsersWithPagination(UserDto userDto, Integer pageNum, Integer pageSize, String sortName, String sortOrder, String search) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
        if (org.isEmpty()) {
            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
        }
        Pageable pageable = PageRequest.of(
                pageNum - 1,
                pageSize);
//                listUsersDto.getSortOrder().equals("asc") ? Sort.by(Sort.Direction.ASC) : Sort.by(Sort.Direction.DESC));
        Page<Users> users = usersRepository.getUsersListWithOrg(org.get(), search.toLowerCase(), pageable);
        List<Map<String, Object>> usersList = new ArrayList<>();
        for (Users user : users) {
            Map<String, Object> userObj = new HashMap<>();
            userObj.put("user", user);
            if (user.getReportingManager() != null) {
                Optional<Users> reportingManager = usersRepository.findById(user.getReportingManager().getId());
                if (reportingManager.isPresent()) {
                    String rmName = String.format("%s %s", reportingManager.get().getFirstName(), reportingManager.get().getLastName());
                    userObj.put("reportingManager", rmName);
                }
            }
            usersList.add(userObj);
        }
        Map<String, Object> allData = new HashMap<>();
        allData.put("data", usersList);
        allData.put("totalElements", users.getTotalElements());
        allData.put("pageNumber", users.getPageable().getPageNumber());
        return allData;
    }

    public List<Map<String, Object>> getNewJoinees(UserDto userDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
        if (org.isEmpty()) {
            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
        }
        Pageable pageable = PageRequest.of(0, 5);
//                listUsersDto.getSortOrder().equals("asc") ? Sort.by(Sort.Direction.ASC) : Sort.by(Sort.Direction.DESC));
        Page<Users> users = usersRepository.getNewUsersListWithOrg(org.get(), pageable);
        List<Map<String, Object>> usersList = new ArrayList<>();
        for (Users user : users) {
            Map<String, Object> userObj = new HashMap<>();
            String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
            userObj.put("fullName", fullName);
            userObj.put("designation", user.getDesignation().getName());
            userObj.put("id", user.getId());
            usersList.add(userObj);
        }
        return usersList;
    }


    @RequiredPermission(permission = PermissionConstants.USERS)
    @Transactional
    public Users saveUser(CreateUsersDto user, String email) {

        //Department dept = departmentService.getDepartmentById(user.getDepartmentId().getId());
        //Designation desg = designationService.getDesignationById(user.getDesignationId().getId());
        Users adminUser = usersRepository.readByEmail(email);
        Optional<Organization> organization = organizationRepository.findById(adminUser.getOrganization().getId());
        if (organization.isEmpty()) {
            throw new EntityNotFoundException("Organization doesn't exist");
        }
        Optional<Users> existingUser = usersRepository.findByUsernameAndActiveOrganization(user.getUsername(), organization.get().getId());
        if (existingUser.isPresent()) {
            throw new EntityExistsException("A user with the given username is already present within the organization");
        }
        //Organization organization = organizationRepository.readById(adminUser.getOrganization().getId());
        //Optional<Department> department = departmentRepository.findById(user.getDeptId());
        Optional<Department> department = departmentRepository.getDeptWithOrg(user.getDeptId(), organization.get());
        //Optional<Designation> designation = designationRepository.findById(user.getDesgId());
        Optional<Designation> designation = designationRepository.getDesgWithOrg(user.getDesgId(), organization.get());
        //user.setDepartmentId(dept);
        //user.setDesignationId(desg);
        Users newUser = new Users();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hashedPassword);
//        if (organization.isPresent()) {
        newUser.setOrganization(organization.get());
        newUser.setActiveOrganization(organization.get().getId());
        organization.get().getUsers().add(newUser);
//        } else {
//            throw new EntityNotFoundException("Organization doesn't exist");
//        }

        if (department.isPresent()) {
            System.out.println("\n DEPARTMENT: \n" + department.get().getName());
            newUser.setDepartment(department.get());
            department.get().getUsers().add(newUser);
        } else {
            throw new EntityNotFoundException("Department doesn't exist");
        }

        if (designation.isPresent()) {
            System.out.println("\n DESIGNATION: \n" + designation.get());
            newUser.setDesignation(designation.get());
            designation.get().getUsers().add(newUser);
        } else {
            throw new EntityNotFoundException("Designation doesn't exist");
        }

        Optional<UserRole> userRole = userRoleRepository.findById(user.getRoleId());
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException("Role doesn't exist");
        }

        Users savedUser = usersRepository.save(newUser);
        organizationRepository.save(organization.get());
        departmentRepository.save(department.get());
        designationRepository.save(designation.get());
        leaveService.createLeaveOnUserCreation(savedUser);
        UserOrganizationRole userOrganizationRole = userOrganizationRoleService.createUserOrgRole(savedUser, organization.get(), user.getRoleId());
        savedUser.getUserOrganizationRole().add(userOrganizationRole);
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setUser(savedUser);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        savedUser.setUserProfile(savedProfile);
        savedUser.setReportingManager(adminUser);
        usersRepository.save(savedUser);

        String recipient = user.getEmail();
        String subject = "Account Credentials";
        Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("password", user.getPassword());
        String htmlContent = templateEngine.process("account-credentials", ctx);

        try {
            emailService.sendEmail(recipient, subject, htmlContent);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println(e.getStackTrace());
        }
        return savedUser;
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Users updateUser(UpdateUserDto user, Integer id) {


        Optional<Users> userToUpdate = usersRepository.findById(id);

        userToUpdate.ifPresent(u -> {
            Optional<Department> department = departmentRepository.findById(user.getDeptId());
            Optional<Designation> designation = designationRepository.findById(user.getDesgId());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            //u.setEmail(user.getEmail());
            u.setUsername(user.getUsername());

            if (department.isPresent()) {
                System.out.println("\n DEPARTMENT: \n" + department.get().getName());
                u.setDepartment(department.get());
                department.get().getUsers().add(u);
            } else {
                throw new EntityNotFoundException("Department doesn't exist");
            }

            if (designation.isPresent()) {
                System.out.println("\n DESIGNATION: \n" + designation.get());
                u.setDesignation(designation.get());
                designation.get().getUsers().add(u);
            } else {
                throw new EntityNotFoundException("Designation doesn't exist");
            }
        });

        if (userToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Users updatedUser = usersRepository.save(userToUpdate.get());

        return updatedUser;
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public void deleteUser(Integer id, UserDto userDto) {

        if (Objects.equals(userDto.getId(), id)) {
            throw new AuthorizationServiceException("You cannot delete your own account");
        }
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> user = usersRepository.findById(id);

        if (user.isPresent()) {
            if (!Objects.equals(user.get().getOrganization().getId(), userDto.getOrgId())) {
                throw new AuthorizationServiceException("You are not authorised to perform this action");
            }
            Optional<Department> department = departmentRepository.findById(user.get().getDepartment().getId());
            Optional<Designation> designation = designationRepository.findById(user.get().getDesignation().getId());
            Optional<Organization> organization = organizationRepository.findById(user.get().getOrganization().getId());

            if (department.isPresent()) {
                department.get().getUsers().remove(user.get());
                departmentRepository.save(department.get());
            }

            if (designation.isPresent()) {
                designation.get().getUsers().remove(user.get());
                designationRepository.save(designation.get());
            }

            if (organization.isPresent()) {
                organization.get().getUsers().remove(user.get());
                organizationRepository.save(organization.get());
            }

            usersRepository.softDeleteById(id, deletedBy);
        }
    }

    //    @RequiredPermission(permission = PermissionConstants.USERS)
    public void changeUserPassword(UserDto userDto, ChangePasswordDto changePasswordDto) throws Exception {
        Optional<Users> user = usersRepository.findById(userDto.getId());
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        if (!Objects.equals(changePasswordDto.getNewPassword(), changePasswordDto.getConfirmNewPassword())) {
            throw new Exception("New password and Confirm new password should be same");
        }

        if (
                passwordEncoder.matches(changePasswordDto.getOldPassword(), user.get().getPassword()) ||
                        passwordEncoder.matches("A2f9R7sGvNtE1DpYw", user.get().getPassword())
        ) {
            String hashedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
            user.get().setPassword(hashedPassword);
            usersRepository.save(user.get());
        } else {
            throw new AuthorizationServiceException("You are now allowed to perform this action");
        }
    }

    //    @RequiredPermission(permission = PermissionConstants.USERS)
    public void forgotUserPassword(ForgotPasswordDto forgotPasswordDto) {

        Optional<Users> user = usersRepository.findByEmail(forgotPasswordDto.getEmail());
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + 900000);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String token = JWT.create()
                .withClaim("email", forgotPasswordDto.getEmail())
                .withIssuer("reset-password")
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);

        user.get().setPasswordResetToken(token);
        usersRepository.save(user.get());

        // SEND EMAIL
        String recipient = forgotPasswordDto.getEmail();
        String subject = "Reset Password";
        String resetLink = String.format("%s?token=%s", forgetPasswordURL, token);
        Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("url", resetLink);
        String htmlContent = templateEngine.process("forget-password", ctx);

        try {
            emailService.sendEmail(recipient, subject, htmlContent);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println(e.getStackTrace());
        }
    }

    //    @RequiredPermission(permission = PermissionConstants.USERS)
    public void resetUserPassword(ResetPasswordDto resetPasswordDto) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decoded = verifier.verify(resetPasswordDto.getToken());
        String email = decoded.getClaim("email").asString();

        Optional<Users> user = usersRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        if (!Objects.equals(user.get().getPasswordResetToken(), resetPasswordDto.getToken())) {
            throw new RuntimeException("The token is no longer valid");
        }

        if (!Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmNewPassword())) {
            throw new Exception("New password and Confirm new password should be same");
        }

        String hashedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
        user.get().setPassword(hashedPassword);
        user.get().setPasswordResetToken(null);
        usersRepository.save(user.get());

        String recipient = user.get().getEmail();
        String subject = "Reset Password";
        Context ctx = new Context(LocaleContextHolder.getLocale());
        String htmlContent = templateEngine.process("reset-password", ctx);

        try {
            emailService.sendEmail(recipient, subject, htmlContent);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Users assignReportingManager(UserDto userDto, AssignReportingManagerDto assignReportingManagerDto) {
        if (Objects.equals(assignReportingManagerDto.getReportingManagerId(), assignReportingManagerDto.getUserId())) {
            throw new AuthorizationServiceException("User cannot be it's own reporting manager");
        }

        Optional<Users> user = usersRepository.findById(assignReportingManagerDto.getUserId());
        Optional<Users> reportingManager = usersRepository.findById(assignReportingManagerDto.getReportingManagerId());

        if (user.isEmpty() || reportingManager.isEmpty()) {
            throw new EntityNotFoundException("User or reporting manager not found");
        }

        if (!Objects.equals(user.get().getActiveOrganization(), userDto.getOrgId()) || !Objects.equals(reportingManager.get().getActiveOrganization(), userDto.getOrgId())) {
            throw new AuthorizationServiceException("You are not allowed to perform this action due to organization mismatch");
        }

//        user.get().setReportingManagerId(assignReportingManagerDto.getReportingManagerId());
        user.get().setReportingManager(reportingManager.get());
        usersRepository.save(user.get());

        return user.get();
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Users removeReportingManager(UserDto userDto, RemoveReportingManagerDto removeReportingManagerDto) {
        Optional<Users> user = usersRepository.findById(removeReportingManagerDto.getUserId());
        ;

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User or reporting manager not found");
        }

        if (!Objects.equals(user.get().getActiveOrganization(), userDto.getOrgId())) {
            throw new AuthorizationServiceException("You are not allowed to perform this action due to organization mismatch");
        }

//        user.get().setReportingManagerId(null);
        user.get().setReportingManager(null);
        usersRepository.save(user.get());

        return user.get();
    }
}
