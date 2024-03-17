package com.mnrclara.api.management.controller;

import com.mnrclara.api.management.model.matterdoclist.*;
import com.mnrclara.api.management.service.MatterDocListHeaderService;
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
@Api(tags = {"MatterDocListHeader"}, value = "MatterDocListHeader  Operations related to MatterDocListHeaderController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterDocListHeader ",description = "Operations related to MatterDocListHeader ")})
@RequestMapping("/matterdoclistheader")
@RestController
public class MatterDocListHeaderController {
	
	@Autowired
	MatterDocListHeaderService matterDocListHeaderService;
	
    @ApiOperation(response = MatterDocListHeader.class, value = "Get all MatterDocListHeader details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterDocListHeader> matterdoclistheaderList = matterDocListHeaderService.getMatterDocListHeaders();
		return new ResponseEntity<>(matterdoclistheaderList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocListHeader.class, value = "Get a MatterDocListHeader") // label for swagger
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterDocListHeader(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId) {
    	MatterDocListHeader matterdoclistheader = matterDocListHeaderService.getMatterDocListHeader(languageId, classId, checkListNo, matterNumber, clientId);
    	log.info("MatterDocListHeader : " + matterdoclistheader);
		return new ResponseEntity<>(matterdoclistheader, HttpStatus.OK);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Get a MatterDocListHeader-New") // label for swagger
	@GetMapping("/new/{matterHeaderId}")
	public ResponseEntity<?> getMatterDocListHeader(@PathVariable Long matterHeaderId) {
		MatterDocListHeader matterdoclistheader = matterDocListHeaderService.getMatterDocListHeader(matterHeaderId);
		return new ResponseEntity<>(matterdoclistheader, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocListHeader.class, value = "Create MatterDocListHeader") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterDocListHeader(@Valid @RequestBody AddMatterDocListHeader newMatterDocListHeader, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader = matterDocListHeaderService.createMatterDocListHeader(newMatterDocListHeader, loginUserID);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocListHeader.class, value = "Update MatterDocListHeader") // label for swagger
    @PatchMapping("/{matterNumber}")
	public ResponseEntity<?> patchMatterDocListHeader(@Valid @RequestBody UpdateMatterDocListHeader updateMatterDocListHeader, @PathVariable String matterNumber, @RequestParam String clientId,
													  @RequestParam Long checkListNo, @RequestParam String languageId, @RequestParam Long classId, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader = 
				matterDocListHeaderService.updateMatterDocListHeader(updateMatterDocListHeader,languageId, classId, checkListNo, matterNumber, clientId, loginUserID);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Update MatterDocListHeader-New") // label for swagger
	@PatchMapping("/new/{matterHeaderId}")
	public ResponseEntity<?> patchMatterDocListHeader(@Valid @RequestBody UpdateMatterDocListHeader updateMatterDocListHeader,
													  @PathVariable Long matterHeaderId,
													  @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader =
				matterDocListHeaderService.updateMatterDocListHeader(updateMatterDocListHeader, matterHeaderId, loginUserID);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocListHeader.class, value = "Delete MatterDocListHeader") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterDocListHeader(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId, @RequestParam String loginUserID) {
    	matterDocListHeaderService.deleteMatterDocListHeader(languageId, classId, checkListNo, matterNumber, clientId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Delete MatterDocListHeader-New") // label for swagger
	@DeleteMapping("/new/{matterHeaderId}")
	public ResponseEntity<?> deleteMatterDocListHeader(@PathVariable Long matterHeaderId, @RequestParam String loginUserID) {
		matterDocListHeaderService.deleteMatterDocListHeader( matterHeaderId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//Search
	@ApiOperation(response = MatterDocListHeader.class, value = "Search MatterDocListHeader") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findMatterDocListHeader(@Valid @RequestBody FindMatterDocListHeader findMatterDocListHeader) throws Exception {
		List<MatterDocListHeader> createdMatterDocListHeader = matterDocListHeaderService.findMatterDocListHeader(findMatterDocListHeader);
		return new ResponseEntity<>(createdMatterDocListHeader, HttpStatus.OK);
	}
}