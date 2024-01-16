package com.tekclover.wms.core.controller;


import com.tekclover.wms.core.model.middleware.*;
import com.tekclover.wms.core.model.transaction.IntegrationLog;
import com.tekclover.wms.core.service.ConnectorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("wms-connector-service")
@Api(tags = {"Connector Service"}, value = "Connector Service Operations") //label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Operation Related to Connector Service Modules")})
public class ConnectorServiceController {

    @Autowired
    ConnectorService connectorService;


    //-------------------------------Supplier Invoice---------------------------
    // GET ALL HEADER
    @ApiOperation(response = SupplierInvoiceHeader.class, value = "Get all Supplier Invoice Header")
    // label for swagger
    @GetMapping("/supplierInvoiceHeader")
    public ResponseEntity<?> getAllSupplierInvoiceHeader(@RequestParam String authToken) {
        SupplierInvoiceHeader[] supplierInvoiceHeader = connectorService.getAllSupplierInvoiceHeader(authToken);
        return new ResponseEntity<>(supplierInvoiceHeader, HttpStatus.OK);
    }

    //===============================Stock_Receipts============================================
    //Get All StockReceipts
    @ApiOperation(response = StockReceiptHeader.class, value = "Get All Stock Receipt Details") //label for Swagger
    @GetMapping("/stockreceipts")
    public ResponseEntity<?> getAllStockReceiptDetails(@RequestParam String authToken) {
        StockReceiptHeader[] stockReceipts = connectorService.getAllStockReceipts(authToken);
        return new ResponseEntity<>(stockReceipts, HttpStatus.OK);
    }

    //==========================================B2BTransferIn===============================================================
    //Get All B2BTransferIns
    @ApiOperation(response = TransferInHeader.class, value = "Get All B2BTransferIn Details")
    @GetMapping("/b2btransferins")
    public ResponseEntity<?> getAllB2BTransferIns(@RequestParam String authToken) {
        TransferInHeader[] transferIns = connectorService.getAllB2BTransferInDetails(authToken);
        return new ResponseEntity<>(transferIns, HttpStatus.OK);
    }


    //=======================================InterWarehouseTransferOutV2====================================================
    //Get All InterWarehouseTransferOutV2s
    @ApiOperation(response = TransferInHeader.class, value = "Get All InterWarehouseTransferOutV2 Details")
    @GetMapping("/interwarehousetransferoutv2s")
    public ResponseEntity<?> getAllInterWhTransferOutV2s(@RequestParam String authToken) {
        TransferInHeader[] transferIns = connectorService.getAllInterWhTransferInV2Details(authToken);
        return new ResponseEntity<>(transferIns, HttpStatus.OK);
    }


    //-------------------------------Stock Adjustment---------------------------
    // GET ALL HEADER
    @ApiOperation(response = StockAdjustment.class, value = "Get all Stock Adjustment")
    // label for swagger
    @GetMapping("/stockAdjustment")
    public ResponseEntity<?> getAllStockAdjustment(@RequestParam String authToken) {
        StockAdjustment[] stockAdjustments = connectorService.getAllStockAdjustment(authToken);
        return new ResponseEntity<>(stockAdjustments, HttpStatus.OK);
    }


    //============================================OUTBOUND=============================================================
    //------------------------------------------SalesOrderV2-----------------------------------------------------------
    //Get All SalesOrderV2 Details
    @ApiOperation(response = PickListHeader.class, value = "Get All SalesOrderV2 Details")
    @GetMapping("/salesorderv2s")
    public ResponseEntity<?> getAllSalesOrderV2s(@RequestParam String authToken) {
        PickListHeader[] pickLists = connectorService.getAllSalesOrderV2Details(authToken);
        return new ResponseEntity<>(pickLists, HttpStatus.OK);
    }


    //------------------------------------------ShipmentOrderV2--------------------------------------------------------
    //Get All ShipmentOrderV2 Details
    @ApiOperation(response = TransferOutHeader.class, value = "Get All ShipmentOrderV2 Details")
    @GetMapping("/shipmentorderv2s")
    public ResponseEntity<?> getAllSoV2s(@RequestParam String authToken) {
        TransferOutHeader[] transferOuts = connectorService.getAllSoV2Details(authToken);
        return new ResponseEntity<>(transferOuts, HttpStatus.OK);
    }


