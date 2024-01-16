package com.tekclover.wms.core.service;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.exception.BadRequestException;
import com.tekclover.wms.core.model.idmaster.*;
import com.tekclover.wms.core.model.threepl.*;
import com.tekclover.wms.core.model.user.AddUserManagement;
import com.tekclover.wms.core.model.user.UpdateUserManagement;
import com.tekclover.wms.core.model.user.UserManagement;
import com.tekclover.wms.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class IDMasterService {

	@Autowired
	PropertiesConfig propertiesConfig;


	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	private String getIDMasterServiceApiUrl () {
		return propertiesConfig.getIdmasterServiceUrl();
	}

	/*--------------------------------------------UserManagement--------------------------------------*/

	// GET - /login/validate
	public UserManagement validateUserID(String userID, String password, String authToken, String version) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "login")
					.queryParam("userID", userID)
					.queryParam("password", password)
					.queryParam("version", version);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserManagement> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserManagement.class);
			return result.getBody();
		} catch (Exception e) {
			throw new BadRequestException(e.getLocalizedMessage());
		}
	}

	// GET ALL
	public UserManagement[] getUserManagements(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usermanagement");
			ResponseEntity<UserManagement[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserManagement[].class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public UserManagement getUserManagement (String userId,String companyCode,String plantId,String languageId,Long userRoleId,String warehouseId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usermanagement/" + userId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCode",companyCode)
							.queryParam("plantId",plantId)
							.queryParam("languageId",languageId)
							.queryParam("userRoleId",userRoleId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserManagement> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserManagement.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public UserManagement createUserManagement (@Valid AddUserManagement newUserManagement, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usermanagement")
					.queryParam("loginUserID", loginUserID);
			HttpEntity<?> entity = new HttpEntity<>(newUserManagement, headers);
			ResponseEntity<UserManagement> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserManagement.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}

	// PATCH
	public UserManagement updateUserManagement (String userId, String warehouseId, String loginUserID,String companyCode,String plantId,String languageId,Long userRoleId,
												@Valid UpdateUserManagement updateUserManagement,
												String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateUserManagement, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usermanagement/" + userId)
					.queryParam("loginUserID", loginUserID)
					.queryParam("companyCode",companyCode)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("userRoleId",userRoleId)
					.queryParam("warehouseId", warehouseId);
			ResponseEntity<UserManagement> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserManagement.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			throw e;
		}
	}

	// DELETE
	public boolean deleteUserManagement (String userId, String warehouseId,String companyCode,String languageId,String plantId,Long userRoleId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usermanagement/" + userId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("companyCode",companyCode)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("userRoleId",userRoleId)
							.queryParam("warehouseId", warehouseId);
			ResponseEntity<String> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public UserManagement[] findUserManagement(FindUserManagement  findUserManagement, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"usermanagement/findUserManagement");
			HttpEntity<?>entity = new HttpEntity<>(findUserManagement,headers);
			ResponseEntity<UserManagement[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserManagement[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------CompanyMaster---------------------------------------------------------------------------------*/

	// GET
	public CompanyId getCompany(String companyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyId);
			ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public CompanyId[] getCompanies (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid");
			ResponseEntity<CompanyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId[].class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public CompanyId addCompany (CompanyId newCompany, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(newCompany, headers);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CompanyId.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public CompanyId updateCompany (String companyCodeId, UpdateCompanyId modifiedCompany, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedCompany, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyCodeId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<CompanyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CompanyId.class);
//			log.info("result : " + result.getStatusCode());
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
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/* ------------------------Currency-----------------------------------------------------------------------------------------*/

	// GET
	public Currency getCurrency(Long currencyId, String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "currency/" + currencyId)
					.queryParam("languageId",languageId);
			ResponseEntity<Currency> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Currency.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public Currency[] getCurrencies (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "currency");
			ResponseEntity<Currency[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Currency[].class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public Currency addCurrency (AddCurrency newCurrency, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(newCurrency, headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "currency")
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<Currency> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Currency.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public Currency updateCurrency (Long currencyId, String languageId, String loginUserID, UpdateCurrency modifiedCurrency, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedCurrency, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "currency/" + currencyId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID",loginUserID);

			ResponseEntity<Currency> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Currency.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteCurrency (Long currencyId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "currency/" + currencyId)
					.queryParam("languageId",languageId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public Currency[] findCurrency(FindCurrency findCurrency, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"currency/find");
			HttpEntity<?>entity = new HttpEntity<>(findCurrency,headers);
			ResponseEntity<Currency[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Currency[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* ------------------------CITY-----------------------------------------------------------------------------------------*/

	// GET
	public City getCity(String cityId,String stateId,String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "city/" + cityId)
					.queryParam("stateId",stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<City> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, City.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public City[] getCities (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "city");
			ResponseEntity<City[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, City[].class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public City addCity (AddCity newCity,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newCity, headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "city")
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<City> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, City.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public City updateCity (String cityId,String stateId,String countryId,String languageId,String loginUserID,
							UpdateCity modifiedCity, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedCity, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "city/" + cityId)
					.queryParam("stateId",stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID",loginUserID);

			ResponseEntity<City> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, City.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteCity (String cityId,String stateId,String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "city/" + cityId)
					.queryParam("stateId",stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
//			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public City[] findCity(FindCity findCity,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"city/find");
			HttpEntity<?>entity = new HttpEntity<>(findCity,headers);
			ResponseEntity<City[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, City[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* ------------------------Country-----------------------------------------------------------------------------------------*/

	// GET ALL
	public Country[] getCountries (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "country");
			ResponseEntity<Country[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Country[].class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public Country getCountry(String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "country/" + countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<Country> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Country.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public Country addCountry (AddCountry newCountry,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(newCountry, headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "country")
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<Country> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Country.class);
//			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public Country updateCountry (String countryId,String languageId,String loginUserID,UpdateCountry modifiedCountry,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedCountry, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "country/" + countryId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID",loginUserID);

			ResponseEntity<Country> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Country.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteCountry (String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "country/" + countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public Country[] findCountry(FindCountry findCountry,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"country/find");
			HttpEntity<?>entity = new HttpEntity<>(findCountry,headers);
			ResponseEntity<Country[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Country[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/* ------------------------State-----------------------------------------------------------------------------------------*/

	// GET
	public State getState(String stateId,String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "state/" + stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<State> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, State.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public State[] getStates (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "state");
			ResponseEntity<State[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, State[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public State addState (AddState newState,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(newState, headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "state")
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<State> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, State.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public State updateState (String stateId,String countryId,String languageId,String loginUserID,UpdateState modifiedState, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedState, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "state/" + stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<State> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, State.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteState (String stateId,String countryId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "state/" + stateId)
					.queryParam("countryId",countryId)
					.queryParam("languageId",languageId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public State[] findState(FindState findState,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"state/find");
			HttpEntity<?>entity = new HttpEntity<>(findState,headers);
			ResponseEntity<State[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, State[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/* ------------------------Vertical-----------------------------------------------------------------------------------------*/

	// GET
	public Vertical getVertical(String verticalId,String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "vertical/" + verticalId)
					.queryParam("languageId",languageId);
			ResponseEntity<Vertical> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Vertical.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET ALL
	public Vertical[] getVerticals (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "vertical");
			ResponseEntity<Vertical[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, Vertical[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// CREATE
	public Vertical addVertical (AddVertical newVertical,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newVertical, headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "vertical")
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<Vertical> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Vertical.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// UPDATE
	public Vertical updateVertical (String verticalId,String languageId,String loginUserID, UpdateVertical updateVertical, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateVertical, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "vertical/" + verticalId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID",loginUserID);
			ResponseEntity<Vertical> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, Vertical.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteVertical (String verticalId,String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Classic WMS's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "vertical/" + verticalId)
					.queryParam("languageId",languageId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public Vertical[] findVertical(FindVertical findVertical,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"vertical/find");
			HttpEntity<?>entity = new HttpEntity<>(findVertical,headers);
			ResponseEntity<Vertical[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, Vertical[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------BarcodeSubTypeId------------------------------------------------------------------------
	// GET ALL
	public BarcodeSubTypeId[] getBarcodeSubTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodesubtypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BarcodeSubTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BarcodeSubTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public BarcodeSubTypeId getBarcodeSubTypeId (String warehouseId, Long barcodeTypeId, Long barcodeSubTypeId,String companyCodeId,String languageId,String plantId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodesubtypeid/" + barcodeSubTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("barcodeTypeId", barcodeTypeId)
							.queryParam("barcodeSubTypeId", barcodeSubTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BarcodeSubTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BarcodeSubTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public BarcodeSubTypeId createBarcodeSubTypeId (AddBarcodeSubTypeId newBarcodeSubTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodesubtypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newBarcodeSubTypeId, headers);
		ResponseEntity<BarcodeSubTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BarcodeSubTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public BarcodeSubTypeId updateBarcodeSubTypeId (String warehouseId, Long barcodeTypeId, Long barcodeSubTypeId,String companyCodeId,String languageId,String plantId,
													String loginUserID, UpdateBarcodeSubTypeId modifiedBarcodeSubTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedBarcodeSubTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodesubtypeid/" + barcodeSubTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("barcodeTypeId", barcodeTypeId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<BarcodeSubTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BarcodeSubTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteBarcodeSubTypeId (String warehouseId,Long barcodeTypeId,Long barcodeSubTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodesubtypeid/" + barcodeSubTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("barcodeTypeId", barcodeTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public BarcodeSubTypeId[] findBarcodeSubTypeId(FindBarcodeSubtypeId findBarcodeSubTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"barcodesubtypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBarcodeSubTypeId,headers);
			ResponseEntity<BarcodeSubTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BarcodeSubTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------BarcodeTypeId------------------------------------------------------------------------
	// GET ALL
	public BarcodeTypeId[] getBarcodeTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodetypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BarcodeTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BarcodeTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public BarcodeTypeId getBarcodeTypeId (String warehouseId, Long barcodeTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodetypeid/" + barcodeTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BarcodeTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BarcodeTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public BarcodeTypeId createBarcodeTypeId (AddBarcodeTypeId newBarcodeTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodetypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newBarcodeTypeId, headers);
		ResponseEntity<BarcodeTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BarcodeTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	// PATCH
	public BarcodeTypeId updateBarcodeTypeId (String warehouseId, Long barcodeTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											  UpdateBarcodeTypeId modifiedBarcodeTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedBarcodeTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodetypeid/" + barcodeTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<BarcodeTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BarcodeTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteBarcodeTypeId (String warehouseId, Long barcodeTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "barcodetypeid/" + barcodeTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public BarcodeTypeId[] findBarcodeTypeId(FindBarcodeTypeId findBarcodeTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"barcodetypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBarcodeTypeId,headers);
			ResponseEntity<BarcodeTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BarcodeTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------CompanyId------------------------------------------------------------------------
	// GET ALL
	public CompanyId[] getCompanyIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CompanyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public CompanyId getCompanyId (String companyCodeId,String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyCodeId)
							.queryParam("languageId", languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public CompanyId createCompanyId (AddCompanyId newCompanyId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newCompanyId, headers);
		ResponseEntity<CompanyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CompanyId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	// PATCH
	public CompanyId updateCompanyId (String companyCodeId,String languageId, String loginUserID, UpdateCompanyId modifiedCompanyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedCompanyId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<CompanyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CompanyId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteCompanyId (String companyCodeId,String languageId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "companyid/" + companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public CompanyId[] findCompanyId(FindCompanyId findCompanyId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"companyid/find");
			HttpEntity<?>entity = new HttpEntity<>(findCompanyId,headers);
			ResponseEntity<CompanyId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CompanyId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------FloorId------------------------------------------------------------------------
	// GET ALL
	public FloorId[] getFloorIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "floorid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<FloorId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, FloorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public FloorId getFloorId (String warehouseId,Long floorId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "floorid/" + floorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<FloorId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, FloorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public FloorId createFloorId (AddFloorId newFloorId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "floorid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newFloorId, headers);
		ResponseEntity<FloorId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, FloorId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	// PATCH
	public FloorId updateFloorId (String warehouseId,Long floorId,String companyCodeId, String languageId,String plantId,String loginUserID, UpdateFloorId updateFloorId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateFloorId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "floorid/" + floorId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<FloorId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, FloorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteFloorId (String warehouseId,Long floorId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "floorid/" + floorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public FloorId[] findFloorId(FindFloorId findFloorId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"floorid/find");
			HttpEntity<?>entity = new HttpEntity<>(findFloorId,headers);
			ResponseEntity<FloorId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, FloorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------ItemGroupId------------------------------------------------------------------------
	// GET ALL
	public ItemGroupId[] getItemGroupIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemgroupid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ItemGroupId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ItemGroupId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ItemGroupId getItemGroupId (String warehouseId,Long itemTypeId,Long itemGroupId,String companyCodeId,String plantId,String languageId,
									   String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemgroupid/" + itemGroupId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("itemTypeId", itemTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ItemGroupId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ItemGroupId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ItemGroupId createItemGroupId (AddItemGroupId newItemGroupId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemgroupid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newItemGroupId, headers);
		ResponseEntity<ItemGroupId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ItemGroupId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public ItemGroupId updateItemGroupId (String warehouseId,Long itemTypeId,Long itemGroupId,String companyCodeId,String plantId,String languageId,
										  String loginUserID, UpdateItemGroupId modifiedItemGroupId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedItemGroupId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemgroupid/" + itemGroupId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("itemTypeId", itemTypeId)
					.queryParam("languageId",languageId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ItemGroupId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ItemGroupId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteItemGroupId (String warehouseId,Long itemTypeId,Long itemGroupId,String companyCodeId,String plantId,String languageId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemgroupid/" + itemGroupId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("itemTypeId", itemTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ItemGroupId[] findItemGroupId(FindItemGroupId findItemGroupId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"itemgroupid/find");
			HttpEntity<?>entity = new HttpEntity<>(findItemGroupId,headers);
			ResponseEntity<ItemGroupId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ItemGroupId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------ItemTypeId------------------------------------------------------------------------
	// GET ALL
	public ItemTypeId[] getItemTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemtypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ItemTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ItemTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ItemTypeId getItemTypeId (String warehouseId,Long itemTypeId,String companyCodeId,String plantId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemtypeid/" + itemTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ItemTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ItemTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ItemTypeId createItemTypeId (AddItemTypeId newItemTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemtypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newItemTypeId, headers);
		ResponseEntity<ItemTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ItemTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public ItemTypeId updateItemTypeId (String warehouseId,Long itemTypeId,String companyCodeId,String plantId,String languageId,
										String loginUserID, UpdateItemTypeId modifiedItemTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedItemTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemtypeid/" + itemTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ItemTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ItemTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteItemTypeId (String warehouseId,Long itemTypeId,String companyCodeId,String plantId,String languageId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "itemtypeid/" + itemTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ItemTypeId[] findItemTypeId(FindItemTypeId findItemTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"itemtypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findItemTypeId,headers);
			ResponseEntity<ItemTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ItemTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------LevelId------------------------------------------------------------------------
	// GET ALL
	public LevelId[] getLevelIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "levelid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LevelId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public LevelId getLevelId (String warehouseId,Long levelId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "levelid/" + levelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LevelId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public LevelId createLevelId (AddLevelId newLevelId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "levelid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newLevelId, headers);
		ResponseEntity<LevelId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LevelId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public LevelId updateLevelId (String warehouseId, Long levelId, String companyCodeId,String languageId,String plantId,
								  String loginUserID, UpdateLevelId modifiedLevelId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedLevelId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "levelid/" + levelId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<LevelId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, LevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteLevelId (String warehouseId,Long levelId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "levelid/" + levelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public LevelId[] findLevelId(FindLevelId findLevelId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"levelid/find");
			HttpEntity<?>entity = new HttpEntity<>(findLevelId,headers);
			ResponseEntity<LevelId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------MenuId------------------------------------------------------------------------
	// GET ALL
	public MenuId[] getMenuIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MenuId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MenuId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public MenuId getMenuId (String warehouseId, Long menuId, Long subMenuId, Long authorizationObjectId,
							 String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid/" + menuId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("subMenuId", subMenuId)
							.queryParam("authorizationObjectId", authorizationObjectId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MenuId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MenuId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public MenuId createMenuId (AddMenuId newMenuId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newMenuId, headers);
		ResponseEntity<MenuId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MenuId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// POST - Bulk create
	public MenuId[] createMenuIdBulk (List<AddMenuId> newMenuId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid/bulk")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newMenuId, headers);
		ResponseEntity<MenuId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MenuId[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public MenuId updateMenuId (String warehouseId, Long menuId, Long subMenuId, Long authorizationObjectId,
								String companyCodeId,String languageId,String plantId,String loginUserID, UpdateMenuId modifiedMenuId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedMenuId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid/" + menuId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("subMenuId", subMenuId)
					.queryParam("authorizationObjectId", authorizationObjectId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<MenuId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MenuId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteMenuId (String warehouseId, Long menuId, Long subMenuId, Long authorizationObjectId,
								 String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "menuid/" + menuId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("subMenuId", subMenuId)
							.queryParam("authorizationObjectId", authorizationObjectId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public MenuId[] findMenuId(FindMenuId findMenuId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"menuid/find");
			HttpEntity<?>entity = new HttpEntity<>(findMenuId,headers);
			ResponseEntity<MenuId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MenuId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------PlantId------------------------------------------------------------------------
	// GET ALL
	public PlantId[] getPlantIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "plantid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PlantId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PlantId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public PlantId getPlantId (String plantId,String companyCodeId,String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "plantid/" + plantId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PlantId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PlantId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public PlantId createPlantId (AddPlantId newPlantId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "plantid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newPlantId, headers);
		ResponseEntity<PlantId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PlantId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public PlantId updatePlantId (String plantId,String companyCodeId,String languageId,String loginUserID, UpdatePlantId updatePlantId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updatePlantId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "plantid/" + plantId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<PlantId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PlantId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deletePlantId (String plantId,String companyCodeId,String languageId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "plantid/" + plantId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public PlantId[] findPlantId(FindPlantId findPlantId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"plantid/find");
			HttpEntity<?>entity = new HttpEntity<>(findPlantId,headers);
			ResponseEntity<PlantId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PlantId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------ProcessSequenceId------------------------------------------------------------------------
	// GET ALL
	public ProcessSequenceId[] getProcessSequenceIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processsequenceid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ProcessSequenceId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProcessSequenceId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ProcessSequenceId getProcessSequenceId (String warehouseId,String processId,Long processSequenceId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processsequenceid/" + processSequenceId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("processId", processId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ProcessSequenceId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProcessSequenceId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public ProcessSequenceId createProcessSequenceId (AddProcessSequenceId newProcessSequenceId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processsequenceid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newProcessSequenceId, headers);
		ResponseEntity<ProcessSequenceId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProcessSequenceId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public ProcessSequenceId updateProcessSequenceId (String warehouseId,String processId,Long processSequenceId,String companyCodeId,String languageId,String plantId,
													  String loginUserID, UpdateProcessSequenceId modifiedProcessSequenceId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedProcessSequenceId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processsequenceid/" + processSequenceId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("processId", processId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ProcessSequenceId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ProcessSequenceId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteProcessSequenceId (String warehouseId,String processId,Long processSequenceId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processsequenceid/" + processSequenceId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("processId", processId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ProcessSequenceId[] findProcessSequenceId(FindProcessSequenceId findProcessSequenceId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"processsequenceid/find");
			HttpEntity<?>entity = new HttpEntity<>(findProcessSequenceId,headers);
			ResponseEntity<ProcessSequenceId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProcessSequenceId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	///--------------------------------------------RowId------------------------------------------------------------------------
	// GET ALL
	public RowId[] getRowIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "rowid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RowId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RowId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public RowId getRowId (String warehouseId,Long floorId,String storageSectionId,String rowId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "rowid/" + rowId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId", floorId)
							.queryParam("storageSectionId", storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RowId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RowId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public RowId createRowId (AddRowId newRowId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "rowid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newRowId, headers);
		ResponseEntity<RowId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RowId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public RowId updateRowId (String warehouseId,Long floorId,String storageSectionId,String rowId,String companyCodeId,
							  String languageId,String plantId,String loginUserID, UpdateRowId updateRowId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateRowId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "rowid/" + rowId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("floorId", floorId)
					.queryParam("storageSectionId", storageSectionId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);
			ResponseEntity<RowId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, RowId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteRowId (String warehouseId,Long floorId,String storageSectionId,String rowId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "rowid/" + rowId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId", floorId)
							.queryParam("storageSectionId", storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public RowId[] findRowId(FindRowId findRowId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"rowid/find");
			HttpEntity<?>entity = new HttpEntity<>(findRowId,headers);
			ResponseEntity<RowId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RowId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------StatusId------------------------------------------------------------------------
	// GET ALL
	public StatusId[] getStatusIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StatusId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StatusId getStatusId (Long statusId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusid/" + statusId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StatusId createStatusId (AddStatusId newStatusId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStatusId, headers);
		ResponseEntity<StatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StatusId updateStatusId (Long statusId,String languageId,String loginUserID,UpdateStatusId modifiedStatusId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedStatusId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusid/" + statusId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StatusId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStatusId (Long statusId,String languageId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusid/" + statusId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StatusId[] findStatusId(FindStatusId findStatusId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"statusid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStatusId,headers);
			ResponseEntity<StatusId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------StorageBinTypeId------------------------------------------------------------------------
	// GET ALL
	public StorageBinTypeId[] getStorageBinTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagebintypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageBinTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageBinTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StorageBinTypeId getStorageBinTypeId (String warehouseId, Long storageClassId, Long storageTypeId,
												 Long storageBinTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagebintypeid/" + storageBinTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("storageClassId", storageClassId)
							.queryParam("storageTypeId", storageTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageBinTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageBinTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StorageBinTypeId createStorageBinTypeId (AddStorageBinTypeId newStorageBinTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagebintypeid")
						.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStorageBinTypeId, headers);
		ResponseEntity<StorageBinTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageBinTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StorageBinTypeId updateStorageBinTypeId (String warehouseId, Long storageClassId, Long storageTypeId,
													Long storageBinTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, UpdateStorageBinTypeId updateStorageBinTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateStorageBinTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagebintypeid/" + storageBinTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("storageClassId", storageClassId)
					.queryParam("storageTypeId", storageTypeId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StorageBinTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StorageBinTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStorageBinTypeId (String warehouseId, Long storageClassId, Long storageTypeId,
										   Long storageBinTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagebintypeid/" + storageBinTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("storageClassId", storageClassId)
							.queryParam("storageTypeId", storageTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StorageBinTypeId[] findStorageBinType(FindStorageBinTypeId findStorageBinTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"storagebintypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStorageBinTypeId,headers);
			ResponseEntity<StorageBinTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageBinTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



	//--------------------------------------------StorageClassId------------------------------------------------------------------------
	// GET ALL
	public StorageClassId[] getStorageClassIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storageclassid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageClassId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageClassId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StorageClassId getStorageClassId (String warehouseId, Long storageClassId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storageclassid/" + storageClassId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageClassId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageClassId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StorageClassId createStorageClassId (AddStorageClassId newStorageClassId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storageclassid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStorageClassId, headers);
		ResponseEntity<StorageClassId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageClassId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StorageClassId updateStorageClassId (String warehouseId, Long storageClassId,String companyCodeId,String languageId,String plantId,String loginUserID,
												UpdateStorageClassId updateStorageClassId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateStorageClassId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storageclassid/" + storageClassId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StorageClassId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StorageClassId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStorageClassId (String warehouseId, Long storageClassId,String companyCodeId,String languageId,String plantId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storageclassid/" + storageClassId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId )
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StorageClassId[] findStorageClassId(FindStorageClassId findStorageClassId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"storageclassid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStorageClassId,headers);
			ResponseEntity<StorageClassId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageClassId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------StorageSectionId------------------------------------------------------------------------
	// GET ALL
	public StorageSectionId[] getStorageSectionIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagesectionid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageSectionId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageSectionId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StorageSectionId getStorageSectionId (String warehouseId, Long floorId, String storageSectionId,
												 String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagesectionid/" + storageSectionId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId", floorId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageSectionId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageSectionId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StorageSectionId createStorageSectionId (AddStorageSectionId newStorageSectionId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagesectionid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStorageSectionId, headers);
		ResponseEntity<StorageSectionId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageSectionId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StorageSectionId updateStorageSectionId (String warehouseId, Long floorId, String storageSectionId,
													String companyCodeId,String languageId,String plantId,String loginUserID, UpdateStorageSectionId updateStorageSectionId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateStorageSectionId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagesectionid/" + storageSectionId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("floorId", floorId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StorageSectionId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StorageSectionId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStorageSectionId (String warehouseId, Long floorId, String storageSectionId,
										   String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagesectionid/" + storageSectionId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId", floorId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StorageSectionId[] findStorageSectionId(FindStorageSectionId findStorageSectionId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"storagesectionid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStorageSectionId,headers);
			ResponseEntity<StorageSectionId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageSectionId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



	//--------------------------------------------StrategyId------------------------------------------------------------------------
	// GET ALL
	public StrategyId[] getStrategyIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "strategyid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StrategyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StrategyId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StrategyId getStrategyId (String warehouseId, Long strategyTypeId, String strategyNo,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "strategyid/" + strategyNo)
							.queryParam("warehouseId", warehouseId)
							.queryParam("strategyTypeId", strategyTypeId)
							.queryParam("companyCodeId", companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StrategyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StrategyId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StrategyId createStrategyId (AddStrategyId newStrategyId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "strategyid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStrategyId, headers);
		ResponseEntity<StrategyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StrategyId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StrategyId updateStrategyId (String warehouseId, Long strategyTypeId, String strategyNo, String companyCodeId,String languageId,String plantId,
										String loginUserID, UpdateStrategyId modifiedStrategyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedStrategyId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "strategyid/" + strategyNo)
					.queryParam("warehouseId", warehouseId)
					.queryParam("strategyTypeId", strategyTypeId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StrategyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StrategyId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStrategyId (String warehouseId, Long strategyTypeId, String strategyNo,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "strategyid/" + strategyNo)
							.queryParam("warehouseId", warehouseId)
							.queryParam("strategyTypeId", strategyTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StrategyId[] findStrategyId(FindStrategyId findStrategyId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"strategyid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStrategyId,headers);
			ResponseEntity<StrategyId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StrategyId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------StroageTypeId------------------------------------------------------------------------
	// GET ALL
	public StorageTypeId[] getStroageTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagetypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public StorageTypeId getStorageTypeId (String warehouseId, Long storageClassId, Long storageTypeId,String companyCodeId,String languageId,String plantId,
										   String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagetypeid/" + storageTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("storageClassId", storageClassId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StorageTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StorageTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public StorageTypeId createStorageTypeId (AddStorageTypeId newStorageTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagetypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStorageTypeId, headers);
		ResponseEntity<StorageTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public StorageTypeId updateStorageTypeId ( Long storageTypeId,String warehouseId, Long storageClassId,String companyCodeId,String languageId,String plantId,
											   String loginUserID, UpdateStorageTypeId updateStorageTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateStorageTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagetypeid/" + storageTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("storageClassId", storageClassId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<StorageTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StorageTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteStorageTypeId (Long storageTypeId,String warehouseId, Long storageClassId, String companyCodeId,
										String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "storagetypeid/" + storageTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("storageClassId", storageClassId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StorageTypeId[] findStorageTypeId(FindStorageTypeId findStorageTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"storagetypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStorageTypeId,headers);
			ResponseEntity<StorageTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------SubItemGroupId------------------------------------------------------------------------
	// GET ALL
	public SubItemGroupId[] getSubItemGroupIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "subitemgroupid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubItemGroupId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubItemGroupId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public SubItemGroupId getSubItemGroupId (String warehouseId, Long itemTypeId, Long itemGroupId,Long subItemGroupId,
											 String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "subitemgroupid/" + subItemGroupId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("itemTypeId", itemTypeId)
							.queryParam("itemGroupId", itemGroupId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubItemGroupId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubItemGroupId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public SubItemGroupId createSubItemGroupId (AddSubItemGroupId newSubItemGroupId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "subitemgroupid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newSubItemGroupId, headers);
		ResponseEntity<SubItemGroupId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubItemGroupId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public SubItemGroupId updateSubItemGroupId (String warehouseId, Long itemTypeId, Long itemGroupId,Long subItemGroupId,
												String companyCodeId,String languageId,String plantId,String loginUserID,
												UpdateSubItemGroupId modifiedSubItemGroupId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedSubItemGroupId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "subitemgroupid/" + subItemGroupId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("itemTypeId", itemTypeId)
					.queryParam("itemGroupId", itemGroupId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<SubItemGroupId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SubItemGroupId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteSubItemGroupId (String warehouseId, Long itemTypeId, Long itemGroupId, Long subItemGroupId,
										 String companyCodeId,String languageId,String plantId,
										 String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "subitemgroupid/" + subItemGroupId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("itemTypeId", itemTypeId)
							.queryParam("itemGroupId", itemGroupId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public SubItemGroupId[] findSubItemGroupId(FindSubItemGroupId findSubItemGroupId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"subitemgroupid/find");
			HttpEntity<?>entity = new HttpEntity<>(findSubItemGroupId,headers);
			ResponseEntity<SubItemGroupId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubItemGroupId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------UomId------------------------------------------------------------------------
	// GET ALL
	public UomId[] getUomIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "uomid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UomId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UomId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public UomId getUomId (String uomId,String companyCodeId,String languageId,String warehouseId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "uomid/" + uomId)
							.queryParam("companyCodeId", companyCodeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("plantId",plantId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UomId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UomId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public UomId createUomId (AddUomId newUomId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "uomid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newUomId, headers);
		ResponseEntity<UomId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UomId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public UomId updateUomId (String uomId,String companyCodeId,String languageId,String warehouseId,String plantId,String loginUserID, UpdateUomId modifiedUomId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedUomId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "uomid/" + uomId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<UomId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UomId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteUomId (String uomId,String companyCodeId,String languageId,String warehouseId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "uomid/" + uomId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public UomId[] findUomId(FindUomId findUomId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"uomid/find");
			HttpEntity<?>entity = new HttpEntity<>(findUomId,headers);
			ResponseEntity<UomId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UomId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------UserTypeId------------------------------------------------------------------------
	// GET ALL
	public UserTypeId[] getUserTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usertypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public UserTypeId getUserTypeId (String warehouseId,Long userTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usertypeid/" + userTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<UserTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, UserTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public UserTypeId createUserTypeId (AddUserTypeId newUserTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usertypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newUserTypeId, headers);
		ResponseEntity<UserTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public UserTypeId updateUserTypeId (String warehouseId, Long userTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
										UpdateUserTypeId modifiedUserTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedUserTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usertypeid/" + userTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<UserTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, UserTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteUserTypeId (String warehouseId, Long userTypeId,String companyCodeId,String languageId,
									 String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "usertypeid/" + userTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public UserTypeId[] findUserTypeId(FindUserTypeId findUserTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"usertypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findUserTypeId,headers);
			ResponseEntity<UserTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, UserTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------VariantId------------------------------------------------------------------------
	// GET ALL
	public VariantId[] getVariantIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "variantid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<VariantId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, VariantId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public VariantId getVariantId (String warehouseId, String variantCode, String variantType, String variantSubCode,String companyCodeId,String plantId,String languageId,
								   String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "variantid/" + variantCode)
							.queryParam("warehouseId", warehouseId)
							.queryParam("variantType", variantType)
							.queryParam("languageId",languageId)
							.queryParam("variantSubCode", variantSubCode)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<VariantId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, VariantId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public VariantId createVariantId (AddVariantId newVariantId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "variantid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newVariantId, headers);
		ResponseEntity<VariantId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, VariantId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public VariantId updateVariantId (String warehouseId, String variantCode, String variantType,String variantSubCode, String companyCodeId,String plantId,String languageId,
									  String loginUserID, UpdateVariantId modifiedVariantId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedVariantId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "variantid/" + variantCode)
					.queryParam("warehouseId", warehouseId)
					.queryParam("variantType", variantType)
					.queryParam("variantSubCode", variantSubCode)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<VariantId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, VariantId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteVariantId (String warehouseId, String variantCode, String variantType, String variantSubCode,String companyCodeId,String plantId,String languageId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "variantid/" + variantCode)
							.queryParam("warehouseId", warehouseId)
							.queryParam("variantType", variantType)
							.queryParam("variantSubCode", variantSubCode)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public VariantId[] findVariantId(FindVariantId findVariantId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"variantid/find");
			HttpEntity<?>entity = new HttpEntity<>(findVariantId,headers);
			ResponseEntity<VariantId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, VariantId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------WarehouseId------------------------------------------------------------------------
	// GET ALL
	public WarehouseId[] getWarehouseIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehouseid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WarehouseId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WarehouseId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public WarehouseId getWarehouseId (String warehouseId,String plantId,String companyCodeId,String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehouseid/" + warehouseId)
							.queryParam("plantId",plantId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WarehouseId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WarehouseId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public WarehouseId createWarehouseId (AddWarehouseId newWarehouseId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehouseid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newWarehouseId, headers);
		ResponseEntity<WarehouseId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public WarehouseId updateWarehouseId (String warehouseId,String plantId,String companyCodeId,String languageId, String loginUserID, UpdateWarehouseId updateWarehouseId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateWarehouseId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehouseid/" + warehouseId)
					.queryParam("plantId",plantId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<WarehouseId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, WarehouseId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteWarehouseId (String warehouseId,String plantId,String companyCodeId,String languageId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehouseid/" + warehouseId)
							.queryParam("plantId",plantId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public WarehouseId[] findWarehouseId(FindWarehouseId findWarehouseId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"warehouseid/find");
			HttpEntity<?>entity = new HttpEntity<>(findWarehouseId,headers);
			ResponseEntity<WarehouseId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------WarehouseTypeId------------------------------------------------------------------------
	// GET ALL
	public WarehouseTypeId[] getWarehouseTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehousetypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WarehouseTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WarehouseTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public WarehouseTypeId getWarehouseTypeId (String warehouseId,Long warehouseTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehousetypeid/" + warehouseTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WarehouseTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WarehouseTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public WarehouseTypeId createWarehouseTypeId (AddWarehouseTypeId newWarehouseTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehousetypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newWarehouseTypeId, headers);
		ResponseEntity<WarehouseTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseTypeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public WarehouseTypeId updateWarehouseTypeId (String warehouseId, Long warehouseTypeId,String companyCodeId,String languageId,String plantId,
												  String loginUserID, UpdateWarehouseTypeId modifiedWarehouseTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedWarehouseTypeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehousetypeid/" + warehouseTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<WarehouseTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, WarehouseTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteWarehouseTypeId (String warehouseId,Long warehouseTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "warehousetypeid/" + warehouseTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public WarehouseTypeId[] findWarehouseTypeId(FindWarehouseTypeId findWarehouseTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"warehousetypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findWarehouseTypeId,headers);
			ResponseEntity<WarehouseTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------HhtUser------------------------------------------------------------------------
	// GET ALL
	public HhtUserOutput[] getHhtUsers (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HhtUserOutput[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HhtUserOutput[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public HhtUserOutput getHhtUser (String userId, String warehouseId,String companyCodeId,Long levelId,
									 String plantId,String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser/" + userId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId)
							.queryParam("levelId",levelId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HhtUserOutput> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HhtUserOutput.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public HhtUserOutput[] getHhtUserByWarehouseId (String warehouseId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser/" + warehouseId + "/hhtUser")
							.queryParam("warehouseId", warehouseId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HhtUserOutput[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HhtUserOutput[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public HhtUser createHhtUser (AddHhtUser newHhtUser, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newHhtUser, headers);
		ResponseEntity<HhtUser> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HhtUser.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public HhtUser updateHhtUser (String userId, String warehouseId,String companyCodeId,String languageId,
								  String plantId,UpdateHhtUser modifiedHhtUser, String loginUserID,
								  Long levelId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedHhtUser, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser/" + userId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("levelId",levelId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<HhtUser> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, HhtUser.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteHhtUser ( String warehouseId, String userId,String companyCodeId,String languageId,
								   String plantId, String loginUserID,Long levelId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "hhtuser/" + userId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId)
							.queryParam("levelId",levelId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//SEARCH
	public HhtUserOutput[] findHhtUser(FindHhtUser findHhtUser,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"hhtuser/findHhtUser");
			HttpEntity<?>entity = new HttpEntity<>(findHhtUser,headers);
			ResponseEntity<HhtUserOutput[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HhtUserOutput[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------RoleAccess------------------------------------------------------------------------
	// GET ALL
	public RoleAccess[] getRoleAccesss (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "roleaccess");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RoleAccess[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RoleAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public RoleAccess[] getRoleAccess (String warehouseId, Long roleId,
									   String companyCodeId, String plantId,
									   String languageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "roleaccess/" + roleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RoleAccess[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RoleAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public RoleAccess[] createRoleAccess (List<AddRoleAccess> newRoleAccess, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "roleaccess")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newRoleAccess, headers);
		ResponseEntity<RoleAccess[]> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RoleAccess[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public RoleAccess[] updateRoleAccess (String warehouseId, Long roleId,String companyCodeId,
										  String languageId,String plantId,String loginUserID,
										  List<AddRoleAccess> modifiedRoleAccess, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedRoleAccess, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "roleaccess/" + roleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId)
							.queryParam("languageId",languageId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<RoleAccess[]> result =
					restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, RoleAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteRoleAccess (String warehouseId, Long roleId,
									 String companyCodeId, String languageId,
									 String plantId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "roleaccess/" + roleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
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
	//SEARCH
	public RoleAccess[] findRoleAccess(FindRoleId findRoleId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"roleaccess/find");
			HttpEntity<?>entity = new HttpEntity<>(findRoleId,headers);
			ResponseEntity<RoleAccess[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RoleAccess[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------DoorId------------------------------------------------------------------------
	// GET ALL
	public DoorId[] getDoorIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "doorid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DoorId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DoorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public DoorId getDoorId (String warehouseId, String doorId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "doorid/" + doorId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DoorId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DoorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public DoorId createDoorId (AddDoorId newDoorId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "doorid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newDoorId, headers);
		ResponseEntity<DoorId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DoorId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public DoorId updateDoorId (String warehouseId, String doorId,String companyCodeId,String languageId,String plantId,
								String loginUserID, UpdateDoorId updateDoorId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateDoorId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "doorid/" + doorId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<DoorId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DoorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteDoorId (String warehouseId, String doorId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "doorid/" + doorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public DoorId[] findDoorId(FindDoorId findDoorId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"doorid/find");
			HttpEntity<?>entity = new HttpEntity<>(findDoorId,headers);
			ResponseEntity<DoorId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DoorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------ModuleId------------------------------------------------------------------------
	// GET ALL
	public ModuleId[] getModuleIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ModuleId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public ModuleId[] getModuleId (String warehouseId, String moduleId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid/" + moduleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ModuleId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	/*public ModuleId createModuleId (AddModuleId newModuleId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newModuleId, headers);
		ResponseEntity<ModuleId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ModuleId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}*/
	public ModuleId[] createModuleId (List<AddModuleId> newModuleId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newModuleId, headers);
		ResponseEntity<ModuleId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ModuleId[].class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	/*public ModuleId updateModuleId (String warehouseId, String moduleId,String companyCodeId,String languageId,String plantId,
									String loginUserID, UpdateModuleId modifiedModuleId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedModuleId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid/" + moduleId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ModuleId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ModuleId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}*/
	public ModuleId[] updateModuleId (String warehouseId, String moduleId,String companyCodeId,String languageId,String plantId,
									  String loginUserID, List<UpdateModuleId> modifiedModuleId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedModuleId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid/" + moduleId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<ModuleId[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteModuleId (String warehouseId, String moduleId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "moduleid/" + moduleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ModuleId[] findModuleId(FindModuleId findModuleId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"moduleid/find");
			HttpEntity<?>entity = new HttpEntity<>(findModuleId,headers);
			ResponseEntity<ModuleId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------AdhocModuleId------------------------------------------------------------------------
	// GET ALL
	public AdhocModuleId[] getAdhocModuleIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "adhocmoduleid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AdhocModuleId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AdhocModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public AdhocModuleId getAdhocModuleId (String warehouseId, String adhocModuleId,String moduleId,String companyCodeId,String languageId,String plantId,
										   String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "adhocmoduleid/" + adhocModuleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("moduleId", moduleId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AdhocModuleId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AdhocModuleId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public AdhocModuleId createAdhocModuleId (AddAdhocModuleId newAdhocModuleId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "adhocmoduleid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newAdhocModuleId, headers);
		ResponseEntity<AdhocModuleId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AdhocModuleId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public AdhocModuleId updateAdhocModuleId (String warehouseId, String adhocModuleId,String moduleId,String companyCodeId,String languageId,String plantId,
											  String loginUserID, UpdateAdhocModuleId modifiedAdhocModuleId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedAdhocModuleId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "adhocmoduleid/" + adhocModuleId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("moduleId", moduleId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<AdhocModuleId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, AdhocModuleId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteAdhocModuleId (String warehouseId, String adhocModuleId, String moduleId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "adhocmoduleid/" + adhocModuleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("moduleId", moduleId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public AdhocModuleId[] findAdhocModuleId(FindAdhocModuleId findAdhocModuleId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"adhocmoduleid/find");
			HttpEntity<?>entity = new HttpEntity<>(findAdhocModuleId,headers);
			ResponseEntity<AdhocModuleId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AdhocModuleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------PalletizationLevelId------------------------------------------------------------------------
	// GET ALL
	public PalletizationLevelId[] getPalletizationLevelIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "palletizationlevelid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PalletizationLevelId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PalletizationLevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public PalletizationLevelId getPalletizationLevelId (String warehouseId,String palletizationLevelId, String palletizationLevel,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "palletizationlevelid/" + palletizationLevelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("palletizationLevel", palletizationLevel)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<PalletizationLevelId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PalletizationLevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public PalletizationLevelId createPalletizationLevelId (AddPalletizationLevelId newPalletizationLevelId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "palletizationlevelid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newPalletizationLevelId, headers);
		ResponseEntity<PalletizationLevelId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PalletizationLevelId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public PalletizationLevelId updatePalletizationLevelId (String warehouseId, String palletizationLevelId,String palletizationLevel,String companyCodeId,String languageId,String plantId,
															String loginUserID, UpdatePalletizationLevelId modifiedPalletizationLevelId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedPalletizationLevelId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "palletizationlevelid/" + palletizationLevelId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("palletizationLevel", palletizationLevel)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<PalletizationLevelId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PalletizationLevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deletePalletizationLevelId (String warehouseId, String palletizationLevelId,String palletizationLevel,String companyCodeId,
											   String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "palletizationlevelid/" + palletizationLevelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("palletizationLevel", palletizationLevel)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public PalletizationLevelId[] findPalletizationLevelId(FindPalletizationLevelId findPalletizationLevelId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"palletizationlevelid/find");
			HttpEntity<?>entity = new HttpEntity<>(findPalletizationLevelId,headers);
			ResponseEntity<PalletizationLevelId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PalletizationLevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------EmployeeId------------------------------------------------------------------------
	// GET ALL
	public EmployeeId[] getEmployeeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "employeeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EmployeeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, EmployeeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET
	public EmployeeId getEmployeeId (String warehouseId, String employeeId,String companyCodeId,String languageId,String plantId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "employeeid/" + employeeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("employeeId", employeeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<EmployeeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, EmployeeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST
	public EmployeeId createEmployeeId (AddEmployeeId newEmployeeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", " RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "employeeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newEmployeeId, headers);
		ResponseEntity<EmployeeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, EmployeeId.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// PATCH
	public EmployeeId updateEmployeeId (String warehouseId, String employeeId,String companyCodeId,String languageId,String plantId,
										String loginUserID, UpdateEmployeeId modifiedEmployeeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", " RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedEmployeeId, headers);

			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "employeeid/" + employeeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId)
					.queryParam("loginUserID", loginUserID);

			ResponseEntity<EmployeeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, EmployeeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// DELETE
	public boolean deleteEmployeeId (String warehouseId, String employeeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "employeeid/" + employeeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public EmployeeId[] findEmployeeId(FindEmployeeId findEmployeeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"employeeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findEmployeeId,headers);
			ResponseEntity<EmployeeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, EmployeeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------BinClassId------------------------------------------------------------------------
	//GET ALL
	public BinClassId[] getBinClassIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binclassid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BinClassId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BinClassId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public BinClassId getBinClassId(String warehouseId,Long binClassId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binclassid/" + binClassId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BinClassId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BinClassId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public BinClassId createBinClassId(AddBinClassId newBinClassId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binclassid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newBinClassId, headers);
		ResponseEntity<BinClassId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BinClassId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public BinClassId updateBinclassId(String warehouseId,Long binClassId,String companyCodeId,String languageId,String plantId,String loginUserID,
									   UpdateBinClassId modifiedBinClassId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedBinClassId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binclassid/" + binClassId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<BinClassId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BinClassId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteBinClassId(String warehouseId,Long binClassId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binclassid/" + binClassId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public BinClassId[] findBinClassId(FindBinClassId findBinClassId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"binclassid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBinClassId,headers);
			ResponseEntity<BinClassId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BinClassId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------DockId ------------------------------------------------------------------------
	//GET ALL
	public DockId[] getDockIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dockid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DockId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DockId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public DockId getDockId(String warehouseId, String dockId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dockid/" + dockId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DockId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DockId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public DockId createDockId(AddDockId newDockId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dockid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newDockId, headers);
		ResponseEntity<DockId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DockId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public DockId updateDockId(String warehouseId,String dockId,String companyCodeId,String languageId,String plantId,String loginUserID,
							   UpdateDockId updateDockId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateDockId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dockid/" + dockId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<DockId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DockId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteDockId(String warehouseId, String dockId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dockid/" + dockId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public DockId[] findDockId(FindDockId findDockId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"dockid/find");
			HttpEntity<?>entity = new HttpEntity<>(findDockId,headers);
			ResponseEntity<DockId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DockId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------WorkCenterId ------------------------------------------------------------------------
	//GET ALL
	public WorkCenterId[] getworkCenterIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "workcenterid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WorkCenterId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WorkCenterId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public WorkCenterId getworkCenterId( String warehouseId,String workCenterId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "workcenterid/" + workCenterId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<WorkCenterId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, WorkCenterId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public WorkCenterId createWorkCenterId(AddWorkCenterId newWorkCenterId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "workcenterid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newWorkCenterId, headers);
		ResponseEntity<WorkCenterId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WorkCenterId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public WorkCenterId updateWorkCenterId(String warehouseId,String workCenterId,String companyCodeId,String languageId,String plantId, String loginUserID,
										   UpdateWorkCenterId updateWorkCenterId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateWorkCenterId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "workcenterid/" + workCenterId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<WorkCenterId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, WorkCenterId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteWorkCenterId(String warehouseId, String workCenterId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "workcenterid/" + workCenterId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public WorkCenterId[] findWorkCenterId(FindWorkCenterId findWorkCenterId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"workcenterid/find");
			HttpEntity<?>entity = new HttpEntity<>(findWorkCenterId,headers);
			ResponseEntity<WorkCenterId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WorkCenterId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------outboundOrderStatusId------------------------------------------------------------------------
	//GET ALL
	public OutboundOrderStatusId[] getoutBoundOrderStatusIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundorderstatusid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<OutboundOrderStatusId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OutboundOrderStatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public OutboundOrderStatusId getoutBoundOrderStatusId(String warehouseId, String outboundOrderStatusId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundorderstatusid/" + outboundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<OutboundOrderStatusId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OutboundOrderStatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public OutboundOrderStatusId createoutBoundOrderStatusId(AddOutboundOrderStatusId newOutboundOrderStatusId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundorderstatusid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newOutboundOrderStatusId, headers);
		ResponseEntity<OutboundOrderStatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundOrderStatusId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public OutboundOrderStatusId updateOutboundOrderStatusId(String warehouseId, String outboundOrderStatusId,String companyCodeId,String languageId,String plantId, String loginUserID,
															 UpdateOutboundOrderStatusId updateOutboundOrderStatusId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateOutboundOrderStatusId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundorderstatusid/" + outboundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<OutboundOrderStatusId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, OutboundOrderStatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteoutBoundOrderStatusId(String warehouseId, String outBoundOrderStatusId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundorderstatusid/" + outBoundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public OutboundOrderStatusId[] findOutboundOrderStatusId(FindOutboundOrderStatusId findOutboundOrderStatusId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"outboundorderstatusid/find");
			HttpEntity<?>entity = new HttpEntity<>(findOutboundOrderStatusId,headers);
			ResponseEntity<OutboundOrderStatusId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundOrderStatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------InboundOrderStatusId ------------------------------------------------------------------------
	//GET ALL
	public InboundOrderStatusId[] getInboundOrderStatusIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundorderstatusid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InboundOrderStatusId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InboundOrderStatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public InboundOrderStatusId getInboundOrderStatusID(String warehouseId,String inboundOrderStatusId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundorderstatusid/" + inboundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InboundOrderStatusId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InboundOrderStatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public InboundOrderStatusId createinboundOrderStatusId(AddInboundOrderStatusId newinboundOrderStatusId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundorderstatusid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newinboundOrderStatusId, headers);
		ResponseEntity<InboundOrderStatusId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundOrderStatusId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public InboundOrderStatusId updateinboundOrderStatusId(String warehouseId, String inboundOrderStatusId, String companyCodeId,String languageId,String plantId,String loginUserID,
														   UpdateInboundOrderStatusId updateInboundOrderStatusId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateInboundOrderStatusId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundorderstatusid/" + inboundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<InboundOrderStatusId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, InboundOrderStatusId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteInboundOrderStatusId(String warehouseId,String inboundOrderStatusId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundorderstatusid/" + inboundOrderStatusId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public InboundOrderStatusId[] findInboundOrderStatusId(FindInboundOrderStatusId findInboundOrderStatusId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"inboundorderstatusid/find");
			HttpEntity<?>entity = new HttpEntity<>(findInboundOrderStatusId,headers);
			ResponseEntity<InboundOrderStatusId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundOrderStatusId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------ControlTypeId ------------------------------------------------------------------------
	//GET ALL
	public ControlTypeId[] getcontrolTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controltypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ControlTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ControlTypeId getcontrolTypeId(String warehouseId, String controlTypeId,String companyCodeId,String languageId,String plantId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controltypeid/" + controlTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ControlTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ControlTypeId createcontrolTypeId(AddControlTypeId newControlTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controltypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newControlTypeId, headers);
		ResponseEntity<ControlTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ControlTypeId updatecontrolTypeId(String warehouseId, String controlTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											 UpdateControlTypeId updateControlTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateControlTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controltypeid/" + controlTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ControlTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ControlTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deletecontrolTypeId(String warehouseId,String controlTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controltypeid/" + controlTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ControlTypeId[] findControlTypeId(FindControlTypeId findControlTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"controltypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findControlTypeId,headers);
			ResponseEntity<ControlTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------ApprovalId ------------------------------------------------------------------------
	//GET ALL
	public ApprovalId[] getapprovalIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ApprovalId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ApprovalId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ApprovalId getapprovalId(String warehouseId, String approvalId, String approvalLevel,String approvalProcessId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalid/" + approvalId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("approvalLevel",approvalLevel)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("approvalProcessId",approvalProcessId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ApprovalId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ApprovalId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ApprovalId createapprovalId(AddApprovalId newApprovalId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newApprovalId, headers);
		ResponseEntity<ApprovalId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ApprovalId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ApprovalId updateApprovalId(String approvalId,String approvalProcessId,
									   String warehouseId,String approvalLevel,String companyCodeId,String languageId,String plantId,String loginUserID,
									   UpdateApprovalId updateApprovalId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateApprovalId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalid/" + approvalId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("approvalLevel",approvalLevel)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("approvalProcessId",approvalProcessId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ApprovalId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ApprovalId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteapprovalId(String warehouseId,String approvalId,String approvalLevel,String approvalProcessId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalid/" + approvalId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("approvalLevel",approvalLevel)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("approvalProcessId",approvalProcessId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ApprovalId[] findApprovalId(FindApprovalId findApprovalId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"approvalid/find");
			HttpEntity<?>entity = new HttpEntity<>(findApprovalId,headers);
			ResponseEntity<ApprovalId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ApprovalId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------RefDocTypeId ------------------------------------------------------------------------
	//GET ALL
	public RefDocTypeId[] getrefDocTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "refdoctypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RefDocTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RefDocTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public RefDocTypeId getrefDocTypeId(String warehouseId,String referenceDocumentTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "refdoctypeid/" + referenceDocumentTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<RefDocTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, RefDocTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public RefDocTypeId createrefDocTypeId(AddRefDocTypeId newRefDocTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "refdoctypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newRefDocTypeId, headers);
		ResponseEntity<RefDocTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RefDocTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public RefDocTypeId updateRefDocTypeId(String warehouseId, String referenceDocumentTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
										   UpdateRefDocTypeId updateRefDocTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateRefDocTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "refdoctypeid/" + referenceDocumentTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<RefDocTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, RefDocTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteRefDocTypeId(String warehouseId,String referenceDocumentTypeId,String companyCodeId,String languageId,String plantId,
									  String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "refdoctypeid/" + referenceDocumentTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public RefDocTypeId[] findRefDocTypeId(FindRefDocTypeId findRefDocTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"refdoctypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findRefDocTypeId,headers);
			ResponseEntity<RefDocTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, RefDocTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------ControlProcessId ------------------------------------------------------------------------
	//GET ALL
	public ControlProcessId[] getControlProcessIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controlprocessid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ControlProcessId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ControlProcessId getControlProcessId(String warehouseId,String controlProcessId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controlprocessid/" + controlProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ControlProcessId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ControlProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ControlProcessId createControlProcessId(AddControlProcessId newControlProcessId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controlprocessid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newControlProcessId, headers);
		ResponseEntity<ControlProcessId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlProcessId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ControlProcessId updateControlProcessId(String warehouseId, String controlProcessId,String companyCodeId,String languageId,String plantId,String loginUserID,
												   UpdateControlProcessId updateControlProcessId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateControlProcessId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controlprocessid/" + controlProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ControlProcessId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ControlProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deletecontrolProcessId(String warehouseId,String controlProcessId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "controlprocessid/" + controlProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ControlProcessId[] findControlProcessId(FindControlProcessId findControlProcessId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"controlprocessid/find");
			HttpEntity<?>entity = new HttpEntity<>(findControlProcessId,headers);
			ResponseEntity<ControlProcessId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ControlProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------AisleId ------------------------------------------------------------------------
	//GET ALL
	public AisleId[] getAisleIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "aisleid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AisleId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AisleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public AisleId getAisleId(String warehouseId,String aisleId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "aisleid/" + aisleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<AisleId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, AisleId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public AisleId createAisleId(AddAisleId newAisled, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "aisleid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newAisled, headers);
		ResponseEntity<AisleId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AisleId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public AisleId updateAisleId(String warehouseId,String aisleId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId,String loginUserID,
								 UpdateAisleId updateAisleId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateAisleId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "aisleid/" + aisleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<AisleId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, AisleId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteAisleId(String warehouseId,String aisleId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "aisleid/" + aisleId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public AisleId[] findAisleId(FindAisleId findAisleId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"aisleid/find");
			HttpEntity<?>entity = new HttpEntity<>(findAisleId,headers);
			ResponseEntity<AisleId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AisleId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------SpanId ------------------------------------------------------------------------
	//GET ALL
	public SpanId[] getSpanIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "spanid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SpanId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SpanId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public SpanId getSpanId(String warehouseId,String aisleId,String spanId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "spanid/" + spanId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("plantId",plantId)
							.queryParam("languageId",languageId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SpanId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SpanId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public SpanId createSpanId(AddSpanId newSpanId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "spanid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newSpanId, headers);
		ResponseEntity<SpanId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SpanId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public SpanId updateSpanId(String warehouseId,String aisleId,String spanId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId,String loginUserID,
							   UpdateSpanId updateSpanId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateSpanId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "spanid/" + spanId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<SpanId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SpanId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteSpanId( String warehouseId,String aisleId,String spanId,Long floorId,String storageSectionId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "spanid/" + spanId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public SpanId[] findSpanId(FindSpanId findSpanId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"spanid/find");
			HttpEntity<?>entity = new HttpEntity<>(findSpanId,headers);
			ResponseEntity<SpanId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SpanId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------ShelfId ------------------------------------------------------------------------
	//GET ALL
	public ShelfId[] getShelfIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "shelfid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ShelfId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ShelfId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public ShelfId getShelfId(String shelfId,String warehouseId, String aisleId,String spanId, Long floorId, String storageSectionId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "shelfid/" + shelfId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("spanId",spanId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ShelfId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ShelfId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ShelfId createShelfId(AddShelfId newShelfId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "shelfid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newShelfId, headers);
		ResponseEntity<ShelfId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ShelfId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ShelfId updateShelfId(String shelfId,String warehouseId, String aisleId,String spanId,Long floorId, String storageSectionId,String companyCodeId,String languageId,String plantId,String loginUserID,
								 UpdateShelfId updateShelfId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateShelfId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "shelfid/" + shelfId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("spanId",spanId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ShelfId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ShelfId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteShelfId(String warehouseId,String aisleId, String shelfId,String spanId,Long floorId, String storageSectionId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "shelfid/" + shelfId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("aisleId",aisleId)
							.queryParam("spanId",spanId)
							.queryParam("floorId",floorId)
							.queryParam("storageSectionId",storageSectionId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ShelfId[] findShelfId(FindShelfId findShelfId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"shelfid/find");
			HttpEntity<?>entity = new HttpEntity<>(findShelfId,headers);
			ResponseEntity<ShelfId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ShelfId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------BinSectionId ------------------------------------------------------------------------
	//GET ALL
	public BinSectionId[] getBinSectionIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binsectionid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BinSectionId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BinSectionId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public BinSectionId getBinSectionId(String warehouseId,String binSectionId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binsectionid/" + binSectionId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<BinSectionId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BinSectionId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public BinSectionId createBinSectionId(AddBinSectionId newBinSectionId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binsectionid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newBinSectionId, headers);
		ResponseEntity<BinSectionId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BinSectionId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public BinSectionId updateBinSectionId(String warehouseId, String binSectionId,String companyCodeId,String languageId,String plantId, String loginUserID,
										   UpdateBinSectionId updateBinSectionId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateBinSectionId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binsectionid/" + binSectionId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<BinSectionId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BinSectionId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteBinSectionId(String warehouseId,String binSectionId,String companyCodeId,String languageId,String plantId, String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "binsectionid/" + binSectionId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public BinSectionId[] findBinSectionId(FindBinSectionId findBinSectionId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"binsectionid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBinSectionId,headers);
			ResponseEntity<BinSectionId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BinSectionId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------DateFormatId ------------------------------------------------------------------------
	//GET ALL
	public DateFormatId[] getDateFormatIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dateformatid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DateFormatId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DateFormatId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public DateFormatId getDateFormatId(String warehouseId,String dateFormatId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dateformatid/" + dateFormatId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DateFormatId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DateFormatId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public DateFormatId createDateFormatId(AddDateFormatId newDateFormatId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dateformatid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newDateFormatId, headers);
		ResponseEntity<DateFormatId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DateFormatId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public DateFormatId updateDateFormatId(String warehouseId, String dateFormatId,String companyCodeId,String languageId,String plantId,String loginUserID,
										   UpdateDateFormatId updateDateFormatId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateDateFormatId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dateformatid/" + dateFormatId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<DateFormatId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DateFormatId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteDateFormatId(String warehouseId,String dateFormatId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "dateformatid/" + dateFormatId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public DateFormatId[] findDateFormatId(FindDateFormatId findDateFormatId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"dateformatid/find");
			HttpEntity<?>entity = new HttpEntity<>(findDateFormatId,headers);
			ResponseEntity<DateFormatId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DateFormatId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------DecimalNotationId ------------------------------------------------------------------------
	//GET ALL
	public DecimalNotationId[] getDecimalNotationIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "decimalnotationid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DecimalNotationId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DecimalNotationId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public DecimalNotationId getDecimalNotationId(String warehouseId,String decimalNotationId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "decimalnotationid/" + decimalNotationId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<DecimalNotationId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, DecimalNotationId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public DecimalNotationId createDecimalNotationId(AddDecimalNotationId newDecimalNotationId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "decimalnotationid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newDecimalNotationId, headers);
		ResponseEntity<DecimalNotationId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DecimalNotationId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public DecimalNotationId updateDecimalNotationId(String warehouseId, String decimalNotationId,String companyCodeId,String languageId,String plantId,String loginUserID,
													 UpdateDecimalNotationId updateDecimalNotationId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateDecimalNotationId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "decimalnotationid/" + decimalNotationId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<DecimalNotationId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, DecimalNotationId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteDecimalNotationId(String warehouseId, String decimalNotationId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "decimalnotationid/" + decimalNotationId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public DecimalNotationId[] findDecimalNotationId(FindDecimalNotationId findDecimalNotationId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"decimalnotationid/find");
			HttpEntity<?>entity = new HttpEntity<>(findDecimalNotationId,headers);
			ResponseEntity<DecimalNotationId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, DecimalNotationId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------LanguageId ------------------------------------------------------------------------
	//GET ALL
	public LanguageId[] getLanguageIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "languageid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LanguageId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LanguageId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public LanguageId getLanguageId(String languageId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "languageid/" + languageId)
					;
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<LanguageId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, LanguageId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public LanguageId createLanguageId(AddLanguageId newLanguageId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "languageid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newLanguageId, headers);
		ResponseEntity<LanguageId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LanguageId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public LanguageId updateLanguageId(String languageId,String loginUserID,
									   UpdateLanguageId updateLanguageId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateLanguageId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "languageid/" + languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<LanguageId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, LanguageId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteLanguageId(String languageId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "languageid/" + languageId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public LanguageId[] findLanguageId(FindLanguageId findLanguageId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"languageid/find");
			HttpEntity<?>entity = new HttpEntity<>(findLanguageId,headers);
			ResponseEntity<LanguageId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, LanguageId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//--------------------------------------------StatusMessagesId ------------------------------------------------------------------------
	public StatusMessagesId[] getStatusMessagesIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusmessagesid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StatusMessagesId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusMessagesId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public StatusMessagesId getStatusMessagesId(String messagesId, String languageId, String messageType,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusmessagesid/" + messagesId)
							.queryParam("languageId",languageId)
							.queryParam("messageType",messageType);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StatusMessagesId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StatusMessagesId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public StatusMessagesId createStatusMessagesId(AddStatusMessagesId newStatusMessagesId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusmessagesid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStatusMessagesId, headers);
		ResponseEntity<StatusMessagesId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusMessagesId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public StatusMessagesId updateStatusMessagesId(String messagesId, String languageId, String messageType, String loginUserID,
												   UpdateStatusMessagesId updateStatusMessagesId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateStatusMessagesId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusmessagesid/" + messagesId)
							.queryParam("languageId",languageId)
							.queryParam("messageType",messageType)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<StatusMessagesId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StatusMessagesId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteStatusMessagesId(String messageId, String languageId, String messageType, String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "statusmessagesid/" + messageId)
							.queryParam("languageId",languageId)
							.queryParam("messageType",messageType)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StatusMessagesId[] findStatusMessagesId(FindStatusMessagesId findStatusMessagesId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"statusmessagesid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStatusMessagesId,headers);
			ResponseEntity<StatusMessagesId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StatusMessagesId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------MovementTypeId ------------------------------------------------------------------------
	public MovementTypeId[] getMovementTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "movementtypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MovementTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MovementTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public MovementTypeId getMovementTypeId(String warehouseId,String movementTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "movementtypeid/" + movementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<MovementTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, MovementTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public MovementTypeId createMovementTypeId(AddMovementTypeId newMovementTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "movementtypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newMovementTypeId, headers);
		ResponseEntity<MovementTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MovementTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public MovementTypeId updateMovementTypeId(String warehouseId,String movementTypeId,String companyCodeId,String languageId,String plantId, String loginUserID,
											   UpdateMovementTypeId updateMovementTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateMovementTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "movementtypeid/" + movementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<MovementTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, MovementTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteMovementTypeId(String warehouseId,String movementTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "movementtypeid/" + movementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public MovementTypeId[] findMovementTypeId(FindMovementTypeId findMovementTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"movementtypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findMovementTypeId,headers);
			ResponseEntity<MovementTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, MovementTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------SubMovementTypeId ------------------------------------------------------------------------
	//GETALL
	public SubMovementTypeId[] getSubMovementTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "submovementtypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubMovementTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubMovementTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public SubMovementTypeId getSubMovementTypeId(String warehouseId,String movementTypeId,String subMovementTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "submovementtypeid/" + subMovementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("movementTypeId",movementTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubMovementTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubMovementTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public SubMovementTypeId createSubMovementTypeId(AddSubMovementTypeId newSubMovementTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "submovementtypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newSubMovementTypeId, headers);
		ResponseEntity<SubMovementTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubMovementTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public SubMovementTypeId updateSubMovementTypeId(String warehouseId,String movementTypeId,String subMovementTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
													 UpdateSubMovementTypeId updateSubMovementTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateSubMovementTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "submovementtypeid/" + subMovementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("movementTypeId",movementTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<SubMovementTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SubMovementTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteSubMovementTypeId(String warehouseId,String movementTypeId,String subMovementTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "submovementtypeid/" + subMovementTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("movementTypeId",movementTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public SubMovementTypeId[] findSubMovementTypeId(FindSubMovementTypeId findSubMovementTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"submovementtypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findSubMovementTypeId,headers);
			ResponseEntity<SubMovementTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubMovementTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------OutboundOrderTypeId ------------------------------------------------------------------------
	public OutboundOrderTypeId[] getOutboundOrderTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundordertypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<OutboundOrderTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OutboundOrderTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public OutboundOrderTypeId getOutboundOrderTypeId(String warehouseId,String outboundOrderTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundordertypeid/" + outboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<OutboundOrderTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, OutboundOrderTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public OutboundOrderTypeId createOutboundOrderTypeId(AddOutboundOrderTypeId newOutboundOrderTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundordertypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newOutboundOrderTypeId, headers);
		ResponseEntity<OutboundOrderTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundOrderTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public OutboundOrderTypeId updateOutboundOrderTypeId(String warehouseId, String outboundOrderTypeId,String companyCodeId,String languageId,String plantId, String loginUserID,
														 UpdateOutboundOrderTypeId updateOutboundOrderTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateOutboundOrderTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundordertypeid/" + outboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<OutboundOrderTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, OutboundOrderTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteOutboundOrderTypeId(String warehouseId,String outboundOrderTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "outboundordertypeid/" + outboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public OutboundOrderTypeId[] findOutboundOrderTypeId(FindOutboundOrderTypeId findOutboundOrderTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"outboundordertypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findOutboundOrderTypeId,headers);
			ResponseEntity<OutboundOrderTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, OutboundOrderTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------InboundOrderTypeId ------------------------------------------------------------------------
	public InboundOrderTypeId[] getInboundOrderTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundordertypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InboundOrderTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InboundOrderTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public InboundOrderTypeId getInboundOrderTypeId(String warehouseId,String inboundOrderTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundordertypeid/" + inboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<InboundOrderTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InboundOrderTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public InboundOrderTypeId createInboundOrderTypeId(AddInboundOrderTypeId newInboundOrderTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundordertypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newInboundOrderTypeId, headers);
		ResponseEntity<InboundOrderTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundOrderTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public InboundOrderTypeId updateInboundOrderTypeId(String warehouseId, String inboundOrderTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
													   UpdateInboundOrderTypeId updateInboundOrderTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateInboundOrderTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundordertypeid/" + inboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID)
					;
			ResponseEntity<InboundOrderTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, InboundOrderTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteInboundOrderTypeId(String warehouseId, String inboundOrderTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "MNRClara's RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "inboundordertypeid/" + inboundOrderTypeId)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);

			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public InboundOrderTypeId[] findInboundOrderTypeId(FindInboundOrderTypeId findInboundOrderTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"inboundordertypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findInboundOrderTypeId,headers);
			ResponseEntity<InboundOrderTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InboundOrderTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------TransferTypeId ------------------------------------------------------------------------
	//GET ALL
	public TransferTypeId[] getTransferTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "transfertypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TransferTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public TransferTypeId getTransferTypeId(String warehouseId,String transferTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "transfertypeid/" + transferTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<TransferTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, TransferTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public TransferTypeId createTransferTypeId(AddTransferTypeId newTransferTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "transfertypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newTransferTypeId, headers);
		ResponseEntity<TransferTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TransferTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public TransferTypeId updateTransferTypeId(String warehouseId, String transferTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											   UpdateTransferTypeId updateTransferTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateTransferTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "transfertypeid/" + transferTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<TransferTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, TransferTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteTransferTypeId(String warehouseId, String transferTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "transfertypeid/" + transferTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public TransferTypeId[] findTransferTypeId(FindTransferTypeId findTransferTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"transfertypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findTransferTypeId,headers);
			ResponseEntity<TransferTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, TransferTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------StockTypeId ------------------------------------------------------------------------
	//GET ALL
	public StockTypeId[] getStockTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "stocktypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StockTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public StockTypeId getStockTypeId(String warehouseId, String stockTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "stocktypeid/" + stockTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<StockTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public StockTypeId createStockTypeId(AddStockTypeId newStockTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "stocktypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newStockTypeId, headers);
		ResponseEntity<StockTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StockTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public StockTypeId updateStockTypeId(String warehouseId, String stockTypeId,String companyCodeId,String languageId,String plantId, String loginUserID,
										 UpdateStockTypeId updateStockTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateStockTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "stocktypeid/" + stockTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<StockTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, StockTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteStockTypeId(String warehouseId, String stockTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "stocktypeid/" + stockTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public StockTypeId[] findStockTypeId(FindStockTypeId findStockTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"stocktypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findStockTypeId,headers);
			ResponseEntity<StockTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StockTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------SpecialStockIndicatorId ------------------------------------------------------------------------
	//GET ALL
	public SpecialStockIndicatorId[] getSpecialStockIndicatorIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "specialstockindicatorid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SpecialStockIndicatorId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SpecialStockIndicatorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public SpecialStockIndicatorId getSpecialStockIndicatorId(String warehouseId,String stockTypeId,String specialStockIndicatorId,
															  String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "specialstockindicatorid/" + specialStockIndicatorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("stockTypeId",stockTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SpecialStockIndicatorId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SpecialStockIndicatorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public SpecialStockIndicatorId createSpecialStockIndicatorId(AddSpecialStockIndicatorId newSpecialStockIndicatorId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "specialstockindicatorid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newSpecialStockIndicatorId, headers);
		ResponseEntity<SpecialStockIndicatorId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SpecialStockIndicatorId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public SpecialStockIndicatorId updateSpecialStockIndicatorId(String warehouseId, String stockTypeId, String specialStockIndicatorId,String companyCodeId,String languageId,String plantId,String loginUserID,
																 UpdateSpecialStockIndicatorId updateSpecialStockIndicatorId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateSpecialStockIndicatorId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "specialstockindicatorid/" + specialStockIndicatorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("stockTypeId",stockTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<SpecialStockIndicatorId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SpecialStockIndicatorId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteSpecialStockIndicatorId(String warehouseId, String stockTypeId, String specialStockIndicatorId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "specialstockindicatorid/" + specialStockIndicatorId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("stockTypeId",stockTypeId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public SpecialStockIndicatorId[] findSpecialStockIndicatorId(FindSpecialStockIndicatorId findSpecialStockIndicatorId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"specialstockindicatorid/find");
			HttpEntity<?>entity = new HttpEntity<>(findSpecialStockIndicatorId,headers);
			ResponseEntity<SpecialStockIndicatorId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SpecialStockIndicatorId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------HandlingEquipmentId ------------------------------------------------------------------------
	//GET ALL
	public HandlingEquipmentId[] getHandlingEquipmentIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingequipmentid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HandlingEquipmentId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HandlingEquipmentId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public HandlingEquipmentId getHandlingEquipmentId(String warehouseId,Long handlingEquipmentNumber,
													  String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingequipmentid/" + handlingEquipmentNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HandlingEquipmentId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HandlingEquipmentId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public HandlingEquipmentId createHandlingEquipmentId(AddHandlingEquipmentId newHandlingEquipmentId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingequipmentid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newHandlingEquipmentId, headers);
		ResponseEntity<HandlingEquipmentId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HandlingEquipmentId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	//PATCH
	public HandlingEquipmentId updateHandlingEquipmentId(String warehouseId, Long handlingEquipmentNumber,
														 String companyCodeId,String languageId,String plantId,String loginUserID,
														 UpdateHandlingEquipmentId updateHandlingEquipmentId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateHandlingEquipmentId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingequipmentid/" + handlingEquipmentNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<HandlingEquipmentId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, HandlingEquipmentId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteHandlingEquipmentId(String warehouseId, Long handlingEquipmentNumber,
											 String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingequipmentid/" + handlingEquipmentNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public HandlingEquipmentId[] findHandlingEquipmentId(FindHandlingEquipmentId findHandlingEquipmentId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"handlingequipmentid/find");
			HttpEntity<?>entity = new HttpEntity<>(findHandlingEquipmentId,headers);
			ResponseEntity<HandlingEquipmentId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HandlingEquipmentId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------HandlingUnitId ------------------------------------------------------------------------
	//GET ALL
	public HandlingUnitId[] getHandlingUnitIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingunitid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HandlingUnitId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HandlingUnitId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public HandlingUnitId getHandlingUnitId(String warehouseId,String handlingUnitNumber,
											String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingunitid/" + handlingUnitNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<HandlingUnitId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, HandlingUnitId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public HandlingUnitId createHandlingUnitId(AddHandlingUnitId newHandlingUnitId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingunitid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newHandlingUnitId, headers);
		ResponseEntity<HandlingUnitId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HandlingUnitId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public HandlingUnitId updateHandlingUnitId(String warehouseId,String handlingUnitNumber,String companyCodeId,
											   String languageId,String plantId, String loginUserID,
											   UpdateHandlingUnitId updateHandlingUnitId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateHandlingUnitId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingunitid/" + handlingUnitNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<HandlingUnitId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, HandlingUnitId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteHandlingUnitId(String warehouseId,String handlingUnitNumber,String companyCodeId,
										String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "handlingunitid/" + handlingUnitNumber)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public HandlingUnitId[] findHandlingUnitId(FindHandlingUnitId findHandlingUnitId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"handlingunitid/find");
			HttpEntity<?>entity = new HttpEntity<>(findHandlingUnitId,headers);
			ResponseEntity<HandlingUnitId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, HandlingUnitId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------CycleCountTypeId------------------------------------------------------------------------
	//GET ALL
	public CycleCountTypeId[] getCycleCountTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "cyclecounttypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CycleCountTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CycleCountTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public CycleCountTypeId getCycleCountTypeId(String warehouseId,String cycleCountTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "cyclecounttypeid/" + cycleCountTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<CycleCountTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, CycleCountTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public CycleCountTypeId createCycleCountTypeId(AddCycleCountTypeId newCycleCountTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "cyclecounttypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newCycleCountTypeId, headers);
		ResponseEntity<CycleCountTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CycleCountTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public CycleCountTypeId updateCycleCountTypeId(String warehouseId, String cycleCountTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
												   UpdateCycleCountTypeId updateCycleCountTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateCycleCountTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "cyclecounttypeid/" + cycleCountTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<CycleCountTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, CycleCountTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteCycleCountTypeId(String warehouseId,String cycleCountTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "cyclecounttypeid/" + cycleCountTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public CycleCountTypeId[] findCycleCountTypeId(FindCycleCountTypeId findCycleCountTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"cyclecounttypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findCycleCountTypeId,headers);
			ResponseEntity<CycleCountTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, CycleCountTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	//--------------------------------------------ReturnTypeId ------------------------------------------------------------------------
	//GET ALL
	public ReturnTypeId[] getReturnTypeIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "returntypeid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ReturnTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ReturnTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ReturnTypeId getReturnTypeId(String warehouseId,String returnTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "returntypeid/" + returnTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ReturnTypeId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ReturnTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ReturnTypeId createReturnTypeId(AddReturnTypeId newReturnTypeId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "returntypeid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newReturnTypeId, headers);
		ResponseEntity<ReturnTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ReturnTypeId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ReturnTypeId updateReturnTypeId(String warehouseId, String returnTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
										   UpdateReturnTypeId updateReturnTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateReturnTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "returntypeid/" + returnTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ReturnTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ReturnTypeId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteReturnTypeId(String warehouseId, String returnTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "returntypeid/" + returnTypeId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ReturnTypeId[] findReturnTypeId(FindReturnTypeId findReturnTypeId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"returntypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findReturnTypeId,headers);
			ResponseEntity<ReturnTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ReturnTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------ApprovalProcessId ------------------------------------------------------------------------
	//GET ALL
	public ApprovalProcessId[] getApprovalProcessIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalprocessid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ApprovalProcessId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ApprovalProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ApprovalProcessId getApprovalProcessId(String warehouseId, String approvalProcessId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalprocessid/" + approvalProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ApprovalProcessId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ApprovalProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ApprovalProcessId createApprovalProcessId(AddApprovalProcessId newApprovalProcessId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalprocessid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newApprovalProcessId, headers);
		ResponseEntity<ApprovalProcessId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ApprovalProcessId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ApprovalProcessId updateApprovalProcessId(String warehouseId, String approvalProcessId,String companyCodeId,String languageId,String plantId, String loginUserID,
													 UpdateApprovalProcessId updateApprovalProcessId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateApprovalProcessId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalprocessid/" + approvalProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ApprovalProcessId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ApprovalProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteApprovalProcessId(String warehouseId, String approvalProcessId,String companyCodeId,String languageId,String plantId, String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "approvalprocessid/" + approvalProcessId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ApprovalProcessId[] findApprovalProcessId(FindApprovalProcessId findApprovalProcessId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"approvalprocessid/find");
			HttpEntity<?>entity = new HttpEntity<>(findApprovalProcessId,headers);
			ResponseEntity<ApprovalProcessId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ApprovalProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//--------------------------------------------ProcessId ------------------------------------------------------------------------
	//GET ALL
	public ProcessId[] getProcessIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ProcessId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ProcessId getProcessId(String warehouseId, String processId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processid/" + processId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<ProcessId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public ProcessId createProcessId(AddProcessId newProcessId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newProcessId, headers);
		ResponseEntity<ProcessId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProcessId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public ProcessId updateProcessId(String warehouseId, String processId,String companyCodeId,String languageId,String plantId,String loginUserID,
									 UpdateProcessId updateProcessId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateProcessId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processid/" + processId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ProcessId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ProcessId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteProcessId(String warehouseId, String processId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "processid/" + processId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public ProcessId[] findProcessId(FindProcessId findProcessId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"processid/find");
			HttpEntity<?>entity = new HttpEntity<>(findProcessId,headers);
			ResponseEntity<ProcessId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ProcessId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



	//--------------------------------------------SubLevelId ------------------------------------------------------------------------
	//GET ALL
	public SubLevelId[] getSubLevelIds(String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "sublevelid");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubLevelId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubLevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public SubLevelId getSubLevelId(String warehouseId,String subLevelId,Long levelId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "sublevelid/" + subLevelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("levelId",levelId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			ResponseEntity<SubLevelId> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, SubLevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//CREATE
	public SubLevelId createSubLevelId(AddSubLevelId newSubLevelId, String loginUserID, String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "sublevelid")
				.queryParam("loginUserID", loginUserID);
		HttpEntity<?> entity = new HttpEntity<>(newSubLevelId, headers);
		ResponseEntity<SubLevelId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubLevelId.class);
		//		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	//PATCH
	public SubLevelId updateSubLevelId(String warehouseId,String subLevelId,Long levelId,String companyCodeId,String languageId,String plantId,String loginUserID,
									   UpdateSubLevelId updateSubLevelId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(updateSubLevelId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "sublevelid/" + subLevelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("levelId",levelId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<SubLevelId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, SubLevelId.class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//DELETE
	public boolean deleteSubLevelId(String warehouseId,String subLevelId,Long levelId,String companyCodeId,String languageId,String plantId,String loginUserID,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "sublevelid/" + subLevelId)
							.queryParam("warehouseId", warehouseId)
							.queryParam("levelId",levelId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId)
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			log.info("result : " + result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//SEARCH
	public SubLevelId[] findSubLevelId(FindSubLevelId findSubLevelId,String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"sublevelid/find");
			HttpEntity<?>entity = new HttpEntity<>(findSubLevelId,headers);
			ResponseEntity<SubLevelId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, SubLevelId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// ThreePL

	/*--------------------------------------BillingFormatId---------------------------------------------------------------------------------*/
	// GET ALL
	public BillingFormatId[] getBillingFormatIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid");
			ResponseEntity<BillingFormatId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormatId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public BillingFormatId getBillingFormatId(String warehouseId,Long billFormatId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<BillingFormatId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFormatId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public BillingFormatId addBillingFormatId (AddBillingFormatId newBillingFormatId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newBillingFormatId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingFormatId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFormatId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public BillingFormatId updateBillingFormatId (String warehouseId, Long billFormatId, String companyCodeId, String languageId, String plantId, String loginUserID,
												  UpdateBillingFormatId modifiedBillingFormatId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFormatId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<BillingFormatId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFormatId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteBillingFormatId(String warehouseId,Long billFormatId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingformatid/" + billFormatId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public BillingFormatId[] findBillingFormatId(FindBillingFormatId findBillingFormatId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingformatid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBillingFormatId,headers);
			ResponseEntity<BillingFormatId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFormatId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------BillingFrequencyId---------------------------------------------------------------------------------*/
	// GET ALL
	public BillingFrequencyId[] getBillingFrequencyIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid");
			ResponseEntity<BillingFrequencyId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequencyId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public BillingFrequencyId getBillingFrequencyId(String warehouseId, Long billFrequencyId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<BillingFrequencyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingFrequencyId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public BillingFrequencyId addBillingFrequencyId (AddBillingFrequencyId newBillingFrequencyId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newBillingFrequencyId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingFrequencyId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFrequencyId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public BillingFrequencyId updateBillingFrequencyId (String warehouseId, Long billFrequencyId,String companyCodeId,String languageId,String plantId,String loginUserID,
														UpdateBillingFrequencyId modifiedBillingFrequencyId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingFrequencyId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<BillingFrequencyId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingFrequencyId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteBillingFrequencyId(String warehouseId,Long billFrequencyId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingfrequencyid/" + billFrequencyId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public BillingFrequencyId[] findBillingFrequencyId(FindBillingFrequencyId findBillingFrequencyId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingfrequencyid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBillingFrequencyId,headers);
			ResponseEntity<BillingFrequencyId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingFrequencyId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------BillingModeId---------------------------------------------------------------------------------*/
	// GET ALL
	public BillingModeId[] getBillingModeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid");
			ResponseEntity<BillingModeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingModeId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public BillingModeId getBillingModeId(String warehouseId, Long billModeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<BillingModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, BillingModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public BillingModeId addBillingModeId (AddBillingModeId newBillingModeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newBillingModeId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<BillingModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public BillingModeId updateBillingModeId (String warehouseId, Long billModeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											  UpdateBillingModeId modifiedBillingModeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedBillingModeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<BillingModeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, BillingModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteBillingModeId(String warehouseId,Long billModeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "billingmodeid/" + billModeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public BillingModeId[] findBillingModeId(FindBillingModeId findBillingModeId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"billingmodeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findBillingModeId,headers);
			ResponseEntity<BillingModeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, BillingModeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------PaymentModeId---------------------------------------------------------------------------------*/
	// GET ALL
	public PaymentModeId[] getPaymentModeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid");
			ResponseEntity<PaymentModeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentModeId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public PaymentModeId getPaymentModeId(String warehouseId, Long paymentModeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<PaymentModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public PaymentModeId addPaymentModeId(AddPaymentModeId newPaymentModeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newPaymentModeId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<PaymentModeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public PaymentModeId updatePaymentModeId (String warehouseId, Long paymentModeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											  UpdatePaymentModeId modifiedPaymentModeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedPaymentModeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);

			ResponseEntity<PaymentModeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentModeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deletePaymentModeId(String warehouseId,Long paymentModeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymentmodeid/" + paymentModeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public PaymentModeId[] findPaymentModeId(FindPaymentModeId findPaymentModeId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"paymentmodeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findPaymentModeId,headers);
			ResponseEntity<PaymentModeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentModeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------PaymentTermId---------------------------------------------------------------------------------*/
	// GET ALL
	public PaymentTermId[] getPaymentTermIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid");
			ResponseEntity<PaymentTermId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentTermId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public PaymentTermId getPaymentTermId(String warehouseId, Long paymentTermId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<PaymentTermId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, PaymentTermId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public PaymentTermId addPaymentTermId(AddPaymentTermId newPaymentTermId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newPaymentTermId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<PaymentTermId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentTermId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public PaymentTermId updatePaymentTermId (String warehouseId, Long paymentTermId,String companyCodeId,String languageId,String plantId,String loginUserID,
											  UpdatePaymentTermId modifiedPaymentTermId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(modifiedPaymentTermId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);

			ResponseEntity<PaymentTermId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, PaymentTermId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deletePaymentTermId(String warehouseId,Long paymentTermId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "paymenttermid/" + paymentTermId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public PaymentTermId[] findPaymentTermId(FindPaymentTermId findPaymentTermId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"paymenttermid/find");
			HttpEntity<?>entity = new HttpEntity<>(findPaymentTermId,headers);
			ResponseEntity<PaymentTermId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, PaymentTermId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/*--------------------------------------ServiceTypeId---------------------------------------------------------------------------------*/
	// GET ALL
	public ServiceTypeId[] getServiceTypeIds (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid");
			ResponseEntity<ServiceTypeId[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ServiceTypeId[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public ServiceTypeId getServiceTypeId(String warehouseId, String moduleId, Long serviceTypeId,String companyCodeId,String languageId,String plantId,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
					.queryParam("warehouseId", warehouseId)
					.queryParam("moduleId",moduleId)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<ServiceTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, ServiceTypeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public ServiceTypeId addServiceTypeId(AddServiceTypeId newServiceTypeId, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newServiceTypeId, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<ServiceTypeId> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ServiceTypeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public ServiceTypeId updateServiceTypeId (String warehouseId,String moduleId, Long serviceTypeId,String companyCodeId,String languageId,String plantId,String loginUserID,
											  UpdateServiceTypeId modifiedServiceTypeId, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(modifiedServiceTypeId, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("moduleId",moduleId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<ServiceTypeId> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, ServiceTypeId.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteServiceTypeId(String warehouseId,String moduleId,Long serviceTypeId,String companyCodeId,String languageId,String plantId,String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Threepl RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "servicetypeid/" + serviceTypeId)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("moduleId",moduleId)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	//SEARCH
	public ServiceTypeId[] findServiceTypeId(FindServiceTypeId findServiceTypeId, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"servicetypeid/find");
			HttpEntity<?>entity = new HttpEntity<>(findServiceTypeId,headers);
			ResponseEntity<ServiceTypeId[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, ServiceTypeId[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*--------------------------------------NumberRange---------------------------------------------------------------------------------*/
	// GET ALL
	public NumberRange[] getNumberRanges (String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "numberrange");
			ResponseEntity<NumberRange[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange[].class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// GET
	public NumberRange getNumberRange(String warehouseId,String companyCodeId,String languageId,String plantId,Long numberRangeCode,Long fiscalYear,String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "numberrange/" + numberRangeCode)
					.queryParam("warehouseId", warehouseId)
					.queryParam("fiscalYear",fiscalYear)
					.queryParam("companyCodeId",companyCodeId)
					.queryParam("languageId",languageId)
					.queryParam("plantId",plantId);
			ResponseEntity<NumberRange> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, NumberRange.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// CREATE
	public NumberRange addNumberRange(AddNumberRange newNumberRange, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newNumberRange, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "numberrange")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<NumberRange> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRange.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//UPDATE
	public NumberRange updateNumberRange (String warehouseId, String companyCodeId, String languageId, String plantId, Long numberRangeCode, Long fiscalYear, String loginUserID, UpdateNumberRange updateNumberRange, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(updateNumberRange, headers);
			HttpClient client = HttpClients.createDefault();
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "numberrange/" + numberRangeCode)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("fiscalYear",fiscalYear)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<NumberRange> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, NumberRange.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// DELETE
	public boolean deleteNumberRange(String warehouseId, String companyCodeId, String languageId, String plantId, Long numberRangeCode, Long fiscalYear, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "numberrange/" + numberRangeCode)
							.queryParam("loginUserID", loginUserID)
							.queryParam("warehouseId",warehouseId)
							.queryParam("fiscalYear",fiscalYear)
							.queryParam("companyCodeId",companyCodeId)
							.queryParam("languageId",languageId)
							.queryParam("plantId",plantId);
			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	//SEARCH
	public NumberRange[] findNumberRange(FindNumberRange findNumberRange, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"numberrange/find");
			HttpEntity<?>entity = new HttpEntity<>(findNumberRange,headers);
			ResponseEntity<NumberRange[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, NumberRange[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//---------------------------------------------------------InterimBArcode--------------------------------
	// GET ALL - InterimBarcode
	public InterimBarcode[] getInterimBarcodes(String authToken) throws ParseException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "interimbarcode");
			ResponseEntity<InterimBarcode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InterimBarcode[].class);

			List<InterimBarcode> obList = new ArrayList<>();
			for (InterimBarcode interimBarcode : result.getBody()) {

				obList.add(addingTimeWithDateInterimBarcode(interimBarcode));

			}
			return obList.toArray(new InterimBarcode[obList.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// GET - InterimBarcode
	public InterimBarcode getInterimBarcode(String storageBin, String itemCode, String barcode, String authToken) throws ParseException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "interimbarcode/" + storageBin)
							.queryParam("itemCode", itemCode)
							.queryParam("barcode", barcode);
			ResponseEntity<InterimBarcode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InterimBarcode.class);

			return addingTimeWithDateInterimBarcode(result.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// POST - InterimBarcode
	public InterimBarcode createInterimBarcode(@Valid InterimBarcode newInterimBarcode, String loginUserID, String authToken) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "IdMaster RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			HttpEntity<?> entity = new HttpEntity<>(newInterimBarcode, headers);
			UriComponentsBuilder builder =
					UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() + "interimbarcode")
							.queryParam("loginUserID", loginUserID);
			ResponseEntity<InterimBarcode> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InterimBarcode.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//FIND - InterimBarcode
	public InterimBarcode[] findInterimBarcode(FindInterimBarcode findInterimBarcode, String authToken){
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "RestTemplate");
			headers.add("Authorization", "Bearer " + authToken);
			UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"interimbarcode/find");
			HttpEntity<?>entity = new HttpEntity<>(findInterimBarcode,headers);
			ResponseEntity<InterimBarcode[]> result =
					getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, InterimBarcode[].class);
			log.info("result : " + result.getStatusCode());
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	//Add Time to Date plus 3 Hours
	public InterimBarcode addingTimeWithDateInterimBarcode(InterimBarcode interimBarcode) throws ParseException {

		if (interimBarcode.getCreatedOn() != null) {
			interimBarcode.setCreatedOn(DateUtils.addTimeToDate(interimBarcode.getCreatedOn(), 3));
		}

		if (interimBarcode.getUpdatedOn() != null) {
			interimBarcode.setUpdatedOn(DateUtils.addTimeToDate(interimBarcode.getUpdatedOn(), 3));
		}

		return interimBarcode;
	}

//	/* ------------------------Country-without Authtoken---------------------------------------------------------------------------------------*/
//
//	public List<Country> getCountrys () {
//		return countryRepository.findAll();
//	}
//
//	/**
//	 * getCountry
//	 * @param countryId
//	 * @return
//	 */
//	public Country getCountry (String countryId,String languageId) {
//		log.info("country Id: " + countryId);
//		Country country = countryRepository.findByCountryIdAndLanguageId(countryId,languageId).orElse(null);
//		if (country == null) {
//			throw new BadRequestException("The given ID doesn't exist : " + countryId);
//		}
//		return country;
//	}
//
//	/**
//	 * createCountry
//	 * @param newCountry
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws InvocationTargetException
//	 */
//	public Country createCountry (AddCountry newCountry,String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//		Country dbCountry = new Country();
//		Optional<Country> duplicateCountry = countryRepository.findByCountryIdAndLanguageId(newCountry.getCountryId(), newCountry.getLanguageId());
//		if (!duplicateCountry.isEmpty()) {
//			throw new EntityNotFoundException("Record is Getting Duplicated");
//		} else {
//
//			BeanUtils.copyProperties(newCountry, dbCountry, CommonUtils.getNullPropertyNames(newCountry));
//			dbCountry.setDeletionIndicator(0L);
//			dbCountry.setCreatedBy(loginUserID);
//			dbCountry.setUpdatedBy(loginUserID);
//			dbCountry.setCreatedOn(new Date());
//			dbCountry.setUpdatedOn(new Date());
//			return countryRepository.save(dbCountry);
//		}
//	}
//
//	/**
//	 * updateCountry
//	 * @param countryId
//	 * @param updateCountry
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws InvocationTargetException
//	 */
//	public Country updateCountry (String countryId,String languageId,String loginUserID,UpdateCountry updateCountry)
//			throws IllegalAccessException, InvocationTargetException {
//		Country dbCountry = getCountry(countryId,languageId);
//		BeanUtils.copyProperties(updateCountry, dbCountry, CommonUtils.getNullPropertyNames(updateCountry));
//		dbCountry.setUpdatedBy(loginUserID);
//		dbCountry.setDeletionIndicator(0L);
//		dbCountry.setUpdatedOn(new Date());
//		return countryRepository.save(dbCountry);
//	}
//
//	/**
//	 * deleteCountry
//	 * @param countryId
//	 */
//	public void deleteCountry (String countryId,String languageId) {
//		Country country = getCountry(countryId,languageId);
//		if ( country != null) {
//			countryRepository.delete(country);
//		} else {
//			throw new EntityNotFoundException("Error in deleting Id: " + countryId);
//		}
//	}
//	//Find Country
//
//	public List<Country> findCountry(FindCountry findCountry) throws ParseException {
//
//		CountrySpecification spec = new CountrySpecification(findCountry);
//		List<Country> results = countryRepository.findAll(spec);
//		log.info("results: " + results);
//		return results;
//	}

}