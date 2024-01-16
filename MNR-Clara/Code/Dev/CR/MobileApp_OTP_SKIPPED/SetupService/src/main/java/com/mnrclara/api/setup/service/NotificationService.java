package com.mnrclara.api.setup.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.setup.exception.BadRequestException;
import com.mnrclara.api.setup.model.notification.AddNotification;
import com.mnrclara.api.setup.model.notification.Notification;
import com.mnrclara.api.setup.model.notification.UpdateNotification;
import com.mnrclara.api.setup.repository.NotificationRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Notification> getNotifications () {
		List<Notification> notificationList =  notificationRepository.findAll();
		notificationList = notificationList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return notificationList;
	}
	
	/**
	 * 
	 * @param languageId
	 * @param classId
	 * @param transactionId
	 * @param userId
	 * @param notificationId
	 * @return
	 */
	public Notification getNotification (Long notificationId) {
		Optional<Notification> notification = 
				notificationRepository.findByNotificationIdAndDeletionIndicator(notificationId, 0L);
		if (notification.isEmpty()) {
			throw new BadRequestException("The given values: notificationId: " + notificationId + 
					" doesn't exist.");
		} 
		return notification.get();
	}
	
	/**
	 * createNotification
	 * @param newNotification
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notification createNotification (AddNotification newNotification, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notification dbNotification = new Notification();
		BeanUtils.copyProperties(newNotification, dbNotification, CommonUtils.getNullPropertyNames(newNotification));
		dbNotification.setDeletionIndicator(0L);
		dbNotification.setCreatedBy(loginUserID);
		dbNotification.setUpdatedBy(loginUserID);
		dbNotification.setCreatedOn(new Date());
		dbNotification.setUpdatedOn(new Date());
		return notificationRepository.save(dbNotification);
	}
	
	/**
	 * updateNotification
	 * @param notificationId
	 * @param loginUserId 
	 * @param updateNotification
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notification updateNotification (Long notificationId, String loginUserId, UpdateNotification updateNotification) 
			throws IllegalAccessException, InvocationTargetException {
		Notification dbNotification = getNotification(notificationId);
		BeanUtils.copyProperties(updateNotification, dbNotification, CommonUtils.getNullPropertyNames(updateNotification));
		dbNotification.setUpdatedBy(loginUserId);
		dbNotification.setUpdatedOn(new Date());
		return notificationRepository.save(dbNotification);
	}
	
	/**
	 * deleteNotification
	 * @param loginUserID 
	 * @param notificationCode
	 */
	public void deleteNotification (Long notificationId, String loginUserID) {
		Notification notification = getNotification(notificationId);
		if ( notification != null) {
			notification.setDeletionIndicator(1L);
			notification.setUpdatedBy(loginUserID);
			notification.setUpdatedOn(new Date());
			notificationRepository.save(notification);
		} 
	}
}
