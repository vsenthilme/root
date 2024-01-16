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

import com.mnrclara.api.setup.model.tasktype.AddTaskType;
import com.mnrclara.api.setup.model.tasktype.TaskType;
import com.mnrclara.api.setup.model.tasktype.UpdateTaskType;
import com.mnrclara.api.setup.service.TaskTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"TaskType"}, value = "TaskType Operations related to TaskTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "TaskType",description = "Operations related to TaskType")})
@RequestMapping("/taskType")
@RestController
public class TaskTypeController {
	
	@Autowired	
	TaskTypeService taskTypeService;
	
    @ApiOperation(response = TaskType.class, value = "Get all TaskType details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<TaskType> taskTypeList = taskTypeService.getCompanies();
		return new ResponseEntity<>(taskTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Get a TaskType") // label for swagger 
	@GetMapping("/{taskTypeCode}")
	public ResponseEntity<?> getTaskType(@PathVariable Long taskTypeCode) {
    	TaskType taskType = taskTypeService.getTaskType(taskTypeCode);
    	log.info("TaskType : " + taskType);
		return new ResponseEntity<>(taskType, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Create TaskType") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postTaskType(@Valid @RequestBody AddTaskType newTaskType, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType createdTaskType = taskTypeService.createTaskType(newTaskType, loginUserID);
		return new ResponseEntity<>(createdTaskType , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Update TaskType") // label for swagger
    @PatchMapping("/{taskTypeCode}")
	public ResponseEntity<?> patchTaskType(@PathVariable Long taskTypeCode, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateTaskType updateTaskType) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType updatedTaskType = taskTypeService.updateTaskType(taskTypeCode, loginUserID, updateTaskType);
		return new ResponseEntity<>(updatedTaskType , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Delete TaskType") // label for swagger
	@DeleteMapping("/{taskTypeCode}")
	public ResponseEntity<?> deleteTaskType(@PathVariable Long taskTypeCode, @RequestParam String loginUserID) {
    	taskTypeService.deleteTaskType(taskTypeCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}