package com.mnrclara.wrapper.core.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.exception.BadRequestException;
import com.mnrclara.wrapper.core.model.auth.AuthToken;
import com.mnrclara.wrapper.core.model.auth.AuthTokenRequest;
import com.mnrclara.wrapper.core.model.user.NewUser;
import com.mnrclara.wrapper.core.model.user.RegisteredUser;
import com.mnrclara.wrapper.core.repository.RegisterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegisterService {
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	private String getSetupServiceApiUrl () {
		return propertiesConfig.getSetupServiceUrl();
	}
	
	public String getClientSecretID () {
		return propertiesConfig.getClientId();
	}
	
	public String getRandomUUID () {
		return commonService.generateUUID();
	}
	
	/**
	 * 
	 * @param registeredUser
	 * @return
	 */
	public String validateRegisteredUser(@Valid RegisteredUser registeredUser) {
		Optional<NewUser> newUserOpt = registerRepository.findByRegisterId(registeredUser.getRegisterId());
		if (newUserOpt.isEmpty()) {
			throw new BadRequestException("Given Client Register ID: " + registeredUser.getRegisterId() + " does not exist.");
		}
		
		String clientSecretIDFromDB = newUserOpt.get().getClientSecretId();
		
		// Client ID validation check
		if (registeredUser.getClientSecretId().equalsIgnoreCase(clientSecretIDFromDB)) {
			return propertiesConfig.getClientSecretKey();
		} else {
			throw new BadRequestException("Client Secret ID doesn't match. Please provide valid Client ID.");
		}
	}

	/**
	 * 
	 * @param clientName
	 * @return
	 */
	public NewUser registerNewUser (String clientName) {
		NewUser newUser = new NewUser();
		
		Optional<NewUser> newUserOpt = registerRepository.findByClientName(clientName);
		if (!newUserOpt.isEmpty()) {
			throw new BadRequestException("Already " + clientName + " exists.");
		}
		newUser.setClientName(clientName);
		newUser.setRegisterId(getRandomUUID ());
		newUser.setClientSecretId(getClientSecretID());
		newUser = registerRepository.save(newUser);
		return newUser;
	}

	/**
	 * 
	 * @param authTokenRequest
	 * @return
	 */
	public AuthToken getAuthToken(@Valid AuthTokenRequest authTokenRequest) {
		return commonService.generateOAuthToken(authTokenRequest.getApiName(),
				authTokenRequest.getClientId(), 
				authTokenRequest.getClientSecretKey(),
				authTokenRequest.getGrantType(),
				authTokenRequest.getOauthUserName(),
				authTokenRequest.getOauthPassword());	
	}
}