package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.Dashboard;
import com.mnrclara.api.crm.model.dto.EMail;
import com.mnrclara.api.crm.model.dto.UserProfile;
import com.mnrclara.api.crm.model.inquiry.AddInquiry;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.SearchInquiry;
import com.mnrclara.api.crm.model.inquiry.UpdateInquiry;
import com.mnrclara.api.crm.model.notes.AddNotes;
import com.mnrclara.api.crm.model.notes.Notes;
import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.repository.InquiryRepository;
import com.mnrclara.api.crm.repository.specification.InquirySpecification;
import com.mnrclara.api.crm.util.CommonUtils;
import com.mnrclara.api.crm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InquiryService {
	
	private static final String NOTES = "NOTES";
	private static final String INQUIRY = "INQUIRY";

	@Autowired
	SetupService setupService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	PCIntakeFormService pcIntakeFormService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	InquiryRepository inquiryRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * getInquirys
	 * @return
	 */
	public List<Inquiry> getInquiries () {
		List<Inquiry> inquirys = inquiryRepository.findAll();
		inquirys = inquirys.stream().filter(a -> a.getDeletionIndicator() != null && a.getDeletionIndicator() != 1).collect(Collectors.toList());
		return inquirys;
	}
	
	/**
	 * getInquiry
	 * @param inquiryModuleCode
	 * @return
	 */
	public Inquiry getInquiry (String inquiryNumber) {
		Inquiry inquiry = inquiryRepository.findByInquiryNumber(inquiryNumber).orElse(null);
		if (inquiry != null && inquiry.getDeletionIndicator() != 1) {
			return inquiry;
		}
		return null; // Record got deleted
	}
	
	/**
	 * 
	 * @param classId
	 * @return
	 */
	public Long getInquiryCountByClassId (Long classId) {
		List<Inquiry> inquiryList = inquiryRepository.findByClassId(classId);
		
		if (classId == 3) { // return all records count
			return Long.valueOf(inquiryList.size());
		}
		Long count = inquiryList.stream().filter(i -> 
					i.getDeletionIndicator() == 0 && (i.getStatusId() == 3 || i.getStatusId() == 4)
					).count();
		return count;
	}
	
	/**
	 * getInquiryCount
	 * @param classId
	 * @return
	 */
	public Dashboard getInquiryCount (Long classId) {
		log.info("classId : " + classId);
		Dashboard dashboard = new Dashboard();
		if (classId == 1 || classId == 2) {
			List<Inquiry> inquiryList = inquiryRepository.findByClassId(classId);
			log.info("Filetered inquiryList : " + inquiryList);
			Long count = inquiryList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 
					&& (i.getStatusId() == 3 || i.getStatusId() == 4)
						).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(Long.valueOf(inquiryList.size()));
			return dashboard;
		}
		
		// return all records count
		if (classId == 3) { 
			List<Inquiry> inquiryList = inquiryRepository.findAll();
			Long totCount = inquiryList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			log.info("All inquiryList : " + inquiryList);
			Long count = inquiryList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 
					&& (i.getStatusId() == 3 || i.getStatusId() == 4)
						).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		}
		return dashboard;
	}
	
	/**
	 * Dashboard - Inquiry
	 * @param loginUserId
	 * @return
	 */
	public Dashboard getInquiryCount (String loginUserId) {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserId, authTokenForSetupService.getAccess_token());
		return getInquiryCount(userProfile.getClassId());
	}
	
	/**
	 * findInquiries
	 * @param searchInquiry
	 * @return
	 * @throws ParseException 
	 */
	public List<Inquiry> findInquiries(SearchInquiry searchInquiry) throws ParseException {
		// Handling multiple values based search
		
		if (searchInquiry.getInqStartDate() != null && searchInquiry.getInqEndDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchInquiry.getInqStartDate(), searchInquiry.getInqEndDate());
			searchInquiry.setInqStartDate(dates[0]);
			searchInquiry.setInqEndDate(dates[1]);
		}
		
		if (searchInquiry.getSAssignedOn() != null && searchInquiry.getEAssignedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchInquiry.getSAssignedOn(), searchInquiry.getEAssignedOn());
			searchInquiry.setSAssignedOn(dates[0]);
			searchInquiry.setEAssignedOn(dates[1]);
		}
		
		InquirySpecification spec = new InquirySpecification(searchInquiry);
		List<Inquiry> searchResults = inquiryRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}
	
	/**
	 * createInquiry
	 * @param newInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry createInquiry (AddInquiry newInquiry, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Inquiry dbInquiry = new Inquiry();
		String SMS_TEXT = "Thanks for inquiring. Our legal team will contact you soon. If you have any questions, "
				+ "please call our office at 281-493-5529. AUTOMATED TEXT: DO-NOT-REPLY";
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		/*
		 * INQ_NO - During Save, Pass CLASS_ID=03, NUM_RAN_CODE=01 in NUMBERRANGE table and 
		 * Fetch NUM_RAN_CURRENT values and add +1 and then insert into the INQUIRY table
		 */
		Long classID = 3L;
		Long NumberRangeCode = 1L;
		String newInquiryNumber = setupService.getNextNumberRange(classID, NumberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newInquiryNumber);
		dbInquiry.setInquiryNumber(newInquiryNumber);
		
		// INQ_DATE - Default current date. With Modifyable option
		Date inquiryDate = new Date();
		dbInquiry.setInquiryDate(inquiryDate);
		
		// INQ_MODE_ID
		dbInquiry.setInquiryModeId(newInquiry.getInquiryModeId());
		
		// FIRST_NM
		dbInquiry.setFirstName(newInquiry.getFirstName());
		
		// LAST_NM
		dbInquiry.setLastName(newInquiry.getLastName());
		
		// EMAIL_ID
		dbInquiry.setEmail(newInquiry.getEmail());
		
		// CONT_NO
		dbInquiry.setContactNumber(newInquiry.getContactNumber());
		
		/*
		 * During Save, pass the entered values and insert a record in NOTES table, 
		 * where TRANS_ID=01, TRANS_NO=INQ_NO, NOTE_TYP_ID=01, 
		 * NOTE_TEXT = entered values in this field and 
		 * NOTE_NO = Pass CLASS_ID=03, NUM_RAN_CODE=02 and fetch NUM_RAN_CURRENT values and add+1. 
		 * After successful insertion of record fetch NOTE_NO and insert in INQUIRY table in INQ_NOTE_NO field 
		 */
		// Insert Record in Notes Table Entry
		Long numberRangeCode = 2L;
		Long transactionId = 1L; 						// TRANS_ID=01
		Long noteTypeId = 1L;		
		Notes createdNotes = createNotes(newInquiryNumber, 
										transactionId,
										noteTypeId,
										classID,
										numberRangeCode,
										newInquiry.getReferenceField9(), // NOTE_TEXT
										loginUserID, 
										null,
										authTokenForSetupService.getAccess_token());
		
		// INQ_NOTE_NO
		if (createdNotes != null) {
			dbInquiry.setInquiryNotesNumber(createdNotes.getNotesNumber());
		}
		
		// STATUS_ID
		dbInquiry.setStatusId(1L);
		
		// TRANS_ID
		dbInquiry.setTransactionId(1L);
		
		// CLASS_ID
		dbInquiry.setClassId(classID);
		
		// LANG_ID
		String langID = setupService.getLanguageId(loginUserID, authTokenForSetupService.getAccess_token());
		dbInquiry.setLanguageId(langID);
		
		// CTD_BY
		dbInquiry.setCreatedBy(loginUserID);
				
		// UPD_BY
		dbInquiry.setUpdatedBy(loginUserID);
		
		// Ref_field_9
		dbInquiry.setReferenceField9(newInquiry.getReferenceField9());
		dbInquiry.setDeletionIndicator(0L);
		
		Inquiry createdInquiry = inquiryRepository.save(dbInquiry);
		if (createdInquiry != null) {
			// Send SMS
			// Get AuthToken for SetupService
			AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
			
			// Removing "-" from the phone number
			String sContactNumber = createdInquiry.getContactNumber();
			sContactNumber = sContactNumber.replaceAll("-", "");
			Long contactNumber = Long.valueOf(sContactNumber);
			Boolean isSuccess = false;
			try {
				isSuccess = commonService.sendSMS(contactNumber, SMS_TEXT, authTokenForCommonService.getAccess_token());
			} catch (Exception e) {
				isSuccess = false;
			}
			
			// Updating SMS Status in REF_FIELD_7
			UpdateInquiry updateInquiry = new UpdateInquiry();
			updateInquiry.setStatusId(1L);
			BeanUtils.copyProperties(createdInquiry, updateInquiry, CommonUtils.getNullPropertyNames(createdInquiry));
			if (isSuccess.booleanValue() == true) {
				updateInquiry.setReferenceField7("true");
				createdInquiry = updateInquiry(createdInquiry.getInquiryNumber(), loginUserID, updateInquiry);
			} else {
				updateInquiry.setReferenceField7("false");
				createdInquiry = updateInquiry(createdInquiry.getInquiryNumber(), loginUserID, updateInquiry);
			}
		}
		return createdInquiry;
	}
	
	/**
	 * 
	 * @param newInquiryNumber
	 * @param transactionId
	 * @param noteTypeId
	 * @param classID
	 * @param numberRangeCode
	 * @param noteText
	 * @param loginUserID
	 * @param websiteLang
	 * @param authToken
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notes createNotes (String newInquiryNumber, 
			Long transactionId,
			Long noteTypeId,
			Long classID,
			Long numberRangeCode,
			String noteText, String loginUserID, String websiteLang, String authToken) 
			throws IllegalAccessException, InvocationTargetException {
		
		// Insert new record in NoteTable
		AddNotes newNotes = new AddNotes();
		newNotes.setTransactionId(transactionId); 			// TRANS_ID=01
		newNotes.setTransactionNo(newInquiryNumber); 		// TRANS_NO=INQ_NO
		newNotes.setNoteTypeId(noteTypeId);					// NOTE_TYP_ID=01
		
		// NOTE_NO
		String newNotesNumber = setupService.getNextNumberRange(classID, numberRangeCode, authToken);
		log.info("nextVal from NotesNumber : " + newNotesNumber);
		newNotes.setNotesNumber(newNotesNumber);
		
		// CLASS_ID
		newNotes.setClassId(classID); // classID = 3L
		
		// NOTE_TEXT - ReferenceField for receiving NOTE_TEXT
		newNotes.setNotesDescription(noteText); 
		
		// LANG_ID
		if (loginUserID != null) {
			String langID = setupService.getLanguageId(loginUserID, authToken);
			newNotes.setLanguageId(langID);
		} else {
			// Setting Language for Website users
			newNotes.setLanguageId(websiteLang);
			loginUserID = "M&R_WP"; // since it is from Website
		}
		
		// Created By
		newNotes.setCreatedBy(loginUserID);
		
		// Created On
		newNotes.setCreatedOn(new Date());
		
		// Saving Notes Table
		Notes createdNotes = notesService.createNotes(newNotes, loginUserID);
		return createdNotes;
	}
	
	/**
	 * updateInquiry
	 * @param inquiryCode
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry updateInquiry (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry) 
			throws IllegalAccessException, InvocationTargetException {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Inquiry dbInquiry = getInquiry(inquiryNumber);
		log.info ("Retrieving DB Inquiry: " + dbInquiry);
		
		// INQ_MODE_ID
		if (updateInquiry.getInquiryModeId() != null && updateInquiry.getInquiryModeId() != dbInquiry.getInquiryModeId()) {
			log.info("Inserting Audit log for INQ_MODE_ID");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "INQ_MODE_ID", 
					String.valueOf(dbInquiry.getInquiryModeId()), 
					String.valueOf(updateInquiry.getInquiryModeId()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// FIRST_NM
		if (updateInquiry.getFirstName() != null && updateInquiry.getFirstName() != dbInquiry.getFirstName()) {
			log.info("Inserting Audit log for FIRST_NM");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L,INQUIRY, "FIRST_NM", 
					dbInquiry.getFirstName(), 
					updateInquiry.getFirstName(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// LAST_NM
		if (updateInquiry.getLastName() != null && updateInquiry.getLastName() != dbInquiry.getLastName()) {
			log.info("Inserting Audit log for LAST_NM");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L,INQUIRY, "LAST_NM", 
					dbInquiry.getLastName(), 
					updateInquiry.getLastName(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// EMAIL_ID
		if (updateInquiry.getEmail() != null && updateInquiry.getEmail() != dbInquiry.getEmail()) {
			log.info("Inserting Audit log for EMAIL_ID");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L,INQUIRY, "EMAIL_ID", 
					dbInquiry.getEmail(), 
					updateInquiry.getEmail(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// CONT_NO
		if (updateInquiry.getContactNumber() != null && updateInquiry.getContactNumber() != dbInquiry.getContactNumber()) {
			log.info("Inserting Audit log for CONT_NO");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "CONT_NO", 
					dbInquiry.getContactNumber(), 
					updateInquiry.getContactNumber(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// INQ_NOTE_NO
		if (updateInquiry.getReferenceField9() != null) { // If NoteText entered
			// Update Notes Table
			Notes dbNotes = notesService.getNotes(dbInquiry.getInquiryNotesNumber());
			notesService.updateNotes(dbInquiry.getInquiryNotesNumber(), updateInquiry.getReferenceField9(), loginUserID);
			log.info("Inserting Audit log for NOTE_TEXT");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, NOTES, "NOTE_TEXT", 
					dbNotes.getNotesDescription(),
					updateInquiry.getReferenceField9(), 
					authTokenForSetupService.getAccess_token());
		}
		
		BeanUtils.copyProperties(updateInquiry, dbInquiry, CommonUtils.getNullPropertyNames(updateInquiry));
		dbInquiry.setUpdatedBy(loginUserID);
		dbInquiry.setUpdatedOn(new Date());
		log.info ("After modified : " + dbInquiry);
		return inquiryRepository.save(dbInquiry);
	}

	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry assignInquiry (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry) 
			throws IllegalAccessException, InvocationTargetException {
		Long status = 3L; //STATUS_ID - Hard Coded Value "3"
		return saveInquiry(inquiryNumber, loginUserID, updateInquiry, status); 
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry updateAssignInquiry (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry) 
			throws IllegalAccessException, InvocationTargetException {
		Long status = 4L; //STATUS_ID - Hard Coded value "4"
		return saveInquiry(inquiryNumber, loginUserID, updateInquiry, status); 
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @param status
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry updateValiationStatus(String inquiryNumber, String loginUserID, 
			@Valid UpdateInquiry updateInquiry, Long status) 
					throws IllegalAccessException, InvocationTargetException {
		return saveInquiry(inquiryNumber, loginUserID, updateInquiry, status); 
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry updateInquiryIntake(String inquiryNumber, String loginUserID, @Valid UpdateInquiry updateInquiry) throws IllegalAccessException, InvocationTargetException {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Inquiry dbInquiry = getInquiry(inquiryNumber);
		log.info ("Retrieving DB Inquiry: " + dbInquiry);
		
		// IT_NOTE_NO
		// Insert Record in Notes Table Entry
		Long classID = dbInquiry.getClassId();
		Long numberRangeCode = 2L;
		Long transactionId = 2L; 	// TRANS_ID=02
		Long noteTypeId = 1L;		
		Notes createdNotes = createNotes(inquiryNumber, 
										transactionId,
										noteTypeId,
										3L, // Hardcoded as 3L for creating notes
										numberRangeCode,
										updateInquiry.getReferenceField8(), // NOTE_TEXT
										loginUserID, 
										null,
										authTokenForSetupService.getAccess_token());
		// IT_NOTE_NO
		if (createdNotes != null) {
			dbInquiry.setIntakeNotesNumber(createdNotes.getNotesNumber());
		}
		
		// IT_FORM_ID
		// Once the form is sent successfully, pass the entered value in INQUIRY table and update IT_FORMID
		
		BeanUtils.copyProperties(updateInquiry, dbInquiry, CommonUtils.getNullPropertyNames(updateInquiry));
		dbInquiry.setUpdatedBy(loginUserID);
		dbInquiry.setUpdatedOn(new Date());
		return inquiryRepository.save(dbInquiry);
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PCIntakeForm afterIntakeFormSent (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry) 
			throws IllegalAccessException, InvocationTargetException {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Inquiry dbInquiry = getInquiry(inquiryNumber);
		log.info ("Re trieving DB Inquiry: " + dbInquiry);
		
		// IT_NOTE_NO
		// Insert Record in Notes Table Entry
		Long classID = dbInquiry.getClassId();
		Long numberRangeCode = 2L;
		Long transactionId = 2L; 	// TRANS_ID=02
		Long noteTypeId = 1L;		
		Notes createdNotes = createNotes(inquiryNumber, 
										transactionId,
										noteTypeId,
										3L, //Hard coded as 3L
										numberRangeCode,
										updateInquiry.getReferenceField8(), // NOTE_TEXT
										loginUserID, 
										null,
										authTokenForSetupService.getAccess_token());
		// IT_NOTE_NO
		if (createdNotes != null) {
			dbInquiry.setIntakeNotesNumber(createdNotes.getNotesNumber());
		}
		
		BeanUtils.copyProperties(updateInquiry, dbInquiry, CommonUtils.getNullPropertyNames(updateInquiry));
		
//		Long status = 7L; //STATUS_ID - Hard Coded Value "7"
		dbInquiry.setReferenceField10("INTAKE FORM SENT");
		dbInquiry.setStatusId(updateInquiry.getStatusId());
		dbInquiry.setUpdatedBy(loginUserID);
		dbInquiry.setUpdatedOn(new Date());
		Inquiry inquiryUpdated = inquiryRepository.save(dbInquiry);
		log.info("Inquiry Status updated : " + inquiryUpdated);
		
		PCIntakeForm pcIntakeForm = pcIntakeFormService.createPCIntakeForm(inquiryUpdated, loginUserID);
		log.info("PCIntakeForm created: " + pcIntakeForm);
		return pcIntakeForm;
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @param status
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Inquiry saveInquiry (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry, Long status) 
			throws IllegalAccessException, InvocationTargetException {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Inquiry dbInquiry = getInquiry(inquiryNumber);
		log.info ("Retrieving DB Inquiry: " + dbInquiry);
		
		// INQ_MODE_ID
		if (updateInquiry.getInquiryModeId() != null && updateInquiry.getInquiryModeId() != dbInquiry.getInquiryModeId()) {
			log.info("Inserting Audit log for INQ_MODE_ID");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "INQ_MODE_ID", 
					String.valueOf(dbInquiry.getInquiryModeId()), 
					String.valueOf(updateInquiry.getInquiryModeId()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// FIRST_NM
		if (updateInquiry.getFirstName() != null && updateInquiry.getFirstName() != dbInquiry.getFirstName()) {
			log.info("Inserting Audit log for FIRST_NM");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "FIRST_NM", 
					dbInquiry.getFirstName(), 
					updateInquiry.getFirstName(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// LAST_NM
		if (updateInquiry.getLastName() != null && updateInquiry.getLastName() != dbInquiry.getLastName()) {
			log.info("Inserting Audit log for LAST_NM");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "LAST_NM", 
					dbInquiry.getLastName(), 
					updateInquiry.getLastName(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// EMAIL_ID
		if (updateInquiry.getEmail() != null && updateInquiry.getEmail() != dbInquiry.getEmail()) {
			log.info("Inserting Audit log for EMAIL_ID");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "EMAIL_ID", 
					dbInquiry.getEmail(), 
					updateInquiry.getEmail(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// CONT_NO
		if (updateInquiry.getContactNumber() != null && updateInquiry.getContactNumber() != dbInquiry.getContactNumber()) {
			log.info("Inserting Audit log for CONT_NO");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, INQUIRY, "CONT_NO", 
					dbInquiry.getContactNumber(), 
					updateInquiry.getContactNumber(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// INQ_NOTE_NO
		if (updateInquiry.getReferenceField9() != null) { // If NoteText entered
			// Update Notes Table
			Notes dbNotes = notesService.getNotes(dbInquiry.getInquiryNotesNumber());
			notesService.updateNotes(dbInquiry.getInquiryNotesNumber(), updateInquiry.getReferenceField9(), loginUserID);
			log.info("Inserting Audit log for NOTE_TEXT");
			setupService.createAuditLogRecord(loginUserID, 
					inquiryNumber, 3L, NOTES, "NOTE_TEXT", 
					dbNotes.getNotesDescription(),
					updateInquiry.getReferenceField9(), 
					authTokenForSetupService.getAccess_token());
		}
		
		BeanUtils.copyProperties(updateInquiry, dbInquiry, CommonUtils.getNullPropertyNames(updateInquiry));
		
		log.info ("Before modified : " + dbInquiry);
		
		dbInquiry.setStatusId(status); 
		dbInquiry.setUpdatedBy(loginUserID);
		dbInquiry.setUpdatedOn(new Date());
		dbInquiry.setAssignedBy(loginUserID);
		dbInquiry.setAssignedOn(new Date());
		
		log.info ("After modified : " + dbInquiry);
		return inquiryRepository.save(dbInquiry);
	}
	
	/**
	 * deleteInquiry
	 * @param inquiryCode
	 */
	public boolean deleteInquiry (String inquiryCode, String loginUserID) {
		Inquiry inquiry = getInquiry(inquiryCode);
		if ( inquiry != null) {
			/*
			 *  Validate STATUS_ID = 03,04,05,06 if yes then update ISDELETED value from 0 to 1 
			 *  for the selected INQ_NO and flag field "REF_FIELD_10"
			 */
			if (inquiry.getStatusId() == 3 || inquiry.getStatusId() == 4 || inquiry.getStatusId() == 5 || inquiry.getStatusId() == 6) {
				inquiry.setDeletionIndicator(1L);
				inquiry.setReferenceField10("INQUIRY DELETED");
				inquiry.setUpdatedBy(loginUserID);
				inquiry.setUpdatedOn(new Date());
				inquiryRepository.save(inquiry);
				return true;
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + inquiryCode);
		}
		return false;
	}
	
	/**
	 * 
	 * @param email
	 */
	public void sendFormThroEmail(EMail email) {
		// Send Email
		// Get AuthToken for SetupService
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		commonService.sendEmail(email, authTokenForCommonService.getAccess_token());
	}
}
