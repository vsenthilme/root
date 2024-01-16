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

import com.iweb2b.api.master.model.subscriptionmgmt.AddSubscriptionManagement;
import com.iweb2b.api.master.model.subscriptionmgmt.SubscriptionManagement;
import com.iweb2b.api.master.model.subscriptionmgmt.UpdateSubscriptionManagement;
import com.iweb2b.api.master.repository.SubscriptionManagementRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SubscriptionManagementService {
	
	@Autowired
	private SubscriptionManagementRepository subscriptionManagementRepository;
	
	public List<SubscriptionManagement> getSubscriptionManagement () {
		List<SubscriptionManagement> subscriptionManagementList =  subscriptionManagementRepository.findAll();
		subscriptionManagementList = subscriptionManagementList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return subscriptionManagementList;
	}
	
	/**
	 * getSubscriptionManagement
	 * @param subscriptionManagementId
	 * @return
	 */
	public SubscriptionManagement getSubscriptionManagement (String partnerId) {
		Optional<SubscriptionManagement> subscriptionManagement = subscriptionManagementRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (subscriptionManagement.isEmpty()) {
			return null;
		}
		return subscriptionManagement.get();
	}
	
	/**
	 * createSubscriptionManagement
	 * @param newSubscriptionManagement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SubscriptionManagement createSubscriptionManagement (AddSubscriptionManagement newSubscriptionManagement, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		SubscriptionManagement dbSubscriptionManagement = new SubscriptionManagement();
		BeanUtils.copyProperties(newSubscriptionManagement, dbSubscriptionManagement, CommonUtils.getNullPropertyNames(newSubscriptionManagement));
		dbSubscriptionManagement.setDeletionIndicator(0L);
		dbSubscriptionManagement.setCreatedBy(loginUserId);
		dbSubscriptionManagement.setUpdatedBy(loginUserId);
		dbSubscriptionManagement.setCreatedOn(new Date());
		dbSubscriptionManagement.setUpdatedOn(new Date());
		return subscriptionManagementRepository.save(dbSubscriptionManagement);
	}
	
	/**
	 * updateSubscriptionManagement
	 * @param subscriptionManagementId
	 * @param loginUserId 
	 * @param updateSubscriptionManagement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SubscriptionManagement updateSubscriptionManagement (String partnerId, String loginUserId, 
			UpdateSubscriptionManagement updateSubscriptionManagement)
			throws IllegalAccessException, InvocationTargetException {
		SubscriptionManagement dbSubscriptionManagement = getSubscriptionManagement(partnerId);
		BeanUtils.copyProperties(updateSubscriptionManagement, dbSubscriptionManagement, 
				CommonUtils.getNullPropertyNames(updateSubscriptionManagement));
		dbSubscriptionManagement.setUpdatedBy(loginUserId);
		dbSubscriptionManagement.setUpdatedOn(new Date());
		return subscriptionManagementRepository.save(dbSubscriptionManagement);
	}
	
	/**
	 * deleteSubscriptionManagement
	 * @param loginUserID 
	 * @param subscriptionmanagementCode
	 */
	public void deleteSubscriptionManagement (String subscriptionmanagementModuleId, String loginUserID) {
		SubscriptionManagement subscriptionmanagement = getSubscriptionManagement(subscriptionmanagementModuleId);
		if (subscriptionmanagement != null) {
			subscriptionmanagement.setDeletionIndicator(1L);
			subscriptionmanagement.setUpdatedBy(loginUserID);
			subscriptionmanagement.setUpdatedOn(new Date());
			subscriptionManagementRepository.save(subscriptionmanagement);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + subscriptionmanagementModuleId);
		}
	}
}
