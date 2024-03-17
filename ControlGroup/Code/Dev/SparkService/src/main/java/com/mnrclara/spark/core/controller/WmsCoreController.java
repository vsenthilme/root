package com.mnrclara.spark.core.controller;


import com.mnrclara.spark.core.model.mnrclara.ClientGeneral;
import com.mnrclara.spark.core.model.wmscorev2.*;
import com.mnrclara.spark.core.service.clara.ClientGeneralNewService;
import com.mnrclara.spark.core.model.mnrclara.FindClientGeneral;
import com.mnrclara.spark.core.service.download.DownloadInventoryWithBean;
import com.mnrclara.spark.core.service.download.InventoryDownloadService;
import com.mnrclara.spark.core.service.wmscorev2.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = {"User"}, value = "User Operations related to UserController")
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Operations related to User")})
@RequestMapping("/wms/spark")
public class WmsCoreController {

    @Autowired
    SparkPreInboundHeaderServiceV2 sparkPreInboundHeaderService;

    @Autowired
    SparkOrderManagementLineServiceV2 sparkOrderManagementLineService;

    @Autowired
    SparkContainerReceiptServiceV2 sparkContainerReceiptService;

    @Autowired
    SparkGrHeaderServiceV2 sparkGrHeaderService;

    @Autowired
    SparkPutAwayHeaderServiceV2 sparkPutAwayHeaderService;

    @Autowired
    SparkStagingHeaderServiceV2 stagingHeaderService;

    @Autowired
    SparkInboundHeaderServiceV2 sparkInboundHeaderService;

    @Autowired
    SparkPreOutboundHeaderServiceV2 sparkPreOutboundHeaderService;

    @Autowired
    SparkOutBoundReversalServiceV2 sparkOutBoundReversalService;

    @Autowired
    SparkOutBoundHeaderServiceV2 sparkOutBoundHeaderService;

    @Autowired
    SparkPickupHeaderServiceV2 sparkPickupHeaderService;

    @Autowired
    SparkQualityHeaderServiceV2 sparkQualityHeaderService;

    @Autowired
    SparkStorageBinServiceV2 sparkStorageBinService;

    @Autowired
    SparkPickupLineServiceV2 sparkPickupLineService;

    @Autowired
    SparkInventoryMovementServiceV2 sparkInventoryMovementService;

    @Autowired
    SparkInhouseTransferLineServiceV2 sparkInhouseTransferLineService;

    @Autowired
    SparkInventoryServiceV2 sparkInventoryService;

    @Autowired
    SparkPutAwayLineServiceV2 sparkPutAwayLineService;

    @Autowired
    SparkPeriodicHeaderServiceV2 sparkPeriodicHeaderService;

    @Autowired
    SparkPerpetualHeaderServiceV2 sparkPerpetualHeaderService;

    @Autowired
    SparkInboundLineServiceV2 sparkInboundLineService;

    @Autowired
    SparkStockReportServiceV2 sparkStockReportService;

    @Autowired
    SparkInhouseTransferHeaderServiceV2 sparkInhouseTransferHeaderService;

    @Autowired
    SparkOrderStatusReportServiceV2 sparkOrderStatusReportService;

    @Autowired
    SparkPerpetualLineServiceV2 sparkPerpetualLineService;

    @Autowired
    SparkPeriodicLineServiceV2 sparkPeriodicLineService;

    @Autowired
    SparkPreOutBoundLineServiceV2 sparkPreOutBoundLineService;

    @Autowired
    SparkStagingLineV2ServiceV2 sparkStagingLineV2Service;

    @Autowired
    SparkGrLineServiceV2 sparkGrLineService;

    @Autowired
    SparkQualityLineServiceV2 sparkQualityLineService;

    @Autowired
    SparkImBasicData1ServiceV2 sparkImBasicData1Service;

    @Autowired
    InventoryDownloadService inventoryDownloadService;

//    @Autowired
//    ClientGeneralService clientGeneralService;

    @Autowired
    ClientGeneralNewService clientGeneralNewService;

    @Autowired
    DownloadInventoryWithBean downloadInventoryWithBean;


