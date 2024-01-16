package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.documenttemplate.DocumentTemplate;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long>{

	public List<DocumentTemplate> findAll();
	public Optional<DocumentTemplate> findByDocumentNumber (String documentTemplateId);
	
	public Optional<DocumentTemplate>
		findByLanguageIdAndClassIdAndCaseCategoryIdAndDocumentNumberAndDeletionIndicator 
		(String languageId, Long classId, Long caseCategoryId, String documentNumber, Long deletionIndicator);
	
	public DocumentTemplate findTopByOrderByCreatedOnDesc();
}