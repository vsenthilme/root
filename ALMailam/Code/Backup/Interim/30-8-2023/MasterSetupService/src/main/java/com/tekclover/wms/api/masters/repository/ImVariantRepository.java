package com.tekclover.wms.api.masters.repository;

import com.tekclover.wms.api.masters.model.imvariant.ImVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImVariantRepository extends JpaRepository<ImVariant,Long>, JpaSpecificationExecutor<ImVariant> {


    Optional<ImVariant> findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndVariantCodeAndVariantTypeAndDeletionIndicator(String companyCodeId,String languageId,String plantId,String warehouseId,String itemCode,Long variantCode,String variantType,Long deletionIndicator);
}
