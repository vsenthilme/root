package com.tekclover.wms.api.transaction.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.FindInboundOrderLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.FindInboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.FindOutboundOrderLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.FindOutboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.OutboundOrderLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.OutboundOrderV2;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.InboundOrderLineV2Specification;
import com.tekclover.wms.api.transaction.repository.specification.InboundOrderV2Specification;
import com.tekclover.wms.api.transaction.repository.specification.OuboundOrderLineV2Specification;
import com.tekclover.wms.api.transaction.repository.specification.OuboundOrderV2Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.InboundIntegrationLog;
import com.tekclover.wms.api.transaction.model.integration.IntegrationApiResponse;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.OutboundIntegrationLog;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrderLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SOHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SOLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ShipmentOrder;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

	@Autowired
	InboundOrderRepository inboundOrderRepository;

	@Autowired
	private CycleCountHeaderRepository cycleCountHeaderRepository;

	@Autowired
	OutboundOrderRepository outboundOrderRepository;
	
	@Autowired
	OutboundOrderLinesRepository outboundOrderLinesRepository;
	
	@Autowired
	IntegrationApiResponseRepository integrationApiResponseRepository;
	
	@Autowired
	InboundIntegrationLogRepository inboundIntegrationLogRepository;

	@Autowired
	OutboundIntegrationLogRepository outboundIntegrationLogRepository;
	
	@Autowired
	WarehouseService warehouseService;

	//------------------------------------------------------------------------------------------------
	@Autowired
	private OutboundOrderV2Repository outboundOrderV2Repository;

	@Autowired
	private InboundOrderV2Repository inboundOrderV2Repository;


	@Autowired
	private InboundOrderLinesV2Repository inboundOrderLinesV2Repository;

	@Autowired
	private OutboundOrderLinesV2Repository outboundOrderLinesV2Repository;


	//------------------------------------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	public List<InboundOrder> getInboundOrders() {
		return inboundOrderRepository.findAll();
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public InboundOrder getOrderById(String orderId) {
		return inboundOrderRepository.findByRefDocumentNo (orderId);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<InboundIntegrationLog> getFailedInboundOrders() {
		return inboundIntegrationLogRepository.findByIntegrationStatus("FAILED");
	}
	
	/**
	 * 
	 * @param sdate
	 * @return
	 * @throws ParseException
	 */
	public List<InboundOrder> getOrderByDate(String sdate) throws ParseException {
		Date date1 = DateUtils.convertStringToDate_start(sdate);
		Date date2 = DateUtils.convertStringToDate_end(sdate);
		return inboundOrderRepository.findByOrderReceivedOnBetween(date1, date2);
	}
	
	/**
	 * 
	 * @param newInboundOrder
	 * @return
	 */
	public InboundOrder createInboundOrders(InboundOrder newInboundOrder) {
		InboundOrder inboundOrder = inboundOrderRepository.save(newInboundOrder);
		return inboundOrder;
	}
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public InboundOrder updateProcessedInboundOrder(String orderId) {
		InboundOrder dbInboundOrder = getOrderById (orderId);
		log.info("orderId : " + orderId);
		log.info("dbInboundOrder : " + dbInboundOrder);
		if (dbInboundOrder != null) {
			dbInboundOrder.setProcessedStatusId(10L);
			dbInboundOrder.setOrderProcessedOn(new Date());
			InboundOrder inboundOrder = inboundOrderRepository.save(dbInboundOrder);
			return inboundOrder;
		}
		return dbInboundOrder;
	}
	
	//-----------------------------V2-------------------------------------------

	/**
	 *
	 * @param orderId
	 * @return
	 */
	public InboundOrderV2 getOrderByIdV2(String orderId) {

		InboundOrderV2 dbInboundOrder = inboundOrderV2Repository.findByRefDocumentNo (orderId);

		if (dbInboundOrder != null) {
			return dbInboundOrder;
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param orderId
	 * @return
	 */
	public InboundOrderV2 updateProcessedInboundOrderV2(String orderId, Long processStatusId) throws ParseException {
		InboundOrderV2 dbInboundOrder = getOrderByIdV2 (orderId);
		log.info("orderId : " + orderId);
		log.info("dbInboundOrder : " + dbInboundOrder);
		if (dbInboundOrder != null) {
			dbInboundOrder.setProcessedStatusId(processStatusId);
			dbInboundOrder.setOrderProcessedOn(new Date());
			InboundOrderV2 inboundOrder = inboundOrderV2Repository.save(dbInboundOrder);
			return inboundOrder;
		}
		return dbInboundOrder;
	}

	/**
	 *
	 * @param newInboundOrderV2
	 * @return
	 */
	public InboundOrderV2 createInboundOrdersV2(InboundOrderV2 newInboundOrderV2) {
//		InboundOrderV2 dbInboundOrder = inboundOrderV2Repository.findByRefDocumentNoAndProcessedStatusIdOrderByOrderReceivedOn(newInboundOrderV2.getOrderId(),0L);
		InboundOrderV2 dbInboundOrder = getOrderByIdV2(newInboundOrderV2.getOrderId());
		if(dbInboundOrder != null) {
			throw new BadRequestException("Order is getting Duplicated");
		}
		InboundOrderV2 inboundOrderV2 = inboundOrderV2Repository.save(newInboundOrderV2);
		return inboundOrderV2;
	}

	//-----------------------------Outbound-------------------------------------------

	/**
	 * 
	 * @return
	 */
	public List<OutboundOrder> getOBOrders() {
		return outboundOrderRepository.findAll();
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public OutboundOrder getOBOrderById(String orderId) {
//		return outboundOrderRepository.findByOrderId(orderId);
		return outboundOrderRepository.findByRefDocumentNo (orderId);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<OutboundIntegrationLog> getFailedOutboundOrders() {
		return outboundIntegrationLogRepository.findByIntegrationStatus("FAILED");
	}

	/**
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public List<OutboundOrder> getOBOrderByDate(String startDate, String endDate) throws ParseException {
		Date date1 = DateUtils.convertStringToDate_start(startDate);
		Date date2 = DateUtils.convertStringToDate_end(endDate);
		return outboundOrderRepository.findByOrderReceivedOnBetween(date1, date2);
	}

	/**
	 *
	 * @param newOutboundOrder
	 * @return
	 */
	public OutboundOrder createOutboundOrders(OutboundOrder newOutboundOrder) {
		OutboundOrder outboundOrder = outboundOrderRepository.save(newOutboundOrder);
		return outboundOrder;
	}

	/**
	 *
	 * @param orderId
	 * @return
	 */
	public OutboundOrder updateProcessedOrder(String orderId) throws ParseException {
		OutboundOrder dbOutboundOrder = getOBOrderById(orderId);
		log.info("orderId : " + orderId);
		log.info("dbOutboundOrder : " + dbOutboundOrder);
		if (dbOutboundOrder != null) {
			dbOutboundOrder.setProcessedStatusId(10L);
			dbOutboundOrder.setOrderProcessedOn(new Date());
			OutboundOrder outboundOrder = outboundOrderRepository.save(dbOutboundOrder);
			return outboundOrder;
		}
		return dbOutboundOrder;
	}
	
	/**
	 * 
	 * @param orderId
	 */
	public void deleteObOrder (String orderId) {
		OutboundOrder existingOrder = getOBOrderById(orderId);
		if (existingOrder == null) {
			throw new BadRequestException(" Order : " + orderId + " doesn't exist.");
		}
		outboundOrderLinesRepository.deleteAll(existingOrder.getLines());
		outboundOrderRepository.delete(existingOrder);
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public ShipmentOrder pushOrder(String orderId) {
		OutboundOrder existingOrder = outboundOrderRepository.findByOrderId(orderId);
		if (existingOrder == null) {
			throw new BadRequestException(" Order : " + orderId + " doesn't exist.");
		}
		
		if (existingOrder != null) {
			if (existingOrder.getOutboundOrderTypeID() == 0L) {
				ShipmentOrder so = new ShipmentOrder();
				/*
				 * WH_ID
				 * REF_DOC_NO
				 * PARTNER_CODE
				 * PARTNER_NM
				 * REQ_DEL_DATE
				 */
				SOHeader soHeader = new SOHeader();
				soHeader.setWareHouseId(existingOrder.getWarehouseID());
				soHeader.setTransferOrderNumber(existingOrder.getRefDocumentNo());
				soHeader.setStoreID(existingOrder.getPartnerCode());
				soHeader.setStoreName(existingOrder.getPartnerName());
				soHeader.setRequiredDeliveryDate(DateUtils.date2String_MMDDYYYY(existingOrder.getRequiredDeliveryDate()));
				
				Set<OutboundOrderLine> lines = existingOrder.getLines();
				List<SOLine> soLines = new ArrayList<>();
				for (OutboundOrderLine obline : lines) {
					/*
					 * OB_LINE_NO
					 * ITM_CODE
					 * ITEM_TEXT
					 * ORD_QTY
					 * ORD_UOM
					 * REF_FIELD_1
					 */
					SOLine soLine = new SOLine();
					soLine.setLineReference(obline.getLineReference());
					soLine.setSku(obline.getItemCode());
					soLine.setSkuDescription(obline.getItemText());
					soLine.setOrderedQty(obline.getOrderedQty());
					soLine.setUom(obline.getUom());
					soLine.setOrderType(obline.getRefField1ForOrderType());
					soLines.add(soLine);
				}
				
				so.setSoHeader(soHeader);
				so.setSoLine(soLines);
				
				ShipmentOrder createdSO = warehouseService.postSO(so, true);
				if (createdSO != null) {
					return createdSO;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public List<IntegrationApiResponse> getConfirmationOrder(String orderId) {
		return integrationApiResponseRepository.findByOrderNumber (orderId);
	}

	/**
	 *
	 * @param cycleCountHeader
	 * @return
	 */
	public CycleCountHeader createCycleCountOrder(CycleCountHeader cycleCountHeader) {
		CycleCountHeader dbCycleCountHeader = cycleCountHeaderRepository.save(cycleCountHeader);
		return dbCycleCountHeader;
	}

	//===================================================================V2========================================================================

	public OutboundOrderV2 updateProcessedOrderV2(String orderId, Long processStatusId) throws ParseException {
		OutboundOrderV2 dbOutboundOrder = getOBOrderByIdV2(orderId);
		log.info("orderId : " + orderId);
		log.info("dbOutboundOrder : " + dbOutboundOrder);
		if (dbOutboundOrder != null) {
			dbOutboundOrder.setProcessedStatusId(processStatusId);
			dbOutboundOrder.setOrderProcessedOn(new Date());
			OutboundOrderV2 outboundOrder = outboundOrderV2Repository.save(dbOutboundOrder);
			return outboundOrder;
		}
		return dbOutboundOrder;
	}

	/**
	 *
	 * @param orderId
	 * @return
	 */
	public OutboundOrderV2 getOBOrderByIdV2(String orderId) {
//		return outboundOrderRepository.findByOrderId(orderId);
		OutboundOrderV2 dbOutboundOrder = outboundOrderV2Repository.findByRefDocumentNo (orderId);

		if(dbOutboundOrder!= null) {
			return dbOutboundOrder;
		} else {
			return null;
		}
		}

	public OutboundOrderV2 createOutboundOrdersV2(OutboundOrderV2 newOutboundOrder) throws ParseException {
//		OutboundOrderV2 dbOutboundOrder = outboundOrderV2Repository.findByRefDocumentNoAndProcessedStatusIdOrderByOrderReceivedOn(newOutboundOrder.getOrderId(), 0L);
		OutboundOrderV2 dbOutboundOrder = getOBOrderByIdV2(newOutboundOrder.getOrderId());
		if(dbOutboundOrder != null) {
			throw new BadRequestException("Order is getting Duplicated");
		}

		newOutboundOrder.setUpdatedOn(new Date());
		OutboundOrderV2 outboundOrder = outboundOrderV2Repository.save(newOutboundOrder);
		return outboundOrder;
	}


	//Find InboundOrder
	public List<InboundOrderV2> findInboundOrderV2(FindInboundOrderV2 findInboundOrder) throws ParseException {

		if (findInboundOrder.getFromOrderProcessedOn() != null && findInboundOrder.getToOrderProcessedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findInboundOrder.getFromOrderProcessedOn(), findInboundOrder.getToOrderProcessedOn());
			findInboundOrder.setFromOrderProcessedOn(dates[0]);
			findInboundOrder.setToOrderProcessedOn(dates[1]);
		}
		if (findInboundOrder.getFromOrderReceivedOn() != null && findInboundOrder.getToOrderReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findInboundOrder.getFromOrderReceivedOn(), findInboundOrder.getToOrderReceivedOn());
			findInboundOrder.setFromOrderReceivedOn(dates[0]);
			findInboundOrder.setToOrderReceivedOn(dates[1]);
		}


		InboundOrderV2Specification spec = new InboundOrderV2Specification(findInboundOrder);
		List<InboundOrderV2> results = inboundOrderV2Repository.findAll(spec);
		return results;

	}

	//Find InboundOrderLine
	public List<InboundOrderLinesV2> findInboundOrderLineV2(FindInboundOrderLineV2 findInboundOrderLineV2) throws ParseException {

		if (findInboundOrderLineV2.getFromExpectedDate() != null && findInboundOrderLineV2.getToExpectedDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findInboundOrderLineV2.getFromExpectedDate(), findInboundOrderLineV2.getToExpectedDate());
			findInboundOrderLineV2.setFromExpectedDate(dates[0]);
			findInboundOrderLineV2.setToExpectedDate(dates[1]);
		}
		if (findInboundOrderLineV2.getFromReceivedDate() != null && findInboundOrderLineV2.getToReceivedDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findInboundOrderLineV2.getFromReceivedDate(), findInboundOrderLineV2.getToReceivedDate());
			findInboundOrderLineV2.setFromReceivedDate(dates[0]);
			findInboundOrderLineV2.setToReceivedDate(dates[1]);
		}


		InboundOrderLineV2Specification spec = new InboundOrderLineV2Specification(findInboundOrderLineV2);
		List<InboundOrderLinesV2> results = inboundOrderLinesV2Repository.findAll(spec);
		return results;

	}

	//Find OutboundOrder
	public List<OutboundOrderV2> findOutboundOrderV2(FindOutboundOrderV2 findOutboundOrderV2) throws ParseException {

		if (findOutboundOrderV2.getFromOrderProcessedOn() != null && findOutboundOrderV2.getToOrderProcessedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findOutboundOrderV2.getFromOrderProcessedOn(), findOutboundOrderV2.getToOrderProcessedOn());
			findOutboundOrderV2.setFromOrderProcessedOn(dates[0]);
			findOutboundOrderV2.setToOrderProcessedOn(dates[1]);
		}
		if (findOutboundOrderV2.getFromOrderReceivedOn() != null && findOutboundOrderV2.getToOrderReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findOutboundOrderV2.getFromOrderReceivedOn(), findOutboundOrderV2.getToOrderReceivedOn());
			findOutboundOrderV2.setFromOrderReceivedOn(dates[0]);
			findOutboundOrderV2.setToOrderReceivedOn(dates[1]);
		}
		if (findOutboundOrderV2.getFromSalesInvoiceDate() != null && findOutboundOrderV2.getToSalesInvoiceDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(findOutboundOrderV2.getFromSalesInvoiceDate(), findOutboundOrderV2.getToSalesInvoiceDate());
			findOutboundOrderV2.setFromSalesInvoiceDate(dates[0]);
			findOutboundOrderV2.setToSalesInvoiceDate(dates[1]);
		}



		OuboundOrderV2Specification spec = new OuboundOrderV2Specification(findOutboundOrderV2);
		List<OutboundOrderV2> results = outboundOrderV2Repository.findAll(spec);
		return results;

	}

	//Find OutboundOrderLine
	public List<OutboundOrderLineV2> findOutboundOrderLineV2(FindOutboundOrderLineV2 findOutboundOrderLineV2) throws ParseException {


		OuboundOrderLineV2Specification spec = new OuboundOrderLineV2Specification(findOutboundOrderLineV2);
		List<OutboundOrderLineV2> results = outboundOrderLinesV2Repository.findAll(spec);
		return results;

	}
}