package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StagingHeaderV2Repository extends JpaRepository<StagingHeaderV2, Long>,
        JpaSpecificationExecutor<StagingHeaderV2>, StreamableJpaSpecificationRepository<StagingHeaderV2> {

    Optional<StagingHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, String stagingNo, Long deletionIndicator);

    List<StagingHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId,
            String preInboundNo, String refDocNumber, Long deletionIndicator);

    List<StagingHeaderV2> findByWarehouseIdAndStatusIdAndDeletionIndicator(
            String warehouseId, Long statusId, Long deletionIndicator);

    Optional<StagingHeaderV2> findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
            String companyCode, String plantId, String languageId, String warehouseId, String preInboundNo, String refDocNumber, Long deletionIndicator);
}