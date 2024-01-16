package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData1;
import com.tekclover.wms.api.transaction.model.inbound.InboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.gr.PackBarcode;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.AddGrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrLineV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundHeaderEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.putaway.v2.PutAwayLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.repository.InboundHeaderRepository;
import com.tekclover.wms.api.transaction.repository.StagingLineV2Repository;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceCancellationService extends BaseService{
    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private MastersService mastersService;

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
    InventoryMovementService inventoryMovementService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    StagingLineV2Repository stagingLineV2Repository;

//===================================================================================================================================

    @Transactional
    public void replaceSupplierInvoice(String companyCode, String languageId, String plantId, String warehouseId, String oldInvoiceNo, String newInvoiceNo, String loginUserId) throws ParseException {

        log.info("Supplier Invoice Cancellation Initiated ---> oldInvoiceNumber ----> : " + oldInvoiceNo + ", " + newInvoiceNo);

        Optional<InboundHeader> dbInvoiceNumber = inboundHeaderRepository.findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndStatusIdAndDeletionIndicator(
                companyCode, plantId, languageId, warehouseId, oldInvoiceNo, 24L, 0L);

        if (dbInvoiceNumber.isPresent()) {
            throw new BadRequestException("Invoice cannot be processed it has been already completed " + oldInvoiceNo);
        }

        //InboundHeader
        InboundHeaderV2 inboundHeaderV2 = inboundHeaderService.deleteInboundHeaderV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("InboundHeader Deleted Successfully" + inboundHeaderV2);

        //InboundLine
        List<InboundLineV2> inboundLineV2 = inboundLineService.deleteInboundLineV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("InboundLine Deleted Successfully" + inboundLineV2);

        //PreInboundHeader
        PreInboundHeaderEntityV2 preInboundHeaderV2 = preInboundHeaderService.deletePreInboundHeader(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
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
        List<GrLineV2> grLineList = grLineService.deleteGrLineV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("GrLine Deleted Successfully " + grLineList);

        if(grLineList != null && !grLineList.isEmpty()) {
            List<InventoryV2> inventoryList = new ArrayList<>();
            for (GrLineV2 grLine : grLineList) {
                InventoryV2 dbInventory = inventoryService.deleteInventoryInvoiceCancellation(companyCode, plantId, languageId, warehouseId, grLine);
                inventoryList.add(dbInventory);
            }
            log.info("Inventory List - after delete(insert) : " + inventoryList);
        }

        //Delete PutAwayHeader
        List<PutAwayHeaderV2> putAwayHeaderV2 = putAwayHeaderService.deletePutAwayHeaderV2(companyCode, plantId, languageId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PutAwayHeader Deleted Successfully " + putAwayHeaderV2);

        //Delete PutAwayLine
        List<PutAwayLineV2> putAwayLineV2List = putAwayLineService.deletePutAwayLineV2(languageId, companyCode, plantId, warehouseId, oldInvoiceNo, loginUserId);
        log.info("PutAwayLine Deleted Successfully" + putAwayLineV2List);

        //InventoryMovement
        List<InventoryMovement> inventoryMovement = inventoryMovementService.deleteInventoryMovement(warehouseId, companyCode, plantId, languageId, oldInvoiceNo,loginUserId);
        log.info("InventoryMovement Deleted Successfully" + inventoryMovement);

        //Get GrHeader NewInvoiceNo
        GrHeaderV2 grHeader = grHeaderService.getGrHeaderForReversalV2(companyCode, plantId, languageId, warehouseId, newInvoiceNo);
        log.info("New Supplier Invoice - GrHeader : " + grHeader);

        if(grHeader != null) {

                List<StagingLineEntityV2> stagingLineEntityList = stagingLineService.getStagingLineForGrLine(
                        companyCode, plantId, languageId, warehouseId, grHeader.getStagingNo(), grHeader.getCaseCode());
                log.info("New Supplier Invoice StagingLine : " + stagingLineEntityList);

                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

                Double itemLength = 0D;
                Double itemWidth = 0D;
                Double itemHeight = 0D;
                Double orderQty = 0D;
                Double cbm = 0D;
                Double cbmPerQty = 0D;

                List<AddGrLineV2> newGrLineList = new ArrayList<>();

                if(stagingLineEntityList != null && !stagingLineEntityList.isEmpty()) {
                    for (StagingLineEntityV2 dbStagingLine : stagingLineEntityList) {
                        List<GrLineV2> grLinePresent = grLineList.stream().filter(n->n.getItemCode().equalsIgnoreCase(dbStagingLine.getItemCode()) && n.getManufacturerName().equalsIgnoreCase(dbStagingLine.getManufacturerName())).collect(Collectors.toList());
                        log.info("GrLine Present in cancelled SupplierInvoice : " + grLinePresent);
                        if(grLinePresent != null && !grLinePresent.isEmpty()) {
                            List<PackBarcode> packBarcodeList = new ArrayList<>();
                            AddGrLineV2 newGrLine = new AddGrLineV2();
                            PackBarcode newPackBarcode = new PackBarcode();

                            BeanUtils.copyProperties(dbStagingLine, newGrLine, CommonUtils.getNullPropertyNames(dbStagingLine));

                            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
                            long NUM_RAN_ID = 6;
                            String nextRangeNumber = getNextRangeNumber(NUM_RAN_ID, dbStagingLine.getCompanyCode(),
                                    dbStagingLine.getPlantId(), dbStagingLine.getLanguageId(), dbStagingLine.getWarehouseId(), authTokenForIDMasterService.getAccess_token());

                            boolean capacityCheck = false;
                            boolean storageBinCapacityCheck = false;

                            ImBasicData imBasicData = new ImBasicData();
                            imBasicData.setCompanyCodeId(dbStagingLine.getCompanyCode());
                            imBasicData.setPlantId(dbStagingLine.getPlantId());
                            imBasicData.setLanguageId(dbStagingLine.getLanguageId());
                            imBasicData.setWarehouseId(dbStagingLine.getWarehouseId());
                            imBasicData.setItemCode(dbStagingLine.getItemCode());
                            imBasicData.setManufacturerName(dbStagingLine.getManufacturerName());
                            ImBasicData1 itemCodeCapacityCheck = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
                            log.info("ImbasicData1 : " + itemCodeCapacityCheck);
                            if (itemCodeCapacityCheck.getCapacityCheck() != null) {
                                capacityCheck = itemCodeCapacityCheck.getCapacityCheck();
                                log.info("capacity Check: " + capacityCheck);
                            }

                            newPackBarcode.setQuantityType("A");
                            newPackBarcode.setBarcode(nextRangeNumber);

                            if (capacityCheck) {

                                if (dbStagingLine.getOrderQty() != null) {
                                    orderQty = dbStagingLine.getOrderQty();
                                }
                                if (itemCodeCapacityCheck.getLength() != null) {
                                    itemLength = itemCodeCapacityCheck.getLength();
                                }
                                if (itemCodeCapacityCheck.getWidth() != null) {
                                    itemWidth = itemCodeCapacityCheck.getWidth();
                                }
                                if (itemCodeCapacityCheck.getHeight() != null) {
                                    itemHeight = itemCodeCapacityCheck.getHeight();
                                }

                                cbmPerQty = itemLength * itemWidth * itemHeight;
                                cbm = orderQty * cbmPerQty;

                                newPackBarcode.setCbmQuantity(cbmPerQty);
                                newPackBarcode.setCbm(cbm);

                                log.info("item Length, Width, Height, Volume[CbmPerQty], CBM: " + itemLength + ", " + itemWidth + "," + itemHeight + ", " + cbmPerQty + ", " + cbm);
                            }
                            if (!capacityCheck) {

                                newPackBarcode.setCbmQuantity(0D);
                                newPackBarcode.setCbm(0D);
                            }

                            packBarcodeList.add(newPackBarcode);

                            newGrLine.setGoodReceiptQty(dbStagingLine.getOrderQty());
                            newGrLine.setAcceptedQty(dbStagingLine.getOrderQty());
                            newGrLine.setGoodsReceiptNo(grHeader.getGoodsReceiptNo());
                            newGrLine.setManufacturerFullName(dbStagingLine.getManufacturerFullName());
                            newGrLine.setReferenceDocumentType(dbStagingLine.getReferenceDocumentType());
                            newGrLine.setPurchaseOrderNumber(dbStagingLine.getPurchaseOrderNumber());
                            if (dbStagingLine.getPartner_item_barcode() != null) {
                                newGrLine.setBarcodeId(dbStagingLine.getPartner_item_barcode());
                            }
                            if (dbStagingLine.getPartner_item_barcode() == null) {
                                newGrLine.setBarcodeId(dbStagingLine.getManufacturerName() + dbStagingLine.getItemCode());
                            }

                            newGrLine.setAssignedUserId(grLinePresent.get(0).getAssignedUserId());

                            newGrLine.setPackBarcodes(packBarcodeList);

                            newGrLineList.add(newGrLine);
                        }
                    }
                }

            List<GrLineV2> createGrLine = null;
            try {
                if(newGrLineList != null && !newGrLineList.isEmpty()) {
                    createGrLine = grLineService.createGrLineV2(newGrLineList, grHeader.getCreatedBy());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            log.info("GrLine Created Successfully: " + createGrLine);

                List<PutAwayLineV2> createdPutawayLine = null;
                //putaway Confirm
                if (createGrLine != null && !createGrLine.isEmpty()) {
                    log.info("Putaway line Confirm Initiated");
                    List<PutAwayLineV2> createPutawayLine = new ArrayList<>();

                    List<PutAwayHeaderV2> dbPutawayHeaderList = putAwayHeaderService.getPutAwayHeaderV2(
                            companyCode,
                            plantId,
                            languageId,
                            warehouseId,
                            newInvoiceNo);
                    log.info("Putaway header: " + dbPutawayHeaderList);

                    if (dbPutawayHeaderList != null && !dbPutawayHeaderList.isEmpty()) {
                        for (PutAwayHeaderV2 dbPutawayHeader : dbPutawayHeaderList) {
                            List<PutAwayLineV2> putAwayLinePresent = putAwayLineV2List.stream().filter(n -> n.getItemCode().equalsIgnoreCase(dbPutawayHeader.getReferenceField5()) && n.getManufacturerName().equalsIgnoreCase(dbPutawayHeader.getManufacturerName())).collect(Collectors.toList());
                            log.info("PutawayLine Present : " + putAwayLinePresent);
                            if (putAwayLinePresent != null && !putAwayLinePresent.isEmpty()) {

                                PutAwayLineV2 putAwayLine = new PutAwayLineV2();

                                List<GrLineV2> grLine = createGrLine.stream().filter(n -> n.getPackBarcodes() == dbPutawayHeader.getPackBarcodes()).collect(Collectors.toList());

                                BeanUtils.copyProperties(grLine.get(0), putAwayLine, CommonUtils.getNullPropertyNames(grLine.get(0)));
                                putAwayLine.setProposedStorageBin(dbPutawayHeader.getProposedStorageBin());
                                putAwayLine.setConfirmedStorageBin(dbPutawayHeader.getProposedStorageBin());
                                putAwayLine.setPutawayConfirmedQty(dbPutawayHeader.getPutAwayQuantity());
                                putAwayLine.setPutAwayNumber(dbPutawayHeader.getPutAwayNumber());
                                createPutawayLine.add(putAwayLine);
                            }
                        }
                    }
                    try {
                        createdPutawayLine = putAwayLineService.putAwayLineConfirmV2(createPutawayLine, grHeader.getCreatedBy());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    log.info("PutawayLine Confirmed: " + createdPutawayLine);
                }
            }
        }
}
