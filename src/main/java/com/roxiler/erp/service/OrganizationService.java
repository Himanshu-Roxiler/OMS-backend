package com.roxiler.erp.service;

import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

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

    public Organization saveOrganization(Organization organization) {

        //Department dept = departmentService.getDepartmentById(organization.getDepartmentId().getId());
        //Designation desg = designationService.getDesignationById(organization.getDesignationId().getId());
        Organization users = organizationRepository.save(organization);

        return organization;
    }

    public String updateOrganization(Organization organization, Integer id) {


        Optional<Organization> orgToUpdate = organizationRepository.findById(id);

        orgToUpdate.ifPresent(org -> {
            org.setName(organization.getName());
            org.setAddress(organization.getAddress());
            org.setCity(organization.getCity());
            org.setState(organization.getState());
            org.setCountry(organization.getCountry());
        });

        if(orgToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Organization updatedOrg = organizationRepository.save(orgToUpdate.get());

        return "Organization updated successfully";
    }


    public String deleteOrganization(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        //organizationRepository.softDeleteById(id, deletedBy);
        this.softDeleteOrganization(id);

        return "Organization deleted Successfully";
    }

    public Organization getOrganization(Integer id) {
        Optional<Organization> organization = organizationRepository.findById(id);

        if(organization.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return organization.get();
    }

    @Transactional
    public void softDeleteOrganization(Integer id) {
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
