package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.PreOutboundLineV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PreOutboundLineV2Repository extends JpaRepository<PreOutboundLineV2, Long>,
        JpaSpecificationExecutor<PreOutboundLineV2>, StreamableJpaSpecificationRepository<PreOutboundLineV2> {


    Optional<PreOutboundLineV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber,
            String preOutboundNo, String partnerCode, Long lineNumber, String itemCode, Long deletionIndicator);

    List<PreOutboundLineV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String refDocNumber, String preOutboundNo, String partnerCode, Long deletionIndicator);

    List<PreOutboundLineV2> findByPreOutboundNo(String preOutboundNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PreOutboundLine ob SET ob.statusId = :statusId \n" +
            "WHERE ob.companyCodeId = :companyCodeId AND ob.plantId = :plantId AND ob.languageId = :languageId AND ob.warehouseId = :warehouseId AND ob.refDocNumber = :refDocNumber")
    void updatePreOutboundLineStatusV2(@Param("companyCodeId") String companyCodeId,
                                       @Param("plantId") String plantId,
                                       @Param("languageId") String languageId,
                                       @Param("warehouseId") String warehouseId,
                                       @Param("refDocNumber") String refDocNumber,
                                       @Param("statusId") Long statusId);
}