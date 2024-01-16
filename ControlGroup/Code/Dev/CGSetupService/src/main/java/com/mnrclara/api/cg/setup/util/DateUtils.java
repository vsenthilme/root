package com.mnrclara.api.cg.setup.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
	
	public static String getCurrentDateTime () {
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime datetime = LocalDateTime.now();
		String currentDatetime = datetime.format(newPattern);
		return currentDatetime;
	}
	
	public static void calculateTime () throws ParseException {
		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		String currentDatetime = datetime.format(newPattern);
		LocalDateTime local_date_3 = LocalDateTime.parse(currentDatetime, newPattern);
		log.info("converted : " + local_date_3);
		
		System.out.println(newPattern.format(local_date_3));
	}

	public static Date[] addTimeToDatesForSearch (Date startDate, Date endDate) throws ParseException {
		LocalDate sLocalDate =  LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDate eLocalDate =  LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		log.info("LocalDate1------->  " + sLocalDate.atTime(0, 0, 0));
		log.info("LocalDate2------->  " + eLocalDate.atTime(23, 59, 0));
		
		LocalDateTime sLocalDateTime = sLocalDate.atTime(0, 0, 0);
		LocalDateTime eLocalDateTime = eLocalDate.atTime(23, 59, 0);
		log.info("LocalDate1---##----> " + sLocalDateTime);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
		String sConvertedDateTime = formatter.format(sLocalDateTime);
		String eConvertedDateTime = formatter.format(eLocalDateTime);
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
		Date sDate = dateFormatter.parse(sConvertedDateTime);
		Date eDate = dateFormatter.parse(eConvertedDateTime);
		
		Date[] dates = new Date[] {
			sDate,
			eDate
		};
		return dates;
	}

	/**
	 *
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToYYYYMMDD(String strDate) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		return date;
	}

	public static void main(String[] args) throws ParseException {
//		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
//		LocalDateTime datetime = LocalDateTime.now();
//		String output = datetime.format(newPattern); 
//		System.out.println("Date in new format (java 8) : " + output);
		calculateTime();
	}
}
