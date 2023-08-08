package com.roxiler.erp.controller;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public Iterable<Department> getAllDepartments() {

        Iterable<Department> departments = departmentService.getAllDepartments();

        for(Department dept: departments) {
            System.out.println("DEPARTMENT: " + dept.getName());
        }

        return departments;
    }

    @PostMapping("/")
    public Department addDepartment(@Valid @RequestBody Department department) {

        Department dept = departmentService.saveDepartment(department, new Organization());

        return dept;
    }

    @PatchMapping("/{id}")
    public String updateDepartment(@Valid @RequestBody Department department, @PathVariable("id") Integer id) {

        String result = departmentService.updateDepartment(department, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id) {

        String result = departmentService.deleteDepartment(id);

        return result;
    }

}
