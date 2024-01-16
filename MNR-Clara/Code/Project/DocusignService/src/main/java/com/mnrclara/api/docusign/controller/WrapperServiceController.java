package com.mnrclara.api.docusign.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.docusign.model.EnvelopeRequest;
import com.mnrclara.api.docusign.model.EnvelopeResponse;
import com.mnrclara.api.docusign.model.EnvelopeStatus;
import com.mnrclara.api.docusign.service.DocusignService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@Api(tags = {"WrapperServiceController"}, value = "WrapperServiceController Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to User")})
@RequestMapping("/docuSign")
public class WrapperServiceController {
	
	@Autowired
	DocusignService docusignService;
	
//    @ApiOperation(response = Optional.class, value = "Docusign") // label for swagger
//	@PostMapping("")
//	public ResponseEntity<?> docuSign(
//				@RequestParam String file,
//				@RequestParam String key,
//				@RequestParam String documentId,
//				@RequestParam String agreementCode,
//				@RequestParam String docName,
//				@RequestParam String signerName,
//				@RequestParam String signerEmail,
//				@RequestParam String filePath) throws IOException {
	@ApiOperation(response = Optional.class, value = "Docusign") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> docuSign(@RequestBody EnvelopeRequest envelopeRequest) throws IOException {
//    	EnvelopeResponse response = docusignService.envelope(key, file, documentId, agreementCode, docName, signerName, signerEmail, filePath);
		EnvelopeResponse response = docusignService.envelope(envelopeRequest);
    	return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Docusign") // label for swagger
	@GetMapping("/envelop/status")
	public ResponseEntity<?> docuSignEnv(@RequestParam String envelopeId, @RequestParam String key) throws IOException {
    	EnvelopeStatus response = docusignService.envelopStatus(envelopeId, key);
    	return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Docusign Document Download") // label for swagger
	@GetMapping("/envelop/clientmatter/status")
	public ResponseEntity<?> docusignDocumentEnvelopeStatus(@RequestParam String envelopeId, 
			@RequestParam String key) throws IOException {
    	EnvelopeStatus response = docusignService.documentEnvelopStatus(envelopeId, key);
    	return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Docusign") // label for swagger
	@GetMapping("/envelop/download")
	public ResponseEntity<?> docuSignDownload(@RequestParam String envelopeId, 
											@RequestParam String documentId,
											@RequestParam String potentialClientId, 
											@RequestParam String key,
											@RequestParam String filePath) throws Exception {
    	String response = docusignService.downloadEnvelop(envelopeId, documentId, potentialClientId, key, filePath);
    	return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Docusign - ClientMatterDocument") // label for swagger
   	@GetMapping("/envelop/clientmatter/download")
   	public ResponseEntity<?> docuSignClientMatterDocumentDownload(@RequestParam String envelopeId, 
   											@RequestParam String documentId,
   											@RequestParam String clientMatterId, 
   											@RequestParam String key,
   											@RequestParam String filePath) throws Exception {
       	String response = docusignService.downloadDocumentEnvelope(envelopeId, documentId, clientMatterId, key, filePath);
       	return new ResponseEntity<>(response, HttpStatus.OK);
   	}
}