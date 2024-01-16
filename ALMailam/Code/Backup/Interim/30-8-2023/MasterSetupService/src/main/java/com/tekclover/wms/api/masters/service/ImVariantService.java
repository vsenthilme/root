package com.tekclover.wms.api.masters.service;


import com.tekclover.wms.api.masters.exception.BadRequestException;
import com.tekclover.wms.api.masters.model.imcapacity.AddImCapacity;
import com.tekclover.wms.api.masters.model.imcapacity.ImCapacity;
import com.tekclover.wms.api.masters.model.imcapacity.SearchImCapacity;
import com.tekclover.wms.api.masters.model.imcapacity.UpdateImCapacity;
import com.tekclover.wms.api.masters.model.imvariant.AddImVariant;
import com.tekclover.wms.api.masters.model.imvariant.ImVariant;
import com.tekclover.wms.api.masters.model.imvariant.SearchImVariant;
import com.tekclover.wms.api.masters.model.imvariant.UpdateImVariant;
import com.tekclover.wms.api.masters.repository.ImCapacityRepository;
import com.tekclover.wms.api.masters.repository.ImVariantRepository;
import com.tekclover.wms.api.masters.repository.specification.ImCapacitySpecification;
import com.tekclover.wms.api.masters.repository.specification.ImVariantSpecification;
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

@Service
@Slf4j
public class ImVariantService {

    @Autowired
    private ImVariantRepository imVariantRepository;

    /**
     * ImVariant
     * @return
     */
    public List<ImVariant> getAllImVariant () {
        List<ImVariant> imVariantList = imVariantRepository.findAll();
        log.info("ImVariant : " + imVariantList);
        imVariantList = imVariantList.stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return imVariantList;
    }

    /**
     * getImVariant
     * @param variantCode
     * @param variantType
     * @param itemCode
     * @return
     */
    public ImVariant getImVariant (String warehouseId, String companyCodeId, String languageId, String plantId,String itemCode,Long variantCode,String variantType) {
        Optional<ImVariant> dbImVariant =
                imVariantRepository.findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndVariantCodeAndVariantTypeAndDeletionIndicator(
                        companyCodeId,
                        languageId,
                        plantId,
                        warehouseId,
                        itemCode,
                        variantCode,
                        variantType,
                        0L
                );
        if (dbImVariant.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "warehouseId - " + warehouseId +
                    "companyCodeId - "+companyCodeId+
                    "plantId - " +plantId+
                    "itemCode - " +itemCode+
                    "variantCode - " +variantCode+
                    "variantType - " +variantType +
                    "doesn't exist.");

        }
        return dbImVariant.get();
    }



    /**
     *
     * @param searchImVariant
     * @return
     * @throws Exception
     */
    public List<ImVariant> findImVariant(SearchImVariant searchImVariant)
            throws Exception {
        ImVariantSpecification spec = new ImVariantSpecification(searchImVariant);
        List<ImVariant> results = imVariantRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    /**
     * createImCapacity
     * @param newImVariant
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ImVariant createImvariant (AddImVariant newImVariant, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImVariant dbImVariant = new ImVariant();
        Optional<ImVariant> duplicateImVariant = imVariantRepository.findByCompanyCodeIdAndLanguageIdAndPlantIdAndWarehouseIdAndItemCodeAndVariantCodeAndVariantTypeAndDeletionIndicator(
                newImVariant.getCompanyCodeId(),
                newImVariant.getLanguageId(),
                newImVariant.getPlantId(),
                newImVariant.getWarehouseId(),
                newImVariant.getItemCode(),
                newImVariant.getVariantCode(),
                newImVariant.getVariantType(),
                0L);
        if (!duplicateImVariant.isEmpty()) {
            throw new BadRequestException("Record is Getting Duplicated");
        } else {
            BeanUtils.copyProperties(newImVariant, dbImVariant, CommonUtils.getNullPropertyNames(newImVariant));
            dbImVariant.setDeletionIndicator(0L);
            dbImVariant.setCreatedBy(loginUserID);
            dbImVariant.setUpdatedBy(loginUserID);
            dbImVariant.setCreatedOn(new Date());
            dbImVariant.setUpdatedOn(new Date());
            return imVariantRepository.save(dbImVariant);
        }
    }

    /**
     * updateImVariant
     * @param itemCode
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ImVariant updateImVariant (String companyCodeId, String plantId, String warehouseId, String languageId, String itemCode,Long variantCode,String variantType, UpdateImVariant updateImVariant, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ImVariant dbImVariant = getImVariant(warehouseId,companyCodeId,languageId,plantId,itemCode,variantCode,variantType);
        BeanUtils.copyProperties(updateImVariant, dbImVariant, CommonUtils.getNullPropertyNames(updateImVariant));
        dbImVariant.setUpdatedBy(loginUserID);
        dbImVariant.setUpdatedOn(new Date());
        return imVariantRepository.save(dbImVariant);
    }

    /**
     * deleteImvariant
     * @param itemCode
     */
    public void deleteImVariant (String companyCodeId,String languageId,String plantId,String warehouseId,String itemCode,Long variantCode,String variantType,String loginUserID) {
        ImVariant imVariant = getImVariant(warehouseId,companyCodeId,languageId,plantId,itemCode,variantCode,variantType);
        if ( imVariant != null) {
            imVariant.setDeletionIndicator (1L);
            imVariant.setUpdatedBy(loginUserID);
            imVariant.setUpdatedOn(new Date());
            imVariantRepository.save(imVariant);
        } else {
            throw new EntityNotFoundException("Error in deleting Id:" + imVariant);
        }
    }
}
