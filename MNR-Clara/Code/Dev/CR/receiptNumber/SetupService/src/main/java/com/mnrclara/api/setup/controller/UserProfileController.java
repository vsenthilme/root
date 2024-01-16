package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.mnrclara.api.setup.model.userprofile.AddUserProfile;
import com.mnrclara.api.setup.model.userprofile.UpdateUserProfile;
import com.mnrclara.api.setup.model.userprofile.UserProfile;
import com.mnrclara.api.setup.service.UserProfileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"UserProfile"}, value = "UserProfile Operations related to UserProfileController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "UserProfile",description = "Operations related to UserProfile")})
@RequestMapping("/userProfile")
@RestController
public class UserProfileController {
	
	@Autowired
	UserProfileService userProfileService;
	
    @ApiOperation(response = UserProfile.class, value = "Get all UserProfile details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserProfile> userProfileList = userProfileService.getUserProfiles();
		return new ResponseEntity<>(userProfileList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile") // label for swagger 
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
    	UserProfile userProfile = userProfileService.getUserProfile(userId);
    	log.info("UserProfile : " + userProfile);
		return new ResponseEntity<>(userProfile, HttpStatus.OK);
	}
    
    @ApiOperation(response = List.class, value = "Get a UserProfile ClassIDs") // label for swagger 
   	@GetMapping("/{userId}/classId")
   	public ResponseEntity<?> getAllClassIdByUserId(@RequestParam String userId) {
       	List<Long> classIds = userProfileService.findClassByUserId(userId);
       	Map<String, List<Long>> body = new HashMap<>();
        body.put("classId", classIds);
   		return new ResponseEntity<>(body, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile Language") // label for swagger 
 	@GetMapping("/{userId}/lang")
 	public ResponseEntity<?> getUserLangID(@PathVariable String userId) {
     	UserProfile userProfile = userProfileService.getUserProfile(userId);
     	if (userProfile != null) {
     		log.info("UserProfile : " + userProfile);
     		return new ResponseEntity<>(userProfile.getLanguageId(), HttpStatus.OK);
     	}
		return null;
 	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile Class ID") // label for swagger 
   	@GetMapping("/classId/{classId}")
   	public ResponseEntity<?> getUserProfileByClassId(@PathVariable Long classId) {
       	UserProfile userProfile = userProfileService.getUserProfile(classId);
       	log.info("UserProfile : " + userProfile);
   		return new ResponseEntity<>(userProfile, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Forgot Password") // label for swagger 
   	@GetMapping("/forgotPassword")
   	public ResponseEntity<?> forgotPassword(@RequestParam String userId, @RequestParam String emailAddress) {
       	UserProfile userProfile = userProfileService.getUserProfile(userId, emailAddress);
       	log.info("UserProfile : " + userProfile);
   		return new ResponseEntity<>(userProfile, HttpStatus.OK);
   	}

    @ApiOperation(response = UserProfile.class, value = "Create UserProfile") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserProfile(@Valid @RequestBody AddUserProfile newUserProfile, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		UserProfile createdUserProfile = userProfileService.createUserProfile(newUserProfile, loginUserID);
		return new ResponseEntity<>(createdUserProfile , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Update UserProfile") // label for swagger
    @PatchMapping("/{userId}")
	public ResponseEntity<?> patchUserProfile(@PathVariable String userId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateUserProfile updateUserProfile) 
			throws IllegalAccessException, InvocationTargetException {
		UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, loginUserID, updateUserProfile);
		return new ResponseEntity<>(updatedUserProfile , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Delete UserProfile") // label for swagger
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUserProfile(@PathVariable String userId, @RequestParam String loginUserID) {
    	userProfileService.deleteUserProfile(userId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}