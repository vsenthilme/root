package com.tekclover.wms.api.transaction.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.validation.Valid;

import com.tekclover.wms.api.transaction.model.warehouse.Warehouse;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountHeader;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountLine;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.periodic.Periodic;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.periodic.PeriodicHeaderV1;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.periodic.PeriodicLineV1;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.perpetual.Perpetual;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.perpetual.PerpetualHeaderV1;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.perpetual.PerpetualLineV1;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.*;
import com.tekclover.wms.api.transaction.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tekclover.wms.api.transaction.config.PropertiesConfig;
import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.controller.exception.InboundOrderRequestException;
import com.tekclover.wms.api.transaction.controller.exception.OutboundOrderRequestException;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.ASN;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.ASNHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.ASNLine;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrder;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrderLines;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InterWarehouseTransferIn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InterWarehouseTransferInHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InterWarehouseTransferInLine;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.SOReturnHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.SOReturnLine;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.SaleOrderReturn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.StoreReturn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.StoreReturnHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.StoreReturnLine;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.AXApiResponse;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.InterWarehouseTransfer;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOut;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOutHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOutLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.OutboundOrderLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPO;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPOLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SOHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SOLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderLine;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ShipmentOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.confirmation.InterWarehouseShipment;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.confirmation.Shipment;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WarehouseService extends BaseService {
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	OrderService orderService;

	@Autowired
	WarehouseRepository warehouseRepository;
	
	/**
	 * 
	 * @return
	 */
	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	/**
	 *
	 * @param asn
	 * @return
	 */
	public InboundOrder postWarehouseASN (ASN asn) {
		log.info("ASNHeader received from External: " + asn);
		InboundOrder savedAsnHeader = saveASN (asn);							// Without Mongo
		log.info("savedAsnHeader: " + savedAsnHeader);
		return savedAsnHeader;
	} 

	/**
	 * 
	 * @param storeReturn
	 * @return
	 */
	public InboundOrder postStoreReturn(StoreReturn storeReturn) {
		log.info("StoreReturnHeader received from External: " + storeReturn);
		InboundOrder savedStoreReturn = saveStoreReturn (storeReturn);
		log.info("savedStoreReturn: " + savedStoreReturn);
		return savedStoreReturn;
	}
	
	/**
	 * 
	 * @param soReturn
	 * @return
	 */
	public InboundOrder postSOReturn(SaleOrderReturn soReturn) {
		log.info("StoreReturnHeader received from External: " + soReturn);
		InboundOrder savedSOReturn = saveSOReturn (soReturn);
		log.info("soReturnHeader: " + savedSOReturn);
		return savedSOReturn;
	}

	/**
	 * 
	 * @param interWarehouseTransferIn
	 * @return
	 */
	public InboundOrder postInterWarehouseTransfer(InterWarehouseTransferIn interWarehouseTransferIn) {
		log.info("InterWarehouseTransferHeader received from External: " + interWarehouseTransferIn);
		InboundOrder savedIWHReturn = saveInterWarehouseTransfer (interWarehouseTransferIn);
		log.info("interWarehouseTransferHeader: " + savedIWHReturn);
		return savedIWHReturn;
	}

	/*-------------------------------------OUTBOUND---------------------------------------------------*/
	/**
	 * ShipmentOrder
	 * @param shipmenOrder
	 * @return
	 */
	public ShipmentOrder postSO( ShipmentOrder shipmenOrder, boolean isRerun) {
		log.info("ShipmenOrder received from External: " + shipmenOrder);
		OutboundOrder savedSoHeader = saveSO (shipmenOrder, isRerun);						// Without Nongo
		log.info("savedSoHeader: " + savedSoHeader.getRefDocumentNo());
		return shipmenOrder;
	}
	
	/**
	 * 
	 * @param salesOrder
	 * @return
	 */
	public SalesOrder postSalesOrder(SalesOrder salesOrder) {
		log.info("SalesOrderHeader received from External: " + salesOrder);
		OutboundOrder savedSoHeader = saveSalesOrder (salesOrder);								// Without Nongo
		log.info("salesOrderHeader: " + savedSoHeader);
		return salesOrder;
	}

	/**
	 * 
	 * @param returnPO
	 * @return
	 */
	public ReturnPO postReturnPO( ReturnPO returnPO) {
		log.info("ReturnPOHeader received from External: " + returnPO);
		OutboundOrder savedReturnPOHeader = saveReturnPO (returnPO);					// Without Nongo
		log.info("savedReturnPOHeader: " + savedReturnPOHeader);
		return returnPO;
	}

	/**
	 * 
	 * @param interWarehouseTransfer
	 * @return
	 */
	public InterWarehouseTransferOut postInterWarehouseTransferOutbound(InterWarehouseTransferOut interWarehouseTransfer) {
		log.info("InterWarehouseTransferHeader received from External: " + interWarehouseTransfer);
		OutboundOrder savedInterWarehouseTransferHeader = saveIWHTransfer (interWarehouseTransfer);													// Without Nongo
		log.info("savedInterWarehouseTransferHeader: " + savedInterWarehouseTransferHeader);
		return interWarehouseTransfer;
	}
	
	/*----------------------------INBOUND-CONFIRMATION-POST---------------------------------------------*/
	// ASN 
	public AXApiResponse postASNConfirmation (com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.ASN asn, 
			String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceAsnUrl());
		HttpEntity<?> entity = new HttpEntity<>(asn, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// StoreReturn
	public AXApiResponse postStoreReturnConfirmation (
			com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.StoreReturn storeReturn,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceStoreReturnUrl());
		HttpEntity<?> entity = new HttpEntity<>(storeReturn, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	// Sale Order Returns
	public AXApiResponse postSOReturnConfirmation (
			com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.SOReturn soReturn,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceSOReturnUrl());
		HttpEntity<?> entity = new HttpEntity<>(soReturn, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/**
	 *
	 * @param iwhTransfer
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postInterWarehouseTransferConfirmation(InterWarehouseTransfer iwhTransfer,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceInterwareHouseUrl());
		HttpEntity<?> entity = new HttpEntity<>(iwhTransfer, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	//--------------------------------OUTBOUND-----------------------------------------------------------------------

	/**
	 * ShipmentConfirmation API
	 * @param shipment
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postShipmentConfirmation (Shipment shipment, String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceShipmentUrl());
		HttpEntity<?> entity = new HttpEntity<>(shipment, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/**
	 * InterWarehouseShipmentConfirmation
	 * @param iwhShipment
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postInterWarehouseShipmentConfirmation(InterWarehouseShipment iwhShipment,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceIWHouseShipmentUrl());
		HttpEntity<?> entity = new HttpEntity<>(iwhShipment, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/**
	 * SaleOrderConfirmation
	 * @param salesOrder
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postSaleOrderConfirmation(
			com.tekclover.wms.api.transaction.model.warehouse.outbound.confirmation.SalesOrder salesOrder,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceSalesOrderUrl());
		HttpEntity<?> entity = new HttpEntity<>(salesOrder, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/**
	 * ReturnPO
	 * @param returnPO
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postReturnPOConfirmation(
			com.tekclover.wms.api.transaction.model.warehouse.outbound.confirmation.ReturnPO returnPO,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);
		
		UriComponentsBuilder builder = 
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceReturnPOUrl());
		HttpEntity<?> entity = new HttpEntity<>(returnPO, headers);
		ResponseEntity<AXApiResponse> result = 
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}
	
	/**
	 * 
	 * @param wareHouseId
	 * @return 
	 */
	private boolean validateWarehouseId(String wareHouseId) {
		log.info("wareHouseId: " + wareHouseId);
		if (wareHouseId.equalsIgnoreCase(WAREHOUSE_ID_110) || wareHouseId.equalsIgnoreCase(WAREHOUSE_ID_111)) {
			log.info("wareHouseId:------------> " + wareHouseId);
			return true;
		} else {
			throw new BadRequestException("Warehouse Id must be either 110 or 111");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized String getUUID() {
		String uniqueID = UUID.randomUUID().toString();
		return uniqueID;
	}
	
	//================================================Moongo=Removed================================================================================
	//------------------------------------------------INBOUND-ORDERS--------------------------------------------------------------------------------
	// POST ASNHeader
	private InboundOrder saveASN (ASN asn) {
		try {
			ASNHeader asnHeader = asn.getAsnHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (asnHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			InboundOrder dbApiHeader = orderService.getOrderById(asnHeader.getAsnNumber());
			if (dbApiHeader != null) {
				throw new InboundOrderRequestException("ASN is already posted and it can't be duplicated.");
			}
						
			List<ASNLine> asnLines = asn.getAsnLine();
			InboundOrder apiHeader = new InboundOrder();
			apiHeader.setOrderId(asnHeader.getAsnNumber());
			apiHeader.setRefDocumentNo(asnHeader.getAsnNumber());
			apiHeader.setRefDocumentType("ASN");
			apiHeader.setWarehouseID(asnHeader.getWareHouseId());
			apiHeader.setInboundOrderTypeId(1L);
			apiHeader.setOrderReceivedOn(new Date());
			
			Set<InboundOrderLines> orderLines = new HashSet<>();
			for (ASNLine asnLine : asnLines) {
				InboundOrderLines apiLine = new InboundOrderLines();
				apiLine.setLineReference(asnLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(asnLine.getSku());							// ITM_CODE
				apiLine.setItemText(asnLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setInvoiceNumber(asnLine.getInvoiceNumber());			// INV_NO
				apiLine.setContainerNumber(asnLine.getContainerNumber());		// CONT_NO
				apiLine.setSupplierCode(asnLine.getSupplierCode());				// PARTNER_CODE
				apiLine.setSupplierPartNumber(asnLine.getSupplierPartNumber()); // PARTNER_ITM_CODE
				apiLine.setManufacturerName(asnLine.getManufacturerName());		// BRND_NM
				apiLine.setManufacturerPartNo(asnLine.getManufacturerPartNo());	// MFR_PART
				apiLine.setOrderId(apiHeader.getOrderId());
				
				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(asnLine.getExpectedDate());
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new BadRequestException("Date format should be MM-dd-yyyy");
				}
				
				apiLine.setOrderedQty(asnLine.getExpectedQty());				// ORD_QTY
				apiLine.setUom(asnLine.getUom());								// ORD_UOM
				apiLine.setItemCaseQty(asnLine.getPackQty());					// ITM_CASE_QTY
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			if (asn.getAsnLine() != null && !asn.getAsnLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("ASN Order Success : " + createdOrder);
				return createdOrder;
			} else if (asn.getAsnLine() == null || asn.getAsnLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("ASN Order Failed : " + createdOrder);
				throw new BadRequestException("ASN Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	// STORE RETURN
	private InboundOrder saveStoreReturn (StoreReturn storeReturn) {
		try {
			StoreReturnHeader storeReturnHeader = storeReturn.getStoreReturnHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (storeReturnHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			InboundOrder dbApiHeader = orderService.getOrderById(storeReturnHeader.getTransferOrderNumber());
			if (dbApiHeader != null) {
				throw new InboundOrderRequestException("StoreReturn is already posted and it can't be duplicated.");
			}
						
			List<StoreReturnLine> storeReturnLines = storeReturn.getStoreReturnLine();
			InboundOrder apiHeader = new InboundOrder();
			apiHeader.setOrderId(storeReturnHeader.getTransferOrderNumber());
			apiHeader.setRefDocumentNo(storeReturnHeader.getTransferOrderNumber());
			apiHeader.setWarehouseID(storeReturnHeader.getWareHouseId());
			apiHeader.setRefDocumentType("RETURN");			
			apiHeader.setInboundOrderTypeId(2L);
			apiHeader.setOrderReceivedOn(new Date());
			
			Set<InboundOrderLines> orderLines = new HashSet<>();
			for (StoreReturnLine storeReturnLine : storeReturnLines) {
				InboundOrderLines apiLine = new InboundOrderLines();
				apiLine.setLineReference(storeReturnLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(storeReturnLine.getSku());							// ITM_CODE
				apiLine.setItemText(storeReturnLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setInvoiceNumber(storeReturnLine.getInvoiceNumber());			// INV_NO
				apiLine.setContainerNumber(storeReturnLine.getContainerNumber());		// CONT_NO
				apiLine.setSupplierCode(storeReturnLine.getStoreID());					// PARTNER_CODE
				apiLine.setSupplierPartNumber(storeReturnLine.getSupplierPartNumber()); // PARTNER_ITM_CODE
				apiLine.setManufacturerName(storeReturnLine.getManufacturerName());		// BRND_NM
				apiLine.setManufacturerPartNo(storeReturnLine.getManufacturerPartNo());	// MFR_PART
				apiLine.setOrderId(apiHeader.getOrderId());
				
				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(storeReturnLine.getExpectedDate());
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}
				
				apiLine.setOrderedQty(storeReturnLine.getExpectedQty());				// ORD_QTY
				apiLine.setUom(storeReturnLine.getUom());								// ORD_UOM
				apiLine.setItemCaseQty(storeReturnLine.getPackQty());					// ITM_CASE_QTY
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (storeReturn.getStoreReturnLine() != null && !storeReturn.getStoreReturnLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("StoreReturn Order Success: " + createdOrder);
				return createdOrder;
			} else if (storeReturn.getStoreReturnLine() == null || storeReturn.getStoreReturnLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("StoreReturn Order Failed : " + createdOrder);
				throw new BadRequestException("StoreReturn Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	// SOReturn
	private InboundOrder saveSOReturn (SaleOrderReturn soReturn) {
		try {
			SOReturnHeader soReturnHeader = soReturn.getSoReturnHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (soReturnHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			InboundOrder dbApiHeader = orderService.getOrderById(soReturnHeader.getReturnOrderReference());
			if (dbApiHeader != null) {
				throw new InboundOrderRequestException("Return Order Reference is already posted and it can't be duplicated.");
			}
						
			List<SOReturnLine> storeReturnLines = soReturn.getSoReturnLine();
			InboundOrder apiHeader = new InboundOrder();
			apiHeader.setOrderId(soReturnHeader.getReturnOrderReference());
			apiHeader.setRefDocumentNo(soReturnHeader.getReturnOrderReference());
			apiHeader.setWarehouseID(soReturnHeader.getWareHouseId());
			apiHeader.setRefDocumentType("RETURN");	
			apiHeader.setInboundOrderTypeId(4L);										// Hardcoded Value 4
			apiHeader.setOrderReceivedOn(new Date());
			
			Set<InboundOrderLines> orderLines = new HashSet<>();
			for (SOReturnLine soReturnLine : storeReturnLines) {
				InboundOrderLines apiLine = new InboundOrderLines();
				apiLine.setLineReference(soReturnLine.getLineReference()); 				// IB_LINE_NO
				apiLine.setItemCode(soReturnLine.getSku());								// ITM_CODE
				apiLine.setItemText(soReturnLine.getSkuDescription()); 					// ITEM_TEXT
				apiLine.setInvoiceNumber(soReturnLine.getInvoiceNumber());				// INV_NO
				apiLine.setContainerNumber(soReturnLine.getContainerNumber());			// CONT_NO
				apiLine.setSupplierCode(soReturnLine.getStoreID());						// PARTNER_CODE
				apiLine.setSupplierPartNumber(soReturnLine.getSupplierPartNumber());	// PARTNER_ITM_CODE
				apiLine.setManufacturerName(soReturnLine.getManufacturerName());		// BRND_NM
				apiLine.setManufacturerPartNo(soReturnLine.getManufacturerPartNo());	// MFR_PART
				apiLine.setOrderId(apiHeader.getOrderId());				
				
				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(soReturnLine.getExpectedDate());
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}
				
				apiLine.setOrderedQty(soReturnLine.getExpectedQty());					// ORD_QTY
				apiLine.setUom(soReturnLine.getUom());									// ORD_UOM
				apiLine.setItemCaseQty(soReturnLine.getPackQty());						// ITM_CASE_QTY
				apiLine.setSalesOrderReference(soReturnLine.getSalesOrderReference());	// REF_FIELD_4
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (soReturn.getSoReturnLine() != null && !soReturn.getSoReturnLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("Return Order Reference Order Success: " + createdOrder);
				return createdOrder;
			} else if (soReturn.getSoReturnLine() == null || soReturn.getSoReturnLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("Return Order Reference Order Failed : " + createdOrder);
				throw new BadRequestException("Return Order Reference Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	// InterWarehouseTransfer
	private InboundOrder saveInterWarehouseTransfer (InterWarehouseTransferIn interWarehouseTransferIn) {
		try {
			InterWarehouseTransferInHeader interWarehouseTransferInHeader = interWarehouseTransferIn.getInterWarehouseTransferInHeader();
			// Warehouse ID Validation
			validateWarehouseId (interWarehouseTransferInHeader.getToWhsId());
			
			// Checking for duplicate RefDocNumber
			InboundOrder dbApiHeader = orderService.getOrderById(interWarehouseTransferInHeader.getTransferOrderNumber());
			if (dbApiHeader != null) {
				throw new InboundOrderRequestException("InterWarehouseTransfer is already posted and it can't be duplicated.");
			}
						
			List<InterWarehouseTransferInLine> interWarehouseTransferInLines = interWarehouseTransferIn.getInterWarehouseTransferInLine();
			InboundOrder apiHeader = new InboundOrder();
			apiHeader.setOrderId(interWarehouseTransferInHeader.getTransferOrderNumber());
			apiHeader.setRefDocumentNo(interWarehouseTransferInHeader.getTransferOrderNumber());
			apiHeader.setWarehouseID(interWarehouseTransferInHeader.getToWhsId());
			apiHeader.setRefDocumentType("WH2WH");				// Hardcoded Value "WH to WH"
			apiHeader.setInboundOrderTypeId(3L);				// Hardcoded Value 3
			apiHeader.setOrderReceivedOn(new Date());
			
			Set<InboundOrderLines> orderLines = new HashSet<>();
			for (InterWarehouseTransferInLine iwhTransferLine : interWarehouseTransferInLines) {
				InboundOrderLines apiLine = new InboundOrderLines();
				apiLine.setLineReference(iwhTransferLine.getLineReference()); 				// IB_LINE_NO
				apiLine.setItemCode(iwhTransferLine.getSku());								// ITM_CODE
				apiLine.setItemText(iwhTransferLine.getSkuDescription()); 					// ITEM_TEXT
				apiLine.setInvoiceNumber(iwhTransferLine.getInvoiceNumber());				// INV_NO
				apiLine.setContainerNumber(iwhTransferLine.getContainerNumber());			// CONT_NO
				apiLine.setSupplierCode(iwhTransferLine.getFromWhsId());					// PARTNER_CODE
				apiLine.setSupplierPartNumber(iwhTransferLine.getSupplierPartNumber());		// PARTNER_ITM_CODE
				apiLine.setManufacturerName(iwhTransferLine.getManufacturerName());			// BRND_NM
				apiLine.setManufacturerPartNo(iwhTransferLine.getManufacturerPartNo());		// MFR_PART
				apiLine.setOrderId(apiHeader.getOrderId());
				
				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(iwhTransferLine.getExpectedDate());
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}
				
				apiLine.setOrderedQty(iwhTransferLine.getExpectedQty());					// ORD_QTY
				apiLine.setUom(iwhTransferLine.getUom());									// ORD_UOM
				apiLine.setItemCaseQty(iwhTransferLine.getPackQty());						// ITM_CASE_QTY
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			if (interWarehouseTransferIn.getInterWarehouseTransferInLine() != null && 
					!interWarehouseTransferIn.getInterWarehouseTransferInLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("InterWarehouseTransfer Order Success: " + createdOrder);
				return createdOrder;
			} else if (interWarehouseTransferIn.getInterWarehouseTransferInLine() == null || 
					interWarehouseTransferIn.getInterWarehouseTransferInLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
				log.info("InterWarehouseTransfer Order Failed : " + createdOrder);
				throw new BadRequestException("InterWarehouseTransfer Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~OUTBOUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	// POST SOHeader
	private OutboundOrder saveSO (ShipmentOrder shipmenOrder, boolean isRerun) {
		try {
			SOHeader soHeader = shipmenOrder.getSoHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (soHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			OutboundOrder obOrder = orderService.getOBOrderById(soHeader.getTransferOrderNumber());
			
			if (obOrder != null) {
				throw new OutboundOrderRequestException("TransferOrderNumber is getting duplicated. This order already exists in the System.");
			}
						
			List<SOLine> soLines = shipmenOrder.getSoLine();
			
			OutboundOrder apiHeader = new OutboundOrder();
			apiHeader.setOrderId(soHeader.getTransferOrderNumber());
			apiHeader.setWarehouseID(soHeader.getWareHouseId());
			apiHeader.setPartnerCode(soHeader.getStoreID());
			apiHeader.setPartnerName(soHeader.getStoreName());
			apiHeader.setRefDocumentNo(soHeader.getTransferOrderNumber());
			apiHeader.setOutboundOrderTypeID(0L);
			apiHeader.setRefDocumentType("SO");						// Hardcoded value "SO"
			apiHeader.setOrderProcessedOn(new Date());
			apiHeader.setOrderReceivedOn(new Date());
			
			try {
				Date reqDelDate = DateUtils.convertStringToDate(soHeader.getRequiredDeliveryDate());
				apiHeader.setRequiredDeliveryDate(reqDelDate);
			} catch (Exception e) {
				throw new OutboundOrderRequestException("Date format should be MM-dd-yyyy");
			}
			
			Set<OutboundOrderLine> orderLines = new HashSet<>();
			for (SOLine soLine : soLines) {
				OutboundOrderLine apiLine = new OutboundOrderLine();
				apiLine.setLineReference(soLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(soLine.getSku());							// ITM_CODE
				apiLine.setItemText(soLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setOrderedQty(soLine.getOrderedQty());					// ORD_QTY
				apiLine.setUom(soLine.getUom()); 								// ORD_UOM
				apiLine.setRefField1ForOrderType(soLine.getOrderType());		// ORDER_TYPE
				apiLine.setOrderId(apiHeader.getOrderId());
				orderLines.add(apiLine);
			}
			
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (shipmenOrder.getSoLine() != null && !shipmenOrder.getSoLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("ShipmentOrder Order Success: " + createdOrder);
				return apiHeader;
			} else if (shipmenOrder.getSoLine() == null || shipmenOrder.getSoLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("ShipmentOrder Order Failed: " + createdOrder);
				throw new BadRequestException("ShipmentOrder Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	// POST 
	private OutboundOrder saveSalesOrder(@Valid SalesOrder salesOrder) {
		try {
			SalesOrderHeader salesOrderHeader = salesOrder.getSalesOrderHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (salesOrderHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			OutboundOrder obOrder = orderService.getOBOrderById(salesOrderHeader.getSalesOrderNumber());
			
			if (obOrder != null) {
				throw new OutboundOrderRequestException("SalesOrderNumber is already posted and it can't be duplicated.");
			}
						
			List<SalesOrderLine> salesOrderLines = salesOrder.getSalesOrderLine();
			OutboundOrder apiHeader = new OutboundOrder();
			apiHeader.setOrderId(salesOrderHeader.getSalesOrderNumber());
			apiHeader.setWarehouseID(salesOrderHeader.getWareHouseId());
			apiHeader.setPartnerCode(salesOrderHeader.getStoreID());
			apiHeader.setPartnerName(salesOrderHeader.getStoreName());
			apiHeader.setRefDocumentNo(salesOrderHeader.getSalesOrderNumber());
			apiHeader.setOutboundOrderTypeID(3L);							// Hardcoded Value "3"
			apiHeader.setRefDocumentType("SaleOrder");						// Hardcoded value "SaleOrder"
			apiHeader.setOrderProcessedOn(new Date());
			apiHeader.setOrderReceivedOn(new Date());
			
			try {
				Date reqDelDate = DateUtils.convertStringToDate(salesOrderHeader.getRequiredDeliveryDate());
				apiHeader.setRequiredDeliveryDate(reqDelDate);
			} catch (Exception e) {
				throw new OutboundOrderRequestException("Date format should be MM-dd-yyyy");
			}
			
			Set<OutboundOrderLine> orderLines = new HashSet<>();
			for (SalesOrderLine soLine : salesOrderLines) {
				OutboundOrderLine apiLine = new OutboundOrderLine();
				apiLine.setLineReference(soLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(soLine.getSku());							// ITM_CODE
				apiLine.setItemText(soLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setOrderedQty(soLine.getOrderedQty());					// ORD_QTY
				apiLine.setUom(soLine.getUom()); 								// ORD_UOM
				apiLine.setRefField1ForOrderType(soLine.getOrderType());		// ORDER_TYPE
				apiLine.setOrderId(apiHeader.getOrderId());
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (salesOrder.getSalesOrderLine() != null && !salesOrder.getSalesOrderLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("SalesOrder Order Success: " + createdOrder);
				return apiHeader;
			} else if (salesOrder.getSalesOrderLine() == null || salesOrder.getSalesOrderLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("SalesOrder Order Failed: " + createdOrder);
				throw new BadRequestException("SalesOrder Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 
	 * @param returnPO
	 * @return
	 */
	private OutboundOrder saveReturnPO (ReturnPO returnPO) {
		try {
			ReturnPOHeader returnPOHeader = returnPO.getReturnPOHeader();
			
			// Warehouse ID Validation
			validateWarehouseId (returnPOHeader.getWareHouseId());
			
			// Checking for duplicate RefDocNumber
			OutboundOrder obOrder = orderService.getOBOrderById(returnPOHeader.getPoNumber());
			
			if (obOrder != null) {
				throw new OutboundOrderRequestException("PONumber is already posted and it can't be duplicated.");
			}
						
			List<ReturnPOLine> returnPOLines = returnPO.getReturnPOLine();
			
			// Mongo Primary Key
			OutboundOrder apiHeader = new OutboundOrder();
			apiHeader.setOrderId(returnPOHeader.getPoNumber());
			apiHeader.setWarehouseID(returnPOHeader.getWareHouseId());
			apiHeader.setPartnerCode(returnPOHeader.getStoreID());
			apiHeader.setPartnerName(returnPOHeader.getStoreName());
			apiHeader.setRefDocumentNo(returnPOHeader.getPoNumber());
			apiHeader.setOutboundOrderTypeID(2L);							// Hardcoded Value "2"
			apiHeader.setRefDocumentType("RETURNPO");						// Hardcoded value "RETURNPO"
			apiHeader.setOrderProcessedOn(new Date());
			apiHeader.setOrderReceivedOn(new Date());
			
			try {
				Date reqDelDate = DateUtils.convertStringToDate(returnPOHeader.getRequiredDeliveryDate());
				apiHeader.setRequiredDeliveryDate(reqDelDate);
			} catch (Exception e) {
				throw new OutboundOrderRequestException("Date format should be MM-dd-yyyy");
			}
			
			Set<OutboundOrderLine> orderLines = new HashSet<>();
			for (ReturnPOLine rpoLine : returnPOLines) {
				OutboundOrderLine apiLine = new OutboundOrderLine();
				apiLine.setLineReference(rpoLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(rpoLine.getSku());							// ITM_CODE
				apiLine.setItemText(rpoLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setOrderedQty(rpoLine.getReturnQty());					// ORD_QTY
				apiLine.setUom(rpoLine.getUom()); 								// ORD_UOM
				apiLine.setRefField1ForOrderType(rpoLine.getOrderType());		// ORDER_TYPE
				apiLine.setOrderId(apiHeader.getOrderId());
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (returnPO.getReturnPOLine() != null && !returnPO.getReturnPOLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("ReturnPO Order Success: " + createdOrder);
				return apiHeader;
			} else if (returnPO.getReturnPOLine() == null || returnPO.getReturnPOLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("ReturnPO Order Failed: " + createdOrder);
				throw new BadRequestException("ReturnPO Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 
	 * @param interWarehouseTransfer
	 * @return
	 */
	private OutboundOrder saveIWHTransfer (InterWarehouseTransferOut interWarehouseTransfer) {
		try {
			InterWarehouseTransferOutHeader interWarehouseTransferOutHeader = 
					interWarehouseTransfer.getInterWarehouseTransferOutHeader();
			// Warehouse ID Validation
			validateWarehouseId (interWarehouseTransferOutHeader.getFromWhsID());
			
			// Checking for duplicate RefDocNumber
			OutboundOrder obOrder = orderService.getOBOrderById(interWarehouseTransferOutHeader.getTransferOrderNumber());
			
			if (obOrder != null) {
				throw new OutboundOrderRequestException("TransferOrderNumber is already posted and it can't be duplicated.");
			}
						
			List<InterWarehouseTransferOutLine> interWarehouseTransferOutLines = 
					interWarehouseTransfer.getInterWarehouseTransferOutLine();
			
			// Mongo Primary Key
			OutboundOrder apiHeader = new OutboundOrder();
			apiHeader.setOrderId(interWarehouseTransferOutHeader.getTransferOrderNumber());
			apiHeader.setWarehouseID(interWarehouseTransferOutHeader.getFromWhsID());
			apiHeader.setPartnerCode(interWarehouseTransferOutHeader.getToWhsID());
			apiHeader.setPartnerName(interWarehouseTransferOutHeader.getStoreName());
			apiHeader.setRefDocumentNo(interWarehouseTransferOutHeader.getTransferOrderNumber());
			apiHeader.setOutboundOrderTypeID(1L);							// Hardcoded Value "1"
			apiHeader.setRefDocumentType("WH2WH");							// Hardcoded value "WH to WH"
			apiHeader.setOrderProcessedOn(new Date());
			apiHeader.setOrderReceivedOn(new Date());
			
			try {
				Date reqDelDate = DateUtils.convertStringToDate(interWarehouseTransferOutHeader.getRequiredDeliveryDate());
				apiHeader.setRequiredDeliveryDate(reqDelDate);
			} catch (Exception e) {
				throw new OutboundOrderRequestException("Date format should be MM-dd-yyyy");
			}
			
			Set<OutboundOrderLine> orderLines = new HashSet<>();
			for (InterWarehouseTransferOutLine iwhTransferLine : interWarehouseTransferOutLines) {
				OutboundOrderLine apiLine = new OutboundOrderLine();
				apiLine.setLineReference(iwhTransferLine.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(iwhTransferLine.getSku());							// ITM_CODE
				apiLine.setItemText(iwhTransferLine.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setOrderedQty(iwhTransferLine.getOrderedQty());					// ORD_QTY
				apiLine.setUom(iwhTransferLine.getUom()); 								// ORD_UOM
				apiLine.setRefField1ForOrderType(iwhTransferLine.getOrderType());		// ORDER_TYPE
				apiLine.setOrderId(apiHeader.getOrderId());
				orderLines.add(apiLine);
			}
			apiHeader.setLines(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			
			if (interWarehouseTransfer.getInterWarehouseTransferOutLine() != null && 
					!interWarehouseTransfer.getInterWarehouseTransferOutLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("InterWarehouseTransferOut Order Success: " + createdOrder);
				return apiHeader;
			} else if (interWarehouseTransfer.getInterWarehouseTransferOutLine() == null || 
					interWarehouseTransfer.getInterWarehouseTransferOutLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				OutboundOrder createdOrder = orderService.createOutboundOrders(apiHeader);
				log.info("InterWarehouseTransferOut Order Failed: " + createdOrder);
				throw new BadRequestException("InterWarehouseTransferOut Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

//=============================================================================================================================
	/**
	 *
	 * @param asnv2
	 * @return
	 */
	public InboundOrderV2 postWarehouseASNV2 (ASNV2 asnv2) {
		log.info("ASNV2Header received from External: " + asnv2);
		InboundOrderV2 savedAsnV2Header = saveASNV2 (asnv2);
		log.info("savedAsnV2Header: " + savedAsnV2Header);
		return savedAsnV2Header;
	}

	// POST ASNV2Header
	private InboundOrderV2 saveASNV2 (ASNV2 asnv2) {
		try {
			ASNHeaderV2 asnV2Header = asnv2.getAsnHeader();
			List<ASNLineV2> asnLineV2s = asnv2.getAsnLine();
			InboundOrderV2 apiHeader = new InboundOrderV2();

			apiHeader.setOrderId(asnV2Header.getAsnNumber());
			apiHeader.setCompanyCode(asnV2Header.getCompanyCode());
			apiHeader.setBranchCode(asnV2Header.getBranchCode());
			apiHeader.setRefDocumentNo(asnV2Header.getAsnNumber());
			apiHeader.setRefDocumentType("ASNV2");
			apiHeader.setInboundOrderTypeId(1L);
			apiHeader.setOrderReceivedOn(new Date());

			// Get Warehouse
			Optional<Warehouse> dbWarehouse =
					warehouseRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
							asnV2Header.getCompanyCode(),
							asnV2Header.getBranchCode(),
							"EN",
							0L
					);
			log.info("dbWarehouse : " + dbWarehouse);
			apiHeader.setWarehouseID(dbWarehouse.get().getWarehouseId());

			Set<InboundOrderLinesV2> orderLines = new HashSet<>();
			for (ASNLineV2 asnLineV2 : asnLineV2s) {
				InboundOrderLinesV2 apiLine = new InboundOrderLinesV2();
				apiLine.setLineReference(asnLineV2.getLineReference()); 			// IB_LINE_NO
				apiLine.setItemCode(asnLineV2.getSku());							// ITM_CODE
				apiLine.setItemText(asnLineV2.getSkuDescription()); 				// ITEM_TEXT
				apiLine.setContainerNumber(asnLineV2.getContainerNumber());			// CONT_NO
				apiLine.setSupplierCode(asnLineV2.getSupplierCode());				// PARTNER_CODE
				apiLine.setSupplierPartNumber(asnLineV2.getSupplierPartNumber());  // PARTNER_ITM_CODE
				apiLine.setManufacturerName(asnLineV2.getManufacturerName());		// BRND_NM
				apiLine.setManufacturerCode(asnLineV2.getManufacturerCode());
				apiLine.setOrigin(asnLineV2.getOrigin());
				apiLine.setExpectedQty(asnLineV2.getExpectedQty());
				apiLine.setSupplierName(asnLineV2.getSupplierName());
				apiLine.setBrand(asnLineV2.getBrand());
				apiLine.setOrderId(apiHeader.getOrderId());


				// EA_DATE
				try {
					ZoneId defaultZoneId = ZoneId.systemDefault();
					String sdate = asnLineV2.getExpectedDate();
					String firstHalf = sdate.substring(0,sdate.lastIndexOf("/"));
					String secondHalf = sdate.substring(sdate.lastIndexOf("/")+1);
					secondHalf = "/20" + secondHalf;
					sdate = firstHalf + secondHalf;
					log.info("sdate--------> : " + sdate);

					LocalDate localDate = DateUtils.dateConv2(sdate);
					log.info("localDate--------> : " + localDate);
					Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
					apiLine.setExpectedDate(date);
				} catch (Exception e) {
					e.printStackTrace();
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}

				apiLine.setOrderedQty(asnLineV2.getExpectedQty());				// ORD_QTY
				apiLine.setUom(asnLineV2.getUom());								// ORD_UOM
				apiLine.setPackQty(asnLineV2.getPackQty());					// ITM_CASE_QTY
				orderLines.add(apiLine);
			}
			apiHeader.setLine(orderLines);
			apiHeader.setOrderProcessedOn(new Date());
			if (asnv2.getAsnLine() != null && !asnv2.getAsnLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrder = orderService.createInboundOrdersV2(apiHeader);
				log.info("ASNV2 Order Success : " + createdOrder);
				return createdOrder;
			} else if (asnv2.getAsnLine() == null || asnv2.getAsnLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrder = orderService.createInboundOrdersV2(apiHeader);
				log.info("ASNV2 Order Failed : " + createdOrder);
				throw new BadRequestException("ASNV2 Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 *
	 * @param soReturnV2
	 * @return
	 */
	public InboundOrderV2 postSOReturnV2(SaleOrderReturnV2 soReturnV2) {
		log.info("StoreReturnHeader received from External: " + soReturnV2);
		InboundOrderV2 savedSOReturn = saveSOReturnV2 (soReturnV2);
		log.info("soReturnHeader: " + savedSOReturn);
		return savedSOReturn;
	}

	// SOReturnV2
	private InboundOrderV2 saveSOReturnV2 (SaleOrderReturnV2 soReturnV2) {
		try {
			SOReturnHeaderV2 soReturnHeaderV2 = soReturnV2.getSoReturnHeader();
			List<SOReturnLineV2> salesOrderReturnLinesV2 = soReturnV2.getSoReturnLine();
			InboundOrderV2 apiHeader = new InboundOrderV2();
			apiHeader.setTransferOrderNumber(soReturnHeaderV2.getTransferOrderNumber());
			apiHeader.setRefDocumentNo(soReturnHeaderV2.getTransferOrderNumber());
			apiHeader.setBranchCode(soReturnHeaderV2.getBranchCode());
			apiHeader.setOrderId(soReturnHeaderV2.getTransferOrderNumber());
			apiHeader.setCompanyCode(soReturnHeaderV2.getCompanyCode());
			apiHeader.setRefDocumentType("SORETURNV2");
			apiHeader.setInboundOrderTypeId(1L);                                        // Hardcoded Value 4
			apiHeader.setOrderReceivedOn(new Date());

			// Get Warehouse
			Optional<com.tekclover.wms.api.transaction.model.warehouse.Warehouse> dbWarehouse =
					warehouseRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
							soReturnHeaderV2.getCompanyCode(),
							soReturnHeaderV2.getBranchCode(),
							"EN",
							0L
					);
			log.info("dbWarehouse : " + dbWarehouse);
			apiHeader.setWarehouseID(dbWarehouse.get().getWarehouseId());

			Set<InboundOrderLinesV2> orderLinesV2 = new HashSet<>();
			for (SOReturnLineV2 soReturnLineV2 : salesOrderReturnLinesV2) {
				InboundOrderLinesV2 apiLine = new InboundOrderLinesV2();
				apiLine.setExpectedQty(soReturnLineV2.getExpectedQty());
				apiLine.setInvoiceNumber(soReturnLineV2.getInvoiceNumber());                // INV_NO
				apiLine.setSalesOrderReference(soReturnLineV2.getSalesOrderReference());
				apiLine.setLineReference(soReturnLineV2.getLineReference());                 // IB_LINE_NO
				apiLine.setItemCode(soReturnLineV2.getSku());                                // ITM_CODE
				apiLine.setItemText(soReturnLineV2.getSkuDescription());                     // ITEM_TEXT
				apiLine.setManufacturerName(soReturnLineV2.getManufacturerName());        // BRND_NM
				apiLine.setStoreID(soReturnLineV2.getStoreID());
				apiLine.setSupplierPartNumber(soReturnLineV2.getSupplierPartNumber());
				apiLine.setUom(soReturnLineV2.getUom());
				apiLine.setPackQty(soReturnLineV2.getPackQty());
				apiLine.setOrigin(soReturnLineV2.getOrigin());
				apiLine.setOrderId(apiHeader.getOrderId());
				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(soReturnLineV2.getExpectedDate());
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new BadRequestException("Date format should be MM-dd-yyyy");
				}

				apiLine.setManufacturerCode(soReturnLineV2.getManufacturerCode());
				apiLine.setBrand(soReturnLineV2.getBrand());
				orderLinesV2.add(apiLine);
			}
			apiHeader.setLine(orderLinesV2);
			apiHeader.setOrderProcessedOn(new Date());

			if (soReturnV2.getSoReturnLine() != null && ! soReturnV2.getSoReturnLine().isEmpty()) {

				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrderV2 = orderService.createInboundOrdersV2(apiHeader);
				log.info("Return Order Reference Order Success: " + createdOrderV2);
				return createdOrderV2;
			} else if (soReturnV2.getSoReturnLine() == null || soReturnV2.getSoReturnLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrderV2 = orderService.createInboundOrdersV2(apiHeader);
				log.info("Return Order Reference Order Failed : " + createdOrderV2);
				throw new BadRequestException("Return Order Reference Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}


	/**
	 *
	 * @param interWarehouseTransferInV2
	 * @return
	 */
	public InboundOrderV2 postInterWarehouseTransferInV2(InterWarehouseTransferInV2 interWarehouseTransferInV2) {
		log.info("InterWarehouseTransferHeaderV2 received from External: " + interWarehouseTransferInV2);
		InboundOrderV2 savedIWHReturnV2 = saveInterWarehouseTransferInV2 (interWarehouseTransferInV2);
		log.info("interWarehouseTransferHeaderV2: " + savedIWHReturnV2);
		return savedIWHReturnV2;
	}


	// InterWarehouseTransferInV2
	private InboundOrderV2 saveInterWarehouseTransferInV2 (InterWarehouseTransferInV2 interWarehouseTransferInV2) {
		try {
			InterWarehouseTransferInHeaderV2 interWarehouseTransferInHeaderV2 = interWarehouseTransferInV2.getInterWarehouseTransferInHeader();
			List<InterWarehouseTransferInLineV2> interWarehouseTransferInLinesV2 = interWarehouseTransferInV2.getInterWarehouseTransferInLine();
			InboundOrderV2 apiHeader = new InboundOrderV2();
			apiHeader.setRefDocumentNo(interWarehouseTransferInHeaderV2.getTransferOrderNumber());
			apiHeader.setOrderId(interWarehouseTransferInHeaderV2.getTransferOrderNumber());
			apiHeader.setCompanyCode(interWarehouseTransferInHeaderV2.getToCompanyCode());
			apiHeader.setTransferOrderNumber(interWarehouseTransferInHeaderV2.getTransferOrderNumber());
			apiHeader.setBranchCode(interWarehouseTransferInHeaderV2.getToBranchCode());
			apiHeader.setInboundOrderTypeId(3L);				// Hardcoded Value 3
			apiHeader.setRefDocumentType("IWTV2");
			apiHeader.setOrderReceivedOn(new Date());

			// Get Warehouse
			Optional<com.tekclover.wms.api.transaction.model.warehouse.Warehouse> dbWarehouse =
					warehouseRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndDeletionIndicator(
							interWarehouseTransferInHeaderV2.getToCompanyCode(),
							interWarehouseTransferInHeaderV2.getToBranchCode(),
							"EN",
							0L
					);
			log.info("dbWarehouse : " + dbWarehouse);
			apiHeader.setWarehouseID(dbWarehouse.get().getWarehouseId());
			Set<InboundOrderLinesV2> orderLinesV2 = new HashSet<>();
			for (InterWarehouseTransferInLineV2 iwhTransferLineV2 : interWarehouseTransferInLinesV2) {
				InboundOrderLinesV2 apiLine = new InboundOrderLinesV2();
				apiLine.setLineReference(iwhTransferLineV2.getLineReference());                 // IB_LINE_NO
				apiLine.setItemCode(iwhTransferLineV2.getSku());                                // ITM_CODE
				apiLine.setItemText(iwhTransferLineV2.getSkuDescription());                     // ITEM_TEXT
				apiLine.setFromCompanyCode(iwhTransferLineV2.getFromCompanyCode());
				apiLine.setSourceBranchCode(iwhTransferLineV2.getFromBranchCode());
				apiLine.setSupplierPartNumber(iwhTransferLineV2.getSupplierPartNumber());        // PARTNER_ITM_CODE
				apiLine.setManufacturerName(iwhTransferLineV2.getManufacturerName());            // BRND_NM
				apiLine.setExpectedQty(iwhTransferLineV2.getExpectedQty());
				apiLine.setUom(iwhTransferLineV2.getUom());
				apiLine.setPackQty(iwhTransferLineV2.getPackQty());
				apiLine.setOrigin(iwhTransferLineV2.getOrigin());
				apiLine.setSupplierName(iwhTransferLineV2.getSupplierName());
				apiLine.setManufacturerCode(iwhTransferLineV2.getManufacturerCode());
				apiLine.setBrand(iwhTransferLineV2.getBrand());
				apiLine.setOrderId(apiHeader.getOrderId());
				orderLinesV2.add(apiLine);

				// EA_DATE
				try {
					ZoneId defaultZoneId = ZoneId.systemDefault();
					String sdate = iwhTransferLineV2.getExpectedDate();
					String firstHalf = sdate.substring(0,sdate.lastIndexOf("/"));
					String secondHalf = sdate.substring(sdate.lastIndexOf("/")+1);
					secondHalf = "/20" + secondHalf;
					sdate = firstHalf + secondHalf;
					log.info("sdate--------> : " + sdate);

					LocalDate localDate = DateUtils.dateConv2(sdate);
					log.info("localDate--------> : " + localDate);
					Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
					apiLine.setExpectedDate(date);
				} catch (Exception e) {
					e.printStackTrace();
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}
			}
			apiHeader.setLine(orderLinesV2);
			apiHeader.setOrderProcessedOn(new Date());
			if (interWarehouseTransferInV2.getInterWarehouseTransferInLine() != null &&
					!interWarehouseTransferInV2.getInterWarehouseTransferInLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrderV2 = orderService.createInboundOrdersV2(apiHeader);
				log.info("InterWarehouseTransferV2 Order Success: " + createdOrderV2);
				return createdOrderV2;
			} else if (interWarehouseTransferInV2.getInterWarehouseTransferInLine() == null ||
					interWarehouseTransferInV2.getInterWarehouseTransferInLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrderV2 = orderService.createInboundOrdersV2(apiHeader);
				log.info("InterWarehouseTransferV2 Order Failed : " + createdOrderV2);
				throw new BadRequestException("InterWarehouseTransferInV2 Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	public InboundOrderV2 postB2bTransferIn(B2bTransferIn b2bTransferIn) {
		log.info("B2bTransferIn received from External: " + b2bTransferIn);
		InboundOrderV2 savedB2bTransferIn = saveB2BTransferIn (b2bTransferIn);
		log.info("B2bTransferIn: " + savedB2bTransferIn);
		return savedB2bTransferIn;
	}


	// B2bTransferIn
	private InboundOrderV2 saveB2BTransferIn (B2bTransferIn b2bTransferIn) {
		try {
			B2bTransferInHeader b2BTransferInHeader = b2bTransferIn.getB2bTransferInHeader();

//			// Warehouse ID Validation
//			validateWarehouseId (b2BTransferInHeader.getBranchCode());

			// Checking for duplicate RefDocNumber
			InboundOrderV2 dbApiHeader = orderService.getOrderByIdV2(b2BTransferInHeader.getTransferOrderNumber());
			if (dbApiHeader != null) {
				throw new InboundOrderRequestException("Return Order Reference is already posted and it can't be duplicated.");
			}

			List<B2bTransferInLine> b2bTransferInLines = b2bTransferIn.getB2bTransferLine();
			InboundOrderV2 apiHeader = new InboundOrderV2();
			apiHeader.setTransferOrderNumber(b2BTransferInHeader.getTransferOrderNumber());
			apiHeader.setCompanyCode(b2BTransferInHeader.getCompanyCode());
			apiHeader.setBranchCode(b2BTransferInHeader.getBranchCode());
			apiHeader.setOrderId(b2BTransferInHeader.getTransferOrderNumber());
			apiHeader.setRefDocumentType("B2BIN");
			apiHeader.setInboundOrderTypeId(4L);                                        // Hardcoded Value 4
			apiHeader.setOrderReceivedOn(new Date());

			Set<InboundOrderLinesV2> orderLines = new HashSet<>();
			for (B2bTransferInLine b2bTransferInLine : b2bTransferInLines) {
				InboundOrderLinesV2 apiLine = new InboundOrderLinesV2();
				apiLine.setLineReference(b2bTransferInLine.getLineReference());                 // IB_LINE_NO
				apiLine.setItemCode(b2bTransferInLine.getSku());                                // ITM_CODE
				apiLine.setItemText(b2bTransferInLine.getSkuDescription());                     // ITEM_TEXT
				apiLine.setSourceBranchCode(b2bTransferInLine.getStoreID());
				apiLine.setSupplierPartNumber(b2bTransferInLine.getSupplierPartNumber());
				apiLine.setManufacturerName(b2bTransferInLine.getManufacturerName());
				apiLine.setExpectedQty(b2bTransferInLine.getExpectedQty());
				apiLine.setCountryOfOrigin(b2bTransferInLine.getOrigin());
				apiLine.setManufacturerCode(b2bTransferInLine.getManufacturerCode());
				apiLine.setBrand(b2bTransferInLine.getBrand());
				apiLine.setSupplierName(b2bTransferInLine.getSupplierName());
				apiLine.setOrderId(apiHeader.getOrderId());

				// EA_DATE
				try {
					Date reqDelDate = DateUtils.convertStringToDate(String.valueOf(b2bTransferInLine.getExpectedDate()));
					apiLine.setExpectedDate(reqDelDate);
				} catch (Exception e) {
					throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
				}

				apiLine.setUom(b2bTransferInLine.getUom());                                    // ORD_UOM
				apiLine.setItemCaseQty(Double.valueOf(b2bTransferInLine.getPackQty()));        // ITM_CASE_QTY
				orderLines.add(apiLine);
			}
			apiHeader.setLine(orderLines);
			apiHeader.setOrderProcessedOn(new Date());

			if (b2bTransferIn.getB2bTransferLine() != null && !b2bTransferIn.getB2bTransferLine().isEmpty()) {
				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrder = orderService.createInboundOrdersV2(apiHeader);
				log.info("Return Order Reference Order Success: " + createdOrder);
				return createdOrder;
			} else if (b2bTransferIn.getB2bTransferLine() == null || b2bTransferIn.getB2bTransferLine().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				InboundOrderV2 createdOrder = orderService.createInboundOrdersV2(apiHeader);
				log.info("Return Order Reference Order Failed : " + createdOrder);
				throw new BadRequestException("Return Order Reference Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------------------
	// ASN
	public AXApiResponse postASNConfirmationV2 (com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.ASNV2 asn,
											  String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API RestTemplate");
		headers.add("Authorization", "Bearer " + authToken);
		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceAsnUrl());
		HttpEntity<?> entity = new HttpEntity<>(asn, headers);
		ResponseEntity<AXApiResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// StoreReturn
	public AXApiResponse postStoreReturnConfirmationV2 (
			com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.StoreReturn storeReturn,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceStoreReturnUrl());
		HttpEntity<?> entity = new HttpEntity<>(storeReturn, headers);
		ResponseEntity<AXApiResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	// Sale Order Returns
	public AXApiResponse postSOReturnConfirmationV2 (
			com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.SOReturn soReturn,
			String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceSOReturnUrl());
		HttpEntity<?> entity = new HttpEntity<>(soReturn, headers);
		ResponseEntity<AXApiResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/**
	 *
	 * @param iwhTransfer
	 * @param access_token
	 * @return
	 */
	public AXApiResponse postInterWarehouseTransferConfirmationV2(InterWarehouseTransferInV2 iwhTransfer,
																String access_token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "AX-API Rest service");
		headers.add("Authorization", "Bearer " + access_token);

		UriComponentsBuilder builder =
				UriComponentsBuilder.fromHttpUrl(propertiesConfig.getAxapiServiceInterwareHouseUrl());
		HttpEntity<?> entity = new HttpEntity<>(iwhTransfer, headers);
		ResponseEntity<AXApiResponse> result =
				getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, AXApiResponse.class);
		log.info("result : " + result.getStatusCode());
		return result.getBody();
	}

	/*---------------------------------CycleCountOrder------------------------------------------*/

	// 	Perpetual
	/**
	 *
	 * @param perpetual
	 * @return
	 */
	public CycleCountHeader postPerpetual(Perpetual perpetual) {
		log.info("CycleCountHeaderOrder received from External: " + perpetual);
		CycleCountHeader savedCycleCount = savePerpetual(perpetual);
		log.info("Perpetual: " + perpetual);
		return savedCycleCount;
	}

	// Perpetual
	private CycleCountHeader savePerpetual(Perpetual perpetual) {
		try {
			PerpetualHeaderV1 perpetualHeaderV1 = perpetual.getPerpetualHeaderV1();
			List<PerpetualLineV1> perpetualLineV1List = perpetual.getPerpetualLineV1();
			CycleCountHeader apiHeader = new CycleCountHeader();
			apiHeader.setCompanyCode(perpetualHeaderV1.getCompanyCode());
			apiHeader.setCycleCountNo(perpetualHeaderV1.getCycleCountNo());
			apiHeader.setOrderId(perpetualHeaderV1.getCycleCountNo());
			apiHeader.setBranchCode(perpetualHeaderV1.getBranchCode());
			apiHeader.setBranchName(perpetualHeaderV1.getBranchName());
			apiHeader.setIsNew(perpetualHeaderV1.getIsNew());
			apiHeader.setCycleCountCreationDate(new Date());

			Set<CycleCountLine> cycleCountLines = new HashSet<>();
			for (PerpetualLineV1 perpetualLineV1 : perpetualLineV1List) {
				CycleCountLine apiLine = new CycleCountLine();
				apiLine.setCycleCountNo(perpetualLineV1.getCycleCountNo());								// CC_NO
				apiLine.setLineOfEachItemCode(perpetualLineV1.getLineNoOfEachItemCode());                	// INV_NO
				apiLine.setItemCode(perpetualLineV1.getItemCode());										// ITM_CODE
				apiLine.setItemDescription(perpetualLineV1.getItemDescription());                 		// ITM_DESC
				apiLine.setUom(perpetualLineV1.getUom());                                					// UOM
				apiLine.setManufacturerCode(perpetualLineV1.getManufacturerCode());                     	// MANU_FAC_CODE
				apiLine.setManufacturerName(perpetualLineV1.getManufacturerName());        				// MANU_FAC_NM
				apiLine.setOrderId(apiHeader.getOrderId());

				cycleCountLines.add(apiLine);
			}
			apiHeader.setLines(cycleCountLines);
			apiHeader.setOrderProcessedOn(new Date());

			if (perpetual.getPerpetualLineV1() != null && ! perpetual.getPerpetualLineV1().isEmpty()) {

				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				CycleCountHeader cycleCountHeader = orderService.createCycleCountOrder(apiHeader);
				log.info("Perpetual Order Success: " + cycleCountHeader);
				return cycleCountHeader;
			} else if (perpetual.getPerpetualLineV1() == null || perpetual.getPerpetualLineV1().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				CycleCountHeader createOrder = orderService.createCycleCountOrder(apiHeader);
				log.info("Periodic Order Failed : " + createOrder);
				throw new BadRequestException("Return Order Reference Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}


	//Periodic
	/**
	 *
	 * @param periodic
	 * @return
	 */
	public CycleCountHeader postPeriodic(Periodic periodic) {
		log.info("Periodic received from External: " + periodic);
		CycleCountHeader savedCycleCount = savePeriodic(periodic);
		log.info("Periodic: " + periodic);
		return savedCycleCount;
	}

	// periodic
	private CycleCountHeader savePeriodic (Periodic periodic) {
		try {
			PeriodicHeaderV1 perpetualHeaderV1 = periodic.getPeriodicHeaderV1();
			List<PeriodicLineV1> periodicLineV1List = periodic.getPeriodicLineV1();
			CycleCountHeader apiHeader = new CycleCountHeader();
			apiHeader.setCompanyCode(perpetualHeaderV1.getCompanyCode());
			apiHeader.setCycleCountNo(perpetualHeaderV1.getCycleCountNo());
			apiHeader.setOrderId(perpetualHeaderV1.getCycleCountNo());
			apiHeader.setBranchCode(perpetualHeaderV1.getBranchCode());
			apiHeader.setBranchName(perpetualHeaderV1.getBranchName());
			apiHeader.setIsNew(perpetualHeaderV1.getIsNew());
			apiHeader.setCycleCountCreationDate(new Date());

			Set<CycleCountLine> cycleCountLines = new HashSet<>();
			for (PeriodicLineV1 periodicLineV1 : periodicLineV1List) {
				CycleCountLine apiLine = new CycleCountLine();
				apiLine.setCycleCountNo(periodicLineV1.getCycleCountNo());								// CC_NO
				apiLine.setLineOfEachItemCode(periodicLineV1.getLineNoOfEachItemCode());                	// INV_NO
				apiLine.setItemCode(periodicLineV1.getItemCode());										// ITM_CODE
				apiLine.setItemDescription(periodicLineV1.getItemDescription());                 		// ITM_DESC
				apiLine.setUom(periodicLineV1.getUom());                                					// UOM
				apiLine.setManufacturerCode(periodicLineV1.getManufacturerCode());                     	// MANU_FAC_CODE
				apiLine.setManufacturerName(periodicLineV1.getManufacturerName());        				// MANU_FAC_NM
				apiLine.setOrderId(apiHeader.getOrderId());

				cycleCountLines.add(apiLine);
			}
			apiHeader.setLines(cycleCountLines);
			apiHeader.setOrderProcessedOn(new Date());

			if (periodic.getPeriodicLineV1() != null && ! periodic.getPeriodicLineV1().isEmpty()) {

				apiHeader.setProcessedStatusId(0L);
				log.info("apiHeader : " + apiHeader);
				CycleCountHeader cycleCountHeader = orderService.createCycleCountOrder(apiHeader);
				log.info("Periodic Order Success: " + cycleCountHeader);
				return cycleCountHeader;
			} else if (periodic.getPeriodicLineV1() == null || periodic.getPeriodicLineV1().isEmpty()) {
				// throw the error as Lines are Empty and set the Indicator as '100'
				apiHeader.setProcessedStatusId(100L);
				log.info("apiHeader : " + apiHeader);
				CycleCountHeader createOrder = orderService.createCycleCountOrder(apiHeader);
				log.info("Periodic Order Failed : " + createOrder);
				throw new BadRequestException("Return Order Reference Order doesn't contain any Lines.");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}


}
