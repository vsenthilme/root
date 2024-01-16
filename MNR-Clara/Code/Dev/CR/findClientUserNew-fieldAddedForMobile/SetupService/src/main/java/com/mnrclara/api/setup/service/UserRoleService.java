package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.userrole.AddUserRole;
import com.mnrclara.api.setup.model.userrole.UpdateUserRole;
import com.mnrclara.api.setup.model.userrole.UserRole;
import com.mnrclara.api.setup.repository.UserRoleRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRoleService {
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	NumberRangeService numberRangeService;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<UserRole> getUserRoles () {
		List<UserRole> userRoleList = userRoleRepository.findAll();
		return userRoleList.stream().filter(n -> n.getDeletionIndicator() != null && 
											n.getDeletionIndicator() == 0L)
									.collect(Collectors.toList());
	}
	
	/**
	 * getUserRole
	 * @param userRoleId
	 * @return
	 */
	public List<UserRole> getUserRole (Long userRoleId) {
		List<UserRole> userRoleList = userRoleRepository.findByUserRoleIdAndDeletionIndicator(userRoleId, 0L);
		if (!userRoleList.isEmpty()) {
			return userRoleList;
		}
		throw new BadRequestException("The given ActivityCode ID : " + userRoleId + " doesn't exist.");
	}
	
	/**
	 * createUserRole
	 * @param newUserRole
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<UserRole> createUserRole (List<AddUserRole> newUserRole, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<UserRole> createdUserRoleList = new ArrayList<>();
		String USER_ROLE_ID = numberRangeService.getNextNumberRange(99L, 3L);
		log.info("USER_ROLE_ID : " + USER_ROLE_ID);
		
		for (AddUserRole addUserRole : newUserRole) {
			UserRole dbUserRole = new UserRole();
			BeanUtils.copyProperties(addUserRole, dbUserRole, CommonUtils.getNullPropertyNames(addUserRole));
			dbUserRole.setUserRoleId(Long.valueOf(USER_ROLE_ID));
			dbUserRole.setDeletionIndicator(0L);
			dbUserRole.setCreatedBy(loginUserID);
			dbUserRole.setUpdatedBy(loginUserID);
			dbUserRole.setCreatedOn(new Date());
			dbUserRole.setUpdatedOn(new Date());
			log.info("dbUserRole : " + dbUserRole);
			createdUserRoleList.add(dbUserRole);
		}
		
		if (!createdUserRoleList.isEmpty()) {
			List<UserRole> createdUserRole = userRoleRepository.saveAll(createdUserRoleList);
			log.info("UserRole created: " + createdUserRole);
		}
		
		return createdUserRoleList;
	}
	
	/**
	 * updateUserRole
	 * @param userRoleId
	 * @param loginUserId 
	 * @param updateUserRole
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<UserRole> updateUserRole (Long userRoleId, String loginUserID, List<UpdateUserRole> updateUserRoles) 
			throws IllegalAccessException, InvocationTargetException {
		List<UserRole> responseUserRoleList = new ArrayList<>();
		List<UserRole> dbUserRoleList = getUserRole(userRoleId);
		if (!dbUserRoleList.isEmpty()) {
			for (UserRole userRole : dbUserRoleList) {
				for (UpdateUserRole updateUserRole : updateUserRoles) {
					if (userRole.getScreenId().equals(updateUserRole.getScreenId()) &&
							userRole.getSubScreenId().equals(updateUserRole.getSubScreenId())) {
						BeanUtils.copyProperties(updateUserRole, userRole, CommonUtils.getNullPropertyNames(updateUserRole));
						userRole.setUpdatedBy(loginUserID);
						userRole.setUpdatedOn(new Date());
						UserRole updatedUserRole = userRoleRepository.save(userRole);
						log.info("updateUserRole : " + updatedUserRole);
						responseUserRoleList.add(updatedUserRole);
					}
				}
			}
		}
		return responseUserRoleList;
	}
	
	/**
	 * deleteUserRole
	 * @param loginUserID 
	 * @param userRoleCode
	 */
	public void deleteUserRole (Long userRoleId, String loginUserID) {
		List<UserRole> userRoles = getUserRole(userRoleId);
		if (!userRoles.isEmpty()) {
			for (UserRole userRole : userRoles) {
				userRole.setDeletionIndicator(1L);
				userRole.setUpdatedBy(loginUserID);
				userRoleRepository.save(userRole);
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userRoleId);
		}
	}
}
