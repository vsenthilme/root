package com.mnrclara.wrapper.core.service;

import java.util.Collections;

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
import com.mnrclara.wrapper.core.model.crm.itform.ITForm000;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm001;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm002;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm002Att;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm003;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm003Att;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm004;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm004Att;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm005;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm006;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm007;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm008;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm009;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm010;
import com.mnrclara.wrapper.core.model.crm.itform.ITForm011;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CRMITFormService {

	@Autowired
	PropertiesConfig propertiesConfig;

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getCRMServiceUrl() {
		return propertiesConfig.getCrmServiceUrl();
	}

	// --------------------------------------------------ITForm000--------------------------------------------------------------------

	public ITForm000 getITForm000(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform000/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm000> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm000.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-001-------------------------------------------------------------------
	// GET - /itform001
	public ITForm001[] getITForm001s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform001");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm001[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm001[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm001 getITForm001(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform001/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm001> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm001.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform001
	public ITForm001 createITForm001(@Valid ITForm001 newITForm001, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform001")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm001, headers);
			ResponseEntity<ITForm001> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm001.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform001
	public ITForm001 updateITForm001(ITForm001 modifiedITForm001, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm001, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform001")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm001> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm001.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-001-------------------------------------------------------------------
	// GET - /itform002
	public ITForm002[] getITForm002s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm002[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm002[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm002 getITForm002(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm002> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm002.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform002
	public ITForm002 createITForm002(@Valid ITForm002 newITForm002, String loginUserID, String authToken) {
		try {
			log.info("newITForm002 : " + newITForm002);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm002, headers);
			ResponseEntity<ITForm002> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm002.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform002
	public ITForm002 updateITForm002(ITForm002 modifiedITForm002, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm002, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm002> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm002.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GET - /id
	public ITForm002Att getITForm002Att(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002/attorney")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm002Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm002Att.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform002
	public ITForm002Att createITForm002Att(@Valid ITForm002Att newITForm002Att, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform002/attorney")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm002Att, headers);
			ResponseEntity<ITForm002Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm002Att.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-003-------------------------------------------------------------------
	// GET - /itform003
	public ITForm003[] getITForm003s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm003[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm003[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm003 getITForm003(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm003> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm003.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform003
	public ITForm003 createITForm003(@Valid ITForm003 newITForm003, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm003, headers);
			ResponseEntity<ITForm003> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm003.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform003
	public ITForm003 updateITForm003(ITForm003 modifiedITForm003, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm003, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm003> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm003.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm003Att getITForm003Att(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003/attorney")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm003Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm003Att.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform003
	public ITForm003Att createITForm003Att(@Valid ITForm003Att newITForm003Att, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform003/attorney")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm003Att, headers);
			ResponseEntity<ITForm003Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm003Att.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-004-------------------------------------------------------------------
	// GET - /itform004
	public ITForm004[] getITForm004s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm004[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm004[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm004 getITForm004(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm004> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm004.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform004
	public ITForm004 createITForm004(@Valid ITForm004 newITForm004, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm004, headers);
			ResponseEntity<ITForm004> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm004.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform004
	public ITForm004 updateITForm004(ITForm004 modifiedITForm004, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm004, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm004> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm004.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GET - /id
	public ITForm004Att getITForm004Att(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004/attorney")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm004Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm004Att.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform004Att
	public ITForm004Att createITForm004Att(@Valid ITForm004Att newITForm004Att, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform004/attorney")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm004Att, headers);
			ResponseEntity<ITForm004Att> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm004Att.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-005-------------------------------------------------------------------
	// GET - /itform005
	public ITForm005[] getITForm005s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform005");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm005[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm005[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm005 getITForm005(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform005/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm005> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm005.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform005
	public ITForm005 createITForm005(@Valid ITForm005 newITForm005, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform005")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm005, headers);
			ResponseEntity<ITForm005> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm005.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform005
	public ITForm005 updateITForm005(ITForm005 modifiedITForm005, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm005, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform005")
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ITForm005> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm005.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-006-------------------------------------------------------------------
	// GET - /itform006
	public ITForm006[] getITForm006s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform006");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm006[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm006[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm006 getITForm006(String inquiryNo, Long classID, String language, String itFormNo, Long itFormID,
			String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform006/id")
					.queryParam("inquiryNo", inquiryNo).queryParam("classID", classID).queryParam("language", language)
					.queryParam("itFormNo", itFormNo).queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm006> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm006.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform006
	public ITForm006 createITForm006(@Valid ITForm006 newITForm006, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform006")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm006, headers);
			ResponseEntity<ITForm006> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm006.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform006
	public ITForm006 updateITForm006(ITForm006 modifiedITForm006, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm006, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform006")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm006> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm006.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-007-------------------------------------------------------------------
	// GET - /itform007
	public ITForm007[] getITForm007s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform007");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm007[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm007[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm007 getITForm007(String language, Long classID, String matterNumber, String clientId, String itFormNo,
			Long itFormID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform007/id")
					.queryParam("matterNumber", matterNumber).queryParam("classID", classID)
					.queryParam("language", language).queryParam("clientId", clientId).queryParam("itFormNo", itFormNo)
					.queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm007> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm007.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform007
	public ITForm007 createITForm007(@Valid ITForm007 newITForm007, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform007")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm007, headers);
			ResponseEntity<ITForm007> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm007.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform007
	public ITForm007 updateITForm007(ITForm007 modifiedITForm007, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm007, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform007")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm007> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm007.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-008-------------------------------------------------------------------
	// GET - /itform008
	public ITForm008[] getITForm008s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform008");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm008[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm008[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm008 getITForm008(String language, Long classID, String matterNumber, String clientId, String itFormNo,
			Long itFormID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform008/id")
					.queryParam("matterNumber", matterNumber).queryParam("classID", classID)
					.queryParam("language", language).queryParam("clientId", clientId).queryParam("itFormNo", itFormNo)
					.queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm008> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm008.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform008
	public ITForm008 createITForm008(@Valid ITForm008 newITForm008, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform008")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm008, headers);
			ResponseEntity<ITForm008> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm008.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform008
	public ITForm008 updateITForm008(ITForm008 modifiedITForm008, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm008, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform008")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm008> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm008.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-009-------------------------------------------------------------------
	// GET - /itform009
	public ITForm009[] getITForm009s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform009");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm009[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm009[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm009 getITForm009(String language, Long classID, String matterNumber, String clientId, String itFormNo,
			Long itFormID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform009/id")
					.queryParam("matterNumber", matterNumber).queryParam("classID", classID)
					.queryParam("language", language).queryParam("clientId", clientId).queryParam("itFormNo", itFormNo)
					.queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm009> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm009.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform009
	public ITForm009 createITForm009(@Valid ITForm009 newITForm009, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform009")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm009, headers);
			ResponseEntity<ITForm009> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm009.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform009
	public ITForm009 updateITForm009(ITForm009 modifiedITForm009, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm009, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform009")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm009> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm009.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-010-------------------------------------------------------------------
	// GET - /itform010
	public ITForm010[] getITForm010s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform010");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm010[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm010[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm010 getITForm010(String language, Long classID, String matterNumber, String clientId, String itFormNo,
			Long itFormID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform010/id")
					.queryParam("matterNumber", matterNumber).queryParam("classID", classID)
					.queryParam("language", language).queryParam("clientId", clientId).queryParam("itFormNo", itFormNo)
					.queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm010> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm010.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform010
	public ITForm010 createITForm010(@Valid ITForm010 newITForm010, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform010")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm010, headers);
			ResponseEntity<ITForm010> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm010.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform010
	public ITForm010 updateITForm010(ITForm010 modifiedITForm010, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm010, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform010")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm010> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm010.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// -------------------------------------------------ITFORM-011-------------------------------------------------------------------
	// GET - /itform011
	public ITForm011[] getITForm011s(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform011");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm011[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
					entity, ITForm011[].class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - /id
	public ITForm011 getITForm011(String language, Long classID, String matterNumber, String clientId, String itFormNo,
			Long itFormID, String authToken) {
		try {

			log.info("language : " + language);
			log.info("classID : " + classID);
			log.info("matterNumber : " + matterNumber);
			log.info("clientId : " + clientId);
			log.info("itFormNo : " + itFormNo);
			log.info("itFormID : " + itFormID);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform011/id")
					.queryParam("matterNumber", matterNumber).queryParam("classID", classID)
					.queryParam("language", language).queryParam("clientId", clientId).queryParam("itFormNo", itFormNo)
					.queryParam("itFormID", itFormID);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ITForm011> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
					ITForm011.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - /itform011
	public ITForm011 createITForm011(@Valid ITForm011 newITForm011, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform011")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newITForm011, headers);
			ResponseEntity<ITForm011> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
					entity, ITForm011.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE - /itform011
	public ITForm011 updateITForm011(ITForm011 modifiedITForm011, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedITForm011, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCRMServiceUrl() + "/itform011")
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<ITForm011> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
					ITForm011.class);
			log.info("result : " + result.getBody());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
