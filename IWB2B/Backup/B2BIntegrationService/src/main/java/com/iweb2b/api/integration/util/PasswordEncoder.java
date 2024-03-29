package com.iweb2b.api.integration.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
	
	static BCryptPasswordEncoder passwordEncoder;
	
	public PasswordEncoder () {
		passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public BCryptPasswordEncoder getEncoder () {
		return passwordEncoder;
	}
	
	public static String encodePassword (String password) {
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	
	public boolean matches (String loginPassword, String dbPassword) {
		return passwordEncoder.matches(loginPassword, dbPassword);
	}
	
	public static String generateToken(String key) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(key);
		System.out.println(hashedPassword);
		return hashedPassword;
	}
	
	public static void main(String[] args) {
		String password = "wms";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println(hashedPassword);
	}
}
