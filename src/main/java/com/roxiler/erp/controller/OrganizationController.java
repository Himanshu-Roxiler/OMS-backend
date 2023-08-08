package com.roxiler.erp.controller;

import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/")
    public Iterable<Organization> getAllOrganizations() {

        Iterable<Organization> organizations = organizationService.getAllOrganizations();

        for(Organization organization: organizations) {
            System.out.println("ORGANIZATION: " + organization.getName());
        }

        return organizations;
    }

    @PostMapping("/")
    public Organization addOrganization(@Valid @RequestBody Organization organization) {

        Organization newOrganization = organizationService.saveOrganization(organization);

        return newOrganization;
    }

    @PatchMapping("/{id}")
    public String updateOrganization(@Valid @RequestBody Organization organization, @PathVariable("id") Integer id) {

        String result = organizationService.updateOrganization(organization, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteOrganization(@PathVariable("id") Integer id) {

        String result = organizationService.deleteOrganization(id);

        return result;
    }

}
