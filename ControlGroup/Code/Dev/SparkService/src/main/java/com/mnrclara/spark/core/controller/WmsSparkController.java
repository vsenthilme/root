package com.mnrclara.spark.core.controller;

import com.mnrclara.spark.core.model.wmscore.*;
import com.mnrclara.spark.core.service.QualityHeaderService;
import com.mnrclara.spark.core.service.wmscore.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = {"User"}, value = "User Operations related to UserController")
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Operations related to User")})
@RequestMapping("/wms")
public class WmsSparkController {

    @Autowired
    StagingHeaderServiceSpark stagingHeaderServiceSpark;
    @Autowired
    StorageBinServiceSpark storageBinServiceSpark;

    @Autowired
    StagingLineServiceSpark stagingLineServiceSpark;

    @Autowired
    QualityHeaderServiceSpark qualityHeaderServiceSpark;

    @Autowired
    QualityLineServiceSpark qualityLineServiceSpark;

    @Autowired
    PutAwayLineServiceSpark putAwayLineServiceSpark;

    @Autowired
    PutAwayHeaderServiceSpark putAwayHeaderServiceSpark;

    @Autowired
    ContainerReceiptServiceV2 containerreceiptServiceV2;

    @Autowired
    GrHeaderServiceV2 grHeaderServiceV2;

    @Autowired
    GrLineServiceV2 grLineServiceV2;

    @Autowired
    InboundHeaderServiceV2 inboundHeaderServiceV2;

    @Autowired
    InboundLineServiceV2 inboundLineServiceV2;

    @Autowired
    InhouseTransferHeaderServiceV2 inhouseTransferHeaderServiceV2;

    @Autowired
    InhouseTransferLineServiceV2 inhouseTransferLineServiceV2;

    @Autowired
    InventoryMovementServiceV2 inventoryMovementServiceV2;

    @Autowired
    InventoryServiceV2 inventoryServiceV2;

    @Autowired
    OrderManagementLineServiceV2 orderManagementLineServiceV2;

    @Autowired
    OutboundHeaderServiceV2 outboundHeaderServiceV2;

    @Autowired
    OutboundReversalServiceV2 outboundReversalServiceV2;

    @Autowired
    PeriodicHeaderServiceV2 periodicHeaderServiceV2;

    @Autowired
    PerpetualHeaderServiceV2 perpetualHeaderServiceV2;

    @Autowired
    PerpetualLineServiceV2 perpetualLineServiceV2;

    @Autowired
    PickupHeaderServiceV2 pickupHeaderServiceV2;

    @Autowired
    PickupLineServiceV2 pickupLineServiceV2;

    @Autowired
    PreInboundHeaderServiceV2 preInboundHeaderServiceV2;

