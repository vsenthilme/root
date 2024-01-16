package com.mnrclara.api.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.dto.PreBillDetails;

@Repository
@Transactional
public interface PreBillDetailsRepository extends PagingAndSortingRepository<PreBillDetails,Long>, 
													JpaSpecificationExecutor<PreBillDetails> {
	
//	@Query(value="SELECT SUM(TOTAL_AMOUNT) AS totalAmount FROM tblprebilldetails\r\n"
//			+ "WHERE CTD_ON BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND \r\n"
//			+ "DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) AND PRE_BILL_NO = :preBillNumber \r\n"
//			+ "GROUP BY PRE_BILL_NO", nativeQuery=true)
//    public Double getAccountAgingDetails(@Param ("fromDiff") Long fromDiff, @Param ("toDiff") Long toDiff,
//    		@Param ("preBillNumber") String preBillNumber);
	
	@Query(value="SELECT SUM(TOTAL_AMOUNT) AS totalAmount , PRE_BILL_NO FROM tblprebilldetails\r\n"
			+ "WHERE CTD_ON BETWEEN DATE_SUB(CURDATE(), INTERVAL :toDiff DAY) AND DATE_SUB(CURDATE(), INTERVAL :fromDiff DAY) \r\n"
			+ "AND PRE_BILL_NO IN (SELECT p.pre_bill_no FROM tblprebilldetails p where p.MATTER_NO = :matterNumber)\r\n"
			+ "GROUP BY PRE_BILL_NO", nativeQuery=true)
    public List<Double> getAccountAgingDetails(@Param ("fromDiff") Long fromDiff, @Param ("toDiff") Long toDiff,
    		@Param ("matterNumber") String matterNumber);
	
	public List<PreBillDetails> findByMatterNumberAndStatusIdNot(String matterNumber, Long statusIds);

	public PreBillDetails findByPreBillNumber(String preBillNumber);
}