package com.roxiler.erp.controller;

import com.roxiler.erp.model.Users;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/")
    public Iterable<Users> getAllDepartments() {

        Iterable<Users> users = usersService.getAllUsers();

        for(Users user: users) {
            System.out.println("DEPARTMENT: " + user.getFirstName() + " " + user.getLastName());
        }

        return users;
    }

    @PostMapping("/")
    public Users addDepartment(@Valid @RequestBody Users user) {

        Users newUser = usersService.saveUser(user, new Organization());

        return newUser;
    }

    @PatchMapping("/{id}")
    public String updateDepartment(@Valid @RequestBody Users user, @PathVariable("id") Integer id) {

        String result = usersService.updateUser(user, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id) {

        String result = usersService.deleteUser(id);

        return result;
    }

}
