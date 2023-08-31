package com.roxiler.erp.controller;

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
@RequestMapping(value = "/leave-tracker")
public class LeaveTrackerController {

    @Autowired
    private LeaveTrackerService leaveTrackerService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLeavesTrackers(@AuthenticationPrincipal UserDto userDto) {

        Iterable<LeavesTracker> LeavesTrackers = leaveTrackerService.getAllLeavesTrackersIterable(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(LeavesTrackers);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addLeavesTracker(@Valid @RequestBody LeavesTracker LeavesTracker, @AuthenticationPrincipal UserDto userDto) {

        LeavesTracker LeavesTracker2 = leaveTrackerService.saveLeavesTracker(LeavesTracker, userDto);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesTracker.");
        responseObject.setData(LeavesTracker2);

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

    
}
