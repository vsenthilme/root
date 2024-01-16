package com.mnrclara.qb.ws.services.utils;

import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

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
	 * @param date
	 * @return
	 */
	public static String dateToString (Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateToStr = dateFormat.format(date);
		System.out.println("Date is "+ dateToStr);
		return dateToStr;
	}
	
	/**
	 * 
	 * @param inputDate
	 * @return
	 * @throws ParseException
	 */
	public static void convertToQBDate (String inputDate) throws ParseException {
		inputDate = inputDate.substring(0, inputDate.indexOf("T"));
		log.info("inputDate:" + inputDate);
	}
	
	public static void main(String[] args) throws ParseException {
//		dateToString (new Date());
		
		String sdate = "2022-10-31T05:00:00.000+00:00";
		convertToQBDate (sdate);
	}
}
