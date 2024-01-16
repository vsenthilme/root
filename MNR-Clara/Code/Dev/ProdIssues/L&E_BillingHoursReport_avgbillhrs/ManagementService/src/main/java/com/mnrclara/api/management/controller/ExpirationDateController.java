package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.management.model.clientnote.ClientNote;
import com.mnrclara.api.management.model.clientnote.SearchClientNote;
import com.mnrclara.api.management.model.expirationdate.SearchExpirationDate;
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

import com.mnrclara.api.management.model.expirationdate.AddExpirationDate;
import com.mnrclara.api.management.model.expirationdate.ExpirationDate;
import com.mnrclara.api.management.model.expirationdate.UpdateExpirationDate;
import com.mnrclara.api.management.service.ExpirationDateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"ExpirationDate"}, value = "ExpirationDate  Operations related to ExpirationDateController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ExpirationDate ",description = "Operations related to ExpirationDate ")})
@RequestMapping("/expirationdate")
@RestController
public class ExpirationDateController {
	
	@Autowired
	ExpirationDateService expirationdateService;
	
    @ApiOperation(response = ExpirationDate.class, value = "Get all ExpirationDate details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ExpirationDate> expirationdateList = expirationdateService.getExpirationDates();
		return new ResponseEntity<>(expirationdateList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Get a ExpirationDate") // label for swagger 
	@GetMapping("/{matterNo}")
	public ResponseEntity<?> getExpirationDate(@PathVariable String matterNo, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String clientId, String documentType) {
    	ExpirationDate expirationdate = expirationdateService.getExpirationDate(matterNo, languageId, classId, clientId, documentType);
    	log.info("ExpirationDate : " + expirationdate);
		return new ResponseEntity<>(expirationdate, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Create ExpirationDate") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postExpirationDate(@Valid @RequestBody AddExpirationDate newExpirationDate, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDate createdExpirationDate = expirationdateService.createExpirationDate(newExpirationDate, loginUserID);
		return new ResponseEntity<>(createdExpirationDate , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Send SMS") // label for swagger
	@GetMapping("/reminderSMS")
	public ResponseEntity<?> sendReminderSMS() throws IllegalAccessException, InvocationTargetException {
		Boolean smsResponse = expirationdateService.sendReminderSMS();
		return new ResponseEntity<>(smsResponse , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Update ExpirationDate") // label for swagger
    @PatchMapping("/{matterNo}")
	public ResponseEntity<?> patchExpirationDate(@PathVariable String matterNo, 
			@Valid @RequestBody UpdateExpirationDate updateExpirationDate, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDate createdExpirationDate = expirationdateService.updateExpirationDate(matterNo, updateExpirationDate, loginUserID);
		return new ResponseEntity<>(createdExpirationDate , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Delete ExpirationDate") // label for swagger
	@DeleteMapping("/{matterNo}")
	public ResponseEntity<?> deleteExpirationDate(@PathVariable String matterNo, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String clientId, String documentType, @RequestParam String loginUserID) {
    	expirationdateService.deleteExpirationDate(matterNo, languageId, classId, clientId, documentType, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(response = ExpirationDate.class, value = "Search Expiration") // label for swagger
	@PostMapping("/findExpirationDate")
	public List<ExpirationDate> findExpirationDate(@RequestBody SearchExpirationDate searchExpirationDate) throws ParseException {
		return expirationdateService.findAllExpirationDate(searchExpirationDate);
	}
}