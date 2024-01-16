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

import com.iweb2b.api.master.model.partnermaster.AddPartnerMaster;
import com.iweb2b.api.master.model.partnermaster.PartnerMaster;
import com.iweb2b.api.master.model.partnermaster.UpdatePartnerMaster;
import com.iweb2b.api.master.repository.PartnerMasterRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PartnerMasterService {
	
	@Autowired
	private PartnerMasterRepository partnerMasterRepository;
	
	public List<PartnerMaster> getPartnerMaster () {
		List<PartnerMaster> partnerMasterList =  partnerMasterRepository.findAll();
		partnerMasterList = partnerMasterList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return partnerMasterList;
	}
	
	/**
	 * getPartnerMaster
	 * @param partnerMasterId
	 * @return
	 */
	public PartnerMaster getPartnerMaster (String partnerMasterId) {
		Optional<PartnerMaster> partnerMaster = partnerMasterRepository.findByPartnerIdAndDeletionIndicator(partnerMasterId, 0L);
		if (partnerMaster.isEmpty()) {
			return null;
		}
		return partnerMaster.get();
	}
	
	/**
	 * createPartnerMaster
	 * @param newPartnerMaster
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PartnerMaster createPartnerMaster (AddPartnerMaster newPartnerMaster, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		PartnerMaster dbPartnerMaster = new PartnerMaster();
		BeanUtils.copyProperties(newPartnerMaster, dbPartnerMaster, CommonUtils.getNullPropertyNames(newPartnerMaster));
		dbPartnerMaster.setDeletionIndicator(0L);
		dbPartnerMaster.setCreatedBy(loginUserId);
		dbPartnerMaster.setUpdatedBy(loginUserId);
		dbPartnerMaster.setCreatedOn(new Date());
		dbPartnerMaster.setUpdatedOn(new Date());
		return partnerMasterRepository.save(dbPartnerMaster);
	}
	
	/**
	 * updatePartnerMaster
	 * @param partnerMasterId
	 * @param loginUserId 
	 * @param updatePartnerMaster
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PartnerMaster updatePartnerMaster (String partnerId, String loginUserId, UpdatePartnerMaster updatePartnerMaster)
			throws IllegalAccessException, InvocationTargetException {
		PartnerMaster dbPartnerMaster = getPartnerMaster(partnerId);
		BeanUtils.copyProperties(updatePartnerMaster, dbPartnerMaster, CommonUtils.getNullPropertyNames(updatePartnerMaster));
		dbPartnerMaster.setUpdatedBy(loginUserId);
		dbPartnerMaster.setUpdatedOn(new Date());
		return partnerMasterRepository.save(dbPartnerMaster);
	}
	
	/**
	 * deletePartnerMaster
	 * @param loginUserID 
	 * @param partnermasterCode
	 */
	public void deletePartnerMaster (String partnermasterModuleId, String loginUserID) {
		PartnerMaster partnermaster = getPartnerMaster(partnermasterModuleId);
		if (partnermaster != null) {
			partnermaster.setDeletionIndicator(1L);
			partnermaster.setUpdatedBy(loginUserID);
			partnermaster.setUpdatedOn(new Date());
			partnerMasterRepository.save(partnermaster);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnermasterModuleId);
		}
	}
}
