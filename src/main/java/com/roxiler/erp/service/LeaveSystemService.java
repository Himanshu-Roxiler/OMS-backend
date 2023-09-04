package com.roxiler.erp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.*;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LeaveSystemService {

    @Autowired
    private LeaveSystemRepository leavesSystemRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DesignationRepository designationRepository;


    public LeavesSystem saveLeavesSystem(LeavesSystem leavesSystem) {   
        Optional<Organization> organizatOptional = organizationRepository.findById(leavesSystem.getOrganization().getId()); 
        if (organizatOptional.isPresent()) {
            leavesSystem.setOrganization(organizatOptional.get());
        } else {
            throw new EntityNotFoundException("LeavesSystem organization is not found"+ leavesSystem.getOrganization().getId());
        }

        Optional<Designation> designationOptional = designationRepository.findById(leavesSystem.getDesignation().getId());
        if (designationOptional.isPresent()) {
            leavesSystem.setDesignation(designationOptional.get());
        } else {
            throw new EntityNotFoundException("LeavesSystem designation is not found"+ leavesSystem.getDesignation().getId());
        }
        LeavesSystem leavesSystem2 = leavesSystemRepository.save(leavesSystem);
        organizatOptional.get().getLeavesSystems().add(leavesSystem2);
        organizationRepository.save(organizatOptional.get());

        return leavesSystem2;
    }

    public Iterable<LeavesSystem> getAllLeavesSystemsIterable() {
        return leavesSystemRepository.findAll();
    }

    public void deleteLeavesSystem(Integer id) {
        Optional<LeavesSystem> LeavesSystem = leavesSystemRepository.findById(id);
        if (LeavesSystem.isEmpty()) {
            throw new EntityNotFoundException("LeavesSystem " + id + " does not exist");
        }
        if(LeavesSystem.isPresent()) {
            leavesSystemRepository.deleteById(LeavesSystem.get().getId());
        }
    }

    public LeavesSystem updatLeavesSystem(LeavesSystem updateLeavesSystem, Integer id) {
        Optional<LeavesSystem> optionalLeavesSystem = leavesSystemRepository.findById(id);

        if (optionalLeavesSystem.isEmpty()) {
            throw new EntityNotFoundException("LeavesSystem " + id + " does not exist");
        }

        if (optionalLeavesSystem.isPresent()) {
            LeavesSystem existingLeavesSystem = optionalLeavesSystem.get();
            existingLeavesSystem.setAccrual(updateLeavesSystem.getAccrual());
            existingLeavesSystem.setCarryOverLimits(updateLeavesSystem.getCarryOverLimits());
            return leavesSystemRepository.save(existingLeavesSystem);
        }
        return updateLeavesSystem;

    }

    public LeavesSystem getLeavesSystemById(Integer id) {
        Optional<LeavesSystem> optionalLeavesSystem = leavesSystemRepository.findById(id);
        if (optionalLeavesSystem.isEmpty()) {
            throw new EntityNotFoundException("LeavesSystem " + id + " does not exist");
        }
        return optionalLeavesSystem.get();
    }

}
