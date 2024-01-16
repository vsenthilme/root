package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.auth.AuthToken;
import com.mnrclara.api.setup.model.clientuser.AddClientUser;
import com.mnrclara.api.setup.model.clientuser.ClientUser;
import com.mnrclara.api.setup.model.clientuser.ClientUserImpl;
import com.mnrclara.api.setup.model.clientuser.EMail;
import com.mnrclara.api.setup.model.clientuser.FindClientUser;
import com.mnrclara.api.setup.model.clientuser.SearchClientUser;
import com.mnrclara.api.setup.model.clientuser.UpdateClientUser;
import com.mnrclara.api.setup.repository.ClientUserRepository;
import com.mnrclara.api.setup.repository.specification.ClientUserSpecification;
import com.mnrclara.api.setup.util.CommonUtils;
import com.mnrclara.api.setup.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientUserService {
	
	@Autowired
	ClientUserRepository clientUserRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	public List<ClientUser> getClientUsers () {
		List<ClientUser> clientUserList = clientUserRepository.findAll();
		return clientUserList
				.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
	}
	
	/**
	 * getClientUser
	 * @param clientUserId
	 * @return
	 */
	public ClientUser getClientUser (String clientUserId) {
		log.info("clientUserId: " + clientUserId);
		ClientUser clientUser = clientUserRepository.findByClientUserId(clientUserId).orElse(null);
		if (clientUser != null && clientUser.getDeletionIndicator() != null && 
				clientUser.getDeletionIndicator() == 0L) {
			return clientUser;
		} 
		throw new BadRequestException("The given ClientUser ID : " + clientUserId + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param contactNumber
	 * @return
	 */
	public ClientUser getClientUserByContactNumber (String contactNumber) {
		ClientUser clientUser = clientUserRepository.findByContactNumberAndDeletionIndicator(contactNumber, 0L);
		if (clientUser != null) {
			return clientUser;
		} 
		throw new BadRequestException("The given ContactNumber : " + contactNumber + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param contactNumber
	 * @return
	 */
	public ClientUser getClientUserByEmailId (String emailId) {
		ClientUser clientUser = clientUserRepository.findByEmailIdAndDeletionIndicator(emailId, 0L);
		if (clientUser != null) {
			return clientUser;
		} 
		throw new BadRequestException("The given emailId : " + emailId + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param searchClientUser
	 * @return
	 * @throws ParseException 
	 */
	public List<ClientUser> findClientUser(SearchClientUser searchClientUser) throws ParseException {
		if (searchClientUser.getStartCreatedOn() != null && searchClientUser.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientUser.getStartCreatedOn(), 
					searchClientUser.getEndCreatedOn());
			searchClientUser.setStartCreatedOn(dates[0]);
			searchClientUser.setEndCreatedOn(dates[1]);
		}
		
		ClientUserSpecification spec = new ClientUserSpecification(searchClientUser);
		List<ClientUser> searchResults = clientUserRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}

	/**
	 * 
	 * @param searchClientUser
	 * @return
	 * @throws ParseException
	 */
	public List<ClientUserImpl> findNewClientUser(FindClientUser searchClientUser) throws ParseException {
		List<ClientUserImpl> data = clientUserRepository.getClientUser(searchClientUser.getClassId(),
				searchClientUser.getClientId(),
				searchClientUser.getContactNumber(),
				searchClientUser.getFullName(),
				searchClientUser.getEmailId());
		return data;
	}
	
	/**
	 * createClientUser
	 * @param newClientUser
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientUser createClientUser (AddClientUser newClientUser, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser dbClientUser = new ClientUser();
		BeanUtils.copyProperties(newClientUser, dbClientUser, CommonUtils.getNullPropertyNames(newClientUser));
		dbClientUser.setDeletionIndicator(0L);
		dbClientUser.setCreatedBy(loginUserID);
		dbClientUser.setUpdatedBy(loginUserID);
		dbClientUser.setCreatedOn(new Date());
		dbClientUser.setUpdatedOn(new Date());
		return clientUserRepository.save(dbClientUser);
	}
	
	/**
	 * updateClientUser
	 * @param clientUserId
	 * @param loginUserID
	 * @param updateClientUser
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientUser updateClientUser (String clientUserId, String loginUserID, UpdateClientUser updateClientUser) 
			throws IllegalAccessException, InvocationTargetException {
		ClientUser dbClientUser = getClientUser(clientUserId);
		BeanUtils.copyProperties(updateClientUser, dbClientUser, CommonUtils.getNullPropertyNames(updateClientUser));
		dbClientUser.setUpdatedBy(loginUserID);
		dbClientUser.setUpdatedOn(new Date());
		return clientUserRepository.save(dbClientUser);
	}
	
	/**
	 * deleteClientUser
	 * @param loginUserID 
	 * @param clientUserId
	 */
	public void deleteClientUser (String clientUserId, String loginUserID) {
		ClientUser clientUser = getClientUser(clientUserId);
		if ( clientUser != null) {
			clientUser.setDeletionIndicator(1L);
			clientUser.setUpdatedBy(loginUserID);
			clientUserRepository.save(clientUser);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientUserId);
		}
	}
	
	/**
	 * 
	 * @param eMail
	 */
	public void sendEmail(@Valid EMail eMail) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		commonService.sendEmail(eMail, authTokenForCommonService.getAccess_token());
		log.info("Mail sent");
	}
}
