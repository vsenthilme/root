package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.matterdoclist.AddMatterDocList;
import com.mnrclara.api.management.model.matterdoclist.MatterDocList;
import com.mnrclara.api.management.model.matterdoclist.MatterDocListHeader;
import com.mnrclara.api.management.model.matterdoclist.SearchMatterDocList;
import com.mnrclara.api.management.service.MatterDocListService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterDocList"}, value = "MatterDocList  Operations related to MatterDocListController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterDocList ",description = "Operations related to MatterDocList ")})
@RequestMapping("/matterdoclist")
@RestController
public class MatterDocListController {
	
	@Autowired
	MatterDocListService matterDocListService;
	
    @ApiOperation(response = MatterDocList.class, value = "Get all MatterDocList details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterDocList> matterdoclistList = matterDocListService.getMatterDocLists();
		return new ResponseEntity<>(matterdoclistList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Get a MatterDocList") // label for swagger 
	@GetMapping("/{matterNumber}")
	public ResponseEntity<?> getMatterDocList(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId) {
    	MatterDocList matterdoclist = matterDocListService.getMatterDocList(languageId, classId, checkListNo, matterNumber, clientId);
    	log.info("MatterDocList : " + matterdoclist);
		return new ResponseEntity<>(matterdoclist, HttpStatus.OK);
	}
    
	@ApiOperation(response = MatterDocList.class, value = "Search MatterDocList") // label for swagger
	@PostMapping("/findMatterDocList")
	public List<MatterDocList> findMatterDocList(@RequestBody SearchMatterDocList searchMatterDocList)
			throws Exception {
		return matterDocListService.findMatterDocList(searchMatterDocList);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Create MatterDocList") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterDocList(@Valid @RequestBody List<AddMatterDocList> newMatterDocList, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<MatterDocList> createdMatterDocList = matterDocListService.createMatterDocList(newMatterDocList, loginUserID);
		return new ResponseEntity<>(createdMatterDocList , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Update MatterDocList") // label for swagger
    @GetMapping("/{matterNumber}/clientPortal/docCheckList")
	public ResponseEntity<?> patchMatterDocList(@PathVariable String matterNumber, @RequestParam String clientId, 
			@RequestParam Long checkListNo, @RequestParam Long matterHeaderId, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader = 
				matterDocListService.updateMatterDocList(clientId, matterNumber, checkListNo, matterHeaderId, loginUserID);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Delete MatterDocList") // label for swagger
	@DeleteMapping("/{matterNumber}")
	public ResponseEntity<?> deleteMatterDocList(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId, @RequestParam String loginUserID) {
    	matterDocListService.deleteMatterDocList(languageId, classId, checkListNo, matterNumber, clientId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}