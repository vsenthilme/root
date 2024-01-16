package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.numberange.AddNumberRange;
import com.mnrclara.api.cg.setup.model.numberange.FindNumberRange;
import com.mnrclara.api.cg.setup.model.numberange.NumberRange;
import com.mnrclara.api.cg.setup.model.numberange.UpdateNumberRange;
import com.mnrclara.api.cg.setup.service.NumberRangeService;
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
@CrossOrigin(origins = "*")
@Api(tags = {"NumberRange"}, value = "NumberRange Operations related to NumberRangeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NumberRange", description = "Operations related to NumberRange")})
@RequestMapping("/numberRange")
@RestController
public class NumberRangeController {

    @Autowired
    NumberRangeService numberRangeService;

    // GET ALL
    @ApiOperation(response = NumberRange.class, value = "Get all NumberRange details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<NumberRange> numberRangeList = numberRangeService.getNumberRanges();
        return new ResponseEntity<>(numberRangeList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = NumberRange.class, value = "Get a NumberRange") // label for swagger 
    @GetMapping("/{numberRangeCode}")
    public ResponseEntity<?> getNumberRange(@PathVariable Long numberRangeCode, @RequestParam String companyId,
                                            @RequestParam String languageId, @RequestParam String numberRangeObject) {
        NumberRange numberRange = numberRangeService.getNumberRange(languageId, companyId, numberRangeCode, numberRangeObject);
        log.info("NumberRange : " + numberRange);
        return new ResponseEntity<>(numberRange, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postNumberRange(@Valid @RequestBody AddNumberRange newNumberRange,
                                             @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        NumberRange createdNumberRange = numberRangeService.createNumberRange(newNumberRange, loginUserID);
        return new ResponseEntity<>(createdNumberRange, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
    @PatchMapping("/{numberRangeCode}")
    public ResponseEntity<?> patchNumberRange(@PathVariable Long numberRangeCode, @RequestParam String companyId, @RequestParam String languageId,
                                              @RequestParam String loginUserID, @RequestParam String numberRangeObject,
                                              @Valid @RequestBody UpdateNumberRange updateNumberRange)
            throws IllegalAccessException, InvocationTargetException {

        NumberRange updatedNumberRange = numberRangeService.updateNumberRange(numberRangeCode, companyId, languageId,
                numberRangeObject, loginUserID, updateNumberRange);
        return new ResponseEntity<>(updatedNumberRange, HttpStatus.OK);
    }

    //DELETE
    @ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
    @DeleteMapping("/{numberRangeCode}")
    public ResponseEntity<?> deleteNumberRange(@PathVariable Long numberRangeCode, @RequestParam String languageId,
                                               @RequestParam String companyId, @RequestParam String numberRangeObject,
                                               @RequestParam String loginUserID) {
        numberRangeService.deleteNumberRange(numberRangeCode, companyId, languageId, numberRangeObject, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND
    @ApiOperation(response = NumberRange.class,value = "Find NumberRange") // label for swagger
    @PostMapping("/findNumberRange")
    public ResponseEntity<?> findNumberRange(@Valid @RequestBody FindNumberRange findNumberRange)throws Exception{
        List<NumberRange> numberRangeList = numberRangeService.findNumberRange(findNumberRange);
        return new ResponseEntity<>(numberRangeList,HttpStatus.OK);
    }

    @ApiOperation(response = NumberRange.class, value = "Get Number Range Current") // label for swagger
    @GetMapping("/nextNumberRange")
    public ResponseEntity<?> getNumberRangeId(@RequestParam Long numberRangeCode, @RequestParam String numberRangeObject,
                                            @RequestParam String companyId,@RequestParam String languageId) {
        String nextRangeValue = numberRangeService.getNextNumberRange(numberRangeCode,numberRangeObject,languageId,companyId);
        return new ResponseEntity<>(nextRangeValue , HttpStatus.OK);
    }
}