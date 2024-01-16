package com.almailem.ams.api.connector.controller;

import com.almailem.ams.api.connector.model.stockreceipt.StockReceiptHeader;
import com.almailem.ams.api.connector.service.StockReceiptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@Api(tags = {"StockReceipt"}, value = "StockReceipt Operations related to StockReceiptController") //label for Swagger
@SwaggerDefinition(tags = {@Tag(name = "StockReceipt", description = "Operations related to StockReceipt")})
@RequestMapping("/stockreceipt")
@RestController
public class StockReceiptController {

    @Autowired
    StockReceiptService stockReceiptService;

    //Get All StockReceipt Details
    @ApiOperation(response = StockReceiptHeader.class, value = "Get All Stock Receipt Details") //label for Swagger
    @GetMapping("")
    public ResponseEntity<?> getAllStockReceipts() {
        List<StockReceiptHeader> stockReceipts = stockReceiptService.getAllStockReceiptDetails();
        return new ResponseEntity<>(stockReceipts, HttpStatus.OK);
    }
}
