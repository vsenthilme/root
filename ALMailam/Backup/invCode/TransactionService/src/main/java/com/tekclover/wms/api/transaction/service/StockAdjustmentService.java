package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.cyclecount.stockadjustment.StockAdjustment;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.SearchInventoryV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.PreOutboundLineV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.repository.InventoryV2Repository;
import com.tekclover.wms.api.transaction.repository.StockAdjustmentRepository;
import com.tekclover.wms.api.transaction.repository.specification.StockAdjustmentSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class StockAdjustmentService extends BaseService {
    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepository;

    @Autowired
    InventoryService inventoryService;


    /**
     * @param stockAdjustment
     * @return
     */
    public WarehouseApiResponse processStockAdjustment(com.tekclover.wms.api.transaction.model.warehouse.stockAdjustment.StockAdjustment stockAdjustment) {

        WarehouseApiResponse warehouseApiResponse = new WarehouseApiResponse();

        StockAdjustment createStockAdjustment = null;

        List<InventoryV2> inventoryList = inventoryService.getInventoryForInhouseTransferV2(stockAdjustment.getCompanyCode(),
                stockAdjustment.getBranchCode(),
                "EN",
                stockAdjustment.getWarehouseId(),
                stockAdjustment.getItemCode(),
                stockAdjustment.getManufacturerName());
        log.info("Inventory List: " + inventoryList);

        if (inventoryList != null) {
            for (InventoryV2 dbInventory : inventoryList) {
                StockAdjustment dbStockAdjustment = new StockAdjustment();
                BeanUtils.copyProperties(dbInventory, dbStockAdjustment, CommonUtils.getNullPropertyNames(dbInventory));

                dbStockAdjustment.setAdjustmentQty(stockAdjustment.getAdjustmentQty());
                dbStockAdjustment.setCompanyCode(stockAdjustment.getCompanyCode());
                dbStockAdjustment.setDateOfAdjustment(stockAdjustment.getDateOfAdjustment());
                dbStockAdjustment.setUnitOfMeasure(stockAdjustment.getUnitOfMeasure());
                dbStockAdjustment.setRemarks(stockAdjustment.getRemarks());
                dbStockAdjustment.setAmsReferenceNo(stockAdjustment.getAmsReferenceNo());
                dbStockAdjustment.setIsCycleCount(stockAdjustment.getIsCycleCount());
                dbStockAdjustment.setIsCompleted(stockAdjustment.getIsCompleted());
                dbStockAdjustment.setIsDamage(stockAdjustment.getIsDamage());
                dbStockAdjustment.setMiddlewareId(stockAdjustment.getMiddlewareId());
                dbStockAdjustment.setMiddlewareTable(stockAdjustment.getMiddlewareTable());
                dbStockAdjustment.setSaUpdatedOn(stockAdjustment.getUpdatedOn());

                dbStockAdjustment.setDeletionIndicator(0L);
                dbStockAdjustment.setCreatedOn(new Date());
                dbStockAdjustment.setCreatedBy("MSD_INT");

                createStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
            }
        }

        if (createStockAdjustment != null) {
            warehouseApiResponse.setStatusCode("200");
            warehouseApiResponse.setMessage("Success");
            return warehouseApiResponse;
        }
        return null;
    }

    /**
     * @param searchStockAdjustment
     * @return
     * @throws ParseException
     */
    public Stream<StockAdjustment> findStockAdjustment(SearchInventoryV2 searchStockAdjustment)
            throws ParseException {
        StockAdjustmentSpecification spec = new StockAdjustmentSpecification(searchStockAdjustment);
        Stream<StockAdjustment> results = stockAdjustmentRepository.stream(spec, StockAdjustment.class);
//        List<StockAdjustment> results = stockAdjustmentRepository.findAll(spec);
        return results;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentId
     * @param storageBin
     * @param updateStockAdjustment
     * @param loginUserId
     * @return
     */
    public StockAdjustment updateStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                 String itemCode, String manufacturerName, Long stockAdjustmentId, String storageBin,
                                                 StockAdjustment updateStockAdjustment, String loginUserId) throws InvocationTargetException, IllegalAccessException {
        StockAdjustment dbStockAdjustment =
                stockAdjustmentRepository.
                        findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentIdAndDeletionIndicator(
                                languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, storageBin, stockAdjustmentId, 0L);
        if (dbStockAdjustment != null) {
            BeanUtils.copyProperties(updateStockAdjustment, dbStockAdjustment, CommonUtils.getNullPropertyNames(updateStockAdjustment));
            dbStockAdjustment.setUpdatedBy(loginUserId);
            dbStockAdjustment.setUpdatedOn(new Date());
            StockAdjustment updatedStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
            log.info("updatedStockAdjustment: " + updatedStockAdjustment);

            if(updatedStockAdjustment != null) {
                if (updatedStockAdjustment.getAdjustmentQty() != null) {

                    InventoryV2 newInventoryV2 = new InventoryV2();
                    BeanUtils.copyProperties(updateStockAdjustment, newInventoryV2, CommonUtils.getNullPropertyNames(updateStockAdjustment));
                    newInventoryV2.setInventoryQuantity(updatedStockAdjustment.getInventoryQuantity()+ updatedStockAdjustment.getAdjustmentQty());
                    InventoryV2 createdInventoryV2 = inventoryV2Repository.save(newInventoryV2);
                    log.info("InventoryV2 created : " + createdInventoryV2);

//                    InventoryV2 inventoryV2 = new InventoryV2();
//                    BeanUtils.copyProperties(updateStockAdjustment, inventoryV2, CommonUtils.getNullPropertyNames(updateStockAdjustment));
//                    InventoryV2 updateInventory = inventoryService.updateInventoryV2(
//                            companyCodeId, plantId, languageId, warehouseId, updatedStockAdjustment.getPackBarcodes(), itemCode,
//                            storageBin, updatedStockAdjustment.getStockTypeId(), updateStockAdjustment.getSpecialStockIndicatorId(),
//                            inventoryV2, loginUserId);
//                    log.info("Inventory Updated: " + updateInventory);
                }
            }
        }
        throw new BadRequestException("The Given StockAdjustmentId doesn't Exist: " + stockAdjustmentId);
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentId
     * @param storageBin
     * @return
     */
    public StockAdjustment getStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                 String itemCode, String manufacturerName, Long stockAdjustmentId, String storageBin) {
        StockAdjustment dbStockAdjustment =
                stockAdjustmentRepository.
                        findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentIdAndDeletionIndicator(
                                languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, storageBin, stockAdjustmentId, 0L);

        if (dbStockAdjustment != null) {

            log.info("StockAdjustment: " + dbStockAdjustment);

            return dbStockAdjustment;
        }
        throw new BadRequestException("The Given StockAdjustmentId doesn't Exist: " + stockAdjustmentId);
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentId
     * @param storageBin
     * @return
     */
    public void deleteStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId, String loginUserId,
                                                 String itemCode, String manufacturerName, Long stockAdjustmentId, String storageBin) {
        StockAdjustment dbStockAdjustment =
                stockAdjustmentRepository.
                        findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentIdAndDeletionIndicator(
                                languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, storageBin, stockAdjustmentId, 0L);

        if (dbStockAdjustment != null) {

            log.info("StockAdjustment: " + dbStockAdjustment);

            dbStockAdjustment.setDeletionIndicator(1L);
            dbStockAdjustment.setUpdatedBy(loginUserId);
            dbStockAdjustment.setUpdatedOn(new Date());

        }
        throw new BadRequestException("The Given StockAdjustmentId doesn't Exist: " + stockAdjustmentId);
    }

}