package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientnote.AddClientNote;
import com.mnrclara.api.management.model.clientnote.ClientNote;
import com.mnrclara.api.management.model.clientnote.SearchClientNote;
import com.mnrclara.api.management.model.clientnote.UpdateClientNote;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.repository.ClientNoteRepository;
import com.mnrclara.api.management.repository.specification.ClientNoteSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientNoteService {

	private static final String CLIENTNOTES = "CLIENTNOTES";

	@Autowired
	private ClientNoteRepository clientNoteRepository;

	@Autowired
	private SetupService setupService;

	@Autowired
	AuthTokenService authTokenService;

	/**
	 * getClientNotes
	 * 
	 * @return
	 */
	public List<ClientNote> getClientNotes() {
		List<ClientNote> clientNoteList = clientNoteRepository.findAll();
		log.info("clientNoteList : " + clientNoteList);
		if (clientNoteList != null) {
			clientNoteList = clientNoteList.stream().filter(n -> n.getDeletionIndicator() == 0)
					.collect(Collectors.toList());
			return clientNoteList;
		}
		return clientNoteList;
	}

	/**
	 * getClientNote
	 * 
	 * @param clientNotesNumber
	 * @return
	 */
	public ClientNote getClientNote(String clientNotesNumber) {
		ClientNote clientNote = clientNoteRepository.findByNotesNumber(clientNotesNumber).orElse(null);
		if (clientNote != null && clientNote.getDeletionIndicator() != null && clientNote.getDeletionIndicator() == 0) {
			return clientNote;
		} else {
			throw new BadRequestException("The given ClientNote ID : " + clientNote + " doesn't exist.");
		}
	}

	/**
	 * createClientNote
	 * 
	 * @param newClientNote
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientNote createClientNote(AddClientNote newClientNote, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientNote dbClientNote = new ClientNote();
		// NOTE_TYP_ID/NOTE_TYP_TEXT
		dbClientNote.setNoteTypeId(newClientNote.getNoteTypeId());

		// MATTER_NO
		dbClientNote.setMatterNumber(newClientNote.getMatterNumber());

		// NOTE_TEXT
		dbClientNote.setNoteText(newClientNote.getNoteText());
		
		// CLIENT_ID
		dbClientNote.setClientId(newClientNote.getClientId());

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		log.info("userProfile : " + userProfile);

		// Pass CLASS_ID=03,NUM_RAN_CODE=02 and fetch NUM_RAN_CURRENT values and add+1 . 
		long classID = 3;
		long NUM_RAN_CODE = 2;
		String NOTE_NO = setupService.getNextNumberRange(classID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange for NOTE_NO: " + NOTE_NO);
		
		// NOTE_NO
		dbClientNote.setNotesNumber(NOTE_NO);

		// LANG_ID
		dbClientNote.setLanguageId(userProfile.getLanguageId());

		// CLASS_ID
		dbClientNote.setClassId(userProfile.getClassId());

		// STATUS_ID - Hard coded value "18" and insert during save
		Long STATUS_ID = 18L;
		dbClientNote.setStatusId(STATUS_ID);

		dbClientNote.setDeletionIndicator(0L);

		// Created By
		dbClientNote.setCreatedBy(loginUserID);

		// Created On
		dbClientNote.setCreatedOn(new Date());

		// Updated By
		dbClientNote.setUpdatedBy(loginUserID);

		// Updated On
		dbClientNote.setUpdatedOn(new Date());

		return clientNoteRepository.save(dbClientNote);
	}

	/**
	 * updateClientNote
	 * 
	 * @param clientnoteId
	 * @param updateClientNote
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientNote updateClientNote(String clientNotesNumber, UpdateClientNote updateClientNote, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientNote dbClientNote = getClientNote(clientNotesNumber);

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// NOTE_TYP_ID/NOTE_TYP_TEXT
		if (updateClientNote.getNoteTypeId() != null
				&& updateClientNote.getNoteTypeId().longValue() != dbClientNote.getNoteTypeId().longValue()) {
			log.info("Inserting Audit log for NOTE_TYP_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientNotesNumber), 3L, CLIENTNOTES,
					"NOTE_TYP_ID", String.valueOf(dbClientNote.getNoteTypeId()),
					String.valueOf(updateClientNote.getNoteTypeId()), authTokenForSetupService.getAccess_token());
		}

		// MATTER_NO
		if (updateClientNote.getMatterNumber() != null
				&& !updateClientNote.getMatterNumber().equalsIgnoreCase(dbClientNote.getMatterNumber())) {
			log.info("Inserting Audit log for MATTER_NO");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientNotesNumber), 3L, CLIENTNOTES,
					"MATTER_NO", String.valueOf(dbClientNote.getMatterNumber()),
					String.valueOf(updateClientNote.getMatterNumber()), authTokenForSetupService.getAccess_token());
		}

		// NOTE_TEXT
		if (updateClientNote.getNoteText() != null
				&& !updateClientNote.getNoteText().equalsIgnoreCase(dbClientNote.getNoteText())) {
			log.info("Inserting Audit log for NOTE_TEXT");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientNotesNumber), 3L, CLIENTNOTES,
					"NOTE_TEXT", String.valueOf(dbClientNote.getNoteText()),
					String.valueOf(updateClientNote.getNoteText()), authTokenForSetupService.getAccess_token());
		}

		BeanUtils.copyProperties(updateClientNote, dbClientNote, CommonUtils.getNullPropertyNames(updateClientNote));

		// Updated By
		dbClientNote.setUpdatedBy(loginUserID);

		// Updated On
		dbClientNote.setUpdatedOn(new Date());
		return clientNoteRepository.save(dbClientNote);
	}

	/**
	 * 
	 * @param searchClientNote
	 * @return
	 * @throws ParseException 
	 */
	public List<ClientNote> findClientNotes(SearchClientNote searchClientNote) throws ParseException {
		if (searchClientNote.getStartCreatedOn() != null && searchClientNote.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientNote.getStartCreatedOn(), searchClientNote.getEndCreatedOn());
			searchClientNote.setStartCreatedOn(dates[0]);
			searchClientNote.setEndCreatedOn(dates[1]);
		}
		
		ClientNoteSpecification spec = new ClientNoteSpecification(searchClientNote);
		List<ClientNote> searchResults = clientNoteRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}

	/**
	 * deleteClientNote
	 * 
	 * @param clientNotesNumber
	 */
	public void deleteClientNote(String clientNotesNumber, String loginUserID) {
		ClientNote clientNote = getClientNote(clientNotesNumber);
		if (clientNote != null) {
			clientNote.setDeletionIndicator(1L);
			clientNote.setUpdatedBy(loginUserID);
			clientNote.setUpdatedOn(new Date());
			clientNoteRepository.save(clientNote);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientNotesNumber);
		}
	}

	/**
	 * 
	 * @param newClientNotes
	 * @param loginUserID
	 */
	public void createBulkClientNotes(AddClientNote[] newClientNotes, String loginUserID) {
		List<ClientNote> clientNotes = new ArrayList<ClientNote>();
		for (AddClientNote addClientNote : newClientNotes) {
			log.info("addClientNote : " + addClientNote);
			ClientNote clientNote = new ClientNote();
			BeanUtils.copyProperties(addClientNote, clientNote, CommonUtils.getNullPropertyNames(addClientNote));
			clientNote.setDeletionIndicator(0L);
			
			if (clientNote.getAdditionalFields1() != null) {
				clientNote.setAdditionalFields1(clientNote.getAdditionalFields1().trim());
			}
			
			if (clientNote.getAdditionalFields2() != null) {
				clientNote.setAdditionalFields2(clientNote.getAdditionalFields2().trim());
			}
			
			if (clientNote.getAdditionalFields3() != null) {
				clientNote.setAdditionalFields3(clientNote.getAdditionalFields3().trim());
			}
			
			if (clientNote.getAdditionalFields4() != null) {
				clientNote.setAdditionalFields4(clientNote.getAdditionalFields4().trim());
			}
			
			if (clientNote.getAdditionalFields5() != null) {
				clientNote.setAdditionalFields5(clientNote.getAdditionalFields5().trim());
			}
			
			if (clientNote.getAdditionalFields6() != null) {
				clientNote.setAdditionalFields6(clientNote.getAdditionalFields6().trim());
			}
			
			if (clientNote.getAdditionalFields7() != null) {
				clientNote.setAdditionalFields7(clientNote.getAdditionalFields7().trim());
			}
			clientNotes.add(clientNote);
		}
		clientNoteRepository.saveAll(clientNotes);
		log.info("ClientNotescreated...");
	}
}
