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

import com.mnrclara.api.setup.model.notetype.AddNoteType;
import com.mnrclara.api.setup.model.notetype.NoteType;
import com.mnrclara.api.setup.model.notetype.UpdateNoteType;
import com.mnrclara.api.setup.service.NoteTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"NoteType"}, value = "NoteType Operations related to NoteTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NoteType",description = "Operations related to NoteType")})
@RequestMapping("/noteType")
@RestController
public class NoteTypeController {
	
	@Autowired
	NoteTypeService noteTypeService;
	
    @ApiOperation(response = NoteType.class, value = "Get all NoteType details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<NoteType> noteTypeList = noteTypeService.getNoteTypes();
		return new ResponseEntity<>(noteTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Get a NoteType") // label for swagger 
	@GetMapping("/{noteTypeId}")
	public ResponseEntity<?> getNoteType(@PathVariable Long noteTypeId) {
    	NoteType noteType = noteTypeService.getNoteType(noteTypeId);
    	log.info("NoteType : " + noteType);
		return new ResponseEntity<>(noteType, HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Create NoteType") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addNoteType(@Valid @RequestBody AddNoteType newNoteType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType createdNoteType = noteTypeService.createNoteType(newNoteType, loginUserID);
		return new ResponseEntity<>(createdNoteType , HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Update NoteType") // label for swagger
    @PatchMapping("/{noteTypeId}")
	public ResponseEntity<?> patchNoteType(@PathVariable Long noteTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateNoteType updateNoteType) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType updatedNoteType = noteTypeService.updateNoteType(noteTypeId, loginUserID, updateNoteType);
		return new ResponseEntity<>(updatedNoteType , HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Delete NoteType") // label for swagger
	@DeleteMapping("/{noteTypeId}")
	public ResponseEntity<?> deleteNoteType(@PathVariable Long noteTypeId, @RequestParam String loginUserID) {
    	noteTypeService.deleteNoteType(noteTypeId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}