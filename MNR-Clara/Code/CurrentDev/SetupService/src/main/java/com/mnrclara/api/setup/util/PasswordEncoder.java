package com.mnrclara.api.setup.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PasswordEncoder {
	
	static BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * 
	 */
	public PasswordEncoder () {
		passwordEncoder = new BCryptPasswordEncoder();
	}
	
	/**
	 * 
	 * @return
	 */
	public BCryptPasswordEncoder getEncoder () {
		return passwordEncoder;
	}
	
	/**
	 * 
	 * @param password
	 * @return
	 */
	public static String encodePassword (String password) {
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	
	/**
	 * 
	 * @param loginPassword
	 * @param dbPassword
	 * @return
	 */
	public boolean matches (String loginPassword, String dbPassword) {
		return passwordEncoder.matches(loginPassword, dbPassword);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String password = "iwe";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println(hashedPassword);
		System.out.println(hashedPassword.equalsIgnoreCase(hashedPassword));
	}
}
