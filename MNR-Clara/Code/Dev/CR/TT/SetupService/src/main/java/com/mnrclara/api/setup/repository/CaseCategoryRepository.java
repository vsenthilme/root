package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.casecategory.CaseCategory;

@Repository
public interface CaseCategoryRepository extends JpaRepository<CaseCategory, Long>{

	public List<CaseCategory> findAll();
	public Optional<CaseCategory> findByCaseCategoryIdAndDeletionIndicator (Long caseCategoryId, Long deletionIndicator);
	
	public List<CaseCategory> findByClassIdAndCategoryStatus(Long classId, String string);
}