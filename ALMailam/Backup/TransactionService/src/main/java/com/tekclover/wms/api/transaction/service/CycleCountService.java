package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.OutboundOrderV2;
import com.tekclover.wms.api.transaction.repository.CycleCountHeaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CycleCountService {
	
	@Autowired
	private CycleCountHeaderRepository cycleCountHeaderRepository;
	
	/**
	 * 
	 * @param warehouseId
	 * @return
	 */
	public void updateProcessedOrderV2(String cycleCountNo) {
		CycleCountHeader dbCycleCountHeader = cycleCountHeaderRepository.findByCycleCountNo(cycleCountNo);
		log.info("cycleCountNo : " + cycleCountNo);
		log.info("dbCycleCountHeader : " + dbCycleCountHeader);
		if (dbCycleCountHeader != null) {
			dbCycleCountHeader.setProcessedStatusId(10L);
			dbCycleCountHeader.setOrderProcessedOn(new Date());
			cycleCountHeaderRepository.save(dbCycleCountHeader);
		}
	}

}
