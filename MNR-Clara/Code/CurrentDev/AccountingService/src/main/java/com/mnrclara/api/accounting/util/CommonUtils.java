package com.mnrclara.api.accounting.util;

import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {
	
	public static enum DashboardTypes {
	    AGREEMENT,
	    AGREEMENT_TOTAL,
	    AGREEMENT_SENT,
	    AGREEMENT_RECEIVED,
	    AGREEMENT_RESENT	    
	}

	/**
	 * getNullPropertyNames
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
	 * @param listMatterNumbers
	 * @return
	 */
	public static List<String> extractMatterNumbers (List<String> listMatterNumbers) {
		List<String> matterNumbersExtractedList = new ArrayList<>();
		for (String matterNumber : listMatterNumbers) {
			if (matterNumber.contains(":") && matterNumber.contains("_")) {
				int colon = matterNumber.indexOf(":");
				int uscore = matterNumber.indexOf("_");
				matterNumber = matterNumber.substring(colon+1, uscore);
				matterNumbersExtractedList.add(matterNumber);
			} else if (matterNumber.contains(":")) {
				int colon = matterNumber.indexOf(":");
				matterNumber = matterNumber.substring(colon+1);
				matterNumbersExtractedList.add(matterNumber);
			} else if (matterNumber.contains("_")) {
				int uscore = matterNumber.indexOf("_");
				matterNumber = matterNumber.substring(0,uscore);
				matterNumbersExtractedList.add(matterNumber);
			} else {
				matterNumbersExtractedList.add(matterNumber);
			}
		}
		log.info("matterNumbersExtractedList : " + matterNumbersExtractedList);
		return matterNumbersExtractedList;
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @return
	 */
	public static String extractMatterNumber (String matterNumber) {
		String extractedMatterNumber = "";
		if (matterNumber.contains(":") && matterNumber.contains("_")) {
			int colon = matterNumber.indexOf(":");
			int uscore = matterNumber.indexOf("_");
			extractedMatterNumber = matterNumber.substring(colon+1, uscore);
		} else if (matterNumber.contains(":")) {
			int colon = matterNumber.indexOf(":");
			extractedMatterNumber = matterNumber.substring(colon+1);
		} else if (matterNumber.contains("_")) {
			int uscore = matterNumber.indexOf("_");
			extractedMatterNumber = matterNumber.substring(0,uscore);
		} else {
			extractedMatterNumber = matterNumber;
		}
//		log.info("extractedMatterNumber : " + extractedMatterNumber);
		return extractedMatterNumber;
	}
	
	public static void main(String[] args) {
//		String phone = "203-333-2222";
//		System.out.println(phone.replaceAll("-", ""));
//		List<String> listMatterNumbers = new ArrayList<>();
//		listMatterNumbers.add("8376:8376-01");
//		listMatterNumbers.add("10078:10078-01_Maria De La Paz Martinez");
//		listMatterNumbers.add("10060-01_Fabiola Madeline Jeronimo de Le");
//		listMatterNumbers.add("53034-01");
//		listMatterNumbers.add("Other Customer");
//		listMatterNumbers.add("8712");
//		listMatterNumbers.add("500018-01_May  Wang");
//		listMatterNumbers.add("SDMIMM1_2022 Immigration Unbillable");
//		extractMatterNumber("8376:8376-01");
//		
//		LocalDate currentDate = LocalDate.of(2022, 7, 1);
//		log.info("" + currentDate);
		
		DecimalFormat df = new DecimalFormat("0.00");
		Double d = 123.444444D;
		log.info("" + Double.valueOf(df.format(d)));
		
	}
}
