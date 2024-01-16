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

import com.mnrclara.api.management.model.caseinfosheet.ImmCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.SearchCaseSheetParams;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.service.CaseInfoSheetService;
import com.mnrclara.api.management.service.ImmCaseInfoSheetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ImmCaseInfoSheet" }, value = "ImmCaseInfoSheet Operations related to ImmCaseInfoSheetController")
@SwaggerDefinition(tags = { @Tag(name = "ImmCaseInfoSheet", description = "Operations related to ImmCaseInfoSheet") })
@RequestMapping("/immCaseInfoSheet")
@RestController
public class ImmCaseInfoSheetController {

	@Autowired
	ImmCaseInfoSheetService immCaseInfoSheetService;

	@Autowired
	CaseInfoSheetService caseInfoSheetService;

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get all ImmCaseInfoSheet details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ImmCaseInfoSheet> immCaseInfoSheetList = immCaseInfoSheetService.getImmCaseInfoSheets();
		return new ResponseEntity<>(immCaseInfoSheetList, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get a ImmCaseInfoSheet") // label for swagger
	@GetMapping("/{caseInformationID}")
	public ResponseEntity<?> getImmCaseInfoSheetById(@PathVariable String caseInformationID) {
		ImmCaseInfoSheet immCaseInfoSheet = immCaseInfoSheetService.getImmCaseInfoSheet(caseInformationID);
		log.info("ImmCaseInfoSheet : " + immCaseInfoSheet);
		return new ResponseEntity<>(immCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Create ImmCaseInfoSheet") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postImmCaseInfoSheet(@Valid @RequestBody ImmCaseInfoSheet newImmCaseInfoSheet,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ImmCaseInfoSheet createdImmCaseInfoSheet = immCaseInfoSheetService.createImmCaseInfoSheet(newImmCaseInfoSheet,
				loginUserID);
		return new ResponseEntity<>(createdImmCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get a ImmCaseInfoSheet") // label for swagger
	@PostMapping("/search")
	public ResponseEntity<?> findByMultipleParams(@RequestBody SearchCaseSheetParams searchCaseSheetParams) {
		List<ImmCaseInfoSheet> ImmCaseInfoSheets = immCaseInfoSheetService.findByQuery(searchCaseSheetParams);
		return new ResponseEntity<>(ImmCaseInfoSheets, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Create ImmCaseInfoSheet") // label for swagger
	@GetMapping("/{caseInformationID}/matter")
	public ResponseEntity<?> postMatter(@PathVariable String caseInformationID, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, Exception {
		MatterGenAcc createdMatterGenAcc = caseInfoSheetService.createMatter(caseInformationID, loginUserID, false);
		return new ResponseEntity<>(createdMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Update ImmCaseInfoSheet") // label for swagger
	@PatchMapping("/{caseInformationID}")
	public ResponseEntity<?> patchImmCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestBody ImmCaseInfoSheet modifiedImmCaseInfoSheet, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ImmCaseInfoSheet updatedImmCaseInfoSheet = immCaseInfoSheetService.updateImmCaseInfoSheet(caseInformationID,
				modifiedImmCaseInfoSheet, loginUserID);
		return new ResponseEntity<>(updatedImmCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Delete ImmCaseInfoSheet") // label for swagger
	@DeleteMapping("/{caseInformationID}")
	public ResponseEntity<?> deleteImmCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		immCaseInfoSheetService.deleteImmCaseInfoSheet(caseInformationID, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}