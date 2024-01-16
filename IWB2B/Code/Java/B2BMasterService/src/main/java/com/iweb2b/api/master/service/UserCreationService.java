package com.iweb2b.api.master.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.master.model.usercreation.AddUserCreation;
import com.iweb2b.api.master.model.usercreation.UpdateUserCreation;
import com.iweb2b.api.master.model.usercreation.UserCreation;
import com.iweb2b.api.master.repository.UserCreationRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserCreationService {
	
	@Autowired
	private UserCreationRepository userCreationRepository;
	
	public List<UserCreation> getUserCreation () {
		List<UserCreation> userCreationList =  userCreationRepository.findAll();
		userCreationList = userCreationList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return userCreationList;
	}
	
	/**
	 * getUserCreation
	 * @param userCreationId
	 * @return
	 */
	public UserCreation getUserCreation (String userCreationId) {
		Optional<UserCreation> userCreation = userCreationRepository.findByUserIdAndDeletionIndicator(userCreationId, 0L);
		if (userCreation.isEmpty()) {
			return null;
		}
		return userCreation.get();
	}
	
	/**
	 * createUserCreation
	 * @param newUserCreation
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserCreation createUserCreation (AddUserCreation newUserCreation, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		UserCreation dbUserCreation = new UserCreation();
		BeanUtils.copyProperties(newUserCreation, dbUserCreation, CommonUtils.getNullPropertyNames(newUserCreation));
		dbUserCreation.setDeletionIndicator(0L);
		dbUserCreation.setCreatedBy(loginUserId);
		dbUserCreation.setUpdatedBy(loginUserId);
		dbUserCreation.setCreatedOn(new Date());
		dbUserCreation.setUpdatedOn(new Date());
		return userCreationRepository.save(dbUserCreation);
	}
	
	/**
	 * updateUserCreation
	 * @param usercreationId
	 * @param loginUserId 
	 * @param updateUserCreation
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserCreation updateUserCreation (String usercreationId, String loginUserId, UpdateUserCreation updateUserCreation)
			throws IllegalAccessException, InvocationTargetException {
		UserCreation dbUserCreation = getUserCreation(usercreationId);
		BeanUtils.copyProperties(updateUserCreation, dbUserCreation, CommonUtils.getNullPropertyNames(updateUserCreation));
		dbUserCreation.setUpdatedBy(loginUserId);
		dbUserCreation.setUpdatedOn(new Date());
		return userCreationRepository.save(dbUserCreation);
	}
	
	/**
	 * deleteUserCreation
	 * @param loginUserID 
	 * @param usercreationCode
	 */
	public void deleteUserCreation (String usercreationModuleId, String loginUserID) {
		UserCreation usercreation = getUserCreation(usercreationModuleId);
		if (usercreation != null) {
			usercreation.setDeletionIndicator(1L);
			usercreation.setUpdatedBy(loginUserID);
			usercreation.setUpdatedOn(new Date());
			userCreationRepository.save(usercreation);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + usercreationModuleId);
		}
	}
}
