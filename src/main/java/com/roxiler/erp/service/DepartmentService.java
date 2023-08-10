package com.roxiler.erp.service;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public Iterable<Department> getAllDepartments() {
        Iterable<Department> departments = departmentRepository.findAll();

        return departments;
    }

    public Department saveDepartment(Department department, Integer orgId) {

        Optional<Organization> org = organizationRepository.findById(orgId);
        if(org.isPresent()) {
            Organization organization = org.get();
            System.out.println("\nDEPT ORG: " + organization + "\n");
            department.setOrganization(organization);
            Department dept = departmentRepository.save(department);
            System.out.println("\nDEPT: " + dept + "\n");
            organization.getDepartments().add(dept);
            System.out.println("\nORG WITH DEPT: " + organization + "\n");
            //organization.setDepartments(departments);

            //organizationRepository.save(organization);
            return dept;
        }

        return null;

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

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        departmentRepository.softDeleteById(id, deletedBy);

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
