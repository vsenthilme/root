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
import com.mnrclara.api.setup.model.deadlinecalculator.AddDeadlineCalculator;
import com.mnrclara.api.setup.model.deadlinecalculator.DeadlineCalculator;
import com.mnrclara.api.setup.model.deadlinecalculator.UpdateDeadlineCalculator;
import com.mnrclara.api.setup.repository.DeadlineCalculatorRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeadlineCalculatorService {
	
	@Autowired
	private DeadlineCalculatorRepository deadlineCalculatorRepository;
	
	public List<DeadlineCalculator> getDeadlineCalculators () {
		List<DeadlineCalculator> deadlineCalculatorList =  deadlineCalculatorRepository.findAll();
		return deadlineCalculatorList.stream().filter(n -> n.getDeletionIndicator() != null && 
				n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getDeadlineCalculator
	 * @param deadlineCalculatorId
	 * @return
	 */
	public DeadlineCalculator getDeadlineCalculator (Long deadlineCalculationId) {
		DeadlineCalculator deadlineCalculator = deadlineCalculatorRepository.findById(deadlineCalculationId).orElse(null);
		if (deadlineCalculator != null && deadlineCalculator.getDeletionIndicator() != null && deadlineCalculator.getDeletionIndicator() == 0) {
			return deadlineCalculator;
		} else {
			throw new BadRequestException("The given DeadlineCalculator ID : " + deadlineCalculationId + " doesn't exist.");
		}
	}
	
	/**
	 * createDeadlineCalculator
	 * @param newDeadlineCalculator
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DeadlineCalculator createDeadlineCalculator (AddDeadlineCalculator newDeadlineCalculator, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator dbDeadlineCalculator = new DeadlineCalculator();
		BeanUtils.copyProperties(newDeadlineCalculator, dbDeadlineCalculator, CommonUtils.getNullPropertyNames(newDeadlineCalculator));
		
		// Generate PK ID value
		dbDeadlineCalculator.setDeadLineCalculationId(System.currentTimeMillis());
		dbDeadlineCalculator.setDeletionIndicator(0L);
		dbDeadlineCalculator.setCreatedBy(loginUserID);
		dbDeadlineCalculator.setUpdatedBy(loginUserID);
		dbDeadlineCalculator.setCreatedOn(new Date());
		dbDeadlineCalculator.setUpdatedOn(new Date());
		return deadlineCalculatorRepository.save(dbDeadlineCalculator);
	}
	
	/**
	 * updateDeadlineCalculator
	 * @param deadlineCalculatorId
	 * @param loginUserId 
	 * @param updateDeadlineCalculator
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DeadlineCalculator updateDeadlineCalculator (Long deadlineCalculationId, String loginUserID, UpdateDeadlineCalculator updateDeadlineCalculator) 
			throws IllegalAccessException, InvocationTargetException {
		DeadlineCalculator dbDeadlineCalculator = getDeadlineCalculator(deadlineCalculationId);
		BeanUtils.copyProperties(updateDeadlineCalculator, dbDeadlineCalculator, CommonUtils.getNullPropertyNames(updateDeadlineCalculator));
		log.info("dbDeadlineCalculator : " + dbDeadlineCalculator);
		dbDeadlineCalculator.setUpdatedBy(loginUserID);
		dbDeadlineCalculator.setUpdatedOn(new Date());
		return deadlineCalculatorRepository.save(dbDeadlineCalculator);
	}
	
	/**
	 * deleteDeadlineCalculator
	 * @param loginUserID 
	 * @param deadlineCalculatorCode
	 */
	public void deleteDeadlineCalculator (Long deadlineCalculationId, String loginUserID) {
		DeadlineCalculator deadlineCalculator = getDeadlineCalculator(deadlineCalculationId);
		if ( deadlineCalculator != null) {
			deadlineCalculator.setDeletionIndicator(1L);
			deadlineCalculator.setUpdatedBy(loginUserID);
			deadlineCalculatorRepository.save(deadlineCalculator);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + deadlineCalculationId);
		}
	}
}
