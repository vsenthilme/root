package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.intakeform.IntakeFormID;

@Repository
public interface IntakeFormRepository extends JpaRepository<IntakeFormID, Long>{

	public List<IntakeFormID> findAll();
	public Optional<IntakeFormID> findByIntakeFormIdAndDeletionIndicator (Long intakeFormId, Long deletionIndicator);
	public IntakeFormID findByClassIdAndClientTypeId(Long classId, Long clientTypeId);
	
	// `LANG_ID`, `CLASS_ID`, `CLIENT_TYP_ID`, `IT_FORM_ID`, `IS_DELETED`
	public Optional<IntakeFormID>  
		findByLanguageIdAndClassIdAndClientTypeIdAndIntakeFormIdAndDeletionIndicator
		(String languageId, Long classId, Long clientTypeId, Long intakeFormId, Long deletionIndicator);
}