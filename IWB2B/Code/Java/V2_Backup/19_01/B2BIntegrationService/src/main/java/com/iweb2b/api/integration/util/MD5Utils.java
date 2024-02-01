package com.iweb2b.api.integration.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Utils {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private static final String OUTPUT_FORMAT = "%-20s:%s";

	/**
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] digest(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		byte[] result = md.digest(input);
		return result;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param jsonString
	 * @param privateKey
	 * @return
	 */
	public static String convertToBase64NMD5 (String jsonString, String privateKey) {
		// base64(md5(Json+privateKey of business parameters))
		String ptext = jsonString + privateKey;
//		String ptext = "J0086024173" + "3B29A9C5728BF3E1DB0C4D66B79748B7" + "a0a1047cce70493c9d5d29704f05d0d9";
		try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(ptext.getBytes());
	        byte byteData[] = md.digest();
	        String base64EncodedString = Base64.getEncoder().encodeToString(byteData);
	        System.out.println (base64EncodedString);
	        return base64EncodedString;
	    } catch(java.security.NoSuchAlgorithmException missing) {
	        return "Error.";
	    }
	}

	public static void main(String[] args) {
		String pText = "Hello MD5";
		System.out.println(String.format(OUTPUT_FORMAT, "Input (string)", pText));
//		System.out.println(String.format(OUTPUT_FORMAT, "Input (length)", pText.length()));

		byte[] md5InBytes = MD5Utils.digest(pText.getBytes(UTF_8));
		System.out.println(String.format(OUTPUT_FORMAT, "MD5 (hex) ", bytesToHex(md5InBytes)));
		// fixed length, 16 bytes, 128 bits.
//		System.out.println(String.format(OUTPUT_FORMAT, "MD5 (length)", md5InBytes.length));
		
		//String jsonString = "{\"serviceType\":\"01\",\"orderType\":\"2\",\"receiver\":{\"area\":\"AlBirriyyah\",\"address\":\"OlayaStreetNorthavenue\",\"phone\":\"+966523456789\",\"city\":\"Riyadh\",\"countryCode\":\"KSA\",\"mobile\":\"+966523456789\",\"name\":\"Ashok\",\"longitude\":\"46.8142405\",\"latitude\":\"24.7923195\",\"prov\":\"AlBirriyyah\"},\"expressType\":\"EZKSA\",\"deliveryType\":\"04\",\"operateType\":1,\"weight\":10.0,\"customerCode\":\"J0086024173\",\"txlogisticId\":\"TESTY12314951\",\"goodsType\":\"ITN1\",\"totalQuantity\":0,\"sender\":{\"area\":\"AdDarAlBaida\",\"address\":\"Behindpetrolbunk,aldhostreetAldho\",\"phone\":\"+966523456789\",\"city\":\"Riyadh\",\"countryCode\":\"KSA\",\"mobile\":\"+966523456789\",\"name\":\"ASHOK\",\"prov\":\"AdDarAlBaida\"},\"digest\":\"VdlpKaoq64AZ0yEsBkvt1A==\",\"items\":[]}";
		String jsonString = "{\r\n"
				+ "    \"serviceType\":\"01\",\r\n"
				+ "    \"orderType\":\"2\",\r\n"
				+ "    \"receiver\":{\r\n"
				+ "        \"area\":\"Al Birriyyah\",\r\n"
				+ "        \"address\":\"Olaya StreetNorth avenue\",\r\n"
				+ "        \"phone\":\"+966523456789\",\r\n"
				+ "        \"city\":\"Riyadh\",\r\n"
				+ "        \"countryCode\":\"KSA\",\r\n"
				+ "        \"mobile\":\"+966523456789\",\r\n"
				+ "        \"name\":\"Ashok\",\r\n"
				+ "        \"longitude\":\"46.8142405\",\r\n"
				+ "            \"latitude\":\"24.7923195\",\r\n"
				+ "        \"prov\":\"Al Birriyyah\"\r\n"
				+ "    },\r\n"
				+ "    \"expressType\":\"EZKSA\",\r\n"
				+ "    \"deliveryType\":\"04\",\r\n"
				+ "    \"operateType\":1,\r\n"
				+ "    \"weight\":10.0,\r\n"
				+ "    \"customerCode\":\"J0086024173\",\r\n"
				+ "    \"txlogisticId\":\"TESTY12314951\",\r\n"
				+ "    \"goodsType\":\"ITN1\",\r\n"
				+ "    \"totalQuantity\":0,\r\n"
				+ "    \"sender\":{\r\n"
				+ "        \"area\":\"Ad Dar Al Baida\",\r\n"
				+ "        \"address\":\"Behind petrol bunk, aldho streetAldho\",\r\n"
				+ "        \"phone\":\"+966523456789\",\r\n"
				+ "        \"city\":\"Riyadh\",\r\n"
				+ "        \"countryCode\":\"KSA\",\r\n"
				+ "        \"mobile\":\"+966523456789\",\r\n"
				+ "        \"name\":\"ASHOK\",\r\n"
				+ "        \"prov\":\"Ad Dar Al Baida\"\r\n"
				+ "    },\r\n"
				+ "    \"digest\":\"VdlpKaoq64AZ0yEsBkvt1A==\",\r\n"
				+ "    \"items\":[     \r\n"
				+ "    ]\r\n"
				+ "}";
		String privateKey = "a0a1047cce70493c9d5d29704f05d0d9";
		convertToBase64NMD5 (jsonString, privateKey);

	}
}