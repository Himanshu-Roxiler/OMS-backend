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
import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/leave")
public class LeaveController {
    
    @Autowired
    private LeaveService leaveService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLeavess(@AuthenticationPrincipal UserDto userDto) {

        Iterable<Leaves> Leavess = leaveService.getAllLeavessIterable(userDto);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(Leavess);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addLeaves(@AuthenticationPrincipal UserDto userDto ,@Valid @RequestBody Leaves Leaves) {

        Leaves Leaves2 = leaveService.saveLeaves(userDto,Leaves);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created Leaves.");
        responseObject.setData(Leaves2);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateLeaves(@Valid @RequestBody Leaves Leaves, @AuthenticationPrincipal UserDto userDto) {

        Leaves Leaves2 = leaveService.updatLeaves(Leaves, userDto.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated Leaves");
        responseObject.setData(Leaves2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteLeaves(@PathVariable("id") Integer id) {

        leaveService.deleteLeaves(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted Leaves.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }


}
