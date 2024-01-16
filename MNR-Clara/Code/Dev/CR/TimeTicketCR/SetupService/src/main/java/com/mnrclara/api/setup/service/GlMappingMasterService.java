package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.glmappingmaster.AddGlMappingMaster;
import com.mnrclara.api.setup.model.glmappingmaster.GlMappingMaster;
import com.mnrclara.api.setup.model.glmappingmaster.UpdateGlMappingMaster;
import com.mnrclara.api.setup.repository.GlMappingMasterRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GlMappingMasterService {
	
	@Autowired
	private GlMappingMasterRepository glMappingMasterRepository;
	
	/**
	 * getGlMappingMasters
	 * @return
	 */
	public List<GlMappingMaster> getGlMappingMasters () {
		List<GlMappingMaster> glMappingMasterList =  glMappingMasterRepository.findAll();
		glMappingMasterList = glMappingMasterList.stream().filter(n -> n.getDeletionIndicator() != null && 
				n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return glMappingMasterList;
	}
	
	/**
	 * getGlMappingMaster
	 * @param itemNumber
	 * @return
	 */
	public GlMappingMaster getGlMappingMaster (Long itemNumber) {
		Optional<GlMappingMaster> glMappingMaster = glMappingMasterRepository.findByItemNumberAndDeletionIndicator(
						itemNumber, 0L);
		if (glMappingMaster != null) {
			return glMappingMaster.get();
		} else {
			throw new BadRequestException("The given GlMappingMaster ID : " + itemNumber + " doesn't exist.");
		}
	}	
	
	/**
	 * createGlMappingMaster
	 * @param newGlMappingMaster
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public GlMappingMaster createGlMappingMaster (AddGlMappingMaster newGlMappingMaster, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster dbGlMappingMaster = new GlMappingMaster();
		BeanUtils.copyProperties(newGlMappingMaster, dbGlMappingMaster, CommonUtils.getNullPropertyNames(newGlMappingMaster));
		dbGlMappingMaster.setDeletionIndicator(0L);
		dbGlMappingMaster.setCreatedBy(loginUserID);
		dbGlMappingMaster.setUpdatedBy(loginUserID);
		dbGlMappingMaster.setCreatedOn(new Date());
		dbGlMappingMaster.setUpdatedOn(new Date());
		return glMappingMasterRepository.save(dbGlMappingMaster);
	}
	
	/**
	 * updateGlMappingMaster
	 * @param loginUserId 
	 * @param itemNumber
	 * @param updateGlMappingMaster
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public GlMappingMaster updateGlMappingMaster (Long itemNumber, UpdateGlMappingMaster updateGlMappingMaster,
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		GlMappingMaster dbGlMappingMaster = getGlMappingMaster(itemNumber);
		BeanUtils.copyProperties(updateGlMappingMaster, dbGlMappingMaster, CommonUtils.getNullPropertyNames(updateGlMappingMaster));
		dbGlMappingMaster.setUpdatedBy(loginUserID);
		dbGlMappingMaster.setUpdatedOn(new Date());
		return glMappingMasterRepository.save(dbGlMappingMaster);
	}
	
	/**
	 * deleteGlMappingMaster
	 * @param loginUserID 
	 * @param itemNumber
	 */
	public void deleteGlMappingMaster (Long itemNumber,  String loginUserID) {
		GlMappingMaster glMappingMaster = getGlMappingMaster(itemNumber);
		if ( glMappingMaster != null ) {
			glMappingMaster.setDeletionIndicator(1L);
			glMappingMaster.setUpdatedBy(loginUserID);
			glMappingMasterRepository.save(glMappingMaster);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + itemNumber);
		}
	}
}
