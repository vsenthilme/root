package com.mnrclara.api.management.service;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.model.dto.AddClientUser;
import com.mnrclara.api.management.model.dto.AuditLog;
import com.mnrclara.api.management.model.dto.BillingMode;
import com.mnrclara.api.management.model.dto.CaseCategory;
import com.mnrclara.api.management.model.dto.CaseSubcategory;
import com.mnrclara.api.management.model.dto.ClientCategory;
import com.mnrclara.api.management.model.dto.ClientUser;
import com.mnrclara.api.management.model.dto.DocumentTemplate;
import com.mnrclara.api.management.model.dto.Referral;
import com.mnrclara.api.management.model.dto.TimekeeperCode;
import com.mnrclara.api.management.model.dto.UserProfile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetupService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		return restTemplate;
	}

	private String getSetupServiceApiUrl() {
		return propertiesConfig.getSetupServiceUrl();
	}
	
	//--------------------------ClientUser-------------------------------------------------------------
	// GET
	public ClientUser getClientUser (String clientUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "clientUser/" + clientUserId);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientUser> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientUser.class);
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
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
		
	/**
	 * getNextNumberRange
	 * 
	 * @param classID
	 * @param numberRangeCode
	 * @param authToken
	 * @return
	 */
	public String getNextNumberRange(Long classID, Long numberRangeCode, String authToken) {
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
	 * 
	 * @param userId
	 * @param authToken
	 * @return
	 */
	public String getLanguageId(String userId, String authToken) {
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
	
	/**
	 * getLanguageIdFromIntakeIDMaster
	 * @param intakeFormId
	 * @param authToken
	 * @return
	 */
	public String getLanguageIdFromIntakeIDMaster(Long intakeFormId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "intakeForm/" + intakeFormId + "/lang");
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
	public UserProfile getUserProfile(String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "userProfile/" + userId);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, UserProfile.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public DocumentTemplate getDocumentTemplate(String documentNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "documentTemplate/" + documentNumber);
			ResponseEntity<DocumentTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DocumentTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param timekeeperCode
	 * @param authToken
	 * @return
	 */
	public TimekeeperCode getTimekeeperCode(String timekeeperCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "timekeeperCode/" + timekeeperCode);
			ResponseEntity<TimekeeperCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, TimekeeperCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param clientCategoryId
	 * @param authToken
	 * @return
	 */
	public ClientCategory getClientCategory (Long clientCategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "clientCategory/" + clientCategoryId);
					
			ResponseEntity<ClientCategory> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param caseCategoryId
	 * @param authToken
	 * @return
	 */
	public CaseCategory getCaseCategory(Long caseCategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory/" + caseCategoryId);
			ResponseEntity<CaseCategory> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, CaseCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
			return null;
		}
	}
	
	/**
	 * getCaseSubcategory
	 * @param caseSubcategoryId
	 * @param authToken
	 * @return
	 */
	public CaseSubcategory getCaseSubcategory(Long caseSubcategoryId, String languageId, Long classId, Long caseCategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "caseSubcategory/" + caseSubcategoryId)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("caseCategoryId", caseCategoryId);
					
			ResponseEntity<CaseSubcategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, CaseSubcategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
			return null;
		}
	}
	
	// --------------------BillModeID--------------------------------------------------------------------------
	public BillingMode[] getBillingModes(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "billingMode");
			ResponseEntity<BillingMode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, BillingMode[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param billingModeId
	 * @param authToken
	 * @return
	 */
	public BillingMode getBillingMode (Long billingModeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getSetupServiceApiUrl() + "billingMode/" + billingModeId);
			ResponseEntity<BillingMode> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, BillingMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//---------------------Client User-------------------------------------------------------------------------
	// POST
	public ClientUser createClientUser (AddClientUser newClientUser, String loginUserID, String authToken) {
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

	// --------------------AUDITLOG----------------------------------------------------------------------------

	/**
	 * createAuditLog
	 * 
	 * @param auditLog
	 * @param authToken
	 * @param authToken2
	 */
	private void createAuditLog(AuditLog auditLog, String loginUserID, String authToken) {
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
	 * 
	 * @param loginUserID
	 * @param pkField
	 * @param tableName
	 * @param modifiedField
	 * @param oldValue
	 * @param newValue
	 * @param authToken
	 */
	public void createAuditLogRecord(String loginUserID, String pkField, Long classId, String tableName,
			String modifiedField, String oldValue, String newValue, String authToken) {
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
		auditLog.setTransactionNo(pkField);

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
