package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.activitycode.ActivityCode;

@Repository
public interface ActivityCodeRepository extends JpaRepository<ActivityCode, Long>{

	public List<ActivityCode> findAll();
	public Optional<ActivityCode> findByActivityCodeAndDeletionIndicator (String activityCodeId, Long deletionIndicator);
	
	// `LANG_ID`, `CLASS_ID`, `ACT_CODE`
	Optional<ActivityCode> 
		findByLanguageIdAndClassIdAndActivityCodeAndDeletionIndicator 
		(String languageId, Long classId, String activitycodeId, Long deletionIndicator);
}