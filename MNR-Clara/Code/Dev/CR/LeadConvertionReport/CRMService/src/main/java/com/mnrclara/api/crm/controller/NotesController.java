package com.mnrclara.api.crm.controller;

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

import com.mnrclara.api.crm.model.notes.AddNotes;
import com.mnrclara.api.crm.model.notes.Notes;
import com.mnrclara.api.crm.model.notes.UpdateNotes;
import com.mnrclara.api.crm.service.NotesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Notes"}, value = "Notes Operations related to NotesController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Notes",description = "Operations related to Notes")})
@RequestMapping("/notes")
@RestController
public class NotesController {
	
	@Autowired
	NotesService notesService;
	
    @ApiOperation(response = Notes.class, value = "Get all Notes details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Notes> notesList = notesService.getNotess();
		return new ResponseEntity<>(notesList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notes.class, value = "Get a Notes") // label for swagger 
	@GetMapping("/{notesNumber}")
	public ResponseEntity<?> getNotes(@PathVariable String notesNumber) {
    	Notes notes = notesService.getNotes(notesNumber);
    	return new ResponseEntity<>(notes, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notes.class, value = "Create Notes") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postNotes(@Valid @RequestBody AddNotes newNotes, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notes createdNotes = notesService.createNotes(newNotes, loginUserID);
		return new ResponseEntity<>(createdNotes , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notes.class, value = "Update Notes") // label for swagger
    @PatchMapping("/{notesNumber}")
	public ResponseEntity<?> patchNotes(@PathVariable String notesNumber, 
			@Valid @RequestBody UpdateNotes updateNotes, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notes updatedNotes = notesService.updateNotes(notesNumber, updateNotes, loginUserID);
		return new ResponseEntity<>(updatedNotes , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notes.class, value = "Delete Notes") // label for swagger
	@DeleteMapping("/{notesNumber}")
	public ResponseEntity<?> deleteNotes(@PathVariable String notesNumber, @RequestParam String loginUserID) {
    	notesService.deleteNotes(notesNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}