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

import com.mnrclara.api.setup.model.billingfrequency.AddBillingFrequency;
import com.mnrclara.api.setup.model.billingfrequency.BillingFrequency;
import com.mnrclara.api.setup.model.billingfrequency.UpdateBillingFrequency;
import com.mnrclara.api.setup.service.BillingFrequencyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"BillingFrequency"}, value = "BillingFrequency Operations related to BillingFrequencyController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "BillingFrequency",description = "Operations related to BillingFrequency")})
@RequestMapping("/billingFrequency")
@RestController
public class BillingFrequencyController {
	
	@Autowired
	BillingFrequencyService billingFrequencyService;
	
    @ApiOperation(response = BillingFrequency.class, value = "Get all BillingFrequency details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<BillingFrequency> billingFrequencyList = billingFrequencyService.getBillingFrequencies();
		return new ResponseEntity<>(billingFrequencyList, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFrequency.class, value = "Get a BillingFrequency") // label for swagger 
	@GetMapping("/{billingFrequencyId}")
	public ResponseEntity<?> getBillingFrequency(@PathVariable Long billingFrequencyId) {
    	BillingFrequency billingFrequency = billingFrequencyService.getBillingFrequency(billingFrequencyId);
    	log.info("BillingFrequency : " + billingFrequency);
		return new ResponseEntity<>(billingFrequency, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFrequency.class, value = "Create BillingFrequency") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addBillingFrequency(@Valid @RequestBody AddBillingFrequency newBillingFrequency, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFrequency createdBillingFrequency = billingFrequencyService.createBillingFrequency(newBillingFrequency, loginUserID);
		return new ResponseEntity<>(createdBillingFrequency , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFrequency.class, value = "Update BillingFrequency") // label for swagger
    @PatchMapping("/{billingFrequencyId}")
	public ResponseEntity<?> patchBillingFrequency(@PathVariable Long billingFrequencyId, 
			@RequestParam String loginUserID, @Valid @RequestBody UpdateBillingFrequency updateBillingFrequency) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFrequency updatedBillingFrequency = 
				billingFrequencyService.updateBillingFrequency(billingFrequencyId, loginUserID, updateBillingFrequency);
		return new ResponseEntity<>(updatedBillingFrequency , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFrequency.class, value = "Delete BillingFrequency") // label for swagger
	@DeleteMapping("/{billingFrequencyId}")
	public ResponseEntity<?> deleteBillingFrequency(@PathVariable Long billingFrequencyId, @RequestParam String loginUserID) {
    	billingFrequencyService.deleteBillingFrequency(billingFrequencyId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}