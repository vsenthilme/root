package com.iweb2b.api.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.integration.config.PropertiesConfig;
import com.iweb2b.api.integration.model.consignment.dto.CancelShipmentRequest;
import com.iweb2b.api.integration.model.consignment.dto.CancelShipmentResponse;
import com.iweb2b.api.integration.model.tracking.ConsignmentTracking;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsignmentTrackingService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	IntegrationService integrationService;

	/**
	 * 
	 * @param referrenceNumber
	 * @return
	 */
	public ConsignmentTracking getConsignmentTracking (String referrenceNumber) {
		// https://demodashboardapi.shipsy.in/api/client/integration/consignment/track?reference_number=E1234567
		ConsignmentTracking consignmentTracking = integrationService.getClientConsignmentTracking(referrenceNumber);
//		log.info("consignmentTracking------> : " + consignmentTracking);
		return consignmentTracking;
	}
	
	/**
	 * 
	 * @param wayBillNumber
	 * @return
	 */
	public ConsignmentTracking getConsignmentTrackingByWayBillNo (String wayBillNumber) {
		// Getting ReferenceNumber from our local DB for the given wayBillNumber
		String referrenceNumber = integrationService.getConsigmentByWayBillNumber (wayBillNumber);
		ConsignmentTracking consignmentTracking = integrationService.getClientConsignmentTracking(referrenceNumber);
		log.info("consignmentTracking------> : " + consignmentTracking);
		return consignmentTracking;
	}
	
	/**
	 * 
	 * @param cancelShipmentRequest
	 * @return
	 */
	public CancelShipmentResponse cancelShipment (CancelShipmentRequest cancelShipmentRequest) {
		CancelShipmentResponse cancelShipmentResponse = integrationService.cancelShipment(cancelShipmentRequest);
		log.info("cancelShipmentResponse------> : " + cancelShipmentResponse);
		return cancelShipmentResponse;
	}
}
