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
import com.mnrclara.api.setup.model.billingfrequency.AddBillingFrequency;
import com.mnrclara.api.setup.model.billingfrequency.BillingFrequency;
import com.mnrclara.api.setup.model.billingfrequency.UpdateBillingFrequency;
import com.mnrclara.api.setup.repository.BillingFrequencyRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillingFrequencyService {
	
	@Autowired
	private BillingFrequencyRepository billingFrequencyRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<BillingFrequency> getBillingFrequencies () {
		List<BillingFrequency> billingFrequencyList =  billingFrequencyRepository.findAll();
		return billingFrequencyList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getBillingFrequency
	 * @param billingfrequencyId
	 * @return
	 */
	public BillingFrequency getBillingFrequency (Long billingFrequencyId) {
		BillingFrequency billingFrequency = billingFrequencyRepository.findByBillingFrequencyId(billingFrequencyId).orElse(null);
		if (billingFrequency.getDeletionIndicator() == 0) {
			return billingFrequency;
		} else {
			throw new BadRequestException("The given Billing Frequency ID : " + billingFrequencyId + " doesn't exist.");
		}
	}
	
	/**
	 * createBillingFrequency
	 * @param newBillingFrequency
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingFrequency createBillingFrequency (AddBillingFrequency newBillingFrequency, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFrequency dbBillingFrequency = new BillingFrequency();
		BeanUtils.copyProperties(newBillingFrequency, dbBillingFrequency, CommonUtils.getNullPropertyNames(newBillingFrequency));
		dbBillingFrequency.setDeletionIndicator(0L);
		dbBillingFrequency.setCreatedBy(loginUserID);
		dbBillingFrequency.setUpdatedBy(loginUserID);
		dbBillingFrequency.setCreatedOn(new Date());
		dbBillingFrequency.setUpdatedOn(new Date());
		return billingFrequencyRepository.save(dbBillingFrequency);
	}
	
	/**
	 * updateBillingFrequency
	 * @param billingfrequencyId
	 * @param loginUserId 
	 * @param updateBillingFrequency
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingFrequency updateBillingFrequency (Long billingfrequencyId, String loginUserID, UpdateBillingFrequency updateBillingFrequency) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFrequency dbBillingFrequency = getBillingFrequency(billingfrequencyId);
		BeanUtils.copyProperties(updateBillingFrequency, dbBillingFrequency, CommonUtils.getNullPropertyNames(updateBillingFrequency));
		dbBillingFrequency.setUpdatedBy(loginUserID);
		dbBillingFrequency.setUpdatedOn(new Date());
		return billingFrequencyRepository.save(dbBillingFrequency);
	}
	
	/**
	 * deleteBillingFrequency
	 * @param billingfrequencyCode
	 */
	public void deleteBillingFrequency (Long billingFrequencyId, String loginUserID) {
		BillingFrequency billingfrequency = getBillingFrequency(billingFrequencyId);
		if ( billingfrequency != null) {
			billingfrequency.setDeletionIndicator(1L);
			billingfrequency.setUpdatedBy(loginUserID);
			billingFrequencyRepository.save(billingfrequency);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + billingFrequencyId);
		}
	}
}
