package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.BomHeader;
import com.tekclover.wms.api.transaction.model.dto.BomLine;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData1;
import com.tekclover.wms.api.transaction.model.dto.Warehouse;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.AddPreInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundLineEntity;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.UpdatePreInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineEntityV2;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PreInboundLineService extends BaseService {

    @Autowired
    private PreInboundLineRepository preInboundLineRepository;

    @Autowired
    private InboundLineRepository inboundLineRepository;

    @Autowired
    private MastersService mastersService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private PreInboundLineV2Repository preInboundLineV2Repository;

    @Autowired
    private InboundLineV2Repository inboundLineV2Repository;

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    /**
     * getPreInboundLines
     * @return
     */
    public List<PreInboundLineEntity> getPreInboundLines () {
        List<PreInboundLineEntity> preInboundLineList =  preInboundLineRepository.findAll();
        preInboundLineList = preInboundLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preInboundLineList;
    }

    /**
     *
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @return
     */
    public PreInboundLineEntity getPreInboundLine (String preInboundNo, String warehouseId,
                                                   String refDocNumber, Long lineNo, String itemCode) {
        Optional<PreInboundLineEntity> preInboundLine =
                preInboundLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        lineNo,
                        itemCode,
                        0L);
        if (preInboundLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",lineNo: " + lineNo +
                    ",temCode: " + itemCode + " doesn't exist.");
        }
        return preInboundLine.get();
    }

    /**
     *
     * @param preInboundNo
     * @return
     */
    public List<PreInboundLineEntity> getPreInboundLine(String preInboundNo) {
        List<PreInboundLineEntity> preInboundLines =
                preInboundLineRepository.findByPreInboundNoAndDeletionIndicator(preInboundNo, 0L);
        if (preInboundLines.isEmpty()) {
            throw new BadRequestException("The given values: preInboundNo:" + preInboundNo +
                    " doesn't exist.");
        }
        preInboundLines.forEach(preInboundLine->{
            var quantity = inboundLineRepository.getQuantityByRefDocNoAndPreInboundNoAndLineNoAndItemCode(
                    preInboundLine.getItemCode(), preInboundLine.getRefDocNumber(), preInboundLine.getPreInboundNo(),
                    preInboundLine.getLineNo(), preInboundLine.getWarehouseId());
            preInboundLine.setReferenceField5(quantity != null ? quantity.toString() : null);
        });
        return preInboundLines;
    }

