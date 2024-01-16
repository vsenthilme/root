package com.mnrclara.wrapper.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.wrapper.core.model.crm.itform.ITForm000;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm001;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm002;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm003;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm003Att;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm004;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm005;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm006;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm007;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm008;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm009;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm010;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm011;
import com.mnrclara.wrapper.core.model.crm.itform.MatterITForm000;
import com.mnrclara.wrapper.core.service.CRMITFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-crm-service/itform")
@Api(tags = { "CRM ITForm Service" }, value = "CRM ITForm Service Operations") // label for swagger
@SwaggerDefinition(tags = { @Tag(name = "User", description = "Operations related to CRM ITForm Modules") })
public class CRMITFormServiceController {

	@Autowired
	CRMITFormService crmITFormService;
	
	//------------------------------------ITForm000--------------------------------------------------------------------
	
	@ApiOperation(response = ITForm000.class, value = "Get a ITForm000") // label for swagger
	@GetMapping("/itform000/id")
	public ResponseEntity<?> getITForm000ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm000 ITForm000 = crmITFormService.getITForm000(inquiryNo, classID, language, itFormNo, itFormID, authToken);
		log.info("ITForm000 : " + ITForm000);
		return new ResponseEntity<>(ITForm000, HttpStatus.OK);
	}

	// -----------------------------------ITForm001--------------------------------------------------------------------
	@ApiOperation(response = ITForm001.class, value = "Get all ITForm001 details") // label for swagger
	@GetMapping("/itform001")
	public ResponseEntity<?> getITForm001List(@RequestParam String authToken) {
		ITForm001[] itform001List = crmITFormService.getITForm001s(authToken);
		return new ResponseEntity<>(itform001List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm001.class, value = "Get a ITForm001") // label for swagger
	@GetMapping("/itform001/id")
	public ResponseEntity<?> getITForm001ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm001 itform001 = crmITFormService.getITForm001(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm001 : " + itform001);
		return new ResponseEntity<>(itform001, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm001.class, value = "Create ITForm001") // label for swagger
	@PostMapping("/itform001")
	public ResponseEntity<?> postITForm001(@Valid @RequestBody ITForm001 newITForm001, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm001 createdITForm001 = crmITFormService.createITForm001(newITForm001, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm001, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm001.class, value = "Update ITForm001") // label for swagger
	@PatchMapping("/itform001")
	public ResponseEntity<?> patchITForm001(@RequestBody ITForm001 modifiedITForm001, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm001 updatedITForm001 = crmITFormService.updateITForm001(modifiedITForm001, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm001, HttpStatus.OK);
	}

	// -----------------------------------ITForm002--------------------------------------------------------------------
	@ApiOperation(response = ITForm002.class, value = "Get all ITForm002 details") // label for swagger
	@GetMapping("/itform002")
	public ResponseEntity<?> getITForm002List(@RequestParam String authToken) {
		ITForm002[] itform002List = crmITFormService.getITForm002s(authToken);
		return new ResponseEntity<>(itform002List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm002.class, value = "Get a ITForm002") // label for swagger
	@GetMapping("/itform002/id")
	public ResponseEntity<?> getITForm002ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm002 itform002 = crmITFormService.getITForm002(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm002 : " + itform002);
		return new ResponseEntity<>(itform002, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm002.class, value = "Create ITForm002") // label for swagger
	@PostMapping("/itform002")
	public ResponseEntity<?> postITForm002(@Valid @RequestBody ITForm002 newITForm002, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm002 createdITForm002 = crmITFormService.createITForm002(newITForm002, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm002, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm002.class, value = "Update ITForm002") // label for swagger
	@PatchMapping("/itform002")
	public ResponseEntity<?> patchITForm002(@RequestBody ITForm002 modifiedITForm002, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm002 updatedITForm002 = crmITFormService.updateITForm002(modifiedITForm002, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm002, HttpStatus.OK);
	}

	// -----------------------------------ITForm003--------------------------------------------------------------------
	@ApiOperation(response = ITForm003.class, value = "Get all ITForm003 details") // label for swagger
	@GetMapping("/itform003")
	public ResponseEntity<?> getITForm003List(@RequestParam String authToken) {
		ITForm003[] itform003List = crmITFormService.getITForm003s(authToken);
		return new ResponseEntity<>(itform003List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm003.class, value = "Get a ITForm003") // label for swagger
	@GetMapping("/itform003/id")
	public ResponseEntity<?> getITForm003ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm003 itform003 = crmITFormService.getITForm003(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm003 : " + itform003);
		return new ResponseEntity<>(itform003, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm003.class, value = "Create ITForm003") // label for swagger
	@PostMapping("/itform003")
	public ResponseEntity<?> postITForm003(@Valid @RequestBody ITForm003 newITForm003, @RequestParam String loginUserID, 
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm003 createdITForm003 = crmITFormService.createITForm003(newITForm003, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm003, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm003.class, value = "Update ITForm003") // label for swagger
	@PatchMapping("/itform003")
	public ResponseEntity<?> patchITForm003(@RequestBody ITForm003 modifiedITForm003, @RequestParam String loginUserID, 
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm003 updatedITForm003 = crmITFormService.updateITForm003(modifiedITForm003, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm003, HttpStatus.OK);
	}
	
	@ApiOperation(response = ITForm003Att.class, value = "Get a ITForm003Att") // label for swagger
	@GetMapping("/itform003/attorney")
	public ResponseEntity<?> getITForm003AttById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm003Att itform003Att = crmITFormService.getITForm003Att(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm003Att : " + itform003Att);
		return new ResponseEntity<>(itform003Att, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm003Att.class, value = "Create ITForm003Att") // label for swagger
	@PostMapping("/itform003/attorney")
	public ResponseEntity<?> postITForm003Att(@Valid @RequestBody ITForm003Att newITForm003Att, @RequestParam String loginUserID, 
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm003Att createdITForm003Att = crmITFormService.createITForm003Att(newITForm003Att, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm003Att, HttpStatus.OK);
	}
	
	// -----------------------------------ITForm004--------------------------------------------------------------------
	@ApiOperation(response = ITForm004.class, value = "Get all ITForm004 details") // label for swagger
	@GetMapping("/itform004")
	public ResponseEntity<?> getITForm004List(@RequestParam String authToken) {
		ITForm004[] itform004List = crmITFormService.getITForm004s(authToken);
		return new ResponseEntity<>(itform004List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm004.class, value = "Get a ITForm004") // label for swagger
	@GetMapping("/itform004/id")
	public ResponseEntity<?> getITForm004ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm004 itform004 = crmITFormService.getITForm004(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm004 : " + itform004);
		return new ResponseEntity<>(itform004, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm004.class, value = "Create ITForm004") // label for swagger
	@PostMapping("/itform004")
	public ResponseEntity<?> postITForm004(@Valid @RequestBody ITForm004 newITForm004, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm004 createdITForm004 = crmITFormService.createITForm004(newITForm004, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm004, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm004.class, value = "Update ITForm004") // label for swagger
	@PatchMapping("/itform004")
	public ResponseEntity<?> patchITForm004(@RequestBody ITForm004 modifiedITForm004, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm004 updatedITForm004 = crmITFormService.updateITForm004(modifiedITForm004, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm004, HttpStatus.OK);
	}
	
	// -----------------------------------ITForm005--------------------------------------------------------------------
	@ApiOperation(response = ITForm005.class, value = "Get all ITForm005 details") // label for swagger
	@GetMapping("/itform005")
	public ResponseEntity<?> getITForm005List(@RequestParam String authToken) {
		ITForm005[] itform005List = crmITFormService.getITForm005s(authToken);
		return new ResponseEntity<>(itform005List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Get a ITForm005") // label for swagger
	@GetMapping("/itform005/id")
	public ResponseEntity<?> getITForm005ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm005 itform005 = crmITFormService.getITForm005(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm005 : " + itform005);
		return new ResponseEntity<>(itform005, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Create ITForm005") // label for swagger
	@PostMapping("/itform005")
	public ResponseEntity<?> postITForm005(@Valid @RequestBody ITForm005 newITForm005, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm005 createdITForm005 = crmITFormService.createITForm005(newITForm005, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm005, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm005.class, value = "Update ITForm005") // label for swagger
	@PatchMapping("/itform005")
	public ResponseEntity<?> patchITForm005(@RequestBody ITForm005 modifiedITForm005, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm005 updatedITForm005 = crmITFormService.updateITForm005(modifiedITForm005, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm005, HttpStatus.OK);
	}
	
	// -----------------------------------ITForm006--------------------------------------------------------------------
	@ApiOperation(response = ITForm006.class, value = "Get all ITForm006 details") // label for swagger
	@GetMapping("/itform006")
	public ResponseEntity<?> getITForm006List(@RequestParam String authToken) {
		ITForm006[] itform006List = crmITFormService.getITForm006s(authToken);
		return new ResponseEntity<>(itform006List, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm006.class, value = "Get a ITForm006") // label for swagger
	@GetMapping("/itform006/id")
	public ResponseEntity<?> getITForm006ById(@RequestParam String inquiryNo, @RequestParam Long classID,
			@RequestParam String language, @RequestParam String itFormNo, @RequestParam Long itFormID,
			@RequestParam String authToken) {
		ITForm006 itform006 = crmITFormService.getITForm006(inquiryNo, classID, language, itFormNo, itFormID,
				authToken);
		log.info("ITForm006 : " + itform006);
		return new ResponseEntity<>(itform006, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm006.class, value = "Create ITForm006") // label for swagger
	@PostMapping("/itform006")
	public ResponseEntity<?> postITForm006(@Valid @RequestBody ITForm006 newITForm006, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm006 createdITForm006 = crmITFormService.createITForm006(newITForm006, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm006, HttpStatus.OK);
	}

	@ApiOperation(response = ITForm006.class, value = "Update ITForm006") // label for swagger
	@PatchMapping("/itform006")
	public ResponseEntity<?> patchITForm006(@RequestBody ITForm006 modifiedITForm006, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ITForm006 updatedITForm006 = crmITFormService.updateITForm006(modifiedITForm006, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm006, HttpStatus.OK);
	}
	
	// -----------------------------------ITForm007--------------------------------------------------------------------
	@ApiOperation(response = ITForm007.class, value = "Get all ITForm007 details") // label for swagger
	@GetMapping("/itform007")
	public ResponseEntity<?> getITForm007List(@RequestParam String authToken) {
		ITForm007[] itform007List = crmITFormService.getITForm007s(authToken);
		return new ResponseEntity<>(itform007List, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Get a ITForm007") // label for swagger 
	@GetMapping("/itform007/id") 
	public ResponseEntity<?> getITForm007ById(@RequestParam String language,
												@RequestParam Long classID, 
												@RequestParam String matterNumber, 
												@RequestParam String clientId,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID,
												@RequestParam String authToken) {
    	ITForm007 itform007 = crmITFormService.getITForm007(language, classID, matterNumber, clientId, itFormNo, itFormID, authToken);
    	log.info("ITForm007 : " + itform007);
    	return new ResponseEntity<>(itform007, HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Create ITForm007") // label for swagger
	@PostMapping("/itform007")
	public ResponseEntity<?> addITForm007(@Valid @RequestBody ITForm007 newITForm007, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm007 createdITForm007 = crmITFormService.createITForm007(newITForm007, loginUserID, authToken);
		return new ResponseEntity<>(createdITForm007 , HttpStatus.OK);
	}
    
    @ApiOperation(response = ITForm007.class, value = "Update ITForm007") // label for swagger
	@PatchMapping("/itform007")
	public ResponseEntity<?> updateITForm007(@RequestBody ITForm007 modifiedITForm007, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		ITForm007 updatedITForm007 = crmITFormService.updateITForm007(modifiedITForm007, loginUserID, authToken);
		return new ResponseEntity<>(updatedITForm007 , HttpStatus.OK);
	}
    
    // -----------------------------------ITForm008--------------------------------------------------------------------
 	@ApiOperation(response = ITForm008.class, value = "Get all ITForm008 details") // label for swagger
 	@GetMapping("/itform008")
 	public ResponseEntity<?> getITForm008List(@RequestParam String authToken) {
 		ITForm008[] itform008List = crmITFormService.getITForm008s(authToken);
 		return new ResponseEntity<>(itform008List, HttpStatus.OK);
 	}
     
    @ApiOperation(response = ITForm008.class, value = "Get a ITForm008") // label for swagger 
 	@GetMapping("/itform008/id") 
 	public ResponseEntity<?> getITForm008ById(@RequestParam String language,
 												@RequestParam Long classID, 
 												@RequestParam String matterNumber, 
 												@RequestParam String clientId,
 												@RequestParam String itFormNo,
 												@RequestParam Long itFormID,
 												@RequestParam String authToken) {
     	ITForm008 itform008 = crmITFormService.getITForm008(language, classID, matterNumber, clientId, itFormNo, itFormID, authToken);
     	log.info("ITForm008 : " + itform008);
     	return new ResponseEntity<>(itform008, HttpStatus.OK);
 	}
     
    @ApiOperation(response = ITForm008.class, value = "Create ITForm008") // label for swagger
 	@PostMapping("/itform008")
 	public ResponseEntity<?> addITForm008(@Valid @RequestBody ITForm008 newITForm008, @RequestParam String loginUserID, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ITForm008 createdITForm008 = crmITFormService.createITForm008(newITForm008, loginUserID, authToken);
 		return new ResponseEntity<>(createdITForm008 , HttpStatus.OK);
 	}
     
    @ApiOperation(response = ITForm008.class, value = "Update ITForm008") // label for swagger
 	@PatchMapping("/itform008")
 	public ResponseEntity<?> updateITForm008(@RequestBody ITForm008 modifiedITForm008, @RequestParam String loginUserID, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ITForm008 updatedITForm008 = crmITFormService.updateITForm008(modifiedITForm008, loginUserID, authToken);
 		return new ResponseEntity<>(updatedITForm008 , HttpStatus.OK);
 	}
    
    // -----------------------------------ITForm009--------------------------------------------------------------------
   	@ApiOperation(response = ITForm009.class, value = "Get all ITForm009 details") // label for swagger
   	@GetMapping("/itform009")
   	public ResponseEntity<?> getITForm009List(@RequestParam String authToken) {
   		ITForm009[] itform009List = crmITFormService.getITForm009s(authToken);
   		return new ResponseEntity<>(itform009List, HttpStatus.OK);
   	}
       
   	@ApiOperation(response = ITForm009.class, value = "Get a ITForm007") // label for swagger 
 	@GetMapping("/itform009/id") 
 	public ResponseEntity<?> getITForm009ById(@RequestParam String language,
 												@RequestParam Long classID, 
 												@RequestParam String matterNumber, 
 												@RequestParam String clientId,
 												@RequestParam String itFormNo,
 												@RequestParam Long itFormID,
 												@RequestParam String authToken) {
     	ITForm009 itform009 = crmITFormService.getITForm009(language, classID, matterNumber, clientId, itFormNo, itFormID, authToken);
     	log.info("ITForm009 : " + itform009);
     	return new ResponseEntity<>(itform009, HttpStatus.OK);
 	}
       
    @ApiOperation(response = ITForm009.class, value = "Create ITForm009") // label for swagger
   	@PostMapping("/itform009")
   	public ResponseEntity<?> addITForm009(@Valid @RequestBody ITForm009 newITForm009, @RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		ITForm009 createdITForm009 = crmITFormService.createITForm009(newITForm009, loginUserID, authToken);
   		return new ResponseEntity<>(createdITForm009 , HttpStatus.OK);
   	}
       
    @ApiOperation(response = ITForm009.class, value = "Update ITForm009") // label for swagger
   	@PatchMapping("/itform009")
   	public ResponseEntity<?> updateITForm009(@RequestBody ITForm009 modifiedITForm009, @RequestParam String loginUserID, @RequestParam String authToken) 
   			throws IllegalAccessException, InvocationTargetException {
   		ITForm009 updatedITForm009 = crmITFormService.updateITForm009(modifiedITForm009, loginUserID, authToken);
   		return new ResponseEntity<>(updatedITForm009 , HttpStatus.OK);
   	}
    
    // -----------------------------------ITForm0010--------------------------------------------------------------------
 	@ApiOperation(response = ITForm010.class, value = "Get all ITForm010 details") // label for swagger
 	@GetMapping("/itform010")
 	public ResponseEntity<?> getITForm010List(@RequestParam String authToken) {
 		ITForm010[] itform010List = crmITFormService.getITForm010s(authToken);
 		return new ResponseEntity<>(itform010List, HttpStatus.OK);
 	}
     
 	@ApiOperation(response = ITForm007.class, value = "Get a ITForm007") // label for swagger 
	@GetMapping("/itform010/id") 
	public ResponseEntity<?> getITForm010ById(@RequestParam String language,
												@RequestParam Long classID, 
												@RequestParam String matterNumber, 
												@RequestParam String clientId,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID,
												@RequestParam String authToken) {
    	ITForm010 itform010 = crmITFormService.getITForm010(language, classID, matterNumber, clientId, itFormNo, itFormID, authToken);
    	log.info("ITForm010 : " + itform010);
    	return new ResponseEntity<>(itform010, HttpStatus.OK);
	}
     
    @ApiOperation(response = ITForm010.class, value = "Create ITForm010") // label for swagger
 	@PostMapping("/itform010")
 	public ResponseEntity<?> addITForm010(@Valid @RequestBody ITForm010 newITForm010, @RequestParam String loginUserID, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ITForm010 createdITForm010 = crmITFormService.createITForm010(newITForm010, loginUserID, authToken);
 		return new ResponseEntity<>(createdITForm010 , HttpStatus.OK);
 	}
     
    @ApiOperation(response = ITForm010.class, value = "Update ITForm010") // label for swagger
 	@PatchMapping("/itform010")
 	public ResponseEntity<?> updateITForm010(@RequestBody ITForm010 modifiedITForm010, @RequestParam String loginUserID, @RequestParam String authToken) 
 			throws IllegalAccessException, InvocationTargetException {
 		ITForm010 updatedITForm010 = crmITFormService.updateITForm010(modifiedITForm010, loginUserID, authToken);
 		return new ResponseEntity<>(updatedITForm010 , HttpStatus.OK);
 	}
    
    // -----------------------------------ITForm011--------------------------------------------------------------------
  	@ApiOperation(response = ITForm011.class, value = "Get all ITForm011 details") // label for swagger
  	@GetMapping("/itform011")
  	public ResponseEntity<?> getITForm011List(@RequestParam String authToken) {
  		ITForm011[] itform011List = crmITFormService.getITForm011s(authToken);
  		return new ResponseEntity<>(itform011List, HttpStatus.OK);
  	}
      
  	@ApiOperation(response = ITForm011.class, value = "Get a ITForm011") // label for swagger 
	@GetMapping("/itform011/id") 
	public ResponseEntity<?> getITForm011ById(@RequestParam String language,
												@RequestParam Long classID, 
												@RequestParam String matterNumber, 
												@RequestParam String clientId,
												@RequestParam String itFormNo,
												@RequestParam Long itFormID,
												@RequestParam String authToken) {
    	ITForm011 itform011 = crmITFormService.getITForm011(language, classID, matterNumber, clientId, itFormNo, itFormID, authToken);
    	log.info("ITForm011 : " + itform011);
    	return new ResponseEntity<>(itform011, HttpStatus.OK);
	}
      
    @ApiOperation(response = ITForm011.class, value = "Create ITForm011") // label for swagger
  	@PostMapping("/itform011")
  	public ResponseEntity<?> addITForm011(@Valid @RequestBody ITForm011 newITForm011, @RequestParam String loginUserID, @RequestParam String authToken) 
  			throws IllegalAccessException, InvocationTargetException {
  		ITForm011 createdITForm011 = crmITFormService.createITForm011(newITForm011, loginUserID, authToken);
  		return new ResponseEntity<>(createdITForm011 , HttpStatus.OK);
  	}
      
    @ApiOperation(response = ITForm011.class, value = "Update ITForm011") // label for swagger
  	@PatchMapping("/itform011")
  	public ResponseEntity<?> updateITForm011(@RequestBody ITForm011 modifiedITForm011, @RequestParam String loginUserID, @RequestParam String authToken) 
  			throws IllegalAccessException, InvocationTargetException {
  		ITForm011 updatedITForm011 = crmITFormService.updateITForm011(modifiedITForm011, loginUserID, authToken);
  		return new ResponseEntity<>(updatedITForm011 , HttpStatus.OK);
  	}
}