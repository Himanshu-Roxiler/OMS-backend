package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.UserRole;
import com.roxiler.erp.model.Users;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    UserRole readById(Integer id);

    UserRole readByName(String name);

    @Query("SELECT role FROM UserRole role WHERE role.organization = :org AND LOWER(role.name) LIKE %:search%")
    Page<UserRole> getRolesListWithOrg(Organization org, String search, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE UserRole userRole SET userRole.deletedAt = CURRENT_TIMESTAMP, userRole.deletedBy = :deletedBy WHERE userRole.id = :id")
    void softDeleteById(Integer id, String deletedBy);

}
