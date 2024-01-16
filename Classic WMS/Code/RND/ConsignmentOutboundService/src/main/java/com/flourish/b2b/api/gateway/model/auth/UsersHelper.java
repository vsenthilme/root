package com.flourish.b2b.api.gateway.model.auth;

@SuppressWarnings("serial")
public class UsersHelper extends org.springframework.security.core.userdetails.User {

	public UsersHelper(UserLogin userLogin) {
		super(
				userLogin.getUserName(),
				userLogin.getPassword(),
				userLogin.getGrantedAutoriyList()
				);
	}
}
