package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.itform.ITForm005;
import com.mnrclara.api.crm.service.ITForm005Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ITForm005" }, value = "ITForm005 Operations related to ITForm005Controller") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "ITForm005", description = "Operations related to ITForm005") })
@RequestMapping("/itform005")
@RestController
public class ITForm005Controller {

	@Autowired
	ITForm005Service itform005Service;

	@ApiOperation(response = ITForm005.class, value = "Get all ITForm005 details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ITForm005> itform005List = itform005Service.getITForm005s();
		return new ResponseEntity<>(itform005List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Get a ITForm005") // label for swagger
	@GetMapping("/id")
	public ResponseEntity<?> getITForm005ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID) {
		ITForm005 itform001 = itform005Service.getITForm005(inquiryNo, classID, language, itFormNo, itFormID);
		log.info("ITForm005 : " + itform001);
		return new ResponseEntity<>(itform001, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Create ITForm005") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addITForm005(@Valid @RequestBody ITForm005 newITForm005, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ITForm005 createdITForm005 = itform005Service.createITForm005(newITForm005, loginUserID);
		return new ResponseEntity<>(createdITForm005, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Update ITForm005") // label for swagger
	@PatchMapping("")
	public ResponseEntity<?> updateITForm005(@RequestBody ITForm005 modifiedITForm005, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ITForm005 updatedITForm005 = itform005Service.updateITForm005(modifiedITForm005, loginUserID);
		return new ResponseEntity<>(updatedITForm005, HttpStatus.OK);
	}
}