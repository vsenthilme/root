package com.iweb2b.api.master.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.master.model.auditlog.AuditLog;
import com.iweb2b.api.master.repository.AuditLogRepository;
import com.iweb2b.api.master.util.CommonUtils;

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
		return auditLogRepository.findByAuditLogNumber(auditLogNumber);
	}
	
	/**
	 * createAuditLog
	 * @param newAuditLog
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AuditLog createAuditLog (AuditLog newAuditLog, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		
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
	 * deleteAuditLog
	 * @param auditlogCode
	 */
	public void deleteAuditLog (String auditlogNumber) {
		AuditLog auditlog = getAuditLog(auditlogNumber);
		if ( auditlog != null) {
			auditLogRepository.delete(auditlog);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + auditlogNumber);
		}
	}
}
