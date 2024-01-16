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

import com.mnrclara.api.setup.model.classid.AddClass;
import com.mnrclara.api.setup.model.classid.UpdateClass;
import com.mnrclara.api.setup.service.ClassService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Class"}, value = "Class Operations related to ClassController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Class",description = "Operations related to Class")})
@RequestMapping("/class")
@RestController
public class ClassController {
	
	@Autowired
	ClassService classService;
	
    @ApiOperation(response = com.mnrclara.api.setup.model.classid.Class.class, value = "Get all Class details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<com.mnrclara.api.setup.model.classid.Class> classList = classService.getClasses();
		return new ResponseEntity<>(classList, HttpStatus.OK);
	}
    
    @ApiOperation(response = com.mnrclara.api.setup.model.classid.Class.class, value = "Get a Class") // label for swagger 
	@GetMapping("/{classId}")
	public ResponseEntity<?> getClass(@PathVariable Long classId) {
    	com.mnrclara.api.setup.model.classid.Class classObj = classService.getClass(classId);
    	log.info("Class : " + classObj);
		return new ResponseEntity<>(classObj, HttpStatus.OK);
	}
    
    @ApiOperation(response = com.mnrclara.api.setup.model.classid.Class.class, value = "Create Class") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addClass(@Valid @RequestBody AddClass newClass, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.api.setup.model.classid.Class createdClass = classService.createClass(newClass, loginUserID);
		return new ResponseEntity<>(createdClass, HttpStatus.OK);
	}
    
    @ApiOperation(response = com.mnrclara.api.setup.model.classid.Class.class, value = "Update Class") // label for swagger
    @PatchMapping("/{classId}")
	public ResponseEntity<?> patchClass(@PathVariable Long classId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateClass updateClass) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.api.setup.model.classid.Class updatedClass = classService.updateClass(classId, loginUserID, updateClass);
		return new ResponseEntity<>(updatedClass , HttpStatus.OK);
	}
    
    @ApiOperation(response = com.mnrclara.api.setup.model.classid.Class.class, value = "Delete Class") // label for swagger
	@DeleteMapping("/{classId}")
	public ResponseEntity<?> deleteClass(@PathVariable Long classId, @RequestParam String loginUserID) {
    	classService.deleteClass(classId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}