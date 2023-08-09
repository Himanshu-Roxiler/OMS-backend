package com.roxiler.erp.controller;

import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.service.DesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/designation")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @GetMapping("/")
    public Iterable<Designation> getAllDesignations() {

        Iterable<Designation> designations = designationService.getAllDesignations();

        for(Designation desg: designations) {
            System.out.println("DESIGNATION: " + desg.getName());
        }

        return designations;
    }

    @PostMapping("/")
    public Designation addDesignation(@Valid @RequestBody Designation designation) {

        Designation desg = designationService.saveDesignation(designation, 1);

        return desg;
    }

    @PatchMapping("/{id}")
    public String updateDesignation(@Valid @RequestBody Designation designation, @PathVariable("id") Integer id) {

        String result = designationService.updateDesignation(designation, id);

        return result;
    }

    @DeleteMapping("/{id}")
    public String deleteDesignation(@PathVariable("id") Integer id) {

        String result = designationService.deleteDesignation(id);

        return result;
    }

}
