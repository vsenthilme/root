package com.tekclover.wms.core.controller;


import com.tekclover.wms.core.model.spark.FindInventoryMovement;
import com.tekclover.wms.core.model.spark.InventoryMovement;
import com.tekclover.wms.core.model.spark.InventoryMovementV2;
import com.tekclover.wms.core.service.SparkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

}
