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

import com.mnrclara.api.setup.model.billingmode.AddBillingMode;
import com.mnrclara.api.setup.model.billingmode.BillingMode;
import com.mnrclara.api.setup.model.billingmode.UpdateBillingMode;
import com.mnrclara.api.setup.service.BillingModeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"BillingMode"}, value = "BillingMode Operations related to BillingModeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "BillingMode",description = "Operations related to BillingMode")})
@RequestMapping("/billingMode")
@RestController
public class BillingModeController {
	
	@Autowired
	BillingModeService billingModeService;
	
    @ApiOperation(response = BillingMode.class, value = "Get all BillingMode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<BillingMode> billingModeList = billingModeService.getBillingModes();
		return new ResponseEntity<>(billingModeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Get a BillingMode") // label for swagger 
	@GetMapping("/{billingModeId}")
	public ResponseEntity<?> getbillingMode(@PathVariable Long billingModeId) {
    	BillingMode billingMode = billingModeService.getBillingMode(billingModeId);
    	log.info("BillingMode : " + billingMode);
		return new ResponseEntity<>(billingMode, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Create BillingMode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addBillingMode(@Valid @RequestBody AddBillingMode newBillingMode, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode createdBillingMode = billingModeService.createBillingMode(newBillingMode, loginUserID);
		return new ResponseEntity<>(createdBillingMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Update BillingMode") // label for swagger
    @PatchMapping("/{billingModeId}")
	public ResponseEntity<?> patchBillingMode(@PathVariable Long billingModeId, @RequestParam String loginUserID, 
			@Valid @RequestBody UpdateBillingMode updateBillingMode) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode updatedBillingMode = billingModeService.updateBillingMode(billingModeId, loginUserID, updateBillingMode);
		return new ResponseEntity<>(updatedBillingMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Delete BillingMode") // label for swagger
	@DeleteMapping("/{billingModeId}")
	public ResponseEntity<?> deleteBillingMode(@PathVariable Long billingModeId, @RequestParam String loginUserID) {
    	billingModeService.deleteBillingMode(billingModeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}