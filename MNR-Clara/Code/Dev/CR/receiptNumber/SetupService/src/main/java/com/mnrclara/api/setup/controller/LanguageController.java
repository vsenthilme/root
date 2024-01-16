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

import com.mnrclara.api.setup.model.language.AddLanguage;
import com.mnrclara.api.setup.model.language.Language;
import com.mnrclara.api.setup.model.language.UpdateLanguage;
import com.mnrclara.api.setup.service.LanguageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Language"}, value = "Language Operations related to LanguageController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Language",description = "Operations related to Language")})
@RequestMapping("/language")
@RestController
public class LanguageController {
	
	@Autowired
	LanguageService languageService;
	
    @ApiOperation(response = Language.class, value = "Get all Language details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Language> languageList = languageService.getLanguages();
		return new ResponseEntity<>(languageList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Get a Language") // label for swagger 
	@GetMapping("/{languageId}")
	public ResponseEntity<?> getLanguage(@PathVariable String languageId) {
    	Language language = languageService.getLanguage(languageId);
    	log.info("Language : " + language);
		return new ResponseEntity<>(language, HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Create Language") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addLanguage(@Valid @RequestBody AddLanguage newLanguage, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Language createdLanguage = languageService.createLanguage(newLanguage, loginUserID);
		return new ResponseEntity<>(createdLanguage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Update Language") // label for swagger
    @PatchMapping("/{languageId}")
	public ResponseEntity<?> patchLanguage(@PathVariable String languageId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateLanguage updateLanguage) 
			throws IllegalAccessException, InvocationTargetException {
		Language updatedLanguage = languageService.updateLanguage(languageId, loginUserID, updateLanguage);
		return new ResponseEntity<>(updatedLanguage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Delete Language") // label for swagger
	@DeleteMapping("/{languageId}")
	public ResponseEntity<?> deleteLanguage(@PathVariable String languageId, @RequestParam String loginUserID) {
    	languageService.deleteLanguage(languageId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}