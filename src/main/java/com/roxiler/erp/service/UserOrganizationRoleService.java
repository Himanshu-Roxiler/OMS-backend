package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserOrganizationRole;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UserOrganizationRoleRepository;
import com.roxiler.erp.repository.UserRoleRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrganizationRoleService {

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole getUserRole(Users user, Organization org) {
        UserOrganizationRole userOrganizationRole = userOrganizationRoleRepository.findUserRole(user, org);
        return userOrganizationRole.getRole();
    }

    @RequiredPermission(permission = PermissionConstants.ADMIN)
    public UserOrganizationRole createUserOrgRole(Users user, Organization org, Integer roleId) {
        UserOrganizationRole userOrganizationRole = new UserOrganizationRole();
        userOrganizationRole.setUser(user);
        userOrganizationRole.setOrganization(org);
        UserRole userRole = userRoleRepository.readById(roleId);
        userOrganizationRole.setRole(userRole);
        UserOrganizationRole userOrgRole = userOrganizationRoleRepository.save(userOrganizationRole);
        return userOrgRole;
    }

    public Iterable<UserOrganizationRole> getAll() {
        Iterable<UserOrganizationRole> userOrganizationRoles = userOrganizationRoleRepository.findAll();
        //Iterable<Organization> organizations = organizationRepository.getPopulatedOrganizations();

        return userOrganizationRoles;
    }
}
