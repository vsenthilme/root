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

import com.iweb2b.api.master.model.usermanagement.AddUserManagement;
import com.iweb2b.api.master.model.usermanagement.UpdateUserManagement;
import com.iweb2b.api.master.model.usermanagement.UserManagement;
import com.iweb2b.api.master.repository.UserManagementRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserManagementService {
	
	@Autowired
	private UserManagementRepository userManagementRepository;
	
	public List<UserManagement> getUserManagement () {
		List<UserManagement> userManagementList =  userManagementRepository.findAll();
		userManagementList = userManagementList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return userManagementList;
	}
	
	/**
	 * getUserManagement
	 * @param userManagementId
	 * @return
	 */
	public UserManagement getUserManagement (String userManagementId) {
		Optional<UserManagement> userManagement = userManagementRepository.findByUserIdAndDeletionIndicator(userManagementId, 0L);
		if (userManagement.isEmpty()) {
			return null;
		}
		return userManagement.get();
	}
	
	/**
	 * createUserManagement
	 * @param newUserManagement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserManagement createUserManagement (AddUserManagement newUserManagement, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		UserManagement dbUserManagement = new UserManagement();
		BeanUtils.copyProperties(newUserManagement, dbUserManagement, CommonUtils.getNullPropertyNames(newUserManagement));
		dbUserManagement.setDeletionIndicator(0L);
		dbUserManagement.setCreatedBy(loginUserId);
		dbUserManagement.setUpdatedBy(loginUserId);
		dbUserManagement.setCreatedOn(new Date());
		dbUserManagement.setUpdatedOn(new Date());
		return userManagementRepository.save(dbUserManagement);
	}
	
	/**
	 * updateUserManagement
	 * @param userManagementId
	 * @param loginUserId 
	 * @param updateUserManagement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserManagement updateUserManagement (String userManagementId, String loginUserId, UpdateUserManagement updateUserManagement)
			throws IllegalAccessException, InvocationTargetException {
		UserManagement dbUserManagement = getUserManagement(userManagementId);
		BeanUtils.copyProperties(updateUserManagement, dbUserManagement, CommonUtils.getNullPropertyNames(updateUserManagement));
		dbUserManagement.setUpdatedBy(loginUserId);
		dbUserManagement.setUpdatedOn(new Date());
		return userManagementRepository.save(dbUserManagement);
	}
	
	/**
	 * deleteUserManagement
	 * @param loginUserID 
	 * @param userManagementId
	 */
	public void deleteUserManagement (String userManagementId, String loginUserID) {
		UserManagement usermanagement = getUserManagement(userManagementId);
		if (usermanagement != null) {
			usermanagement.setDeletionIndicator(1L);
			usermanagement.setUpdatedBy(loginUserID);
			usermanagement.setUpdatedOn(new Date());
			userManagementRepository.save(usermanagement);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userManagementId);
		}
	}
}
