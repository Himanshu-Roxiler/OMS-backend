package com.roxiler.erp.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.roxiler.erp.dto.roles.CreateUserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.model.Feature;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.FeatureRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UserRoleRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private FeatureRepository featureRepository;


    public UserRole saveUserRole(CreateUserRoleDto userRoleDto, Integer userId, Integer orgId) {
        Optional<Users> user = usersRepository.findById(userId);
        UserRole userRole = new UserRole();
        if (user.isPresent()) {
            Organization organization = organizationRepository.readById(orgId);
            userRole.setOrganization(organization);
            userRole.setName(userRoleDto.getName());
            Set<Feature> features = new HashSet<>();

            for (Integer featureId : userRoleDto.getFeatureIds()) {
                Optional<Feature> feature = featureRepository.findById(featureId);
                if (feature.isPresent()) {
                    features.add(feature.get());
                    System.out.println("\nFEATURE ID: " + feature.get().getName());
                }

                //userRoleRepository.save(userRole);
            }

            for (Feature feature : features) {
                System.out.printf("FEATURE: " + feature.getName());
            }
            System.out.println("\nFEATURES: " + features.size());

//            Set<Feature> features = new HashSet<Feature>();
//            if (userRole.getFeatures() != null) {
//                for (Feature feature : userRole.getFeatures()) {
//                    if (feature.getId() == null) {
//                        Feature newFeature = featureRepository.save(feature);
//                        features.add(newFeature);
//                    } else {
//                        features.add(featureRepository.findById(feature.getId()).get());
//                    }
//                }
//                userRole.setFeatures(features);
//            }
            //userRole.getUsers().add(user.get());
            userRole.getFeatures().addAll(features);
            UserRole savedUserRole = userRoleRepository.save(userRole);
//            if (features != null) {
//                for (Feature feature : features) {
//                    feature.getRoles().add(saveUserRole);
//                    featureRepository.save(feature);
//                }
//                userRole.setFeatures(features);
//            }
            return savedUserRole;
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    public Iterable<UserRole> getAllUserRolesIterable() {
        return userRoleRepository.findAll();
    }

    public void deleteUserRole(Integer id) {
        Optional<UserRole> UserRole = userRoleRepository.findById(id);
        if (UserRole.isEmpty()) {
            throw new EntityNotFoundException("UserRole " + id + " does not exist");
        }
        if (UserRole.isPresent()) {
            for (Feature feature : UserRole.get().getFeatures()) {
                feature.getRoles().remove(UserRole.get());
                featureRepository.save(feature);
            }
            Optional<Organization> optionalOrganization = organizationRepository.findById(UserRole.get().getOrganization().getId());
            if (optionalOrganization.isPresent()) {
                optionalOrganization.get().getRoles().remove(UserRole.get());
                organizationRepository.save(optionalOrganization.get());
            }
            userRoleRepository.deleteById(UserRole.get().getId());
        }
    }

    public UserRole updatUserRole(UserRole updateUserRole, Integer id) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(id);
        if (optionalUserRole.isEmpty()) {
            throw new EntityNotFoundException("UserRole " + id + " does not exist");
        }
        if (optionalUserRole.isPresent()) {
            UserRole existingUserRole = optionalUserRole.get();
            existingUserRole.setName(updateUserRole.getName());
            //existingUserRole.setIsGlobal(updateUserRole.getIsGlobal());
            return userRoleRepository.save(existingUserRole);
        }
        return updateUserRole;

    }

    public UserRole getUserRoleById(Integer id) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(id);
        if (optionalUserRole.isEmpty()) {
            throw new EntityNotFoundException("UserRole " + id + " does not exist");
        }
        return optionalUserRole.get();
    }


}
