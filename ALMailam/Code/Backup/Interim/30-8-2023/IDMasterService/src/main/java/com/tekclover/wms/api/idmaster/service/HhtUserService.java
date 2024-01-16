package com.tekclover.wms.api.idmaster.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.idmaster.repository.Specification.HhtUserSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.idmaster.controller.exception.BadRequestException;
import com.tekclover.wms.api.idmaster.model.hhtuser.*;
import com.tekclover.wms.api.idmaster.model.hhtuser.HhtUser;
import com.tekclover.wms.api.idmaster.model.hhtuser.UpdateHhtUser;
import com.tekclover.wms.api.idmaster.repository.HhtUserRepository;
import com.tekclover.wms.api.idmaster.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HhtUserService {

	@Autowired
	private HhtUserRepository hhtUserRepository;

	/**
	 * getHhtUsers
	 * @return
	 */
	public List<HhtUser> getHhtUsers () {
		List<HhtUser> hhtUserList =  hhtUserRepository.findAll();
		hhtUserList = hhtUserList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return hhtUserList;
	}

	/**
	 * getHhtUser
	 * @param userId
	 * @return
	 */
	public HhtUser getHhtUser (String userId, String warehouseId,String companyCodeId,String plantId,String languageId) {
		Optional<HhtUser> hhtUser = hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
				userId,
				warehouseId,
				companyCodeId,
				plantId,
				languageId,
				0L);
		if(hhtUser.isEmpty()){
			throw new BadRequestException("The given values : " +
					"warehouseId - " + warehouseId +
					"companyCodeId - " + companyCodeId +
					"userId - "+userId+
					" doesn't exist.");
		}
		return hhtUser.get();
	}

	/**
	 *
	 * @param warehouseId
	 * @return
	 */
	public List<HhtUser> getHhtUser (String warehouseId) {
		List<HhtUser> hhtUser = hhtUserRepository.findByWarehouseIdAndDeletionIndicator(warehouseId, 0L);
		if (hhtUser != null) {
			return hhtUser;
		} else {
			throw new BadRequestException("The given warehouseId ID : " + warehouseId + " doesn't exist.");
		}
	}

	/**
	 *
	 * @param searchHhtUser
	 * @return
	 * @throws ParseException
	 */
//	public List<HhtUser> findHhtUser(SearchHhtUser searchHhtUser)
//			throws ParseException {
//		HhtUserSpecification spec = new HhtUserSpecification(searchHhtUser);
//		List<HhtUser> results = hhtUserRepository.findAll(spec);
//		log.info("results: " + results);
//		return results;
//	}

	/**
	 * createHhtUser
	 * @param newHhtUser
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HhtUser createHhtUser (AddHhtUser newHhtUser, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		HhtUser dbHhtUser = new HhtUser();
		Optional<HhtUser> duplicateHhtUser=hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
				newHhtUser.getUserId(),
				newHhtUser.getWarehouseId(),
				newHhtUser.getCompanyCodeId(),
				newHhtUser.getPlantId(),
				newHhtUser.getLanguageId(),
				0L
		);
		if(!duplicateHhtUser.isEmpty()){
			throw new IllegalAccessException("User is getting Duplicated");
		}
		log.info("newHhtUser : " + newHhtUser);
		BeanUtils.copyProperties(newHhtUser, dbHhtUser, CommonUtils.getNullPropertyNames(newHhtUser));
		dbHhtUser.setDeletionIndicator(0L);
		dbHhtUser.setCreatedBy(loginUserID);
		dbHhtUser.setUpdatedBy(loginUserID);
		dbHhtUser.setCreatedOn(new Date());
		dbHhtUser.setUpdatedOn(new Date());
		return hhtUserRepository.save(dbHhtUser);
	}

	/**
	 * updateHhtUser
	 * @param loginUserID
	 * @param userId
	 * @param updateHhtUser
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HhtUser updateHhtUser (String warehouseId, String userId,String companyCodeId,String languageId,String plantId,UpdateHhtUser updateHhtUser, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		HhtUser dbHhtUser = getHhtUser(userId, warehouseId,companyCodeId,plantId,languageId);
		BeanUtils.copyProperties(updateHhtUser, dbHhtUser, CommonUtils.getNullPropertyNames(updateHhtUser));
		dbHhtUser.setUpdatedBy(loginUserID);
		dbHhtUser.setUpdatedOn(new Date());
		return hhtUserRepository.save(dbHhtUser);
	}

	/**
	 * deleteHhtUser
	 * @param loginUserID
	 * @param userId
	 */
	public void deleteHhtUser (String warehouseId, String userId,String companyCodeId,String plantId,String languageId,String loginUserID) {
		HhtUser hhtUser = getHhtUser(userId, warehouseId,companyCodeId,plantId,languageId);
		if ( hhtUser != null) {
			hhtUser.setDeletionIndicator(1L);
			hhtUser.setUpdatedBy(loginUserID);
			hhtUserRepository.save(hhtUser);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userId);
		}
	}

	//Find HhtUser
	public List<HhtUser> findHhtUser(FindHhtUser findHhtUser) throws ParseException {

		HhtUserSpecification spec = new HhtUserSpecification(findHhtUser);
		List<HhtUser> results = hhtUserRepository.findAll(spec);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		log.info("results: " + results);
		return results;
	}
}
