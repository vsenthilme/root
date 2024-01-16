package com.mnrclara.wrapper.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.model.crm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.report.LeadConversionReport;
import com.mnrclara.wrapper.core.model.report.ReferralReport;
import com.mnrclara.wrapper.core.model.report.SearchPCIntakeFormReport;
import com.mnrclara.wrapper.core.model.report.SearchPotentialClientReport;
import com.mnrclara.wrapper.core.model.report.SearchReferralReport;
import com.mnrclara.wrapper.core.service.CRMService;
import com.mnrclara.wrapper.core.service.RegisterService;
import com.mnrclara.wrapper.core.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-crm-service")
@Api(tags = {"CRM Service"}, value = "CRM Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to CRM Modules")})
public class CRMServiceController {

	@Autowired
	CRMService crmService;

	@Autowired
	ReportService reportService;

	//-----------------------------------Inquiry--------------------------------------------------------------------
    @ApiOperation(response = Inquiry.class, value = "Get all Inquiry details") // label for swagger
	@GetMapping("/inquiry")
	public ResponseEntity<?> getInquirys(@RequestParam String authToken) {
		Inquiry[] userTypeList = crmService.getInquiries(authToken);
		return new ResponseEntity<>(userTypeList, HttpStatus.OK);
	}

    @ApiOperation(response = Inquiry.class, value = "Get a Inquiry") // label for swagger 
	@GetMapping("/inquiry/{inquiryNumber}")
	public ResponseEntity<?> getInquiry(@PathVariable String inquiryNumber, @RequestParam String authToken) {
    	Inquiry objInquiry = crmService.getInquiry(inquiryNumber, authToken);
    	log.info("Inquiry : " + objInquiry);
		return new ResponseEntity<>(objInquiry, HttpStatus.OK);
	}

    @ApiOperation(response = Long.class, value = "Get a Inquiry Count ") // label for swagger 
   	@GetMapping("/inquiry/{classId}/count")
   	public ResponseEntity<?> getInquiryCunt(@PathVariable Long classId, @RequestParam String authToken) {
       	InquiryClassCount inquiryCount = crmService.getInquiryCount(classId, authToken);
       return new ResponseEntity<>(inquiryCount, HttpStatus.OK);
   }

   @ApiOperation(response = Inquiry.class, value = "Search Inquiry") // label for swagger
   @PostMapping("/inquiry/findInquiry")
   public Inquiry[] findInquiry(@RequestBody SearchInquiry searchInquiry, @RequestParam String authToken) throws Exception {
   		return crmService.findInquiries(searchInquiry, authToken);
   	}

    @ApiOperation(response = Inquiry.class, value = "Create Inquiry") // label for swagger
	@PostMapping("/inquiry")
	public ResponseEntity<?> postInquiry(@Valid @RequestBody Inquiry newInquiry,
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		Inquiry createdInquiry = crmService.createInquiry(newInquiry, loginUserID, authToken);
		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
	}

    @ApiOperation(response = Inquiry.class, value = "Create Inquiry from Website") // label for swagger
	@PostMapping("/inquiry/external")
	public ResponseEntity<?> postInquiryWebsite(@Valid @RequestBody AddInquiryWebsite newInquiryWebsite, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		Inquiry createdInquiry = crmService.createInquiryWebsite(newInquiryWebsite, authToken);
		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
	}

    @ApiOperation(response = Inquiry.class, value = "Update Inquiry") // label for swagger
    @PatchMapping("/inquiry")
	public ResponseEntity<?> patchInquiry(@RequestParam String inquiryNumber,
			@RequestParam String loginUserID,
			@Valid @RequestBody Inquiry updateInquiry,
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		Inquiry updatedInquiry = crmService.updateInquiry(inquiryNumber, loginUserID, updateInquiry, authToken);
		return new ResponseEntity<>(updatedInquiry , HttpStatus.OK);
	}

    @ApiOperation(response = Inquiry.class, value = "Assign Inquiry") // label for swagger
    @PatchMapping("/inquiry/assignInquiry")
   	public ResponseEntity<?> assignInquiry(@RequestParam String inquiryNumber,
			@RequestParam String loginUserID,
			@Valid @RequestBody Inquiry updateInquiry,
			@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		Inquiry createdInquiry = crmService.assignInquiry(inquiryNumber, loginUserID, updateInquiry, authToken);
   		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
   	}

