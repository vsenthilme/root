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

import com.iweb2b.api.master.model.monitoring.AddSystemMonitoringDeck;
import com.iweb2b.api.master.model.monitoring.SystemMonitoringDeck;
import com.iweb2b.api.master.model.monitoring.UpdateSystemMonitoringDeck;
import com.iweb2b.api.master.repository.SystemMonitoringDeckRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemMonitoringDeckService {
	
	@Autowired
	private SystemMonitoringDeckRepository systemMonitoringDeckRepository;
	
	public List<SystemMonitoringDeck> getSystemMonitoringDeck () {
		List<SystemMonitoringDeck> systemMonitoringDeckList =  systemMonitoringDeckRepository.findAll();
		systemMonitoringDeckList = systemMonitoringDeckList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return systemMonitoringDeckList;
	}
	
	/**
	 * getSystemMonitoringDeck
	 * @param systemMonitoringDeckId
	 * @return
	 */
	public SystemMonitoringDeck getSystemMonitoringDeck (String systemMonitoringDeckId) {
		Optional<SystemMonitoringDeck> systemMonitoringDeck = 
				systemMonitoringDeckRepository.findByPartnerIdAndDeletionIndicator(systemMonitoringDeckId, 0L);
		if (systemMonitoringDeck.isEmpty()) {
			return null;
		}
		return systemMonitoringDeck.get();
	}
	
	/**
	 * createSystemMonitoringDeck
	 * @param newSystemMonitoringDeck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SystemMonitoringDeck createSystemMonitoringDeck (AddSystemMonitoringDeck newSystemMonitoringDeck, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		SystemMonitoringDeck dbSystemMonitoringDeck = new SystemMonitoringDeck();
		BeanUtils.copyProperties(newSystemMonitoringDeck, dbSystemMonitoringDeck, CommonUtils.getNullPropertyNames(newSystemMonitoringDeck));
		dbSystemMonitoringDeck.setDeletionIndicator(0L);
		dbSystemMonitoringDeck.setCreatedBy(loginUserId);
		dbSystemMonitoringDeck.setUpdatedBy(loginUserId);
		dbSystemMonitoringDeck.setCreatedOn(new Date());
		dbSystemMonitoringDeck.setUpdatedOn(new Date());
		return systemMonitoringDeckRepository.save(dbSystemMonitoringDeck);
	}
	
	/**
	 * updateSystemMonitoringDeck
	 * @param systemMonitoringDeckId
	 * @param loginUserId 
	 * @param updateSystemMonitoringDeck
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SystemMonitoringDeck updateSystemMonitoringDeck (String partnerId, String loginUserId, UpdateSystemMonitoringDeck updateSystemMonitoringDeck)
			throws IllegalAccessException, InvocationTargetException {
		SystemMonitoringDeck dbSystemMonitoringDeck = getSystemMonitoringDeck(partnerId);
		BeanUtils.copyProperties(updateSystemMonitoringDeck, dbSystemMonitoringDeck, CommonUtils.getNullPropertyNames(updateSystemMonitoringDeck));
		dbSystemMonitoringDeck.setUpdatedBy(loginUserId);
		dbSystemMonitoringDeck.setUpdatedOn(new Date());
		return systemMonitoringDeckRepository.save(dbSystemMonitoringDeck);
	}
	
	/**
	 * deleteSystemMonitoringDeck
	 * @param loginUserID 
	 * @param systemmonitoringdeckCode
	 */
	public void deleteSystemMonitoringDeck (String partnerId, String loginUserID) {
		SystemMonitoringDeck systemmonitoringdeck = getSystemMonitoringDeck(partnerId);
		if (systemmonitoringdeck != null) {
			systemmonitoringdeck.setDeletionIndicator(1L);
			systemmonitoringdeck.setUpdatedBy(loginUserID);
			systemmonitoringdeck.setUpdatedOn(new Date());
			systemMonitoringDeckRepository.save(systemmonitoringdeck);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnerId);
		}
	}
}
