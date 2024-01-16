package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.masters.model.auth.AuthToken;
import com.tekclover.wms.api.masters.model.dto.Warehouse;
import com.tekclover.wms.api.masters.model.idmaster.WarehouseId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.businesspartner.AddBusinessPartner;
import com.tekclover.wms.api.masters.model.businesspartner.BusinessPartner;
import com.tekclover.wms.api.masters.model.businesspartner.SearchBusinessPartner;
import com.tekclover.wms.api.masters.model.businesspartner.UpdateBusinessPartner;
import com.tekclover.wms.api.masters.repository.BusinessPartnerRepository;
import com.tekclover.wms.api.masters.repository.specification.BusinessPartnerSpecification;
import com.tekclover.wms.api.masters.util.CommonUtils;
import com.tekclover.wms.api.masters.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessPartnerService {

	@Autowired
	AuthTokenService authTokenService;
	@Autowired
	IDMasterService idMasterService;
	@Autowired
	private BusinessPartnerRepository businesspartnerRepository;

	/**
	 * getBusinessPartners
	 * @return
	 */
	public List<BusinessPartner> getBusinessPartners () {
		List<BusinessPartner> businesspartnerList = businesspartnerRepository.findAll();
		log.info("businesspartnerList : " + businesspartnerList);
		businesspartnerList = businesspartnerList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return businesspartnerList;
	}

	/**
	 * getBusinessPartner
	 * @param partnerCode
	 * @return
	 */
	public BusinessPartner getBusinessPartner (String partnerCode,String companyCodeId,String plantId,String warehouseId, String languageId,Long businessPartnerType) {
		Optional<BusinessPartner> businesspartner = businesspartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndPartnerCodeAndBusinessPartnerTypeAndLanguageIdAndDeletionIndicator(
				companyCodeId,
				plantId,
				warehouseId,
				partnerCode,
				businessPartnerType,
				languageId,
				0L);
		if(businesspartner.isEmpty()){
			throw new BadRequestException("The given values:"+
					"partnerCode " + partnerCode +
					"companyCodeId"+companyCodeId+
					"plantId"+plantId+" doesn't exist.");
		}
		return businesspartner.get();
	}

	/**
	 *
	 * @param searchBusinessPartner
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public List<BusinessPartner> findBusinessPartner(SearchBusinessPartner searchBusinessPartner)
			throws Exception {
		if (searchBusinessPartner.getStartCreatedOn() != null && searchBusinessPartner.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchBusinessPartner.getStartCreatedOn(), searchBusinessPartner.getEndCreatedOn());
			searchBusinessPartner.setStartCreatedOn(dates[0]);
			searchBusinessPartner.setEndCreatedOn(dates[1]);
		}

		if (searchBusinessPartner.getStartUpdatedOn() != null && searchBusinessPartner.getEndUpdatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchBusinessPartner.getStartUpdatedOn(), searchBusinessPartner.getEndUpdatedOn());
			searchBusinessPartner.setStartUpdatedOn(dates[0]);
			searchBusinessPartner.setEndUpdatedOn(dates[1]);
		}

		BusinessPartnerSpecification spec = new BusinessPartnerSpecification(searchBusinessPartner);
		List<BusinessPartner> results = businesspartnerRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 * createBusinessPartner
	 * @param newBusinessPartner
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BusinessPartner createBusinessPartner (AddBusinessPartner newBusinessPartner, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<BusinessPartner> duplicateBusinessPartner=businesspartnerRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndPartnerCodeAndBusinessPartnerTypeAndLanguageIdAndDeletionIndicator(newBusinessPartner.getCompanyCodeId(), newBusinessPartner.getPlantId(), newBusinessPartner.getWarehouseId(),newBusinessPartner.getPartnerCode(), newBusinessPartner.getBusinessPartnerType(), newBusinessPartner.getLanguageId(), 0L);
		BusinessPartner dbBusinessPartner = new BusinessPartner();
		if(!duplicateBusinessPartner.isEmpty()){
			throw new EntityNotFoundException("Record is Getting Duplicated");
		} else {
			BeanUtils.copyProperties(newBusinessPartner, dbBusinessPartner, CommonUtils.getNullPropertyNames(newBusinessPartner));
			dbBusinessPartner.setDeletionIndicator(0L);
			dbBusinessPartner.setCreatedBy(loginUserID);
			dbBusinessPartner.setUpdatedBy(loginUserID);
			dbBusinessPartner.setCreatedOn(new Date());
			dbBusinessPartner.setUpdatedOn(new Date());
			return businesspartnerRepository.save(dbBusinessPartner);
		}
	}

	/**
	 * updateBusinessPartner
	 * @param businesspartner
	 * @param updateBusinessPartner
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public BusinessPartner updateBusinessPartner (String partnerCode,String companyCodeId,String plantId,String warehouseId,String languageId,Long businessPartnerType, UpdateBusinessPartner updateBusinessPartner, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		BusinessPartner dbBusinessPartner = getBusinessPartner(partnerCode,companyCodeId,plantId,warehouseId,languageId,businessPartnerType);
		BeanUtils.copyProperties(updateBusinessPartner, dbBusinessPartner, CommonUtils.getNullPropertyNames(updateBusinessPartner));
		dbBusinessPartner.setUpdatedBy(loginUserID);
		dbBusinessPartner.setUpdatedOn(new Date());
		return businesspartnerRepository.save(dbBusinessPartner);
	}

	/**
	 * deleteBusinessPartner
	 * @param businesspartner
	 */
	public void deleteBusinessPartner (String partnerCode,String companyCodeId,String plantId,String warehouseId,String languageId,Long businessPartnerType, String loginUserID) {
		BusinessPartner businesspartner = getBusinessPartner(partnerCode,companyCodeId,plantId,warehouseId,languageId,businessPartnerType);
		if ( businesspartner != null) {
			businesspartner.setDeletionIndicator (1L);
			businesspartner.setUpdatedBy(loginUserID);
			businesspartner.setUpdatedOn(new Date());
			businesspartnerRepository.save(businesspartner);
		} else {
			throw new EntityNotFoundException("Error in deleting Id:" + partnerCode);
		}
	}
}
