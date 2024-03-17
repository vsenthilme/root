package com.mnrclara.api.common.service;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.sms.SMS;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SMSService {

	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * API Username: MNR API Password: NpawABXs API Company Key: 3NfkSpV2QzKfFtB
	 * Phone Number: 501.413.1951
	 * 
	 * @return
	 */
	public Boolean sendSMS(SMS newSMS) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> request = new HttpEntity<String>(headers);

			LocalDateTime date = LocalDateTime.now();
			int seconds = date.toLocalTime().toSecondOfDay();

//		Production API URL: https://www.mozeo.com/mozeo/customer/sendtxt.php
			String messagingApiUrl = propertiesConfig.getMozeoUrl();

//		Dev APi URL: https://www.mozeo.com/mozeo/customer/sendtxt-dev.php
//		String messagingApiUrl = "https://www.mozeo.com/mozeo/customer/sendtxt-dev.php"; // Dev

			String accessTokenUrl = messagingApiUrl; // "https://www.mozeo.com/mozeo/customer/sendtxt-dev.php";
			accessTokenUrl += "?to=" + newSMS.getToNumber() + "&datetimestamp=" + seconds + "&messagebody='"
					+ newSMS.getTextMessage() + "'" + "&companykey=3NfkSpV2QzKfFtB" + "&username=MNR" + "&password=NpawABXs"
					+ "&stop=no";
			log.info("accessTokenUrl : " + accessTokenUrl);

			ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);
			log.info("Access Token Response ---------" + response.getBody());
			
			String xmlString = response.getBody();
			Document doc = convertStringToXMLDocument(xmlString);
			String smsResponse = doc.getElementsByTagName("sendsuccess").item(0).getFirstChild().getNodeValue();
			log.info("smsResponse : " + smsResponse);
			
			if (smsResponse != null && smsResponse.equalsIgnoreCase("yes")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 *
	 * @param newSMS
	 * @return
	 */
	public Boolean sendSMSBodyQuoteOmitted(SMS newSMS) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> request = new HttpEntity<String>(headers);

			LocalDateTime date = LocalDateTime.now();
			int seconds = date.toLocalTime().toSecondOfDay();

			String messagingApiUrl = propertiesConfig.getMozeoUrl();
			String accessTokenUrl = messagingApiUrl; // "https://www.mozeo.com/mozeo/customer/sendtxt-dev.php";
			accessTokenUrl += "?to=" + newSMS.getToNumber() + "&datetimestamp=" + seconds + "&messagebody="
					+ newSMS.getTextMessage() + "&companykey=3NfkSpV2QzKfFtB" + "&username=MNR" + "&password=NpawABXs"
					+ "&stop=no";
			log.info("accessTokenUrl : " + accessTokenUrl);

			ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);
			log.info("Access Token Response ---------" + response.getBody());

			String xmlString = response.getBody();
			Document doc = convertStringToXMLDocument(xmlString);
			String smsResponse = doc.getElementsByTagName("sendsuccess").item(0).getFirstChild().getNodeValue();
			log.info("smsResponse : " + smsResponse);

			if (smsResponse != null && smsResponse.equalsIgnoreCase("yes")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		String xmlString = "<?xml version=\"1.0\"?>\r\n" + "<message>\r\n"
				+ "<companykey>3NfkSpV2QzKfFtB</companykey>\r\n" + "<to>8323170701</to>\r\n"
				+ "<datetimestamp>36382</datetimestamp>\r\n" + "<messageid>16541643836</messageid>\r\n"
				+ "<messagebody>'Thanks for Inquiring us, Our Legal team will contact you shortly - Team M</messagebody>\r\n"
				+ "<sendsuccess>Yes</sendsuccess>\r\n" + "<errorcode></errorcode>\r\n" + "</message>\r\n";
		// Use method to convert XML string content to XML Document object
		Document doc = convertStringToXMLDocument(xmlString);

		// Verify XML document is build correctly
		log.info(doc.getFirstChild().getNodeName());
		log.info("Doc : " + doc.getElementsByTagName("sendsuccess").item(0).getFirstChild().getNodeValue());
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
