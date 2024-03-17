package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.company.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{

	public List<Company> findAll();
	public Company findByCompanyId(String companyId);
	
	// `COMP_ID`, `LANG_ID`, `IS_DELETED`
	Optional<Company> findByCompanyIdAndLanguageIdAndDeletionIndicator 
	(String companyId, String languageId, Long deletionIndicator);
}