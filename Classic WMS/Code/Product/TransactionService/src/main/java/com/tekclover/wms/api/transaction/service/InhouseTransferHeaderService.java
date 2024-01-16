package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tekclover.wms.api.transaction.model.IkeyValuePair;
import com.tekclover.wms.api.transaction.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.IImbasicData1;
import com.tekclover.wms.api.transaction.model.dto.StorageBin;
import com.tekclover.wms.api.transaction.model.inbound.inventory.AddInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.mnc.AddInhouseTransferHeader;
import com.tekclover.wms.api.transaction.model.mnc.AddInhouseTransferLine;
import com.tekclover.wms.api.transaction.model.mnc.InhouseTransferHeader;
import com.tekclover.wms.api.transaction.model.mnc.InhouseTransferHeaderEntity;
import com.tekclover.wms.api.transaction.model.mnc.InhouseTransferLine;
import com.tekclover.wms.api.transaction.model.mnc.InhouseTransferLineEntity;
import com.tekclover.wms.api.transaction.model.mnc.SearchInhouseTransferHeader;
import com.tekclover.wms.api.transaction.repository.specification.InhouseTransferHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InhouseTransferHeaderService extends BaseService {
    @Autowired
    private ContainerReceiptRepository containerReceiptRepository;

    private static final String ONESTEP = "ONESTEP";

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private InhouseTransferHeaderRepository inhouseTransferHeaderRepository;

    @Autowired
    private InhouseTransferLineRepository inhouseTransferLineRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StorageBinRepository storageBinRepository;

    @Autowired
    private ImBasicData1Repository imbasicdata1Repository;

    /**
     * getInHouseTransferHeaders
     *
     * @return
     */
    public List<InhouseTransferHeader> getInHouseTransferHeaders() {
        List<InhouseTransferHeader> InHouseTransferHeaderList = inhouseTransferHeaderRepository.findAll();
        InHouseTransferHeaderList = InHouseTransferHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return InHouseTransferHeaderList;
    }

    /**
     * @param warehouseId
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param transferNumber
     * @param transferTypeId
     * @return
     */
    public InhouseTransferHeader getInHouseTransferHeader(String warehouseId, String companyCode, String plantId, String languageId,
                                                          String transferNumber, Long transferTypeId) {
        Optional<InhouseTransferHeader> inHouseTransferHeader =
                inhouseTransferHeaderRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndTransferNumberAndTransferTypeIdAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        transferNumber,
                        transferTypeId,
                        0L);
        if (inHouseTransferHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",companyCode:" + companyCode +
                    ",plantId:" + plantId +
                    ",languageId: " + languageId +
                    ",transferNumber: " + transferNumber +
                    " doesn't exist.");
        }
        return inHouseTransferHeader.get();
    }

    /**
     * @param searchInHouseTransferHeader
     * @return
     * @throws Exception
     */
    public List<InhouseTransferHeader> findInHouseTransferHeader(SearchInhouseTransferHeader searchInHouseTransferHeader) throws Exception {
        if (searchInHouseTransferHeader.getStartCreatedOn() != null &&
                searchInHouseTransferHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchInHouseTransferHeader.getStartCreatedOn(),
                    searchInHouseTransferHeader.getEndCreatedOn());
            searchInHouseTransferHeader.setStartCreatedOn(dates[0]);
            searchInHouseTransferHeader.setEndCreatedOn(dates[1]);
        }

        InhouseTransferHeaderSpecification spec = new InhouseTransferHeaderSpecification(searchInHouseTransferHeader);
        List<InhouseTransferHeader> results = inhouseTransferHeaderRepository.findAll(spec);
        return results;
    }

    /**
     * Streaming
     *
     * @param searchInHouseTransferHeader
     * @return
     * @throws Exception
     */
    public Stream<InhouseTransferHeader> findInHouseTransferHeaderNew(SearchInhouseTransferHeader searchInHouseTransferHeader) throws Exception {
        if (searchInHouseTransferHeader.getStartCreatedOn() != null &&
                searchInHouseTransferHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchInHouseTransferHeader.getStartCreatedOn(),
                    searchInHouseTransferHeader.getEndCreatedOn());
            searchInHouseTransferHeader.setStartCreatedOn(dates[0]);
            searchInHouseTransferHeader.setEndCreatedOn(dates[1]);
        }

        InhouseTransferHeaderSpecification spec = new InhouseTransferHeaderSpecification(searchInHouseTransferHeader);
        Stream<InhouseTransferHeader> results = inhouseTransferHeaderRepository.stream(spec, InhouseTransferHeader.class);
        return results;
    }

    /**
     * @param newInhouseTransferHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public InhouseTransferHeaderEntity createInHouseTransferHeader(AddInhouseTransferHeader newInhouseTransferHeader,
                                                                   String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        InhouseTransferHeader dbInhouseTransferHeader = new InhouseTransferHeader();
        log.info("newInHouseTransferHeader : " + newInhouseTransferHeader);

//		if(newInhouseTransferHeader.getTransferTypeId().equals(3L)){
//			int i = 0;
//			for(AddInhouseTransferLine lineData : newInhouseTransferHeader.getInhouseTransferLine()) {
//				if(lineData.getTransferConfirmedQty() == null || lineData.getTransferOrderQty() == null || lineData.getTransferConfirmedQty() == 0L || lineData.getTransferOrderQty() == 0L ){
//					throw new BadRequestException("Transfer Quantity cannot not be null or zero for line : " + (i+1));
//				}
//				i++;
//			}
//		}

        BeanUtils.copyProperties(newInhouseTransferHeader, dbInhouseTransferHeader, CommonUtils.getNullPropertyNames(newInhouseTransferHeader));
        AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
//		UserManagement userManagement = getUserManagement (loginUserID, newInhouseTransferHeader.getWarehouseId());

        IkeyValuePair ikeyValuePair = containerReceiptRepository.getDescription(
                newInhouseTransferHeader.getLanguageId(), newInhouseTransferHeader.getCompanyCodeId(),
                newInhouseTransferHeader.getPlantId(), newInhouseTransferHeader.getWarehouseId());

        if(ikeyValuePair != null){
            dbInhouseTransferHeader.setCompanyDescription(ikeyValuePair.getCompanyDesc());
            dbInhouseTransferHeader.setPlantDescription(ikeyValuePair.getPlantDesc());
            dbInhouseTransferHeader.setWarehouseDescription(ikeyValuePair.getWarehouseDesc());
        }

        dbInhouseTransferHeader.setLanguageId(newInhouseTransferHeader.getLanguageId());
        dbInhouseTransferHeader.setCompanyCodeId(newInhouseTransferHeader.getCompanyCodeId());
        dbInhouseTransferHeader.setPlantId(newInhouseTransferHeader.getPlantId());
        dbInhouseTransferHeader.setWarehouseId(newInhouseTransferHeader.getWarehouseId());
        // TR_NO
        String TRANSFER_NO = getTransferNo(newInhouseTransferHeader.getWarehouseId(),
                newInhouseTransferHeader.getCompanyCodeId(),
                newInhouseTransferHeader.getPlantId(),
                newInhouseTransferHeader.getLanguageId(),
                authTokenForIDMasterService.getAccess_token());
        dbInhouseTransferHeader.setTransferNumber(TRANSFER_NO);

        // STATUS_ID - Hard Coded Value="30" at the time of Confirmation
        dbInhouseTransferHeader.setStatusId(30L);

        String statusDescription = containerReceiptRepository.getStatusDescription(30L,newInhouseTransferHeader.getLanguageId());
        if(statusDescription != null){
            dbInhouseTransferHeader.setStatusDescription(statusDescription);
        }
        dbInhouseTransferHeader.setDeletionIndicator(0L);
        dbInhouseTransferHeader.setCreatedBy(loginUserID);
        dbInhouseTransferHeader.setUpdatedBy(loginUserID);
        dbInhouseTransferHeader.setCreatedOn(new Date());
        dbInhouseTransferHeader.setUpdatedOn(new Date());

        // - TR_TYP_ID -
        Long transferTypeId = dbInhouseTransferHeader.getTransferTypeId();

        /*
         * LINES Table
         */
        InhouseTransferHeaderEntity responseHeader = new InhouseTransferHeaderEntity();
        List<InhouseTransferLineEntity> responseLines = new ArrayList<>();
        for (AddInhouseTransferLine newInhouseTransferLine : newInhouseTransferHeader.getInhouseTransferLine()) {
            InhouseTransferLine dbInhouseTransferLine = new InhouseTransferLine();
            BeanUtils.copyProperties(newInhouseTransferLine, dbInhouseTransferLine, CommonUtils.getNullPropertyNames(newInhouseTransferLine));

            dbInhouseTransferLine.setLanguageId(dbInhouseTransferHeader.getLanguageId());
            dbInhouseTransferLine.setCompanyCodeId(dbInhouseTransferHeader.getCompanyCodeId());
            dbInhouseTransferLine.setPlantId(dbInhouseTransferHeader.getPlantId());

            // WH_ID
            dbInhouseTransferLine.setWarehouseId(dbInhouseTransferHeader.getWarehouseId());

            // TR_NO
            dbInhouseTransferLine.setTransferNumber(TRANSFER_NO);

            // STATUS_ID - Hard Coded Value="30" at the time of Confirmation
            dbInhouseTransferLine.setStatusId(30L);
            dbInhouseTransferLine.setDeletionIndicator(0L);
            dbInhouseTransferLine.setCreatedBy(loginUserID);
            dbInhouseTransferLine.setCreatedOn(new Date());
            dbInhouseTransferLine.setUpdatedBy(loginUserID);
            dbInhouseTransferLine.setUpdatedOn(new Date());
            dbInhouseTransferLine.setConfirmedBy(loginUserID);
            dbInhouseTransferLine.setConfirmedOn(new Date());

            // Save InhouseTransferLine
            InhouseTransferLine createdInhouseTransferLine = inhouseTransferLineRepository.save(dbInhouseTransferLine);
            log.info("InhouseTransferLine created : " + createdInhouseTransferLine);

            /* Response List */
            InhouseTransferLineEntity responseInhouseTransferLineEntity = new InhouseTransferLineEntity();
            BeanUtils.copyProperties(createdInhouseTransferLine, responseInhouseTransferLineEntity,
                    CommonUtils.getNullPropertyNames(createdInhouseTransferLine));
            responseLines.add(responseInhouseTransferLineEntity);

            if (createdInhouseTransferLine != null) {
                // Save InhouseTransferHeader
                log.info("InhouseTransferHeader before create-->: " + dbInhouseTransferHeader);
                InhouseTransferHeader createdInhouseTransferHeader = inhouseTransferHeaderRepository.save(dbInhouseTransferHeader);
                log.info("InhouseTransferHeader created: " + createdInhouseTransferHeader);

                /*--------------------INVENTORY TABLE UPDATES-----------------------------------------------*/
                updateInventory(createdInhouseTransferHeader, createdInhouseTransferLine, loginUserID);

                /*--------------------INVENTORYMOVEMENT TABLE UPDATES-----------------------------------------------*/
                /*
                 * If TR_TYP_ID = 01, insert 2 records in INVENTORYMOVEMENT table.
                 * One record with STCK_TYP_ID = SRCE_STCK_TYP_ID and other record with STCK_TYP_ID = TGT_STCK_TYP_ID
                 */
//				if (transferTypeId == 1L) {
//					// Row insertion for Source
//					Long stockTypeId = createdInhouseTransferLine.getSourceStockTypeId();
//					String movementQtyValue = "N";
//					String itemCode = createdInhouseTransferLine.getSourceItemCode();
//					String storageBin = createdInhouseTransferLine.getSourceStorageBin();
//					createInventoryMovement (createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
//							movementQtyValue, loginUserID);
//					
//					// Row insertion for Target
//					stockTypeId = createdInhouseTransferLine.getTargetStockTypeId();
//					movementQtyValue = "P";
//					createInventoryMovement (createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
//							movementQtyValue, loginUserID);
//				}

                /*
                 * "1. If TR_TYP_ID = 02, insert 2 records in INVENTORYMOVEMENT table.
                 * One record with ITM_CODE = SRCE_ITM_CODE and other record with ITM_CODE = TGT_ITM_CODE
                 */
//				if (transferTypeId == 2L) {
//					// Row insertion for Source
//					Long stockTypeId = createdInhouseTransferLine.getSourceStockTypeId();
//					String storageBin = createdInhouseTransferLine.getSourceStorageBin();
//					String movementQtyValue = "N";
//					String itemCode = createdInhouseTransferLine.getSourceItemCode();
//					createInventoryMovement (createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
//							movementQtyValue, loginUserID);
//					
//					// Row insertion for Target
//					movementQtyValue = "P";
//					itemCode = createdInhouseTransferLine.getTargetItemCode();
//					createInventoryMovement (createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
//							movementQtyValue, loginUserID);
//				}

                /*
                 * If TR_TYP_ID = 03, insert 2 records in INVENTORYMOVEMENT table.
                 * One record with ST_BIN = SRCE_ST_BIN and other record with ST_BIN = TGT_ST_BIN
                 */
                if (transferTypeId == 3L) {
                    // Row insertion for Source
                    Long stockTypeId = createdInhouseTransferLine.getSourceStockTypeId();
                    String itemCode = createdInhouseTransferLine.getSourceItemCode();
                    String movementQtyValue = "N";
                    String storageBin = createdInhouseTransferLine.getSourceStorageBin();
                    createInventoryMovement(createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
                            movementQtyValue, loginUserID);

                    // Row insertion for Target
                    movementQtyValue = "P";
                    storageBin = createdInhouseTransferLine.getTargetStorageBin();
                    createInventoryMovement(createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, storageBin,
                            movementQtyValue, loginUserID);
                }

                /* Response Header */
                BeanUtils.copyProperties(createdInhouseTransferHeader, responseHeader,
                        CommonUtils.getNullPropertyNames(createdInhouseTransferHeader));
            }
        }

        responseHeader.setInhouseTransferLine(responseLines);
        return responseHeader;
    }

    /**
     * @param createdInhouseTransferHeader
     * @param createdInhouseTransferLine
     * @param loginUserID
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void updateInventory(InhouseTransferHeader createdInhouseTransferHeader,
                                 InhouseTransferLine createdInhouseTransferLine, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        Long transferTypeId = createdInhouseTransferHeader.getTransferTypeId();
        String transferMethod = createdInhouseTransferHeader.getTransferMethod();
        String warehouseId = createdInhouseTransferHeader.getWarehouseId();
        String itemCode = createdInhouseTransferLine.getSourceItemCode(); // sourceItemCode
        String companyCodeId = createdInhouseTransferHeader.getCompanyCodeId();
        String plantId = createdInhouseTransferHeader.getPlantId();
        String languageId = createdInhouseTransferHeader.getLanguageId();

        log.info("--------transferTypeId-------- : " + transferTypeId);
        log.info("--------transferMethod-------- : " + transferMethod);
        /*
         * 1 .TR_TYP_ID = 01, TR_MTD = ONESTEP
         * Pass WH_ID/ITM_CODE in INVENTORY TABLE and update STCK_TYP_ID with TGT_STCK_TYP_ID
         */
        if (transferTypeId == 1L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            List<Inventory> inventoryList = inventoryService.getInventoryV2(companyCodeId, plantId, languageId, warehouseId, itemCode);
            log.info("-------------------inventoryList----------------- : " + inventoryList);
            for (Inventory inventory : inventoryList) {
                log.info("inventory: " + inventory);
                inventory.setStockTypeId(createdInhouseTransferLine.getTargetStockTypeId());
                Inventory updatedInventory = inventoryRepository.save(inventory);
                log.info("transferTypeId: " + transferTypeId);
                log.info("updatedInventory : " + updatedInventory);
            }
        }

        /*
         * 2 .TR_TYP_ID = 02, TR_MTD = ONESTEP
         * Pass WH_ID/SRCE_ITM_CODE as ITM_CODE in INVENTORY TABLE and update SRC
         * E_ITM_CODE with TGT_ITM_CODE
         */
        if (transferTypeId == 2L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            List<Inventory> inventoryList = inventoryService.getInventoryV2(companyCodeId, plantId, languageId,warehouseId, itemCode);
            for (Inventory dbInventory : inventoryList) {

                // insert a record with target item code and delete the old record in Inventory table
                Inventory newInventory = new Inventory();
                BeanUtils.copyProperties(dbInventory, newInventory, CommonUtils.getNullPropertyNames(dbInventory));
                newInventory.setItemCode(createdInhouseTransferLine.getTargetItemCode());
                Inventory createdNewInventory = inventoryRepository.save(newInventory);
                log.info("createdNewInventory : " + createdNewInventory);

                // Delete the old record
                inventoryRepository.delete(dbInventory);
                log.info("dbInventory deleted.");
            }
        }

        /*
         * 3 .TR_TYP_ID = 03, TR_MTD=ONESTEP
         * Pass WH_ID/SRCE_ITM_CODE/PACK_BARCOE/SRCE_ST_BIN in INVENTORY TABLE and
         * update INV_QTY value (INV_QTY - TR_CNF_QTY) and delete the record if INV_QTY becomes Zero
         */
        if (transferTypeId == 3L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            Inventory inventorySourceItemCode =
                    inventoryService.getInventory(warehouseId, companyCodeId, plantId, languageId,
                            createdInhouseTransferLine.getPackBarcodes(),
                            createdInhouseTransferLine.getSourceItemCode(),
                            createdInhouseTransferLine.getSourceStorageBin());
            log.info("---------inventory----------> : " + inventorySourceItemCode);
            if (inventorySourceItemCode != null) {
                Double inventoryQty = inventorySourceItemCode.getInventoryQuantity();
                Double transferConfirmedQty = createdInhouseTransferLine.getTransferConfirmedQty();
                double INV_QTY = inventoryQty - transferConfirmedQty;
                if (INV_QTY < 0) {
                    throw new BadRequestException("Inventory became negative." + INV_QTY);
                }

                log.info("-----Source----INV_QTY-----------> : " + INV_QTY);
                inventorySourceItemCode.setInventoryQuantity(INV_QTY);
                Inventory updatedInventory = inventoryRepository.save(inventorySourceItemCode);
                log.info("--------source---inventory-----updated----->" + updatedInventory);

                if (INV_QTY == 0 && (inventorySourceItemCode.getAllocatedQuantity() == null || inventorySourceItemCode.getAllocatedQuantity() == 0D)) {
                    // Deleting record
                    inventoryRepository.delete(inventorySourceItemCode);
                    log.info("---------inventory-----deleted-----");
                    try {
                        StorageBin dbStorageBin = getStorageBin(createdInhouseTransferLine.getWarehouseId(),
                                createdInhouseTransferLine.getSourceStorageBin());
                        dbStorageBin.setStatusId(0L);
                        dbStorageBin.setUpdatedBy(loginUserID);
                        dbStorageBin.setUpdatedOn(new Date());
                        storageBinRepository.save(dbStorageBin);
                        log.info("---------storage bin updated-------", dbStorageBin);
                    } catch (Exception e) {
                        log.error("---------storagebin-update-----", e);
                    }

                }

                // Pass WH_ID/ TGT_ITM_CODE/PACK_BARCODE/TGT_ST_BIN in INVENTORY TABLE validate for a record.
                Inventory inventoryTargetItemCode = inventoryService.getInventory(warehouseId, companyCodeId, plantId, languageId, createdInhouseTransferLine.getPackBarcodes(),
                        createdInhouseTransferLine.getTargetItemCode(), createdInhouseTransferLine.getTargetStorageBin());
                if (inventoryTargetItemCode != null) {
                    // update INV_QTY value (INV_QTY + TR_CNF_QTY)
                    inventoryQty = inventoryTargetItemCode.getInventoryQuantity();
                    transferConfirmedQty = createdInhouseTransferLine.getTransferConfirmedQty();
                    INV_QTY = inventoryQty + transferConfirmedQty;
                    log.info("-----Target----INV_QTY-----------> : " + INV_QTY);

                    inventoryTargetItemCode.setInventoryQuantity(INV_QTY);
                    Inventory targetUpdatedInventory = inventoryRepository.save(inventoryTargetItemCode);
                    log.info("------->updatedInventory : " + targetUpdatedInventory);
                } else {
                    /*
                     * Fetch from INHOUSETRANSFERLINE table and insert in INVENTORY table as
                     * WH_ID/ TGT_ITM_CODE/PAL_CODE/PACK_BARCODE/TGT_ST_BIN/CASE_CODE/STCK_TYP_ID/SP_ST_IND_ID/INV_QTY=TR_CNF_QTY/
                     * INV_UOM as QTY_UOM/BIN_CL_ID of ST_BIN from STORAGEBIN table
                     */

                    // "LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PACK_BARCODE", "ITM_CODE", "ST_BIN", "SP_ST_IND_ID"
                    AddInventory newInventory = new AddInventory();
                    BeanUtils.copyProperties(createdInhouseTransferLine, newInventory, CommonUtils.getNullPropertyNames(createdInhouseTransferLine));
                    newInventory.setItemCode(createdInhouseTransferLine.getTargetItemCode());
                    newInventory.setPalletCode(createdInhouseTransferLine.getPalletCode());
                    newInventory.setPackBarcodes(createdInhouseTransferLine.getPackBarcodes());
                    newInventory.setStorageBin(createdInhouseTransferLine.getTargetStorageBin());
                    newInventory.setCaseCode(createdInhouseTransferLine.getCaseCode());
                    newInventory.setStockTypeId(createdInhouseTransferLine.getTargetStockTypeId());

                    if (createdInhouseTransferLine.getSpecialStockIndicatorId() == null) {
                        newInventory.setSpecialStockIndicatorId(1L);
                    } else {
                        newInventory.setSpecialStockIndicatorId(createdInhouseTransferLine.getSpecialStockIndicatorId());
                    }

                    newInventory.setInventoryQuantity(createdInhouseTransferLine.getTransferConfirmedQty());
                    newInventory.setInventoryUom(createdInhouseTransferLine.getTransferUom());

                    StorageBin storageBin = getStorageBin(createdInhouseTransferLine.getWarehouseId(),
                            createdInhouseTransferLine.getTargetStorageBin());
                    newInventory.setBinClassId(storageBin.getBinClassId());

                    List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(newInventory.getItemCode());
                    if (imbasicdata1 != null && !imbasicdata1.isEmpty()) {
                        newInventory.setReferenceField8(imbasicdata1.get(0).getDescription());
                        newInventory.setReferenceField9(imbasicdata1.get(0).getManufacturePart());
                    }
                    if (storageBin != null) {
                        newInventory.setReferenceField10(storageBin.getStorageSectionId());
                        newInventory.setReferenceField5(storageBin.getAisleNumber());
                        newInventory.setReferenceField6(storageBin.getShelfId());
                        newInventory.setReferenceField7(storageBin.getRowId());
                    }

                    Inventory createdInventory = inventoryService.createInventory(newInventory, loginUserID);
                    log.info("createdInventory------> : " + createdInventory);
                }
            }
        }
    }

    /**
     * getStorageBin
     *
     * @param storageBin
     * @return
     */
    public StorageBin getStorageBin(String warehouseId, String storageBin) {
        StorageBin storagebin = storageBinRepository.findByWarehouseIdAndStorageBinAndDeletionIndicator(warehouseId, storageBin, 0L);
        if (storagebin != null && storagebin.getDeletionIndicator() != null && storagebin.getDeletionIndicator() == 0) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + storageBin + " doesn't exist.");
        }
    }

    /**
     * @param createdInhouseTransferLine
     * @param transferTypeId
     * @param stockTypeId
     * @param itemCode
     * @param storageBin
     * @param movementQtyValue
     * @param loginUserID
     */
    private void createInventoryMovement(InhouseTransferLine createdInhouseTransferLine, Long transferTypeId,
                                         Long stockTypeId, String itemCode, String storageBin, String movementQtyValue, String loginUserID) {
        InventoryMovement inventoryMovement = new InventoryMovement();
        BeanUtils.copyProperties(createdInhouseTransferLine, inventoryMovement,
                CommonUtils.getNullPropertyNames(createdInhouseTransferLine));

        // CASE_CODE
        inventoryMovement.setCaseCode(createdInhouseTransferLine.getCaseCode());

        // PAL_CODE
        inventoryMovement.setPalletCode(createdInhouseTransferLine.getPalletCode());

        // MVT_TYP_ID
        inventoryMovement.setMovementType(2L);

        // SUB_MVT_TYP_ID
        /*
         * "Pass WH_ID/MVT_TYP_ID=02  in SUBMOVEMENTTYPEID table
         * 1. If TR_TYP_ID = 01 and fetch SUB_MVT_TYP_ID=01 and Autofill
         * 2. If TR_TYP_ID = 02 and fetch SUB_MVT_TYP_ID=02 and Autofill
         * 3.If TR_TYP_ID = 03 and fetch SUB_MVT_TYP_ID=03 and Autofill"
         */
        inventoryMovement.setSubmovementType(transferTypeId);

        // VAR_ID
        inventoryMovement.setVariantCode(1L);

        // VAR_SUB_ID
        inventoryMovement.setVariantSubCode("1");

        // STR_MTD
        inventoryMovement.setStorageMethod("1");

        // STR_NO
        inventoryMovement.setBatchSerialNumber("1");

        // ITM_CODE
        inventoryMovement.setItemCode(itemCode);

        // MVT_DOC_NO
        inventoryMovement.setMovementDocumentNo(createdInhouseTransferLine.getTransferNumber());

        // ST_BIN
        inventoryMovement.setStorageBin(storageBin);

        // STCK_TYP_ID
        inventoryMovement.setStockTypeId(stockTypeId);

        // SP_ST_IND_ID
        inventoryMovement.setSpecialStockIndicator(createdInhouseTransferLine.getSpecialStockIndicatorId());

        // MVT_QTY
        inventoryMovement.setMovementQty(createdInhouseTransferLine.getTransferConfirmedQty());

        // MVT_QTY_VAL
        inventoryMovement.setMovementQtyValue(movementQtyValue);

        // MVT_UOM
        inventoryMovement.setInventoryUom(createdInhouseTransferLine.getTransferUom());

        /*
         * BAL_OH_QTY
         * -------------
         * During Inhouse transfer for transfer type ID -3 and insertion of record Inventorymovement table,
         *  append BAL_OH_QTY field Zero
         */
        inventoryMovement.setBalanceOHQty(0D);

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(createdInhouseTransferLine.getCreatedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(createdInhouseTransferLine.getCreatedOn());
        inventoryMovement.setDeletionIndicator(0L);
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        log.info("inventoryMovement created: for transferTypeId : " + transferTypeId + "---" + inventoryMovement);
    }

    /**
     * @param warehouseId
     * @return
     */
    private String getTransferNo(String warehouseId, String companyCode, String plantId, String languageId, String authToken) {
        String nextRangeNumber = getNextRangeNumber(8, warehouseId, companyCode, plantId, languageId, authToken);
        return nextRangeNumber;
    }
}
