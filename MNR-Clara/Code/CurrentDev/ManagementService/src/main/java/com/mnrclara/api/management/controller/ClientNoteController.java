package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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

import com.mnrclara.api.management.model.clientnote.AddClientNote;
import com.mnrclara.api.management.model.clientnote.ClientNote;
import com.mnrclara.api.management.model.clientnote.SearchClientNote;
import com.mnrclara.api.management.model.clientnote.UpdateClientNote;
import com.mnrclara.api.management.service.ClientNoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ClientNote" }, value = "ClientNote Operations related to ClientNoteController") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "ClientNote", description = "Operations related to ClientNote") })
@RequestMapping("/clientnote")
@RestController
public class ClientNoteController {

	@Autowired
	ClientNoteService clientNoteService;

	@ApiOperation(response = ClientNote.class, value = "Get all ClientNote details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ClientNote> clientNoteList = clientNoteService.getClientNotes();
		return new ResponseEntity<>(clientNoteList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Get a ClientNote") // label for swagger
	@GetMapping("/{clientNotesNumber}")
	public ResponseEntity<?> getClientNote(@PathVariable String clientNotesNumber) {
		ClientNote clientnote = clientNoteService.getClientNote(clientNotesNumber);
		log.info("ClientNote : " + clientnote);
		return new ResponseEntity<>(clientnote, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Search ClientNote") // label for swagger
	@PostMapping("/findClientNotes")
	public List<ClientNote> findClientGeneral(@RequestBody SearchClientNote searchClientNote) throws ParseException {
		return clientNoteService.findClientNotes(searchClientNote);
	}

	@ApiOperation(response = ClientNote.class, value = "Create ClientNote") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postClientNote(@Valid @RequestBody AddClientNote newClientNote,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ClientNote createdClientNote = clientNoteService.createClientNote(newClientNote, loginUserID);
		return new ResponseEntity<>(createdClientNote, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientNote.class, value = "Create ClientGeneral") // label for swagger
	@PostMapping("/batch")
	public ResponseEntity<?> postBulkClientNotes(@RequestBody AddClientNote[] newClientNotes,
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		clientNoteService.createBulkClientNotes(newClientNotes, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Update ClientNote") // label for swagger
	@PatchMapping("/{clientNotesNumber}")
	public ResponseEntity<?> patchClientNote(@PathVariable String clientNotesNumber,
			@Valid @RequestBody UpdateClientNote updateClientNote, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientNote updatedClientNote = clientNoteService.updateClientNote(clientNotesNumber, updateClientNote,
				loginUserID);
		return new ResponseEntity<>(updatedClientNote, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Delete ClientNote") // label for swagger
	@DeleteMapping("/{clientNotesNumber}")
	public ResponseEntity<?> deleteClientNote(@PathVariable String clientNotesNumber, @RequestParam String loginUserID) {
		clientNoteService.deleteClientNote(clientNotesNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}