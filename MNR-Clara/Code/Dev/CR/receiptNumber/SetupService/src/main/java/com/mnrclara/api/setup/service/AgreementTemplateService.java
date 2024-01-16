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
import com.mnrclara.api.setup.model.agreementtemplate.AddAgreementTemplate;
import com.mnrclara.api.setup.model.agreementtemplate.AgreementTemplate;
import com.mnrclara.api.setup.model.agreementtemplate.UpdateAgreementTemplate;
import com.mnrclara.api.setup.repository.AgreementTemplateRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgreementTemplateService {
	
	@Autowired
	private AgreementTemplateRepository agreementTemplateRepository;
	
	@Autowired
	private NumberRangeService numberRangeService;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<AgreementTemplate> getAgreementTemplates () {
		List<AgreementTemplate> agreementTemplateList =  agreementTemplateRepository.findAll();
		agreementTemplateList = agreementTemplateList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return agreementTemplateList;
	}
	
	/**
	 * getAgreementTemplate
	 * @param aggrementtemplateId
	 * @return
	 */
	public AgreementTemplate getAgreementTemplate (String agreementCode) {
		AgreementTemplate agreementTemplate = agreementTemplateRepository.findByAgreementCode(agreementCode).orElse(null);
		if (agreementTemplate != null && agreementTemplate.getDeletionIndicator() == 0) {
			return agreementTemplate;
		} else {
			throw new BadRequestException("The given AgreementTemplate ID : " + agreementCode + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param agreementCode
	 * @param classId
	 * @return
	 */
	public List<AgreementTemplate> getAgreementTemplate (String agreementCode, Long classId) {
		List<AgreementTemplate> agreementTemplate = agreementTemplateRepository.findByAgreementCodeAndClassId(agreementCode, classId);
		return agreementTemplate;
	}
	
	/**
	 * getTopAgreementTemplate
	 * @return
	 */
	public AgreementTemplate getTopAgreementTemplate () {
		return agreementTemplateRepository.findTopByOrderByCreatedOnDesc();
	}
	
	/**
	 * createAgreementTemplate
	 * @param newAgreementTemplate
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AgreementTemplate createAgreementTemplate (AddAgreementTemplate newAgreementTemplate, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<AgreementTemplate> agreementtemplate = 
				agreementTemplateRepository.findByLanguageIdAndClassIdAndCaseCategoryIdAndAgreementCodeAndAgreementUrlAndDeletionIndicator(
						newAgreementTemplate.getLanguageId(), 
						newAgreementTemplate.getClassId(), 
						newAgreementTemplate.getCaseCategoryId(), 
						newAgreementTemplate.getAgreementCode(), 
						newAgreementTemplate.getAgreementUrl(),
						0L);
		if (!agreementtemplate.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		
		AgreementTemplate dbAgreementTemplate = new AgreementTemplate();
		
		AgreementTemplate topAgreementTemplate = getTopAgreementTemplate();
		String maxAgreementTempCode = null;
		if (topAgreementTemplate != null) {
			maxAgreementTempCode = topAgreementTemplate.getAgreementCode();
			maxAgreementTempCode = maxAgreementTempCode.substring(1);
			Long code = Long.valueOf(maxAgreementTempCode);
			code ++;
			maxAgreementTempCode = String.valueOf(code);
			if (maxAgreementTempCode.length() == 1) {
				maxAgreementTempCode = "A00" + maxAgreementTempCode;
			} else if (maxAgreementTempCode.length() == 2) {
				maxAgreementTempCode = "A0" + maxAgreementTempCode;
			} else {
				maxAgreementTempCode = "A" + maxAgreementTempCode;
			}
		} else {
			maxAgreementTempCode = "A001";
		}
		
		log.info("Code generated : " + maxAgreementTempCode);
		BeanUtils.copyProperties(newAgreementTemplate, dbAgreementTemplate, CommonUtils.getNullPropertyNames(newAgreementTemplate));
		dbAgreementTemplate.setAgreementCode(maxAgreementTempCode);
		dbAgreementTemplate.setDeletionIndicator(0L);
		dbAgreementTemplate.setCreatedBy(loginUserID);
		dbAgreementTemplate.setUpdatedBy(loginUserID);
		dbAgreementTemplate.setCreatedOn(new Date());
		dbAgreementTemplate.setUpdatedOn(new Date());
		return agreementTemplateRepository.save(dbAgreementTemplate);
	}
	
	/**
	 * updateAgreementTemplate
	 * @param loginUserId 
	 * @param aggrementtemplateId
	 * @param updateAgreementTemplate
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AgreementTemplate updateAgreementTemplate (String agreementCode, 
			String loginUserID, UpdateAgreementTemplate updateAgreementTemplate) 
			throws IllegalAccessException, InvocationTargetException {
		AgreementTemplate dbAgreementTemplate = getAgreementTemplate(agreementCode);
		BeanUtils.copyProperties(updateAgreementTemplate, dbAgreementTemplate, CommonUtils.getNullPropertyNames(updateAgreementTemplate));
		dbAgreementTemplate.setUpdatedBy(loginUserID);
		dbAgreementTemplate.setUpdatedOn(new Date());
		return agreementTemplateRepository.save(dbAgreementTemplate);
	}
	
	/**
	 * deleteAgreementTemplate
	 * @param loginUserID 
	 * @param aggrementtemplateCode
	 */
	public void deleteAgreementTemplate (String agreementCode, String loginUserID) {
		AgreementTemplate agreementTemplate = getAgreementTemplate(agreementCode);
		if ( agreementTemplate != null) {
			agreementTemplate.setDeletionIndicator(1L);
			agreementTemplate.setUpdatedBy(loginUserID);
			agreementTemplateRepository.save(agreementTemplate);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + agreementCode);
		}
	}
}
