package com.roxiler.erp.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.model.Feature;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.repository.FeatureRepository;
import com.roxiler.erp.repository.UserRoleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeatureService {
    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public Feature saveFeature(Feature feature) {
        if (feature.getRoles() != null) {
            Set<UserRole> roles = new HashSet<UserRole>();
            for (UserRole userRole : feature.getRoles()) {
                if (userRole.getId() != null) {
                    roles.add(userRoleRepository.save(userRole));
                } else {
                    roles.add(userRoleRepository.findById(userRole.getId()).get());
                }
            }
            feature.setRoles(roles);
        }
        Feature saveFeature = featureRepository.save(feature);
        return saveFeature;
    }

    public Iterable<Feature> getAllFeaturesIterable() {
        return featureRepository.findAll();
    }

    public void deleteFeature(Integer id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if (feature.isEmpty()) {
            throw new EntityNotFoundException("Feature " + id + " does not exist");
        }
        if(feature.isPresent()) {
            for (UserRole userRole : feature.get().getRoles()) {
                feature.get().getRoles().remove(userRole);
                userRoleRepository.save(userRole);
            }
            featureRepository.deleteById(feature.get().getId());
        }
    }

    public Feature updatFeature(Feature updateFeature, Integer id) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);

        if (optionalFeature.isEmpty()) {
            throw new EntityNotFoundException("Feature " + id + " does not exist");
        }

        if (optionalFeature.isPresent()) {
            Feature existingFeature = optionalFeature.get();
            existingFeature.setDisplayName(updateFeature.getDisplayName());
            existingFeature.setName(updateFeature.getName());
            return featureRepository.save(existingFeature);
        }
        return updateFeature;

    }

    public Feature getFeatureById(Integer id) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);
        if (optionalFeature.isEmpty()) {
            throw new EntityNotFoundException("Feature " + id + " does not exist");
        }
        return optionalFeature.get();
    }


}
