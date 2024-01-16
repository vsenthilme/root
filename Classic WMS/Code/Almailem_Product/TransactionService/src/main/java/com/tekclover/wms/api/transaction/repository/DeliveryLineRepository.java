package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.deliveryline.DeliveryLine;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DeliveryLineRepository extends JpaRepository<DeliveryLine, String>,
        JpaSpecificationExecutor<DeliveryLine>, StreamableJpaSpecificationRepository<DeliveryLine> {

    Optional<DeliveryLine> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeliveryNoAndItemCodeAndLineNumberAndInvoiceNumberAndRefDocNumberAndDeletionIndicator(
            String companyCodeId, String plantId, String warehouseId, String languageId, Long deliveryNo, String itemCode,
            Long lineNumber, String invoiceNumber, String refDocNumber, Long deletionIndicator);

    List<DeliveryLine> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndDeliveryNoAndItemCodeAndLineNumberAndInvoiceNumberAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, Long deliveryNo,
            String itemCode, Long lineNumber, String invoiceNumber, String refDocNumber, Long deletionIndicator);
}
