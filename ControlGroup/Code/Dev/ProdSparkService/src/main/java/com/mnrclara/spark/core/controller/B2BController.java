package com.mnrclara.spark.core.controller;


import com.mnrclara.spark.core.model.FindImBasicData1;
import com.mnrclara.spark.core.model.ImBasicData1;
import com.mnrclara.spark.core.model.b2b.Consignment;
import com.mnrclara.spark.core.model.b2b.FindConsignment;
import com.mnrclara.spark.core.service.b2b.ConsignmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = {"User"}, value = "User Operations related to UserController")
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Operations related to User")})
@RequestMapping("/b2b")
public class B2BController {

    @Autowired
    ConsignmentService consignmentService;


    @ApiOperation(response = Consignment.class, value = "Spark Consignment")
    @PostMapping("/consignment")
    public ResponseEntity<?> findConsignment(FindConsignment findConsignment) throws Exception {
        List<Consignment> consignmentList = consignmentService.findConsignment(findConsignment);
        return new ResponseEntity<>(consignmentList, HttpStatus.OK);
    }
}
