package com.almailem.ams.api.connector.controller;

import com.almailem.ams.api.connector.model.master.ItemMaster;
import com.almailem.ams.api.connector.service.ItemMasterService;
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
@Api(tags = {"ItemMaster"}, value = "ItemMaster Operations related to ItemMasterController")
@SwaggerDefinition(tags = {@Tag(name = "ItemMaster", description = "Operations related to ItemMaster")})
@RequestMapping("/itemmaster")
@RestController
public class ItemMasterController {

    @Autowired
    ItemMasterService itemMasterService;

    //Get All ItemMaster Details
    @ApiOperation(response = ItemMaster.class, value = "Get All ItemMaster Details")
    @GetMapping("")
    public ResponseEntity<?> getAllItemMasters() {
        List<ItemMaster> itemMasters = itemMasterService.getAllItemMasterDetails();
        return new ResponseEntity<>(itemMasters, HttpStatus.OK);
    }
}
