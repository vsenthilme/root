package com.mnrclara.wrapper.core.model;

@SuppressWarnings("serial")
public class UsersHelper extends org.springframework.security.core.userdetails.User {

	public UsersHelper(UserLogin userLogin) {
		super(
			userLogin.getName(),
			userLogin.getPassword(),
			userLogin.getGrantedAutoriyList()
		);
	}
}
