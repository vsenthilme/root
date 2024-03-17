package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.model.notes.AddNotes;
import com.mnrclara.api.crm.model.notes.Notes;
import com.mnrclara.api.crm.model.notes.UpdateNotes;
import com.mnrclara.api.crm.repository.NotesRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotesService {
	
	@Autowired
	SetupService setupSerice;
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	NotesRepository notesRepository;
	
	/**
	 * getNotess
	 * @return
	 */
	public List<Notes> getNotess () {
		List<Notes> notess = notesRepository.findAll();
		notess = notess.stream().filter(a -> a.getDeletionIndicator() != 1).collect(Collectors.toList());
		return notess;
	}
	
	/**
	 * getNotes
	 * @param notesModuleCode
	 * @return
	 */
	public Notes getNotes (String notesNumber) {
		Notes notes = notesRepository.findByNotesNumber(notesNumber).orElse(null);
		if (notes != null && notes.getDeletionIndicator() != null && notes.getDeletionIndicator() == 0L) {
			return notes;
		}
//		throw new BadRequestException("The given NotesNumber : " + notesNumber + " doesn't exists");
		return null;
	}
	
	/**
	 * createNotes
	 * @param newNotes
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notes createNotes (AddNotes newNotes, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notes dbNotes = new Notes();
		BeanUtils.copyProperties(newNotes, dbNotes);
		dbNotes.setCreatedBy(loginUserID);
		dbNotes.setUpdatedBy(newNotes.getCreatedBy());
		dbNotes.setDeletionIndicator(0L);
		return notesRepository.save(dbNotes);
	}
	
	/**
	 * 
	 * @param newTransactionNo
	 * @param transactionId
	 * @param noteTypeId
	 * @param classID
	 * @param numberRangeCode
	 * @param noteText
	 * @param loginUserID
	 * @param websiteLang
	 * @param setupServiceAuthToken
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notes createNotes (String newTransactionNo, 
			Long transactionId,
			Long noteTypeId,
			Long classID,
			Long numberRangeCode,
			String noteText, String loginUserID, String websiteLang, String setupServiceAuthToken) 
			throws IllegalAccessException, InvocationTargetException {
		
		// Insert new record in NoteTable
		AddNotes newNotes = new AddNotes();
		newNotes.setTransactionId(transactionId); 			// TRANS_ID=01
		newNotes.setTransactionNo(newTransactionNo); 		// TRANS_NO=INQ_NO
		newNotes.setNoteTypeId(noteTypeId);					// NOTE_TYP_ID=01
		
		// NOTE_NO
		String newNotesNumber = setupSerice.getNextNumberRange(classID, numberRangeCode, setupServiceAuthToken);
		log.info("nextVal from NotesNumber : " + newNotesNumber);
		newNotes.setNotesNumber(newNotesNumber);
		
		// CLASS_ID
		newNotes.setClassId(classID); // classID = 3L
		
		// NOTE_TEXT - ReferenceField for receiving NOTE_TEXT
		newNotes.setNotesDescription(noteText); 
		
		// LANG_ID
		if (loginUserID != null) {
			String langID = setupSerice.getLanguageId(loginUserID, setupServiceAuthToken);
			newNotes.setLanguageId(langID);
		} else {
			// Setting Language for Website users
			newNotes.setLanguageId(websiteLang);
		}
		
		// Created By
		newNotes.setCreatedBy(loginUserID);
		
		// Saving Notes Table
		Notes createdNotes = createNotes(newNotes, loginUserID);
		return createdNotes;
	}
	
	/**
	 * updateNotes
	 * @param notesCode
	 * @param updateNotes
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notes updateNotes (String notesNumber, UpdateNotes updateNotes, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notes dbNotes = getNotes(notesNumber);
		log.info ("DB : " + dbNotes);
		BeanUtils.copyProperties(updateNotes, dbNotes);
		dbNotes.setUpdatedBy(loginUserID);
		log.info ("After modified : " + dbNotes);
		return notesRepository.save(dbNotes);
	}
	
	/**
	 * 
	 * @param notesNumber
	 * @param noteText
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notes updateNotes (String notesNumber, String noteText, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notes dbNotes = getNotes(notesNumber);
		dbNotes.setNotesDescription(noteText);
		dbNotes.setUpdatedBy(loginUserID);
		log.info ("After modified : " + dbNotes);
		return notesRepository.save(dbNotes);
	}
	
	/**
	 * deleteNotes
	 * @param notesCode
	 */
	public void deleteNotes (String notesCode, String loginUserID) {
		Notes notes = getNotes(notesCode);
		if ( notes != null) {
			notes.setDeletionIndicator(1L);
			notes.setUpdatedBy(loginUserID);
			notesRepository.save(notes);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + notesCode);
		}
	}
}
