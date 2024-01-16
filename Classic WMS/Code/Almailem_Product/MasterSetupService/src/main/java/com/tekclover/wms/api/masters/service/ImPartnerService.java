package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
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

//	/**
//	 *
//	 * @param businessPartnerCode
//	 * @param companyCodeId
//	 * @param plantId
//	 * @param languageId
//	 * @param warehouseId
//	 * @param itemCode
//	 * @param businessPartnerType
//	 * @param partnerItemBarcode
//	 * @return
//	 */
//	public List<ImPartner> getImPartner (String businessPartnerCode,String companyCodeId,String plantId,String languageId,
//										 String warehouseId,String itemCode,String businessPartnerType,String partnerItemBarcode ) {
//		List<ImPartner> impartner =
//				impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndDeletionIndicator(
//						businessPartnerCode,
//						companyCodeId,
//						plantId,
//						warehouseId,
//						languageId,
//						itemCode,
//						businessPartnerType,
//						partnerItemBarcode,
//						0L);
//		if(impartner.isEmpty()) {
//			throw new BadRequestException("The given values:" +
//					"businessPartnerCode " + businessPartnerCode +
//					"itemCode"+itemCode+
//					"plantId"+plantId+
//					"companyCodeId"+companyCodeId+" doesn't exist.");
//		}
//		return impartner;
//	}

	/**
	 *
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param itemCode
	 * @return
	 */
	public List<ImPartner> getImPartner (String companyCodeId,String plantId,String languageId,String warehouseId,String itemCode ) {
		List<ImPartner> impartner =
				impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndDeletionIndicator(
						companyCodeId,
						plantId,
						warehouseId,
						languageId,
						itemCode,
						0L);
		if(impartner.isEmpty()) {
			throw new BadRequestException("The given values:" +
					" plantId " + plantId +
					"itemCode" + itemCode +
					" warehouse " + warehouseId +
					" language Id " + languageId +
					" companyCodeId " + companyCodeId +" doesn't exist.");
		}
		return impartner;
	}

	/**
	 *
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

//
//	/**
//	 * createImPartner
//	 * @param newImPartner
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws InvocationTargetException
//	 */
//	public List<ImPartner> createImPartner (List<AddImPartner> newImPartner, String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//
//		List<ImPartner>imPartnerList=new ArrayList<>();
//
//		for(AddImPartner addImPartner:newImPartner) {
//			List<ImPartner> duplicateImPartner =
//					impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndDeletionIndicator(addImPartner.getBusinessPartnerCode(), addImPartner.getCompanyCodeId(),
//							addImPartner.getPlantId(),
//							addImPartner.getWarehouseId(),
//							addImPartner.getLanguageId(),
//							addImPartner.getItemCode(),
//							addImPartner.getBusinessPartnerType(),
//							addImPartner.getPartnerItemBarcode(),
//							0L);
//			if (!duplicateImPartner.isEmpty()) {
//				throw new BadRequestException("Record is Getting Duplicated");
//			} else {
//				ImPartner dbImPartner = new ImPartner();
//				BeanUtils.copyProperties(addImPartner, dbImPartner, CommonUtils.getNullPropertyNames(addImPartner));
//				dbImPartner.setDeletionIndicator(0L);
//				dbImPartner.setCreatedBy(loginUserID);
//				dbImPartner.setUpdatedBy(loginUserID);
//				dbImPartner.setCreatedOn(new Date());
//				dbImPartner.setUpdatedOn(new Date());
//				ImPartner savedImPartner = impartnerRepository.save(dbImPartner);
//				imPartnerList.add(savedImPartner);
//			}
//		}
//		return imPartnerList;
//	}


	/**
	 * createImPartner
	 * @param newImPartner
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<ImPartner> createImPartner (List<AddImPartner> newImPartner, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {

		List<ImPartner>imPartnerList=new ArrayList<>();

		for(AddImPartner addImPartner:newImPartner) {
			List<ImPartner> duplicateImPartner =
					impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndBrandNameAndManufacturerCodeAndManufacturerNameAndDeletionIndicator(
							addImPartner.getBusinessPartnerCode(),
							addImPartner.getCompanyCodeId(),
							addImPartner.getPlantId(),
							addImPartner.getWarehouseId(),
							addImPartner.getLanguageId(),
							addImPartner.getItemCode(),
							addImPartner.getBusinessPartnerType(),
							addImPartner.getPartnerItemBarcode(),
							addImPartner.getBrandName(),
							addImPartner.getManufacturerCode(),
							addImPartner.getManufacturerName(),
							0L);

			if (!duplicateImPartner.isEmpty()) {
				throw new BadRequestException("Record is Getting Duplicated");
			} else {
				ImPartner dbImPartner = new ImPartner();
				BeanUtils.copyProperties(addImPartner, dbImPartner, CommonUtils.getNullPropertyNames(addImPartner));
				dbImPartner.setDeletionIndicator(0L);
				dbImPartner.setCreatedBy(loginUserID);
				dbImPartner.setUpdatedBy(loginUserID);
				dbImPartner.setCreatedOn(DateUtils.getCurrentKWTDateTime());
				dbImPartner.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
				ImPartner savedImPartner = impartnerRepository.save(dbImPartner);
				imPartnerList.add(savedImPartner);
			}
		}
		return imPartnerList;
	}


	/**
	 *
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param itemCode
	 * @param updateImPartner
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
//	 */

	public List<ImPartner> updateImPartner (String companyCodeId,String plantId,
											String languageId,String warehouseId,String itemCode,List<AddImPartner> updateImPartner,
											String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {

		List<ImPartner> dbImPartner =
				impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndDeletionIndicator(
						companyCodeId, plantId, warehouseId,languageId, itemCode, 0L);

		if(dbImPartner!=null) {
			for (ImPartner newImPartner : dbImPartner) {
				newImPartner.setUpdatedBy(loginUserID);
				newImPartner.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
				newImPartner.setDeletionIndicator(1L);
				impartnerRepository.save(newImPartner);
			}
		}
	 else {
		throw new EntityNotFoundException("The Given Values of companyId " + companyCodeId +
				" plantId " + plantId +
				" warehouseId " + warehouseId +
				" languageId " + languageId +
				" itemCode " + itemCode + "doesn't exists");
	}

	List<ImPartner>createImPartner=createImPartner(updateImPartner,loginUserID);
		return createImPartner;
	}


	/**
	 *
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param itemCode
	 * @param loginUserID
	 */
		public void deleteImPartner (String companyCodeId,String plantId,String languageId,
				String warehouseId,String itemCode,String loginUserID) throws ParseException {

			List<ImPartner> impartner = getImPartner(companyCodeId,plantId,languageId,warehouseId,itemCode);

			if ( impartner != null) {
				for(ImPartner dbImpartner:impartner) {
					dbImpartner.setDeletionIndicator(1L);
					dbImpartner.setUpdatedBy(loginUserID);
					dbImpartner.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
					impartnerRepository.save(dbImpartner);
				}
			} else {
				throw new EntityNotFoundException("Error in deleting Id:" + itemCode);
			}
		}
}
