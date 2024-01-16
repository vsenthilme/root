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

import com.mnrclara.api.setup.model.deadlinecalculator.AddDeadlineCalculator;
import com.mnrclara.api.setup.model.deadlinecalculator.DeadlineCalculator;
import com.mnrclara.api.setup.model.deadlinecalculator.UpdateDeadlineCalculator;
import com.mnrclara.api.setup.service.DeadlineCalculatorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"DeadlineCalculator"}, value = "DeadlineCalculator Operations related to DeadlineCalculatorController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "DeadlineCalculator",description = "Operations related to DeadlineCalculator")})
@RequestMapping("/deadlineCalculator")
@RestController
public class DeadlineCalculatorController {
	
	@Autowired
	DeadlineCalculatorService deadlineCalculatorService;
	
    @ApiOperation(response = DeadlineCalculator.class, value = "Get all DeadlineCalculator details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<DeadlineCalculator> deadlineCalculatorList = deadlineCalculatorService.getDeadlineCalculators();
		return new ResponseEntity<>(deadlineCalculatorList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Get a DeadlineCalculator") // label for swagger 
	@GetMapping("/{deadLineCalculationId}")
	public ResponseEntity<?> getDeadlineCalculator(@PathVariable Long deadLineCalculationId) {
    	DeadlineCalculator deadlineCalculator = deadlineCalculatorService.getDeadlineCalculator(deadLineCalculationId);
    	log.info("DeadlineCalculator : " + deadlineCalculator);
		return new ResponseEntity<>(deadlineCalculator, HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Create DeadlineCalculator") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postDeadlineCalculator(@Valid @RequestBody AddDeadlineCalculator newDeadlineCalculator, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator createdDeadlineCalculator = deadlineCalculatorService.createDeadlineCalculator(newDeadlineCalculator, loginUserID);
		return new ResponseEntity<>(createdDeadlineCalculator , HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Update DeadlineCalculator") // label for swagger
    @PatchMapping("/{deadLineCalculationId}")
	public ResponseEntity<?> patchDeadlineCalculator(@PathVariable Long deadLineCalculationId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateDeadlineCalculator updateDeadlineCalculator) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator updatedDeadlineCalculator = 
				deadlineCalculatorService.updateDeadlineCalculator(deadLineCalculationId, loginUserID, updateDeadlineCalculator);
		return new ResponseEntity<>(updatedDeadlineCalculator , HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Delete DeadlineCalculator") // label for swagger
	@DeleteMapping("/{deadLineCalculationId}")
	public ResponseEntity<?> deleteDeadlineCalculator(@PathVariable Long deadLineCalculationId, @RequestParam String loginUserID) {
    	deadlineCalculatorService.deleteDeadlineCalculator(deadLineCalculationId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}