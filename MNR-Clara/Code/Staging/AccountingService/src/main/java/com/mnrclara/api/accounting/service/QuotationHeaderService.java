package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.auth.AuthToken;
import com.mnrclara.api.accounting.model.management.MatterGenAcc;
import com.mnrclara.api.accounting.model.quotation.QuotationHeader;
import com.mnrclara.api.accounting.model.quotation.QuotationHeaderEntity;
import com.mnrclara.api.accounting.model.quotation.QuotationLine;
import com.mnrclara.api.accounting.model.quotation.QuotationLineEntity;
import com.mnrclara.api.accounting.model.quotation.SearchQuotationHeader;
import com.mnrclara.api.accounting.repository.QuotationHeaderRepository;
import com.mnrclara.api.accounting.repository.QuotationLineRepository;
import com.mnrclara.api.accounting.repository.specification.QuotationHeaderSpecification;
import com.mnrclara.api.accounting.util.CommonUtils;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuotationHeaderService {
	
	@Autowired
	private QuotationHeaderRepository quotationHeaderRepository;
	
	@Autowired
	private QuotationLineRepository quotationLineRepository;
	
	@Autowired
	private QuotationLineService quotationLineService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * getQuotationHeaders
	 * @return
	 */
	public List<QuotationHeader> getQuotationHeaders () {
		List<QuotationHeaderEntity> quotationHeaderEntityList =  quotationHeaderRepository.findAll();
		quotationHeaderEntityList = 
				quotationHeaderEntityList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		
		List<QuotationHeader> responseQuotationHeaderList = new ArrayList<>();
		for (QuotationHeaderEntity quotationHeaderEntity : quotationHeaderEntityList) {
			QuotationHeader responseQuotationHeader = copyHeaderEntityToBean(quotationHeaderEntity);
			
			List<QuotationLineEntity> dbQuotationLineEntityList = 
					quotationLineService.getQuotationLine(responseQuotationHeader.getQuotationNo(), responseQuotationHeader.getQuotationRevisionNo());
			if (dbQuotationLineEntityList != null) {
				List<QuotationLine> listQuotationLine = createBeanListAsLine (dbQuotationLineEntityList);
				responseQuotationHeader.setQuotationLine(listQuotationLine);
			}
			
			responseQuotationHeaderList.add(responseQuotationHeader);
		}
		return responseQuotationHeaderList;
	}
	
	/**
	 * getAllMatterGenerals
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	public Page<QuotationHeaderEntity> getAllQuotationHeaders (Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<QuotationHeaderEntity> pagedResult = quotationHeaderRepository.findAll(paging);
		return pagedResult;
		
//		List<QuotationHeaderEntity> quotationHeaderEntityList = 
//				pagedResult.stream()
//						.filter(n -> n.getDeletionIndicator() != null && 
//							n.getDeletionIndicator() == 0).collect(Collectors.toList());
//		
//		List<QuotationHeader> responseQuotationHeaderList = new ArrayList<>();
//		for (QuotationHeaderEntity quotationHeaderEntity : quotationHeaderEntityList) {
//			QuotationHeader responseQuotationHeader = copyHeaderEntityToBean(quotationHeaderEntity);
//			
//			List<QuotationLineEntity> dbQuotationLineEntityList = 
//					quotationLineService.getQuotationLine(responseQuotationHeader.getQuotationNo(), responseQuotationHeader.getQuotationRevisionNo());
//			if (dbQuotationLineEntityList != null) {
//				List<QuotationLine> listQuotationLine = createBeanListAsLine (dbQuotationLineEntityList);
//				responseQuotationHeader.setQuotationLine(listQuotationLine);
//			}
//			responseQuotationHeaderList.add(responseQuotationHeader);
//		}
//		Page<QuotationHeader> page = new PageImpl<QuotationHeader>(responseQuotationHeaderList);
//		return page;
	}
	
	/**
	 * 
	 * @param quotationHeaderEntity
	 * @return
	 */
	private QuotationHeader copyHeaderEntityToBean(QuotationHeaderEntity quotationHeaderEntity) {
		QuotationHeader quotationHeader = new QuotationHeader();
		BeanUtils.copyProperties(quotationHeaderEntity, quotationHeader, CommonUtils.getNullPropertyNames(quotationHeaderEntity));
		return quotationHeader;
	}
	
	private QuotationLine copyLineEntityToBean(QuotationLineEntity quotationLineEntity) {
		QuotationLine quotationLine = new QuotationLine();
		BeanUtils.copyProperties(quotationLineEntity, quotationLine, CommonUtils.getNullPropertyNames(quotationLineEntity));
		return quotationLine;
	}
	
	private QuotationHeaderEntity copyBeanToHeaderEntity(QuotationHeader quotationHeader) {
		QuotationHeaderEntity quotationHeaderEntity = new QuotationHeaderEntity();
		BeanUtils.copyProperties(quotationHeader, quotationHeaderEntity, CommonUtils.getNullPropertyNames(quotationHeader));
		return quotationHeaderEntity;
	}
	
	private QuotationLineEntity copyBeanToLineEntity(QuotationLine quotationLine) {
		QuotationLineEntity quotationLineEntity = new QuotationLineEntity();
		BeanUtils.copyProperties(quotationLine, quotationLineEntity, CommonUtils.getNullPropertyNames(quotationLine));
		return quotationLineEntity;
	}
	
	/**
	 * 
	 * @param quotationHeaderList
	 * @return
	 */
	private List<QuotationHeader> createBeanList(List<QuotationHeaderEntity> quotationHeaderList) {
		List<QuotationHeader> listQuotationHeader = new ArrayList<>();
		for (QuotationHeaderEntity quotationHeaderEntity : quotationHeaderList) {
			QuotationHeader quotationHeader = copyHeaderEntityToBean(quotationHeaderEntity);
			listQuotationHeader.add(quotationHeader);
		}
		return listQuotationHeader;
	}
	
	/**
	 * 
	 * @param quotationLineEntityList
	 * @return
	 */
	private List<QuotationLine> createBeanListAsLine(List<QuotationLineEntity> quotationLineEntityList) {
		List<QuotationLine> listQuotationLine = new ArrayList<>();
		for (QuotationLineEntity quotationLineEntity : quotationLineEntityList) {
			QuotationLine quotationLine = copyLineEntityToBean(quotationLineEntity);
			listQuotationLine.add(quotationLine);
		}
		return listQuotationLine;
	}
	
	/**
	 * getQuotationHeader
	 * @param quotationNo
	 * @return
	 */
	public QuotationHeader getQuotationHeader (String quotationNo, Long quotationRevisionNo) {
		Optional<QuotationHeaderEntity> optQuotationHeaderEntity = 
				quotationHeaderRepository.findByQuotationNoAndQuotationRevisionNoAndDeletionIndicator(quotationNo, quotationRevisionNo, 0L);
		if (!optQuotationHeaderEntity.isEmpty()) {
			QuotationHeader responseQuotationHeader = copyHeaderEntityToBean(optQuotationHeaderEntity.get());
			List<QuotationLineEntity> dbQuotationLineEntityList = 
					quotationLineService.getQuotationLine(responseQuotationHeader.getQuotationNo(), responseQuotationHeader.getQuotationRevisionNo());
			if (dbQuotationLineEntityList != null) {
				List<QuotationLine> listQuotationLine = createBeanListAsLine (dbQuotationLineEntityList);
				responseQuotationHeader.setQuotationLine(listQuotationLine);
			}
			return responseQuotationHeader;
		} else {
			throw new BadRequestException("The given QuotationHeader ID : " + quotationNo + " doesn't exist.");
		}
	}
	
	/**
	 * getQuotationHeaderByMatterNumber
	 * @param matterNumber
	 * @return
	 */
	public QuotationHeader getQuotationHeaderByMatterNumber (String matterNumber) {
		Optional<QuotationHeaderEntity> optQuotationHeaderEntity = quotationHeaderRepository.findByMatterNumber(matterNumber);
		if (!optQuotationHeaderEntity.isEmpty()) {
			return copyHeaderEntityToBean(optQuotationHeaderEntity.get());
		} else {
			throw new BadRequestException("The given MatterNumber : " + matterNumber + " doesn't exist.");
		}
	}
	
	/**
	 * findQuotationHeader
	 * @param searchQuotationHeader
	 * @return
	 * @throws Exception
	 */
	public List<QuotationHeader> findQuotationHeader(SearchQuotationHeader searchQuotationHeader) throws Exception {
		if (searchQuotationHeader.getStartQuotationDate() != null && searchQuotationHeader.getEndQuotationDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchQuotationHeader.getStartQuotationDate(), 
							searchQuotationHeader.getEndQuotationDate());
			searchQuotationHeader.setStartQuotationDate(dates[0]);
			searchQuotationHeader.setEndQuotationDate(dates[1]);
		}
		
		QuotationHeaderSpecification spec = new QuotationHeaderSpecification(searchQuotationHeader);
		List<QuotationHeaderEntity> results = quotationHeaderRepository.findAll(spec);
		
		List<QuotationHeader> listQuotationHeader = createBeanList(results);
		return listQuotationHeader;
	}
	
	/**
	 * createQuotationHeader
	 * @param newQuotationHeader
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QuotationHeader createQuotationHeader (QuotationHeader newQuotationHeader, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		log.info("newQuotationHeader : " + newQuotationHeader.getQuotationNo() + "," + newQuotationHeader.getQuotationRevisionNo());
		// During save derive Lang_id value from MatterGenAcc table for selected matter_No
		// Get AuthToken for ManagementService
		AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
		MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(newQuotationHeader.getMatterNumber(), authTokenForManagementService.getAccess_token());
				
		QuotationHeaderEntity dbQuotationHeaderEntity = new QuotationHeaderEntity();
		BeanUtils.copyProperties(newQuotationHeader, dbQuotationHeaderEntity, CommonUtils.getNullPropertyNames(newQuotationHeader));
		
		dbQuotationHeaderEntity.setLanguageId(matterGenAcc.getLanguageId());
		dbQuotationHeaderEntity.setClassId(matterGenAcc.getClassId());
		dbQuotationHeaderEntity.setQuotationNo(getNextQuotationNo());
		dbQuotationHeaderEntity.setStatusId(1L); 				// Hard coded value 1 for first time creating the quote
		dbQuotationHeaderEntity.setQuotationRevisionNo(1L);
		dbQuotationHeaderEntity.setDeletionIndicator(0L);
		dbQuotationHeaderEntity.setCreatedBy(loginUserID);
		dbQuotationHeaderEntity.setUpdatedBy(loginUserID);
		dbQuotationHeaderEntity.setCreatedOn(new Date());
		dbQuotationHeaderEntity.setUpdatedOn(new Date());
		QuotationHeaderEntity createdQuotationHeaderEntity = quotationHeaderRepository.save(dbQuotationHeaderEntity);
		log.info("createdQuotationHeader : " + createdQuotationHeaderEntity);
		log.info("\n--------22222-----> : " + createdQuotationHeaderEntity.getQuotationNo() + "," + 
					createdQuotationHeaderEntity.getQuotationRevisionNo());
		
//		Long maxSerialNumber = quotationLineService.getMaxSerialNumber(createdQuotationHeaderEntity.getQuotationNo(), createdQuotationHeaderEntity.getQuotationRevisionNo());
//		log.info("maxSerialNumber : " + maxSerialNumber);
//		if (maxSerialNumber == null) {
//			maxSerialNumber = 0L;
//		}
//		maxSerialNumber ++;
		
		log.info("-------newQuotationHeader.getAddQuotationLine()-------> : " + newQuotationHeader.getQuotationLine());
		List<QuotationLine> responseQuotationLines = new ArrayList<>();
		
		for (QuotationLine addQuotationLine : newQuotationHeader.getQuotationLine()) {
			QuotationLineEntity newQuotationLineEntity = copyBeanToLineEntity(addQuotationLine);
			
			newQuotationLineEntity.setQuotationNo(dbQuotationHeaderEntity.getQuotationNo());
			newQuotationLineEntity.setQuotationRevisionNo(dbQuotationHeaderEntity.getQuotationRevisionNo());
			
			log.info("-------addQuotationLine.getSerialNumber()-------> : " + addQuotationLine.getSerialNumber());
			newQuotationLineEntity.setSerialNumber(addQuotationLine.getSerialNumber());
			newQuotationLineEntity.setLanguageId(dbQuotationHeaderEntity.getLanguageId());
			newQuotationLineEntity.setClassId(dbQuotationHeaderEntity.getClassId());
			newQuotationLineEntity.setMatterNumber(dbQuotationHeaderEntity.getMatterNumber());
			newQuotationLineEntity.setClientId(dbQuotationHeaderEntity.getClientId());
			
			QuotationLineEntity createdQuotationLineEntity = quotationLineService.createQuotationLine(newQuotationLineEntity, loginUserID);
			log.info("createdQuotationLine : " + createdQuotationLineEntity);
			
			// Copying to Response object
			QuotationLine createdQuotationLine = copyLineEntityToBean(newQuotationLineEntity);
			responseQuotationLines.add(createdQuotationLine);
		}
		
		// Copying to Response object
		QuotationHeader createdQuotationHeader = copyHeaderEntityToBean(createdQuotationHeaderEntity);
		createdQuotationHeader.setQuotationLine(responseQuotationLines);
		return createdQuotationHeader;
	}
	
	/**
	 * During Save, Pass CLASS_ID=02, NUM_RAN_CODE=15 in NUMBERRANGE table and 
	 * Fetch NUM_RAN_CURRENT values and add +1 and then insert
	 * @return
	 */
	private String getNextQuotationNo () {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
				
		Long classID = 2L;
		Long NumberRangeCode = 15L;
		String newQuotationNo = setupService.getNextNumberRange(classID, NumberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newQuotationNo);
		return newQuotationNo;
	}
	
	/**
	 * updateQuotationHeader
	 * @param loginUserId 
	 * @param quotationNo
	 * @param updateQuotationHeader
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public QuotationHeader updateQuotationHeader (String quotationNo, Long quotationRevisionNo,
			QuotationHeader updateQuotationHeader, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		QuotationHeader dbQuotationHeader = getQuotationHeader(quotationNo, quotationRevisionNo);
		QuotationHeaderEntity dbQuotationHeaderEntity = copyBeanToHeaderEntity(dbQuotationHeader);
		log.info("dbQuotationHeaderEntity : " + dbQuotationHeaderEntity);
		log.info("updateQuotationHeader : " + updateQuotationHeader);
		
		//During Save if status_id = 1 then update all the field values in QuotationHeader & QuotationLine tables
		if (updateQuotationHeader.getStatusId() != 2L) {
			BeanUtils.copyProperties(updateQuotationHeader, dbQuotationHeaderEntity, CommonUtils.getNullPropertyNames(updateQuotationHeader));
			dbQuotationHeaderEntity.setUpdatedBy(loginUserID);
			dbQuotationHeaderEntity.setUpdatedOn(new Date());
			QuotationHeaderEntity updatedQuotationHeaderEntity = quotationHeaderRepository.save(dbQuotationHeaderEntity);
			log.info("updatedQuotationHeader : " + updatedQuotationHeaderEntity);
			
			// Update QuotationLine
			List<QuotationLine> responseQuotationLines = new ArrayList<>();
			for (QuotationLine updateQuotationLine : updateQuotationHeader.getQuotationLine()) {
				if (updateQuotationLine != null) {
					QuotationLineEntity quotationLineEntity = copyBeanToLineEntity(updateQuotationLine);
					
					QuotationLineEntity updatedQuotationLineEntity = 
							quotationLineService.updateQuotationLine(quotationNo, quotationRevisionNo, updateQuotationLine.getSerialNumber(), 
									quotationLineEntity, loginUserID);
					QuotationLine updatedQuotationLine = copyLineEntityToBean (updatedQuotationLineEntity);
					responseQuotationLines.add(updatedQuotationLine);
				}
			}
			// Copying to Response object
			QuotationHeader responseQuotationHeader = copyHeaderEntityToBean(updatedQuotationHeaderEntity);
			responseQuotationHeader.setQuotationLine(responseQuotationLines);
			return responseQuotationHeader;
		} 
		
		if (updateQuotationHeader.getStatusId() == 2L) {
			/*
			 * During Save if status_id = 2 then insert new record in QuotationHeader & QuotationLine 
			 * tables by adding +1 to the existing quote revision no field and status_id value as 1
			 */
			QuotationHeaderEntity newQuotationHeaderEntity = new QuotationHeaderEntity();
			BeanUtils.copyProperties(updateQuotationHeader, newQuotationHeaderEntity, CommonUtils.getNullPropertyNames(updateQuotationHeader));
			Long existingQuotRevisionNo = dbQuotationHeader.getQuotationRevisionNo();
			log.info("existingQuotRevisionNo : " + existingQuotRevisionNo);
			existingQuotRevisionNo ++;
			
			newQuotationHeaderEntity.setQuotationNo(updateQuotationHeader.getQuotationNo()); // Maintaining the same QuotationNo
			newQuotationHeaderEntity.setQuotationRevisionNo(existingQuotRevisionNo);
			newQuotationHeaderEntity.setClassId(dbQuotationHeaderEntity.getClassId());
			newQuotationHeaderEntity.setStatusId(1L); 				// Hard coded value 1 for first time creating the quote
			newQuotationHeaderEntity.setDeletionIndicator(0L);
			newQuotationHeaderEntity.setCreatedBy(loginUserID);
			newQuotationHeaderEntity.setUpdatedBy(loginUserID);
			newQuotationHeaderEntity.setCreatedOn(new Date());
			newQuotationHeaderEntity.setUpdatedOn(new Date());
			QuotationHeaderEntity updatedQuotationHeaderEntity = quotationHeaderRepository.save(newQuotationHeaderEntity);
			log.info("updatedQuotationHeaderEntity : " + updatedQuotationHeaderEntity);
			
			List<QuotationLine> createdQuotationLineList = new ArrayList<>();
			for (QuotationLine inputQuotationLine : updateQuotationHeader.getQuotationLine()) {
				QuotationLineEntity newQuotationLineEntity = copyBeanToLineEntity(inputQuotationLine);
			
				// Insert new record in QuationLine table
				newQuotationLineEntity.setQuotationNo(updatedQuotationHeaderEntity.getQuotationNo());
				newQuotationLineEntity.setQuotationRevisionNo(existingQuotRevisionNo);
				newQuotationLineEntity.setSerialNumber(inputQuotationLine.getSerialNumber());
				newQuotationLineEntity.setLanguageId(updatedQuotationHeaderEntity.getLanguageId());
				newQuotationLineEntity.setClassId(updatedQuotationHeaderEntity.getClassId());
				newQuotationLineEntity.setMatterNumber(updatedQuotationHeaderEntity.getMatterNumber());
				newQuotationLineEntity.setClientId(updatedQuotationHeaderEntity.getClientId());
				
				/*
				 * ITEM_NO, Item_Text, Quantity, Rate_HR, Total_Amt
				 */
				newQuotationLineEntity.setItemNumber(inputQuotationLine.getItemNumber());
				newQuotationLineEntity.setItemDescription(inputQuotationLine.getItemDescription());
				newQuotationLineEntity.setQuantity(inputQuotationLine.getQuantity());
				newQuotationLineEntity.setRateperHour(inputQuotationLine.getRateperHour());
				newQuotationLineEntity.setTotalAmount(inputQuotationLine.getTotalAmount());
				
				QuotationLineEntity createdQuotationLineEntity = 
						quotationLineService.createQuotationLine(newQuotationLineEntity, loginUserID);
				log.info("createdQuotationLine : " + createdQuotationLineEntity);
				QuotationLine createdQuotationLine = copyLineEntityToBean(createdQuotationLineEntity);
				createdQuotationLineList.add(createdQuotationLine);
			}
			
			QuotationHeader quotationHeader = copyHeaderEntityToBean(updatedQuotationHeaderEntity);
			quotationHeader.setQuotationLine(createdQuotationLineList);
			return quotationHeader;
		}
		return updateQuotationHeader;
	}
	
	/**
	 * deleteQuotationHeader
	 * @param loginUserID 
	 * @param quotationNo
	 */
	public void deleteQuotationHeader (String quotationNo, Long quotationRevisionNo, String loginUserID) {
		QuotationHeader quotationHeader = getQuotationHeader(quotationNo, quotationRevisionNo);
		QuotationHeaderEntity quotationHeaderEntity = copyBeanToHeaderEntity(quotationHeader);
		if ( quotationHeader != null && quotationHeader.getStatusId() ==  1L) {
			List<QuotationLineEntity> quotationLines = quotationLineService.getQuotationLine(quotationNo, quotationRevisionNo);
			for (QuotationLineEntity quotationLine : quotationLines) {
				quotationLine.setDeletionIndicator(1L);
				quotationLine.setUpdatedBy(loginUserID);
				quotationLineRepository.save(quotationLine);
			}
			
			quotationHeaderEntity.setDeletionIndicator(1L);
			quotationHeaderEntity.setUpdatedBy(loginUserID);
			quotationHeaderRepository.save(quotationHeaderEntity);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + quotationNo + " because Status is not 1.");
		}
	}
}
