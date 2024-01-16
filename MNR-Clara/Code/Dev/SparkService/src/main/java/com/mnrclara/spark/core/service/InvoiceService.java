package com.mnrclara.spark.core.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.persistence.Column;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import com.mnrclara.spark.core.model.InvoiceHeader;
import com.mnrclara.spark.core.model.SearchInvoiceHeader;
import com.mnrclara.spark.core.util.DateUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceService {

	Properties connProp = new Properties();
	SparkSession spark = null;

	public InvoiceService() {
		// Connection Properties
		connProp.setProperty("driver", "com.mysql.cj.jdbc.Driver");
		connProp.put("user", "root");
		connProp.put("password", "30NcyBuK");

		spark = SparkSession.builder().master("local[*]").appName("SparkByExamples.com").getOrCreate();

		// Read from MySQL Table
		val df2 = spark.read().jdbc("jdbc:mysql://35.154.84.178:3306/MNRCLARA", "tblinvoiceheader", connProp);
		df2.createOrReplaceTempView("tblinvoiceheader");
	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceHeader> findInvoiceHeaders(SearchInvoiceHeader searchInvoiceHeader) throws Exception {
		/*
		 * @Column(name = "") private Date ;
		 */
		Dataset<Row> queryDF = spark.sql("SELECT LANG_ID as languageId, CLASS_ID as classId, \r\n"
				+ "	MATTER_NO as matterNumber, CLIENT_ID as clientId, \r\n"
				+ "	CASE_CATEGORY_ID as caseCategoryId, CASE_SUB_CATEGORY_ID as caseSubCategoryId,\r\n"
				+ "	INVOICE_NO as invoiceNumber, INVOICE_FISCAL_YEAR as invoiceFiscalYear,\r\n"
				+ "	INVOICE_PERIOD as invoicePeriod, PRE_BILL_BATCH_NO as preBillBatchNumber,\r\n"
				+ "	PRE_BILL_NO as preBillNumber, POSTING_DATE as postingDate	\r\n" + "	FROM tblinvoiceheader");

		String fromDate = "2022-07-01 00:00:00";
		String toDate = "2023-07-31 23:59:59";
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate);
		Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate);
//		log.info("convertStringToDate-------> : " + date1);

//		 queryDF
//		.filter(a->a.getTimestamp(2).after(date1) && a.getTimestamp(2).before(date2))
//		.show();
//		 Date[] dateConv = DateUtils.addTimeToDates (searchInvoiceHeader.getStartInvoiceDate(), searchInvoiceHeader.getEndInvoiceDate());
		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String strDate1 = dateFormat.format(searchInvoiceHeader.getStartInvoiceDate());
//		String strDate2 = dateFormat.format(searchInvoiceHeader.getEndInvoiceDate());
//		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate1);
//		Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate2);

		log.info("dateConv[0]-------> : " + date1);
		log.info("dateConv[1]-------> : " + date2);

		Encoder<InvoiceHeader> invoiceHeaderEncoder = Encoders.bean(InvoiceHeader.class);
		Dataset<InvoiceHeader> dataSetInvoiceHeaderRows = queryDF.filter(a -> a.getTimestamp(11).after(date1) && a.getTimestamp(11).before(date2))
				.as(invoiceHeaderEncoder);
		return dataSetInvoiceHeaderRows.collectAsList();
	}
}
