package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.numberange.AddNumberRange;
import com.mnrclara.api.setup.model.numberange.NumberRange;
import com.mnrclara.api.setup.model.numberange.UpdateNumberRange;
import com.mnrclara.api.setup.service.NumberRangeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"NumberRange"}, value = "NumberRange Operations related to NumberRangeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NumberRange",description = "Operations related to NumberRange")})
@RequestMapping("/numberRange")
@RestController
public class NumberRangeController {
	
	@Autowired
	NumberRangeService numberRangeService;
	
    @ApiOperation(response = NumberRange.class, value = "Get all NumberRange details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<NumberRange> numberRangeList = numberRangeService.getNumberRanges();
		return new ResponseEntity<>(numberRangeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Get a NumberRange") // label for swagger 
	@GetMapping("/{numberRangeCode}")
	public ResponseEntity<?> getNumberRange(@PathVariable Long numberRangeCode) {
    	NumberRange numberRange = numberRangeService.getNumberRange(numberRangeCode);
    	log.info("NumberRange : " + numberRange);
		return new ResponseEntity<>(numberRange, HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Get Number Range Current") // label for swagger
	@GetMapping("/nextNumberRange")
	public ResponseEntity<?> getNumberRange(@RequestParam Long numberRangeCode, @RequestParam Long classID) {
		String nextRangeValue = numberRangeService.getNextNumberRange(numberRangeCode, classID);
		return new ResponseEntity<>(nextRangeValue , HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postNumberRange(@Valid @RequestBody AddNumberRange newNumberRange, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		NumberRange createdNumberRange = numberRangeService.createNumberRange(newNumberRange, loginUserID);
		return new ResponseEntity<>(createdNumberRange , HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
    @PatchMapping("/{numberRangeCode}")
	public ResponseEntity<?> patchNumberRange(@PathVariable Long numberRangeCode, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateNumberRange updateNumberRange) 
			throws IllegalAccessException, InvocationTargetException {
		NumberRange updatedNumberRange = numberRangeService.updateNumberRange(numberRangeCode, loginUserID, updateNumberRange);
		return new ResponseEntity<>(updatedNumberRange , HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
	@DeleteMapping("/{numberRangeCode}")
	public ResponseEntity<?> deleteNumberRange(@PathVariable Long numberRangeCode) {
    	numberRangeService.deleteNumberRange(numberRangeCode);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}