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

import com.mnrclara.api.management.model.matterfeesharing.AddMatterFeeSharing;
import com.mnrclara.api.management.model.matterfeesharing.MatterFeeSharing;
import com.mnrclara.api.management.model.matterfeesharing.UpdateMatterFeeSharing;
import com.mnrclara.api.management.service.MatterFeeSharingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterFeeSharing"}, value = "MatterFeeSharing Operations related to MatterFeeSharingController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterFeeSharing",description = "Operations related to MatterFeeSharing")})
@RequestMapping("/matterfeesharing")
@RestController
public class MatterFeeSharingController {
	
	@Autowired
	MatterFeeSharingService matterFeeSharingService;
	
    @ApiOperation(response = MatterFeeSharing.class, value = "Get all MatterFeeSharing details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterFeeSharing> matterFeeSharingList = matterFeeSharingService.getMatterFeeSharings();
		return new ResponseEntity<>(matterFeeSharingList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Get a MatterFeeSharing") // label for swagger 
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterFeeSharing(@PathVariable String matterNumber, @RequestParam String timeKeeperCode) {
    	MatterFeeSharing matterfeesharing = matterFeeSharingService.getMatterFeeSharing(matterNumber, timeKeeperCode);
    	log.info("MatterFeeSharing : " + matterfeesharing);
		return new ResponseEntity<>(matterfeesharing, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Create MatterFeeSharing") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterFeeSharing(@Valid @RequestBody AddMatterFeeSharing newMatterFeeSharing, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		MatterFeeSharing createdMatterFeeSharing = matterFeeSharingService.createMatterFeeSharing(newMatterFeeSharing, loginUserID);
		return new ResponseEntity<>(createdMatterFeeSharing , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Update MatterFeeSharing") // label for swagger
    @PatchMapping("/{matterNumber}")
	public ResponseEntity<?> patchMatterFeeSharing(@PathVariable String matterNumber, @RequestParam String timeKeeperCode,
			@Valid @RequestBody UpdateMatterFeeSharing updateMatterFeeSharing, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterFeeSharing updatedMatterFeeSharing = 
				matterFeeSharingService.updateMatterFeeSharing(matterNumber, timeKeeperCode, updateMatterFeeSharing, loginUserID);
		return new ResponseEntity<>(updatedMatterFeeSharing , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Delete MatterFeeSharing") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterFeeSharing(@PathVariable String matterNumber, @RequestParam String timeKeeperCode, @RequestParam String loginUserID){
    	matterFeeSharingService.deleteMatterFeeSharing(matterNumber, timeKeeperCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}