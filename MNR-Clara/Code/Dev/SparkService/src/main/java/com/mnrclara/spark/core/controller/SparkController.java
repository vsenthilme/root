package com.mnrclara.spark.core.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.spark.core.model.InvoiceHeader;
import com.mnrclara.spark.core.model.SearchInvoiceHeader;
import com.mnrclara.spark.core.model.User;
import com.mnrclara.spark.core.service.InvoiceService;
import com.mnrclara.spark.core.service.UserService;
import com.mnrclara.spark.core.util.Client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = {"User"}, value = "User Operations related to UserController") 
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to User")})
public class SparkController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	InvoiceService invoiceService;
	
    @ApiOperation(response = Optional.class, value = "Get all Users") 
	@GetMapping("/user")
	public ResponseEntity<?> getAll() {
		List<User> userList = userService.getUsers();
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Get a User") 
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
    	List<User> userList = userService.getUsers();
    	User user = userList.stream()
    			.filter(u -> u.getUserId() == id)
    			.findFirst()
                .orElse(null);
    	log.info("User : " + user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Create User") 
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestBody User newUser) {
    	log.info("User name: " + newUser.getUserName());
    	User createdUser = userService.createUser(newUser);
		return new ResponseEntity<>(createdUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Patch User") 
	@PatchMapping("/user")
	public ResponseEntity<?> patchUser(@RequestBody User modifiedUser) {
    	log.info("User name: " + modifiedUser.getUserName());
    	User updatedUser = userService.patchUser(modifiedUser);
		return new ResponseEntity<>(updatedUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Delete User") 
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    	userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    
    @ApiOperation(response = Optional.class, value = "Spark Test") 
   	@GetMapping("/spark")
   	public ResponseEntity<?> sparkTest() throws Exception {
    	List<Client> client = userService.sparkProcess();
   		return new ResponseEntity<>(client, HttpStatus.OK);
   	}
    
    @ApiOperation(response = Optional.class, value = "Spark Test") 
   	@PostMapping("/spark/invoiceHeader")
   	public ResponseEntity<?> findInvoiceHeaders(@RequestBody SearchInvoiceHeader searchInvoiceHeader) throws Exception {
//    	List<Client> client = userService.sparkProcess();
    	List<InvoiceHeader> invoiceHeaders = invoiceService.findInvoiceHeaders(searchInvoiceHeader);
   		return new ResponseEntity<>(invoiceHeaders, HttpStatus.OK);
   	}
}