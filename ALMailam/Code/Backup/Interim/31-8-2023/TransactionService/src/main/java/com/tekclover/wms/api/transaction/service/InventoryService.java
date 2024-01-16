package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.InventoryV2;
import com.tekclover.wms.api.transaction.model.inbound.inventory.v2.SearchInventoryV2;
import com.tekclover.wms.api.transaction.repository.InventoryV2Repository;
import com.tekclover.wms.api.transaction.repository.specification.InventoryV2Specification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.dto.IInventory;
import com.tekclover.wms.api.transaction.model.dto.Warehouse;
import com.tekclover.wms.api.transaction.model.inbound.inventory.AddInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.SearchInventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.UpdateInventory;
import com.tekclover.wms.api.transaction.repository.InventoryRepository;
import com.tekclover.wms.api.transaction.repository.specification.InventorySpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryService extends BaseService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryV2Repository inventoryV2Repository;

    /**
     * getInventorys
     *
     * @return
     */
    public List<Inventory> getInventorys() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        inventoryList = inventoryList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return inventoryList;
    }

    /**
     * getInventory
     *
     * @param stockTypeId
     * @return
     */
    public Inventory getInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin, Long stockTypeId,
                                  Long specialStockIndicatorId) {
        Optional<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndStockTypeIdAndSpecialStockIndicatorIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        storageBin,
                        stockTypeId,
                        specialStockIndicatorId,
                        0L
                );
        if (inventory.isEmpty()) {
            throw new BadRequestException("The given Inventory ID : " +
                    ", warehouseId: " + warehouseId +
                    ", packBarcodes: " + packBarcodes +
                    ", itemCode: " + itemCode +
                    ", storageBin: " + storageBin +
                    ", stockTypeId: " + stockTypeId +
                    ", specialStockIndicatorId: " + specialStockIndicatorId +
                    " doesn't exist.");
        }
        return inventory.get();
    }

    /**
     * getInventory
     *
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param binClassId
     * @return
     */
    public Inventory getInventory(String warehouseId, String packBarcodes, String itemCode, Long binClassId) {
        Optional<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndBinClassIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        binClassId,
                        0L
                );

        if (inventory != null) {
            return inventory.get();
        }
        return null;
    }

    public InventoryV2 getInventory(String companyCodeId, String plantId,
                                    String languageId, String warehouseId,
                                    String packBarcodes, String itemCode, Long binClassId) {
        Optional<InventoryV2> inventory =
                inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndBinClassIdAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        binClassId,
                        0L
                );

        if (inventory != null) {
            return inventory.get();
        }
        return null;
    }

    public List<InventoryV2> getInventory(String companyCodeId, String plantId,
                                          String languageId, String warehouseId,
                                          String referenceDocumentNo, String itemCode) {
        List<InventoryV2> inventory =
                inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndReferenceDocumentNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        referenceDocumentNo,
                        itemCode,
                        0L
                );

        if (inventory == null) {
            throw new BadRequestException("The given Inventory ID : " +
                    ", warehouseId: " + warehouseId +
                    ", companyCodeId: " + companyCodeId +
                    ", plantId: " + plantId +
                    ", languageId: " + languageId +
                    ", referenceDocumentNo: " + referenceDocumentNo +
                    ", itemCode: " + itemCode +
                    " doesn't exist.");
        }
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param packBarcodes
     * @param binClassId
     * @return
     */
    public List<Inventory> getInventoryForDeliveryConfirmtion(String warehouseId, String itemCode,
                                                              String packBarcodes, Long binClassId) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndPackBarcodesAndBinClassIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        packBarcodes,
                        binClassId,
                        0L
                );
        if (inventory != null) {
            return inventory;
        }
        return null;
    }


    /**
     * @param warehouseId
     * @param itemCode
     * @param stockTypeId
     * @return
     */
    public List<Inventory> getInventoryForStockReport(String warehouseId, String itemCode, Long stockTypeId) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndAndStockTypeIdAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        stockTypeId,
                        0L
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param stockTypeId
     * @param binClassId
     * @return
     */
    public List<Inventory> getInventoryForOrderManagement(String warehouseId, String itemCode, Long stockTypeId, Long binClassId) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndAndStockTypeIdAndBinClassIdAndInventoryQuantityGreaterThanAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        stockTypeId,
                        binClassId,
                        0D,
                        0L
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @return
     */
    public List<Inventory> getInventoryForDelete(String warehouseId, String packBarcodes, String itemCode) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        0L
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param palletCode
     * @param caseCode
     * @param packBarcodes
     * @param itemCode
     * @return
     */
    public List<Inventory> getInventory(String warehouseId, String palletCode, String caseCode, String packBarcodes,
                                        String itemCode) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPalletCodeAndCaseCodeAndPackBarcodesAndItemCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        palletCode,
                        caseCode,
                        packBarcodes,
                        itemCode,
                        0L
                );
        if (inventory.isEmpty()) {
            throw new BadRequestException("The given Inventory ID : " +
                    ", warehouseId: " + warehouseId +
                    ", palletCode: " + palletCode +
                    ", caseCode: " + caseCode +
                    ", packBarcodes: " + packBarcodes +
                    ", itemCode: " + itemCode +
                    " doesn't exist.");
        }
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param palletCode
     * @param packBarcodes
     * @param storageBin
     * @return
     */
    public List<Inventory> getInventoryForTransfers(String warehouseId, String itemCode, String palletCode, String packBarcodes, String storageBin) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndPalletCodeAndPackBarcodesAndStorageBinAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        palletCode,
                        packBarcodes,
                        storageBin,
                        0L
                );

        if (!inventory.isEmpty()) {
            return inventory;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param storageBin
     * @return
     */
    public Inventory getInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin) {
        log.info("getInventory----------> : " + warehouseId + "," + packBarcodes + "," + itemCode + "," + storageBin);
        Optional<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        storageBin,
                        0L
                );
        if (inventory.isEmpty()) {
            log.error("---------Inventory is null-----------");
            return null;
        }
        log.info("getInventory record----------> : " + inventory.get());
        return inventory.get();
    }

    /**
     * @param warehouseId
     * @param storageBin
     * @return
     */
    public Inventory getInventoryByStorageBin(String warehouseId, String storageBin) {
        Optional<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndStorageBinAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        storageBin,
                        0L
                );
        if (inventory.isEmpty()) {
            log.error("---------Inventory is null-----------");
            return null;
        }
        return inventory.get();
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param binClassId
     * @return
     */
    public List<Inventory> getInventory(String warehouseId, String itemCode, Long binClassId) {
        Warehouse warehouse = getWarehouse(warehouseId);
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
                        warehouse.getLanguageId(),
                        warehouse.getCompanyCode(),
                        warehouse.getPlantId(),
                        warehouseId,
                        itemCode,
                        binClassId,
                        0L
                );
        if (inventory.isEmpty()) {
            throw new BadRequestException("The given PreInboundHeader ID : " +
                    ", warehouseId: " + warehouseId +
                    ", itemCode: " + itemCode +
                    ", binClassId: " + binClassId +
                    " doesn't exist.");
        }
        return inventory;
    }

    public List<Inventory> getInventory(String companyCode, String plantId,
                                        String languageId, String warehouseId,
                                        String itemCode, Long binClassId) {
//        Warehouse warehouse = getWarehouse(warehouseId);
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndBinClassIdAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        itemCode,
                        binClassId,
                        0L
                );
        if (inventory.isEmpty()) {
            throw new BadRequestException("The given PreInboundHeader ID : " +
                    ", warehouseId: " + warehouseId +
                    ", itemCode: " + itemCode +
                    ", binClassId: " + binClassId +
                    " doesn't exist.");
        }
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @return
     */
    public List<Inventory> getInventory(String warehouseId, String itemCode) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        0L
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param binClassId
     * @param storageBin
     * @param stockTypeId
     * @return
     */
    public List<Inventory> getInventoryForOrderMgmt(String warehouseId, String itemCode, Long binClassId, String storageBin, Long stockTypeId) {
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndBinClassIdAndStorageBinAndStockTypeIdAndDeletionIndicatorAndInventoryQuantityGreaterThanOrderByInventoryQuantity(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        itemCode,
                        binClassId,
                        storageBin,
                        stockTypeId,
                        0L,
                        0D
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param stSecIds
     * @return
     */
    public List<IInventory> getInventoryGroupByStorageBin(String warehouseId, String itemCode, List<String> stSecIds) {
        List<IInventory> inventory = inventoryRepository.findInventoryGroupByStorageBin(warehouseId, itemCode, stSecIds);
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param storageSectionIds
     * @return
     */
    public List<Inventory> getInventoryForAdditionalBins(String warehouseId, String itemCode, List<String> storageSectionIds) {
        Warehouse warehouse = getWarehouse(warehouseId);
        log.info("InventoryForAdditionalBins ID : warehouseId: " + warehouseId + ", itemCode: " + itemCode + ", storageSectionIds: " + storageSectionIds);
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndReferenceField10InAndBinClassIdAndInventoryQuantityGreaterThan(
                        warehouse.getLanguageId(),
                        warehouse.getCompanyCode(),
                        warehouse.getPlantId(),
                        warehouseId,
                        itemCode,
                        storageSectionIds,
                        1L,
                        0D
                );
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param storageSectionIds
     * @param stockTypeId
     * @return
     */
    public List<Inventory> getInventoryForAdditionalBinsForOB2(String warehouseId, String itemCode, List<String> storageSectionIds, Long stockTypeId) {
        Warehouse warehouse = getWarehouse(warehouseId);
        List<Inventory> inventory =
                inventoryRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndReferenceField10InAndStockTypeIdAndBinClassIdAndInventoryQuantityGreaterThan(
                        warehouse.getLanguageId(),
                        warehouse.getCompanyCode(),
                        warehouse.getPlantId(),
                        warehouseId,
                        itemCode,
                        storageSectionIds,
                        stockTypeId,
                        1L,
                        0D
                );

        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @param storageBin
     * @param stockTypeId
     * @return
     */
    public List<Long> getInventoryQtyCount(String warehouseId, String itemCode, List<String> storageBin,
                                           Long stockTypeId) {
        List<Long> inventory = inventoryRepository.findInventoryQtyCount(warehouseId, itemCode, storageBin, stockTypeId);
        return inventory;
    }

    /**
     * @param warehouseId
     * @param itemCode
     * @return
     */
    public Double getInventoryQtyCount(String warehouseId, String itemCode) {
        Double inventoryQty = inventoryRepository.getInventoryQtyCount(warehouseId, itemCode);
        return inventoryQty;
    }

    /**
     * @param warehouseId
     * @param stBins
     * @return
     */
    public List<Inventory> getInventoryByStorageBin(String warehouseId, List<String> stBins) {
        List<Inventory> inventory = inventoryRepository.findByWarehouseIdAndStorageBinIn(warehouseId, stBins);
        return inventory;
    }

    /**
     * @param searchInventory
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     * @throws ParseException
     */
    public Page<Inventory> findInventory(SearchInventory searchInventory, Integer pageNo, Integer pageSize, String sortBy)
            throws ParseException {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        InventorySpecification spec = new InventorySpecification(searchInventory);
        Page<Inventory> results = inventoryRepository.findAll(spec, paging);
        return results;
    }

    /**
     * @param searchInventory
     * @return
     * @throws ParseException
     */
    public List<Inventory> findInventory(SearchInventory searchInventory)
            throws ParseException {
        InventorySpecification spec = new InventorySpecification(searchInventory);
        List<Inventory> results = inventoryRepository.findAll(spec);
        results.stream().forEach(n -> {
            if (n.getInventoryQuantity() == null) {
                n.setInventoryQuantity(0D);
            }
            if (n.getAllocatedQuantity() == null) {
                n.setAllocatedQuantity(0D);
            }
            n.setReferenceField4(n.getInventoryQuantity() + n.getAllocatedQuantity());
        });
        return results;
    }

    //V2
    public List<InventoryV2> findInventoryV2(SearchInventoryV2 searchInventory)
            throws ParseException {
        InventoryV2Specification spec = new InventoryV2Specification(searchInventory);
        List<InventoryV2> results = inventoryV2Repository.findAll(spec);
        return results;
    }

    /**
     * @param searchInventory
     * @return
     * @throws ParseException
     */
    public List<Inventory> getQuantityValidatedInventory(SearchInventory searchInventory)
            throws ParseException {
        InventorySpecification spec = new InventorySpecification(searchInventory);
        List<Inventory> results = inventoryRepository.findAll(spec);
        if (!results.isEmpty()) {
            results = results.stream().filter(x -> (x.getInventoryQuantity() != null && x.getInventoryQuantity() != 0) && (x.getAllocatedQuantity() != null && x.getAllocatedQuantity() != 0)).collect(Collectors.toList());
        }
        return results;
    }

    /**
     * createInventory
     *
     * @param newInventory
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Inventory createInventory(AddInventory newInventory, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Inventory dbInventory = new Inventory();
        log.info("newInventory : " + newInventory);
        BeanUtils.copyProperties(newInventory, dbInventory, CommonUtils.getNullPropertyNames(newInventory));
        dbInventory.setDeletionIndicator(0L);
        dbInventory.setCreatedBy(loginUserID);
        dbInventory.setCreatedOn(new Date());
        return inventoryRepository.save(dbInventory);
    }

    /**
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param storageBin
     * @param stockTypeId
     * @param specialStockIndicatorId
     * @param updateInventory
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Inventory updateInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin,
                                     Long stockTypeId, Long specialStockIndicatorId, UpdateInventory updateInventory, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Inventory dbInventory = getInventory(warehouseId, packBarcodes, itemCode, storageBin, stockTypeId, specialStockIndicatorId);
        BeanUtils.copyProperties(updateInventory, dbInventory, CommonUtils.getNullPropertyNames(updateInventory));
        dbInventory.setUpdatedBy(loginUserID);
        dbInventory.setUpdatedOn(new Date());
        return inventoryRepository.save(dbInventory);
    }

    public InventoryV2 updateInventoryV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                         String packBarcodes, String itemCode, String storageBin, Long stockTypeId,
                                         Long specialStockIndicatorId, UpdateInventory updateInventory, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        InventoryV2 dbInventory = inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndStockTypeIdAndSpecialStockIndicatorIdAndDeletionIndicator(
                languageId, companyCodeId, plantId,
                warehouseId, packBarcodes, itemCode,
                storageBin, stockTypeId, specialStockIndicatorId, 0L);
        BeanUtils.copyProperties(updateInventory, dbInventory, CommonUtils.getNullPropertyNames(updateInventory));
        dbInventory.setUpdatedBy(loginUserID);
        dbInventory.setUpdatedOn(new Date());
        return inventoryRepository.save(dbInventory);
    }

    /**
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param storageBin
     * @param stockTypeId
     * @param specialStockIndicatorId
     */
    public void deleteInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin, Long stockTypeId,
                                Long specialStockIndicatorId) {
        Inventory inventory = getInventory(warehouseId, packBarcodes, itemCode, storageBin, stockTypeId, specialStockIndicatorId);
        if (inventory != null) {
            inventory.setDeletionIndicator(1L);
            inventoryRepository.save(inventory);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + stockTypeId);
        }
    }

    /**
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @return
     */
    public boolean deleteInventory(String warehouseId, String packBarcodes, String itemCode) {
        try {
            List<Inventory> inventoryList = getInventoryForDelete(warehouseId, packBarcodes, itemCode);
            log.info("inventoryList : " + inventoryList);
            if (inventoryList != null) {
                for (Inventory inventory : inventoryList) {
                    inventoryRepository.delete(inventory);
                    log.info("inventory deleted.");
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new EntityNotFoundException("Error in deleting Id: " + e.toString());
        }
    }

    /*
     * -------------------------V2----------------------------------------------------------------
     */

    /**
     *
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param storageBin
     * @param manufacturerCode
     * @return
     */
    public InventoryV2 getInventory(String companyCode, String plantId, String languageId,
                                    String warehouseId, String packBarcodes, String itemCode,
                                    String storageBin, String manufacturerCode) {
        log.info("getInventory----------> : " + warehouseId + "," + packBarcodes + "," + itemCode + "," + storageBin);
        Optional<InventoryV2> inventory =
                inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndStorageBinAndManufacturerCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        storageBin,
                        manufacturerCode,
                        0L
                );
        if (inventory.isEmpty()) {
            log.error("---------Inventory is null-----------");
            return null;
        }
        log.info("getInventory record----------> : " + inventory.get());
        return inventory.get();
    }

    /**
     *
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param packBarcodes
     * @param itemCode
     * @param manufacturerCode
     * @return
     */
    public InventoryV2 getInventory(String companyCode, String plantId, String languageId,
                                    String warehouseId, String packBarcodes, String itemCode,
                                    String manufacturerCode) {
        log.info("getInventory----------> : " + warehouseId + "," + packBarcodes + "," + itemCode );
        Optional<InventoryV2> inventory =
                inventoryV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPackBarcodesAndItemCodeAndManufacturerCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        packBarcodes,
                        itemCode,
                        manufacturerCode,
                        0L
                );
        if (inventory.isEmpty()) {
            log.error("---------Inventory is null-----------");
            return null;
        }
        log.info("getInventory record----------> : " + inventory.get());
        return inventory.get();
    }
}
