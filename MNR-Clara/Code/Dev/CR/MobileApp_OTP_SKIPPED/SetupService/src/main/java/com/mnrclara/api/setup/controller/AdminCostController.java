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

import com.mnrclara.api.setup.model.admincost.AddAdminCost;
import com.mnrclara.api.setup.model.admincost.AdminCost;
import com.mnrclara.api.setup.model.admincost.UpdateAdminCost;
import com.mnrclara.api.setup.service.AdminCostService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"AdminCost"}, value = "AdminCost Operations related to AdminCostController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "AdminCost",description = "Operations related to AdminCost")})
@RequestMapping("/adminCost")
@RestController
public class AdminCostController {
	
	@Autowired
	AdminCostService adminCostService;
	
    @ApiOperation(response = AdminCost.class, value = "Get all AdminCost details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<AdminCost> AdminCostList = adminCostService.getAdminCosts();
		return new ResponseEntity<>(AdminCostList, HttpStatus.OK);
	}
    
    @ApiOperation(response = AdminCost.class, value = "Get a AdminCost") // label for swagger 
	@GetMapping("/{adminCostId}")
	public ResponseEntity<?> getAdminCost(@PathVariable Long adminCostId) {
    	AdminCost adminCost = adminCostService.getAdminCost(adminCostId);
    	log.info("AdminCost : " + adminCost);
		return new ResponseEntity<>(adminCost, HttpStatus.OK);
	}
    
    @ApiOperation(response = AdminCost.class, value = "Create AdminCost") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addAdminCost(@Valid @RequestBody AddAdminCost newAdminCost, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		AdminCost createdAdminCost = adminCostService.createAdminCost(newAdminCost, loginUserID);
		return new ResponseEntity<>(createdAdminCost , HttpStatus.OK);
	}
    
    @ApiOperation(response = AdminCost.class, value = "Update AdminCost") // label for swagger
    @PatchMapping("/{adminCostId}")
	public ResponseEntity<?> patchAdminCost(@PathVariable Long adminCostId, @RequestParam String loginUserID, 
			@RequestBody UpdateAdminCost updateAdminCost) 
			throws IllegalAccessException, InvocationTargetException {
		AdminCost updatedAdminCost = adminCostService.updateAdminCost(adminCostId, loginUserID, updateAdminCost);
		return new ResponseEntity<>(updatedAdminCost , HttpStatus.OK);
	}
    
    @ApiOperation(response = AdminCost.class, value = "Delete AdminCost") // label for swagger
	@DeleteMapping("/{adminCostId}")
	public ResponseEntity<?> deleteAdminCost(@PathVariable Long adminCostId, @RequestParam String loginUserID) {
    	adminCostService.deleteAdminCost(adminCostId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}