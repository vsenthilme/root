package com.mnrclara.api.management.service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.expirationdate.*;
import com.mnrclara.api.management.repository.ExpirationDateRepository;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExpirationDateService {
	
	@Autowired
	private ExpirationDateRepository expirationdateRepository;
	
	@Autowired
	private ClientGeneralService clientGeneralService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	/**
	 * getExpirationDates
	 * @return
	 */
	public List<ExpirationDate> getExpirationDates () {
		List<ExpirationDate> expirationdateList = expirationdateRepository.findAll();
		
		if (expirationdateList != null) {
			log.info("expirationdateList : " + expirationdateList.get(0).getExpirationDate());
		}
		
		expirationdateList = expirationdateList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return expirationdateList;
	}
	
	/**
	 * getExpirationDate
	 * @param matterNo
	 * @param languageId
	 * @param classId
	 * @param clientId
	 * @param documentType
	 * @return
	 */
	public ExpirationDate getExpirationDate (String matterNo, String languageId, Long classId, String clientId, String documentType) {
		log.info("matterNo, languageId, classId, clientId, documentType : " + 
				matterNo + "," + languageId + "," + classId + "," + clientId + "," +documentType);
		Optional<ExpirationDate> optExpirationDate = 
				expirationdateRepository.findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentTypeAndDeletionIndicator(
						languageId, classId, matterNo, clientId, documentType, 0L);
		if (optExpirationDate.isEmpty()) {
			throw new BadRequestException("Record doesn't exist for the given values.");
		}
		
		return optExpirationDate.get();
	}
	
	/**
	 * getExpirationDate
	 * @param matterNo
	 * @param languageId
	 * @param classId
	 * @param clientId
	 * @return
	 */
	public ExpirationDate getExpirationDate (String matterNo, String languageId, Long classId, String clientId) {
		Optional<ExpirationDate> optExpirationDate = 
				expirationdateRepository.findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDeletionIndicator(
						languageId, classId, matterNo, clientId, 0L);
		if (optExpirationDate.isEmpty()) {
			throw new BadRequestException("Record doesn't exist for the given values.");
		}
		
		return optExpirationDate.get();
	}
	
	/**
	 * createExpirationDate
	 * @param newExpirationDate
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpirationDate createExpirationDate (AddExpirationDate newExpirationDate, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("createExpirationDate : " + newExpirationDate);
		Optional<ExpirationDate> optExpirationDate = 
				expirationdateRepository.findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentTypeAndDeletionIndicator(
						newExpirationDate.getLanguageId(), 
						newExpirationDate.getClassId(),
						newExpirationDate.getMatterNumber(),
						newExpirationDate.getClientId(),
						newExpirationDate.getDocumentType(),
						0L);
		if (!optExpirationDate.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated for the given values.");
		}
		ExpirationDate dbExpirationDate = new ExpirationDate();
		BeanUtils.copyProperties(newExpirationDate, dbExpirationDate, CommonUtils.getNullPropertyNames(newExpirationDate));
		
		log.info("dbExpirationDate--getExpirationDate---> : " + dbExpirationDate.getExpirationDate());
		log.info("dbExpirationDate--getReminderDate---> : " + dbExpirationDate.getReminderDate());
		
		dbExpirationDate.setStatusId(18L);
		dbExpirationDate.setCreatedBy(loginUserID);
		dbExpirationDate.setUpdatedBy(loginUserID);
		dbExpirationDate.setCreatedOn(new Date());
		dbExpirationDate.setUpdatedOn(new Date());
		dbExpirationDate = expirationdateRepository.save(dbExpirationDate);
		log.info("Saved dbExpirationDate : " + dbExpirationDate);
		return dbExpirationDate;
	}
	
	/**
	 * 
	 * @param matterNo
	 * @param documentType
	 * @return
	 */
	public Boolean sendReminderSMS () {
		log.info("sendReminderSMS called.....");
		
		Date CURRENT_DATE = new Date();
		List<IExpirationDate> expirationDates = expirationdateRepository.findByReminderDateForReminderSMS ();
		if (expirationDates.isEmpty()) {
			throw new BadRequestException("Record doesn't exist for the current Date in ExpirationDates.");
		}
		
		log.info("ExpirationDate========> : " + expirationDates);
		
		// Once the record is inserted successfully in EXPIRATIONDATE table, fetch the REMINDER_DATE,CLIENT_ID, REMINDER_TEXT values 
		String CLIENT_ID = "";
		String REMINDER_TEXT = "";
		String CONTACT_NO = "";
		Date REMINDER_DATE = null;
		for (IExpirationDate expirationDateRecord : expirationDates) {
			/*
			 * If the ToggleNotification is off (0) then Notification should not send
			 */
			log.info("ExpirationDateRecord---ToggleNotification---> : " + expirationDateRecord.getToggleNotification());
			if (expirationDateRecord.getToggleNotification() != null && expirationDateRecord.getToggleNotification().booleanValue() == true) {
				CLIENT_ID = expirationDateRecord.getClientId();
				REMINDER_DATE = expirationDateRecord.getReminderDate();
				REMINDER_TEXT = expirationDateRecord.getReminderText();
				log.info("ExpirationDate : " + CLIENT_ID + ":" + REMINDER_DATE + ":" + REMINDER_TEXT);
				
				LocalDate currentDate = Instant.ofEpochMilli(CURRENT_DATE.getTime())
					      .atZone(ZoneId.systemDefault())
					      .toLocalDate();
				LocalDate reminderDate = Instant.ofEpochMilli(REMINDER_DATE.getTime())
					      .atZone(ZoneId.systemDefault())
					      .toLocalDate();
				
				log.info("Date #: " + reminderDate.isEqual(currentDate));
				
				// Checking ReminderDate is current Date
				if (reminderDate.isEqual(currentDate)) {
					// Pass CLIENT_ID in CLIENTGENERAL table and fetch CONT_NO value
					ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(CLIENT_ID);
					CONTACT_NO = clientGeneral.getContactNumber();
					log.info("CONTACT_NO : " + CONTACT_NO);
					
					// Send SMS
					CONTACT_NO = CONTACT_NO.replaceAll("\\D", "");
					AuthToken authToken = authTokenService.getCommonServiceAuthToken();
					Boolean response = commonService.sendSMS(Long.valueOf(CONTACT_NO), REMINDER_TEXT, authToken.getAccess_token());
					log.info("SMS Response: " + response);
					
					// Updating status in DB
					List<ExpirationDate> dbExpirationDates = expirationdateRepository.findByClientIdAndMatterNumber (expirationDateRecord.getClientId(), expirationDateRecord.getMatterNumber());
					for (ExpirationDate expirationDate : dbExpirationDates) {
						expirationDate.setReferenceField1(response.toString());
						expirationDate.setUpdatedBy("SYSTEM");
						expirationDate.setUpdatedOn(new Date());
						expirationDate = expirationdateRepository.save(expirationDate);
						log.info("Saved expirationDate : " + expirationDate);
					}
					return response;
				}
			}
		}
		return null;
	}
	
	/**
	 * updateExpirationDate
	 * @param matterNo
	 * @param updateExpirationDate
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpirationDate updateExpirationDate (String matterNo, UpdateExpirationDate updateExpirationDate, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDate dbExpirationDate = 
				getExpirationDate(matterNo, 
						updateExpirationDate.getLanguageId(), 
						updateExpirationDate.getClassId(),
						updateExpirationDate.getClientId(),
						updateExpirationDate.getDocumentType());
		BeanUtils.copyProperties(updateExpirationDate, dbExpirationDate, CommonUtils.getNullPropertyNames(updateExpirationDate));
		dbExpirationDate.setUpdatedBy(loginUserID);
		dbExpirationDate.setUpdatedOn(new Date());
		return expirationdateRepository.save(dbExpirationDate);
	}
	
	/**
	 * deleteExpirationDate
	 * @param matterNo
	 * @param languageId
	 * @param classId
	 * @param clientId
	 * @param documentType
	 * @param loginUserID
	 */
	public void deleteExpirationDate (String matterNo, String languageId, Long classId, String clientId, String documentType, String loginUserID) {
		ExpirationDate dbExpirationDate = 
				getExpirationDate(matterNo, 
						languageId, 
						classId,
						clientId,
						documentType);
		if ( dbExpirationDate != null) {
			dbExpirationDate.setDeletionIndicator (1L);
			dbExpirationDate.setUpdatedBy(loginUserID);
			dbExpirationDate.setUpdatedOn(new Date());
			expirationdateRepository.save(dbExpirationDate);
		} else {
			throw new EntityNotFoundException("Error in deleting the record for the given values.");
		}
	}

	public List<ExpirationDate> findAllExpirationDate(SearchExpirationDate searchExpirationDate) throws ParseException {
//		if (searchExpirationDate.getCreationToDate() != null && searchExpirationDate.getCreationFromDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(searchExpirationDate.getCreationFromDate(), searchExpirationDate.getCreationToDate());
//			searchExpirationDate.setCreationFromDate(dates[0]);
//			searchExpirationDate.setExpirationToDate(dates[1]);
//		}
		List<ExpirationDate> expirationDateList = new ArrayList<>();
		List<IExpirationDate> searchResults = expirationdateRepository.findAllExpirationDate();
		searchResults.forEach(data->{
			ExpirationDate expirationDate = new ExpirationDate();
			BeanUtils.copyProperties(data,expirationDate);
			expirationDateList.add(expirationDate);
		});
		log.info("results: " + expirationDateList);
		return expirationDateList;
	}
}
