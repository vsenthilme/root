package com.tekclover.wms.api.transaction.repository;

import java.util.List;

import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.outbound.OutboundLineInterim;

@Repository
@Transactional
public interface OutboundLineInterimRepository extends JpaRepository<OutboundLineInterim, Long>,
        JpaSpecificationExecutor<OutboundLineInterim>, StreamableJpaSpecificationRepository<OutboundLineInterim> {

    public List<OutboundLineInterim> findAll();

    @Query(value = "SELECT SUM(DLV_QTY) FROM tbloutboundlinedup\r\n"
            + "WHERE WH_ID = :warehouseId AND PRE_OB_NO = :preOutboundNo \r\n"
            + "AND REF_DOC_NO = :refDocNumber AND PARTNER_CODE = :partnerCode\r\n"
            + "AND OB_LINE_NO = :lineNumber AND ITM_CODE = :itemCode AND IS_DELETED = 0 \r\n"
            + "GROUP BY ITM_CODE, OB_LINE_NO, PARTNER_CODE, REF_DOC_NO, PRE_OB_NO, WH_ID", nativeQuery = true)
    public Double getSumOfDeliveryLine(@Param("warehouseId") String warehouseId,
                                       @Param("preOutboundNo") String preOutboundNo,
                                       @Param("refDocNumber") String refDocNumber,
                                       @Param("partnerCode") String partnerCode,
                                       @Param("lineNumber") Long lineNumber,
                                       @Param("itemCode") String itemCode);

    public List<OutboundLineInterim> findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber,
            String itemCode, Long deletionIndicator);
}