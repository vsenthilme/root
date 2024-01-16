package com.iweb2b.api.master.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.master.controller.exception.BadRequestException;
import com.iweb2b.api.master.model.user.AddUser;
import com.iweb2b.api.master.model.user.User;
import com.iweb2b.api.master.repository.UserRepository;
import com.iweb2b.api.master.util.PasswordEncoder;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder = new PasswordEncoder();
	
	/**
	 * 
	 * @param username
	 * @param loginPassword
	 * @return
	 */
	public User validateUser (String username, String loginPassword) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
    		throw new BadRequestException("Invalid Username : " + username);
    	}
		
		boolean isSuccess = passwordEncoder.matches(loginPassword, user.getPassword());
		if (!isSuccess) {
			throw new BadRequestException("Password is wrong. Please enter correct password.");
		}
		return user;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<User> getUsers () {
		return userRepository.findAll();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public User getUser (long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	/**
	 * 
	 * @param newUser
	 * @return
	 */
	public User createUser (AddUser newUser) {
		User dbUser = new User();
		dbUser.setUsername(newUser.getUsername());
		dbUser.setRole(User.Role.USER);
		dbUser.setEmail(newUser.getEmail());
		dbUser.setCity(newUser.getCity());
		dbUser.setState(newUser.getState());
		dbUser.setCountry(newUser.getCountry());
		dbUser.setFirstname(newUser.getFirstname());
		dbUser.setLastname(newUser.getLastname());
		dbUser.setCompany(newUser.getCompany());
		
		dbUser.setPassword(PasswordEncoder.encodePassword(newUser.getPassword()));
		return userRepository.save(dbUser);
	}
	
	/**
	 * 
	 * @param id
	 * @param modifiedUser
	 * @return
	 */
	public User patchUser (Long id, User modifiedUser) {
		User dbUser = getUser(id);
		dbUser.setEmail(modifiedUser.getEmail());
		dbUser.setCity(modifiedUser.getCity());
		dbUser.setState(modifiedUser.getState());
		dbUser.setCountry(modifiedUser.getCountry());
		dbUser.setFirstname(modifiedUser.getFirstname());
		dbUser.setLastname(modifiedUser.getLastname());
		dbUser.setCompany(modifiedUser.getCompany());

		dbUser.setPassword(PasswordEncoder.encodePassword(modifiedUser.getPassword()));
		return userRepository.save(dbUser);
	}
	
	/**
	 * 
	 * @param userId
	 */
	public void deleteUser (long userId) {
		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
		} else {
			throw new EntityNotFoundException(String.valueOf(userId));
		}
	}
}
