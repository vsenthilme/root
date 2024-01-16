package com.tekclover.wms.api.enterprise.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.enterprise.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.enterprise.controller.exception.BadRequestException;
import com.tekclover.wms.api.enterprise.model.doors.AddDoors;
import com.tekclover.wms.api.enterprise.model.doors.Doors;
import com.tekclover.wms.api.enterprise.model.doors.UpdateDoors;
import com.tekclover.wms.api.enterprise.repository.DoorsRepository;
import com.tekclover.wms.api.enterprise.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DoorsService extends BaseService {
	
	@Autowired
	private DoorsRepository doorsRepository;
	
	/**
	 * getDoorss
	 * @return
	 */
	public List<Doors> getDoorss () {
		List<Doors> doorsList = doorsRepository.findAll();
		log.info("doorsList : " + doorsList);
		doorsList = doorsList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return doorsList;
	}
	
	/**
	 * getDoors
	 * @param doorNumber
	 * @return
	 */
	public Doors getDoors (String doorId) {
		Doors doors = doorsRepository.findByDoorId(doorId).orElse(null);
		if (doors != null && doors.getDeletionIndicator() != null && doors.getDeletionIndicator() == 0) {
			return doors;
		} else {
			throw new BadRequestException("The given Doors ID : " + doorId + " doesn't exist.");
		}
	}
	
	/**
	 * createDoors
	 * @param newDoors
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Doors createDoors (AddDoors newDoors, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		Doors dbDoors = new Doors();
		BeanUtils.copyProperties(newDoors, dbDoors, CommonUtils.getNullPropertyNames(newDoors));
		dbDoors.setDeletionIndicator(0L);
		dbDoors.setCompanyId(getCompanyCode());
		dbDoors.setCreatedBy(loginUserID);
		dbDoors.setUpdatedBy(loginUserID);
		dbDoors.setCreatedOn(DateUtils.getCurrentKWTDateTime());
		dbDoors.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
		return doorsRepository.save(dbDoors);
	}
	
	/**
	 * updateDoors
	 * @param doorsCode
	 * @param updateDoors
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Doors updateDoors (String doorNumber, UpdateDoors updateDoors, String loginUserID)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		Doors dbDoors = getDoors(doorNumber);
		BeanUtils.copyProperties(updateDoors, dbDoors, CommonUtils.getNullPropertyNames(updateDoors));
		dbDoors.setUpdatedBy(loginUserID);
		dbDoors.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
		return doorsRepository.save(dbDoors);
	}
	
	/**
	 * deleteDoors
	 * @param doorsCode
	 */
	public void deleteDoors (String doorNumber, String loginUserID) throws ParseException {
		Doors doors = getDoors(doorNumber);
		if ( doors != null) {
			doors.setDeletionIndicator (1L);
			doors.setUpdatedBy(loginUserID);
			doors.setUpdatedOn(DateUtils.getCurrentKWTDateTime());
			doorsRepository.save(doors);
		} else {
			throw new EntityNotFoundException(String.valueOf(doorNumber));
		}
	}
}
