package com.iweb2b.core.controller;

import com.iweb2b.core.model.spark.*;
import com.iweb2b.core.service.SparkService;
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
@RequestMapping("/b2b-spark-service")
@Api(tags = { "Spark Services" }, value = "Spark Services") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "Spark", description = "Operations related to SparkService") })
public class SparkServiceController {

    @Autowired
    private SparkService sparkService;


    @ApiOperation(response = ConsignmentV2.class, value = "Find ConsignmentV2")
    @PostMapping("/consignmentV2")
    public ResponseEntity<?> findConsignmentV2(@RequestBody FindConsignmentV2 findConsignmentV2) {
        ConsignmentV2[] consignmentV2s = sparkService.findConsignmentV2(findConsignmentV2);
        return new ResponseEntity<>(consignmentV2s, HttpStatus.OK);
    }


    @ApiOperation(response = Jntwebhook.class, value = "Find Jntwebhook")
    @PostMapping("/jntwebhook")
    public ResponseEntity<?> findB2BJntwebhook(@RequestBody FindJntwebhook findJntwebhook) {
        Jntwebhook[] jntwebhookList = sparkService.findB2BJntwebhook(findJntwebhook);
        return new ResponseEntity<>(jntwebhookList, HttpStatus.OK);
    }

    @ApiOperation(response = JntHubCode.class, value = "Find JntHubCode")
    @PostMapping("/jntHubCode")
    public ResponseEntity<?> findB2BJntHubCode(@RequestBody FindJntHubCode findJntHubCode) {
        JntHubCode[] jntHubCodeList = sparkService.findB2BJntHubCode(findJntHubCode);
        return new ResponseEntity<>(jntHubCodeList, HttpStatus.OK);
    }

    @ApiOperation(response = QPWebhook.class, value = "Find QPWebhook")
    @PostMapping("/qpwebhook")
    public ResponseEntity<?> findB2BQPWebhook(@RequestBody FindQPWebhook findQPWebhook) {
        QPWebhook[] qpWebhookList = sparkService.findB2BQPWebhook(findQPWebhook);
        return new ResponseEntity<>(qpWebhookList, HttpStatus.OK);
    }

}
