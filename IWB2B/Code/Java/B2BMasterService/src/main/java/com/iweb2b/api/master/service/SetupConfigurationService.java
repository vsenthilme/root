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

import com.iweb2b.api.master.model.setupconfiguration.AddSetupConfiguration;
import com.iweb2b.api.master.model.setupconfiguration.SetupConfiguration;
import com.iweb2b.api.master.model.setupconfiguration.UpdateSetupConfiguration;
import com.iweb2b.api.master.repository.SetupConfigurationRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetupConfigurationService {
	
	@Autowired
	private SetupConfigurationRepository setupConfigurationRepository;
	
	public List<SetupConfiguration> getSetupConfiguration () {
		List<SetupConfiguration> setupConfigurationList =  setupConfigurationRepository.findAll();
		setupConfigurationList = setupConfigurationList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return setupConfigurationList;
	}
	
	/**
	 * getSetupConfiguration
	 * @param setupConfigurationId
	 * @return
	 */
	public SetupConfiguration getSetupConfiguration (String setupConfigurationId) {
		Optional<SetupConfiguration> setupConfiguration = 
				setupConfigurationRepository.findByPartnerIdAndDeletionIndicator(setupConfigurationId, 0L);
		if (setupConfiguration.isEmpty()) {
			return null;
		}
		return setupConfiguration.get();
	}
	
	/**
	 * createSetupConfiguration
	 * @param newSetupConfiguration
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SetupConfiguration createSetupConfiguration (AddSetupConfiguration newSetupConfiguration, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		SetupConfiguration dbSetupConfiguration = new SetupConfiguration();
		BeanUtils.copyProperties(newSetupConfiguration, dbSetupConfiguration, CommonUtils.getNullPropertyNames(newSetupConfiguration));
		dbSetupConfiguration.setDeletionIndicator(0L);
		dbSetupConfiguration.setCreatedBy(loginUserId);
		dbSetupConfiguration.setUpdatedBy(loginUserId);
		dbSetupConfiguration.setCreatedOn(new Date());
		dbSetupConfiguration.setUpdatedOn(new Date());
		return setupConfigurationRepository.save(dbSetupConfiguration);
	}
	
	/**
	 * updateSetupConfiguration
	 * @param setupConfigurationId
	 * @param loginUserId 
	 * @param updateSetupConfiguration
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public SetupConfiguration updateSetupConfiguration (String partnerId, String loginUserId, UpdateSetupConfiguration updateSetupConfiguration)
			throws IllegalAccessException, InvocationTargetException {
		SetupConfiguration dbSetupConfiguration = getSetupConfiguration(partnerId);
		BeanUtils.copyProperties(updateSetupConfiguration, dbSetupConfiguration, CommonUtils.getNullPropertyNames(updateSetupConfiguration));
		dbSetupConfiguration.setUpdatedBy(loginUserId);
		dbSetupConfiguration.setUpdatedOn(new Date());
		return setupConfigurationRepository.save(dbSetupConfiguration);
	}
	
	/**
	 * deleteSetupConfiguration
	 * @param loginUserID 
	 * @param setupconfigurationCode
	 */
	public void deleteSetupConfiguration (String setupconfigurationModuleId, String loginUserID) {
		SetupConfiguration setupconfiguration = getSetupConfiguration(setupconfigurationModuleId);
		if (setupconfiguration != null) {
			setupconfiguration.setDeletionIndicator(1L);
			setupconfiguration.setUpdatedBy(loginUserID);
			setupconfiguration.setUpdatedOn(new Date());
			setupConfigurationRepository.save(setupconfiguration);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + setupconfigurationModuleId);
		}
	}
}
