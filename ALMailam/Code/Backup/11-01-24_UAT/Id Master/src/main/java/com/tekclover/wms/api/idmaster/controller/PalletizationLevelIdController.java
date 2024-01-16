package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.palletizationlevelid.*;
import com.tekclover.wms.api.idmaster.service.PalletizationLevelIdService;
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
import java.text.ParseException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"PalletizationLevelId"}, value = "PalletizationLevelId  Operations related to PalletizationLevelIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PalletizationLevelId ",description = "Operations related to PalletizationLevelId ")})
@RequestMapping("/palletizationlevelid")
@RestController
public class PalletizationLevelIdController {

	@Autowired
	PalletizationLevelIdService palletizationlevelidService;
	
    @ApiOperation(response = PalletizationLevelId.class, value = "Get all PalletizationLevelId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PalletizationLevelId> palletizationlevelidList = palletizationlevelidService.getPalletizationLevelIds();
		return new ResponseEntity<>(palletizationlevelidList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = PalletizationLevelId.class, value = "Get a PalletizationLevelId") // label for swagger 
	@GetMapping("/{palletizationLevelId}")
	public ResponseEntity<?> getPalletizationLevelId(@PathVariable String palletizationLevelId,@RequestParam String palletizationLevel,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId
													 ) {
    	PalletizationLevelId palletizationlevelid = 
    			palletizationlevelidService.getPalletizationLevelId(warehouseId, palletizationLevelId, palletizationLevel,companyCodeId,languageId,plantId);
    	log.info("PalletizationLevelId : " + palletizationlevelid);
		return new ResponseEntity<>(palletizationlevelid, HttpStatus.OK);
	}
    
    @ApiOperation(response = PalletizationLevelId.class, value = "Create PalletizationLevelId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postPalletizationLevelId(@Valid @RequestBody AddPalletizationLevelId newPalletizationLevelId, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
		PalletizationLevelId createdPalletizationLevelId = palletizationlevelidService.createPalletizationLevelId(newPalletizationLevelId, loginUserID);
		return new ResponseEntity<>(createdPalletizationLevelId , HttpStatus.OK);
	}
    
    @ApiOperation(response = PalletizationLevelId.class, value = "Update PalletizationLevelId") // label for swagger
    @PatchMapping("/{palletizationLevelId}")
	public ResponseEntity<?> patchPalletizationLevelId(@PathVariable String palletizationLevelId, @RequestParam String palletizationLevel,
			@RequestParam String warehouseId, @RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdatePalletizationLevelId updatePalletizationLevelId)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		PalletizationLevelId createdPalletizationLevelId = 
				palletizationlevelidService.updatePalletizationLevelId(warehouseId,palletizationLevelId, palletizationLevel,companyCodeId,languageId,plantId,loginUserID, updatePalletizationLevelId);
		return new ResponseEntity<>(createdPalletizationLevelId , HttpStatus.OK);
	}
    
    @ApiOperation(response = PalletizationLevelId.class, value = "Delete PalletizationLevelId") // label for swagger
	@DeleteMapping("/{palletizationLevelId}")
	public ResponseEntity<?> deletePalletizationLevelId(@PathVariable String palletizationLevelId,@RequestParam String palletizationLevel,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId, @RequestParam String loginUserID) {
    	palletizationlevelidService.deletePalletizationLevelId(warehouseId, palletizationLevelId, palletizationLevel,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//Search
	@ApiOperation(response = PalletizationLevelId.class, value = "Find PalletizationLevelId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findPalletizationLevelId(@Valid @RequestBody FindPalletizationLevelId findPalletizationLevelId) throws Exception {
		List<PalletizationLevelId> createdPalletizationLevelId = palletizationlevelidService.findPalletizationLevelId(findPalletizationLevelId);
		return new ResponseEntity<>(createdPalletizationLevelId, HttpStatus.OK);
	}
}