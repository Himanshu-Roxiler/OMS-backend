package com.roxiler.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.Users;

import java.util.Optional;

public interface LeavesRepository extends JpaRepository<Leaves, Integer> {
    //    @Query(value = "SELECT leave FROM Leaves leave WHERE leave.user= :user")
    Leaves readByUser(Users user);

    @Query("SELECT leave FROM Leaves leave WHERE leave.user = :user")
    Optional<Leaves> findByUser(Users user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE leaves leave SET leave.deletedAt = CURRENT_TIMESTAMP, leave.deletedBy = :deletedBy WHERE leave.id = :id", nativeQuery = true)
    void softDeleteById(Integer id, String deletedBy);
}
