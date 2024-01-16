package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            "Where ref_doc_no IN (:refDocNumber) And is_deleted = 0) \r\n" +
            "Else 0 \r\n" +
            "END as receivedLines \r\n" +
            "From tblinboundline il ", nativeQuery = true)
    Long getReceivedLinesByRefDocNumber(@Param(value = "refDocNumber") String refDocNumber);
}