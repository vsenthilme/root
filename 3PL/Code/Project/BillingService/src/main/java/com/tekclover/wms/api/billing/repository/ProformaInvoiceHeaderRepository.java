package com.tekclover.wms.api.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.billing.model.proformainvoiceheader.ProformaInvoiceHeader;

import java.util.Optional;

@Repository
@Transactional
public interface ProformaInvoiceHeaderRepository extends JpaRepository<ProformaInvoiceHeader,String>, JpaSpecificationExecutor<String> {

    Optional<ProformaInvoiceHeader> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndProformaBillNoAndPartnerCodeAndLanguageIdAndDeletionIndicator(String companyCode, String plantId, String warehouseId, String proformaBillNo, String partnerCode, String languageId, Long deletionIndicator);
}
