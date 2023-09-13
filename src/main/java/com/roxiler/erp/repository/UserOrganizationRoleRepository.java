package com.roxiler.erp.repository;

import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserOrganizationRole;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, Integer> {

    UserOrganizationRole findByUserAndOrganization(Users user, Organization org);

    @Query("SELECT uor FROM UserOrganizationRole uor WHERE uor.user = :user AND uor.organization = :org")
    UserOrganizationRole findUserRole(Users user, Organization org);

    @Query("SELECT uor FROM UserOrganizationRole uor WHERE uor.role = :userRole")
    Iterable<UserOrganizationRole> findAllByRole(UserRole userRole);

    @Query("SELECT uor.role FROM UserOrganizationRole uor WHERE uor.user = :user AND uor.organization = :org")
    Iterable<UserRole> findUserRoleFromUserOrg(Users user, Organization org);

}
