package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import com.tekclover.wms.api.enterprise.model.batchserial.*;
import com.tekclover.wms.api.enterprise.model.floor.Floor;
import com.tekclover.wms.api.enterprise.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.enterprise.controller.exception.BadRequestException;
import com.tekclover.wms.api.enterprise.repository.specification.BatchSerialSpecification;
import com.tekclover.wms.api.enterprise.util.CommonUtils;
import com.tekclover.wms.api.enterprise.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BatchSerialService{
	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private LevelReferenceRepository levelReferenceRepository;

	@Autowired
	private LevelReferenceService levelReferenceService;
	@Autowired
	private BatchSerialRepository batchSerialRepository;

	/**
	 * getBatchSerials
	 * @return
	 */
	public List<BatchSerial> getBatchSerials () {
		List<BatchSerial> batchserialList = batchSerialRepository.findAll();
		log.info("batchserialList : " + batchserialList);
		batchserialList = batchserialList.stream().filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<BatchSerial> newBatchSerial=new ArrayList<>();
		for(BatchSerial dbBatchSerial:batchserialList) {
			if (dbBatchSerial.getCompanyIdAndDescription() != null&&dbBatchSerial.getPlantIdAndDescription()!=null&&dbBatchSerial.getWarehouseIdAndDescription()!=null&&dbBatchSerial.getLevelIdAndDescription()!=null) {
				IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(dbBatchSerial.getCompanyId(), dbBatchSerial.getLanguageId());
				IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(dbBatchSerial.getPlantId(), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId());
				IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(dbBatchSerial.getWarehouseId(), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(), dbBatchSerial.getPlantId());
				IkeyValuePair ikeyValuePair3 = batchSerialRepository.getLevelIdAndDescription(String.valueOf(dbBatchSerial.getLevelId()), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(), dbBatchSerial.getPlantId(), dbBatchSerial.getWarehouseId());
				if (iKeyValuePair != null) {
					dbBatchSerial.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbBatchSerial.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbBatchSerial.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
				}
			}

			newBatchSerial.add(dbBatchSerial);
		}
		return newBatchSerial;
	}

	/**
	 * getBatchSerial
	 * @param storageMethod
	 * @return
	 */
	public BatchSerial getBatchSerial (String storageMethod,String plantId,String companyId,String languageId,String warehouseId,Long levelId) {
		Optional<BatchSerial> batchserial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndDeletionIndicator(
				storageMethod,
				plantId,
				companyId,
				languageId,
				warehouseId,
				levelId,
				0L
		);
		if(batchserial.isEmpty()){
			throw new BadRequestException("The StorageMethod is :"+storageMethod);
		}
		BatchSerial newBatchSerial = new BatchSerial();
		BeanUtils.copyProperties(batchserial.get(),newBatchSerial, CommonUtils.getNullPropertyNames(batchserial));
		IkeyValuePair iKeyValuePair=companyRepository.getCompanyIdAndDescription(newBatchSerial.getCompanyId(), newBatchSerial.getLanguageId());
		IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(newBatchSerial.getPlantId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId());
		IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(newBatchSerial.getWarehouseId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId());
		IkeyValuePair ikeyValuePair3=batchSerialRepository.getLevelIdAndDescription(String.valueOf(newBatchSerial.getLevelId()),newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId(), newBatchSerial.getWarehouseId());
		if(iKeyValuePair!=null) {
			newBatchSerial.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
		}
		if(iKeyValuePair1!=null) {
			newBatchSerial.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
		}
		if(iKeyValuePair2!=null) {
			newBatchSerial.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
		}
		if(ikeyValuePair3!=null) {
			newBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
		}
		return newBatchSerial;
	}

	/**
	 * findBatchSerial
	 * @param searchBatchSerial
	 * @return
	 * @throws Exception
	 */
	public List<BatchSerial> findBatchSerial(SearchBatchSerial searchBatchSerial) throws Exception {
		if (searchBatchSerial.getStartCreatedOn() != null && searchBatchSerial.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchBatchSerial.getStartCreatedOn(), searchBatchSerial.getEndCreatedOn());
			searchBatchSerial.setStartCreatedOn(dates[0]);
			searchBatchSerial.setEndCreatedOn(dates[1]);
		}

		BatchSerialSpecification spec = new BatchSerialSpecification(searchBatchSerial);
		List<BatchSerial> results = batchSerialRepository.findAll(spec);
		log.info("results: " + results);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<BatchSerial> newBatchSerial=new ArrayList<>();
		for(BatchSerial dbBatchSerial:results) {
			if (dbBatchSerial.getCompanyIdAndDescription() != null&&dbBatchSerial.getPlantIdAndDescription()!=null&&dbBatchSerial.getWarehouseIdAndDescription()!=null&&dbBatchSerial.getLevelIdAndDescription()!=null) {
				IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(dbBatchSerial.getCompanyId(), dbBatchSerial.getLanguageId());
				IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(dbBatchSerial.getPlantId(), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId());
				IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(dbBatchSerial.getWarehouseId(), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(), dbBatchSerial.getPlantId());
				IkeyValuePair ikeyValuePair3= batchSerialRepository.getLevelIdAndDescription(String.valueOf(dbBatchSerial.getLevelId()), dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(), dbBatchSerial.getPlantId(), dbBatchSerial.getWarehouseId());
				if (iKeyValuePair != null) {
					dbBatchSerial.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbBatchSerial.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbBatchSerial.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
				}
			}
			newBatchSerial.add(dbBatchSerial);
		}
		return newBatchSerial;
	}
