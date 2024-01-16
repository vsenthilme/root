package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.model.periodic.PeriodicHeader;
import com.almailem.ams.api.connector.model.periodic.PeriodicLine;
import com.almailem.ams.api.connector.model.perpetual.PerpetualHeader;
import com.almailem.ams.api.connector.model.perpetual.PerpetualLine;
import com.almailem.ams.api.connector.model.picklist.PickListHeader;
import com.almailem.ams.api.connector.model.picklist.PickListLine;
import com.almailem.ams.api.connector.model.purchasereturn.PurchaseReturnHeader;
import com.almailem.ams.api.connector.model.purchasereturn.PurchaseReturnLine;
import com.almailem.ams.api.connector.model.salesinvoice.SalesInvoice;
import com.almailem.ams.api.connector.model.salesreturn.SalesReturnHeader;
import com.almailem.ams.api.connector.model.salesreturn.SalesReturnLine;
import com.almailem.ams.api.connector.model.stockreceipt.StockReceiptHeader;
import com.almailem.ams.api.connector.model.stockreceipt.StockReceiptLine;
import com.almailem.ams.api.connector.model.supplierinvoice.SupplierInvoiceHeader;
import com.almailem.ams.api.connector.model.supplierinvoice.SupplierInvoiceLine;
import com.almailem.ams.api.connector.model.transferin.TransferInHeader;
import com.almailem.ams.api.connector.model.transferin.TransferInLine;
import com.almailem.ams.api.connector.model.transferout.TransferOutHeader;
import com.almailem.ams.api.connector.model.transferout.TransferOutLine;
import com.almailem.ams.api.connector.model.wms.*;
import com.almailem.ams.api.connector.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    SupplierInvoiceService supplierInvoiceService;

    @Autowired
    StockReceiptService stockReceiptService;

    @Autowired
    SalesReturnService salesReturnService;

    @Autowired
    B2BTransferInService b2BTransferInService;

    @Autowired
    InterWarehouseTransferInService interWarehouseTransferInService;

    @Autowired
    ReturnPOService returnPOService;

    @Autowired
    InterWarehouseTransferOutService interWarehouseTransferOutService;

    @Autowired
    ShipmentOrderService shipmentOrderService;

    @Autowired
    SalesInvoiceService salesInvoiceService;

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    PerpetualService perpetualService;

    @Autowired
    PeriodicService periodicService;

    @Autowired
    IDMasterService idMasterService;

    //-------------------------------------------------------------------------------------------

    @Autowired
    SupplierInvoiceHeaderRepository supplierInvoiceHeaderRepository;

    @Autowired
    StockReceiptHeaderRepository stockReceiptHeaderRepository;

    @Autowired
    SalesReturnHeaderRepository salesReturnHeaderRepository;

    @Autowired
    TransferInHeaderRepository transferInHeaderRepository;

    @Autowired
    PurchaseReturnHeaderRepository purchaseReturnHeaderRepository;

    @Autowired
    TransferOutHeaderRepository transferOutHeaderRepository;

    @Autowired
    PickListHeaderRepository pickListHeaderRepository;

    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;

    @Autowired
    PerpetualHeaderRepository perpetualHeaderRepository;

    @Autowired
    PeriodicHeaderRepository periodicHeaderRepository;

    @Autowired
    StockAdjustmentService stockAdjustmentService;

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepo;

    @Autowired
    IntegrationLogService integrationLogService;


    //-------------------------------------------------------------------------------------------
    List<ASN> inboundList = null;
    List<com.almailem.ams.api.connector.model.wms.StockReceiptHeader> inboundSRList = null;
    List<SaleOrderReturn> inboundSRTList = null;
    List<B2bTransferIn> inboundB2BList = null;
    List<InterWarehouseTransferIn> inboundIWTList = null;
    List<ReturnPO> outboundRPOList = null;
    List<InterWarehouseTransferOut> outboundIWhtList = null;
    List<ShipmentOrder> outboundSOList = null;

    List<SalesOrder> outboundSalesOrderList = null;
    List<com.almailem.ams.api.connector.model.wms.SalesInvoice> outboundSIList = null;

    List<Perpetual> stcPerpetualList = null;
    List<Periodic> stcPeriodicList = null;

    List<StockAdjustment> saList = null;

    //=================================================================================================================
    static CopyOnWriteArrayList<ASN> spList = null;                               // ASN Inbound List
    static CopyOnWriteArrayList<com.almailem.ams.api.connector.model.wms.StockReceiptHeader> spSRList = null;                // StockReceipt Inbound List
    static CopyOnWriteArrayList<SaleOrderReturn> spSRTList = null;                // SaleOrder Inbound List
    static CopyOnWriteArrayList<B2bTransferIn> spB2BList = null;                    // B2B Inbound List
    static CopyOnWriteArrayList<InterWarehouseTransferIn> spIWTList = null;       // InterWarehouse Inbound List

    static CopyOnWriteArrayList<ReturnPO> spRPOList = null;                       // ReturnPO Outbound List
    static CopyOnWriteArrayList<InterWarehouseTransferOut> spIWhtList = null;     // InterWarehouseTransfer Outbound List
    static CopyOnWriteArrayList<ShipmentOrder> spSOList = null;                   // ShipmentOrder Outbound List

    static CopyOnWriteArrayList<SalesOrder> spSalesOrderList = null;                               // ASN Inbound List
    static CopyOnWriteArrayList<com.almailem.ams.api.connector.model.wms.SalesInvoice> spSIList = null;

    static CopyOnWriteArrayList<Perpetual> spPerpetualList = null;                   // Perpetual List
    static CopyOnWriteArrayList<Periodic> spPeriodicList = null;                     // Periodic List

    static CopyOnWriteArrayList<StockAdjustment> spStockAdjustmentList = null;       // Stock Adjustment List

    //==========================================================================================================================
    public WarehouseApiResponse processInboundOrder() throws IllegalAccessException, InvocationTargetException {
        if (inboundList == null || inboundList.isEmpty()) {
            List<SupplierInvoiceHeader> supplierInvoiceHeaders = supplierInvoiceHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On SupplierInvoiceHeaders: " + supplierInvoiceHeaders);
            inboundList = new ArrayList<>();
            ASN asn = new ASN();
            for (SupplierInvoiceHeader dbIBOrder : supplierInvoiceHeaders) {

                ASNHeader asnHeader = new ASNHeader();

                asnHeader.setAsnNumber(dbIBOrder.getSupplierInvoiceNo());
                asnHeader.setCompanyCode(dbIBOrder.getCompanyCode());
                asnHeader.setBranchCode(dbIBOrder.getBranchCode());
                asnHeader.setIsCancelled(dbIBOrder.getIsCancelled());
                asnHeader.setIsCompleted(dbIBOrder.getIsCompleted());
                asnHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                asnHeader.setMiddlewareId(dbIBOrder.getSupplierInvoiceHeaderId());
                asnHeader.setMiddlewareTable("IB_SUPPLIER_INVOICE");

                List<ASNLine> asnLineList = new ArrayList<>();
                for (SupplierInvoiceLine line : dbIBOrder.getSupplierInvoiceLines()) {
                    ASNLine asnLine = new ASNLine();

                    asnLine.setSku(line.getItemCode());
                    asnLine.setSkuDescription(line.getItemDescription());
                    asnLine.setManufacturerCode(line.getManufacturerCode());
                    asnLine.setManufacturerName(line.getManufacturerShortName());
                    asnLine.setSupplierPartNumber(line.getSupplierPartNo());
                    asnLine.setSupplierName(line.getSupplierName());
                    asnLine.setSupplierCode(line.getSupplierCode());
                    asnLine.setPackQty(line.getInvoiceQty());
                    asnLine.setUom(line.getUnitOfMeasure());
                    asnLine.setExpectedQty(line.getInvoiceQty());
                    asnLine.setExpectedDate(String.valueOf(line.getInvoiceDate()));
                    asnLine.setLineReference(line.getLineNoForEachItem());
                    asnLine.setContainerNumber(line.getContainerNo());
                    asnLine.setManufacturerFullName(line.getManufacturerFullName());
                    asnLine.setPurchaseOrderNumber(line.getPurchaseOrderNo());
                    asnLine.setCompanyCode(line.getCompanyCode());
                    asnLine.setBranchCode(line.getBranchCode());

                    asnLine.setReceivedDate(line.getReceivedDate());
                    asnLine.setReceivedQty(line.getReceivedQty());
                    asnLine.setReceivedBy(line.getReceivedBy());
                    asnLine.setIsCancelled(line.getIsCancelled());
                    asnLine.setIsCompleted(line.getIsCompleted());
                    asnLine.setManufacturerFullName(line.getManufacturerFullName());
                    asnLine.setSupplierInvoiceNo(line.getSupplierInvoiceNo());

                    asnLine.setMiddlewareId(line.getSupplierInvoiceLineId());
                    asnLine.setMiddlewareHeaderId(dbIBOrder.getSupplierInvoiceHeaderId());
                    asnLine.setMiddlewareTable("IB_SUPPLIER_INVOICE");

                    asnLineList.add(asnLine);
                }
                asn.setAsnHeader(asnHeader);
                asn.setAsnLine(asnLineList);
                inboundList.add(asn);
            }
            spList = new CopyOnWriteArrayList<ASN>(inboundList);
//            log.info("There is no Supplier Invoice record found to process (sql) ...Waiting..");
        }

        if (inboundList != null) {
            log.info("Latest Supplier Invoice found: " + inboundList);
            for (ASN inbound : spList) {
                try {
                    log.info("Supplier Invoice Number : " + inbound.getAsnHeader().getAsnNumber());

                    WarehouseApiResponse inboundHeader = supplierInvoiceService.postASNV2(inbound);

                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        supplierInvoiceService.updateProcessedInboundOrder(inbound.getAsnHeader().getMiddlewareId(),
                                inbound.getAsnHeader().getCompanyCode(), inbound.getAsnHeader().getBranchCode(),
                                inbound.getAsnHeader().getAsnNumber(), 10L);

                        inboundList.remove(inbound);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //IntegrationLog
                    integrationLogService.createAsnLog(inbound, e.toString());

                    // Updating the Processed Status = 100
                    supplierInvoiceService.updateProcessedInboundOrder(inbound.getAsnHeader().getMiddlewareId(),
                            inbound.getAsnHeader().getCompanyCode(), inbound.getAsnHeader().getBranchCode(),
                            inbound.getAsnHeader().getAsnNumber(), 100L);

//                    supplierInvoiceService.createInboundIntegrationLog(inbound);
                    inboundList.remove(inbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }


    //=====================================================StockReceipt============================================================
    public WarehouseApiResponse processInboundOrderSR() throws IllegalAccessException, InvocationTargetException {
        if (inboundSRList == null || inboundSRList.isEmpty()) {
            List<StockReceiptHeader> stockReceiptHeaders = stockReceiptHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On stockReceiptHeaders: " + stockReceiptHeaders);
            inboundSRList = new ArrayList<>();
            for (StockReceiptHeader dbIBOrder : stockReceiptHeaders) {

                com.almailem.ams.api.connector.model.wms.StockReceiptHeader stockReceiptHeader = new com.almailem.ams.api.connector.model.wms.StockReceiptHeader();

                stockReceiptHeader.setCompanyCode(dbIBOrder.getCompanyCode());
                stockReceiptHeader.setBranchCode(dbIBOrder.getBranchCode());
                stockReceiptHeader.setReceiptNo(dbIBOrder.getReceiptNo());
                stockReceiptHeader.setIsCompleted(dbIBOrder.getIsCompleted());
                stockReceiptHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                stockReceiptHeader.setMiddlewareId(dbIBOrder.getStockReceiptHeaderId());
                stockReceiptHeader.setMiddlewareTable("IB_STOCK_RECEIPT");

                List<com.almailem.ams.api.connector.model.wms.StockReceiptLine> stockReceiptLineList = new ArrayList<>();
                for (StockReceiptLine line : dbIBOrder.getStockReceiptLines()) {
                    com.almailem.ams.api.connector.model.wms.StockReceiptLine stockReceiptLine = new com.almailem.ams.api.connector.model.wms.StockReceiptLine();

                    stockReceiptLine.setItemCode(line.getItemCode());
                    stockReceiptLine.setItemDescription(line.getItemDescription());
                    stockReceiptLine.setManufacturerCode(line.getManufacturerCode());
                    stockReceiptLine.setManufacturerShortName(line.getManufacturerShortName());
                    stockReceiptLine.setSupplierPartNo(line.getSupplierPartNo());
                    stockReceiptLine.setSupplierName(line.getSupplierName());
                    stockReceiptLine.setSupplierCode(line.getSupplierCode());
                    stockReceiptLine.setUnitOfMeasure(line.getUnitOfMeasure());
                    stockReceiptLine.setReceiptQty(line.getReceiptQty());
                    stockReceiptLine.setReceiptDate(line.getReceiptDate());
                    stockReceiptLine.setLineNoForEachItem(line.getLineNoForEachItem());
                    stockReceiptLine.setManufacturerFullName(line.getManufacturerFullName());

                    stockReceiptLine.setReceiptNo(line.getReceiptNo());
                    stockReceiptLine.setManufacturerFullName(line.getManufacturerFullName());
                    stockReceiptLine.setIsCompleted(line.getIsCompleted());

                    stockReceiptLine.setBranchCode(line.getBranchCode());
                    stockReceiptLine.setCompanyCode(line.getCompanyCode());
                    stockReceiptLine.setMiddlewareId(line.getStockReceiptLineId());
                    stockReceiptLine.setMiddlewareHeaderId(dbIBOrder.getStockReceiptHeaderId());
                    stockReceiptLine.setMiddlewareTable("IB_STOCK_RECEIPT");

                    stockReceiptLineList.add(stockReceiptLine);
                }
                stockReceiptHeader.setStockReceiptLines(stockReceiptLineList);
                inboundSRList.add(stockReceiptHeader);
            }
            spSRList = new CopyOnWriteArrayList<com.almailem.ams.api.connector.model.wms.StockReceiptHeader>(inboundSRList);
//            log.info("There is no Stock Receipt record found to process (sql) ...Waiting..");
        }

        if (inboundSRList != null) {
            log.info("Latest Stock Receipt found: " + inboundSRList);
            for (com.almailem.ams.api.connector.model.wms.StockReceiptHeader inbound : spSRList) {
                try {
                    log.info("Stock Receipt Number : " + inbound.getReceiptNo());

                    WarehouseApiResponse inboundHeader = stockReceiptService.postStockReceipt(inbound);

                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        stockReceiptService.updateProcessedInboundOrder(inbound.getMiddlewareId(), inbound.getCompanyCode(),
                                inbound.getBranchCode(), inbound.getReceiptNo(), 10L);

                        inboundSRList.remove(inbound);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    // Updating the Processed Status = 100
                    stockReceiptService.updateProcessedInboundOrder(inbound.getMiddlewareId(), inbound.getCompanyCode(),
                            inbound.getBranchCode(), inbound.getReceiptNo(), 100L);

//                    stockReceiptService.createInboundIntegrationLog(inbound);
                    integrationLogService.createStockReceiptHeaderLog(inbound, e.toString());
                    inboundSRList.remove(inbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    //=====================================================SalesReturn============================================================
    public WarehouseApiResponse processInboundOrderSRT() throws IllegalAccessException, InvocationTargetException {
        if (inboundSRTList == null || inboundSRTList.isEmpty()) {
            List<SalesReturnHeader> salesReturnHeaders = salesReturnHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On salesReturnHeaders: " + salesReturnHeaders);
            inboundSRTList = new ArrayList<>();
            for (SalesReturnHeader dbIBOrder : salesReturnHeaders) {
                SaleOrderReturn saleOrderReturn = new SaleOrderReturn();
                SOReturnHeader salesReturnHeader = new SOReturnHeader();

                salesReturnHeader.setCompanyCode(dbIBOrder.getCompanyCode());
                salesReturnHeader.setBranchCode(dbIBOrder.getBranchCodeOfReceivingWarehouse());
                salesReturnHeader.setTransferOrderNumber(dbIBOrder.getReturnOrderNo());
                salesReturnHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                salesReturnHeader.setIsCompleted(dbIBOrder.getIsCompleted());
                salesReturnHeader.setIsCancelled(dbIBOrder.getIsCancelled());
                salesReturnHeader.setMiddlewareId(dbIBOrder.getSalesReturnHeaderId());
                salesReturnHeader.setMiddlewareTable("IB_SALE_RETURN");

                List<SOReturnLine> salesReturnLineList = new ArrayList<>();
                for (SalesReturnLine line : dbIBOrder.getSalesReturnLines()) {
                    SOReturnLine salesReturnLine = new SOReturnLine();

                    salesReturnLine.setLineReference(line.getLineNoOfEachItem());
                    salesReturnLine.setSku(line.getItemCode());
                    salesReturnLine.setSkuDescription(line.getItemDescription());
                    salesReturnLine.setInvoiceNumber(line.getReferenceInvoiceNo());
                    salesReturnLine.setStoreID(line.getSourceBranchCode());
                    salesReturnLine.setSupplierPartNumber(line.getSupplierPartNo());
                    salesReturnLine.setManufacturerName(line.getManufacturerShortName());
                    salesReturnLine.setExpectedDate(String.valueOf(line.getReturnOrderDate()));
                    salesReturnLine.setExpectedQty(line.getReturnQty());
                    salesReturnLine.setUom(line.getUnitOfMeasure());

                    salesReturnLine.setIsCancelled(line.getIsCancelled());
                    salesReturnLine.setIsCompleted(line.getIsCompleted());
                    salesReturnLine.setSourceBranchCode(line.getSourceBranchCode());

                    if (line.getNoOfPacks() != null) {
                        salesReturnLine.setPackQty(Double.valueOf(line.getNoOfPacks()));
                    }
                    salesReturnLine.setOrigin(line.getCountryOfOrigin());
                    salesReturnLine.setManufacturerCode(line.getManufacturerCode());
                    salesReturnLine.setManufacturerFullName(line.getManufacturerFullName());
                    salesReturnLine.setMiddlewareId(line.getSalesReturnLineId());
                    salesReturnLine.setMiddlewareHeaderId(dbIBOrder.getSalesReturnHeaderId());
                    salesReturnLine.setMiddlewareTable("IB_SALE_RETURN");

                    salesReturnLineList.add(salesReturnLine);
                }
                saleOrderReturn.setSoReturnHeader(salesReturnHeader);
                saleOrderReturn.setSoReturnLine(salesReturnLineList);
                inboundSRTList.add(saleOrderReturn);
            }
            spSRTList = new CopyOnWriteArrayList<SaleOrderReturn>(inboundSRTList);
//            log.info("There is no Sales Return record found to process (sql) ...Waiting..");
        }

        if (inboundSRTList != null) {
            log.info("Latest Sales Return found: " + inboundSRTList);
            for (SaleOrderReturn inbound : spSRTList) {
                try {
                    log.info("Sales Return Number : " + inbound.getSoReturnHeader().getTransferOrderNumber());

                    WarehouseApiResponse inboundHeader = salesReturnService.postSaleOrderReturn(inbound);

                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        salesReturnService.updateProcessedInboundOrder(inbound.getSoReturnHeader().getMiddlewareId(),
                                inbound.getSoReturnHeader().getCompanyCode(), inbound.getSoReturnHeader().getBranchCode(),
                                inbound.getSoReturnHeader().getTransferOrderNumber(), 10L);

                        inboundSRTList.remove(inbound);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesOrderReturnLog(inbound, e.getMessage());

                    // Updating the Processed Status = 100
                    salesReturnService.updateProcessedInboundOrder(inbound.getSoReturnHeader().getMiddlewareId(),
                            inbound.getSoReturnHeader().getCompanyCode(), inbound.getSoReturnHeader().getBranchCode(),
                            inbound.getSoReturnHeader().getTransferOrderNumber(), 100L);

                    inboundSRTList.remove(inbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }


    //=====================================================Interwarehouse============================================================
    public WarehouseApiResponse processInboundOrderIWT() throws IllegalAccessException, InvocationTargetException {

        if (inboundIWTList == null || inboundB2BList == null || inboundB2BList.isEmpty() || inboundIWTList.isEmpty()) {

            List<TransferInHeader> transferInHeaders = transferInHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On transferInHeaders: " + transferInHeaders);
            inboundIWTList = new ArrayList<>();
            inboundB2BList = new ArrayList<>();
            String[] branchcode = new String[]{"115", "125", "212", "222"};

            for (TransferInHeader dbIBOrder : transferInHeaders) {

                boolean sourceBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbIBOrder.getSourceBranchCode()));
                boolean targetBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbIBOrder.getTargetBranchCode()));

                log.info("sourceBranchExist,targetBranchExist: " + sourceBranchExist, targetBranchExist);

                B2bTransferIn b2bTransferIn = new B2bTransferIn();
                List<B2bTransferInLine> b2bTransferInLines = new ArrayList<>();

                InterWarehouseTransferIn interWarehouseTransferIn = new InterWarehouseTransferIn();
                List<InterWarehouseTransferInLine> interWarehouseTransferInLineList = new ArrayList<>();

                if (!sourceBranchExist && !targetBranchExist) {
                    log.info("IB NON WMS to NON WMS: " + sourceBranchExist, targetBranchExist);
                    B2bTransferInHeader b2bTransferInHeader = new B2bTransferInHeader();

                    b2bTransferInHeader.setCompanyCode(dbIBOrder.getTargetCompanyCode());
                    b2bTransferInHeader.setBranchCode(dbIBOrder.getTargetBranchCode());
                    b2bTransferInHeader.setTransferOrderNumber(dbIBOrder.getTransferOrderNo());
                    b2bTransferInHeader.setSourceBranchCode(dbIBOrder.getSourceBranchCode());
                    b2bTransferInHeader.setSourceCompanyCode(dbIBOrder.getSourceCompanyCode());
                    b2bTransferInHeader.setMiddlewareId(dbIBOrder.getTransferInHeaderId());
                    b2bTransferInHeader.setMiddlewareTable("IB_NONWMS_TO_NONWMS");
                    b2bTransferInHeader.setTransferOrderDate(dbIBOrder.getTransferOrderDate());
                    b2bTransferInHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                    b2bTransferInHeader.setIsCompleted(dbIBOrder.getIsCompleted());

                    for (TransferInLine line : dbIBOrder.getTransferInLines()) {

                        B2bTransferInLine b2bTransferInLine = new B2bTransferInLine();

                        b2bTransferInLine.setLineReference(line.getLineNoOfEachItem());
                        b2bTransferInLine.setSku(line.getItemCode());
                        b2bTransferInLine.setSkuDescription(line.getItemDescription());
                        b2bTransferInLine.setManufacturerName(line.getManufacturerShortName());
                        b2bTransferInLine.setExpectedQty(line.getTransferQty());
                        b2bTransferInLine.setUom(line.getUnitOfMeasure());
                        b2bTransferInLine.setManufacturerCode(line.getManufacturerCode());
                        b2bTransferInLine.setManufacturerFullName(line.getManufacturerFullName());
                        b2bTransferInLine.setExpectedDate(String.valueOf(dbIBOrder.getTransferOrderDate()));
                        b2bTransferInLine.setStoreID(dbIBOrder.getTargetBranchCode());
                        b2bTransferInLine.setOrigin(dbIBOrder.getSourceCompanyCode());
                        b2bTransferInLine.setBrand(line.getManufacturerShortName());
                        b2bTransferInLine.setTransferOrderNo(line.getTransferOrderNo());
                        b2bTransferInLine.setIsCompleted(line.getIsCompleted());

                        if (line.getTransferQty() != null) {
                            Double newDouble = new Double(line.getTransferQty());
                            Long tfrQty = newDouble.longValue();

                            b2bTransferInLine.setPackQty(tfrQty);
                        }
                        b2bTransferInLine.setMiddlewareId(line.getTransferInLineId());
                        b2bTransferInLine.setMiddlewareHeaderId(dbIBOrder.getTransferInHeaderId());
                        b2bTransferInLine.setMiddlewareTable("IB_NONWMS_TO_NONWMS");

                        b2bTransferInLines.add(b2bTransferInLine);
                    }

                    b2bTransferIn.setB2bTransferInHeader(b2bTransferInHeader);
                    b2bTransferIn.setB2bTransferLine(b2bTransferInLines);
                    inboundB2BList.add(b2bTransferIn);
                }

                if (!sourceBranchExist && targetBranchExist) {
                    log.info("IB NON WMS to WMS: " + sourceBranchExist, targetBranchExist);
                    B2bTransferInHeader b2bTransferInHeader = new B2bTransferInHeader();

                    b2bTransferInHeader.setCompanyCode(dbIBOrder.getTargetCompanyCode());
                    b2bTransferInHeader.setBranchCode(dbIBOrder.getTargetBranchCode());
                    b2bTransferInHeader.setTransferOrderNumber(dbIBOrder.getTransferOrderNo());
                    b2bTransferInHeader.setSourceBranchCode(dbIBOrder.getSourceBranchCode());
                    b2bTransferInHeader.setSourceCompanyCode(dbIBOrder.getSourceCompanyCode());
                    b2bTransferInHeader.setMiddlewareId(dbIBOrder.getTransferInHeaderId());
                    b2bTransferInHeader.setMiddlewareTable("IB_B2B");
                    b2bTransferInHeader.setTransferOrderDate(dbIBOrder.getTransferOrderDate());
                    b2bTransferInHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                    b2bTransferInHeader.setIsCompleted(dbIBOrder.getIsCompleted());

                    for (TransferInLine line : dbIBOrder.getTransferInLines()) {

                        B2bTransferInLine b2bTransferInLine = new B2bTransferInLine();

                        b2bTransferInLine.setLineReference(line.getLineNoOfEachItem());
                        b2bTransferInLine.setSku(line.getItemCode());
                        b2bTransferInLine.setSkuDescription(line.getItemDescription());
                        b2bTransferInLine.setManufacturerName(line.getManufacturerShortName());
                        b2bTransferInLine.setExpectedQty(line.getTransferQty());
                        b2bTransferInLine.setUom(line.getUnitOfMeasure());
                        b2bTransferInLine.setManufacturerCode(line.getManufacturerCode());
                        b2bTransferInLine.setManufacturerFullName(line.getManufacturerFullName());
                        b2bTransferInLine.setExpectedDate(String.valueOf(dbIBOrder.getTransferOrderDate()));
                        b2bTransferInLine.setStoreID(dbIBOrder.getTargetBranchCode());
                        b2bTransferInLine.setOrigin(dbIBOrder.getSourceCompanyCode());
                        b2bTransferInLine.setBrand(line.getManufacturerShortName());
                        b2bTransferInLine.setTransferOrderNo(line.getTransferOrderNo());
                        b2bTransferInLine.setIsCompleted(line.getIsCompleted());

                        if (line.getTransferQty() != null) {
                            Double newDouble = new Double(line.getTransferQty());
                            Long tfrQty = newDouble.longValue();

                            b2bTransferInLine.setPackQty(tfrQty);
                        }
                        b2bTransferInLine.setMiddlewareId(line.getTransferInLineId());
                        b2bTransferInLine.setMiddlewareHeaderId(dbIBOrder.getTransferInHeaderId());
                        b2bTransferInLine.setMiddlewareTable("IB_B2B");

                        b2bTransferInLines.add(b2bTransferInLine);
                    }

                    b2bTransferIn.setB2bTransferInHeader(b2bTransferInHeader);
                    b2bTransferIn.setB2bTransferLine(b2bTransferInLines);
                    inboundB2BList.add(b2bTransferIn);
                }

                if (sourceBranchExist && targetBranchExist) {
                    log.info("IB WMS to WMS: " + sourceBranchExist, targetBranchExist);
                    InterWarehouseTransferInHeader interWarehouseTransferInHeader = new InterWarehouseTransferInHeader();

                    interWarehouseTransferInHeader.setToCompanyCode(dbIBOrder.getTargetCompanyCode());
                    interWarehouseTransferInHeader.setToBranchCode(dbIBOrder.getTargetBranchCode());
                    interWarehouseTransferInHeader.setSourceCompanyCode(dbIBOrder.getSourceCompanyCode());
                    interWarehouseTransferInHeader.setSourceBranchCode(dbIBOrder.getSourceBranchCode());
                    interWarehouseTransferInHeader.setIsCompleted(dbIBOrder.getIsCompleted());
                    interWarehouseTransferInHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                    interWarehouseTransferInHeader.setTransferOrderNumber(dbIBOrder.getTransferOrderNo());
                    interWarehouseTransferInHeader.setTransferOrderDate(dbIBOrder.getTransferOrderDate());
                    interWarehouseTransferInHeader.setMiddlewareId(dbIBOrder.getTransferInHeaderId());
                    interWarehouseTransferInHeader.setMiddlewareTable("IB_IWT");

                    for (TransferInLine line : dbIBOrder.getTransferInLines()) {

                        InterWarehouseTransferInLine interWarehouseTransferInLine = new InterWarehouseTransferInLine();

                        interWarehouseTransferInLine.setLineReference(line.getLineNoOfEachItem());
                        interWarehouseTransferInLine.setSku(line.getItemCode());
                        interWarehouseTransferInLine.setSkuDescription(line.getItemDescription());
                        interWarehouseTransferInLine.setManufacturerName(line.getManufacturerShortName());
                        interWarehouseTransferInLine.setExpectedQty(line.getTransferQty());
                        interWarehouseTransferInLine.setPackQty(line.getTransferQty());
                        interWarehouseTransferInLine.setUom(line.getUnitOfMeasure());
                        interWarehouseTransferInLine.setManufacturerCode(line.getManufacturerCode());
                        interWarehouseTransferInLine.setManufacturerFullName(line.getManufacturerFullName());
                        interWarehouseTransferInLine.setExpectedDate(String.valueOf(dbIBOrder.getTransferOrderDate()));
                        interWarehouseTransferInLine.setFromBranchCode(dbIBOrder.getSourceBranchCode());
                        interWarehouseTransferInLine.setFromCompanyCode(dbIBOrder.getSourceCompanyCode());
                        interWarehouseTransferInLine.setBrand(line.getManufacturerShortName());
                        interWarehouseTransferInLine.setTransferOrderNo(dbIBOrder.getTransferOrderNo());
                        interWarehouseTransferInLine.setIsCompleted(line.getIsCompleted());

                        interWarehouseTransferInLine.setMiddlewareId(line.getTransferInLineId());
                        interWarehouseTransferInLine.setMiddlewareHeaderId(dbIBOrder.getTransferInHeaderId());
                        interWarehouseTransferInLine.setMiddlewareTable("IB_IWT");

                        interWarehouseTransferInLineList.add(interWarehouseTransferInLine);
                    }
                    interWarehouseTransferIn.setInterWarehouseTransferInHeader(interWarehouseTransferInHeader);
                    interWarehouseTransferIn.setInterWarehouseTransferInLine(interWarehouseTransferInLineList);
                    inboundIWTList.add(interWarehouseTransferIn);
                }
                if (sourceBranchExist && !targetBranchExist) {
                    log.info("IB WMS to NON WMS: " + sourceBranchExist, targetBranchExist);
                    InterWarehouseTransferInHeader interWarehouseTransferInHeader = new InterWarehouseTransferInHeader();

                    interWarehouseTransferInHeader.setToCompanyCode(dbIBOrder.getTargetCompanyCode());
                    interWarehouseTransferInHeader.setToBranchCode(dbIBOrder.getTargetBranchCode());
                    interWarehouseTransferInHeader.setSourceCompanyCode(dbIBOrder.getSourceCompanyCode());
                    interWarehouseTransferInHeader.setSourceBranchCode(dbIBOrder.getSourceBranchCode());
                    interWarehouseTransferInHeader.setIsCompleted(dbIBOrder.getIsCompleted());
                    interWarehouseTransferInHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
                    interWarehouseTransferInHeader.setTransferOrderNumber(dbIBOrder.getTransferOrderNo());
                    interWarehouseTransferInHeader.setTransferOrderDate(dbIBOrder.getTransferOrderDate());
                    interWarehouseTransferInHeader.setMiddlewareId(dbIBOrder.getTransferInHeaderId());
                    interWarehouseTransferInHeader.setMiddlewareTable("IB_WMS_TO_NONWMS");

                    for (TransferInLine line : dbIBOrder.getTransferInLines()) {

                        InterWarehouseTransferInLine interWarehouseTransferInLine = new InterWarehouseTransferInLine();

                        interWarehouseTransferInLine.setLineReference(line.getLineNoOfEachItem());
                        interWarehouseTransferInLine.setSku(line.getItemCode());
                        interWarehouseTransferInLine.setSkuDescription(line.getItemDescription());
                        interWarehouseTransferInLine.setManufacturerName(line.getManufacturerShortName());
                        interWarehouseTransferInLine.setExpectedQty(line.getTransferQty());
                        interWarehouseTransferInLine.setPackQty(line.getTransferQty());
                        interWarehouseTransferInLine.setUom(line.getUnitOfMeasure());
                        interWarehouseTransferInLine.setManufacturerCode(line.getManufacturerCode());
                        interWarehouseTransferInLine.setManufacturerFullName(line.getManufacturerFullName());
                        interWarehouseTransferInLine.setExpectedDate(String.valueOf(dbIBOrder.getTransferOrderDate()));
                        interWarehouseTransferInLine.setFromBranchCode(dbIBOrder.getSourceBranchCode());
                        interWarehouseTransferInLine.setFromCompanyCode(dbIBOrder.getSourceCompanyCode());
                        interWarehouseTransferInLine.setBrand(line.getManufacturerShortName());
                        interWarehouseTransferInLine.setTransferOrderNo(dbIBOrder.getTransferOrderNo());
                        interWarehouseTransferInLine.setIsCompleted(line.getIsCompleted());

                        interWarehouseTransferInLine.setMiddlewareId(line.getTransferInLineId());
                        interWarehouseTransferInLine.setMiddlewareHeaderId(dbIBOrder.getTransferInHeaderId());
                        interWarehouseTransferInLine.setMiddlewareTable("IB_IWT");

                        interWarehouseTransferInLineList.add(interWarehouseTransferInLine);
                    }
                    interWarehouseTransferIn.setInterWarehouseTransferInHeader(interWarehouseTransferInHeader);
                    interWarehouseTransferIn.setInterWarehouseTransferInLine(interWarehouseTransferInLineList);
                    inboundIWTList.add(interWarehouseTransferIn);
                }
            }
            if (inboundB2BList != null) {
                spB2BList = new CopyOnWriteArrayList<B2bTransferIn>(inboundB2BList);
//                log.info("There is no B2B record found to process (sql) ...Waiting..");
            }
            if (inboundIWTList != null) {
                spIWTList = new CopyOnWriteArrayList<InterWarehouseTransferIn>(inboundIWTList);
//                log.info("There is no IWT record found to process (sql) ...Waiting..");
            }
        }

        if (inboundB2BList != null) {
            log.info("Latest B2B Transfer found: " + inboundB2BList);
            for (B2bTransferIn inbound : spB2BList) {
                try {
                    log.info("B2B Transfer Order Number : " + inbound.getB2bTransferInHeader().getTransferOrderNumber());
                    WarehouseApiResponse inboundHeader = b2BTransferInService.postB2BTransferIn(inbound);
                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        b2BTransferInService.updateProcessedInboundOrder(inbound.getB2bTransferInHeader().getMiddlewareId(),
                                inbound.getB2bTransferInHeader().getCompanyCode(), inbound.getB2bTransferInHeader().getBranchCode(),
                                inbound.getB2bTransferInHeader().getTransferOrderNumber(), 10L);
                        inboundB2BList.remove(inbound);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createB2bTransferLog(inbound, e.toString());

                    // Updating the Processed Status = 100
                    b2BTransferInService.updateProcessedInboundOrder(inbound.getB2bTransferInHeader().getMiddlewareId(),
                            inbound.getB2bTransferInHeader().getCompanyCode(), inbound.getB2bTransferInHeader().getBranchCode(),
                            inbound.getB2bTransferInHeader().getTransferOrderNumber(), 100L);

//                    b2BTransferInService.createInboundIntegrationLog(inbound);
                    inboundB2BList.remove(inbound);

                    throw new RuntimeException(e);
                }
            }
        }

        if (inboundIWTList != null) {
            log.info("Latest InterWareHouse Transfer found: " + inboundIWTList);
            for (InterWarehouseTransferIn inbound : spIWTList) {
                try {
                    log.info("InterWarehouse Transfer Order Number : " + inbound.getInterWarehouseTransferInHeader().getTransferOrderNumber());
                    WarehouseApiResponse inboundHeader = interWarehouseTransferInService.postIWTTransferIn(inbound);
                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        interWarehouseTransferInService.updateProcessedInboundOrder(inbound.getInterWarehouseTransferInHeader().getMiddlewareId(),
                                inbound.getInterWarehouseTransferInHeader().getToCompanyCode(), inbound.getInterWarehouseTransferInHeader().getToBranchCode(),
                                inbound.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 10L);

                        inboundIWTList.remove(inbound);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createInterWarehouseTransferInLog(inbound, e.toString());

                    // Updating the Processed Status = 100
                    interWarehouseTransferInService.updateProcessedInboundOrder(inbound.getInterWarehouseTransferInHeader().getMiddlewareId(),
                            inbound.getInterWarehouseTransferInHeader().getToCompanyCode(), inbound.getInterWarehouseTransferInHeader().getToBranchCode(),
                            inbound.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 100L);

//                    interWarehouseTransferInV2Service.createInboundIntegrationLog(inbound);
                    inboundIWTList.remove(inbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

//    public WarehouseApiResponse processInboundOrderIWT() throws IllegalAccessException, InvocationTargetException {
//
//        if (inboundIWTList == null || inboundB2BList == null || inboundB2BList.isEmpty() || inboundIWTList.isEmpty()) {
//
//            List<TransferInHeader> transferInHeaders = transferInHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
//
//            inboundIWTList = new ArrayList<>();
//            inboundB2BList = new ArrayList<>();
//            String[] branchcode = new String[]{"115", "125", "212", "222"};
//
//            for (TransferInHeader dbIBOrder : transferInHeaders) {
//
//                boolean sourceBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbIBOrder.getSourceBranchCode()));
//                boolean targetBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbIBOrder.getTargetBranchCode()));
//
//                log.info("sourceBranchExist,targetBranchExist: " + sourceBranchExist, targetBranchExist);
//
//                B2bTransferIn b2bTransferIn = new B2bTransferIn();
//                List<B2bTransferInLine> b2bTransferInLines = new ArrayList<>();
//
//                InterWarehouseTransferIn interWarehouseTransferIn = new InterWarehouseTransferIn();
//                List<InterWarehouseTransferInLine> interWarehouseTransferInLineList = new ArrayList<>();
//
////                if (!sourceBranchExist && !targetBranchExist) {
//                log.info("IB : " + sourceBranchExist, targetBranchExist);
//                B2bTransferInHeader b2bTransferInHeader = new B2bTransferInHeader();
//
//                b2bTransferInHeader.setCompanyCode(dbIBOrder.getTargetCompanyCode());
//                b2bTransferInHeader.setBranchCode(dbIBOrder.getTargetBranchCode());
//                b2bTransferInHeader.setTransferOrderNumber(dbIBOrder.getTransferOrderNo());
//                b2bTransferInHeader.setSourceBranchCode(dbIBOrder.getSourceBranchCode());
//                b2bTransferInHeader.setSourceCompanyCode(dbIBOrder.getSourceCompanyCode());
//                b2bTransferInHeader.setMiddlewareId(dbIBOrder.getTransferInHeaderId());
//                b2bTransferInHeader.setMiddlewareTable("IB_NONWMS_TO_NONWMS");
//                b2bTransferInHeader.setTransferOrderDate(dbIBOrder.getTransferOrderDate());
//                b2bTransferInHeader.setUpdatedOn(dbIBOrder.getUpdatedOn());
//                b2bTransferInHeader.setIsCompleted(dbIBOrder.getIsCompleted());
//
//                for (TransferInLine line : dbIBOrder.getTransferInLines()) {
//
//                    B2bTransferInLine b2bTransferInLine = new B2bTransferInLine();
//
//                    b2bTransferInLine.setLineReference(line.getLineNoOfEachItem());
//                    b2bTransferInLine.setSku(line.getItemCode());
//                    b2bTransferInLine.setSkuDescription(line.getItemDescription());
//                    b2bTransferInLine.setManufacturerName(line.getManufacturerShortName());
//                    b2bTransferInLine.setExpectedQty(line.getTransferQty());
//                    b2bTransferInLine.setUom(line.getUnitOfMeasure());
//                    b2bTransferInLine.setManufacturerCode(line.getManufacturerCode());
//                    b2bTransferInLine.setManufacturerFullName(line.getManufacturerFullName());
//                    b2bTransferInLine.setExpectedDate(String.valueOf(dbIBOrder.getTransferOrderDate()));
//                    b2bTransferInLine.setStoreID(dbIBOrder.getTargetBranchCode());
//                    b2bTransferInLine.setOrigin(dbIBOrder.getSourceCompanyCode());
//                    b2bTransferInLine.setBrand(line.getManufacturerShortName());
//                    b2bTransferInLine.setTransferOrderNo(line.getTransferOrderNo());
//                    b2bTransferInLine.setIsCompleted(line.getIsCompleted());
//
//                    if (line.getTransferQty() != null) {
//                        Double newDouble = new Double(line.getTransferQty());
//                        Long tfrQty = newDouble.longValue();
//                        ;
//                        b2bTransferInLine.setPackQty(tfrQty);
//                    }
//                    b2bTransferInLine.setMiddlewareId(line.getTransferInLineId());
//                    b2bTransferInLine.setMiddlewareHeaderId(dbIBOrder.getTransferInHeaderId());
//                    b2bTransferInLine.setMiddlewareTable("IB_NONWMS_TO_NONWMS");
//
//                    b2bTransferInLines.add(b2bTransferInLine);
//                }
//
//                b2bTransferIn.setB2bTransferInHeader(b2bTransferInHeader);
//                b2bTransferIn.setB2bTransferLine(b2bTransferInLines);
//                inboundB2BList.add(b2bTransferIn);
////                }
//
//
//                if (inboundB2BList != null) {
//                    spB2BList = new CopyOnWriteArrayList<B2bTransferIn>(inboundB2BList);
//                    log.info("There is no B2B record found to process (sql) ...Waiting..");
//                }
//
//            }
//
//            if (inboundB2BList != null) {
//                log.info("Latest B2B Transfer found: " + inboundB2BList);
//                for (B2bTransferIn inbound : spB2BList) {
//                    try {
//                        log.info("B2B Transfer Order Number : " + inbound.getB2bTransferInHeader().getTransferOrderNumber());
//                        WarehouseApiResponse inboundHeader = b2BTransferInService.postB2BTransferIn(inbound);
//                        if (inboundHeader != null) {
//                            // Updating the Processed Status
//                            b2BTransferInService.updateProcessedInboundOrder(inbound.getB2bTransferInHeader().getTransferOrderNumber());
//                            inboundB2BList.remove(inbound);
//                            return inboundHeader;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        log.error("Error on inbound processing : " + e.toString());
//
//                        //Integration Log
//                        integrationLogService.createB2bTransferLog(inbound, e.toString());
//
//                        // Updating the Processed Status
//                        b2BTransferInService.updateProcessedInboundOrder(inbound.getB2bTransferInHeader().getTransferOrderNumber());
////                    b2BTransferInService.createInboundIntegrationLog(inbound);
//                        inboundB2BList.remove(inbound);
//
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//        return null;
//    }

    //===========================================Outbound==============================================================
    //===========================================Purchase_Return=======================================================
    public WarehouseApiResponse processOutboundOrderRPO() throws IllegalAccessException, InvocationTargetException {
        if (outboundRPOList == null || outboundRPOList.isEmpty()) {
            List<PurchaseReturnHeader> purchaseReturns = purchaseReturnHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On purchaseReturns: " + purchaseReturns);
            outboundRPOList = new ArrayList<>();
            ReturnPO returnPO = new ReturnPO();
            for (PurchaseReturnHeader dbObOrder : purchaseReturns) {
                ReturnPOHeader returnPOHeader = new ReturnPOHeader();

                returnPOHeader.setCompanyCode(dbObOrder.getCompanyCode());
                returnPOHeader.setBranchCode(dbObOrder.getBranchCode());
                returnPOHeader.setPoNumber(dbObOrder.getReturnOrderNo());
                returnPOHeader.setStoreID(dbObOrder.getBranchCode());
                returnPOHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getReturnOrderDate()));
                returnPOHeader.setIsCancelled(dbObOrder.getIsCancelled());
                returnPOHeader.setIsCompleted(dbObOrder.getIsCompleted());
                returnPOHeader.setUpdatedOn(dbObOrder.getUpdatedOn());
                returnPOHeader.setMiddlewareId(dbObOrder.getPurchaseReturnHeaderId());
                returnPOHeader.setMiddlewareTable("OB_PURCHASE_RETURN_HEADER");

                List<ReturnPOLine> returnPOLineList = new ArrayList<>();
                for (PurchaseReturnLine line : dbObOrder.getPurchaseReturnLines()) {
                    ReturnPOLine returnPOLine = new ReturnPOLine();

                    returnPOLine.setReturnOrderNo(line.getReturnOrderNo());
                    returnPOLine.setLineReference(line.getLineNoOfEachItemCode());
                    returnPOLine.setSku(line.getItemCode());
                    returnPOLine.setSkuDescription(line.getItemDescription());
                    returnPOLine.setReturnQty(line.getReturnOrderQty());
                    returnPOLine.setExpectedQty(line.getReturnOrderQty());
                    returnPOLine.setUom(line.getUnitOfMeasure());
                    returnPOLine.setManufacturerCode(line.getManufacturerCode());
                    returnPOLine.setManufacturerName(line.getManufacturerShortName());
                    returnPOLine.setBrand(line.getManufacturerFullName());
                    returnPOLine.setSupplierInvoiceNo(line.getSupplierInvoiceNo());
                    returnPOLine.setIsCancelled(line.getIsCancelled());
                    returnPOLine.setIsCompleted(line.getIsCompleted());
                    returnPOLine.setMiddlewareId(line.getPurchaseReturnLineId());
                    returnPOLine.setMiddlewareHeaderId(dbObOrder.getPurchaseReturnHeaderId());
                    returnPOLine.setMiddlewareTable("OB_PURCHASE_RETURN_LINE");
                    returnPOLineList.add(returnPOLine);
                }
                returnPO.setReturnPOLine(returnPOLineList);
                returnPO.setReturnPOHeader(returnPOHeader);
                outboundRPOList.add(returnPO);
            }
            spRPOList = new CopyOnWriteArrayList<ReturnPO>(outboundRPOList);
//            log.info("There is no Purchase Return record found to process (sql) ...Waiting..");
        }

        if (outboundRPOList != null) {
            log.info("Latest Purchase Return found: " + outboundRPOList);
            for (ReturnPO outbound : spRPOList) {
                try {
                    log.info("Purchase Return Number: " + outbound.getReturnPOHeader().getPoNumber());
                    WarehouseApiResponse response = returnPOService.postReturnPOV2(outbound);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        returnPOService.updateProcessedOutboundOrder(outbound.getReturnPOHeader().getMiddlewareId(),
                                outbound.getReturnPOHeader().getCompanyCode(), outbound.getReturnPOHeader().getBranchCode(),
                                outbound.getReturnPOHeader().getPoNumber(), 10L);

                        outboundRPOList.remove(outbound);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createReturnPoLog(outbound, e.toString());

                    //Updating the Processed Status = 100
                    returnPOService.updateProcessedOutboundOrder(outbound.getReturnPOHeader().getMiddlewareId(),
                            outbound.getReturnPOHeader().getCompanyCode(), outbound.getReturnPOHeader().getBranchCode(),
                            outbound.getReturnPOHeader().getPoNumber(), 100L);

                    outboundRPOList.remove(outbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    //------------------------------------InterWarehouse & ShipmentOrder---------------------------------------------
    public WarehouseApiResponse processOutboundOrderIWT() throws IllegalAccessException, InvocationTargetException {

        if (outboundIWhtList == null || outboundSOList == null || outboundSOList.isEmpty() || outboundIWhtList.isEmpty()) {

            List<TransferOutHeader> transferOuts = transferOutHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOnDesc(0L);
            log.info("TransferOut / Shipment Order Found: " + transferOuts);
            outboundIWhtList = new ArrayList<>();
            outboundSOList = new ArrayList<>();
            String[] branchcode = new String[]{"115", "125", "212", "222"};

            for (TransferOutHeader dbObOrder : transferOuts) {

                boolean sourceBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbObOrder.getSourceBranchCode()));
                boolean targetBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbObOrder.getTargetBranchCode()));

                log.info("sourceBranchExist: " + sourceBranchExist);
                log.info("targetBranchExist: " + targetBranchExist);

                ShipmentOrder shipmentOrder = new ShipmentOrder();
                List<SOLine> soV2List = new ArrayList<>();

                InterWarehouseTransferOut iWhTransferOut = new InterWarehouseTransferOut();
                List<InterWarehouseTransferOutLine> iWhtOutLineList = new ArrayList<>();

                if (sourceBranchExist && !targetBranchExist) {
                    log.info("OB WMS to NON WMS: " + sourceBranchExist, targetBranchExist);
                    log.info("Shipment Order: " + dbObOrder);
                    SOHeader soHeader = new SOHeader();

                    soHeader.setCompanyCode(dbObOrder.getSourceCompanyCode());
                    soHeader.setBranchCode(dbObOrder.getSourceBranchCode());
                    soHeader.setTransferOrderNumber(dbObOrder.getTransferOrderNumber());
                    soHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getTransferOrderDate()));
                    soHeader.setStoreID(dbObOrder.getSourceBranchCode());
                    soHeader.setTargetCompanyCode(dbObOrder.getTargetCompanyCode());
                    soHeader.setTargetBranchCode(dbObOrder.getTargetBranchCode());
                    soHeader.setOrderType(dbObOrder.getFulfilmentMethod());
                    soHeader.setMiddlewareId(dbObOrder.getTransferOutHeaderId());
                    soHeader.setMiddlewareTable("OB_SHIPMENT_ORDER_HEADER");

                    for (TransferOutLine line : dbObOrder.getTransferOutLines()) {
                        log.info("Shipment Order Lines: " + dbObOrder.getTransferOutLines());
                        SOLine soLine = new SOLine();

                        soLine.setTransferOrderNumber(line.getTransferOrderNumber());
                        soLine.setLineReference(line.getLineNumberOfEachItem());
                        soLine.setSku(line.getItemCode());
                        soLine.setSkuDescription(line.getItemDescription());
                        soLine.setOrderedQty(line.getTransferOrderQty());
                        soLine.setExpectedQty(line.getTransferOrderQty());
                        soLine.setUom(line.getUnitOfMeasure());
                        soLine.setManufacturerCode(line.getManufacturerCode());
                        soLine.setManufacturerName(line.getManufacturerShortName());
                        soLine.setFromCompanyCode(dbObOrder.getSourceCompanyCode());
                        soLine.setOrderType(dbObOrder.getFulfilmentMethod());
                        soLine.setManufacturerFullName(line.getManufacturerFullName());
                        soLine.setMiddlewareId(line.getTransferOutLineId());
                        soLine.setMiddlewareHeaderId(dbObOrder.getTransferOutHeaderId());
                        soLine.setMiddlewareTable("OB_SHIPMENT_ORDER_LINE");
                        soV2List.add(soLine);
                    }
                    shipmentOrder.setSoHeader(soHeader);
                    shipmentOrder.setSoLine(soV2List);
                    outboundSOList.add(shipmentOrder);
                    log.info("outboundSOList: " + outboundSOList);
                }

                if (!sourceBranchExist && targetBranchExist) {
                    log.info("OB NON WMS to WMS: " + sourceBranchExist, targetBranchExist);
                    log.info("TransferOut Order: " + dbObOrder);
                    InterWarehouseTransferOutHeader iWhtOutHeader = new InterWarehouseTransferOutHeader();

                    iWhtOutHeader.setFromCompanyCode(dbObOrder.getSourceCompanyCode());
                    iWhtOutHeader.setToCompanyCode(dbObOrder.getTargetCompanyCode());
                    iWhtOutHeader.setTransferOrderNumber(dbObOrder.getTransferOrderNumber());
                    iWhtOutHeader.setFromBranchCode(dbObOrder.getSourceBranchCode());
                    iWhtOutHeader.setToBranchCode(dbObOrder.getTargetBranchCode());
                    iWhtOutHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getTransferOrderDate()));
                    iWhtOutHeader.setOrderType(dbObOrder.getFulfilmentMethod());
                    iWhtOutHeader.setMiddlewareId(dbObOrder.getTransferOutHeaderId());
                    iWhtOutHeader.setMiddlewareTable("OB_IWHTRANSFER_OUT_HEADER");

                    for (TransferOutLine line : dbObOrder.getTransferOutLines()) {
                        log.info("TrnasferOut Order Lines: " + dbObOrder.getTransferOutLines());
                        InterWarehouseTransferOutLine iWhtOutLine = new InterWarehouseTransferOutLine();

                        iWhtOutLine.setTransferOrderNumber(line.getTransferOrderNumber());
                        iWhtOutLine.setLineReference(line.getLineNumberOfEachItem());
                        iWhtOutLine.setSku(line.getItemCode());
                        iWhtOutLine.setSkuDescription(line.getItemDescription());
                        iWhtOutLine.setOrderedQty(line.getTransferOrderQty());
                        iWhtOutLine.setUom(line.getUnitOfMeasure());
                        iWhtOutLine.setManufacturerCode(line.getManufacturerCode());
                        iWhtOutLine.setManufacturerName(line.getManufacturerShortName());
                        iWhtOutLine.setOrderType(dbObOrder.getFulfilmentMethod());
                        iWhtOutLine.setManufacturerFullName(line.getManufacturerFullName());
                        iWhtOutLine.setMiddlewareId(line.getTransferOutLineId());
                        iWhtOutLine.setMiddlewareHeaderId(line.getTransferOutHeaderId());
                        iWhtOutLine.setMiddlewareTable("OB_IWHTRANSFER_OUT_LINE");
                        iWhtOutLineList.add(iWhtOutLine);
                    }
                    iWhTransferOut.setInterWarehouseTransferOutHeader(iWhtOutHeader);
                    iWhTransferOut.setInterWarehouseTransferOutLine(iWhtOutLineList);
                    outboundIWhtList.add(iWhTransferOut);
                    log.info("outboundIWhtList: " + outboundIWhtList);
                }

                if (sourceBranchExist && targetBranchExist) {
                    log.info("OB WMS to WMS: " + sourceBranchExist, targetBranchExist);
                    log.info("TransferOut Order: " + dbObOrder);
                    InterWarehouseTransferOutHeader iWhtOutHeader = new InterWarehouseTransferOutHeader();

                    iWhtOutHeader.setFromCompanyCode(dbObOrder.getSourceCompanyCode());
                    iWhtOutHeader.setToCompanyCode(dbObOrder.getTargetCompanyCode());
                    iWhtOutHeader.setTransferOrderNumber(dbObOrder.getTransferOrderNumber());
                    iWhtOutHeader.setFromBranchCode(dbObOrder.getSourceBranchCode());
                    iWhtOutHeader.setToBranchCode(dbObOrder.getTargetBranchCode());
                    iWhtOutHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getTransferOrderDate()));
                    iWhtOutHeader.setOrderType(dbObOrder.getFulfilmentMethod());
                    iWhtOutHeader.setMiddlewareId(dbObOrder.getTransferOutHeaderId());
                    iWhtOutHeader.setMiddlewareTable("OB_WMS_TO_WMS");

                    for (TransferOutLine line : dbObOrder.getTransferOutLines()) {
                        log.info("TrnasferOut Order Lines: " + dbObOrder.getTransferOutLines());
                        InterWarehouseTransferOutLine iWhtOutLine = new InterWarehouseTransferOutLine();

                        iWhtOutLine.setTransferOrderNumber(line.getTransferOrderNumber());
                        iWhtOutLine.setLineReference(line.getLineNumberOfEachItem());
                        iWhtOutLine.setSku(line.getItemCode());
                        iWhtOutLine.setSkuDescription(line.getItemDescription());
                        iWhtOutLine.setOrderedQty(line.getTransferOrderQty());
                        iWhtOutLine.setUom(line.getUnitOfMeasure());
                        iWhtOutLine.setManufacturerCode(line.getManufacturerCode());
                        iWhtOutLine.setManufacturerName(line.getManufacturerShortName());
                        iWhtOutLine.setOrderType(dbObOrder.getFulfilmentMethod());
                        iWhtOutLine.setManufacturerFullName(line.getManufacturerFullName());
                        iWhtOutLine.setMiddlewareId(line.getTransferOutLineId());
                        iWhtOutLine.setMiddlewareHeaderId(line.getTransferOutHeaderId());
                        iWhtOutLine.setMiddlewareTable("OB_IWHTRANSFER_OUT_LINE");
                        iWhtOutLineList.add(iWhtOutLine);
                    }
                    iWhTransferOut.setInterWarehouseTransferOutHeader(iWhtOutHeader);
                    iWhTransferOut.setInterWarehouseTransferOutLine(iWhtOutLineList);
                    outboundIWhtList.add(iWhTransferOut);
                    log.info("outboundIWhtList: " + outboundIWhtList);
                }

                if (!sourceBranchExist && !targetBranchExist) {
                    log.info("OB NON WMS to NON WMS: " + sourceBranchExist, targetBranchExist);
                    log.info("TransferOut Order: " + dbObOrder);
                    InterWarehouseTransferOutHeader iWhtOutHeader = new InterWarehouseTransferOutHeader();

                    iWhtOutHeader.setFromCompanyCode(dbObOrder.getSourceCompanyCode());
                    iWhtOutHeader.setToCompanyCode(dbObOrder.getTargetCompanyCode());
                    iWhtOutHeader.setTransferOrderNumber(dbObOrder.getTransferOrderNumber());
                    iWhtOutHeader.setFromBranchCode(dbObOrder.getSourceBranchCode());
                    iWhtOutHeader.setToBranchCode(dbObOrder.getTargetBranchCode());
                    iWhtOutHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getTransferOrderDate()));
                    iWhtOutHeader.setOrderType(dbObOrder.getFulfilmentMethod());
                    iWhtOutHeader.setMiddlewareId(dbObOrder.getTransferOutHeaderId());
                    iWhtOutHeader.setMiddlewareTable("OB_NONWMS_TO_NONWMS");

                    for (TransferOutLine line : dbObOrder.getTransferOutLines()) {
                        log.info("TransferOut Order Lines: " + dbObOrder.getTransferOutLines());
                        InterWarehouseTransferOutLine iWhtOutLine = new InterWarehouseTransferOutLine();

                        iWhtOutLine.setTransferOrderNumber(line.getTransferOrderNumber());
                        iWhtOutLine.setLineReference(line.getLineNumberOfEachItem());
                        iWhtOutLine.setSku(line.getItemCode());
                        iWhtOutLine.setSkuDescription(line.getItemDescription());
                        iWhtOutLine.setOrderedQty(line.getTransferOrderQty());
                        iWhtOutLine.setUom(line.getUnitOfMeasure());
                        iWhtOutLine.setManufacturerCode(line.getManufacturerCode());
                        iWhtOutLine.setManufacturerName(line.getManufacturerShortName());
                        iWhtOutLine.setOrderType(dbObOrder.getFulfilmentMethod());
                        iWhtOutLine.setManufacturerFullName(line.getManufacturerFullName());
                        iWhtOutLine.setMiddlewareId(line.getTransferOutLineId());
                        iWhtOutLine.setMiddlewareHeaderId(line.getTransferOutHeaderId());
                        iWhtOutLine.setMiddlewareTable("OB_IWHTRANSFER_OUT_LINE");
                        iWhtOutLineList.add(iWhtOutLine);
                    }
                    iWhTransferOut.setInterWarehouseTransferOutHeader(iWhtOutHeader);
                    iWhTransferOut.setInterWarehouseTransferOutLine(iWhtOutLineList);
                    outboundIWhtList.add(iWhTransferOut);
                    log.info("outboundIWhtList: OB_NONWMS_TO_NONWMS " + outboundIWhtList);
                }
            }
            if (outboundSOList != null) {
                spSOList = new CopyOnWriteArrayList<ShipmentOrder>(outboundSOList);
//                log.info("There is no IWhTOut record found to process (sql) ...Waiting..");
            }
            if (outboundIWhtList != null) {
                spIWhtList = new CopyOnWriteArrayList<InterWarehouseTransferOut>(outboundIWhtList);
//                log.info("There is no SO record found to process (sql) ...Waiting..");
            }
        }

        if (outboundSOList != null) {
            log.info("Latest Shipment Order found: " + outboundSOList);
            for (ShipmentOrder outbound : spSOList) {
                try {
                    log.info("SO Transfer Order Number: " + outbound.getSoHeader().getTransferOrderNumber());
                    WarehouseApiResponse response = shipmentOrderService.postShipmentOrder(outbound);
                    if (response != null) {
                        // Updating the Processed Status = 10
                        shipmentOrderService.updateProcessedOutboundOrder(outbound.getSoHeader().getMiddlewareId(), outbound.getSoHeader().getCompanyCode(),
                                outbound.getSoHeader().getBranchCode(), outbound.getSoHeader().getTransferOrderNumber(), 10L);

                        outboundSOList.remove(outbound);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createShipmentOrderLog(outbound, e.toString());

                    // Updating the Processed Status = 100
                    shipmentOrderService.updateProcessedOutboundOrder(outbound.getSoHeader().getMiddlewareId(), outbound.getSoHeader().getCompanyCode(),
                            outbound.getSoHeader().getBranchCode(), outbound.getSoHeader().getTransferOrderNumber(), 100L);

                    outboundSOList.remove(outbound);
                    throw new RuntimeException(e);
                }
            }
        }

        if (outboundIWhtList != null) {
            log.info("Latest Transfer Out found: " + outboundIWhtList);
            for (InterWarehouseTransferOut outbound : spIWhtList) {
                try {
                    log.info("IWT Transfer Out Number: " + outbound.getInterWarehouseTransferOutHeader().getTransferOrderNumber());
                    WarehouseApiResponse response = interWarehouseTransferOutService.postIWhTransferOutV2(outbound);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        interWarehouseTransferOutService.updateProcessedOutboundOrder(outbound.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                outbound.getInterWarehouseTransferOutHeader().getFromCompanyCode(), outbound.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                outbound.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 10L);

                        outboundIWhtList.remove(outbound);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createInterWarehouseTransferOutLog(outbound, e.toString());

                    //Updating the Processed Status = 100
                    interWarehouseTransferOutService.updateProcessedOutboundOrder(outbound.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                            outbound.getInterWarehouseTransferOutHeader().getFromCompanyCode(), outbound.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                            outbound.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 100L);

                    outboundIWhtList.remove(outbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

//    public WarehouseApiResponse processOutboundOrderIWT() throws IllegalAccessException, InvocationTargetException {
//
//        if (outboundIWhtList == null || outboundSOList == null || outboundSOList.isEmpty() || outboundIWhtList.isEmpty()) {
//
//            List<TransferOutHeader> transferOuts = transferOutHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOnDesc(0L);
//            log.info("TransferOut / Shipment Order Found: " + transferOuts);
//            outboundIWhtList = new ArrayList<>();
//            outboundSOList = new ArrayList<>();
//            String[] branchcode = new String[]{"115", "125", "212", "222"};
//
//            for (TransferOutHeader dbObOrder : transferOuts) {
//
//                boolean sourceBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbObOrder.getSourceBranchCode()));
//                boolean targetBranchExist = Arrays.stream(branchcode).anyMatch(n -> n.equalsIgnoreCase(dbObOrder.getTargetBranchCode()));
//
//                log.info("sourceBranchExist: " + sourceBranchExist);
//                log.info("targetBranchExist: " + targetBranchExist);
//
//                ShipmentOrder shipmentOrder = new ShipmentOrder();
//                List<SOLine> soV2List = new ArrayList<>();
//
//                InterWarehouseTransferOut iWhTransferOut = new InterWarehouseTransferOut();
//                List<InterWarehouseTransferOutLine> iWhtOutLineList = new ArrayList<>();
//
////                if (sourceBranchExist && !targetBranchExist) {
////                    log.info("OB WMS to NON WMS: " + sourceBranchExist, targetBranchExist);
//                log.info("Shipment Order: " + dbObOrder);
//                SOHeader soHeader = new SOHeader();
//
//                soHeader.setCompanyCode(dbObOrder.getSourceCompanyCode());
//                soHeader.setBranchCode(dbObOrder.getSourceBranchCode());
//                soHeader.setTransferOrderNumber(dbObOrder.getTransferOrderNumber());
//                soHeader.setRequiredDeliveryDate(String.valueOf(dbObOrder.getTransferOrderDate()));
//                soHeader.setStoreID(dbObOrder.getSourceBranchCode());
//                soHeader.setTargetCompanyCode(dbObOrder.getTargetCompanyCode());
//                soHeader.setTargetBranchCode(dbObOrder.getTargetBranchCode());
//                soHeader.setOrderType(dbObOrder.getFulfilmentMethod());
//                soHeader.setMiddlewareId(dbObOrder.getTransferOutHeaderId());
//                soHeader.setMiddlewareTable("OB_SHIPMENT_ORDER_HEADER");
//
//                for (TransferOutLine line : dbObOrder.getTransferOutLines()) {
//                    log.info("Shipment Order Lines: " + dbObOrder.getTransferOutLines());
//                    SOLine soLine = new SOLine();
//
//                    soLine.setTransferOrderNumber(line.getTransferOrderNumber());
//                    soLine.setLineReference(line.getLineNumberOfEachItem());
//                    soLine.setSku(line.getItemCode());
//                    soLine.setSkuDescription(line.getItemDescription());
//                    soLine.setOrderedQty(line.getTransferOrderQty());
//                    soLine.setExpectedQty(line.getTransferOrderQty());
//                    soLine.setUom(line.getUnitOfMeasure());
//                    soLine.setManufacturerCode(line.getManufacturerCode());
//                    soLine.setManufacturerName(line.getManufacturerShortName());
//                    soLine.setFromCompanyCode(dbObOrder.getSourceCompanyCode());
//                    soLine.setOrderType(dbObOrder.getFulfilmentMethod());
//                    soLine.setManufacturerFullName(line.getManufacturerFullName());
//                    soLine.setMiddlewareId(line.getTransferOutLineId());
//                    soLine.setMiddlewareHeaderId(dbObOrder.getTransferOutHeaderId());
//                    soLine.setMiddlewareTable("OB_SHIPMENT_ORDER_LINE");
//                    soV2List.add(soLine);
//                }
//                shipmentOrder.setSoHeader(soHeader);
//                shipmentOrder.setSoLine(soV2List);
//                outboundSOList.add(shipmentOrder);
//                log.info("outboundSOList: " + outboundSOList);
////                }
//
//                if (outboundSOList != null) {
//                    spSOList = new CopyOnWriteArrayList<ShipmentOrder>(outboundSOList);
//                    log.info("There is no IWhTOut record found to process (sql) ...Waiting..");
//                }
////                if (outboundIWhtList != null) {
////                    spIWhtList = new CopyOnWriteArrayList<InterWarehouseTransferOut>(outboundIWhtList);
////                    log.info("There is no SO record found to process (sql) ...Waiting..");
////                }
//            }
//
//            if (outboundSOList != null) {
//                log.info("Latest Shipment Order found: " + outboundSOList);
//                for (ShipmentOrder outbound : spSOList) {
//                    try {
//                        log.info("SO Transfer Order Number: " + outbound.getSoHeader().getTransferOrderNumber());
//                        WarehouseApiResponse response = shipmentOrderService.postShipmentOrder(outbound);
//                        if (response != null) {
//                            // Updating the Processed Status
//                            shipmentOrderService.updateProcessedOutboundOrder(outbound.getSoHeader().getTransferOrderNumber());
//                            outboundSOList.remove(outbound);
//                            return response;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        log.error("Error on outbound processing : " + e.toString());
//
//                        //Integration Log
//                        integrationLogService.createShipmentOrderLog(outbound, e.toString());
//
//                        //Updating the Processed Status
//                        shipmentOrderService.updateProcessedOutboundOrder(outbound.getSoHeader().getTransferOrderNumber());
//                        outboundSOList.remove(outbound);
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public WarehouseApiResponse processOutboundOrderPL() throws IllegalAccessException, InvocationTargetException {
        if (outboundSalesOrderList == null || outboundSalesOrderList.isEmpty()) {
            List<PickListHeader> pickListHeaders = pickListHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On pickListHeaders: " + pickListHeaders);
            outboundSalesOrderList = new ArrayList<>();
            SalesOrder salesOrder = new SalesOrder();
            for (PickListHeader dbOBOrder : pickListHeaders) {

                SalesOrderHeader salesOrderHeader = new SalesOrderHeader();

                salesOrderHeader.setSalesOrderNumber(dbOBOrder.getSalesOrderNo());
                salesOrderHeader.setCompanyCode(dbOBOrder.getCompanyCode());
                salesOrderHeader.setBranchCode(dbOBOrder.getBranchCode());
                salesOrderHeader.setPickListNumber(dbOBOrder.getPickListNo());
                salesOrderHeader.setRequiredDeliveryDate(String.valueOf(dbOBOrder.getPickListdate()));
                salesOrderHeader.setStoreID(dbOBOrder.getBranchCode());
                salesOrderHeader.setStoreName(dbOBOrder.getBranchCode());
                salesOrderHeader.setTokenNumber(dbOBOrder.getTokenNumber());
                salesOrderHeader.setStatus("ACTIVE");
                salesOrderHeader.setMiddlewareId(dbOBOrder.getPickListHeaderId());
                salesOrderHeader.setMiddlewareTable("OB_SalesOrder");

                List<SalesOrderLine> salesOrderLines = new ArrayList<>();
                for (PickListLine line : dbOBOrder.getPickListLines()) {
                    SalesOrderLine salesOrderLine = new SalesOrderLine();

                    salesOrderLine.setLineReference(line.getLineNumberOfEachItem());
                    salesOrderLine.setSku(line.getItemCode());
                    salesOrderLine.setSkuDescription(line.getItemDescription());
                    salesOrderLine.setManufacturerCode(line.getManufacturerCode());
                    salesOrderLine.setManufacturerName(line.getManufacturerShortName());
                    salesOrderLine.setManufacturerFullName(line.getManufacturerFullName());
                    salesOrderLine.setUom(line.getUnitOfMeasure());
                    salesOrderLine.setOrderedQty(line.getPickListQty());
                    salesOrderLine.setExpectedQty(line.getPickListQty());
                    salesOrderLine.setPackQty(line.getPickedQty());
                    salesOrderLine.setPickListNo(line.getPickListNo());
                    salesOrderLine.setSalesOrderNo(line.getSalesOrderNo());
                    salesOrderLine.setMiddlewareId(line.getPickListLineId());
                    salesOrderLine.setMiddlewareHeaderId(dbOBOrder.getPickListHeaderId());
                    salesOrderLine.setMiddlewareTable("OB_SalesOrder");

                    salesOrderLines.add(salesOrderLine);
                }
                salesOrder.setSalesOrderHeader(salesOrderHeader);
                salesOrder.setSalesOrderLine(salesOrderLines);
                outboundSalesOrderList.add(salesOrder);
            }
            spSalesOrderList = new CopyOnWriteArrayList<SalesOrder>(outboundSalesOrderList);
//            log.info("There is no Sale Order/PickList record found to process (sql) ...Waiting..");
        }

        if (outboundSalesOrderList != null) {
            log.info("Latest Sale Order found: " + outboundSalesOrderList);
            for (SalesOrder outbound : spSalesOrderList) {
                try {
                    log.info("Sale Order Number : " + outbound.getSalesOrderHeader().getSalesOrderNumber());
                    log.info("Pick List Number : " + outbound.getSalesOrderHeader().getPickListNumber());

                    WarehouseApiResponse outboundHeader = salesOrderService.postSalesOrder(outbound);

                    if (outboundHeader != null) {
                        // Updating the Processed Status = 10
                        salesOrderService.updateProcessedInboundOrder(outbound.getSalesOrderHeader().getMiddlewareId(),
                                outbound.getSalesOrderHeader().getCompanyCode(), outbound.getSalesOrderHeader().getBranchCode(),
                                outbound.getSalesOrderHeader().getPickListNumber(), 10L);
                        outboundSalesOrderList.remove(outbound);
                        return outboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesOrderLog(outbound, e.toString());

                    // Updating the Processed Status = 100
                    salesOrderService.updateProcessedInboundOrder(outbound.getSalesOrderHeader().getMiddlewareId(),
                            outbound.getSalesOrderHeader().getCompanyCode(), outbound.getSalesOrderHeader().getBranchCode(),
                            outbound.getSalesOrderHeader().getPickListNumber(), 100L);

//                    salesOrderV2Service.createInboundIntegrationLog(outbound);
                    outboundSalesOrderList.remove(outbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public WarehouseApiResponse processOutboundOrderSI() throws IllegalAccessException, InvocationTargetException {
        if (outboundSIList == null || outboundSIList.isEmpty()) {
            List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On salesInvoiceList: " + salesInvoiceList);
            outboundSIList = new ArrayList<>();
            for (SalesInvoice dbOBOrder : salesInvoiceList) {

                com.almailem.ams.api.connector.model.wms.SalesInvoice salesInvoice = new com.almailem.ams.api.connector.model.wms.SalesInvoice();

                salesInvoice.setCompanyCode(dbOBOrder.getCompanyCode());
                salesInvoice.setBranchCode(dbOBOrder.getBranchCode());
                salesInvoice.setSalesOrderNumber(dbOBOrder.getSalesOrderNumber());
                salesInvoice.setSalesInvoiceNumber(dbOBOrder.getSalesInvoiceNumber());
                salesInvoice.setPickListNumber(dbOBOrder.getPickListNumber());
                salesInvoice.setInvoiceDate(String.valueOf(dbOBOrder.getInvoiceDate()));

                salesInvoice.setAddress(dbOBOrder.getAddress());
                salesInvoice.setStatus(dbOBOrder.getStatus());
                salesInvoice.setAlternateNo(dbOBOrder.getAlternateNo());
                salesInvoice.setCustomerId(dbOBOrder.getCustomerId());
                salesInvoice.setCustomerName(dbOBOrder.getCustomerName());
                salesInvoice.setPhoneNumber(dbOBOrder.getPhoneNumber());
                salesInvoice.setDeliveryType(dbOBOrder.getDeliveryType());

                salesInvoice.setMiddlewareId(dbOBOrder.getSalesInvoiceId());
                salesInvoice.setMiddlewareTable("OBSalesInvoice");

                outboundSIList.add(salesInvoice);
            }
            spSIList = new CopyOnWriteArrayList<com.almailem.ams.api.connector.model.wms.SalesInvoice>(outboundSIList);
//            log.info("There is no record found to process (sql) ...Waiting..");
        }

        if (outboundSIList != null) {
            log.info("Latest Sales Invoice found: " + outboundSOList);
            for (com.almailem.ams.api.connector.model.wms.SalesInvoice outbound : spSIList) {
                try {
                    log.info("Sales Invoice Number : " + outbound.getSalesInvoiceNumber());
                    WarehouseApiResponse outboundHeader = salesInvoiceService.postSalesInvoice(outbound);
                    if (outboundHeader != null) {

                        // Updating the Processed Status = 10
                        salesInvoiceService.updateProcessedOutboundOrder(outbound.getMiddlewareId(), outbound.getCompanyCode(),
                                outbound.getBranchCode(), outbound.getSalesInvoiceNumber(), 10L);

                        outboundSIList.remove(outbound);
                        return outboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesInvoice(outbound, e.toString());

                    // Updating the Processed Status = 100
                    salesInvoiceService.updateProcessedOutboundOrder(outbound.getMiddlewareId(), outbound.getCompanyCode(),
                            outbound.getBranchCode(), outbound.getSalesInvoiceNumber(), 100L);

//                      salesInvoiceService.createInboundIntegrationLog(outbound);
                    outboundSIList.remove(outbound);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    //======================================StockCount_Perpetual=======================================================
    public WarehouseApiResponse processPerpetualOrder() throws IllegalAccessException, InvocationTargetException {
        if (stcPerpetualList == null || stcPerpetualList.isEmpty()) {
            List<PerpetualHeader> perpetualHeaders = perpetualHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On perpetualHeaders: " + perpetualHeaders);
            stcPerpetualList = new ArrayList<>();
            Perpetual perpetual = new Perpetual();

            for (PerpetualHeader dbObOrder : perpetualHeaders) {
                PerpetualHeaderV1 perpetualHeaderV1 = new PerpetualHeaderV1();

                perpetualHeaderV1.setCompanyCode(dbObOrder.getCompanyCode());
                perpetualHeaderV1.setCycleCountNo(dbObOrder.getCycleCountNo());
                perpetualHeaderV1.setBranchCode(dbObOrder.getBranchCode());
                perpetualHeaderV1.setBranchName(dbObOrder.getBranchName());
                perpetualHeaderV1.setIsNew(dbObOrder.getIsNew());
                perpetualHeaderV1.setCycleCountCreationDate(dbObOrder.getCycleCountCreationDate());
                perpetualHeaderV1.setUpdatedOn(dbObOrder.getUpdatedOn());
                perpetualHeaderV1.setIsCancelled(dbObOrder.getIsCancelled());
                perpetualHeaderV1.setIsCompleted(dbObOrder.getIsCompleted());
                perpetualHeaderV1.setMiddlewareId(dbObOrder.getPerpetualHeaderId());
                perpetualHeaderV1.setMiddlewareTable("PERPETUAL_HEADER");

                List<PerpetualLineV1> perpetualLineV1List = new ArrayList<>();
                for (PerpetualLine line : dbObOrder.getPerpetualLines()) {
                    PerpetualLineV1 perpetualLineV1 = new PerpetualLineV1();

                    perpetualLineV1.setCycleCountNo(line.getCycleCountNo());
                    perpetualLineV1.setLineNoOfEachItemCode(line.getLineNoOfEachItemCode());
                    perpetualLineV1.setItemCode(line.getItemCode());
                    perpetualLineV1.setItemDescription(line.getItemDescription());
                    perpetualLineV1.setUom(line.getUnitOfMeasure());
                    perpetualLineV1.setManufacturerCode(line.getManufacturerCode());
                    perpetualLineV1.setManufacturerName(line.getManufacturerName());
                    perpetualLineV1.setFrozenQty(line.getFrozenQty());
                    perpetualLineV1.setCountedQty(line.getCountedQty());
                    perpetualLineV1.setIsCancelled(line.getIsCancelled());
                    perpetualLineV1.setIsCompleted(line.getIsCompleted());
                    perpetualLineV1.setMiddlewareId(line.getPerpetualLineId());
                    perpetualLineV1.setMiddlewareHeaderId(dbObOrder.getPerpetualHeaderId());
                    perpetualLineV1.setMiddlewareTable("PERPETUAL_LINE");
                    perpetualLineV1List.add(perpetualLineV1);
                }
                perpetual.setPerpetualLineV1(perpetualLineV1List);
                perpetual.setPerpetualHeaderV1(perpetualHeaderV1);
                stcPerpetualList.add(perpetual);
            }
            spPerpetualList = new CopyOnWriteArrayList<Perpetual>(stcPerpetualList);
            log.info("There is no Perpetual record found to process (sql) ...Waiting..");
        }

        if (stcPerpetualList != null) {
            log.info("Latest Perpetual Order found: " + stcPerpetualList);
            for (Perpetual stockCount : spPerpetualList) {
                try {
                    log.info("Perpetual Order Number: " + stockCount.getPerpetualHeaderV1().getCycleCountNo());
                    WarehouseApiResponse response = perpetualService.postPerpetualOrder(stockCount);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        perpetualService.updateProcessedPerpetualOrder(stockCount.getPerpetualHeaderV1().getMiddlewareId(),
                                stockCount.getPerpetualHeaderV1().getCompanyCode(), stockCount.getPerpetualHeaderV1().getBranchCode(),
                                stockCount.getPerpetualHeaderV1().getCycleCountNo(), 10L);
                        stcPerpetualList.remove(stockCount);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on StockCount processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createPerpetualLog(stockCount, e.toString());

                    //Updating the Processed Status = 100
                    perpetualService.updateProcessedPerpetualOrder(stockCount.getPerpetualHeaderV1().getMiddlewareId(),
                            stockCount.getPerpetualHeaderV1().getCompanyCode(), stockCount.getPerpetualHeaderV1().getBranchCode(),
                            stockCount.getPerpetualHeaderV1().getCycleCountNo(), 100L);
                    stcPerpetualList.remove(stockCount);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    //======================================StockCount_Periodic=======================================================
    public WarehouseApiResponse processPeriodicOrder() throws IllegalAccessException, InvocationTargetException {
        if (stcPeriodicList == null || stcPeriodicList.isEmpty()) {
            List<PeriodicHeader> periodicHeaders = periodicHeaderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On periodicHeaders: " + periodicHeaders);
            stcPeriodicList = new ArrayList<>();
            Periodic periodic = new Periodic();

            for (PeriodicHeader dbOrder : periodicHeaders) {
                PeriodicHeaderV1 periodicHeaderV1 = new PeriodicHeaderV1();

                periodicHeaderV1.setCompanyCode(dbOrder.getCompanyCode());
                periodicHeaderV1.setBranchCode(dbOrder.getBranchCode());
                periodicHeaderV1.setBranchName(dbOrder.getBranchName());
                periodicHeaderV1.setCycleCountNo(dbOrder.getCycleCountNo());
                periodicHeaderV1.setCycleCountCreationDate(dbOrder.getCycleCountCreationDate());
                periodicHeaderV1.setIsNew(dbOrder.getIsNew());
                periodicHeaderV1.setIsCancelled(dbOrder.getIsCancelled());
                periodicHeaderV1.setIsCompleted(dbOrder.getIsCompleted());
                periodicHeaderV1.setUpdatedOn(dbOrder.getUpdatedOn());
                periodicHeaderV1.setMiddlewareId(dbOrder.getPeriodicHeaderId());
                periodicHeaderV1.setMiddlewareTable("PERIODIC_HEADER");

                List<PeriodicLineV1> periodicLineV1List = new ArrayList<>();
                for (PeriodicLine line : dbOrder.getPeriodicLines()) {
                    PeriodicLineV1 periodicLineV1 = new PeriodicLineV1();

                    periodicLineV1.setCycleCountNo(line.getCycleCountNo());
                    periodicLineV1.setLineNoOfEachItemCode(line.getLineNoOfEachItemCode());
                    periodicLineV1.setItemCode(line.getItemCode());
                    periodicLineV1.setItemDescription(line.getItemDescription());
                    periodicLineV1.setItemDescription(line.getItemDescription());
                    periodicLineV1.setUom(line.getUnitOfMeasure());
                    periodicLineV1.setManufacturerCode(line.getManufacturerCode());
                    periodicLineV1.setManufacturerName(line.getManufacturerName());
                    periodicLineV1.setCountedQty(line.getCountedQty());
                    periodicLineV1.setFrozenQty(line.getFrozenQty());
                    periodicLineV1.setIsCompleted(line.getIsCompleted());
                    periodicLineV1.setIsCancelled(line.getIsCancelled());
                    periodicLineV1.setMiddlewareId(line.getPeriodicLineId());
                    periodicLineV1.setMiddlewareHeaderId(dbOrder.getPeriodicHeaderId());
                    periodicLineV1.setMiddlewareTable("PERIODIC_LINE");
                    periodicLineV1List.add(periodicLineV1);
                }
                periodic.setPeriodicLineV1(periodicLineV1List);
                periodic.setPeriodicHeaderV1(periodicHeaderV1);
                stcPeriodicList.add(periodic);
            }
            spPeriodicList = new CopyOnWriteArrayList<Periodic>(stcPeriodicList);
//            log.info("There is no Periodic record found to process (sql) ...Waiting..");
        }

        if (stcPeriodicList != null) {
            log.info("Latest Periodic Order found: " + stcPeriodicList);
            for (Periodic stockCount : spPeriodicList) {
                try {
                    log.info("Periodic Order Number: " + stockCount.getPeriodicHeaderV1().getCycleCountNo());
                    WarehouseApiResponse response = periodicService.postPeriodicOrder(stockCount);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        periodicService.updateProcessedPeriodicOrder(stockCount.getPeriodicHeaderV1().getMiddlewareId(),
                                stockCount.getPeriodicHeaderV1().getCompanyCode(), stockCount.getPeriodicHeaderV1().getBranchCode(),
                                stockCount.getPeriodicHeaderV1().getCycleCountNo(), 10L);

                        stcPeriodicList.remove(stockCount);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on StockCount processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createPeriodicLog(stockCount, e.toString());

                    //Updating the Processed Status
                    periodicService.updateProcessedPeriodicOrder(stockCount.getPeriodicHeaderV1().getMiddlewareId(),
                            stockCount.getPeriodicHeaderV1().getCompanyCode(), stockCount.getPeriodicHeaderV1().getBranchCode(),
                            stockCount.getPeriodicHeaderV1().getCycleCountNo(), 100L);

                    stcPeriodicList.remove(stockCount);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    //=======================================================V2============================================================

    public WarehouseApiResponse processStockAdjustmentOrder() throws IllegalAccessException, InvocationTargetException {
        if (saList == null || saList.isEmpty()) {
            List<com.almailem.ams.api.connector.model.stockadjustment.StockAdjustment> stockAdjustments = stockAdjustmentRepo.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("StockAdjustment Found: " + stockAdjustments);
            saList = new ArrayList<>();
            StockAdjustment stockAdjustment = new StockAdjustment();
            for (com.almailem.ams.api.connector.model.stockadjustment.StockAdjustment dbSA : stockAdjustments) {

//                stockAdjustment.setOrderId(dbSA.getItemCode());
                stockAdjustment.setCompanyCode(dbSA.getCompanyCode());
                stockAdjustment.setBranchCode(dbSA.getBranchCode());
                stockAdjustment.setBranchName(dbSA.getBranchName());
                stockAdjustment.setDateOfAdjustment(dbSA.getDateOfAdjustment());
                stockAdjustment.setIsDamage(dbSA.getIsDamage());
                stockAdjustment.setIsCycleCount(dbSA.getIsCycleCount());
                stockAdjustment.setItemCode(dbSA.getItemCode());
                stockAdjustment.setItemDescription(dbSA.getItemDescription());
                stockAdjustment.setAdjustmentQty(dbSA.getAdjustmentQty());
                stockAdjustment.setUnitOfMeasure(dbSA.getUnitOfMeasure());
                stockAdjustment.setManufacturerCode(dbSA.getManufacturerCode());
                if (dbSA.getManufacturerName() != null) {
                    stockAdjustment.setManufacturerName(dbSA.getManufacturerName());
                }
                if (dbSA.getManufacturerName() == null) {
                    stockAdjustment.setManufacturerName(dbSA.getManufacturerCode());
                }
                stockAdjustment.setRemarks(dbSA.getRemarks());
                stockAdjustment.setAmsReferenceNo(dbSA.getAmsReferenceNo());
                stockAdjustment.setIsCompleted(dbSA.getIsCompleted());
                stockAdjustment.setUpdatedOn(dbSA.getUpdatedOn());
                stockAdjustment.setStockAdjustmentId(dbSA.getStockAdjustmentId());
                stockAdjustment.setMiddlewareId(dbSA.getStockAdjustmentId());
                stockAdjustment.setMiddlewareTable("STOCK_ADJUSTMENT");

                saList.add(stockAdjustment);
            }
            spStockAdjustmentList = new CopyOnWriteArrayList<StockAdjustment>(saList);
//            log.info("There is no StockAdjustment record found to process (sql) ...Waiting..");
        }

        if (saList != null) {
            log.info("Latest Stock Adjustment found: " + saList);
            for (StockAdjustment stockCount : spStockAdjustmentList) {
                try {
                    log.info("Item Code: " + stockCount.getItemCode());
                    WarehouseApiResponse response = stockAdjustmentService.postStockAdjustment(stockCount);
                    if (response != null) {
                        // Updating the Processed Status = 10
                        stockAdjustmentService.updateProcessedStockAdjustment(stockCount.getStockAdjustmentId(),
                                stockCount.getCompanyCode(), stockCount.getBranchCode(), stockCount.getItemCode(), 10L);
                        saList.remove(stockCount);
                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on Stock Adjustment processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createStockAdjustment(stockCount, e.toString());

                    // Updating the Processed Status = 100
                    stockAdjustmentService.updateProcessedStockAdjustment(stockCount.getStockAdjustmentId(),
                            stockCount.getCompanyCode(), stockCount.getBranchCode(), stockCount.getItemCode(), 100L);
                    saList.remove(stockCount);
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

}