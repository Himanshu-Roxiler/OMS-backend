package com.roxiler.erp.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeatureService {
    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

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

    @RequiredPermission(permission = PermissionConstants.ROLES)
    public void deleteFeature(Integer id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if (feature.isEmpty()) {
            throw new EntityNotFoundException("Feature " + id + " does not exist");
        }
        if (feature.isPresent()) {
            for (UserRole userRole : feature.get().getRoles()) {
                feature.get().getRoles().remove(userRole);
                userRoleRepository.save(userRole);
            }
            featureRepository.deleteById(feature.get().getId());
        }
    }

    @RequiredPermission(permission = PermissionConstants.ROLES)
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

    @RequiredPermission(permission = PermissionConstants.ROLES)
    public Feature getFeatureById(Integer id) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);
        if (optionalFeature.isEmpty()) {
            throw new EntityNotFoundException("Feature " + id + " does not exist");
        }
        return optionalFeature.get();
    }

    @RequiredPermission(permission = PermissionConstants.PROFILE)
    public Iterable<Feature> getUserFeatures(UserDto userDto, Integer id) {

        if (!Objects.equals(userDto.getId(), id)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }

        Users user = usersRepository.readByEmail(userDto.getEmail());
        Organization org = user.getOrganization();
        UserOrganizationRole userOrganizationRole = userOrganizationRoleRepository.findUserRole(user, org);
        Integer roleId = userOrganizationRole.getRole().getId();
        UserRole userRole = userRoleRepository.readById(roleId);
        Iterable<Feature> features = userRole.getFeatures();

        return features;
    }

}
