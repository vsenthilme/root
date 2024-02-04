package com.tekclover.wms.api.idmaster.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.tekclover.wms.api.idmaster.model.floorid.FindFloorId;
import com.tekclover.wms.api.idmaster.repository.LanguageIdRepository;
import com.tekclover.wms.api.idmaster.repository.PlantIdRepository;
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

import com.tekclover.wms.api.idmaster.model.floorid.AddFloorId;
import com.tekclover.wms.api.idmaster.model.floorid.FloorId;
import com.tekclover.wms.api.idmaster.model.floorid.UpdateFloorId;
import com.tekclover.wms.api.idmaster.service.FloorIdService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"FloorId"}, value = "FloorId  Operations related to FloorIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "FloorId ",description = "Operations related to FloorId ")})
@RequestMapping("/floorid")
@RestController
public class FloorIdController {
	@Autowired
	private PlantIdRepository plantIdRepository;
	@Autowired
	private LanguageIdRepository languageIdRepository;

	@Autowired
	FloorIdService flooridService;

	@ApiOperation(response = FloorId.class, value = "Get all FloorId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<FloorId> flooridList = flooridService.getFloorIds();
		return new ResponseEntity<>(flooridList, HttpStatus.OK);
	}

	@ApiOperation(response = FloorId.class, value = "Get a FloorId") // label for swagger
	@GetMapping("/{floorId}")
	public ResponseEntity<?> getFloorId(@RequestParam String warehouseId,@PathVariable Long floorId, @RequestParam String companyCodeId,
										@RequestParam String languageId,@RequestParam String plantId) {
		FloorId floorid =
				flooridService.getFloorId(warehouseId,floorId,companyCodeId,languageId,plantId);
		log.info("FloorId : " + floorid);
		return new ResponseEntity<>(floorid, HttpStatus.OK);
	}

	@ApiOperation(response = FloorId.class, value = "Create FloorId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postFloorId(@Valid @RequestBody AddFloorId newFloorId,
										 @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
		FloorId createdFloorId = flooridService.createFloorId(newFloorId, loginUserID);
		return new ResponseEntity<>(createdFloorId , HttpStatus.OK);
	}

	@ApiOperation(response = FloorId.class, value = "Update FloorId") // label for swagger
	@PatchMapping("/{floorId}")
	public ResponseEntity<?> patchFloorId(@RequestParam String warehouseId,@PathVariable Long floorId,@RequestParam String companyCodeId,
										  @RequestParam String languageId,@RequestParam String plantId,@RequestParam String loginUserID,
										  @Valid @RequestBody UpdateFloorId updateFloorId)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		FloorId createdFloorId =
				flooridService.updateFloorId(warehouseId,floorId,companyCodeId,languageId,plantId,loginUserID, updateFloorId);
		return new ResponseEntity<>(createdFloorId , HttpStatus.OK);
	}

	@ApiOperation(response = FloorId.class, value = "Delete FloorId") // label for swagger
	@DeleteMapping("/{floorId}")
	public ResponseEntity<?> deleteFloorId(@RequestParam String warehouseId,@PathVariable Long floorId,@RequestParam String companyCodeId,
										   @RequestParam String languageId,@RequestParam String plantId,@RequestParam String loginUserID) {
		flooridService.deleteFloorId(warehouseId,floorId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = FloorId.class, value = "Find FloorId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findFloorId(@Valid @RequestBody FindFloorId findFloorId) throws Exception {
		List<FloorId> createdFloorId = flooridService.findFloorId(findFloorId);
		return new ResponseEntity<>(createdFloorId, HttpStatus.OK);
	}
}