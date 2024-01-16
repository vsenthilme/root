package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.*;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.IInventoryImpl;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.outbound.OutboundHeader;
import com.tekclover.wms.api.transaction.model.outbound.OutboundLine;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.OrderManagementHeader;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.OrderManagementLine;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2.OrderManagementHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.v2.OrderManagementLineV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.AddPickupLine;
import com.tekclover.wms.api.transaction.model.outbound.pickup.PickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupLineV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.*;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.*;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.AddQualityLineV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.QualityHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.v2.QualityLineV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PreOutboundHeaderSpecification;
import com.tekclover.wms.api.transaction.repository.specification.PreOutboundHeaderV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PreOutboundHeaderService extends BaseService {

    @Autowired
    private PreOutboundHeaderRepository preOutboundHeaderRepository;

    @Autowired
    private PreOutboundLineRepository preOutboundLineRepository;

    @Autowired
    private OrderManagementHeaderRepository orderManagementHeaderRepository;

    @Autowired
    private OutboundHeaderRepository outboundHeaderRepository;

    @Autowired
    private OutboundLineRepository outboundLineRepository;

    @Autowired
    private OrderManagementLineRepository orderManagementLineRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OutboundLineService outboundLineService;

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    private OrderManagementHeaderService orderManagementHeaderService;

    @Autowired
    private OrderManagementLineService orderManagementLineService;

    @Autowired
    private OutboundIntegrationLogRepository outboundIntegrationLogRepository;

    @Autowired
    private MastersService mastersService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private OutboundOrderV2Repository outboundOrderV2Repository;

    @Autowired
    private PreOutboundHeaderService preOutboundHeaderService;


    //------------------------------------------------------------------------------------------------------

    @Autowired
    private OrderManagementLineV2Repository orderManagementLineV2Repository;
    @Autowired
    private OutboundHeaderV2Repository outboundHeaderV2Repository;
    @Autowired
    private OrderManagementHeaderV2Repository orderManagementHeaderV2Repository;
    @Autowired
    private OutboundLineV2Repository outboundLineV2Repository;
    @Autowired
    private PreOutboundLineV2Repository preOutboundLineV2Repository;
    @Autowired
    private PreOutboundHeaderV2Repository preOutboundHeaderV2Repository;
    @Autowired
    private PickupHeaderService pickupHeaderService;
    @Autowired
    private PickupLineService pickupLineService;
    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    @Autowired
    private QualityHeaderService qualityHeaderService;

    @Autowired
    private QualityLineService qualityLineService;

    @Autowired
    private PreOutboundLineService preOutboundLineService;

    @Autowired
    private OutboundHeaderService outboundHeaderService;
    String statusDescription = null;

    //------------------------------------------------------------------------------------------------------

    /**
     * getPreOutboundHeaders
     *
     * @return
     */
    public List<PreOutboundHeader> getPreOutboundHeaders() {
        List<PreOutboundHeader> preOutboundHeaderList = preOutboundHeaderRepository.findAll();
        preOutboundHeaderList = preOutboundHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preOutboundHeaderList;
    }

    /**
     * getPreOutboundHeader
     *
     * @param preOutboundNo
     * @return
     */
    public PreOutboundHeader getPreOutboundHeader(String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode) {
        Optional<PreOutboundHeader> preOutboundHeader =
                preOutboundHeaderRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
                        getLanguageId(), getCompanyCode(), getPlantId(), warehouseId, refDocNumber, preOutboundNo, partnerCode, 0L);
        if (!preOutboundHeader.isEmpty()) {
            return preOutboundHeader.get();
        }
        return null;
    }

    /**
     * @param preOutboundNo
     * @return
     */
    public PreOutboundHeader getPreOutboundHeader(String preOutboundNo) {
        PreOutboundHeader preOutboundHeader = preOutboundHeaderRepository.findByPreOutboundNo(preOutboundNo);
        if (preOutboundHeader != null && preOutboundHeader.getDeletionIndicator() == 0) {
            return preOutboundHeader;
        } else {
            throw new BadRequestException("The given PreOutboundHeader ID : " + preOutboundNo + " doesn't exist.");
        }
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @return
     */
    public PreOutboundHeader getPreOutboundHeader(String warehouseId, String refDocNumber) {
        Optional<PreOutboundHeader> preOutboundHeader =
                preOutboundHeaderRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                        getLanguageId(), getCompanyCode(), getPlantId(), warehouseId, refDocNumber, 0L);
        if (!preOutboundHeader.isEmpty()) {
            return preOutboundHeader.get();
        }
        return null;
    }

    /**
     * @param searchPreOutboundHeader
     * @return
     * @throws Exception
     */
    public List<PreOutboundHeader> findPreOutboundHeader(SearchPreOutboundHeader searchPreOutboundHeader)
            throws Exception {

        if (searchPreOutboundHeader.getStartRequiredDeliveryDate() != null && searchPreOutboundHeader.getEndRequiredDeliveryDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartRequiredDeliveryDate(), searchPreOutboundHeader.getEndRequiredDeliveryDate());
            searchPreOutboundHeader.setStartRequiredDeliveryDate(dates[0]);
            searchPreOutboundHeader.setEndRequiredDeliveryDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartOrderDate() != null && searchPreOutboundHeader.getStartOrderDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartOrderDate(), searchPreOutboundHeader.getEndOrderDate());
            searchPreOutboundHeader.setStartOrderDate(dates[0]);
            searchPreOutboundHeader.setEndOrderDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartCreatedOn() != null && searchPreOutboundHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartCreatedOn(), searchPreOutboundHeader.getEndCreatedOn());
            searchPreOutboundHeader.setStartCreatedOn(dates[0]);
            searchPreOutboundHeader.setEndCreatedOn(dates[1]);
        }

        PreOutboundHeaderSpecification spec = new PreOutboundHeaderSpecification(searchPreOutboundHeader);
        List<PreOutboundHeader> results = preOutboundHeaderRepository.findAll(spec);
        return results;
    }

    /**
     * @param searchPreOutboundHeader
     * @return
     * @throws Exception
     */
    public Stream<PreOutboundHeader> findPreOutboundHeaderNew(SearchPreOutboundHeader searchPreOutboundHeader)
            throws Exception {

        if (searchPreOutboundHeader.getStartRequiredDeliveryDate() != null && searchPreOutboundHeader.getEndRequiredDeliveryDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartRequiredDeliveryDate(), searchPreOutboundHeader.getEndRequiredDeliveryDate());
            searchPreOutboundHeader.setStartRequiredDeliveryDate(dates[0]);
            searchPreOutboundHeader.setEndRequiredDeliveryDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartOrderDate() != null && searchPreOutboundHeader.getStartOrderDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartOrderDate(), searchPreOutboundHeader.getEndOrderDate());
            searchPreOutboundHeader.setStartOrderDate(dates[0]);
            searchPreOutboundHeader.setEndOrderDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartCreatedOn() != null && searchPreOutboundHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartCreatedOn(), searchPreOutboundHeader.getEndCreatedOn());
            searchPreOutboundHeader.setStartCreatedOn(dates[0]);
            searchPreOutboundHeader.setEndCreatedOn(dates[1]);
        }

        PreOutboundHeaderSpecification spec = new PreOutboundHeaderSpecification(searchPreOutboundHeader);
        Stream<PreOutboundHeader> preOutboundHeaderList = preOutboundHeaderRepository.stream(spec, PreOutboundHeader.class);
        return preOutboundHeaderList;
    }

    /**
     * createPreOutboundHeader
     *
     * @param newPreOutboundHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundHeader createPreOutboundHeader(AddPreOutboundHeader newPreOutboundHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundHeader dbPreOutboundHeader = new PreOutboundHeader();
        log.info("newPreOutboundHeader : " + newPreOutboundHeader);
        BeanUtils.copyProperties(newPreOutboundHeader, dbPreOutboundHeader, CommonUtils.getNullPropertyNames(newPreOutboundHeader));

        dbPreOutboundHeader.setDeletionIndicator(0L);
        dbPreOutboundHeader.setCreatedBy(loginUserID);
        dbPreOutboundHeader.setUpdatedBy(loginUserID);
        dbPreOutboundHeader.setCreatedOn(new Date());
        dbPreOutboundHeader.setUpdatedOn(new Date());
        return preOutboundHeaderRepository.save(dbPreOutboundHeader);
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param loginUserID
     * @param updatePreOutboundHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundHeader updatePreOutboundHeader(String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode,
                                                     String loginUserID, UpdatePreOutboundHeader updatePreOutboundHeader)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundHeader dbPreOutboundHeader = getPreOutboundHeader(warehouseId, refDocNumber, preOutboundNo, partnerCode);
        BeanUtils.copyProperties(updatePreOutboundHeader, dbPreOutboundHeader, CommonUtils.getNullPropertyNames(updatePreOutboundHeader));
        dbPreOutboundHeader.setUpdatedBy(loginUserID);
        dbPreOutboundHeader.setUpdatedOn(new Date());
        return preOutboundHeaderRepository.save(dbPreOutboundHeader);
    }

    /**
     * deletePreOutboundHeader
     *
     * @param loginUserID
     * @param preOutboundNo
     */
    public void deletePreOutboundHeader(String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, String loginUserID) {
        PreOutboundHeader preOutboundHeader = getPreOutboundHeader(preOutboundNo);
        if (preOutboundHeader != null) {
            preOutboundHeader.setDeletionIndicator(1L);
            preOutboundHeader.setUpdatedBy(loginUserID);
            preOutboundHeaderRepository.save(preOutboundHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + preOutboundNo);
        }
    }

    /*----------------------PROCESS-OUTBOUND-RECEIVED----------------------------------------------*/
    /*
     * Process the PreoutboundIntegraion data
     */
    @Transactional
    public OutboundHeader processOutboundReceived(OutboundIntegrationHeader outboundIntegrationHeader)
            throws IllegalAccessException, InvocationTargetException, BadRequestException, Exception {
        /*
         * Checking whether received refDocNumber processed already.
         */
        Optional<PreOutboundHeader> orderProcessedStatus =
                preOutboundHeaderRepository.findByRefDocNumberAndDeletionIndicator(outboundIntegrationHeader.getRefDocumentNo(), 0);
        if (!orderProcessedStatus.isEmpty()) {
            orderService.updateProcessedOrder(outboundIntegrationHeader.getRefDocumentNo());
            throw new BadRequestException("Order :" + outboundIntegrationHeader.getRefDocumentNo() +
                    " already processed. Reprocessing can't be allowed.");
        }

        String warehouseId = outboundIntegrationHeader.getWarehouseID();
        log.info("warehouseId : " + warehouseId);

        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        Warehouse warehouse = getWarehouse(warehouseId);
        log.info("warehouse : " + warehouse);
        if (warehouse == null) {
            log.info("warehouse not found.");
            throw new BadRequestException("Warehouse cannot be null.");
        }

        // Getting PreOutboundNo from NumberRangeTable
        String preOutboundNo = getPreOutboundNo(warehouseId);
        String refField1ForOrderType = null;

        List<PreOutboundLine> overallCreatedPreoutboundLineList = new ArrayList<>();
        for (OutboundIntegrationLine outboundIntegrationLine : outboundIntegrationHeader.getOutboundIntegrationLine()) {
            log.info("outboundIntegrationLine : " + outboundIntegrationLine);

            /*-------------Insertion of BOM item in preOutboundLine table---------------------------------------------------------*/
            /*
             * Before Insertion into PREOUTBOUNDLINE table , validate the Below
             * Pass the WH_ID/ITM_CODE as PAR_ITM_CODE into BOMHEADER table and validate the records,
             * If record is not Null then fetch BOM_NO Pass BOM_NO in BOMITEM table and fetch CHL_ITM_CODE and
             * CHL_ITM_QTY values and insert along with PAR_ITM_CODE in PREOUTBOUNDLINE table as below
             * Insertion of CHL_ITM_CODE values
             */
            BomHeader bomHeader = mastersService.getBomHeader(outboundIntegrationLine.getItemCode(),
                    warehouseId, authTokenForMastersService.getAccess_token());
            if (bomHeader != null) {
                BomLine[] bomLine = mastersService.getBomLine(bomHeader.getBomNumber(), bomHeader.getWarehouseId(),
                        authTokenForMastersService.getAccess_token());
                List<PreOutboundLine> toBeCreatedpreOutboundLineList = new ArrayList<>();
                for (BomLine dbBomLine : bomLine) {
                    toBeCreatedpreOutboundLineList.add(createPreoutboundLineBOMBased(warehouse.getCompanyCode(),
                            warehouse.getPlantId(), preOutboundNo, outboundIntegrationHeader, dbBomLine, outboundIntegrationLine));
                }

                // Batch Insert - preOutboundLines
                if (!toBeCreatedpreOutboundLineList.isEmpty()) {
                    List<PreOutboundLine> createdpreOutboundLine = preOutboundLineRepository.saveAll(toBeCreatedpreOutboundLineList);
                    log.info("createdpreOutboundLine [BOM] : " + createdpreOutboundLine);
                    overallCreatedPreoutboundLineList.addAll(createdpreOutboundLine);
                }
            }
            refField1ForOrderType = outboundIntegrationLine.getRefField1ForOrderType();
        }

        /*
         * Inserting BOM Line records in OutboundLine and OrderManagementLine
         */
        if (!overallCreatedPreoutboundLineList.isEmpty()) {
            for (PreOutboundLine preOutboundLine : overallCreatedPreoutboundLineList) {
                // OrderManagementLine
                OrderManagementLine orderManagementLine = createOrderManagementLine(warehouse.getCompanyCode(),
                        warehouse.getPlantId(), preOutboundNo, outboundIntegrationHeader, preOutboundLine);
                log.info("orderManagementLine created---BOM---> : " + orderManagementLine);
            }

            /*------------------Record Insertion in OUTBOUNDLINE table--for BOM---------*/
            List<OutboundLine> createOutboundLineListForBOM = createOutboundLine(overallCreatedPreoutboundLineList);
            log.info("createOutboundLine created : " + createOutboundLineListForBOM);
        }

        /*
         * Append PREOUTBOUNDLINE table through below logic
         */
        List<PreOutboundLine> createdPreOutboundLineList = new ArrayList<>();
        for (OutboundIntegrationLine outboundIntegrationLine : outboundIntegrationHeader.getOutboundIntegrationLine()) {
            // PreOutboundLine
            try {
                PreOutboundLine preOutboundLine = createPreOutboundLine(warehouse.getCompanyCode(),
                        warehouse.getPlantId(), preOutboundNo, outboundIntegrationHeader, outboundIntegrationLine);
                PreOutboundLine createdPreOutboundLine = preOutboundLineRepository.save(preOutboundLine);
                log.info("preOutboundLine created---1---> : " + createdPreOutboundLine);
                createdPreOutboundLineList.add(createdPreOutboundLine);

                // OrderManagementLine
                OrderManagementLine orderManagementLine = createOrderManagementLine(warehouse.getCompanyCode(),
                        warehouse.getPlantId(), preOutboundNo, outboundIntegrationHeader, preOutboundLine);
                log.info("orderManagementLine created---1---> : " + orderManagementLine);
            } catch (Exception e) {
                log.error("Error on processing Preoutboudline : " + e.toString());
                e.printStackTrace();
            }
        }

        /*------------------Record Insertion in OUTBOUNDLINE tables-----------*/
        List<OutboundLine> createOutboundLineList = createOutboundLine(createdPreOutboundLineList);
        log.info("createOutboundLine created : " + createOutboundLineList);

        /*------------------Insert into PreOutboundHeader table-----------------------------*/
        PreOutboundHeader createdPreOutboundHeader = createPreOutboundHeader(warehouse.getCompanyCode(),
                warehouse.getPlantId(), preOutboundNo, outboundIntegrationHeader, refField1ForOrderType);
        log.info("preOutboundHeader Created : " + createdPreOutboundHeader);

        /*------------------ORDERMANAGEMENTHEADER TABLE-------------------------------------*/
        OrderManagementHeader createdOrderManagementHeader = createOrderManagementHeader(createdPreOutboundHeader);
        log.info("OrderMangementHeader Created : " + createdOrderManagementHeader);

        /*------------------Record Insertion in OUTBOUNDHEADER/OUTBOUNDLINE tables-----------*/
        OutboundHeader outboundHeader = createOutboundHeader(createdPreOutboundHeader, createdOrderManagementHeader.getStatusId());

        /*------------------------------------------------------------------------------------*/
        updateStatusAs47ForOBHeader(warehouseId, preOutboundNo, outboundHeader.getRefDocNumber());
        return outboundHeader;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     */
    private void updateStatusAs47ForOBHeader(String warehouseId, String preOutboundNo, String refDocNumber) {
        List<OutboundLine> outboundLineList = outboundLineService.getOutboundLine(warehouseId, preOutboundNo, refDocNumber);
        long matchedCount = outboundLineList.stream().filter(a -> a.getStatusId() == 47L).count();
        boolean isConditionMet = (matchedCount == outboundLineList.size());
        Long STATUS_ID_47 = 47L;
        if (isConditionMet) {
            //----------------Outbound Header update----------------------------------------------------------------------------------------
            outboundHeaderRepository.updateOutboundHeaderStatusAs47(warehouseId, refDocNumber, STATUS_ID_47);
            log.info("OutboundHeader status updated as 47. ");

            //----------------Preoutbound Header--------------------------------------------------------------------------------------------
            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            StatusId idStatus = idmasterService.getStatus(STATUS_ID_47, warehouseId, authTokenForIDService.getAccess_token());
            preOutboundHeaderRepository.updatePreOutboundHeaderStatus(warehouseId, refDocNumber, STATUS_ID_47, idStatus.getStatus());
            log.info("PreOutbound Header status updated as 47.");
        }
    }

    /**
     * @param createdPreOutboundLine
     * @return
     */
    private List<OutboundLine> createOutboundLine(List<PreOutboundLine> createdPreOutboundLine) {
        List<OutboundLine> outboundLines = new ArrayList<>();
        for (PreOutboundLine preOutboundLine : createdPreOutboundLine) {
            List<OrderManagementLine> orderManagementLine = orderManagementLineService.getOrderManagementLine(preOutboundLine.getPreOutboundNo(),
                    preOutboundLine.getLineNumber(), preOutboundLine.getItemCode());
            for (OrderManagementLine dbOrderManagementLine : orderManagementLine) {
                OutboundLine outboundLine = new OutboundLine();
                BeanUtils.copyProperties(preOutboundLine, outboundLine, CommonUtils.getNullPropertyNames(preOutboundLine));
                outboundLine.setDeliveryQty(0D);
                outboundLine.setStatusId(dbOrderManagementLine.getStatusId());
                outboundLine.setCreatedBy(preOutboundLine.getCreatedBy());
                outboundLine.setCreatedOn(preOutboundLine.getCreatedOn());
                outboundLines.add(outboundLine);
            }
        }

        outboundLines = outboundLineRepository.saveAll(outboundLines);
        log.info("outboundLines created -----2------>: " + outboundLines);
        return outboundLines;
    }

    /**
     * @param createdPreOutboundHeader
     * @param statusId
     * @return
     * @throws ParseException
     */
    private OutboundHeader createOutboundHeader(PreOutboundHeader createdPreOutboundHeader, Long statusId) throws ParseException {
        OutboundHeader outboundHeader = new OutboundHeader();
        BeanUtils.copyProperties(createdPreOutboundHeader, outboundHeader,
                CommonUtils.getNullPropertyNames(createdPreOutboundHeader));
        OrderManagementHeader dbOrderManagementHeader = orderManagementHeaderService.getOrderManagementHeader(createdPreOutboundHeader.getPreOutboundNo());

        /*
         * Setting up KuwaitTime
         */
        Date kwtDate = DateUtils.getCurrentKWTDateTime();
        outboundHeader.setRefDocDate(kwtDate);
        outboundHeader.setStatusId(dbOrderManagementHeader.getStatusId());
        outboundHeader.setCreatedBy(createdPreOutboundHeader.getCreatedBy());
        outboundHeader.setCreatedOn(createdPreOutboundHeader.getCreatedOn());
        outboundHeader = outboundHeaderRepository.save(outboundHeader);
        log.info("Created outboundHeader : " + outboundHeader);
        return outboundHeader;
    }

    /**
     * @param createdPreOutboundHeader
     * @return
     */
    private OrderManagementHeader createOrderManagementHeader(PreOutboundHeader createdPreOutboundHeader) {
        OrderManagementHeader newOrderManagementHeader = new OrderManagementHeader();
        BeanUtils.copyProperties(createdPreOutboundHeader, newOrderManagementHeader, CommonUtils.getNullPropertyNames(createdPreOutboundHeader));

        AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
        StatusId idStatus = idmasterService.getStatus(41L, createdPreOutboundHeader.getWarehouseId(), authTokenForIDService.getAccess_token());
        newOrderManagementHeader.setStatusId(41L);    // Hard Coded Value "41"
        newOrderManagementHeader.setReferenceField7(idStatus.getStatus());
        OrderManagementHeader createdOrderMangementHeader = orderManagementHeaderRepository.save(newOrderManagementHeader);
        return createdOrderMangementHeader;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param refField1ForOrderType
     * @return
     * @throws ParseException
     */
    private PreOutboundHeader createPreOutboundHeader(String companyCodeId, String plantId, String preOutboundNo,
                                                      OutboundIntegrationHeader outboundIntegrationHeader, String refField1ForOrderType) throws ParseException {
        AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
        PreOutboundHeader preOutboundHeader = new PreOutboundHeader();
        preOutboundHeader.setLanguageId("EN");
        preOutboundHeader.setCompanyCodeId(companyCodeId);
        preOutboundHeader.setPlantId(plantId);
        preOutboundHeader.setWarehouseId(outboundIntegrationHeader.getWarehouseID());
        preOutboundHeader.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());
        preOutboundHeader.setPreOutboundNo(preOutboundNo);                                                // PRE_OB_NO
        preOutboundHeader.setPartnerCode(outboundIntegrationHeader.getPartnerCode());
        preOutboundHeader.setOutboundOrderTypeId(outboundIntegrationHeader.getOutboundOrderTypeID());    // Hardcoded value "0"
        preOutboundHeader.setReferenceDocumentType("SO");                                                // Hardcoded value "SO"

        /*
         * Setting up KuwaitTime
         */
        Date kwtDate = DateUtils.getCurrentKWTDateTime();
        preOutboundHeader.setRefDocDate(kwtDate);
        preOutboundHeader.setStatusId(39L);
        preOutboundHeader.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // REF_FIELD_1
        preOutboundHeader.setReferenceField1(refField1ForOrderType);

        // Status Description
        StatusId idStatus = idmasterService.getStatus(39L, outboundIntegrationHeader.getWarehouseID(), authTokenForIDService.getAccess_token());

        // REF_FIELD_10
        preOutboundHeader.setReferenceField10(idStatus.getStatus());

        preOutboundHeader.setDeletionIndicator(0L);
        preOutboundHeader.setCreatedBy("MSD_INT");
        preOutboundHeader.setCreatedOn(kwtDate);
        PreOutboundHeader createdPreOutboundHeader = preOutboundHeaderRepository.save(preOutboundHeader);
        log.info("createdPreOutboundHeader : " + createdPreOutboundHeader);
        return createdPreOutboundHeader;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param dbBomLine
     * @param outboundIntegrationLine
     * @return
     */
    private PreOutboundLine createPreoutboundLineBOMBased(String companyCodeId, String plantId, String preOutboundNo,
                                                          OutboundIntegrationHeader outboundIntegrationHeader, BomLine dbBomLine,
                                                          OutboundIntegrationLine outboundIntegrationLine) {
        Warehouse warehouse = getWarehouse(outboundIntegrationHeader.getWarehouseID());

        PreOutboundLine preOutboundLine = new PreOutboundLine();
        preOutboundLine.setLanguageId(warehouse.getLanguageId());
        preOutboundLine.setCompanyCodeId(companyCodeId);
        preOutboundLine.setPlantId(plantId);

        // WH_ID
        preOutboundLine.setWarehouseId(outboundIntegrationHeader.getWarehouseID());

        // PRE_IB_NO
        preOutboundLine.setPreOutboundNo(preOutboundNo);

        // REF_DOC_NO
        preOutboundLine.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());

        // OB_ORD_TYP_ID
        preOutboundLine.setOutboundOrderTypeId(Long.valueOf(outboundIntegrationHeader.getOutboundOrderTypeID()));

        // IB__LINE_NO
        preOutboundLine.setLineNumber(outboundIntegrationLine.getLineReference());

        // ITM_CODE
        preOutboundLine.setItemCode(dbBomLine.getChildItemCode());

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        //HAREESH 27-08-2022 - bom line creation get item description based on child item code change
        ImBasicData1 imBasicData1 =
                mastersService.getImBasicData1ByItemCode(dbBomLine.getChildItemCode(),
                        outboundIntegrationHeader.getWarehouseID(), authTokenForMastersService.getAccess_token());
        if (imBasicData1 != null) {
            preOutboundLine.setDescription(imBasicData1.getDescription());
        }

        // PARTNER_CODE
        preOutboundLine.setPartnerCode(outboundIntegrationHeader.getPartnerCode());

        // ORD_QTY
        double orderQuantity = outboundIntegrationLine.getOrderedQty() * dbBomLine.getChildItemQuantity();
        preOutboundLine.setOrderQty(orderQuantity);

        // ORD_UOM
        preOutboundLine.setOrderUom(outboundIntegrationLine.getUom());

        // REQ_DEL_DATE
        preOutboundLine.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // MFR
        preOutboundLine.setManufacturerPartNo(imBasicData1.getManufacturerPartNo());

        // STCK_TYP_ID
        preOutboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preOutboundLine.setSpecialStockIndicatorId(1L);

        // STATUS_ID
        preOutboundLine.setStatusId(39L);

        // REF_FIELD_1
        preOutboundLine.setReferenceField1(outboundIntegrationLine.getRefField1ForOrderType());

        // REF_FIELD_2
        preOutboundLine.setReferenceField2("BOM");

        preOutboundLine.setDeletionIndicator(0L);
        preOutboundLine.setCreatedBy("MSD_INT");
        preOutboundLine.setCreatedOn(new Date());
        return preOutboundLine;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param outboundIntegrationLine
     * @return
     */
    private PreOutboundLine createPreOutboundLine(String companyCodeId, String plantId, String preOutboundNo,
                                                  OutboundIntegrationHeader outboundIntegrationHeader, OutboundIntegrationLine outboundIntegrationLine) {
        PreOutboundLine preOutboundLine = new PreOutboundLine();

        preOutboundLine.setLanguageId("EN");
        preOutboundLine.setCompanyCodeId(companyCodeId);
        preOutboundLine.setPlantId(plantId);

        // WH_ID
        preOutboundLine.setWarehouseId(outboundIntegrationHeader.getWarehouseID());

        // REF DOC Number
        preOutboundLine.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());

        // PRE_IB_NO
        preOutboundLine.setPreOutboundNo(preOutboundNo);

        // PARTNER_CODE
        preOutboundLine.setPartnerCode(outboundIntegrationHeader.getPartnerCode());

        // IB__LINE_NO
        preOutboundLine.setLineNumber(outboundIntegrationLine.getLineReference());

        // ITM_CODE
        preOutboundLine.setItemCode(outboundIntegrationLine.getItemCode());

        // OB_ORD_TYP_ID
        preOutboundLine.setOutboundOrderTypeId(outboundIntegrationHeader.getOutboundOrderTypeID());

        // STATUS_ID
        preOutboundLine.setStatusId(39L);

        // STCK_TYP_ID
        preOutboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preOutboundLine.setSpecialStockIndicatorId(1L);

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData1 imBasicData1 =
                mastersService.getImBasicData1ByItemCode(outboundIntegrationLine.getItemCode(),
                        outboundIntegrationHeader.getWarehouseID(),
                        authTokenForMastersService.getAccess_token());
        log.info("imBasicData1 : " + imBasicData1);
        if (imBasicData1 != null && imBasicData1.getDescription() != null) {
            preOutboundLine.setDescription(imBasicData1.getDescription());
        } else {
            preOutboundLine.setDescription(outboundIntegrationLine.getItemText());
        }

        // ORD_QTY
        preOutboundLine.setOrderQty(outboundIntegrationLine.getOrderedQty());

        // ORD_UOM
        preOutboundLine.setOrderUom(outboundIntegrationLine.getUom());

        // REQ_DEL_DATE
        preOutboundLine.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // REF_FIELD_1
        preOutboundLine.setReferenceField1(outboundIntegrationLine.getRefField1ForOrderType());

        preOutboundLine.setDeletionIndicator(0L);
        preOutboundLine.setCreatedBy("MSD_INT");
        preOutboundLine.setCreatedOn(new Date());

        log.info("preOutboundLine : " + preOutboundLine);
        return preOutboundLine;
    }

    /**
     * ORDERMANAGEMENTLINE TABLE
     *
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param preOutboundLine
     * @return
     */
    private OrderManagementLine createOrderManagementLine(String companyCodeId, String plantId, String preOutboundNo,
                                                          OutboundIntegrationHeader outboundIntegrationHeader, PreOutboundLine preOutboundLine) {
        OrderManagementLine orderManagementLine = new OrderManagementLine();
        BeanUtils.copyProperties(preOutboundLine, orderManagementLine, CommonUtils.getNullPropertyNames(preOutboundLine));

        // INV_QTY
        /*
         * 1. If OB_ORD_TYP_ID = 0,1,3
         * Pass WH_ID/ITM_CODE in INVENTORY table and fetch ST_BIN.
         * Pass ST_BIN into STORAGEBIN table and filter ST_BIN values by ST_SEC_ID values of ZB,ZC,ZG,ZT and
         * PUTAWAY_BLOCK and PICK_BLOCK are false(Null).
         *
         * 2. If OB_ORD_TYP_ID = 2
         * Pass  WH_ID/ITM_CODE in INVENTORY table and fetch ST_BIN.
         * Pass ST_BIN into STORAGEBIN table and filter ST_BIN values by ST_SEC_ID values of ZD and PUTAWAY_BLOCK
         * and PICK_BLOCK are false(Null).
         *
         * Pass the filtered ST_BIN/WH_ID/ITM_CODE/BIN_CL_ID=01/STCK_TYP_ID=1 in Inventory table and fetch Sum(INV_QTY)"
         */
        log.info("orderManagementLine : " + orderManagementLine);

        Long OB_ORD_TYP_ID = outboundIntegrationHeader.getOutboundOrderTypeID();
        if (OB_ORD_TYP_ID == 0L || OB_ORD_TYP_ID == 1L || OB_ORD_TYP_ID == 3L) {
            List<String> storageSectionIds = Arrays.asList("ZB", "ZC", "ZG", "ZT"); //ZB,ZC,ZG,ZT
            orderManagementLine = createOrderManagement(storageSectionIds, orderManagementLine, preOutboundLine.getWarehouseId(),
                    preOutboundLine.getItemCode(), preOutboundLine.getOrderQty());
        }

        if (OB_ORD_TYP_ID == 2L) {
            List<String> storageSectionIds = Arrays.asList("ZD"); //ZD
            orderManagementLine = createOrderManagement(storageSectionIds, orderManagementLine, preOutboundLine.getWarehouseId(),
                    preOutboundLine.getItemCode(), preOutboundLine.getOrderQty());
        }

        // PROP_ST_BIN
        return orderManagementLine;
    }

    /**
     * @param warehouseId
     * @return
     */
    private String getPreOutboundNo(String warehouseId) {
        /*
         * Pass WH_ID - User logged in WH_ID and NUM_RAN_CODE=9  in NUMBERRANGE table and
         * fetch NUM_RAN_CURRENT value of FISCALYEAR=CURRENT YEAR and add +1and then update in PREOUTBOUNDHEADER table
         */
        try {
            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
            return getNextRangeNumber(9, warehouseId, authTokenForIDMasterService.getAccess_token());
        } catch (Exception e) {
            throw new BadRequestException("Error on Number Range");
        }
    }


    /**
     * @param storageSectionIds
     * @param orderManagementLine
     * @param warehouseId
     * @param itemCode
     * @param ORD_QTY
     * @return
     */
    private OrderManagementLine createOrderManagement(List<String> storageSectionIds, OrderManagementLine orderManagementLine,
                                                      String warehouseId, String itemCode, Double ORD_QTY) {
        List<Inventory> stockType1InventoryList = inventoryService.getInventoryForOrderManagement(warehouseId, itemCode, 1L, 1L);
        log.info("---Global---stockType1InventoryList-------> : " + stockType1InventoryList);
        if (stockType1InventoryList.isEmpty()) {
            return createEMPTYOrderManagementLine(orderManagementLine);
        }
        return orderManagementLineService.updateAllocation(orderManagementLine, storageSectionIds, ORD_QTY, warehouseId, itemCode, "ORDER_PLACED");
    }

    /**
     * @param orderManagementLine
     * @return
     */
    private OrderManagementLine createEMPTYOrderManagementLine(OrderManagementLine orderManagementLine) {
        AuthToken idmasterAuthToken = authTokenService.getIDMasterServiceAuthToken();
        StatusId idStatus = idmasterService.getStatus(47L, orderManagementLine.getWarehouseId(), idmasterAuthToken.getAccess_token());

        orderManagementLine.setStatusId(47L);
        orderManagementLine.setReferenceField7(idStatus.getStatus());
        orderManagementLine.setProposedStorageBin("");
        orderManagementLine.setProposedPackBarCode("");
        orderManagementLine.setInventoryQty(0D);
        orderManagementLine.setAllocatedQty(0D);
        orderManagementLine = orderManagementLineRepository.save(orderManagementLine);
        log.info("orderManagementLine created: " + orderManagementLine);
        return orderManagementLine;
    }

    /**
     * @param outbound
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public OutboundIntegrationLog createOutboundIntegrationLog(OutboundIntegrationHeader outbound)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        Warehouse warehouse = getWarehouse(outbound.getWarehouseID());
        OutboundIntegrationLog dbOutboundIntegrationLog = new OutboundIntegrationLog();
        dbOutboundIntegrationLog.setLanguageId("EN");
        dbOutboundIntegrationLog.setCompanyCodeId(warehouse.getCompanyCode());
        dbOutboundIntegrationLog.setPlantId(warehouse.getPlantId());
        dbOutboundIntegrationLog.setWarehouseId(warehouse.getWarehouseId());
        dbOutboundIntegrationLog.setIntegrationLogNumber(outbound.getRefDocumentNo());
        dbOutboundIntegrationLog.setRefDocNumber(outbound.getRefDocumentNo());
        dbOutboundIntegrationLog.setOrderReceiptDate(outbound.getOrderProcessedOn());
        dbOutboundIntegrationLog.setIntegrationStatus("FAILED");
        dbOutboundIntegrationLog.setOrderReceiptDate(outbound.getOrderProcessedOn());
        dbOutboundIntegrationLog.setDeletionIndicator(0L);
        dbOutboundIntegrationLog.setCreatedBy("MSD_API");
        /*
         * Setting up KuwaitTime
         */
        Date kwtDate = DateUtils.getCurrentKWTDateTime();
        dbOutboundIntegrationLog.setCreatedOn(kwtDate);
        dbOutboundIntegrationLog = outboundIntegrationLogRepository.save(dbOutboundIntegrationLog);
        log.info("dbOutboundIntegrationLog : " + dbOutboundIntegrationLog);
        return dbOutboundIntegrationLog;
    }

