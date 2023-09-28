package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.constants.RoleNameConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.holiday.CreateHolidayDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private HolidayService holidayService;

    @Transactional
    @EntityGraph(value = "departments")
    public Iterable<Organization> findPopulatedOrganizations() {
        return organizationRepository.findAll();
    }

    @Transactional
    public Iterable<Organization> getAllOrganizations() {
        Iterable<Organization> organizations = organizationRepository.findAll();
        //Iterable<Organization> organizations = organizationRepository.getPopulatedOrganizations();

        return organizations;
    }

    //@RequiredPermission(permission = PermissionConstants.ADMIN)
    public Organization saveOrganization(Organization organization, UserDto userDto) {

        //Department dept = departmentService.getDepartmentById(organization.getDepartmentId().getId());
        //Designation desg = designationService.getDesignationById(organization.getDesignationId().getId());
        Optional<Users> user = usersRepository.findById(userDto.getId());
        if (user.isPresent()) {

            if (user.get().getOrganization() != null) {
                throw new EntityExistsException("The user is already associated with an organization!");
            }
            organization.getUsers().add(user.get());
            Organization org = organizationRepository.save(organization);
            user.get().setOrganization(org);
            user.get().setActiveOrganization(org.getId());
            usersRepository.save(user.get());

            UserOrganizationRole userOrganizationRole = new UserOrganizationRole();
            userOrganizationRole.setOrganization(org);
            UserRole userRole = userRoleRepository.readByName(RoleNameConstants.ADMIN);
            userOrganizationRole.setRole(userRole);
            userOrganizationRole.setUser(user.get());
            UserOrganizationRole userOrgRole = userOrganizationRoleRepository.save(userOrganizationRole);
            organization.getUserOrganizationRole().add(userOrgRole);
            Organization savedOrg = organizationRepository.save(organization);
            userDto.setOrgId(savedOrg.getId());
            holidayService.createHolidaysOnOrgCreation(userDto);
        }

        return organization;
    }

    @RequiredPermission(permission = PermissionConstants.ADMIN)
    public Organization updateOrganization(Organization organization, Integer id, String userEmail) {

        Users user = usersRepository.readByEmail(userEmail);
        if (!Objects.equals(user.getOrganization().getId(), id)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        Optional<Organization> orgToUpdate = organizationRepository.findById(id);

        orgToUpdate.ifPresent(org -> {
            org.setName(organization.getName());
            org.setAddress(organization.getAddress());
            org.setCity(organization.getCity());
            org.setState(organization.getState());
            org.setCountry(organization.getCountry());
        });

        if (orgToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Organization updatedOrg = organizationRepository.save(orgToUpdate.get());

        return updatedOrg;
    }


    @RequiredPermission(permission = PermissionConstants.ADMIN)
    public void deleteOrganization(Integer id, String userEmail) {
        Users user = usersRepository.readByEmail(userEmail);
        if (!Objects.equals(user.getOrganization().getId(), id)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        //organizationRepository.softDeleteById(id, deletedBy);
        this.softDeleteOrganization(id, userEmail);
    }

    @RequiredPermission(permission = PermissionConstants.ADMIN)
    public Organization getOrganization(Integer id) {
        Optional<Organization> organization = organizationRepository.findById(id);

        if (organization.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return organization.get();
    }

    public Organization getOrganizationByUserId(UserDto userDto) {
        Optional<Organization> organization = organizationRepository.findById(userDto.getOrgId());

        if (organization.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return organization.get();
    }

    @Transactional
    @RequiredPermission(permission = PermissionConstants.ADMIN)
    public void softDeleteOrganization(Integer id, String userEmail) {
        Users user = usersRepository.readByEmail(userEmail);
        if (!Objects.equals(user.getOrganization().getId(), id)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();

        // Retrieve the organization
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        // Soft delete each department
        for (Department department : organization.getDepartments()) {
            department.setDeletedAt(LocalDateTime.now());
            department.setDeletedBy(deletedBy);
            departmentRepository.save(department);
        }
        for (Designation designation : organization.getDesignations()) {
            designation.setDeletedAt(LocalDateTime.now());
            designation.setDeletedBy(deletedBy);
            designationRepository.save(designation);
        }

        // Soft delete the organization
        organization.setDeletedAt(LocalDateTime.now());
        organization.setDeletedBy(deletedBy);
        organizationRepository.save(organization);
    }
}
