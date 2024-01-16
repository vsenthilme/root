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
import com.mnrclara.api.setup.model.clientcategory.AddClientCategory;
import com.mnrclara.api.setup.model.clientcategory.ClientCategory;
import com.mnrclara.api.setup.model.clientcategory.UpdateClientCategory;
import com.mnrclara.api.setup.repository.ClientCategoryRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientCategoryService {
	
	@Autowired
	private ClientCategoryRepository clientCategoryRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<ClientCategory> getClientCategories () {
		List<ClientCategory> clientCategoryList =  clientCategoryRepository.findAll();
		clientCategoryList = clientCategoryList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return clientCategoryList;
	}
	
	/**
	 * getClientCategory
	 * @param clientCategoryId
	 * @return
	 */
	public ClientCategory getClientCategory (Long clientCategoryId) {
		ClientCategory clientCategory = clientCategoryRepository.findByClientCategoryId(clientCategoryId).orElse(null);
		if (clientCategory != null && clientCategory.getDeletionIndicator() == 0) {
			return clientCategory;
		} else {
			throw new BadRequestException("The given ClientCategory ID : " + clientCategoryId + " doesn't exist.");
		}
	}
	
	/**
	 * createClientCategory
	 * @param newClientCategory
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientCategory createClientCategory (AddClientCategory newClientCategory, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		
		ClientCategory dbClientCategory = new ClientCategory();
		BeanUtils.copyProperties(newClientCategory, dbClientCategory, CommonUtils.getNullPropertyNames(newClientCategory));
		dbClientCategory.setDeletionIndicator(0L);
		dbClientCategory.setCreatedBy(loginUserID);
		dbClientCategory.setUpdatedBy(loginUserID);
		dbClientCategory.setCreatedOn(new Date());
		dbClientCategory.setUpdatedOn(new Date());
		return clientCategoryRepository.save(dbClientCategory);
	}
	
	/**
	 * updateClientCategory
	 * @param clientcategoryId
	 * @param loginUserId 
	 * @param updateClientCategory
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientCategory updateClientCategory (Long clientCategoryId, String loginUserID, UpdateClientCategory updateClientCategory) 
			throws IllegalAccessException, InvocationTargetException {
		ClientCategory dbClientCategory = getClientCategory(clientCategoryId);
		BeanUtils.copyProperties(updateClientCategory, dbClientCategory, CommonUtils.getNullPropertyNames(updateClientCategory));
		dbClientCategory.setUpdatedBy(loginUserID);
		dbClientCategory.setUpdatedOn(new Date());
		return clientCategoryRepository.save(dbClientCategory);
	}
	
	/**
	 * deleteClientCategory
	 * @param loginUserID 
	 * @param clientcategoryCode
	 */
	public void deleteClientCategory (Long clientCategoryId, String loginUserID) {
		ClientCategory clientcategory = getClientCategory(clientCategoryId);
		if ( clientcategory != null) {
			clientcategory.setDeletionIndicator(1L);
			clientcategory.setUpdatedBy(loginUserID);
			clientcategory.setUpdatedOn(new Date());
			clientCategoryRepository.save(clientcategory);
		} else {
			throw new EntityNotFoundException("Error in deleting clientCategoryId : " + clientCategoryId);
		}
	}
}
