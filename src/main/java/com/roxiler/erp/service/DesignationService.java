package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.designation.CreateDesignationDto;
import com.roxiler.erp.dto.designation.ListDesignationDto;
import com.roxiler.erp.dto.designation.UpdateDesignationDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
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

import java.util.Objects;
import java.util.Optional;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Iterable<Designation> getAllDesignations() {
        Iterable<Designation> designations = designationRepository.findAll();

        return designations;
    }

    @RequiredPermission(permission = PermissionConstants.DESIGNATION)
    public Page<Designation> getListDesignationsWithPagination(UserDto userDto, Integer pageNum, Integer pageSize, String sortName, String sortOrder, String search) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
        if (org.isEmpty()) {
            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
        }
        Pageable pageable = PageRequest.of(
                pageNum - 1,
                pageSize);
        Page<Designation> designations = designationRepository.getListDesgWithOrg(org.get(), search.toLowerCase(), pageable);
        return designations;
    }

    @RequiredPermission(permission = PermissionConstants.DESIGNATION)
    public Designation saveDesignation(CreateDesignationDto designation, Integer orgId) {
        Optional<Organization> org = organizationRepository.findById(orgId);
        Designation newDesg = new Designation();
        if (org.isPresent()) {
            Organization organization = org.get();
            newDesg.setOrganization(organization);
            newDesg.setName(designation.getName());
            newDesg.setDescription(designation.getDescription());
            Designation desg = designationRepository.save(newDesg);
            organization.getDesignations().add(desg);
            //organization.setDepartments(departments);

            organizationRepository.save(organization);
            return desg;
        }

        return null;
    }

    @RequiredPermission(permission = PermissionConstants.DESIGNATION)
    public Designation updateDesignation(UpdateDesignationDto designation, Integer id, String email) {

        Users user = usersRepository.readByEmail(email);
        Optional<Designation> desgToUpdate = designationRepository.findById(id);

        desgToUpdate.ifPresent(desg -> {
            if (desgToUpdate.get().getOrganization().getId().equals(user.getActiveOrganization())) {
                desg.setName(designation.getName());
                desg.setDescription(designation.getDescription());
            } else {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
        });

        if (desgToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Designation updatedDesignation = designationRepository.save(desgToUpdate.get());

        return updatedDesignation;
    }

    @RequiredPermission(permission = PermissionConstants.DESIGNATION)
    public void deleteDesignation(Integer id, String email) {

        Users user = usersRepository.readByEmail(email);
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Designation> desg = designationRepository.findById(id);

        if (desg.isPresent()) {
            if (!Objects.equals(desg.get().getOrganization().getId(), user.getActiveOrganization())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            Optional<Organization> organization = organizationRepository.findById(desg.get().getOrganization().getId());

            if (organization.isPresent()) {
                organization.get().getDesignations().remove(desg.get());
                organizationRepository.save(organization.get());
            }

            designationRepository.softDeleteById(id, deletedBy);
        }
    }

    @RequiredPermission(permission = PermissionConstants.DESIGNATION)
    public Designation getDesignationById(Integer id) {
        Optional<Designation> desg = designationRepository.findById(id);

        if (desg.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return desg.get();
    }

}
