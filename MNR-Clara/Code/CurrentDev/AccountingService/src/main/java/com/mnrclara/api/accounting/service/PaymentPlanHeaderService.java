package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.auth.AuthToken;
import com.mnrclara.api.accounting.model.management.MatterGenAcc;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeader;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeaderEntity;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanLine;
import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanLineEntity;
import com.mnrclara.api.accounting.model.paymentplan.SearchPaymentPlanHeader;
import com.mnrclara.api.accounting.repository.PaymentPlanHeaderRepository;
import com.mnrclara.api.accounting.repository.PaymentPlanLineRepository;
import com.mnrclara.api.accounting.repository.specification.PaymentPlanHeaderSpecification;
import com.mnrclara.api.accounting.util.CommonUtils;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentPlanHeaderService {
	
	@Autowired
	private PaymentPlanHeaderRepository paymentPlanHeaderRepository;
	
	@Autowired
	private PaymentPlanLineRepository paymentPlanLineRepository;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	PaymentPlanLineService paymentPlanLineService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * getPaymentPlanHeaders
	 * @return
	 */
	public List<PaymentPlanHeader> getPaymentPlanHeaders () {
		List<PaymentPlanHeaderEntity> paymentPlanHeaderEntityList =  paymentPlanHeaderRepository.findAll();
		paymentPlanHeaderEntityList = 
				paymentPlanHeaderEntityList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		
		List<PaymentPlanHeader> responsePaymentPlanHeaderList = new ArrayList<>();
		for (PaymentPlanHeaderEntity paymentPlanHeaderEntity : paymentPlanHeaderEntityList) {
			PaymentPlanHeader responsePaymentPlanHeader = copyHeaderEntityToBean(paymentPlanHeaderEntity);
			
			// String paymentPlanNumber, Long paymentPlanRevisionNo
			List<PaymentPlanLineEntity> dbPaymentPlanLineEntityList = paymentPlanLineService.getPaymentPlanLine(responsePaymentPlanHeader.getPaymentPlanNumber(),
							responsePaymentPlanHeader.getPaymentPlanRevisionNo());
			if (dbPaymentPlanLineEntityList != null) {
				List<PaymentPlanLine> listPaymentPlanLine = createBeanListAsLine (dbPaymentPlanLineEntityList);
				responsePaymentPlanHeader.setPaymentPlanLines(listPaymentPlanLine);
			}
			
			responsePaymentPlanHeaderList.add(responsePaymentPlanHeader);
		}
		return responsePaymentPlanHeaderList;
	}
	
	/**
	 * getAllPaymentPlanHeaders
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	public Page<PaymentPlanHeader> getAllPaymentPlanHeaders (Integer pageNo, Integer pageSize, String sortBy) {
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
			Page<PaymentPlanHeaderEntity> pagedResult = paymentPlanHeaderRepository.findAll(paging);
			List<PaymentPlanHeaderEntity> paymentPlanHeaderEntityList = 
					pagedResult.stream()
								.filter(n -> n.getDeletionIndicator() != null && 
											n.getDeletionIndicator() == 0)
								.collect(Collectors.toList());
			
			List<PaymentPlanHeader> responsePaymentPlanHeaderList = new ArrayList<>();
			for (PaymentPlanHeaderEntity paymentPlanHeaderEntity : paymentPlanHeaderEntityList) {
				PaymentPlanHeader responsePaymentPlanHeader = copyHeaderEntityToBean(paymentPlanHeaderEntity);
				
				// String paymentPlanNumber, Long paymentPlanRevisionNo
				List<PaymentPlanLineEntity> dbPaymentPlanLineEntityList = paymentPlanLineService.getPaymentPlanLine(responsePaymentPlanHeader.getPaymentPlanNumber(),
								responsePaymentPlanHeader.getPaymentPlanRevisionNo());
				if (dbPaymentPlanLineEntityList != null) {
					List<PaymentPlanLine> listPaymentPlanLine = createBeanListAsLine (dbPaymentPlanLineEntityList);
					responsePaymentPlanHeader.setPaymentPlanLines(listPaymentPlanLine);
				}
				responsePaymentPlanHeaderList.add(responsePaymentPlanHeader);
			}
			Page<PaymentPlanHeader> page = new PageImpl<PaymentPlanHeader> (responsePaymentPlanHeaderList);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @return
	 */
	public PaymentPlanHeader getPaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo) {
		Optional<PaymentPlanHeaderEntity> paymentPlanHeaderEntity = 
				paymentPlanHeaderRepository.findByPaymentPlanNumberAndPaymentPlanRevisionNoAndDeletionIndicator(
						paymentPlanNumber, paymentPlanRevisionNo, 0L);
		if (paymentPlanHeaderEntity.isEmpty()) {
			throw new BadRequestException("The given PaymentPlanHeader ID : " + 
					paymentPlanNumber + " and paymentPlanRevisionNo:" + 
					paymentPlanRevisionNo + " doesn't exist.");
		}
		
		PaymentPlanHeader responsePaymentPlanHeader = copyHeaderEntityToBean(paymentPlanHeaderEntity.get());
		List<PaymentPlanLineEntity> dbPaymentPlanLineEntityList = 
				paymentPlanLineService.getPaymentPlanLine(responsePaymentPlanHeader.getPaymentPlanNumber(), 
						responsePaymentPlanHeader.getPaymentPlanRevisionNo());
		if (dbPaymentPlanLineEntityList != null) {
			List<PaymentPlanLine> listPaymentPlanLine = createBeanListAsLine (dbPaymentPlanLineEntityList);
			responsePaymentPlanHeader.setPaymentPlanLines(listPaymentPlanLine);
		}
		return responsePaymentPlanHeader;
	}
	
	/**
	 * 
	 * @param searchPaymentPlanHeader
	 * @return
	 * @throws ParseException
	 */
	public List<PaymentPlanHeader> findPaymentPlanHeader(SearchPaymentPlanHeader searchPaymentPlanHeader) throws ParseException {
		if (searchPaymentPlanHeader.getStartPaymentPlanDate() != null && searchPaymentPlanHeader.getEndPaymentPlanDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPaymentPlanHeader.getStartPaymentPlanDate(), 
							searchPaymentPlanHeader.getEndPaymentPlanDate());
			searchPaymentPlanHeader.setStartPaymentPlanDate(dates[1]);
			searchPaymentPlanHeader.setEndPaymentPlanDate(dates[2]);
		}
		
		if (searchPaymentPlanHeader.getStartCreatedOn() != null && searchPaymentPlanHeader.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPaymentPlanHeader.getStartCreatedOn(), 
							searchPaymentPlanHeader.getEndCreatedOn());
			searchPaymentPlanHeader.setStartCreatedOn(dates[1]);
			searchPaymentPlanHeader.setEndCreatedOn(dates[2]);
		}
		
		PaymentPlanHeaderSpecification spec = new PaymentPlanHeaderSpecification(searchPaymentPlanHeader);
		List<PaymentPlanHeaderEntity> results = paymentPlanHeaderRepository.findAll(spec);
		log.info("results: " + results);
		
		List<PaymentPlanHeader> listPaymentPlanHeader = createBeanList(results);
		return listPaymentPlanHeader;
	}
	
	/**
	 * createPaymentPlanHeader
	 * @param newPaymentPlanHeader
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public PaymentPlanHeader createPaymentPlanHeader (PaymentPlanHeader newPaymentPlanHeader, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		Optional<PaymentPlanHeaderEntity> paymentPlanHeaderEntity = 
				paymentPlanHeaderRepository.findByPaymentPlanNumberAndPaymentPlanRevisionNoAndDeletionIndicator(
						newPaymentPlanHeader.getPaymentPlanNumber(),
						newPaymentPlanHeader.getPaymentPlanRevisionNo(),
						0L);
		if (!paymentPlanHeaderEntity.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		
		PaymentPlanHeaderEntity dbPaymentPlanHeaderEntity = new PaymentPlanHeaderEntity();
		log.info("newPaymentPlanHeader : " + newPaymentPlanHeader);
		BeanUtils.copyProperties(newPaymentPlanHeader, dbPaymentPlanHeaderEntity, CommonUtils.getNullPropertyNames(newPaymentPlanHeader));
		
		dbPaymentPlanHeaderEntity.setPaymentPlanDate(DateUtils.convertStringToDate (newPaymentPlanHeader.getPaymentPlanDate()));
		dbPaymentPlanHeaderEntity.setPaymentPlanStartDate(DateUtils.convertStringToDate (newPaymentPlanHeader.getPaymentPlanStartDate()));
		
		// During save derive Lang_id value from MatterGenAcc table for selected matter_No
		// Get AuthToken for ManagementService
		AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
		MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(newPaymentPlanHeader.getMatterNumber(), authTokenForManagementService.getAccess_token());
					
		/*
		 * During Save, Pass CLASS_ID=02, NUM_RAN_CODE=16 in NUMBERRANGE table and 
		 * Fetch NUM_RAN_CURRENT values and add +1 and then insert
		 */
		dbPaymentPlanHeaderEntity.setPaymentPlanNumber(getNextPaymentPlanNo());
		
		// Plan_Rev_No - Hard coded value 1 for first time creating the quote
		dbPaymentPlanHeaderEntity.setPaymentPlanRevisionNo(1L);
		dbPaymentPlanHeaderEntity.setLanguageId(matterGenAcc.getLanguageId());
		dbPaymentPlanHeaderEntity.setClassId(matterGenAcc.getClassId());
		dbPaymentPlanHeaderEntity.setStatusId(1L);
		dbPaymentPlanHeaderEntity.setDeletionIndicator(0L);
		dbPaymentPlanHeaderEntity.setCreatedBy(loginUserID);
		dbPaymentPlanHeaderEntity.setUpdatedBy(loginUserID);
		dbPaymentPlanHeaderEntity.setCreatedOn(new Date());
		dbPaymentPlanHeaderEntity.setUpdatedOn(new Date());
		
		PaymentPlanHeaderEntity createdPaymentPlanHeaderEntity = paymentPlanHeaderRepository.save(dbPaymentPlanHeaderEntity);
		log.info("createdPaymentPlanHeader : " + createdPaymentPlanHeaderEntity);
		PaymentPlanHeader responsePaymentPlanHeader = copyHeaderEntityToBean(createdPaymentPlanHeaderEntity);
		
		// Insert Record in PaymentPlanLine
		List<PaymentPlanLine> responsePaymentPlanLines = new ArrayList<>();
		List<PaymentPlanLine> newPaymentPlanLines = newPaymentPlanHeader.getPaymentPlanLines();
		for (PaymentPlanLine newPaymentPlanLine : newPaymentPlanLines) {
			PaymentPlanLineEntity newPaymentPlanLineEntity = copyBeanToLineEntity(newPaymentPlanLine);
			
			newPaymentPlanLineEntity.setDueDate(DateUtils.convertStringToDate (newPaymentPlanLine.getDueDate()));
			newPaymentPlanLineEntity.setReminderDate(DateUtils.convertStringToDate (newPaymentPlanLine.getReminderDate()));
			
			newPaymentPlanLineEntity.setPaymentPlanNumber(createdPaymentPlanHeaderEntity.getPaymentPlanNumber());
			newPaymentPlanLineEntity.setPaymentPlanRevisionNo(createdPaymentPlanHeaderEntity.getPaymentPlanRevisionNo());
			newPaymentPlanLineEntity.setLanguageId(createdPaymentPlanHeaderEntity.getLanguageId());
			newPaymentPlanLineEntity.setClassId(createdPaymentPlanHeaderEntity.getClassId());
			newPaymentPlanLineEntity.setMatterNumber(createdPaymentPlanHeaderEntity.getMatterNumber());
			newPaymentPlanLineEntity.setClientId(createdPaymentPlanHeaderEntity.getClientId());
			newPaymentPlanLineEntity.setQuotationNo(createdPaymentPlanHeaderEntity.getQuotationNo());
			newPaymentPlanLineEntity.setStatusId(1L);
			
			// Rest of the params will be sent from UI
			PaymentPlanLineEntity createdAddPaymentPlanLineEntity = paymentPlanLineService.createPaymentPlanLine(newPaymentPlanLineEntity, loginUserID);
			log.info("createdAddPaymentPlanLine : " + createdAddPaymentPlanLineEntity);
			
			/*
			 * Return response
			 */
			PaymentPlanLine responsePaymentPlanLine = copyLineEntityToBean(createdAddPaymentPlanLineEntity);
			responsePaymentPlanLines.add(responsePaymentPlanLine);
		}
		
		// Copying to Response object
		responsePaymentPlanHeader.setPaymentPlanLines(responsePaymentPlanLines);
		return responsePaymentPlanHeader;
	}
	
	/**
	 * During Save, Pass CLASS_ID=02, NUM_RAN_CODE=16 in NUMBERRANGE table and 
	 * Fetch NUM_RAN_CURRENT values and add +1 and then insert
	 * @return
	 */
	private String getNextPaymentPlanNo () {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
				
		Long classID = 2L;
		Long NumberRangeCode = 16L;
		String newPaymentPlanNo = setupService.getNextNumberRange(classID, NumberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newPaymentPlanNo);
		return newPaymentPlanNo;
	}
	
	/**
	 * updatePaymentPlanHeader
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @param loginUserID
	 * @param updatePaymentPlanHeader
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public PaymentPlanHeader updatePaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo,
			PaymentPlanHeader updatePaymentPlanHeader, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		PaymentPlanHeader addPaymentPlanHeader = getPaymentPlanHeader (paymentPlanNumber, paymentPlanRevisionNo);
		BeanUtils.copyProperties(updatePaymentPlanHeader, addPaymentPlanHeader, CommonUtils.getNullPropertyNames(updatePaymentPlanHeader));
		
		PaymentPlanHeaderEntity dbPaymentPlanHeader = new PaymentPlanHeaderEntity();
		BeanUtils.copyProperties(addPaymentPlanHeader, dbPaymentPlanHeader, CommonUtils.getNullPropertyNames(addPaymentPlanHeader));
		
		Date datePaymentPlanStartDate = DateUtils.convertStringToDate(updatePaymentPlanHeader.getPaymentPlanStartDate());
		Date datePaymentPlanDate = DateUtils.convertStringToDate(updatePaymentPlanHeader.getPaymentPlanDate());
		
		dbPaymentPlanHeader.setPaymentPlanDate(datePaymentPlanDate);
		dbPaymentPlanHeader.setPaymentPlanStartDate(datePaymentPlanStartDate);
		dbPaymentPlanHeader.setDeletionIndicator(0L);
		dbPaymentPlanHeader.setUpdatedBy(loginUserID);
		dbPaymentPlanHeader.setUpdatedOn(new Date());
		PaymentPlanHeaderEntity paymentPlanHeader = paymentPlanHeaderRepository.save(dbPaymentPlanHeader);
		log.info("updatedPaymentPlanHeader : " + paymentPlanHeader);
		
		PaymentPlanHeader updatedPaymentPlanHeader = new PaymentPlanHeader();
		BeanUtils.copyProperties(paymentPlanHeader, updatedPaymentPlanHeader, CommonUtils.getNullPropertyNames(paymentPlanHeader));
		
		// Insert Record in PaymentPlanLine
		List<PaymentPlanLine> newPaymentPlanLines = updatePaymentPlanHeader.getPaymentPlanLines();
		List<PaymentPlanLine> responsePaymentPlanLines = new ArrayList<>();
		if (newPaymentPlanLines != null) {
			for (PaymentPlanLine newPaymentPlanLine : newPaymentPlanLines) {
				PaymentPlanLineEntity newPaymentPlanLineEntity = copyBeanToLineEntity(newPaymentPlanLine);
				
				newPaymentPlanLineEntity.setPaymentPlanNumber(paymentPlanHeader.getPaymentPlanNumber());
				newPaymentPlanLineEntity.setPaymentPlanRevisionNo(paymentPlanHeader.getPaymentPlanRevisionNo());
				newPaymentPlanLineEntity.setLanguageId(paymentPlanHeader.getLanguageId());
				newPaymentPlanLineEntity.setClassId(paymentPlanHeader.getClassId());
				newPaymentPlanLineEntity.setMatterNumber(paymentPlanHeader.getMatterNumber());
				newPaymentPlanLineEntity.setClientId(paymentPlanHeader.getClientId());
				newPaymentPlanLineEntity.setQuotationNo(paymentPlanHeader.getQuotationNo());
//				private Long paymentReminderDays;	
//				private Date reminderDate;
				
				// Rest of the params will be sent from UI
				// paymentPlanNumber, paymentPlanRevisionNo, itemNumber, updatePaymentPlanLine, loginUserID
				PaymentPlanLineEntity paymentPlanLineEntity = 
						paymentPlanLineService.updatePaymentPlanLine(paymentPlanHeader.getPaymentPlanNumber(),
								paymentPlanHeader.getPaymentPlanRevisionNo(), newPaymentPlanLine.getItemNumber(), 
								newPaymentPlanLineEntity, loginUserID);
				log.info("paymentPlanLineEntity : " + paymentPlanLineEntity);
				
				PaymentPlanLine updatedPaymentPlanLine = copyLineEntityToBean(newPaymentPlanLineEntity);
				responsePaymentPlanLines.add(updatedPaymentPlanLine);
			}
		}
		
		updatedPaymentPlanHeader.setPaymentPlanLines(responsePaymentPlanLines);
		return updatedPaymentPlanHeader;
	}
	
	/**
	 * deletePaymentPlanHeader
	 * @param paymentPlanNumber
	 * @param paymentPlanRevisionNo
	 * @param loginUserID
	 */
	public void deletePaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo, String loginUserID) {
		PaymentPlanHeader paymentPlanHeader = getPaymentPlanHeader (paymentPlanNumber, paymentPlanRevisionNo);
		if (paymentPlanHeader != null) {
			// Line Delete
			List<PaymentPlanLine> dbPaymentPlanLines = paymentPlanHeader.getPaymentPlanLines();
			for (PaymentPlanLine PaymentPlanLine : dbPaymentPlanLines) {
				PaymentPlanLineEntity dbPaymentPlanLineEntity = copyBeanToLineEntity(PaymentPlanLine);
				dbPaymentPlanLineEntity.setDeletionIndicator(1L);
				dbPaymentPlanLineEntity.setUpdatedBy(loginUserID);
				paymentPlanLineRepository.save(dbPaymentPlanLineEntity);
			}
			
			// Header Delete
			PaymentPlanHeaderEntity dbPaymentPlanHeaderEntity = copyBeanToHeaderEntity(paymentPlanHeader);
			dbPaymentPlanHeaderEntity.setDeletionIndicator(1L);
			dbPaymentPlanHeaderEntity.setUpdatedBy(loginUserID);
			paymentPlanHeaderRepository.save(dbPaymentPlanHeaderEntity);
		}
	}

	/**
	 * 
	 * @param paymentPlanHeaderEntity
	 * @return
	 */
	private PaymentPlanHeader copyHeaderEntityToBean(PaymentPlanHeaderEntity paymentPlanHeaderEntity) {
		PaymentPlanHeader paymentPlanHeader = new PaymentPlanHeader();
		BeanUtils.copyProperties(paymentPlanHeaderEntity, paymentPlanHeader, CommonUtils.getNullPropertyNames(paymentPlanHeaderEntity));
		
		paymentPlanHeader.setPaymentPlanDate(DateUtils.dateToString (paymentPlanHeaderEntity.getPaymentPlanDate()));
		paymentPlanHeader.setPaymentPlanStartDate(DateUtils.dateToString (paymentPlanHeaderEntity.getPaymentPlanStartDate()));
		return paymentPlanHeader;
	}
	
	/**
	 * 
	 * @param paymentPlanLineEntity
	 * @return
	 */
	private PaymentPlanLine copyLineEntityToBean(PaymentPlanLineEntity paymentPlanLineEntity) {
		PaymentPlanLine paymentPlanLine = new PaymentPlanLine();
		BeanUtils.copyProperties(paymentPlanLineEntity, paymentPlanLine, CommonUtils.getNullPropertyNames(paymentPlanLineEntity));
		paymentPlanLine.setDueDate(DateUtils.dateToString (paymentPlanLineEntity.getDueDate()));
		paymentPlanLine.setReminderDate(DateUtils.dateToString (paymentPlanLineEntity.getReminderDate()));
		return paymentPlanLine;
	}
	
	/**
	 * 
	 * @param paymentPlanHeader
	 * @return
	 */
	private PaymentPlanHeaderEntity copyBeanToHeaderEntity(PaymentPlanHeader paymentPlanHeader) {
		PaymentPlanHeaderEntity paymentPlanHeaderEntity = new PaymentPlanHeaderEntity();
		BeanUtils.copyProperties(paymentPlanHeader, paymentPlanHeaderEntity, CommonUtils.getNullPropertyNames(paymentPlanHeader));
		return paymentPlanHeaderEntity;
	}
	
	/**
	 * 
	 * @param paymentPlanLine
	 * @return
	 */
	private PaymentPlanLineEntity copyBeanToLineEntity(PaymentPlanLine paymentPlanLine) {
		PaymentPlanLineEntity paymentPlanLineEntity = new PaymentPlanLineEntity();
		BeanUtils.copyProperties(paymentPlanLine, paymentPlanLineEntity, CommonUtils.getNullPropertyNames(paymentPlanLine));
		return paymentPlanLineEntity;
	}
	
	/**
	 * 
	 * @param paymentPlanHeaderList
	 * @return
	 */
	private List<PaymentPlanHeader> createBeanList(List<PaymentPlanHeaderEntity> paymentPlanHeaderList) {
		List<PaymentPlanHeader> listPaymentPlanHeader = new ArrayList<>();
		for (PaymentPlanHeaderEntity paymentPlanHeaderEntity : paymentPlanHeaderList) {
			PaymentPlanHeader paymentPlanHeader = copyHeaderEntityToBean(paymentPlanHeaderEntity);
			listPaymentPlanHeader.add(paymentPlanHeader);
		}
		return listPaymentPlanHeader;
	}
	
	/**
	 * 
	 * @param paymentPlanLineEntityList
	 * @return
	 */
	private List<PaymentPlanLine> createBeanListAsLine(List<PaymentPlanLineEntity> paymentPlanLineEntityList) {
		List<PaymentPlanLine> listPaymentPlanLine = new ArrayList<>();
		for (PaymentPlanLineEntity paymentPlanLineEntity : paymentPlanLineEntityList) {
			PaymentPlanLine paymentPlanLine = copyLineEntityToBean(paymentPlanLineEntity);
			listPaymentPlanLine.add(paymentPlanLine);
		}
		return listPaymentPlanLine;
	}
}