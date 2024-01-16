package com.mnrclara.api.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

	public static String dateConv(String input) throws ParseException {
//		String input = "2017-01-18 20:10:00";
		DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		
		LocalDateTime datetime = LocalDateTime.parse(input, oldPattern); 
		String output = datetime.format(newPattern); 
		System.out.println("Date in old format (java 8) : " + input); 
		System.out.println("Date in new format (java 8) : " + output);
		return output;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCurrentDateTime () {
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime datetime = LocalDateTime.now();
		String currentDatetime = datetime.format(newPattern);
		return currentDatetime;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCurrentTimestamp () {
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMddyyyy_HHmmss");
		LocalDateTime datetime = LocalDateTime.now();
		String currentDatetime = datetime.format(newPattern);
		return currentDatetime;
	}
	
	public static void main(String[] args) {
//		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
//		LocalDateTime datetime = LocalDateTime.now();
//		String output = datetime.format(newPattern); 
//		System.out.println("Date in new format (java 8) : " + output);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
		String startTime = "20211102 053000";
		String endTime = "20211102 063000";
		LocalDateTime sDatetime = LocalDateTime.parse(startTime, formatter);
		LocalDateTime eDatetime = LocalDateTime.parse(endTime, formatter);
		System.out.println("DateTime " + sDatetime + "," + eDatetime);
	}
	
	public static void calculateTime () {
		
		Date date = new Date();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
		String strDate = dateFormat.format(date);  
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String input = "2021-11-02 12:10:00";
//		LocalDateTime datetime = LocalDateTime.parse(input, formatter);
		LocalDateTime datetime = LocalDateTime.parse(strDate, formatter); 
		
		LocalDateTime currentDatetime = LocalDateTime.now();
		String strCurrentDatetime = currentDatetime.format(formatter);		
		LocalDateTime dateTime2 = LocalDateTime.parse(strCurrentDatetime, formatter);

		long toHours = java.time.Duration.between(datetime, dateTime2).toHours();
		log.info("toHours :" + toHours);
		
		long toMinutes = java.time.Duration.between(datetime, dateTime2).toMinutes();
		log.info("toMinutes :" + toMinutes);
		
		long toSeconds = java.time.Duration.between(datetime, dateTime2).toSeconds();
		log.info("toSeconds :" + toSeconds);
	}
}
