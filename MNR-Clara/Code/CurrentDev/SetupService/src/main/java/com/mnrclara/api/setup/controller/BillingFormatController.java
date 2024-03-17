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

import com.mnrclara.api.setup.model.billingformat.AddBillingFormat;
import com.mnrclara.api.setup.model.billingformat.BillingFormat;
import com.mnrclara.api.setup.model.billingformat.UpdateBillingFormat;
import com.mnrclara.api.setup.service.BillingFormatService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"BillingFormat"}, value = "BillingFormat Operations related to BillingFormatController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "BillingFormat",description = "Operations related to BillingFormat")})
@RequestMapping("/billingFormat")
@RestController
public class BillingFormatController {
	
	@Autowired
	BillingFormatService billingFormatService;
	
    @ApiOperation(response = BillingFormat.class, value = "Get all BillingFormat details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<BillingFormat> billingFormatList = billingFormatService.getBillingFormats();
		return new ResponseEntity<>(billingFormatList, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFormat.class, value = "Get a BillingFormat") // label for swagger 
	@GetMapping("/{billingFormatId}")
	public ResponseEntity<?> getBillingFormat(@PathVariable Long billingFormatId) {
    	BillingFormat billingFormat = billingFormatService.getBillingFormat(billingFormatId);
    	log.info("BillingFormat : " + billingFormat);
		return new ResponseEntity<>(billingFormat, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFormat.class, value = "Create BillingFormat") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addBillingFormat(@Valid @RequestBody AddBillingFormat newBillingFormat, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFormat createdBillingFormat = billingFormatService.createBillingFormat(newBillingFormat, loginUserID);
		return new ResponseEntity<>(createdBillingFormat , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFormat.class, value = "Update BillingFormat") // label for swagger
    @PatchMapping("/{billingFormatId}")
	public ResponseEntity<?> patchBillingFormat(@PathVariable Long billingFormatId, @RequestParam String loginUserID, 
			@Valid @RequestBody UpdateBillingFormat updateBillingFormat) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFormat updatedBillingFormat = billingFormatService.updateBillingFormat(billingFormatId, loginUserID, updateBillingFormat);
		return new ResponseEntity<>(updatedBillingFormat , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingFormat.class, value = "Delete BillingFormat") // label for swagger
	@DeleteMapping("/{billingFormatId}")
	public ResponseEntity<?> deleteBillingFormat(@PathVariable Long billingFormatId, @RequestParam String loginUserID) {
    	billingFormatService.deleteBillingFormat(billingFormatId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}