package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.expirationdate.AddExpirationDate;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.receiptappnotice.AddReceiptAppNotice;
import com.mnrclara.api.management.model.receiptappnotice.ReceiptAppNotice;
import com.mnrclara.api.management.model.receiptappnotice.UpdateReceiptAppNotice;
import com.mnrclara.api.management.repository.ReceiptAppNoticeRepository;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceiptAppNoticeService {
	
	@Autowired
	private ReceiptAppNoticeRepository receiptAppNoticeRepository;
	
	@Autowired
	private MatterGenAccService matterGenAccService;
	
	@Autowired
	private ExpirationDateService expirationDateService;
	
	/**
	 * getReceiptAppNotices
	 * @return
	 */
	public List<ReceiptAppNotice> getReceiptAppNotices () {
		List<ReceiptAppNotice> receiptAppNoticeList =  receiptAppNoticeRepository.findAll();
		receiptAppNoticeList = receiptAppNoticeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return receiptAppNoticeList;
	}
	
	/**
	 * getReceiptAppNotice
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param receiptNo
	 * @return
	 */
	public ReceiptAppNotice getReceiptAppNotice (String languageId, Long classId, String matterNumber, String receiptNo) {
		Optional<ReceiptAppNotice> receiptAppNotice = 
				receiptAppNoticeRepository.findByLanguageIdAndClassIdAndMatterNumberAndReceiptNoAndDeletionIndicator(
						languageId,
						classId,
						matterNumber,
						receiptNo,
						0L);
		if (!receiptAppNotice.isEmpty()) {
			return receiptAppNotice.get();
		} else {
			throw new BadRequestException("The given ReceiptAppNotice ID : " + receiptNo + ","
					+ "languageId: " + languageId + ", classId: " + classId + ", matterNumber: " + matterNumber + "doesn't exist.");
		}
	}	
	
	/**
	 * createReceiptAppNotice
	 * @param newReceiptAppNotice
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ReceiptAppNotice createReceiptAppNotice (AddReceiptAppNotice newReceiptAppNotice, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<ReceiptAppNotice> receiptappnotice = 
				receiptAppNoticeRepository.findByLanguageIdAndClassIdAndMatterNumberAndReceiptNoAndDeletionIndicator(
						newReceiptAppNotice.getLanguageId(),
						newReceiptAppNotice.getClassId(),
						newReceiptAppNotice.getMatterNumber(),
						newReceiptAppNotice.getReceiptNo(),
						0L);
		if (!receiptappnotice.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		ReceiptAppNotice dbReceiptAppNotice = new ReceiptAppNotice();
		log.info("newReceiptAppNotice : " + newReceiptAppNotice);
		BeanUtils.copyProperties(newReceiptAppNotice, dbReceiptAppNotice, CommonUtils.getNullPropertyNames(newReceiptAppNotice));
		
		// StatusId - Hard Coded value - 42
		dbReceiptAppNotice.setStatusId(42L);
		
		// LangId, ClassId, ClientId
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newReceiptAppNotice.getMatterNumber());
		
		dbReceiptAppNotice.setLanguageId(matterGenAcc.getLanguageId());
		dbReceiptAppNotice.setClassId(matterGenAcc.getClassId());
		dbReceiptAppNotice.setClientId(matterGenAcc.getClientId());		
		dbReceiptAppNotice.setDeletionIndicator(0L);
		dbReceiptAppNotice.setCreatedBy(loginUserID);
		dbReceiptAppNotice.setUpdatedBy(loginUserID);
		dbReceiptAppNotice.setCreatedOn(new Date());
		dbReceiptAppNotice.setUpdatedOn(new Date());
		return receiptAppNoticeRepository.save(dbReceiptAppNotice);
	}
	
	/**
	 * updateReceiptAppNotice
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param receiptNo
	 * @param updateReceiptAppNotice
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ReceiptAppNotice updateReceiptAppNotice (String languageId, Long classId, String matterNumber, String receiptNo, 
			UpdateReceiptAppNotice updateReceiptAppNotice, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("updateReceiptAppNotice : " + updateReceiptAppNotice);
		ReceiptAppNotice dbReceiptAppNotice = getReceiptAppNotice(languageId, classId, matterNumber, receiptNo);
		BeanUtils.copyProperties(updateReceiptAppNotice, dbReceiptAppNotice, CommonUtils.getNullPropertyNames(updateReceiptAppNotice));
		dbReceiptAppNotice.setReceiptNo(receiptNo);
		dbReceiptAppNotice.setUpdatedBy(loginUserID);
		dbReceiptAppNotice.setUpdatedOn(new Date());
		
		log.info("dbReceiptAppNotice---1----> : " + dbReceiptAppNotice);
		ReceiptAppNotice updatedReceiptAppNotice = receiptAppNoticeRepository.save(dbReceiptAppNotice);
		log.info("dbReceiptAppNotice---2----> : " + updatedReceiptAppNotice);
		
		/*
		 * When STATUS_ID = 10, in RECEIPTAPPNOTICE update functionality insert a record in EXPIRATIONDATE table as below
		 */
		if (updateReceiptAppNotice.getStatusId() == 10L) {
			AddExpirationDate newExpirationDate = new AddExpirationDate();
			BeanUtils.copyProperties(updateReceiptAppNotice, newExpirationDate, CommonUtils.getNullPropertyNames(updateReceiptAppNotice));
			// eligibiltyDate
			newExpirationDate.setEligibilityDate(updateReceiptAppNotice.getEligibiltyDate());
			
			// Reminder Date
			if(updateReceiptAppNotice.getEligibiltyDate() != null && updateReceiptAppNotice.getReminderDays() != null){
				Date reminderDate = DateUtils.subtractDaysFromDate(updateReceiptAppNotice.getEligibiltyDate(), updateReceiptAppNotice.getReminderDays());
				newExpirationDate.setReminderDate(reminderDate);
			}
			
			newExpirationDate.setReminderDescription(updateReceiptAppNotice.getReceiptNote());
			newExpirationDate.setStatusId(18L);
			
			// LangId, ClassId, ClientId
			MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(matterNumber);
			
			newExpirationDate.setLanguageId(matterGenAcc.getLanguageId());
			newExpirationDate.setClassId(matterGenAcc.getClassId());
			newExpirationDate.setClientId(matterGenAcc.getClientId());		
			newExpirationDate.setDeletionIndicator(0L);
			newExpirationDate.setCreatedBy(loginUserID);
			expirationDateService.createExpirationDate(newExpirationDate, loginUserID);
		}
		
		return updatedReceiptAppNotice;
	}
	
	/**
	 * deleteReceiptAppNotice
	 * @param languageId
	 * @param classId
	 * @param matterNumber
	 * @param receiptNo
	 * @param loginUserID
	 */
	public void deleteReceiptAppNotice (String languageId, Long classId, String matterNumber, String receiptNo, String loginUserID) {
		ReceiptAppNotice receiptAppNotice = getReceiptAppNotice(languageId, classId, matterNumber, receiptNo);
		if ( receiptAppNotice != null) {
			receiptAppNotice.setDeletionIndicator(1L);
			receiptAppNotice.setUpdatedBy(loginUserID);
			receiptAppNoticeRepository.save(receiptAppNotice);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + receiptNo);
		}
	}
}
