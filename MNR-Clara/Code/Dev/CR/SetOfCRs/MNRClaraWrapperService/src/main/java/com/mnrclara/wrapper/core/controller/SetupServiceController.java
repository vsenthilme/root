package com.mnrclara.wrapper.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.model.setup.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.User;
import com.mnrclara.wrapper.core.model.setup.ActivityCode;
import com.mnrclara.wrapper.core.model.setup.AdminCost;
import com.mnrclara.wrapper.core.model.setup.AgreementTemplate;
import com.mnrclara.wrapper.core.model.setup.AuditLog;
import com.mnrclara.wrapper.core.model.setup.BillingFormat;
import com.mnrclara.wrapper.core.model.setup.BillingFrequency;
import com.mnrclara.wrapper.core.model.setup.BillingMode;
import com.mnrclara.wrapper.core.model.setup.CaseCategory;
import com.mnrclara.wrapper.core.model.setup.CaseSubcategory;
import com.mnrclara.wrapper.core.model.setup.ChartOfAccounts;
import com.mnrclara.wrapper.core.model.setup.ClientCategory;
import com.mnrclara.wrapper.core.model.setup.ClientType;
import com.mnrclara.wrapper.core.model.setup.ClientUser;
import com.mnrclara.wrapper.core.model.setup.Company;
import com.mnrclara.wrapper.core.model.setup.DeadlineCalculator;
import com.mnrclara.wrapper.core.model.setup.DocCheckList;
import com.mnrclara.wrapper.core.model.setup.DocumentTemplate;
import com.mnrclara.wrapper.core.model.setup.DocumentTemplateCompositeKey;
import com.mnrclara.wrapper.core.model.setup.*;
import com.mnrclara.wrapper.core.model.setup.EMail;
import com.mnrclara.wrapper.core.model.setup.ExpenseCode;
import com.mnrclara.wrapper.core.model.setup.ExpirationDocType;
import com.mnrclara.wrapper.core.model.setup.GlMappingMaster;
import com.mnrclara.wrapper.core.model.setup.InquiryMode;
import com.mnrclara.wrapper.core.model.setup.IntakeFormID;
import com.mnrclara.wrapper.core.model.setup.Language;
import com.mnrclara.wrapper.core.model.setup.Message;
import com.mnrclara.wrapper.core.model.setup.NoteType;
import com.mnrclara.wrapper.core.model.setup.Notification;
import com.mnrclara.wrapper.core.model.setup.NumberRange;
import com.mnrclara.wrapper.core.model.setup.Referral;
import com.mnrclara.wrapper.core.model.setup.Screen;
import com.mnrclara.wrapper.core.model.setup.SearchClientUser;
import com.mnrclara.wrapper.core.model.setup.SearchDocCheckList;
import com.mnrclara.wrapper.core.model.setup.Status;
import com.mnrclara.wrapper.core.model.setup.TaskType;
import com.mnrclara.wrapper.core.model.setup.TaskbasedCode;
import com.mnrclara.wrapper.core.model.setup.TimekeeperCode;
import com.mnrclara.wrapper.core.model.setup.Transaction;
import com.mnrclara.wrapper.core.model.setup.UpdateDocCheckList;
import com.mnrclara.wrapper.core.model.setup.UserProfile;
import com.mnrclara.wrapper.core.model.setup.UserRole;
import com.mnrclara.wrapper.core.model.setup.UserType;
import com.mnrclara.wrapper.core.service.RegisterService;
import com.mnrclara.wrapper.core.service.SetupService;
import com.mnrclara.wrapper.core.util.CommonUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-setup-service")
@Api(tags = { "Setup Services" }, value = "Setup Services") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "Setup", description = "Operations related to SetupService") })
public class SetupServiceController {

	@Autowired
	SetupService setupService;

	@Autowired
	RegisterService registerService;

	/*
	 * --------------------------------LOGIN----------------------------------------
	 */

	/**
	 * This end point will be called for Web App User Login
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	@ApiOperation(response = Optional.class, value = "Login User") // label for swagger
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> loginUser(@RequestParam String userId, @RequestParam String password,
			@RequestParam String authToken) {
		try {
			UserProfile loggedInUser = setupService.validateUser(userId, password, authToken);
			log.info("LoginUser::: " + loggedInUser);
			return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			e.printStackTrace();
			CommonUtils commonUtils = new CommonUtils();
			Map<String, String> mapError = commonUtils.prepareErrorResponse(e.getLocalizedMessage());
			return new ResponseEntity<>(mapError, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@ApiOperation(response = UserProfile.class, value = "Send EmailOTP") 			// label for swagger
	@GetMapping("/login/emailOTP")
	public ResponseEntity<?> emailOTP (@RequestParam String userId, @RequestParam String authToken) {
    	boolean response = setupService.emailOTP(userId, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Send Verify EmailOTP") 			// label for swagger
	@GetMapping("/login/verifyEmailOTP")
	public ResponseEntity<?> verifyEmailOTP (@RequestParam String userId, @RequestParam Long otp,
			@RequestParam String authToken) {
    	UserProfile response = setupService.verifyEmailOTP(userId, otp, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "Verify OTP") // label for swagger
	@RequestMapping(value = "/login/clientUser/verifyOTP", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> loginUser(@RequestParam String contactNumber, @RequestParam Long otp,
			@RequestParam String authToken) {
		try {
			ClientUser clientUser = setupService.verifyOtp(contactNumber, otp, authToken);
			return new ResponseEntity<>(clientUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			e.printStackTrace();
			CommonUtils commonUtils = new CommonUtils();
			Map<String, String> mapError = commonUtils.prepareErrorResponse(e.getLocalizedMessage());
			return new ResponseEntity<>(mapError, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@ApiOperation(response = UserProfile.class, value = "Validate Login User") // label for swagger
	@GetMapping("/login/clientUser/sendOTP")
	public ResponseEntity<?> sendOTP(@RequestParam String contactNumber, @RequestParam String authToken) {
    	Boolean response = setupService.sendOTP(contactNumber, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//----------------------------------CR-------------------------------------------------------
	
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
   	@GetMapping("/login/clientUser/emailOTP")
   	public ResponseEntity<?> clientUserEmailOTP (@RequestParam String emailId, @RequestParam String authToken) {
       	boolean response = setupService.clientUserEmailOTP(emailId, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Send OTP") 			// label for swagger
	@GetMapping("/login/clientUser/verifyEmailOTP")
	public ResponseEntity<?> clientUserVerifyEmailOTP (@RequestParam String emailId, @RequestParam Long otp,
			@RequestParam String authToken) {
    	ClientUser response = setupService.clientUserVerifyEmailOTP(emailId, otp, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/*
	 * --------------------------------ActivityCode---------------------------------
	 */

