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
import com.mnrclara.api.setup.model.documenttemplate.AddDocumentTemplate;
import com.mnrclara.api.setup.model.documenttemplate.DocumentTemplate;
import com.mnrclara.api.setup.model.documenttemplate.DocumentTemplateCompositeKey;
import com.mnrclara.api.setup.model.documenttemplate.UpdateDocumentTemplate;
import com.mnrclara.api.setup.repository.DocumentTemplateRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentTemplateService {
	
	@Autowired
	private DocumentTemplateRepository documentTemplateRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<DocumentTemplate> getDocumentTemplates () {
		List<DocumentTemplate> documentTemplateList = documentTemplateRepository.findAll();
		return documentTemplateList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getDocumentTemplate
	 * @param documentTemplateId
	 * @return
	 */
	public DocumentTemplate getDocumentTemplate (String documentTemplateId) {
		DocumentTemplate documentTemplate = documentTemplateRepository.findByDocumentNumber(documentTemplateId).orElse(null);
		if ( documentTemplate != null && documentTemplate.getDeletionIndicator() == 0) {
			return documentTemplate;
		} else {
			throw new BadRequestException("The given DocumentTemplate ID : " + documentTemplateId + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param updateDocumentTemplate
	 * @return
	 */
	public DocumentTemplate getDocumentTemplate (DocumentTemplateCompositeKey compositeKey) {
		Optional<DocumentTemplate> documentTemplate = 
				documentTemplateRepository.findByLanguageIdAndClassIdAndCaseCategoryIdAndDocumentNumberAndDeletionIndicator (
						compositeKey.getLanguageId(),
						compositeKey.getClassId(),
						compositeKey.getCaseCategoryId(),
						compositeKey.getDocumentNumber(),
						0L);
		log.info("documentTemplate : " + documentTemplate);
		if (documentTemplate.isEmpty()) {
			throw new BadRequestException("The given DocumentTemplate doesn't exist.");
		}
		return documentTemplate.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public DocumentTemplate getTopDocumentTemplate () {
		return documentTemplateRepository.findTopByOrderByCreatedOnDesc();
	}
	
	/**
	 * createDocumentTemplate
	 * @param newDocumentTemplate
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DocumentTemplate createDocumentTemplate (AddDocumentTemplate newDocumentTemplate, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<DocumentTemplate> documentTemplate = 
				documentTemplateRepository.findByLanguageIdAndClassIdAndCaseCategoryIdAndDocumentNumberAndDeletionIndicator(
					newDocumentTemplate.getLanguageId(),
					newDocumentTemplate.getClassId(),
					newDocumentTemplate.getCaseCategoryId(),
					newDocumentTemplate.getDocumentNumber(),
					0L);
		if (!documentTemplate.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		
		DocumentTemplate topDocumentTemplate = getTopDocumentTemplate();
		String maxDocumentTemplate = null;
		if (topDocumentTemplate != null) {
			maxDocumentTemplate = topDocumentTemplate.getDocumentNumber();
			maxDocumentTemplate = maxDocumentTemplate.substring(1);
			Long code = Long.valueOf(maxDocumentTemplate);
			code ++;
			maxDocumentTemplate = String.valueOf(code);
			if (maxDocumentTemplate.length() == 1) {
				maxDocumentTemplate = "D00" + maxDocumentTemplate;
			} else if (maxDocumentTemplate.length() == 2) {
				maxDocumentTemplate = "D0" + maxDocumentTemplate;
			} else {
				maxDocumentTemplate = "D" + maxDocumentTemplate;
			}
		} else {
			maxDocumentTemplate = "D001";
		}
		
		log.info("Code generated : " + maxDocumentTemplate);
		
		DocumentTemplate dbDocumentTemplate = new DocumentTemplate();
		BeanUtils.copyProperties(newDocumentTemplate, dbDocumentTemplate);
		
		dbDocumentTemplate.setDocumentNumber(maxDocumentTemplate);
		dbDocumentTemplate.setDeletionIndicator(0L);
		dbDocumentTemplate.setCreatedBy(loginUserID);
		dbDocumentTemplate.setUpdatedBy(loginUserID);
		dbDocumentTemplate.setCreatedOn(new Date());
		dbDocumentTemplate.setUpdatedOn(new Date());
		return documentTemplateRepository.save(dbDocumentTemplate);
	}
	
	/**
	 * updateDocumentTemplate
	 * @param documentTemplateId
	 * @param loginUserId 
	 * @param updateDocumentTemplate
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DocumentTemplate updateDocumentTemplate (UpdateDocumentTemplate updateDocumentTemplate, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DocumentTemplateCompositeKey key = new DocumentTemplateCompositeKey();
		BeanUtils.copyProperties(updateDocumentTemplate, key, CommonUtils.getNullPropertyNames(updateDocumentTemplate));
		
		log.info("key : " + key);
		DocumentTemplate dbDocumentTemplate = getDocumentTemplate(key);
		BeanUtils.copyProperties(updateDocumentTemplate, dbDocumentTemplate, CommonUtils.getNullPropertyNames(updateDocumentTemplate));
		dbDocumentTemplate.setUpdatedBy(loginUserID);
		dbDocumentTemplate.setUpdatedOn(new Date());
		return documentTemplateRepository.save(dbDocumentTemplate);
	}
	
	/**
	 * deleteDocumentTemplate
	 * @param loginUserID 
	 * @param documentTemplateCode
	 */
	public void deleteDocumentTemplate (DocumentTemplateCompositeKey key, String loginUserID) {
		DocumentTemplate documentTemplate = getDocumentTemplate(key);
		if ( documentTemplate != null) {
			documentTemplate.setDeletionIndicator(1L);
			documentTemplate.setUpdatedBy(loginUserID);
			documentTemplateRepository.save(documentTemplate);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + key);
		}
	}
}
