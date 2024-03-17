package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mnrclara.api.management.model.clientdocument.AddClientDocument;
import com.mnrclara.api.management.model.clientdocument.ClientDocument;
import com.mnrclara.api.management.model.clientdocument.SearchClientDocument;
import com.mnrclara.api.management.model.clientdocument.UpdateClientDocument;
import com.mnrclara.api.management.model.dto.EnvelopeStatus;
import com.mnrclara.api.management.service.ClientDocumentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientDocument" }, value = "ClientDocument Operations related to ClientDocumentController")
@SwaggerDefinition(tags = { @Tag(name = "ClientDocument", description = "Operations related to ClientDocument") })
@RequestMapping("/clientdocument")
@RestController
public class ClientDocumentController {

	@Autowired
	ClientDocumentService clientDocumentService;

	@ApiOperation(response = ClientDocument.class, value = "Get all ClientDocument details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientDocument> clientDocumentList = clientDocumentService.getClientDocuments();
		return new ResponseEntity<>(clientDocumentList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientDocument.class, value = "Get a ClientDocument") // label for swagger
	@GetMapping("/{clientDocumentId}")
	public ResponseEntity<?> getClientDocument(@PathVariable Long clientDocumentId) {
		ClientDocument clientdocument = clientDocumentService.getClientDocument(clientDocumentId);
		log.info("ClientDocument : " + clientdocument);
		return new ResponseEntity<>(clientdocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Search ClientDocument") // label for swagger
	@PostMapping("/findClientDocument")
	public List<ClientDocument> findClientDocument(@RequestBody SearchClientDocument searchClientDocument)
			throws ParseException {
		return clientDocumentService.findClientDocument(searchClientDocument);
	}

	@ApiOperation(response = ClientDocument.class, value = "Create ClientDocument") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientDocument(@Valid @RequestBody AddClientDocument newClientDocument, 
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument createdClientDocument = clientDocumentService.createClientDocument(newClientDocument, loginUserID);
		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Send Document to Docusign") // label for swagger
	@PostMapping("/docusign")
	public ResponseEntity<?> postClientDocumentToDocusign(@RequestParam Long classId, 
			@RequestParam String clientId, @RequestParam String documentNumber, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument createdClientDocument = 
				clientDocumentService.sendDocumentToDocusign(classId, clientId, documentNumber, loginUserID);
		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Update ClientDocument") // label for swagger
	@PatchMapping("/{clientDocumentId}")
	public ResponseEntity<?> patchClientDocument(@PathVariable Long clientDocumentId,
			@Valid @RequestBody UpdateClientDocument updateClientDocument, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument updatedClientDocument = clientDocumentService.updateClientDocument(clientDocumentId,
				updateClientDocument, loginUserID);
		return new ResponseEntity<>(updatedClientDocument, HttpStatus.OK);
	}
	
    @ApiOperation(response = Optional.class, value = "DocuSign Download") // label for swagger
   	@GetMapping("/{clientId}/envelope/download")
   	public ResponseEntity<?> downloadEnvelopeFromDocusign(@PathVariable String clientId, @RequestParam Long classId,
   			@RequestParam String documentNumber, @RequestParam String loginUserID) throws Exception {
    	ClientDocument response = 
    			clientDocumentService.downloadEnvelopeFromDocusign(classId, clientId, documentNumber, loginUserID);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    @ApiOperation(response = Optional.class, value = "DocuSign Status") // label for swagger
   	@GetMapping("/{clientId}/envelope/status")
   	public ResponseEntity<?> getEnvelopeStatusFromDocusign(@PathVariable String clientId, 
   			@RequestParam String loginUserID) throws Exception {
    	EnvelopeStatus response = clientDocumentService.getEnvelopeStatusFromDocusign(clientId, loginUserID);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

	@ApiOperation(response = Optional.class, value = "Delete ClientDocument") // label for swagger
	@DeleteMapping("/{clientDocumentId}")
	public ResponseEntity<?> deleteClientDocument(@PathVariable Long clientDocumentId, @RequestParam String loginUserID) {
		clientDocumentService.deleteClientDocument(clientDocumentId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    //--------------------------------------------------------------------------------------------------------
    @ApiOperation(response = ClientDocument.class, value = "Process Mailmarge Manual Document") // label for swagger
	@PatchMapping("/{clientId}/mailmerge/manual")
	public ResponseEntity<?> postMailmergeManual(@PathVariable String clientId, @RequestParam Long classId,
   			@RequestParam String documentNumber, String documentUrl, @RequestParam String loginUserID) 
   					throws IllegalAccessException, InvocationTargetException {
		ClientDocument createdClientDocument = 
				clientDocumentService.doProcessEditedClientDocument(classId, clientId, documentNumber, documentUrl, loginUserID);
		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
	}
}