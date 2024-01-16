package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryMovementV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface InventoryMovementV2Repository extends JpaRepository<InventoryMovementV2,Long>, JpaSpecificationExecutor<InventoryMovementV2> {

	Optional<InventoryMovementV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndMovementTypeAndSubmovementTypeAndPackBarcodesAndItemCodeAndBatchSerialNumberAndMovementDocumentNoAndDeletionIndicator(
			String languageId, String companyCode, String plantId, String warehouseId,
			Long movementType, Long submovementType, String packBarcodes, String itemCode,
			String batchSerialNumber, String movementDocumentNo, Long deletionIndicator);
}