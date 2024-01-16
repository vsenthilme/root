package com.tekclover.wms.api.masters.service;

import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.impalletization.AddImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.ImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.SearchImPalletization;
import com.tekclover.wms.api.masters.model.impalletization.UpdateImPalletization;
import com.tekclover.wms.api.masters.repository.ImPalletizationRepository;
import com.tekclover.wms.api.masters.repository.specification.ImPalletizationSpecification;
import com.tekclover.wms.api.masters.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImPalletizationService {

    @Autowired
    private ImPalletizationRepository imPalletizationRepository;

    /**
     * ImBatchSerial
     * @return
     */
    public List<ImPalletization> getAllImPalletization () {
        List<ImPalletization> imPalletizationList = imPalletizationRepository.findAll();
        log.info("imPalletization : " + imPalletizationList);
        imPalletizationList = imPalletizationList.stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return imPalletizationList;
    }

    /**
     * getImPalletization
     * @param companyCodeId
     * @param itemCode
     * @return
     */
    public ImPalletization getImPalletization (String warehouseId, String companyCodeId, String languageId, String plantId,String itemCode,String palletizationLevel) {
        Optional<ImPalletization> dbImPalletization =
                imPalletizationRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndPalletizationLevelAndDeletionIndicator(
                        companyCodeId,
                        plantId,
                        languageId,
                        warehouseId,
                        itemCode,
                        palletizationLevel,
                        0L
                );
        if (dbImPalletization.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "warehouseId - " + warehouseId +
                    "companyCodeId - "+companyCodeId+
                    "plantId - " +plantId+
                    "itemCode - " +itemCode+
                    "palletizationLevel - "+palletizationLevel+
                    "doesn't exist.");

        }
        return dbImPalletization.get();
    }



    /**
     *
     * @param searchImPalletization
     * @return
     * @throws Exception
     */
    public List<ImPalletization> findImPalletization(SearchImPalletization searchImPalletization)
            throws Exception {
        ImPalletizationSpecification spec = new ImPalletizationSpecification(searchImPalletization);
        List<ImPalletization> results = imPalletizationRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    /**
     * createImPalletization
     * @param newImPalletization
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ImPalletization createImPalletization (AddImPalletization newImPalletization, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImPalletization dbImPalletization = new ImPalletization();
        Optional<ImPalletization> duplicateImPalletization = imPalletizationRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndPalletizationLevelAndDeletionIndicator(
                newImPalletization.getCompanyCodeId(),
                newImPalletization.getPlantId(),
                newImPalletization.getLanguageId(),
                newImPalletization.getWarehouseId(),
                newImPalletization.getItemCode(),
                newImPalletization.getPalletizationLevel(),
                0L);
        if (!duplicateImPalletization.isEmpty()) {
            throw new BadRequestException("Record is Getting Duplicated");
        } else {
            BeanUtils.copyProperties(newImPalletization, dbImPalletization, CommonUtils.getNullPropertyNames(newImPalletization));
            dbImPalletization.setDeletionIndicator(0L);
            dbImPalletization.setCreatedBy(loginUserID);
            dbImPalletization.setUpdatedBy(loginUserID);
            dbImPalletization.setCreatedOn(new Date());
            dbImPalletization.setUpdatedOn(new Date());
            return imPalletizationRepository.save(dbImPalletization);
        }
    }

    /**
     * updateImPalletization
     * @param itemCode
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ImPalletization updateImPalletization (String companyCodeId, String plantId, String warehouseId, String languageId, String itemCode, String palletizationLevel, UpdateImPalletization updateImPalletization, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImPalletization dbImPalletization = getImPalletization(warehouseId,companyCodeId,languageId,plantId,itemCode,palletizationLevel);
        BeanUtils.copyProperties(updateImPalletization, dbImPalletization, CommonUtils.getNullPropertyNames(updateImPalletization));
        dbImPalletization.setUpdatedBy(loginUserID);
        dbImPalletization.setUpdatedOn(new Date());
        return imPalletizationRepository.save(dbImPalletization);
    }

    /**
     * deleteImPalletization
     * @param itemCode
     */
    public void deleteImPalletization (String companyCodeId,String languageId,String plantId,String warehouseId,String itemCode,String palletizationLevel,String loginUserID) {
        ImPalletization imPalletization = getImPalletization(warehouseId,companyCodeId,languageId,plantId,itemCode,palletizationLevel);
        if ( imPalletization != null) {
            imPalletization.setDeletionIndicator (1L);
            imPalletization.setUpdatedBy(loginUserID);
            imPalletization.setUpdatedOn(new Date());
            imPalletizationRepository.save(imPalletization);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + imPalletization);
        }
    }
}
