package com.iweb2b.api.integration.service;

import com.iweb2b.api.integration.controller.exception.BadRequestException;
import com.iweb2b.api.integration.model.usermanagement.*;
import com.iweb2b.api.integration.repository.*;
import com.iweb2b.api.integration.repository.Specification.UserAccessSpecification;
import com.iweb2b.api.integration.util.CommonUtils;
import com.iweb2b.api.integration.util.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAccessService {

	@Autowired
	private UserAccessRepository userAccessRepository;
	
	private PasswordEncoder passwordEncoder = new PasswordEncoder();

	/**
	 *
	 * @return
	 */
	public List<UserAccess> getUserAccesss () {
		List<UserAccess> userManagementList = userAccessRepository.findAll();
		userManagementList = userManagementList.stream().filter(a -> a.getDeletionIndicator() != null &&
				a.getDeletionIndicator().longValue() == 0).collect(Collectors.toList());

		return userManagementList;
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	public UserAccess getUserAccess (String languageId, String companyCode, String userId) {
		UserAccess userManagement = userAccessRepository.
				findByLanguageIdAndCompanyCodeAndUserIdAndDeletionIndicator(
						languageId,
						companyCode,
						userId,
						0L);
		if (userManagement == null) {
			throw new BadRequestException("Invalid Username : " + userId);
		}


		return userManagement;
	}

	/**
	 *
	 * @param userId
	 * @param
	 * @return
	 */
	public UserAccess getUserAccess(String userId) {
		Optional<UserAccess> userManagement =
				userAccessRepository.findByUserIdAndDeletionIndicator(userId, 0L);
		if (userManagement == null) {
			throw new BadRequestException("Invalid Username : " + userId);
		}
		return userManagement.get();
	}

	/**
	 *
	 * @param userId
	 * @param loginPassword
	 * @return
	 */
	public UserAccess validateUser (String userId, String loginPassword) {

		Optional<UserAccess> userManagement = userAccessRepository.findByUserIdAndDeletionIndicator(userId, 0L);

		if (userManagement.isEmpty()) {
			throw new BadRequestException("Invalid Username : " + userId);
		}

		boolean isSuccess = false;
		isSuccess = passwordEncoder.matches(loginPassword, userManagement.get().getPassword());

		if (!isSuccess) {
			throw new BadRequestException("Password is wrong. Please enter correct password.");
		}

		return userManagement.get();
	}

	/**
	 * createUserAccess
	 * @param newUserAccess
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserAccess createUserAccess (AddUserAccess newUserAccess, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		UserAccess dbUserAccess = new UserAccess();
		UserAccess duplicateCheck = userAccessRepository.findByLanguageIdAndCompanyCodeAndUserIdAndDeletionIndicator(
				"EN",
				"1000",
				newUserAccess.getUserId(),
				0L);

		if (duplicateCheck != null) {

			throw new IllegalAccessException("User is getting Duplicated");

		} else {

			BeanUtils.copyProperties(newUserAccess, dbUserAccess, CommonUtils.getNullPropertyNames(newUserAccess));

			try {
				// Password encryption
				String encodedPwd = passwordEncoder.encodePassword(newUserAccess.getPassword());

				//hard code
				dbUserAccess.setLanguageId("EN");
				dbUserAccess.setCompanyCode("1000");

				dbUserAccess.setPassword(encodedPwd);
				dbUserAccess.setCreatedBy(loginUserID);
				dbUserAccess.setCreatedOn(new Date());
				dbUserAccess.setUpdatedBy(loginUserID);
				dbUserAccess.setUpdatedOn(new Date());
				dbUserAccess.setDeletionIndicator(0L);
				return userAccessRepository.save(dbUserAccess);

			} catch (Exception e) {
				log.error("Error : " + e);
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 *
	 * @param userId
	 * @param updateUserAccess
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public UserAccess updateUserAccess (String userId, UpdateUserAccess updateUserAccess, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		UserAccess dbUserAccess = userAccessRepository.
				findByLanguageIdAndCompanyCodeAndUserIdAndDeletionIndicator(
						"EN",
						"1000",
						userId,
						0L);
		BeanUtils.copyProperties(updateUserAccess, dbUserAccess, CommonUtils.getNullPropertyNames(updateUserAccess));

		if (updateUserAccess.getPassword() != null) {
			// Password encryption
			String encodedPwd = passwordEncoder.encodePassword(updateUserAccess.getPassword());
			dbUserAccess.setPassword(encodedPwd);
		}

		dbUserAccess.setUpdatedBy(loginUserID);
		dbUserAccess.setUpdatedOn(new Date());
		return userAccessRepository.save(dbUserAccess);
	}

	/**
	 *
	 * @param userId
	 * @param loginUserID
	 */
	public void deleteUserAccess (String userId,
									  String loginUserID) {
		UserAccess dbUserAccess = getUserAccess(userId);

		if ( dbUserAccess != null) {
			dbUserAccess.setUpdatedBy(loginUserID);
			dbUserAccess.setUpdatedOn(new Date());
			dbUserAccess.setDeletionIndicator(1L);
			userAccessRepository.save(dbUserAccess);

		} else {

			throw new EntityNotFoundException("Error in deleting Id: " + userId);
		}
	}

	//Find UserAccess
	public List<UserAccess> findUserAccess(FindUserAccess findUserAccess) throws ParseException {

		UserAccessSpecification spec = new UserAccessSpecification(findUserAccess);
		List<UserAccess> results = userAccessRepository.findAll(spec);

		return results;
	}

}
