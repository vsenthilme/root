package com.tekclover.wms.api.transaction.service;


import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.inbound.InboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.InboundIntegrationHeader;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.InboundIntegrationLine;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundHeaderEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderV2;
import com.tekclover.wms.api.transaction.repository.GrHeaderV2Repository;
import com.tekclover.wms.api.transaction.repository.InboundHeaderRepository;
import com.tekclover.wms.api.transaction.repository.InboundOrderLinesV2Repository;
import com.tekclover.wms.api.transaction.repository.InboundOrderV2Repository;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class InvoiceCancellationService {
    @Autowired
    private GrHeaderV2Repository grHeaderV2Repository;

    @Autowired
    InboundHeaderRepository inboundHeaderRepository;

    @Autowired
    PreInboundHeaderService preInboundHeaderService;

    @Autowired
    PreInboundLineService preInboundLineService;

    @Autowired
    StagingHeaderService stagingHeaderService;

    @Autowired
    StagingLineService stagingLineService;

    @Autowired
    GrHeaderService grHeaderService;

    @Autowired
    GrLineService grLineService;

    @Autowired
    PutAwayHeaderService putAwayHeaderService;

    @Autowired
    PutAwayLineService putAwayLineService;

    @Autowired
    InboundHeaderService inboundHeaderService;

    @Autowired
    InboundLineService inboundLineService;

    @Autowired
    InboundOrderLinesV2Repository inboundOrderLinesV2Repository;

    @Autowired
    InboundOrderV2Repository inboundOrderV2Repository;

    @Autowired
    PreInboundHeaderService preinboundheaderService;

    @Autowired
    InventoryMovementService inventoryMovementService;

    @Autowired
    OrderService orderService;

//===================================================================================================================================

    @Transactional
    public void replaceSupplierInvoice(String companyCode, String languageId, String plantId, String warehouseId, String oldInvoiceNo, String newInvoiceNo, String loginUserId) throws ParseException {

        Optional<InboundHeader> dbInvoiceNumber = inboundHeaderRepository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndStatusIdAndDeletionIndicator(
                companyCode, plantId, languageId, warehouseId, oldInvoiceNo, 24L, 0L);

        if (dbInvoiceNumber.isPresent()) {
            throw new BadRequestException("Invoice cannot be processed it has been already completed in WMS " + oldInvoiceNo);
        }

        //  PreInboundHeader
        List<PreInboundHeaderEntityV2> preInboundHeaderV2 = preInboundHeaderService.deletePreInoundHeader(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PreInboundHeader Deleted SuccessFully" + preInboundHeaderV2);

        //Delete PreInboundLine
        List<PreInboundLineEntityV2> preInboundLineEntityV2 = preInboundLineService.deletePreInboundLine(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PreInboundLine Deleted Successfully " + preInboundLineEntityV2);

        //Delete StagingHeader
        StagingHeaderV2 stagingHeaderV2 = stagingHeaderService.deleteStagingHeaderV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("StagingHeader Deleted Successfully" + stagingHeaderV2);

        //Delete StagingLine
        List<StagingLineEntityV2> stagingLineEntityV2 = stagingLineService.deleteStagingLineV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("StagingLine Deleted Successfully " + stagingLineEntityV2);

        //Delete GrHeaderService
        GrHeaderV2 grHeaderV2 = grHeaderService.deleteGrHeaderV2(companyCode, languageId, plantId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("GrHeader Deleted Successfully " + grHeaderV2);

        //Delete GrLine
        List<GrLineV2> grLineV2 = grLineService.deleteGrLineV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("GrLine Deleted Successfully " + grLineV2);

        //Delete PutAwayHeader
        List<PutAwayHeaderV2> putAwayHeaderV2 = putAwayHeaderService.deletePutAwayHeaderV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PutAwayHeader Deleted Successfully " + putAwayHeaderV2);

        //Delete PutAwayLine
        List<PutAwayLineV2> putAwayLineV2List = putAwayLineService.deletePutAwayLineV2(languageId, companyCode, plantId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PutAwayLine Deleted Successfully" + putAwayLineV2List);

        //InboundHeader
        InboundHeaderV2 inboundHeaderV2 = inboundHeaderService.deleteInboundHeaderV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("InboundHeader Deleted Successfully" + inboundHeaderV2);

        //InboundLine
        List<InboundLineV2> inboundLineV2 = inboundLineService.deleteInboundLineV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("InboundLine Deleted Successfully" + inboundLineV2);

        //InventoryMovement
        InventoryMovement inventoryMovement = inventoryMovementService.deleteInventoryMovement(warehouseId, companyCode, plantId, languageId, oldInvoiceNo,loginUserId);
        log.info("InventoryMovement Deleted Successfully" + inventoryMovement);

        //Get GrHeader NewInvoiceNo
        GrHeaderV2 grHeader = grHeaderV2Repository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                companyCode, plantId, languageId, warehouseId, newInvoiceNo,0L);

//        InboundOrderV2 sqlInboundList = inboundOrderV2Repository.findTopByRefDocumentNoOrderByOrderReceivedOnDesc(newInvoiceNo);
//
//        if (sqlInboundList != null) {
//            InboundIntegrationHeader inboundIntegrationHeader = new InboundIntegrationHeader();
//            BeanUtils.copyProperties(sqlInboundList, inboundIntegrationHeader, CommonUtils.getNullPropertyNames(sqlInboundList));
//            inboundIntegrationHeader.setId(sqlInboundList.getOrderId());
//            inboundIntegrationHeader.setMiddlewareId(String.valueOf(sqlInboundList.getMiddlewareId()));
//            inboundIntegrationHeader.setMiddlewareTable(sqlInboundList.getMiddlewareTable());
//
//            List<InboundOrderLinesV2> sqlInboundLineList = inboundOrderLinesV2Repository.getOrderLines(sqlInboundList.getOrderId());
//            log.info("line list: " + sqlInboundLineList);
//            List<InboundIntegrationLine> inboundIntegrationLineList = new ArrayList<>();
//            for (InboundOrderLinesV2 line : sqlInboundLineList) {
//                InboundIntegrationLine inboundIntegrationLine = new InboundIntegrationLine();
//                BeanUtils.copyProperties(line, inboundIntegrationLine, CommonUtils.getNullPropertyNames(line));
//
//                inboundIntegrationLine.setLineReference(line.getLineReference());
//                inboundIntegrationLine.setItemCode(line.getItemCode());
//                inboundIntegrationLine.setItemText(line.getItemText());
//                inboundIntegrationLine.setInvoiceNumber(line.getInvoiceNumber());
//                inboundIntegrationLine.setContainerNumber(line.getContainerNumber());
//                inboundIntegrationLine.setSupplierCode(line.getSupplierCode());
//                inboundIntegrationLine.setSupplierPartNumber(line.getSupplierPartNumber());
//                inboundIntegrationLine.setManufacturerName(line.getManufacturerName());
//                inboundIntegrationLine.setManufacturerPartNo(line.getManufacturerPartNo());
//                inboundIntegrationLine.setExpectedDate(line.getExpectedDate());
//                inboundIntegrationLine.setOrderedQty(line.getExpectedQty());
//                inboundIntegrationLine.setUom(line.getUom());
//                inboundIntegrationLine.setItemCaseQty(line.getItemCaseQty());
//                inboundIntegrationLine.setSalesOrderReference(line.getSalesOrderReference());
//                inboundIntegrationLine.setManufacturerCode(line.getManufacturerCode());
//                inboundIntegrationLine.setOrigin(line.getOrigin());
//                inboundIntegrationLine.setBrand(line.getBrand());
//
//                inboundIntegrationLine.setSupplierName(line.getSupplierName());
//
//                inboundIntegrationLine.setMiddlewareId(String.valueOf(line.getMiddlewareId()));
//                inboundIntegrationLine.setMiddlewareHeaderId(String.valueOf(line.getMiddlewareHeaderId()));
//                inboundIntegrationLine.setMiddlewareTable(line.getMiddlewareTable());
//                inboundIntegrationLine.setManufacturerFullName(line.getManufacturerFullName());
//                inboundIntegrationLine.setPurchaseOrderNumber(line.getPurchaseOrderNumber());
//                inboundIntegrationLine.setContainerNumber(line.getContainerNumber());
//                inboundIntegrationHeader.setContainerNo(line.getContainerNumber());
//
//                inboundIntegrationLineList.add(inboundIntegrationLine);
//            }
//            inboundIntegrationHeader.setInboundIntegrationLine(inboundIntegrationLineList);

            try {
//                log.info("InboundOrder ID : " + inboundIntegrationHeader.getRefDocumentNo());
//                preinboundheaderService.processInboundReceivedV2(inboundIntegrationHeader.getRefDocumentNo(), inboundIntegrationHeader);
                stagingLineService.createGrLine(grHeader);

            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error on inbound processing : " + e.toString());
            }
        }
//    }

}
