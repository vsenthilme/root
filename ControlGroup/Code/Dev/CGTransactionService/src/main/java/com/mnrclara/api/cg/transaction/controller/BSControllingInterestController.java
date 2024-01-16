package com.mnrclara.api.cg.transaction.controller;

import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.AddBSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.BSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.FindBSControllingInterest;
import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.UpdateBSControllingInterest;
import com.mnrclara.api.cg.transaction.repository.BSControllingInterestRepository;
import com.mnrclara.api.cg.transaction.service.BSControllingInterestService;
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
@Api(tags = {"BSControllingInterest"}, value = "BSControllingInterest Operations related to BSControllingInterest")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "BSControllingInterest", description = "Operations related to BSControllingInterest")})
@RequestMapping("/bscontrollinginterest")
@RestController
public class BSControllingInterestController {
    @Autowired
    private BSControllingInterestRepository bSControllingInterestRepository;

    @Autowired
    private BSControllingInterestService bsControllingInterestService;

    // GET ALL
    @ApiOperation(response = BSControllingInterest.class, value = "Get all BSControllingInterest details")
    // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<BSControllingInterest> bsControllingInterest =
                bsControllingInterestService.getAllBSControllingInterest();
        return new ResponseEntity<>(bsControllingInterest, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = BSControllingInterest.class, value = "Get a BSControllingInterest") // label for swagger
    @GetMapping("/{validationId}")
    public ResponseEntity<?> getBSControllingInterest(@PathVariable Long validationId, @RequestParam String languageId,
                                                      @RequestParam String companyId) {

        BSControllingInterest dbBSControllingInterest =
                bsControllingInterestService.getBSControllingInterest(companyId, languageId, validationId);
        log.info("BSControllingInterest : " + dbBSControllingInterest);
        return new ResponseEntity<>(dbBSControllingInterest, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = BSControllingInterest.class, value = "Create BSControllingInterest") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addBSControllingInterest(@Valid @RequestBody AddBSControllingInterest newADDBSControllingInterest,
                                                      @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        BSControllingInterest createdBSControllingInterest =
                bsControllingInterestService.createBSControllingInterest(newADDBSControllingInterest, loginUserID);
        return new ResponseEntity<>(createdBSControllingInterest, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = BSControllingInterest.class, value = "Update BSControllingInterest") // label for swagger
    @PatchMapping("/{validationId}")
    public ResponseEntity<?> patchBSControllingInterest(@PathVariable Long validationId, @RequestParam String languageId,
                                                        @RequestParam String loginUserID, @RequestParam String companyId,
                                                        @RequestBody UpdateBSControllingInterest updateBSControllingInterest)
            throws IllegalAccessException, InvocationTargetException {

        BSControllingInterest dbBSControllingInterest =
                bsControllingInterestService.updateBSControllingInterest(companyId, languageId, validationId,
                        loginUserID, updateBSControllingInterest);
        return new ResponseEntity<>(dbBSControllingInterest, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = BSControllingInterest.class, value = "Delete BSControllingInterest") // label for swagger
    @DeleteMapping("/{validationId}")
    public ResponseEntity<?> deleteBSControllingInterest(@PathVariable Long validationId, @RequestParam String companyId,
                                                         @RequestParam String languageId, @RequestParam String loginUserID) {

        bsControllingInterestService.deleteBSControllingInterest(companyId, languageId, validationId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = BSControllingInterest.class, value = "Find BSControllingInterest") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findBSControllingInterest(@Valid @RequestBody FindBSControllingInterest findBSControllingInterest)
            throws Exception {
        List<BSControllingInterest> createBSControllingInterest =
                bsControllingInterestService.findBSControllingInterest(findBSControllingInterest);
        return new ResponseEntity<>(createBSControllingInterest, HttpStatus.OK);
    }
}
