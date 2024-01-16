package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm007;
import com.mnrclara.api.crm.repository.ITForm007Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm007Service {
	
	@Autowired
	ITForm007Repository itForm007Repository;
	
	/**
	 * getITForm007s
	 * @return
	 */
	public List<ITForm007> getITForm007s () {
		return itForm007Repository.findAll();
	}
	
	/**
	 * getITForm007
	 * @param itFormNo2 
	 * @param itForm006Id
	 * @return
	 */
	public ITForm007 getITForm007 (String language, Long classID, String matterNumber, String clientId, String itFormNo, Long itFormID) {
		String id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		log.info("getITForm007 : " + id);
		return itForm007Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm007
	 * @param newITForm007
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm007 createITForm007 (ITForm007 newITForm007, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm007.setId(newITForm007.getId());
		newITForm007.setCreatedBy(loginUserID);
		newITForm007.setUpdatedBy(loginUserID);
		newITForm007.setCreatedOn(new Date());
		newITForm007.setUpdatedOn(new Date());
		log.info("newITForm007 : " + newITForm007);
		return itForm007Repository.save(newITForm007);
	}
	
	/**
	 * updateITForm007
	 * @param loginUserID 
	 * @param itForm006Code
	 * @param updateITForm007
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm007 updateITForm007 (ITForm007 modifiedITForm007, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm007.getId());
		ITForm007 dbITForm007 = itForm007Repository.findById(modifiedITForm007.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm007 = mapper.convertValue(modifiedITForm007, ITForm007.class);
		dbITForm007.setId(modifiedITForm007.getId());
		
		log.info("Tag : " + dbITForm007);
		dbITForm007.setUpdatedBy(loginUserID);
		dbITForm007.setUpdatedOn(new Date());
		return itForm007Repository.save(dbITForm007);
	}
}