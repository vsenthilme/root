package com.tekclover.wms.api.transaction.scheduler;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class BatchJobScheduler {

    @Autowired
    ScheduleAsyncService scheduleAsyncService;

    //-------------------------------------------------------------------------------------------

    @Scheduled(fixedDelay = 30000)
    public void scheduleJob() throws InterruptedException, InvocationTargetException, IllegalAccessException, ParseException {

        CompletableFuture<WarehouseApiResponse> inboundOrder = scheduleAsyncService.processInboundOrder();
        CompletableFuture<WarehouseApiResponse> outboundOrder = scheduleAsyncService.processOutboundOrder();
        CompletableFuture<WarehouseApiResponse> perpetualStockCountOrder = scheduleAsyncService.processPerpetualStockCountOrder();
        CompletableFuture<WarehouseApiResponse> periodicStockCountOrder = scheduleAsyncService.processPeriodicStockCountOrder();
        CompletableFuture<WarehouseApiResponse> stockAdjustmentOrder = scheduleAsyncService.processStockAdjustmentOrder();

//        CompletableFuture.allOf(inboundOrder,outboundOrder,perpetualStockCountOrder,periodicStockCountOrder,stockAdjustmentOrder).join();

    }

}