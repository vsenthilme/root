package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.inboundorderstatusid.*;
import com.tekclover.wms.api.idmaster.service.InboundOrderStatusIdService;
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
@Api(tags = {"InboundOrderStatusId"}, value = "InboundOrderStatusId  Operations related to InboundOrderStatusIdController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "InboundOrderStatusId ",description = "Operations related to InboundOrderStatusId ")})
@RequestMapping("/inboundorderstatusid")
@RestController
public class InboundOrderStatusIdController {
	
	@Autowired
	InboundOrderStatusIdService inboundorderstatusidService;
	
    @ApiOperation(response = InboundOrderStatusId.class, value = "Get all InboundOrderStatusId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<InboundOrderStatusId> inboundorderstatusidList = inboundorderstatusidService.getInboundOrderStatusIds();
		return new ResponseEntity<>(inboundorderstatusidList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = InboundOrderStatusId.class, value = "Get a InboundOrderStatusId") // label for swagger 
	@GetMapping("/{inboundOrderStatusId}")
	public ResponseEntity<?> getInboundOrderStatusId(@RequestParam String warehouseId, @PathVariable String inboundOrderStatusId,
													 @RequestParam String companyCodeId, @RequestParam String languageId,@RequestParam String plantId) {
    	InboundOrderStatusId inboundorderstatusid = 
    			inboundorderstatusidService.getInboundOrderStatusId(warehouseId, inboundOrderStatusId,companyCodeId,languageId,plantId);
    	log.info("InboundOrderStatusId : " + inboundorderstatusid);
		return new ResponseEntity<>(inboundorderstatusid, HttpStatus.OK);
	}
    
    @ApiOperation(response = InboundOrderStatusId.class, value = "Create InboundOrderStatusId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postInboundOrderStatusId(@Valid @RequestBody AddInboundOrderStatusId newInboundOrderStatusId, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
		InboundOrderStatusId createdInboundOrderStatusId = inboundorderstatusidService.createInboundOrderStatusId(newInboundOrderStatusId, loginUserID);
		return new ResponseEntity<>(createdInboundOrderStatusId , HttpStatus.OK);
	}
    
    @ApiOperation(response = InboundOrderStatusId.class, value = "Update InboundOrderStatusId") // label for swagger
    @PatchMapping("/{inboundOrderStatusId}")
	public ResponseEntity<?> patchInboundOrderStatusId(@RequestParam String warehouseId, @PathVariable String inboundOrderStatusId,@RequestParam String companyCodeId,
													   @RequestParam String languageId,@RequestParam String plantId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateInboundOrderStatusId updateInboundOrderStatusId)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		InboundOrderStatusId createdInboundOrderStatusId = 
				inboundorderstatusidService.updateInboundOrderStatusId(warehouseId, inboundOrderStatusId,companyCodeId,languageId,plantId,loginUserID, updateInboundOrderStatusId);
		return new ResponseEntity<>(createdInboundOrderStatusId , HttpStatus.OK);
	}
    
    @ApiOperation(response = InboundOrderStatusId.class, value = "Delete InboundOrderStatusId") // label for swagger
	@DeleteMapping("/{inboundOrderStatusId}")
	public ResponseEntity<?> deleteInboundOrderStatusId(@RequestParam String warehouseId,@PathVariable String inboundOrderStatusId,@RequestParam String companyCodeId,
														@RequestParam String languageId,@RequestParam String plantId,
			 @RequestParam String loginUserID) {
    	inboundorderstatusidService.deleteInboundOrderStatusId(warehouseId, inboundOrderStatusId,companyCodeId,languageId,plantId,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = InboundOrderStatusId.class, value = "Find InboundOrderStatusId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findInboundOrderStatusId(@Valid @RequestBody FindInboundOrderStatusId findInboundOrderStatusId) throws Exception {
		List<InboundOrderStatusId> createdInboundOrderStatusId= inboundorderstatusidService.findInboundOrderStatusId(findInboundOrderStatusId);
		return new ResponseEntity<>(createdInboundOrderStatusId, HttpStatus.OK);
	}
}