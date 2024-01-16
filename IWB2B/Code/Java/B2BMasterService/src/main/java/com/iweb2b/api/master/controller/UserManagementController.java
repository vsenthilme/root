package com.iweb2b.api.master.controller;

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

import com.iweb2b.api.master.model.usermanagement.AddUserManagement;
import com.iweb2b.api.master.model.usermanagement.UpdateUserManagement;
import com.iweb2b.api.master.model.usermanagement.UserManagement;
import com.iweb2b.api.master.service.UserManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "UserManagement" }, value = "UserManagement Operations related to UserManagementController") 
@SwaggerDefinition(tags = { @Tag(name = "UserManagement", description = "Operations related to UserManagement") })
@RequestMapping("/userManagement")
@RestController
public class UserManagementController {

	@Autowired
	UserManagementService userManagementService;

	@ApiOperation(response = UserManagement.class, value = "Get all UserManagement details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserManagement> userManagementList = userManagementService.getUserManagement();
		return new ResponseEntity<>(userManagementList, HttpStatus.OK);
	}

	@ApiOperation(response = UserManagement.class, value = "Get a UserManagement") // label for swagger
	@GetMapping("/{userManagementId}")
	public ResponseEntity<?> getUserManagement(@PathVariable String userManagementId) {
		UserManagement dbUserManagement = userManagementService.getUserManagement(userManagementId);
		log.info("UserManagement : " + dbUserManagement);
		return new ResponseEntity<>(dbUserManagement, HttpStatus.OK);
	}

	@ApiOperation(response = UserManagement.class, value = "Create UserManagement") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserManagement(@Valid @RequestBody AddUserManagement newUserManagement,
			@RequestParam String loginUserID) throws Exception {
		UserManagement createdUserManagement = userManagementService.createUserManagement(newUserManagement, loginUserID);
		return new ResponseEntity<>(createdUserManagement, HttpStatus.OK);
	}

	@ApiOperation(response = UserManagement.class, value = "Update UserManagement") // label for swagger
	@PatchMapping("/{userManagement}")
	public ResponseEntity<?> patchUserManagement(@PathVariable String userManagement,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateUserManagement updateUserManagement)
			throws IllegalAccessException, InvocationTargetException {
		UserManagement updatedUserManagement = userManagementService.updateUserManagement(userManagement, loginUserID,
				updateUserManagement);
		return new ResponseEntity<>(updatedUserManagement, HttpStatus.OK);
	}

	@ApiOperation(response = UserManagement.class, value = "Delete UserManagement") // label for swagger
	@DeleteMapping("/{userManagement}")
	public ResponseEntity<?> deleteUserManagement(@PathVariable String userManagement, @RequestParam String loginUserID) {
		userManagementService.deleteUserManagement(userManagement, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
