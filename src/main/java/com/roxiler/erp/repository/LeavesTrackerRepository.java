package com.roxiler.erp.repository;

import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LeavesTrackerRepository extends JpaRepository<LeavesTracker, Integer> {

    @Query(value = "SELECT leaveTracker FROM LeavesTracker leaveTracker WHERE leaveTracker.user= :user")
    Iterable<LeavesTracker> findAllByUser(Users user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE LeavesTracker leaveTracker SET leaveTracker.deletedAt = CURRENT_TIMESTAMP, leaveTracker.deletedBy = :deletedBy WHERE leave.id = :id", nativeQuery = true)
    void softDeleteById(Integer id, String deletedBy);
}
