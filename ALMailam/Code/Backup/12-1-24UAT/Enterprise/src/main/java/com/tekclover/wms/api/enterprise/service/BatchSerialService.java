package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import com.tekclover.wms.api.enterprise.model.batchserial.*;
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
public class BatchSerialService {
	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private CompanyRepository companyRepository;
//	@Autowired
//	private LevelReferenceRepository levelReferenceRepository;

//	@Autowired
//	private LevelReferenceService levelReferenceService;
	@Autowired
	private BatchSerialRepository batchSerialRepository;

	/**
	 * getBatchSerials
	 *
	 * @return
	 */
	public List<BatchSerial> getBatchSerials() {
		List<BatchSerial> batchserialList = batchSerialRepository.findAll();
		log.info("batchserialList : " + batchserialList);
		batchserialList = batchserialList.stream().filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());

		String companyCodeId = null;
		String plantId = null;
		String warehouseId = null;
		Long levelId = null;
		IkeyValuePair ikeyValuePair = null;

		List<BatchSerial> newBatchSerialList = new ArrayList<>();

		for (BatchSerial dbBatchSerial : batchserialList) {

			if (companyCodeId != dbBatchSerial.getCompanyId() && plantId != dbBatchSerial.getPlantId() &&
					warehouseId != dbBatchSerial.getWarehouseId() && levelId != dbBatchSerial.getLevelId()) {

				companyCodeId = dbBatchSerial.getCompanyId();
				plantId = dbBatchSerial.getPlantId();
				warehouseId = dbBatchSerial.getWarehouseId();
				levelId = dbBatchSerial.getLevelId();

				ikeyValuePair = batchSerialRepository.getDescription(
						companyCodeId,
						plantId,
						warehouseId,
						dbBatchSerial.getLanguageId(),
						levelId);
			}
			if (ikeyValuePair != null) {
				dbBatchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getCompanyDescription());
				dbBatchSerial.setPlantIdAndDescription(ikeyValuePair.getPlantId() + "-" + ikeyValuePair.getPlantDescription());
				dbBatchSerial.setWarehouseIdAndDescription(ikeyValuePair.getWarehouseId() + "-" + ikeyValuePair.getWarehouseDescription());
				dbBatchSerial.setLevelIdAndDescription(ikeyValuePair.getLevelId() + "-" + ikeyValuePair.getLevelDescription());
			}
//				BeanUtils.copyProperties(dbBatchSerial, newBatchSerialOutput, CommonUtils.getNullPropertyNames(dbBatchSerial));
//
//						List<String> newLevelReferenceList = new ArrayList<>();
//			if(dbBatchSerial.getLevelReferences() != null) {
//				for (LevelReference dbLevelReference : dbBatchSerial.getLevelReferences()) {
//					newLevelReferenceList.add(dbLevelReference.getLevelReference());
//				}
//			}
//						newBatchSerialOutput.setLevelReference(newLevelReferenceList);

			newBatchSerialList.add(dbBatchSerial);
		}
		return newBatchSerialList;
	}

	/**
	 * getBatchSerial
	 *
	 * @param storageMethod
	 * @return
	 */
	public List<BatchSerial> getBatchSerial(String storageMethod, String plantId, String companyId,
											String languageId, String warehouseId, Long levelId,String maintenance) {

		List<BatchSerial> batchserial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndMaintenanceAndDeletionIndicator(
				storageMethod,
				plantId,
				companyId,
				languageId,
				warehouseId,
				levelId,
				maintenance,
				0L
		);
		if (batchserial.isEmpty()) {
			throw new BadRequestException("The StorageMethod is :" + storageMethod);
		}
		return batchserial;
}

