package com.mnrclara.api.management.service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.model.matterdoclist.*;
import com.mnrclara.api.management.repository.MatterDocListHeaderRepository;

import com.mnrclara.api.management.repository.MatterDocListLineRepository;
import com.mnrclara.api.management.repository.specification.MatterDocListHeaderSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatterDocListHeaderService {
	
	@Autowired
	private MatterDocListHeaderRepository matterDocListHeaderRepository;
	@Autowired
	private MatterDocListLineRepository matterDocListLineRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * getMatterDocListHeaders
	 * @return
	 */
	public List<MatterDocListHeader> getMatterDocListHeaders () {
		List<MatterDocListHeader> matterDocListHeaderList =  matterDocListHeaderRepository.findAll();
		matterDocListHeaderList = matterDocListHeaderList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterDocListHeaderList;
	}
	
	/**
	 * getMatterDocListHeader
	 * @param matterNumber
	 * @return
	 */
	public MatterDocListHeader getMatterDocListHeader (String languageId, Long classId, Long checkListNo,
			String matterNumber, String clientId) {
		Optional<MatterDocListHeader> matterDocListHeader =
				matterDocListHeaderRepository.findByLanguageIdAndClassIdAndCheckListNoAndMatterNumberAndClientIdAndDeletionIndicator(
						languageId, classId, checkListNo, matterNumber, clientId, 0L);
		if (matterDocListHeader != null) {
			return matterDocListHeader.get();
		}
		throw new BadRequestException("The given MatterDocListHeader ID : matterNumber: " + matterNumber + 
				"checkListNo: " + checkListNo + "," + 
				"clientId: " + clientId + "," + 
				"classId: " + classId + " doesn't exist.");
	}

	public MatterDocListHeader getMatterDocListHeader (Long matterHeaderId) {
		Optional<MatterDocListHeader> matterDocListHeader =
				matterDocListHeaderRepository.findByMatterHeaderIdAndDeletionIndicator(matterHeaderId, 0L);
		if (matterDocListHeader != null) {
			return matterDocListHeader.get();
		}
		throw new BadRequestException("The given MatterDocListHeader ID : " + matterHeaderId + " doesn't exist.");
	}

	public MatterDocListLine getMatterDocListLine (Long matterHeaderId, Long sequenceNumber) {
		Optional<MatterDocListLine> matterDocListLine =
				matterDocListLineRepository.findByMatterHeaderIdAndSequenceNumberAndDeletionIndicator(matterHeaderId, sequenceNumber, 0L);
		if (matterDocListLine != null) {
			return matterDocListLine.get();
		}
		throw new BadRequestException("The given MatterDocListLine Matter Header Id & SequenceNumber : " + matterHeaderId +"&"+ sequenceNumber + " doesn't exist.");
	}
	
	/**
	 * createMatterDocListHeader
	 * @param newMatterDocListHeader
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterDocListHeader createMatterDocListHeader (AddMatterDocListHeader newMatterDocListHeader, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
			MatterDocListHeader dbMatterDocListHeader = new MatterDocListHeader();
			BeanUtils.copyProperties(newMatterDocListHeader, dbMatterDocListHeader, CommonUtils.getNullPropertyNames(newMatterDocListHeader));
			// STATUS_ID	Hard Coded Value '22" 
			dbMatterDocListHeader.setStatusId(22L);
			dbMatterDocListHeader.setDeletionIndicator(0L);
			dbMatterDocListHeader.setCreatedBy(loginUserID);
			dbMatterDocListHeader.setUpdatedBy(loginUserID);
			dbMatterDocListHeader.setCreatedOn(new Date());
			dbMatterDocListHeader.setUpdatedOn(new Date());
			MatterDocListHeader savedMatterDocListHeader = matterDocListHeaderRepository.save(dbMatterDocListHeader);

			savedMatterDocListHeader.setMatterDocLists(new HashSet<>());
			if(newMatterDocListHeader.getMatterDocLists()!=null){
			for(AddMatterDocListLine newMatterDocListLine : newMatterDocListHeader.getMatterDocLists()){
			MatterDocListLine dbMatterDocListLine = new MatterDocListLine();
				BeanUtils.copyProperties(newMatterDocListLine, dbMatterDocListLine, CommonUtils.getNullPropertyNames(newMatterDocListLine));
				dbMatterDocListLine.setDeletionIndicator(0L);
				dbMatterDocListLine.setCreatedBy(loginUserID);
				dbMatterDocListLine.setUpdatedBy(loginUserID);
				dbMatterDocListLine.setCreatedOn(new Date());
				dbMatterDocListLine.setUpdatedOn(new Date());
				dbMatterDocListLine.setMatterNumber(savedMatterDocListHeader.getMatterNumber());
				dbMatterDocListLine.setMatterHeaderId(savedMatterDocListHeader.getMatterHeaderId());
				savedMatterDocListHeader.getMatterDocLists().add(matterDocListLineRepository.save(dbMatterDocListLine)) ;
				}
			}
		return savedMatterDocListHeader;
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
	public MatterDocListHeader updateMatterDocListHeader (UpdateMatterDocListHeader updateMatterDocListHeader,String languageId, Long classId, Long checkListNo,String matterNumber, String clientId, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
	    
		MatterDocListHeader dbMatterDocListHeader = getMatterDocListHeader (languageId, classId, checkListNo, matterNumber, clientId);
		//dbMatterDocListHeader.setStatusId(23L);
		dbMatterDocListHeader.setReceivedOn(new Date());
		dbMatterDocListHeader.setUpdatedBy(loginUserID);
		dbMatterDocListHeader.setUpdatedOn(new Date());
		MatterDocListHeader savedMatterDocListHeader = matterDocListHeaderRepository.save(dbMatterDocListHeader);

//		if(updateMatterDocListHeader.getMatterDocLists()!=null){
//			List<MatterDocListLine> nwMatterDocList = matterDocListLineRepository.findByMatterHeaderIdAndDeletionIndicator( matterHeaderId, 0L);
//			if(nwMatterDocList!=null){
//				for(MatterDocListLine newMatterDocList : nwMatterDocList){
//					newMatterDocList.setDeletionIndicator(1L);
//					newMatterDocList.setUpdatedBy(loginUserID);
//					newMatterDocList.setUpdatedOn(new Date());
//					matterDocListLineRepository.save(newMatterDocList);
//				}
//			}
//			savedMatterDocListHeader.setMatterDocLists(new HashSet<>());
		if(updateMatterDocListHeader.getMatterDocLists()!=null){
			for (AddMatterDocListLine newAddMatterDocList : updateMatterDocListHeader.getMatterDocLists()) {
				MatterDocListLine dbMatterDocList = getMatterDocListLine(savedMatterDocListHeader.getMatterHeaderId(), newAddMatterDocList.getSequenceNumber());
				if (dbMatterDocList != null) {
					dbMatterDocList.setDeletionIndicator(0L);
					dbMatterDocList.setCreatedOn(new Date());
					dbMatterDocList.setCreatedBy(loginUserID);
					dbMatterDocList.setUpdatedBy(loginUserID);
					dbMatterDocList.setUpdatedOn(new Date());
					dbMatterDocList.setMatterNumber(savedMatterDocListHeader.getMatterNumber());
					dbMatterDocList.setMatterHeaderId(savedMatterDocListHeader.getMatterHeaderId());
					MatterDocListLine savedMatterDocListLine = matterDocListLineRepository.save(dbMatterDocList);
					savedMatterDocListHeader.getMatterDocLists().add(savedMatterDocListLine);
				}
			}
		}

		return savedMatterDocListHeader;
	}

	public MatterDocListHeader updateMatterDocListHeader (UpdateMatterDocListHeader updateMatterDocListHeader,Long matterHeaderId, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		MatterDocListHeader dbMatterDocListHeader = getMatterDocListHeader (matterHeaderId);
		//dbMatterDocListHeader.setStatusId(23L);
		dbMatterDocListHeader.setReceivedOn(new Date());
		dbMatterDocListHeader.setUpdatedBy(loginUserID);
		dbMatterDocListHeader.setUpdatedOn(new Date());
		MatterDocListHeader savedMatterDocListHeader = matterDocListHeaderRepository.save(dbMatterDocListHeader);

//		if(updateMatterDocListHeader.getMatterDocLists()!=null){
//			List<MatterDocListLine> nwMatterDocList = matterDocListLineRepository.findByMatterHeaderIdAndDeletionIndicator( matterHeaderId, 0L);
//			if(nwMatterDocList!=null){
//				for(MatterDocListLine newMatterDocList : nwMatterDocList){
//					newMatterDocList.setDeletionIndicator(1L);
//					newMatterDocList.setUpdatedBy(loginUserID);
//					newMatterDocList.setUpdatedOn(new Date());
//					matterDocListLineRepository.save(newMatterDocList);
//				}
//			}
//			savedMatterDocListHeader.setMatterDocLists(new HashSet<>());
		if(updateMatterDocListHeader.getMatterDocLists()!=null){
			for (AddMatterDocListLine newAddMatterDocList : updateMatterDocListHeader.getMatterDocLists()) {
				MatterDocListLine dbMatterDocList = getMatterDocListLine(savedMatterDocListHeader.getMatterHeaderId(),newAddMatterDocList.getSequenceNumber());
				if (dbMatterDocList != null) {
					BeanUtils.copyProperties(newAddMatterDocList, dbMatterDocList, CommonUtils.getNullPropertyNames(newAddMatterDocList));
					dbMatterDocList.setDeletionIndicator(0L);
					dbMatterDocList.setCreatedOn(new Date());
					dbMatterDocList.setCreatedBy(loginUserID);
					dbMatterDocList.setUpdatedBy(loginUserID);
					dbMatterDocList.setUpdatedOn(new Date());
					dbMatterDocList.setMatterNumber(savedMatterDocListHeader.getMatterNumber());
					dbMatterDocList.setMatterHeaderId(savedMatterDocListHeader.getMatterHeaderId());
					MatterDocListLine savedMatterDocListLine = matterDocListLineRepository.save(dbMatterDocList);
					savedMatterDocListHeader.getMatterDocLists().add(savedMatterDocListLine);
				}
			}
		}

		return savedMatterDocListHeader;
	}
	
	/**
	 * deleteMatterDocListHeader
	 * @param loginUserID 
	 * @param matterNumber
	 */
	public void deleteMatterDocListHeader (String languageId, Long classId, Long checkListNo, String matterNumber, String clientId, String loginUserID) {
		MatterDocListHeader matterDocListHeader = getMatterDocListHeader (languageId, classId, checkListNo, matterNumber, clientId);
		if ( matterDocListHeader != null && matterDocListHeader.getStatusId() == 22L ) {
			matterDocListHeader.setDeletionIndicator(1L);
			matterDocListHeader.setUpdatedBy(loginUserID);
			matterDocListHeader.setUpdatedOn(new Date());
			matterDocListHeaderRepository.save(matterDocListHeader);
			List<MatterDocListLine> dbMatterDocList = matterDocListLineRepository.findByMatterNumberAndCheckListNoAndDeletionIndicator( matterNumber, checkListNo, 0L);
			if(dbMatterDocList!=null){
				for(MatterDocListLine newMatterDocList : dbMatterDocList){
					newMatterDocList.setDeletionIndicator(1L);
					newMatterDocList.setUpdatedBy(loginUserID);
					newMatterDocList.setUpdatedOn(new Date());
					matterDocListLineRepository.save(newMatterDocList);
				}
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + checkListNo + " as this record status is not 22.");
		}
	}
	public void deleteMatterDocListHeader ( Long matterHeaderId, String loginUserID) {
		MatterDocListHeader matterDocListHeader = getMatterDocListHeader (matterHeaderId);
		if ( matterDocListHeader != null && matterDocListHeader.getStatusId() == 22L ) {
			matterDocListHeader.setDeletionIndicator(1L);
			matterDocListHeader.setUpdatedBy(loginUserID);
			matterDocListHeader.setUpdatedOn(new Date());
			matterDocListHeaderRepository.save(matterDocListHeader);
			List<MatterDocListLine> dbMatterDocList = matterDocListLineRepository.findByMatterHeaderIdAndDeletionIndicator( matterHeaderId, 0L);
			if(dbMatterDocList!=null){
				for(MatterDocListLine newMatterDocList : dbMatterDocList){
					newMatterDocList.setDeletionIndicator(1L);
					newMatterDocList.setUpdatedBy(loginUserID);
					newMatterDocList.setUpdatedOn(new Date());
					matterDocListLineRepository.save(newMatterDocList);
				}
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterHeaderId + " as this record status is not 22.");
		}
	}
	/**
	 *
	 * @param searchMatterDocList
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<MatterDocListHeader> findMatterDocListHeader(FindMatterDocListHeader searchMatterDocList)
			throws ParseException, java.text.ParseException {
		if (searchMatterDocList.getStartCreatedOn() != null && searchMatterDocList.getStartCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterDocList.getStartCreatedOn(), searchMatterDocList.getEndCreatedOn());
			searchMatterDocList.setStartCreatedOn(dates[0]);
			searchMatterDocList.setEndCreatedOn(dates[1]);
		}
		MatterDocListHeaderSpecification spec = new MatterDocListHeaderSpecification(searchMatterDocList);
		List<MatterDocListHeader> results = matterDocListHeaderRepository.findAll(spec);
		log.info("results: " + results);
		return results;
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
