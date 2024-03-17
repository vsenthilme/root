package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnrclara.api.crm.model.itform.ITForm002;
import com.mnrclara.api.crm.model.itform.ITForm002Att;
import com.mnrclara.api.crm.repository.ITForm002AttRepository;
import com.mnrclara.api.crm.repository.ITForm002Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ITForm002Service {
	
	@Autowired
	ITForm002Repository itForm002Repository;
	
	@Autowired
	ITForm002AttRepository itForm002AttRepository;
	
	/**
	 * getITForm002s
	 * @return
	 */
	public List<ITForm002> getITForm002s () {
		return itForm002Repository.findAll();
	}
	
	/**
	 * getITForm002
	 * @param inquiryNo
	 * @param classID
	 * @param language
	 * @param itFormNo
	 * @param itFormID
	 * @return
	 */
	public ITForm002 getITForm002 (String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return itForm002Repository.findById(id).orElse(null);
	}
	
	/**
	 * createITForm002
	 * @param newITForm002
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm002 createITForm002 (ITForm002 newITForm002, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + newITForm002.getId());
		newITForm002.setId(newITForm002.getId());
		newITForm002.setCreatedBy(loginUserID);
		newITForm002.setUpdatedBy(loginUserID);
		newITForm002.setCreatedOn(new Date());
		newITForm002.setUpdatedOn(new Date());
		return itForm002Repository.save(newITForm002);
	}
	
	/**
	 * 
	 * @param modifiedITForm002
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ITForm002 updateITForm002 (ITForm002 modifiedITForm002, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("ID value: " + modifiedITForm002.getId());
		ITForm002 dbITForm002 = itForm002Repository.findById(modifiedITForm002.getId()).orElse(null);
		
		ObjectMapper mapper = new ObjectMapper();
		dbITForm002 = mapper.convertValue(modifiedITForm002, ITForm002.class);
		dbITForm002.setId(modifiedITForm002.getId());
		
		log.info("Tag : " + dbITForm002);
		dbITForm002.setUpdatedBy(loginUserID);
		dbITForm002.setUpdatedOn(new Date());
		return itForm002Repository.save(dbITForm002);
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
	public ITForm002Att getITForm002Att(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID) {
		String id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID + ":" + "ITForm002Att";
		log.info("ID------------> : " + id);
		return itForm002AttRepository.findById(id).orElse(null);
	}

	/**
	 * 
	 * @param newITForm002Att
	 * @param loginUserID
	 * @return
	 * @throws Exception
	 */
	public ITForm002Att createITForm002Att(@Valid ITForm002Att newITForm002Att, String loginUserID) throws Exception {
		newITForm002Att.setId(newITForm002Att.getId("ITForm002Att"));
		newITForm002Att.setCreatedBy(loginUserID);
		newITForm002Att.setUpdatedBy(loginUserID);
		newITForm002Att.setCreatedOn(new Date());
		newITForm002Att.setUpdatedOn(new Date());
		ITForm002Att createdITForm002Att =  itForm002AttRepository.save(newITForm002Att);
		log.info("createdITForm003Att-------->: " + createdITForm002Att);
		return createdITForm002Att;
	}
}