package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.management.model.clientgeneral.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.dto.DashboardReport;
import com.mnrclara.api.management.model.dto.KeyValuePair;
import com.mnrclara.api.management.model.dto.docketwise.Contact;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.service.ClientGeneralService;
import com.mnrclara.api.management.service.CommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientGeneral" }, value = "ClientGeneral Operations related to ClientGeneralController")
@SwaggerDefinition(tags = { @Tag(name = "ClientGeneral", description = "Operations related to ClientGeneral") })
@RequestMapping("/clientgeneral")
@RestController
public class ClientGeneralController {

	@Autowired
	ClientGeneralService clientGeneralService;
	
	@Autowired
	CommonService commonService;

	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientGeneral> clientGeneralList = clientGeneralService.getClientGenerals();
		return new ResponseEntity<>(clientGeneralList, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/dropdown/client")
	public ResponseEntity<?> getAllClientList() {
		List<KeyValuePair> clientGeneralList = clientGeneralService.getAllClientList();
		return new ResponseEntity<>(clientGeneralList, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "CLIENT_ID") String sortBy,
									@RequestParam List<Long> classId) {
		Page<ClientGeneral> list = clientGeneralService.getAllClientGenerals(pageNo, pageSize, sortBy, classId);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/{clientgeneralId}")
	public ResponseEntity<?> getClientGeneral(@PathVariable String clientgeneralId) {
		ClientGeneral clientgeneral = clientGeneralService.getClientGeneral(clientgeneralId);
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/top")
	public ResponseEntity<?> getTopClientGeneral() {
		ClientGeneral clientgeneral = clientGeneralService.getLatestClientGeneral();
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/limit")
	public ResponseEntity<?> getClientGeneralByLimit() {
		ClientGeneral clientgeneral = clientGeneralService.getClientGeneralByLimit();
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard Total - ClientGeneral") // label for swagger
	@GetMapping("/dashboard/total")
	public ResponseEntity<?> getDashboardTotal(@RequestParam String loginUserID) {
		DashboardReport dashboardReport = clientGeneralService.getDashboardTotal(loginUserID);
		log.info("ClientGeneral Dashboard Total: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard Active - ClientGeneral") // label for swagger
	@GetMapping("/dashboard/active")
	public ResponseEntity<?> getDashboardActive(@RequestParam String loginUserID) {
		DashboardReport dashboardReport = clientGeneralService.getDashboardActive(loginUserID);
		log.info("ClientGeneral Dashboard Active: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard RecentClients - ClientGeneral") // label for swagger
	@GetMapping("/dashboard/recentClients")
	public ResponseEntity<?> getDashboardRecentClients(@RequestParam String loginUserID) {
		DashboardReport dashboardReport = clientGeneralService.getDashboardRecentClients(loginUserID);
		log.info("ClientGeneral Dashboard Active: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Search ClientGeneral") // label for swagger
	@PostMapping("/findClientGeneral")
	public List<ClientGeneral> findClientGeneral(@RequestBody SearchClientGeneral searchClientGeneral)
			throws ParseException {
		return clientGeneralService.findClientGenerals(searchClientGeneral);
	}
	//Find Client General New - SQL Query
	@ApiOperation(response = ClientGeneral.class, value = "Search ClientGeneral New") // label for swagger
	@PostMapping("/findClientGeneralNew")
	public List<IClientGeneralNew> findClientGeneralNew(@RequestBody SearchClientGeneral searchClientGeneral)
			throws ParseException {
		return clientGeneralService.findClientGeneralNew(searchClientGeneral);
	}
	
	//--------------------------Conflict-Check----------------------------------------------------------
	@ApiOperation(response = ClientGeneral.class, value = "Search ClientGeneral") // label for swagger
	@GetMapping("/findRecords")
	public List<ClientGeneral> findRecords(@RequestParam String fullTextSearch) throws ParseException {
		return clientGeneralService.findRecords(fullTextSearch);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Create ClientGeneral") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientGeneral(@Valid @RequestBody AddClientGeneral newClientGeneral,
			@RequestParam String loginUserID, Boolean isFromPotentialEndpoint)
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral createdClientGeneral = clientGeneralService.createClientGeneral(newClientGeneral, loginUserID,
				isFromPotentialEndpoint);
		return new ResponseEntity<>(createdClientGeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Create ClientGeneral") // label for swagger
	@PostMapping("/batch")
	public ResponseEntity<?> postBulkClientGenerals(@RequestBody AddClientGeneral[] newClientGenerals,
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		clientGeneralService.createBulkClientGenerals(newClientGenerals, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Update ClientGeneral") // label for swagger
	@PatchMapping("/{clientGeneralId}")
	public ResponseEntity<?> patchClientGeneral(@PathVariable String clientGeneralId,
			@Valid @RequestBody UpdateClientGeneral updateClientGeneral, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral updatedClientGeneral = clientGeneralService.updateClientGeneral(clientGeneralId,
				updateClientGeneral, loginUserID);
		return new ResponseEntity<>(updatedClientGeneral, HttpStatus.OK);
	}
	
	//--------------------------Docketwise-----------------------------------------------------------
	@ApiOperation(response = ClientGeneral.class, value = "Get a Docketwise Contacts") // label for swagger
	@GetMapping("/docketwise/contacts")
	public ResponseEntity<?> getDocketwiseContacts() {
		List<Contact> contacts = clientGeneralService.getDocketwiseContacts();
		log.info("contacts : " + contacts);
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a Docketwise Matters") // label for swagger
	@GetMapping("/docketwise/matters")
	public ResponseEntity<?> getDocketwiseMatters() {
		List<Matter> matters = clientGeneralService.getDocketwiseMatters();
		log.info("contacts : " + matters);
		return new ResponseEntity<>(matters, HttpStatus.OK);
	}
	
	//--------------------------Reports-----------------------------------------------------------
	@ApiOperation(response = ClientGeneralLNEReport[].class, value = "Client General LNE Report") // label for swagger
	@PostMapping("/lneReport")
	public ResponseEntity<?> getLNEClientGeneralReport (@RequestBody SearchClientGeneralLNEReport searchClientGeneralReport) 
			throws ParseException {
		List<ClientGeneralLNEReport> clientGeneralReport = 
				clientGeneralService.getLNEClientGeneralReport(searchClientGeneralReport);
		log.info("clientGeneralReport : " + clientGeneralReport);
		return new ResponseEntity<>(clientGeneralReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneralIMMReport[].class, value = "Client General IMM Report") // label for swagger
	@PostMapping("/immigrationReport")
	public ResponseEntity<?> getIMMClientGeneralReport (@RequestBody SearchClientGeneralIMMReport searchClientGeneralIMMReport) 
			throws ParseException {
		List<ClientGeneralIMMReport> clientGeneralImmReport = 
				clientGeneralService.getIMMClientGeneralReport(searchClientGeneralIMMReport);
		log.info("clientGeneralImmReport : " + clientGeneralImmReport);
		return new ResponseEntity<>(clientGeneralImmReport, HttpStatus.OK);
	}
}