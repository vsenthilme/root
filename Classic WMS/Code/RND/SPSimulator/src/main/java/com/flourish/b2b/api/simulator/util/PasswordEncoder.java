package com.flourish.b2b.api.simulator.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

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
	
	
	private static final Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
	private static final String clientId = "pixeltrice";//clientId
	private static final String clientSecret = "pixeltrice-secret-key";//client secret
	private static final String tokenUrl = "http://localhost:8085/oauth/token";
	private static final String auth = clientId + ":" + clientSecret;
	private static final String authentication = Base64.getEncoder().encodeToString(auth.getBytes());
	
//	String clientId = "pixeltrice";
//	String clientSecret = "pixeltrice-secret-key";
//	
//	String keys = "pixeltrice:pixeltrice-secret-key";
//	String URL = "http://localhost:8085/oauth/token";
	
	private static String getClientCredentials() {
	    String content = "grant_type=client_credentials";
	    BufferedReader reader = null;
	    HttpsURLConnection connection = null;
	    String returnValue = "";
	    try {
	        URL url = new URL(tokenUrl);
//	        URL url = new URL(null, tokenUrl, new sun.net.www.protocol.https.Handler());
	        connection =  (HttpsURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + authentication);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        connection.setRequestProperty("Accept", "application/json");
	        PrintStream os = new PrintStream(connection.getOutputStream());
	        os.print(content);
	        os.close();
	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line = null;
	        StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        String response = out.toString();
	        Matcher matcher = pat.matcher(response);
	        if (matcher.matches() && matcher.groupCount() > 0) {
	            returnValue = matcher.group(1);
	        }
	    } catch (Exception e) {
	        System.out.println("Error : " + e.getMessage());
	    } finally {
//	        if (reader != null) {
//	            try {
//	                reader.close();
//	            } catch (IOException e) {
//	            }
//	        }
//	        connection.disconnect();
	    }
	    return returnValue;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println("Token: " + getClientCredentials());
		testIt();
	}
	
	private static void testIt(){

	      String https_url = "https://www.google.com/";
	      URL url;
	      try {

	         url = new URL(https_url);
	         HttpURLConnection con = (HttpURLConnection) url.openConnection();
	         System.out.println(con);
	            
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
}
