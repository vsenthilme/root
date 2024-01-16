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

import com.iweb2b.api.master.model.clientassignment.AddClientAssignment;
import com.iweb2b.api.master.model.clientassignment.ClientAssignment;
import com.iweb2b.api.master.model.clientassignment.UpdateClientAssignment;
import com.iweb2b.api.master.repository.ClientAssignmentRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientAssignmentService {
	
	@Autowired
	private ClientAssignmentRepository clientAssignmentRepository;
	
	public List<ClientAssignment> getClientAssignment () {
		List<ClientAssignment> clientAssignmentList =  clientAssignmentRepository.findAll();
		clientAssignmentList = clientAssignmentList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return clientAssignmentList;
	}
	
	/**
	 * getClientAssignment
	 * @param clientAssignmentId
	 * @return
	 */
	public ClientAssignment getClientAssignment (String partnerId) {
		Optional<ClientAssignment> clientAssignment = clientAssignmentRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);

		if (clientAssignment.isEmpty()) {
			return null;
		}
		return clientAssignment.get();
	}
	
	/**
	 * createClientAssignment
	 * @param newClientAssignment
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientAssignment createClientAssignment (AddClientAssignment newClientAssignment, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		ClientAssignment dbClientAssignment = new ClientAssignment();
		BeanUtils.copyProperties(newClientAssignment, dbClientAssignment, CommonUtils.getNullPropertyNames(newClientAssignment));
		dbClientAssignment.setDeletionIndicator(0L);
		dbClientAssignment.setCreatedBy(loginUserId);
		dbClientAssignment.setUpdatedBy(loginUserId);
		dbClientAssignment.setCreatedOn(new Date());
		dbClientAssignment.setUpdatedOn(new Date());
		return clientAssignmentRepository.save(dbClientAssignment);
	}
	
	/**
	 * updateClientAssignment
	 * @param clientAssignmentId
	 * @param loginUserId 
	 * @param updateClientAssignment
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientAssignment updateClientAssignment (String partnerId, String loginUserId, UpdateClientAssignment updateClientAssignment)
			throws IllegalAccessException, InvocationTargetException {
		ClientAssignment dbClientAssignment = getClientAssignment(partnerId);
		BeanUtils.copyProperties(updateClientAssignment, dbClientAssignment, CommonUtils.getNullPropertyNames(updateClientAssignment));
		dbClientAssignment.setUpdatedBy(loginUserId);
		dbClientAssignment.setUpdatedOn(new Date());
		return clientAssignmentRepository.save(dbClientAssignment);
	}
	
	/**
	 * deleteClientAssignment
	 * @param loginUserID 
	 * @param clientassignmentCode
	 */
	public void deleteClientAssignment (String clientassignmentModuleId, String loginUserID) {
		ClientAssignment clientassignment = getClientAssignment(clientassignmentModuleId);
		if (clientassignment != null) {
			clientassignment.setDeletionIndicator(1L);
			clientassignment.setUpdatedBy(loginUserID);
			clientassignment.setUpdatedOn(new Date());
			clientAssignmentRepository.save(clientassignment);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientassignmentModuleId);
		}
	}
}
