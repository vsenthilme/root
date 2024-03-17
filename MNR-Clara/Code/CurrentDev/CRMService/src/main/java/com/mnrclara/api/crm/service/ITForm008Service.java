package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm008;
import com.mnrclara.api.crm.repository.ITForm008Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm008Service {
	
	@Autowired
	ITForm008Repository itForm008Repository;
	
	/**
	 * getITForm008s
	 * @return
	 */
	public List<ITForm008> getITForm008s () {
		return itForm008Repository.findAll();
	}
	
	/**
	 * getITForm008
	 * @param itFormNo2 
	 * @param itForm006Id
	 * @return
	 */
	public ITForm008 getITForm008 (String language, Long classID, String matterNumber, String clientId, String itFormNo, Long itFormID) {
		String id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		log.info("getITForm008 : " + id);
		return itForm008Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm008
	 * @param newITForm008
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm008 createITForm008 (ITForm008 newITForm008, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm008.setId(newITForm008.getId());
		newITForm008.setCreatedBy(loginUserID);
		newITForm008.setUpdatedBy(loginUserID);
		newITForm008.setCreatedOn(new Date());
		newITForm008.setUpdatedOn(new Date());
		log.info("newITForm008 : " + newITForm008);
		return itForm008Repository.save(newITForm008);
	}
	
	/**
	 * updateITForm008
	 * @param loginUserID 
	 * @param itForm006Code
	 * @param updateITForm008
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm008 updateITForm008 (ITForm008 modifiedITForm008, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm008.getId());
		ITForm008 dbITForm008 = itForm008Repository.findById(modifiedITForm008.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm008 = mapper.convertValue(modifiedITForm008, ITForm008.class);
		dbITForm008.setId(modifiedITForm008.getId());
		
		log.info("Tag : " + dbITForm008);
		dbITForm008.setUpdatedBy(loginUserID);
		dbITForm008.setUpdatedOn(new Date());
		return itForm008Repository.save(dbITForm008);
	}
}