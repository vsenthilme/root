package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.model.referral.AddReferral;
import com.mnrclara.api.setup.model.referral.Referral;
import com.mnrclara.api.setup.model.referral.UpdateReferral;
import com.mnrclara.api.setup.repository.ReferralRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReferralService {
	
	@Autowired
	private ReferralRepository referralRepository;
	
	public List<Referral> getReferrals() {
		List<Referral> referralList = referralRepository.findAll();
		return referralList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getReferral
	 * @param referralId
	 * @return
	 */
	public Referral getReferral (Long referralId) {
		Referral referral = referralRepository.findByReferralId(referralId);
		if (referral != null && referral.getDeletionIndicator() == 0) {
			return referral;
		} 
//		throw new BadRequestException("The given Referral ID : " + referralId + " doesn't exist.");
		return null;
	}
	
	/**
	 * createReferral
	 * @param newReferral
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Referral createReferral (AddReferral newReferral, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Referral dbReferral = new Referral();
		BeanUtils.copyProperties(newReferral, dbReferral, CommonUtils.getNullPropertyNames(newReferral));
		dbReferral.setDeletionIndicator(0L);
		dbReferral.setCreatedBy(loginUserID);
		dbReferral.setUpdatedBy(loginUserID);
		dbReferral.setCreatedOn(new Date());
		dbReferral.setUpdatedOn(new Date());
		return referralRepository.save(dbReferral);
	}
	
	/**
	 * updateReferral
	 * @param referralId
	 * @param loginUserId 
	 * @param updateReferral
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Referral updateReferral (String languageId, Long classId, Long referralId, String loginUserID, UpdateReferral updateReferral) 
			throws IllegalAccessException, InvocationTargetException {
		Referral dbReferral = getReferral(referralId);
		BeanUtils.copyProperties(updateReferral, dbReferral, CommonUtils.getNullPropertyNames(updateReferral));
		dbReferral.setUpdatedBy(loginUserID);
		dbReferral.setUpdatedOn(new Date());
		return referralRepository.save(dbReferral);
	}
	
	/**
	 * deleteReferral
	 * @param loginUserID 
	 * @param referralCode
	 */
	public void deleteReferral (String languageId, Long classId, Long referralId, String loginUserID) {
		Referral referral = getReferral(referralId);
		if ( referral != null) {
			referral.setDeletionIndicator(1L);
			referral.setUpdatedBy(loginUserID);
			referral.setUpdatedOn(new Date());
			referralRepository.save(referral);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + referralId);
		}
	}
}
