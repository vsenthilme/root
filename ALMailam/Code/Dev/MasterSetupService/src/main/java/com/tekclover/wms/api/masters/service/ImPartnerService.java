package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.masters.model.impartner.SearchImPartner;
import com.tekclover.wms.api.masters.repository.specification.ImPartnerSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.impartner.AddImPartner;
import com.tekclover.wms.api.masters.model.impartner.ImPartner;
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
	public List<ImPartner> getImPartner (String companyCodeId, String plantId, String languageId, String warehouseId, String itemCode, String manufacturerName ) {
		List<ImPartner> impartner =
				impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndManufacturerNameAndDeletionIndicator(
						companyCodeId,
						plantId,
						warehouseId,
						languageId,
						itemCode,
						manufacturerName,
						0L);
		if(impartner.isEmpty()) {
			throw new BadRequestException("The given values: " +
					" plantId " + plantId +
					" itemCode " + itemCode +
					" manufacturerName " + manufacturerName +
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
		List<ImPartner> results = impartnerRepository.stream(spec, ImPartner.class).collect(Collectors.toList());
		log.info("results: " + results);
		return results;
	}

//	public List<ImPartner> createImPartner (List<AddImPartner> newImPartner, String loginUserID) {
//
//		List<ImPartner>imPartnerList=new ArrayList<>();
//
//		for(AddImPartner addImPartner:newImPartner) {
//			List<ImPartner> duplicateImPartner = impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndPartnerItemBarcodeAndDeletionIndicator(
//					addImPartner.getCompanyCodeId(), addImPartner.getPlantId(),
//					addImPartner.getWarehouseId(), addImPartner.getLanguageId(),
//					addImPartner.getPartnerItemBarcode(), 0L);
//			log.info("ImPartner with BarcodeId: " + duplicateImPartner);
//
//			if (duplicateImPartner != null && !duplicateImPartner.isEmpty()) {
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
	 *
	 * @param newImPartner
	 * @param loginUserID
	 * @return
	 */
	public List<ImPartner> createImPartner (List<AddImPartner> newImPartner, String loginUserID) {

		try {
			List<ImPartner>imPartnerList=new ArrayList<>();

			for(AddImPartner addImPartner:newImPartner) {

				//Duplicate Barcode Validation
				List<ImPartner> duplicateBarcodeCheck = impartnerRepository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPartnerItemBarcodeAndDeletionIndicator(
						addImPartner.getCompanyCodeId(), addImPartner.getPlantId(), addImPartner.getLanguageId(), addImPartner.getWarehouseId(),
						addImPartner.getPartnerItemBarcode(), 0L);
				log.info("Duplicate BarcodeId : " + duplicateBarcodeCheck);
				if(duplicateBarcodeCheck != null && !duplicateBarcodeCheck.isEmpty()) {
					for(ImPartner dbImPartner : duplicateBarcodeCheck) {
						String dbItemCode = dbImPartner.getItemCode();
						String dbManufacturerName = dbImPartner.getManufacturerName();
						String dbItmMfrName = dbItemCode+dbManufacturerName;
						String newItemCode = addImPartner.getItemCode();
						String newManufacturerName = addImPartner.getManufacturerName();
						String newItmMfrName = newItemCode+newManufacturerName;
						log.info("dbItmMfrName, newItmMfrName : " + dbItmMfrName + ", " + newItmMfrName);
						if(!dbItmMfrName.equalsIgnoreCase(newItmMfrName)) {
							throw new BadRequestException("BarcodeId already exist: " + addImPartner.getPartnerItemBarcode());
						}
					}
				}

				List<ImPartner> existingBarcodeId = impartnerRepository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerNameAndPartnerItemBarcodeAndDeletionIndicator(
						addImPartner.getCompanyCodeId(), addImPartner.getPlantId(), addImPartner.getLanguageId(), addImPartner.getWarehouseId(),
						addImPartner.getItemCode(), addImPartner.getManufacturerName(), addImPartner.getPartnerItemBarcode(), 0L);
				log.info("Existing BarcodeId : " + existingBarcodeId);

				if(existingBarcodeId != null && !existingBarcodeId.isEmpty()) {
					return existingBarcodeId;
				}

				ImPartner imPartner = new ImPartner();
				BeanUtils.copyProperties(addImPartner, imPartner, CommonUtils.getNullPropertyNames(addImPartner));
				imPartner.setDeletionIndicator(0L);
				imPartner.setCreatedBy(loginUserID);
				imPartner.setUpdatedBy(loginUserID);
				imPartner.setCreatedOn(new Date());
				imPartner.setUpdatedOn(new Date());
				ImPartner savedImPartner = impartnerRepository.save(imPartner);
				imPartnerList.add(savedImPartner);
			}
			return imPartnerList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
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

//	public List<ImPartner> updateImPartner (String companyCodeId, String plantId, String languageId, String warehouseId,
//											String itemCode, String manufacturerName, List<AddImPartner> updateImPartner,
//											String loginUserID)
//			throws IllegalAccessException, InvocationTargetException, ParseException {
//
//		List<ImPartner> dbImPartner =
//				impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndManufacturerNameAndDeletionIndicator(
//						companyCodeId, plantId, warehouseId,languageId, itemCode, manufacturerName,0L);
//
//		if(dbImPartner!=null) {
//			for (ImPartner newImPartner : dbImPartner) {
//				newImPartner.setUpdatedBy(loginUserID);
//				newImPartner.setUpdatedOn((new Date()));
//				newImPartner.setDeletionIndicator(1L);
//				impartnerRepository.save(newImPartner);
//			}
//		}
//	 else {
//		throw new EntityNotFoundException("The Given Values of companyId " + companyCodeId +
//				" plantId " + plantId +
//				" manufacturerName " + manufacturerName +
//				" warehouseId " + warehouseId +
//				" languageId " + languageId +
//				" itemCode " + itemCode + "doesn't exists");
//	}
//
//	 List<ImPartner>createImPartner=createImPartner(updateImPartner,loginUserID);
//	 return createImPartner;
//	}
public List<ImPartner> updateImPartner (String companyCodeId, String plantId, String languageId, String warehouseId,
										String itemCode, String manufacturerName, List<AddImPartner> updateImPartner,
										String loginUserID) {

			List<ImPartner> updatedImpartnerList = new ArrayList<>();
			for (AddImPartner newImPartner : updateImPartner) {
				ImPartner dbImpartner = impartnerRepository.findByBusinessPartnerCodeAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndBusinessPartnerTypeAndPartnerItemBarcodeAndManufacturerNameAndDeletionIndicator(
						newImPartner.getBusinessPartnerCode(), companyCodeId, plantId, warehouseId, languageId, itemCode,
						newImPartner.getBusinessPartnerType(), newImPartner.getPartnerItemBarcode(), manufacturerName, 0L);

				if (dbImpartner != null) {
					BeanUtils.copyProperties(newImPartner, dbImpartner, CommonUtils.getNullPropertyNames(newImPartner));
					dbImpartner.setUpdatedBy(loginUserID);
					dbImpartner.setUpdatedOn((new Date()));
					impartnerRepository.save(dbImpartner);
					updatedImpartnerList.add(dbImpartner);
					log.info("Impartner Updated: " + dbImpartner);
				} else {
					ImPartner imPartner = new ImPartner();
					BeanUtils.copyProperties(newImPartner, imPartner, CommonUtils.getNullPropertyNames(newImPartner));
					imPartner.setDeletionIndicator(0L);
					imPartner.setCreatedBy(loginUserID);
					imPartner.setUpdatedBy(loginUserID);
					imPartner.setCreatedOn(new Date());
					imPartner.setUpdatedOn(new Date());
					impartnerRepository.save(imPartner);
					updatedImpartnerList.add(imPartner);
					log.info("Created Impartner: " + imPartner);
				}
			}
	 	return updatedImpartnerList;
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
	public void deleteImPartner (String companyCodeId, String plantId, String languageId, String warehouseId,
								 String itemCode, String manufacturerName, String loginUserID) throws ParseException {

		List<ImPartner> impartner =
				impartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndItemCodeAndManufacturerNameAndDeletionIndicator(
						companyCodeId,
						plantId,
						warehouseId,
						languageId,
						itemCode,
						manufacturerName,
						0L);

		if ( impartner != null) {
			for(ImPartner dbImpartner:impartner) {
				dbImpartner.setDeletionIndicator(1L);
				dbImpartner.setUpdatedBy(loginUserID);
				dbImpartner.setUpdatedOn(new Date());
				impartnerRepository.save(dbImpartner);
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id:" + itemCode);
		}
	}
}
