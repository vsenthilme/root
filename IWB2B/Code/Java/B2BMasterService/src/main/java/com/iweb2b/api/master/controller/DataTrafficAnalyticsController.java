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

import com.iweb2b.api.master.model.analytics.AddDataTrafficAnalytics;
import com.iweb2b.api.master.model.analytics.DataTrafficAnalytics;
import com.iweb2b.api.master.model.analytics.UpdateDataTrafficAnalytics;
import com.iweb2b.api.master.service.DataTrafficAnalyticsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "DataTrafficAnalytics" }, value = "DataTrafficAnalytics Operations related to DataTrafficAnalyticsController")
@SwaggerDefinition(tags = { @Tag(name = "DataTrafficAnalytics", description = "Operations related to DataTrafficAnalytics") })
@RequestMapping("/analytics")
@RestController
public class DataTrafficAnalyticsController {

	@Autowired
	DataTrafficAnalyticsService dataTrafficAnalyticsService;

	@ApiOperation(response = DataTrafficAnalytics.class, value = "Get all DataTrafficAnalytics details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<DataTrafficAnalytics> dataTrafficAnalyticsList = dataTrafficAnalyticsService.getDataTrafficAnalytics();
		return new ResponseEntity<>(dataTrafficAnalyticsList, HttpStatus.OK);
	}

	@ApiOperation(response = DataTrafficAnalytics.class, value = "Get a DataTrafficAnalytics") // label for swagger
	@GetMapping("/{dataTrafficAnalyticsId}")
	public ResponseEntity<?> getDataTrafficAnalytics(@PathVariable String dataTrafficAnalyticsId) {
		DataTrafficAnalytics dbDataTrafficAnalytics = dataTrafficAnalyticsService.getDataTrafficAnalytics(dataTrafficAnalyticsId);
		log.info("DataTrafficAnalytics : " + dbDataTrafficAnalytics);
		return new ResponseEntity<>(dbDataTrafficAnalytics, HttpStatus.OK);
	}

	@ApiOperation(response = DataTrafficAnalytics.class, value = "Create DataTrafficAnalytics") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postDataTrafficAnalytics(@Valid @RequestBody AddDataTrafficAnalytics newDataTrafficAnalytics,
			@RequestParam String loginUserID) throws Exception {
		DataTrafficAnalytics createdDataTrafficAnalytics = 
				dataTrafficAnalyticsService.createDataTrafficAnalytics(newDataTrafficAnalytics, loginUserID);
		return new ResponseEntity<>(createdDataTrafficAnalytics, HttpStatus.OK);
	}

	@ApiOperation(response = DataTrafficAnalytics.class, value = "Update DataTrafficAnalytics") // label for swagger
	@PatchMapping("/{dataTrafficAnalytics}")
	public ResponseEntity<?> patchDataTrafficAnalytics(@PathVariable String dataTrafficAnalytics,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateDataTrafficAnalytics updateDataTrafficAnalytics)
			throws IllegalAccessException, InvocationTargetException {
		DataTrafficAnalytics updatedDataTrafficAnalytics = dataTrafficAnalyticsService.updateDataTrafficAnalytics(dataTrafficAnalytics, loginUserID,
				updateDataTrafficAnalytics);
		return new ResponseEntity<>(updatedDataTrafficAnalytics, HttpStatus.OK);
	}

	@ApiOperation(response = DataTrafficAnalytics.class, value = "Delete DataTrafficAnalytics") // label for swagger
	@DeleteMapping("/{dataTrafficAnalytics}")
	public ResponseEntity<?> deleteDataTrafficAnalytics(@PathVariable String dataTrafficAnalytics, @RequestParam String loginUserID) {
		dataTrafficAnalyticsService.deleteDataTrafficAnalytics(dataTrafficAnalytics, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
