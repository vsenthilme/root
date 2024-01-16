package com.tekclover.wms.api.transaction.service;


import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.deliveryheader.AddDeliveryHeader;
import com.tekclover.wms.api.transaction.model.deliveryheader.DeliveryHeader;
import com.tekclover.wms.api.transaction.model.deliveryheader.SearchDeliveryHeader;
import com.tekclover.wms.api.transaction.model.deliveryheader.UpdateDeliveryHeader;
import com.tekclover.wms.api.transaction.repository.DeliveryHeaderRepository;
import com.tekclover.wms.api.transaction.repository.specification.DeliveryHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeliveryHeaderService {

    @Autowired
    private DeliveryHeaderRepository deliveryHeaderRepository;


    /**
     * getAllDeliveryLine
     *
     * @return
     */
    public List<DeliveryHeader> getAllDeliveryHeader() {
        List<DeliveryHeader> deliveryHeaderList = deliveryHeaderRepository.findAll();
        deliveryHeaderList = deliveryHeaderList
                .stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return deliveryHeaderList;
    }


    /**
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param languageId
     * @param deliveryNo
     * @return
     */
    public DeliveryHeader getDeliveryHeader(String companyCodeId, String plantId,
                                            String warehouseId, String languageId, String deliveryNo) {
        Optional<DeliveryHeader> dbDeliveryHeader =
                deliveryHeaderRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndDeliveryNoAndLanguageIdAndDeletionIndicator(
                        companyCodeId,
                        plantId,
                        warehouseId,
                        deliveryNo,
                        languageId,
                        0L
                );
        if (dbDeliveryHeader.isEmpty()) {
            throw new BadRequestException("The given values CompanyCodeId -"
                    + companyCodeId + " PlantId "
                    + plantId + " WarehouseId "
                    + warehouseId + " languageId "
                    + languageId + " DeliveryNo "
                    + deliveryNo + " doesn't exist.");

        }
        return dbDeliveryHeader.get();
    }


    /**
     *
     * @param newDeliveryHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DeliveryHeader createDeliveryHeader (AddDeliveryHeader newDeliveryHeader, String loginUserID)
                throws IllegalAccessException, InvocationTargetException {
            Optional<DeliveryHeader> deliveryHeader =
                    deliveryHeaderRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndDeliveryNoAndLanguageIdAndDeletionIndicator(
                            newDeliveryHeader.getCompanyCodeId(),
                            newDeliveryHeader.getPlantId(),
                            newDeliveryHeader.getWarehouseId(),
                            newDeliveryHeader.getDeliveryNo(),
                            newDeliveryHeader.getLanguageId(),
                            0L);
            if (!deliveryHeader.isEmpty()) {
                throw new BadRequestException("Record is getting duplicated with the given values");
            }
            DeliveryHeader dbDeliveryHeader = new DeliveryHeader();
            BeanUtils.copyProperties(newDeliveryHeader, dbDeliveryHeader, CommonUtils.getNullPropertyNames(newDeliveryHeader));
            dbDeliveryHeader.setDeletionIndicator(0L);
            dbDeliveryHeader.setCreatedBy(loginUserID);
            dbDeliveryHeader.setUpdatedBy(loginUserID);
            dbDeliveryHeader.setCreatedOn(new Date());
            dbDeliveryHeader.setUpdatedOn(new Date());
            return deliveryHeaderRepository.save(dbDeliveryHeader);
        }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param deliveryNo
     * @param languageId
     * @param updateDeliveryHeader
     * @param loginUserId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DeliveryHeader UpdateDeliveryHeader(String companyCodeId, String plantId, String warehouseId, String deliveryNo,
                                               String languageId, UpdateDeliveryHeader updateDeliveryHeader, String loginUserId)
        throws IllegalAccessException, InvocationTargetException {

            DeliveryHeader dbDeliveryHeader =
                    getDeliveryHeader(companyCodeId,plantId,warehouseId,languageId,deliveryNo);
            BeanUtils.copyProperties(updateDeliveryHeader,dbDeliveryHeader,CommonUtils.getNullPropertyNames(updateDeliveryHeader));
            dbDeliveryHeader.setUpdatedBy(loginUserId);
            dbDeliveryHeader.setUpdatedOn(new Date());
            return deliveryHeaderRepository.save(dbDeliveryHeader);
        }


    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param deliveryNo
     * @param languageId
     * @param loginUserID
     */
    public void deleteDeliveryHeader(String companyCodeId,String plantId,String warehouseId,
                                     String deliveryNo,String languageId,String loginUserID){

        DeliveryHeader deliveryHeader = getDeliveryHeader(companyCodeId,plantId,warehouseId,languageId,deliveryNo);
        if(deliveryHeader != null){
            deliveryHeader.setDeletionIndicator(1L);
            deliveryHeader.setUpdatedBy(loginUserID);
            deliveryHeaderRepository.save(deliveryHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + deliveryNo);
        }
        }

    /**
     *
     * @param searchDeliveryHeader
     * @return
     * @throws ParseException
     */
    public List<DeliveryHeader> findDeliveryHeader(SearchDeliveryHeader searchDeliveryHeader) throws ParseException {

       DeliveryHeaderSpecification spec = new DeliveryHeaderSpecification(searchDeliveryHeader);
       List<DeliveryHeader> results = deliveryHeaderRepository.findAll(spec);
        results = results.stream().filter(n-> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        return results;
    }
}
