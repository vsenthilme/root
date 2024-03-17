package com.mnrclara.api.management.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.EMail;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.matterassignment.MatterAssignment;
import com.mnrclara.api.management.model.matterdoclist.AddMatterDocList;
import com.mnrclara.api.management.model.matterdoclist.MatterDocList;
import com.mnrclara.api.management.model.matterdoclist.MatterDocListHeader;
import com.mnrclara.api.management.model.matterdoclist.MatterDocListLine;
import com.mnrclara.api.management.model.matterdoclist.SearchMatterDocList;
import com.mnrclara.api.management.repository.ClientGeneralRepository;
import com.mnrclara.api.management.repository.MatterDocListHeaderRepository;
import com.mnrclara.api.management.repository.MatterDocListLineRepository;
import com.mnrclara.api.management.repository.MatterDocListRepository;
import com.mnrclara.api.management.repository.specification.MatterDocListSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterDocListService {
	
	@Autowired
	private MatterDocListRepository matterDocListRepository;
	
	@Autowired
	private MatterDocListHeaderRepository matterDocListHeaderRepository;
	
	@Autowired
	private MatterDocListLineRepository matterDocListLineRepository;
	
	@Autowired
	private MatterAssignmentService matterAssignmentService;
	
	@Autowired
	private ClientGeneralRepository clientGeneralRepository;
	
	@Autowired
	private SetupService setupService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PropertiesConfig propertiesConfig;
	
	/**
	 * getMatterDocLists
	 * @return
	 */
	public List<MatterDocList> getMatterDocLists () {
		List<MatterDocList> matterDocListList =  matterDocListRepository.findAll();
		matterDocListList = matterDocListList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterDocListList;
	}
	
	/**
	 * 
	 * @param matterDocHeaderId
	 * @return
	 */
	public MatterDocListHeader getMatterDocListHeader (Long matterDocHeaderId) {
		MatterDocListHeader matterDocList = matterDocListHeaderRepository.findByMatterHeaderId(matterDocHeaderId);
		if (matterDocList != null) {
			return matterDocList;
		}
		throw new BadRequestException("The given MatterDocList ID : " + matterDocHeaderId + " doesn't exist.");
	}
	
	/**
	 * getMatterDocList
	 * @param matterNumber
	 * @return
	 */
	public MatterDocList getMatterDocList (String languageId, Long classId, Long checkListNo, 
			String matterNumber, String clientId) {
		Optional<MatterDocList> matterDocList = 
				matterDocListRepository.findByLanguageIdAndClassIdAndCheckListNoAndMatterNumberAndClientIdAndDeletionIndicator(
						languageId, classId, checkListNo, matterNumber, clientId, 0L);
		if (matterDocList != null) {
			return matterDocList.get();
		}
		throw new BadRequestException("The given MatterDocList ID : matterNumber: " + matterNumber + 
				"checkListNo: " + checkListNo + "," + 
				"clientId: " + clientId + "," + 
				"classId: " + classId + " doesn't exist.");
	}
	
	/**
	 * getMatterDocList
	 * @param clientId
	 * @param matterNumber
	 * @param checkListNo
	 * @return
	 */
	public MatterDocList getMatterDocList (String clientId, String matterNumber, Long checkListNo, Long sequenceNumber) {
		MatterDocList matterDocList = 
				matterDocListRepository.findByMatterNumberAndClientIdAndCheckListNoAndSequenceNumber (matterNumber, clientId, checkListNo, sequenceNumber);
		if (matterDocList != null) {
			return matterDocList;
		}
		throw new BadRequestException("The given MatterDocList ID : matterNumber: " + matterNumber + 
				"checkListNo: " + checkListNo + "," + 
				"clientId: " + clientId + "," + 
				"sequenceNumber: " + sequenceNumber +
				" doesn't exist.");
	}
	
	/**
	 * 
	 * @param searchMatterDocList
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException 
	 */
	public List<MatterDocList> findMatterDocList(SearchMatterDocList searchMatterDocList) 
			throws ParseException, java.text.ParseException {		
		if (searchMatterDocList.getStartCreatedOn() != null && searchMatterDocList.getStartCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterDocList.getStartCreatedOn(), searchMatterDocList.getEndCreatedOn());
			searchMatterDocList.setStartCreatedOn(dates[0]);
			searchMatterDocList.setEndCreatedOn(dates[1]);
		}	
		MatterDocListSpecification spec = new MatterDocListSpecification(searchMatterDocList);
		List<MatterDocList> results = matterDocListRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}
	
	/**
	 * createMatterDocList
	 * @param newMatterDocList
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<MatterDocList> createMatterDocList (List<AddMatterDocList> newMatterDocList, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		List<MatterDocList> createdMatterDocList = new ArrayList<>();
		for (AddMatterDocList newMatterDocRecord : newMatterDocList) {
			MatterDocList dbMatterDocList = new MatterDocList();
			log.info("newMatterDocList : " + newMatterDocList);
			BeanUtils.copyProperties(newMatterDocRecord, dbMatterDocList, CommonUtils.getNullPropertyNames(newMatterDocRecord));
			
			// STATUS_ID	Hard Coded Value '22" 
			dbMatterDocList.setStatusId(22L);
			dbMatterDocList.setDeletionIndicator(0L);
			dbMatterDocList.setCreatedBy(loginUserID);
			dbMatterDocList.setUpdatedBy(loginUserID);
			dbMatterDocList.setCreatedOn(new Date());
			dbMatterDocList.setUpdatedOn(new Date());
			createdMatterDocList.add(matterDocListRepository.save(dbMatterDocList));
		}
		return createdMatterDocList;
	}
	
	/**
	 * 
	 * @param clientId
	 * @param matterNumber
	 * @param checkListNo
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterDocListHeader updateMatterDocList (String clientId, String matterNumber, Long checkListNo, Long matterHeaderId, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		/*
		 * Moving all Temp files to ClientPortal Folder
		 */
		String filePath = propertiesConfig.getDocStorageBasePath();
		String srcFilePath = filePath + "/clientportal/temp/" + clientId + "/" + matterNumber + "/" + checkListNo;
		String tgtFilePath = filePath + "/clientportal/" + clientId + "/" + matterNumber + "/" + checkListNo;
		
		// File move from Source to Destination Folder
		Path sourceDir = Paths.get(srcFilePath);
	    Path destinationDir = Paths.get(tgtFilePath);

	    if (!Files.exists(destinationDir)) {
			try {
				Files.createDirectories(destinationDir);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
			}
		}
	    
	    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDir)) {
	        for (Path path : directoryStream) {
	            log.info("copying " + path.toString());
	            Path d2 = destinationDir.resolve(path.getFileName());
	            
	            log.info("destination File=" + d2);
	            Files.move(path, d2);
	        }
	        log.info("Files moved successfully.");
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	    
		MatterDocListHeader dbMatterDocListHeader = getMatterDocListHeader (matterHeaderId);
		dbMatterDocListHeader.setStatusId(23L);
		dbMatterDocListHeader.setReceivedOn(new Date());
		dbMatterDocListHeader.setUpdatedBy(loginUserID);
		dbMatterDocListHeader.setUpdatedOn(new Date());
		MatterDocListHeader updatedMatterDocListHeader = matterDocListHeaderRepository.save(dbMatterDocListHeader);
		log.info("updatedMatterDocListHeader : " + updatedMatterDocListHeader);
		
		Set<MatterDocListLine> dbMatterDocListLine = dbMatterDocListHeader.getMatterDocLists();
		dbMatterDocListLine.stream().forEach(a->a.setStatusId(23L));
		matterDocListLineRepository.saveAll(dbMatterDocListLine);
		
		updatedMatterDocListHeader.setReferenceField1(tgtFilePath);
		
		/*
		 * Sending Email
		 * ---------------------
		 * 1. Pass MatterNo to MatterAssignment and get Ref_field_2 is Paralegal, Legal Assist is LegalAssist
		 * 2. Pass Ref_field_2 and LegalAssit (User IDs) to UserProfile table and get respective email and send mail
		 */
		MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(matterNumber);
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		if (matterAssignment != null) {
			String paraLegalUserId = matterAssignment.getReferenceField2(); // Para Legal
			String legalAssistUserId = matterAssignment.getLegalAssistant(); // LegalAssist
			
			Optional<ClientGeneral> clientGeneral = clientGeneralRepository.findByClientId(matterAssignment.getClientId());
			
			log.info("paraLegalUserId : " + paraLegalUserId);
			log.info("legalAssistUserId : " + legalAssistUserId);
			
			// Para Legal
			if (paraLegalUserId != null) {
				UserProfile userProfile = setupService.getUserProfile(paraLegalUserId, authTokenForSetupService.getAccess_token());
				if (userProfile != null ) {
					sendMail (userProfile.getEmailId(), userProfile.getFullName(), matterNumber, clientGeneral.get().getFirstNameLastName());				
				}
			}
			
			if (legalAssistUserId != null) {
				UserProfile userProfile = setupService.getUserProfile(legalAssistUserId, authTokenForSetupService.getAccess_token());
				if (userProfile != null ) {
					sendMail (userProfile.getEmailId(), userProfile.getFullName(), matterNumber, clientGeneral.get().getFirstNameLastName());	
				}
			}
		}
		return updatedMatterDocListHeader;
	}
	
	/**
	 * 
	 * @param emailId
	 * @param firstName
	 * @param matterNo
	 * @param firstNameLastName 
	 */
	private void sendMail (String emailId, String firstName, String matterNo, String firstNameLastName) {
		EMail email = new EMail();
		String subject = "Document uploaded for Matter Number:" + matterNo;
		String msg = "<p style=\"font-family:'Times New Roman';color:blue;size:18px;\">" +
				" Dear " + firstName + ", <br/><br/> "
				+ firstNameLastName + " has uploaded a new document for Matter Number: [" + matterNo + "]. "
				+ "Please view it in Client Portal Documents within Matter Management.";
		msg += "<br/><br/>";
		msg += "Thank you,";
		msg += "<br/>M&R CLARA</p>";
		
		email.setFromAddress(propertiesConfig.getLoginEmailFromAddress());
		email.setSubject(subject);
		email.setBodyText(msg);
		email.setToAddress(emailId);
		email.setSenderName("ClaraITAdmin");
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getCommonServiceAuthToken();
		boolean isEMailSent = commonService.sendEmail(email, authTokenForSetupService.getAccess_token());
		log.info("isEMailSent:" + isEMailSent);
	}
	
	/**
	 * deleteMatterDocList
	 * @param loginUserID 
	 * @param matterNumber
	 */
	public void deleteMatterDocList (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String loginUserID) {
		MatterDocList matterDocList = getMatterDocList (languageId, classId, checkListNo, matterNumber, clientId);
		if ( matterDocList != null && matterDocList.getStatusId() == 22L ) {
			matterDocList.setDeletionIndicator(1L);
			matterDocList.setUpdatedBy(loginUserID);
			matterDocListRepository.save(matterDocList);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + checkListNo + " as this record status is not 22.");
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Path sourceDir = Paths.get("D:\\Murugavel.R\\Project\\7horses\\RND\\proc");
	    Path destinationDir = Paths.get("D:\\Murugavel.R\\Project\\7horses\\RND\\proc1");

	    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDir)) {
	        for (Path path : directoryStream) {
	            System.out.println("copying " + path.toString());
	            Path d2 = destinationDir.resolve(path.getFileName());
	            System.out.println("destination File=" + d2);
	            Files.move(path, d2);
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
}
