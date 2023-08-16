package com.roxiler.erp.service;

import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
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

    @RequiredPermission(permission = "admin")
    public Organization saveOrganization(Organization organization, Integer userId) {

        //Department dept = departmentService.getDepartmentById(organization.getDepartmentId().getId());
        //Designation desg = designationService.getDesignationById(organization.getDesignationId().getId());
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            organization.getUsers().add(user.get());
            Organization org = organizationRepository.save(organization);
            user.get().setOrganization(org);
            usersRepository.save(user.get());
        }

        return organization;
    }

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


    public void deleteOrganization(Integer id, String userEmail) {
        Users user = usersRepository.readByEmail(userEmail);
        if (!Objects.equals(user.getOrganization().getId(), id)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        //organizationRepository.softDeleteById(id, deletedBy);
        this.softDeleteOrganization(id, userEmail);
    }

    public Organization getOrganization(Integer id) {
        Optional<Organization> organization = organizationRepository.findById(id);

        if (organization.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return organization.get();
    }

    @Transactional
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
