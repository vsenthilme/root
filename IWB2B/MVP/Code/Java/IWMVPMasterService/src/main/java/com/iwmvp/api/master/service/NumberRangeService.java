package com.iwmvp.api.master.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.iwmvp.api.master.repository.Specification.NumberRangeSpecification;
import com.iwmvp.api.master.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwmvp.api.master.controller.exception.BadRequestException;
import com.iwmvp.api.master.model.numberrange.*;
import com.iwmvp.api.master.repository.NumberRangeRepository;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityNotFoundException;
@Slf4j
@Service
public class NumberRangeService extends BaseService {
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
	 * @param numberRangeObject
	 * @return
	 */
	public NumberRange getNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId){
		Optional<NumberRange> dbNumberRange=
				numberRangeRepository.findByCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndLanguageIdAndDeletionIndicator(
						companyId,
						numberRangeCode,
						numberRangeObject,
						languageId,
						0l
				);
		if (dbNumberRange.isEmpty()) {
			throw new BadRequestException("The given values : " +
					"NumberRangeCode - " + numberRangeCode +
					"NumberRangeObject - "+ numberRangeObject +
					"doesn't exist.");
		}
		return dbNumberRange.get();
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
		NumberRange dbNumberRange= new NumberRange();
		BeanUtils.copyProperties(newNumberRange, dbNumberRange, CommonUtils.getNullPropertyNames(newNumberRange));
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
	 * @param numberRangeObject
	 * @param loginUserID
	 * @param updateNumberRange
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NumberRange updateNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId, String loginUserID,
										   UpdateNumberRange updateNumberRange)
			throws IllegalAccessException, InvocationTargetException {
		NumberRange dbNumberRange = getNumberRange(numberRangeCode,numberRangeObject,companyId,languageId);
		BeanUtils.copyProperties(updateNumberRange, dbNumberRange, CommonUtils.getNullPropertyNames(updateNumberRange));
		dbNumberRange.setUpdatedBy(loginUserID);
		dbNumberRange.setUpdatedOn(new Date());
		return numberRangeRepository.save(dbNumberRange);
	}
	/**
	 * deleteLoyaltySetup
	 * @param numberRangeCode
	 * @param numberRangeObject
	 * @param loginUserID
	 */
	public void deleteNumberRange(Long numberRangeCode, String numberRangeObject,String companyId,String languageId,String loginUserID) {
		NumberRange dbNumberRange = getNumberRange(numberRangeCode,numberRangeObject,companyId,languageId);
		if ( dbNumberRange != null) {
			dbNumberRange.setDeletionIndicator(1L);
			dbNumberRange.setUpdatedBy(loginUserID);
			numberRangeRepository.save(dbNumberRange);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + numberRangeCode);
		}
	}
	//Find NumberRange
	public List<NumberRange> findNumberRange(FindNumberRange findNumberRange) throws ParseException {

		NumberRangeSpecification spec = new NumberRangeSpecification(findNumberRange);
		List<NumberRange> results = numberRangeRepository.findAll(spec);
		return results;
	}
}
