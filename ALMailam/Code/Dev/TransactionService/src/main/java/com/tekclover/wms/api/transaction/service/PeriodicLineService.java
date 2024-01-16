package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.config.PropertiesConfig;
import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.cyclecount.periodic.*;
import com.tekclover.wms.api.transaction.model.cyclecount.periodic.v2.*;
import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.AssignHHTUserCC;
import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2.PerpetualHeaderV2;
import com.tekclover.wms.api.transaction.model.dto.*;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayLineV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountLine;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PeriodicLineSpecification;
import com.tekclover.wms.api.transaction.repository.specification.PeriodicLineV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PeriodicLineService extends BaseService {
    @Autowired
    private CycleCountLineRepository cycleCountLineRepository;
    @Autowired
    private PeriodicHeaderV2Repository periodicHeaderV2Repository;

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    private PeriodicLineV2Repository periodicLineV2Repository;

    @Autowired
    private PeriodicLineTempV2Repository periodicLineTempV2Repository;

    private static final String WRITEOFF = "WRITEOFF";
    //    private static final String SKIP = "SKIP";
    private static final String SKIP = "CONFIRM";
    private static final String RECOUNT = "RECOUNT";

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    MastersService mastersService;

    @Autowired
    PeriodicHeaderService periodicHeaderService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private PeriodicLineRepository periodicLineRepository;

    @Autowired
    private ImBasicData1Repository imbasicdata1Repository;

    @Autowired
    private PutAwayLineService putAwayLineService;

    @Autowired
    private PickupLineService pickupLineService;

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    @Autowired
    private PeriodicLineService periodicLineService;

    String statusDescription = null;

    //=======================================================================================================================

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getConnectorServiceApiUrl() {
        return propertiesConfig.getConnectorServiceUrl();
    }

//=======================================================================================================================

    /**
     * getPeriodicLines
     *
     * @return
     */
    public List<PeriodicLine> getPeriodicLines() {
        List<PeriodicLine> periodicLineList = periodicLineRepository.findAll();
        periodicLineList = periodicLineList.stream().filter(n -> n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return periodicLineList;
    }

    /**
     * @param companyCode
     * @param languageId
     * @param plantId
     * @param warehouseId
     * @param statusId
     * @return
     */
    public List<PeriodicLine> getPeriodicLine(String companyCode, String languageId, String plantId,
                                              String warehouseId, List<Long> statusId) {
        return periodicLineRepository.findByCompanyCodeAndLanguageIdAndPlantIdAndWarehouseIdAndStatusIdInAndDeletionIndicator(
                companyCode, languageId, plantId, warehouseId, statusId, 0L);
    }

    /**
     *
     * @param companyCode
     * @param languageId
     * @param plantId
     * @param warehouseId
     * @param cycleCounterId
     * @param statusId
     * @return
     */
    public List<PeriodicLine> getPeriodicLine(String companyCode, String languageId, String plantId,
                                              String warehouseId, String cycleCounterId, List<Long> statusId) {
        return periodicLineRepository.findByCompanyCodeAndLanguageIdAndPlantIdAndWarehouseIdAndCycleCounterIdAndStatusIdInAndDeletionIndicator(
                companyCode, languageId, plantId, warehouseId,cycleCounterId, statusId, 0L);
    }

    /**
     * @param warehouseId
     * @param cycleCountNo
     * @param storageBin
     * @param itemCode
     * @param packBarcodes
     * @return
     */
    public PeriodicLine getPeriodicLine(String warehouseId, String cycleCountNo, String storageBin, String itemCode,
                                        String packBarcodes) {
        PeriodicLine periodicLine =
                periodicLineRepository.findByWarehouseIdAndCycleCountNoAndStorageBinAndItemCodeAndPackBarcodesAndDeletionIndicator(
                        warehouseId, cycleCountNo, storageBin, itemCode, packBarcodes, 0L);
        if (periodicLine != null && periodicLine.getDeletionIndicator() == 0) {
            return periodicLine;
        }
        throw new BadRequestException("The given PeriodicLine ID : " + storageBin + " doesn't exist.");
    }

    /**
     * @param cycleCountNo
     * @return
     */
    public List<PeriodicLine> getPeriodicLine(String cycleCountNo) {
        List<PeriodicLine> PeriodicLine = periodicLineRepository.findByCycleCountNoAndDeletionIndicator(cycleCountNo, 0L);
        return PeriodicLine;
    }

    /**
     * @param searchPeriodicLine
     * @return
     * @throws Exception
     */
    public List<PeriodicLine> findPeriodicLine(SearchPeriodicLine searchPeriodicLine) throws Exception {
        if (searchPeriodicLine.getStartCreatedOn() != null && searchPeriodicLine.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPeriodicLine.getStartCreatedOn(),
                    searchPeriodicLine.getEndCreatedOn());
            searchPeriodicLine.setStartCreatedOn(dates[0]);
            searchPeriodicLine.setEndCreatedOn(dates[1]);
        }

        PeriodicLineSpecification spec = new PeriodicLineSpecification(searchPeriodicLine);
        List<PeriodicLine> PeriodicLineResults = periodicLineRepository.findAll(spec);
        return PeriodicLineResults;
    }

    /**
     * Stream
     *
     * @param searchPeriodicLine
     * @return
     * @throws Exception
     */
    public Stream<PeriodicLine> findPeriodicLineStream(SearchPeriodicLine searchPeriodicLine) throws Exception {
        if (searchPeriodicLine.getStartCreatedOn() != null && searchPeriodicLine.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPeriodicLine.getStartCreatedOn(),
                    searchPeriodicLine.getEndCreatedOn());
            searchPeriodicLine.setStartCreatedOn(dates[0]);
            searchPeriodicLine.setEndCreatedOn(dates[1]);
        }

        PeriodicLineSpecification spec = new PeriodicLineSpecification(searchPeriodicLine);
        Stream<PeriodicLine> PeriodicLineResults = periodicLineRepository.stream(spec, PeriodicLine.class);
        return PeriodicLineResults;
    }

    /**
     * @param newPeriodicLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PeriodicLine createPeriodicLine(AddPeriodicLine newPeriodicLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PeriodicLine dbPeriodicLine = new PeriodicLine();
        log.info("newPeriodicLine : " + newPeriodicLine);
        BeanUtils.copyProperties(newPeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(newPeriodicLine));
        dbPeriodicLine.setDeletionIndicator(0L);
        dbPeriodicLine.setCreatedBy(loginUserID);
        dbPeriodicLine.setCreatedOn(new Date());
        dbPeriodicLine.setCountedBy(loginUserID);
        dbPeriodicLine.setCountedOn(new Date());
        return periodicLineRepository.save(dbPeriodicLine);
    }

    /**
     * @param assignHHTUsers
     * @param loginUserID
     * @return
     */
    public List<PeriodicLine> updateAssingHHTUser(List<AssignHHTUserCC> assignHHTUsers, String loginUserID) {
        try {
            log.info("assignHHTUsers : " + assignHHTUsers);
            List<PeriodicLine> responseList = new ArrayList<>();
            for (AssignHHTUserCC assignHHTUser : assignHHTUsers) {
                PeriodicLine periodicLine = getPeriodicLine(assignHHTUser.getWarehouseId(), assignHHTUser.getCycleCountNo(),
                        assignHHTUser.getStorageBin(), assignHHTUser.getItemCode(), assignHHTUser.getPackBarcodes());
                periodicLine.setCycleCounterId(assignHHTUser.getCycleCounterId());
                periodicLine.setCycleCounterName(assignHHTUser.getCycleCounterName());
                periodicLine.setStatusId(72L);
                periodicLine.setCountedBy(loginUserID);
                periodicLine.setCountedOn(new Date());
                PeriodicLine updatedPeriodicLine = periodicLineRepository.save(periodicLine);
                log.info("updatedPeriodicLine : " + updatedPeriodicLine);

                responseList.add(updatedPeriodicLine);
            }
            return responseList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param updatePeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PeriodicLine> updatePeriodicLineForMobileCount(List<UpdatePeriodicLine> updatePeriodicLines,
                                                               String loginUserID) throws IllegalAccessException, InvocationTargetException {
        List<PeriodicLine> responsePeriodicLines = new ArrayList<>();
        for (UpdatePeriodicLine updatePeriodicLine : updatePeriodicLines) {
            PeriodicLine dbPeriodicLine = getPeriodicLine(updatePeriodicLine.getWarehouseId(), updatePeriodicLine.getCycleCountNo(),
                    updatePeriodicLine.getStorageBin(), updatePeriodicLine.getItemCode(), updatePeriodicLine.getPackBarcodes());
            if (dbPeriodicLine != null) { /* Update */
                BeanUtils.copyProperties(updatePeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(updatePeriodicLine));

                // INV_QTY
                double INV_QTY = updatePeriodicLine.getInventoryQuantity();
                dbPeriodicLine.setInventoryQuantity(INV_QTY);

                // CTD_QTY
                double CTD_QTY = updatePeriodicLine.getCountedQty();
                dbPeriodicLine.setCountedQty(CTD_QTY);

                // VAR_QTY = INV_QTY - CTD_QTY
                double VAR_QTY = INV_QTY - CTD_QTY;
                dbPeriodicLine.setVarianceQty(VAR_QTY);

                /*
                 * HardCoded Value "78" if VAR_QTY = 0 and
                 * Hardcodeed value"74" - if VAR_QTY is greater than or less than Zero
                 */
                if (VAR_QTY == 0) {
                    dbPeriodicLine.setStatusId(78L);
                } else if (VAR_QTY > 0 || VAR_QTY < 0) {
                    dbPeriodicLine.setStatusId(74L);
                }

                dbPeriodicLine.setCountedBy(loginUserID);
                dbPeriodicLine.setCountedOn(new Date());
                PeriodicLine updatedPeriodicLine = periodicLineRepository.save(dbPeriodicLine);
                log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                responsePeriodicLines.add(updatedPeriodicLine);
            } else {
                // Create new Record
                AddPeriodicLine newPeriodicLine = new AddPeriodicLine();
                BeanUtils.copyProperties(updatePeriodicLine, newPeriodicLine, CommonUtils.getNullPropertyNames(updatePeriodicLine));
                newPeriodicLine.setCycleCountNo(updatePeriodicLine.getCycleCountNo());
                PeriodicLine createdPeriodicLine = createPeriodicLine(newPeriodicLine, loginUserID);
                log.info("createdPeriodicLine : " + createdPeriodicLine);
                responsePeriodicLines.add(createdPeriodicLine);
            }
        }

        return responsePeriodicLines;
    }

    /**
     * @param cycleCountNo
     * @param updatePeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PeriodicUpdateResponse updatePeriodicLine(String cycleCountNo, List<UpdatePeriodicLine> updatePeriodicLines,
                                                     String loginUserID) throws IllegalAccessException, InvocationTargetException {
        List<PeriodicLine> responsePeriodicLines = new ArrayList<>();
        try {
            List<PeriodicLine> newPeriodicLines = new ArrayList<>();
            for (UpdatePeriodicLine updatePeriodicLine : updatePeriodicLines) {
                PeriodicLine dbPeriodicLine = getPeriodicLine(updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getCycleCountNo(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getPackBarcodes());
                BeanUtils.copyProperties(updatePeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(updatePeriodicLine));
                dbPeriodicLine.setRemarks(updatePeriodicLine.getRemarks());
                dbPeriodicLine.setCycleCountAction(updatePeriodicLine.getCycleCountAction());

                /*
                 * 1. Action = WRITEOFF
                 * If ACTION = WRITEOFF , update ACTION field in PeriodicLine as WRITEOFF by passing unique fields and
                 * update in STATUS_ID field as "78"
                 */
                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(WRITEOFF)) {
                    dbPeriodicLine.setStatusId(78L);
                    dbPeriodicLine.setCycleCountAction(WRITEOFF);
                    PeriodicLine updatedPeriodicLine = periodicLineRepository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

                    /*
                     * Inventory table update
                     * ---------------------------
                     * Fetch CNT_QTY of the selected ITM_CODE and Pass WH_ID/ITM_CODE/ST_BIN/PACK_BARCODE values in INVENTORY table
                     * and replace INV_QTY as CNT_QTY
                     */
                    updateInventory(updatedPeriodicLine);
                    createInventoryMovement(updatedPeriodicLine);
                }

                /*
                 * 2. Action = SKIP
                 * if ACTION = SKIP in UI,  update ACTION field in PeriodicLine as SKIP by passing unique fields
                 * and update in STATUS_ID field as "78"
                 */
                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(SKIP)) {
                    dbPeriodicLine.setStatusId(78L);
                    dbPeriodicLine.setCycleCountAction(SKIP);
                    PeriodicLine updatedPeriodicLine = periodicLineRepository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

                    /*
                     * Inventory table update
                     * ---------------------------
                     * Insert a new record by passing WH_ID/ITM_CODE/PACK_BARCODE/ST_BIN (fetch ST_BIN
                     * from STORAGEBIN table where BIN_CL_ID=5) values in INVENTORY table and append INV_QTY as
                     * VAR_QTY
                     */
//					createInventory (updatedPeriodicLine);
//					createInventoryMovement (updatedPeriodicLine) ;
                }

                /*
                 * 3. Action = RECOUNT (default Action Value)
                 * If ACTION = RECOUNT, update ACTION field in PeriodicLine as SKIP by passing unique fields
                 * and update in STATUS_ID field as "78"
                 */
                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(RECOUNT)) {
                    dbPeriodicLine.setStatusId(78L);
                    dbPeriodicLine.setCycleCountAction(RECOUNT);
                    PeriodicLine updatedPeriodicLine = periodicLineRepository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

                    /*
                     * Preparation of new PeriodicLines
                     */
                    PeriodicLine newPeriodicLine = new PeriodicLine();
                    BeanUtils.copyProperties(updatedPeriodicLine, newPeriodicLine, CommonUtils.getNullPropertyNames(updatedPeriodicLine));
                    newPeriodicLine.setStatusId(78L);
                    newPeriodicLines.add(newPeriodicLine);
                }
            }

            /*
             * Create New CC_NO record as below
             */
            PeriodicHeader newlyCreatedPeriodicHeader = new PeriodicHeader();
            if (!newPeriodicLines.isEmpty()) {
                log.info("newPeriodicLines : " + newPeriodicLines);

                // Create new PeriodicHeader and Lines
                PeriodicHeaderEntity createdPeriodicHeader = createNewHeaderNLines(cycleCountNo, newPeriodicLines, loginUserID);
                log.info("createdPeriodicHeader : " + createdPeriodicHeader);
                BeanUtils.copyProperties(createdPeriodicHeader, newlyCreatedPeriodicHeader, CommonUtils.getNullPropertyNames(createdPeriodicHeader));
            }

            // Update new PeriodicHeader
            PeriodicHeader dbPeriodicHeader = periodicHeaderService.getPeriodicHeader(cycleCountNo);
            UpdatePeriodicHeader updatePeriodicHeader = new UpdatePeriodicHeader();
            BeanUtils.copyProperties(dbPeriodicHeader, updatePeriodicHeader, CommonUtils.getNullPropertyNames(dbPeriodicHeader));
            PeriodicHeader updatedPeriodicHeader = periodicHeaderService.updatePeriodicHeaderFromPeriodicLine(dbPeriodicHeader.getWarehouseId(),
                    dbPeriodicHeader.getCycleCountTypeId(), dbPeriodicHeader.getCycleCountNo(), loginUserID);
            log.info("updatedPeriodicHeader : " + updatedPeriodicHeader);

            PeriodicUpdateResponse response = new PeriodicUpdateResponse();
            response.setPeriodicHeader(newlyCreatedPeriodicHeader);
            response.setPeriodicLines(responsePeriodicLines);
            log.info("PeriodicUpdateResponse------> : " + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param cycleCountNo
     * @param newPeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private PeriodicHeaderEntity createNewHeaderNLines(String cycleCountNo, List<PeriodicLine> newPeriodicLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        log.info("newPeriodicLines : " + newPeriodicLines);
        if (newPeriodicLines != null) {
            PeriodicHeader dbPeriodicHeader = periodicHeaderService.getPeriodicHeader(cycleCountNo);
            AddPeriodicHeader newPeriodicHeader = new AddPeriodicHeader();
            BeanUtils.copyProperties(dbPeriodicHeader, newPeriodicHeader, CommonUtils.getNullPropertyNames(dbPeriodicHeader));
            newPeriodicHeader.setPeriodicLine(newPeriodicLines);
            PeriodicHeaderEntity createdPeriodicHeader = periodicHeaderService.createPeriodicHeader(newPeriodicHeader, loginUserID);
            log.info("createdPeritodicHeader : " + createdPeriodicHeader);
            return createdPeriodicHeader;
        }
        return null;
    }

    /**
     * @param updatePeriodicLine
     * @return
     */
    private Inventory updateInventory(PeriodicLine updatePeriodicLine) {
        Inventory inventory = inventoryService.getInventory(updatePeriodicLine.getWarehouseId(),
                updatePeriodicLine.getPackBarcodes(), updatePeriodicLine.getItemCode(),
                updatePeriodicLine.getStorageBin());
        inventory.setInventoryQuantity(updatePeriodicLine.getCountedQty());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("updatedInventory : " + updatedInventory);
        return updatedInventory;
    }

    /**
     * @param updatePeriodicLine
     * @return
     */
    private Inventory createInventory(PeriodicLine updatePeriodicLine) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(updatePeriodicLine, inventory, CommonUtils.getNullPropertyNames(updatePeriodicLine));
        inventory.setCompanyCodeId(updatePeriodicLine.getCompanyCode());

        // VAR_ID, VAR_SUB_ID, STR_MTD, STR_NO ---> Hard coded as '1'
        inventory.setVariantCode(1L);
        inventory.setVariantSubCode("1");
        inventory.setStorageMethod("1");
        inventory.setBatchSerialNumber("1");
        inventory.setBinClassId(5L);

        // ST_BIN ---Pass WH_ID/BIN_CL_ID=5 in STORAGEBIN table and fetch ST_BIN value and update
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        StorageBin storageBin =
                mastersService.getStorageBin(updatePeriodicLine.getWarehouseId(), 5L, authTokenForMastersService.getAccess_token());
        inventory.setStorageBin(storageBin.getStorageBin());

        List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(inventory.getItemCode());
        if (imbasicdata1 != null && !imbasicdata1.isEmpty()) {
            inventory.setReferenceField8(imbasicdata1.get(0).getDescription());
            inventory.setReferenceField9(imbasicdata1.get(0).getManufacturePart());
        }
        if (storageBin != null) {
            inventory.setReferenceField10(storageBin.getStorageSectionId());
            inventory.setReferenceField5(storageBin.getAisleNumber());
            inventory.setReferenceField6(storageBin.getShelfId());
            inventory.setReferenceField7(storageBin.getRowId());
        }

        // STCK_TYP_ID
        inventory.setStockTypeId(1L);

        // SP_ST_IND_ID
        inventory.setSpecialStockIndicatorId(1L);

        // INV_QTY
        inventory.setInventoryQuantity(updatePeriodicLine.getVarianceQty());

        // INV_UOM
        inventory.setInventoryUom(updatePeriodicLine.getInventoryUom());

        inventory.setCreatedBy(updatePeriodicLine.getCreatedBy());
        inventory.setCreatedOn(updatePeriodicLine.getCreatedOn());
        Inventory createdinventory = inventoryRepository.save(inventory);
        log.info("created inventory : " + createdinventory);
        return createdinventory;
    }

    /**
     * @param updatedPeriodicLine
     * @return
     */
    private InventoryMovement createInventoryMovement(PeriodicLine updatedPeriodicLine) {
        InventoryMovement inventoryMovement = new InventoryMovement();
        BeanUtils.copyProperties(updatedPeriodicLine, inventoryMovement, CommonUtils.getNullPropertyNames(updatedPeriodicLine));

        inventoryMovement.setLanguageId(updatedPeriodicLine.getLanguageId());
        inventoryMovement.setCompanyCodeId(updatedPeriodicLine.getCompanyCode());
        inventoryMovement.setPlantId(updatedPeriodicLine.getPlantId());
        inventoryMovement.setWarehouseId(updatedPeriodicLine.getWarehouseId());

        // MVT_TYP_ID
        inventoryMovement.setMovementType(4L);

        // SUB_MVT_TYP_ID
        inventoryMovement.setSubmovementType(1L);

        // ITEM_TEXT
        // Pass ITM_CODE in IMBASICDATA table and fetch ITEM_TEXT values
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(inventoryMovement.getItemCode(),
                inventoryMovement.getWarehouseId(), authTokenForMastersService.getAccess_token());
        inventoryMovement.setDescription(imBasicData1.getDescription());

        // MFR_PART of ITM_CODE from BASICDATA1
        inventoryMovement.setManufacturerName(imBasicData1.getManufacturerPartNo());

        /*
         * MVT_QTY_VAL
         * -----------------
         * Hard Coded value "P", if VAR_QTY is negative and Hard coded value "N", if VAR_QTY is positive
         */
        if (updatedPeriodicLine.getVarianceQty() < 0) {
            inventoryMovement.setMovementQtyValue("P");
        } else if (updatedPeriodicLine.getVarianceQty() > 0) {
            inventoryMovement.setMovementQtyValue("N");
        }

        inventoryMovement.setBatchSerialNumber("1");
        inventoryMovement.setMovementDocumentNo(updatedPeriodicLine.getCycleCountNo());
        inventoryMovement.setMovementQty(updatedPeriodicLine.getVarianceQty());
        inventoryMovement.setCreatedBy(updatedPeriodicLine.getCreatedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(new Date());
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        log.info("created InventoryMovement : " + inventoryMovement);
        return inventoryMovement;
    }

    /**
     * deletePeriodicLine
     *
     * @param loginUserID
     * @param storageBin
     */
    public void deletePeriodicLine(String warehouseId, String cycleCountNo, String storageBin, String itemCode, String packBarcodes, String loginUserID) {
        PeriodicLine periodicLine = getPeriodicLine(warehouseId, cycleCountNo, storageBin, itemCode, packBarcodes);
        if (periodicLine != null) {
            periodicLine.setDeletionIndicator(1L);
            periodicLine.setConfirmedBy(loginUserID);
            periodicLine.setConfirmedOn(new Date());
            periodicLineRepository.save(periodicLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + storageBin);
        }
    }

    //===========================================================V2==================================================================================

    public List<PeriodicLineV2> getPeriodicLinesV2() {
        List<PeriodicLineV2> periodicLineList = periodicLineV2Repository.findAll();
        periodicLineList = periodicLineList.stream().filter(n -> n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return periodicLineList;
    }

    /**
     * @param warehouseId
     * @param cycleCountNo
     * @param storageBin
     * @param itemCode
     * @param packBarcodes
     * @return
     */
    public PeriodicLineV2 getPeriodicLineV2(String companyCode, String plantId, String languageId, String warehouseId,
                                            String cycleCountNo, String storageBin, String itemCode, String packBarcodes) {
        PeriodicLineV2 periodicLine =
                periodicLineV2Repository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndCycleCountNoAndStorageBinAndItemCodeAndPackBarcodesAndDeletionIndicator(
                        companyCode, plantId, languageId, warehouseId, cycleCountNo, storageBin, itemCode, packBarcodes, 0L);
        if (periodicLine != null) {
            return periodicLine;
        }
        throw new BadRequestException("The given PeriodicLine ID : "
                + "companyCode" + companyCode
                + "plantId " + plantId
                + "languageId " + languageId
                + "storageBin " + storageBin
                + "itemCode " + itemCode + " doesn't exist.");
    }

    /**
     * @param cycleCountNo
     * @return
     */
    public List<PeriodicLineV2> getPeriodicLineV2(String companyCode, String plantId, String languageId, String warehouseId, String cycleCountNo) {
        List<PeriodicLineV2> PeriodicLine =
                periodicLineV2Repository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndCycleCountNoAndDeletionIndicator(
                        companyCode, plantId, languageId, warehouseId, cycleCountNo, 0L);
        return PeriodicLine;
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param cycleCountNo
     * @param statusId
     * @return
     */
    public List<PeriodicLineV2> getPeriodicLineForFindV2(String companyCode, String plantId, String languageId, String warehouseId,
                                                         String cycleCountNo, List<Long> statusId) {
        List<PeriodicLineV2> PeriodicLine =
                periodicLineV2Repository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndCycleCountNoAndStatusIdInAndDeletionIndicator(
                        companyCode, plantId, languageId, warehouseId, cycleCountNo, statusId, 0L);
        return PeriodicLine;
    }

    /**
     * Stream
     *
     * @param searchPeriodicLine
     * @return
     * @throws Exception
     */
    public Stream<PeriodicLineV2> findPeriodicLineStreamV2(SearchPeriodicLineV2 searchPeriodicLine) throws Exception {
        if (searchPeriodicLine.getStartCreatedOn() != null && searchPeriodicLine.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPeriodicLine.getStartCreatedOn(),
                    searchPeriodicLine.getEndCreatedOn());
            searchPeriodicLine.setStartCreatedOn(dates[0]);
            searchPeriodicLine.setEndCreatedOn(dates[1]);
        }

        PeriodicLineV2Specification spec = new PeriodicLineV2Specification(searchPeriodicLine);
        Stream<PeriodicLineV2> PeriodicLineResults = periodicLineV2Repository.stream(spec, PeriodicLineV2.class);
        return PeriodicLineResults;
    }

    /**
     * @param newPeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PeriodicLineV2> createPeriodicLineV2(List<PeriodicLineV2> newPeriodicLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        List<PeriodicLineV2> newPeriodicLineList = new ArrayList<>();
        for (PeriodicLineV2 newPeriodicLine : newPeriodicLines) {
            PeriodicLineV2 dbPeriodicLine = new PeriodicLineV2();
            BeanUtils.copyProperties(newPeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(newPeriodicLine));
            dbPeriodicLine.setDeletionIndicator(0L);

            statusDescription = stagingLineV2Repository.getStatusDescription(dbPeriodicLine.getStatusId(), dbPeriodicLine.getLanguageId());
            dbPeriodicLine.setStatusDescription(statusDescription);

            dbPeriodicLine.setCreatedBy(loginUserID);
            dbPeriodicLine.setCreatedOn(DateUtils.getCurrentKWTDateTime());
            dbPeriodicLine.setCountedBy(loginUserID);
            dbPeriodicLine.setCountedOn(DateUtils.getCurrentKWTDateTime());
            newPeriodicLineList.add(dbPeriodicLine);
        }
        return periodicLineV2Repository.saveAll(newPeriodicLineList);
    }

    /**
     * @param newPeriodicLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PeriodicLineV2 createPeriodicLineV2(PeriodicLineV2 newPeriodicLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        PeriodicLineV2 dbPeriodicLine = new PeriodicLineV2();
        log.info("newPeriodicLine : " + newPeriodicLine);
        BeanUtils.copyProperties(newPeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(newPeriodicLine));
        dbPeriodicLine.setDeletionIndicator(0L);
        dbPeriodicLine.setCreatedBy(loginUserID);
        dbPeriodicLine.setCreatedOn(DateUtils.getCurrentKWTDateTime());
        dbPeriodicLine.setCountedBy(loginUserID);
        dbPeriodicLine.setCountedOn(DateUtils.getCurrentKWTDateTime());
        return periodicLineV2Repository.save(dbPeriodicLine);
    }

    /**
     * @param assignHHTUsers
     * @param loginUserID
     * @return
     */
    public List<PeriodicLineV2> updateAssingHHTUserV2(List<AssignHHTUserCC> assignHHTUsers, String loginUserID) {
        log.info("assignHHTUsers : " + assignHHTUsers);
        List<PeriodicLineV2> responseList = new ArrayList<>();
        for (AssignHHTUserCC assignHHTUser : assignHHTUsers) {

            statusDescription = stagingLineV2Repository.getStatusDescription(72L, assignHHTUser.getLanguageId());

            periodicLineV2Repository.updateHHTUser(assignHHTUser.getCycleCounterId(),
                    assignHHTUser.getCycleCounterName(), 72L, statusDescription, loginUserID, new Date(),
                    assignHHTUser.getCompanyCodeId(), assignHHTUser.getPlantId(), assignHHTUser.getLanguageId(),
                    assignHHTUser.getWarehouseId(), assignHHTUser.getCycleCountNo(),
                    assignHHTUser.getStorageBin(), assignHHTUser.getItemCode(), assignHHTUser.getPackBarcodes());
            log.info("periodic Line updated with status 72, assigned user  : " + assignHHTUser.getCycleCounterId());
        }

        PeriodicHeaderV2 dbPeriodicHeader = periodicHeaderService.getPeriodicHeaderV2(
                assignHHTUsers.get(0).getCompanyCodeId(),
                assignHHTUsers.get(0).getPlantId(),
                assignHHTUsers.get(0).getLanguageId(),
                assignHHTUsers.get(0).getWarehouseId(),
                assignHHTUsers.get(0).getCycleCountNo());

        if (dbPeriodicHeader != null) {
            List<PeriodicLineV2> periodicLines = periodicLineService.getPeriodicLineV2(assignHHTUsers.get(0).getCompanyCodeId(),
                    assignHHTUsers.get(0).getPlantId(),
                    assignHHTUsers.get(0).getLanguageId(),
                    assignHHTUsers.get(0).getWarehouseId(),
                    assignHHTUsers.get(0).getCycleCountNo());

            long count_72 = periodicLines.stream().filter(a -> a.getStatusId() == 72L).count();
            log.info("status Count_72, periodicLine Size : " + count_72 + ", " + periodicLines.size());
            if (periodicLines.size() >= count_72) {
                dbPeriodicHeader.setStatusId(73L);
                log.info("assignHHTUser statusId : " + dbPeriodicHeader.getStatusId());
                statusDescription = stagingLineV2Repository.getStatusDescription(dbPeriodicHeader.getStatusId(), assignHHTUsers.get(0).getLanguageId());
                dbPeriodicHeader.setStatusDescription(statusDescription);
                periodicHeaderV2Repository.save(dbPeriodicHeader);
            }
        }

        responseList = getPeriodicLineV2(assignHHTUsers.get(0).getCompanyCodeId(),
                assignHHTUsers.get(0).getPlantId(),
                assignHHTUsers.get(0).getLanguageId(),
                assignHHTUsers.get(0).getWarehouseId(),
                assignHHTUsers.get(0).getCycleCountNo());

        return responseList;
    }

//				PeriodicLineV2 periodicLine = getPeriodicLineV2(
//						assignHHTUser.getCompanyCodeId(), assignHHTUser.getPlantId(),
//						assignHHTUser.getLanguageId(), assignHHTUser.getWarehouseId(), assignHHTUser.getCycleCountNo(),
//						assignHHTUser.getStorageBin(), assignHHTUser.getItemCode(), assignHHTUser.getPackBarcodes());
//				periodicLine.setCycleCounterId(assignHHTUser.getCycleCounterId());
//				periodicLine.setCycleCounterName(assignHHTUser.getCycleCounterName());
//				periodicLine.setStatusId(72L);
//				periodicLine.setCountedBy(loginUserID);
//				periodicLine.setCountedOn(new Date());
//				PeriodicLineV2 updatedPeriodicLine = periodicLineV2Repository.save(periodicLine);
//				log.info("updatedPeriodicLine : " + updatedPeriodicLine);
//
//				responseList.add(updatedPeriodicLine);
//			}

//			return responseList;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * @param updatePeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PeriodicLineV2> updatePeriodicLineForMobileCountV2(List<PeriodicLineV2> updatePeriodicLines,
                                                                   String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
        List<PeriodicLineV2> responsePeriodicLines = new ArrayList<>();
        List<PeriodicLineV2> createPeriodicLine = new ArrayList<>();
        List<PeriodicLineV2> updateBatchPeriodicLine = new ArrayList<>();
        for (PeriodicLineV2 updatePeriodicLine : updatePeriodicLines) {
            PeriodicLineV2 dbPeriodicLine = getPeriodicLineV2(
                    updatePeriodicLine.getCompanyCode(), updatePeriodicLine.getPlantId(),
                    updatePeriodicLine.getLanguageId(), updatePeriodicLine.getWarehouseId(), updatePeriodicLine.getCycleCountNo(),
                    updatePeriodicLine.getStorageBin(), updatePeriodicLine.getItemCode(), updatePeriodicLine.getPackBarcodes());
            if (dbPeriodicLine != null) { /* Update */
                BeanUtils.copyProperties(updatePeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(updatePeriodicLine));

                // INV_QTY
                double INV_QTY = updatePeriodicLine.getInventoryQuantity();
                dbPeriodicLine.setInventoryQuantity(INV_QTY);

                double OB_QTY = 0D;
                double IB_QTY = 0D;
                List<PutAwayLineV2> putAwayLineList = putAwayLineService.getPutAwayLineForPerpetualCountV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getManufacturerName(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getCountedOn());
                if (putAwayLineList != null) {
                    IB_QTY = putAwayLineList.stream().mapToDouble(a -> a.getPutawayConfirmedQty()).sum();
                    dbPeriodicLine.setInboundQuantity(IB_QTY);
                }
                List<PickupLineV2> pickupLineList = pickupLineService.getPickupLineForPerpetualCountV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getManufacturerName(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getCountedOn());
                if (pickupLineList != null) {
                    OB_QTY = pickupLineList.stream().mapToDouble(a -> a.getPickConfirmQty()).sum();
                    dbPeriodicLine.setOutboundQuantity(OB_QTY);
                }


                // CTD_QTY
                if (updatePeriodicLine.getCountedQty() != null) {
                    double CTD_QTY = updatePeriodicLine.getCountedQty();
                    dbPeriodicLine.setCountedQty(CTD_QTY);

                    // VAR_QTY = (INV_QTY + IB_QTY) - (OB_QTY + CTD_QTY);
                    double VAR_QTY = (INV_QTY + IB_QTY) - (OB_QTY + CTD_QTY);
                    dbPeriodicLine.setVarianceQty(VAR_QTY);

                    /*
                     * HardCoded Value "78" if VAR_QTY = 0 and
                     * Hardcodeed value"74" - if VAR_QTY is greater than or less than Zero
                     */
                    //status 78 commented because user doing count at first time
//                    if (VAR_QTY == 0) {
//                        dbPeriodicLine.setStatusId(78L);
//                    } else if (VAR_QTY > 0 || VAR_QTY < 0) {
                        dbPeriodicLine.setStatusId(74L);
//                    }
                }

                statusDescription = stagingLineV2Repository.getStatusDescription(dbPeriodicLine.getStatusId(), dbPeriodicLine.getLanguageId());
                dbPeriodicLine.setStatusDescription(statusDescription);

                dbPeriodicLine.setCountedBy(loginUserID);
                dbPeriodicLine.setCountedOn(DateUtils.getCurrentKWTDateTime());
                updateBatchPeriodicLine.add(dbPeriodicLine);
            } else {
                // Create new Record
                PeriodicLineV2 newPeriodicLineV2 = new PeriodicLineV2();
                BeanUtils.copyProperties(updatePeriodicLine, newPeriodicLineV2, CommonUtils.getNullPropertyNames(updatePeriodicLine));
                newPeriodicLineV2.setCycleCountNo(updatePeriodicLine.getCycleCountNo());

                statusDescription = stagingLineV2Repository.getStatusDescription(updatePeriodicLine.getStatusId(), updatePeriodicLine.getLanguageId());
                newPeriodicLineV2.setStatusDescription(statusDescription);

                newPeriodicLineV2.setDeletionIndicator(0L);
                newPeriodicLineV2.setCreatedBy(loginUserID);
                newPeriodicLineV2.setCreatedOn(DateUtils.getCurrentKWTDateTime());
                newPeriodicLineV2.setCountedBy(loginUserID);
                newPeriodicLineV2.setCountedOn(DateUtils.getCurrentKWTDateTime());
                createPeriodicLine.add(newPeriodicLineV2);
            }
        }
        responsePeriodicLines.addAll(createPeriodicLineV2(createPeriodicLine, loginUserID));
        responsePeriodicLines.addAll(periodicLineV2Repository.saveAll(updateBatchPeriodicLine));
        return responsePeriodicLines;
    }

    /**
     * @param cycleCountNo
     * @param updatePeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PeriodicUpdateResponseV2 updatePeriodicLineV2(String cycleCountNo, List<PeriodicLineV2> updatePeriodicLines,
                                                         String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
        List<PeriodicLineV2> responsePeriodicLines = new ArrayList<>();
        try {
            List<PeriodicLineV2> newPeriodicLines = new ArrayList<>();
            for (PeriodicLineV2 updatePeriodicLine : updatePeriodicLines) {
                PeriodicLineV2 dbPeriodicLine = getPeriodicLineV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getCycleCountNo(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getPackBarcodes());
                BeanUtils.copyProperties(updatePeriodicLine, dbPeriodicLine, CommonUtils.getNullPropertyNames(updatePeriodicLine));
                dbPeriodicLine.setRemarks(updatePeriodicLine.getRemarks());
                dbPeriodicLine.setCycleCountAction(updatePeriodicLine.getCycleCountAction());

                // INV_QTY
                double INV_QTY = updatePeriodicLine.getInventoryQuantity();
                double CTD_QTY = updatePeriodicLine.getCountedQty();

                double OB_QTY = 0D;
                double IB_QTY = 0D;
                List<PutAwayLineV2> putAwayLineList = putAwayLineService.getPutAwayLineForPerpetualCountV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getManufacturerName(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getCountedOn());
                if (putAwayLineList != null) {
                    IB_QTY = putAwayLineList.stream().mapToDouble(a -> a.getPutawayConfirmedQty()).sum();
                    dbPeriodicLine.setInboundQuantity(IB_QTY);
                }
                List<PickupLineV2> pickupLineList = pickupLineService.getPickupLineForPerpetualCountV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getManufacturerName(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getCountedOn());
                if (pickupLineList != null) {
                    OB_QTY = pickupLineList.stream().mapToDouble(a -> a.getPickConfirmQty()).sum();
                    dbPeriodicLine.setOutboundQuantity(OB_QTY);
                }

                /*
                 * 1. Action = WRITEOFF
                 * If ACTION = WRITEOFF , update ACTION field in PeriodicLine as WRITEOFF by passing unique fields and
                 * update in STATUS_ID field as "78"
                 */
                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(WRITEOFF)) {
                    dbPeriodicLine.setStatusId(78L);
                    dbPeriodicLine.setCycleCountAction(WRITEOFF);

                    statusDescription = stagingLineV2Repository.getStatusDescription(78L, dbPeriodicLine.getLanguageId());
                    dbPeriodicLine.setStatusDescription(statusDescription);

                    PeriodicLineV2 updatedPeriodicLine = periodicLineV2Repository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

                    /*
                     * Inventory table update
                     * ---------------------------
                     * Fetch CNT_QTY of the selected ITM_CODE and Pass WH_ID/ITM_CODE/ST_BIN/PACK_BARCODE values in INVENTORY table
                     * and replace INV_QTY as CNT_QTY
                     */
                    updateInventoryV2(updatedPeriodicLine);
                    createInventoryMovementV2(updatedPeriodicLine);
                }

                /*
                 * 2. Action = SKIP
                 * if ACTION = SKIP in UI,  update ACTION field in PeriodicLine as SKIP by passing unique fields
                 * and update in STATUS_ID field as "78"
                 */
                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(SKIP)) {

                    if (updatePeriodicLine.getSecondCountedQty() == null && updatePeriodicLine.getFirstCountedQty() != null) {
                        dbPeriodicLine.setSecondCountedQty(updatePeriodicLine.getCountedQty());
                        double VAR_QTY = (INV_QTY + IB_QTY) - (OB_QTY + CTD_QTY);
                        dbPeriodicLine.setVarianceQty(VAR_QTY);
                    }
                    if (updatePeriodicLine.getFirstCountedQty() == null) {
                        dbPeriodicLine.setFirstCountedQty(updatePeriodicLine.getCountedQty());
                    }
                    dbPeriodicLine.setStatusId(78L);
                    dbPeriodicLine.setCycleCountAction(SKIP);

                    statusDescription = stagingLineV2Repository.getStatusDescription(78L, dbPeriodicLine.getLanguageId());
                    dbPeriodicLine.setStatusDescription(statusDescription);

                    PeriodicLineV2 updatedPeriodicLine = periodicLineV2Repository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

                    /*
                     * Inventory table update
                     * ---------------------------
                     * Insert a new record by passing WH_ID/ITM_CODE/PACK_BARCODE/ST_BIN (fetch ST_BIN
                     * from STORAGEBIN table where BIN_CL_ID=5) values in INVENTORY table and append INV_QTY as
                     * VAR_QTY
                     */
//					createInventory (updatedPeriodicLine);
//					createInventoryMovement (updatedPeriodicLine) ;
                }

                /*
                 * 3. Action = RECOUNT (default Action Value)
                 * If ACTION = RECOUNT, update ACTION field in PeriodicLine as SKIP by passing unique fields
                 * and update in STATUS_ID field as "78"
                 */
                log.info("---------->updatePeriodicLine data : " + updatePeriodicLine);
                log.info("---------->RECOUNT : " + RECOUNT);

