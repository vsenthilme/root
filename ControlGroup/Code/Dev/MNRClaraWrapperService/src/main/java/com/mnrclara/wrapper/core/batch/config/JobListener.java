package com.mnrclara.wrapper.core.batch.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mnrclara.wrapper.core.config.PropertiesConfig;
import com.mnrclara.wrapper.core.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobListener implements JobExecutionListener {
	private JobExecution activeJob;

	@Autowired
	private JobOperator jobOperator;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		final String jobName = jobExecution.getJobInstance().getJobName();
		final BatchStatus batchStatus = jobExecution.getStatus();

		log.info("JobListener beforeJob with job {} and status {}", jobName, batchStatus.isRunning());

		synchronized (jobExecution) {
			if (activeJob != null && activeJob.isRunning()) {
				log.info("EmployeeJobListener beforeJob isRunning with job {} and status {}", jobName,
						batchStatus.isRunning());
				try {
					jobOperator.stop(jobExecution.getId());
				} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
					e.printStackTrace();
				}
			} else {
				activeJob = jobExecution;
			}
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		final String jobName = jobExecution.getJobInstance().getJobName();
		log.info("jobName : " + jobName);
		
		final BatchStatus jobExecutionStatus = jobExecution.getStatus();
		synchronized (jobExecution) {
			if (jobExecution == activeJob) {
				activeJob = null;
			}
			
			log.info("jobName@@@ : " + jobExecutionStatus.getBatchStatus().name());
			if (jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("FAILED")) {
				log.error("jobName@@@ : " + jobExecutionStatus.getBatchStatus().name());
			}
			
			// jobClientGeneral
			if (jobName.equalsIgnoreCase("jobClientGeneral") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "clientGeneral" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/clientGeneral.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobClientNote
			if (jobName.equalsIgnoreCase("jobClientNote") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "clientNote" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/clientNote.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterAssignment
			if (jobName.equalsIgnoreCase("jobMatterAssignment") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterAssignment" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterAssignment.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterExpense
			if (jobName.equalsIgnoreCase("jobMatterExpense") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterExpense" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterExpense.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterGenAcc
			if (jobName.equalsIgnoreCase("jobMatterGenAcc") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterGenAcc" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterGenAcc.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterNote
			if (jobName.equalsIgnoreCase("jobMatterNote") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterNote" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterNote.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterRate
			if (jobName.equalsIgnoreCase("jobMatterRate") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterRate" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterRate.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobMatterTimeTicket
			if (jobName.equalsIgnoreCase("jobMatterTimeTicket") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "matterTimeTicket" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/matterTimeTicket.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//-------------------------------------------------------------------------------------------------------------
			
			// jobInvoiceHeader
			if (jobName.equalsIgnoreCase("jobInvoiceHeader") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "invoiceHeader" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/invoiceHeader.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobInvoiceLine
			if (jobName.equalsIgnoreCase("jobInvoiceLine") && 
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "invoiceLine" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/invoiceLine.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobPaymentPlanHeader
			if (jobName.equalsIgnoreCase("jobPaymentPlanHeader") && jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "paymentPlanHeader" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/paymentPlanHeader.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobPaymentPlanLine
			if (jobName.equalsIgnoreCase("jobPaymentPlanLine") && jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "paymentPlanLine" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/paymentPlanLine.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// jobPaymentUpdate
			if (jobName.equalsIgnoreCase("jobPaymentUpdate") && jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				 Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "paymentUpdate" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							 	Paths.get(propertiesConfig.getFileUploadDir() + "/paymentUpdate.csv"),
						        Paths.get(renamedFile.getAbsolutePath())
					        );
					 log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//---------------------------------Control Group----------------------------------------------------------------------------
			// StorePartnerListing
			if (jobName.equalsIgnoreCase("jobStorePartnerListing") &&
					jobExecutionStatus.getBatchStatus().name().equalsIgnoreCase("COMPLETED")) {
				Path temp;
				try {
					String time = DateUtils.getCurrentTimestamp();
					String fileName = propertiesConfig.getFileMoveToDir() + "storePartnerListing" + "_" + time + ".csv";
					File renamedFile = new File (fileName);
					temp = Files.move (
							Paths.get(propertiesConfig.getFileUploadDir() + "/storePartnerListing.csv"),
							Paths.get(renamedFile.getAbsolutePath())
					);
					log.info("temp : " + temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