//	private OrderManagementLine createOrderManagement (List<String> storageSectionIds, OrderManagementLine orderManagementLine,
//	String warehouseId, String itemCode, Double ORD_QTY) {
//List<Inventory> stBinInventoryList = inventoryService.getInventory(warehouseId, itemCode);
//log.info("---Global---stBinInventoryList-------> : " + stBinInventoryList);
//
///*
// * If the Inventory doesn't exists in the Table then inserting 0th record in Ordermanagementline table
// */
//if (stBinInventoryList.isEmpty()) {
//	return createEMPTYOrderManagementLine(orderManagementLine);
//}
//
///* To obtain the SumOfInvQty */
//List<String> stBins = stBinInventoryList.stream().map(Inventory::getStorageBin).collect(Collectors.toList());
//log.info("---Filtered---stBins -----> : " + stBins);
//
//List<Inventory> finalInventoryList = new ArrayList<>();
//AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
//StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
//storageBinPutAway.setStorageBin(stBins);
//storageBinPutAway.setStorageSectionIds(storageSectionIds);
//storageBinPutAway.setWarehouseId(warehouseId);
//
//StorageBin[] storageBin = mastersService.getStorageBin(storageBinPutAway, authTokenForMastersService.getAccess_token());
//log.info("---1----selected----storageBins---from---masters-----> : " + storageBin);
//
//// If the StorageBin returns null, then creating OrderManagementLine table with Zero Alloc_qty and Inv_Qty
//if (storageBin == null) {
//	return createEMPTYOrderManagementLine(orderManagementLine);
//}
//
//if (storageBin != null && storageBin.length > 0) {
//	log.info("----2----selected----storageBins---from---masters-----> : " + Arrays.asList(storageBin));
//	
//	// Pass the filtered ST_BIN/WH_ID/ITM_CODE/BIN_CL_ID=01/STCK_TYP_ID=1 in Inventory table and fetch SUM (INV_QTY)
//	for (StorageBin dbStorageBin : storageBin) {
//		List<Inventory> listInventory = 
//				inventoryService.getInventoryForOrderMgmt (warehouseId, itemCode, 1L, dbStorageBin.getStorageBin(), 1L);
//		log.info("----Selected--Inventory--by--stBin--wise----> : " + listInventory);
//		if (listInventory != null) {
//			finalInventoryList.addAll(listInventory);
//		}
//	}
//}
//log.info("Final inventory list###########---> : " + finalInventoryList);
//
///*
// * If the Inventory doesn't exists in the Table then inserting 0th record in Ordermanamentline table
// */
//if (finalInventoryList.isEmpty()) {
//	return createEMPTYOrderManagementLine(orderManagementLine);
//}
//
//Double tempMaxQty = 0D;
//for (Inventory inventory : finalInventoryList) {
//	if (tempMaxQty < inventory.getInventoryQuantity()) {
//		tempMaxQty = inventory.getInventoryQuantity();
//	}
//}
//
//Inventory maxQtyHoldsInventory = new Inventory();
//for (Inventory inventory : finalInventoryList) {
////	if (inventory.getInventoryQuantity() == tempMaxQty) { // Commenting this ...............
//	if (inventory.getInventoryQuantity() == ORD_QTY) { // This is avoid the If max condition so that always else block will execute
//		BeanUtils.copyProperties(inventory, maxQtyHoldsInventory, CommonUtils.getNullPropertyNames(inventory));
//	}
//}
//log.info("Found ------tempMaxQty-----> : " + tempMaxQty);
//log.info("Found ------tempMaxQty--Inventory---> : " + maxQtyHoldsInventory);
//
///*
// * Sorting the list
// */
//Collections.sort(finalInventoryList, new Comparator<Inventory>() {
//    public int compare(Inventory s1, Inventory s2) {
//        return ((Double)s1.getInventoryQuantity()).compareTo(s2.getInventoryQuantity());
//    }
//});
//
//for (Inventory inventory : finalInventoryList) {
//	log.info("Collections------sort-----> : " + inventory.getInventoryQuantity());
//}
//
//if (maxQtyHoldsInventory.getInventoryQuantity() != null && ORD_QTY < maxQtyHoldsInventory.getInventoryQuantity()) {
//	Long STATUS_ID = 0L;
//	Double ALLOC_QTY = 0D;
//	
//	Double INV_QTY = maxQtyHoldsInventory.getInventoryQuantity();
//	
//	// INV_QTY
//	orderManagementLine.setInventoryQty(INV_QTY);
//	
//	if (ORD_QTY < INV_QTY) {
//		ALLOC_QTY = ORD_QTY;
//	} else if (ORD_QTY > INV_QTY) {
//		ALLOC_QTY = INV_QTY;
//	} else if (INV_QTY == 0) {
//		ALLOC_QTY = 0D;
//	}
//	log.info ("ALLOC_QTY -----@@--->: " + ALLOC_QTY);
//	
//	orderManagementLine.setAllocatedQty(ALLOC_QTY);
//	orderManagementLine.setReAllocatedQty(ALLOC_QTY);
//				
//	// STATUS_ID 
//	/* if ORD_QTY> ALLOC_QTY, then STATUS_ID is hardcoded as "42" */
//	if (ORD_QTY > ALLOC_QTY) {
//		STATUS_ID = 42L;
//	}
//	
//	/* if ORD_QTY=ALLOC_QTY, then STATUS_ID is hardcoded as "43" */
//	if (ORD_QTY == ALLOC_QTY) {
//		STATUS_ID = 43L;
//	}
//	
//	orderManagementLine.setStatusId(STATUS_ID);
//	orderManagementLine.setPickupCreatedBy("MSD_INT");
//	orderManagementLine.setPickupCreatedOn(new Date());
//	orderManagementLine.setProposedStorageBin(maxQtyHoldsInventory.getStorageBin());
//	orderManagementLine.setProposedPackBarCode(maxQtyHoldsInventory.getPackBarcodes());
//	
//	// Ref_Field_9 for storing ST_SEC_ID
//	orderManagementLine.setReferenceField9(maxQtyHoldsInventory.getReferenceField10());
//	
//	// Span ID
//	// Getting StorageBin by WarehouseId
//	StorageBin spanIdStorageBin = mastersService.getStorageBin(orderManagementLine.getProposedStorageBin(), orderManagementLine.getWarehouseId(),
//					authTokenForMastersService.getAccess_token());
//	
//	// Ref_Field_10 for storing SPAN_ID
//	orderManagementLine.setReferenceField10(spanIdStorageBin.getSpanId());
//	
//	OrderManagementLine createdOrderManagementLine = orderManagementLineRepository.save(orderManagementLine);
//	log.info("---1--orderManagementLine created------: " + createdOrderManagementLine);
//	
//	if (orderManagementLine.getAllocatedQty() > 0) {
//		// Update Inventory table
//		Inventory inventoryForUpdate = inventoryService.getInventory(warehouseId, orderManagementLine.getProposedPackBarCode(), 
//				itemCode, orderManagementLine.getProposedStorageBin());
//		log.info("-----inventoryForUpdate------> : " + inventoryForUpdate);
//		if (inventoryForUpdate == null) {
//			throw new BadRequestException("Inventory found as null.");
//		}
//		
//		double dbInventoryQty = 0;
//		double dbInvAllocatedQty = 0;
//		
//		if (inventoryForUpdate.getAllocatedQuantity() != null) {
//			dbInvAllocatedQty = inventoryForUpdate.getAllocatedQuantity();
//		}
//		
//		if (inventoryForUpdate.getInventoryQuantity() != null) {
//			dbInventoryQty = inventoryForUpdate.getInventoryQuantity();
//		}
//		
//		double inventoryQty = dbInventoryQty - orderManagementLine.getAllocatedQty();
//		double allocatedQty = dbInvAllocatedQty + orderManagementLine.getAllocatedQty();
//		inventoryForUpdate.setInventoryQuantity(inventoryQty);
//		inventoryForUpdate.setAllocatedQuantity(allocatedQty);
//		inventoryForUpdate = inventoryRepository.save(inventoryForUpdate);
//		log.info("inventoryForUpdate updated: " + inventoryForUpdate);
//		
//		if (inventoryQty == 0 && allocatedQty == 0) {
//			log.info("inventoryForUpdate inventoryQty became zero." + inventoryQty);
//			inventoryRepository.delete(inventoryForUpdate);
//		}
//	}
//} else { // This is for ORD_QTY > maxQtyHoldsInventory.getInventoryQuantity(), so, creating new ordermanagementline records
//	for (Inventory stBinInventory : finalInventoryList) {
//		OrderManagementLine newOrderManagementLine = new OrderManagementLine();
//		BeanUtils.copyProperties(orderManagementLine, newOrderManagementLine, CommonUtils.getNullPropertyNames(orderManagementLine));
//		
//		Long STATUS_ID = 0L;
//		Double ALLOC_QTY = 0D;
//		
//		log.info("\nfinalListInventory Inventory ---->: " + stBinInventory + "\n");
//		log.info("\nBin-wise Inventory : " + stBinInventory + "\n");
//		
//		/*	
//		 * ALLOC_QTY
//		 * 1. If ORD_QTY< INV_QTY , then ALLOC_QTY = ORD_QTY.
//		 * 2. If ORD_QTY>INV_QTY, then ALLOC_QTY = INV_QTY.
//		 * If INV_QTY = 0, Auto fill ALLOC_QTY=0
//		 */
//		Double INV_QTY = stBinInventory.getInventoryQuantity();
//		
//		// INV_QTY
//		newOrderManagementLine.setInventoryQty(INV_QTY);
//		
//		if (ORD_QTY <= INV_QTY) {
//			ALLOC_QTY = ORD_QTY;
//		} else if (ORD_QTY > INV_QTY) {
//			ALLOC_QTY = INV_QTY;
//		} else if (INV_QTY == 0) {
//			ALLOC_QTY = 0D;
//		}
//		log.info ("ALLOC_QTY -----1--->: " + ALLOC_QTY);
//		
//		newOrderManagementLine.setAllocatedQty(ALLOC_QTY);
//		newOrderManagementLine.setReAllocatedQty(ALLOC_QTY);
//					
//		// STATUS_ID 
//		/* if ORD_QTY> ALLOC_QTY , then STATUS_ID is hardcoded as "42" */
//		if (ORD_QTY > ALLOC_QTY) {
//			STATUS_ID = 42L;
//		}
//		
//		/* if ORD_QTY=ALLOC_QTY,  then STATUS_ID is hardcoded as "43" */
//		if (ORD_QTY == ALLOC_QTY) {
//			STATUS_ID = 43L;
//		}
//		
//		newOrderManagementLine.setStatusId(STATUS_ID);
//		newOrderManagementLine.setPickupCreatedBy("MSD_INT");
//		newOrderManagementLine.setPickupCreatedOn(new Date());
//		if (stBinInventory.getStorageBin() == null) {
//			newOrderManagementLine.setProposedStorageBin(null);
//		} else {
//			newOrderManagementLine.setProposedStorageBin(stBinInventory.getStorageBin());
//		}
//		
//		if (stBinInventory.getPackBarcodes() == null) {
//			newOrderManagementLine.setProposedPackBarCode(null);
//		} else {
//			newOrderManagementLine.setProposedPackBarCode(stBinInventory.getPackBarcodes());
//		}
//		
//		OrderManagementLine createdOrderManagementLine = orderManagementLineRepository.save(newOrderManagementLine);
//		log.info("-----2------orderManagementLine created:-------> " + createdOrderManagementLine);
//		
//		if (ORD_QTY > ALLOC_QTY) {
//			ORD_QTY = ORD_QTY - ALLOC_QTY;
//		}
//		
//		if (createdOrderManagementLine.getAllocatedQty() > 0) {
//			// Update Inventory table
//			Inventory inventoryForUpdate = inventoryService.getInventory(warehouseId, createdOrderManagementLine.getProposedPackBarCode(), 
//					itemCode, createdOrderManagementLine.getProposedStorageBin());
//			
//			double dbInventoryQty = 0;
//			double dbInvAllocatedQty = 0;
//			
//			if (inventoryForUpdate.getInventoryQuantity() != null) {
//				dbInventoryQty = inventoryForUpdate.getInventoryQuantity();
//			}
//			
//			if (inventoryForUpdate.getAllocatedQuantity() != null) {
//				dbInvAllocatedQty = inventoryForUpdate.getAllocatedQuantity();
//			}
//			
//			double inventoryQty = dbInventoryQty - createdOrderManagementLine.getAllocatedQty();
//			double allocatedQty = dbInvAllocatedQty + createdOrderManagementLine.getAllocatedQty();
//			inventoryForUpdate.setInventoryQuantity(inventoryQty);
//			inventoryForUpdate.setAllocatedQuantity(allocatedQty);
//			inventoryForUpdate = inventoryRepository.save(inventoryForUpdate);
//			log.info("inventoryForUpdate updated: " + inventoryForUpdate);
//			
//			if (inventoryQty == 0 && allocatedQty == 0) {
//				log.info("inventoryForUpdate inventoryQty became zero." + inventoryQty);
//				inventoryRepository.delete(inventoryForUpdate);
//			}
//		}
//		
//		if (ORD_QTY == ALLOC_QTY) {
//			log.info("ORD_QTY fully allocated: " + ORD_QTY);
//			break; //If the Inventory satisfied the Ord_qty
//		}
//	}
//}
//
//log.info("orderManagementLine========> : " + orderManagementLine);
//return orderManagementLine;
//}

    //=================================================================V2=======================================================================

    /**
     * getPreOutboundHeaders
     *
     * @return
     */
    public List<PreOutboundHeaderV2> getPreOutboundHeadersV2() {
        List<PreOutboundHeaderV2> preOutboundHeaderList = preOutboundHeaderV2Repository.findAll();
        preOutboundHeaderList = preOutboundHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preOutboundHeaderList;
    }

    /**
     * getPreOutboundHeader
     *
     * @param preOutboundNo
     * @return
     */
    public PreOutboundHeaderV2 getPreOutboundHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode) {
        Optional<PreOutboundHeaderV2> preOutboundHeader =
                preOutboundHeaderV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, preOutboundNo, partnerCode, 0L);
        if (!preOutboundHeader.isEmpty()) {
            return preOutboundHeader.get();
        }
        return null;
    }

    /**
     * @param preOutboundNo
     * @return
     */
    public PreOutboundHeaderV2 getPreOutboundHeaderV2(String preOutboundNo) {
        PreOutboundHeaderV2 preOutboundHeader = preOutboundHeaderV2Repository.findByPreOutboundNoAndDeletionIndicator(preOutboundNo, 0L);
        if (preOutboundHeader != null) {
            return preOutboundHeader;
        } else {
            throw new BadRequestException("The given PreOutboundHeader ID : " + preOutboundNo + " doesn't exist.");
        }
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @return
     */
    public PreOutboundHeaderV2 getPreOutboundHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String refDocNumber) {
        Optional<PreOutboundHeaderV2> preOutboundHeader =
                preOutboundHeaderV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, 0L);
        if (!preOutboundHeader.isEmpty()) {
            return preOutboundHeader.get();
        }
        return null;
    }

    /**
     * @param searchPreOutboundHeader
     * @return
     * @throws Exception
     */
    public Stream<PreOutboundHeaderV2> findPreOutboundHeaderV2(SearchPreOutboundHeaderV2 searchPreOutboundHeader)
            throws Exception {

        if (searchPreOutboundHeader.getStartRequiredDeliveryDate() != null && searchPreOutboundHeader.getEndRequiredDeliveryDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartRequiredDeliveryDate(), searchPreOutboundHeader.getEndRequiredDeliveryDate());
            searchPreOutboundHeader.setStartRequiredDeliveryDate(dates[0]);
            searchPreOutboundHeader.setEndRequiredDeliveryDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartOrderDate() != null && searchPreOutboundHeader.getStartOrderDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartOrderDate(), searchPreOutboundHeader.getEndOrderDate());
            searchPreOutboundHeader.setStartOrderDate(dates[0]);
            searchPreOutboundHeader.setEndOrderDate(dates[1]);
        }

        if (searchPreOutboundHeader.getStartCreatedOn() != null && searchPreOutboundHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreOutboundHeader.getStartCreatedOn(), searchPreOutboundHeader.getEndCreatedOn());
            searchPreOutboundHeader.setStartCreatedOn(dates[0]);
            searchPreOutboundHeader.setEndCreatedOn(dates[1]);
        }

        PreOutboundHeaderV2Specification spec = new PreOutboundHeaderV2Specification(searchPreOutboundHeader);
        Stream<PreOutboundHeaderV2> preOutboundHeaderList = preOutboundHeaderV2Repository.stream(spec, PreOutboundHeaderV2.class);
        return preOutboundHeaderList;
    }

    /**
     * createPreOutboundHeader
     *
     * @param newPreOutboundHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundHeaderV2 createPreOutboundHeaderV2(PreOutboundHeaderV2 newPreOutboundHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        PreOutboundHeaderV2 dbPreOutboundHeader = new PreOutboundHeaderV2();
        log.info("newPreOutboundHeader : " + newPreOutboundHeader);
        BeanUtils.copyProperties(newPreOutboundHeader, dbPreOutboundHeader, CommonUtils.getNullPropertyNames(newPreOutboundHeader));

        dbPreOutboundHeader.setDeletionIndicator(0L);
        dbPreOutboundHeader.setCreatedBy(loginUserID);
        dbPreOutboundHeader.setUpdatedBy(loginUserID);
        dbPreOutboundHeader.setCreatedOn(DateUtils.getCurrentKWTDateTime());
        dbPreOutboundHeader.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
        return preOutboundHeaderV2Repository.save(dbPreOutboundHeader);
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param loginUserID
     * @param updatePreOutboundHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundHeaderV2 updatePreOutboundHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                         String refDocNumber, String preOutboundNo, String partnerCode,
                                                         String loginUserID, PreOutboundHeaderV2 updatePreOutboundHeader)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        PreOutboundHeaderV2 dbPreOutboundHeader = getPreOutboundHeaderV2(companyCodeId, plantId, languageId, warehouseId, refDocNumber, preOutboundNo, partnerCode);
        BeanUtils.copyProperties(updatePreOutboundHeader, dbPreOutboundHeader, CommonUtils.getNullPropertyNames(updatePreOutboundHeader));
        dbPreOutboundHeader.setUpdatedBy(loginUserID);
        dbPreOutboundHeader.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
        return preOutboundHeaderV2Repository.save(dbPreOutboundHeader);
    }

    /**
     * deletePreOutboundHeader
     *
     * @param loginUserID
     * @param preOutboundNo
     */
    public void deletePreOutboundHeaderV2(String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, String loginUserID) {
        PreOutboundHeaderV2 preOutboundHeader = getPreOutboundHeaderV2(companyCodeId, plantId, languageId, warehouseId, refDocNumber, preOutboundNo, partnerCode);
        if (preOutboundHeader != null) {
            preOutboundHeader.setDeletionIndicator(1L);
            preOutboundHeader.setUpdatedBy(loginUserID);
            preOutboundHeaderV2Repository.save(preOutboundHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + preOutboundNo);
        }
    }

    /*----------------------PROCESS-OUTBOUND-RECEIVED----------------------------------------------*/
    /*
     * Process the PreoutboundIntegraion data
     */
    @Transactional
    public OutboundHeaderV2 processOutboundReceivedV2(OutboundIntegrationHeaderV2 outboundIntegrationHeader)
            throws IllegalAccessException, InvocationTargetException, BadRequestException, Exception {
        /*
         * Checking whether received refDocNumber processed already.
         */
        Optional<PreOutboundHeaderV2> orderProcessedStatus =
                preOutboundHeaderV2Repository.findByRefDocNumberAndDeletionIndicator(outboundIntegrationHeader.getRefDocumentNo(), 0L);
        if (!orderProcessedStatus.isEmpty()) {
            orderService.updateProcessedOrderV2(outboundIntegrationHeader.getRefDocumentNo());
            throw new BadRequestException("Order :" + outboundIntegrationHeader.getRefDocumentNo() +
                    " already processed. Reprocessing can't be allowed.");
        }

        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        Optional<com.tekclover.wms.api.transaction.model.warehouse.Warehouse> warehouse =
                null;
        String warehouseId = null;
        String companyCodeId = null;
        String plantId = null;
        String languageId = null;

        try {
            warehouse = warehouseRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
                    outboundIntegrationHeader.getCompanyCode(), outboundIntegrationHeader.getBranchCode(), "EN", 0L);
            log.info("warehouse : " + warehouse.get().getWarehouseId());

            warehouseId = warehouse.get().getWarehouseId();
            companyCodeId = outboundIntegrationHeader.getCompanyCode();
            plantId = outboundIntegrationHeader.getBranchCode();
            languageId = warehouse.get().getLanguageId();
            log.info("warehouseId : " + warehouseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (warehouse == null) {
            log.info("warehouse not found.");
            throw new BadRequestException("Warehouse cannot be null.");
        }

        if (outboundIntegrationHeader.getOutboundOrderTypeID() == 3) {
            String salesOrderNumber = outboundIntegrationHeader.getSalesOrderNumber();
            companyCodeId = outboundIntegrationHeader.getCompanyCode();
            plantId = outboundIntegrationHeader.getBranchCode();
            warehouseId = outboundIntegrationHeader.getWarehouseID();
            languageId = outboundIntegrationHeader.getLanguageId();
            String loginUserID = "MSD_INT";

            //Check WMS order table
            List<OutboundHeaderV2> outbound = outboundHeaderV2Repository.findBySalesOrderNumber(salesOrderNumber);
            String newPickListNo = outboundIntegrationHeader.getPickListNumber();
            List<String> oldPickListNo = outbound.stream().filter(n -> n.getPickListNumber() != newPickListNo).map(OutboundHeaderV2::getPickListNumber).collect(Collectors.toList());

            for (String oldPickListNumber : oldPickListNo) {
                OutboundHeaderV2 outboundOrderV2 =
                        outboundHeaderV2Repository.findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndRefDocNumberAndStatusIdAndDeletionIndicator(
                                companyCodeId, languageId, plantId, warehouseId, oldPickListNumber, 59L, 0L);

                if (outboundOrderV2 != null) {
                    // Update error message for the new PicklistNo
                    throw new BadRequestException("Picklist cannot be processed as Sales order has been already confirmed in WMS");
                }

                //Delete PickListData
                deletePickList(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
            }
        }

        if (outboundIntegrationHeader.getOutboundOrderTypeID() == 4 || outboundIntegrationHeader.getReferenceDocumentType().equalsIgnoreCase("Sales Invoice")) {
            OutboundHeaderV2 updateOutboundHeaderAndLine = outboundHeaderService.updateOutboundHeaderForSalesInvoice(outboundIntegrationHeader, warehouseId);
            log.info("SalesInvoice Updated in OutboundHeader and Line");
            if (updateOutboundHeaderAndLine == null) {
                updateOutboundHeaderAndLine = new OutboundHeaderV2();
            }
            return updateOutboundHeaderAndLine;
        }

        // Getting PreOutboundNo from NumberRangeTable
        String preOutboundNo = getPreOutboundNo(warehouseId, companyCodeId, plantId, languageId);
        String refField1ForOrderType = null;

        List<PreOutboundLineV2> overallCreatedPreoutboundLineList = new ArrayList<>();
        for (OutboundIntegrationLineV2 outboundIntegrationLine : outboundIntegrationHeader.getOutboundIntegrationLines()) {
            log.info("outboundIntegrationLine : " + outboundIntegrationLine);

            /*-------------Insertion of BOM item in preOutboundLine table---------------------------------------------------------*/
            /*
             * Before Insertion into PREOUTBOUNDLINE table , validate the Below
             * Pass the WH_ID/ITM_CODE as PAR_ITM_CODE into BOMHEADER table and validate the records,
             * If record is not Null then fetch BOM_NO Pass BOM_NO in BOMITEM table and fetch CHL_ITM_CODE and
             * CHL_ITM_QTY values and insert along with PAR_ITM_CODE in PREOUTBOUNDLINE table as below
             * Insertion of CHL_ITM_CODE values
             */
            BomHeader bomHeader = mastersService.getBomHeader(outboundIntegrationLine.getItemCode(),
                    companyCodeId, plantId, languageId, warehouseId, authTokenForMastersService.getAccess_token());
            if (bomHeader != null) {
                BomLine[] bomLine = mastersService.getBomLine(bomHeader.getBomNumber(), companyCodeId, plantId, languageId, warehouseId,
                        authTokenForMastersService.getAccess_token());
                List<PreOutboundLineV2> toBeCreatedpreOutboundLineList = new ArrayList<>();
                for (BomLine dbBomLine : bomLine) {
                    toBeCreatedpreOutboundLineList.add(createPreOutboundLineBOMBasedV2(companyCodeId,
                            plantId, languageId, preOutboundNo, outboundIntegrationHeader, dbBomLine, outboundIntegrationLine));
                }

                // Batch Insert - preOutboundLines
                if (!toBeCreatedpreOutboundLineList.isEmpty()) {
                    List<PreOutboundLineV2> createdpreOutboundLine = preOutboundLineV2Repository.saveAll(toBeCreatedpreOutboundLineList);
                    log.info("createdpreOutboundLine [BOM] : " + createdpreOutboundLine);
                    overallCreatedPreoutboundLineList.addAll(createdpreOutboundLine);
                }
            }
            refField1ForOrderType = outboundIntegrationLine.getRefField1ForOrderType();
        }

        /*
         * Inserting BOM Line records in OutboundLine and OrderManagementLine
         */
        if (!overallCreatedPreoutboundLineList.isEmpty()) {
            for (PreOutboundLineV2 preOutboundLine : overallCreatedPreoutboundLineList) {
//                 OrderManagementLine
                OrderManagementLineV2 orderManagementLine = createOrderManagementLineV2(companyCodeId,
                        plantId, languageId, preOutboundNo, outboundIntegrationHeader, preOutboundLine);
                log.info("orderManagementLine created---BOM---> : " + orderManagementLine);
            }

            /*------------------Record Insertion in OUTBOUNDLINE table--for BOM---------*/
            List<OutboundLineV2> createOutboundLineListForBOM = createOutboundLineV2(overallCreatedPreoutboundLineList, outboundIntegrationHeader);
            log.info("createOutboundLine created : " + createOutboundLineListForBOM);
        }

        /*
         * Append PREOUTBOUNDLINE table through below logic
         */
        List<PreOutboundLineV2> createdPreOutboundLineList = new ArrayList<>();
        for (OutboundIntegrationLineV2 outboundIntegrationLine : outboundIntegrationHeader.getOutboundIntegrationLines()) {
            // PreOutboundLine
            try {
                PreOutboundLineV2 preOutboundLine = createPreOutboundLineV2(companyCodeId,
                        plantId, languageId, warehouseId, preOutboundNo, outboundIntegrationHeader, outboundIntegrationLine);
                PreOutboundLineV2 createdPreOutboundLine = preOutboundLineV2Repository.save(preOutboundLine);
                log.info("preOutboundLine created---1---> : " + createdPreOutboundLine);
                createdPreOutboundLineList.add(createdPreOutboundLine);

                // OrderManagementLine
                OrderManagementLineV2 orderManagementLine = createOrderManagementLineV2(companyCodeId,
                        plantId, languageId, preOutboundNo, outboundIntegrationHeader, preOutboundLine);
                log.info("orderManagementLine created---1---> : " + orderManagementLine);

            } catch (Exception e) {
                log.error("Error on processing PreOutboundLine : " + e.toString());
                e.printStackTrace();
            }
        }

        /*------------------Record Insertion in OUTBOUNDLINE tables-----------*/
        List<OutboundLineV2> createOutboundLineList = createOutboundLineV2(createdPreOutboundLineList, outboundIntegrationHeader);
        log.info("createOutboundLine created : " + createOutboundLineList);

        /*------------------Insert into PreOutboundHeader table-----------------------------*/
        PreOutboundHeaderV2 createdPreOutboundHeader = createPreOutboundHeaderV2(companyCodeId,
                plantId, languageId, warehouseId, preOutboundNo, outboundIntegrationHeader, refField1ForOrderType);
        log.info("preOutboundHeader Created : " + createdPreOutboundHeader);

        /*------------------ORDERMANAGEMENTHEADER TABLE-------------------------------------*/
        OrderManagementHeaderV2 createdOrderManagementHeader = createOrderManagementHeaderV2(createdPreOutboundHeader);
        log.info("OrderMangementHeader Created : " + createdOrderManagementHeader);

        /*------------------Record Insertion in OUTBOUNDHEADER/OUTBOUNDLINE tables-----------*/
//        OutboundHeaderV2 outboundHeader = createOutboundHeaderV2(createdPreOutboundHeader, createdOrderManagementHeader.getStatusId());
        OutboundHeaderV2 outboundHeader = createOutboundHeaderV2(createdPreOutboundHeader, createdOrderManagementHeader.getStatusId(), outboundIntegrationHeader);

        /*------------------------------------------------------------------------------------*/
        updateStatusAs47ForOBHeaderV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, outboundHeader.getRefDocNumber());

        /*------------------------------------------------------------------------------------*/
        //Pickup Header Automation only for Picklist Header - OutboundOrderTypeId - 3L : referenceDocumentType - Pick List
        if (outboundIntegrationHeader.getOutboundOrderTypeID() == 3L) {
            updateStatusAs48ForPickupHeaderCreateSuccess(companyCodeId, plantId, languageId, warehouseId, outboundIntegrationHeader, preOutboundNo,
                    outboundHeader.getRefDocNumber(), outboundHeader.getPartnerCode());
        }

        return outboundHeader;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param outboundIntegrationHeader
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void updateStatusAs48ForPickupHeaderCreateSuccess(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                              OutboundIntegrationHeaderV2 outboundIntegrationHeader,
                                                              String preOutboundNo, String refDocNumber, String partnerCode) throws InvocationTargetException, IllegalAccessException, ParseException {

        List<OrderManagementLineV2> orderManagementLineV2List = orderManagementLineService.
                getOrderManagementLineForPickupLineV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber);

        //pickup header create
        Long assignPickerIdLineCount = 0L;
        Long assignPickerListCount = 0L;
//        String assignPickerId = null;
        Long hhtUserCount = 0L;
        Long hhtUserCountInPickupHeader = 0L;
        Long lineCount = 0L;
        Integer pickerPosition = 0;
        Integer picker0Position = 0;
        Integer picker1Position = 0;
        Integer picker2Position = 0;
        Integer picker3Position = 0;
        Integer picker4Position = 0;

        Integer obPicker0Position = 0;
        Integer obPicker1Position = 0;
        Integer obPicker2Position = 0;
        Integer obPicker3Position = 0;


        lineCount = orderManagementLineV2List.stream().count();
        log.info("total Order Lines Count: " + lineCount);

        List<String> assignPickerList = new ArrayList<>();
        List<String> pickUpheaderUserList = new ArrayList<>();

        List<String> assignedPickerIdList = new ArrayList<>();
        List<String> linePickerIdAssignedList = new ArrayList<>();
        List<Long> levelId0List = new ArrayList<>();
        List<Long> levelId1List = new ArrayList<>();
        List<Long> levelId2List = new ArrayList<>();
        List<Long> levelId3List = new ArrayList<>();
        List<Long> levelId4List = new ArrayList<>();

        List<Long> obType0List = new ArrayList<>();
        List<Long> obType1List = new ArrayList<>();
        List<Long> obType2List = new ArrayList<>();
        List<Long> obType3List = new ArrayList<>();
        for (OrderManagementLineV2 orderManagementLine : orderManagementLineV2List) {
            String assignPickerId = null;

            if (orderManagementLine != null && orderManagementLine.getStatusId() != 47L) {
                log.info("orderManagementLine: " + orderManagementLine);

                int currentTime = DateUtils.getCurrentTime();
                log.info("CurrentTime: " + currentTime);

//                if (currentTime < 15) {

                if (warehouseId.equalsIgnoreCase("100")) {
                    log.info("warehouseId: " + warehouseId);

                    Long OB_ORD_TYP_ID = outboundIntegrationHeader.getOutboundOrderTypeID();
                    log.info("OutboundOrderTypeId: " + OB_ORD_TYP_ID);

                    if (OB_ORD_TYP_ID == 0) {
                        if (obType0List.stream().anyMatch(a -> a.equals(OB_ORD_TYP_ID))) {
                            obPicker0Position = obPicker0Position + 1;
                        }
                    }
                    if (OB_ORD_TYP_ID == 1) {
                        if (obType1List.stream().anyMatch(a -> a.equals(OB_ORD_TYP_ID))) {
                            obPicker1Position = obPicker1Position + 1;
                        }
                    }
                    if (OB_ORD_TYP_ID == 2) {
                        if (obType2List.stream().anyMatch(a -> a.equals(OB_ORD_TYP_ID))) {
                            obPicker2Position = obPicker2Position + 1;
                        }
                    }
                    if (OB_ORD_TYP_ID == 3) {
                        if (obType3List.stream().anyMatch(a -> a.equals(OB_ORD_TYP_ID))) {
                            obPicker3Position = obPicker3Position + 1;
                        }
                    }


                    List<String> hhtUserList = preOutboundHeaderV2Repository.getHHTUserPresentList(OB_ORD_TYP_ID, companyCodeId, plantId, languageId, warehouseId);
                    log.info("hhtUserList: " + hhtUserList);

                    if (hhtUserList != null) {
                        hhtUserCount = hhtUserList.stream().count();
                        log.info("hhtUserList count: " + hhtUserCount);
                    }
                    if (hhtUserList != null) {

                        hhtUserCount = hhtUserList.stream().count();
                        log.info("hhtUserList count: " + hhtUserCount);

                        PickupHeaderV2 assignPickerPickUpHeader = pickupHeaderService.getPickupHeaderAutomation(companyCodeId, plantId, languageId, warehouseId,
                                orderManagementLine.getItemCode(), orderManagementLine.getManufacturerName());
                        log.info("pickupHeader--> Status48---> assignPicker---> SameItem: " + assignPickerPickUpHeader);
                        if (assignPickerPickUpHeader != null) {
                            log.info("Picker Assigned: " + assignPickerPickUpHeader.getAssignedPickerId());
                            assignPickerId = assignPickerPickUpHeader.getAssignedPickerId();
                        }

                        if (assignPickerId == null) {
                            log.info("assignPickerId: " + assignPickerId);
                            List<PickupHeaderV2> pickupHeaderAssignedPickerIdList = pickupHeaderService.getPickupHeaderAutomationWithoutStatusId(companyCodeId, plantId, languageId, warehouseId, hhtUserList);
                            if(pickupHeaderAssignedPickerIdList != null && !pickupHeaderAssignedPickerIdList.isEmpty()) {
                                log.info("PickUpHeaderList Count: " + pickupHeaderAssignedPickerIdList.stream().count());
                            }

                            if (pickupHeaderAssignedPickerIdList != null) {
                                pickUpheaderUserList = pickupHeaderAssignedPickerIdList.stream().map(PickupHeaderV2::getAssignedPickerId).distinct().collect(Collectors.toList());
                                hhtUserCountInPickupHeader = pickupHeaderAssignedPickerIdList.stream().count();
                                log.info("hhtUser PickupHeader count: " + hhtUserCountInPickupHeader);
                            }

                            List<PickupLineV2> pickupLineList = pickupLineService.getPickupLineAutomation(companyCodeId, plantId, languageId, warehouseId, hhtUserList);
                            if(pickupLineList != null) {
                                log.info("PickupLineV2List count: " + pickupLineList.stream().count());
                            }
                            if (pickupLineList != null) {
                                linePickerIdAssignedList = pickupLineList.stream().map(PickupLineV2::getAssignedPickerId).distinct().collect(Collectors.toList());
                                assignPickerIdLineCount = linePickerIdAssignedList.stream().count();
                                log.info("assignPickerIdLineCount: " + assignPickerIdLineCount);
                            }

                            assignPickerList = new ArrayList<>();

                            assignPickerListCount = assignPickerList.stream().count();

                            outerLoop:
                            if (lineCount != assignPickerListCount) {
                                log.info("lineCount != assignPickerListCount :" + lineCount + ", " + assignPickerListCount);
                                for (int i = 1; i <= lineCount; ) {
                                    log.info("i, lineCount: " + i + ", " + lineCount);
                                    for (String hhtUser : hhtUserList) {
                                        List<PickupHeaderV2> pickupHeaderList = pickupHeaderService.getPickupHeaderAutomationWithOutStatusIdV2(companyCodeId, plantId, languageId, warehouseId, hhtUser);
                                        log.info("pickUpHeader: " + pickupHeaderList);
                                        if (pickupHeaderList == null || pickupHeaderList.isEmpty()) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }
                                    if (pickupLineList != null) {
                                        log.info("pickupLineList: " + linePickerIdAssignedList);
                                        for (String hhtUser : linePickerIdAssignedList) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }
                                    if (pickupHeaderAssignedPickerIdList != null) {
                                        log.info("pickupHeaderAssignedPickerIdList: " + pickUpheaderUserList);
                                        for (String hhtUser : pickUpheaderUserList) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }

                                    if (hhtUserList != null) {
                                        log.info("hhtUserList: " + hhtUserList);
                                        while (true) {
                                            for (String hhtUser : hhtUserList) {
                                                assignPickerList.add(hhtUser);
                                                i++;

                                                if (i > lineCount) {
                                                    assignPickerListCount = assignPickerList.stream().count();
                                                    log.info("assignPickerListCount: " + assignPickerListCount);
                                                    break outerLoop;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (OB_ORD_TYP_ID == 0) {
                                assignPickerId = assignPickerList.get(obPicker0Position);
                                log.info("OB_ORD_TYP_ID 0 picker");
                                log.info("obPicker0Position: " + obPicker0Position);
                                obType0List.add(OB_ORD_TYP_ID);
                            }
                            if (OB_ORD_TYP_ID == 1) {
                                assignPickerId = assignPickerList.get(obPicker1Position);
                                log.info("OB_ORD_TYP_ID 1 picker");
                                log.info("obPicker1Position: " + obPicker1Position);
                                obType1List.add(OB_ORD_TYP_ID);
                            }
                            if (OB_ORD_TYP_ID == 2) {
                                assignPickerId = assignPickerList.get(obPicker2Position);
                                log.info("OB_ORD_TYP_ID 2 picker");
                                log.info("obPicker2Position: " + obPicker2Position);
                                obType2List.add(OB_ORD_TYP_ID);
                            }
                            if (OB_ORD_TYP_ID == 3) {
                                assignPickerId = assignPickerList.get(obPicker3Position);
                                log.info("OB_ORD_TYP_ID 3 picker");
                                log.info("obPicker3Position: " + obPicker3Position);
                                obType3List.add(OB_ORD_TYP_ID);
                            }

                            log.info("assignPickerId: " + assignPickerId);
                        }
                    }
                }
//            }
                if (warehouseId.equalsIgnoreCase("200")) {
                    log.info("warehouseId: " + warehouseId);

                    if (orderManagementLine.getLevelId() == null) {
                        orderManagementLine.setLevelId("1");                //HardCode default Level
                    }
                    Long LEVEL_ID = Long.valueOf(orderManagementLine.getLevelId());
                    log.info("levelId: " + LEVEL_ID);

                    if (LEVEL_ID == 0) {
                        if (levelId0List.stream().anyMatch(a -> a.equals(LEVEL_ID))) {
                            picker0Position = picker0Position + 1;
                        }
                    }
                    if (LEVEL_ID == 1) {
                        if (levelId1List.stream().anyMatch(a -> a.equals(LEVEL_ID))) {
                            picker1Position = picker1Position + 1;
                        }
                    }
                    if (LEVEL_ID == 2) {
                        if (levelId2List.stream().anyMatch(a -> a.equals(LEVEL_ID))) {
                            picker2Position = picker2Position + 1;
                        }
                    }
                    if (LEVEL_ID == 3) {
                        if (levelId3List.stream().anyMatch(a -> a.equals(LEVEL_ID))) {
                            picker3Position = picker3Position + 1;
                        }
                    }
                    if (LEVEL_ID == 4) {
                        if (levelId4List.stream().anyMatch(a -> a.equals(LEVEL_ID))) {
                            picker4Position = picker4Position + 1;
                        }
                    }

                    List<String> hhtUserList = preOutboundHeaderV2Repository.getHHTUserByLevelIdPresentList(LEVEL_ID, companyCodeId, plantId, languageId, warehouseId);
                    log.info("hhtUserList: " + hhtUserList);

                    if (hhtUserList != null) {

                        hhtUserCount = hhtUserList.stream().count();
                        log.info("hhtUserList count: " + hhtUserCount);

                        PickupHeaderV2 assignPickerPickUpHeader = pickupHeaderService.getPickupHeaderAutomation(companyCodeId, plantId, languageId, warehouseId,
                                orderManagementLine.getItemCode(), orderManagementLine.getManufacturerName());
                        log.info("pickupHeader--> Status48---> assignPicker---> SameItem: " + assignPickerPickUpHeader);
                        if (assignPickerPickUpHeader != null) {
                            log.info("Picker Assigned: " + assignPickerPickUpHeader.getAssignedPickerId());
                            assignPickerId = assignPickerPickUpHeader.getAssignedPickerId();
                        }

                        if (assignPickerId == null) {
                            log.info("assignPickerId: " + assignPickerId);

                            List<PickupHeaderV2> pickupHeaderAssignedPickerIdList = pickupHeaderService.getPickupHeaderAutomationWithoutStatusId(companyCodeId, plantId, languageId, warehouseId, hhtUserList);
                            if(pickupHeaderAssignedPickerIdList != null && !pickupHeaderAssignedPickerIdList.isEmpty()) {
                                log.info("PickUpHeader Count: " + pickupHeaderAssignedPickerIdList.stream().count());
                            }

                            if (pickupHeaderAssignedPickerIdList != null) {
                                pickUpheaderUserList = pickupHeaderAssignedPickerIdList.stream().map(PickupHeaderV2::getAssignedPickerId).distinct().collect(Collectors.toList());
                                hhtUserCountInPickupHeader = pickupHeaderAssignedPickerIdList.stream().count();
                                log.info("hhtUser PickupHeader count: " + hhtUserCountInPickupHeader);
                            }

                            List<PickupLineV2> pickupLineList = pickupLineService.getPickupLineAutomation(companyCodeId, plantId, languageId, warehouseId, hhtUserList);
                            if(pickupLineList != null) {
                                log.info("PickupLineV2List Count: " + pickupLineList.stream().count());
                            }
                            if (pickupLineList != null) {
                                linePickerIdAssignedList = pickupLineList.stream().map(PickupLineV2::getAssignedPickerId).distinct().collect(Collectors.toList());
                                assignPickerIdLineCount = linePickerIdAssignedList.stream().count();
                                log.info("assignPickerIdLineCount: " + assignPickerIdLineCount);
                            }

                            assignPickerList = new ArrayList<>();

                            assignPickerListCount = assignPickerList.stream().count();

                            outerLoop:
                            if (lineCount != assignPickerListCount) {
                                log.info("lineCount != assignPickerListCount :" + lineCount + ", " + assignPickerListCount);
                                for (int i = 1; i <= lineCount; ) {
                                    log.info("i, lineCount: " + i + ", " + lineCount);
                                    for (String hhtUser : hhtUserList) {
                                        List<PickupHeaderV2> pickupHeaderList = pickupHeaderService.getPickupHeaderAutomationWithOutStatusIdV2(companyCodeId, plantId, languageId, warehouseId, hhtUser);
                                        log.info("pickUpHeader: " + pickupHeaderList);
                                        if (pickupHeaderList == null || pickupHeaderList.isEmpty()) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }
                                    if (pickupLineList != null) {
                                        log.info("pickupLineList: " + linePickerIdAssignedList);
                                        for (String hhtUser : linePickerIdAssignedList) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }
                                    if (pickupHeaderAssignedPickerIdList != null) {
                                        log.info("pickupHeaderAssignedPickerIdList: " + pickUpheaderUserList);
                                        for (String hhtUser : pickUpheaderUserList) {
                                            assignPickerList.add(hhtUser);
                                            i++;
                                            if (i > lineCount) {
                                                assignPickerListCount = assignPickerList.stream().count();
                                                log.info("assignPickerListCount: " + assignPickerListCount);
                                                break outerLoop;
                                            }
                                        }
                                    }

                                    if (hhtUserList != null) {
                                        log.info("hhtUserList: " + hhtUserList);
                                        while (true) {
                                            for (String hhtUser : hhtUserList) {
                                                assignPickerList.add(hhtUser);
                                                i++;

                                                if (i > lineCount) {
                                                    assignPickerListCount = assignPickerList.stream().count();
                                                    log.info("assignPickerListCount: " + assignPickerListCount);
                                                    break outerLoop;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (LEVEL_ID == 0) {
                                assignPickerId = assignPickerList.get(picker0Position);
                                log.info("Level 0 picker");
                                log.info("picker0Position: " + picker0Position);
                                levelId0List.add(LEVEL_ID);
                            }
                            if (LEVEL_ID == 1) {
                                assignPickerId = assignPickerList.get(picker1Position);
                                log.info("Level 1 picker");
                                log.info("picker1Position: " + picker1Position);
                                levelId1List.add(LEVEL_ID);
                            }
                            if (LEVEL_ID == 2) {
                                assignPickerId = assignPickerList.get(picker2Position);
                                log.info("Level 2 picker");
                                log.info("picker2Position: " + picker2Position);
                                levelId2List.add(LEVEL_ID);
                            }
                            if (LEVEL_ID == 3) {
                                assignPickerId = assignPickerList.get(picker3Position);
                                log.info("Level 3 picker");
                                log.info("picker3Position: " + picker3Position);
                                levelId3List.add(LEVEL_ID);
                            }
                            if (LEVEL_ID == 4) {
                                assignPickerId = assignPickerList.get(picker4Position);
                                log.info("Level 4 picker");
                                log.info("picker4Position: " + picker4Position);
                                levelId4List.add(LEVEL_ID);
                            }

                            log.info("assignPickerId: " + assignPickerId);

                        }
                    }
                }

                PickupHeaderV2 newPickupHeader = new PickupHeaderV2();
                BeanUtils.copyProperties(orderManagementLine, newPickupHeader, CommonUtils.getNullPropertyNames(orderManagementLine));
                long NUM_RAN_CODE = 10;
                String PU_NO = getNextRangeNumber(NUM_RAN_CODE, companyCodeId, plantId, languageId, warehouseId);
                log.info("PU_NO : " + PU_NO);

                newPickupHeader.setAssignedPickerId(assignPickerId);
                newPickupHeader.setPickupNumber(PU_NO);

                newPickupHeader.setPickToQty(orderManagementLine.getAllocatedQty());

                // PICK_UOM
                newPickupHeader.setPickUom(orderManagementLine.getOrderUom());

                // STATUS_ID
                newPickupHeader.setStatusId(48L);
                statusDescription = stagingLineV2Repository.getStatusDescription(48L, languageId);
                newPickupHeader.setStatusDescription(statusDescription);

                // ProposedPackbarcode
                newPickupHeader.setProposedPackBarCode(orderManagementLine.getProposedPackBarCode());

                // REF_FIELD_1
                newPickupHeader.setReferenceField1(orderManagementLine.getReferenceField1());

                newPickupHeader.setManufacturerCode(orderManagementLine.getManufacturerCode());
                newPickupHeader.setManufacturerName(orderManagementLine.getManufacturerName());
                newPickupHeader.setManufacturerPartNo(orderManagementLine.getManufacturerPartNo());
                newPickupHeader.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
                newPickupHeader.setPickListNumber(outboundIntegrationHeader.getPickListNumber());
                newPickupHeader.setSalesInvoiceNumber(orderManagementLine.getSalesInvoiceNumber());
                newPickupHeader.setOutboundOrderTypeId(orderManagementLine.getOutboundOrderTypeId());
                newPickupHeader.setReferenceDocumentType(outboundIntegrationHeader.getReferenceDocumentType());
                newPickupHeader.setSupplierInvoiceNo(orderManagementLine.getSupplierInvoiceNo());
                newPickupHeader.setTokenNumber(outboundIntegrationHeader.getTokenNumber());
                newPickupHeader.setLevelId(orderManagementLine.getLevelId());
                newPickupHeader.setTargetBranchCode(orderManagementLine.getTargetBranchCode());

                newPickupHeader.setFromBranchCode(outboundIntegrationHeader.getFromBranchCode());
                newPickupHeader.setIsCompleted(outboundIntegrationHeader.getIsCompleted());
                newPickupHeader.setIsCancelled(outboundIntegrationHeader.getIsCancelled());
                newPickupHeader.setMUpdatedOn(outboundIntegrationHeader.getMUpdatedOn());

                PickupHeaderV2 createdPickupHeader = pickupHeaderService.createPickupHeaderV2(newPickupHeader, orderManagementLine.getPickupCreatedBy());
                log.info("pickupHeader created: " + createdPickupHeader);

                orderManagementLine.setPickupNumber(PU_NO);
                orderManagementLine.setAssignedPickerId(assignPickerId);
                orderManagementLine.setStatusId(48L);                        // 2. Update STATUS_ID = 48
                orderManagementLine.setStatusDescription(statusDescription);
//                    orderManagementLine.setPickupUpdatedBy("Automate");            // Ref_field_7
                orderManagementLine.setPickupUpdatedOn(DateUtils.getCurrentKWTDateTime());
                OrderManagementLineV2 orderManagementLineV2 = orderManagementLineV2Repository.save(orderManagementLine);
                log.info("OrderManagementLine updated : " + orderManagementLineV2);
//                    }

                /*
                 * Update ORDERMANAGEMENTHEADER --------------------------------- Pass the
                 * Selected WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE/OB_LINE_NO/ITM_CODE in
                 * OUTBOUNDLINE table and update SATATU_ID as 48
                 */
                OutboundLineV2 outboundLine = outboundLineService.getOutboundLineV2(companyCodeId, plantId, languageId, warehouseId,
                        preOutboundNo, refDocNumber, partnerCode,
                        orderManagementLine.getLineNumber(),
                        orderManagementLine.getItemCode());
                outboundLine.setStatusId(48L);
                outboundLine.setStatusDescription(statusDescription);
                outboundLine = outboundLineV2Repository.save(outboundLine);
                log.info("outboundLine updated : " + outboundLine);
            }

        }
        // OutboundHeader Update
        OutboundHeaderV2 outboundHeader = outboundHeaderService.getOutboundHeaderV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo,
                refDocNumber, partnerCode);
        outboundHeader.setStatusId(48L);
        outboundHeader.setStatusDescription(statusDescription);
        outboundHeaderV2Repository.save(outboundHeader);
        log.info("outboundHeader updated : " + outboundHeader);

        // ORDERMANAGEMENTHEADER Update
        OrderManagementHeaderV2 orderManagementHeader = orderManagementHeaderService
                .getOrderManagementHeaderV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode);
        orderManagementHeader.setStatusId(48L);
        orderManagementHeader.setStatusDescription(statusDescription);
        orderManagementHeaderV2Repository.save(orderManagementHeader);
        log.info("orderManagementHeader updated : " + orderManagementHeader);
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     */
    private void updateStatusAs47ForOBHeaderV2(String companyCodeId, String plantId,
                                               String languageId, String warehouseId, String preOutboundNo, String refDocNumber) {
        List<OutboundLineV2> outboundLineList = outboundLineService.getOutboundLineV2(
                companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber);

        long matchedCount = 0L;
        boolean isConditionMet = false;
        if (outboundLineList != null) {
            matchedCount = outboundLineList.stream().filter(a -> a.getStatusId() == 47L).count();
            isConditionMet = (matchedCount == outboundLineList.size());
        }
        Long STATUS_ID_47 = 47L;
        if (isConditionMet) {
            //----------------Outbound Header update----------------------------------------------------------------------------------------
            outboundHeaderV2Repository.updateOutboundHeaderStatusAs47(companyCodeId, plantId, languageId, warehouseId, refDocNumber, STATUS_ID_47);
            log.info("OutboundHeader status updated as 47. ");

            //----------------Preoutbound Header--------------------------------------------------------------------------------------------
//            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
//            StatusId idStatus = idmasterService.getStatus(STATUS_ID_47, warehouseId, authTokenForIDService.getAccess_token());
            statusDescription = stagingLineV2Repository.getStatusDescription(STATUS_ID_47, languageId);
            preOutboundHeaderV2Repository.updatePreOutboundHeaderStatus(languageId, companyCodeId, plantId, warehouseId, refDocNumber, STATUS_ID_47, statusDescription);
            log.info("PreOutbound Header status updated as 47.");
        }
    }

    /**
     * @param outboundIntegrationHeader
     * @param warehouseId
     * @return
     */
//    private OutboundHeaderV2 updateOutboundHeader(OutboundIntegrationHeaderV2 outboundIntegrationHeader, String warehouseId) {
//        OutboundHeaderV2 dbOutboundHeader = outboundHeaderService.getOutboundHeaderForSalesInvoiceUpdateV2(
//                outboundIntegrationHeader.getCompanyCode(), outboundIntegrationHeader.getBranchCode(), "EN",
//                warehouseId, outboundIntegrationHeader.getPickListNumber());
//        log.info("OutboundHeader: " + dbOutboundHeader);
//        if (dbOutboundHeader != null) {
//            dbOutboundHeader.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
//            dbOutboundHeader.setSalesInvoiceNumber(outboundIntegrationHeader.getSalesInvoiceNumber());
//            dbOutboundHeader.setInvoiceDate(outboundIntegrationHeader.getRequiredDeliveryDate());
//            outboundHeaderV2Repository.save(dbOutboundHeader);
//            log.info("OutboundHeader updated with salesInvoice: " + outboundIntegrationHeader.getSalesInvoiceNumber());
//            List<OutboundLineV2> dbOutboundLineList = outboundLineService.getOutboundLineV2(outboundIntegrationHeader.getCompanyCode(),
//                    outboundIntegrationHeader.getBranchCode(), "EN", warehouseId,
//                    dbOutboundHeader.getPreOutboundNo(), dbOutboundHeader.getRefDocNumber());
//            if (dbOutboundLineList != null) {
//                List<OutboundLineV2> updateOutboundLineList = new ArrayList<>();
//                for (OutboundLineV2 dbOutboundLine : dbOutboundLineList) {
//                    dbOutboundLine.setSalesInvoiceNumber(outboundIntegrationHeader.getSalesInvoiceNumber());
//                    dbOutboundLine.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
//                    dbOutboundLine.setInvoiceDate(outboundIntegrationHeader.getRequiredDeliveryDate());
//                    updateOutboundLineList.add(dbOutboundLine);
//                }
//                outboundLineV2Repository.saveAll(updateOutboundLineList);
//            }
//        }
//        return dbOutboundHeader;
//    }

    /**
     * @return
     */
    private String getPreOutboundNo(String warehouseId, String companyCodeId, String plantId, String languageId) {
        /*
         * Pass WH_ID - User logged in WH_ID and NUM_RAN_CODE = 2 values in NUMBERRANGE table and
         * fetch NUM_RAN_CURRENT value of FISCALYEAR = CURRENT YEAR and add +1and then
         * update in PREINBOUNDHEADER table
         */
        try {
            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
            String nextRangeNumber = getNextRangeNumber(9L, companyCodeId, plantId, languageId, warehouseId, authTokenForIDMasterService.getAccess_token());
            return nextRangeNumber;
        } catch (Exception e) {
            throw new BadRequestException("Error on Number generation." + e.toString());
        }
    }

    /**
     * @param createdPreOutboundLine
     * @return
     */
    private List<OutboundLineV2> createOutboundLineV2(List<PreOutboundLineV2> createdPreOutboundLine, OutboundIntegrationHeaderV2 outboundIntegrationHeader) {
        List<OutboundLineV2> outboundLines = new ArrayList<>();
        for (PreOutboundLineV2 preOutboundLine : createdPreOutboundLine) {
            List<OrderManagementLineV2> orderManagementLine = orderManagementLineService.getOrderManagementLineV2(
                    preOutboundLine.getCompanyCodeId(),
                    preOutboundLine.getPlantId(),
                    preOutboundLine.getLanguageId(),
                    preOutboundLine.getWarehouseId(),
                    preOutboundLine.getPreOutboundNo(),
                    preOutboundLine.getLineNumber(),
                    preOutboundLine.getItemCode());
            for (OrderManagementLineV2 dbOrderManagementLine : orderManagementLine) {
                OutboundLineV2 outboundLine = new OutboundLineV2();
                BeanUtils.copyProperties(preOutboundLine, outboundLine, CommonUtils.getNullPropertyNames(preOutboundLine));
                outboundLine.setDeliveryQty(0D);
                outboundLine.setStatusId(dbOrderManagementLine.getStatusId());
                outboundLine.setCreatedBy(preOutboundLine.getCreatedBy());
                outboundLine.setCreatedOn(preOutboundLine.getCreatedOn());

                IKeyValuePair description = stagingLineV2Repository.getDescription(preOutboundLine.getCompanyCodeId(),
                        preOutboundLine.getLanguageId(),
                        preOutboundLine.getPlantId(),
                        preOutboundLine.getWarehouseId());

                if (preOutboundLine.getStatusId() != null) {
                    statusDescription = stagingLineV2Repository.getStatusDescription(preOutboundLine.getStatusId(), preOutboundLine.getLanguageId());
                    outboundLine.setStatusDescription(statusDescription);
                }
                outboundLine.setCompanyDescription(description.getCompanyDesc());
                outboundLine.setPlantDescription(description.getPlantDesc());
                outboundLine.setWarehouseDescription(description.getWarehouseDesc());

                outboundLine.setInvoiceDate(outboundIntegrationHeader.getRequiredDeliveryDate());
                outboundLine.setSalesInvoiceNumber(outboundIntegrationHeader.getSalesInvoiceNumber());
                outboundLine.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
                outboundLine.setPickListNumber(outboundIntegrationHeader.getPickListNumber());
                outboundLine.setSupplierInvoiceNo(preOutboundLine.getSupplierInvoiceNo());
                outboundLine.setTokenNumber(preOutboundLine.getTokenNumber());

                outboundLine.setMiddlewareId(preOutboundLine.getMiddlewareId());
                outboundLine.setMiddlewareHeaderId(preOutboundLine.getMiddlewareHeaderId());
                outboundLine.setMiddlewareTable(preOutboundLine.getMiddlewareTable());
                outboundLine.setSalesInvoiceNumber(preOutboundLine.getSalesInvoiceNumber());
                outboundLine.setManufacturerFullName(preOutboundLine.getManufacturerFullName());
                outboundLine.setManufacturerName(preOutboundLine.getManufacturerName());
                outboundLine.setOutboundOrderTypeId(preOutboundLine.getOutboundOrderTypeId());
                outboundLine.setReferenceDocumentType(preOutboundLine.getReferenceDocumentType());
                outboundLine.setTargetBranchCode(preOutboundLine.getTargetBranchCode());
                outboundLine.setBarcodeId(dbOrderManagementLine.getBarcodeId());

                outboundLine.setTransferOrderNo(preOutboundLine.getTransferOrderNo());
                outboundLine.setReturnOrderNo(preOutboundLine.getReturnOrderNo());
                outboundLine.setIsCompleted(preOutboundLine.getIsCompleted());
                outboundLine.setIsCancelled(preOutboundLine.getIsCancelled());

                outboundLines.add(outboundLine);
            }
        }

        outboundLines = outboundLineV2Repository.saveAll(outboundLines);
        log.info("outboundLines created -----2------>: " + outboundLines);
        return outboundLines;
    }

    /**
     * @param createdPreOutboundHeader
     * @param statusId
     * @return
     * @throws ParseException
     */
    private OutboundHeaderV2 createOutboundHeaderV2(PreOutboundHeaderV2 createdPreOutboundHeader, Long statusId, OutboundIntegrationHeaderV2 outboundIntegrationHeader) throws ParseException {

        OutboundHeaderV2 outboundHeader = new OutboundHeaderV2();
        BeanUtils.copyProperties(createdPreOutboundHeader, outboundHeader, CommonUtils.getNullPropertyNames(createdPreOutboundHeader));
//        OrderManagementHeaderV2 dbOrderManagementHeader = orderManagementHeaderService.getOrderManagementHeaderV2(createdPreOutboundHeader.getPreOutboundNo());

        /*
         * Setting up KuwaitTime
         */
        Date kwtDate = DateUtils.getCurrentKWTDateTime();
        outboundHeader.setRefDocDate(kwtDate);
        outboundHeader.setStatusId(statusId);

        statusDescription = stagingLineV2Repository.getStatusDescription(statusId, createdPreOutboundHeader.getLanguageId());

        outboundHeader.setStatusDescription(statusDescription);

        outboundHeader.setCompanyDescription(createdPreOutboundHeader.getCompanyDescription());
        outboundHeader.setPlantDescription(createdPreOutboundHeader.getPlantDescription());
        outboundHeader.setWarehouseDescription(createdPreOutboundHeader.getWarehouseDescription());
        outboundHeader.setMiddlewareId(createdPreOutboundHeader.getMiddlewareId());
        outboundHeader.setMiddlewareTable(createdPreOutboundHeader.getMiddlewareTable());
        outboundHeader.setReferenceDocumentType(createdPreOutboundHeader.getReferenceDocumentType());

        outboundHeader.setInvoiceDate(outboundIntegrationHeader.getRequiredDeliveryDate());
        outboundHeader.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
        outboundHeader.setSalesInvoiceNumber(outboundIntegrationHeader.getSalesInvoiceNumber());
        outboundHeader.setPickListNumber(outboundIntegrationHeader.getPickListNumber());
        outboundHeader.setTokenNumber(outboundIntegrationHeader.getTokenNumber());
        outboundHeader.setTargetBranchCode(outboundIntegrationHeader.getTargetBranchCode());

        outboundHeader.setFromBranchCode(outboundIntegrationHeader.getFromBranchCode());
        outboundHeader.setIsCompleted(outboundIntegrationHeader.getIsCompleted());
        outboundHeader.setIsCancelled(outboundIntegrationHeader.getIsCancelled());
        outboundHeader.setMUpdatedOn(outboundHeader.getMUpdatedOn());

        outboundHeader.setCreatedBy(createdPreOutboundHeader.getCreatedBy());
        outboundHeader.setCreatedOn(createdPreOutboundHeader.getCreatedOn());
        outboundHeader = outboundHeaderV2Repository.save(outboundHeader);
        log.info("Created outboundHeader : " + outboundHeader);
        return outboundHeader;
    }


    /**
     * @param createdPreOutboundHeader
     * @return
     */
    private OrderManagementHeaderV2 createOrderManagementHeaderV2(PreOutboundHeaderV2 createdPreOutboundHeader) {

        OrderManagementHeaderV2 newOrderManagementHeader = new OrderManagementHeaderV2();
        BeanUtils.copyProperties(createdPreOutboundHeader, newOrderManagementHeader, CommonUtils.getNullPropertyNames(createdPreOutboundHeader));

//        AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
//        StatusId idStatus = idmasterService.getStatus(41L, createdPreOutboundHeader.getWarehouseId(), authTokenForIDService.getAccess_token());
        newOrderManagementHeader.setStatusId(41L);    // Hard Coded Value "41"

        statusDescription = stagingLineV2Repository.getStatusDescription(41L, createdPreOutboundHeader.getLanguageId());

        newOrderManagementHeader.setStatusDescription(statusDescription);
        newOrderManagementHeader.setCompanyDescription(createdPreOutboundHeader.getCompanyDescription());
        newOrderManagementHeader.setPlantDescription(createdPreOutboundHeader.getPlantDescription());
        newOrderManagementHeader.setWarehouseDescription(createdPreOutboundHeader.getWarehouseDescription());
        newOrderManagementHeader.setMiddlewareId(createdPreOutboundHeader.getMiddlewareId());
        newOrderManagementHeader.setMiddlewareTable(createdPreOutboundHeader.getMiddlewareTable());
        newOrderManagementHeader.setReferenceDocumentType(createdPreOutboundHeader.getReferenceDocumentType());
        newOrderManagementHeader.setSalesOrderNumber(createdPreOutboundHeader.getSalesOrderNumber());
        newOrderManagementHeader.setPickListNumber(createdPreOutboundHeader.getPickListNumber());
        newOrderManagementHeader.setTokenNumber(createdPreOutboundHeader.getTokenNumber());
        newOrderManagementHeader.setTargetBranchCode(createdPreOutboundHeader.getTargetBranchCode());


        newOrderManagementHeader.setFromBranchCode(createdPreOutboundHeader.getFromBranchCode());
        newOrderManagementHeader.setIsCompleted(createdPreOutboundHeader.getIsCompleted());
        newOrderManagementHeader.setIsCancelled(createdPreOutboundHeader.getIsCancelled());
        newOrderManagementHeader.setMUpdatedOn(createdPreOutboundHeader.getMUpdatedOn());

        newOrderManagementHeader.setReferenceField7(statusDescription);
        OrderManagementHeaderV2 createdOrderMangementHeader = orderManagementHeaderV2Repository.save(newOrderManagementHeader);
        return createdOrderMangementHeader;
    }


    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param refField1ForOrderType
     * @return
     * @throws ParseException
     */
    private PreOutboundHeaderV2 createPreOutboundHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
                                                          OutboundIntegrationHeaderV2 outboundIntegrationHeader, String refField1ForOrderType) throws ParseException {
        AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
        PreOutboundHeaderV2 preOutboundHeader = new PreOutboundHeaderV2();
        preOutboundHeader.setLanguageId(languageId);
        preOutboundHeader.setCompanyCodeId(companyCodeId);
        preOutboundHeader.setPlantId(plantId);
        preOutboundHeader.setWarehouseId(warehouseId);
        preOutboundHeader.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());
        preOutboundHeader.setPreOutboundNo(preOutboundNo);                                                // PRE_OB_NO
        preOutboundHeader.setPartnerCode(outboundIntegrationHeader.getPartnerCode());
        preOutboundHeader.setOutboundOrderTypeId(outboundIntegrationHeader.getOutboundOrderTypeID());    // Hardcoded value "0"
//        preOutboundHeader.setReferenceDocumentType("SO");                                                // Hardcoded value "SO"

        /*
         * Setting up KuwaitTime
         */
        Date kwtDate = DateUtils.getCurrentKWTDateTime();
        preOutboundHeader.setRefDocDate(kwtDate);
        preOutboundHeader.setStatusId(39L);
        preOutboundHeader.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // REF_FIELD_1
        preOutboundHeader.setReferenceField1(refField1ForOrderType);

        IKeyValuePair description = stagingLineV2Repository.getDescription(companyCodeId,
                languageId,
                plantId,
                warehouseId);

        preOutboundHeader.setCompanyDescription(description.getCompanyDesc());
        preOutboundHeader.setPlantDescription(description.getPlantDesc());
        preOutboundHeader.setWarehouseDescription(description.getWarehouseDesc());

        preOutboundHeader.setMiddlewareId(outboundIntegrationHeader.getMiddlewareId());
        preOutboundHeader.setMiddlewareTable(outboundIntegrationHeader.getMiddlewareTable());
        preOutboundHeader.setReferenceDocumentType(outboundIntegrationHeader.getReferenceDocumentType());
        preOutboundHeader.setSalesOrderNumber(outboundIntegrationHeader.getSalesOrderNumber());
        preOutboundHeader.setPickListNumber(outboundIntegrationHeader.getPickListNumber());
        preOutboundHeader.setTokenNumber(outboundIntegrationHeader.getTokenNumber());
        preOutboundHeader.setTargetBranchCode(outboundIntegrationHeader.getTargetBranchCode());

        preOutboundHeader.setFromBranchCode(outboundIntegrationHeader.getFromBranchCode());
        preOutboundHeader.setIsCompleted(outboundIntegrationHeader.getIsCompleted());
        preOutboundHeader.setIsCancelled(outboundIntegrationHeader.getIsCancelled());
        preOutboundHeader.setMUpdatedOn(outboundIntegrationHeader.getMUpdatedOn());

        // Status Description
//		StatusId idStatus = idmasterService.getStatus(39L, outboundIntegrationHeader.getWarehouseID(), authTokenForIDService.getAccess_token());
        statusDescription = stagingLineV2Repository.getStatusDescription(39L, languageId);
        log.info("PreOutboundHeader StatusDescription: " + statusDescription);
        // REF_FIELD_10
        preOutboundHeader.setReferenceField10(statusDescription);

        preOutboundHeader.setStatusDescription(statusDescription);

        preOutboundHeader.setDeletionIndicator(0L);
        preOutboundHeader.setCreatedBy("MSD_INT");
        preOutboundHeader.setCreatedOn(kwtDate);
        PreOutboundHeaderV2 createdPreOutboundHeader = preOutboundHeaderV2Repository.save(preOutboundHeader);
        log.info("createdPreOutboundHeader : " + createdPreOutboundHeader);
        return createdPreOutboundHeader;
    }


    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param dbBomLine
     * @param outboundIntegrationLine
     * @return
     */
    private PreOutboundLineV2 createPreOutboundLineBOMBasedV2(String companyCodeId, String plantId, String languageId, String preOutboundNo,
                                                              OutboundIntegrationHeaderV2 outboundIntegrationHeader, BomLine dbBomLine,
                                                              OutboundIntegrationLineV2 outboundIntegrationLine) throws ParseException {
//        Warehouse warehouse = getWarehouse(outboundIntegrationHeader.getWarehouseID());

        PreOutboundLineV2 preOutboundLine = new PreOutboundLineV2();
        preOutboundLine.setLanguageId(languageId);
        preOutboundLine.setCompanyCodeId(companyCodeId);
        preOutboundLine.setPlantId(plantId);

        // WH_ID
        preOutboundLine.setWarehouseId(outboundIntegrationHeader.getWarehouseID());

        // PRE_IB_NO
        preOutboundLine.setPreOutboundNo(preOutboundNo);

        // REF_DOC_NO
        preOutboundLine.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());

        // OB_ORD_TYP_ID
        preOutboundLine.setOutboundOrderTypeId(Long.valueOf(outboundIntegrationHeader.getOutboundOrderTypeID()));

        // IB__LINE_NO
        preOutboundLine.setLineNumber(outboundIntegrationLine.getLineReference());

        // ITM_CODE
        preOutboundLine.setItemCode(dbBomLine.getChildItemCode());
        preOutboundLine.setManufacturerName(outboundIntegrationLine.getManufacturerName());

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        //HAREESH 27-08-2022 - bom line creation get item description based on child item code change

        ImBasicData1 imBasicData1 =
                mastersService.getImBasicData1ByItemCodeV2(dbBomLine.getChildItemCode(),
                        languageId,
                        companyCodeId,
                        plantId,
                        outboundIntegrationHeader.getWarehouseID(),
                        outboundIntegrationLine.getManufacturerName(),
                        authTokenForMastersService.getAccess_token());
        if (imBasicData1 != null) {
            preOutboundLine.setDescription(imBasicData1.getDescription());
        }

        // PARTNER_CODE
        preOutboundLine.setPartnerCode(outboundIntegrationHeader.getPartnerCode());

        // ORD_QTY
        double orderQuantity = outboundIntegrationLine.getOrderedQty() * dbBomLine.getChildItemQuantity();
        preOutboundLine.setOrderQty(orderQuantity);

        // ORD_UOM
        preOutboundLine.setOrderUom(outboundIntegrationLine.getUom());

        // REQ_DEL_DATE
        preOutboundLine.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // MFR
        preOutboundLine.setManufacturerPartNo(imBasicData1.getManufacturerPartNo());

        // STCK_TYP_ID
        preOutboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preOutboundLine.setSpecialStockIndicatorId(1L);

        // STATUS_ID
        preOutboundLine.setStatusId(39L);

        IKeyValuePair description = stagingLineV2Repository.getDescription(preOutboundLine.getCompanyCodeId(),
                preOutboundLine.getLanguageId(),
                preOutboundLine.getPlantId(),
                preOutboundLine.getWarehouseId());

        statusDescription = stagingLineV2Repository.getStatusDescription(39L, languageId);
        preOutboundLine.setStatusDescription(statusDescription);

        preOutboundLine.setCompanyDescription(description.getCompanyDesc());
        preOutboundLine.setPlantDescription(description.getPlantDesc());
        preOutboundLine.setWarehouseDescription(description.getWarehouseDesc());

        preOutboundLine.setMiddlewareId(outboundIntegrationLine.getMiddlewareId());
        preOutboundLine.setMiddlewareHeaderId(outboundIntegrationLine.getMiddlewareHeaderId());
        preOutboundLine.setMiddlewareTable(outboundIntegrationLine.getMiddlewareTable());
        preOutboundLine.setReferenceDocumentType(outboundIntegrationLine.getReferenceDocumentType());
        preOutboundLine.setSalesInvoiceNumber(outboundIntegrationLine.getSalesInvoiceNo());
        preOutboundLine.setManufacturerFullName(outboundIntegrationLine.getManufacturerFullName());
        preOutboundLine.setManufacturerName(outboundIntegrationLine.getManufacturerName());
        preOutboundLine.setManufacturerCode(outboundIntegrationLine.getManufacturerCode());
        preOutboundLine.setPickListNumber(outboundIntegrationLine.getPickListNo());
        preOutboundLine.setSalesOrderNumber(outboundIntegrationLine.getSalesOrderNumber());
        preOutboundLine.setSupplierInvoiceNo(outboundIntegrationLine.getSupplierInvoiceNo());
        preOutboundLine.setTokenNumber(outboundIntegrationHeader.getTokenNumber());
        preOutboundLine.setTargetBranchCode(outboundIntegrationHeader.getTargetBranchCode());

        preOutboundLine.setTransferOrderNo(outboundIntegrationLine.getTransferOrderNo());
        preOutboundLine.setReturnOrderNo(outboundIntegrationLine.getReturnOrderNo());
        preOutboundLine.setIsCompleted(outboundIntegrationLine.getIsCompleted());
        preOutboundLine.setIsCancelled(outboundIntegrationLine.getIsCancelled());

        // REF_FIELD_1
        preOutboundLine.setReferenceField1(outboundIntegrationLine.getRefField1ForOrderType());

        // REF_FIELD_2
        preOutboundLine.setReferenceField2("BOM");

        preOutboundLine.setDeletionIndicator(0L);
        preOutboundLine.setCreatedBy("MSD_INT");
//        preOutboundLine.setCreatedOn(new Date());
        preOutboundLine.setCreatedOn(DateUtils.getCurrentKWTDateTime());
        return preOutboundLine;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param outboundIntegrationLine
     * @return
     */
    private PreOutboundLineV2 createPreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
                                                      OutboundIntegrationHeaderV2 outboundIntegrationHeader, OutboundIntegrationLineV2 outboundIntegrationLine) throws ParseException {
        PreOutboundLineV2 preOutboundLine = new PreOutboundLineV2();

        preOutboundLine.setLanguageId(languageId);
        preOutboundLine.setCompanyCodeId(companyCodeId);
        preOutboundLine.setPlantId(plantId);

        // WH_ID
        preOutboundLine.setWarehouseId(warehouseId);

        // REF DOC Number
        preOutboundLine.setRefDocNumber(outboundIntegrationHeader.getRefDocumentNo());

        // PRE_IB_NO
        preOutboundLine.setPreOutboundNo(preOutboundNo);

        // PARTNER_CODE
        preOutboundLine.setPartnerCode(outboundIntegrationHeader.getPartnerCode());

        // IB__LINE_NO
        preOutboundLine.setLineNumber(outboundIntegrationLine.getLineReference());

        // ITM_CODE
        preOutboundLine.setItemCode(outboundIntegrationLine.getItemCode());

        // OB_ORD_TYP_ID
        preOutboundLine.setOutboundOrderTypeId(outboundIntegrationHeader.getOutboundOrderTypeID());

        // STATUS_ID
        preOutboundLine.setStatusId(39L);

        // STCK_TYP_ID
        preOutboundLine.setStockTypeId(1L);

        // SP_ST_IND_ID
        preOutboundLine.setSpecialStockIndicatorId(1L);

        preOutboundLine.setManufacturerName(outboundIntegrationLine.getManufacturerName());

        IKeyValuePair description = stagingLineV2Repository.getDescription(preOutboundLine.getCompanyCodeId(),
                preOutboundLine.getLanguageId(),
                preOutboundLine.getPlantId(),
                preOutboundLine.getWarehouseId());

        statusDescription = stagingLineV2Repository.getStatusDescription(39L, languageId);
        preOutboundLine.setStatusDescription(statusDescription);

        preOutboundLine.setCompanyDescription(description.getCompanyDesc());
        preOutboundLine.setPlantDescription(description.getPlantDesc());
        preOutboundLine.setWarehouseDescription(description.getWarehouseDesc());

        preOutboundLine.setMiddlewareId(outboundIntegrationLine.getMiddlewareId());
        preOutboundLine.setMiddlewareHeaderId(outboundIntegrationLine.getMiddlewareHeaderId());
        preOutboundLine.setMiddlewareTable(outboundIntegrationLine.getMiddlewareTable());
        preOutboundLine.setReferenceDocumentType(outboundIntegrationLine.getReferenceDocumentType());
        preOutboundLine.setSalesInvoiceNumber(outboundIntegrationLine.getSalesInvoiceNo());
        preOutboundLine.setManufacturerFullName(outboundIntegrationLine.getManufacturerFullName());
        preOutboundLine.setManufacturerCode(outboundIntegrationLine.getManufacturerCode());
        preOutboundLine.setManufacturerName(outboundIntegrationLine.getManufacturerName());
        preOutboundLine.setPickListNumber(outboundIntegrationLine.getPickListNo());
        preOutboundLine.setSalesOrderNumber(outboundIntegrationLine.getSalesOrderNumber());
        preOutboundLine.setSupplierInvoiceNo(outboundIntegrationLine.getSupplierInvoiceNo());
        preOutboundLine.setTokenNumber(outboundIntegrationHeader.getTokenNumber());
        preOutboundLine.setTargetBranchCode(outboundIntegrationHeader.getTargetBranchCode());

        preOutboundLine.setTransferOrderNo(outboundIntegrationLine.getTransferOrderNo());
        preOutboundLine.setReturnOrderNo(outboundIntegrationLine.getReturnOrderNo());
        preOutboundLine.setIsCompleted(outboundIntegrationLine.getIsCompleted());
        preOutboundLine.setIsCancelled(outboundIntegrationLine.getIsCancelled());

        // ITEM_TEXT - Pass CHL_ITM_CODE as ITM_CODE in IMBASICDATA1 table and fetch ITEM_TEXT and insert
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        ImBasicData1 imBasicData1 =
                mastersService.getImBasicData1ByItemCodeV2(outboundIntegrationLine.getItemCode(),
                        languageId,
                        companyCodeId,
                        plantId,
                        outboundIntegrationHeader.getWarehouseID(),
                        outboundIntegrationLine.getManufacturerName(),
                        authTokenForMastersService.getAccess_token());
        log.info("imBasicData1 : " + imBasicData1);
        if (imBasicData1 != null && imBasicData1.getDescription() != null) {
            preOutboundLine.setDescription(imBasicData1.getDescription());
        } else {
            preOutboundLine.setDescription(outboundIntegrationLine.getItemText());
        }

        // ORD_QTY
        preOutboundLine.setOrderQty(outboundIntegrationLine.getOrderedQty());

        // ORD_UOM
        preOutboundLine.setOrderUom(outboundIntegrationLine.getUom());

        // REQ_DEL_DATE
        preOutboundLine.setRequiredDeliveryDate(outboundIntegrationHeader.getRequiredDeliveryDate());

        // REF_FIELD_1
        preOutboundLine.setReferenceField1(outboundIntegrationLine.getRefField1ForOrderType());

        preOutboundLine.setDeletionIndicator(0L);
        preOutboundLine.setCreatedBy("MSD_INT");
//        preOutboundLine.setCreatedOn(new Date());
        preOutboundLine.setCreatedOn(DateUtils.getCurrentKWTDateTime());

        log.info("preOutboundLine : " + preOutboundLine);
        return preOutboundLine;
    }

    /**
     * ORDERMANAGEMENTLINE TABLE
     *
     * @param companyCodeId
     * @param plantId
     * @param preOutboundNo
     * @param outboundIntegrationHeader
     * @param preOutboundLine
     * @return
     */
    private OrderManagementLineV2 createOrderManagementLineV2(String companyCodeId, String plantId, String languageId, String preOutboundNo,
                                                              OutboundIntegrationHeaderV2 outboundIntegrationHeader, PreOutboundLineV2 preOutboundLine) throws ParseException {
        OrderManagementLineV2 orderManagementLine = new OrderManagementLineV2();
        BeanUtils.copyProperties(preOutboundLine, orderManagementLine, CommonUtils.getNullPropertyNames(preOutboundLine));

        orderManagementLine.setMiddlewareId(preOutboundLine.getMiddlewareId());
        orderManagementLine.setMiddlewareHeaderId(preOutboundLine.getMiddlewareHeaderId());
        orderManagementLine.setMiddlewareTable(preOutboundLine.getMiddlewareTable());
        orderManagementLine.setReferenceDocumentType(preOutboundLine.getReferenceDocumentType());
        orderManagementLine.setSalesInvoiceNumber(preOutboundLine.getSalesInvoiceNumber());
        orderManagementLine.setManufacturerName(preOutboundLine.getManufacturerName());
        orderManagementLine.setItemCode(preOutboundLine.getItemCode());
        orderManagementLine.setReferenceField1(preOutboundLine.getReferenceField1());
        orderManagementLine.setManufacturerFullName(preOutboundLine.getManufacturerFullName());
        orderManagementLine.setPickListNumber(preOutboundLine.getPickListNumber());
        orderManagementLine.setSupplierInvoiceNo(preOutboundLine.getSupplierInvoiceNo());
        orderManagementLine.setTokenNumber(preOutboundLine.getTokenNumber());
        orderManagementLine.setTargetBranchCode(preOutboundLine.getTargetBranchCode());

        List<String> barcode = stagingLineV2Repository.getPartnerItemBarcode(preOutboundLine.getItemCode(),
                preOutboundLine.getCompanyCodeId(),
                preOutboundLine.getPlantId(),
                preOutboundLine.getWarehouseId(),
                preOutboundLine.getManufacturerName(),
                preOutboundLine.getLanguageId());
        log.info("Barcode : " + barcode);

        if (barcode != null && !barcode.isEmpty()) {
            orderManagementLine.setBarcodeId(barcode.get(0));
            orderManagementLine.setItemBarcode(barcode.get(0));
        }
        if (barcode != null && !barcode.isEmpty()) {
            orderManagementLine.setBarcodeId(orderManagementLine.getItemCode());
            orderManagementLine.setItemBarcode(orderManagementLine.getItemCode());
        }

        orderManagementLine.setTransferOrderNo(preOutboundLine.getTransferOrderNo());
        orderManagementLine.setReturnOrderNo(preOutboundLine.getReturnOrderNo());
        orderManagementLine.setIsCompleted(preOutboundLine.getIsCompleted());
        orderManagementLine.setIsCancelled(preOutboundLine.getIsCancelled());

        List<InventoryV2> inventoryV2 = inventoryService.getInventoryForInhouseTransferV2(companyCodeId, plantId, languageId, preOutboundLine.getWarehouseId(),
                preOutboundLine.getItemCode(), preOutboundLine.getManufacturerName());

        if (inventoryV2 != null && !inventoryV2.isEmpty()) {
            orderManagementLine.setBarcodeId(inventoryV2.get(0).getBarcodeId());
            orderManagementLine.setLevelId(inventoryV2.get(0).getLevelId());
            log.info("PartnerItemBarCode: " + inventoryV2.get(0).getBarcodeId());
        }

        // INV_QTY
        /*
         * 1. If OB_ORD_TYP_ID = 0,1,3
         * Pass WH_ID/ITM_CODE in INVENTORY table and fetch ST_BIN.
         * Pass ST_BIN into STORAGEBIN table and filter ST_BIN values by ST_SEC_ID values of ZB,ZC,ZG,ZT and
         * PUTAWAY_BLOCK and PICK_BLOCK are false(Null).
         *
         * 2. If OB_ORD_TYP_ID = 2
         * Pass  WH_ID/ITM_CODE in INVENTORY table and fetch ST_BIN.
         * Pass ST_BIN into STORAGEBIN table and filter ST_BIN values by ST_SEC_ID values of ZD and PUTAWAY_BLOCK
         * and PICK_BLOCK are false(Null).
         *
         * Pass the filtered ST_BIN/WH_ID/ITM_CODE/BIN_CL_ID=01/STCK_TYP_ID=1 in Inventory table and fetch Sum(INV_QTY)"
         */
        log.info("orderManagementLine : " + orderManagementLine);

        Long OB_ORD_TYP_ID = outboundIntegrationHeader.getOutboundOrderTypeID();
        Long BIN_CLASS_ID;

        if (OB_ORD_TYP_ID == 0L || OB_ORD_TYP_ID == 1L || OB_ORD_TYP_ID == 3L) {
//            List<String> storageSectionIds = Arrays.asList("ZB", "ZC", "ZG", "ZT"); //ZB,ZC,ZG,ZT
            BIN_CLASS_ID = 1L;
            orderManagementLine = createOrderManagementV2(companyCodeId, plantId, languageId, BIN_CLASS_ID, orderManagementLine, preOutboundLine.getWarehouseId(),
                    preOutboundLine.getItemCode(), preOutboundLine.getOrderQty());
        }

        if (OB_ORD_TYP_ID == 2L) {
//            List<String> storageSectionIds = Arrays.asList("ZD"); //ZD
            BIN_CLASS_ID = 7L;
            orderManagementLine = createOrderManagementV2(companyCodeId, plantId, languageId, BIN_CLASS_ID, orderManagementLine, preOutboundLine.getWarehouseId(),
                    preOutboundLine.getItemCode(), preOutboundLine.getOrderQty());
        }

        // PROP_ST_BIN
        return orderManagementLine;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param binClassId
     * @param orderManagementLine
     * @param warehouseId
     * @param itemCode
     * @param ORD_QTY
     * @return
     */
    private OrderManagementLineV2 createOrderManagementV2(String companyCodeId, String plantId, String languageId,
                                                          Long binClassId, OrderManagementLineV2 orderManagementLine,
                                                          String warehouseId, String itemCode, Double ORD_QTY) throws ParseException {
        List<IInventoryImpl> stockType1InventoryList = inventoryService.
                getInventoryForOrderManagementV2(companyCodeId, plantId, languageId, warehouseId, itemCode, 1L, binClassId, orderManagementLine.getManufacturerName());
        log.info("---Global---stockType1InventoryList-------> : " + stockType1InventoryList);
        if (stockType1InventoryList.isEmpty()) {
            return createEMPTYOrderManagementLineV2(orderManagementLine);
        }
        return orderManagementLineService.updateAllocationV2(orderManagementLine, binClassId, ORD_QTY, warehouseId, itemCode, "ORDER_PLACED");
    }

    /**
     * @param orderManagementLine
     * @return
     */
    private OrderManagementLineV2 createEMPTYOrderManagementLineV2(OrderManagementLineV2 orderManagementLine) {

        orderManagementLine.setStatusId(47L);

        statusDescription = stagingLineV2Repository.getStatusDescription(47L, orderManagementLine.getLanguageId());

        orderManagementLine.setReferenceField7(statusDescription);
        orderManagementLine.setProposedStorageBin("");
        orderManagementLine.setProposedPackBarCode("");
        orderManagementLine.setInventoryQty(0D);
        orderManagementLine.setAllocatedQty(0D);

        IKeyValuePair description = stagingLineV2Repository.getDescription(orderManagementLine.getCompanyCodeId(),
                orderManagementLine.getLanguageId(),
                orderManagementLine.getPlantId(),
                orderManagementLine.getWarehouseId());


        orderManagementLine.setCompanyDescription(description.getCompanyDesc());
        orderManagementLine.setPlantDescription(description.getPlantDesc());
        orderManagementLine.setWarehouseDescription(description.getWarehouseDesc());

        orderManagementLine.setStatusDescription(statusDescription);

        orderManagementLine = orderManagementLineV2Repository.save(orderManagementLine);
        log.info("orderManagementLine created: " + orderManagementLine);
        return orderManagementLine;
    }

    public OutboundIntegrationLog createOutboundIntegrationLogV2(OutboundIntegrationHeaderV2 outbound)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        try {
            OutboundIntegrationLog dbOutboundIntegrationLog = new OutboundIntegrationLog();

            try {
                Optional<com.tekclover.wms.api.transaction.model.warehouse.Warehouse> warehouse =
                        warehouseRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
                                outbound.getCompanyCode(), outbound.getBranchCode(), "EN", 0L);
                dbOutboundIntegrationLog.setWarehouseId(warehouse.get().getWarehouseId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            dbOutboundIntegrationLog.setLanguageId("EN");
            dbOutboundIntegrationLog.setCompanyCodeId(outbound.getCompanyCode());
            dbOutboundIntegrationLog.setPlantId(outbound.getBranchCode());
            dbOutboundIntegrationLog.setIntegrationLogNumber(outbound.getRefDocumentNo());
            dbOutboundIntegrationLog.setRefDocNumber(outbound.getRefDocumentNo());
            dbOutboundIntegrationLog.setOrderReceiptDate(outbound.getOrderProcessedOn());
            dbOutboundIntegrationLog.setIntegrationStatus("FAILED");
            dbOutboundIntegrationLog.setOrderReceiptDate(outbound.getOrderProcessedOn());
            dbOutboundIntegrationLog.setDeletionIndicator(0L);
            dbOutboundIntegrationLog.setCreatedBy("MSD_API");
            /*
             * Setting up KuwaitTime
             */
            Date kwtDate = DateUtils.getCurrentKWTDateTime();
            dbOutboundIntegrationLog.setCreatedOn(kwtDate);
            dbOutboundIntegrationLog = outboundIntegrationLogRepository.save(dbOutboundIntegrationLog);
            log.info("dbOutboundIntegrationLog : " + dbOutboundIntegrationLog);
            return dbOutboundIntegrationLog;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //Delete PreOutBoundHeader
    public PreOutboundHeaderV2 deletePreOutboundHeader(String companyCodeId, String languageId, String plantId,
                                                       String warehouseId, String refDocNumber, String loginUserID) throws Exception {
        PreOutboundHeaderV2 preOutboundHeaderV2 =
                preOutboundHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, refDocNumber, 0L);

        if (preOutboundHeaderV2 != null) {
            preOutboundHeaderV2.setUpdatedBy(loginUserID);
            preOutboundHeaderV2.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
//            preOutboundHeaderV2.setUpdatedOn(new Date());
            preOutboundHeaderV2.setDeletionIndicator(1L);
            preOutboundHeaderV2Repository.save(preOutboundHeaderV2);
        }
        return preOutboundHeaderV2;
    }


    //Delete PickList
    private void deletePickList(String companyCodeId, String plantId, String languageId,
                                String warehouseId, String oldPickListNumber, String loginUserID) throws Exception {

        //Delete PreOutBoundHeaderV2
        PreOutboundHeaderV2 preOutboundHeader = deletePreOutboundHeader(companyCodeId, languageId, plantId, warehouseId, oldPickListNumber, loginUserID);
        log.info("PreOutBoundHeader Deleted SuccessFully" + preOutboundHeader);

        //Delete PreOutboundLine
        List<PreOutboundLineV2> preOutboundLineV2List = preOutboundLineService.deletePreOutBoundLine(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("PreOutBoundLine Deleted SuccessFully" + preOutboundLineV2List);

        //DeleteOrderManagementLine
        OrderManagementHeader orderManagementHeader = orderManagementHeaderService.deleteOrderManagementHeaderV2(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("OrderManagementHeader Deleted SuccessFully" + orderManagementHeader);

        //DeleteOrderManagementLine
        List<OrderManagementLineV2> orderManagementLine = orderManagementLineService.deleteOrderManagementLineV2(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("OrderManagementLine Deleted SuccessFully" + orderManagementLine);

        //Delete PickupHeader
        PickupHeader pickupHeader = pickupHeaderService.deletePickUpHeaderV2(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("PickupHeader Deleted SuccessFully" + pickupHeader);

        //Delete PickUpLine
        List<PickupLineV2> pickupLineV2 = pickupLineService.deletePickUpLine(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("PickupLineV2 Deleted SuccessFully" + pickupLineV2);

        //Quality Header
        QualityHeaderV2 qualityHeaderV2 = qualityHeaderService.deleteQualityHeaderV2(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("QualityHeader Deleted SuccessFully" + qualityHeaderV2);

        //Quality Line
        List<QualityLineV2> qualityLineV2 = qualityLineService.deleteQualityLine(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("QualityLine Deleted SuccessFully" + qualityLineV2);

        //Delete OutBoundHeader
        OutboundHeaderV2 outboundHeaderV2 = outboundHeaderService.deleteOutBoundHeader(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("OutboundHeaderV2 Deleted SuccessFully" + outboundHeaderV2);

        //Delete OutBoundLine
        List<OutboundLineV2> outboundLineV2 = outboundLineService.deleteOutBoundLine(companyCodeId, plantId, languageId, warehouseId, oldPickListNumber, loginUserID);
        log.info("OutboundLineV2 Deleted SuccessFully" + outboundLineV2);

        //Delete InventoryMovement
        InventoryMovement inventoryMovement = inventoryMovementService.deleteInventoryMovement(warehouseId, companyCodeId, plantId, languageId, oldPickListNumber, loginUserID);
        log.info("InventoryMovement Deleted SuccessFully" + inventoryMovement);

        if (orderManagementLine != null && pickupHeader != null) {
            List<AddPickupLine> dbPickUpLine = new ArrayList<>();
            for (OrderManagementLineV2 orderManagementLineV2 : orderManagementLine) {
                try {
                    BeanUtils.copyProperties(orderManagementLineV2, dbPickUpLine, CommonUtils.getNullPropertyNames(orderManagementLineV2));
                    List<PickupLineV2> pickUpLine = pickupLineService.createPickupLineV2(dbPickUpLine, loginUserID);
                    if (pickUpLine != null) {
                        for (PickupLineV2 pickupLine : pickUpLine) {
                            List<AddQualityLineV2> qualityLineV2List = new ArrayList<>();
                            BeanUtils.copyProperties(pickupLine, qualityLineV2List, CommonUtils.getNullPropertyNames(pickupLine));
                            List<QualityLineV2> dbQualityLine = qualityLineService.createQualityLineV2(qualityLineV2List, loginUserID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());
                }
            }
        }
    }
}