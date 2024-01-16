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

import com.mnrclara.api.setup.model.activitycode.ActivityCode;
import com.mnrclara.api.setup.model.activitycode.AddActivityCode;
import com.mnrclara.api.setup.model.activitycode.UpdateActivityCode;
import com.mnrclara.api.setup.repository.ActivityCodeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityCodeService {
	
	@Autowired
	private ActivityCodeRepository activityCodeRepository;
	
	public List<ActivityCode> getActivityCodes () {
		List<ActivityCode> activityCodeList =  activityCodeRepository.findAll();
		activityCodeList = activityCodeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return activityCodeList;
	}
	
	/**
	 * getActivityCode
	 * @param activityCodeId
	 * @return
	 */
	public ActivityCode getActivityCode (String activityCodeId) {
		Optional<ActivityCode> activityCode = 
				activityCodeRepository.findByActivityCodeAndDeletionIndicator(activityCodeId, 0L);
//		if (activityCode.isEmpty()) {
//			throw new BadRequestException("The given ActivityCode ID : " + activityCodeId + " doesn't exist.");
//		}
		if (activityCode.isEmpty()) {
			return null;
		}
		return activityCode.get();
	}
	
	/**
	 * createActivityCode
	 * @param newActivityCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ActivityCode createActivityCode (AddActivityCode newActivityCode, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
//		Optional<ActivityCode> activitycode = 
//				activityCodeRepository.findByLanguageIdAndClassIdAndActivityCodeAndDeletionIndicator (
//						newActivityCode.getLanguageId(), 
//						newActivityCode.getClassId(), 
//						newActivityCode.getActivityCode(), 
//						0L);	
//		if (!activitycode.isEmpty()) {
//			throw new BadRequestException("Record is getting duplicated with the given values");
//		}
		ActivityCode dbActivityCode = new ActivityCode();
		BeanUtils.copyProperties(newActivityCode, dbActivityCode, CommonUtils.getNullPropertyNames(newActivityCode));
		dbActivityCode.setDeletionIndicator(0L);
		dbActivityCode.setCreatedBy(loginUserId);
		dbActivityCode.setUpdatedBy(loginUserId);
		dbActivityCode.setCreatedOn(new Date());
		dbActivityCode.setUpdatedOn(new Date());
		return activityCodeRepository.save(dbActivityCode);
	}
	
	/**
	 * updateActivityCode
	 * @param activitycodeId
	 * @param loginUserId 
	 * @param updateActivityCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ActivityCode updateActivityCode (String activitycodeId, String loginUserId, UpdateActivityCode updateActivityCode)
			throws IllegalAccessException, InvocationTargetException {
		ActivityCode dbActivityCode = getActivityCode(activitycodeId);
		BeanUtils.copyProperties(updateActivityCode, dbActivityCode, CommonUtils.getNullPropertyNames(updateActivityCode));
		dbActivityCode.setUpdatedBy(loginUserId);
		dbActivityCode.setUpdatedOn(new Date());
		return activityCodeRepository.save(dbActivityCode);
	}
	
	/**
	 * deleteActivityCode
	 * @param loginUserID 
	 * @param activitycodeCode
	 */
	public void deleteActivityCode (String activitycodeModuleId, String loginUserID) {
		ActivityCode activitycode = getActivityCode(activitycodeModuleId);
		if (activitycode != null) {
			activitycode.setDeletionIndicator(1L);
			activitycode.setUpdatedBy(loginUserID);
			activitycode.setUpdatedOn(new Date());
			activityCodeRepository.save(activitycode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + activitycodeModuleId);
		}
	}
}
