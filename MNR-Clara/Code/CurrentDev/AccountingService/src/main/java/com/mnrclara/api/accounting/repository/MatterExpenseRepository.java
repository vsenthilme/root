package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.prebill.MatterExpense;


@Repository
@Transactional
public interface MatterExpenseRepository extends JpaRepository<MatterExpense, Long>,JpaSpecificationExecutor<MatterExpense> {

	@Query(value="SELECT SUM(EXP_AMOUNT) AS EXPENSEAMOUNT \r\n"
			+ "	FROM tblmatterexpenseid \r\n"
			+ "	WHERE REF_FIELD_1 = :preBillNumber "
			+ "	GROUP BY REF_FIELD_1", nativeQuery=true)
    public Double getExpenseAmount(@Param ("preBillNumber") String preBillNumber);
	
	public List<MatterExpense> findByReferenceField1(String referenceField1);
	
	public List<MatterExpense> findByMatterNumberAndReferenceField1 (String matterNumber, String preBillNumber);
	
	@Query(value="SELECT SUM(EXP_AMOUNT) AS EXPENSEAMOUNT \r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE REF_FIELD_1 = :preBillNumber \r\n"
			+ "GROUP BY REF_FIELD_1", nativeQuery=true)
    public Double getSumOfExpenseAmount(@Param ("preBillNumber") String preBillNumber);

	public List<MatterExpense> findByMatterNumber(String matterNumber);
	
	@Query(value = "SELECT SUM(EXP_AMOUNT) AS expenseAmount\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND UPPER(REF_FIELD_5) = UPPER('HARD COST') \r\n"
			+ "AND REF_FIELD_2 BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) \r\n"
			+ "AND STATUS_ID IN (34, 56)", nativeQuery = true)
	public Double findHarcCostExpAmount (@Param ("fromDiff") Long fromDiff, @Param ("toDiff") Long toDiff,
			@Param ("matterNumber") String matterNumber);
	
	@Query(value = "SELECT SUM(EXP_AMOUNT) AS expenseAmount\r\n"
			+ "FROM tblmatterexpenseid \r\n"
			+ "WHERE MATTER_NO = :matterNumber AND UPPER(REF_FIELD_5) = UPPER('SOFT COST') \r\n"
			+ "AND REF_FIELD_2 BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) \r\n"
			+ "AND STATUS_ID IN (37,38)", nativeQuery = true)
	public Double findSoftCostExpAmount (@Param ("fromDiff") Long fromDiff, @Param ("toDiff") Long toDiff, 
			@Param ("matterNumber") String matterNumber);

	public List<MatterExpense> findByMatterNumberAndStatusIdAndDeletionIndicatorAndReferenceField2Between(String matterNumber, Long sTATUS_ID_37,
			Long delFlag, Date startDate, Date feesCutoffDate);
	
	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblmatterexpenseid set STATUS_ID = 59, IS_DELETED = 1 \r\n"
			+ " WHERE MATTER_EXP_ID = :matterExpenseId", nativeQuery = true)
	public void deleteMatterExpense(@Param(value = "matterExpenseId") Long matterExpenseId);

	public List<MatterExpense> findByMatterNumberAndDeletionIndicator(String matterNumber, long l);

	public List<MatterExpense> findByReferenceField2Between(Date date, Date date2);
	
}