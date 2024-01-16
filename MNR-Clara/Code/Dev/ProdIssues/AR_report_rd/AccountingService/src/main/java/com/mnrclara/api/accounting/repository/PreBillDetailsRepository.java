package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.prebill.PreBillDetails;

@Repository
@Transactional
public interface PreBillDetailsRepository extends PagingAndSortingRepository<PreBillDetails,Long>, 
													JpaSpecificationExecutor<PreBillDetails> {
	
	public List<PreBillDetails> findAll();
	public List<PreBillDetails> findByMatterNumber(String matterNumber);
	public List<PreBillDetails> findByPreBillNumberAndStatusIdIn(String preBillNumber, List<Long> statusId);
	public PreBillDetails findByPreBillNumber(String preBillNumber);
	public PreBillDetails findByPreBillNumberAndDeletionIndicator(String preBillNumber, Long deletionIndicator);
	public PreBillDetails findByMatterNumberAndPreBillNumberAndDeletionIndicator(String matterNumber, String preBillNumber, Long deletionIndicator);
	
	/**
	 * 
	 * @param preBillBatchNumber
	 * @param preBillNumber
	 * @param preBillDate
	 * @param matterNumber
	 * @param deletionIndicator
	 * @return
	 */
	public Optional<PreBillDetails> 
		findByPreBillBatchNumberAndPreBillNumberAndPreBillDateAndMatterNumberAndDeletionIndicator(
				String preBillBatchNumber, String preBillNumber, Date preBillDate, String matterNumber, Long deletionIndicator);
	
	/**
	 * 
	 * @param preBillBatchNumber
	 * @param preBillNumber
	 * @param matterNumber
	 * @param deletionIndicator
	 * @return
	 */
	public List<PreBillDetails> 
	findByPreBillBatchNumberAndPreBillNumberAndMatterNumberAndDeletionIndicator (
			String preBillBatchNumber, String preBillNumber, String matterNumber, Long deletionIndicator);
	
	/**
	 * 
	 * @param preBillNumber
	 * @param matterNumber
	 * @param deletionIndicator
	 * @return
	 */
	public List<PreBillDetails> findByPreBillNumberAndMatterNumberAndDeletionIndicator (
			String preBillNumber, String matterNumber, Long deletionIndicator);

	public Page<PreBillDetails> findByDeletionIndicator (Long deletionIndicator, Pageable page);
	
	public List<PreBillDetails> findByMatterNumberAndStatusIdAndStartDateForPreBillBetweenAndFeesCostCutoffDateBetweenAndDeletionIndicator (
			String matterNumber, Long statusId, Date start1, Date start2, Date feesCut1, Date feesCut2, Long deletionIndicator);

	@Modifying
	@Query (value = "UPDATE MNRCLARA.tblprebilldetails set STATUS_ID = 59, IS_DELETED = 1 \r\n"
			+ " WHERE PRE_BILL_NO = :preBillNumber and MATTER_NO = :matterNumber", nativeQuery = true)
	public void deletePreBillDetails(@Param(value = "preBillNumber") String preBillNumber, @Param(value = "matterNumber") String matterNumber);
	
	//------------------------AR REPORT--------------------------------------------------------
	@Query(value = "SELECT DISTINCT MATTER_NO FROM tblmattergenaccid \r\n"
			+ "	WHERE MATTER_NO IN :matterNumbers AND IS_DELETED = 0 AND CLASS_ID IN :classID AND STATUS_ID <> 30", nativeQuery = true)
	public List<String> getMatterNumbersByNotStatusID30 (@Param(value = "matterNumbers") List<String> matterNumbers, 
			@Param(value = "classID") List<Long> classID);
	
	@Query(value = "SELECT DISTINCT MATTER_NO FROM tblmattergenaccid \r\n"
			+ "	WHERE MATTER_NO :matterNumbers AND CLASS_ID IN :classID \r\n"
			+ "	AND IS_DELETED = 0" , nativeQuery = true)
	public List<String> getMatterNumbersByWithoutStatusID (@Param(value = "matterNumbers") List<String> matterNumbers, 
			@Param(value = "classID") List<Long> classID);
}