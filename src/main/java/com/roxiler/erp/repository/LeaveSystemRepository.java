package com.roxiler.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.LeavesSystem;

public interface LeaveSystemRepository extends JpaRepository<LeavesSystem, Integer> {
     @Query(value = "SELECT * FROM leaves_system leaves WHERE user_id=:userId", nativeQuery = true)
    Iterable<Leaves> findAllByUser(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE leaves_system leave SET leave.deletedAt = CURRENT_TIMESTAMP, leave.deletedBy = :deletedBy WHERE leave.id = :id", nativeQuery = true)
    void softDeleteById(Integer id, String deletedBy);
}