    //=======================================================================================================================

    @ApiOperation(response = Optional.class, value = "Spark FindPreInboundHeader")
    @PostMapping("/preinboundHeader")
    public ResponseEntity<?> findPreinboundHeaderv2(@RequestBody FindPreInboundHeaderV2 findPreInboundHeaderV2) throws Exception {
        List<PreInboundHeaderV2> preInboundHeaderv2 = sparkPreInboundHeaderService.findPreInboundHeaderv2(findPreInboundHeaderV2);
        return new ResponseEntity<>(preInboundHeaderv2, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark FindContainerReceipt")
    @PostMapping("/containerReceipt")
    public ResponseEntity<?> findContainerReceiptV2(@RequestBody FindContainerReceiptV2 findContainerReceiptV2) throws Exception {
        List<ContainerReceiptV2> containerReceiptV2 = sparkContainerReceiptService.findContainerReceiptV2(findContainerReceiptV2);
        return new ResponseEntity<>(containerReceiptV2, HttpStatus.OK);
    }

    @ApiOperation(response = Optional.class, value = "Spark Find GrHeader")
    @PostMapping("/grheader")
    public ResponseEntity<?> findGrHeaderV2(@RequestBody SearchGrHeaderV2 searchGrHeaderV2) throws Exception {
        List<GrHeaderV2> findGrHeaderV2 = sparkGrHeaderService.findGrHeaderV2(searchGrHeaderV2);
        return new ResponseEntity<>(findGrHeaderV2, HttpStatus.OK);
    }

    //Find PutAwayHeader
    @ApiOperation(response = Optional.class, value = "Spark Find PutAwayHeader")
    @PostMapping("/putawayheader")
    public ResponseEntity<?> findPutawayHeaderV2(@RequestBody SearchPutAwayHeaderV2 searchPutAwayHeaderV2) throws Exception {
        List<PutAwayHeaderV2> findPutAwayHeaderV2 = sparkPutAwayHeaderService.findPutAwayHeaderV2(searchPutAwayHeaderV2);
        return new ResponseEntity<>(findPutAwayHeaderV2, HttpStatus.OK);
    }

    //Find StagingHeader
    @ApiOperation(response = StagingHeaderV2.class, value = "Spark Find StagingHeader")
    @PostMapping("/stagingheader")
    public ResponseEntity<?> searchStagingHeader(@RequestBody FindStagingHeaderV2 findStagingHeader) throws Exception {
        List<StagingHeaderV2> stList = stagingHeaderService.findStagingHeader(findStagingHeader);
        return new ResponseEntity<>(stList, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeaderV2.class, value = "Spark InboundHeader")
    @PostMapping("/inboundHeader")
    public ResponseEntity<?> findInboundHeader(@RequestBody FindInboundHeaderV2 findInboundHeader) throws Exception {
        List<InboundHeaderV2> inboundHeaderList = sparkInboundHeaderService.findInboundHeader(findInboundHeader);
        return new ResponseEntity<>(inboundHeaderList, HttpStatus.OK);
    }

    /*================================================OUTBOUND=======================================================*/

    //Find PreOutboundHeader
    @ApiOperation(response = PreOutboundHeaderV2.class, value = "Spark Find PreOutboundHeader")
    @PostMapping("/preoutboundheader")
    public ResponseEntity<?> searchPreOutboundHeader(@RequestBody FindPreOutboundHeaderV2 findPreObHeader) throws Exception {
        List<PreOutboundHeaderV2> preObList = sparkPreOutboundHeaderService.findPreOutboundHeader(findPreObHeader);
        return new ResponseEntity<>(preObList, HttpStatus.OK);
    }

    //Find OrderManagementLineV2
    @ApiOperation(response = OrderManagementLineV2.class, value = "Spark Find OrderManagementLine")
    @PostMapping("/ordermanagementline")
    public ResponseEntity<?> searchOrderManagementLineV2(@RequestBody FindOrderManagementLineV2 findOrderManagementLine)
            throws Exception {
        List<OrderManagementLineV2> omlList = sparkOrderManagementLineService.findOrderManagementLine(findOrderManagementLine);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    // PickupHeaderV2
    @ApiOperation(response = PickupHeaderV2.class, value = "Spark Find PickupHeader")
    @PostMapping("/pickupheader")
    public ResponseEntity<?> findPickupHeaderV2(@RequestBody FindPickupHeaderV2 findPickupHeaderV2) throws Exception {
        List<PickupHeaderV2> pickupHeaderV2List = sparkPickupHeaderService.findPickupHeaderV2(findPickupHeaderV2);
        return new ResponseEntity<>(pickupHeaderV2List, HttpStatus.OK);
    }

    // QualityHeaderV2
    @ApiOperation(response = QualityHeaderV2.class, value = "Spark Find QualityHeader")
    @PostMapping("/qualityheader")
    public ResponseEntity<?> findQualityHeaderV2(@RequestBody FindQualityHeaderV2 findQualityHeaderV2) throws Exception {
        List<QualityHeaderV2> qualityHeaderV2List = sparkQualityHeaderService.findQualityHeaderV2(findQualityHeaderV2);
        return new ResponseEntity<>(qualityHeaderV2List, HttpStatus.OK);
    }

    // QualityHeaderV2
    @ApiOperation(response = OutBoundHeaderV2.class, value = "Spark Find OutBoundHeader")
    @PostMapping("/outboundheader")
    public ResponseEntity<?> findOutboundHeaderV2(@RequestBody FindOutBoundHeaderV2 findOutBoundHeaderV2) throws Exception {
        List<OutBoundHeaderV2> outBoundHeaderV2List = sparkOutBoundHeaderService.findOutBoundHeaderV2(findOutBoundHeaderV2);
        return new ResponseEntity<>(outBoundHeaderV2List, HttpStatus.OK);
    }

    //Find OutBoundReversal
    @ApiOperation(response = OutBoundReversalV2.class, value = "Spark Find OutBoundReversal")
    @PostMapping("/outboundreversal")
    public ResponseEntity<?> searchOutboundReversal(@RequestBody FindOutBoundReversalV2 findOutBoundReversalV2)
            throws Exception {
        List<OutBoundReversalV2> omlList = sparkOutBoundReversalService.findOutBoundReversal(findOutBoundReversalV2);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    //Find StorageBin
    @ApiOperation(response = StorageBin.class, value = "Spark Find StorageBin")
    @PostMapping("/storagebin")
    public ResponseEntity<?> searchStorageBin(@RequestBody SearchStorageBin searchStorageBin)
            throws Exception {
        List<StorageBin> omlList = sparkStorageBinService.findStorageBin(searchStorageBin);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    //Find PickupLine
    @ApiOperation(response = PickupLine.class, value = "Spark Find PickUpLine")
    @PostMapping("/pickupline")
    public ResponseEntity<?> searchPickUpLine(@RequestBody SearchPickupLineV2 searchStorageBin)
            throws Exception {
        List<PickupLine> omlList = sparkPickupLineService.findPickupLines(searchStorageBin);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    //Find InventoryMovement
    @ApiOperation(response = InventoryMovementV2.class, value = "Spark Find InventoryMovement")
    @PostMapping("/inventorymovement")
    public ResponseEntity<?> searchInventoryMovement(@RequestBody SearchInventoryMovementV2 searchInventoryMovementV2)
            throws Exception {
        List<InventoryMovementV2> omlList = sparkInventoryMovementService.findInventoryMovement(searchInventoryMovementV2);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    //Find InhouseTransferLine
    @ApiOperation(response = InhouseTransferLine.class, value = "Spark Find InhouseTransferLine")
    @PostMapping("/inhousetransferline")
    public ResponseEntity<?> searchInhouseTransferLine(@RequestBody SearchInhouseTransferLine searchInhouseTransferLine)
            throws Exception {
        List<InhouseTransferLine> omlList = sparkInhouseTransferLineService.findInhouseTransferLines(searchInhouseTransferLine);
        return new ResponseEntity<>(omlList, HttpStatus.OK);
    }

    // Find InventoryV2
    @ApiOperation(response = InventoryV2.class, value = "Spark Find Inventory")
    @PostMapping("/inventory")
    public ResponseEntity<?> searchInventoryV2(@RequestBody FindInventoryV2 findInventoryV2) throws Exception {
        List<InventoryV2> inventoryList = sparkInventoryService.findInventoryV2(findInventoryV2);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    // Find PutAwayLineV2
    @ApiOperation(response = PutAwayLineV2.class, value = "Spark Find PutAwayLine")
    @PostMapping("/putawayline")
    public ResponseEntity<?> searchPutAwayLineV2(@RequestBody FindPutAwayLineV2 findPutAwayLineV2) throws Exception {
        List<PutAwayLineV2> putAwayLineList = sparkPutAwayLineService.findPutAwayLineV2(findPutAwayLineV2);
        return new ResponseEntity<>(putAwayLineList, HttpStatus.OK);
    }

    // Find PeriodicHeader
    @ApiOperation(response = PeriodicHeaderV2.class, value = "Spark Find PeriodicHeader")
    @PostMapping("/periodicheader")
    public ResponseEntity<?> searchPeriodicHeaderV2(@RequestBody FindPeriodicHeaderV2 findPeriodicHeaderV2) throws Exception {
        List<PeriodicHeaderV2> periodicHeaderList = sparkPeriodicHeaderService.findPeriodicHeaderV2(findPeriodicHeaderV2);
        return new ResponseEntity<>(periodicHeaderList, HttpStatus.OK);
    }

    // Find PerpetualHeader
    @ApiOperation(response = PerpetualHeader.class, value = "Spark Find PerpetualHeader")
    @PostMapping("/perpetualheader")
    public ResponseEntity<?> searchPerpetualHeader(@RequestBody SearchPerpetualHeaderV2 searchPerpetualHeaderV2) throws Exception {
        List<PerpetualHeader> perpetualHeaderList = sparkPerpetualHeaderService.findPerpetualHeader(searchPerpetualHeaderV2);
        return new ResponseEntity<>(perpetualHeaderList, HttpStatus.OK);
    }

    // Find InboundLine
    @ApiOperation(response = InboundLineV2.class, value = "Spark Find InboundLine")
    @PostMapping("/inboundline")
    public ResponseEntity<?> searchInboundLine(@RequestBody FindInboundLineV2 findInboundLineV2) throws Exception {
        List<InboundLineV2> inboundLineList = sparkInboundLineService.findInboundLineV2(findInboundLineV2);
        return new ResponseEntity<>(inboundLineList, HttpStatus.OK);
    }

    // Get InhouseTransferHeader
    @ApiOperation(response = InhouseTransferHeader.class, value = "Spark Get InhouseTransferHeader")
    @PostMapping("/inhousetransferheader")
    public ResponseEntity<?> getInhouseTransferHeader() throws Exception {
        List<InhouseTransferHeader> inboundLineList = sparkInhouseTransferHeaderService.getAllInhouseTransferHeader();
        return new ResponseEntity<>(inboundLineList, HttpStatus.OK);
    }

    // Get StockReport
    @ApiOperation(response = StockReport.class, value = "Spark Get StockReport")
    @PostMapping("/stockreport")
    public ResponseEntity<?> getStockReport(FindStockReport findStockReport) throws Exception {
        List<StockReport> stockReportList = sparkStockReportService.getStockReport(findStockReport);
        return new ResponseEntity<>(stockReportList, HttpStatus.OK);
    }

    // Get OrderStatusReport
    @ApiOperation(response = Optional.class, value = "Spark Get Order Status Report")
    @PostMapping("/orderstatusreport")
    public ResponseEntity<?> findOrderStatus(@RequestBody SearchOrderStatusReport searchOrderStatusReport) throws Exception {
        List<OrderStatusReport> findOrderStatus = sparkOrderStatusReportService.findOrderStatusReport(searchOrderStatusReport);
        return new ResponseEntity<>(findOrderStatus, HttpStatus.OK);
    }

    // Get PerpetualLine
    @ApiOperation(response = Optional.class, value = "Spark Find PerpetualLine ")
    @PostMapping("/perpetualline")
    public ResponseEntity<?> findPerpetualLine(@RequestBody SearchPerpetualLineV2 searchPerpetualLineV2) throws Exception {
        List<PerpetualLineV2> findPerpetualLine = sparkPerpetualLineService.findPerpetualLine(searchPerpetualLineV2);
        return new ResponseEntity<>(findPerpetualLine, HttpStatus.OK);
    }

    // Get PeriodicLine
    @ApiOperation(response = Optional.class, value = "Spark Find PeriodicLine ")
    @PostMapping("/periodicline")
    public ResponseEntity<?> findPeriodicLine(@RequestBody FindPeriodicLineV2 findPeriodicLineV2) throws Exception {
        List<PeriodicLineV2> findPeriodicLine = sparkPeriodicLineService.findPeriodicLine(findPeriodicLineV2);
        return new ResponseEntity<>(findPeriodicLine, HttpStatus.OK);
    }

    // Get PreOutBoundLine
    @ApiOperation(response = Optional.class, value = "Spark Find PreOutBoundLine ")
    @PostMapping("/preoutboundline")
    public ResponseEntity<?> findPreOutBoundLine(@RequestBody FindPreOutBoundLineV2 findPeriodicLineV2) throws Exception {
        List<PreOutBoundLineV2> findPeriodicLine = sparkPreOutBoundLineService.findPreOutBoundLine(findPeriodicLineV2);
        return new ResponseEntity<>(findPeriodicLine, HttpStatus.OK);
    }

    //Get StagingLine
    @ApiOperation(response = StagingLineV2.class, value = "Spark FindStagingLine")
    @PostMapping("/stagingLine")
    public ResponseEntity<?> findStagingLineV2(@RequestBody FindStagingLineV2 findStagingLineV2) throws Exception {
        List<StagingLineV2> stagingLineV2 = sparkStagingLineV2Service.findStagingLineV2(findStagingLineV2);
        return new ResponseEntity<>(stagingLineV2, HttpStatus.OK);
    }

    //Get GrLine
    @ApiOperation(response = GrLineV2.class, value = "Spark FindGrLine")
    @PostMapping("/grline")
    public ResponseEntity<?> findGrLineV2(@RequestBody FindGrLineV2 findGrLineV2) throws Exception {
        List<GrLineV2> stagingLineV2 = sparkGrLineService.findGrLineV2(findGrLineV2);
        return new ResponseEntity<>(stagingLineV2, HttpStatus.OK);
    }

    //Find QualityLine
    @ApiOperation(response = QualityLineV2.class, value = "Spark FindQualityLine")
    @PostMapping("/qualityline")
    public ResponseEntity<?> qualityLineV2(@RequestBody FindQualityLineV2 findQualityLineV2) throws Exception {
        List<QualityLineV2> qualityLineV2 = sparkQualityLineService.findQualityLineV2(findQualityLineV2);
        return new ResponseEntity<>(qualityLineV2, HttpStatus.OK);
    }

    @ApiOperation(response = ImBasicData1.class, value = "Spark ImBasicData1")
    @PostMapping("/imbasicdata1")
    public ResponseEntity<?> findImBasicData1(SearchImBasicData1 findImBasicData1) throws Exception {
        List<ImBasicData1> imBasicData1List = sparkImBasicData1Service.searchImBasicData1(findImBasicData1);
        return new ResponseEntity<>(imBasicData1List, HttpStatus.OK);
    }

    @ApiOperation(response = ClientGeneral.class, value = "Spark ClientGeneral")
    @PostMapping("/clientGeneral")
    public ResponseEntity<?> findClientGeneral(@RequestBody FindClientGeneral findClientGeneral) throws Exception {
        List<ClientGeneral> clientGeneralList = clientGeneralNewService.findClientGeneral(findClientGeneral) ;
        return new ResponseEntity<>(clientGeneralList, HttpStatus.OK);
    }

    @GetMapping("/downloadInventory")
    public String downloadInventoryInCSV(){
        try {
            inventoryDownloadService.downloadInventoryInCSV();
            return "Successfully Download";
        } catch (Exception e){
            e.printStackTrace();
            return "Error During CSV Download";
        }
    }



}
