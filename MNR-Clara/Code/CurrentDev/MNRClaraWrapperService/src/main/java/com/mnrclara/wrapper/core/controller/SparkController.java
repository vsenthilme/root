package com.mnrclara.wrapper.core.controller;


import com.mnrclara.wrapper.core.model.spark.ClientGeneral;
import com.mnrclara.wrapper.core.model.spark.FindClientGeneral;
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


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-spark-service")
@Api(tags = {"Spark Service"}, value = "Spark Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Operations related to Spark Modules")})
public class SparkController {

    @Autowired
    private SparkService sparkService;

    /*========================================Management=================================================*/


    @ApiOperation(response = ClientGeneral.class, value = "Find Client General")
    @PostMapping("/findClientGeneral")
    public ResponseEntity<?> findClientGeneral(@RequestBody FindClientGeneral findClientGeneral) throws Exception {

        ClientGeneral[] dbClientGeneral = sparkService.findClientGeneral(findClientGeneral);
        return new ResponseEntity<>(dbClientGeneral, HttpStatus.OK);
    }
}
