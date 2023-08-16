package com.roxiler.erp.aspects;

import com.roxiler.erp.interfaces.RequiredPermission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PermissionAspect {

    @Before(value = "@annotation(com.roxiler.erp.interfaces.RequiredPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
        System.out.println("\n\nThe value of the argument passed is: \n\n" + requiredPermission.permission());
    }
}
