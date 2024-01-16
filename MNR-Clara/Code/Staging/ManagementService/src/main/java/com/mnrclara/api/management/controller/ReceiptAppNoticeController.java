package com.mnrclara.api.management.controller;

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

import com.mnrclara.api.management.model.receiptappnotice.AddReceiptAppNotice;
import com.mnrclara.api.management.model.receiptappnotice.ReceiptAppNotice;
import com.mnrclara.api.management.model.receiptappnotice.UpdateReceiptAppNotice;
import com.mnrclara.api.management.service.ReceiptAppNoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"ReceiptAppNotice"}, value = "ReceiptAppNotice  Operations related to ReceiptAppNoticeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ReceiptAppNotice ",description = "Operations related to ReceiptAppNotice ")})
@RequestMapping("/receiptappnotice")
@RestController
public class ReceiptAppNoticeController {
	
	@Autowired
	ReceiptAppNoticeService receiptappnoticeService;
	
    @ApiOperation(response = ReceiptAppNotice.class, value = "Get all ReceiptAppNotice details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ReceiptAppNotice> receiptappnoticeList = receiptappnoticeService.getReceiptAppNotices();
		return new ResponseEntity<>(receiptappnoticeList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ReceiptAppNotice.class, value = "Get a ReceiptAppNotice") // label for swagger 
	@GetMapping("/{receiptNo}")
	public ResponseEntity<?> getReceiptAppNotice(@PathVariable String receiptNo, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber) {
    	ReceiptAppNotice receiptappnotice = 
    			receiptappnoticeService.getReceiptAppNotice(languageId, classId, matterNumber, receiptNo);
    	log.info("ReceiptAppNotice : " + receiptappnotice);
		return new ResponseEntity<>(receiptappnotice, HttpStatus.OK);
	}
    
    @ApiOperation(response = ReceiptAppNotice.class, value = "Create ReceiptAppNotice") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postReceiptAppNotice(@Valid @RequestBody AddReceiptAppNotice newReceiptAppNotice, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ReceiptAppNotice createdReceiptAppNotice = receiptappnoticeService.createReceiptAppNotice(newReceiptAppNotice, loginUserID);
		return new ResponseEntity<>(createdReceiptAppNotice , HttpStatus.OK);
	}
    
    @ApiOperation(response = ReceiptAppNotice.class, value = "Update ReceiptAppNotice") // label for swagger
    @PatchMapping("/{receiptNo}")
	public ResponseEntity<?> patchReceiptAppNotice(@PathVariable String receiptNo, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber,
			@Valid @RequestBody UpdateReceiptAppNotice updateReceiptAppNotice, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ReceiptAppNotice createdReceiptAppNotice = 
				receiptappnoticeService.updateReceiptAppNotice(languageId, classId, matterNumber, receiptNo, updateReceiptAppNotice, loginUserID);
		return new ResponseEntity<>(createdReceiptAppNotice , HttpStatus.OK);
	}
    
    @ApiOperation(response = ReceiptAppNotice.class, value = "Delete ReceiptAppNotice") // label for swagger
	@DeleteMapping("/{receiptNo}")
	public ResponseEntity<?> deleteReceiptAppNotice(@PathVariable String receiptNo, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber, @RequestParam String loginUserID) {
    	receiptappnoticeService.deleteReceiptAppNotice(languageId, classId, matterNumber, receiptNo, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}