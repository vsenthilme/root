package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.masters.model.imstrategies.SearchImStrategies;
import com.tekclover.wms.api.masters.repository.specification.ImStrategiesSpecification;
import com.tekclover.wms.api.masters.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.imstrategies.AddImStrategies;
import com.tekclover.wms.api.masters.model.imstrategies.ImStrategies;
import com.tekclover.wms.api.masters.model.imstrategies.UpdateImStrategies;
import com.tekclover.wms.api.masters.repository.ImStrategiesRepository;
import com.tekclover.wms.api.masters.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImStrategiesService {

	@Autowired
	private ImStrategiesRepository imstrategiesRepository;

	/**
	 * getImStrategiess
	 * @return
	 */
	public List<ImStrategies> getImStrategiess () {
		List<ImStrategies> imstrategiesList = imstrategiesRepository.findAll();
		log.info("imstrategiesList : " + imstrategiesList);
		imstrategiesList = imstrategiesList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return imstrategiesList;
	}

	/**
	 * getImStrategies
	 * @param strategeyTypeId
	 * @return
	 */
	public ImStrategies getImStrategies (String strategeyTypeId,String companyCodeId,String plantId,String warehouseId,String itemCode,Long sequenceIndicator,String languageId) {
		Optional<ImStrategies> imstrategies = imstrategiesRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndItemCodeAndStrategeyTypeIdAndSequenceIndicatorAndDeletionIndicator(
				companyCodeId,
				plantId,
				languageId,
				itemCode,
				strategeyTypeId,
				sequenceIndicator,
				0L
		);
		if (imstrategies.isEmpty()) {
			throw new BadRequestException("The given values : " +
					"warehouseId - " + warehouseId +
					"strategeyTypeId - " + strategeyTypeId +
					"itemCode - " + itemCode +
					"doesn't exist.");

		}
		return imstrategies.get();
	}

	/**
	 * findImStrategies
	 * @param searchImStrategies
	 * @return
	 * @throws ParseException
	 */
	public List<ImStrategies> findImStrategies(SearchImStrategies searchImStrategies) throws ParseException {

		ImStrategiesSpecification spec = new ImStrategiesSpecification(searchImStrategies);
		List<ImStrategies> results = imstrategiesRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 * createImStrategies
	 * @param newImStrategies
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImStrategies createImStrategies (AddImStrategies newImStrategies, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ImStrategies dbImStrategies = new ImStrategies();
		Optional<ImStrategies> duplicateStrategies = imstrategiesRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndItemCodeAndStrategeyTypeIdAndSequenceIndicatorAndDeletionIndicator(newImStrategies.getCompanyCodeId(), newImStrategies.getPlantId(), newImStrategies.getLanguageId(), newImStrategies.getItemCode(), newImStrategies.getStrategeyTypeId(), newImStrategies.getSequenceIndicator(), 0L);
		if (!duplicateStrategies.isEmpty()) {
			throw new EntityNotFoundException("Record is Getting Duplicated");
		} else {
			BeanUtils.copyProperties(newImStrategies, dbImStrategies, CommonUtils.getNullPropertyNames(newImStrategies));
			dbImStrategies.setDeletionIndicator(0L);
			dbImStrategies.setCreatedBy(loginUserID);
			dbImStrategies.setUpdatedBy(loginUserID);
			dbImStrategies.setCreatedOn(new Date());
			dbImStrategies.setUpdatedOn(new Date());
			return imstrategiesRepository.save(dbImStrategies);
		}
	}

	/**
	 * updateImStrategies
	 * @param strategeyTypeId
	 * @param updateImStrategies
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImStrategies updateImStrategies (String strategeyTypeId,String companyCodeId,String plantId,String warehouseId,String itemCode,Long sequenceIndicator,String languageId, UpdateImStrategies updateImStrategies, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ImStrategies dbImStrategies = getImStrategies(strategeyTypeId,companyCodeId,plantId,warehouseId,itemCode,sequenceIndicator,languageId);
		BeanUtils.copyProperties(updateImStrategies, dbImStrategies, CommonUtils.getNullPropertyNames(updateImStrategies));
		dbImStrategies.setUpdatedBy(loginUserID);
		dbImStrategies.setUpdatedOn(new Date());
		return imstrategiesRepository.save(dbImStrategies);
	}

	/**
	 * deleteImStrategies
	 * @param strategeyTypeId
	 */
	public void deleteImStrategies (String strategeyTypeId,String companyCodeId,String plantId,String warehouseId,String itemCode,Long sequenceIndicator,String languageId,String loginUserID) {
		ImStrategies imstrategies = getImStrategies(strategeyTypeId,companyCodeId,plantId,warehouseId,itemCode,sequenceIndicator,languageId);
		if ( imstrategies != null) {
			imstrategies.setDeletionIndicator (1L);
			imstrategies.setUpdatedBy(loginUserID);
			imstrategies.setUpdatedOn(new Date());
			imstrategiesRepository.save(imstrategies);
		} else {
			throw new EntityNotFoundException("Error in deleting Id:" + strategeyTypeId);
		}
	}
}
