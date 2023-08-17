package com.roxiler.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roxiler.erp.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {   
}
