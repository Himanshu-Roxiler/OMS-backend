package com.roxiler.erp.repository;

import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LeavesTrackerRepository extends JpaRepository<LeavesTracker, Integer> {

    @Query(value = "SELECT leaveTracker FROM LeavesTracker leaveTracker WHERE leaveTracker.userId = :userId")
    Iterable<LeavesTracker> findAllByUser(Integer userId);

    @Query(value = "SELECT leaveTracker FROM LeavesTracker leaveTracker WHERE leaveTracker.reportingManager = :reportingManagerId")
    Iterable<LeavesTracker> findAllByReportingManager(Integer reportingManagerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE LeavesTracker leaveTracker SET leaveTracker.deletedAt = CURRENT_TIMESTAMP, leaveTracker.deletedBy = :deletedBy WHERE leave.id = :id", nativeQuery = true)
    void softDeleteById(Integer id, String deletedBy);

    @Query(value = "SELECT leaveTracker FROM LeavesTracker leaveTracker WHERE leaveTracker.userId = :userId AND (leaveTracker.startDate > CURRENT_DATE OR leaveTracker.endDate <= CURRENT_DATE)")
    Iterable<LeavesTracker> findUpcomingLeaves(Integer userId);

    @Query(value = "SELECT leaveTracker FROM LeavesTracker leaveTracker WHERE leaveTracker.userId = :id AND ((leaveTracker.startDate >= :startDate AND leaveTracker.startDate <= :endDate) OR (leaveTracker.endDate >= :startDate AND leaveTracker.endDate <= :endDate))")
    List<LeavesTracker> findByStartDateAndEndDate(Integer id, Date startDate, Date endDate);
}
