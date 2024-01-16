package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.clientmatter.AddClientMatter;
import com.mnrclara.api.management.model.clientmatter.ClientMatter;
import com.mnrclara.api.management.model.clientmatter.UpdateClientMatter;
import com.mnrclara.api.management.repository.ClientMatterRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientMatterService {

	@Autowired
	private ClientMatterRepository clientMatterRepository;

	/**
	 * getClientMatters
	 * 
	 * @return
	 */
	public List<ClientMatter> getClientMatters() {
		List<ClientMatter> clientMatterList = clientMatterRepository.findAll();
		clientMatterList = clientMatterList.stream().filter(n -> n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return clientMatterList;
	}

	/**
	 * getClientMatter
	 * 
	 * @param clientMatterId
	 * @return
	 */
	public ClientMatter getClientMatter(Long clientMatterId) {
		ClientMatter clientMatter = clientMatterRepository.findByMatterNumber(clientMatterId).orElse(null);
		if (clientMatter.getDeletionIndicator() == 0) {
			return clientMatter;
		} else {
			throw new BadRequestException("The given ClientMatter ID : " + clientMatter + " doesn't exist.");
		}
	}

	/**
	 * createClientMatter
	 * 
	 * @param newClientMatter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientMatter createClientMatter(AddClientMatter newClientMatter)
			throws IllegalAccessException, InvocationTargetException {
		ClientMatter dbClientMatter = new ClientMatter();
		BeanUtils.copyProperties(newClientMatter, dbClientMatter);
		return clientMatterRepository.save(dbClientMatter);
	}

	/**
	 * updateClientMatter
	 * 
	 * @param clientmatterId
	 * @param updateClientMatter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientMatter updateClientMatter(Long clientmatterId, UpdateClientMatter updateClientMatter)
			throws IllegalAccessException, InvocationTargetException {
		ClientMatter dbClientMatter = getClientMatter(clientmatterId);
		BeanUtils.copyProperties(updateClientMatter, dbClientMatter,
				CommonUtils.getNullPropertyNames(updateClientMatter));
		return clientMatterRepository.save(dbClientMatter);
	}

	/**
	 * deleteClientMatter
	 * 
	 * @param clientmatterCode
	 */
	public void deleteClientMatter(Long clientmatterModuleId) {
		ClientMatter clientmatter = getClientMatter(clientmatterModuleId);
		if (clientmatter != null) {
			clientmatter.setDeletionIndicator(1L);
			clientMatterRepository.save(clientmatter);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientmatterModuleId);
		}
	}
}
