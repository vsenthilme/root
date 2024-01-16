package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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

import com.mnrclara.api.management.model.matteritform.AddMatterITForm;
import com.mnrclara.api.management.model.matteritform.MatterITForm;
import com.mnrclara.api.management.model.matteritform.SearchMatterITForm;
import com.mnrclara.api.management.model.matteritform.UpdateMatterITForm;
import com.mnrclara.api.management.service.MatterITFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterITForm"}, value = "MatterITForm Operations related to MatterITFormController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterITForm",description = "Operations related to MatterITForm")})
@RequestMapping("/matteritform")
@RestController
public class MatterITFormController {
	
	@Autowired
	MatterITFormService matterITFormService;
	
    @ApiOperation(response = MatterITForm.class, value = "Get all MatterITForm details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterITForm> matterITFormList = matterITFormService.getMatterITForms();
		return new ResponseEntity<>(matterITFormList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Get a MatterITForm") // label for swagger 
	@GetMapping("/{intakeFormNumber}")
	public ResponseEntity<?> getMatterITForm(@PathVariable String intakeFormNumber) {
    	MatterITForm matterITForm = matterITFormService.getMatterITForm(intakeFormNumber);
    	log.info("MatterITForm : " + matterITForm);
		return new ResponseEntity<>(matterITForm, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Search MatterDocument") // label for swagger
	@PostMapping("/findMatterITForm")
	public List<MatterITForm> findMatterITForm(@RequestBody SearchMatterITForm searchMatterITForm)
			throws ParseException {
		return matterITFormService.findMatterITForm(searchMatterITForm);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Create MatterITForm") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterITForm(@Valid @RequestBody AddMatterITForm newMatterITForm, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm createdMatterITForm = matterITFormService.createMatterITForm(newMatterITForm, loginUserID);
		return new ResponseEntity<>(createdMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Update MatterITForm") // label for swagger
    @PatchMapping("/{intakeFormNumber}")
	public ResponseEntity<?> patchMatterITForm(@PathVariable String intakeFormNumber, 
			@Valid @RequestBody UpdateMatterITForm updateMatterITForm, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm updatedMatterITForm = matterITFormService.updateMatterITForm(intakeFormNumber, updateMatterITForm, loginUserID);
		return new ResponseEntity<>(updatedMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Update MatterITForm") // label for swagger
    @PatchMapping("/{intakeFormNumber}/approve")
	public ResponseEntity<?> patchMatterITFormApprove(@PathVariable String intakeFormNumber, 
			@Valid @RequestBody UpdateMatterITForm updateMatterITForm, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm updatedMatterITForm = matterITFormService.approveMatterITForm(intakeFormNumber, updateMatterITForm, loginUserID);
		return new ResponseEntity<>(updatedMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Delete MatterITForm") // label for swagger
	@DeleteMapping("/{intakeFormNumber}")
	public ResponseEntity<?> deleteMatterITForm(@PathVariable String intakeFormNumber, @RequestParam String loginUserID) {
    	matterITFormService.deleteMatterITForm(intakeFormNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}