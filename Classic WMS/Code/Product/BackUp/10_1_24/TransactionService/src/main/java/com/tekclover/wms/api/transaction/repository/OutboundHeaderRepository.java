package com.tekclover.wms.api.transaction.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.outbound.OutboundHeader;
import com.tekclover.wms.api.transaction.model.outbound.OutboundHeaderStream;

@Repository
@Transactional
public interface OutboundHeaderRepository extends JpaRepository<OutboundHeader, Long>, JpaSpecificationExecutor<OutboundHeader> {
    String UPGRADE_SKIPLOCKED = "-2";

    public List<OutboundHeader> findAll();

    public Optional<OutboundHeader>
    findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, Long deletionIndicator);

    public Optional<OutboundHeader> findByPreOutboundNo(String preOutboundNo);

    public OutboundHeader findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
            String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode, Long deletionIndicator);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE) // adds 'FOR UPDATE' statement
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = UPGRADE_SKIPLOCKED)})
    public OutboundHeader findByWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndPreOutboundNoAndRefDocNumberAndReferenceField2AndDeletionIndicator(
            String warehouseId, String companyCodeId, String plantId, String languageId, String preOutboundNo, String refDocNumber, String referenceField2, Long deletionIndicator);

    public OutboundHeader findByRefDocNumberAndDeletionIndicator(String refDocNumber, long l);

    /*
     * Reports
     */
    public List<OutboundHeader> findByWarehouseIdAndPartnerCodeAndReferenceField1AndStatusIdAndDeliveryConfirmedOnBetween(
            String warehouseId, String partnerCode, String refField1, Long statusId, Date startDate, Date endDate);

    public List<OutboundHeader> findByWarehouseIdAndStatusIdAndDeliveryConfirmedOnBetween(String warehouseId, Long statusId, Date startDate, Date endDate);

    public List<OutboundHeader> findByStatusIdAndPartnerCodeAndDeliveryConfirmedOnBetween(Long statusId,
                                                                                          String partnerCode, Date startDate, Date endDate);

    @Query(value = "select \n" +
            "oh.c_id , oh.lang_id, oh.partner_code, oh.plant_id, oh.pre_ob_no,oh.ref_doc_no ,oh.wh_id,oh.dlv_ctd_by,oh.dlv_ctd_on,oh.is_deleted,oh.dlv_cnf_by,oh.dlv_cnf_on,\n" +
            "oh.dlv_ord_no, oh.ob_ord_typ_id,oh.ref_doc_date,oh.ref_doc_typ,oh.remark,oh.req_del_date,oh.dlv_rev_by,oh.dlv_rev_on,oh.status_id,oh.dlv_utd_by,oh.dlv_utd_on,\n" +
            "oh.ref_field_1,oh.ref_field_2,oh.ref_field_3,oh.ref_field_4,oh.ref_field_5,oh.ref_field_6,\n" +
            "(CASE WHEN sum(dlv_qty) is not null THEN sum(dlv_qty) ELSE 0 END) as ref_field_7,\n" +
            "COUNT(CASE WHEN dlv_qty is not null and dlv_qty > 0 THEN  dlv_qty ELSE  NULL END) as ref_field_8,\n" +
            "SUM(ORD_QTY) as ref_field_9,\n" +
            "count(ord_qty) as ref_field_10 \n" +
            "from tbloutboundheader oh\n" +
            "join tbloutboundline ol on ol.ref_doc_no = oh.ref_doc_no\n" +
            "where ol.ref_field_2 is null and \n" +
            "(COALESCE(:warehouseId, null) IS NULL OR (oh.wh_id IN (:warehouseId))) and \n" +
            "(COALESCE(:refDocNo, null) IS NULL OR (oh.ref_doc_no IN (:refDocNo))) and \n" +
            "(COALESCE(:partnerCode, null) IS NULL OR (oh.partner_code IN (:partnerCode))) and \n" +
            "(COALESCE(:outboundOrderTypeId, null) IS NULL OR (oh.ob_ord_typ_id IN (:outboundOrderTypeId))) and \n" +
            "(COALESCE(:statusId, null) IS NULL OR (oh.status_id IN (:statusId))) and \n" +
            "(COALESCE(:soType, null) IS NULL OR (oh.ref_field_1 IN (:soType))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) IS NULL OR (oh.REQ_DEL_DATE between COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) and COALESCE(CONVERT(VARCHAR(255), :endRequiredDeliveryDate), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) IS NULL OR (oh.DLV_CNF_ON between COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) and COALESCE(CONVERT(VARCHAR(255), :endDeliveryConfirmedOn), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) IS NULL OR (oh.DLV_CTD_ON between COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) and COALESCE(CONVERT(VARCHAR(255), :endOrderDate), null)))\n" +
            "group by oh.c_id , oh.lang_id, oh.partner_code, oh.plant_id, oh.pre_ob_no,oh.ref_doc_no ,oh.wh_id,oh.dlv_ctd_by,oh.dlv_ctd_on,oh.is_deleted,oh.dlv_cnf_by,oh.dlv_cnf_on,\n" +
            "oh.dlv_ord_no, oh.ob_ord_typ_id,oh.ref_doc_date,oh.ref_doc_typ,oh.remark,oh.req_del_date,oh.dlv_rev_by,oh.dlv_rev_on,oh.status_id,oh.dlv_utd_by,oh.dlv_utd_on,\n" +
            "oh.ref_field_1,oh.ref_field_2,oh.ref_field_3,oh.ref_field_4,oh.ref_field_5,oh.ref_field_6,\n" +
            "ol.ref_doc_no , ol.c_id , ol.lang_id, ol.plant_id, ol.wh_id, ol.pre_ob_no, ol.partner_code", nativeQuery = true)
    public List<OutboundHeader> findAllOutBoundHeaderData(
            @Param(value = "warehouseId") List<String> warehouseId,
            @Param(value = "refDocNo") List<String> refDocNo,
            @Param(value = "partnerCode") List<String> partnerCode,
            @Param(value = "outboundOrderTypeId") List<Long> outboundOrderTypeId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "soType") List<String> soType,
            @Param(value = "startRequiredDeliveryDate") Date startRequiredDeliveryDate,
            @Param(value = "endRequiredDeliveryDate") Date endRequiredDeliveryDate,
            @Param(value = "startDeliveryConfirmedOn") Date startDeliveryConfirmedOn,
            @Param(value = "endDeliveryConfirmedOn") Date endDeliveryConfirmedOn,
            @Param(value = "startOrderDate") Date startOrderDate,
            @Param(value = "endOrderDate") Date endOrderDate);

    @Query(value = "select \n" +
            "oh.c_id , oh.lang_id, oh.partner_code, oh.plant_id, oh.pre_ob_no,oh.ref_doc_no ,oh.wh_id,oh.dlv_ctd_by,oh.dlv_ctd_on,oh.is_deleted,oh.dlv_cnf_by,oh.dlv_cnf_on,\n" +
            "oh.dlv_ord_no, oh.ob_ord_typ_id,oh.ref_doc_date,oh.ref_doc_typ,oh.remark,oh.req_del_date,oh.dlv_rev_by,oh.dlv_rev_on,oh.status_id,oh.dlv_utd_by,oh.dlv_utd_on,\n" +
            "oh.ref_field_1,oh.ref_field_2,oh.ref_field_3,oh.ref_field_4,oh.ref_field_5,oh.ref_field_6,\n" +
            "(CASE WHEN sum(dlv_qty) is not null THEN sum(dlv_qty) ELSE 0 END) as ref_field_7,\n" +
            "COUNT(distinct itm_code) as ref_field_8,\n" +
            "SUM(ORD_QTY) as ref_field_9,\n" +
            "count(ord_qty) as ref_field_10 \n" +
            "from tbloutboundheader oh\n" +
            "left join tbloutboundlinedup ol on ol.ref_doc_no = oh.ref_doc_no\n" +
            "where ol.ref_field_2 is null and \n" +
            "(COALESCE(:warehouseId, null) IS NULL OR (oh.wh_id IN (:warehouseId))) and \n" +
            "(COALESCE(:refDocNo, null) IS NULL OR (oh.ref_doc_no IN (:refDocNo))) and \n" +
            "(COALESCE(:partnerCode, null) IS NULL OR (oh.partner_code IN (:partnerCode))) and \n" +
            "(COALESCE(:outboundOrderTypeId, null) IS NULL OR (oh.ob_ord_typ_id IN (:outboundOrderTypeId))) and \n" +
            "(COALESCE(:statusId, null) IS NULL OR (oh.status_id IN (:statusId))) and \n" +
            "(COALESCE(:soType, null) IS NULL OR (oh.ref_field_1 IN (:soType))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) IS NULL OR (oh.REQ_DEL_DATE between COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) and COALESCE(CONVERT(VARCHAR(255), :endRequiredDeliveryDate), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) IS NULL OR (oh.DLV_CNF_ON between COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) and COALESCE(CONVERT(VARCHAR(255), :endDeliveryConfirmedOn), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) IS NULL OR (oh.DLV_CTD_ON between COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) and COALESCE(CONVERT(VARCHAR(255), :endOrderDate), null)))\n" +
            "group by oh.c_id , oh.lang_id, oh.partner_code, oh.plant_id, oh.pre_ob_no,oh.ref_doc_no ,oh.wh_id,oh.dlv_ctd_by,oh.dlv_ctd_on,oh.is_deleted,oh.dlv_cnf_by,oh.dlv_cnf_on,\n" +
            "oh.dlv_ord_no, oh.ob_ord_typ_id,oh.ref_doc_date,oh.ref_doc_typ,oh.remark,oh.req_del_date,oh.dlv_rev_by,oh.dlv_rev_on,oh.status_id,oh.dlv_utd_by,oh.dlv_utd_on,\n" +
            "oh.ref_field_1,oh.ref_field_2,oh.ref_field_3,oh.ref_field_4,oh.ref_field_5,oh.ref_field_6,\n" +
            "ol.ref_doc_no , ol.c_id , ol.lang_id, ol.plant_id, ol.wh_id, ol.pre_ob_no, ol.partner_code", nativeQuery = true)
    public List<OutboundHeader> findAllOutBoundHeaderDataForRFD(
            @Param(value = "warehouseId") List<String> warehouseId,
            @Param(value = "refDocNo") List<String> refDocNo,
            @Param(value = "partnerCode") List<String> partnerCode,
            @Param(value = "outboundOrderTypeId") List<Long> outboundOrderTypeId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "soType") List<String> soType,
            @Param(value = "startRequiredDeliveryDate") Date startRequiredDeliveryDate,
            @Param(value = "endRequiredDeliveryDate") Date endRequiredDeliveryDate,
            @Param(value = "startDeliveryConfirmedOn") Date startDeliveryConfirmedOn,
            @Param(value = "endDeliveryConfirmedOn") Date endDeliveryConfirmedOn,
            @Param(value = "startOrderDate") Date startOrderDate,
            @Param(value = "endOrderDate") Date endOrderDate);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE) // adds 'FOR UPDATE' statement
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = UPGRADE_SKIPLOCKED)})
    public OutboundHeader findByRefDocNumberAndWarehouseIdAndDeletionIndicator(String refDocNumber, String warehouseId, long l);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("Update OutboundHeader ob SET ob.statusId = :statusId, ob.deliveryConfirmedOn = :deliveryConfirmedOn \r\n "
            + " WHERE ob.warehouseId = :warehouseId AND ob.companyCodeId = :companyCodeId AND ob.plantId = :plantId AND ob.languageId = :languageId AND ob.refDocNumber = :refDocNumber")
    public void updateOutboundHeaderStatus(@Param("warehouseId") String warehouseId,
                                           @Param("companyCodeId")String companyCodeId,
                                           @Param("plantId")String plantId,
                                           @Param("languageId")String languageId,
                                           @Param("refDocNumber") String refDocNumber,
                                           @Param("statusId") Long statusId,
                                           @Param("deliveryConfirmedOn") Date deliveryConfirmedOn);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("Update OutboundHeader ob SET ob.statusId = :statusId \r\n "
            + " WHERE ob.companyCodeId = :companyCodeId AND ob.plantId = :plantId AND ob.languageId = :languageId AND ob.warehouseId = :warehouseId AND ob.refDocNumber = :refDocNumber")
    public void updateOutboundHeaderStatusAs47(@Param("companyCodeId") String companyCodeId,
                                               @Param("plantId") String plantId,
                                               @Param("languageId") String languageId,
                                               @Param("warehouseId") String warehouseId,
                                               @Param("refDocNumber") String refDocNumber,
                                               @Param("statusId") Long statusId);

    public List<OutboundHeader> findByWarehouseIdAndStatusIdAndPartnerCodeAndDeliveryConfirmedOnBetween(
            String warehouseId, long l, String partnerCode, Date fromDeliveryDate_d, Date toDeliveryDate_d);


    @QueryHints(@javax.persistence.QueryHint(name = "org.hibernate.fetchSize", value = "100"))
    @Query(value = "select \n" +
            "oh.c_id companyCodeId, oh.lang_id languageId, oh.partner_code partnerCode, \n" +
            "oh.plant_id plantId, oh.pre_ob_no preOutboundNo,oh.ref_doc_no refDocNumber,oh.wh_id warehouseId, \n" +
            "oh.dlv_ctd_by createdBy, \n" +
            "DATEADD(HOUR,3,oh.dlv_ctd_on) createdOn, \n" +
            "oh.is_deleted deletionIndicator, \n" +
            "oh.dlv_cnf_by deliveryConfirmedBy, \n" +
            "DATEADD(HOUR,3,oh.dlv_cnf_on) deliveryConfirmedOn, \n" +
            "oh.dlv_ord_no deliveryOrderNo, oh.ob_ord_typ_id outboundOrderTypeId, \n" +
            "DATEADD(HOUR,3,oh.ref_doc_date) refDocDate, \n" +
            "oh.ref_doc_typ referenceDocumentType,oh.remark remarks, \n" +
            "DATEADD(HOUR,3,oh.req_del_date) requiredDeliveryDate,\n" +
            "oh.dlv_rev_by reversedBy, \n" +
            "DATEADD(HOUR,3,oh.dlv_rev_on) reversedOn, \n" +
            "oh.status_id statusId,oh.dlv_utd_by updatedBy, \n" +
            "DATEADD(HOUR,3,oh.dlv_utd_on) updatedOn,\n" +
            "oh.ref_field_1 referenceField1,oh.ref_field_2 referenceField2,oh.ref_field_3 referenceField3, \n" +
            "oh.ref_field_4 referenceField4,oh.ref_field_5 referenceField5,oh.ref_field_6 referenceField6,\n" +
            "(CASE WHEN sum(dlv_qty) is not null THEN sum(dlv_qty) ELSE 0 END) as referenceField7,\n" +
            "COUNT(CASE WHEN dlv_qty is not null and dlv_qty > 0 THEN  dlv_qty ELSE  NULL END) as referenceField8,\n" +
            "SUM(ORD_QTY) as referenceField9,\n" +
            "count(ord_qty) as referenceField10 \n" +
            "from tbloutboundheader oh\n" +
            "join tbloutboundline ol on ol.ref_doc_no = oh.ref_doc_no\n" +
            "where ol.ref_field_2 is null and \n" +
            "(COALESCE(:warehouseId, null) IS NULL OR (oh.wh_id IN (:warehouseId))) and \n" +
            "(COALESCE(:refDocNo, null) IS NULL OR (oh.ref_doc_no IN (:refDocNo))) and \n" +
            "(COALESCE(:partnerCode, null) IS NULL OR (oh.partner_code IN (:partnerCode))) and \n" +
            "(COALESCE(:outboundOrderTypeId, null) IS NULL OR (oh.ob_ord_typ_id IN (:outboundOrderTypeId))) and \n" +
            "(COALESCE(:statusId, null) IS NULL OR (oh.status_id IN (:statusId))) and \n" +
            "(COALESCE(:soType, null) IS NULL OR (oh.ref_field_1 IN (:soType))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) IS NULL OR (oh.REQ_DEL_DATE between COALESCE(CONVERT(VARCHAR(255), :startRequiredDeliveryDate), null) and COALESCE(CONVERT(VARCHAR(255), :endRequiredDeliveryDate), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) IS NULL OR (oh.DLV_CNF_ON between COALESCE(CONVERT(VARCHAR(255), :startDeliveryConfirmedOn), null) and COALESCE(CONVERT(VARCHAR(255), :endDeliveryConfirmedOn), null))) and\n" +
            "(COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) IS NULL OR (oh.DLV_CTD_ON between COALESCE(CONVERT(VARCHAR(255), :startOrderDate), null) and COALESCE(CONVERT(VARCHAR(255), :endOrderDate), null)))\n" +
            "group by oh.c_id , oh.lang_id, oh.partner_code, oh.plant_id, oh.pre_ob_no,oh.ref_doc_no ,oh.wh_id,oh.dlv_ctd_by,oh.dlv_ctd_on,oh.is_deleted,oh.dlv_cnf_by,oh.dlv_cnf_on,\n" +
            "oh.dlv_ord_no, oh.ob_ord_typ_id,oh.ref_doc_date,oh.ref_doc_typ,oh.remark,oh.req_del_date,oh.dlv_rev_by,oh.dlv_rev_on,oh.status_id,oh.dlv_utd_by,oh.dlv_utd_on,\n" +
            "oh.ref_field_1,oh.ref_field_2,oh.ref_field_3,oh.ref_field_4,oh.ref_field_5,oh.ref_field_6,\n" +
            "ol.ref_doc_no , ol.c_id , ol.lang_id, ol.plant_id, ol.wh_id, ol.pre_ob_no, ol.partner_code", nativeQuery = true)
    Stream<OutboundHeaderStream> findAllOutBoundHeader(
            @Param(value = "warehouseId") List<String> warehouseId,
            @Param(value = "refDocNo") List<String> refDocNo,
            @Param(value = "partnerCode") List<String> partnerCode,
            @Param(value = "outboundOrderTypeId") List<Long> outboundOrderTypeId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "soType") List<String> soType,
            @Param(value = "startRequiredDeliveryDate") Date startRequiredDeliveryDate,
            @Param(value = "endRequiredDeliveryDate") Date endRequiredDeliveryDate,
            @Param(value = "startDeliveryConfirmedOn") Date startDeliveryConfirmedOn,
            @Param(value = "endDeliveryConfirmedOn") Date endDeliveryConfirmedOn,
            @Param(value = "startOrderDate") Date startOrderDate,
            @Param(value = "endOrderDate") Date endOrderDate);

    @Query(value = "select status_text from tblstatusid where \n" +
            "status_id in (:statusId) and wh_id in (:warehouseId) and \n" +
            "lang_id in (:languageId) and c_id in (:companyCodeId) and is_deleted = 0", nativeQuery = true)
    public String findStatusDescription(@Param(value = "statusId") Long statusId,
                                        @Param(value = "warehouseId") String warehouseId,
                                        @Param(value = "languageId") String languageId,
                                        @Param(value = "companyCodeId") String companyCodeId);

    //============================================================================================================================

    /*
     * Calling this Stored Proc during STATUS_ID = 57
     * ------------------------------------------------------
     *  @warehouseId, @refDocNumber, @preOutboundNo, @partnerCode, @updatedBy
     */
    @Transactional
    @Procedure(procedureName = "obheader_status_57_update_proc")
    public void updateOBHeaderByProcedure(
            @Param("warehouseId") String warehouseId,
            @Param("refDocNumber") String refDocNumber,
            @Param("preOutboundNo") String preOutboundNo,
            @Param("partnerCode") String partnerCode,
            @Param("updatedBy") String updatedBy
    );

    OutboundHeader findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode, Long l);
}