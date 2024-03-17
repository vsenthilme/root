package com.iweb2b.api.integration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iweb2b.api.integration.config.PropertiesConfig;
import com.iweb2b.api.integration.model.consignment.dto.CancelShipmentRequest;
import com.iweb2b.api.integration.model.consignment.dto.CancelShipmentResponse;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentEntity;
import com.iweb2b.api.integration.model.tracking.ConsignmentTracking;
import com.iweb2b.api.integration.repository.ConsignmentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsignmentTrackingService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	IntegrationService integrationService;
	
	@Autowired
	ConsignmentRepository consignmentRepository;

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
		List<String[]> consignments = consignmentRepository.findConsigmentByCustomerReferenceNumber (cancelShipmentRequest.getCustomerReferenceNo());
		if (consignments != null) {
			for (String[] consignmentArr : consignments) {
				CancelShipmentRequest cancelReq = new CancelShipmentRequest();
				cancelReq.setAWBNo(new String[] {consignmentArr[0]});
				cancelReq.setCustomerReferenceNo(consignmentArr[1]);
				CancelShipmentResponse cancelShipmentResponse = integrationService.cancelShipment(cancelReq);
				log.info("cancelShipmentResponse------> : " + cancelShipmentResponse);
				
				if (cancelShipmentResponse.getStatus() != null && cancelShipmentResponse.getStatus().equalsIgnoreCase("OK")) {
					ConsignmentEntity consignmentEntity = consignmentRepository.findConsigment (consignmentArr[0]);
					consignmentEntity.setCancelStatus(1L);
					consignmentRepository.save(consignmentEntity);
					log.info("cancelShipmentResponse-updated---in---local----> : " + consignmentEntity);
				}
				return cancelShipmentResponse;
			}
		}
		return null;
	}
}
