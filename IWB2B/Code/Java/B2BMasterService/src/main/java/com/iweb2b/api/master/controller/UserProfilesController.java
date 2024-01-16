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

import com.iweb2b.api.master.model.userprofiles.AddUserProfiles;
import com.iweb2b.api.master.model.userprofiles.UpdateUserProfiles;
import com.iweb2b.api.master.model.userprofiles.UserProfiles;
import com.iweb2b.api.master.service.UserProfilesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "UserProfiles" }, value = "UserProfiles Operations related to UserProfilesController") 
@SwaggerDefinition(tags = { @Tag(name = "UserProfiles", description = "Operations related to UserProfiles") })
@RequestMapping("/userProfiles")
@RestController
public class UserProfilesController {

	@Autowired
	UserProfilesService userProfilesService;

	@ApiOperation(response = UserProfiles.class, value = "Get all UserProfiles details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserProfiles> userProfilesList = userProfilesService.getUserProfiles();
		return new ResponseEntity<>(userProfilesList, HttpStatus.OK);
	}

	@ApiOperation(response = UserProfiles.class, value = "Get a UserProfiles") // label for swagger
	@GetMapping("/{userProfilesId}")
	public ResponseEntity<?> getUserProfiles(@PathVariable String userProfilesId) {
		UserProfiles dbUserProfiles = userProfilesService.getUserProfiles(userProfilesId);
		log.info("UserProfiles : " + dbUserProfiles);
		return new ResponseEntity<>(dbUserProfiles, HttpStatus.OK);
	}

	@ApiOperation(response = UserProfiles.class, value = "Create UserProfiles") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserProfiles(@Valid @RequestBody AddUserProfiles newUserProfiles,
			@RequestParam String loginUserID) throws Exception {
		UserProfiles createdUserProfiles = userProfilesService.createUserProfiles(newUserProfiles, loginUserID);
		return new ResponseEntity<>(createdUserProfiles, HttpStatus.OK);
	}

	@ApiOperation(response = UserProfiles.class, value = "Update UserProfiles") // label for swagger
	@PatchMapping("/{userProfiles}")
	public ResponseEntity<?> patchUserProfiles(@PathVariable String userProfiles,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateUserProfiles updateUserProfiles)
			throws IllegalAccessException, InvocationTargetException {
		UserProfiles updatedUserProfiles = userProfilesService.updateUserProfiles(userProfiles, loginUserID,
				updateUserProfiles);
		return new ResponseEntity<>(updatedUserProfiles, HttpStatus.OK);
	}

	@ApiOperation(response = UserProfiles.class, value = "Delete UserProfiles") // label for swagger
	@DeleteMapping("/{userProfiles}")
	public ResponseEntity<?> deleteUserProfiles(@PathVariable String userProfiles, @RequestParam String loginUserID) {
		userProfilesService.deleteUserProfiles(userProfiles, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
