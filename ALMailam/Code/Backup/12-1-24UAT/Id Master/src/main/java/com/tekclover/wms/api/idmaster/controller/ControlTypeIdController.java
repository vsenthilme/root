package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.controltypeid.*;
import com.tekclover.wms.api.idmaster.service.ControlTypeIdService;
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
@Api(tags = {"ControlTypeId"}, value = "ControlTypeId  Operations related to ControlTypeIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ControlTypeId ",description = "Operations related to ControlTypeId ")})
@RequestMapping("/controltypeid")
@RestController
public class ControlTypeIdController {
	
	@Autowired
	ControlTypeIdService controltypeidService;
	
    @ApiOperation(response = ControlTypeId.class, value = "Get all ControlTypeId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ControlTypeId> controltypeidList = controltypeidService.getControlTypeIds();
		return new ResponseEntity<>(controltypeidList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ControlTypeId.class, value = "Get a ControlTypeId") // label for swagger 
	@GetMapping("/{controlTypeId}")
	public ResponseEntity<?> getControlTypeId(@RequestParam String warehouseId,@PathVariable String controlTypeId,@RequestParam String companyCodeId,
											  @RequestParam String languageId,@RequestParam String plantId) {
    	ControlTypeId controltypeid = 
    			controltypeidService.getControlTypeId(warehouseId, controlTypeId,companyCodeId,languageId,plantId);
    	log.info("ControlTypeId : " + controltypeid);
		return new ResponseEntity<>(controltypeid, HttpStatus.OK);
	}
    
    @ApiOperation(response = ControlTypeId.class, value = "Create ControlTypeId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postControlTypeId(@Valid @RequestBody AddControlTypeId newControlTypeId, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
		ControlTypeId createdControlTypeId = controltypeidService.createControlTypeId(newControlTypeId, loginUserID);
		return new ResponseEntity<>(createdControlTypeId , HttpStatus.OK);
	}
    
    @ApiOperation(response = ControlTypeId.class, value = "Update ControlTypeId") // label for swagger
    @PatchMapping("/{controlTypeId}")
	public ResponseEntity<?> patchControlTypeId(@RequestParam String warehouseId,@PathVariable String controlTypeId,
												@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,
			@Valid @RequestBody UpdateControlTypeId updateControlTypeId, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		ControlTypeId createdControlTypeId = 
				controltypeidService.updateControlTypeId(warehouseId, controlTypeId,companyCodeId,languageId,plantId,loginUserID, updateControlTypeId);
		return new ResponseEntity<>(createdControlTypeId , HttpStatus.OK);
	}
    
    @ApiOperation(response = ControlTypeId.class, value = "Delete ControlTypeId") // label for swagger
	@DeleteMapping("/{controlTypeId}")
	public ResponseEntity<?> deleteControlTypeId(@RequestParam String warehouseId,@PathVariable String controlTypeId,
												 @RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,
			 @RequestParam String loginUserID) {
    	controltypeidService.deleteControlTypeId(warehouseId, controlTypeId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = ControlTypeId.class, value = "Find ControlTypeId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findControlTypeId(@Valid @RequestBody FindControlTypeId findControlTypeId) throws Exception {
		List<ControlTypeId> createdControlTypeId = controltypeidService.findControlTypeId(findControlTypeId);
		return new ResponseEntity<>(createdControlTypeId, HttpStatus.OK);
	}
}