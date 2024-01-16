package com.iweb2b.api.integration.controller;

import com.iweb2b.api.integration.model.usermanagement.*;
import com.iweb2b.api.integration.service.UserAccessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"UserAccess"}, value = "UserAccess  Operations related to UserAccessController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "UserAccess ",description = "Operations related to UserAccess ")})
@RequestMapping("/useraccess")
@RestController
public class UserAccessController {

	@Autowired
	UserAccessService userAccessService;

	@ApiOperation(response = UserAccess.class, value = "Get all UserAccess details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserAccess> userManagementList = userAccessService.getUserAccesss();
		return new ResponseEntity<>(userManagementList, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Get a UserAccess") // label for swagger 
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserAccess(@PathVariable String userId) {
		UserAccess userManagement = userAccessService.getUserAccess(userId);
		log.info("UserAccess : " + userManagement);
		return new ResponseEntity<>(userManagement, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Create UserAccess") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserAccess(@Valid @RequestBody AddUserAccess newUserAccess, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		try {
			UserAccess createdUserAccess = userAccessService.createUserAccess(newUserAccess, loginUserID);
			return new ResponseEntity<>(createdUserAccess , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.toString() , HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(response = UserAccess.class, value = "Update UserAccess") // label for swagger
	@PatchMapping("/{userId}")
	public ResponseEntity<?> patchUserAccess(@PathVariable String userId,
												 @RequestBody UpdateUserAccess updateUserAccess, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess updatedUserAccess =
				userAccessService.updateUserAccess(userId, updateUserAccess, loginUserID);
		return new ResponseEntity<>(updatedUserAccess , HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Delete UserAccess") // label for swagger
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUserAccess(@PathVariable String userId,
												  @RequestParam String loginUserID) {
		userAccessService.deleteUserAccess(userId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	//Search
	@ApiOperation(response = UserAccess.class, value = "Find UserAccess") // label for swagger
	@PostMapping("/findUserAccess")
	public ResponseEntity<?> findUserAccess(@Valid @RequestBody FindUserAccess findUserAccess) throws Exception {
		List<UserAccess> createdUserAccess = userAccessService.findUserAccess(findUserAccess);
		return new ResponseEntity<>(createdUserAccess, HttpStatus.OK);
	}

	//------------------login-------------------------------------------------------------
	@ApiOperation(response = UserAccess.class, value = "Validate Login User") // label for swagger
	@GetMapping("/login")
	public ResponseEntity<?> validateUserID(@RequestParam String userID, @RequestParam String password) {
		log.info("UserID:" + userID + " - " + "Password: " + password );
		UserAccess validatedUser = userAccessService.validateUser(userID, password);
		return new ResponseEntity<>(validatedUser, HttpStatus.OK);
	}

}