    @Autowired
    PreOutboundHeaderServiceV2 preOutboundHeaderServiceV2;


    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/storagebin")
    public ResponseEntity<?> findStorageBin(@RequestBody FindStorageBin findStorageBin) throws Exception {
        List<StorageBin> storageBin = storageBinServiceSpark.findStorageBin(findStorageBin);
        return new ResponseEntity<>(storageBin, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/stagingline")
    public ResponseEntity<?> findStagingLine(@RequestBody FindStagingLine findStagingLine) throws Exception {
        List<StagingLine> stagingLine = stagingLineServiceSpark.findStagingLine(findStagingLine);
        return new ResponseEntity<>(stagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/stagingheader")
    public ResponseEntity<?> findStagingHeader(@RequestBody FindStagingHeaderV2 findStagingHeader) throws Exception {
        List<StagingHeader> stagingHeader = stagingHeaderServiceSpark.findStagingHeader(findStagingHeader);
        return new ResponseEntity<>(stagingHeader, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/qualityheader")
    public ResponseEntity<?> findQualityheader(@RequestBody FindQualityHeader findQualityHeader) throws Exception {
        List<QualityHeader> stagingHeader = qualityHeaderServiceSpark.findQualityHeader(findQualityHeader);
        return new ResponseEntity<>(stagingHeader, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/qualityline")
    public ResponseEntity<?> findQualityline(@RequestBody FindQualityLine findQualityLine) throws Exception {
        List<QualityLine> qualityline = qualityLineServiceSpark.findQualityLine(findQualityLine);
        return new ResponseEntity<>(qualityline, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/putawayline")
    public ResponseEntity<?> findQualityline(@RequestBody FindPutAwayLine findPutAwayLine) throws Exception {
        List<PutAwayLine> putawayline = putAwayLineServiceSpark.findPutAwayLine(findPutAwayLine);
        return new ResponseEntity<>(putawayline, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Test")
    @PostMapping("/putawayheader")
    public ResponseEntity<?> findPutawayheader(@RequestBody FindPutAwayHeader findPutAwayHeader) throws Exception {
        List<PutAwayHeader> putawayheader = putAwayHeaderServiceSpark.findPutAwayHeader(findPutAwayHeader);
        return new ResponseEntity<>(putawayheader, HttpStatus.OK);
    }

    @ApiOperation(response = ContainerReceipt.class, value = "Search ContainerReceipt") // label for swagger
    @PostMapping("/spark/findContainerReceipt")
    public List<ContainerReceipt> findContainerReceipt(@RequestBody SearchContainerReceipt searchContainerReceipt)
            throws Exception {
        return containerreceiptServiceV2.findContainerReceiptV3(searchContainerReceipt);
    }

    @ApiOperation(response = GrHeader.class, value = "Search GrHeader") // label for swagger
    @PostMapping("/spark/findGrHeader")
    public List<GrHeader> findGrHeader(@RequestBody SearchGrHeader searchGrHeader)
            throws Exception {
        return grHeaderServiceV2.findGrHeaderV3(searchGrHeader);
    }

    @ApiOperation(response = GrLine.class, value = "Search GrLine") // label for swagger
    @PostMapping("/spark/findGrLine")
    public List<GrLine> findGrLine(@RequestBody SearchGrLine searchGrLine)
            throws Exception {
        return grLineServiceV2.findGrLineV3(searchGrLine);
    }

    @ApiOperation(response = InboundHeader.class, value = "Search InboundHeader") // label for swagger
    @PostMapping("/spark/findInboundHeader")
    public List<InboundHeader> findInboundHeader(@RequestBody SearchInboundHeader searchInboundHeader)
            throws Exception {
        return inboundHeaderServiceV2.findInboundHeaderV3(searchInboundHeader);
    }

    @ApiOperation(response = InboundLine.class, value = "Search InboundLine") // label for swagger
    @PostMapping("/spark/findInboundLine")
    public List<InboundLine> findInboundLine(@RequestBody SearchInboundLine searchInboundLine)
            throws Exception {
        return inboundLineServiceV2.findInboundLineV3(searchInboundLine);
    }

    @ApiOperation(response = InhouseTransferHeader.class, value = "Search InhouseTransferHeader") // label for swagger
    @PostMapping("/spark/findInhouseTransferHeader")
    public List<InhouseTransferHeader> findInhouseTransferHeader(@RequestBody SearchInhouseTransferHeader searchInhouseTransferHeader)
            throws Exception {
        return inhouseTransferHeaderServiceV2.findInhouseTransferHeaderV3(searchInhouseTransferHeader);
    }

    @ApiOperation(response = InhouseTransferLine.class, value = "Search InhouseTransferLine") // label for swagger
    @PostMapping("/spark/findInhouseTransferLine")
    public List<InhouseTransferLine> findInhouseTransferLine(@RequestBody SearchInhouseTransferLine searchInhouseTransferLine)
            throws Exception {
        return inhouseTransferLineServiceV2.findInhouseTransferLinesV3(searchInhouseTransferLine);
    }

    @ApiOperation(response = InventoryMovement.class, value = "Search InventoryMovement") // label for swagger
    @PostMapping("/spark/findInventoryMovement")
    public List<InventoryMovement> findInventoryMovement(@RequestBody SearchInventoryMovement searchInventoryMovement)
            throws Exception {
        return inventoryMovementServiceV2.findInventoryMovementV3(searchInventoryMovement);
    }

    @ApiOperation(response = Inventory.class, value = "Search Inventory") // label for swagger
    @PostMapping("/spark/findInventory")
    public List<Inventory> findInventory(@RequestBody SearchInventory searchInventory)
            throws Exception {
        return inventoryServiceV2.findInventoryV3(searchInventory);
    }

    @ApiOperation(response = OrderManagementLine.class, value = "Search OrderManagementLine") // label for swagger
    @PostMapping("/spark/findOrderManagementLine")
    public List<OrderManagementLine> findOrderManagementLine(@RequestBody SearchOrderManagementLine searchOrderManagementLine)
            throws Exception {
        return orderManagementLineServiceV2.findOrderManagementLineV3(searchOrderManagementLine);
    }
    @ApiOperation(response = OutboundHeader.class, value = "Search OutboundHeader") // label for swagger
    @PostMapping("/spark/findOutboundHeader")
    public List<OutboundHeader> findOutboundHeader(@RequestBody SearchOutboundHeader searchOutboundHeader)
            throws Exception {
        return outboundHeaderServiceV2.findOutboundHeaderV3(searchOutboundHeader);
    }

    @ApiOperation(response = OutboundReversal.class, value = "Search OutboundReversal") // label for swagger
    @PostMapping("/spark/findOutboundReversal")
    public List<OutboundReversal> findOutboundReversal(@RequestBody SearchOutboundReversal searchOutboundReversal)
            throws Exception {
        return outboundReversalServiceV2.findOutboundReversalV3(searchOutboundReversal);
    }

    @ApiOperation(response = PeriodicHeader.class, value = "Search PeriodicHeader") // label for swagger
    @PostMapping("/spark/findPeriodicHeader")
    public List<PeriodicHeader> findPeriodicHeader(@RequestBody SearchPeriodicHeader searchPeriodicHeader)
            throws Exception {
        return periodicHeaderServiceV2.findPeriodicHeaderV3(searchPeriodicHeader);
    }

    @ApiOperation(response = PerpetualHeader.class, value = "Search PerpetualHeader") // label for swagger
    @PostMapping("/spark/findPerpetualHeader")
    public List<PerpetualHeader> findPerpetualHeader(@RequestBody SearchPerpetualHeader searchPerpetualHeader)
            throws Exception {
        return perpetualHeaderServiceV2.findPerpetualHeaderV3(searchPerpetualHeader);
    }

    @ApiOperation(response = PerpetualLine.class, value = "Search PerpetualLine") // label for swagger
    @PostMapping("/spark/findPerpetualLine")
    public List<PerpetualLine> findPerpetualLine(@RequestBody SearchPerpetualLine searchPerpetualLine)
            throws Exception {
        return perpetualLineServiceV2.findPerpetualLineV3(searchPerpetualLine);
    }

    @ApiOperation(response = PickupHeader.class, value = "Search PickupHeader") // label for swagger
    @PostMapping("/spark/findPickupHeader")
    public List<PickupHeader> findPickupHeader(@RequestBody SearchPickupHeader searchPickupHeader)
            throws Exception {
        return pickupHeaderServiceV2.findPickupHeaderV3(searchPickupHeader);
    }

    @ApiOperation(response = PickupHeader.class, value = "Search PickupLine") // label for swagger
    @PostMapping("/spark/findPickupLine")
    public List<PickupLine> findPickupLine(@RequestBody SearchPickupLine searchPickupLine)
            throws Exception {
        return pickupLineServiceV2.findPickupLineV3(searchPickupLine);
    }

    @ApiOperation(response = PreInboundHeader.class, value = "Search PreInboundHeader") // label for swagger
    @PostMapping("/spark/findPreInboundHeader")
    public List<PreInboundHeader> findPreInboundHeader(@RequestBody SearchPreInboundHeader searchPreInboundHeader)
            throws Exception {
        return preInboundHeaderServiceV2.findPreInboundHeaderV3(searchPreInboundHeader);
    }

    @ApiOperation(response = PreOutboundHeader.class, value = "Search PreOutboundHeader") // label for swagger
    @PostMapping("/spark/findPreOutboundHeader")
    public List<PreOutboundHeader> findPreOutboundHeader(@RequestBody SearchPreOutboundHeader searchPreOutboundHeader)
            throws Exception {
        return preOutboundHeaderServiceV2.findPreOutboundHeaderV3(searchPreOutboundHeader);
    }

}