//                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(RECOUNT) && updatePeriodicLine.getStatusId() == 75L) {
////                    dbPerpetualLine.setStatusId(78L);
//
//                    if (updatePeriodicLine.getSecondCountedQty() == null && updatePeriodicLine.getFirstCountedQty() != null) {
//                        dbPeriodicLine.setSecondCountedQty(updatePeriodicLine.getCountedQty());
//                        dbPeriodicLine.setCycleCountAction(SKIP);
//                        dbPeriodicLine.setCountedQty(0D);
//                        dbPeriodicLine.setStatusId(78L);
//                    }
//
////                    if (updatePeriodicLine.getFirstCountedQty() == null && updatePeriodicLine.getSecondCountedQty() == null) {
////                        dbPeriodicLine.setFirstCountedQty(updatePeriodicLine.getCountedQty());
////                        dbPeriodicLine.setCycleCountAction(RECOUNT);
////                        dbPeriodicLine.setCountedQty(0D);
////                        dbPeriodicLine.setStatusId(75L);
////                    }
//
////                    if (updatePeriodicLine.getSecondCountedQty() != null && updatePeriodicLine.getFirstCountedQty() != null) {
////                        dbPeriodicLine.setStatusId(78L);
////                        dbPeriodicLine.setCycleCountAction(SKIP);
////                    }
//
//                    statusDescription = stagingLineV2Repository.getStatusDescription(dbPeriodicLine.getStatusId(), dbPeriodicLine.getLanguageId());
//                    dbPeriodicLine.setStatusDescription(statusDescription);
//
//                    PeriodicLineV2 updatedPeriodicLine = periodicLineV2Repository.save(dbPeriodicLine);
//                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
//                    responsePeriodicLines.add(updatedPeriodicLine);
//
////                    /*
////                     * Preparation of new PeriodicLine
////                     */
////                    PeriodicLineV2 newPeriodicLine = new PeriodicLineV2();
////                    BeanUtils.copyProperties(updatedPeriodicLine, newPeriodicLine, CommonUtils.getNullPropertyNames(updatedPeriodicLine));
////                    newPeriodicLine.setStatusId(78L);
////                    newPeriodicLines.add(newPeriodicLine);
//                }

                if (updatePeriodicLine.getCycleCountAction().equalsIgnoreCase(RECOUNT)) {

//                    if (updatePeriodicLine.getFirstCountedQty() == null && updatePeriodicLine.getSecondCountedQty() == null) {
                    dbPeriodicLine.setFirstCountedQty(updatePeriodicLine.getCountedQty());
                    double VAR_QTY = (INV_QTY + IB_QTY) - (OB_QTY + CTD_QTY);
                    dbPeriodicLine.setVarianceQty(VAR_QTY);
                    dbPeriodicLine.setCycleCountAction(RECOUNT);
                    dbPeriodicLine.setCountedQty(0D);
                    dbPeriodicLine.setStatusId(75L);
//                    }

                    statusDescription = stagingLineV2Repository.getStatusDescription(dbPeriodicLine.getStatusId(), dbPeriodicLine.getLanguageId());
                    dbPeriodicLine.setStatusDescription(statusDescription);

                    PeriodicLineV2 updatedPeriodicLine = periodicLineV2Repository.save(dbPeriodicLine);
                    log.info("updatedPeriodicLine : " + updatedPeriodicLine);
                    responsePeriodicLines.add(updatedPeriodicLine);

//                    /*
//                     * Preparation of new PeriodicLine
//                     */
//                    PeriodicLineV2 newPeriodicLine = new PeriodicLineV2();
//                    BeanUtils.copyProperties(updatedPeriodicLine, newPeriodicLine, CommonUtils.getNullPropertyNames(updatedPeriodicLine));
//                    newPeriodicLine.setStatusId(78L);
//                    newPeriodicLines.add(newPeriodicLine);
                }
            }

