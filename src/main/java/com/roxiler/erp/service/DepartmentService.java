package com.roxiler.erp.service;

import com.roxiler.erp.dto.department.CreateDepartmentDto;
import com.roxiler.erp.dto.department.UpdateDepartmentDto;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.ResponseObject;
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

    public Department saveDepartment(CreateDepartmentDto department, Integer orgId) {

        Department dept = new Department();
        Optional<Organization> org = organizationRepository.findById(orgId);
        if(org.isPresent()) {
            Organization organization = org.get();
            dept.setOrganization(organization);
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
            departmentRepository.save(dept);
            organization.getDepartments().add(dept);
            organizationRepository.save(organization);

            return dept;
        } else {
            throw new EntityNotFoundException("Organization associated with user not found");
        }
    }

    public Department updateDepartment(UpdateDepartmentDto department, Integer id) {


        Optional<Department> deptToUpdate = departmentRepository.findById(id);

        deptToUpdate.ifPresent(dept -> {
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
        });

        if(deptToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Department updatedDepartment = departmentRepository.save(deptToUpdate.get());

        return updatedDepartment;
    }

    public void deleteDepartment(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Department> dept = departmentRepository.findById(id);

        if(dept.isPresent()) {
            Optional<Organization> organization = organizationRepository.findById(dept.get().getOrganization().getId());

            if(organization.isPresent()) {
                organization.get().getDepartments().remove(dept.get());
                organizationRepository.save(organization.get());
            }

            departmentRepository.softDeleteById(id, deletedBy);
        }
    }

    public Department getDepartmentById(Integer id) {

        Optional<Department> dept = departmentRepository.findById(id);
        if(dept.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return dept.get();
    }
}
