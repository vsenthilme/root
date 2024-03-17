package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.matteritform.MatterITForm;

@Repository
@Transactional
public interface MatterITFormRepository extends JpaRepository<MatterITForm,Long>,JpaSpecificationExecutor<MatterITForm> {

	public List<MatterITForm> findAll();
	public Optional<MatterITForm> findById (Long matterItFormId);
	public Optional<MatterITForm> findByIntakeFormNumber(String intakeFormNumber);
	
	/*
	 * `LANG_ID`, `CLASS_ID`,`MATTER_NO`, `CLIENT_ID`,  `IT_FORM_ID`,  `IT_FORM_NO`
	 */
	public Optional<MatterITForm> findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndIntakeFormIdAndIntakeFormNumberAndDeletionIndicator(
			String languageId, 
			Long classId, 
			String matterNumber,
			String clientId, 
			Long intakeFormId, 
			String intakeFormNumber, 
			Long deletionIndicator);
}

