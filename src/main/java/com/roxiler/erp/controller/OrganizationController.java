package com.roxiler.erp.controller;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllOrganizations(@AuthenticationPrincipal UserDto user) {

        Iterable<Organization> organizations = organizationService.getAllOrganizations();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched organizations");
        responseObject.setData(organizations);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addOrganization(@AuthenticationPrincipal UserDto user, @Valid @RequestBody Organization organization) {

        Organization newOrganization = organizationService.saveOrganization(organization, user.getId());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created organization");
        responseObject.setData(newOrganization);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateOrganization(@AuthenticationPrincipal UserDto user, @Valid @RequestBody Organization organization, @PathVariable("id") Integer id) {

        Organization org = organizationService.updateOrganization(organization, id, user.getEmail());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated organization");
        responseObject.setData(org);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrganization(@AuthenticationPrincipal UserDto user, @PathVariable("id") Integer id) {

        organizationService.softDeleteOrganization(id, user.getEmail());
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted organization");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
