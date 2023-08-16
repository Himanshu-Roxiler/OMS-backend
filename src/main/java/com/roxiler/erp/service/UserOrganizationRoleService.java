package com.roxiler.erp.service;

import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserOrganizationRole;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UserOrganizationRoleRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrganizationRoleService {

    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;

    public UserRole getUserRole(Users user, Organization org) {
        UserOrganizationRole userOrganizationRole = userOrganizationRoleRepository.findUserRole(user, org);
        return userOrganizationRole.getRole();
    }
}
