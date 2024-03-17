package com.almailem.ams.api.connector.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.almailem.ams.api.connector.model.wms.ASN;
import com.almailem.ams.api.connector.model.wms.ASNHeader;
import com.almailem.ams.api.connector.model.wms.ASNLine;
import com.almailem.ams.api.connector.model.wms.B2bTransferIn;
import com.almailem.ams.api.connector.model.wms.B2bTransferInHeader;
import com.almailem.ams.api.connector.model.wms.B2bTransferInLine;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferIn;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferInHeader;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferInLine;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferOut;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferOutHeader;
import com.almailem.ams.api.connector.model.wms.InterWarehouseTransferOutLine;
import com.almailem.ams.api.connector.model.wms.Periodic;
import com.almailem.ams.api.connector.model.wms.PeriodicHeaderV1;
import com.almailem.ams.api.connector.model.wms.PeriodicLineV1;
import com.almailem.ams.api.connector.model.wms.Perpetual;
import com.almailem.ams.api.connector.model.wms.PerpetualHeaderV1;
import com.almailem.ams.api.connector.model.wms.PerpetualLineV1;
import com.almailem.ams.api.connector.model.wms.ReturnPO;
import com.almailem.ams.api.connector.model.wms.ReturnPOHeader;
import com.almailem.ams.api.connector.model.wms.ReturnPOLine;
import com.almailem.ams.api.connector.model.wms.SOHeader;
import com.almailem.ams.api.connector.model.wms.SOLine;
import com.almailem.ams.api.connector.model.wms.SOReturnHeader;
import com.almailem.ams.api.connector.model.wms.SOReturnLine;
import com.almailem.ams.api.connector.model.wms.SaleOrderReturn;
import com.almailem.ams.api.connector.model.wms.SalesOrder;
import com.almailem.ams.api.connector.model.wms.SalesOrderHeader;
import com.almailem.ams.api.connector.model.wms.SalesOrderLine;
import com.almailem.ams.api.connector.model.wms.ShipmentOrder;
import com.almailem.ams.api.connector.model.wms.StockAdjustment;
import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.repository.PeriodicHeaderRepository;
import com.almailem.ams.api.connector.repository.PerpetualHeaderRepository;
import com.almailem.ams.api.connector.repository.PickListHeaderRepository;
import com.almailem.ams.api.connector.repository.PurchaseReturnHeaderRepository;
import com.almailem.ams.api.connector.repository.SalesInvoiceRepository;
import com.almailem.ams.api.connector.repository.SalesReturnHeaderRepository;
import com.almailem.ams.api.connector.repository.StockAdjustmentRepository;
import com.almailem.ams.api.connector.repository.StockReceiptHeaderRepository;
import com.almailem.ams.api.connector.repository.SupplierInvoiceHeaderRepository;
import com.almailem.ams.api.connector.repository.TransferInHeaderRepository;
import com.almailem.ams.api.connector.repository.TransferOutHeaderRepository;

