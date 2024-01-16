package com.almailem.ams.api.connector.service;

import com.almailem.ams.api.connector.model.wms.WarehouseApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ScheduleAsyncService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    MastersService mastersService;

    //-------------------------------------------------------------------------------------------


    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleSupplierInvoice() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("supplierInvoice Started Processing");
        WarehouseApiResponse supplierInvoice = transactionService.processInboundOrder();
//        log.info("supplierInvoice finished Processing");
        return CompletableFuture.completedFuture(supplierInvoice);

    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleStockReceipt() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("stockReceipt Started Processing");
        WarehouseApiResponse stockReceipt = transactionService.processInboundOrderSR();
//        log.info("stockReceipt finished Processing");
        return CompletableFuture.completedFuture(stockReceipt);

    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleSalesReturn() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("salesReturn Started Processing");
        WarehouseApiResponse salesReturn = transactionService.processInboundOrderSRT();
//        log.info("salesReturn finished Processing");
        return CompletableFuture.completedFuture(salesReturn);

    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleIWTTransfer() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Transfer In Started Processing");
        WarehouseApiResponse iwtTransfer = transactionService.processInboundOrderIWT();
//        log.info("Transfer In finished Processing");
        return CompletableFuture.completedFuture(iwtTransfer);

    }

    //-------------------------------------------------------------------OUTBOUND---------------------------------------------------------------
    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleOutboundPurchaseReturn() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Purchase Return Started Processing");
        WarehouseApiResponse purReturn = transactionService.processOutboundOrderRPO();
//        log.info("Purchase Return finished Processing");
        return CompletableFuture.completedFuture(purReturn);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleOutboundSalesOrder() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("SalesOrder Started Processing");
        WarehouseApiResponse salesOrder = transactionService.processOutboundOrderPL();
//        log.info("SalesOrder finished Processing");
        return CompletableFuture.completedFuture(salesOrder);

    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleOutboundIWTTransfer() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Transfer Out Started Processing");
        WarehouseApiResponse transferOut = transactionService.processOutboundOrderIWT();
//        log.info("Transfer Out finished Processing");
        return CompletableFuture.completedFuture(transferOut);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleOutboundSalesInvoice() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Sales Invoice Started Processing");
        WarehouseApiResponse salesInvoice = transactionService.processOutboundOrderSI();
//        log.info("Sales Invoice finished Processing");
        return CompletableFuture.completedFuture(salesInvoice);
    }

    //-------------------------------------------------------------------MASTER---------------------------------------------------------------
    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleItemMaster() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Item Master Started Processing");
        WarehouseApiResponse itemMaster = mastersService.processItemMasterOrder();
//        log.info("Item Master finished Processing");
        return CompletableFuture.completedFuture(itemMaster);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleCustomerMaster() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Customer Master Started Processing");
        WarehouseApiResponse customerMaster = mastersService.processCustomerMasterOrder();
//        log.info("Customer Master finished Processing");
        return CompletableFuture.completedFuture(customerMaster);

    }
    //----------------------------------------------StockCount---------------------------------------------------------
    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> schedulePerpetual() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("StockCount Perpetual Started Processing");
        WarehouseApiResponse perpetual = transactionService.processPerpetualOrder();
//        log.info("StockCount Perpetual finished Processing");
        return CompletableFuture.completedFuture(perpetual);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> schedulePeriodic() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("StockCount Periodic Started Processing");
        WarehouseApiResponse periodic = transactionService.processPeriodicOrder();
//        log.info("StockCount Periodic finished Processing");
        return CompletableFuture.completedFuture(periodic);
    }

    //-----------------------------------------Stock_Adjustment--------------------------------------------------------
    @Async("asyncTaskExecutor")
    public CompletableFuture<WarehouseApiResponse> scheduleStockAdjustment() throws InterruptedException, InvocationTargetException, IllegalAccessException {

//        log.info("Stock Adjustment Started Processing");
        WarehouseApiResponse sa = transactionService.processStockAdjustmentOrder();
//        log.info("Stock Adjustment finished Processing");
        return CompletableFuture.completedFuture(sa);
    }
}