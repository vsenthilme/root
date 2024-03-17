package com.mnrclara.api.common.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mnrclara.api.common.model.docketwise.Contact;
import com.mnrclara.api.common.model.docketwise.CreateContact;
import com.mnrclara.api.common.model.docketwise.Matter;
import com.mnrclara.api.common.model.docketwise.UpdateContact;
import com.mnrclara.api.common.model.docusign.AuthToken;
import com.mnrclara.api.common.model.docusign.envelope.EnvelopeRequest;
import com.mnrclara.api.common.model.docusign.envelope.EnvelopeResponse;
import com.mnrclara.api.common.model.docusign.status.EnvelopeStatus;
import com.mnrclara.api.common.model.docusign.userinfo.UserInfo;
import com.mnrclara.api.common.model.email.EMail;
import com.mnrclara.api.common.model.mailmerge.DownloadFileResponse;
import com.mnrclara.api.common.model.mailmerge.MailMerge;
import com.mnrclara.api.common.model.mailmerge.UploadFileResponse;
import com.mnrclara.api.common.model.sms.SMS;
import com.mnrclara.api.common.service.DocketwiseService;
import com.mnrclara.api.common.service.DocusignService;
import com.mnrclara.api.common.service.DropboxService;
import com.mnrclara.api.common.service.EmailService;
import com.mnrclara.api.common.service.FileStorageService;
import com.mnrclara.api.common.service.MailMergeService;
import com.mnrclara.api.common.service.SMSService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"CommunicationController" }, value = "CommunicationController Operations related to CommunicationController")
@SwaggerDefinition(tags = { @Tag(name = "Communication", description = "Operations related to Communication") })
@RequestMapping("/communication")
@RestController
public class CommunicationController {

	@Autowired
	SMSService smsService;

	@Autowired
	EmailService emailService;

	@Autowired
	MailMergeService mailMergeService;

	@Autowired
	DocusignService docusignService;

	@Autowired
	DropboxService dropboxService;

	@Autowired
	DocketwiseService docketwiseService;

	@Autowired
	FileStorageService fileStorageService;

	@ApiOperation(response = SMS.class, value = "Send SMS") // label for swagger
	@PostMapping("/sendSMS")
	public ResponseEntity<?> postSMS(@Valid @RequestBody SMS newSMS)
			throws IllegalAccessException, InvocationTargetException {
		boolean smsResponse = smsService.sendSMS(newSMS);
		return new ResponseEntity<>(smsResponse, HttpStatus.OK);
	}
	
	@ApiOperation(response = SMS.class, value = "Send SMS") // label for swagger
	@PostMapping("/sendSMS/feedback")
	public ResponseEntity<?> postSMSForFeedback (@Valid @RequestBody SMS newSMS)
			throws IllegalAccessException, InvocationTargetException {
		boolean smsResponse = smsService.sendSMSBodyQuoteOmitted(newSMS);
		return new ResponseEntity<>(smsResponse, HttpStatus.OK);
	}

	@ApiOperation(response = SMS.class, value = "Send Email") // label for swagger
	@PostMapping("/sendEmail")
	public ResponseEntity<?> postEmail(@Valid @RequestBody EMail newEmail)
			throws IllegalAccessException, InvocationTargetException, MessagingException, IOException {
		emailService.sendMail(newEmail);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}

	//Send Mail with Attachment
	@ApiOperation(response = SMS.class, value = "Send Email with Attachment") // label for swagger
	@PostMapping("/sendEmailWithAttachment")
	public ResponseEntity<?> postEmailWithAttachment(@Valid @RequestBody EMail newEmail,
													 @RequestParam String location,
													 @RequestParam String fileName)
			throws IllegalAccessException, InvocationTargetException, MessagingException, IOException {
		emailService.sendEmailWithAttachment(newEmail, fileName, location);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}

	//Send Mail with Attachment - CG
	@ApiOperation(response = SMS.class, value = "Send Email with Attachment Control Group") // label for swagger
	@PostMapping("/cg/sendEmailWithAttachment")
	public ResponseEntity<?> postEmailWithAttachmentCG(@Valid @RequestBody EMail newEmail,
//													 @RequestParam String location,
													 @RequestParam String file)
			throws Exception {
		emailService.sendEmailWithAttachmentCG(newEmail, file);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}