    //-------------------------------------------ReturnPOV2--------------------------------------------------------
    //Get All ReturnPOV2 Details
    @ApiOperation(response = PurchaseReturnHeader.class, value = "Get All ReturnPOV2 Details")
    @GetMapping("/returnpov2s")
    public ResponseEntity<?> getAllReturnPOV2s(@RequestParam String authToken) {
        PurchaseReturnHeader[] purchaseReturns = connectorService.getAllReturnPOV2Details(authToken);
        return new ResponseEntity<>(purchaseReturns, HttpStatus.OK);
    }


    //-------------------------------------InterWarehouseTransferOutV2------------------------------------------------
    //Get All InterWarehouseTransferOutV2 Details
    @ApiOperation(response = TransferOutHeader.class, value = "Get All InterWarehouseTransferOutV2 Details")
    @GetMapping("interwarehousetransferoutv2s")
    public ResponseEntity<?> getAllInterWarehouseTransferOutV2s(@RequestParam String authToken) {
        TransferOutHeader[] transferOuts = connectorService.getAllIWhTransferOutV2Details(authToken);
        return new ResponseEntity<>(transferOuts, HttpStatus.OK);
    }


    //-----------------------------------------SalesInvoice---------------------------------------------------------
    //Get All SalesInvoice Details
    @ApiOperation(response = SalesInvoice.class, value = "Get All SalesInvoice Details")
    @GetMapping("/salesinvoices")
    public ResponseEntity<?> getAllSalesInvoices(@RequestParam String authToken) {
        SalesInvoice[] salesInvoices = connectorService.getAllSalesInvoiceDetails(authToken);
        return new ResponseEntity<>(salesInvoices, HttpStatus.OK);
    }


    //============================================Masters============================================================
    //-------------------------------------------ItemMaster----------------------------------------------------------
    //Get All ItemMaster Details
    @ApiOperation(response = ItemMaster.class, value = "Get All ItemMaster Details")
    @GetMapping("/itemmasters")
    public ResponseEntity<?> getAllItemMasters(@RequestParam String authToken) {
        ItemMaster[] itemMasters = connectorService.getAllItemMasterDetails(authToken);
        return new ResponseEntity<>(itemMasters, HttpStatus.OK);
    }


    //-------------------------------------------CustomerMaster----------------------------------------------------------
    //Get All CustomerMaster Details
    @ApiOperation(response = CustomerMaster.class, value = "Get All CustomerMaster Details")
    @GetMapping("/customermasters")
    public ResponseEntity<?> getAllCustomerMasters(@RequestParam String authToken) {
        CustomerMaster[] customerMasters = connectorService.getAllCustomerMasterDetails(authToken);
        return new ResponseEntity<>(customerMasters, HttpStatus.OK);
    }


    //============================================StockCount=========================================================
    //---------------------------------------------Perpetual---------------------------------------------------------
    //Get All Perpetual Details
    @ApiOperation(response = PerpetualHeader.class, value = "Get All Perpetual Details")
    @GetMapping("/perpetuals")
    public ResponseEntity<?> getAllPerpetuals(@RequestParam String authToken) {
        PerpetualHeader[] perpetuals = connectorService.getAllPerpetualDetails(authToken);
        return new ResponseEntity<>(perpetuals, HttpStatus.OK);
    }


    //---------------------------------------------Perpetual---------------------------------------------------------
    //Get All Perpetual Details
    @ApiOperation(response = PeriodicHeader.class, value = "Get All Periodic Details")
    @GetMapping("/periodics")
    public ResponseEntity<?> getAllPeriodic(@RequestParam String authToken) {
        PeriodicHeader[] periodic = connectorService.getAllPeriodicDetails(authToken);
        return new ResponseEntity<>(periodic, HttpStatus.OK);
    }

    //======================================Integration Log=================================================

    //GET ALL INTEGRATION LOG
    @ApiOperation(response = IntegrationLog.class, value = "Get All Integration Log Details")
    @GetMapping("/integrationlog")
    public ResponseEntity<?>getAllIntegrationLog(@RequestParam String authToken){
        IntegrationLog[] integrationLogs = connectorService.getAllIntegrationLog(authToken);
        return new ResponseEntity<>(integrationLogs, HttpStatus.OK);
    }

}

