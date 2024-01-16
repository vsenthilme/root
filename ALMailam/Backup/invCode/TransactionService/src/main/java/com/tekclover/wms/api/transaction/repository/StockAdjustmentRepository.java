package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.cyclecount.stockadjustment.StockAdjustment;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, String>,
        JpaSpecificationExecutor<StockAdjustment>, StreamableJpaSpecificationRepository<StockAdjustment> {


    StockAdjustment findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentIdAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            String manufacturerName, String storageBin, Long stockAdjustmentId, Long deletionIndicator);
}