package com.roxiler.erp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.roxiler.erp.constants.LeaveDurationConstants;
import com.roxiler.erp.constants.TypeOfLeaveConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.leaves.UpdateLeaveSystemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
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

    @Autowired
    private UsersRepository usersRepository;

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

    public LeavesSystem getLeaveSystem(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        Designation designation = user.getDesignation();
        LeavesSystem leavesSystem = leavesSystemRepository.readByDesignationAndOrganization(designation, designation.getOrganization());
        return leavesSystem;
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

    public LeavesSystem updatLeavesSystem(UpdateLeaveSystemDto updateLeaveSystemDto, UserDto userDto) {
        Optional<LeavesSystem> optionalLeavesSystem = leavesSystemRepository.findById(updateLeaveSystemDto.getLeavePolicyId());
        Users user = usersRepository.readByEmail(userDto.getEmail());
        if (optionalLeavesSystem.isEmpty()) {
            throw new EntityNotFoundException("Leave Policy not found");
        }

        LeavesSystem existingLeavesSystem = optionalLeavesSystem.get();
        if (!Objects.equals(userDto.getOrgId(), existingLeavesSystem.getOrganization().getId())) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        existingLeavesSystem.setAccrual(updateLeaveSystemDto.getAccrual());
        existingLeavesSystem.setCarryOverLimits(updateLeaveSystemDto.getCarryOverLimits());
        existingLeavesSystem.setConsecutiveLeaves(updateLeaveSystemDto.getConsecutiveLeaves());
        existingLeavesSystem.setAllowedLeaveDurations(updateLeaveSystemDto.getAllowedLeaveDurations());
        updateLeaveSystemDto.setAllowedLeaveTypes(updateLeaveSystemDto.getAllowedLeaveTypes());
        return leavesSystemRepository.save(existingLeavesSystem);

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
