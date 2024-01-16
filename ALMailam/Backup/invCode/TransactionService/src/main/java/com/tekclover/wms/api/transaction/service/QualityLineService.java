package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.*;
import com.tekclover.wms.api.transaction.model.inbound.inventory.*;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.outbound.OutboundHeader;
import com.tekclover.wms.api.transaction.model.outbound.OutboundLine;
import com.tekclover.wms.api.transaction.model.outbound.OutboundLineInterim;
import com.tekclover.wms.api.transaction.model.outbound.UpdateOutboundHeader;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2.OrderManagementLineV2;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2.SearchOrderManagementLineV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.SearchPickupHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.*;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.QualityHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.QualityLineV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.SearchQualityHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.SearchQualityLineV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.QualityLineSpecification;
import com.tekclover.wms.api.transaction.repository.specification.QualityLineV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class QualityLineService extends BaseService {

    @Autowired
    private QualityLineRepository qualityLineRepository;

    @Autowired
    private OutboundLineRepository outboundLineRepository;

    @Autowired
    private QualityHeaderService qualityHeaderService;

    @Autowired
    private OutboundHeaderService outboundHeaderService;

    @Autowired
    private OutboundLineService outboundLineService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MastersService mastersService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Autowired
    private ImBasicData1Repository imbasicdata1Repository;

    @Autowired
    private OutboundLineInterimRepository outboundLineInterimRepository;

    //------------------------------------------------------------------------------------------------------
    @Autowired
    private OrderManagementLineService orderManagementLineService;
    @Autowired
    private OrderManagementLineV2Repository orderManagementLineV2Repository;
    @Autowired
    private OutboundLineV2Repository outboundLineV2Repository;
    @Autowired
    private InventoryV2Repository inventoryV2Repository;
    @Autowired
    private PickupHeaderService pickupHeaderService;
    @Autowired
    private QualityLineV2Repository qualityLineV2Repository;
    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;
    String statusDescription = null;
    //------------------------------------------------------------------------------------------------------

    /**
     * getQualityLines
     *
     * @return
     */
    public List<QualityLine> getQualityLines() {
        List<QualityLine> qualityLineList = qualityLineRepository.findAll();
        qualityLineList = qualityLineList.stream().filter(n -> n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return qualityLineList;
    }

    /**
     * getQualityLine
     *
     * @return
     */
    public QualityLine getQualityLine(String partnerCode) {
        QualityLine qualityLine = qualityLineRepository.findByPartnerCode(partnerCode).orElse(null);
        if (qualityLine != null && qualityLine.getDeletionIndicator() == 0) {
            return qualityLine;
        } else {
            throw new BadRequestException("The given QualityLine ID : " + partnerCode + " doesn't exist.");
        }
    }

    /**
     * // Fetch WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE/OB_LINE_NO/ITM_CODE
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLine getQualityLine(String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode,
                                      Long lineNumber, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        throw new BadRequestException("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:"
                + preOutboundNo + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:"
                + lineNumber + ",itemCode:" + itemCode + " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLine getQualityLineValidated(String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, Long lineNumber, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<QualityLine> getQualityLineForReversal(String warehouseId, String preOutboundNo, String refDocNumber,
                                                       String partnerCode, Long lineNumber, String itemCode) {
        List<QualityLine> qualityLine = qualityLineRepository
                .findAllByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLine getQualityLineForUpdate(String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, Long lineNumber, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        log.info("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                + ",itemCode:" + itemCode + " doesn't exist.");
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<QualityLine> getQualityLineForUpdateForDeliverConformation(String warehouseId, String preOutboundNo,
                                                                           String refDocNumber, String partnerCode, Long lineNumber, String itemCode) {
        List<QualityLine> qualityLine = qualityLineRepository
                .findAllByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        log.info("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                + ",itemCode:" + itemCode + " doesn't exist.");
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumbers
     * @param itemCodes
     * @return
     */
    public List<QualityLine> getQualityLine(String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode, List<Long> lineNumbers, List<String> itemCodes) {
        List<QualityLine> qualityLine = qualityLineRepository
                .findAllByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberInAndItemCodeInAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumbers, itemCodes, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLine getQualityLineForUpdate(String warehouseId, String preOutboundNo, String refDocNumber,
                                                String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        throw new BadRequestException(
                "The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                        + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                        + ",qualityInspectionNo:" + qualityInspectionNo + ",itemCode:" + itemCode + " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLine findDuplicateRecord(String warehouseId, String preOutboundNo, String refDocNumber,
                                            String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLine findQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                        String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode) {
        QualityLine qualityLine = qualityLineRepository
                .findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param searchQualityLine
     * @return
     * @throws ParseException
     */
    public List<QualityLine> findQualityLine(SearchQualityLine searchQualityLine) throws ParseException {
        QualityLineSpecification spec = new QualityLineSpecification(searchQualityLine);
        List<QualityLine> results = qualityLineRepository.findAll(spec);
        return results;
    }

    /**
     * @param qualityLineList
     * @return
     */
    public static List<AddQualityLine> getDuplicates(List<AddQualityLine> qualityLineList) {
        return getDuplicatesMap(qualityLineList).values().stream()
                .filter(duplicates -> duplicates.size() > 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<AddQualityLine> getDuplicatesV2(List<AddQualityLine> qualityLineList) {
        return getDuplicatesMapV2(qualityLineList).values().stream()
                .filter(duplicates -> duplicates.size() > 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * @param addQualityLineList
     * @return
     */
    private static Map<String, List<AddQualityLine>> getDuplicatesMap(List<AddQualityLine> addQualityLineList) {
        return addQualityLineList.stream().collect(Collectors.groupingBy(AddQualityLine::uniqueAttributes));
    }

    private static Map<String, List<AddQualityLine>> getDuplicatesMapV2(List<AddQualityLine> addQualityLineList) {
        return addQualityLineList.stream().collect(Collectors.groupingBy(AddQualityLine::uniqueAttributes));
    }

    /**
     * @param newQualityLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLine> createQualityLine(List<AddQualityLine> newQualityLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        try {
            log.info("-------createQualityLine--------called-------> " + new Date());

            List<AddQualityLine> dupQualityLines = getDuplicates(newQualityLines);
            log.info("-------dupQualityLines--------> " + dupQualityLines);
            if (dupQualityLines != null && !dupQualityLines.isEmpty()) {
                newQualityLines.removeAll(dupQualityLines);
                newQualityLines.add(dupQualityLines.get(0));
                log.info("-------newQualityLines---removed-dupQualityLines-----> " + newQualityLines);
            }

            /*
             * The below flag helps to avoid duplicate request and updating of outboundline
             * table
             */
            List<QualityLine> createdQualityLineList = new ArrayList<>();
            for (AddQualityLine newQualityLine : newQualityLines) {
                log.info("Input from UI:  " + newQualityLine);

                QualityLine dbQualityLine = new QualityLine();
                BeanUtils.copyProperties(newQualityLine, dbQualityLine,
                        CommonUtils.getNullPropertyNames(newQualityLine));

                // STATUS_ID - HardCoded Value "55"
                dbQualityLine.setStatusId(55L);
                dbQualityLine.setDeletionIndicator(0L);
                dbQualityLine.setQualityCreatedBy(loginUserID);
                dbQualityLine.setQualityUpdatedBy(loginUserID);
                dbQualityLine.setQualityCreatedOn(new Date());
                dbQualityLine.setQualityUpdatedOn(new Date());

                /*
                 * String warehouseId, String preOutboundNo, String refDocNumber, String
                 * partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode
                 */
                QualityLine existingQualityLine = findDuplicateRecord(newQualityLine.getWarehouseId(),
                        newQualityLine.getPreOutboundNo(), newQualityLine.getRefDocNumber(),
                        newQualityLine.getPartnerCode(), newQualityLine.getLineNumber(),
                        newQualityLine.getQualityInspectionNo(), newQualityLine.getItemCode());
                log.info("existingQualityLine record status : " + existingQualityLine);

                /*
                 * Checking whether the record already exists (created) or not. If it is not
                 * created then only the rest of the logic has been carry forward
                 */
                if (existingQualityLine == null) {
                    QualityLine createdQualityLine = qualityLineRepository.save(dbQualityLine);
                    log.info("createdQualityLine: " + createdQualityLine);

                    // createOutboundLineInterim
                    createOutboundLineInterim(createdQualityLine);
                    createdQualityLineList.add(createdQualityLine);
                }
            }

            /*
             * Based on created QualityLine List, updating respective tables
             */
            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            for (QualityLine dbQualityLine : createdQualityLineList) {
                /*-----------------STATUS updates in QualityHeader-----------------------*/
                try {
                    UpdateQualityHeader updateQualityHeader = new UpdateQualityHeader();
                    updateQualityHeader.setStatusId(55L);
                    StatusId idStatus = idmasterService.getStatus(55L, dbQualityLine.getWarehouseId(), authTokenForIDService.getAccess_token());
                    updateQualityHeader.setReferenceField10(idStatus.getStatus());
                    QualityHeader qualityHeader = qualityHeaderService.updateQualityHeader(
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getQualityInspectionNo(),
                            dbQualityLine.getActualHeNo(), loginUserID, updateQualityHeader);
                    log.info("qualityHeader updated : " + qualityHeader);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log.info("qualityHeader updated Error : " + e1.toString());
                }

                /*-------------------OUTBOUNDLINE------Update---------------------------*/
                /*
                 * Pass WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE /OB_LINE_NO/_ITM_CODE values in
                 * QUALITYILINE table and fetch QC_QTY values and pass the same values in
                 * OUTBOUNDLINE table and update DLV_QTY
                 *
                 * Pass Unique keys in OUTBOUNDLINE table and update STATUS_ID as "57"
                 */
                Long NUM_RAN_CODE = 12L;
                String DLV_ORD_NO = getNextRangeNumber(NUM_RAN_CODE, dbQualityLine.getWarehouseId());

                updateOutboundLine(dbQualityLine, DLV_ORD_NO);
                try {
                    /*-------------------OUTBOUNDHEADER------Update---------------------------*/
                    boolean isStatus57 = false;
                    List<OutboundLine> outboundLines = outboundLineService.getOutboundLine(
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode());
//					log.info("outboundLine re-queried-----> : " + outboundLines);

                    outboundLines = outboundLines.stream().filter(o -> o.getStatusId() == 57L)
                            .collect(Collectors.toList());
                    if (outboundLines != null) {
                        isStatus57 = true;
                    }

                    UpdateOutboundHeader updateOutboundHeader = new UpdateOutboundHeader();
                    updateOutboundHeader.setDeliveryOrderNo(DLV_ORD_NO);
                    if (isStatus57) { // If Status if 57 then update OutboundHeader with Status 57.
                        updateOutboundHeader.setStatusId(57L);
                    }

                    OutboundHeader outboundHeader = outboundHeaderService.updateOutboundHeader(
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode(), updateOutboundHeader,
                            loginUserID);
                    log.info("outboundHeader updated : " + outboundHeader);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log.info("outboundHeader updated error: " + e1.toString());
                }

                /*-----------------Inventory Updates--------------------------------------*/
                // Pass WH_ID/ITM_CODE/ST_BIN/PACK_BARCODE in INVENTORY table
                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
                Long BIN_CLASS_ID = 4L;
                StorageBin storageBin = mastersService.getStorageBin(dbQualityLine.getWarehouseId(), BIN_CLASS_ID,
                        authTokenForMastersService.getAccess_token());
                Warehouse warehouse = getWarehouse(dbQualityLine.getWarehouseId());
                Inventory inventory = null;
                try {
                    inventory = inventoryService.getInventory(dbQualityLine.getWarehouseId(),
                            dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(),
                            storageBin.getStorageBin());
                    log.info("inventory---BIN_CLASS_ID-4----> : " + inventory);

                    if (inventory != null) {
                        Double INV_QTY = inventory.getInventoryQuantity() - dbQualityLine.getQualityQty();
                        log.info("Calculated inventory INV_QTY: " + INV_QTY);
                        inventory.setInventoryQuantity(INV_QTY);

                        // INV_QTY > 0 then, update Inventory Table
                        inventory = inventoryRepository.save(inventory);
                        log.info("inventory updated : " + inventory);

                        if (INV_QTY == 0) {
                            log.info("inventory INV_QTY: " + INV_QTY);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                /*-------------------Inserting record in InventoryMovement-------------------------------------*/
                Long subMvtTypeId = 2L;
                String movementDocumentNo = dbQualityLine.getQualityInspectionNo();
                String stBin = storageBin.getStorageBin();
                String movementQtyValue = "N";
                InventoryMovement inventoryMovement = createInventoryMovement(dbQualityLine, subMvtTypeId,
                        movementDocumentNo, stBin, movementQtyValue, loginUserID);
                log.info("InventoryMovement created : " + inventoryMovement);

                /*--------------------------------------------------------------------------*/
                // 2.Insert a new record in INVENTORY table as below
                // Fetch from QUALITYLINE table and insert WH_ID/ITM_CODE/ST_BIN= (ST_BIN value
                // of BIN_CLASS_ID=5
                // from STORAGEBIN table)/PACK_BARCODE/INV_QTY = QC_QTY - INVENTORY UPDATE 2
                BIN_CLASS_ID = 5L;
                storageBin = mastersService.getStorageBin(dbQualityLine.getWarehouseId(), BIN_CLASS_ID,
                        authTokenForMastersService.getAccess_token());
                warehouse = getWarehouse(dbQualityLine.getWarehouseId());

                /*
                 * Checking Inventory table before creating new record inventory
                 */
                // Pass WH_ID/ITM_CODE/ST_BIN = (ST_BIN value of BIN_CLASS_ID=5 /PACK_BARCODE
                Inventory existingInventory = inventoryService.getInventory(dbQualityLine.getWarehouseId(),
                        dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(), storageBin.getStorageBin());
                log.info("existingInventory : " + existingInventory);
                if (existingInventory != null) {
                    Double INV_QTY = existingInventory.getInventoryQuantity() + dbQualityLine.getQualityQty();
                    UpdateInventory updateInventory = new UpdateInventory();
                    updateInventory.setInventoryQuantity(INV_QTY);
                    try {
                        Inventory updatedInventory = inventoryService.updateInventory(dbQualityLine.getWarehouseId(),
                                dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(),
                                storageBin.getStorageBin(), 1L, 1L, updateInventory, loginUserID);
                        log.info("updatedInventory----------> : " + updatedInventory);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("updatedInventory error----------> : " + e.toString());
                    }
                } else {
                    log.info("AddInventory========>");
                    AddInventory newInventory = new AddInventory();
                    newInventory.setLanguageId(warehouse.getLanguageId());
                    newInventory.setCompanyCodeId(warehouse.getCompanyCode());
                    newInventory.setPlantId(warehouse.getPlantId());
                    newInventory.setStockTypeId(inventory.getStockTypeId());
                    newInventory.setBinClassId(BIN_CLASS_ID);
                    newInventory.setWarehouseId(dbQualityLine.getWarehouseId());
                    newInventory.setPackBarcodes(dbQualityLine.getPickPackBarCode());
                    newInventory.setItemCode(dbQualityLine.getItemCode());
                    newInventory.setStorageBin(storageBin.getStorageBin());
                    newInventory.setInventoryQuantity(dbQualityLine.getQualityQty());
                    newInventory.setSpecialStockIndicatorId(1L);

                    List<IImbasicData1> imbasicdata1 = imbasicdata1Repository
                            .findByItemCode(newInventory.getItemCode());
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
                    log.info("newInventory created : " + createdInventory);
                }

                /*-----------------------InventoryMovement----------------------------------*/
                // Inserting record in InventoryMovement
                subMvtTypeId = 2L;
                movementDocumentNo = DLV_ORD_NO;
                stBin = storageBin.getStorageBin();
                movementQtyValue = "P";
                inventoryMovement = createInventoryMovement(dbQualityLine, subMvtTypeId, movementDocumentNo, stBin,
                        movementQtyValue, loginUserID);
                log.info("InventoryMovement created for update2: " + inventoryMovement);
            }
            return createdQualityLineList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param dbQualityLine
     */
    private void createOutboundLineInterim(QualityLine dbQualityLine) {
//		OutboundLine dbOutboundLine = outboundLineService.getOutboundLine(dbQualityLine.getWarehouseId(),
//				dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
//				dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//				dbQualityLine.getItemCode());
//		log.info("##############dbOutboundLine QUERIED ----------->: " + dbOutboundLine);

        OutboundLineInterim outboundLineInterim = new OutboundLineInterim();
        BeanUtils.copyProperties(dbQualityLine, outboundLineInterim, CommonUtils.getNullPropertyNames(dbQualityLine));
        outboundLineInterim.setDeletionIndicator(0L);
        outboundLineInterim.setDeliveryQty(dbQualityLine.getQualityQty());
        outboundLineInterim.setCreatedBy(dbQualityLine.getQualityCreatedBy());
        outboundLineInterim.setCreatedOn(new Date());

        OutboundLineInterim createdOutboundLine = outboundLineInterimRepository.saveAndFlush(outboundLineInterim);
        log.info("outboundLineInterim created ----------->: " + createdOutboundLine);
    }

    /**
     *
     * @param dbQualityLine
     * @param DLV_ORD_NO
     */
//	@Transactional(isolation = Isolation.READ_COMMITTED)
//	private void updateOutboundLine (QualityLine dbQualityLine, String DLV_ORD_NO) {
//		try {
//			//---------------Update-Lock-Applied---------------------------------------------------------
//			OutboundLine outboundLine = outboundLineService.getOutboundLine(dbQualityLine.getWarehouseId(),
//					dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
//					dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//					dbQualityLine.getItemCode());
//			log.info("DB outboundLine : " + outboundLine);
//			if (outboundLine != null) {
//				Double exisitingDelQty = 0D;
//				if (outboundLine.getDeliveryQty() != null) {
//					exisitingDelQty = outboundLine.getDeliveryQty();
//				} else {
//					exisitingDelQty = 0D;
//				}
//				exisitingDelQty = exisitingDelQty + dbQualityLine.getQualityQty();
//				log.info("DB after outboundLine existingDelQty : " + exisitingDelQty);
//				outboundLineRepository.updateOutboundLine(dbQualityLine.getWarehouseId(),
//						dbQualityLine.getRefDocNumber(), dbQualityLine.getPreOutboundNo(),
//						dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//						dbQualityLine.getItemCode(), DLV_ORD_NO, 57L, exisitingDelQty);
//				log.info("outboundLine updated.");
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			log.info("outboundLine updated error: " + e1.toString());
//		}
//	}

    /**
     * @param dbQualityLine
     * @param DLV_ORD_NO
     */
    private void updateOutboundLine(QualityLine dbQualityLine, String DLV_ORD_NO) {
        try {
            Double deliveryQty = outboundLineInterimRepository.getSumOfDeliveryLine(dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                    dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
                    dbQualityLine.getItemCode());
            log.info("=======updateOutboundLine==========>: " + deliveryQty);

            // Get Existing Record
            OutboundLine existingOutboundLine = outboundLineService.getOutboundLine(dbQualityLine.getWarehouseId(),
                    dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
                    dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
                    dbQualityLine.getItemCode());

            // Insert
            OutboundLine outboundLine = new OutboundLine();
            BeanUtils.copyProperties(existingOutboundLine, outboundLine, CommonUtils.getNullPropertyNames(existingOutboundLine));
            outboundLine.setDeliveryQty(deliveryQty);
            outboundLine.setDeliveryOrderNo(DLV_ORD_NO);
            outboundLine.setStatusId(57L);
            outboundLine.setDeletionIndicator(0L);

            // Delete
            outboundLineRepository.delete(existingOutboundLine);
//			outboundLineRepository.deleteOutboundLineMain(dbQualityLine.getWarehouseId(),
//					dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
//					dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//					dbQualityLine.getItemCode());

            OutboundLine createdOutboundLineNewly = outboundLineRepository.save(outboundLine);
            log.info("createdOutboundLineNewly created ----------->: " + createdOutboundLineNewly);
        } catch (Exception e1) {
            e1.printStackTrace();
            log.info("outboundLine updated error: " + e1.toString());
        }
    }

    /**
     * @param dbQualityLine
     * @param subMvtTypeId
     * @param movementDocumentNo
     * @param storageBin
     * @param movementQtyValue
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private InventoryMovement createInventoryMovement(QualityLine dbQualityLine, Long subMvtTypeId,
                                                      String movementDocumentNo, String storageBin, String movementQtyValue, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        AddInventoryMovement inventoryMovement = new AddInventoryMovement();
        BeanUtils.copyProperties(dbQualityLine, inventoryMovement, CommonUtils.getNullPropertyNames(dbQualityLine));

        // MVT_TYP_ID
        inventoryMovement.setMovementType(3L);

        // SUB_MVT_TYP_ID
        inventoryMovement.setSubmovementType(subMvtTypeId);

        // VAR_ID
        inventoryMovement.setVariantCode(1L);

        // VAR_SUB_ID
        inventoryMovement.setVariantSubCode("1");

        // STR_MTD
        inventoryMovement.setStorageMethod("1");

        // STR_NO
        inventoryMovement.setBatchSerialNumber("1");

        // MVT_DOC_NO
        inventoryMovement.setMovementDocumentNo(movementDocumentNo);

        // ST_BIN
        inventoryMovement.setStorageBin(storageBin);

        // MVT_QTY_VAL
        inventoryMovement.setMovementQtyValue(movementQtyValue);

        // PACK_BAR_CODE
        inventoryMovement.setPackBarcodes(dbQualityLine.getPickPackBarCode());

        // MVT_QTY
        inventoryMovement.setMovementQty(dbQualityLine.getPickConfirmQty());

        // MVT_UOM
        inventoryMovement.setInventoryUom(dbQualityLine.getQualityConfirmUom());

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(dbQualityLine.getQualityConfirmedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(dbQualityLine.getQualityCreatedOn());

        InventoryMovement createdInventoryMovement = inventoryMovementService.createInventoryMovement(inventoryMovement,
                loginUserID);
        return createdInventoryMovement;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updateQualityLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLine> updateQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, Long lineNumber, String itemCode, String loginUserID,
                                               UpdateQualityLine updateQualityLine) throws IllegalAccessException, InvocationTargetException {
        List<QualityLine> dbQualityLine = getQualityLineForUpdateForDeliverConformation(warehouseId, preOutboundNo,
                refDocNumber, partnerCode, lineNumber, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.forEach(data -> {
                BeanUtils.copyProperties(updateQualityLine, data, CommonUtils.getNullPropertyNames(updateQualityLine));
                data.setQualityUpdatedBy(loginUserID);
                data.setQualityUpdatedOn(new Date());
            });
            return qualityLineRepository.saveAll(dbQualityLine);
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @param loginUserID
     * @param updateQualityLine
     * @return
     */
    public QualityLine updateQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                         String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID,
                                         @Valid UpdateQualityLine updateQualityLine) {
        QualityLine dbQualityLine = getQualityLineForUpdate(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                lineNumber, qualityInspectionNo, itemCode);
        if (dbQualityLine != null) {
            BeanUtils.copyProperties(updateQualityLine, dbQualityLine,
                    CommonUtils.getNullPropertyNames(updateQualityLine));
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineRepository.save(dbQualityLine);
        }
        return null;
    }

    /**
     * deleteQualityLine
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public QualityLine deleteQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                         String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        QualityLine dbQualityLine = getQualityLine(warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber,
                itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineRepository.save(dbQualityLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLine> deleteQualityLineForReversal(String warehouseId, String preOutboundNo, String refDocNumber,
                                                          String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<QualityLine> dbQualityLine = getQualityLineForReversal(warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode);

        if (dbQualityLine != null && !dbQualityLine.isEmpty()) {
            List<QualityLine> qualityLineList = new ArrayList<>();
            dbQualityLine.forEach(data -> {
                data.setDeletionIndicator(1L);
                data.setQualityUpdatedBy(loginUserID);
                data.setQualityUpdatedOn(new Date());
                qualityLineList.add(data);
            });
            return qualityLineRepository.saveAll(qualityLineList);
        } else {
            return null;
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<OutboundLineInterim> deleteOutboundLineInterimForReversal(String warehouseId, String preOutboundNo, String refDocNumber,
                                                                          String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<OutboundLineInterim> listOutboundLineInterim = outboundLineInterimRepository.
                findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(warehouseId,
                        preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (listOutboundLineInterim != null && !listOutboundLineInterim.isEmpty()) {
            listOutboundLineInterim.forEach(data -> {
                data.setDeletionIndicator(1L);
                data.setUpdatedBy(loginUserID);
                data.setUpdatedOn(new Date());
            });
            return outboundLineInterimRepository.saveAll(listOutboundLineInterim);
        }
        return listOutboundLineInterim;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public QualityLine deleteQualityLineValidated(String warehouseId, String preOutboundNo, String refDocNumber,
                                                  String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        QualityLine dbQualityLine = getQualityLineValidated(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                lineNumber, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineRepository.save(dbQualityLine);
        } else {
            return null;
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @param loginUserID
     * @return
     */
    public QualityLine deleteQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                         String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID) {
        QualityLine dbQualityLine = getQualityLineForUpdate(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                lineNumber, qualityInspectionNo, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineRepository.save(dbQualityLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
        }
    }

    //====================================================================V2===============================================================================

    /**
     * getQualityLines
     *
     * @return
     */
    public List<QualityLineV2> getQualityLinesV2() {
        List<QualityLineV2> qualityLineList = qualityLineV2Repository.findAll();
        qualityLineList = qualityLineList.stream().filter(n -> n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return qualityLineList;
    }

    /**
     * getQualityLine
     *
     * @return
     */
    public QualityLineV2 getQualityLineV2(String companyCodeId, String plantId, String languageId, String partnerCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndPartnerCode(
                companyCodeId, plantId, languageId, partnerCode);
        if (qualityLine != null && qualityLine.getDeletionIndicator() == 0) {
            return qualityLine;
        } else {
            throw new BadRequestException("The given QualityLine ID : " + partnerCode + " doesn't exist.");
        }
    }

    /**
     * // Fetch WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE/OB_LINE_NO/ITM_CODE
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLineV2 getQualityLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                          String preOutboundNo, String refDocNumber, String partnerCode,
                                          Long lineNumber, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        throw new BadRequestException("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:"
                + preOutboundNo + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:"
                + lineNumber + ",itemCode:" + itemCode + " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLineV2 getQualityLineValidatedV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                   String preOutboundNo, String refDocNumber,
                                                   String partnerCode, Long lineNumber, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<QualityLineV2> getQualityLineForReversalV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                           String preOutboundNo, String refDocNumber,
                                                           String partnerCode, Long lineNumber, String itemCode) {
        List<QualityLineV2> qualityLine = qualityLineV2Repository
                .findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public QualityLineV2 getQualityLineForUpdateV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                   String preOutboundNo, String refDocNumber,
                                                   String partnerCode, Long lineNumber, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        log.info("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                + ",itemCode:" + itemCode + " doesn't exist.");
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<QualityLineV2> getQualityLineForUpdateForDeliverConformationV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                               String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber, String itemCode) {
        List<QualityLineV2> qualityLine = qualityLineV2Repository
                .findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        log.info("The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                + ",itemCode:" + itemCode + " doesn't exist.");
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumbers
     * @param itemCodes
     * @return
     */
    public List<QualityLineV2> getQualityLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                String preOutboundNo, String refDocNumber, String partnerCode, List<Long> lineNumbers, List<String> itemCodes) {
        List<QualityLineV2> qualityLine = qualityLineV2Repository
                .findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberInAndItemCodeInAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumbers, itemCodes, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLineV2 getQualityLineForUpdateV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                    String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber,
                                                    String qualityInspectionNo, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        throw new BadRequestException(
                "The given QualityLine ID : " + "warehouseId:" + warehouseId + ",preOutboundNo:" + preOutboundNo
                        + ",refDocNumber:" + refDocNumber + ",partnerCode:" + partnerCode + ",lineNumber:" + lineNumber
                        + ",qualityInspectionNo:" + qualityInspectionNo + ",itemCode:" + itemCode + " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLineV2 findDuplicateRecordV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber,
                                                String qualityInspectionNo, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @return
     */
    private QualityLineV2 findQualityLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                            String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber,
                                            String qualityInspectionNo, String itemCode) {
        QualityLineV2 qualityLine = qualityLineV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndQualityInspectionNoAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo,
                        itemCode, 0L);
        if (qualityLine != null) {
            return qualityLine;
        }
        return null;
    }

    /**
     * @param searchQualityLine
     * @return
     * @throws ParseException
     */
    public Stream<QualityLineV2> findQualityLineV2(SearchQualityLineV2 searchQualityLine) throws ParseException {
        QualityLineV2Specification spec = new QualityLineV2Specification(searchQualityLine);
        Stream<QualityLineV2> results = qualityLineV2Repository.stream(spec, QualityLineV2.class);
        return results;
    }

    /**
     * @param newQualityLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLineV2> createQualityLineV2(List<AddQualityLine> newQualityLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        try {
            log.info("-------createQualityLine--------called-------> " + new Date());

            List<AddQualityLine> dupQualityLines = getDuplicatesV2(newQualityLines);
            log.info("-------dupQualityLines--------> " + dupQualityLines);
            if (dupQualityLines != null && !dupQualityLines.isEmpty()) {
                newQualityLines.removeAll(dupQualityLines);
                newQualityLines.add(dupQualityLines.get(0));
                log.info("-------newQualityLines---removed-dupQualityLines-----> " + newQualityLines);
            }

            /*
             * The below flag helps to avoid duplicate request and updating of outboundline
             * table
             */
            List<QualityLineV2> createdQualityLineList = new ArrayList<>();
            for (AddQualityLine newQualityLine : newQualityLines) {
                log.info("Input from UI:  " + newQualityLine);

                QualityLineV2 dbQualityLine = new QualityLineV2();
                BeanUtils.copyProperties(newQualityLine, dbQualityLine, CommonUtils.getNullPropertyNames(newQualityLine));

                // STATUS_ID - HardCoded Value "55"
                dbQualityLine.setStatusId(55L);

                IKeyValuePair description = stagingLineV2Repository.getDescription(dbQualityLine.getCompanyCodeId(),
                        dbQualityLine.getLanguageId(),
                        dbQualityLine.getPlantId(),
                        dbQualityLine.getWarehouseId());

                statusDescription = stagingLineV2Repository.getStatusDescription(55L, dbQualityLine.getLanguageId());
                dbQualityLine.setStatusDescription(statusDescription);

                dbQualityLine.setCompanyDescription(description.getCompanyDesc());
                dbQualityLine.setPlantDescription(description.getPlantDesc());
                dbQualityLine.setWarehouseDescription(description.getWarehouseDesc());

                OrderManagementLineV2 dbOrderManagementLine = orderManagementLineService.getOrderManagementLineForQualityLineV2(
                        newQualityLine.getCompanyCodeId(),
                        newQualityLine.getPlantId(),
                        newQualityLine.getLanguageId(),
                        newQualityLine.getWarehouseId(),
                        newQualityLine.getPreOutboundNo(),
                        newQualityLine.getRefDocNumber(),
                        newQualityLine.getLineNumber(),
                        newQualityLine.getItemCode());
                log.info("OrderManagementLine: " + dbOrderManagementLine);

                if(dbOrderManagementLine != null) {
                    dbQualityLine.setManufacturerName(dbOrderManagementLine.getManufacturerName());
                    dbQualityLine.setManufacturerFullName(dbOrderManagementLine.getManufacturerFullName());
                    dbQualityLine.setMiddlewareId(dbOrderManagementLine.getMiddlewareId());
                    dbQualityLine.setMiddlewareHeaderId(dbOrderManagementLine.getMiddlewareHeaderId());
                    dbQualityLine.setMiddlewareTable(dbOrderManagementLine.getMiddlewareTable());
                    dbQualityLine.setReferenceDocumentType(dbOrderManagementLine.getReferenceDocumentType());
                }

                dbQualityLine.setDeletionIndicator(0L);
                dbQualityLine.setQualityCreatedBy(loginUserID);
                dbQualityLine.setQualityUpdatedBy(loginUserID);
                dbQualityLine.setQualityCreatedOn(new Date());
                dbQualityLine.setQualityUpdatedOn(new Date());

                /*
                 * String warehouseId, String preOutboundNo, String refDocNumber, String
                 * partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode
                 */
                QualityLineV2 existingQualityLine = findDuplicateRecordV2(
                        newQualityLine.getCompanyCodeId(),
                        newQualityLine.getPlantId(),
                        newQualityLine.getLanguageId(),
                        newQualityLine.getWarehouseId(),
                        newQualityLine.getPreOutboundNo(), newQualityLine.getRefDocNumber(),
                        newQualityLine.getPartnerCode(), newQualityLine.getLineNumber(),
                        newQualityLine.getQualityInspectionNo(), newQualityLine.getItemCode());
                log.info("existingQualityLine record status : " + existingQualityLine);

                /*
                 * Checking whether the record already exists (created) or not. If it is not
                 * created then only the rest of the logic has been carry forward
                 */
                if (existingQualityLine == null) {
                    QualityLineV2 createdQualityLine = qualityLineV2Repository.save(dbQualityLine);
                    log.info("createdQualityLine: " + createdQualityLine);

                    // createOutboundLineInterim
                    createOutboundLineInterimV2(createdQualityLine);
                    createdQualityLineList.add(createdQualityLine);
                }
            }

            /*
             * Based on created QualityLine List, updating respective tables
             */
//            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            for (QualityLineV2 dbQualityLine : createdQualityLineList) {
                /*-----------------STATUS updates in QualityHeader-----------------------*/
                try {
                    QualityHeaderV2 updateQualityHeader = new QualityHeaderV2();
                    updateQualityHeader.setStatusId(55L);
//                    StatusId idStatus = idmasterService.getStatus(55L, dbQualityLine.getWarehouseId(), authTokenForIDService.getAccess_token());
                    statusDescription = stagingLineV2Repository.getStatusDescription(55L, dbQualityLine.getLanguageId());
                    updateQualityHeader.setReferenceField10(statusDescription);
                    updateQualityHeader.setStatusDescription(statusDescription);
                    QualityHeaderV2 qualityHeader = qualityHeaderService.updateQualityHeaderV2(
                            dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                            dbQualityLine.getLanguageId(),
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getQualityInspectionNo(),
                            dbQualityLine.getActualHeNo(), loginUserID, updateQualityHeader);
                    log.info("qualityHeader updated : " + qualityHeader);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log.info("qualityHeader updated Error : " + e1.toString());
                }

                /*-------------------OUTBOUNDLINE------Update---------------------------*/
                /*
                 * Pass WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE /OB_LINE_NO/_ITM_CODE values in
                 * QUALITYILINE table and fetch QC_QTY values and pass the same values in
                 * OUTBOUNDLINE table and update DLV_QTY
                 *
                 * Pass Unique keys in OUTBOUNDLINE table and update STATUS_ID as "57"
                 */
                Long NUM_RAN_CODE = 12L;
                String DLV_ORD_NO = getNextRangeNumber(NUM_RAN_CODE, dbQualityLine.getCompanyCodeId(),
                        dbQualityLine.getPlantId(), dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId());

                updateOutboundLineV2(dbQualityLine, DLV_ORD_NO);
                try {
                    /*-------------------OUTBOUNDHEADER------Update---------------------------*/
                    boolean isStatus57 = false;
                    List<OutboundLineV2> outboundLines = outboundLineService.getOutboundLineV2(
                            dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(), dbQualityLine.getLanguageId(),
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode());
//					log.info("outboundLine re-queried-----> : " + outboundLines);

                    outboundLines = outboundLines.stream().filter(o -> o.getStatusId() == 57L)
                            .collect(Collectors.toList());
                    if (outboundLines != null) {
                        isStatus57 = true;
                    }

                    OutboundHeaderV2 updateOutboundHeader = new OutboundHeaderV2();
                    updateOutboundHeader.setDeliveryOrderNo(DLV_ORD_NO);
                    if (isStatus57) { // If Status if 57 then update OutboundHeader with Status 57.
                        updateOutboundHeader.setStatusId(57L);
                    }

                    OutboundHeaderV2 outboundHeader = outboundHeaderService.updateOutboundHeaderV2(
                            dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(), dbQualityLine.getLanguageId(),
                            dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                            dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode(), updateOutboundHeader,
                            loginUserID);
                    log.info("outboundHeader updated : " + outboundHeader);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    log.info("outboundHeader updated error: " + e1.toString());
                }

                /*-----------------Inventory Updates--------------------------------------*/
                // Pass WH_ID/ITM_CODE/ST_BIN/PACK_BARCODE in INVENTORY table
                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
                Long BIN_CLASS_ID = 4L;
                StorageBinV2 storageBin = mastersService.getStorageBin(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                        dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(), BIN_CLASS_ID,
                        authTokenForMastersService.getAccess_token());
//                Warehouse warehouse = getWarehouse(dbQualityLine.getWarehouseId());
                InventoryV2 inventory = null;
                try {
                    inventory = inventoryService.getInventoryV2(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                            dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                            dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(),
                            storageBin.getStorageBin());
                    log.info("inventory---BIN_CLASS_ID-4----> : " + inventory);

                    if (inventory != null) {
                        Double INV_QTY = inventory.getInventoryQuantity() - dbQualityLine.getQualityQty();
                        log.info("Calculated inventory INV_QTY: " + INV_QTY);
                        inventory.setInventoryQuantity(INV_QTY);

                        // INV_QTY > 0 then, update Inventory Table
                        inventory = inventoryV2Repository.save(inventory);
                        log.info("inventory updated : " + inventory);

                        if (INV_QTY == 0) {
                            log.info("inventory INV_QTY: " + INV_QTY);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                /*-------------------Inserting record in InventoryMovement-------------------------------------*/
                Long subMvtTypeId = 2L;
                String movementDocumentNo = dbQualityLine.getQualityInspectionNo();
                String stBin = storageBin.getStorageBin();
                String movementQtyValue = "N";
                InventoryMovement inventoryMovement = createInventoryMovementV2(dbQualityLine, subMvtTypeId,
                        movementDocumentNo, stBin, movementQtyValue, loginUserID);
                log.info("InventoryMovement created : " + inventoryMovement);

                /*--------------------------------------------------------------------------*/
                // 2.Insert a new record in INVENTORY table as below
                // Fetch from QUALITYLINE table and insert WH_ID/ITM_CODE/ST_BIN= (ST_BIN value
                // of BIN_CLASS_ID=5
                // from STORAGEBIN table)/PACK_BARCODE/INV_QTY = QC_QTY - INVENTORY UPDATE 2
                BIN_CLASS_ID = 5L;
                storageBin = mastersService.getStorageBin(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                        dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(), BIN_CLASS_ID,
                        authTokenForMastersService.getAccess_token());
//                warehouse = getWarehouse(dbQualityLine.getWarehouseId());

                /*
                 * Checking Inventory table before creating new record inventory
                 */
                // Pass WH_ID/ITM_CODE/ST_BIN = (ST_BIN value of BIN_CLASS_ID=5 /PACK_BARCODE
                InventoryV2 existingInventory = inventoryService.getInventoryV2(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                        dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                        dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(), storageBin.getStorageBin());
                log.info("existingInventory : " + existingInventory);
                if (existingInventory != null) {
                    Double INV_QTY = existingInventory.getInventoryQuantity() + dbQualityLine.getQualityQty();
                    InventoryV2 updateInventory = new InventoryV2();
                    updateInventory.setInventoryQuantity(INV_QTY);
                    try {
                        InventoryV2 updatedInventory = inventoryService.updateInventoryV2(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                                dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                                dbQualityLine.getPickPackBarCode(), dbQualityLine.getItemCode(),
                                storageBin.getStorageBin(), 1L, 1L, updateInventory, loginUserID);
                        log.info("updatedInventory----------> : " + updatedInventory);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("updatedInventory error----------> : " + e.toString());
                    }
                } else {
                    log.info("AddInventory========>");
                    InventoryV2 newInventory = new InventoryV2();
                    newInventory.setLanguageId(dbQualityLine.getLanguageId());
                    newInventory.setCompanyCodeId(dbQualityLine.getCompanyCodeId());
                    newInventory.setPlantId(dbQualityLine.getPlantId());
                    if (inventory != null) {
                        newInventory.setStockTypeId(inventory.getStockTypeId());
                    }
                    newInventory.setBinClassId(BIN_CLASS_ID);
                    newInventory.setWarehouseId(dbQualityLine.getWarehouseId());
                    newInventory.setPackBarcodes(dbQualityLine.getPickPackBarCode());
                    newInventory.setItemCode(dbQualityLine.getItemCode());
                    newInventory.setStorageBin(storageBin.getStorageBin());
                    newInventory.setInventoryQuantity(dbQualityLine.getQualityQty());
                    newInventory.setSpecialStockIndicatorId(1L);
                    newInventory.setManufacturerName(dbQualityLine.getManufacturerName());

                    if (dbQualityLine.getManufacturerPartNo() != null) {
                        newInventory.setManufacturerCode(dbQualityLine.getManufacturerPartNo());
                        log.info("QL Mfr PartNo: " + dbQualityLine.getManufacturerPartNo());
                    }
                    if (dbQualityLine.getManufacturerName() != null) {
                        newInventory.setManufacturerCode(dbQualityLine.getManufacturerName());
                        log.info("QL Mfr Name: " + dbQualityLine.getManufacturerName());
                    }

                    if (dbQualityLine.getManufacturerName() == null) {
                        OrderManagementLineV2 orderManagementLine = orderManagementLineService.getOrderManagementLineForQualityLineV2(
                                dbQualityLine.getCompanyCodeId(),
                                dbQualityLine.getPlantId(),
                                dbQualityLine.getLanguageId(),
                                dbQualityLine.getWarehouseId(),
                                dbQualityLine.getPreOutboundNo(),
                                dbQualityLine.getRefDocNumber(),
                                dbQualityLine.getLineNumber(),
                                dbQualityLine.getItemCode());
                        log.info("OrderManagementLine: " + orderManagementLine);
                        if (orderManagementLine != null) {
                            newInventory.setManufacturerName(orderManagementLine.getManufacturerName());
                            newInventory.setManufacturerCode(orderManagementLine.getManufacturerName());
                        }
                    }
                    ImBasicData1 imbasicdata1 = mastersService.getImBasicData1ByItemCodeV2(newInventory.getItemCode(),
                            newInventory.getLanguageId(), newInventory.getCompanyCodeId(),
                            newInventory.getPlantId(), newInventory.getWarehouseId(),
                            newInventory.getManufacturerName(), authTokenForMastersService.getAccess_token());
                    log.info("ImbasicData1: " + imbasicdata1);

//                    List<IImbasicData1> imbasicdata1 = imbasicdata1Repository
//                            .findByItemCode(newInventory.getItemCode());

                    if (imbasicdata1 != null) {
                        newInventory.setManufacturerCode(imbasicdata1.getManufacturerPartNo());
                        newInventory.setManufacturerName(imbasicdata1.getManufacturerPartNo());
                        log.info("ImbasicData1 Mfr PartNo: " + imbasicdata1.getManufacturerPartNo());
                        newInventory.setReferenceField8(imbasicdata1.getDescription());
                        newInventory.setReferenceField9(imbasicdata1.getManufacturerPartNo());
                    }
                    if (storageBin != null) {
                        newInventory.setReferenceField10(storageBin.getStorageSectionId());
                        newInventory.setReferenceField5(storageBin.getAisleNumber());
                        newInventory.setReferenceField6(storageBin.getShelfId());
                        newInventory.setReferenceField7(storageBin.getRowId());
                        newInventory.setLevelId(String.valueOf(storageBin.getFloorId()));
                    }

                    InventoryV2 createdInventory = inventoryService.createInventoryV2(newInventory, loginUserID);
                    log.info("newInventory created : " + createdInventory);
                }

                /*-----------------------InventoryMovement----------------------------------*/
                // Inserting record in InventoryMovement
                subMvtTypeId = 2L;
                movementDocumentNo = DLV_ORD_NO;
                stBin = storageBin.getStorageBin();
                movementQtyValue = "P";
                inventoryMovement = createInventoryMovementV2(dbQualityLine, subMvtTypeId, movementDocumentNo, stBin,
                        movementQtyValue, loginUserID);
                log.info("InventoryMovement created for update2: " + inventoryMovement);
            }
            return createdQualityLineList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param dbQualityLine
     */
    private void createOutboundLineInterimV2(QualityLineV2 dbQualityLine) {
//		OutboundLine dbOutboundLine = outboundLineService.getOutboundLine(dbQualityLine.getWarehouseId(),
//				dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
//				dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//				dbQualityLine.getItemCode());
//		log.info("##############dbOutboundLine QUERIED ----------->: " + dbOutboundLine);

        OutboundLineInterim outboundLineInterim = new OutboundLineInterim();
        BeanUtils.copyProperties(dbQualityLine, outboundLineInterim, CommonUtils.getNullPropertyNames(dbQualityLine));
        outboundLineInterim.setDeletionIndicator(0L);
        outboundLineInterim.setDeliveryQty(dbQualityLine.getQualityQty());
        outboundLineInterim.setCreatedBy(dbQualityLine.getQualityCreatedBy());
        outboundLineInterim.setCreatedOn(new Date());

        OutboundLineInterim createdOutboundLine = outboundLineInterimRepository.saveAndFlush(outboundLineInterim);
        log.info("outboundLineInterim created ----------->: " + createdOutboundLine);
    }


    /**
     * @param dbQualityLine
     * @param DLV_ORD_NO
     */
    private void updateOutboundLineV2(QualityLineV2 dbQualityLine, String DLV_ORD_NO) {
        try {
            Double deliveryQty = outboundLineInterimRepository.getSumOfDeliveryLine(dbQualityLine.getWarehouseId(), dbQualityLine.getPreOutboundNo(),
                    dbQualityLine.getRefDocNumber(), dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
                    dbQualityLine.getItemCode());
            log.info("=======updateOutboundLine==========>: " + deliveryQty);

            // Get Existing Record
            OutboundLineV2 existingOutboundLine = outboundLineService.getOutboundLineV2(dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                    dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                    dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
                    dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
                    dbQualityLine.getItemCode());

            // Insert
            OutboundLineV2 outboundLine = new OutboundLineV2();
            BeanUtils.copyProperties(existingOutboundLine, outboundLine, CommonUtils.getNullPropertyNames(existingOutboundLine));
            outboundLine.setDeliveryQty(deliveryQty);
            outboundLine.setDeliveryOrderNo(DLV_ORD_NO);
            outboundLine.setStatusId(57L);
            outboundLine.setDeletionIndicator(0L);

            // Delete
            outboundLineV2Repository.delete(existingOutboundLine);
//			outboundLineRepository.deleteOutboundLineMain(dbQualityLine.getWarehouseId(),
//					dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
//					dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
//					dbQualityLine.getItemCode());

            OutboundLineV2 createdOutboundLineNewly = outboundLineV2Repository.save(outboundLine);
            log.info("createdOutboundLineNewly created ----------->: " + createdOutboundLineNewly);

//            List<Long> statusId = null;
            Long[] statusId = new Long[]{42L, 43L};
            SearchOrderManagementLineV2 searchOrderManagementLine = new SearchOrderManagementLineV2();
//            statusId.set(0, 42L);
//            statusId.set(1, 43L);
            searchOrderManagementLine.setStatusId(List.of(statusId));
            Stream<OrderManagementLineV2> orderManagementLineList = orderManagementLineService.findOrderManagementLineV2(searchOrderManagementLine);

//            statusId.clear();
            SearchPickupHeaderV2 searchPickupHeader = new SearchPickupHeaderV2();
            statusId = new Long[]{48L};
//            statusId.set(0, 48L);
            searchPickupHeader.setStatusId(List.of(statusId));
            Stream<PickupHeaderV2> pickupHeaderList = pickupHeaderService.findPickupHeaderV2(searchPickupHeader);

//            statusId.clear();
            SearchQualityHeaderV2 searchQualityHeader = new SearchQualityHeaderV2();
            statusId = new Long[]{54L};
//            statusId.set(0, 54L);
            searchQualityHeader.setStatusId(List.of(statusId));
            Stream<QualityHeaderV2> qualityHeaderList = qualityHeaderService.findQualityHeaderNewV2(searchQualityHeader);

            if (orderManagementLineList == null && pickupHeaderList == null && qualityHeaderList == null) {
                OutboundLineV2 updateOutboundLineStatus = new OutboundLineV2();
                updateOutboundLineStatus.setStatusId(57L);
                OutboundLineV2 updatedOutboundLineV2 = outboundLineService.updateOutboundLineV2(
                        dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                        dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                        dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
                        dbQualityLine.getPartnerCode(), dbQualityLine.getLineNumber(),
                        dbQualityLine.getItemCode(), dbQualityLine.getQualityCreatedBy(),
                        updateOutboundLineStatus);
                if (updatedOutboundLineV2 != null) {
                    List<OutboundLineV2> deliveryConfirm = outboundLineService.deliveryConfirmationV2(
                            dbQualityLine.getCompanyCodeId(), dbQualityLine.getPlantId(),
                            dbQualityLine.getLanguageId(), dbQualityLine.getWarehouseId(),
                            dbQualityLine.getPreOutboundNo(), dbQualityLine.getRefDocNumber(),
                            dbQualityLine.getPartnerCode(), dbQualityLine.getQualityConfirmedBy());
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            log.info("outboundLine updated error: " + e1.toString());
        }
    }

    /**
     * @param dbQualityLine
     * @param subMvtTypeId
     * @param movementDocumentNo
     * @param storageBin
     * @param movementQtyValue
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private InventoryMovement createInventoryMovementV2(QualityLine dbQualityLine, Long subMvtTypeId,
                                                        String movementDocumentNo, String storageBin, String movementQtyValue, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        AddInventoryMovement inventoryMovement = new AddInventoryMovement();
        BeanUtils.copyProperties(dbQualityLine, inventoryMovement, CommonUtils.getNullPropertyNames(dbQualityLine));

        // MVT_TYP_ID
        inventoryMovement.setMovementType(3L);

        // SUB_MVT_TYP_ID
        inventoryMovement.setSubmovementType(subMvtTypeId);

        // VAR_ID
        inventoryMovement.setVariantCode(1L);

        // VAR_SUB_ID
        inventoryMovement.setVariantSubCode("1");

        // STR_MTD
        inventoryMovement.setStorageMethod("1");

        // STR_NO
        inventoryMovement.setBatchSerialNumber("1");

        // MVT_DOC_NO
        inventoryMovement.setMovementDocumentNo(movementDocumentNo);

        // ST_BIN
        inventoryMovement.setStorageBin(storageBin);

        // MVT_QTY_VAL
        inventoryMovement.setMovementQtyValue(movementQtyValue);

        // PACK_BAR_CODE
        inventoryMovement.setPackBarcodes(dbQualityLine.getPickPackBarCode());

        // MVT_QTY
        inventoryMovement.setMovementQty(dbQualityLine.getPickConfirmQty());

        // MVT_UOM
        inventoryMovement.setInventoryUom(dbQualityLine.getQualityConfirmUom());

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(dbQualityLine.getQualityConfirmedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(dbQualityLine.getQualityCreatedOn());

        InventoryMovement createdInventoryMovement = inventoryMovementService.createInventoryMovement(inventoryMovement,
                loginUserID);
        return createdInventoryMovement;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updateQualityLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLineV2> updateQualityLineV2(String companyCodeId, String plantId, String languageId,
                                                   String warehouseId, String preOutboundNo, String refDocNumber,
                                                   String partnerCode, Long lineNumber, String itemCode, String loginUserID,
                                                   QualityLineV2 updateQualityLine) throws IllegalAccessException, InvocationTargetException {
        List<QualityLineV2> dbQualityLine = getQualityLineForUpdateForDeliverConformationV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo,
                refDocNumber, partnerCode, lineNumber, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.forEach(data -> {
                BeanUtils.copyProperties(updateQualityLine, data, CommonUtils.getNullPropertyNames(updateQualityLine));
                data.setQualityUpdatedBy(loginUserID);
                data.setQualityUpdatedOn(new Date());
            });
            return qualityLineV2Repository.saveAll(dbQualityLine);
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @param loginUserID
     * @param updateQualityLine
     * @return
     */
    public QualityLineV2 updateQualityLineV2(String companyCodeId, String plantId, String languageId,
                                             String warehouseId, String preOutboundNo, String refDocNumber,
                                             String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID,
                                             @Valid QualityLineV2 updateQualityLine) {
        QualityLineV2 dbQualityLine = getQualityLineForUpdateV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                lineNumber, qualityInspectionNo, itemCode);
        if (dbQualityLine != null) {
            BeanUtils.copyProperties(updateQualityLine, dbQualityLine,
                    CommonUtils.getNullPropertyNames(updateQualityLine));
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineV2Repository.save(dbQualityLine);
        }
        return null;
    }

    /**
     * deleteQualityLine
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public QualityLineV2 deleteQualityLineV2(String companyCodeId, String plantId, String languageId,
                                             String warehouseId, String preOutboundNo, String refDocNumber,
                                             String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        QualityLineV2 dbQualityLine = getQualityLineV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber,
                itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineV2Repository.save(dbQualityLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<QualityLineV2> deleteQualityLineForReversalV2(String companyCodeId, String plantId, String languageId,
                                                              String warehouseId, String preOutboundNo, String refDocNumber,
                                                              String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<QualityLineV2> dbQualityLine = getQualityLineForReversalV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode);

        if (dbQualityLine != null && !dbQualityLine.isEmpty()) {
            List<QualityLineV2> qualityLineList = new ArrayList<>();
            dbQualityLine.forEach(data -> {
                data.setDeletionIndicator(1L);
                data.setQualityUpdatedBy(loginUserID);
                data.setQualityUpdatedOn(new Date());
                qualityLineList.add(data);
            });
            return qualityLineV2Repository.saveAll(qualityLineList);
        } else {
            return null;
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<OutboundLineInterim> deleteOutboundLineInterimForReversalV2(String warehouseId, String preOutboundNo, String refDocNumber,
                                                                            String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<OutboundLineInterim> listOutboundLineInterim = outboundLineInterimRepository.
                findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(warehouseId,
                        preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode, 0L);
        if (listOutboundLineInterim != null && !listOutboundLineInterim.isEmpty()) {
            listOutboundLineInterim.forEach(data -> {
                data.setDeletionIndicator(1L);
                data.setUpdatedBy(loginUserID);
                data.setUpdatedOn(new Date());
            });
            return outboundLineInterimRepository.saveAll(listOutboundLineInterim);
        }
        return listOutboundLineInterim;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public QualityLineV2 deleteQualityLineValidatedV2(String companyCodeId, String plantId, String languageId,
                                                      String warehouseId, String preOutboundNo, String refDocNumber,
                                                      String partnerCode, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        QualityLineV2 dbQualityLine = getQualityLineValidatedV2(companyCodeId, plantId, languageId, warehouseId,
                preOutboundNo, refDocNumber, partnerCode, lineNumber, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            return qualityLineV2Repository.save(dbQualityLine);
        } else {
            return null;
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param lineNumber
     * @param qualityInspectionNo
     * @param itemCode
     * @param loginUserID
     * @return
     */
    public void deleteQualityLineV2(String companyCodeId, String plantId, String languageId,
                                    String warehouseId, String preOutboundNo, String refDocNumber,
                                    String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID) {
        QualityLineV2 dbQualityLine = getQualityLineForUpdateV2(companyCodeId, plantId, languageId, warehouseId,
                preOutboundNo, refDocNumber, partnerCode, lineNumber, qualityInspectionNo, itemCode);
        if (dbQualityLine != null) {
            dbQualityLine.setDeletionIndicator(1L);
            dbQualityLine.setQualityUpdatedBy(loginUserID);
            dbQualityLine.setQualityUpdatedOn(new Date());
            qualityLineV2Repository.save(dbQualityLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
        }
    }
}
