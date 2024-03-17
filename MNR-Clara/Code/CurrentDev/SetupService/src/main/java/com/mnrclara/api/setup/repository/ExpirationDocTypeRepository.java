package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.setup.model.expirationdoctype.ExpirationDocType;

@Repository
@Transactional
public interface ExpirationDocTypeRepository extends JpaRepository<ExpirationDocType, Long> {
	public List<ExpirationDocType> findAll();
	public Optional<ExpirationDocType> findByDocumentType(String documentType);
	
	// `LANG_ID`, `CLASS_ID`, `DOC_TYPE`, `IS_DELETED`
	Optional<ExpirationDocType>
		findByLanguageIdAndClassIdAndDocumentTypeAndDeletionIndicator 
		(String languageId, Long classId, String documentType, Long deletionIndicator);
}