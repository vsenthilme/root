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

import com.mnrclara.api.setup.model.chartofaccounts.AddChartOfAccounts;
import com.mnrclara.api.setup.model.chartofaccounts.ChartOfAccounts;
import com.mnrclara.api.setup.model.chartofaccounts.UpdateChartOfAccounts;
import com.mnrclara.api.setup.service.ChartOfAccountsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ChartOfAccounts"}, value = "ChartOfAccounts Operations related to ChartOfAccountsController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ChartOfAccounts",description = "Operations related to ChartOfAccounts")})
@RequestMapping("/chartOfAccounts")
@RestController
public class ChartOfAccountsController {
	
	@Autowired
	ChartOfAccountsService chartofaccountsService;
	
    @ApiOperation(response = ChartOfAccounts.class, value = "Get all ChartOfAccounts details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ChartOfAccounts> chartofaccountsList = chartofaccountsService.getChartOfAccountsList();
		return new ResponseEntity<>(chartofaccountsList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ChartOfAccounts.class, value = "Get a ChartOfAccounts") // label for swagger 
	@GetMapping("/{accountNumber}")
	public ResponseEntity<?> getChartOfAccounts(@PathVariable String accountNumber) {
    	ChartOfAccounts chartOfAccounts = chartofaccountsService.getChartOfAccounts(accountNumber);
    	log.info("ChartOfAccounts : " + chartOfAccounts);
		return new ResponseEntity<>(chartOfAccounts, HttpStatus.OK);
	}
    
    @ApiOperation(response = ChartOfAccounts.class, value = "Create ChartOfAccounts") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addChartOfAccounts(@Valid @RequestBody AddChartOfAccounts newChartOfAccounts, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ChartOfAccounts createdChartOfAccounts = chartofaccountsService.createChartOfAccounts(newChartOfAccounts, loginUserID);
		return new ResponseEntity<>(createdChartOfAccounts , HttpStatus.OK);
	}
    
    @ApiOperation(response = ChartOfAccounts.class, value = "Update ChartOfAccounts") // label for swagger
    @PatchMapping("/{accountNumber}")
	public ResponseEntity<?> patchChartOfAccounts(@PathVariable String accountNumber, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateChartOfAccounts updateChartOfAccounts) 
			throws IllegalAccessException, InvocationTargetException {
		ChartOfAccounts updatedChartOfAccounts = 
				chartofaccountsService.updateChartOfAccounts(accountNumber, loginUserID, updateChartOfAccounts);
		return new ResponseEntity<>(updatedChartOfAccounts , HttpStatus.OK);
	}
    
    @ApiOperation(response = ChartOfAccounts.class, value = "Delete ChartOfAccounts") // label for swagger
	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<?> deleteChartOfAccounts(@PathVariable String accountNumber, @RequestParam String loginUserID) {
    	chartofaccountsService.deleteChartOfAccounts(accountNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}