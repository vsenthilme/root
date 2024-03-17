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

import com.mnrclara.api.setup.model.userrole.AddUserRole;
import com.mnrclara.api.setup.model.userrole.UpdateUserRole;
import com.mnrclara.api.setup.model.userrole.UserRole;
import com.mnrclara.api.setup.service.UserRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"UserRole"}, value = "UserRole Operations related to UserRoleController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "UserRole",description = "Operations related to UserRole")})
@RequestMapping("/userRole")
@RestController
public class UserRoleController {
	
	@Autowired
	UserRoleService userRoleService;
	
    @ApiOperation(response = UserRole.class, value = "Get all UserRole details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<UserRole> userRoleList = userRoleService.getUserRoles();
		return new ResponseEntity<>(userRoleList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole.class, value = "Get a UserRole") // label for swagger 
	@GetMapping("/{userRoleId}")
	public ResponseEntity<?> getUserRole(@PathVariable Long userRoleId) {
    	List<UserRole> userRoles = userRoleService.getUserRole(userRoleId);
    	log.info("UserRoles : " + userRoles);
		return new ResponseEntity<>(userRoles, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole.class, value = "Create UserRole") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postUserRole(@Valid @RequestBody List<AddUserRole> newUserRole, 
			@RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<UserRole> createdUserRole = userRoleService.createUserRole(newUserRole, loginUserID);
		return new ResponseEntity<>(createdUserRole , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole.class, value = "Update UserRole") // label for swagger
    @PatchMapping("/{userRoleId}")
	public ResponseEntity<?> patchUserRole(@PathVariable Long userRoleId, @RequestParam String loginUserID,
			@Valid @RequestBody List<UpdateUserRole> updateUserRole) 
			throws IllegalAccessException, InvocationTargetException {
		List<UserRole> updatedUserRole = userRoleService.updateUserRole(userRoleId, loginUserID, updateUserRole);
		return new ResponseEntity<>(updatedUserRole , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole.class, value = "Delete UserRole") // label for swagger
	@DeleteMapping("/{userRoleId}")
	public ResponseEntity<?> deleteUserRole(@PathVariable Long userRoleId, @RequestParam String loginUserID) {
    	userRoleService.deleteUserRole(userRoleId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}