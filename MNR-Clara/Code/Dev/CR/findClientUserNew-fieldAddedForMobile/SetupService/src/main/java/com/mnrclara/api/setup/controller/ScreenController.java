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

import com.mnrclara.api.setup.model.screen.AddScreen;
import com.mnrclara.api.setup.model.screen.Screen;
import com.mnrclara.api.setup.model.screen.UpdateScreen;
import com.mnrclara.api.setup.service.ScreenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Screen"}, value = "Screen Operations related to ScreenController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Screen",description = "Operations related to Screen")})
@RequestMapping("/screen")
@RestController
public class ScreenController {
	
	@Autowired
	ScreenService screenService;
	
    @ApiOperation(response = Screen.class, value = "Get all Screen details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Screen> screenList = screenService.getScreens();
		return new ResponseEntity<>(screenList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Get a Screen") // label for swagger 
	@GetMapping("/{screenId}")
	public ResponseEntity<?> getScreen(@PathVariable Long screenId) {
    	Screen screen = screenService.getScreen(screenId);
    	log.info("Screen : " + screen);
		return new ResponseEntity<>(screen, HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Create Screen") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addScreen(@Valid @RequestBody AddScreen newScreen, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Screen createdScreen = screenService.createScreen(newScreen, loginUserID);
		return new ResponseEntity<>(createdScreen , HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Update Screen") // label for swagger
    @PatchMapping("/{screenId}")
	public ResponseEntity<?> patchScreen(@PathVariable Long screenId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateScreen updateScreen) 
			throws IllegalAccessException, InvocationTargetException {
		Screen updatedScreen = screenService.updateScreen(screenId, loginUserID, updateScreen);
		return new ResponseEntity<>(updatedScreen , HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Delete Screen") // label for swagger
	@DeleteMapping("/{screenId}")
	public ResponseEntity<?> deleteScreen(@PathVariable Long screenId, @RequestParam String loginUserID	) {
    	screenService.deleteScreen(screenId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}