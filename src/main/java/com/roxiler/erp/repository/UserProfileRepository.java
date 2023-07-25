package com.roxiler.erp.repository;

import com.roxiler.erp.model.UserProfile;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
}
