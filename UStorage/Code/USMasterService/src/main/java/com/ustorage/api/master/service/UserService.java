package com.ustorage.api.master.service;

import javax.persistence.EntityNotFoundException;

import com.ustorage.api.master.model.warehouse.Warehouse;
import com.ustorage.api.master.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ustorage.api.master.controller.exception.BadRequestException;
import com.ustorage.api.master.model.user.*;
import com.ustorage.api.master.repository.UserRepository;
import com.ustorage.api.master.util.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder = new PasswordEncoder();
	
	public User validateUser (String username, String loginPassword) {
		User user = userRepository.findByUsernameAndDeletionIndicator(username,0).orElse(null);
		if (user == null) {
    		throw new BadRequestException("Invalid Username : " + username);
    	}
		
		/*boolean isSuccess = passwordEncoder.matches(loginPassword, user.getPassword());
		if (!isSuccess) {
			throw new BadRequestException("Password is wrong. Please enter correct password.");
		}*/
		if (!Objects.equals(loginPassword, user.getPassword())){
			throw new BadRequestException("Password is wrong. Please enter correct password");
		}
		
		return user;
	}
	/**
	 *
	 * @return
	 */
	public List<User> getUsers () {

		List<User> userList =  userRepository.findAll();
		userList = userList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return userList;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public User getUser (long id) {

		Optional<User> user = userRepository.findByIdAndDeletionIndicator(id, 0L);
		if (user.isEmpty()) {
			return null;
		}
		return user.get();
	}

	/**
	 *
	 * @param userName
	 * @return
	 */
	public String getUsr (String userName) {

		String user = userRepository.getUsrName(userName);
		if (user==null || user.isEmpty()) {
			return null;
		}
		return user;
	}

	/**
	 *
	 * @param newUser
	 * @return
	 */
	public User createUser (AddUser newUser,String loginUserID) {
		User dbUser = new User();
		String username = newUser.getUsername();
		String dbUserName = getUsr(username);
		if(dbUserName==null){
			BeanUtils.copyProperties(newUser, dbUser, CommonUtils.getNullPropertyNames(newUser));
			dbUser.setDeletionIndicator(0L);
			dbUser.setCreatedOn(new Date());
			dbUser.setCreatedBy(loginUserID);
			dbUser.setUpdatedBy(loginUserID);
			dbUser.setUpdatedOn(new Date());
//			dbUser.setUsername(newUser.getUsername());
//			dbUser.setRole(newUser.getRole());
//			dbUser.setEmail(newUser.getEmail());
//			dbUser.setCity(newUser.getCity());
//			dbUser.setState(newUser.getState());
//			dbUser.setCountry(newUser.getCountry());
//			dbUser.setFirstname(newUser.getFirstname());
//			dbUser.setLastname(newUser.getLastname());
//			dbUser.setCompany(newUser.getCompany());
//			dbUser.setPhoneNo(newUser.getPhoneNo());
//			dbUser.setUserTypeId(newUser.getUserTypeId());
//			dbUser.setStatus(newUser.getStatus());
//			dbUser.setPassword(newUser.getPassword());
//			dbUser.setPassword(PasswordEncoder.encodePassword(newUser.getPassword()));
		}else{
			throw new BadRequestException("Username already Exist: " + username);
		}
		return userRepository.save(dbUser);
	}

	/**
	 *
	 * @param id
	 * @param modifiedUser
	 * @return
	 */
	public User patchUser (Long id, User modifiedUser,String loginUserID) {
		User dbUser = getUser(id);
		BeanUtils.copyProperties(modifiedUser, dbUser, CommonUtils.getNullPropertyNames(modifiedUser));
		dbUser.setDeletionIndicator(0L);
		dbUser.setUpdatedBy(loginUserID);
		dbUser.setUpdatedOn(new Date());
//		dbUser.setRole(modifiedUser.getRole());
//		dbUser.setEmail(modifiedUser.getEmail());
//		dbUser.setCity(modifiedUser.getCity());
//		dbUser.setState(modifiedUser.getState());
//		dbUser.setCountry(modifiedUser.getCountry());
//		dbUser.setFirstname(modifiedUser.getFirstname());
//		dbUser.setLastname(modifiedUser.getLastname());
//		dbUser.setCompany(modifiedUser.getCompany());
//		dbUser.setPhoneNo(modifiedUser.getPhoneNo());
//		dbUser.setUserTypeId(modifiedUser.getUserTypeId());
//		dbUser.setStatus(modifiedUser.getStatus());
//		dbUser.setPassword(modifiedUser.getPassword());
		//dbUser.setPassword(PasswordEncoder.encodePassword(modifiedUser.getPassword()));
		return userRepository.save(dbUser);
	}

	/**
	 *
	 * @param userId
	 */
	/*public void deleteUser (long userId) {
		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
		} else {
			throw new EntityNotFoundException(String.valueOf(userId));
		}
	}*/

	public void deleteUser (Long userId, String loginUserID) {
		User user = getUser(userId);
		if (user != null) {
			user.setDeletionIndicator(1L);
			user.setUpdatedBy(loginUserID);
			user.setUpdatedOn(new Date());
			userRepository.save(user);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + userId);
		}
	}
}
