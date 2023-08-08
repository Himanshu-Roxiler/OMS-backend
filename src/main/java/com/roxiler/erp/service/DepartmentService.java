package com.roxiler.erp.service;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Iterable<Department> getAllDepartments() {
        Iterable<Department> departments = departmentRepository.findAll();

        return departments;
    }

    public Department saveDepartment(Department department, Organization org) {
        /*The organization needs to be replaced by finding it using org repository*/
        department.setOrganization(org);
        Department departments = departmentRepository.save(department);

        return department;
    }

    public String updateDepartment(Department department, Integer id) {


        Optional<Department> deptToUpdate = departmentRepository.findById(id);

        deptToUpdate.ifPresent(dept -> {
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
        });

        if(deptToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Department updatedDepartment = departmentRepository.save(deptToUpdate.get());

        return "Department updated successfully";
    }

    public String deleteDepartment(Integer id) {

        departmentRepository.deleteById(id);

        return "Department deleted Successfully";
    }

    public Department getDepartmentById(Integer id) {
        Optional<Department> dept = departmentRepository.findById(id);

        if(dept.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return dept.get();
    }
}
