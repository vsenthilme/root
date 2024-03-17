package com.mnrclara.api.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.model.itform.ITForm001;
import com.mnrclara.api.crm.model.itform.ITForm002;
import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm004;
import com.mnrclara.api.crm.model.itform.ITForm005;
import com.mnrclara.api.crm.model.itform.ITForm006;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailMergeService {
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	ITForm001Service itFormm001Service;
	
	@Autowired
	ITForm002Service itFormm002Service;
	
	@Autowired
	ITForm003Service itFormm003Service;
	
	@Autowired
	ITForm004Service itFormm004Service;
	
	@Autowired
	ITForm005Service itFormm005Service;
	
	@Autowired
	ITForm006Service itFormm006Service;
	
	/**
	 * getMailMergeFields
	 * @param potentialClientId
	 * @return
	 */
	public String getWorkNo (String potentialClientId) {
		PotentialClient dbPotentialClient = potentialClientService.getPotentialClient(potentialClientId);
		ITForm001 objITForm001 = getFieldFrom001 (dbPotentialClient);
		ITForm002 objITForm002 = getFieldFrom002 (dbPotentialClient);
		ITForm003 objITForm003 = getFieldFrom003 (dbPotentialClient);
		ITForm004 objITForm004 = getFieldFrom004 (dbPotentialClient);
		ITForm005 objITForm005 = getFieldFrom005 (dbPotentialClient);
		ITForm006 objITForm006 = getFieldFrom006 (dbPotentialClient);
		
		/*
		 * "Pass the selected POT_CLIENT_ID in POTENTIALCLIENT table and fetch IT_FORM_ID/IT_FORM_NO values
			1. If IT_FORM_ID = 001,002,003,004 and 005 ,pass IT_FORM_NO in MongoDB and fetch WORK field value and autofill
			2. If IT_FORM_ID=006, pass IT_FORM_NO in MongoDB and fetch office field value and autofill"
		 */
		if (dbPotentialClient.getIntakeFormId() == 1 && objITForm001 != null) {
			return String.valueOf(objITForm001.getContactNumber().getWorkNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 2 && objITForm002 != null) {
			return String.valueOf(objITForm002.getContactNumber().getWorkNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 3 && objITForm003 != null) {
			return String.valueOf(objITForm003.getContactNumber().getWorkNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 4 && objITForm004 != null) {
			return String.valueOf(objITForm004.getContactNumber().getWorkNo());
		} else if (dbPotentialClient.getIntakeFormId() == 5 && objITForm005 != null) {
			return String.valueOf(objITForm005.getContactNumber().getWorkNo());
		} else if (dbPotentialClient.getIntakeFormId() == 6 && objITForm006 != null) {
			return String.valueOf(objITForm006.getContactNumber().getWorkNo());
		}
		return null;
	}
	
	/**
	 * 
	 * @param potentialClientId
	 * @return
	 */
	public String getHomeNo (String potentialClientId) {
		PotentialClient dbPotentialClient = potentialClientService.getPotentialClient(potentialClientId);
		ITForm001 objITForm001 = getFieldFrom001 (dbPotentialClient);
		ITForm002 objITForm002 = getFieldFrom002 (dbPotentialClient);
		ITForm003 objITForm003 = getFieldFrom003 (dbPotentialClient);
		ITForm004 objITForm004 = getFieldFrom004 (dbPotentialClient);
		ITForm005 objITForm005 = getFieldFrom005 (dbPotentialClient);
		ITForm006 objITForm006 = getFieldFrom006 (dbPotentialClient);
		
		/*
		 * "Pass the selected POT_CLIENT_ID in POTENTIALCLIENT table and fetch IT_FORM_ID/IT_FORM_NO values
			1. If IT_FORM_ID = 001,002,003,004 and 005 ,pass IT_FORM_NO in MongoDB and fetch WORK field value and autofill
			2. If IT_FORM_ID=006, pass IT_FORM_NO in MongoDB and fetch office field value and autofill"
		 */
		if (dbPotentialClient.getIntakeFormId() == 1 && objITForm001 != null) {
			return String.valueOf(objITForm001.getContactNumber().getHomeNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 2 && objITForm002 != null) {
			return String.valueOf(objITForm002.getContactNumber().getHomeNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 3 && objITForm003 != null) {
			return String.valueOf(objITForm003.getContactNumber().getHomeNo()); //Work_Phone
		} else if (dbPotentialClient.getIntakeFormId() == 4 && objITForm004 != null) {
			return String.valueOf(objITForm004.getContactNumber().getHomeNo());
		} else if (dbPotentialClient.getIntakeFormId() == 5 && objITForm005 != null) {
			return String.valueOf(objITForm005.getContactNumber().getHomeNo());
		} else if (dbPotentialClient.getIntakeFormId() == 6 && objITForm006 != null) {
			return String.valueOf(objITForm006.getContactNumber().getHomeNo());
		}
		return null;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm001 getFieldFrom001(PotentialClient dbPotentialClient) {
		ITForm001 objITForm001 = itFormm001Service.getITForm001(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm001;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm002 getFieldFrom002(PotentialClient dbPotentialClient) {
		ITForm002 objITForm002 = itFormm002Service.getITForm002(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm002;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm003 getFieldFrom003(PotentialClient dbPotentialClient) {
		ITForm003 objITForm003 = itFormm003Service.getITForm003(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm003;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm004 getFieldFrom004(PotentialClient dbPotentialClient) {
		ITForm004 objITForm004 = itFormm004Service.getITForm004(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm004;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm005 getFieldFrom005(PotentialClient dbPotentialClient) {
		ITForm005 objITForm005 = itFormm005Service.getITForm005(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm005;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private ITForm006 getFieldFrom006(PotentialClient dbPotentialClient) {
		ITForm006 objITForm006 = itFormm006Service.getITForm006(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		return objITForm006;
	}
}
