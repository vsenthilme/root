package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.*;
import com.tekclover.wms.api.transaction.model.inbound.gr.StorageBinPutAway;
import com.tekclover.wms.api.transaction.model.inbound.inventory.AddInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.mnc.*;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.InhouseTransferHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class InhouseTransferHeaderService extends BaseService {
    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

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

    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    private MastersService mastersService;

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
     * getInHouseTransferHeader
     *
     * @param transferNumber
     * @return
     */
    public InhouseTransferHeader getInHouseTransferHeader(String warehouseId, String transferNumber, Long transferTypeId) {
        Optional<InhouseTransferHeader> inHouseTransferHeader =
                inhouseTransferHeaderRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndTransferNumberAndTransferTypeIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        transferNumber,
                        transferTypeId,
                        0L);
        if (inHouseTransferHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
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
                searchInHouseTransferHeader.getEndCreatedOn() != null) {
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
                searchInHouseTransferHeader.getEndCreatedOn() != null) {
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
     * createInHouseTransferHeader
     *
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

        dbInhouseTransferHeader.setLanguageId(getLanguageId());
        dbInhouseTransferHeader.setCompanyCodeId(getCompanyCode());
        dbInhouseTransferHeader.setPlantId(getPlantId());
        dbInhouseTransferHeader.setWarehouseId(newInhouseTransferHeader.getWarehouseId());
        // TR_NO
        String TRANSFER_NO = getTransferNo(newInhouseTransferHeader.getWarehouseId(), authTokenForIDMasterService.getAccess_token());
        dbInhouseTransferHeader.setTransferNumber(TRANSFER_NO);

        // STATUS_ID - Hard Coded Value="30" at the time of Confirmation
        dbInhouseTransferHeader.setStatusId(30L);
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

            dbInhouseTransferLine.setLanguageId(getLanguageId());
            dbInhouseTransferLine.setCompanyCodeId(getCompanyCode());
            dbInhouseTransferLine.setPlantId(getPlantId());

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

        log.info("--------transferTypeId-------- : " + transferTypeId);
        log.info("--------transferMethod-------- : " + transferMethod);
        /*
         * 1 .TR_TYP_ID = 01, TR_MTD = ONESTEP
         * Pass WH_ID/ITM_CODE in INVENTORY TABLE and update STCK_TYP_ID with TGT_STCK_TYP_ID
         */
        if (transferTypeId == 1L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            List<Inventory> inventoryList = inventoryService.getInventory(warehouseId, itemCode);
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
            List<Inventory> inventoryList = inventoryService.getInventory(warehouseId, createdInhouseTransferLine.getSourceItemCode());
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
                    inventoryService.getInventory(warehouseId,
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
                Inventory inventoryTargetItemCode = inventoryService.getInventory(warehouseId, createdInhouseTransferLine.getPackBarcodes(),
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

        inventoryMovement.setCompanyDescription(createdInhouseTransferLine.getCompanyDescription());
        inventoryMovement.setPlantDescription(createdInhouseTransferLine.getPlantDescription());
        inventoryMovement.setWarehouseDescription(createdInhouseTransferLine.getWarehouseDescription());

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
     * @param createdInhouseTransferLine
     * @param transferTypeId
     * @param stockTypeId
     * @param itemCode
     * @param manufacturerName
     * @param storageBin
     * @param movementQtyValue
     * @param loginUserID
     */
    private void createInventoryMovementV2(InhouseTransferLine createdInhouseTransferLine, Long transferTypeId,
                                           Long stockTypeId, String itemCode, String manufacturerName, String storageBin, String movementQtyValue, String loginUserID) {
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

        inventoryMovement.setCompanyDescription(createdInhouseTransferLine.getCompanyDescription());
        inventoryMovement.setPlantDescription(createdInhouseTransferLine.getPlantDescription());
        inventoryMovement.setWarehouseDescription(createdInhouseTransferLine.getWarehouseDescription());
        inventoryMovement.setManufacturerName(manufacturerName);
        inventoryMovement.setBarcodeId(createdInhouseTransferLine.getSourceBarcodeId());

        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData imBasicData = new ImBasicData();
        imBasicData.setCompanyCodeId(createdInhouseTransferLine.getCompanyCodeId());
        imBasicData.setPlantId(createdInhouseTransferLine.getPlantId());
        imBasicData.setLanguageId(createdInhouseTransferLine.getLanguageId());
        imBasicData.setWarehouseId(createdInhouseTransferLine.getWarehouseId());
        imBasicData.setItemCode(itemCode);
        imBasicData.setManufacturerName(manufacturerName);
        ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
        log.info("imbasicdata1 : " + imbasicdata1);

        if (imbasicdata1 != null) {
            inventoryMovement.setDescription(imbasicdata1.getDescription());
        }

        // MVT_DOC_NO
//        inventoryMovement.setMovementDocumentNo(createdInhouseTransferLine.getTransferNumber());
        inventoryMovement.setReferenceField10(createdInhouseTransferLine.getTransferNumber());

        // ST_BIN
        inventoryMovement.setStorageBin(storageBin);

        // STCK_TYP_ID
        inventoryMovement.setStockTypeId(stockTypeId);

        // SP_ST_IND_ID
        inventoryMovement.setSpecialStockIndicator(createdInhouseTransferLine.getSpecialStockIndicatorId());

        // MVT_QTY
//        inventoryMovement.setMovementQty(createdInhouseTransferLine.getTransferConfirmedQty());
        inventoryMovement.setMovementQty(0D);                       //Instructed to set '0' since inventory remains unchanged, Qty only moved from one bin to another bin

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
//        inventoryMovement.setBalanceOHQty(0D);
        // BAL_OH_QTY
        Double sumOfInvQty = inventoryService.getInventoryQtyCountForInvMmt(
                createdInhouseTransferLine.getCompanyCodeId(),
                createdInhouseTransferLine.getPlantId(),
                createdInhouseTransferLine.getLanguageId(),
                createdInhouseTransferLine.getWarehouseId(),
                manufacturerName,
                itemCode);
        log.info("BalanceOhQty: " + sumOfInvQty);
        if(sumOfInvQty != null) {
        inventoryMovement.setBalanceOHQty(sumOfInvQty);
            Double openQty = sumOfInvQty;                                           //Inv Qty unchanged
            inventoryMovement.setReferenceField2(String.valueOf(openQty));          //Qty before inventory Movement occur
        }
        if(sumOfInvQty == null) {
            inventoryMovement.setBalanceOHQty(0D);
            inventoryMovement.setReferenceField2("0");          //Qty before inventory Movement occur
        }

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(loginUserID);

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(createdInhouseTransferLine.getCreatedOn());
        inventoryMovement.setDeletionIndicator(0L);
        inventoryMovement.setMovementDocumentNo(String.valueOf(System.currentTimeMillis()));
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        log.info("inventoryMovement created: for transferTypeId : " + transferTypeId + "---" + inventoryMovement);
    }

    /**
     * @param warehouseId
     * @return
     */
    private String getTransferNo(String warehouseId, String authToken) {
        String nextRangeNumber = getNextRangeNumber(8, warehouseId, authToken);
        return nextRangeNumber;
    }

    //================================================================V2=============================================================

    /**
     * createInHouseTransferHeader
     *
     * @param newInhouseTransferHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Transactional
    public InhouseTransferHeaderEntity createInHouseTransferHeaderV2(AddInhouseTransferHeader newInhouseTransferHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        if (newInhouseTransferHeader != null) {
            if (newInhouseTransferHeader.getInhouseTransferLine() != null) {
                Long transferQtyNull = newInhouseTransferHeader.getInhouseTransferLine().stream().filter(a -> a.getTransferOrderQty() == null || a.getTransferConfirmedQty() == null).count();
                if (transferQtyNull > 0) {
                    throw new BadRequestException("TransferQty is Missing!");
                }
            }
        }

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

        dbInhouseTransferHeader.setLanguageId(newInhouseTransferHeader.getLanguageId());
        dbInhouseTransferHeader.setCompanyCodeId(newInhouseTransferHeader.getCompanyCodeId());
        dbInhouseTransferHeader.setPlantId(newInhouseTransferHeader.getPlantId());
        dbInhouseTransferHeader.setWarehouseId(newInhouseTransferHeader.getWarehouseId());
        // TR_NO
        String TRANSFER_NO = getTransferNoV2(newInhouseTransferHeader.getCompanyCodeId(),
                newInhouseTransferHeader.getPlantId(),
                newInhouseTransferHeader.getLanguageId(),
                newInhouseTransferHeader.getWarehouseId(),
                authTokenForIDMasterService.getAccess_token());
        dbInhouseTransferHeader.setTransferNumber(TRANSFER_NO);

        // STATUS_ID - Hard Coded Value="30" at the time of Confirmation
        dbInhouseTransferHeader.setStatusId(30L);
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
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        List<InhouseTransferLineEntity> responseLines = new ArrayList<>();
        for (AddInhouseTransferLine newInhouseTransferLine : newInhouseTransferHeader.getInhouseTransferLine()) {

            StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
            storageBinPutAway.setCompanyCodeId(newInhouseTransferLine.getCompanyCodeId());
            storageBinPutAway.setPlantId(newInhouseTransferLine.getPlantId());
            storageBinPutAway.setLanguageId(newInhouseTransferLine.getLanguageId());
            storageBinPutAway.setWarehouseId(newInhouseTransferLine.getWarehouseId());
            storageBinPutAway.setBin(newInhouseTransferLine.getTargetStorageBin());
            StorageBinV2 dbStorageBin = null;
            try {
                dbStorageBin = mastersService.getaStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
            } catch (Exception e) {
                throw new BadRequestException("Invalid StorageBin");
            }

            //restrict bin to bin transfer from bin class Id 3
            //11-03-2024 - Ticket No. ALM/2024/004
            storageBinPutAway.setBin(newInhouseTransferLine.getSourceStorageBin());
            StorageBinV2 dbSourceStorageBin = null;
            try {
                dbSourceStorageBin = mastersService.getaStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
            } catch (Exception e) {
                throw new BadRequestException("Invalid StorageBin");
            }
            if(dbSourceStorageBin != null && dbSourceStorageBin.getBinClassId() == 3L) {
                throw new BadRequestException("Source Bin must be a Live Location - Either BinClassId 1 or 7");
            }

            if (dbStorageBin != null && dbStorageBin.getBinClassId() == 3L) {
                throw new BadRequestException("Target Bin must be a Live Location - Either BinClassId 1 or 7");
            }

            InhouseTransferLine dbInhouseTransferLine = new InhouseTransferLine();
            BeanUtils.copyProperties(newInhouseTransferLine, dbInhouseTransferLine, CommonUtils.getNullPropertyNames(newInhouseTransferLine));
            dbInhouseTransferLine.setLanguageId(newInhouseTransferLine.getLanguageId());
            dbInhouseTransferLine.setCompanyCodeId(newInhouseTransferLine.getCompanyCodeId());
            dbInhouseTransferLine.setPlantId(newInhouseTransferLine.getPlantId());

            // WH_ID
            dbInhouseTransferLine.setWarehouseId(dbInhouseTransferHeader.getWarehouseId());

            // TR_NO
            dbInhouseTransferLine.setTransferNumber(TRANSFER_NO);
            dbInhouseTransferLine.setManufacturerName(newInhouseTransferLine.getManufacturerName());
            dbInhouseTransferLine.setTransferUom(newInhouseTransferLine.getTransferUom());

            // STATUS_ID - Hard Coded Value="30" at the time of Confirmation
            dbInhouseTransferLine.setStatusId(30L);
            dbInhouseTransferLine.setDeletionIndicator(0L);
            dbInhouseTransferLine.setCreatedBy(loginUserID);
            dbInhouseTransferLine.setCreatedOn(new Date());
            dbInhouseTransferLine.setUpdatedBy(loginUserID);
            dbInhouseTransferLine.setUpdatedOn(new Date());
            dbInhouseTransferLine.setConfirmedBy(loginUserID);
            dbInhouseTransferLine.setConfirmedOn(new Date());

            IKeyValuePair description = stagingLineV2Repository.getDescription(dbInhouseTransferLine.getCompanyCodeId(),
                    dbInhouseTransferLine.getLanguageId(),
                    dbInhouseTransferLine.getPlantId(),
                    dbInhouseTransferLine.getWarehouseId());

            dbInhouseTransferLine.setCompanyDescription(description.getCompanyDesc());
            dbInhouseTransferLine.setPlantDescription(description.getPlantDesc());
            dbInhouseTransferLine.setWarehouseDescription(description.getWarehouseDesc());

            List<String> barcode = stagingLineV2Repository.getPartnerItemBarcode(dbInhouseTransferLine.getSourceItemCode(),
                    dbInhouseTransferLine.getCompanyCodeId(),
                    dbInhouseTransferLine.getPlantId(),
                    dbInhouseTransferLine.getWarehouseId(),
                    dbInhouseTransferLine.getManufacturerName(),
                    dbInhouseTransferLine.getLanguageId());
            log.info("source item Barcode : " + barcode);
            if (barcode != null && !barcode.isEmpty()) {
                dbInhouseTransferLine.setSourceBarcodeId(barcode.get(0));
            }

            List<String> targetBarcode = stagingLineV2Repository.getPartnerItemBarcode(dbInhouseTransferLine.getTargetItemCode(),
                    dbInhouseTransferLine.getCompanyCodeId(),
                    dbInhouseTransferLine.getPlantId(),
                    dbInhouseTransferLine.getWarehouseId(),
                    dbInhouseTransferLine.getManufacturerName(),
                    dbInhouseTransferLine.getLanguageId());
            log.info("target item Barcode : " + targetBarcode);
            if (targetBarcode != null && !targetBarcode.isEmpty()) {
                dbInhouseTransferLine.setTargetBarcodeId(targetBarcode.get(0));
            }

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
                updateInventoryV2(createdInhouseTransferHeader, createdInhouseTransferLine, loginUserID);

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
                    createInventoryMovementV2(createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, createdInhouseTransferLine.getManufacturerName(),
                            storageBin, movementQtyValue, loginUserID);

                    // Row insertion for Target
                    movementQtyValue = "P";
                    storageBin = createdInhouseTransferLine.getTargetStorageBin();
                    createInventoryMovementV2(createdInhouseTransferLine, transferTypeId, stockTypeId, itemCode, createdInhouseTransferLine.getManufacturerName(),
                            storageBin, movementQtyValue, loginUserID);
                }

                /* Response Header */
                BeanUtils.copyProperties(createdInhouseTransferHeader, responseHeader, CommonUtils.getNullPropertyNames(createdInhouseTransferHeader));
            }
        }

        responseHeader.setInhouseTransferLine(responseLines);
        return responseHeader;
    }

    /**
     * @param warehouseId
     * @return
     */
    private String getTransferNoV2(String companyCode, String plantId, String languageId, String warehouseId, String authToken) {
        String nextRangeNumber = getNextRangeNumber(8L, companyCode, plantId, languageId, warehouseId, authToken);
        return nextRangeNumber;
    }

    /**
     * @param createdInhouseTransferHeader
     * @param createdInhouseTransferLine
     * @param loginUserID
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void updateInventoryV2(InhouseTransferHeader createdInhouseTransferHeader,
                                   InhouseTransferLine createdInhouseTransferLine, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        Long transferTypeId = createdInhouseTransferHeader.getTransferTypeId();
        String transferMethod = createdInhouseTransferHeader.getTransferMethod();
        String warehouseId = createdInhouseTransferHeader.getWarehouseId();
        String itemCode = createdInhouseTransferLine.getSourceItemCode(); // sourceItemCode

        String companyCode = createdInhouseTransferHeader.getCompanyCodeId();
        String plantId = createdInhouseTransferHeader.getPlantId();
        String languageId = createdInhouseTransferHeader.getLanguageId();

        log.info("--------transferTypeId-------- : " + transferTypeId);
        log.info("--------transferMethod-------- : " + transferMethod);
        /*
         * 1 .TR_TYP_ID = 01, TR_MTD = ONESTEP
         * Pass WH_ID/ITM_CODE in INVENTORY TABLE and update STCK_TYP_ID with TGT_STCK_TYP_ID
         */
        if (transferTypeId == 1L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            List<InventoryV2> inventoryList =
                    inventoryService.getInventoryForInhouseTransferV2(companyCode, plantId, languageId, warehouseId,
                            itemCode, createdInhouseTransferHeader.getManufacturerName());
            log.info("-------------------inventoryList----------------- : " + inventoryList);
            for (InventoryV2 inventory : inventoryList) {
                log.info("inventory: " + inventory);
                inventory.setStockTypeId(createdInhouseTransferLine.getTargetStockTypeId());
//                Inventory updatedInventory = inventoryV2Repository.save(inventory);
                InventoryV2 newInventoryV2 = new InventoryV2();
                BeanUtils.copyProperties(inventory, newInventoryV2, CommonUtils.getNullPropertyNames(inventory));
                newInventoryV2.setInventoryId(System.currentTimeMillis());
                InventoryV2 createdInventoryV2 = inventoryV2Repository.save(newInventoryV2);
                log.info("InventoryV2 created : " + createdInventoryV2);
                log.info("transferTypeId: " + transferTypeId);
            }
        }

        /*
         * 2 .TR_TYP_ID = 02, TR_MTD = ONESTEP
         * Pass WH_ID/SRCE_ITM_CODE as ITM_CODE in INVENTORY TABLE and update SRC
         * E_ITM_CODE with TGT_ITM_CODE
         */
        if (transferTypeId == 2L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            List<InventoryV2> inventoryList =
                    inventoryService.getInventoryForInhouseTransferV2(companyCode, plantId, languageId, warehouseId,
                            createdInhouseTransferLine.getSourceItemCode(), createdInhouseTransferLine.getManufacturerName());
            for (InventoryV2 dbInventory : inventoryList) {
                // insert a record with target item code and delete the old record in Inventory table
                InventoryV2 newInventory = new InventoryV2();
                BeanUtils.copyProperties(dbInventory, newInventory, CommonUtils.getNullPropertyNames(dbInventory));
                newInventory.setInventoryId(System.currentTimeMillis());
                newInventory.setItemCode(createdInhouseTransferLine.getTargetItemCode());
                Inventory createdNewInventory = inventoryV2Repository.save(newInventory);
                log.info("createdNewInventory : " + createdNewInventory);

                // Delete the old record
                inventoryV2Repository.delete(dbInventory);
                log.info("dbInventory deleted.");
            }
        }

        /*
         * 3 .TR_TYP_ID = 03, TR_MTD=ONESTEP
         * Pass WH_ID/SRCE_ITM_CODE/PACK_BARCOE/SRCE_ST_BIN in INVENTORY TABLE and
         * update INV_QTY value (INV_QTY - TR_CNF_QTY) and delete the record if INV_QTY becomes Zero
         */
        if (transferTypeId == 3L && transferMethod.equalsIgnoreCase(ONESTEP)) {
            InventoryV2 inventorySourceItemCode =
                    inventoryService.getInventoryForInhouseTransferV2(companyCode, plantId, languageId, warehouseId,
                            createdInhouseTransferLine.getPackBarcodes(),
                            createdInhouseTransferLine.getSourceItemCode(),
                            createdInhouseTransferLine.getManufacturerName(),
                            createdInhouseTransferLine.getSourceStorageBin());
            log.info("---------inventory----------> : " + inventorySourceItemCode);
            if (inventorySourceItemCode != null) {
                Double inventoryQty = inventorySourceItemCode.getInventoryQuantity();
                Double ALLOC_QTY = 0D;
                if (inventorySourceItemCode.getAllocatedQuantity() != null) {
                    ALLOC_QTY = inventorySourceItemCode.getAllocatedQuantity();
                }
                Double transferConfirmedQty = createdInhouseTransferLine.getTransferConfirmedQty();
                double INV_QTY = inventoryQty - transferConfirmedQty;
                if (INV_QTY < 0) {
                    throw new BadRequestException("Inventory became negative." + INV_QTY);
                }
                log.info("-----Source----INV_QTY-----------> : " + INV_QTY);
                log.info("-----Source----ALLOC_QTY-----------> : " + ALLOC_QTY);
                inventorySourceItemCode.setInventoryQuantity(INV_QTY);
                inventorySourceItemCode.setAllocatedQuantity(ALLOC_QTY);
//                InventoryV2 updatedInventory = inventoryV2Repository.save(inventorySourceItemCode);
//                log.info("--------source---inventory-----updated----->" + updatedInventory);
                InventoryV2 newInventoryV2 = new InventoryV2();
                BeanUtils.copyProperties(inventorySourceItemCode, newInventoryV2, CommonUtils.getNullPropertyNames(inventorySourceItemCode));
                newInventoryV2.setUpdatedOn(new Date());
                newInventoryV2.setInventoryId(System.currentTimeMillis());
                Double totalQty = inventorySourceItemCode.getInventoryQuantity() + inventorySourceItemCode.getAllocatedQuantity();
                newInventoryV2.setReferenceField4(totalQty);
                InventoryV2 createdInventoryV2 = inventoryV2Repository.save(newInventoryV2);
                log.info("InventoryV2 created : " + createdInventoryV2);

                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
                if (INV_QTY == 0 && (inventorySourceItemCode.getAllocatedQuantity() == null || inventorySourceItemCode.getAllocatedQuantity() == 0D)) {
//                if (INV_QTY == 0) {
                    // Deleting record
//                    inventoryV2Repository.delete(inventorySourceItemCode);

                    InventoryV2 deleteInventoryV2 = new InventoryV2();
                    BeanUtils.copyProperties(inventorySourceItemCode, deleteInventoryV2, CommonUtils.getNullPropertyNames(inventorySourceItemCode));
                    deleteInventoryV2.setUpdatedOn(new Date());
                    deleteInventoryV2.setInventoryQuantity(0D);
                    deleteInventoryV2.setAllocatedQuantity(0D);
                    deleteInventoryV2.setReferenceField4(0D);
                    deleteInventoryV2.setInventoryId(System.currentTimeMillis());
                    InventoryV2 deletedInventoryV2 = inventoryV2Repository.save(deleteInventoryV2);
                    log.info("---------inventory-----deleted-----");
                    try {
                        StorageBinV2 dbStorageBin = mastersService.getStorageBinV2(createdInhouseTransferLine.getSourceStorageBin(),
                                createdInhouseTransferLine.getWarehouseId(),
                                companyCode, plantId, languageId, authTokenForMastersService.getAccess_token());

                        if (dbStorageBin != null) {
                            dbStorageBin.setStatusId(0L);
                            if (dbStorageBin.isCapacityCheck()) {
                                dbStorageBin.setRemainingVolume(dbStorageBin.getTotalVolume());
                                dbStorageBin.setOccupiedVolume("0");
                            }
                            dbStorageBin.setUpdatedBy(loginUserID);
                            dbStorageBin.setUpdatedOn(new Date());
                            mastersService.updateStorageBinV2(dbStorageBin.getStorageBin(), dbStorageBin, companyCode,
                                    plantId, languageId, warehouseId, loginUserID, authTokenForMastersService.getAccess_token());
//                        storageBinRepository.save(dbStorageBin);
                            log.info("---------storage bin updated-------" + dbStorageBin);
                        }
                    } catch (Exception e) {
                        log.error("---------storagebin-update-----" + e);
                    }

                }

                // Pass WH_ID/ TGT_ITM_CODE/PACK_BARCODE/TGT_ST_BIN in INVENTORY TABLE validate for a record.
                InventoryV2 inventoryTargetItemCode =
                        inventoryService.getInventoryForInhouseTransferV2(companyCode, plantId, languageId, warehouseId,
                                createdInhouseTransferLine.getPackBarcodes(),
                                createdInhouseTransferLine.getTargetItemCode(),
                                createdInhouseTransferLine.getManufacturerName(),
                                createdInhouseTransferLine.getTargetStorageBin());
                if (inventoryTargetItemCode != null) {
                    // update INV_QTY value (INV_QTY + TR_CNF_QTY)
                    inventoryQty = inventoryTargetItemCode.getInventoryQuantity();
                    ALLOC_QTY = 0D;
                    if (inventoryTargetItemCode.getAllocatedQuantity() != null) {
                        ALLOC_QTY = inventoryTargetItemCode.getAllocatedQuantity();
                    }
                    transferConfirmedQty = createdInhouseTransferLine.getTransferConfirmedQty();
                    INV_QTY = inventoryQty + transferConfirmedQty;
                    log.info("-----Target----INV_QTY-----------> : " + INV_QTY);
                    log.info("-----Target----ALLOC_QTY-----------> : " + ALLOC_QTY);

                    inventoryTargetItemCode.setInventoryQuantity(INV_QTY);
                    inventoryTargetItemCode.setAllocatedQuantity(ALLOC_QTY);
                    inventoryTargetItemCode.setBarcodeId(inventorySourceItemCode.getBarcodeId());
//                    InventoryV2 targetUpdatedInventory = inventoryV2Repository.save(inventoryTargetItemCode);
//                    log.info("------->updatedInventory : " + targetUpdatedInventory);
                    InventoryV2 newInventoryV2_1 = new InventoryV2();
                    BeanUtils.copyProperties(inventoryTargetItemCode, newInventoryV2_1, CommonUtils.getNullPropertyNames(inventoryTargetItemCode));
                    newInventoryV2_1.setUpdatedOn(new Date());
                    newInventoryV2_1.setInventoryId(System.currentTimeMillis());
                    newInventoryV2_1.setReferenceField4(inventoryTargetItemCode.getInventoryQuantity() + inventoryTargetItemCode.getAllocatedQuantity());
                    createdInventoryV2 = inventoryV2Repository.save(newInventoryV2_1);
                    log.info("InventoryV2 created : " + createdInventoryV2);
                } else {
                    /*
                     * Fetch from INHOUSETRANSFERLINE table and insert in INVENTORY table as
                     * WH_ID/ TGT_ITM_CODE/PAL_CODE/PACK_BARCODE/TGT_ST_BIN/CASE_CODE/STCK_TYP_ID/SP_ST_IND_ID/INV_QTY=TR_CNF_QTY/
                     * INV_UOM as QTY_UOM/BIN_CL_ID of ST_BIN from STORAGEBIN table
                     */

                    // "LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PACK_BARCODE", "ITM_CODE", "ST_BIN", "SP_ST_IND_ID"
                    InventoryV2 newInventory = new InventoryV2();
                    BeanUtils.copyProperties(createdInhouseTransferLine, newInventory, CommonUtils.getNullPropertyNames(createdInhouseTransferLine));

                    newInventory.setItemCode(createdInhouseTransferLine.getTargetItemCode());
                    newInventory.setPalletCode(createdInhouseTransferLine.getPalletCode());
                    newInventory.setPackBarcodes(createdInhouseTransferLine.getPackBarcodes());
                    newInventory.setStorageBin(createdInhouseTransferLine.getTargetStorageBin());
                    newInventory.setCaseCode(createdInhouseTransferLine.getCaseCode());
                    newInventory.setStockTypeId(createdInhouseTransferLine.getTargetStockTypeId());

                    String stockTypeDesc = getStockTypeDesc(companyCode, plantId, languageId, warehouseId, createdInhouseTransferLine.getSourceStockTypeId());
                    newInventory.setStockTypeDescription(stockTypeDesc);

                    IKeyValuePair description = stagingLineV2Repository.getDescription(companyCode,
                            languageId,
                            plantId,
                            warehouseId);

                    newInventory.setCompanyDescription(description.getCompanyDesc());
                    newInventory.setPlantDescription(description.getPlantDesc());
                    newInventory.setWarehouseDescription(description.getWarehouseDesc());

                    if (createdInhouseTransferLine.getSpecialStockIndicatorId() == null) {
                        newInventory.setSpecialStockIndicatorId(1L);
                    } else {
                        newInventory.setSpecialStockIndicatorId(createdInhouseTransferLine.getSpecialStockIndicatorId());
                    }

                    if (inventorySourceItemCode.getBarcodeId() != null) {
                        newInventory.setBarcodeId(inventorySourceItemCode.getBarcodeId());
                    }
                    List<String> barcode = stagingLineV2Repository.getPartnerItemBarcode(itemCode, companyCode, plantId, warehouseId,
                            createdInhouseTransferLine.getManufacturerName(), languageId);
                    log.info("Barcode : " + barcode);
                    if (inventorySourceItemCode.getBarcodeId() == null) {
                        if (barcode != null && !barcode.isEmpty()) {
                            newInventory.setBarcodeId(barcode.get(0));
                        }
                    }

                    newInventory.setInventoryQuantity(createdInhouseTransferLine.getTransferConfirmedQty());
                    newInventory.setAllocatedQuantity(0D);
                    newInventory.setReferenceField4(newInventory.getInventoryQuantity() + newInventory.getAllocatedQuantity());
                    log.info("INV_QTY--->ALLOC_QTY--->TOT_QTY----> : " + newInventory.getInventoryQuantity() + ", " + newInventory.getAllocatedQuantity() + ", " + newInventory.getReferenceField4());
                    newInventory.setInventoryUom(createdInhouseTransferLine.getTransferUom());

                    StorageBinV2 storageBin = mastersService.getStorageBinV2(createdInhouseTransferLine.getTargetStorageBin(),
                            createdInhouseTransferLine.getWarehouseId(), companyCode, plantId, languageId, authTokenForMastersService.getAccess_token());

                    ImBasicData imBasicData = new ImBasicData();
                    imBasicData.setCompanyCodeId(companyCode);
                    imBasicData.setPlantId(plantId);
                    imBasicData.setLanguageId(languageId);
                    imBasicData.setWarehouseId(warehouseId);
                    imBasicData.setItemCode(itemCode);
                    imBasicData.setManufacturerName(createdInhouseTransferLine.getManufacturerName());
                    ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
                    log.info("ImBasicData1 : " + imbasicdata1);
//                    ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(itemCode, languageId, companyCode, plantId, warehouseId,
//                            createdInhouseTransferLine.getManufacturerName(), authTokenForMastersService.getAccess_token());

                    if (imbasicdata1 != null) {
                        newInventory.setReferenceField8(imbasicdata1.getDescription());
                        newInventory.setReferenceField9(imbasicdata1.getManufacturerPartNo());
                        newInventory.setManufacturerCode(imbasicdata1.getManufacturerPartNo());
                        newInventory.setManufacturerName(imbasicdata1.getManufacturerPartNo());
                        newInventory.setDescription(imbasicdata1.getDescription());
                    }
                    if (storageBin != null) {
                        newInventory.setBinClassId(storageBin.getBinClassId());
                        newInventory.setReferenceField10(storageBin.getStorageSectionId());
                        newInventory.setReferenceField5(storageBin.getAisleNumber());
                        newInventory.setReferenceField6(storageBin.getShelfId());
                        newInventory.setReferenceField7(storageBin.getRowId());
                        newInventory.setLevelId(String.valueOf(storageBin.getFloorId()));
                    }
                    newInventory.setCreatedOn(new Date());
                    newInventory.setUpdatedOn(new Date());
                    newInventory.setInventoryId(System.currentTimeMillis());
                    InventoryV2 createdInventory = inventoryService.createInventoryV2(newInventory, loginUserID);
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
    public StorageBin getStorageBinV2(String companyCode, String plantId, String languageId, String warehouseId, String storageBin) {
        StorageBin storagebin = storageBinRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStorageBinAndDeletionIndicator(
                companyCode, plantId, languageId, warehouseId, storageBin, 0L);
        if (storagebin != null && storagebin.getDeletionIndicator() != null && storagebin.getDeletionIndicator() == 0) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + storageBin + " doesn't exist.");
        }
    }
}
