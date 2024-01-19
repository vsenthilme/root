package com.tekclover.wms.core.controller;

import com.tekclover.wms.core.model.Connector.*;
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

    //======================================Integration Log=================================================

    //GET ALL INTEGRATION LOG
    @ApiOperation(response = IntegrationLog.class, value = "Get All Integration Log Details")
    @GetMapping("/integrationlog")
    public ResponseEntity<?> getAllIntegrationLog(@RequestParam String authToken) {
        IntegrationLog[] integrationLogs = connectorService.getAllIntegrationLog(authToken);
        return new ResponseEntity<>(integrationLogs, HttpStatus.OK);
    }

    //FindSupplierInvoice
    @ApiOperation(response = SupplierInvoiceHeader[].class, value = "Find SupplierInvoiceHeader")//label for swagger
    @PostMapping("/findSupplierInvoiceHeader")
    public ResponseEntity<?> findSupplierInvoiceHeader(@RequestBody SearchSupplierInvoiceHeader searchSupplierInvoiceHeader,
                                                       @RequestParam String authToken) {
        SupplierInvoiceHeader[] supplierInvoiceHeader = connectorService.findSupplierInvoiceHeader(searchSupplierInvoiceHeader, authToken);
        return new ResponseEntity<>(supplierInvoiceHeader, HttpStatus.OK);
    }

    @ApiOperation(response = StockReceiptHeader[].class, value = "Find StockReceiptHeader")//label for swagger
    @PostMapping("/findStockReceiptHeader")
    public ResponseEntity<?> findStockReceiptHeader(@RequestBody SearchStockReceiptHeader searchStockReceiptHeader
            , @RequestParam String authToken) {
        StockReceiptHeader[] supplierStockReceiptHeader = connectorService.findStockReceiptHeader(searchStockReceiptHeader, authToken);
        return new ResponseEntity<>(supplierStockReceiptHeader, HttpStatus.OK);
    }

    @ApiOperation(response = TransferInHeader[].class, value = "Find B2BTransferInHeader")//label for swagger
    @PostMapping("/findB2BTransferInHeader")
    public ResponseEntity<?> findB2BTransferInHeader(@RequestBody SearchTransferInHeader searchTransferInHeader
            , @RequestParam String authToken) {
        TransferInHeader[] transferInHeader = connectorService.findB2BTransferInHeader(searchTransferInHeader, authToken);
        return new ResponseEntity<>(transferInHeader, HttpStatus.OK);
    }

    @ApiOperation(response = TransferInHeader[].class, value = "Find InterWareHouseTransferInHeader")//label for swagger
    @PostMapping("/findInterWareHouseTransferInHeader")
    public ResponseEntity<?> findInterWareHouseTransferInHeader(@RequestBody SearchTransferInHeader searchTransferInHeader
            , @RequestParam String authToken) {
        TransferInHeader[] transferInHeader = connectorService.findInterWareHouseTransferInHeader(searchTransferInHeader, authToken);
        return new ResponseEntity<>(transferInHeader, HttpStatus.OK);
    }

    // Find SalesReturnHeader
    @ApiOperation(response = SalesReturnHeader[].class, value = "Find SalesReturnHeader") //label for swagger
    @PostMapping("/findSalesReturnHeader")
    public ResponseEntity<?> findSalesReturnHeader(@RequestBody FindSalesReturnHeader findSalesReturnHeader,
                                                   @RequestParam String authToken) {
        SalesReturnHeader[] salesReturnHeader = connectorService.findSalesReturnHeader(findSalesReturnHeader, authToken);
        return new ResponseEntity<>(salesReturnHeader, HttpStatus.OK);
    }

    // Find StockAdjustment
    @ApiOperation(response = StockAdjustment[].class, value = "Find StockAdjustment") //label for swagger
    @PostMapping("/findStockAdjustment")
    public ResponseEntity<?> findStockAdjustment(@RequestBody FindStockAdjustment findStockAdjustment,
                                                 @RequestParam String authToken) {
        StockAdjustment[] stockAdjustment = connectorService.findStockAdjustment(findStockAdjustment, authToken);
        return new ResponseEntity<>(stockAdjustment, HttpStatus.OK);
    }

    // Find PickListHeader
    @ApiOperation(response = PickListHeader[].class, value = "Find PickListHeader") //label for swagger
    @PostMapping("/findPickListHeader")
    public ResponseEntity<?> findPickListHeader(@RequestBody FindPickListHeader findPickListHeader,
                                                @RequestParam String authToken) {
        PickListHeader[] pickListHeader = connectorService.findPickListHeader(findPickListHeader, authToken);
        return new ResponseEntity<>(pickListHeader, HttpStatus.OK);
    }

    // Find PurchaseReturnHeader
    @ApiOperation(response = PurchaseReturnHeader[].class, value = "Find PurchaseReturnHeader") //label for swagger
    @PostMapping("/findPurchaseReturnHeader")
    public ResponseEntity<?> findPurchaseReturnHeader(@RequestBody FindPurchaseReturnHeader findPurchaseReturnHeader,
                                                      @RequestParam String authToken) {
        PurchaseReturnHeader[] purchaseReturnHeader = connectorService.findPurchaseReturnHeader(findPurchaseReturnHeader, authToken);
        return new ResponseEntity<>(purchaseReturnHeader, HttpStatus.OK);
    }

    // Find SalesInvoice
    @ApiOperation(response = SalesInvoice[].class, value = "Find SalesInvoice") //label for swagger
    @PostMapping("/findSalesInvoice")
    public ResponseEntity<?> findSalesInvoice(@RequestBody FindSalesInvoice findSalesInvoice,
                                              @RequestParam String authToken) {
        SalesInvoice[] salesInvoice = connectorService.findSalesInvoice(findSalesInvoice, authToken);
        return new ResponseEntity<>(salesInvoice, HttpStatus.OK);
    }

    // Find InterWarehouseTransferOut
    @ApiOperation(response = TransferOutHeader[].class, value = "Find InterWarehouseTransferOut") //label for swagger
    @PostMapping("/findInterWarehouseTransferOut")
    public ResponseEntity<?> searchInterWarehouseTransferOut(@RequestBody FindTransferOutHeader findTransferOutHeader,
                                                             @RequestParam String authToken) {
        TransferOutHeader[] interWarehouseTransferOut = connectorService.findInterWarehouseTransferOut(findTransferOutHeader, authToken);
        return new ResponseEntity<>(interWarehouseTransferOut, HttpStatus.OK);
    }


    // Find ShipmentOrder
    @ApiOperation(response = TransferOutHeader[].class, value = "Find ShipmentOrder") //label for swagger
    @PostMapping("/findShipmentOrder")
    public ResponseEntity<?> searchShipmentOrder(@RequestBody FindTransferOutHeader findTransferOutHeader,
                                                 @RequestParam String authToken) {
        TransferOutHeader[] shipmentOrder = connectorService.findShipmentOrder(findTransferOutHeader, authToken);
        return new ResponseEntity<>(shipmentOrder, HttpStatus.OK);
    }

    // Find ItemMaster
    @ApiOperation(response = ItemMaster[].class, value = "Find ItemMaster") //label for swagger
    @PostMapping("/findItemMaster")
    public ResponseEntity<?> searchItemMaster(@RequestBody FindItemMaster findItemMaster,
                                              @RequestParam String authToken) {
        ItemMaster[] itemMaster = connectorService.findItemMaster(findItemMaster, authToken);
        return new ResponseEntity<>(itemMaster, HttpStatus.OK);
    }

    // Find CustomerMaster
    @ApiOperation(response = CustomerMaster[].class, value = "Find CustomerMaster") //label for swagger
    @PostMapping("/findCustomerMaster")
    public ResponseEntity<?> searchCustomerMaster(@RequestBody FindCustomerMaster findCustomerMaster,
                                                  @RequestParam String authToken) {
        CustomerMaster[] customerMaster = connectorService.findCustomerMaster(findCustomerMaster, authToken);
        return new ResponseEntity<>(customerMaster, HttpStatus.OK);
    }

    // Find PerpetualHeader
    @ApiOperation(response = PerpetualHeader[].class, value = "Find PerpetualHeader") //label for swagger
    @PostMapping("/findPerpetualHeader")
    public ResponseEntity<?> searchPerpetualHeader(@RequestBody FindPerpetualHeader findPerpetualHeader,
                                                   @RequestParam String authToken) {
        PerpetualHeader[] perpetualHeaders = connectorService.findPerpetualHeader(findPerpetualHeader, authToken);
        return new ResponseEntity<>(perpetualHeaders, HttpStatus.OK);
    }

    // Find PeriodicHeader
    @ApiOperation(response = PeriodicHeader[].class, value = "Find PeriodicHeader") //label for swagger
    @PostMapping("/findPeriodicHeader")
    public ResponseEntity<?> searchPeriodicHeader(@RequestBody FindPeriodicHeader findPeriodicHeader,
                                                  @RequestParam String authToken) {
        PeriodicHeader[] periodicHeader = connectorService.findPeriodicHeader(findPeriodicHeader, authToken);
        return new ResponseEntity<>(periodicHeader, HttpStatus.OK);
    }

}
