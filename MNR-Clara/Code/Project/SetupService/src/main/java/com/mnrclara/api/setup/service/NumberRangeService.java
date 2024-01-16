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
import com.mnrclara.api.setup.exception.DataNotFoundException;
import com.mnrclara.api.setup.model.numberange.AddNumberRange;
import com.mnrclara.api.setup.model.numberange.NumberRange;
import com.mnrclara.api.setup.model.numberange.UpdateNumberRange;
import com.mnrclara.api.setup.repository.NumberRangeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NumberRangeService {

	@Autowired
	NumberRangeRepository numberRangeRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<NumberRange> getNumberRanges () {
		List<NumberRange> numberRangeList = numberRangeRepository.findAll();
		return numberRangeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param numberRangeCode
	 * @return
	 */
	public NumberRange getNumberRange (Long numberRangeCode) {
		NumberRange numberRange = numberRangeRepository.findByNumberRangeCode(numberRangeCode).orElse(null);
		if (numberRange.getDeletionIndicator() == 0) {
			return numberRange;
		} else {
			throw new BadRequestException("The given ActivityCode ID : " + numberRangeCode + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param newNumberRange
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NumberRange createNumberRange (AddNumberRange newNumberRange, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<NumberRange> numberRange = 
				numberRangeRepository.findByLanguageIdAndClassIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator (
					newNumberRange.getLanguageId(),
					newNumberRange.getClassId(),
					newNumberRange.getNumberRangeCode(),
					newNumberRange.getNumberRangeObject(),
					0L);
		if (!numberRange.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		NumberRange dbNumberRange = new NumberRange();
		BeanUtils.copyProperties(newNumberRange, dbNumberRange);
		dbNumberRange.setDeletionIndicator(0L);
		dbNumberRange.setCreatedBy(loginUserID);
		dbNumberRange.setUpdatedBy(loginUserID);
		dbNumberRange.setCreatedOn(new Date());
		dbNumberRange.setUpdatedOn(new Date());
		return numberRangeRepository.save(dbNumberRange);
	}
	
	/**
	 * 
	 * @param numberRangeCode
	 * @param loginUserID
	 * @param updateNumberRange
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NumberRange updateNumberRange (Long numberRangeCode, String loginUserID, UpdateNumberRange updateNumberRange) 
			throws IllegalAccessException, InvocationTargetException {
		NumberRange dbNumberRange = getNumberRange(numberRangeCode);
		BeanUtils.copyProperties(updateNumberRange, dbNumberRange, CommonUtils.getNullPropertyNames(updateNumberRange));
		dbNumberRange.setUpdatedBy(loginUserID);
		dbNumberRange.setUpdatedOn(new Date());
		return numberRangeRepository.save(dbNumberRange);
	}
	
	/**
	 * 
	 * @param numberRangeModuleId
	 * @param loginUserID
	 */
	public void deleteNumberRange (Long numberRangeModuleId) {
		NumberRange numberRange = getNumberRange(numberRangeModuleId);
		if (numberRange != null) {
			numberRangeRepository.delete(numberRange);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + numberRangeModuleId);
		}
	}
	
	/**
	 * 
	 * @param numberRangeCode
	 * @param classId
	 * @return
	 */
	public String getNextNumberRange (Long numberRangeCode, Long classId) {
		log.info("numberRangeCode : " + numberRangeCode + ":" + classId);
		Optional<NumberRange> numberRangeOpt = numberRangeRepository.findByNumberRangeCodeAndClassId(numberRangeCode, classId);
		if (numberRangeOpt.isEmpty()) {
			throw new DataNotFoundException("Record not found for given Range Code and Class: " + numberRangeCode + "," + classId);
		}
		NumberRange currentNumberRange = numberRangeOpt.get();
		
		Optional<NumberRange> optNumberRange = 
				numberRangeRepository.findByLanguageIdAndClassIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator (
						currentNumberRange.getLanguageId(),
						classId,
						numberRangeCode,
						currentNumberRange.getNumberRangeObject(),
					0L);
		NumberRange numberRange = optNumberRange.get();
		log.info("Current record: " + numberRange);
		
		String strCurrentValue = numberRange.getNumberRangeCurrent();
		Long currentValue = 0L;
		if (strCurrentValue.startsWith("AL")) { // Increment logic for AuditLog Insert
			strCurrentValue = strCurrentValue.substring(2); // AL1000002
			currentValue = Long.valueOf(strCurrentValue);
			currentValue ++;
			strCurrentValue = "AL" + String.valueOf(currentValue);
			numberRange.setNumberRangeCurrent(strCurrentValue);
			log.info("currentValue of AL: " + currentValue);
		} else {
			currentValue = Long.valueOf(strCurrentValue);
			currentValue ++;
			log.info("currentValue : " + currentValue);
			numberRange.setNumberRangeCurrent(String.valueOf(currentValue));
			strCurrentValue = String.valueOf(currentValue);
		}
		log.info("New value numberRange: " + numberRange);
		numberRangeRepository.save(numberRange);
		log.info("New value has been updated in NumberRangeCurrent column");
		return strCurrentValue;
	}
}
