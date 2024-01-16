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

import com.iweb2b.api.master.model.setupconfiguration.AddSetupConfiguration;
import com.iweb2b.api.master.model.setupconfiguration.SetupConfiguration;
import com.iweb2b.api.master.model.setupconfiguration.UpdateSetupConfiguration;
import com.iweb2b.api.master.service.SetupConfigurationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "SetupConfiguration" }, value = "SetupConfiguration Operations related to SetupConfigurationController") 
@SwaggerDefinition(tags = { @Tag(name = "SetupConfiguration", description = "Operations related to SetupConfiguration") })
@RequestMapping("/setupConfiguration")
@RestController
public class SetupConfigurationController {

	@Autowired
	SetupConfigurationService setupConfigurationService;

	@ApiOperation(response = SetupConfiguration.class, value = "Get all SetupConfiguration details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<SetupConfiguration> setupConfigurationList = setupConfigurationService.getSetupConfiguration();
		return new ResponseEntity<>(setupConfigurationList, HttpStatus.OK);
	}

	@ApiOperation(response = SetupConfiguration.class, value = "Get a SetupConfiguration") // label for swagger
	@GetMapping("/{setupConfigurationId}")
	public ResponseEntity<?> getSetupConfiguration(@PathVariable String setupConfigurationId) {
		SetupConfiguration dbSetupConfiguration = setupConfigurationService.getSetupConfiguration(setupConfigurationId);
		log.info("SetupConfiguration : " + dbSetupConfiguration);
		return new ResponseEntity<>(dbSetupConfiguration, HttpStatus.OK);
	}

	@ApiOperation(response = SetupConfiguration.class, value = "Create SetupConfiguration") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postSetupConfiguration(@Valid @RequestBody AddSetupConfiguration newSetupConfiguration,
			@RequestParam String loginUserID) throws Exception {
		SetupConfiguration createdSetupConfiguration = setupConfigurationService.createSetupConfiguration(newSetupConfiguration, loginUserID);
		return new ResponseEntity<>(createdSetupConfiguration, HttpStatus.OK);
	}

	@ApiOperation(response = SetupConfiguration.class, value = "Update SetupConfiguration") // label for swagger
	@PatchMapping("/{setupConfiguration}")
	public ResponseEntity<?> patchSetupConfiguration(@PathVariable String setupConfiguration,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateSetupConfiguration updateSetupConfiguration)
			throws IllegalAccessException, InvocationTargetException {
		SetupConfiguration updatedSetupConfiguration = setupConfigurationService.updateSetupConfiguration(setupConfiguration, loginUserID,
				updateSetupConfiguration);
		return new ResponseEntity<>(updatedSetupConfiguration, HttpStatus.OK);
	}

	@ApiOperation(response = SetupConfiguration.class, value = "Delete SetupConfiguration") // label for swagger
	@DeleteMapping("/{setupConfiguration}")
	public ResponseEntity<?> deleteSetupConfiguration(@PathVariable String setupConfiguration, @RequestParam String loginUserID) {
		setupConfigurationService.deleteSetupConfiguration(setupConfiguration, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
