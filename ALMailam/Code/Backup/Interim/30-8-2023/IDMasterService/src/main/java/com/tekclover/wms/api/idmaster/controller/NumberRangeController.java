package com.tekclover.wms.api.idmaster.controller;

import com.tekclover.wms.api.idmaster.model.numberrange.AddNumberRange;
import com.tekclover.wms.api.idmaster.model.numberrange.FindNumberRange;
import com.tekclover.wms.api.idmaster.model.numberrange.UpdateNumberRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tekclover.wms.api.idmaster.model.numberrange.NumberRange;
import com.tekclover.wms.api.idmaster.service.NumberRangeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"NumberRange"}, value = "NumberRange Operations related to NumberRangeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NumberRange",description = "Operations related to NumberRange")})
@RequestMapping("/numberrange")
@RestController
public class NumberRangeController {
	
	@Autowired
	NumberRangeService numberRangeService;
	
    @ApiOperation(response = NumberRange.class, value = "Get Number Range Current") // label for swagger
	@GetMapping("/nextNumberRange/{numberRangeCode}")
	public ResponseEntity<?> getNextNumberRange(@PathVariable Long numberRangeCode, 
			@RequestParam Long fiscalYear, @RequestParam String warehouseId) {
		String nextRangeValue = numberRangeService.getNextNumberRange(numberRangeCode, fiscalYear, warehouseId);
		return new ResponseEntity<>(nextRangeValue , HttpStatus.OK);
	}

	@ApiOperation(response = NumberRange.class, value = "Get all NumberRange details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<NumberRange> numberRangeList = numberRangeService.getNumberRanges();
		return new ResponseEntity<>(numberRangeList, HttpStatus.OK);
	}

	@ApiOperation(response = NumberRange.class, value = "Get a NumberRange") // label for swagger
	@GetMapping("/{numberRangeCode}")
	public ResponseEntity<?> getNumberRange(@RequestParam String warehouseId,@PathVariable Long numberRangeCode,@RequestParam Long fiscalYear,
											  @RequestParam String companyCodeId,@RequestParam String languageId, @RequestParam String plantId) {
		NumberRange numberRange =
				numberRangeService.getNumberRange(warehouseId, companyCodeId,languageId,plantId,numberRangeCode,fiscalYear);
		log.info("NumberRange : " + numberRangeCode);
		return new ResponseEntity<>(numberRange, HttpStatus.OK);
	}

//	@ApiOperation(response = BarcodeTypeId.class, value = "Search BarcodeTypeId") // label for swagger
//	@PostMapping("/findBarcodeTypeId")
//	public List<BarcodeTypeId> findBarcodeTypeId(@RequestBody SearchBarcodeTypeId searchBarcodeTypeId)
//			throws Exception {
//		return barcodetypeidService.findBarcodeTypeId(searchBarcodeTypeId);
//	}

	@ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postNumberRange(@Valid @RequestBody AddNumberRange addNumberRange,
											   @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		NumberRange createdNumberRange = numberRangeService.createNumberRange(addNumberRange, loginUserID);
		return new ResponseEntity<>(createdNumberRange , HttpStatus.OK);
	}

	@ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
	@PatchMapping("/{numberRangeCode}")
	public ResponseEntity<?> patchNumberRange(@RequestParam String warehouseId, @PathVariable Long numberRangeCode, @RequestParam Long fiscalYear,
											  @RequestParam String companyCodeId, @RequestParam String languageId, @RequestParam String plantId,
											  @Valid @RequestBody UpdateNumberRange updateNumberRange, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		NumberRange createdNumberRange =
				numberRangeService.updateNumberRange(warehouseId,companyCodeId,languageId,plantId,numberRangeCode,fiscalYear,loginUserID,updateNumberRange);
		return new ResponseEntity<>(createdNumberRange , HttpStatus.OK);
	}

	@ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
	@DeleteMapping("/{numberRangeCode}")
	public ResponseEntity<?> deleteNumberRange(@RequestParam String warehouseId,@PathVariable Long numberRangeCode,@RequestParam Long fiscalYear,@RequestParam String companyCodeId,
												 @RequestParam String languageId,@RequestParam String plantId,
												 @RequestParam String loginUserID) {
		numberRangeService.deleteNumberRange(warehouseId,companyCodeId,languageId,plantId,numberRangeCode,fiscalYear,loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = NumberRange.class, value = "Find NumberRange") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findNumberRange(@Valid @RequestBody FindNumberRange findNumberRange) throws Exception {
		List<NumberRange> createdNumberRange = numberRangeService.findNumberRange(findNumberRange);
		return new ResponseEntity<>(createdNumberRange, HttpStatus.OK);
	}
}