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

import com.iweb2b.api.master.model.monitoring.AddMonitoringControl;
import com.iweb2b.api.master.model.monitoring.MonitoringControl;
import com.iweb2b.api.master.model.monitoring.UpdateMonitoringControl;
import com.iweb2b.api.master.service.MonitoringControlService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MonitoringControl" }, value = "MonitoringControl Operations related to MonitoringControlController")
@SwaggerDefinition(tags = { @Tag(name = "MonitoringControl", description = "Operations related to MonitoringControl") })
@RequestMapping("/monitoringControl")
@RestController
public class MonitoringControlController {

	@Autowired
	MonitoringControlService monitoringControlService;

	@ApiOperation(response = MonitoringControl.class, value = "Get all MonitoringControl details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MonitoringControl> monitoringControlList = monitoringControlService.getMonitoringControl();
		return new ResponseEntity<>(monitoringControlList, HttpStatus.OK);
	}

	@ApiOperation(response = MonitoringControl.class, value = "Get a MonitoringControl") // label for swagger
	@GetMapping("/{monitoringControlId}")
	public ResponseEntity<?> getMonitoringControl(@PathVariable String monitoringControlId) {
		MonitoringControl dbMonitoringControl = monitoringControlService.getMonitoringControl(monitoringControlId);
		log.info("MonitoringControl : " + dbMonitoringControl);
		return new ResponseEntity<>(dbMonitoringControl, HttpStatus.OK);
	}

	@ApiOperation(response = MonitoringControl.class, value = "Create MonitoringControl") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMonitoringControl(@Valid @RequestBody AddMonitoringControl newMonitoringControl,
			@RequestParam String loginUserID) throws Exception {
		MonitoringControl createdMonitoringControl = monitoringControlService.createMonitoringControl(newMonitoringControl, loginUserID);
		return new ResponseEntity<>(createdMonitoringControl, HttpStatus.OK);
	}

	@ApiOperation(response = MonitoringControl.class, value = "Update MonitoringControl") // label for swagger
	@PatchMapping("/{monitoringControl}")
	public ResponseEntity<?> patchMonitoringControl(@PathVariable String monitoringControl,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateMonitoringControl updateMonitoringControl)
			throws IllegalAccessException, InvocationTargetException {
		MonitoringControl updatedMonitoringControl = monitoringControlService.updateMonitoringControl(monitoringControl, loginUserID,
				updateMonitoringControl);
		return new ResponseEntity<>(updatedMonitoringControl, HttpStatus.OK);
	}

	@ApiOperation(response = MonitoringControl.class, value = "Delete MonitoringControl") // label for swagger
	@DeleteMapping("/{monitoringControl}")
	public ResponseEntity<?> deleteMonitoringControl(@PathVariable String monitoringControl, @RequestParam String loginUserID) {
		monitoringControlService.deleteMonitoringControl(monitoringControl, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
