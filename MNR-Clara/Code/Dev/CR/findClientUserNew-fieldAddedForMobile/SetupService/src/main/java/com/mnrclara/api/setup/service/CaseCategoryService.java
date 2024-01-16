package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.casecategory.AddCaseCategory;
import com.mnrclara.api.setup.model.casecategory.CaseCategory;
import com.mnrclara.api.setup.model.casecategory.UpdateCaseCategory;
import com.mnrclara.api.setup.repository.CaseCategoryRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaseCategoryService {
	
	@Autowired
	private CaseCategoryRepository caseCategoryRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<CaseCategory> getCaseCategories () {
		List<CaseCategory> caseCategoryList =  caseCategoryRepository.findAll();
//		log.info("caseCategoryList : " + caseCategoryList.size());
//		List<CaseCategory> newCatList = new ArrayList<>();
//		for (CaseCategory caseCategory: caseCategoryList) {
//			log.info("caseCategory: " + caseCategory);
//			if (caseCategory != null && caseCategory.getDeletionIndicator() != null && caseCategory.getDeletionIndicator() == 0) {
//				newCatList.add(caseCategory);
//			}
//		}
		caseCategoryList = Optional.ofNullable(caseCategoryList)
			            .orElseGet(Collections::emptyList)
			            .stream()
			            .filter(Objects::nonNull)
			            .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
			            .collect(Collectors.toList());
							
		return caseCategoryList;
	}
	
	/**
	 * getCaseCategory
	 * @param caseCategoryId
	 * @return
	 */
	public CaseCategory getCaseCategory (Long caseCategoryId) {
		Optional<CaseCategory> caseCategory = caseCategoryRepository.findByCaseCategoryIdAndDeletionIndicator(caseCategoryId, 0L);
		if (!caseCategory.isEmpty()) {
			return caseCategory.get();
		} else {
			throw new BadRequestException("The given caseCategory ID : " + caseCategoryId + " doesn't exist.");
		}
	}
	
	/**
	 * getCaseCategoryByClassId
	 * @param classId
	 * @return
	 */
	public List<CaseCategory> getCaseCategoryByClassId (Long classId) {
		List<CaseCategory> caseCategoryList = caseCategoryRepository.findByClassIdAndCategoryStatus(classId, "ACTIVE");
		return caseCategoryList;
	}
	
	/**
	 * createCaseCategory
	 * @param newCaseCategory
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CaseCategory createCaseCategory (AddCaseCategory newCaseCategory, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory dbCaseCategory = new CaseCategory();
		BeanUtils.copyProperties(newCaseCategory, dbCaseCategory, CommonUtils.getNullPropertyNames(newCaseCategory));
		dbCaseCategory.setDeletionIndicator(0L);
		dbCaseCategory.setCreatedBy(loginUserID);
		dbCaseCategory.setUpdatedBy(loginUserID);
		dbCaseCategory.setCreatedOn(new Date());
		dbCaseCategory.setUpdatedOn(new Date());
		return caseCategoryRepository.save(dbCaseCategory);
	}
	
	/**
	 * updateCaseCategory
	 * @param caseCategoryId
	 * @param loginUserId 
	 * @param updateCaseCategory
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CaseCategory updateCaseCategory (Long caseCategoryId, String loginUserID, UpdateCaseCategory updateCaseCategory) 
			throws IllegalAccessException, InvocationTargetException {
		CaseCategory dbCaseCategory = getCaseCategory(caseCategoryId);
		BeanUtils.copyProperties(updateCaseCategory, dbCaseCategory, CommonUtils.getNullPropertyNames(updateCaseCategory));
		dbCaseCategory.setUpdatedBy(loginUserID);
		dbCaseCategory.setUpdatedOn(new Date());
		return caseCategoryRepository.save(dbCaseCategory);
	}
	
	/**
	 * deleteCaseCategory
	 * @param loginUserID 
	 * @param caseCategoryCode
	 */
	public void deleteCaseCategory (Long caseCategoryId, String loginUserID) {
		CaseCategory caseCategory = getCaseCategory(caseCategoryId);
		if ( caseCategory != null) {
			caseCategory.setDeletionIndicator(1L);
			caseCategory.setUpdatedBy(loginUserID);
			caseCategoryRepository.save(caseCategory);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + caseCategoryId);
		}
	}
}
