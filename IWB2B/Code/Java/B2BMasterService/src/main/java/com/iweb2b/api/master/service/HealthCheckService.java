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

import com.iweb2b.api.master.model.healthcheck.AddHealthCheck;
import com.iweb2b.api.master.model.healthcheck.HealthCheck;
import com.iweb2b.api.master.model.healthcheck.UpdateHealthCheck;
import com.iweb2b.api.master.repository.HealthCheckRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthCheckService {
	
	@Autowired
	private HealthCheckRepository healthCheckRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<HealthCheck> getHealthCheck () {
		List<HealthCheck> HealthCheckList =  healthCheckRepository.findAll();
		HealthCheckList = HealthCheckList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return HealthCheckList;
	}
	
	/**
	 * getHealthCheck
	 * @param HealthCheckId
	 * @return
	 */
	public HealthCheck getHealthCheck (String healthCheckId) {
		/*
		 * select * from tblhealthcheck where partner_id=? and is_deleted=?
		 */
		Optional<HealthCheck> HealthCheck = healthCheckRepository.findByPartnerIdAndDeletionIndicator(healthCheckId, 0L);
		if (HealthCheck.isEmpty()) {
			return null;
		}
		return HealthCheck.get();
	}
	
	/**
	 * createHealthCheck
	 * @param newHealthCheck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HealthCheck createHealthCheck (AddHealthCheck newHealthCheck, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		HealthCheck dbHealthCheck = new HealthCheck();
		BeanUtils.copyProperties(newHealthCheck, dbHealthCheck, CommonUtils.getNullPropertyNames(newHealthCheck));
		dbHealthCheck.setDeletionIndicator(0L);
		dbHealthCheck.setCreatedBy(loginUserId);
		dbHealthCheck.setUpdatedBy(loginUserId);
		dbHealthCheck.setCreatedOn(new Date());
		dbHealthCheck.setUpdatedOn(new Date());
		return healthCheckRepository.save(dbHealthCheck);
	}
	
	/**
	 * updateHealthCheck
	 * @param HealthCheckId
	 * @param loginUserId 
	 * @param updateHealthCheck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HealthCheck updateHealthCheck (String HealthCheckId, String loginUserId, UpdateHealthCheck updateHealthCheck)
			throws IllegalAccessException, InvocationTargetException {
		HealthCheck dbHealthCheck = getHealthCheck(HealthCheckId);
		BeanUtils.copyProperties(updateHealthCheck, dbHealthCheck, CommonUtils.getNullPropertyNames(updateHealthCheck));
		dbHealthCheck.setUpdatedBy(loginUserId);
		dbHealthCheck.setUpdatedOn(new Date());
		return healthCheckRepository.save(dbHealthCheck);
	}
	
	/**
	 * deleteHealthCheck
	 * @param loginUserID 
	 * @param HealthCheckCode
	 */
	public void deleteHealthCheck (String healthCheckModuleId, String loginUserID) {
		HealthCheck dbHealthCheck = getHealthCheck(healthCheckModuleId);
		if (dbHealthCheck != null) {
			dbHealthCheck.setDeletionIndicator(1L);
			dbHealthCheck.setUpdatedBy(loginUserID);
			dbHealthCheck.setUpdatedOn(new Date());
			healthCheckRepository.save(dbHealthCheck);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + healthCheckModuleId);
		}
	}
}
