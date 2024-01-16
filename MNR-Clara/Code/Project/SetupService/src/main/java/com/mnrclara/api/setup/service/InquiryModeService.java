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
import com.mnrclara.api.setup.model.inquirymode.AddInquiryMode;
import com.mnrclara.api.setup.model.inquirymode.InquiryMode;
import com.mnrclara.api.setup.model.inquirymode.UpdateInquiryMode;
import com.mnrclara.api.setup.repository.InquiryModeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InquiryModeService {
	
	@Autowired
	private InquiryModeRepository inquiryModeRepository;
	
	private String ACTIVE_STATUS = "ACTIVE";
	
	public List<InquiryMode> getInquiryModes () {
		List<InquiryMode> inquiryModeList = inquiryModeRepository.findAll();
		return inquiryModeList.stream().filter(	n -> n.getDeletionIndicator() == 0 ).collect(Collectors.toList());
	}
	
	/**
	 * getInquiryMode
	 * @param inquiryModeId
	 * @return
	 */
	public InquiryMode getInquiryMode (Long inquiryModeId) {
		InquiryMode inquiryMode = inquiryModeRepository.findByInquiryModeId(inquiryModeId);
		if (inquiryMode != null 
				&& inquiryMode.getDeletionIndicator() == 0) {
			return inquiryMode;
		} else {
			throw new BadRequestException("The given InquiryMode ID : " + inquiryModeId + " doesn't exist or it is not ACTIVE.");
		}
	}
	
	/**
	 * createInquiryMode
	 * @param newInquiryMode
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InquiryMode createInquiryMode (AddInquiryMode newInquiryMode, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<InquiryMode>  inquirymode = 
				inquiryModeRepository.findByLanguageIdAndClassIdAndInquiryModeIdAndDeletionIndicator (
					newInquiryMode.getLanguageId(),
					newInquiryMode.getClassId(),
					newInquiryMode.getInquiryModeId(),
					0L);
		if (!inquirymode.isEmpty() && inquirymode.get().getDeletionIndicator() == 0) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		
		InquiryMode dbInquiryMode = new InquiryMode();
		BeanUtils.copyProperties(newInquiryMode, dbInquiryMode);
		if (newInquiryMode.getInquiryModeStatus() != null) {
			dbInquiryMode.setInquiryModeStatus(newInquiryMode.getInquiryModeStatus().toUpperCase());
		}
		
		dbInquiryMode.setDeletionIndicator(0L);
		dbInquiryMode.setCreatedBy(loginUserID);
		dbInquiryMode.setUpdatedBy(loginUserID);
		dbInquiryMode.setCreatedOn(new Date());
		dbInquiryMode.setUpdatedOn(new Date());
		return inquiryModeRepository.save(dbInquiryMode);
	}
	
	/**
	 * updateInquiryMode
	 * @param inquiryModeId
	 * @param loginUserId 
	 * @param updateInquiryMode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public InquiryMode updateInquiryMode (Long inquiryModeId, String loginUserID, UpdateInquiryMode updateInquiryMode) 
			throws IllegalAccessException, InvocationTargetException {
		InquiryMode dbInquiryMode = getInquiryMode(inquiryModeId);
		BeanUtils.copyProperties(updateInquiryMode, dbInquiryMode, CommonUtils.getNullPropertyNames(updateInquiryMode));
		if (updateInquiryMode.getInquiryModeStatus() != null) {
			dbInquiryMode.setInquiryModeStatus(updateInquiryMode.getInquiryModeStatus().toUpperCase());
		}
		dbInquiryMode.setUpdatedBy(loginUserID);
		dbInquiryMode.setUpdatedOn(new Date());
		return inquiryModeRepository.save(dbInquiryMode);
	}
	
	/**
	 * deleteInquiryMode
	 * @param loginUserID 
	 * @param inquiryModeCode
	 */
	public void deleteInquiryMode (Long inquiryModeId, String loginUserID) {
		InquiryMode inquiryMode = getInquiryMode(inquiryModeId);
		if ( inquiryMode != null) {
			inquiryMode.setDeletionIndicator(1L);
			inquiryMode.setUpdatedBy(loginUserID);
			inquiryModeRepository.save(inquiryMode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + inquiryModeId);
		}
	}
}
