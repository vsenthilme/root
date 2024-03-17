package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.mnrclara.api.management.model.mattertask.AddMatterTask;
import com.mnrclara.api.management.model.mattertask.MatterTask;
import com.mnrclara.api.management.model.mattertask.SearchMatterTask;
import com.mnrclara.api.management.model.mattertask.UpdateMatterTask;
import com.mnrclara.api.management.service.MatterTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MatterTask" }, value = "MatterTask Operations related to MatterTaskController") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "MatterTask", description = "Operations related to MatterTask") })
@RequestMapping("/mattertask")
@RestController
public class MatterTaskController {

	@Autowired
	MatterTaskService matterTaskService;

	@ApiOperation(response = MatterTask.class, value = "Get all MatterTask details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterTask> matterTaskList = matterTaskService.getMatterTasks();
		return new ResponseEntity<>(matterTaskList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Get a MatterTask") // label for swagger
	@GetMapping("/{taskNumber}")
	public ResponseEntity<?> getMatterTask(@PathVariable String taskNumber) {
		MatterTask mattertask = matterTaskService.getMatterTask(taskNumber);
		log.info("MatterTask : " + mattertask);
		return new ResponseEntity<>(mattertask, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterTask.class, value = "Search MatterTask") // label for swagger
    @PostMapping("/findMatterTasks")
    public List<MatterTask> findMatterTasks(@RequestBody SearchMatterTask searchMatterTask) throws ParseException {
		return matterTaskService.findMatterTasks(searchMatterTask);
	}

	@ApiOperation(response = MatterTask.class, value = "Create MatterTask") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterTask(@Valid @RequestBody AddMatterTask newMatterTask,
			@RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
		MatterTask createdMatterTask = matterTaskService.createMatterTask(newMatterTask, loginUserID);
		matterTaskService.sendNotification(createdMatterTask,"Matter Task");
		return new ResponseEntity<>(createdMatterTask, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Update MatterTask") // label for swagger
	@PatchMapping("/{taskNumber}")
	public ResponseEntity<?> patchMatterTask(@PathVariable String taskNumber,
			@Valid @RequestBody UpdateMatterTask updateMatterTask, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterTask updatedMatterTask = matterTaskService.updateMatterTask(taskNumber, updateMatterTask, loginUserID);
		if(updatedMatterTask.getStatusId() == 32L){
			matterTaskService.sendNotification(updatedMatterTask,"Matter Task Update");
		}
		return new ResponseEntity<>(updatedMatterTask, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Delete MatterTask") // label for swagger
	@DeleteMapping("/{taskNumber}")
	public ResponseEntity<?> deleteMatterTask(@PathVariable String taskNumber) {
		matterTaskService.deleteMatterTask(taskNumber);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}