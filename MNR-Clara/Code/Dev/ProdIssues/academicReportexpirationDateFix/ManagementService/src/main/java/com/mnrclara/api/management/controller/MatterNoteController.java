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

import com.mnrclara.api.management.model.matternote.AddMatterNote;
import com.mnrclara.api.management.model.matternote.MatterNote;
import com.mnrclara.api.management.model.matternote.SearchMatterNote;
import com.mnrclara.api.management.model.matternote.UpdateMatterNote;
import com.mnrclara.api.management.service.MatterNoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MatterNote" }, value = "MatterNote Operations related to MatterNoteController") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "MatterNote", description = "Operations related to MatterNote") })
@RequestMapping("/matternote")
@RestController
public class MatterNoteController {

	@Autowired
	MatterNoteService matterNoteService;

	@ApiOperation(response = MatterNote.class, value = "Get all MatterNote details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterNote> clientNoteList = matterNoteService.getMatterNotes();
		return new ResponseEntity<>(clientNoteList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Get a MatterNote") // label for swagger
	@GetMapping("/{matterNotesNumber}")
	public ResponseEntity<?> getMatterNote(@PathVariable String matterNotesNumber) {
		MatterNote matternote = matterNoteService.getMatterNote(matterNotesNumber);
		return new ResponseEntity<>(matternote, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Search MatterNote") // label for swagger
	@PostMapping("/findMatterNotes")
	public List<MatterNote> findMatterNotes(@RequestBody SearchMatterNote searchMatterNote) throws ParseException {
		return matterNoteService.findMatterNotes(searchMatterNote);
	}

	@ApiOperation(response = MatterNote.class, value = "Create MatterNote") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterNote(@Valid @RequestBody AddMatterNote newMatterNote,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		MatterNote createdMatterNote = matterNoteService.createMatterNote(newMatterNote, loginUserID);
		return new ResponseEntity<>(createdMatterNote, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterNote.class, value = "Create Bulk MatterNote") // label for swagger
	@PostMapping("/batch")
	public ResponseEntity<?> postBulkMatterNote(@Valid @RequestBody AddMatterNote[] newMatterNote,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		matterNoteService.createBulkMatterNotes(newMatterNote, loginUserID);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Update MatterNote") // label for swagger
	@PatchMapping("/{matterNotesNumber}")
	public ResponseEntity<?> patchMatterNote(@PathVariable String matterNotesNumber,
			@Valid @RequestBody UpdateMatterNote updateMatterNote, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterNote updatedMatterNote = matterNoteService.updateMatterNote(matterNotesNumber, updateMatterNote,
				loginUserID);
		return new ResponseEntity<>(updatedMatterNote, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Delete MatterNote") // label for swagger
	@DeleteMapping("/{matterNotesNumber}")
	public ResponseEntity<?> deleteMatterNote(@PathVariable String matterNotesNumber, @RequestParam String loginUserID) {
		matterNoteService.deleteMatterNote(matterNotesNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}