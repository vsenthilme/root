package com.iweb2b.core.util;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

	/**
	 * 
	 * @return
	 */
	public String randomUUID () {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

		System.out.println(generatedString);
		return generatedString;
	}
	
	/**
	 * 
	 * @param errorMsg
	 * @return
	 */
	public Map<String, String> prepareErrorResponse (String errorMsg) {
		errorMsg = errorMsg.substring(errorMsg.indexOf('['));
		JSONArray array = new JSONArray(errorMsg);
		Map<String, String> mapError = new HashMap<>();
		for(int i = 0; i < array.length(); i ++) {  
			JSONObject object = array.getJSONObject(i);
			for (Object key : object.names()) {
				mapError.put(String.valueOf(key), object.getString(String.valueOf(key)));
			}
		}
		return mapError;
	}
	
	/**
	 * getNullPropertyNames
	 * 
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
	
	/**
	 * 
	 * @param escapeText
	 * @return
	 */
	public static String escapeComma (String escapeText) {
		String escaped = StringEscapeUtils.escapeCsv(escapeText); // I said "Hey, I am 5'10"."
		return escaped;
	}	
	
	/**
	 * 
	 * @param args
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		String password = "test";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		System.out.println(hashedPassword);
		
//		escapeComma();
		
		SPojo d = new SPojo();
		d.setUI_REF_KEY("");
		d.setAR_UNAME("jj");
		d.setBUKRS("");
		d.setEKGRP("");
		d.setEKORG("");
		d.setHOUSE_NO("");
		d.setNAME1("");
		d.setNAME2("");
		d.setPLANT("");
		d.setSTREET("");
		d.setTEXT1("");
		d.setTEXT2("");
		d.setTEXT3("");
		d.setVEND1("");
		d.setVEND2("");
		d.setVEND3("");
		d.setPO_ITEMSet(new String[] {});
		d.setPO_TEXTSet(new String[] {});
		d.setPOReturnSet(new String[] {});
		
		D d1 = new D();
		d1.setD(d);
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(d1);
        System.out.println(json);
	}
}

/*
 * 
 * {
    "d": {
        "UI_REF_KEY": "1",
        "BUKRS": "HARI",
        "PLANT": "1000",
        "EKORG": "1000",
        "EKGRP": "101",
        "AR_UNAME": "",
        "NAME1": "",
        "NAME2": "",
        "STREET": "",
        "HOUSE_NO": "",
        "VEND1": "",
        "VEND2": "",
        "VEND3": "",
        "TEXT1": "",
        "TEXT2": "",
        "TEXT3": "",
        "PO_ITEMSet": [
            {}
        ],
        "PO_TEXTSet": [
            {}
        ],
        "POReturnSet": [
            {}
        ]
    }
}
 */


@Data
class D {
	SPojo d = new SPojo();
}

@Data
class SPojo {
	@JsonProperty("UI_REF_KEY")
	private String UI_REF_KEY;
	
	@JsonProperty("BUKRS")
    private String BUKRS;
	
	@JsonProperty("PLANT")
    private String PLANT;
    
	@JsonProperty("EKORG")
    private String EKORG;
    
	@JsonProperty("EKGRP")
    private String EKGRP;
    
	@JsonProperty("AR_UNAME")
    private String AR_UNAME;
    
	@JsonProperty("NAME1")
    private String NAME1;
    
	@JsonProperty("NAME2")
    private String NAME2;
	
	@JsonProperty("STREET")
    private String STREET;
	
	@JsonProperty("HOUSE_NO")
    private String HOUSE_NO;
	
	@JsonProperty("VEND1")
    private String VEND1;
	
	@JsonProperty("VEND2")
    private String VEND2;
	
	@JsonProperty("VEND3")
    private String VEND3;
	
	@JsonProperty("TEXT1")
    private String TEXT1;
	
	@JsonProperty("TEXT2")
    private String TEXT2;
	
	@JsonProperty("TEXT3")
    private String TEXT3;
	
	@JsonProperty("PO_ITEMSet")
	private String[] PO_ITEMSet;
	
	@JsonProperty("PO_TEXTSet")
	private String[] PO_TEXTSet;
	
	@JsonProperty("POReturnSet")
	private String[] POReturnSet;
}
