package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.repository.specification.InboundHeaderV2Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboundHeaderV2Repository extends JpaRepository<InboundHeaderV2, Long>,
        JpaSpecificationExecutor<InboundHeaderV2> {

    Optional<InboundHeaderV2> findByWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);

    List<InboundHeaderV2> findByRefDocNumberAndDeletionIndicator(String refDocNumber, Long deletionIndicator);

    Optional<InboundHeaderV2> findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId,
            String warehouseId, String refDocNumber, String preInboundNo, Long deletionIndicator);
}