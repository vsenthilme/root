package com.mnrclara.api.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.setup.CaseCategory;

@Repository
public interface CaseCategoryRepository extends JpaRepository<CaseCategory, Long>{

	public List<CaseCategory> findAll();
	public CaseCategory findByCaseCategoryIdAndDeletionIndicator (Long caseCategoryId, Long deletionIndicator);
	public List<CaseCategory> findByClassIdAndCategoryStatus(Long classId, String string);
}