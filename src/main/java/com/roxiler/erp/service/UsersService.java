package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserSignupDto;
import com.roxiler.erp.dto.users.CreateUsersDto;
import com.roxiler.erp.dto.users.UpdateUserDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UsersService {

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

    @RequiredPermission(permission = PermissionConstants.USERS)
    public Iterable<Users> getAllUsers() {
        Iterable<Users> users = usersRepository.findAll();

//        for (Users user : users) {
//            if (user.getDepartment() != null) {
//                Optional<Department> dept = departmentRepository.findById(user.getDepartment().getId());
//                if (dept.isPresent()) {
//                    user.setDepartment(dept.get());
//                }
//            }
//
//            if (user.getDesignation() != null) {
//                Optional<Designation> desg = designationRepository.findById(user.getDesignation().getId());
//                if (desg.isPresent()) {
//                    user.setDesignation(desg.get());
//                }
//            }
//        }

        return users;
    }


    @RequiredPermission(permission = PermissionConstants.USERS)
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

        UserOrganizationRole userOrganizationRole = userOrganizationRoleService.createUserOrgRole(savedUser, organization.get(), user.getRoleId());
        savedUser.getUserOrganizationRole().add(userOrganizationRole);
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setUser(savedUser);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        savedUser.setUserProfile(savedProfile);
        usersRepository.save(savedUser);
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
    public void deleteUser(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> user = usersRepository.findById(id);

        if (user.isPresent()) {
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
}
