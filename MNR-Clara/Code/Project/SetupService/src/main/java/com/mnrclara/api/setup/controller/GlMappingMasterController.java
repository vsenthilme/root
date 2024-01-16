package com.mnrclara.api.setup.controller;

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

import com.mnrclara.api.setup.model.glmappingmaster.AddGlMappingMaster;
import com.mnrclara.api.setup.model.glmappingmaster.GlMappingMaster;
import com.mnrclara.api.setup.model.glmappingmaster.UpdateGlMappingMaster;
import com.mnrclara.api.setup.service.GlMappingMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"GlMappingMaster"}, value = "GlMappingMaster  Operations related to GlMappingMasterController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "GlMappingMaster ",description = "Operations related to GlMappingMaster ")})
@RequestMapping("/glmappingmaster")
@RestController
public class GlMappingMasterController {
	
	@Autowired
	GlMappingMasterService glmappingmasterService;
	
    @ApiOperation(response = GlMappingMaster.class, value = "Get all GlMappingMaster details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<GlMappingMaster> glmappingmasterList = glmappingmasterService.getGlMappingMasters();
		return new ResponseEntity<>(glmappingmasterList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = GlMappingMaster.class, value = "Get a GlMappingMaster") // label for swagger 
	@GetMapping("/{itemNumber}")
	public ResponseEntity<?> getGlMappingMaster(@PathVariable Long itemNumber) {
    	GlMappingMaster glmappingmaster = glmappingmasterService.getGlMappingMaster(itemNumber);
    	log.info("GlMappingMaster : " + glmappingmaster);
		return new ResponseEntity<>(glmappingmaster, HttpStatus.OK);
	}
    
    @ApiOperation(response = GlMappingMaster.class, value = "Create GlMappingMaster") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postGlMappingMaster(@Valid @RequestBody AddGlMappingMaster newGlMappingMaster, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster createdGlMappingMaster = glmappingmasterService.createGlMappingMaster(newGlMappingMaster, loginUserID);
		return new ResponseEntity<>(createdGlMappingMaster , HttpStatus.OK);
	}
    
    @ApiOperation(response = GlMappingMaster.class, value = "Update GlMappingMaster") // label for swagger
    @PatchMapping("/{itemNumber}")
	public ResponseEntity<?> patchGlMappingMaster(@PathVariable Long itemNumber, 
			@Valid @RequestBody UpdateGlMappingMaster updateGlMappingMaster, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster createdGlMappingMaster = 
				glmappingmasterService.updateGlMappingMaster(itemNumber, updateGlMappingMaster, loginUserID);
		return new ResponseEntity<>(createdGlMappingMaster , HttpStatus.OK);
	}
    
    @ApiOperation(response = GlMappingMaster.class, value = "Delete GlMappingMaster") // label for swagger
	@DeleteMapping("/{itemNumber}")
	public ResponseEntity<?> deleteGlMappingMaster(@PathVariable Long itemNumber, @RequestParam String loginUserID) {
    	glmappingmasterService.deleteGlMappingMaster(itemNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}