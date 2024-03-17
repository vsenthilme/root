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

import com.mnrclara.api.setup.model.expensecode.AddExpenseCode;
import com.mnrclara.api.setup.model.expensecode.ExpenseCode;
import com.mnrclara.api.setup.model.expensecode.UpdateExpenseCode;
import com.mnrclara.api.setup.service.ExpenseCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ExpenseCode"}, value = "ExpenseCode Operations related to ExpenseCodeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ExpenseCode",description = "Operations related to ExpenseCode")})
@RequestMapping("/expenseCode")
@RestController
public class ExpenseCodeController {
	
	@Autowired
	ExpenseCodeService expenseCodeService;
	
    @ApiOperation(response = ExpenseCode.class, value = "Get all ExpenseCode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ExpenseCode> expenseCodeList = expenseCodeService.getExpenseCodes();
		return new ResponseEntity<>(expenseCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Get a ExpenseCode") // label for swagger 
	@GetMapping("/{expenseCode}")
	public ResponseEntity<?> getExpenseCode(@PathVariable String expenseCode) {
    	ExpenseCode dbExpenseCode = expenseCodeService.getExpenseCode(expenseCode);
    	log.info("ExpenseCode : " + dbExpenseCode);
		return new ResponseEntity<>(dbExpenseCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Create ExpenseCode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addExpenseCode(@Valid @RequestBody AddExpenseCode newExpenseCode, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpenseCode createdExpenseCode = expenseCodeService.createExpenseCode(newExpenseCode, loginUserID);
		return new ResponseEntity<>(createdExpenseCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Update ExpenseCode") // label for swagger
    @PatchMapping("/{expenseCodeId}")
	public ResponseEntity<?> patchExpenseCode(@PathVariable String expenseCodeId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateExpenseCode updateExpenseCode) 
			throws IllegalAccessException, InvocationTargetException {
		ExpenseCode updatedExpenseCode = expenseCodeService.updateExpenseCode(expenseCodeId, loginUserID, updateExpenseCode);
		return new ResponseEntity<>(updatedExpenseCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Delete ExpenseCode") // label for swagger
	@DeleteMapping("/{expenseCodeId}")
	public ResponseEntity<?> deleteExpenseCode(@PathVariable String expenseCodeId, @RequestParam String loginUserID) {
    	expenseCodeService.deleteExpenseCode(expenseCodeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}