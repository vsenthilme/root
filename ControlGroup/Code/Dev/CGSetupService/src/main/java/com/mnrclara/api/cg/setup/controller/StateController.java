package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.state.AddState;
import com.mnrclara.api.cg.setup.model.state.FindState;
import com.mnrclara.api.cg.setup.model.state.State;
import com.mnrclara.api.cg.setup.model.state.UpdateState;
import com.mnrclara.api.cg.setup.service.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"State"}, value = "State Operations related to StateController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "State",description = "Operations related to State")})
@RequestMapping("/state")
@RestController
public class StateController {
	
	@Autowired
	StateService stateService;
	
    @ApiOperation(response = State.class, value = "Get all State details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<State> stateList = stateService.getStates();
		return new ResponseEntity<>(stateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = State.class, value = "Get a State") // label for swagger 
	@GetMapping("/{stateId}")
	public ResponseEntity<?> getState(@PathVariable String stateId, @RequestParam String languageId,
									  @RequestParam String companyId) {
    	State state = stateService.getState(stateId, companyId, languageId);
    	log.info("State : " + state);
		return new ResponseEntity<>(state, HttpStatus.OK);
	}
    
    @ApiOperation(response = State.class, value = "Create State") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addState(@Valid @RequestBody AddState newState, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		State createdState = stateService.createState(newState, loginUserID);
		return new ResponseEntity<>(createdState , HttpStatus.OK);
	}
    
    @ApiOperation(response = State.class, value = "Update State") // label for swagger
    @PatchMapping("/{stateId}")
	public ResponseEntity<?> patchState(@PathVariable String stateId,@RequestParam String languageId,
										@RequestParam String loginUserID,@RequestParam String companyId,
										@Valid @RequestBody UpdateState updateState)
			throws IllegalAccessException, InvocationTargetException {
		State updatedState = stateService.updateState(stateId, languageId, companyId, loginUserID, updateState);
		return new ResponseEntity<>(updatedState , HttpStatus.OK);
	}
    
    @ApiOperation(response = State.class, value = "Delete State") // label for swagger
	@DeleteMapping("/{stateId}")
	public ResponseEntity<?> deleteState(@PathVariable String stateId, @RequestParam String loginUserID,
										 @RequestParam String languageId, @RequestParam String companyId) {
    	stateService.deleteState(stateId, companyId, languageId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//Search
	@ApiOperation(response = State.class, value = "Find State") // label for swagger
	@PostMapping("/find")
	public ResponseEntity<?> findState(@Valid @RequestBody FindState findState) throws Exception {
		List<State> createdState = stateService.findState(findState);
		return new ResponseEntity<>(createdState, HttpStatus.OK);
	}
}