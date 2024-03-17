package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm001;
import com.mnrclara.api.crm.repository.ITForm001Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm001Service {
	
	@Autowired
	ITForm001Repository itForm001Repository;
	
	/**
	 * getITForm001s
	 * @return
	 */
	public List<ITForm001> getITForm001s () {
		return itForm001Repository.findAll();
	}
	
	/**
	 * getITForm001
	 * @param itFormID 
	 * @param itFormNo 
	 * @param itForm001Id
	 * @return
	 */
	public ITForm001 getITForm001 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		// "100003:2:EN:30000003:1"
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return itForm001Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm001
	 * @param newITForm001
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm001 createITForm001 (ITForm001 newITForm001, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + newITForm001.getId());
		newITForm001.setCreatedBy(loginUserID);
		newITForm001.setUpdatedBy(loginUserID);
		newITForm001.setCreatedOn(new Date());
		newITForm001.setUpdatedOn(new Date());
		return itForm001Repository.save(newITForm001);
	}
	
	/**
	 * 
	 * @param modifiedITForm001
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm001 updateITForm001 (ITForm001 modifiedITForm001, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm001.getId());
		ITForm001 dbITForm001 = itForm001Repository.findById(modifiedITForm001.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm001 = mapper.convertValue(modifiedITForm001, ITForm001.class);
		dbITForm001.setId(modifiedITForm001.getId());
		
		log.info("Tag : " + dbITForm001);
		dbITForm001.setUpdatedBy(loginUserID);
		dbITForm001.setUpdatedOn(new Date());
		return itForm001Repository.save(dbITForm001);
	}
}