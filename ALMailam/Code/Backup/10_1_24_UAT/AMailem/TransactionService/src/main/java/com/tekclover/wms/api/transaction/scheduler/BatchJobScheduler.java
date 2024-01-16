package com.tekclover.wms.api.transaction.scheduler;

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.v2.PeriodicHeaderEntityV2;
import com.tekclover.wms.api.transaction.model.cyclecount.periodic.v2.PeriodicHeaderV2;
import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2.PerpetualHeaderEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.InboundIntegrationHeader;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.InboundIntegrationLine;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.OutboundIntegrationHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.OutboundIntegrationLineV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundHeaderV2;
import com.tekclover.wms.api.transaction.model.warehouse.cyclecount.CycleCountHeader;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.OutboundOrderLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.v2.OutboundOrderV2;
import com.tekclover.wms.api.transaction.model.warehouse.stockAdjustment.StockAdjustment;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.service.*;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class BatchJobScheduler {
    @Autowired
    private OutboundOrderV2Repository outboundOrderV2Repository;

    @Autowired
    PreInboundHeaderService preinboundheaderService;

    @Autowired
    PreOutboundHeaderService preOutboundHeaderService;

    @Autowired
    ReportsService reportsService;

    @Autowired
    OrderService orderService;

    @Autowired
    PerpetualHeaderService perpetualHeaderService;

    @Autowired
    PeriodicHeaderService periodicHeaderService;

    @Autowired
    StockAdjustmentMiddlewareService stockAdjustmentMiddlewareService;

    @Autowired
    StockAdjustmentService stockAdjustmentService;

    //-------------------------------------------------------------------------------------------

    @Autowired
    InboundOrderRepository inboundOrderRepository;

    @Autowired
    OutboundOrderRepository outboundOrderRepository;

    @Autowired
    StockAdjustmentMiddlewareRepository stockAdjustmentRepository;

    //-------------------------------------------------------------------------------------------

    @Autowired
    InboundOrderV2Repository inboundOrderV2Repository;

    @Autowired
    InboundOrderLinesV2Repository inboundOrderLinesV2Repository;

    @Autowired
    CycleCountHeaderRepository cycleCountHeaderRepository;

    @Autowired
    CycleCountService cycleCountService;
    //-------------------------------------------------------------------------------------------

    List<InboundIntegrationHeader> inboundList = null;
    //    List<OutboundIntegrationHeader> outboundList = null;
    List<OutboundIntegrationHeaderV2> outboundList = null;
    List<CycleCountHeader> stockCountPerpetualList = null;
    List<CycleCountHeader> stockCountPeriodicList = null;
    List<StockAdjustment> stockAdjustmentList = null;
    static CopyOnWriteArrayList<InboundIntegrationHeader> spList = null;            // Inbound List
    //    static CopyOnWriteArrayList<OutboundIntegrationHeader> spOutboundList = null;    // Outbound List
    static CopyOnWriteArrayList<OutboundIntegrationHeaderV2> spOutboundList = null;    // Outbound List
    static CopyOnWriteArrayList<CycleCountHeader> scPerpetualList = null;    // StockCount List
    static CopyOnWriteArrayList<CycleCountHeader> scPeriodicList = null;    // StockCount List
    static CopyOnWriteArrayList<StockAdjustment> stockAdjustments = null;    // StockAdjustment List

    // Schedule Report
//	@Scheduled(cron = "0 0/5 6 * * *") // 0 0 */2 * * ?
//	@Scheduled(cron = "0 0 */1 * * ?")
//	public void scheduleInvReport() throws IllegalAccessException, InvocationTargetException {
//		reportsService.exportXlsxFile();
//	}

    //=======================================SQL-Processing==========================================================================
//	@Scheduled(fixedDelay = 50000)
//	public void processInboundOrder() throws IllegalAccessException, InvocationTargetException {
//		if (inboundList == null || inboundList.isEmpty()) {
//			List<InboundOrder> sqlInboundList = inboundOrderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
//			inboundList = new ArrayList<>();
//			for (InboundOrder dbOBOrder : sqlInboundList) {
//				InboundIntegrationHeader inboundIntegrationHeader = new InboundIntegrationHeader();
//				BeanUtils.copyProperties(dbOBOrder, inboundIntegrationHeader, CommonUtils.getNullPropertyNames(dbOBOrder));
//				inboundIntegrationHeader.setId(dbOBOrder.getOrderId());
//				List<InboundIntegrationLine> inboundIntegrationLineList = new ArrayList<>();
//				for (InboundOrderLines line : dbOBOrder.getLines()) {
//					InboundIntegrationLine inboundIntegrationLine = new InboundIntegrationLine();
//					BeanUtils.copyProperties(line, inboundIntegrationLine, CommonUtils.getNullPropertyNames(line));
//					inboundIntegrationLineList.add(inboundIntegrationLine);
//				}
//				inboundIntegrationHeader.setInboundIntegrationLine (inboundIntegrationLineList);
//				inboundList.add(inboundIntegrationHeader);
//			}
//			spList = new CopyOnWriteArrayList<InboundIntegrationHeader>(inboundList);
//			log.info("There is no record found to process (sql) ...Waiting..");
//		}
//
//		if (inboundList != null) {
//			log.info("Latest InboundOrder found: " + inboundList);
//			for (InboundIntegrationHeader inbound : spList) {
//				try {
//					log.info("InboundOrder ID : " + inbound.getRefDocumentNo());
//					InboundHeader inboundHeader = preinboundheaderService.processInboundReceived(inbound.getRefDocumentNo(), inbound);
//					if (inboundHeader != null) {
//						// Updating the Processed Status
//						orderService.updateProcessedInboundOrder(inbound.getRefDocumentNo());
//						inboundList.remove(inbound);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.error("Error on inbound processing : " + e.toString());
//					// Updating the Processed Status
//					orderService.updateProcessedInboundOrder(inbound.getRefDocumentNo());
//					preinboundheaderService.createInboundIntegrationLog(inbound);
//					inboundList.remove(inbound);
//				}
//			}
//		}
//	}
    //-----------------------------------------------------------------------------------------------------------------------------
    // OutboundRecord
    //    @Scheduled(fixedDelay = 50000)
//    public void processOutboundOrder() throws IllegalAccessException, InvocationTargetException, ParseException {
//        if (outboundList == null || outboundList.isEmpty()) {
//            List<OutboundOrder> sqlOutboundList = outboundOrderRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
//            outboundList = new ArrayList<>();
//            for (OutboundOrder dbOBOrder : sqlOutboundList) {
//                OutboundIntegrationHeader outboundIntegrationHeader = new OutboundIntegrationHeader();
//                BeanUtils.copyProperties(dbOBOrder, outboundIntegrationHeader, CommonUtils.getNullPropertyNames(dbOBOrder));
//                outboundIntegrationHeader.setId(dbOBOrder.getOrderId());
//                List<OutboundIntegrationLine> outboundIntegrationLineList = new ArrayList<>();
//                for (OutboundOrderLine line : dbOBOrder.getLines()) {
//                    OutboundIntegrationLine outboundIntegrationLine = new OutboundIntegrationLine();
//                    BeanUtils.copyProperties(line, outboundIntegrationLine, CommonUtils.getNullPropertyNames(line));
//                    outboundIntegrationLineList.add(outboundIntegrationLine);
//                }
//                outboundIntegrationHeader.setOutboundIntegrationLine(outboundIntegrationLineList);
//                outboundList.add(outboundIntegrationHeader);
//            }
//            spOutboundList = new CopyOnWriteArrayList<OutboundIntegrationHeader>(outboundList);
//            log.info("There is no record found to process (sql) ...Waiting..");
//        }
//
//        if (outboundList != null) {
//            log.info("Latest OutboundOrder found: " + outboundList);
//            for (OutboundIntegrationHeader outbound : spOutboundList) {
//                try {
//                    log.info("OutboundOrder ID : " + outbound.getRefDocumentNo());
//                    OutboundHeader outboundHeader = preOutboundHeaderService.processOutboundReceived(outbound);
//                    if (outboundHeader != null) {
//                        // Updating the Processed Status
//                        orderService.updateProcessedOrder(outbound.getRefDocumentNo());
//                        outboundList.remove(outbound);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error("Error on outbound processing : " + e.toString());
//                    // Updating the Processed Status
//                    orderService.updateProcessedOrder(outbound.getRefDocumentNo());
//                    preOutboundHeaderService.createOutboundIntegrationLog(outbound);
//                    outboundList.remove(outbound);
//                }
//            }
//        }
//    }
    //=============================================================================================================================
    @Scheduled(fixedDelay = 30000)
    public void processInboundOrder() throws IllegalAccessException, InvocationTargetException, ParseException {
        if (inboundList == null || inboundList.isEmpty()) {
            List<InboundOrderV2> sqlInboundList = inboundOrderV2Repository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            inboundList = new ArrayList<>();
            for (InboundOrderV2 dbOBOrder : sqlInboundList) {
                InboundIntegrationHeader inboundIntegrationHeader = new InboundIntegrationHeader();
                BeanUtils.copyProperties(dbOBOrder, inboundIntegrationHeader, CommonUtils.getNullPropertyNames(dbOBOrder));
                inboundIntegrationHeader.setId(dbOBOrder.getOrderId());
                inboundIntegrationHeader.setMiddlewareId(String.valueOf(dbOBOrder.getMiddlewareId()));
                inboundIntegrationHeader.setMiddlewareTable(dbOBOrder.getMiddlewareTable());

                List<InboundOrderLinesV2> sqlInboundLineList = inboundOrderLinesV2Repository.getOrderLines(dbOBOrder.getOrderId());
                log.info("line list: " + sqlInboundLineList);
                List<InboundIntegrationLine> inboundIntegrationLineList = new ArrayList<>();
                for (InboundOrderLinesV2 line : sqlInboundLineList) {
                    InboundIntegrationLine inboundIntegrationLine = new InboundIntegrationLine();
                    BeanUtils.copyProperties(line, inboundIntegrationLine, CommonUtils.getNullPropertyNames(line));

                    inboundIntegrationLine.setLineReference(line.getLineReference());
                    inboundIntegrationLine.setItemCode(line.getItemCode());
                    inboundIntegrationLine.setItemText(line.getItemText());
                    inboundIntegrationLine.setInvoiceNumber(line.getInvoiceNumber());
                    inboundIntegrationLine.setContainerNumber(line.getContainerNumber());
                    inboundIntegrationLine.setSupplierCode(line.getSupplierCode());
                    inboundIntegrationLine.setSupplierPartNumber(line.getSupplierPartNumber());
                    inboundIntegrationLine.setManufacturerName(line.getManufacturerName());
                    inboundIntegrationLine.setManufacturerPartNo(line.getManufacturerPartNo());
                    inboundIntegrationLine.setExpectedDate(line.getExpectedDate());
                    inboundIntegrationLine.setOrderedQty(line.getExpectedQty());
                    inboundIntegrationLine.setUom(line.getUom());
                    inboundIntegrationLine.setItemCaseQty(line.getItemCaseQty());
                    inboundIntegrationLine.setSalesOrderReference(line.getSalesOrderReference());
                    inboundIntegrationLine.setManufacturerCode(line.getManufacturerCode());
                    inboundIntegrationLine.setOrigin(line.getOrigin());
                    inboundIntegrationLine.setBrand(line.getBrand());

                    inboundIntegrationLine.setSupplierName(line.getSupplierName());

                    inboundIntegrationLine.setMiddlewareId(String.valueOf(line.getMiddlewareId()));
                    inboundIntegrationLine.setMiddlewareHeaderId(String.valueOf(line.getMiddlewareHeaderId()));
                    inboundIntegrationLine.setMiddlewareTable(line.getMiddlewareTable());
                    inboundIntegrationLine.setManufacturerFullName(line.getManufacturerFullName());
                    inboundIntegrationLine.setPurchaseOrderNumber(line.getPurchaseOrderNumber());
                    inboundIntegrationLine.setContainerNumber(line.getContainerNumber());
                    inboundIntegrationHeader.setContainerNo(line.getContainerNumber());

                    inboundIntegrationLineList.add(inboundIntegrationLine);
                }
                inboundIntegrationHeader.setInboundIntegrationLine(inboundIntegrationLineList);
                inboundList.add(inboundIntegrationHeader);
            }
            spList = new CopyOnWriteArrayList<InboundIntegrationHeader>(inboundList);
            log.info("There is no record found to process (sql) ...Waiting..");
        }

        if (inboundList != null) {
            log.info("Latest InboundOrder found: " + inboundList);
            for (InboundIntegrationHeader inbound : spList) {
                try {
                    log.info("InboundOrder ID : " + inbound.getRefDocumentNo());
                    InboundHeaderV2 inboundHeader = preinboundheaderService.processInboundReceivedV2(inbound.getRefDocumentNo(), inbound);
                    if (inboundHeader != null) {
                        // Updating the Processed Status
                        orderService.updateProcessedInboundOrderV2(inbound.getRefDocumentNo());
                        inboundList.remove(inbound);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on inbound processing : " + e.toString());
                    // Updating the Processed Status
                    orderService.updateProcessedInboundOrderV2(inbound.getRefDocumentNo());
                    preinboundheaderService.createInboundIntegrationLog(inbound);
                    inboundList.remove(inbound);
                }
            }
        }
    }

    //=====================================================================V2=============================================================================
    // OutboundRecord
    @Scheduled(fixedDelay = 50000)
    public void processOutboundOrder() throws IllegalAccessException, InvocationTargetException, ParseException {
        if (outboundList == null || outboundList.isEmpty()) {
            List<OutboundOrderV2> sqlOutboundList = outboundOrderV2Repository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
            outboundList = new ArrayList<>();
            for (OutboundOrderV2 dbOBOrder : sqlOutboundList) {
                OutboundIntegrationHeaderV2 outboundIntegrationHeader = new OutboundIntegrationHeaderV2();
                BeanUtils.copyProperties(dbOBOrder, outboundIntegrationHeader, CommonUtils.getNullPropertyNames(dbOBOrder));
                outboundIntegrationHeader.setId(dbOBOrder.getOrderId());
                outboundIntegrationHeader.setCompanyCode(dbOBOrder.getCompanyCode());
                outboundIntegrationHeader.setBranchCode(dbOBOrder.getBranchCode());
                outboundIntegrationHeader.setReferenceDocumentType(dbOBOrder.getRefDocumentType());
                outboundIntegrationHeader.setMiddlewareId(dbOBOrder.getMiddlewareId());
                outboundIntegrationHeader.setMiddlewareTable(dbOBOrder.getMiddlewareTable());
                outboundIntegrationHeader.setReferenceDocumentType(dbOBOrder.getRefDocumentType());
                outboundIntegrationHeader.setSalesOrderNumber(dbOBOrder.getSalesOrderNumber());
                outboundIntegrationHeader.setPickListNumber(dbOBOrder.getPickListNumber());
                outboundIntegrationHeader.setTokenNumber(dbOBOrder.getTokenNumber());
                outboundIntegrationHeader.setTargetCompanyCode(dbOBOrder.getTargetCompanyCode());
                outboundIntegrationHeader.setTargetBranchCode(dbOBOrder.getTargetBranchCode());
                if(dbOBOrder.getOutboundOrderTypeID() == 3L) {
                    outboundIntegrationHeader.setStatus(dbOBOrder.getPickListStatus());
                    outboundIntegrationHeader.setRequiredDeliveryDate(dbOBOrder.getRequiredDeliveryDate());
                }
                if (dbOBOrder.getOutboundOrderTypeID() != 3L) {
                    outboundIntegrationHeader.setStatus(dbOBOrder.getStatus());
                }


                if (outboundIntegrationHeader.getOutboundOrderTypeID() == 4) {
                    outboundIntegrationHeader.setSalesInvoiceNumber(dbOBOrder.getSalesInvoiceNumber());
                    outboundIntegrationHeader.setSalesOrderNumber(dbOBOrder.getSalesOrderNumber());
                    outboundIntegrationHeader.setRequiredDeliveryDate(dbOBOrder.getSalesInvoiceDate());
                    outboundIntegrationHeader.setDeliveryType(dbOBOrder.getDeliveryType());
                    outboundIntegrationHeader.setCustomerId(dbOBOrder.getCustomerId());
                    outboundIntegrationHeader.setCustomerName(dbOBOrder.getCustomerName());
                    outboundIntegrationHeader.setAddress(dbOBOrder.getAddress());
                    outboundIntegrationHeader.setPhoneNumber(dbOBOrder.getPhoneNumber());
                    outboundIntegrationHeader.setAlternateNo(dbOBOrder.getAlternateNo());
                    outboundIntegrationHeader.setStatus(dbOBOrder.getStatus());
                }

                List<OutboundIntegrationLineV2> outboundIntegrationLineList = new ArrayList<>();
                for (OutboundOrderLineV2 line : dbOBOrder.getLine()) {
                    OutboundIntegrationLineV2 outboundIntegrationLine = new OutboundIntegrationLineV2();
                    BeanUtils.copyProperties(line, outboundIntegrationLine, CommonUtils.getNullPropertyNames(line));
                    outboundIntegrationLine.setCompanyCode(line.getFromCompanyCode());
                    outboundIntegrationLine.setBranchCode(line.getSourceBranchCode());
                    outboundIntegrationLine.setManufacturerName(line.getManufacturerName());
                    outboundIntegrationLine.setManufacturerCode(line.getManufacturerName());
                    outboundIntegrationLine.setMiddlewareId(line.getMiddlewareId());
                    outboundIntegrationLine.setMiddlewareHeaderId(line.getMiddlewareHeaderId());
                    outboundIntegrationLine.setMiddlewareTable(line.getMiddlewareTable());
                    outboundIntegrationLine.setSalesInvoiceNo(line.getSalesInvoiceNo());
                    outboundIntegrationLine.setReferenceDocumentType(dbOBOrder.getRefDocumentType());
                    outboundIntegrationLine.setRefField1ForOrderType(line.getRefField1ForOrderType());
                    outboundIntegrationLine.setSalesOrderNumber(line.getSalesOrderNo());
                    outboundIntegrationLine.setSupplierInvoiceNo(line.getSupplierInvoiceNo());
                    outboundIntegrationLine.setPickListNo(line.getPickListNo());
                    outboundIntegrationLine.setManufacturerFullName(line.getManufacturerFullName());
                    outboundIntegrationLineList.add(outboundIntegrationLine);
                }
                outboundIntegrationHeader.setOutboundIntegrationLines(outboundIntegrationLineList);
                outboundList.add(outboundIntegrationHeader);
            }
            spOutboundList = new CopyOnWriteArrayList<OutboundIntegrationHeaderV2>(outboundList);
            log.info("There is no record found to process (sql) ...Waiting..");
        }

        if (outboundList != null) {
            log.info("Latest OutboundOrder found: " + outboundList);
            for (OutboundIntegrationHeaderV2 outbound : spOutboundList) {
                try {
                    log.info("OutboundOrder ID : " + outbound.getRefDocumentNo());
                    OutboundHeaderV2 outboundHeader = preOutboundHeaderService.processOutboundReceivedV2(outbound);
                    if (outboundHeader != null) {
                        // Updating the Processed Status
                        orderService.updateProcessedOrderV2(outbound.getRefDocumentNo());
                        outboundList.remove(outbound);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on outbound processing : " + e.toString());
                    // Updating the Processed Status
                    orderService.updateProcessedOrderV2(outbound.getRefDocumentNo());
                    preOutboundHeaderService.createOutboundIntegrationLogV2(outbound);
                    outboundList.remove(outbound);
                }
            }
        }
    }

    //=====================================================================StockCount=============================================================================
    // PerpetualCount
    @Scheduled(fixedDelay = 30000)
    public void processPerpetualStockCountOrder() throws IllegalAccessException, InvocationTargetException, ParseException {
        if (stockCountPerpetualList == null || stockCountPerpetualList.isEmpty()) {
            List<CycleCountHeader> scpList = cycleCountHeaderRepository.findTopByProcessedStatusIdAndStockCountTypeOrderByOrderReceivedOn(0L, "PERPETUAL");
//            stockCountPerpetualList = new ArrayList<>();
            stockCountPerpetualList = new CopyOnWriteArrayList<CycleCountHeader>(scpList);
            scPerpetualList = new CopyOnWriteArrayList<CycleCountHeader>(stockCountPerpetualList);
            log.info("stockCountPerpetualList : " + stockCountPerpetualList);
            log.info("There is no stock count record found to process (sql) ...Waiting..");
        }

        if (stockCountPerpetualList != null) {
            log.info("Latest Perpetual StockCount found: " + stockCountPerpetualList);
            for (CycleCountHeader stockCount : scPerpetualList) {
                try {
                    log.info("Perpetual StockCount CycleCountNo : " + stockCount.getCycleCountNo());
                    PerpetualHeaderEntityV2 perpetualStockCount = perpetualHeaderService.processStockCountReceived(stockCount);
                    if (perpetualStockCount != null) {
                        // Updating the Processed Status
                        cycleCountService.updateProcessedOrderV2(stockCount.getCycleCountNo());
                        stockCountPerpetualList.remove(stockCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on PerpetualStockCount processing : " + e.toString());
                    // Updating the Processed Status
                    cycleCountService.updateProcessedOrderV2(stockCount.getCycleCountNo());
//                    preOutboundHeaderService.createOutboundIntegrationLogV2(outbound);
                    stockCountPerpetualList.remove(stockCount);
                }
            }
        }
    }

    // PeriodicCount
    @Scheduled(fixedDelay = 30000)
    public void processPeriodicStockCountOrder() throws IllegalAccessException, InvocationTargetException, ParseException {if (stockCountPeriodicList == null || stockCountPeriodicList.isEmpty()) {
            List<CycleCountHeader> scpList = cycleCountHeaderRepository.findTopByProcessedStatusIdAndStockCountTypeOrderByOrderReceivedOn(0L, "PERIODIC");
            stockCountPeriodicList = new CopyOnWriteArrayList<CycleCountHeader>(scpList);
            scPeriodicList = new CopyOnWriteArrayList<CycleCountHeader>(stockCountPeriodicList);
            log.info("stockCountPeriodicList : " + stockCountPeriodicList);
            log.info("There is no Periodic stock count record found to process (sql) ...Waiting..");
        }

        if (stockCountPeriodicList != null) {
            log.info("Latest Periodic StockCount found: " + stockCountPeriodicList);
            for (CycleCountHeader stockCount : scPeriodicList) {
                try {
                    log.info("Periodic StockCount CycleCountNo : " + stockCount.getCycleCountNo());
                    PeriodicHeaderEntityV2 periodicHeaderV2 = periodicHeaderService.processStockCountReceived(stockCount);
                    if (periodicHeaderV2 != null) {
                        // Updating the Processed Status
                        cycleCountService.updateProcessedOrderV2(stockCount.getCycleCountNo());
                        stockCountPeriodicList.remove(stockCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on PeriodicStockCount processing : " + e.toString());
                    // Updating the Processed Status
                    cycleCountService.updateProcessedOrderV2(stockCount.getCycleCountNo());
//                    preOutboundHeaderService.createOutboundIntegrationLogV2(outbound);
                    stockCountPeriodicList.remove(stockCount);
                }
            }
        }
    }

    //=====================================================================StockAdjustment=============================================================================
    // PerpetualCount
    @Scheduled(fixedDelay = 30000)
    public void processStockAdjustmentOrder() throws IllegalAccessException, InvocationTargetException, ParseException {
        if (stockAdjustmentList == null || stockAdjustmentList.isEmpty()) {
            List<StockAdjustment> saList = stockAdjustmentRepository.findTopByProcessedStatusIdOrderByOrderReceivedOn(0L);
//            stockCountPerpetualList = new ArrayList<>();
            stockAdjustmentList = new CopyOnWriteArrayList<StockAdjustment>(saList);
            stockAdjustments = new CopyOnWriteArrayList<StockAdjustment>(stockAdjustmentList);
            log.info("stockAdjustmentList : " + stockAdjustmentList);
            log.info("There is no stock adjustment record found to process (sql) ...Waiting..");
        }

        if (stockAdjustmentList != null) {
            log.info("Latest StockAdjustment found: " + stockAdjustmentList);
            for (StockAdjustment stockAdjustment : stockAdjustments) {
                try {
                    log.info("StockAdjustment Id : " + stockAdjustment.getStockAdjustmentId());
                    WarehouseApiResponse dbStockAdjustment = stockAdjustmentService.processStockAdjustment(stockAdjustment);
                    if (dbStockAdjustment != null) {
                        // Updating the Processed Status
                        stockAdjustmentMiddlewareService.updateProcessedOrderV2(stockAdjustment.getStockAdjustmentId());
                        stockAdjustmentList.remove(stockAdjustment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error on StockAdjustment processing : " + e.toString());
                    // Updating the Processed Status
                    stockAdjustmentMiddlewareService.updateProcessedOrderV2(stockAdjustment.getStockAdjustmentId());
//                    preOutboundHeaderService.createOutboundIntegrationLogV2(outbound);
                    stockAdjustmentList.remove(stockAdjustment);
                }
            }
        }
    }
}