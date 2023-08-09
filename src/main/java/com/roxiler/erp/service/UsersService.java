package com.roxiler.erp.service;

import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private DepartmentService departmentService;

    public Iterable<Users> getAllUsers() {
        Iterable<Users> users = usersRepository.findAll();

        return users;
    }

    public Users saveUser(Users user, Organization org) {

        Department dept = departmentService.getDepartmentById(user.getDepartmentId().getId());
        Designation desg = designationService.getDesignationById(user.getDesignationId().getId());
        user.setDepartmentId(dept);
        user.setDesignationId(desg);
        user.setOrganization(org);
        user.setActiveOrganization(org.getId());
        Users users = usersRepository.save(user);

        return user;
    }

    public String updateUser(Users user, Integer id) {


        Optional<Users> userToUpdate = usersRepository.findById(id);

        userToUpdate.ifPresent(u -> {
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setEmail(user.getEmail());
            u.setUsername(user.getUsername());
            u.setDepartmentId(user.getDepartmentId());
            u.setDesignationId(user.getDesignationId());
        });

        if(userToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Users updatedUser = usersRepository.save(userToUpdate.get());

        return "User updated successfully";
    }

    public String deleteUser(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        usersRepository.softDeleteById(id, deletedBy);

        return "User deleted Successfully";
    }
}
