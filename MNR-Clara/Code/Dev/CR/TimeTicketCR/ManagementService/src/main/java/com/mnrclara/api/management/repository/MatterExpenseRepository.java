package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mnrclara.api.management.model.matterexpense.IMatterExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.dto.IMatterExpenseCountAndSum;
import com.mnrclara.api.management.model.matterexpense.MatterExpense;

@Repository
@Transactional
public interface MatterExpenseRepository extends JpaRepository<MatterExpense, Long>,JpaSpecificationExecutor<MatterExpense>, DynamicNativeQueryTT {

	public MatterExpense findByMatterExpenseIdAndDeletionIndicator(Long matterExpenseId, Long deletionIndicator);
	
	public List<MatterExpense> findByReferenceField1AndDeletionIndicator(String referenceField1, Long deletionIndicator);
	
	@Query(value = "SELECT SUM(EXP_AMOUNT) AS expenseAmount\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND UPPER(REF_FIELD_5) = UPPER('HARD COST') \r\n"
			+ "AND REF_FIELD_2 BETWEEN :fromDiff AND :toDiff \r\n"
			+ "AND STATUS_ID IN (37,38) AND IS_DELETED = 0", nativeQuery = true)
	public Double findHarcCostExpAmount (
			@Param ("fromDiff") Date fromDiff, 
			@Param ("toDiff") Date toDiff,
			@Param ("matterNumber") String matterNumberList);
	
	@Query(value = "SELECT SUM(EXP_AMOUNT) AS expenseAmount\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND UPPER(REF_FIELD_5) = UPPER('SOFT COST') \r\n"
			+ "AND REF_FIELD_2 BETWEEN :fromDiff AND :toDiff \r\n"
			+ "AND STATUS_ID IN (37,38) AND IS_DELETED = 0", nativeQuery = true)
	public Double findSoftCostExpAmount (
			@Param ("fromDiff") Date fromDiff, 
			@Param ("toDiff") Date toDiff,
			@Param ("matterNumber") String matterNumberList);
	
	@Query(value = "SELECT COUNT(MATTER_EXP_ID) AS expenseCount, \r\n"
			+ " SUM(EXP_AMOUNT) AS expenseAmount, \r\n"
			+ " MATTER_NO AS matterNumber"
			+ " FROM tblmatterexpenseid WHERE MATTER_NO IN :matterNumber  \r\n"
			+ " AND REF_FIELD_2 BETWEEN :startDate AND :feesCutoffDate "
			+ " AND STATUS_ID = 37 AND IS_DELETED = 0 GROUP BY MATTER_NO", 
			nativeQuery = true)
	List<IMatterExpenseCountAndSum> findExpenseByIndividual(@Param ("matterNumber")  List<String> matterNumber,
											@Param ("startDate") Date startDate, 
											@Param ("feesCutoffDate") Date feesCutoffDate);
	
	@Query(value = "SELECT MATTER_EXP_ID AS matterExpenseId, \r\n"
			+ "	EXP_CODE AS expenseCode, \r\n"
			+ "	LANG_ID AS languageId, \r\n"
			+ "	CLASS_ID AS classId, \r\n"
			+ "	MATTER_NO AS matterNumber, \r\n"
			+ "	CASEINFO_NO AS caseInformationNo, \r\n"
			+ "	CLIENT_ID AS  clientId, 	\r\n"
			+ "	CASE_CATEGORY_ID AS caseCategoryId, \r\n"
			+ "	CASE_SUB_CATEGORY_ID AS caseSubCategoryId, \r\n"
			+ "	COST_ITEM AS costPerItem, 	\r\n"
			+ "	NO_ITEMS AS numberofItems, 	\r\n"
			+ "	EXP_AMOUNT AS expenseAmount,	\r\n"
			+ "	RATE_UNIT AS  rateUnit, \r\n"
			+ "	EXP_TEXT AS expenseDescription,	\r\n"
			+ "	EXP_TYPE AS expenseType, \r\n"
			+ "	BILL_TYPE AS billType, \r\n"
			+ "	WRITE_OFF AS writeOff, \r\n"
			+ "	EXP_ACCOUNT_NO AS expenseAccountNumber, \r\n"
			+ "	STATUS_ID AS statusId,\r\n"
			+ "	REF_FIELD_2 AS referenceField2,\r\n"
			+ "	CTD_BY AS createdBy,\r\n"
			+ "	CTD_ON AS createdOn,\r\n"
			+ "	UTD_BY AS updatedBy,\r\n"
			+ "	UTD_ON AS updatedOn\r\n"
			+ "	FROM tblmatterexpenseid \r\n"
			+ "	WHERE MATTER_NO IN :matterNumber AND STATUS_ID = 37 \r\n"
			+ " AND REF_FIELD_2 BETWEEN :startDate AND :feesCutoffDate AND IS_DELETED = 0", 
			nativeQuery = true)
	List<com.mnrclara.api.management.model.dto.IMatterExpense> findExpenseRecordsByIndividual(
			@Param ("matterNumber")  List<String> matterNumber,
			@Param ("startDate") Date startDate, 
			@Param ("feesCutoffDate") Date feesCutoffDate);
	
