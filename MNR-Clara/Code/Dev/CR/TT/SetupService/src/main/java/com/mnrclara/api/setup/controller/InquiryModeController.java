package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.inquirymode.AddInquiryMode;
import com.mnrclara.api.setup.model.inquirymode.InquiryMode;
import com.mnrclara.api.setup.model.inquirymode.UpdateInquiryMode;
import com.mnrclara.api.setup.service.InquiryModeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"InquiryMode"}, value = "InquiryMode Operations related to InquiryModeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "InquiryMode",description = "Operations related to InquiryMode")})
@RequestMapping("/inquiryMode")
@RestController
public class InquiryModeController {
	
	@Autowired
	InquiryModeService inquiryModeService;
	
    @ApiOperation(response = InquiryMode.class, value = "Get all InquiryMode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<InquiryMode> inquiryModeList = inquiryModeService.getInquiryModes();
		return new ResponseEntity<>(inquiryModeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Get a InquiryMode") // label for swagger 
	@GetMapping("/{inquiryModeId}")
	public ResponseEntity<?> getInquiryMode(@PathVariable Long inquiryModeId) {
    	InquiryMode inquiryMode = inquiryModeService.getInquiryMode(inquiryModeId);
    	log.info("InquiryMode : " + inquiryMode);
		return new ResponseEntity<>(inquiryMode, HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Create InquiryMode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addInquiryMode(@Valid @RequestBody AddInquiryMode newInquiryMode, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		InquiryMode createdInquiryMode = inquiryModeService.createInquiryMode(newInquiryMode, loginUserID);
		return new ResponseEntity<>(createdInquiryMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Update InquiryMode") // label for swagger
    @PatchMapping("/{inquiryModeId}")
	public ResponseEntity<?> patchInquiryMode(@PathVariable Long inquiryModeId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateInquiryMode updateInquiryMode) 
			throws IllegalAccessException, InvocationTargetException {
		InquiryMode updatedInquiryMode = inquiryModeService.updateInquiryMode(inquiryModeId, loginUserID, updateInquiryMode);
		return new ResponseEntity<>(updatedInquiryMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Delete InquiryMode") // label for swagger
	@DeleteMapping("/{inquiryModeId}")
	public ResponseEntity<?> deleteInquiryMode(@PathVariable Long inquiryModeId, @RequestParam String loginUserID) {
    	inquiryModeService.deleteInquiryMode(inquiryModeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}