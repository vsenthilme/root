package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InboundHeaderV2Repository extends JpaRepository<InboundHeaderV2, Long>,
        JpaSpecificationExecutor<InboundHeaderV2>, StreamableJpaSpecificationRepository<InboundHeaderV2> {

    Optional<InboundHeaderV2> findByWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);

    List<InboundHeaderV2> findByRefDocNumberAndDeletionIndicator(String refDocNumber, Long deletionIndicator);

    Optional<InboundHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);

    @Query(value = "Select \r\n" +
            "CASE \r\n" +
            "When OrderLinesCount IS NOT NULL THEN OrderLinesCount \r\n" +
            "Else 0 \r\n" +
            "END as countOfOrderLines \r\n" +
            "From \r\n" +
            "(Select COUNT(*) as OrderLinesCount \r\n" +
            "From tblinboundline \r\n" +
            "Where ref_doc_no IN (:refDocNumber) AND is_deleted = 0 \r\n" +
            ") As CountsSubquery ", nativeQuery = true)
    Long getCountOfTheOrderLinesByRefDocNumber(@Param(value = "refDocNumber") String refDocNumber);

    @Query(value = "Select \r\n" +
            "CASE \r\n" +
            "When SUM(il.accept_qty + il.damage_qty) > 0 Then \r\n" +
            "(Select COUNT(*) \r\n" +
            "From tblinboundline \r\n" +
            "Where ref_doc_no IN (:refDocNumber) And is_deleted = 0 AND STATUS_ID = 20) \r\n" +
            "Else 0 \r\n" +
            "END as receivedLines \r\n" +
            "From tblinboundline il ", nativeQuery = true)
    Long getReceivedLinesByRefDocNumberOld(@Param(value = "refDocNumber") String refDocNumber);

    @Query(value =
            "Select COUNT(*) \r\n" +
            "From tblputawayline \r\n" +
            "Where ref_doc_no IN (:refDocNumber) And is_deleted = 0 AND STATUS_ID IN (20,24) \r\n"
           , nativeQuery = true)
    Long getReceivedLinesByRefDocNumber(@Param(value = "refDocNumber") String refDocNumber);

    InboundHeaderV2 findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String refDocNumber, Long deletionIndicator);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE InboundHeaderV2 ib SET ib.statusId = :statusId, ib.confirmedBy = :confirmedBy, ib.confirmedOn = :confirmedOn, ib.statusDescription = :statusDescription \n" +
            "WHERE ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber and ib.companyCode = :companyCode and ib.plantId = :plantId and ib.languageId = :languageId")
    void updateInboundHeaderStatus(@Param("warehouseId") String warehouseId,
                                   @Param("companyCode") String companyCode,
                                   @Param("plantId") String plantId,
                                   @Param("languageId") String languageId,
                                   @Param("refDocNumber") String refDocNumber,
                                   @Param("statusId") Long statusId,
                                   @Param("statusDescription") String statusDescription,
                                   @Param("confirmedBy") String confirmedBy,
                                   @Param("confirmedOn") Date confirmedOn);

    @Transactional
    @Procedure(procedureName = "ibheader_status_update_ib_cnf_proc")
    public void updateIbheaderStatusUpdateInboundConfirmProc(
            @Param("companyCodeId") String companyCode,
            @Param("plantId") String plantId,
            @Param("languageId") String languageId,
            @Param("warehouseId") String warehouseId,
            @Param("refDocNumber") String refDocNumber,
            @Param("preInboundNo") String preInboundNo,
            @Param("statusId") Long statusId,
            @Param("statusDescription") String statusDescription,
            @Param("updatedBy") String updatedBy,
            @Param("updatedOn") Date updatedOn
    );
}