//    /**
//     *
//     * @param preInboundNo
//     * @return
//     */
//    public List<PreInboundLineEntityV2> getPreInboundLineV2(String preInboundNo) {
//        List<PreInboundLineEntityV2> preInboundLines =
//                preInboundLineV2Repository.findByPreInboundNoAndDeletionIndicator(preInboundNo, 0L);
//        if (preInboundLines.isEmpty()) {
//            throw new BadRequestException("The given values: preInboundNo:" + preInboundNo +
//                    " doesn't exist.");
//        }
//        preInboundLines.forEach(preInboundLine->{
//            var quantity = inboundLineRepository.getQuantityByRefDocNoAndPreInboundNoAndLineNoAndItemCode(
//                    preInboundLine.getItemCode(), preInboundLine.getRefDocNumber(), preInboundLine.getPreInboundNo(),
//                    preInboundLine.getLineNo(), preInboundLine.getWarehouseId());
//            preInboundLine.setReferenceField5(quantity != null ? quantity.toString() : null);
//        });
//        return preInboundLines;
//    }

    /**
     * createPreInboundLine
     * @param newPreInboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreInboundLineEntity createPreInboundLine (AddPreInboundLine newPreInboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        PreInboundLineEntity dbPreInboundLine = new PreInboundLineEntity();
        log.info("newPreInboundLine : " + newPreInboundLine);
        BeanUtils.copyProperties(newPreInboundLine, dbPreInboundLine, CommonUtils.getNullPropertyNames(newPreInboundLine));
        dbPreInboundLine.setDeletionIndicator(0L);
        dbPreInboundLine.setCreatedBy(loginUserID);
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setCreatedOn(new Date());
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineRepository.save(dbPreInboundLine);
    }

    /**
     * createPreInboundLine
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param itemCode
     * @param lineNo
     * @param loginUserID
     * @return
     */
    public List<PreInboundLineEntity> createPreInboundLine (String preInboundNo, String warehouseId, String refDocNumber,
                                                            String itemCode, Long lineNo, String loginUserID) {
        try {
            // Query PreInboundLine
            PreInboundLineEntity preInboundLineEntity = getPreInboundLine (preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);

            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            BomHeader bomHeader = mastersService.getBomHeader(itemCode, warehouseId, authTokenForMastersService.getAccess_token());
            if (bomHeader != null) {
                BomLine[] bomLine =
                        mastersService.getBomLine(bomHeader.getBomNumber(), bomHeader.getWarehouseId(), authTokenForMastersService.getAccess_token());
                List<PreInboundLineEntity> toBeCreatedPreInboundLineList = new ArrayList<>();
                for (BomLine dbBomLine : bomLine) {
                    toBeCreatedPreInboundLineList.add(createPreInboundLineBOMBased (dbBomLine.getCompanyCode(),
                            dbBomLine.getPlantId(), dbBomLine.getLanguageId(), preInboundNo, refDocNumber, warehouseId, lineNo, itemCode, dbBomLine,
                            preInboundLineEntity, loginUserID));
                }

                // Batch Insert - PreInboundLines
                if (!toBeCreatedPreInboundLineList.isEmpty()) {
                    List<PreInboundLineEntity> createdPreInboundLineList = preInboundLineRepository.saveAll(toBeCreatedPreInboundLineList);
                    log.info("createdPreInboundLineList [BOM] : " + createdPreInboundLineList);

                    List<InboundLine> toBeCreatedInboundLineList = new ArrayList<>();
                    for (PreInboundLineEntity createdPreInboundLine : createdPreInboundLineList) {
                        log.info("PreInboundLine---------> : " + createdPreInboundLine);
                        InboundLine inboundLine = new InboundLine();
                        BeanUtils.copyProperties(createdPreInboundLine, inboundLine, CommonUtils.getNullPropertyNames(createdPreInboundLine));

                        // OrdQty
                        inboundLine.setOrderedQuantity(createdPreInboundLine.getOrderQty());

                        // OrdUOM
                        inboundLine.setOrderedUnitOfMeasure(createdPreInboundLine.getOrderUom());

                        // IB_order_type_id
                        inboundLine.setInboundOrderTypeId(createdPreInboundLine.getInboundOrderTypeId());

                        // StatusId
                        inboundLine.setStatusId(createdPreInboundLine.getStatusId());

                        // MFR_PART
                        inboundLine.setManufacturerPartNo(createdPreInboundLine.getManufacturerPartNo());
                        inboundLine.setDescription(createdPreInboundLine.getItemDescription());
                        inboundLine.setVendorCode(createdPreInboundLine.getBusinessPartnerCode());
                        inboundLine.setDeletionIndicator(0L);
                        inboundLine.setCreatedBy(createdPreInboundLine.getCreatedBy());
                        inboundLine.setCreatedOn(createdPreInboundLine.getCreatedOn());
                        toBeCreatedInboundLineList.add(inboundLine);
                    }

                    List<InboundLine> createdInboundLine = inboundLineRepository.saveAll(toBeCreatedInboundLineList);
                    log.info("createdInboundLine : " + createdInboundLine);
                    return createdPreInboundLineList;
                }
            } else {
                throw new BadRequestException("There is No Bom for this item.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.toString());
        }
        return null;
    }

    /**
     *
     * @param companyCode
     * @param plantID
     * @param languageId
     * @param preInboundNo
     * @param refDocNumber
     * @param warehouseId
     * @param lineNo
     * @param itemCode
     * @param bomLine
     * @param preInboundLineEntity
     * @param loginUserID
     * @return
     */
    private PreInboundLineEntity createPreInboundLineBOMBased(String companyCode, String plantID, String languageId, String preInboundNo, String refDocNumber,
                                                              String warehouseId, Long lineNo, String itemCode, BomLine bomLine, PreInboundLineEntity preInboundLineEntity, String loginUserID) {
        Warehouse warehouse = getWarehouse(warehouseId, companyCode, plantID, languageId);

        PreInboundLineEntity preInboundLine = new PreInboundLineEntity();
        BeanUtils.copyProperties(preInboundLineEntity, preInboundLine, CommonUtils.getNullPropertyNames(preInboundLineEntity));

        preInboundLine.setLanguageId(warehouse.getLanguageId());
        preInboundLine.setWarehouseId(warehouseId);
        preInboundLine.setCompanyCode(companyCode);
        preInboundLine.setPlantId(plantID);
        preInboundLine.setRefDocNumber(refDocNumber);
        preInboundLine.setInboundOrderTypeId(preInboundLineEntity.getInboundOrderTypeId());

        // PRE_IB_NO
        preInboundLine.setPreInboundNo(preInboundNo);

        // IB__LINE_NO
        preInboundLine.setLineNo(lineNo);

        // ITM_CODE
        preInboundLine.setItemCode(bomLine.getChildItemCode());

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(itemCode, warehouseId, authTokenForMastersService.getAccess_token());
        preInboundLine.setItemDescription(imBasicData1.getDescription());

        // MFR
        preInboundLine.setManufacturerPartNo(imBasicData1.getManufacturerPartNo());

        // ORD_QTY
        double orderQuantity = preInboundLineEntity.getOrderQty() * bomLine.getChildItemQuantity();
        preInboundLine.setOrderQty(orderQuantity);

        // ORD_UOM
        preInboundLine.setOrderUom(preInboundLineEntity.getOrderUom());

        // EA_DATEpreInboundLineEntity
        preInboundLine.setExpectedArrivalDate(preInboundLineEntity.getExpectedArrivalDate());

        // STCK_TYP_ID
        preInboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preInboundLine.setSpecialStockIndicatorId(1L);

        // STATUS_ID
        preInboundLine.setStatusId(6L);

        // REF_FIELD_1
        preInboundLine.setReferenceField1("BOM");

        preInboundLine.setDeletionIndicator(0L);
        preInboundLine.setCreatedBy(loginUserID);
        preInboundLine.setCreatedOn(new Date());
        return preInboundLine;
    }

    /**
     *
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param updatePreInboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreInboundLineEntity updatePreInboundLine (String preInboundNo, String warehouseId,
                                                      String refDocNumber, Long lineNo, String itemCode, UpdatePreInboundLine updatePreInboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreInboundLineEntity dbPreInboundLine = getPreInboundLine(preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        BeanUtils.copyProperties(updatePreInboundLine, dbPreInboundLine, CommonUtils.getNullPropertyNames(updatePreInboundLine));
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineRepository.save(dbPreInboundLine);
    }

    /**
     *
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param statusId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreInboundLineEntity updatePreInboundLine (String preInboundNo, String warehouseId,
                                                      String refDocNumber, Long lineNo, String itemCode, Long statusId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreInboundLineEntity dbPreInboundLine = getPreInboundLine(preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        dbPreInboundLine.setStatusId(statusId);
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineRepository.save(dbPreInboundLine);
    }

    /**
     *
     * @param asnNumber
     */
    public void updateASN (String asnNumber) {
        List<PreInboundLineEntity> preInboundLines = getPreInboundLines();
        preInboundLines.forEach(preiblines -> preiblines.setReferenceField1(asnNumber));
        preInboundLineRepository.saveAll(preInboundLines);
    }

    /**
     * deletePreInboundLine
     * @param loginUserID
     * @param lineNo
     */
    public void deletePreInboundLine (String preInboundNo, String warehouseId,
                                      String refDocNumber, Long lineNo, String itemCode,  String loginUserID) {
        PreInboundLineEntity preInboundLine = getPreInboundLine(preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        if ( preInboundLine != null && preInboundLine.getStatusId() == 6L) {
            preInboundLine.setDeletionIndicator(1L);
            preInboundLine.setUpdatedBy(loginUserID);
            preInboundLine.setUpdatedOn(new Date());
            preInboundLineRepository.save(preInboundLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + preInboundNo);
        }
    }

    //=============================================================V2==================================================================//

    /**
     * @return
     */
    public List<PreInboundLineEntityV2> getPreInboundLinesV2() {
        List<PreInboundLineEntityV2> preInboundLineList = preInboundLineV2Repository.findAll();
        preInboundLineList = preInboundLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preInboundLineList;
    }

    /**
     * @param preInboundNo
     * @return
     */
    public List<PreInboundLineEntityV2> getPreInboundLineV2(String preInboundNo) {
        List<PreInboundLineEntityV2> preInboundLines =
                preInboundLineV2Repository.findByPreInboundNoAndDeletionIndicator(preInboundNo, 0L);
        if (preInboundLines.isEmpty()) {
            throw new BadRequestException("The given values: preInboundNo:" + preInboundNo +
                    " doesn't exist.");
        }
        preInboundLines.forEach(preInboundLine -> {
            var quantity = inboundLineRepository.getQuantityByRefDocNoAndPreInboundNoAndLineNoAndItemCode(
                    preInboundLine.getItemCode(), preInboundLine.getRefDocNumber(), preInboundLine.getPreInboundNo(),
                    preInboundLine.getLineNo(), preInboundLine.getWarehouseId());
            preInboundLine.setReferenceField5(quantity != null ? quantity.toString() : null);
        });
        return preInboundLines;
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @return
     */
    public PreInboundLineEntityV2 getPreInboundLineV2(String companyCode, String plantId, String languageId,
                                                      String preInboundNo, String warehouseId, String refDocNumber,
                                                      Long lineNo, String itemCode) {
        Optional<PreInboundLineEntityV2> preInboundLine =
                preInboundLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndLineNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        lineNo,
                        itemCode,
                        0L);
        if (preInboundLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",lineNo: " + lineNo +
                    ",temCode: " + itemCode + " doesn't exist.");
        }
        return preInboundLine.get();
    }

    public PreInboundLineEntityV2 createPreInboundLineV2(PreInboundLineV2 newPreInboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        PreInboundLineEntityV2 dbPreInboundLine = new PreInboundLineEntityV2();
        log.info("newPreInboundLine : " + newPreInboundLine);
        BeanUtils.copyProperties(newPreInboundLine, dbPreInboundLine, CommonUtils.getNullPropertyNames(newPreInboundLine));

        IKeyValuePair description = stagingLineV2Repository.getDescription(newPreInboundLine.getCompanyCode(),
                newPreInboundLine.getLanguageId(),
                newPreInboundLine.getPlantId(),
                newPreInboundLine.getWarehouseId());

        dbPreInboundLine.setCompanyDescription(description.getCompanyDesc());
        dbPreInboundLine.setPlantDescription(description.getPlantDesc());
        dbPreInboundLine.setWarehouseDescription(description.getWarehouseDesc());

        dbPreInboundLine.setDeletionIndicator(0L);
        dbPreInboundLine.setCreatedBy(loginUserID);
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setCreatedOn(new Date());
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineV2Repository.save(dbPreInboundLine);
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param updatePreInboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreInboundLineEntityV2 updatePreInboundLineEntityV2(String companyCode, String plantId, String languageId,
                                                               String preInboundNo, String warehouseId, String refDocNumber,
                                                               Long lineNo, String itemCode, PreInboundLineV2 updatePreInboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreInboundLineEntityV2 dbPreInboundLine = getPreInboundLineV2(companyCode, plantId, languageId, preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        BeanUtils.copyProperties(updatePreInboundLine, dbPreInboundLine, CommonUtils.getNullPropertyNames(updatePreInboundLine));
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineV2Repository.save(dbPreInboundLine);
    }

    public PreInboundLineEntityV2 updatePreInboundLineV2(String companyCode, String plantId, String languageId,
                                                         String preInboundNo, String warehouseId, String refDocNumber,
                                                         Long lineNo, String itemCode, Long statusId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreInboundLineEntityV2 dbPreInboundLine = getPreInboundLineV2(companyCode, plantId, languageId, preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        dbPreInboundLine.setStatusId(statusId);
        if (statusId != null) {
            String statusDescription = stagingLineV2Repository.getStatusDescription(statusId, languageId);
            dbPreInboundLine.setStatusDescription(statusDescription);
        }
        dbPreInboundLine.setUpdatedBy(loginUserID);
        dbPreInboundLine.setUpdatedOn(new Date());
        return preInboundLineV2Repository.save(dbPreInboundLine);
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param loginUserID
     */
    public void deletePreInboundLineV2(String companyCode, String plantId, String languageId,
                                       String preInboundNo, String warehouseId, String refDocNumber,
                                       Long lineNo, String itemCode, String loginUserID) {
        PreInboundLineEntityV2 preInboundLine = getPreInboundLineV2(companyCode, plantId, languageId, preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);
        if (preInboundLine != null && preInboundLine.getStatusId() == 6L) {
            preInboundLine.setDeletionIndicator(1L);
            preInboundLine.setUpdatedBy(loginUserID);
            preInboundLine.setUpdatedOn(new Date());
            preInboundLineV2Repository.save(preInboundLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + preInboundNo);
        }
    }

    /**
     * @param asnNumber
     */
    public void updateASNV2(String asnNumber) {
        List<PreInboundLineEntityV2> preInboundLines = getPreInboundLinesV2();
        preInboundLines.stream().forEach(preiblines -> preiblines.setReferenceField1(asnNumber));
        preInboundLineV2Repository.saveAll(preInboundLines);
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param warehouseId
     * @param refDocNumber
     * @param itemCode
     * @param lineNo
     * @param loginUserID
     * @return
     */
    public List<PreInboundLineEntityV2> createPreInboundLineV2(String companyCode, String plantId, String languageId,
                                                               String preInboundNo, String warehouseId, String refDocNumber,
                                                               String itemCode, Long lineNo, String loginUserID) {
        try {
            // Query PreInboundLine
            PreInboundLineEntityV2 preInboundLineEntity = getPreInboundLineV2(companyCode, plantId, languageId, preInboundNo, warehouseId, refDocNumber, lineNo, itemCode);

            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            BomHeader bomHeader = mastersService.getBomHeader(itemCode, warehouseId, authTokenForMastersService.getAccess_token());
            if (bomHeader != null) {
                BomLine[] bomLine =
                        mastersService.getBomLine(bomHeader.getBomNumber(), bomHeader.getWarehouseId(), authTokenForMastersService.getAccess_token());
                List<PreInboundLineEntityV2> toBeCreatedPreInboundLineList = new ArrayList<>();
                for (BomLine dbBomLine : bomLine) {
                    toBeCreatedPreInboundLineList.add(createPreInboundLineBOMBasedV2(dbBomLine.getCompanyCode(), dbBomLine.getPlantId(),
                            dbBomLine.getLanguageId(), preInboundNo, refDocNumber,
                            warehouseId, lineNo, itemCode, dbBomLine,
                            preInboundLineEntity, loginUserID));
                }

                // Batch Insert - PreInboundLines
                if (!toBeCreatedPreInboundLineList.isEmpty()) {
                    List<PreInboundLineEntityV2> createdPreInboundLineList = preInboundLineV2Repository.saveAll(toBeCreatedPreInboundLineList);
                    log.info("createdPreInboundLineList [BOM] : " + createdPreInboundLineList);

                    List<InboundLine> toBeCreatedInboundLineList = new ArrayList<>();
                    for (PreInboundLineEntityV2 createdPreInboundLine : createdPreInboundLineList) {
                        log.info("PreInboundLine---------> : " + createdPreInboundLine);
                        InboundLine inboundLine = new InboundLine();
                        BeanUtils.copyProperties(createdPreInboundLine, inboundLine, CommonUtils.getNullPropertyNames(createdPreInboundLine));

                        // OrdQty
                        inboundLine.setOrderedQuantity(createdPreInboundLine.getOrderQty());

                        // OrdUOM
                        inboundLine.setOrderedUnitOfMeasure(createdPreInboundLine.getOrderUom());

                        // IB_order_type_id
                        inboundLine.setInboundOrderTypeId(createdPreInboundLine.getInboundOrderTypeId());

                        // StatusId
                        inboundLine.setStatusId(createdPreInboundLine.getStatusId());

                        // MFR_PART
                        inboundLine.setManufacturerPartNo(createdPreInboundLine.getManufacturerPartNo());
                        inboundLine.setDescription(createdPreInboundLine.getItemDescription());
                        inboundLine.setVendorCode(createdPreInboundLine.getBusinessPartnerCode());
                        inboundLine.setDeletionIndicator(0L);
                        inboundLine.setCreatedBy(createdPreInboundLine.getCreatedBy());
                        inboundLine.setCreatedOn(createdPreInboundLine.getCreatedOn());

//                        IKeyValuePair description = stagingLineV2Repository.getDescription(createdPreInboundLine.getCompanyCode(),
//                                createdPreInboundLine.getLanguageId(),
//                                createdPreInboundLine.getPlantId(),
//                                createdPreInboundLine.getWarehouseId());
//
//                        inboundLine.setCompanyDescription(description.getCompanyDesc());
//                        inboundLine.setPlantDescription(description.getPlantDesc());
//                        inboundLine.setWarehouseDescription(description.getWarehouseDesc());

                        toBeCreatedInboundLineList.add(inboundLine);
                    }

                    List<InboundLine> createdInboundLine = inboundLineRepository.saveAll(toBeCreatedInboundLineList);
                    log.info("createdInboundLine : " + createdInboundLine);
                    return createdPreInboundLineList;
                }
            } else {
                throw new BadRequestException("There is No Bom for this item.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.toString());
        }
        return null;
    }

    /**
     * @param companyCode
     * @param plantID
     * @param languageId
     * @param preInboundNo
     * @param refDocNumber
     * @param warehouseId
     * @param lineNo
     * @param itemCode
     * @param bomLine
     * @param preInboundLineEntity
     * @param loginUserID
     * @return
     */
    private PreInboundLineEntityV2 createPreInboundLineBOMBasedV2(String companyCode, String plantID, String languageId,
                                                                  String preInboundNo, String refDocNumber, String warehouseId,
                                                                  Long lineNo, String itemCode, BomLine bomLine,
                                                                  PreInboundLineEntityV2 preInboundLineEntity, String loginUserID) {
        Warehouse warehouse = getWarehouse(warehouseId, companyCode, plantID, languageId);

        PreInboundLineEntityV2 preInboundLine = new PreInboundLineEntityV2();
        BeanUtils.copyProperties(preInboundLineEntity, preInboundLine, CommonUtils.getNullPropertyNames(preInboundLineEntity));

        preInboundLine.setLanguageId(warehouse.getLanguageId());
        preInboundLine.setWarehouseId(warehouseId);
        preInboundLine.setCompanyCode(companyCode);
        preInboundLine.setPlantId(plantID);
        preInboundLine.setRefDocNumber(refDocNumber);
        preInboundLine.setInboundOrderTypeId(preInboundLineEntity.getInboundOrderTypeId());

        IKeyValuePair description = stagingLineV2Repository.getDescription(preInboundLineEntity.getCompanyCode(),
                preInboundLineEntity.getLanguageId(),
                preInboundLineEntity.getPlantId(),
                preInboundLineEntity.getWarehouseId());

        preInboundLine.setCompanyDescription(description.getCompanyDesc());
        preInboundLine.setPlantDescription(description.getPlantDesc());
        preInboundLine.setWarehouseDescription(description.getWarehouseDesc());

        // PRE_IB_NO
        preInboundLine.setPreInboundNo(preInboundNo);

        // IB__LINE_NO
        preInboundLine.setLineNo(lineNo);

        // ITM_CODE
        preInboundLine.setItemCode(bomLine.getChildItemCode());

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(itemCode, warehouseId, authTokenForMastersService.getAccess_token());
        preInboundLine.setItemDescription(imBasicData1.getDescription());

        // MFR
        preInboundLine.setManufacturerPartNo(imBasicData1.getManufacturerPartNo());

        // ORD_QTY
        double orderQuantity = preInboundLineEntity.getOrderQty() * bomLine.getChildItemQuantity();
        preInboundLine.setOrderQty(orderQuantity);

        // ORD_UOM
        preInboundLine.setOrderUom(preInboundLineEntity.getOrderUom());

        // EA_DATEpreInboundLineEntity
        preInboundLine.setExpectedArrivalDate(preInboundLineEntity.getExpectedArrivalDate());

        // STCK_TYP_ID
        preInboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preInboundLine.setSpecialStockIndicatorId(1L);

        // STATUS_ID
        preInboundLine.setStatusId(6L);
        String statusDescription = stagingLineV2Repository.getStatusDescription(6L, preInboundLine.getLanguageId());
        preInboundLine.setStatusDescription(statusDescription);
        // REF_FIELD_1
        preInboundLine.setReferenceField1("BOM");

        preInboundLine.setDeletionIndicator(0L);
        preInboundLine.setCreatedBy(loginUserID);
        preInboundLine.setCreatedOn(new Date());
        return preInboundLine;
    }
}
