package com.flourish.b2b.api.gateway.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.ErrorOrder;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.NewOrder;
import com.flourish.b2b.api.gateway.model.consignmentoutbound.Order;
import com.flourish.b2b.api.gateway.service.ConsignmentOutboundService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"Consignment Outbound"}, value = "Shipper Operations related to ConsignmentOutboundController")
@SwaggerDefinition(tags = {@Tag(name = "Consignment Outbound",description = "Operations related to ConsignmentOutbound")})
@RequestMapping("/v1/consignment-outbound")
@RestController
public class ConsignmentOutboundController {
	
	@Autowired
	ConsignmentOutboundService conOutboundService;
	
    @ApiOperation(response = Order.class, value = "Get all Consignments")
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Order> consignmentList = conOutboundService.getConsignments();
		return new ResponseEntity<>(consignmentList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = Order.class, value = "Get a Consignment")
   	@GetMapping("/{shipmentOrderNo}")
   	public ResponseEntity<?> getConsignment(@PathVariable String shipmentOrderNo) {
    	Order order = conOutboundService.getConsignmentByShipmentOrderNo(shipmentOrderNo);
       	log.info("order : " + order.getConsigneCode());
   		return new ResponseEntity<>(order, HttpStatus.OK);
   	}
    
    @ApiOperation(response = Order.class, value = "Post new Consignment")
	@PostMapping("")
	public ResponseEntity<?> postConsignment(@Valid @RequestBody NewOrder newOrder) 
			throws IllegalAccessException, InvocationTargetException, JsonProcessingException {
    	log.info("Consignment Controller is being called------- " + newOrder.getShipperCode());
		String createdOrderResponse = null;
		createdOrderResponse = conOutboundService.postConsignment(newOrder);
		return new ResponseEntity<>(createdOrderResponse, HttpStatus.OK);
	}
    
    @ApiOperation(response = Order.class, value = "Get all Consignments Error Logs")
	@GetMapping("/errorLogs")
	public ResponseEntity<?> getAllErrorLogs() {
		List<ErrorOrder> consignmentOBErrorLogsList = conOutboundService.getConsignmentOutboundErrorLogs();
		return new ResponseEntity<>(consignmentOBErrorLogsList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = Order.class, value = "Get a Consignment Error Log")
	@GetMapping("/errorLogs/{id}")
	public ResponseEntity<?> getErrorLog(@PathVariable Long id) {
		ErrorOrder consignmentOBErrorLog = conOutboundService.getConsignmentOutboundErrorLog(id);
		return new ResponseEntity<>(consignmentOBErrorLog, HttpStatus.OK); 
	}
    
    @PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile excelFile) {
		String message = "";
		try {
			List<NewOrder> newOrders = conOutboundService.readExcelFile(excelFile);
			for (NewOrder newOrder : newOrders) {
				log.info("Shipment Order No Code : " + newOrder.getShipmentOrderNo());
				log.info("--------------------------------------");
			}
			
			message = "Uploaded the file successfully: " + excelFile.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(newOrders);
		} catch (Exception e) {
			e.printStackTrace();
			message = "Could not upload the file: " + excelFile.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getLocalizedMessage());
		}
	}
}