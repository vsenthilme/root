package com.mnrclara.api.crm.service;

import com.mnrclara.api.crm.controller.exception.BadRequestException;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.pcitform.*;
import com.mnrclara.api.crm.repository.FeedbackFormRepository;
import com.mnrclara.api.crm.repository.PCIntakeFormRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class FeedbackFormService {

	@Autowired
	FeedbackFormRepository feedbackFormRepository;
	
	@Autowired
	PCIntakeFormRepository pcIntakeFormRepository;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	CommonService commonService;

	/**
	 * @param intakeFormNumber
	 * @return
	 */
	public FeedbackForm getFeedbackForm(String intakeFormNumber) {
		Optional<FeedbackForm> feedbackForm = feedbackFormRepository.findByIntakeFormNumberAndDeletionIndicator(intakeFormNumber, 0L);
		if (feedbackForm.isEmpty()) {
			throw new BadRequestException("The given intakeFormNumber " + intakeFormNumber + " doesn't exist.");
		}
		return feedbackForm.get();
	}

	/**
	 * @param newFeedbackForm
	 * @param loginUserID
	 * @return
	 */
	public FeedbackForm createFeedbackForm(FeedbackForm newFeedbackForm, String loginUserID) {

		Optional<FeedbackForm> feedbackForm = feedbackFormRepository.findByIntakeFormNumberAndDeletionIndicator(newFeedbackForm.getIntakeFormNumber(),0L);

		if (!feedbackForm.isEmpty()) {

			throw new BadRequestException("Already record exists with the same Intake FormNumber. Quiting.!");

		} else {

			newFeedbackForm.setDeletionIndicator(0L);
			newFeedbackForm.setCreatedBy(loginUserID);
			newFeedbackForm.setUpdatedBy(loginUserID);
			newFeedbackForm.setCreatedOn(new Date());
			newFeedbackForm.setUpdatedOn(new Date());
			return feedbackFormRepository.save(newFeedbackForm);
		}
	}

	//---------------------SMS-For-Feedback CR-----------------------------------------------------------------------

	/**
	 *
	 * @param intakeFormNumber
	 * @param feedback
	 */
	public void sendFeedbackSMS(String intakeFormNumber, Feedback feedback) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		Boolean isSuccess = false;
		Optional<PCIntakeForm> optPCIntakeForm = pcIntakeFormRepository.findByIntakeFormNumber(intakeFormNumber);
		if (!optPCIntakeForm.isEmpty()) {
			/*
			 * English SMS: Thank you for choosing Monty & Ramirez. Please complete this survey about your most recent visit. 
			 * Spanish SMS: Gracias por elegir Monty and Ramirez. Por favor podría completar estas preguntas acerca de su visita más reciente. Muchas gracias.
			 */
			String englishText = "Thank you for choosing Monty and Ramirez. Please complete this survey about your most recent visit.";
			String spanishText = "Gracias por elegir Monty y Ramirez. Por favor podría completar estas preguntas acerca de su visita más reciente. Muchas gracias.";
			String SMS_TEXT = "";
			PCIntakeForm pcIntakeForm = optPCIntakeForm.get();
			if (pcIntakeForm.getIntakeFormId() == 3L) { // 3 for Spanish
				SMS_TEXT = spanishText + " " + feedback.getUrl();
			} else {
				SMS_TEXT = englishText + " " + feedback.getUrl();
			}
			
			try {
				isSuccess = commonService.sendSMSForFeedback(feedback.getContactNumber(), SMS_TEXT, authTokenForCommonService.getAccess_token());
			} catch (Exception e) {
				isSuccess = false;
			}
			
			// Updating SMS Status
			pcIntakeForm.setSmsStatus(isSuccess);
			if (isSuccess.booleanValue() == true) {
				pcIntakeForm.setUpdatedBy("FEEDBACK SENT");
				pcIntakeForm.setUpdatedOn(new Date());
				PCIntakeForm updatedPCIntakeForm = pcIntakeFormRepository.save(pcIntakeForm);
				log.info("Feedback SMS Sent: " + updatedPCIntakeForm);
			}
		}
	}
}