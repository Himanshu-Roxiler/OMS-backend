package com.roxiler.erp.controller;

import com.roxiler.erp.dto.leaves.ApproveLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CancelLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CreateLeaveTrackerDto;
import com.roxiler.erp.dto.leaves.RejectLeaveRequestDto;
import com.roxiler.erp.interfaces.LeaveBreakup;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.LeaveTrackerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/leave-tracker")
public class LeaveTrackerController {

    @Autowired
    private LeaveTrackerService leaveTrackerService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLeavesTrackers(@AuthenticationPrincipal UserDto userDto) {

        Iterable<LeavesTracker> LeavesTrackers = leaveTrackerService.getAllLeavesTrackersIterable(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched requests made by user");
        responseObject.setData(LeavesTrackers);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @GetMapping("/reporting-manager")
    public ResponseEntity<ResponseObject> getAllLeavesTrackersForRM(@AuthenticationPrincipal UserDto userDto) {

        Iterable<LeavesTracker> LeavesTrackers = leaveTrackerService.getAllLeavesTrackersForRM(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched all the leave requests made");
        responseObject.setData(LeavesTrackers);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addLeavesTracker(
            @Valid @RequestBody CreateLeaveTrackerDto createLeaveTrackerDto,
            @AuthenticationPrincipal UserDto userDto) {

        LeavesTracker leaveReq = leaveTrackerService.saveLeavesTracker(createLeaveTrackerDto, userDto);
        System.out.println("\nTHE RETURNED OBJECT IS:\n" + leaveReq);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesTracker.");
        responseObject.setData(leaveReq);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateLeavesTracker(@Valid @RequestBody LeavesTracker LeavesTracker, @AuthenticationPrincipal UserDto userDto) {

        LeavesTracker LeavesTracker2 = leaveTrackerService.updatLeavesTracker(LeavesTracker, userDto.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated LeavesTracker");
        responseObject.setData(LeavesTracker2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteLeavesTracker(@PathVariable("id") Integer id) {

        leaveTrackerService.deleteLeavesTracker(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted LeavesTracker.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseObject> approveLeaveRequest(
            @Valid @RequestBody ApproveLeaveRequestDto approveLeaveRequestDto,
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id
    ) {

        System.out.println("LEAVE REQUEST: \n" + approveLeaveRequestDto);
        LeavesTracker leavesTracker = leaveTrackerService.approveLeaveRequest(approveLeaveRequestDto, userDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesTracker.");
        responseObject.setData(leavesTracker);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PostMapping("/cancel-leave-request/{id}")
    public ResponseEntity<ResponseObject> cancelLeaveRequest(
            @Valid @RequestBody CancelLeaveRequestDto cancelLeaveRequestDto,
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id
    ) {

        LeavesTracker leavesTracker = leaveTrackerService.cancelLeaveRequest(cancelLeaveRequestDto, userDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesTracker.");
        responseObject.setData(leavesTracker);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PostMapping("/reject-leave-request/{id}")
    public ResponseEntity<ResponseObject> rejectLeaveRequest(
            @Valid @RequestBody RejectLeaveRequestDto rejectLeaveRequestDto,
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id
    ) {

        LeavesTracker leavesTracker = leaveTrackerService.rejectLeaveRequest(rejectLeaveRequestDto, userDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesTracker.");
        responseObject.setData(leavesTracker);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }
}
