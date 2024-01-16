package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.controller.exception.BadRequestException;
import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm003Att;
import com.mnrclara.api.crm.repository.ITForm003AttRepository;
import com.mnrclara.api.crm.repository.ITForm003Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm003Service {
	
	@Autowired
	ITForm003Repository itForm003Repository;
	
	@Autowired
	ITForm003AttRepository itForm003AttRepository;
	
	/**
	 * getITForm003s
	 * @return
	 */
	public List<ITForm003> getITForm003s () {
		return itForm003Repository.findAll();
	}
	
	/**
	 * getITForm003
	 * @param inquiryNo
	 * @param classID
	 * @param language
	 * @param itFormNo
	 * @param itFormID
	 * @return
	 */
	public ITForm003 getITForm003 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID ;
		log.info("ID------------> : " + id);
		return itForm003Repository.findById(id).orElse(null);
	}
	
	/**
	 * 
	 * @param inquiryNo
	 * @param classID
	 * @param language
	 * @param itFormNo
	 * @param itFormID
	 * @return
	 */
	public ITForm003Att getITForm003Att (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID + ":" + "ITForm003Att";
		log.info("ID------------> : " + id);
		return itForm003AttRepository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm003
	 * @param newITForm003
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm003 createITForm003 (ITForm003 newITForm003, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm003.setId(newITForm003.getId());
		newITForm003.setCreatedBy(loginUserID);
		newITForm003.setUpdatedBy(loginUserID);
		newITForm003.setCreatedOn(new Date());
		newITForm003.setUpdatedOn(new Date());
		return itForm003Repository.save(newITForm003);
	}
	
	/**
	 * 
	 * @param newITForm003Att
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm003Att createITForm003Att (ITForm003Att newITForm003Att, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		newITForm003Att.setId(newITForm003Att.getId("ITForm003Att"));
		newITForm003Att.setCreatedBy(loginUserID);
		newITForm003Att.setUpdatedBy(loginUserID);
		newITForm003Att.setCreatedOn(new Date());
		newITForm003Att.setUpdatedOn(new Date());
		ITForm003Att createdITForm003Att =  itForm003AttRepository.save(newITForm003Att);
		log.info("createdITForm003Att-------->: " + createdITForm003Att);
		return createdITForm003Att;
	}
	
	/**
	 * updateITForm003
	 * @param modifiedITForm003
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm003 updateITForm003 (ITForm003 modifiedITForm003, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm003.getId());
		ITForm003 dbITForm003 = itForm003Repository.findById(modifiedITForm003.getId("ITForm003Att")).orElse(null);
		if (dbITForm003 != null) {
			ObjectMapper mapper = new ObjectMapper();
			dbITForm003 = mapper.convertValue(modifiedITForm003, ITForm003.class);
			dbITForm003.setId(modifiedITForm003.getId());
			
			log.info("modifiedITForm003 : " + dbITForm003);
			dbITForm003.setUpdatedBy(loginUserID);
			dbITForm003.setUpdatedOn(new Date());
			ITForm003 updatedITForm003 = itForm003Repository.save(dbITForm003);
			log.info("updatedITForm003 : " + updatedITForm003);
		}
		throw new BadRequestException("Given date not found: " + modifiedITForm003.getId());
	}
}