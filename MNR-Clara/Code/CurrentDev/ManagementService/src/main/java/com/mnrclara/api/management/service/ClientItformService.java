package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.clientitform.AddClientItform;
import com.mnrclara.api.management.model.clientitform.ClientItform;
import com.mnrclara.api.management.model.clientitform.UpdateClientItform;
import com.mnrclara.api.management.repository.ClientItformRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientItformService {

	@Autowired
	private ClientItformRepository clientItformRepository;

	/**
	 * getClientItforms
	 * 
	 * @return
	 */
	public List<ClientItform> getClientItforms() {
		List<ClientItform> clientItformList = clientItformRepository.findAll();
		clientItformList = clientItformList.stream().filter(n -> n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return clientItformList;
	}

	/**
	 * getClientItform
	 * 
	 * @param clientItformId
	 * @return
	 */
	public ClientItform getClientItform(Long clientItformId) {
		ClientItform clientItform = clientItformRepository.findByIntakeFormId(clientItformId).orElse(null);
		if (clientItform.getDeletionIndicator() == 0) {
			return clientItform;
		} else {
			throw new BadRequestException("The given ClientItform ID : " + clientItform + " doesn't exist.");
		}
	}

	/**
	 * createClientItform
	 * 
	 * @param newClientItform
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientItform createClientItform(AddClientItform newClientItform)
			throws IllegalAccessException, InvocationTargetException {
		ClientItform dbClientItform = new ClientItform();
		BeanUtils.copyProperties(newClientItform, dbClientItform);
		return clientItformRepository.save(dbClientItform);
	}

	/**
	 * updateClientItform
	 * 
	 * @param clientitformId
	 * @param updateClientItform
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientItform updateClientItform(Long clientitformId, UpdateClientItform updateClientItform)
			throws IllegalAccessException, InvocationTargetException {
		ClientItform dbClientItform = getClientItform(clientitformId);
		BeanUtils.copyProperties(updateClientItform, dbClientItform,
				CommonUtils.getNullPropertyNames(updateClientItform));
		return clientItformRepository.save(dbClientItform);
	}

	/**
	 * deleteClientItform
	 * 
	 * @param clientitformCode
	 */
	public void deleteClientItform(Long clientitformModuleId) {
		ClientItform clientitform = getClientItform(clientitformModuleId);
		if (clientitform != null) {
			clientitform.setDeletionIndicator(1L);
			clientItformRepository.save(clientitform);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientitformModuleId);
		}
	}
}
