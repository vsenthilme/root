package com.mnrclara.api.crm.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.crm.repository.InquiryRepository;
import com.mnrclara.api.crm.service.NotificationService;
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

import com.mnrclara.api.crm.model.pcitform.Feedback;
import com.mnrclara.api.crm.model.pcitform.LeadConversionReport;
import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeForm;
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeFormReport;
import com.mnrclara.api.crm.model.pcitform.UpdatePCIntakeForm;
import com.mnrclara.api.crm.service.PCIntakeFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"PCIntakeForm"}, value = "PCIntakeForm Operations related to PCIntakeFormController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "PCIntakeForm",description = "Operations related to PCIntakeForm")})
@RequestMapping("/pcIntakeForm")
@RestController
public class PCIntakeFormController {

	@Autowired
	PCIntakeFormService pcIntakeFormService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	InquiryRepository inquiryRepository;

    @ApiOperation(response = PCIntakeForm.class, value = "Get all PCIntakeForm details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<PCIntakeForm> pcIntakeFormList = pcIntakeFormService.getPCIntakeForms();
		return new ResponseEntity<>(pcIntakeFormList, HttpStatus.OK);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Get a PCIntakeForm") // label for swagger 
	@GetMapping("/{intakeFormNumber}")
	public ResponseEntity<?> getPCIntakeForm(@PathVariable String intakeFormNumber) {
    	PCIntakeForm pcIntakeForm = pcIntakeFormService.getPCIntakeForm(intakeFormNumber);
    	log.info("PCIntakeForm : " + pcIntakeForm);
    	if (pcIntakeForm != null) {
    		return new ResponseEntity<>(pcIntakeForm, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given Intake Form Number : " + intakeFormNumber + " doesn't exists", HttpStatus.OK);
    	}
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Find Inquiry") // label for swagger
    @PostMapping("/findPCIntakeForm")
    public List<PCIntakeForm> findPCIntakeForms (@RequestBody SearchPCIntakeForm searchPCIntakeForm) throws ParseException {
		return pcIntakeFormService.findPCIntakeForms(searchPCIntakeForm);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Get a PCIntakeForm") // label for swagger 
    @GetMapping("/{intakeFormNumber}/inquiryNumber/{inquiryNumber}")
   	public ResponseEntity<?> getPCIntakeForm(@PathVariable String intakeFormNumber,
   												@PathVariable String inquiryNumber){
    	PCIntakeForm pcIntakeForm = pcIntakeFormService.getPCIntakeForm(intakeFormNumber, inquiryNumber);
    	log.info("PCIntakeForm : " + pcIntakeForm);
    	if (pcIntakeForm != null) {
    		return new ResponseEntity<>(pcIntakeForm, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given Intake Form Number : " + intakeFormNumber + " doesn't exists", HttpStatus.OK);
    	}
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Update PCIntakeForm") // label for swagger
    @PatchMapping("/{intakeFormNumber}")
	public ResponseEntity<?> patchPCIntakeForm(@PathVariable String intakeFormNumber,
			@Valid @RequestBody UpdatePCIntakeForm updatePCIntakeForm, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
    	log.info("updatePCIntakeForm ------> : " + updatePCIntakeForm);
    	PCIntakeForm updatedPCIntakeForm =
				pcIntakeFormService.updatePCIntakeForm(intakeFormNumber, updatePCIntakeForm, loginUserID);
        if (updatedPCIntakeForm.getStatusId() == 9L) {
            String assignedUserId = inquiryRepository.getAssignedUserId(updatedPCIntakeForm.getInquiryNumber());
            this.notificationService.saveNotifications(
                    assignedUserId != null ? Arrays.asList(assignedUserId) : new ArrayList<>(),
                    null, "Intake " + updatedPCIntakeForm.getIntakeFormNumber() + " has been received on ",
                    "Intake",
                    updatedPCIntakeForm.getUpdatedOn(), updatedPCIntakeForm.getUpdatedBy());
        }
        return new ResponseEntity<>(updatedPCIntakeForm, HttpStatus.OK);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Delete PCIntakeForm") // label for swagger
	@DeleteMapping("/{intakeFormNumber}/inquiryNumber/{inquiryNumber}")
	public ResponseEntity<?> deletePCIntakeForm(@PathVariable String intakeFormNumber,
												@PathVariable String inquiryNumber,
												@RequestParam String loginUserID) {
    	pcIntakeFormService.deletePCIntakeForm(intakeFormNumber, inquiryNumber, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    //------------------------------------Reports------------------------------------------------------------------
    @ApiOperation(response = PCIntakeForm.class, value = "PCIntakeForm Report") // label for swagger
    @PostMapping("/report")
    public List<LeadConversionReport> findPCIntakeFormReport(@RequestBody SearchPCIntakeFormReport searchPCIntakeFormReport)
    		throws Exception {
    	List<LeadConversionReport> reportList = pcIntakeFormService.getPCIntakeFormReport(searchPCIntakeFormReport);
    	log.info("LeadConversionReport : " + reportList);
    	return reportList;
	}
}