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

import com.iweb2b.api.master.model.userprofiles.AddUserProfiles;
import com.iweb2b.api.master.model.userprofiles.UpdateUserProfiles;
import com.iweb2b.api.master.model.userprofiles.UserProfiles;
import com.iweb2b.api.master.repository.UserProfilesRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserProfilesService {
	
	@Autowired
	private UserProfilesRepository userProfilesRepository;
	
	public List<UserProfiles> getUserProfiles () {
		List<UserProfiles> userProfilesList =  userProfilesRepository.findAll();
		userProfilesList = userProfilesList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return userProfilesList;
	}
	
	/**
	 * getUserProfiles
	 * @param userProfilesId
	 * @return
	 */
	public UserProfiles getUserProfiles (String userProfilesId) {
		Optional<UserProfiles> userProfiles = userProfilesRepository.findByUserIdAndDeletionIndicator(userProfilesId, 0L);
		if (userProfiles.isEmpty()) {
			return null;
		}
		return userProfiles.get();
	}
	
	/**
	 * createUserProfiles
	 * @param newUserProfiles
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserProfiles createUserProfiles (AddUserProfiles newUserProfiles, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		UserProfiles dbUserProfiles = new UserProfiles();
		BeanUtils.copyProperties(newUserProfiles, dbUserProfiles, CommonUtils.getNullPropertyNames(newUserProfiles));
		dbUserProfiles.setDeletionIndicator(0L);
		dbUserProfiles.setCreatedBy(loginUserId);
		dbUserProfiles.setUpdatedBy(loginUserId);
		dbUserProfiles.setCreatedOn(new Date());
		dbUserProfiles.setUpdatedOn(new Date());
		return userProfilesRepository.save(dbUserProfiles);
	}
	
	/**
	 * updateUserProfiles
	 * @param userprofilesId
	 * @param loginUserId 
	 * @param updateUserProfiles
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserProfiles updateUserProfiles (String userprofilesId, String loginUserId, UpdateUserProfiles updateUserProfiles)
			throws IllegalAccessException, InvocationTargetException {
		UserProfiles dbUserProfiles = getUserProfiles(userprofilesId);
		BeanUtils.copyProperties(updateUserProfiles, dbUserProfiles, CommonUtils.getNullPropertyNames(updateUserProfiles));
		dbUserProfiles.setUpdatedBy(loginUserId);
		dbUserProfiles.setUpdatedOn(new Date());
		return userProfilesRepository.save(dbUserProfiles);
	}
	
	/**
	 * deleteUserProfiles
	 * @param loginUserID 
	 * @param userprofilesCode
	 */
	public void deleteUserProfiles (String userId, String loginUserID) {
		UserProfiles userprofiles = getUserProfiles(userId);
		if (userprofiles != null) {
			userprofiles.setDeletionIndicator(1L);
			userprofiles.setUpdatedBy(loginUserID);
			userprofiles.setUpdatedOn(new Date());
			userProfilesRepository.save(userprofiles);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userId);
		}
	}
}
