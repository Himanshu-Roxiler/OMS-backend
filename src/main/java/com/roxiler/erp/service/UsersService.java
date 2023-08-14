package com.roxiler.erp.service;

import com.roxiler.erp.dto.users.CreateUsersDto;
import com.roxiler.erp.dto.users.UpdateUserDto;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    public Iterable<Users> getAllUsers() {
        Iterable<Users> users = usersRepository.findAll();

        for(Users user: users) {
            Optional<Department> dept = departmentRepository.findById(user.getDepartment().getId());
            Optional<Designation> desg = designationRepository.findById(user.getDesignation().getId());
            if(dept.isPresent()) {
                user.setDepartment(dept.get());
            }
            if(desg.isPresent()) {
                user.setDesignation(desg.get());
            }
        }

        return users;
    }

    public Users saveUser(CreateUsersDto user) {

        //Department dept = departmentService.getDepartmentById(user.getDepartmentId().getId());
        //Designation desg = designationService.getDesignationById(user.getDesignationId().getId());
        Optional<Organization> organization = organizationRepository.findById(user.getOrgId());
        Optional<Department> department = departmentRepository.findById(user.getDeptId());
        Optional<Designation> designation = designationRepository.findById(user.getDesgId());
        //user.setDepartmentId(dept);
        //user.setDesignationId(desg);
        Users newUser = new Users();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        if(organization.isPresent()) {
            newUser.setOrganization(organization.get());
            newUser.setActiveOrganization(organization.get().getId());
            organization.get().getUsers().add(newUser);
        } else {
            throw new EntityNotFoundException("Organization doesn't exist");
        }

        if(department.isPresent()) {
            System.out.println("\n DEPARTMENT: \n" + department.get().getName());
            newUser.setDepartment(department.get());
            department.get().getUsers().add(newUser);
        } else {
            throw new EntityNotFoundException("Department doesn't exist");
        }

        if(designation.isPresent()) {
            System.out.println("\n DESIGNATION: \n" + designation.get());
            newUser.setDesignation(designation.get());
            designation.get().getUsers().add(newUser);
        } else {
            throw new EntityNotFoundException("Designation doesn't exist");
        }

        Users savedUser = usersRepository.save(newUser);
        organizationRepository.save(organization.get());
        departmentRepository.save(department.get());
        designationRepository.save(designation.get());

        System.out.println("USER DEPARTMENT: " + savedUser.getDepartment().getName());
        return savedUser;
    }

    public Users updateUser(UpdateUserDto user, Integer id) {


        Optional<Users> userToUpdate = usersRepository.findById(id);

        userToUpdate.ifPresent(u -> {
            Optional<Department> department = departmentRepository.findById(user.getDeptId());
            Optional<Designation> designation = designationRepository.findById(user.getDesgId());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());
            u.setUsername(user.getUsername());

            if(department.isPresent()) {
                System.out.println("\n DEPARTMENT: \n" + department.get().getName());
                u.setDepartment(department.get());
                department.get().getUsers().add(u);
            } else {
                throw new EntityNotFoundException("Department doesn't exist");
            }

            if(designation.isPresent()) {
                System.out.println("\n DESIGNATION: \n" + designation.get());
                u.setDesignation(designation.get());
                designation.get().getUsers().add(u);
            } else {
                throw new EntityNotFoundException("Designation doesn't exist");
            }
        });

        if(userToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Users updatedUser = usersRepository.save(userToUpdate.get());

        return updatedUser;
    }

    public void deleteUser(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> user = usersRepository.findById(id);

        if(user.isPresent()) {
            Optional<Department> department = departmentRepository.findById(user.get().getDepartment().getId());
            Optional<Designation> designation = designationRepository.findById(user.get().getDesignation().getId());
            Optional<Organization> organization = organizationRepository.findById(user.get().getOrganization().getId());

            if(department.isPresent()) {
                department.get().getUsers().remove(user.get());
                departmentRepository.save(department.get());
            }

            if(designation.isPresent()) {
                designation.get().getUsers().remove(user.get());
                designationRepository.save(designation.get());
            }

            if(organization.isPresent()) {
                organization.get().getUsers().remove(user.get());
                organizationRepository.save(organization.get());
            }

            usersRepository.softDeleteById(id, deletedBy);
        }
    }
}
