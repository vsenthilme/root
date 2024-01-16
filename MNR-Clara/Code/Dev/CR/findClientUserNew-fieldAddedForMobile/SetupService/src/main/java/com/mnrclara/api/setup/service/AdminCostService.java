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
import com.mnrclara.api.setup.model.admincost.AddAdminCost;
import com.mnrclara.api.setup.model.admincost.AdminCost;
import com.mnrclara.api.setup.model.admincost.UpdateAdminCost;
import com.mnrclara.api.setup.repository.AdminCostRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminCostService {
	
	@Autowired
	private AdminCostRepository adminCostRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<AdminCost> getAdminCosts () {
		List<AdminCost> adminCostList = adminCostRepository.findAll();
		adminCostList = adminCostList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return adminCostList;
	}
	
	/**
	 * getAdminCost
	 * @param AdminCostId
	 * @return
	 */
	public AdminCost getAdminCost (Long adminCostId) {
		AdminCost adminCost = adminCostRepository.findByAdminCostId(adminCostId).orElse(null);
		if (adminCost != null && adminCost.getDeletionIndicator() == 0) {
			return adminCost;
		} else {
			throw new BadRequestException("The given AdminCost ID : " + adminCostId + " doesn't exist.");
		}
	}
	
	/**
	 * createAdminCost
	 * @param newAdminCost
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AdminCost createAdminCost (AddAdminCost newAdminCost, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		AdminCost dbAdminCost = new AdminCost();
		BeanUtils.copyProperties(newAdminCost, dbAdminCost, CommonUtils.getNullPropertyNames(newAdminCost));
		dbAdminCost.setDeletionIndicator(0L);
		dbAdminCost.setCreatedBy(loginUserID);
		dbAdminCost.setUpdatedBy(loginUserID);
		dbAdminCost.setCreatedOn(new Date());
		dbAdminCost.setUpdatedOn(new Date());
		return adminCostRepository.save(dbAdminCost);
	}
	
	/**
	 * updateAdminCost
	 * @param loginUserId 
	 * @param AdminCostId
	 * @param updateAdminCost
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AdminCost updateAdminCost (Long adminCostId, String loginUserId, UpdateAdminCost updateAdminCost) 
			throws IllegalAccessException, InvocationTargetException {
		AdminCost dbAdminCost = getAdminCost(adminCostId);
		BeanUtils.copyProperties(updateAdminCost, dbAdminCost, CommonUtils.getNullPropertyNames(updateAdminCost));
		dbAdminCost.setUpdatedBy(loginUserId);
		dbAdminCost.setUpdatedOn(new Date());
		return adminCostRepository.save(dbAdminCost);
	}
	
	/**
	 * deleteAdminCost
	 * @param loginUserID 
	 * @param AdminCostCode
	 */
	public void deleteAdminCost (Long adminCostId, String loginUserID) {
		AdminCost adminCost = getAdminCost(adminCostId);
		if ( adminCost != null) {
			adminCost.setDeletionIndicator(1L);
			adminCost.setUpdatedBy(loginUserID);
			adminCost.setUpdatedOn(new Date());
			adminCostRepository.save(adminCost);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + adminCostId);
		}
	}
}
