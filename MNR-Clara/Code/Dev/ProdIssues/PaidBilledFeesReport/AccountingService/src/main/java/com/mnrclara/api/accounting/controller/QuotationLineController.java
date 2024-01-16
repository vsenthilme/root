package com.mnrclara.api.accounting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.accounting.model.quotation.QuotationLineEntity;
import com.mnrclara.api.accounting.service.QuotationLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"QuotationLine"}, value = "QuotationLine  Operations related to QuotationLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "QuotationLine ",description = "Operations related to QuotationLine ")})
@RequestMapping("/quotationline")
@RestController
public class QuotationLineController {
	
	@Autowired
	QuotationLineService quotationlineService;
	
    @ApiOperation(response = QuotationLineEntity.class, value = "Get a QuotationLine") // label for swagger 
	@GetMapping("/{quotationNo}")
	public ResponseEntity<?> getQuotationLines(@PathVariable String quotationNo, @RequestParam Long quotationRevisionNo) {
    	List<QuotationLineEntity> quotationlines = quotationlineService.getQuotationLine(quotationNo, quotationRevisionNo);
    	log.info("QuotationLine : " + quotationlines);
		return new ResponseEntity<>(quotationlines, HttpStatus.OK);
	}
    
    @ApiOperation(response = QuotationLineEntity.class, value = "Get a QuotationLine") // label for swagger 
	@GetMapping("/{quotationNo}/{quotationRevisionNo}/{serialNo}")
	public ResponseEntity<?> getQuotationLine(@PathVariable String quotationNo, @PathVariable Long quotationRevisionNo, @PathVariable Long serialNo) {
    	QuotationLineEntity quotationline = quotationlineService.getQuotationLine(quotationNo, quotationRevisionNo, serialNo);
    	log.info("QuotationLine : " + quotationline);
		return new ResponseEntity<>(quotationline, HttpStatus.OK);
	}
}