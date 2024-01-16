package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.SearchInboundLineV2;
import com.tekclover.wms.api.transaction.repository.InboundLineV2Repository;
import com.tekclover.wms.api.transaction.repository.InventoryV2Repository;
import com.tekclover.wms.api.transaction.repository.specification.InboundLineV2Specification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.inbound.AddInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.SearchInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.UpdateInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayLine;
import com.tekclover.wms.api.transaction.model.inbound.putaway.UpdatePutAwayLine;
import com.tekclover.wms.api.transaction.repository.InboundLineRepository;
import com.tekclover.wms.api.transaction.repository.specification.InboundLineSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InboundLineService extends BaseService {

    @Autowired
    private InboundLineRepository inboundLineRepository;

    @Autowired
    private PutAwayLineService putAwayLineService;

    @Autowired
    private PutAwayHeaderService putAwayHeaderService;

    @Autowired
    private PreInboundHeaderService preInboundHeaderService;

    @Autowired
    private PreInboundLineService preInboundLineService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    private InboundLineV2Repository inboundLineV2Repository;

    /**
     * getInboundLines
     *
     * @return
     */
    public List<InboundLine> getInboundLines() {
        List<InboundLine> inboundLineList = inboundLineRepository.findAll();
        inboundLineList = inboundLineList
                .stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return inboundLineList;
    }

    /**
     * getInboundLine
     *
     * @param lineNo
     * @return
     */
    public InboundLine getInboundLine(String warehouseId, String refDocNumber, String preInboundNo, Long lineNo, String itemCode) {
        Optional<InboundLine> inboundLine =
                inboundLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        lineNo,
                        itemCode,
                        0L);
        log.info("inboundLine : " + inboundLine);
        if (inboundLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",lineNo: " + lineNo +
                    ",temCode: " + itemCode + " doesn't exist.");
        }

        return inboundLine.get();
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @return
     */
    public List<InboundLine> getInboundLine(String warehouseId, String refDocNumber, String preInboundNo) {
        List<InboundLine> inboundLine =
                inboundLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        0L);
        log.info("inboundLine : " + inboundLine);
        if (inboundLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    " doesn't exist.");
        }

        return inboundLine;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @return
     */
    public List<InboundLine> getInboundLinebyRefDocNoISNULL(String warehouseId, String refDocNumber, String preInboundNo) {
        List<InboundLine> inboundLine =
                inboundLineRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndReferenceField1AndStatusIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        null,
                        20L,
                        0L);
        log.info("inboundLine : " + inboundLine);
        if (inboundLine.isEmpty()) {
            throw new BadRequestException("The given values in getInboundLinebyRefDocNoISNULL: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    " doesn't exist.");
        }

        return inboundLine;
    }

    /**
     * getInboundLine
     *
     * @param refDocNumber
     * @return
     */
    public List<InboundLine> getInboundLine(String refDocNumber) {
        List<InboundLine> inboundLine = inboundLineRepository.findByRefDocNumberAndDeletionIndicator(refDocNumber, 0L);
        log.info("inboundLine : " + inboundLine);
        return inboundLine;
    }

    public List<InboundLine> getInboundLine(String refDocNumber, String warehouseId) {
        List<InboundLine> inboundLine = inboundLineRepository.findByRefDocNumberAndWarehouseIdAndDeletionIndicator(refDocNumber, warehouseId, 0L);
        log.info("inboundLine : " + inboundLine);
        return inboundLine;
    }

    /**
     * @param newInboundLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public InboundLine confirmInboundLine(List<AddInboundLine> newInboundLines, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        for (AddInboundLine addInboundLine : newInboundLines) {
            /*
             * Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE values in INBOUNDLINE tables and
             * Validate STATUS_ID of all the Values are 20
             */
            InboundLine inboundLine =
                    getInboundLine(addInboundLine.getWarehouseId(), addInboundLine.getRefDocNumber(),
                            addInboundLine.getPreInboundNo(), addInboundLine.getLineNo(), addInboundLine.getItemCode());
            if (inboundLine.getStatusId() == 20L) {
                addInboundLine.setStatusId(24L);
                inboundLine = createInboundLine(addInboundLine, loginUserID);
                log.info("inboundLine : " + inboundLine);

                // PUTAWAY table STATUS Updates
                // Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE values in PUTAWAYLINE tables and  update STATUS_ID as 24
                List<PutAwayLine> putAwayLines = putAwayLineService.getPutAwayLine(inboundLine.getWarehouseId(),
                        inboundLine.getPreInboundNo(), inboundLine.getRefDocNumber(), inboundLine.getLineNo(), inboundLine.getItemCode());
                for (PutAwayLine putAwayLine : putAwayLines) {
                    putAwayLine.setStatusId(24L);
                    UpdatePutAwayLine updatePutAwayLine = new UpdatePutAwayLine();
                    BeanUtils.copyProperties(putAwayLine, updatePutAwayLine, CommonUtils.getNullPropertyNames(putAwayLine));
                    putAwayLine = putAwayLineService.updatePutAwayLine(updatePutAwayLine, loginUserID);
                    log.info("putAwayLine---------> " + putAwayLine);
                }

                // Pass WH_ID/PRE_IB_NO/REF_DOC_NO values in PUTAWAYHEADER tables and  update STATUS_ID as 24
                putAwayHeaderService.updatePutAwayHeader(inboundLine.getWarehouseId(), inboundLine.getPreInboundNo(),
                        inboundLine.getRefDocNumber(), 24L, loginUserID);

                // PREINBOUND table updates
                // Pass WH_ID/PRE_IB_NO/REF_DOC_NO values in PREINBOUNDHEADER tables and  update STATUS_ID as 24
                preInboundHeaderService.updatePreInboundHeader(inboundLine.getWarehouseId(), inboundLine.getPreInboundNo(),
                        inboundLine.getRefDocNumber(), 24L, loginUserID);

                // Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE values in PREINBOUNDLINE tables and  update STATUS_ID as 24
                preInboundLineService.updatePreInboundLine(inboundLine.getPreInboundNo(), inboundLine.getWarehouseId(),
                        inboundLine.getRefDocNumber(), inboundLine.getLineNo(), inboundLine.getItemCode(), 24L, loginUserID);

                //V2 Code
                List<InventoryV2> inventoryList = inventoryService.getInventory(inboundLine.getCompanyCode(),
                        inboundLine.getPlantId(),
                        inboundLine.getLanguageId(),
                        inboundLine.getWarehouseId(),
                        inboundLine.getReferenceOrderNo(),
                        inboundLine.getItemCode());
                for (InventoryV2 dbInventory : inventoryList) {
                    dbInventory.setStockTypeId(1L);
                    inventoryV2Repository.save(dbInventory);
                }
            }
        }
        return null;
    }

    /**
     * @param searchInboundLine
     * @return
     * @throws Exception
     */
    public List<InboundLine> findInboundLine(SearchInboundLine searchInboundLine) throws Exception {
        if (searchInboundLine.getStartConfirmedOn() != null && searchInboundLine.getEndConfirmedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchInboundLine.getStartConfirmedOn(),
                    searchInboundLine.getEndConfirmedOn());
            searchInboundLine.setStartConfirmedOn(dates[0]);
            searchInboundLine.setEndConfirmedOn(dates[1]);
        }

        InboundLineSpecification spec = new InboundLineSpecification(searchInboundLine);
        List<InboundLine> results = inboundLineRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    //V2
    public List<InboundLine> findInboundLineV2(SearchInboundLineV2 searchInboundLine) throws Exception {
        if (searchInboundLine.getStartConfirmedOn() != null && searchInboundLine.getEndConfirmedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchInboundLine.getStartConfirmedOn(),
                    searchInboundLine.getEndConfirmedOn());
            searchInboundLine.setStartConfirmedOn(dates[0]);
            searchInboundLine.setEndConfirmedOn(dates[1]);
        }

        InboundLineV2Specification spec = new InboundLineV2Specification(searchInboundLine);
        List<InboundLine> results = inboundLineV2Repository.findAll(spec);
        return results;
    }

    /**
     * createInboundLine
     *
     * @param newInboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public InboundLine createInboundLine(AddInboundLine newInboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        InboundLine dbInboundLine = new InboundLine();
        log.info("newInboundLine : " + newInboundLine);
        BeanUtils.copyProperties(newInboundLine, dbInboundLine, CommonUtils.getNullPropertyNames(newInboundLine));
        dbInboundLine.setDeletionIndicator(0L);
        dbInboundLine.setCreatedBy(loginUserID);
        dbInboundLine.setUpdatedBy(loginUserID);
        dbInboundLine.setCreatedOn(new Date());
        dbInboundLine.setUpdatedOn(new Date());
        return inboundLineRepository.save(dbInboundLine);
    }

    /**
     * updateInboundLine
     *
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param loginUserID
     * @param updateInboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public InboundLine updateInboundLine(String warehouseId, String refDocNumber, String preInboundNo, Long lineNo, String itemCode,
                                         String loginUserID, UpdateInboundLine updateInboundLine)
            throws IllegalAccessException, InvocationTargetException {
        InboundLine dbInboundLine = getInboundLine(warehouseId, refDocNumber, preInboundNo, lineNo, itemCode);
        BeanUtils.copyProperties(updateInboundLine, dbInboundLine, CommonUtils.getNullPropertyNames(updateInboundLine));
        dbInboundLine.setUpdatedBy(loginUserID);
        dbInboundLine.setUpdatedOn(new Date());
        return inboundLineRepository.save(dbInboundLine);
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<InboundLine> inboundLines = getInboundLines();
        inboundLines.forEach(p -> p.setReferenceField1(asnNumber));
        inboundLineRepository.saveAll(inboundLines);
    }

    /**
     * deleteInboundLine
     *
     * @param loginUserID
     * @param lineNo
     */
    public void deleteInboundLine(String warehouseId, String refDocNumber, String preInboundNo, Long lineNo, String itemCode, String loginUserID) {
        InboundLine inboundLine = getInboundLine(warehouseId, refDocNumber, preInboundNo, lineNo, itemCode);
        if (inboundLine != null) {
            inboundLine.setDeletionIndicator(1L);
            inboundLine.setUpdatedBy(loginUserID);
            inboundLineRepository.save(inboundLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNo);
        }
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @return
     */
    public InboundLineV2 getInboundLineV2(String companyCode, String plantId,
                                          String languageId, String warehouseId, String refDocNumber,
                                          String preInboundNo, Long lineNo, String itemCode) {
        Optional<InboundLineV2> inboundLine =
                inboundLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        lineNo,
                        itemCode,
                        0L);
        log.info("inboundLine : " + inboundLine);
        if (inboundLine.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",lineNo: " + lineNo +
                    ",temCode: " + itemCode + " doesn't exist.");
        }

        return inboundLine.get();
    }

    /**
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param loginUserID
     * @param updateInboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public InboundLineV2 updateInboundLineV2(String companyCode, String plantId,
                                             String languageId, String warehouseId,
                                             String refDocNumber, String preInboundNo,
                                             Long lineNo, String itemCode,
                                             String loginUserID, InboundLineV2 updateInboundLine)
            throws IllegalAccessException, InvocationTargetException {
        InboundLineV2 dbInboundLine = getInboundLineV2(companyCode, plantId, languageId, warehouseId, refDocNumber, preInboundNo, lineNo, itemCode);
        BeanUtils.copyProperties(updateInboundLine, dbInboundLine, CommonUtils.getNullPropertyNames(updateInboundLine));
        dbInboundLine.setUpdatedBy(loginUserID);
        dbInboundLine.setUpdatedOn(new Date());
        return inboundLineV2Repository.save(dbInboundLine);
    }
}
