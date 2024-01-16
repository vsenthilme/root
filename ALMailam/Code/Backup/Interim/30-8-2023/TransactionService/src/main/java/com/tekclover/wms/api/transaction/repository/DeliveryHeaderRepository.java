package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryheader.DeliveryHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DeliveryHeaderRepository extends JpaRepository<DeliveryHeader,String>, JpaSpecificationExecutor<DeliveryHeader> {

    Optional<DeliveryHeader> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndDeliveryNoAndLanguageIdAndDeletionIndicator(
            String companyCodeId,String plantId,String warehouseId,String deliveryHeader,String languageId,Long deletionIndicator);
}
