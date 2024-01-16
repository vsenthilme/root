package com.mnrclara.api.crm.controller;

import com.mnrclara.api.crm.model.pcitform.*;
import com.mnrclara.api.crm.service.FeedbackFormService;
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

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"FeedbackForm"}, value = "FeedbackForm Operations related to FeedbackFormController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "FeedbackForm",description = "Operations related to FeedbackForm")})
@RequestMapping("/feedbackform")
@RestController
public class FeedbackFormController {

	@Autowired
	FeedbackFormService feedbackFormService;

    @ApiOperation(response = FeedbackForm.class, value = "Get a FeedbackForm") // label for swagger
	@GetMapping("/{intakeFormNumber}")
	public ResponseEntity<?> getFeedbackForm(@PathVariable String intakeFormNumber) {
		FeedbackForm feedbackForm = feedbackFormService.getFeedbackForm(intakeFormNumber);
    	return new ResponseEntity<>(feedbackForm, HttpStatus.OK);

	}

	@ApiOperation(response = FeedbackForm.class, value = "Create FeedbackForm") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postFeedbackForm(@Valid @RequestBody FeedbackForm newStagingHeader,
											  @RequestParam String loginUserID) {
		FeedbackForm createdFeedbackForm = feedbackFormService.createFeedbackForm(newStagingHeader, loginUserID);
		return new ResponseEntity<>(createdFeedbackForm , HttpStatus.OK);
	}
	
	//---------------------------------FeedbackForm-CR-Sending SMS-------------------------------------------------
    @ApiOperation(response = Boolean.class, value = "FeedbackForm-CR-Send SNS") // label for swagger
    @PostMapping("/{intakeFormNumber}/feedback/sms")
    public ResponseEntity<?> sendFeedbackSMS(@PathVariable String intakeFormNumber, @RequestBody Feedback feedback)
    		throws Exception {
    	feedbackFormService.sendFeedbackSMS(intakeFormNumber, feedback);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}