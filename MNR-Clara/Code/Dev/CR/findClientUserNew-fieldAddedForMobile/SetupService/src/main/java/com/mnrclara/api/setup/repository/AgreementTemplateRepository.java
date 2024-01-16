package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.agreementtemplate.AgreementTemplate;

@Repository
public interface AgreementTemplateRepository extends JpaRepository<AgreementTemplate, Long>{

	public List<AgreementTemplate> findAll();
	public Optional<AgreementTemplate> findByAgreementCode (String agreementCode);
	
	// `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `AGREEMENT_CODE`, `AGREEMENT_URL`, `IS_DELETED`
	public Optional<AgreementTemplate>
		findByLanguageIdAndClassIdAndCaseCategoryIdAndAgreementCodeAndAgreementUrlAndDeletionIndicator
			(String languageId, Long classId, Long caseCategoryId, String agreementCode, String agreementUrl, Long deletionIndicator);
	
	public List<AgreementTemplate> findByAgreementCodeAndClassId(String agreementCode, Long classId);
	
	public AgreementTemplate findTopByOrderByCreatedOnDesc();
}