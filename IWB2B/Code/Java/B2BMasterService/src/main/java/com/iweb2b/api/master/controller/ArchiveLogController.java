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

import com.iweb2b.api.master.model.archivelogs.AddArchiveLogs;
import com.iweb2b.api.master.model.archivelogs.ArchiveLogs;
import com.iweb2b.api.master.model.archivelogs.UpdateArchiveLogs;
import com.iweb2b.api.master.service.ArchiveLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "ArchiveLogs" }, value = "ArchiveLogs Operations related to ArchiveLogsController") // label for
																										// swagger
@SwaggerDefinition(tags = { @Tag(name = "ArchiveLogs", description = "Operations related to ArchiveLogs") })
@RequestMapping("/archiveLogs")
@RestController
public class ArchiveLogController {

	@Autowired
	ArchiveLogService archiveLogsService;

	@ApiOperation(response = ArchiveLogs.class, value = "Get all ArchiveLogs details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<ArchiveLogs> archiveLogsList = archiveLogsService.getArchiveLogs();
		return new ResponseEntity<>(archiveLogsList, HttpStatus.OK);
	}

	@ApiOperation(response = ArchiveLogs.class, value = "Get a ArchiveLogs") // label for swagger
	@GetMapping("/{archiveLogsId}")
	public ResponseEntity<?> getArchiveLogs(@PathVariable String archiveLogsId) {
		ArchiveLogs dbArchiveLogs = archiveLogsService.getArchiveLogs(archiveLogsId);
		log.info("ArchiveLogs : " + dbArchiveLogs);
		return new ResponseEntity<>(dbArchiveLogs, HttpStatus.OK);
	}

	@ApiOperation(response = ArchiveLogs.class, value = "Create ArchiveLogs") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postArchiveLogs(@Valid @RequestBody AddArchiveLogs newArchiveLogs,
			@RequestParam String loginUserID) throws Exception {
		ArchiveLogs createdArchiveLogs = archiveLogsService.createArchiveLogs(newArchiveLogs, loginUserID);
		return new ResponseEntity<>(createdArchiveLogs, HttpStatus.OK);
	}

	@ApiOperation(response = ArchiveLogs.class, value = "Update ArchiveLogs") // label for swagger
	@PatchMapping("/{archiveLogs}")
	public ResponseEntity<?> patchArchiveLogs(@PathVariable String archiveLogs,
			@RequestParam String loginUserID,
			@Valid @RequestBody UpdateArchiveLogs updateArchiveLogs)
			throws IllegalAccessException, InvocationTargetException {
		ArchiveLogs updatedArchiveLogs = archiveLogsService.updateArchiveLogs(archiveLogs, loginUserID,
				updateArchiveLogs);
		return new ResponseEntity<>(updatedArchiveLogs, HttpStatus.OK);
	}

	@ApiOperation(response = ArchiveLogs.class, value = "Delete ArchiveLogs") // label for swagger
	@DeleteMapping("/{archiveLogs}")
	public ResponseEntity<?> deleteArchiveLogs(@PathVariable String archiveLogs, @RequestParam String loginUserID) {
		archiveLogsService.deleteArchiveLogs(archiveLogs, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
