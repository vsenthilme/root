package com.mnrclara.api.accounting.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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

import com.mnrclara.api.accounting.model.quotation.QuotationHeader;
import com.mnrclara.api.accounting.model.quotation.QuotationHeaderEntity;
import com.mnrclara.api.accounting.model.quotation.SearchQuotationHeader;
import com.mnrclara.api.accounting.service.QuotationHeaderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"QuotationHeader"}, value = "QuotationHeader  Operations related to QuotationHeaderController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "QuotationHeader ",description = "Operations related to QuotationHeader ")})
@RequestMapping("/quotationheader")
@RestController
public class QuotationHeaderController {
	
	@Autowired
	QuotationHeaderService quotationHeaderService;
	
    @ApiOperation(response = QuotationHeader.class, value = "Get all QuotationHeader details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<QuotationHeader> quotationheaderList = quotationHeaderService.getQuotationHeaders();
		return new ResponseEntity<>(quotationheaderList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = QuotationHeader.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "quotationNo") String sortBy) {
		Page<QuotationHeader> list = quotationHeaderService.getAllQuotationHeaders(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
    
    @ApiOperation(response = QuotationHeader.class, value = "Get a QuotationHeader") // label for swagger 
	@GetMapping("/{quotationNo}")
	public ResponseEntity<?> getQuotationHeader(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo) {
    	QuotationHeader quotationheader = quotationHeaderService.getQuotationHeader(quotationNo, quotationRevisionNo);
		return new ResponseEntity<>(quotationheader, HttpStatus.OK);
	}
    
	@ApiOperation(response = QuotationHeader.class, value = "Search QuotationHeader") // label for swagger
	@PostMapping("/findQuotationHeader")
	public ResponseEntity<?> findQuotationHeader(@RequestBody SearchQuotationHeader searchQuotationHeader)
			throws Exception {
		List<QuotationHeader> quotationHeaderResults = quotationHeaderService.findQuotationHeader(searchQuotationHeader);
		return new ResponseEntity<>(quotationHeaderResults, HttpStatus.OK);
	}
	
    @ApiOperation(response = QuotationHeader.class, value = "Create QuotationHeader") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postQuotationHeader(@RequestBody QuotationHeader newQuotationHeader, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		QuotationHeader createdQuotationHeader = quotationHeaderService.createQuotationHeader(newQuotationHeader, loginUserID);
		return new ResponseEntity<>(createdQuotationHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = QuotationHeader.class, value = "Update QuotationHeader") // label for swagger
    @PatchMapping("/{quotationNo}")
	public ResponseEntity<?> patchQuotationHeader(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo,
			@RequestBody QuotationHeader updateQuotationHeader, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		QuotationHeader createdQuotationHeader = quotationHeaderService.updateQuotationHeader(quotationNo, quotationRevisionNo, updateQuotationHeader, loginUserID);
		return new ResponseEntity<>(createdQuotationHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = QuotationHeader.class, value = "Delete QuotationHeader") // label for swagger
	@DeleteMapping("/{quotationNo}")
	public ResponseEntity<?> deleteQuotationHeader(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo, @RequestParam String loginUserID) {
    	quotationHeaderService.deleteQuotationHeader(quotationNo, quotationRevisionNo, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}