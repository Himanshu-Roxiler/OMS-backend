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
import com.roxiler.erp.model.LeavesSystem;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.LeaveSystemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/leave-system")
public class LeaveSystemController {

    @Autowired
    private LeaveSystemService leaveSystemService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLeavesSystems(@AuthenticationPrincipal UserDto userDto) {

        Iterable<LeavesSystem> LeavesSystems = leaveSystemService.getAllLeavesSystemsIterable();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(LeavesSystems);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addLeavesSystem(@AuthenticationPrincipal UserDto userDto,@Valid @RequestBody LeavesSystem LeavesSystem) {

        LeavesSystem LeavesSystem2 = leaveSystemService.saveLeavesSystem(LeavesSystem);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created LeavesSystem.");
        responseObject.setData(LeavesSystem2);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateLeavesSystem(@Valid @RequestBody LeavesSystem LeavesSystem, @AuthenticationPrincipal UserDto userDto) {

        LeavesSystem LeavesSystem2 = leaveSystemService.updatLeavesSystem(LeavesSystem, userDto.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated LeavesSystem");
        responseObject.setData(LeavesSystem2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteLeavesSystem(@PathVariable("id") Integer id) {

        leaveSystemService.deleteLeavesSystem(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted LeavesSystem.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }
    
}