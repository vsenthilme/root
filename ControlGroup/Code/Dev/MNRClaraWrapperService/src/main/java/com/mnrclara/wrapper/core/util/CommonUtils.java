package com.mnrclara.wrapper.core.util;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static String decodeString () throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
		String str = 
				"%7B%22QBXML%22:%20%7B%22QBXMLMsgsRs%22:%20%7B%22CustomerAddRs%22:%20%7B%0A%20%20%20%20%22requestID%22:%20%22Q3VzdG9tZXJBZGR8MTExMTIxMjE%3D%22,%0A%20%20%20%20%22statusSeverity%22:%20%22Error%22,%0A%20%20%20%20%22statusMessage%22:%20%22There%20was%20an%20error%20when%20saving%20a%20Customers%20list,%20element%20%5C%22test%20f%5C%22.%20%22,%0A%20%20%20%20%22statusCode%22:%203180%0A%7D%7D%7D%7D";
		 String result = java.net.URLDecoder.decode(str, StandardCharsets.UTF_8.name());
		 System.out.println(result);
		 ObjectMapper mapper = new ObjectMapper();
		 Map<String,Object> map = mapper.readValue(result, Map.class);
		 for (String s : map.keySet()) {
			 
			 Map<String,Object> owner = (Map<String, Object>)map.get(s);
			 Map<String,Object> owner2 = (Map<String, Object>)owner.get("QBXMLMsgsRs");
			 Map<String,Object> owner3 = (Map<String, Object>)owner2.get("CustomerAddRs");
			 System.out.println(owner3.get("statusMessage"));
		 }
		 return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public String randomUUID() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		System.out.println(generatedString);
		return generatedString;
	}

	/**
	 * 
	 * @param errorMsg
	 * @return
	 */
	public Map<String, String> prepareErrorResponse(String errorMsg) {
		errorMsg = errorMsg.substring(errorMsg.indexOf('['));
		JSONArray array = new JSONArray(errorMsg);
		Map<String, String> mapError = new HashMap<>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			for (Object key : object.names()) {
				mapError.put(String.valueOf(key), object.getString(String.valueOf(key)));
			}
		}
		return mapError;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames(Object source) {
		if (source != null) {
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
		return null;
	}
	
	/**
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static void main(String[] args) throws ParseException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
//		List<Double> numbers = Arrays.asList(1D, null, 3D, 4D, 5D, null, 7D, 8D, 9D, 10D);	
//		Double summingIntValue = numbers.stream().filter(a->a!=null).collect(Collectors.summingDouble(Double::longValue));
//		System.out.println("Sum of integers using summingInt : " + summingIntValue);
		
		Man m1 = new CommonUtils().new Man("A1", 12);
		Man m2 = new CommonUtils().new Man("A2", 42);
		Man m3 = new CommonUtils().new Man("A23", 32);
		
		List<Man> mans = new ArrayList<>();
		mans.add(m1);
		mans.add(m2);
		mans.add(m3);
		
		Man[] m = mans.toArray(new Man[mans.size()]);
		for (Man mm : m) {
			log.info("M : " + mm.getName());
		}
	}
	
	@Data
	@AllArgsConstructor
	class Man {
		private String name;
		private int age;
	}
}
