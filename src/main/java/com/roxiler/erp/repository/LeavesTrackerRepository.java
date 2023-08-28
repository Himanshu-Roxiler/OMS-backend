package com.roxiler.erp.repository;

import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.service.LeavesTrackerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LeavesTrackerRepository extends JpaRepository<LeavesTracker, Integer> {

    @Query("SELECT leaves FROM LeavesTracker leaves WHERE leaves.user = :user")
    Iterable<LeavesTracker> findAllByUser(Users user);

    @Modifying
    @Transactional
    @Query("UPDATE LeavesTracker leave SET leave.deletedAt = CURRENT_TIMESTAMP, leave.deletedBy = :deletedBy WHERE leave.id = :id")
    void softDeleteById(Integer id, String deletedBy);
}
