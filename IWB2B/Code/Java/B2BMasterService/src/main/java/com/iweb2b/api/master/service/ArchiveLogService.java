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

import com.iweb2b.api.master.model.archivelogs.AddArchiveLogs;
import com.iweb2b.api.master.model.archivelogs.ArchiveLogs;
import com.iweb2b.api.master.model.archivelogs.UpdateArchiveLogs;
import com.iweb2b.api.master.repository.ArchiveLogsRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArchiveLogService {
	
	@Autowired
	private ArchiveLogsRepository archivesLogsRepository;
	
	public List<ArchiveLogs> getArchiveLogs () {
		List<ArchiveLogs> archivesLogsList =  archivesLogsRepository.findAll();
		archivesLogsList = archivesLogsList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return archivesLogsList;
	}
	
	/**
	 * getArchiveLogs
	 * @param archivesLogsId
	 * @return
	 */
	public ArchiveLogs getArchiveLogs (String partnerId) {
		Optional<ArchiveLogs> archivesLogs = archivesLogsRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (archivesLogs.isEmpty()) {
			return null;
		}
		return archivesLogs.get();
	}
	
	/**
	 * createArchiveLogs
	 * @param newArchiveLogs
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ArchiveLogs createArchiveLogs (AddArchiveLogs newArchiveLogs, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		ArchiveLogs dbArchiveLogs = new ArchiveLogs();
		BeanUtils.copyProperties(newArchiveLogs, dbArchiveLogs, CommonUtils.getNullPropertyNames(newArchiveLogs));
		dbArchiveLogs.setDeletionIndicator(0L);
		dbArchiveLogs.setCreatedBy(loginUserId);
		dbArchiveLogs.setUpdatedBy(loginUserId);
		dbArchiveLogs.setCreatedOn(new Date());
		dbArchiveLogs.setUpdatedOn(new Date());
		return archivesLogsRepository.save(dbArchiveLogs);
	}
	
	/**
	 * updateArchiveLogs
	 * @param archivesLogsId
	 * @param loginUserId 
	 * @param updateArchiveLogs
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ArchiveLogs updateArchiveLogs (String partnerId, String loginUserId, UpdateArchiveLogs updateArchiveLogs)
			throws IllegalAccessException, InvocationTargetException {
		ArchiveLogs dbArchiveLogs = getArchiveLogs(partnerId);
		BeanUtils.copyProperties(updateArchiveLogs, dbArchiveLogs, CommonUtils.getNullPropertyNames(updateArchiveLogs));
		dbArchiveLogs.setUpdatedBy(loginUserId);
		dbArchiveLogs.setUpdatedOn(new Date());
		return archivesLogsRepository.save(dbArchiveLogs);
	}
	
	/**
	 * deleteArchiveLogs
	 * @param loginUserID 
	 * @param archiveslogsCode
	 */
	public void deleteArchiveLogs (String partnerId, String loginUserID) {
		ArchiveLogs archiveslogs = getArchiveLogs(partnerId);
		if (archiveslogs != null) {
			archiveslogs.setDeletionIndicator(1L);
			archiveslogs.setUpdatedBy(loginUserID);
			archiveslogs.setUpdatedOn(new Date());
			archivesLogsRepository.save(archiveslogs);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnerId);
		}
	}
}
