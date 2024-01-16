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
import com.mnrclara.api.setup.model.billingformat.AddBillingFormat;
import com.mnrclara.api.setup.model.billingformat.BillingFormat;
import com.mnrclara.api.setup.model.billingformat.UpdateBillingFormat;
import com.mnrclara.api.setup.repository.BillingFormatRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillingFormatService {
	
	@Autowired
	private BillingFormatRepository billingFormatRepository;
	
	public List<BillingFormat> getBillingFormats () {
		List<BillingFormat> bhillingFormatList = billingFormatRepository.findAll();
		return bhillingFormatList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getBillingFormat
	 * @param billingformatId
	 * @return
	 */
	public BillingFormat getBillingFormat (Long billingformatId) {
		BillingFormat billingFormat = billingFormatRepository.findByBillingFormatId(billingformatId).orElse(null);
		if (billingFormat.getDeletionIndicator() == 0) {
			return billingFormat;
		} else {
			throw new BadRequestException("The given BillingFormat ID : " + billingformatId + " doesn't exist.");
		}
	}
	
	/**
	 * createBillingFormat
	 * @param newBillingFormat
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingFormat createBillingFormat (AddBillingFormat newBillingFormat, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		BillingFormat dbBillingFormat = new BillingFormat();
		BeanUtils.copyProperties(newBillingFormat, dbBillingFormat, CommonUtils.getNullPropertyNames(newBillingFormat));
		dbBillingFormat.setDeletionIndicator(0L);
		dbBillingFormat.setCreatedBy(loginUserID);
		dbBillingFormat.setUpdatedBy(loginUserID);
		dbBillingFormat.setCreatedOn(new Date());
		dbBillingFormat.setUpdatedOn(new Date());
		return billingFormatRepository.save(dbBillingFormat);
	}
	
	/**
	 * updateBillingFormat
	 * @param billingformatId
	 * @param loginUserId 
	 * @param updateBillingFormat
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BillingFormat updateBillingFormat (Long billingformatId, String loginUserID, 
			UpdateBillingFormat updateBillingFormat) throws IllegalAccessException, InvocationTargetException {
		BillingFormat dbBillingFormat = getBillingFormat(billingformatId);
		BeanUtils.copyProperties(updateBillingFormat, dbBillingFormat, CommonUtils.getNullPropertyNames(updateBillingFormat));
		dbBillingFormat.setUpdatedBy(loginUserID);
		dbBillingFormat.setUpdatedOn(new Date());
		return billingFormatRepository.save(dbBillingFormat);
	}
	
	/**
	 * deleteBillingFormat
	 * @param billingformatCode
	 */
	public void deleteBillingFormat (Long billingformatId, String loginUserID) {
		BillingFormat billingformat = getBillingFormat(billingformatId);
		if (billingformat != null) {
			billingformat.setDeletionIndicator(1L);
			billingformat.setUpdatedBy(loginUserID);
			billingFormatRepository.save(billingformat);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + billingformatId);
		}
	}
}