	//------------------BillByGroup---------------------------------------------------------------------------------------
	@Query(value = " SELECT COUNT(MATTER_EXP_ID) AS expenseCount,EXP_TYPE as expenseType, \r\n"
			+ "	SUM(EXP_AMOUNT) AS expenseAmount, tblmatterexpenseid.MATTER_NO AS matterNumber \r\n"
			+ "	FROM tblmatterexpenseid INNER JOIN tblmatterassignmentid\r\n"
			+ "	ON tblmatterexpenseid.MATTER_NO = tblmatterassignmentid.MATTER_NO \r\n"
			+ "	WHERE tblmatterexpenseid.MATTER_NO IN :matterNumber \r\n"
			+ "	AND tblmatterexpenseid.CTD_ON BETWEEN :startDate AND :feesCutoffDate \r\n"
			+ "	AND tblmatterexpenseid.STATUS_ID = 37\r\n"
			+ "	OR ORIGINATING_TK IN :originatingTimeKeeper \r\n"
			+ "    OR RESPONSIBLE_TK IN :responsibleTimeKeeper \r\n"
			+ "    OR ASSIGNED_TK IN :assignedTimeKeeper "
			+ "    GROUP BY tblmatterexpenseid.MATTER_NO, EXP_TYPE ",
			nativeQuery = true)
	List<IMatterExpenseCountAndSum> findExpenseByGroup(@Param ("matterNumber") List<String> matterNumber,
																	@Param ("startDate") Date startDate, 
																	@Param ("feesCutoffDate") Date feesCutoffDate,
																	@Param ("originatingTimeKeeper") List<String> originatingTimeKeeper,
																	@Param ("responsibleTimeKeeper") List<String> responsibleTimeKeeper,
																	@Param ("assignedTimeKeeper") List<String> assignedTimeKeeper);
	
	@Query(value = "SELECT MATTER_EXP_ID AS matterExpenseId, \r\n"
			+ "	EXP_CODE AS expenseCode, \r\n"
			+ "	LANG_ID AS languageId, \r\n"
			+ "	CLASS_ID AS classId, \r\n"
			+ "	MATTER_NO AS matterNumber, \r\n"
			+ "	CASEINFO_NO AS caseInformationNo, \r\n"
			+ "	CLIENT_ID AS  clientId, 	\r\n"
			+ "	CASE_CATEGORY_ID AS caseCategoryId, \r\n"
			+ "	CASE_SUB_CATEGORY_ID AS caseSubCategoryId, \r\n"
			+ "	COST_ITEM AS costPerItem, 	\r\n"
			+ "	NO_ITEMS AS numberOfItems, 	\r\n"
			+ "	EXP_AMOUNT AS expenseAmount,	\r\n"
			+ "	RATE_UNIT AS  rateUnit, \r\n"
			+ "	EXP_TEXT AS expenseDescription,	\r\n"
			+ "	EXP_TYPE AS expenseType, \r\n"
			+ "	BILL_TYPE AS billType, \r\n"
			+ "	WRITE_OFF AS writeOff, \r\n"
			+ "	EXP_ACCOUNT_NO AS expenseAccountNumber, \r\n"
			+ "	STATUS_ID AS statusId,\r\n"
			+ "	REF_FIELD_2 AS referenceField2,\r\n"
			+ "	CTD_BY AS createdBy,\r\n"
			+ "	CTD_ON AS createdOn,\r\n"
			+ "	UTD_BY AS updatedBy,\r\n"
			+ "	UTD_ON AS updatedOn\r\n"
			+ "	FROM tblmatterexpenseid \r\n"
			+ "	WHERE MATTER_NO IN :matterNumber AND STATUS_ID = 37 \r\n"
			+ " AND REF_FIELD_2 BETWEEN :startDate AND :feesCutoffDate ", 
			nativeQuery = true)
	List<com.mnrclara.api.management.model.dto.IMatterExpense> findExpenseRecordsByGroup(
			@Param ("matterNumber")  List<String> matterNumber,
			@Param ("startDate") Date startDate, 
			@Param ("feesCutoffDate") Date feesCutoffDate);

	@Query(value = "SELECT SUM(EXP_AMOUNT) AS expenseAmount\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE REF_FIELD_1 = :preBillNumber \r\n"
			+ "AND is_deleted = 0 ", nativeQuery = true)
	public Double findExpAmountByPreBillNumber (@Param ("preBillNumber") String preBillNumber);

	//----------------------------------Find All Expense------------------------------------------------------

