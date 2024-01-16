package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import com.tekclover.wms.api.enterprise.model.variant.*;
import com.tekclover.wms.api.enterprise.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.enterprise.controller.exception.BadRequestException;
import com.tekclover.wms.api.enterprise.repository.specification.VariantSpecification;
import com.tekclover.wms.api.enterprise.util.CommonUtils;
import com.tekclover.wms.api.enterprise.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VariantService {
	@Autowired
	private BatchSerialRepository batchSerialRepository;
	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private LevelReferenceVariantService levelReferenceVariantService;

	@Autowired
	private LevelReferenceVariantRepository levelReferenceVariantRepository;
	@Autowired
	private VariantRepository variantRepository;

	/**
	 * getVariants
	 *
	 * @return
	 */
	public List<Variant> getVariants() {
		List<Variant> variantList = variantRepository.findAll();
		log.info("variantList : " + variantList);
		variantList = variantList.stream().filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<Variant> newVariant = new ArrayList<>();
		for (Variant dbVariant : variantList) {
			if (dbVariant.getCompanyIdAndDescription() != null && dbVariant.getPlantIdAndDescription() != null && dbVariant.getWarehouseIdAndDescription() != null) {
				IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(dbVariant.getCompanyId(), dbVariant.getLanguageId());
				IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(dbVariant.getPlantId(), dbVariant.getLanguageId(), dbVariant.getCompanyId());
				IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(dbVariant.getWarehouseId(), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId());
				IkeyValuePair ikeyValuePair3 = variantRepository.getVariantIdAndDescription(dbVariant.getVariantId(), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId(), dbVariant.getWarehouseId());
				IkeyValuePair ikeyValuePair4 = batchSerialRepository.getLevelIdAndDescription(String.valueOf(dbVariant.getLevelId()), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId(), dbVariant.getWarehouseId());
				if (iKeyValuePair != null) {
					dbVariant.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbVariant.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbVariant.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair4 != null) {
					dbVariant.setLevelIdAndDescription(ikeyValuePair4.getLevelId() + "-" + ikeyValuePair4.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbVariant.setDescription(ikeyValuePair3.getDescription());
				}
			}
			newVariant.add(dbVariant);
		}
		return newVariant;
	}

	/**
	 * getVariant
	 *
	 * @param variantId
	 * @return
	 */
	public Variant getVariant(String variantId,String companyId,String languageId,String plantId,String warehouseId,Long levelId) {
		Optional<Variant> variant = variantRepository.findByVariantIdAndLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndLevelIdAndDeletionIndicator(variantId,languageId,companyId,plantId,warehouseId,levelId,0L);
		if (variant.isEmpty()) {
			throw new BadRequestException("The given Values of "+"Variant ID : " + variantId +
					"companyId" + companyId +
					"plantId" + plantId +
					"warehouseId" + warehouseId +
					"languageId" + languageId +
					"levelId" + levelId +" doesn't exist.");
		}
		Variant newVariant = new Variant();
		BeanUtils.copyProperties(variant.get(), newVariant, CommonUtils.getNullPropertyNames(variant));
		IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(newVariant.getCompanyId(), newVariant.getLanguageId());
		IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(newVariant.getPlantId(), newVariant.getLanguageId(), newVariant.getCompanyId());
		IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(newVariant.getWarehouseId(), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId());
		IkeyValuePair ikeyValuePair3 = variantRepository.getVariantIdAndDescription(newVariant.getVariantId(), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId(), newVariant.getWarehouseId());
		IkeyValuePair ikeyValuePair4 = batchSerialRepository.getLevelIdAndDescription(String.valueOf(newVariant.getLevelId()), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId(), newVariant.getWarehouseId());
		if(iKeyValuePair!=null) {
			newVariant.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
		}
		if(iKeyValuePair1!=null) {
			newVariant.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
		}
		if(iKeyValuePair2!=null) {
			newVariant.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
		}
		if(ikeyValuePair4!=null) {
			newVariant.setLevelIdAndDescription(ikeyValuePair4.getLevelId() + "-" + ikeyValuePair4.getDescription());
		}
		if(ikeyValuePair3!=null) {
			newVariant.setDescription(ikeyValuePair3.getDescription());
		}
		return newVariant;
	}

	/**
	 * findVariant
	 *
	 * @param searchVariant
	 * @return
	 */
	public List<Variant> findVariant(SearchVariant searchVariant) throws Exception {
		if (searchVariant.getStartCreatedOn() != null && searchVariant.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchVariant.getStartCreatedOn(), searchVariant.getEndCreatedOn());
			searchVariant.setStartCreatedOn(dates[0]);
			searchVariant.setEndCreatedOn(dates[1]);
		}

		VariantSpecification spec = new VariantSpecification(searchVariant);
		List<Variant> results = variantRepository.findAll(spec);
		log.info("results: " + results);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<Variant> newVariant = new ArrayList<>();
		for (Variant dbVariant : results) {
			if (dbVariant.getCompanyIdAndDescription() != null && dbVariant.getPlantIdAndDescription() != null && dbVariant.getWarehouseIdAndDescription() != null) {
				IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(dbVariant.getCompanyId(), dbVariant.getLanguageId());
				IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(dbVariant.getPlantId(), dbVariant.getLanguageId(), dbVariant.getCompanyId());
				IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(dbVariant.getWarehouseId(), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId());
				IkeyValuePair ikeyValuePair3 = variantRepository.getVariantIdAndDescription(dbVariant.getVariantId(), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId(), dbVariant.getWarehouseId());
				IkeyValuePair ikeyValuePair4 = batchSerialRepository.getLevelIdAndDescription(String.valueOf(dbVariant.getLevelId()), dbVariant.getLanguageId(), dbVariant.getCompanyId(), dbVariant.getPlantId(), dbVariant.getWarehouseId());
				if (iKeyValuePair != null) {
					dbVariant.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbVariant.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbVariant.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair4 != null) {
					dbVariant.setLevelIdAndDescription(ikeyValuePair4.getLevelId() + "-" + ikeyValuePair4.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbVariant.setDescription(ikeyValuePair3.getDescription());
				}
			}
			newVariant.add(dbVariant);
		}
		return newVariant;

	}

	/**
	 * createVariant
	 *
	 * @param newVariant
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Variant createVariant(AddVariant newVariant, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<Variant> duplicateVariant = variantRepository.findByVariantIdAndLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndLevelIdAndDeletionIndicator(newVariant.getVariantId(), newVariant.getLanguageId(), newVariant.getCompanyId(),
				newVariant.getPlantId(), newVariant.getWarehouseId(), newVariant.getLevelId(), 0L);
		if (!duplicateVariant.isEmpty()) {
			throw new EntityNotFoundException("Record is Getting Duplicated");
		} else {
			IkeyValuePair ikeyValuePair = companyRepository.getCompanyIdAndDescription(newVariant.getCompanyId(), newVariant.getLanguageId());
			IkeyValuePair ikeyValuePair1 = plantRepository.getPlantIdAndDescription(newVariant.getPlantId(), newVariant.getLanguageId(), newVariant.getCompanyId());
			IkeyValuePair ikeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(newVariant.getWarehouseId(), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId());
			IkeyValuePair ikeyValuePair3 = batchSerialRepository.getLevelIdAndDescription(String.valueOf(newVariant.getLevelId()), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId(), newVariant.getWarehouseId());
			IkeyValuePair ikeyValuePair4 = variantRepository.getVariantIdAndDescription(newVariant.getVariantId(), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId(), newVariant.getWarehouseId());
			Variant dbVariant = new Variant();
			BeanUtils.copyProperties(newVariant, dbVariant, CommonUtils.getNullPropertyNames(newVariant));

			if(ikeyValuePair != null && ikeyValuePair1 != null && ikeyValuePair2 != null &&
					ikeyValuePair3 != null && ikeyValuePair4 != null) {

				dbVariant.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getDescription());
				dbVariant.setPlantIdAndDescription(ikeyValuePair1.getPlantId() + "-" + ikeyValuePair1.getDescription());
				dbVariant.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId() + "-" + ikeyValuePair2.getDescription());
				dbVariant.setLevelIdAndDescription(ikeyValuePair3.getLevelId() + "-" + ikeyValuePair3.getDescription());
				dbVariant.setDescription(ikeyValuePair4.getDescription());
				dbVariant.setVariantId(ikeyValuePair4.getVariantId());
			}
			else {
				throw new BadRequestException("The given values of Company Id "
						+ newVariant.getCompanyId() + " Plant Id "
						+ newVariant.getPlantId() + " Warehouse Id "
						+ newVariant.getWarehouseId() + " Level Id "
						+ newVariant.getLevelId() + " Variant Id "
						+ newVariant.getVariantId() + " doesn't exist");
			}

			dbVariant.setDeletionIndicator(0L);
			dbVariant.setCreatedBy(loginUserID);
			dbVariant.setUpdatedBy(loginUserID);
			dbVariant.setCreatedOn(new Date());
			dbVariant.setUpdatedOn(new Date());
			Variant savedVariant = variantRepository.save(dbVariant);

			savedVariant.setLevelReferenceVariants(new HashSet<>());
			if (newVariant.getLevelReferenceVariants() != null) {
				for (LevelReferenceVariant newLevelReference : newVariant.getLevelReferenceVariants()) {
					LevelReferenceVariant dblevelReferenceVariant = new LevelReferenceVariant();
					BeanUtils.copyProperties(newLevelReference, dblevelReferenceVariant, CommonUtils.getNullPropertyNames(newLevelReference));
					dblevelReferenceVariant.setDeletionIndicator(0L);
					dblevelReferenceVariant.setCreatedBy(loginUserID);
					dblevelReferenceVariant.setUpdatedBy(loginUserID);
					dblevelReferenceVariant.setCreatedOn(new Date());
					dblevelReferenceVariant.setUpdatedOn(new Date());
					dblevelReferenceVariant.setVariantId(savedVariant.getVariantId());
					LevelReferenceVariant savedLevelReferenceVariant = levelReferenceVariantRepository.save(dblevelReferenceVariant);
					savedVariant.getLevelReferenceVariants().add(savedLevelReferenceVariant);
				}

			}
			return savedVariant;
		}
	}


//	}public List<Variant> createVariant (List<AddVariant> newVariant, String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//		Variant dbVariant = new Variant();
//		Optional<Variant> duplicateVariant = variantRepository.findByVariantId(dbVariant.getVariantId());
//		if (!duplicateVariant.isEmpty()) {
//			throw new EntityNotFoundException("Record is Getting Duplicated");
//		} else {
//			BeanUtils.copyProperties(newVariant, dbVariant, CommonUtils.getNullPropertyNames(newVariant));
//			dbVariant.setDeletionIndicator(0L);
//			IkeyValuePair ikeyValuePair = companyRepository.getCompanyIdAndDescription(newVariant.getCompanyId(), newVariant.getLanguageId());
//			IkeyValuePair ikeyValuePair1 = plantRepository.getPlantIdAndDescription(newVariant.getPlantId(), newVariant.getLanguageId(), newVariant.getCompanyId());
//			IkeyValuePair ikeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(newVariant.getWarehouseId(), newVariant.getLanguageId(), newVariant.getCompanyId(), newVariant.getPlantId());
//			IkeyValuePair ikeyValuePair3= batchSerialRepository.getLevelIdAndDescription(String.valueOf(newVariant.getLevelId()), newVariant.getLanguageId());
//			dbVariant.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getDescription());
//			dbVariant.setPlantIdAndDescription(ikeyValuePair1.getPlantId() + "-" + ikeyValuePair1.getDescription());
//			dbVariant.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId() + "-" + ikeyValuePair2.getDescription());
//			dbVariant.setLevelIdAndDescription(ikeyValuePair3.getLevelId()+"-"+ikeyValuePair3.getDescription());
//			dbVariant.setCreatedBy(loginUserID);
//			dbVariant.setUpdatedBy(loginUserID);
//			dbVariant.setCreatedOn(new Date());
//			dbVariant.setUpdatedOn(new Date());
//			return variantRepository.save(dbVariant);
//		}
//	}

	/**
	 * updateVariant
	 *
	 * @param variantId
	 * @param updateVariant
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Variant updateVariant(String variantId,String companyId,String languageId,String plantId,String warehouseId,Long levelId,UpdateVariant updateVariant, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Variant dbVariant = getVariant(variantId,companyId,languageId,plantId,warehouseId,levelId);
		BeanUtils.copyProperties(updateVariant, dbVariant, CommonUtils.getNullPropertyNames(updateVariant));
		dbVariant.setUpdatedBy(loginUserID);
		dbVariant.setUpdatedOn(new Date());
		Variant savedVariant = variantRepository.save(dbVariant);
		if (updateVariant.getLevelReferenceVariants() != null) {
			if (levelReferenceVariantService.getLevelReferenceVariant(variantId) != null) {
				levelReferenceVariantService.deleteLevelReferences(variantId, loginUserID);
			}
			for (LevelReferenceVariant newLevelReferenceVariant : updateVariant.getLevelReferenceVariants()) {
				LevelReferenceVariant dbLevelReference = new LevelReferenceVariant();
				BeanUtils.copyProperties(newLevelReferenceVariant, dbLevelReference, CommonUtils.getNullPropertyNames(newLevelReferenceVariant));
				dbLevelReference.setDeletionIndicator(0L);
				dbLevelReference.setCreatedOn(new Date());
				dbLevelReference.setCreatedBy(loginUserID);
				dbLevelReference.setUpdatedBy(loginUserID);
				dbLevelReference.setUpdatedOn(new Date());
				dbLevelReference.setVariantId(savedVariant.getVariantId());
				LevelReferenceVariant savedLevelReference = levelReferenceVariantRepository.save(dbLevelReference);
				savedVariant.getLevelReferenceVariants().add(savedLevelReference);
			}
		}
		return savedVariant;

	}

	/**
	 * deleteVariant
	 *
	 * @param variantId
	 */
	public void deleteVariant(String variantId,String companyId,String languageId,String plantId,String warehouseId,Long levelId,String loginUserID) {
		Variant variant = getVariant(variantId,companyId,languageId,plantId,warehouseId,levelId);
		if (variant != null) {
			variant.setDeletionIndicator(1L);
			variant.setUpdatedBy(loginUserID);
			variant.setUpdatedOn(new Date());
			variantRepository.save(variant);
			//batchSerialRepository.save(batchSerial);
			if (levelReferenceVariantService.getLevelReferenceVariant(variantId) != null) {
				levelReferenceVariantService.deleteLevelReferences(variantId, loginUserID);
			}
		}else {
			throw new EntityNotFoundException(variantId);
		}
	}
}
