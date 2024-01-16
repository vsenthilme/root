package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.auditlog.AddAuditLog;
import com.mnrclara.api.setup.model.auditlog.AuditLog;
import com.mnrclara.api.setup.model.auditlog.UpdateAuditLog;
import com.mnrclara.api.setup.repository.AuditLogRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuditLogService {
	
	@Autowired
	private AuditLogRepository auditLogRepository;
	
	public List<AuditLog> getAuditLogs () {
		return auditLogRepository.findAll();
	}
	
	/**
	 * getAuditLog
	 * @param auditlogId
	 * @return
	 */
	public AuditLog getAuditLog (String auditLogNumber) {
		return auditLogRepository.findByAuditLogNumber(auditLogNumber).orElse(null);
	}
	
	/**
	 * createAuditLog
	 * @param newAuditLog
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AuditLog createAuditLog (AddAuditLog newAuditLog, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<AuditLog> auditlog = 
				auditLogRepository.findByLanguageIdAndClassIdAndAuditLogNumberAndTransactionIdAndTransactionNo (
						newAuditLog.getLanguageId(), 
						newAuditLog.getClassId(), 
						newAuditLog.getAuditLogNumber(), 
						newAuditLog.getTransactionId(), 
						newAuditLog.getTransactionNo());	
		if (!auditlog.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		AuditLog dbAuditLog = new AuditLog();
		BeanUtils.copyProperties(newAuditLog, dbAuditLog, CommonUtils.getNullPropertyNames(newAuditLog));
		dbAuditLog.setCreatedBy(loginUserID);
		dbAuditLog.setUpdatedBy(loginUserID);
		dbAuditLog.setCreatedOn(new Date());
		dbAuditLog.setUpdatedOn(new Date());
		log.info("dbAuditLog----------> : " + dbAuditLog);
		return auditLogRepository.save(dbAuditLog);
	}
	
	/**
	 * updateAuditLog
	 * @param auditlogId
	 * @param loginUserId 
	 * @param updateAuditLog
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AuditLog updateAuditLog (String auditlogId, String loginUserID, UpdateAuditLog updateAuditLog) 
			throws IllegalAccessException, InvocationTargetException {
		AuditLog dbAuditLog = getAuditLog(auditlogId);
		BeanUtils.copyProperties(updateAuditLog, dbAuditLog, CommonUtils.getNullPropertyNames(updateAuditLog));
		dbAuditLog.setUpdatedBy(loginUserID);
		dbAuditLog.setUpdatedOn(new Date());
		return auditLogRepository.save(dbAuditLog);
	}
	
	/**
	 * deleteAuditLog
	 * @param auditlogCode
	 */
	public void deleteAuditLog (String auditlogModuleId) {
		AuditLog auditlog = getAuditLog(auditlogModuleId);
		if ( auditlog != null) {
			auditLogRepository.delete(auditlog);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + auditlogModuleId);
		}
	}
}
