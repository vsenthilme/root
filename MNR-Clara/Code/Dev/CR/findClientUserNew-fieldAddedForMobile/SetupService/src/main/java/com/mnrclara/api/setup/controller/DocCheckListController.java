package com.mnrclara.api.setup.controller;

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

import com.mnrclara.api.setup.model.docchecklist.AddDocCheckList;
import com.mnrclara.api.setup.model.docchecklist.DocCheckList;
import com.mnrclara.api.setup.model.docchecklist.SearchDocCheckList;
import com.mnrclara.api.setup.model.docchecklist.UpdateDocCheckList;
import com.mnrclara.api.setup.service.DocCheckListService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"DocCheckList"}, value = "DocCheckList  Operations related to DocCheckListController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "DocCheckList ",description = "Operations related to DocCheckList ")})
@RequestMapping("/docchecklist")
@RestController
public class DocCheckListController {
	
	@Autowired
	DocCheckListService docchecklistService;
	
    @ApiOperation(response = DocCheckList.class, value = "Get all DocCheckList details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<DocCheckList> docchecklistList = docchecklistService.getDocCheckLists();
		return new ResponseEntity<>(docchecklistList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Get a DocCheckList") // label for swagger 
	@GetMapping("/{checkListNo}")
	public ResponseEntity<?> getDocCheckList(@PathVariable Long checkListNo,
			@RequestParam String languageId, @RequestParam Long classId, @RequestParam Long caseCategoryId, 
			@RequestParam Long caseSubCategoryId, @RequestParam Long sequenceNo) {
    	DocCheckList docchecklist = 
    			docchecklistService.getDocCheckList(languageId, classId, checkListNo, caseCategoryId, caseSubCategoryId, sequenceNo);
    	log.info("DocCheckList : " + docchecklist);
		return new ResponseEntity<>(docchecklist, HttpStatus.OK);
	}
    
	@ApiOperation(response = DocCheckList.class, value = "Search DocCheckList") // label for swagger
	@PostMapping("/findDocCheckList")
	public List<DocCheckList> findDocCheckList(@RequestBody SearchDocCheckList searchDocCheckList)
			throws Exception {
		return docchecklistService.findDocCheckLists(searchDocCheckList);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Create DocCheckList") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postDocCheckList(@Valid @RequestBody List<AddDocCheckList> newDocCheckList, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		List<DocCheckList> createdDocCheckList = docchecklistService.createDocCheckList(newDocCheckList, loginUserID);
		return new ResponseEntity<>(createdDocCheckList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Update DocCheckList") // label for swagger
    @PatchMapping("")
	public ResponseEntity<?> patchDocCheckList(@RequestBody List<UpdateDocCheckList> updateDocCheckList, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<DocCheckList> createdDocCheckList = docchecklistService.updateDocCheckList(updateDocCheckList, loginUserID);
		return new ResponseEntity<>(createdDocCheckList , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Delete DocCheckList") // label for swagger
	@DeleteMapping("/{checkListNo}")
	public ResponseEntity<?> deleteDocCheckList (@PathVariable Long checkListNo, 
			@RequestParam(required = false) Long sequenceNo)
					throws IllegalAccessException, InvocationTargetException {
    	docchecklistService.deleteDocCheckList (checkListNo, sequenceNo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}