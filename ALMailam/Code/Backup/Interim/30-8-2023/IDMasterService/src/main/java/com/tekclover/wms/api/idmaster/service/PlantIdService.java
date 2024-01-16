package com.tekclover.wms.api.idmaster.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.idmaster.model.IKeyValuePair;
import com.tekclover.wms.api.idmaster.model.companyid.CompanyId;
import com.tekclover.wms.api.idmaster.model.moduleid.ModuleId;
import com.tekclover.wms.api.idmaster.model.plantid.FindPlantId;
import com.tekclover.wms.api.idmaster.model.roleaccess.RoleAccess;
import com.tekclover.wms.api.idmaster.repository.CompanyIdRepository;
import com.tekclover.wms.api.idmaster.repository.ModuleIdRepository;
import com.tekclover.wms.api.idmaster.repository.RoleAccessRepository;
import com.tekclover.wms.api.idmaster.repository.Specification.PlantIdSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.idmaster.controller.exception.BadRequestException;
import com.tekclover.wms.api.idmaster.model.plantid.AddPlantId;
import com.tekclover.wms.api.idmaster.model.plantid.PlantId;
import com.tekclover.wms.api.idmaster.model.plantid.UpdatePlantId;
import com.tekclover.wms.api.idmaster.repository.PlantIdRepository;
import com.tekclover.wms.api.idmaster.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlantIdService{

	@Autowired
	private CompanyIdService companyIdService;
	@Autowired
	private CompanyIdRepository companyIdRepository;
	@Autowired
	private PlantIdRepository plantIdRepository;
	@Autowired
	private RoleAccessRepository roleAccessRepository;
	@Autowired
	private ModuleIdRepository moduleIdRepository;

	/**
	 * getPlantIds
	 * @return
	 */
	public List<PlantId> getPlantIds () {
		List<PlantId> plantIdList =  plantIdRepository.findAll();
		plantIdList = plantIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<PlantId>newPlantId=new ArrayList<>();
		for(PlantId dbPlantId:plantIdList) {
			if (dbPlantId.getCompanyIdAndDescription() != null) {
				IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(dbPlantId.getCompanyCodeId(), dbPlantId.getLanguageId());
				if (iKeyValuePair != null) {
					dbPlantId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
			}
			newPlantId.add(dbPlantId);
		}
		return newPlantId;
	}

	/**
	 * getPlantId
	 * @param plantId
	 * @param companyCodeId
	 * @param languageId
	 * @return
	 */
	public PlantId getPlantId (String plantId,String companyCodeId,String languageId) {
		Optional<PlantId> dbPlantId =
				plantIdRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
						companyCodeId, plantId, languageId, 0L);
		if (dbPlantId.isEmpty()) {
			throw new BadRequestException("The given values : " +
					"plantId - " + plantId +
					" doesn't exist.");
		}
		PlantId newPlantId = new PlantId();
		BeanUtils.copyProperties(dbPlantId.get(),newPlantId, CommonUtils.getNullPropertyNames(dbPlantId));
		IKeyValuePair iKeyValuePair=companyIdRepository.getCompanyIdAndDescription(companyCodeId,languageId);
		if(iKeyValuePair!=null) {
			newPlantId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
		}
		return newPlantId;
	}

//	/**
//	 *
//	 * @param searchPlantId
//	 * @return
//	 * @throws ParseException
//	 */
//	public List<PlantId> findPlantId(SearchPlantId searchPlantId)
//			throws ParseException {
//		PlantIdSpecification spec = new PlantIdSpecification(searchPlantId);
//		List<PlantId> results = plantIdRepository.findAll(spec);
//		log.info("results: " + results);
//		return results;
//	}

	/**
	 * createPlantId
	 * @param newPlantId
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PlantId createPlantId (AddPlantId newPlantId, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		PlantId dbPlantId = new PlantId();
		Optional<PlantId> duplicatePlantId=plantIdRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(newPlantId.getCompanyCodeId(), newPlantId.getPlantId(), newPlantId.getLanguageId(), 0L);
		if(!duplicatePlantId.isEmpty()) {
			throw new EntityNotFoundException("Record is Getting Duplicated");
		}else {
			CompanyId dbCompanyId = companyIdService.getCompanyId(newPlantId.getCompanyCodeId(), newPlantId.getLanguageId());
			log.info("newPlantId : " + newPlantId);
			BeanUtils.copyProperties(newPlantId, dbPlantId, CommonUtils.getNullPropertyNames(newPlantId));
			dbPlantId.setDeletionIndicator(0L);
			dbPlantId.setCompanyIdAndDescription(dbCompanyId.getCompanyCodeId() + "-" + dbCompanyId.getDescription());
			dbPlantId.setCreatedBy(loginUserID);
			dbPlantId.setUpdatedBy(loginUserID);
			dbPlantId.setCreatedOn(new Date());
			dbPlantId.setUpdatedOn(new Date());
			plantIdRepository.save(dbPlantId);
		}
		return dbPlantId;
	}

	/**
	 * updatePlantId
	 * @param loginUserID
	 * @param plantId
	 * @param updatePlantId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PlantId updatePlantId (String plantId,String companyCodeId,String languageId,String loginUserID, UpdatePlantId updatePlantId)
			throws IllegalAccessException, InvocationTargetException {
		PlantId dbPlantId = getPlantId(plantId,companyCodeId,languageId);
		BeanUtils.copyProperties(updatePlantId, dbPlantId, CommonUtils.getNullPropertyNames(updatePlantId));
		dbPlantId.setUpdatedBy(loginUserID);
		dbPlantId.setUpdatedOn(new Date());

		updateRoleAccess(plantId, companyCodeId, languageId, updatePlantId.getDescription());									//Update Description
		updateModuleId(plantId, companyCodeId, languageId, updatePlantId.getDescription());									//Update Description

		return plantIdRepository.save(dbPlantId);
	}

	/**
	 * deletePlantId
	 * @param loginUserID
	 * @param plantId
	 */
	public void deletePlantId (String plantId,String companyCodeId,String languageId, String loginUserID) {
		PlantId dbPlantId = getPlantId(plantId,companyCodeId,languageId);
		if ( dbPlantId != null) {
			dbPlantId.setDeletionIndicator(1L);
			dbPlantId.setUpdatedBy(loginUserID);
			plantIdRepository.save(dbPlantId);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + plantId);
		}
	}

	//Find PlantId
	public List<PlantId> findPlant(FindPlantId findPlantId) throws ParseException {

		PlantIdSpecification spec = new PlantIdSpecification(findPlantId);
		List<PlantId> results = plantIdRepository.findAll(spec);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		log.info("results: " + results);
       List<PlantId>newPlantId=new ArrayList<>();
		for(PlantId dbPlantId:results) {
			if (dbPlantId.getCompanyIdAndDescription() != null) {
				IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(dbPlantId.getCompanyCodeId(), dbPlantId.getLanguageId());
				if (iKeyValuePair != null) {
					dbPlantId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
			}
			newPlantId.add(dbPlantId);
		}
		return newPlantId;
	}

	//update Description
	public void updateRoleAccess(String plantId, String  companyCodeId, String languageId, String description){
		List<RoleAccess> roleAccessList = roleAccessRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndDeletionIndicator(
																languageId, companyCodeId, plantId, 0L);
		if(roleAccessList != null) {
			roleAccessList.stream().forEach(n -> n.setPlantIdAndDescription(plantId + "-" + description));
		}
	}
	public void updateModuleId(String plantId, String  companyCodeId, String languageId, String description){
		List<ModuleId> moduleIdList = moduleIdRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndDeletionIndicator(
																languageId, companyCodeId, plantId, 0L);
		if(moduleIdList != null) {
			moduleIdList.stream().forEach(n -> n.setPlantIdAndDescription(plantId + "-" + description));
		}
	}
}
