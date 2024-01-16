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

import com.iweb2b.api.master.model.analytics.AddDataTrafficAnalytics;
import com.iweb2b.api.master.model.analytics.DataTrafficAnalytics;
import com.iweb2b.api.master.model.analytics.UpdateDataTrafficAnalytics;
import com.iweb2b.api.master.repository.DataTrafficAnalyticsRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataTrafficAnalyticsService {
	
	@Autowired
	private DataTrafficAnalyticsRepository dataTrafficAnalyticsRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<DataTrafficAnalytics> getDataTrafficAnalytics () {
		List<DataTrafficAnalytics> dataTrafficAnalyticsList =  dataTrafficAnalyticsRepository.findAll();
		dataTrafficAnalyticsList = dataTrafficAnalyticsList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return dataTrafficAnalyticsList;
	}
	
	/**
	 * getDataTrafficAnalytics
	 * @param dataTrafficAnalyticsId
	 * @return
	 */
	public DataTrafficAnalytics getDataTrafficAnalytics (String partnerId) {
		Optional<DataTrafficAnalytics> dataTrafficAnalytics = dataTrafficAnalyticsRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (dataTrafficAnalytics.isEmpty()) {
			return null;
		}
		return dataTrafficAnalytics.get();
	}
	
	/**
	 * createDataTrafficAnalytics
	 * @param newDataTrafficAnalytics
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DataTrafficAnalytics createDataTrafficAnalytics (AddDataTrafficAnalytics newDataTrafficAnalytics, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {
		DataTrafficAnalytics dbDataTrafficAnalytics = new DataTrafficAnalytics();
		BeanUtils.copyProperties(newDataTrafficAnalytics, dbDataTrafficAnalytics, CommonUtils.getNullPropertyNames(newDataTrafficAnalytics));
		dbDataTrafficAnalytics.setDeletionIndicator(0L);
		dbDataTrafficAnalytics.setCreatedBy(loginUserId);
		dbDataTrafficAnalytics.setUpdatedBy(loginUserId);
		dbDataTrafficAnalytics.setCreatedOn(new Date());
		dbDataTrafficAnalytics.setUpdatedOn(new Date());
		return dataTrafficAnalyticsRepository.save(dbDataTrafficAnalytics);
	}
	
	/**
	 * updateDataTrafficAnalytics
	 * @param dataTrafficAnalyticsId
	 * @param loginUserId 
	 * @param updateDataTrafficAnalytics
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public DataTrafficAnalytics updateDataTrafficAnalytics (String partnerId, String loginUserId, 
			UpdateDataTrafficAnalytics updateDataTrafficAnalytics)
			throws IllegalAccessException, InvocationTargetException {
		DataTrafficAnalytics dbDataTrafficAnalytics = getDataTrafficAnalytics(partnerId);
		BeanUtils.copyProperties(updateDataTrafficAnalytics, dbDataTrafficAnalytics, CommonUtils.getNullPropertyNames(updateDataTrafficAnalytics));
		dbDataTrafficAnalytics.setUpdatedBy(loginUserId);
		dbDataTrafficAnalytics.setUpdatedOn(new Date());
		return dataTrafficAnalyticsRepository.save(dbDataTrafficAnalytics);
	}
	
	/**
	 * deleteDataTrafficAnalytics
	 * @param loginUserID 
	 * @param datatrafficanalyticsCode
	 */
	public void deleteDataTrafficAnalytics (String datatrafficanalyticsModuleId, String loginUserID) {
		DataTrafficAnalytics datatrafficanalytics = getDataTrafficAnalytics(datatrafficanalyticsModuleId);
		if (datatrafficanalytics != null) {
			datatrafficanalytics.setDeletionIndicator(1L);
			datatrafficanalytics.setUpdatedBy(loginUserID);
			datatrafficanalytics.setUpdatedOn(new Date());
			dataTrafficAnalyticsRepository.save(datatrafficanalytics);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + datatrafficanalyticsModuleId);
		}
	}
}
