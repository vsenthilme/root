package com.iwmvp.api.master.controller;

import com.iwmvp.api.master.model.loyaltycategory.*;
import com.iwmvp.api.master.service.LoyaltyCategoryService;
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
@Api(tags = {"LoyaltyCategory"},value = "LoyaltyCategory Operations related to LoyaltyCategoryController")//label for Swagger
@SwaggerDefinition(tags ={@Tag(name="LoyaltyCategory",description = "Operations related to LoyaltyCategory")})
@RequestMapping("/loyaltycategory")
@RestController
public class LoyaltyCategoryController {
    @Autowired
    private LoyaltyCategoryService loyaltyCategoryService;
    @ApiOperation(response = LoyaltyCategory.class, value = "Get all LoyaltyCategory details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        List<LoyaltyCategory> LoyaltyCategoryList=loyaltyCategoryService.getAllLoyaltyCategory();
        return new ResponseEntity<>(LoyaltyCategoryList, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Get a LoyaltyCategory") // label for swagger
    @GetMapping("/{rangeId}")
    public ResponseEntity<?> getLoyaltyCategory( @PathVariable Long rangeId) {
        LoyaltyCategory LoyaltyCategoryList =
                loyaltyCategoryService.getLoyaltyCategory(rangeId);
        log.info("LoyaltyCategoryList : " + LoyaltyCategoryList);
        return new ResponseEntity<>(LoyaltyCategoryList, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Create LoyaltyCategory") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postLoyaltyCategory(@Valid @RequestBody AddLoyaltyCategory newLoyaltyCategory,
                                          @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory createdLoyaltyCategory = loyaltyCategoryService.createLoyaltyCategory(newLoyaltyCategory, loginUserID);
        return new ResponseEntity<>(createdLoyaltyCategory, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Update LoyaltyCategory") // label for swagger
    @PatchMapping("/{rangeId}")
    public ResponseEntity<?> patchLoyaltyCategory(@PathVariable Long rangeId,
                                                  @Valid @RequestBody UpdateLoyaltyCategory updateLoyaltyCategory, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        LoyaltyCategory createdLoyaltyCategory =
                loyaltyCategoryService.updateLoyaltyCategory(rangeId,loginUserID, updateLoyaltyCategory);
        return new ResponseEntity<>(createdLoyaltyCategory, HttpStatus.OK);
    }
    @ApiOperation(response = LoyaltyCategory.class, value = "Delete LoyaltyCategory") // label for swagger
    @DeleteMapping("/{rangeId}")
    public ResponseEntity<?> deleteLoyaltyCategory(@PathVariable Long rangeId,
                                            @RequestParam String loginUserID) {
        loyaltyCategoryService.deleteLoyaltyCategory(rangeId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //Find
    @ApiOperation(response = LoyaltyCategory.class, value = "Find LoyaltyCategory") // label for swagger
    @PostMapping("/findLoyaltyCategory")
    public ResponseEntity<?> findLoyaltyCategory(@Valid @RequestBody FindLoyaltyCategory findLoyaltyCategory) throws Exception {
        List<LoyaltyCategory> createdLoyaltyCategory = loyaltyCategoryService.findLoyaltyCategory(findLoyaltyCategory);
        return new ResponseEntity<>(createdLoyaltyCategory, HttpStatus.OK);
    }
}
