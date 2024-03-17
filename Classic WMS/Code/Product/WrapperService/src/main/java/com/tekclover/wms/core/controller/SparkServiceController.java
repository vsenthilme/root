package com.tekclover.wms.core.controller;


import com.tekclover.wms.core.model.enterprise.Company;
import com.tekclover.wms.core.model.enterprise.SearchCompany;
import com.tekclover.wms.core.model.spark.*;

import com.tekclover.wms.core.service.SparkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/wms-spark-service")
@Api(tags = { "Spark Service" }, value = "Spark Service Operations") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "User", description = "Operations related to Enterprise Modules") })
public class SparkServiceController {

    @Autowired
    SparkService sparkService;


    @ApiOperation(response = StorageBin.class, value = "Search Storage Bin") // label for swagger
    @PostMapping("/storagebin/findStorageBin")
    public ResponseEntity<?> findStorageBin(@RequestBody FindStorageBin findStorageBin)
            throws ParseException {
        StorageBin[] storageBin = sparkService.findStorageBin(findStorageBin);
        return new ResponseEntity<>(storageBin, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Search Storage Bin") // label for swagger
    @PostMapping("/stagingline")
    public ResponseEntity<?> findStagingLine(@RequestBody FindStagingLine findStagingLine)
            throws ParseException {
        StagingLine[] stagingLine = sparkService.findStagingLine(findStagingLine);
        return new ResponseEntity<>(stagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingHeader.class, value = "Search Quality Header") // label for swagger
    @PostMapping("/stagingheader")
    public ResponseEntity<?> findStagingHeaderV2(@RequestBody FindStagingHeaderV2 findStagingHeaderV2)
            throws ParseException {
        StagingHeader[] stagingHeader = sparkService.findStagingHeader(findStagingHeaderV2);
        return new ResponseEntity<>(stagingHeader, HttpStatus.OK);
    }

    @ApiOperation(response = QualityHeader.class, value = "Search Quality Header") // label for swagger
    @PostMapping("/qualityheader")
    public ResponseEntity<?> findQualityHeader(@RequestBody FindQualityHeader findQualityHeader)
            throws ParseException {
        QualityHeader[] qualityHeader = sparkService.findQualityHeader(findQualityHeader);
        return new ResponseEntity<>(qualityHeader, HttpStatus.OK);
    }

    @ApiOperation(response = QualityLine.class, value = "Search Quality Line") // label for swagger
    @PostMapping("/qualityline")
    public ResponseEntity<?> findQualityLine(@RequestBody FindQualityLine findQualityLine)
            throws ParseException {
        QualityLine[] qualityLine = sparkService.findQualityLine(findQualityLine);
        return new ResponseEntity<>(qualityLine, HttpStatus.OK);
    }

@ApiOperation(response = PutAwayLine.class, value = "Search Putaway Line") // label for swagger
@PostMapping("/putawayline")
public ResponseEntity<?> findPutawayLine(@RequestBody FindPutAwayLine findPutawayLine)
        throws ParseException {
    PutAwayLine[] putAwayLine = sparkService.findPutAwayLine(findPutawayLine);
    return new ResponseEntity<>(putAwayLine, HttpStatus.OK);
}

@ApiOperation(response = PutAwayHeader.class, value = "Search Putaway Header") // label for swagger
@PostMapping("/putawayheader")
public ResponseEntity<?> findPutAwayHeader(@RequestBody FindPutAwayHeader findPutAwayHeader)
        throws ParseException {
    PutAwayHeader[] putAwayHeader = sparkService.findPutAwayHeader(findPutAwayHeader);
    return new ResponseEntity<>(putAwayHeader, HttpStatus.OK);
}

    @ApiOperation(response = ContainerReceipt.class, value = "Spark ContainerReceipt details")
    @PostMapping("/containerReceipt")
    public ResponseEntity<?> findContainerReceipt(@RequestBody SearchContainerReceipt searchContainerReceipt) throws Exception {
        ContainerReceipt[] containerReceipt = sparkService.findContainerReceipt(searchContainerReceipt);
        return new ResponseEntity<>(containerReceipt, HttpStatus.OK);
    }

    @ApiOperation(response = GrHeader.class, value = "Spark GrHeader details")
    @PostMapping("/grHeader")
    public ResponseEntity<?> findGrHeader(@RequestBody SearchGrHeader searchGrHeader) throws Exception {
        GrHeader[] grHeader = sparkService.findGrHeader(searchGrHeader);
        return new ResponseEntity<>(grHeader, HttpStatus.OK);
    }

    @ApiOperation(response = GrLine.class, value = "Spark GrLine details")
    @PostMapping("/grLine")
    public ResponseEntity<?> findGrLine(@RequestBody SearchGrLine searchGrLine) throws Exception {
        GrLine[] grLine = sparkService.findGrLine(searchGrLine);
        return new ResponseEntity<>(grLine, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Spark InboundHeader details")
    @PostMapping("/inboundHeader")
    public ResponseEntity<?> findInBoundHeader(@RequestBody SearchInboundHeader searchInboundHeader) throws Exception {
        InboundHeader[] inboundHeader = sparkService.findInboundHeader(searchInboundHeader);
        return new ResponseEntity<>(inboundHeader, HttpStatus.OK);
    }

    @ApiOperation(response = InboundLine.class, value = "Spark InboundLine details")
    @PostMapping("/inboundline")
    public ResponseEntity<?> findInBoundLine(@RequestBody SearchInboundLine searchInboundLine) throws Exception {
        InboundLine[] inboundLine = sparkService.findInboundLine(searchInboundLine);
        return new ResponseEntity<>(inboundLine, HttpStatus.OK);
    }

    @ApiOperation(response = InhouseTransferHeader.class, value = "Spark InhouseTransferHeader details")
    @PostMapping("/inhouseTransferHeader")
    public ResponseEntity<?> findInBoundLine(@RequestBody SearchInhouseTransferHeader searchInhouseTransferHeader) throws Exception {
        InhouseTransferHeader[] inhouseTransferHeader = sparkService.findInhouseTransferHeader(searchInhouseTransferHeader);
        return new ResponseEntity<>(inhouseTransferHeader, HttpStatus.OK);
    }

    @ApiOperation(response = InhouseTransferLine.class, value = "Spark InhouseTransferLine details")
    @PostMapping("/inhouseTransferLine")
    public ResponseEntity<?> findInBoundLine(@RequestBody SearchInhouseTransferLine searchInhouseTransferLine) throws Exception {
        InhouseTransferLine[] inhouseTransferLine = sparkService.findInhouseTransferLine(searchInhouseTransferLine);
        return new ResponseEntity<>(inhouseTransferLine, HttpStatus.OK);
    }

    @ApiOperation(response = InventoryMovement.class, value = "Spark InventoryMovement details")
    @PostMapping("/inventoryMovement")
    public ResponseEntity<?> findInventoryMovement(@RequestBody SearchInventoryMovement searchInventoryMovement) throws Exception {
        InventoryMovement[] inventoryMovement = sparkService.findInventoryMovement(searchInventoryMovement);
        return new ResponseEntity<>(inventoryMovement, HttpStatus.OK);
    }

    @ApiOperation(response = Inventory.class, value = "Spark Inventory details")
    @PostMapping("/inventory")
    public ResponseEntity<?> findInventory(@RequestBody SearchInventory searchInventory) throws Exception {
        Inventory[] inventory = sparkService.findInventory(searchInventory);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @ApiOperation(response = OrderManagementLine.class, value = "Spark OrderManagementLine details")
    @PostMapping("/orderManagementLine")
    public ResponseEntity<?> findOrderManagementLine(@RequestBody SearchOrderManagementLine searchOrderManagementLine) throws Exception {
        OrderManagementLine[] orderManagementLine = sparkService.findOrderManagementLine(searchOrderManagementLine);
        return new ResponseEntity<>(orderManagementLine, HttpStatus.OK);
    }

    @ApiOperation(response = OutboundHeader.class, value = "Spark OutboundHeader details")
    @PostMapping("/outboundHeader")
    public ResponseEntity<?> findOutboundHeader(@RequestBody SearchOutboundHeader searchOutboundHeader) throws Exception {
        OutboundHeader[] outboundHeader = sparkService.findOutboundHeader(searchOutboundHeader);
        return new ResponseEntity<>(outboundHeader, HttpStatus.OK);
    }

    @ApiOperation(response = OutboundReversal.class, value = "Spark OutboundReversal details")
    @PostMapping("/outboundReversal")
    public ResponseEntity<?> findOutboundReversal(@RequestBody SearchOutboundReversal searchOutboundReversal) throws Exception {
        OutboundReversal[] outboundReversal = sparkService.findOutboundReversal(searchOutboundReversal);
        return new ResponseEntity<>(outboundReversal, HttpStatus.OK);
    }

    @ApiOperation(response = PeriodicHeader.class, value = "Spark PeriodicHeader details")
    @PostMapping("/periodicHeader")
    public ResponseEntity<?> findPeriodicHeader(@RequestBody SearchPeriodicHeader searchPeriodicHeader) throws Exception {
        PeriodicHeader[] periodicHeader = sparkService.findPeriodicHeader(searchPeriodicHeader);
        return new ResponseEntity<>(periodicHeader, HttpStatus.OK);
    }

    @ApiOperation(response = PerpetualHeader.class, value = "Spark PerpetualHeader details")
    @PostMapping("/perpetualHeader")
    public ResponseEntity<?> findPerpetualHeader(@RequestBody SearchPerpetualHeader searchPerpetualHeader) throws Exception {
        PerpetualHeader[] perpetualHeader = sparkService.findPerpetualHeader(searchPerpetualHeader);
        return new ResponseEntity<>(perpetualHeader, HttpStatus.OK);
    }

    @ApiOperation(response = PerpetualLine.class, value = "Spark PerpetualLine details")
    @PostMapping("/perpetualLine")
    public ResponseEntity<?> findPerpetualLine(@RequestBody SearchPerpetualLine searchPerpetualLine) throws Exception {
        PerpetualLine[] perpetualLine = sparkService.findPerpetualLine(searchPerpetualLine);
        return new ResponseEntity<>(perpetualLine, HttpStatus.OK);
    }

    @ApiOperation(response = PickupHeader.class, value = "Spark PickupHeader details")
    @PostMapping("/pickupHeader")
    public ResponseEntity<?> findPickupHeader(@RequestBody SearchPickupHeader searchPickupHeader) throws Exception {
        PickupHeader[] pickupHeader = sparkService.findPickupHeader(searchPickupHeader);
        return new ResponseEntity<>(pickupHeader, HttpStatus.OK);
    }

    @ApiOperation(response = PickupLine.class, value = "Spark PickupLine details")
    @PostMapping("/pickupLine")
    public ResponseEntity<?> findPickupLine(@RequestBody SearchPickupLine searchPickupLine) throws Exception {
        PickupLine[] pickupLine = sparkService.findPickupLine(searchPickupLine);
        return new ResponseEntity<>(pickupLine, HttpStatus.OK);
    }
    @ApiOperation(response = PreInboundHeader.class, value = "Spark PreInboundHeader details")
    @PostMapping("/preInboundHeader")
    public ResponseEntity<?> findPreInboundHeader(@RequestBody SearchPreInboundHeader searchPreInboundHeader) throws Exception {
        PreInboundHeader[] preInboundHeader = sparkService.findPreInboundHeader(searchPreInboundHeader);
        return new ResponseEntity<>(preInboundHeader, HttpStatus.OK);
    }
    @ApiOperation(response = PreOutboundHeader.class, value = "Spark PreOutboundHeader details")
    @PostMapping("/preOutboundHeader")
    public ResponseEntity<?> findPreOutboundHeader(@RequestBody SearchPreOutboundHeader searchPreOutboundHeader) throws Exception {
        PreOutboundHeader[] preOutboundHeader = sparkService.findPreOutboundHeader(searchPreOutboundHeader);
        return new ResponseEntity<>(preOutboundHeader, HttpStatus.OK);
    }

    @ApiOperation(response = ImBasicData1.class, value = "Spark ImBasicData1 details")
    @PostMapping("/imbasicdata1")
    public ResponseEntity<?> findImBasicData1(@RequestBody SearchImBasicData1 searchImBasicData1) throws Exception {
        ImBasicData1[] imBasicData1s = sparkService.findImBasicData1(searchImBasicData1);
        return new ResponseEntity<>(imBasicData1s, HttpStatus.OK);
    }
}
