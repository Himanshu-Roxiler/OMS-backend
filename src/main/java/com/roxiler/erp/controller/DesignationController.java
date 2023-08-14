package com.roxiler.erp.controller;

import com.roxiler.erp.dto.designation.CreateDesignationDto;
import com.roxiler.erp.dto.designation.UpdateDesignationDto;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.DesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/designation")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllDesignations() {

        Iterable<Designation> designations = designationService.getAllDesignations();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(designations);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> addDesignation(@Valid @RequestBody CreateDesignationDto designation) {

        Designation desg = designationService.saveDesignation(designation, 1);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created departments");
        responseObject.setData(desg);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateDesignation(@Valid @RequestBody UpdateDesignationDto designation, @PathVariable("id") Integer id) {

        Designation desg = designationService.updateDesignation(designation, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated designation");
        responseObject.setData(desg);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteDesignation(@PathVariable("id") Integer id) {

        designationService.deleteDesignation(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted designation");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
