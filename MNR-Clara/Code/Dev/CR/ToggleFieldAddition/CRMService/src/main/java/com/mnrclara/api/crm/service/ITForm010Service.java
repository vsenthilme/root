package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm010;
import com.mnrclara.api.crm.repository.ITForm010Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm010Service {
	
	@Autowired
	ITForm010Repository itForm010Repository;
	
	/**
	 * getITForm010s
	 * @return
	 */
	public List<ITForm010> getITForm010s () {
		return itForm010Repository.findAll();
	}
	
	/**
	 * getITForm010
	 * @param itForm010Id
	 * @return
	 */
	public ITForm010 getITForm010 (String language, Long classID, String matterNumber, String clientId, String itFormNo, Long itFormID) {
		String id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		log.info("getITForm010 : " + id);
		return itForm010Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm010
	 * @param newITForm010
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm010 createITForm010 (ITForm010 newITForm010, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm010.setId(newITForm010.getId());
		newITForm010.setCreatedBy(loginUserID);
		newITForm010.setUpdatedBy(loginUserID);
		newITForm010.setCreatedOn(new Date());
		newITForm010.setUpdatedOn(new Date());
		log.info("newITForm010 : " + newITForm010);
		return itForm010Repository.save(newITForm010);
	}
	
	/**
	 * updateITForm010
	 * @param loginUserID 
	 * @param itForm010Code
	 * @param updateITForm010
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm010 updateITForm010 (ITForm010 modifiedITForm010, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm010.getId());
		ITForm010 dbITForm010 = itForm010Repository.findById(modifiedITForm010.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm010 = mapper.convertValue(modifiedITForm010, ITForm010.class);
		dbITForm010.setId(modifiedITForm010.getId());
		
		log.info("Tag : " + dbITForm010);
		dbITForm010.setUpdatedBy(loginUserID);
		dbITForm010.setUpdatedOn(new Date());
		return itForm010Repository.save(dbITForm010);
	}
}