package com.iweb2b.api.integration.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.consignment.entity.ConsignmentEntity;
import com.iweb2b.api.integration.model.consignment.entity.IConsignmentEntity;

@Repository
@Transactional
public interface ConsignmentRepository extends JpaRepository<ConsignmentEntity, Long> {

	public List<ConsignmentEntity> findAll();

	@Query(value = "SELECT * \r\n" +
			"FROM tblconsignment where reference_number in (:reference_number)", nativeQuery = true)
	public ConsignmentEntity findConsigment (@Param("reference_number") String reference_number);
	
	@Query(value = "SELECT REFERENCE_NUMBER FROM tblconsignment where reference_number like 'QAP%';", nativeQuery = true)
	public List<String> findConsigmentByQP();
	
	@Query(value = "SELECT REFERENCE_NUMBER FROM tblconsignment where AWB_3RD_PARTY=:jnt_billcode", nativeQuery = true)
	public String findConsigmentByBillCode(@Param("jnt_billcode") String jnt_billcode);

	//select tc.REFERENCE_NUMBER from tblconsignment tc join tblconsignmentwebhook tcw on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER WHERE tcw.hub_code = 'JT' GROUP BY tc.REFERENCE_NUMBER;
	@Query(value ="select tc.REFERENCE_NUMBER \n"+
			"from tblconsignment tc\n" +
			"join tblconsignmentwebhook tcw on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER \n" +
			"WHERE tcw.hub_code = :hubCode GROUP BY tc.REFERENCE_NUMBER",nativeQuery = true)
	public List<String> getByHubcode(@Param(value="hubCode") String hubCode);

	@Query(value = "SELECT TOP 1 * \r\n" +
			"FROM tblconsignment WHERE REFERENCE_NUMBER IN (:reference_number) ORDER BY CREATED_AT DESC", nativeQuery = true)
	public ConsignmentEntity findConsigmentUniqueRecord (@Param("reference_number") String reference_number);
	
	/*
	 *  public String getReferenceNumber();
    public String getCustomerReferenceNumber();
    public String getStatusDescription();
    public String getAwb3rdParty();
    public Date getCreatedAt();
    public Boolean getIsAwbPrinted();
    public String getScanType();
    public String getCustomerCode();
    public String getOrderType();
	 */
	@Query(value = "SELECT distinct c.REFERENCE_NUMBER AS referenceNumber, c.CUSTOMER_REFERENCE_NUMBER AS customerReferenceNumber, \r\n"
			+ "c.STATUS_DESCRIPTION AS statusDescription, c.AWB_3RD_PARTY AS awb3rdParty, c.CREATED_AT AS createdAt, \r\n"
			+ "c.IS_AWB_PRINTED AS isAwbPrinted, j.scan_type AS scanType, c.ORDER_TYPE AS orderType, c.CUSTOMER_CODE AS customerCode \r\n"
			+ "from tblconsignment c \r\n"
			+ "left outer join tbljntwebhook j on j.BILL_CODE = c.AWB_3RD_PARTY\r\n"
			+ "WHERE c.REFERENCE_NUMBER IN (:reference_number) \r\n"
			+ "ORDER BY c.CREATED_AT", nativeQuery = true)
	public List<IConsignmentEntity> findConsigmentByReferenceNumber (@Param("reference_number") List<String> reference_number);
	
	// SELECT TOP 1 jw.scan_type scanType FROM tbljntwebhook jw WHERE JW.BILL_CODE = 'JTE000145058765' ORDER BY SCAN_TIME DESC;
	@Query(value ="SELECT TOP 1 jw.scan_type scanType \r\n"
			+ "FROM tbljntwebhook jw WHERE JW.BILL_CODE = :billCode \r\n"
			+ "ORDER BY SCAN_TIME DESC", nativeQuery = true)
	public String getScanType(@Param(value="billCode") String billCode);

	@Query(value ="SELECT count(tc.consignment_id) \n"
			+ "FROM tblconsignment tc \n"
			+ "WHERE \n"
			+ "tc.CREATED_AT BETWEEN :fromDate AND :toDate and \n"
			+ "tc.JNT_PUSH_STATUS = :pushStatus and \n"
			+ "tc.ORDER_TYPE = :orderType and tc.awb_3rd_party is not null ", nativeQuery = true)
	public Long getJNTCount(@Param(value="pushStatus") String pushStatus,
							 @Param(value="orderType") String orderType,
//							 @Param(value="customerCode") String customerCode,
							 @Param(value="fromDate") Date fromDate,
							 @Param(value="toDate") Date toDate );

@Query(value ="SELECT count(tc.consignment_id) \n"
			+ "FROM tblconsignment tc \n"
			+ "WHERE tc.CREATED_AT BETWEEN :fromDate AND :toDate \n"
			+ "and tc.BOUTIQAAT_PUSH_STATUS = :pushStatus and \n"
			+ "ORDER_TYPE = :orderType and \n"
//			+ "tc.reference_number is not null and \n"
			+ "tc.customer_code = :customerCode and tc.awb_3rd_party is not null", nativeQuery = true)
	public Long getBoutiqaatCount(@Param(value="pushStatus") String pushStatus,
								 @Param(value="orderType") String orderType,
								  @Param(value="customerCode") String customerCode,
								 @Param(value="fromDate") Date fromDate,
								 @Param(value="toDate") Date toDate );

@Query(value ="SELECT count(tc.consignment_id) \n"
			+ "FROM tblconsignment tc \n"
			+ "WHERE tc.CREATED_AT BETWEEN :fromDate AND :toDate \n"
			+ "and tc.BOUTIQAAT_PUSH_STATUS = :pushStatus and \n"
			+ "ORDER_TYPE = :orderType and \n"
//			+ "tc.reference_number is not null and \n"
			+ "tc.customer_code = :customerCode and tc.awb_3rd_party is not null", nativeQuery = true)
	public Long getWintlCount(@Param(value="pushStatus") String pushStatus,
								 @Param(value="orderType") String orderType,
								  @Param(value="customerCode") String customerCode,
								 @Param(value="fromDate") Date fromDate,
								 @Param(value="toDate") Date toDate );


}

