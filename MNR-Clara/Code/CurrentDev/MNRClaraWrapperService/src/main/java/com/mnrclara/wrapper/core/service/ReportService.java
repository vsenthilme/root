package com.mnrclara.wrapper.core.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.accounting.QuotationHeader;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.auth.AuthTokenRequest;
import com.mnrclara.wrapper.core.model.crm.PotentialClient;
import com.mnrclara.wrapper.core.model.report.LeadConversionReport;
import com.mnrclara.wrapper.core.model.report.ReferralReport;
import com.mnrclara.wrapper.core.model.report.SearchPCIntakeFormReport;
import com.mnrclara.wrapper.core.model.report.SearchPotentialClientReport;
import com.mnrclara.wrapper.core.model.report.SearchReferralReport;
import com.mnrclara.wrapper.core.model.user.NewUser;
import com.mnrclara.wrapper.core.model.user.RegisteredUser;
import com.mnrclara.wrapper.core.repository.RegisterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getAccountingServiceUrl() {
		return propertiesConfig.getAccountingServiceUrl();
	}
	
	private String getCRMServiceUrl () {
		return propertiesConfig.getCrmServiceUrl();
	}
	
	// PotentialClient Report
	public PotentialClient[] getPotentialClientReport(SearchPotentialClientReport searchPotentialClientReport,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "potentialClient/report");
			HttpEntity<?> entity = new HttpEntity<>(searchPotentialClientReport, headers);
			ResponseEntity<PotentialClient[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PotentialClient[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Lead Conversion report
	public LeadConversionReport[] getPCIntakeFormReport(SearchPCIntakeFormReport searchPCIntakeFormReport,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "pcIntakeForm/report");
			HttpEntity<?> entity = new HttpEntity<>(searchPCIntakeFormReport, headers);
			ResponseEntity<LeadConversionReport[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LeadConversionReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// ReferralReport
	public ReferralReport[] getReferralReport(SearchReferralReport searchReferralReport, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "potentialClient/referralReport");
			HttpEntity<?> entity = new HttpEntity<>(searchReferralReport, headers);
			ResponseEntity<ReferralReport[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ReferralReport[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}