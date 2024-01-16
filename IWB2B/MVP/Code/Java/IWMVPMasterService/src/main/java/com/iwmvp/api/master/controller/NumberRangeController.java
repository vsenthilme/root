package com.iwmvp.api.master.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.iwmvp.api.master.model.numberrange.*;
import com.iwmvp.api.master.service.NumberRangeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"NumberRange"}, value = "NumberRange Operations related to NumberRangeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NumberRange",description = "Operations related to NumberRange")})
@RequestMapping("/numberrange")
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
	public ResponseEntity<?> getNumberRange(@PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,
											@RequestParam String companyId,@RequestParam String languageId) {
		NumberRange numberRange = numberRangeService.getNumberRange(numberRangeCode,numberRangeObject,companyId,languageId);
		log.info("NumberRange : " + numberRange);
		return new ResponseEntity<>(numberRange, HttpStatus.OK);
	}
	@ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postNumberRange(@Valid @RequestBody AddNumberRange newNumberRange,
												 @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		NumberRange createdNumberRange = numberRangeService.createNumberRange(newNumberRange, loginUserID);
		return new ResponseEntity<>(createdNumberRange, HttpStatus.OK);
	}
	@ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
	@PatchMapping("/{numberRangeCode}")
	public ResponseEntity<?> patchNumberRange(@PathVariable Long numberRangeCode,@RequestParam String  numberRangeObject,@RequestParam String companyId,@RequestParam String languageId,
												  @Valid @RequestBody UpdateNumberRange updateNumberRange, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		NumberRange UpdateNumberRange =
				numberRangeService.updateNumberRange(numberRangeCode, numberRangeObject,companyId,languageId,loginUserID, updateNumberRange);
		return new ResponseEntity<>(UpdateNumberRange, HttpStatus.OK);
	}
	@ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
	@DeleteMapping("/{numberRangeCode}")
	public ResponseEntity<?> deleteNumberRange(@PathVariable Long numberRangeCode,@RequestParam String numberRangeObject,@RequestParam String companyId,@RequestParam String languageId,
												   @RequestParam String loginUserID) {
		numberRangeService.deleteNumberRange(numberRangeCode,numberRangeObject,companyId,languageId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Find
	@ApiOperation(response = NumberRange.class, value = "Find NumberRange") // label for swagger
	@PostMapping("/findNumberRange")
	public ResponseEntity<?> findNumberRange(@Valid @RequestBody FindNumberRange findNumberRange) throws Exception {
		List<NumberRange> createdNumberRange = numberRangeService.findNumberRange(findNumberRange);
		return new ResponseEntity<>(createdNumberRange, HttpStatus.OK);
	}
}