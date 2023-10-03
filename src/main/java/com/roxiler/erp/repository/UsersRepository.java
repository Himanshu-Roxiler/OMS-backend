package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Organization;
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
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Users user SET user.deletedAt = CURRENT_TIMESTAMP, user.deletedBy = :deletedBy WHERE user.id = :id")
    void softDeleteById(Integer id, String deletedBy);

    Users readByEmail(String email);

    Optional<Users> findByEmail(String email);

    @Query("SELECT user FROM Users user WHERE user.organization = :org AND LOWER(CONCAT(user.firstName, ' ', user.lastName)) LIKE %:search%")
    Page<Users> getUsersListWithOrg(Organization org, String search, Pageable pageable);

    @Query("SELECT user FROM Users user WHERE user.organization = :org ORDER BY user.createdAt DESC")
    Page<Users> getNewUsersListWithOrg(Organization org, Pageable pageable);

    Users readByUsernameAndActiveOrganization(String username, Integer orgId);

    Optional<Users> findByUsernameAndActiveOrganization(String username, Integer orgId);

    Users readByGoogleId(String googleId);

    Users readByOutlookId(String outlookId);
}
