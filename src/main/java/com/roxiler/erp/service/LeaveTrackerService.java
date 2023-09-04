package com.roxiler.erp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.LeavesTrackerRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LeaveTrackerService {

    @Autowired
    private LeavesTrackerRepository leaveTrackerRepository;


     @Autowired
    private UsersRepository usersRepository;


    public LeavesTracker saveLeavesTracker(LeavesTracker leavesTracker, UserDto userDto) {  
        Optional<Users> user = usersRepository.findById(userDto.getId());

        if (user.isPresent()) {
            leavesTracker.setUser(user.get());
        } else {
            throw new EntityNotFoundException("LeavesTracker user not found"+ userDto.getId());
        }
        // Optional<Users> reportingManager = usersRepository.findById(leavesTracker.getReportingManager());
        // if (reportingManager.isPresent()) {
        //     leavesTracker.setUser(reportingManager.get());
        // } else {
        //     throw new EntityNotFoundException("LeavesTracker reporting manager not found"+ leavesTracker.getReportingManager());
        // }

        return leaveTrackerRepository.save(leavesTracker);
    }

    public Iterable<LeavesTracker> getAllLeavesTrackersIterable(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        return leaveTrackerRepository.findAllByUser(user);
    }

    public void deleteLeavesTracker(Integer id) {
        Optional<LeavesTracker> LeavesTracker = leaveTrackerRepository.findById(id);
        if (LeavesTracker.isEmpty()) {
            throw new EntityNotFoundException("LeavesTracker " + id + " does not exist");
        }
        if(LeavesTracker.isPresent()) {
            leaveTrackerRepository.deleteById(LeavesTracker.get().getId());
        }
    }

    public LeavesTracker updatLeavesTracker(LeavesTracker updateLeavesTracker, Integer id) {
        Optional<LeavesTracker> optionalLeavesTracker = leaveTrackerRepository.findById(id);

        if (optionalLeavesTracker.isEmpty()) {
            throw new EntityNotFoundException("LeavesTracker " + id + " does not exist");
        }

        if (optionalLeavesTracker.isPresent()) {
            LeavesTracker existingLeavesTracker = optionalLeavesTracker.get();
            existingLeavesTracker.setApprovedEndDate(updateLeavesTracker.getApprovedEndDate());
            existingLeavesTracker.setApprovedStartDate(updateLeavesTracker.getApprovedStartDate());
            existingLeavesTracker.setComment(updateLeavesTracker.getComment());
            existingLeavesTracker.setNote(updateLeavesTracker.getNote());
            existingLeavesTracker.setReason(updateLeavesTracker.getReason());
            existingLeavesTracker.setEndDate(updateLeavesTracker.getEndDate());
            existingLeavesTracker.setStartDate(updateLeavesTracker.getStartDate());
            existingLeavesTracker.setIsApproved(updateLeavesTracker.getIsApproved());
            existingLeavesTracker.setTypeOfLeave(updateLeavesTracker.getTypeOfLeave());
            existingLeavesTracker.setNoOfDays(updateLeavesTracker.getNoOfDays());

            return leaveTrackerRepository.save(existingLeavesTracker);
        }
        return updateLeavesTracker;

    }

    public LeavesTracker getLeavesTrackerById(Integer id) {
        Optional<LeavesTracker> optionalLeavesTracker = leaveTrackerRepository.findById(id);
        if (optionalLeavesTracker.isEmpty()) {
            throw new EntityNotFoundException("LeavesTracker " + id + " does not exist");
        }
        return optionalLeavesTracker.get();
    }

    
}
