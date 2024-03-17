package com.iweb2b.api.integration.util;

import java.util.List;

import lombok.Data;

@Data
public class User {
	private String name;
	private int age;
	private List<String> messages;
	public User () {
		
	}
}