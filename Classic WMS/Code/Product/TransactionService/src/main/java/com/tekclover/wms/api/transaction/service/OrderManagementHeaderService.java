package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.AddOrderManagementHeader;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.OrderManagementHeader;
import com.tekclover.wms.api.transaction.model.outbound.ordermangement.UpdateOrderManagementHeader;
import com.tekclover.wms.api.transaction.repository.OrderManagementHeaderRepository;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderManagementHeaderService {

    @Autowired
    private OrderManagementHeaderRepository orderManagementHeaderRepository;

    /**
     * getOrderManagementHeaders
     *
     * @return
     */
    public List<OrderManagementHeader> getOrderManagementHeaders() {
        List<OrderManagementHeader> orderManagementHeaderList = orderManagementHeaderRepository.findAll();
        orderManagementHeaderList = orderManagementHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return orderManagementHeaderList;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @return
     */
    public OrderManagementHeader getOrderManagementHeader(String companyCodeId, String plantId, String languageId,
                                                          String warehouseId, String preOutboundNo,
                                                          String refDocNumber, String partnerCode) {
        OrderManagementHeader orderManagementHeader =
                orderManagementHeaderRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, 0L);
        if (orderManagementHeader != null) {
            return orderManagementHeader;
        }

        throw new BadRequestException("The given OrderManagementHeader ID : " +
                "companyCodeId : " + companyCodeId +
                "plantId : " + plantId +
                "languageId : " + languageId +
                "warehouseId : " + warehouseId +
                ", preOutboundNo : " + preOutboundNo +
                ", refDocNumber : " + refDocNumber +
                ", partnerCode : " + partnerCode +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @return
     */
    public OrderManagementHeader getOrderManagementHeaderForUpdate(String companyCodeId, String plantId, String languageId,
                                                                   String warehouseId, String preOutboundNo,
                                                                   String refDocNumber, String partnerCode) {
        OrderManagementHeader orderManagementHeader =
                orderManagementHeaderRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, 0L);
        if (orderManagementHeader != null) {
            return orderManagementHeader;
        }
        log.info("The given OrderManagementHeader ID : " +
                "warehouseId : " + warehouseId +
                ", preOutboundNo : " + preOutboundNo +
                ", refDocNumber : " + refDocNumber +
                ", partnerCode : " + partnerCode +
                " doesn't exist.");
        return null;
    }

    /**
     * @param preOutboundNo
     * @return
     */
    public OrderManagementHeader getOrderManagementHeader(String preOutboundNo) {
        OrderManagementHeader orderManagementHeader = orderManagementHeaderRepository.findByPreOutboundNo(preOutboundNo);
        if (orderManagementHeader != null && orderManagementHeader.getDeletionIndicator() == 0) {
            return orderManagementHeader;
        } else {
            throw new BadRequestException("The given OrderManagementHeader ID : " + preOutboundNo + " doesn't exist.");
        }
    }

    /**
     * createOrderManagementHeader
     *
     * @param newOrderManagementHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OrderManagementHeader createOrderManagementHeader(AddOrderManagementHeader newOrderManagementHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<OrderManagementHeader> ordermangementheader =
                orderManagementHeaderRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndDeletionIndicator(
                        newOrderManagementHeader.getLanguageId(),
                        newOrderManagementHeader.getCompanyCodeId(),
                        newOrderManagementHeader.getPlantId(),
                        newOrderManagementHeader.getWarehouseId(),
                        newOrderManagementHeader.getPreOutboundNo(),
                        newOrderManagementHeader.getRefDocNumber(),
                        newOrderManagementHeader.getPartnerCode(),
                        0L);
        if (!ordermangementheader.isEmpty()) {
            throw new BadRequestException("Record is getting duplicated with the given values");
        }
        OrderManagementHeader dbOrderManagementHeader = new OrderManagementHeader();
        log.info("newOrderManagementHeader : " + newOrderManagementHeader);
        BeanUtils.copyProperties(newOrderManagementHeader, dbOrderManagementHeader);
        dbOrderManagementHeader.setDeletionIndicator(0L);
        return orderManagementHeaderRepository.save(dbOrderManagementHeader);
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param loginUserID
     * @param updateOrderManagementHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OrderManagementHeader updateOrderManagementHeader(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                             String preOutboundNo, String refDocNumber, String partnerCode,
                                                             String loginUserID, OrderManagementHeader updateOrderManagementHeader)
            throws IllegalAccessException, InvocationTargetException {
        OrderManagementHeader dbOrderManagementHeader =
                getOrderManagementHeaderForUpdate(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode);
        if (dbOrderManagementHeader != null) {
            BeanUtils.copyProperties(updateOrderManagementHeader, dbOrderManagementHeader, CommonUtils.getNullPropertyNames(updateOrderManagementHeader));
            return orderManagementHeaderRepository.save(dbOrderManagementHeader);
        }
        return null;
    }

    /**
     * deleteOrderManagementHeader
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param loginUserID
     */
    public void deleteOrderManagementHeader(String companyCodeId, String plantId, String languageId, String warehouseId,
                                            String preOutboundNo, String refDocNumber, String partnerCode, String loginUserID) {
        OrderManagementHeader orderManagementHeader =
                getOrderManagementHeader(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode);
        if (orderManagementHeader != null) {
            orderManagementHeader.setDeletionIndicator(1L);
            orderManagementHeader.setPickerAssignedBy(loginUserID );
            orderManagementHeaderRepository.save(orderManagementHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + refDocNumber);
        }
    }
}
