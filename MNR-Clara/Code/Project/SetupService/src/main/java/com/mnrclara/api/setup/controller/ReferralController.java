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

import com.mnrclara.api.setup.model.referral.AddReferral;
import com.mnrclara.api.setup.model.referral.Referral;
import com.mnrclara.api.setup.model.referral.UpdateReferral;
import com.mnrclara.api.setup.service.ReferralService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Referral"}, value = "Referral Operations related to ReferralController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Referral",description = "Operations related to Referral")})
@RequestMapping("/referral")
@RestController
public class ReferralController {
	
	@Autowired
	ReferralService referralService;
	
    @ApiOperation(response = Referral.class, value = "Get all Referral details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Referral> referralList = referralService.getReferrals();
		return new ResponseEntity<>(referralList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Get a Referral") // label for swagger 
	@GetMapping("/{referralId}")
	public ResponseEntity<?> getReferral(@PathVariable Long referralId) {
    	Referral referral = referralService.getReferral(referralId);
    	log.info("Referral : " + referral);
		return new ResponseEntity<>(referral, HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Create Referral") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addReferral(@Valid @RequestBody AddReferral newReferral, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Referral createdReferral = referralService.createReferral(newReferral, loginUserID);
		return new ResponseEntity<>(createdReferral , HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Update Referral") // label for swagger
    @PatchMapping("/{referralId}")
	public ResponseEntity<?> patchReferral(@PathVariable Long referralId, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateReferral updateReferral) 
			throws IllegalAccessException, InvocationTargetException {
		Referral updatedReferral = referralService.updateReferral(languageId, classId, referralId, loginUserID, updateReferral);
		return new ResponseEntity<>(updatedReferral , HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Delete Referral") // label for swagger
	@DeleteMapping("/{referralId}")
	public ResponseEntity<?> deleteReferral(@PathVariable Long referralId, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String loginUserID) {
    	referralService.deleteReferral(languageId, classId, referralId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}