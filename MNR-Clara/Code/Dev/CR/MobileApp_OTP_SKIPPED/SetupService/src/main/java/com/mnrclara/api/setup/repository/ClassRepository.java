package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<com.mnrclara.api.setup.model.classid.Class, Long> {

	public List<com.mnrclara.api.setup.model.classid.Class> findAll();
	
	public com.mnrclara.api.setup.model.classid.Class findByClassIdAndDeletionIndicator(Long classId, Long deletionIndicator);
	
	// `LANG_ID`, `CLASS_ID`, `CLASS`, `COMP_ID`, `IS_DELETED`
	Optional<com.mnrclara.api.setup.model.classid.Class> 
			findByLanguageIdAndClassIdAndCompanyIdAndDeletionIndicator
				(String languageId, Long classId, String companyId, Long deletionIndicator);
}