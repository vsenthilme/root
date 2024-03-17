package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm011;
import com.mnrclara.api.crm.repository.ITForm011Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm011Service {
	
	@Autowired
	ITForm011Repository itForm011Repository;
	
	/**
	 * getITForm011s
	 * @return
	 */
	public List<ITForm011> getITForm011s () {
		return itForm011Repository.findAll();
	}
	
	/**
	 * getITForm011
	 * @param itForm011Id
	 * @return
	 */
	public ITForm011 getITForm011 (String language, Long classID, String matterNumber, String clientId, String itFormNo, Long itFormID) {
		String id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		log.info("getITForm011 : " + id);
		return itForm011Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm011
	 * @param newITForm011
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm011 createITForm011 (ITForm011 newITForm011, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm011.setId(newITForm011.getId());
		newITForm011.setCreatedBy(loginUserID);
		newITForm011.setUpdatedBy(loginUserID);
		newITForm011.setCreatedOn(new Date());
		newITForm011.setUpdatedOn(new Date());
		log.info("newITForm011 : " + newITForm011);
		return itForm011Repository.save(newITForm011);
	}
	
	/**
	 * updateITForm011
	 * @param loginUserID 
	 * @param itForm011Code
	 * @param updateITForm011
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm011 updateITForm011 (ITForm011 modifiedITForm011, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm011.getId());
		ITForm011 dbITForm011 = itForm011Repository.findById(modifiedITForm011.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm011 = mapper.convertValue(modifiedITForm011, ITForm011.class);
		dbITForm011.setId(modifiedITForm011.getId());
		
		log.info("Tag : " + dbITForm011);
		dbITForm011.setUpdatedBy(loginUserID);
		dbITForm011.setUpdatedOn(new Date());
		return itForm011Repository.save(dbITForm011);
	}
}