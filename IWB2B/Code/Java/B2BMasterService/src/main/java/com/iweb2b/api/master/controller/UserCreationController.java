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

import com.iweb2b.api.master.model.usercreation.AddUserCreation;
import com.iweb2b.api.master.model.usercreation.UpdateUserCreation;
import com.iweb2b.api.master.model.usercreation.UserCreation;
import com.iweb2b.api.master.service.UserCreationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "UserCreation" }, value = "UserCreation Operations related to UserCreationController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "UserCreation", description = "Operations related to UserCreation") })
@RequestMapping("/userCreation")
@RestController
public class UserCreationController {

	@Autowired
	UserCreationService userCreationService;

	@ApiOperation(response = UserCreation.class, value = "Get all UserCreation details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserCreation> userCreationList = userCreationService.getUserCreation();
		return new ResponseEntity<>(userCreationList, HttpStatus.OK);
	}

	@ApiOperation(response = UserCreation.class, value = "Get a UserCreation") // label for swagger
	@GetMapping("/{userCreationId}")
	public ResponseEntity<?> getUserCreation(@PathVariable String userCreationId) {
		UserCreation dbUserCreation = userCreationService.getUserCreation(userCreationId);
		log.info("UserCreation : " + dbUserCreation);
		return new ResponseEntity<>(dbUserCreation, HttpStatus.OK);
	}

	@ApiOperation(response = UserCreation.class, value = "Create UserCreation") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserCreation(@Valid @RequestBody AddUserCreation newUserCreation,
			@RequestParam String loginUserID) throws Exception {
		UserCreation createdUserCreation = userCreationService.createUserCreation(newUserCreation, loginUserID);
		return new ResponseEntity<>(createdUserCreation, HttpStatus.OK);
	}

	@ApiOperation(response = UserCreation.class, value = "Update UserCreation") // label for swagger
	@PatchMapping("/{userCreation}")
	public ResponseEntity<?> patchUserCreation(@PathVariable String userCreation,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateUserCreation updateUserCreation)
			throws IllegalAccessException, InvocationTargetException {
		UserCreation updatedUserCreation = userCreationService.updateUserCreation(userCreation, loginUserID,
				updateUserCreation);
		return new ResponseEntity<>(updatedUserCreation, HttpStatus.OK);
	}

	@ApiOperation(response = UserCreation.class, value = "Delete UserCreation") // label for swagger
	@DeleteMapping("/{userCreation}")
	public ResponseEntity<?> deleteUserCreation(@PathVariable String userCreation, @RequestParam String loginUserID) {
		userCreationService.deleteUserCreation(userCreation, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
