package com.mnrclara.api.common.service;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.common.controller.exception.BadRequestException;
import com.mnrclara.api.common.model.user.AddUser;
import com.mnrclara.api.common.model.user.ModifyUser;
import com.mnrclara.api.common.model.user.User;
import com.mnrclara.api.common.repository.UserRepository;
import com.mnrclara.api.common.util.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder = new PasswordEncoder();
	
	public List<User> getUsers () {
		return userRepository.findAll();
	}
	
	public User getUser (long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public boolean validateUser (String username, String loginPassword) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
    		throw new BadRequestException("Invalid Username : " + username);
    	}
		
		return passwordEncoder.matches(loginPassword, user.getPassword());
	}
	
	public User createUser (AddUser newUser) {
		User dbUser = new User();
		dbUser.setUsername(newUser.getUsername());
		dbUser.setRole(User.Role.USER);
		dbUser.setEmail(newUser.getEmail());
		dbUser.setCity(newUser.getCity());
		dbUser.setState(newUser.getState());
		dbUser.setCountry(newUser.getCountry());
		
		dbUser.setPassword(PasswordEncoder.encodePassword(newUser.getPassword()));
		return userRepository.save(dbUser);
	}
	
	public User patchUser (Long id, ModifyUser modifiedUser) {
		User dbUser = getUser(id);
		dbUser.setEmail(modifiedUser.getEmail());
		dbUser.setCity(modifiedUser.getCity());
		dbUser.setState(modifiedUser.getState());
		dbUser.setCountry(modifiedUser.getCountry());
		return userRepository.save(dbUser);
	}
	
	public void deleteUser (long userId) {
		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
		} else {
			throw new EntityNotFoundException(String.valueOf(userId));
		}
	}
	
	@SuppressWarnings("static-access")
	public void changePassword(String email, String oldPassword, String newPassword)
	throws ConstraintViolationException{
		User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("email"));
	    if (oldPassword == null || oldPassword.isEmpty() 
	    		  || passwordEncoder.getEncoder().matches(oldPassword, user.getPassword())) {
	    	user.setPassword(passwordEncoder.encodePassword(newPassword));
	        userRepository.save(user);
	    } else {
	        throw new ConstraintViolationException("old password doesn't match", new HashSet<>());
	    }
	}
}
