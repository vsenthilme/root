package com.tekclover.wms.api.masters.controller;


import com.tekclover.wms.api.masters.model.impacking.AddImPacking;
import com.tekclover.wms.api.masters.model.impacking.ImPacking;
import com.tekclover.wms.api.masters.model.impacking.SearchImPacking;
import com.tekclover.wms.api.masters.model.impacking.UpdateImPacking;
import com.tekclover.wms.api.masters.model.impalletization.AddImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.ImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.SearchImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.UpdateImPalletization;
import com.tekclover.wms.api.masters.service.ImPackingService;
import com.tekclover.wms.api.masters.service.ImPalletizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"ImPalletization"}, value = "ImPalletization  Operations related to ImPalletizationController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ImPalletization ",description = "Operations related to ImPalletization ")})
@RequestMapping("/impalletization")
@RestController
public class ImPalletizationController {
    @Autowired
    ImPalletizationService imPalletizationService;

    @ApiOperation(response = ImPalletization.class, value = "Get all ImPalletization details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ImPalletization> imPalletizationList = imPalletizationService.getAllImPalletization();
        return new ResponseEntity<>(imPalletizationList, HttpStatus.OK);
    }

    @ApiOperation(response = ImPalletization.class, value = "Get a ImPalletization") // label for swagger
    @GetMapping("/{palletizationLevel}")
    public ResponseEntity<?> getImPacking(@PathVariable String palletizationLevel, @RequestParam String companyCodeId, @RequestParam String plantId, @RequestParam String languageId, @RequestParam String warehouseId, @RequestParam String itemCode) {
        ImPalletization imPalletization = imPalletizationService.getImPalletization(warehouseId,companyCodeId,languageId,plantId,itemCode,palletizationLevel);
        log.info("ImPalletization : " + imPalletization);
        return new ResponseEntity<>(imPalletization, HttpStatus.OK);
    }

    @ApiOperation(response = ImPalletization.class, value = "Search ImPalletization") // label for swagger
    @PostMapping("/findImPalletization")
    public List<ImPalletization> findImPalletization(@RequestBody SearchImPalletization searchImPalletization)
            throws Exception {
        return imPalletizationService.findImPalletization(searchImPalletization);
    }

    @ApiOperation(response = ImPalletization.class, value = "Create ImPalletization") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postImPalletization(@Valid @RequestBody AddImPalletization newImPalletization, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImPalletization createdPalletization = imPalletizationService.createImPalletization(newImPalletization, loginUserID);
        return new ResponseEntity<>(createdPalletization , HttpStatus.OK);
    }

    @ApiOperation(response = ImPalletization.class, value = "Update ImPalletization") // label for swagger
    @PatchMapping("/{palletizationLevel}")
    public ResponseEntity<?> patchPalletization(@PathVariable String palletizationLevel, @RequestParam String companyCodeId, @RequestParam String plantId, @RequestParam String languageId, @RequestParam String warehouseId, @RequestParam String itemCode,
                                                @Valid @RequestBody UpdateImPalletization updateImPalletization, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImPalletization createdImPalletization = imPalletizationService.updateImPalletization(companyCodeId,plantId,warehouseId,languageId,itemCode,palletizationLevel,updateImPalletization,loginUserID);
        return new ResponseEntity<>(createdImPalletization , HttpStatus.OK);
    }

    @ApiOperation(response = ImPalletization.class, value = "Delete ImPalletization") // label for swagger
    @DeleteMapping("/{palletizationLevel}")
    public ResponseEntity<?> deleteImPalletization(@PathVariable String palletizationLevel,@RequestParam String companyCodeId,@RequestParam String plantId,@RequestParam String languageId,@RequestParam String warehouseId,@RequestParam String itemCode, @RequestParam String loginUserID) {
        imPalletizationService.deleteImPalletization(companyCodeId,languageId,plantId,warehouseId,itemCode,palletizationLevel,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
