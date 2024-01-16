package com.mnrclara.api.management.util;

import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

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

	public static void main(String[] args) {
//		LocalDate localDate = LocalDate.now();
//		System.out.println(localDate);
//		
//		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDateTime datetime = LocalDateTime.now();
//		String currentDatetime = datetime.format(newPattern);
//		System.out.println(currentDatetime);
		
		List<Long> list1 = new ArrayList<>();
//		list1.add(1L);
//		list1.add(2L);
//		list1.add(3L);
		
//		boolean onlyHourlyAndFlatFee = false;
//		boolean onlyContigency = false;
//		boolean allBillModes = false;
//		if (list1.contains(1L) || list1.contains(2L)) { 
//			onlyHourlyAndFlatFee = true;
//		} 
//		
//		if (list1.contains(3L)) {
//			onlyContigency = true;
//		} 
//		
//		if (list1 == null || list1.size() == 0) {
//			allBillModes = true;
//		}
//		
//		log.info("onlyHourlyAndFlatFee: " + onlyHourlyAndFlatFee);
//		log.info("onlyContigency: " + onlyContigency);
//		log.info("allBillModes: " + allBillModes);
//		
//		double a = 12;
//		Long b = -23L;
//		double dd = a + b;
//		log.info ("dd : " + dd);
		
		CommonUtils c = new CommonUtils();
		c.doProcess();
	}
	
	private static void doProcess () {
		List<String[]> ticketList = new ArrayList<>();
		String[] s1 = new String[] {"100", "X", "10019-01"};
		String[] s2 = new String[] {"101", "Y", "10019-02"};
		
		ticketList.add(s1);
		ticketList.add(s2);
		
		List<String[]> expenseList = new ArrayList<>();
		s1 = new String[] {"1001", "A1", "10019-01"};
		s2 = new String[] {"1011", "B1", "10019-02"};
		String[] s3 = new String[] {"1021", "C1", "10019-03"};
		
		expenseList.add(s1);
		expenseList.add(s2);
		expenseList.add(s3);
		
		List<String> matterNumbers = new ArrayList<>();
		matterNumbers.add("10019-01");
		matterNumbers.add("10019-02");
		matterNumbers.add("10019-03");
		
		List<X> xList = new ArrayList<>();
		matterNumbers.stream().forEach(inputMatterNumber -> {
			CommonUtils.X x = new CommonUtils().new X();
			String[] ticketData = ticketList.stream().filter(t -> t [2].equalsIgnoreCase(inputMatterNumber)).findFirst().orElse(null);
			String[] expenseData = expenseList.stream().filter(t -> t [2].equalsIgnoreCase(inputMatterNumber)).findFirst().orElse(null);
			x.setMatterNumber(inputMatterNumber);
			if (ticketData != null) {
				x.setTicketData(ticketData);
			}
			
			if (expenseData != null) {
				x.setExpenseData(expenseData);
			}
			xList.add(x);
		});
		
		log.info("X-----> : " + xList);
	}
	
	@Data
	class X {
		String matterNumber;
		String[] ticketData;
		String[] expenseData;
	}
}
