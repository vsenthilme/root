package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.dto.EMail;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.UpdateInquiry;
import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.service.InquiryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Intake"}, value = "IntakeController Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Intake",description = "Operations related to Intake")})
@RequestMapping("/inquiry/intake")
public class IntakeController {
	
	@Autowired
	InquiryService inquiryService;
	
	@ApiOperation(response = Inquiry.class, value = "Get a Inquiry") // label for swagger 
	@GetMapping("/{inquiryNumber}")
	public ResponseEntity<?> getInquiry(@PathVariable String inquiryNumber) {
    	Inquiry inquiry = inquiryService.getInquiry(inquiryNumber);
    	log.info("Inquiry : " + inquiry);
    	if (inquiry != null) {
    		return new ResponseEntity<>(inquiry, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given InquiryNumber : " + inquiryNumber + " doesn't exists", HttpStatus.OK);
    	}
	}
	
	@ApiOperation(response = Inquiry.class, value = "Send IntakeForm") // label for swagger
    @PatchMapping("/updateStatus")
   	public ResponseEntity<?> updateStatus(@RequestParam String inquiryNumber, 
			@RequestParam String loginUserID, @Valid @RequestBody UpdateInquiry updateInquiry)
   			throws IllegalAccessException, InvocationTargetException {
   		PCIntakeForm createdPCIntakeForm = inquiryService.afterIntakeFormSent(inquiryNumber, loginUserID, updateInquiry);
   		return new ResponseEntity<>(createdPCIntakeForm, HttpStatus.OK);
   	}
	
	@ApiOperation(response = Inquiry.class, value = "Send IntakeForm") // label for swagger
    @PostMapping("/sendFormThroEmail")
   	public ResponseEntity<?> sendFormThroEmail(@Valid @RequestBody EMail email)
   			throws IllegalAccessException, InvocationTargetException {
   		inquiryService.sendFormThroEmail(email);
   		return new ResponseEntity<>(HttpStatus.OK);
   	}
}