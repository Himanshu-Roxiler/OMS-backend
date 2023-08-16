package com.roxiler.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roxiler.erp.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> { 
}
