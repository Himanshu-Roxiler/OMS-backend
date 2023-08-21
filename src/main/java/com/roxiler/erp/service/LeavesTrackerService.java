package com.roxiler.erp.service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.leaves.CreateLeaveTrackerDto;
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

    public LeavesTracker makeLeaveRequest(UserDto userDto, CreateLeaveTrackerDto leaveRequest) {

        LeavesTracker leavesTracker = new LeavesTracker();
        leavesTracker.setStartDate(leaveRequest.getStartDate());
        leavesTracker.setEndDate(leaveRequest.getEndDate());
        leavesTracker.setReason(leaveRequest.getReason());
        leavesTracker.setTypeOfLeave(leaveRequest.getTypeOfLeave());
        Users user = usersRepository.readByEmail(userDto.getEmail());
        leavesTracker.setUser(user);
        leavesTracker.setReporting_manager(user.getReportingManager());
        LeavesTracker leave = leavesTrackerRepository.save(leavesTracker);

        // TO DO
        // update leaves table once request is made

        return leave;
    }

    public void deleteLeaveRequest(UserDto userDto, Integer leaveRequestId) {

        Optional<LeavesTracker> leave = leavesTrackerRepository.findById(leaveRequestId);

        if (leave.isPresent()) {
            if (!Objects.equals(userDto.getId(), leave.get().getUser().getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
            leavesTrackerRepository.softDeleteById(leaveRequestId, deletedBy);
        } else {
            throw new EntityNotFoundException("No leave request found for given id");
        }
    }
}
