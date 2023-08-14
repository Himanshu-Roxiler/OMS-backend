package com.roxiler.erp.service;

import com.roxiler.erp.dto.designation.CreateDesignationDto;
import com.roxiler.erp.dto.designation.UpdateDesignationDto;
import com.roxiler.erp.model.*;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public Iterable<Designation> getAllDesignations() {
        Iterable<Designation> designations = designationRepository.findAll();

        return designations;
    }

    public Designation saveDesignation(CreateDesignationDto designation, Integer orgId) {
        Optional<Organization> org = organizationRepository.findById(orgId);
        Designation newDesg = new Designation();
        if(org.isPresent()) {
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

    public Designation updateDesignation(UpdateDesignationDto designation, Integer id) {


        Optional<Designation> desgToUpdate = designationRepository.findById(id);

        desgToUpdate.ifPresent(desg -> {
            desg.setName(designation.getName());
            desg.setDescription(designation.getDescription());
        });

        if(desgToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Designation updatedDesignation = designationRepository.save(desgToUpdate.get());

        return updatedDesignation;
    }

    public void deleteDesignation(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Designation> desg = designationRepository.findById(id);

        if(desg.isPresent()) {
            Optional<Organization> organization = organizationRepository.findById(desg.get().getOrganization().getId());


            if(organization.isPresent()) {
                organization.get().getDesignations().remove(desg.get());
                organizationRepository.save(organization.get());
            }

            designationRepository.softDeleteById(id, deletedBy);
        }
    }
    public Designation getDesignationById(Integer id) {
        Optional<Designation> desg = designationRepository.findById(id);

        if(desg.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return desg.get();
    }

}
