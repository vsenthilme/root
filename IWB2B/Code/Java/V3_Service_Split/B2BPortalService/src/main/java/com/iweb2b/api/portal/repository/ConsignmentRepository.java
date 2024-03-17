package com.iweb2b.api.portal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.consignment.dto.ConsignmentImpl;
import com.iweb2b.api.portal.model.consignment.entity.ConsignmentEntity;
import com.iweb2b.api.portal.model.consignment.entity.IConsignmentEntity;
import com.iweb2b.api.portal.repository.fragments.StreamableJpaSpecificationRepository;

@Repository
@Transactional
public interface ConsignmentRepository extends JpaRepository<ConsignmentEntity, Long>,
		StreamableJpaSpecificationRepository<ConsignmentEntity> {

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
	
	@Query(value = "SELECT distinct c.REFERENCE_NUMBER AS referenceNumber, c.CUSTOMER_REFERENCE_NUMBER AS customerReferenceNumber, \r\n"
			+ "c.STATUS_DESCRIPTION AS statusDescription, c.AWB_3RD_PARTY AS awb3rdParty, c.CREATED_AT AS createdAt, \r\n"
			+ "c.IS_AWB_PRINTED AS isAwbPrinted, j.scan_type AS scanType, c.ORDER_TYPE AS orderType, c.CUSTOMER_CODE AS customerCode, \r\n"
			+ "j.SCAN_TIME AS eventTime "
			+ "from tblconsignment c \r\n"
			+ "left outer join tbljntwebhook j on j.BILL_CODE = c.AWB_3RD_PARTY\r\n"
			+ "WHERE c.REFERENCE_NUMBER IN (select tc.REFERENCE_NUMBER from tblconsignment tc join tblconsignmentwebhook tcw on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER WHERE tcw.hub_code = :hubCode GROUP BY tc.REFERENCE_NUMBER) \r\n"
			+ "ORDER BY c.CREATED_AT", nativeQuery = true)
	public List<IConsignmentEntity> findConsigmentByReferenceNumber (@Param("hubCode") String hubCode);
	
	@Query(value = "SELECT c.REFERENCE_NUMBER AS referenceNumber, \r\n"
			+ "c.CUSTOMER_REFERENCE_NUMBER AS customerReferenceNumber, \r\n"
			+ "c.STATUS_DESCRIPTION AS statusDescription, \r\n"
			+ "c.CREATED_AT AS createdAt, \r\n"
			+ "c.IS_AWB_PRINTED AS isAwbPrinted, \r\n"
			+ "j.ACTION_TIME AS actionTime, \r\n"
			+ "c.ORDER_TYPE AS orderType, \r\n"
			+ "c.CUSTOMER_CODE AS customerCode, \r\n"
			+ "c.QP_WH_STATUS AS qpWebhookStatus,\r\n"
			+ "j.item_action_name AS actionName\r\n"
			+ "from tblconsignment c \r\n"
			+ "left outer join tblqpwebhook j on j.TRACKING_NO = c.REFERENCE_NUMBER\r\n"
			+ "WHERE c.REFERENCE_NUMBER IN \r\n"
			+ "(select tc.REFERENCE_NUMBER from tblconsignment tc join tblconsignmentwebhook tcw \r\n"
			+ "on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER WHERE tcw.hub_code = 'QATARPOST' GROUP BY tc.REFERENCE_NUMBER) \r\n"
			+ "ORDER BY c.REFERENCE_NUMBER, j.ACTION_TIME", nativeQuery = true)
	public List<IConsignmentEntity> findQPConsigmentByReferenceNumber (@Param("hubCode") String hubCode);

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

	@Query(value = "select\n" +
			"tc.consignment_id consignmentId,\n" +
			"tc.reference_number,\n" +
			"tc.cod_amount,\n" +
			"tc.cod_collection_mode,\n" +
			"tc.customer_code,\n" +
			"tc.service_type_id,\n" +
			"tc.consignment_type,\n" +
			"tc.load_type,\n" +
			"tc.description,\n" +
			"tc.cod_favor_of,\n" +
			"tc.dimension_unit,\n" +
			"tc.length,\n" +
			"tc.width,\n" +
			"tc.height,\n" +
			"tc.weight_unit,\n" +
			"tc.weight,\n" +
			"tc.declared_value,\n" +
			"tc.num_pieces,\n" +
			"tc.notes,\n" +
			"tc.customer_reference_number,\n" +
			"tc.is_risk_surcharge_applicable,\n" +
			"tc.created_at,\n" +
			"tc.status_description,\n" +
			"tc.customer_civil_id,\n" +
			"tc.receiver_civil_id,\n" +
			"tc.currency,\n" +
			"tc.awb_3rd_Party,\n" +
			"tc.scanType_3rd_Party,\n" +
			"tc.hubCode_3rd_Party,\n" +
			"tc.order_type orderType,\n" +
			"tc.JNT_PUSH_STATUS jntPushStatus,\n" +
			"tc.BOUTIQAAT_PUSH_STATUS boutiqaatPushStatus,\n" +
			"tc.IS_AWB_PRINTED,\n" +
			"tc.inco_terms incoTerms,\n" +
			"j.scan_type scanType,\n" +
			"tcw.hub_code hubCode\n" +
			"from\n" +
			"tblconsignment tc\n" +
			"join tblconsignmentwebhook tcw on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER\n" +
			"left outer join tbljntwebhook j on j.BILL_CODE = tc.AWB_3RD_PARTY\n" +
			"where \n" +
			"(COALESCE(:awb_3rd_Party, null) IS NULL OR (tc.awb_3rd_Party IN (:awb_3rd_Party))) and \n" +
			"(COALESCE(:boutiqaatPushStatus, null) IS NULL OR (tc.BOUTIQAAT_PUSH_STATUS IN (:boutiqaatPushStatus))) and \n" +
			"(COALESCE(:consignmentId, null) IS NULL OR (tc.consignment_id IN (:consignmentId))) and \n" +
			"(COALESCE(:consignment_type, null) IS NULL OR (tc.consignment_type IN (:consignment_type))) and \n" +
			"(COALESCE(:customer_civil_id, null) IS NULL OR (tc.customer_civil_id IN (:customer_civil_id))) and \n" +
			"(COALESCE(:customer_code, null) IS NULL OR (tc.customer_code IN (:customer_code))) and \n" +
			"(COALESCE(:customer_reference_number, null) IS NULL OR (tc.customer_reference_number IN (:customer_reference_number))) and \n" +
			"(COALESCE(:hubCode_3rd_Party, null) IS NULL OR (tc.hubCode_3rd_Party IN (:hubCode_3rd_Party))) and \n" +
			"(COALESCE(:jntPushStatus, null) IS NULL OR (tc.jnt_push_status IN (:jntPushStatus))) and \n" +
			"(COALESCE(:orderType, null) IS NULL OR (tc.order_type IN (:orderType))) and \n" +
			"(COALESCE(:receiver_civil_id, null) IS NULL OR (tc.receiver_civil_id IN (:receiver_civil_id))) and \n" +
			"(COALESCE(:reference_number, null) IS NULL OR (tc.reference_number IN (:reference_number))) and \n" +
			"(COALESCE(:scanType_3rd_Party, null) IS NULL OR (tc.scanType_3rd_Party IN (scanType_3rd_Party))) and \n" +
			"(COALESCE(:service_type_id, null) IS NULL OR (tc.service_type_id IN (:service_type_id))) and \n" +
			"(COALESCE(:scanType, null) IS NULL OR (j.scan_type IN (:scanType))) and \n" +
			"(COALESCE(:hubCode, null) IS NULL OR (tcw.hub_code IN (:hubCode))) and \n" +
			"(COALESCE(CONVERT(VARCHAR(255), :fromDate), null) IS NULL OR (tcw.created_at between COALESCE(CONVERT(VARCHAR(255), :fromDate), null) and COALESCE(CONVERT(VARCHAR(255), :toDate), null))) and \n" +
			"(COALESCE(CONVERT(VARCHAR(255), :startDate), null) IS NULL OR (tc.created_at between COALESCE(CONVERT(VARCHAR(255), :startDate), null) and COALESCE(CONVERT(VARCHAR(255), :endDate), null))) ", nativeQuery = true)
	public List<ConsignmentImpl> findConsignment(@Param(value = "awb_3rd_Party") List<String> awb_3rd_Party,
												 @Param(value = "boutiqaatPushStatus") List<String> boutiqaatPushStatus,
												 @Param(value = "consignmentId") List<Long> consignmentId,
												 @Param(value = "consignment_type") List<String> consignment_type,
												 @Param(value = "customer_civil_id") List<String> customer_civil_id,
												 @Param(value = "customer_code") List<String> customer_code,
												 @Param(value = "customer_reference_number") List<String> customer_reference_number,
												 @Param(value = "hubCode_3rd_Party") List<String> hubCode_3rd_Party,
												 @Param(value = "jntPushStatus") List<String> jntPushStatus,
												 @Param(value = "orderType") List<String> orderType,
												 @Param(value = "receiver_civil_id") List<String> receiver_civil_id,
												 @Param(value = "reference_number") List<String> reference_number,
												 @Param(value = "scanType_3rd_Party") List<String> scanType_3rd_Party,
												 @Param(value = "service_type_id") List<String> service_type_id,
												 @Param(value = "scanType") List<String> scanType,
												 @Param(value = "hubCode") List<String> hubCode,
												 @Param(value = "startDate") Date startDate,
												 @Param(value = "endDate") Date endDate,
												 @Param(value = "fromDate") Date fromDate,
												 @Param(value = "toDate") Date toDate);

	@Query(value = "select\n" +
			"tc.consignment_id consignmentId,\n" +
			"tc.reference_number,\n" +
			"tc.cod_amount,\n" +
			"tc.cod_collection_mode,\n" +
			"tc.customer_code,\n" +
			"tc.service_type_id,\n" +
			"tc.consignment_type,\n" +
			"tc.load_type,\n" +
			"tc.description,\n" +
			"tc.cod_favor_of,\n" +
			"tc.dimension_unit,\n" +
			"tc.length,\n" +
			"tc.width,\n" +
			"tc.height,\n" +
			"tc.weight_unit,\n" +
			"tc.weight,\n" +
			"tc.declared_value,\n" +
			"tc.num_pieces,\n" +
			"tc.notes,\n" +
			"tc.customer_reference_number,\n" +
			"tc.is_risk_surcharge_applicable,\n" +
			"tc.created_at,\n" +
			"tc.status_description,\n" +
			"tc.customer_civil_id,\n" +
			"tc.receiver_civil_id,\n" +
			"tc.currency,\n" +
			"tc.awb_3rd_Party,\n" +
			"tc.scanType_3rd_Party,\n" +
			"tc.hubCode_3rd_Party,\n" +
			"tc.order_type orderType,\n" +
			"tc.JNT_PUSH_STATUS jntPushStatus,\n" +
			"tc.BOUTIQAAT_PUSH_STATUS boutiqaatPushStatus,\n" +
			"tc.IS_AWB_PRINTED,\n" +
			"tc.inco_terms incoTerms,\n" +
			"j.scan_type scanType,\n" +
			"tcw.hub_code hubCode\n" +
			"from\n" +
			"tblconsignment tc\n" +
			"join tblconsignmentwebhook tcw on tcw.REFERENCE_NUMBER = tc.REFERENCE_NUMBER\n" +
			"left outer join tbljntwebhook j on j.BILL_CODE = tc.AWB_3RD_PARTY\n" +
			"where \n" +
			"(COALESCE(:awb_3rd_Party, null) IS NULL OR (tc.awb_3rd_Party IN (:awb_3rd_Party))) and \n" +
			"(COALESCE(:boutiqaatPushStatus, null) IS NULL OR (tc.BOUTIQAAT_PUSH_STATUS IN (:boutiqaatPushStatus))) and \n" +
			"(COALESCE(:consignmentId, null) IS NULL OR (tc.consignment_id IN (:consignmentId))) and \n" +
			"(COALESCE(:consignment_type, null) IS NULL OR (tc.consignment_type IN (:consignment_type))) and \n" +
			"(COALESCE(:customer_civil_id, null) IS NULL OR (tc.customer_civil_id IN (:customer_civil_id))) and \n" +
			"(COALESCE(:customer_code, null) IS NULL OR (tc.customer_code IN (:customer_code))) and \n" +
			"(COALESCE(:customer_reference_number, null) IS NULL OR (tc.customer_reference_number IN (:customer_reference_number))) and \n" +
			"(COALESCE(:hubCode_3rd_Party, null) IS NULL OR (tc.hubCode_3rd_Party IN (:hubCode_3rd_Party))) and \n" +
			"(COALESCE(:jntPushStatus, null) IS NULL OR (tc.jnt_push_status IN (:jntPushStatus))) and \n" +
			"(COALESCE(:orderType, null) IS NULL OR (tc.order_type IN (:orderType))) and \n" +
			"(COALESCE(:receiver_civil_id, null) IS NULL OR (tc.receiver_civil_id IN (:receiver_civil_id))) and \n" +
			"(COALESCE(:reference_number, null) IS NULL OR (tc.reference_number IN (:reference_number))) and \n" +
			"(COALESCE(:scanType_3rd_Party, null) IS NULL OR (tc.scanType_3rd_Party IN (scanType_3rd_Party))) and \n" +
			"(COALESCE(:service_type_id, null) IS NULL OR (tc.service_type_id IN (:service_type_id))) and \n" +
//            "(COALESCE(:scanType, null) IS NULL OR (j.scan_type IN (:scanType))) and \n" +
			"j.scan_type IS NULL and \n" +
			"(COALESCE(:hubCode, null) IS NULL OR (tcw.hub_code IN (:hubCode))) and \n" +
			"(COALESCE(CONVERT(VARCHAR(255), :fromDate), null) IS NULL OR (tcw.created_at between COALESCE(CONVERT(VARCHAR(255), :fromDate), null) and COALESCE(CONVERT(VARCHAR(255), :toDate), null))) and \n" +
			"(COALESCE(CONVERT(VARCHAR(255), :startDate), null) IS NULL OR (tc.created_at between COALESCE(CONVERT(VARCHAR(255), :startDate), null) and COALESCE(CONVERT(VARCHAR(255), :endDate), null))) ", nativeQuery = true)
	public List<ConsignmentImpl> findConsignmentWithScanTypeNull(@Param(value = "awb_3rd_Party") List<String> awb_3rd_Party,
																 @Param(value = "boutiqaatPushStatus") List<String> boutiqaatPushStatus,
																 @Param(value = "consignmentId") List<Long> consignmentId,
																 @Param(value = "consignment_type") List<String> consignment_type,
																 @Param(value = "customer_civil_id") List<String> customer_civil_id,
																 @Param(value = "customer_code") List<String> customer_code,
																 @Param(value = "customer_reference_number") List<String> customer_reference_number,
																 @Param(value = "hubCode_3rd_Party") List<String> hubCode_3rd_Party,
																 @Param(value = "jntPushStatus") List<String> jntPushStatus,
																 @Param(value = "orderType") List<String> orderType,
																 @Param(value = "receiver_civil_id") List<String> receiver_civil_id,
																 @Param(value = "reference_number") List<String> reference_number,
																 @Param(value = "scanType_3rd_Party") List<String> scanType_3rd_Party,
																 @Param(value = "service_type_id") List<String> service_type_id,
//                                                                 @Param(value = "scanType") List<String> scanType,
																 @Param(value = "hubCode") List<String> hubCode,
																 @Param(value = "startDate") Date startDate,
																 @Param(value = "endDate") Date endDate,
																 @Param(value = "fromDate") Date fromDate,
																 @Param(value = "toDate") Date toDate);


}

