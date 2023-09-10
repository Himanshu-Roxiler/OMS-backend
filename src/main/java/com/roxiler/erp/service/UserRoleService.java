package com.roxiler.erp.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.roles.CreateUserRoleDto;
import com.roxiler.erp.dto.roles.ListRolesDto;
import com.roxiler.erp.model.*;
import com.roxiler.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    public UserRole saveUserRole(CreateUserRoleDto userRoleDto, Integer userId, Integer orgId) {
        Optional<Users> user = usersRepository.findById(userId);
        UserRole userRole = new UserRole();
        if (user.isPresent()) {
            Organization organization = organizationRepository.readById(orgId);
            userRole.setOrganization(organization);
            userRole.setName(userRoleDto.getName());
            userRoleRepository.save(userRole);
            Set<Feature> features = new HashSet<Feature>();

            for (Integer featureId : userRoleDto.getFeatureIds()) {
                Optional<Feature> feature = featureRepository.findById(featureId);
                if (feature.isPresent()) {
                    feature.get().getRoles().add(userRole);
                    featureRepository.save(feature.get());
                    //userRole.getFeatures().add(feature.get());
                    //userRoleRepository.save(userRole);
                    features.add(feature.get());
                    System.out.println("\nFEATURE ID: " + feature.get());
                }
            }

            Feature profileFeature = featureRepository.findByName(PermissionConstants.PROFILE);
            profileFeature.getRoles().add(userRole);
            featureRepository.save(profileFeature);
            features.add(profileFeature);

            Feature leaveFeature = featureRepository.findByName(PermissionConstants.LEAVE);
            leaveFeature.getRoles().add(userRole);
            featureRepository.save(leaveFeature);
            features.add(leaveFeature);

            userRole.getFeatures().addAll(features);
            userRoleRepository.save(userRole);

            return userRole;
        } else {
            throw new EntityNotFoundException("User is not found");
        }
    }

    public Page<UserRole> getAllUserRolesIterable(UserDto userDto, ListRolesDto listRolesDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());
        if (org.isEmpty()) {
            throw new EntityNotFoundException("No organization is found for user " + userDto.getOrgId());
        }
        int pageSize = 10;
        int pageNum = 1;
        if (listRolesDto.getPageNum().describeConstable().isPresent()) {
            pageNum = listRolesDto.getPageNum();
        }
        if (listRolesDto.getPageSize().describeConstable().isPresent()) {
            pageSize = listRolesDto.getPageSize();
        }
        Pageable pageable = PageRequest.of(
                pageNum - 1,
                pageSize);
//        Iterable<UserRole> userRoles = userRoleRepository.findAll();
        Page<UserRole> userRoles = userRoleRepository.getRolesListWithOrg(org.get(), pageable);
        return userRoles;
    }

    @Transactional
    public void deleteUserRole(Integer id, UserDto userDto) {
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException("UserRole " + id + " does not exist");
        }
        if (userRole.isPresent()) {
//            if(!userRole.get().getUserOrganizationRole().isEmpty()) {
//                throw new RequestRejectedException("The user role is associated with an user, can't delete");
//            }
            if (!Objects.equals(userDto.getOrgId(), userRole.get().getOrganization().getId())) {
                throw new AuthorizationServiceException("You are not allowed to perform this action");
            }
            for (Feature feature : userRole.get().getFeatures()) {
                feature.getRoles().remove(userRole.get());
                featureRepository.save(feature);
            }
            Optional<Organization> optionalOrganization = organizationRepository.findById(userRole.get().getOrganization().getId());
            if (optionalOrganization.isPresent()) {
                optionalOrganization.get().getRoles().remove(userRole.get());
                organizationRepository.save(optionalOrganization.get());
                Users user = usersRepository.readByUsernameAndActiveOrganization(userDto.getUsername(), optionalOrganization.get().getId());
//                Iterable<UserOrganizationRole> userOrganizationRoles = userOrganizationRoleRepository.findAllByRole(userRole.get());
//                System.out.printf("\n\nUSER-ORG-ROLES: \n" + userOrganizationRoles);
//                for (UserOrganizationRole userOrganizationRole : userOrganizationRoles) {
//                    System.out.printf("\nID: " + userOrganizationRole.getId());
//                    userOrganizationRoleRepository.deleteById(userOrganizationRole.getId());
//                }
            }

            userRoleRepository.deleteById(userRole.get().getId());
        }
    }

    public UserRole updatUserRole(UserRole updateUserRole, Integer id, UserDto userDto) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(id);
        if (optionalUserRole.isEmpty()) {
            throw new EntityNotFoundException("UserRole " + id + " does not exist");
        }
        if (optionalUserRole.isPresent()) {
            if (!Objects.equals(userDto.getOrgId(), optionalUserRole.get().getOrganization().getId())) {
                throw new AuthorizationServiceException("You are not authorized to perform this action");
            }
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
