package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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

import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.SearchCaseSheetParams;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.service.CaseInfoSheetService;
import com.mnrclara.api.management.service.LeCaseInfoSheetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "LeCaseInfoSheet" }, value = "LeCaseInfoSheet Operations related to LeCaseInfoSheetController")
@SwaggerDefinition(tags = { @Tag(name = "LeCaseInfoSheet", description = "Operations related to LeCaseInfoSheet") })
@RequestMapping("/leCaseInfoSheet")
@RestController
public class LeCaseInfoSheetController {

	@Autowired
	LeCaseInfoSheetService leCaseInfoSheetService;

	@Autowired
	CaseInfoSheetService caseInfoSheetService;

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get all LeCaseInfoSheet details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<LeCaseInfoSheet> leCaseInfoSheetList = leCaseInfoSheetService.getLeCaseInfoSheets();
		return new ResponseEntity<>(leCaseInfoSheetList, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get a LeCaseInfoSheet") // label for swagger
	@GetMapping("/{caseInformationID}")
	public ResponseEntity<?> getLeCaseInfoSheetById(@PathVariable String caseInformationID) {
		LeCaseInfoSheet leCaseInfoSheet = leCaseInfoSheetService.getLeCaseInfoSheet(caseInformationID);
		log.info("LeCaseInfoSheet : " + leCaseInfoSheet);
		return new ResponseEntity<>(leCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get a LeCaseInfoSheet") // label for swagger
	@PostMapping("/search")
	public ResponseEntity<?> findByMultipleParams(@RequestBody SearchCaseSheetParams searchCaseSheetParams) {
		List<LeCaseInfoSheet> LeCaseInfoSheets = leCaseInfoSheetService.findByQuery (searchCaseSheetParams);
		return new ResponseEntity<>(LeCaseInfoSheets, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Create LeCaseInfoSheet") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postLeCaseInfoSheet(@Valid @RequestBody LeCaseInfoSheet newLeCaseInfoSheet,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		LeCaseInfoSheet createdLeCaseInfoSheet = leCaseInfoSheetService.createLeCaseInfoSheet(newLeCaseInfoSheet,
				loginUserID);
		return new ResponseEntity<>(createdLeCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Create Matter") // label for swagger
	@GetMapping("/{caseInformationID}/matter")
	public ResponseEntity<?> postMatter(@PathVariable String caseInformationID, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterGenAcc createdMatterGenAcc = caseInfoSheetService.createMatter(caseInformationID, loginUserID, true);
		return new ResponseEntity<>(createdMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Update LeCaseInfoSheet") // label for swagger
	@PatchMapping("/{caseInformationID}")
	public ResponseEntity<?> patchLeCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestBody LeCaseInfoSheet modifiedLeCaseInfoSheet, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		LeCaseInfoSheet updatedLeCaseInfoSheet = leCaseInfoSheetService.updateLeCaseInfoSheet(caseInformationID,
				modifiedLeCaseInfoSheet, loginUserID);
		return new ResponseEntity<>(updatedLeCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Delete LeCaseInfoSheet") // label for swagger
	@DeleteMapping("/{caseInformationID}")
	public ResponseEntity<?> deleteLeCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestParam String loginUserID) throws Exception {
		leCaseInfoSheetService.deleteLeCaseInfoSheet(caseInformationID, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}