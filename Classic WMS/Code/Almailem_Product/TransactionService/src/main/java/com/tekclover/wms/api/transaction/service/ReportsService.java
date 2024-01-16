package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.cyclecount.periodic.PeriodicLine;
import com.tekclover.wms.api.transaction.model.cyclecount.perpetual.PerpetualLine;
import com.tekclover.wms.api.transaction.model.dto.BusinessPartner;
import com.tekclover.wms.api.transaction.model.dto.BusinessPartnerV2;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData1;
import com.tekclover.wms.api.transaction.model.dto.StorageBin;
import com.tekclover.wms.api.transaction.model.impl.OrderStatusReportImpl;
import com.tekclover.wms.api.transaction.model.impl.ShipmentDispatchSummaryReportImpl;
import com.tekclover.wms.api.transaction.model.impl.StockReportImpl;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.GrHeader;
import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import com.tekclover.wms.api.transaction.model.inbound.inventory.InventoryMovement;
import com.tekclover.wms.api.transaction.model.inbound.inventory.SearchInventory;
import com.tekclover.wms.api.transaction.model.inbound.putaway.PutAwayHeader;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import com.tekclover.wms.api.transaction.model.outbound.*;
import com.tekclover.wms.api.transaction.model.outbound.pickup.PickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.quality.QualityHeader;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.v2.OutboundLineV2;
import com.tekclover.wms.api.transaction.model.report.*;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.StockMovementReportNewSpecification;
import com.tekclover.wms.api.transaction.repository.specification.TransactionHistoryReportSpecification;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReportsService extends BaseService {
    @Autowired
    private QualityHeaderRepository qualityHeaderRepository;
    @Autowired
    private InventoryV2Repository inventoryV2Repository;
    @Autowired
    private OutboundHeaderV2Repository outboundHeaderV2Repository;
    @Autowired
    private PeriodicHeaderRepository periodicHeaderRepository;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PreInboundHeaderService preInboundHeaderService;

    @Autowired
    InboundHeaderService inboundHeaderService;

    @Autowired
    InboundLineService inboundLineService;

    @Autowired
    OutboundHeaderService outboundHeaderService;

    @Autowired
    OutboundLineService outboundLineService;

    @Autowired
    MastersService mastersService;

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    StagingHeaderService stagingHeaderService;

    @Autowired
    PutAwayHeaderService putAwayHeaderService;

    @Autowired
    PickupHeaderService pickupHeaderService;

    @Autowired
    PickupLineService pickupLineService;

    @Autowired
    QualityHeaderService qualityHeaderService;

    @Autowired
    StorageBinRepository storagebinRepository;

    @Autowired
    ImBasicData1Repository imbasicdata1Repository;

    @Autowired
    OutboundHeaderRepository outboundHeaderRepository;

    @Autowired
    OutboundLineRepository outboundLineRepository;

    @Autowired
    InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    WorkBookService workBookService;

    @Autowired
    ContainerReceiptRepository containerReceiptRepository;

    @Autowired
    InboundHeaderRepository inboundHeaderRepository;

    @Autowired
    InboundLineRepository inboundLineRepository;

    @Autowired
    PerpetualLineService perpetualLineService;

    @Autowired
    PeriodicLineService periodicLineService;

    @Autowired
    InventoryStockRepository inventoryStockRepository;
    @Autowired
    TransactionHistoryReportRepository transactionHistoryReportRepository;

    @Autowired
    PutAwayLineRepository putAwayLineRepository;

    @Autowired
    PickupLineRepository pickupLineRepository;

    @Autowired
    StockMovementReportRepository stockMovementReportRepository;

    @Autowired
    StockMovementReport1Repository stockMovementReport1Repository;

    @Autowired
    TransactionHistoryResultRepository transactionHistoryResultRepository;

    @Autowired
    GrHeaderService grHeaderService;

    @Autowired
    PeriodicHeaderService periodicHeaderService;


    /**
     * Stock Report ---------------------
     *
     * @param warehouseId
     * @param itemCode
     * @param itemText
     * @param stockTypeId
     * @return
     */
//	public Page<StockReport> getStockReport(List<String> warehouseId, List<String> itemCode, String itemText,
//			String stockTypeText, Integer pageNo, Integer pageSize, String sortBy) {
//		if (warehouseId == null) {
//			throw new BadRequestException("WarehouseId can't be blank.");
//		}
//
//		if (stockTypeText == null) {
//			throw new BadRequestException("StockTypeText can't be blank.");
//		}
//
//		try {
//			SearchInventory searchInventory = new SearchInventory();
//			searchInventory.setWarehouseId(warehouseId);
//
//			if (itemCode != null) {
//				searchInventory.setItemCode(itemCode);
//			}
//
//			if (itemText != null) {
//				searchInventory.setDescription(itemText);
//			}
//
//			List<Long> stockTypeIdList = null;
//			if (stockTypeText.equalsIgnoreCase("ALL")) {
//				stockTypeIdList = Arrays.asList(1L, 7L);
//			} else if (stockTypeText.equalsIgnoreCase("ON HAND")) {
//				stockTypeIdList = Arrays.asList(1L);
//			} else if (stockTypeText.equalsIgnoreCase("DAMAGED")) {
//				stockTypeIdList = Arrays.asList(1L);
//			} else if (stockTypeText.equalsIgnoreCase("HOLD")) {
//				stockTypeIdList = Arrays.asList(7L);
//			}
//
//			searchInventory.setStockTypeId(stockTypeIdList);
//			log.info("searchInventory : " + searchInventory);
//
//			Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
//			Page<Inventory> inventoryList = inventoryService.findInventory(searchInventory, pageNo, pageSize, sortBy);
//			log.info("inventoryList : " + inventoryList);
//
//			List<StockReport> stockReportList = new ArrayList<>();
//			Set<String> uniqueItemCode = new HashSet<>();
//			for (Inventory inventory : inventoryList) {
//				if (uniqueItemCode.add(inventory.getItemCode())) {
//					StockReport stockReport = new StockReport();
//
//					// WH_ID
//					stockReport.setWarehouseId(inventory.getWarehouseId());
//
//					// ITM_CODE
//					stockReport.setItemCode(inventory.getItemCode());
//
//					/*
//					 * MFR_SKU -------------- Pass the fetched ITM_CODE values in IMBASICDATA1 table
//					 * and fetch MFR_SKU values
//					 */
//					ImBasicData1 imBasicData1 = imbasicdata1Repository.findByItemCodeAndWarehouseIdAndDeletionIndicator(
//							inventory.getItemCode(), inventory.getWarehouseId(), 0L);
//					if (imBasicData1 != null) {
//						stockReport.setManufacturerSKU(imBasicData1.getManufacturerPartNo());
//						stockReport.setItemText(imBasicData1.getDescription());
//					} else {
//						stockReport.setManufacturerSKU("");
//						stockReport.setItemText("");
//					}
//
//					if (stockTypeText.equalsIgnoreCase("ALL")) {
//						/*
//						 * For onhand, damageqty -> stock_type_id is 1 For Hold -> stok_type_id is 7
//						 */
//						// ON HAND
//						List<String> storageSectionIds = Arrays.asList("ZB", "ZG", "ZC", "ZT");
//						double ON_HAND_INVQTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 1L,
//								storageSectionIds);
//						stockReport.setOnHandQty(ON_HAND_INVQTY);
//
//						// DAMAGED
//						storageSectionIds = Arrays.asList("ZD");
//						double DAMAGED_INVQTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 1L,
//								storageSectionIds);
//						stockReport.setDamageQty(DAMAGED_INVQTY);
//
//						// HOLD
//						storageSectionIds = Arrays.asList("ZB", "ZG", "ZD", "ZC", "ZT");
//						double HOLD_INVQTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 7L,
//								storageSectionIds);
//						stockReport.setHoldQty(HOLD_INVQTY);
//
//						// Available Qty
//						double AVAILABLE_QTY = ON_HAND_INVQTY + DAMAGED_INVQTY + HOLD_INVQTY;
//						stockReport.setAvailableQty(AVAILABLE_QTY);
//
//						if (AVAILABLE_QTY != 0) {
//							stockReportList.add(stockReport);
//						}
//						log.info("ALL-------stockReport:" + stockReport);
//					} else if (stockTypeText.equalsIgnoreCase("ON HAND")) {
//						// stock_type_id = 1
//						List<String> storageSectionIds = Arrays.asList("ZB", "ZG", "ZC", "ZT");
//						double INV_QTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 1L,
//								storageSectionIds);
//						if (INV_QTY != 0) {
//							stockReport.setOnHandQty(INV_QTY);
//							stockReport.setDamageQty(0D);
//							stockReport.setHoldQty(0D);
//							stockReport.setAvailableQty(INV_QTY);
//							log.info("ON HAND-------stockReport:" + stockReport);
//							stockReportList.add(stockReport);
//						}
//					} else if (stockTypeText.equalsIgnoreCase("DAMAGED")) {
//						// stock_type_id = 1
//						List<String> storageSectionIds = Arrays.asList("ZD");
//						double INV_QTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 1L,
//								storageSectionIds);
//
//						if (INV_QTY != 0) {
//							stockReport.setDamageQty(INV_QTY);
//							stockReport.setOnHandQty(0D);
//							stockReport.setHoldQty(0D);
//							stockReport.setAvailableQty(INV_QTY);
//
//							log.info("DAMAGED-------stockReport:" + stockReport);
//							stockReportList.add(stockReport);
//						}
//					} else if (stockTypeText.equalsIgnoreCase("HOLD")) {
//						// STCK_TYP_ID = 7
//						List<String> storageSectionIds = Arrays.asList("ZB", "ZG", "ZD", "ZC", "ZT");
//						double INV_QTY = getInventoryQty(inventory.getWarehouseId(), inventory.getItemCode(), 7L,
//								storageSectionIds);
//
//						if (INV_QTY != 0) {
//							stockReport.setHoldQty(INV_QTY);
//							stockReport.setOnHandQty(0D);
//							stockReport.setDamageQty(0D);
//							stockReport.setAvailableQty(INV_QTY);
//							log.info("HOLD-------stockReport:" + stockReport);
//							stockReportList.add(stockReport);
//						}
//					}
//				}
//			}
//			log.info("stockReportList : " + stockReportList);
//			final Page<StockReport> page = new PageImpl<>(stockReportList, pageable, inventoryList.getTotalElements());
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param itemCode
     * @param itemText
     * @param stockTypeText
     * @return
     */
    public List<StockReport> getAllStockReport(List<String> languageId, List<String> companyCodeId, List<String> plantId,
                                               List<String> warehouseId, List<String> itemCode, List<String> manufacturerName, String itemText, String stockTypeText) {

        if (languageId == null) {
            throw new BadRequestException("languageId can't be blank");
        }

        if (companyCodeId == null) {
            throw new BadRequestException("companyCodeId can't be blank");
        }

        if (plantId == null) {
            throw new BadRequestException("plantId can't be blank");
        }

        if (warehouseId == null) {
            throw new BadRequestException("WarehouseId can't be blank.");
        }

        if (stockTypeText == null) {
            throw new BadRequestException("StockTypeText can't be blank.");
        }

        if (itemCode != null && itemCode.isEmpty()) {
            itemCode = null;
        }
        if (manufacturerName != null && manufacturerName.isEmpty()) {
            manufacturerName = null;
        }

        if (itemText != null && itemText.trim().equals("")) {
            itemText = null;
        }

        List<StockReport> stockReportList = new ArrayList<>();
        List<StockReportImpl> reportList = inventoryV2Repository.getAllStockReportNew(
                languageId, companyCodeId, plantId,
                warehouseId,
                itemCode,
                itemText,
                manufacturerName,
                stockTypeText);

        reportList.forEach(data -> {
            StockReport stockReport = new StockReport();
            BeanUtils.copyProperties(data, stockReport);
            stockReportList.add(stockReport);
        });
        return stockReportList;
    }

    /**
     * Inventory Report -------------------------
     *
     * @param warehouseId
     * @param itemCode
     * @param storageBin
     * @param stockTypeText
     * @param stSectionIds
     * @param sortBy
     * @param pageSize
     * @param pageNo
     * @param sortBy
     * @param pageSize
     * @param pageNo
     * @return
     */
    public Page<InventoryReport> getInventoryReport(List<String> warehouseId, List<String> itemCode, String storageBin,
                                                    String stockTypeText, List<String> stSectionIds, Integer pageNo, Integer pageSize, String sortBy) {
        try {
            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

            if (warehouseId == null) {
                throw new BadRequestException("WarehouseId can't be blank.");
            }

            SearchInventory searchInventory = new SearchInventory();
            searchInventory.setWarehouseId(warehouseId);
            searchInventory.setItemCode(itemCode);
            searchInventory.setStorageBin(new ArrayList<>());
            /*
             * If ST_SEC_ID field value is entered in Search field, Pass ST_SEC_ID in
             * STORAGE_BIN table and fetch ST_BIN values and pass these values in INVENTORY
             * table to fetch the output values
             */
            if (stSectionIds != null) {
                StorageBin[] dbStorageBin = mastersService.getStorageBinBySectionId(warehouseId.get(0), stSectionIds,
                        authTokenForMastersService.getAccess_token());
                List<String> stBins = Arrays.asList(dbStorageBin).stream().map(StorageBin::getStorageBin)
                        .collect(Collectors.toList());
                searchInventory.setStorageBin(stBins);
            }

            if (storageBin != null && !storageBin.trim().equals("")) {
                searchInventory.getStorageBin().add(storageBin);
            }
            searchInventory.setStockTypeId(new ArrayList<>());
            if (stockTypeText.equals("ALL")) {
                searchInventory.getStockTypeId().add(1L);
                searchInventory.getStockTypeId().add(7L);
            } else if (stockTypeText.equals("HOLD")) {
                searchInventory.getStockTypeId().add(7L);
            } else if (stockTypeText.equals("ON HAND")) {
                searchInventory.getStockTypeId().add(1L);
            }

            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<Inventory> inventoryList = inventoryService.findInventory(searchInventory, pageNo, pageSize, sortBy);
            List<InventoryReport> reportInventoryList = new ArrayList<>();
            for (Inventory dbInventory : inventoryList) {
                InventoryReport reportInventory = new InventoryReport();

                // WH_ID
                reportInventory.setWarehouseId(dbInventory.getWarehouseId());

                // ITM_CODE
                reportInventory.setItemCode(dbInventory.getItemCode());
                log.info("dbInventory.getItemCode() : " + dbInventory.getItemCode());

                /*
                 * ITEM_TEXT
                 *
                 * Pass the fetched ITM_CODE values in IMBASICDATA1 table and fetch MFR_SKU
                 * values
                 */
                ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(dbInventory.getItemCode(),
                        dbInventory.getWarehouseId(), authTokenForMastersService.getAccess_token());
                log.info("imBasicData1 : " + imBasicData1);

                if (imBasicData1 != null) {
                    reportInventory.setDescription(imBasicData1.getDescription());
                }

                // INV_UOM
                reportInventory.setUom(dbInventory.getInventoryUom());

                // ST_BIN
                reportInventory.setStorageBin(dbInventory.getStorageBin());
                log.info("dbInventory.getStorageBin() : " + dbInventory.getStorageBin());

                /*
                 * ST_SEC_ID Pass the selected ST_BIN values into STORAGEBIN table and fetch
                 * ST_SEC_ID values
                 */
                StorageBin stBin = mastersService.getStorageBin(dbInventory.getStorageBin(), warehouseId.get(0),
                        authTokenForMastersService.getAccess_token());
                reportInventory.setStorageSectionId(stBin.getStorageSectionId());

                // PACK_BARCODE
                reportInventory.setPackBarcodes(dbInventory.getPackBarcodes());

                // INV_QTY
                reportInventory.setInventoryQty(dbInventory.getInventoryQuantity());

                // STCK_TYP_ID/STCK_TYP_TEXT
                reportInventory.setStockType(dbInventory.getStockTypeId());
                reportInventoryList.add(reportInventory);
            }
            final Page<InventoryReport> page = new PageImpl<>(reportInventoryList, pageable,
                    inventoryList.getTotalElements());
            return page;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
//	public Page<InventoryReport> scheduleInventoryReport(Integer pageNo, Integer pageSize, String sortBy) {
//		List<String> warehouseId = new ArrayList<>();
//		warehouseId.add("110");
//		warehouseId.add("111");
//		AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
//		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
//		Page<Inventory> inventoryList = inventoryRepository.findByWarehouseIdInAndDeletionIndicator (warehouseId, 0L, pageable);
//		
////		Page<Inventory> inventoryList = inventoryRepository.findByWarehouseIdInAndDeletionIndicatorAndItemCode (warehouseId, 0L, "020309497", pageable);
//		List<InventoryReport> reportInventoryList = new ArrayList<>();
//		
//		for (Inventory dbInventory : inventoryList) {
//			boolean isInventoryNeedUpdate = false;
//			InventoryReport reportInventory = new InventoryReport();
//
//			// WH_ID
//			reportInventory.setWarehouseId(dbInventory.getWarehouseId());
//
//			// ITM_CODE
//			reportInventory.setItemCode(dbInventory.getItemCode());
//
//			/*
//			 * ITEM_TEXT
//			 * -------------------------------------------------------------------------
//			 * Pass the fetched ITM_CODE values in IMBASICDATA1 table and fetch MFR_SKU
//			 * values
//			 */
//
//			try {
//				ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(dbInventory.getItemCode(),
//						dbInventory.getWarehouseId(), authTokenForMastersService.getAccess_token());
//
//				if (imBasicData1 != null) {
//					reportInventory.setDescription(imBasicData1.getDescription());
//					reportInventory.setMfrPartNumber(imBasicData1.getManufacturerPartNo());
//					
//					if (dbInventory.getReferenceField8() == null) {
//						dbInventory.setReferenceField8(imBasicData1.getDescription());	
//						isInventoryNeedUpdate = true;
//					}
//					
//					if (dbInventory.getReferenceField9() == null) {
//						dbInventory.setReferenceField9(imBasicData1.getManufacturerPartNo());
//						isInventoryNeedUpdate = true;
//					}
//				}
//			} catch(Exception e) {
//				log.info("ERROR : imBasicData1 master get error " + dbInventory.getItemCode() + " " +
//						dbInventory.getWarehouseId(), e);
//			}
//
//			// INV_UOM
//			reportInventory.setUom(dbInventory.getInventoryUom());
//
//			// ST_BIN
//			reportInventory.setStorageBin(dbInventory.getStorageBin());
//
//			/*
//			 * ST_SEC_ID Pass the selected ST_BIN values into STORAGEBIN table and fetch
//			 * ST_SEC_ID values
//			 */
//			try {
//				StorageBin stBin = mastersService.getStorageBin(dbInventory.getStorageBin(),
//						authTokenForMastersService.getAccess_token());
//				reportInventory.setStorageSectionId(stBin.getStorageSectionId());
//				
//				if (dbInventory.getReferenceField10() == null) {
//					dbInventory.setReferenceField10(stBin.getStorageSectionId());
//					isInventoryNeedUpdate = true;
//				}
//			} catch(Exception e) {
//				log.error("ERROR : stBin master get error "+ dbInventory.getStorageBin(), e);
//			}
//
//			// PACK_BARCODE
//			reportInventory.setPackBarcodes(dbInventory.getPackBarcodes());
//
//			// INV_QTY
//			try {
//				reportInventory.setInventoryQty(dbInventory.getInventoryQuantity() != null ? dbInventory.getInventoryQuantity() : 0);
//				reportInventory.setAllocatedQty(dbInventory.getAllocatedQuantity() != null ? dbInventory.getAllocatedQuantity() : 0);
//
//				reportInventory.setTotalQuantity(Double.sum(dbInventory.getInventoryQuantity() != null ? dbInventory.getInventoryQuantity() : 0,
//						dbInventory.getAllocatedQuantity() != null ? dbInventory.getAllocatedQuantity() : 0 ) );
//			} catch(Exception e) {
//				log.error("ERROR : ALL_QTY , TOTAL_QTY CALCULATE  ", e);
//			}
//
//			
//			// STCK_TYP_ID/STCK_TYP_TEXT
//			reportInventory.setStockType(dbInventory.getStockTypeId());
//			
//			if (isInventoryNeedUpdate) {
//				inventoryRepository.save(dbInventory);
//				log.info("dbInventory got updated");
//			}
//			
//			reportInventoryList.add(reportInventory);
//		}
//		final Page<InventoryReport> page = new PageImpl<>(reportInventoryList, pageable,
//				inventoryList.getTotalElements());
//		return page;
//	}

    /**
     * @return
     */
    public List<InventoryReport> generateInventoryReport() {
        List<String> warehouseId = new ArrayList<>();
        warehouseId.add("110");
        warehouseId.add("111");
        List<Inventory> inventoryList = inventoryRepository.findByWarehouseIdInAndDeletionIndicator(warehouseId, 0L);
        List<InventoryReport> reportInventoryList = new ArrayList<>();

        inventoryList = inventoryList.stream()
                .filter(data -> data != null &&
                        (
                                (data.getInventoryQuantity() != null && data.getInventoryQuantity() > 0) ||
                                        (data.getAllocatedQuantity() != null && data.getAllocatedQuantity() > 0)
                        ))
                .collect(Collectors.toList());

        for (Inventory dbInventory : inventoryList) {
            InventoryReport reportInventory = new InventoryReport();

            // WH_ID
            reportInventory.setWarehouseId(dbInventory.getWarehouseId());

            // ITM_CODE
            reportInventory.setItemCode(dbInventory.getItemCode());

            /*
             * ITEM_TEXT
             * -------------------------------------------------------------------------
             * Pass the fetched ITM_CODE values in IMBASICDATA1 table and fetch MFR_SKU
             * values
             */
            //HARESSH- 23-08-2022 - commented since data is already directly stored during inventory creation
//			try {
//				ImBasicData1 imbasicdata1 = imbasicdata1Repository.findByItemCodeAndWarehouseIdInAndDeletionIndicator(
//						dbInventory.getItemCode(), warehouseId, 0L);
//				if (imbasicdata1 != null) {
//					reportInventory.setDescription(imbasicdata1.getDescription());
//					reportInventory.setMfrPartNumber(imbasicdata1.getManufacturerPartNo());
//				}
//			} catch(Exception e) {
//				log.info("ERROR : imBasicData1 master get error " + dbInventory.getItemCode() + " " +
//						dbInventory.getWarehouseId(), e);
//			}

            reportInventory.setDescription(dbInventory.getReferenceField8());
            reportInventory.setMfrPartNumber(dbInventory.getReferenceField9());
            reportInventory.setStorageSectionId(dbInventory.getReferenceField10());
            reportInventory.setAisleId(dbInventory.getReferenceField5());
            reportInventory.setLevelId(dbInventory.getReferenceField6());
            reportInventory.setRowId(dbInventory.getReferenceField7());


            // INV_UOM
            reportInventory.setUom(dbInventory.getInventoryUom());

            // ST_BIN
            reportInventory.setStorageBin(dbInventory.getStorageBin());

            /*
             * ST_SEC_ID Pass the selected ST_BIN values into STORAGEBIN table and fetch
             * ST_SEC_ID values
             */

            //HARESSH- 23-08-2022 - commented since data is already directly stored during inventory creation
//			try {
//				String storagebin = storagebinRepository.findByStorageBin(dbInventory.getStorageBin());
//				reportInventory.setStorageSectionId(storagebin);
//			} catch(Exception e) {
//				log.info("ERROR : stBin master get error "+ dbInventory.getStorageBin(), e);
//			}

            // PACK_BARCODE
            reportInventory.setPackBarcodes(dbInventory.getPackBarcodes());

            // INV_QTY
            try {
                reportInventory.setInventoryQty(dbInventory.getInventoryQuantity() != null ? dbInventory.getInventoryQuantity() : 0);
                reportInventory.setAllocatedQty(dbInventory.getAllocatedQuantity() != null ? dbInventory.getAllocatedQuantity() : 0);

                reportInventory.setTotalQuantity(Double.sum(dbInventory.getInventoryQuantity() != null ? dbInventory.getInventoryQuantity() : 0,
                        dbInventory.getAllocatedQuantity() != null ? dbInventory.getAllocatedQuantity() : 0));
            } catch (Exception e) {
                log.info("ERROR : ALL_QTY , TOTAL_QTY CALCULATE  ", e);
            }

            // STCK_TYP_ID/STCK_TYP_TEXT
            reportInventory.setStockType(dbInventory.getStockTypeId());
            reportInventoryList.add(reportInventory);
        }
        return reportInventoryList;
    }

    /**
     *
     * @param warehouseId
     * @param itemCode
     * @param fromCreatedOn
     * @param toCreatedOn
     * @return
     * @throws java.text.ParseException
     */
//	public List<StockMovementReport> getStockMovementReport(String warehouseId, String itemCode, String fromCreatedOn,
//			String toCreatedOn) throws java.text.ParseException {
//		// Warehouse
//		if (warehouseId == null) {
//			throw new BadRequestException("WarehouseId can't be blank.");
//		}
//
//		// Item Code
//		if (itemCode == null) {
//			throw new BadRequestException("ItemCode can't be blank.");
//		}
//
//		// Date
//		if (fromCreatedOn == null || toCreatedOn == null) {
//			throw new BadRequestException("CreatedOn can't be blank.");
//		}
//
//		/*
//		 * Pass the Search paramaters (WH_ID,ITM_CODE,IM_CTD_ON) values in
//		 * INVENOTRYMOVEMENT table for the selected date and fetch the below output
//		 * values
//		 */
//		Date fromDate = null;
//		Date toDate = null;
//		try {
//			fromDate = DateUtils.convertStringToDate(fromCreatedOn);
//			fromDate = DateUtils.addTimeToDate(fromDate);
//			toDate = DateUtils.convertStringToDate(toCreatedOn);
//			toDate = DateUtils.addDayEndTimeToDate(toDate);
//		} catch (Exception e) {
//			throw new BadRequestException("Date shoud be in MM-dd-yyyy format.");
//		}
//
//		List<InventoryMovement> inventoryMovementSearchResults_123 = inventoryMovementRepository
//				.findByWarehouseIdAndItemCodeAndCreatedOnBetweenAndMovementTypeAndSubmovementTypeInOrderByCreatedOnAsc(warehouseId,
//						itemCode, fromDate, toDate, 1L, Arrays.asList(2L, 3L));
//		List<StockMovementReport> reportStockMovementList_1 = fillData(inventoryMovementSearchResults_123);
//		log.info("reportStockMovementList_1 : " + reportStockMovementList_1);
//		stockMovementReportRepository.saveAll(reportStockMovementList_1);
//		
//		List<InventoryMovement> inventoryMovementSearchResults_35 = inventoryMovementRepository
//				.findByWarehouseIdAndItemCodeAndCreatedOnBetweenAndMovementTypeAndSubmovementTypeInOrderByCreatedOnAsc(warehouseId,
//						itemCode, fromDate, toDate, 3L, Arrays.asList(5L));
//		List<StockMovementReport> reportStockMovementList_2 = fillData(inventoryMovementSearchResults_35);
//		log.info("reportStockMovementList_2 : " + reportStockMovementList_2);
//		stockMovementReportRepository.saveAll(reportStockMovementList_2);
//		
//		reportStockMovementList_1.addAll(reportStockMovementList_2);
//		return reportStockMovementList_1;
//	}

    /**
     * @param inventoryMovementSearchResults
     * @return
     */
    private List<StockMovementReport> fillData(List<InventoryMovement> inventoryMovementSearchResults) {
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
        List<StockMovementReport> reportStockMovementList = new ArrayList<>();
        for (InventoryMovement inventoryMovement : inventoryMovementSearchResults) {
            StockMovementReport stockMovementReport = new StockMovementReport();
            stockMovementReport.setStockMovementReportId(System.currentTimeMillis());

            // WH_ID
            stockMovementReport.setWarehouseId(inventoryMovement.getWarehouseId());

            // ITM_CODE
            stockMovementReport.setItemCode(inventoryMovement.getItemCode());

            /*
             * MFR_SKU -------------- Pass the fetched ITM_CODE values in IMBASICDATA1 table
             * and fetch MFR_SKU values
             */
            ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(inventoryMovement.getItemCode(),
                    inventoryMovement.getWarehouseId(), authTokenForMastersService.getAccess_token());
            stockMovementReport.setManufacturerSKU(imBasicData1.getManufacturerPartNo());

            // ITEM_TEXT
            stockMovementReport.setItemText(imBasicData1.getDescription());

            // MVT_QTY
            /*
             * "Fetch MVT_QTY values where 1. MVT_TYP_ID = 1, SUB_MVT_TYP_ID=2,3 (Subtract
             * MVT_QTY values between 2 and 3) 2. MVT_TYP_ID = 3, SUB_MVT_TYP_ID=5 3.
             * MVT_TYP_ID = 2"
             */
            if (inventoryMovement.getMovementType() == 1L && inventoryMovement.getSubmovementType() == 2L) {
                stockMovementReport.setMovementQty(inventoryMovement.getMovementQty());
            } else if (inventoryMovement.getMovementType() == 1L && inventoryMovement.getSubmovementType() == 3L) {
                stockMovementReport.setMovementQty(-inventoryMovement.getMovementQty()); // Assign -ve number
            } else if (inventoryMovement.getMovementType() == 3L && inventoryMovement.getSubmovementType() == 5L) {
                stockMovementReport.setMovementQty(inventoryMovement.getMovementQty());
            }

            /*
             * Document type --------------------------------------------------- If
             * MVT_TYP_ID = 1, Hard Coded value "Inbound" and if MVT_TYP_ID=3, Hard Coded
             * Value "Outbound", MVT_TYP_ID=2, Harcoded Value "Transfer"
             */
            if (inventoryMovement.getMovementType() == 1L) {
                stockMovementReport.setDocumentType("Inbound");
            } else if (inventoryMovement.getMovementType() == 3L) {
                stockMovementReport.setDocumentType("Outbound");
            }

            // Document Number
            stockMovementReport.setDocumentNumber(inventoryMovement.getRefDocNumber());

            /*
             * PARTNER_CODE --------------------------- 1. For MVT_TYP_ID = 1 records, pass
             * REF_DOC_NO in INBOUNDLINE table and fetch PARTNER_CODE values and fill 2. For
             * MVT_TYP_ID = 3 records, pass REF_DOC_NO in OUTBOUNDHEADER table and fetch
             * PARTNER_CODE values and fill 3. For MVT_TYP_ID = 2, Hard Coded Value "" BIN
             * to BIN"""
             */
            if (inventoryMovement.getMovementType() == 1L) {
                List<InboundLine> inboundLine = inboundLineService.getInboundLine(inventoryMovement.getRefDocNumber(), inventoryMovement.getWarehouseId());
                log.info("inboundLine : " + inboundLine);
                if (!inboundLine.isEmpty()) {
                    stockMovementReport.setCustomerCode(inboundLine.get(0).getVendorCode());
                }
            } else if (inventoryMovement.getMovementType() == 3L) {
                OutboundHeader outboundHeader = outboundHeaderService.getOutboundHeader(inventoryMovement.getRefDocNumber(), inventoryMovement.getWarehouseId());
                log.info("outboundHeader : " + outboundHeader);
                if (outboundHeader != null) {
                    stockMovementReport.setCustomerCode(outboundHeader.getPartnerCode());
                }
            }

            // Date & Time
            Date date = inventoryMovement.getCreatedOn();
            LocalDateTime datetime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String currentDate = datetime.format(newPattern);

            DateTimeFormatter newTimePattern = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = datetime.format(newTimePattern);
            stockMovementReport.setCreatedOn(currentDate);
            stockMovementReport.setCreatedTime(currentTime);

            Double balanceOHQty = 0D;
            Double movementQty = 0D;

            // BAL_OH_QTY
            if (inventoryMovement.getBalanceOHQty() != null) {
                balanceOHQty = inventoryMovement.getBalanceOHQty();
                stockMovementReport.setBalanceOHQty(balanceOHQty);
            }

            if (inventoryMovement.getMovementQty() != null) {
                movementQty = inventoryMovement.getMovementQty();
            }

            /*
             * Opening Stock ----------------------------- 1 For MVT_TYP_ID = 1 , opening
             * stock = BAL_OH_QTY - MVT_QTY 2. For MVT-TYP_ID = 3, opening stock =
             * BAL_OH_QTY + MVT_QTY 3. For MVT_TYP_ID = 2, Opening stock = BAL_OH_QTY +
             * MVT_QTY
             */
            if (inventoryMovement.getMovementType() == 1) {
                Double openingStock = balanceOHQty - movementQty;
                stockMovementReport.setOpeningStock(openingStock);
            } else if (inventoryMovement.getMovementType() == 3) {
                Double openingStock = balanceOHQty + movementQty;
                stockMovementReport.setOpeningStock(openingStock);
            }
            reportStockMovementList.add(stockMovementReport);
        }
        return reportStockMovementList;
    }

    /**
     * Get OrderStatusReport
     *
     * @param request
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public List<OrderStatusReport> getOrderStatusReport(SearchOrderStatusReport request) throws ParseException, java.text.ParseException {

        // LANG_ID
        if (request.getLanguageId() == null) {
            throw new BadRequestException("LanguageId can't be blank");
        }

        // C_ID
        if (request.getCompanyCodeId() == null) {
            throw new BadRequestException("CompanyCodeId can't be blank");
        }

        // PLANT_ID
        if (request.getPlantId() == null) {
            throw new BadRequestException("PlantId can't be blank");
        }

        // WH_ID
        if (request.getWarehouseId() == null) {
            throw new BadRequestException("WarehouseId can't be blank");
        }

        // DeliveryDate
        if (request.getFromDeliveryDate() == null || request.getToDeliveryDate() == null) {
            throw new BadRequestException("DeliveryDate can't be blank");
        }

        SearchOrderStatusReport searchOutboundLine = new SearchOrderStatusReport();
        searchOutboundLine.setWarehouseId(request.getWarehouseId());
        searchOutboundLine.setCompanyCodeId(request.getCompanyCodeId());
        searchOutboundLine.setPlantId(request.getPlantId());
        searchOutboundLine.setLanguageId(request.getLanguageId());

        if (request.getFromDeliveryDate() != null && request.getToDeliveryDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(request.getFromDeliveryDate(), request.getToDeliveryDate());
            searchOutboundLine.setFromDeliveryDate(dates[0]);
            searchOutboundLine.setToDeliveryDate(dates[1]);
        }

        List<OrderStatusReportImpl> outboundLineSearchResults = outboundLineService.findOutboundLineOrderStatusReport(searchOutboundLine);

        if (request.getCustomerCode() != null && !request.getCustomerCode().isEmpty()) {
            outboundLineSearchResults = outboundLineSearchResults.stream().filter(data -> request.getCustomerCode().contains(data.getPartnerCode())).collect(Collectors.toList());
        }
        log.info("outboundLineSearchResults customerCodeFilter: " + outboundLineSearchResults);
        if (request.getOrderNumber() != null && !request.getOrderNumber().isEmpty()) {
            outboundLineSearchResults = outboundLineSearchResults.stream().filter(data -> request.getOrderNumber().contains(data.getSoNumber())).collect(Collectors.toList());
        }
        log.info("outboundLineSearchResults orderNumberFilter: " + outboundLineSearchResults);
        if (request.getOrderType() != null && !request.getOrderType().isEmpty()) {
            outboundLineSearchResults = outboundLineSearchResults.stream().filter(data -> request.getOrderType().contains(data.getOrderType())).collect(Collectors.toList());
        }
        log.info("outboundLineSearchResults OrderTypeFilter: " + outboundLineSearchResults);
        if (request.getStatusId() != null && !request.getStatusId().isEmpty()) {
            outboundLineSearchResults = outboundLineSearchResults.stream().filter(data -> request.getStatusId().contains(data.getStatusId())).collect(Collectors.toList());
        }
        log.info("outboundLineSearchResults customerCodeFilter: " + outboundLineSearchResults);

        List<OrderStatusReport> reportOrderStatusReportList = new ArrayList<>();
        for (OrderStatusReportImpl outboundLine : outboundLineSearchResults) {
            OrderStatusReport orderStatusReport = new OrderStatusReport();

            orderStatusReport.setLanguageId(outboundLine.getLanguageId());  // LANG_ID
            orderStatusReport.setCompanyCodeId(outboundLine.getCompanyCodeId());  // C_ID
            orderStatusReport.setPlantId(outboundLine.getPlantId());  // PLANT_ID
            orderStatusReport.setWarehouseId(outboundLine.getWarehouseId()); // WH_ID
            orderStatusReport.setSoNumber(outboundLine.getSoNumber()); // REF_DOC_NO
            orderStatusReport.setDoNumber(outboundLine.getDoNumber()); // DLV_ORD_NO
            orderStatusReport.setCustomerCode(outboundLine.getPartnerCode()); // PARTNER_CODE
            orderStatusReport.setCustomerName(outboundLine.getPartnerName());
            orderStatusReport.setSku(outboundLine.getItemCode()); // ITM_CODE
            orderStatusReport.setSkuDescription(outboundLine.getItemDescription()); // ITEM_TEXT
            orderStatusReport.setOrderedQty(outboundLine.getOrderedQty()); // ORD_QTY
            orderStatusReport.setDeliveredQty(outboundLine.getDeliveryQty()); // DLV_QTY
            orderStatusReport.setDeliveryConfirmedOn(outboundLine.getDeliveryConfirmedOn()); // DLV_CNF_ON
            orderStatusReport.setOrderReceivedDate(outboundLine.getRefDocDate());
            orderStatusReport.setExpectedDeliveryDate(outboundLine.getRequiredDeliveryDate());
            orderStatusReport.setOrderType(outboundLine.getOrderType());
            orderStatusReport.setCompanyDescription(outboundLine.getCompanyDescription());
            orderStatusReport.setPlantDescription(outboundLine.getPlantDescription());
            orderStatusReport.setWarehouseDescription(outboundLine.getWarehouseDescription());
            orderStatusReport.setRefDocNumber(outboundLine.getSoNumber());
            orderStatusReport.setItemCode(outboundLine.getItemCode());

            // % of Delivered - (DLV_QTY/ORD_QTY)*100
            double deliveryQty = 0;
            double orderQty = 0;
            if (outboundLine.getDeliveryQty() != null) {
                deliveryQty = outboundLine.getDeliveryQty();
            }
            if (outboundLine.getOrderedQty() != null) {
                orderQty = outboundLine.getOrderedQty();
            }
            double percOfDlv = Math.round((deliveryQty / orderQty) * 100);
            orderStatusReport.setPercentageOfDelivered(percOfDlv);

            // STATUS_ID
            /*
             * Hard coded Options Delivered - if STATUS_ID 59, Partial deliveries -If
             * STATUS_ID 42,43,48,50,55 , Not fulfilled- STATUS_ID 51,47)
             * Hardcoded directly in query
             */
            orderStatusReport.setStatusId(outboundLine.getStatusIdName());
            reportOrderStatusReportList.add(orderStatusReport);
        }
        log.info("reportOrderStatusReportList: " + reportOrderStatusReportList);
        return reportOrderStatusReportList;
    }


    /**
     * @param warehouseId
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @param storeCode
     * @param soType
     * @param orderNumber
     * @return
     * @throws java.text.ParseException
     * @throws ParseException
     */
    public List<ShipmentDeliveryReport> getShipmentDeliveryReport(String warehouseId, String fromDeliveryDate,
                                                                  String toDeliveryDate, String storeCode, List<String> soType, String orderNumber)
            throws ParseException, java.text.ParseException {
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

        try {
            // WH_ID
            if (warehouseId == null) {
                throw new BadRequestException("WarehouseId can't be blank.");
            }

            if (orderNumber == null) {
                throw new BadRequestException("OrderNumber can't be blank.");
            }

            SearchOutboundLineReport searchOutboundLineReport = new SearchOutboundLineReport();
            searchOutboundLineReport.setWarehouseId(warehouseId);
            searchOutboundLineReport.setRefDocNumber(orderNumber);

            if (!storeCode.isEmpty()) {
                searchOutboundLineReport.setPartnerCode(storeCode);
                log.info("storeCode: " + storeCode);
            }

            if (!soType.isEmpty()) {
                searchOutboundLineReport.setSoTypeRefField1(soType);
                log.info("soType: " + soType);
            }

            if (fromDeliveryDate != null && fromDeliveryDate.trim().length() > 0 && toDeliveryDate != null
                    && toDeliveryDate.trim().length() > 0) {
                log.info("fromDeliveryDate : " + fromDeliveryDate);
                log.info("toDeliveryDate : " + toDeliveryDate);

                Date fromDeliveryDate_d = DateUtils.convertStringToDate(fromDeliveryDate);
                fromDeliveryDate_d = DateUtils.addTimeToDate(fromDeliveryDate_d);

                Date toDeliveryDate_d = DateUtils.convertStringToDate(toDeliveryDate);
                toDeliveryDate_d = DateUtils.addDayEndTimeToDate(toDeliveryDate_d);

                log.info("Date: " + fromDeliveryDate_d + "," + toDeliveryDate_d);

                searchOutboundLineReport.setStartConfirmedOn(fromDeliveryDate_d);
                searchOutboundLineReport.setEndConfirmedOn(toDeliveryDate_d);
                log.info("fromDeliveryDate_d : " + fromDeliveryDate_d);
            }

            List<OutboundLine> outboundLineSearchResults = outboundLineService
                    .findOutboundLineReport(searchOutboundLineReport);
            log.info("outboundLineSearchResults : " + outboundLineSearchResults);
            double total = 0;
            if (outboundLineSearchResults.isEmpty()) {
                log.info("Resultset is EMPTY");
            } else {
                total = outboundLineSearchResults.stream().filter(a -> a.getDeliveryQty() != null)
                        .mapToDouble(OutboundLine::getDeliveryQty).sum();
                log.info("total : " + total);
            }

            List<ShipmentDeliveryReport> shipmentDeliveryList = new ArrayList<>();
            for (OutboundLine outboundLine : outboundLineSearchResults) {
                ShipmentDeliveryReport shipmentDelivery = new ShipmentDeliveryReport();
                shipmentDelivery.setDeliveryDate(outboundLine.getDeliveryConfirmedOn());
                shipmentDelivery.setDeliveryTo(outboundLine.getPartnerCode());
                shipmentDelivery.setOrderType(outboundLine.getReferenceField1());
                shipmentDelivery.setCustomerRef(outboundLine.getRefDocNumber()); // REF_DOC_NO
                shipmentDelivery.setCommodity(outboundLine.getItemCode());
                shipmentDelivery.setDescription(outboundLine.getDescription());

                // Obtain Partner Name
                BusinessPartner partner = mastersService.getBusinessPartner(outboundLine.getPartnerCode(),
                        authTokenForMastersService.getAccess_token());
                shipmentDelivery.setPartnerName(partner.getPartnerName());

                /*
                 * MFR_PART
                 */
                ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(outboundLine.getItemCode(),
                        warehouseId, authTokenForMastersService.getAccess_token());
                if (imBasicData1 != null) {
                    shipmentDelivery.setManfCode(imBasicData1.getManufacturerPartNo());
                }

                shipmentDelivery.setQuantity(outboundLine.getDeliveryQty()); // DLV_QTY
                shipmentDelivery.setTotal(total);
                shipmentDeliveryList.add(shipmentDelivery);
            }
            return shipmentDeliveryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param warehouseId
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @param storeCode
     * @param soType
     * @param orderNumber
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public List<ShipmentDeliveryReport> getShipmentDeliveryReportV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                    String fromDeliveryDate, String toDeliveryDate, String storeCode,
                                                                    List<String> soType, String orderNumber)
            throws ParseException, java.text.ParseException {
        AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();

        try {
            // WH_ID
            if (warehouseId == null) {
                throw new BadRequestException("WarehouseId can't be blank.");
            }

            if (orderNumber == null) {
                throw new BadRequestException("OrderNumber can't be blank.");
            }

            SearchOutboundLineReportV2 searchOutboundLineReport = new SearchOutboundLineReportV2();
            searchOutboundLineReport.setWarehouseId(warehouseId);
            searchOutboundLineReport.setRefDocNumber(orderNumber);
            searchOutboundLineReport.setCompanyCodeId(Collections.singletonList(companyCodeId));
            searchOutboundLineReport.setPlantId(Collections.singletonList(plantId));
            searchOutboundLineReport.setLanguageId(Collections.singletonList(languageId));

            if (!storeCode.isEmpty()) {
                searchOutboundLineReport.setPartnerCode(storeCode);
                log.info("storeCode: " + storeCode);
            }

            if (!soType.isEmpty()) {
                searchOutboundLineReport.setSoTypeRefField1(soType);
                log.info("soType: " + soType);
            }

            if (fromDeliveryDate != null && fromDeliveryDate.trim().length() > 0 && toDeliveryDate != null
                    && toDeliveryDate.trim().length() > 0) {
                log.info("fromDeliveryDate : " + fromDeliveryDate);
                log.info("toDeliveryDate : " + toDeliveryDate);

                Date fromDeliveryDate_d = DateUtils.convertStringToDate(fromDeliveryDate);
                fromDeliveryDate_d = DateUtils.addTimeToDate(fromDeliveryDate_d);

                Date toDeliveryDate_d = DateUtils.convertStringToDate(toDeliveryDate);
                toDeliveryDate_d = DateUtils.addDayEndTimeToDate(toDeliveryDate_d);

                log.info("Date: " + fromDeliveryDate_d + "," + toDeliveryDate_d);

                searchOutboundLineReport.setStartConfirmedOn(fromDeliveryDate_d);
                searchOutboundLineReport.setEndConfirmedOn(toDeliveryDate_d);
                log.info("fromDeliveryDate_d : " + fromDeliveryDate_d);
            }

            List<OutboundLineV2> outboundLineSearchResults = outboundLineService
                    .findOutboundLineReportV2(searchOutboundLineReport);
            log.info("outboundLineSearchResults : " + outboundLineSearchResults);
            double total = 0;
            if (outboundLineSearchResults.isEmpty()) {
                log.info("Resultset is EMPTY");
            } else {
                total = outboundLineSearchResults.stream().filter(a -> a.getDeliveryQty() != null)
                        .mapToDouble(OutboundLine::getDeliveryQty).sum();
                log.info("total : " + total);
            }

            List<ShipmentDeliveryReport> shipmentDeliveryList = new ArrayList<>();
            for (OutboundLineV2 outboundLine : outboundLineSearchResults) {
                ShipmentDeliveryReport shipmentDelivery = new ShipmentDeliveryReport();
                shipmentDelivery.setDeliveryDate(outboundLine.getDeliveryConfirmedOn());
                shipmentDelivery.setDeliveryTo(outboundLine.getPartnerCode());
                shipmentDelivery.setOrderType(outboundLine.getReferenceField1());
                shipmentDelivery.setCustomerRef(outboundLine.getRefDocNumber()); // REF_DOC_NO
                shipmentDelivery.setCommodity(outboundLine.getItemCode());
                shipmentDelivery.setDescription(outboundLine.getDescription());
                shipmentDelivery.setManfCode(outboundLine.getManufacturerName());

                // Obtain Partner Name
//                BusinessPartner partner = mastersService.getBusinessPartner(outboundLine.getPartnerCode(),
//                        authTokenForMastersService.getAccess_token());
//                shipmentDelivery.setPartnerName(partner.getPartnerName());

                /*
                 * MFR_PART
                 */

//                ImBasicData1 imBasicData1 = mastersService.getImBasicData1ByItemCode(outboundLine.getItemCode(),
//                        warehouseId, authTokenForMastersService.getAccess_token());
//                if (imBasicData1 != null) {
//                    shipmentDelivery.setManfCode(imBasicData1.getManufacturerPartNo());
//                }

                shipmentDelivery.setQuantity(outboundLine.getDeliveryQty()); // DLV_QTY
                shipmentDelivery.setTotal(total);
                shipmentDeliveryList.add(shipmentDelivery);
            }
            return shipmentDeliveryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @param customerCode
     * @return
     * @throws java.text.ParseException
     * @throws ParseException
     */
    public ShipmentDeliverySummaryReport getShipmentDeliverySummaryReport(String fromDeliveryDate, String toDeliveryDate,
                                                                          List<String> customerCode, String warehouseIds,
                                                                          String companyCodeId, String plantId, String languageId)
            throws ParseException, java.text.ParseException {
        /*
         * Pass the Input Parameters in Outbound Line table (From and TO date in
         * DLV_CNF_ON fields) and fetch the below Fields, If Customer Code is Selected
         * all, Pass all the values into OUTBOUNDLINE table
         */
        // Date range
        if (fromDeliveryDate == null || toDeliveryDate == null) {
            throw new BadRequestException("DeliveryDate can't be blank.");
        }

        Date fromDeliveryDate_d = null;
        Date toDeliveryDate_d = null;
        try {
            log.info("Input Date: " + fromDeliveryDate + "," + toDeliveryDate);
            if (fromDeliveryDate.indexOf("T") > 0) {
                fromDeliveryDate_d = DateUtils.convertStringToDateWithT(fromDeliveryDate);
                toDeliveryDate_d = DateUtils.convertStringToDateWithT(toDeliveryDate);
            } else {
                fromDeliveryDate_d = DateUtils.addTimeToDate(fromDeliveryDate, 14, 0, 0);
                toDeliveryDate_d = DateUtils.addTimeToDate(toDeliveryDate, 13, 59, 59);
            }
            log.info("Date: " + fromDeliveryDate_d + "," + toDeliveryDate_d);
        } catch (Exception e) {
            throw new BadRequestException("Date should be in yyyy-MM-dd format.");
        }

        List<OutboundHeaderV2> outboundHeaderList = outboundHeaderV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndDeliveryConfirmedOnBetween(
                        companyCodeId, plantId, languageId, warehouseIds, 59L, fromDeliveryDate_d, toDeliveryDate_d);
        ShipmentDeliverySummaryReport shipmentDeliverySummaryReport = new ShipmentDeliverySummaryReport();
        List<ShipmentDeliverySummary> shipmentDeliverySummaryList = new ArrayList<>();
//        String languageId = null;
        String companyCode = null;
//        String plantId = null;
        String warehouseId = null;

        try {
            double sumOfLineItems = 0.0;
            Set<String> partnerCodes = new HashSet<>();
            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            for (OutboundHeaderV2 outboundHeader : outboundHeaderList) {
                languageId = outboundHeader.getLanguageId();
                companyCode = outboundHeader.getCompanyCodeId();
                plantId = outboundHeader.getPlantId();
                warehouseId = outboundHeader.getWarehouseId();
                partnerCodes.add(outboundHeader.getPartnerCode());

                // Report Preparation
                ShipmentDeliverySummary shipmentDeliverySummary = new ShipmentDeliverySummary();

                shipmentDeliverySummary.setSo(outboundHeader.getRefDocNumber());                            // SO
                shipmentDeliverySummary.setExpectedDeliveryDate(outboundHeader.getRequiredDeliveryDate());    // DEL_DATE
                shipmentDeliverySummary.setDeliveryDateTime(outboundHeader.getDeliveryConfirmedOn());        // DLV_CNF_ON
                shipmentDeliverySummary.setBranchCode(outboundHeader.getPartnerCode());                    // PARTNER_CODE/PARTNER_NM

                BusinessPartnerV2 dbBusinessPartner = mastersService.getBusinessPartnerV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(),
                        outboundHeader.getLanguageId(), outboundHeader.getWarehouseId(),
                        2L,
                        outboundHeader.getPartnerCode(),
                        authTokenForMastersService.getAccess_token());
                shipmentDeliverySummary.setBranchDesc(dbBusinessPartner.getPartnerName());

                shipmentDeliverySummary.setOrderType(outboundHeader.getReferenceField1());

                // Line Ordered
                List<Long> countOfOrderedLines = outboundLineService.getCountofOrderedLinesV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(),
                        outboundHeader.getLanguageId(), outboundHeader.getWarehouseId(),
                        outboundHeader.getPreOutboundNo(), outboundHeader.getRefDocNumber());

                // Line Shipped
                List<Long> deliveryLines = outboundLineService.getDeliveryLinesV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(),
                        outboundHeader.getLanguageId(), outboundHeader.getWarehouseId(),
                        outboundHeader.getPreOutboundNo(), outboundHeader.getRefDocNumber());

                // Ordered Qty
                List<Long> sumOfOrderedQty = outboundLineService.getSumOfOrderedQtyV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(),
                        outboundHeader.getLanguageId(), outboundHeader.getWarehouseId(),
                        outboundHeader.getPreOutboundNo(), outboundHeader.getRefDocNumber());

                // Shipped Qty
                List<Long> sumOfDeliveryQtyList = outboundLineService.getDeliveryQtyV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(),
                        outboundHeader.getLanguageId(), outboundHeader.getWarehouseId(),
                        outboundHeader.getPreOutboundNo(), outboundHeader.getRefDocNumber());

                double pickupLineCount = pickupLineService.getPickupLineCountV2(
                        outboundHeader.getCompanyCodeId(), outboundHeader.getPlantId(), outboundHeader.getLanguageId(),
                        outboundHeader.getWarehouseId(), outboundHeader.getPreOutboundNo(),
                        Arrays.asList(outboundHeader.getRefDocNumber()));

                double countOfOrderedLinesvalue = countOfOrderedLines.stream().mapToLong(Long::longValue).sum();
                double deliveryLinesCount = deliveryLines.stream().mapToLong(Long::longValue).sum();
                double sumOfOrderedQtyValue = sumOfOrderedQty.stream().mapToLong(Long::longValue).sum();
                double sumOfDeliveryQty = sumOfDeliveryQtyList.stream().mapToLong(Long::longValue).sum();

                sumOfLineItems += countOfOrderedLinesvalue;
                shipmentDeliverySummary.setLineOrdered(countOfOrderedLinesvalue);
                shipmentDeliverySummary.setLineShipped(deliveryLinesCount);
                shipmentDeliverySummary.setOrderedQty(sumOfOrderedQtyValue);
                shipmentDeliverySummary.setShippedQty(sumOfDeliveryQty);
                shipmentDeliverySummary.setPickedQty(pickupLineCount);

                // % Shipped - Divide (Shipped lines/Ordered Lines)*100
                double percShipped = Math.round((deliveryLinesCount / countOfOrderedLinesvalue) * 100);
                shipmentDeliverySummary.setPercentageShipped(percShipped);
                log.info("shipmentDeliverySummary : " + shipmentDeliverySummary);

                shipmentDeliverySummaryList.add(shipmentDeliverySummary);
            }

            // --------------------------------------------------------------------------------------------------------------------------------
            /*
             * Partner Code : 101, 102, 103, 107, 109, 111, 113 - Normal
             */
            //List<String> partnerCodes = Arrays.asList("101", "102", "103", "107", "109", "111", "112", "113");
            List<SummaryMetrics> summaryMetricsList = new ArrayList<>();
            for (String pCode : partnerCodes) {
                SummaryMetrics partnerCode_N = getMetricsDetails(languageId, companyCode, plantId, "N", warehouseId, pCode, "N", fromDeliveryDate_d,
                        toDeliveryDate_d);
                SummaryMetrics partnerCode_S = getMetricsDetails(languageId, companyCode, plantId, "S", warehouseId, pCode, "S", fromDeliveryDate_d,
                        toDeliveryDate_d);

                if (partnerCode_N != null) {
                    summaryMetricsList.add(partnerCode_N);
                }

                if (partnerCode_S != null) {
                    summaryMetricsList.add(partnerCode_S);
                }
            }

            shipmentDeliverySummaryReport.setShipmentDeliverySummary(shipmentDeliverySummaryList);
            shipmentDeliverySummaryReport.setSummaryMetrics(summaryMetricsList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipmentDeliverySummaryReport;
    }

//	/**
//	 * ShipmentDeliverySummary Report
//	 *
//	 * @param fromDeliveryDate
//	 * @param toDeliveryDate
//	 * @param customerCode
//	 * @return
//	 * @throws java.text.ParseException
//	 * @throws ParseException
//	 */
//	public ShipmentDispatchSummaryReport getShipmentDispatchSummaryReport(String fromDeliveryDate,
//			String toDeliveryDate, List<String> customerCode) throws ParseException, java.text.ParseException {
//		// Date range
//		if (fromDeliveryDate == null || toDeliveryDate == null) {
//			throw new BadRequestException("DeliveryDate can't be blank.");
//		}
//
//		/*
//		 * Pass the Input Parameters in Outbound Line table (Delivery date with From
//		 * 00:00 and TO 23:49 in DLV_CNF_ON fields) and fetch the below Fields, If
//		 * Customer Code is Selected as all or passed as Null, Pass Date ranges alone
//		 * into OUTBOUNDLINE table
//		 */
//		Date fromDate = null;
//		Date toDate = null;
//		try {
//			fromDate = DateUtils.convertStringToDate(fromDeliveryDate);
//			fromDate = DateUtils.addTimeToDate(fromDate);
//			log.info("Date---f----->: " + fromDate);
//
//			toDate = DateUtils.convertStringToDate(toDeliveryDate);
//			toDate = DateUtils.addDayEndTimeToDate(toDate);
//			log.info("Date---t----->: " + toDate);
//		} catch (Exception e) {
//			throw new BadRequestException("Date shoud be in MM-dd-yyyy format.");
//		}
//
//		SearchOutboundLine searchOutboundLine = new SearchOutboundLine();
//		searchOutboundLine.setFromDeliveryDate(fromDate);
//		searchOutboundLine.setToDeliveryDate(toDate);
//
//		if (!customerCode.isEmpty()) {
//			searchOutboundLine.setPartnerCode(customerCode);
//		}
//
//		List<OutboundLine> outboundLineSearchResults = outboundLineService
//				.findOutboundLineShipmentReport(searchOutboundLine);
//		log.info("outboundLineSearchResults----->: " + outboundLineSearchResults);
//
//		ShipmentDispatchSummaryReport shipmentDispatchSummary = new ShipmentDispatchSummaryReport();
//
//		// Printed on
//		List<String> partnerCodes = outboundLineSearchResults.stream().map(OutboundLine::getPartnerCode).distinct()
//				.collect(Collectors.toList());
//		log.info("partnerCodes----->: " + partnerCodes);
//
//		List<ShipmentDispatch> listShipmentDispatch = new ArrayList<>();
//		for (String partnerCode : partnerCodes) {
//			ShipmentDispatch shipmentDispatch = new ShipmentDispatch();
//
//			// Header
//			ShipmentDispatchHeader header = new ShipmentDispatchHeader();
//			header.setPartnerCode(partnerCode);
//
//			// Obtaining Child
//			List<OutboundLine> partnerOBLines = outboundLineSearchResults.stream()
//					.filter(a -> a.getPartnerCode().equalsIgnoreCase(partnerCode)).collect(Collectors.toList());
//			// List
//			List<ShipmentDispatchList> shipmentDispatchList = new ArrayList<>();
//			double totalLinesOrdered = 0.0;
//			double totalLinesShipped = 0.0;
//			double totalOrderedQty = 0.0;
//			double totalShippedQty = 0.0;
//
//			for (OutboundLine outboundLine : partnerOBLines) {
//				ShipmentDispatchList list = new ShipmentDispatchList();
//				list.setSoNumber(outboundLine.getRefDocNumber()); // REF_DOC_NO
//
//				/*
//				 * REF_DOC_DATE ----------------- Pass REF_DOC_NO in OUTBOUNDHEADER table and
//				 * fetch RFEF_DOC_DATE field value
//				 */
//				OutboundHeader outboundHeader = null;
//				try {
//					outboundHeader = outboundHeaderService.getOutboundHeader(outboundLine.getRefDocNumber());
//					log.info("outboundHeader : " + outboundHeader);
//				} catch (Exception e) {
//					List<OutboundHeader> outboundHeaders = outboundHeaderService.getOutboundHeaders();
//					outboundHeaders = outboundHeaders.stream()
//							.filter(a -> a.getRefDocNumber().equalsIgnoreCase(outboundLine.getRefDocNumber()))
//							.collect(Collectors.toList());
//					outboundHeader = outboundHeaders.get(0);
//					log.info("outboundHeader : " + outboundHeader);
//				}
//				list.setOrderReceiptTime(outboundHeader.getRefDocDate());
//
//				/*
//				 * Lines ordered ----------------- Pass WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE
//				 * in OUTBOUNDLINE and fetch the COUNT of Lines for OB_LINE_NO where REF_FIELD_2
//				 * = Null and display
//				 */
//				SearchOutboundLine obLineSearch = new SearchOutboundLine();
//				obLineSearch.setWarehouseId(Arrays.asList(outboundLine.getWarehouseId()));
//				obLineSearch.setPreOutboundNo(Arrays.asList(outboundLine.getPreOutboundNo()));
//				obLineSearch.setPartnerCode(partnerCodes);
//				obLineSearch.setRefDocNumber(Arrays.asList(outboundLine.getRefDocNumber()));
//
//				List<OutboundLine> obResults = outboundLineService.findOutboundLineShipmentReport(obLineSearch);
//				if (!obResults.isEmpty()) {
//					obResults = obResults.stream().filter(a -> a.getReferenceField2() == null)
//							.collect(Collectors.toList());
//					double linesOrdered = obResults.stream().count();
//					list.setLinesOrdered(linesOrdered);
//					totalLinesOrdered += linesOrdered;
//					log.info("linesOrdered : " + linesOrdered);
//					log.info("totalLinesOrdered : " + totalLinesOrdered);
//				}
//
//				/*
//				 * Lines shipped
//				 * -----------------------------------------------------------------------------
//				 * -------- Pass PRE_OB_NO/OB_LINE_NO/ITM_CODE/REF_DOC_NO/PARTNER_CODE in
//				 * OUTBOUNDLINE table and fetch Count of OB_LINE_NO values where
//				 * REF_FIELD_2=Null and DLV_QTY>0
//				 */
//				SearchOutboundLine obLineSearch1 = new SearchOutboundLine();
//				obLineSearch1.setPreOutboundNo(Arrays.asList(outboundLine.getPreOutboundNo()));
//				obLineSearch1.setLineNumber(Arrays.asList(outboundLine.getLineNumber()));
//				obLineSearch1.setItemCode(Arrays.asList(outboundLine.getItemCode()));
//				obLineSearch1.setPartnerCode(partnerCodes);
//				obLineSearch1.setRefDocNumber(Arrays.asList(outboundLine.getRefDocNumber()));
//				obResults = outboundLineService.findOutboundLineShipmentReport(obLineSearch1);
//				log.info("obResults : " + obResults);
//
//				if (!obResults.isEmpty()) {
//					double linesShipped = obResults.stream().filter(
//							a -> a.getReferenceField2() == null && a.getDeliveryQty() != null && a.getDeliveryQty() > 0)
//							.collect(Collectors.toList()).stream().count();
//
//					totalLinesShipped += linesShipped;
//					list.setLinesShipped(linesShipped);
//					log.info("linesShipped : " + linesShipped);
//					log.info("totalLinesShipped : " + totalLinesShipped);
//				}
//
//				/*
//				 * Ordered Qty ----------------- Pass WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CODE in
//				 * OUTBOUNDLINE and fetch the SUM of ORD_QTY values for OB_LINE_NO where
//				 * REF_FIELD_2 = Null and display
//				 */
//				double orderedQty = obResults.stream()
//						.filter(a -> a.getReferenceField2() == null && a.getOrderQty() != null)
//						.mapToDouble(OutboundLine::getOrderQty).sum();
//				list.setOrderedQty(orderedQty);
//				totalOrderedQty += orderedQty;
//				log.info("orderedQty : " + orderedQty);
//				log.info("totalOrderedQty : " + totalOrderedQty);
//
//				/*
//				 * Shipped qty ------------- Pass WH_ID/PRE_OB_NO/REF_DOC_NO/PARTNER_CDOE in
//				 * OUTBOUNDLINE and fetch the SUM of DLV_QTY values for OB_LINE_NO where
//				 * REF_FIELD_2 = Null and display
//				 */
//				double shippedQty = obResults.stream()
//						.filter(a -> a.getReferenceField2() == null && a.getDeliveryQty() != null)
//						.mapToDouble(OutboundLine::getDeliveryQty).sum();
//				list.setShippedQty(shippedQty);
//				totalShippedQty += shippedQty;
//				log.info("shippedQty : " + shippedQty);
//				log.info("totalShippedQty : " + totalShippedQty);
//
//				/*
//				 * % shipped - Divide (Shipped lines/Ordered Lines)*100
//				 */
//				double percShipped = Math.round((shippedQty / orderedQty) * 100);
//				list.setPerentageShipped(percShipped);
//				shipmentDispatchList.add(list);
//				log.info("percentage Shipped : " + percShipped);
//			}
//
//			/*
//			 * Total logic
//			 */
//			header.setTotalLinesOrdered(totalLinesOrdered);
//			header.setTotalLinesShipped(totalLinesShipped);
//			header.setTotalOrderedQty(totalOrderedQty);
//			header.setTotalShippedQty(totalShippedQty);
//			double averagePercentage = Math.round((totalShippedQty / totalOrderedQty) * 100);
//			header.setAveragePercentage(averagePercentage);
//			log.info("header : " + header);
//
//			shipmentDispatch.setHeader(header);
//			shipmentDispatch.setShipmentDispatchList(shipmentDispatchList);
//			listShipmentDispatch.add(shipmentDispatch);
//		}
//
//		shipmentDispatchSummary.setShipmentDispatch(listShipmentDispatch);
//		log.info("listShipmentDispatch : " + listShipmentDispatch);
//		return shipmentDispatchSummary;
//	}

    /**
     * ShipmentDeliverySummary Report
     *
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @param customerCode
     * @return
     * @throws java.text.ParseException
     * @throws ParseException
     */
    public ShipmentDispatchSummaryReport getShipmentDispatchSummaryReport(String fromDeliveryDate,
                                                                          String toDeliveryDate, List<String> customerCode, String warehouseId) throws ParseException, Exception {
        // Date range
        if (fromDeliveryDate == null || toDeliveryDate == null) {
            throw new BadRequestException("DeliveryDate can't be blank.");
        }

        /*
         * Pass the Input Parameters in Outbound Line table (Delivery date with From
         * 00:00 and TO 23:49 in DLV_CNF_ON fields) and fetch the below Fields, If
         * Customer Code is Selected as all or passed as Null, Pass Date ranges alone
         * into OUTBOUNDLINE table
         */
        Date fromDate = null;
        Date toDate = null;
        try {
            log.info("Input Date: " + fromDeliveryDate + "," + toDeliveryDate);
            if (fromDeliveryDate.indexOf("T") > 0) {
                fromDate = DateUtils.convertStringToDateWithT(fromDeliveryDate);
                toDate = DateUtils.convertStringToDateWithT(toDeliveryDate);
            } else {
                fromDate = DateUtils.addTimeToDate(fromDeliveryDate, 14, 0, 0);
                toDate = DateUtils.addTimeToDate(toDeliveryDate, 13, 59, 59);
            }
            log.info("Date: " + fromDate + "," + toDate);

        } catch (Exception e) {
            throw new BadRequestException("Date should be in yyyy-MM-dd format.");
        }

        SearchOutboundLine searchOutboundLine = new SearchOutboundLine();
        searchOutboundLine.setFromDeliveryDate(fromDate);
        searchOutboundLine.setToDeliveryDate(toDate);
        searchOutboundLine.setWarehouseId(new ArrayList<>());
        searchOutboundLine.getWarehouseId().add(warehouseId);
        List<ShipmentDispatchSummaryReportImpl> outboundLineSearchResults = outboundLineService
                .findOutboundLineShipmentReport(searchOutboundLine);
        log.info("outboundLineSearchResults----->: " + outboundLineSearchResults);

        if (customerCode != null && !customerCode.isEmpty()) {
            outboundLineSearchResults = outboundLineSearchResults.stream().filter(data -> customerCode.contains(data.getPartnerCode())).collect(Collectors.toList());
        }

        List<OutboundHeader> outboundHeaderList = outboundHeaderRepository
                .findByWarehouseIdAndStatusIdAndDeliveryConfirmedOnBetween(warehouseId, 59L, fromDate, toDate);
        List<String> refDocNoList = outboundHeaderList.stream().map(OutboundHeader::getRefDocNumber).collect(Collectors.toList());
        log.info("refDocNoList : " + refDocNoList);

        ShipmentDispatchSummaryReport shipmentDispatchSummary = new ShipmentDispatchSummaryReport();

        // Printed on
        List<String> partnerCodes = outboundLineSearchResults.stream().map(ShipmentDispatchSummaryReportImpl::getPartnerCode).distinct()
                .collect(Collectors.toList());
        log.info("partnerCodes----->: " + partnerCodes);

        List<ShipmentDispatch> listShipmentDispatch = new ArrayList<>();
        for (String partnerCode : partnerCodes) {
            ShipmentDispatch shipmentDispatch = new ShipmentDispatch();

            // Header
            ShipmentDispatchHeader header = new ShipmentDispatchHeader();

            // Obtain Partner Name
            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            BusinessPartner partner = mastersService.getBusinessPartner(partnerCode, authTokenForMastersService.getAccess_token());
            header.setPartnerCode(partnerCode + "-" + partner.getPartnerName());

            // Obtaining Child
            List<ShipmentDispatchSummaryReportImpl> partnerOBLines = outboundLineSearchResults.stream()
                    .filter(a -> a.getPartnerCode().equalsIgnoreCase(partnerCode)).collect(Collectors.toList());

            // List
            List<ShipmentDispatchList> shipmentDispatchList = new ArrayList<>();
            double totalLinesOrdered = 0.0;
            double totalLinesShipped = 0.0;
            double totalOrderedQty = 0.0;
            double totalShippedQty = 0.0;
            double totalPickedQty = 0.0;
            for (ShipmentDispatchSummaryReportImpl outboundLine : partnerOBLines) {
                ShipmentDispatchList list = new ShipmentDispatchList();
                list.setSoNumber(outboundLine.getSoNumber()); // REF_DOC_NO
                list.setOrderReceiptTime(outboundLine.getOrderReceiptTime());
                list.setLinesOrdered(outboundLine.getLinesOrdered());

                totalLinesOrdered += outboundLine.getLinesOrdered();
                log.info("linesOrdered : " + outboundLine.getLinesOrdered());
                log.info("totalLinesOrdered : " + totalLinesOrdered);

                list.setLinesShipped(outboundLine.getLinesShipped());

                totalLinesShipped += outboundLine.getLinesShipped();
                log.info("linesShipped : " + outboundLine.getLinesShipped());
                log.info("totalLinesShipped : " + totalLinesShipped);

                list.setOrderedQty(outboundLine.getOrderedQty());
                totalOrderedQty += outboundLine.getOrderedQty();
                log.info("orderedQty : " + outboundLine.getOrderedQty());
                log.info("totalOrderedQty : " + totalOrderedQty);

                list.setShippedQty(outboundLine.getShippedQty());
                totalShippedQty += outboundLine.getShippedQty();
                log.info("shippedQty : " + outboundLine.getShippedQty());
                log.info("totalShippedQty : " + totalShippedQty);

                list.setPerentageShipped(outboundLine.getPercentageShipped());
                log.info("percentage Shipped : " + outboundLine.getPercentageShipped());

                // PickupLine Count
                //-------------------PickupLine-Count-----------------------------------------------------
                List<String> preOutboundNoList = outboundHeaderList.stream()
                        .filter(o -> o.getRefDocNumber().equalsIgnoreCase(outboundLine.getSoNumber()) &&
                                o.getPartnerCode().equalsIgnoreCase(partnerCode))
                        .map(OutboundHeader::getPreOutboundNo).collect(Collectors.toList());

                log.info("preOutboundNoList : " + preOutboundNoList);
                double pickupLineCount = pickupLineService.getPickupLineCount(getLanguageId(), getCompanyCode(), getPlantId(),
                        warehouseId, Arrays.asList(preOutboundNoList.get(0)), Arrays.asList(outboundLine.getSoNumber()), partnerCode);
                log.info("pickupLineCount Picked : " + pickupLineCount);

                list.setPickedQty(pickupLineCount);
                totalPickedQty += pickupLineCount;
                shipmentDispatchList.add(list);
            }

            /*
             * Total logic
             */
            header.setTotalLinesOrdered(totalLinesOrdered);
            header.setTotalLinesShipped(totalLinesShipped);
            header.setTotalOrderedQty(totalOrderedQty);
            header.setTotalShippedQty(totalShippedQty);
            header.setTotalPickedQty(totalPickedQty);

            double averagePercentage = Math.round((totalShippedQty / totalOrderedQty) * 100);
            header.setAveragePercentage(averagePercentage);
            log.info("header : " + header);

            shipmentDispatch.setHeader(header);
            shipmentDispatch.setShipmentDispatchList(shipmentDispatchList);
            listShipmentDispatch.add(shipmentDispatch);
        }

        shipmentDispatchSummary.setShipmentDispatch(listShipmentDispatch);
//		log.info("listShipmentDispatch : " + listShipmentDispatch);
        return shipmentDispatchSummary;
    }

    /**
     * @param asnNumber
     * @return
     * @throws Exception
     */
    public ReceiptConfimationReport getReceiptConfimationReport(String asnNumber) throws Exception {
        if (asnNumber == null) {
            throw new BadRequestException("ASNNumber can't be blank");
        }

        ReceiptConfimationReport receiptConfimation;
        try {
            receiptConfimation = new ReceiptConfimationReport();
            ReceiptHeader receiptHeader = new ReceiptHeader();

            // 22-08-2022-Hareesh //commented the not used method call

//			SearchInboundHeader searchInboundHeader = new SearchInboundHeader();
//			searchInboundHeader.setRefDocNumber(Arrays.asList(asnNumber));
//			List<InboundHeader> inboundHeaderSearchResults = inboundHeaderService
//					.findInboundHeader(searchInboundHeader);
//			log.info("inboundHeaderSearchResults : " + inboundHeaderSearchResults);

//			List<InboundLine> inboundLineSearchResults = inboundLineService.getInboundLine(asnNumber);
            List<InboundLineV2> inboundLineSearchResults = inboundLineService.getInboundLineV2(asnNumber);
//			log.info("inboundLineSearchResults ------>: " + inboundLineSearchResults);

            double sumTotalOfExpectedQty = 0.0;
            double sumTotalOfAccxpectedQty = 0.0;
            double sumTotalOfDamagedQty = 0.0;
            double sumTotalOfMissingORExcess = 0.0;
            List<Receipt> receiptList = new ArrayList<>();
            log.info("inboundLine---------> : " + inboundLineSearchResults);
            if (!inboundLineSearchResults.isEmpty()) {
                // Supplier - PARTNER_CODE
                receiptHeader.setSupplier(inboundLineSearchResults.get(0).getVendorCode());

                receiptHeader.setSupplierName(inboundLineSearchResults.get(0).getSupplierName());

                // Container No
                receiptHeader.setContainerNo(inboundLineSearchResults.get(0).getContainerNo());

                // Order Number - REF_DOC_NO
                receiptHeader.setOrderNumber(inboundLineSearchResults.get(0).getRefDocNumber());

                // Order Type -> PREINBOUNDHEADER - REF_DOC_TYPE
                // Pass REF_DOC_NO in PREINBOUNDHEADER and fetch REF_DOC_TYPE
                String referenceDocumentType = preInboundHeaderService.getReferenceDocumentTypeFromPreInboundHeader(
                        inboundLineSearchResults.get(0).getWarehouseId(), inboundLineSearchResults.get(0).getPreInboundNo(),
                        inboundLineSearchResults.get(0).getRefDocNumber());
                receiptHeader.setOrderType(referenceDocumentType);
                log.info("preInboundHeader referenceDocumentType--------> : " + referenceDocumentType);
            }
            for (InboundLineV2 inboundLine : inboundLineSearchResults) {


                Receipt receipt = new Receipt();

                // SKU - ITM_CODE
                receipt.setSku(inboundLine.getItemCode());

                // Description - ITEM_TEXT
                receipt.setDescription(inboundLine.getDescription());

                // Mfr.Sku - MFR_PART
//				receipt.setMfrSku(inboundLine.getManufacturerPartNo());
                receipt.setMfrSku(inboundLine.getManufacturerName());

                // Expected - ORD_QTY
                double expQty = 0;
                if (inboundLine.getOrderQty() != null) {
                    expQty = inboundLine.getOrderQty();
                    receipt.setExpectedQty(expQty);
                    sumTotalOfExpectedQty += expQty;
                    log.info("expQty------#--> : " + expQty);
                }

                // Accepted - ACCEPT_QTY
                double acceptQty = 0;
                if (inboundLine.getAcceptedQty() != null) {
                    acceptQty = inboundLine.getAcceptedQty();
                    receipt.setAcceptedQty(acceptQty);
                    sumTotalOfAccxpectedQty += acceptQty;
                    log.info("acceptQty------#--> : " + acceptQty);
                }

                // Damaged - DAMAGE_QTY
                double damageQty = 0;
                if (inboundLine.getDamageQty() != null) {
                    damageQty = inboundLine.getDamageQty();
                    receipt.setDamagedQty(damageQty);
                    sumTotalOfDamagedQty += damageQty;
                    log.info("damageQty------#--> : " + damageQty);
                }

                // Missing/Excess - SUM(Accepted + Damaged) - Expected
                double missingORExcessSum = (acceptQty + damageQty) - expQty;
                sumTotalOfMissingORExcess += missingORExcessSum;
                receipt.setMissingORExcess(missingORExcessSum);
                log.info("missingORExcessSum------#--> : " + missingORExcessSum);

                // Status
                /*
                 * 1. If Missing/Excess = 0, then hardcode Status as ""Received"" 2. If Damage
                 * qty is greater than zero, then Hard code status ""Damage Received"" 3. If
                 * Missing/Excess is less than 0, then hardcode Status as ""Partial Received""
                 * 4. If Missing/Excess is excess than 0, then hardcode Status as ""Excess
                 * Received"" 5. If Sum (Accepted Qty +Damaged qty) is 0, then Hardcode status
                 * as ""Not yet received""
                 */
                if (missingORExcessSum == 0) {
                    receipt.setStatus("Received");
                } else if (damageQty > 0) {
                    receipt.setStatus("Damage Received");
                } else if (missingORExcessSum < 0) {
                    receipt.setStatus("Partial Received");
                } else if (missingORExcessSum > 0) {
                    receipt.setStatus("Excess Received");
                } else if (sumTotalOfMissingORExcess == 0) {
                    receipt.setStatus("Not Received");
                }
                log.info("receipt------#--> : " + receipt);
                receiptList.add(receipt);
            }

            receiptHeader.setExpectedQtySum(sumTotalOfExpectedQty);
            receiptHeader.setAcceptedQtySum(sumTotalOfAccxpectedQty);
            receiptHeader.setDamagedQtySum(sumTotalOfDamagedQty);
            receiptHeader.setMissingORExcessSum(sumTotalOfMissingORExcess);

            receiptConfimation.setReceiptHeader(receiptHeader);
            receiptConfimation.setReceiptList(receiptList);
            log.info("receiptConfimation : " + receiptConfimation);
            return receiptConfimation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param languageId
     * @param companyCode
     * @param plantId
     * @param type
     * @param warehouseId
     * @param partnerCode
     * @param refField1
     * @param fromDeliveryDate_d
     * @param toDeliveryDate_d
     * @return
     * @throws ParseException
     * @throws Exception
     */
    private SummaryMetrics getMetricsDetails(String languageId, String companyCode, String plantId, String type,
                                             String warehouseId, String partnerCode, String refField1, Date fromDeliveryDate_d, Date toDeliveryDate_d) throws ParseException, Exception {
        List<OutboundHeaderV2> outboundHeaderList = outboundHeaderV2Repository
                .findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndPartnerCodeAndDeliveryConfirmedOnBetween(
                        companyCode, plantId, languageId, warehouseId, 59L, partnerCode, fromDeliveryDate_d, toDeliveryDate_d);
        log.info("partnerCode--->: " + partnerCode + " refField1:----> : " + refField1 + "--:fromDeliveryDate_d:---> : " + fromDeliveryDate_d
                + " --> toDeliveryDate_d;  " + toDeliveryDate_d);
        log.info("---------------------->outboundHeaderList : " + outboundHeaderList);

        List<String> refDocNoList = outboundHeaderList.stream()
                .filter(a -> a.getReferenceField1().equalsIgnoreCase(refField1)).map(OutboundHeader::getRefDocNumber)
                .collect(Collectors.toList());
        log.info("refDocNoList : " + refDocNoList);

        List<String> preOutboundNoList = outboundHeaderList.stream()
                .filter(a -> a.getReferenceField1().equalsIgnoreCase(refField1) &&
                        a.getPartnerCode().equalsIgnoreCase(partnerCode))
                .map(OutboundHeader::getPreOutboundNo)
                .collect(Collectors.toList());
        log.info("preOutboundNoList : " + preOutboundNoList);

        List<String> refField1List = outboundHeaderList.stream().map(OutboundHeader::getReferenceField1)
                .collect(Collectors.toList());

        Long totalOrdeCount = refField1List.stream().filter(a -> a.equalsIgnoreCase(refField1)).count();
        log.info("refField1List : " + refField1List + "," + totalOrdeCount);

        /*
         * 101- Line items(N) ----------------------- Pass the selected REF_DOC_NO in
         * OUTBOUNDLINE table and fetch the COUNT of Lines for OB_LINE_NO where
         * REF_FIELD_2 = Null and display (Ordered Lines)
         */
        List<Long> outboundLineLineItems = outboundLineService.getLineItem_NByRefDocNoAndRefField2IsNullV2(
                companyCode, plantId, languageId, warehouseId, refDocNoList);

        if (!outboundLineLineItems.isEmpty()) {
            //double line_item_N = outboundLineLineItems.stream().count();
            double line_item_N = outboundLineLineItems.stream().mapToLong(Long::longValue).sum();
            log.info("line_item_N : " + line_item_N);

            /*
             * 101 % Shipped(N) -------------------------- Pass the selected REF_DOC_NO in
             * OUTBOUNDLINE table and fetch Count of OB_LINE_NO values where
             * REF_FIELD_2=Null and DLV_QTY>0 (Shipped Lines) % Shipped = (Shipped
             * Lines/Order Lines) * 100"
             */
            List<Long> shipped_lines_NList = outboundLineService.getShippedLinesV2(
                    companyCode, plantId, languageId, warehouseId, refDocNoList, fromDeliveryDate_d, toDeliveryDate_d);

            double shipped_lines_N = shipped_lines_NList.stream().mapToLong(Long::longValue).sum();
            double percShipped_N = Math.round((shipped_lines_N / line_item_N) * 100);

            // PickupLine Count
            log.info("---getMetricsDetails--1---pickupLineCount--------> : " + preOutboundNoList + "," + refDocNoList + "," + partnerCode);
            double pickupLineCount = pickupLineService.getPickupLineCount(languageId, companyCode, plantId, warehouseId,
                    preOutboundNoList, refDocNoList, partnerCode);
            log.info("---getMetricsDetails--2--pickupLineCount--------> : " + pickupLineCount);

            // SumOfOrderQty by PartnerCode
            Long sumOfOrderQty =
                    outboundLineService.getSumOfOrderedQtyByPartnerCodeV2(companyCode, plantId, languageId,
                            warehouseId, preOutboundNoList, refDocNoList, partnerCode);

            // SunOfDeliveryQty by PartnerCode
            Long sumOfDeliveryQty =
                    outboundLineService.getDeliveryQtyByPartnerCodeV2(companyCode, plantId, languageId,
                            warehouseId, preOutboundNoList, refDocNoList, partnerCode);

            // Populate Metrics
            MetricsSummary metricsSummary = new MetricsSummary();
            metricsSummary.setTotalOrder(totalOrdeCount);
            metricsSummary.setLineItems(line_item_N);
            metricsSummary.setPercentageShipped(percShipped_N);
            metricsSummary.setLineItemPicked(pickupLineCount);
            metricsSummary.setOrderedQty(sumOfOrderQty);
            metricsSummary.setDeliveryQty(sumOfDeliveryQty);
            metricsSummary.setShippedLines(shipped_lines_N);

            // Obtain Partner Name
            AuthToken authTokenForMastersService = authTokenService.getMastersServiceAuthToken();
            BusinessPartnerV2 partner = mastersService.getBusinessPartnerV2(companyCode, plantId, languageId, warehouseId, 2L,
                    partnerCode, authTokenForMastersService.getAccess_token());

            SummaryMetrics summaryMetrics = new SummaryMetrics();
            summaryMetrics.setPartnerCode(partnerCode + "-" + partner.getPartnerName());
            summaryMetrics.setType(type);
            summaryMetrics.setMetricsSummary(metricsSummary);
            return summaryMetrics;
        }
        return null;
    }

    /**
     * processInventory
     *
     * @param warehouseId
     * @param itemCode
     * @param stockTypeId
     * @return
     */
    private double getInventoryQty(String warehouseId, String itemCode, Long stockTypeId,
                                   List<String> storageSectionIds) {
        try {
            List<Inventory> stBinInventoryList = inventoryService.getInventoryForStockReport(warehouseId, itemCode,
                    stockTypeId);
            if (!stBinInventoryList.isEmpty()) {
                List<String> stBins = stBinInventoryList.stream().map(Inventory::getStorageBin)
                        .collect(Collectors.toList());
                log.info("stBins : " + stBins);
                log.info("storageSectionIds : " + storageSectionIds);

                List<StorageBin> storagebinList = storagebinRepository
                        .findByStorageBinInAndStorageSectionIdInAndPutawayBlockAndPickingBlockAndDeletionIndicatorOrderByStorageBinDesc(
                                stBins, storageSectionIds, 0, 0, 0L);
                log.info("storagebinList : " + storagebinList);
                if (storagebinList != null && !storagebinList.isEmpty()) {
                    List<String> storageBinList = storagebinList.stream().map(StorageBin::getStorageBin)
                            .collect(Collectors.toList());
                    log.info("inventory-params-----> : wh_id:" + warehouseId + "," + ",itemCode:" + itemCode
                            + ",storageBinList:" + storageBinList + ",stockTypeId: " + stockTypeId);

                    List<Long> inventoryQtyCountList = inventoryService.getInventoryQtyCount(warehouseId, itemCode,
                            storageBinList, stockTypeId);
                    log.info("inventoryList--------> : "
                            + inventoryQtyCountList.stream().mapToLong(Long::longValue).sum());

                    long qty = inventoryQtyCountList.stream().mapToLong(Long::longValue).sum();
                    log.info("inventoryList----qty----> : " + qty);

                    return qty;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * getMobileDashboard
     *
     * @param warehouseId
     * @return
     */
    public MobileDashboard getMobileDashboard(String companyCode, String plantId, String warehouseId, String languageId, String loginUserID) {
        MobileDashboard mobileDashboard = new MobileDashboard();

        /*--------------Inbound--------------------------------*/
        MobileDashboard.InboundCount inboundCount = mobileDashboard.new InboundCount();

        // -------------Cases------------------------------------
        // Pass Login WH_ID into STAGINGHEADER table and fetch the count of records
        // where STATUS_ID=12
//		List<StagingHeader> stagingHeaderList = stagingHeaderService.getStagingHeaderCount(warehouseId);
//		long cases = stagingHeaderList.stream().count();
//		inboundCount.setCases(cases);

        List<Long> statusId = Arrays.asList(16L);
        List<GrHeader> grHeaders = grHeaderService.getGrHeader(companyCode, plantId, languageId, warehouseId, statusId);
        long cases = grHeaders.stream().count();
        inboundCount.setCases(cases);

        // -------------Putaway----------------------------------
        // Pass Login WH_ID into PUTAWAYHEADER table and fetch the count of records
        // where STATUS_ID=19
        // and IB_ORD_TYP_ID= 1 and 3
        List<Long> orderTypeId = Arrays.asList(1L, 3L, 4L, 5L);
        List<PutAwayHeader> putAwayHeaderList = putAwayHeaderService.getPutAwayHeaderCount(
                companyCode, plantId, languageId, warehouseId, orderTypeId);
        long putaway = putAwayHeaderList.stream().count();
        inboundCount.setPutaway(putaway);

        // -------------Reversals-------------------------------
        // Pass Login WH_ID into PUTAWAYHEADER table and fetch the count of records
        // where STATUS_ID=19
        // and IB_ORD_TYP_ID= 2 and 4
        orderTypeId = Arrays.asList(2L);
        putAwayHeaderList = putAwayHeaderService.getPutAwayHeaderCount(companyCode, plantId, languageId, warehouseId, orderTypeId);
        long reversals = putAwayHeaderList.stream().count();
        inboundCount.setReversals(reversals);

        //Get LevelId from User HHtUser
        String levelId = pickupLineRepository.getLevelId(companyCode, plantId, languageId, warehouseId, loginUserID);

        /*--------------Outbound--------------------------------*/
        MobileDashboard.OutboundCount outboundCount = mobileDashboard.new OutboundCount();

        // --------------Picking---------------------------------------------------------------------------
        // Pass Login WH_ID into PICKUPHEADER table and fetch the count of records where
        // STATUS_ID=48 and
        // OB_ORD_TYP_ID= 0,1 and 3
        orderTypeId = Arrays.asList(0L, 1L, 3L);
        List<PickupHeaderV2> pickupHeaderList =
                pickupHeaderService.getPickupHeaderCount(companyCode, plantId, languageId, warehouseId, levelId, orderTypeId);
        long picking = pickupHeaderList.stream().count();
        outboundCount.setPicking(picking);

        // -------------Reversals-------------------------------------------------------------------------
        // Pass Login WH_ID into PICKUPHEADER table and fetch the count of records where
        // STATUS_ID=48 and
        // OB_ORD_TYP_ID= 2
        orderTypeId = Arrays.asList(2L);
        pickupHeaderList = pickupHeaderService.getPickupHeaderCount(companyCode, plantId, languageId, warehouseId, levelId, orderTypeId);
        reversals = pickupHeaderList.stream().count();
        outboundCount.setReversals(reversals);

        // -----------Quality-----------------------------------------------------------------------------
        // Pass Login WH_ID into QUALITYHEADER table and fetch the count of records
        // where STATUS_ID=54


        List<Long> qualityHeaderCount = qualityHeaderRepository.getQualityHeaderCount(companyCode, plantId, languageId, warehouseId,54L);
//        List<QualityHeader> qualityHeader = qualityHeaderService.getQualityHeaderCount(companyCode, plantId, languageId, warehouseId);
        long quality = qualityHeaderCount.stream().count();

        outboundCount.setQuality(quality);

        /*--------------StockCount--------------------------------*/
        MobileDashboard.StockCount stockCount = mobileDashboard.new StockCount();

        List<Long> status2 = Arrays.asList(72L, 75L);

        List<PerpetualLine> perpetualLines = perpetualLineService.getPerpetualLine(companyCode, languageId, plantId, warehouseId, loginUserID, status2);
        List<PeriodicLine> periodicLines = periodicLineService.getPeriodicLine(companyCode, languageId, plantId, warehouseId, loginUserID, status2);

        long perpetualCount = perpetualLines.stream().count();
        long periodicLineCount = periodicLines.stream().count();

        stockCount.setPerpertual(perpetualCount);
        stockCount.setPeriodic(periodicLineCount);

        //------------PERPETUAL & PERIODIC COUNT----------------------------------------------------------
        // Pass Login WH_ID into PERPETUAL & PERIODIC LINE table and fetch the count of records
        // where STATUS_ID = 72
//		try {
//			SearchPerpetualLine searchPerpetualLine = new SearchPerpetualLine();
////			searchPerpetualLine.setCompanyCodeId(Collections.singletonList(companyCode));
////			searchPerpetualLine.setLanguageId(Collections.singletonList(languageId));
////			searchPerpetualLine.setPlantId(Collections.singletonList(plantId));
//			searchPerpetualLine.setWarehouseId(warehouseId);
//			searchPerpetualLine.setLineStatusId(Arrays.asList(72L));
//			List<PerpetualLine> perpetualLines = perpetualLineService.findPerpetualLine(searchPerpetualLine);
//			long perpetualCount = perpetualLines.stream().count();
//
//			SearchPeriodicLine searchPeriodicLine = new SearchPeriodicLine();
//			searchPeriodicLine.setWarehouseId(warehouseId);
//			searchPeriodicLine.setLineStatusId(Arrays.asList(72L));
//			List<PeriodicLine> periodicLines = periodicLineService.findPeriodicLine(searchPeriodicLine);
//			long periodicLineCount = periodicLines.stream().count();
//
//			stockCount.setPerpertual(perpetualCount);
//			stockCount.setPeriodic(periodicLineCount);
//			log.info("stockCount : " + stockCount);

//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        mobileDashboard.setInboundCount(inboundCount);
        mobileDashboard.setOutboundCount(outboundCount);
        mobileDashboard.setStockCount(stockCount);
        return mobileDashboard;
    }

    /**
     * AwaitingASN Count
     *
     * @param warehouseId
     * @param fromCreatedOn
     * @param toCreatedOn
     * @return
     * @throws java.text.ParseException
     */
    @Transactional
    private long getAwaitingASNCount(String warehouseId, Date fromCreatedOn, Date toCreatedOn)
            throws java.text.ParseException {
        /*
         * Receipts - Awaiting ASN -------------------------- Pass the logged in WH_ID
         * and current date in CR_CTD_ON field in CONTAINERRECEIPT table and fetch the
         * count of records where REF_DOC_NO is Null
         */
        Date[] dates = DateUtils.addTimeToDatesForSearch(fromCreatedOn, toCreatedOn);
        fromCreatedOn = dates[0];
        toCreatedOn = dates[1];
        long awaitingASNCount = containerReceiptRepository.countByWarehouseIdAndContainerReceivedDateBetweenAndRefDocNumberIsNull(warehouseId, fromCreatedOn, toCreatedOn);
        log.info("awaitingASNCount : " + awaitingASNCount);
        return awaitingASNCount;
    }

    /**
     * @param warehouseId
     * @param startConfirmedOn
     * @param endConfirmedOn
     * @return
     * @throws Exception
     */
    @Transactional
    private long getContainerReceivedCount(String warehouseId, Date startConfirmedOn, Date endConfirmedOn)
            throws Exception {
        /*
         * Container Received ------------------------ Pass the logged in WH_ID and
         * current date in IB_CNF_ON field in INBOUNDHEADER table and fetch the count of
         * records
         */
        Date[] dates = DateUtils.addTimeToDatesForSearch(startConfirmedOn, endConfirmedOn);
        startConfirmedOn = dates[0];
        endConfirmedOn = dates[1];
        long containerReceivedCount = inboundHeaderRepository.countByWarehouseIdAndConfirmedOnBetweenAndStatusIdAndDeletionIndicator(
                warehouseId, startConfirmedOn, endConfirmedOn, 24L, 0L);
        log.info("containerReceivedCount : " + containerReceivedCount);
        return containerReceivedCount;
    }

    /**
     * @param warehouseId
     * @param startConfirmedOn
     * @param endConfirmedOn
     * @return
     * @throws Exception
     */
    @Transactional
    private long getItemReceivedCount(String warehouseId, Date startConfirmedOn, Date endConfirmedOn) throws Exception {
        /*
         * Item Received ------------------- Pass the logged in WH_ID and current date
         * in IB_CNF_ON field in INBOUNDLINE table and fetch the count of records where
         * REF_FIELD_1 is Null
         */
        Date[] dates = DateUtils.addTimeToDatesForSearch(startConfirmedOn, endConfirmedOn);
        startConfirmedOn = dates[0];
        endConfirmedOn = dates[1];
        long itemReceivedCount = inboundLineRepository.countByWarehouseIdAndConfirmedOnBetweenAndStatusIdAndReferenceField1IsNull(
                warehouseId, startConfirmedOn, endConfirmedOn, 24L);
        log.info("itemReceivedCount : " + itemReceivedCount);
        return itemReceivedCount;
    }

    /**
     * @param warehouseId
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @return
     * @throws Exception
     */
    private long getShippedLineCount(String warehouseId, Date fromDeliveryDate, Date toDeliveryDate) throws Exception {
        /*
         * Shipped Line --------------------- Shipped Line Pass the logged in WH_ID and
         * Current date as DLV_CNF_ON in OUTBOUNDLINE table and fetch Count of
         * OB_LINE_NO values where REF_FIELD_2=Null and DLV_QTY > 0
         */
        log.info("---getShippedLineCount>>>>fromDeliveryDate-----> : " + fromDeliveryDate);
        log.info("---getShippedLineCount>>>>toDeliveryDate-----> : " + toDeliveryDate);
        long shippedLineCount =
                outboundLineRepository.countByWarehouseIdAndDeliveryConfirmedOnBetweenAndStatusIdAndDeletionIndicatorAndReferenceField2IsNullAndDeliveryQtyIsNotNullAndDeliveryQtyGreaterThan(
                        warehouseId, fromDeliveryDate, toDeliveryDate, 59L, 0L, Double.valueOf(0));

        log.info("shippedLineCount : " + shippedLineCount);
        return shippedLineCount;
    }

    /**
     * @param warehouseId
     * @param fromDeliveryDate
     * @param toDeliveryDate
     * @param type
     * @return
     * @throws Exception
     */
    private long getNormalNSpecialCount(String warehouseId, Date fromDeliveryDate, Date toDeliveryDate, String type)
            throws Exception {
        /*
         * Normal ---------------- Pass the logged in WH_ID and Current date as
         * DLV_CNF_ON in OUTBOUNDLINE table and fetch Count of OB_LINE_NO values where
         * REF_FIELD_1=N, REF_FIELD_2=Null and DLV_QTY>0 (Shipped Lines)
         */
        long normalCount =
                outboundLineRepository.countByWarehouseIdAndDeliveryConfirmedOnBetweenAndStatusIdAndDeletionIndicatorAndReferenceField1AndReferenceField2IsNullAndDeliveryQtyIsNotNullAndDeliveryQtyGreaterThan(
                        warehouseId, fromDeliveryDate, toDeliveryDate, 59L, 0L, type, Double.valueOf(0));

        log.info("normalCount : " + normalCount);
        return normalCount;
    }

    /**
     *
     * @return
     */
//	public WorkBookSheetDTO exportXlsxFile() {
//		try {
//			int pageSize = 500;
//			Page<InventoryReport> pageResult = scheduleInventoryReport(0, pageSize, "itemCode");
//			List<InventoryReport> listRecords = new ArrayList<>();
//			listRecords.addAll(pageResult.getContent());
//	
//			for (int pageNo = 1; pageNo <= pageResult.getTotalPages(); pageNo++) {
//				pageResult = scheduleInventoryReport(pageNo, pageSize, "itemCode");
//				listRecords.addAll(pageResult.getContent());
//				log.info("listRecords count: " + listRecords.size());
//			}
//	
//			WorkBookSheetDTO workBookSheetDTO = workBookService.createWorkBookWithSheet("inventory");
//			CellStyle headerStyle = workBookSheetDTO.getStyle();
//			CellStyle cellStyle = workBookService.createLineCellStyle(workBookSheetDTO.getWorkbook());
//			CellStyle decimalFormatCells = workBookService.createLineCellStyle(workBookSheetDTO.getWorkbook());
//
//			listRecords = listRecords.stream()
//					.filter(data-> data != null && 
//							(
//								(data.getInventoryQty() != null && data.getInventoryQty() > 0) || 
//								(data.getAllocatedQty() != null && data.getAllocatedQty() > 0)
//							))
//					.collect(Collectors.toList());
//
//			/*
//			 * private String WAREHOUSEID; // WH_ID private String ITEMCODE; // ITM_CODE
//			 * private String DESCRIPTION; // ITEM_TEXT private String UOM; // INV_UOM
//			 * private String STORAGEBIN; // ST_BIN private String STORAGESECTIONID; //
//			 * ST_SEC_ID private String PACKBARCODES; // PACK_BARCODE private Double
//			 * INVENTORYQTY; // INV_QTY private Long STOCKTYPE; // STCK_TYP_TEXT
//			 */
//			ArrayList<String> headerData = new ArrayList<>();
//			headerData.add("WAREHOUSEID");
//			headerData.add("ITEMCODE");
//			headerData.add("DESCRIPTION");
//			headerData.add("MFR PART NO");
//			headerData.add("UOM");
//			headerData.add("STORAGEBIN");
//			headerData.add("STORAGESECTIONID");
//			headerData.add("PACKBARCODES");
//			headerData.add("INVENTORYQTY");
//			headerData.add("ALLOCATEDQTY");
//			headerData.add("TOTALQTY");
//			headerData.add("STOCKTYPE");
//
//			this.createHeaderRow(workBookSheetDTO.getWorkbook().getSheet("inventory"), headerStyle, headerData);
//
//			int rowIndex = 1;
//			for (InventoryReport data : listRecords) {
//				int cellIndex = 0;
//				Row row = workBookSheetDTO.getWorkbook().getSheet("inventory").createRow(rowIndex++);
//				try {
//					row.createCell(cellIndex).setCellValue(data.getWarehouseId());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getItemCode());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getDescription());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getMfrPartNumber());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getUom());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getStorageBin());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getStorageSectionId());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getPackBarcodes());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getInventoryQty());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getAllocatedQty() != null ? data.getAllocatedQty() : 0);
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getTotalQuantity() != null ? data.getTotalQuantity() : 0);
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//					cellIndex++;
//
//					row.createCell(cellIndex).setCellValue(data.getStockType());
//					row.getCell(cellIndex).setCellStyle(cellStyle);
//				} catch (Exception e){
//					log.info("ERROR : Excel inventory bind row " + rowIndex, e);
//				}
//			}
//
//			OutputStream fout = new FileOutputStream("inventory.xlsx");
//			workBookSheetDTO.getWorkbook().write(fout);
//			return workBookSheetDTO;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * @param os
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream getOutputStreamToByteArray(OutputStream os) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(bos.toByteArray());
        byte[] arr = bos.toByteArray();
        os.write(arr);
        return bos;
    }

    /**
     * @param sheet
     * @param headerStyle
     * @param header
     */
    public void createHeaderRow(Sheet sheet, CellStyle headerStyle, List<String> header) {
        Row headerRows = sheet.createRow(0);
        int k = 0;
        for (String value : header) {
            headerRows.createCell(k);
            headerRows.getCell(k).setCellValue(value.toString());
            headerRows.getCell(k).setCellStyle(headerStyle);
            k++;
        }
    }

    /**
     * @param warehouseId
     * @return
     * @throws Exception
     */
    @Transactional
    public Dashboard getDashboardCount(String warehouseId) throws Exception {
        Dashboard dashboard = new Dashboard();

        /*--------------------------DAY-----------------------------------------------*/
        Dashboard.Day day = dashboard.new Day();
        Dashboard.Day.Receipts dayReceipts = day.new Receipts();

        /*-----------------------Receipts--------------------------------------------*/
        // Awaiting ASN
        long awaitingASNCount = getAwaitingASNCount(warehouseId, DateUtils.dateSubtract(1), DateUtils.dateSubtract(1));
        dayReceipts.setAwaitingASN(awaitingASNCount);

        long containerReceivedCount = getContainerReceivedCount(warehouseId, DateUtils.dateSubtract(1), DateUtils.dateSubtract(1));
        dayReceipts.setContainerReceived(containerReceivedCount);

        long itemReceivedCount = getItemReceivedCount(warehouseId, DateUtils.dateSubtract(1), DateUtils.dateSubtract(1));
        dayReceipts.setItemReceived(itemReceivedCount);

        //Shipping Day
        Dashboard.Day.Shipping dayShipping = day.new Shipping();

        Date fromDate = DateUtils.dateSubtract(1, 14, 0, 0); // From Yesterday 14:00 to Today 15:00
        Date endDate = DateUtils.dateSubtract(0, 13, 59, 59);
        long shippedLineCount = getShippedLineCount(warehouseId, fromDate, endDate);
        dayShipping.setShippedLine(shippedLineCount);

        long normalCount = getNormalNSpecialCount(warehouseId, fromDate, endDate, "N");
        dayShipping.setNormal(normalCount);

        long specialCount = getNormalNSpecialCount(warehouseId, fromDate, endDate, "S");
        dayShipping.setSpecial(specialCount);

        day.setReceipts(dayReceipts);
        day.setShipping(dayShipping);
        dashboard.setDay(day);

        /*--------------------------MONTH--------------------------------------------*/
        Dashboard.Month month = dashboard.new Month();
        Dashboard.Month.Receipts monthReceipts = month.new Receipts();

        /*-----------------------Receipts--------------------------------------------*/
        // Awaiting ASN
        LocalDate today = LocalDate.now();
        today = today.withDayOfMonth(1);
        log.info("First day of current month: " + today.withDayOfMonth(1));
        Date beginningOfMonth = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        awaitingASNCount = getAwaitingASNCount(warehouseId, beginningOfMonth, new Date());
        monthReceipts.setAwaitingASN(awaitingASNCount);

        containerReceivedCount = getContainerReceivedCount(warehouseId, beginningOfMonth, new Date());
        monthReceipts.setContainerReceived(containerReceivedCount);

        itemReceivedCount = getItemReceivedCount(warehouseId, beginningOfMonth, new Date());
        monthReceipts.setItemReceived(itemReceivedCount);

        //Shipping Month
        Dashboard.Month.Shipping monthShipping = month.new Shipping();

        LocalDate currentDay = LocalDate.now();
        LocalDate beginDayOfMonth = today.withDayOfMonth(1);
        fromDate = DateUtils.addTimeToDate(beginDayOfMonth, 14, 0, 0);
        endDate = DateUtils.addTimeToDate(currentDay, 13, 59, 59);

//		shippedLineCount = getShippedLineCount(warehouseId, beginningOfMonth, new Date());
        shippedLineCount = getShippedLineCount(warehouseId, fromDate, endDate);
        monthShipping.setShippedLine(shippedLineCount);

//		normalCount = getNormalNSpecialCount(warehouseId, beginningOfMonth, new Date(), "N");
        normalCount = getNormalNSpecialCount(warehouseId, fromDate, endDate, "N");
        monthShipping.setNormal(normalCount);

//		specialCount = getNormalNSpecialCount(warehouseId, beginningOfMonth, new Date(), "S");
        specialCount = getNormalNSpecialCount(warehouseId, fromDate, endDate, "S");
        monthShipping.setSpecial(specialCount);

        //Bin Status
        Dashboard.BinStatus binStatus = dashboard.new BinStatus();

        long statusCount = storagebinRepository.countByWarehouseIdAndStatusIdAndDeletionIndicator(warehouseId, 0L, 0L);
        binStatus.setStatusEqualToZeroCount(statusCount);

        statusCount = storagebinRepository.countByWarehouseIdAndStatusIdNotAndDeletionIndicator(warehouseId, 0L, 0L);
        binStatus.setStatusNotEqualToZeroCount(statusCount);

        month.setReceipts(monthReceipts);
        month.setShipping(monthShipping);
        dashboard.setMonth(month);
        dashboard.setBinStatus(binStatus);

        return dashboard;
    }

    /**
     * @param fastSlowMovingDashboardRequest
     * @return
     * @throws Exception
     */
    @Transactional
    public List<FastSlowMovingDashboard> getFastSlowMovingDashboard(FastSlowMovingDashboardRequest fastSlowMovingDashboardRequest) throws Exception {

        log.info("Fast slow moving dashboard request {}", fastSlowMovingDashboardRequest);
        if (fastSlowMovingDashboardRequest.getWarehouseId() == null || fastSlowMovingDashboardRequest.getWarehouseId().isEmpty()) {
            throw new BadRequestException("Please provide valid warehouseId");
        }
        if (fastSlowMovingDashboardRequest.getFromDate() == null || fastSlowMovingDashboardRequest.getToDate() == null) {
            throw new BadRequestException("Please provide valid from date and to date");
        }
        return getFastSlowMovingDashboardData(fastSlowMovingDashboardRequest.getWarehouseId(),
                fastSlowMovingDashboardRequest.getFromDate(), fastSlowMovingDashboardRequest.getToDate());
    }

    /**
     *
     * @param warehouseId
     * @param itemCode
     * @param fromCreatedOn
     * @param toCreatedOn
     * @return
     */
//	public List<InventoryStock> getInventoryStockReport(FindImBasicData1 searchImBasicData1) {
//		try {
//
//			if (searchImBasicData1.getFromCreatedOn() != null && searchImBasicData1.getFromCreatedOn() != null) {
//				Date[] dates = DateUtils.addTimeToDatesForSearch(searchImBasicData1.getFromCreatedOn(),
//						searchImBasicData1.getToCreatedOn());
//				searchImBasicData1.setFromCreatedOn(dates[0]);
//				searchImBasicData1.setToCreatedOn(dates[1]);
//			}
//
//			ImBasicData1Specification spec = new ImBasicData1Specification(searchImBasicData1);
//			List<ImBasicData1> resultsImBasicData1 = imbasicdata1Repository.findAll(spec);
//			log.info("resultsImBasicData1 : " + resultsImBasicData1);
//			List<InventoryStock> inventoryStockList = new ArrayList<>();
//
//			// 3. Pass WH_ID in IMBASICDATA1 table and fetch the ITM_CODES
//			Set<String> itemCodeSet = resultsImBasicData1.parallelStream().map(ImBasicData1::getItemCode).collect(Collectors.toSet());
//			log.info("itemCodeSet : " + itemCodeSet);
//
//			itemCodeSet.parallelStream().forEach(i -> {
//				InventoryStock inventoryStock = new InventoryStock();
//				inventoryStock.setWarehouseId(searchImBasicData1.getWarehouseId());
//				inventoryStock.setItemCode(i);
//
//				/*
//				 * Pass ITM_CODE values into INVENTORY_STOCK table and fetch sum of INV_QTY+ ALLOC_QTY where BIN_CL_ID=1 and Sum by ITM_CODE
//				 * This is stock for 20-06-2022 (A)
//				 */
//				Double sumOfInvQty_AllocQty = inventoryStockRepository.findSumOfInventoryQtyAndAllocQty(searchImBasicData1.getItemCode());
//
//				sumOfInvQty_AllocQty = (sumOfInvQty_AllocQty != null) ? sumOfInvQty_AllocQty : 0D;
//				log.info("INVENTORY_STOCK : " + sumOfInvQty_AllocQty);
//
//				/*
//				 * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters
//				 * into PUTAWAYLINE table where status_ID = 20 and IS_DELETED = 0
//				 * Fetch SUM of PA_CNF_QTY and group by ITM_CODE(B)
//				 */
//				Date dateFrom = null;
//				Date dateTo = null;
//				try {
//					dateFrom = DateUtils.convertStringToDateByYYYYMMDD ("2022-06-20");
//					Date[] dates = DateUtils.addTimeToDatesForSearch(dateFrom, searchImBasicData1.getFromCreatedOn());
//					dateFrom = dates[0];
//					dateTo = dates[1];
//				} catch (java.text.ParseException e) {
//					e.printStackTrace();
//				}
//
//				Double sumOfPAConfirmQty = putAwayLineRepository.findSumOfPAConfirmQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfPAConfirmQty = (sumOfPAConfirmQty != null) ? sumOfPAConfirmQty : 0D;
//				log.info("PUTAWAYLINE : " + sumOfPAConfirmQty);
//
//				/*
//				 * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters into PICKUPLINE table
//				 * where status_ID=50 and IS_DELETED=0
//				 * Fetch SUM of PU_QTY and group by ITM_CODE{C}
//				 */
//				Double sumOfPickupLineQty = pickupLineRepository.findSumOfPickupLineQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfPickupLineQty = (sumOfPickupLineQty != null) ? sumOfPickupLineQty : 0D;
//				log.info("PICKUPLINE : " + sumOfPickupLineQty);
//
//				/*
//				 * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters into INVENTORYMOVEMENT table
//				 * where MVT_TYP_ID=4, SUB_MVT_TYP_ID=1 and IS_DELETED=0
//				 * Fetch SUM of MVT_QTY and group by ITM_CODE(D)
//				 */
//				Double sumOfMvtQty = inventoryMovementRepository.findSumOfMvtQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfMvtQty = (sumOfMvtQty != null) ? sumOfMvtQty : 0D;
//				log.info("INVENTORYMOVEMENT : " + sumOfMvtQty);
//
//				// Opening stock - 3 column - E
//				Double openingStock = ((sumOfInvQty_AllocQty + sumOfPAConfirmQty + sumOfPickupLineQty) - sumOfMvtQty);
//				log.info("openingStock : " + openingStock);
//				inventoryStock.setOpeningStock(openingStock);
//
//				try {
//					Date[] dates = DateUtils.addTimeToDatesForSearch(searchImBasicData1.getFromCreatedOn(),
//							searchImBasicData1.getToCreatedOn());
//					dateFrom = dates[0];
//					dateTo = dates[1];
//					log.info("----SecII----> dateFrom & dateTo---> : " + dateFrom + "," + dateTo);
//				} catch (java.text.ParseException e) {
//					e.printStackTrace();
//				}
//
//				// Output Column - 4 - PUTAWAYLINE
//				Double sumOfPAConfirmQty_4 = putAwayLineRepository.findSumOfPAConfirmQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfPAConfirmQty_4 = (sumOfPAConfirmQty_4 != null) ? sumOfPAConfirmQty_4 : 0D;
//				log.info("PUTAWAYLINE_4 : " + sumOfPAConfirmQty_4);
//				inventoryStock.setInboundQty(sumOfPAConfirmQty_4);
//
//				// Output Column - 5 - PICKUPLINE
//				Double sumOfPickupLineQty_5 = pickupLineRepository.findSumOfPickupLineQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfPickupLineQty_5 = (sumOfPickupLineQty_5 != null) ? sumOfPickupLineQty_5 : 0D;
//				log.info("PICKUPLINE_5 : " + sumOfPickupLineQty_5);
//				inventoryStock.setOutboundQty(sumOfPickupLineQty_5);
//
//				// Output Column - 6 -INVENTORYMOVEMENT
//				Double sumOfMvtQty_6 = inventoryMovementRepository.findSumOfMvtQty (searchImBasicData1.getItemCode(), dateFrom, dateTo);
//				sumOfMvtQty_6 = (sumOfMvtQty_6 != null) ? sumOfMvtQty_6 : 0D;
//				log.info("INVENTORYMOVEMENT_6 : " + sumOfMvtQty_6);
//				inventoryStock.setStockAdjustmentQty(sumOfMvtQty_6);
//
//				// Output Column - 7 - (E+F+H) - G
//				Double closingStock = ((openingStock + sumOfPAConfirmQty_4 + sumOfMvtQty_6) - sumOfPickupLineQty_5);
//				log.info("closingStock : " + closingStock);
//				inventoryStock.setClosingStock(closingStock);
//
//				inventoryStockList.add(inventoryStock);
//			});
//			return inventoryStockList;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * @param searchImBasicData1
     * @return
     */
    public Stream<TransactionHistoryReport> getTransactionHistoryReport(FindImBasicData1 searchImBasicData1) {
        try {
            if (searchImBasicData1.getFromCreatedOn() != null && searchImBasicData1.getFromCreatedOn() != null) {
                Date[] dates = DateUtils.addTimeToDatesForSearch(searchImBasicData1.getFromCreatedOn(),
                        searchImBasicData1.getToCreatedOn());
                searchImBasicData1.setFromCreatedOn(dates[0]);
                searchImBasicData1.setToCreatedOn(dates[1]);
            }

            Date openingStockDateFrom = null;
            Date openingStockDateTo = null;
            Date closingStockDateFrom = null;
            Date closingStockDateTo = null;

            String itemCode = "0";
            String manufacturerName = "0";

            try {
                openingStockDateFrom = DateUtils.convertStringToDateByYYYYMMDD("2022-06-20");
                Date[] dates = DateUtils.addTimeToDatesForSearch(openingStockDateFrom, searchImBasicData1.getFromCreatedOn());
                openingStockDateFrom = dates[0];
                openingStockDateTo = DateUtils.dateSubtract(dates[1]);
                log.info("----Opening Stock----> dateFrom & dateTo---> : " + openingStockDateFrom + "," + openingStockDateTo);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            try {
                Date[] dates = DateUtils.addTimeToDatesForSearch(searchImBasicData1.getFromCreatedOn(),
                        searchImBasicData1.getToCreatedOn());
                closingStockDateFrom = dates[0];
                closingStockDateTo = dates[1];
                log.info("----Closing Stock----> dateFrom & dateTo---> : " + closingStockDateFrom + "," + closingStockDateTo);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            if (searchImBasicData1 != null) {
                if (searchImBasicData1.getItemCode() == null || searchImBasicData1.getItemCode().isEmpty()) {
                    itemCode = "0";
                }
                if (searchImBasicData1.getItemCode() != null && !searchImBasicData1.getItemCode().isEmpty() && searchImBasicData1.getItemCode().size() == 1) {
                    itemCode = searchImBasicData1.getItemCode().get(0);
                }
                if (searchImBasicData1.getItemCode() != null && searchImBasicData1.getItemCode().size() > 1) {
                    throw new RuntimeException("Item Code can be null or Single");
                }
            }

            if (searchImBasicData1 != null) {
                if (searchImBasicData1.getManufacturerName() == null) {
                    manufacturerName = "0";
                }
                if (searchImBasicData1.getManufacturerName() != null) {
                    manufacturerName = searchImBasicData1.getManufacturerName();
                }
            }

            /*
             * Pass ITM_CODE values into INVENTORY_STOCK table and fetch sum of INV_QTY+ ALLOC_QTY where BIN_CL_ID=1 and Sum by ITM_CODE
             * This is stock for 20-06-2022 (A)
             */

            /*
             * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters
             * into PUTAWAYLINE table where status_ID = 20 and IS_DELETED = 0
             * Fetch SUM of PA_CNF_QTY and group by ITM_CODE(B)
             */

            /*
             * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters into PICKUPLINE table
             * where status_ID=50 and IS_DELETED=0
             * Fetch SUM of PU_QTY and group by ITM_CODE{C}
             */

            /*
             * Pass ITM_CODE values and From date 20-06-2022 and to date as From date of selection parameters into INVENTORYMOVEMENT table
             * where MVT_TYP_ID=4, SUB_MVT_TYP_ID=1 and IS_DELETED=0
             * Fetch SUM of MVT_QTY and group by ITM_CODE(D)
             */

            // Opening stock - 3 column - E [openingStock = ((sumOfInvQty_AllocQty + sumOfPAConfirmQty + sumOfMvtQty) - sumOfPickupLineQty);]

            //--------------------------------------------------------------closing stock---------------------------------------------------------------------

            // Output Column - 7 - (E+F+H) - G [closingStock = ((openingStock + sumOfPAConfirmQty_4 + sumOfMvtQty_6) - sumOfPickupLineQty_5);]

            transactionHistoryResultRepository.SP_THR(
                    searchImBasicData1.getCompanyCodeId(),
                    searchImBasicData1.getPlantId(),
                    searchImBasicData1.getLanguageId(),
                    searchImBasicData1.getWarehouseId(),
                    itemCode,
                    manufacturerName,
                    openingStockDateFrom,
                    openingStockDateTo,
                    closingStockDateFrom,
                    closingStockDateTo);
            log.info("Report Generated successfully through Stored Procedure");

            TransactionHistoryReportSpecification specification = new TransactionHistoryReportSpecification();
            Stream<TransactionHistoryReport> inventoryStockList = transactionHistoryReportRepository.stream(specification, TransactionHistoryReport.class);
            log.info("TransactionHistoryReport -----> Output");

            return inventoryStockList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param fromCreatedOn
     * @param toCreatedOn
     * @return
     * @throws java.text.ParseException
     */
    @Transactional
    private List<FastSlowMovingDashboard> getFastSlowMovingDashboardData(String warehouseId, Date fromCreatedOn, Date toCreatedOn)
            throws java.text.ParseException {
        List<FastSlowMovingDashboard> itemDataList = new ArrayList<>();

        List<FastSlowMovingDashboard> fastMoving = new ArrayList<>();
        List<FastSlowMovingDashboard> averageMoving = new ArrayList<>();
        List<FastSlowMovingDashboard> slowMoving = new ArrayList<>();
        /*
         * Receipts - Awaiting ASN -------------------------- Pass the logged in WH_ID
         * and current date in CR_CTD_ON field in CONTAINERRECEIPT table and fetch the
         * count of records where REF_DOC_NO is Null
         */
        Date[] dates = DateUtils.addTimeToDatesForSearch(fromCreatedOn, toCreatedOn);
        fromCreatedOn = dates[0];
        toCreatedOn = dates[1];
        List<FastSlowMovingDashboard.FastSlowMovingDashboardImpl> itemData = outboundLineRepository.getFastSlowMovingDashboardData(warehouseId, fromCreatedOn, toCreatedOn);
//		log.info("FastSlowMovingDashboard itemData : " + itemData);
        if (itemData != null && !itemData.isEmpty()) {
            int splitSize = itemData.size() / 3;
            for (FastSlowMovingDashboard.FastSlowMovingDashboardImpl item : itemData) {
                FastSlowMovingDashboard data = new FastSlowMovingDashboard();
                data.setItemCode(item.getItemCode());
                data.setDeliveryQuantity(item.getDeliveryQuantity());
                data.setItemText(item.getItemText());

                if (fastMoving.size() < splitSize) {
                    data.setType("FAST");
                    fastMoving.add(data);
                } else if (fastMoving.size() == splitSize && averageMoving.size() < splitSize) {
                    data.setType("AVERAGE");
                    averageMoving.add(data);
                } else {
                    data.setType("SLOW");
                    slowMoving.add(data);
                }
            }
        }
        itemDataList.addAll(fastMoving.stream().limit(100).collect(Collectors.toList()));
        itemDataList.addAll(averageMoving.stream().limit(100).collect(Collectors.toList()));
        itemDataList.addAll(slowMoving.stream().collect(lastN(100)));
        return itemDataList;
    }

    /**
     * @param <T>
     * @param n
     * @return
     */
    public static <T> Collector<T, ?, List<T>> lastN(int n) {
        return Collector.<T, Deque<T>, List<T>>of(ArrayDeque::new, (acc, t) -> {
            if (acc.size() == n)
                acc.pollFirst();
            acc.add(t);
        }, (acc1, acc2) -> {
            while (acc2.size() < n && !acc1.isEmpty()) {
                acc2.addFirst(acc1.pollLast());
            }
            return acc2;
        }, ArrayList::new);
    }

    //-------------------------------------------------Get all StockMovementReport---------------------------------

    /**
     * getStockMovementReports
     *
     * @return
     */
    public List<StockMovementReport> getStockMovementReports() {
        List<StockMovementReport> stockMovementReportList = stockMovementReportRepository.findAll();
        return stockMovementReportList;
    }

    //-------------------------------------------------Get all StockMovementReport New---------------------------------

    /**
     * @return
     * @throws Exception
     */
    public Stream<StockMovementReport1> findStockMovementReportNew() throws Exception {
        StockMovementReportNewSpecification spec = new StockMovementReportNewSpecification();
        Stream<StockMovementReport1> results = stockMovementReport1Repository.stream(spec, StockMovementReport1.class);
        return results;
    }

}
