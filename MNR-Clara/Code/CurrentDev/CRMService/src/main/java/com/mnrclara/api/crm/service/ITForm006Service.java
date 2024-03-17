package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm006;
import com.mnrclara.api.crm.repository.ITForm006Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm006Service {
	
	@Autowired
	ITForm006Repository itForm006Repository;
	
	/**
	 * getITForm006s
	 * @return
	 */
	public List<ITForm006> getITForm006s () {
		return itForm006Repository.findAll();
	}
	
	/**
	 * getITForm006
	 * @param itForm006Id
	 * @return
	 */
	public ITForm006 getITForm006 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return itForm006Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm006
	 * @param newITForm006
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm006 createITForm006 (ITForm006 newITForm006, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm006.setId(newITForm006.getId());
		newITForm006.setCreatedBy(loginUserID);
		newITForm006.setUpdatedBy(loginUserID);
		newITForm006.setCreatedOn(new Date());
		newITForm006.setUpdatedOn(new Date());
		return itForm006Repository.save(newITForm006);
	}
	
	/**
	 * updateITForm006
	 * @param loginUserID 
	 * @param itForm006Code
	 * @param updateITForm006
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm006 updateITForm006 (ITForm006 modifiedITForm006, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm006.getId());
		ITForm006 dbITForm006 = itForm006Repository.findById(modifiedITForm006.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm006 = mapper.convertValue(modifiedITForm006, ITForm006.class);
		dbITForm006.setId(modifiedITForm006.getId());
		
		log.info("Tag : " + dbITForm006);
		dbITForm006.setUpdatedBy(loginUserID);
		dbITForm006.setUpdatedOn(new Date());
		return itForm006Repository.save(dbITForm006);
	}
}