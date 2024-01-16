package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.spanid.*;
import com.tekclover.wms.api.idmaster.service.SpanIdService;
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
@Api(tags = {"SpanId"}, value = "SpanId  Operations related to SpanIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "SpanId ",description = "Operations related to SpanId ")})
@RequestMapping("/spanid")
@RestController
public class SpanIdController {
	
	@Autowired
	SpanIdService spanidService;
	
    @ApiOperation(response = SpanId.class, value = "Get all SpanId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<SpanId> spanidList = spanidService.getSpanIds();
		return new ResponseEntity<>(spanidList, HttpStatus.OK); 
	}

    @ApiOperation(response = SpanId.class, value = "Get a SpanId") // label for swagger 
	@GetMapping("/{spanId}")
	public ResponseEntity<?> getSpanId(@RequestParam String warehouseId,@RequestParam String aisleId,@PathVariable String spanId,
									   @RequestParam Long floorId, @RequestParam String storageSectionId,@RequestParam String companyCodeId,
									   @RequestParam String languageId,@RequestParam String plantId) {
    	SpanId spanid = 
    			spanidService.getSpanId(warehouseId,aisleId,spanId,floorId,storageSectionId,companyCodeId,languageId,plantId);
    	log.info("SpanId : " + spanid);
		return new ResponseEntity<>(spanid, HttpStatus.OK);
	}
    
    @ApiOperation(response = SpanId.class, value = "Create SpanId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postSpanId(@Valid @RequestBody AddSpanId newSpanId, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		SpanId createdSpanId = spanidService.createSpanId(newSpanId, loginUserID);
		return new ResponseEntity<>(createdSpanId , HttpStatus.OK);
	}
    
    @ApiOperation(response = SpanId.class, value = "Update SpanId") // label for swagger
    @PatchMapping("/{spanId}")
	public ResponseEntity<?> patchSpanId(@RequestParam String warehouseId,@RequestParam String aisleId,@PathVariable String spanId,
										 @RequestParam Long floorId, @RequestParam String storageSectionId,@RequestParam String companyCodeId,
										 @RequestParam String languageId,@RequestParam String plantId,@RequestParam String loginUserID,
			@Valid @RequestBody UpdateSpanId updateSpanId)
			throws IllegalAccessException, InvocationTargetException {
		SpanId createdSpanId = 
				spanidService.updateSpanId(warehouseId,aisleId, spanId,floorId, storageSectionId,companyCodeId,languageId,plantId,loginUserID, updateSpanId);
		return new ResponseEntity<>(createdSpanId , HttpStatus.OK);
	}
    
    @ApiOperation(response = SpanId.class, value = "Delete SpanId") // label for swagger
	@DeleteMapping("/{spanId}")
	public ResponseEntity<?> deleteSpanId(@RequestParam String warehouseId,@RequestParam String aisleId,@PathVariable String spanId,
										  @RequestParam Long floorId, @RequestParam String storageSectionId,@RequestParam String companyCodeId,
										  @RequestParam String languageId,@RequestParam String plantId, @RequestParam String loginUserID) {
    	spanidService.deleteSpanId(warehouseId,aisleId,spanId,floorId,storageSectionId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = SpanId.class, value = "Find SpanId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findSpanId(@Valid @RequestBody FindSpanId findSpanId) throws Exception {
		List<SpanId> createdSpanId = spanidService.findSpanId(findSpanId);
		return new ResponseEntity<>(createdSpanId, HttpStatus.OK);
	}
}