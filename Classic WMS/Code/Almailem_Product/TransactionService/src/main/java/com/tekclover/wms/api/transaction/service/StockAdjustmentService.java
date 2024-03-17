package com.tekclover.wms.api.transaction.service;

import com.opencsv.exceptions.CsvException;
import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.v2.PerpetualLineV2;
import com.tekclover.wms.api.transaction.model.cyclecount.stockadjustment.SearchStockAdjustment;
import com.tekclover.wms.api.transaction.model.cyclecount.stockadjustment.StockAdjustment;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData1;
import com.tekclover.wms.api.transaction.model.dto.StorageBinV2;
import com.tekclover.wms.api.transaction.model.inbound.gr.StorageBinPutAway;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.WarehouseApiResponse;
import com.tekclover.wms.api.transaction.repository.InventoryV2Repository;
import com.tekclover.wms.api.transaction.repository.StagingLineV2Repository;
import com.tekclover.wms.api.transaction.repository.StockAdjustmentRepository;
import com.tekclover.wms.api.transaction.repository.specification.StockAdjustmentSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class StockAdjustmentService extends BaseService {
    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;
    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepository;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PerpetualLineService perpetualLineService;

    @Autowired
    MastersService mastersService;

    String statusDescription = null;


    /**
     * @param stockAdjustment
     * @return
     */
    @Transactional
    public WarehouseApiResponse processStockAdjustment(com.tekclover.wms.api.transaction.model.warehouse.stockAdjustment.StockAdjustment stockAdjustment) throws java.text.ParseException, IOException, CsvException {

        WarehouseApiResponse warehouseApiResponse = new WarehouseApiResponse();

        //Code Changed to do stock Adjustment Automatically
        StockAdjustment createStockAdjustment = autoUpdateStockAdjustment(stockAdjustment);

//        StockAdjustment createStockAdjustment = null;
//        List<StockAdjustment> stockAdjustmentList = new ArrayList<>();
//
//        List<IInventoryImpl> inventoryList = inventoryService.getInventoryForStockAdjustment(stockAdjustment.getCompanyCode(),
//                stockAdjustment.getBranchCode(),
//                "EN",
//                stockAdjustment.getWarehouseId(),
//                stockAdjustment.getItemCode(),
//                stockAdjustment.getManufacturerName());
//        log.info("Inventory List: " + inventoryList);
//
//        if (inventoryList == null || inventoryList.isEmpty()) {
//            warehouseApiResponse.setStatusCode("1400");
//            warehouseApiResponse.setMessage("No Inventory Found");
//            return warehouseApiResponse;
//        }
//        if (inventoryList != null) {
//            Long stockAdjustmentKey = System.currentTimeMillis();
//            for (IInventoryImpl dbInventory : inventoryList) {
//                StockAdjustment dbStockAdjustment = new StockAdjustment();
//                BeanUtils.copyProperties(dbInventory, dbStockAdjustment, CommonUtils.getNullPropertyNames(dbInventory));
//
//                dbStockAdjustment.setAdjustmentQty(stockAdjustment.getAdjustmentQty());
//                dbStockAdjustment.setCompanyCode(stockAdjustment.getCompanyCode());
//                dbStockAdjustment.setBranchCode(stockAdjustment.getBranchCode());
//                if (dbInventory.getDescription() != null) {
//                    dbStockAdjustment.setItemDescription(dbInventory.getDescription());
//                }
//                if (dbInventory.getDescription() == null) {
//                    if (dbInventory.getReferenceField8() != null) {
//                        dbStockAdjustment.setItemDescription(dbInventory.getReferenceField8());
//                    }
//                }
//                dbStockAdjustment.setPackBarcodes(dbInventory.getPackBarcodes());
//                dbStockAdjustment.setBranchName(stockAdjustment.getBranchName());
//                dbStockAdjustment.setDateOfAdjustment(stockAdjustment.getDateOfAdjustment());
//                dbStockAdjustment.setUnitOfMeasure(stockAdjustment.getUnitOfMeasure());
//                dbStockAdjustment.setManufacturerCode(stockAdjustment.getManufacturerCode());
//                dbStockAdjustment.setItemDescription(stockAdjustment.getItemDescription());
//                dbStockAdjustment.setReferenceField1(stockAdjustment.getRefDocType());
//                dbStockAdjustment.setRemarks(stockAdjustment.getRemarks());
//                dbStockAdjustment.setAmsReferenceNo(stockAdjustment.getAmsReferenceNo());
//                dbStockAdjustment.setIsCycleCount(stockAdjustment.getIsCycleCount());
//                dbStockAdjustment.setIsCompleted(stockAdjustment.getIsCompleted());
//                dbStockAdjustment.setIsDamage(stockAdjustment.getIsDamage());
//                dbStockAdjustment.setMiddlewareId(stockAdjustment.getMiddlewareId());
//                dbStockAdjustment.setMiddlewareTable(stockAdjustment.getMiddlewareTable());
//                dbStockAdjustment.setSaUpdatedOn(stockAdjustment.getUpdatedOn());
//                dbStockAdjustment.setStockAdjustmentKey(stockAdjustmentKey);
//
//                dbStockAdjustment.setStatusId(87L);                 //Hard Code - StockAdjustment Created
//                statusDescription = stagingLineV2Repository.getStatusDescription(87L, dbInventory.getLanguageId());
//                dbStockAdjustment.setStatusDescription(statusDescription);
//
//                dbStockAdjustment.setDeletionIndicator(0L);
//                dbStockAdjustment.setCreatedOn(new Date());
//                dbStockAdjustment.setCreatedBy("MW_AMS");
//
//                createStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
//                stockAdjustmentList.add(dbStockAdjustment);
//            }
//        }

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
    public Stream<StockAdjustment> findStockAdjustment(SearchStockAdjustment searchStockAdjustment)
            throws ParseException {
        StockAdjustmentSpecification spec = new StockAdjustmentSpecification(searchStockAdjustment);
        Stream<StockAdjustment> results = stockAdjustmentRepository.stream(spec, StockAdjustment.class);
//        List<StockAdjustment> results = stockAdjustmentRepository.findAll(spec);
        return results;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentKey
     * @param updateStockAdjustmentList
     * @param loginUserId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<StockAdjustment> updateStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                       String itemCode, String manufacturerName, Long stockAdjustmentKey,
                                                       List<StockAdjustment> updateStockAdjustmentList, String loginUserId) throws InvocationTargetException, IllegalAccessException, java.text.ParseException {

        List<StockAdjustment> updatedStockAdjustmentList = new ArrayList<>();
        if (updateStockAdjustmentList != null) {
            for (StockAdjustment updateStockAdjustment : updateStockAdjustmentList) {

                StockAdjustment dbStockAdjustment =
                        stockAdjustmentRepository.
                                findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStockAdjustmentKeyAndStockAdjustmentIdAndDeletionIndicator(
                                        languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, stockAdjustmentKey, updateStockAdjustment.getStockAdjustmentId(), 0L);

                BeanUtils.copyProperties(updateStockAdjustment, dbStockAdjustment, CommonUtils.getNullPropertyNames(updateStockAdjustment));
                dbStockAdjustment.setUpdatedBy(loginUserId);
                dbStockAdjustment.setUpdatedOn(new Date());

                statusDescription = stagingLineV2Repository.getStatusDescription(dbStockAdjustment.getStatusId(), dbStockAdjustment.getLanguageId());
                dbStockAdjustment.setStatusDescription(statusDescription);

                StockAdjustment updatedStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
                log.info("updatedStockAdjustment: " + updatedStockAdjustment);
                updatedStockAdjustmentList.add(updatedStockAdjustment);

                if (updatedStockAdjustment != null) {
                    if (updatedStockAdjustment.getAdjustmentQty() != null) {
                        List<InventoryV2> dbInventoryList =
                                inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndManufacturerNameAndStorageBinAndStockTypeIdAndSpecialStockIndicatorIdAndDeletionIndicatorOrderByInventoryIdDesc(
                                        languageId, companyCodeId, plantId,
                                        warehouseId, updatedStockAdjustment.getPackBarcodes(), itemCode, manufacturerName,
                                        updatedStockAdjustment.getStorageBin(), updatedStockAdjustment.getStockTypeId(), dbStockAdjustment.getSpecialStockIndicatorId(), 0L);

                        if (dbInventoryList != null) {
                            InventoryV2 dbInventoryV2 = dbInventoryList.get(0);
                            InventoryV2 newInventoryV2 = new InventoryV2();
                            BeanUtils.copyProperties(dbInventoryV2, newInventoryV2, CommonUtils.getNullPropertyNames(dbInventoryV2));
                            newInventoryV2.setInventoryId(System.currentTimeMillis());
                            newInventoryV2.setInventoryQuantity(updatedStockAdjustment.getInventoryQuantity() + updatedStockAdjustment.getAdjustmentQty());
                            InventoryV2 createdInventoryV2 = inventoryV2Repository.save(newInventoryV2);
                            log.info("InventoryV2 created : " + createdInventoryV2);
                        }

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
        }
        if (updateStockAdjustmentList == null || updateStockAdjustmentList.isEmpty()) {
            throw new BadRequestException("The Given StockAdjustmentId doesn't Exist: " + stockAdjustmentKey);
        }
        return updatedStockAdjustmentList;
    }

    /**
     * @param stockAdjustment
     * @return
     * @throws java.text.ParseException
     */
    public StockAdjustment autoUpdateStockAdjustment(com.tekclover.wms.api.transaction.model.warehouse.stockAdjustment.StockAdjustment stockAdjustment) throws java.text.ParseException, IOException, CsvException {

        if (stockAdjustment.getIsCycleCount().equalsIgnoreCase("Y") && stockAdjustment.getIsDamage().equalsIgnoreCase("N")) {
            log.info("IsCycleCount: " + stockAdjustment.getIsCycleCount());
            PerpetualLineV2 perpetualLine = perpetualLineService.getPerpetualLineForStockAdjustmentV2(
                    stockAdjustment.getCompanyCode(),
                    stockAdjustment.getBranchCode(),
                    "EN",
                    stockAdjustment.getWarehouseId(),
                    stockAdjustment.getItemCode(),
                    stockAdjustment.getManufacturerName());
            log.info("Perpetual Line: " + perpetualLine);

            if (perpetualLine != null) {

                InventoryV2 dbInventory = inventoryService.getInventoryV2(
                        perpetualLine.getCompanyCodeId(),
                        perpetualLine.getPlantId(),
                        perpetualLine.getLanguageId(),
                        perpetualLine.getWarehouseId(),
                        perpetualLine.getPackBarcodes(),
                        perpetualLine.getItemCode(),
                        perpetualLine.getStorageBin(),
                        perpetualLine.getManufacturerName());
                log.info("Inventory: " + dbInventory);

                if (dbInventory != null) {
                    InventoryV2 newInventory = new InventoryV2();
                    BeanUtils.copyProperties(dbInventory, newInventory, CommonUtils.getNullPropertyNames(dbInventory));
                    newInventory.setInventoryId(System.currentTimeMillis());
                    newInventory.setInventoryQuantity(dbInventory.getInventoryQuantity() + stockAdjustment.getAdjustmentQty());
                    Double ALLOC_QTY = 0D;
                    if(dbInventory.getAllocatedQuantity() != null) {
                        ALLOC_QTY = dbInventory.getAllocatedQuantity();
                    }
                    newInventory.setReferenceField4(dbInventory.getInventoryQuantity() + ALLOC_QTY);
                    inventoryV2Repository.save(newInventory);

                    //StockAdjustment Record Insert
                    StockAdjustment dbStockAdjustment = new StockAdjustment();
                    BeanUtils.copyProperties(newInventory, dbStockAdjustment, CommonUtils.getNullPropertyNames(newInventory));

                    dbStockAdjustment.setAdjustmentQty(stockAdjustment.getAdjustmentQty());
                    dbStockAdjustment.setCompanyCode(stockAdjustment.getCompanyCode());
                    dbStockAdjustment.setBranchCode(stockAdjustment.getBranchCode());

                    if (stockAdjustment.getItemDescription() != null) {
                        dbStockAdjustment.setItemDescription(stockAdjustment.getItemDescription());
                    }
                    if (stockAdjustment.getItemDescription() == null && dbInventory.getDescription() != null) {
                        dbStockAdjustment.setItemDescription(dbInventory.getDescription());
                    }
                    if (stockAdjustment.getItemDescription() == null && dbInventory.getDescription() == null) {
                        if (dbInventory.getReferenceField8() != null) {
                            dbStockAdjustment.setItemDescription(dbInventory.getReferenceField8());
                        }
                    }

                    dbStockAdjustment.setBeforeAdjustment(dbInventory.getInventoryQuantity());
                    dbStockAdjustment.setAfterAdjustment(newInventory.getInventoryQuantity());

                    dbStockAdjustment.setPackBarcodes(dbInventory.getPackBarcodes());
                    dbStockAdjustment.setBranchName(stockAdjustment.getBranchName());
                    dbStockAdjustment.setDateOfAdjustment(stockAdjustment.getDateOfAdjustment());
                    dbStockAdjustment.setUnitOfMeasure(stockAdjustment.getUnitOfMeasure());
                    dbStockAdjustment.setManufacturerCode(stockAdjustment.getManufacturerCode());
                    dbStockAdjustment.setReferenceField1(stockAdjustment.getRefDocType());
                    dbStockAdjustment.setRemarks(stockAdjustment.getRemarks());
                    dbStockAdjustment.setAmsReferenceNo(stockAdjustment.getAmsReferenceNo());
                    dbStockAdjustment.setIsCycleCount(stockAdjustment.getIsCycleCount());
                    dbStockAdjustment.setIsCompleted(stockAdjustment.getIsCompleted());
                    dbStockAdjustment.setIsDamage(stockAdjustment.getIsDamage());
                    dbStockAdjustment.setMiddlewareId(stockAdjustment.getMiddlewareId());
                    dbStockAdjustment.setMiddlewareTable(stockAdjustment.getMiddlewareTable());
                    dbStockAdjustment.setSaUpdatedOn(stockAdjustment.getUpdatedOn());
                    dbStockAdjustment.setStockAdjustmentKey(newInventory.getInventoryId());

                    dbStockAdjustment.setStatusId(88L);                 //Hard Code - StockAdjustment Done/Closed
                    statusDescription = stagingLineV2Repository.getStatusDescription(88L, dbInventory.getLanguageId());
                    dbStockAdjustment.setStatusDescription(statusDescription);

                    dbStockAdjustment.setDeletionIndicator(0L);
                    dbStockAdjustment.setCreatedOn(new Date());
                    dbStockAdjustment.setIsCompleted("Y");
                    dbStockAdjustment.setCreatedBy("MW_AMS");

                    StockAdjustment createStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
                    log.info("createdStockAdjustment: " + createStockAdjustment);
                    return createStockAdjustment;
                }
            }
        }

        //Stock Adjustment Damage Code
        if (stockAdjustment.getIsDamage().equalsIgnoreCase("Y") && stockAdjustment.getIsCycleCount().equalsIgnoreCase("N")) {
            log.info("IsDamage: " + stockAdjustment.getIsDamage());

            InventoryV2 dbInventory = inventoryService.getInventoryForStockAdjustmentDamageV2(
                    stockAdjustment.getCompanyCode(),
                    stockAdjustment.getBranchCode(),
                    "EN",
                    stockAdjustment.getWarehouseId(),
                    stockAdjustment.getItemCode(),
                    "99999",
                    7L,
                    stockAdjustment.getManufacturerName());
            log.info("Inventory: " + dbInventory);

            InventoryV2 newInventory = new InventoryV2();

            if (dbInventory != null) {
                log.info("Inventory : " + dbInventory);
                BeanUtils.copyProperties(dbInventory, newInventory, CommonUtils.getNullPropertyNames(dbInventory));
                newInventory.setInventoryQuantity(dbInventory.getInventoryQuantity() + stockAdjustment.getAdjustmentQty());
                Double ALLOC_QTY = 0D;
                if(dbInventory.getAllocatedQuantity() != null) {
                    ALLOC_QTY = dbInventory.getAllocatedQuantity();
                }
                newInventory.setReferenceField4(dbInventory.getInventoryQuantity() + ALLOC_QTY);
            }
            if (dbInventory == null) {
                log.info("New Inventory-----> BinclassId 7 for this item is empty");
                newInventory.setCompanyCodeId(stockAdjustment.getCompanyCode());
                newInventory.setPlantId(stockAdjustment.getBranchCode());
                newInventory.setWarehouseId(stockAdjustment.getWarehouseId());
                newInventory.setLanguageId("EN");
                newInventory.setVariantCode(1L);                // VAR_ID
                newInventory.setVariantSubCode("1");            // VAR_SUB_ID
                newInventory.setStorageMethod("1");            // STR_MTD
                newInventory.setBatchSerialNumber("1");        // STR_NO
                newInventory.setDeletionIndicator(0L);
                newInventory.setBinClassId(7L);
                newInventory.setPackBarcodes("99999");

                newInventory.setItemCode(stockAdjustment.getItemCode());
                newInventory.setManufacturerCode(stockAdjustment.getManufacturerName());
                newInventory.setManufacturerName(stockAdjustment.getManufacturerName());

                AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
                StorageBinPutAway storageBinPutAway = new StorageBinPutAway();
                storageBinPutAway.setCompanyCodeId(stockAdjustment.getCompanyCode());
                storageBinPutAway.setPlantId(stockAdjustment.getBranchCode());
                storageBinPutAway.setLanguageId(newInventory.getLanguageId());
                storageBinPutAway.setWarehouseId(stockAdjustment.getWarehouseId());
                storageBinPutAway.setBinClassId(7L);

                StorageBinV2 storageBin = mastersService.getStorageBinBinClassId7(storageBinPutAway, authTokenForMastersService.getAccess_token());
                log.info("storageBin: " + storageBin);

                if (storageBin != null) {
                    newInventory.setReferenceField10(storageBin.getStorageSectionId());
                    newInventory.setReferenceField5(storageBin.getAisleNumber());
                    newInventory.setReferenceField6(storageBin.getShelfId());
                    newInventory.setReferenceField7(storageBin.getRowId());
                    newInventory.setLevelId(String.valueOf(storageBin.getFloorId()));
                    newInventory.setStorageBin(storageBin.getStorageBin());
                }

                ImBasicData imBasicData = new ImBasicData();
                imBasicData.setCompanyCodeId(stockAdjustment.getCompanyCode());
                imBasicData.setPlantId(stockAdjustment.getBranchCode());
                imBasicData.setLanguageId(newInventory.getLanguageId());
                imBasicData.setWarehouseId(stockAdjustment.getWarehouseId());
                imBasicData.setItemCode(stockAdjustment.getItemCode());
                imBasicData.setManufacturerName(stockAdjustment.getManufacturerName());
                ImBasicData1 dbItemCode = mastersService.getImBasicData1ByItemCodeV2(imBasicData, authTokenForMastersService.getAccess_token());
                log.info("ImbasicData1 : " + dbItemCode);

                if (dbItemCode != null) {
                    newInventory.setReferenceField8(dbItemCode.getDescription());
                    newInventory.setReferenceField9(dbItemCode.getManufacturerPartNo());
                    newInventory.setManufacturerCode(dbItemCode.getManufacturerPartNo());
                    newInventory.setDescription(dbItemCode.getDescription());
                }

                // STCK_TYP_ID
                newInventory.setStockTypeId(1L);
                String stockTypeDesc = getStockTypeDesc(stockAdjustment.getCompanyCode(), stockAdjustment.getBranchCode(),
                        newInventory.getLanguageId(), stockAdjustment.getWarehouseId(), 1L);
                newInventory.setStockTypeDescription(stockTypeDesc);

                // SP_ST_IND_ID
                newInventory.setSpecialStockIndicatorId(1L);

                if (stockAdjustment.getAdjustmentQty() >= 0) {
                    newInventory.setInventoryQuantity(stockAdjustment.getAdjustmentQty());
                }
                if (stockAdjustment.getAdjustmentQty() < 0) {
                    newInventory.setInventoryQuantity(0D);
                }

                Double ALLOC_QTY = 0D;
                newInventory.setAllocatedQuantity(ALLOC_QTY);
                newInventory.setReferenceField4(dbInventory.getInventoryQuantity() + ALLOC_QTY);
                newInventory.setCompanyDescription(stockAdjustment.getCompanyDescription());
                newInventory.setPlantDescription(stockAdjustment.getPlantDescription());
                newInventory.setWarehouseDescription(stockAdjustment.getWarehouseDescription());

                List<String> barcode = stagingLineV2Repository.getPartnerItemBarcode(stockAdjustment.getItemCode(),
                        stockAdjustment.getCompanyCode(),
                        stockAdjustment.getBranchCode(),
                        stockAdjustment.getWarehouseId(),
                        stockAdjustment.getManufacturerName(),
                        newInventory.getLanguageId());
                log.info("Barcode : " + barcode);

                if (barcode != null && !barcode.isEmpty())  {
                    newInventory.setBarcodeId(barcode.get(0));
                }

                newInventory.setCreatedOn(new Date());
            }

            newInventory.setUpdatedOn(new Date());
            newInventory.setInventoryId(System.currentTimeMillis());
            inventoryV2Repository.save(newInventory);

            //StockAdjustment Record Insert
            StockAdjustment dbStockAdjustment = new StockAdjustment();
            BeanUtils.copyProperties(newInventory, dbStockAdjustment, CommonUtils.getNullPropertyNames(newInventory));

            dbStockAdjustment.setAdjustmentQty(stockAdjustment.getAdjustmentQty());
            dbStockAdjustment.setCompanyCode(stockAdjustment.getCompanyCode());
            dbStockAdjustment.setBranchCode(stockAdjustment.getBranchCode());

            if (stockAdjustment.getItemDescription() != null) {
                dbStockAdjustment.setItemDescription(stockAdjustment.getItemDescription());
            }
            if (stockAdjustment.getItemDescription() == null && newInventory.getDescription() != null) {
                dbStockAdjustment.setItemDescription(newInventory.getDescription());
            }
            if (stockAdjustment.getItemDescription() == null && newInventory.getDescription() == null) {
                if (dbInventory.getReferenceField8() != null) {
                    dbStockAdjustment.setItemDescription(newInventory.getReferenceField8());
                }
            }

            if (dbInventory != null) {
                dbStockAdjustment.setBeforeAdjustment(dbInventory.getInventoryQuantity());
            }
            if (dbInventory == null) {
                dbStockAdjustment.setBeforeAdjustment(0D);
            }
            dbStockAdjustment.setAfterAdjustment(newInventory.getInventoryQuantity());

            dbStockAdjustment.setPackBarcodes(newInventory.getPackBarcodes());
            dbStockAdjustment.setBranchName(stockAdjustment.getBranchName());
            dbStockAdjustment.setDateOfAdjustment(stockAdjustment.getDateOfAdjustment());
            dbStockAdjustment.setUnitOfMeasure(stockAdjustment.getUnitOfMeasure());
            dbStockAdjustment.setManufacturerCode(stockAdjustment.getManufacturerCode());
            dbStockAdjustment.setReferenceField1(stockAdjustment.getRefDocType());
            dbStockAdjustment.setRemarks(stockAdjustment.getRemarks());
            dbStockAdjustment.setAmsReferenceNo(stockAdjustment.getAmsReferenceNo());
            dbStockAdjustment.setIsCycleCount(stockAdjustment.getIsCycleCount());
            dbStockAdjustment.setIsCompleted(stockAdjustment.getIsCompleted());
            dbStockAdjustment.setIsDamage(stockAdjustment.getIsDamage());
            dbStockAdjustment.setMiddlewareId(stockAdjustment.getMiddlewareId());
            dbStockAdjustment.setMiddlewareTable(stockAdjustment.getMiddlewareTable());
            dbStockAdjustment.setSaUpdatedOn(stockAdjustment.getUpdatedOn());
            dbStockAdjustment.setStockAdjustmentKey(System.currentTimeMillis());

            dbStockAdjustment.setStatusId(88L);                 //Hard Code - StockAdjustment Done/Closed
            statusDescription = stagingLineV2Repository.getStatusDescription(88L, newInventory.getLanguageId());
            dbStockAdjustment.setStatusDescription(statusDescription);

            dbStockAdjustment.setDeletionIndicator(0L);
            dbStockAdjustment.setIsCompleted("Y");
            dbStockAdjustment.setCreatedOn(new Date());
            dbStockAdjustment.setCreatedBy("MW_AMS");

            StockAdjustment createStockAdjustment = stockAdjustmentRepository.save(dbStockAdjustment);
            log.info("createdStockAdjustment: " + createStockAdjustment);

            return createStockAdjustment;
        }
        return null;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentKey
     * @param storageBin
     * @return
     */
    public List<StockAdjustment> getStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                    String itemCode, String manufacturerName, Long stockAdjustmentKey, String storageBin) {
        List<StockAdjustment> dbStockAdjustment =
                stockAdjustmentRepository.
                        findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentKeyAndDeletionIndicator(
                                languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, storageBin, stockAdjustmentKey, 0L);

        if (dbStockAdjustment != null) {

            log.info("StockAdjustment: " + dbStockAdjustment);

            return dbStockAdjustment;
        }
        throw new BadRequestException("The Given StockAdjustmentKey doesn't Exist: " + stockAdjustmentKey);
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param loginUserId
     * @param itemCode
     * @param manufacturerName
     * @param stockAdjustmentKey
     * @param storageBin
     */
    public void deleteStockAdjustment(String companyCodeId, String plantId, String languageId, String warehouseId, String loginUserId,
                                      String itemCode, String manufacturerName, Long stockAdjustmentKey, String storageBin) {
        List<StockAdjustment> dbStockAdjustmentList =
                stockAdjustmentRepository.
                        findByLanguageIdAndCompanyCodeAndBranchCodeAndWarehouseIdAndItemCodeAndManufacturerNameAndStorageBinAndStockAdjustmentKeyAndDeletionIndicator(
                                languageId, companyCodeId, plantId, warehouseId, itemCode, manufacturerName, storageBin, stockAdjustmentKey, 0L);

        if (dbStockAdjustmentList != null) {

            for (StockAdjustment dbStockAdjustment : dbStockAdjustmentList) {

                log.info("StockAdjustment: " + dbStockAdjustment);

                dbStockAdjustment.setDeletionIndicator(1L);
                dbStockAdjustment.setUpdatedBy(loginUserId);
                dbStockAdjustment.setUpdatedOn(new Date());
                stockAdjustmentRepository.save(dbStockAdjustment);

            }
        }
        throw new BadRequestException("The Given StockAdjustmentId doesn't Exist: " + stockAdjustmentKey);
    }

}