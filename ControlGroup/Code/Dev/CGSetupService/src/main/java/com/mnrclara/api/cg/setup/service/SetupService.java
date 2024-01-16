package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

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

//	/**
//	 * getNextNumberRange
//	 * @param classID
//	 * @param numberRangeCode
//	 * @param authToken
//	 * @return
//	 */
//	public String getNextNumberRange (Long classID, Long numberRangeCode) {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "MNRClara's RestTemplate");
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			UriComponentsBuilder builder = UriComponentsBuilder
//					.fromHttpUrl(getSetupServiceApiUrl() + "/mnr-cg-setup-service/numberRange/numberRangeCode")
//					.queryParam("classID", classID)
//					.queryParam("numberRangeCode", numberRangeCode);
//			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
//					String.class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}


}
