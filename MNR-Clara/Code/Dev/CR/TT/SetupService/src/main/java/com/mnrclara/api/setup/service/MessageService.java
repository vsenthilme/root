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
import com.mnrclara.api.setup.model.message.AddMessage;
import com.mnrclara.api.setup.model.message.Message;
import com.mnrclara.api.setup.model.message.UpdateMessage;
import com.mnrclara.api.setup.repository.MessageRepository;
import com.mnrclara.api.setup.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	/**
	 * getCompanies
	 * @return
	 */
	public List<Message> getMessages () {
		List<Message> messageList =  messageRepository.findAll();
		messageList = messageList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return messageList;
	}
	
	/**
	 * getMessage
	 * @param messageId
	 * @return
	 */
	public Message getMessage (Long messageId) {
		Message message = messageRepository.findByMessageId(messageId);
		if (message != null && message.getDeletionIndicator() == 0) {
			return message;
		} else {
			throw new BadRequestException("The given Message ID : " + messageId + " doesn't exist.");
		}
	}
	
	/**
	 * createMessage
	 * @param newMessage
	 * @param loginUserID 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Message createMessage (AddMessage newMessage, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Optional<Message> message = 
				messageRepository.findByLanguageIdAndClassIdAndMessageIdAndMessageTypeAndDeletionIndicator(			
					newMessage.getLanguageId(),
					newMessage.getClassId(),
					newMessage.getMessageId(),
					newMessage.getMessageType(),
					0L);
		if (!message.isEmpty()) {
			throw new BadRequestException("Record is getting duplicated with the given values");
		}
		Message dbMessage = new Message();
		BeanUtils.copyProperties(newMessage, dbMessage);
		dbMessage.setDeletionIndicator(0L);
		dbMessage.setCreatedBy(loginUserID);
		dbMessage.setUpdatedBy(loginUserID);
		dbMessage.setCreatedOn(new Date());
		dbMessage.setUpdatedOn(new Date());
		return messageRepository.save(dbMessage);
	}
	
	/**
	 * updateMessage
	 * @param messageId
	 * @param loginUserId 
	 * @param updateMessage
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Message updateMessage (Long messageId, String loginUserId, UpdateMessage updateMessage) 
			throws IllegalAccessException, InvocationTargetException {
		Message dbMessage = getMessage(messageId);
		BeanUtils.copyProperties(updateMessage, dbMessage, CommonUtils.getNullPropertyNames(updateMessage));
		dbMessage.setUpdatedBy(loginUserId);
		dbMessage.setUpdatedOn(new Date());
		return messageRepository.save(dbMessage);
	}
	
	/**
	 * deleteMessage
	 * @param loginUserID 
	 * @param messageCode
	 */
	public void deleteMessage (Long messageModuleId, String loginUserID) {
		Message message = getMessage(messageModuleId);
		if ( message != null) {
			message.setDeletionIndicator(1L);
			message.setUpdatedBy(loginUserID);
			messageRepository.save(message);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + messageModuleId);
		}
	}
}
