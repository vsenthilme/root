package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.expirationdoctype.AddExpirationDocType;
import com.mnrclara.api.setup.model.expirationdoctype.ExpirationDocType;
import com.mnrclara.api.setup.model.expirationdoctype.UpdateExpirationDocType;
import com.mnrclara.api.setup.repository.ExpirationDocTypeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpirationDocTypeService {
	
	@Autowired
	private ExpirationDocTypeRepository expirationdoctypeRepository;
	
	/**
	 * getExpirationDocTypes
	 * @return
	 */
	public List<ExpirationDocType> getExpirationDocTypes () {
		List<ExpirationDocType> expirationdoctypeList = expirationdoctypeRepository.findAll();
		log.info("expirationdoctypeList : " + expirationdoctypeList);
		expirationdoctypeList = expirationdoctypeList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return expirationdoctypeList;
	}
	
	/**
	 * getExpirationDocType
	 * @param documentType
	 * @return
	 */
	public ExpirationDocType getExpirationDocType (String languageId, Long classId, String documentType) {
		Optional<ExpirationDocType> optExpirationdoctype = 
				expirationdoctypeRepository.findByLanguageIdAndClassIdAndDocumentTypeAndDeletionIndicator(
						languageId,
						classId,
						documentType,
						0L);
		if (optExpirationdoctype.isEmpty()) {
			throw new BadRequestException("The record doesn't exist for the given values: DocumentType - " + 
		documentType + ", Class ID - " + classId + ", languageId - " + languageId);
		}
		return optExpirationdoctype.get();
	}
	
	/**
	 * createExpirationDocType
	 * @param newExpirationDocType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpirationDocType createExpirationDocType (AddExpirationDocType newExpirationDocType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<ExpirationDocType> optExpirationdoctype = 
				expirationdoctypeRepository.findByLanguageIdAndClassIdAndDocumentTypeAndDeletionIndicator(
						newExpirationDocType.getLanguageId(),
						newExpirationDocType.getClassId(),
						newExpirationDocType.getDocumentType(),
						0L);
		if (!optExpirationdoctype.isEmpty()) {
			throw new BadRequestException("The given DocumentType data is getting duplicated.");
		}
		
		ExpirationDocType dbExpirationDocType = new ExpirationDocType();
		BeanUtils.copyProperties(newExpirationDocType, dbExpirationDocType, CommonUtils.getNullPropertyNames(newExpirationDocType));
		
		dbExpirationDocType.setCreatedBy(loginUserID);
		dbExpirationDocType.setUpdatedBy(loginUserID);
		dbExpirationDocType.setCreatedOn(new Date());
		dbExpirationDocType.setUpdatedOn(new Date());
		
		log.info("dbExpirationDocType : " + dbExpirationDocType);
		return expirationdoctypeRepository.save(dbExpirationDocType);
	}
	
	/**
	 * updateExpirationDocType
	 * @param expirationdoctypeCode
	 * @param updateExpirationDocType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ExpirationDocType updateExpirationDocType (String documentType, UpdateExpirationDocType updateExpirationDocType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ExpirationDocType dbExpirationDocType = getExpirationDocType(updateExpirationDocType.getLanguageId(), 
				updateExpirationDocType.getClassId(), documentType);
		BeanUtils.copyProperties(updateExpirationDocType, dbExpirationDocType, CommonUtils.getNullPropertyNames(updateExpirationDocType));
		dbExpirationDocType.setUpdatedBy(loginUserID);
		dbExpirationDocType.setUpdatedOn(new Date());
		return expirationdoctypeRepository.save(dbExpirationDocType);
	}
	
	/**
	 * deleteExpirationDocType
	 * @param expirationdoctypeCode
	 */
	public void deleteExpirationDocType (String languageId, Long classId, String documentType, String loginUserID) {
		ExpirationDocType expirationdoctype = getExpirationDocType(languageId, classId, documentType);
		if ( expirationdoctype != null) {
			expirationdoctype.setDeletionIndicator (1L);
			expirationdoctype.setUpdatedBy(loginUserID);
			expirationdoctype.setUpdatedOn(new Date());
			expirationdoctypeRepository.save(expirationdoctype);
		} else {
			throw new EntityNotFoundException("Error in deleting Id:" + documentType);
		}
	}
}
