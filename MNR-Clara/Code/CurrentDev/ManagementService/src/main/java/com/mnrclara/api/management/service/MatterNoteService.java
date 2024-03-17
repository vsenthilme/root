package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.matternote.AddMatterNote;
import com.mnrclara.api.management.model.matternote.MatterNote;
import com.mnrclara.api.management.model.matternote.SearchMatterNote;
import com.mnrclara.api.management.model.matternote.UpdateMatterNote;
import com.mnrclara.api.management.repository.MatterNoteRepository;
import com.mnrclara.api.management.repository.specification.MatterNoteSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterNoteService {
	
	private static final String MATTERNOTES = "MATTERNOTES";

	@Autowired
	private MatterNoteRepository matterNoteRepository;
	
	@Autowired
	private SetupService setupService;

	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * getMatterNotes
	 * @return
	 */
	public List<MatterNote> getMatterNotes () {
		List<MatterNote> matterNoteList =  matterNoteRepository.findAll();
		matterNoteList = matterNoteList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return matterNoteList;
	}
	
	/**
	 * getMatterNote
	 * @param matterNoteId
	 * @return
	 */
	public MatterNote getMatterNote (String matterNoteNumber) {
		List<MatterNote> matterNote = matterNoteRepository.findByNotesNumberAndDeletionIndicator(matterNoteNumber, 0L);
		log.info("matterNote-------->:" + matterNote);
		if (matterNote != null && !matterNote.isEmpty()) {
			return matterNote.get(0);
		} 
		return null;
	}
	
	/**
	 * findMatterNotes
	 * @param searchMatterNote
	 * @return
	 * @throws ParseException 
	 */
	public List<MatterNote> findMatterNotes (SearchMatterNote searchMatterNote) throws ParseException {
		if (searchMatterNote.getStartCreatedOn() != null && searchMatterNote.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterNote.getStartCreatedOn(), searchMatterNote.getEndCreatedOn());
			searchMatterNote.setStartCreatedOn(dates[0]);
			searchMatterNote.setEndCreatedOn(dates[1]);
		}
		
		MatterNoteSpecification spec = new MatterNoteSpecification(searchMatterNote);
		List<MatterNote> searchResults = matterNoteRepository.findAll(spec);
//		log.info("searchResults: " + searchResults);
		return searchResults;
	}
	
	/**
	 * createMatterNote
	 * @param newMatterNote
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterNote createMatterNote (AddMatterNote newMatterNote, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterNote dbMatterNote = new MatterNote();
		BeanUtils.copyProperties(newMatterNote, dbMatterNote, CommonUtils.getNullPropertyNames(newMatterNote));
		
		// Pass CLASS_ID=03,NUM_RAN_CODE=02 and fetch NUM_RAN_CURRENT values and add+1 . 
		long classID = 3;
		long NUM_RAN_CODE = 2;
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		String NOTE_NO = setupService.getNextNumberRange(classID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange for NOTE_NO: " + NOTE_NO);
		
		// NOTE_NO
		dbMatterNote.setNotesNumber(NOTE_NO);
		
		// LANG_ID
		dbMatterNote.setLanguageId("EN");
		
		// CLASS_ID
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		log.info("userProfile : " + userProfile);
		dbMatterNote.setClassId(userProfile.getClassId());
		
		// STATUS_ID
		dbMatterNote.setStatusId(18L); // Hard coded value "18" and insert during save
		
		dbMatterNote.setDeletionIndicator(0L);

		// Created By
		dbMatterNote.setCreatedBy(loginUserID);

		// Created On
		dbMatterNote.setCreatedOn(new Date());

		// Updated By
		dbMatterNote.setUpdatedBy(loginUserID);

		// Updated On
		dbMatterNote.setUpdatedOn(new Date());
		
		return matterNoteRepository.save(dbMatterNote);
	}
	
	/**
	 * 
	 * @param newMatterNotes
	 * @param loginUserID
	 */
	public void createBulkMatterNotes(@Valid AddMatterNote[] newMatterNotes, String loginUserID) {
		List<MatterNote> createMatterNotes = new ArrayList<>();
		for (AddMatterNote newMatterNote : newMatterNotes) {
			MatterNote dbMatterNote = new MatterNote();
			BeanUtils.copyProperties(newMatterNote, dbMatterNote, CommonUtils.getNullPropertyNames(newMatterNote));
			
			// Pass CLASS_ID=03,NUM_RAN_CODE=02 and fetch NUM_RAN_CURRENT values and add+1 . 
			long classID = 3;
			long NUM_RAN_CODE = 2;
			AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
			String NOTE_NO = setupService.getNextNumberRange(classID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange for NOTE_NO: " + NOTE_NO);
			
			// NOTE_NO
			dbMatterNote.setNotesNumber(NOTE_NO);
			
			// STATUS_ID
			dbMatterNote.setStatusId(18L); // Hard coded value "18" and insert during save
			
			dbMatterNote.setDeletionIndicator(0L);
			createMatterNotes.add(dbMatterNote);
		}
		List<MatterNote> createdMatterNotes = matterNoteRepository.saveAll(createMatterNotes);
		log.info("createdMatterNotes : " + createdMatterNotes);
	}
	
	/**
	 * updateMatterNote
	 * @param matternoteId
	 * @param updateMatterNote
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterNote updateMatterNote (String matterNoteNumber, UpdateMatterNote updateMatterNote, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterNote dbMatterNote = getMatterNote(matterNoteNumber);
		BeanUtils.copyProperties(updateMatterNote, dbMatterNote, CommonUtils.getNullPropertyNames(updateMatterNote));
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// NOTE_TYP_ID/NOTE_TYP_TEXT
		if (updateMatterNote.getNoteTypeId() != null
				&& updateMatterNote.getNoteTypeId().longValue() != dbMatterNote.getNoteTypeId().longValue()) {
			log.info("Inserting Audit log for NOTE_TYP_ID");
			setupService.createAuditLogRecord(loginUserID, matterNoteNumber, 3L, MATTERNOTES,
					"NOTE_TYP_ID", String.valueOf(dbMatterNote.getNoteTypeId()),
					String.valueOf(updateMatterNote.getNoteTypeId()), authTokenForSetupService.getAccess_token());
		}
		
		// MATTER_NO
		if (updateMatterNote.getMatterNumber() != null
				&& !updateMatterNote.getMatterNumber().equalsIgnoreCase(dbMatterNote.getMatterNumber())) {
			log.info("Inserting Audit log for MATTER_NO");
			setupService.createAuditLogRecord(loginUserID, matterNoteNumber, 3L, MATTERNOTES, 
					"MATTER_NO", dbMatterNote.getMatterNumber(),
					updateMatterNote.getMatterNumber(), authTokenForSetupService.getAccess_token());
		}
		
		// NOTE_TEXT
		if (updateMatterNote.getNotesDescription() != null
				&& !updateMatterNote.getNotesDescription().equalsIgnoreCase(dbMatterNote.getNotesDescription())) {
			log.info("Inserting Audit log for NOTE_TEXT");
			setupService.createAuditLogRecord(loginUserID, matterNoteNumber, 3L, MATTERNOTES, 
					"NOTE_TEXT", dbMatterNote.getNotesDescription(),
					updateMatterNote.getNotesDescription(), authTokenForSetupService.getAccess_token());
		}
		
		// STATUS_ID
		if (updateMatterNote.getStatusId() != null
				&& updateMatterNote.getStatusId().longValue() != dbMatterNote.getStatusId().longValue()) {
			log.info("Inserting Audit log for STATUS_ID");
			setupService.createAuditLogRecord(loginUserID, matterNoteNumber, 3L, MATTERNOTES,
					"STATUS_ID", String.valueOf(dbMatterNote.getStatusId()),
					String.valueOf(updateMatterNote.getStatusId()), authTokenForSetupService.getAccess_token());
		}
		
		dbMatterNote.setUpdatedBy(loginUserID);
		dbMatterNote.setUpdatedOn(new Date());
		return matterNoteRepository.save(dbMatterNote);
	}
	
	/**
	 * deleteMatterNote
	 * @param matterNoteNumber
	 * @param loginUserID
	 */
	public void deleteMatterNote (String matterNoteNumber, String loginUserID) {
		MatterNote matternote = getMatterNote(matterNoteNumber);
		if ( matternote != null) {
			matternote.setDeletionIndicator(1L);
			matternote.setUpdatedBy(loginUserID);
			matternote.setUpdatedOn(new Date());
			matterNoteRepository.save(matternote);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterNoteNumber);
		}
	}
}
