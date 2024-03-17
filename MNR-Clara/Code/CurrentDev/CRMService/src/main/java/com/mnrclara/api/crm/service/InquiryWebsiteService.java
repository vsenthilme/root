package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.UpdateInquiry;
import com.mnrclara.api.crm.model.notes.Notes;
import com.mnrclara.api.crm.model.wordpress.AddInquiryWebsite;
import com.mnrclara.api.crm.repository.InquiryRepository;
import com.mnrclara.api.crm.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InquiryWebsiteService {
	
	@Autowired
	private InquiryService inquiryService;
	
	@Autowired
	SetupService setupSerice;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	InquiryRepository inquiryRepository;
	
	/**
	 * 
	 * @param inquiryNumber
	 * @return
	 */
	public Inquiry getInquiry (String inquiryNumber) {
		Inquiry inquiry = inquiryRepository.findByInquiryNumber(inquiryNumber).orElse(null);
		if (inquiry != null && inquiry.getDeletionIndicator() != 1) {
			return inquiry;
		}
		return null; // Record got deleted
	}
	
	/*
	 * Website (Wordpress) JSON Structure
	 * {
		  "language": "",
		  "email": "",
		  "phone": "",
		  "name"": "",
		  "legalIssueDescription": "",
		  "zipCode": "",
		  "preferableCommunicationMedium": "",
		  "department": "",
		  "message": ""
		}
	 */
	/**
	 * 
	 * @param newInquiryWebsite
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry createInquiryWebsite(AddInquiryWebsite newInquiryWebsite) 
			throws IllegalAccessException, InvocationTargetException {
		Long classID = 3L;
		String CREATED_BY = "M&R_WP";
//		String SMS_TEXT = "Thank you for inquiring. Our legal team will contact you shortly. \r\n "
//				+ "If you have any questions, please call our office at (281) 493-5529 AUTOMATED RESPONSE: DO-NOT-REPLY";
		
		String SMS_TEXT = "Thanks for inquiring. Our legal team will contact you soon. If you have any questions, "
				+ "please call our office at 281-493-5529. AUTOMATED TEXT: DO-NOT-REPLY";
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Inquiry dbInquiry = new Inquiry();
		
		// INQ_MODE_ID
		dbInquiry.setInquiryModeId(1L); // Default value '1
		
		// LANG_ID
		dbInquiry.setLanguageId(newInquiryWebsite.getLanguage());
		
		// First Name
		dbInquiry.setFirstName(newInquiryWebsite.getFirstName());
			
		// Last Name
		dbInquiry.setLastName(newInquiryWebsite.getLastName());
		
		// Email
		dbInquiry.setEmail(newInquiryWebsite.getEmail());
		
		// Phone
		dbInquiry.setContactNumber(newInquiryWebsite.getPhone());
		
		// ZipCode
		dbInquiry.setReferenceField1(newInquiryWebsite.getZipCode());
		
		// Communication Medium Phone/Email
		dbInquiry.setReferenceField2(newInquiryWebsite.getPreferabalCommunicationMedium());
		
		//  Service Type
		dbInquiry.setReferenceField3(newInquiryWebsite.getDepartment());
		
		// Message
		dbInquiry.setReferenceField4(newInquiryWebsite.getMessage());
		
		// Form ID
		dbInquiry.setReferenceField11(newInquiryWebsite.getFormId());
		
		// INQ_DATE - Default current date. With Modifyable option
		Date inquiryDate = new Date();
		dbInquiry.setInquiryDate(inquiryDate);
		
		// STATUS_ID
		dbInquiry.setStatusId(1L);
		
		// CLASS_ID
		dbInquiry.setClassId(classID);
		
		// TRANS_ID
		dbInquiry.setTransactionId(1L);
		
		// CREATED_ON
		dbInquiry.setCreatedOn(inquiryDate);
		dbInquiry.setUpdatedOn(inquiryDate);
		
		// CREATED_BY
		dbInquiry.setCreatedBy(CREATED_BY);
		dbInquiry.setUpdatedBy(CREATED_BY);
		dbInquiry.setDeletionIndicator(0L);
		
		log.info("dbInquiry : " + dbInquiry);
		
		/*
		 * Prod Fix: Avoiding creating double Inquiry creation
		 */
		LocalDate sLocalDate =  LocalDate.ofInstant(inquiryDate.toInstant(), ZoneId.systemDefault());
		String inqDate = sLocalDate.format(DateTimeFormatter. ofPattern("yyyy-MM-dd"));
		Inquiry existingInquiry = inquiryRepository.findDuplicateEntry(inqDate, dbInquiry.getFirstName(), dbInquiry.getLastName(), 
				dbInquiry.getEmail(), dbInquiry.getContactNumber());
		log.info("existingInquiry-------> : " + existingInquiry);
		
		boolean allowToCreate = true;
		if (existingInquiry != null) {
			Date existingInquiryDate = existingInquiry.getInquiryDate();
			long seconds = (inquiryDate.getTime() - existingInquiryDate.getTime())/1000;
			log.info("seconds-------> : " + seconds);
			if (seconds < 5) {
				allowToCreate = false;
			}
		}
		
		if (allowToCreate) {
			/*
			 * INQ_NO - During Save, Pass CLASS_ID=03, NUM_RAN_CODE=01 in NUMBERRANGE table and 
			 * Fetch NUM_RAN_CURRENT values and add +1 and then insert into the INQUIRY table
			 */
			Long NumberRangeCode = 1L;
			String newInquiryNumber = setupSerice.getNextNumberRange(classID, NumberRangeCode, authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange : " + newInquiryNumber);
			dbInquiry.setInquiryNumber(newInquiryNumber);
			
			// Insert Record in Notes Table Entry
			/*
			 * During Save, pass the entered values and insert a record in NOTES table, 
			 * where TRANS_ID=01, CLASS_ID=03, TRANS_NO=INQ_NO, NOTE_TYP_ID=01, NOTE_TEXT = entered 
			 * values in UI stored in REF_FIELD_9 and NOTE_NO= Pass CLASS_ID=03,NUM_RAN_CODE=02 and 
			 * fetch NUM_RAN_CURRENT values and add+1 . After successful insertion of record fetch NOTE_NO 
			 * and insert in INQUIRY table in INQ_NOTE_NO field 
			 */
			/*
			 * String newInquiryNumber, 
				Long transactionId,
				Long noteTypeId,
				Long classID,
				Long numberRangeCode,
				String noteText, String loginUserID, String websiteLang, String authToken
			 */
			Long transactionId = 1L;
			Long noteTypeId = 1L;
			Long noteClassID = 3L;
			Long noteNumberRangeCode = 2L;
			Notes createdNotes = inquiryService.createNotes(newInquiryNumber, 
															transactionId, // transactionId
															noteTypeId, // noteTypeId
															noteClassID, // classID
															noteNumberRangeCode, // numberRangeCode
															newInquiryWebsite.getLegelIssueDescription(), // noteText
															null, // loginUserID
															newInquiryWebsite.getLanguage(), 
															authTokenForSetupService.getAccess_token());
			// INQ_NOTE_NO
			if (createdNotes != null) {
				dbInquiry.setInquiryNotesNumber(createdNotes.getNotesNumber());
			}
			
			// Save Inquiry
			Inquiry createdInquiry = inquiryRepository.save(dbInquiry);
			log.info("createdInquiry : " + createdInquiry.getInquiryNumber());
			if (createdInquiry != null) {
				// Send SMS
				// Get AuthToken for SetupService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				
				// Removing "-" from the phone number
				String sContactNumber = createdInquiry.getContactNumber();
				sContactNumber = sContactNumber.replaceAll("-", "");
				Long contactNumber = Long.valueOf(sContactNumber);
				Boolean isSuccess = commonService.sendSMS(contactNumber, SMS_TEXT, authTokenForCommonService.getAccess_token());
				
				// Updating SMS Status in REF_FIELD_7
				UpdateInquiry updateInquiry = new UpdateInquiry();
				updateInquiry.setStatusId(1L);
				BeanUtils.copyProperties(createdInquiry, updateInquiry, CommonUtils.getNullPropertyNames(createdInquiry));
				if (isSuccess.booleanValue() == true) {
					updateInquiry.setReferenceField7("TRUE");
					createdInquiry = updateInquiry(createdInquiry.getInquiryNumber(), CREATED_BY, updateInquiry);
				} else {
					updateInquiry.setReferenceField7("FALSE");
					createdInquiry = updateInquiry(createdInquiry.getInquiryNumber(), CREATED_BY, updateInquiry);
				}
				
				return createdInquiry;
			}
		}
		return null;
	}
	
	/**
	 * updateInquiry
	 * @param inquiryNumber
	 * @param loginUserID
	 * @param updateInquiry
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Inquiry updateInquiry (String inquiryNumber, String loginUserID, UpdateInquiry updateInquiry) 
			throws IllegalAccessException, InvocationTargetException {
		Inquiry dbInquiry = getInquiry(inquiryNumber);
		BeanUtils.copyProperties(updateInquiry, dbInquiry, CommonUtils.getNullPropertyNames(updateInquiry));
		dbInquiry.setUpdatedBy(loginUserID);
		dbInquiry.setUpdatedOn(new Date());
		log.info ("After modified : " + dbInquiry);
		return inquiryRepository.save(dbInquiry);
	}
}
