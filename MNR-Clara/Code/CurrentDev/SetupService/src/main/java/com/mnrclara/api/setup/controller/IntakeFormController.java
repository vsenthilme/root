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

import com.mnrclara.api.setup.model.intakeform.AddIntakeFormID;
import com.mnrclara.api.setup.model.intakeform.IntakeFormID;
import com.mnrclara.api.setup.model.intakeform.UpdateIntakeFormID;
import com.mnrclara.api.setup.service.IntakeFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"IntakeForm"}, value = "IntakeForm Operations related to IntakeFormController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "IntakeForm",description = "Operations related to IntakeForm")})
@RequestMapping("/intakeForm")
@RestController
public class IntakeFormController {
	
	@Autowired
	IntakeFormService intakeformService;
	
    @ApiOperation(response = IntakeFormID.class, value = "Get all IntakeForm details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<IntakeFormID> inTakeFormList = intakeformService.getIntakeForms();
		return new ResponseEntity<>(inTakeFormList, HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Get a IntakeForm") // label for swagger 
	@GetMapping("/{intakeFormId}")
	public ResponseEntity<?> getIntakeForm(@PathVariable Long intakeFormId) {
    	IntakeFormID intakeform = intakeformService.getIntakeForm(intakeFormId);
    	log.info("IntakeForm : " + intakeform);
		return new ResponseEntity<>(intakeform, HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Get a IntakeForm") // label for swagger 
	@GetMapping("/{intakeFormId}/lang")
	public ResponseEntity<?> getIntakeFormLanguage(@PathVariable Long intakeFormId) {
    	IntakeFormID intakeform = intakeformService.getIntakeForm(intakeFormId);
    	log.info("IntakeForm : " + intakeform);
		return new ResponseEntity<>(intakeform.getLanguageId(), HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Get a IntakeForm") // label for swagger 
	@GetMapping("/classId")
	public ResponseEntity<?> getIntakeFormByClassIdAndClientTypeId(@RequestParam Long classId, @RequestParam Long clientTypeId) {
    	IntakeFormID intakeform = intakeformService.getIntakeForm(classId, clientTypeId);
    	log.info("IntakeForm : " + intakeform);
		return new ResponseEntity<>(intakeform, HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Create IntakeForm") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postIntakeForm(@Valid @RequestBody AddIntakeFormID newIntakeForm, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID createdIntakeForm = intakeformService.createIntakeForm(newIntakeForm, loginUserID);
		return new ResponseEntity<>(createdIntakeForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Update IntakeForm") // label for swagger
    @PatchMapping("/{intakeFormId}")
	public ResponseEntity<?> patchIntakeForm(@PathVariable Long intakeFormId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateIntakeFormID updateIntakeForm) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID updatedIntakeForm = intakeformService.updateIntakeForm(intakeFormId, loginUserID, updateIntakeForm);
		return new ResponseEntity<>(updatedIntakeForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Delete IntakeForm") // label for swagger
	@DeleteMapping("/{intakeFormId}")
	public ResponseEntity<?> deleteIntakeForm(@PathVariable Long intakeFormId, @RequestParam String loginUserID) {
    	intakeformService.deleteIntakeForm(intakeFormId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}