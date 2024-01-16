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

import com.mnrclara.api.setup.model.transaction.AddTransaction;
import com.mnrclara.api.setup.model.transaction.Transaction;
import com.mnrclara.api.setup.model.transaction.UpdateTransaction;
import com.mnrclara.api.setup.service.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Transaction"}, value = "Transaction Operations related to TransactionController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Transaction",description = "Operations related to Transaction")})
@RequestMapping("/transaction")
@RestController
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
    @ApiOperation(response = Transaction.class, value = "Get all Transaction details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Transaction> transactionList = transactionService.getTransactions();
		return new ResponseEntity<>(transactionList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Get a Transaction") // label for swagger 
	@GetMapping("/{transactionId}")
	public ResponseEntity<?> getTransaction(@PathVariable Long transactionId) {
    	Transaction transaction = transactionService.getTransaction(transactionId);
    	log.info("Transaction : " + transaction);
		return new ResponseEntity<>(transaction, HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Create Transaction") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addTransaction(@Valid @RequestBody AddTransaction newTransaction, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Transaction createdTransaction = transactionService.createTransaction(newTransaction, loginUserID);
		return new ResponseEntity<>(createdTransaction , HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Update Transaction") // label for swagger
    @PatchMapping("/{transactionId}")
	public ResponseEntity<?> patchTransaction(@PathVariable Long transactionId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateTransaction updateTransaction) 
			throws IllegalAccessException, InvocationTargetException {
		Transaction updatedTransaction = transactionService.updateTransaction(transactionId, loginUserID, updateTransaction);
		return new ResponseEntity<>(updatedTransaction , HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Delete Transaction") // label for swagger
	@DeleteMapping("/{transactionId}")
	public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId, @RequestParam String loginUserID) {
    	transactionService.deleteTransaction(transactionId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}