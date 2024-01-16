package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.dto.StorageBinV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.SearchPutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PutAwayLineV2Specification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.IImbasicData1;
import com.tekclover.wms.api.transaction.model.dto.StorageBin;
import com.tekclover.wms.api.transaction.model.dto.Warehouse;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.putaway.AddPutAwayLine;
import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayHeader;
import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayLine;
import com.tekclover.wms.api.transaction.model.inbound.putaway.SearchPutAwayLine;
import com.tekclover.wms.api.transaction.model.inbound.putaway.UpdatePutAwayLine;
import com.tekclover.wms.api.transaction.repository.specification.PutAwayLineSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PutAwayLineService extends BaseService {
	
	@Autowired
	private PutAwayHeaderRepository putAwayHeaderRepository;
	
	@Autowired
	private PutAwayLineRepository putAwayLineRepository;
	
	@Autowired
	private PutAwayHeaderService putAwayHeaderService;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private InboundLineRepository inboundLineRepository;
	
	@Autowired
	private InventoryMovementRepository inventoryMovementRepository;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private MastersService mastersService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private InboundLineService inboundLineService;

	@Autowired
	private ImBasicData1Repository imbasicdata1Repository;
	//--------------------------------------------------------------------------------------------
	@Autowired
	private InboundLineV2Repository inboundLineV2Repository;

	@Autowired
	private InventoryV2Repository inventoryV2Repository;

	@Autowired
	private PutAwayHeaderV2Repository putAwayHeaderV2Repository;

	@Autowired
	private PutAwayLineV2Repository putAwayLineV2Repository;

	@Autowired
	private StagingLineV2Repository stagingLineV2Repository;

	String statusDescription = null;

	//--------------------------------------------------------------------------------------------

	/**
	 * getPutAwayLines
	 * @return
	 */
	public List<PutAwayLine> getPutAwayLines () {
		List<PutAwayLine> putAwayLineList =  putAwayLineRepository.findAll();
		putAwayLineList = putAwayLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return putAwayLineList;
	}
	
	/**
	 * WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE
	 * @param warehouseId
	 * @param preInboundNo
	 * @param refDocNumber
	 * @param lineNo
	 * @param itemCode
	 * @return
	 */
	public List<PutAwayLine> getPutAwayLine (String warehouseId, String preInboundNo, String refDocNumber, Long lineNo, String itemCode) {
		List<PutAwayLine> putAwayLine = 
				putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
						getLanguageId(),
						getCompanyCode(),
						getPlantId(),
						warehouseId, 
						preInboundNo, 
						refDocNumber, 
						lineNo, 
						itemCode, 
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values in PutAwayLine: warehouseId:" + warehouseId + 
					",preInboundNo: " + preInboundNo + 
					",lineNo: " + lineNo + 
					",itemCode: " + itemCode + 
					",lineNo: " + lineNo + 
					" doesn't exist.");
		} 
		
		return putAwayLine;
	}
	
	/**
	 * getPutAwayLineByStatusId
	 * @param warehouseId
	 * @param preInboundNo
	 * @param refDocNumber
	 * @return
	 */
	public long getPutAwayLineByStatusId (String warehouseId, String preInboundNo, String refDocNumber) {
		long putAwayLineStatusIdCount = putAwayLineRepository.getPutawayLineCountByStatusId(getCompanyCode(), getPlantId(), warehouseId, preInboundNo, refDocNumber);
		return putAwayLineStatusIdCount;
	}
	
	/**
	 * 
	 * @param warehouseId
	 * @param putAwayNumber
	 * @param refDocNumber
	 * @param statusId
	 * @return
	 */
	public long getPutAwayLineByStatusId (String warehouseId, String putAwayNumber, String refDocNumber, Long statusId) {
		long putAwayLineStatusIdCount = 
				putAwayLineRepository.getPutawayLineCountByStatusId(getCompanyCode(), getPlantId(), warehouseId, putAwayNumber, refDocNumber, statusId);
		return putAwayLineStatusIdCount;
	}
	
	/**
	 * 
	 * @param warehouseId
	 * @param refDocNumber
	 * @param putAwayNumber
	 * @return
	 */
	public List<PutAwayLine> getPutAwayLine (String warehouseId, String refDocNumber, String putAwayNumber) {
		List<PutAwayLine> putAwayLine = 
				putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPutAwayNumberAndDeletionIndicator(
						getLanguageId(),
						getCompanyCode(),
						getPlantId(),
						warehouseId, 
						refDocNumber, 
						putAwayNumber, 
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: warehouseId:" + warehouseId + 
					",refDocNumber: " + refDocNumber + 
					",putAwayNumber: " + putAwayNumber + 
					" doesn't exist.");
		} 
		
		return putAwayLine;
	}
	
	/**
	 * getPutAwayLine
	 * @param confirmedStorageBin
	 * @return
	 */
	public PutAwayLine getPutAwayLine (String warehouseId, String goodsReceiptNo, String preInboundNo, String refDocNumber, 
			String putAwayNumber, Long lineNo, String itemCode, String proposedStorageBin, List<String> confirmedStorageBin) {
		Optional<PutAwayLine> putAwayLine = 
				putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndPreInboundNoAndRefDocNumberAndPutAwayNumberAndLineNoAndItemCodeAndProposedStorageBinAndConfirmedStorageBinInAndDeletionIndicator(
						getLanguageId(),
						getCompanyCode(),
						getPlantId(),
						warehouseId, 
						goodsReceiptNo,
						preInboundNo, 
						refDocNumber, 
						putAwayNumber, 
						lineNo, 
						itemCode, 
						proposedStorageBin, 
						confirmedStorageBin,
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: warehouseId:" + warehouseId + 
					",goodsReceiptNo: " + goodsReceiptNo + "," +
					",preInboundNo: " + preInboundNo + "," +
					",refDocNumber: " + refDocNumber + "," +
					",putAwayNumber: " + putAwayNumber + "," +
					",putAwayNumber: " + putAwayNumber + "," +
					",lineNo: " + lineNo + "," +
					",itemCode: " + itemCode + "," +
					",lineNo: " + lineNo + "," +
					",proposedStorageBin: " + proposedStorageBin + 
					",confirmedStorageBin: " + confirmedStorageBin + 
					" doesn't exist.");
		} 
		
		return putAwayLine.get();
	}
	
	/**
	 * 
	 * @param refDocNumber
	 * @param packBarcodes
	 * @return
	 */
	public List<PutAwayLine> getPutAwayLine (String refDocNumber, String packBarcodes) {
		List<PutAwayLine> putAwayLine = 
				putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator (
					getLanguageId(),
					getCompanyCode(),
					getPlantId(),
					refDocNumber, 
					packBarcodes,
					0L
					);
			if (putAwayLine.isEmpty()) {
				throw new BadRequestException("The given values: " + 
						",refDocNumber: " + refDocNumber + "," +
						",packBarcodes: " + packBarcodes + "," +
						" doesn't exist.");
			} 
		return putAwayLine;
	}
	
	/**
	 * 
	 * @param refDocNumber
	 * @return
	 */
	public List<PutAwayLine> getPutAwayLine(String refDocNumber) {
		List<PutAwayLine> putAwayLine = 
				putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndDeletionIndicator(
						getLanguageId(),
						getCompanyCode(),
						getPlantId(),
						refDocNumber, 
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: " + 
					"refDocNumber: " + refDocNumber + 
					" doesn't exist.");
		} 
		return putAwayLine;
	}
	
	/**
	 * 
	 * @param searchPutAwayLine
	 * @return
	 * @throws Exception
	 */
	public List<PutAwayLine> findPutAwayLine(SearchPutAwayLine searchPutAwayLine) throws Exception {

		if (searchPutAwayLine.getFromConfirmedDate() != null && searchPutAwayLine.getToConfirmedDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPutAwayLine.getFromConfirmedDate(), searchPutAwayLine.getToConfirmedDate());
			searchPutAwayLine.setFromConfirmedDate(dates[0]);
			searchPutAwayLine.setToConfirmedDate(dates[1]);
		}

		PutAwayLineSpecification spec = new PutAwayLineSpecification(searchPutAwayLine);
		List<PutAwayLine> results = putAwayLineRepository.findAll(spec);
		return results;
	}
	
	/**
	 * createPutAwayLine
	 * @param newPutAwayLine
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PutAwayLine createPutAwayLine (AddPutAwayLine newPutAwayLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLine dbPutAwayLine = new PutAwayLine();
		log.info("newPutAwayLine : " + newPutAwayLine);
		BeanUtils.copyProperties(newPutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(newPutAwayLine));
		dbPutAwayLine.setDeletionIndicator(0L);
		dbPutAwayLine.setCreatedBy(loginUserID);
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setCreatedOn(new Date());
		dbPutAwayLine.setUpdatedOn(new Date());
		return putAwayLineRepository.save(dbPutAwayLine);
	}
	
	/**
	 * 
	 * @param newPutAwayLines
	 * @param loginUserID
	 * @param loginUserID
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<PutAwayLine> putAwayLineConfirm(@Valid List<AddPutAwayLine> newPutAwayLines, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<PutAwayLine> createdPutAwayLines = new ArrayList<>();
		log.info("newPutAwayLines to confirm : " + newPutAwayLines);
		try {
			for (AddPutAwayLine newPutAwayLine : newPutAwayLines) {
				PutAwayLine dbPutAwayLine = new PutAwayLine();
				Warehouse warehouse = getWarehouse(newPutAwayLine.getWarehouseId());
				
				BeanUtils.copyProperties(newPutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(newPutAwayLine));
				if (newPutAwayLine.getCompanyCode() == null) {
					dbPutAwayLine.setCompanyCode(warehouse.getCompanyCode());
				} else {
					dbPutAwayLine.setCompanyCode(newPutAwayLine.getCompanyCode());
				}
				dbPutAwayLine.setPutawayConfirmedQty(newPutAwayLine.getPutawayConfirmedQty());
				dbPutAwayLine.setConfirmedStorageBin(newPutAwayLine.getConfirmedStorageBin());
				dbPutAwayLine.setStatusId(20L);
				dbPutAwayLine.setDeletionIndicator(0L);
				dbPutAwayLine.setCreatedBy(loginUserID);
				dbPutAwayLine.setUpdatedBy(loginUserID);
				dbPutAwayLine.setCreatedOn(new Date());
				dbPutAwayLine.setUpdatedOn(new Date());
				
				Optional<PutAwayLine> existingPutAwayLine = putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndPreInboundNoAndRefDocNumberAndPutAwayNumberAndLineNoAndItemCodeAndProposedStorageBinAndConfirmedStorageBinInAndDeletionIndicator(
						getLanguageId(), getCompanyCode(), getPlantId(), newPutAwayLine.getWarehouseId(), newPutAwayLine.getGoodsReceiptNo(), 
						newPutAwayLine.getPreInboundNo(), newPutAwayLine.getRefDocNumber(), newPutAwayLine.getPutAwayNumber(), newPutAwayLine.getLineNo(), 
						newPutAwayLine.getItemCode(), newPutAwayLine.getProposedStorageBin(), Arrays.asList(newPutAwayLine.getConfirmedStorageBin()), 
						newPutAwayLine.getDeletionIndicator());
				log.info("Existing putawayline already created : " + existingPutAwayLine);
				if(existingPutAwayLine.isEmpty()) {
					PutAwayLine createdPutAwayLine = putAwayLineRepository.save(dbPutAwayLine);
					log.info("---------->createdPutAwayLine created: " + createdPutAwayLine);
					createdPutAwayLines.add(createdPutAwayLine);
					boolean isInventoryCreated = false;
					boolean isInventoryMovemoentCreated = false;
					if (createdPutAwayLine != null && createdPutAwayLine.getPutawayConfirmedQty() > 0L ) {
						// Insert a record into INVENTORY table as below
						Inventory inventory = new Inventory();
						BeanUtils.copyProperties(createdPutAwayLine, inventory, CommonUtils.getNullPropertyNames(createdPutAwayLine));
						inventory.setCompanyCodeId(createdPutAwayLine.getCompanyCode());
						inventory.setVariantCode(1L); 				// VAR_ID
						inventory.setVariantSubCode("1"); 			// VAR_SUB_ID
						inventory.setStorageMethod("1"); 			// STR_MTD
						inventory.setBatchSerialNumber("1"); 		// STR_NO 
						inventory.setBatchSerialNumber(newPutAwayLine.getBatchSerialNumber()); 
						inventory.setStorageBin(createdPutAwayLine.getConfirmedStorageBin());

						AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
						StorageBin dbStorageBin = 
								mastersService.getStorageBin(dbPutAwayLine.getConfirmedStorageBin(), 
										dbPutAwayLine.getWarehouseId(),
										authTokenForMastersService.getAccess_token());
						inventory.setBinClassId(dbStorageBin.getBinClassId());

						List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(inventory.getItemCode());
						if(imbasicdata1 != null && !imbasicdata1.isEmpty()){
							inventory.setReferenceField8(imbasicdata1.get(0).getDescription());
							inventory.setReferenceField9(imbasicdata1.get(0).getManufacturePart());
						}
						
						if(dbStorageBin != null){
							inventory.setReferenceField10(dbStorageBin.getStorageSectionId());
							inventory.setReferenceField5(dbStorageBin.getAisleNumber());
							inventory.setReferenceField6(dbStorageBin.getShelfId());
							inventory.setReferenceField7(dbStorageBin.getRowId());
						}

						/*
						 * Insert PA_CNF_QTY value in this field.
						 * Also Pass WH_ID/PACK_BARCODE/ITM_CODE/BIN_CL_ID=3 in INVENTORY table and fetch ST_BIN/INV_QTY value.
						 * Update INV_QTY value by (INV_QTY - PA_CNF_QTY) . If this value becomes Zero, then delete the record"
						 */
						try {
							Inventory existinginventory = inventoryService.getInventory(createdPutAwayLine.getWarehouseId(),
									createdPutAwayLine.getPackBarcodes(), dbPutAwayLine.getItemCode(), 3L);
							double INV_QTY = existinginventory.getInventoryQuantity() - createdPutAwayLine.getPutawayConfirmedQty();
							log.info("INV_QTY : " + INV_QTY);
							if (INV_QTY >= 0) {
								existinginventory.setInventoryQuantity(INV_QTY);
								Inventory updatedInventory = inventoryRepository.save(existinginventory);
								log.info("updatedInventory--------> : " + updatedInventory);
							}
						} catch (Exception e) {
							log.info("Existing Inventory---Error-----> : " + e.toString());
						}

						// INV_QTY
						inventory.setInventoryQuantity(createdPutAwayLine.getPutawayConfirmedQty());

						// INV_UOM
						inventory.setInventoryUom(createdPutAwayLine.getPutAwayUom());
						inventory.setCreatedBy(createdPutAwayLine.getCreatedBy());
						inventory.setCreatedOn(createdPutAwayLine.getCreatedOn());
						Inventory createdInventory = inventoryRepository.save(inventory);
						log.info("createdInventory : " + createdInventory);
						if (createdInventory != null) {
							isInventoryCreated = true;
						}
						
						/* Insert a record into INVENTORYMOVEMENT table */
						InventoryMovement createdInventoryMovement = createInventoryMovement (createdPutAwayLine);
						log.info("inventoryMovement created: " + createdInventoryMovement);
						if (createdInventoryMovement != null) {
							isInventoryMovemoentCreated = true;
						}
						
						// Updating StorageBin StatusId as '1'
						dbStorageBin.setStatusId(1L);
						mastersService.updateStorageBin(dbPutAwayLine.getConfirmedStorageBin(), dbStorageBin, loginUserID, authTokenForMastersService.getAccess_token());
						
						if (isInventoryCreated && isInventoryMovemoentCreated) {
							List<PutAwayHeader> headers = putAwayHeaderService.getPutAwayHeader(createdPutAwayLine.getWarehouseId(), 
									createdPutAwayLine.getPreInboundNo(), createdPutAwayLine.getRefDocNumber(), createdPutAwayLine.getPutAwayNumber());
							for (PutAwayHeader putAwayHeader : headers) {
								putAwayHeader.setStatusId(20L);
								putAwayHeader = putAwayHeaderRepository.save(putAwayHeader);
								log.info("putAwayHeader updated: " + putAwayHeader);
							}
						
							/*--------------------- INBOUNDTABLE Updates ------------------------------------------*/
							// Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE values in PUTAWAYLINE table and 
							// fetch PA_CNF_QTY values and QTY_TYPE values and updated STATUS_ID as 20
							updateInboundLine (createdPutAwayLine);
//							double addedAcceptQty = 0.0;
//							double addedDamageQty = 0.0;
//							
//							InboundLine inboundLine = inboundLineService.getInboundLine(createdPutAwayLine.getWarehouseId(), 
//									createdPutAwayLine.getRefDocNumber(), createdPutAwayLine.getPreInboundNo(), createdPutAwayLine.getLineNo(), 
//									createdPutAwayLine.getItemCode());
//							log.info("inboundLine----from--DB---------> " + inboundLine);
//							
//							// If QTY_TYPE = A, add PA_CNF_QTY with existing value in ACCEPT_QTY field
//							if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("A")) {
//								if (inboundLine.getAcceptedQty() != null) {
//									addedAcceptQty = inboundLine.getAcceptedQty() + createdPutAwayLine.getPutawayConfirmedQty();
//								} else {
//									addedAcceptQty = createdPutAwayLine.getPutawayConfirmedQty();
//								}
//								
//								inboundLine.setAcceptedQty(addedAcceptQty);
//							}
//							
//							// if QTY_TYPE = D, add PA_CNF_QTY with existing value in DAMAGE_QTY field
//							if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("D")) {
//								if (inboundLine.getDamageQty() != null) {
//									addedDamageQty = inboundLine.getDamageQty() + createdPutAwayLine.getPutawayConfirmedQty();
//								} else {
//									addedDamageQty = createdPutAwayLine.getPutawayConfirmedQty();
//								}
//								
//								inboundLine.setDamageQty(addedDamageQty);
//							}
//							
//							inboundLine.setStatusId(20L);
//							inboundLine = inboundLineRepository.save(inboundLine);
//							log.info("inboundLine updated : " + inboundLine);
						}
					}
				} else {
					log.info("Putaway Line already exist : " + existingPutAwayLine);
				}
			}
			return createdPutAwayLines;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param createdPutAwayLine
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	private void updateInboundLine (PutAwayLine createdPutAwayLine) {
		double addedAcceptQty = 0.0;
		double addedDamageQty = 0.0;
		
		InboundLine inboundLine = inboundLineService.getInboundLine(createdPutAwayLine.getWarehouseId(), 
				createdPutAwayLine.getRefDocNumber(), createdPutAwayLine.getPreInboundNo(), createdPutAwayLine.getLineNo(), 
				createdPutAwayLine.getItemCode());
		log.info("inboundLine----from--DB---------> " + inboundLine);
		
		// If QTY_TYPE = A, add PA_CNF_QTY with existing value in ACCEPT_QTY field
		if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("A")) {
			if (inboundLine.getAcceptedQty() != null) {
				addedAcceptQty = inboundLine.getAcceptedQty() + createdPutAwayLine.getPutawayConfirmedQty();
			} else {
				addedAcceptQty = createdPutAwayLine.getPutawayConfirmedQty();
			}
			
			inboundLine.setAcceptedQty(addedAcceptQty);
		}
		
		// if QTY_TYPE = D, add PA_CNF_QTY with existing value in DAMAGE_QTY field
		if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("D")) {
			if (inboundLine.getDamageQty() != null) {
				addedDamageQty = inboundLine.getDamageQty() + createdPutAwayLine.getPutawayConfirmedQty();
			} else {
				addedDamageQty = createdPutAwayLine.getPutawayConfirmedQty();
			}
			
			inboundLine.setDamageQty(addedDamageQty);
		}
		
		inboundLine.setStatusId(20L);
		inboundLine = inboundLineRepository.save(inboundLine);
		log.info("inboundLine updated : " + inboundLine);
	}
	
	/**
	 * 
	 * @param dbPutAwayLine
	 * @return 
	 */
	private InventoryMovement createInventoryMovement(PutAwayLine dbPutAwayLine) {
		InventoryMovement inventoryMovement = new InventoryMovement();
		BeanUtils.copyProperties(dbPutAwayLine, inventoryMovement, CommonUtils.getNullPropertyNames(dbPutAwayLine));
		inventoryMovement.setCompanyCodeId(dbPutAwayLine.getCompanyCode());
		
		// MVT_TYP_ID
		inventoryMovement.setMovementType(1L);
		
		// SUB_MVT_TYP_ID
		inventoryMovement.setSubmovementType(2L);
		
		// VAR_ID
		inventoryMovement.setVariantCode(1L);
		
		// VAR_SUB_ID
		inventoryMovement.setVariantSubCode("1");
		
		// STR_MTD
		inventoryMovement.setStorageMethod("1");
		
		// STR_NO
		inventoryMovement.setBatchSerialNumber("1");
		
		// CASE_CODE
		inventoryMovement.setCaseCode("999999");
		
		// PAL_CODE
		inventoryMovement.setPalletCode("999999");
		
		// MVT_DOC_NO
		inventoryMovement.setMovementDocumentNo(dbPutAwayLine.getRefDocNumber());
		
		// ST_BIN
		inventoryMovement.setStorageBin(dbPutAwayLine.getConfirmedStorageBin());
		
		// MVT_QTY
		inventoryMovement.setMovementQty(dbPutAwayLine.getPutawayConfirmedQty());
		
		// MVT_QTY_VAL
		inventoryMovement.setMovementQtyValue("P");
		
		// MVT_UOM
		inventoryMovement.setInventoryUom(dbPutAwayLine.getPutAwayUom());
		
		/*
		 * -----THE BELOW IS NOT USED-------------
		 * Pass WH_ID/ITM_CODE/PACK_BARCODE/BIN_CL_ID is equal to 1 in INVENTORY table and fetch INV_QTY
		 * BAL_OH_QTY = INV_QTY
		 */
		// PASS WH_ID/ITM_CODE/BIN_CL_ID and sum the INV_QTY for all selected inventory
		List<Inventory> inventoryList = 
				inventoryService.getInventory (dbPutAwayLine.getWarehouseId(), dbPutAwayLine.getItemCode(), 1L);
		double sumOfInvQty = inventoryList.stream().mapToDouble(a->a.getInventoryQuantity()).sum();
		inventoryMovement.setBalanceOHQty(sumOfInvQty);
		
		// IM_CTD_BY
		inventoryMovement.setCreatedBy(dbPutAwayLine.getCreatedBy());
		
		// IM_CTD_ON
		inventoryMovement.setCreatedOn(dbPutAwayLine.getCreatedOn());
		inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
		return inventoryMovement;
	}
	
	/**
	 * updatePutAwayLine
	 * @param loginUserID
	 * @param confirmedStorageBin
	 * @param updatePutAwayLine
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PutAwayLine updatePutAwayLine (String warehouseId, String goodsReceiptNo, String preInboundNo, String refDocNumber, 
			String putAwayNumber, Long lineNo, String itemCode, String proposedStorageBin, String confirmedStorageBin,
			String loginUserID, UpdatePutAwayLine updatePutAwayLine) 
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLine dbPutAwayLine = getPutAwayLine(warehouseId, goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber, 
				lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
		BeanUtils.copyProperties(updatePutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(updatePutAwayLine));
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setUpdatedOn(new Date());
		return putAwayLineRepository.save(dbPutAwayLine);
	}
	
	/**
	 * 
	 * @param updatePutAwayLine
	 * @param loginUserID
	 * @return
	 */
	public PutAwayLine updatePutAwayLine ( UpdatePutAwayLine updatePutAwayLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLine dbPutAwayLine = new PutAwayLine();
		BeanUtils.copyProperties(updatePutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(updatePutAwayLine));
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setUpdatedOn(new Date());
		return putAwayLineRepository.save(dbPutAwayLine);
	}
	
	/**
	 * 
	 * @param asnNumber
	 */
	public void updateASN (String asnNumber) {
		List<PutAwayLine> putAwayLines = getPutAwayLines();
		putAwayLines.forEach(p -> p.setReferenceField1(asnNumber));
		putAwayLineRepository.saveAll(putAwayLines);
	}
	
	/**
	 * deletePutAwayLine
	 * @param loginUserID 
	 * @param confirmedStorageBin
	 */
	public void deletePutAwayLine (String languageId, String companyCodeId, String plantId, String warehouseId, 
			String goodsReceiptNo, String preInboundNo, String refDocNumber, String putAwayNumber, Long lineNo, 
			String itemCode, String proposedStorageBin, String confirmedStorageBin, String loginUserID) {
		PutAwayLine putAwayLine = getPutAwayLine(warehouseId, goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber, 
				lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
		if ( putAwayLine != null) {
			putAwayLine.setDeletionIndicator(1L);
			putAwayLine.setUpdatedBy(loginUserID);
			putAwayLineRepository.save(putAwayLine);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + confirmedStorageBin);
		}
	}

	//=====================================================================V2====================================================
	/**
	 * @param companyId
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param preInboundNo
	 * @param refDocNumber
	 * @return
	 */
	public long getPutAwayLineByStatusIdV2(String companyId, String plantId, String languageId, String warehouseId,
										   String preInboundNo, String refDocNumber) {
		long putAwayLineStatusIdCount = putAwayLineV2Repository.getPutawayLineCountByStatusId(companyId, plantId, warehouseId, languageId, preInboundNo, refDocNumber);
		return putAwayLineStatusIdCount;
	}

	/**
	 * @param companyCode
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param putAwayNumber
	 * @param refDocNumber
	 * @param statusId
	 * @return
	 */
	public long getPutAwayLineByStatusIdV2(String companyCode, String plantId, String languageId,
										   String warehouseId, String putAwayNumber, String refDocNumber, Long statusId) {
		long putAwayLineStatusIdCount =
				putAwayLineV2Repository.getPutawayLineCountByStatusId(companyCode, plantId, warehouseId, languageId, putAwayNumber, refDocNumber, statusId);
		return putAwayLineStatusIdCount;
	}

	/**
	 * @param companyCode
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param preInboundNo
	 * @param refDocNumber
	 * @param lineNo
	 * @param itemCode
	 * @return
	 */

	public List<PutAwayLineV2> getPutAwayLineV2(String companyCode, String plantId, String languageId,
												String warehouseId, String preInboundNo, String refDocNumber,
												Long lineNo, String itemCode) {
		List<PutAwayLineV2> putAwayLine =
				putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
						languageId,
						companyCode,
						plantId,
						warehouseId,
						preInboundNo,
						refDocNumber,
						lineNo,
						itemCode,
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values in PutAwayLine: warehouseId:" + warehouseId +
					",preInboundNo: " + preInboundNo +
					",lineNo: " + lineNo +
					",itemCode: " + itemCode +
					",lineNo: " + lineNo +
					" doesn't exist.");
		}

		return putAwayLine;
	}

	public PutAwayLineV2 getPutAwayLineV2(String companyCode, String plantId, String languageId,
										  String warehouseId, String goodsReceiptNo, String preInboundNo,
										  String refDocNumber, String putAwayNumber, Long lineNo,
										  String itemCode, String proposedStorageBin, List<String> confirmedStorageBin) {
		Optional<PutAwayLineV2> putAwayLine =
				putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndPreInboundNoAndRefDocNumberAndPutAwayNumberAndLineNoAndItemCodeAndProposedStorageBinAndConfirmedStorageBinInAndDeletionIndicator(
						languageId,
						companyCode,
						plantId,
						warehouseId,
						goodsReceiptNo,
						preInboundNo,
						refDocNumber,
						putAwayNumber,
						lineNo,
						itemCode,
						proposedStorageBin,
						confirmedStorageBin,
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: warehouseId:" + warehouseId +
					",goodsReceiptNo: " + goodsReceiptNo + "," +
					",preInboundNo: " + preInboundNo + "," +
					",refDocNumber: " + refDocNumber + "," +
					",putAwayNumber: " + putAwayNumber + "," +
					",putAwayNumber: " + putAwayNumber + "," +
					",lineNo: " + lineNo + "," +
					",itemCode: " + itemCode + "," +
					",lineNo: " + lineNo + "," +
					",proposedStorageBin: " + proposedStorageBin +
					",confirmedStorageBin: " + confirmedStorageBin +
					" doesn't exist.");
		}

		return putAwayLine.get();
	}

	/**
	 * @param warehouseId
	 * @param refDocNumber
	 * @param putAwayNumber
	 * @return
	 */
	public List<PutAwayLineV2> getPutAwayLineV2(String companyCode, String plantId, String languageId,
												String warehouseId, String refDocNumber, String putAwayNumber) {
		List<PutAwayLineV2> putAwayLine =
				putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPutAwayNumberAndDeletionIndicator(
						languageId,
						companyCode,
						plantId,
						warehouseId,
						refDocNumber,
						putAwayNumber,
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: warehouseId:" + warehouseId +
					",refDocNumber: " + refDocNumber +
					",putAwayNumber: " + putAwayNumber +
					" doesn't exist.");
		}

		return putAwayLine;
	}

	/**
	 * @param companyCode
	 * @param plantId
	 * @param languageId
	 * @param refDocNumber
	 * @param packBarcodes
	 * @return
	 */
	public List<PutAwayLineV2> getPutAwayLineV2(String companyCode, String plantId, String languageId,
												String refDocNumber, String packBarcodes) {
		List<PutAwayLineV2> putAwayLine =
				putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
						languageId,
						companyCode,
						plantId,
						refDocNumber,
						packBarcodes,
						0L
				);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: " +
					",refDocNumber: " + refDocNumber + "," +
					",packBarcodes: " + packBarcodes + "," +
					" doesn't exist.");
		}
		return putAwayLine;
	}

	public List<PutAwayLineV2> getPutAwayLineV2(String companyCode, String plantId,
												String languageId, String refDocNumber) {
		List<PutAwayLineV2> putAwayLine =
				putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndDeletionIndicator(
						languageId,
						companyCode,
						plantId,
						refDocNumber,
						0L);
		if (putAwayLine.isEmpty()) {
			throw new BadRequestException("The given values: " +
					"refDocNumber: " + refDocNumber +
					" doesn't exist.");
		}
		return putAwayLine;
	}

	/**
	 * @param newPutAwayLines
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public List<PutAwayLineV2> putAwayLineConfirmV2(@Valid List<PutAwayLineV2> newPutAwayLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        List<PutAwayLineV2> createdPutAwayLines = new ArrayList<>();

        log.info("newPutAwayLines to confirm : " + newPutAwayLines);

        try {
            for (PutAwayLineV2 newPutAwayLine : newPutAwayLines) {
//                if (newPutAwayLine.getPutAwayQuantity() == null) {
//                    newPutAwayLine.setPutAwayQuantity(0D);
//                }
//                if (newPutAwayLine.getPutawayConfirmedQty() == null) {
//                    newPutAwayLine.setPutawayConfirmedQty(0D);
//                }
//                if (newPutAwayLine.getPutawayConfirmedQty() < newPutAwayLine.getPutAwayQuantity()) {
//                    List<PutAwayHeaderV2> dbPutAwayHeaderList = putAwayHeaderService.getPutAwayHeaderV2(newPutAwayLine.getWarehouseId(),
//                            newPutAwayLine.getPreInboundNo(),
//                            newPutAwayLine.getRefDocNumber(),
//                            newPutAwayLine.getPutAwayNumber(),
//                            newPutAwayLine.getCompanyCode(),
//                            newPutAwayLine.getPlantId(),
//                            newPutAwayLine.getLanguageId());
//                    if (dbPutAwayHeaderList != null) {
//                        for (PutAwayHeaderV2 newPutAwayHeader : dbPutAwayHeaderList) {
//                            if (!newPutAwayHeader.getApprovalStatus().equalsIgnoreCase("Approved")) {
//                                throw new BadRequestException("Binned Quantity is less than Received");
//                            }
//                        }
//                    }
//                }

                PutAwayLineV2 dbPutAwayLine = new PutAwayLineV2();
				PutAwayHeaderV2 dbPutAwayHeader = new PutAwayHeaderV2();

				//this code is set for mobile device to work
				if (newPutAwayLine.getCompanyCode() == null) {
					dbPutAwayHeader = putAwayHeaderService.getPutawayHeaderV2(newPutAwayLine.getPutAwayNumber());
					newPutAwayLine.setCompanyCode(dbPutAwayHeader.getCompanyCodeId());
				}
				if (newPutAwayLine.getBarcodeId() == null) {
					newPutAwayLine.setBarcodeId(dbPutAwayHeader.getBarcodeId());
				}
				if (newPutAwayLine.getManufacturerName() == null) {
					newPutAwayLine.setManufacturerName(dbPutAwayHeader.getManufacturerName());
				}

				//V2 Code
				IKeyValuePair description = stagingLineV2Repository.getDescription(newPutAwayLine.getCompanyCode(),
						newPutAwayLine.getLanguageId(),
						newPutAwayLine.getPlantId(),
						newPutAwayLine.getWarehouseId());

				newPutAwayLine.setCompanyDescription(description.getCompanyDesc());
				newPutAwayLine.setPlantDescription(description.getPlantDesc());
				newPutAwayLine.setWarehouseDescription(description.getWarehouseDesc());

				Warehouse warehouse = getWarehouse(newPutAwayLine.getWarehouseId(),
						newPutAwayLine.getCompanyCode(),
						newPutAwayLine.getPlantId(),
						newPutAwayLine.getLanguageId());

                BeanUtils.copyProperties(newPutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(newPutAwayLine));
                if (newPutAwayLine.getCompanyCode() == null) {
                    dbPutAwayLine.setCompanyCode(warehouse.getCompanyCode());
                } else {
                    dbPutAwayLine.setCompanyCode(newPutAwayLine.getCompanyCode());
                }
                dbPutAwayLine.setPutawayConfirmedQty(newPutAwayLine.getPutawayConfirmedQty());
                dbPutAwayLine.setConfirmedStorageBin(newPutAwayLine.getConfirmedStorageBin());
                dbPutAwayLine.setStatusId(20L);
                String statusDescription = stagingLineV2Repository.getStatusDescription(20L, newPutAwayLine.getLanguageId());
                dbPutAwayLine.setStatusDescription(statusDescription);
                dbPutAwayLine.setPackBarcodes(newPutAwayLine.getPackBarcodes());
				dbPutAwayLine.setBarcodeId(newPutAwayLine.getBarcodeId());
                dbPutAwayLine.setDeletionIndicator(0L);
                dbPutAwayLine.setCreatedBy(loginUserID);
                dbPutAwayLine.setUpdatedBy(loginUserID);
                dbPutAwayLine.setCreatedOn(new Date());
                dbPutAwayLine.setUpdatedOn(new Date());

                Optional<PutAwayLineV2> existingPutAwayLine = putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndPreInboundNoAndRefDocNumberAndPutAwayNumberAndLineNoAndItemCodeAndProposedStorageBinAndConfirmedStorageBinInAndDeletionIndicator(
                        newPutAwayLine.getLanguageId(), newPutAwayLine.getCompanyCode(), newPutAwayLine.getPlantId(),
                        newPutAwayLine.getWarehouseId(), newPutAwayLine.getGoodsReceiptNo(),
                        newPutAwayLine.getPreInboundNo(), newPutAwayLine.getRefDocNumber(),
                        newPutAwayLine.getPutAwayNumber(), newPutAwayLine.getLineNo(),
                        newPutAwayLine.getItemCode(), newPutAwayLine.getProposedStorageBin(),
                        Arrays.asList(newPutAwayLine.getConfirmedStorageBin()),
                        newPutAwayLine.getDeletionIndicator());

                log.info("Existing putawayline already created : " + existingPutAwayLine);

                if (existingPutAwayLine.isEmpty()) {

                    PutAwayLineV2 createdPutAwayLine = putAwayLineV2Repository.save(dbPutAwayLine);

                    log.info("---------->createdPutAwayLine created: " + createdPutAwayLine);

                    createdPutAwayLines.add(createdPutAwayLine);

                    boolean isInventoryCreated = false;
                    boolean isInventoryMovemoentCreated = false;

                    if (createdPutAwayLine != null && createdPutAwayLine.getPutawayConfirmedQty() > 0L) {

                        // Insert a record into INVENTORY table as below
                        InventoryV2 inventory = new InventoryV2();
                        BeanUtils.copyProperties(createdPutAwayLine, inventory, CommonUtils.getNullPropertyNames(createdPutAwayLine));
                        inventory.setCompanyCodeId(createdPutAwayLine.getCompanyCode());
                        inventory.setVariantCode(1L);                // VAR_ID
                        inventory.setVariantSubCode("1");            // VAR_SUB_ID
                        inventory.setStorageMethod("1");            // STR_MTD
                        inventory.setBatchSerialNumber("1");        // STR_NO
                        inventory.setBatchSerialNumber(newPutAwayLine.getBatchSerialNumber());
                        inventory.setStorageBin(createdPutAwayLine.getConfirmedStorageBin());
						inventory.setBarcodeId(createdPutAwayLine.getBarcodeId());

                        //v2 code
                        if (createdPutAwayLine.getCbm() == null) {
                            createdPutAwayLine.setCbm("0");
                        }
                        if (createdPutAwayLine.getCbmQuantity() == null) {
                            createdPutAwayLine.setCbmQuantity(0D);
                        }
                        inventory.setCbmPerQuantity(String.valueOf(Double.valueOf(createdPutAwayLine.getCbm()) / createdPutAwayLine.getCbmQuantity()));
                        inventory.setStockTypeId(10L);

						inventory.setStockIndicator(0L);
						inventory.setGrDate(new Date());
						inventory.setPartnerId(newPutAwayLine.getVendorCode());

                        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
						StorageBinV2 dbStorageBin = mastersService.getStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(),
								dbPutAwayLine.getWarehouseId(),
								dbPutAwayLine.getCompanyCode(),
								dbPutAwayLine.getPlantId(),
								dbPutAwayLine.getLanguageId(),
								authTokenForMastersService.getAccess_token());
                        inventory.setBinClassId(dbStorageBin.getBinClassId());

                        List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(inventory.getItemCode());
                        if (imbasicdata1 != null && !imbasicdata1.isEmpty()) {
                            inventory.setReferenceField8(imbasicdata1.get(0).getDescription());
                            inventory.setReferenceField9(imbasicdata1.get(0).getManufacturePart());
                        }

                        if (dbStorageBin != null) {
                            inventory.setReferenceField10(dbStorageBin.getStorageSectionId());
                            inventory.setReferenceField5(dbStorageBin.getAisleNumber());
                            inventory.setReferenceField6(dbStorageBin.getShelfId());
                            inventory.setReferenceField7(dbStorageBin.getRowId());
                        }

						inventory.setCompanyDescription(dbPutAwayLine.getCompanyDescription());
						inventory.setPlantDescription(dbPutAwayLine.getPlantDescription());
						inventory.setWarehouseDescription(dbPutAwayLine.getWarehouseDescription());

                        /*
                         * Insert PA_CNF_QTY value in this field.
                         * Also Pass WH_ID/PACK_BARCODE/ITM_CODE/BIN_CL_ID=3 in INVENTORY table and fetch ST_BIN/INV_QTY value.
                         * Update INV_QTY value by (INV_QTY - PA_CNF_QTY) . If this value becomes Zero, then delete the record"
                         */
                        try {
                            InventoryV2 existinginventory = inventoryService.getInventory(
                                    createdPutAwayLine.getCompanyCode(), createdPutAwayLine.getPlantId(), createdPutAwayLine.getLanguageId(),
                                    createdPutAwayLine.getWarehouseId(),
                                    createdPutAwayLine.getPackBarcodes(), dbPutAwayLine.getItemCode(), 3L);

                            double INV_QTY = existinginventory.getInventoryQuantity() - createdPutAwayLine.getPutawayConfirmedQty();
                            log.info("INV_QTY : " + INV_QTY);

                            if (INV_QTY >= 0) {
                                existinginventory.setInventoryQuantity(INV_QTY);
                                InventoryV2 updatedInventory = inventoryV2Repository.save(existinginventory);
                                log.info("updatedInventory--------> : " + updatedInventory);
                            }

                        } catch (Exception e) {
                            log.info("Existing Inventory---Error-----> : " + e.toString());
                        }

                        // INV_QTY
                        inventory.setInventoryQuantity(createdPutAwayLine.getPutawayConfirmedQty());

                        // INV_UOM
                        inventory.setInventoryUom(createdPutAwayLine.getPutAwayUom());
                        inventory.setCreatedBy(createdPutAwayLine.getCreatedBy());
                        inventory.setCreatedOn(createdPutAwayLine.getCreatedOn());
                        InventoryV2 createdInventory = inventoryV2Repository.save(inventory);
                        log.info("createdInventory : " + createdInventory);

                        if (createdInventory != null) {
                            isInventoryCreated = true;
                        }

                        /* Insert a record into INVENTORYMOVEMENT table */
                        InventoryMovement createdInventoryMovement = createInventoryMovementV2(createdPutAwayLine);
                        log.info("inventoryMovement created: " + createdInventoryMovement);

                        if (createdInventoryMovement != null) {
                            isInventoryMovemoentCreated = true;
                        }

                        // Updating StorageBin StatusId as '1'
                        dbStorageBin.setStatusId(1L);
                        mastersService.updateStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(), dbStorageBin,
                                dbPutAwayLine.getCompanyCode(), dbPutAwayLine.getPlantId(), dbPutAwayLine.getLanguageId(), dbPutAwayLine.getWarehouseId(),
                                loginUserID, authTokenForMastersService.getAccess_token());

                        if (isInventoryCreated && isInventoryMovemoentCreated) {
                            List<PutAwayHeaderV2> headers = putAwayHeaderService.getPutAwayHeaderV2(createdPutAwayLine.getWarehouseId(),
                                    createdPutAwayLine.getPreInboundNo(),
                                    createdPutAwayLine.getRefDocNumber(),
                                    createdPutAwayLine.getPutAwayNumber(),
                                    createdPutAwayLine.getCompanyCode(),
                                    createdPutAwayLine.getPlantId(),
                                    createdPutAwayLine.getLanguageId());
							statusDescription = stagingLineV2Repository.getStatusDescription(20L, createdPutAwayLine.getLanguageId());
                            for (PutAwayHeaderV2 putAwayHeader : headers) {
                                putAwayHeader.setStatusId(20L);
								putAwayHeader.setStatusDescription(statusDescription);
                                putAwayHeader = putAwayHeaderV2Repository.save(putAwayHeader);
                                log.info("putAwayHeader updated: " + putAwayHeader);
                            }

                            /*--------------------- INBOUNDTABLE Updates ------------------------------------------*/
                            // Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE values in PUTAWAYLINE table and
                            // fetch PA_CNF_QTY values and QTY_TYPE values and updated STATUS_ID as 20
                            double addedAcceptQty = 0.0;
                            double addedDamageQty = 0.0;

							InboundLineV2 inboundLine = inboundLineService.getInboundLineV2(createdPutAwayLine.getCompanyCode(),
									createdPutAwayLine.getPlantId(),
									createdPutAwayLine.getLanguageId(),
									createdPutAwayLine.getWarehouseId(),
									createdPutAwayLine.getRefDocNumber(),
									createdPutAwayLine.getPreInboundNo(),
									createdPutAwayLine.getLineNo(),
									createdPutAwayLine.getItemCode());
                            log.info("inboundLine----from--DB---------> " + inboundLine);

                            // If QTY_TYPE = A, add PA_CNF_QTY with existing value in ACCEPT_QTY field
                            if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("A")) {
                                if (inboundLine.getAcceptedQty() != null) {
                                    addedAcceptQty = inboundLine.getAcceptedQty() + createdPutAwayLine.getPutawayConfirmedQty();
                                } else {
                                    addedAcceptQty = createdPutAwayLine.getPutawayConfirmedQty();
                                }

                                inboundLine.setAcceptedQty(addedAcceptQty);
                            }

                            // if QTY_TYPE = D, add PA_CNF_QTY with existing value in DAMAGE_QTY field
                            if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("D")) {
                                if (inboundLine.getDamageQty() != null) {
                                    addedDamageQty = inboundLine.getDamageQty() + createdPutAwayLine.getPutawayConfirmedQty();
                                } else {
                                    addedDamageQty = createdPutAwayLine.getPutawayConfirmedQty();
                                }

                                inboundLine.setDamageQty(addedDamageQty);
                            }

                            inboundLine.setStatusId(20L);
							statusDescription = stagingLineV2Repository.getStatusDescription(20L, createdPutAwayLine.getLanguageId());
							inboundLine.setStatusDescription(statusDescription);
                            inboundLine = inboundLineV2Repository.save(inboundLine);
                            log.info("inboundLine updated : " + inboundLine);
                        }
                        if (dbPutAwayLine.getCbm() != null) {

                            // ST_BIN ---Pass WH_ID/CNF_ST_BIN in STORAGEBIN table and fetch OCC_VOL value and update
							StorageBinV2 storageBin = mastersService.getStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(),
                                                                                    dbPutAwayLine.getWarehouseId(),
                                                                                    dbPutAwayLine.getCompanyCode(),
                                                                                    dbPutAwayLine.getPlantId(),
                                                                                    dbPutAwayLine.getLanguageId(),
                                                                                    authTokenForMastersService.getAccess_token());

							if (dbStorageBin.getOccupiedVolume() == null) {
								dbStorageBin.setOccupiedVolume("0");
							}
							if (dbStorageBin.getTotalVolume() == null) {
								dbStorageBin.setTotalVolume("0");
							}

                            Double occupiedVolume = Double.valueOf(storageBin.getOccupiedVolume());
                            Double cbm = Double.valueOf(dbPutAwayLine.getCbm());

                            storageBin.setOccupiedVolume(String.valueOf(occupiedVolume + cbm));
                            storageBin.setRemainingVolume(String.valueOf(Double.valueOf(storageBin.getTotalVolume()) - Double.valueOf(storageBin.getOccupiedVolume())));

                            StorageBinV2 updateStorageBin = mastersService.updateStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(),
                                    storageBin,
                                    dbPutAwayLine.getCompanyCode(),
                                    dbPutAwayLine.getPlantId(),
                                    dbPutAwayLine.getLanguageId(),
                                    dbPutAwayLine.getWarehouseId(),
                                    loginUserID,
                                    authTokenForMastersService.getAccess_token());

                        }
                    }
                } else {
                    log.info("Putaway Line already exist : " + existingPutAwayLine);
                }
            }

            return createdPutAwayLines;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

	public PutAwayLineV2 createPutAwayLineV2(PutAwayLineV2 newPutAwayLine, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLineV2 dbPutAwayLine = new PutAwayLineV2();
		log.info("newPutAwayLine : " + newPutAwayLine);
		BeanUtils.copyProperties(newPutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(newPutAwayLine));

//        if (newPutAwayLine.getPutawayConfirmedQty() < newPutAwayLine.getPutAwayQuantity()) {
//            List<PutAwayHeaderV2> dbPutAwayHeaderList = putAwayHeaderService.getPutAwayHeaderV2(newPutAwayLine.getWarehouseId(),
//                    newPutAwayLine.getPreInboundNo(),
//                    newPutAwayLine.getRefDocNumber(),
//                    newPutAwayLine.getPutAwayNumber(),
//                    newPutAwayLine.getCompanyCode(),
//                    newPutAwayLine.getPlantId(),
//                    newPutAwayLine.getLanguageId());
//            if (dbPutAwayHeaderList != null) {
//                for (PutAwayHeaderV2 newPutAwayHeader : dbPutAwayHeaderList) {
//                    if (!newPutAwayHeader.getApprovalStatus().equalsIgnoreCase("Approved")) {
//                        throw new BadRequestException("Binned Quantity is less than Received");
//                    }
//                }
//            }
//        }

		IKeyValuePair description = stagingLineV2Repository.getDescription(newPutAwayLine.getCompanyCode(),
				newPutAwayLine.getLanguageId(),
				newPutAwayLine.getPlantId(),
				newPutAwayLine.getWarehouseId());

		dbPutAwayLine.setCompanyDescription(description.getCompanyDesc());
		dbPutAwayLine.setPlantDescription(description.getPlantDesc());
		dbPutAwayLine.setWarehouseDescription(description.getWarehouseDesc());

		dbPutAwayLine.setDeletionIndicator(0L);
		dbPutAwayLine.setCreatedBy(loginUserID);
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setCreatedOn(new Date());
		dbPutAwayLine.setUpdatedOn(new Date());
		PutAwayLineV2 createdPutAwayLine = putAwayLineRepository.save(dbPutAwayLine);

		/*----------------Inventory tables Create---------------------------------------------*/
		InventoryV2 createdinventory = createInventory(dbPutAwayLine);

		if (dbPutAwayLine.getCbm() != null) {

			// ST_BIN ---Pass WH_ID/CNF_ST_BIN in STORAGEBIN table and fetch OCC_VOL value and update
			AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
			StorageBinV2 storageBin = mastersService.getStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(), dbPutAwayLine.getWarehouseId(), authTokenForMastersService.getAccess_token());

			Double occupiedVolume = Double.valueOf(storageBin.getOccupiedVolume());
			Double cbm = Double.valueOf(dbPutAwayLine.getCbm());

			storageBin.setOccupiedVolume(String.valueOf(occupiedVolume + cbm));
			storageBin.setRemainingVolume(String.valueOf(Double.valueOf(storageBin.getTotalVolume()) - Double.valueOf(storageBin.getOccupiedVolume())));

			StorageBinV2 updateStorageBin = mastersService.updateStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(),
					storageBin,
					dbPutAwayLine.getCompanyCode(),
					dbPutAwayLine.getPlantId(),
					dbPutAwayLine.getLanguageId(),
					dbPutAwayLine.getWarehouseId(),
					loginUserID,
					authTokenForMastersService.getAccess_token());

		}

		return createdPutAwayLine;
	}

	/**
	 * @param createdPALine
	 * @return
	 */
	private InventoryV2 createInventory(PutAwayLineV2 createdPALine) {

		InventoryV2 inventory = new InventoryV2();

		BeanUtils.copyProperties(createdPALine, inventory, CommonUtils.getNullPropertyNames(createdPALine));

		inventory.setCompanyCodeId(createdPALine.getCompanyCode());


		//V2 Code (remaining all fields copied already using beanUtils.copyProperties)
		if (createdPALine.getCbm() == null) {
			createdPALine.setCbm("0");
		}
		if (createdPALine.getCbmQuantity() == null) {
			createdPALine.setCbmQuantity(0D);
		}
		inventory.setCbmPerQuantity(String.valueOf(Double.valueOf(createdPALine.getCbm()) / createdPALine.getCbmQuantity()));
		inventory.setStockTypeId(10L);

		InventoryV2 createdinventory = inventoryV2Repository.save(inventory);
		log.info("created inventory : " + createdinventory);
		return createdinventory;
	}

	/**
	 * @param searchPutAwayLine
	 * @return
	 * @throws Exception
	 */
	public List<PutAwayLineV2> findPutAwayLineV2(SearchPutAwayLineV2 searchPutAwayLine) throws Exception {

		if (searchPutAwayLine.getFromConfirmedDate() != null && searchPutAwayLine.getToConfirmedDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPutAwayLine.getFromConfirmedDate(), searchPutAwayLine.getToConfirmedDate());
			searchPutAwayLine.setFromConfirmedDate(dates[0]);
			searchPutAwayLine.setToConfirmedDate(dates[1]);
		}

		PutAwayLineV2Specification spec = new PutAwayLineV2Specification(searchPutAwayLine);
		List<PutAwayLineV2> results = putAwayLineV2Repository.findAll(spec);
		return results;
	}

	/**
	 * @param companyCode
	 * @param plantId
	 * @param languageId
	 * @param warehouseId
	 * @param goodsReceiptNo
	 * @param preInboundNo
	 * @param refDocNumber
	 * @param putAwayNumber
	 * @param lineNo
	 * @param itemCode
	 * @param proposedStorageBin
	 * @param confirmedStorageBin
	 * @param loginUserID
	 * @param updatePutAwayLine
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PutAwayLineV2 updatePutAwayLinev2(String companyCode, String plantId, String languageId,
											 String warehouseId, String goodsReceiptNo, String preInboundNo,
											 String refDocNumber, String putAwayNumber, Long lineNo, String itemCode,
											 String proposedStorageBin, String confirmedStorageBin,
											 String loginUserID, PutAwayLineV2 updatePutAwayLine)
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLineV2 dbPutAwayLine = getPutAwayLineV2(companyCode, plantId, languageId, warehouseId,
				goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber,
				lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
		BeanUtils.copyProperties(updatePutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(updatePutAwayLine));
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setUpdatedOn(new Date());
		return putAwayLineV2Repository.save(dbPutAwayLine);
	}

	/**
	 * @param updatePutAwayLine
	 * @param loginUserID
	 * @return
	 */
	public PutAwayLineV2 updatePutAwayLineV2(PutAwayLineV2 updatePutAwayLine, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		PutAwayLineV2 dbPutAwayLine = new PutAwayLineV2();
		BeanUtils.copyProperties(updatePutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(updatePutAwayLine));
		dbPutAwayLine.setUpdatedBy(loginUserID);
		dbPutAwayLine.setUpdatedOn(new Date());
		return putAwayLineV2Repository.save(dbPutAwayLine);
	}

	/**
	 * @param languageId
	 * @param companyCodeId
	 * @param plantId
	 * @param warehouseId
	 * @param goodsReceiptNo
	 * @param preInboundNo
	 * @param refDocNumber
	 * @param putAwayNumber
	 * @param lineNo
	 * @param itemCode
	 * @param proposedStorageBin
	 * @param confirmedStorageBin
	 * @param loginUserID
	 */
	public void deletePutAwayLineV2(String languageId, String companyCodeId, String plantId, String warehouseId,
									String goodsReceiptNo, String preInboundNo, String refDocNumber, String putAwayNumber, Long lineNo,
									String itemCode, String proposedStorageBin, String confirmedStorageBin, String loginUserID) {
		PutAwayLineV2 putAwayLine = getPutAwayLineV2(companyCodeId, plantId, languageId, warehouseId,
				goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber,
				lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
		if (putAwayLine != null) {
			putAwayLine.setDeletionIndicator(1L);
			putAwayLine.setUpdatedBy(loginUserID);
			putAwayLineRepository.save(putAwayLine);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + confirmedStorageBin);
		}
	}

	/**
	 * @return
	 */
	public List<PutAwayLineV2> getPutAwayLinesV2() {
		List<PutAwayLineV2> putAwayLineList = putAwayLineV2Repository.findAll();
		putAwayLineList = putAwayLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return putAwayLineList;
	}

	/**
	 * @param asnNumber
	 */
	public void updateASNV2(String asnNumber) {
		List<PutAwayLineV2> putAwayLines = getPutAwayLinesV2();
		putAwayLines.stream().forEach(p -> p.setReferenceField1(asnNumber));
		putAwayLineV2Repository.saveAll(putAwayLines);
	}

	/**
	 *
	 * @param dbPutAwayLine
	 * @return
	 */
	private InventoryMovement createInventoryMovementV2(PutAwayLineV2 dbPutAwayLine) {
		InventoryMovement inventoryMovement = new InventoryMovement();
		BeanUtils.copyProperties(dbPutAwayLine, inventoryMovement, CommonUtils.getNullPropertyNames(dbPutAwayLine));
		inventoryMovement.setCompanyCodeId(dbPutAwayLine.getCompanyCode());

		// MVT_TYP_ID
		inventoryMovement.setMovementType(1L);

		// SUB_MVT_TYP_ID
		inventoryMovement.setSubmovementType(2L);

		// VAR_ID
		inventoryMovement.setVariantCode(1L);

		// VAR_SUB_ID
		inventoryMovement.setVariantSubCode("1");

		// STR_MTD
		inventoryMovement.setStorageMethod("1");

		// STR_NO
		inventoryMovement.setBatchSerialNumber("1");

		// CASE_CODE
		inventoryMovement.setCaseCode("999999");

		// PAL_CODE
		inventoryMovement.setPalletCode("999999");

		// MVT_DOC_NO
		inventoryMovement.setMovementDocumentNo(dbPutAwayLine.getRefDocNumber());

		// ST_BIN
		inventoryMovement.setStorageBin(dbPutAwayLine.getConfirmedStorageBin());

		// MVT_QTY
		inventoryMovement.setMovementQty(dbPutAwayLine.getPutawayConfirmedQty());

		// MVT_QTY_VAL
		inventoryMovement.setMovementQtyValue("P");

		// MVT_UOM
		inventoryMovement.setInventoryUom(dbPutAwayLine.getPutAwayUom());

		/*
		 * -----THE BELOW IS NOT USED-------------
		 * Pass WH_ID/ITM_CODE/PACK_BARCODE/BIN_CL_ID is equal to 1 in INVENTORY table and fetch INV_QTY
		 * BAL_OH_QTY = INV_QTY
		 */
		// PASS WH_ID/ITM_CODE/BIN_CL_ID and sum the INV_QTY for all selected inventory
		List<InventoryV2> inventoryList =
				inventoryService.getInventory (
						dbPutAwayLine.getCompanyCode(),
						dbPutAwayLine.getPlantId(),
						dbPutAwayLine.getLanguageId(),
						dbPutAwayLine.getWarehouseId(),
						dbPutAwayLine.getItemCode(),
						1L);
		double sumOfInvQty = inventoryList.stream().mapToDouble(a->a.getInventoryQuantity()).sum();
		inventoryMovement.setBalanceOHQty(sumOfInvQty);

		// IM_CTD_BY
		inventoryMovement.setCreatedBy(dbPutAwayLine.getCreatedBy());

		// IM_CTD_ON
		inventoryMovement.setCreatedOn(dbPutAwayLine.getCreatedOn());
		inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
		return inventoryMovement;
	}
}