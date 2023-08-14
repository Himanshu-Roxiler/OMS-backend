package com.roxiler.erp.repository;

import com.roxiler.erp.model.Users;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Users user SET user.deletedAt = CURRENT_TIMESTAMP, user.deletedBy = :deletedBy WHERE user.id = :id")
    void softDeleteById(Integer id, String deletedBy);

    Users readByEmail(String email);
    Users readByUsername(String username);
}