//		BatchSerial newBatchSerial = new BatchSerial();
//		BeanUtils.copyProperties(batchserial.get(),newBatchSerial, CommonUtils.getNullPropertyNames(batchserial));
//		IkeyValuePair iKeyValuePair=companyRepository.getCompanyIdAndDescription(newBatchSerial.getCompanyId(), newBatchSerial.getLanguageId());
//		IkeyValuePair iKeyValuePair1=plantRepository.getPlantIdAndDescription(newBatchSerial.getPlantId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId());
//		IkeyValuePair iKeyValuePair2=warehouseRepository.getWarehouseIdAndDescription(newBatchSerial.getWarehouseId(), newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId());
//		IkeyValuePair ikeyValuePair3=batchSerialRepository.getLevelIdAndDescription(String.valueOf(newBatchSerial.getLevelId()),newBatchSerial.getLanguageId(), newBatchSerial.getCompanyId(), newBatchSerial.getPlantId(), newBatchSerial.getWarehouseId());
//		if(iKeyValuePair!=null) {
//			newBatchSerial.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
//		}
//		if(iKeyValuePair1!=null) {
//			newBatchSerial.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
//		}
//		if(iKeyValuePair2!=null) {
//			newBatchSerial.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
//		}
//		if(ikeyValuePair3!=null) {
//			newBatchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
//		}
//		return newBatchSerial;

	public List<BatchSerial> getBatchSerialOutput (String storageMethod,String plantId,String companyId,
												   String languageId,String warehouseId,Long levelId,String maintenance) {

		List<BatchSerial>batchSerialList=new ArrayList<>();

		List<BatchSerial> batchserial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndMaintenanceAndDeletionIndicator(
				storageMethod,
				plantId,
				companyId,
				languageId,
				warehouseId,
				levelId,
				maintenance,
				0L
		);
		if(batchserial.isEmpty()){
			throw new BadRequestException("The Batch Serial - storageMethod doesn't Exist :"+storageMethod);
		}
		for(BatchSerial dbBatchSerial:batchserial){

			IkeyValuePair ikeyValuePair = batchSerialRepository.getDescription(
					companyId,
					plantId,
					warehouseId,
					languageId,
					levelId);

			if(ikeyValuePair != null) {
				dbBatchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId()+"-"+ikeyValuePair.getCompanyDescription());
				dbBatchSerial.setPlantIdAndDescription(ikeyValuePair.getPlantId()+"-"+ikeyValuePair.getPlantDescription());
				dbBatchSerial.setWarehouseIdAndDescription(ikeyValuePair.getWarehouseId()+"-"+ikeyValuePair.getWarehouseDescription());
				dbBatchSerial.setLevelIdAndDescription(ikeyValuePair.getLevelId()+"-"+ikeyValuePair.getLevelDescription());
			}
			batchSerialList.add(dbBatchSerial);
		}
		return batchSerialList;
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

		String companyCodeId = null;
		String plantId = null;
		String warehouseId = null;
		Long levelId = null;
		IkeyValuePair ikeyValuePair = null;

		List<BatchSerial> newBatchSerialList =new ArrayList<>();

		for(BatchSerial dbBatchSerial:results) {

			if(companyCodeId != dbBatchSerial.getCompanyId() && plantId != dbBatchSerial.getPlantId() &&
					warehouseId != dbBatchSerial.getWarehouseId() && levelId != dbBatchSerial.getLevelId()) {

				companyCodeId = dbBatchSerial.getCompanyId();
				plantId = dbBatchSerial.getPlantId();
				warehouseId = dbBatchSerial.getWarehouseId();
				levelId = dbBatchSerial.getLevelId();

				ikeyValuePair = batchSerialRepository.getDescription(
						companyCodeId,
						plantId,
						warehouseId,
						dbBatchSerial.getLanguageId(),
						levelId);
			}
			if(ikeyValuePair != null) {
				dbBatchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId()+"-"+ikeyValuePair.getCompanyDescription());
				dbBatchSerial.setPlantIdAndDescription(ikeyValuePair.getPlantId()+"-"+ikeyValuePair.getPlantDescription());
				dbBatchSerial.setWarehouseIdAndDescription(ikeyValuePair.getWarehouseId()+"-"+ikeyValuePair.getWarehouseDescription());
				dbBatchSerial.setLevelIdAndDescription(ikeyValuePair.getLevelId()+"-"+ikeyValuePair.getLevelDescription());
			}
