package com.tekclover.wms.core.controller;


import com.tekclover.wms.core.model.spark.*;
import com.tekclover.wms.core.model.transaction.QualityHeader;
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

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-spark-service")
@Api(tags = { "Spark Services" }, value = "Spark Services") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "Spark", description = "Operations related to SparkService") })
public class SparkServiceController {

@Autowired
private SparkService sparkService;

    @ApiOperation(response = InventoryMovement[].class, value = "Find InventoryMovemt")//label for swagger
    @PostMapping("/inventorymovement/findInventoryMovement")
    public InventoryMovement[] findInventoryMovement(@RequestBody FindInventoryMovement findInventoryMovement) throws Exception {
        return sparkService.findInventoryMovement(findInventoryMovement);
    }

    @ApiOperation(response = InventoryMovementV2[].class, value = "Find InventoryMovemtV2")//label for swagger
    @PostMapping("/inventorymovementv2/findInventoryMovementV2")
    public InventoryMovementV2[] findInventoryMovementV2(@RequestBody FindInventoryMovement findInventoryMovement) throws Exception {
        return sparkService.findInventoryMovementV2(findInventoryMovement);
    }

    @ApiOperation(response = ImBasicData1.class, value = "Get All ImBasicData1 details")
    @PostMapping("/imbasicdata1")
    public ResponseEntity<?> findImBasicData1(@RequestBody FindImBasicData1 findImBasicData1) {
        ImBasicData1[] imBasicData1s = sparkService.findImBasicData1(findImBasicData1);
        return new ResponseEntity<>(imBasicData1s, HttpStatus.OK);
    }


    @ApiOperation(response = PutAwayHeader[].class, value = "Find PutAway Header")//label for swagger
    @PostMapping("/findPutawayHeader")
    public PutAwayHeader[] findPutAwayHeader(@RequestBody FindPutAwayHeader findPutAwayHeader) throws Exception {
        return sparkService.findPutAwayHeader(findPutAwayHeader);
    }

    @ApiOperation(response = InboundHeader[].class, value = "Find Inbound Header")//label for swagger
    @PostMapping("/findInboundHeader")
    public InboundHeader[] findInboundHeader(@RequestBody FindInboundHeader findInboundHeader) throws Exception {
        return sparkService.findInboundHeader(findInboundHeader);
    }

    @ApiOperation(response = PickupHeader[].class, value = "Find Pickup Header")//label for swagger
    @PostMapping("/findPickupHeader")
    public PickupHeader[] findPickupHeader(@RequestBody FindPickupHeader findPickupHeader) throws Exception {
        return sparkService.findPickupHeader(findPickupHeader);
    }

    @ApiOperation(response = QualityHeader[].class, value = "Find Quality Header")//label for swagger
    @PostMapping("/findQualityHeader")
    public QualityHeader[] findQualityHeader(@RequestBody FindQualityHeader findQualityHeader) throws Exception {
        return sparkService.findQualityHeader(findQualityHeader);
    }

    //Find StagingHeader
    @ApiOperation(response = StagingHeader[].class, value = "Find StagingHeader") //label for swagger
    @PostMapping("/stagingheader/findStagingHeader")
    public StagingHeader[] searchStagingHeader(@RequestBody FindStagingHeader findStagingHeader) throws Exception {
        return sparkService.findStagingHeader(findStagingHeader);
    }

    //Find GrHeader
    @ApiOperation(response = GrHeader[].class, value = "Find GrHeader") //label for swagger
    @PostMapping("/grheader/findGrHeader")
    public GrHeader[] searchGrHeader(@RequestBody FindGrHeader findGrHeader) throws Exception {
        return sparkService.findGrHeader(findGrHeader);
    }

    //Find PreOutboundHeader
    @ApiOperation(response = PreOutboundHeader[].class, value = "Find PreOutboundHeader") //label for swagger
    @PostMapping("/preoutboundheader/findPreOutboundHeader")
    public PreOutboundHeader[] searchPreOutboundHeader(@RequestBody FindPreOutboundHeader findPreOutboundHeader) throws Exception {
        return sparkService.findPreOutboundHeader(findPreOutboundHeader);
    }

    //Find OrderManagementLine
    @ApiOperation(response = OrderManagementLine[].class, value = "Find OrderManagementLine") //label for swagger
    @PostMapping("/ordermanagementline/findOrderManagementLineNew")
    public OrderManagementLine[] searchOrderManagementLine(@RequestBody FindOrderManagementLine findOrderManagementLine)
            throws Exception {
        return sparkService.findOrderManagementLine(findOrderManagementLine);
    }

    //findPreInboundHeader
    @ApiOperation(response = PreInboundHeader[].class, value = "Find PreInbound Header")//label for swagger
    @PostMapping("/preinboundheader/findPreInboundHeader")
    public PreInboundHeader[] findPreInboundHeader(@RequestBody FindPreInboundHeader findPreInboundHeader) throws Exception {
        return sparkService.findPreInboundHeader(findPreInboundHeader);
    }
    //findOutboundHeader
    @ApiOperation(response = OutBoundHeader[].class, value = "Find Outbound Header")//label for swagger
    @PostMapping("/outboundheader/findOutboundHeader")
    public OutBoundHeader[] findOutboundHeader(@RequestBody FindOutBoundHeader findOutBoundHeader) throws Exception {
        return sparkService.findOutboundHeader(findOutBoundHeader);
    }

    //findOutboundReversal
    @ApiOperation(response = OutBoundReversal[].class, value = "Find Outbound Reversal")//label for swagger
    @PostMapping("/outboundreversal/findOutboundReversal")
    public OutBoundReversal[] findOutBoundReversal(@RequestBody FindOutBoundReversal findOutBoundReversal) throws Exception {
        return sparkService.findOutBoundReversal(findOutBoundReversal);
    }

    //findContainer Receipt
    @ApiOperation(response = ContainerReceipt[].class, value = "Find Outbound Reversal")//label for swagger
    @PostMapping("/containerReceipt/findContainerReceipt")
    public ContainerReceipt[] findContainerReceipt(@RequestBody FindContainerReceipt findContainerReceipt) throws Exception {
        return sparkService.findContainerReceipt(findContainerReceipt);
    }

    //Find OrderManagementLine
    @ApiOperation(response = OutboundHeaderOutput[].class, value = "Find OutboundHeaderOutput") //label for swagger
    @PostMapping("/outboundheaderoutput/find")
    public OutboundHeaderOutput[] searchOutboundHeaderOutput(@RequestBody FindOutBoundHeader findOutBoundHeader)
            throws Exception {
        return sparkService.findOutboundHeaderOutput(findOutBoundHeader);
    }
}