import lombok.extern.slf4j.Slf4j;

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

        	List<SupplierInvoiceHeader> supplierInvoiceHeaders = supplierInvoiceHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On SupplierInvoiceHeaders: " + supplierInvoiceHeaders);
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
                
                try {
                    WarehouseApiResponse inboundHeader = supplierInvoiceService.postASNV2(asn);
                    if (inboundHeader != null) {

                        // Updating the Processed Status = 10
                        supplierInvoiceService.updateProcessedInboundOrder(asn.getAsnHeader().getMiddlewareId(),
                        		asn.getAsnHeader().getCompanyCode(), asn.getAsnHeader().getBranchCode(),
                        		asn.getAsnHeader().getAsnNumber(), 10L);
                        log.info ("ASN : " + asn.getAsnHeader().getAsnNumber() + " processed.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //IntegrationLog
                    integrationLogService.createAsnLog(asn, e.toString());

                    // Updating the Processed Status = 100
                    supplierInvoiceService.updateProcessedInboundOrder(asn.getAsnHeader().getMiddlewareId(),
                    		asn.getAsnHeader().getCompanyCode(), asn.getAsnHeader().getBranchCode(),
                    		asn.getAsnHeader().getAsnNumber(), 100L);
                    throw new RuntimeException(e);
                }
                
            }
        return null;
    }


    //=====================================================StockReceipt============================================================
    public WarehouseApiResponse processInboundOrderSR() throws IllegalAccessException, InvocationTargetException {

            List<StockReceiptHeader> stockReceiptHeaders = stockReceiptHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On stockReceiptHeaders: " + stockReceiptHeaders);
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
                
                try {
                    log.info("Stock Receipt Number : " + stockReceiptHeader.getReceiptNo());
                    WarehouseApiResponse inboundHeader = stockReceiptService.postStockReceipt(stockReceiptHeader);
                    if (inboundHeader != null) {
                        // Updating the Processed Status = 10
                        stockReceiptService.updateProcessedInboundOrder(stockReceiptHeader.getMiddlewareId(), stockReceiptHeader.getCompanyCode(),
                        		stockReceiptHeader.getBranchCode(), stockReceiptHeader.getReceiptNo(), 10L);
                        return inboundHeader;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    // Updating the Processed Status = 100
                    stockReceiptService.updateProcessedInboundOrder(stockReceiptHeader.getMiddlewareId(), stockReceiptHeader.getCompanyCode(),
                    		stockReceiptHeader.getBranchCode(), stockReceiptHeader.getReceiptNo(), 100L);
//                    stockReceiptService.createInboundIntegrationLog(inbound);
                    integrationLogService.createStockReceiptHeaderLog(stockReceiptHeader, e.toString());
                    throw new RuntimeException(e);
                }
            }
        return null;
    }

    //=====================================================SalesReturn============================================================
    public WarehouseApiResponse processInboundOrderSRT() throws IllegalAccessException, InvocationTargetException {

            List<SalesReturnHeader> salesReturnHeaders = salesReturnHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On salesReturnHeaders: " + salesReturnHeaders);
            for (SalesReturnHeader dbIBOrder : salesReturnHeaders) {
                SaleOrderReturn saleOrderReturn = new SaleOrderReturn();
                SOReturnHeader salesReturnHeader = new SOReturnHeader();

                salesReturnHeader.setCompanyCode(dbIBOrder.getCompanyCode());
                salesReturnHeader.setBranchCode(dbIBOrder.getBranchCode());
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

                try {
                    log.info("Sales Return Number : " + saleOrderReturn.getSoReturnHeader().getTransferOrderNumber());
                    WarehouseApiResponse inboundHeader = salesReturnService.postSaleOrderReturn(saleOrderReturn);
                    if (inboundHeader != null) {
                        // Updating the Processed Status = 10
                        salesReturnService.updateProcessedInboundOrder(saleOrderReturn.getSoReturnHeader().getMiddlewareId(),
                                saleOrderReturn.getSoReturnHeader().getCompanyCode(), saleOrderReturn.getSoReturnHeader().getBranchCode(),
                                saleOrderReturn.getSoReturnHeader().getTransferOrderNumber(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesOrderReturnLog(saleOrderReturn, e.getMessage());

                    // Updating the Processed Status = 100
                    salesReturnService.updateProcessedInboundOrder(saleOrderReturn.getSoReturnHeader().getMiddlewareId(),
                            saleOrderReturn.getSoReturnHeader().getCompanyCode(), saleOrderReturn.getSoReturnHeader().getBranchCode(),
                            saleOrderReturn.getSoReturnHeader().getTransferOrderNumber(), 100L);
                    throw new RuntimeException(e);
                }
            }
        return null;
    }


    //=====================================================Interwarehouse============================================================
    public WarehouseApiResponse processInboundOrderIWT() throws IllegalAccessException, InvocationTargetException {

            List<TransferInHeader> transferInHeaders = transferInHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On transferInHeaders: " + transferInHeaders);
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
                    try {
                        log.info("B2B Transfer Order Number : " + b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber());
                        WarehouseApiResponse inboundHeader = b2BTransferInService.postB2BTransferIn(b2bTransferIn);
                        if (inboundHeader != null) {

                            // Updating the Processed Status = 10
                            b2BTransferInService.updateProcessedInboundOrder(b2bTransferIn.getB2bTransferInHeader().getMiddlewareId(),
                                    b2bTransferIn.getB2bTransferInHeader().getCompanyCode(), b2bTransferIn.getB2bTransferInHeader().getBranchCode(),
                                    b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on inbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createB2bTransferLog(b2bTransferIn, e.toString());

                        // Updating the Processed Status = 100
                        b2BTransferInService.updateProcessedInboundOrder(b2bTransferIn.getB2bTransferInHeader().getMiddlewareId(),
                                b2bTransferIn.getB2bTransferInHeader().getCompanyCode(), b2bTransferIn.getB2bTransferInHeader().getBranchCode(),
                                b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    try {
                        log.info("B2B Transfer Order Number : " + b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber());
                        WarehouseApiResponse inboundHeader = b2BTransferInService.postB2BTransferIn(b2bTransferIn);
                        if (inboundHeader != null) {
                            // Updating the Processed Status = 10
                            b2BTransferInService.updateProcessedInboundOrder(b2bTransferIn.getB2bTransferInHeader().getMiddlewareId(),
                                    b2bTransferIn.getB2bTransferInHeader().getCompanyCode(), b2bTransferIn.getB2bTransferInHeader().getBranchCode(),
                                    b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on inbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createB2bTransferLog(b2bTransferIn, e.toString());

                        // Updating the Processed Status = 100
                        b2BTransferInService.updateProcessedInboundOrder(b2bTransferIn.getB2bTransferInHeader().getMiddlewareId(),
                                b2bTransferIn.getB2bTransferInHeader().getCompanyCode(), b2bTransferIn.getB2bTransferInHeader().getBranchCode(),
                                b2bTransferIn.getB2bTransferInHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    try {
                        log.info("InterWarehouse Transfer Order Number : " + interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber());
                        WarehouseApiResponse inboundHeader = interWarehouseTransferInService.postIWTTransferIn(interWarehouseTransferIn);
                        if (inboundHeader != null) {

                            // Updating the Processed Status = 10
                            interWarehouseTransferInService.updateProcessedInboundOrder(interWarehouseTransferIn.getInterWarehouseTransferInHeader().getMiddlewareId(),
                                    interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToCompanyCode(), interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToBranchCode(),
                                    interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on inbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createInterWarehouseTransferInLog(interWarehouseTransferIn, e.toString());

                        // Updating the Processed Status = 100
                        interWarehouseTransferInService.updateProcessedInboundOrder(interWarehouseTransferIn.getInterWarehouseTransferInHeader().getMiddlewareId(),
                                interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToCompanyCode(), interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToBranchCode(),
                                interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    try {
                        log.info("InterWarehouse Transfer Order Number : " + interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber());
                        WarehouseApiResponse inboundHeader = interWarehouseTransferInService.postIWTTransferIn(interWarehouseTransferIn);
                        if (inboundHeader != null) {
                            // Updating the Processed Status = 10
                            interWarehouseTransferInService.updateProcessedInboundOrder(interWarehouseTransferIn.getInterWarehouseTransferInHeader().getMiddlewareId(),
                                    interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToCompanyCode(), interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToBranchCode(),
                                    interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on inbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createInterWarehouseTransferInLog(interWarehouseTransferIn, e.toString());

                        // Updating the Processed Status = 100
                        interWarehouseTransferInService.updateProcessedInboundOrder(interWarehouseTransferIn.getInterWarehouseTransferInHeader().getMiddlewareId(),
                                interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToCompanyCode(), interWarehouseTransferIn.getInterWarehouseTransferInHeader().getToBranchCode(),
                                interWarehouseTransferIn.getInterWarehouseTransferInHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
                }
            }
        return null;
    }

    //===========================================Outbound==============================================================
    //===========================================Purchase_Return=======================================================
    public WarehouseApiResponse processOutboundOrderRPO() throws IllegalAccessException, InvocationTargetException {

        List<PurchaseReturnHeader> purchaseReturns = purchaseReturnHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On purchaseReturns: " + purchaseReturns);
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
                try {
                    log.info("Purchase Return Number: " + returnPO.getReturnPOHeader().getPoNumber());
                    WarehouseApiResponse response = returnPOService.postReturnPOV2(returnPO);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        returnPOService.updateProcessedOutboundOrder(returnPO.getReturnPOHeader().getMiddlewareId(),
                                returnPO.getReturnPOHeader().getCompanyCode(), returnPO.getReturnPOHeader().getBranchCode(),
                                returnPO.getReturnPOHeader().getPoNumber(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createReturnPoLog(returnPO, e.toString());

                    //Updating the Processed Status = 100
                    returnPOService.updateProcessedOutboundOrder(returnPO.getReturnPOHeader().getMiddlewareId(),
                            returnPO.getReturnPOHeader().getCompanyCode(), returnPO.getReturnPOHeader().getBranchCode(),
                            returnPO.getReturnPOHeader().getPoNumber(), 100L);
                    throw new RuntimeException(e);
                }
            }
        return null;
    }

    //------------------------------------InterWarehouse & ShipmentOrder---------------------------------------------
    public WarehouseApiResponse processOutboundOrderIWT() throws IllegalAccessException, InvocationTargetException {

            List<TransferOutHeader> transferOuts = transferOutHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOnDesc(0L);
            log.info("TransferOut / Shipment Order Found: " + transferOuts);
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
                    log.info("outboundSOList: " + outboundSOList);
                    try {
                        log.info("SO Transfer Order Number: " + shipmentOrder.getSoHeader().getTransferOrderNumber());
                        WarehouseApiResponse response = shipmentOrderService.postShipmentOrder(shipmentOrder);
                        if (response != null) {
                            // Updating the Processed Status = 10
                            shipmentOrderService.updateProcessedOutboundOrder(shipmentOrder.getSoHeader().getMiddlewareId(), shipmentOrder.getSoHeader().getCompanyCode(),
                                    shipmentOrder.getSoHeader().getBranchCode(), shipmentOrder.getSoHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on outbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createShipmentOrderLog(shipmentOrder, e.toString());

                        // Updating the Processed Status = 100
                        shipmentOrderService.updateProcessedOutboundOrder(shipmentOrder.getSoHeader().getMiddlewareId(), shipmentOrder.getSoHeader().getCompanyCode(),
                                shipmentOrder.getSoHeader().getBranchCode(), shipmentOrder.getSoHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    log.info("outboundIWhtList: " + outboundIWhtList);
                    try {
                        log.info("IWT Transfer Out Number: " + iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber());
                        WarehouseApiResponse response = interWarehouseTransferOutService.postIWhTransferOutV2(iWhTransferOut);
                        if (response != null) {

                            //Updating the Processed Status = 10
                            interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on outbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createInterWarehouseTransferOutLog(iWhTransferOut, e.toString());

                        //Updating the Processed Status = 100
                        interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    log.info("outboundIWhtList: " + outboundIWhtList);
                    try {
                        log.info("IWT Transfer Out Number: " + iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber());
                        WarehouseApiResponse response = interWarehouseTransferOutService.postIWhTransferOutV2(iWhTransferOut);
                        if (response != null) {

                            //Updating the Processed Status = 10
                            interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on outbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createInterWarehouseTransferOutLog(iWhTransferOut, e.toString());

                        //Updating the Processed Status = 100
                        interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
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
                    log.info("outboundIWhtList: OB_NONWMS_TO_NONWMS " + outboundIWhtList);
                    try {
                        log.info("IWT Transfer Out Number: " + iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber());
                        WarehouseApiResponse response = interWarehouseTransferOutService.postIWhTransferOutV2(iWhTransferOut);
                        if (response != null) {

                            //Updating the Processed Status = 10
                            interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                    iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 10L);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error on outbound processing : " + e.toString());

                        //Integration Log
                        integrationLogService.createInterWarehouseTransferOutLog(iWhTransferOut, e.toString());

                        //Updating the Processed Status = 100
                        interWarehouseTransferOutService.updateProcessedOutboundOrder(iWhTransferOut.getInterWarehouseTransferOutHeader().getMiddlewareId(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getFromCompanyCode(), iWhTransferOut.getInterWarehouseTransferOutHeader().getFromBranchCode(),
                                iWhTransferOut.getInterWarehouseTransferOutHeader().getTransferOrderNumber(), 100L);
                        throw new RuntimeException(e);
                    }
                }
            }
        return null;
    }

    public WarehouseApiResponse processOutboundOrderPL() throws IllegalAccessException, InvocationTargetException {

        List<PickListHeader> pickListHeaders = pickListHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On pickListHeaders: " + pickListHeaders);
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

                try {
                    log.info("Sale Order Number : " + salesOrder.getSalesOrderHeader().getSalesOrderNumber());
                    log.info("Pick List Number : " + salesOrder.getSalesOrderHeader().getPickListNumber());

                    WarehouseApiResponse outboundHeader = salesOrderService.postSalesOrder(salesOrder);

                    if (outboundHeader != null) {
                        // Updating the Processed Status = 10
                        salesOrderService.updateProcessedInboundOrder(salesOrder.getSalesOrderHeader().getMiddlewareId(),
                                salesOrder.getSalesOrderHeader().getCompanyCode(), salesOrder.getSalesOrderHeader().getBranchCode(),
                                salesOrder.getSalesOrderHeader().getPickListNumber(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesOrderLog(salesOrder, e.toString());

                    // Updating the Processed Status = 100
                    salesOrderService.updateProcessedInboundOrder(salesOrder.getSalesOrderHeader().getMiddlewareId(),
                            salesOrder.getSalesOrderHeader().getCompanyCode(), salesOrder.getSalesOrderHeader().getBranchCode(),
                            salesOrder.getSalesOrderHeader().getPickListNumber(), 100L);
                    throw new RuntimeException(e);
                }
               
            }
        return null;
    }

    public WarehouseApiResponse processOutboundOrderSI() throws IllegalAccessException, InvocationTargetException {

            List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On salesInvoiceList: " + salesInvoiceList);
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

                try {
                    log.info("Sales Invoice Number : " + salesInvoice.getSalesInvoiceNumber());
                    WarehouseApiResponse outboundHeader = salesInvoiceService.postSalesInvoice(salesInvoice);
                    if (outboundHeader != null) {

                        // Updating the Processed Status = 10
                        salesInvoiceService.updateProcessedOutboundOrder(salesInvoice.getMiddlewareId(), salesInvoice.getCompanyCode(),
                                salesInvoice.getBranchCode(), salesInvoice.getSalesInvoiceNumber(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createSalesInvoice(salesInvoice, e.toString());

                    // Updating the Processed Status = 100
                    salesInvoiceService.updateProcessedOutboundOrder(salesInvoice.getMiddlewareId(), salesInvoice.getCompanyCode(),
                            salesInvoice.getBranchCode(), salesInvoice.getSalesInvoiceNumber(), 100L);
                    throw new RuntimeException(e);
                }
            }

        return null;
    }

    //======================================StockCount_Perpetual=======================================================
    public WarehouseApiResponse processPerpetualOrder() throws IllegalAccessException, InvocationTargetException {

        List<PerpetualHeader> perpetualHeaders = perpetualHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On perpetualHeaders: " + perpetualHeaders);
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
                try {
                    log.info("Perpetual Order Number: " + perpetual.getPerpetualHeaderV1().getCycleCountNo());
                    WarehouseApiResponse response = perpetualService.postPerpetualOrder(perpetual);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        perpetualService.updateProcessedPerpetualOrder(perpetual.getPerpetualHeaderV1().getMiddlewareId(),
                                perpetual.getPerpetualHeaderV1().getCompanyCode(), perpetual.getPerpetualHeaderV1().getBranchCode(),
                                perpetual.getPerpetualHeaderV1().getCycleCountNo(), 10L);
//                        stcPerpetualList.remove(perpetual);
//                        return response;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on StockCount processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createPerpetualLog(perpetual, e.toString());

                    //Updating the Processed Status = 100
                    perpetualService.updateProcessedPerpetualOrder(perpetual.getPerpetualHeaderV1().getMiddlewareId(),
                            perpetual.getPerpetualHeaderV1().getCompanyCode(), perpetual.getPerpetualHeaderV1().getBranchCode(),
                            perpetual.getPerpetualHeaderV1().getCycleCountNo(), 100L);
                    throw new RuntimeException(e);
                }
            }
            log.info("There is no Perpetual record found to process (sql) ...Waiting..");
        return null;
    }

    //======================================StockCount_Periodic=======================================================
    public WarehouseApiResponse processPeriodicOrder() throws IllegalAccessException, InvocationTargetException {

        List<PeriodicHeader> periodicHeaders = periodicHeaderRepository.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("Order Received On periodicHeaders: " + periodicHeaders);
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
                try {
                    log.info("Periodic Order Number: " + periodic.getPeriodicHeaderV1().getCycleCountNo());
                    WarehouseApiResponse response = periodicService.postPeriodicOrder(periodic);
                    if (response != null) {

                        //Updating the Processed Status = 10
                        periodicService.updateProcessedPeriodicOrder(periodic.getPeriodicHeaderV1().getMiddlewareId(),
                                periodic.getPeriodicHeaderV1().getCompanyCode(), periodic.getPeriodicHeaderV1().getBranchCode(),
                                periodic.getPeriodicHeaderV1().getCycleCountNo(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on StockCount processing: " + e.toString());

                    //Integration Log
                    integrationLogService.createPeriodicLog(periodic, e.toString());

                    //Updating the Processed Status
                    periodicService.updateProcessedPeriodicOrder(periodic.getPeriodicHeaderV1().getMiddlewareId(),
                            periodic.getPeriodicHeaderV1().getCompanyCode(), periodic.getPeriodicHeaderV1().getBranchCode(),
                            periodic.getPeriodicHeaderV1().getCycleCountNo(), 100L);
                    throw new RuntimeException(e);
                }
            }
        return null;
    }

    //=======================================================V2============================================================

    public WarehouseApiResponse processStockAdjustmentOrder() throws IllegalAccessException, InvocationTargetException {

        List<com.almailem.ams.api.connector.model.stockadjustment.StockAdjustment> stockAdjustments = stockAdjustmentRepo.findByProcessedStatusIdOrderByOrderReceivedOn(0L);
            log.info("StockAdjustment Found: " + stockAdjustments);
            StockAdjustment stockAdjustment = new StockAdjustment();
            for (com.almailem.ams.api.connector.model.stockadjustment.StockAdjustment dbSA : stockAdjustments) {

                stockAdjustment.setCompanyCode(dbSA.getCompanyCode());
                stockAdjustment.setBranchCode(dbSA.getBranchCode());
                stockAdjustment.setBranchName(dbSA.getBranchName());
                stockAdjustment.setDateOfAdjustment(dbSA.getDateOfAdjustment());
                stockAdjustment.setIsDamage(dbSA.getIsDamage());
                if(dbSA.getIsDamage() != null) {
	                if(dbSA.getIsDamage().equalsIgnoreCase("Y")) {
	                    stockAdjustment.setIsCycleCount("N");
	                }
	                if(dbSA.getIsDamage().equalsIgnoreCase("N")) {
	                    stockAdjustment.setIsCycleCount("Y");
	                }
            	}
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

                try {
                    log.info("Item Code: " + stockAdjustment.getItemCode());
                    WarehouseApiResponse response = stockAdjustmentService.postStockAdjustment(stockAdjustment);
                    if (response != null) {
                        // Updating the Processed Status = 10
                        stockAdjustmentService.updateProcessedStockAdjustment(stockAdjustment.getStockAdjustmentId(),
                                stockAdjustment.getCompanyCode(), stockAdjustment.getBranchCode(), stockAdjustment.getItemCode(), 10L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on Stock Adjustment processing : " + e.toString());

                    //Integration Log
                    integrationLogService.createStockAdjustment(stockAdjustment, e.toString());

                    // Updating the Processed Status = 100
                    stockAdjustmentService.updateProcessedStockAdjustment(stockAdjustment.getStockAdjustmentId(),
                            stockAdjustment.getCompanyCode(), stockAdjustment.getBranchCode(), stockAdjustment.getItemCode(), 100L);
                    throw new RuntimeException(e);
                }
            }
        return null;
    }

}