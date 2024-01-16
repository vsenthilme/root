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

import com.iweb2b.api.master.model.subscriptionmgmt.AddSubscriptionManagement;
import com.iweb2b.api.master.model.subscriptionmgmt.SubscriptionManagement;
import com.iweb2b.api.master.model.subscriptionmgmt.UpdateSubscriptionManagement;
import com.iweb2b.api.master.service.SubscriptionManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "SubscriptionManagement" }, value = "SubscriptionManagement Operations related to SubscriptionManagementController") 
@SwaggerDefinition(tags = { @Tag(name = "SubscriptionManagement", description = "Operations related to SubscriptionManagement") })
@RequestMapping("/subscriptionManagement")
@RestController
public class SubscriptionManagementController {

	@Autowired
	SubscriptionManagementService subscriptionManagementService;

	@ApiOperation(response = SubscriptionManagement.class, value = "Get all SubscriptionManagement details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<SubscriptionManagement> subscriptionManagementList = subscriptionManagementService.getSubscriptionManagement();
		return new ResponseEntity<>(subscriptionManagementList, HttpStatus.OK);
	}

	@ApiOperation(response = SubscriptionManagement.class, value = "Get a SubscriptionManagement") // label for swagger
	@GetMapping("/{subscriptionManagementId}")
	public ResponseEntity<?> getSubscriptionManagement(@PathVariable String subscriptionManagementId) {
		SubscriptionManagement dbSubscriptionManagement = subscriptionManagementService.getSubscriptionManagement(subscriptionManagementId);
		log.info("SubscriptionManagement : " + dbSubscriptionManagement);
		return new ResponseEntity<>(dbSubscriptionManagement, HttpStatus.OK);
	}

	@ApiOperation(response = SubscriptionManagement.class, value = "Create SubscriptionManagement") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postSubscriptionManagement(@Valid @RequestBody AddSubscriptionManagement newSubscriptionManagement,
			@RequestParam String loginUserID) throws Exception {
		SubscriptionManagement createdSubscriptionManagement = subscriptionManagementService.createSubscriptionManagement(newSubscriptionManagement, loginUserID);
		return new ResponseEntity<>(createdSubscriptionManagement, HttpStatus.OK);
	}

	@ApiOperation(response = SubscriptionManagement.class, value = "Update SubscriptionManagement") // label for swagger
	@PatchMapping("/{subscriptionManagement}")
	public ResponseEntity<?> patchSubscriptionManagement(@PathVariable String subscriptionManagement,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateSubscriptionManagement updateSubscriptionManagement)
			throws IllegalAccessException, InvocationTargetException {
		SubscriptionManagement updatedSubscriptionManagement = subscriptionManagementService.updateSubscriptionManagement(subscriptionManagement, loginUserID,
				updateSubscriptionManagement);
		return new ResponseEntity<>(updatedSubscriptionManagement, HttpStatus.OK);
	}

	@ApiOperation(response = SubscriptionManagement.class, value = "Delete SubscriptionManagement") // label for swagger
	@DeleteMapping("/{subscriptionManagement}")
	public ResponseEntity<?> deleteSubscriptionManagement(@PathVariable String subscriptionManagement, @RequestParam String loginUserID) {
		subscriptionManagementService.deleteSubscriptionManagement(subscriptionManagement, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
