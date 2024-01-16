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
import com.mnrclara.api.setup.model.intakeform.AddIntakeFormID;
import com.mnrclara.api.setup.model.intakeform.IntakeFormID;
import com.mnrclara.api.setup.model.intakeform.UpdateIntakeFormID;
import com.mnrclara.api.setup.repository.IntakeFormRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IntakeFormService {
	
	@Autowired
	private IntakeFormRepository inTakeFormRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<IntakeFormID> getIntakeForms () {
		List<IntakeFormID> inTakeFormList =  inTakeFormRepository.findAll();
		inTakeFormList = inTakeFormList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return inTakeFormList;
	}
	
	/**
	 * getIntakeForm
	 * @param inTakeFormId
	 * @return
	 */
	public IntakeFormID getIntakeForm (Long inTakeFormId) {
		IntakeFormID inTakeForm = inTakeFormRepository.findByIntakeFormIdAndDeletionIndicator(inTakeFormId, 0L).orElse(null);
		if (inTakeForm != null) {
			return inTakeForm;
		} else {
			throw new BadRequestException("The given IntakeForm ID : " + inTakeFormId + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param classId
	 * @param clientTypeId
	 * @return
	 */
	public IntakeFormID getIntakeForm (Long classId, Long clientTypeId) {
		IntakeFormID inTakeForm = inTakeFormRepository.findByClassIdAndClientTypeId(classId, clientTypeId);
		if (inTakeForm != null && inTakeForm.getDeletionIndicator() == 0) {
			return inTakeForm;
		} else {
			throw new BadRequestException("No records found for the givem class ID : " + classId + " and ClientTypeID : " + clientTypeId);
		}
	}
	
	/**
	 * createIntakeForm
	 * @param newIntakeForm
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public IntakeFormID createIntakeForm (AddIntakeFormID newIntakeForm, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID dbIntakeForm = new IntakeFormID();
		BeanUtils.copyProperties(newIntakeForm, dbIntakeForm, CommonUtils.getNullPropertyNames(newIntakeForm));
		dbIntakeForm.setDeletionIndicator(0L);
		dbIntakeForm.setCreatedBy(loginUserID);
		dbIntakeForm.setUpdatedBy(loginUserID);
		dbIntakeForm.setCreatedOn(new Date());
		dbIntakeForm.setUpdatedOn(new Date());
		return inTakeFormRepository.save(dbIntakeForm);
	}
	
	/**
	 * updateIntakeForm
	 * @param intakeformId
	 * @param loginUserId 
	 * @param updateIntakeForm
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public IntakeFormID updateIntakeForm (Long intakeformId, String loginUserID, UpdateIntakeFormID updateIntakeForm) 
			throws IllegalAccessException, InvocationTargetException {
		IntakeFormID dbIntakeForm = getIntakeForm(intakeformId);
		BeanUtils.copyProperties(updateIntakeForm, dbIntakeForm, CommonUtils.getNullPropertyNames(updateIntakeForm));
		dbIntakeForm.setUpdatedBy(loginUserID);
		dbIntakeForm.setUpdatedOn(new Date());
		return inTakeFormRepository.save(dbIntakeForm);
	}
	
	/**
	 * deleteIntakeForm
	 * @param loginUserID 
	 * @param intakeformCode
	 */
	public void deleteIntakeForm (Long intakeformModuleId, String loginUserID) {
		IntakeFormID intakeform = getIntakeForm(intakeformModuleId);
		if ( intakeform != null) {
			intakeform.setDeletionIndicator(1L);
			intakeform.setUpdatedBy(loginUserID);
			inTakeFormRepository.save(intakeform);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + intakeformModuleId);
		}
	}
}
