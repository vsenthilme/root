package com.flourish.b2b.api.simulator.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flourish.b2b.api.simulator.model.ConsignmentOutbound;
import com.flourish.b2b.api.simulator.model.NewConsignmentOutbound;
import com.flourish.b2b.api.simulator.model.NewPickup;
import com.flourish.b2b.api.simulator.model.Pickup;
import com.flourish.b2b.api.simulator.model.user.AddUser;
import com.flourish.b2b.api.simulator.model.user.ModifyUser;
import com.flourish.b2b.api.simulator.model.user.User;
import com.flourish.b2b.api.simulator.service.SimulatorService;
import com.flourish.b2b.api.simulator.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"Service Provider Simulator"}, value = "Operations related to ServiceProvider Simulator Controller") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to User")})
@RequestMapping("/v1/simulator")
@RestController
public class SimulatorController {
	
	@Autowired
	SimulatorService simulatorService;
	
    @ApiOperation(response = Pickup.class, value = "Get all Consignments")
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Pickup> pickupList = simulatorService.getConsignments();
		return new ResponseEntity<>(pickupList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Pickup.class, value = "Get Consignment")
	@GetMapping("/{id}")
	public ResponseEntity<?> getConsignment(@PathVariable String id) {
    	Pickup pickup = simulatorService.getConsignmentByShipmentOrderNo(id);
    	log.info("Pickup : " + pickup);
		return new ResponseEntity<>(pickup, HttpStatus.OK);
	}
    
    @ApiOperation(response = Pickup.class, value = "Create Consignment")
	@PostMapping("")
	public ResponseEntity<?> postConsignment(@Valid @RequestBody NewPickup newPickup) 
			throws IllegalAccessException, InvocationTargetException {
    	Pickup createdPickup = simulatorService.createConsignment(newPickup);
		return new ResponseEntity<>(createdPickup , HttpStatus.OK);
	}
    
    @ApiOperation(response = ConsignmentOutbound.class, value = "Create Consignment Outbound")
	@PostMapping("/consignment-outbound")
	public ResponseEntity<?> postConsignmentOutbound(@Valid @RequestBody NewConsignmentOutbound newConsignmentOutbound) 
			throws IllegalAccessException, InvocationTargetException {
    	ConsignmentOutbound createdConsignmentOutbound = simulatorService.createConsignmentOutbound(newConsignmentOutbound);
    	
    	String message = "";
    	if (createdConsignmentOutbound != null) {
    		message += "Post consignment outbound received successfully.";
    	}
    	
    	return new ResponseEntity<>(message , HttpStatus.OK);
	}
    
    @ApiOperation(response = Pickup.class, value = "Get all Consignment outbounds")
	@GetMapping("/consignment-outbound")
	public ResponseEntity<?> getAllConsignmentOutbounds() {
		List<ConsignmentOutbound> consignmentOutboundList = simulatorService.getConsignmentOutbounds();
		return new ResponseEntity<>(consignmentOutboundList, HttpStatus.OK);
	}
}