package com.tekclover.wms.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.masters.model.impartner.ImPartner;

@Repository
@Transactional
public interface ImPartnerRepository extends JpaRepository<ImPartner,Long>, JpaSpecificationExecutor<ImPartner> {

	Optional<ImPartner> findByBusinessPartnerCode(String businessPartnerCode);
	List<ImPartner> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndDeletionIndicator(
			 String companyCodeId, String plantId, String warehouseId, String languageId, String itemCode, Long deletionIndicator );

List<ImPartner> findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndBrandNameAndManufacturerCodeAndManufacturerNameAndDeletionIndicator(
			String businessPartnerCode, String companyCodeId, String plantId, String warehouseId, String languageId, String itemCode, String businessPartnerType, String partnerItemBarcode,
			String brandName,String manufacturerCode,String manufacturerName, Long deletionIndicator );




}