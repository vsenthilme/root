package com.mnrclara.api.crm.model.wordpress;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddInquiryWebsite {
	
	/*
	 * "department"
	 * "email" 
	 * "firstName"
	 * "language" 
	 * "lastName"
	 * "legelIssueDescription" 
	 * "message" 
	 * "phone" 
	 * "preferabalCommunicationMedium"
	 * "zipCode" 
	 */
	private String department;
	
	@Email (message = "Please correct Email address")
	private String email;
	
	@NotBlank(message = "FirstName is mandatory")
	private String firstName;
	
	@NotBlank(message = "Language is mandatory")
	private String language;
	private String lastName;
	private String legelIssueDescription;
	private String message;
	
	@NotBlank(message = "Phone Number is mandatory")
	private String phone;
	
	private String preferabalCommunicationMedium;
	private String zipCode;
	
	private String serviceType;
	private String formId;
}
