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
import com.mnrclara.api.setup.model.billingmode.AddBillingMode;
import com.mnrclara.api.setup.model.billingmode.BillingMode;
import com.mnrclara.api.setup.model.billingmode.UpdateBillingMode;
import com.mnrclara.api.setup.repository.BillingModeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillingModeService {
	
	@Autowired
	private BillingModeRepository billingModeRepository;
	
	public List<BillingMode> getBillingModes () {
		List<BillingMode> billingModeList =  billingModeRepository.findAll();
		return billingModeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getBillingMode
	 * @param billingmodeId
	 * @return
	 */
	public BillingMode getBillingMode (Long billingModeId) {
		BillingMode billingMode = billingModeRepository.findByBillingModeId(billingModeId).orElse(null);
		if (billingMode.getDeletionIndicator() == 0) {
			return billingMode;
		} else {
			throw new BadRequestException("The given BillingMode ID : " + billingModeId + " doesn't exist.");
		}
	}
	
	/**
	 * createBillingMode
	 * @param newBillingMode
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingMode createBillingMode (AddBillingMode newBillingMode, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode dbBillingMode = new BillingMode();
		BeanUtils.copyProperties(newBillingMode, dbBillingMode, CommonUtils.getNullPropertyNames(newBillingMode));
		dbBillingMode.setDeletionIndicator(0L);
		dbBillingMode.setCreatedBy(loginUserID);
		dbBillingMode.setUpdatedBy(loginUserID);
		dbBillingMode.setCreatedOn(new Date());
		dbBillingMode.setUpdatedOn(new Date());
		return billingModeRepository.save(dbBillingMode);
	}
	
	/**
	 * updateBillingMode
	 * @param loginUserId 
	 * @param billingmodeId
	 * @param updateBillingMode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingMode updateBillingMode (Long billingModeId, String loginUserID, UpdateBillingMode updateBillingMode) 
			throws IllegalAccessException, InvocationTargetException {
		BillingMode dbBillingMode = getBillingMode(billingModeId);
		BeanUtils.copyProperties(updateBillingMode, dbBillingMode, CommonUtils.getNullPropertyNames(updateBillingMode));
		dbBillingMode.setUpdatedBy(loginUserID);
		dbBillingMode.setUpdatedOn(new Date());
		return billingModeRepository.save(dbBillingMode);
	}
	
	/**
	 * deleteBillingMode
	 * @param billingmodeCode
	 */
	public void deleteBillingMode (Long billingModeId, String loginUserID) {
		BillingMode billingMode = getBillingMode(billingModeId);
		if ( billingMode != null) {
			billingMode.setDeletionIndicator(1L);
			billingMode.setUpdatedBy(loginUserID);
			billingMode.setUpdatedOn(new Date());
			billingModeRepository.save(billingMode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + billingModeId);
		}
	}
}
