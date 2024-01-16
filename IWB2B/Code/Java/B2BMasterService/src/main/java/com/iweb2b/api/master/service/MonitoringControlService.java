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

import com.iweb2b.api.master.model.monitoring.AddMonitoringControl;
import com.iweb2b.api.master.model.monitoring.MonitoringControl;
import com.iweb2b.api.master.model.monitoring.UpdateMonitoringControl;
import com.iweb2b.api.master.repository.MonitoringControlRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonitoringControlService {
	
	@Autowired
	private MonitoringControlRepository monitoringControlRepository;
	
	public List<MonitoringControl> getMonitoringControl () {
		List<MonitoringControl> monitoringControlList =  monitoringControlRepository.findAll();
		monitoringControlList = monitoringControlList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return monitoringControlList;
	}
	
	/**
	 * getMonitoringControl
	 * @param partnerId
	 * @return
	 */
	public MonitoringControl getMonitoringControl (String partnerId) {
		Optional<MonitoringControl> monitoringControl = monitoringControlRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (monitoringControl.isEmpty()) {
			return null;
		}
		return monitoringControl.get();
	}
	
	/**
	 * createMonitoringControl
	 * @param newMonitoringControl
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MonitoringControl createMonitoringControl (AddMonitoringControl newMonitoringControl, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		MonitoringControl dbMonitoringControl = new MonitoringControl();
		BeanUtils.copyProperties(newMonitoringControl, dbMonitoringControl, CommonUtils.getNullPropertyNames(newMonitoringControl));
		dbMonitoringControl.setDeletionIndicator(0L);
		dbMonitoringControl.setCreatedBy(loginUserId);
		dbMonitoringControl.setUpdatedBy(loginUserId);
		dbMonitoringControl.setCreatedOn(new Date());
		dbMonitoringControl.setUpdatedOn(new Date());
		return monitoringControlRepository.save(dbMonitoringControl);
	}
	
	/**
	 * updateMonitoringControl
	 * @param partnerId
	 * @param loginUserId 
	 * @param updateMonitoringControl
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MonitoringControl updateMonitoringControl (String partnerId, String loginUserId, UpdateMonitoringControl updateMonitoringControl)
			throws IllegalAccessException, InvocationTargetException {
		MonitoringControl dbMonitoringControl = getMonitoringControl(partnerId);
		BeanUtils.copyProperties(updateMonitoringControl, dbMonitoringControl, CommonUtils.getNullPropertyNames(updateMonitoringControl));
		dbMonitoringControl.setUpdatedBy(loginUserId);
		dbMonitoringControl.setUpdatedOn(new Date());
		return monitoringControlRepository.save(dbMonitoringControl);
	}
	
	/**
	 * deleteMonitoringControl
	 * @param loginUserID 
	 * @param monitoringcontrolModuleId
	 */
	public void deleteMonitoringControl (String monitoringcontrolModuleId, String loginUserID) {
		MonitoringControl monitoringcontrol = getMonitoringControl(monitoringcontrolModuleId);
		if (monitoringcontrol != null) {
			monitoringcontrol.setDeletionIndicator(1L);
			monitoringcontrol.setUpdatedBy(loginUserID);
			monitoringcontrol.setUpdatedOn(new Date());
			monitoringControlRepository.save(monitoringcontrol);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + monitoringcontrolModuleId);
		}
	}
}
