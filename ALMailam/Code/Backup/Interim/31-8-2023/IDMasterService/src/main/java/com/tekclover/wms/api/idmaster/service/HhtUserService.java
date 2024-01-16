package com.tekclover.wms.api.idmaster.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.idmaster.model.IKeyValuePair;
import com.tekclover.wms.api.idmaster.model.inboundorderstatusid.InboundOrderStatusId;
import com.tekclover.wms.api.idmaster.model.outboundordertypeid.OutboundOrderTypeId;
import com.tekclover.wms.api.idmaster.repository.*;
import com.tekclover.wms.api.idmaster.repository.Specification.HhtUserSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.idmaster.controller.exception.BadRequestException;
import com.tekclover.wms.api.idmaster.model.hhtuser.*;
import com.tekclover.wms.api.idmaster.model.hhtuser.HhtUser;
import com.tekclover.wms.api.idmaster.model.hhtuser.UpdateHhtUser;
import com.tekclover.wms.api.idmaster.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HhtUserService {
	@Autowired
	private OrderTypeIdRepository orderTypeIdRepository;
	@Autowired
	private LevelIdRepository levelIdRepository;

	@Autowired
	private WarehouseRepository warehouseRepository;

	@Autowired
	private PlantIdRepository plantIdRepository;

	@Autowired
	private OrderTypeIdService orderTypeIdService;

	@Autowired
	private CompanyIdRepository companyIdRepository;

	@Autowired
	private OutboundOrderTypeIdService outboundOrderTypeIdService;

	@Autowired
	private HhtUserRepository hhtUserRepository;

	/**
	 *
	 * @return
	 */
	public List<HhtUserOutput> getHhtUsers () {
		List<HhtUser> hhtUserList =  hhtUserRepository.findAll();
		hhtUserList = hhtUserList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());

		List<HhtUserOutput> newHhtUser=new ArrayList<>();

		for(HhtUser dbHhtUser:hhtUserList) {

			HhtUserOutput dbHhtUserOutput = new HhtUserOutput();

//			if (dbHhtUser.getCompanyIdAndDescription() != null && dbHhtUser.getPlantIdAndDescription() != null &&
//					dbHhtUser.getWarehouseIdAndDescription() != null) {

				IKeyValuePair iKeyValuePair =
						companyIdRepository.getCompanyIdAndDescription(dbHhtUser.getCompanyCodeId(), dbHhtUser.getLanguageId());

				IKeyValuePair iKeyValuePair1 =
						plantIdRepository.getPlantIdAndDescription(dbHhtUser.getPlantId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId());

				IKeyValuePair iKeyValuePair2 =
						warehouseRepository.getWarehouseIdAndDescription(dbHhtUser.getWarehouseId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId(), dbHhtUser.getPlantId());

				IKeyValuePair iKeyValuePair3 =
						levelIdRepository.getLevelIdAndDescription(dbHhtUser.getLevelId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId(), dbHhtUser.getPlantId(),
								dbHhtUser.getWarehouseId());

				if (iKeyValuePair != null) {
					dbHhtUser.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbHhtUser.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbHhtUser.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if(iKeyValuePair3 != null){
					dbHhtUser.setLevelIdAndDescription(iKeyValuePair3.getLevelId() + "-" + iKeyValuePair3.getDescription());
				}
//			}
			BeanUtils.copyProperties(dbHhtUser, dbHhtUserOutput, CommonUtils.getNullPropertyNames(dbHhtUser));
			if(dbHhtUser.getOrderTypeIds() != null) {
				List<String> orderTypeId = new ArrayList<>();
				for (OrderTypeId dbOrderTypeId : dbHhtUser.getOrderTypeIds()) {
					orderTypeId.add(dbOrderTypeId.getOrderTypeId());
				}
				dbHhtUserOutput.setOrderType(orderTypeId);
			}

			newHhtUser.add(dbHhtUserOutput);
		}
		return newHhtUser;
	}

	/**
	 *
	 * @param userId
	 * @param warehouseId
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @return
	 */
	public HhtUserOutput getHhtUser (String userId, String warehouseId,String companyCodeId,Long levelId,
							   String plantId,String languageId) {
		Optional<HhtUser> hhtUser = hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndLevelIdAndDeletionIndicator(
				userId,
				warehouseId,
				companyCodeId,
				plantId,
				languageId,
				levelId,
				0L);
		if(hhtUser.isEmpty()){
			throw new BadRequestException("The given values : " +
					"warehouseId - " + warehouseId +
					"companyCodeId - " + companyCodeId +
					"userId - " + userId +
					"LevelId - " + levelId +
					" Plant - " + plantId +
					" doesn't exist.");
		}

		HhtUserOutput dbHhtUserOutput = new HhtUserOutput();
		BeanUtils.copyProperties(hhtUser.get(), dbHhtUserOutput, CommonUtils.getNullPropertyNames(hhtUser.get()));
		if(hhtUser.get().getOrderTypeIds() != null) {
			List<String> orderTypeId = new ArrayList<>();
			for (OrderTypeId dbOrderTypeId : hhtUser.get().getOrderTypeIds()) {
				orderTypeId.add(dbOrderTypeId.getOrderTypeId());
			}
			dbHhtUserOutput.setOrderType(orderTypeId);
		}
		return dbHhtUserOutput;
	}

	/**
	 *
	 * @param warehouseId
	 * @return
	 */
	public List<HhtUserOutput> getHhtUser (String warehouseId) {
		List<HhtUser> hhtUser = hhtUserRepository.findByWarehouseIdAndDeletionIndicator(warehouseId, 0L);
		if (hhtUser != null) {
			List<HhtUserOutput> hhtUserOutputList = new ArrayList<>();
		for(HhtUser dbHhtUser : hhtUser) {
			HhtUserOutput dbHhtUserOutput = new HhtUserOutput();
			BeanUtils.copyProperties(dbHhtUser, dbHhtUserOutput, CommonUtils.getNullPropertyNames(dbHhtUser));
			if (dbHhtUser.getOrderTypeIds() != null) {
				List<String> orderTypeId = new ArrayList<>();
				for (OrderTypeId dbOrderTypeId : dbHhtUser.getOrderTypeIds()) {
					orderTypeId.add(dbOrderTypeId.getOrderTypeId());
				}
				dbHhtUserOutput.setOrderType(orderTypeId);
			}
			hhtUserOutputList.add(dbHhtUserOutput);
		}

		return hhtUserOutputList;

		} else {
			throw new BadRequestException("The given warehouseId ID : " + warehouseId + " doesn't exist.");
		}
	}

	/**
	 *
	 * @param newHhtUser
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HhtUser createHhtUser (AddHhtUser newHhtUser, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		HhtUser dbHhtUser = new HhtUser();
		Optional<HhtUser> duplicateHhtUser=hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
				newHhtUser.getUserId(),
				newHhtUser.getWarehouseId(),
				newHhtUser.getCompanyCodeId(),
				newHhtUser.getPlantId(),
				newHhtUser.getLanguageId(),
				0L
		);
		if(!duplicateHhtUser.isEmpty()){
			throw new IllegalAccessException("User is getting Duplicated");
		}
		IKeyValuePair iKeyValuePair =
				companyIdRepository.getCompanyIdAndDescription(newHhtUser.getCompanyCodeId(), newHhtUser.getLanguageId());

		IKeyValuePair iKeyValuePair1=
				plantIdRepository.getPlantIdAndDescription(newHhtUser.getPlantId(),
						newHhtUser.getLanguageId(), newHhtUser.getCompanyCodeId());

		IKeyValuePair iKeyValuePair2 =
				warehouseRepository.getWarehouseIdAndDescription(newHhtUser.getWarehouseId(), newHhtUser.getLanguageId(),
						newHhtUser.getCompanyCodeId(), newHhtUser.getPlantId());

		IKeyValuePair iKeyValuePair3 =
				levelIdRepository.getLevelIdAndDescription(newHhtUser.getLevelId(),newHhtUser.getLanguageId(),
						newHhtUser.getCompanyCodeId(), newHhtUser.getPlantId(), newHhtUser.getWarehouseId());

		BeanUtils.copyProperties(newHhtUser, dbHhtUser, CommonUtils.getNullPropertyNames(newHhtUser));

		if(iKeyValuePair != null && iKeyValuePair1 != null
				&& iKeyValuePair2 != null && iKeyValuePair3 != null) {

			dbHhtUser.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId()+ "-" +iKeyValuePair.getDescription());
			dbHhtUser.setPlantIdAndDescription(iKeyValuePair1.getPlantId()+ "-" +iKeyValuePair1.getDescription());
			dbHhtUser.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId()+"-"+iKeyValuePair2.getDescription());
			dbHhtUser.setLevelIdAndDescription(iKeyValuePair3.getLevelId()+ "-" +iKeyValuePair3.getDescription());

		} else {

			throw new BadRequestException("The given values of Company Id "
					+ newHhtUser.getCompanyCodeId() + " Plant Id "
					+ newHhtUser.getPlantId() + " User Id "
					+ newHhtUser.getUserId() + " Warehouse Id "
					+ newHhtUser.getWarehouseId() + " Level Id "
					+ newHhtUser.getLevelId() + " doesn't exist");
		}

		dbHhtUser.setDeletionIndicator(0L);
		dbHhtUser.setCreatedBy(loginUserID);
		dbHhtUser.setUpdatedBy(loginUserID);
		dbHhtUser.setCreatedOn(new Date());
		dbHhtUser.setUpdatedOn(new Date());
		HhtUser savedHhtUser= hhtUserRepository.save(dbHhtUser);

		savedHhtUser.setOrderTypeIds(new HashSet<>());
		if(newHhtUser.getOrderType() != null){
			for(String newOrderTypeId: newHhtUser.getOrderType()){
				OutboundOrderTypeId dbOutboundOrderTypeId =
						outboundOrderTypeIdService.getOutboundOrderTypeId(dbHhtUser.getWarehouseId(),
									newOrderTypeId, dbHhtUser.getCompanyCodeId(), dbHhtUser.getLanguageId(), dbHhtUser.getPlantId());

				OrderTypeId dbOrderTypeId=new OrderTypeId();
				dbOrderTypeId.setOrderTypeId(newOrderTypeId);
				dbOrderTypeId.setDeletionIndicator(0L);
				dbOrderTypeId.setCreatedBy(loginUserID);
				dbOrderTypeId.setUpdatedBy(loginUserID);
				dbOrderTypeId.setCreatedOn(new Date());
				dbOrderTypeId.setUpdatedOn(new Date());
				dbOrderTypeId.setUserId(savedHhtUser.getUserId());
				OrderTypeId savedOrderTypeId = orderTypeIdRepository.save(dbOrderTypeId);
				savedHhtUser.getOrderTypeIds().add(savedOrderTypeId);
			}
		}
		return savedHhtUser;
	}


	/**
	 *
	 * @param warehouseId
	 * @param userId
	 * @param companyCodeId
	 * @param languageId
	 * @param plantId
	 * @param updateHhtUser
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HhtUser updateHhtUser (String warehouseId, String userId,String companyCodeId,String languageId,Long levelId,
								  String plantId,UpdateHhtUser updateHhtUser, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<HhtUser> dbHhtUser = hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndLevelIdAndDeletionIndicator(
												userId, warehouseId,companyCodeId,
												plantId, languageId, levelId, 0L);
		BeanUtils.copyProperties(updateHhtUser, dbHhtUser.get(), CommonUtils.getNullPropertyNames(updateHhtUser));
		dbHhtUser.get().setUpdatedBy(loginUserID);
		dbHhtUser.get().setUpdatedOn(new Date());
		HhtUser savedHhtUser = hhtUserRepository.save(dbHhtUser.get());

		if(updateHhtUser.getOrderType() != null){
			if(orderTypeIdService.getOrderTypeId(userId) != null){
				orderTypeIdService.deleteOrderTypeId(userId,loginUserID);
			}
			for(String newOrderTypeId : updateHhtUser.getOrderType()){
				OrderTypeId dbOrderTypeId = new OrderTypeId();

				OutboundOrderTypeId outboundOrderTypeId=
						outboundOrderTypeIdService.getOutboundOrderTypeId(dbHhtUser.get().getWarehouseId(),
																			newOrderTypeId,
																			dbHhtUser.get().getCompanyCodeId(),
																			dbHhtUser.get().getLanguageId(),
																			dbHhtUser.get().getPlantId());

				dbOrderTypeId.setOrderTypeId(newOrderTypeId);
				dbOrderTypeId.setDeletionIndicator(0L);
				dbOrderTypeId.setCreatedOn(new Date());
				dbOrderTypeId.setCreatedBy(loginUserID);
				dbOrderTypeId.setUpdatedBy(loginUserID);
				dbOrderTypeId.setUpdatedOn(new Date());
				dbOrderTypeId.setUserId(savedHhtUser.getUserId());
				OrderTypeId savedOrderTypeId = orderTypeIdRepository.save(dbOrderTypeId);
				savedHhtUser.getOrderTypeIds().add(savedOrderTypeId);
			}
		}
		return savedHhtUser;
	}

	/**
	 *
	 * @param warehouseId
	 * @param userId
	 * @param companyCodeId
	 * @param plantId
	 * @param languageId
	 * @param loginUserID
	 */
	public void deleteHhtUser (String warehouseId, String userId,String companyCodeId,Long levelId,
							   String plantId,String languageId,String loginUserID) {

		Optional<HhtUser> hhtUser = hhtUserRepository.findByUserIdAndWarehouseIdAndCompanyCodeIdAndPlantIdAndLanguageIdAndLevelIdAndDeletionIndicator(
										userId,
										warehouseId,
										companyCodeId,
										plantId,
										languageId,
										levelId,
								0L);

		if ( hhtUser.get() != null) {
			hhtUser.get().setDeletionIndicator(1L);
			hhtUser.get().setUpdatedBy(loginUserID);
			hhtUserRepository.save(hhtUser.get());
			if(orderTypeIdService.getOrderTypeId(userId)!=null){
				orderTypeIdService.deleteOrderTypeId(userId,loginUserID);
			}
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userId);
		}
	}

	/**
	 *
	 * @param findHhtUser
	 * @return
	 * @throws ParseException
	 */
	//Find HhtUser
	public List<HhtUserOutput> findHhtUser(FindHhtUser findHhtUser) throws ParseException {

		HhtUserSpecification spec = new HhtUserSpecification(findHhtUser);
		List<HhtUser> results = hhtUserRepository.findAll(spec);

		List<HhtUserOutput> newHhtUser=new ArrayList<>();

		for(HhtUser dbHhtUser:results) {

			HhtUserOutput dbHhtUserOutput = new HhtUserOutput();

//			if (dbHhtUser.getCompanyIdAndDescription() != null && dbHhtUser.getPlantIdAndDescription() != null &&
//					dbHhtUser.getWarehouseIdAndDescription() != null) {

				IKeyValuePair iKeyValuePair =
						companyIdRepository.getCompanyIdAndDescription(dbHhtUser.getCompanyCodeId(), dbHhtUser.getLanguageId());

				IKeyValuePair iKeyValuePair1 =
						plantIdRepository.getPlantIdAndDescription(dbHhtUser.getPlantId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId());

				IKeyValuePair iKeyValuePair2 =
						warehouseRepository.getWarehouseIdAndDescription(dbHhtUser.getWarehouseId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId(), dbHhtUser.getPlantId());

				IKeyValuePair iKeyValuePair3 =
						levelIdRepository.getLevelIdAndDescription(dbHhtUser.getLevelId(),
								dbHhtUser.getLanguageId(), dbHhtUser.getCompanyCodeId(), dbHhtUser.getPlantId(),
								dbHhtUser.getWarehouseId());

				if (iKeyValuePair != null) {
					dbHhtUser.setCompanyIdAndDescription(iKeyValuePair.getCompanyCodeId() + "-" + iKeyValuePair.getDescription());
				}
				if (iKeyValuePair1 != null) {
					dbHhtUser.setPlantIdAndDescription(iKeyValuePair1.getPlantId() + "-" + iKeyValuePair1.getDescription());
				}
				if (iKeyValuePair2 != null) {
					dbHhtUser.setWarehouseIdAndDescription(iKeyValuePair2.getWarehouseId() + "-" + iKeyValuePair2.getDescription());
				}
				if(iKeyValuePair3 != null){
					dbHhtUser.setLevelIdAndDescription(iKeyValuePair3.getLevelId() + "-" + iKeyValuePair3.getDescription());
				}
//			}
			BeanUtils.copyProperties(dbHhtUser, dbHhtUserOutput, CommonUtils.getNullPropertyNames(dbHhtUser));
			if(dbHhtUser.getOrderTypeIds() != null) {
				List<String> orderTypeId = new ArrayList<>();
				for (OrderTypeId dbOrderTypeId : dbHhtUser.getOrderTypeIds()) {
					orderTypeId.add(dbOrderTypeId.getOrderTypeId());
				}
				dbHhtUserOutput.setOrderType(orderTypeId);
			}

			newHhtUser.add(dbHhtUserOutput);
		}
		return newHhtUser;
	}
}
