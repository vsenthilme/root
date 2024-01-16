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
import com.mnrclara.api.setup.model.classid.AddClass;
import com.mnrclara.api.setup.model.classid.UpdateClass;
import com.mnrclara.api.setup.repository.ClassRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClassService {
	
	@Autowired
	private ClassRepository classRepository;
	
	/**
	 * 
	 * @return
	 */
	public List<com.mnrclara.api.setup.model.classid.Class> getClasses () {
		List<com.mnrclara.api.setup.model.classid.Class> classList =  classRepository.findAll();
		return classList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getClass
	 * @param classId
	 * @return
	 */
	public com.mnrclara.api.setup.model.classid.Class getClass (Long classId) {
		com.mnrclara.api.setup.model.classid.Class objClass = classRepository.findByClassIdAndDeletionIndicator(classId, 0L);
		if (objClass != null) {
			return objClass;
		} else {
			throw new BadRequestException("The given Class ID : " + classId + " doesn't exist.");
		}
	}
	
	/**
	 * createClass
	 * @param newClass
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public com.mnrclara.api.setup.model.classid.Class createClass (AddClass newClass, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.api.setup.model.classid.Class dbClass = new com.mnrclara.api.setup.model.classid.Class();
		BeanUtils.copyProperties(newClass, dbClass, CommonUtils.getNullPropertyNames(newClass));
		dbClass.setDeletionIndicator(0L);
		dbClass.setCreatedBy(loginUserID);
		dbClass.setUpdatedBy(loginUserID);
		dbClass.setCreatedOn(new Date());
		dbClass.setUpdatedOn(new Date());
		return classRepository.save(dbClass);
	}
	
	/**
	 * updateClass
	 * @param classId
	 * @param loginUserId 
	 * @param updateClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public com.mnrclara.api.setup.model.classid.Class updateClass (Long classId, String loginUserID, UpdateClass updateClass) 
			throws IllegalAccessException, InvocationTargetException {
		com.mnrclara.api.setup.model.classid.Class dbClass = getClass(classId);
		BeanUtils.copyProperties(updateClass, dbClass, CommonUtils.getNullPropertyNames(updateClass));
		dbClass.setUpdatedBy(loginUserID);
		dbClass.setUpdatedOn(new Date());
		return classRepository.save(dbClass);
	}
	
	/**
	 * deleteClass
	 * @param loginUserID 
	 * @param classCode
	 */
	public void deleteClass (Long classId, String loginUserID) {
		com.mnrclara.api.setup.model.classid.Class classObj = getClass(classId);
		if ( classObj != null) {
			classObj.setDeletionIndicator(1L);
			classObj.setUpdatedBy(loginUserID);
			classRepository.save(classObj);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + classId); 
		}
	}
}
