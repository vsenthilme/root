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
import com.mnrclara.api.setup.model.usertype.AddUserType;
import com.mnrclara.api.setup.model.usertype.UpdateUserType;
import com.mnrclara.api.setup.model.usertype.UserType;
import com.mnrclara.api.setup.repository.UserTypeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserTypeService {
	
	@Autowired
	private UserTypeRepository userTypeRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<UserType> getUserTypes () {
		List<UserType> userTypeList =  userTypeRepository.findAll();
		userTypeList = userTypeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return userTypeList;
	}
	
	/**
	 * getUserType
	 * @param userTypeId
	 * @param classId 
	 * @param languageId 
	 * @return
	 */
	public UserType getUserType (Long userTypeId, String languageId, Long classId) {
		Optional<UserType> optUsertype = 
				userTypeRepository.findByLanguageIdAndClassIdAndUserTypeIdAndDeletionIndicator (
						languageId, classId, userTypeId, 0L);
		if (optUsertype.isEmpty()) {
			throw new BadRequestException("The given UserType ID : " + userTypeId + ", classId: " + 
							classId + ", languageId : " + languageId + " doesn't exist.");
		} 
		return optUsertype.get();
	}
	
	/**
	 * 
	 * @param userTypeId
	 * @return
	 */
	public UserType getUserType (Long userTypeId) {
		Optional<UserType> optUsertype = 
				userTypeRepository.findByUserTypeIdAndDeletionIndicator (userTypeId, 0L);
		if (optUsertype.isEmpty()) {
			throw new BadRequestException("The given UserType ID : " + userTypeId + " doesn't exist.");
		} 
		return optUsertype.get();
	}
	
	/**
	 * createUserType
	 * @param newUserType
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserType createUserType (AddUserType newUserType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		UserType dbUserType = new UserType();
		BeanUtils.copyProperties(newUserType, dbUserType, CommonUtils.getNullPropertyNames(newUserType));
		dbUserType.setDeletionIndicator(0L);
		dbUserType.setCreatedBy(loginUserID);
		dbUserType.setUpdatedBy(loginUserID);
		dbUserType.setCreatedOn(new Date());
		dbUserType.setUpdatedOn(new Date());
		return userTypeRepository.save(dbUserType);
	}
	
	/**
	 * updateUserType
	 * @param usertypeId
	 * @param loginUserId 
	 * @param updateUserType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserType updateUserType (Long usertypeId, String loginUserId, UpdateUserType updateUserType) 
			throws IllegalAccessException, InvocationTargetException {
		UserType dbUserType = getUserType(usertypeId);
		BeanUtils.copyProperties(updateUserType, dbUserType, CommonUtils.getNullPropertyNames(updateUserType));
		dbUserType.setUpdatedBy(loginUserId);
		dbUserType.setUpdatedOn(new Date());
		return userTypeRepository.save(dbUserType);
	}
	
	/**
	 * deleteUserType
	 * @param loginUserID 
	 * @param loginUserID2 
	 * @param classId 
	 * @param usertypeCode
	 */
	public void deleteUserType (Long usertypeId, String loginUserID) {
		UserType usertype = getUserType(usertypeId);
		if ( usertype != null) {
			usertype.setDeletionIndicator(1L);
			usertype.setUpdatedBy(loginUserID);
			userTypeRepository.save(usertype);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + usertypeId);
		}
	}
}
