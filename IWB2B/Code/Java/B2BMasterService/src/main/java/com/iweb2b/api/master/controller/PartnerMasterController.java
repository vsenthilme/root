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

import com.iweb2b.api.master.model.partnermaster.AddPartnerMaster;
import com.iweb2b.api.master.model.partnermaster.PartnerMaster;
import com.iweb2b.api.master.model.partnermaster.UpdatePartnerMaster;
import com.iweb2b.api.master.service.PartnerMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "PartnerMaster" }, value = "PartnerMaster Operations related to PartnerMasterController") 
@SwaggerDefinition(tags = { @Tag(name = "PartnerMaster", description = "Operations related to PartnerMaster") })
@RequestMapping("/partnerMaster")
@RestController
public class PartnerMasterController {

	@Autowired
	PartnerMasterService partnerMasterService;

	@ApiOperation(response = PartnerMaster.class, value = "Get all PartnerMaster details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PartnerMaster> partnerMasterList = partnerMasterService.getPartnerMaster();
		return new ResponseEntity<>(partnerMasterList, HttpStatus.OK);
	}

	@ApiOperation(response = PartnerMaster.class, value = "Get a PartnerMaster") // label for swagger
	@GetMapping("/{partnerMasterId}")
	public ResponseEntity<?> getPartnerMaster(@PathVariable String partnerMasterId) {
		PartnerMaster dbPartnerMaster = partnerMasterService.getPartnerMaster(partnerMasterId);
		log.info("PartnerMaster : " + dbPartnerMaster);
		return new ResponseEntity<>(dbPartnerMaster, HttpStatus.OK);
	}

	@ApiOperation(response = PartnerMaster.class, value = "Create PartnerMaster") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postPartnerMaster(@Valid @RequestBody AddPartnerMaster newPartnerMaster,
			@RequestParam String loginUserID) throws Exception {
		PartnerMaster createdPartnerMaster = partnerMasterService.createPartnerMaster(newPartnerMaster, loginUserID);
		return new ResponseEntity<>(createdPartnerMaster, HttpStatus.OK);
	}

	@ApiOperation(response = PartnerMaster.class, value = "Update PartnerMaster") // label for swagger
	@PatchMapping("/{partnerMaster}")
	public ResponseEntity<?> patchPartnerMaster(@PathVariable String partnerMaster,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdatePartnerMaster updatePartnerMaster)
			throws IllegalAccessException, InvocationTargetException {
		PartnerMaster updatedPartnerMaster = partnerMasterService.updatePartnerMaster(partnerMaster, loginUserID,
				updatePartnerMaster);
		return new ResponseEntity<>(updatedPartnerMaster, HttpStatus.OK);
	}

	@ApiOperation(response = PartnerMaster.class, value = "Delete PartnerMaster") // label for swagger
	@DeleteMapping("/{partnerMaster}")
	public ResponseEntity<?> deletePartnerMaster(@PathVariable String partnerMaster, @RequestParam String loginUserID) {
		partnerMasterService.deletePartnerMaster(partnerMaster, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
