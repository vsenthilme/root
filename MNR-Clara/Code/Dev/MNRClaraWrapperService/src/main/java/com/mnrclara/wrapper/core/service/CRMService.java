package com.mnrclara.wrapper.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.model.crm.*;
import com.mnrclara.wrapper.core.model.management.ClientGeneral;
import com.mnrclara.wrapper.core.model.management.MatterTimeTicket;
import com.mnrclara.wrapper.core.util.DateUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
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
import com.mnrclara.wrapper.core.model.accounting.AddInvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.InvoiceHeader;
import com.mnrclara.wrapper.core.model.accounting.SearchInvoiceHeaderInput;
import com.mnrclara.wrapper.core.model.auth.AuthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CRMService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	private String getCRMServiceUrl () {
		return propertiesConfig.getCrmServiceUrl();
	}
	
	//-------------------------------Inquiry----------------------------------------------------------------------------------
	// GET ALL
	public Inquiry[] getInquiries (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Inquiry[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Inquiry[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Inquiry getInquiry (String inquiryNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Inquiry> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /{classId}/count
	public InquiryClassCount getInquiryCount (Long classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + classId + "/count");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InquiryClassCount> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InquiryClassCount.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /inquiry/findInquiry
	public Inquiry[] findInquiries(SearchInquiry searchInquiry, String authToken) throws Exception {
		try {
			if (searchInquiry.getSInqStartDate() != null) {
				searchInquiry.setInqStartDate(DateUtils.convertStringToYYYYMMDD(searchInquiry.getSInqStartDate()));
			}

			if (searchInquiry.getSInqEndDate() != null) {
				searchInquiry.setInqEndDate(DateUtils.convertStringToYYYYMMDD(searchInquiry.getSInqEndDate()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/findInquiry");
			HttpEntity<?> entity = new HttpEntity<>(searchInquiry, headers);
			ResponseEntity<Inquiry[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Inquiry[].class);
			log.info("result : " + result.getBody());

			Inquiry[] arrResponseInquiry = result.getBody();
			List<Inquiry> inquiryValidationList = new ArrayList<>();
			for (Inquiry inquiryValidate : arrResponseInquiry) {
				inquiryValidate.setSInquiryDate(DateUtils.dateToString(inquiryValidate.getInquiryDate()));
				inquiryValidate.setSCreatedOn(DateUtils.dateToString(inquiryValidate.getCreatedOn()));
				inquiryValidationList.add(inquiryValidate);
			}
			Inquiry[] arrInquiry = inquiryValidationList.toArray(new Inquiry[inquiryValidationList.size()]);
			return arrInquiry;
//			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public Inquiry createInquiry (Inquiry inquiry, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(inquiry, headers);
			ResponseEntity<Inquiry> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST for External - /external
	public Inquiry createInquiryWebsite(AddInquiryWebsite newInquiryWebsite, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/external");
			HttpEntity<?> entity = new HttpEntity<>(newInquiryWebsite, headers);
			ResponseEntity<Inquiry> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public Inquiry updateInquiry (String inquiryNumber, String loginUserID, Inquiry modifiedInquiry, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Inquiry> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE - /inquiry/assignInquiry
	public Inquiry assignInquiry(String inquiryNumber, String loginUserID, Inquiry updateInquiry, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/assignInquiry")
					.queryParam("inquiryNumber", inquiryNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Inquiry> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE - /inquiry/{inquiryNumber}/assignInquiry
	public Inquiry updateAssignInquiry(String inquiryNumber, String loginUserID, @Valid Inquiry updateInquiry, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber + "/assignInquiry")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Inquiry> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE - /{inquiryNumber}/updateInquiryIntake
	public Inquiry updateInquiryIntake(String inquiryNumber, String loginUserID, @Valid Inquiry updateInquiry,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber + "/updateInquiryIntake")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Inquiry> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE - /inquiry/{inquiryNumber}/updateValiationStatus
	public Inquiry updateValiationStatus(String inquiryNumber, String loginUserID, @Valid Inquiry updateInquiry,
			Long status, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber + "/updateValiationStatus")
					.queryParam("status", status)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Inquiry> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteInquiry (String inquiryNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/" + inquiryNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//-------------------------------------------INTAKE------------------------------------------------------------------------
	// GET - /{inquiryNumber}
	public Inquiry getInquiryIntake(String inquiryNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/intake/" + inquiryNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Inquiry> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Inquiry.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /inquiry/intake/updateStatus
	public PCIntakeForm afterIntakeFormSent(String inquiryNumber, String loginUserID, @Valid Inquiry updateInquiry, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInquiry, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/intake/updateStatus")
					.queryParam("inquiryNumber", inquiryNumber)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<PCIntakeForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * sendEmail
	 * @param email
	 * @param authToken
	 * @return
	 */
	public String sendEmail (EMail email, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(email, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/inquiry/intake/sendFormThroEmail");
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//----------------------------------PC-INTAKE-FORM----------------------------------------------------------------
	// GET - /pcIntakeForm
	public PCIntakeForm[] getPCIntakeForms(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PCIntakeForm[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PCIntakeForm[].class);
			
			PCIntakeForm[] arrResponsePCIntakeForm = result.getBody();
			List<PCIntakeForm> pcIntakeFormList = new ArrayList<>();
			for (PCIntakeForm pcIntakeForm : arrResponsePCIntakeForm) {
				pcIntakeForm.setApprovedOnString(DateUtils.dateToString(pcIntakeForm.getApprovedOn()));
				pcIntakeForm.setSentOnString(DateUtils.dateToString(pcIntakeForm.getSentOn()));
				pcIntakeForm.setReceivedOnString(DateUtils.dateToString(pcIntakeForm.getReceivedOn()));
				pcIntakeFormList.add(pcIntakeForm);
			}
			PCIntakeForm[] arrPCIntakeForm = pcIntakeFormList.toArray(new PCIntakeForm[pcIntakeFormList.size()]);
			return arrPCIntakeForm;
		} catch (Exception e) {
			throw e;
		}
	}

	// GET - /pcIntakeForm/{intakeFormNumber}
	public PCIntakeForm getPCIntakeForm(String intakeFormNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PCIntakeForm> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /pcIntakeForm/findPCIntakeForm
	public PCIntakeForm[] findPCIntakeForm(SearchPCIntakeForm searchPCIntakeForm,
			String authToken) throws Exception {
		try {
			if (searchPCIntakeForm.getStartApprovedOn() != null) {
				searchPCIntakeForm.setSApprovedOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getStartApprovedOn()));
			}
			
			if (searchPCIntakeForm.getEndApprovedOn() != null) {
				searchPCIntakeForm.setEApprovedOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getEndApprovedOn()));
			}
			
			if (searchPCIntakeForm.getStartReceivedOn() != null) {
				searchPCIntakeForm.setSReceivedOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getStartReceivedOn()));
			}
			
			if (searchPCIntakeForm.getEndReceivedOn() != null) {
				searchPCIntakeForm.setEReceivedOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getEndReceivedOn()));
			}
			
			if (searchPCIntakeForm.getStartSentOn() != null) {
				searchPCIntakeForm.setSSentOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getStartSentOn()));
			}
			
			if (searchPCIntakeForm.getEndSentOn() != null) {
				searchPCIntakeForm.setESentOn(DateUtils.convertStringToYYYYMMDD(searchPCIntakeForm.getEndSentOn()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/findPCIntakeForm");
			HttpEntity<?> entity = new HttpEntity<>(searchPCIntakeForm, headers);
			ResponseEntity<PCIntakeForm[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PCIntakeForm[].class);
			
			PCIntakeForm[] arrResponsePCIntakeForm = result.getBody();
			List<PCIntakeForm> pcIntakeFormList = new ArrayList<>();
			for (PCIntakeForm pcIntakeForm : arrResponsePCIntakeForm) {
				pcIntakeForm.setApprovedOnString(DateUtils.dateToString(pcIntakeForm.getApprovedOn()));
				pcIntakeForm.setSentOnString(DateUtils.dateToString(pcIntakeForm.getSentOn()));
				pcIntakeForm.setReceivedOnString(DateUtils.dateToString(pcIntakeForm.getReceivedOn()));
				pcIntakeFormList.add(pcIntakeForm);
			}
			PCIntakeForm[] arrPCIntakeForm = pcIntakeFormList.toArray(new PCIntakeForm[pcIntakeFormList.size()]);
			return arrPCIntakeForm;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /pcIntakeForm/{intakeFormNumber}/inquiryNumber/{inquiryNumber}
	public PCIntakeForm getPCIntakeForm(String intakeFormNumber, String inquiryNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber + "/inquiryNumber/" + inquiryNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PCIntakeForm> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /pcIntakeForm
	public PCIntakeForm createPCIntakeForm(String inquiryNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm")
					.queryParam("inquiryNumber", inquiryNumber)
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PCIntakeForm> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /pcIntakeForm/{intakeFormNumber}
	public PCIntakeForm updatePCIntakeForm(String intakeFormNumber, @Valid UpdatePCIntakeForm updatePCIntakeForm,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updatePCIntakeForm, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<PCIntakeForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /pcIntakeForm/{intakeFormNumber}/status
	public PCIntakeForm updatePCIntakeFormStatus(String loginUserID, String inquiryNumber, Long intakeFormId,
			String intakeFormNumber, String authToken, UpdatePCIntakeForm updatePCIntakeForm) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updatePCIntakeForm, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber + "/status")
					.queryParam("loginUserID", loginUserID)
					.queryParam("inquiryNumber", inquiryNumber)
					.queryParam("intakeFormId", intakeFormId);
					
			ResponseEntity<PCIntakeForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /pcIntakeForm/{intakeFormNumber}/receive}
	public PCIntakeForm updatePCIntakeFormReceive(String intakeFormNumber, String inquiryNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber + "/receive")
					.queryParam("inquiryNumber", inquiryNumber)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<PCIntakeForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PCIntakeForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /pcIntakeForm/{intakeFormNumber}/approve
	public PotentialClient updatePCIntakeFormApprove(String intakeFormNumber, String inquiryNumber, Long statusId,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber + "/approve")
					.queryParam("inquiryNumber", inquiryNumber)					
					.queryParam("statusId", statusId)
					.queryParam("loginUserID", loginUserID);					
					
			ResponseEntity<PotentialClient> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE - /pcIntakeForm/{intakeFormNumber}/inquiryNumber/{inquiryNumber}
	public boolean deletePCIntakeForm(String intakeFormNumber, String inquiryNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/pcIntakeForm/" + intakeFormNumber + "/inquiryNumber/" + inquiryNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//-----------------------------------POTENTIAL-CLIENT----------------------------------------------------------------
	// GET - /potentialClient
	public PotentialClient[] getPotentialClients(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PotentialClient[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PotentialClient[].class);
			log.info("result : " + result.getBody());
			
			List<PotentialClient> potentialClientList = new ArrayList<>();
			for (PotentialClient potentialClient : result.getBody()) {
				potentialClient.setSCreatedOn(DateUtils.dateToString(potentialClient.getCreatedOn()));
				potentialClient.setSConsultationDate(DateUtils.dateToString(potentialClient.getConsultationDate()));
				potentialClientList.add(potentialClient);
			}
			
			log.info("potentialClientList: " + potentialClientList);
			PotentialClient[] arrPotentialClient = potentialClientList.toArray(new PotentialClient[potentialClientList.size()]);
			return arrPotentialClient;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /potentialClient/{potentialClientId}
	public PotentialClient getPotentialClient(String potentialClientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PotentialClient> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /potentialClient/findPotentialClient
	public PotentialClient[] findPotentialClient(SearchPotentialClient searchPotentialClient, String authToken) throws Exception {
		try {
			SearchPotentialClientInput searchPotentialClientInput = new SearchPotentialClientInput();
			BeanUtils.copyProperties(searchPotentialClient, searchPotentialClientInput, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchPotentialClient));
			
			if (searchPotentialClient.getSCreatedOn() != null) {
				searchPotentialClientInput.setSCreatedOn(DateUtils.convertStringToYYYYMMDD(searchPotentialClient.getSCreatedOn()));
			}
			
			if (searchPotentialClient.getECreatedOn() != null) {
				searchPotentialClientInput.setECreatedOn(DateUtils.convertStringToYYYYMMDD(searchPotentialClient.getECreatedOn()));
			}
			
			if (searchPotentialClient.getSConsultationDate() != null) {
				searchPotentialClientInput.setSConsultationDate(DateUtils.convertStringToYYYYMMDD(searchPotentialClient.getSConsultationDate()));
			}
			
			if (searchPotentialClient.getEConsultationDate() != null) {
				searchPotentialClientInput.setEConsultationDate(DateUtils.convertStringToYYYYMMDD(searchPotentialClient.getEConsultationDate()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/findPotentialClient");
			HttpEntity<?> entity = new HttpEntity<>(searchPotentialClientInput, headers);
			ResponseEntity<PotentialClient[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PotentialClient[].class);
			log.info("result : " + result.getBody());
			
			List<PotentialClient> potentialClientList = new ArrayList<>();
			for (PotentialClient potentialClient : result.getBody()) {
				potentialClient.setSCreatedOn(DateUtils.dateToString(potentialClient.getCreatedOn()));
				potentialClient.setSConsultationDate(DateUtils.dateToString(potentialClient.getConsultationDate()));
				potentialClientList.add(potentialClient);
			}
			
			log.info("potentialClientList: " + potentialClientList);
			PotentialClient[] arrPotentialClient = potentialClientList.toArray(new PotentialClient[potentialClientList.size()]);
			return arrPotentialClient;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /potentialClient
	public PotentialClient createPotentialClient(String loginUserID, @Valid AddPotentialClient newPotentialClient,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newPotentialClient, headers);
			ResponseEntity<PotentialClient> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /potentialClient/{potentialClientId}/clientGeneral
	public ClientGeneral createClientGeneral(String potentialClientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId + "/clientGeneral")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /potentialClient/{potentialClientId}
	public PotentialClient updatePotentialClient(String potentialClientId, String loginUserID,
			@Valid UpdatePotentialClient updatePotentialClient, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updatePotentialClient, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId)
					.queryParam("loginUserID", loginUserID);					
					
			ResponseEntity<PotentialClient> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /potentialClient/{potentialClientId}/agreement
	public PotentialClient updatePotentialClientAgreement(String potentialClientId, String loginUserID,
			@Valid UpdatePotentialClientAgreement updatePotentialClientAgreement, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updatePotentialClientAgreement, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId + "/agreement")
					.queryParam("loginUserID", loginUserID);					
					
			ResponseEntity<PotentialClient> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /potentialClient/{potentialClientId}/status
	public PotentialClient updatePotentialClientStatus(String potentialClientId, String loginUserID, Long status,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId + "/status")
					.queryParam("loginUserID", loginUserID)
					.queryParam("status", status);
					
			ResponseEntity<PotentialClient> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PotentialClient.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE - /potentialClient/{potentialClientId}
	public String deletePotentialClient(String potentialClientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/potentialClient/" + potentialClientId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//----------------------------------AGREEMENT-----------------------------------------------------------------------------
	/**
	 * genToken
	 * @param code
	 * @param authToken
	 * @return
	 */
	public AuthToken genToken (String code, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/docusign/token")
					.queryParam("code", code);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AuthToken> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AuthToken.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /agreement
	public Agreement[] getAgreements(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Agreement[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Agreement[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreement/{agreementCode}
	public Agreement getAgreement(String agreementCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/" + agreementCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Agreement> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Agreement.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /agreement/findAgreement
	public Agreement[] findAgreement(SearchAgreement searchAgreement, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Agreement-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/findAgreement");
			HttpEntity<?> entity = new HttpEntity<>(searchAgreement, headers);
			ResponseEntity<Agreement[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Agreement[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /agreement
	public Agreement createAgreement(AddAgreement addAgreement, String potentialClientId, String loginUserID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement")
					.queryParam("loginUserID", loginUserID)
					.queryParam("potentialClientId", potentialClientId);
			HttpEntity<?> entity = new HttpEntity<>(addAgreement, headers);
			ResponseEntity<Agreement> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Agreement.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /agreement/{agreementCode}
	public Agreement updateAgreement(String agreementCode, @Valid UpdateAgreement updateAgreement, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateAgreement, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/" + agreementCode)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<Agreement> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Agreement.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /agreement/{agreementCode}/fromDocusign
	public Agreement updateAgreementFromDocusignFlow(String agreementCode, 
													String potentialClientId,
													@Valid UpdateAgreement updateAgreement, 
													String loginUserID,
													String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateAgreement, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/" + agreementCode + "/fromDocusign")
					.queryParam("potentialClientId", potentialClientId)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<Agreement> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Agreement.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /agreement/{agreementCode}/status
	public Agreement updateAgreementStatus(String agreementCode, Long status, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/" + agreementCode + "/status")
					.queryParam("status", status)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<Agreement> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Agreement.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE - /agreement/{agreementCode}
	public boolean deleteAgreement(String agreementCode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/" + agreementCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * getDocusignEnvelopeStatus
	 * @param authToken
	 * @param authToken2 
	 * @return
	 */
	public EnvelopeStatus getDocusignEnvelopeStatus (String potentialClientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/docusign/envelope/status")
					.queryParam("potentialClientId", potentialClientId);
			ResponseEntity<EnvelopeStatus> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * downloadEnvelope
	 * @param potentialClientId
	 * @param authToken2 
	 * @return
	 */
	public String downloadEnvelopeFromDocusign (String potentialClientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/agreement/docusign/envelope/download")
					.queryParam("potentialClientId", potentialClientId)
					.queryParam("loginUserID", loginUserID);
					
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//----------------------------------CONFLICT SEARCH-----------------------------------------------------------------------------
	/**
	 * genToken
	 * @param searchTableNames 
	 * @param code
	 * @param authToken
	 * @return
	 */
	public ConflictSearchResult conflictSearch (List<String> searchTableNames, String searchFieldValue, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/conflictCheck/search")
					.queryParam("searchTableNames", searchTableNames)
					.queryParam("searchFieldValue", searchFieldValue);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ConflictSearchResult> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ConflictSearchResult.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Notification[] getAllNotifications (String userId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/notification-message")
					.queryParam("userId", userId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Notification[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Notification[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public Boolean updateNotificationAsRead (String loginUserID,Long id, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/notification-message/mark-read/"+id)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Boolean> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public Boolean updateNotificationAsReadAll (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/notification-message/mark-read-all")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<Boolean> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
