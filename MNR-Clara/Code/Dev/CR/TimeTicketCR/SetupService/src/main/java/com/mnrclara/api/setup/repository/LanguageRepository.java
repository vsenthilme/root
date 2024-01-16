package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.language.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long>{

	public List<Language> findAll();
	Optional<Language> findByLanguageId (String languageId);
	
	Optional<Language> findByLanguageIdAndClassId(String languageId, Long classId);
	
	// `LANG_ID`, `CLASS_ID`, `IS_DELETED`
	Optional<Language>
		findByLanguageIdAndClassIdAndDeletionIndicator (String languageId, Long classId, Long deletionIndicator);
}