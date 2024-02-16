package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.caseinfosheet.ImmCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.ImmCaseInfoSheetRepository;
import com.mnrclara.api.management.repository.LeCaseInfoSheetRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaseInfoSheetService {

	@Autowired
	private LeCaseInfoSheetRepository leCaseInfoSheetRepository;

	@Autowired
	private ImmCaseInfoSheetRepository immCaseInfoSheetRepository;

	@Autowired
	private MatterGenAccRepository matterGenAccRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MatterGenAccService matterGenAccService;
	
	@Autowired
	private ClientGeneralService clientGeneralService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * 
	 * @param caseInformationID
	 * @param loginUserID
	 * @return
	 * @throws ParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public MatterGenAcc createMatter(String caseInformationID, String loginUserID, boolean isFromLE) throws IllegalAccessException, InvocationTargetException, ParseException {
		MatterGenAcc matterGeneralAccount = new MatterGenAcc();
		LeCaseInfoSheet dbLeCaseInfoSheet = null;
		ImmCaseInfoSheet immCaseInfoSheet = null;
		String clientID = "";
		
		if (isFromLE) {
			dbLeCaseInfoSheet = leCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
			log.info("dbLeCaseInfoSheet : " + dbLeCaseInfoSheet);

			// CLASS_ID
			matterGeneralAccount.setClassId(dbLeCaseInfoSheet.getClassId());

			// CLIENT_ID
			matterGeneralAccount.setClientId(dbLeCaseInfoSheet.getClientId());

			// CASE_CATEGORY_ID
			matterGeneralAccount.setCaseCategoryId(dbLeCaseInfoSheet.getCaseCategoryId());

			// CASE_SUB_CATEGORY_ID
			matterGeneralAccount.setCaseSubCategoryId(dbLeCaseInfoSheet.getCaseSubCategoryId());

			// MATTER_TEXT
			matterGeneralAccount.setMatterDescription(dbLeCaseInfoSheet.getMatterDescription());

			clientID = dbLeCaseInfoSheet.getClientId();
		} else {
			immCaseInfoSheet = immCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
			log.info("immCaseInfoSheet -------> : " + immCaseInfoSheet);

			// CLASS_ID
			matterGeneralAccount.setClassId(immCaseInfoSheet.getClassId());

			// CLIENT_ID
			matterGeneralAccount.setClientId(immCaseInfoSheet.getClientId());

			// CASE_CATEGORY_ID
			matterGeneralAccount.setCaseCategoryId(immCaseInfoSheet.getCaseCategoryId());

			// CASE_SUB_CATEGORY_ID
			matterGeneralAccount.setCaseSubCategoryId(immCaseInfoSheet.getCaseSubCategoryId());

			// MATTER_TEXT
			matterGeneralAccount.setMatterDescription(immCaseInfoSheet.getMatterDescription());

			clientID = immCaseInfoSheet.getClientId();
		}
		
		// MATTER_NO
		/*
		 * "Pass the selected CLIENT_ID in MATTERGENACC table and fetch MATTER_NO
		 * values. 1. sort by descending, add the next number to the latest no and
		 * insert (Example : if the last no is 10001-05, insert a no 10001-06) 2. If
		 * MATTER_NO value is null, insert a number start with '01' by adding CLIENT_ID
		 * (Example : 10001-01)"
		 * 
		 */
		String MATTER_NO = null;
		
		// Saving in DB
		MatterGenAcc createdMatterGenAcc;
		try {
			/*
			 * Checkng Client_ID (Ref_field_9 is null) in Docketwise or not
			 */
			//Client ID from ClientGeneral
			ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(matterGeneralAccount.getClientId());
			log.info("clientGeneral.getReferenceField9() : " + clientGeneral.getReferenceField9());
			
			if (clientGeneral.getClassId().longValue() == 2 && clientGeneral.getReferenceField9() == null) {
				throw new BadRequestException("Client Id is not found in in Docketwise. Can't create Matter.");
			}

			// LANG_ID
			matterGeneralAccount.setLanguageId("EN");

			// CASEINFO_NO
			matterGeneralAccount.setCaseInformationNo(caseInformationID);

			List<MatterGenAcc> matterGeneralAccounts = matterGenAccRepository.findByClientId(clientID);
			try {
				MATTER_NO = getNextMatterNo(clientID, matterGeneralAccounts);
			} catch (Exception e) {
				e.printStackTrace();
				MatterGenAcc latestMatterGenAcc = matterGenAccRepository.findTopByClientIdOrderByCreatedOnDesc (clientID);
				log.info("latestMatterGenAcc: " + latestMatterGenAcc);
				String matterNumber = latestMatterGenAcc.getMatterNumber();
				
				int matterExtn = 0;
				String sSplitted = matterNumber.substring(matterNumber.lastIndexOf("-") + 1);
				matterExtn = Integer.valueOf(sSplitted);
				log.info("matterExtn 1->: " + matterExtn);
				matterExtn++;
				
				if (matterExtn < 10) {
					MATTER_NO = clientID + "-0" + matterExtn;
				} else if (matterExtn >= 10) {
					MATTER_NO = clientID + "-" + matterExtn;
				} else {
					MATTER_NO = clientID + "-01";
				}
			}
			
			log.info("MATTER_NO : " + MATTER_NO);
			matterGeneralAccount.setMatterNumber(MATTER_NO);

			// TRANS_ID
			matterGeneralAccount.setTransactionId(8L); // Hard Coded Value "08"

			// CASE_OPEN_DATE
			matterGeneralAccount.setCaseOpenedDate(new Date());
			
			// REF_FIELD MAPPING
			matterGeneralAccount.setReferenceField15(clientGeneral.getReferenceField16());
			matterGeneralAccount.setReferenceField16(clientGeneral.getReferenceField17());
			matterGeneralAccount.setReferenceField17(clientGeneral.getReferenceField18());
			matterGeneralAccount.setReferenceField18(clientGeneral.getReferenceField19());
			matterGeneralAccount.setReferenceField19(clientGeneral.getReferenceField20());

			// STATUS_ID
			matterGeneralAccount.setStatusId(26L); // Hard coded Value "25"
			matterGeneralAccount.setSentToQB(0L);
			matterGeneralAccount.setDeletionIndicator(0L);
			matterGeneralAccount.setCreatedBy(loginUserID);
			matterGeneralAccount.setUpdatedBy(loginUserID);
			matterGeneralAccount.setCreatedOn(new Date());
			matterGeneralAccount.setUpdatedOn(new Date());
			
			createdMatterGenAcc = matterGenAccRepository.save(matterGeneralAccount);
			log.info("matterGeneralAccount : " + createdMatterGenAcc);
		
			/*
			 * When a MATTER_NO is created and inserted in MATTERGENACC table successfully,
			 * update LECASEINFOSHEET table by CASEINFO_NO with below fields
			 */
			if (isFromLE && dbLeCaseInfoSheet != null) {
				// MATTER_NO
				dbLeCaseInfoSheet.setMatterNumber(MATTER_NO);
	
				// STATUS_ID
				dbLeCaseInfoSheet.setStatusId(26L); // Update with Hardcoded value "26"
				leCaseInfoSheetRepository.save(dbLeCaseInfoSheet);
			} else if (immCaseInfoSheet != null) {
				// MATTER_NO
				immCaseInfoSheet.setMatterNumber(MATTER_NO);
	
				// STATUS_ID
				immCaseInfoSheet.setStatusId(26L); // Update with Hardcoded value "26"
				immCaseInfoSheetRepository.save(immCaseInfoSheet);
			}
			
			//-------------------- Docketwise Integration-----------------------------------------------
			if (createdMatterGenAcc.getClassId().longValue() == 2 && 
					propertiesConfig.getDocketwiseStoreFlag().equalsIgnoreCase("true")) {
				Matter matter = populateDocketwiseMatter (createdMatterGenAcc);
				log.info("Matter from caseinfosheet: " + matter);
				
				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Matter createdMatter = commonService.createDocketwiseMatter(matter, authTokenForCommonService.getAccess_token());
				
				log.info("createdMatter------------> : " + createdMatter);
				
				// Updating MatterGenAcc with new created ID
				MatterGenAcc updateMatterGenAcc = matterGenAccService.getMatterGenAcc(createdMatterGenAcc.getMatterNumber());
				updateMatterGenAcc.setReferenceField9(String.valueOf(createdMatter.getId()));
				updateMatterGenAcc.setUpdatedBy(loginUserID);
				updateMatterGenAcc.setUpdatedOn(new Date());
				updateMatterGenAcc = matterGenAccRepository.save(updateMatterGenAcc);
				return updateMatterGenAcc;
			}
			return createdMatterGenAcc;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * populateDocketwiseMatter
	 * @param createdMatterGenAcc
	 * @return
	 */
	private Matter populateDocketwiseMatter(MatterGenAcc createdMatterGenAcc) {
		//Client ID from ClientGeneral
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(createdMatterGenAcc.getClientId());
		
		Matter matter = new Matter();

		// Number
		matter.setNumber(createdMatterGenAcc.getMatterNumber());
		
		// Title
		matter.setTitle(createdMatterGenAcc.getMatterDescription());
		
		// Description
		matter.setDescription(createdMatterGenAcc.getMatterDescription());
		
		// Client_id
		log.info("clientGeneral.getReferenceField9() : " + clientGeneral.getReferenceField9());
		if (clientGeneral.getReferenceField9() == null) {
			throw new BadRequestException("Client Id is not found in clientGeneral.getReferenceField9().");
		}
		matter.setClient_id(Long.valueOf(clientGeneral.getReferenceField9()));
		
		return matter;
	}

	/**
	 * getNextMatterNo
	 * 
	 * @param clientId
	 * @param matterGeneralAccounts
	 * @return
	 */
	private String getNextMatterNo(String clientId, List<MatterGenAcc> matterGeneralAccounts) {
		if (matterGeneralAccounts.isEmpty()) {
			// Generate new Matter NO
			return clientId + "-01"; // 10001-01
		} else {
			// Increment Matter No by 1
			List<Integer> matterSubnumbers = new ArrayList<>();
			for (MatterGenAcc matter : matterGeneralAccounts) {
				if (matter != null && matter.getMatterNumber() != null 
						&& matter.getMatterNumber().indexOf('-') >= 0) {
					String[] sSplitted = matter.getMatterNumber().split("-");
					matterSubnumbers.add(Integer.valueOf(sSplitted[1]));
				}
			}
			if (matterSubnumbers.isEmpty()) { // Matter doesn't contain extn.
				return clientId + "-01"; // 10001-01
			}
			
			Collections.sort(matterSubnumbers);
			log.info("matterSubnumbers :" + matterSubnumbers);
			
			int maxNo = matterSubnumbers.get(matterSubnumbers.size() - 1);
			maxNo++;
			log.info("Max No : " + maxNo);
			if (maxNo < 10) {
				log.info("Max Matter No : " + clientId + "-0" + maxNo);
				return clientId + "-0" + maxNo;
			} else if (maxNo >= 10 || maxNo <= 99) {
				log.info("Max Matter No : " + clientId + "-" + maxNo);
				return clientId + "-" + maxNo;
			} else {
				return clientId + "-01"; // 10001-01
			}
		}
	}
	
	/**
	 * 
	 */
	public void	syncupMatterWithDocket () {
		try {
			MatterGenAcc matterGenAcc = 
					matterGenAccRepository.findTopByClassIdAndSentToDocketwiseAndDeletionIndicatorOrderByCreatedOn (2L, 0L, 0L);
			if (matterGenAcc != null) {
				Matter matter = populateDocketwiseMatter (matterGenAcc);
				
				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Matter createdMatter = commonService.createDocketwiseMatter(matter, authTokenForCommonService.getAccess_token());
				log.info("createdMatter------------> : " + createdMatter.getId());
				if (createdMatter.getId() != null) {
					// Updating MatterGenAcc with new created ID
					matterGenAcc.setReferenceField9(String.valueOf(createdMatter.getId()));
					matterGenAcc.setSentToDocketwise(1L);
					MatterGenAcc updatedMatterGenAcc = matterGenAccRepository.save(matterGenAcc);
					log.info("createdMatter---updated---------> : " + updatedMatterGenAcc.getMatterNumber());
				}
			}
		} catch (Exception e) {
			log.info("Error on Matter syncup--------> : " + e.toString());
		}
	}
	
	public static void main(String[] args) {
		String clientID = "2656";
		String matterNumber = "-2656213";
		
		log.info("exists : " + matterNumber.indexOf('-'));
		
//		int matterExtn = 0;
//		String sSplitted = matterNumber.substring(matterNumber.lastIndexOf("-") + 1);
//		matterExtn = Integer.valueOf(sSplitted);
//		log.info("matterExtn 1->: " + matterExtn);
//		matterExtn++;
//		
//		if (matterExtn < 10) {
//			log.info(clientID + "-0" + matterExtn);
//		} else if (matterExtn >= 10 || matterExtn <= 99) {
//			log.info(clientID + "-" + matterExtn);
//		} else {
//			log.info(clientID + "-01");
//		}
	}
}