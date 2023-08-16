package com.roxiler.erp.aspects;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.Feature;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UserOrganizationRoleRepository;
import com.roxiler.erp.repository.UsersRepository;
import com.roxiler.erp.service.UserOrganizationRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserOrganizationRoleService userOrganizationRoleService;

    @Before(value = "@annotation(com.roxiler.erp.interfaces.RequiredPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
        String permission = requiredPermission.permission();
        System.out.println("\n\nThe value of the argument passed is: \n\n" + permission);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) auth.getPrincipal();
        System.out.printf("\nAUTH: " + userDto.getEmail());
        Integer userId = userDto.getId();
        Users user = usersRepository.readByEmail(userDto.getEmail());
        Organization org = user.getOrganization();
        UserRole userRole = userOrganizationRoleService.getUserRole(user, org);
        //Set<UserRole> roles = user.getRoles();
        Set<Feature> features = userRole.getFeatures();
        if (features.stream().noneMatch(feature -> feature.getName().equals(permission))) {
            System.out.println("\n\nNo role matched\n\n");
            //throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
    }
}
