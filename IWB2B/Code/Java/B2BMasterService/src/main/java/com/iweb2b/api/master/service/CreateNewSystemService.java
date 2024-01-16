package com.iweb2b.api.master.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.master.model.createnewsystems.AddCreateNewSystem;
import com.iweb2b.api.master.model.createnewsystems.CreateNewSystem;
import com.iweb2b.api.master.model.createnewsystems.UpdateCreateNewSystem;
import com.iweb2b.api.master.repository.CreateNewSystemRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateNewSystemService {
	
	@Autowired
	private CreateNewSystemRepository createNewSystemRepository;
	
	public List<CreateNewSystem> getCreateNewSystem () {
		List<CreateNewSystem> createNewSystemsList =  createNewSystemRepository.findAll();
		createNewSystemsList = createNewSystemsList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return createNewSystemsList;
	}
	
	/**
	 * getCreateNewSystem
	 * @param partnerId
	 * @return
	 */
	public CreateNewSystem getCreateNewSystem (String partnerId) {
		Optional<CreateNewSystem> createNewSystems = createNewSystemRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (createNewSystems.isEmpty()) {
			return null;
		}
		return createNewSystems.get();
	}
	
	/**
	 * createCreateNewSystem
	 * @param newCreateNewSystem
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CreateNewSystem createCreateNewSystem (AddCreateNewSystem newCreateNewSystem, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		CreateNewSystem dbCreateNewSystem = new CreateNewSystem();
		BeanUtils.copyProperties(newCreateNewSystem, dbCreateNewSystem, CommonUtils.getNullPropertyNames(newCreateNewSystem));
		dbCreateNewSystem.setDeletionIndicator(0L);
		dbCreateNewSystem.setCreatedBy(loginUserId);
		dbCreateNewSystem.setUpdatedBy(loginUserId);
		dbCreateNewSystem.setCreatedOn(new Date());
		dbCreateNewSystem.setUpdatedOn(new Date());
		return createNewSystemRepository.save(dbCreateNewSystem);
	}
	
	/**
	 * updateCreateNewSystem
	 * @param partnerId
	 * @param loginUserId 
	 * @param updateCreateNewSystem
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CreateNewSystem updateCreateNewSystem (String partnerId, String loginUserId, UpdateCreateNewSystem updateCreateNewSystem)
			throws IllegalAccessException, InvocationTargetException {
		CreateNewSystem dbCreateNewSystem = getCreateNewSystem(partnerId);
		BeanUtils.copyProperties(updateCreateNewSystem, dbCreateNewSystem, CommonUtils.getNullPropertyNames(updateCreateNewSystem));
		dbCreateNewSystem.setUpdatedBy(loginUserId);
		dbCreateNewSystem.setUpdatedOn(new Date());
		return createNewSystemRepository.save(dbCreateNewSystem);
	}
	
	/**
	 * deleteCreateNewSystem
	 * @param loginUserID 
	 * @param partnerId
	 */
	public void deleteCreateNewSystem (String partnerId, String loginUserID) {
		CreateNewSystem createnewsystem = getCreateNewSystem(partnerId);
		if (createnewsystem != null) {
			createnewsystem.setDeletionIndicator(1L);
			createnewsystem.setUpdatedBy(loginUserID);
			createnewsystem.setUpdatedOn(new Date());
			createNewSystemRepository.save(createnewsystem);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnerId);
		}
	}
}
