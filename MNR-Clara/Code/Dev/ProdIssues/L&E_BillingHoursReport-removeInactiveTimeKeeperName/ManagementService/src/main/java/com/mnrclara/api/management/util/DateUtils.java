package com.mnrclara.api.management.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
	public static String getCurrentDateTime() {
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
	
	public static String getCurrentDate () {
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime datetime = LocalDateTime.now();
		String currentDatetime = datetime.format(newPattern);
		return currentDatetime;
	}
	
	/**
	 * getCurrentMonthFirstAndLastDates - Using by Dashboard
	 * @return
	 */
	public static String[] getCurrentMonthFirstAndLastDates() {
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime datetime = LocalDateTime.now();
		String currentDatetime = datetime.format(newPattern);
		
		LocalDate lastDateOfMonth = LocalDate.parse(currentDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		lastDateOfMonth = lastDateOfMonth.withDayOfMonth(
				lastDateOfMonth.getMonth().length(lastDateOfMonth.isLeapYear()));
		String lastDateOfCurrentMonth = lastDateOfMonth.format(newPattern);
		System.out.println(lastDateOfCurrentMonth);
		
		String date = "01";
		int month = datetime.getMonthValue();
		int year = datetime.getYear();
		date = year + "-" + month + "-" + date;
		System.out.println(date);
		
		return new String [] {date, lastDateOfCurrentMonth};
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static LocalDateTime convertDateToLocalDateTime(Date date, String timeFlag) {
		LocalDate sLocalDate =  LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
		log.info("--------input Date----------> " + sLocalDate);
		if (timeFlag.equalsIgnoreCase("START")) {
			log.info("--------conv Date--1--------> " + sLocalDate.atStartOfDay());
//			return sLocalDate.atStartOfDay();
			return sLocalDate.atTime(5, 30);
		} else {
			LocalDateTime nextTime = sLocalDate.atStartOfDay();
			log.info("--------conv Date--2--------> " + nextTime.plusHours(12));
//			return nextTime.plusHours(12);
			return sLocalDate.atTime(6, 30);
		}
	}
	
	/**
	 * convertDateForSearch
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String[] convertDateForSearch (Date startDate, Date endDate) {
		LocalDate sLocalDate =  LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDate eLocalDate =  LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		log.info("LocalDate1------->  " + sLocalDate.atTime(0, 0, 0));
		log.info("LocalDate2------->  " + eLocalDate.atTime(23, 59, 0));
		
		LocalDateTime sLocalDateTime = sLocalDate.atTime(0, 0, 0);
		LocalDateTime eLocalDateTime = eLocalDate.atTime(23, 59, 0);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String sConvertedDateTime = formatter.format(sLocalDateTime).replace("T", " ");
		String eConvertedDateTime = formatter.format(eLocalDateTime).replace("T", " ");
		
		log.info("---@--> " + sConvertedDateTime);
		log.info("---$--> " + eConvertedDateTime);
		
		String[] dates = new String[] {
			sConvertedDateTime,
			eConvertedDateTime
		};
		return dates;
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
		return dates;
	}
	
	/**
	 * 
	 * @param srcDate
	 * @return
	 * @throws ParseException
	 */
	public static Date addCurrentTimeToDate (Date srcDate) throws ParseException {
		LocalDate sLocalDate =  LocalDate.ofInstant(srcDate.toInstant(), ZoneId.systemDefault());
		int hour = LocalDateTime.now().getHour();
		int min = LocalDateTime.now().getMinute();
		int sec = LocalDateTime.now().getSecond();
		
		LocalDateTime sLocalDateTime = sLocalDate.atTime(hour, min, sec);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String sConvertedDateTime = formatter.format(sLocalDateTime);
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
		Date sDate = dateFormatter.parse(sConvertedDateTime);
		log.info("sDate: " + sDate);
		return sDate;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static Date[] addTimeToDatesForSearch (LocalDate startDate, LocalDate endDate) throws ParseException {
		LocalDateTime sLocalDateTime = startDate.atTime(0, 0, 0);
		LocalDateTime eLocalDateTime = endDate.atTime(23, 59, 0);
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
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareLocalDates(LocalDate date1, LocalDate date2) throws ParseException {
		if (date1.compareTo(date2) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param dateParam1
	 * @param dateParam2
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDates(Date dateParam1, Date dateParam2) throws ParseException {
		LocalDate date1 =  LocalDate.ofInstant(dateParam1.toInstant(), ZoneId.systemDefault());
		LocalDate date2 =  LocalDate.ofInstant(dateParam2.toInstant(), ZoneId.systemDefault());
		if (date1.compareTo(date2) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareLocalDateTime(LocalDateTime date1, LocalDateTime date2) throws ParseException {
		if (date1.compareTo(date2) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date convertStringToDate (String strDate) {
		strDate += " 00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
		Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		log.info("convertStringToDate-------> : " + date);
		return date;
	}
	
	public static Date convertStringToDate2 (String strDate) {
		strDate += " 00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
		Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		log.info("convertStringToDate-------> : " + date);
		return date;
	}

	/**
	 *
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate3(String strDate) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		return date;
	}
	
	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date convertStringToYYYMMDDFormat(String strDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
		Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		log.info("convertStringToYYYMMDDFormat-------> : " + date);
		return date;
	}
	
	/**
	 * 
	 * @param date
	 * @param numberOfDays
	 * @return
	 */
	public static Date subtractDaysFromDate (Date date, long numberOfDays) {
		LocalDate localDate =  LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDate newDate = localDate.minusDays(numberOfDays);
		
		LocalDateTime sLocalDateTime = newDate.atTime(0, 0, 0);
		Date subtractedDate = Date.from(sLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
		log.info("newDate : " + newDate);
		log.info("minusDate : " + subtractedDate);
		return subtractedDate;
	}

	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public static Date[] getCurrentMonth () throws ParseException {
		LocalDate currentdate = LocalDate.now();
	    LocalDate monthBegin = currentdate.withDayOfMonth(1);
	    LocalDate monthEnd = currentdate.plusMonths(1).withDayOfMonth(1).minusDays(1);
	    
	    log.info("monthBegin: " + monthBegin);
	    log.info("monthEnd: " + monthEnd);
	    
		Date[] currentMonth = addTimeToDatesForSearch(monthBegin, monthEnd);
		return currentMonth;
	}
	
	/**
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date[] getPreviousMonth () throws ParseException {
		YearMonth thisMonth = YearMonth.now();
		YearMonth lastMonth = thisMonth.minusMonths(1);
		LocalDate monthBegin = lastMonth.atDay(1);
		LocalDate monthEnd = lastMonth.atEndOfMonth();
		
		log.info("monthBegin: " + monthBegin);
		log.info("monthEnd: " + monthEnd);
		
		Date[] currentMonth = addTimeToDatesForSearch(monthBegin, monthEnd);
		return currentMonth;
	}
	
	/**
	 * 
	 * @param inputDate
	 * @return
	 * @throws ParseException
	 */
	public static Date addTimeToDate (Date inputDate) {
		try {
			LocalDateTime localDateTime = LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.systemDefault());
			localDateTime = localDateTime.plusHours(6);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			String sConvertedDateTime = formatter.format(localDateTime);
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
			Date sDate = dateFormatter.parse(sConvertedDateTime);
			return sDate;
		} catch (Exception e) {
			log.info("Exception in DateUtils: " + e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param fromDate1
	 * @param toDate1
	 * @return 
	 * @throws ParseException
	 */
	public static List<Date[]> splitAgingDates (Date fromDateInput, Date toDateInput) throws Exception {
		LocalDateTime date1 = LocalDateTime.ofInstant(fromDateInput.toInstant(), ZoneId.systemDefault());
		LocalDateTime date2 = LocalDateTime.ofInstant(toDateInput.toInstant(), ZoneId.systemDefault());
		long daysBetween = Duration.between(date2, date1).toDays();
		
		LocalDate fromDate = LocalDate.ofInstant(fromDateInput.toInstant(), ZoneId.systemDefault());
		LocalDate toDate = LocalDate.ofInstant(toDateInput.toInstant(), ZoneId.systemDefault());
		if (daysBetween > 120) {
			daysBetween = 120;
		}
		
		List<Date[]> listDates = new ArrayList<>();
		LocalDate subDate = fromDate.minusDays(30);
		log.info("splitDate: " + fromDate + "-" + subDate);
		Date[] convDates = addTimeToDatesForSearch(fromDate, subDate);
		listDates.add(convDates);
		
		fromDate = subDate.minusDays(1);
		for (int i=30; i < daysBetween; i+=30) {
			LocalDate splitDate = fromDate.minusDays(30);
			LocalDate splitDate1 = splitDate.minusDays(1);
			log.info("splitDate: " + fromDate + "-" + splitDate1);
			convDates = addTimeToDatesForSearch(fromDate, splitDate1);
			listDates.add(convDates);
			fromDate = splitDate1.minusDays(1);
		}
		log.info("splitDate: " + fromDate + ":" + toDate);
		convDates = addTimeToDatesForSearch(fromDate, toDate);
		listDates.add(convDates);
		return listDates;
	}
	
	/*
	 * Current - 0, 30	-> 0
	 * 30, 60			-> 1
	 * 60, 90			-> 2
	 * 90, 120			-> 3
	 * 120 & abv		-> 4
	 */
	public static List<Date[]> buildAgingDates (Date fromDate1, Date toDate1) throws ParseException {
		LocalDate fromDate = LocalDate.ofInstant(fromDate1.toInstant(), ZoneId.systemDefault());
		LocalDate toDate = LocalDate.ofInstant(toDate1.toInstant(), ZoneId.systemDefault());
		log.info("Date --fromDate: " + fromDate + ", toDate:" + toDate);
		
		int fromMonth = fromDate.getMonth().getValue();
		int toMonth = toDate.getMonth().getValue();
		
		log.info("month --fromMonth: " + fromMonth + ", toMonth:" + toMonth);
		List<Date[]> dates = new ArrayList<>();
		
		int year = Year.now().getValue();
		log.info("year:  " + year);
		for (int i = toMonth; i>=fromMonth; i--) {
			YearMonth thisMonth = YearMonth.of(year, i);
			LocalDate monthBegin = thisMonth.atDay(1);
			LocalDate monthEnd = thisMonth.atEndOfMonth();
			Date[] convDates = addTimeToDatesForSearch(monthBegin, monthEnd);
			log.info("Date converted: " + convDates[0] + "," + convDates[1]);
			dates.add(convDates);
		}
		return dates;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString (Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateToStr = dateFormat.format(date);
			return dateToStr;
		}
		return null;
	}

	public static int calculateWorkingDays(Date startDate, Date endDate) {
		int workingDays = 0;

		LocalDate fromDate = LocalDate.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDate toDate = LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

		LocalDate date = fromDate;
		while (!date.isAfter(toDate)) {
			if (isWorkingDay(date)) {
				workingDays++;
			}

			date = date.plusDays(1);
		}

		return workingDays;
	}

	public static boolean isWorkingDay(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static Date[] getPreviousWeekMondayAndFriday() throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate previousFriday = today.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)) ;
		LocalDate previousWeekMonday = previousFriday.minusDays(4);
		Date[] arrDate = addTimeToDatesForSearch(previousWeekMonday, previousFriday);
		log.info("previousWeekMonday: " + arrDate[0]);
		log.info("previousFriday: " + arrDate[1]);
		return arrDate;
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		convertStringToDate2 ("01-01-2000");
		
//		tempCode();
		/*
		 * Dropdown options( current month, current quarter, previous month, previous quarter,Current Year,Previous year)
		 */
//	      LocalDate currentdate = LocalDate.now();
//	      System.out.println("Current date: "+currentdate);
//	      
//	      //Getting the current day
//	      int currentDay = currentdate.getDayOfMonth();
//	      System.out.println("Current day: "+currentDay);
//	      
//	      //Getting the current month
//	      Month currentMonth = currentdate.getMonth();
//	      System.out.println("Current month: "+currentMonth.getValue());
//	      
//	      //getting the current year
//	      int currentYear = currentdate.getYear();
//	      System.out.println("Current currentYear: "+currentYear);
		
//		YearMonth thisMonth = YearMonth.now();
//		YearMonth last = thisMonth.minusMonths(20);
//		YearMonth prev = thisMonth.minusMonths(0);
//		
////		LocalDate prevmonth = prev.atDay(1);
//		LocalDate currentdate = LocalDate.now();
//		LocalDate lastend = last.atEndOfMonth();
//		
//		log.info("monthBegin: " + currentdate);
//		log.info("monthEnd: " + lastend);
////		convertStringToYYYMMDDFormat ("2022-11-02");
//		
//		Date[] d1 = addTimeToDatesForSearch(currentdate, lastend);
////		buildAgingDates (d1[0], d1[1]);
//		
//		List<Date[]> listDates = splitAgingDates (d1[0], d1[1]);
//		log.info("listDates: " + listDates);
	}
}
