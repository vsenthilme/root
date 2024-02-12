package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.*;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PickHeaderV2Specification;
import com.tekclover.wms.api.transaction.repository.specification.PickupHeaderV2Specification;
import com.tekclover.wms.api.transaction.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.outbound.pickup.AddPickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.PickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.SearchPickupHeader;
import com.tekclover.wms.api.transaction.model.outbound.pickup.UpdatePickupHeader;
import com.tekclover.wms.api.transaction.repository.specification.PickupHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PickupHeaderService {

    @Autowired
    private PickupHeaderRepository pickupHeaderRepository;

    //------------------------------------------------------------------------------------------------------

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    @Autowired
    private PickupHeaderV2Repository pickupHeaderV2Repository;

    String statusDescription = null;
    //------------------------------------------------------------------------------------------------------

    /**
     * getPickupHeaders
     *
     * @return
     */
    public List<PickupHeader> getPickupHeaders() {
        List<PickupHeader> pickupHeaderList = pickupHeaderRepository.findAll();
        pickupHeaderList = pickupHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return pickupHeaderList;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public PickupHeader getPickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                        String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        PickupHeader pickupHeader =
                pickupHeaderRepository.findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && pickupHeader.getDeletionIndicator() == 0) {
            return pickupHeader;
        }

        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     * @return
     */
    public long getPickupHeaderCountForDeliveryConfirmation(String warehouseId, String refDocNumber, String preOutboundNo, Long statusId) {
        long pickupHeaderCount = pickupHeaderRepository.getPickupHeaderByWarehouseIdAndRefDocNumberAndPreOutboundNoAndStatusIdInAndDeletionIndicator(
                warehouseId, refDocNumber, preOutboundNo, statusId, 0L);
        return pickupHeaderCount;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<PickupHeader> getPickupHeaderForReversal(String warehouseId, String preOutboundNo, String refDocNumber,
                                                         String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        List<PickupHeader> pickupHeader =
                pickupHeaderRepository.findAllByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && pickupHeader.size() > 0) {
            return pickupHeader;
        } else {
            return null;
        }
    }

    /**
     * getPickupHeader
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @return
     */
    public PickupHeader getPickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                        String partnerCode, String pickupNumber) {
        PickupHeader pickupHeader =
                pickupHeaderRepository.findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber, 0L);
        if (pickupHeader != null && pickupHeader.getDeletionIndicator() == 0) {
            return pickupHeader;
        }
        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public PickupHeader getPickupHeaderForUpdate(String warehouseId, String preOutboundNo, String refDocNumber,
                                                 String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        PickupHeader pickupHeader =
                pickupHeaderRepository.findByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null) {
            return pickupHeader;
        }

        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<PickupHeader> getPickupHeaderForUpdateConfirmation(String warehouseId, String preOutboundNo, String refDocNumber,
                                                                   String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        List<PickupHeader> pickupHeader =
                pickupHeaderRepository.findAllByWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && !pickupHeader.isEmpty()) {
            return pickupHeader;
        }
        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    /**
     * @param searchPickupHeader
     * @return
     * @throws ParseException
     */
    public List<PickupHeader> findPickupHeader(SearchPickupHeader searchPickupHeader)
            throws ParseException {
        PickupHeaderSpecification spec = new PickupHeaderSpecification(searchPickupHeader);
        List<PickupHeader> results = pickupHeaderRepository.findAll(spec);
        return results;
    }

    //Stream
    public Stream<PickupHeader> findPickupHeaderNew(SearchPickupHeader searchPickupHeader)
            throws ParseException {
        PickupHeaderSpecification spec = new PickupHeaderSpecification(searchPickupHeader);
        Stream<PickupHeader> results = pickupHeaderRepository.stream(spec, PickupHeader.class).parallel();
        return results;
    }

    /**
     * @param warehouseId
     * @param orderTypeId
     * @return
     */
    public List<PickupHeader> getPickupHeaderCount(String companyCodeId, String plantId, String languageId,
                                                   String warehouseId, List<Long> orderTypeId) {
        List<PickupHeader> header =
                pickupHeaderRepository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndOutboundOrderTypeIdInAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, 48L, orderTypeId, 0L);
        return header;
    }

    /**
     * @param warehouseId
     * @param orderTypeId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderCount(String companyCodeId, String plantId, String languageId,
                                                     String warehouseId,String levelId, List<Long> orderTypeId) {
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndLevelIdAndOutboundOrderTypeIdInAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, 48L, levelId, orderTypeId, 0L);
        return header;
    }

    /**
     * createPickupHeader
     *
     * @param newPickupHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeader createPickupHeader(AddPickupHeader newPickupHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PickupHeader dbPickupHeader = new PickupHeader();
        log.info("newPickupHeader : " + newPickupHeader);
        BeanUtils.copyProperties(newPickupHeader, dbPickupHeader);
        dbPickupHeader.setDeletionIndicator(0L);
        return pickupHeaderRepository.save(dbPickupHeader);
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updatePickupHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeader updatePickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                           String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID,
                                           UpdatePickupHeader updatePickupHeader) throws IllegalAccessException, InvocationTargetException {
        PickupHeader dbPickupHeader = getPickupHeaderForUpdate(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            BeanUtils.copyProperties(updatePickupHeader, dbPickupHeader, CommonUtils.getNullPropertyNames(updatePickupHeader));
            dbPickupHeader.setPickUpdatedBy(loginUserID);
            dbPickupHeader.setPickUpdatedOn(new Date());
            return pickupHeaderRepository.save(dbPickupHeader);
        }
        return null;
    }

    public List<PickupHeader> updatePickupHeaderForConfirmation(String warehouseId, String preOutboundNo, String refDocNumber,
                                                                String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID,
                                                                UpdatePickupHeader updatePickupHeader) throws IllegalAccessException, InvocationTargetException {
        List<PickupHeader> dbPickupHeader = getPickupHeaderForUpdateConfirmation(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null && !dbPickupHeader.isEmpty()) {
            List<PickupHeader> toSave = new ArrayList<>();
            for (PickupHeader data : dbPickupHeader) {
                BeanUtils.copyProperties(updatePickupHeader, data, CommonUtils.getNullPropertyNames(updatePickupHeader));
                data.setPickUpdatedBy(loginUserID);
                data.setPickUpdatedOn(new Date());
                toSave.add(data);
            }
            return pickupHeaderRepository.saveAll(toSave);
        }
        return null;
    }

    /**
     * updateAssignedPickerInPickupHeader
     *
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PickupHeader> patchAssignedPickerIdInPickupHeader(String loginUserID,
                                                                  List<UpdatePickupHeader> updatePickupHeaderList) throws IllegalAccessException, InvocationTargetException {
        List<PickupHeader> pickupHeaderList = new ArrayList<>();
        try {
            log.info("Process start to update Assigned Picker Id in PickupHeader: " + updatePickupHeaderList);
            for (UpdatePickupHeader data : updatePickupHeaderList) {
                log.info("PickupHeader object to update : " + data);
                PickupHeader dbPickupHeader = getPickupHeaderForUpdate(data.getWarehouseId(), data.getPreOutboundNo(), data.getRefDocNumber(), data.getPartnerCode(),
                        data.getPickupNumber(), data.getLineNumber(), data.getItemCode());
                log.info("Old PickupHeader object from db : " + data);
                if (dbPickupHeader != null) {
                    dbPickupHeader.setAssignedPickerId(data.getAssignedPickerId());
                    dbPickupHeader.setPickUpdatedBy(loginUserID);
                    dbPickupHeader.setPickUpdatedOn(new Date());
                    PickupHeader pickupHeader = pickupHeaderRepository.save(dbPickupHeader);
                    pickupHeaderList.add(pickupHeader);
                } else {
                    log.info("No record for PickupHeader object from db for data : " + data);
                    throw new BadRequestException("Error in data");
                }
            }
            return pickupHeaderList;
        } catch (Exception e) {
            log.error("Update Assigned Picker Id in PickupHeader failed for : " + updatePickupHeaderList);
            throw new BadRequestException("Error in data");
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeader deletePickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                           String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PickupHeader dbPickupHeader = getPickupHeader(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            dbPickupHeader.setDeletionIndicator(1L);
            dbPickupHeader.setPickupReversedBy(loginUserID);
            dbPickupHeader.setPickupReversedOn(new Date());
            return pickupHeaderRepository.save(dbPickupHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting PickupHeader : -> Id: " + lineNumber);
        }
    }

    public List<PickupHeader> deletePickupHeaderForReversal(String warehouseId, String preOutboundNo, String refDocNumber,
                                                            String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<PickupHeader> dbPickupHeader = getPickupHeaderForReversal(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null && dbPickupHeader.size() > 0) {
            List<PickupHeader> toSaveData = new ArrayList<>();
            dbPickupHeader.forEach(pickupHeader -> {
                pickupHeader.setDeletionIndicator(1L);
                pickupHeader.setPickupReversedBy(loginUserID);
                pickupHeader.setPickupReversedOn(new Date());
                toSaveData.add(pickupHeader);
            });
            return pickupHeaderRepository.saveAll(toSaveData);
        } else {
            return null;
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param proposedStorageBin
     * @param proposedPackCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeader deletePickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                           String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String proposedStorageBin,
                                           String proposedPackCode, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        PickupHeader dbPickupHeader = getPickupHeader(warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            dbPickupHeader.setDeletionIndicator(1L);
            dbPickupHeader.setPickupReversedBy(loginUserID);
            dbPickupHeader.setPickupReversedOn(new Date());
            return pickupHeaderRepository.save(dbPickupHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting PickupHeader : -> Id: " + lineNumber);
        }
    }

    //===================================================================V2===============================================================

    /**
     * getPickupHeaders
     *
     * @return
     */
    public List<PickupHeaderV2> getPickupHeadersV2() {
        List<PickupHeaderV2> pickupHeaderList = pickupHeaderV2Repository.findAll();
        pickupHeaderList = pickupHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return pickupHeaderList;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public PickupHeaderV2 getPickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                            String preOutboundNo, String refDocNumber, String partnerCode,
                                            String pickupNumber, Long lineNumber, String itemCode) {
        PickupHeaderV2 pickupHeader =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && pickupHeader.getDeletionIndicator() == 0) {
            return pickupHeader;
        }

        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderForPickListCancellationV2(String companyCodeId, String plantId, String languageId, String warehouseId, String refDocNumber) {
        List<PickupHeaderV2> pickupHeader =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, refDocNumber, 0L);
        log.info("New Picklist ---> pickup header : " + pickupHeader);
        if (pickupHeader != null && !pickupHeader.isEmpty()) {
            return pickupHeader;
        }
        log.info("No Pickup Header for New Picklist");
        return null;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     * @return
     */
    public long getPickupHeaderCountForDeliveryConfirmationV2(String companyCodeId, String plantId, String languageId,
                                                              String warehouseId, String refDocNumber, String preOutboundNo, Long statusId) {
        long pickupHeaderCount = pickupHeaderV2Repository.getPickupHeaderByWarehouseIdAndRefDocNumberAndPreOutboundNoAndStatusIdInAndDeletionIndicatorV2(
                companyCodeId, plantId, languageId, warehouseId, refDocNumber, preOutboundNo, statusId, 0L);
        return pickupHeaderCount;
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderForReversalV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                             String preOutboundNo, String refDocNumber, String partnerCode,
                                                             String pickupNumber, Long lineNumber, String itemCode) {
        List<PickupHeaderV2> pickupHeader =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && pickupHeader.size() > 0) {
            return pickupHeader;
        } else {
            return null;
        }
    }

    /**
     * getPickupHeader
     *
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @return
     */
    public PickupHeaderV2 getPickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                            String preOutboundNo, String refDocNumber, String partnerCode, String pickupNumber) {
        PickupHeaderV2 pickupHeader =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber, 0L);
        if (pickupHeader != null && pickupHeader.getDeletionIndicator() == 0) {
            return pickupHeader;
        }
        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public PickupHeaderV2 getPickupHeaderForUpdateV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                                     String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        PickupHeaderV2 pickupHeader =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null) {
            return pickupHeader;
        }

        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderForUpdateConfirmationV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                                                       String partnerCode, String pickupNumber, Long lineNumber, String itemCode) {
        List<PickupHeaderV2> pickupHeader =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndPickupNumberAndLineNumberAndItemCodeAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode, pickupNumber,
                        lineNumber, itemCode, 0L);
        if (pickupHeader != null && !pickupHeader.isEmpty()) {
            return pickupHeader;
        }
        throw new BadRequestException("The given PickupHeader ID : " +
                "warehouseId : " + warehouseId +
                ",preOutboundNo : " + preOutboundNo +
                ",refDocNumber : " + refDocNumber +
                ",partnerCode : " + partnerCode +
                ",pickupNumber : " + pickupNumber +
                ",lineNumber : " + lineNumber +
                ",itemCode : " + itemCode +
                " doesn't exist.");
    }

    //Stream - Find
    public Stream<PickupHeaderV2> findPickupHeaderV2(SearchPickupHeaderV2 searchPickupHeader)
            throws ParseException {
        PickupHeaderV2Specification spec = new PickupHeaderV2Specification(searchPickupHeader);
        Stream<PickupHeaderV2> results = pickupHeaderV2Repository.stream(spec, PickupHeaderV2.class);
        return results;
    }

    /**
     *
     * @param searchPickupHeader
     * @return
     * @throws ParseException
     */
    public List<PickupHeaderV2> findPickupHeaderNewV2(SearchPickupHeaderV2 searchPickupHeader)
            throws ParseException {
        log.info("searchPickupHeader: " + searchPickupHeader);
        PickupHeaderV2Specification spec = new PickupHeaderV2Specification(searchPickupHeader);
        List<PickupHeaderV2> results = pickupHeaderV2Repository.stream(spec, PickupHeaderV2.class).collect(Collectors.toList());
        log.info("Pickupheader Results: " + results);
        return results;
    }

    /**
     * @param warehouseId
     * @param orderTypeId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderCountV2(String companyCodeId, String plantId, String languageId, String warehouseId, List<Long> orderTypeId) {
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndOutboundOrderTypeIdInAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, 48L, orderTypeId, 0L);
        return header;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderAutomationV2(String companyCodeId, String plantId, String languageId, String warehouseId, String assignedPickerId) {
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndStatusIdAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L, 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderAutomationWithOutStatusIdV2(String companyCodeId, String plantId, String languageId,
                                                                           String warehouseId, String assignedPickerId) {
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 0L);
        return header;
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderAutomation(String companyCodeId, String plantId, String languageId, String warehouseId, String assignedPickerId, Long statusId) throws java.text.ParseException {
        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, statusId, dates[0], dates[1], 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderAutomateCurrentDate(String companyCodeId, String plantId, String languageId, String warehouseId, String assignedPickerId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L, dates[0], dates[1], 0L);
        return header;
    }

    public PickupHeaderV2 getPickupHeaderAutomateCurrentDateNew(String companyCodeId, String plantId, String languageId, String warehouseId, String assignedPickerId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        PickupHeaderV2 header =
                pickupHeaderV2Repository.findTopByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L, dates[0], dates[1], 0L);
        return header;
    }

    public List<PickupHeaderV2> getPickupHeaderAutomateCurrentDateHhtList(String companyCodeId, String plantId, String languageId, String warehouseId, List<String> assignedPickerId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdInAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L, dates[0], dates[1], 0L);
        return header;
    }

    public PickupHeaderV2 getPickupHeaderAutomateCurrentDateHhtListNew(String companyCodeId, String plantId, String languageId, String warehouseId, List<String> assignedPickerId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        PickupHeaderV2 header =
                pickupHeaderV2Repository.findTopByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdInAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L, dates[0], dates[1], 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @param levelId
     * @return
     * @throws java.text.ParseException
     */
    public String getPickupHeaderAutomateCurrentDateHhtListCount(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                 List<String> assignedPickerId, Long levelId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        IKeyValuePair header =
                pickupHeaderV2Repository.getAssignPickerNew(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L,levelId, dates[0], dates[1]);
        if(header != null) {
            return header.getAssignPicker();
        }
        return null;
    }

    public String getPickupHeaderAutomateCurrentDateHhtListCount(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                 List<String> assignedPickerId, Long levelId, Long statusId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        IKeyValuePair header =
                pickupHeaderV2Repository.getAssignPickerNew(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, levelId, statusId, dates[0], dates[1]);
        if(header != null) {
            return header.getAssignPicker();
        }
        return null;
    }

    public String getPickupHeaderAutomateCurrentDateHhtListCountNew(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                         List<String> assignedPickerId, Long outboundOrderTypeId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        IKeyValuePair header =
                pickupHeaderV2Repository.getAssignPickerWh100New(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, 48L,outboundOrderTypeId, dates[0], dates[1]);
        if(header != null) {
            return header.getAssignPicker();
        }
        return null;
    }

    public String getPickupHeaderAutomateCurrentDateHhtListCountNew(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                                    List<String> assignedPickerId, Long outboundOrderTypeId, Long statusId) throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        IKeyValuePair header =
                pickupHeaderV2Repository.getAssignPickerWh100New(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, statusId,outboundOrderTypeId, dates[0], dates[1]);
        if(header != null) {
            return header.getAssignPicker();
        }
        return null;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @param refDocNumber
     * @return
     * @throws java.text.ParseException
     */
    public List<PickupHeaderV2> getPickupHeaderAutomateCurrentDateSameOrder(String companyCodeId, String plantId, String languageId,
                                                                            String warehouseId, String assignedPickerId, String refDocNumber)
            throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndRefDocNumberAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, refDocNumber, 48L, dates[0], dates[1], 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @param refDocNumber
     * @return
     * @throws java.text.ParseException
     */
    public PickupHeaderV2 getPickupHeaderAutomateCurrentDateSameOrderNew(String companyCodeId, String plantId, String languageId,
                                                                         String warehouseId, String assignedPickerId, String refDocNumber)
            throws java.text.ParseException {

        Date[] dates = DateUtils.addTimeToDatesForSearch(new Date(), new Date());

        PickupHeaderV2 header =
                pickupHeaderV2Repository.findTopByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdAndRefDocNumberAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicatorOrderByPickupCreatedOn(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId, refDocNumber, 48L, dates[0], dates[1], 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @return
     */
    public PickupHeaderV2 getPickupHeaderAutomation(String companyCodeId, String plantId, String languageId, String warehouseId, String itemCode, String manufacturerName) {
        PickupHeaderV2 header =
                pickupHeaderV2Repository.findTopByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerNameAndStatusIdAndDeletionIndicatorOrderByPickupCreatedOnDesc(
                        companyCodeId, plantId, languageId, warehouseId, itemCode, manufacturerName, 48L, 0L);
        return header;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param assignedPickerId
     * @return
     */
    public List<PickupHeaderV2> getPickupHeaderAutomationWithoutStatusId(String companyCodeId, String plantId, String languageId, String warehouseId, List<String> assignedPickerId) {
        List<PickupHeaderV2> header =
                pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndAssignedPickerIdInAndDeletionIndicator(
                        companyCodeId, plantId, languageId, warehouseId, assignedPickerId,  0L);
        return header;
    }

    /**
     * createPickupHeader
     *
     * @param newPickupHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeaderV2 createPickupHeaderV2(PickupHeaderV2 newPickupHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PickupHeaderV2 dbPickupHeader = new PickupHeaderV2();
        log.info("newPickupHeader : " + newPickupHeader);
        BeanUtils.copyProperties(newPickupHeader, dbPickupHeader, CommonUtils.getNullPropertyNames(newPickupHeader));

        IKeyValuePair description = stagingLineV2Repository.getDescription(dbPickupHeader.getCompanyCodeId(),
                dbPickupHeader.getLanguageId(),
                dbPickupHeader.getPlantId(),
                dbPickupHeader.getWarehouseId());

        if (dbPickupHeader.getStatusId() != null) {
            statusDescription = stagingLineV2Repository.getStatusDescription(dbPickupHeader.getStatusId(), dbPickupHeader.getLanguageId());
            dbPickupHeader.setStatusDescription(statusDescription);
        }

        dbPickupHeader.setCompanyDescription(description.getCompanyDesc());
        dbPickupHeader.setPlantDescription(description.getPlantDesc());
        dbPickupHeader.setWarehouseDescription(description.getWarehouseDesc());

//        if(newPickupHeader.getLevelId() == null) {
//
//        }

        dbPickupHeader.setDeletionIndicator(0L);
        dbPickupHeader.setPickupCreatedBy(loginUserID);
        dbPickupHeader.setPickupCreatedOn(new Date());
        dbPickupHeader.setPickUpdatedBy(loginUserID);
        dbPickupHeader.setPickUpdatedOn(new Date());
        return pickupHeaderV2Repository.save(dbPickupHeader);
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updatePickupHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeaderV2 updatePickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID,
                                               PickupHeaderV2 updatePickupHeader) throws IllegalAccessException, InvocationTargetException {
        PickupHeaderV2 dbPickupHeader = getPickupHeaderForUpdateV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            BeanUtils.copyProperties(updatePickupHeader, dbPickupHeader, CommonUtils.getNullPropertyNames(updatePickupHeader));
            dbPickupHeader.setPickUpdatedBy(loginUserID);
            dbPickupHeader.setPickUpdatedOn(new Date());
            return pickupHeaderV2Repository.save(dbPickupHeader);
        }
        return null;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param itemCode
     * @param manufacturerName
     * @param barcodeId
     * @param loginUserID
     */
    public void updatePickupHeaderForBarcodeV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                               String itemCode, String manufacturerName, String barcodeId, String loginUserID) {
        List<PickupHeaderV2> dbPickupHeaderList = pickupHeaderV2Repository.findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndItemCodeAndManufacturerNameAndStatusIdAndDeletionIndicator(
                companyCodeId, plantId, languageId, warehouseId, itemCode, manufacturerName, 48L, 0L);
        log.info("PickupHeader status_48: " + dbPickupHeaderList);
        if (dbPickupHeaderList != null && !dbPickupHeaderList.isEmpty()) {
            for (PickupHeaderV2 dbPickupHeader : dbPickupHeaderList) {
                dbPickupHeader.setBarcodeId(barcodeId);
                dbPickupHeader.setPickUpdatedBy(loginUserID);
                dbPickupHeader.setPickUpdatedOn(new Date());
                pickupHeaderV2Repository.save(dbPickupHeader);
            }
        }
    }

    public List<PickupHeaderV2> updatePickupHeaderForConfirmationV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                                                    String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID,
                                                                    PickupHeaderV2 updatePickupHeader) throws IllegalAccessException, InvocationTargetException {
        List<PickupHeaderV2> dbPickupHeader = getPickupHeaderForUpdateConfirmationV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null && !dbPickupHeader.isEmpty()) {
            List<PickupHeaderV2> toSave = new ArrayList<>();
            for (PickupHeaderV2 data : dbPickupHeader) {
                BeanUtils.copyProperties(updatePickupHeader, data, CommonUtils.getNullPropertyNames(updatePickupHeader));
                data.setPickUpdatedBy(loginUserID);
                data.setPickUpdatedOn(new Date());
                toSave.add(data);
            }
            return pickupHeaderV2Repository.saveAll(toSave);
        }
        return null;
    }

    /**
     * updateAssignedPickerInPickupHeader
     *
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PickupHeaderV2> patchAssignedPickerIdInPickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String loginUserID,
                                                                      List<PickupHeaderV2> updatePickupHeaderList) throws IllegalAccessException, InvocationTargetException {
        List<PickupHeaderV2> pickupHeaderList = new ArrayList<>();
        try {
            log.info("Process start to update Assigned Picker Id in PickupHeader: " + updatePickupHeaderList);
            for (PickupHeaderV2 data : updatePickupHeaderList) {
                log.info("PickupHeader object to update : " + data);
                PickupHeaderV2 dbPickupHeader = getPickupHeaderForUpdateV2(
                        companyCodeId, plantId, languageId,
                        warehouseId, data.getPreOutboundNo(), data.getRefDocNumber(), data.getPartnerCode(),
                        data.getPickupNumber(), data.getLineNumber(), data.getItemCode());
                log.info("Old PickupHeader object from db : " + data);
                if (dbPickupHeader != null) {
                    dbPickupHeader.setAssignedPickerId(data.getAssignedPickerId());
                    dbPickupHeader.setPickUpdatedBy(loginUserID);
                    dbPickupHeader.setPickUpdatedOn(new Date());
                    PickupHeaderV2 pickupHeader = pickupHeaderV2Repository.save(dbPickupHeader);
                    pickupHeaderList.add(pickupHeader);
                } else {
                    log.info("No record for PickupHeader object from db for data : " + data);
                    throw new BadRequestException("Error in data");
                }
            }
            return pickupHeaderList;
        } catch (Exception e) {
            log.error("Update Assigned Picker Id in PickupHeader failed for : " + updatePickupHeaderList);
            throw new BadRequestException("Error in data");
        }
    }

    /**
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeaderV2 deletePickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PickupHeaderV2 dbPickupHeader = getPickupHeaderV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            dbPickupHeader.setDeletionIndicator(1L);
            dbPickupHeader.setPickupReversedBy(loginUserID);
            dbPickupHeader.setPickupReversedOn(new Date());
            return pickupHeaderV2Repository.save(dbPickupHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting PickupHeader : -> Id: " + lineNumber);
        }
    }

    public List<PickupHeaderV2> deletePickupHeaderForReversalV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                                                String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<PickupHeaderV2> dbPickupHeader = getPickupHeaderForReversalV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null && dbPickupHeader.size() > 0) {
            List<PickupHeaderV2> toSaveData = new ArrayList<>();
            dbPickupHeader.forEach(pickupHeader -> {
                pickupHeader.setDeletionIndicator(1L);
                pickupHeader.setPickupReversedBy(loginUserID);
                pickupHeader.setPickupReversedOn(new Date());
                toSaveData.add(pickupHeader);
            });
            return pickupHeaderV2Repository.saveAll(toSaveData);
        } else {
            return null;
        }
    }

    /**
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param preOutboundNo
     * @param refDocNumber
     * @param partnerCode
     * @param pickupNumber
     * @param lineNumber
     * @param itemCode
     * @param proposedStorageBin
     * @param proposedPackCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PickupHeaderV2 deletePickupHeaderV2(String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String proposedStorageBin,
                                               String proposedPackCode, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        PickupHeaderV2 dbPickupHeader = getPickupHeaderV2(companyCodeId, plantId, languageId, warehouseId, preOutboundNo, refDocNumber, partnerCode,
                pickupNumber, lineNumber, itemCode);
        if (dbPickupHeader != null) {
            dbPickupHeader.setDeletionIndicator(1L);
            dbPickupHeader.setPickupReversedBy(loginUserID);
            dbPickupHeader.setPickupReversedOn(new Date());
            return pickupHeaderV2Repository.save(dbPickupHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting PickupHeader : -> Id: " + lineNumber);
        }
    }

    /**
     *
     * @param findPickUpHeader
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public PickUpHeaderReport findPickUpHeaderWithStatusId(FindPickUpHeader findPickUpHeader) throws ParseException, java.text.ParseException {

        if (findPickUpHeader.getFromDate() != null && findPickUpHeader.getToDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findPickUpHeader.getFromDate(), findPickUpHeader.getToDate());
            findPickUpHeader.setFromDate(dates[0]);
            findPickUpHeader.setToDate(dates[1]);
        }

        PickHeaderV2Specification spec = new PickHeaderV2Specification(findPickUpHeader);
        List<PickupHeaderV2> results = pickupHeaderV2Repository.findAll(spec);

        Map<String, PickerReport> pickerReportMap = new HashMap<>();

        for (PickupHeaderV2 pickUpHeader : results) {
            if (pickUpHeader.getStatusId() == 48) {
                String assignedPickerId = pickUpHeader.getAssignedPickerId();

                PickerReport pickerReport = pickerReportMap.computeIfAbsent(assignedPickerId, k -> {
                    PickerReport newPickerReport = new PickerReport();
                    newPickerReport.setAssignedPickerId(assignedPickerId);
                    newPickerReport.setPickUpHeaderCount(0L);
                    newPickerReport.setLevelId(pickUpHeader.getLevelId());
                    newPickerReport.setPickupHeaderV2(new ArrayList<>());
                    return newPickerReport;
                });
                pickerReport.setPickUpHeaderCount(pickerReport.getPickUpHeaderCount() + 1);


                PickUpHeaderCount pickUpHeaderCount = new PickUpHeaderCount();
                BeanUtils.copyProperties(pickUpHeader, pickUpHeaderCount);
                pickerReport.getPickupHeaderV2().add(pickUpHeaderCount);
            }
        }

        List<PickerReport> pickerReportList = new ArrayList<>(pickerReportMap.values());
        PickUpHeaderReport pickUpHeaderReport = new PickUpHeaderReport();
        pickUpHeaderReport.setPickerReportList(pickerReportList);

        return pickUpHeaderReport;
    }

    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param languageId
     * @param warehouseId
     * @param refDocNumber
     * @param loginUserID
     * @return
     * @throws Exception
     */
    //Delete PickUpHeaderV2
    public List<PickupHeaderV2> deletePickUpHeaderV2(String companyCodeId, String plantId, String languageId,
                                                     String warehouseId, String refDocNumber, String loginUserID)throws Exception {

        List<PickupHeaderV2> pickupHeaderList = pickupHeaderV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
                companyCodeId, plantId, languageId, warehouseId, refDocNumber, 0L);
        log.info("PickList Cancellation - PickupHeader : " + pickupHeaderList);
        List<PickupHeaderV2> pickupHeaders = new ArrayList<>();
        if (pickupHeaderList != null && !pickupHeaderList.isEmpty()) {
            for(PickupHeaderV2 pickupHeaderV2 : pickupHeaderList){
            pickupHeaderV2.setDeletionIndicator(1L);
            pickupHeaderV2.setPickupReversedBy(loginUserID);
            pickupHeaderV2.setPickupReversedOn(new Date());
            pickupHeaderV2Repository.save(pickupHeaderV2);
            pickupHeaders.add(pickupHeaderV2);
            }
        }
        return pickupHeaders;
    }
}
