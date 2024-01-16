package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm004;
import com.mnrclara.api.crm.repository.ITForm004Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm004Service {
	
	@Autowired
	ITForm004Repository itForm004Repository;
	
	/**
	 * getITForm004s
	 * @return
	 */
	public List<ITForm004> getITForm004s () {
		return itForm004Repository.findAll();
	}
	
	/**
	 * getITForm004
	 * @param inquiryNo
	 * @param classID
	 * @param language
	 * @param itFormNo
	 * @param itFormID
	 * @return
	 */
	public ITForm004 getITForm004 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return itForm004Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm004
	 * @param newITForm004
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm004 createITForm004 (ITForm004 newITForm004, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm004.setId(newITForm004.getId());
		newITForm004.setCreatedBy(loginUserID);
		newITForm004.setUpdatedBy(loginUserID);
		newITForm004.setCreatedOn(new Date());
		newITForm004.setUpdatedOn(new Date());
		return itForm004Repository.save(newITForm004);
	}
	
	/**
	 * updateITForm004
	 * @param loginUserID 
	 * @param itForm004Code
	 * @param updateITForm004
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm004 updateITForm004 (ITForm004 modifiedITForm004, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm004.getId());
		ITForm004 dbITForm004 = itForm004Repository.findById(modifiedITForm004.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm004 = mapper.convertValue(modifiedITForm004, ITForm004.class);
		dbITForm004.setId(modifiedITForm004.getId());
		
		log.info("Tag : " + dbITForm004);
		dbITForm004.setUpdatedBy(loginUserID);
		dbITForm004.setUpdatedOn(new Date());
		return itForm004Repository.save(dbITForm004);
	}
}