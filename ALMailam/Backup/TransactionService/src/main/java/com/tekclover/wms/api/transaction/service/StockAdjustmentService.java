package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.model.warehouse.stockAdjustment.StockAdjustment;
import com.tekclover.wms.api.transaction.repository.StockAdjustmentRepository;
import com.tekclover.wms.api.transaction.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class StockAdjustmentService extends BaseService {
    @Autowired
    StockAdjustmentRepository stockAdjustmentRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    /**
     * @param stockAdjustment
     * @return
     */
    public StockAdjustment createStockAdjustment(StockAdjustment stockAdjustment) {

        if (stockAdjustment != null) {
            StockAdjustment createStockAdjustment = stockAdjustmentRepository.save(stockAdjustment);
            return createStockAdjustment;
        }
        return null;
    }

    /**
     * @param stockAdjustmentId
     */
    public void updateProcessedOrderV2(Long stockAdjustmentId) {
        StockAdjustment dbStockAdjustment = stockAdjustmentRepository.findByStockAdjustmentId(stockAdjustmentId);
        log.info("StockAdjustmentId : " + stockAdjustmentId);
        log.info("dbStockAdjustmentId : " + dbStockAdjustment);
        if (dbStockAdjustment != null) {
            dbStockAdjustment.setProcessedStatusId(10L);
            dbStockAdjustment.setOrderProcessedOn(new Date());
            stockAdjustmentRepository.save(dbStockAdjustment);
        }
    }

    /**
     * @param stockAdjustment
     * @return
     */
    public StockAdjustment processStockAdjustment(StockAdjustment stockAdjustment) {

        StockAdjustment newStockAdjustment = new StockAdjustment();

//        List<InventoryV2> db


        return newStockAdjustment;
    }

}