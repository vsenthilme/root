package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.dockid.*;
import com.tekclover.wms.api.idmaster.repository.PlantIdRepository;
import com.tekclover.wms.api.idmaster.service.DockIdService;
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
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"DockId"}, value = "DockId  Operations related to DockIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "DockId ",description = "Operations related to DockId ")})
@RequestMapping("/dockid")
@RestController
public class DockIdController {
	@Autowired
	private PlantIdRepository plantIdRepository;

	@Autowired
	DockIdService dockidService;
	
    @ApiOperation(response = DockId.class, value = "Get all DockId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<DockId> dockidList = dockidService.getDockIds();
		return new ResponseEntity<>(dockidList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = DockId.class, value = "Get a DockId") // label for swagger 
	@GetMapping("/{dockId}")
	public ResponseEntity<?> getDockId(@PathVariable String dockId,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId) {
    	DockId dockid = 
    			dockidService.getDockId(warehouseId,dockId,companyCodeId,languageId,plantId);
    	log.info("DockId : " + dockid);
		return new ResponseEntity<>(dockid, HttpStatus.OK);
	}
    
    @ApiOperation(response = DockId.class, value = "Create DockId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postDockId(@Valid @RequestBody AddDockId newDockId, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		DockId createdDockId = dockidService.createDockId(newDockId, loginUserID);
		return new ResponseEntity<>(createdDockId , HttpStatus.OK);
	}
    
    @ApiOperation(response = DockId.class, value = "Update DockId") // label for swagger
    @PatchMapping("/{dockId}")
	public ResponseEntity<?> patchDockId(@PathVariable String dockId,
			@RequestParam String warehouseId, @RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,
			@Valid @RequestBody UpdateDockId updateDockId, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DockId createdDockId = 
				dockidService.updateDockId(warehouseId,dockId,companyCodeId,languageId,plantId,loginUserID, updateDockId);
		return new ResponseEntity<>(createdDockId , HttpStatus.OK);
	}
    
    @ApiOperation(response = DockId.class, value = "Delete DockId") // label for swagger
	@DeleteMapping("/{dockId}")
	public ResponseEntity<?> deleteDockId(@PathVariable String dockId,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,@RequestParam String loginUserID) {
    	dockidService.deleteDockId(warehouseId,dockId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = DockId.class, value = "Find DockId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findDockId(@Valid @RequestBody FindDockId findDockId) throws Exception {
		List<DockId> createdDockId = dockidService.findDockId(findDockId);
		return new ResponseEntity<>(createdDockId, HttpStatus.OK);
	}
}