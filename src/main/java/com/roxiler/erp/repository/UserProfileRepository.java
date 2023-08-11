package com.roxiler.erp.repository;

import com.roxiler.erp.model.UserProfile;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Organization org SET org.deletedAt = CURRENT_TIMESTAMP, org.deletedBy = :deletedBy WHERE org.id = :id")
    void softDeleteById(Integer id, String deletedBy);
}
