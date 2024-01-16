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

import com.iweb2b.api.master.model.notification.AddNotification;
import com.iweb2b.api.master.model.notification.Notification;
import com.iweb2b.api.master.model.notification.UpdateNotification;
import com.iweb2b.api.master.repository.NotificationRepository;
import com.iweb2b.api.master.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	public List<Notification> getNotification () {
		List<Notification> notificationList =  notificationRepository.findAll();
		notificationList = notificationList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return notificationList;
	}
	
	/**
	 * getNotification
	 * @param notificationId
	 * @return
	 */
	public Notification getNotification (String partnerId) {
		Optional<Notification> notification = notificationRepository.findByPartnerIdAndDeletionIndicator(partnerId, 0L);
		if (notification.isEmpty()) {
			return null;
		}
		return notification.get();
	}
	
	/**
	 * createNotification
	 * @param newNotification
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Notification createNotification (AddNotification newNotification, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException, Exception {

		Notification dbNotification = new Notification();
		BeanUtils.copyProperties(newNotification, dbNotification, CommonUtils.getNullPropertyNames(newNotification));
		dbNotification.setDeletionIndicator(0L);
		dbNotification.setCreatedBy(loginUserId);
		dbNotification.setUpdatedBy(loginUserId);
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
	public Notification updateNotification (String notificationId, String loginUserId, UpdateNotification updateNotification)
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
	public void deleteNotification (String partnerId, String loginUserID) {
		Notification notification = getNotification(partnerId);
		if (notification != null) {
			notification.setDeletionIndicator(1L);
			notification.setUpdatedBy(loginUserID);
			notification.setUpdatedOn(new Date());
			notificationRepository.save(notification);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + partnerId);
		}
	}
}
