package com.almailem.ams.api.connector.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import com.almailem.ams.api.connector.service.ScheduleAsyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchJobScheduler {

    @Autowired
    ScheduleAsyncService scheduleAsyncService;

    //-------------------------------------------------------------------------------------------

    @Scheduled(fixedDelay = 20000)
    public void scheduleJob() throws InterruptedException, InvocationTargetException, IllegalAccessException {

        CompletableFuture<WarehouseApiResponse> supplierInvoice = scheduleAsyncService.scheduleSupplierInvoice();
        CompletableFuture<WarehouseApiResponse> stockReceipt = scheduleAsyncService.scheduleStockReceipt();
        CompletableFuture<WarehouseApiResponse> salesReturn = scheduleAsyncService.scheduleSalesReturn();
        CompletableFuture<WarehouseApiResponse> iwtTransfer = scheduleAsyncService.scheduleIWTTransfer();

        CompletableFuture<WarehouseApiResponse> salesInvoice = scheduleAsyncService.scheduleOutboundSalesInvoice();
        CompletableFuture<WarehouseApiResponse> salesOrder = scheduleAsyncService.scheduleOutboundSalesOrder();
        CompletableFuture<WarehouseApiResponse> purchaseReturn = scheduleAsyncService.scheduleOutboundPurchaseReturn();
        CompletableFuture<WarehouseApiResponse> transferOut = scheduleAsyncService.scheduleOutboundIWTTransfer();

        CompletableFuture<WarehouseApiResponse> itemMaster = scheduleAsyncService.scheduleItemMaster();
        CompletableFuture<WarehouseApiResponse> customerMaster = scheduleAsyncService.scheduleCustomerMaster();

        CompletableFuture<WarehouseApiResponse> scPerpetual = scheduleAsyncService.schedulePerpetual();
        CompletableFuture<WarehouseApiResponse> scPeriodic = scheduleAsyncService.schedulePeriodic();

        CompletableFuture<WarehouseApiResponse> stockAdjustment = scheduleAsyncService.scheduleStockAdjustment();

//        CompletableFuture.allOf(supplierInvoice,stockReceipt,salesReturn,b2bTransfer,iwtTransfer).join();

    }

}