package com.roxiler.erp.service;

import java.util.Optional;

import com.roxiler.erp.constants.LeaveDurationConstants;
import com.roxiler.erp.constants.TypeOfLeaveConstants;
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
            throw new EntityNotFoundException("LeavesSystem organization is not found" + leavesSystem.getOrganization().getId());
        }

        Optional<Designation> designationOptional = designationRepository.findById(leavesSystem.getDesignation().getId());
        if (designationOptional.isPresent()) {
            leavesSystem.setDesignation(designationOptional.get());
        } else {
            throw new EntityNotFoundException("LeavesSystem designation is not found" + leavesSystem.getDesignation().getId());
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
        if (LeavesSystem.isPresent()) {
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

    public void createLeavesSystemWithDesignation(Designation designation, Organization organization) {
        LeavesSystem leavesSystem = new LeavesSystem();

        leavesSystem.setAccrual(1f);
        leavesSystem.setConsecutiveLeaves(15f);
        leavesSystem.setCarryOverLimits(10f);
        leavesSystem.setDesignation(designation);
        leavesSystem.setOrganization(organization);
        String[] leaveTypes = {TypeOfLeaveConstants.VACATION_LEAVE, TypeOfLeaveConstants.UNPAID_LEAVE, TypeOfLeaveConstants.SICK_LEAVE, TypeOfLeaveConstants.PAID_LEAVE};
        String[] leaveDurations = {LeaveDurationConstants.FULL_DAY, LeaveDurationConstants.HALF_DAY, LeaveDurationConstants.QUARTER_DAY};
        String leaveTypesString = leaveTypes.toString();
        String leaveDurationString = leaveDurations.toString();
        leavesSystem.setAllowedLeaveTypes(leaveTypes);
        leavesSystem.setAllowedLeaveDurations(leaveDurations);

        leavesSystemRepository.save(leavesSystem);
    }

    public LeavesSystem getLeaveSystemByDesignation(Designation designation) {
        LeavesSystem leavesSystem = leavesSystemRepository.readByDesignationAndOrganization(designation, designation.getOrganization());
        return leavesSystem;
    }
}
