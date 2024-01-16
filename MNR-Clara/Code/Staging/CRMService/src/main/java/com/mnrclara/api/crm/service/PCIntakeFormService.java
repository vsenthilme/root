package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.controller.exception.BadRequestException;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.Dashboard;
import com.mnrclara.api.crm.model.dto.InquiryMode;
import com.mnrclara.api.crm.model.dto.UserProfile;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.UpdateInquiry;
import com.mnrclara.api.crm.model.itform.ITForm000;
import com.mnrclara.api.crm.model.notes.Notes;
import com.mnrclara.api.crm.model.pcitform.DuplicateQueryParams;
import com.mnrclara.api.crm.model.pcitform.Feedback;
import com.mnrclara.api.crm.model.pcitform.LeadConversionReport;
import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeForm;
import com.mnrclara.api.crm.model.pcitform.SearchPCIntakeFormReport;
import com.mnrclara.api.crm.model.pcitform.UpdatePCIntakeForm;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.repository.PCIntakeFormRepository;
import com.mnrclara.api.crm.repository.specification.PCIntakeFormReportSpecification;
import com.mnrclara.api.crm.repository.specification.PCIntakeFormSpecification;
import com.mnrclara.api.crm.util.CommonUtils;
import com.mnrclara.api.crm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PCIntakeFormService {
	
	@Autowired
	InquiryService inquiryService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	MongoService mongoService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	PCIntakeFormRepository pcIntakeFormRepository;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * getPCIntakeForms
	 * @return
	 */
	public List<PCIntakeForm> getPCIntakeForms () {
		List<PCIntakeForm> pcIntakeForms = pcIntakeFormRepository.findAll();
		pcIntakeForms = pcIntakeForms.stream().filter(a -> a.getDeletionIndicator() != 1).collect(Collectors.toList());
		return pcIntakeForms;
	}
	
	/**
	 * getPCIntakeForm
	 * @param pcIntakeFormCode
	 * @return
	 */
	public PCIntakeForm getPCIntakeForm (String pcIntakeFormCode) {
		PCIntakeForm pcIntakeForm = pcIntakeFormRepository.findByIntakeFormNumber(pcIntakeFormCode).orElse(null);
		if (pcIntakeForm.getDeletionIndicator() != 1) {
			return pcIntakeForm;
		}
		return null; // Record got deleted
	}
	
	/**
	 * getPCIntakeForm
	 * @param intakeFormNumber
	 * @param inquiryNumber
	 * @return
	 */
	public PCIntakeForm getPCIntakeForm (String intakeFormNumber, String inquiryNumber) {
		Optional<PCIntakeForm> dbPCIntakeFormOpt = pcIntakeFormRepository.findByIntakeFormNumberAndInquiryNumber(intakeFormNumber, inquiryNumber);
		if (dbPCIntakeFormOpt.isEmpty()) {
			throw new BadRequestException("Record not found for the given Inouts: ");
		}
		PCIntakeForm dbPCIntakeForm = dbPCIntakeFormOpt.get();
		return dbPCIntakeForm;
	}
	
	/**
	 * findPCIntakeFormDuplicate
	 * @param queryParams
	 * @return
	 */
	public boolean findPCIntakeFormDuplicate (DuplicateQueryParams queryParams) {
		List<PCIntakeForm> pcIntakeFormList = pcIntakeFormRepository.findDuplicatePCIntakeForm(
									queryParams.getLanguageId(), 
									queryParams.getInquiryNumber(), 
									queryParams.getClassId(), 
									queryParams.getIntakeFormId(), 
									queryParams.getIntakeFormNumber());
		if (pcIntakeFormList.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Filters : ISDELETED value = 0 & STATUS_ID=07,08,09
	 * @param classId
	 * @return
	 */
	public Dashboard getPCIntakeFormCount (Long classId) {
		log.info("classId : " + classId);
		Dashboard dashboard = new Dashboard();
		if (classId == 1 || classId == 2) {
			List<PCIntakeForm> pcIntakeFormList = pcIntakeFormRepository.findByClassId(classId);
			Long count = pcIntakeFormList.stream()
					.filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 && 
						(i.getStatusId() == 7 || i.getStatusId() == 8 || i.getStatusId() == 9)
					).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(Long.valueOf(pcIntakeFormList.size()));
			log.info("Filtered list : " + pcIntakeFormList);
			return dashboard;
		}
		
		if (classId == 3) { // return all records count
			List<PCIntakeForm> pcIntakeFormList = pcIntakeFormRepository.findAll();
			Long totCount = pcIntakeFormList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			Long count = pcIntakeFormList.stream()
					.filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 && 
						(i.getStatusId() == 7 || i.getStatusId() == 8 || i.getStatusId() == 9)
					).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		}
		return dashboard;
	}
	
	/**
	 * Dashboard - PCINTAKEFORM
	 * 
	 * @param loginUserId
	 * @return
	 */
	public Dashboard getPCIntakeFormCount (String loginUserId) {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserId, authTokenForSetupService.getAccess_token());
		return getPCIntakeFormCount(userProfile.getClassId());
	}
	
	/**
	 * createPCIntakeForm
	 * @param inquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PCIntakeForm createPCIntakeForm (Inquiry inquiry, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException {
		// Check for Duplicates
		DuplicateQueryParams dup = new DuplicateQueryParams();
		dup.setLanguageId(inquiry.getLanguageId());
		dup.setInquiryNumber(inquiry.getInquiryNumber());
		dup.setClassId(inquiry.getClassId());
		dup.setIntakeFormId(inquiry.getIntakeFormId());
		dup.setIntakeFormNumber(inquiry.getIntakeNotesNumber());
		boolean doesRecordExists = findPCIntakeFormDuplicate(dup);
		
		if (doesRecordExists) {
			throw new BadRequestException("Already record exists with the same Inquiry Number. Quiting.");
		}
		
		PCIntakeForm dbPCIntakeForm = new PCIntakeForm();
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// INQ_NO
		dbPCIntakeForm.setInquiryNumber(inquiry.getInquiryNumber());
		
		// LANG_ID
		// Pass INQ_NO in INQUIRY table and fetch LANG_ID and insert into PCINTAKEFORM table
		dbPCIntakeForm.setLanguageId(inquiry.getLanguageId());
		
		// CLASS_ID
		dbPCIntakeForm.setClassId(inquiry.getClassId());
		
		// IT_FORM_NO
		Long classID = 3L;
		Long numberRangeCode = 7L;
		String newIntakeNumber = setupService.getNextNumberRange(classID, numberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newIntakeNumber);
		dbPCIntakeForm.setIntakeFormNumber(newIntakeNumber);
		
		// IT_FORM_ID
		// Pass INQ_NO in INQUIRY table and fetch IT_FORM_ID and insert into PCINTAKEFORM table
		dbPCIntakeForm.setIntakeFormId(inquiry.getIntakeFormId());
		
		// STATUS_ID
		dbPCIntakeForm.setStatusId(7L); // Hard Coded Value"7"
		
		// TRANS_ID
		dbPCIntakeForm.setTransactionId(2L); // Hard Coded value "02"
		
		// EMail_ID
		// "TO" Email ID from Inquiry table
		dbPCIntakeForm.setEmail(inquiry.getEmail());
		
		// ALT_EMAIL_ID
		// "TO" Email ID
		dbPCIntakeForm.setAlternateEmailId(inquiry.getEmail());
		
		// IT_NOTE_NO
		// Pass INQ_NO in INQUIRY table and fetch IT_NOTE_NO and insert in PCINTAKEFORM table
		dbPCIntakeForm.setIntakeNotesNumber(inquiry.getIntakeNotesNumber());
		
		// SENT_ON
		dbPCIntakeForm.setSentOn(new Date());
		
		// CTD_BY
		dbPCIntakeForm.setCreatedBy(loginUserId);
		
		// CTD_ON
		dbPCIntakeForm.setCreatedOn(new Date());
		dbPCIntakeForm.setDeletionIndicator(0L);
		
		log.info("dbPCIntakeForm : " + dbPCIntakeForm); 
		PCIntakeForm createdPCIntakeForm =  pcIntakeFormRepository.save(dbPCIntakeForm);
		
		//---------------------------------------------------------------------------------------
		// Insert a record in corresponding Mongo DB as below
		ITForm000 itForm000 = new ITForm000();
		
		// INQ_NO
		itForm000.setInquiryNo(inquiry.getInquiryNumber());
		
		// LANG_ID
		// Pass INQ_NO in INQUIRY table and fetch LANG_ID and insert into PCINTAKEFORM table
		itForm000.setLanguage(inquiry.getLanguageId());
		
		// CLASS_ID
		itForm000.setClassID(inquiry.getClassId());
		
		// IT_FORM_NO
		itForm000.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm000.setItFormID(inquiry.getIntakeFormId());
		
		// STATUS_ID
		itForm000.setStatusId(7L); // Hard Coded Value"7"
				
		// SENT_ON
		itForm000.setSentOn(new Date());
		
		// CTD_BY
		itForm000.setCreatedBy(loginUserId);
		
		// CTD_ON
		itForm000.setCreatedOn(new Date());
		
		// ID
		itForm000.setId(itForm000.getId());
		mongoService.addITForm000(itForm000);
		return createdPCIntakeForm;
	}
	
	/**
	 * updatePCIntakeForm
	 * @param pcIntakeFormCode
	 * @param updatePCIntakeForm
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public PCIntakeForm updatePCIntakeForm (String pcIntakeFormCode, UpdatePCIntakeForm updatePCIntakeForm, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		PCIntakeForm dbPCIntakeForm = getPCIntakeForm(pcIntakeFormCode);
		BeanUtils.copyProperties(updatePCIntakeForm, dbPCIntakeForm, CommonUtils.getNullPropertyNames(updatePCIntakeForm));
		
		log.info("dbPCIntakeForm : " + dbPCIntakeForm);
		if (updatePCIntakeForm.getStatusId() != null) {
			dbPCIntakeForm.setStatusId(updatePCIntakeForm.getStatusId());
		}
		
		//If selected STATUS_ID = 10, then update this field value with SERVER time during save
		if (updatePCIntakeForm.getStatusId() == 10L) { 
			dbPCIntakeForm.setReferenceField10("APPROVED");
			dbPCIntakeForm.setApprovedOn(new Date());
			dbPCIntakeForm.setApprovedBy(loginUserID);
			
			log.info("dbPCIntakeForm.getIntakeNotesNumber() : " + dbPCIntakeForm.getIntakeNotesNumber());
			
			// Creating PotentialClient
			Date ref_field_1 = DateUtils.convertStringToDate(dbPCIntakeForm.getReferenceField1());
			log.info("Converted Date : " + ref_field_1);
			PotentialClient potentialClient = potentialClientService.createPotentialClient(loginUserID, 
					dbPCIntakeForm.getIntakeFormNumber(), 
					dbPCIntakeForm.getInquiryNumber(),
					dbPCIntakeForm.getIntakeNotesNumber(),
					ref_field_1,
					dbPCIntakeForm.getReferenceField2());
			log.info("Potential Client got created.");
			
			// Setting up potentialClient ID in Ref. Field 7
			dbPCIntakeForm.setReferenceField7(potentialClient.getPotentialClientId());
		} else {
			dbPCIntakeForm.setReceivedOn(new Date());
		}
		
		dbPCIntakeForm.setUpdatedBy(loginUserID);
		dbPCIntakeForm.setUpdatedOn(new Date());
		PCIntakeForm updatedPCIntakeForm = pcIntakeFormRepository.save(dbPCIntakeForm);
		log.info("PCIntakeForm got updated with Status Approved.");
		
		return updatedPCIntakeForm;
	}
	
	/**
	 * deletePCIntakeForm
	 * @param intakeFormNumber
	 * @param inquiryNumber 
	 */
	public void deletePCIntakeForm (String intakeFormNumber, String inquiryNumber, String loginUserId) {
		PCIntakeForm pcIntakeForm = getPCIntakeForm(intakeFormNumber, inquiryNumber);
		if ( pcIntakeForm == null ) {
			throw new EntityNotFoundException("Error in deleting ITForm No: " + intakeFormNumber);
		}
		Long statusId = pcIntakeForm.getStatusId();
		if (statusId == 5L || statusId == 6L || statusId == 7L || statusId == 8L || statusId == 9L) {
			pcIntakeForm.setDeletionIndicator(1L);
			pcIntakeForm.setReferenceField10("DELETED");
			pcIntakeForm.setUpdatedBy(loginUserId);
			pcIntakeForm.setUpdatedOn(new Date());
			pcIntakeFormRepository.save(pcIntakeForm);
		}
	}

	/**
	 * 
	 * @param
	 * @param searchPCIntakeForm
	 * @return
	 * @throws ParseException 
	 */
	public List<PCIntakeForm> findPCIntakeForms (SearchPCIntakeForm searchPCIntakeForm) throws ParseException {
		if (searchPCIntakeForm.getSSentOn() != null && searchPCIntakeForm.getESentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPCIntakeForm.getSSentOn(), searchPCIntakeForm.getESentOn());
			searchPCIntakeForm.setSSentOn(dates[0]);
			searchPCIntakeForm.setESentOn(dates[1]);
		}
		
		if (searchPCIntakeForm.getSReceivedOn() != null && searchPCIntakeForm.getEReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPCIntakeForm.getSReceivedOn(), searchPCIntakeForm.getEReceivedOn());
			searchPCIntakeForm.setSReceivedOn(dates[0]);
			searchPCIntakeForm.setEReceivedOn(dates[1]);
		}
		
		if (searchPCIntakeForm.getSApprovedOn() != null && searchPCIntakeForm.getEApprovedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPCIntakeForm.getSApprovedOn(), searchPCIntakeForm.getEApprovedOn());
			searchPCIntakeForm.setSApprovedOn(dates[0]);
			searchPCIntakeForm.setEApprovedOn(dates[1]);
		}
		
		PCIntakeFormSpecification spec = new PCIntakeFormSpecification(searchPCIntakeForm);
		List<PCIntakeForm> searchResults = pcIntakeFormRepository.findAll(spec);
		searchResults = searchResults.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		log.info("results: " + searchResults);
		return searchResults;
	}

	/**
	 * 
	 * @param searchPCIntakeFormReport
	 * @return
	 * @throws ParseException
	 */
	public List<LeadConversionReport> getPCIntakeFormReport(SearchPCIntakeFormReport searchPCIntakeFormReport) throws Exception {
		List<LeadConversionReport> leadConversionReportList = null;
		try {
			if (searchPCIntakeFormReport.getClassId() == null) {
				throw new BadRequestException("Class ID can't be blank.");
			}
			
			if (searchPCIntakeFormReport.getStatusId() == null && searchPCIntakeFormReport.getStatusId().isEmpty()) {
				throw new BadRequestException("StatusId can't be blank.");
			}

			if (searchPCIntakeFormReport.getFromCreatedOn() == null && 
					searchPCIntakeFormReport.getToCreatedOn() == null) {
				throw new BadRequestException("CreatedOn can't be blank.");
			}
			
			if (searchPCIntakeFormReport.getFromCreatedOn() != null && searchPCIntakeFormReport.getToCreatedOn() != null) {
				Date[] dates = DateUtils.addTimeToDatesForSearch(searchPCIntakeFormReport.getFromCreatedOn(), 
						searchPCIntakeFormReport.getToCreatedOn());
				searchPCIntakeFormReport.setFromCreatedOn(dates[0]);
				searchPCIntakeFormReport.setToCreatedOn(dates[1]);
			}
			
			PCIntakeFormReportSpecification spec = new PCIntakeFormReportSpecification (searchPCIntakeFormReport);
			List<PCIntakeForm> pcIntakeFormSearchResults = pcIntakeFormRepository.findAll(spec);
			log.info("PCIntakeFormSearchResults: " + pcIntakeFormSearchResults);
			
			AuthToken authToken = authTokenService.getSetupServiceAuthToken();
			leadConversionReportList = new ArrayList<>();
			for (PCIntakeForm pcIntakeForm : pcIntakeFormSearchResults) {
				//---------Inquiry Fields----------------------------------------------
				Inquiry inquiry = inquiryService.getInquiry(pcIntakeForm.getInquiryNumber());
				log.error("inquiry : " + inquiry);
				
				if (inquiry != null) {
					LeadConversionReport leadConversionReport = new LeadConversionReport();
					
					//------------------PCIntakeForm-------------------------------------------------------
					/*
					 * CLASS_ID
					 * -----------------
					 * Fetch from POTENTIALCLIENT table.Pass CLASS_ID and fetch CLASS values from CLASSID table and display both the values
					 */
					try {
						com.mnrclara.api.crm.model.dto.Class classId =
								setupService.getClassId(pcIntakeForm.getClassId(), authToken.getAccess_token());
						if(classId != null){
							leadConversionReport.setClassId(classId.getClassDescription());
						}
					} catch(Exception e){
						log.error("Error in getting classId details from setup service: " + pcIntakeForm.getClassId());
					}
	
					leadConversionReport.setInquiryNumber(pcIntakeForm.getInquiryNumber());
					leadConversionReport.setInquiryAssignedToRefField4(pcIntakeForm.getReferenceField2());
					leadConversionReport.setIntakeFormReceived(pcIntakeForm.getReceivedOn());
					leadConversionReport.setConsultationDate(pcIntakeForm.getReferenceField1());
					leadConversionReport.setConsultingAttorney(pcIntakeForm.getReferenceField2());
					leadConversionReport.setStatusId(pcIntakeForm.getStatusId());
					
					// Get InquiryMode
					try {
						InquiryMode inquiryMode =
								setupService.getInquiryMode(String.valueOf(inquiry.getInquiryModeId()), authToken.getAccess_token());
						log.info("inquiryMode: " + inquiryMode);
						if(inquiryMode != null){
							leadConversionReport.setModeOfInquiry(inquiryMode.getInquiryModeDescription());
						}
					} catch(Exception e){
						log.error("Error in getting inquiryMode details from setup service: " + inquiry.getInquiryModeId());
					}
					
					leadConversionReport.setInquiyDate(inquiry.getInquiryDate());
					leadConversionReport.setFirstNameLastName(inquiry.getFirstName() + " " + inquiry.getLastName());
					leadConversionReport.setContactNumber(inquiry.getContactNumber());
					leadConversionReport.setEmail(inquiry.getEmail());
					
					// Pass INQ_NO in INQUIRY table and fetch INQ_NOTE_NO. Pass INQ_NOTE_NO as NOTE_NO in NOTES table and 
					// fetch NOTES_TEXT
					if (inquiry.getInquiryNotesNumber() != null) {
						Notes notes = notesService.getNotes(inquiry.getInquiryNotesNumber());
						log.info("notes: " + notes);
						if (notes != null) {
							leadConversionReport.setOrginalInquiryObjective(notes.getNotesDescription()); // INQ_NOTE_NO
						}
					}
					
					//---------POTENTIALCLIENT---------------------------------------------
					List<PotentialClient> potentialClient = potentialClientService.getPotentialClientByInquiryNumber(inquiry.getInquiryNumber());
					log.info("potentialClient: " + potentialClient);
					if (potentialClient != null && !potentialClient.isEmpty()) {
						leadConversionReport.setCreatedOn(potentialClient.get(0).getCreatedOn());
					}
					
					leadConversionReportList.add(leadConversionReport);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leadConversionReportList;
	}
}
