package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.tekclover.wms.api.transaction.model.inbound.gr.v2.SearchGrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayHeader;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.IImbasicData1;
import com.tekclover.wms.api.transaction.model.dto.StatusId;
import com.tekclover.wms.api.transaction.model.dto.StorageBin;
import com.tekclover.wms.api.transaction.model.dto.StorageBinV2;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.PackBarcode;
import com.tekclover.wms.api.transaction.model.inbound.gr.SearchGrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.StorageBinPutAway;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.AddGrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
//import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.repository.specification.GrLineV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GrLineV2Service extends BaseService {

    @Autowired
    private GrHeaderRepository grHeaderRepository;

    @Autowired
    private PutAwayHeaderRepository putAwayHeaderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private InboundLineRepository inboundLineRepository;

    @Autowired
    private StagingLineRepository stagingLineRepository;

    @Autowired
    private GrHeaderService grHeaderService;

    @Autowired
    private InboundLineService inboundLineService;

    @Autowired
    private StagingLineService stagingLineService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private MastersService mastersService;

    @Autowired
    private ImBasicData1Repository imbasicdata1Repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InboundLineV2Repository inboundLineV2Repository;

    @Autowired
    private GrHeaderV2Repository grHeaderV2Repository;

    @Autowired
    private GrLineV2Repository grLineV2Repository;

    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    private StagingLineV2Service stagingLineV2Service;

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    @Autowired
    private PutAwayLineV2Repository putAwayLineV2Repository;

    String statusDescription = null;

    /**
     * getGrLines
     *
     * @return
     */
    public List<GrLineV2> getGrLines() {
        List<GrLineV2> grLineList = grLineV2Repository.findAll();
        grLineList = grLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return grLineList;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param goodsReceiptNo
     * @param palletCode
     * @param caseCode
     * @param packBarcodes
     * @param lineNo
     * @param itemCode
     * @return
     */
    public GrLineV2 getGrLine(String warehouseId, String preInboundNo, String refDocNumber,
                              String goodsReceiptNo, String palletCode, String caseCode,
                              String packBarcodes, Long lineNo, String itemCode,
                              String companyCodeId, String plantId, String languageId) {
        Optional<GrLineV2> grLine = grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndPackBarcodesAndLineNoAndItemCodeAndDeletionIndicator(
                languageId,
                companyCodeId,
                plantId,
                warehouseId,
                preInboundNo,
                refDocNumber,
                goodsReceiptNo,
                palletCode,
                caseCode,
                packBarcodes,
                lineNo,
                itemCode,
                0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",packBarcodes: " + packBarcodes +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",goodsReceiptNo: " + goodsReceiptNo +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        return grLine.get();
    }

    /**
     * PRE_IB_NO/REF_DOC_NO/PACK_BARCODE/IB_LINE_NO/ITM_CODE
     *
     * @param preInboundNo
     * @param refDocNumber
     * @param packBarcodes
     * @param lineNo
     * @param itemCode
     * @return
     */
    public List<GrLineV2> getGrLine(String preInboundNo, String refDocNumber,
                                    String packBarcodes, Long lineNo, String itemCode,
                                    String companyCodeId, String plantId, String languageId) {
        List<GrLineV2> grLine =
                grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndRefDocNumberAndPackBarcodesAndLineNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        preInboundNo,
                        refDocNumber,
                        packBarcodes,
                        lineNo,
                        itemCode,
                        0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: " +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",packBarcodes: " + packBarcodes +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        return grLine;
    }

    /**
     * @param refDocNumber
     * @param packBarcodes
     * @return
     */
    public List<GrLineV2> getGrLine(String refDocNumber, String packBarcodes, String companyCodeId,
                                    String plantId, String languageId, String warehouseId) {
        List<GrLineV2> grLine =
                grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        packBarcodes,
                        0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: " +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",packBarcodes: " + packBarcodes + "," +
                    " doesn't exist in GRLine.");
        }

        return grLine;
    }

    /**
     * @param refDocNumber
     * @param packBarcodes
     * @param warehouseId
     * @param preInboundNo
     * @param caseCode
     * @return
     */
    public List<GrLineV2> getGrLine(String refDocNumber, String packBarcodes,
                                    String warehouseId, String preInboundNo,
                                    String caseCode, String companyCodeId, String plantId, String languageId) {
        List<GrLineV2> grLine =
                grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndWarehouseIdAndPreInboundNoAndCaseCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        refDocNumber,
                        packBarcodes,
                        warehouseId,
                        preInboundNo,
                        caseCode,
                        0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",packBarcodes: " + packBarcodes + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",caseCode: " + caseCode +
                    " doesn't exist.");
        }

        return grLine;
    }

    /**
     * @param refDocNumber
     * @param packBarcodes
     * @return
     */
    public List<GrLineV2> getGrLine(String refDocNumber, String packBarcodes, String companyCodeId, String plantId, String languageId) {
        List<GrLineV2> grLine =
                grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        refDocNumber,
                        packBarcodes,
                        0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: " +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",packBarcodes: " + packBarcodes + "," +
                    " doesn't exist in GRLine.");
        }

        return grLine;
    }

    /**
     * @param searchGrLine
     * @return
     * @throws ParseException
     */
    public List<GrLineV2> findGrLine(SearchGrLineV2 searchGrLine) throws ParseException {
        GrLineV2Specification spec = new GrLineV2Specification(searchGrLine);
        List<GrLineV2> results = grLineV2Repository.findAll(spec);
        return results;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @return
     */
    public List<GrLineV2> getGrLineForUpdate(String warehouseId, String preInboundNo, String refDocNumber,
                                             Long lineNo, String itemCode,
                                             String companyCodeId, String plantId, String languageId) {
        List<GrLineV2> grLine =
                grLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        lineNo,
                        itemCode,
                        0L);
        if (grLine.isEmpty()) {
            throw new BadRequestException("The given values: " +
                    ",warehouseId: " + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        return grLine;
    }

    /**
     * @param acceptQty
     * @param damageQty
     * @param loginUserID
     * @return
     */
    public List<PackBarcode> generatePackBarcode(Long acceptQty, Long damageQty, String warehouseId,
                                                 String companyCodeId, String plantId,
                                                 String languageId, String loginUserID) {
        AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
        long NUM_RAN_ID = 6;
        List<PackBarcode> packBarcodes = new ArrayList<>();

        // Accept Qty
        if (acceptQty != 0) {
            String nextRangeNumber = getNextRangeNumber(NUM_RAN_ID, warehouseId, companyCodeId, plantId, languageId, authTokenForIDMasterService.getAccess_token());
            PackBarcode acceptQtyPackBarcode = new PackBarcode();
            acceptQtyPackBarcode.setQuantityType("A");
            acceptQtyPackBarcode.setBarcode(nextRangeNumber);
            packBarcodes.add(acceptQtyPackBarcode);
        }

        // Damage Qty
        if (damageQty != 0) {
            String nextRangeNumber = getNextRangeNumber(NUM_RAN_ID, warehouseId, companyCodeId, plantId, languageId, authTokenForIDMasterService.getAccess_token());
            PackBarcode damageQtyPackBarcode = new PackBarcode();
            damageQtyPackBarcode.setQuantityType("D");
            damageQtyPackBarcode.setBarcode(nextRangeNumber);
            packBarcodes.add(damageQtyPackBarcode);
        }
        return packBarcodes;
    }

    /**
     * @param newGrLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<GrLineV2> createGrLine(@Valid List<AddGrLineV2> newGrLines, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<GrLineV2> createdGRLines = new ArrayList<>();
        try {
            String warehouseId = null;
            String languageId = null;
//            List<AddGrLineV2> dupGrLines = getDuplicates(newGrLines);
//            log.info("-------dupGrLines--------> " + dupGrLines);
//            if (dupGrLines != null && !dupGrLines.isEmpty()) {
//                newGrLines.removeAll(dupGrLines);
//                newGrLines.add(dupGrLines.get(0));
//                log.info("-------GrLines---removed-dupPickupLines-----> " + newGrLines);
//            }

            // Inserting multiple records
            for (AddGrLineV2 newGrLine : newGrLines) {
                warehouseId = newGrLine.getWarehouseId();
                languageId = newGrLine.getLanguageId();
                /*------------Inserting based on the PackBarcodes -----------*/
                for (PackBarcode packBarcode : newGrLine.getPackBarcodes()) {
                    GrLineV2 dbGrLine = new GrLineV2();
                    log.info("newGrLine : " + newGrLine);
                    BeanUtils.copyProperties(newGrLine, dbGrLine, CommonUtils.getNullPropertyNames(newGrLine));
                    dbGrLine.setCompanyCode(newGrLine.getCompanyCode());

                    // GR_QTY
                    if (packBarcode.getQuantityType().equalsIgnoreCase("A")) {
                        Double grQty = newGrLine.getAcceptedQty();
                        dbGrLine.setGoodReceiptQty(grQty);
                        dbGrLine.setAcceptedQty(grQty);
                        dbGrLine.setDamageQty(0D);
                        log.info("A-------->: " + dbGrLine);
                    } else if (packBarcode.getQuantityType().equalsIgnoreCase("D")) {
                        Double grQty = newGrLine.getDamageQty();
                        dbGrLine.setGoodReceiptQty(grQty);
                        dbGrLine.setDamageQty(newGrLine.getDamageQty());
                        dbGrLine.setAcceptedQty(0D);
                        log.info("D-------->: " + dbGrLine);
                    }

                    dbGrLine.setQuantityType(packBarcode.getQuantityType());
                    dbGrLine.setPackBarcodes(packBarcode.getBarcode());
                    dbGrLine.setStatusId(17L);

                    //V2 Code

                    IKeyValuePair description = stagingLineV2Repository.getDescription(newGrLine.getCompanyCode(),
                            newGrLine.getLanguageId(),
                            newGrLine.getPlantId(),
                            newGrLine.getWarehouseId());

                    statusDescription = stagingLineV2Repository.getStatusDescription(17L, newGrLine.getLanguageId());
                    dbGrLine.setStatusDescription(statusDescription);

                    dbGrLine.setCompanyDescription(description.getCompanyDesc());
                    dbGrLine.setPlantDescription(description.getPlantDesc());
                    dbGrLine.setWarehouseDescription(description.getWarehouseDesc());

                    if (newGrLine.getCbm() == null) {
                        newGrLine.setCbm(0D);
                    }
                    if (newGrLine.getGoodReceiptQty() == null) {
                        newGrLine.setGoodReceiptQty(0D);
                    }
                    dbGrLine.setCbmQuantity(newGrLine.getCbm() / newGrLine.getGoodReceiptQty());

                    dbGrLine.setDeletionIndicator(0L);
                    dbGrLine.setCreatedBy(loginUserID);
                    dbGrLine.setUpdatedBy(loginUserID);
                    dbGrLine.setCreatedOn(new Date());
                    dbGrLine.setUpdatedOn(new Date());
                    List<GrLineV2> oldGrLine = grLineV2Repository.findByGoodsReceiptNoAndItemCodeAndLineNoAndLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndPackBarcodesAndWarehouseIdAndPreInboundNoAndCaseCodeAndCreatedOnAndDeletionIndicator(
                            dbGrLine.getGoodsReceiptNo(), dbGrLine.getItemCode(), dbGrLine.getLineNo(),
                            dbGrLine.getLanguageId(),
                            dbGrLine.getCompanyCode(),
                            dbGrLine.getPlantId(),
                            dbGrLine.getRefDocNumber(),
                            dbGrLine.getPackBarcodes(),
                            dbGrLine.getWarehouseId(),
                            dbGrLine.getPreInboundNo(),
                            dbGrLine.getCaseCode(),
                            dbGrLine.getCreatedOn(),
                            0L
                    );
                    GrLineV2 createdGRLine = null;

                    //validate to check if grline is already exists
                    if (oldGrLine == null || oldGrLine.isEmpty()) {
                        createdGRLine = grLineV2Repository.save(dbGrLine);
                        log.info("createdGRLine : " + createdGRLine);
                        createdGRLines.add(createdGRLine);

                        if (createdGRLine != null) {
                            // Record Insertion in PUTAWAYHEADER table
                            createPutAwayHeader(createdGRLine, loginUserID);
                        }
                    }
                }
                log.info("Records were inserted successfully...");
            }

            // STATUS updates
            /*
             * Pass WH_ID/PRE_IB_NO/REF_DOC_NO/GR_NO/IB_LINE_NO/ITM_CODE in GRLINE table and
             * validate STATUS_ID of the all the filtered line items = 17 , if yes
             */
            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            StatusId idStatus = idmasterService.getStatus(17L, warehouseId, languageId, authTokenForIDService.getAccess_token());
            for (GrLineV2 grLine : createdGRLines) {
                /*
                 * 1. Update GRHEADER table with STATUS_ID=17 by Passing WH_ID/GR_NO/CASE_CODE/REF_DOC_NO and
                 * GR_CNF_BY with USR_ID and GR_CNF_ON with Server time
                 */
//                List<GrHeaderV2> grHeaders = grHeaderService.getGrHeaderV2(
//                        grLine.getWarehouseId(),
//                        grLine.getGoodsReceiptNo(),
//                        grLine.getCaseCode(),
//                        grLine.getCompanyCode(),
//                        grLine.getPlantId(),
//                        grLine.getLanguageId(),
//                        grLine.getRefDocNumber());
                List<GrHeaderV2> grHeaders = grHeaderV2Repository.getGrHeaderV2(
                        grLine.getWarehouseId(),
                        grLine.getGoodsReceiptNo(),
                        grLine.getCaseCode(),
                        grLine.getCompanyCode(),
                        grLine.getPlantId(),
                        grLine.getLanguageId(),
                        grLine.getRefDocNumber());
                for (GrHeaderV2 grHeader : grHeaders) {
                    if (grHeader.getCompanyCode() == null) {
                        grHeader.setCompanyCode(getCompanyCode());
                    }
                    grHeader.setStatusId(17L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(17L, grLine.getLanguageId());
                    grHeader.setStatusDescription(statusDescription);
                    grHeader.setReferenceField10(idStatus.getStatus());
                    grHeader.setCreatedBy(loginUserID);
                    grHeader.setCreatedOn(new Date());
                    grHeader = grHeaderRepository.save(grHeader);
                    log.info("grHeader updated: " + grHeader);
                }

                /*
                 * '2. 'Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE/CASECODE in STAGINIGLINE table and
                 * update STATUS_ID as 17
                 */
//                List<StagingLineEntityV2> stagingLineEntityList =
//                        stagingLineV2Service.getStagingLine(grLine.getWarehouseId(),
//                                grLine.getRefDocNumber(),
//                                grLine.getPreInboundNo(),
//                                grLine.getLineNo(),
//                                grLine.getItemCode(),
//                                grLine.getCaseCode(),
//                                grLine.getManufacturerCode());

                List<StagingLineEntityV2> stagingLineEntityList =
                        stagingLineV2Service.getStagingLine(
                                grLine.getCompanyCode(),
                                grLine.getPlantId(),
                                grLine.getLanguageId(),
                                grLine.getWarehouseId(),
                                grLine.getRefDocNumber(),
                                grLine.getPreInboundNo(),
                                grLine.getLineNo(),
                                grLine.getItemCode(),
                                grLine.getCaseCode());
                for (StagingLineEntityV2 stagingLineEntity : stagingLineEntityList) {

                    //v2 code
                    if (stagingLineEntity.getRec_accept_qty() == null) {
                        stagingLineEntity.setRec_accept_qty(0D);
                    }
                    if (grLine.getAcceptedQty() == null) {
                        grLine.setAcceptedQty(0D);
                    }
                    if (stagingLineEntity.getRec_damage_qty() == null) {
                        stagingLineEntity.setRec_damage_qty(0D);
                    }
                    if (grLine.getDamageQty() == null) {
                        grLine.setDamageQty(0D);
                    }

                    Double rec_accept_qty = stagingLineEntity.getRec_accept_qty() + grLine.getAcceptedQty();
                    Double rec_damage_qty = stagingLineEntity.getRec_damage_qty() + grLine.getDamageQty();

                    stagingLineEntity.setRec_accept_qty(rec_accept_qty);
                    stagingLineEntity.setRec_damage_qty(rec_damage_qty);

                    stagingLineEntity.setStatusId(17L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(17L, grLine.getLanguageId());
                    stagingLineEntity.setStatusDescription(statusDescription);
                    stagingLineEntity = stagingLineV2Repository.save(stagingLineEntity);
                    log.info("stagingLineEntity updated: " + stagingLineEntity);
                }

                /*
                 * 3. Then Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE in INBOUNDLINE table and
                 * updated STATUS_ID as 17
                 */
//                InboundLine inboundLine = inboundLineService.getInboundLine(grLine.getWarehouseId(),
//                        grLine.getRefDocNumber(), grLine.getPreInboundNo(), grLine.getLineNo(), grLine.getItemCode());
                InboundLineV2 inboundLine = inboundLineV2Repository.getInboundLineV2(grLine.getWarehouseId(),
                        grLine.getLineNo(),
                        grLine.getPreInboundNo(),
                        grLine.getItemCode(),
                        grLine.getCompanyCode(),
                        grLine.getPlantId(),
                        grLine.getLanguageId(),
                        grLine.getRefDocNumber());
                inboundLine.setStatusId(17L);
                inboundLine.setStatusDescription(statusDescription);
                inboundLine = inboundLineV2Repository.save(inboundLine);
                log.info("inboundLine updated : " + inboundLine);
            }
            return createdGRLines;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param grLineList
     * @return
     */
//    public static List<AddGrLineV2> getDuplicates(List<AddGrLineV2> grLineList) {
//        return getDuplicatesMap(grLineList).values().stream()
//                .filter(duplicates -> duplicates.size() > 1)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//    }

    /**
     * @param addGrLineList
     * @return
     */
//    private static Map<String, List<AddGrLineV2>> getDuplicatesMap(List<AddGrLineV2> addGrLineList) {
//        return addGrLineList.stream().collect(Collectors.groupingBy(AddGrLineV2::uniqueAttributes));
//    }

    /**
     * @param createdGRLine
     * @param loginUserID
     */
//    private void createPutAwayHeader(GrLineV2 createdGRLine, String loginUserID) {
//        Double cbm = null;
//
//        if (createdGRLine.getCbm() != null && createdGRLine.getCbm() == 0) {
//            cbm = null;
//        }
//        if (createdGRLine.getCbm() != null) {
//            cbm = createdGRLine.getCbm();
//        }
//
//        while (cbm == null || cbm != 0) {
//            if (createdGRLine.getAssignedUserId() == null && cbm != null) {
//                cbm = 0D;       //condition to exit the loop
//            }
//            //  ASS_HE_NO
//            if (createdGRLine != null && createdGRLine.getAssignedUserId() != null) {
//                // Insert record into PutAwayHeader
//                //private Double putAwayQuantity, private String putAwayUom;
//                PutAwayHeaderV2 putAwayHeader = new PutAwayHeaderV2();
//                BeanUtils.copyProperties(createdGRLine, putAwayHeader, CommonUtils.getNullPropertyNames(createdGRLine));
//                putAwayHeader.setCompanyCodeId(createdGRLine.getCompanyCode());
//                putAwayHeader.setReferenceField5(createdGRLine.getItemCode());
//                // PA_NO
//                AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
//                long NUM_RAN_CODE = 7;
//                String nextPANumber = getNextRangeNumber(NUM_RAN_CODE, createdGRLine.getWarehouseId(),
//                        createdGRLine.getCompanyCode(),
//                        createdGRLine.getPlantId(),
//                        createdGRLine.getLanguageId(),
//                        authTokenForIDMasterService.getAccess_token());
//                putAwayHeader.setPutAwayNumber(nextPANumber);
//
//                putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
//                putAwayHeader.setPutAwayUom(createdGRLine.getOrderUom());
//
//                //set bar code id for packbarcode
//                putAwayHeader.setPackBarcodes(createdGRLine.getBarcodeId());
//
//                //set pack bar code for actual packbarcode
//                putAwayHeader.setActualPackBarcodes(createdGRLine.getPackBarcodes());
//
//                //-----------------PROP_ST_BIN---------------------------------------------
//                /*
//                 * 1. Fetch ITM_CODE from GRLINE table and Pass WH_ID/ITM_CODE/BIN_CLASS_ID=1 in INVENTORY table and Fetch ST_BIN values.
//                 * Pass ST_BIN values into STORAGEBIN table  where ST_SEC_ID = ZB,ZG,ZD,ZC,ZT and PUTAWAY_BLOCK and PICK_BLOCK columns are Null( FALSE) and
//                 * fetch the filtered values and sort the latest and insert.
//                 *
//                 * If WH_ID=111, fetch ST_BIN values of ST_SEC_ID= ZT and sort the latest and insert
//                 */
////			List<String> storageSectionIds = Arrays.asList("ZB","ZG","ZD","ZC","ZT");
//
//                //V2 Code
////                Long binClassId = 1L;                   //currently(21-8-2023) hard code to get the output
//                Long binClassId = 0L;                   //actual code follows
//                if (createdGRLine.getInboundOrderTypeId() == 1 || createdGRLine.getInboundOrderTypeId() == 2) {
//                    binClassId = 1L;
//                } else if (createdGRLine.getInboundOrderTypeId() == 3) {
//                    binClassId = 7L;
//                }
//
//                if (createdGRLine.getCbm() == null || cbm == null) {
//                    cbm = 0D;
//                }
//                // Discussed to remove this condition
////			if (createdGRLine.getWarehouseId().equalsIgnoreCase(WAREHOUSEID_111)) {
////				storageSectionIds = Arrays.asList("ZT");
////			}
//                List<InventoryV2> stBinInventoryList =
//                        inventoryV2Repository.findByWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(createdGRLine.getWarehouseId(),
//                                createdGRLine.getItemCode(), binClassId, 0L);
//                log.info("stBinInventoryList -----------> : " + stBinInventoryList);
//
//                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
//
//                if (createdGRLine.getInterimStorageBin() != null) {
//
//                    putAwayHeader.setProposedStorageBin(createdGRLine.getInterimStorageBin());
//                    putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
//
//                } else if (!stBinInventoryList.isEmpty()) {
//                    List<String> stBins = stBinInventoryList.stream().map(Inventory::getStorageBin).collect(Collectors.toList());
//                    log.info("stBins -----------> : " + stBins);
//
//                    StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
//                    storageBinPutAway.setStorageBin(stBins);
//                    storageBinPutAway.setWarehouseId(createdGRLine.getWarehouseId());
//                    storageBinPutAway.setBinClassId(binClassId);
//                    StorageBinV2[] storageBin = mastersService.getStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
//                    log.info("storagebin -----------> : " + storageBin);
//
//                    if (createdGRLine.getCbm() == null || cbm == null) {
//                        cbm = 0D;
//                    }
//
//                    if (createdGRLine.getCbm() != null) {
//                        Double cbmPerQuantity = createdGRLine.getCbmQuantity();
//                        List<StorageBinV2> stBinList = Arrays.stream(storageBin)
//                                .filter(n -> n.getCapacityCheck() == 1 &&
//                                        cbmPerQuantity < Double.valueOf(n.getRemainingVolume()))
//                                .sorted(Comparator.comparing(StorageBinV2::getRemainingVolume)).collect(Collectors.toList());
//                        if (stBinList != null && stBinList.size() > 0) {
//                            String proposedStorageBin = stBinList.get(0).getStorageBin();
//
//                            if (stBinList.get(0).getTotalVolume() == null) {
//                                stBinList.get(0).setTotalVolume("0");
//                            }
//                            if (stBinList.get(0).getAllocatedVolume() == null) {
//                                stBinList.get(0).setAllocatedVolume(0D);
//                            }
//                            if (stBinList.get(0).getOccupiedVolume() == null) {
//                                stBinList.get(0).setOccupiedVolume("0");
//                            }
//                            if (stBinList.get(0).getRemainingVolume() == null) {
//                                stBinList.get(0).setRemainingVolume("0");
//                            }
//
//                            Double allocatedVolume;
//                            Double occupiedVolume = Double.valueOf(stBinList.get(0).getOccupiedVolume());
//                            Double remainingVolume = Double.valueOf(stBinList.get(0).getRemainingVolume());
//
//                            Integer qty = (int) (remainingVolume / cbmPerQuantity);
//                            allocatedVolume = qty * cbmPerQuantity;
//                            remainingVolume = remainingVolume - (allocatedVolume + occupiedVolume);
//                            cbm = cbm - allocatedVolume;
//
//                            StorageBinV2 modifiedStorageBin = new StorageBinV2();
//                            modifiedStorageBin.setRemainingVolume(String.valueOf(remainingVolume));
//                            modifiedStorageBin.setAllocatedVolume(allocatedVolume);
//
//                            StorageBinV2 updateStorageBinV2 = mastersService.updateStorageBinV2(proposedStorageBin,
//                                    modifiedStorageBin,
//                                    createdGRLine.getCompanyCode(),
//                                    createdGRLine.getPlantId(),
//                                    createdGRLine.getLanguageId(),
//                                    createdGRLine.getWarehouseId(),
//                                    loginUserID,
//                                    authTokenForMastersService.getAccess_token());
//
//                            putAwayHeader.setProposedStorageBin(proposedStorageBin);
//                            putAwayHeader.setPutAwayQuantity((remainingVolume / createdGRLine.getCbm()) * createdGRLine.getGoodReceiptQty());
//                        }
//                    } else if (storageBin != null && storageBin.length > 0) {
//                        putAwayHeader.setProposedStorageBin(createdGRLine.getInterimStorageBin());
//                        putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
//                    }
//                } else {
//                    // If ST_BIN value is null
//                    // Validate if ACCEPT_QTY is not null and DAMAGE_QTY is NULL,
//                    // then pass WH_ID in STORAGEBIN table and fetch ST_BIN values for STATUS_ID=EMPTY.
//                    log.info("QuantityType : " + createdGRLine.getQuantityType());
//                    if (createdGRLine.getQuantityType().equalsIgnoreCase("A")) {
//                        StorageBinV2[] storageBinEMPTY =
//                                mastersService.getStorageBinByStatusV2(createdGRLine.getWarehouseId(), 0L, authTokenForMastersService.getAccess_token());
//                        log.info("storageBinEMPTY -----------> : " + storageBinEMPTY);
//                        List<StorageBinV2> storageBinEMPTYList = Arrays.asList(storageBinEMPTY);
//                        List<String> stBins = storageBinEMPTYList.stream().map(StorageBinV2::getStorageBin).collect(Collectors.toList());
//
//                        /*
//                         * Pass ST_BIN values into STORAGEBIN table  where where ST_SEC_ID = ZB,ZG,ZD,ZC,ZT and
//                         * PUTAWAY_BLOCK and PICK_BLOCK columns are Null( FALSE) and fetch the filteerd values and
//                         * Sort the latest and Insert.
//                         */
//                        // Prod Issue - SQL Grammer on StorageBin-----23-08-2022
//                        // Start
//                        if (stBins != null && stBins.size() > 2000) {
//                            List<List<String>> splitedList = CommonUtils.splitArrayList(stBins, 1800); // SQL Query accepts max 2100 count only in IN condition
////						storageSectionIds = Arrays.asList("ZB","ZG","ZC","ZT"); // Removing ZD
//                            StorageBinV2[] storageBin = getStorageBinForSplitedListV2(splitedList, binClassId,
//                                    createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());
//
//                            // Provided Null else validation
//                            log.info("storageBin2 -----------> : " + storageBin);
//                            if (storageBin != null && storageBin.length > 0) {
//                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
//                            } else {
//                                Long binClassID = 2L;
//                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
//                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
//                            }
//                        } else {
//                            StorageBinV2[] storageBin = getStorageBinV2(binClassId, stBins, createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());
//                            if (storageBin != null && storageBin.length > 0) {
//                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
//                            } else {
//                                Long binClassID = 2L;
//                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
//                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
//                            }
//                        }
//                        // End
//                    }
//
//                    /*
//                     * Validate if ACCEPT_QTY is null and DAMAGE_QTY is not NULL , then pass WH_ID in STORAGEBIN table and
//                     * fetch ST_BIN values for STATUS_ID=EMPTY.
//                     */
//                    if (createdGRLine.getQuantityType().equalsIgnoreCase("D")) {
//                        StorageBinV2[] storageBinEMPTY =
//                                mastersService.getStorageBinByStatusV2(createdGRLine.getWarehouseId(), 0L, authTokenForMastersService.getAccess_token());
//                        List<StorageBinV2> storageBinEMPTYList = Arrays.asList(storageBinEMPTY);
//                        List<String> stBins = storageBinEMPTYList.stream().map(StorageBinV2::getStorageBin).collect(Collectors.toList());
//
//                        // Pass ST_BIN values into STORAGEBIN table  where where ST_SEC_ID = ZD and PUTAWAY_BLOCK and
//                        // PICK_BLOCK columns are Null( FALSE)
//                        if (stBins != null && stBins.size() > 2000) {
////						storageSectionIds = Arrays.asList("ZD");
//                            List<List<String>> splitedList = CommonUtils.splitArrayList(stBins, 1800); // SQL Query accepts max 2100 count only in IN condition
//                            StorageBinV2[] storageBin = getStorageBinForSplitedListV2(splitedList, binClassId,
//                                    createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());
//                            if (storageBin != null && storageBin.length > 0) {
//                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
//                            } else {
//                                Long binClassID = 2L;
//                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
//                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
//                            }
//                        } else {
//                            StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
//                            storageBinPutAway.setStorageBin(stBins);
//                            storageBinPutAway.setBinClassId(binClassId);
//                            storageBinPutAway.setWarehouseId(createdGRLine.getWarehouseId());
//                            StorageBinV2[] storageBin = mastersService.getStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
//                            if (storageBin != null && storageBin.length > 0) {
//                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
//                            } else {
//                                Long binClassID = 2L;
//                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
//                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
//                            }
//                        }
//                    }
//
//                    putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
//                }
//
//                /////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                //PROP_HE_NO	<- PAWAY_HE_NO
//                putAwayHeader.setProposedHandlingEquipment(createdGRLine.getPutAwayHandlingEquipment());
//                putAwayHeader.setStatusId(19L);
//                statusDescription = stagingLineV2Repository.getStatusDescription(19L, createdGRLine.getLanguageId());
//                putAwayHeader.setStatusDescription(statusDescription);
//                putAwayHeader.setDeletionIndicator(0L);
//                putAwayHeader.setCreatedBy(loginUserID);
//                putAwayHeader.setCreatedOn(new Date());
//                putAwayHeader.setUpdatedBy(loginUserID);
//                putAwayHeader.setUpdatedOn(new Date());
//                putAwayHeader = putAwayHeaderRepository.save(putAwayHeader);
//                log.info("putAwayHeader : " + putAwayHeader);
//
//                /*----------------Inventory tables Create---------------------------------------------*/
//                InventoryV2 createdinventory = createInventory(createdGRLine);
//
//                /*----------------INVENTORYMOVEMENT table Update---------------------------------------------*/
//                createInventoryMovement(createdGRLine, createdinventory);
//            }
//        }
//    }

    private void createPutAwayHeader(GrLineV2 createdGRLine, String loginUserID) {
        Double cbm = null;

        if (createdGRLine.getCbm() != null && createdGRLine.getCbm() == 0) {
            cbm = null;
        }
        if (createdGRLine.getCbm() != null) {
            cbm = createdGRLine.getCbm();
        }

        while (cbm == null || cbm != 0) {
            if (createdGRLine.getAssignedUserId() == null && cbm != null) {
                cbm = 0D;       //condition to exit the loop
            }
            //  ASS_HE_NO
            if (createdGRLine != null && createdGRLine.getAssignedUserId() != null) {
                // Insert record into PutAwayHeader
                //private Double putAwayQuantity, private String putAwayUom;
                PutAwayHeader putAwayHeader = new PutAwayHeader();
                BeanUtils.copyProperties(createdGRLine, putAwayHeader, CommonUtils.getNullPropertyNames(createdGRLine));
                putAwayHeader.setCompanyCodeId(createdGRLine.getCompanyCode());
                putAwayHeader.setReferenceField5(createdGRLine.getItemCode());
                putAwayHeader.setManufacturerName(createdGRLine.getManufacturerName());
                // PA_NO
                AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
                long NUM_RAN_CODE = 7;
                String nextPANumber = getNextRangeNumber(NUM_RAN_CODE, createdGRLine.getWarehouseId(),
                        createdGRLine.getCompanyCode(),
                        createdGRLine.getPlantId(),
                        createdGRLine.getLanguageId(),
                        authTokenForIDMasterService.getAccess_token());
                putAwayHeader.setPutAwayNumber(nextPANumber);

                putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
                putAwayHeader.setPutAwayUom(createdGRLine.getOrderUom());

                //set bar code id for packbarcode
//                putAwayHeader.setPackBarcodes(createdGRLine.getBarcodeId());
                putAwayHeader.setBarcodeId(createdGRLine.getBarcodeId());

                //set pack bar code for actual packbarcode
                putAwayHeader.setPackBarcodes(createdGRLine.getPackBarcodes());

                //-----------------PROP_ST_BIN---------------------------------------------
                /*
                 * 1. Fetch ITM_CODE from GRLINE table and Pass WH_ID/ITM_CODE/BIN_CLASS_ID=1 in INVENTORY table and Fetch ST_BIN values.
                 * Pass ST_BIN values into STORAGEBIN table  where ST_SEC_ID = ZB,ZG,ZD,ZC,ZT and PUTAWAY_BLOCK and PICK_BLOCK columns are Null( FALSE) and
                 * fetch the filtered values and sort the latest and insert.
                 *
                 * If WH_ID=111, fetch ST_BIN values of ST_SEC_ID= ZT and sort the latest and insert
                 */
//			List<String> storageSectionIds = Arrays.asList("ZB","ZG","ZD","ZC","ZT");

                //V2 Code
//                Long binClassId = 1L;                   //currently(21-8-2023) hard code to get the output
                Long binClassId = 0L;                   //actual code follows
                if (createdGRLine.getInboundOrderTypeId() == 1 || createdGRLine.getInboundOrderTypeId() == 2) {
                    binClassId = 1L;
                } else if (createdGRLine.getInboundOrderTypeId() == 3) {
                    binClassId = 7L;
                }

                if (createdGRLine.getCbm() == null || cbm == null) {
                    cbm = 0D;
                }
                // Discussed to remove this condition
//			if (createdGRLine.getWarehouseId().equalsIgnoreCase(WAREHOUSEID_111)) {
//				storageSectionIds = Arrays.asList("ZT");
//			}
                List<InventoryV2> stBinInventoryList =
                        inventoryV2Repository.findByWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(createdGRLine.getWarehouseId(),
                                createdGRLine.getItemCode(), binClassId, 0L);
                log.info("stBinInventoryList -----------> : " + stBinInventoryList);

                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

                if (createdGRLine.getInterimStorageBin() != null) {

                    putAwayHeader.setProposedStorageBin(createdGRLine.getInterimStorageBin());
                    putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());

                } else if (!stBinInventoryList.isEmpty()) {
                    List<String> stBins = stBinInventoryList.stream().map(Inventory::getStorageBin).collect(Collectors.toList());
                    log.info("stBins -----------> : " + stBins);

                    StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
                    storageBinPutAway.setStorageBin(stBins);
                    storageBinPutAway.setWarehouseId(createdGRLine.getWarehouseId());
                    storageBinPutAway.setBinClassId(binClassId);
                    StorageBinV2[] storageBin = mastersService.getStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
                    log.info("storagebin -----------> : " + storageBin);

                    if (createdGRLine.getCbm() == null || cbm == null) {
                        cbm = 0D;
                    }

                    if (createdGRLine.getCbm() != null) {
                        Double cbmPerQuantity = createdGRLine.getCbmQuantity();
                        List<StorageBinV2> stBinList = Arrays.stream(storageBin)
                                .filter(n -> n.getCapacityCheck() == 1 &&
                                        cbmPerQuantity < Double.valueOf(n.getRemainingVolume()))
                                .sorted(Comparator.comparing(StorageBinV2::getRemainingVolume)).collect(Collectors.toList());
                        if (stBinList != null && stBinList.size() > 0) {
                            String proposedStorageBin = stBinList.get(0).getStorageBin();

                            if (stBinList.get(0).getTotalVolume() == null) {
                                stBinList.get(0).setTotalVolume("0");
                            }
                            if (stBinList.get(0).getAllocatedVolume() == null) {
                                stBinList.get(0).setAllocatedVolume(0D);
                            }
                            if (stBinList.get(0).getOccupiedVolume() == null) {
                                stBinList.get(0).setOccupiedVolume("0");
                            }
                            if (stBinList.get(0).getRemainingVolume() == null) {
                                stBinList.get(0).setRemainingVolume("0");
                            }

                            Double allocatedVolume;
                            Double occupiedVolume = Double.valueOf(stBinList.get(0).getOccupiedVolume());
                            Double remainingVolume = Double.valueOf(stBinList.get(0).getRemainingVolume());

                            Integer qty = (int) (remainingVolume / cbmPerQuantity);
                            allocatedVolume = qty * cbmPerQuantity;
                            remainingVolume = remainingVolume - (allocatedVolume + occupiedVolume);
                            cbm = cbm - allocatedVolume;

                            StorageBinV2 modifiedStorageBin = new StorageBinV2();
                            modifiedStorageBin.setRemainingVolume(String.valueOf(remainingVolume));
                            modifiedStorageBin.setAllocatedVolume(allocatedVolume);

                            StorageBinV2 updateStorageBinV2 = mastersService.updateStorageBinV2(proposedStorageBin,
                                    modifiedStorageBin,
                                    createdGRLine.getCompanyCode(),
                                    createdGRLine.getPlantId(),
                                    createdGRLine.getLanguageId(),
                                    createdGRLine.getWarehouseId(),
                                    loginUserID,
                                    authTokenForMastersService.getAccess_token());

                            putAwayHeader.setProposedStorageBin(proposedStorageBin);
                            putAwayHeader.setPutAwayQuantity((remainingVolume / createdGRLine.getCbm()) * createdGRLine.getGoodReceiptQty());
                        }
                    } else if (storageBin != null && storageBin.length > 0) {
                        putAwayHeader.setProposedStorageBin(createdGRLine.getInterimStorageBin());
                        putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
                    }
                } else {
                    // If ST_BIN value is null
                    // Validate if ACCEPT_QTY is not null and DAMAGE_QTY is NULL,
                    // then pass WH_ID in STORAGEBIN table and fetch ST_BIN values for STATUS_ID=EMPTY.
                    log.info("QuantityType : " + createdGRLine.getQuantityType());
                    if (createdGRLine.getQuantityType().equalsIgnoreCase("A")) {
                        StorageBinV2[] storageBinEMPTY =
                                mastersService.getStorageBinByStatusV2(createdGRLine.getWarehouseId(), 0L, authTokenForMastersService.getAccess_token());
                        log.info("storageBinEMPTY -----------> : " + storageBinEMPTY);
                        List<StorageBinV2> storageBinEMPTYList = Arrays.asList(storageBinEMPTY);
                        List<String> stBins = storageBinEMPTYList.stream().map(StorageBinV2::getStorageBin).collect(Collectors.toList());

                        /*
                         * Pass ST_BIN values into STORAGEBIN table  where where ST_SEC_ID = ZB,ZG,ZD,ZC,ZT and
                         * PUTAWAY_BLOCK and PICK_BLOCK columns are Null( FALSE) and fetch the filteerd values and
                         * Sort the latest and Insert.
                         */
                        // Prod Issue - SQL Grammer on StorageBin-----23-08-2022
                        // Start
                        if (stBins != null && stBins.size() > 2000) {
                            List<List<String>> splitedList = CommonUtils.splitArrayList(stBins, 1800); // SQL Query accepts max 2100 count only in IN condition
//						storageSectionIds = Arrays.asList("ZB","ZG","ZC","ZT"); // Removing ZD
                            StorageBinV2[] storageBin = getStorageBinForSplitedListV2(splitedList, binClassId,
                                    createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());

                            // Provided Null else validation
                            log.info("storageBin2 -----------> : " + storageBin);
                            if (storageBin != null && storageBin.length > 0) {
                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
                            } else {
                                Long binClassID = 2L;
                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
                            }
                        } else {
                            StorageBinV2[] storageBin = getStorageBinV2(binClassId, stBins, createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());
                            if (storageBin != null && storageBin.length > 0) {
                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
                            } else {
                                Long binClassID = 2L;
                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
                            }
                        }
                        // End
                    }

                    /*
                     * Validate if ACCEPT_QTY is null and DAMAGE_QTY is not NULL , then pass WH_ID in STORAGEBIN table and
                     * fetch ST_BIN values for STATUS_ID=EMPTY.
                     */
                    if (createdGRLine.getQuantityType().equalsIgnoreCase("D")) {
                        StorageBinV2[] storageBinEMPTY =
                                mastersService.getStorageBinByStatusV2(createdGRLine.getWarehouseId(), 0L, authTokenForMastersService.getAccess_token());
                        List<StorageBinV2> storageBinEMPTYList = Arrays.asList(storageBinEMPTY);
                        List<String> stBins = storageBinEMPTYList.stream().map(StorageBinV2::getStorageBin).collect(Collectors.toList());

                        // Pass ST_BIN values into STORAGEBIN table  where where ST_SEC_ID = ZD and PUTAWAY_BLOCK and
                        // PICK_BLOCK columns are Null( FALSE)
                        if (stBins != null && stBins.size() > 2000) {
//						storageSectionIds = Arrays.asList("ZD");
                            List<List<String>> splitedList = CommonUtils.splitArrayList(stBins, 1800); // SQL Query accepts max 2100 count only in IN condition
                            StorageBinV2[] storageBin = getStorageBinForSplitedListV2(splitedList, binClassId,
                                    createdGRLine.getWarehouseId(), authTokenForMastersService.getAccess_token());
                            if (storageBin != null && storageBin.length > 0) {
                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
                            } else {
                                Long binClassID = 2L;
                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
                            }
                        } else {
                            StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
                            storageBinPutAway.setStorageBin(stBins);
                            storageBinPutAway.setBinClassId(binClassId);
                            storageBinPutAway.setWarehouseId(createdGRLine.getWarehouseId());
                            StorageBinV2[] storageBin = mastersService.getStorageBinV2(storageBinPutAway, authTokenForMastersService.getAccess_token());
                            if (storageBin != null && storageBin.length > 0) {
                                putAwayHeader.setProposedStorageBin(storageBin[0].getStorageBin());
                            } else {
                                Long binClassID = 2L;
                                StorageBinV2 stBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), binClassID, authTokenForMastersService.getAccess_token());
                                putAwayHeader.setProposedStorageBin(stBin.getStorageBin());
                            }
                        }
                    }

                    putAwayHeader.setPutAwayQuantity(createdGRLine.getGoodReceiptQty());
                }

                /////////////////////////////////////////////////////////////////////////////////////////////////////

                //PROP_HE_NO	<- PAWAY_HE_NO
                putAwayHeader.setProposedHandlingEquipment(createdGRLine.getPutAwayHandlingEquipment());
                putAwayHeader.setStatusId(19L);
                statusDescription = stagingLineV2Repository.getStatusDescription(19L, createdGRLine.getLanguageId());
                putAwayHeader.setStatusDescription(statusDescription);
                putAwayHeader.setDeletionIndicator(0L);
                putAwayHeader.setCreatedBy(loginUserID);
                putAwayHeader.setCreatedOn(new Date());
                putAwayHeader.setUpdatedBy(loginUserID);
                putAwayHeader.setUpdatedOn(new Date());
                putAwayHeader = putAwayHeaderRepository.save(putAwayHeader);
                log.info("putAwayHeader : " + putAwayHeader);

                /*----------------Inventory tables Create---------------------------------------------*/
                InventoryV2 createdinventory = createInventory(createdGRLine);

                /*----------------INVENTORYMOVEMENT table Update---------------------------------------------*/
                createInventoryMovement(createdGRLine, createdinventory);
            }
        }
    }

    /**
     * @param splitedList
     * @param storageSectionIds
     * @param authToken
     * @return
     */
    private StorageBin[] getStorageBinForSplitedList(List<List<String>> splitedList, List<String> storageSectionIds, String warehouseId, String authToken) {
        for (List<String> list : splitedList) {
            StorageBin[] storageBin = getStorageBin(storageSectionIds, list, warehouseId, authToken);
            if (storageBin != null && storageBin.length > 0) {
                return storageBin;
            }
        }
        return null;
    }

    private StorageBinV2[] getStorageBinForSplitedListV2(List<List<String>> splitedList, Long binClassId, String warehouseId, String authToken) {
        for (List<String> list : splitedList) {
            StorageBinV2[] storageBin = getStorageBinV2(binClassId, list, warehouseId, authToken);
            if (storageBin != null && storageBin.length > 0) {
                return storageBin;
            }
        }
        return null;
    }

    /**
     * @param storageSectionIds
     * @param stBins
     * @param authToken
     * @return
     */
    private StorageBin[] getStorageBin(List<String> storageSectionIds, List<String> stBins, String warehouseId, String authToken) {
        StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
        storageBinPutAway.setStorageBin(stBins);
        storageBinPutAway.setStorageSectionIds(storageSectionIds);
        storageBinPutAway.setWarehouseId(warehouseId);
        StorageBin[] storageBin = mastersService.getStorageBin(storageBinPutAway, authToken);
        return storageBin;
    }

    private StorageBinV2[] getStorageBinV2(Long binClassId, List<String> stBins, String warehouseId, String authToken) {
        StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
        storageBinPutAway.setStorageBin(stBins);
        storageBinPutAway.setBinClassId(binClassId);
        storageBinPutAway.setWarehouseId(warehouseId);
        StorageBinV2[] storageBin = mastersService.getStorageBinV2(storageBinPutAway, authToken);
        return storageBin;
    }

    /**
     * @param <T>
     * @param list
     * @return
     */
    private static <T> List[] splitList(List<String> list) {
        // get the size of the list
        int size = list.size();

        // construct a new list from the returned view by `List.subList()` method
        List<String> first = new ArrayList<>(list.subList(0, (size + 1) / 2));
        List<String> second = new ArrayList<>(list.subList((size + 1) / 2, size));

        // return an array of lists to accommodate both lists
        return new List[]{first, second};
    }

    /**
     * @param createdGRLine
     * @param createdinventory
     */
    private void createInventoryMovement(GrLineV2 createdGRLine, InventoryV2 createdinventory) {
        InventoryMovement inventoryMovement = new InventoryMovement();
        BeanUtils.copyProperties(createdGRLine, inventoryMovement, CommonUtils.getNullPropertyNames(createdGRLine));
        inventoryMovement.setCompanyCodeId(createdGRLine.getCompanyCode());

        // MVT_TYP_ID
        inventoryMovement.setMovementType(1L);

        // SUB_MVT_TYP_ID
        inventoryMovement.setSubmovementType(1L);

        // STR_MTD
        inventoryMovement.setStorageMethod("1");

        // STR_NO
        inventoryMovement.setBatchSerialNumber("1");

        // MVT_DOC_NO
        inventoryMovement.setMovementDocumentNo(createdGRLine.getGoodsReceiptNo());

        // ST_BIN
        inventoryMovement.setStorageBin(createdinventory.getStorageBin());

        // MVT_QTY
        inventoryMovement.setMovementQty(createdGRLine.getGoodReceiptQty());

        // MVT_QTY_VAL
        inventoryMovement.setMovementQtyValue("P");

        // MVT_UOM
        inventoryMovement.setInventoryUom(createdGRLine.getOrderUom());

        inventoryMovement.setVariantCode(1L);
        inventoryMovement.setVariantSubCode("1");

        inventoryMovement.setPackBarcodes(createdGRLine.getPackBarcodes());

        // IM_CTD_BY
        inventoryMovement.setCreatedBy(createdGRLine.getCreatedBy());

        // IM_CTD_ON
        inventoryMovement.setCreatedOn(createdGRLine.getCreatedOn());
        inventoryMovement = inventoryMovementRepository.save(inventoryMovement);
        log.info("inventoryMovement : " + inventoryMovement);
    }

    /**
     * @param createdGRLine
     * @return
     */
    private InventoryV2 createInventory(GrLineV2 createdGRLine) {

//        InventoryV2 dbInventory = inventoryService.getInventory(createdGRLine.getCompanyCode(),
//                createdGRLine.getPlantId(),
//                createdGRLine.getLanguageId(),
//                createdGRLine.getWarehouseId(),
//                createdGRLine.getPackBarcodes(),
//                createdGRLine.getItemCode(),
//                createdGRLine.getManufacturerCode());
//
//        if (dbInventory != null) {

            InventoryV2 inventory = new InventoryV2();
            BeanUtils.copyProperties(createdGRLine, inventory, CommonUtils.getNullPropertyNames(createdGRLine));
            inventory.setCompanyCodeId(createdGRLine.getCompanyCode());


            // VAR_ID, VAR_SUB_ID, STR_MTD, STR_NO ---> Hard coded as '1'
            inventory.setVariantCode(1L);
            inventory.setVariantSubCode("1");
            inventory.setStorageMethod("1");
            inventory.setBatchSerialNumber("1");
            inventory.setBatchSerialNumber(createdGRLine.getBatchSerialNumber());
            inventory.setBinClassId(3L);

            // ST_BIN ---Pass WH_ID/BIN_CL_ID=3 in STORAGEBIN table and fetch ST_BIN value and update
            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            StorageBin storageBin = mastersService.getStorageBin(createdGRLine.getWarehouseId(), 3L, authTokenForMastersService.getAccess_token());
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
            inventory.setInventoryQuantity(createdGRLine.getGoodReceiptQty());

            //packbarcode
            inventory.setPackBarcodes(createdGRLine.getPackBarcodes());

            // INV_UOM
            inventory.setInventoryUom(createdGRLine.getOrderUom());
            inventory.setCreatedBy(createdGRLine.getCreatedBy());
            inventory.setCreatedOn(createdGRLine.getCreatedOn());

            //V2 Code (remaining all fields copied already using beanUtils.copyProperties)
            if (createdGRLine.getInventoryQuantity() == null) {
                createdGRLine.setInventoryQuantity(0D);
            }
            if (createdGRLine.getCbm() == null) {
                createdGRLine.setCbm(0D);
            }
            inventory.setCbmPerQuantity(String.valueOf(createdGRLine.getCbm() / createdGRLine.getInventoryQuantity()));

            InventoryV2 createdinventory = inventoryV2Repository.save(inventory);
            log.info("created inventory : " + createdinventory);
            return createdinventory;
//        } else {
//
//            dbInventory.setBatchSerialNumber(createdGRLine.getBatchSerialNumber());
//
//            if ( dbInventory.getInventoryQuantity() == null ) {
//                dbInventory.setInventoryQuantity(0D);
//            }
//            if(createdGRLine.getGoodReceiptQty() == null) {
//                createdGRLine.setGoodReceiptQty(0D);
//            }
//
//            dbInventory.setInventoryQuantity(dbInventory.getInventoryQuantity() + createdGRLine.getGoodReceiptQty());
//            // INV_QTY
////            dbInventory.setInventoryQuantity(createdGRLine.getGoodReceiptQty());
//
//            // INV_UOM
//            dbInventory.setInventoryUom(createdGRLine.getOrderUom());
//            dbInventory.setCreatedBy(createdGRLine.getCreatedBy());
//            dbInventory.setCreatedOn(createdGRLine.getCreatedOn());
//
//            //V2 Code (remaining all fields copied already using beanUtils.copyProperties)
//            if (createdGRLine.getInventoryQuantity() == null) {
//                createdGRLine.setInventoryQuantity(0D);
//            }
//            if (createdGRLine.getCbm() == null) {
//                createdGRLine.setCbm(0D);
//            }
//            dbInventory.setCbmPerQuantity(String.valueOf(createdGRLine.getCbm() / createdGRLine.getInventoryQuantity()));
//
//            InventoryV2 updatedInventory = inventoryV2Repository.save(dbInventory);
//            log.info("updateted inventory : " + updatedInventory);
//            return updatedInventory;
//        }
    }

    /**
     * update GrLineV2
     *
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param goodsReceiptNo
     * @param palletCode
     * @param caseCode
     * @param packBarcodes
     * @param lineNo
     * @param itemCode
     * @param loginUserID
     * @param updateGrLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public GrLineV2 updateGrLine(String companyCodeId, String plantId, String languageId,
                                 String warehouseId, String preInboundNo, String refDocNumber,
                                 String goodsReceiptNo, String palletCode, String caseCode,
                                 String packBarcodes, Long lineNo, String itemCode,
                                 String loginUserID, GrLineV2 updateGrLine)
            throws IllegalAccessException, InvocationTargetException {
        GrLineV2 dbGrLine = getGrLine(warehouseId, preInboundNo, refDocNumber,
                goodsReceiptNo, palletCode, caseCode,
                packBarcodes, lineNo, itemCode,
                companyCodeId,
                plantId,
                languageId);
        BeanUtils.copyProperties(updateGrLine, dbGrLine, CommonUtils.getNullPropertyNames(updateGrLine));
        dbGrLine.setUpdatedBy(loginUserID);
        dbGrLine.setUpdatedOn(new Date());
        GrLineV2 updatedGrLine = grLineV2Repository.save(dbGrLine);
        return updatedGrLine;
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<GrLineV2> grLines = getGrLines();
        grLines.forEach(g -> g.setReferenceField1(asnNumber));
        grLineV2Repository.saveAll(grLines);
    }

    /**
     * deleteGrLine
     *
     * @param loginUserID
     * @param itemCode
     */
    public void deleteGrLine(String companyCodeId, String plantId, String languageId,
                             String warehouseId, String preInboundNo, String refDocNumber,
                             String goodsReceiptNo, String palletCode, String caseCode,
                             String packBarcodes, Long lineNo, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        GrLineV2 grLine = getGrLine(warehouseId, preInboundNo, refDocNumber,
                goodsReceiptNo, palletCode, caseCode,
                packBarcodes, lineNo, itemCode,
                companyCodeId, plantId, languageId);
        if (grLine != null) {
            grLine.setDeletionIndicator(1L);
            grLine.setUpdatedBy(loginUserID);
            grLineV2Repository.save(grLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",packBarcodes: " + packBarcodes +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",goodsReceiptNo: " + goodsReceiptNo +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }
    }
}
