package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm009;
import com.mnrclara.api.crm.repository.ITForm009Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm009Service {
	
	@Autowired
	ITForm009Repository itForm009Repository;
	
	/**
	 * getITForm009s
	 * @return
	 */
	public List<ITForm009> getITForm009s () {
		return itForm009Repository.findAll();
	}
	
	/**
	 * getITForm009
	 * @param itForm009Id
	 * @return
	 */
	public ITForm009 getITForm009 (String language, Long classID, String matterNumber, String clientId, String itFormNo, Long itFormID) {
		String id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		log.info("getITForm009 : " + id);
		return itForm009Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm009
	 * @param newITForm009
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm009 createITForm009 (ITForm009 newITForm009, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm009.setId(newITForm009.getId());
		newITForm009.setCreatedBy(loginUserID);
		newITForm009.setUpdatedBy(loginUserID);
		newITForm009.setCreatedOn(new Date());
		newITForm009.setUpdatedOn(new Date());
		log.info("newITForm009 : " + newITForm009);
		return itForm009Repository.save(newITForm009);
	}
	
	/**
	 * updateITForm009
	 * @param loginUserID 
	 * @param itForm009Code
	 * @param updateITForm009
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm009 updateITForm009 (ITForm009 modifiedITForm009, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm009.getId());
		ITForm009 dbITForm009 = itForm009Repository.findById(modifiedITForm009.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm009 = mapper.convertValue(modifiedITForm009, ITForm009.class);
		dbITForm009.setId(modifiedITForm009.getId());
		
		log.info("Tag : " + dbITForm009);
		dbITForm009.setUpdatedBy(loginUserID);
		dbITForm009.setUpdatedOn(new Date());
		return itForm009Repository.save(dbITForm009);
	}
}