//			BeanUtils.copyProperties(dbBatchSerial, newBatchSerialOutput, CommonUtils.getNullPropertyNames(dbBatchSerial));
//
//			List<String> newLevelReferenceList = new ArrayList<>();
//			if(dbBatchSerial.getLevelReferences() != null) {
//				for (LevelReference dbLevelReference : dbBatchSerial.getLevelReferences()) {
//					newLevelReferenceList.add(dbLevelReference.getLevelReference());
//				}
//			}
//			newBatchSerialOutput.setLevelReference(newLevelReferenceList);

			newBatchSerialList.add(dbBatchSerial);
		}
		return newBatchSerialList;
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
//		dbBatchSerial.setCreatedOn(DateUtils.getCurrentKWTDateTime());
//		dbBatchSerial.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
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
//				dblevelReference.setCreatedOn(DateUtils.getCurrentKWTDateTime());
//				dblevelReference.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
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
	public List<BatchSerial> createBatchSerial (List<AddBatchSerial> newBatchSerial, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		List<BatchSerial> batchSerialList = new ArrayList<>();

		for (AddBatchSerial dbBatchSerial : newBatchSerial) {

			List<BatchSerial> duplicateBatchSerial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndLevelReferencesAndMaintenanceAndDeletionIndicator(
					dbBatchSerial.getStorageMethod(),
					dbBatchSerial.getPlantId(),
					dbBatchSerial.getCompanyId(),
					dbBatchSerial.getLanguageId(),
					dbBatchSerial.getWarehouseId(),
					dbBatchSerial.getLevelId(),
					dbBatchSerial.getLevelReferences(),
					dbBatchSerial.getMaintenance(),
					0L);

			if (!duplicateBatchSerial.isEmpty()) {

				throw new BadRequestException("The given values are getting duplicated.");

			} else {

				IkeyValuePair ikeyValuePair =
						companyRepository.getCompanyIdAndDescription(dbBatchSerial.getCompanyId(), dbBatchSerial.getLanguageId());

				IkeyValuePair ikeyValuePair1 =
						plantRepository.getPlantIdAndDescription(dbBatchSerial.getPlantId(),
								dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId());

				IkeyValuePair ikeyValuePair2 =
						warehouseRepository.getWarehouseIdAndDescription(dbBatchSerial.getWarehouseId(),
								dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(), dbBatchSerial.getPlantId());

				IkeyValuePair ikeyValuePair3 =
						batchSerialRepository.getLevelIdAndDescription(String.valueOf(dbBatchSerial.getLevelId()),
								dbBatchSerial.getLanguageId(), dbBatchSerial.getCompanyId(),
								dbBatchSerial.getPlantId(), dbBatchSerial.getWarehouseId());

				BatchSerial batchSerial = new BatchSerial();
				Long id = batchSerialRepository.getId();

				BeanUtils.copyProperties(dbBatchSerial, batchSerial, CommonUtils.getNullPropertyNames(dbBatchSerial));

				if (id != null) {

					batchSerial.setId(id);

				} else {

					batchSerial.setId(1L);
				}

				if (ikeyValuePair != null && ikeyValuePair1 != null &&
						ikeyValuePair2 != null && ikeyValuePair3 != null) {
					batchSerial.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getDescription());
					batchSerial.setPlantIdAndDescription(ikeyValuePair1.getPlantId() + "-" + ikeyValuePair1.getDescription());
					batchSerial.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId() + "-" + ikeyValuePair2.getDescription());
					batchSerial.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
				} else {
					throw new BadRequestException("The given values of Company Id "
							+ dbBatchSerial.getCompanyId() + " Plant Id "
							+ dbBatchSerial.getPlantId() + " Warehouse Id "
							+ dbBatchSerial.getWarehouseId() + " Level Id "
							+ dbBatchSerial.getLevelId() + " doesn't exist");
				}
				batchSerial.setDeletionIndicator(0L);
				batchSerial.setCreatedBy(loginUserID);
				batchSerial.setUpdatedBy(loginUserID);
				batchSerial.setCreatedOn(DateUtils.getCurrentKWTDateTime());
				batchSerial.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
				BatchSerial savedBatchSerial = batchSerialRepository.save(batchSerial);

//			savedBatchSerial.setLevelReferences(new HashSet<>());
//			if (dbBatchSerial.getLevelReferences() != null) {
//				for (LevelReference newLevelReference : dbBatchSerial.getLevelReferences()) {
//					LevelReference dblevelReference = new LevelReference();
//					BeanUtils.copyProperties(newLevelReference, dblevelReference, CommonUtils.getNullPropertyNames(newLevelReference));
//					dblevelReference.setDeletionIndicator(0L);
//					dblevelReference.setCreatedBy(loginUserID);
//					dblevelReference.setUpdatedBy(loginUserID);
//					dblevelReference.setCreatedOn(DateUtils.getCurrentKWTDateTime());
//					dblevelReference.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
//					dblevelReference.setStorageMethod(savedBatchSerial.getStorageMethod());
//					LevelReference savedLevelReference = levelReferenceRepository.save(dblevelReference);
//					savedBatchSerial.getLevelReferences().add(savedLevelReference);
//				}
//			}
				batchSerialList.add(savedBatchSerial);
			}
		}
		return batchSerialList;
	}



	/**
	 * updateBatchSerial
	 // * @param batchserialCode
	 * @param updateBatchSerial
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<BatchSerial> updateBatchSerial (String storageMethod,String companyId,String plantId,String languageId,String maintenance,
												String warehouseId,Long levelId, List<UpdateBatchSerial> updateBatchSerial, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {

		List<BatchSerial> batchSerialList = new ArrayList<>();

		for (UpdateBatchSerial newUpdateBatchSerial : updateBatchSerial) {

			if (newUpdateBatchSerial.getId() != null) {
				BatchSerial dbBatchSerial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndMaintenanceAndIdAndDeletionIndicator(
						storageMethod,
						plantId,
						companyId,
						languageId,
						warehouseId,
						levelId,
						maintenance,
						newUpdateBatchSerial.getId(),
						0L);

				if (dbBatchSerial != null) {

					BeanUtils.copyProperties(newUpdateBatchSerial, dbBatchSerial, CommonUtils.getNullPropertyNames(newUpdateBatchSerial));
					dbBatchSerial.setUpdatedBy(loginUserID);
					dbBatchSerial.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
					batchSerialList.add(batchSerialRepository.save(dbBatchSerial));
				}
				else {
					throw new EntityNotFoundException("The given values of companyCodeId " + companyId +
							" plantId " + plantId +
							" languageId " + languageId +
							" warehouseId " + warehouseId +
							" levelId " + levelId +
							" storageMethod " + storageMethod +
							" maintenance " + maintenance + "doesn't exists ");
				}
			}else {
					Long id = batchSerialRepository.getId();
					BatchSerial newBatchSerial = new BatchSerial();

					List<BatchSerial> duplicateBatchSerial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndLevelReferencesAndMaintenanceAndDeletionIndicator(
							newUpdateBatchSerial.getStorageMethod(),
							newUpdateBatchSerial.getPlantId(),
							newUpdateBatchSerial.getCompanyId(),
							newUpdateBatchSerial.getLanguageId(),
							newUpdateBatchSerial.getWarehouseId(),
							newUpdateBatchSerial.getLevelId(),
							newUpdateBatchSerial.getLevelReferences(),
							newUpdateBatchSerial.getMaintenance(),
							0L
					);

					if (!duplicateBatchSerial.isEmpty()) {

						throw new EntityNotFoundException("Record is Getting Duplicated");

					} else {

						BeanUtils.copyProperties(newUpdateBatchSerial, newBatchSerial, CommonUtils.getNullPropertyNames(newUpdateBatchSerial));
						newBatchSerial.setId(id);
						newBatchSerial.setDeletionIndicator(0L);
						newBatchSerial.setUpdatedBy(loginUserID);
						newBatchSerial.setUpdatedOn(DateUtils.getCurrentKWTDateTime());

						batchSerialList.add(batchSerialRepository.save(newBatchSerial));

//					if (updateBatchSerial1.getLevelReferences() != null) {
//						if (levelReferenceService.getLevelReferences(storageMethod) != null) {
//							levelReferenceService.deleteLevelReferences(storageMethod, loginUserID);
//						}
//						for (LevelReference newLevelReference : updateBatchSerial1.getLevelReferences()) {
//							LevelReference dbLevelReference = new LevelReference();
//							BeanUtils.copyProperties(newLevelReference, dbLevelReference, CommonUtils.getNullPropertyNames(newLevelReference));
//							dbLevelReference.setDeletionIndicator(0L);
//							dbLevelReference.setCreatedOn(DateUtils.getCurrentKWTDateTime());
//							dbLevelReference.setCreatedBy(loginUserID);
//							dbLevelReference.setUpdatedBy(loginUserID);
//							dbLevelReference.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
//							dbLevelReference.setStorageMethod(savedBatchSerial.getStorageMethod());
//							LevelReference savedLevelReference = levelReferenceRepository.save(dbLevelReference);
//							savedBatchSerial.getLevelReferences().add(savedLevelReference);
//						}
//					}

					}
				}
			}

		return batchSerialList;
	}

	/**
	 * deleteBatchSerial
	 // * @param batchserialCode
	 */
	public void deleteBatchSerial (String storageMethod,String companyId,String languageId,String plantId,
								   String warehouseId,Long levelId,String maintenance,String loginUserID) throws ParseException {
		List<BatchSerial> dbBatchSerial = batchSerialRepository.findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndMaintenanceAndDeletionIndicator(
				storageMethod,
				plantId,
				companyId,
				languageId,
				warehouseId,
				levelId,
				maintenance,
				0L);

		if ( dbBatchSerial != null) {
			for(BatchSerial batchSerial:dbBatchSerial) {
				batchSerial.setDeletionIndicator(1L);
				batchSerial.setUpdatedBy(loginUserID);
				batchSerial.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
				batchSerialRepository.save(batchSerial);
//				if (levelReferenceService.getLevelReferences(storageMethod) != null) {
//					levelReferenceService.deleteLevelReferences(storageMethod, loginUserID);
//				}
			}
		} else {
			throw new EntityNotFoundException(String.valueOf(storageMethod));
		}
	}
}
