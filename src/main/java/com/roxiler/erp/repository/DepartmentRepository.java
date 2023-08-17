package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.Users;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Department dept SET dept.deletedAt = CURRENT_TIMESTAMP, dept.deletedBy = :deletedBy WHERE dept.id = :id")
    void softDeleteById(Integer id, String deletedBy);

    @Query("SELECT dept FROM Department dept WHERE dept.id = :id AND dept.organization = :org")
    Optional<Department> getDeptWithOrg(Integer id, Organization org);
}