	@ApiOperation(response = ActivityCode.class, value = "Get all ActivityCode details") // label for swagger
	@GetMapping("/activityCode")
	public ResponseEntity<?> getActivityCodes(@RequestParam String authToken) {
		ActivityCode[] activityCodeList = setupService.getActivityCodes(authToken);
		return new ResponseEntity<>(activityCodeList, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Get a ActivityCode") // label for swagger
	@GetMapping("/activityCode/{activityCodeId}")
	public ResponseEntity<?> getActivityCode(@PathVariable String activityCodeId, @RequestParam String authToken) {
		ActivityCode dbActivityCode = setupService.getActivityCode(activityCodeId, authToken);
		log.info("ActivityCode : " + dbActivityCode);
		return new ResponseEntity<>(dbActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Create ActivityCode") // label for swagger
	@PostMapping("/activityCode")
	public ResponseEntity<?> postActivityCode(@Valid @RequestBody ActivityCode newActivityCode, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		ActivityCode createdActivityCode = setupService.createActivityCode(newActivityCode, loginUserID, authToken);
		return new ResponseEntity<>(createdActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Update ActivityCode") // label for swagger
	@RequestMapping(value = "/activityCode", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchActivityCode(@RequestParam String activityCodeId, @RequestParam String loginUserID,
			@RequestParam String authToken,
			@Valid @RequestBody ActivityCode updateActivityCode)
			throws IllegalAccessException, InvocationTargetException {
		ActivityCode updatedActivityCode = setupService.updateActivityCode(activityCodeId, loginUserID, updateActivityCode, authToken);
		return new ResponseEntity<>(updatedActivityCode, HttpStatus.OK);
	}

	@ApiOperation(response = ActivityCode.class, value = "Delete ActivityCode") // label for swagger
	@DeleteMapping("/activityCode/{activityCodeId}")
	public ResponseEntity<?> deleteActivityCode(@PathVariable String activityCodeId,
			@RequestParam String loginUserID, @RequestParam String authToken) {
		setupService.deleteActivityCode(activityCodeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// -----------------------ADMINCOST----------------------------------------------------------------------------
	@ApiOperation(response = AdminCost.class, value = "Get all AdminCost details") // label for swagger
	@GetMapping("/adminCost")
	public ResponseEntity<?> getAdminCosts(@RequestParam String authToken) {
		AdminCost[] adminCostList = setupService.getAdminCosts(authToken);
		return new ResponseEntity<>(adminCostList, HttpStatus.OK);
	}

	@ApiOperation(response = AdminCost.class, value = "Get a AdminCost") // label for swagger
	@GetMapping("/adminCost/{adminCostId}")
	public ResponseEntity<?> getAdminCost(@PathVariable Long adminCostId, @RequestParam String authToken) {
		AdminCost adminCost = setupService.getAdminCost(adminCostId, authToken);
		log.info("AdminCost : " + adminCost);
		return new ResponseEntity<>(adminCost, HttpStatus.OK);
	}

	@ApiOperation(response = AdminCost.class, value = "Create AdminCost") // label for swagger
	@PostMapping("/adminCost")
	public ResponseEntity<?> createAdminCost(@Valid @RequestBody AdminCost newAdminCost, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		AdminCost createdAdminCost = setupService.createAdminCost(newAdminCost, loginUserID, authToken);
		return new ResponseEntity<>(createdAdminCost, HttpStatus.OK);
	}

	@ApiOperation(response = AdminCost.class, value = "Update AdminCost") // label for swagger
	@PatchMapping("/adminCost")
	public ResponseEntity<?> patchAdminCost(@RequestParam Long adminCostId, @RequestParam String loginUserID,
			@RequestParam String authToken, @RequestBody AdminCost updateAdminCost) 
					throws IllegalAccessException, InvocationTargetException {
		AdminCost updatedAdminCost = setupService.updateAdminCost(adminCostId, loginUserID, updateAdminCost, authToken);
		return new ResponseEntity<>(updatedAdminCost, HttpStatus.OK);
	}

	@ApiOperation(response = AdminCost.class, value = "Delete AdminCost") // label for swagger
	@DeleteMapping("/adminCost/{adminCostId}")
	public ResponseEntity<?> deleteAdminCost(@PathVariable Long adminCostId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		setupService.deleteAdminCost(adminCostId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// -----------------------AGREEMENT-TEMPLATE-------------------------------------------------------
	@ApiOperation(response = AgreementTemplate.class, value = "Get all AgreementTemplate details") // label for swagger
	@GetMapping("/agreementTemplate")
	public ResponseEntity<?> getAgreementTemplates(@RequestParam String authToken) {
		AgreementTemplate[] aggrementTemplateList = setupService.getAgreementTemplates(authToken);
		return new ResponseEntity<>(aggrementTemplateList, HttpStatus.OK);
	}

	@ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger
	@GetMapping("/agreementTemplate/{agreementCode}")
	public ResponseEntity<?> getAgreementTemplate(@PathVariable String agreementCode, @RequestParam String authToken) {
		AgreementTemplate aggrementTemplate = setupService.getAgreementTemplate(agreementCode, authToken);
		log.info("AgreementTemplate : " + aggrementTemplate);
		return new ResponseEntity<>(aggrementTemplate, HttpStatus.OK);
	}

	@ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger
	@GetMapping("/agreementTemplate/{agreementCode}/documentUrl")
	public ResponseEntity<?> getAgreementTemplateDocURL(@PathVariable String agreementCode, @RequestParam String authToken) {
		AgreementTemplate aggrementTemplate = setupService.getAgreementTemplateDocument(agreementCode, authToken);
		log.info("DocURL : " + aggrementTemplate.getAgreementUrl());
		log.info("MailMerge : " + aggrementTemplate.getMailMerge());
		Map<String, String> kayValueMap = new HashMap<>();
		kayValueMap.put("documentUrl", aggrementTemplate.getAgreementUrl());
		kayValueMap.put("mailMerge", String.valueOf(aggrementTemplate.getMailMerge()));
		return new ResponseEntity<>(kayValueMap, HttpStatus.OK);
	}

	// Pass CLASS_ID/CASE_CATEGORY_ID in AGREEMENTTEMPLATE table and fetch
	// AGREEMENT_CODE/AGREEMENT_TEXT values where AGREEMENT_STATUS = ACTIVE and display in dropdown as Combination
	@ApiOperation(response = AgreementTemplate.class, value = "Get a AgreementTemplate") // label for swagger
	@GetMapping("/agreementTemplate/{agreementCode}/classId/{classId}")
	public ResponseEntity<?> getAgreementTemplateByClassIdAndagreementCode(@PathVariable String agreementCode,
			@PathVariable Long classId, @RequestParam String authToken) {
		AgreementTemplate aggrementTemplate = setupService.getAgreementTemplateClassId(agreementCode, classId, authToken);
		log.info("AgreementTemplate : " + aggrementTemplate);
		return new ResponseEntity<>(aggrementTemplate, HttpStatus.OK);
	}

	@ApiOperation(response = AgreementTemplate.class, value = "Create AgreementTemplate") // label for swagger
	@PostMapping("/agreementTemplate")
	public ResponseEntity<?> createAgreementTemplate(@Valid @RequestBody AgreementTemplate newAgreementTemplate, 
			@RequestParam String loginUserID,
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		AgreementTemplate createdAgreementTemplate = setupService.createAgreementTemplate(newAgreementTemplate, loginUserID, authToken);
		return new ResponseEntity<>(createdAgreementTemplate, HttpStatus.OK);
	}

	@ApiOperation(response = AgreementTemplate.class, value = "Update AgreementTemplate") // label for swagger
	@PatchMapping("/agreementTemplate")
	public ResponseEntity<?> patchAgreementTemplate(@RequestParam String agreementCode,
			@Valid @RequestBody AgreementTemplate updateAgreementTemplate, @RequestParam String loginUserID, 
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		AgreementTemplate updatedAgreementTemplate = setupService.updateAgreementTemplate(agreementCode, loginUserID,
				updateAgreementTemplate, authToken);
		return new ResponseEntity<>(updatedAgreementTemplate, HttpStatus.OK);
	}

	@ApiOperation(response = AgreementTemplate.class, value = "Delete AgreementTemplate") // label for swagger
	@DeleteMapping("/agreementTemplate/{agreementCode}")
	public ResponseEntity<?> deleteAgreementTemplate(@PathVariable String agreementCode, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		setupService.deleteAgreementTemplate(agreementCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//---------------------AUDIT-LOG--------------------------------------------------------------------------
	@ApiOperation(response = AuditLog.class, value = "Get all AuditLog details") // label for swagger
	@GetMapping("/auditLog")
	public ResponseEntity<?> getAuditLogs(@RequestParam String authToken) {
		AuditLog[] auditlogList = setupService.getAuditLogs(authToken);
		return new ResponseEntity<>(auditlogList, HttpStatus.OK);
	}
    
    @ApiOperation(response = AuditLog.class, value = "Get a AuditLog") // label for swagger 
	@GetMapping("/auditLog/{auditLogNumber}")
	public ResponseEntity<?> getAuditLog(@PathVariable String auditLogNumber, @RequestParam String authToken) {
    	AuditLog auditlog = setupService.getAuditLog(auditLogNumber, authToken);
    	log.info("AuditLog : " + auditlog);
		return new ResponseEntity<>(auditlog, HttpStatus.OK);
	}
    
    @ApiOperation(response = AuditLog.class, value = "Create AuditLog") // label for swagger
	@PostMapping("/auditLog")
	public ResponseEntity<?> postAuditLog(@Valid @RequestBody AuditLog newAuditLog, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		AuditLog createdAuditLog = setupService.createAuditLog(newAuditLog, loginUserID, authToken);
		return new ResponseEntity<>(createdAuditLog , HttpStatus.OK);
	}
    
    @ApiOperation(response = AuditLog.class, value = "Update AuditLog") // label for swagger
    @PatchMapping("/auditLog")
	public ResponseEntity<?> patchAuditLog(@RequestParam String auditLogNumber, 
			@Valid @RequestBody AuditLog updateAuditLog, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		AuditLog updatedAuditLog = setupService.updateAuditLog(auditLogNumber, loginUserID, updateAuditLog, authToken);
		return new ResponseEntity<>(updatedAuditLog , HttpStatus.OK);
	}
    
    @ApiOperation(response = AuditLog.class, value = "Delete AuditLog") // label for swagger
	@DeleteMapping("/auditLog/{auditLogNumber}")
	public ResponseEntity<?> deleteAuditLog(@PathVariable String auditLogNumber, @RequestParam String authToken) {
    	setupService.deleteAuditLog(auditLogNumber, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //------------------------BILLING-FORMAT------------------------------------------------------------------
    @ApiOperation(response = BillingFormat.class, value = "Get all BillingFormat details") // label for swagger
   	@GetMapping("/billingFormat")
   	public ResponseEntity<?> getBillingFormats(@RequestParam String authToken) {
   		BillingFormat[] billingFormatList = setupService.getBillingFormats(authToken);
   		return new ResponseEntity<>(billingFormatList, HttpStatus.OK);
   	}
       
    @ApiOperation(response = BillingFormat.class, value = "Get a BillingFormat") // label for swagger 
   	@GetMapping("/billingFormat/{billingFormatId}")
   	public ResponseEntity<?> getBillingFormat(@PathVariable Long billingFormatId, @RequestParam String authToken) {
       	BillingFormat billingFormat = setupService.getBillingFormat(billingFormatId, authToken);
       	log.info("BillingFormat : " + billingFormat);
   		return new ResponseEntity<>(billingFormat, HttpStatus.OK);
   	}
       
//    @ApiOperation(response = BillingFormat.class, value = "Create BillingFormat") // label for swagger
   	@PostMapping("/billingFormat")
   	public ResponseEntity<?> postBillingFormat(@Valid @RequestBody BillingFormat newBillingFormat, 
   			@RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		BillingFormat createdBillingFormat = setupService.createBillingFormat(newBillingFormat, loginUserID, authToken);
   		return new ResponseEntity<>(createdBillingFormat , HttpStatus.OK);
   	}
       
   @ApiOperation(response = BillingFormat.class, value = "Update BillingFormat") // label for swagger
   @PatchMapping("/billingFormat")
   	public ResponseEntity<?> patchBillingFormat(@RequestParam Long billingFormatId, 
   			@Valid @RequestBody BillingFormat updateBillingFormat, @RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		BillingFormat updatedBillingFormat = setupService.updateBillingFormat(billingFormatId, loginUserID, updateBillingFormat, authToken);
   		return new ResponseEntity<>(updatedBillingFormat , HttpStatus.OK);
   	}
       
   	@ApiOperation(response = BillingFormat.class, value = "Delete BillingFormat") // label for swagger
   	@DeleteMapping("/billingFormat/{billingFormatId}")
   	public ResponseEntity<?> deleteBillingFormat(@PathVariable Long billingFormatId, 
   			@RequestParam String loginUserID,
   			@RequestParam String authToken) {
       	setupService.deleteBillingFormat(billingFormatId, loginUserID, authToken);
   		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   	}
   	
   	//------------------------------BILLING_FREQUENCY----------------------------------------------
    @ApiOperation(response = BillingFrequency.class, value = "Get all BillingFrequency details") // label for swagger
   	@GetMapping("/billingFrequency")
   	public ResponseEntity<?> getAll(@RequestParam String authToken) {
   		BillingFrequency[] billingFrequencyList = setupService.getBillingFrequencies(authToken);
   		return new ResponseEntity<>(billingFrequencyList, HttpStatus.OK);
   	}
       
   @ApiOperation(response = BillingFrequency.class, value = "Get a BillingFrequency") // label for swagger 
   	@GetMapping("/billingFrequency/{billingFrequencyId}")
   	public ResponseEntity<?> getBillingFrequency(@PathVariable Long billingFrequencyId, @RequestParam String authToken) {
       	BillingFrequency billingFrequency = setupService.getBillingFrequency(billingFrequencyId, authToken);
       	log.info("BillingFrequency : " + billingFrequency);
   		return new ResponseEntity<>(billingFrequency, HttpStatus.OK);
   	}
       
   	@ApiOperation(response = BillingFrequency.class, value = "Create BillingFrequency") // label for swagger
   	@PostMapping("/billingFrequency")
   	public ResponseEntity<?> postBillingFrequency(@Valid @RequestBody BillingFrequency newBillingFrequency, 
   			@RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		BillingFrequency createdBillingFrequency = setupService.createBillingFrequency(newBillingFrequency, loginUserID, authToken);
   		return new ResponseEntity<>(createdBillingFrequency , HttpStatus.OK);
   	}
       
   	@ApiOperation(response = BillingFrequency.class, value = "Update BillingFrequency") // label for swagger
   	@PatchMapping("/billingFrequency")
   	public ResponseEntity<?> patchBillingFrequency(@RequestParam Long billingFrequencyId, 
   			@Valid @RequestBody BillingFrequency updateBillingFrequency, 
   			@RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		BillingFrequency updatedBillingFrequency = 
   				setupService.updateBillingFrequency(billingFrequencyId, loginUserID, updateBillingFrequency, authToken);
   		return new ResponseEntity<>(updatedBillingFrequency , HttpStatus.OK);
   	}
       
   	@ApiOperation(response = BillingFrequency.class, value = "Delete BillingFrequency") // label for swagger
   	@DeleteMapping("/billingFrequency/{billingFrequencyId}")
   	public ResponseEntity<?> deleteBillingFrequency(@PathVariable Long billingFrequencyId, 
   			@RequestParam String loginUserID, @RequestParam String authToken) {
       	setupService.deleteBillingFrequency(billingFrequencyId, loginUserID, authToken);
   		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   	}
   	
   	//----------------------------BILLING-MODEL------------------------------------------------------------------
    @ApiOperation(response = BillingMode.class, value = "Get all BillingMode details") // label for swagger
	@GetMapping("/billingMode")
	public ResponseEntity<?> getBillingModes(@RequestParam String authToken) {
		BillingMode[] billingModeList = setupService.getBillingModes(authToken);
		return new ResponseEntity<>(billingModeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Get a BillingMode") // label for swagger 
	@GetMapping("/billingMode/{billingModeId}")
	public ResponseEntity<?> getbillingMode(@PathVariable Long billingModeId, @RequestParam String authToken) {
    	BillingMode billingMode = setupService.getBillingMode(billingModeId, authToken);
    	log.info("BillingMode : " + billingMode);
		return new ResponseEntity<>(billingMode, HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Create BillingMode") // label for swagger
	@PostMapping("/billingMode")
	public ResponseEntity<?> postBillingMode(@Valid @RequestBody BillingMode newBillingMode, 
			@RequestParam String loginUserID, 
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode createdBillingMode = setupService.createBillingMode(newBillingMode, loginUserID, authToken);
		return new ResponseEntity<>(createdBillingMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Update BillingMode") // label for swagger
    @PatchMapping("/billingMode/{billingModeId}")
	public ResponseEntity<?> patchBillingMode(@RequestParam Long billingModeId, 
			@Valid @RequestBody BillingMode updateBillingMode, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode updatedBillingMode = setupService.updateBillingMode(billingModeId, loginUserID, updateBillingMode, authToken);
		return new ResponseEntity<>(updatedBillingMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = BillingMode.class, value = "Delete BillingMode") // label for swagger
	@DeleteMapping("/billingMode/{billingModeId}")
	public ResponseEntity<?> deleteBillingMode(@PathVariable Long billingModeId, 
			@RequestParam String loginUserID,
			@RequestParam String authToken) {
    	setupService.deleteBillingMode(billingModeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-------------------CASE-CATEGORY--------------------------------------------------------------------------
    @ApiOperation(response = CaseCategory.class, value = "Get all CaseCategory details") // label for swagger
	@GetMapping("/caseCategory")
	public ResponseEntity<?> getCaseCategories(@RequestParam String authToken) {
		CaseCategory[] caseCategoryList = setupService.getCaseCategories(authToken);
		return new ResponseEntity<>(caseCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Get a CaseCategory") // label for swagger 
	@GetMapping("/caseCategory/{caseCategoryId}")
	public ResponseEntity<?> getCaseCategory(@PathVariable Long caseCategoryId, @RequestParam String authToken) {
    	CaseCategory caseCategory = setupService.getCaseCategory(caseCategoryId, authToken);
    	log.info("CaseCategory : " + caseCategory);
		return new ResponseEntity<>(caseCategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Get a CaseCategory") // label for swagger 
	@GetMapping("/caseCategory/{classId}/classId")
	public ResponseEntity<?> getCaseCategoryClassId(@PathVariable Long classId, @RequestParam String authToken) {
    	CaseCategory[] caseCategoryList = setupService.getCaseCategoryByClassId(classId, authToken);
    	log.info("CaseCategory : " + caseCategoryList);
		return new ResponseEntity<>(caseCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Create CaseCategory") // label for swagger
	@PostMapping("/caseCategory")
	public ResponseEntity<?> postCaseCategory(@Valid @RequestBody CaseCategory newCaseCategory, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory createdCaseCategory = setupService.createCaseCategory(newCaseCategory, loginUserID, authToken);
		return new ResponseEntity<>(createdCaseCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Update CaseCategory") // label for swagger
    @PatchMapping("/caseCategory")
	public ResponseEntity<?> patchCaseCategory(@RequestParam Long caseCategoryId, @RequestParam String loginUserID,
			@Valid @RequestBody CaseCategory updateCaseCategory, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory updatedCaseCategory = setupService.updateCaseCategory(caseCategoryId, loginUserID, updateCaseCategory, authToken);
		return new ResponseEntity<>(updatedCaseCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseCategory.class, value = "Delete CaseCategory") // label for swagger
	@DeleteMapping("/caseCategory/{caseCategoryId}")
	public ResponseEntity<?> deleteCaseCategory(@PathVariable Long caseCategoryId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteCaseCategory(caseCategoryId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-------------------------CASE-SUB-CATEGORY----------------------------------------------------------
    @ApiOperation(response = CaseSubcategory.class, value = "Get all CaseSubcategory details") // label for swagger
	@GetMapping("/caseSubcategory")
	public ResponseEntity<?> getCaseSubcategories(@RequestParam String authToken) {
		CaseSubcategory[] caseSubcategoryList = setupService.getCaseSubcategories(authToken);
		return new ResponseEntity<>(caseSubcategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Get a CaseSubcategory") // label for swagger 
	@GetMapping("/caseSubcategory/{caseSubcategoryId}")
	public ResponseEntity<?> getCaseSubcategory(@PathVariable Long caseSubcategoryId, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam Long caseCategoryId, @RequestParam String authToken) {
    	CaseSubcategory caseSubcategory = 
    			setupService.getCaseSubcategory(languageId, classId, caseCategoryId, caseSubcategoryId, authToken);
    	log.info("CaseSubcategory : " + caseSubcategory);
		return new ResponseEntity<>(caseSubcategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Create CaseSubcategory") // label for swagger
	@PostMapping("/caseSubcategory")
	public ResponseEntity<?> postCaseSubcategory(@Valid @RequestBody CaseSubcategory newCaseSubcategory, 
			@RequestParam String loginUserID,
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory createdCaseSubcategory = setupService.createCaseSubcategory(newCaseSubcategory, loginUserID, authToken);
		return new ResponseEntity<>(createdCaseSubcategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Update CaseSubcategory") // label for swagger
    @PatchMapping("/caseSubcategory")
	public ResponseEntity<?> patchCaseSubcategory(@RequestParam Long caseSubcategoryId,  @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam Long caseCategoryId, @RequestParam String loginUserID,
			@Valid @RequestBody CaseSubcategory updateCaseSubcategory,
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory updatedCaseSubcategory = 
				setupService.updateCaseSubcategory(languageId, classId, caseCategoryId, caseSubcategoryId, loginUserID, updateCaseSubcategory, authToken);
		return new ResponseEntity<>(updatedCaseSubcategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = CaseSubcategory.class, value = "Delete CaseSubcategory") // label for swagger
	@DeleteMapping("/caseSubcategory/{caseSubcategoryId}")
	public ResponseEntity<?> deleteCaseSubcategory(@RequestParam Long caseSubcategoryId,  @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam Long caseCategoryId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteCaseSubcategory(languageId, classId, caseCategoryId, caseSubcategoryId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-------------------------------CHART-OF-ACCOUNTS-------------------------------------------------------------------
    @ApiOperation(response = ChartOfAccounts.class, value = "Get all ChartOfAccounts details") // label for swagger
 	@GetMapping("/chartOfAccounts")
 	public ResponseEntity<?> getAllChartOfAccounts(@RequestParam String authToken) {
 		ChartOfAccounts[] chartofaccountsList = setupService.getChartOfAccountsList(authToken);
 		return new ResponseEntity<>(chartofaccountsList, HttpStatus.OK);
 	}
     
    @ApiOperation(response = ChartOfAccounts.class, value = "Get a ChartOfAccounts") // label for swagger 
 	@GetMapping("/chartOfAccounts/{accountNumber}")
 	public ResponseEntity<?> getChartOfAccounts(@PathVariable Long accountNumber, @RequestParam String authToken) {
     	ChartOfAccounts chartOfAccounts = setupService.getChartOfAccounts(accountNumber, authToken);
     	log.info("ChartOfAccounts : " + chartOfAccounts);
 		return new ResponseEntity<>(chartOfAccounts, HttpStatus.OK);
 	}
     
    @ApiOperation(response = ChartOfAccounts.class, value = "Create ChartOfAccounts") // label for swagger
 	@PostMapping("/chartOfAccounts")
 	public ResponseEntity<?> postChartOfAccounts(@Valid @RequestBody ChartOfAccounts newChartOfAccounts, 
 			@RequestParam String loginUserID, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ChartOfAccounts createdChartOfAccounts = setupService.createChartOfAccounts(newChartOfAccounts, loginUserID, authToken);
 		return new ResponseEntity<>(createdChartOfAccounts , HttpStatus.OK);
 	}
     
    @ApiOperation(response = ChartOfAccounts.class, value = "Update ChartOfAccounts") // label for swagger
    @PatchMapping("/chartOfAccounts")
 	public ResponseEntity<?> patchChartOfAccounts(@RequestParam String accountNumber, @RequestParam String loginUserID,
 			@Valid @RequestBody ChartOfAccounts updateChartOfAccounts, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ChartOfAccounts updatedChartOfAccounts = setupService.updateChartOfAccounts(accountNumber, loginUserID, updateChartOfAccounts, authToken);
 		return new ResponseEntity<>(updatedChartOfAccounts , HttpStatus.OK);
 	}
     
    @ApiOperation(response = ChartOfAccounts.class, value = "Delete ChartOfAccounts") // label for swagger
 	@DeleteMapping("/chartOfAccounts/{accountNumber}")
 	public ResponseEntity<?> deleteChartOfAccounts(@PathVariable String accountNumber, @RequestParam String loginUserID, @RequestParam String authToken) {
     	setupService.deleteChartOfAccounts(accountNumber, loginUserID, authToken);
 		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
 	}
    
    //-----------------------------------------CLASS--------------------------------------------------------------------
    @ApiOperation(response = Class.class, value = "Get all Class details") // label for swagger
	@GetMapping("/class")
	public ResponseEntity<?> getClasses(@RequestParam String authToken) {
		com.mnrclara.wrapper.core.model.setup.Class[] classList = setupService.getClasses(authToken);
		return new ResponseEntity<>(classList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Class.class, value = "Get a Class") // label for swagger 
	@GetMapping("/class/{classId}")
	public ResponseEntity<?> getClass(@PathVariable Long classId, @RequestParam String authToken) {
    	com.mnrclara.wrapper.core.model.setup.Class classObj = setupService.getClass(classId, authToken);
    	log.info("Class : " + classObj);
		return new ResponseEntity<>(classObj, HttpStatus.OK);
	}
    
    @ApiOperation(response = Class.class, value = "Create Class") // label for swagger
	@PostMapping("/class")
	public ResponseEntity<?> postClass(@Valid @RequestBody com.mnrclara.wrapper.core.model.setup.Class newClass,
			@RequestParam String loginUserID,
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.wrapper.core.model.setup.Class createdClass = setupService.createClass(newClass, loginUserID, authToken);
		return new ResponseEntity<>(createdClass, HttpStatus.OK);
	}
    
    @ApiOperation(response = Class.class, value = "Update Class") // label for swagger
    @PatchMapping("/class/{classId}")
	public ResponseEntity<?> patchClass(@PathVariable Long classId, @RequestParam String loginUserID,
			@Valid @RequestBody com.mnrclara.wrapper.core.model.setup.Class updateClass,
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.wrapper.core.model.setup.Class updatedClass = 
				setupService.updateClass(classId, loginUserID, updateClass, authToken);
		return new ResponseEntity<>(updatedClass , HttpStatus.OK);
	}
    
    @ApiOperation(response = Class.class, value = "Delete Class") // label for swagger
	@DeleteMapping("/class/{classId}")
	public ResponseEntity<?> deleteClass(@PathVariable Long classId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteClass(classId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-------------------------------CLIENT-CATEGORY---------------------------------------------------------------
    @ApiOperation(response = ClientCategory.class, value = "Get all ClientCategory details") // label for swagger
	@GetMapping("/clientCategory")
	public ResponseEntity<?> getClientCategories(@RequestParam String authToken) {
		ClientCategory[] clientCategoryList = setupService.getClientCategories(authToken);
		return new ResponseEntity<>(clientCategoryList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Get a ClientCategory") // label for swagger 
	@GetMapping("/clientCategory/{clientCategoryId}")
	public ResponseEntity<?> getClientCategory(@PathVariable Long clientCategoryId, @RequestParam String authToken) {
    	ClientCategory clientcategory = setupService.getClientCategory(clientCategoryId, authToken);
    	log.info("ClientCategory : " + clientcategory);
		return new ResponseEntity<>(clientcategory, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Create ClientCategory") // label for swagger
	@PostMapping("/clientCategory")
	public ResponseEntity<?> postClientCategory(@Valid @RequestBody ClientCategory newClientCategory, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientCategory createdClientCategory = setupService.createClientCategory(newClientCategory, loginUserID, authToken);
		return new ResponseEntity<>(createdClientCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Update ClientCategory") // label for swagger
    @PatchMapping("/clientCategory")
	public ResponseEntity<?> patchClientCategory(@RequestParam Long clientCategoryId, @RequestParam String loginUserID,
			@Valid @RequestBody ClientCategory updateClientCategory, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientCategory updatedClientCategory = 
				setupService.updateClientCategory(clientCategoryId, loginUserID, updateClientCategory, authToken);
		return new ResponseEntity<>(updatedClientCategory , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientCategory.class, value = "Delete ClientCategory") // label for swagger
	@DeleteMapping("/clientCategory/{clientCategoryId}")
	public ResponseEntity<?> deleteClientCategory(@PathVariable Long clientCategoryId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteClientCategory(clientCategoryId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------CLIENT_TYPE--------------------------------------------------------------------
    @ApiOperation(response = ClientType.class, value = "Get all ClientType details") // label for swagger
	@GetMapping("/clientType")
	public ResponseEntity<?> getClientTypes(@RequestParam String authToken) {
		ClientType[] clientTypeList = setupService.getClientTypes(authToken);
		return new ResponseEntity<>(clientTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Get a ClientType") // label for swagger 
	@GetMapping("/clientType/{clientTypeId}")
	public ResponseEntity<?> getClientType(@PathVariable Long clientTypeId, @RequestParam String authToken) {
    	ClientType clientType = setupService.getClientType(clientTypeId, authToken);
    	log.info("ClientType : " + clientType);
		return new ResponseEntity<>(clientType, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Create ClientType") // label for swagger
	@PostMapping("/clientType")
	public ResponseEntity<?> postClientType(@Valid @RequestBody ClientType newClientType, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType createdClientType = setupService.createClientType(newClientType, loginUserID, authToken);
		return new ResponseEntity<>(createdClientType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Update ClientType") // label for swagger
    @PatchMapping("/clientType")
	public ResponseEntity<?> patchClientType(@RequestParam Long clientTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody ClientType updateClientType, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType updatedClientType = setupService.updateClientType(clientTypeId, loginUserID, updateClientType, authToken);
		return new ResponseEntity<>(updatedClientType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientType.class, value = "Delete ClientType") // label for swagger
	@DeleteMapping("/clientType/{clientTypeId}")
	public ResponseEntity<?> deleteClientType(@PathVariable Long clientTypeId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteClientType(clientTypeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------CLIENT_USER--------------------------------------------------------------------
    @ApiOperation(response = ClientUser.class, value = "Get all ClientUser details") // label for swagger
	@GetMapping("/clientUser")
	public ResponseEntity<?> getClientUsers(@RequestParam String authToken) {
		ClientUser[] clientUserList = setupService.getClientUsers(authToken);
		return new ResponseEntity<>(clientUserList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Get a ClientUser") // label for swagger 
	@GetMapping("/clientUser/{clientUserId}")
	public ResponseEntity<?> getClientUser(@PathVariable Long clientUserId, @RequestParam String authToken) {
    	ClientUser clientUser = setupService.getClientUser(clientUserId, authToken);
    	log.info("ClientUser : " + clientUser);
		return new ResponseEntity<>(clientUser, HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Find ClientUser") // label for swagger
    @PostMapping("/clientUser/findClientUser")
    public ClientUser[] findClientUser(@RequestBody SearchClientUser searchClientUser,
    		@RequestParam String authToken) throws ParseException {
		return setupService.findClientUser(searchClientUser, authToken);
	}
	@ApiOperation(response = ClientUser.class, value = "Find ClientUser-New") // label for swagger
	@PostMapping("/clientUser/findClientUserNew")
	public ClientUser[] findClientUser(@RequestBody FindClientUser findClientUser,
									   @RequestParam String authToken) throws ParseException {
		return setupService.findClientUserNew(findClientUser, authToken);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Send To Client") // label for swagger
   	@PostMapping("/clientUser/sendToClient")
   	public ResponseEntity<?> sendToClient (@Valid @RequestBody EMail eMail, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
    	setupService.sendEmail(eMail, authToken);
   		return new ResponseEntity<>(HttpStatus.OK);
   	}
    
    @ApiOperation(response = ClientUser.class, value = "Create ClientUser") // label for swagger
	@PostMapping("/clientUser")
	public ResponseEntity<?> postClientUser(@Valid @RequestBody ClientUser newClientUser, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser createdClientUser = setupService.createClientUser(newClientUser, loginUserID, authToken);
		return new ResponseEntity<>(createdClientUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Update ClientUser") // label for swagger
    @PatchMapping("/clientUser/{clientUserId}")
	public ResponseEntity<?> patchClientUser(@PathVariable Long clientUserId, @RequestParam String loginUserID,
			@Valid @RequestBody ClientUser updateClientUser, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser updatedClientUser = setupService.updateClientUser(clientUserId, loginUserID, updateClientUser, authToken);
		return new ResponseEntity<>(updatedClientUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = ClientUser.class, value = "Delete ClientUser") // label for swagger
	@DeleteMapping("/clientUser/{clientUserId}")
	public ResponseEntity<?> deleteClientUser(@PathVariable Long clientUserId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteClientUser(clientUserId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------COMPANY--------------------------------------------------------------------
    @ApiOperation(response = Company.class, value = "Get all Company details") // label for swagger
	@GetMapping("/company")
	public ResponseEntity<?> getCompanies(@RequestParam String authToken) {
		Company[] companyList = setupService.getCompanies(authToken);
		return new ResponseEntity<>(companyList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Company.class, value = "Get a Company") // label for swagger 
	@GetMapping("/company/{companyId}")
	public ResponseEntity<?> getCompany(@PathVariable String companyId, @RequestParam String authToken) {
    	Company company = setupService.getCompany(companyId, authToken);
    	log.info("Company : " + company);
		return new ResponseEntity<>(company, HttpStatus.OK);
	}
    
    @ApiOperation(response = Company.class, value = "Create Company") // label for swagger
	@PostMapping("/company")
	public ResponseEntity<?> postCompany(@Valid @RequestBody Company newCompany, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Company createdCompany = setupService.createCompany(newCompany, loginUserID, authToken);
		return new ResponseEntity<>(createdCompany , HttpStatus.OK);
	}
    
    @ApiOperation(response = Company.class, value = "Update Company") // label for swagger
    @PatchMapping("/company")
	public ResponseEntity<?> patchCompany(@RequestParam String companyId, @RequestParam String loginUserID,
			@Valid @RequestBody Company updateCompany, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Company updatedCompany = setupService.updateCompany(companyId, loginUserID, updateCompany, authToken);
		return new ResponseEntity<>(updatedCompany , HttpStatus.OK);
	}
    
    @ApiOperation(response = Company.class, value = "Delete Company") // label for swagger
	@DeleteMapping("/company/{companyId}")
	public ResponseEntity<?> deleteCompany(@PathVariable String companyId, @RequestParam String loginUserID, 
			@RequestParam String authToken) {
    	setupService.deleteCompany(companyId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------DeadlineCalculator--------------------------------------------------------------------
    @ApiOperation(response = DeadlineCalculator.class, value = "Get all DeadlineCalculator details") // label for swagger
	@GetMapping("/deadlineCalculator")
	public ResponseEntity<?> getDeadlineCalculators(@RequestParam String authToken) {
		DeadlineCalculator[] deadlineCalculatorList = setupService.getDeadlineCalculators(authToken);
		return new ResponseEntity<>(deadlineCalculatorList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Get a DeadlineCalculator") // label for swagger 
	@GetMapping("/deadlineCalculator/{deadlineCalculationId}")
	public ResponseEntity<?> getDeadlineCalculator(@PathVariable Long deadlineCalculationId, @RequestParam String authToken) {
    	DeadlineCalculator deadlineCalculator = setupService.getDeadlineCalculator(deadlineCalculationId, authToken);
    	log.info("DeadlineCalculator : " + deadlineCalculator);
		return new ResponseEntity<>(deadlineCalculator, HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Create DeadlineCalculator") // label for swagger
	@PostMapping("/deadlineCalculator")
	public ResponseEntity<?> postDeadlineCalculator(@Valid @RequestBody DeadlineCalculator newDeadlineCalculator, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator createdDeadlineCalculator = setupService.createDeadlineCalculator(newDeadlineCalculator, loginUserID, authToken);
		return new ResponseEntity<>(createdDeadlineCalculator , HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Update DeadlineCalculator") // label for swagger
    @PatchMapping("/deadlineCalculator/{deadlineCalculationId}")
	public ResponseEntity<?> patchDeadlineCalculator(@PathVariable Long deadlineCalculationId, @RequestParam String loginUserID,
			@Valid @RequestBody DeadlineCalculator updateDeadlineCalculator, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator updatedDeadlineCalculator = 
				setupService.updateDeadlineCalculator(deadlineCalculationId, loginUserID, updateDeadlineCalculator, authToken);
		return new ResponseEntity<>(updatedDeadlineCalculator , HttpStatus.OK);
	}
    
    @ApiOperation(response = DeadlineCalculator.class, value = "Delete DeadlineCalculator") // label for swagger
	@DeleteMapping("/deadlineCalculator/{deadlineCalculationId}")
	public ResponseEntity<?> deleteDeadlineCalculator(@PathVariable Long deadlineCalculationId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteDeadlineCalculator(deadlineCalculationId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------DocumentTemplate--------------------------------------------------------------------
    @ApiOperation(response = DocumentTemplate.class, value = "Get all DocumentTemplate details") // label for swagger
	@GetMapping("/documentTemplate")
	public ResponseEntity<?> getDocumentTemplates(@RequestParam String authToken) {
		DocumentTemplate[] documentTemplateList = setupService.getDocumentTemplates(authToken);
		return new ResponseEntity<>(documentTemplateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Get a DocumentTemplate") // label for swagger 
	@GetMapping("/documentTemplate/{documentNumber}")
	public ResponseEntity<?> getDocumentTemplate(@PathVariable String documentNumber, @RequestParam String authToken) {
    	DocumentTemplate documentTemplate = setupService.getDocumentTemplate(documentNumber, authToken);
    	log.info("DocumentTemplate : " + documentTemplate);
		return new ResponseEntity<>(documentTemplate, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Create DocumentTemplate") // label for swagger
	@PostMapping("/documentTemplate")
	public ResponseEntity<?> postDocumentTemplate(@Valid @RequestBody DocumentTemplate newDocumentTemplate, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		DocumentTemplate createdDocumentTemplate = setupService.createDocumentTemplate(newDocumentTemplate, loginUserID, authToken);
		return new ResponseEntity<>(createdDocumentTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Update DocumentTemplate") // label for swagger
    @PatchMapping("/documentTemplate")
	public ResponseEntity<?> patchDocumentTemplate(@RequestParam String loginUserID,
			@Valid @RequestBody DocumentTemplate updateDocumentTemplate, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		DocumentTemplate updatedDocumentTemplate = setupService.updateDocumentTemplate(loginUserID, updateDocumentTemplate, authToken);
		return new ResponseEntity<>(updatedDocumentTemplate , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocumentTemplate.class, value = "Delete DocumentTemplate") // label for swagger
	@DeleteMapping("/documentTemplate")
	public ResponseEntity<?> deleteDocumentTemplate(@RequestBody DocumentTemplateCompositeKey key, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteDocumentTemplate(key, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------ExpenseCode--------------------------------------------------------------------
    @ApiOperation(response = ExpenseCode.class, value = "Get all ExpenseCode details") // label for swagger
	@GetMapping("/expenseCode")
	public ResponseEntity<?> getExpenseCodes(@RequestParam String authToken) {
		ExpenseCode[] expenseCodeList = setupService.getExpenseCodes(authToken);
		return new ResponseEntity<>(expenseCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Get a ExpenseCode") // label for swagger 
	@GetMapping("/expenseCode/{expenseCode}")
	public ResponseEntity<?> getExpenseCode(@PathVariable String expenseCode, @RequestParam String authToken) {
    	ExpenseCode objExpenseCode = setupService.getExpenseCode(expenseCode, authToken);
    	log.info("ExpenseCode : " + objExpenseCode);
		return new ResponseEntity<>(objExpenseCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Create ExpenseCode") // label for swagger
	@PostMapping("/expenseCode")
	public ResponseEntity<?> postExpenseCode(@Valid @RequestBody ExpenseCode newExpenseCode, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ExpenseCode createdExpenseCode = setupService.createExpenseCode(newExpenseCode, loginUserID, authToken);
		return new ResponseEntity<>(createdExpenseCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Update ExpenseCode") // label for swagger
    @PatchMapping("/expenseCode")
	public ResponseEntity<?> patchExpenseCode(@RequestParam String expenseCode, @RequestParam String loginUserID,
			@Valid @RequestBody ExpenseCode updateExpenseCode, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ExpenseCode updatedExpenseCode = setupService.updateExpenseCode(expenseCode, loginUserID, updateExpenseCode, authToken);
		return new ResponseEntity<>(updatedExpenseCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpenseCode.class, value = "Delete ExpenseCode") // label for swagger
	@DeleteMapping("/expenseCode/{expenseCode}")
	public ResponseEntity<?> deleteExpenseCode(@PathVariable String expenseCode, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteExpenseCode(expenseCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------InquiryMode--------------------------------------------------------------------
    @ApiOperation(response = InquiryMode.class, value = "Get all InquiryMode details") // label for swagger
	@GetMapping("/inquiryMode")
	public ResponseEntity<?> getInquiryModes(@RequestParam String authToken) {
		InquiryMode[] inquiryModeList = setupService.getInquiryModes(authToken);
		return new ResponseEntity<>(inquiryModeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Get a InquiryMode") // label for swagger 
	@GetMapping("/inquiryMode/{inquiryModeId}")
	public ResponseEntity<?> getInquiryMode(@PathVariable String inquiryModeId, @RequestParam String authToken) {
    	InquiryMode inquiryMode = setupService.getInquiryMode(inquiryModeId, authToken);
    	log.info("InquiryMode : " + inquiryMode);
		return new ResponseEntity<>(inquiryMode, HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Create InquiryMode") // label for swagger
	@PostMapping("/inquiryMode")
	public ResponseEntity<?> postInquiryMode(@Valid @RequestBody InquiryMode newInquiryMode, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		InquiryMode createdInquiryMode = setupService.createInquiryMode(newInquiryMode, loginUserID, authToken);
		return new ResponseEntity<>(createdInquiryMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Update InquiryMode") // label for swagger
    @PatchMapping("/inquiryMode")
	public ResponseEntity<?> patchInquiryMode(@RequestParam String inquiryModeId, @RequestParam String loginUserID,
			@Valid @RequestBody InquiryMode updateInquiryMode, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		InquiryMode updatedInquiryMode = setupService.updateInquiryMode(inquiryModeId, loginUserID, updateInquiryMode, authToken);
		return new ResponseEntity<>(updatedInquiryMode , HttpStatus.OK);
	}
    
    @ApiOperation(response = InquiryMode.class, value = "Delete InquiryMode") // label for swagger
	@DeleteMapping("/inquiryMode/{inquiryModeId}")
	public ResponseEntity<?> deleteInquiryMode(@PathVariable String inquiryModeId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteInquiryMode(inquiryModeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------IntakeFormID--------------------------------------------------------------------
    @ApiOperation(response = IntakeFormID.class, value = "Get all IntakeFormID details") // label for swagger
	@GetMapping("/intakeFormId")
	public ResponseEntity<?> getIntakeFormIDs(@RequestParam String authToken) {
		IntakeFormID[] intakeFormIdList = setupService.getIntakeFormIDs(authToken);
		return new ResponseEntity<>(intakeFormIdList, HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Get a IntakeFormID") // label for swagger 
	@GetMapping("/intakeFormId/{intakeFormId}")
	public ResponseEntity<?> getIntakeFormID(@PathVariable String intakeFormId, @RequestParam String authToken) {
    	IntakeFormID intakeFormID = setupService.getIntakeFormID(intakeFormId, authToken);
    	log.info("IntakeFormID : " + intakeFormID);
		return new ResponseEntity<>(intakeFormID, HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Get a IntakeForm") // label for swagger 
  	@GetMapping("/intakeFormId/classId")
  	public ResponseEntity<?> getIntakeFormByClassIdAndClientTypeId(@RequestParam Long classId, 
  			@RequestParam Long clientTypeId, @RequestParam String authToken) {
      	IntakeFormID intakeform = setupService.getIntakeFormByClassIdAndClientTypeId(classId, clientTypeId, authToken);
      	log.info("IntakeForm : " + intakeform);
  		return new ResponseEntity<>(intakeform, HttpStatus.OK);
  	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Create IntakeFormID") // label for swagger
	@PostMapping("/intakeFormId")
	public ResponseEntity<?> postIntakeFormID(@Valid @RequestBody IntakeFormID newIntakeFormID, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID createdIntakeFormID = setupService.createIntakeFormID(newIntakeFormID, loginUserID, authToken);
		return new ResponseEntity<>(createdIntakeFormID , HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Update IntakeFormID") // label for swagger
    @PatchMapping("/intakeFormId")
	public ResponseEntity<?> patchIntakeFormID(@RequestParam String intakeFormIdId, @RequestParam String loginUserID,
			@Valid @RequestBody IntakeFormID updateIntakeFormID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID updatedIntakeFormID = setupService.updateIntakeFormID(intakeFormIdId, loginUserID, updateIntakeFormID, authToken);
		return new ResponseEntity<>(updatedIntakeFormID , HttpStatus.OK);
	}
    
    @ApiOperation(response = IntakeFormID.class, value = "Delete IntakeFormID") // label for swagger
	@DeleteMapping("/intakeFormId/{intakeFormId}")
	public ResponseEntity<?> deleteIntakeFormID(@PathVariable String intakeFormId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteIntakeFormID(intakeFormId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    
    //-----------------------------------Language--------------------------------------------------------------------
    @ApiOperation(response = Language.class, value = "Get all Language details") // label for swagger
	@GetMapping("/language")
	public ResponseEntity<?> getLanguages(@RequestParam String authToken) {
		Language[] languageList = setupService.getLanguages(authToken);
		return new ResponseEntity<>(languageList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Get a Language") // label for swagger 
	@GetMapping("/language/{languageId}")
	public ResponseEntity<?> getLanguage(@PathVariable String languageId, @RequestParam String authToken) {
    	Language language = setupService.getLanguage(languageId, authToken);
    	log.info("Language : " + language);
		return new ResponseEntity<>(language, HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Create Language") // label for swagger
	@PostMapping("/language")
	public ResponseEntity<?> postLanguage(@Valid @RequestBody Language newLanguage, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Language createdLanguage = setupService.createLanguage(newLanguage, loginUserID, authToken);
		return new ResponseEntity<>(createdLanguage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Update Language") // label for swagger
    @PatchMapping("/language")
	public ResponseEntity<?> patchLanguage(@RequestParam String languageId, @RequestParam String loginUserID,
			@Valid @RequestBody Language updateLanguage, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Language updatedLanguage = setupService.updateLanguage(languageId, loginUserID, updateLanguage, authToken);
		return new ResponseEntity<>(updatedLanguage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Language.class, value = "Delete Language") // label for swagger
	@DeleteMapping("/language/{languageId}")
	public ResponseEntity<?> deleteLanguage(@PathVariable String languageId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteLanguage(languageId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Message--------------------------------------------------------------------
    @ApiOperation(response = Message.class, value = "Get all Message details") // label for swagger
	@GetMapping("/message")
	public ResponseEntity<?> getMessages(@RequestParam String authToken) {
		Message[] messageList = setupService.getMessages(authToken);
		return new ResponseEntity<>(messageList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Get a Message") // label for swagger 
	@GetMapping("/message/{messageId}")
	public ResponseEntity<?> getMessage(@PathVariable String messageId, @RequestParam String authToken) {
    	Message message = setupService.getMessage(messageId, authToken);
    	log.info("Message : " + message);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Create Message") // label for swagger
	@PostMapping("/message")
	public ResponseEntity<?> postMessage(@Valid @RequestBody Message newMessage, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Message createdMessage = setupService.createMessage(newMessage, loginUserID, authToken);
		return new ResponseEntity<>(createdMessage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Update Message") // label for swagger
    @PatchMapping("/message")
	public ResponseEntity<?> patchMessage(@RequestParam String messageId, @RequestParam String loginUserID,
			@Valid @RequestBody Message updateMessage, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Message updatedMessage = setupService.updateMessage(messageId, loginUserID ,updateMessage, authToken);
		return new ResponseEntity<>(updatedMessage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Delete Message") // label for swagger
	@DeleteMapping("/message/{messageId}")
	public ResponseEntity<?> deleteMessage(@PathVariable String messageId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteMessage(messageId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------NoteType--------------------------------------------------------------------
    @ApiOperation(response = NoteType.class, value = "Get all NoteType details") // label for swagger
	@GetMapping("/noteType")
	public ResponseEntity<?> getNoteTypes(@RequestParam String authToken) {
		NoteType[] noteTypeList = setupService.getNoteTypes(authToken);
		return new ResponseEntity<>(noteTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Get a NoteType") // label for swagger 
	@GetMapping("/noteType/{noteTypeId}")
	public ResponseEntity<?> getNoteType(@PathVariable String noteTypeId, @RequestParam String authToken) {
    	NoteType noteType = setupService.getNoteType(noteTypeId, authToken);
    	log.info("NoteType : " + noteType);
		return new ResponseEntity<>(noteType, HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Create NoteType") // label for swagger
	@PostMapping("/noteType")
	public ResponseEntity<?> postNoteType(@Valid @RequestBody NoteType newNoteType, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType createdNoteType = setupService.createNoteType(newNoteType, loginUserID, authToken);
		return new ResponseEntity<>(createdNoteType , HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Update NoteType") // label for swagger
    @PatchMapping("/noteType")
	public ResponseEntity<?> patchNoteType(@RequestParam String noteTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody NoteType updateNoteType, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType updatedNoteType = setupService.updateNoteType(noteTypeId, loginUserID, updateNoteType, authToken);
		return new ResponseEntity<>(updatedNoteType , HttpStatus.OK);
	}
    
    @ApiOperation(response = NoteType.class, value = "Delete NoteType") // label for swagger
	@DeleteMapping("/noteType/{noteTypeId}")
	public ResponseEntity<?> deleteNoteType(@PathVariable String noteTypeId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteNoteType(noteTypeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Notification--------------------------------------------------------------------
    @ApiOperation(response = Notification.class, value = "Get all Notification details") // label for swagger
	@GetMapping("/notification")
	public ResponseEntity<?> getNotifications(@RequestParam String authToken) {
		Notification[] noteTypeList = setupService.getNotifications(authToken);
		return new ResponseEntity<>(noteTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Get a Notification") // label for swagger 
	@GetMapping("/notification/{notificationId}")
    public ResponseEntity<?> getNotification(@PathVariable Long notificationId, @RequestParam String authToken) {
    	Notification notification = setupService.getNotification(notificationId, authToken);
    	log.info("Notification : " + notification);
		return new ResponseEntity<>(notification, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Create Notification") // label for swagger
	@PostMapping("/notification")
	public ResponseEntity<?> postNotification(@Valid @RequestBody Notification newNotification, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Notification createdNotification = setupService.createNotification(newNotification, loginUserID, authToken);
		return new ResponseEntity<>(createdNotification , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Update Notification") // label for swagger
    @PatchMapping("/notification")
	public ResponseEntity<?> patchNotification(@RequestParam Long notificationId, @RequestParam String loginUserID,
			@Valid @RequestBody Notification updateNotification, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Notification updatedNotification = setupService.updateNotification(notificationId, loginUserID, updateNotification, authToken);
		return new ResponseEntity<>(updatedNotification , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Delete Notification") // label for swagger
	@DeleteMapping("/notification/{notificationId}")
	public ResponseEntity<?> deleteNotification(@PathVariable String notificationId, @RequestParam String loginUserID,
			@RequestParam String authToken) {
    	setupService.deleteNotification(notificationId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------NumberRange--------------------------------------------------------------------
    @ApiOperation(response = NumberRange.class, value = "Get all NumberRange details") // label for swagger
	@GetMapping("/numberRange")
	public ResponseEntity<?> getNumberRanges(@RequestParam String authToken) {
		NumberRange[] numberRangeList = setupService.getNumberRanges(authToken);
		return new ResponseEntity<>(numberRangeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Get a NumberRange") // label for swagger 
	@GetMapping("/numberRange/{numberRangeCode}")
	public ResponseEntity<?> getNumberRange(@PathVariable String numberRangeCode, @RequestParam String authToken) {
    	NumberRange numberRange = setupService.getNumberRange(numberRangeCode, authToken);
    	log.info("NumberRange : " + numberRange);
		return new ResponseEntity<>(numberRange, HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Get Number Range Current") // label for swagger
   	@GetMapping("/numberRange/nextNumberRange")
   	public ResponseEntity<?> getNumberRange(@RequestParam Long numberRangeCode, @RequestParam Long classID, @RequestParam String authToken) {
   		String nextRangeValue = setupService.getNextNumberRange(numberRangeCode, classID, authToken);
   		return new ResponseEntity<>(nextRangeValue , HttpStatus.OK);
   	}
    
    @ApiOperation(response = NumberRange.class, value = "Create NumberRange") // label for swagger
	@PostMapping("/numberRange")
	public ResponseEntity<?> postNumberRange(@Valid @RequestBody NumberRange newNumberRange, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		NumberRange createdNumberRange = setupService.createNumberRange(newNumberRange, loginUserID, authToken);
		return new ResponseEntity<>(createdNumberRange , HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Update NumberRange") // label for swagger
    @PatchMapping("/numberRange")
	public ResponseEntity<?> patchNumberRange(@RequestParam Long numberRangeCode, @RequestParam String loginUserID,
			@Valid @RequestBody NumberRange updateNumberRange, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		NumberRange updatedNumberRange = setupService.updateNumberRange(numberRangeCode, loginUserID, updateNumberRange, authToken);
		return new ResponseEntity<>(updatedNumberRange , HttpStatus.OK);
	}
    
    @ApiOperation(response = NumberRange.class, value = "Delete NumberRange") // label for swagger
	@DeleteMapping("/numberRange/{numberRangeCode}")
	public ResponseEntity<?> deleteNumberRange(@PathVariable Long numberRangeCode, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteNumberRange(numberRangeCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Referral--------------------------------------------------------------------
    @ApiOperation(response = Referral.class, value = "Get all Referral details") // label for swagger
	@GetMapping("/referral")
	public ResponseEntity<?> getReferrals(@RequestParam String authToken) {
		Referral[] referralList = setupService.getReferrals(authToken);
		return new ResponseEntity<>(referralList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Get a Referral") // label for swagger 
	@GetMapping("/referral/{referralId}")
	public ResponseEntity<?> getReferral(@PathVariable String referralId, @RequestParam String authToken) {
    	Referral referral = setupService.getReferral(referralId, authToken);
    	log.info("Referral : " + referral);
		return new ResponseEntity<>(referral, HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Create Referral") // label for swagger
	@PostMapping("/referral")
	public ResponseEntity<?> postReferral(@Valid @RequestBody Referral newReferral, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Referral createdReferral = setupService.createReferral(newReferral, loginUserID, authToken);
		return new ResponseEntity<>(createdReferral , HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Update Referral") // label for swagger
    @PatchMapping("/referral")
	public ResponseEntity<?> patchReferral(@RequestParam Long referralId, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String loginUserID,
			@Valid @RequestBody Referral updateReferral, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Referral updatedReferral = setupService.updateReferral(languageId, classId, referralId, loginUserID, updateReferral, authToken);
		return new ResponseEntity<>(updatedReferral , HttpStatus.OK);
	}
    
    @ApiOperation(response = Referral.class, value = "Delete Referral") // label for swagger
	@DeleteMapping("/referral/{referralId}")
	public ResponseEntity<?> deleteReferral(@PathVariable Long referralId, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteReferral(languageId, classId, referralId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Screen--------------------------------------------------------------------
    @ApiOperation(response = Screen.class, value = "Get all Screen details") // label for swagger
	@GetMapping("/screen")
	public ResponseEntity<?> getScreens(@RequestParam String authToken) {
		Screen[] screenList = setupService.getScreens(authToken);
		return new ResponseEntity<>(screenList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Get a Screen") // label for swagger 
	@GetMapping("/screen/{screenId}")
	public ResponseEntity<?> getScreen(@PathVariable String screenId, @RequestParam String authToken) {
    	Screen screen = setupService.getScreen(screenId, authToken);
    	log.info("Screen : " + screen);
		return new ResponseEntity<>(screen, HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Create Screen") // label for swagger
	@PostMapping("/screen")
	public ResponseEntity<?> postScreen(@Valid @RequestBody Screen newScreen, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Screen createdScreen = setupService.createScreen(newScreen, loginUserID, authToken);
		return new ResponseEntity<>(createdScreen , HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Update Screen") // label for swagger
    @PatchMapping("/screen")
	public ResponseEntity<?> patchScreen(@RequestParam Long screenId, @RequestParam String loginUserID,
			@Valid @RequestBody Screen updateScreen, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Screen updatedScreen = setupService.updateScreen(screenId, loginUserID, updateScreen, authToken);
		return new ResponseEntity<>(updatedScreen , HttpStatus.OK);
	}
    
    @ApiOperation(response = Screen.class, value = "Delete Screen") // label for swagger
	@DeleteMapping("/screen/{screenId}")
	public ResponseEntity<?> deleteScreen(@PathVariable Long screenId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteScreen(screenId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Status--------------------------------------------------------------------
    @ApiOperation(response = Status.class, value = "Get all Status details") // label for swagger
	@GetMapping("/status")
	public ResponseEntity<?> getStatuss(@RequestParam String authToken) {
		Status[] statusList = setupService.getStatus(authToken);
		return new ResponseEntity<>(statusList, HttpStatus.OK);
	}

	@ApiOperation(response = StatusMobile.class, value = "Get all Status details For Mobile") // label for swagger
	@GetMapping("/status/mobile")
	public ResponseEntity<?> getStatusMobile(@RequestParam String authToken) {
		StatusMobile[] statusList = setupService.getStatusForMobile(authToken);
		return new ResponseEntity<>(statusList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Status.class, value = "Get a Status") // label for swagger 
	@GetMapping("/status/{statusId}")
	public ResponseEntity<?> getStatus(@PathVariable String statusId, @RequestParam String authToken) {
    	Status status = setupService.getStatus(statusId, authToken);
    	log.info("Status : " + status);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
    
    @ApiOperation(response = Status.class, value = "Create Status") // label for swagger
	@PostMapping("/status")
	public ResponseEntity<?> postStatus(@Valid @RequestBody Status newStatus, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Status createdStatus = setupService.createStatus(newStatus, loginUserID, authToken);
		return new ResponseEntity<>(createdStatus , HttpStatus.OK);
	}
    
    @ApiOperation(response = Status.class, value = "Update Status") // label for swagger
    @PatchMapping("/status")
	public ResponseEntity<?> patchStatus(@RequestParam Long statusId, @RequestParam String loginUserID,
			@Valid @RequestBody Status updateStatus, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Status updatedStatus = setupService.updateStatus(statusId, loginUserID, updateStatus, authToken);
		return new ResponseEntity<>(updatedStatus , HttpStatus.OK);
	}
    
    @ApiOperation(response = Status.class, value = "Delete Status") // label for swagger
	@DeleteMapping("/status/{statusId}")
	public ResponseEntity<?> deleteStatus(@PathVariable Long statusId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteStatus(statusId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------TaskbasedCode--------------------------------------------------------------------
    @ApiOperation(response = TaskbasedCode.class, value = "Get all TaskbasedCode details") // label for swagger
	@GetMapping("/taskbasedCode")
	public ResponseEntity<?> getTaskbasedCodes(@RequestParam String authToken) {
		TaskbasedCode[] taskbasedCodeList = setupService.getTaskbasedCodes(authToken);
		return new ResponseEntity<>(taskbasedCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Get a TaskbasedCode") // label for swagger 
	@GetMapping("/taskbasedCode/{taskCode}")
	public ResponseEntity<?> getTaskbasedCode(@PathVariable String taskCode, @RequestParam String authToken) {
    	TaskbasedCode taskbasedCode = setupService.getTaskbasedCode(taskCode, authToken);
    	log.info("TaskbasedCode : " + taskbasedCode);
		return new ResponseEntity<>(taskbasedCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Create TaskbasedCode") // label for swagger
	@PostMapping("/taskbasedCode")
	public ResponseEntity<?> postTaskbasedCode(@Valid @RequestBody TaskbasedCode newTaskbasedCode, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TaskbasedCode createdTaskbasedCode = setupService.createTaskbasedCode(newTaskbasedCode, loginUserID, authToken);
		return new ResponseEntity<>(createdTaskbasedCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Update TaskbasedCode") // label for swagger
    @PatchMapping("/taskbasedCode")
	public ResponseEntity<?> patchTaskbasedCode(@RequestParam String taskCode, @RequestParam String loginUserID,
			@Valid @RequestBody TaskbasedCode updateTaskbasedCode, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TaskbasedCode updatedTaskbasedCode = setupService.updateTaskbasedCode(taskCode, loginUserID, updateTaskbasedCode, authToken);
		return new ResponseEntity<>(updatedTaskbasedCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskbasedCode.class, value = "Delete TaskbasedCode") // label for swagger
	@DeleteMapping("/taskbasedCode/{taskCode}")
	public ResponseEntity<?> deleteTaskbasedCode(@PathVariable String taskCode, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteTaskbasedCode(taskCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------TaskType--------------------------------------------------------------------
    @ApiOperation(response = TaskType.class, value = "Get all TaskType details") // label for swagger
	@GetMapping("/taskType")
	public ResponseEntity<?> getTaskTypes(@RequestParam String authToken) {
		TaskType[] taskTypeList = setupService.getTaskTypes(authToken);
		return new ResponseEntity<>(taskTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Get a TaskType") // label for swagger 
	@GetMapping("/taskType/{taskTypeCode}")
	public ResponseEntity<?> getTaskType(@PathVariable Long taskTypeCode, @RequestParam String authToken) {
    	TaskType taskType = setupService.getTaskType(taskTypeCode, authToken);
    	log.info("TaskType : " + taskType);
		return new ResponseEntity<>(taskType, HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Create TaskType") // label for swagger
	@PostMapping("/taskType")
	public ResponseEntity<?> postTaskType(@Valid @RequestBody TaskType newTaskType, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType createdTaskType = setupService.createTaskType(newTaskType, loginUserID, authToken);
		return new ResponseEntity<>(createdTaskType , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Update TaskType") // label for swagger
    @PatchMapping("/taskType")
	public ResponseEntity<?> patchTaskType(@RequestParam Long taskTypeCode, @RequestParam String loginUserID,
			@Valid @RequestBody TaskType updateTaskType, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType updatedTaskType = setupService.updateTaskType(taskTypeCode, loginUserID, updateTaskType, authToken);
		return new ResponseEntity<>(updatedTaskType , HttpStatus.OK);
	}
    
    @ApiOperation(response = TaskType.class, value = "Delete TaskType") // label for swagger
	@DeleteMapping("/taskType/{taskTypeCode}")
	public ResponseEntity<?> deleteTaskType(@PathVariable Long taskTypeCode, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteTaskType(taskTypeCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------TimekeeperCode--------------------------------------------------------------------
    @ApiOperation(response = TimekeeperCode.class, value = "Get all TimekeeperCode details") // label for swagger
	@GetMapping("/timekeeperCode")
	public ResponseEntity<?> getTimekeeperCodes(@RequestParam String authToken) {
		TimekeeperCode[] timekeeperCodeList = setupService.getTimekeeperCodes(authToken);
		return new ResponseEntity<>(timekeeperCodeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Get a TimekeeperCode") // label for swagger 
	@GetMapping("/timekeeperCode/{timekeeperCode}")
	public ResponseEntity<?> getTimekeeperCode(@PathVariable String timekeeperCode, @RequestParam String authToken) {
    	TimekeeperCode objTimekeeperCode = setupService.getTimekeeperCode(timekeeperCode, authToken);
    	log.info("TimekeeperCode : " + objTimekeeperCode);
		return new ResponseEntity<>(objTimekeeperCode, HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Create TimekeeperCode") // label for swagger
	@PostMapping("/timekeeperCode")
	public ResponseEntity<?> postTimekeeperCode(@Valid @RequestBody TimekeeperCode newTimekeeperCode, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode createdTimekeeperCode = setupService.createTimekeeperCode(newTimekeeperCode, loginUserID, authToken);
		return new ResponseEntity<>(createdTimekeeperCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Update TimekeeperCode") // label for swagger
    @PatchMapping("/timekeeperCode")
	public ResponseEntity<?> patchTimekeeperCode(@RequestParam String timekeeperCode, @RequestParam String loginUserID,
			@Valid @RequestBody TimekeeperCode updateTimekeeperCode, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode updatedTimekeeperCode = 
				setupService.updateTimekeeperCode(timekeeperCode, loginUserID, updateTimekeeperCode, authToken);
		return new ResponseEntity<>(updatedTimekeeperCode , HttpStatus.OK);
	}
    
    @ApiOperation(response = TimekeeperCode.class, value = "Delete TimekeeperCode") // label for swagger
	@DeleteMapping("/timekeeperCode/{timekeeperCode}")
	public ResponseEntity<?> deleteTimekeeperCode(@PathVariable String timekeeperCode, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteTimekeeperCode(timekeeperCode, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------Transaction--------------------------------------------------------------------
    @ApiOperation(response = Transaction.class, value = "Get all Transaction details") // label for swagger
	@GetMapping("/transaction")
	public ResponseEntity<?> getTransactions(@RequestParam String authToken) {
		Transaction[] transactionList = setupService.getTransactions(authToken);
		return new ResponseEntity<>(transactionList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Get a Transaction") // label for swagger 
	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<?> getTransaction(@PathVariable String transactionId, @RequestParam String authToken) {
    	Transaction objTransaction = setupService.getTransaction(transactionId, authToken);
    	log.info("Transaction : " + objTransaction);
		return new ResponseEntity<>(objTransaction, HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Create Transaction") // label for swagger
	@PostMapping("/transaction")
	public ResponseEntity<?> postTransaction(@Valid @RequestBody Transaction newTransaction, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Transaction createdTransaction = setupService.createTransaction(newTransaction, loginUserID, authToken);
		return new ResponseEntity<>(createdTransaction , HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Update Transaction") // label for swagger
    @PatchMapping("/transaction")
	public ResponseEntity<?> patchTransaction(@RequestParam String transactionId, @RequestParam String loginUserID,
			@Valid @RequestBody Transaction updateTransaction, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		Transaction updatedTransaction = setupService.updateTransaction(transactionId, loginUserID, updateTransaction, authToken);
		return new ResponseEntity<>(updatedTransaction , HttpStatus.OK);
	}
    
    @ApiOperation(response = Transaction.class, value = "Delete Transaction") // label for swagger
	@DeleteMapping("/transaction/{transactionId}")
	public ResponseEntity<?> deleteTransaction(@PathVariable String transactionId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteTransaction(transactionId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------User--------------------------------------------------------------------
    @ApiOperation(response = User.class, value = "Get all User details") // label for swagger
	@GetMapping("/user")
	public ResponseEntity<?> getUsers(@RequestParam String authToken) {
		User[] userList = setupService.getUsers(authToken);
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
    
    @ApiOperation(response = User.class, value = "Get a User") // label for swagger 
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable String id, @RequestParam String authToken) {
    	User objUser = setupService.getUser(id, authToken);
    	log.info("User : " + objUser);
		return new ResponseEntity<>(objUser, HttpStatus.OK);
	}
    
    @ApiOperation(response = User.class, value = "Create User") // label for swagger
	@PostMapping("/user")
	public ResponseEntity<?> postUser(@Valid @RequestBody User newUser, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		User createdUser = setupService.createUser(newUser, authToken);
		return new ResponseEntity<>(createdUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = User.class, value = "Update User") // label for swagger
    @PatchMapping("/user")
	public ResponseEntity<?> patchUser(@RequestParam String id, @RequestParam String loginUserID,
			@Valid @RequestBody User updateUser, 
			@RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		User updatedUser = setupService.updateUser(id, updateUser, authToken);
		return new ResponseEntity<>(updatedUser , HttpStatus.OK);
	}
    
    @ApiOperation(response = User.class, value = "Delete User") // label for swagger
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id, @RequestParam String authToken) {
    	setupService.deleteUser(id, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    @ApiOperation(response = Optional.class, value = "Change Password") // label for swagger
    @PutMapping("/user/{email}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable String email, 
    		@RequestParam(required = false) String oldPassword, 
    		@RequestParam String newPassword,
    		@RequestParam String authToken) {
    	setupService.changePassword(email, oldPassword, newPassword, authToken);
		return new ResponseEntity<>("Password updated successfully.", HttpStatus.OK);
    }
    
    //-----------------------------------UserProfile--------------------------------------------------------------------
    @ApiOperation(response = UserProfile.class, value = "Get all UserProfile details") // label for swagger
	@GetMapping("/userProfile")
	public ResponseEntity<?> getUserProfiles(@RequestParam String authToken) {
		UserProfile[] userProfileList = setupService.getUserProfiles(authToken);
		return new ResponseEntity<>(userProfileList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile") // label for swagger 
	@GetMapping("/userProfile/{userId}")
	public ResponseEntity<?> getUserProfile(@PathVariable String userId, @RequestParam String authToken) {
    	UserProfile objUserProfile = setupService.getUserProfile(userId, authToken);
    	log.info("UserProfile : " + objUserProfile);
		return new ResponseEntity<>(objUserProfile, HttpStatus.OK);
	}
    
    @ApiOperation(response = List.class, value = "Get a UserProfile ClassIDs") // label for swagger 
   	@GetMapping("/userProfile/{userId}/classId")
   	public ResponseEntity<?> getAllClassIdByUserId(@RequestParam String userId, @RequestParam String authToken) {
       	Long[] classIds = setupService.findClassByUserId(userId, authToken);
       	Map<String, List<Long>> body = new HashMap<>();
        body.put("classId", Arrays.asList(classIds));
   		return new ResponseEntity<>(body, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile Language") // label for swagger 
 	@GetMapping("/userProfile/{userId}/lang")
 	public ResponseEntity<?> getUserLangID(@PathVariable String userId, @RequestParam String authToken) {
     	UserProfile userProfile = setupService.getUserProfileLang(userId, authToken);
     	if (userProfile != null) {
     		log.info("UserProfile : " + userProfile);
     		return new ResponseEntity<>(userProfile.getLanguageId(), HttpStatus.OK);
     	}
		return null;
 	}
    
    @ApiOperation(response = UserProfile.class, value = "Get a UserProfile Class ID") // label for swagger 
   	@GetMapping("/userProfile/classId/{classId}")
   	public ResponseEntity<?> getUserProfileByClassId(@PathVariable Long classId, @RequestParam String authToken) {
       	UserProfile userProfile = setupService.getUserProfileByClassId(classId, authToken);
       	log.info("UserProfile : " + userProfile);
   		return new ResponseEntity<>(userProfile, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserProfile.class, value = "Create UserProfile") // label for swagger
	@PostMapping("/userProfile")
	public ResponseEntity<?> postUserProfile(@Valid @RequestBody UserProfile newUserProfile, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserProfile createdUserProfile = setupService.createUserProfile(newUserProfile, loginUserID, authToken);
		return new ResponseEntity<>(createdUserProfile , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Update UserProfile") // label for swagger
    @PatchMapping("/userProfile/{userId}")
	public ResponseEntity<?> patchUserProfile(@PathVariable String userId, @RequestParam String loginUserID,
			@Valid @RequestBody UserProfile updateUserProfile, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserProfile updatedUserProfile = setupService.updateUserProfile(userId, loginUserID, updateUserProfile, authToken);
		return new ResponseEntity<>(updatedUserProfile , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserProfile.class, value = "Delete UserProfile") // label for swagger
	@DeleteMapping("/userProfile/{userId}")
	public ResponseEntity<?> deleteUserProfile(@PathVariable String userId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteUserProfile(userId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------UserRole--------------------------------------------------------------------
    @ApiOperation(response = UserRole.class, value = "Get all UserRole details") // label for swagger
	@GetMapping("/userRole")
	public ResponseEntity<?> getUserRoles(@RequestParam String authToken) {
		UserRole[] userRoleList = setupService.getUserRoles(authToken);
		return new ResponseEntity<>(userRoleList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole[].class, value = "Get a UserRole") // label for swagger 
   	@GetMapping("/userRole/{userRoleId}")
   	public ResponseEntity<?> getUserRole(@PathVariable Long userRoleId, @RequestParam String authToken) {
       	UserRole[] userRoles = setupService.getUserRole(userRoleId, authToken);
       	log.info("UserRoles : " + userRoles);
   		return new ResponseEntity<>(userRoles, HttpStatus.OK);
   	}
    
    @ApiOperation(response = UserRole.class, value = "Create UserRole") // label for swagger
	@PostMapping("/userRole")
	public ResponseEntity<?> postUserRole(@Valid @RequestBody List<UserRole> newUserRole, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserRole[] createdUserRole = setupService.createUserRole(newUserRole, loginUserID, authToken);
		return new ResponseEntity<>(createdUserRole , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole[].class, value = "Update UserRole") // label for swagger
    @PatchMapping("/userRole/{userRoleId}")
	public ResponseEntity<?> patchUserRole(@PathVariable String userRoleId, @RequestParam String loginUserID,
			@Valid @RequestBody List<UserRole> updateUserRole, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserRole[] updatedUserRole = setupService.updateUserRole(userRoleId, loginUserID, updateUserRole, authToken);
		return new ResponseEntity<>(updatedUserRole , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserRole.class, value = "Delete UserRole") // label for swagger
	@DeleteMapping("/userRole/{userRoleId}")
	public ResponseEntity<?> deleteUserRole(@PathVariable String userRoleId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteUserRole(userRoleId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-----------------------------------UserType--------------------------------------------------------------------
    @ApiOperation(response = UserType.class, value = "Get all UserType details") // label for swagger
	@GetMapping("/userType")
	public ResponseEntity<?> getUserTypes(@RequestParam String authToken) {
		UserType[] userTypeList = setupService.getUserTypes(authToken);
		return new ResponseEntity<>(userTypeList, HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Get a UserType") // label for swagger 
	@GetMapping("/userType/{userTypeId}")
	public ResponseEntity<?> getUserType(@PathVariable String userTypeId, @RequestParam String authToken) {
    	UserType objUserType = setupService.getUserType(userTypeId, authToken);
    	log.info("UserType : " + objUserType);
		return new ResponseEntity<>(objUserType, HttpStatus.OK);
	}
    
//    @ApiOperation(response = UserType.class, value = "Get a UserType") // label for swagger 
//	@GetMapping("/userType/{userTypeId}")
//	public ResponseEntity<?> getUserType(@PathVariable String userTypeId, @RequestParam String languageId, 
//			@RequestParam Long classId, @RequestParam String authToken) {
//    	UserType objUserType = setupService.getUserType(userTypeId, languageId, classId, authToken);
//    	log.info("UserType : " + objUserType);
//		return new ResponseEntity<>(objUserType, HttpStatus.OK);
//	}
    
    @ApiOperation(response = UserType.class, value = "Create UserType") // label for swagger
	@PostMapping("/userType")
	public ResponseEntity<?> postUserType(@Valid @RequestBody UserType newUserType, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserType createdUserType = setupService.createUserType(newUserType, loginUserID, authToken);
		return new ResponseEntity<>(createdUserType , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Update UserType") // label for swagger
    @PatchMapping("/userType")
	public ResponseEntity<?> patchUserType(@RequestParam String userTypeId, @RequestParam String loginUserID,
			@Valid @RequestBody UserType updateUserType, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		UserType updatedUserType = setupService.updateUserType(userTypeId, loginUserID, updateUserType, authToken);
		return new ResponseEntity<>(updatedUserType , HttpStatus.OK);
	}
    
    @ApiOperation(response = UserType.class, value = "Delete UserType") // label for swagger
	@DeleteMapping("/userType/{userTypeId}")
	public ResponseEntity<?> deleteUserType(@PathVariable String userTypeId, @RequestParam String loginUserID, 
			@RequestParam String authToken) {
    	setupService.deleteUserType(userTypeId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /* -----------------------------EXPIRATIONDATE---EXPIRATIONDOCTYPE---------------------------------------------------------------*/
	
	@ApiOperation(response = ExpirationDocType.class, value = "Get a ExpirationDocType") // label for swagger
	@RequestMapping(value = "/expirationdoctype", method = RequestMethod.GET)
   	public ResponseEntity<?> getExpirationDocTypes(@RequestParam String authToken) {
		ExpirationDocType[] expirationdoctype = setupService.getExpirationDocTypes(authToken);
    	log.info("ExpirationDocType : " + expirationdoctype);
		return new ResponseEntity<>(expirationdoctype, HttpStatus.OK); 
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Get a ExpirationDocType") // label for swagger 
	@RequestMapping(value = "/expirationdoctype/{documentType}", method = RequestMethod.GET)
	public ResponseEntity<?> getExpirationDocType(@PathVariable String documentType, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String authToken) {
    	ExpirationDocType expirationdoctype = setupService.getExpirationDocType(documentType, languageId, classId, authToken);
    	log.info("ExpirationDocType : " + expirationdoctype);
		return new ResponseEntity<>(expirationdoctype, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Create New ExpirationDocType") // label for swagger
    @RequestMapping(value = "/expirationdoctype", method = RequestMethod.POST)
	public ResponseEntity<?> createExpirationDocType(@RequestBody ExpirationDocType newExpirationDocType, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		ExpirationDocType createdExpirationDocType = setupService.addExpirationDocType(newExpirationDocType, loginUserID, authToken);
		return new ResponseEntity<>(createdExpirationDocType , HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Update ExpirationDocType") // label for swagger
    @RequestMapping(value = "/expirationdoctype/{documentType}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateExpirationDocType(@PathVariable String documentType, 
			@RequestBody ExpirationDocType updatedExpirationDocType, @RequestParam String loginUserID, @RequestParam String authToken) {			
		ExpirationDocType modifiedExpirationDocType = setupService.updateExpirationDocType(documentType, updatedExpirationDocType, loginUserID, authToken);
		return new ResponseEntity<>(modifiedExpirationDocType , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDocType.class, value = "Delete ExpirationDocType") // label for swagger
	@RequestMapping(value = "/expirationdoctype", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteExpirationDocType(@RequestParam String documentType, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String loginUserID, @RequestParam String authToken) {
    	setupService.deleteExpirationDocType(documentType, languageId, classId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    /*
	 * --------------------------------GlMappingMaster---------------------------------
	 */
	@ApiOperation(response = GlMappingMaster.class, value = "Get all GlMappingMaster details") // label for swagger
	@GetMapping("/glmappingmaster")
	public ResponseEntity<?> getGlMappingMasters(@RequestParam String authToken) {
		GlMappingMaster [] itemNumberList = setupService.getGlMappingMasters(authToken);
		return new ResponseEntity<>(itemNumberList, HttpStatus.OK);
	}

	@ApiOperation(response = GlMappingMaster.class, value = "Get a GlMappingMaster") // label for swagger
	@GetMapping("/glmappingmaster/{itemNumber}")
	public ResponseEntity<?> getGlMappingMaster(@PathVariable Long itemNumber, @RequestParam String authToken) {
		GlMappingMaster dbGlMappingMaster = setupService.getGlMappingMaster(itemNumber, authToken);
		log.info("GlMappingMaster : " + dbGlMappingMaster);
		return new ResponseEntity<>(dbGlMappingMaster, HttpStatus.OK);
	}

	@ApiOperation(response = GlMappingMaster.class, value = "Create GlMappingMaster") // label for swagger
	@PostMapping("/glmappingmaster")
	public ResponseEntity<?> postGlMappingMaster(@Valid @RequestBody GlMappingMaster newGlMappingMaster, 
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster createdGlMappingMaster = 
				setupService.createGlMappingMaster(newGlMappingMaster, loginUserID, authToken);
		return new ResponseEntity<>(createdGlMappingMaster, HttpStatus.OK);
	}

	@ApiOperation(response = GlMappingMaster.class, value = "Update GlMappingMaster") // label for swagger
	@RequestMapping(value = "/glmappingmaster/{itemNumber}", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchGlMappingMaster(@RequestParam Long itemNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken,
			@Valid @RequestBody GlMappingMaster updateGlMappingMaster)
			throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster updatedGlMappingMaster = 
				setupService.updateGlMappingMaster(itemNumber, updateGlMappingMaster, loginUserID, authToken);
		return new ResponseEntity<>(updatedGlMappingMaster, HttpStatus.OK);
	}

	@ApiOperation(response = GlMappingMaster.class, value = "Delete GlMappingMaster") // label for swagger
	@DeleteMapping("/glmappingmaster/{itemNumber}")
	public ResponseEntity<?> deleteGlMappingMaster(@PathVariable Long itemNumber, @RequestParam String loginUserID, 
			@RequestParam String authToken) {
		setupService.deleteGlMappingMaster(itemNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//---------------------------DOC-CHECK-LIST--------------------------------------------------------------
	@ApiOperation(response = DocCheckList.class, value = "Get all DocCheckList details") // label for swagger
	@GetMapping("/docchecklist")
	public ResponseEntity<?> getDocCheckLists(@RequestParam String authToken) {
		DocCheckList[] docchecklistList = setupService.getDocCheckLists(authToken);
		return new ResponseEntity<>(docchecklistList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Get a DocCheckList") // label for swagger 
	@GetMapping("/docchecklist/{checkListNo}")
	public ResponseEntity<?> getDocCheckList(@PathVariable Long checkListNo,
			@RequestParam String languageId, @RequestParam Long classId, @RequestParam Long caseCategoryId, 
			@RequestParam Long caseSubCategoryId, @RequestParam Long sequenceNo, @RequestParam String authToken) {
    	DocCheckList docchecklist = 
    			setupService.getDocCheckList(languageId, classId, checkListNo, caseCategoryId, caseSubCategoryId, 
    					sequenceNo, authToken);
    	log.info("DocCheckList : " + docchecklist);
		return new ResponseEntity<>(docchecklist, HttpStatus.OK);
	}
    
	@ApiOperation(response = DocCheckList.class, value = "Search DocCheckList") // label for swagger
	@PostMapping("/docchecklist/findDocCheckList")
	public DocCheckList[] findDocCheckList(@RequestBody SearchDocCheckList searchDocCheckList,
			@RequestParam String authToken)
			throws Exception {
		return setupService.findDocCheckLists(searchDocCheckList, authToken);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Create DocCheckList") // label for swagger
	@PostMapping("/docchecklist")
	public ResponseEntity<?> postDocCheckList(@Valid @RequestBody List<DocCheckList> newDocCheckList, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
					throws IllegalAccessException, InvocationTargetException {
		DocCheckList[] createdDocCheckList = 
				setupService.createDocCheckList(newDocCheckList, loginUserID, authToken);
		return new ResponseEntity<>(createdDocCheckList, HttpStatus.OK);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Update DocCheckList") // label for swagger
    @PatchMapping("/docchecklist/{checkListNo}")
	public ResponseEntity<?> patchDocCheckList(@RequestBody List<UpdateDocCheckList> updateDocCheckList, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		DocCheckList[] createdDocCheckList = setupService.updateDocCheckList(updateDocCheckList, loginUserID, authToken);
		return new ResponseEntity<>(createdDocCheckList , HttpStatus.OK);
	}
    
    @ApiOperation(response = DocCheckList.class, value = "Delete DocCheckList") // label for swagger
	@DeleteMapping("/docchecklist/{checkListNo}")
    public ResponseEntity<?> deleteDocCheckList (@PathVariable Long checkListNo, 
			@RequestParam(required = false) Long sequenceNo, @RequestParam String authToken)
					throws IllegalAccessException, InvocationTargetException {
    	setupService.deleteDocCheckList (checkListNo, sequenceNo, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}