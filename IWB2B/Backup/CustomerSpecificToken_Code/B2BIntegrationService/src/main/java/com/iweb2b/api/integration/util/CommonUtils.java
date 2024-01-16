package com.iweb2b.api.integration.util;

import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

	/**
	 * 
	 * @author Murugavel
	 *
	 */
	public static enum DashboardTypes {
		AGREEMENT, AGREEMENT_TOTAL, AGREEMENT_SENT, AGREEMENT_RECEIVED, AGREEMENT_RESENT
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
	 * copy
	 * 
	 * @param fromBean
	 * @return
	 */
	public static Object copy(Object fromBean) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEncoder out = new XMLEncoder(bos);
		out.writeObject(fromBean);
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		XMLDecoder in = new XMLDecoder(bis);
		Object toBean = in.readObject();
		in.close();
		return toBean;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getStatusMapping(String mapString) {
		/*
		 * Pickup scan - Pickup completed
		 * Station sending/DC sending - Intransit to HUB
		 * Station arrival - Intransit to Hub
		 * DC arrival - Inscan at Hub
		 * Delivery Scan - Attempted
		 * Sign Scan - Delivered
		 * Abnormal Parcel Scan - Cancel
		 * Returned Parcel Scan - RTO
		 * Returned Signed - RTO
		 */
		Map<String, String> mapStatus = new HashMap<>();
		mapStatus.put("PICKUP SCAN", "shipment_clear_successfully");
		mapStatus.put("STATION SENDING/DC SENDING", "intransit_to_hub");
		mapStatus.put("STATION ARRIVAL", "inscan_at_hub");	
		mapStatus.put("DC ARRIVAL", "reached_at_hub");
		mapStatus.put("DELIVERY SCAN", "accept");
		mapStatus.put("SIGN SCAN", "delivered");
		mapStatus.put("ABNORMAL PARCEL SCAN", "attempted");
		mapStatus.put("RETURNED PARCEL SCAN", "rto");
		mapStatus.put("RETURNED SIGNED", "delivered");
		return mapStatus.get(mapString);
	}
	
	/**
	 * 
	 * @param date
	 * @param tz
	 * @return
	 */
	public static String toISOString(Date date, TimeZone tz) {
        try {
        	String FORMAT_DATE_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			if (tz == null) {
			    tz = TimeZone.getDefault();
			}
			DateFormat f = new SimpleDateFormat(FORMAT_DATE_ISO);
			f.setTimeZone(tz);
			return f.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

	/**
	 * 
	 * @param date
	 * @return
	 */
    public static String toISOString (Date date) {
        return toISOString(date, TimeZone.getDefault());
    }

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		LocalDate localDate = LocalDate.now();
//		System.out.println(localDate);
//		
//		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDateTime datetime = LocalDateTime.now();
//		String currentDatetime = datetime.format(newPattern);
//		System.out.println(currentDatetime);
		
		String date = toISOString(new Date());
		log.info("date : " + date);
	}
}
