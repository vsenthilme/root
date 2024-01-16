package com.tekclover.wms.core.batch.scheduler;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchJobScheduler {

	@Autowired
	private Job jobBomHeader;

	@Autowired
	private Job jobBinLocation;

	@Autowired
	private Job jobBomLine;

	@Autowired
	private Job jobBusinessPartner;

	@Autowired
	private Job jobHandlingEquipment;

	@Autowired
	private Job jobInventory;

	@Autowired
	private Job jobImBasicData1;

	@Autowired
	private Job jobImBasicData1WhId111;

	@Autowired
	private Job jobIMPartner;

	@Autowired
	private Job jobIMPartnerWhId111;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job dbToCsvJob;

	@Autowired
	private Job dbToCsvJob2;

	@Autowired
	private Job periodicJob;

	@Autowired
	private Job jobBinLocationV2;

	@Autowired
	private Job jobIMPartnerV2;

	@Autowired
	private Job jobStatusId;

	@Autowired
	private Job jobItemGroupId;

	@Autowired
	private Job jobSubItemGroupId;

	/**
	 * jobBomHeader
	 * @throws Exception
	 */
	public void runJobBomHeader() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobBomHeader, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @throws Exception
	 *
	 */
	public void runJobBomLine() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobBomLine, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * jobBinLocation
	 */
	public void runJobBinLocation() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobBinLocation, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobBinLocation-V2
	 */
	public void runJobBinLocationV2() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobBinLocationV2, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JobBusinessPartner
	 */
	public void runJobBusinessPartner() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobBusinessPartner, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobHandlingEquipment
	 */
	public void runJobHandlingEquipment() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobHandlingEquipment, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobInventory
	 */
	public void runJobInventory() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobInventory, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobImBasicData1
	 */
	public void runJobImBasicData1() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobImBasicData1, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobImBasicData1WhId111
	 */
	public void runJobImBasicData1WhId111() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobImBasicData1WhId111, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobImBasicData1WhId111
	 */
	public void runJobIMPartner() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobIMPartner, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobImBasicData1WhId111
	 */
	public void runJobIMPartnerWhId111() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobIMPartnerWhId111, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobIMPartnerV2
	 */
	public void runJobIMPartnerV2() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobIMPartnerV2, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * jobStatusId
	 */
	public void runJobStatusId() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobStatusId, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobItemGroupId
	 */
	public void runJobItemGroupId() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobItemGroupId, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * jobSubItemGroupId
	 */
	public void runJobSubItemGroupId() {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(jobSubItemGroupId, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	 *
	 */
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

	public void runJobdbToCsvJob2() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(dbToCsvJob2, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void runJobPeriodic() throws Exception {
		JobParameters params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(periodicJob, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}