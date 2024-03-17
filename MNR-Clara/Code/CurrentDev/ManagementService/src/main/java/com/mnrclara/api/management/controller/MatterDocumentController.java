package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mnrclara.api.management.model.dto.EnvelopeStatus;
import com.mnrclara.api.management.model.matterdocument.AddMatterDocument;
import com.mnrclara.api.management.model.matterdocument.MatterDocument;
import com.mnrclara.api.management.model.matterdocument.SearchMatterDocument;
import com.mnrclara.api.management.model.matterdocument.UpdateMatterDocument;
import com.mnrclara.api.management.service.MatterDocumentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = { "MatterDocument" }, value = "MatterDocument Operations related to MatterDocumentController")
@SwaggerDefinition(tags = { @Tag(name = "MatterDocument", description = "Operations related to MatterDocument") })
@RequestMapping("/matterdocument")
@RestController
public class MatterDocumentController {

	@Autowired
	MatterDocumentService matterDocumentService;

	@ApiOperation(response = MatterDocument.class, value = "Get all MatterDocument details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<MatterDocument> matterDocumentList = matterDocumentService.getMatterDocuments();
		return new ResponseEntity<>(matterDocumentList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterDocument.class, value = "Get a MatterDocument") // label for swagger
	@GetMapping("/{matterDocumentId}")
	public ResponseEntity<?> getMatterDocument(@PathVariable Long matterDocumentId) {
		MatterDocument matterdocument = matterDocumentService.getMatterDocument(matterDocumentId);
		log.info("MatterDocument : " + matterdocument);
		return new ResponseEntity<>(matterdocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Search MatterDocument") // label for swagger
	@PostMapping("/findMatterDocument")
	public List<MatterDocument> findMatterDocument(@RequestBody SearchMatterDocument searchMatterDocument)
			throws ParseException {
		return matterDocumentService.findMatterDocument(searchMatterDocument);
	}

	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postMatterDocument(@Valid @RequestBody AddMatterDocument newMatterDocument, 
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = matterDocumentService.createMatterDocument(newMatterDocument, loginUserID);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument - ClientPortal") // label for swagger
	@PostMapping("/clientPortal")
	public ResponseEntity<?> postMatterDocuments(@Valid @RequestBody AddMatterDocument newMatterDocument, 
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = matterDocumentService.createClientPortalMatterDocuemnt(newMatterDocument, loginUserID);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument - ClientPortal - Docs Upload") // label for swagger
	@PostMapping("/clientPortal/docsUpload") // Req.8
	public ResponseEntity<?> postMatterDocumentsForClientPortal(@Valid @RequestBody AddMatterDocument newMatterDocument, 
			@RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				matterDocumentService.createMatterDocuemntForClientPortalDocsUpload(newMatterDocument, loginUserID);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Send Document to Docusign") // label for swagger
	@PostMapping("/docusign")
	public ResponseEntity<?> postMatterDocumentToDocusign(@RequestParam Long classId, @RequestParam String matterNumber, 
			@RequestParam String documentNumber, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				matterDocumentService.sendDocumentToDocusign(classId, matterNumber, documentNumber, loginUserID);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Update MatterDocument") // label for swagger
	@PatchMapping("/{matterDocumentId}")
	public ResponseEntity<?> patchMatterDocument(@PathVariable Long matterDocumentId,
			@Valid @RequestBody UpdateMatterDocument updateMatterDocument, @RequestParam String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument updatedMatterDocument = matterDocumentService.updateMatterDocument(matterDocumentId,
				updateMatterDocument, loginUserID);
		return new ResponseEntity<>(updatedMatterDocument, HttpStatus.OK);
	}
	@ApiOperation(response = Optional.class, value = "Delete MatterDocument") // label for swagger
	@DeleteMapping("/{matterDocumentId}")
	public ResponseEntity<?> deleteMatterDocument(@PathVariable Long matterDocumentId, @RequestParam String loginUserID) {
		matterDocumentService.deleteMatterDocument(matterDocumentId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    @ApiOperation(response = Optional.class, value = "DocuSign Download") // label for swagger
   	@GetMapping("/{matterNumber}/envelope/download")
   	public ResponseEntity<?> downloadEnvelopeFromDocusign(@PathVariable String matterNumber, @RequestParam Long classId,
   			@RequestParam String documentNumber, @RequestParam String loginUserID) throws Exception {
    	MatterDocument response = matterDocumentService.downloadEnvelopeFromDocusign(classId, matterNumber, documentNumber, loginUserID);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    @ApiOperation(response = Optional.class, value = "DocuSign Envelope Status") // label for swagger
   	@GetMapping("/{matterNumber}/envelope/status")
   	public ResponseEntity<?> getEnvelopeStatusFromDocusign(@PathVariable String matterNumber, 
   			@RequestParam String loginUserID) throws Exception {
    	EnvelopeStatus response = matterDocumentService.getEnvelopeStatusFromDocusign(matterNumber, loginUserID);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    //--------------------------------------------------------------------------------------------------------
    @ApiOperation(response = MatterDocument.class, value = "Process Mailmarge Manual Document") // label for swagger
	@PatchMapping("/{matterDocumentId}/mailmerge/manual")
	public ResponseEntity<?> postMailmergeManual(@PathVariable Long matterDocumentId, String documentUrl, @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
    	MatterDocument createdMatterDocument = 
    			matterDocumentService.doProcessEditedMatterDocument(matterDocumentId, documentUrl, loginUserID);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
}