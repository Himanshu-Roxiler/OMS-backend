package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.department.CreateDepartmentDto;
import com.roxiler.erp.dto.department.UpdateDepartmentDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Department;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    UsersRepository usersRepository;

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Iterable<Department> getAllDepartments() {
        Iterable<Department> departments = departmentRepository.findAll();

        return departments;
    }

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Department saveDepartment(CreateDepartmentDto department, Integer orgId) {

        Department dept = new Department();
        Optional<Organization> org = organizationRepository.findById(orgId);
        if (org.isPresent()) {
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

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Department updateDepartment(UpdateDepartmentDto department, Integer id, String email) {

        Users user = usersRepository.readByEmail(email);
        Optional<Department> deptToUpdate = departmentRepository.findById(id);

        deptToUpdate.ifPresent(dept -> {
            if (deptToUpdate.get().getOrganization().getId().equals(user.getActiveOrganization())) {
                dept.setName(department.getName());
                dept.setDescription(department.getDescription());
            } else {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
        });

        if (deptToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Department updatedDepartment = departmentRepository.save(deptToUpdate.get());

        return updatedDepartment;
    }

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public void deleteDepartment(Integer id, String email) {

        Users user = usersRepository.readByEmail(email);
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Department> dept = departmentRepository.findById(id);

        if (dept.isPresent()) {
            if (!Objects.equals(dept.get().getOrganization().getId(), user.getActiveOrganization())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            Optional<Organization> organization = organizationRepository.findById(dept.get().getOrganization().getId());

            if (organization.isPresent()) {
                organization.get().getDepartments().remove(dept.get());
                organizationRepository.save(organization.get());
            }

            departmentRepository.softDeleteById(id, deletedBy);
        }
    }

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Department getDepartmentById(Integer id) {

        Optional<Department> dept = departmentRepository.findById(id);
        if (dept.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return dept.get();
    }
}
