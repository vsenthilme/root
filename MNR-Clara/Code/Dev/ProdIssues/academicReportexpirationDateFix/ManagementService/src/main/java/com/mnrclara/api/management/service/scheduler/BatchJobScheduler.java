package com.mnrclara.api.management.service.scheduler;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mnrclara.api.management.service.CaseInfoSheetService;
import com.mnrclara.api.management.service.ClientGeneralService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchJobScheduler {
	
	@Autowired
	ClientGeneralService clientGeneralService;
	
	@Autowired
	CaseInfoSheetService caseInfoSheetService;
	
//	@Scheduled(fixedDelay = 5000)
	public void processClient() throws IllegalAccessException, InvocationTargetException {
		clientGeneralService.syncupClientWithDocket();
	}
	
//	@Scheduled(fixedDelay = 10000)
	public void processMatter() throws IllegalAccessException, InvocationTargetException {
		caseInfoSheetService.syncupMatterWithDocket();
	}
}