    @ApiOperation(response = Inquiry.class, value = "Update Assign Inquiry") // label for swagger
    @PatchMapping("/inquiry/{inquiryNumber}/assignInquiry")
   	public ResponseEntity<?> patchAssignInquiry(@PathVariable String inquiryNumber,
												@RequestParam String loginUserID,
												@Valid @RequestBody Inquiry updateInquiry,
												@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		Inquiry createdInquiry = crmService.updateAssignInquiry(inquiryNumber, loginUserID, updateInquiry, authToken);
   		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
   	}

    @ApiOperation(response = Inquiry.class, value = "Update Inquiry Intake") // label for swagger
    @PatchMapping("/inquiry/{inquiryNumber}/updateInquiryIntake")
   	public ResponseEntity<?> updateInquiryIntake(@PathVariable String inquiryNumber,
												@RequestParam String loginUserID,
												@Valid @RequestBody Inquiry updateInquiry,
												@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		Inquiry createdInquiry = crmService.updateInquiryIntake(inquiryNumber, loginUserID, updateInquiry, authToken);
   		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
   	}

    @ApiOperation(response = Inquiry.class, value = "Update Inquiry Validation Status") // label for swagger
    @PatchMapping("/inquiry/{inquiryNumber}/updateValiationStatus")
   	public ResponseEntity<?> patchUpdateValiationStatus(@PathVariable String inquiryNumber,
   														@RequestParam Long status,
														@RequestParam String loginUserID,
														@Valid @RequestBody Inquiry updateInquiry,
														@RequestParam String authToken
														)
   			throws IllegalAccessException, InvocationTargetException {
   		Inquiry createdInquiry = crmService.updateValiationStatus(inquiryNumber, loginUserID, updateInquiry, status, authToken);
   		return new ResponseEntity<>(createdInquiry , HttpStatus.OK);
   	}

