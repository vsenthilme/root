package com.iwmvp.api.master.controller;

import com.iwmvp.api.master.model.loyaltycategory.FindLoyaltyCategory;
import com.iwmvp.api.master.model.loyaltycategory.LoyaltyCategory;
import com.iwmvp.api.master.model.loyaltysetup.*;
import com.iwmvp.api.master.service.LoyaltySetupService;
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
@Api(tags = {"LoyaltySetup"},value = "LoyaltySetup Operations related to LoyaltySetupController")//label for Swagger
@SwaggerDefinition(tags ={@Tag(name="LoyaltySetup",description = "Operations related to LoyaltySetup")})
@RequestMapping("/loyaltysetup")
@RestController
public class LoyaltySetupController {
    @Autowired
    private LoyaltySetupService loyaltySetupService;
    @ApiOperation(response = LoyaltySetup.class, value = "Get all LoyaltySetup details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        List<LoyaltySetup> LoyaltySetupList=loyaltySetupService.getLoyaltySetups();
        return new ResponseEntity<>(LoyaltySetupList, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Get a LoyaltySetup") // label for swagger
    @GetMapping("/{loyaltyId}")
    public ResponseEntity<?> getLoyaltySetup( @PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId) {
        LoyaltySetup LoyaltySetupList =
                loyaltySetupService.getLoyaltySetup(loyaltyId,categoryId,companyId,languageId);
        log.info("LoyaltySetupList : " + LoyaltySetupList);
        return new ResponseEntity<>(LoyaltySetupList, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Create LoyaltySetup") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postLoyaltySetup(@Valid @RequestBody AddLoyaltySetup newLoyaltySetup,
                                          @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup createdLoyaltySetup = loyaltySetupService.createLoyaltySetup(newLoyaltySetup, loginUserID);
        return new ResponseEntity<>(createdLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Update LoyaltySetup") // label for swagger
    @PatchMapping("/{loyaltyId}")
    public ResponseEntity<?> patchLoyaltySetup(@PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId,
                                               @Valid @RequestBody UpdateLoyaltySetup updateLoyaltySetup, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltySetup createdLoyaltySetup =
                loyaltySetupService.updateLoyaltySetup(loyaltyId,categoryId,companyId,languageId, loginUserID, updateLoyaltySetup);
        return new ResponseEntity<>(createdLoyaltySetup, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltySetup.class, value = "Delete LoyaltySetup") // label for swagger
    @DeleteMapping("/{loyaltyId}")
    public ResponseEntity<?> deleteLoyaltySetup(@PathVariable Long loyaltyId,@RequestParam String categoryId,@RequestParam String companyId,@RequestParam String languageId,
                                            @RequestParam String loginUserID) {
        loyaltySetupService.deleteLoyaltySetup(loyaltyId,categoryId,companyId,languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //Find
    @ApiOperation(response = LoyaltySetup.class, value = "Find LoyaltySetup") // label for swagger
    @PostMapping("/findLoyaltySetup")
    public ResponseEntity<?> findLoyaltySetup(@Valid @RequestBody FindLoyaltySetup findLoyaltySetup) throws Exception {
        List<LoyaltySetup> createdLoyaltySetup = loyaltySetupService.findLoyaltySetup(findLoyaltySetup);
        return new ResponseEntity<>(createdLoyaltySetup, HttpStatus.OK);
    }
}
