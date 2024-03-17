package com.mnrclara.api.crm.service;

import com.mnrclara.api.crm.model.Notification.Notification;
import com.mnrclara.api.crm.model.Notification.OutputMessage;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendMessage(String topic, String message, String userId) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        simpMessagingTemplate.convertAndSend("/topic/messages",
                new OutputMessage(topic, message, time, userId, null));
    }

    public List<Notification> getNotificationList(String userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrUserIdIsNull(userId);
        return notifications;
    }

    public Boolean updateNotificationAsRead(String userId) {
        try {
            notificationRepository.updateStatus(userId);
            return true;
        } catch (Exception e) {
            log.error("error in updating status of notification for user " + userId);
            return false;
        }
    }

    public Boolean updateNotificationRead(String userId,Long id) {
        try {
            notificationRepository.updateStatusById(userId,id);
            return true;
        } catch (Exception e) {
            log.error("error in updating status of notification for user " + userId);
            return false;
        }
    }

    @Async
    public void saveNotifications(List<String> userId, String userType, String message,
                                  String topic, Date createdOn, String createdBy) {
        List<Notification> toSave = new ArrayList<>();
        if (userId != null && !userId.isEmpty()) {
            for (String user : userId) {
                toSave.add(createNotificationModel(user, userType, message, topic, createdOn, createdBy));
            }
        } else {
            toSave.add(createNotificationModel(null, userType, message, topic, createdOn, createdBy));
        }
        try {
            List<Notification> savedNotificationList = notificationRepository.saveAll(toSave);
            sendMessage(topic, message, null);
            log.info("Notification created for " + topic + " message: " + message, savedNotificationList);
        } catch (Exception e) {
            log.error("Error in creation notification for " + topic + " message: " + message, toSave);
        }
    }

    public Notification createNotificationModel(String userId, String userType,
                                                String message, String topic,
                                                Date createdOn, String createdBy) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserType(userType);
        notification.setMessage(message);
        notification.setTopic(topic);
        notification.setCreatedOn(createdOn);
        notification.setCreatedBy(createdBy);
        return notification;
    }

}
