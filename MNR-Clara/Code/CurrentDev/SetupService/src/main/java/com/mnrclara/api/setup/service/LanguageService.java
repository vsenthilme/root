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
import com.mnrclara.api.setup.model.language.AddLanguage;
import com.mnrclara.api.setup.model.language.Language;
import com.mnrclara.api.setup.model.language.UpdateLanguage;
import com.mnrclara.api.setup.repository.LanguageRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LanguageService {
	
	@Autowired
	private LanguageRepository languageRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Language> getLanguages () {
		List<Language> languageList = languageRepository.findAll();
		if (languageList != null) {
			languageList = languageList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
			return languageList;
		}
		return languageList;
	}
	
	/**
	 * getLanguage
	 * @param languageId
	 * @return
	 */
	public Language getLanguage (String languageId) {
		Language language = languageRepository.findByLanguageId(languageId).orElse(null);
		if (language != null && language.getDeletionIndicator() == 0) {
			return language;
		} else {
			throw new BadRequestException("The given Language ID : " + languageId + " doesn't exist.");
		}
	}
	
	/**
	 * createLanguage
	 * @param newLanguage
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Language createLanguage (AddLanguage newLanguage, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Language> language = 
				languageRepository.findByLanguageIdAndClassIdAndDeletionIndicator (		
					newLanguage.getLanguageId(),
					newLanguage.getClassId(),
					0L);
		if (!language.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Language dbLanguage = new Language();
		BeanUtils.copyProperties(newLanguage, dbLanguage);
		dbLanguage.setDeletionIndicator(0L);
		dbLanguage.setCreatedBy(loginUserID);
		dbLanguage.setUpdatedBy(loginUserID);
		dbLanguage.setCreatedOn(new Date());
		dbLanguage.setUpdatedOn(new Date());
		return languageRepository.save(dbLanguage);
	}
	
	/**
	 * updateLanguage
	 * @param languageId
	 * @param loginUserId 
	 * @param updateLanguage
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Language updateLanguage (String languageId, String loginUserID, UpdateLanguage updateLanguage) 
			throws IllegalAccessException, InvocationTargetException {
		Language dbLanguage = getLanguage(languageId);
		BeanUtils.copyProperties(updateLanguage, dbLanguage, CommonUtils.getNullPropertyNames(updateLanguage));
		dbLanguage.setUpdatedBy(loginUserID);
		dbLanguage.setUpdatedOn(new Date());
		return languageRepository.save(dbLanguage);
	}
	
	/**
	 * deleteLanguage
	 * @param loginUserID 
	 * @param languageCode
	 */
	public void deleteLanguage (String languageModuleId, String loginUserID) {
		Language language = getLanguage(languageModuleId);
		if ( language != null) {
			language.setDeletionIndicator(1L);
			language.setUpdatedBy(loginUserID);
			languageRepository.save(language);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + languageModuleId);
		}
	}
}
