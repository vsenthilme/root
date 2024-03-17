package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import com.mnrclara.api.management.model.dto.MatterAssignmentImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.dto.TimekeeperCode;
import com.mnrclara.api.management.model.matterassignment.*;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.matterrate.AddMatterRate;
import com.mnrclara.api.management.model.matterrate.MatterRate;
import com.mnrclara.api.management.repository.MatterAssignmentRepository;
import com.mnrclara.api.management.repository.specification.MatterAssignmentSpecification;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MatterAssignmentService {

	private static final String MATTERASSIGNMENT = "MATTERASSIGNMENT";

	@Autowired
	private MatterAssignmentRepository matterAssignmentRepository;
	
	@Autowired
	private MatterGenAccService matterGenAccService;
	
	@Autowired
	private SetupService setupService;

	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private MatterRateService matterRateService;

	
		/**
	 * getMatterAssignments
	 * 
	 * @return
	 */
	public List<MatterAssignment> getMatterAssignments() {
		List<MatterAssignment> matterAssignmentList = matterAssignmentRepository.findAll();
		matterAssignmentList = matterAssignmentList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterAssignmentList;
	}
	
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	public Page<MatterAssignment> getAllMatterAssignments(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<MatterAssignment> pagedResult = matterAssignmentRepository.findAll(paging);
		return pagedResult;
	}

	/**
	 * getMatterAssignment
	 * 
	 * @param matterNumber
	 * @return
	 */
	public MatterAssignment getMatterAssignment(String matterNumber) {
		MatterAssignment matterAssignment = matterAssignmentRepository.findByMatterNumber(matterNumber).orElse(null);
		if (matterAssignment != null && matterAssignment.getDeletionIndicator() != null && 
				matterAssignment.getDeletionIndicator() == 0) {
			return matterAssignment;
		} else {
			throw new BadRequestException("The given MatterAssignment ID : " + matterNumber + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param searchMatterAssignment
	 * @return
	 * @throws ParseException
	 */
	public List<MatterAssignment> findMatterAssignment(SearchMatterAssignment searchMatterAssignment) throws ParseException {
		MatterAssignmentSpecification spec = new MatterAssignmentSpecification(searchMatterAssignment);
		List<MatterAssignment> results = matterAssignmentRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	//Streaming
	@Transactional
	public List<MatterAssignmentImpl> findMatterAssignmentStream(SearchMatterAssignment searchMatterAssignment) throws ParseException {

		Stream<MatterAssignmentImpl> resultSet = matterAssignmentRepository.getMatterAssignmentListStreaming(
				searchMatterAssignment.getMatterNumber(),
				searchMatterAssignment.getMatterDescription(),
				searchMatterAssignment.getClientId(),
				searchMatterAssignment.getCaseCategoryId(),
				searchMatterAssignment.getCaseSubCategoryId(),
				searchMatterAssignment.getPartner(),
				searchMatterAssignment.getOriginatingTimeKeeper(),
				searchMatterAssignment.getResponsibleTimeKeeper(),
				searchMatterAssignment.getAssignedTimeKeeper(),
				searchMatterAssignment.getLegalAssistant(),
				searchMatterAssignment.getStatusId()
		);

		List<MatterAssignmentImpl> result = resultSet.collect(Collectors.toList());

		return result;
	}

	/**
	 * createMatterAssignment
	 * 
	 * @param newMatterAssignment
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterAssignment createMatterAssignment(AddMatterAssignment newMatterAssignment, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		MatterAssignment dbMatterAssignment = new MatterAssignment();
		BeanUtils.copyProperties(newMatterAssignment, dbMatterAssignment, CommonUtils.getNullPropertyNames(newMatterAssignment));
		
		MatterGenAcc matterGeneral = matterGenAccService.getMatterGenAcc(dbMatterAssignment.getMatterNumber());
		
		// CLIENT_ID
		dbMatterAssignment.setClientId(matterGeneral.getClientId());
		
		// CASEINFO_NO
		dbMatterAssignment.setCaseInformationNo(matterGeneral.getCaseInformationNo());
		
		// LANG_ID
		dbMatterAssignment.setLanguageId(matterGeneral.getLanguageId());
		
		// CLASS_ID
		dbMatterAssignment.setClassId(matterGeneral.getClassId());
		
		// MATTER_TEXT
		dbMatterAssignment.setMatterDescription(newMatterAssignment.getMatterDescription());
		
		// CASE_CATEGORY_ID/CASE_CATEGORY
		dbMatterAssignment.setCaseCategoryId(matterGeneral.getCaseCategoryId());
		
		// CASE_SUB_CATEGORY_ID/CASE_SUB_CATEGORY
		dbMatterAssignment.setCaseSubCategoryId(matterGeneral.getCaseSubCategoryId());
		
		// CASE_OPEN_DATE
		dbMatterAssignment.setCaseOpenedDate(matterGeneral.getCaseOpenedDate());
		
		// PARTNER
		dbMatterAssignment.setPartner(newMatterAssignment.getPartner());
		
		// ORIGINATING_TK
		dbMatterAssignment.setOriginatingTimeKeeper(newMatterAssignment.getOriginatingTimeKeeper());
		
		// RESPONSIBLE_TK
		dbMatterAssignment.setResponsibleTimeKeeper(newMatterAssignment.getResponsibleTimeKeeper());
		
		// LEGAL_ASSIST
		dbMatterAssignment.setLegalAssistant(newMatterAssignment.getLegalAssistant());
		
		// STATUS_ID - Status ID of the Selected MATTER_NO
		dbMatterAssignment.setStatusId(39L);
				
		dbMatterAssignment.setDeletionIndicator(0L);
		dbMatterAssignment.setCreatedBy(loginUserID);
		dbMatterAssignment.setUpdatedBy(loginUserID);
		dbMatterAssignment.setCreatedOn(new Date());
		dbMatterAssignment.setUpdatedOn(new Date());
		dbMatterAssignment = matterAssignmentRepository.save(dbMatterAssignment);
		log.info("dbMatterAssignment : " + dbMatterAssignment);
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
				
		// PARTNER
		if (dbMatterAssignment.getPartner() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getPartner(), authTokenForSetupService.getAccess_token());
			MatterRate partnerMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getPartner(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("partnerMatterRate : " + partnerMatterRate);
		}
		
		// ORIGINATING_TK
		if (dbMatterAssignment.getOriginatingTimeKeeper() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getOriginatingTimeKeeper(), authTokenForSetupService.getAccess_token());
			MatterRate originatingTimeKeeperMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getOriginatingTimeKeeper(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("originatingTimeKeeperMatterRate : " + originatingTimeKeeperMatterRate);
		}
		
		// RESPONSIBLE_TK
		if (dbMatterAssignment.getResponsibleTimeKeeper() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getResponsibleTimeKeeper(), authTokenForSetupService.getAccess_token());
			MatterRate responsibleTimeKeeperMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getResponsibleTimeKeeper(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("responsibleTimeKeeper : " + responsibleTimeKeeperMatterRate);
		}
		
		// ASSIGNED_TK
		if (dbMatterAssignment.getAssignedTimeKeeper() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getAssignedTimeKeeper(), authTokenForSetupService.getAccess_token());
			MatterRate assignedTimeKeeperMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getAssignedTimeKeeper(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("assignedTimeKeeperMatterRate : " + assignedTimeKeeperMatterRate);
		}
		
		// LEGAL_ASSIST
		if (dbMatterAssignment.getLegalAssistant() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getLegalAssistant(), authTokenForSetupService.getAccess_token());
			MatterRate legalAssistantMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getLegalAssistant(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("legalAssistantMatterRate : " + legalAssistantMatterRate);
		}
		
		// LAW_CLERK
		if (dbMatterAssignment.getReferenceField1() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getReferenceField1(), authTokenForSetupService.getAccess_token());
			MatterRate lawClerkMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getReferenceField1(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("lawClerkMatterRate : " + lawClerkMatterRate);
		}
		
		// PARA-LEGAL
		if (dbMatterAssignment.getReferenceField2() != null) {
			// Get TimekeeperCode
			TimekeeperCode timekeeperCode = 
					setupService.getTimekeeperCode(dbMatterAssignment.getReferenceField2(), authTokenForSetupService.getAccess_token());
			MatterRate paraLegalMatterRate = 
					createMatterRate(dbMatterAssignment, dbMatterAssignment.getReferenceField2(), timekeeperCode.getDefaultRate(), loginUserID);
			log.info("paraLegalMatterRate : " + paraLegalMatterRate);
		}
		return dbMatterAssignment;
	}
	
	/**
	 * createBulkMatterAssignments
	 * @param newMatterAssignments
	 * @param loginUserID
	 */
	public void createBulkMatterAssignments(AddMatterAssignment[] newMatterAssignments, String loginUserID) {
		List<MatterAssignment> matterAssignmentCreateList = new ArrayList<>();
		for (AddMatterAssignment newMatterAssignment : newMatterAssignments) {
			MatterAssignment dbMatterAssignment = new MatterAssignment();
			BeanUtils.copyProperties(newMatterAssignment, dbMatterAssignment, CommonUtils.getNullPropertyNames(newMatterAssignment));
			MatterGenAcc matterGeneral = matterGenAccService.getMatterGenAcc(dbMatterAssignment.getMatterNumber());
			
			// CLIENT_ID
			if (matterGeneral.getClientId() != null) {
				dbMatterAssignment.setClientId(matterGeneral.getClientId().trim());
			}
			
			// CASEINFO_NO
			if (matterGeneral.getCaseInformationNo() != null) {
				dbMatterAssignment.setCaseInformationNo(matterGeneral.getCaseInformationNo().trim());
			}
			
			// LANG_ID
			if (matterGeneral.getLanguageId() != null) {
				dbMatterAssignment.setLanguageId(matterGeneral.getLanguageId().trim());
			}
			
			// CLASS_ID
			dbMatterAssignment.setClassId(matterGeneral.getClassId());
			
			// MATTER_TEXT
			if (matterGeneral.getMatterDescription() != null) {
				dbMatterAssignment.setMatterDescription(newMatterAssignment.getMatterDescription().trim());
			}
			
			// CASE_CATEGORY_ID/CASE_CATEGORY
			dbMatterAssignment.setCaseCategoryId(matterGeneral.getCaseCategoryId());
			
			// CASE_SUB_CATEGORY_ID/CASE_SUB_CATEGORY
			dbMatterAssignment.setCaseSubCategoryId(matterGeneral.getCaseSubCategoryId());
			
			// CASE_OPEN_DATE
			if (matterGeneral.getMatterDescription() != null) {
				dbMatterAssignment.setCaseOpenedDate(matterGeneral.getCaseOpenedDate());
			}
			
			// PARTNER
			if (newMatterAssignment.getPartner() != null) {
				dbMatterAssignment.setPartner(newMatterAssignment.getPartner().trim());
			}
			
			// ORIGINATING_TK
			if (newMatterAssignment.getOriginatingTimeKeeper() != null) {
				dbMatterAssignment.setOriginatingTimeKeeper(newMatterAssignment.getOriginatingTimeKeeper().trim());
			}
			
			// RESPONSIBLE_TK
			dbMatterAssignment.setResponsibleTimeKeeper(newMatterAssignment.getResponsibleTimeKeeper());
			
			// LEGAL_ASSIST
			dbMatterAssignment.setLegalAssistant(newMatterAssignment.getLegalAssistant());
			
			// STATUS_ID - Status ID of the Selected MATTER_NO
			dbMatterAssignment.setStatusId(39L);
					
			dbMatterAssignment.setDeletionIndicator(0L);
			matterAssignmentCreateList.add(dbMatterAssignment);
		}
		Iterable<MatterAssignment> createdMatterAssignments = matterAssignmentRepository.saveAll(matterAssignmentCreateList);
		log.info("MatterAssignment created..." + createdMatterAssignments);
	}
	
	/**
	 * createMatterRate
	 * @param dbMatterAssignment
	 * @param defaultRate
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private MatterRate createMatterRate (MatterAssignment dbMatterAssignment, String timekeeperCode, Double defaultRate, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		log.info("-----timekeeperCode : " + timekeeperCode);		
		log.info("-----defaultRate : " + defaultRate);	
		AddMatterRate matterRate = new AddMatterRate();
		matterRate.setMatterNumber(dbMatterAssignment.getMatterNumber()); 			// MATTER_NO
		matterRate.setTimeKeeperCode(timekeeperCode); 								// TK_CODE
		matterRate.setDefaultRatePerHour(defaultRate); 								// DEF_RATE
		matterRate.setAssignedRatePerHour(defaultRate); 							// ASSIGNED_RATE
		matterRate.setClassId(dbMatterAssignment.getClassId());						// CLASS_ID
		matterRate.setLanguageId(dbMatterAssignment.getLanguageId()); 				// LANG_ID
		matterRate.setClientId(dbMatterAssignment.getClientId()); 					// CLIENT_ID
		matterRate.setCaseCategoryId(dbMatterAssignment.getCaseCategoryId()); 		// CASE_CATEGORY_ID
		matterRate.setCaseSubCategoryId(dbMatterAssignment.getCaseSubCategoryId());	// CASE_SUB_CATEGORY_ID
		matterRate.setStatusId(dbMatterAssignment.getStatusId()); 					//STATUS_ID
		MatterRate createdMatterRate = matterRateService.createMatterRate(matterRate, loginUserID);
		log.info("createdMatterRate : " + createdMatterRate);
		return createdMatterRate;
	}

	/**
	 * updateMatterAssignment
	 * 
	 * @param matterNumber
	 * @param updateMatterAssignment
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterAssignment updateMatterAssignment(String matterNumber, UpdateMatterAssignment updateMatterAssignment,
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		MatterAssignment dbMatterAssignment = getMatterAssignment(matterNumber);
		BeanUtils.copyProperties(updateMatterAssignment, dbMatterAssignment, CommonUtils.getNullPropertyNames(updateMatterAssignment));
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// PARTNER
		if (updateMatterAssignment.getPartner() != null
				&& !updateMatterAssignment.getPartner().equalsIgnoreCase(dbMatterAssignment.getPartner())) {
			log.info("Inserting Audit log for PARTNER");
			setupService.createAuditLogRecord(loginUserID, matterNumber, 3L, MATTERASSIGNMENT,
					"PARTNER", dbMatterAssignment.getPartner(),updateMatterAssignment.getPartner(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// ORIGINATING_TK
		if (updateMatterAssignment.getOriginatingTimeKeeper() != null
				&& !updateMatterAssignment.getOriginatingTimeKeeper().equalsIgnoreCase(dbMatterAssignment.getOriginatingTimeKeeper())) {
			log.info("Inserting Audit log for ORIGINATING_TK");
			setupService.createAuditLogRecord(loginUserID, matterNumber, 3L, MATTERASSIGNMENT,
					"ORIGINATING_TK", dbMatterAssignment.getOriginatingTimeKeeper(),updateMatterAssignment.getOriginatingTimeKeeper(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// RESPONSIBLE_TK
		if (updateMatterAssignment.getResponsibleTimeKeeper() != null
				&& !updateMatterAssignment.getResponsibleTimeKeeper().equalsIgnoreCase(dbMatterAssignment.getResponsibleTimeKeeper())) {
			log.info("Inserting Audit log for RESPONSIBLE_TK");
			setupService.createAuditLogRecord(loginUserID, matterNumber, 3L, MATTERASSIGNMENT,
					"RESPONSIBLE_TK", dbMatterAssignment.getResponsibleTimeKeeper(),updateMatterAssignment.getResponsibleTimeKeeper(), 
					authTokenForSetupService.getAccess_token());
		}
			
		// LEGAL_ASSIST
		if (updateMatterAssignment.getLegalAssistant() != null
				&& !updateMatterAssignment.getLegalAssistant().equalsIgnoreCase(dbMatterAssignment.getLegalAssistant())) {
			log.info("Inserting Audit log for LEGAL_ASSIST");
			setupService.createAuditLogRecord(loginUserID, matterNumber, 3L, MATTERASSIGNMENT,
					"LEGAL_ASSIST", dbMatterAssignment.getLegalAssistant(),updateMatterAssignment.getLegalAssistant(), 
					authTokenForSetupService.getAccess_token());
		}
				
		dbMatterAssignment.setUpdatedBy(loginUserID);
		dbMatterAssignment.setUpdatedOn(new Date());
		return matterAssignmentRepository.save(dbMatterAssignment);
	}

	/**
	 * deleteMatterAssignment
	 * 
	 * @param matterNumber
	 */
	public void deleteMatterAssignment(String matterNumber) {
		MatterAssignment matterassignment = getMatterAssignment(matterNumber);
		if (matterassignment != null) {
			matterassignment.setDeletionIndicator(1L);
			matterAssignmentRepository.save(matterassignment);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterNumber);
		}
	}
}
