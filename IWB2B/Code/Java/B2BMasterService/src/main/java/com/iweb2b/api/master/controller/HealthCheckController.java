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

import com.iweb2b.api.master.model.healthcheck.AddHealthCheck;
import com.iweb2b.api.master.model.healthcheck.HealthCheck;
import com.iweb2b.api.master.model.healthcheck.UpdateHealthCheck;
import com.iweb2b.api.master.service.HealthCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "HealthCheck" }, value = "HealthCheck Operations related to HealthCheckController") 
@SwaggerDefinition(tags = { @Tag(name = "HealthCheck", description = "Operations related to HealthCheck") })
@RequestMapping("/healthCheck")
@RestController
public class HealthCheckController {

	@Autowired
	HealthCheckService apiConsoleHealthCheckService;

	@ApiOperation(response = HealthCheck.class, value = "Get all HealthCheck details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<HealthCheck> apiConsoleHealthCheckList = apiConsoleHealthCheckService.getHealthCheck();
		return new ResponseEntity<>(apiConsoleHealthCheckList, HttpStatus.OK);
	}

	@ApiOperation(response = HealthCheck.class, value = "Get a HealthCheck") // label for swagger
	@GetMapping("/{healthCheckId}")
	public ResponseEntity<?> getHealthCheck(@PathVariable String healthCheckId) {
		HealthCheck dbHealthCheck = apiConsoleHealthCheckService.getHealthCheck(healthCheckId);
		log.info("HealthCheck : " + dbHealthCheck);
		return new ResponseEntity<>(dbHealthCheck, HttpStatus.OK);
	}

	@ApiOperation(response = HealthCheck.class, value = "Create HealthCheck") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postHealthCheck(@Valid @RequestBody AddHealthCheck newHealthCheck,
			@RequestParam String loginUserID) throws Exception {
		HealthCheck createdHealthCheck = apiConsoleHealthCheckService.createHealthCheck(newHealthCheck, loginUserID);
		return new ResponseEntity<>(createdHealthCheck, HttpStatus.OK);
	}

	@ApiOperation(response = HealthCheck.class, value = "Update HealthCheck") // label for swagger
	@PatchMapping("/{healthCheck}")
	public ResponseEntity<?> patchHealthCheck(@PathVariable String healthCheck,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateHealthCheck updateHealthCheck)
			throws IllegalAccessException, InvocationTargetException {
		HealthCheck updatedHealthCheck = apiConsoleHealthCheckService.updateHealthCheck(healthCheck, loginUserID,
				updateHealthCheck);
		return new ResponseEntity<>(updatedHealthCheck, HttpStatus.OK);
	}

	@ApiOperation(response = HealthCheck.class, value = "Delete HealthCheck") // label for swagger
	@DeleteMapping("/{healthCheck}")
	public ResponseEntity<?> deleteHealthCheck(@PathVariable String healthCheck, @RequestParam String loginUserID) {
		apiConsoleHealthCheckService.deleteHealthCheck(healthCheck, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