//            PeriodicHeaderV2 newlyCreatedPeriodicHeader = new PeriodicHeaderV2();
//            if (!newPeriodicLines.isEmpty()) {
//                log.info("newPeriodicLines : " + newPeriodicLines);
//
//                // Create new PeriodicHeader and Lines
//                PeriodicHeaderEntityV2 createdPeriodicHeader = createNewHeaderNLinesV2(cycleCountNo, newPeriodicLines, loginUserID);
//                log.info("createdPeriodicHeader : " + createdPeriodicHeader);
//                BeanUtils.copyProperties(createdPeriodicHeader, newlyCreatedPeriodicHeader, CommonUtils.getNullPropertyNames(createdPeriodicHeader));
//            }

            // Update new PeriodicHeader
            PeriodicHeaderV2 dbPeriodicHeader = periodicHeaderService.getPeriodicHeaderV2(
                    updatePeriodicLines.get(0).getCompanyCode(),
                    updatePeriodicLines.get(0).getPlantId(),
                    updatePeriodicLines.get(0).getLanguageId(),
                    updatePeriodicLines.get(0).getWarehouseId(),
                    cycleCountNo);
//            PeriodicHeaderV2 updatePeriodicHeader = new PeriodicHeaderV2();
//            BeanUtils.copyProperties(dbPeriodicHeader, updatePeriodicHeader, CommonUtils.getNullPropertyNames(dbPeriodicHeader));
            PeriodicHeaderV2 updatedPeriodicHeader = periodicHeaderService.updatePeriodicHeaderFromPeriodicLineV2(
                    dbPeriodicHeader.getCompanyCode(),
                    dbPeriodicHeader.getPlantId(),
                    dbPeriodicHeader.getLanguageId(),
                    dbPeriodicHeader.getWarehouseId(),
                    dbPeriodicHeader.getCycleCountTypeId(),
                    dbPeriodicHeader.getCycleCountNo(), loginUserID);
            log.info("updatedPeriodicHeader : " + updatedPeriodicHeader);

            PeriodicUpdateResponseV2 response = new PeriodicUpdateResponseV2();
            response.setPeriodicHeader(dbPeriodicHeader);
            response.setPeriodicLines(responsePeriodicLines);
            log.info("PeriodicUpdateResponse------> : " + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @param cycleCountNo
     * @param newPeriodicLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private PeriodicHeaderEntityV2 createNewHeaderNLinesV2(String cycleCountNo, List<PeriodicLineV2> newPeriodicLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        log.info("newPeriodicLines : " + newPeriodicLines);
        if (newPeriodicLines != null) {
            PeriodicHeaderV2 dbPeriodicHeader = periodicHeaderService.getPeriodicHeaderV2(
                    newPeriodicLines.get(0).getCompanyCode(),
                    newPeriodicLines.get(0).getPlantId(),
                    newPeriodicLines.get(0).getLanguageId(),
                    newPeriodicLines.get(0).getWarehouseId(),
                    cycleCountNo);
            PeriodicHeaderEntityV2 newPeriodicHeader = new PeriodicHeaderEntityV2();
            BeanUtils.copyProperties(dbPeriodicHeader, newPeriodicHeader, CommonUtils.getNullPropertyNames(dbPeriodicHeader));
            newPeriodicHeader.setPeriodicLine(newPeriodicLines);
            PeriodicHeaderEntityV2 createdPeriodicHeader = periodicHeaderService.createPeriodicHeaderV2(newPeriodicHeader, loginUserID);
            log.info("createdPeritodicHeader : " + createdPeriodicHeader);
            return createdPeriodicHeader;
        }
        return null;
    }

    /**
     * @param updatePeriodicLine
     * @return
     */
    private InventoryV2 updateInventoryV2(PeriodicLineV2 updatePeriodicLine) throws ParseException {
        InventoryV2 inventory = inventoryService.getInventoryV2(
                updatePeriodicLine.getCompanyCode(),
                updatePeriodicLine.getPlantId(),
                updatePeriodicLine.getLanguageId(),
                updatePeriodicLine.getWarehouseId(),
                updatePeriodicLine.getPackBarcodes(), updatePeriodicLine.getItemCode(),
                updatePeriodicLine.getStorageBin());
        if (inventory != null) {
            InventoryV2 newinventory = new InventoryV2();
            BeanUtils.copyProperties(inventory, newinventory, CommonUtils.getNullPropertyNames(inventory));
            newinventory.setInventoryId(System.currentTimeMillis());
            newinventory.setInventoryQuantity(updatePeriodicLine.getCountedQty());
            InventoryV2 updatedInventory = inventoryV2Repository.save(newinventory);
            log.info("updatedInventory : " + updatedInventory);
            return updatedInventory;
        } else {
            return createInventoryV2(updatePeriodicLine);
        }
    }

    /**
     * @param updatePeriodicLine
     * @return
     */
    private InventoryV2 createInventoryV2(PeriodicLineV2 updatePeriodicLine) throws ParseException {
        InventoryV2 inventory = new InventoryV2();
        BeanUtils.copyProperties(updatePeriodicLine, inventory, CommonUtils.getNullPropertyNames(updatePeriodicLine));
        inventory.setInventoryId(System.currentTimeMillis());
        inventory.setCompanyCodeId(updatePeriodicLine.getCompanyCode());

        // VAR_ID, VAR_SUB_ID, STR_MTD, STR_NO ---> Hard coded as '1'
        inventory.setVariantCode(1L);
        inventory.setVariantSubCode("1");
        inventory.setStorageMethod("1");
        inventory.setBatchSerialNumber("1");
        inventory.setBinClassId(5L);

        // ST_BIN ---Pass WH_ID/BIN_CL_ID=5 in STORAGEBIN table and fetch ST_BIN value and update
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        StorageBinV2 storageBin =
                mastersService.getStorageBin(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(), 5L, authTokenForMastersService.getAccess_token());
        inventory.setStorageBin(storageBin.getStorageBin());

//		List<IImbasicData1> imbasicdata1 = imbasicdata1Repository.findByItemCode(inventory.getItemCode());
        ImBasicData imBasicData = new ImBasicData();
        imBasicData.setCompanyCodeId(updatePeriodicLine.getCompanyCode());
        imBasicData.setPlantId(updatePeriodicLine.getPlantId());
        imBasicData.setLanguageId(updatePeriodicLine.getLanguageId());
        imBasicData.setWarehouseId(updatePeriodicLine.getWarehouseId());
        imBasicData.setItemCode(updatePeriodicLine.getItemCode());
        imBasicData.setManufacturerName(updatePeriodicLine.getManufacturerName());
        ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
        log.info("imbasicdata1 : " + imbasicdata1);

        if (imbasicdata1 != null) {
            inventory.setReferenceField8(imbasicdata1.getDescription());
            inventory.setReferenceField9(imbasicdata1.getManufacturerPartNo());
            inventory.setManufacturerCode(imbasicdata1.getManufacturerPartNo());
            inventory.setManufacturerName(imbasicdata1.getManufacturerPartNo());
        }
        if (storageBin != null) {
            inventory.setReferenceField10(storageBin.getStorageSectionId());
            inventory.setReferenceField5(storageBin.getAisleNumber());
            inventory.setReferenceField6(storageBin.getShelfId());
            inventory.setReferenceField7(storageBin.getRowId());
        }

        // STCK_TYP_ID
        inventory.setStockTypeId(1L);

        // SP_ST_IND_ID
        inventory.setSpecialStockIndicatorId(1L);

        // INV_QTY
        inventory.setInventoryQuantity(updatePeriodicLine.getVarianceQty());

        // INV_UOM
        inventory.setInventoryUom(updatePeriodicLine.getInventoryUom());

        inventory.setCreatedBy(updatePeriodicLine.getCreatedBy());
        inventory.setCreatedOn(updatePeriodicLine.getCreatedOn());
        inventory.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
        InventoryV2 createdinventory = inventoryV2Repository.save(inventory);
        log.info("created inventory : " + createdinventory);
        return createdinventory;
    }

    /**
     * @param updatedPeriodicLine
     * @return
     */
    private InventoryMovement createInventoryMovementV2(PeriodicLineV2 updatedPeriodicLine) throws ParseException {
        InventoryMovement inventoryMovement = new InventoryMovement();
        BeanUtils.copyProperties(updatedPeriodicLine, inventoryMovement, CommonUtils.getNullPropertyNames(updatedPeriodicLine));

        inventoryMovement.setLanguageId(updatedPeriodicLine.getLanguageId());
        inventoryMovement.setCompanyCodeId(updatedPeriodicLine.getCompanyCode());
        inventoryMovement.setPlantId(updatedPeriodicLine.getPlantId());
        inventoryMovement.setWarehouseId(updatedPeriodicLine.getWarehouseId());

        // MVT_TYP_ID
        inventoryMovement.setMovementType(4L);

        // SUB_MVT_TYP_ID
        inventoryMovement.setSubmovementType(1L);

        // ITEM_TEXT
        // Pass ITM_CODE in IMBASICDATA table and fetch ITEM_TEXT values
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData imBasicData = new ImBasicData();
        imBasicData.setCompanyCodeId(updatedPeriodicLine.getCompanyCode());
        imBasicData.setPlantId(updatedPeriodicLine.getPlantId());
        imBasicData.setLanguageId(updatedPeriodicLine.getLanguageId());
        imBasicData.setWarehouseId(updatedPeriodicLine.getWarehouseId());
        imBasicData.setItemCode(updatedPeriodicLine.getItemCode());
        imBasicData.setManufacturerName(updatedPeriodicLine.getManufacturerName());
        ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
        log.info("imbasicdata1 : " + imbasicdata1);

        if (imbasicdata1 != null) {

            inventoryMovement.setDescription(imbasicdata1.getDescription());

            // MFR_PART of ITM_CODE from BASICDATA1
            inventoryMovement.setManufacturerName(imbasicdata1.getManufacturerPartNo());
        }

        inventoryMovement.setCompanyDescription(updatedPeriodicLine.getCompanyDescription());
        inventoryMovement.setPlantDescription(updatedPeriodicLine.getPlantDescription());
        inventoryMovement.setWarehouseDescription(updatedPeriodicLine.getWarehouseDescription());
        inventoryMovement.setBarcodeId(updatedPeriodicLine.getBarcodeId());
        /*
         * MVT_QTY_VAL
         * -----------------
         * Hard Coded value "P", if VAR_QTY is negative and Hard coded value "N", if VAR_QTY is positive
         */
        if (updatedPeriodicLine.getVarianceQty() < 0) {
            inventoryMovement.setMovementQtyValue("P");
        } else if (updatedPeriodicLine.getVarianceQty() > 0) {
            inventoryMovement.setMovementQtyValue("N");
        }

        inventoryMovement.setBatchSerialNumber("1");
        inventoryMovement.setMovementDocumentNo(updatedPeriodicLine.getCycleCountNo());
        inventoryMovement.setMovementQty(updatedPeriodicLine.getVarianceQty());
        inventoryMovement.setCreatedBy(updatedPeriodicLine.getCreatedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(DateUtils.getCurrentKWTDateTime());
        inventoryMovement.setCreatedBy(updatedPeriodicLine.getCreatedBy());

        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        log.info("created InventoryMovement : " + inventoryMovement);
        return inventoryMovement;
    }

    /**
     * deletePeriodicLine
     *
     * @param loginUserID
     * @param storageBin
     */
    public void deletePeriodicLineV2(String warehouseId, String cycleCountNo, String storageBin, String itemCode, String packBarcodes, String loginUserID) throws ParseException {
        PeriodicLine periodicLine = getPeriodicLine(warehouseId, cycleCountNo, storageBin, itemCode, packBarcodes);
        if (periodicLine != null) {
            periodicLine.setDeletionIndicator(1L);
            periodicLine.setConfirmedBy(loginUserID);
            periodicLine.setConfirmedOn(DateUtils.getCurrentKWTDateTime());
            periodicLineRepository.save(periodicLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + storageBin);
        }
    }

    /**
     * @param cycleCountNo
     * @param periodicLineV2s
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public WarehouseApiResponse updatePeriodicLineConfirmV2(String cycleCountNo, List<PeriodicLineV2> periodicLineV2s,
                                                            String loginUserID) throws IllegalAccessException, InvocationTargetException {
        List<PeriodicLineV2> responsePeriodicLines = new ArrayList<>();
        List<PeriodicLineV2> newPeriodicLine = new ArrayList<>();
        List<String> statusId78 = new ArrayList<>();
        try {
            for (PeriodicLineV2 updatePeriodicLine : periodicLineV2s) {
                PeriodicLineV2 dbPeriodicLine = getPeriodicLineV2(
                        updatePeriodicLine.getCompanyCode(),
                        updatePeriodicLine.getPlantId(),
                        updatePeriodicLine.getLanguageId(),
                        updatePeriodicLine.getWarehouseId(),
                        updatePeriodicLine.getCycleCountNo(),
                        updatePeriodicLine.getStorageBin(),
                        updatePeriodicLine.getItemCode(),
                        updatePeriodicLine.getPackBarcodes());

                if (dbPeriodicLine.getStatusId() == 78L) {
                    statusId78.add("True");
                    responsePeriodicLines.add(dbPeriodicLine);
                }
            }

            Long periodicLineCount = periodicLineV2s.stream().count();
            Long statusIdCount = statusId78.stream().filter(a -> a.equalsIgnoreCase("True")).count();

            if (periodicLineCount != statusIdCount) {
                throw new BadRequestException("Perpetual Lines are not completely Processed");
            }

            if (periodicLineCount == statusIdCount) {
                // Update new PerpetualHeader
                PeriodicHeaderV2 dbPeriodicHeaderV2 = periodicHeaderService.getPeriodicHeaderV2(
                        periodicLineV2s.get(0).getCompanyCode(),
                        periodicLineV2s.get(0).getPlantId(),
                        periodicLineV2s.get(0).getLanguageId(),
                        periodicLineV2s.get(0).getWarehouseId(),
                        cycleCountNo);
                if (dbPeriodicHeaderV2.getStatusId() != 78L) {
                    throw new BadRequestException("Perpetual Header are not completely Processed");
                }
            }

            WarehouseApiResponse warehouseApiResponse = null;
            AuthToken authTokenForConnectorService = authTokenService.getConnectorServiceAuthToken();
            if (responsePeriodicLines != null || !responsePeriodicLines.isEmpty()) {

                for (PeriodicLineV2 dbPeriodicLine : responsePeriodicLines) {
                    PeriodicLineTempV2 periodicLineTempV2 = new PeriodicLineTempV2();
                    BeanUtils.copyProperties(dbPeriodicLine, periodicLineTempV2, CommonUtils.getNullPropertyNames(dbPeriodicLine));
                    periodicLineTempV2Repository.save(periodicLineTempV2);
                }
                List<IKeyValuePair> updatePeriodicLine = periodicLineTempV2Repository.getPeriodicHeader(
                        periodicLineV2s.get(0).getWarehouseId(),
                        periodicLineV2s.get(0).getCompanyCode(),
                        periodicLineV2s.get(0).getPlantId(),
                        cycleCountNo,
                        periodicLineV2s.get(0).getLanguageId());
                List<UpdatePeriodicLineV2> updatePeriodicLineV2s = new ArrayList<>();
                for (IKeyValuePair iKeyValuePair : updatePeriodicLine) {
                    UpdatePeriodicLineV2 updatePeriodicLineV2 = new UpdatePeriodicLineV2();
                    updatePeriodicLineV2.setCycleCountNo(iKeyValuePair.getReferenceCycleCountNo());
                    updatePeriodicLineV2.setItemCode(iKeyValuePair.getItemCode());
                    updatePeriodicLineV2.setManufacturerName(iKeyValuePair.getManufacturerName());
                    updatePeriodicLineV2.setInventoryQty(iKeyValuePair.getInventoryQty());
                    updatePeriodicLineV2s.add(updatePeriodicLineV2);
                }
                //update cyclecount order Table
                updatePeriodicStockCountOrderTable(updatePeriodicLineV2s);
                //push to AMS
                warehouseApiResponse = updatePeriodicLine(updatePeriodicLineV2s, authTokenForConnectorService.getAccess_token());
                if(warehouseApiResponse.getStatusCode().equalsIgnoreCase("200")){
                    PeriodicHeaderV2 dbPeriodicHeader = periodicHeaderService.getPeriodicHeaderV2(
                            periodicLineV2s.get(0).getCompanyCode(),
                            periodicLineV2s.get(0).getPlantId(),
                            periodicLineV2s.get(0).getLanguageId(),
                            periodicLineV2s.get(0).getWarehouseId(),
                            cycleCountNo);
                    log.info("update perpetualHeader: --->ref_field_1--->True" + dbPeriodicHeader);
                    dbPeriodicHeader.setReferenceField1("True");
                    periodicHeaderV2Repository.save(dbPeriodicHeader);
                }
                periodicLineTempV2Repository.truncateTblperiodictempline();
            }

            return warehouseApiResponse;


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param updateStockCountLines
     */
    public void updatePeriodicStockCountOrderTable(List<UpdatePeriodicLineV2> updateStockCountLines) {
        if (updateStockCountLines != null) {
            log.info("Perpertual Lines to be Updated:" + updateStockCountLines);
            for (UpdatePeriodicLineV2 dbPeriodicLine : updateStockCountLines) {
                CycleCountLine updatePplCountedQty = cycleCountLineRepository.findByCycleCountNoAndItemCodeAndManufacturerCode(
                        dbPeriodicLine.getCycleCountNo(),
                        dbPeriodicLine.getItemCode(),
                        dbPeriodicLine.getManufacturerName());
                if (updatePplCountedQty != null) {
                    log.info("Periodic Line to be Updated:" + updatePplCountedQty);

                    updatePplCountedQty.setCountedQty(dbPeriodicLine.getInventoryQty());
                    updatePplCountedQty.setIsCompleted("1");
//                    PerpetualLine updateCountedQty = perpetualLineRepository.save(updatePplCountedQty);
                    try {
                        cycleCountLineRepository.save(updatePplCountedQty);
//                        cycleCountLineRepository.updatePplLine(dbPeriodicLine.getInventoryQty(),
//                                1L,
//                                dbPeriodicLine.getCycleCountNo(),
//                                dbPeriodicLine.getItemCode(),
//                                dbPeriodicLine.getManufacturerName());
                        log.info("Periodic Line CountedQty Updated, CountedQty: " + dbPeriodicLine + ", " + dbPeriodicLine.getInventoryQty());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    // PATCH
    public WarehouseApiResponse updatePeriodicLine(List<UpdatePeriodicLineV2> updatePeriodicLineV2s, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(updatePeriodicLineV2s, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(getConnectorServiceApiUrl() + "periodic/updateCountedQty");

            ResponseEntity<WarehouseApiResponse> result =
                    restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, WarehouseApiResponse.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}