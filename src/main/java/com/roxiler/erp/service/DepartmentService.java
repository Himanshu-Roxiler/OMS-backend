package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.department.CreateDepartmentDto;
import com.roxiler.erp.dto.department.ListDepartmentDto;
import com.roxiler.erp.dto.department.UpdateDepartmentDto;
import com.roxiler.erp.dto.designation.CreateDesignationDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
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
    private UsersRepository usersRepository;

    @Autowired
    private LeaveService leaveService;

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Iterable<Department> getAllDepartments() {
        Iterable<Department> departments = departmentRepository.findAll();

        return departments;
    }

//    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
//    public Iterable<Department> getListDepartmentsFromOrg(UserDto userDto) {
//        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
//        if (org.isEmpty()) {
//            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
//        }
//        Iterable<Department> departments = departmentRepository.getDeptListWithOrg(org.get());
//
//        return departments;
//    }

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Page<Department> getListDepartmentsWithPagination(UserDto userDto, Integer pageNum, Integer pageSize, String sortName, String sortOrder, String search) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
        if (org.isEmpty()) {
            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
        }
        Pageable pageable = PageRequest.of(
                pageNum - 1,
                pageSize);
        Page<Department> departments = departmentRepository.getDeptListWithOrg(org.get(), search.toLowerCase(), pageable);

        return departments;
    }

    public void createAdminDepartment(UserDto userDto) {
        CreateDepartmentDto createDepartmentDto = new CreateDepartmentDto();
        createDepartmentDto.setName("Administration");
        createDepartmentDto.setDescription("This is the administration department");

        Department department = saveDepartment(createDepartmentDto, userDto.getOrgId());
        Optional<Users> user = usersRepository.findByEmail(userDto.getEmail());
        user.get().setDepartment(department);
        usersRepository.save(user.get());
    }

    @RequiredPermission(permission = PermissionConstants.DEPARTMENT)
    public Department saveDepartment(CreateDepartmentDto department, Integer orgId) {

        Department dept = new Department();
        System.out.println("1");
        Optional<Organization> org = organizationRepository.findById(orgId);
        if (org.isPresent()) {
            System.out.println("2");
            Organization organization = org.get();
            dept.setOrganization(organization);
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
            departmentRepository.save(dept);
            System.out.println("3");
            organization.getDepartments().add(dept);
            organizationRepository.save(organization);
            System.out.println("4");
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
    public void deleteDepartment(Integer id, String email) throws Exception {

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

            if (dept.get().getUsers().size() == 0) {
                departmentRepository.softDeleteById(id, deletedBy);
            } else {
                throw new Exception("Unable to delete department as there are users associated with the department.");
            }

        } else {
            throw new EntityNotFoundException("Department is not found.");
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
