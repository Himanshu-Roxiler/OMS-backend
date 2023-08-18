package com.roxiler.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roxiler.erp.model.Feature;
import com.roxiler.erp.model.ResponseObject;
import com.roxiler.erp.service.FeatureService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllFeatures() {

        Iterable<Feature> features = featureService.getAllFeaturesIterable();

        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully fetched departments");
        responseObject.setData(features);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addFeature(@Valid @RequestBody Feature feature) {

        Feature feature2 = featureService.saveFeature(feature);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully created feature.");
        responseObject.setData(feature2);

        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> updateFeature(@Valid @RequestBody Feature feature, @PathVariable("id") Integer id) {

        Feature feature2 = featureService.updatFeature(feature, id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully updated feature");
        responseObject.setData(feature2);
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteFeature(@PathVariable("id") Integer id) {

        featureService.deleteFeature(id);
        ResponseObject responseObject = new ResponseObject();
        responseObject.setIs_success(true);
        responseObject.setMessage("Successfully deleted feature.");
        ResponseEntity<ResponseObject> response = new ResponseEntity<>(responseObject, HttpStatus.OK);

        return response;
    }

}
