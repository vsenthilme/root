package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.management.model.matterexpense.*;
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

import com.mnrclara.api.management.model.matternote.MatterNote;
import com.mnrclara.api.management.service.MatterExpenseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterExpense"}, value = "MatterExpense Operations related to MatterExpenseController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterExpense",description = "Operations related to MatterExpense")})
@RequestMapping("/matterexpense")
@RestController
public class MatterExpenseController {
	
	@Autowired
	MatterExpenseService matterExpenseService;
	
    @ApiOperation(response = MatterExpense.class, value = "Get all MatterExpense details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterExpense> matterExpenseList = matterExpenseService.getMatterExpenses();
		return new ResponseEntity<>(matterExpenseList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Get a MatterExpense") // label for swagger 
	@GetMapping("/{matterExpenseId}")
	public ResponseEntity<?> getMatterExpense(@PathVariable Long matterExpenseId) {
    	MatterExpense matterexpense = matterExpenseService.getMatterExpense(matterExpenseId);
    	log.info("MatterExpense : " + matterexpense);
		return new ResponseEntity<>(matterexpense, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Get a MatterExpense") // label for swagger 
   	@GetMapping("/{preBillNumber}/preBillApprove")
   	public ResponseEntity<?> getMatterExpenseForApprove(@PathVariable String preBillNumber) {
       	List<MatterExpense> matterexpense = matterExpenseService.getMatterExpenseForApprove(preBillNumber);
       	log.info("MatterExpense : " + matterexpense);
   		return new ResponseEntity<>(matterexpense, HttpStatus.OK);
   	}
    
    @ApiOperation(response = MatterExpense.class, value = "Search MatterExpense") // label for swagger
	@PostMapping("/findMatterExpenses")
	public List<MatterExpense> findMatterExpenses(@RequestBody SearchMatterExpense searchMatterExpense) throws ParseException {
		return matterExpenseService.findMatterExpenses(searchMatterExpense);
	}

	@ApiOperation(response = MatterExpense.class, value = "Search MatterExpense") // label for swagger
	@PostMapping("/findMatterExpenses/new")
	public List<IMatterExpense> findMatterExpensesNew(@RequestBody SearchMatterExpense searchMatterExpense) throws ParseException {
		return matterExpenseService.findMatterExpensesNew(searchMatterExpense);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Create MatterExpense") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterExpense(@Valid @RequestBody AddMatterExpense newMatterExpense, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense createdMatterExpense = matterExpenseService.createMatterExpense(newMatterExpense, loginUserID);
		return new ResponseEntity<>(createdMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Create MatterExpense") // label for swagger
   	@PostMapping("/batch")
   	public ResponseEntity<?> postBulkMatterExpense(@Valid @RequestBody AddMatterExpense[] newMatterExpenses, 
   			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
   		matterExpenseService.createBulkMatterExpenses(newMatterExpenses, loginUserID);
   		return new ResponseEntity<>(HttpStatus.OK);
   	}
    
    @ApiOperation(response = MatterExpense.class, value = "Update MatterExpense") // label for swagger
    @PatchMapping("/{matterExpenseId}")
	public ResponseEntity<?> patchMatterExpense(@PathVariable Long matterExpenseId, 
			@Valid @RequestBody UpdateMatterExpense updateMatterExpense, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense updatedMatterExpense = 
				matterExpenseService.updateMatterExpense(matterExpenseId, updateMatterExpense, loginUserID);
		return new ResponseEntity<>(updatedMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Update MatterExpenses") // label for swagger
    @PatchMapping("/status")
	public ResponseEntity<?> patchMatterExpense(@RequestBody List<UpdateMatterExpense> updateMatterExpense, 
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		List<MatterExpense> updatedMatterExpense = 
				matterExpenseService.updateMatterExpense(updateMatterExpense, loginUserID);
		return new ResponseEntity<>(updatedMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Delete MatterExpense") // label for swagger
	@DeleteMapping("/{matterExpenseId}")
	public ResponseEntity<?> deleteMatterExpense(@PathVariable Long matterExpenseId, @RequestParam String loginUserID) {
    	matterExpenseService.deleteMatterExpense(matterExpenseId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}