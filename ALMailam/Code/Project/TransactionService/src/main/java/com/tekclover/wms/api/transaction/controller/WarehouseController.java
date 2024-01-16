package com.tekclover.wms.api.transaction.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.ASN;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InboundOrder;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.InterWarehouseTransferIn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.SaleOrderReturn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.StoreReturn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.ASNV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.B2bTransferIn;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InterWarehouseTransferInV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.SaleOrderReturnV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOut;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ReturnPO;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrder;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.ShipmentOrder;
import com.tekclover.wms.api.transaction.service.WarehouseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"Warehouse"}, value = "Warehouse Operations related to WarehouseController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Warehouse ",description = "Operations related to Warehouse ")})
@RequestMapping("/warehouse")
@RestController
public class WarehouseController {
	
	@Autowired
	WarehouseService warehouseService;
	
	/*----------------------------INBOUND------------------------------------------------------------*/
    
	// ASN
	@ApiOperation(response = ASN.class, value = "ASN") // label for swagger
	@PostMapping("/inbound/asn")
	public ResponseEntity<?> postASN (@Valid @RequestBody ASN asn) 
			throws IllegalAccessException, InvocationTargetException {
		try {
			InboundOrder createdASNHeader = warehouseService.postWarehouseASN(asn);
			if (createdASNHeader != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("ASN Order Error: " + asn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
	
	/*-----------------StoreReturn---------------------------------------------------------*/
    @ApiOperation(response = StoreReturn.class, value = "Store Return") // label for swagger
	@PostMapping("/inbound/storeReturn")
	public ResponseEntity<?> postStoreReturn(@Valid @RequestBody StoreReturn storeReturn) 
			throws IllegalAccessException, InvocationTargetException {
    	try {
			InboundOrder createdStoreReturn = warehouseService.postStoreReturn(storeReturn);
			if (createdStoreReturn != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("Store Return Order Error: " + storeReturn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    /*-----------------Sale Order Return---------------------------------------------------------*/
    @ApiOperation(response = SaleOrderReturn.class, value = "Sale Order Return") // label for swagger
	@PostMapping("/inbound/soReturn")
	public ResponseEntity<?> postSOReturn(@Valid @RequestBody SaleOrderReturn soReturn) 
			throws IllegalAccessException, InvocationTargetException {
    	try {
			InboundOrder createdSOReturn = warehouseService.postSOReturn(soReturn);
			if (createdSOReturn != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("soReturn order Error: " + soReturn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    /*-----------------InterWarehouseTransfer-Inbound---------------------------------------------------------*/
    @ApiOperation(response = InterWarehouseTransferIn.class, value = "Inter Warehouse Transfer") // label for swagger
	@PostMapping("/inbound/interWarehouseTransfer")
	public ResponseEntity<?> postInterWarehouseTransfer(@Valid @RequestBody InterWarehouseTransferIn interWarehouseTransferIn) 
			throws IllegalAccessException, InvocationTargetException {
    	try {
			InboundOrder createdInterWarehouseTransferIn = 
					warehouseService.postInterWarehouseTransfer(interWarehouseTransferIn);
			if (createdInterWarehouseTransferIn != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("interWarehouseTransfer order Error: " + interWarehouseTransferIn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    /*--------------------------------Outbound--------------------------------------------------------------*/
    /*----------------------------Shipment order------------------------------------------------------------*/
    @ApiOperation(response = ShipmentOrder.class, value = "Create Shipment Order") // label for swagger
	@PostMapping("/outbound/so")
	public ResponseEntity<?> postShipmenOrder(@Valid @RequestBody ShipmentOrder shipmenOrder) 
			throws IllegalAccessException, InvocationTargetException {
    	try {
			ShipmentOrder createdSO = warehouseService.postSO(shipmenOrder, false);
			if (createdSO != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("ShipmentOrder order Error: " + shipmenOrder);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    //----------------------------Bulk Orders---------------------------------------------------------------------
    @ApiOperation(response = ShipmentOrder.class, value = "Create Bulk Shipment Orders") // label for swagger
	@PostMapping("/outbound/so/bulk")
	public ResponseEntity<?> postShipmenOrders(@Valid @RequestBody List<ShipmentOrder> shipmenOrders) 
			throws IllegalAccessException, InvocationTargetException {
    	try {
    		List<WarehouseApiResponse> responseList = new ArrayList<>();
    		for (ShipmentOrder shipmentOrder : shipmenOrders) {
				ShipmentOrder createdSO = warehouseService.postSO(shipmentOrder, false);
				if (createdSO != null) {
					WarehouseApiResponse response = new WarehouseApiResponse();
					response.setStatusCode("200");
					response.setMessage("Success");
					responseList.add(response);
				}
    		}
    		return new ResponseEntity<>(responseList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}
    
    /*----------------------------Sale order True Express-------------------------------------------------------*/
    @ApiOperation(response = SalesOrder.class, value = "Sales order") // label for swagger
	@PostMapping("/outbound/salesOrder")
	public ResponseEntity<?> postSalesOrder(@Valid @RequestBody SalesOrder salesOrder) 
			throws IllegalAccessException, InvocationTargetException {
		try {
			SalesOrder createdSalesOrder = warehouseService.postSalesOrder(salesOrder);
			if (createdSalesOrder != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("SalesOrder order Error: " + salesOrder);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    /*----------------------------Return PO------------------------------------------------------------*/
    @ApiOperation(response = ReturnPO.class, value = "Return PO") // label for swagger
	@PostMapping("/outbound/returnPO")
	public ResponseEntity<?> postReturnPO(@Valid @RequestBody ReturnPO returnPO) 
			throws IllegalAccessException, InvocationTargetException {
		try {
			ReturnPO createdReturnPO = warehouseService.postReturnPO(returnPO);
			if (createdReturnPO != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("ReturnPO order Error: " + returnPO);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
    
    /*----------------------------Interwarehouse SO------------------------------------------------------------*/
    @ApiOperation(response = InterWarehouseTransferOut.class, value = "Inter Warehouse Transfer Out") // label for swagger
   	@PostMapping("/outbound/interWarehouseTransfer")
   	public ResponseEntity<?> postReturnPO(@Valid 
   			@RequestBody InterWarehouseTransferOut interWarehouseTransfer) 
   			throws IllegalAccessException, InvocationTargetException {
   		try {
			InterWarehouseTransferOut createdInterWarehouseTransfer = 
					warehouseService.postInterWarehouseTransferOutbound(interWarehouseTransfer);
			if (createdInterWarehouseTransfer != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("InterWarehouseTransferOut order Error: " + interWarehouseTransfer);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
   	}

	// ASNV2
	@ApiOperation(response = ASNV2.class, value = "ASNV2") // label for swagger
	@PostMapping("/inbound/asn/v2")
	public ResponseEntity<?> postASNV2 (@Valid @RequestBody ASNV2 asn)
			throws IllegalAccessException, InvocationTargetException {
		try {
			InboundOrderV2 createdASNHeader = warehouseService.postWarehouseASNV2(asn);
			if (createdASNHeader != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("ASNV2 Order Error: " + asn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}

	//Sale Order Return
	@ApiOperation(response = SaleOrderReturnV2.class, value = "Sale Order ReturnV2") // label for swagger
	@PostMapping("/inbound/soReturn/v2")
	public ResponseEntity<?> postSOReturnV2(@Valid @RequestBody SaleOrderReturnV2 soReturnV2)
			throws IllegalAccessException, InvocationTargetException {
		try {
			InboundOrderV2 createdSOReturnV2 = warehouseService.postSOReturnV2(soReturnV2);
			if (createdSOReturnV2 != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("soReturn order Error: " + soReturnV2);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}

	//InterWarehouseTransferInV2-Inbound
	@ApiOperation(response = InterWarehouseTransferInV2.class, value = "Inter Warehouse Transfer V2") // label for swagger
	@PostMapping("/inbound/interWarehouseTransferIn/v2")
	public ResponseEntity<?> postInterWarehouseTransferInV2(@Valid @RequestBody InterWarehouseTransferInV2 interWarehouseTransferInV2)
			throws IllegalAccessException, InvocationTargetException {
		try {
			InboundOrderV2 createdInterWarehouseTransferInV2 =
					warehouseService.postInterWarehouseTransferInV2(interWarehouseTransferInV2);
			if (createdInterWarehouseTransferInV2 != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("interWarehouseTransfer order Error: " + interWarehouseTransferInV2);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
	
	// WH2WH Transfer Order
	@ApiOperation(response = InterWarehouseTransferInV2.class, value = "Upload Inter Warehouse Transfer V2") // label for swagger
	@PostMapping("/inbound/interWarehouseTransferIn/upload/v2")
	public ResponseEntity<?> postInterWarehouseTransferInUploadV2 (@Valid @RequestBody List<InterWarehouseTransferInV2> interWarehouseTransferInV2List)
			throws IllegalAccessException, InvocationTargetException {
		try {
			List<WarehouseApiResponse> responseList = new ArrayList<>();
			for (InterWarehouseTransferInV2 interWarehouseTransferInV2 : interWarehouseTransferInV2List) {
				InboundOrderV2 createdInterWarehouseTransferInV2 =
							warehouseService.postInterWarehouseTransferInV2(interWarehouseTransferInV2);
				if (createdInterWarehouseTransferInV2 != null) {
					WarehouseApiResponse response = new WarehouseApiResponse();
					response.setStatusCode("200");
					response.setMessage("Success");
					responseList.add(response);
				}
			}
			return new ResponseEntity<>(responseList, HttpStatus.OK);
		} catch (Exception e) {
			log.info("interWarehouseTransfer order Error: " + e);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	/*-----------------B2bTransferIn-Inbound---------------------------------------------------------*/
	@ApiOperation(response = B2bTransferIn.class, value = "B2bTransferIn") // label for swagger
	@PostMapping("/inbound/b2bTransferIn")
	public ResponseEntity<?> postB2bTransferIn(@Valid @RequestBody B2bTransferIn b2bTransferIn)
			throws IllegalAccessException, InvocationTargetException {
		try {
			InboundOrderV2 createdB2bTransferIn =
					warehouseService.postB2bTransferIn(b2bTransferIn);
			if (createdB2bTransferIn != null) {
				WarehouseApiResponse response = new WarehouseApiResponse();
				response.setStatusCode("200");
				response.setMessage("Success");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.info("interWarehouseTransfer order Error: " + b2bTransferIn);
			e.printStackTrace();
			WarehouseApiResponse response = new WarehouseApiResponse();
			response.setStatusCode("1400");
			response.setMessage("Not Success: " + e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		return null;
	}
}