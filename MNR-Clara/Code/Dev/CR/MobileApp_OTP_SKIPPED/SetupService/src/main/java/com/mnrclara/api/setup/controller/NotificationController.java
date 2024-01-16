package com.mnrclara.api.setup.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.setup.model.notification.AddNotification;
import com.mnrclara.api.setup.model.notification.Notification;
import com.mnrclara.api.setup.model.notification.UpdateNotification;
import com.mnrclara.api.setup.service.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Notification"}, value = "Notification Operations related to NotificationController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Notification",description = "Operations related to Notification")})
@RequestMapping("/notification")
@RestController
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
    @ApiOperation(response = Notification.class, value = "Get all Notification details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Notification> notificationList = notificationService.getNotifications();
		return new ResponseEntity<>(notificationList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Get a Notification") // label for swagger 
	@GetMapping("/{notificationId}")
	public ResponseEntity<?> getNotification(@PathVariable Long notificationId) {
    	Notification notification = notificationService.getNotification(notificationId);
    	log.info("Notification : " + notification);
		return new ResponseEntity<>(notification, HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Create Notification") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> postNotification(@Valid @RequestBody AddNotification newNotification, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Notification createdNotification = notificationService.createNotification(newNotification, loginUserID);
		return new ResponseEntity<>(createdNotification , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Update Notification") // label for swagger
    @PatchMapping("/{notificationId}")
	public ResponseEntity<?> patchNotification(@PathVariable Long notificationId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateNotification updateNotification) 
			throws IllegalAccessException, InvocationTargetException {
		Notification updatedNotification = 
				notificationService.updateNotification(notificationId, loginUserID, updateNotification);
		return new ResponseEntity<>(updatedNotification , HttpStatus.OK);
	}
    
    @ApiOperation(response = Notification.class, value = "Delete Notification") // label for swagger
	@DeleteMapping("/{notificationId}")
	public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId, @RequestParam String languageId, @RequestParam Long classId, 
			@RequestParam Long transactionId, @RequestParam String userId, @RequestParam String loginUserID) {
    	notificationService.deleteNotification(notificationId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}