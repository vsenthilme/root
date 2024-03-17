package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
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
import com.mnrclara.api.accounting.model.quotation.QuotationLineEntity;
import com.mnrclara.api.accounting.repository.QuotationLineRepository;
import com.mnrclara.api.accounting.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuotationLineService {
	
	private static final String QUOTATIONLINE = "QUOTATIONLINE";

	@Autowired
	private QuotationLineRepository quotationLineRepository;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * getQuotationLines
	 * @return
	 */
	public List<QuotationLineEntity> getQuotationLines () {
		List<QuotationLineEntity> quotationLineList =  quotationLineRepository.findAll();
		quotationLineList = quotationLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return quotationLineList;
	}
	
	/**
	 * getQuotationLine
	 * @param serialNumber 
	 * @param quotationRevisionNo 
	 * @param quotationRevisionNo
	 * @return
	 */
	public QuotationLineEntity getQuotationLine (String quotationNo, Long quotationRevisionNo, Long serialNumber) {
		Optional<QuotationLineEntity> optQuotationLine = 
				quotationLineRepository.findByQuotationNoAndQuotationRevisionNoAndSerialNumberAndDeletionIndicator(
						quotationNo, quotationRevisionNo, serialNumber, 0L);
		log.info("optQuotationLine : " + optQuotationLine);
		if (!optQuotationLine.isEmpty()) {
			return optQuotationLine.get();
		} else {
			throw new BadRequestException("The record doesnt exists for the given values." + 
					"quotationNo: " + quotationNo + "," + 
					"quotationRevisionNo:" + quotationRevisionNo + "," + 
					"serialNumber: " + serialNumber);
		}
	}
	
	/**
	 * getQuotationLine
	 * @param quotationNo
	 * @param quotationRevisionNo
	 * @return
	 */
	public List<QuotationLineEntity> getQuotationLine (String quotationNo, Long quotationRevisionNo) {
		List<QuotationLineEntity> quotationLines = 
				quotationLineRepository.findByQuotationNoAndQuotationRevisionNoAndDeletionIndicator(
						quotationNo, quotationRevisionNo, 0L);
		if (!quotationLines.isEmpty()) {
			return quotationLines;
		} 
		return null;
	}
	
	/**
	 * getMaxSerialNumber
	 * @param quotationNo
	 * @param quotationRevisionNo
	 * @return
	 */
	public Long getMaxSerialNumber(String quotationNo, Long quotationRevisionNo) {
		Long maxSerialNumber =  quotationLineRepository.getMaxSerialNumber(quotationNo, quotationRevisionNo);
		return maxSerialNumber;
	}
	
	/**
	 * createQuotationLine
	 * @param newQuotationLine
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QuotationLineEntity createQuotationLine (QuotationLineEntity newQuotationLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<QuotationLineEntity> quotationline = 
				quotationLineRepository.findByQuotationNoAndQuotationRevisionNoAndSerialNumberAndDeletionIndicator(
						newQuotationLine.getQuotationNo(),
						newQuotationLine.getQuotationRevisionNo(),
						newQuotationLine.getSerialNumber(),
						0L);
		if (!quotationline.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		QuotationLineEntity dbQuotationLine = new QuotationLineEntity();
		BeanUtils.copyProperties(newQuotationLine, dbQuotationLine, CommonUtils.getNullPropertyNames(newQuotationLine));
		dbQuotationLine.setDeletionIndicator(0L);
		dbQuotationLine.setStatusId(1L);
		dbQuotationLine.setCreatedBy(loginUserID);
		dbQuotationLine.setUpdatedBy(loginUserID);
		dbQuotationLine.setCreatedOn(new Date());
		dbQuotationLine.setUpdatedOn(new Date());
		
		return quotationLineRepository.save(dbQuotationLine);
	}
	
	/**
	 * updateQuotationLine
	 * @param loginUserId 
	 * @param quotationRevisionNo
	 * @param updateQuotationLine
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QuotationLineEntity updateQuotationLine (String quotationNo, 
			Long quotationRevisionNo, Long serialNumber, QuotationLineEntity updateQuotationLine, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		QuotationLineEntity dbQuotationLine = getQuotationLine(quotationNo, quotationRevisionNo, serialNumber);
		
		// Creating Audit Log
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// ITEM_NO
		if (updateQuotationLine.getItemNumber() != null && updateQuotationLine.getItemNumber() != dbQuotationLine.getItemNumber()) {
			log.info("Inserting Audit log for ITEM_NO");
			setupService.createAuditLogRecord(loginUserID, 
					quotationNo, 3L, QUOTATIONLINE, "ITEM_NO", 
					String.valueOf(updateQuotationLine.getItemNumber()), 
					String.valueOf(dbQuotationLine.getItemNumber()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// Item_Text
		if (updateQuotationLine.getItemDescription() != null && updateQuotationLine.getItemDescription() != dbQuotationLine.getItemDescription()) {
			log.info("Inserting Audit log for Item_Text");
			setupService.createAuditLogRecord(loginUserID, 
					quotationNo, 3L, QUOTATIONLINE, "Item_Text", 
					updateQuotationLine.getItemDescription(), 
					dbQuotationLine.getItemDescription(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// Quantity
		if (updateQuotationLine.getQuantity() != null && updateQuotationLine.getQuantity() != dbQuotationLine.getQuantity()) {
			log.info("Inserting Audit log for Quantity");
			setupService.createAuditLogRecord(loginUserID, 
					quotationNo, 3L, QUOTATIONLINE, "Quantity", 
					String.valueOf(updateQuotationLine.getQuantity()), 
					String.valueOf(dbQuotationLine.getQuantity()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// Rate_HR
		if (updateQuotationLine.getRateperHour() != null && updateQuotationLine.getRateperHour() != dbQuotationLine.getRateperHour()) {
			log.info("Inserting Audit log for Rate_HR");
			setupService.createAuditLogRecord(loginUserID, 
					quotationNo, 3L, QUOTATIONLINE, "Rate_HR", 
					String.valueOf(updateQuotationLine.getRateperHour()), 
					String.valueOf(dbQuotationLine.getRateperHour()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// Total_Amt
		if (updateQuotationLine.getTotalAmount() != null && updateQuotationLine.getTotalAmount() != dbQuotationLine.getTotalAmount()) {
			log.info("Inserting Audit log for Total_Amt");
			setupService.createAuditLogRecord(loginUserID, 
					quotationNo, 3L, QUOTATIONLINE, "Total_Amt", 
					String.valueOf(updateQuotationLine.getTotalAmount()), 
					String.valueOf(dbQuotationLine.getTotalAmount()), 
					authTokenForSetupService.getAccess_token());
		}
				
		BeanUtils.copyProperties(updateQuotationLine, dbQuotationLine, CommonUtils.getNullPropertyNames(updateQuotationLine));
		dbQuotationLine.setUpdatedBy(loginUserID);
		dbQuotationLine.setUpdatedOn(new Date());
		dbQuotationLine = quotationLineRepository.save(dbQuotationLine);
		return dbQuotationLine;
	}
	
	/**
	 * deleteQuotationLine
	 * @param loginUserID 
	 * @param quotationRevisionNo
	 */
	public void deleteQuotationLine (String quotationNo, Long quotationRevisionNo, Long serialNumber, String loginUserID) {
		QuotationLineEntity quotationLine = getQuotationLine(quotationNo, quotationRevisionNo, serialNumber);
		if ( quotationLine != null) {
			quotationLine.setDeletionIndicator(1L);
			quotationLine.setUpdatedBy(loginUserID);
			quotationLineRepository.save(quotationLine);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + quotationNo);
		}
	}
}
