package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

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

import com.mnrclara.api.setup.model.clientuser.AddClientUser;
import com.mnrclara.api.setup.model.clientuser.ClientUser;
import com.mnrclara.api.setup.model.clientuser.EMail;
import com.mnrclara.api.setup.model.clientuser.*;
import com.mnrclara.api.setup.model.clientuser.UpdateClientUser;
import com.mnrclara.api.setup.service.ClientUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"ClientUser"}, value = "ClientUser Operations related to ClientUserController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ClientUser",description = "Operations related to ClientUser")})
@RequestMapping("/clientUser")
@RestController
public class ClientUserController {
	
	@Autowired
	ClientUserService clientUserService;
	
    @ApiOperation(response = ClientUser.class, value = "Get all ClientUser details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientUser> clientUserList = clientUserService.getClientUsers();
		return new ResponseEntity<>(clientUserList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Get a ClientUser") // label for swagger 
	@GetMapping("/{clientUserId}")
	public ResponseEntity<?> getClientUser(@PathVariable String clientUserId) {
    	ClientUser clientUser = clientUserService.getClientUser(clientUserId);
    	log.info("ClientUser : " + clientUser);
		return new ResponseEntity<>(clientUser, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Find ClientUser") // label for swagger
    @PostMapping("/findClientUser")
    public List<ClientUser> findClientUser(@RequestBody SearchClientUser searchClientUser) throws ParseException {
		return clientUserService.findClientUser(searchClientUser);
	}
	@ApiOperation(response = ClientUser.class, value = "Find ClientUser New") // label for swagger
	@PostMapping("/findClientUserNew")
	public ResponseEntity<List<ClientUserImpl>> getClientUserNew (@RequestBody FindClientUser findClientUser)
			throws Exception {
		List<ClientUserImpl> data = clientUserService.findNewClientUser(findClientUser);
		return new ResponseEntity<List<ClientUserImpl>>(data, HttpStatus.OK);
	}

    
    @ApiOperation(response = ClientUser.class, value = "Create ClientUser") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addClientUser(@Valid @RequestBody AddClientUser newClientUser, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser createdClientUser = clientUserService.createClientUser(newClientUser, loginUserID);
		return new ResponseEntity<>(createdClientUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Send To Client") // label for swagger
	@PostMapping("/sendToClient")
	public ResponseEntity<?> sendToClient (@Valid @RequestBody EMail eMail) 
			throws IllegalAccessException, InvocationTargetException {
		clientUserService.sendEmail(eMail);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Update ClientUser") // label for swagger
    @PatchMapping("/{clientUserId}")
	public ResponseEntity<?> patchClientUser(@PathVariable String clientUserId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateClientUser updateClientUser) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser updatedClientUser = clientUserService.updateClientUser(clientUserId, loginUserID, updateClientUser);
		return new ResponseEntity<>(updatedClientUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Delete ClientUser") // label for swagger
	@DeleteMapping("/{clientUserId}")
	public ResponseEntity<?> deleteClientUser(@PathVariable String clientUserId, @RequestParam String loginUserID) {
    	clientUserService.deleteClientUser(clientUserId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}