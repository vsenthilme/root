package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
	
}