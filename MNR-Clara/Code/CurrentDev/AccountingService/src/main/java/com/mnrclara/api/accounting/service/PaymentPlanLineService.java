package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.auth.AuthToken;
import com.mnrclara.api.accounting.model.management.ClientGeneral;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeaderEntity;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanLineEntity;
import com.mnrclara.api.accounting.repository.PaymentPlanHeaderRepository;
import com.mnrclara.api.accounting.repository.PaymentPlanLineRepository;
import com.mnrclara.api.accounting.util.CommonUtils;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentPlanLineService {
	
	@Autowired
	PaymentPlanHeaderRepository paymentPlanHeaderRepository;
	
	@Autowired
	PaymentPlanLineRepository paymentPlanLineRepository;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * getPaymentPlanLines
	 * @return
	 */
	public List<PaymentPlanLineEntity> getPaymentPlanLines () {
		List<PaymentPlanLineEntity> paymentPlanLineList =  paymentPlanLineRepository.findAll();
		paymentPlanLineList = paymentPlanLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return paymentPlanLineList;
	}
	
	/**
	 * getPaymentPlanLine
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @param itemNumber
	 * @return
	 */
	public PaymentPlanLineEntity getPaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber) {
		Optional<PaymentPlanLineEntity> paymentPlanLine = 
				paymentPlanLineRepository.findByPaymentPlanNumberAndPaymentPlanRevisionNoAndItemNumberAndDeletionIndicator(
						paymentPlanNumber, paymentPlanRevisionNo, itemNumber, 0L);
		if (paymentPlanLine.isEmpty()) {
			throw new BadRequestException("The given PaymentPlanHeader ID : " + paymentPlanNumber + 
											" , paymentPlanRevisionNo:" + paymentPlanRevisionNo + 
											" and itemNumber:" + itemNumber + " doesn't exist.");
		}
		return paymentPlanLine.get();
	}
	
	/**
	 * 
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @return
	 */
	public List<PaymentPlanLineEntity> getPaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo) {
		List<PaymentPlanLineEntity> paymentPlanLines = 
				paymentPlanLineRepository.findByPaymentPlanNumberAndPaymentPlanRevisionNoAndDeletionIndicator(
						paymentPlanNumber, paymentPlanRevisionNo, 0L);
		if (paymentPlanLines.isEmpty()) {
			throw new BadRequestException("The given paymentPlanLine ID : " + paymentPlanNumber + 
											" , paymentPlanRevisionNo:" + paymentPlanRevisionNo + " doesn't exist.");
		}
		return paymentPlanLines;
	}
	
	
	/**
	 * createPaymentPlanLine
	 * @param newPaymentPlanLine
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PaymentPlanLineEntity createPaymentPlanLine (PaymentPlanLineEntity newPaymentPlanLineEntity, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<PaymentPlanLineEntity> paymentplanline = 
				paymentPlanLineRepository.findByPaymentPlanNumberAndPaymentPlanRevisionNoAndItemNumberAndDeletionIndicator(
						newPaymentPlanLineEntity.getPaymentPlanNumber(),
						newPaymentPlanLineEntity.getPaymentPlanRevisionNo(),
						newPaymentPlanLineEntity.getItemNumber(),
						0L);
		if (!paymentplanline.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		PaymentPlanLineEntity dbPaymentPlanLine = new PaymentPlanLineEntity();
		log.info("newPaymentPlanLineEntity : " + newPaymentPlanLineEntity);
		BeanUtils.copyProperties(newPaymentPlanLineEntity, dbPaymentPlanLine, CommonUtils.getNullPropertyNames(newPaymentPlanLineEntity));
		dbPaymentPlanLine.setDeletionIndicator(0L);
		dbPaymentPlanLine.setCreatedBy(loginUserID);
		dbPaymentPlanLine.setUpdatedBy(loginUserID);
		dbPaymentPlanLine.setCreatedOn(new Date());
		dbPaymentPlanLine.setUpdatedOn(new Date());
		return paymentPlanLineRepository.save(dbPaymentPlanLine);
	}
	
	/**
	 * updatePaymentPlanLine
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @param itemNumber
	 * @param loginUserID
	 * @param updatePaymentPlanLine
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PaymentPlanLineEntity updatePaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, 
			PaymentPlanLineEntity updatePaymentPlanLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		PaymentPlanLineEntity dbPaymentPlanLineEntity = getPaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber);
		BeanUtils.copyProperties(updatePaymentPlanLine, dbPaymentPlanLineEntity, CommonUtils.getNullPropertyNames(updatePaymentPlanLine));
		dbPaymentPlanLineEntity.setUpdatedBy(loginUserID);
		dbPaymentPlanLineEntity.setUpdatedOn(new Date());
		return paymentPlanLineRepository.save(dbPaymentPlanLineEntity);
	}
	
	/**
	 * deletePaymentPlanLine
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @param itemNumber
	 * @param loginUserID
	 */
	public void deletePaymentPlanLine (String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, String loginUserID) {
		PaymentPlanLineEntity paymentPlanLine = getPaymentPlanLine(paymentPlanNumber, paymentPlanRevisionNo, itemNumber);
		if ( paymentPlanLine != null) {
			paymentPlanLine.setDeletionIndicator(1L);
			paymentPlanLine.setUpdatedBy(loginUserID);
			paymentPlanLineRepository.save(paymentPlanLine);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + paymentPlanRevisionNo);
		}
	}
	
	/**
	 * 
	 * @param clientId 
	 * @param matterNo
	 * @param documentType
	 * @return
	 * @throws ParseException 
	 */
	public Boolean sendReminderSMS () throws ParseException {
		/*
		 * Querying PaymentPlanLines by CurrentDate
		 */
		Date CURRENT_DATE = new Date();
		Date[] dateSearch = DateUtils.addTimeToDatesForSearch(CURRENT_DATE, CURRENT_DATE);
		List<PaymentPlanLineEntity> paymentPlanLines = 
				paymentPlanLineRepository.findByReminderDateBetweenAndDeletionIndicatorAndReferenceField10IsNull(dateSearch[0], dateSearch[1], 0L);
		log.info("paymentPlanLines:" + paymentPlanLines);
		
		for (PaymentPlanLineEntity paymentPlanLine : paymentPlanLines) {
			// Obtaining latest RevisionNo
			PaymentPlanHeaderEntity topPaymentPlanHeader = 
					paymentPlanHeaderRepository.findTopByPaymentPlanNumberOrderByPaymentPlanRevisionNoDesc(paymentPlanLine.getPaymentPlanNumber());
			String paymentPlanText = topPaymentPlanHeader.getPaymentPlanText();
			
			log.info("Status Id :" + topPaymentPlanHeader.getStatusId());
			if (topPaymentPlanHeader.getStatusId() == 1L || topPaymentPlanHeader.getStatusId() == 12L) {
				AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
				ClientGeneral clientGeneral = managementService.getClientGeneral(paymentPlanLine.getClientId(), authTokenForManagementService.getAccess_token());
				String CONTACT_NO = clientGeneral.getContactNumber();
				log.info("CONTACT_NO :" + CONTACT_NO);
				
				// Send SMS
				CONTACT_NO = CONTACT_NO.replaceAll("\\D", "");
				AuthToken authToken = authTokenService.getCommonServiceAuthToken();
				Boolean response = commonService.sendSMS(Long.valueOf(CONTACT_NO), paymentPlanText, authToken.getAccess_token());
				log.info("SMS Response: " + response);
				
				if (response.booleanValue() == true) {
					paymentPlanLine.setReminderStatus("TRUE");
					paymentPlanLine.setReferenceField10("SMS_SENT");
				} else {
					paymentPlanLine.setReminderStatus("FALSE");
				}
				paymentPlanLineRepository.save(paymentPlanLine);
				return response;
			}
		}
		return null;
	}
}
