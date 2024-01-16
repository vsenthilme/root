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

import com.mnrclara.api.setup.model.taskbasedcode.AddTaskbasedCode;
import com.mnrclara.api.setup.model.taskbasedcode.TaskbasedCode;
import com.mnrclara.api.setup.model.taskbasedcode.UpdateTaskbasedCode;
import com.mnrclara.api.setup.service.TaskbasedCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"TaskbasedCode"}, value = "TaskbasedCode Operations related to TaskbasedCodeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "TaskbasedCode",description = "Operations related to TaskbasedCode")})
@RequestMapping("/taskbasedCode")
@RestController
public class TaskbasedCodeController {
	
	@Autowired
	TaskbasedCodeService taskbasedCodeService;
	
    @ApiOperation(response = TaskbasedCode.class, value = "Get all TaskbasedCode details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<TaskbasedCode> taskbasedCodeList = taskbasedCodeService.getTaskbasedCodes();
		return new ResponseEntity<>(taskbasedCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Get a TaskbasedCode") // label for swagger 
	@GetMapping("/{taskCode}")
	public ResponseEntity<?> getTaskbasedCode(@PathVariable String taskCode) {
    	TaskbasedCode taskbasedCode = taskbasedCodeService.getTaskbasedCode(taskCode);
    	log.info("TaskbasedCode : " + taskbasedCode);
		return new ResponseEntity<>(taskbasedCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Create TaskbasedCode") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addTaskbasedCode(@Valid @RequestBody AddTaskbasedCode newTaskbasedCode, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TaskbasedCode createdTaskbasedCode = taskbasedCodeService.createTaskbasedCode(newTaskbasedCode, loginUserID);
		return new ResponseEntity<>(createdTaskbasedCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Update TaskbasedCode") // label for swagger
    @PatchMapping("/{taskCode}")
	public ResponseEntity<?> patchTaskbasedCode(@PathVariable String taskCode, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateTaskbasedCode updateTaskbasedCode) 
			throws IllegalAccessException, InvocationTargetException {
		TaskbasedCode updatedTaskbasedCode = taskbasedCodeService.updateTaskbasedCode(taskCode, loginUserID, updateTaskbasedCode);
		return new ResponseEntity<>(updatedTaskbasedCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Delete TaskbasedCode") // label for swagger
	@DeleteMapping("/{taskCode}")
	public ResponseEntity<?> deleteTaskbasedCode(@PathVariable String taskCode, @RequestParam String loginUserID) {
    	taskbasedCodeService.deleteTaskbasedCode(taskCode, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}