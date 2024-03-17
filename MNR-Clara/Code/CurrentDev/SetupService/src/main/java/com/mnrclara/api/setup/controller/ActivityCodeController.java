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

import com.mnrclara.api.setup.model.activitycode.ActivityCode;
import com.mnrclara.api.setup.model.activitycode.AddActivityCode;
import com.mnrclara.api.setup.model.activitycode.UpdateActivityCode;
import com.mnrclara.api.setup.service.ActivityCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ActivityCode" }, value = "ActivityCode Operations related to ActivityCodeController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "ActivityCode", description = "Operations related to ActivityCode") })
@RequestMapping("/activityCode")
@RestController
public class ActivityCodeController {

	@Autowired
	ActivityCodeService activityCodeService;

	@ApiOperation(response = ActivityCode.class, value = "Get all ActivityCode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ActivityCode> activityCodeList = activityCodeService.getActivityCodes();
		return new ResponseEntity<>(activityCodeList, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Get a ActivityCode") // label for swagger
	@GetMapping("/{activityCodeId}")
	public ResponseEntity<?> getActivityCode(@PathVariable String activityCodeId) {
		ActivityCode dbActivityCode = activityCodeService.getActivityCode(activityCodeId);
		log.info("ActivityCode : " + dbActivityCode);
		return new ResponseEntity<>(dbActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Create ActivityCode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postActivityCode(@Valid @RequestBody AddActivityCode newActivityCode,
			@RequestParam String loginUserID) throws Exception {
		ActivityCode createdActivityCode = activityCodeService.createActivityCode(newActivityCode, loginUserID);
		return new ResponseEntity<>(createdActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Update ActivityCode") // label for swagger
	@PatchMapping("/{activityCode}")
	public ResponseEntity<?> patchActivityCode(@PathVariable String activityCode,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateActivityCode updateActivityCode)
			throws IllegalAccessException, InvocationTargetException {
		ActivityCode updatedActivityCode = activityCodeService.updateActivityCode(activityCode, loginUserID,
				updateActivityCode);
		return new ResponseEntity<>(updatedActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Delete ActivityCode") // label for swagger
	@DeleteMapping("/{activityCode}")
	public ResponseEntity<?> deleteActivityCode(@PathVariable String activityCode, @RequestParam String loginUserID) {
		activityCodeService.deleteActivityCode(activityCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}