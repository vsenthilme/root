package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.matterrate.AddMatterRate;
import com.mnrclara.api.management.model.matterrate.MatterRate;
import com.mnrclara.api.management.model.matterrate.UpdateMatterRate;
import com.mnrclara.api.management.service.MatterRateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterRate"}, value = "MatterRate Operations related to MatterRateController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterRate", description = "Operations related to MatterRate")})
@RequestMapping("/matterrate")
@RestController
public class MatterRateController {
	
	@Autowired
	MatterRateService matterRateService;
	
    @ApiOperation(response = MatterRate.class, value = "Get all MatterRate details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterRate> matterRateList = matterRateService.getMatterRates();
		return new ResponseEntity<>(matterRateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Get a MatterRate") // label for swagger 
	@GetMapping("/{matterNumber}/matterNumber")
	public ResponseEntity<?> getMatterRate(@PathVariable String matterNumber) {
    	List<MatterRate> matterrate = matterRateService.getMatterRate(matterNumber);
		return new ResponseEntity<>(matterrate, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Get a MatterRate") // label for swagger 
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterRate(@PathVariable String matterNumber, @RequestParam String timeKeeperCode) {
    	MatterRate matterrate = matterRateService.getMatterRate(matterNumber, timeKeeperCode);
		return new ResponseEntity<>(matterrate, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Create MatterRate") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterRate(@Valid @RequestBody AddMatterRate newMatterRate, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterRate createdMatterRate = matterRateService.createMatterRate(newMatterRate, loginUserID);
		return new ResponseEntity<>(createdMatterRate , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Create MatterRate") // label for swagger
   	@PostMapping("/batch")
   	public ResponseEntity<?> postBulkMatterRate(@Valid @RequestBody AddMatterRate[] newMatterRate, 
   			@RequestParam String loginUserID) 
   			throws IllegalAccessException, InvocationTargetException {
   		List<MatterRate> createdMatterRate = matterRateService.createBulkMatterRate(newMatterRate, loginUserID);
   		return new ResponseEntity<>(createdMatterRate , HttpStatus.OK);
   	}
    
    @ApiOperation(response = MatterRate.class, value = "Update MatterRate") // label for swagger
    @PatchMapping("/{matterNumber}")
	public ResponseEntity<?> patchMatterRate(@PathVariable String matterNumber, @RequestParam String timeKeeperCode,
			@Valid @RequestBody UpdateMatterRate updateMatterRate, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterRate updatedMatterRate = matterRateService.updateMatterRate(matterNumber, timeKeeperCode, updateMatterRate, loginUserID);
		return new ResponseEntity<>(updatedMatterRate , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Delete MatterRate") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterRate(@PathVariable String matterNumber, @RequestParam String timeKeeperCode, @RequestParam String loginUserID) {
    	matterRateService.deleteMatterRate(matterNumber, timeKeeperCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}