	@ApiOperation(response = MailMerge.class, value = "Proess MailMerge") // label for swagger
	@PostMapping("/mailMerge")
	public ResponseEntity<?> postMailMerge(@Valid @RequestBody MailMerge mailMerge) throws Exception {
		MailMerge response = mailMergeService.doMailMerge(mailMerge);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = UploadFileResponse.class, value = "Upload Files") // label for swagger
	@PostMapping("/uploadFile")
	public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();
		UploadFileResponse response = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(),
				file.getSize());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new BadRequestException("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@ApiOperation(response = Optional.class, value = "Send Email") // label for swagger
	@GetMapping("/dropbox/download")
	public ResponseEntity<?> dropboxDownload(@RequestParam String fileUrl) throws Exception {
		String filePath = dropboxService.doDownloadFile(fileUrl);
		DownloadFileResponse response = new DownloadFileResponse();
		response.setFileDownloadUri(filePath);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Send Email") // label for swagger
	@GetMapping("/dropbox/{filepath}/download")
	public ResponseEntity<?> dropboxDownloadWithFolderPath(@PathVariable String filepath, @RequestParam String fileUrl)
			throws Exception {
		String filePath = dropboxService.doDownloadFile(filepath, fileUrl);
		DownloadFileResponse response = new DownloadFileResponse();
		response.setFileDownloadUri(filePath);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
//	@PostMapping("/docusign/envelope")
//	public ResponseEntity<?> postEnvelope(@RequestParam String file, @RequestParam String documentId,
//			@RequestParam String docName, @RequestParam String signerName, @RequestParam String signerEmail,
//			@RequestParam String agreementCode, @RequestParam String filePath) throws IOException {
//		EnvelopeResponse response = docusignService.callDocusignEnvelope(file, documentId, agreementCode, docName,
//				signerName, signerEmail, filePath);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@PostMapping("/docusign/envelope")
	public ResponseEntity<?> postEnvelope(@RequestBody EnvelopeRequest envelopeRequest) throws IOException {
		EnvelopeResponse response = docusignService.callDocusignEnvelope(envelopeRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/token")
	public ResponseEntity<?> getToken(@RequestParam String code) throws Exception {
		AuthToken response = docusignService.generateOAuthToken(code);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/userInfo")
	public ResponseEntity<?> getUserInfo() throws Exception {
		UserInfo response = docusignService.getUserInfo();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/envelope/status")
	public ResponseEntity<?> getEnvelopseStatus(@RequestParam String potentialClientId) throws Exception {
		EnvelopeStatus response = docusignService.getDocusignEnvelopeStatus(potentialClientId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/envelope/clientmatter/status")
	public ResponseEntity<?> getDocumentEnvelopeStatus(@RequestParam String clientMatterId) throws Exception {
		EnvelopeStatus response = docusignService.getDocusignDocumentEnvelopeStatus(clientMatterId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/envelope/download")
	public ResponseEntity<?> getSignedDocment(@RequestParam String potentialClientId, @RequestParam String filePath)
			throws Exception {
		String response = docusignService.downloadEnvelope(potentialClientId, filePath);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "DocuSign") // label for swagger
	@GetMapping("/docusign/envelope/clientmatter/download")
	public ResponseEntity<?> getSignedClientMatterDocment(@RequestParam String clientMatterId,
			@RequestParam String filePath) throws Exception {
		String response = docusignService.downloadClientMatterDocumentEnvelope(clientMatterId, filePath);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Outlook Calendar") // label for swagger
	@PostMapping("/calendar")
	public ResponseEntity<?> postCalendar(@Valid @RequestBody EMail calendarEmail) throws Exception {
		emailService.send(calendarEmail);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	// -------------------------Docketwise--------------------------------------------------------------
	@ApiOperation(response = Optional.class, value = "Get Docketwise Contacts") // label for swagger
	@GetMapping("/docketwise/contacts")
	public ResponseEntity<?> getDocketwiseContacts() throws Exception {
		return new ResponseEntity<>(docketwiseService.getContacts(), HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Create Docketwise Contacts") // label for swagger
	@PostMapping("/docketwise/contacts")
	public ResponseEntity<?> postDocketwiseContact(@Valid @RequestBody CreateContact createContact) throws Exception {
		Contact contactResponse = docketwiseService.createContact(createContact);
		return new ResponseEntity<>(contactResponse, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Update Docketwise Contacts") // label for swagger
	@PutMapping("/docketwise/contacts/{contactId}")
	public ResponseEntity<?> putDocketwiseContact(@PathVariable String contactId,
			@Valid @RequestBody UpdateContact updateContact) throws Exception {
		Contact contactResponse = docketwiseService.updateContact(contactId, updateContact);
		return new ResponseEntity<>(contactResponse, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Get Docketwise Matters") // label for swagger
	@GetMapping("/docketwise/matters")
	public ResponseEntity<?> getDocketwiseMatters() throws Exception {
		return new ResponseEntity<>(docketwiseService.getMatters(), HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Get Docketwise Matters") // label for swagger
	@GetMapping("/docketwise/matters/{matterId}")
	public ResponseEntity<?> getDocketwiseMatter(@PathVariable String matterId) throws Exception {
		return new ResponseEntity<>(docketwiseService.getMatter(matterId), HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Create Docketwise Matters") // label for swagger
	@PostMapping("/docketwise/matters")
	public ResponseEntity<?> postDocketwiseMatter(@Valid @RequestBody Matter matter) throws Exception {
		Matter matterResponse = docketwiseService.createMatter(matter);
		return new ResponseEntity<>(matterResponse, HttpStatus.OK);
	}

	@ApiOperation(response = Optional.class, value = "Update Docketwise Matters") // label for swagger
	@PutMapping("/docketwise/matters/{matterId}")
	public ResponseEntity<?> putDocketwiseContact(@PathVariable String matterId, @Valid @RequestBody Matter matter)
			throws Exception {
		Matter matterResponse = docketwiseService.updateMatter(matterId, matter);
		return new ResponseEntity<>(matterResponse, HttpStatus.OK);
	}

	//Send Mail with Attachment
	@ApiOperation(response = SMS.class, value = "Check_Request Send Email with Attachment") // label for swagger
	@PostMapping("/checkRequestSendEmailWithAttachment")
	public ResponseEntity<?> postCheckRequestEmail(@Valid @RequestBody EMail newEmail,
												   @RequestParam String location)
			throws IllegalAccessException, InvocationTargetException, MessagingException, IOException {
		emailService.sendMailInCheckRequest(newEmail, location);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
}