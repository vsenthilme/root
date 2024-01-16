package com.mnrclara.api.crm.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {
	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws ParseException
	 */
	private static String dateConvFromUI (String input) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = inputFormat.parse(input); //"2018-04-10T04:00:00.000Z");
		String formattedDate = outputFormat.format(date);
		System.out.println(formattedDate); // prints 10-04-2018
		
		return formattedDate;
	}
	
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
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
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
		log.info("dates---##----> " + dates[0] + "," + dates[1]);
		return dates;
	}
	
	/**
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate (String strDate) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = inputFormat.parse(strDate); //"2018-04-10T04:00:00.000Z");
		String formattedDate = outputFormat.format(date);
		System.out.println(formattedDate); // prints 10-04-2018
		
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);  
		log.info("convertStringToDate-------> : " + date1);
		return date1;
	}
	
	public static Date convertStringDateToDate(String strDate) throws ParseException {
		LocalTime time = LocalTime.now();
		System.out.println("---------> " + time);
		strDate = strDate + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
		
		Date date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(strDate);
		log.info("convertStringToDate-------> : " + date);
		return date;
	}
	
	public static void main(String[] args) throws ParseException {
//		convertStringToDate("2022-05-30T21:00:00.000Z");
		Date d1 = convertStringDateToDate ("07-05-2022");
		Date d2 = new Date ();
		long seconds = (d2.getTime() - d1.getTime())/1000;
		log.info("seconds-------> : " + seconds);
		
		LocalDate sLocalDate =  LocalDate.ofInstant(d2.toInstant(), ZoneId.systemDefault());
		String formattedDate = sLocalDate.format(DateTimeFormatter. ofPattern("yyy-MM-dd"));
		log.info("formattedDate-------> : " + formattedDate);
	}
}
