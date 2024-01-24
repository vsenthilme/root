package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.dto.ImPartner;
import com.tekclover.wms.api.transaction.repository.ImPartnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ImPartnerService {

	@Autowired
	private ImPartnerRepository impartnerRepository;
	public List<ImPartner> getImpartnerList(String companyCodeId, String plantId, String languageId, String warehouseId,
											String partnerItemBarcode, String itemCode, String manufacturerName) {

		List<ImPartner> imPartnerList =
				impartnerRepository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPartnerItemBarcodeAndItemCodeAndManufacturerNameAndDeletionIndicator(
						companyCodeId, plantId, languageId, warehouseId, partnerItemBarcode, itemCode, manufacturerName, 0L);
//		if(imPartnerList != null && !imPartnerList.isEmpty()) {
			log.info("Impartner List: " + imPartnerList);
			return imPartnerList;
//		}

//		throw new BadRequestException("ImPartner Not Found: " + partnerItemBarcode + ", " + itemCode + ", " + manufacturerName);
	}

	/**
	 *
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param partnerItemBarcode
	 * @return
	 */
	public List<ImPartner> getImpartnerBarcodeList(String companyCodeId, String plantId, String languageId, String warehouseId,
												   String partnerItemBarcode) {

		List<ImPartner> imPartnerList =
				impartnerRepository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPartnerItemBarcodeAndDeletionIndicator(
						companyCodeId, plantId, languageId, warehouseId, partnerItemBarcode, 0L);
//		if(imPartnerList != null && !imPartnerList.isEmpty()) {
			log.info("Impartner List: " + imPartnerList);
			return imPartnerList;
//		}
//		throw new BadRequestException("ImPartner Barcode Not Found: " + partnerItemBarcode );
	}

	/**
	 *
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param itemCode
	 * @param manufacturerName
	 * @return
	 */
	public List<ImPartner> getImpartnerItemCodeList(String companyCodeId, String plantId, String languageId,
													String warehouseId, String itemCode, String manufacturerName) {

		List<ImPartner> imPartnerList =
				impartnerRepository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerNameAndDeletionIndicator(
						companyCodeId, plantId, languageId, warehouseId, itemCode, manufacturerName, 0L);
//		if(imPartnerList != null && !imPartnerList.isEmpty()) {
			log.info("Impartner List: " + imPartnerList);
			return imPartnerList;
//		}
//		throw new BadRequestException("ImPartner Not Found: " + itemCode + ", " + manufacturerName);
	}

}
