package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.docchecklist.AddDocCheckList;
import com.mnrclara.api.setup.model.docchecklist.DocCheckList;
import com.mnrclara.api.setup.model.docchecklist.SearchDocCheckList;
import com.mnrclara.api.setup.model.docchecklist.UpdateDocCheckList;
import com.mnrclara.api.setup.repository.DocCheckListRepository;
import com.mnrclara.api.setup.repository.specification.DocCheckListSpecification;
import com.mnrclara.api.setup.util.CommonUtils;
import com.mnrclara.api.setup.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocCheckListService {
	
	@Autowired
	private DocCheckListRepository docCheckListRepository;
	
	@Autowired
	NumberRangeService numberRangeService;
	
	/**
	 * getDocCheckLists
	 * @return
	 */
	public List<DocCheckList> getDocCheckLists () {
		List<DocCheckList> docCheckListList =  docCheckListRepository.findAll();
		docCheckListList = docCheckListList.stream()
					.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
					.collect(Collectors.toList());
		return docCheckListList;
	}
	
	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param checkListNo
	 * @param caseCategoryId
	 * @param caseSubCategoryId
	 * @param sequenceNo
	 * @return
	 */
	public DocCheckList getDocCheckList (String languageId, Long classId, Long checkListNo, Long caseCategoryId, 
			Long caseSubCategoryId, Long sequenceNo) {
		Optional<DocCheckList> docCheckList = 
				docCheckListRepository.findByLanguageIdAndClassIdAndCheckListNoAndCaseCategoryIdAndCaseSubCategoryIdAndSequenceNoAndDeletionIndicator(
						languageId, classId, checkListNo, caseCategoryId, caseSubCategoryId, sequenceNo, 0L);
		if (docCheckList != null) {
			return docCheckList.get();
		}
		throw new BadRequestException("The given DocCheckList ID : " + checkListNo + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @param sequenceNo
	 * @return
	 */
	public DocCheckList getDocCheckList (Long checkListNo, Long sequenceNo) {
		Optional<DocCheckList> docCheckList = docCheckListRepository.findByCheckListNoAndSequenceNoAndDeletionIndicator(
						checkListNo, sequenceNo, 0L);
		if (docCheckList != null) {
			return docCheckList.get();
		}
		throw new BadRequestException("The given DocCheckList ID : " + checkListNo + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @return
	 */
	public DocCheckList getDocCheckList (Long checkListNo) {
		Optional<DocCheckList> docCheckList = 
				docCheckListRepository.findByCheckListNoAndDeletionIndicator(checkListNo, 0L);
		if (docCheckList != null) {
			return docCheckList.get();
		}
		throw new BadRequestException("The given DocCheckList ID : " + checkListNo + " doesn't exist.");
	}
	
	/**
	 * 
	 * @param searchDocCheckList
	 * @return
	 * @throws ParseException
	 */
	public List<DocCheckList> findDocCheckLists(SearchDocCheckList searchDocCheckList) throws ParseException {
		if (searchDocCheckList.getStartCreatedOn() != null && searchDocCheckList.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchDocCheckList.getStartCreatedOn(), searchDocCheckList.getEndCreatedOn());
			searchDocCheckList.setStartCreatedOn(dates[0]);
			searchDocCheckList.setEndCreatedOn(dates[1]);
		}
		
		DocCheckListSpecification spec = new DocCheckListSpecification(searchDocCheckList);
		List<DocCheckList> searchResults = docCheckListRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}
	
	/**
	 * createDocCheckList
	 * @param newDocCheckList
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<DocCheckList> createDocCheckList (List<AddDocCheckList> newDocCheckList, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<DocCheckList> docCheckLists = new ArrayList <>();
		/*
		 * Number Generation
		 * -----------------------
		 * Pass CLASS_ID = 03, NUM_RAN_CODE = 20 in NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and 
		 * add +1 and then insert in CHK_LIST_NO field
		 */
		long CLASS_ID = 3;
		long NUM_RAN_CODE = 20;
		String checkListNumber = numberRangeService.getNextNumberRange(NUM_RAN_CODE, CLASS_ID);
		Long checkListNo = Long.valueOf(checkListNumber);
		log.info("checkListNumber : " + checkListNumber);
		
		for (AddDocCheckList addDocCheckList : newDocCheckList) {
			DocCheckList dbDocCheckList = new DocCheckList();
			log.info("newDocCheckList : " + newDocCheckList);
			
			BeanUtils.copyProperties(addDocCheckList, dbDocCheckList, CommonUtils.getNullPropertyNames(addDocCheckList));
			dbDocCheckList.setCheckListNo(checkListNo);
			
			dbDocCheckList.setStatusId("ACTIVE");
			dbDocCheckList.setDeletionIndicator(0L);
			dbDocCheckList.setCreatedBy(loginUserID);
			dbDocCheckList.setUpdatedBy(loginUserID);
			dbDocCheckList.setCreatedOn(new Date());
			dbDocCheckList.setUpdatedOn(new Date());
			DocCheckList createdDocCheckList = docCheckListRepository.save(dbDocCheckList);
			log.info("createdDocCheckList : " + createdDocCheckList);
			docCheckLists.add(createdDocCheckList);
		}
		return docCheckLists;
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @param updateDocCheckList
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<DocCheckList> updateDocCheckList (List<UpdateDocCheckList> updateDocCheckLists, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<AddDocCheckList> addDocCheckLists = new ArrayList<>();
		for (UpdateDocCheckList updateDocCheckList : updateDocCheckLists) {
			deleteDocCheckList (updateDocCheckList.getCheckListNo(), updateDocCheckList.getSequenceNo());
			AddDocCheckList newDocCheckList = new AddDocCheckList();
			BeanUtils.copyProperties(updateDocCheckList, newDocCheckList, CommonUtils.getNullPropertyNames(updateDocCheckList));
			addDocCheckLists.add(newDocCheckList);
		}
		return createDocCheckList (addDocCheckLists, loginUserID);
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @param loginUserID
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void deleteDocCheckList (String languageId, Long classId, Long checkListNo, Long caseCategoryId, 
			Long caseSubCategoryId, Long sequenceNo, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		DocCheckList dbDocCheckList = getDocCheckList (languageId, classId, checkListNo, caseCategoryId, 
				caseSubCategoryId, sequenceNo);
		dbDocCheckList.setDeletionIndicator(1L);
		dbDocCheckList.setUpdatedBy(loginUserID);
		dbDocCheckList.setUpdatedOn(new Date());
		docCheckListRepository.save(dbDocCheckList);
		log.info("Record got deleted");
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @param sequenceNo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void deleteDocCheckList (Long checkListNo, Long sequenceNo) 
			throws IllegalAccessException, InvocationTargetException {
		if (sequenceNo != null) {
			DocCheckList dbDocCheckList = getDocCheckList (checkListNo, sequenceNo);
			docCheckListRepository.delete(dbDocCheckList);
			log.info("Record got deleted");
		} else {
			deleteDocCheckList (checkListNo);
		}
	}
	
	/**
	 * 
	 * @param checkListNo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void deleteDocCheckList (Long checkListNo) 
			throws IllegalAccessException, InvocationTargetException {
		DocCheckList dbDocCheckList = getDocCheckList (checkListNo);
		docCheckListRepository.delete(dbDocCheckList);
		log.info("Record got deleted");
	}
}
