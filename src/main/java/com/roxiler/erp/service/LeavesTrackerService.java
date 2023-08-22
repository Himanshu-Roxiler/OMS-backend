package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.leaves.ApproveLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CancelLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CreateLeaveTrackerDto;
import com.roxiler.erp.dto.leaves.RejectLeaveRequestDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.LeavesTrackerRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class LeavesTrackerService {

    @Autowired
    private LeavesTrackerRepository leavesTrackerRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Iterable<LeavesTracker> getAllLeaveRequestsForUser(UserDto userDto) {

        Iterable<LeavesTracker> leaveRequests = leavesTrackerRepository.findAllByUser(userDto.getId());

        return leaveRequests;
    }

    @RequiredPermission(permission = PermissionConstants.LEAVE)
    public LeavesTracker makeLeaveRequest(UserDto userDto, CreateLeaveTrackerDto leaveRequest) {

        LeavesTracker leavesTracker = new LeavesTracker();
        leavesTracker.setStartDate(leaveRequest.getStartDate());
        leavesTracker.setEndDate(leaveRequest.getEndDate());
        leavesTracker.setReason(leaveRequest.getReason());
        leavesTracker.setTypeOfLeave(leaveRequest.getTypeOfLeave());
        leavesTracker.setNoOfDays(leaveRequest.getNoOfDays());
        Users user = usersRepository.readByEmail(userDto.getEmail());
        leavesTracker.setUser(user);
        leavesTracker.setReporting_manager(user.getReportingManager());
        LeavesTracker leave = leavesTrackerRepository.save(leavesTracker);

        // TO DO
        // update leaves table once request is made

        return leave;
    }

    @RequiredPermission(permission = PermissionConstants.LEAVE)
    public void deleteLeaveRequest(UserDto userDto, Integer leaveRequestId, CancelLeaveRequestDto leaveRequestDto) {

        Optional<LeavesTracker> leave = leavesTrackerRepository.findById(leaveRequestId);

        if (leave.isPresent()) {
            if (!Objects.equals(userDto.getId(), leave.get().getUser().getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
            leave.get().setLeaveCancelReason(leaveRequestDto.getLeaveCancelReason());
            leavesTrackerRepository.softDeleteById(leaveRequestId, deletedBy);

            // TO DO: Restore leaves for user in user table
        } else {
            throw new EntityNotFoundException("No leave request found for given id");
        }
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public LeavesTracker approveLeaveRequest(UserDto userDto, ApproveLeaveRequestDto leaveRequestDto, Integer id) {

        Optional<LeavesTracker> leaveRequest = leavesTrackerRepository.findById(id);

        if (leaveRequest.isPresent()) {
            if (!Objects.equals(leaveRequest.get().getReporting_manager().getId(), userDto.getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            leaveRequest.get().setIsApproved(true);
            leaveRequest.get().setApprovedStartDate(leaveRequestDto.getApprovedStartDate());
            leaveRequest.get().setApprovedEndDate(leaveRequestDto.getApprovedEndDate());
            leaveRequest.get().setComment(leaveRequestDto.getComment());
            leaveRequest.get().setNote(leaveRequestDto.getNote());

            // TO DO update leaves table as well

            LeavesTracker updatedLeaveRequest = leavesTrackerRepository.save(leaveRequest.get());

            return updatedLeaveRequest;
        } else {
            throw new EntityNotFoundException("No leave request found by the given id");
        }
    }

    @RequiredPermission(permission = PermissionConstants.USERS)
    public LeavesTracker rejectLeaveRequest(UserDto userDto, RejectLeaveRequestDto leaveRequestDto, Integer id) {

        Optional<LeavesTracker> leaveRequest = leavesTrackerRepository.findById(id);

        if (leaveRequest.isPresent()) {
            if (!Objects.equals(leaveRequest.get().getReporting_manager().getId(), userDto.getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            leaveRequest.get().setIsApproved(false);
            leaveRequest.get().setComment(leaveRequestDto.getComment());
            leaveRequest.get().setNote(leaveRequestDto.getNote());

            // TO DO update leaves table as well

            LeavesTracker updatedLeaveRequest = leavesTrackerRepository.save(leaveRequest.get());

            return updatedLeaveRequest;
        } else {
            throw new EntityNotFoundException("No leave request found by the given id");
        }
    }
}
