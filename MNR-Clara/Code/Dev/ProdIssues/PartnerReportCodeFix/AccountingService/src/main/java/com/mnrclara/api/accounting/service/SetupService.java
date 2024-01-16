package com.mnrclara.api.accounting.service;

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

import com.mnrclara.api.accounting.config.PropertiesConfig;
import com.mnrclara.api.accounting.model.auditlog.AuditLog;
import com.mnrclara.api.accounting.model.dto.ActivityCode;
import com.mnrclara.api.accounting.model.dto.BillingFormat;
import com.mnrclara.api.accounting.model.dto.BillingFrequency;
import com.mnrclara.api.accounting.model.dto.BillingMode;
import com.mnrclara.api.accounting.model.dto.CaseCategory;
import com.mnrclara.api.accounting.model.dto.CaseSubcategory;
import com.mnrclara.api.accounting.model.dto.Status;
import com.mnrclara.api.accounting.model.dto.TaskbasedCode;
import com.mnrclara.api.accounting.model.dto.TimekeeperCode;

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
	
	// GET
	public com.mnrclara.api.accounting.model.dto.Class getClass (Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class/" + classId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<com.mnrclara.api.accounting.model.dto.Class> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, 
							com.mnrclara.api.accounting.model.dto.Class.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// GET
	public Status getStatus (Long statusId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/status/" + statusId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Status> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Status.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public BillingFormat getBillingFormat (Long billingFormatId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat/" + billingFormatId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingFormat> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormat.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ActivityCode getActivityCode (String activityCodeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode/" + activityCodeId);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ActivityCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ActivityCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public TaskbasedCode getTaskbasedCode (String taskCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskbasedCode/" + taskCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TaskbasedCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TaskbasedCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - CaseCategory
	public CaseCategory getCaseCategory (Long caseCategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory/" + caseCategoryId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseCategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CaseCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public CaseSubcategory getCaseSubcategory (String languageId, Long classId, 
			Long caseCategoryId, Long caseSubcategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/caseSubcategory/" + caseSubcategoryId)
						.queryParam("languageId", languageId)
						.queryParam("classId", classId)
						.queryParam("caseCategoryId", caseCategoryId);
						
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseSubcategory> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CaseSubcategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public TimekeeperCode getTimekeeperCode (String timekeeperCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/timekeeperCode/" + timekeeperCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TimekeeperCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TimekeeperCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public BillingMode getBillingMode (Long billingModeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingMode/" + billingModeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingMode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public BillingFrequency getBillingFrequency (Long billingFrequencyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFrequency/" + billingFrequencyId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingFrequency> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequency.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
