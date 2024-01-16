package com.mnrclara.api.management.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.dto.Dashboard;
import com.mnrclara.api.management.service.DashboardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"Dashboard"}, value = "DashboardController Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Dashboard")})
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
    /*
     * Dashboard - Billed Income
     */
    @ApiOperation(response = Optional.class, value = "Dashboard - AGREEMENT_RESENT") // label for swagger
	@GetMapping("/agreementResent")
	public ResponseEntity<?> agreementResentDashboard (@RequestParam Long classId, @RequestParam String period) {
		Dashboard dashboard = dashboardService.generateDashboard(classId, period);
    	return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}
}