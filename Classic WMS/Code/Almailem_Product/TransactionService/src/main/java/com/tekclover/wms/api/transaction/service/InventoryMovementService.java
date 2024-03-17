package com.tekclover.wms.api.transaction.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import com.opencsv.exceptions.CsvException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.errorlog.ErrorLog;
import com.tekclover.wms.api.transaction.repository.ErrorLogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.inbound.inventory.AddInventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.SearchInventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.UpdateInventoryMovement;
import com.tekclover.wms.api.transaction.repository.InventoryMovementRepository;
import com.tekclover.wms.api.transaction.repository.specification.InventoryMovementSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryMovementService extends BaseService {
	
	@Autowired
	private InventoryMovementRepository inventoryMovementRepository;
	
	@Autowired
	private ErrorLogRepository errorLogRepository;

	@Autowired
	private ErrorLogService errorLogService;
	
	/**
	 * getInventoryMovements
	 * @return
	 */
	public List<InventoryMovement> getInventoryMovements () {
		List<InventoryMovement> inventoryMovementList =  inventoryMovementRepository.findAll();
		inventoryMovementList = 
				inventoryMovementList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return inventoryMovementList;
	}
	
	/**
	 * getInventoryMovement
	 * @param movementType
	 * @return
	 */
	public InventoryMovement getInventoryMovement (String warehouseId, Long movementType, Long submovementType, String packBarcodes, 
			String itemCode, String batchSerialNumber, String movementDocumentNo) throws IOException, CsvException {
		/*
		 * LANG_ID, C_ID, PLANT_ID, WH_ID, MVT_TYP_ID, SUB_MVT_TYP_ID, PACK_BARCODE, ITM_CODE, STR_NO, MVT_DOC_NO
		 */
		Optional<InventoryMovement> inventoryMovement = 
				inventoryMovementRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndMovementTypeAndSubmovementTypeAndPackBarcodesAndItemCodeAndBatchSerialNumberAndMovementDocumentNoAndDeletionIndicator(
						getLanguageId(),
						getCompanyCode(),
						getPlantId(),
						warehouseId, 
						movementType, 
						submovementType, 
						packBarcodes, 
						itemCode, 
						batchSerialNumber, 
						movementDocumentNo,
						0L
						);
		if (inventoryMovement.isEmpty()) {
			// Error Log
			createInventoryMovementLog1(warehouseId, movementType, submovementType, packBarcodes, itemCode, batchSerialNumber,
					movementDocumentNo, "InventoryMovement with given values and movementType - " + movementType + " doesn't exists.");
			throw new BadRequestException("The given InventoryMovement ID : " +
										", warehouseId: " + warehouseId + 
										", movementType: " + movementType + 
										", submovementType: " + submovementType + 
										", packBarcodes: " + packBarcodes + 
										", itemCode: " + itemCode + 
										", batchSerialNumber: " + batchSerialNumber + 
										", movementDocumentNo: " + movementDocumentNo + 
										" doesn't exist.");
		} 
		return inventoryMovement.get();
	}

	/**
	 * @param searchInventoryMovement
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public Stream<InventoryMovement> findInventoryMovement(SearchInventoryMovement searchInventoryMovement)
			throws ParseException, java.text.ParseException {
		if (searchInventoryMovement.getFromCreatedOn() != null && searchInventoryMovement.getToCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchInventoryMovement.getFromCreatedOn(), searchInventoryMovement.getToCreatedOn());
			searchInventoryMovement.setFromCreatedOn(dates[0]);
			searchInventoryMovement.setToCreatedOn(dates[1]);
		}
		InventoryMovementSpecification spec = new InventoryMovementSpecification(searchInventoryMovement);
		Stream<InventoryMovement> results = inventoryMovementRepository.stream(spec, InventoryMovement.class);

//		List<InventoryMovement> inventoryMovementList = new ArrayList<>();
//		for (InventoryMovement dbInventoryMovement : results) {
//			IKeyValuePair iKeyValuePair = inventoryMovementRepository.getDescription(
//					dbInventoryMovement.getLanguageId(), dbInventoryMovement.getCompanyCodeId(),
//					dbInventoryMovement.getPlantId(), dbInventoryMovement.getWarehouseId());
//			if (iKeyValuePair != null) {
//				dbInventoryMovement.setCompanyDescription(iKeyValuePair.getCompanyDesc());
//				dbInventoryMovement.setPlantDescription(iKeyValuePair.getPlantDesc());
//				dbInventoryMovement.setWarehouseDescription(iKeyValuePair.getWarehouseDesc());
//			}
//			inventoryMovementList.add(dbInventoryMovement);
//		}
		return results;
	}



	/**
	 * 		
	 * @param newInventoryMovement
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InventoryMovement createInventoryMovement (AddInventoryMovement newInventoryMovement, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, IOException, CsvException {
		try {
		InventoryMovement dbInventoryMovement = new InventoryMovement();
		log.info("newInventoryMovement : " + newInventoryMovement);
		BeanUtils.copyProperties(newInventoryMovement, dbInventoryMovement, CommonUtils.getNullPropertyNames(newInventoryMovement));
		dbInventoryMovement.setDeletionIndicator(0L);
		dbInventoryMovement.setCreatedBy(loginUserID);
		dbInventoryMovement.setCreatedOn(new Date());
		return inventoryMovementRepository.save(dbInventoryMovement);
		} catch (BeansException e) {
			// Error Log
			createInventoryMovementLog(newInventoryMovement, e.toString());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param warehouseId
	 * @param movementType
	 * @param submovementType
	 * @param palletCode
	 * @param caseCode
	 * @param packBarcodes
	 * @param itemCode
	 * @param variantCode
	 * @param variantSubCode
	 * @param batchSerialNumber
	 * @param movementDocumentNo
	 * @param loginUserID
	 * @param updateInventoryMovement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InventoryMovement updateInventoryMovement (String warehouseId, Long movementType, Long submovementType, String packBarcodes, 
			String itemCode, String batchSerialNumber, String movementDocumentNo, UpdateInventoryMovement updateInventoryMovement) 
			throws IllegalAccessException, InvocationTargetException, IOException, CsvException {
		InventoryMovement dbInventoryMovement = 
				getInventoryMovement(warehouseId, movementType, submovementType, packBarcodes, itemCode, batchSerialNumber, movementDocumentNo);
		BeanUtils.copyProperties(updateInventoryMovement, dbInventoryMovement, CommonUtils.getNullPropertyNames(updateInventoryMovement));
		return inventoryMovementRepository.save(dbInventoryMovement);
	}

	/**
	 * deleteInventoryMovement
	 * @param loginUserID
	 * @param movementType
	 */
	public void deleteInventoryMovement (String warehouseId, Long movementType, Long submovementType, String packBarcodes,
										 String itemCode, String batchSerialNumber, String movementDocumentNo, String loginUserID) throws IOException, CsvException {
		InventoryMovement inventoryMovement =
				getInventoryMovement(warehouseId, movementType, submovementType, packBarcodes, itemCode, batchSerialNumber, movementDocumentNo);
		if ( inventoryMovement != null) {
			inventoryMovement.setDeletionIndicator(1L);
			inventoryMovementRepository.save(inventoryMovement);
		} else {
			// Error Log
			createInventoryMovementLog1(warehouseId, movementType, submovementType, packBarcodes, itemCode, batchSerialNumber,
					movementDocumentNo, "Error in deleting InventoryMovement Id - " + movementType);
			throw new EntityNotFoundException("Error in deleting Id: " + movementType);
		}
	}

	/**
	 *
	 * @param warehouseId
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param refDocNumber
	 * @param loginUserID
	 * @return
	 */
	// Delete InventoryMovement
	public List<InventoryMovement> deleteInventoryMovement (String warehouseId, String companyCodeId, String plantId,
															String languageId, String refDocNumber, String loginUserID) {
		List<InventoryMovement> inventoryMovements = new ArrayList<>();
		List<InventoryMovement> inventoryMovementList = inventoryMovementRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
				companyCodeId, plantId, languageId,warehouseId, refDocNumber, 0L);
		log.info("InventoryMovement - cancellation : " + inventoryMovementList);
		if ( inventoryMovementList != null && !inventoryMovementList.isEmpty()) {
			for (InventoryMovement inventoryMovement : inventoryMovementList) {
				inventoryMovement.setDeletionIndicator(1L);
				inventoryMovementRepository.save(inventoryMovement);
				inventoryMovements.add(inventoryMovement);
			}
		}
		return inventoryMovements;
	}

	//=======================================InventoryMovement_ErrorLog================================================
	private void createInventoryMovementLog(AddInventoryMovement newInventoryMovement, String error) throws IOException, CsvException {

		List<ErrorLog> errorLogList = new ArrayList<>();
		ErrorLog errorLog = new ErrorLog();
		errorLog.setOrderTypeId(String.valueOf(newInventoryMovement.getMovementType()));
		errorLog.setOrderDate(new Date());
		errorLog.setLanguageId(newInventoryMovement.getLanguageId());
		errorLog.setCompanyCodeId(newInventoryMovement.getCompanyCodeId());
		errorLog.setPlantId(newInventoryMovement.getPlantId());
		errorLog.setWarehouseId(newInventoryMovement.getWarehouseId());
		errorLog.setRefDocNumber(newInventoryMovement.getRefDocNumber());
		errorLog.setItemCode(newInventoryMovement.getItemCode());
		errorLog.setReferenceField1(String.valueOf(newInventoryMovement.getSubmovementType()));
		errorLog.setReferenceField2(newInventoryMovement.getMovementDocumentNo());
		errorLog.setReferenceField3(newInventoryMovement.getStorageBin());
		errorLog.setErrorMessage(error);
		errorLog.setCreatedBy("MSD_API");
		errorLog.setCreatedOn(new Date());
//		errorLogRepository.save(errorLog);
		errorLogList.add(errorLog);
		errorLogService.writeLog(errorLogList);
	}

	private void createInventoryMovementLog1(String warehouseId, Long movementType, Long submovementType, String packBarcodes,
											 String itemCode, String batchSerialNumber, String movementDocumentNo, String error) throws IOException, CsvException {

		List<ErrorLog> errorLogList = new ArrayList<>();
		ErrorLog errorLog = new ErrorLog();
		errorLog.setOrderTypeId(String.valueOf(movementType));
		errorLog.setOrderDate(new Date());
		errorLog.setLanguageId(getLanguageId());
		errorLog.setCompanyCodeId(getCompanyCode());
		errorLog.setPlantId(getPlantId());
		errorLog.setWarehouseId(warehouseId);
		errorLog.setItemCode(itemCode);
		errorLog.setReferenceField1(String.valueOf(submovementType));
		errorLog.setReferenceField2(packBarcodes);
		errorLog.setReferenceField3(batchSerialNumber);
		errorLog.setReferenceField4(movementDocumentNo);
		errorLog.setErrorMessage(error);
		errorLog.setCreatedBy("MSD_API");
		errorLog.setCreatedOn(new Date());
//		errorLogRepository.save(errorLog);
		errorLogList.add(errorLog);
		errorLogService.writeLog(errorLogList);
	}

}
