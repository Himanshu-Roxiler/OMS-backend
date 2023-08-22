package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.leaves.ApproveLeaveRequestDto;
import com.roxiler.erp.dto.leaves.CreateLeaveTrackerDto;
import com.roxiler.erp.dto.leaves.RejectLeaveRequestDto;
import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.LeavesTrackerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/leaves")
public class LeavesRequestController {

    @Autowired
    private LeavesTrackerService leavesTrackerService;

    @GetMapping("/user")
    public ResponseEntity<ResponseObject> getAllLeavesForUser(
            @AuthenticationPrincipal UserDto userDto
    ) {
        Iterable<LeavesTracker> leaves = leavesTrackerService.getAllLeaveRequestsForUser(userDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched all leave requests");
        responseObject.setData(leaves);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseObject> makeLeaveRequest(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody CreateLeaveTrackerDto leaveRequest
    ) {
        LeavesTracker leave = leavesTrackerService.makeLeaveRequest(userDto, leaveRequest);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created leave request");
        responseObject.setData(leave);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id")
    public ResponseEntity<ResponseObject> deleteLeaveRequest(
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id
    ) {
        leavesTrackerService.deleteLeaveRequest(userDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted leave request");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> approveLeaveRequest(
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id,
            @Valid @RequestBody ApproveLeaveRequestDto leaveRequestDto
    ) {
        LeavesTracker leavesTracker = leavesTrackerService.approveLeaveRequest(userDto, leaveRequestDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully approved leave request");
        responseObject.setData(leavesTracker);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<ResponseObject> rejectLeaveRequest(
            @AuthenticationPrincipal UserDto userDto,
            @PathVariable("id") Integer id,
            @Valid @RequestBody RejectLeaveRequestDto leaveRequestDto
    ) {
        LeavesTracker leavesTracker = leavesTrackerService.rejectLeaveRequest(userDto, leaveRequestDto, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully rejected leave request");
        responseObject.setData(leavesTracker);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }
}
