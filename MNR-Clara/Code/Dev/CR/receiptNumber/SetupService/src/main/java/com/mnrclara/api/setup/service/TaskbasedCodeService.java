package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.taskbasedcode.AddTaskbasedCode;
import com.mnrclara.api.setup.model.taskbasedcode.TaskbasedCode;
import com.mnrclara.api.setup.model.taskbasedcode.UpdateTaskbasedCode;
import com.mnrclara.api.setup.repository.TaskbasedCodeRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskbasedCodeService {
	
	@Autowired
	private TaskbasedCodeRepository taskbasedCodeRepository;
	
	public List<TaskbasedCode> getTaskbasedCodes () {
		List<TaskbasedCode> taskbasedCodeList = taskbasedCodeRepository.findAll();
		return taskbasedCodeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getTaskbasedCode
	 * @param taskbasedCodeId
	 * @return
	 */
	public TaskbasedCode getTaskbasedCode (String taskbasedCodeId) {
		TaskbasedCode taskbasedCode = taskbasedCodeRepository.findByTaskCode(taskbasedCodeId).orElse(null);
		if (taskbasedCode.getDeletionIndicator() == 0) {
			return taskbasedCode;
		} else {
			throw new BadRequestException("The given TaskbasedCode ID : " + taskbasedCodeId + " doesn't exist.");
		}
	}
	
	/**
	 * createTaskbasedCode
	 * @param newTaskbasedCode
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TaskbasedCode createTaskbasedCode (AddTaskbasedCode newTaskbasedCode, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<TaskbasedCode> taskbasedcode = 
				taskbasedCodeRepository.findByLanguageIdAndClassIdAndTaskCodeAndDeletionIndicator (
					newTaskbasedCode.getLanguageId(),
					newTaskbasedCode.getClassId(),
					newTaskbasedCode.getTaskCode(),
					0L);
		if (!taskbasedcode.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		TaskbasedCode dbTaskbasedCode = new TaskbasedCode();
		BeanUtils.copyProperties(newTaskbasedCode, dbTaskbasedCode);
		dbTaskbasedCode.setDeletionIndicator(0L);
		dbTaskbasedCode.setCreatedBy(loginUserID);
		dbTaskbasedCode.setUpdatedBy(loginUserID);
		dbTaskbasedCode.setCreatedOn(new Date());
		dbTaskbasedCode.setUpdatedOn(new Date());
		return taskbasedCodeRepository.save(dbTaskbasedCode);
	}
	
	/**
	 * updateTaskbasedCode
	 * @param taskbasedCodeId
	 * @param loginUserId 
	 * @param updateTaskbasedCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public TaskbasedCode updateTaskbasedCode (String taskbasedCodeId, String loginUserID, UpdateTaskbasedCode updateTaskbasedCode) 
			throws IllegalAccessException, InvocationTargetException {
		TaskbasedCode dbTaskbasedCode = getTaskbasedCode(taskbasedCodeId);
		BeanUtils.copyProperties(updateTaskbasedCode, dbTaskbasedCode, CommonUtils.getNullPropertyNames(updateTaskbasedCode));
		dbTaskbasedCode.setUpdatedBy(loginUserID);
		dbTaskbasedCode.setUpdatedOn(new Date());
		log.info("desc: " + dbTaskbasedCode.getTaskcodeDescription());
		return taskbasedCodeRepository.save(dbTaskbasedCode);
	}
	
	/**
	 * deleteTaskbasedCode
	 * @param loginUserID 
	 * @param taskbasedCodeCode
	 */
	public void deleteTaskbasedCode (String taskbasedCodeId, String loginUserID) {
		TaskbasedCode taskbasedCode = getTaskbasedCode(taskbasedCodeId);
		if ( taskbasedCode != null) {
			taskbasedCode.setDeletionIndicator(1L);
			taskbasedCode.setUpdatedBy(loginUserID);
			taskbasedCodeRepository.save(taskbasedCode);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + taskbasedCodeId);
		}
	}
}
