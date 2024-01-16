package com.tekclover.wms.api.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.billing.model.invoiceheader.InvoiceHeader;

import java.util.Optional;

@Repository
@Transactional
public interface InvoiceHeaderRepository extends JpaRepository<InvoiceHeader,String >,JpaSpecificationExecutor<InvoiceHeader> {
    Optional<InvoiceHeader> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndInvoiceNumberAndPartnerCodeAndLanguageIdAndDeletionIndicator(String companyCode, String plantId, String warehouseId, String invoiceNumber, String partnerCode, String languageId, Long deletionIndicator);
}
