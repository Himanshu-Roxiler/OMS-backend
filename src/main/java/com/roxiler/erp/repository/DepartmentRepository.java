package com.roxiler.erp.repository;

import com.roxiler.erp.model.Department;
import com.roxiler.erp.model.Users;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {
}
