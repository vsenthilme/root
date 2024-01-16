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

import com.mnrclara.api.setup.model.message.AddMessage;
import com.mnrclara.api.setup.model.message.Message;
import com.mnrclara.api.setup.model.message.UpdateMessage;
import com.mnrclara.api.setup.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Message"}, value = "Message Operations related to MessageController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Message",description = "Operations related to Message")})
@RequestMapping("/message")
@RestController
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
    @ApiOperation(response = Message.class, value = "Get all Message details") // label for swagger
	@GetMapping("")
	public ResponseEntity<?> getAll() {
		List<Message> messageList = messageService.getMessages();
		return new ResponseEntity<>(messageList, HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Get a Message") // label for swagger 
	@GetMapping("/{messageId}")
	public ResponseEntity<?> getMessage(@PathVariable Long messageId) {
    	Message message = messageService.getMessage(messageId);
    	log.info("Message : " + message);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Create Message") // label for swagger
	@PostMapping("")
	public ResponseEntity<?> addMessage(@Valid @RequestBody AddMessage newMessage, @RequestParam String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		Message createdMessage = messageService.createMessage(newMessage, loginUserID);
		return new ResponseEntity<>(createdMessage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Update Message") // label for swagger
    @PatchMapping("/{messageId}")
	public ResponseEntity<?> patchMessage(@PathVariable Long messageId, @RequestParam String loginUserID,
			@Valid @RequestBody UpdateMessage updateMessage) 
			throws IllegalAccessException, InvocationTargetException {
		Message updatedMessage = messageService.updateMessage(messageId, loginUserID, updateMessage);
		return new ResponseEntity<>(updatedMessage , HttpStatus.OK);
	}
    
    @ApiOperation(response = Message.class, value = "Delete Message") // label for swagger
	@DeleteMapping("/{messageId}")
	public ResponseEntity<?> deleteMessage(@PathVariable Long messageId, @RequestParam String loginUserID) {
    	messageService.deleteMessage(messageId, loginUserID);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}