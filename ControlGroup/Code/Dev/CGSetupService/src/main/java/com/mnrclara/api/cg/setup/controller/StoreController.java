package com.mnrclara.api.cg.setup.controller;


import com.mnrclara.api.cg.setup.model.store.AddStoreId;
import com.mnrclara.api.cg.setup.model.store.StoreId;
import com.mnrclara.api.cg.setup.service.StoreIdService;
import com.mnrclara.api.cg.setup.model.store.FindStoreId;
import com.mnrclara.api.cg.setup.model.store.UpdateStoreId;
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
@Api(tags = {"Store"}, value = "Store Operations related to StoreController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Store",description = "Operations related to Store")})
@RequestMapping("/store")
@RestController
public class StoreController {

    @Autowired
    StoreIdService storeIdService;

    // GET ALL
    @ApiOperation(response = StoreId.class, value = "Get all StoreId details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<StoreId> cityList = storeIdService.getAllStoreId();
        return new ResponseEntity<>(cityList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = StoreId.class, value = "Get a StoreId") // label for swagger
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreId(@PathVariable Long storeId, @RequestParam String languageId,
                                     @RequestParam String companyId) {
        StoreId dbStoreId = storeIdService.getStoreId(storeId, companyId, languageId);
        log.info("StoreId : " + dbStoreId);
        return new ResponseEntity<>(dbStoreId, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = StoreId.class, value = "Create StoreId") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addStoreId(@Valid @RequestBody AddStoreId newStoreId, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StoreId createdStoreId = storeIdService.createStoreId(newStoreId, loginUserID);
        return new ResponseEntity<>(createdStoreId , HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = StoreId.class, value = "Update StoreId") // label for swagger
    @PatchMapping("/{storeId}")
    public ResponseEntity<?> patchCity(@PathVariable Long storeId, @RequestParam String languageId,
                                       @RequestParam String loginUserID, @RequestParam String companyId,
                                       @RequestBody UpdateStoreId updateStore)
            throws IllegalAccessException, InvocationTargetException {

        StoreId updateStoreId =
                storeIdService.updateStoreId(storeId, languageId, companyId, loginUserID, updateStore);
        return new ResponseEntity<>(updateStoreId , HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = StoreId.class, value = "Delete StoreId") // label for swagger
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteCity(@PathVariable Long storeId, @RequestParam String companyId,
                                        @RequestParam String languageId, @RequestParam String loginUserID) {
        storeIdService.deleteStoreId(storeId, companyId, languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = StoreId.class, value = "Find StoreId") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findStoreId(@Valid @RequestBody FindStoreId findStoreId) throws Exception {
        List<StoreId> createdStoreId= storeIdService.findStoreId(findStoreId);
        return new ResponseEntity<>(createdStoreId, HttpStatus.OK);
    }

}
