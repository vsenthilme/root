package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm005;
import com.mnrclara.api.crm.repository.ITForm005Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm005Service {
	
	@Autowired
	ITForm005Repository itForm005Repository;
	
	/**
	 * getITForm005s
	 * @return
	 */
	public List<ITForm005> getITForm005s () {
		return itForm005Repository.findAll();
	}
	
	/**
	 * getITForm005
	 * @param itForm005Id
	 * @return
	 */
	public ITForm005 getITForm005 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return itForm005Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm005
	 * @param newITForm005
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm005 createITForm005 (ITForm005 newITForm005, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm005.setId(newITForm005.getId());
		newITForm005.setCreatedBy(loginUserID);
		newITForm005.setUpdatedBy(loginUserID);
		newITForm005.setCreatedOn(new Date());
		newITForm005.setUpdatedOn(new Date());
		return itForm005Repository.save(newITForm005);
	}
	
	/**
	 * updateITForm005
	 * @param itForm005Code
	 * @param modifiedITForm005
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm005 updateITForm005 (ITForm005 modifiedITForm005, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm005.getId());
		ITForm005 dbITForm005 = itForm005Repository.findById(modifiedITForm005.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm005 = mapper.convertValue(modifiedITForm005, ITForm005.class);
		dbITForm005.setId(modifiedITForm005.getId());
		
		log.info("Tag : " + dbITForm005);
		dbITForm005.setUpdatedBy(loginUserID);
		dbITForm005.setUpdatedOn(new Date());
		return itForm005Repository.save(dbITForm005);
	}
}