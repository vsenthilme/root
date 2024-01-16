package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.masters.model.impartner.SearchImPartner;
import com.tekclover.wms.api.masters.repository.specification.ImPartnerSpecification;
import com.tekclover.wms.api.masters.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.impartner.AddImPartner;
import com.tekclover.wms.api.masters.model.impartner.ImPartner;
import com.tekclover.wms.api.masters.model.impartner.UpdateImPartner;
import com.tekclover.wms.api.masters.repository.ImPartnerRepository;
import com.tekclover.wms.api.masters.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImPartnerService {

	@Autowired
	private ImPartnerRepository impartnerRepository;

	/**
	 * getImPartners
	 * @return
	 */
	public List<ImPartner> getImPartners () {
		List<ImPartner> impartnerList = impartnerRepository.findAll();
		log.info("impartnerList : " + impartnerList);
		impartnerList = impartnerList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return impartnerList;
	}

	/**
	 * getImPartner
	 * @param businessPartnerCode
	 * @return
	 */
	public ImPartner getImPartner (String businessPartnerCode,String companyCodeId,String plantId,String languageId,String warehouseId,String itemCode,String businessPartnerType,String partnerItemBarcode ) {
		Optional<ImPartner> impartner = impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndDeletionIndicator(
				businessPartnerCode,
				companyCodeId,
				plantId,
				warehouseId,
				languageId,
				itemCode,
				businessPartnerType,
				partnerItemBarcode,
				0L);
		if(impartner.isEmpty()) {
			throw new BadRequestException("The given values:" +
					"businessPartnerCode " + businessPartnerCode +
					"itemCode"+itemCode+
					"plantId"+plantId+
					"companyCodeId"+companyCodeId+" doesn't exist.");
		}
		return impartner.get();
	}

	/**
	 * findImPartner
	 * @param searchImPartner
	 * @return
	 * @throws ParseException
	 */
	public List<ImPartner> findImPartner(SearchImPartner searchImPartner) throws ParseException {

		ImPartnerSpecification spec = new ImPartnerSpecification(searchImPartner);
		List<ImPartner> results = impartnerRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 * createImPartner
	 * @param newImPartner
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImPartner createImPartner (AddImPartner newImPartner, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ImPartner dbImPartner = new ImPartner();
		Optional<ImPartner> duplicateImPartner = impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndDeletionIndicator(newImPartner.getBusinessPartnerCode(), newImPartner.getCompanyCodeId(), newImPartner.getPlantId(), newImPartner.getWarehouseId(), newImPartner.getLanguageId(), newImPartner.getItemCode(), newImPartner.getBusinessPartnerType(), newImPartner.getPartnerItemBarcode(), 0L);
		if (!duplicateImPartner.isEmpty()) {
			throw new BadRequestException("Record is Getting Duplicated");
		} else {
			BeanUtils.copyProperties(newImPartner, dbImPartner, CommonUtils.getNullPropertyNames(newImPartner));
			dbImPartner.setDeletionIndicator(0L);
			dbImPartner.setCreatedBy(loginUserID);
			dbImPartner.setUpdatedBy(loginUserID);
			dbImPartner.setCreatedOn(new Date());
			dbImPartner.setUpdatedOn(new Date());
			return impartnerRepository.save(dbImPartner);
		}
	}

	/**
	 * updateImPartner
	 * @param impartner
	 * @param updateImPartner
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImPartner updateImPartner (String businessPartnerCode,String companyCodeId,String plantId,String languageId,String warehouseId,String itemCode,String businessPartnerType,String partnerItemBarcode,UpdateImPartner updateImPartner, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ImPartner dbImPartner = getImPartner(businessPartnerCode,companyCodeId,plantId,languageId,warehouseId,itemCode,businessPartnerType,partnerItemBarcode);
		BeanUtils.copyProperties(updateImPartner, dbImPartner, CommonUtils.getNullPropertyNames(updateImPartner));
		dbImPartner.setUpdatedBy(loginUserID);
		dbImPartner.setUpdatedOn(new Date());
		return impartnerRepository.save(dbImPartner);
	}

	/**
	 * deleteImPartner
	 * @param impartner
	 */
	public void deleteImPartner (String businessPartnerCode,String companyCodeId,String plantId,String languageId,String warehouseId,String itemCode,String businessPartnerType,String partnerItemBarcode, String loginUserID) {
		ImPartner impartner = getImPartner(businessPartnerCode,companyCodeId,plantId,languageId,warehouseId,itemCode,businessPartnerType,partnerItemBarcode);
		if ( impartner != null) {
			impartner.setDeletionIndicator (1L);
			impartner.setUpdatedBy(loginUserID);
			impartner.setUpdatedOn(new Date());
			impartnerRepository.save(impartner);
		} else {
			throw new EntityNotFoundException("Error in deleting Id:" + businessPartnerCode);
		}
	}
}
