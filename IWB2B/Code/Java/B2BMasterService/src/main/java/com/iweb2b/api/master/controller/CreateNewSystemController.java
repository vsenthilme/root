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

import com.iweb2b.api.master.model.createnewsystems.AddCreateNewSystem;
import com.iweb2b.api.master.model.createnewsystems.CreateNewSystem;
import com.iweb2b.api.master.model.createnewsystems.UpdateCreateNewSystem;
import com.iweb2b.api.master.service.CreateNewSystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "CreateNewSystem" }, value = "CreateNewSystem Operations related to CreateNewSystemController")
@SwaggerDefinition(tags = { @Tag(name = "CreateNewSystem", description = "Operations related to CreateNewSystem") })
@RequestMapping("/createNewSystem")
@RestController
public class CreateNewSystemController {

	@Autowired
	CreateNewSystemService createNewSystemsService;

	@ApiOperation(response = CreateNewSystem.class, value = "Get all CreateNewSystem details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<CreateNewSystem> createNewSystemsList = createNewSystemsService.getCreateNewSystem();
		return new ResponseEntity<>(createNewSystemsList, HttpStatus.OK);
	}

	@ApiOperation(response = CreateNewSystem.class, value = "Get a CreateNewSystem") // label for swagger
	@GetMapping("/{createNewSystemsId}")
	public ResponseEntity<?> getCreateNewSystem(@PathVariable String createNewSystemsId) {
		CreateNewSystem dbCreateNewSystem = createNewSystemsService.getCreateNewSystem(createNewSystemsId);
		log.info("CreateNewSystem : " + dbCreateNewSystem);
		return new ResponseEntity<>(dbCreateNewSystem, HttpStatus.OK);
	}

	@ApiOperation(response = CreateNewSystem.class, value = "Create CreateNewSystem") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postCreateNewSystem(@Valid @RequestBody AddCreateNewSystem newCreateNewSystem,
			@RequestParam String loginUserID) throws Exception {
		CreateNewSystem createdCreateNewSystem = createNewSystemsService.createCreateNewSystem(newCreateNewSystem, loginUserID);
		return new ResponseEntity<>(createdCreateNewSystem, HttpStatus.OK);
	}

	@ApiOperation(response = CreateNewSystem.class, value = "Update CreateNewSystem") // label for swagger
	@PatchMapping("/{createNewSystem}")
	public ResponseEntity<?> patchCreateNewSystem(@PathVariable String createNewSystems,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateCreateNewSystem updateCreateNewSystem)
			throws IllegalAccessException, InvocationTargetException {
		CreateNewSystem updatedCreateNewSystem = createNewSystemsService.updateCreateNewSystem(createNewSystems, loginUserID,
				updateCreateNewSystem);
		return new ResponseEntity<>(updatedCreateNewSystem, HttpStatus.OK);
	}

	@ApiOperation(response = CreateNewSystem.class, value = "Delete CreateNewSystem") // label for swagger
	@DeleteMapping("/{createNewSystem}")
	public ResponseEntity<?> deleteCreateNewSystem(@PathVariable String createNewSystem, @RequestParam String loginUserID) {
		createNewSystemsService.deleteCreateNewSystem(createNewSystem, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
