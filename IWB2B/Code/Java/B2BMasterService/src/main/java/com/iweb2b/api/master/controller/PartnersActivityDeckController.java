package com.iweb2b.api.master.controller;

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

import com.iweb2b.api.master.model.partnersactivitydeck.AddPartnersActivityDeck;
import com.iweb2b.api.master.model.partnersactivitydeck.PartnersActivityDeck;
import com.iweb2b.api.master.model.partnersactivitydeck.UpdatePartnersActivityDeck;
import com.iweb2b.api.master.service.PartnersActivityDeckService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "PartnersActivityDeck" }, value = "PartnersActivityDeck Operations related to PartnersActivityDeckController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "PartnersActivityDeck", description = "Operations related to PartnersActivityDeck") })
@RequestMapping("/partnersActivityDeck")
@RestController
public class PartnersActivityDeckController {

	@Autowired
	PartnersActivityDeckService partnersActivityDeckService;

	@ApiOperation(response = PartnersActivityDeck.class, value = "Get all PartnersActivityDeck details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PartnersActivityDeck> partnersActivityDeckList = partnersActivityDeckService.getPartnersActivityDeck();
		return new ResponseEntity<>(partnersActivityDeckList, HttpStatus.OK);
	}

	@ApiOperation(response = PartnersActivityDeck.class, value = "Get a PartnersActivityDeck") // label for swagger
	@GetMapping("/{partnersActivityDeckId}")
	public ResponseEntity<?> getPartnersActivityDeck(@PathVariable String partnersActivityDeckId) {
		PartnersActivityDeck dbPartnersActivityDeck = partnersActivityDeckService.getPartnersActivityDeck(partnersActivityDeckId);
		log.info("PartnersActivityDeck : " + dbPartnersActivityDeck);
		return new ResponseEntity<>(dbPartnersActivityDeck, HttpStatus.OK);
	}

	@ApiOperation(response = PartnersActivityDeck.class, value = "Create PartnersActivityDeck") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postPartnersActivityDeck(@Valid @RequestBody AddPartnersActivityDeck newPartnersActivityDeck,
			@RequestParam String loginUserID) throws Exception {
		PartnersActivityDeck createdPartnersActivityDeck = partnersActivityDeckService.createPartnersActivityDeck(newPartnersActivityDeck, loginUserID);
		return new ResponseEntity<>(createdPartnersActivityDeck, HttpStatus.OK);
	}

	@ApiOperation(response = PartnersActivityDeck.class, value = "Update PartnersActivityDeck") // label for swagger
	@PatchMapping("/{partnersActivityDeck}")
	public ResponseEntity<?> patchPartnersActivityDeck(@PathVariable String partnersActivityDeck,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdatePartnersActivityDeck updatePartnersActivityDeck)
			throws IllegalAccessException, InvocationTargetException {
		PartnersActivityDeck updatedPartnersActivityDeck = partnersActivityDeckService.updatePartnersActivityDeck(partnersActivityDeck, loginUserID,
				updatePartnersActivityDeck);
		return new ResponseEntity<>(updatedPartnersActivityDeck, HttpStatus.OK);
	}

	@ApiOperation(response = PartnersActivityDeck.class, value = "Delete PartnersActivityDeck") // label for swagger
	@DeleteMapping("/{partnersActivityDeck}")
	public ResponseEntity<?> deletePartnersActivityDeck(@PathVariable String partnersActivityDeck, @RequestParam String loginUserID) {
		partnersActivityDeckService.deletePartnersActivityDeck(partnersActivityDeck, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
