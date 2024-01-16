package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.PreOutboundHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreOutboundHeaderV2Repository extends JpaRepository<PreOutboundHeaderV2,Long>, JpaSpecificationExecutor<PreOutboundHeaderV2>,
																StreamableJpaSpecificationRepository<PreOutboundHeaderV2> {

	Optional<PreOutboundHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(String languageId, String comapnyCodeId, String plantId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, long l);

	PreOutboundHeaderV2 findByPreOutboundNo(String preOutboundNo);
}