package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.companyid.AddCompanyId;
import com.mnrclara.api.cg.setup.model.companyid.CompanyId;
import com.mnrclara.api.cg.setup.model.companyid.FindCompanyId;
import com.mnrclara.api.cg.setup.model.companyid.UpdateCompanyId;
import com.mnrclara.api.cg.setup.service.CompanyIdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {" CompanyId "}, value = " CompanyId  Operations related to CompanyIdController ") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = " CompanyId ",description = "Operations related to CompanyId ")})
@RequestMapping("/companyid")
@RestController
public class CompanyIdController {

	@Autowired
	CompanyIdService companyidService;

	@ApiOperation(response = CompanyId.class, value = "Get all CompanyId details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<CompanyId> companyidList = companyidService.getCompanyIds();
		return new ResponseEntity<>(companyidList, HttpStatus.OK);
	}

	@ApiOperation(response = CompanyId.class, value = "Get a CompanyId") // label for swagger
	@GetMapping("/{companyId}")
	public ResponseEntity<?> getCompanyId(@PathVariable String companyId, @RequestParam String languageId) {
		CompanyId companyid = companyidService.getCompanyId(companyId, languageId);
		log.info("CompanyId : " + companyid);
		return new ResponseEntity<>(companyid, HttpStatus.OK);
	}

	@ApiOperation(response = CompanyId.class, value = "Create CompanyId") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postCompanyId(@Valid @RequestBody AddCompanyId newCompanyId,
										   @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		CompanyId createdCompanyId = companyidService.createCompanyId(newCompanyId, loginUserID);
		return new ResponseEntity<>(createdCompanyId , HttpStatus.OK);
	}

	@ApiOperation(response = CompanyId.class, value = "Update CompanyId") // label for swagger
	@PatchMapping("/{companyId}")
	public ResponseEntity<?> patchCompanyId(@PathVariable String companyId, @RequestParam String languageId,
											@RequestParam String loginUserID,@Valid @RequestBody UpdateCompanyId updateCompanyId)
			throws IllegalAccessException, InvocationTargetException {

		CompanyId createdCompanyId =
				companyidService.updateCompanyId(companyId, languageId, loginUserID, updateCompanyId);
		return new ResponseEntity<>(createdCompanyId , HttpStatus.OK);
	}

	@ApiOperation(response = CompanyId.class, value = "Delete CompanyId") // label for swagger
	@DeleteMapping("/{companyId}")
	public ResponseEntity<?> deleteCompanyId(@PathVariable String companyId, @RequestParam String languageId,
											 @RequestParam String loginUserID) {
		companyidService.deleteCompanyId(companyId, languageId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//Search
	@ApiOperation(response = CompanyId.class, value = "Find CompanyId") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findCompanyId(@Valid @RequestBody FindCompanyId findCompanyId) throws Exception {
		List<CompanyId> createdCompanyId = companyidService.findCompanyId(findCompanyId);
		return new ResponseEntity<>(createdCompanyId, HttpStatus.OK);
	}
}