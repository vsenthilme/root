package com.tekclover.wms.api.idmaster.repository;

import com.tekclover.wms.api.idmaster.model.hhtuser.OrderTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderTypeIdRepository extends JpaRepository<OrderTypeId,String>, JpaSpecificationExecutor<OrderTypeId> {

    List<OrderTypeId> findByUserIdAndDeletionIndicator(String userId, Long deletionIndicator);

    List<OrderTypeId> findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndUserIdAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String userId, Long deletionIndicator);
}
