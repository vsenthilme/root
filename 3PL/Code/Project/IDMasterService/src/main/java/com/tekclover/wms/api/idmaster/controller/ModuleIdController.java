package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.moduleid.*;
import com.tekclover.wms.api.idmaster.service.ModuleIdService;
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
@Api(tags = {"ModuleId"}, value = "ModuleId  Operations related to ModuleIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ModuleId ",description = "Operations related to ModuleId ")})
@RequestMapping("/moduleid")
@RestController
public class ModuleIdController {
	
	@Autowired
	ModuleIdService moduleidService;
	
    @ApiOperation(response = ModuleId.class, value = "Get all ModuleId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ModuleId> moduleidList = moduleidService.getModuleIds();
		return new ResponseEntity<>(moduleidList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ModuleId.class, value = "Get a ModuleId") // label for swagger 
	@GetMapping("/{moduleId}")
	public ResponseEntity<?> getModuleIds(@PathVariable String moduleId,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId) {
    	List<ModuleId> moduleid =
    			moduleidService.getModuleIdList(warehouseId,moduleId,companyCodeId,languageId,plantId);
    	log.info("ModuleId : " + moduleid);
		return new ResponseEntity<>(moduleid, HttpStatus.OK);
	}
    
    /*@ApiOperation(response = ModuleId.class, value = "Create ModuleId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postModuleId(@Valid @RequestBody AddModuleId newModuleId,
										  @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ModuleId createdModuleId = moduleidService.createModuleId(newModuleId, loginUserID);
		return new ResponseEntity<>(createdModuleId , HttpStatus.OK);
	} */

	@ApiOperation(response = ModuleId.class, value = "Create ModuleId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postModuleId(@Valid @RequestBody List<AddModuleId> newModuleId,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		List<ModuleId> createdModuleId = moduleidService.createModuleId(newModuleId, loginUserID);
		return new ResponseEntity<>(createdModuleId , HttpStatus.OK);
	}
    
  /*  @ApiOperation(response = ModuleId.class, value = "Update ModuleId") // label for swagger
    @PatchMapping("/{moduleId}")
	public ResponseEntity<?> patchModuleId(@PathVariable String moduleId,
			@RequestParam String warehouseId, @RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,
			@Valid @RequestBody UpdateModuleId updateModuleId, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ModuleId createdModuleId = 
				moduleidService.updateModuleId(warehouseId, moduleId,companyCodeId,languageId,plantId,loginUserID, updateModuleId);
		return new ResponseEntity<>(createdModuleId , HttpStatus.OK);
	}*/

	@ApiOperation(response = ModuleId.class, value = "Update ModuleId") // label for swagger
    @PatchMapping("/{moduleId}")
	public ResponseEntity<?> patchModuleId(@PathVariable String moduleId,
			@RequestParam String warehouseId, @RequestParam String companyCodeId,@RequestParam String languageId,@RequestParam String plantId,
			@Valid @RequestBody List<UpdateModuleId> updateModuleId, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		List<ModuleId> createdModuleId =
				moduleidService.updateModuleId(warehouseId, moduleId,companyCodeId,languageId,plantId,loginUserID, updateModuleId);
		return new ResponseEntity<>(createdModuleId , HttpStatus.OK);
	}
    
    @ApiOperation(response = ModuleId.class, value = "Delete ModuleId") // label for swagger
	@DeleteMapping("/{moduleId}")
	public ResponseEntity<?> deleteModuleId(@PathVariable String moduleId,
			@RequestParam String warehouseId,@RequestParam String companyCodeId,@RequestParam String languageId,
											@RequestParam String plantId,@RequestParam String loginUserID) {
    	moduleidService.deleteModuleId(warehouseId, moduleId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//Search
	@ApiOperation(response = ModuleId.class, value = "Find ModuleId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findModuleId(@Valid @RequestBody FindModuleId findModuleId) throws Exception {
		List<ModuleId> createdUomId = moduleidService.findModuleId(findModuleId);
		return new ResponseEntity<>(createdUomId, HttpStatus.OK);
	}

}