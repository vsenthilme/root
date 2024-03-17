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

import com.mnrclara.api.setup.model.casesubcategory.AddCaseSubcategory;
import com.mnrclara.api.setup.model.casesubcategory.CaseSubcategory;
import com.mnrclara.api.setup.model.casesubcategory.UpdateCaseSubcategory;
import com.mnrclara.api.setup.service.CaseSubcategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"CaseSubcategory"}, value = "CaseSubcategory Operations related to CaseSubcategoryController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CaseSubcategory",description = "Operations related to CaseSubcategory")})
@RequestMapping("/caseSubcategory")
@RestController
public class CaseSubcategoryController {
	
	@Autowired
	CaseSubcategoryService caseSubcategoryService;
	
    @ApiOperation(response = CaseSubcategory.class, value = "Get all CaseSubcategory details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<CaseSubcategory> caseSubcategoryList = caseSubcategoryService.getCaseSubcategories();
		return new ResponseEntity<>(caseSubcategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Get a CaseSubcategory") // label for swagger 
	@GetMapping("/{caseSubcategoryId}")
	public ResponseEntity<?> getCaseSubcategory(@PathVariable Long caseSubcategoryId,
			@RequestParam String languageId, @RequestParam Long classId, @RequestParam Long caseCategoryId) {
    	CaseSubcategory caseSubcategory = 
    			caseSubcategoryService.getCaseSubcategory(caseSubcategoryId);
    	log.info("CaseSubcategory : " + caseSubcategory);
		return new ResponseEntity<>(caseSubcategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Create CaseSubcategory") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addCaseSubcategory(@Valid @RequestBody AddCaseSubcategory newCaseSubcategory, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory createdCaseSubcategory = caseSubcategoryService.createCaseSubcategory(newCaseSubcategory, loginUserID);
		return new ResponseEntity<>(createdCaseSubcategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Update CaseSubcategory") // label for swagger
    @PatchMapping("/{caseSubcategoryId}")
	public ResponseEntity<?> patchCaseSubcategory(@PathVariable Long caseSubcategoryId,
			@RequestParam String languageId, @RequestParam Long classId, @RequestParam Long caseCategoryId, 
			@RequestParam String loginUserID, @Valid @RequestBody UpdateCaseSubcategory updateCaseSubcategory) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory updatedCaseSubcategory = 
				caseSubcategoryService.updateCaseSubcategory(languageId, classId, caseCategoryId, caseSubcategoryId, loginUserID, updateCaseSubcategory);
		return new ResponseEntity<>(updatedCaseSubcategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Delete CaseSubcategory") // label for swagger
	@DeleteMapping("/{caseSubcategoryId}")
	public ResponseEntity<?> deleteCaseSubcategory(@PathVariable Long caseSubcategoryId, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam Long caseCategoryId, @RequestParam String loginUserID) {
    	caseSubcategoryService.deleteCaseSubcategory(languageId, classId, caseCategoryId, caseSubcategoryId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}