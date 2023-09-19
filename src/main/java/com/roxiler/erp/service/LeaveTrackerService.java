package com.roxiler.erp.service;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roxiler.erp.dto.leaves.ApproveLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CreateLeaveTrackerDto;
import com.roxiler.erp.interfaces.ApprovedLeaveBreakup;
import com.roxiler.erp.interfaces.LeaveBreakup;
import com.roxiler.erp.repository.LeavesRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.LeavesTrackerRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveTrackerService {

    @Autowired
    private LeavesTrackerRepository leaveTrackerRepository;


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LeaveService leaveService;


    @Transactional
    public LeavesTracker saveLeavesTracker(CreateLeaveTrackerDto createLeaveTrackerDto, UserDto userDto) {
        Optional<Users> user = usersRepository.findById(userDto.getId());
        Optional<Users> reportingManager = usersRepository.findById(createLeaveTrackerDto.getReportingManager());
        LeavesTracker leavesTracker = new LeavesTracker();
        Float noOfDays = 0f;

        if (user.isEmpty() || reportingManager.isEmpty()) {
            throw new EntityNotFoundException("User or Reporting Manager not found");
        } else {
            LeaveBreakup[] leaveBreakups = createLeaveTrackerDto.getLeaveBreakups();
            for (LeaveBreakup leaveBreakup : leaveBreakups) {
                Float numLeaveValue = leaveBreakup.getNoOfDays();
                if (numLeaveValue != 0.25f && numLeaveValue != 0.5f && numLeaveValue != 1f) {
                    throw new EntityNotFoundException("Wrong number of days input");
                }
                noOfDays += numLeaveValue;
            }
            leavesTracker.setUserId(user.get().getId());
            leavesTracker.setReportingManager(createLeaveTrackerDto.getReportingManager());
            leavesTracker.setStartDate(createLeaveTrackerDto.getStartDate());
            leavesTracker.setEndDate(createLeaveTrackerDto.getEndDate());
            leavesTracker.setReason(createLeaveTrackerDto.getReason());
            leavesTracker.setNoOfDays(noOfDays);
            leavesTracker.setTypeOfLeave(createLeaveTrackerDto.getTypeOfLeave());
            Gson gson = new Gson();
            String leaveBreakupsString = gson.toJson(createLeaveTrackerDto.getLeaveBreakups());
            System.out.println("\nLEAVE BREAKUP STRING\n" + leaveBreakupsString);
            leavesTracker.setLeaveBreakups(leaveBreakupsString);

        }
        System.out.println("\nNUM DAYS\n" + noOfDays);

        leaveService.updateLeavesUponRequest(noOfDays, user.get(), createLeaveTrackerDto.getTypeOfLeave());

        LeavesTracker savedLeave = leaveTrackerRepository.save(leavesTracker);
        System.out.println("\nSAVED : \n" + savedLeave.getLeaveBreakups() + "\n" + savedLeave.getReason() + "\n" + savedLeave.getCreatedBy());
        return savedLeave;
    }

    public Iterable<LeavesTracker> getAllLeavesTrackersIterable(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        Iterable<LeavesTracker> leaveTrackers = leaveTrackerRepository.findAllByUser(user.getId());
        Gson gson = new Gson();
        for (LeavesTracker leavesTracker : leaveTrackers) {
            LeaveBreakup[] leaveBreakups = gson.fromJson(leavesTracker.getLeaveBreakups(), LeaveBreakup[].class);
            for (LeaveBreakup leaveBreakup : leaveBreakups) {
                System.out.println("\nSTART DATE : " + leaveBreakup.getStartDate());
                System.out.println("\nEND DATE : " + leaveBreakup.getEndDate());
                System.out.println("\nNo of days : " + leaveBreakup.getNoOfDays());
                System.out.println("\nIs approved : " + leaveBreakup.getIsApproved());
            }
        }
        return leaveTrackers;
    }

    public Iterable<LeavesTracker> getAllLeavesTrackersForRM(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        Iterable<LeavesTracker> leaveTrackers = leaveTrackerRepository.findAllByReportingManager(user.getId());
        return leaveTrackers;
    }

    public void deleteLeavesTracker(Integer id) {
        Optional<LeavesTracker> LeavesTracker = leaveTrackerRepository.findById(id);
        if (LeavesTracker.isEmpty()) {
            throw new EntityNotFoundException("LeavesTracker " + id + " does not exist");
        }
        if (LeavesTracker.isPresent()) {
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

    @Transactional
    public LeavesTracker approveLeaveRequest(ApproveLeaveRequestDto approveLeaveRequestDto, UserDto userDto, Integer id) {

        Optional<LeavesTracker> leavesTracker = leaveTrackerRepository.findById(id);
        if (leavesTracker.isPresent()) {
            LeavesTracker updateLeaveTracker = leavesTracker.get();
            if (!Objects.equals(updateLeaveTracker.getReportingManager(), userDto.getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            updateLeaveTracker.setApprovedStartDate(approveLeaveRequestDto.getApprovedStartDate());
            updateLeaveTracker.setApprovedEndDate(approveLeaveRequestDto.getApprovedEndDate());
            updateLeaveTracker.setIsApproved(true);
            updateLeaveTracker.setComment(approveLeaveRequestDto.getComment());
            updateLeaveTracker.setNote(approveLeaveRequestDto.getNote());
            ApprovedLeaveBreakup[] approvedLeaveBreakups = approveLeaveRequestDto.getApprovedLeaveBreakups();
            Float noOfDays = 0f;
            for (ApprovedLeaveBreakup approvedLeaveBreakup : approvedLeaveBreakups) {
                Float numLeaveValue = approvedLeaveBreakup.getNoOfDays();
                if (numLeaveValue != 0.25f && numLeaveValue != 0.5f && numLeaveValue != 1f) {
                    throw new EntityNotFoundException("Wrong number of days input");
                }
                if (approvedLeaveBreakup.getIsApproved()) {
                    noOfDays += numLeaveValue;
                }
            }
            if (noOfDays < updateLeaveTracker.getNoOfDays()) {
                leaveService.updatesLeaveUponApproval(updateLeaveTracker.getNoOfDays(), noOfDays, updateLeaveTracker.getUserId(), updateLeaveTracker.getTypeOfLeave());
            }
            Gson gson = new Gson();
            String leaveBreakupsString = gson.toJson(approvedLeaveBreakups);
            System.out.println("\nLEAVE BREAKUP STRING\n" + leaveBreakupsString);
            updateLeaveTracker.setApprovedLeaveBreakups(leaveBreakupsString);

            LeavesTracker updatedLeave = leaveTrackerRepository.save(updateLeaveTracker);
            return updatedLeave;
        }

        throw new EntityNotFoundException("No leave request found for the given id");
    }
}
