package com.tekclover.wms.api.transaction.controller;

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

import com.tekclover.wms.api.transaction.model.outbound.quality.AddQualityLine;
import com.tekclover.wms.api.transaction.model.outbound.quality.QualityLine;
import com.tekclover.wms.api.transaction.model.outbound.quality.SearchQualityLine;
import com.tekclover.wms.api.transaction.model.outbound.quality.UpdateQualityLine;
import com.tekclover.wms.api.transaction.service.QualityLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"QualityLine"}, value = "QualityLine  Operations related to QualityLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "QualityLine ",description = "Operations related to QualityLine ")})
@RequestMapping("/qualityline")
@RestController
public class QualityLineController {
	
	@Autowired
	QualityLineService qualitylineService;
	
    @ApiOperation(response = QualityLine.class, value = "Get all QualityLine details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<QualityLine> qualitylineList = qualitylineService.getQualityLines();
		return new ResponseEntity<>(qualitylineList, HttpStatus.OK); 
	}
    
	@ApiOperation(response = QualityLine.class, value = "Search QualityLine") // label for swagger
	@PostMapping("/findQualityLine")
	public List<QualityLine> findQualityLine(@RequestBody SearchQualityLine searchQualityLine)
			throws Exception {
		return qualitylineService.findQualityLine(searchQualityLine);
	}
    
    @ApiOperation(response = QualityLine.class, value = "Create QualityLine") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postQualityLine(@Valid @RequestBody List<AddQualityLine> newQualityLine, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<QualityLine> createdQualityLine = qualitylineService.createQualityLine(newQualityLine, loginUserID);
		return new ResponseEntity<>(createdQualityLine , HttpStatus.OK);
	}
    
    @ApiOperation(response = QualityLine.class, value = "Update QualityLine") // label for swagger
    @PatchMapping("/{partnerCode}")
	public ResponseEntity<?> patchQualityLine(@PathVariable String partnerCode, 
			@RequestParam String warehouseId, @RequestParam String preOutboundNo, 
			@RequestParam String refDocNumber, @RequestParam Long lineNumber, 
			@RequestParam String qualityInspectionNo, @RequestParam String itemCode,
			@Valid @RequestBody UpdateQualityLine updateQualityLine, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		QualityLine createdQualityLine = 
				qualitylineService.updateQualityLine(warehouseId, preOutboundNo, refDocNumber, partnerCode, 
						lineNumber, qualityInspectionNo, itemCode, loginUserID, updateQualityLine);
		return new ResponseEntity<>(createdQualityLine , HttpStatus.OK);
	}
    
    @ApiOperation(response = QualityLine.class, value = "Delete QualityLine") // label for swagger
	@DeleteMapping("/{partnerCode}")
	public ResponseEntity<?> deleteQualityLine(@PathVariable String partnerCode, 
			@RequestParam String warehouseId, @RequestParam String preOutboundNo, 
			@RequestParam String refDocNumber, @RequestParam Long lineNumber, 
			@RequestParam String qualityInspectionNo, @RequestParam String itemCode, @RequestParam String loginUserID) {
    	qualitylineService.deleteQualityLine(warehouseId, preOutboundNo, refDocNumber, partnerCode, 
				lineNumber, qualityInspectionNo, itemCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}