    @ApiOperation(response = Inquiry.class, value = "Delete Inquiry") // label for swagger
	@DeleteMapping("/inquiry/{inquiryNumber}")
	public ResponseEntity<?> deleteInquiry(@PathVariable String inquiryNumber,
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	crmService.deleteInquiry(inquiryNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    //-----------------------------------Feedback Form--------------------------------------------------------------------

	@ApiOperation(response = FeedbackForm.class, value = "Get a Feedback Form") // label for swagger
	@GetMapping("/feedbackform/{intakeFormNumber}")
	public ResponseEntity<?> getFeedbackForm(@PathVariable String intakeFormNumber, @RequestParam String authToken) {
		FeedbackForm objFeedbackForm = crmService.getFeedbackForm(intakeFormNumber, authToken);
		log.info("FeedbackForm : " + objFeedbackForm);
		return new ResponseEntity<>(objFeedbackForm, HttpStatus.OK);
	}

	@ApiOperation(response = FeedbackForm.class, value = "Create Feedback Form") // label for swagger
	@PostMapping("/feedbackform/")
	public ResponseEntity<?> postFeedbackForm(@Valid @RequestBody FeedbackForm feedbackForm,
											  @RequestParam String loginUserID,
											  @RequestParam String authToken )
			throws IllegalAccessException, InvocationTargetException {
		FeedbackForm createdFeedbackForm = crmService.createFeedbackForm(feedbackForm, loginUserID, authToken);
		return new ResponseEntity<>(createdFeedbackForm , HttpStatus.OK);
	}

    //-----------------------------------Intake--------------------------------------------------------------------

    @ApiOperation(response = Inquiry.class, value = "Get a Inquiry") // label for swagger 
	@GetMapping("/inquiry/intake/{inquiryNumber}")
	public ResponseEntity<?> getInquiryIntake(@PathVariable String inquiryNumber,
												@RequestParam String authToken) {
    	Inquiry inquiry = crmService.getInquiryIntake(inquiryNumber, authToken);
    	log.info("Inquiry : " + inquiry);
    	if (inquiry != null) {
    		return new ResponseEntity<>(inquiry, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given InquiryNumber : " + inquiryNumber + " doesn't exists", HttpStatus.OK);
    	}
	}

	@ApiOperation(response = Inquiry.class, value = "Update Status") // label for swagger
    @PatchMapping("/inquiry/intake/updateStatus")
   	public ResponseEntity<?> updateStatus(@RequestParam String inquiryNumber,
			@RequestParam String loginUserID, @Valid @RequestBody Inquiry updateInquiry,
			 @RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		PCIntakeForm createdPCIntakeForm = crmService.afterIntakeFormSent(inquiryNumber, loginUserID, updateInquiry, authToken);
   		return new ResponseEntity<>(createdPCIntakeForm , HttpStatus.OK);
   	}

	@ApiOperation(response = Inquiry.class, value = "Send IntakeForm thro Email") // label for swagger
    @PostMapping("/inquiry/intake/sendFormThroEmail")
   	public ResponseEntity<?> sendFormThroEmail(@Valid @RequestBody EMail email, @RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		crmService.sendEmail(email, authToken);
   		return new ResponseEntity<>(HttpStatus.OK);
   	}

	//----------------------------------PC-INTAKE-FORM----------------------------------------------------------------

	@ApiOperation(response = PCIntakeForm.class, value = "Get all PCIntakeForm details") // label for swagger
	@GetMapping("/pcIntakeForm")
	public ResponseEntity<?> getPCIntakeFormList(@RequestParam String authToken) {
		PCIntakeForm[] pcIntakeFormList = crmService.getPCIntakeForms(authToken);
		return new ResponseEntity<>(pcIntakeFormList, HttpStatus.OK);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Get a PCIntakeForm") // label for swagger 
	@GetMapping("/pcIntakeForm/{intakeFormNumber}")
	public ResponseEntity<?> getPCIntakeForm(@PathVariable String intakeFormNumber,
												@RequestParam String authToken) {
    	PCIntakeForm pcIntakeForm = crmService.getPCIntakeForm(intakeFormNumber, authToken);
    	log.info("PCIntakeForm : " + pcIntakeForm);
    	if (pcIntakeForm != null) {
    		return new ResponseEntity<>(pcIntakeForm, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given Intake Form Number : " + intakeFormNumber + " doesn't exists", HttpStatus.OK);
    	}
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Find Inquiry") // label for swagger
    @PostMapping("/pcIntakeForm/findPCIntakeForm")
    public PCIntakeForm[] findPCIntakeForm(@RequestBody SearchPCIntakeForm searchPCIntakeForm, @RequestParam String authToken) throws Exception {
		return crmService.findPCIntakeForm(searchPCIntakeForm, authToken);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Get a PCIntakeForm") // label for swagger 
    @GetMapping("/pcIntakeForm/{intakeFormNumber}/inquiryNumber/{inquiryNumber}")
   	public ResponseEntity<?> getPCIntakeFormByInquiryNumber(@PathVariable String intakeFormNumber,
   												@PathVariable String inquiryNumber,
   												@RequestParam String authToken){
    	PCIntakeForm pcIntakeForm = crmService.getPCIntakeForm(intakeFormNumber, inquiryNumber, authToken);
    	log.info("PCIntakeForm : " + pcIntakeForm);
    	if (pcIntakeForm != null) {
    		return new ResponseEntity<>(pcIntakeForm, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<>("The given Intake Form Number : " + intakeFormNumber + " doesn't exists", HttpStatus.OK);
    	}
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Update PCIntakeForm") // label for swagger
    @PatchMapping("/pcIntakeForm/{intakeFormNumber}")
	public ResponseEntity<?> patchPCIntakeForm(@PathVariable String intakeFormNumber,
												@Valid @RequestBody UpdatePCIntakeForm updatePCIntakeForm,
												@RequestParam String loginUserID,
												@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		PCIntakeForm updatedPCIntakeForm =
				crmService.updatePCIntakeForm(intakeFormNumber, updatePCIntakeForm, loginUserID, authToken);
		return new ResponseEntity<>(updatedPCIntakeForm , HttpStatus.OK);
	}

    @ApiOperation(response = PCIntakeForm.class, value = "Delete PCIntakeForm") // label for swagger
	@DeleteMapping("/pcIntakeForm/{intakeFormNumber}/inquiryNumber/{inquiryNumber}")
	public ResponseEntity<?> deletePCIntakeForm(@PathVariable String intakeFormNumber,
												@PathVariable String inquiryNumber,
												@RequestParam String loginUserID,
												@RequestParam String authToken) {
    	crmService.deletePCIntakeForm(intakeFormNumber, inquiryNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    //-----------------------------------POTENTIAL-CLIENT----------------------------------------------------------------

    @ApiOperation(response = PotentialClient.class, value = "Get all PotentialClient details") // label for swagger
   	@GetMapping("/potentialClient")
   	public ResponseEntity<?> getPotentialClients(@RequestParam String authToken) {
   		PotentialClient[] potentialClientList = crmService.getPotentialClients(authToken);
   		return new ResponseEntity<>(potentialClientList, HttpStatus.OK);
   	}

    @ApiOperation(response = PotentialClient.class, value = "Get a PotentialClient") // label for swagger 
   	@GetMapping("/potentialClient/{potentialClientId}")
   	public ResponseEntity<?> getPotentialClient(@PathVariable String potentialClientId, @RequestParam String authToken) {
       	PotentialClient potentialClient = crmService.getPotentialClient (potentialClientId, authToken);
       	log.info("PotentialClient : " + potentialClient);
       	if (potentialClient != null) {
       		return new ResponseEntity<>(potentialClient, HttpStatus.OK);
       	} else {
       		return new ResponseEntity<>("The given Potetnial ClientId : " + potentialClientId + " doesn't exists", HttpStatus.OK);
       	}
   	}

    @ApiOperation(response = PotentialClient.class, value = "Find PotentialClient") // label for swagger
    @PostMapping("/potentialClient/findPotentialClient")
    public PotentialClient[] findPotentialClient(@RequestBody SearchPotentialClient searchPotentialClient,
		   @RequestParam String authToken) throws Exception {
    	return crmService.findPotentialClient(searchPotentialClient, authToken);
   	}

   	@ApiOperation(response = PotentialClient.class, value = "Create Client General") // label for swagger
   	@GetMapping("/potentialClient/{potentialClientId}/clientGeneral")
   	public ResponseEntity<?> getClientGeneral(@PathVariable String potentialClientId,
									   			@RequestParam String loginUserID,
									   			@RequestParam String authToken )
   			throws IllegalAccessException, InvocationTargetException {
   		com.mnrclara.wrapper.core.model.management.ClientGeneral createdClientGeneral = crmService.createClientGeneral(potentialClientId, loginUserID, authToken);
   		return new ResponseEntity<>(createdClientGeneral , HttpStatus.OK);
   	}

   	@ApiOperation(response = PotentialClient.class, value = "Update PotentialClient") // label for swagger
   	@PatchMapping("/potentialClient/{potentialClientId}")
   	public ResponseEntity<?> patchPotentialClient(@PathVariable String potentialClientId,
										   			@RequestParam String loginUserID,
										   			@Valid @RequestBody UpdatePotentialClient updatePotentialClient,
										   			@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		PotentialClient updatedPotentialClient =
   				crmService.updatePotentialClient(potentialClientId, loginUserID, updatePotentialClient, authToken);
   		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
   	}

   	@ApiOperation(response = PotentialClient.class, value = "Update PotentialClient Agreement") // label for swagger
   	@PatchMapping("/potentialClient/{potentialClientId}/agreement")
   	public ResponseEntity<?> patchPotentialClientAgreement(@PathVariable String potentialClientId,
										   			@RequestParam String loginUserID,
										   			@Valid @RequestBody UpdatePotentialClientAgreement updatePotentialClientAgreement,
										   			@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		PotentialClient updatedPotentialClient =
   				crmService.updatePotentialClientAgreement(potentialClientId, loginUserID, updatePotentialClientAgreement, authToken);
   		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
   	}

   	@ApiOperation(response = PotentialClient.class, value = "Update PotentialClient") // label for swagger
   	@PatchMapping("/potentialClient/{potentialClientId}/status")
   	public ResponseEntity<?> patchPotentialClient(@PathVariable String potentialClientId,
										   			@RequestParam String loginUserID,
										   			@RequestParam Long status,
										   			@RequestParam String authToken)
   			throws IllegalAccessException, InvocationTargetException {
   		PotentialClient updatedPotentialClient =
   				crmService.updatePotentialClientStatus(potentialClientId, loginUserID, status, authToken);
   		return new ResponseEntity<>(updatedPotentialClient , HttpStatus.OK);
   	}

   	@ApiOperation(response = PotentialClient.class, value = "Delete PotentialClient") // label for swagger
   	@DeleteMapping("/potentialClient/{potentialClientId}")
   	public ResponseEntity<?> deletePotentialClient(@PathVariable String potentialClientId,
   													@RequestParam String loginUserID,
   													@RequestParam String authToken) {
       	String response = crmService.deletePotentialClient(potentialClientId, loginUserID, authToken);
       	log.info("response delete : " + response);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

   	//----------------------------------AGREEMENT-----------------------------------------------------------------------------

    @ApiOperation(response = Agreement.class, value = "Get all Agreement details") // label for swagger
  	@GetMapping("/agreement")
  	public ResponseEntity<?> getAgreements(@RequestParam String authToken) {
  		Agreement[] agreementList = crmService.getAgreements(authToken);
  		return new ResponseEntity<>(agreementList, HttpStatus.OK);
  	}

    @ApiOperation(response = Agreement.class, value = "Get a Agreement") // label for swagger 
  	@GetMapping("/agreement/{agreementCode}")
  	public ResponseEntity<?> getAgreement(@PathVariable String agreementCode, @RequestParam String authToken) {
      	Agreement agreement = crmService.getAgreement(agreementCode, authToken);
      	log.info("Agreement : " + agreement);
      	if (agreement != null) {
      		return new ResponseEntity<>(agreement, HttpStatus.OK);
      	} else {
      		return new ResponseEntity<>("The given AgreementCode : " + agreementCode + " doesn't exists", HttpStatus.OK);
      	}
  	}

    @ApiOperation(response = PotentialClient.class, value = "Find Agreement") // label for swagger
    @PostMapping("/agreement/findAgreement")
    public Agreement[] findAgreement(@RequestBody SearchAgreement searchAgreement, @RequestParam String authToken) {
  		return crmService.findAgreement(searchAgreement, authToken);
  	}

    @ApiOperation(response = Agreement.class, value = "Create Agreement") // label for swagger
  	@PostMapping("/agreement")
  	public ResponseEntity<?> postAgreement(@RequestBody AddAgreement addAgreement,
								  			@RequestParam String potentialClientId,
								  			@RequestParam String loginUserID,
								  			@RequestParam String authToken)
  			throws IllegalAccessException, InvocationTargetException {
  		Agreement createdAgreement = crmService.createAgreement(addAgreement, potentialClientId, loginUserID, authToken);
  		return new ResponseEntity<>(createdAgreement , HttpStatus.OK);
  	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement") // label for swagger
    @PatchMapping("/agreement/{agreementCode}")
  	public ResponseEntity<?> patchAgreement(@PathVariable String agreementCode,
								  			@Valid @RequestBody UpdateAgreement updateAgreement,
								  			@RequestParam String loginUserID,
								  			@RequestParam String authToken)
  			throws IllegalAccessException, InvocationTargetException {
  		Agreement updatedAgreement = crmService.updateAgreement(agreementCode, updateAgreement, loginUserID, authToken);
  		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
  	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement") // label for swagger
    @PatchMapping("/agreement/{agreementCode}/fromDocusign")
  	public ResponseEntity<?> patchAgreementFromDocusign(@PathVariable String agreementCode,
											  			@RequestParam String potentialClientId,
											  			@Valid @RequestBody UpdateAgreement updateAgreement,
											  			@RequestParam String loginUserID,
											  			@RequestParam String authToken)
  			throws IllegalAccessException, InvocationTargetException {
  		Agreement updatedAgreement =
  				crmService.updateAgreementFromDocusignFlow(agreementCode, potentialClientId, updateAgreement, loginUserID, authToken);
  		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
  	}

    @ApiOperation(response = Agreement.class, value = "Update Agreement Status") // label for swagger
    @PatchMapping("/agreement/{agreementCode}/status")
  	public ResponseEntity<?> patchAgreement(@RequestParam String agreementCode,
								  			@RequestParam Long status,
								  			@RequestParam String loginUserID,
								  			@RequestParam String authToken)
  			throws IllegalAccessException, InvocationTargetException {
  		Agreement updatedAgreement = crmService.updateAgreementStatus(agreementCode, status, loginUserID, authToken);
  		return new ResponseEntity<>(updatedAgreement , HttpStatus.OK);
  	}

    @ApiOperation(response = Agreement.class, value = "Delete Agreement") // label for swagger
  	@DeleteMapping("/agreement/{agreementCode}")
  	public ResponseEntity<?> deleteAgreement(@PathVariable String agreementCode,
  												@RequestParam String loginUserID,
  												@RequestParam String authToken) {
      	crmService.deleteAgreement(agreementCode, loginUserID, authToken);
  		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  	}

    @ApiOperation(response = Optional.class, value = "DocuSign Status") // label for swagger
   	@GetMapping("/agreement/docusign/envelope/status")
   	public ResponseEntity<?> getStatusFromDocusign(@RequestParam String potentialClientId,
   			@RequestParam String authToken) throws Exception {
    	EnvelopeStatus response = crmService.getDocusignEnvelopeStatus(potentialClientId, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

    @ApiOperation(response = Optional.class, value = "DocuSign Download") // label for swagger
   	@GetMapping("/agreement/docusign/envelope/download")
   	public ResponseEntity<?> downloadEnvelopeFromDocusign(@RequestParam String potentialClientId,
   			@RequestParam String loginUserID,
   			@RequestParam String authToken) throws Exception {
    	String response = crmService.downloadEnvelopeFromDocusign(potentialClientId, loginUserID, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
   	@GetMapping("/agreement/docusign/token")
   	public ResponseEntity<?> getToken(@RequestParam String code, @RequestParam String authToken)
   			throws Exception {
   		AuthToken auth = crmService.genToken(code, authToken);
   		return new ResponseEntity<>(auth, HttpStatus.OK);
   	}

	//----------------------------------Conflict Search-----------------------------------------------------------------------------

   	@ApiOperation(response = Optional.class, value = "Conflict Search") // label for swagger
   	@GetMapping("/conflictSearch")
   	public ResponseEntity<?> conflictSearch(@RequestParam List<String> searchTableNames,
   			@RequestParam String searchFieldValue,
   			@RequestParam String authToken)
   			throws Exception {
   		ConflictSearchResult response = crmService.conflictSearch(searchTableNames, searchFieldValue, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}

   	/*---------------------Reports--------------------------------------------------------*/
    @ApiOperation(response = PotentialClient[].class, value = "PotentialClient Report") // label for swagger
    @PostMapping("/potentialClient/report")
    public PotentialClient[] getPotentialClientReport(@RequestBody SearchPotentialClientReport searchPotentialClientReport,
    		@RequestParam String authToken) throws ParseException {
		return reportService.getPotentialClientReport(searchPotentialClientReport, authToken);
	}

    @ApiOperation(response = LeadConversionReport[].class, value = "Lead Conversion Report") // label for swagger
    @PostMapping("/pcIntakeForm/leadConversionReport")
    public LeadConversionReport[] findPotentialClientReport(@RequestBody SearchPCIntakeFormReport searchPCIntakeFormReport,
    		@RequestParam String authToken) throws ParseException {
    	LeadConversionReport[] reportList = reportService.getPCIntakeFormReport(searchPCIntakeFormReport, authToken);
    	log.info("LeadConversionReport : " + Arrays.asList(reportList));
    	return reportList;
	}

    @ApiOperation(response = ReferralReport[].class, value = "Referral report") // label for swagger
    @PostMapping("/potentialClient/referralReport")
    public ReferralReport[] getReferralReport(@RequestBody SearchReferralReport searchReferralReport,
    		@RequestParam String authToken) throws ParseException {
		return reportService.getReferralReport(searchReferralReport, authToken);
	}


	@ApiOperation(response = Notification[].class, value = "Notification List") // label for swagger
	@GetMapping("/notification-message")
	public Notification[] getAllNotifications(@RequestParam String userId, @RequestParam String authToken) throws ParseException {
		return crmService.getAllNotifications(userId, authToken);
	}

	@ApiOperation(response = Notification.class, value = "Mark Read Notification") // label for swagger
	@GetMapping("/notification-message/mark-read/{id}")
	public ResponseEntity<?> markNotificationRead(@RequestParam String loginUserID,@PathVariable Long id,@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		Boolean result = crmService.updateNotificationAsRead(loginUserID, id, authToken);
		return new ResponseEntity<>(result , HttpStatus.OK);
	}
	@ApiOperation(response = Notification.class, value = "Mark Read All Notification") // label for swagger
	@GetMapping("/notification-message/mark-read-all")
	public ResponseEntity<?> markNotificationReadAll(@RequestParam String loginUserID,@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		Boolean result = crmService.updateNotificationAsReadAll(loginUserID, authToken);
		return new ResponseEntity<>(result , HttpStatus.OK);
	}

}