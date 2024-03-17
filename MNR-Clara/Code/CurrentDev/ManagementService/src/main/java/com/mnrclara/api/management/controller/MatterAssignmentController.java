package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.mnrclara.api.management.model.dto.MatterAssignmentImpl;
import com.mnrclara.api.management.model.matterassignment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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

import com.mnrclara.api.management.service.MatterAssignmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MatterAssignment" }, value = "MatterAssignment Operations related to MatterAssignmentController")
@SwaggerDefinition(tags = { @Tag(name = "MatterAssignment", description = "Operations related to MatterAssignment") })
@RequestMapping("/matterassignment")
@RestController
public class MatterAssignmentController {

	@Autowired
	MatterAssignmentService matterAssignmentService;

	@ApiOperation(response = MatterAssignment.class, value = "Get all MatterAssignment details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterAssignment> matterAssignmentList = matterAssignmentService.getMatterAssignments();
		return new ResponseEntity<>(matterAssignmentList, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterAssignment.class, value = "Get all MatterAssignment details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "matterNumber") String sortBy) {
		Page<MatterAssignment> list = matterAssignmentService.getAllMatterAssignments(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}

	@ApiOperation(response = MatterAssignment.class, value = "Get a MatterAssignment") // label for swagger
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterAssignment(@PathVariable String matterNumber) {
		MatterAssignment matterassignment = matterAssignmentService.getMatterAssignment(matterNumber);
		log.info("MatterAssignment : " + matterassignment);
		return new ResponseEntity<>(matterassignment, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterAssignment.class, value = "Search MatterAssignment") // label for swagger
	@PostMapping("/findMatterAssignment")
	public List<MatterAssignment> findMatterAssignment(@RequestBody SearchMatterAssignment searchMatterAssignment)
			throws ParseException {
		return matterAssignmentService.findMatterAssignment(searchMatterAssignment);
	}

	//Streaming
	@ApiOperation(response = MatterAssignment.class, value = "Search MatterAssignment Streaming") // label for swagger
	@PostMapping("/findMatterAssignmentStream")
	public List<MatterAssignmentImpl> findMatterAssignmentStream(@RequestBody SearchMatterAssignment searchMatterAssignment)
			throws ParseException {
		return matterAssignmentService.findMatterAssignmentStream(searchMatterAssignment);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Create MatterAssignment") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterAssignment(@Valid @RequestBody AddMatterAssignment newMatterAssignment,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		MatterAssignment createdMatterAssignment = matterAssignmentService.createMatterAssignment(newMatterAssignment,
				loginUserID);
		return new ResponseEntity<>(createdMatterAssignment, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterAssignment.class, value = "Create MatterAssignment") // label for swagger
	@PostMapping("/batch")
	public ResponseEntity<?> postBulkMatterAssignments(@RequestBody AddMatterAssignment[] newMatterAssignments,
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		matterAssignmentService.createBulkMatterAssignments(newMatterAssignments, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Update MatterAssignment") // label for swagger
	@PatchMapping("/{matterNumber}")
	public ResponseEntity<?> patchMatterAssignment(@PathVariable String matterNumber,
			@Valid @RequestBody UpdateMatterAssignment updateMatterAssignment, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterAssignment updatedMatterAssignment = matterAssignmentService.updateMatterAssignment(matterNumber,
				updateMatterAssignment, loginUserID);
		return new ResponseEntity<>(updatedMatterAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Delete MatterAssignment") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterAssignment(@PathVariable String matterNumber) {
		matterAssignmentService.deleteMatterAssignment(matterNumber);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}