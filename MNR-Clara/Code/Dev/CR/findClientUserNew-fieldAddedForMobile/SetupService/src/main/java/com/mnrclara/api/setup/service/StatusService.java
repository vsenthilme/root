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
import com.mnrclara.api.setup.model.status.AddStatus;
import com.mnrclara.api.setup.model.status.Status;
import com.mnrclara.api.setup.model.status.UpdateStatus;
import com.mnrclara.api.setup.repository.StatusRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusService {
	
	@Autowired
	private StatusRepository statusRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Status> getAllStatus() {
		List<Status> statusList =  statusRepository.findAll();
		statusList = statusList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return statusList;
	}
	
	/**O
	 * getStatus
	 * @param statusId
	 * @return
	 */
	public Status getStatus (Long statusId) {
		Status status = statusRepository.findByStatusId(statusId);
		if (status != null && status.getDeletionIndicator() == 0) {
			return status;
		} else {
			throw new BadRequestException("The given Status ID : " + statusId + " doesn't exist.");
		}
	}
	
	/**
	 * createStatus
	 * @param newStatus
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Status createStatus (AddStatus newStatus, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Status> status = 
				statusRepository.findByLanguageIdAndStatusIdAndDeletionIndicator (		
					newStatus.getLanguageId(),
					newStatus.getStatusId(),
					0L);
		if (!status.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Status dbStatus = new Status();
		BeanUtils.copyProperties(newStatus, dbStatus);
		dbStatus.setDeletionIndicator(0L);
		dbStatus.setCreatedBy(loginUserID);
		dbStatus.setUpdatedBy(loginUserID);
		dbStatus.setCreatedOn(new Date());
		dbStatus.setUpdatedOn(new Date());
		return statusRepository.save(dbStatus);
	}
	
	/**
	 * updateStatus
	 * @param statusId
	 * @param loginUserId 
	 * @param updateStatus
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Status updateStatus (Long statusId, String loginUserID, UpdateStatus updateStatus) 
			throws IllegalAccessException, InvocationTargetException {
		Status dbStatus = getStatus(statusId);
		BeanUtils.copyProperties(updateStatus, dbStatus, CommonUtils.getNullPropertyNames(updateStatus));
		dbStatus.setUpdatedBy(loginUserID);
		dbStatus.setUpdatedOn(new Date());
		return statusRepository.save(dbStatus);
	}
	
	/**
	 * deleteStatus
	 * @param loginUserID 
	 * @param statusCode
	 */
	public void deleteStatus (Long statusModuleId, String loginUserID) {
		Status status = getStatus(statusModuleId);
		if ( status != null) {
			status.setDeletionIndicator(1L);
			status.setUpdatedBy(loginUserID);
			statusRepository.save(status);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + statusModuleId);
		}
	}
}
