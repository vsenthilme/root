package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.matterfeesharing.AddMatterFeeSharing;
import com.mnrclara.api.management.model.matterfeesharing.MatterFeeSharing;
import com.mnrclara.api.management.model.matterfeesharing.UpdateMatterFeeSharing;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.MatterFeeSharingRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterFeeSharingService {
	
	@Autowired
	private MatterFeeSharingRepository matterFeeSharingRepository;
	
	@Autowired
	private MatterGenAccService matterGenAccService;
	
	/**
	 * getMatterFeeSharings
	 * @return
	 */
	public List<MatterFeeSharing> getMatterFeeSharings () {
		List<MatterFeeSharing> matterFeeSharingList = matterFeeSharingRepository.findAll();
		matterFeeSharingList = matterFeeSharingList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterFeeSharingList;
	}
	
	/**
	 * getMatterFeeSharing
	 * @param timeKeeperCode 
	 * @param matterFeeSharingId
	 * @return
	 */
	public MatterFeeSharing getMatterFeeSharing (String matterNumber, String timeKeeperCode) {
		MatterFeeSharing matterFeeSharing = 
				matterFeeSharingRepository.findByMatterNumberAndTimeKeeperCode(matterNumber, timeKeeperCode).orElse(null);
		if (matterFeeSharing != null && 
				matterFeeSharing.getDeletionIndicator() != null && matterFeeSharing.getDeletionIndicator() == 0) {
			return matterFeeSharing;
		} else {
			throw new BadRequestException("The given MatterFeeSharing ID : " + matterNumber + " doesn't exist.");
		}
	}
	
	/**
	 * createMatterFeeSharing
	 * @param newMatterFeeSharing
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterFeeSharing createMatterFeeSharing (AddMatterFeeSharing newMatterFeeSharing, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		
		MatterFeeSharing dbMatterFeeSharing = new MatterFeeSharing();
		BeanUtils.copyProperties(newMatterFeeSharing, dbMatterFeeSharing, CommonUtils.getNullPropertyNames(newMatterFeeSharing));
		
		// TK_CODE
		dbMatterFeeSharing.setTimeKeeperCode(newMatterFeeSharing.getTimeKeeperCode());
		
		// FEE_SHARE_PERCENTAGE
		dbMatterFeeSharing.setFeeSharingPercentage(newMatterFeeSharing.getFeeSharingPercentage());
		
		// MATTER_NO
		dbMatterFeeSharing.setMatterNumber(newMatterFeeSharing.getMatterNumber());
		
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterFeeSharing.getMatterNumber());

		// LANG_ID
		dbMatterFeeSharing.setLanguageId(matterGenAcc.getLanguageId());

		// CLASS_ID
		dbMatterFeeSharing.setClassId(matterGenAcc.getClassId());		
		
		// CLIENT_ID
		dbMatterFeeSharing.setClientId(matterGenAcc.getClientId());
		
		// CASE_CATEGORY_ID
		dbMatterFeeSharing.setCaseCategoryId(matterGenAcc.getCaseCategoryId());	
		
		// CASE_SUB_CATEGORY_ID
		dbMatterFeeSharing.setCaseSubCategoryId(matterGenAcc.getCaseSubCategoryId());
		
		// STATUS_ID
		dbMatterFeeSharing.setStatusId(matterGenAcc.getStatusId());
		
		dbMatterFeeSharing.setDeletionIndicator(0L);
		dbMatterFeeSharing.setCreatedBy(loginUserID);
		dbMatterFeeSharing.setUpdatedBy(loginUserID);
		dbMatterFeeSharing.setCreatedOn(new Date());
		dbMatterFeeSharing.setUpdatedOn(new Date());
		return matterFeeSharingRepository.save(dbMatterFeeSharing);
	}
	
	/**'
	 * updateMatterFeeSharing
	 * @param matterNumber
	 * @param timeKeeperCode
	 * @param updateMatterFeeSharing
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterFeeSharing updateMatterFeeSharing (String matterNumber, String timeKeeperCode, 
			UpdateMatterFeeSharing updateMatterFeeSharing, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<MatterFeeSharing> optDbMatterFeeSharing = 
				matterFeeSharingRepository.findByMatterNumberAndTimeKeeperCode(matterNumber, timeKeeperCode);
		if (optDbMatterFeeSharing.isEmpty()) {
			throw new BadRequestException("Record not found for the given matterNumber : " + matterNumber + ", timeKeeperCode:" + timeKeeperCode );
		}
		MatterFeeSharing dbMatterFeeSharing = optDbMatterFeeSharing.get();
		BeanUtils.copyProperties(updateMatterFeeSharing, dbMatterFeeSharing, 
				CommonUtils.getNullPropertyNames(updateMatterFeeSharing));
		
		// FEE_SHARE_PERCENTAGE
		dbMatterFeeSharing.setFeeSharingPercentage(updateMatterFeeSharing.getFeeSharingPercentage());
		return matterFeeSharingRepository.save(dbMatterFeeSharing);
	}
	
	/**
	 * deleteMatterFeeSharing
	 * @param matterfeesharingCode
	 */
	public void deleteMatterFeeSharing (String matterNumber, String timeKeeperCode, String loginUserID) {
		MatterFeeSharing matterfeesharing = getMatterFeeSharing(matterNumber, timeKeeperCode);
		if ( matterfeesharing != null) {
			matterfeesharing.setDeletionIndicator(1L);
			matterfeesharing.setUpdatedBy(loginUserID);
			matterfeesharing.setUpdatedOn(new Date());
			matterFeeSharingRepository.save(matterfeesharing);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterNumber);
		}
	}
}
