package com.roxiler.erp.service;

import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.DepartmentRepository;
import com.roxiler.erp.repository.DesignationRepository;
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
    private OrganizationService organizationService;

    public Iterable<Designation> getAllDesignations() {
        Iterable<Designation> designations = designationRepository.findAll();

        return designations;
    }

    public Designation saveDesignation(Designation designation, Integer orgId) {
        /*The organization needs to be replaced by finding it using org repository*/
        Organization org = organizationService.getOrganization(orgId);
        designation.setOrganization(org);
        Designation designations = designationRepository.save(designation);

        return designation;
    }

    public String updateDesignation(Designation designation, Integer id) {


        Optional<Designation> desgToUpdate = designationRepository.findById(id);

        desgToUpdate.ifPresent(desg -> {
            desg.setName(designation.getName());
            desg.setDescription(designation.getDescription());
        });

        if(desgToUpdate.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Designation updatedDesignation = designationRepository.save(desgToUpdate.get());

        return "Designation updated successfully";
    }

    public String deleteDesignation(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        designationRepository.softDeleteById(id, deletedBy);

        return "Designation deleted Successfully";
    }
    public Designation getDesignationById(Integer id) {
        Optional<Designation> desg = designationRepository.findById(id);

        if(desg.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return desg.get();
    }

}
