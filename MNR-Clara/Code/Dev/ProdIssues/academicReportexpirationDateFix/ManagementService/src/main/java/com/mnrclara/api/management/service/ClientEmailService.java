package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientemail.AddClientEmail;
import com.mnrclara.api.management.model.clientemail.ClientEmail;
import com.mnrclara.api.management.model.clientemail.SearchClientEmail;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.EMail;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.repository.ClientEmailRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientEmailService {

	@Autowired
	private ClientEmailRepository clientEmailRepository;

	@Autowired
	private ClientGeneralService clientGeneralService;

	@Autowired
	private SetupService setupService;

	@Autowired
	CommonService commonService;

	@Autowired
	AuthTokenService authTokenService;

	/**
	 * getClientEmails
	 * 
	 * @return
	 */
	public List<ClientEmail> getClientEmails() {
		List<ClientEmail> clientEmailList = clientEmailRepository.findAll();
		clientEmailList = clientEmailList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return clientEmailList;
	}

	/**
	 * getClientEmail
	 * 
	 * @param clientEmailId
	 * @return
	 */
	public ClientEmail getClientEmail(Long emailId) {
		ClientEmail clientEmail = clientEmailRepository.findById(emailId).orElse(null);
		if (clientEmail != null && clientEmail.getDeletionIndicator() == 0) {
			return clientEmail;
		} else {
			throw new BadRequestException("The given ClientEmail ID : " + clientEmail + " doesn't exist.");
		}
	}

	/**
	 * 
	 * @param searchClientEmail
	 * @return
	 */
	public List<ClientEmail> findClientEmails(SearchClientEmail searchClientEmail) {
		String mailType = "";
		String sEmailDate = "";
		String eEmailDate = "";
		String from = "";
		String to = "";

		if (searchClientEmail.getMailType() == null) {
			searchClientEmail.setMailType("");
		} else {
			mailType = "%" + searchClientEmail.getMailType() + "%";
		}

		if (searchClientEmail.getFrom() == null) {
			searchClientEmail.setFrom("");
		} else {
			from = "%" + searchClientEmail.getFrom() + "%";
		}

		if (searchClientEmail.getTo() == null) {
			searchClientEmail.setTo("");
		} else {
			to = "%" + searchClientEmail.getTo() + "%";
		}

		if (searchClientEmail.getStartEmailDateTime() != null && searchClientEmail.getEndEmailDateTime() != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sEmailDate = dateFormat.format(searchClientEmail.getStartEmailDateTime());
			eEmailDate = dateFormat.format(searchClientEmail.getEndEmailDateTime());
			log.info("date : " + sEmailDate + "," + eEmailDate);
		}

		List<ClientEmail> clientEmails = clientEmailRepository.findByMultipleParams(mailType, sEmailDate, eEmailDate,
				from, to);

		log.info("ClientEmail #### : " + clientEmails);
		return clientEmails.stream().filter(a -> a.getDeletionIndicator() != null && a.getDeletionIndicator() == 0).collect(Collectors.toList());
	}

	/**
	 * createClientEmail
	 * 
	 * @param newClientEmail
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientEmail createClientEmail(AddClientEmail newClientEmail, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientEmail dbClientEmail = new ClientEmail();
		EMail eMail = new EMail();

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		log.info("userProfile : " + userProfile);

		// FROM
		dbClientEmail.setFromMailId(userProfile.getEmailId());
		eMail.setFromAddress(userProfile.getEmailId());

		// TO
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(newClientEmail.getClientId());
		dbClientEmail.setToMailId(clientGeneral.getEmailId());
		eMail.setToAddress(clientGeneral.getEmailId());

		// CC & BCC
		dbClientEmail.setCc(newClientEmail.getCc());
		eMail.setCcAddress(newClientEmail.getCc());

		// SUBJECT(CASE_NO)
		dbClientEmail.setSubject(newClientEmail.getSubject());
		eMail.setSubject(newClientEmail.getSubject());

		// BODY
		dbClientEmail.setBody(newClientEmail.getBody());
		eMail.setBodyText(newClientEmail.getBody());

		// EMAIL_DATE_TIME
		dbClientEmail.setEmailDateTime(new Date());

		// LANG_ID
		dbClientEmail.setLanguageId("EN");

		// CLASS_ID
		dbClientEmail.setClassId(userProfile.getClassId());

		// MAIL_TYP
		dbClientEmail.setMailType("Outgoing"); // Hard coded value "Outgoing"

		// CLIENT_ID
		dbClientEmail.setClientId(newClientEmail.getClientId());

		// CTD_BY - logged in USR_ID
		dbClientEmail.setCreatedBy(loginUserID);

		// CTD_ON - Server time
		dbClientEmail.setCreatedOn(new Date());

		// UTD_BY - logged in USR_ID
		dbClientEmail.setUpdatedBy(loginUserID);

		// UTD_ON - Server time
		dbClientEmail.setUpdatedOn(new Date());

		dbClientEmail.setDeletionIndicator(0L);

		ClientEmail clientEmail = clientEmailRepository.save(dbClientEmail);

		// --------------------Sending-Mails-to-all-Recipients----------------------------------

		// Get AuthToken for CommunicationService
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		Boolean emailResponse = commonService.sendEmail(eMail, authTokenForCommonService.getAccess_token());
		log.info("Email response : " + emailResponse);

		return clientEmail;
	}

	/**
	 * deleteClientEmail
	 * 
	 * @param clientemailCode
	 */
	public void deleteClientEmail(Long emailDateTime) {
		ClientEmail clientemail = getClientEmail(emailDateTime);
		if (clientemail != null) {
			clientemail.setDeletionIndicator(1L);
			clientEmailRepository.save(clientemail);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + emailDateTime);
		}
	}
}
