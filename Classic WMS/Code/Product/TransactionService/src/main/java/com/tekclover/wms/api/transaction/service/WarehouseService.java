package com.tekclover.wms.api.transaction.service;

import java.util.*;

import javax.validation.Valid;

import com.tekclover.wms.api.transaction.model.IkeyValuePair;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.*;
import com.tekclover.wms.api.transaction.repository.ContainerReceiptRepository;
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
    private ContainerReceiptRepository containerReceiptRepository;

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    OrderService orderService;

    @Autowired
    WarehouseRepository warehouseRepository;

    /**
     * @return
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    /**
     * @param asn
     * @return
     */
    public InboundOrder postWarehouseASN(ASN asn) {
        log.info("ASNHeader received from External: " + asn);
        InboundOrder savedAsnHeader = saveASN(asn);
        log.info("savedAsnHeader: " + savedAsnHeader);
        return savedAsnHeader;
    }

    /**
     * @param soReturn
     * @return
     */
    public InboundOrder postSOReturn(SaleOrderReturn soReturn) {
        log.info("StoreReturnHeader received from External: " + soReturn);
        InboundOrder savedSOReturn = saveSOReturn(soReturn);
        log.info("soReturnHeader: " + savedSOReturn);
        return savedSOReturn;
    }

    /**
     *
     * @param
     * @return
     */
    public InboundOrder postB2bTransferIn(B2bTransferIn b2bTransferIn) {
        log.info("B2bTransferIn received from External: " + b2bTransferIn);
        InboundOrder savedB2bTransferIn = saveB2BTransferIn (b2bTransferIn);
        log.info("B2bTransferIn: " + savedB2bTransferIn);
        return savedB2bTransferIn;
    }


    /**
     * @param storeReturn
     * @return
     */
    public InboundOrder postStoreReturn(StoreReturn storeReturn) {
        log.info("StoreReturnHeader received from External: " + storeReturn);
        InboundOrder savedStoreReturn = saveStoreReturn(storeReturn);
        log.info("savedStoreReturn: " + savedStoreReturn);
        return savedStoreReturn;
    }


    /**
     * @param interWarehouseTransferIn
     * @return
     */
    public InboundOrder postInterWarehouseTransfer(InterWarehouseTransferIn interWarehouseTransferIn) {
        log.info("InterWarehouseTransferHeader received from External: " + interWarehouseTransferIn);
        InboundOrder savedIWHReturn = saveInterWarehouseTransfer(interWarehouseTransferIn);
        log.info("interWarehouseTransferHeader: " + savedIWHReturn);
        return savedIWHReturn;
    }



    /*-------------------------------------OUTBOUND---------------------------------------------------*/

    /**
     * ShipmentOrder
     *
     * @param shipmenOrder
     * @return
     */
    public ShipmentOrder postSO(ShipmentOrder shipmenOrder, boolean isRerun) {
        log.info("ShipmenOrder received from External: " + shipmenOrder);
        OutboundOrder savedSoHeader = saveSO(shipmenOrder, isRerun);                        // Without Nongo
        log.info("savedSoHeader: " + savedSoHeader.getRefDocumentNo());
        return shipmenOrder;
    }

    /**
     * @param salesOrder
     * @return
     */
    public SalesOrder postSalesOrder(SalesOrder salesOrder) {
        log.info("SalesOrderHeader received from External: " + salesOrder);
        OutboundOrder savedSoHeader = saveSalesOrder(salesOrder);                                // Without Nongo
        log.info("salesOrderHeader: " + savedSoHeader);
        return salesOrder;
    }

    /**
     * @param returnPO
     * @return
     */
    public ReturnPO postReturnPO(ReturnPO returnPO) {
        log.info("ReturnPOHeader received from External: " + returnPO);
        OutboundOrder savedReturnPOHeader = saveReturnPO(returnPO);                    // Without Nongo
        log.info("savedReturnPOHeader: " + savedReturnPOHeader);
        return returnPO;
    }

    /**
     * @param interWarehouseTransfer
     * @return
     */
    public InterWarehouseTransferOut postInterWarehouseTransferOutbound(InterWarehouseTransferOut interWarehouseTransfer) {
        log.info("InterWarehouseTransferHeader received from External: " + interWarehouseTransfer);
        OutboundOrder savedInterWarehouseTransferHeader = saveIWHTransfer(interWarehouseTransfer);                                                    // Without Nongo
        log.info("savedInterWarehouseTransferHeader: " + savedInterWarehouseTransferHeader);
        return interWarehouseTransfer;
    }

    /*----------------------------INBOUND-CONFIRMATION-POST---------------------------------------------*/
    // ASN
    public AXApiResponse postASNConfirmation(com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.ASN asn,
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
    public AXApiResponse postStoreReturnConfirmation(
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
    public AXApiResponse postSOReturnConfirmation(
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
     * InterWarehouseTransfer API
     *
     * @param iwhTransferHeader
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
     *
     * @param shipmentHeader
     * @param access_token
     * @return
     */
    public AXApiResponse postShipmentConfirmation(Shipment shipment, String access_token) {
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
     *
     * @param iwhShipmentHeader
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
     *
     * @param salesOrderHeader
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
     *
     * @param returnPOHeader
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
     * @return
     */
    public static synchronized String getUUID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    //------------------------------------------------INBOUND-ORDERS--------------------------------------------------------------------------------

    // POST ASNHeader
    private InboundOrder saveASN(ASN asn) {
        try {
            ASNHeader asnHeader = asn.getAsnHeader();

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
            apiHeader.setCompanyCode(asnHeader.getCompanyCode());
            apiHeader.setBranchCode(asnHeader.getBranchCode());
            apiHeader.setLanguageId(asnHeader.getLanguageId());
            apiHeader.setInboundOrderTypeId(1L);

            IkeyValuePair ikeyValuePair = containerReceiptRepository.getDescription(apiHeader.getLanguageId(),
                    apiHeader.getCompanyCode(), apiHeader.getBranchCode(), apiHeader.getWarehouseID());

            if(ikeyValuePair != null){
                apiHeader.setCompanyDescription(ikeyValuePair.getCompanyDesc());
                apiHeader.setPlantDescription(ikeyValuePair.getPlantDesc());
                apiHeader.setWarehouseDescription(ikeyValuePair.getWarehouseDesc());
            }

            apiHeader.setOrderReceivedOn(new Date());

            Set<InboundOrderLines> orderLines = new HashSet<>();
            for (ASNLine asnLine : asnLines) {
                InboundOrderLines apiLine = new InboundOrderLines();
                apiLine.setLineReference(asnLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(asnLine.getSku());                            // ITM_CODE
                apiLine.setItemText(asnLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setInvoiceNumber(asnLine.getInvoiceNumber());            // INV_NO
                apiLine.setContainerNumber(asnLine.getContainerNumber());        // CONT_NO
                apiLine.setSupplierCode(asnLine.getSupplierCode());                // PARTNER_CODE
                apiLine.setSupplierPartNumber(asnLine.getSupplierPartNumber()); // PARTNER_ITM_CODE
                apiLine.setManufacturerName(asnLine.getManufacturerName());        // BRND_NM
                apiLine.setManufacturerPartNo(asnLine.getManufacturerPartNo());    // MFR_PART
                apiLine.setOrderId(apiHeader.getOrderId());

                // EA_DATE
                try {
                    Date reqDelDate = DateUtils.convertStringToDate(asnLine.getExpectedDate());
                    apiLine.setExpectedDate(reqDelDate);
                } catch (Exception e) {
                    throw new BadRequestException("Date format should be MM-dd-yyyy");
                }

                apiLine.setOrderedQty(asnLine.getExpectedQty());                // ORD_QTY
                apiLine.setUom(asnLine.getUom());                                // ORD_UOM
                apiLine.setItemCaseQty(asnLine.getPackQty());                    // ITM_CASE_QTY
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

    // SOReturn
    private InboundOrder saveSOReturn(SaleOrderReturn soReturn) {
        try {
            SOReturnHeader soReturnHeader = soReturn.getSoReturnHeader();

            InboundOrder dbApiHeader = orderService.getOrderById(soReturnHeader.getReturnOrderReference());
            if (dbApiHeader != null) {
                throw new InboundOrderRequestException("Return Order Reference is already posted and it can't be duplicated.");
            }

            List<SOReturnLine> storeReturnLines = soReturn.getSoReturnLine();
            InboundOrder apiHeader = new InboundOrder();
            apiHeader.setOrderId(soReturnHeader.getReturnOrderReference());
            apiHeader.setRefDocumentNo(soReturnHeader.getReturnOrderReference());
            apiHeader.setWarehouseID(soReturnHeader.getWareHouseId());
            apiHeader.setCompanyCode(soReturnHeader.getCompanyCode());
            apiHeader.setBranchCode(soReturnHeader.getBranchCode());
            apiHeader.setLanguageId(soReturnHeader.getLanguageId());
            apiHeader.setRefDocumentType("RETURN");
            apiHeader.setInboundOrderTypeId(4L);                                        // Hardcoded Value 4
            apiHeader.setOrderReceivedOn(new Date());

            Set<InboundOrderLines> orderLines = new HashSet<>();
            for (SOReturnLine soReturnLine : storeReturnLines) {
                InboundOrderLines apiLine = new InboundOrderLines();
                apiLine.setLineReference(soReturnLine.getLineReference());                // IB_LINE_NO
                apiLine.setItemCode(soReturnLine.getSku());                                // ITM_CODE
                apiLine.setItemText(soReturnLine.getSkuDescription());                    // ITEM_TEXT
                apiLine.setInvoiceNumber(soReturnLine.getInvoiceNumber());                // INV_NO
                apiLine.setSupplierCode(soReturnLine.getStoreID());                        // PARTNER_CODE
                apiLine.setSupplierPartNumber(soReturnLine.getSupplierPartNumber());    // PARTNER_ITM_CODE
                apiLine.setManufacturerName(soReturnLine.getManufacturerName());        // BRND_NM

                apiLine.setOrderId(apiHeader.getOrderId());

                // EA_DATE
                try {
                    Date reqDelDate = DateUtils.convertStringToDate(soReturnLine.getExpectedDate());
                    apiLine.setExpectedDate(reqDelDate);
                } catch (Exception e) {
                    throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
                }

                apiLine.setOrderedQty(soReturnLine.getExpectedQty());                    // ORD_QTY
                apiLine.setUom(soReturnLine.getUom());                                    // ORD_UOM
                apiLine.setItemCaseQty(soReturnLine.getPackQty());                        // ITM_CASE_QTY
                apiLine.setOrigin(soReturnLine.getOrigin());
                apiLine.setManufacturerCode(soReturnLine.getManufacturerCode());
                apiLine.setBrand(soReturnLine.getBrand());
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


    // B2bTransferIn
    private InboundOrder saveB2BTransferIn (B2bTransferIn b2bTransferIn) {
        try {
            B2bTransferInHeader b2BTransferInHeader = b2bTransferIn.getB2bTransferInHeader();
            List<B2bTransferInLine> b2bTransferInLines = b2bTransferIn.getB2bTransferLine();
            InboundOrder apiHeader = new InboundOrder();
            apiHeader.setOrderId(b2BTransferInHeader.getTransferOrderNumber());
            apiHeader.setCompanyCode(b2BTransferInHeader.getCompanyCode());
            apiHeader.setBranchCode(b2BTransferInHeader.getBranchCode());
            apiHeader.setLanguageId(b2BTransferInHeader.getLanguageId());
            apiHeader.setOrderId(b2BTransferInHeader.getTransferOrderNumber());
            apiHeader.setRefDocumentNo(b2BTransferInHeader.getTransferOrderNumber());
            apiHeader.setRefDocumentType("Non-WMS to WMS");
            apiHeader.setInboundOrderTypeId(3L);
            apiHeader.setOrderReceivedOn(new Date());


            Set<InboundOrderLines> orderLines = new HashSet<>();
            for (B2bTransferInLine b2bTransferInLine : b2bTransferInLines) {
                InboundOrderLines apiLine = new InboundOrderLines();
                apiLine.setLineReference(b2bTransferInLine.getLineReference());
                apiLine.setItemCode(b2bTransferInLine.getSku());
                apiLine.setItemText(b2bTransferInLine.getSkuDescription());
                apiLine.setStoreID(b2bTransferInLine.getStoreID());
                apiLine.setSupplierPartNumber(b2bTransferInLine.getSupplierPartNumber());
                apiLine.setManufacturerName(b2bTransferInLine.getManufacturerName());
                apiLine.setExpectedQty(b2bTransferInLine.getExpectedQty());
                apiLine.setUom(b2bTransferInLine.getUom());
                apiLine.setPackQty(b2bTransferInLine.getPackQty());
                apiLine.setOrigin(b2bTransferInLine.getOrigin());
                apiLine.setManufacturerCode(b2bTransferInLine.getManufacturerCode());
                apiLine.setBrand(b2bTransferInLine.getBrand());
                apiLine.setSupplierName(b2bTransferInLine.getSupplierName());
                apiLine.setOrderId(apiHeader.getOrderId());

                try {
                    Date reqDelDate = DateUtils.convertStringToDate(b2bTransferInLine.getExpectedDate());
                    apiLine.setExpectedDate(reqDelDate);
                } catch (Exception e) {
                    throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
                }
                orderLines.add(apiLine);
            }
            apiHeader.setLines(orderLines);
            apiHeader.setOrderProcessedOn(new Date());

            if (b2bTransferIn.getB2bTransferLine() != null && !b2bTransferIn.getB2bTransferLine().isEmpty()) {
                apiHeader.setProcessedStatusId(0L);
                log.info("apiHeader : " + apiHeader);
                InboundOrder createdOrder = orderService.createInboundOrders(apiHeader);
                log.info("Return Order Reference Order Success: " + createdOrder);
                return createdOrder;
            } else if (b2bTransferIn.getB2bTransferLine() == null || b2bTransferIn.getB2bTransferLine().isEmpty()) {
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
    private InboundOrder saveInterWarehouseTransfer(InterWarehouseTransferIn interWarehouseTransferIn) {
        try {
            InterWarehouseTransferInHeader interWarehouseTransferInHeader = interWarehouseTransferIn.getInterWarehouseTransferInHeader();

            // Checking for duplicate RefDocNumber
            InboundOrder dbApiHeader = orderService.getOrderById(interWarehouseTransferInHeader.getTransferOrderNumber());
            if (dbApiHeader != null) {
                throw new InboundOrderRequestException("InterWarehouseTransfer is already posted and it can't be duplicated.");
            }

            List<InterWarehouseTransferInLine> interWarehouseTransferInLines = interWarehouseTransferIn.getInterWarehouseTransferInLine();
            InboundOrder apiHeader = new InboundOrder();
            apiHeader.setOrderId(interWarehouseTransferInHeader.getTransferOrderNumber());
            apiHeader.setRefDocumentNo(interWarehouseTransferInHeader.getTransferOrderNumber());
            apiHeader.setWarehouseID(interWarehouseTransferInHeader.getWarehouseId());
            apiHeader.setCompanyCode(interWarehouseTransferInHeader.getCompanyCode());
            apiHeader.setBranchCode(interWarehouseTransferInHeader.getBranchCode());
            apiHeader.setLanguageId(interWarehouseTransferInHeader.getLanguageId());
            apiHeader.setRefDocumentType("WH2WH");                // Hardcoded Value "WH to WH"
            apiHeader.setInboundOrderTypeId(3L);                // Hardcoded Value 3
            apiHeader.setOrderReceivedOn(new Date());

            Set<InboundOrderLines> orderLines = new HashSet<>();
            for (InterWarehouseTransferInLine iwhTransferLine : interWarehouseTransferInLines) {
                InboundOrderLines apiLine = new InboundOrderLines();
                apiLine.setLineReference(iwhTransferLine.getLineReference());                // IB_LINE_NO
                apiLine.setItemCode(iwhTransferLine.getSku());                                // ITM_CODE
                apiLine.setItemText(iwhTransferLine.getSkuDescription());                    // ITEM_TEXT
                apiLine.setSupplierPartNumber(iwhTransferLine.getSupplierPartNumber());        // PARTNER_ITM_CODE
                apiLine.setManufacturerName(iwhTransferLine.getManufacturerName());            // BRND_NM
                apiLine.setManufacturerPartNo(iwhTransferLine.getManufacturerPartNo());        // MFR_PART
                apiLine.setExpectedQty(iwhTransferLine.getExpectedQty());
                apiLine.setUom(iwhTransferLine.getUom());
                apiLine.setPackQty(iwhTransferLine.getPackQty());
                apiLine.setOrigin(iwhTransferLine.getOrigin());
                apiLine.setSupplierName(iwhTransferLine.getSupplierName());
                apiLine.setManufacturerCode(iwhTransferLine.getManufacturerCode());
                apiLine.setBrand(iwhTransferLine.getBrand());
                apiLine.setOrderId(apiHeader.getOrderId());

                // EA_DATE
                try {
                    Date reqDelDate = DateUtils.convertStringToDate(iwhTransferLine.getExpectedDate());
                    apiLine.setExpectedDate(reqDelDate);
                } catch (Exception e) {
                    throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
                }

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


    // STORE RETURN
    private InboundOrder saveStoreReturn(StoreReturn storeReturn) {
        try {
            StoreReturnHeader storeReturnHeader = storeReturn.getStoreReturnHeader();

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
            apiHeader.setCompanyCode(storeReturnHeader.getCompanyCode());
            apiHeader.setLanguageId(storeReturnHeader.getLanguageId());
            apiHeader.setBranchCode(storeReturnHeader.getBranchCode());
            apiHeader.setRefDocumentType("RETURN");
            apiHeader.setInboundOrderTypeId(2L);
            apiHeader.setOrderReceivedOn(new Date());

            Set<InboundOrderLines> orderLines = new HashSet<>();
            for (StoreReturnLine storeReturnLine : storeReturnLines) {
                InboundOrderLines apiLine = new InboundOrderLines();
                apiLine.setLineReference(storeReturnLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(storeReturnLine.getSku());                            // ITM_CODE
                apiLine.setItemText(storeReturnLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setInvoiceNumber(storeReturnLine.getInvoiceNumber());            // INV_NO
                apiLine.setContainerNumber(storeReturnLine.getContainerNumber());        // CONT_NO
                apiLine.setSupplierCode(storeReturnLine.getStoreID());                    // PARTNER_CODE
                apiLine.setSupplierPartNumber(storeReturnLine.getSupplierPartNumber()); // PARTNER_ITM_CODE
                apiLine.setManufacturerName(storeReturnLine.getManufacturerName());        // BRND_NM
                apiLine.setManufacturerPartNo(storeReturnLine.getManufacturerPartNo());    // MFR_PART
                apiLine.setOrderId(apiHeader.getOrderId());

                // EA_DATE
                try {
                    Date reqDelDate = DateUtils.convertStringToDate(storeReturnLine.getExpectedDate());
                    apiLine.setExpectedDate(reqDelDate);
                } catch (Exception e) {
                    throw new InboundOrderRequestException("Date format should be MM-dd-yyyy");
                }

                apiLine.setOrderedQty(storeReturnLine.getExpectedQty());                // ORD_QTY
                apiLine.setUom(storeReturnLine.getUom());                                // ORD_UOM
                apiLine.setItemCaseQty(storeReturnLine.getPackQty());                    // ITM_CASE_QTY
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




    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~OUTBOUND~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // POST SOHeader
    private OutboundOrder saveSO(ShipmentOrder shipmenOrder, boolean isRerun) {
        try {
            SOHeader soHeader = shipmenOrder.getSoHeader();

            // Checking for duplicate RefDocNumber
            OutboundOrder obOrder = orderService.getOBOrderById(soHeader.getTransferOrderNumber());

            if (obOrder != null) {
                throw new OutboundOrderRequestException("TransferOrderNumber is getting duplicated. This order already exists in the System.");
            }

            List<SOLine> soLines = shipmenOrder.getSoLine();

            OutboundOrder apiHeader = new OutboundOrder();
            apiHeader.setOrderId(soHeader.getTransferOrderNumber());
            apiHeader.setBranchCode(soHeader.getBranchCode());
            apiHeader.setCompanyCode(soHeader.getCompanyCode());
            apiHeader.setWarehouseID(soHeader.getWareHouseId());
            apiHeader.setLanguageId(soHeader.getLanguageId());

            IkeyValuePair ikeyValuePair = containerReceiptRepository.getDescription(soHeader.getLanguageId(),
                    soHeader.getCompanyCode(), soHeader.getBranchCode(), soHeader.getWareHouseId());

            if(ikeyValuePair != null){
                apiHeader.setCompanyDescription(ikeyValuePair.getCompanyDesc());
                apiHeader.setPlantDescription(ikeyValuePair.getPlantDesc());
                apiHeader.setWarehouseDescription(ikeyValuePair.getWarehouseDesc());
            }
            apiHeader.setPartnerCode(soHeader.getStoreID());
            apiHeader.setPartnerName(soHeader.getStoreName());
            apiHeader.setRefDocumentNo(soHeader.getTransferOrderNumber());
            apiHeader.setOutboundOrderTypeID(0L);
            apiHeader.setRefDocumentType("SO");                        // Hardcoded value "SO"
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
                apiLine.setLineReference(soLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(soLine.getSku());                            // ITM_CODE
                apiLine.setItemText(soLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setOrderedQty(soLine.getOrderedQty());                    // ORD_QTY
                apiLine.setUom(soLine.getUom());                                // ORD_UOM
                apiLine.setRefField1ForOrderType(soLine.getOrderType());        // ORDER_TYPE
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
            apiHeader.setCompanyCode(salesOrderHeader.getCompanyCode());
            apiHeader.setBranchCode(salesOrderHeader.getBranchCode());
            apiHeader.setLanguageId(salesOrderHeader.getLanguageId());
            apiHeader.setOutboundOrderTypeID(3L);                            // Hardcoded Value "3"
            apiHeader.setRefDocumentType("SaleOrder");                        // Hardcoded value "SaleOrder"
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
                apiLine.setLineReference(soLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(soLine.getSku());                            // ITM_CODE
                apiLine.setItemText(soLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setOrderedQty(soLine.getOrderedQty());                    // ORD_QTY
                apiLine.setUom(soLine.getUom());                                // ORD_UOM
                apiLine.setRefField1ForOrderType(soLine.getOrderType());        // ORDER_TYPE
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
     * @param returnPO
     * @return
     */
    private OutboundOrder saveReturnPO(ReturnPO returnPO) {
        try {
            ReturnPOHeader returnPOHeader = returnPO.getReturnPOHeader();

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
            apiHeader.setCompanyCode(returnPOHeader.getCompanyCode());
            apiHeader.setBranchCode(returnPOHeader.getBranchCode());
            apiHeader.setLanguageId(returnPOHeader.getLanguageId());
            apiHeader.setOutboundOrderTypeID(2L);                            // Hardcoded Value "2"
            apiHeader.setRefDocumentType("RETURNPO");                        // Hardcoded value "RETURNPO"
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
                apiLine.setLineReference(rpoLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(rpoLine.getSku());                            // ITM_CODE
                apiLine.setItemText(rpoLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setOrderedQty(rpoLine.getReturnQty());                    // ORD_QTY
                apiLine.setUom(rpoLine.getUom());                                // ORD_UOM
                apiLine.setRefField1ForOrderType(rpoLine.getOrderType());        // ORDER_TYPE
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
     * @param interWarehouseTransfer
     * @return
     */
    private OutboundOrder saveIWHTransfer(InterWarehouseTransferOut interWarehouseTransfer) {
        try {
            InterWarehouseTransferOutHeader interWarehouseTransferOutHeader =
                    interWarehouseTransfer.getInterWarehouseTransferOutHeader();

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
            apiHeader.setCompanyCode(interWarehouseTransferOutHeader.getCompanyCode());
            apiHeader.setBranchCode(interWarehouseTransferOutHeader.getBranchCode());
            apiHeader.setLanguageId(interWarehouseTransferOutHeader.getLanguageId());
            apiHeader.setOutboundOrderTypeID(1L);                            // Hardcoded Value "1"
            apiHeader.setRefDocumentType("WH2WH");                            // Hardcoded value "WH to WH"
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
                apiLine.setLineReference(iwhTransferLine.getLineReference());            // IB_LINE_NO
                apiLine.setItemCode(iwhTransferLine.getSku());                            // ITM_CODE
                apiLine.setItemText(iwhTransferLine.getSkuDescription());                // ITEM_TEXT
                apiLine.setOrderedQty(iwhTransferLine.getOrderedQty());                    // ORD_QTY
                apiLine.setUom(iwhTransferLine.getUom());                                // ORD_UOM
                apiLine.setRefField1ForOrderType(iwhTransferLine.getOrderType());        // ORDER_TYPE
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
}
