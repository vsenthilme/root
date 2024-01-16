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

import com.iweb2b.api.master.model.monitoring.AddSystemMonitoringDeck;
import com.iweb2b.api.master.model.monitoring.SystemMonitoringDeck;
import com.iweb2b.api.master.model.monitoring.UpdateSystemMonitoringDeck;
import com.iweb2b.api.master.service.SystemMonitoringDeckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "SystemMonitoringDeck" }, value = "SystemMonitoringDeck Operations related to SystemMonitoringDeckController")
@SwaggerDefinition(tags = { @Tag(name = "SystemMonitoringDeck", description = "Operations related to SystemMonitoringDeck") })
@RequestMapping("/systemMonitoringDeck")
@RestController
public class SystemMonitoringDeckController {

	@Autowired
	SystemMonitoringDeckService systemMonitoringDeckService;

	@ApiOperation(response = SystemMonitoringDeck.class, value = "Get all SystemMonitoringDeck details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<SystemMonitoringDeck> systemMonitoringDeckList = systemMonitoringDeckService.getSystemMonitoringDeck();
		return new ResponseEntity<>(systemMonitoringDeckList, HttpStatus.OK);
	}

	@ApiOperation(response = SystemMonitoringDeck.class, value = "Get a SystemMonitoringDeck") // label for swagger
	@GetMapping("/{systemMonitoringDeckId}")
	public ResponseEntity<?> getSystemMonitoringDeck(@PathVariable String systemMonitoringDeckId) {
		SystemMonitoringDeck dbSystemMonitoringDeck = systemMonitoringDeckService.getSystemMonitoringDeck(systemMonitoringDeckId);
		log.info("SystemMonitoringDeck : " + dbSystemMonitoringDeck);
		return new ResponseEntity<>(dbSystemMonitoringDeck, HttpStatus.OK);
	}

	@ApiOperation(response = SystemMonitoringDeck.class, value = "Create SystemMonitoringDeck") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postSystemMonitoringDeck(@Valid @RequestBody AddSystemMonitoringDeck newSystemMonitoringDeck,
			@RequestParam String loginUserID) throws Exception {
		SystemMonitoringDeck createdSystemMonitoringDeck = systemMonitoringDeckService.createSystemMonitoringDeck(newSystemMonitoringDeck, loginUserID);
		return new ResponseEntity<>(createdSystemMonitoringDeck, HttpStatus.OK);
	}

	@ApiOperation(response = SystemMonitoringDeck.class, value = "Update SystemMonitoringDeck") // label for swagger
	@PatchMapping("/{systemMonitoringDeck}")
	public ResponseEntity<?> patchSystemMonitoringDeck(@PathVariable String systemMonitoringDeck,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateSystemMonitoringDeck updateSystemMonitoringDeck)
			throws IllegalAccessException, InvocationTargetException {
		SystemMonitoringDeck updatedSystemMonitoringDeck = systemMonitoringDeckService.updateSystemMonitoringDeck(systemMonitoringDeck, loginUserID,
				updateSystemMonitoringDeck);
		return new ResponseEntity<>(updatedSystemMonitoringDeck, HttpStatus.OK);
	}

	@ApiOperation(response = SystemMonitoringDeck.class, value = "Delete SystemMonitoringDeck") // label for swagger
	@DeleteMapping("/{systemMonitoringDeck}")
	public ResponseEntity<?> deleteSystemMonitoringDeck(@PathVariable String systemMonitoringDeck, @RequestParam String loginUserID) {
		systemMonitoringDeckService.deleteSystemMonitoringDeck(systemMonitoringDeck, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
