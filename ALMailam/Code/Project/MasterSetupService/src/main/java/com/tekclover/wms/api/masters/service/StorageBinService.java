package com.tekclover.wms.api.masters.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.masters.model.impl.ItemListImpl;
import com.tekclover.wms.api.masters.model.impl.StorageBinListImpl;
import com.tekclover.wms.api.masters.model.storagebin.v2.StorageBinV2;
import com.tekclover.wms.api.masters.repository.StorageBinV2Repository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.storagebin.AddStorageBin;
import com.tekclover.wms.api.masters.model.storagebin.SearchStorageBin;
import com.tekclover.wms.api.masters.model.storagebin.StorageBin;
import com.tekclover.wms.api.masters.model.storagebin.StorageBinPutAway;
import com.tekclover.wms.api.masters.model.storagebin.UpdateStorageBin;
import com.tekclover.wms.api.masters.repository.StorageBinRepository;
import com.tekclover.wms.api.masters.repository.specification.StorageBinSpecification;
import com.tekclover.wms.api.masters.util.CommonUtils;
import com.tekclover.wms.api.masters.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageBinService {

    @Autowired
    private StorageBinRepository storagebinRepository;

    @Autowired
    private StorageBinV2Repository storageBinV2Repository;

    /**
     * getStorageBins
     *
     * @return
     */
    public List<StorageBin> getStorageBins() {
        List<StorageBin> storagebinList = storagebinRepository.findAll();
//		log.info("storagebinList : " + storagebinList);
        storagebinList = storagebinList.stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return storagebinList;
    }

    /**
     * getStorageBin
     *
     * @param storageBin
     * @return
     */
    public StorageBin getStorageBin(String storageBin, String companyCodeId, String plantId, String warehouseId, String languageId) {
        Optional<StorageBin> storagebin = storagebinRepository.findByStorageBinAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(
                storageBin,
                companyCodeId,
                plantId,
                warehouseId,
                languageId,
                0L);
        if (storagebin.isEmpty()) {
            throw new BadRequestException("The Given Values:" +
                    "storageBin" + storageBin +
                    "companyCodeId" + companyCodeId +
                    "plantId" + plantId +
                    "warehouseId" + warehouseId + "doesn't exist:");
        }
        return storagebin.get();
    }

    //V2
    public StorageBinV2 getStorageBinV2(String storageBin, String companyCodeId, String plantId, String warehouseId, String languageId) {
        Optional<StorageBinV2> storagebin = storageBinV2Repository.findByStorageBinAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(
                storageBin,
                companyCodeId,
                plantId,
                warehouseId,
                languageId,
                0L);
        if (storagebin.isEmpty()) {
            throw new BadRequestException("The Given Values: " +
                    "storageBin" + storageBin +
                    "companyCodeId " + companyCodeId +
                    "plantId " + plantId +
                    "warehouseId " + warehouseId + " doesn't exist:");
        }
        return storagebin.get();
    }

    /**
     *
     * @param warehouseId
     * @param binClassId
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @return
     */
    public StorageBinV2 getStorageBinByBinClassId(String warehouseId, Long binClassId, String companyCodeId, String plantId,  String languageId) {
        StorageBinV2 storagebin = storageBinV2Repository.getStorageBinByBinClassId(
                                                            binClassId,
                                                            companyCodeId,
                                                            plantId,
                                                            warehouseId,
                                                            languageId);
        if (storagebin == null) {
            throw new BadRequestException("The Given Values: " +
                    "binClassId" + binClassId +
                    "companyCodeId " + companyCodeId +
                    "plantId " + plantId +
                    "warehouseId " + warehouseId + " doesn't exist:");
        }
        return storagebin;
    }

    /**
     *
     * @param warehouseId
     * @param binClassId
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @return
     */
    public StorageBinV2 getStorageBinByBinClassIdV2(String warehouseId, Long binClassId, String companyCodeId, String plantId,  String languageId) {
        Optional<StorageBinV2> storagebin = storageBinV2Repository.findTopByBinClassIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(
                                                            binClassId,
                                                            companyCodeId,
                                                            plantId,
                                                            warehouseId,
                                                            languageId,0L);
        if (storagebin.isEmpty()) {
            throw new BadRequestException("The Given Values: " +
                    "binClassId" + binClassId +
                    "companyCodeId " + companyCodeId +
                    "plantId " + plantId +
                    "warehouseId " + warehouseId + " doesn't exist:");
        }
        return storagebin.get();
    }

    /**
     * @param storageBinPutAway
     * @return
     */
    public List<StorageBin> getStorageBin(StorageBinPutAway storageBinPutAway) {
        List<StorageBin> storagebinList =
                storagebinRepository.findByWarehouseIdAndStorageBinInAndStorageSectionIdInAndPutawayBlockAndPickingBlockAndDeletionIndicatorOrderByStorageBinDesc(
                        storageBinPutAway.getWarehouseId(),
                        storageBinPutAway.getStorageBin(),
                        storageBinPutAway.getStorageSectionIds(),
                        0,
                        0,
                        0L);
        if (!storagebinList.isEmpty()) {
            return storagebinList;
        }
        return null;
    }

    /**
     * @param storageBinPutAway
     * @return
     */
    public List<StorageBinV2> getStorageBinV2(StorageBinPutAway storageBinPutAway) {
        List<StorageBinV2> storagebinList =
                storageBinV2Repository.findByWarehouseIdAndStorageBinInAndBinClassIdAndPutawayBlockAndPickingBlockAndDeletionIndicatorOrderByStorageBinDesc(
                        storageBinPutAway.getWarehouseId(),
                        storageBinPutAway.getStorageBin(),
                        storageBinPutAway.getBinClassId(),
                        0,
                        0,
                        0L);
        if (!storagebinList.isEmpty()) {
            return storagebinList;
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param statusId
     * @return
     */
    public List<StorageBin> getStorageBinByStatus(String warehouseId, Long statusId) {
        List<StorageBin> storagebin = storagebinRepository.findByWarehouseIdAndStatusIdAndDeletionIndicator(warehouseId, statusId, 0L);
        if (storagebin != null) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + warehouseId + ", statusId: " + statusId + " doesn't exist.");
        }
    }

    //V2
    public List<StorageBinV2> getStorageBinByStatusV2(String warehouseId, Long statusId) {
        List<StorageBinV2> storagebin = storageBinV2Repository.findByWarehouseIdAndStatusIdAndDeletionIndicator(warehouseId, statusId, 0L);
        if (storagebin != null) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + warehouseId + ", statusId: " + statusId + " doesn't exist.");
        }
    }

    /**
     * @param warehouseId
     * @param statusId
     * @return
     */
    public List<StorageBin> getStorageBinByStatusNotEqual(String warehouseId, Long statusId) {
        List<StorageBin> storagebin =
                storagebinRepository.findByWarehouseIdAndStatusIdNotAndDeletionIndicator(warehouseId,
                        statusId, 0L);
        if (storagebin != null) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBinByStatusNotEqual : " + warehouseId + ", statusId: " + statusId + " doesn't exist.");
        }
    }

    /**
     * @param likeSearchByStorageBinNDesc
     * @return
     */
    public List<StorageBinListImpl> findStorageBinLikeSearch(String likeSearchByStorageBinNDesc) {
        if (likeSearchByStorageBinNDesc != null && !likeSearchByStorageBinNDesc.trim().isEmpty()) {
            List<StorageBinListImpl> data = storagebinRepository.getStorageBinListBySearch(likeSearchByStorageBinNDesc.trim(),
                    likeSearchByStorageBinNDesc.trim());
            return data;
        } else {
            throw new BadRequestException("Search string must not be empty");
        }
    }

    /**
     * getStorageBin
     *
     * @param warehouseId
     * @param binClassId
     * @return
     */
    public StorageBin getStorageBin(String warehouseId, Long binClassId) {
        StorageBin storagebin = storagebinRepository.findByWarehouseIdAndBinClassIdAndDeletionIndicator(
                warehouseId, binClassId, 0L);
        if (storagebin != null) {
            return storagebin;
        } else {
            throw new BadRequestException("The given values : "
                    + " warehouseId:" + warehouseId
                    + ", binClassId:" + binClassId
                    + " doesn't exist.");
        }
    }

    /**
     * @param warehouseId
     * @param storageBin
     * @return
     */
    public StorageBin getStorageBin(String warehouseId, String storageBin) {
        StorageBin storagebin = storagebinRepository.findByWarehouseIdAndStorageBinAndDeletionIndicator(warehouseId, storageBin, 0L);
        log.info("Storage bin==========>: " + storagebin);
        if (storagebin != null && storagebin.getDeletionIndicator() != null && storagebin.getDeletionIndicator() == 0) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + storageBin + " doesn't exist.");
        }
    }

    /**
     * @param warehouseId
     * @param storageBin
     * @return
     */
    public StorageBinV2 getStorageBinV2(String warehouseId, String storageBin) {
        StorageBinV2 storagebin = storageBinV2Repository.findByWarehouseIdAndStorageBinAndDeletionIndicator(warehouseId, storageBin, 0L);
        log.info("Storage bin==========>: " + storagebin);
        if (storagebin != null && storagebin.getDeletionIndicator() != null && storagebin.getDeletionIndicator() == 0) {
            return storagebin;
        } else {
            throw new BadRequestException("The given StorageBin ID : " + storageBin + " doesn't exist.");
        }
    }

    /**
     * @param stSectionIds
     * @return
     */
    public List<StorageBin> getStorageBin(String warehouseId, List<String> stSectionIds) {
        List<StorageBin> storagebin = storagebinRepository.findByWarehouseIdAndStorageSectionIdIn(warehouseId, stSectionIds);
        if (storagebin != null) {
            return storagebin;
        } else {
            throw new BadRequestException("The given values : "
                    + " stSectionIds:" + stSectionIds
                    + " doesn't exist.");
        }
    }

    /**
     * @param searchStorageBin
     * @return
     * @throws Exception
     */
    public List<StorageBin> findStorageBin(SearchStorageBin searchStorageBin) throws Exception {
        if (searchStorageBin.getStartCreatedOn() != null && searchStorageBin.getEndCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStorageBin.getStartCreatedOn(), searchStorageBin.getEndCreatedOn());
            searchStorageBin.setStartCreatedOn(dates[0]);
            searchStorageBin.setEndCreatedOn(dates[1]);
        }

        if (searchStorageBin.getStartUpdatedOn() != null && searchStorageBin.getEndUpdatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStorageBin.getStartUpdatedOn(), searchStorageBin.getEndUpdatedOn());
            searchStorageBin.setStartUpdatedOn(dates[0]);
            searchStorageBin.setEndUpdatedOn(dates[1]);
        }

        StorageBinSpecification spec = new StorageBinSpecification(searchStorageBin);
        List<StorageBin> results = storagebinRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    //Streaming
    public Stream<StorageBin> findStorageBinStream(SearchStorageBin searchStorageBin) throws Exception {
        if (searchStorageBin.getStartCreatedOn() != null && searchStorageBin.getEndCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStorageBin.getStartCreatedOn(), searchStorageBin.getEndCreatedOn());
            searchStorageBin.setStartCreatedOn(dates[0]);
            searchStorageBin.setEndCreatedOn(dates[1]);
        }

        if (searchStorageBin.getStartUpdatedOn() != null && searchStorageBin.getEndUpdatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStorageBin.getStartUpdatedOn(), searchStorageBin.getEndUpdatedOn());
            searchStorageBin.setStartUpdatedOn(dates[0]);
            searchStorageBin.setEndUpdatedOn(dates[1]);
        }

        StorageBinSpecification spec = new StorageBinSpecification(searchStorageBin);
        Stream<StorageBin> results = storagebinRepository.stream(spec, StorageBin.class);
        return results;
    }

    /**
     * createStorageBin
     *
     * @param newStorageBin
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorageBin createStorageBin(AddStorageBin newStorageBin, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StorageBin dbStorageBin = new StorageBin();
        Optional<StorageBin> duplicateStorageBin = storagebinRepository.findByStorageBinAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(newStorageBin.getStorageBin(), newStorageBin.getCompanyCodeId(), newStorageBin.getPlantId(), newStorageBin.getWarehouseId(), newStorageBin.getLanguageId(), 0L);
        if (!duplicateStorageBin.isEmpty()) {
            throw new BadRequestException("Record is Getting Duplicate");
        } else {
            BeanUtils.copyProperties(newStorageBin, dbStorageBin, CommonUtils.getNullPropertyNames(newStorageBin));
            dbStorageBin.setDeletionIndicator(0L);
            dbStorageBin.setCreatedBy(loginUserID);
            dbStorageBin.setUpdatedBy(loginUserID);
            dbStorageBin.setCreatedOn(new Date());
            dbStorageBin.setUpdatedOn(new Date());
            return storagebinRepository.save(dbStorageBin);
        }
    }

    /**
     * updateStorageBin
     *
     * @param storageBin
     * @param updateStorageBin
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorageBin updateStorageBin(String storageBin, String companyCodeId, String plantId, String warehouseId, String languageId, UpdateStorageBin updateStorageBin, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StorageBin dbStorageBin = getStorageBin(storageBin, companyCodeId, plantId, warehouseId, languageId);
        BeanUtils.copyProperties(updateStorageBin, dbStorageBin, CommonUtils.getNullPropertyNames(updateStorageBin));
        dbStorageBin.setUpdatedBy(loginUserID);
        dbStorageBin.setUpdatedOn(new Date());
        return storagebinRepository.save(dbStorageBin);
    }

    /**
     * createStorageBin
     *
     * @param newStorageBin
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorageBinV2 createStorageBinV2(StorageBinV2 newStorageBin, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StorageBinV2 dbStorageBin = new StorageBinV2();
        Optional<StorageBinV2> duplicateStorageBin = storageBinV2Repository.
                findByStorageBinAndCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeletionIndicator(
                        newStorageBin.getStorageBin(), newStorageBin.getCompanyCodeId(),
                        newStorageBin.getPlantId(), newStorageBin.getWarehouseId(),
                        newStorageBin.getLanguageId(), 0L);
        if (!duplicateStorageBin.isEmpty()) {
            throw new BadRequestException("Record is Getting Duplicate");
        } else {
            BeanUtils.copyProperties(newStorageBin, dbStorageBin, CommonUtils.getNullPropertyNames(newStorageBin));
            dbStorageBin.setDeletionIndicator(0L);
            dbStorageBin.setCreatedBy(loginUserID);
            dbStorageBin.setUpdatedBy(loginUserID);
            dbStorageBin.setCreatedOn(new Date());
            dbStorageBin.setUpdatedOn(new Date());
            return storageBinV2Repository.save(dbStorageBin);
        }
    }

    /**
     * updateStorageBin
     *
     * @param storageBin
     * @param updateStorageBin
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorageBinV2 updateStorageBinV2(String storageBin, String companyCodeId,
										   String plantId, String warehouseId,
										   String languageId, StorageBinV2 updateStorageBin, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
//        StorageBinV2 dbStorageBin = storageBinV2Repository.getStorageBin(storageBin, companyCodeId, plantId, warehouseId, languageId);
        StorageBinV2 dbStorageBin = getStorageBinV2(storageBin, companyCodeId, plantId, warehouseId, languageId);
        log.info("dbstorageBin: " + dbStorageBin);
        BeanUtils.copyProperties(updateStorageBin, dbStorageBin, CommonUtils.getNullPropertyNames(updateStorageBin));
        dbStorageBin.setUpdatedBy(loginUserID);
        dbStorageBin.setUpdatedOn(new Date());
        return storageBinV2Repository.save(dbStorageBin);
    }

    /**
     * deleteStorageBin
     *
     * @param storageBin
     */
    public void deleteStorageBin(String storageBin, String companyCodeId, String plantId, String warehouseId, String languageId, String loginUserID) {
        StorageBin storagebin = getStorageBin(storageBin, companyCodeId, plantId, warehouseId, languageId);
        if (storagebin != null) {
            storagebin.setDeletionIndicator(1L);
            storagebin.setUpdatedBy(loginUserID);
            storagebin.setUpdatedOn(new Date());
            storagebinRepository.save(storagebin);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + storageBin);
        }
    }
}
