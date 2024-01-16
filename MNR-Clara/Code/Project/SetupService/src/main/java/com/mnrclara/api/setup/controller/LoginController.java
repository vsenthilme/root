package com.mnrclara.api.setup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.clientuser.ClientUser;
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
@Api(tags = {"Login"}, value = "Login Operations related to User Login") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Login",description = "Operations related to Login")})
@RequestMapping("/login")
@RestController
public class LoginController {
	
	@Autowired
	UserProfileService userProfileService;
	
    @ApiOperation(response = UserProfile.class, value = "Validate Login User") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> validateUser(@RequestParam String userId, @RequestParam String password) {
    	log.info("UserId: " + userId + " - " + "Password: " + password);
    	UserProfile userProfile = userProfileService.validateUser(userId, password);
		return new ResponseEntity<>(userProfile, HttpStatus.OK);
	}
    
    //-------------------Client-Portal-UserLogin-------------------------------------------------
    @ApiOperation(response = UserProfile.class, value = "Validate Login User") 	// label for swagger
	@GetMapping("/clientUser/sendOTP")
	public ResponseEntity<?> sendOTP(@RequestParam String contactNumber) {
    	Boolean response = userProfileService.sendOTP(contactNumber);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Validate Login User") 	// label for swagger
   	@GetMapping("/clientUser/verifyOTP")
   	public ResponseEntity<?> verifyOTP (@RequestParam String contactNumber, @RequestParam Long otp) {
       	ClientUser clientUser = userProfileService.validateOtp (contactNumber, otp);
   		return new ResponseEntity<>(clientUser, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
   	@GetMapping("/clientUser/emailOTP")
   	public ResponseEntity<?> clientUserEmailOTP (@RequestParam String emailId) {
       	boolean response = userProfileService.clientUserEmailOTP(emailId);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
	@GetMapping("/clientUser/verifyEmailOTP")
	public ResponseEntity<?> clientUserVerifyEmailOTP (@RequestParam String emailId, @RequestParam Long otp) {
    	ClientUser response = userProfileService.verifyEmailOTPForAppUser(emailId, otp);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    //-----------------------EMAIL-OTP-----------------------------------------------------------
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
	@GetMapping("/emailOTP")
	public ResponseEntity<?> emailOTP (@RequestParam String userId) {
    	boolean response = userProfileService.emailOTP(userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
	@GetMapping("/verifyEmailOTP")
	public ResponseEntity<?> verifyEmailOTP (@RequestParam String userId, @RequestParam Long otp) {
    	Boolean response = userProfileService.verifyEmailOTP(userId, otp);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}