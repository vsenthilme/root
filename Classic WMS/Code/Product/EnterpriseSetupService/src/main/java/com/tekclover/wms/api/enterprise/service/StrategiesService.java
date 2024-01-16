package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import com.tekclover.wms.api.enterprise.repository.CompanyRepository;
import com.tekclover.wms.api.enterprise.repository.PlantRepository;
import com.tekclover.wms.api.enterprise.repository.WarehouseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.enterprise.controller.exception.BadRequestException;
import com.tekclover.wms.api.enterprise.model.strategies.AddStrategies;
import com.tekclover.wms.api.enterprise.model.strategies.SearchStrategies;
import com.tekclover.wms.api.enterprise.model.strategies.Strategies;
import com.tekclover.wms.api.enterprise.model.strategies.UpdateStrategies;
import com.tekclover.wms.api.enterprise.repository.StrategiesRepository;
import com.tekclover.wms.api.enterprise.repository.specification.StrategiesSpecification;
import com.tekclover.wms.api.enterprise.util.CommonUtils;
import com.tekclover.wms.api.enterprise.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StrategiesService {
	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private PlantRepository plantRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StrategiesRepository strategiesRepository;

	/**
	 * getStrategiess
	 *
	 * @return
	 */
	public List<Strategies> getStrategiess() {
		List<Strategies> strategiesList = strategiesRepository.findAll();
		log.info("strategiesList : " + strategiesList);
		strategiesList = strategiesList.stream().filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<Strategies> newStrategies = new ArrayList<>();
		for (Strategies dbStrategyId : strategiesList) {
			if (dbStrategyId.getCompanyIdAndDescription() != null &&
					dbStrategyId.getPlantIdAndDescription() != null &&
					dbStrategyId.getWarehouseIdAndDescription() != null) {

				IkeyValuePair iKeyValuePair = companyRepository.
						getCompanyIdAndDescription(dbStrategyId.getCompanyId(), dbStrategyId.getLanguageId());

				IkeyValuePair iKeyValuePair1 = plantRepository.
						getPlantIdAndDescription(dbStrategyId.getPlantId(), dbStrategyId.getLanguageId(),
								dbStrategyId.getCompanyId());

				IkeyValuePair iKeyValuePair2 = warehouseRepository.
						getWarehouseIdAndDescription(dbStrategyId.getWarehouseId(), dbStrategyId.getLanguageId(),
								dbStrategyId.getCompanyId(), dbStrategyId.getPlantId());

				IkeyValuePair ikeyValuePair3 = strategiesRepository.
						getStrategyIdAndDescription(dbStrategyId.getStrategyNo(), dbStrategyId.getStrategyTypeId(),
								dbStrategyId.getLanguageId(), dbStrategyId.getCompanyId(), dbStrategyId.getPlantId(),
								dbStrategyId.getWarehouseId());

				if (iKeyValuePair != null) {
					dbStrategyId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbStrategyId.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbStrategyId.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbStrategyId.setDescription(ikeyValuePair3.getDescription());
					dbStrategyId.setStrategyNoText(ikeyValuePair3.getStrategyNoText());
				}
			}
			newStrategies.add(dbStrategyId);
		}
		return newStrategies;
	}

	/**
	 * getStrategies
	 *
	 * @param warehouseId
	 * @param sequenceIndicator
	 * @param strategyTypeId
	 * @param strategyNo
	 * @param priority
	 * @return
	 */
	public List<Strategies> getStrategies(String companyId, String languageId, String plantId, String warehouseId,
										  Long strategyTypeId, Long sequenceIndicator,
										  String strategyNo, Long priority) {
		List<Strategies> strategiesList = new ArrayList<>();
		List<Strategies> strategies =
				strategiesRepository.findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndStrategyTypeIdAndSequenceIndicatorAndStrategyNoAndPriorityAndDeletionIndicator(
						languageId,
						companyId,
						plantId,
						warehouseId,
						strategyTypeId,
						sequenceIndicator,
						strategyNo,
						priority,
						0L);
		if (strategies.isEmpty()) {
			throw new BadRequestException("The given Strategies Id : " + strategyTypeId + " doesn't exist.");
		}
		for (Strategies strategies1 : strategies) {
			Strategies newStrategyId = new Strategies();
			BeanUtils.copyProperties(strategies1, newStrategyId, CommonUtils.getNullPropertyNames(strategies1));

			IkeyValuePair iKeyValuePair = companyRepository.getCompanyIdAndDescription(companyId, languageId);

			IkeyValuePair iKeyValuePair1 = plantRepository.getPlantIdAndDescription(plantId, languageId, companyId);

			IkeyValuePair iKeyValuePair2 = warehouseRepository.getWarehouseIdAndDescription(warehouseId, languageId, companyId, plantId);

			IkeyValuePair ikeyValuePair3 = strategiesRepository.getStrategyIdAndDescription(strategyNo, strategyTypeId, languageId,
					companyId, plantId, warehouseId);

			if (iKeyValuePair != null) {
				newStrategyId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
			}
			if (iKeyValuePair1 != null) {
				newStrategyId.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
			}
			if (iKeyValuePair2 != null) {
				newStrategyId.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
			}
			if (ikeyValuePair3 != null) {
				newStrategyId.setDescription(ikeyValuePair3.getDescription());
				newStrategyId.setStrategyNoText(ikeyValuePair3.getStrategyNoText());
			}
			strategiesList.add(newStrategyId);
		}
		return strategiesList;
	}

	/**
	 * findStrategies
	 *
	 * @param searchStrategies
	 * @return
	 * @throws ParseException
	 */
	public List<Strategies> findStrategies(SearchStrategies searchStrategies) throws Exception {
		if (searchStrategies.getStartCreatedOn() != null && searchStrategies.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchStrategies.getStartCreatedOn(), searchStrategies.getEndCreatedOn());
			searchStrategies.setStartCreatedOn(dates[0]);
			searchStrategies.setEndCreatedOn(dates[1]);
		}

		StrategiesSpecification spec = new StrategiesSpecification(searchStrategies);
		List<Strategies> results = strategiesRepository.findAll(spec);
		log.info("results: " + results);
		results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		List<Strategies> newStrategies = new ArrayList<>();

		for (Strategies dbStrategyId : results) {
			if (dbStrategyId.getCompanyIdAndDescription() != null &&
					dbStrategyId.getPlantIdAndDescription() != null &&
					dbStrategyId.getWarehouseIdAndDescription() != null) {

				IkeyValuePair iKeyValuePair = companyRepository.
						getCompanyIdAndDescription(dbStrategyId.getCompanyId(), dbStrategyId.getLanguageId());

				IkeyValuePair iKeyValuePair1 = plantRepository.
						getPlantIdAndDescription(dbStrategyId.getPlantId(), dbStrategyId.getLanguageId(),
								dbStrategyId.getCompanyId());

				IkeyValuePair iKeyValuePair2 = warehouseRepository.
						getWarehouseIdAndDescription(dbStrategyId.getWarehouseId(), dbStrategyId.getLanguageId(),
								dbStrategyId.getCompanyId(), dbStrategyId.getPlantId());

				IkeyValuePair ikeyValuePair3 = strategiesRepository.
						getStrategyIdAndDescription(dbStrategyId.getStrategyNo(), dbStrategyId.getStrategyTypeId(),
								dbStrategyId.getLanguageId(), dbStrategyId.getCompanyId(), dbStrategyId.getPlantId(),
								dbStrategyId.getWarehouseId());

				if (iKeyValuePair != null) {
					dbStrategyId.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbStrategyId.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbStrategyId.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if (ikeyValuePair3 != null) {
					dbStrategyId.setDescription(ikeyValuePair3.getDescription());
					dbStrategyId.setStrategyNoText(ikeyValuePair3.getStrategyNoText());
				}
			}
			newStrategies.add(dbStrategyId);
		}
		return newStrategies;
	}

	/**
	 * createStrategies
	 *
	 * @param newStrategies
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<Strategies> createStrategies(List<AddStrategies> newStrategies, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		List<Strategies> strategiesList = new ArrayList<>();

		for (AddStrategies addStrategies : newStrategies) {
			List<Strategies> optStrategies =
					strategiesRepository.findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndStrategyTypeIdAndSequenceIndicatorAndStrategyNoAndPriorityAndDeletionIndicator(
							addStrategies.getLanguageId(),
							addStrategies.getCompanyId(),
							addStrategies.getPlantId(),
							addStrategies.getWarehouseId(),
							addStrategies.getStrategyTypeId(),
							addStrategies.getSequenceIndicator(),
							addStrategies.getStrategyNo(),
							addStrategies.getPriority(),
							0L);
			if (!optStrategies.isEmpty()) {
				throw new BadRequestException("The given values are getting duplicated.");
			}

			Strategies dbStrategies = new Strategies();
			BeanUtils.copyProperties(addStrategies, dbStrategies, CommonUtils.getNullPropertyNames(addStrategies));

			IkeyValuePair ikeyValuePair =
					companyRepository.getCompanyIdAndDescription(addStrategies.getCompanyId(), addStrategies.getLanguageId());

			IkeyValuePair ikeyValuePair1 =
					plantRepository.getPlantIdAndDescription(addStrategies.getPlantId(),
							addStrategies.getLanguageId(), addStrategies.getCompanyId());

			IkeyValuePair ikeyValuePair2 =
					warehouseRepository.getWarehouseIdAndDescription(addStrategies.getWarehouseId(),
							addStrategies.getLanguageId(), addStrategies.getCompanyId(), addStrategies.getPlantId());

			IkeyValuePair ikeyValuePair3 =
					strategiesRepository.getStrategyIdAndDescription(addStrategies.getStrategyNo(),
							addStrategies.getStrategyTypeId(), addStrategies.getLanguageId(), addStrategies.getCompanyId(),
							addStrategies.getPlantId(), addStrategies.getWarehouseId());

			if (ikeyValuePair != null && ikeyValuePair1 != null &&
					ikeyValuePair2 != null && ikeyValuePair3 != null) {
				dbStrategies.setCompanyIdAndDescription(ikeyValuePair.getCompanyCodeId() + "-" + ikeyValuePair.getDescription());
				dbStrategies.setPlantIdAndDescription(ikeyValuePair1.getPlantId() + "-" + ikeyValuePair1.getDescription());
				dbStrategies.setWarehouseIdAndDescription(ikeyValuePair2.getWarehouseId() + "-" + ikeyValuePair2.getDescription());
				dbStrategies.setDescription(ikeyValuePair3.getDescription());
				dbStrategies.setStrategyNoText(ikeyValuePair3.getStrategyNoText());
			} else {
				throw new BadRequestException("The given values of Company Id "
						+ addStrategies.getCompanyId() + " Plant Id "
						+ addStrategies.getPlantId() + " Warehouse Id "
						+ addStrategies.getWarehouseId() + " Strategy Type Id "
						+ addStrategies.getStrategyTypeId() + " Strategy No "
						+ addStrategies.getStrategyNo() + " doesn't exist ");
			}
			dbStrategies.setDeletionIndicator(0L);
			dbStrategies.setCreatedBy(loginUserID);
			dbStrategies.setUpdatedBy(loginUserID);
			dbStrategies.setCreatedOn(new Date());
			dbStrategies.setUpdatedOn(new Date());
			Strategies savedStrategies = strategiesRepository.save(dbStrategies);

			strategiesList.add(savedStrategies);
		}
		return strategiesList;

	}



	/**
	 * updateStrategies
	 *
	 * @param strategyTypeId
	 * @param updateStrategies
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<Strategies> updateStrategies(String companyId, String languageId, String plantId, String warehouseId,
											 Long strategyTypeId, Long sequenceIndicator, String strategyNo,
											 Long priority, List<UpdateStrategies> updateStrategies, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		List<Strategies> newStrategies = new ArrayList<>();

		List<Strategies> dbStrategies = strategiesRepository.findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndStrategyTypeIdAndSequenceIndicatorAndStrategyNoAndPriorityAndDeletionIndicator(
				languageId, companyId, plantId, warehouseId,
				strategyTypeId, sequenceIndicator, strategyNo,
				priority, 0L);

		for (UpdateStrategies updateStrategies1 : updateStrategies) {
			for (Strategies dbstrategies : dbStrategies) {
				BeanUtils.copyProperties(updateStrategies1, dbstrategies, CommonUtils.getNullPropertyNames(updateStrategies1));
				dbstrategies.setUpdatedBy(loginUserID);
				dbstrategies.setUpdatedOn(new Date());
				Strategies saved = strategiesRepository.save(dbstrategies);
				newStrategies.add(saved);
			}
		}
		return newStrategies;
	}

		/**
		 * deleteStrategies
		 * @param strategyTypeId
		 */
		public void deleteStrategies (String companyId,String languageId,String plantId,String warehouseId,
				Long strategyTypeId, Long sequenceIndicator, String strategyNo,
				Long priority, String loginUserID) {

			List<Strategies> strategies = getStrategies(companyId,languageId,plantId,warehouseId,
					strategyTypeId, sequenceIndicator, strategyNo, priority);
			if ( strategies != null) {
		for(Strategies dbStrategies:strategies) {
			dbStrategies.setDeletionIndicator(1L);
			dbStrategies.setUpdatedBy(loginUserID);
			dbStrategies.setUpdatedOn(new Date());
			strategiesRepository.save(dbStrategies);
		}
			} else {
				throw new EntityNotFoundException("Error in deleting Id: " + strategyTypeId);
			}
		}
}
