package com.mnrclara.api.accounting.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.accounting.model.prebill.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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

import com.mnrclara.api.accounting.service.PreBillDetailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"PreBillDetails"}, value = "PreBillDetails  Operations related to PreBillDetailsController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PreBillDetails ",description = "Operations related to PreBillDetails ")})
@RequestMapping("/prebilldetails")
@RestController
public class PreBillDetailsController {
	
	@Autowired
	PreBillDetailsService prebilldetailsService;
	
    @ApiOperation(response = PreBillDetails.class, value = "Get all PreBillDetails details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<AddPreBillDetails> prebilldetailsList = prebilldetailsService.getPreBillDetailss();
		return new ResponseEntity<>(prebilldetailsList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = PreBillDetails.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "preBillNumber") String sortBy) {
		Page<PreBillDetails> list = prebilldetailsService.getAllPreBillDetailss(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
    
    @ApiOperation(response = PreBillDetails.class, value = "Get a PreBillDetails") // label for swagger 
	@GetMapping("/{preBillNumber}")
	public ResponseEntity<?> getPreBillDetails(@PathVariable String preBillNumber) {
    	PreBillDetails prebilldetails = prebilldetailsService.getPreBillDetails(preBillNumber);
		return new ResponseEntity<>(prebilldetails, HttpStatus.OK);
	}
    
	@ApiOperation(response = PreBillDetails.class, value = "Search PreBillDetails") // label for swagger
	@PostMapping("/findPreBillDetails")
	public List<AddPreBillDetails> findPreBillDetails(@RequestBody SearchPreBillDetails searchPreBillDetails)
			throws Exception {
		return prebilldetailsService.findPreBillDetails(searchPreBillDetails);
	}
	
    @ApiOperation(response = PreBillDetails.class, value = "Create PreBillDetails") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postPreBillDetails(@Valid @RequestBody List<AddPreBillDetails> newPreBillDetails, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		List<AddPreBillDetails> createdPreBillDetails = 
				prebilldetailsService.createPreBillDetails(newPreBillDetails, loginUserID);
		prebilldetailsService.sendNotification(createdPreBillDetails.get(0),"Prebill Create");
		return new ResponseEntity<>(createdPreBillDetails , HttpStatus.OK);
	}
    
    /*---------------------------------------------------------------------------------------------------------------*/
    @ApiOperation(response = MatterTimeExpenseTicket.class, value = "Execute Bill") // label for swagger
   	@PostMapping("/executeBill")
   	public ResponseEntity<?> executeBill(@Valid @RequestBody BillByGroup newBillByGroup, @RequestParam Boolean isByIndividual) 
   			throws IllegalAccessException, InvocationTargetException, ParseException {
    	log.info("newBillByGroup : " + newBillByGroup);
    	MatterTimeExpenseTicket[] arrMatterTimeExpenseTicket = 
    			prebilldetailsService.executeBill(newBillByGroup, isByIndividual);
    	return new ResponseEntity<>(arrMatterTimeExpenseTicket, HttpStatus.OK);
   	}
    
    /*---------------------------------------------------------------------------------------------------------------*/
    @ApiOperation(response = PreBillDetails.class, value = "Update PreBillDetails") // label for swagger
	@PatchMapping("/{preBillBatchNumber}")
	public ResponseEntity<?> updatePreBillDetails(@PathVariable String preBillBatchNumber, @RequestBody UpdatePreBillDetails updatePreBillDetails, 
			@RequestParam String preBillNumber, @RequestParam Date preBillDate, @RequestParam String matterNumber, @RequestParam String loginUserID) {
    	PreBillDetails updatedPreBillDetails = 
    			prebilldetailsService.updatePreBillDetails(preBillBatchNumber, preBillNumber, preBillDate, matterNumber, 
    			updatePreBillDetails, loginUserID);
		return new ResponseEntity<>(updatedPreBillDetails, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(response = PreBillDetails.class, value = "Approve PreBillDetails") // label for swagger
	@PostMapping("/approve")
	public ResponseEntity<?> approvePreBillDetails(@Valid @RequestBody List<PreBillApproveSaveDetails> preBillApproveSaveDetails, @RequestParam String loginUserID) {
		List<AddPreBillDetails> response = prebilldetailsService.approvePreBillDetails(preBillApproveSaveDetails, loginUserID);
		if (response != null && !response.isEmpty()) {
			prebilldetailsService.sendNotification(response.get(0),"Prebill Approve");
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = PreBillDetails.class, value = "Approve PreBillDetails") // label for swagger
	@PostMapping("/save")
	public ResponseEntity<?> savePreBillDetails(@Valid @RequestBody List<PreBillApproveSaveDetails> preBillApproveSaveDetails, 
			@RequestParam String loginUserID) {
		List<AddPreBillDetails> response = prebilldetailsService.savePreBillDetails(preBillApproveSaveDetails, loginUserID);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = PreBillDetails.class, value = "Delete PreBillDetails") // label for swagger
	@DeleteMapping("/{preBillNumber}")
	public ResponseEntity<?> deletePreBillDetails(@PathVariable String preBillNumber, @RequestParam String loginUserID) {
    	prebilldetailsService.deletePreBillDetails(preBillNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}