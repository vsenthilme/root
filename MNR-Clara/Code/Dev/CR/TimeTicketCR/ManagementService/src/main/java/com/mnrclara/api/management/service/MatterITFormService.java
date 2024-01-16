package com.mnrclara.api.management.service;

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

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.matteritform.AddMatterITForm;
import com.mnrclara.api.management.model.matteritform.ITForm007;
import com.mnrclara.api.management.model.matteritform.ITForm008;
import com.mnrclara.api.management.model.matteritform.ITForm009;
import com.mnrclara.api.management.model.matteritform.ITForm010;
import com.mnrclara.api.management.model.matteritform.ITForm011;
import com.mnrclara.api.management.model.matteritform.MatterITForm;
import com.mnrclara.api.management.model.matteritform.SearchMatterITForm;
import com.mnrclara.api.management.model.matteritform.UpdateMatterITForm;
import com.mnrclara.api.management.repository.MatterITFormRepository;
import com.mnrclara.api.management.repository.specification.MatterITFormSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterITFormService {
	
	@Autowired
	private MatterITFormRepository matterITFormRepository;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	MongoService mongoService;
	
	@Autowired
	MatterGenAccService matterGenAccService;
	
	/**
	 * getMatterITForms
	 * @return
	 */
	public List<MatterITForm> getMatterITForms () {
		List<MatterITForm> MatterITFormList =  matterITFormRepository.findAll();
		MatterITFormList = MatterITFormList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return MatterITFormList;
	}
	
	/**
	 * getMatterITForm
	 * @param intakeFormId
	 * @return
	 */
	public MatterITForm getMatterITForm (String intakeFormNumber) {
		MatterITForm MatterITForm = matterITFormRepository.findByIntakeFormNumber(intakeFormNumber).orElse(null);
		if (MatterITForm != null && MatterITForm.getDeletionIndicator() != null && MatterITForm.getDeletionIndicator() == 0) {
			return MatterITForm;
		} else {
			throw new BadRequestException("The given MatterITForm ID : " + intakeFormNumber + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param matterITForm
	 * @return
	 */
	public Optional<MatterITForm> getMatterITForm (MatterITForm matterITForm) {
		Optional<MatterITForm> optMatterITForm = 
				matterITFormRepository.findByLanguageIdAndClassIdAndMatterNumberAndClientIdAndIntakeFormIdAndIntakeFormNumberAndDeletionIndicator(
						matterITForm.getLanguageId(),
						matterITForm.getClassId(),
						matterITForm.getMatterNumber(),
						matterITForm.getClientId(),
						matterITForm.getIntakeFormId(),
						matterITForm.getIntakeFormNumber(), 0L);
//		if (optMatterITForm.isEmpty()) {
//			throw new BadRequestException("Record doesn't exist with the given values");
//		}
		return optMatterITForm;
	}
	
	public List<MatterITForm> findMatterITForm(SearchMatterITForm searchMatterITForm) throws ParseException {
		if (searchMatterITForm.getSSentOn() != null && searchMatterITForm.getESentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterITForm.getSSentOn(), searchMatterITForm.getESentOn());
			searchMatterITForm.setSSentOn(dates[0]);
			searchMatterITForm.setESentOn(dates[1]);
		}
		
		if (searchMatterITForm.getSReceivedOn() != null && searchMatterITForm.getEReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterITForm.getSReceivedOn(), searchMatterITForm.getEReceivedOn());
			searchMatterITForm.setSReceivedOn(dates[0]);
			searchMatterITForm.setEReceivedOn(dates[1]);
		}
		
		MatterITFormSpecification spec = new MatterITFormSpecification(searchMatterITForm);
		List<MatterITForm> results = matterITFormRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}
	
	/**
	 * createMatterITForm
	 * @param newMatterITForm
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterITForm createMatterITForm (AddMatterITForm newMatterITForm, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		String languageId = setupService.getLanguageIdFromIntakeIDMaster(newMatterITForm.getIntakeFormId(), authTokenForSetupService.getAccess_token());
		
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterITForm.getMatterNumber());
		Long classId = matterGenAcc.getClassId();
		
		// Check for Duplicates
		MatterITForm checkDupMatterITForm = new MatterITForm();
		BeanUtils.copyProperties(newMatterITForm, checkDupMatterITForm, CommonUtils.getNullPropertyNames(newMatterITForm));
		Optional<MatterITForm> matterITForm = getMatterITForm(checkDupMatterITForm);
		if (!matterITForm.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
				
		MatterITForm dbMatterITForm = new MatterITForm();
		
		// LANG_ID - LANG_ID of the passed IT_FORM_ID
		dbMatterITForm.setLanguageId(languageId);
		
		// CLASS_ID - CLASS_ID of the selected MATTER_NO
		dbMatterITForm.setClassId(classId);
		
		// MATTER_NO
		dbMatterITForm.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		dbMatterITForm.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_ID
		dbMatterITForm.setIntakeFormId(newMatterITForm.getIntakeFormId());
		
		// IT_FORM_NO
		Long classID = 3L;
		Long numberRangeCode = 8L;
		String newIntakeNumber = setupService.getNextNumberRange(classID, numberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newIntakeNumber);
		dbMatterITForm.setIntakeFormNumber(newIntakeNumber);
		
		// CLIENT_USR_ID
		dbMatterITForm.setClientUserId(newMatterITForm.getClientUserId());
		
		// STATUS_ID
		dbMatterITForm.setStatusId(10L); // Hard Coded value "10L"
		
		// SENT_ON
		dbMatterITForm.setSentOn(new Date());
		
		dbMatterITForm.setCreatedBy(loginUserID);
		dbMatterITForm.setCreatedOn(new Date());
		dbMatterITForm.setUpdatedBy(loginUserID);
		dbMatterITForm.setUpdatedOn(new Date());
		dbMatterITForm.setDeletionIndicator(0L);
		
		log.info("dbMatterITForm : " + dbMatterITForm); 
		MatterITForm createdMatterITForm = matterITFormRepository.save(dbMatterITForm);
		
		//---------------------------------------------------------------------------------------
		// Insert a record in corresponding Mongo DB as below based on ITFormID
		if (newMatterITForm.getIntakeFormId() == 7) {
			ITForm007 responseITForm007 = saveITForm007(languageId, classId, newIntakeNumber, newMatterITForm, loginUserID);
			log.info("responseITForm007 : " + responseITForm007);
		}
		
		if (newMatterITForm.getIntakeFormId() == 8) {
			ITForm008 responseITForm008 = saveITForm008(languageId, classId, newIntakeNumber, newMatterITForm, loginUserID);
			log.info("responseITForm008 : " + responseITForm008);
		}
		
		if (newMatterITForm.getIntakeFormId() == 9) {
			ITForm009 responseITForm009 = saveITForm009(languageId, classId, newIntakeNumber, newMatterITForm, loginUserID);
			log.info("responseITForm009 : " + responseITForm009);
		}
		
		if (newMatterITForm.getIntakeFormId() == 10) {
			ITForm010 responseITForm010 = saveITForm010(languageId, classId, newIntakeNumber, newMatterITForm, loginUserID);
			log.info("responseITForm010 : " + responseITForm010);
		}
		
		if (newMatterITForm.getIntakeFormId() == 11) {
			ITForm011 responseITForm011 = saveITForm011(languageId, classId, newIntakeNumber, newMatterITForm, loginUserID);
			log.info("responseITForm011 : " + responseITForm011);
		}
		return createdMatterITForm;
		
	}
	
	/**
	 * saveITForm007
	 * @param languageId
	 * @param classId
	 * @param newIntakeNumber
	 * @param newMatterITForm
	 * @param loginUserID
	 * @return
	 */
	private ITForm007 saveITForm007 (String languageId, Long classId, String newIntakeNumber, AddMatterITForm newMatterITForm, String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForCrmService = authTokenService.getCrmServiceAuthToken();
		
		ITForm007 itForm007 = new ITForm007();
		
		// LANG_ID
		itForm007.setLanguage(languageId);
		
		// CLASS_ID
		itForm007.setClassID(classId);
		
		// MATTER_NO
		itForm007.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		itForm007.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_NO
		itForm007.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm007.setItFormID(newMatterITForm.getIntakeFormId());
		
		// STATUS_ID
		itForm007.setStatus(9L); // Hard Coded Value"22"
				
		// SENT_ON
		itForm007.setSentOn(new Date());
		
		// CTD_BY
		itForm007.setCreatedBy(loginUserID);
		
		// CTD_ON
		itForm007.setCreatedOn(new Date());
		
		// UTD_BY
		itForm007.setUpdatedBy(loginUserID);
		
		// UTD_ON
		itForm007.setUpdatedOn(new Date());
		
		// ID
		itForm007.setId(itForm007.getId());
		return mongoService.createITForm007(itForm007, loginUserID, authTokenForCrmService.getAccess_token());
	}
	
	/**
	 * saveITForm007
	 * @param languageId
	 * @param classId
	 * @param newIntakeNumber
	 * @param newMatterITForm
	 * @param loginUserID
	 * @return
	 */
	private ITForm008 saveITForm008 (String languageId, Long classId, String newIntakeNumber, AddMatterITForm newMatterITForm, String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForCrmService = authTokenService.getCrmServiceAuthToken();
		
		ITForm008 itForm008 = new ITForm008();
		
		// LANG_ID
		itForm008.setLanguage(languageId);
		
		// CLASS_ID
		itForm008.setClassID(classId);
		
		// MATTER_NO
		itForm008.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		itForm008.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_NO
		itForm008.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm008.setItFormID(newMatterITForm.getIntakeFormId());
		
		// STATUS_ID
		itForm008.setStatus(9L); // Hard Coded Value"22"
				
		// SENT_ON
		itForm008.setSentOn(new Date());
		
		// CTD_BY
		itForm008.setCreatedBy(loginUserID);
		
		// CTD_ON
		itForm008.setCreatedOn(new Date());
		
		// UTD_BY
		itForm008.setUpdatedBy(loginUserID);
		
		// UTD_ON
		itForm008.setUpdatedOn(new Date());
		
		// ID
		itForm008.setId(itForm008.getId());
		return mongoService.createITForm008(itForm008, loginUserID, authTokenForCrmService.getAccess_token());
	}
	
	/**
	 * saveITForm007
	 * @param languageId
	 * @param classId
	 * @param newIntakeNumber
	 * @param newMatterITForm
	 * @param loginUserID
	 * @return
	 */
	private ITForm009 saveITForm009 (String languageId, Long classId, String newIntakeNumber, AddMatterITForm newMatterITForm, String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForCrmService = authTokenService.getCrmServiceAuthToken();
		
		ITForm009 itForm009 = new ITForm009();
		
		// LANG_ID
		itForm009.setLanguage(languageId);
		
		// CLASS_ID
		itForm009.setClassID(classId);
		
		// MATTER_NO
		itForm009.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		itForm009.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_NO
		itForm009.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm009.setItFormID(newMatterITForm.getIntakeFormId());
		
		// STATUS_ID
		itForm009.setStatus(9L); // Hard Coded Value"22"
				
		// SENT_ON
		itForm009.setSentOn(new Date());
		
		// CTD_BY
		itForm009.setCreatedBy(loginUserID);
		
		// CTD_ON
		itForm009.setCreatedOn(new Date());
		
		// UTD_BY
		itForm009.setUpdatedBy(loginUserID);
		
		// UTD_ON
		itForm009.setUpdatedOn(new Date());
		
		// ID
		itForm009.setId(itForm009.getId());
		return mongoService.createITForm009(itForm009, loginUserID, authTokenForCrmService.getAccess_token());
	}
	
	/**
	 * saveITForm007
	 * @param languageId
	 * @param classId
	 * @param newIntakeNumber
	 * @param newMatterITForm
	 * @param loginUserID
	 * @return
	 */
	private ITForm010 saveITForm010 (String languageId, Long classId, String newIntakeNumber, AddMatterITForm newMatterITForm, String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForCrmService = authTokenService.getCrmServiceAuthToken();
		
		ITForm010 itForm010 = new ITForm010();
		
		// LANG_ID
		itForm010.setLanguage(languageId);
		
		// CLASS_ID
		itForm010.setClassID(classId);
		
		// MATTER_NO
		itForm010.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		itForm010.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_NO
		itForm010.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm010.setItFormID(newMatterITForm.getIntakeFormId());
		
		// STATUS_ID
		itForm010.setStatus(9L); // Hard Coded Value"22"
				
		// SENT_ON
		itForm010.setSentOn(new Date());
		
		// CTD_BY
		itForm010.setCreatedBy(loginUserID);
		
		// CTD_ON
		itForm010.setCreatedOn(new Date());
		
		// UTD_BY
		itForm010.setUpdatedBy(loginUserID);
		
		// UTD_ON
		itForm010.setUpdatedOn(new Date());
		
		// ID
		itForm010.setId(itForm010.getId());
		return mongoService.createITForm010(itForm010, loginUserID, authTokenForCrmService.getAccess_token());
	}
	
	/**
	 * saveITForm011
	 * @param languageId
	 * @param classId
	 * @param newIntakeNumber
	 * @param newMatterITForm
	 * @param loginUserID
	 * @return
	 */
	private ITForm011 saveITForm011 (String languageId, Long classId, String newIntakeNumber, AddMatterITForm newMatterITForm, String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForCrmService = authTokenService.getCrmServiceAuthToken();
		
		ITForm011 itForm011 = new ITForm011();
		
		// LANG_ID
		itForm011.setLanguage(languageId);
		
		// CLASS_ID
		itForm011.setClassID(classId);
		
		// MATTER_NO
		itForm011.setMatterNumber(newMatterITForm.getMatterNumber());
		
		// CLIENT_ID
		itForm011.setClientId(newMatterITForm.getClientId());
		
		// IT_FORM_NO
		itForm011.setItFormNo(newIntakeNumber);
		
		// IT_FORM_ID
		itForm011.setItFormID(newMatterITForm.getIntakeFormId());
		
		// STATUS_ID
		itForm011.setStatus(9L); // Hard Coded Value"22"
				
		// SENT_ON
		itForm011.setSentOn(new Date());
		
		// CTD_BY
		itForm011.setCreatedBy(loginUserID);
		
		// CTD_ON
		itForm011.setCreatedOn(new Date());
		
		// UTD_BY
		itForm011.setUpdatedBy(loginUserID);
		
		// UTD_ON
		itForm011.setUpdatedOn(new Date());
		
		// ID
		itForm011.setId(itForm011.getId());
		return mongoService.createITForm011(itForm011, loginUserID, authTokenForCrmService.getAccess_token());
	}
	
	/**
	 * updateMatterITForm
	 * @param intakeFormNumber 
	 * @param intakeFormId
	 * @param updateMatterITForm
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterITForm updateMatterITForm (String intakeFormNumber, UpdateMatterITForm updateMatterITForm, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		// Check for Duplicates
		MatterITForm dbMatterITForm = getMatterITForm(intakeFormNumber);
		BeanUtils.copyProperties(updateMatterITForm, dbMatterITForm, CommonUtils.getNullPropertyNames(updateMatterITForm));
		dbMatterITForm.setUpdatedBy(loginUserID);
		dbMatterITForm.setUpdatedOn(new Date());
		dbMatterITForm = matterITFormRepository.save(dbMatterITForm);
		return dbMatterITForm;
	}
	
	/**
	 * 
	 * @param updateMatterITForm
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterITForm approveMatterITForm (String intakeFormNumber, UpdateMatterITForm updateMatterITForm, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterITForm dbMatterITForm = getMatterITForm(intakeFormNumber);
		BeanUtils.copyProperties(updateMatterITForm, dbMatterITForm, CommonUtils.getNullPropertyNames(updateMatterITForm));
		
		// If STATUS_ID=10, update APPROVED_ON with server date_time
		if (updateMatterITForm.getStatusId().longValue() == 10) {
			dbMatterITForm.setApprovedOn(new Date());
		}
		
		dbMatterITForm.setUpdatedBy(loginUserID);
		dbMatterITForm.setUpdatedOn(new Date());
		return matterITFormRepository.save(dbMatterITForm);
	}
	
	/**
	 * 
	 * @param intakeFormNumber
	 * @param loginUserID
	 */
	public void deleteMatterITForm (String intakeFormNumber, String loginUserID) {
		Optional<MatterITForm> optMatterITForm = matterITFormRepository.findByIntakeFormNumber(intakeFormNumber);
		if ( optMatterITForm.isEmpty() ) {
			throw new EntityNotFoundException("Error in deleting Id: " + intakeFormNumber);
		}
		
		MatterITForm matterITForm = optMatterITForm.get();
		if (matterITForm.getStatusId().longValue() != 10) {
			matterITForm.setDeletionIndicator(1L);
			matterITForm.setUpdatedBy(loginUserID);
			matterITForm.setUpdatedOn(new Date());
			matterITFormRepository.save(matterITForm);
		} else {
			throw new BadRequestException("Selected INTAKE FORM can't be deleted as this is already approved");
		}
	}
}