	@Query(value = "select \n"
			+ "	m.MATTER_EXP_ID matterExpenseId, \n"
			+ "	m.LANG_ID languageId, \n"
			+ "	m.CLASS_ID classId, \n"
			+ "	m.MATTER_NO matterNumber, \n"
			+ "	m.CASEINFO_NO caseInformationNo, \n"
			+ "	m.CLIENT_ID clientId, \n"
			+ "	m.EXP_CODE expenseCode, \n"
			+ "	m.CASE_CATEGORY_ID caseCategoryId, \n"
			+ "	m.CASE_SUB_CATEGORY_ID caseSubCategoryId, \n"
			+ "	m.COST_ITEM costPerItem, \n"
			+ "	m.NO_ITEMS numberofItems, \n"
			+ "	m.EXP_AMOUNT expenseAmount, \n"
			+ "	m.RATE_UNIT rateUnit, \n"
			+ "	m.EXP_TEXT expenseDescription, \n"
			+ "	m.EXP_TYPE expenseType, \n"
			+ "	m.BILL_TYPE billType, \n"
			+ "	m.WRITE_OFF writeOff, \n"
			+ "	m.EXP_ACCOUNT_NO expenseAccountNumber, \n"
			+ "	m.STATUS_ID statusId, \n"
			+ "	m.IS_DELETED deletionIndicator, \n"
			+ "	m.REF_FIELD_1 referenceField1, \n"
			+ "	date_format(m.REF_FIELD_2,'%m-%d-%Y') sReferenceField2, \n"
			+ "	m.REF_FIELD_2 referenceField2, \n"
			+ "	m.REF_FIELD_3 referenceField3, \n"
			+ "	m.REF_FIELD_4 referenceField4, \n"
			+ "	m.REF_FIELD_5 referenceField5, \n"
			+ "	mg.matter_text referenceField6, \n"
			+ "	m.REF_FIELD_7 referenceField7, \n"
			+ "	m.REF_FIELD_8 referenceField8, \n"
			+ "	m.REF_FIELD_9 referenceField9, \n"
			+ "	m.REF_FIELD_10 referenceField10, \n"
			+ "	m.CTD_BY createdBy, \n"
			+ "	date_format(m.CTD_ON,'%m-%d-%Y') sCreatedOn, \n"
			+ "	m.CTD_ON createdOn, \n"
			+ "	m.UTD_BY updatedBy, \n"
			+ "	date_format(m.UTD_ON,'%m-%d-%Y') sUpdatedOn, \n"
			+ "	m.UTD_ON updatedOn, \n"
			+ "	m.billable_to_client billableToClient, \n"
			+ "	m.card_not_accepted cardNotAccepted, \n"
			+ "	m.payable_to payableTo, \n"
			+ "	m.payment_mode paymentMode, \n"
			+ "	date_format(m.required_date,'%m-%d-%Y') sRequiredDate, \n"
			+ "	m.required_date requiredDate, \n"
			+ "	m.vendor_fax vendorFax, \n"
			+ "	m.vendor_mailing_address vendorMailingAddress, \n"
			+ "	m.vendor_notes vendorNotes, \n"
			+ "	m.vendor_phone vendorPhone, \n"
			+ "	m.user_name userName, \n"
			+ "	m.qb_dept qbDepartment, \n"
			+ "	m.check_request_status checkRequestStatus, \n"
			+ "	m.document_name documentName, \n"
			+ "	m.physical_card physicalCard, \n"
			+ "	m.credit_card creditCard, \n"
			+ "	date_format(m.expiration_date,'%m-%d-%Y') sExpirationDate, \n"
			+ "	m.expiration_date expirationDate, \n"
			+ "	m.name_on_card_check nameOnCardOrCheck, \n"
			+ "	m.security_code securityCode, \n"
			+ "	m.type_of_credit_card typeOfCreditCard, \n"
			+ "	m.check_request_ctd_by checkRequestCreatedBy, \n"
			+ "	date_format(m.check_request_ctd_on,'%m-%d-%Y') SCheckRequestCreatedOn,\n"
			+ "	m.check_request_ctd_on checkRequestCreatedOn\n"
			+ "	from tblmatterexpenseid m \n"
			+ "	left join tblmattergenaccid mg on mg.matter_no = m.matter_no \n"
			+ "	where \n"
			+ "(COALESCE(:matterNumber,null) IS NULL OR (m.MATTER_NO IN (:matterNumber))) and \n"
			+ "(COALESCE(:statusId) IS NULL OR (m.STATUS_ID IN (:statusId))) and \n"
			+ "(COALESCE(:createdBy) IS NULL OR (m.CTD_BY IN (:createdBy))) and \n"
			+ "(COALESCE(:expenseCode,null) IS NULL OR (m.EXP_CODE IN (:expenseCode))) and \n"
			+ "(COALESCE(:expenseType,null) IS NULL OR (m.EXP_TYPE IN (:expenseType))) and \n"
			+ "(COALESCE(:startCreatedOn) IS NULL OR (m.REF_FIELD_2 BETWEEN :startCreatedOn AND :endCreatedOn)) and \n"
			+ "m.is_deleted=0 ", nativeQuery=true)
	public List<IMatterExpense> getExpenseList(
			@Param(value = "matterNumber") String matterNumber,
			@Param(value = "statusId") List<Long> statusId,
			@Param(value = "createdBy") List<String> createdBy,
			@Param(value = "expenseCode") List<String> expenseCode,
			@Param(value = "expenseType") List<String> expenseType,
			@Param(value = "startCreatedOn") Date startCreatedOn,
			@Param(value = "endCreatedOn") Date endCreatedOn);
	
}