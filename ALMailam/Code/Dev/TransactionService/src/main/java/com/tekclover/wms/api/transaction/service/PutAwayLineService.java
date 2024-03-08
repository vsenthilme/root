package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.*;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.StorageBinPutAway;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.IInventoryImpl;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.*;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.SearchPutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.PutAwayLineImpl;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PutAwayLineSpecification;
import com.tekclover.wms.api.transaction.repository.specification.PutAwayLineV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private StagingLineService stagingLineService;

    @Autowired
    private StorageBinRepository storageBinRepository;

    @Autowired
    private GrLineService grLineService;

    String statusDescription = null;
    String stockTypeDesc = null;

    //--------------------------------------------------------------------------------------------

    /**
     * getPutAwayLines
     *
     * @return
     */
    public List<PutAwayLine> getPutAwayLines() {
        List<PutAwayLine> putAwayLineList = putAwayLineRepository.findAll();
        putAwayLineList = putAwayLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return putAwayLineList;
    }

    /**
     * WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE
     *
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @return
     */
    public List<PutAwayLine> getPutAwayLine(String warehouseId, String preInboundNo, String refDocNumber, Long lineNo, String itemCode) {
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
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public List<PutAwayLine> getPutAwayLine2(String warehouseId, String preInboundNo, String refDocNumber) {
        List<PutAwayLine> putAwayLine =
                putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        0L);
        if (putAwayLine.isEmpty()) {
            throw new BadRequestException("The given values in PutAwayLine: warehouseId:" + warehouseId +
                    ",preInboundNo: " + preInboundNo +
                    ",refDocNumber: " + refDocNumber +
                    " doesn't exist.");
        }

        return putAwayLine;
    }

    /**
     * getPutAwayLineByStatusId
     *
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public long getPutAwayLineByStatusId(String warehouseId, String preInboundNo, String refDocNumber) {
        long putAwayLineStatusIdCount = putAwayLineRepository.getPutawayLineCountByStatusId(getCompanyCode(), getPlantId(), warehouseId, preInboundNo, refDocNumber);
        return putAwayLineStatusIdCount;
    }

    /**
     * @param warehouseId
     * @param putAwayNumber
     * @param refDocNumber
     * @param statusId
     * @return
     */
    public long getPutAwayLineByStatusId(String warehouseId, String putAwayNumber, String refDocNumber, Long statusId) {
        long putAwayLineStatusIdCount =
                putAwayLineRepository.getPutawayLineCountByStatusId(getCompanyCode(), getPlantId(), warehouseId, putAwayNumber, refDocNumber, statusId);
        return putAwayLineStatusIdCount;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param putAwayNumber
     * @return
     */
    public List<PutAwayLine> getPutAwayLine(String warehouseId, String refDocNumber, String putAwayNumber) {
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
     *
     * @param confirmedStorageBin
     * @return
     */
    public PutAwayLine getPutAwayLine(String warehouseId, String goodsReceiptNo, String preInboundNo, String refDocNumber,
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
     * @param refDocNumber
     * @param packBarcodes
     * @return
     */
    public List<PutAwayLine> getPutAwayLine(String refDocNumber, String packBarcodes) {
        List<PutAwayLine> putAwayLine =
                putAwayLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
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
     *
     * @param newPutAwayLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PutAwayLine createPutAwayLine(AddPutAwayLine newPutAwayLine, String loginUserID)
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
                if (existingPutAwayLine.isEmpty()) {
                    PutAwayLine createdPutAwayLine = putAwayLineRepository.save(dbPutAwayLine);
                    log.info("---------->createdPutAwayLine created: " + createdPutAwayLine);
                    createdPutAwayLines.add(createdPutAwayLine);
                    boolean isInventoryCreated = false;
                    boolean isInventoryMovemoentCreated = false;
                    if (createdPutAwayLine != null && createdPutAwayLine.getPutawayConfirmedQty() > 0L) {
                        // Insert a record into INVENTORY table as below
                        Inventory inventory = new Inventory();
                        BeanUtils.copyProperties(createdPutAwayLine, inventory, CommonUtils.getNullPropertyNames(createdPutAwayLine));
                        inventory.setCompanyCodeId(createdPutAwayLine.getCompanyCode());
                        inventory.setVariantCode(1L);                // VAR_ID
                        inventory.setVariantSubCode("1");            // VAR_SUB_ID
                        inventory.setStorageMethod("1");            // STR_MTD
                        inventory.setBatchSerialNumber("1");        // STR_NO
                        inventory.setBatchSerialNumber(newPutAwayLine.getBatchSerialNumber());
                        inventory.setStorageBin(createdPutAwayLine.getConfirmedStorageBin());

                        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
                        StorageBin dbStorageBin =
                                mastersService.getStorageBin(dbPutAwayLine.getConfirmedStorageBin(),
                                        dbPutAwayLine.getWarehouseId(),
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
                        InventoryMovement createdInventoryMovement = createInventoryMovement(createdPutAwayLine);
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
                            updateInboundLine(createdPutAwayLine);
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
     * @param createdPutAwayLine
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void updateInboundLine(PutAwayLine createdPutAwayLine) {
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
                inventoryService.getInventory(dbPutAwayLine.getWarehouseId(), dbPutAwayLine.getItemCode(), 1L);
        double sumOfInvQty = inventoryList.stream().mapToDouble(a -> a.getInventoryQuantity()).sum();
        log.info("BalanceOhQty: " + sumOfInvQty);
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
     *
     * @param loginUserID
     * @param confirmedStorageBin
     * @param updatePutAwayLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PutAwayLine updatePutAwayLine(String warehouseId, String goodsReceiptNo, String preInboundNo, String refDocNumber,
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
     * @param updatePutAwayLine
     * @param loginUserID
     * @return
     */
    public PutAwayLine updatePutAwayLine(UpdatePutAwayLine updatePutAwayLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PutAwayLine dbPutAwayLine = new PutAwayLine();
        BeanUtils.copyProperties(updatePutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(updatePutAwayLine));
        dbPutAwayLine.setUpdatedBy(loginUserID);
        dbPutAwayLine.setUpdatedOn(new Date());
        return putAwayLineRepository.save(dbPutAwayLine);
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<PutAwayLine> putAwayLines = getPutAwayLines();
        putAwayLines.forEach(p -> p.setReferenceField1(asnNumber));
        putAwayLineRepository.saveAll(putAwayLines);
    }

    /**
     * deletePutAwayLine
     *
     * @param loginUserID
     * @param confirmedStorageBin
     */
    public void deletePutAwayLine(String languageId, String companyCodeId, String plantId, String warehouseId,
                                  String goodsReceiptNo, String preInboundNo, String refDocNumber, String putAwayNumber, Long lineNo,
                                  String itemCode, String proposedStorageBin, String confirmedStorageBin, String loginUserID) {
        PutAwayLine putAwayLine = getPutAwayLine(warehouseId, goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber,
                lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
        if (putAwayLine != null) {
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
     * @param companyId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public double getSumOfPutawayLineQtyByStatusId20(String companyId, String plantId, String languageId, String warehouseId,
                                                     String preInboundNo, String refDocNumber) {
        double sumOfPutAwayLineQty =
                putAwayLineV2Repository.getSumOfPutawayLineQtyByStatusId20(companyId, plantId, warehouseId, languageId, preInboundNo, refDocNumber);
        return sumOfPutAwayLineQty;
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
     *
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @param putAwayNumber
     * @return
     */
    public List<PutAwayLineV2> getPutAwayLineV2ForReversal(String companyCode, String plantId, String languageId,
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
        return putAwayLine;
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param storageBin
     * @return
     */
    public List<PutAwayLineV2> getPutAwayLineForPerpetualCountV2(String companyCode, String plantId, String languageId, String warehouseId,
                                                                 String itemCode, String manufacturerName, String storageBin, Date stockCountDate) {
        List<PutAwayLineV2> putAwayLine =
                putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerNameAndProposedStorageBinAndStatusIdAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        itemCode,
                        manufacturerName,
                        storageBin,
                        20L,
                        0L);
        if (putAwayLine == null || putAwayLine.isEmpty()) {
            return null;
        }

        return putAwayLine;
    }

    public List<PutAwayLineV2> getPutAwayLineV2ForPutawayConfirm(String companyCode, String plantId, String languageId,
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
            return null;
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
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @return
     */
    public List<PutAwayLineV2> getPutAwayLineForInboundConfirmV2(String companyCode, String plantId, String languageId, String warehouseId, String refDocNumber) {
        List<PutAwayLineV2> putAwayLine =
                putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndStatusIdAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        20L,
                        0L);
        if (putAwayLine.isEmpty()) {
            return null;
        }
        return putAwayLine;
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @param itemCode
     * @param manufacturerName
     * @param lineNumber
     * @param preInboundNo
     * @return
     */
//    public PutAwayLineV2 getPutAwayLineForInboundConfirmV2(String companyCode, String plantId, String languageId, String warehouseId,
//                                                           String refDocNumber, String itemCode, String manufacturerName,
//                                                           Long lineNumber, String preInboundNo, String packBarcodes) {
//        PutAwayLineV2 putAwayLine =
//                putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndItemCodeAndManufacturerNameAndLineNoAndStatusIdAndPackBarcodesAndDeletionIndicator(
//                        languageId,
//                        companyCode,
//                        plantId,
//                        warehouseId,
//                        refDocNumber,
//                        preInboundNo,
//                        itemCode,
//                        manufacturerName,
//                        lineNumber,
//                        20L,
//                        packBarcodes,
//                        0L);
//        if (putAwayLine == null) {
//            return null;
//        }
//        return putAwayLine;
//    }
    public List<PutAwayLineV2> getPutAwayLineForInboundConfirmV2(String companyCode, String plantId, String languageId, String warehouseId,
                                                                 String refDocNumber, String itemCode, String manufacturerName,
                                                                 Long lineNumber, String preInboundNo, String packBarcodes) {
        List<PutAwayLineV2> putAwayLine =
                putAwayLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndItemCodeAndManufacturerNameAndLineNoAndStatusIdAndPackBarcodesAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        itemCode,
                        manufacturerName,
                        lineNumber,
                        20L,
                        packBarcodes,
                        0L);
        if (putAwayLine == null) {
            return null;
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
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public List<PutAwayLineV2> putAwayLineConfirmV2(@Valid List<PutAwayLineV2> newPutAwayLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        List<PutAwayLineV2> createdPutAwayLines = new ArrayList<>();
        log.info("newPutAwayLines to confirm : " + newPutAwayLines);
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
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
//                PutAwayHeaderV2 dbPutAwayHeader = new PutAwayHeaderV2();

                String itemCode = newPutAwayLine.getItemCode();
                String companyCode = newPutAwayLine.getCompanyCode();
                String plantId = newPutAwayLine.getPlantId();
                String languageId = newPutAwayLine.getLanguageId();
                String warehouseId = newPutAwayLine.getWarehouseId();

                Double cbmPerQuantity = 0D;
                Double cbm = 0D;
                Double allocatedVolume = 0D;
                Double occupiedVolume = 0D;
                Double remainingVolume = 0D;
                Double totalVolume = 0D;

                Double allocateQty = 0D;
                Double orderedQty = 0D;
                Double differenceQty = 0D;
                Double assignedProposedBinVolume = 0D;

                Long statusId = 0L;

                boolean capacityCheck = false;
                boolean storageBinCapacityCheck = false;

                ImBasicData imBasicData = new ImBasicData();
                imBasicData.setCompanyCodeId(companyCode);
                imBasicData.setPlantId(plantId);
                imBasicData.setLanguageId(languageId);
                imBasicData.setWarehouseId(warehouseId);
                imBasicData.setItemCode(itemCode);
                imBasicData.setManufacturerName(newPutAwayLine.getManufacturerName());
                ImBasicData1 itemCodeCapacityCheck = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
                log.info("ImbasicData1 : " + itemCodeCapacityCheck);

                if (itemCodeCapacityCheck != null) {
                if (itemCodeCapacityCheck.getCapacityCheck() != null) {
                    capacityCheck = itemCodeCapacityCheck.getCapacityCheck();
                    log.info("capacity Check: " + capacityCheck);
                }
                }

                String confirmedStorageBin = newPutAwayLine.getConfirmedStorageBin();
                String proposedStorageBin = newPutAwayLine.getProposedStorageBin();
                log.info("proposedBin, confirmedBin: " + newPutAwayLine.getProposedStorageBin() + ", " + newPutAwayLine.getConfirmedStorageBin());

                StorageBinV2 storageBin = storageBinRepository.getStorageBin(companyCode, plantId, languageId, warehouseId, newPutAwayLine.getConfirmedStorageBin());
                StorageBinV2 proposedBin = storageBinRepository.getStorageBin(companyCode, plantId, languageId, warehouseId, newPutAwayLine.getProposedStorageBin());

                PutAwayHeaderV2 findPutawayHeader = putAwayHeaderService.getPutawayHeaderV2(companyCode, plantId, warehouseId, languageId, newPutAwayLine.getPutAwayNumber());
                List<PutAwayLineV2> findPutawayLine = getPutAwayLineV2ForPutawayConfirm(companyCode, plantId, languageId, warehouseId, newPutAwayLine.getRefDocNumber(), newPutAwayLine.getPutAwayNumber());

                if (storageBin != null) {
                    if (storageBin.isCapacityCheck()) {
                        storageBinCapacityCheck = storageBin.isCapacityCheck();
                        log.info("confirmed storageBinCapacityCheck: " + storageBinCapacityCheck);
                    }
                }

                if (capacityCheck && !storageBinCapacityCheck) {
                    throw new BadRequestException("Selected Bin is not under Capacity Check. Kindly Select a Capacity Enabled Bin!");
                }
                if (!capacityCheck && storageBinCapacityCheck) {
                    throw new BadRequestException("Selected ItemCode is not under Capacity Check. Kindly Select a Capacity Enabled Item!");
                }
//                if (!confirmedStorageBin.equalsIgnoreCase(proposedStorageBin)) {
//                    if (storageBin.getStatusId() == 1 && storageBin.getBinClassId() != 7) {
//                        log.info("confirmed storageBin is Not Empty: " + storageBin.getStorageBin());
//                        List<InventoryV2> stBinInventoryList = inventoryService.getInventoryForPutawayHeader(itemCode, newPutAwayLine.getManufacturerName(), storageBin.getBinClassId(), companyCode, plantId, languageId, warehouseId);
//                        List<PutAwayHeaderV2> stBinPutawayHeaderList = putAwayHeaderService.getPutAwayHeaderForPutAwayConfirm(companyCode, plantId, languageId, warehouseId, newPutAwayLine.getRefDocNumber(), newPutAwayLine.getPreInboundNo());
//                        if (stBinInventoryList != null) {
//                            log.info("Item present in confirmed storageBin : " + stBinInventoryList.get(0));
//                        }
//                        if (stBinInventoryList == null && stBinPutawayHeaderList == null) {
//                            throw new BadRequestException("Selected Bin is not empty and item present in the bin is not same as selected. Kindly Select a different Bin!");
//                        }
//                    }
//                }

                if (capacityCheck && storageBinCapacityCheck) {

                    if (!confirmedStorageBin.equalsIgnoreCase(proposedStorageBin)) {
                        log.info("confirmedStorageBin != proposedBin: " + confirmedStorageBin + ", " + proposedStorageBin);

                        if (newPutAwayLine.getCbmQuantity() != null) {
                            cbmPerQuantity = newPutAwayLine.getCbmQuantity();
                        }
                        if (newPutAwayLine.getCbm() != null && newPutAwayLine.getCbm() != "") {
                            cbm = Double.valueOf(newPutAwayLine.getCbm());
                        }
                        if (storageBin.getTotalVolume() != null && storageBin.getTotalVolume() != "") {
                            totalVolume = Double.valueOf(storageBin.getTotalVolume());
                        }
                        if (storageBin.getAllocatedVolume() != null) {
                            allocatedVolume = Double.valueOf(storageBin.getAllocatedVolume());
                        }
                        if (storageBin.getOccupiedVolume() != null && storageBin.getOccupiedVolume() != "") {
                            occupiedVolume = Double.valueOf(storageBin.getOccupiedVolume());
                        }
                        if (storageBin.getRemainingVolume() != null && storageBin.getRemainingVolume() != "") {
                            remainingVolume = Double.valueOf(storageBin.getRemainingVolume());
                        }

                        if (remainingVolume <= 0) {
                            throw new BadRequestException("Selected Bin doesn't have required space to store the selected quantity. Kindly Select a different Bin!");
                        }

                        allocateQty = newPutAwayLine.getPutawayConfirmedQty();

                        if (remainingVolume < cbmPerQuantity) {
                            throw new BadRequestException("Selected Bin doesn't have required space to store the selected quantity. Kindly Select a different Bin!");
                        }

                        allocatedVolume = allocateQty * cbmPerQuantity;
                        if (allocatedVolume <= remainingVolume) {
                            allocatedVolume = allocateQty * cbmPerQuantity;
                        } else {
                            throw new BadRequestException("Selected Bin doesn't have required space to store the selected quantity. Kindly Select a different Bin!");
                        }
                        if (totalVolume >= remainingVolume) {
                            remainingVolume = totalVolume - (allocatedVolume + occupiedVolume);
                        } else {
                            remainingVolume = remainingVolume - allocatedVolume;
                        }
                        occupiedVolume = occupiedVolume + allocatedVolume;

                        log.info("remainingVolume, occupiedVolume: " + remainingVolume + ", " + occupiedVolume);

                        if ((occupiedVolume == 0 || occupiedVolume == 0D || occupiedVolume == 0.0) && remainingVolume.equals(totalVolume)) {
                            log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                            statusId = 0L;
                            log.info("StorageBin Emptied");
                        } else {
                            log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                            statusId = 1L;
                            log.info("StorageBin Occupied");
                        }

                        //confirmed Bin volume update
                        updateStorageBin(remainingVolume, occupiedVolume, allocatedVolume, newPutAwayLine.getConfirmedStorageBin(),
                                companyCode, plantId, languageId, warehouseId, statusId, loginUserID, authTokenForMastersService.getAccess_token());

                        if (findPutawayLine == null) {
                            //proposed Bin revert volume update done during putaway header create
                            remainingVolume = Double.valueOf(proposedBin.getRemainingVolume());
//                            allocatedVolume = proposedBin.getAllocatedVolume();
                            occupiedVolume = Double.valueOf(proposedBin.getOccupiedVolume());
                            totalVolume = Double.valueOf(proposedBin.getTotalVolume());
                            log.info("proposed Bin before confirm remainingVolume, occupiedVolume: " + remainingVolume + ", " + occupiedVolume);

                            remainingVolume = remainingVolume + allocatedVolume;
                            occupiedVolume = occupiedVolume - allocatedVolume;

                            log.info("proposed bin after confirm remainingVolume, occupiedVolume: " + remainingVolume + ", " + occupiedVolume);

                            if ((occupiedVolume == 0 || occupiedVolume == 0D || occupiedVolume == 0.0) && remainingVolume.equals(totalVolume)) {
                                log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                                statusId = 0L;
                                log.info("StorageBin Emptied");
                            } else {
                                log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                                statusId = 1L;
                                log.info("StorageBin Occupied");
                            }

                            updateStorageBin(remainingVolume, occupiedVolume, allocatedVolume, newPutAwayLine.getProposedStorageBin(),
                                    companyCode, plantId, languageId, warehouseId, statusId, loginUserID, authTokenForMastersService.getAccess_token());
                        }

                        log.info("Storage Bin occupied volume got updated");

                    }
                    if (confirmedStorageBin.equalsIgnoreCase(proposedStorageBin)) {
                        log.info("confirmedStorageBin == proposedBin" + confirmedStorageBin + ", " + proposedStorageBin);

                        if (findPutawayHeader.getPutAwayQuantity() > newPutAwayLine.getPutawayConfirmedQty()) {
                            log.info("putAwayQty > confirmQty" + findPutawayHeader.getPutAwayQuantity() + ", " + newPutAwayLine.getPutawayConfirmedQty());

                            if (newPutAwayLine.getCbmQuantity() != null) {
                                cbmPerQuantity = newPutAwayLine.getCbmQuantity();
                            }
                            if (newPutAwayLine.getCbm() != null && newPutAwayLine.getCbm() != "") {
                                cbm = Double.valueOf(newPutAwayLine.getCbm());
                            }
                            if (proposedBin.getTotalVolume() != null && proposedBin.getTotalVolume() != "") {
                                totalVolume = Double.valueOf(proposedBin.getTotalVolume());
                            }
                            if (proposedBin.getAllocatedVolume() != null) {
                                allocatedVolume = Double.valueOf(proposedBin.getAllocatedVolume());
                            }
                            if (proposedBin.getOccupiedVolume() != null && proposedBin.getOccupiedVolume() != "") {
                                occupiedVolume = Double.valueOf(proposedBin.getOccupiedVolume());
                            }
                            if (proposedBin.getRemainingVolume() != null && proposedBin.getRemainingVolume() != "") {
                                remainingVolume = Double.valueOf(proposedBin.getRemainingVolume());
                            }

                            allocateQty = newPutAwayLine.getPutawayConfirmedQty();
                            if (newPutAwayLine.getOrderQty() != null) {
                                orderedQty = newPutAwayLine.getOrderQty();
                            }
                            log.info("allocateQty(confirmed PutawayQty), putawayQty, orderQty: " + allocateQty + ", " + findPutawayHeader.getPutAwayQuantity() + ", " + orderedQty);

                            assignedProposedBinVolume = findPutawayHeader.getPutAwayQuantity() * cbmPerQuantity;
                            allocatedVolume = allocateQty * cbmPerQuantity;

                            log.info("assignedProposedBinVolume, allocatedVolume: " + assignedProposedBinVolume + ", " + allocatedVolume);

                            remainingVolume = remainingVolume + assignedProposedBinVolume - allocatedVolume;
                            occupiedVolume = occupiedVolume - assignedProposedBinVolume + allocatedVolume;

                            log.info("remainingVolume, occupiedVolume: " + remainingVolume + ", " + occupiedVolume);

                            if ((occupiedVolume == 0 || occupiedVolume == 0D || occupiedVolume == 0.0) && remainingVolume.equals(totalVolume)) {
                                log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                                statusId = 0L;
                                log.info("StorageBin Emptied");
                            } else {
                                log.info("occupiedVolume,remainingVolume,totalVolume: " + occupiedVolume + ", " + remainingVolume + "," + totalVolume);
                                statusId = 1L;
                                log.info("StorageBin Occupied");
                            }

                            //confirmed Bin volume update
                            updateStorageBin(remainingVolume, occupiedVolume, allocatedVolume, newPutAwayLine.getConfirmedStorageBin(),
                                    companyCode, plantId, languageId, warehouseId, statusId, loginUserID, authTokenForMastersService.getAccess_token());

                            log.info("Storage Bin occupied volume got updated");

                        }
                    }
                }
                //this code is set for mobile device to work
//                if (newPutAwayLine.getCompanyCode() == null || newPutAwayLine.getBarcodeId() == null || newPutAwayLine.getManufacturerName() == null || newPutAwayLine.getPutAwayUom() == null) {
//                    dbPutAwayHeader = putAwayHeaderService.getPutawayHeaderV2(newPutAwayLine.getPutAwayNumber());
//                    newPutAwayLine.setCompanyCode(dbPutAwayHeader.getCompanyCodeId());
//                    newPutAwayLine.setBarcodeId(dbPutAwayHeader.getBarcodeId());
//                    newPutAwayLine.setManufacturerName(dbPutAwayHeader.getManufacturerName());
//                    newPutAwayLine.setPutAwayUom(dbPutAwayHeader.getPutAwayUom());
//                }
//                if (newPutAwayLine.getBarcodeId() == null) {
//                    newPutAwayLine.setBarcodeId(dbPutAwayHeader.getBarcodeId());
//                }
//                if (newPutAwayLine.getManufacturerName() == null) {
//                    newPutAwayLine.setManufacturerName(dbPutAwayHeader.getManufacturerName());
//                }
//                if (newPutAwayLine.getPutAwayUom() == null) {
//                    newPutAwayLine.setPutAwayUom(dbPutAwayHeader.getPutAwayUom());
//                }

                //V2 Code
                IKeyValuePair description = stagingLineV2Repository.getDescription(companyCode,
                        languageId,
                        plantId,
                        warehouseId);

                newPutAwayLine.setCompanyDescription(description.getCompanyDesc());
                newPutAwayLine.setPlantDescription(description.getPlantDesc());
                newPutAwayLine.setWarehouseDescription(description.getWarehouseDesc());

                StagingLineEntityV2 dbStagingLineEntity = stagingLineService.getStagingLineForPutAwayLineV2(companyCode, plantId, languageId, warehouseId,
                        newPutAwayLine.getPreInboundNo(), newPutAwayLine.getRefDocNumber(), newPutAwayLine.getLineNo(), itemCode, newPutAwayLine.getManufacturerName());
                log.info("StagingLine: " + dbStagingLineEntity);
                if (dbStagingLineEntity != null) {
                    if (newPutAwayLine.getManufacturerFullName() != null) {
                        newPutAwayLine.setManufacturerFullName(newPutAwayLine.getManufacturerFullName());
                    } else {
                        newPutAwayLine.setManufacturerFullName(dbStagingLineEntity.getManufacturerFullName());
                    }
                    if (newPutAwayLine.getMiddlewareId() != null) {
                        newPutAwayLine.setMiddlewareId(newPutAwayLine.getMiddlewareId());
                    } else {
                        newPutAwayLine.setMiddlewareId(dbStagingLineEntity.getMiddlewareId());
                    }
                    if (newPutAwayLine.getMiddlewareHeaderId() != null) {
                        newPutAwayLine.setMiddlewareHeaderId(newPutAwayLine.getMiddlewareHeaderId());
                    } else {
                        newPutAwayLine.setMiddlewareHeaderId(dbStagingLineEntity.getMiddlewareHeaderId());
                    }
                    if (newPutAwayLine.getMiddlewareTable() != null) {
                        newPutAwayLine.setMiddlewareTable(newPutAwayLine.getMiddlewareTable());
                    } else {
                        newPutAwayLine.setMiddlewareTable(dbStagingLineEntity.getMiddlewareTable());
                    }
                    if (newPutAwayLine.getPurchaseOrderNumber() != null) {
                        newPutAwayLine.setPurchaseOrderNumber(newPutAwayLine.getPurchaseOrderNumber());
                    } else {
                        newPutAwayLine.setPurchaseOrderNumber(dbStagingLineEntity.getPurchaseOrderNumber());
                    }
                    newPutAwayLine.setReferenceDocumentType(dbStagingLineEntity.getReferenceDocumentType());
                    newPutAwayLine.setPutAwayUom(dbStagingLineEntity.getOrderUom());
                    newPutAwayLine.setDescription(dbStagingLineEntity.getItemDescription());
                }

//                Warehouse warehouse = getWarehouse(newPutAwayLine.getWarehouseId(),
//                        newPutAwayLine.getCompanyCode(),
//                        newPutAwayLine.getPlantId(),
//                        newPutAwayLine.getLanguageId());

                BeanUtils.copyProperties(newPutAwayLine, dbPutAwayLine, CommonUtils.getNullPropertyNames(newPutAwayLine));
//                if (newPutAwayLine.getCompanyCode() == null) {
//                    dbPutAwayLine.setCompanyCode(warehouse.getCompanyCode());
//                } else {
                    dbPutAwayLine.setCompanyCode(newPutAwayLine.getCompanyCode());
//                }

                dbPutAwayLine.setBranchCode(newPutAwayLine.getBranchCode());
                dbPutAwayLine.setTransferOrderNo(newPutAwayLine.getTransferOrderNo());
                dbPutAwayLine.setIsCompleted(newPutAwayLine.getIsCompleted());

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
                dbPutAwayLine.setConfirmedBy(loginUserID);

                log.info("putawayHeader: " + findPutawayHeader);
                if(findPutawayHeader != null) {
                    dbPutAwayLine.setCreatedOn(findPutawayHeader.getCreatedOn());
                    dbPutAwayLine.setPutAwayQuantity(findPutawayHeader.getPutAwayQuantity());
                    dbPutAwayLine.setInboundOrderTypeId(findPutawayHeader.getInboundOrderTypeId());
                } else {
                    dbPutAwayLine.setCreatedOn(new Date());
                }
                dbPutAwayLine.setUpdatedOn(new Date());
                dbPutAwayLine.setConfirmedOn(new Date());

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

                    try {
                        String leadTime = putAwayLineV2Repository.getleadtime(companyCode, plantId, languageId, warehouseId, newPutAwayLine.getPutAwayNumber(), new Date());
                        dbPutAwayLine.setReferenceField1(leadTime);
                        log.info("LeadTime: " + leadTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    PutAwayLineV2 createdPutAwayLine = putAwayLineV2Repository.save(dbPutAwayLine);

                    log.info("---------->createdPutAwayLine created: " + createdPutAwayLine);

                    createdPutAwayLines.add(createdPutAwayLine);

//                    boolean isInventoryCreated = false;
//                    boolean isInventoryMovemoentCreated = false;

                    if (createdPutAwayLine != null && createdPutAwayLine.getPutawayConfirmedQty() > 0L) {
                        // Insert a record into INVENTORY table as below
                        /*
                         * Commenting out Inventory creation alone
                         */
//                        InventoryV2 inventory = new InventoryV2();
//                        BeanUtils.copyProperties(createdPutAwayLine, inventory, CommonUtils.getNullPropertyNames(createdPutAwayLine));
//                        inventory.setInventoryId(System.currentTimeMillis());
//                        inventory.setCompanyCodeId(createdPutAwayLine.getCompanyCode());
//                        inventory.setVariantCode(1L);                // VAR_ID
//                        inventory.setVariantSubCode("1");            // VAR_SUB_ID
//                        inventory.setStorageMethod("1");            // STR_MTD
//                        inventory.setBatchSerialNumber("1");        // STR_NO
//                        inventory.setBatchSerialNumber(newPutAwayLine.getBatchSerialNumber());
//                        inventory.setStorageBin(createdPutAwayLine.getConfirmedStorageBin());
//                        inventory.setBarcodeId(createdPutAwayLine.getBarcodeId());

                        //v2 code
//                        if (createdPutAwayLine.getCbm() == null) {
//                            createdPutAwayLine.setCbm("0");
//                        }
//                        if (createdPutAwayLine.getCbmQuantity() == null) {
//                            createdPutAwayLine.setCbmQuantity(0D);
//                        }
//                        inventory.setCbmPerQuantity(String.valueOf(Double.valueOf(createdPutAwayLine.getCbm()) / createdPutAwayLine.getCbmQuantity()));
//                        inventory.setCbmPerQuantity(String.valueOf(createdPutAwayLine.getCbmQuantity()));

//                        Double invQty = 0D;
//                        Double cbmPerQty = 0D;
//                        Double invCbm = 0D;
//
//                        log.info("CapacityCheck for Create Inventory-----------> : " + capacityCheck);

//                        if (capacityCheck) {
//                            if (createdPutAwayLine.getCbmQuantity() != null) {
//                                inventory.setCbmPerQuantity(String.valueOf(createdPutAwayLine.getCbmQuantity()));
//                            }
//                            if (createdPutAwayLine.getPutawayConfirmedQty() != null) {
//                                invQty = createdPutAwayLine.getPutawayConfirmedQty();
//                            }
//                            if (createdPutAwayLine.getCbmQuantity() == null) {
//
//                                if (createdPutAwayLine.getCbm() != null) {
//                                    cbm = Double.valueOf(createdPutAwayLine.getCbm());
//                                }
//                                cbmPerQty = cbm / invQty;
//                                inventory.setCbmPerQuantity(String.valueOf(cbmPerQty));
//                            }
//                            if (createdPutAwayLine.getCbm() != null) {
//                                invCbm = Double.valueOf(createdPutAwayLine.getCbm());
//                            }
//                            if (createdPutAwayLine.getCbm() == null) {
//                                invCbm = invQty * Double.valueOf(inventory.getCbmPerQuantity());
//                            }
//                            inventory.setCbm(String.valueOf(invCbm));
//                        }

                        StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
                        storageBinPutAway.setCompanyCodeId(dbPutAwayLine.getCompanyCode());
                        storageBinPutAway.setPlantId(dbPutAwayLine.getPlantId());
                        storageBinPutAway.setLanguageId(dbPutAwayLine.getLanguageId());
                        storageBinPutAway.setWarehouseId(dbPutAwayLine.getWarehouseId());
                        storageBinPutAway.setBin(dbPutAwayLine.getConfirmedStorageBin());

                        StorageBinV2 dbStorageBin = null;
                        try {
                            dbStorageBin = mastersService.getaStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
                        } catch (Exception e) {
                            throw new BadRequestException("Invalid StorageBin");
                        }

//                        List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(inventory.getItemCode());
//                        if (imbasicdata1 != null && !imbasicdata1.isEmpty()) {
//                            inventory.setReferenceField8(imbasicdata1.get(0).getDescription());
//                            inventory.setReferenceField9(imbasicdata1.get(0).getManufacturePart());
//                        }
//                        if (itemCodeCapacityCheck != null) {
//                            inventory.setReferenceField8(itemCodeCapacityCheck.getDescription());
//                            inventory.setReferenceField9(itemCodeCapacityCheck.getManufacturerPartNo());
//                            inventory.setDescription(itemCodeCapacityCheck.getDescription());
//                        }

//                        if (dbStorageBin != null) {
//                            inventory.setBinClassId(dbStorageBin.getBinClassId());
//                            inventory.setReferenceField10(dbStorageBin.getStorageSectionId());
//                            inventory.setReferenceField5(dbStorageBin.getAisleNumber());
//                            inventory.setReferenceField6(dbStorageBin.getShelfId());
//                            inventory.setReferenceField7(dbStorageBin.getRowId());
//                            inventory.setLevelId(String.valueOf(dbStorageBin.getFloorId()));
//                        }

//                        inventory.setCompanyDescription(dbPutAwayLine.getCompanyDescription());
//                        inventory.setPlantDescription(dbPutAwayLine.getPlantDescription());
//                        inventory.setWarehouseDescription(dbPutAwayLine.getWarehouseDescription());

                        /*
                         * Insert PA_CNF_QTY value in this field.
                         * Also Pass WH_ID/PACK_BARCODE/ITM_CODE/BIN_CL_ID=3 in INVENTORY table and fetch ST_BIN/INV_QTY value.
                         * Update INV_QTY value by (INV_QTY - PA_CNF_QTY) . If this value becomes Zero, then delete the record"
                         */
//                        try {
//                            InventoryV2 existinginventory = inventoryService.getInventory(
//                                    createdPutAwayLine.getCompanyCode(), createdPutAwayLine.getPlantId(), createdPutAwayLine.getLanguageId(),
//                                    createdPutAwayLine.getWarehouseId(),
//                                    createdPutAwayLine.getPackBarcodes(), createdPutAwayLine.getItemCode(),
//                                    createdPutAwayLine.getManufacturerName(), 3L);
//
////                            IInventoryImpl existinginventory = inventoryService.getInventoryforExistingBin(
////                                    createdPutAwayLine.getCompanyCode(), createdPutAwayLine.getPlantId(), createdPutAwayLine.getLanguageId(),
////                                    createdPutAwayLine.getWarehouseId(),
////                                    createdPutAwayLine.getPackBarcodes(), createdPutAwayLine.getItemCode(),
////                                    createdPutAwayLine.getManufacturerName(), 3L);
//
//                            double INV_QTY = existinginventory.getInventoryQuantity() - createdPutAwayLine.getPutawayConfirmedQty();
//                            log.info("INV_QTY : " + INV_QTY);
//
////                            inventory.setPalletCode(existinginventory.getPalletCode());
////                            inventory.setCaseCode(existinginventory.getCaseCode());
////                            inventory.setDescription(itemCodeCapacityCheck.getDescription());
//
////                            if (capacityCheck) {
////                                if (existinginventory.getCbm() != null && createdPutAwayLine.getCbm() != null) {
////                                    invCbm = Double.valueOf(existinginventory.getCbm()) - Double.valueOf(createdPutAwayLine.getCbm());
////                                    log.info("INV_CBM: " + invCbm);
////                                }
////                                if (invCbm >= 0) {
////                                    existinginventory.setCbm(String.valueOf(invCbm));
////                                }
////                            }
//                            if (INV_QTY >= 0) {
////                                existinginventory.setInventoryQuantity(INV_QTY);
////                                InventoryV2 updatedInventory = inventoryV2Repository.save(existinginventory);
////                                log.info("updatedInventory--------> : " + updatedInventory);
//                                InventoryV2 inventory2 = new InventoryV2();
//                                BeanUtils.copyProperties(existinginventory, inventory2, CommonUtils.getNullPropertyNames(existinginventory));
//                                stockTypeDesc = getStockTypeDesc(createdPutAwayLine.getCompanyCode(), createdPutAwayLine.getPlantId(), createdPutAwayLine.getLanguageId(), createdPutAwayLine.getWarehouseId(), existinginventory.getStockTypeId());
//                                inventory2.setStockTypeDescription(stockTypeDesc);
//                                inventory2.setInventoryQuantity(INV_QTY);
//                                inventory2.setInventoryId(System.currentTimeMillis());
//                                InventoryV2 createdInventoryV2 = inventoryV2Repository.save(inventory2);
//                                log.info("----existinginventory--createdInventoryV2--------> : " + createdInventoryV2);
//                            }
//
//                        } catch (Exception e) {
//                            log.info("Existing Inventory---Error-----> : " + e.toString());
//                        }

                        // INV_QTY
//                        inventory.setInventoryQuantity(createdPutAwayLine.getPutawayConfirmedQty());

//                        inventory.setStockTypeId(10L);
//                        stockTypeDesc = getStockTypeDesc(dbPutAwayLine.getCompanyCode(),dbPutAwayLine.getPlantId(),dbPutAwayLine.getLanguageId(),dbPutAwayLine.getWarehouseId(),10L);
//                        inventory.setStockTypeDescription(stockTypeDesc);

                        // INV_UOM
//                        inventory.setInventoryUom(createdPutAwayLine.getPutAwayUom());
//                        inventory.setReferenceDocumentNo(createdPutAwayLine.getRefDocNumber());
//                        inventory.setDescription(itemCodeCapacityCheck.getDescription());
//                        inventory.setCreatedBy(createdPutAwayLine.getCreatedBy());
//                        inventory.setCreatedOn(createdPutAwayLine.getCreatedOn());
                        //InventoryV2 createdInventory = inventoryV2Repository.save(inventory);
//                        log.info("createdInventory : " + createdInventory);
//                        log.info("createdInventory BinClassId : " + createdInventory.getBinClassId());
//
//                        if (createdInventory != null) {
//                            isInventoryCreated = true;
//                        }

                        /* Insert a record into INVENTORYMOVEMENT table */
//                        InventoryMovement createdInventoryMovement = createInventoryMovementV2(createdPutAwayLine);
//                        log.info("inventoryMovement created: " + createdInventoryMovement);
//                        log.info("inventoryMovement created binClassId: " + createdInventory.getBinClassId());

//                        if (createdInventoryMovement != null) {
//                            isInventoryMovemoentCreated = true;
//                        }

                        // Updating StorageBin StatusId as '1'
                        dbStorageBin.setStatusId(1L);
                        mastersService.updateStorageBinV2(dbPutAwayLine.getConfirmedStorageBin(), dbStorageBin,
                                dbPutAwayLine.getCompanyCode(), dbPutAwayLine.getPlantId(), dbPutAwayLine.getLanguageId(), dbPutAwayLine.getWarehouseId(),
                                loginUserID, authTokenForMastersService.getAccess_token());

//                        if (isInventoryCreated && isInventoryMovemoentCreated) {
//                        if (isInventoryMovemoentCreated) {
                        PutAwayHeaderV2 putAwayHeader = putAwayHeaderService.getPutAwayHeaderV2ForPutAwayLine(createdPutAwayLine.getWarehouseId(),
                                createdPutAwayLine.getPreInboundNo(),
                                createdPutAwayLine.getRefDocNumber(),
                                createdPutAwayLine.getPutAwayNumber(),
                                createdPutAwayLine.getCompanyCode(),
                                createdPutAwayLine.getPlantId(),
                                createdPutAwayLine.getLanguageId());

                        confirmedStorageBin = createdPutAwayLine.getConfirmedStorageBin();
                        proposedStorageBin = putAwayHeader.getProposedStorageBin();
                        if (putAwayHeader != null) {
                            log.info("putawayConfirmQty, putawayQty: " + createdPutAwayLine.getPutawayConfirmedQty() + ", " + putAwayHeader.getPutAwayQuantity());

                            putAwayHeader.setStatusId(20L);
                            log.info("PutawayHeader StatusId : 20");
                            statusDescription = stagingLineV2Repository.getStatusDescription(putAwayHeader.getStatusId(), createdPutAwayLine.getLanguageId());
                            putAwayHeader.setStatusDescription(statusDescription);
                            putAwayHeader = putAwayHeaderV2Repository.save(putAwayHeader);
                            log.info("putAwayHeader updated: " + putAwayHeader);

                            if (createdPutAwayLine.getPutawayConfirmedQty() < putAwayHeader.getPutAwayQuantity()) {
//                                List<PutAwayLineV2> filteredlist = newPutAwayLines
//                                        .stream()
//                                        .filter(a ->
//                                                a.getPutAwayNumber().equalsIgnoreCase(createdPutAwayLine.getPutAwayNumber()) &&
//                                                a.getRefDocNumber().equalsIgnoreCase(createdPutAwayLine.getRefDocNumber()) &&
//                                                a.getPreInboundNo().equalsIgnoreCase(createdPutAwayLine.getPreInboundNo()) &&
//                                                a.getItemCode().equalsIgnoreCase(createdPutAwayLine.getItemCode()) &&
//                                                a.getManufacturerName().equalsIgnoreCase(createdPutAwayLine.getManufacturerName()) &&
//                                                a.getCompanyCode().equalsIgnoreCase(createdPutAwayLine.getCompanyCode()) &&
//                                                a.getPlantId().equalsIgnoreCase(createdPutAwayLine.getPlantId()) &&
//                                                a.getLanguageId().equalsIgnoreCase(createdPutAwayLine.getLanguageId()) &&
//                                                a.getWarehouseId().equalsIgnoreCase(createdPutAwayLine.getWarehouseId()) &&
//                                                a.getLineNo().equals(createdPutAwayLine.getLineNo()) &&
//                                                a.getConfirmedStorageBin().equalsIgnoreCase(createdPutAwayLine.getConfirmedStorageBin()))
//                                        .collect(Collectors.toList());
//                                log.info("PutawayLine filtered List: " + filteredlist);
//                                if (filteredlist != null && !filteredlist.isEmpty()) {
//                                    Double putawayQty = filteredlist.stream().mapToDouble(a -> a.getPutawayConfirmedQty()).sum();
//                                    Double dbPutawayQty = 0D;
//                                    if(putAwayHeader.getReferenceField3() != null) {
//                                        dbPutawayQty = Double.valueOf(putAwayHeader.getReferenceField3());
//                                    }
                                    Double dbAssignedPutawayQty = 0D;
                                    if (putAwayHeader.getReferenceField2() != null) {
                                        dbAssignedPutawayQty = Double.valueOf(putAwayHeader.getReferenceField2());
                                    }
                                    if (putAwayHeader.getReferenceField2() == null) {
                                        dbAssignedPutawayQty = putAwayHeader.getPutAwayQuantity();
                                    }
                                    Double dbPutawayQty = putAwayLineV2Repository.getPutawayCnfQuantity(createdPutAwayLine.getCompanyCode(),
                                                                                                        createdPutAwayLine.getPlantId(),
                                                                                                        createdPutAwayLine.getLanguageId(),
                                                                                                        createdPutAwayLine.getWarehouseId(),
                                                                                                        createdPutAwayLine.getRefDocNumber(),
                                                                                                        createdPutAwayLine.getPreInboundNo(),
                                                                                                        createdPutAwayLine.getItemCode(),
                                                                                                        createdPutAwayLine.getManufacturerName(),
                                                                                                        createdPutAwayLine.getLineNo());
                                    if(dbPutawayQty == null) {
                                        dbPutawayQty = 0D;
                                    }

                                    log.info("tot_pa_cnf_qty,created_pa_line_cnf_qty,partial_pa_header_pa_qty,pa_header_pa_qty,RF2 : "
                                            + dbPutawayQty + ", " + createdPutAwayLine.getPutawayConfirmedQty()
                                            + ", " + putAwayHeader.getPutAwayQuantity() + ", " + putAwayHeader.getReferenceField2());
                                    if (dbPutawayQty > dbAssignedPutawayQty) {
                                        throw new BadRequestException("sum of confirm Putaway line qty is greater than assigned putaway header qty");
                                    }
                                    if (dbPutawayQty <= dbAssignedPutawayQty) {
                                        if (proposedStorageBin.equalsIgnoreCase(confirmedStorageBin)) {
                                            log.info("New PutawayHeader Creation: ");
                                            PutAwayHeaderV2 newPutAwayHeader = new PutAwayHeaderV2();
                                            BeanUtils.copyProperties(putAwayHeader, newPutAwayHeader, CommonUtils.getNullPropertyNames(putAwayHeader));

                                            // PA_NO
                                            long NUM_RAN_CODE = 7;
                                            String nextPANumber = getNextRangeNumber(NUM_RAN_CODE, companyCode, plantId, languageId, warehouseId, authTokenForIDMasterService.getAccess_token());
                                            newPutAwayHeader.setPutAwayNumber(nextPANumber);                           //PutAway Number

                                            newPutAwayHeader.setReferenceField1(String.valueOf(putAwayHeader.getPutAwayQuantity()));
                                            if (putAwayHeader.getReferenceField4() == null) {
                                                newPutAwayHeader.setReferenceField2(String.valueOf(putAwayHeader.getPutAwayQuantity()));
                                                newPutAwayHeader.setReferenceField4("1");
                                            }
                                            Double putawaycnfQty = 0D;
                                            if (newPutAwayHeader.getReferenceField3() != null) {
                                                putawaycnfQty = Double.valueOf(newPutAwayHeader.getReferenceField3());
                                            }
                                            putawaycnfQty = putawaycnfQty + createdPutAwayLine.getPutawayConfirmedQty();
                                            newPutAwayHeader.setReferenceField3(String.valueOf(putawaycnfQty));

//                                    Double PUTAWAY_QTY = (putAwayHeader.getPutAwayQuantity() != null ? putAwayHeader.getPutAwayQuantity() : 0) - (createdPutAwayLine.getPutawayConfirmedQty() != null ? createdPutAwayLine.getPutawayConfirmedQty() : 0);
                                            Double PUTAWAY_QTY = dbAssignedPutawayQty - dbPutawayQty;
                                            if(PUTAWAY_QTY < 0) {
                                                throw new BadRequestException("total confirm qty greater than putaway qty");
                                            }
                                            newPutAwayHeader.setPutAwayQuantity(PUTAWAY_QTY);
                                            log.info("OrderQty ReCalcuated/Changed : " + PUTAWAY_QTY);
                                            newPutAwayHeader.setStatusId(19L);
                                            log.info("PutawayHeader StatusId : 19");
                                            statusDescription = stagingLineV2Repository.getStatusDescription(newPutAwayHeader.getStatusId(), createdPutAwayLine.getLanguageId());
                                            newPutAwayHeader.setStatusDescription(statusDescription);
                                            newPutAwayHeader = putAwayHeaderV2Repository.save(newPutAwayHeader);
                                            log.info("putAwayHeader created: " + newPutAwayHeader);
                                        }
                                        if (!proposedStorageBin.equalsIgnoreCase(confirmedStorageBin)) {

                                            putAwayHeader.setReferenceField1(String.valueOf(putAwayHeader.getPutAwayQuantity()));
                                            if (putAwayHeader.getReferenceField4() == null) {
                                                putAwayHeader.setReferenceField2(String.valueOf(putAwayHeader.getPutAwayQuantity()));
                                                putAwayHeader.setReferenceField4("1");
                                            }
                                            Double PUTAWAY_QTY = dbAssignedPutawayQty - dbPutawayQty;
                                            if(PUTAWAY_QTY < 0) {
                                                throw new BadRequestException("total confirm qty greater than putaway qty");
                                            }
                                            putAwayHeader.setPutAwayQuantity(PUTAWAY_QTY);
                                            log.info("OrderQty ReCalcuated/Changed : " + PUTAWAY_QTY);
                                            putAwayHeader.setStatusId(19L);
                                            log.info("PutawayHeader StatusId : 19");
                                            statusDescription = stagingLineV2Repository.getStatusDescription(putAwayHeader.getStatusId(), createdPutAwayLine.getLanguageId());
                                            putAwayHeader.setStatusDescription(statusDescription);
                                            putAwayHeader = putAwayHeaderV2Repository.save(putAwayHeader);
                                            log.info("putAwayHeader updated: " + putAwayHeader);
                                        }
                                    }
//                                }
                            }
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
                                inboundLine.setVarianceQty(inboundLine.getOrderQty() - addedAcceptQty);
                            }

                            // if QTY_TYPE = D, add PA_CNF_QTY with existing value in DAMAGE_QTY field
                            if (createdPutAwayLine.getQuantityType().equalsIgnoreCase("D")) {
                                if (inboundLine.getDamageQty() != null) {
                                    addedDamageQty = inboundLine.getDamageQty() + createdPutAwayLine.getPutawayConfirmedQty();
                                } else {
                                    addedDamageQty = createdPutAwayLine.getPutawayConfirmedQty();
                                }
                                inboundLine.setDamageQty(addedDamageQty);
                                inboundLine.setVarianceQty(inboundLine.getOrderQty() - addedDamageQty);
                            }

                            inboundLine.setStatusId(20L);
                            statusDescription = stagingLineV2Repository.getStatusDescription(20L, createdPutAwayLine.getLanguageId());
                            inboundLine.setStatusDescription(statusDescription);
                            inboundLine = inboundLineV2Repository.save(inboundLine);
                            log.info("inboundLine updated : " + inboundLine);
//                        }
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
     * @param remainingVolume
     * @param occupiedVolume
     * @param allocatedVolume
     * @param storageBin
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param loginUserId
     * @param authToken
     */
    public void updateStorageBin(Double remainingVolume, Double occupiedVolume, Double allocatedVolume,
                                 String storageBin, String companyCode, String plantId, String languageId,
                                 String warehouseId, Long statusId, String loginUserId, String authToken) {

        StorageBinV2 modifiedStorageBin = new StorageBinV2();
        modifiedStorageBin.setRemainingVolume(String.valueOf(remainingVolume));
        modifiedStorageBin.setAllocatedVolume(allocatedVolume);
        modifiedStorageBin.setOccupiedVolume(String.valueOf(occupiedVolume));
        modifiedStorageBin.setCapacityCheck(true);
        modifiedStorageBin.setStatusId(statusId);

        StorageBinV2 updateStorageBinV2 = mastersService.updateStorageBinV2(storageBin,
                modifiedStorageBin,
                companyCode,
                plantId,
                languageId,
                warehouseId,
                loginUserId,
                authToken);

        if (updateStorageBinV2 != null) {
            log.info("Storage Bin Volume Updated successfully ");
        }
    }

    public PutAwayLineV2 createPutAwayLineV2(PutAwayLineV2 newPutAwayLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
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
            throws IllegalAccessException, InvocationTargetException, ParseException {
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
                                    String itemCode, String proposedStorageBin, String confirmedStorageBin, String loginUserID) throws ParseException {
        PutAwayLineV2 putAwayLine = getPutAwayLineV2(companyCodeId, plantId, languageId, warehouseId,
                goodsReceiptNo, preInboundNo, refDocNumber, putAwayNumber,
                lineNo, itemCode, proposedStorageBin, Arrays.asList(confirmedStorageBin));
        if (putAwayLine != null) {
            putAwayLine.setDeletionIndicator(1L);
            putAwayLine.setUpdatedBy(loginUserID);
            putAwayLine.setUpdatedOn(new Date());
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

        inventoryMovement.setManufacturerName(dbPutAwayLine.getManufacturerName());
        inventoryMovement.setRefDocNumber(dbPutAwayLine.getRefDocNumber());
        inventoryMovement.setCompanyDescription(dbPutAwayLine.getCompanyDescription());
        inventoryMovement.setPlantDescription(dbPutAwayLine.getPlantDescription());
        inventoryMovement.setWarehouseDescription(dbPutAwayLine.getWarehouseDescription());
        inventoryMovement.setBarcodeId(dbPutAwayLine.getBarcodeId());
        inventoryMovement.setDescription(dbPutAwayLine.getDescription());

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

        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

        StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
        storageBinPutAway.setCompanyCodeId(dbPutAwayLine.getCompanyCode());
        storageBinPutAway.setPlantId(dbPutAwayLine.getPlantId());
        storageBinPutAway.setLanguageId(dbPutAwayLine.getLanguageId());
        storageBinPutAway.setWarehouseId(dbPutAwayLine.getWarehouseId());
        storageBinPutAway.setBin(dbPutAwayLine.getConfirmedStorageBin());

        StorageBinV2 dbStoragebin = null;
        try {
            dbStoragebin = mastersService.getaStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
        } catch (Exception e) {
            throw new BadRequestException("Invalid StorageBin");
        }

        log.info("StorageBin: " + dbStoragebin);

        /*
         * -----THE BELOW IS NOT USED-------------
         * Pass WH_ID/ITM_CODE/PACK_BARCODE/BIN_CL_ID is equal to 1 in INVENTORY table and fetch INV_QTY
         * BAL_OH_QTY = INV_QTY
         */
        // PASS WH_ID/ITM_CODE/BIN_CL_ID and sum the INV_QTY for all selected inventory
        Long binClassId = 0L;                   //actual code follows
        if (dbPutAwayLine.getInboundOrderTypeId() == 1 || dbPutAwayLine.getInboundOrderTypeId() == 3 || dbPutAwayLine.getInboundOrderTypeId() == 4 || dbPutAwayLine.getInboundOrderTypeId() == 5) {
            binClassId = 1L;
            log.info("Inv Mmt BinClassId: " + binClassId);
        }
        if (dbPutAwayLine.getInboundOrderTypeId() == 2) {
            if (dbStoragebin != null) {
                binClassId = dbStoragebin.getBinClassId();
            } else {
                binClassId = 7L;
            }
            log.info("Inv Mmt Ib_Ord_Typ_Id 2 - BinClassId: " + binClassId);
        }
        List<IInventoryImpl> inventoryList = inventoryService.getInventoryForInvMmt(dbPutAwayLine.getCompanyCode(),
                dbPutAwayLine.getPlantId(),
                dbPutAwayLine.getLanguageId(),
                dbPutAwayLine.getWarehouseId(),
                dbPutAwayLine.getItemCode(),
                dbPutAwayLine.getManufacturerName(),
                binClassId);
        if (inventoryList != null) {
            double sumOfInvQty = inventoryList.stream().mapToDouble(a -> a.getInventoryQuantity()).sum();

            log.info("Inv Mmt sumOfInvQty: " + sumOfInvQty);

            inventoryMovement.setBalanceOHQty(sumOfInvQty);
        }
        if (inventoryList == null) {
            inventoryMovement.setBalanceOHQty(dbPutAwayLine.getPutawayConfirmedQty());
        }

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(dbPutAwayLine.getCreatedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(dbPutAwayLine.getCreatedOn());
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        return inventoryMovement;
    }

    /**
     *
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param loginUserID
     * @return
     * @throws ParseException
     */
    //Delete PutAwayLine
    public List<PutAwayLineV2> deletePutAwayLineV2(String languageId, String companyCodeId, String plantId, String warehouseId,
                                                   String refDocNumber, String loginUserID) throws ParseException {
        List<PutAwayLineV2> putAwayLineV2List = new ArrayList<>();
        List<PutAwayLineV2> putAwayLineList = putAwayLineV2Repository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                companyCodeId, plantId, languageId, warehouseId, refDocNumber, 0L);
        log.info("putAwayLineList - cancellation : " + putAwayLineList);
        if (putAwayLineList != null && !putAwayLineList.isEmpty()) {
            for (PutAwayLineV2 putAwayLineV2 : putAwayLineList) {
                putAwayLineV2.setDeletionIndicator(1L);
                putAwayLineV2.setUpdatedBy(loginUserID);
                putAwayLineV2.setUpdatedOn(new Date());
                PutAwayLineV2 dbPutAwayLine = putAwayLineV2Repository.save(putAwayLineV2);
                putAwayLineV2List.add(dbPutAwayLine);
            }
        }
        return putAwayLineV2List;
    }
}
