package com.almailem.ams.api.connector.controller;

import com.almailem.ams.api.connector.model.transferin.TransferInHeader;
import com.almailem.ams.api.connector.service.B2BTransferInService;
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
@Api(tags = {"B2BTransferIn"}, value = "B2BTransferIn Operations related to B2BTransferInController")
//label for Swagger
@SwaggerDefinition(tags = {@Tag(name = "B2BTransferIn", description = "Operations related to B2BTransferIn")})
@RequestMapping("/b2btransferin")
@RestController
public class B2BTransferInController {

    @Autowired
    B2BTransferInService b2BTransferInService;

    /**
     * Get All B2BTransferIn Details
     *
     * @return
     */
    @ApiOperation(response = TransferInHeader.class, value = "Get All B2BTransferIn Details")
    @GetMapping("")
    public ResponseEntity<?> getAllB2BTransferIns() {
        List<TransferInHeader> transferIns = b2BTransferInService.getAllB2BTransferInDetails();
        return new ResponseEntity<>(transferIns, HttpStatus.OK);
    }
}
