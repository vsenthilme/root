package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.expirationdate.ExpirationDate;
import com.mnrclara.api.management.model.expirationdate.IExpirationDate;

@Repository
@Transactional
public interface ExpirationDateRepository extends JpaRepository<ExpirationDate, Long> {
	public List<ExpirationDate> findAll();
	public Optional<ExpirationDate> findByClientId(String clientId);
	
	// `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `CLIENT_ID`, `DOC_TYPE`
	public Optional<ExpirationDate> 
		findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentTypeAndDeletionIndicator (
				String languageId,
				Long classId,
				String matterNumber,
				String clientId,
				String documentType,
				Long deletionIndicator);
	
	public List<ExpirationDate> findByMatterNumberAndDocumentType (String matterNumber, String documentType);
	
	@Query(value = "SELECT CLIENT_ID AS clientId, MATTER_NO AS matterNumber, REMINDER_DATE AS reminderDate, "
			+ " REMINDER_TEXT AS reminderText\r\n"
			+ "	FROM tblexpirationdate \r\n"
			+ "	WHERE DATE(REMINDER_DATE) = CURDATE() AND IS_DELETED = 0", 
			nativeQuery = true)
	List<IExpirationDate> findByReminderDateForReminderSMS();
	
	public List<ExpirationDate> findByClientIdAndMatterNumber (String clientId, String matterNumber);
	public Optional<ExpirationDate> findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDeletionIndicator(
			String languageId, Long classId, String matterNo, String clientId, long l);

	@Query(value = "SELECT e.class_id as classId,ci.class as className, \n" +
			"e.client_id as clientId, cg.FIRST_LAST_NM as clientName, cg.EMAIL_ID as clientEmail,\n" +
			" e.matter_no as matterNumber, m.MATTER_TEXT as matterDescription, e.doc_type as documentType,\n" +
			"e.reminder_date as remainderDate,e.reminder_days as remainderDays,e.expiration_date as expirationDate \n" +
			"from tblexpirationdate as e\n" +
			"join tblclassid as ci on e.class_id = ci.class_id\n" +
			"join tblclientgeneralid as cg on e.client_id = cg.CLIENT_ID\n" +
			"join tblmattergenaccid as m on e.matter_no = m.MATTER_NO Where e.IS_DELETED = 0",
			nativeQuery = true)
	List<IExpirationDate> findAllExpirationDate();
}