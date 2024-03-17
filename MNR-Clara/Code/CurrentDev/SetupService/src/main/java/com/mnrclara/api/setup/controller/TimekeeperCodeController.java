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

import com.mnrclara.api.setup.model.timekeepercode.AddTimekeeperCode;
import com.mnrclara.api.setup.model.timekeepercode.TimekeeperCode;
import com.mnrclara.api.setup.model.timekeepercode.UpdateTimekeeperCode;
import com.mnrclara.api.setup.service.TimekeeperCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"TimekeeperCode"}, value = "TimekeeperCode Operations related to TimekeeperCodeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "TimekeeperCode",description = "Operations related to TimekeeperCode")})
@RequestMapping("/timekeeperCode")
@RestController
public class TimekeeperCodeController {
	
	@Autowired
	TimekeeperCodeService timekeeperCodeService;
	
    @ApiOperation(response = TimekeeperCode.class, value = "Get all TimekeeperCode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<TimekeeperCode> timekeeperCodeList = timekeeperCodeService.getActivityCodes();
		return new ResponseEntity<>(timekeeperCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Get a TimekeeperCode") // label for swagger 
	@GetMapping("/{timeKeeperCode}")
	public ResponseEntity<?> getTimekeeperCode(@PathVariable String timeKeeperCode) {
    	TimekeeperCode dbTimekeeperCode = timekeeperCodeService.getTimekeeperCode(timeKeeperCode);
    	log.info("TimekeeperCode : " + dbTimekeeperCode);
		return new ResponseEntity<>(dbTimekeeperCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Create TimekeeperCode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addTimekeeperCode(@Valid @RequestBody AddTimekeeperCode newTimekeeperCode, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode createdTimekeeperCode = timekeeperCodeService.createTimekeeperCode(newTimekeeperCode, loginUserID);
		return new ResponseEntity<>(createdTimekeeperCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Update TimekeeperCode") // label for swagger
    @PatchMapping("/{timeKeeperCode}")
	public ResponseEntity<?> patchTimekeeperCode(@PathVariable String timeKeeperCode, @RequestParam String loginUserID,
			@Valid @RequestBody TimekeeperCode updateTimekeeperCode)
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode updatedTimekeeperCode = timekeeperCodeService.updateTimekeeperCode(timeKeeperCode, loginUserID, updateTimekeeperCode);
		return new ResponseEntity<>(updatedTimekeeperCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Delete TimekeeperCode") // label for swagger
	@DeleteMapping("/{timeKeeperCode}")
	public ResponseEntity<?> deleteTimekeeperCode(@PathVariable String timeKeeperCode, @RequestParam String loginUserID) {
    	timekeeperCodeService.deleteTimekeeperCode(timeKeeperCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}