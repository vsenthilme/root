package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.casesubcategory.CaseSubcategory;

@Repository
public interface CaseSubcategoryRepository extends JpaRepository<CaseSubcategory, Long>{

	public List<CaseSubcategory> findAll();
	
	//`LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `CASE_SUB_CATEGORY_ID`, `CASE_SUB_CATEGORY`, `IS_DELETED`
	Optional<CaseSubcategory> 
		findByLanguageIdAndClassIdAndCaseCategoryIdAndCaseSubcategoryIdAndDeletionIndicator
			(String languageId, Long classId, Long caseCategoryId, Long caseSubcategoryId, Long deletionIndicator);

	public CaseSubcategory findByCaseSubcategoryId(Long caseSubcategoryId);

}