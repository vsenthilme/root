package com.mnrclara.wrapper.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.accounting.*;
import com.mnrclara.wrapper.core.model.accounting.report.BilledPaidFeesReportOutput;
import com.mnrclara.wrapper.core.model.report.*;
import com.mnrclara.wrapper.core.util.DateUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
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
import com.mnrclara.wrapper.core.model.management.PaginatedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountingService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	AuthTokenService authTokenService;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getAccountingServiceUrl() {
		return propertiesConfig.getAccountingServiceUrl();
	}

	//--------------------------------------------QUOTATIONHEADER------------------------------------------------------------------------
	// GET ALL
	public QuotationHeader[] getQuotationHeaders (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<QuotationHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, QuotationHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ALL - PAGINATION
	public Page<QuotationHeader> getAllQuotationHeaders(Integer pageNo, Integer pageSize, String sortBy,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getAccountingServiceUrl() + "quotationheader/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<QuotationHeader>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<QuotationHeader>>() {};
			ResponseEntity<PaginatedResponse<QuotationHeader>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public QuotationHeader getQuotationHeader (String quotationNo, Long quotationRevisionNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader/" + quotationNo)
					.queryParam("quotationRevisionNo", quotationRevisionNo);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<QuotationHeader> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, QuotationHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// SEARCH
	public QuotationHeader[] findQuotationHeaders (SearchQuotationHeader searchQuotationHeader, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader/findQuotationHeader");
			HttpEntity<?> entity = new HttpEntity<>(searchQuotationHeader, headers);
			ResponseEntity<QuotationHeader[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QuotationHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public QuotationHeader createQuotationHeader (QuotationHeader newQuotationHeader, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		log.info("URL:" + getAccountingServiceUrl() + "quotationheader");	
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader")
				.queryParam("loginUserID", loginUserID);

		HttpEntity<?> entity = new HttpEntity<>(newQuotationHeader, headers);
		ResponseEntity<QuotationHeader> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QuotationHeader.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// PATCH
	public QuotationHeader updateQuotationHeader (String quotationNo, Long quotationRevisionNo, 
			String loginUserID, QuotationHeader modifiedQuotationHeader, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedQuotationHeader, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader/" + quotationNo)
					.queryParam("quotationRevisionNo", quotationRevisionNo)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<QuotationHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, QuotationHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteQuotationHeader (String quotationNo, Long quotationRevisionNo, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationheader/" + quotationNo)
					.queryParam("quotationRevisionNo", quotationRevisionNo)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//--------------------------------------------QUOTATIONLINE------------------------------------------------------------------------
	
	// GET - /{quotationNo}
	public QuotationLine[] getQuotationLines (String quotationNo, Long quotationRevisionNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationline/" + quotationNo)
					.queryParam("quotationRevisionNo", quotationRevisionNo);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<QuotationLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, QuotationLine[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /{quotationNo}/{quotationRevisionNo}/{serialNo}
	public QuotationLine getQuotationLine (String quotationNo, Long quotationRevisionNo, Long serialNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationline/" + quotationNo + "/" + quotationRevisionNo + "/" + serialNo);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<QuotationLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, QuotationLine.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// PATCH
	public QuotationLine updateQuotationLine (String quotationNo, Long quotationRevisionNo, Long serialNo, 
			QuotationLine modifiedQuotationHeader, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedQuotationHeader, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "quotationline/" + quotationNo)
					.queryParam("quotationRevisionNo", quotationRevisionNo)
					.queryParam("serialNo", serialNo)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<QuotationLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, QuotationLine.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//--------------------------------------------PAYMENTPLANHEADER------------------------------------------------------------------------
	// GET ALL
	public PaymentPlanHeader[] getPaymentPlanHeaders (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PaymentPlanHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentPlanHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ALL - PAGINATION
	public Page<PaymentPlanHeader> getAllPaymentPlanHeaders(Integer pageNo, Integer pageSize, String sortBy,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<PaymentPlanHeader>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<PaymentPlanHeader>>() {};
			ResponseEntity<PaginatedResponse<PaymentPlanHeader>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public PaymentPlanHeader getPaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo);
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<PaymentPlanHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentPlanHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public PaymentPlanHeader[] findPaymentPlanHeader (SearchPaymentPlanHeader searchPaymentPlanHeader, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader/findPaymentPlanHeader");
			HttpEntity<?> entity = new HttpEntity<>(searchPaymentPlanHeader, headers);	
			ResponseEntity<PaymentPlanHeader[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentPlanHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Boolean sendReminderSMS () {
		try {
			log.info("Reminder SMS called : " + new Date());
			com.mnrclara.wrapper.core.model.auth.AuthToken authTokenForAcctService = 
					authTokenService.getAccountingServiceAuthToken();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authTokenForAcctService.getAccess_token());
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline/reminderSMS");
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public PaymentPlanHeader createPaymentPlanHeader (PaymentPlanHeader newPaymentPlanHeader, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newPaymentPlanHeader, headers);
		ResponseEntity<PaymentPlanHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentPlanHeader.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// PATCH
	public PaymentPlanHeader updatePaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo, 
			String loginUserID, PaymentPlanHeader updatePaymentPlanHeader, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updatePaymentPlanHeader, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<PaymentPlanHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentPlanHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deletePaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanheader/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
		
	//--------------------------------------------PAYMENTPLANLINE------------------------------------------------------------------------
	// GET ALL
	public PaymentPlanLine[] getPaymentPlanLines (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PaymentPlanLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentPlanLine[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public PaymentPlanLine getPaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo)
					.queryParam("itemNumber", itemNumber);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PaymentPlanLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentPlanLine.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public PaymentPlanLine createPaymentPlanLine (PaymentPlanLine newPaymentPlanLine, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline")
				.queryParam("loginUserID", loginUserID);

		HttpEntity<?> entity = new HttpEntity<>(newPaymentPlanLine, headers);
		ResponseEntity<PaymentPlanLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentPlanLine.class);
		return result.getBody();
	}
	
	// PATCH
	public PaymentPlanLine updatePaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, String loginUserID, PaymentPlanLine modifiedPaymentPlanLine, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedPaymentPlanLine, headers);
			
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo)
					.queryParam("itemNumber", itemNumber)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<PaymentPlanLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentPlanLine.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deletePaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "paymentplanline/" + paymentPlanNumber)
					.queryParam("paymentPlanRevisionNo", paymentPlanRevisionNo)
					.queryParam("itemNumber", itemNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//-----------------------------------PRE-BILL----------------------------------------------------------------------------
	//--------------------------------------------PreBillDetails------------------------------------------------------------------------
	// GET ALL
	public PreBillDetails[] getPreBillDetails (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PreBillDetails[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PreBillDetails[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public PreBillDetails getPreBillDetails (String preBillNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/" + preBillNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<PreBillDetails> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PreBillDetails.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ALL - PAGINATION
	public Page<PreBillDetails> getAllPreBillDetailss(Integer pageNo, Integer pageSize, String sortBy, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<PreBillDetails>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<PreBillDetails>>() {};
			ResponseEntity<PaginatedResponse<PreBillDetails>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public PreBillDetails[] createPreBillDetails (List<PreBillDetails> newPreBillDetails, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newPreBillDetails, headers);
		ResponseEntity<PreBillDetails[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
				PreBillDetails[].class);
		return result.getBody();
	}
	
	// POST - executeBill
	public MatterTimeExpenseTicket[] executeBill(@Valid BillByGroup newBillByGroup, Boolean isByIndividual,
			String authToken) throws ParseException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/executeBill")
					.queryParam("isByIndividual", isByIndividual);
		
		// Converting String to Date
		BillByGroupInput billByGroupInput = new BillByGroupInput();
		BeanUtils.copyProperties(newBillByGroup, billByGroupInput, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(newBillByGroup));
		billByGroupInput.setPreBillDate(DateUtils.convertStringToYYYYMMDD(newBillByGroup.getPreBillDate()));
		billByGroupInput.setStartDate(DateUtils.convertStringToYYYYMMDD(newBillByGroup.getStartDate()));
		billByGroupInput.setFeesCutoffDate(DateUtils.convertStringToYYYYMMDD(newBillByGroup.getFeesCutoffDate()));
		billByGroupInput.setPaymentCutoffDate(DateUtils.convertStringToYYYYMMDD(newBillByGroup.getPaymentCutoffDate()));
		
		HttpEntity<?> entity = new HttpEntity<>(billByGroupInput, headers);
		ResponseEntity<MatterTimeExpenseTicket[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterTimeExpenseTicket[].class);
		
		List<MatterTimeExpenseTicket> matterTimeExpenseTickeList = new ArrayList<>();
		for (MatterTimeExpenseTicket matterTimeExpenseTicket : result.getBody()) {
			MatterTimeExpenseTicket matterTimeExpenseTicketEntity = new MatterTimeExpenseTicket();
			BeanUtils.copyProperties(matterTimeExpenseTicket, matterTimeExpenseTicketEntity, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(matterTimeExpenseTicket));
			matterTimeExpenseTicketEntity.setSPreBillDate(DateUtils.dateToString(matterTimeExpenseTicket.getPreBillDate()));
			matterTimeExpenseTickeList.add(matterTimeExpenseTicketEntity);
			
			log.info("result--------MatterTimeExpenseTicket---------> : " + matterTimeExpenseTicketEntity );
		}
		
		MatterTimeExpenseTicket[] arrMatterTimeExpenseTicket = matterTimeExpenseTickeList.toArray(new MatterTimeExpenseTicket[matterTimeExpenseTickeList.size()]);
		return arrMatterTimeExpenseTicket;
	}
	
	// SEARCH
	public PreBillDetails[] findPreBillDetails(SearchPreBillDetails searchPreBillDetails, String authToken) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/findPreBillDetails");

			SearchPreBillDetailsInput searchPreBillDetailsInput = new SearchPreBillDetailsInput();
			BeanUtils.copyProperties(searchPreBillDetails, searchPreBillDetailsInput, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchPreBillDetails));
			
			if (searchPreBillDetails.getStartPreBillDate() != null && searchPreBillDetails.getEndPreBillDate() != null) {
				searchPreBillDetailsInput.setStartPreBillDate(DateUtils.convertStringToYYYYMMDD(searchPreBillDetails.getStartPreBillDate()));
				searchPreBillDetailsInput.setEndPreBillDate(DateUtils.convertStringToYYYYMMDD(searchPreBillDetails.getEndPreBillDate()));
			}
			
			HttpEntity<?> entity = new HttpEntity<>(searchPreBillDetailsInput, headers);
			ResponseEntity<PreBillDetailsEntity[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PreBillDetailsEntity[].class);
			
			List<PreBillDetails> preBillDetailsList = new ArrayList<>();
			for (PreBillDetailsEntity preBillDetailsEntity : result.getBody()) {
				PreBillDetails preBillDetails = new PreBillDetails();
				BeanUtils.copyProperties(preBillDetailsEntity, preBillDetails, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(preBillDetailsEntity));
				
				preBillDetails.setStartDateForPreBill(DateUtils.dateToString(preBillDetailsEntity.getStartDateForPreBill()));
				preBillDetails.setFeesCostCutoffDate(DateUtils.dateToString(preBillDetailsEntity.getFeesCostCutoffDate()));
				preBillDetails.setPaymentCutoffDate(DateUtils.dateToString(preBillDetailsEntity.getPaymentCutoffDate()));
				preBillDetails.setPreBillDate(DateUtils.dateToString(preBillDetailsEntity.getPreBillDate()));
				
				log.info("preBillDetailsEntity : " + preBillDetailsEntity);
				log.info("preBillDetails : " + preBillDetails);
				preBillDetailsList.add(preBillDetails);
			}
			
			PreBillDetails[] arrPreBillDetails = preBillDetailsList.toArray(new PreBillDetails[preBillDetailsList.size()]);
			return arrPreBillDetails;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// PATCH
	public PreBillDetails updatePreBillDetails (String preBillBatchNumber, String preBillNumber,  Date preBillDate, String matterNumber, 
			String loginUserID, PreBillDetails modifiedPreBillDetails, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(modifiedPreBillDetails, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/" + preBillNumber)
					.queryParam("preBillBatchNumber", preBillBatchNumber)
					.queryParam("preBillDate", preBillDate)
					.queryParam("matterNumber", matterNumber)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<PreBillDetails> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PreBillDetails.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public PreBillDetails[] approvePreBillDetails(List<PreBillApproveSaveDetails> preBillApproveSaveDetails, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(preBillApproveSaveDetails,headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/approve")
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<PreBillDetails[]> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, PreBillDetails[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SAVE
	public PreBillDetails[] savePreBillDetails(List<PreBillApproveSaveDetails> preBillApproveSaveDetails , String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(preBillApproveSaveDetails,headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/save")
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<PreBillDetails[]> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, PreBillDetails[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deletePreBillDetails (String preBillNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "prebilldetails/" + preBillNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//---------------------------Invoice Header----------------------------------------------------------
	
	// GET
	public InvoiceHeader getLatestInvoice(String authToken) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/top");
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<InvoiceHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceHeader.class);
			InvoiceHeader latestInvoiceHeader = result.getBody();
			return latestInvoiceHeader;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public String getTopByQbQuery(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/top/qbQuery");
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public AddInvoiceHeader[] getInvoiceHeaders(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AddInvoiceHeader[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AddInvoiceHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ALL - PAGINATION
	public Page<AddInvoiceHeader> getAllInvoiceHeaders (Integer pageNo, Integer pageSize, String sortBy,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<AddInvoiceHeader>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<AddInvoiceHeader>>() {};
			ResponseEntity<PaginatedResponse<AddInvoiceHeader>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public AddInvoiceHeader getInvoiceHeader(String invoiceNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/" + invoiceNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<AddInvoiceHeader> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AddInvoiceHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public InvoiceHeader[] findInvoiceHeader(SearchInvoiceHeader searchInvoiceHeader, String authToken) throws ParseException {
		SearchInvoiceHeaderInput searchInvoiceHeaderInput = new SearchInvoiceHeaderInput();
		BeanUtils.copyProperties(searchInvoiceHeader, searchInvoiceHeaderInput, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchInvoiceHeader));
		
		if (searchInvoiceHeader.getStartInvoiceDate() != null) {
			searchInvoiceHeaderInput.setStartInvoiceDate(DateUtils.convertStringToYYYYMMDD(searchInvoiceHeader.getStartInvoiceDate()));
		}
		
		if (searchInvoiceHeader.getEndInvoiceDate() != null) {
			searchInvoiceHeaderInput.setEndInvoiceDate(DateUtils.convertStringToYYYYMMDD(searchInvoiceHeader.getEndInvoiceDate()));
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/findInvoiceHeader");
		HttpEntity<?> entity = new HttpEntity<>(searchInvoiceHeaderInput, headers);
		ResponseEntity<AddInvoiceHeader[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AddInvoiceHeader[].class);
		
		List<InvoiceHeader> createdInvoiceHeaders = new ArrayList<>();
		for (AddInvoiceHeader addInvoiceHeader : result.getBody()) {
			InvoiceHeader invoiceHeader = new InvoiceHeader();
			BeanUtils.copyProperties(addInvoiceHeader, invoiceHeader, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(addInvoiceHeader));
			invoiceHeader.setInvoiceDate(DateUtils.dateToString(addInvoiceHeader.getInvoiceDate()));
			invoiceHeader.setPreBillDate(DateUtils.dateToString(addInvoiceHeader.getPreBillDate()));
			invoiceHeader.setPostingDate(DateUtils.dateToString(addInvoiceHeader.getPostingDate()));
			createdInvoiceHeaders.add(invoiceHeader);
		}
		
		InvoiceHeader[] arrInvoiceHeader = createdInvoiceHeaders.toArray(new InvoiceHeader[createdInvoiceHeaders.size()]);
		return arrInvoiceHeader;
	}
	
	// SEARCH - PAYMENT UPDATE
	public PaymentUpdate[] findPaymentUpdate(SearchPaymentUpdate searchPaymentUpdate, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/findPaymentUpdate");
		HttpEntity<?> entity = new HttpEntity<>(searchPaymentUpdate, headers);
		ResponseEntity<PaymentUpdate[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentUpdate[].class);
		log.info("result : " + result.getBody());
		return result.getBody();
	}
	
	// /invoiceline/{}
	public AddInvoiceLine[] getInvoiceLine(String invoiceNumber, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceline/" + invoiceNumber);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<AddInvoiceLine[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AddInvoiceLine[].class);
		return result.getBody();
	}

	// Invoice Execute
	public InvoicePreBillDetails[] invoiceExecute(SearchPreBillDetails searchPreBillDetails, String authToken) throws ParseException {
		SearchPreBillDetailsInput searchPreBillDetailsInput = new SearchPreBillDetailsInput();
		BeanUtils.copyProperties(searchPreBillDetails, searchPreBillDetailsInput, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchPreBillDetails));
		
		if (searchPreBillDetails.getStartPreBillDate() != null && searchPreBillDetails.getEndPreBillDate() != null) {
			searchPreBillDetailsInput.setStartPreBillDate(DateUtils.convertStringToYYYYMMDD(searchPreBillDetails.getStartPreBillDate()));
			searchPreBillDetailsInput.setEndPreBillDate(DateUtils.convertStringToYYYYMMDD(searchPreBillDetails.getEndPreBillDate()));
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/invoiceExecute");
		HttpEntity<?> entity = new HttpEntity<>(searchPreBillDetailsInput, headers);
		ResponseEntity<InvoicePreBillDetails[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoicePreBillDetails[].class);
		return result.getBody();
	}
	
	// POST
	public InvoiceCreateResponse createInvoiceHeader (@Valid List<InvoiceHeader> newInvoiceHeaders, 
			String loginUserID, String authToken) throws ParseException {
		List<AddInvoiceHeader> addInvoiceHeaders = new ArrayList<>();
		for (com.mnrclara.wrapper.core.model.accounting.InvoiceHeader newInvoiceHeader : newInvoiceHeaders) {
			AddInvoiceHeader addInvoiceHeader = new AddInvoiceHeader();
			BeanUtils.copyProperties(newInvoiceHeader, addInvoiceHeader, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(newInvoiceHeader));
			addInvoiceHeader.setInvoiceDate(DateUtils.convertStringToYYYYMMDD(newInvoiceHeader.getInvoiceDate()));
			addInvoiceHeader.setPreBillDate(DateUtils.convertStringToYYYYMMDD(newInvoiceHeader.getPreBillDate()));
			addInvoiceHeader.setPostingDate(DateUtils.convertStringToYYYYMMDD(newInvoiceHeader.getPostingDate()));
			addInvoiceHeaders.add(addInvoiceHeader);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(addInvoiceHeaders, headers);
//		ResponseEntity<AddInvoiceHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, 
//				entity, AddInvoiceHeader[].class);
		ResponseEntity<InvoiceCreateResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InvoiceCreateResponse.class);
		InvoiceCreateResponse invoiceCreateResponse = result.getBody();
		
		List<InvoiceHeader> createdInvoiceHeaders = new ArrayList<>();
		for (AddInvoiceHeader addInvoiceHeader : invoiceCreateResponse.getCreatedInvoiceHeaders()) {
			InvoiceHeader invoiceHeader = new InvoiceHeader();
			BeanUtils.copyProperties(addInvoiceHeader, invoiceHeader, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(addInvoiceHeader));
			invoiceHeader.setInvoiceDate(DateUtils.dateToString(addInvoiceHeader.getInvoiceDate()));
			invoiceHeader.setPreBillDate(DateUtils.dateToString(addInvoiceHeader.getPreBillDate()));
			invoiceHeader.setPostingDate(DateUtils.dateToString(addInvoiceHeader.getPostingDate()));
			createdInvoiceHeaders.add(invoiceHeader);
		}
		
		InvoiceHeader[] arrInvoiceHeader = createdInvoiceHeaders.toArray(new InvoiceHeader[createdInvoiceHeaders.size()]);
//		return arrInvoiceHeader;
		invoiceCreateResponse.setInvoiceHeaders(createdInvoiceHeaders);
		return invoiceCreateResponse;
	}

	// PATCH 
	public InvoiceHeader updateInvoiceHeader(String invoiceNumber, @Valid InvoiceHeader invoiceHeader,
			String loginUserID, String authToken) throws Exception {
		try {
			UpdateInvoiceHeader updateInvoiceHeader = new UpdateInvoiceHeader();
			BeanUtils.copyProperties(invoiceHeader, updateInvoiceHeader, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(invoiceHeader));
			
			if (invoiceHeader.getInvoiceDate() != null) {
				updateInvoiceHeader.setInvoiceDate(DateUtils.convertStringToYYYYMMDD(invoiceHeader.getInvoiceDate()));
			}
			
			if (invoiceHeader.getPostingDate() != null) {
				updateInvoiceHeader.setPostingDate(DateUtils.convertStringToYYYYMMDD(invoiceHeader.getPostingDate()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(updateInvoiceHeader, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/" + invoiceNumber)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<UpdateInvoiceHeader> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UpdateInvoiceHeader.class);
			
			UpdateInvoiceHeader updatedInvoiceHeaderResponse = result.getBody(); 
			InvoiceHeader updatedInvoiceHeader = new InvoiceHeader();
			BeanUtils.copyProperties(updatedInvoiceHeaderResponse, updatedInvoiceHeader, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(updatedInvoiceHeaderResponse));
			updatedInvoiceHeader.setInvoiceDate(DateUtils.dateToString(updatedInvoiceHeaderResponse.getInvoiceDate()));
			updatedInvoiceHeader.setPostingDate(DateUtils.dateToString(updatedInvoiceHeaderResponse.getPostingDate()));
			
			return updatedInvoiceHeader;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public InvoiceHeader updateInvoiceHeaderQB(String invoiceNumber, Long statusId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/" + invoiceNumber + "/qb")
					.queryParam("statusId", statusId);
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<InvoiceHeader> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InvoiceHeader.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - createPaymentUpdate
	public PaymentUpdate[] createPaymentUpdate(InvoiceRet invoiceRet, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate");
		HttpEntity<?> entity = new HttpEntity<>(invoiceRet, headers);
		ResponseEntity<PaymentUpdate[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentUpdate[].class);
		return result.getBody();
	}
	
	// POST - PaymentUpdate Table
	public PaymentUpdate createPaymentUpdateTable(PaymentUpdate paymentUpdate, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate/create")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(paymentUpdate, headers);
		ResponseEntity<PaymentUpdate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, 
				entity, PaymentUpdate.class);
		return result.getBody();
	}
	
	// PATCH - PaymentUpdate Table
	public PaymentUpdate updatePaymentUpdate(Long paymentId,
											 @Valid PaymentUpdate paymentUpdate, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(paymentUpdate, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate/" + paymentId);
			ResponseEntity<PaymentUpdate> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentUpdate.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET - PaymentUpdate Table
	public PaymentUpdate getPaymentUpdate(Long paymentId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate/" + paymentId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PaymentUpdate> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentUpdate.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// Delete - PaymentUpdate Table
	public PaymentUpdate deletePaymentUpdate(Long paymentId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate/" + paymentId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PaymentUpdate> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, PaymentUpdate.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - createPaymentUpdateByReceivePayment
	public PaymentUpdate[] createPaymentUpdateByReceivePayment(List<ReceivePaymentResponse> receivePaymentResponseList, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/paymentUpdate/receivePayment");
		HttpEntity<?> entity = new HttpEntity<>(receivePaymentResponseList, headers);
		ResponseEntity<PaymentUpdate[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, 
				entity, PaymentUpdate[].class);
		return result.getBody();
	}

	// DELETE
	public boolean deleteInvoiceHeader(String invoiceNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/" + invoiceNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - TRANSFER-BILLING
	public TransferBilling transferBilling(String fromMatterNumber, String toMatterNumber, String fromDateRange,
				String toDateRange, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/transferBilling")
				.queryParam("fromMatterNumber", fromMatterNumber)
				.queryParam("toMatterNumber", toMatterNumber)
				.queryParam("fromDateRange", fromDateRange)
				.queryParam("toDateRange", toDateRange);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<TransferBilling> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferBilling.class);
		log.info("TransferBilling result : " + result.getBody());
		return result.getBody();
	}

	// OUTPUTFORM - PREBILL
	public PreBillOutputForm getPreBillDetailsOutputForm(String preBillNumber, String matterNumber,
			String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "outputforms/preBill")
				.queryParam("preBillNumber", preBillNumber)
				.queryParam("matterNumber", matterNumber);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<PreBillOutputForm> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, 
				entity, PreBillOutputForm.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// OUTPUTFORM - Invoice
	public PreBillOutputForm getInvoiceOutputForm(String invoiceNumber, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "outputforms/invoice")
				.queryParam("invoiceNumber", invoiceNumber);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<PreBillOutputForm> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, 
				entity, PreBillOutputForm.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/*
	 * ---------------------------Reports-------------------------------------------------------
	 */
	// POST - ARAgingReport
	public ARAgingReport[] createARAgingReport(@Valid ARAgingReportInput arAgingReportInput, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/arAgingReport");
		HttpEntity<?> entity = new HttpEntity<>(arAgingReportInput, headers);
		ResponseEntity<ARAgingReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, 
				entity, ARAgingReport[].class);
		log.info("result : " + result);
		return result.getBody();
	}

	// POST - BillingReport
	public BillingReport[] createBillingReport(@Valid BillingReportInput billingReportInput, String authToken) throws ParseException {
		BillingReportInputEntity billingReportInputEntity = new BillingReportInputEntity();
		BeanUtils.copyProperties(billingReportInput, billingReportInputEntity, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(billingReportInput));
		if (billingReportInput.getFromBillingDate() != null) {
			billingReportInputEntity.setFromBillingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getFromBillingDate()));
		}
		
		if (billingReportInput.getToBillingDate() != null) {
			billingReportInputEntity.setToBillingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getToBillingDate()));
		}
		
		billingReportInputEntity.setFromPostingDate(DateUtils.convertStringToYYYYMMDD(billingReportInput.getFromPostingDate()));
		billingReportInputEntity.setToPostingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getToPostingDate()));
			
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/billingReport");
		HttpEntity<?> entity = new HttpEntity<>(billingReportInputEntity, headers);
		ResponseEntity<BillingReportEntity[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingReportEntity[].class);
		log.info("result : " + result);
		
		List<BillingReport> billingReportList = new ArrayList<>();
		for (BillingReportEntity dbBillingReportEntity : result.getBody()) {
			BillingReport billingReport = new BillingReport();
			BeanUtils.copyProperties(dbBillingReportEntity, billingReport, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(dbBillingReportEntity));
			billingReport.setBillingDate(DateUtils.dateToString(dbBillingReportEntity.getBillingDate()));
			billingReport.setPostingDate(DateUtils.dateToString(dbBillingReportEntity.getPostingDate()));
			billingReportList.add(billingReport);
		}
		log.info("billingReportList : " + billingReportList);
		return billingReportList.toArray(new BillingReport[billingReportList.size()]);
	}

	// POST - PartnerBillingReport
	public PartnerBillingReport[] createPartnerBillingReport(@Valid BillingReportInput billingReportInput, String authToken) throws ParseException {

		if(billingReportInput.getFromPostingDate() == null || billingReportInput.getToPostingDate() == null) {
			throw new BadRequestException("Posting Date is Required for Partner Billing Report. Quiting !");
		}

		BillingReportInputEntity billingReportInputEntity = new BillingReportInputEntity();
		BeanUtils.copyProperties(billingReportInput, billingReportInputEntity,
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(billingReportInput));
		if (billingReportInput.getFromBillingDate() != null) {
			billingReportInputEntity.setFromBillingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getFromBillingDate()));
		}

		if (billingReportInput.getToBillingDate() != null) {
			billingReportInputEntity.setToBillingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getToBillingDate()));
		}

		billingReportInputEntity.setFromPostingDate(DateUtils.convertStringToYYYYMMDD(billingReportInput.getFromPostingDate()));
		billingReportInputEntity.setToPostingDate (DateUtils.convertStringToYYYYMMDD(billingReportInput.getToPostingDate()));

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/partnerBillingReport");
		HttpEntity<?> entity = new HttpEntity<>(billingReportInputEntity, headers);
		ResponseEntity<PartnerBillingReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PartnerBillingReport[].class);
		log.info("partnerBillingReportList : " + result.getBody());
		return result.getBody();
	}

	// POST - Matter PL Report
	public MatterPLReport[] createMatterPLReport(@Valid MatterPLReportInput matterPLReportInput, String authToken) throws ParseException {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/matterPandLReport");
		HttpEntity<?> entity = new HttpEntity<>(matterPLReportInput, headers);
		ResponseEntity<MatterPLReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterPLReport[].class);
		log.info("result : " + result);
		return result.getBody();
	}

	// POST - Immigration Payment Plan Report
	public ImmigrationPaymentPlanReport[] createImmigrationPaymentPlanReport(@Valid ImmigrationPaymentPlanReportInput immigrationPaymentPlanReportInput, String authToken) throws ParseException {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/immigrationPaymentPlanReport");
		HttpEntity<?> entity = new HttpEntity<>(immigrationPaymentPlanReportInput, headers);
		ResponseEntity<ImmigrationPaymentPlanReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ImmigrationPaymentPlanReport[].class);
		log.info("result : " + result);
		return result.getBody();
	}

	// POST -
	public MatterBillingActvityReport createBillingActivityReport(@Valid MatterBillingActvityReportInput billingReportInput, 
			String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "invoiceheader/matterBillingActivityReport");
		HttpEntity<?> entity = new HttpEntity<>(billingReportInput, headers);
		ResponseEntity<MatterBillingActvityReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, 
				entity, MatterBillingActvityReport.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	//-----------------------------------------Dashboard--------------------------------------------------------
	
	// GET - BilledIncomeDashboard
	public BilledIncomeDashboard generateBilledIncomeDashboard(Long classId, String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billedIncome")
							.queryParam("classId", classId)
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - CaseAssignmentDashboard
	public CaseAssignmentDashboard getCaseAssignmentDashboard(Long classId, String timeKeepers, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/caseAssignment")
							.queryParam("classId", classId)
							.queryParam("timeKeepers", timeKeepers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CaseAssignmentDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, CaseAssignmentDashboard.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Dashboard - billableUnbillableTime
	public BilledIncomeDashboard getBillableNonBillableTime(Long classId,String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billableUnbillableTime")
							.queryParam("classId", classId)
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Dashboard - clientReferral
	public BilledIncomeDashboard getClientReferral(Long classId,String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/clientReferral")
							.queryParam("classId", classId)
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Dashboard - practiceBreakdown
	public BilledIncomeDashboard getPracticeBreakDown(Long classId,String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/practiceBreakDown")
							.queryParam("classId", classId)
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Dashboard - topClients
	public BilledIncomeDashboard getTopClients(String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/topClients")
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Dashboard - leadConvertion
	public BilledIncomeDashboard getLeadConversion(Long classId,String period, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/leadConversion")
							.queryParam("classId", classId)
							.queryParam("period", period);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BilledIncomeDashboard> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
							entity, BilledIncomeDashboard.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SEARCH
	public MatterListingReport[] getMatterListing (SearchMatterListing searchMatterListing, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/matter-listing");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterListing, headers);
			ResponseEntity<MatterListingReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterListingReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SEARCH
	public MatterListingReport[] getMatterRatesListing (SearchMatterListing searchMatterListing, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/matter-rates-listing");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterListing, headers);
			ResponseEntity<MatterListingReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterListingReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SEARCH
	public BilledUnBilledHoursReport[] getBilledUnBilledHours (BilledUnBilledHours requestData, String authToken) throws ParseException {
		try {
			BilledUnBilledHoursModel requestDataForService = new BilledUnBilledHoursModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromDate() != null) {
				requestDataForService.setFromDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
			}
			if (requestData.getToDate() != null) {
				requestDataForService.setToDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billed-unbilled-hours");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<BilledUnBilledHoursReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledUnBilledHoursReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SEARCH
	public ClientCashReceiptsReport[] getClientCashReceipts (ClientCashReceipts requestData, String authToken) throws ParseException {
		try {
			ClientCashReceiptsModel requestDataForService = new ClientCashReceiptsModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromDate() != null) {
				requestDataForService.setFromDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
			}
			if (requestData.getToDate() != null) {
				requestDataForService.setToDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/client-cash-receipt-report");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<ClientCashReceiptsReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientCashReceiptsReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// SEARCH
	public ClientIncomeSummaryReport[] getClientIncomeSummary (ClientCashReceipts requestData, String authToken) throws ParseException {
		try {
			ClientCashReceiptsModel requestDataForService = new ClientCashReceiptsModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromDate() != null) {
				requestDataForService.setFromDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
			}
			if (requestData.getToDate() != null) {
				requestDataForService.setToDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/client-income-summary-report");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<ClientIncomeSummaryReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ClientIncomeSummaryReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param requestData
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public ARReport[] getARReport (SearchAR requestData, String authToken) throws ParseException {
		try {
			SearchARModel requestDataForService = new SearchARModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromDate() != null) {
				requestDataForService.setFromDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
			}
			if (requestData.getToDate() != null) {
				requestDataForService.setToDate (DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/ar-report");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<ARReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ARReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param requestData
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public WriteOffReport[] getWriteOffReport (ClientCashReceipts requestData, String authToken) throws ParseException {
		try {
			ClientCashReceiptsModel requestDataForService = new ClientCashReceiptsModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromDate() != null) {
				requestDataForService.setFromDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
			}
			if (requestData.getToDate() != null) {
				requestDataForService.setToDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/write-off-report");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<WriteOffReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WriteOffReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param requestData
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public BilledHoursReport[] getBilledHoursPaidReport (BilledHoursPaid requestData, String authToken) throws ParseException {
		try {
			BilledHoursPaidModel requestDataForService = new BilledHoursPaidModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromPostingDate() != null) {
				requestDataForService.setFromPostingDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromPostingDate()));
			}
			if (requestData.getToPostingDate() != null) {
				requestDataForService.setToPostingDate (DateUtils.convertStringToYYYYMMDD(requestData.getToPostingDate()));
			}
			if (requestData.getFromTimeTicketDate() != null) {
				requestDataForService.setFromTimeTicketDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromTimeTicketDate()));
			}
			if (requestData.getToTimeTicketDate() != null) {
				requestDataForService.setToTimeTicketDate (DateUtils.convertStringToYYYYMMDD(requestData.getToTimeTicketDate()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billed-hours-paid");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<BilledHoursReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledHoursReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *
	 * @param requestData
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public BilledHoursReport[] getPartnerBilledHoursPaidReport (BilledHoursPaid requestData, String authToken) throws ParseException {
		try {
			BilledHoursPaidModel requestDataForService = new BilledHoursPaidModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromPostingDate() != null) {
				requestDataForService.setFromPostingDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromPostingDate()));
			}
			if (requestData.getToPostingDate() != null) {
				requestDataForService.setToPostingDate (DateUtils.convertStringToYYYYMMDD(requestData.getToPostingDate()));
			}
			if (requestData.getFromTimeTicketDate() != null) {
				requestDataForService.setFromTimeTicketDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromTimeTicketDate()));
			}
			if (requestData.getToTimeTicketDate() != null) {
				requestDataForService.setToTimeTicketDate (DateUtils.convertStringToYYYYMMDD(requestData.getToTimeTicketDate()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/partnerBilledHoursPaid");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<BilledHoursReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledHoursReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param requestData
	 * @param authToken
	 * @return
	 */
//	public BilledPaidFeesReport[] getBilledPaidFeesReport (BilledPaidFees requestData, String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "MNRClara RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billed-paid-fees");
//			HttpEntity<?> entity = new HttpEntity<>(requestData, headers);
//			ResponseEntity<BilledPaidFeesReport[]> result =
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledPaidFeesReport[].class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	public BilledPaidFeesReportOutput[] getBilledPaidFeesReport (BilledPaidFees requestData, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/billed-paid-fees");
			HttpEntity<?> entity = new HttpEntity<>(requestData, headers);
			ResponseEntity<BilledPaidFeesReportOutput[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BilledPaidFeesReportOutput[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param requestData
	 * @param authToken
	 * @return
	 * @throws ParseException
	 */
	public ExpirationDateReport[] getExpirationDateReport (ExpirationDateRequest requestData, String authToken) throws ParseException {
		try {
			ExpirationDateRequestModel requestDataForService = new ExpirationDateRequestModel();
			BeanUtils.copyProperties(requestData, requestDataForService,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(requestData));
			if (requestData.getFromExpirationDate() != null) {
				requestDataForService.setFromExpirationDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromExpirationDate()));
			}
			if (requestData.getToExpirationDate() != null) {
				requestDataForService.setToExpirationDate (DateUtils.convertStringToYYYYMMDD(requestData.getToExpirationDate()));
			}
			if (requestData.getFromEligibilityDate() != null) {
				requestDataForService.setFromEligibilityDate (DateUtils.convertStringToYYYYMMDD(requestData.getFromEligibilityDate()));
			}
			if (requestData.getToEligibilityDate() != null) {
				requestDataForService.setToEligibilityDate (DateUtils.convertStringToYYYYMMDD(requestData.getToEligibilityDate()));
			}


			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getAccountingServiceUrl() + "dashboard/expiration-date");
			HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
			ResponseEntity<ExpirationDateReport[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ExpirationDateReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
