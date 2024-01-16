package com.tekclover.wms.api.billing.service.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.service.CaseInfoSheetService;
import com.mnrclara.api.management.service.ClientGeneralService;
import com.mnrclara.api.management.service.MatterGenAccService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchJobScheduler {
	
	@Autowired
	ClientGeneralService clientGeneralService;
	
	@Autowired
	CaseInfoSheetService caseInfoSheetService;
	
	@Autowired
	MatterGenAccService matterGenAccService;
	
//	@Scheduled(fixedDelay = 5000)
	public void processClient() throws IllegalAccessException, InvocationTargetException {
		clientGeneralService.syncupClientWithDocket();
	}
	
//	@Scheduled(fixedDelay = 10000)
	public void processMatter() throws IllegalAccessException, InvocationTargetException {
		caseInfoSheetService.syncupMatterWithDocket();
	}
	
//	@Scheduled(cron = "0 15 5 * * ?")
	@Scheduled(fixedDelay = 5000)
	public void processDocketwiseUpdateForAllMatters() throws IllegalAccessException, InvocationTargetException {
		log.info("----------processDocketwiseUpdateForAllMatters-----------started------>: " + new Date());
		matterGenAccService.scheduleUpdatePriorityDateForAllMatters();
		log.info("----------processDocketwiseUpdateForAllMatters-----------ended------>: " + new Date());
	}
}