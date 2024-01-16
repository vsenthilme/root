package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.deliveryline.AddDeliveryLine;
import com.tekclover.wms.api.transaction.model.deliveryline.DeliveryLine;
import com.tekclover.wms.api.transaction.model.deliveryline.SearchDeliveryLine;
import com.tekclover.wms.api.transaction.model.deliveryline.UpdateDeliveryLine;
import com.tekclover.wms.api.transaction.repository.DeliveryLineRepository;
import com.tekclover.wms.api.transaction.repository.specification.DeliveryLineSpecification;
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
public class DeliveryLineService {

    @Autowired
    private DeliveryLineRepository deliveryLineRepository;

    /**
     * getAllDeliveryLine
     *
     * @return
     */
    public List<DeliveryLine> getAllDeliveryLine() {
        List<DeliveryLine> deliveryLineList = deliveryLineRepository.findAll();
        deliveryLineList = deliveryLineList
                .stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return deliveryLineList;
    }


    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param invoiceNumber
     * @param refDocNumber
     * @param languageId
     * @param deliveryNo
     * @param itemCode
     * @param lineNo
     * @return
     */
    public DeliveryLine getDeliveryLine(String companyCodeId, String plantId, String warehouseId,
                                        String invoiceNumber,String refDocNumber,String languageId,
                                        String deliveryNo,String itemCode,Long lineNo) {
        Optional<DeliveryLine> dbDeliveryLine =
                deliveryLineRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeliveryNoAndItemCodeAndLineNoAndInvoiceNumberAndRefDocNumberAndDeletionIndicator(
                        companyCodeId,
                        plantId,
                        warehouseId,
                        languageId,
                        deliveryNo,
                        itemCode,
                        lineNo,
                        invoiceNumber,
                        refDocNumber,
                        0L
                );
        if (dbDeliveryLine.isEmpty()) {
            throw new BadRequestException("The given values CompanyCodeId -"
                    + companyCodeId + " PlantId "
                    + plantId + " WarehouseId "
                    + warehouseId + " languageId "
                    + languageId + " DeliveryNo "
                    + deliveryNo + " Item Code "
                    + itemCode + " Line No "
                    + lineNo +  " Invoice Number "
                    + invoiceNumber + " Ref Doc Number "
                    + refDocNumber + " doesn't exist.");

        }
        return dbDeliveryLine.get();
    }


    /**
     *
     * @param newDeliveryLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DeliveryLine createDeliveryLine (AddDeliveryLine newDeliveryLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<DeliveryLine> deliveryLine =
                deliveryLineRepository.findByCompanyCodeIdAndPlantIdAndWarehouseIdAndLanguageIdAndDeliveryNoAndItemCodeAndLineNoAndInvoiceNumberAndRefDocNumberAndDeletionIndicator(
                        newDeliveryLine.getCompanyCodeId(),
                        newDeliveryLine.getPlantId(),
                        newDeliveryLine.getWarehouseId(),
                        newDeliveryLine.getLanguageId(),
                        newDeliveryLine.getDeliveryNo(),
                        newDeliveryLine.getItemCode(),
                        newDeliveryLine.getLineNo(),
                        newDeliveryLine.getInvoiceNumber(),
                        newDeliveryLine.getRefDocNumber(),
                        0L);
        if (!deliveryLine.isEmpty()) {
            throw new BadRequestException("Record is getting duplicated with the given values");
        }
        DeliveryLine dbDeliveryLine = new DeliveryLine();
        BeanUtils.copyProperties(newDeliveryLine, dbDeliveryLine, CommonUtils.getNullPropertyNames(newDeliveryLine));
        dbDeliveryLine.setDeletionIndicator(0L);
        dbDeliveryLine.setCreatedBy(loginUserID);
        dbDeliveryLine.setUpdatedBy(loginUserID);
        dbDeliveryLine.setCreatedOn(new Date());
        dbDeliveryLine.setUpdatedOn(new Date());
        return deliveryLineRepository.save(dbDeliveryLine);
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param deliveryNo
     * @param languageId
     * @param itemCode
     * @param lineNo
     * @param invoiceNumber
     * @param refDocNumber
     * @param updateDeliveryLine
     * @param loginUserId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public DeliveryLine UpdateDeliveryLine(String companyCodeId, String plantId, String warehouseId, String deliveryNo,
                                           String languageId, String itemCode, Long lineNo, String invoiceNumber,
                                           String refDocNumber, UpdateDeliveryLine updateDeliveryLine, String loginUserId)
            throws IllegalAccessException, InvocationTargetException {

        DeliveryLine dbDeliveryLine = getDeliveryLine(companyCodeId,plantId,warehouseId,invoiceNumber,
                refDocNumber,languageId,deliveryNo,itemCode,lineNo);
        BeanUtils.copyProperties(updateDeliveryLine,dbDeliveryLine,CommonUtils.getNullPropertyNames(updateDeliveryLine));
        dbDeliveryLine.setUpdatedBy(loginUserId);
        dbDeliveryLine.setUpdatedOn(new Date());
        return deliveryLineRepository.save(dbDeliveryLine);
    }


    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param deliveryNo
     * @param refDocNumber
     * @param invoiceNumber
     * @param itemCode
     * @param lineNo
     * @param languageId
     * @param loginUserID
     */
    public void deleteDeliveryLine(String companyCodeId,String plantId,String warehouseId,String deliveryNo,
                                   String refDocNumber,String invoiceNumber,String itemCode,
                                   Long lineNo,String languageId,String loginUserID){

        DeliveryLine deliveryLine = getDeliveryLine(companyCodeId,plantId,warehouseId,invoiceNumber,
                refDocNumber,languageId,deliveryNo,itemCode,lineNo);
        if(deliveryLine != null){
            deliveryLine.setDeletionIndicator(1L);
            deliveryLine.setUpdatedBy(loginUserID);
            deliveryLineRepository.save(deliveryLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + deliveryNo);
        }
    }

    /**
     *
     * @param searchDeliveryLine
     * @return
     * @throws ParseException
     */
    public List<DeliveryLine> findDeliveryLine(SearchDeliveryLine searchDeliveryLine) throws ParseException {

        DeliveryLineSpecification spec = new DeliveryLineSpecification(searchDeliveryLine);
        List<DeliveryLine> results = deliveryLineRepository.findAll(spec);
        results = results.stream().filter(n-> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        return results;
    }
}
