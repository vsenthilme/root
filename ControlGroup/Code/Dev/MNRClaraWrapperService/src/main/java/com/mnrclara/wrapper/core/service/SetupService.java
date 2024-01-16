package com.mnrclara.wrapper.core.service;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
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

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.User;
import com.mnrclara.wrapper.core.model.management.QBSync;
import com.mnrclara.wrapper.core.model.setup.ActivityCode;
import com.mnrclara.wrapper.core.model.setup.AdminCost;
import com.mnrclara.wrapper.core.model.setup.AgreementTemplate;
import com.mnrclara.wrapper.core.model.setup.AuditLog;
import com.mnrclara.wrapper.core.model.setup.BillingFormat;
import com.mnrclara.wrapper.core.model.setup.BillingFrequency;
import com.mnrclara.wrapper.core.model.setup.BillingMode;
import com.mnrclara.wrapper.core.model.setup.CaseCategory;
import com.mnrclara.wrapper.core.model.setup.CaseSubcategory;
import com.mnrclara.wrapper.core.model.setup.ChartOfAccounts;
import com.mnrclara.wrapper.core.model.setup.ClientCategory;
import com.mnrclara.wrapper.core.model.setup.ClientType;
import com.mnrclara.wrapper.core.model.setup.ClientUser;
import com.mnrclara.wrapper.core.model.setup.Company;
import com.mnrclara.wrapper.core.model.setup.DeadlineCalculator;
import com.mnrclara.wrapper.core.model.setup.DocCheckList;
import com.mnrclara.wrapper.core.model.setup.DocumentTemplate;
import com.mnrclara.wrapper.core.model.setup.*;
import com.mnrclara.wrapper.core.model.setup.EMail;
import com.mnrclara.wrapper.core.model.setup.ExpenseCode;
import com.mnrclara.wrapper.core.model.setup.ExpirationDocType;
import com.mnrclara.wrapper.core.model.setup.GlMappingMaster;
import com.mnrclara.wrapper.core.model.setup.InquiryMode;
import com.mnrclara.wrapper.core.model.setup.IntakeFormID;
import com.mnrclara.wrapper.core.model.setup.Language;
import com.mnrclara.wrapper.core.model.setup.Message;
import com.mnrclara.wrapper.core.model.setup.NoteType;
import com.mnrclara.wrapper.core.model.setup.Notification;
import com.mnrclara.wrapper.core.model.setup.NumberRange;
import com.mnrclara.wrapper.core.model.setup.Referral;
import com.mnrclara.wrapper.core.model.setup.Screen;
import com.mnrclara.wrapper.core.model.setup.SearchClientUser;
import com.mnrclara.wrapper.core.model.setup.SearchDocCheckList;
import com.mnrclara.wrapper.core.model.setup.Status;
import com.mnrclara.wrapper.core.model.setup.TaskType;
import com.mnrclara.wrapper.core.model.setup.TaskbasedCode;
import com.mnrclara.wrapper.core.model.setup.TimekeeperCode;
import com.mnrclara.wrapper.core.model.setup.Transaction;
import com.mnrclara.wrapper.core.model.setup.UpdateDocCheckList;
import com.mnrclara.wrapper.core.model.setup.UserProfile;
import com.mnrclara.wrapper.core.model.setup.UserRole;
import com.mnrclara.wrapper.core.model.setup.UserType;

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
	
	private String getSetupServiceApiUrl () {
		return propertiesConfig.getSetupServiceUrl();
	}
	
	/**
	 * 
	 * @param userId
	 * @param password
	 * @param authToken
	 * @return
	 */
	public UserProfile validateUser (String userId, String password, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login")
			        .queryParam("userId", userId)
			        .queryParam("password", password);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public ClientUser verifyOtp(String contactNumber, Long otp, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/clientUser/verifyOTP")
			        .queryParam("contactNumber", contactNumber)
			        .queryParam("otp", otp);
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
	
	// sentOTP
	public Boolean sendOTP(String contactNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/clientUser/sendOTP")
			        .queryParam("contactNumber", contactNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Boolean> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// EMAIL-OTP
	public boolean emailOTP(String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/emailOTP")
			        .queryParam("userId", userId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Boolean> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// VERIFY-EMAILOTP
	public UserProfile verifyEmailOTP(String userId, Long otp, String authToken) {
		try {
			//
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/verifyEmailOTP")
			        .queryParam("userId", userId)
			        .queryParam("otp", otp);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// EMAIL-OTP
	public boolean clientUserEmailOTP (String emailId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/clientUser/emailOTP")
			        .queryParam("emailId", emailId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Boolean> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// CLIENt-USER - VERIFY EMAIL OTP
	public ClientUser clientUserVerifyEmailOTP(String emailId, Long otp, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "login/clientUser/verifyEmailOTP")
			        .queryParam("emailId", emailId)
			        .queryParam("otp", otp);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientUser> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientUser.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// GET ALL
	public ActivityCode[] getActivityCodes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode");

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ActivityCode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ActivityCode[].class);
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
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode/" + activityCodeId);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ActivityCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ActivityCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ActivityCode createActivityCode (ActivityCode newActivityCode, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode")
				.queryParam("loginUserID", loginUserID);

		HttpEntity<?> entity = new HttpEntity<>(newActivityCode, headers);
		ResponseEntity<ActivityCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ActivityCode.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// PATCH
	public ActivityCode updateActivityCode (String activityCodeId, String loginUserID, ActivityCode modifiedActivityCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedActivityCode, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode/" + activityCodeId)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<ActivityCode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ActivityCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteActivityCode (String activityCodeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "activityCode/" + activityCodeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------ADMINCOST-----------------------------------------------------------------------
	// GET ALL
	public AdminCost[] getAdminCosts (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "adminCost");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AdminCost[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AdminCost[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public AdminCost getAdminCost (Long adminCost, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "adminCost/" + adminCost);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AdminCost> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AdminCost.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public AdminCost createAdminCost (AdminCost newAdminCost, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "adminCost")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newAdminCost, headers);
			ResponseEntity<AdminCost> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AdminCost.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// PATCH
	public AdminCost updateAdminCost (Long adminCost, String loginUserID, AdminCost modifiedAdminCost, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedAdminCost, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "adminCost/" + adminCost)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<AdminCost> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, AdminCost.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteAdminCost (Long adminCost, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "adminCost/" + adminCost)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------AGREEMENT-TEMPLATE-----------------------------------------------------------------------
	// GET ALL
	public AgreementTemplate[] getAgreementTemplates (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AgreementTemplate[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AgreementTemplate[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public AgreementTemplate getAgreementTemplate (String agreementCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AgreementTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AgreementTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET "/agreementTemplate/{agreementCode}/documentUrl"
	public AgreementTemplate getAgreementTemplateDocument (String agreementCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode + "/documentUrl");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AgreementTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AgreementTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET "/agreementTemplate/{agreementCode}/classId/{classId]"
	public AgreementTemplate getAgreementTemplateClassId (String agreementCode, Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode)
					.queryParam("classId", classId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AgreementTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AgreementTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public AgreementTemplate createAgreementTemplate (AgreementTemplate newAgreementTemplate, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newAgreementTemplate, headers);
			ResponseEntity<AgreementTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AgreementTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// PATCH
	public AgreementTemplate updateAgreementTemplate (String agreementCode, String loginUserID, AgreementTemplate modifiedAgreementTemplate, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedAgreementTemplate, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<AgreementTemplate> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, AgreementTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteAgreementTemplate (String agreementCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "agreementTemplate/" + agreementCode)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------------AUDIT-LOG-------------------------------------------------------
	// GET ALL
	public AuditLog[] getAuditLogs (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AuditLog[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AuditLog[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public AuditLog getAuditLog (String auditLogId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog/" + auditLogId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AuditLog> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AuditLog.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public AuditLog createAuditLog (AuditLog newAuditLog, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newAuditLog, headers);
			ResponseEntity<AuditLog> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AuditLog.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public AuditLog updateAuditLog (String auditLogId, String loginUserID, AuditLog modifiedAuditLog, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedAuditLog, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog/" + auditLogId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<AuditLog> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, AuditLog.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteAuditLog (String auditLogId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "auditLog/" + auditLogId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------------BILLING-FORMAT-------------------------------------------------------
	// GET ALL
	public BillingFormat[] getBillingFormats (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingFormat[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormat[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public BillingFormat getBillingFormat (Long agreementCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat/" + agreementCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingFormat> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormat.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public BillingFormat createBillingFormat (BillingFormat newBillingFormat, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newBillingFormat, headers);
			ResponseEntity<BillingFormat> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFormat.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public BillingFormat updateBillingFormat (Long billingFormatId, String loginUserID, BillingFormat modifiedBillingFormat, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFormat, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat/" + billingFormatId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingFormat> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFormat.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteBillingFormat (Long billingFormatId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFormat/" + billingFormatId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------------BILLING-FREQUENCY-------------------------------------------------------
	// GET ALL
	public BillingFrequency[] getBillingFrequencies (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFrequency");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingFrequency[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequency[].class);
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
	
	// POST
	public BillingFrequency createBillingFrequency (BillingFrequency newBillingFrequency, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFrequency")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newBillingFrequency, headers);
			ResponseEntity<BillingFrequency> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFrequency.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public BillingFrequency updateBillingFrequency (Long billingFrequencyId, String loginUserID, BillingFrequency modifiedBillingFrequency, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFrequency, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFrequency/" + billingFrequencyId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingFrequency> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFrequency.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteBillingFrequency (Long billingFrequencyId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingFrequency/" + billingFrequencyId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------------BILLING-MODE-------------------------------------------------------
	// GET ALL
	public BillingMode[] getBillingModes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingMode");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BillingMode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingMode[].class);
			log.info("result : " + result.getStatusCode());
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
	
	// POST
	public BillingMode createBillingMode (BillingMode newBillingMode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingMode")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newBillingMode, headers);
			ResponseEntity<BillingMode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public BillingMode updateBillingMode (Long billingModeId, String loginUserID, BillingMode modifiedBillingMode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingMode, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingMode/" + billingModeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingMode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteBillingMode (Long billingModeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "billingMode/" + billingModeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------CASE-CATEGORY--------------------------------------------------------------------------
	// GET ALL
	public CaseCategory[] getCaseCategories (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseCategory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CaseCategory[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
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
	
	// GET - "/caseCategory/{classId}/classId"
	public CaseCategory[] getCaseCategoryByClassId(Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory/" + classId + "/classId");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseCategory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CaseCategory[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public CaseCategory createCaseCategory (CaseCategory newCaseCategory, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newCaseCategory, headers);
			ResponseEntity<CaseCategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CaseCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public CaseCategory updateCaseCategory (Long caseCategoryId, String loginUserID, CaseCategory modifiedCaseCategory, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedCaseCategory, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory/" + caseCategoryId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<CaseCategory> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CaseCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteCaseCategory (Long caseCategoryId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "caseCategory/" + caseCategoryId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	 //-------------------------CASE-SUB-CATEGORY-------------------------------------------------------------------------------
	// GET ALL
	public CaseSubcategory[] getCaseSubcategories (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/caseSubcategory");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseSubcategory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CaseSubcategory[].class);
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
	
	// POST
	public CaseSubcategory createCaseSubcategory (CaseSubcategory newCaseSubcategory, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/caseSubcategory")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newCaseSubcategory, headers);
			ResponseEntity<CaseSubcategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CaseSubcategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public CaseSubcategory updateCaseSubcategory (String languageId, Long classId, Long caseCategoryId, 
			Long caseSubcategoryId, String loginUserID, CaseSubcategory modifiedCaseSubcategory, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);			
			HttpEntity<?> entity = new HttpEntity<>(modifiedCaseSubcategory, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/caseSubcategory/" + caseSubcategoryId)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("caseCategoryId", caseCategoryId)					
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<CaseSubcategory> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CaseSubcategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteCaseSubcategory (String languageId, Long classId, Long caseCategoryId, 
			Long caseSubcategoryId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/caseSubcategory/" + caseSubcategoryId)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("caseCategoryId", caseCategoryId)					
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------CHART-OF-ACCOUNTS-------------------------------------------------------------------------
	// GET ALL
	public ChartOfAccounts[] getChartOfAccountsList (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/chartOfAccounts");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ChartOfAccounts[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ChartOfAccounts[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ChartOfAccounts getChartOfAccounts (Long accountNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/chartOfAccounts/" + accountNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ChartOfAccounts> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ChartOfAccounts.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ChartOfAccounts createChartOfAccounts (ChartOfAccounts newChartOfAccounts, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/chartOfAccounts")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newChartOfAccounts, headers);
			ResponseEntity<ChartOfAccounts> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ChartOfAccounts.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ChartOfAccounts updateChartOfAccounts (String accountNumber, String loginUserID, ChartOfAccounts modifiedChartOfAccounts, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedChartOfAccounts, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/chartOfAccounts/" + accountNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ChartOfAccounts> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ChartOfAccounts.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteChartOfAccounts (String accountNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/chartOfAccounts/" + accountNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------CLASS----------------------------------------------------------------------------------
	// GET ALL
	public com.mnrclara.wrapper.core.model.setup.Class[] getClasses (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<com.mnrclara.wrapper.core.model.setup.Class[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, com.mnrclara.wrapper.core.model.setup.Class[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// GET
	public com.mnrclara.wrapper.core.model.setup.Class getClass (Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class/" + classId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<com.mnrclara.wrapper.core.model.setup.Class> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, com.mnrclara.wrapper.core.model.setup.Class.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// POST
	public com.mnrclara.wrapper.core.model.setup.Class createClass (com.mnrclara.wrapper.core.model.setup.Class newChartOfAccounts, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newChartOfAccounts, headers);
			ResponseEntity<com.mnrclara.wrapper.core.model.setup.Class> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, com.mnrclara.wrapper.core.model.setup.Class.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// UPDATE
	public com.mnrclara.wrapper.core.model.setup.Class updateClass (Long classId, String loginUserID, com.mnrclara.wrapper.core.model.setup.Class modifiedChartOfAccounts, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedChartOfAccounts, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class/" + classId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<com.mnrclara.wrapper.core.model.setup.Class> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, com.mnrclara.wrapper.core.model.setup.Class.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteClass (Long classId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/class/" + classId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//-------------------------------ClientCategory----------------------------------------------------------------------------------
	// GET ALL
	public ClientCategory[] getClientCategories (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientCategory");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientCategory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientCategory[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ClientCategory getClientCategory (Long clientCategoryId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientCategory/" + clientCategoryId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientCategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ClientCategory createClientCategory (ClientCategory clientCategory, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientCategory")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientCategory, headers);
			ResponseEntity<ClientCategory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ClientCategory updateClientCategory (Long clientCategoryId, String loginUserID, ClientCategory modifiedClientCategory, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedClientCategory, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientCategory/" + clientCategoryId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientCategory> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientCategory.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteClientCategory (Long clientCategoryId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientCategory/" + clientCategoryId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------ClientType----------------------------------------------------------------------------------
	// GET ALL
	public ClientType[] getClientTypes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientType");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientType[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ClientType getClientType (Long clientTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientType/" + clientTypeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ClientType createClientType (ClientType clientCategory, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientType")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientCategory, headers);
			ResponseEntity<ClientType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ClientType updateClientType (Long clientTypeId, String loginUserID, ClientType modifiedClientType, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedClientType, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientType/" + clientTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteClientType (Long clientTypeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientType/" + clientTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------ClientUser----------------------------------------------------------------------------------
	// GET ALL
	public ClientUser[] getClientUsers (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientUser[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientUser[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ClientUser getClientUser (Long clientUserId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/" + clientUserId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientUser> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientUser.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// FIND
	public ClientUser[] findClientUser(SearchClientUser searchClientUser, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/findClientUser");
			HttpEntity<?> entity = new HttpEntity<>(searchClientUser, headers);
			ResponseEntity<ClientUser[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientUser[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	// FIND-New
	public ClientUser[] findClientUserNew(FindClientUser findClientUser, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/findClientUserNew");
			HttpEntity<?> entity = new HttpEntity<>(findClientUser, headers);
			ResponseEntity<ClientUser[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientUser[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// sendEmail
	public Boolean sendEmail (@Valid EMail eMail, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/sendToClient");
			HttpEntity<?> entity = new HttpEntity<>(eMail, headers);
			ResponseEntity<Boolean> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}
	
	// POST
	public ClientUser createClientUser (ClientUser clientCategory, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientCategory, headers);
			ResponseEntity<ClientUser> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientUser.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ClientUser updateClientUser (Long clientUserId, String loginUserID, ClientUser modifiedClientUser, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedClientUser, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/" + clientUserId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientUser> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientUser.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteClientUser (Long clientUserId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/clientUser/" + clientUserId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Company----------------------------------------------------------------------------------
	// GET ALL
	public Company[] getCompanies (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/company");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Company[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Company[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Company getCompany (String companyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/company/" + companyId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Company> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Company.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Company createCompany (Company company, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/company")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(company, headers);
			ResponseEntity<Company> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Company.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Company updateCompany (String companyId, String loginUserID, Company modifiedCompany, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedCompany, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/company/" + companyId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Company> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Company.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteCompany (String companyId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/company/" + companyId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------DeadlineCalculator----------------------------------------------------------------------------------
	// GET ALL
	public DeadlineCalculator[] getDeadlineCalculators (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/deadlineCalculator");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DeadlineCalculator[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DeadlineCalculator[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DeadlineCalculator getDeadlineCalculator (Long deadlineCalculationId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/deadlineCalculator/" + deadlineCalculationId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DeadlineCalculator> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DeadlineCalculator.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public DeadlineCalculator createDeadlineCalculator (DeadlineCalculator DeadlineCalculator, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/deadlineCalculator")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(DeadlineCalculator, headers);
			ResponseEntity<DeadlineCalculator> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DeadlineCalculator.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public DeadlineCalculator updateDeadlineCalculator (Long deadlineCalculationId, String loginUserID, DeadlineCalculator modifiedDeadlineCalculator, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedDeadlineCalculator, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/deadlineCalculator/" + deadlineCalculationId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<DeadlineCalculator> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DeadlineCalculator.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteDeadlineCalculator (Long deadlineCalculatorDays, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/deadlineCalculator/" + deadlineCalculatorDays)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------DocumentTemplate----------------------------------------------------------------------------------
	// GET ALL
	public DocumentTemplate[] getDocumentTemplates (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/documentTemplate");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DocumentTemplate[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DocumentTemplate[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DocumentTemplate getDocumentTemplate (String documentNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/documentTemplate/" + documentNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DocumentTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DocumentTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public DocumentTemplate createDocumentTemplate (DocumentTemplate DocumentTemplate, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/documentTemplate")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(DocumentTemplate, headers);
			ResponseEntity<DocumentTemplate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DocumentTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public DocumentTemplate updateDocumentTemplate (String loginUserID, DocumentTemplate modifiedDocumentTemplate, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedDocumentTemplate, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/documentTemplate")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<DocumentTemplate> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DocumentTemplate.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteDocumentTemplate (DocumentTemplateCompositeKey key, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(key, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/documentTemplate")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------ExpenseCode----------------------------------------------------------------------------------
	// GET ALL
	public ExpenseCode[] getExpenseCodes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expenseCode");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ExpenseCode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpenseCode[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ExpenseCode getExpenseCode (String expenseCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expenseCode/" + expenseCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ExpenseCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpenseCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ExpenseCode createExpenseCode (ExpenseCode ExpenseCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expenseCode")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(ExpenseCode, headers);
			ResponseEntity<ExpenseCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ExpenseCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ExpenseCode updateExpenseCode (String expenseCode, String loginUserID, ExpenseCode modifiedExpenseCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedExpenseCode, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expenseCode/" + expenseCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ExpenseCode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ExpenseCode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteExpenseCode (String expenseCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expenseCode/" + expenseCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------InquiryMode----------------------------------------------------------------------------------
	// GET ALL
	public InquiryMode[] getInquiryModes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InquiryMode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InquiryMode[].class);
			log.info("result : " + result.getStatusCode());
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
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode/" + inquiryModeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InquiryMode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InquiryMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public InquiryMode createInquiryMode (InquiryMode InquiryMode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(InquiryMode, headers);
			ResponseEntity<InquiryMode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InquiryMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public InquiryMode updateInquiryMode (String inquiryModeId, String loginUserID, 
			InquiryMode modifiedInquiryMode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedInquiryMode, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode/" + inquiryModeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<InquiryMode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, InquiryMode.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteInquiryMode (String inquiryModeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/inquiryMode/" + inquiryModeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------IntakeFormID----------------------------------------------------------------------------------
	// GET ALL
	public IntakeFormID[] getIntakeFormIDs (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<IntakeFormID[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, IntakeFormID[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public IntakeFormID getIntakeFormID (String intakeFormId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm/" + intakeFormId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<IntakeFormID> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, IntakeFormID.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public IntakeFormID getIntakeFormByClassIdAndClientTypeId(Long classId, Long clientTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm/classId")
					.queryParam("classId", classId)
			        .queryParam("clientTypeId", clientTypeId);
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<IntakeFormID> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, IntakeFormID.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public IntakeFormID createIntakeFormID (IntakeFormID IntakeFormID, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(IntakeFormID, headers);
			ResponseEntity<IntakeFormID> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, IntakeFormID.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public IntakeFormID updateIntakeFormID (String intakeFormId, String loginUserID, 
			IntakeFormID modifiedIntakeFormID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedIntakeFormID, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm/" + intakeFormId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<IntakeFormID> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, IntakeFormID.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteIntakeFormID (String intakeFormId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/intakeForm/" + intakeFormId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Language----------------------------------------------------------------------------------
	// GET ALL
	public Language[] getLanguages (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/language");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Language[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Language[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Language getLanguage (String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/language/" + languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Language> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Language.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Language createLanguage (Language Language, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/language")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Language, headers);
			ResponseEntity<Language> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Language.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Language updateLanguage (String languageId, String loginUserID, Language modifiedLanguage, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedLanguage, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/language/" + languageId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Language> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Language.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteLanguage (String languageId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/language/" + languageId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Message----------------------------------------------------------------------------------
	// GET ALL
	public Message[] getMessages (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/message");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Message[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Message[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Message getMessage (String messageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/message/" + messageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Message> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Message.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Message createMessage (Message Message, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/message")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Message, headers);
			ResponseEntity<Message> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Message.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Message updateMessage (String messageId, String loginUserID, Message modifiedMessage, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedMessage, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/message/" + messageId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Message> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Message.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteMessage (String messageId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/message/" + messageId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------NoteType----------------------------------------------------------------------------------
	// GET ALL
	public NoteType[] getNoteTypes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/noteType");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<NoteType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NoteType[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public NoteType getNoteType (String noteTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/noteType/" + noteTypeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<NoteType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NoteType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public NoteType createNoteType (NoteType NoteType, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/noteType")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(NoteType, headers);
			ResponseEntity<NoteType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NoteType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public NoteType updateNoteType (String noteTypeId, String loginUserID, NoteType modifiedNoteType, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedNoteType, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/noteType/" + noteTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<NoteType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, NoteType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteNoteType (String noteTypeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/noteType/" + noteTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	//-------------------------------Notification----------------------------------------------------------------------------------
	// GET ALL
	public Notification[] getNotifications (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/notification");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Notification[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Notification[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Notification getNotification (Long notificationId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/notification/" + notificationId);
					
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Notification> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Notification.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// POST
	public Notification createNotification (Notification Notification, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/notification")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Notification, headers);
			ResponseEntity<Notification> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Notification.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Notification updateNotification (Long notificationId, String loginUserID, 
			Notification modifiedNotification, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedNotification, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/notification/" + notificationId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Notification> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Notification.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteNotification (String notificationId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/notification/" + notificationId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	//-------------------------------NumberRange----------------------------------------------------------------------------------
	// GET ALL
	public NumberRange[] getNumberRanges (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<NumberRange[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public NumberRange getNumberRange (String numberRangeCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange/" + numberRangeCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<NumberRange> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public String getNextNumberRange (Long numberRangeCode, Long classID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange/nextNumberRange")
					.queryParam("numberRangeCode", numberRangeCode)
					.queryParam("classID", classID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public NumberRange createNumberRange (NumberRange NumberRange, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(NumberRange, headers);
			ResponseEntity<NumberRange> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRange.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public NumberRange updateNumberRange (Long numberRangeCode, String loginUserID, NumberRange modifiedNumberRange, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedNumberRange, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange/" + numberRangeCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<NumberRange> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, NumberRange.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteNumberRange (Long numberRangeCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/numberRange/" + numberRangeCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Referral----------------------------------------------------------------------------------
	// GET ALL
	public Referral[] getReferrals (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/referral");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Referral[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Referral[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Referral getReferral (String referralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/referral/" + referralId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Referral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Referral.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Referral createReferral (Referral Referral, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/referral")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Referral, headers);
			ResponseEntity<Referral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Referral.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Referral updateReferral (String languageId, Long classId, Long referralCode, String loginUserID, Referral modifiedReferral, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedReferral, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/referral/" + referralCode)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Referral> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Referral.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteReferral (String languageId, Long classId, Long referralCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/referral/" + referralCode)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Screen----------------------------------------------------------------------------------
	// GET ALL
	public Screen[] getScreens (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/screen");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Screen[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Screen[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Screen getScreen (String screenId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/screen/" + screenId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Screen> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Screen.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Screen createScreen (Screen Screen, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/screen")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Screen, headers);
			ResponseEntity<Screen> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Screen.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Screen updateScreen (Long screenId, String loginUserID, Screen modifiedScreen, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedScreen, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/screen/" + screenId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Screen> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Screen.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteScreen (Long screenId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/screen/" + screenId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Status----------------------------------------------------------------------------------
	// GET ALL
	public Status[] getStatus (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/status");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Status[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Status[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Status getStatus (String statusId, String authToken) {
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
	
	// POST
	public Status createStatus (Status Status, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/status")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Status, headers);
			ResponseEntity<Status> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Status.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Status updateStatus (Long statusId, String loginUserID, Status modifiedStatus, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedStatus, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/status/" + statusId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Status> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Status.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteStatus (Long statusId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/status/" + statusId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------TaskbasedCode----------------------------------------------------------------------------------
	// GET ALL
	public TaskbasedCode[] getTaskbasedCodes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskbasedCode");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TaskbasedCode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TaskbasedCode[].class);
			log.info("result : " + result.getBody());
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
	
	// POST
	public TaskbasedCode createTaskbasedCode (TaskbasedCode TaskbasedCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskbasedCode")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(TaskbasedCode, headers);
			ResponseEntity<TaskbasedCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TaskbasedCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public TaskbasedCode updateTaskbasedCode (String taskCode, String loginUserID, TaskbasedCode modifiedTaskbasedCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedTaskbasedCode, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskbasedCode/" + taskCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<TaskbasedCode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, TaskbasedCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteTaskbasedCode (String taskCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskbasedCode/" + taskCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------TaskType----------------------------------------------------------------------------------
	// GET ALL
	public TaskType[] getTaskTypes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskType");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TaskType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TaskType[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public TaskType getTaskType (Long taskTypeCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskType/" + taskTypeCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TaskType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TaskType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// POST
	public TaskType createTaskType (TaskType TaskType, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskType")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(TaskType, headers);
			ResponseEntity<TaskType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TaskType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	// UPDATE
	public TaskType updateTaskType (Long taskTypeCode, String loginUserID, TaskType modifiedTaskType, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedTaskType, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskType/" + taskTypeCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<TaskType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, TaskType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteTaskType (Long taskTypeCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/taskType/" + taskTypeCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------TimekeeperCode----------------------------------------------------------------------------------
	// GET ALL
	public TimekeeperCode[] getTimekeeperCodes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/timekeeperCode");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TimekeeperCode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TimekeeperCode[].class);
			log.info("result : " + result.getBody());
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
	
	// POST
	public TimekeeperCode createTimekeeperCode (TimekeeperCode TimekeeperCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/timekeeperCode")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(TimekeeperCode, headers);
			ResponseEntity<TimekeeperCode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TimekeeperCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public TimekeeperCode updateTimekeeperCode (String timekeeperCode, String loginUserID, TimekeeperCode updateTimekeeperCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateTimekeeperCode, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/timekeeperCode/" + timekeeperCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<TimekeeperCode> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, TimekeeperCode.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteTimekeeperCode (String timekeeperCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/timekeeperCode/" + timekeeperCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------Transaction----------------------------------------------------------------------------------
	// GET ALL
	public Transaction[] getTransactions (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/transaction");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Transaction[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Transaction[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Transaction getTransaction (String transactionId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/transaction/" + transactionId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Transaction> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Transaction.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Transaction createTransaction (Transaction Transaction, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/transaction")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(Transaction, headers);
			ResponseEntity<Transaction> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Transaction.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Transaction updateTransaction (String transactionId, String loginUserID, Transaction modifiedTransaction, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedTransaction, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/transaction/" + transactionId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Transaction> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Transaction.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteTransaction (String transactionId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/transaction/" + transactionId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------User----------------------------------------------------------------------------------
	// GET ALL
	public User[] getUsers (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<User[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, User[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public User getUser (String id, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user/" + id);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<User> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, User.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public User createUser (User User, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user");
			HttpEntity<?> entity = new HttpEntity<>(User, headers);
			ResponseEntity<User> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, User.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public User updateUser (String id, User modifiedUser, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedUser, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user/" + id);
			ResponseEntity<User> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, User.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void changePassword(String email, String oldPassword, String newPassword, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user/" + email + "/changePassword")
					.queryParam("oldPassword", oldPassword)
					.queryParam("newPassword", newPassword);
			ResponseEntity<User> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, User.class);
			log.info("result : " + result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteUser (String id, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/user/" + id);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------UserProfile----------------------------------------------------------------------------------
	// GET ALL
	public UserProfile[] getUserProfiles (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public UserProfile getUserProfile (String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/" + userId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /userProfile/{userId}/classId
	public Long[] findClassByUserId(String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/" + userId + "/classId");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Long[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Long[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /userProfile/{userId}/lang
	public UserProfile getUserProfileLang(String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/" + userId + "/lang");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /userProfile/classId/{classId}
	public UserProfile getUserProfileByClassId(Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/classId/" + classId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserProfile.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public UserProfile createUserProfile (UserProfile UserProfile, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(UserProfile, headers);
			ResponseEntity<UserProfile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserProfile.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public UserProfile updateUserProfile (String userId, String loginUserID, UserProfile modifiedUserProfile, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedUserProfile, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/" + userId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<UserProfile> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserProfile.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteUserProfile (String userId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userProfile/" + userId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------UserRole----------------------------------------------------------------------------------
	// GET ALL
	public UserRole[] getUserRoles (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserRole-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userRole");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserRole[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserRole[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public UserRole[] getUserRole (Long userRoleId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserRole-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userRole/" + userRoleId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserRole[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserRole[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public UserRole[] createUserRole (List<UserRole> UserRole, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserRole-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userRole")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(UserRole, headers);
			ResponseEntity<UserRole[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserRole[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public UserRole[] updateUserRole (String userRoleId, String loginUserID, 
			@Valid List<UserRole> updateUserRole, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserRole-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateUserRole, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userRole/" + userRoleId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<UserRole[]> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserRole[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteUserRole (String userRoleId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserRole-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userRole/" + userRoleId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-------------------------------UserType----------------------------------------------------------------------------------
	// GET ALL
	public UserType[] getUserTypes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserType-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserType[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public UserType getUserType (String userTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserType-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType/" + userTypeId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
//	public UserType getUserType (String userTypeId, String languageId, Long classId, String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("UserType-Agent", "MNRClara RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType/" + userTypeId)
//					.queryParam("languageId", languageId)
//					.queryParam("classId", classId);
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<UserType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserType.class);
//			log.info("result : " + result.getBody());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	
	// POST
	public UserType createUserType (UserType UserType, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserType-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(UserType, headers);
			ResponseEntity<UserType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public UserType updateUserType (String userTypeId, String loginUserID, UserType modifiedUserType, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserType-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedUserType, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType/" + userTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<UserType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserType.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteUserType (String userTypeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserType-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/userType/" + userTypeId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/* -----------------------------EXPIRATIONDATE---EXPIRATIONDOCTYPE---------------------------------------------------------------*/
	// Get ALL
	public ExpirationDocType[] getExpirationDocTypes (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expirationdoctype");
			ResponseEntity<ExpirationDocType[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpirationDocType[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ExpirationDocType
	public ExpirationDocType getExpirationDocType(String documentType, String languageId, Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expirationdoctype/" + documentType)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId);
			ResponseEntity<ExpirationDocType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpirationDocType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST ExpirationDocType
	public ExpirationDocType addExpirationDocType (ExpirationDocType expirationdoctype, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expirationdoctype")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(expirationdoctype, headers);
			ResponseEntity<ExpirationDocType> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ExpirationDocType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Patch ExpirationDocType
	public ExpirationDocType updateExpirationDocType (String documentType, ExpirationDocType modifiedExpirationDocType, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedExpirationDocType, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expirationdoctype/" + documentType)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<ExpirationDocType> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ExpirationDocType.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Delete ExpirationDocType
	public boolean deleteExpirationDocType (String documentType, String languageId, Long classId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/expirationdoctype/" + documentType)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//--------------------------------------------GLMAPPINGMASTER------------------------------------------------------------------------
	// GET ALL
	public GlMappingMaster [] getGlMappingMasters (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/glmappingmaster");

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<GlMappingMaster []> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, GlMappingMaster[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public GlMappingMaster getGlMappingMaster (Long itemNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/glmappingmaster/" + itemNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<GlMappingMaster> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, GlMappingMaster.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public GlMappingMaster createGlMappingMaster (GlMappingMaster newGlMappingMaster, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/glmappingmaster")
					.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newGlMappingMaster, headers);
		ResponseEntity<GlMappingMaster> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, GlMappingMaster.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// PATCH
	public GlMappingMaster updateGlMappingMaster (Long itemNumber, 
			GlMappingMaster modifiedGlMappingMaster, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedGlMappingMaster, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/glmappingmaster/" + itemNumber)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<GlMappingMaster> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, GlMappingMaster.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteGlMappingMaster (Long itemNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/glmappingmaster/" + itemNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//------------------------------------DocCheckList--------------------------------------------------------------
	// GET ALL
	public DocCheckList [] getDocCheckLists (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DocCheckList[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DocCheckList[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DocCheckList getDocCheckList (String languageId, Long classId, Long checkListNo, 
			Long caseCategoryId, Long caseSubCategoryId, Long sequenceNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist/" + checkListNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("caseCategoryId", caseCategoryId)
					.queryParam("caseSubCategoryId", caseSubCategoryId)
					.queryParam("sequenceNo", sequenceNo);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DocCheckList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DocCheckList.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public DocCheckList[] createDocCheckList (List<DocCheckList> newDocCheckList, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist")
					.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newDocCheckList, headers);
		ResponseEntity<DocCheckList[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DocCheckList[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// PATCH
	public DocCheckList[] updateDocCheckList (List<UpdateDocCheckList> updateDocCheckList, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateDocCheckList, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<DocCheckList[]> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DocCheckList[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteDocCheckList (Long checkListNo, Long sequenceNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist/" + checkListNo)
					.queryParam("sequenceNo", sequenceNo);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// FIND
	public DocCheckList[] findDocCheckLists(SearchDocCheckList searchDocCheckList, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("UserProfile-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getSetupServiceApiUrl() + "/docchecklist/findDocCheckList");
			HttpEntity<?> entity = new HttpEntity<>(searchDocCheckList, headers);
			ResponseEntity<DocCheckList[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DocCheckList[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//------------------------------QBSYNC-------------------------------------------------------------------------
	// POST
	public GlMappingMaster createQbSync(QBSync qbSync, String access_token) {
		// TODO Auto-generated method stub
		return null;
	}
}