//	public BatchSerial createBatchSerial (AddBatchSerial newBatchSerial, String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//		Optional<BatchSerial>duplicateBatchSerial=batchSerialRepository.findByStorageMethodAndDeletionIndicator(
//				newBatchSerial.getStorageMethod(),
//				0L);
//		if(!duplicateBatchSerial.isEmpty()){
//			throw new BadRequestException("The given values are getting duplicated.");
//		}
//		IkeyValuePair ikeyValuePair=companyRepository.getCompanyIdAndDescription(newBatchSerial.getCompanyId(), newBatchSerial.getLanguageId());
//		IkeyValuePair ikeyValuePair1=plantRepository.getPlantIdAndDescription(newBatchSerial.getPlantId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId());
//		IkeyValuePair ikeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(newBatchSerial.getWarehouseId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId());
//		IkeyValuePair ikeyValuePair3=batchSerialRepository.getLevelIdAndDescription(String.valueOf(newBatchSerial.getLevelId()), newBatchSerial.getLanguageId());
//		BatchSerial dbBatchSerial = new BatchSerial();
//		BeanUtils.copyProperties(newBatchSerial, dbBatchSerial, CommonUtils.getNullPropertyNames(newBatchSerial));
//		dbBatchSerial.setDeletionIndicator(0L);
//		dbBatchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId()+"-"+ikeyValuePair.getDescription());
//		dbBatchSerial.setPlantIdAndDescription(ikeyValuePair1.getPlantId()+"-"+ikeyValuePair1.getDescription());
//		dbBatchSerial.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId()+"-"+ikeyValuePair2.getDescription());
//		dbBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId()+"-"+ikeyValuePair3.getDescription());
//		dbBatchSerial.setCreatedBy(loginUserID);
//		dbBatchSerial.setUpdatedBy(loginUserID);
//		dbBatchSerial.setCreatedOn(new Date());
//		dbBatchSerial.setUpdatedOn(new Date());
//		BatchSerial savedBatchSerial=batchSerialRepository.save(dbBatchSerial);
//
//		savedBatchSerial.setLevelReferences(new HashSet<>());
//		if(newBatchSerial.getLevelReferences()!=null){
//			for(LevelReference newLevelReference:newBatchSerial.getLevelReferences()){
//				LevelReference dblevelReference=new LevelReference();
//				BeanUtils.copyProperties(newLevelReference, dblevelReference, CommonUtils.getNullPropertyNames(newLevelReference));
//				dblevelReference.setDeletionIndicator(0L);
//				dblevelReference.setCreatedBy(loginUserID);
//				dblevelReference.setUpdatedBy(loginUserID);
//				dblevelReference.setCreatedOn(new Date());
//				dblevelReference.setUpdatedOn(new Date());
//				dblevelReference.setStorageMethod(savedBatchSerial.getStorageMethod());
//				LevelReference savedLevelReference = levelReferenceRepository.save(dblevelReference);
//				savedBatchSerial.getLevelReferences().add(savedLevelReference);
//			}
//
//		}
//		return savedBatchSerial;
//	}

	/**
	 * createBatchSerial
	 * @param newBatchSerial
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BatchSerial createBatchSerial (AddBatchSerial newBatchSerial, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<BatchSerial>duplicateBatchSerial=batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndDeletionIndicator(
				newBatchSerial.getStorageMethod(),
				newBatchSerial.getPlantId(),
				newBatchSerial.getCompanyId(),
				newBatchSerial.getLanguageId(),
				newBatchSerial.getWarehouseId(),
				newBatchSerial.getLevelId(),
				0L);
		if(!duplicateBatchSerial.isEmpty()){
			throw new BadRequestException("The given values are getting duplicated.");
		}
		IkeyValuePair ikeyValuePair=companyRepository.getCompanyIdAndDescription(newBatchSerial.getCompanyId(), newBatchSerial.getLanguageId());
		IkeyValuePair ikeyValuePair1=plantRepository.getPlantIdAndDescription(newBatchSerial.getPlantId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId());
		IkeyValuePair ikeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(newBatchSerial.getWarehouseId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId());
		IkeyValuePair ikeyValuePair3=batchSerialRepository.getLevelIdAndDescription(String.valueOf(newBatchSerial.getLevelId()), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId(), newBatchSerial.getWarehouseId());
		BatchSerial dbBatchSerial = new BatchSerial();
		BeanUtils.copyProperties(newBatchSerial, dbBatchSerial, CommonUtils.getNullPropertyNames(newBatchSerial));

		if(ikeyValuePair != null && ikeyValuePair1 != null &&
				ikeyValuePair2 != null && ikeyValuePair3 != null) {
			dbBatchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getDescription());
			dbBatchSerial.setPlantIdAndDescription(ikeyValuePair1.getPlantId() + "-" + ikeyValuePair1.getDescription());
			dbBatchSerial.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId() + "-" + ikeyValuePair2.getDescription());
			dbBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
		}
		else {
			throw new BadRequestException("The given values of Company Id "
			+ newBatchSerial.getCompanyId() + " Plant Id "
			+ newBatchSerial.getPlantId() + " Warehouse Id "
			+ newBatchSerial.getWarehouseId() + " Level Id "
			+ newBatchSerial.getLevelId() + " doesn't exist");
		}
		dbBatchSerial.setDeletionIndicator(0L);
		dbBatchSerial.setCreatedBy(loginUserID);
		dbBatchSerial.setUpdatedBy(loginUserID);
		dbBatchSerial.setCreatedOn(new Date());
		dbBatchSerial.setUpdatedOn(new Date());
		BatchSerial savedBatchSerial=batchSerialRepository.save(dbBatchSerial);

		savedBatchSerial.setLevelReferences(new HashSet<>());
         if(newBatchSerial.getLevelReferences()!=null){
         for(LevelReference newLevelReference:newBatchSerial.getLevelReferences()){
             LevelReference dblevelReference=new LevelReference();
			 BeanUtils.copyProperties(newLevelReference, dblevelReference, CommonUtils.getNullPropertyNames(newLevelReference));
			 dblevelReference.setDeletionIndicator(0L);
			 dblevelReference.setCreatedBy(loginUserID);
			 dblevelReference.setUpdatedBy(loginUserID);
			 dblevelReference.setCreatedOn(new Date());
			 dblevelReference.setUpdatedOn(new Date());
			 dblevelReference.setStorageMethod(savedBatchSerial.getStorageMethod());
			 LevelReference savedLevelReference = levelReferenceRepository.save(dblevelReference);
			 savedBatchSerial.getLevelReferences().add(savedLevelReference);
		 }

		 }
		return savedBatchSerial;
	}

	/**
	 * updateBatchSerial
	 // * @param batchserialCode
	 * @param updateBatchSerial
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BatchSerial updateBatchSerial (String storageMethod,String companyId,String plantId,String languageId,String warehouseId,Long levelId, UpdateBatchSerial updateBatchSerial, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		BatchSerial dbBatchSerial = getBatchSerial(storageMethod,plantId,companyId,languageId,warehouseId,levelId);
		BeanUtils.copyProperties(updateBatchSerial, dbBatchSerial, CommonUtils.getNullPropertyNames(updateBatchSerial));
		dbBatchSerial.setUpdatedBy(loginUserID);
		dbBatchSerial.setUpdatedOn(new Date());
		BatchSerial savedBatchSerial = batchSerialRepository.save(dbBatchSerial);

		if (updateBatchSerial.getLevelReferences()!= null) {
			if (levelReferenceService.getLevelReferences(storageMethod) != null) {
				levelReferenceService.deleteLevelReferences(storageMethod, loginUserID);
			}
			for (LevelReference newLevelReference : updateBatchSerial.getLevelReferences()) {
				LevelReference dbLevelReference = new LevelReference();
				BeanUtils.copyProperties(newLevelReference, dbLevelReference, CommonUtils.getNullPropertyNames(newLevelReference));
				dbLevelReference.setDeletionIndicator(0L);
				dbLevelReference.setCreatedOn(new Date());
				dbLevelReference.setCreatedBy(loginUserID);
				dbLevelReference.setUpdatedBy(loginUserID);
				dbLevelReference.setUpdatedOn(new Date());
				dbLevelReference.setStorageMethod(savedBatchSerial.getStorageMethod());
				LevelReference savedLevelReference = levelReferenceRepository.save(dbLevelReference);
				savedBatchSerial.getLevelReferences().add(savedLevelReference);
			}
		}
		return savedBatchSerial;
	}


	/**
	 * deleteBatchSerial
	 // * @param batchserialCode
	 */
	public void deleteBatchSerial (String storageMethod,String companyId,String languageId,String plantId,String warehouseId,Long levelId,String loginUserID) {
		BatchSerial batchSerial = getBatchSerial(storageMethod,plantId,companyId,languageId,warehouseId,levelId);
		if ( batchSerial != null) {
			batchSerial.setDeletionIndicator (1L);
			batchSerial.setUpdatedBy(loginUserID);
			batchSerial.setUpdatedOn(new Date());
			batchSerialRepository.save(batchSerial);
			if(levelReferenceService.getLevelReferences(storageMethod)!=null){
              levelReferenceService.deleteLevelReferences(storageMethod,loginUserID);
			}
		} else {
			throw new EntityNotFoundException(String.valueOf(storageMethod));
		}
	}
}
