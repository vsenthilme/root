package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.clienttype.AddClientType;
import com.mnrclara.api.setup.model.clienttype.ClientType;
import com.mnrclara.api.setup.model.clienttype.UpdateClientType;
import com.mnrclara.api.setup.repository.ClientTypeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientTypeService {
	
	@Autowired
	private ClientTypeRepository clientTypeRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<ClientType> getClientTypes () {
		List<ClientType> clientTypeList =  clientTypeRepository.findAll();
		clientTypeList = clientTypeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return clientTypeList;
	}
	
	/**
	 * getClientType
	 * @param clientTypeId
	 * @return
	 */
	public ClientType getClientType (Long clientTypeId) {
		ClientType clientType = clientTypeRepository.findByClientTypeId(clientTypeId);
		if (clientType != null && clientType.getDeletionIndicator() == 0) {
			return clientType;
		} else {
			throw new BadRequestException("The given ClientType ID : " + clientTypeId + " doesn't exist.");
		}
	}
	
	/**
	 * createClientType
	 * @param newClientType
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientType createClientType (AddClientType newClientType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType dbClientType = new ClientType();
		BeanUtils.copyProperties(newClientType, dbClientType, CommonUtils.getNullPropertyNames(newClientType));
		dbClientType.setDeletionIndicator(0L);
		dbClientType.setCreatedBy(loginUserID);
		dbClientType.setUpdatedBy(loginUserID);
		dbClientType.setCreatedOn(new Date());
		dbClientType.setUpdatedOn(new Date());
		return clientTypeRepository.save(dbClientType);
	}
	
	/**
	 * updateClientType
	 * @param clienttypeId
	 * @param loginUserId 
	 * @param updateClientType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientType updateClientType (Long clientTypeId, String loginUserID, UpdateClientType updateClientType) 
			throws IllegalAccessException, InvocationTargetException {
		ClientType dbClientType = getClientType(clientTypeId);
		BeanUtils.copyProperties(updateClientType, dbClientType, CommonUtils.getNullPropertyNames(updateClientType));
		dbClientType.setUpdatedBy(loginUserID);
		dbClientType.setUpdatedOn(new Date());
		return clientTypeRepository.save(dbClientType);
	}
	
	/**
	 * deleteClientType
	 * @param loginUserID 
	 * @param clienttypeCode
	 */
	public void deleteClientType (Long clientTypeId, String loginUserID) {
		ClientType clienttype = getClientType(clientTypeId);
		if ( clienttype != null) {
			clienttype.setDeletionIndicator(1L);
			clienttype.setUpdatedBy(loginUserID);
			clienttype.setUpdatedOn(new Date());
			clientTypeRepository.save(clienttype);
		} else {
			throw new EntityNotFoundException("Error in deleting clientTypeId: " + clientTypeId);
		}
	}
}
