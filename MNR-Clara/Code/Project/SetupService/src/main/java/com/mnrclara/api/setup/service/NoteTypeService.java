package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.notetype.AddNoteType;
import com.mnrclara.api.setup.model.notetype.NoteType;
import com.mnrclara.api.setup.model.notetype.UpdateNoteType;
import com.mnrclara.api.setup.repository.NoteTypeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteTypeService {
	
	@Autowired
	private NoteTypeRepository noteTypeRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<NoteType> getNoteTypes () {
		List<NoteType> noteTypeList = noteTypeRepository.findAll();
		noteTypeList = noteTypeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return noteTypeList;
	}
	
	/**O
	 * getNoteType
	 * @param noteTypeId
	 * @return
	 */
	public NoteType getNoteType (Long noteTypeId) {
		NoteType noteType = noteTypeRepository.findByNoteTypeId(noteTypeId);
		if (noteType.getDeletionIndicator() == 0) {
			return noteType;
		} else {
			throw new BadRequestException("The given NoteType ID : " + noteTypeId + " doesn't exist.");
		}
	}
	
	/**
	 * createNoteType
	 * @param newNoteType
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NoteType createNoteType (AddNoteType newNoteType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType dbNoteType = new NoteType();
		BeanUtils.copyProperties(newNoteType, dbNoteType, CommonUtils.getNullPropertyNames(newNoteType));
		dbNoteType.setDeletionIndicator(0L);
		dbNoteType.setCreatedBy(loginUserID);
		dbNoteType.setUpdatedBy(loginUserID);
		dbNoteType.setCreatedOn(new Date());
		dbNoteType.setUpdatedOn(new Date());
		return noteTypeRepository.save(dbNoteType);
	}
	
	/**
	 * updateNoteType
	 * @param noteTypeId
	 * @param loginUserId 
	 * @param updateNoteType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NoteType updateNoteType (Long noteTypeId, String loginUserID, UpdateNoteType updateNoteType) 
			throws IllegalAccessException, InvocationTargetException {
		NoteType dbNoteType = getNoteType(noteTypeId);
		BeanUtils.copyProperties(updateNoteType, dbNoteType, CommonUtils.getNullPropertyNames(updateNoteType));
		dbNoteType.setUpdatedBy(loginUserID);
		dbNoteType.setUpdatedOn(new Date());
		return noteTypeRepository.save(dbNoteType);
	}
	
	/**
	 * deleteNoteType
	 * @param loginUserID 
	 * @param noteTypeCode
	 */
	public void deleteNoteType (Long noteTypeId, String loginUserID) {
		NoteType noteType = getNoteType(noteTypeId);
		if ( noteType != null) {
			noteType.setDeletionIndicator(1L);
			noteType.setUpdatedBy(loginUserID);
			noteTypeRepository.save(noteType);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + noteTypeId);
		}
	}
}
