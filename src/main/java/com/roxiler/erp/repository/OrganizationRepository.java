package com.roxiler.erp.repository;

import com.roxiler.erp.model.Organization;
import com.roxiler.erp.model.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Transactional
    @EntityGraph(attributePaths = "departments")
    default Iterable<Organization> getPopulatedOrganizations() {
        return findAll();
    }

    ;

    Organization readById(Integer id);
}
