package com.mnrclara.api.accounting.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

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

import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeader;
import com.mnrclara.api.accounting.model.paymentplan.SearchPaymentPlanHeader;
import com.mnrclara.api.accounting.service.PaymentPlanHeaderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"PaymentPlanHeader"}, value = "PaymentPlanHeader  Operations related to PaymentPlanHeaderController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PaymentPlanHeader ",description = "Operations related to PaymentPlanHeader ")})
@RequestMapping("/paymentplanheader")
@RestController
public class PaymentPlanHeaderController {
	
	@Autowired
	PaymentPlanHeaderService paymentPlanHeaderService;
	
    @ApiOperation(response = PaymentPlanHeader.class, value = "Get all PaymentPlanHeader details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PaymentPlanHeader> paymentplanheaderList = paymentPlanHeaderService.getPaymentPlanHeaders();
		return new ResponseEntity<>(paymentplanheaderList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "paymentPlanNumber") String sortBy) {
		Page<PaymentPlanHeader> list = paymentPlanHeaderService.getAllPaymentPlanHeaders(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Get a PaymentPlanHeader") // label for swagger 
	@GetMapping("/{paymentPlanNumber}")
	public ResponseEntity<?> getPaymentPlanHeader(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo) {
    	PaymentPlanHeader paymentplanheader = paymentPlanHeaderService.getPaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo);
    	log.info("PaymentPlanHeader : " + paymentplanheader);
		return new ResponseEntity<>(paymentplanheader, HttpStatus.OK);
	}
    
	@ApiOperation(response = PaymentPlanHeader.class, value = "Search PaymentPlanHeader") // label for swagger
	@PostMapping("/findPaymentPlanHeader")
	public List<PaymentPlanHeader> findPaymentPlanHeader(@RequestBody SearchPaymentPlanHeader searchPaymentPlanHeader)
			throws Exception {
		return paymentPlanHeaderService.findPaymentPlanHeader(searchPaymentPlanHeader);
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Create PaymentPlanHeader") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postPaymentPlanHeader(@Valid @RequestBody PaymentPlanHeader newPaymentPlanHeader, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		PaymentPlanHeader createdPaymentPlanHeader = paymentPlanHeaderService.createPaymentPlanHeader(newPaymentPlanHeader, loginUserID);
		return new ResponseEntity<>(createdPaymentPlanHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Update PaymentPlanHeader") // label for swagger
    @PatchMapping("/{paymentPlanNumber}")
	public ResponseEntity<?> patchPaymentPlanHeader(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo,
			@Valid @RequestBody PaymentPlanHeader updatePaymentPlanHeader, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		PaymentPlanHeader createdPaymentPlanHeader = 
				paymentPlanHeaderService.updatePaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo, updatePaymentPlanHeader, loginUserID);
		return new ResponseEntity<>(createdPaymentPlanHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = PaymentPlanHeader.class, value = "Delete PaymentPlanHeader") // label for swagger
	@DeleteMapping("/{paymentPlanNumber}")
	public ResponseEntity<?> deletePaymentPlanHeader(@PathVariable String paymentPlanNumber, @RequestParam Long paymentPlanRevisionNo, 
			@RequestParam String loginUserID) {
    	paymentPlanHeaderService.deletePaymentPlanHeader(paymentPlanNumber, paymentPlanRevisionNo, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}