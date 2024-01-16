package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.*;
import com.mnrclara.api.management.model.dto.IKeyValuePair;

@Repository
@Transactional
public interface ClientGeneralRepository extends PagingAndSortingRepository<ClientGeneral, Long>, 
	JpaSpecificationExecutor<ClientGeneral> {

	@QueryHints(@javax.persistence.QueryHint(name="org.hibernate.fetchSize", value="10000"))
	public List<ClientGeneral> findAll();

	public List<ClientGeneral> findAllByDeletionIndicator(Long deleteFlag);
	
	public List<ClientGeneral> findAllByStatusIdAndDeletionIndicator(Long statusId, Long deleteFlag);

	public Optional<ClientGeneral> findByClientId(String clientGeneralId);

	public List<ClientGeneral> findByClassIdAndDeletionIndicator(Long classId, Long deletedFlag);

	public List<ClientGeneral> findByClassIdAndDeletionIndicatorAndStatusId(Long classId, Long deletedFlag,
			Long statusId);

	@Query(value = "SELECT * FROM tblclientgeneralid " + " WHERE CLASS_ID = :classId AND "
			+ " CTD_ON BETWEEN :sCreatedOn AND :eCreatedOn AND \r\n" + " STATUS_ID = :statusId AND "
			+ " IS_DELETED = 0", nativeQuery = true)
	public List<ClientGeneral> findByCreatedOn(@Param("classId") Long classId, @Param("sCreatedOn") String sCreatedOn,
			@Param("eCreatedOn") String eCreatedOn, @Param("statusId") Long statusId);
	
	@Query(value = "SELECT * FROM tblclientgeneralid WHERE "
			+ " CTD_ON BETWEEN :sCreatedOn AND :eCreatedOn AND \r\n" + " STATUS_ID = :statusId AND "
			+ " IS_DELETED = 0", nativeQuery = true)
	public List<ClientGeneral> findByCreatedOnWithStatusId(@Param("sCreatedOn") String sCreatedOn,
			@Param("eCreatedOn") String eCreatedOn, @Param("statusId") Long statusId);
	
	//-------------BRING------TOP----RECORD------------------------------------------------------------
	public ClientGeneral findTopByOrderByCreatedOnDesc ();
	
	//----------------ConflictSearch-------------------------------------------------------------------
	/*
	 *`FIRST_NM`, `LAST_NM`, `LAST_FIRST_NM`, `FIRST_LAST_NM`, `CLIENT_ID`, `OCCUPATION`, `MAIL_ADDRESS`, `EMAIL_ID`, `COUNTRY`, `CITY`, 
	 *`ADDRESS_LINE3`, `ADDRESS_LINE2`, `ADDRESS_LINE1`
	 */
	@Query (value = "SELECT * FROM MNRCLARA.tblclientgeneralid WHERE \r\n"
			+ " MATCH (FIRST_NM, LAST_NM, LAST_FIRST_NM, FIRST_LAST_NM, CLIENT_ID, OCCUPATION, \r\n"
			+ " MAIL_ADDRESS, EMAIL_ID, CITY, \r\n"
			+ " ADDRESS_LINE3, ADDRESS_LINE2, ADDRESS_LINE1) \r\n"
			+ " AGAINST (:searchText)", nativeQuery = true)
	public List<ClientGeneral> findRecords(@Param(value = "searchText") String searchText);
	
	public ClientGeneral findByReferenceField2(String referenceField2);
	
	public List<ClientGeneral> findBySentToQBAndDeletionIndicator(Long sentToQB, Long deletedFlag);

	public ClientGeneral findTopBySentToQBAndDeletionIndicatorOrderByClientIdDesc(Long l, Long m);

	public ClientGeneral findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(long l, long m);

	//----------------------UI-APIs----------------------------------------------------------------------
	@Query(
			value = "SELECT CLIENT_ID AS clientId,\r\n"
					+ "LANG_ID AS languageId,\r\n"
					+ "CLASS_ID AS classId,CLIENT_CAT_ID AS clientCategoryId,\r\n"
					+ "TRANS_ID AS transactionId,\r\n"
					+ "POT_CLIENT_ID AS potentialClientId,\r\n"
					+ "INQ_NO AS inquiryNumber,\r\n"
					+ "IT_FORM_ID AS intakeFormId,\r\n"
					+ "IT_FORM_NO AS intakeFormNumber,\r\n"
					+ "FIRST_NM AS firstName,\r\n"
					+ "LAST_NM AS lastName,\r\n"
					+ "FIRST_LAST_NM AS firstNameLastName,\r\n"
					+ "LAST_FIRST_NM AS lastNameFirstName,\r\n"
					+ "CORP_CLIENT_ID AS corporationClientId,\r\n"
					+ "REFERRAL_ID AS referralId,\r\n"
					+ "EMail_ID AS emailId,\r\n"
					+ "CONT_NO AS contactNumber,\r\n"
					+ "ADDRESS_LINE1 AS addressLine1,\r\n"
					+ "ADDRESS_LINE2 AS addressLine2,\r\n"
					+ "ADDRESS_LINE3 AS addressLine3,\r\n"
					+ "CITY AS city,\r\n"
					+ "STATE AS state,\r\n"
					+ "COUNTRY AS country,\r\n"
					+ "ZIP_CODE AS zipCode,\r\n"
					+ "CONSULT_DATE AS consultationDate,\r\n"
					+ "SSN_ID AS socialSecurityNo,\r\n"
					+ "MAIL_ADDRESS AS mailingAddress,\r\n"
					+ "OCCUPATION AS occupation,\r\n"
					+ "STATUS_ID AS statusId,\r\n"
					+ "SUITE_NO AS suiteDoorNo,\r\n"
					+ "WORK AS workNo,\r\n"
					+ "HOME AS homeNo,\r\n"
					+ "FAX AS fax,\r\n"
					+ "ALT_EMAIL_ID AS alternateEmailId,\r\n"
					+ "IS_MAIL_SAME AS isMailingAddressSame,CTD_BY AS createdBy,\r\n"
					+ "CTD_ON AS createdOn,\r\n"
					+ "UTD_BY AS updatedBy,\r\n"
					+ "UTD_ON AS updatedOn "
					+ "FROM tblclientgeneralid \r\n"
					+ "WHERE CLASS_ID in :classId and IS_DELETED = 0", 
			countQuery = "SELECT * FROM tblclientgeneralid WHERE CLASS_ID in :classId and IS_DELETED = 0",
			nativeQuery = true)
	public Page<IClientGeneral> findAllClientsByClassId(@Param(value = "classId") List<Long> classId, Pageable pageable);
	
	@Query(value = "SELECT distinct CLIENT_ID as keyIndex, FIRST_LAST_NM AS value\r\n"
			+ "FROM tblclientgeneralid WHERE IS_DELETED = 0", nativeQuery = true)
	public Page<ClientGeneral> findAllByClassIdIn(List<Long> classId, Pageable pageable);
	
	@Query(value = "SELECT distinct CLIENT_ID as keyIndex, FIRST_LAST_NM AS value\r\n"
			+ "FROM tblclientgeneralid WHERE IS_DELETED = 0", nativeQuery = true)
	public List<IKeyValuePair> findClientNames();

	@Query (value = "SELECT statusId.STATUS_TEXT AS status \r\n"
			+ "FROM tblclientgeneralid client \r\n"
			+ "JOIN tblstatusid statusId ON client.STATUS_ID = statusId.STATUS_ID\r\n"
			+ "where client.CLIENT_ID = :clientId", nativeQuery = true)
	public String getStatusId (@Param(value = "clientId") String clientId);
	
	@Query (value = "SELECT CLS.CLASS AS VALUE \r\n"
			+ "FROM tblclientgeneralid CLIENT \r\n"
			+ "JOIN tblclassid CLS ON CLIENT.CLASS_ID = CLS.CLASS_ID\r\n"
			+ "WHERE CLIENT.CLIENT_ID = :clientId", nativeQuery = true)
	public String getClassName (@Param(value = "clientId") String clientId);
	
	//---------------------Docketwise-----------------------------------------------------------
	public ClientGeneral findTopByClassIdAndSentToDocketwiseAndDeletionIndicatorOrderByCreatedOn(long l, long m,
			long n);

	@Query (value = "SELECT statusId.STATUS_TEXT \r\n"
			+ "FROM tblstatusid statusId \r\n"
			+ "where statusId.STATUS_ID = :statusId", nativeQuery = true)
	public String getStatusName (@Param(value = "statusId") Long statusId);

	@Query (value = "SELECT referral_text \r\n"
			+ "FROM tblreferralid \r\n"
			+ "where referral_id = :referralId", nativeQuery = true)
	public String getReferralIdName (@Param(value = "referralId") Long referralId);

	//---------------------------Find Client General New-----------------------------------------------------------
	@Query(value = "select *, \n"
			+ "(case when x1.corporationId is not null and x1.corporationId != '' then \n"
			+ "(select first_last_nm from tblclientgeneralid where client_id = x1.corporationId) else null end) corporationClientId \n"
			+ "from \n"
			+ "(select \n"
			+ "tc.class classId, \n"
			+ "cg.client_id clientId, \n"
			+ "cg.first_last_nm firstNameLastName, \n"
			+ "cg.email_id emailId, \n"
			+ "cg.cont_no contactNumber, \n"
			+ "cg.address_line1 addressLine1, \n"
			+ "cg.address_line2 addressLine2, \n"
			+ "cg.it_form_no intakeFormNumber, \n"
			+ "cg.it_form_id intakeFormId, \n"
			+ "cg.city city, \n"
			+ "cg.state state, \n"
			+ "cg.zip_code zipCode, \n"
			+ "cg.ctd_on createdOnString, \n"
			+ "ts.status_text statusId, \n"
			+ "cg.corp_client_id corporationId \n"
			+ "from tblclientgeneralid cg \n"
			+ "left join tblclassid tc on tc.class_id = cg.class_id \n"
			+ "left join tblstatusid ts on ts.status_id = cg.status_id \n"
			+ "where \n"
			+ "(COALESCE(:clientId,null) IS NULL OR (cg.client_id IN (:clientId))) and \n"
			+ "(COALESCE(:intakeFormNumber,null) IS NULL OR (cg.it_form_no IN (:intakeFormNumber))) and \n"
			+ "(COALESCE(:statusId,null) IS NULL OR (cg.status_id IN (:statusId))) and \n"
			+ "(COALESCE(:classId,null) IS NULL OR (cg.class_id IN (:classId))) and \n"
			+ "(COALESCE(:startDate) IS NULL OR (cg.ctd_on BETWEEN :startDate AND :endDate)) and \n"
			+ "(COALESCE(:firstNameLastName,null) IS NULL OR (cg.first_last_nm IN (:firstNameLastName))) and \n"
			+ "(COALESCE(:emailId,null) IS NULL OR (cg.email_id IN (:emailId))) and \n"
			+ "(COALESCE(:contactNumber,null) IS NULL OR (cg.cont_no IN (:contactNumber))) and \n"
			+ "(COALESCE(:addressLine1,null) IS NULL OR (cg.address_line1 IN (:addressLine1))) and \n"
			+ "cg.is_deleted = 0) x1", nativeQuery = true)
	public List<IClientGeneralNew> getClientGeneralList(
			@Param(value = "clientId") List<String> clientId,
			@Param(value = "intakeFormNumber") List<String> intakeFormNumber,
			@Param(value = "statusId") List<Long> statusId,
			@Param(value = "classId") List<Long> classId,
			@Param(value = "firstNameLastName") String firstNameLastName,
			@Param(value = "emailId") String emailId,
			@Param(value = "contactNumber") String contactNumber,
			@Param(value = "addressLine1") String addressLine1,
			@Param(value = "startDate") Date startDate,
			@Param(value = "endDate") Date endDate);
	
}