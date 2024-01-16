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

import com.mnrclara.api.setup.model.casecategory.AddCaseCategory;
import com.mnrclara.api.setup.model.casecategory.CaseCategory;
import com.mnrclara.api.setup.model.casecategory.UpdateCaseCategory;
import com.mnrclara.api.setup.service.CaseCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"CaseCategory"}, value = "CaseCategory Operations related to CaseCategoryController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CaseCategory",description = "Operations related to CaseCategory")})
@RequestMapping("/caseCategory")
@RestController
public class CaseCategoryController {
	
	@Autowired
	CaseCategoryService caseCategoryService;
	
    @ApiOperation(response = CaseCategory.class, value = "Get all CaseCategory details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<CaseCategory> caseCategoryList = caseCategoryService.getCaseCategories();
		return new ResponseEntity<>(caseCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Get a CaseCategory") // label for swagger 
	@GetMapping("/{caseCategoryId}")
	public ResponseEntity<?> getCaseCategory(@PathVariable Long caseCategoryId) {
    	CaseCategory caseCategory = caseCategoryService.getCaseCategory(caseCategoryId);
    	log.info("CaseCategory : " + caseCategory);
		return new ResponseEntity<>(caseCategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Get a CaseCategory") // label for swagger 
	@GetMapping("/{classId}/classId")
	public ResponseEntity<?> getCaseCategoryClassId(@PathVariable Long classId) {
    	List<CaseCategory> caseCategoryList = caseCategoryService.getCaseCategoryByClassId(classId);
    	log.info("CaseCategory : " + caseCategoryList);
		return new ResponseEntity<>(caseCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Create CaseCategory") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addCaseCategory(@Valid @RequestBody AddCaseCategory newCaseCategory, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory createdCaseCategory = caseCategoryService.createCaseCategory(newCaseCategory, loginUserID);
		return new ResponseEntity<>(createdCaseCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Update CaseCategory") // label for swagger
    @PatchMapping("/{caseCategoryId}")
	public ResponseEntity<?> patchCaseCategory(@PathVariable Long caseCategoryId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateCaseCategory updateCaseCategory) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory updatedCaseCategory = caseCategoryService.updateCaseCategory(caseCategoryId, loginUserID, updateCaseCategory);
		return new ResponseEntity<>(updatedCaseCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Delete CaseCategory") // label for swagger
	@DeleteMapping("/{caseCategoryId}")
	public ResponseEntity<?> deleteCaseCategory(@PathVariable Long caseCategoryId, @RequestParam String loginUserID	) {
    	caseCategoryService.deleteCaseCategory(caseCategoryId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}