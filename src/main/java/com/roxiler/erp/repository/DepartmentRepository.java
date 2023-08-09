package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Users;
import lombok.Data;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Organization org SET org.deletedAt = CURRENT_TIMESTAMP, org.deletedBy = :deletedBy WHERE org.id = :id")
    void softDeleteById(Integer id, String deletedBy);
}
