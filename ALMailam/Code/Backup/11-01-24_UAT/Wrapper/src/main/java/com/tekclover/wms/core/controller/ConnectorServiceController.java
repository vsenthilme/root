package com.tekclover.wms.core.controller;

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
    public ResponseEntity<?> getAllIntegrationLog(@RequestParam String authToken){
        IntegrationLog[] integrationLogs = connectorService.getAllIntegrationLog(authToken);
        return new ResponseEntity<>(integrationLogs, HttpStatus.OK);
    }
}
