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

import com.mnrclara.api.setup.model.usertype.AddUserType;
import com.mnrclara.api.setup.model.usertype.UpdateUserType;
import com.mnrclara.api.setup.model.usertype.UserType;
import com.mnrclara.api.setup.service.UserTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"UserType"}, value = "UserType Operations related to UserTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "UserType",description = "Operations related to UserType")})
@RequestMapping("/userType")
@RestController
public class UserTypeController {
	
	@Autowired
	UserTypeService userTypeService;
	
    @ApiOperation(response = UserType.class, value = "Get all UserType details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserType> userTypeList = userTypeService.getUserTypes();
		return new ResponseEntity<>(userTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Get a UserType") // label for swagger 
	@GetMapping("/{userTypeId}")
	public ResponseEntity<?> getUserType(@PathVariable Long userTypeId) {
    	UserType usertype = userTypeService.getUserType(userTypeId);
    	log.info("UserType : " + usertype);
		return new ResponseEntity<>(usertype, HttpStatus.OK);
	}
    
//    @ApiOperation(response = UserType.class, value = "Get a UserType") // label for swagger 
//   	@GetMapping("/{userTypeId}")
//   	public ResponseEntity<?> getUserType(@PathVariable Long userTypeId, @RequestParam String languageId, @RequestParam Long classId) {
//       	UserType usertype = userTypeService.getUserType(userTypeId, languageId, classId);
//       	log.info("UserType : " + usertype);
//   		return new ResponseEntity<>(usertype, HttpStatus.OK);
//   	}
    
    @ApiOperation(response = UserType.class, value = "Create UserType") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addUserType(@Valid @RequestBody AddUserType newUserType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		UserType createdUserType = userTypeService.createUserType(newUserType, loginUserID);
		return new ResponseEntity<>(createdUserType , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Update UserType") // label for swagger
    @PatchMapping("/{userTypeId}")
	public ResponseEntity<?> patchUserType(@PathVariable Long userTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateUserType updateUserType) 
			throws IllegalAccessException, InvocationTargetException {
		UserType updatedUserType = userTypeService.updateUserType(userTypeId, loginUserID, updateUserType);
		return new ResponseEntity<>(updatedUserType , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Delete UserType") // label for swagger
	@DeleteMapping("/{userTypeId}")
	public ResponseEntity<?> deleteUserType(@PathVariable Long userTypeId,  @RequestParam String loginUserID) {
    	userTypeService.deleteUserType(userTypeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}