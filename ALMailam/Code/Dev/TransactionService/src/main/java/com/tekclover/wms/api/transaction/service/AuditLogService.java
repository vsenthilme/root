package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.model.auditlog.AuditLog;
import com.tekclover.wms.api.transaction.repository.AuditLogRepository;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AuditLogService {
	
	@Autowired
	private AuditLogRepository auditLogRepository;
	
	public List<AuditLog> getAuditLogs () {
		return auditLogRepository.findAll();
	}

	/**
	 *
	 * @param auditLogNumber
	 * @return
	 */
	public AuditLog getAuditLog (String auditLogNumber) {
		return auditLogRepository.findByAuditLogNumber(auditLogNumber);
	}

	public Long getAuditLogNumber () {
		Long auditLogNumber;
		if(auditLogRepository.getAuditLogNumber() == null) {
			auditLogNumber = 1L;
		} else {
			auditLogNumber = Long.valueOf(auditLogRepository.getAuditLogNumber());
		}
		return auditLogNumber;
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
		dbAuditLog.setAuditLogNumber(System.currentTimeMillis());
		dbAuditLog.setCreatedBy(loginUserID);
		dbAuditLog.setUpdatedBy(loginUserID);
		dbAuditLog.setCreatedOn(new Date());
		dbAuditLog.setUpdatedOn(new Date());
		log.info("dbAuditLog----------> : " + dbAuditLog);
		return auditLogRepository.save(dbAuditLog);
	}
	
	/**
	 * deleteAuditLog
	 * @param auditLogNumber
	 */
	public void deleteAuditLog (String auditLogNumber) {
		AuditLog auditlog = getAuditLog(auditLogNumber);
		if ( auditlog != null) {
			auditlog.setDeletionIndicator(1L);
			auditLogRepository.save(auditlog);
//			auditLogRepository.delete(auditlog);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + auditLogNumber);
		}
	}
}
