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
import com.mnrclara.api.setup.model.casesubcategory.AddCaseSubcategory;
import com.mnrclara.api.setup.model.casesubcategory.CaseSubcategory;
import com.mnrclara.api.setup.model.casesubcategory.UpdateCaseSubcategory;
import com.mnrclara.api.setup.repository.CaseSubcategoryRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaseSubcategoryService {
	
	@Autowired
	private CaseSubcategoryRepository caseSubcategoryRepository;
	
	public List<CaseSubcategory> getCaseSubcategories () {
		List<CaseSubcategory> caseSubcategoryList = caseSubcategoryRepository.findAll();
		return caseSubcategoryList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getCaseSubcategory
	 * @param casesubcategoryId
	 * @return
	 */
	public CaseSubcategory getCaseSubcategory (Long caseSubcategoryId) {
		CaseSubcategory caseSubcategory = caseSubcategoryRepository.findByCaseSubcategoryId(caseSubcategoryId);
		if (caseSubcategory.getDeletionIndicator() == 0) {
			return caseSubcategory;
		} else {
			throw new BadRequestException("The given CaseSubcategory ID : " + caseSubcategoryId + " doesn't exist.");
		}
	}
	
	/**
	 * createCaseSubcategory
	 * @param newCaseSubcategory
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CaseSubcategory createCaseSubcategory (AddCaseSubcategory newCaseSubcategory, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory dbCaseSubcategory = new CaseSubcategory();
		BeanUtils.copyProperties(newCaseSubcategory, dbCaseSubcategory, CommonUtils.getNullPropertyNames(newCaseSubcategory));
		dbCaseSubcategory.setDeletionIndicator(0L);
		dbCaseSubcategory.setCreatedBy(loginUserID);
		dbCaseSubcategory.setUpdatedBy(loginUserID);
		dbCaseSubcategory.setCreatedOn(new Date());
		dbCaseSubcategory.setUpdatedOn(new Date());
		return caseSubcategoryRepository.save(dbCaseSubcategory);
	}
	
	/**
	 * updateCaseSubcategory
	 * @param loginUserId 
	 * @param casesubcategoryId
	 * @param updateCaseSubcategory
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CaseSubcategory updateCaseSubcategory (String languageId, Long classId, Long caseCategoryId, 
			Long caseSubcategoryId, String loginUserID, UpdateCaseSubcategory updateCaseSubcategory) 
			throws IllegalAccessException, InvocationTargetException {
		CaseSubcategory dbCaseSubcategory = getCaseSubcategory (caseSubcategoryId);
		BeanUtils.copyProperties(updateCaseSubcategory, dbCaseSubcategory, CommonUtils.getNullPropertyNames(updateCaseSubcategory));
		dbCaseSubcategory.setUpdatedBy(loginUserID);
		dbCaseSubcategory.setUpdatedOn(new Date());
		return caseSubcategoryRepository.save(dbCaseSubcategory);
	}
	
	/**
	 * deleteCaseSubcategory
	 * @param loginUserID 
	 * @param casesubcategoryCode
	 */
	public void deleteCaseSubcategory (String languageId, Long classId, Long caseCategoryId, Long caseSubcategoryId, String loginUserID) {
		CaseSubcategory caseSubcategory = getCaseSubcategory (caseSubcategoryId);
		if ( caseSubcategory != null) {
			caseSubcategory.setDeletionIndicator(1L);
			caseSubcategory.setUpdatedBy(loginUserID);
			caseSubcategoryRepository.save(caseSubcategory);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: : " + caseSubcategoryId + 
					" doesn't exist.");
		}
	}
}
