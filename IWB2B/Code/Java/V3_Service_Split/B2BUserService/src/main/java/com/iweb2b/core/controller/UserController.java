package com.iweb2b.core.controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.itextpdf.text.DocumentException;
import com.iweb2b.core.model.auth.CustomerAccessToken;
import com.iweb2b.core.model.integration.*;
import com.iweb2b.core.model.integration.asyad.Consignment;
import com.iweb2b.core.model.integration.asyad.JNTPrintLabelResponse;
import com.iweb2b.core.model.integration.asyad.JNTWebhookRequest;
import com.iweb2b.core.model.integration.asyad.PdfLabelInput;
import com.iweb2b.core.util.PDFMergeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iweb2b.core.exception.BadRequestException;
import com.iweb2b.core.exception.CustomErrorResponse;
import com.iweb2b.core.model.auth.AuthToken;
import com.iweb2b.core.model.auth.AuthTokenRequest;
import com.iweb2b.core.service.AuthTokenService;
import com.iweb2b.core.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"User Service"}, value = "User Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "UserAccess",description = "Operations related to User")})
@RequestMapping("/iwe-user-service")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AuthTokenService authTokenService;


	//-----------------------------------CONSIGNMENT-CREATION-----------------------------------------------------
	@ApiOperation(response = Optional.class, value = "OAuth Token") // label for swagger
	@PostMapping("/auth-token")
	public ResponseEntity<?> authToken(@Valid @RequestBody AuthTokenRequest authTokenRequest) {
		AuthToken authToken = authTokenService.getAuthToken(authTokenRequest);
		return new ResponseEntity<>(authToken, HttpStatus.OK);
	}

	/*
	 * --------------------------------UserManagement---------------------------------
	 */
	@ApiOperation(response = UserAccess.class, value = "Get all UserAccess details") // label for swagger
	@GetMapping("/useraccess")
	public ResponseEntity<?> getAllUser(@RequestHeader String authToken) {
		UserAccess[] userAccessList = userService.getUserAccesss(authToken);
		return new ResponseEntity<>(userAccessList, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Get a UserAccess") // label for swagger
	@GetMapping("/useraccess/{userId}")
	public ResponseEntity<?> getUserAccess(@PathVariable String userId,
										   @RequestHeader String authToken) {
		UserAccess dbUserAccess = userService.getUserAccess(userId, authToken);
		log.info("UserAccess : " + dbUserAccess);
		return new ResponseEntity<>(dbUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Create UserAccess") // label for swagger
	@PostMapping("/useraccess")
	public ResponseEntity<?> postUserAccess(@Valid @RequestBody AddUserAccess newUserAccess,
											@RequestParam String loginUserID, @RequestHeader String authToken)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess createdUserAccess = userService.createUserAccess(newUserAccess, loginUserID, authToken);
		return new ResponseEntity<>(createdUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Update UserAccess") // label for swagger
	@RequestMapping(value = "/useraccess/{userId}", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchUserAccess(@PathVariable String userId,
											 @RequestParam String loginUserID, @RequestHeader String authToken,
											 @Valid @RequestBody UpdateUserAccess updateUserAccess)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess updatedUserAccess = userService.updateUserAccess(userId, loginUserID, updateUserAccess, authToken);
		return new ResponseEntity<>(updatedUserAccess, HttpStatus.OK);
	}

	@ApiOperation(response = UserAccess.class, value = "Delete UserAccess") // label for swagger
	@DeleteMapping("/useraccess/{userId}")
	public ResponseEntity<?> deleteUserAccess(@PathVariable String userId,
											  @RequestParam String loginUserID, @RequestHeader String authToken) {

		userService.deleteUserAccess(userId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//FIND
	@ApiOperation(response = UserAccess[].class, value = "Find UserAccess")//label for swagger
	@PostMapping("/useraccess/findUserAccess")
	public UserAccess[] findUserAccess(@RequestBody FindUserAccess findUserAccess,
									   @RequestHeader String authToken)throws Exception{
		return userService.findUserAccess(findUserAccess,authToken);
	}

	/* --------------------------------LOGIN-------------------------------------------------*/

	@ApiOperation(response = Optional.class, value = "Login User") // label for swagger
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password,
										  @RequestHeader String authToken) {
		try {
			UserAccess loggedUser = userService.validateUserID(userName, password, authToken);
			log.info("LoginUser::: " + loggedUser);
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			log.error("Invalid user");
			String str = "Either UserId is invalid or Password does not match.";
			CustomErrorResponse error = new CustomErrorResponse();
			error.setTimestamp(LocalDateTime.now());
			error.setError(str);
			error.setStatus(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
		}
	}

	/* --------------------------------DASHBOARD-------------------------------------------------*/
	@ApiOperation(response = DashboardCountOutput.class, value = "Get a DashboardCount") // label for swagger
	@PostMapping("/dashboard/getDashboardCount")
	public ResponseEntity<?> getDashboardCount(@RequestBody CountInput countInput, @RequestHeader String authToken) {
		DashboardCountOutput dbDashboardCount = userService.getDashboardCount(countInput, authToken);
		return new ResponseEntity<>(dbDashboardCount, HttpStatus.OK);
	}

	@ApiOperation(response = Consignment.class, value = "Find Consignment") // label for swagger
	@PostMapping("/softdata/findConsignment")
	public ResponseEntity<?> findConsignment(@RequestBody FindConsignment findConsignment, @RequestHeader String authToken) throws Exception {
		Consignment[] response = userService.findConsignment(findConsignment, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Get All using Hub Code") // label for swagger
	@GetMapping("/softdata/{hubCode}/orders")
	public ResponseEntity<?> getHubCodeOrders(@PathVariable String hubCode, @RequestHeader String authToken) throws Exception {
		Consignment[] response = userService.getHubCodeOrders(hubCode, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = JNTWebhookRequest.class, value = "Get PrintLabel") // label for swagger
	@GetMapping("/softdata/jnt/{billCode}/printLabel")
	public ResponseEntity<?> printLabel(@PathVariable String billCode, @RequestHeader String authToken) throws Exception {
		JNTPrintLabelResponse printLabelResponse = userService.printLabel(billCode, authToken);
		log.info("printLabelResponse : " + printLabelResponse);
		return new ResponseEntity<>(printLabelResponse, HttpStatus.OK);
	}

	private String merge(List<String> billCode) throws IOException, DocumentException {
		List<String> inputPdfList = new ArrayList<>();
		for (String code : billCode) {
			inputPdfList.add(code + ".pdf");
		}
		return PDFMergeExample.mergeFiles(inputPdfList);
	}

	@ApiOperation(response = JNTWebhookRequest.class, value = "Get PrintLabel") // label for swagger
	@PostMapping("/softdata/jnt/bulk/printLabel")
	public ResponseEntity<?> pdfPrintLabel(@RequestBody PdfLabelInput pdfLabelInput, @RequestHeader String authToken) throws Exception {
		for (String code : pdfLabelInput.getBillCodes()) {
			OutputStream os = new FileOutputStream( code + ".pdf");
			byte[] printLabelResponse = userService.pdfPrintLabel(code, authToken);
			log.info("printLabelResponse : " + printLabelResponse);
			os.write(printLabelResponse);
			os.close();
		}

		String filename = merge(pdfLabelInput.getBillCodes());
		File file = new File (filename);
		FileInputStream f1 = new FileInputStream(filename);
		byte[] result = new byte[(int)file.length()];
		f1.read(result);
		f1.close();

		pdfLabelInput.getBillCodes().stream().forEach(code -> {
			File f = new File (code + ".pdf");
			f.delete();
		});

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".pdf");
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return ResponseEntity.ok()
				.headers(header)
				.contentType(MediaType.APPLICATION_PDF)
				.body(result);
	}
}