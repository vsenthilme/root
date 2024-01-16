package com.tekclover.wms.core.controller;


import com.tekclover.wms.core.model.enterprise.Company;
import com.tekclover.wms.core.model.enterprise.SearchCompany;
import com.tekclover.wms.core.model.spark.FindStagingLine;
import com.tekclover.wms.core.model.spark.FindStorageBin;
import com.tekclover.wms.core.model.spark.StagingLine;
import com.tekclover.wms.core.model.spark.StorageBin;
import com.tekclover.wms.core.service.SparkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/wms-spark-service")
@Api(tags = { "Spark Service" }, value = "Spark Service Operations") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "User", description = "Operations related to Enterprise Modules") })
public class SparkServiceController {

    @Autowired
    SparkService sparkService;


//    @ApiOperation(response = StorageBin.class, value = "Search Storage Bin") // label for swagger
//    @PostMapping("/storagebin/findStorageBin")
//    public StorageBin[] findStorageBin(@RequestBody FindStorageBin findStorageBin, @RequestParam String authToken)
//            throws ParseException {
//        return sparkService.findStorageBin(findStorageBin, authToken);
//    }
//
//    @ApiOperation(response = StagingLine.class, value = "Search Storage Bin") // label for swagger
//    @PostMapping("/stagingline")
//    public StorageBin[] findStorageBin(@RequestBody FindStagingLine findStagingLine, @RequestParam String authToken)
//            throws ParseException {
//        return sparkService.findStagingLine(findStagingLine, authToken);
//    }
}
