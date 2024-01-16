package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.timekeepercode.TimekeeperCode;

@Repository
public interface TimekeeperCodeRepository extends JpaRepository<TimekeeperCode, Long>{

	public List<TimekeeperCode> findAll();
	Optional<TimekeeperCode> findByTimekeeperCode (String timekeepercodeId);
	
	// `LANG_ID`, `CLASS_ID`, `TK_CODE`, `USR_TYP_ID`, `DEF_RATE`, `IS_DELETED`
	Optional<TimekeeperCode>
		findByLanguageIdAndClassIdAndTimekeeperCodeAndUserTypeIdAndDefaultRateAndDeletionIndicator 
		(String languageId, Long classId, String timeKeeperCode, Long userTypeId, Double defaultRate, Long deletionIndicator);
	
	public TimekeeperCode findByTimekeeperCodeAndDeletionIndicator(String timekeeperCode, Long deletionIndicator);
}