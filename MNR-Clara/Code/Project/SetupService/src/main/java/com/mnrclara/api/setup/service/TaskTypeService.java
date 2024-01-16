package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.tasktype.AddTaskType;
import com.mnrclara.api.setup.model.tasktype.TaskType;
import com.mnrclara.api.setup.model.tasktype.UpdateTaskType;
import com.mnrclara.api.setup.repository.TaskTypeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskTypeService {
	
	@Autowired
	private TaskTypeRepository taskTypeRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<TaskType> getCompanies () {
		return taskTypeRepository.findAll();
	}
	
	/**
	 * getTaskType
	 * @param taskTypeId
	 * @return
	 */
	public TaskType getTaskType (Long taskTypeId) {
		TaskType taskType = taskTypeRepository.findByTaskTypeCode(taskTypeId).orElse(null);
		if (taskType.getDeletionIndicator() == 0) {
			return taskType;
		} else {
			throw new BadRequestException("The given TaskType ID : " + taskTypeId + " doesn't exist.");
		}
	}
	
	/**
	 * createTaskType
	 * @param newTaskType
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TaskType createTaskType (AddTaskType newTaskType, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType dbTaskType = new TaskType();
		BeanUtils.copyProperties(newTaskType, dbTaskType, CommonUtils.getNullPropertyNames(newTaskType));
		log.info("dbTaskType : " + dbTaskType);
		dbTaskType.setDeletionIndicator(0L);
		dbTaskType.setCreatedBy(loginUserID);
		dbTaskType.setUpdatedBy(loginUserID);
		dbTaskType.setCreatedOn(new Date());
		dbTaskType.setUpdatedOn(new Date());
		return taskTypeRepository.save(dbTaskType);
	}
	
	/**
	 * updateTaskType
	 * @param taskTypeId
	 * @param loginUserId 
	 * @param updateTaskType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TaskType updateTaskType (Long taskTypeId, String loginUserID, UpdateTaskType updateTaskType) 
			throws IllegalAccessException, InvocationTargetException {
		TaskType dbTaskType = getTaskType(taskTypeId);
		BeanUtils.copyProperties(updateTaskType, dbTaskType, CommonUtils.getNullPropertyNames(updateTaskType));
		dbTaskType.setUpdatedBy(loginUserID);
		dbTaskType.setUpdatedOn(new Date());
		return taskTypeRepository.save(dbTaskType);
	}
	
	/**
	 * deleteTaskType
	 * @param loginUserID 
	 * @param taskTypeCode
	 */
	public void deleteTaskType (Long taskTypeCode, String loginUserID) {
		TaskType taskType = getTaskType(taskTypeCode);
		if ( taskType != null) {
			taskType.setDeletionIndicator(1L);
			taskType.setUpdatedBy(loginUserID);
			taskType.setUpdatedOn(new Date());
			taskTypeRepository.save(taskType);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + taskTypeCode);
		}
	}
}
