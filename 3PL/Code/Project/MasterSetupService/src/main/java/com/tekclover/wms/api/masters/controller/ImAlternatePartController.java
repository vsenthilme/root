package com.tekclover.wms.api.masters.controller;

import com.tekclover.wms.api.masters.model.imalternateparts.AddImAlternatePart;
import com.tekclover.wms.api.masters.model.imalternateparts.ImAlternatePart;
import com.tekclover.wms.api.masters.model.imalternateparts.SearchImAlternateParts;
import com.tekclover.wms.api.masters.model.imalternateparts.UpdateImAlternatePart;
import com.tekclover.wms.api.masters.service.ImAlternatePartService;
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
@Api(tags = {"ImAlternatePart"}, value = "ImAlternatePart  Operations related to ImAlternatePartController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ImAlternatePart ",description = "Operations related to ImAlternate ")})
@RequestMapping("/imalternatepart")
@RestController
public class ImAlternatePartController {

    @Autowired
    private ImAlternatePartService imAlternatePartService;

    @ApiOperation(response = ImAlternatePart.class, value = "Get all ImAlternatePart details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ImAlternatePart> imAlternatePart = imAlternatePartService.getAllImAlternateParts();
        return new ResponseEntity<>(imAlternatePart, HttpStatus.OK);
    }

    @ApiOperation(response = ImAlternatePart.class, value = "Get a ImAlternatePart") // label for swagger
    @GetMapping("/{itemCode}")
    public ResponseEntity<?> getImAlternatePart(@RequestParam String companyCodeId,
                                                @RequestParam String languageId, @RequestParam String plantId,
                                                @PathVariable String itemCode, @RequestParam String warehouseId) {

        List<ImAlternatePart> dbImAlternatePart =
                imAlternatePartService.getImAlternatePart(companyCodeId,languageId,warehouseId,plantId,itemCode);
        return new ResponseEntity<>(dbImAlternatePart, HttpStatus.OK);
    }


    @ApiOperation(response = ImAlternatePart.class, value = "Create ImAlternatePart") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postHandlingUnit(@Valid @RequestBody List<AddImAlternatePart> newImAlternatePart, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<ImAlternatePart> createdImAlternatePart= imAlternatePartService.createAlternatePart(newImAlternatePart, loginUserID);
        return new ResponseEntity<>(createdImAlternatePart , HttpStatus.OK);
    }

    @ApiOperation(response = ImAlternatePart.class, value = "Update ImAlternatePart") // label for swagger
    @PatchMapping("/{itemCode}")
    public ResponseEntity<?> patchImAlternatePart(@PathVariable String itemCode, @RequestParam String companyCodeId,
                                               @RequestParam String languageId, @RequestParam String plantId,
                                               @RequestParam String warehouseId, @RequestParam String loginUserID,
                                               @Valid @RequestBody List<AddImAlternatePart> updateImAlternatePart)
            throws IllegalAccessException, InvocationTargetException {

        List<ImAlternatePart> createdImAlternatePart = imAlternatePartService.updateImAlternatePart(companyCodeId,
                languageId,plantId,warehouseId,itemCode, updateImAlternatePart, loginUserID);
        return new ResponseEntity<>(createdImAlternatePart , HttpStatus.OK);
    }

    @ApiOperation(response = ImAlternatePart.class, value = "Delete ImAlternatePart") // label for swagger
    @DeleteMapping("/{itemCode}")
    public ResponseEntity<?> deleteHandlingUnit(@PathVariable String itemCode, @RequestParam String companyCodeId,
                                                @RequestParam String languageId, @RequestParam String plantId,
                                                 @RequestParam String warehouseId,@RequestParam String loginUserID) {

        imAlternatePartService.deleteImAlternateUom(companyCodeId,languageId,plantId,warehouseId,itemCode,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = ImAlternatePart.class, value = "Find ImAlternatePart") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findAisleId(@Valid @RequestBody SearchImAlternateParts findImAlternateParts) throws Exception {
        List<ImAlternatePart> createdIamAlternatePart = imAlternatePartService.findIAmAlternatePart(findImAlternateParts);
        return new ResponseEntity<>(createdIamAlternatePart, HttpStatus.OK);
    }
}
