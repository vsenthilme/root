package com.mnrclara.api.accounting.model.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UserLogin {

	private String userName;
	private String password;
	private Collection<GrantedAuthority> grantedAutoriyList = new ArrayList<>();
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<GrantedAuthority> getGrantedAutoriyList() {
		return grantedAutoriyList;
	}
	public void setGrantedAutoriyList(Collection<GrantedAuthority> grantedAutoriyList) {
		this.grantedAutoriyList = grantedAutoriyList;
	}
}
