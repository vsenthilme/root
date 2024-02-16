package com.mnrclara.wrapper.core.batch.scheduler;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mnrclara.wrapper.core.service.AccountingService;
import com.mnrclara.wrapper.core.service.ManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchJobScheduler {
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private Job jobClientGeneral;
	
	@Autowired
	private Job jobClientNote;
	
	@Autowired
	private Job jobMatterAssignment;
	
	@Autowired
	private Job jobMatterExpense;
	
	@Autowired
	private Job jobMatterGenAcc;
	
	@Autowired
	private Job jobMatterNote;
	
	@Autowired
	private Job jobMatterRate;
	
	@Autowired
	private Job jobMatterTimeTicket;
	
	@Autowired
	private Job jobInvoiceHeader;
	
	@Autowired
	private Job jobInvoiceLine;
	
	@Autowired
	private Job jobPaymentPlanHeader;
	
	@Autowired
	private Job jobPaymentPlanLine;
	
	@Autowired
	private Job jobPaymentUpdate;
	
	@Autowired
	private Job dbToCsvJob;
	
	@Autowired
	private JobLauncher jobLauncher;
	
//	@Scheduled(cron = "0 0 10 * * *") 
	@Scheduled(fixedDelay = 300000)
	public void sendReminderSMSForExpirationDoc() {
		log.info("sendReminderSMS - ExpirationDoc called at :" + new Date());
		managementService.sendReminderSMS();
	}
	
	@Scheduled(cron = "0 0 10 * * *")
	public void sendReminderSMSForPaymentPlan() {
		log.info("sendReminderSMS - PaymentPlan called at :" + new Date());
		accountingService.sendReminderSMS();
	}
	
	//-----------------------------------------------------------------------------------
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobClientGeneral() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobClientGeneral, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobClientNote() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobClientNote, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterAssignment() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterAssignment, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterExpense() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterExpense, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterGenAcc() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterGenAcc, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterNote() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterNote, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterRate() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterRate, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobMatterTimeTicket() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobMatterTimeTicket, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobInvoiceHeader() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobInvoiceHeader, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobInvoiceLine() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobInvoiceLine, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobPaymentPlanHeader() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobPaymentPlanHeader, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobPaymentPlanLine() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobPaymentPlanLine, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void runJobPaymentUpdate() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobPaymentUpdate, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void runJobdbToCsvJob() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(dbToCsvJob, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}