package com.iweb2b.core.util;

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
		String password = "webhook";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println(hashedPassword);
		System.out.println(hashedPassword.equalsIgnoreCase(hashedPassword));
		
		String authorization = "Basic $2a$10$nzkfksOOLCI7mupALKmlzOQE7Zq6dCl15d4W9ZAvjR/laGXGhHt5G";
		
		// Lifetoken : Basic $2a$10$yxoEUIpQBTnAkUCoL3j2SOXk1NwNrsCG9UGSffBDg2tmQ2U3cpcrK
		String passedToken = authorization.substring(6);
		System.out.println("00-" + passedToken);
		
//		boolean isValid = passwordEncoder.matches(passedToken, password);
		if ("$2a$10$nzkfksOOLCI7mupALKmlzOQE7Zq6dCl15d4W9ZAvjR/laGXGhHt5G".equals(passedToken)) {
			System.out.println("ss");
		}
	}
}
