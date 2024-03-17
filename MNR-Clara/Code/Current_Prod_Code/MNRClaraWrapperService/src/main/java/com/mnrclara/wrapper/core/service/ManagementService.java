package com.mnrclara.wrapper.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.crm.Inquiry;
import com.mnrclara.wrapper.core.model.crm.SearchPotentialClientInput;
import com.mnrclara.wrapper.core.model.report.*;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;
import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.model.accounting.ReceiptAppNotice;
import com.mnrclara.wrapper.core.model.accounting.ReceiptAppNoticeEntity;
import com.mnrclara.wrapper.core.model.accounting.UpdateReceiptAppNoticeEntity;
import com.mnrclara.wrapper.core.model.crm.EnvelopeStatus;
import com.mnrclara.wrapper.core.model.management.*;
import com.mnrclara.wrapper.core.model.management.ClientDocument;
import com.mnrclara.wrapper.core.model.management.ClientGeneral;
import com.mnrclara.wrapper.core.model.management.ClientNote;
import com.mnrclara.wrapper.core.model.management.DashboardReport;
import com.mnrclara.wrapper.core.model.management.Dropdown;
import com.mnrclara.wrapper.core.model.management.ExpirationDate;
import com.mnrclara.wrapper.core.model.management.ExpirationDateEntity;
import com.mnrclara.wrapper.core.model.management.FindMatterGenNew;
import com.mnrclara.wrapper.core.model.management.FindMatterGeneral;
import com.mnrclara.wrapper.core.model.management.ImmCaseInfoSheet;
import com.mnrclara.wrapper.core.model.management.KeyValuePair;
import com.mnrclara.wrapper.core.model.management.LeCaseInfoSheet;
import com.mnrclara.wrapper.core.model.management.MatterAssignment;
import com.mnrclara.wrapper.core.model.management.MatterDocList;
import com.mnrclara.wrapper.core.model.management.MatterDocListHeader;
import com.mnrclara.wrapper.core.model.management.MatterDocument;
import com.mnrclara.wrapper.core.model.management.MatterDropdownList;
import com.mnrclara.wrapper.core.model.management.MatterExpense;
import com.mnrclara.wrapper.core.model.management.MatterFeeSharing;
import com.mnrclara.wrapper.core.model.management.MatterGenAcc;
import com.mnrclara.wrapper.core.model.management.MatterITForm;
import com.mnrclara.wrapper.core.model.management.MatterNote;
import com.mnrclara.wrapper.core.model.management.MatterRate;
import com.mnrclara.wrapper.core.model.management.MatterTask;
import com.mnrclara.wrapper.core.model.management.MatterTimeTicket;
import com.mnrclara.wrapper.core.model.management.PaginatedResponse;
import com.mnrclara.wrapper.core.model.management.QBSync;
import com.mnrclara.wrapper.core.model.management.SearchCaseSheetParams;
import com.mnrclara.wrapper.core.model.management.SearchClientDocument;
import com.mnrclara.wrapper.core.model.management.SearchClientGeneral;
import com.mnrclara.wrapper.core.model.management.SearchClientNote;
import com.mnrclara.wrapper.core.model.management.SearchExpirationDate;
import com.mnrclara.wrapper.core.model.management.SearchMatterAssignment;
import com.mnrclara.wrapper.core.model.management.SearchMatterDocList;
import com.mnrclara.wrapper.core.model.management.SearchMatterDocument;
import com.mnrclara.wrapper.core.model.management.SearchMatterExpense;
import com.mnrclara.wrapper.core.model.management.SearchMatterGeneral;
import com.mnrclara.wrapper.core.model.management.SearchMatterITForm;
import com.mnrclara.wrapper.core.model.management.SearchMatterNote;
import com.mnrclara.wrapper.core.model.management.SearchMatterTask;
import com.mnrclara.wrapper.core.model.management.SearchMatterTimeTicket;
import com.mnrclara.wrapper.core.model.management.SearchQbSync;
import com.mnrclara.wrapper.core.model.management.UpdateMatterDocListHeader;
import com.mnrclara.wrapper.core.util.CommonUtils;
import com.mnrclara.wrapper.core.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagementService {

	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
    FileStorageService fileStorageService;
	
	@Autowired
	AuthTokenService authTokenService;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getManagementServiceUrl() {
		return propertiesConfig.getManagementServiceUrl();
	}

	// -------------------------------Client-General-----------------------------------------------------------------
	// GET ALL
	public ClientGeneral[] getClientGenerals(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientGeneral[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public KeyValuePair[] getAllClientList(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/dropdown/client");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<KeyValuePair[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, KeyValuePair[].class);
//			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ALL
	public Page<?> getAllClientGenerals(Integer pageNo, Integer pageSize, String sortBy, List<Long> classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy)
					.queryParam("classId", classId);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<ClientGeneral>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<ClientGeneral>>() {};
			ResponseEntity<PaginatedResponse<ClientGeneral>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ClientGeneral getClientGeneral(String clientgeneralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/" + clientgeneralId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
//	public ClientGeneral[] getLatestClientGeneral(String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//			UriComponentsBuilder builder = UriComponentsBuilder
//					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/top");
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<ClientGeneral[]> result = 
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
//					entity, ClientGeneral[].class);
//			log.info("result : " + result.getBody());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	
	public ClientGeneral getLatestClientGeneral(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/top");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public ClientGeneral getClientGeneralByLimit(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/limit");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientGeneral> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /clientgeneral/findClientGeneral
	public ClientGeneral[] findClientGenerals(SearchClientGeneral searchClientGeneral, String authToken) throws Exception {
		try {
			if (searchClientGeneral.getStartCreatedOnString() != null) {
				searchClientGeneral.setStartCreatedOn(DateUtils.convertStringToYYYYMMDD(searchClientGeneral.getStartCreatedOnString()));
			}

			if (searchClientGeneral.getEndCreatedOnString() != null) {
				searchClientGeneral.setEndCreatedOn(DateUtils.convertStringToYYYYMMDD(searchClientGeneral.getEndCreatedOnString()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/findClientGeneral");
			HttpEntity<?> entity = new HttpEntity<>(searchClientGeneral, headers);
			ResponseEntity<ClientGeneral[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientGeneral[].class);
			log.info("result : " + result.getBody());
//			return result.getBody();
			
			ClientGeneral[] arrResponseClientGeneral = result.getBody();
			List<ClientGeneral> clientGeneralList = new ArrayList<>();
			for (ClientGeneral clientGeneral : arrResponseClientGeneral) {
				clientGeneral.setCreatedOnString(DateUtils.dateToString(clientGeneral.getCreatedOn()));
				clientGeneralList.add(clientGeneral);
			}
			ClientGeneral[] arrClientGeneral = clientGeneralList.toArray(new ClientGeneral[clientGeneralList.size()]);
			return arrClientGeneral;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /clientgeneral/findClientGeneralNew
	public ClientGeneralNew[] findClientGeneralNew(SearchClientGeneral searchClientGeneral, String authToken) throws Exception {
		try {
			if (searchClientGeneral.getStartCreatedOnString() != null) {
				searchClientGeneral.setStartCreatedOn(DateUtils.convertStringToYYYYMMDD(searchClientGeneral.getStartCreatedOnString()));
			}

			if (searchClientGeneral.getEndCreatedOnString() != null) {
				searchClientGeneral.setEndCreatedOn(DateUtils.convertStringToYYYYMMDD(searchClientGeneral.getEndCreatedOnString()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/findClientGeneralNew");
			HttpEntity<?> entity = new HttpEntity<>(searchClientGeneral, headers);
			ResponseEntity<ClientGeneralNew[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientGeneralNew[].class);
			log.info("result : " + result.getBody());
			return result.getBody();

//			List<ClientGeneralNew> clientGeneralList = new ArrayList<>();
//			for (ClientGeneralNew clientGeneral : result.getBody()) {
//				clientGeneral.setCreatedOnString(DateUtils.dateToString(clientGeneral.getCreatedOn()));
//				clientGeneralList.add(clientGeneral);
//			}
//			ClientGeneralNew[] arrClientGeneral = clientGeneralList.toArray(new ClientGeneralNew[clientGeneralList.size()]);
//			return arrClientGeneral;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardTotal(String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/dashboard/total")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardActive (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/dashboard/active")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardRecentClients (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/dashboard/recentClients")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ClientGeneral createClientGeneral(ClientGeneral clientGeneral, String loginUserID,
			Boolean isFromPotentialEndpoint, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral").queryParam("loginUserID", loginUserID)
					.queryParam("isFromPotentialEndpoint", isFromPotentialEndpoint);
			HttpEntity<?> entity = new HttpEntity<>(clientGeneral, headers);
			ResponseEntity<ClientGeneral> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public ClientGeneral updateClientGeneral(String clientgeneralId, ClientGeneral modifiedClientGeneral,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedClientGeneral, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/" + clientgeneralId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientGeneral> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, ClientGeneral.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteClientGeneral(String clientgeneralId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/" + clientgeneralId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Client-Note-----------------------------------------------------------------
	// GET ALL
	public ClientNote[] getClientNotes(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientNote[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientNote[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ClientNote getClientNote(String clientgeneralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote/" + clientgeneralId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientNote> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /inquiry/findClientNote
	public ClientNote[] findClientNotes(SearchClientNote searchClientNote, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote/findClientNotes");
			HttpEntity<?> entity = new HttpEntity<>(searchClientNote, headers);
			ResponseEntity<ClientNote[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientNote[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ClientNote createClientNote(ClientNote clientGeneral, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientGeneral, headers);
			ResponseEntity<ClientNote> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public String createBulkClientNotes(com.mnrclara.wrapper.core.model.datamigration.ClientNote[] arrClientNote, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote/batch").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(arrClientNote, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public ClientNote updateClientNote(String clientgeneralId, ClientNote modifiedClientNote,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedClientNote, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote/" + clientgeneralId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientNote> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, ClientNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteClientNote(String clientgeneralId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientNote-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientnote/" + clientgeneralId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Client-Document-----------------------------------------------------------------
	// GET ALL
	public ClientDocument[] getClientDocuments(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientDocument[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientDocument[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ClientDocument getClientDocument(Long clientgeneralId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientgeneralId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ClientDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /clientdocument/findClientDocument
	public ClientDocument[] findClientDocuments(SearchClientDocument searchClientDocument, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/findClientDocument");
			HttpEntity<?> entity = new HttpEntity<>(searchClientDocument, headers);
			ResponseEntity<ClientDocument[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientDocument[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ClientDocument createClientDocument(ClientDocument clientGeneral, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientGeneral, headers);
			ResponseEntity<ClientDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public ClientDocument updateClientDocument(Long clientDocumentId, ClientDocument modifiedClientDocument,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedClientDocument, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientDocumentId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, ClientDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// Delete
	public ClientDocument deleteClientDocumentId(Long clientDocumentId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientDocumentId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE,
					entity, ClientDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ClientDocument sendDocumentToDocusign(Long classId, String clientId, String documentNumber, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/docusign")
					.queryParam("classId", classId)
					.queryParam("clientId", clientId)
					.queryParam("documentNumber", documentNumber)					
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ClientDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ClientDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Dosusign Download
	public ClientDocument docusignEnvelopeDownload(Long classId, String clientId, String documentNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientId + "/envelope/download")
					.queryParam("classId", classId)
					.queryParam("documentNumber", documentNumber)
					.queryParam("loginUserID", loginUserID);				
			ResponseEntity<ClientDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ClientDocument.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * getDocusignClientEnvelopeStatus
	 * @param clientId
	 * @param loginUserID
	 * @param authToken
	 * @return
	 */
	public EnvelopeStatus getDocusignClientEnvelopeStatus (String clientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Inquiry-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientId + "/envelope/status")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<EnvelopeStatus> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// MailMerge/Manual
	public ClientDocument doProcessEditedClientDocument (Long classId, String clientId, String documentNumber, 
			String location, MultipartFile file, String loginUserID, String authToken) throws Exception {
		try {
			Map<String, String> mapResponse = fileStorageService.storeFileForNonMailMerge(location, file, loginUserID, classId);
			log.info("File uploaded: mapResponse : " + mapResponse);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			
			// /{clientId}/mailmerge/manual
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientId + "/mailmerge/manual")
					.queryParam("classId", classId)
					.queryParam("documentNumber", documentNumber)
					.queryParam("documentUrl", mapResponse.get("file"))
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ClientDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ClientDocument.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteClientDocument(String clientgeneralId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientDocument-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientdocument/" + clientgeneralId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------LeCaseInfoSheet-----------------------------------------------------------------
	// GET ALL
	public LeCaseInfoSheet[] getLeCaseInfoSheets(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LeCaseInfoSheet[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, LeCaseInfoSheet[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public LeCaseInfoSheet getLeCaseInfoSheet(String caseInformationID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet/" + caseInformationID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LeCaseInfoSheet> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, LeCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /leCaseInfoSheet/search
	public LeCaseInfoSheet[] findLeCaseInfoSheets(SearchCaseSheetParams searchCaseSheetParams, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet/search");
			HttpEntity<?> entity = new HttpEntity<>(searchCaseSheetParams, headers);
			ResponseEntity<LeCaseInfoSheet[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, LeCaseInfoSheet[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public LeCaseInfoSheet createLeCaseInfoSheet(LeCaseInfoSheet leCaseInfoSheet, String loginUserID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(leCaseInfoSheet, headers);
			ResponseEntity<LeCaseInfoSheet> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, LeCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc createMatterFromLeCaseInfoSheet(String caseInformationID, String loginUserID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet/" + caseInformationID + "/matter")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public LeCaseInfoSheet updateLeCaseInfoSheet(String caseInformationID, LeCaseInfoSheet modifiedClientGeneral,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedClientGeneral, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet/" + caseInformationID)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<LeCaseInfoSheet> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, LeCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteLeCaseInfoSheet(String caseInformationID, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("LeCaseInfoSheet-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/leCaseInfoSheet/" + caseInformationID)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------ImmCaseInfoSheet-----------------------------------------------------------------
	// GET ALL
	public ImmCaseInfoSheet[] getImmCaseInfoSheets(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ImmCaseInfoSheet[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ImmCaseInfoSheet[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ImmCaseInfoSheet getImmCaseInfoSheet(String caseInformationID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet/" + caseInformationID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ImmCaseInfoSheet> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ImmCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /immCaseInfoSheet/search
	public ImmCaseInfoSheet[] findImmCaseInfoSheets(SearchCaseSheetParams searchCaseSheetParams, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet/search");
			HttpEntity<?> entity = new HttpEntity<>(searchCaseSheetParams, headers);
			ResponseEntity<ImmCaseInfoSheet[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ImmCaseInfoSheet[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ImmCaseInfoSheet createImmCaseInfoSheet(ImmCaseInfoSheet ImmCaseInfoSheet, String loginUserID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(ImmCaseInfoSheet, headers);
			ResponseEntity<ImmCaseInfoSheet> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ImmCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc createMatterFromImmCaseInfoSheet(String caseInformationID, String loginUserID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet/" + caseInformationID + "/matter")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public ImmCaseInfoSheet updateImmCaseInfoSheet(String caseInformationID, ImmCaseInfoSheet modifiedClientGeneral,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedClientGeneral, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet/" + caseInformationID)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ImmCaseInfoSheet> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, ImmCaseInfoSheet.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteImmCaseInfoSheet(String caseInformationID, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ImmCaseInfoSheet-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/immCaseInfoSheet/" + caseInformationID)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------MatterGenAcc-----------------------------------------------------------------
	// GET ALL
	public MatterGenAcc[] getMatterGenAccs(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET PAGINATION
	public Page<?> getAllMatterGenAccs(Integer pageNo, Integer pageSize, String sortBy, 
			List<Long> classId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy)
					.queryParam("classId", classId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<MatterGenAcc>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<MatterGenAcc>>() {};
			ResponseEntity<PaginatedResponse<MatterGenAcc>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public Dropdown getDropdownList(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dropdown");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Dropdown> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, Dropdown.class);
//			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterDropdownList getMatterDropdownList (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dropdown/matter");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDropdownList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterDropdownList.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterNumberDropDown[] getMatterNumberDropdown (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dropdown/matterAlone");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterNumberDropDown[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterNumberDropDown[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterNumberDropDown[] getMatterNumberDropdownNew (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dropdown/matter/new");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterNumberDropDown[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterNumberDropDown[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterDropdownList getOpenMatterDropdownList (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dropdown/matter/open");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDropdownList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterDropdownList.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc getMatterGenAcc(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterGenAcc.class);
			MatterGenAcc matterGenAcc = result.getBody();
			matterGenAcc.setSCaseOpenedDate(DateUtils.dateToString(matterGenAcc.getCaseOpenedDate()));
			matterGenAcc.setSCaseClosedDate(DateUtils.dateToString(matterGenAcc.getCaseClosedDate()));
			matterGenAcc.setSCaseFiledDate(DateUtils.dateToString(matterGenAcc.getCaseFiledDate()));
			matterGenAcc.setSPriorityDate(DateUtils.dateToString(matterGenAcc.getPriorityDate()));
			matterGenAcc.setSExpirationDate(DateUtils.dateToString(matterGenAcc.getExpirationDate()));
			matterGenAcc.setSReceiptDate(DateUtils.dateToString(matterGenAcc.getReceiptDate()));
			matterGenAcc.setSCourtDate(DateUtils.dateToString(matterGenAcc.getCourtDate()));
			matterGenAcc.setSApprovalDate(DateUtils.dateToString(matterGenAcc.getApprovalDate()));
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET Date - for Mobile API
    public MatterGenAccDate getMatterGenAccDate(String matterNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/date/" + matterNumber);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<MatterGenAccDate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterGenAccDate.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
	
	// GET - /{matterNumber}/docketwise/{matterId}
	public MatterGenAcc getMatterGenAccFromDocketwise(String matterNumber, String matterId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber + "/docketwise/" + matterId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
//			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
//	public MatterGenAcc[] getLatestMatterGeneral(String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "MNRClara RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//			UriComponentsBuilder builder = UriComponentsBuilder
//					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/top");
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<MatterGenAcc[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
//					entity, MatterGenAcc[].class);
//			log.info("result : " + result.getBody());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	
	public MatterGenAcc getLatestMatterGeneral(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/top");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc getMatterGeneralByLimit (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/limit");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /mattergenacc/findClientGeneral
	public MatterGenAcc[] findMatterGenAccs(SearchMatterGeneral searchMatterGeneral, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/findMatterGenAccs");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterGeneral, headers);
			ResponseEntity<MatterGenAcc[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterGenAcc[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /mattergenacc - new(query based)
	public FindMatterGenNew[] findMatterGeneralNew (FindMatterGeneral searchMatterGeneral, String authToken) throws ParseException{
		try {
			if (searchMatterGeneral.getFromDateString() != null) {
				searchMatterGeneral.setFromDate(DateUtils.convertStringToYYYYMMDD(searchMatterGeneral.getFromDateString()));
			}

			if (searchMatterGeneral.getEndDateString() != null) {
				searchMatterGeneral.setEndDate(DateUtils.convertStringToYYYYMMDD(searchMatterGeneral.getEndDateString()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/findMatterGeneralNew");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterGeneral, headers);
			ResponseEntity<FindMatterGenNew[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, FindMatterGenNew[].class);
			//log.info("result : " + result.getBody());
			FindMatterGenNew[] arrResponseFindMatter = result.getBody();
			List<FindMatterGenNew> findMatterList = new ArrayList<>();
			for (FindMatterGenNew findMatter : arrResponseFindMatter) {
				findMatter.setSCaseOpenedDate(DateUtils.dateToString(findMatter.getCaseOpenedDate()));
				findMatterList.add(findMatter);
			}
			FindMatterGenNew[] arrFindMatter = findMatterList.toArray(new FindMatterGenNew[findMatterList.size()]);
			return arrFindMatter;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /mattergenacc - Mobile(query based)
	public MatterGenMobileResponse[] findMatterGeneralMobile(FindMatterGeneral searchMatterGeneral, String authToken) throws ParseException {
		try {
			if (searchMatterGeneral.getFromDateString() != null) {
				searchMatterGeneral.setFromDate(DateUtils.convertStringToYYYYMMDD(searchMatterGeneral.getFromDateString()));
			}

			if (searchMatterGeneral.getEndDateString() != null) {
				searchMatterGeneral.setEndDate(DateUtils.convertStringToYYYYMMDD(searchMatterGeneral.getEndDateString()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/findMatterGeneralMobile");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterGeneral, headers);
			ResponseEntity<MatterGenMobileResponse[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterGenMobileResponse[].class);
			log.info("result : " + result.getBody());

			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardTotalForMatter (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dashboard/total")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public DashboardReport getDashboardOpenForMatter (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dashboard/open")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardFiledForMatter (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dashboard/filed")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardRTEForMatter (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dashboard/RTE")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public DashboardReport getDashboardClosedForMatter (String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/dashboard/closed")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DashboardReport> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, DashboardReport.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterGenAcc createMatterGenAcc(MatterGenAcc clientGeneral, String loginUserID,
			Boolean isFromPotentialEndpoint, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc").queryParam("loginUserID", loginUserID)
					.queryParam("isFromPotentialEndpoint", isFromPotentialEndpoint);
			HttpEntity<?> entity = new HttpEntity<>(clientGeneral, headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterGenAcc updateMatterGenAcc(String matterNumber, MatterGenAcc modifiedMatterGenAcc,
			String loginUserID, String authToken) throws Exception {
		try {
			// Get all String type Dates
			if (modifiedMatterGenAcc.getSCaseOpenedDate() != null) {
				modifiedMatterGenAcc.setCaseOpenedDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSCaseOpenedDate()));
			}
			
			if (modifiedMatterGenAcc.getSCaseClosedDate() != null) {
				modifiedMatterGenAcc.setCaseClosedDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSCaseClosedDate()));
			}
			
			if (modifiedMatterGenAcc.getSCaseFiledDate() != null) {
				modifiedMatterGenAcc.setCaseFiledDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSCaseFiledDate()));
			}
			
			if (modifiedMatterGenAcc.getSPriorityDate() != null) {
				modifiedMatterGenAcc.setPriorityDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSPriorityDate()));
			}
			
			if (modifiedMatterGenAcc.getSReceiptDate() != null) {
				modifiedMatterGenAcc.setReceiptDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSReceiptDate()));
			}
			
			if (modifiedMatterGenAcc.getSExpirationDate() != null) {
				modifiedMatterGenAcc.setExpirationDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSExpirationDate()));
			}
			
			if (modifiedMatterGenAcc.getSCourtDate() != null) {
				modifiedMatterGenAcc.setCourtDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSCourtDate()));
			}
			
			if (modifiedMatterGenAcc.getSApprovalDate() != null) {
				modifiedMatterGenAcc.setApprovalDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterGenAcc.getSApprovalDate()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterGenAcc, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterGenAcc> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			
			MatterGenAcc updatedMatterGenAcc = result.getBody();
			updatedMatterGenAcc.setSCaseOpenedDate(DateUtils.dateToString(updatedMatterGenAcc.getCaseOpenedDate()));
			updatedMatterGenAcc.setSCaseClosedDate(DateUtils.dateToString(updatedMatterGenAcc.getCaseClosedDate()));
			updatedMatterGenAcc.setSCaseFiledDate(DateUtils.dateToString(updatedMatterGenAcc.getCaseFiledDate()));
			updatedMatterGenAcc.setSPriorityDate(DateUtils.dateToString(updatedMatterGenAcc.getPriorityDate()));
			updatedMatterGenAcc.setSExpirationDate(DateUtils.dateToString(updatedMatterGenAcc.getExpirationDate()));
			updatedMatterGenAcc.setSReceiptDate(DateUtils.dateToString(updatedMatterGenAcc.getReceiptDate()));
			updatedMatterGenAcc.setSCourtDate(DateUtils.dateToString(updatedMatterGenAcc.getCourtDate()));
			updatedMatterGenAcc.setSApprovalDate(DateUtils.dateToString(updatedMatterGenAcc.getApprovalDate()));
			log.info("=============>updatedMatterGenAcc : " + updatedMatterGenAcc);
			
			return updatedMatterGenAcc;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterGenAcc getQbSyncUpdated (String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber + "/qbSync")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public MatterGenAcc updateMatterAccounting(String matterNumber, MatterGenAcc modifiedMatterGenAcc,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterGenAcc, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber + "/accounting")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterGenAcc> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterGenAcc.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteMatterGenAcc(String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param authToken
	 * @return
	 */
	public MatterGenAcc push2Docketwise (String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterGenAcc-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/" + matterNumber + "/push2Docketwise");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterGenAcc> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterGenAcc.class);
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	// -------------------------------Matter-Note-----------------------------------------------------------------
	// GET ALL
	public MatterNote[] getMatterNotes(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterNote[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterNote[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterNote getMatterNote(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterNote> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /matternote/findClientGeneral
	public MatterNote[] findMatterNotes(SearchMatterNote searchMatterNote, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote/findMatterNotes");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterNote, headers);
			ResponseEntity<MatterNote[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterNote[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterNote createMatterNote(MatterNote clientGeneral, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(clientGeneral, headers);
			ResponseEntity<MatterNote> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterNote updateMatterNote(String matterNumber, MatterNote modifiedMatterNote,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterNote, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterNote> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterNote.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMatterNote(String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterNote-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Matter-Task---------------------------------------------------------------
	// GET ALL
	public MatterTask[] getMatterTasks(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTask-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTask[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTask[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterTask getMatterTask(String taskNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTask-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask/" + taskNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTask> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTask.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /mattertask/findMatterTasks
	public MatterTask[] findMatterTasks (SearchMatterTask searchMatterTask, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask/findMatterTasks");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterTask, headers);
			ResponseEntity<MatterTask[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterTask[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public MatterTask createMatterTask(MatterTask newMatterTask, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTask-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterTask, headers);
			ResponseEntity<MatterTask> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterTask.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterTask updateMatterTask(String taskNumber, MatterTask modifiedMatterTask,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTask-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterTask, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask/" + taskNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterTask> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterTask.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMatterTask(String taskNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTask-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertask/" + taskNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Matter-Document-----------------------------------------------------------------
	// GET ALL
	public MatterDocument[] getMatterDocuments(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocument[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterDocument[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterDocument getMatterDocument(Long matterDocumentId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterDocumentId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /matterdocument/findMatterDocument
	public MatterDocument[] findMatterDocuments(SearchMatterDocument searchMatterDocument, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/findMatterDocument");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterDocument, headers);
			ResponseEntity<MatterDocument[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterDocument[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterDocument createMatterDocument(MatterDocument matterDocument, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(matterDocument, headers);
			ResponseEntity<MatterDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterDocument createClientPortalMatterDocuemnt (MatterDocument matterDocument, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/clientPortal")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(matterDocument, headers);
			ResponseEntity<MatterDocument> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterDocument createMatterDocuemntForClientPortalDocsUpload (MatterDocument matterDocument, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterDocument-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/clientPortal/docsUpload")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(matterDocument, headers);
			ResponseEntity<MatterDocument> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterDocument updateMatterDocument(Long matterDocumentId, MatterDocument modifiedMatterDocument,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterDocument, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterDocumentId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// Delete
	public MatterDocument deleteMatterDocumentId(Long matterDocumentId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterDocumentId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterDocument sendMatterDocumentToDocusign(Long classId, String matterNumber, String documentNumber, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/docusign")
					.queryParam("classId", classId)
					.queryParam("matterNumber", matterNumber)
					.queryParam("documentNumber", documentNumber)
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterDocument.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// MailMerge/Manual
	public MatterDocument doProcessEditedMattertDocument (Long classId, Long matterDocumentId, String location, MultipartFile file, String loginUserID, String authToken) 
		throws Exception {
		try {
			Map<String, String> mapResponse = fileStorageService.storeFileForNonMailMerge(location, file, loginUserID, classId);
			log.info("File uploaded: mapResponse : " + mapResponse);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterDocumentId + "/mailmerge/manual")
					.queryParam("documentUrl", mapResponse.get("file"))
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterDocument> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterDocument.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Dosusign Download
	public MatterDocument docusignMatterEnvelopeDownload(Long classId, 
			String matterNumber, 
			String documentNumber,
			String loginUserID, 
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterNumber + "/envelope/download")
					.queryParam("classId", classId)
					.queryParam("documentNumber", documentNumber)
					.queryParam("loginUserID", loginUserID);					
			ResponseEntity<MatterDocument> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocument.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// EnvelopeStatus
	public EnvelopeStatus getDocusignMatterEnvelopeStatus (String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterNumber + "/envelope/status")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<EnvelopeStatus> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, EnvelopeStatus.class);
			log.info("result : " + result);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMatterDocument(String matterDocumentId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterdocument/" + matterDocumentId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Matter-Assignment---------------------------------------------------------------
	// GET ALL
	public MatterAssignment[] getMatterAssignments(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterAssignment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterAssignment[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Page<MatterAssignment> getAllMatterAssignments(Integer pageNo, Integer pageSize, String sortBy, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ParameterizedTypeReference<PaginatedResponse<MatterAssignment>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<MatterAssignment>>() {};
			ResponseEntity<PaginatedResponse<MatterAssignment>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterAssignment getMatterAssignment(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterAssignment> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterAssignment.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /matterassignment/findMatterAssignment
	public MatterAssignment[] findMatterAssignment(SearchMatterAssignment searchMatterAssignment, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/findMatterAssignment");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterAssignment, headers);
			ResponseEntity<MatterAssignment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterAssignment[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
//	public MatterAssignment[] findMatterAssignmentNew(SearchMatterAssignment searchMatterAssignment, String authToken) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//			UriComponentsBuilder builder = UriComponentsBuilder
//					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/findMatterAssignmentNew");
//			HttpEntity<?> entity = new HttpEntity<>(searchMatterAssignment, headers);
//			ResponseEntity<MatterAssignment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
//					entity, MatterAssignment[].class);
//			log.info("result : " + result.getBody());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
	public MatterAssignment[] findMatterAssignmentStream(SearchMatterAssignment searchMatterAssignment, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/findMatterAssignmentStream");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterAssignment, headers);
			ResponseEntity<MatterAssignment[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterAssignment[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public MatterAssignment createMatterAssignment(MatterAssignment newMatterAssignment, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterAssignment, headers);
			ResponseEntity<MatterAssignment> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterAssignment.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterAssignment updateMatterAssignment(String matterNumber, MatterAssignment modifiedMatterAssignment,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterAssignment, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterAssignment> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterAssignment.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMatterAssignment(String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterAssignment-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Matter-Time-Ticket---------------------------------------------------------------
	// GET ALL
	public MatterTimeTicket[] getMatterTimeTickets(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTimeTicket[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTimeTicket[].class);
			MatterTimeTicket[] arrResponseMatterTimeTicket = result.getBody();
			List<MatterTimeTicket> matterTimeTicketList = new ArrayList<>();
			for (MatterTimeTicket matterTimeTicket : arrResponseMatterTimeTicket) {
				matterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(matterTimeTicket.getTimeTicketDate()));
				matterTimeTicket.setSCreatedOn(DateUtils.dateToString(matterTimeTicket.getCreatedOn()));
				matterTimeTicketList.add(matterTimeTicket);
			}
			MatterTimeTicket[] arrTimeTickets = matterTimeTicketList.toArray(new MatterTimeTicket[matterTimeTicketList.size()]);
			return arrTimeTickets;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - PreBill Approve
	public MatterTimeTicket[] getMatterTimeTicketPreBill(String preBillNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + preBillNo + "/preBillApprove");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTimeTicket[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTimeTicket[].class);
			MatterTimeTicket[] arrResponseMatterTimeTicket = result.getBody();
			List<MatterTimeTicket> matterTimeTicketList = new ArrayList<>();
			if (arrResponseMatterTimeTicket != null) {
				for (MatterTimeTicket matterTimeTicket : arrResponseMatterTimeTicket) {
					matterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(matterTimeTicket.getTimeTicketDate()));
					matterTimeTicket.setSCreatedOn(DateUtils.dateToString(matterTimeTicket.getCreatedOn()));
					matterTimeTicketList.add(matterTimeTicket);
				}
				MatterTimeTicket[] arrTimeTickets = matterTimeTicketList.toArray(new MatterTimeTicket[matterTimeTicketList.size()]);
				return arrTimeTickets;
			}
			return null;			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterTimeTicket getMatterTimeTicket(String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + matterNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTimeTicket> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTimeTicket.class);
			MatterTimeTicket responseMatterTimeTicket = result.getBody();
			responseMatterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(responseMatterTimeTicket.getTimeTicketDate()));
			responseMatterTimeTicket.setSCreatedOn(DateUtils.dateToString(responseMatterTimeTicket.getCreatedOn()));
			return responseMatterTimeTicket;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterTimeTicketMobile getMatterTimeTicketMobile(String timeTicketNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/mobile/" + timeTicketNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterTimeTicketMobile> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterTimeTicketMobile.class);
			MatterTimeTicketMobile responseMatterTimeTicket = result.getBody();
			responseMatterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(responseMatterTimeTicket.getTimeTicketDate()));
			responseMatterTimeTicket.setSCreatedOn(DateUtils.dateToString(responseMatterTimeTicket.getCreatedOn()));
			return responseMatterTimeTicket;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /mattertimeticket/findMatterTimeTicket
	public MatterTimeTicket[] findMatterTimeTickets(SearchMatterTimeTicket searchMatterTimeTicket, String authToken) throws Exception {
		try {
			if (searchMatterTimeTicket.getSStartTimeTicketDate() != null) {
				searchMatterTimeTicket.setStartTimeTicketDate(DateUtils.convertStringToYYYYMMDD(searchMatterTimeTicket.getSStartTimeTicketDate()));
			}
			
			if (searchMatterTimeTicket.getSEndTimeTicketDate() != null) {
				searchMatterTimeTicket.setEndTimeTicketDate(DateUtils.convertStringToYYYYMMDD(searchMatterTimeTicket.getSEndTimeTicketDate()));
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/findMatterTimeTicket");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterTimeTicket, headers);
			ResponseEntity<MatterTimeTicket[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterTimeTicket[].class);
			
			MatterTimeTicket[] arrResponseMatterTimeTicket = result.getBody();
			List<MatterTimeTicket> matterTimeTicketList = new ArrayList<>();
			for (MatterTimeTicket matterTimeTicket : arrResponseMatterTimeTicket) {
				matterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(matterTimeTicket.getTimeTicketDate()));
				matterTimeTicket.setSCreatedOn(DateUtils.dateToString(matterTimeTicket.getCreatedOn()));
				matterTimeTicketList.add(matterTimeTicket);
			}
			MatterTimeTicket[] arrTimeTickets = matterTimeTicketList.toArray(new MatterTimeTicket[matterTimeTicketList.size()]);
			return arrTimeTickets;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// find - /mattertimeticket/findTimeKeeperBillingReport
	public TimeKeeperBillingReport[] findTimeKeeperBillingReport(TimeKeeperBillingReportInput timeKeeperBillingReportInput,
																 								String authToken) throws Exception {
		try {

			if(timeKeeperBillingReportInput.getFromDate() == null || timeKeeperBillingReportInput.getToDate() == null) {
				throw new BadRequestException("The Date Range is Required");
			}

			if (timeKeeperBillingReportInput.getFromDate() != null) {
				timeKeeperBillingReportInput.setStartDate(DateUtils.convertStringToYYYYMMDD(timeKeeperBillingReportInput.getFromDate()));
			}

			if (timeKeeperBillingReportInput.getToDate() != null) {
				timeKeeperBillingReportInput.setEndDate(DateUtils.convertStringToYYYYMMDD(timeKeeperBillingReportInput.getToDate()));
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/findTimeKeeperBillingReport");
			HttpEntity<?> entity = new HttpEntity<>(timeKeeperBillingReportInput, headers);
			ResponseEntity<TimeKeeperBillingReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, TimeKeeperBillingReport[].class);

			return result.getBody();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public MatterTimeTicket createMatterTimeTicket(MatterTimeTicket newMatterTimeTicket, String loginUserID, String authToken) throws Exception {
		try {
			newMatterTimeTicket.setTimeTicketDate(DateUtils.convertStringToYYYYMMDD(newMatterTimeTicket.getSTimeTicketDate()));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket").queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterTimeTicket, headers);
			ResponseEntity<MatterTimeTicket> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterTimeTicket.class);
			MatterTimeTicket responseMatterTimeTicket = result.getBody();
			responseMatterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(responseMatterTimeTicket.getTimeTicketDate()));
			responseMatterTimeTicket.setSCreatedOn(DateUtils.dateToString(responseMatterTimeTicket.getCreatedOn()));
			return responseMatterTimeTicket;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterTimeTicket updateMatterTimeTicket(String matterNumber, MatterTimeTicket modifiedMatterTimeTicket,
			String loginUserID, String authToken) throws Exception {
		try {
			modifiedMatterTimeTicket.setTimeTicketDate(DateUtils.convertStringToYYYYMMDD(modifiedMatterTimeTicket.getSTimeTicketDate()));
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterTimeTicket, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterTimeTicket> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
					entity, MatterTimeTicket.class);
			
			MatterTimeTicket responseMatterTimeTicket = result.getBody();
			responseMatterTimeTicket.setSTimeTicketDate(DateUtils.dateToString(responseMatterTimeTicket.getTimeTicketDate()));
			return responseMatterTimeTicket;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMatterTimeTicket(String matterNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("MatterTimeTicket-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/" + matterNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// -------------------------------Matter-Rate-------------------------------------------------------------------------
	// GET ALL
	public MatterRate[] getMatterRates (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterRate[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterRate[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterRate getMatterRate (String matterNumber, String timeKeeperCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate/" + matterNumber)
					.queryParam("timeKeeperCode", timeKeeperCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterRate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterRate.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterRate[] getMatterRate (String matterNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate/" + matterNumber + "/matterNumber");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterRate[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterRate[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterRate createMatterRate (MatterRate newMatterRate, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterRate, headers);
			ResponseEntity<MatterRate> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterRate.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterRate updateMatterRate (String matterNumber, String timeKeeperCode, MatterRate modifiedMatterRate,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterRate, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate/" + matterNumber)
					.queryParam("timeKeeperCode", timeKeeperCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterRate> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterRate.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// ----------------------Matter-Fee-Sharing----------------------------------------------------------------
	// GET ALL
	public MatterFeeSharing[] getMatterFeeSharings (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterfeesharing");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterFeeSharing[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterFeeSharing[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterFeeSharing getMatterFeeSharing (String matterNumber, String timeKeeperCode, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterfeesharing/" + matterNumber)
					.queryParam("timeKeeperCode", timeKeeperCode);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterFeeSharing> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterFeeSharing.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterFeeSharing createMatterFeeSharing (MatterFeeSharing newMatterFeeSharing, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterfeesharing")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterFeeSharing, headers);
			ResponseEntity<MatterFeeSharing> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterFeeSharing.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterFeeSharing updateMatterFeeSharing (String matterNumber, String timeKeeperCode, MatterFeeSharing modifiedMatterFeeSharing,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterFeeSharing, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterfeesharing/" + matterNumber)
					.queryParam("timeKeeperCode", timeKeeperCode)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterFeeSharing> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterFeeSharing.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// ----------------------Matter-Expense----------------------------------------------------------------
	// GET ALL
	public MatterExpense[] getMatterExpenses (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterExpense[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterExpense[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /matterexpense/{preBillNo}/preBillApprove
	public MatterExpense[] getMatterExpensePreBill(String preBillNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + preBillNo + "/preBillApprove");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterExpense[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterExpense[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterExpense getMatterExpense (Long matterExpenseId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + matterExpenseId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterExpense> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /matterexpense/findMatterExpenses
	public MatterExpense[] findMatterExpenses(SearchMatterExpense searchMatterExpense, String authToken) throws ParseException{
		try {
			SearchMatterExpenseInput searchMatterExpenseInput = new SearchMatterExpenseInput();
			BeanUtils.copyProperties(searchMatterExpense, searchMatterExpenseInput, 
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchMatterExpense));
            if (searchMatterExpense.getStartCreatedOn() != null) {
            	searchMatterExpenseInput.setStartCreatedOn(DateUtils.convertStringToYYYYMMDD(searchMatterExpense.getStartCreatedOn()));
            }

            if (searchMatterExpense.getEndCreatedOn() != null) {
            	searchMatterExpenseInput.setEndCreatedOn(DateUtils.convertStringToYYYYMMDD(searchMatterExpense.getEndCreatedOn()));
            }
            log.info("searchMatterExpense.getStartCreatedOn : " + searchMatterExpense.getStartCreatedOn());
            log.info("searchMatterExpense.getEndCreatedOn : " + searchMatterExpense.getEndCreatedOn());
            
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/findMatterExpenses");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterExpenseInput, headers);
			ResponseEntity<MatterExpense[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterExpense[].class);
			log.info("result : " + result.getBody());

            MatterExpense[] arrResponseMatterExpense = result.getBody();
            List<MatterExpense> matterExpenseList = new ArrayList<>();
            for (MatterExpense matterExpense : arrResponseMatterExpense) {
                matterExpense.setSReferenceField2(DateUtils.dateToString(matterExpense.getReferenceField2()));
                matterExpense.setSCreatedOn(DateUtils.dateToString(matterExpense.getCreatedOn()));
                matterExpenseList.add(matterExpense);
            }
            MatterExpense[] arrMatterExpense = matterExpenseList.toArray(new MatterExpense[matterExpenseList.size()]);
            return arrMatterExpense;
//			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /matterexpense/findMatterExpenses - New
	public MatterExpense[] findMatterExpensesNew(SearchMatterExpense searchMatterExpense, String authToken) throws ParseException {
		try {
			SearchMatterExpenseInput searchMatterExpenseInput = new SearchMatterExpenseInput();
			BeanUtils.copyProperties(searchMatterExpense, searchMatterExpenseInput,
					com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(searchMatterExpense));
			if (searchMatterExpense.getStartCreatedOn() != null) {
				searchMatterExpenseInput.setStartCreatedOn(DateUtils.convertStringToYYYYMMDD(searchMatterExpense.getStartCreatedOn()));
			}

			if (searchMatterExpense.getEndCreatedOn() != null) {
				searchMatterExpenseInput.setEndCreatedOn(DateUtils.convertStringToYYYYMMDD(searchMatterExpense.getEndCreatedOn()));
			}
			log.info("searchMatterExpense.getStartCreatedOn : " + searchMatterExpense.getStartCreatedOn());
			log.info("searchMatterExpense.getEndCreatedOn : " + searchMatterExpense.getEndCreatedOn());

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/findMatterExpenses/new");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterExpenseInput, headers);
			ResponseEntity<MatterExpense[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterExpense[].class);
//            log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterExpense createMatterExpense (MatterExpense newMatterExpense, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterExpense, headers);
			ResponseEntity<MatterExpense> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterExpense updateMatterExpense (Long matterExpenseId, MatterExpense modifiedMatterExpense,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterExpense, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + matterExpenseId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterExpense> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterExpense.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// UPDATE
	public MatterExpense[] updateMatterExpense (List<MatterExpense> updateMatterExpense,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateMatterExpense, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/status")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterExpense[]> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterExpense[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteMatterExpense(Long matterExpenseId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/" + matterExpenseId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// ----------------------Matter-ITForm----------------------------------------------------------------
	// GET ALL
	public MatterITForm[] getMatterITForms (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterITForm[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterITForm[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterITForm getMatterITForm (String intakeFormNumber, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform/" + intakeFormNumber);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterITForm> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, MatterITForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET - /matteritform/findMatterITForms
	public MatterITForm[] findMatterITForm (SearchMatterITForm searchMatterITForm, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform/findMatterITForm");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterITForm, headers);
			ResponseEntity<MatterITForm[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterITForm[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterITForm createMatterITForm (MatterITForm newMatterITForm, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newMatterITForm, headers);
			ResponseEntity<MatterITForm> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, MatterITForm.class);
//			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public MatterITForm updateMatterITForm (String intakeFormNumber, MatterITForm modifiedMatterITForm,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterITForm, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform/" + intakeFormNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterITForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterITForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Approve
	public MatterITForm approveMatterITForm (String intakeFormNumber, MatterITForm modifiedMatterITForm,
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMatterITForm, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform/" + intakeFormNumber + "/approve")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<MatterITForm> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterITForm.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteMatterITForm(String intakeFormNumber, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matteritform/" + intakeFormNumber)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
					String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/* -----------------------------EXPIRATIONDATE---EXPIRATIONDATE---------------------------------------------------------------*/
	// Get ALL
	public ExpirationDate[] getExpirationDates (String authToken) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate");
			ResponseEntity<ExpirationDateEntity[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpirationDateEntity[].class);
			
			ExpirationDateEntity[] arrExpirationDateEntity = result.getBody();
			List<ExpirationDate> expirationDateList = new ArrayList<>();
			for (ExpirationDateEntity expirationDateEntity : arrExpirationDateEntity) {
				expirationDateList.add(convertEntityToExpirationDate (expirationDateEntity));
			}
			ExpirationDate[] arrExpirationDate = expirationDateList.toArray(new ExpirationDate[expirationDateList.size()]);
			return arrExpirationDate;
		} catch (Exception e) {;
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET ExpirationDate
	public ExpirationDate getExpirationDate(String matterNo, String languageId,
			Long classId, String clientId, String documentType, String authToken) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate/" + matterNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("clientId", clientId)
					.queryParam("documentType", documentType);
					
			ResponseEntity<ExpirationDateEntity> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ExpirationDateEntity.class);
			return convertEntityToExpirationDate (result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST ExpirationDate
	public ExpirationDate addExpirationDate (ExpirationDate expirationdate, String loginUserID, String authToken) throws Exception {
		try {
			ExpirationDateEntity expirationDateEntity = convertExpirationDateToEntity (expirationdate);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate")
					.queryParam("loginUserID", loginUserID);

			HttpEntity<?> entity = new HttpEntity<>(expirationDateEntity, headers);
			ResponseEntity<ExpirationDateEntity> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ExpirationDateEntity.class);
			return convertEntityToExpirationDate (result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET SMS 
	public Boolean sendReminderSMS () {
		try {
			log.info("Reminder SMS called : " + new Date());
			com.mnrclara.wrapper.core.model.auth.AuthToken authTokenForMgmtService = 
					authTokenService.getManagementServiceAuthToken();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authTokenForMgmtService.getAccess_token());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate/reminderSMS");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Boolean.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Patch ExpirationDate
	public ExpirationDate updateExpirationDate (String matterNo, ExpirationDate modifiedExpirationDate, String loginUserID, String authToken) 
			throws Exception {
		try {
			ExpirationDateEntity expirationDateEntity = convertExpirationDateToEntity (modifiedExpirationDate);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(expirationDateEntity, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate/" + matterNo)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ExpirationDateEntity> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ExpirationDateEntity.class);
			return convertEntityToExpirationDate (result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// Delete ExpirationDate
	public boolean deleteExpirationDate (String matterNo, String languageId,
			Long classId, String clientId, String documentType, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/expirationdate/" + matterNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("clientId", clientId)
					.queryParam("documentType", documentType)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// FIND
	public ExpirationDate[] findExpirationDate(SearchExpirationDate searchExpirationDate, String authToken) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "expirationdate/findExpirationDate");
			HttpEntity<?> entity = new HttpEntity<>(searchExpirationDate, headers);
			ResponseEntity<ExpirationDateEntity[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ExpirationDateEntity[].class);
			ExpirationDateEntity[] arrExpirationDateEntity = result.getBody();
			List<ExpirationDate> expirationDateList = new ArrayList<>();
			for (ExpirationDateEntity expirationDateEntity : arrExpirationDateEntity) {
				expirationDateList.add(convertEntityToExpirationDate (expirationDateEntity));
			}
			return (ExpirationDate[]) expirationDateList.toArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param expirationdate
	 * @return
	 * @throws ParseException
	 */
	private ExpirationDateEntity convertExpirationDateToEntity (ExpirationDate expirationdate) throws ParseException {
		ExpirationDateEntity expirationDateEntity = new ExpirationDateEntity();
		BeanUtils.copyProperties(expirationdate, expirationDateEntity, com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(expirationdate));
		expirationDateEntity.setApprovalDate(DateUtils.convertStringToYYYYMMDD(expirationdate.getApprovalDate()));
		expirationDateEntity.setExpirationDate(DateUtils.convertStringToYYYYMMDD(expirationdate.getExpirationDate()));
		expirationDateEntity.setEligibilityDate(DateUtils.convertStringToYYYYMMDD(expirationdate.getEligibilityDate()));
		expirationDateEntity.setReminderDate(DateUtils.convertStringToYYYYMMDD(expirationdate.getReminderDate()));
		return expirationDateEntity;
	}
	
	/**
	 * 
	 * @param expirationDateEntity
	 * @return
	 * @throws ParseException
	 */
	private ExpirationDate convertEntityToExpirationDate (ExpirationDateEntity expirationDateEntity) throws ParseException {
		ExpirationDate expirationDate = new ExpirationDate();
		BeanUtils.copyProperties(expirationDateEntity, expirationDate, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(expirationDateEntity));
		expirationDate.setApprovalDate(DateUtils.dateToString(expirationDateEntity.getApprovalDate()));
		expirationDate.setExpirationDate(DateUtils.dateToString(expirationDateEntity.getExpirationDate()));
		expirationDate.setEligibilityDate(DateUtils.dateToString(expirationDateEntity.getEligibilityDate()));
		expirationDate.setReminderDate(DateUtils.dateToString(expirationDateEntity.getReminderDate()));
		return expirationDate;
	}
	
	//--------------------------------------------ReceiptAppNotice------------------------------------------------------------------------
	// GET ALL
	public ReceiptAppNotice[] getReceiptAppNotices (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "receiptappnotice");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ReceiptAppNoticeEntity[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ReceiptAppNoticeEntity[].class);
			
			List<ReceiptAppNotice> receiptAppNoticeList = new ArrayList<>();
			for (ReceiptAppNoticeEntity receiptAppNoticeEntity : result.getBody()) {
				receiptAppNoticeList.add(convertFromEntity(receiptAppNoticeEntity));
			}
			return receiptAppNoticeList.toArray(new ReceiptAppNotice[receiptAppNoticeList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public ReceiptAppNotice getReceiptAppNotice (String languageId, Long classId, String matterNumber, String receiptNo, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "receiptappnotice/" + receiptNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("matterNumber", matterNumber);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ReceiptAppNoticeEntity> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ReceiptAppNoticeEntity.class);
			return convertFromEntity(result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public ReceiptAppNotice createReceiptAppNotice (ReceiptAppNotice newReceiptAppNotice, String loginUserID, String authToken) 
			throws UploadErrorException, DbxException, ParseException {
		ReceiptAppNoticeEntity receiptAppNoticeEntity = convertToEntity(newReceiptAppNotice);		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "receiptappnotice")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(receiptAppNoticeEntity, headers);
		ResponseEntity<ReceiptAppNoticeEntity> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ReceiptAppNoticeEntity.class);
		return convertFromEntity(result.getBody());
	}
	
	// PATCH
	public ReceiptAppNotice updateReceiptAppNotice (String languageId, Long classId, String matterNumber, String receiptNo, 
			String loginUserID, ReceiptAppNotice modifiedReceiptAppNotice, String authToken) throws Exception {
		try {
			UpdateReceiptAppNoticeEntity receiptAppNoticeEntity = convertToUpdateEntity (modifiedReceiptAppNotice);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(receiptAppNoticeEntity, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "receiptappnotice/" + receiptNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("matterNumber", matterNumber)
					.queryParam("loginUserID", loginUserID);
			
			ResponseEntity<UpdateReceiptAppNoticeEntity> result = 
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UpdateReceiptAppNoticeEntity.class);
			return convertFromUpdateEntity(result.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// DELETE
	public boolean deleteReceiptAppNotice (String languageId, Long classId, String matterNumber, 
			String receiptNo, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "receiptappnotice/" + receiptNo)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("matterNumber", matterNumber)
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
	
	/**
	 * 
	 * @param newReceiptAppNotice
	 * @return
	 * @throws ParseException 
	 */
	private ReceiptAppNoticeEntity convertToEntity (ReceiptAppNotice newReceiptAppNotice) throws ParseException {
		ReceiptAppNoticeEntity receiptAppNoticeEntity = new ReceiptAppNoticeEntity();
		BeanUtils.copyProperties(newReceiptAppNotice, receiptAppNoticeEntity, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(newReceiptAppNotice));
		if (newReceiptAppNotice.getReceiptNoticeDate() != null) {
			receiptAppNoticeEntity.setReceiptNoticeDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReceiptNoticeDate()));
		}
		
		if (newReceiptAppNotice.getReceiptDate() != null) {
			receiptAppNoticeEntity.setReceiptDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReceiptDate()));
		}
		
		if (newReceiptAppNotice.getDateGovt() != null) {
			receiptAppNoticeEntity.setDateGovt(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getDateGovt()));
		}
		
		if (newReceiptAppNotice.getApprovedOn() != null) {
			receiptAppNoticeEntity.setApprovedOn(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovedOn()));
		}
		
		if (newReceiptAppNotice.getApprovalReceiptDate() != null) {
			receiptAppNoticeEntity.setApprovalReceiptDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovalReceiptDate()));
		}
		
		if (newReceiptAppNotice.getApprovalDate() != null) {
			receiptAppNoticeEntity.setApprovalDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovalDate()));
		}
		
		if (newReceiptAppNotice.getExpirationDate() != null) {
			receiptAppNoticeEntity.setExpirationDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getExpirationDate()));
		}
		
		if (newReceiptAppNotice.getEligibiltyDate() != null) {
			receiptAppNoticeEntity.setEligibiltyDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getEligibiltyDate()));
		}
		
		if (newReceiptAppNotice.getReminderDate() != null) {
			receiptAppNoticeEntity.setReminderDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReminderDate()));
		}
		
		if (newReceiptAppNotice.getCreatedOn() != null) {
			receiptAppNoticeEntity.setCreatedOn(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getCreatedOn()));
		}
		
		return receiptAppNoticeEntity;
	}
	
	/**
	 * 
	 * @param newReceiptAppNotice
	 * @return
	 * @throws ParseException
	 */
	private UpdateReceiptAppNoticeEntity convertToUpdateEntity (ReceiptAppNotice newReceiptAppNotice) throws ParseException {
		UpdateReceiptAppNoticeEntity receiptAppNoticeEntity = new UpdateReceiptAppNoticeEntity();
		BeanUtils.copyProperties(newReceiptAppNotice, receiptAppNoticeEntity, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(newReceiptAppNotice));
		if (newReceiptAppNotice.getReceiptNoticeDate() != null) {
			receiptAppNoticeEntity.setReceiptNoticeDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReceiptNoticeDate()));
		}
		
		if (newReceiptAppNotice.getReceiptDate() != null) {
			receiptAppNoticeEntity.setReceiptDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReceiptDate()));
		}
		
		if (newReceiptAppNotice.getDateGovt() != null) {
			receiptAppNoticeEntity.setDateGovt(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getDateGovt()));
		}
		
		if (newReceiptAppNotice.getApprovedOn() != null) {
			receiptAppNoticeEntity.setApprovedOn(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovedOn()));
		}
		
		if (newReceiptAppNotice.getApprovalReceiptDate() != null) {
			receiptAppNoticeEntity.setApprovalReceiptDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovalReceiptDate()));
		}
		
		if (newReceiptAppNotice.getApprovalDate() != null) {
			receiptAppNoticeEntity.setApprovalDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getApprovalDate()));
		}
		
		if (newReceiptAppNotice.getExpirationDate() != null) {
			receiptAppNoticeEntity.setExpirationDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getExpirationDate()));
		}
		
		if (newReceiptAppNotice.getEligibiltyDate() != null) {
			receiptAppNoticeEntity.setEligibiltyDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getEligibiltyDate()));
		}
		
		if (newReceiptAppNotice.getReminderDate() != null) {
			receiptAppNoticeEntity.setReminderDate(DateUtils.convertStringToYYYYMMDD(newReceiptAppNotice.getReminderDate()));
		}
		
		return receiptAppNoticeEntity;
	}
	
	/**
	 * 
	 * @param receiptAppNoticeEntity
	 * @return
	 */
	private ReceiptAppNotice convertFromEntity (ReceiptAppNoticeEntity receiptAppNoticeEntity) {
		ReceiptAppNotice newReceiptAppNotice = new ReceiptAppNotice();
		BeanUtils.copyProperties(receiptAppNoticeEntity, newReceiptAppNotice, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(receiptAppNoticeEntity));
		if (receiptAppNoticeEntity.getReceiptNoticeDate() != null) {
			newReceiptAppNotice.setReceiptNoticeDate(DateUtils.dateToString(receiptAppNoticeEntity.getReceiptNoticeDate()));
		}
		
		if (receiptAppNoticeEntity.getReceiptDate() != null) {
			newReceiptAppNotice.setReceiptDate(DateUtils.dateToString(receiptAppNoticeEntity.getReceiptDate()));
		}
		
		if (receiptAppNoticeEntity.getDateGovt() != null) {
			newReceiptAppNotice.setDateGovt(DateUtils.dateToString(receiptAppNoticeEntity.getDateGovt()));
		}
		
		if (receiptAppNoticeEntity.getApprovedOn() != null) {
			newReceiptAppNotice.setApprovedOn(DateUtils.dateToString(receiptAppNoticeEntity.getApprovedOn()));
		}
		
		if (receiptAppNoticeEntity.getApprovalReceiptDate() != null) {
			newReceiptAppNotice.setApprovalReceiptDate(DateUtils.dateToString(receiptAppNoticeEntity.getApprovalReceiptDate()));
		}
		
		if (receiptAppNoticeEntity.getApprovalDate() != null) {
			newReceiptAppNotice.setApprovalDate(DateUtils.dateToString(receiptAppNoticeEntity.getApprovalDate()));
		}
		
		if (receiptAppNoticeEntity.getExpirationDate() != null) {
			newReceiptAppNotice.setExpirationDate(DateUtils.dateToString(receiptAppNoticeEntity.getExpirationDate()));
		}
		
		if (receiptAppNoticeEntity.getEligibiltyDate() != null) {
			newReceiptAppNotice.setEligibiltyDate(DateUtils.dateToString(receiptAppNoticeEntity.getEligibiltyDate()));
		}
		
		if (receiptAppNoticeEntity.getReminderDate() != null) {
			newReceiptAppNotice.setReminderDate(DateUtils.dateToString(receiptAppNoticeEntity.getReminderDate()));
		}
		
		if (receiptAppNoticeEntity.getCreatedOn() != null) {
			newReceiptAppNotice.setCreatedOn(DateUtils.dateToString(receiptAppNoticeEntity.getCreatedOn()));
		}
		
		return newReceiptAppNotice;
	}
	
	/**
	 * 
	 * @param receiptAppNoticeEntity
	 * @return
	 */
	private ReceiptAppNotice convertFromUpdateEntity (UpdateReceiptAppNoticeEntity receiptAppNoticeEntity) {
		ReceiptAppNotice newReceiptAppNotice = new ReceiptAppNotice();
		BeanUtils.copyProperties(receiptAppNoticeEntity, newReceiptAppNotice, 
				com.mnrclara.wrapper.core.util.CommonUtils.getNullPropertyNames(receiptAppNoticeEntity));
		if (receiptAppNoticeEntity.getReceiptNoticeDate() != null) {
			newReceiptAppNotice.setReceiptNoticeDate(DateUtils.dateToString(receiptAppNoticeEntity.getReceiptNoticeDate()));
		}
		
		if (receiptAppNoticeEntity.getReceiptDate() != null) {
			newReceiptAppNotice.setReceiptDate(DateUtils.dateToString(receiptAppNoticeEntity.getReceiptDate()));
		}
		
		if (receiptAppNoticeEntity.getDateGovt() != null) {
			newReceiptAppNotice.setDateGovt(DateUtils.dateToString(receiptAppNoticeEntity.getDateGovt()));
		}
		
		if (receiptAppNoticeEntity.getApprovedOn() != null) {
			newReceiptAppNotice.setApprovedOn(DateUtils.dateToString(receiptAppNoticeEntity.getApprovedOn()));
		}
		
		if (receiptAppNoticeEntity.getApprovalReceiptDate() != null) {
			newReceiptAppNotice.setApprovalReceiptDate(DateUtils.dateToString(receiptAppNoticeEntity.getApprovalReceiptDate()));
		}
		
		if (receiptAppNoticeEntity.getApprovalDate() != null) {
			newReceiptAppNotice.setApprovalDate(DateUtils.dateToString(receiptAppNoticeEntity.getApprovalDate()));
		}
		
		if (receiptAppNoticeEntity.getExpirationDate() != null) {
			newReceiptAppNotice.setExpirationDate(DateUtils.dateToString(receiptAppNoticeEntity.getExpirationDate()));
		}
		
		if (receiptAppNoticeEntity.getEligibiltyDate() != null) {
			newReceiptAppNotice.setEligibiltyDate(DateUtils.dateToString(receiptAppNoticeEntity.getEligibiltyDate()));
		}
		
		if (receiptAppNoticeEntity.getReminderDate() != null) {
			newReceiptAppNotice.setReminderDate(DateUtils.dateToString(receiptAppNoticeEntity.getReminderDate()));
		}
		
		return newReceiptAppNotice;
	}
	
	//--------------------------------------------MatterDocList------------------------------------------------------------------------
	// GET ALL
	public MatterDocList[] getMatterDocLists (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocList[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocList[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// GET
	public MatterDocList getMatterDocList (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist/" + matterNumber)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("checkListNo", checkListNo)
					.queryParam("matterNumber", matterNumber)
					.queryParam("clientId", clientId);
			HttpEntity<?> entity = new HttpEntity<>(headers);	
			ResponseEntity<MatterDocList> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocList.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - findMatterDocList
	public MatterDocList[] findMatterDocList(SearchMatterDocList searchMatterDocList, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist/findMatterDocList");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterDocList, headers);	
			ResponseEntity<MatterDocList[]> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterDocList[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST
	public MatterDocList[] createMatterDocList (@Valid List<MatterDocList> newMatterDocList, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newMatterDocList, headers);
		ResponseEntity<MatterDocList[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterDocList[].class);
		return result.getBody();
	}
	
	// PATCH
	public MatterDocListHeader updateMatterDocList(String clientId, String matterNumber, Long checkListNo, Long matterHeaderId, String loginUserID,
			String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara's RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpClient client = HttpClients.createDefault();
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist/" + matterNumber
						+ "/clientPortal/docCheckList")
				.queryParam("checkListNo", checkListNo)
				.queryParam("matterNumber", matterNumber)
				.queryParam("clientId", clientId)
				.queryParam("matterHeaderId", matterHeaderId)
				.queryParam("loginUserID", loginUserID);
		ResponseEntity<MatterDocListHeader> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocListHeader.class);
		return result.getBody();
	}
	
	// DELETE
	public boolean deleteMatterDocList (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = 
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclist/" + matterNumber)
					.queryParam("languageId", languageId)
					.queryParam("classId", classId)
					.queryParam("checkListNo", checkListNo)
					.queryParam("matterNumber", matterNumber)
					.queryParam("clientId", clientId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------MatterDocListHeader------------------------------------------------------------------------
	// GET ALL
	public MatterDocListHeader[] getMatterDocListHeaders (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocListHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocListHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MatterDocListHeader getMatterDocListHeader (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/" + matterNumber)
							.queryParam("languageId", languageId)
							.queryParam("classId", classId)
							.queryParam("checkListNo", checkListNo)
							.queryParam("clientId", clientId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocListHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocListHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET-New
	public MatterDocListHeader getMatterDocListHeader ( Long matterHeaderId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/new/" + matterHeaderId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MatterDocListHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MatterDocListHeader.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	// POST
	public MatterDocListHeader createMatterDocListHeader (@Valid AddMatterDocListHeader newMatterDocListHeader, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newMatterDocListHeader, headers);
		ResponseEntity<MatterDocListHeader> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterDocListHeader.class);
		return result.getBody();
	}


	// PATCH
	public MatterDocListHeader updateMatterDocListHeader(@Valid UpdateMatterDocListHeader updateMatterDocListHeader, String clientId, String matterNumber, Long checkListNo, Long classId,String languageId, String loginUserID,
														 String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara's RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		HttpEntity<?> entity = new HttpEntity<>(updateMatterDocListHeader,headers);
		HttpClient client = HttpClients.createDefault();
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/" + matterNumber)
						.queryParam("checkListNo", checkListNo)
						.queryParam("clientId", clientId)
						.queryParam("languageId", languageId)
						.queryParam("classId", classId)
						.queryParam("loginUserID", loginUserID);
		ResponseEntity<MatterDocListHeader> result =
				restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterDocListHeader.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	// PATCH-New
	public MatterDocListHeader updateMatterDocListHeader(@Valid UpdateMatterDocListHeader updateMatterDocListHeader, Long matterHeaderId, String loginUserID,
														 String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara's RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		HttpEntity<?> entity = new HttpEntity<>(updateMatterDocListHeader,headers);
		HttpClient client = HttpClients.createDefault();
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/new/" + matterHeaderId)
						.queryParam("loginUserID", loginUserID);
		ResponseEntity<MatterDocListHeader> result =
				restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MatterDocListHeader.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// DELETE
	public boolean deleteMatterDocListHeader (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/" + matterNumber)
							.queryParam("languageId", languageId)
							.queryParam("classId", classId)
							.queryParam("checkListNo", checkListNo)
							.queryParam("matterNumber", matterNumber)
							.queryParam("clientId", clientId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE-New
	public boolean deleteMatterDocListHeader (Long matterHeaderId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/new/" + matterHeaderId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// POST - findMatterDocListHeader
	public MatterDocListHeader[] findMatterDocListHeader(FindMatterDocListHeader searchMatterDocList, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "matterdoclistheader/find");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterDocList, headers);
			ResponseEntity<MatterDocListHeader[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterDocListHeader[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//---------------------------------------Reports-----------------------------------------------------------
	// LNE Report
	public ClientGeneralLNEReport[] getLNEClientGeneralReport(SearchClientGeneralLNEReport searchClientGeneralReport,
			String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "clientgeneral/lneReport");

		HttpEntity<?> entity = new HttpEntity<>(searchClientGeneralReport, headers);
		ResponseEntity<ClientGeneralLNEReport[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
						ClientGeneralLNEReport[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// Immigration Report
	public ClientGeneralIMMReport[] getIMMClientGeneralReport(
			SearchClientGeneralIMMReport searchClientGeneralIMMReport, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "clientgeneral/immigrationReport");
		HttpEntity<?> entity = new HttpEntity<>(searchClientGeneralIMMReport, headers);
		ResponseEntity<ClientGeneralIMMReport[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
						ClientGeneralIMMReport[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// GET
	public MatterImmigrationReport[] getMatterImmigrationReport(ImmigrationMatter immigrationMatter,
			String authToken) throws ParseException{
        try {
            if (immigrationMatter.getSearchMatterIMMReport().getSFromCaseOpenDate() != null) {
                immigrationMatter.getSearchMatterIMMReport().setFromCaseOpenDate(DateUtils.convertStringToYYYYMMDD(immigrationMatter.getSearchMatterIMMReport().getSFromCaseOpenDate()));
            }
            if (immigrationMatter.getSearchMatterIMMReport().getSToCaseOpenDate() != null) {
				immigrationMatter.getSearchMatterIMMReport().setToCaseOpenDate(DateUtils.convertStringToYYYYMMDD(immigrationMatter.getSearchMatterIMMReport().getSToCaseOpenDate()));
            }
			if (immigrationMatter.getSearchMatterIMMReport().getSFromCaseClosedDate() != null) {
				immigrationMatter.getSearchMatterIMMReport().setFromCaseClosedDate(DateUtils.convertStringToYYYYMMDD(immigrationMatter.getSearchMatterIMMReport().getSFromCaseClosedDate()));
			}
			if (immigrationMatter.getSearchMatterIMMReport().getSToCaseClosedDate() != null) {
				immigrationMatter.getSearchMatterIMMReport().setToCaseClosedDate(DateUtils.convertStringToYYYYMMDD(immigrationMatter.getSearchMatterIMMReport().getSToCaseClosedDate()));
			}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/immigrationReport");
		HttpEntity<?> entity = new HttpEntity<>(immigrationMatter, headers);
		ResponseEntity<MatterImmigrationReport[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
						MatterImmigrationReport[].class);
//		log.info("result : " + result.getStatusCode());
//		return result.getBody();
			MatterImmigrationReport[] arrResponseMatterImmigrationReport = result.getBody();
            List<MatterImmigrationReport> matterImmigrationReport = new ArrayList<>();
            for (MatterImmigrationReport matterImmigration : arrResponseMatterImmigrationReport) {
				if(matterImmigration.getMatterOpenedDate()!=null){
				matterImmigration.setSMatterOpenedDate(DateUtils.dateToString(matterImmigration.getMatterOpenedDate()));}
				if(matterImmigration.getMatterClosedDate()!=null){
				matterImmigration.setSMatterClosedDate(DateUtils.dateToString(matterImmigration.getMatterClosedDate()));}
				matterImmigrationReport.add(matterImmigration);
            }
			MatterImmigrationReport[] arrMatterImmigration = matterImmigrationReport.toArray(new MatterImmigrationReport[matterImmigrationReport.size()]);
            return arrMatterImmigration;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
	}

	// Attorney Productivity Report
	public AttorneyProductivityReport[] getAttorneyProductivityReport(AttorneyProductivityInput attorneyProductivityInput,
																   String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/attorneyProductivityReport");
		HttpEntity<?> entity = new HttpEntity<>(attorneyProductivityInput, headers);
		ResponseEntity<AttorneyProductivityReport[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
						AttorneyProductivityReport[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// POST -  MatterLNEReport
	public MatterLNEReport[] getMatterLNEReport(LNEMatter lneMatter, String authToken) throws ParseException{
		try {
			if (lneMatter.getSearchMatterLNEReport().getSFromCaseOpenDate() != null) {
				lneMatter.getSearchMatterLNEReport().setFromCaseOpenDate(DateUtils.convertStringToYYYYMMDD(lneMatter.getSearchMatterLNEReport().getSFromCaseOpenDate()));
			}
			if (lneMatter.getSearchMatterLNEReport().getSToCaseOpenDate() != null) {
				lneMatter.getSearchMatterLNEReport().setToCaseOpenDate(DateUtils.convertStringToYYYYMMDD(lneMatter.getSearchMatterLNEReport().getSToCaseOpenDate()));
			}
			if (lneMatter.getSearchMatterLNEReport().getSFromCaseClosedDate() != null) {
				lneMatter.getSearchMatterLNEReport().setFromCaseClosedDate(DateUtils.convertStringToYYYYMMDD(lneMatter.getSearchMatterLNEReport().getSFromCaseClosedDate()));
			}
			if (lneMatter.getSearchMatterLNEReport().getSToCaseClosedDate() != null) {
				lneMatter.getSearchMatterLNEReport().setToCaseClosedDate(DateUtils.convertStringToYYYYMMDD(lneMatter.getSearchMatterLNEReport().getSToCaseClosedDate()));
			}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/lneReport");
		HttpEntity<?> entity = new HttpEntity<>(lneMatter, headers);
		ResponseEntity<MatterLNEReport[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
						MatterLNEReport[].class);
//		return result.getBody();
			MatterLNEReport[] arrResponseMatterLNEReport = result.getBody();
			List<MatterLNEReport> matterLNEReportList = new ArrayList<>();
			for (MatterLNEReport matterLNEReport : arrResponseMatterLNEReport) {
				matterLNEReport.setSMatterOpenedDate(DateUtils.dateToString(matterLNEReport.getMatterOpenedDate()));
				matterLNEReport.setSMatterClosedDate(DateUtils.dateToString(matterLNEReport.getMatterClosedDate()));
				matterLNEReportList.add(matterLNEReport);
			}
			MatterLNEReport[] arrMatterLNEReport = matterLNEReportList.toArray(new MatterLNEReport[matterLNEReportList.size()]);
			return arrMatterLNEReport;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	// POST - WIPAgedPBReport
	public WIPAgedPBReport[] getWIPAgedPBReport(WIPAgedPBReportInput wipAgedPBReportInput, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/wipAgedPBReport");
		HttpEntity<?> entity = new HttpEntity<>(wipAgedPBReportInput, headers);
		ResponseEntity<WIPAgedPBReport[]> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
						WIPAgedPBReport[].class);
		return result.getBody();
	}
	
	// POST - WIPAgedPBReport
	public Page<?> getWIPAgedPBReport(WIPAgedPBReportInput wipAgedPBReportInput, Integer pageNo, Integer pageSize, 
			String sortBy, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/wipAgedPBReport/pagination")
					.queryParam("pageNo", pageNo)
					.queryParam("pageSize", pageSize)
					.queryParam("sortBy", sortBy);
			HttpEntity<?> entity = new HttpEntity<>(wipAgedPBReportInput, headers);
			ParameterizedTypeReference<PaginatedResponse<WIPAgedPBReport>> responseType = 
					new ParameterizedTypeReference<PaginatedResponse<WIPAgedPBReport>>() {};
			ResponseEntity<PaginatedResponse<WIPAgedPBReport>> result = 
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, responseType);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/*
	 * -----------------------BATCH-INSERT----------------------------------------------------------------
	 */
	// POST - ClientGeneral
	public String createBulkClientGenerals(com.mnrclara.wrapper.core.model.datamigration.ClientGeneral[] arrClientGeneral, 
			String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/clientgeneral/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(arrClientGeneral, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - MatterAssignment
	public String createBulkMatterAssignments(com.mnrclara.wrapper.core.model.datamigration.MatterAssignment[] arrMatterAssignment, 
			String loginUserID, String authToken) {
		try {
			List<MatterAssignment> serviceMatterAssignmentList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterAssignment bulkMatterAssignment : arrMatterAssignment) {
				MatterAssignment matterAssignment = new MatterAssignment();
				BeanUtils.copyProperties(bulkMatterAssignment, matterAssignment, CommonUtils.getNullPropertyNames(bulkMatterAssignment));
				if (bulkMatterAssignment.getCaseOpenedDate() != null) {
					matterAssignment.setCaseOpenedDate(DateUtils.convertStringToDate(bulkMatterAssignment.getCaseOpenedDate()));
				}
				
				if (bulkMatterAssignment.getCreatedOn() != null) {
					matterAssignment.setCreatedOn(DateUtils.convertStringToDate(bulkMatterAssignment.getCreatedOn()));
				}
				serviceMatterAssignmentList.add(matterAssignment);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterassignment/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterAssignmentList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - MatterExpense
	public String createBulkMatterExpenses(com.mnrclara.wrapper.core.model.datamigration.MatterExpense[] arrMatterExpense, String loginUserID, String authToken) {
		try {
			List<MatterExpense> serviceMatterExpenseList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterExpense bulkMatterExpense : arrMatterExpense) {
				MatterExpense matterExpense = new MatterExpense();
				BeanUtils.copyProperties(bulkMatterExpense, matterExpense, CommonUtils.getNullPropertyNames(bulkMatterExpense));
				
				if (bulkMatterExpense.getCreatedOn() != null) {
					matterExpense.setCreatedOn(DateUtils.convertStringToDate(bulkMatterExpense.getCreatedOn()));
				}
				serviceMatterExpenseList.add(matterExpense);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterexpense/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterExpenseList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}

	// POST - MatterTimeTicket
	public String createBulkMatterTimeTickets(com.mnrclara.wrapper.core.model.datamigration.MatterTimeTicket[] arrMatterTimeTicket, 
			String loginUserID, String authToken) {
		try {
			List<MatterTimeTicket> serviceMatterTimeTicketList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterTimeTicket bulkMatterTimeTicket : arrMatterTimeTicket) {
				MatterTimeTicket matterTimeTicket = new MatterTimeTicket();
				BeanUtils.copyProperties(bulkMatterTimeTicket, matterTimeTicket, CommonUtils.getNullPropertyNames(bulkMatterTimeTicket));
				
				if (bulkMatterTimeTicket.getTimeTicketDate() != null) {
					matterTimeTicket.setTimeTicketDate(DateUtils.convertStringToDate(bulkMatterTimeTicket.getTimeTicketDate()));
				}
				
				if (bulkMatterTimeTicket.getCreatedOn() != null) {
					matterTimeTicket.setCreatedOn(DateUtils.convertStringToDate(bulkMatterTimeTicket.getCreatedOn()));
				}
				serviceMatterTimeTicketList.add(matterTimeTicket);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattertimeticket/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterTimeTicketList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}				
	}

	// POST - MatterNote
	public String createBulkMatterNotes(com.mnrclara.wrapper.core.model.datamigration.MatterNote[] arrMatterNote, String loginUserID, String authToken) {
		try {
			List<MatterNote> serviceMatterNoteList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterNote bulkMatterNote : arrMatterNote) {
				MatterNote matterNote = new MatterNote();
				BeanUtils.copyProperties(bulkMatterNote, matterNote, CommonUtils.getNullPropertyNames(bulkMatterNote));
				
				if (bulkMatterNote.getCreatedOn() != null && bulkMatterNote.getCreatedOn().trim().length() > 0) {
					matterNote.setCreatedOn(DateUtils.convertStringToDate(bulkMatterNote.getCreatedOn()));
				}
				serviceMatterNoteList.add(matterNote);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("ClientGeneral-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matternote/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterNoteList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}					
	}

	// POST - MatterGenAcc
	public String createBulkMatterGenerals(com.mnrclara.wrapper.core.model.datamigration.MatterGenAcc[] arrMatterGenAcc, 
			String loginUserID, String authToken) {
		try {
			/*
			 * Converting all Dates
			 * private Date caseOpenedDate;
				private Date caseClosedDate;
				private Date caseFiledDate;
				private Date priorityDate;
				private Date receiptDate;
				private Date expirationDate;
				private Date courtDate;
				private Date approvalDate;
				private Date createdOn;
				private Date updatedOn;
			 */
			List<MatterGenAcc> serviceMatterList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterGenAcc bulkMatter : arrMatterGenAcc) {
				MatterGenAcc matterGenAcc = new MatterGenAcc();
				BeanUtils.copyProperties(bulkMatter, matterGenAcc, CommonUtils.getNullPropertyNames(bulkMatter));
				if (bulkMatter.getCaseOpenedDate() != null) {
					matterGenAcc.setCaseOpenedDate(DateUtils.convertStringToDate(bulkMatter.getCaseOpenedDate()));
				}
				
				if (bulkMatter.getCaseClosedDate() != null && bulkMatter.getCaseClosedDate().trim().length() > 0) {
					matterGenAcc.setCaseClosedDate(DateUtils.convertStringToDate(bulkMatter.getCaseClosedDate()));
				}
				
				if (bulkMatter.getCaseFiledDate() != null && bulkMatter.getCaseFiledDate().trim().length() > 0) {
					matterGenAcc.setCaseFiledDate(DateUtils.convertStringToDate(bulkMatter.getCaseFiledDate()));
				}
				
				if (bulkMatter.getPriorityDate() != null && bulkMatter.getPriorityDate().trim().length() > 0) {
					matterGenAcc.setPriorityDate(DateUtils.convertStringToDate(bulkMatter.getPriorityDate()));
				}
				
				if (bulkMatter.getReceiptDate() != null && bulkMatter.getReceiptDate().trim().length() > 0) {
					matterGenAcc.setReceiptDate(DateUtils.convertStringToDate(bulkMatter.getReceiptDate()));
				}
				
				if (bulkMatter.getExpirationDate() != null && bulkMatter.getExpirationDate().trim().length() > 0) {
					matterGenAcc.setExpirationDate(DateUtils.convertStringToDate(bulkMatter.getExpirationDate()));
				}
				
				if (bulkMatter.getCourtDate() != null && bulkMatter.getCourtDate().trim().length() > 0) {
					matterGenAcc.setCourtDate(DateUtils.convertStringToDate(bulkMatter.getCourtDate()));
				}
				
				if (bulkMatter.getApprovalDate() != null && bulkMatter.getApprovalDate().trim().length() > 0) {
					matterGenAcc.setApprovalDate(DateUtils.convertStringToDate(bulkMatter.getApprovalDate()));
				}
				
				if (bulkMatter.getCreatedOn() != null && bulkMatter.getCreatedOn().trim().length() > 0) {
					matterGenAcc.setCreatedOn(DateUtils.convertStringToDate(bulkMatter.getCreatedOn()));
				}
				
				if (bulkMatter.getUpdatedOn() != null && bulkMatter.getUpdatedOn().trim().length() > 0) {
					matterGenAcc.setUpdatedOn(DateUtils.convertStringToDate(bulkMatter.getUpdatedOn()));
				}
				
				serviceMatterList.add(matterGenAcc);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/mattergenacc/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}					
	}

	// POST - MatterRates
	public String createBulkMatterRates(com.mnrclara.wrapper.core.model.datamigration.MatterRate[] arrMatterRates,
			String loginUserID, String authToken) {
		try {
			List<MatterRate> serviceMatterRateList = new ArrayList<>();
			for (com.mnrclara.wrapper.core.model.datamigration.MatterRate bulkMatterRate : arrMatterRates) {
				MatterRate matterRate = new MatterRate();
				BeanUtils.copyProperties(bulkMatterRate, matterRate, CommonUtils.getNullPropertyNames(bulkMatterRate));
				
				if (bulkMatterRate.getCreatedOn() != null) {
					matterRate.setCreatedOn(DateUtils.convertStringToDate(bulkMatterRate.getCreatedOn()));
				}
				serviceMatterRateList.add(matterRate);
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getManagementServiceUrl() + "/matterrate/batch")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(serviceMatterRateList, headers);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, String.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}							
	}
	
	//------------------------------------------------QBSycn------------------------------------------------------------------

	// POST - QBSync
	public QBSync createQbSync(QBSync qbSync, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(getManagementServiceUrl() + "/qbsync");
		HttpEntity<?> entity = new HttpEntity<>(qbSync, headers);
		ResponseEntity<QBSync> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
				entity, QBSync.class);
		return result.getBody();
	}
	
	// GET ALL
	public QBSync[] getQBSync(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/qbsync");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<QBSync[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, 
				QBSync[].class);
		return result.getBody();
	}

	// FIND - QBSYNC
	public QBSync[] findQbSync(SearchQbSync searchQbSync, String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + access_token);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(getManagementServiceUrl() + "/qbsync/findQbSync");
		HttpEntity<?> entity = new HttpEntity<>(searchQbSync, headers);
		ResponseEntity<QBSync[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
				entity, QBSync[].class);
		return result.getBody();
	}
	
	// PATCH
	public QBSync updateQbSync(String id, QBSync qbSync, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara's RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		
		HttpEntity<?> entity = new HttpEntity<>(qbSync, headers);
		HttpClient client = HttpClients.createDefault();
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client)); 
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "/qbsync/" + id);
		ResponseEntity<QBSync> result = 
				restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, QBSync.class);
		return result.getBody();
	}

	// POST - academicReport
	public AcademicReport[] getAcademicReport(AcademicReportInput academicReportInput, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "MNRClara RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattergenacc/academicReport");
		HttpEntity<?> entity = new HttpEntity<>(academicReportInput, headers);
		ResponseEntity<AcademicReport[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
						AcademicReport[].class);
		return result.getBody();
	}

	//FIND TimeTicketNotification
	public TimeTicketNotification[] findTimeTicketNotification(FindTimeTicketNotification findTimeTicketNotification, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattertimeticket/findTimeTicketNotification");
			HttpEntity<?> entity = new HttpEntity<>(findTimeTicketNotification, headers);
			ResponseEntity<TimeTicketNotification[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TimeTicketNotification[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//FIND IMatterTimeTicket
	public MatterTimeTicket[] findMatterTimeTicketMobile(SearchMatterTimeTicket searchMatterTimeTicket, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getManagementServiceUrl() + "mattertimeticket/findIMatterTimeTicket");
			HttpEntity<?> entity = new HttpEntity<>(searchMatterTimeTicket, headers);
			ResponseEntity<MatterTimeTicket[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MatterTimeTicket[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
}
