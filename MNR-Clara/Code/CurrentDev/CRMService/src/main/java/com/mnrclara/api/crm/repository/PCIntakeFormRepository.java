package com.mnrclara.api.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;

@Repository
@Transactional
public interface PCIntakeFormRepository extends JpaRepository<PCIntakeForm, Long>, JpaSpecificationExecutor<PCIntakeForm> {
	public Optional<PCIntakeForm> findByIntakeFormNumber(@Param("intakeFormNumber") String intakeFormNumber);
	public Optional<PCIntakeForm> findByIntakeFormNumberAndInquiryNumber(String intakeFormNumber, String inquiryNumber);
	public Optional<PCIntakeForm> findByInquiryNumberAndIntakeFormIdAndIntakeFormNumber(String inquiryNumber, Long intakeFormId, String intakeFormNumber );
	public List<PCIntakeForm> findByClassId(Long classId);
	
	// Validate data types LANG_ID/INQ_NO/CLASS_ID/IT_FORM_ID/IT_FORM_NO/IS_DELETED=0 for duplicate records before inserting into PICINTAKEFORM table
	@Query (value = "SELECT * FROM MNRCLARA.tblpcintakeformid\r\n"
			+ "WHERE LANG_ID = :langId "
			+ "AND INQ_NO = :inqNumber "
			+ "AND CLASS_ID = :classId "
			+ "AND IT_FORM_ID = :itFormId "
			+ "AND IT_FORM_NO = :itFormNo "
			+ "AND IS_DELETED = 0", nativeQuery = true)
	public List<PCIntakeForm> findDuplicatePCIntakeForm(@Param(value = "langId") String langId,
													@Param(value = "inqNumber") String inqNumber, 
													@Param(value = "classId") Long classId,		
													@Param(value = "itFormId") Long itFormId,
													@Param(value = "itFormNo") String itFormNo
													);
}	
