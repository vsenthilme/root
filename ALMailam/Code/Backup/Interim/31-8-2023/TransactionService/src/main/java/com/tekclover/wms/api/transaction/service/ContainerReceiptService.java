package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.v2.ContainerReceiptV2;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.v2.SearchContainerReceiptV2;
import com.tekclover.wms.api.transaction.repository.ContainerReceiptV2Repository;
import com.tekclover.wms.api.transaction.repository.StagingLineV2Repository;
import com.tekclover.wms.api.transaction.repository.specification.ContainerReceiptSpecificationV2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.inbound.InboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.AddContainerReceipt;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.ContainerReceipt;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.SearchContainerReceipt;
import com.tekclover.wms.api.transaction.model.inbound.containerreceipt.UpdateContainerReceipt;
import com.tekclover.wms.api.transaction.repository.ContainerReceiptRepository;
import com.tekclover.wms.api.transaction.repository.InboundHeaderRepository;
import com.tekclover.wms.api.transaction.repository.specification.ContainerReceiptSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContainerReceiptService extends BaseService {

    @Autowired
    private ContainerReceiptRepository containerReceiptRepository;

    @Autowired
    private IDMasterService idmasterService;

    @Autowired
    private InboundHeaderRepository inboundHeaderRepository;

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    private ContainerReceiptV2Repository containerReceiptV2Repository;

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    private final String tableName = "CONTAINERRECEIPT";

    String statusDescription = null;

    /**
     * getContainerReceipts
     *
     * @return
     */
    public List<ContainerReceipt> getContainerReceipts() {
        List<ContainerReceipt> containerReceiptList = containerReceiptRepository.findAll();
        containerReceiptList =
                containerReceiptList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return containerReceiptList;
    }

    /**
     * @param containerReceiptNo
     * @return
     */
    public ContainerReceipt getContainerReceipt(String containerReceiptNo) {
        Optional<ContainerReceipt> containerReceipt =
                containerReceiptRepository.findByContainerReceiptNoAndDeletionIndicator(containerReceiptNo, 0L);
        if (!containerReceipt.isEmpty()) {
            return containerReceipt.get();
        } else {
            throw new BadRequestException("The given ContainerReceipt ID : " + containerReceiptNo + " doesn't exist.");
        }
    }

    /**
     * @param preInboundNo
     * @param refDocNumber
     * @param containerReceiptNo
     * @param loginUserID
     * @return
     */
    public ContainerReceipt getContainerReceipt(String preInboundNo,
                                                String refDocNumber, String containerReceiptNo, String warehouseId, String loginUserID) {
        Optional<ContainerReceipt> containerReceipt =
                containerReceiptRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndContainerReceiptNoAndDeletionIndicator(
                        getLanguageId(), getCompanyCode(), getPlantId(), warehouseId, preInboundNo, refDocNumber,
                        containerReceiptNo, 0L);
        if (containerReceipt.isEmpty()) {
            throw new BadRequestException("The given ContainerReceipt ID : " + containerReceiptNo + " doesn't exist.");
        }
        return containerReceipt.get();
    }

    /**
     * @param searchContainerReceipt
     * @return
     * @throws ParseException
     */
    public List<ContainerReceipt> findContainerReceipt(SearchContainerReceipt searchContainerReceipt) throws ParseException {

        if (searchContainerReceipt.getStartContainerReceivedDate() != null && searchContainerReceipt.getEndContainerReceivedDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchContainerReceipt.getStartContainerReceivedDate(), searchContainerReceipt.getEndContainerReceivedDate());
            searchContainerReceipt.setStartContainerReceivedDate(dates[0]);
            searchContainerReceipt.setEndContainerReceivedDate(dates[1]);
        }

        ContainerReceiptSpecification spec = new ContainerReceiptSpecification(searchContainerReceipt);
        List<ContainerReceipt> results = containerReceiptRepository.findAll(spec);

        for (ContainerReceipt containerReceipt : results) {
            List<InboundHeader> inboundHeaderData = this.inboundHeaderRepository.findByRefDocNumberAndDeletionIndicator(containerReceipt.getRefDocNumber(), 0L);
            if (inboundHeaderData != null && !inboundHeaderData.isEmpty() && inboundHeaderData.get(0).getConfirmedOn() != null) {
                containerReceipt.setReferenceField5(inboundHeaderData.get(0).getConfirmedOn().toString());
            } else {
                containerReceipt.setReferenceField5(null);
            }
        }
        return results;
    }

    /**
     * createContainerReceipt
     *
     * @param newContainerReceipt
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ContainerReceipt createContainerReceipt(AddContainerReceipt newContainerReceipt, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        log.info("newContainerReceipt : " + newContainerReceipt);

        ContainerReceipt dbContainerReceipt = new ContainerReceipt();
        BeanUtils.copyProperties(newContainerReceipt, dbContainerReceipt, CommonUtils.getNullPropertyNames(newContainerReceipt));
        dbContainerReceipt.setLanguageId(getLanguageId());
        dbContainerReceipt.setCompanyCodeId(getCompanyCode());
        dbContainerReceipt.setPlantId(getPlantId());

        // WH_ID
        dbContainerReceipt.setWarehouseId(newContainerReceipt.getWarehouseId());

        // REF_DOC_NO
        dbContainerReceipt.setRefDocNumber(newContainerReceipt.getRefDocNumber());

        // PRE_IB_NO
        dbContainerReceipt.setPreInboundNo(newContainerReceipt.getPreInboundNo());

        // CONT_REC_NO
        AuthToken authTokenForIdmasterService = authTokenService.getIDMasterServiceAuthToken();
        String containerRecNo = getNextRangeNumber(newContainerReceipt.getWarehouseId(),
                newContainerReceipt.getCompanyCodeId(),
                newContainerReceipt.getPlantId(),
                newContainerReceipt.getLanguageId(),
                authTokenForIdmasterService.getAccess_token());
        log.info("containerRecNo-------> : " + containerRecNo);

        dbContainerReceipt.setContainerReceiptNo(containerRecNo);
        dbContainerReceipt.setContainerReceivedDate(new Date());
        dbContainerReceipt.setReferenceField2(newContainerReceipt.getReferenceField2());
        dbContainerReceipt.setStatusId(10L);
        dbContainerReceipt.setDeletionIndicator(0L);
        dbContainerReceipt.setCreatedBy(loginUserID);
        dbContainerReceipt.setUpdatedBy(loginUserID);
        dbContainerReceipt.setCreatedOn(new Date());
        dbContainerReceipt.setUpdatedOn(new Date());
        return containerReceiptRepository.save(dbContainerReceipt);
    }

    /**
     * updateContainerReceipt
     *
     * @param containerReceiptNo
     * @param updateContainerReceipt
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ContainerReceipt updateContainerReceipt(String containerReceiptNo,
                                                   UpdateContainerReceipt updateContainerReceipt, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        log.info("update container receipt no : " + containerReceiptNo);
        log.info("update container receipt data : " + updateContainerReceipt);
        ContainerReceipt dbContainerReceipt = getContainerReceipt(containerReceiptNo);
        BeanUtils.copyProperties(updateContainerReceipt, dbContainerReceipt, CommonUtils.getNullPropertyNames(updateContainerReceipt));

        // REF_DOC_NO
        if (updateContainerReceipt.getRefDocNumber() != null && !updateContainerReceipt.getRefDocNumber()
                .equalsIgnoreCase(dbContainerReceipt.getRefDocNumber())) {
            log.info("Inserting Audit log for REF_DOC_NO");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(), dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "REF_DOC_NO",
                    dbContainerReceipt.getRefDocNumber(), updateContainerReceipt.getRefDocNumber(),
                    loginUserID);
        }

        // CONT_REC_DATE
        if (updateContainerReceipt.getContainerReceivedDate() != null &&
                updateContainerReceipt.getContainerReceivedDate() != dbContainerReceipt.getContainerReceivedDate()) {
            log.info("Inserting Audit log for CONT_REC_DATE");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(), dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "CONT_REC_DATE",
                    String.valueOf(dbContainerReceipt.getContainerReceivedDate()),
                    String.valueOf(updateContainerReceipt.getContainerReceivedDate()),
                    loginUserID);
        }

        // CONT_NO
        if (updateContainerReceipt.getContainerNo() != null && !updateContainerReceipt.getContainerNo()
                .equalsIgnoreCase(dbContainerReceipt.getContainerNo())) {
            log.info("Inserting Audit log for CONT_NO");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(),
                    dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "CONT_NO",
                    dbContainerReceipt.getContainerNo(), updateContainerReceipt.getContainerNo(),
                    loginUserID);
        }

        dbContainerReceipt.setUpdatedBy(loginUserID);
        dbContainerReceipt.setUpdatedOn(new Date());
        return containerReceiptRepository.save(dbContainerReceipt);
    }

    /**
     * deleteContainerReceipt
     *
     * @param loginUserID
     * @param containerReceiptNo
     */
    public void deleteContainerReceipt(String preInboundNo, String refDocNumber, String containerReceiptNo,
                                       String warehouseId, String loginUserID) {
        ContainerReceipt containerReceipt = getContainerReceipt(preInboundNo, refDocNumber, containerReceiptNo,
                warehouseId, loginUserID);
        if (containerReceipt != null) {
            containerReceipt.setDeletionIndicator(1L);
            containerReceipt.setUpdatedBy(loginUserID);
            containerReceiptRepository.save(containerReceipt);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + containerReceiptNo);
        }
    }

    /**
     * @param warehouseId
     * @param accessToken
     * @return
     */
    private String getNextRangeNumber(String warehouseId,
                                      String companyCodeId, String plantId,
                                      String languageId, String accessToken) {
        /*
         * Pass WH_ID - User logged in WH_ID and NUM_RAN_ID = 01 values in NumberRANGE table
         * fetch NUM_RAN_CURRENT value of FISCALYEAR = CURRENT YEAR and add +1and then update in
         * CONTAINERRECEIPT table during save
         */
        long NUM_RAN_CODE = 1;
        int FISCALYEAR = Year.now().getValue();
        String nextNumberRange = idmasterService.getNextNumberRange(NUM_RAN_CODE, warehouseId, companyCodeId, plantId, languageId, accessToken);
//		String nextNumberRange = idmasterService.getNextNumberRange(NUM_RAN_CODE, FISCALYEAR, warehouseId , companyCodeId, plantId, languageId,  accessToken);
        return nextNumberRange;
    }

    //--------------------------------------------------------------V2-----------------------------------------------------------------------------//

    /**
     * @param containerReceiptNo
     * @return
     */
    public ContainerReceiptV2 getContainerReceiptV2(String companyCode, String plantId, String languageId,
                                                    String warehouseId, String containerReceiptNo) {
        Optional<ContainerReceiptV2> containerReceipt =
                containerReceiptV2Repository.findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndContainerReceiptNoAndDeletionIndicator(
                        companyCode, plantId, languageId,
                        warehouseId, containerReceiptNo, 0L);
        if (containerReceipt.isPresent()) {
            return containerReceipt.get();
        } else {
            throw new BadRequestException("The given ContainerReceipt ID : " + containerReceiptNo + " doesn't exist.");
        }
    }

    /**
     * @param preInboundNo
     * @param refDocNumber
     * @param containerReceiptNo
     * @param warehouseId
     * @return
     */
    public ContainerReceiptV2 getContainerReceiptV2(String companyCode, String plantId, String languageId, String warehouseId,
                                                    String containerReceiptNo, String preInboundNo, String refDocNumber) {
        Optional<ContainerReceiptV2> containerReceipt =
                containerReceiptV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndContainerReceiptNoAndDeletionIndicator(
                        languageId, companyCode, plantId, warehouseId, preInboundNo, refDocNumber,
                        containerReceiptNo, 0L);
        if (containerReceipt.isEmpty()) {
            throw new BadRequestException("The given ContainerReceipt ID : " + containerReceiptNo + " doesn't exist.");
        }
        return containerReceipt.get();
    }

    /**
     * @param searchContainerReceipt
     * @return
     * @throws ParseException
     */
    public List<ContainerReceiptV2> findContainerReceiptV2(SearchContainerReceiptV2 searchContainerReceipt) throws ParseException {

        if (searchContainerReceipt.getStartContainerReceivedDate() != null && searchContainerReceipt.getEndContainerReceivedDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchContainerReceipt.getStartContainerReceivedDate(), searchContainerReceipt.getEndContainerReceivedDate());
            searchContainerReceipt.setStartContainerReceivedDate(dates[0]);
            searchContainerReceipt.setEndContainerReceivedDate(dates[1]);
        }

        ContainerReceiptSpecificationV2 spec = new ContainerReceiptSpecificationV2(searchContainerReceipt);
        List<ContainerReceiptV2> results = containerReceiptV2Repository.findAll(spec);

        for (ContainerReceiptV2 containerReceipt : results) {
            List<InboundHeader> inboundHeaderData = this.inboundHeaderRepository.findByRefDocNumberAndDeletionIndicator(containerReceipt.getRefDocNumber(), 0L);
            if (inboundHeaderData != null && !inboundHeaderData.isEmpty() && inboundHeaderData.get(0).getConfirmedOn() != null) {
                containerReceipt.setReferenceField5(inboundHeaderData.get(0).getConfirmedOn().toString());
            } else {
                containerReceipt.setReferenceField5(null);
            }
        }
        return results;
    }

    /**
     * @param newContainerReceipt
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ContainerReceiptV2 createContainerReceiptV2(ContainerReceiptV2 newContainerReceipt, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        log.info("newContainerReceipt : " + newContainerReceipt);

        ContainerReceiptV2 dbContainerReceipt = new ContainerReceiptV2();
        BeanUtils.copyProperties(newContainerReceipt, dbContainerReceipt, CommonUtils.getNullPropertyNames(newContainerReceipt));

        IKeyValuePair description = stagingLineV2Repository.getDescription(dbContainerReceipt.getCompanyCodeId(),
                dbContainerReceipt.getLanguageId(),
                dbContainerReceipt.getPlantId(),
                dbContainerReceipt.getWarehouseId());

        dbContainerReceipt.setCompanyDescription(description.getCompanyDesc());
        dbContainerReceipt.setPlantDescription(description.getPlantDesc());
        dbContainerReceipt.setWarehouseDescription(description.getWarehouseDesc());

        // WH_ID
        dbContainerReceipt.setWarehouseId(newContainerReceipt.getWarehouseId());

        // REF_DOC_NO
        dbContainerReceipt.setRefDocNumber(newContainerReceipt.getRefDocNumber());

        // PRE_IB_NO
        dbContainerReceipt.setPreInboundNo(newContainerReceipt.getPreInboundNo());

        // CONT_REC_NO
        AuthToken authTokenForIdmasterService = authTokenService.getIDMasterServiceAuthToken();
        String containerRecNo = getNextRangeNumber(newContainerReceipt.getWarehouseId(),
                newContainerReceipt.getCompanyCodeId(),
                newContainerReceipt.getPlantId(),
                newContainerReceipt.getLanguageId(),
                authTokenForIdmasterService.getAccess_token());
        log.info("containerRecNo-------> : " + containerRecNo);

        dbContainerReceipt.setContainerReceiptNo(containerRecNo);
        dbContainerReceipt.setContainerReceivedDate(new Date());
        dbContainerReceipt.setReferenceField2(newContainerReceipt.getReferenceField2());
        dbContainerReceipt.setStatusId(10L);
        statusDescription = stagingLineV2Repository.getStatusDescription(10L, newContainerReceipt.getLanguageId());
        dbContainerReceipt.setStatusDescription(statusDescription);

        dbContainerReceipt.setDeletionIndicator(0L);
        dbContainerReceipt.setCreatedBy(loginUserID);
        dbContainerReceipt.setUpdatedBy(loginUserID);
        dbContainerReceipt.setCreatedOn(new Date());
        dbContainerReceipt.setUpdatedOn(new Date());
        return containerReceiptV2Repository.save(dbContainerReceipt);
    }

    /**
     * @param containerReceiptNo
     * @param updateContainerReceipt
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ContainerReceiptV2 updateContainerReceiptV2(String companyCode, String plantId, String languageId,
                                                       String warehouseId, String containerReceiptNo,
                                                       ContainerReceiptV2 updateContainerReceipt, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        log.info("update container receipt no : " + containerReceiptNo);
        log.info("update container receipt data : " + updateContainerReceipt);

        ContainerReceiptV2 dbContainerReceipt = getContainerReceiptV2(companyCode, plantId, languageId, warehouseId, containerReceiptNo);
        BeanUtils.copyProperties(updateContainerReceipt, dbContainerReceipt, CommonUtils.getNullPropertyNames(updateContainerReceipt));

        // REF_DOC_NO
        if (updateContainerReceipt.getRefDocNumber() != null && !updateContainerReceipt.getRefDocNumber()
                .equalsIgnoreCase(dbContainerReceipt.getRefDocNumber())) {
            log.info("Inserting Audit log for REF_DOC_NO");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(), dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "REF_DOC_NO",
                    dbContainerReceipt.getRefDocNumber(), updateContainerReceipt.getRefDocNumber(),
                    loginUserID);
        }

        // CONT_REC_DATE
        if (updateContainerReceipt.getContainerReceivedDate() != null &&
                updateContainerReceipt.getContainerReceivedDate() != dbContainerReceipt.getContainerReceivedDate()) {
            log.info("Inserting Audit log for CONT_REC_DATE");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(), dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "CONT_REC_DATE",
                    String.valueOf(dbContainerReceipt.getContainerReceivedDate()),
                    String.valueOf(updateContainerReceipt.getContainerReceivedDate()),
                    loginUserID);
        }

        // CONT_NO
        if (updateContainerReceipt.getContainerNo() != null && !updateContainerReceipt.getContainerNo()
                .equalsIgnoreCase(dbContainerReceipt.getContainerNo())) {
            log.info("Inserting Audit log for CONT_NO");
            idmasterService.createAuditLogRecord(dbContainerReceipt.getWarehouseId(),
                    dbContainerReceipt.getCompanyCodeId(),
                    dbContainerReceipt.getPlantId(),
                    dbContainerReceipt.getLanguageId(),
                    tableName, 0, 0, "CONT_NO",
                    dbContainerReceipt.getContainerNo(), updateContainerReceipt.getContainerNo(),
                    loginUserID);
        }

        dbContainerReceipt.setUpdatedBy(loginUserID);
        dbContainerReceipt.setUpdatedOn(new Date());
        return containerReceiptV2Repository.save(dbContainerReceipt);
    }

    /**
     * @param preInboundNo
     * @param refDocNumber
     * @param containerReceiptNo
     * @param warehouseId
     * @param loginUserID
     */
    public void deleteContainerReceiptV2(String companyCode, String plantId, String languageId, String warehouseId,
                                         String preInboundNo, String refDocNumber, String containerReceiptNo, String loginUserID) {
        ContainerReceiptV2 containerReceipt = getContainerReceiptV2(companyCode, plantId, languageId, warehouseId,
                preInboundNo, refDocNumber, containerReceiptNo);
        if (containerReceipt != null) {
            containerReceipt.setDeletionIndicator(1L);
            containerReceipt.setUpdatedBy(loginUserID);
            containerReceiptV2Repository.save(containerReceipt);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + containerReceiptNo);
        }
    }

}
