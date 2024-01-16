package com.mnrclara.wrapper.core.controller;


import com.mnrclara.wrapper.core.model.spark.*;
import com.mnrclara.wrapper.core.service.SparkService;
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

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-spark-service")
@Api(tags = { "Spark Services" }, value = "Spark Services") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "Spark", description = "Operations related to SparkService") })
public class SparkServiceController {

@Autowired
private SparkService sparkService;

    //FIND
    @ApiOperation(response = ControlGroupTypeV1[].class, value = "Find ControlGroupType")//label for swagger
    @PostMapping("/controlgrouptype/findcontrolgroup")
    public ControlGroupTypeV1[] findControlGroupType(@RequestBody FindControlGroupTypeV1 findControlGroup) throws Exception {
        return sparkService.findControlGroupType(findControlGroup);
    }

    @ApiOperation(response = InventoryMovement[].class, value = "Find InventoryMovemt")//label for swagger
    @PostMapping("/inventorymovement/findInventory")
    public InventoryMovement[] findInventoryMovement(@RequestBody FindInventoryMovement findInventoryMovement) throws Exception {
        return sparkService.findInventoryMovement(findInventoryMovement);
    }

    @ApiOperation(response = ControlGroupResponse.class,value = "Calculate Total Language ID Sum")
    @PostMapping("/calculateTotalLanguageId")
    public ControlGroupResponse calculateTotalLanguageId(@RequestBody FindResult findResult) throws Exception {
        return sparkService.totalLanguageId(findResult);
    }

    @ApiOperation(response = ControlGroupResponse.class,value = "Calculate Total Company ID Sum")
    @PostMapping("/calculateTotalCompanyId")
    public ControlGroupResponse calculateTotalCompanyId(@RequestBody FindResult findResult) throws Exception {
        return sparkService.totalCompanyId(findResult);
    }

    @ApiOperation(response = BilledHoursPaidReport.class,value = "Calculate Total Company ID Sum")
    @PostMapping("/billedHoursPaidReport")
    public BilledHoursPaidReport[] billedHoursPaidReportList(@RequestBody BilledHoursPaid billedHoursPaid) throws Exception {
        return sparkService.getBilledHoursPaid(billedHoursPaid);
    }

    @ApiOperation(response = InvoiceHeader[].class, value = "Find InvoiceHeader")//label for swagger
    @PostMapping("/invoiceheader/findInvocieHeader")
    public InvoiceHeader[] findInvoiceHeader(@RequestBody SearchInvoiceHeader searchInvoiceHeader) throws Exception {
        return sparkService.findInvoiceHeader(searchInvoiceHeader);
    }

    @ApiOperation(response = Inventory[].class, value = "Find Inventory")//label for swagger
    @PostMapping("/inventory/findInventory")
    public Inventory[] findInventory(@RequestBody FindInventory findInventory) throws Exception {
        return sparkService.findInventory(findInventory);
    }

    @ApiOperation(response = ImBasicData1.class, value = "Get All ImBasicData1 details")
    @GetMapping("/imbasicdata1")
    public ResponseEntity<?> getAllImBasicData1() {
        ImBasicData1[] imBasicData1s = sparkService.getImBasicData1();
        return new ResponseEntity<>(imBasicData1s, HttpStatus.OK);
    }
}
