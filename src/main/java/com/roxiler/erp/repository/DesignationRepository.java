package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Designation;
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
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Designation desg SET desg.deletedAt = CURRENT_TIMESTAMP, desg.deletedBy = :deletedBy WHERE desg.id = :id")
    void softDeleteById(Integer id, String deletedBy);

    @Query("SELECT desg FROM Designation desg WHERE desg.id = :id AND desg.organization = :org")
    Optional<Designation> getDesgWithOrg(Integer id, Organization org);

    @Query("SELECT desg FROM Designation desg WHERE desg.organization = :org")
    Iterable<Designation> getListDesgWithOrg(Organization org);

    
}
