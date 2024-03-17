package com.mnrclara.api.crm.service;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.auditlog.AuditLog;
import com.mnrclara.api.crm.model.dto.Agreement;
import com.mnrclara.api.crm.model.dto.ClientUser;
import com.mnrclara.api.crm.model.dto.InquiryMode;
import com.mnrclara.api.crm.model.dto.Referral;
import com.mnrclara.api.crm.model.dto.UserProfile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetupService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getSetupServiceApiUrl() {
		return propertiesConfig.getSetupServiceUrl();
	}
	
	// Class
	public com.mnrclara.api.crm.model.dto.Class getClassId (Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "class/" + classId);
			ResponseEntity<com.mnrclara.api.crm.model.dto.Class> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					com.mnrclara.api.crm.model.dto.Class.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Referral
	public Referral getReferralId (Long referralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "referral/" + referralId);
			ResponseEntity<Referral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Referral.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public InquiryMode getInquiryMode (String inquiryModeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode/" + inquiryModeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InquiryMode> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InquiryMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * getNextNumberRange
	 * @param classID
	 * @param numberRangeCode
	 * @param authToken
	 * @return
	 */
	public String getNextNumberRange (Long classID, Long numberRangeCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "numberRange/nextNumberRange").queryParam("classID", classID)
					.queryParam("numberRangeCode", numberRangeCode);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * getLanguageId
	 * @param userId
	 * @param authToken
	 * @return
	 */
	public String getLanguageId (String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "userProfile/" + userId + "/lang");
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public UserProfile getUserProfile (String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "userProfile/" + userId);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					UserProfile.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Agreement getAgreementURL (String agreementCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode + "/documentUrl");
			ResponseEntity<Agreement> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					Agreement.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//---------------------Client User-------------------------------------------------------------------------
	// POST
	public ClientUser createClientUser (ClientUser newClientUser, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		HttpEntity<?> entity = new HttpEntity<>(newClientUser, headers);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "clientUser")
				.queryParam("loginUserID", loginUserID);
		ResponseEntity<ClientUser> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientUser.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	//--------------------AUDIT LOG----------------------------------------------------------------------------

	/**
	 * createAuditLog
	 * @param auditLog
	 * @param authToken
	 * @param authToken2 
	 */
	private void createAuditLog (AuditLog auditLog, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(auditLog, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<AuditLog> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
					AuditLog.class);
			log.info("result : " + result.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * createAuditLogRecord
	 * @param loginUserID
	 * @param pkField
	 * @param tableName
	 * @param modifiedField
	 * @param oldValue
	 * @param newValue
	 * @param authToken
	 */
	public void createAuditLogRecord(String loginUserID, String pkField, Long classId, String tableName, String modifiedField,
			String oldValue, String newValue, String authToken) {
		AuditLog auditLog = new AuditLog();

		// LANG_ID
		String langID = getLanguageId(loginUserID, authToken);
		auditLog.setLanguageId(langID);

		// CLASS_ID
		auditLog.setClassId(classId); // Insert a Value '03

		// AUD_LOG_NO
		/*
		 * During Save, Pass CLASS_ID=03, NUM_RAN_CODE=03 in NUMBERRANGE table and Fetch
		 * NUM_RAN_CURRENT values and add +1 and then insert
		 */
		Long classID = 3L;
		Long numberRangeCode = 3L;
		String newAuditLogNumber = getNextNumberRange(classID, numberRangeCode, authToken);
		log.info("nextVal for AuditLogNumber : " + newAuditLogNumber);
		auditLog.setAuditLogNumber(newAuditLogNumber);

		// TRANS_ID
		auditLog.setTransactionId(1L); // Insert a Value '01

		// TRANS_NO
		auditLog.setTransactionNo(pkField); // INQ_NO (PK)

		// TABLE_NM
		auditLog.setModifiedTableName(tableName); // Ex: INQUIRY

		// MOD_FIELD
		auditLog.setModifiedField(modifiedField);

		// OLD_VL
		auditLog.setOldValue(oldValue);

		// NEW_VL
		auditLog.setNewValue(newValue);
		
		// CTD_BY
		auditLog.setCreatedBy(loginUserID);

		// UTD_BY
		auditLog.setUpdatedBy(loginUserID);

		// UTD_ON
		auditLog.setUpdatedOn(new Date());

		createAuditLog(auditLog, loginUserID, authToken);
	}
}
