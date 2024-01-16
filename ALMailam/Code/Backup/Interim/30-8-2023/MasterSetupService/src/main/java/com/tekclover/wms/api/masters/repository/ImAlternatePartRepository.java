package com.tekclover.wms.api.masters.repository;

import com.tekclover.wms.api.masters.model.imalternateparts.ImAlternatePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ImAlternatePartRepository extends JpaRepository<ImAlternatePart,String>, JpaSpecificationExecutor<ImAlternatePart> {

    Optional<ImAlternatePart>findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndAltItemCodeAndDeletionIndicator(String companyCodeId,String languageId,
                                                                                                                                   String plantId,String warehouseId,String itemCode,String altItemCode,Long deletionIndicator);
}
