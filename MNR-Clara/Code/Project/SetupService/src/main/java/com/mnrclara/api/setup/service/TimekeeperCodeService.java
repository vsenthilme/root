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
import com.mnrclara.api.setup.model.timekeepercode.AddTimekeeperCode;
import com.mnrclara.api.setup.model.timekeepercode.TimekeeperCode;
import com.mnrclara.api.setup.model.timekeepercode.UpdateTimekeeperCode;
import com.mnrclara.api.setup.repository.TimekeeperCodeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TimekeeperCodeService {
	
	@Autowired
	private TimekeeperCodeRepository timekeeperCodeRepository;
	
	public List<TimekeeperCode> getActivityCodes () {
		List<TimekeeperCode> timekeeperCodeList = timekeeperCodeRepository.findAll();
		return timekeeperCodeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getTimekeeperCode
	 * @param timekeeperCodeId
	 * @return
	 */
	public TimekeeperCode getTimekeeperCode (String timekeeperCodeId) {
		TimekeeperCode timekeeperCode = timekeeperCodeRepository.findByTimekeeperCode(timekeeperCodeId).orElse(null);
		if (timekeeperCode != null && timekeeperCode.getDeletionIndicator() != null && timekeeperCode.getDeletionIndicator() == 0) {
			return timekeeperCode;
		} else {
			throw new BadRequestException("The given TimekeeperCode ID : " + timekeeperCodeId + " doesn't exist.");
		}
	}
	
	/**
	 * createTimekeeperCode
	 * @param newTimekeeperCode
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TimekeeperCode createTimekeeperCode (AddTimekeeperCode newTimekeeperCode, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode timekeeperCode = timekeeperCodeRepository.findByTimekeeperCodeAndDeletionIndicator(newTimekeeperCode.getTimekeeperCode(), 0L);
		if (timekeeperCode != null) {
			throw new BadRequestException("Given Timekeepercode : " + newTimekeeperCode.getTimekeeperCode() + " already exists.");
		}
		TimekeeperCode dbTimekeeperCode = new TimekeeperCode();
		BeanUtils.copyProperties(newTimekeeperCode, dbTimekeeperCode, CommonUtils.getNullPropertyNames(newTimekeeperCode));
		dbTimekeeperCode.setDeletionIndicator(0L);
		dbTimekeeperCode.setCreatedBy(loginUserID);
		dbTimekeeperCode.setUpdatedBy(loginUserID);
		dbTimekeeperCode.setCreatedOn(new Date());
		dbTimekeeperCode.setUpdatedOn(new Date());
		return timekeeperCodeRepository.save(dbTimekeeperCode);
	}
	
	/**
	 * updateTimekeeperCode
	 * @param timekeeperCodeId
	 * @param loginUserId 
	 * @param updateTimekeeperCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TimekeeperCode updateTimekeeperCode (String timekeeperCode, String loginUserID, TimekeeperCode updateTimekeeperCode)
			throws IllegalAccessException, InvocationTargetException {
		TimekeeperCode dbTimekeeperCode = getTimekeeperCode(timekeeperCode);
		BeanUtils.copyProperties(updateTimekeeperCode, dbTimekeeperCode, CommonUtils.getNullPropertyNames(updateTimekeeperCode));
		dbTimekeeperCode.setUpdatedBy(loginUserID);
		dbTimekeeperCode.setUpdatedOn(new Date());
		return timekeeperCodeRepository.save(dbTimekeeperCode);
	}
	
	/**
	 * deleteTimekeeperCode
	 * @param timekeeperCodeId
	 * @param loginUserID 
	 */
	public void deleteTimekeeperCode (String timekeeperCode, String loginUserID) {
		TimekeeperCode objTimekeeperCode = getTimekeeperCode(timekeeperCode);
		if ( objTimekeeperCode != null) {
			objTimekeeperCode.setDeletionIndicator(1L);
			objTimekeeperCode.setUpdatedBy(loginUserID);
			timekeeperCodeRepository.save(objTimekeeperCode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + timekeeperCode);
		}
	}
}
