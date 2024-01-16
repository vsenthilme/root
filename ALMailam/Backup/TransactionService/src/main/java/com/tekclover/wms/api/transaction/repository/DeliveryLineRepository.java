package com.tekclover.wms.api.transaction.repository;


import com.tekclover.wms.api.transaction.model.deliveryline.DeliveryLine;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DeliveryLineRepository extends JpaRepository<DeliveryLine,String>,
        JpaSpecificationExecutor<DeliveryLine>, StreamableJpaSpecificationRepository<DeliveryLine> {

 Optional<DeliveryLine> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeliveryNoAndItemCodeAndLineNoAndInvoiceNumberAndRefDocNumberAndDeletionIndicator(
         String companyCodeId,String plantId,String warehouseId,String languageId,String deliveryNo,String itemCode,Long lineNo,String invoiceNumber,String refDocNumber,Long deletionIndicator);
}
