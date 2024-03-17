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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneral;
import com.mnrclara.api.management.model.qb.QBSync;
import com.mnrclara.api.management.model.qb.SearchQbSync;
import com.mnrclara.api.management.service.QbSyncService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "QBSync" }, value = "QBSync Operations related to QbSyncController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "QBSync", description = "Operations related to QBSync") })
@RequestMapping("/qbsync")
@RestController
public class QbSyncController {

	@Autowired
	QbSyncService qbSyncService;

	@ApiOperation(response = QBSync.class, value = "Get all QBSync details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<QBSync> activityCodeList = qbSyncService.getQBSyncs();
		return new ResponseEntity<>(activityCodeList, HttpStatus.OK);
	}

	@ApiOperation(response = QBSync.class, value = "Get a QBSync") // label for swagger
	@GetMapping("/{id}")
	public ResponseEntity<?> getQBSync(@PathVariable String id) {
		QBSync dbQbSync = qbSyncService.getQBSync(id);
		return new ResponseEntity<>(dbQbSync, HttpStatus.OK);
	}
	
	@ApiOperation(response = QBSync.class, value = "Search QBSync") // label for swagger
	@PostMapping("/findQbSync")
	public List<QBSync> findQbSync(@RequestBody SearchQbSync searchQbSync)
			throws ParseException {
		return qbSyncService.findQbSync(searchQbSync);
	}

	@ApiOperation(response = QBSync.class, value = "Create QBSync") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postQBSync(@Valid @RequestBody QBSync newQbSync) throws Exception {
		QBSync createdQbSync = qbSyncService.createQBSync(newQbSync);
		return new ResponseEntity<>(createdQbSync, HttpStatus.OK);
	}

	@ApiOperation(response = QBSync.class, value = "Update QBSync") // label for swagger
	@PatchMapping("/{id}")
	public ResponseEntity<?> patchQBSync(@PathVariable String id, @Valid @RequestBody QBSync updateQbSync)
			throws IllegalAccessException, InvocationTargetException {
		QBSync updatedQbSync = qbSyncService.updateQBSync(id, updateQbSync);
		return new ResponseEntity<>(updatedQbSync, HttpStatus.OK);
	}
}