package com.mnrclara.wrapper.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.model.management.*;
import com.mnrclara.wrapper.core.model.report.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.mnrclara.wrapper.core.model.accounting.ReceiptAppNotice;
import com.mnrclara.wrapper.core.model.crm.EnvelopeStatus;
import com.mnrclara.wrapper.core.service.ManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mnr-management-service")
@Api(tags = {"Management Service"}, value = "Management Service Operations") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "User",description = "Operations related to Management Modules")})
public class ManagementServiceController { 
	
	@Autowired
	ManagementService managementService;

	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/clientgeneral")
	public ResponseEntity<?> getAll(@RequestParam String authToken) {
		ClientGeneral[] clientGeneralList = managementService.getClientGenerals(authToken);
		return new ResponseEntity<>(clientGeneralList, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/clientgeneral/dropdown/client")
	public ResponseEntity<?> getAllClientList(@RequestParam String authToken) {
		KeyValuePair[] clientGeneralList = managementService.getAllClientList(authToken);
		return new ResponseEntity<>(clientGeneralList, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/clientgeneral/pagination")
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "client_Id") String sortBy,
									@RequestParam List<Long> classId,
									@RequestParam String authToken) {
		Page<?> list = managementService.getAllClientGenerals(pageNo, pageSize, sortBy, classId, authToken);
        return new ResponseEntity<>(list, HttpStatus.OK); 
	}

	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/{clientgeneralId}")
	public ResponseEntity<?> getClientGeneral(@PathVariable String clientgeneralId, @RequestParam String authToken) {
		ClientGeneral clientgeneral = managementService.getClientGeneral(clientgeneralId, authToken);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Search ClientGeneral") // label for swagger
	@PostMapping("/clientgeneral/findClientGeneral")
	public ClientGeneral[] findClientGeneral(@RequestBody SearchClientGeneral searchClientGeneral, 
			@RequestParam String authToken)
			throws Exception {
		return managementService.findClientGenerals(searchClientGeneral, authToken);
	}
	//Find Client General New
	@ApiOperation(response = ClientGeneral.class, value = "Search ClientGeneral New") // label for swagger
	@PostMapping("/clientgeneral/findClientGeneralNew")
	public ClientGeneralNew[] findClientGeneralNew(@RequestBody SearchClientGeneral searchClientGeneral,
			@RequestParam String authToken)
			throws Exception {
		return managementService.findClientGeneralNew(searchClientGeneral, authToken);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard Total - ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/dashboard/total")
	public ResponseEntity<?> getDashboardTotal(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardTotal(loginUserID, authToken);
		log.info("ClientGeneral Dashboard Total: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard Active - ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/dashboard/active")
	public ResponseEntity<?> getDashboardActive(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardActive(loginUserID, authToken);
		log.info("ClientGeneral Dashboard Active: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Dashboard RecentClients - ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/dashboard/recentClients")
	public ResponseEntity<?> getDashboardRecentClients(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardRecentClients (loginUserID, authToken);
		log.info("ClientGeneral Dashboard Active: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Create ClientGeneral") // label for swagger
	@PostMapping("/clientgeneral")
	public ResponseEntity<?> postClientGeneral(@Valid @RequestBody ClientGeneral newClientGeneral,
			@RequestParam String loginUserID, Boolean isFromPotentialEndpoint, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral createdClientGeneral = managementService.createClientGeneral(newClientGeneral, loginUserID,
				isFromPotentialEndpoint, authToken);
		return new ResponseEntity<>(createdClientGeneral, HttpStatus.OK);
	}

	@ApiOperation(response = ClientGeneral.class, value = "Update ClientGeneral") // label for swagger
	@PatchMapping("/clientgeneral/{clientGeneralId}")
	public ResponseEntity<?> patchClientGeneral(@PathVariable String clientGeneralId,
			@Valid @RequestBody ClientGeneral updateClientGeneral, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral updatedClientGeneral = managementService.updateClientGeneral(clientGeneralId,
				updateClientGeneral, loginUserID, authToken);
		return new ResponseEntity<>(updatedClientGeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Delete ClientGeneral") // label for swagger
	@DeleteMapping("/clientgeneral/{clientGeneralId}")
	public ResponseEntity<?> deleteClientGeneral(@PathVariable String clientGeneralId, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteClientGeneral(clientGeneralId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/top")
	public ResponseEntity<?> getTopClientGeneral(@RequestParam String authToken) {
		ClientGeneral clientgeneral = managementService.getLatestClientGeneral(authToken);
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneral.class, value = "Get a ClientGeneral") // label for swagger
	@GetMapping("/clientgeneral/limit")
	public ResponseEntity<?> getClientGeneralByLimit(@RequestParam String authToken) {
		ClientGeneral clientgeneral = managementService.getClientGeneralByLimit(authToken);
		log.info("ClientGeneral : " + clientgeneral);
		return new ResponseEntity<>(clientgeneral, HttpStatus.OK);
	}
	
	//-------------------------Client-Note--------------------------------------------------------------
	
	@ApiOperation(response = ClientNote.class, value = "Get all ClientNote details") // label for swagger
	@GetMapping("/clientnote")
	public ResponseEntity<?> getClientNotes(@RequestParam String authToken) {
		ClientNote[] clientNoteList = managementService.getClientNotes(authToken);
		return new ResponseEntity<>(clientNoteList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Get a ClientNote") // label for swagger
	@GetMapping("/clientnote/{clientNotesNumber}")
	public ResponseEntity<?> getClientNote(@PathVariable String clientNotesNumber, @RequestParam String authToken) {
		ClientNote clientnote = managementService.getClientNote(clientNotesNumber, authToken);
		log.info("ClientNote : " + clientnote);
		return new ResponseEntity<>(clientnote, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Search ClientNote") // label for swagger
	@PostMapping("/clientnote/findClientNotes")
	public ClientNote[] findClientGeneral(@RequestBody SearchClientNote searchClientNote,
			@RequestParam String authToken) throws ParseException {
		return managementService.findClientNotes(searchClientNote, authToken);
	}

	@ApiOperation(response = ClientNote.class, value = "Create ClientNote") // label for swagger
	@PostMapping("/clientnote")
	public ResponseEntity<?> postClientNote(@Valid @RequestBody ClientNote newClientNote,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		ClientNote createdClientNote = managementService.createClientNote(newClientNote, loginUserID, authToken);
		return new ResponseEntity<>(createdClientNote, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Update ClientNote") // label for swagger
	@PatchMapping("/clientnote/{clientNotesNumber}")
	public ResponseEntity<?> patchClientNote(@PathVariable String clientNotesNumber,
			@Valid @RequestBody ClientNote updateClientNote, @RequestParam String loginUserID,
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		ClientNote updatedClientNote = managementService.updateClientNote(clientNotesNumber, updateClientNote,
				loginUserID, authToken);
		return new ResponseEntity<>(updatedClientNote, HttpStatus.OK);
	}

	@ApiOperation(response = ClientNote.class, value = "Delete ClientNote") // label for swagger
	@DeleteMapping("/clientnote/{clientNotesNumber}")
	public ResponseEntity<?> deleteClientNote(@PathVariable String clientNotesNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteClientNote(clientNotesNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//-----------------------Client-Document-----------------------------------------------------------
	
	@ApiOperation(response = ClientDocument.class, value = "Get all ClientDocument details") // label for swagger
	@GetMapping("/clientdocument")
	public ResponseEntity<?> getClientDocuments(@RequestParam String authToken) {
		ClientDocument[] clientDocumentList = managementService.getClientDocuments(authToken);
		return new ResponseEntity<>(clientDocumentList, HttpStatus.OK);
	}

	@ApiOperation(response = ClientDocument.class, value = "Get a ClientDocument") // label for swagger
	@GetMapping("/clientdocument/{clientDocumentId}")
	public ResponseEntity<?> getClientDocument(@PathVariable Long clientDocumentId, @RequestParam String authToken) {
		ClientDocument clientdocument = managementService.getClientDocument(clientDocumentId, authToken);
		log.info("ClientDocument : " + clientdocument);
		return new ResponseEntity<>(clientdocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Search ClientDocument") // label for swagger
	@PostMapping("/clientdocument/findClientDocument")
	public ClientDocument[] findClientDocument(@RequestBody SearchClientDocument searchClientDocument,
			@RequestParam String authToken) throws ParseException {
		return managementService.findClientDocuments(searchClientDocument, authToken);
	}

	@ApiOperation(response = ClientDocument.class, value = "Create ClientDocument") // label for swagger
	@PostMapping("/clientdocument")
	public ResponseEntity<?> postClientDocument(@Valid @RequestBody ClientDocument newClientDocument, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument createdClientDocument = managementService.createClientDocument(newClientDocument, loginUserID, authToken);
		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Send Document to Docusign") // label for swagger
	@PostMapping("/clientdocument/docusign")
	public ResponseEntity<?> postClientDocumentToDocusign(@RequestParam Long classId, @RequestParam String clientId, 
			@RequestParam String documentNumber, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument createdClientDocument = 
				managementService.sendDocumentToDocusign(classId, clientId, documentNumber, loginUserID, authToken);
		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientDocument.class, value = "Update ClientDocument") // label for swagger
	@PatchMapping("/clientdocument/{clientDocumentId}")
	public ResponseEntity<?> patchClientDocument(@PathVariable Long clientDocumentId,
			@Valid @RequestBody ClientDocument updateClientDocument, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ClientDocument updatedClientDocument = managementService.updateClientDocument(clientDocumentId,
				updateClientDocument, loginUserID, authToken);
		return new ResponseEntity<>(updatedClientDocument, HttpStatus.OK);
	}

	@ApiOperation(response = ClientDocument.class, value = "Delete ClientDocument") // label for swagger
	@DeleteMapping("/clientdocument/{clientDocumentId}")
	public ResponseEntity<?> deleteClientDocument(@PathVariable Long clientDocumentId,
												  @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		managementService.deleteClientDocumentId(clientDocumentId,loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(response = ClientDocument.class, value = "Docusign Envelope Document") // label for swagger
	@GetMapping("/clientdocument/{clientId}/docusign/download")
	public ResponseEntity<?> downloadClientDocumentEnvelope (@PathVariable String clientId, @RequestParam Long classId,
   			@RequestParam String documentNumber, @RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		ClientDocument response = managementService.docusignEnvelopeDownload(classId, clientId, documentNumber, loginUserID, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
    @ApiOperation(response = Optional.class, value = "DocuSign Envelope Status") // label for swagger
   	@GetMapping("/clientdocument/{clientId}/docusign/envelope/status")
   	public ResponseEntity<?> getEnvelopeStatusFromDocusign(@PathVariable String clientId, 
   			@RequestParam String loginUserID, @RequestParam String authToken) throws Exception {
    	EnvelopeStatus response = managementService.getDocusignClientEnvelopeStatus(clientId, loginUserID, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
    
    @ApiOperation(response = ClientDocument.class, value = "Process Mailmarge Manual Document") // label for swagger
   	@PatchMapping("/clientdocument/{clientId}/mailmerge/manual")
   	public ResponseEntity<?> postProcessEditedClientDocument(@PathVariable String clientId, @RequestParam Long classId, 
   			@RequestParam String documentNumber, @RequestParam String location, @RequestParam("file") MultipartFile file,
   			@RequestParam String loginUserID, @RequestParam String authToken) 
   					throws Exception {
    	ClientDocument createdClientDocument = 
   				managementService.doProcessEditedClientDocument(classId, clientId, documentNumber, location, file, loginUserID, authToken);
   		return new ResponseEntity<>(createdClientDocument, HttpStatus.OK);
   	}
	
	//-----------------------LeCaseIngoSheet------------------------------------------------------------
	
	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get all LeCaseInfoSheet details") // label for swagger
	@GetMapping("/leCaseInfoSheet")
	public ResponseEntity<?> getLeCaseInfoSheets(@RequestParam String authToken) {
		LeCaseInfoSheet[] leCaseInfoSheetList = managementService.getLeCaseInfoSheets(authToken);
		return new ResponseEntity<>(leCaseInfoSheetList, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get a LeCaseInfoSheet") // label for swagger
	@GetMapping("/leCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> getLeCaseInfoSheetById(@PathVariable String caseInformationID, @RequestParam String authToken) {
		LeCaseInfoSheet leCaseInfoSheet = managementService.getLeCaseInfoSheet(caseInformationID, authToken);
		log.info("LeCaseInfoSheet : " + leCaseInfoSheet);
		return new ResponseEntity<>(leCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Get a LeCaseInfoSheet") // label for swagger
	@PostMapping("/leCaseInfoSheet/search")
	public ResponseEntity<?> findByMultipleParams(@RequestBody SearchCaseSheetParams searchCaseSheetParams, @RequestParam String authToken) {
		LeCaseInfoSheet[] LeCaseInfoSheets = managementService.findLeCaseInfoSheets(searchCaseSheetParams, authToken);
		return new ResponseEntity<>(LeCaseInfoSheets, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Create LeCaseInfoSheet") // label for swagger
	@PostMapping("/leCaseInfoSheet")
	public ResponseEntity<?> postLeCaseInfoSheet(@Valid @RequestBody LeCaseInfoSheet newLeCaseInfoSheet,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		LeCaseInfoSheet createdLeCaseInfoSheet = managementService.createLeCaseInfoSheet(newLeCaseInfoSheet,
				loginUserID, authToken);
		return new ResponseEntity<>(createdLeCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Create Matter") // label for swagger
	@GetMapping("/leCaseInfoSheet/{caseInformationID}/matter")
	public ResponseEntity<?> postMatter(@PathVariable String caseInformationID, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterGenAcc createdMatterGenAcc = managementService.createMatterFromLeCaseInfoSheet(caseInformationID, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Update LeCaseInfoSheet") // label for swagger
	@PatchMapping("/leCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> patchLeCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestBody LeCaseInfoSheet modifiedLeCaseInfoSheet, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		LeCaseInfoSheet updatedLeCaseInfoSheet = managementService.updateLeCaseInfoSheet(caseInformationID,
				modifiedLeCaseInfoSheet, loginUserID, authToken);
		return new ResponseEntity<>(updatedLeCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = LeCaseInfoSheet.class, value = "Delete LeCaseInfoSheet") // label for swagger
	@DeleteMapping("/leCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> deleteLeCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		managementService.deleteLeCaseInfoSheet(caseInformationID, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//-----------------------ImmCaseInfoSheet------------------------------------------------------------
	
	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get all ImmCaseInfoSheet details") // label for swagger
	@GetMapping("/immCaseInfoSheet")
	public ResponseEntity<?> getImmCaseInfoSheets(@RequestParam String authToken) {
		ImmCaseInfoSheet[] leCaseInfoSheetList = managementService.getImmCaseInfoSheets(authToken);
		return new ResponseEntity<>(leCaseInfoSheetList, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get a ImmCaseInfoSheet") // label for swagger
	@GetMapping("/immCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> getImmCaseInfoSheetById(@PathVariable String caseInformationID, @RequestParam String authToken) {
		ImmCaseInfoSheet immCaseInfoSheet = managementService.getImmCaseInfoSheet(caseInformationID, authToken);
		log.info("ImmCaseInfoSheet : " + immCaseInfoSheet);
		return new ResponseEntity<>(immCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Get a ImmCaseInfoSheet") // label for swagger
	@PostMapping("/immCaseInfoSheet/search")
	public ResponseEntity<?> findimmCaseInfoSheetSearch(@RequestBody SearchCaseSheetParams searchCaseSheetParams, @RequestParam String authToken) {
		ImmCaseInfoSheet[] ImmCaseInfoSheets = managementService.findImmCaseInfoSheets(searchCaseSheetParams, authToken);
		return new ResponseEntity<>(ImmCaseInfoSheets, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Create ImmCaseInfoSheet") // label for swagger
	@PostMapping("/immCaseInfoSheet")
	public ResponseEntity<?> postImmCaseInfoSheet(@Valid @RequestBody ImmCaseInfoSheet newImmCaseInfoSheet,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		ImmCaseInfoSheet createdImmCaseInfoSheet = managementService.createImmCaseInfoSheet(newImmCaseInfoSheet,
				loginUserID, authToken);
		return new ResponseEntity<>(createdImmCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Create Matter") // label for swagger
	@GetMapping("/immCaseInfoSheet/{caseInformationID}/matter")
	public ResponseEntity<?> postMatterFromImmCaseInfoSheet(@PathVariable String caseInformationID, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterGenAcc createdMatterGenAcc = managementService.createMatterFromImmCaseInfoSheet(caseInformationID, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Update ImmCaseInfoSheet") // label for swagger
	@PatchMapping("/immCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> patchImmCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestBody ImmCaseInfoSheet modifiedImmCaseInfoSheet, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		ImmCaseInfoSheet updatedImmCaseInfoSheet = managementService.updateImmCaseInfoSheet(caseInformationID,
				modifiedImmCaseInfoSheet, loginUserID, authToken);
		return new ResponseEntity<>(updatedImmCaseInfoSheet, HttpStatus.OK);
	}

	@ApiOperation(response = ImmCaseInfoSheet.class, value = "Delete ImmCaseInfoSheet") // label for swagger
	@DeleteMapping("/immCaseInfoSheet/{caseInformationID}")
	public ResponseEntity<?> deleteImmCaseInfoSheet(@PathVariable String caseInformationID,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		managementService.deleteImmCaseInfoSheet(caseInformationID, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//---------------------------MatterGenAcc---------------------------------------------------------------
	@ApiOperation(response = MatterGenAcc.class, value = "Get all MatterGeneralAccount details") // label for swagger
	@GetMapping("/mattergenacc")
	public ResponseEntity<?> getMatterGenAccs(@RequestParam String authToken) {
		MatterGenAcc[] matterGenAccList = managementService.getMatterGenAccs(authToken);
		return new ResponseEntity<>(matterGenAccList, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get all ClientGeneral details") // label for swagger
	@GetMapping("/mattergenacc/pagination")
	public ResponseEntity<?> getAllMatterGenAccs(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "MATTER_NO") String sortBy,
									@RequestParam List<Long> classId,
									@RequestParam String authToken) {
		Page<?> list = managementService.getAllMatterGenAccs(pageNo, pageSize, sortBy, classId, authToken);
		log.info("list : " + list);
        return new ResponseEntity<>(list, HttpStatus.OK); 
	}
	
	//----------------------------------------------------------------------------------------------
    @ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/mattergenacc/dropdown")
	public ResponseEntity<?> getDropdownList(@RequestParam String authToken) {
		Dropdown matterGenAcc = managementService.getDropdownList(authToken);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
    
    @ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/mattergenacc/dropdown/matter")
	public ResponseEntity<?> getMatterDropdownList(@RequestParam String authToken) {
		MatterDropdownList matterDropdownList = managementService.getMatterDropdownList(authToken);
		return new ResponseEntity<>(matterDropdownList, HttpStatus.OK);
	}


	@ApiOperation(response = Dropdown.class, value = "Get Matter Dropdown") // label for swagger
	@GetMapping("/mattergenacc/dropdown/matter/new")
	public ResponseEntity<?> getMatterDropdown(@RequestParam String authToken) {
		MatterNumberDropDown[] matterDropdownList = managementService.getMatterNumberDropdown(authToken);
		return new ResponseEntity<>(matterDropdownList, HttpStatus.OK);
	}


	@ApiOperation(response = Dropdown.class, value = "Get Matter Dropdown") // label for swagger
	@GetMapping("/mattergenacc/dropdown/matterList")
	public ResponseEntity<?> getMatterDropdownNew(@RequestParam String authToken) {
		MatterNumberDropDown[] matterDropdownList = managementService.getMatterNumberDropdownNew(authToken);
		return new ResponseEntity<>(matterDropdownList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Dropdown.class, value = "Get a Dropdown") // label for swagger
	@GetMapping("/dropdown/matter/open")
	public ResponseEntity<?> getOpenMatterDropdownList(@RequestParam String authToken) {
		MatterDropdownList matterGenAcc = managementService.getOpenMatterDropdownList(authToken);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGeneralAccount") // label for swagger
	@GetMapping("/mattergenacc/{matterNumber}")
	public ResponseEntity<?> getMatterGenAcc(@PathVariable String matterNumber, @RequestParam String authToken) {
		MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(matterNumber, authToken);
		log.info("MatterGenAcc : " + matterGenAcc);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Get a MatterGeneralAccount") // label for swagger
	@GetMapping("/mattergenacc/{matterNumber}/docketwise/{matterId}")
	public ResponseEntity<?> getMatterGenAccFromDocketwise(@PathVariable String matterNumber, @PathVariable String matterId,
			@RequestParam String authToken) throws ParseException {
		MatterGenAcc matterGenAcc = managementService.getMatterGenAccFromDocketwise(matterNumber, matterId, authToken);
		log.info("MatterGenAcc : " + matterGenAcc);
		return new ResponseEntity<>(matterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Search MatterGenAcc") // label for swagger
    @PostMapping("/mattergenacc/findMatterGenAccs")
    public MatterGenAcc[] findMatterGenAccs(@RequestBody SearchMatterGeneral searchMatterGeneral, 
    		@RequestParam String authToken) throws ParseException {
		return managementService.findMatterGenAccs(searchMatterGeneral, authToken);
	}
	//Find Matter General New
	@ApiOperation(response = Optional.class, value = "Search MatterGeneralAcc-New") // label for swagger
	@PostMapping("/mattergenacc/findMatterGeneralNew")
	public ResponseEntity<?> findMatterGenAccs(@RequestBody FindMatterGeneral searchMatterGeneral,
											@RequestParam String authToken) throws ParseException {
		FindMatterGenNew[] data = managementService.findMatterGeneralNew(searchMatterGeneral, authToken);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Total") // label for swagger
	@GetMapping("/mattergenacc/dashboard/total")
	public ResponseEntity<?> getDashboardTotalForMatter(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardTotalForMatter(loginUserID, authToken);
		log.info("MatterGenAcc Dashboard Total: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Open") // label for swagger
	@GetMapping("/mattergenacc/dashboard/open")
	public ResponseEntity<?> getDashboardOpenForMatter(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardOpenForMatter(loginUserID, authToken);
		log.info("MatterGenAcc Dashboard Open: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - Filed") // label for swagger
	@GetMapping("/mattergenacc/dashboard/filed")
	public ResponseEntity<?> getDashboardFiledForMatter(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardFiledForMatter(loginUserID, authToken);
		log.info("MatterGenAcc Dashboard Filed: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - RTE") // label for swagger
	@GetMapping("/mattergenacc/dashboard/RTE")
	public ResponseEntity<?> getDashboardRTEForMatter(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardRTEForMatter(loginUserID, authToken);
		log.info("MatterGenAcc Dashboard RTE: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Dashboard MatterGenAcc - CLOSED") // label for swagger
	@GetMapping("/mattergenacc/dashboard/closed")
	public ResponseEntity<?> getDashboardClosedForMatter(@RequestParam String loginUserID, @RequestParam String authToken) {
		DashboardReport dashboardReport = managementService.getDashboardClosedForMatter(loginUserID, authToken);
		log.info("MatterGenAcc Dashboard Closed: " + dashboardReport);
		return new ResponseEntity<>(dashboardReport, HttpStatus.OK);
	}

	//------------------------------Academic Report--------------------------------------------------------

	@ApiOperation(response = AcademicReport[].class, value = "Academic Report") // label for swagger
	@PostMapping("/mattergenacc/academicReport")
	public ResponseEntity<?> getAcademicReport(@RequestBody AcademicReportInput academicReportInput,
											   @RequestParam String authToken) throws ParseException {
		AcademicReport[] academicReports = managementService.getAcademicReport(academicReportInput, authToken);
		log.info("academicReport : " + academicReports);
		return new ResponseEntity<>(academicReports, HttpStatus.OK);
	}

	@ApiOperation(response = MatterGenAcc.class, value = "Update MatterGeneralAccount") // label for swagger
	@PatchMapping("/mattergenacc/{matterNumber}")
	public ResponseEntity<?> patchMatterGenAcc(@PathVariable String matterNumber,
			@Valid @RequestBody MatterGenAcc updateMatterGenAcc, @RequestParam String loginUserID, @RequestParam String authToken)
			throws Exception {
		MatterGenAcc updatedMatterGenAcc = managementService.updateMatterGenAcc(matterNumber, updateMatterGenAcc, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterGenAcc.class, value = "Update MatterGeneral Accounting") // label for swagger
	@PatchMapping("/mattergenacc/{matterNumber}/accounting")
	public ResponseEntity<?> patchMatterAccounting(@PathVariable String matterNumber,
			@Valid @RequestBody MatterGenAcc updateMatterGenAcc, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterGenAcc updatedMatterGenAcc = managementService.updateMatterAccounting(matterNumber, updateMatterGenAcc, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}

	@ApiOperation(response = MatterGenAcc.class, value = "Delete MatterGeneralAccount") // label for swagger
	@DeleteMapping("/mattergenacc/{matterNumber}")
	public ResponseEntity<?> deleteMatterGenAcc(@PathVariable String matterNumber, @RequestParam String loginUserID, 
			@RequestParam String authToken) {
		managementService.deleteMatterGenAcc(matterNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//--------------------Push-To-Docketwise-------------------------------------------------------------------------
	@ApiOperation(response = MatterGenAcc.class, value = "Push2Docketwise MatterGeneral") // label for swagger
	@GetMapping("/mattergenacc/{matterNumber}/push2Docketwise")
	public ResponseEntity<?> push2Docketwise(@PathVariable String matterNumber, @RequestParam String authToken) {
		MatterGenAcc updatedMatterGenAcc = managementService.push2Docketwise(matterNumber, authToken);
		return new ResponseEntity<>(updatedMatterGenAcc, HttpStatus.OK);
	}
	
	//---------------------------------Matter-Note-------------------------------------------------------
	
	@ApiOperation(response = MatterNote.class, value = "Get all MatterNote details") // label for swagger
	@GetMapping("/matternote")
	public ResponseEntity<?> getMatterNotes(@RequestParam String authToken) {
		MatterNote[] clientNoteList = managementService.getMatterNotes(authToken);
		return new ResponseEntity<>(clientNoteList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Get a MatterNote") // label for swagger
	@GetMapping("/matternote/{matterNotesNumber}")
	public ResponseEntity<?> getMatterNote(@PathVariable String matterNotesNumber, @RequestParam String authToken) {
		MatterNote matternote = managementService.getMatterNote(matterNotesNumber, authToken);
		log.info("MatterNote : " + matternote);
		return new ResponseEntity<>(matternote, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Search MatterNote") // label for swagger
	@PostMapping("/matternote/findMatterNotes")
	public MatterNote[] findMatterNotes(@RequestBody SearchMatterNote searchMatterNote, 
			@RequestParam String authToken) {
		return managementService.findMatterNotes(searchMatterNote, authToken);
	}

	@ApiOperation(response = MatterNote.class, value = "Create MatterNote") // label for swagger
	@PostMapping("/matternote")
	public ResponseEntity<?> postMatterNote(@Valid @RequestBody MatterNote newMatterNote,
			@RequestParam String loginUserID, @RequestParam String authToken) 
					throws IllegalAccessException, InvocationTargetException {
		MatterNote createdMatterNote = managementService.createMatterNote(newMatterNote, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterNote, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Update MatterNote") // label for swagger
	@PatchMapping("/matternote/{matterNotesNumber}")
	public ResponseEntity<?> patchMatterNote(@PathVariable String matterNotesNumber,
			@Valid @RequestBody MatterNote updateMatterNote, @RequestParam String loginUserID, 
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterNote updatedMatterNote = managementService.updateMatterNote(matterNotesNumber, updateMatterNote,
				loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterNote, HttpStatus.OK);
	}

	@ApiOperation(response = MatterNote.class, value = "Delete MatterNote") // label for swagger
	@DeleteMapping("/matternote/{matterNotesNumber}")
	public ResponseEntity<?> deleteMatterNote(@PathVariable String matterNotesNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteMatterNote(matterNotesNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//---------------------------------Matter-Task-------------------------------------------------------
	@ApiOperation(response = MatterTask.class, value = "Get all MatterTask details") // label for swagger
	@GetMapping("/mattertask")
	public ResponseEntity<?> getMatterTasks(@RequestParam String authToken) {
		MatterTask[] matterTaskList = managementService.getMatterTasks(authToken);
		return new ResponseEntity<>(matterTaskList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Get a MatterTask") // label for swagger
	@GetMapping("/mattertask/{taskNumber}")
	public ResponseEntity<?> getMatterTask(@PathVariable String taskNumber, @RequestParam String authToken) {
		MatterTask mattertask = managementService.getMatterTask(taskNumber, authToken);
		log.info("MatterTask : " + mattertask);
		return new ResponseEntity<>(mattertask, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterTask.class, value = "Search MatterTask") // label for swagger
    @PostMapping("/mattertask/findMatterTasks")
    public MatterTask[] findMatterTasks(@RequestBody SearchMatterTask searchMatterTask,
    		@RequestParam String authToken) throws ParseException {
		return managementService.findMatterTasks(searchMatterTask, authToken);
	}

	@ApiOperation(response = MatterTask.class, value = "Create MatterTask") // label for swagger
	@PostMapping("/mattertask")
	public ResponseEntity<?> postMatterTask(@Valid @RequestBody MatterTask newMatterTask,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterTask createdMatterTask = managementService.createMatterTask(newMatterTask, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterTask, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Update MatterTask") // label for swagger
	@PatchMapping("/mattertask/{taskNumber}")
	public ResponseEntity<?> patchMatterTask(@PathVariable String taskNumber,
			@Valid @RequestBody MatterTask updateMatterTask, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterTask updatedMatterTask = managementService.updateMatterTask(taskNumber, updateMatterTask, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterTask, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTask.class, value = "Delete MatterTask") // label for swagger
	@DeleteMapping("/mattertask/{taskNumber}")
	public ResponseEntity<?> deleteMatterTask(@PathVariable String taskNumber, @RequestParam String loginUserID, 
			@RequestParam String authToken) {
		managementService.deleteMatterTask(taskNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//-------------------Matter-Document---------------------------------------------------------------------------
	
	@ApiOperation(response = MatterDocument.class, value = "Get all MatterDocument details") // label for swagger
	@GetMapping("/matterdocument")
	public ResponseEntity<?> getMatterDocuments(@RequestParam String authToken) {
		MatterDocument[] matterDocumentList = managementService.getMatterDocuments(authToken);
		return new ResponseEntity<>(matterDocumentList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterDocument.class, value = "Get a MatterDocument") // label for swagger
	@GetMapping("/matterdocument/{matterDocumentId}")
	public ResponseEntity<?> getMatterDocument(@PathVariable Long matterDocumentId, @RequestParam String authToken) {
		MatterDocument matterdocument = managementService.getMatterDocument(matterDocumentId, authToken);
		log.info("MatterDocument : " + matterdocument);
		return new ResponseEntity<>(matterdocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Search MatterDocument") // label for swagger
	@PostMapping("/matterdocument/findMatterDocument")
	public MatterDocument[] findMatterDocument(@RequestBody SearchMatterDocument searchMatterDocument, 
			@RequestParam String authToken) throws ParseException {
		return managementService.findMatterDocuments(searchMatterDocument, authToken);
	}

	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument") // label for swagger
	@PostMapping("/matterdocument")
	public ResponseEntity<?> postMatterDocument(@Valid @RequestBody MatterDocument newMatterDocument, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				managementService.createMatterDocument(newMatterDocument, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument") // label for swagger
	@PostMapping("/matterdocument/clientPortal")
	public ResponseEntity<?> postMatterDocuments(@Valid @RequestBody MatterDocument newMatterDocument, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				managementService.createClientPortalMatterDocuemnt(newMatterDocument, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Create MatterDocument - ClientPortal - Docs Upload") // label for swagger
	@PostMapping("/matterdocument/clientPortal/docsUpload")
	public ResponseEntity<?> postMatterDocumentsForClientPortal(@Valid @RequestBody MatterDocument newMatterDocument, 
			@RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				managementService.createMatterDocuemntForClientPortalDocsUpload(newMatterDocument, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Send Document to Docusign") // label for swagger
	@PostMapping("/matterdocument/docusign")
	public ResponseEntity<?> postMatterDocumentToDocusign(@RequestParam Long classId, @RequestParam String matterNumber, 
			@RequestParam String documentNumber, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument createdMatterDocument = 
				managementService.sendMatterDocumentToDocusign(classId, matterNumber, documentNumber, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Update MatterDocument") // label for swagger
	@PatchMapping("/matterdocument/{matterDocumentId}")
	public ResponseEntity<?> patchMatterDocument(@PathVariable Long matterDocumentId,
			@Valid @RequestBody MatterDocument updateMatterDocument, @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocument updatedMatterDocument = managementService.updateMatterDocument(matterDocumentId,
				updateMatterDocument, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterDocument, HttpStatus.OK);
	}
	@ApiOperation(response = MatterDocument.class, value = "Delete MatterDocument") // label for swagger
	@DeleteMapping("/matterdocument/{matterDocumentId}")
	public ResponseEntity<?> deleteMatterDocument(@PathVariable Long matterDocumentId,
												  @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		managementService.deleteMatterDocumentId(matterDocumentId,loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocument.class, value = "Docusign Envelope Document") // label for swagger
	@GetMapping("/matterdocument/{matterNumber}/docusign/download")
	public ResponseEntity<?> downloadMatterDocumentEnvelope (@PathVariable String matterNumber, @RequestParam Long classId,
			@RequestParam String documentNumber, @RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterDocument response = managementService.docusignMatterEnvelopeDownload(classId, matterNumber, documentNumber, loginUserID, authToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ApiOperation(response = Optional.class, value = "DocuSign Envelope Status") // label for swagger
   	@GetMapping("/matterdocument/{matterNumber}/docusign/envelope/status")
   	public ResponseEntity<?> getMatterEnvelopeStatusFromDocusign(@PathVariable String matterNumber, 
   			@RequestParam String loginUserID, @RequestParam String authToken) throws Exception {
    	EnvelopeStatus response = managementService.getDocusignMatterEnvelopeStatus(matterNumber, loginUserID, authToken);
   		return new ResponseEntity<>(response, HttpStatus.OK);
   	}
	
	@ApiOperation(response = MatterDocument.class, value = "Process Edited MatterDocument") // label for swagger
   	@PatchMapping("/matterdocument/{matterDocumentId}/mailmerge/manual")
   	public ResponseEntity<?> postProcessEditedMatterDocument(@PathVariable Long matterDocumentId, @RequestParam Long classId, 
   			@RequestParam String location, @RequestParam("file") MultipartFile file, @RequestParam String loginUserID, @RequestParam String authToken) 
   					throws Exception {
		MatterDocument createdMatterDocument = 
   				managementService.doProcessEditedMattertDocument(classId, matterDocumentId, location, file, loginUserID, authToken);
   		return new ResponseEntity<>(createdMatterDocument, HttpStatus.OK);
   	}
	
	//--------------------MATTER-ASSIGNMENT-------------------------------------------------------------------------
	@ApiOperation(response = MatterAssignment.class, value = "Get all MatterAssignment details") // label for swagger
	@GetMapping("/matterassignment")
	public ResponseEntity<?> getMatterAssignments(@RequestParam String authToken) {
		MatterAssignment[] matterAssignmentList = managementService.getMatterAssignments(authToken);
		return new ResponseEntity<>(matterAssignmentList, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterAssignment.class, value = "Get all MatterAssignment details") // label for swagger
	@GetMapping("/matterassignment/pagination")
	public ResponseEntity<?> getAllMatterAssignments(@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "10") Integer pageSize,
									@RequestParam(defaultValue = "matterNumber") String sortBy,
									@RequestParam String authToken) {
		Page<MatterAssignment> list = managementService.getAllMatterAssignments(pageNo, pageSize, sortBy, authToken);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK); 
	}

	@ApiOperation(response = MatterAssignment.class, value = "Get a MatterAssignment") // label for swagger
	@GetMapping("/matterassignment/{matterNumber}")
	public ResponseEntity<?> getMatterAssignment(@PathVariable String matterNumber, @RequestParam String authToken) {
		MatterAssignment matterassignment = managementService.getMatterAssignment(matterNumber, authToken);
		log.info("MatterAssignment : " + matterassignment);
		return new ResponseEntity<>(matterassignment, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterAssignment.class, value = "Search MatterAssignment") // label for swagger
	@PostMapping("/matterassignment/findMatterAssignment")
	public MatterAssignment[] findMatterAssignment(@RequestBody SearchMatterAssignment searchMatterAssignment, @RequestParam String authToken)
			throws ParseException {
		return managementService.findMatterAssignment(searchMatterAssignment, authToken);
	}

//	@ApiOperation(response = MatterAssignment.class, value = "Search MatterAssignment New") // label for swagger
//	@PostMapping("/matterassignment/findMatterAssignmentNew")
//	public MatterAssignment[] findMatterAssignmentNew(@RequestBody SearchMatterAssignment searchMatterAssignment, @RequestParam String authToken)
//			throws ParseException {
//		return managementService.findMatterAssignmentNew(searchMatterAssignment, authToken);
//	}
	//Stream
	@ApiOperation(response = MatterAssignment.class, value = "Search MatterAssignment Stream") // label for swagger
	@PostMapping("/matterassignment/findMatterAssignmentStream")
	public MatterAssignment[] findMatterAssignmentStream(@RequestBody SearchMatterAssignment searchMatterAssignment, @RequestParam String authToken)
			throws ParseException {
		return managementService.findMatterAssignmentStream(searchMatterAssignment, authToken);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Create MatterAssignment") // label for swagger
	@PostMapping("/matterassignment")
	public ResponseEntity<?> postMatterAssignment(@Valid @RequestBody MatterAssignment newMatterAssignment,
			@RequestParam String loginUserID, @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterAssignment createdMatterAssignment = managementService.createMatterAssignment(newMatterAssignment,
				loginUserID, authToken);
		return new ResponseEntity<>(createdMatterAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Update MatterAssignment") // label for swagger
	@PatchMapping("/matterassignment/{matterNumber}")
	public ResponseEntity<?> patchMatterAssignment(@PathVariable String matterNumber,
			@Valid @RequestBody MatterAssignment updateMatterAssignment, @RequestParam String loginUserID,
			@RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterAssignment updatedMatterAssignment = managementService.updateMatterAssignment(matterNumber,
				updateMatterAssignment, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterAssignment, HttpStatus.OK);
	}

	@ApiOperation(response = MatterAssignment.class, value = "Delete MatterAssignment") // label for swagger
	@DeleteMapping("/matterassignment/{matterNumber}")
	public ResponseEntity<?> deleteMatterAssignment(@PathVariable String matterNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteMatterAssignment(matterNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//--------------------MATTER-TIME-TICKET-------------------------------------------------------------------------
	@ApiOperation(response = MatterTimeTicket.class, value = "Get all MatterTimeTicket details") // label for swagger
	@GetMapping("/mattertimeticket")
	public ResponseEntity<?> getMatterTimeTickets(@RequestParam String authToken) {
		MatterTimeTicket[] matterTimeTicketList = managementService.getMatterTimeTickets(authToken);
		return new ResponseEntity<>(matterTimeTicketList, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterTimeTicket.class, value = "Get a MatterTimeTicket for PreBill Approve") // label for swagger 
	@GetMapping("/mattertimeticket/{preBillNo}/preBillApprove")
	public ResponseEntity<?> getMatterTimeTicketPreBill(@PathVariable String preBillNo, @RequestParam String authToken) {
    	MatterTimeTicket[] mattertimeticket = managementService.getMatterTimeTicketPreBill(preBillNo, authToken);
    	log.info("MatterTimeTicket : " + mattertimeticket);
		return new ResponseEntity<>(mattertimeticket, HttpStatus.OK);
	}
	    
    @ApiOperation(response = MatterTimeTicket.class, value = "Get a MatterTimeTicket") // label for swagger 
	@GetMapping("/mattertimeticket/{timeTicketNumber}")
	public ResponseEntity<?> getMatterTimeTicket(@PathVariable String timeTicketNumber, @RequestParam String authToken) {
    	MatterTimeTicket mattertimeticket = managementService.getMatterTimeTicket(timeTicketNumber, authToken);
    	log.info("MatterTimeTicket : " + mattertimeticket);
		return new ResponseEntity<>(mattertimeticket, HttpStatus.OK);
	}

	@ApiOperation(response = MatterTimeTicketMobile.class, value = "Get a MatterTimeTicket Mobile") // label for swagger
	@GetMapping("/mattertimeticket/mobile/{timeTicketNumber}")
	public ResponseEntity<?> getMatterTimeTicketMobile(@PathVariable String timeTicketNumber, @RequestParam String authToken) {
    	MatterTimeTicketMobile mattertimeticket = managementService.getMatterTimeTicketMobile(timeTicketNumber, authToken);
    	log.info("MatterTimeTicket : " + mattertimeticket);
		return new ResponseEntity<>(mattertimeticket, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterTimeTicket.class, value = "Search MatterTimeTicket") // label for swagger
	@PostMapping("/mattertimeticket/findMatterTimeTicket")
	public MatterTimeTicket[] findMatterTimeTicket(@RequestBody SearchMatterTimeTicket searchMatterTimeTicket, 
			@RequestParam String authToken) throws Exception {
		return managementService.findMatterTimeTickets(searchMatterTimeTicket, authToken); 
	}

	//findTimeKeeperBilling Report
	@ApiOperation(response = TimeKeeperBillingReport.class, value = "Search Time Keeper Billing Report") // label for swagger
	@PostMapping("/mattertimeticket/findTimeKeeperBillingReport")
	public TimeKeeperBillingReport[] findTimeKeeperBillingReport(@RequestBody TimeKeeperBillingReportInput timeKeeperBillingReportInput,
													@RequestParam String authToken) throws Exception {
		return managementService.findTimeKeeperBillingReport(timeKeeperBillingReportInput, authToken);
	}
    
    @ApiOperation(response = MatterTimeTicket.class, value = "Create MatterTimeTicket") // label for swagger
	@PostMapping("/mattertimeticket")
	public ResponseEntity<?> postMatterTimeTicket(@Valid @RequestBody MatterTimeTicket newMatterTimeTicket, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws Exception {
		MatterTimeTicket createdMatterTimeTicket = 
				managementService.createMatterTimeTicket(newMatterTimeTicket, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterTimeTicket , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterTimeTicket.class, value = "Update MatterTimeTicket") // label for swagger
    @PatchMapping("/mattertimeticket/{timeTicketNumber}")
	public ResponseEntity<?> patchMatterTimeTicket(@PathVariable String timeTicketNumber, 
			@Valid @RequestBody MatterTimeTicket updateMatterTimeTicket, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws Exception {
		MatterTimeTicket updatedMatterTimeTicket = 
				managementService.updateMatterTimeTicket(timeTicketNumber, updateMatterTimeTicket, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterTimeTicket , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterTimeTicket.class, value = "Delete MatterTimeTicket") // label for swagger
	@DeleteMapping("/mattertimeticket/{timeTicketNumber}")
	public ResponseEntity<?> deleteMatterTimeTicket(@PathVariable String timeTicketNumber,
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	managementService.deleteMatterTimeTicket(timeTicketNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //----------------------Matter-Rate-----------------------------------------------------------------------
    
    @ApiOperation(response = MatterRate.class, value = "Get all MatterRate details") // label for swagger
	@GetMapping("/matterrate")
	public ResponseEntity<?> getMatterRateList(@RequestParam String authToken) {
		MatterRate[] matterRateList = managementService.getMatterRates(authToken);
		return new ResponseEntity<>(matterRateList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Get a MatterRate") // label for swagger 
	@GetMapping("/matterrate/{matterNumber}")
	public ResponseEntity<?> getMatterRate(@PathVariable String matterNumber, 
			@RequestParam String timeKeeperCode, @RequestParam String authToken) {
    	MatterRate matterrate = managementService.getMatterRate (matterNumber, timeKeeperCode, authToken);
    	log.info("MatterRate : " + matterrate);
		return new ResponseEntity<>(matterrate, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Get a MatterRate") // label for swagger 
	@GetMapping("/matterrate/{matterNumber}/matterNumber")
	public ResponseEntity<?> getMatterRate(@PathVariable String matterNumber,
			@RequestParam String authToken) {
    	MatterRate[] matterrate = managementService.getMatterRate(matterNumber, authToken);
		return new ResponseEntity<>(matterrate, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Create MatterRate") // label for swagger
	@PostMapping("/matterrate")
	public ResponseEntity<?> postMatterRate(@Valid @RequestBody MatterRate newMatterRate, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterRate createdMatterRate = managementService.createMatterRate(newMatterRate, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterRate , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterRate.class, value = "Update MatterRate") // label for swagger
    @PatchMapping("/matterrate/{matterNumber}")
	public ResponseEntity<?> patchMatterRate(@PathVariable String matterNumber, @RequestParam String timeKeeperCode, @Valid @RequestBody MatterRate updateMatterRate, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterRate updatedMatterRate = managementService.updateMatterRate(matterNumber, timeKeeperCode, updateMatterRate, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterRate , HttpStatus.OK);
	}
    
    //-----------------------Matter-Fee-Sharing-------------------------------------------------------------------
    @ApiOperation(response = MatterFeeSharing.class, value = "Get all MatterFeeSharing details") // label for swagger
	@GetMapping("/matterfeesharing")
	public ResponseEntity<?> getMatterFeeSharingList(@RequestParam String authToken) {
		MatterFeeSharing[] matterFeeSharingList = managementService.getMatterFeeSharings(authToken);
		return new ResponseEntity<>(matterFeeSharingList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Get a MatterFeeSharing") // label for swagger 
	@GetMapping("/matterfeesharing/{matterNumber}")
	public ResponseEntity<?> getMatterFeeSharing(@PathVariable String matterNumber, @RequestParam String timeKeeperCode, @RequestParam String authToken) {
    	MatterFeeSharing matterfeesharing = managementService.getMatterFeeSharing(matterNumber, timeKeeperCode, authToken);
    	log.info("MatterFeeSharing : " + matterfeesharing);
		return new ResponseEntity<>(matterfeesharing, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Create MatterFeeSharing") // label for swagger
	@PostMapping("/matterfeesharing")
	public ResponseEntity<?> postMatterFeeSharing(@Valid @RequestBody MatterFeeSharing newMatterFeeSharing, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
					throws IllegalAccessException, InvocationTargetException {
		MatterFeeSharing createdMatterFeeSharing = managementService.createMatterFeeSharing(newMatterFeeSharing, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterFeeSharing , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterFeeSharing.class, value = "Update MatterFeeSharing") // label for swagger
    @PatchMapping("/matterfeesharing/{matterNumber}")
	public ResponseEntity<?> patchMatterFeeSharing(@PathVariable String matterNumber, @RequestParam String timeKeeperCode,
			@Valid @RequestBody MatterFeeSharing updateMatterFeeSharing, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterFeeSharing updatedMatterFeeSharing = 
				managementService.updateMatterFeeSharing(matterNumber, timeKeeperCode, updateMatterFeeSharing, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterFeeSharing , HttpStatus.OK);
	}
    
    //-------------------------Matter-Expense---------------------------------------------------------------------
    @ApiOperation(response = MatterExpense.class, value = "Get all MatterExpense details") // label for swagger
	@GetMapping("/matterexpense")
	public ResponseEntity<?> getMatterExpenseList( @RequestParam String authToken ) {
		MatterExpense[] matterExpenseList = managementService.getMatterExpenses(authToken);
		return new ResponseEntity<>(matterExpenseList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Get a MatterExpense") // label for swagger 
	@GetMapping("/matterexpense/{preBillNo}/preBillApprove")
	public ResponseEntity<?> getMatterExpensePreBill(@PathVariable String preBillNo, @RequestParam String authToken) {
    	MatterExpense[] matterexpense = managementService.getMatterExpensePreBill(preBillNo, authToken);
    	log.info("MatterExpense : " + matterexpense);
		return new ResponseEntity<>(matterexpense, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Get a MatterExpense") // label for swagger 
	@GetMapping("/matterexpense/{matterExpenseId}")
	public ResponseEntity<?> getMatterExpense(@PathVariable Long matterExpenseId, @RequestParam String authToken) {
    	MatterExpense matterexpense = managementService.getMatterExpense(matterExpenseId, authToken);
    	log.info("MatterExpense : " + matterexpense);
		return new ResponseEntity<>(matterexpense, HttpStatus.OK);
	}

	@ApiOperation(response = MatterExpense.class, value = "Search MatterExpense") // label for swagger
	@PostMapping("/matterexpense/findMatterExpenses")
	public MatterExpense[] findMatterExpenses(@RequestBody SearchMatterExpense searchMatterExpense,
											  @RequestParam String authToken) throws ParseException {
		return managementService.findMatterExpenses(searchMatterExpense, authToken);
	}

	@ApiOperation(response = MatterExpense.class, value = "Search MatterExpense New") // label for swagger
	@PostMapping("/matterexpense/findMatterExpensesNew")
	public MatterExpense[] findMatterExpensesNew(@RequestBody SearchMatterExpense searchMatterExpense,
												 @RequestParam String authToken) throws ParseException {
		return managementService.findMatterExpensesNew(searchMatterExpense, authToken);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Create MatterExpense") // label for swagger
	@PostMapping("/matterexpense")
	public ResponseEntity<?> postMatterExpense(@Valid @RequestBody MatterExpense newMatterExpense, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense createdMatterExpense = managementService.createMatterExpense(newMatterExpense, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Update MatterExpense") // label for swagger
    @PatchMapping("/matterexpense/{matterExpenseId}")
	public ResponseEntity<?> patchMatterExpense(@PathVariable Long matterExpenseId, 
			@Valid @RequestBody MatterExpense updateMatterExpense, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense updatedMatterExpense = 
				managementService.updateMatterExpense(matterExpenseId, updateMatterExpense, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Update MatterExpense") // label for swagger
    @PatchMapping("/matterexpense/status")
	public ResponseEntity<?> patchMatterExpense(@RequestBody List<MatterExpense> updateMatterExpense, @RequestParam String loginUserID, 
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterExpense[] updatedMatterExpense = managementService.updateMatterExpense(updateMatterExpense, 
				loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterExpense , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterExpense.class, value = "Delete MatterExpense") // label for swagger
	@DeleteMapping("/matterexpense/{matterExpenseId}")
	public ResponseEntity<?> deleteMatterExpense(@PathVariable Long matterExpenseId, @RequestParam String loginUserID,
			@RequestParam String authToken) {
    	managementService.deleteMatterExpense(matterExpenseId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    //-------------------------MatterITForm---------------------------------------------------------------------
    
    @ApiOperation(response = MatterITForm.class, value = "Get all MatterITForm details") // label for swagger
	@GetMapping("/matteritform")
	public ResponseEntity<?> getMatterITFormList(@RequestParam String authToken) {
		MatterITForm[] matterITFormList = managementService.getMatterITForms(authToken);
		return new ResponseEntity<>(matterITFormList, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Get a MatterITForm") // label for swagger 
	@GetMapping("/matteritform/{intakeFormNumber}")
	public ResponseEntity<?> getMatterITForm(@PathVariable String intakeFormNumber, @RequestParam String authToken) {
    	MatterITForm matterITForm = managementService.getMatterITForm(intakeFormNumber, authToken);
//    	log.info("MatterITForm : " + matterITForm);
		return new ResponseEntity<>(matterITForm, HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Search MatterDocument") // label for swagger
	@PostMapping("/matteritform/findMatterITForm")
	public MatterITForm[] findMatterITForm(@RequestBody SearchMatterITForm searchMatterITForm, @RequestParam String authToken)
			throws ParseException {
		return managementService.findMatterITForm(searchMatterITForm, authToken);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Create MatterITForm") // label for swagger
	@PostMapping("/matteritform")
	public ResponseEntity<?> postMatterITForm(@Valid @RequestBody MatterITForm newMatterITForm, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm createdMatterITForm = managementService.createMatterITForm(newMatterITForm, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Update MatterITForm") // label for swagger
    @PatchMapping("/matteritform/{intakeFormNumber}")
	public ResponseEntity<?> patchMatterITForm(@PathVariable String intakeFormNumber, 
			@Valid @RequestBody MatterITForm updateMatterITForm, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm updatedMatterITForm = 
				managementService.updateMatterITForm(intakeFormNumber, updateMatterITForm, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Update MatterITForm") // label for swagger
    @PatchMapping("/matteritform/{intakeFormNumber}/approve")
	public ResponseEntity<?> patchMatterITFormApprove(@PathVariable String intakeFormNumber, 
			@Valid @RequestBody MatterITForm updateMatterITForm, @RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm updatedMatterITForm = 
				managementService.approveMatterITForm(intakeFormNumber, updateMatterITForm, loginUserID, authToken);
		return new ResponseEntity<>(updatedMatterITForm , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterITForm.class, value = "Delete MatterITForm") // label for swagger
	@DeleteMapping("/matteritform/{intakeFormNumber}")
	public ResponseEntity<?> deleteMatterITForm(@PathVariable String intakeFormNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
    	managementService.deleteMatterITForm(intakeFormNumber, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
    /* -----------------------------EXPIRATIONDATE----------------------------------------------------------------*/
    
	@ApiOperation(response = ExpirationDate.class, value = "Get a ExpirationDate") // label for swagger
	@RequestMapping(value = "/expirationdate", method = RequestMethod.GET)
   	public ResponseEntity<?> getExpirationDates(@RequestParam String authToken) throws Exception {
		ExpirationDate[] expirationdate = managementService.getExpirationDates(authToken);
    	log.info("ExpirationDate : " + expirationdate);
		return new ResponseEntity<>(expirationdate, HttpStatus.OK); 
	}
    
	@ApiOperation(response = ExpirationDate.class, value = "Get a ExpirationDate") // label for swagger 
	@GetMapping("/expirationdate/{matterNo}")
	public ResponseEntity<?> getExpirationDate(@PathVariable String matterNo, @RequestParam String languageId,
			@RequestParam Long classId, @RequestParam String clientId, String documentType, @RequestParam String authToken) throws Exception {
    	ExpirationDate expirationdate = managementService.getExpirationDate(matterNo, languageId, classId, clientId, documentType, authToken);
    	log.info("ExpirationDate : " + expirationdate);
		return new ResponseEntity<>(expirationdate, HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Create New ExpirationDate") // label for swagger
    @RequestMapping(value = "/expirationdate", method = RequestMethod.POST)
	public ResponseEntity<?> createExpirationDate(@RequestBody ExpirationDate newExpirationDate, @RequestParam String loginUserID, 
			@RequestParam String authToken) throws Exception { 
			ExpirationDate createdExpirationDate = managementService.addExpirationDate(newExpirationDate, loginUserID, authToken);
		return new ResponseEntity<>(createdExpirationDate , HttpStatus.OK);
	}
    
    @ApiOperation(response = Optional.class, value = "Update ExpirationDate") // label for swagger
    @RequestMapping(value = "/expirationdate/{matterNo}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateExpirationDate(@PathVariable String matterNo, 
			@RequestBody ExpirationDate updatedExpirationDate, @RequestParam String loginUserID, @RequestParam String authToken) 
					throws Exception {		
		ExpirationDate modifiedExpirationDate = managementService.updateExpirationDate(matterNo, updatedExpirationDate, loginUserID, authToken);
		return new ResponseEntity<>(modifiedExpirationDate , HttpStatus.OK);
	}
    
    @ApiOperation(response = ExpirationDate.class, value = "Delete ExpirationDate") // label for swagger
   	@DeleteMapping("/expirationdate/{matterNo}")
   	public ResponseEntity<?> deleteExpirationDate(@PathVariable String matterNo, @RequestParam String languageId,
   			@RequestParam Long classId, @RequestParam String clientId, String documentType, @RequestParam String loginUserID,
   			@RequestParam String authToken) {
    	managementService.deleteExpirationDate(matterNo, languageId, classId, clientId, documentType, loginUserID, authToken);
   		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   	}

	@ApiOperation(response = ExpirationDate.class, value = "Search ExpirationDate") // label for swagger
	@PostMapping("/expirationdate/findExpirationDate")
	public ExpirationDate[] findExpirationDate(@RequestBody SearchExpirationDate searchExpirationDate,
												   @RequestParam String authToken) throws Exception {
		return managementService.findExpirationDate(searchExpirationDate, authToken);
	}
    
	/*
	 * --------------------------------ReceiptAppNotice---------------------------------
	 */
	@ApiOperation(response = ReceiptAppNotice.class, value = "Get all ReceiptAppNotice details") // label for swagger
	@GetMapping("/receiptappnotice")
	public ResponseEntity<?> getReceiptAppNotices(@RequestParam String authToken) {
		ReceiptAppNotice[] receiptNoList = managementService.getReceiptAppNotices(authToken);
		return new ResponseEntity<>(receiptNoList, HttpStatus.OK);
	}

	@ApiOperation(response = ReceiptAppNotice.class, value = "Get a ReceiptAppNotice") // label for swagger
	@GetMapping("/receiptappnotice/{receiptNo}")
	public ResponseEntity<?> getReceiptAppNotice(@PathVariable String receiptNo, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber, @RequestParam String authToken) {
		ReceiptAppNotice dbReceiptAppNotice = managementService.getReceiptAppNotice(languageId, classId, matterNumber, receiptNo, authToken);
		log.info("ReceiptAppNotice : " + dbReceiptAppNotice);
		return new ResponseEntity<>(dbReceiptAppNotice, HttpStatus.OK);
	}

	@ApiOperation(response = ReceiptAppNotice.class, value = "Create ReceiptAppNotice") // label for swagger
	@PostMapping("/receiptappnotice")
	public ResponseEntity<?> postReceiptAppNotice(@RequestBody ReceiptAppNotice newReceiptAppNotice, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
					throws IllegalAccessException, InvocationTargetException, DbxException, ParseException {
		ReceiptAppNotice createdReceiptAppNotice = 
				managementService.createReceiptAppNotice(newReceiptAppNotice, loginUserID, authToken);
		return new ResponseEntity<>(createdReceiptAppNotice, HttpStatus.OK);
	}

	@ApiOperation(response = ReceiptAppNotice.class, value = "Update ReceiptAppNotice") // label for swagger
	@RequestMapping(value = "/receiptappnotice", method = RequestMethod.PATCH)
	public ResponseEntity<?> patchReceiptAppNotice(@RequestParam String receiptNo, @RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber, @RequestParam String loginUserID,
			@RequestParam String authToken, @RequestBody ReceiptAppNotice updateReceiptAppNotice)
			throws Exception {
		ReceiptAppNotice updatedReceiptAppNotice = 
				managementService.updateReceiptAppNotice(languageId, classId, matterNumber, receiptNo, loginUserID, updateReceiptAppNotice, authToken);
		return new ResponseEntity<>(updatedReceiptAppNotice, HttpStatus.OK);
	}

	@ApiOperation(response = ReceiptAppNotice.class, value = "Delete ReceiptAppNotice") // label for swagger
	@DeleteMapping("/receiptappnotice/{receiptNo}")
	public ResponseEntity<?> deleteReceiptAppNotice(@PathVariable String receiptNo, 
			@RequestParam String languageId, 
			@RequestParam Long classId, @RequestParam String matterNumber, 
			@RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteReceiptAppNotice(languageId, classId, matterNumber, receiptNo, 
				loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//--------------------------Reports-----------------------------------------------------------
	@ApiOperation(response = ClientGeneralLNEReport[].class, value = "LNE ClientGeneral Report") // label for swagger
	@PostMapping("/clientgeneral/lneReport")
	public ResponseEntity<?> getLNEClientGeneralReport (@RequestBody SearchClientGeneralLNEReport searchClientGeneralReport,
			@RequestParam String authToken) throws ParseException {
		ClientGeneralLNEReport[] clientGeneralReport = 
				managementService.getLNEClientGeneralReport(searchClientGeneralReport, authToken);
		log.info("clientGeneralReport : " + clientGeneralReport);
		return new ResponseEntity<>(clientGeneralReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = ClientGeneralIMMReport[].class, value = "Client General IMM Report") // label for swagger
	@PostMapping("/clientgeneral/immigrationReport")
	public ResponseEntity<?> getIMMClientGeneralReport (@RequestBody SearchClientGeneralIMMReport searchClientGeneralIMMReport,
			@RequestParam String authToken) throws ParseException {
		ClientGeneralIMMReport[] clientGeneralImmReport = 
				managementService.getIMMClientGeneralReport(searchClientGeneralIMMReport, authToken);
		log.info("clientGeneralImmReport : " + clientGeneralImmReport);
		return new ResponseEntity<>(clientGeneralImmReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterImmigrationReport[].class, value = "Matter Immigration Report") // label for swagger
	@PostMapping("/mattergenacc/immigrationReport")
	public ResponseEntity<?> getMatterImmigrationReport (@RequestBody ImmigrationMatter immigrationMatter, @RequestParam String authToken)
					throws ParseException {
		MatterImmigrationReport[] matterImmigrationReport = 
				managementService.getMatterImmigrationReport(immigrationMatter, authToken);
		log.info("matterImmigrationReport : " + matterImmigrationReport);
		return new ResponseEntity<>(matterImmigrationReport, HttpStatus.OK);
	}
    @ApiOperation(response = AttorneyProductivityReport[].class, value = "Attorney Productivity Report") // label for swagger
    @PostMapping("/mattergenacc/attorneyProductivityReport")
    public ResponseEntity<?> getAttorneyProductivityReport (@RequestBody AttorneyProductivityInput attorneyProductivityInput, @RequestParam String authToken)
            throws ParseException {
        AttorneyProductivityReport[] attorneyProductivityReports =
                managementService.getAttorneyProductivityReport(attorneyProductivityInput, authToken);
        return new ResponseEntity<>(attorneyProductivityReports, HttpStatus.OK);
    }
	
	@ApiOperation(response = MatterLNEReport[].class, value = "Matter LNE Report") // label for swagger
	@PostMapping("/mattergenacc/lneReport")
	public ResponseEntity<?> getMatterLNEReport (@RequestBody LNEMatter lneMatter, 
			@RequestParam String authToken) throws ParseException {
		MatterLNEReport[] matterLNEReport = managementService.getMatterLNEReport(lneMatter, authToken);
		log.info("matterLNEReport : " + matterLNEReport);
		return new ResponseEntity<>(matterLNEReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = WIPAgedPBReport[].class, value = "WIPAgedPBReport") // label for swagger
	@PostMapping("/mattergenacc/wipAgedPBReport")
	public ResponseEntity<?> getWIPAgedPBReport (@RequestBody WIPAgedPBReportInput wipAgedPBReportInput,
			@RequestParam String authToken) throws ParseException {
		log.info("WIPAgedPBReportInput FromDate: " + wipAgedPBReportInput.getFromDate());
		log.info("WIPAgedPBReportInput ToDate: " + wipAgedPBReportInput.getToDate());
		
		WIPAgedPBReport[] matterLNEReport = managementService.getWIPAgedPBReport(wipAgedPBReportInput, authToken);
		log.info("matterLNEReport : " + matterLNEReport);
		return new ResponseEntity<>(matterLNEReport, HttpStatus.OK);
	}
	
	@ApiOperation(response = WIPAgedPBReport.class, value = "WIPAgedPBReport") // label for swagger
	@PostMapping("/mattergenacc/wipAgedPBReport/pagination")
	public ResponseEntity<?> getWIPAgedPBReport (@RequestParam(defaultValue = "0") Integer pageNo,
									@RequestParam(defaultValue = "100") Integer pageSize,
									@RequestParam(defaultValue = "MATTER_NO") String sortBy,
									@RequestBody WIPAgedPBReportInput wipAgedPBReportInput,
									@RequestParam String authToken) throws Exception {
		Page<?> pageWIPAgedPBReport = 
				managementService.getWIPAgedPBReport(wipAgedPBReportInput, pageNo, pageSize, sortBy, authToken);
		log.info("WIPAgedPBReport : " + pageWIPAgedPBReport);
        return new ResponseEntity<>(pageWIPAgedPBReport, new HttpHeaders(), HttpStatus.OK);
	}
	
	//------------------------Matter-Doc-list--------------------------------------------------------------
	@ApiOperation(response = MatterDocList.class, value = "Get all MatterDocList details") // label for swagger
	@GetMapping("/matterdoclist")
	public ResponseEntity<?> getMatterDocLists(@RequestParam String authToken) {
		MatterDocList[] matterdoclistList = managementService.getMatterDocLists(authToken);
		return new ResponseEntity<>(matterdoclistList, HttpStatus.OK); 
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Get a MatterDocList") // label for swagger 
	@GetMapping("/matterdoclist/{matterNumber}")
	public ResponseEntity<?> getMatterDocList(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId,
		@RequestParam String authToken) {
    	MatterDocList matterdoclist = 
    			managementService.getMatterDocList(languageId, classId, checkListNo, matterNumber, clientId, authToken);
    	log.info("MatterDocList : " + matterdoclist);
		return new ResponseEntity<>(matterdoclist, HttpStatus.OK);
	}
    
	@ApiOperation(response = MatterDocList.class, value = "Search MatterDocList") // label for swagger
	@PostMapping("/matterdoclist/findMatterDocList")
	public MatterDocList[] findMatterDocList(@RequestBody SearchMatterDocList searchMatterDocList,
			@RequestParam String authToken)
			throws Exception {
		return managementService.findMatterDocList(searchMatterDocList, authToken);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Create MatterDocList") // label for swagger
	@PostMapping("/matterdoclist")
	public ResponseEntity<?> postMatterDocList(@Valid @RequestBody List<MatterDocList> newMatterDocList, 
			@RequestParam String loginUserID, @RequestParam String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		MatterDocList[] createdMatterDocList = managementService.createMatterDocList(newMatterDocList, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocList , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Update MatterDocList") // label for swagger
    @GetMapping("/matterdoclist/{matterNumber}/clientPortal/docCheckList")
	public ResponseEntity<?> patchMatterDocList(@PathVariable String matterNumber, @RequestParam String clientId, 
			@RequestParam Long checkListNo, @RequestParam Long matterHeaderId, @RequestParam String loginUserID, 
			@RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
    	MatterDocListHeader createdMatterDocList = 
				managementService.updateMatterDocList(clientId, matterNumber, checkListNo, matterHeaderId, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocList , HttpStatus.OK);
	}
    
    @ApiOperation(response = MatterDocList.class, value = "Delete MatterDocList") // label for swagger
	@DeleteMapping("/matterdoclist/{matterNumber}")
	public ResponseEntity<?> deleteMatterDocList(@PathVariable String matterNumber, @RequestParam String languageId, 
		@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId, 
		@RequestParam String loginUserID, @RequestParam String authToken) {
    	managementService.deleteMatterDocList(languageId, classId, checkListNo, matterNumber, clientId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	//------------------------Matter-Doc-list Header--------------------------------------------------------------
	@ApiOperation(response = MatterDocListHeader.class, value = "Get all MatterDocListHeader details") // label for swagger
	@GetMapping("/matterdoclistheader")
	public ResponseEntity<?> getMatterDocListHeaders(@RequestParam String authToken) {
		MatterDocListHeader[] matterdoclistheaderList = managementService.getMatterDocListHeaders(authToken);
		return new ResponseEntity<>(matterdoclistheaderList, HttpStatus.OK);
	}

	@ApiOperation(response = MatterDocListHeader.class, value = "Get a MatterDocListHeader") // label for swagger
	@GetMapping("/matterdoclistheader/{matterNumber}")
	public ResponseEntity<?> getMatterDocListHeader(@PathVariable String matterNumber, @RequestParam String languageId,
													@RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId,
													@RequestParam String authToken) {
		MatterDocListHeader matterdoclistheader =
				managementService.getMatterDocListHeader(languageId, classId, checkListNo, matterNumber, clientId, authToken);
		log.info("MatterDocListHeader : " + matterdoclistheader);
		return new ResponseEntity<>(matterdoclistheader, HttpStatus.OK);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Get a MatterDocListHeader-New") // label for swagger
	@GetMapping("/matterdoclistheader/new/{matterHeaderId}")
	public ResponseEntity<?> getMatterDocListHeader(@PathVariable Long matterHeaderId,
													@RequestParam String authToken) {
		MatterDocListHeader matterdoclistheader =
				managementService.getMatterDocListHeader(matterHeaderId, authToken);
		return new ResponseEntity<>(matterdoclistheader, HttpStatus.OK);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Create MatterDocListHeader") // label for swagger
	@PostMapping("/matterdoclistheader")
	public ResponseEntity<?> postMatterDocListHeader(@Valid @RequestBody AddMatterDocListHeader newMatterDocListHeader,
													 @RequestParam String loginUserID, @RequestParam String authToken)
			throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader = managementService.createMatterDocListHeader(newMatterDocListHeader, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}

	@ApiOperation(response = MatterDocListHeader.class, value = "Update MatterDocListHeader") // label for swagger
	@PatchMapping("/matterdoclistheader/{matterNumber}")
	public ResponseEntity<?> patchMatterDocListHeader(@Valid @RequestBody UpdateMatterDocListHeader updateMatterDocListHeader,@PathVariable String matterNumber, @RequestParam String clientId,
													  @RequestParam String languageId,@RequestParam Long checkListNo, @RequestParam Long classId, @RequestParam String loginUserID,
													  @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader =
				managementService.updateMatterDocListHeader(updateMatterDocListHeader, clientId, matterNumber, checkListNo, classId,languageId, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}
	
	@ApiOperation(response = MatterDocListHeader.class, value = "Update MatterDocListHeader-New") // label for swagger
	@PatchMapping("/matterdoclistheader/new/{matterHeaderId}")
	public ResponseEntity<?> patchMatterDocListHeader(@Valid @RequestBody UpdateMatterDocListHeader updateMatterDocListHeader,@PathVariable Long matterHeaderId,
													  @RequestParam String loginUserID,
													  @RequestParam String authToken) throws IllegalAccessException, InvocationTargetException {
		MatterDocListHeader createdMatterDocListHeader =
				managementService.updateMatterDocListHeader(updateMatterDocListHeader, matterHeaderId, loginUserID, authToken);
		return new ResponseEntity<>(createdMatterDocListHeader , HttpStatus.OK);
	}

	@ApiOperation(response = MatterDocListHeader.class, value = "Delete MatterDocListHeader") // label for swagger
	@DeleteMapping("/matterdoclistheader/{matterNumber}")
	public ResponseEntity<?> deleteMatterDocListHeader(@PathVariable String matterNumber, @RequestParam String languageId,
													   @RequestParam Long classId, @RequestParam Long checkListNo, @RequestParam String clientId,
													   @RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteMatterDocListHeader(languageId, classId, checkListNo, matterNumber, clientId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@ApiOperation(response = MatterDocListHeader.class, value = "Delete MatterDocListHeader-New") // label for swagger
	@DeleteMapping("/matterdoclistheader/new/{matterHeaderId}")
	public ResponseEntity<?> deleteMatterDocListHeader(@PathVariable Long matterHeaderId,
													   @RequestParam String loginUserID, @RequestParam String authToken) {
		managementService.deleteMatterDocListHeader( matterHeaderId, loginUserID, authToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(response = MatterDocListHeader.class, value = "Search MatterDocListHeader") // label for swagger
	@PostMapping("/matterdoclistheader/find")
	public MatterDocListHeader[] findMatterDocListHeader(@RequestBody FindMatterDocListHeader searchMatterDocList,
											 @RequestParam String authToken)
			throws Exception {
		return managementService.findMatterDocListHeader(searchMatterDocList, authToken);
	}

	// Search TimeTicketNotification
	@ApiOperation(response = TimeTicketNotification.class, value = "Search TimeTicketNotification") // label for swagger
	@PostMapping("/timeticketnotification/find")
	public TimeTicketNotification[] findTimeTicketNotification(@RequestBody FindTimeTicketNotification findTimeTicketNotification,
															   @RequestParam String authToken)
			throws Exception {
		return managementService.findTimeTicketNotification(findTimeTicketNotification, authToken);
	}

	//Search IMattterTimeTIcket
	@ApiOperation(response = MatterTimeTicket.class, value = "Search MatterTimeTicket for Mobile") // label for swagger
	@PostMapping("/mattertimeticket/findMatterTimeTicketMobile")
	public ResponseEntity<?> findMatterTimeTicketMobile(@RequestBody SearchMatterTimeTicket searchMatterTimeTicket,
														@RequestParam String authToken) throws Exception {
		MatterTimeTicket[] matterTimeTickets = managementService.findMatterTimeTicketMobile(searchMatterTimeTicket, authToken);
		log.info("matterTimeTickets : " + matterTimeTickets);
		return new ResponseEntity<>(matterTimeTickets, HttpStatus.OK);
	}
}