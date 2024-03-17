package com.mnrclara.api.crm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.crm.model.dto.ConflictSearchResult;
import com.mnrclara.api.crm.service.ConflictCheckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Conflict Check"}, value = "Conflict Check Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to User")})
@RequestMapping("/conflictCheck")
public class ConflictCheckController {
	
	@Autowired
	ConflictCheckService conflictCheckService;
	
    @ApiOperation(response = Optional.class, value = "Search FreeText") // label for swagger
	@GetMapping("/search")
	public ResponseEntity<?> searchFreeText (@RequestParam List<String> searchTableNames, @RequestParam String searchFieldValue ) {
    	ConflictSearchResult searchResults = conflictCheckService.findRecords(searchTableNames, searchFieldValue);
    	return new ResponseEntity<>(searchResults, HttpStatus.OK);
	}
}