package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.v2.InboundLineV2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.IKeyValuePair;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.model.dto.StatusId;
import com.tekclover.wms.api.transaction.model.dto.Warehouse;
import com.tekclover.wms.api.transaction.model.inbound.InboundLine;
import com.tekclover.wms.api.transaction.model.inbound.UpdateInboundLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.v2.GrHeaderV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.AddPreInboundLineV2;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.PreInboundLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.AssignHHTUser;
import com.tekclover.wms.api.transaction.model.inbound.staging.CaseConfirmation;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingHeader;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLineEntity;
import com.tekclover.wms.api.transaction.model.inbound.staging.UpdateStagingHeader;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.SearchStagingLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.repository.GrHeaderV2Repository;
import com.tekclover.wms.api.transaction.repository.PreInboundLineRepository;
import com.tekclover.wms.api.transaction.repository.PreInboundLineV2Repository;
import com.tekclover.wms.api.transaction.repository.StagingLineRepository;
import com.tekclover.wms.api.transaction.repository.StagingLineV2Repository;
import com.tekclover.wms.api.transaction.repository.specification.StagingLineV2Specification;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StagingLineV2Service extends BaseService {

    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    @Autowired
    private StagingLineRepository stagingLineRepository;

    @Autowired
    private StagingHeaderService stagingHeaderService;

    @Autowired
    private InboundLineService inboundLineService;

    @Autowired
    private PreInboundLineService preInboundLineService;

    @Autowired
    private PreInboundLineRepository preInboundLineRepository;

    @Autowired
    private PreInboundLineV2Repository preInboundLineV2Repository;

    @Autowired
    private GrHeaderService grHeaderService;

    @Autowired
    private GrHeaderV2Repository grHeaderV2Repository;

    @Autowired
    private IDMasterService idmasterService;

    @Autowired
    private AuthTokenService authTokenService;

    String statusDescription = null;

    /**
     * getStagingLineEntitys
     *
     * @return
     */
    public StagingLineEntityV2 getStagingLine(String warehouseId, String companyCodeId,
                                              String plantId, String languageId, String preInboundNo,
                                              String refDocNumber, String stagingNo,
                                              String palletCode, String caseCode,
                                              Long lineNo, String itemCode, String manufacturerCode) {
        Optional<StagingLineEntityV2> stagingLineV2 = stagingLineV2Repository
                .findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndPalletCodeAndCaseCodeAndLineNoAndItemCodeAndManufacturerCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        stagingNo,
                        palletCode,
                        caseCode,
                        lineNo,
                        itemCode,
                        manufacturerCode,
                        0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",stagingNo: " + stagingNo +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",manufacturerCode: " + manufacturerCode +
                    " doesn't exist.");
        }

        return stagingLineV2.get();
    }

    /**
     * getStagingLineEntity
     *
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @return
     */
    public List<StagingLineEntityV2> getStagingLines(String warehouseId, String refDocNumber, String preInboundNo,
                                                     Long lineNo, String itemCode, String manufacturerCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository
                        .findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndManufacturerCodeAndDeletionIndicator(
                                getLanguageId(),
                                getCompanyCode(),
                                getPlantId(),
                                warehouseId,
                                refDocNumber,
                                preInboundNo,
                                lineNo,
                                itemCode,
                                manufacturerCode,
                                0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",manufacturerCode: " + manufacturerCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public long getStagingLineByStatusId(String warehouseId, String preInboundNo, String refDocNumber, String manufacturerCode) {
        long putAwayLineStatusIdCount = stagingLineV2Repository.getStagingLineCountByStatusId(
                getCompanyCode(), getPlantId(), warehouseId,
                preInboundNo, refDocNumber, manufacturerCode);
        return putAwayLineStatusIdCount;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param caseCode
     * @return
     */
    public List<StagingLineEntityV2> getStagingLine(String warehouseId, String refDocNumber,
                                                    String preInboundNo, Long lineNo,
                                                    String itemCode, String caseCode, String manufacturerCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndCaseCodeAndManufacturerCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        lineNo,
                        itemCode,
                        caseCode,
                        manufacturerCode,
                        0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",caseCode: " + caseCode +
                    ",manufacturerCode: " + manufacturerCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    /**
     * createStagingLineEntity
     * @param newStagingLines
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
//	public List<StagingLineEntityV2> createStagingLine (List<AddStagingLineV2> newStagingLines, String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//
//		List<StagingLineEntityV2> stagingLineEntityList = new ArrayList<>();
//		String preInboundNo = null;
//
//		for (AddStagingLineV2 newStagingLine : newStagingLines) {
//			log.info("newStagingLineEntity : " + newStagingLine);
//			if (newStagingLine.getCaseCode() == null) {
//				throw new BadRequestException("CaseCode is not presented.");
//			}
//
//			// Warehouse
//			AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
//			Warehouse warehouse = idmasterService.getWarehouse(newStagingLine.getWarehouseId(), authTokenForIDMasterService.getAccess_token());
//			// Insert based on the number of casecodes
//
//			for (String caseCode : newStagingLine.getCaseCode()) {
//
//				StagingLineEntityV2 dbStagingLineV2Entity = new StagingLineEntityV2();
//
//				BeanUtils.copyProperties(newStagingLine, dbStagingLineV2Entity, CommonUtils.getNullPropertyNames(newStagingLine));
//				dbStagingLineV2Entity.setCaseCode(caseCode);
//				dbStagingLineV2Entity.setPalletCode(caseCode); 	//Copy CASE_CODE
//
//				// STATUS_ID - Hard Coded Value "13"
//				dbStagingLineV2Entity.setStatusId (13L);
//
//				// C_ID
//				dbStagingLineV2Entity.setCompanyCode(warehouse.getCompanyCode());
//
//				// PLANT_ID
//				dbStagingLineV2Entity.setPlantId(warehouse.getPlantId());
//
//				// LANG_ID
//				dbStagingLineV2Entity.setLanguageId("EN");
//
//				/*Pass the C_ID,PLANT_ID,WH_ID,LANG_ID and ITM_CODE
//				for each record of staging line table into inventory table and
//				fetch the sum of INV_QTY+ ALLOC_QTY values and append in this field.
//				If results are null append as Zero*/
//
//				dbStagingLineV2Entity.setInventoryQuantity(stagingLineV2Repository.getTotalQuantity(
//						newStagingLine.getItemCode(),
//						newStagingLine.getWarehouseId(),
//						newStagingLine.getLanguageId(),
//						newStagingLine.getPlantId(),
//						newStagingLine.getCompanyCode()	));
//
//
//				dbStagingLineV2Entity.setDeletionIndicator(0L);
//				dbStagingLineV2Entity.setCreatedBy(loginUserID);
//				dbStagingLineV2Entity.setUpdatedBy(loginUserID);
//				dbStagingLineV2Entity.setCreatedOn(new Date());
//				dbStagingLineV2Entity.setUpdatedOn(new Date());
//
//				//Setting Description for Company, Plant and Warehouse
//				dbStagingLineV2Entity.setCompanyDescription(
//						stagingLineV2Repository.getCompanyDescription(newStagingLine.getCompanyCode()));
//				dbStagingLineV2Entity.setPlantDescription(
//						stagingLineV2Repository.getPlantDescription(newStagingLine.getCompanyCode(), newStagingLine.getPlantId()));
//				dbStagingLineV2Entity.setWarehouseDescription(
//						stagingLineV2Repository.getWarehouseDescription(
//								newStagingLine.getCompanyCode(), newStagingLine.getPlantId(), newStagingLine.getWarehouseId()));
//
//				stagingLineEntityList.add(dbStagingLineV2Entity);
//
//				// PreInboundNo
//				preInboundNo = dbStagingLineV2Entity.getPreInboundNo();
//			}
//		}
//
//		// Batch Insert
//		if (!stagingLineEntityList.isEmpty()) {
//			List<StagingLineEntityV2> createdStagingLineEntityList =
//					stagingLineV2Repository.saveAll(stagingLineEntityList);
//			log.info("created StagingLine records." + createdStagingLineEntityList);
//
////			List<StagingLineEntityV2> responseStagingLineList = new ArrayList<>();
////			for (StagingLineEntityV2 stagingLineV2Entity : createdStagingLineEntityList) {
////				responseStagingLineList.add(copyLineEntityToBean (stagingLineV2Entity));
////			}
//
//			// Update PreInboundLines
//			List<PreInboundLineEntity> preInboundLineList = preInboundLineService.getPreInboundLine(preInboundNo);
//			for (PreInboundLineEntity preInboundLineEntity : preInboundLineList) {
//				// STATUS_ID - Hard Coded Value "13"
//				preInboundLineEntity.setStatusId (13L);
//			}
//			List<PreInboundLineEntity> updatedList = preInboundLineRepository.saveAll(preInboundLineList);
//			log.info("updated PreInboundLineEntity : " + updatedList);
//			return createdStagingLineEntityList;
//		}
//
//		return null;
//	}

    /**
     * @param inputPreInboundLines
     * @param warehouseId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<StagingLineEntityV2> createStagingLine(List<PreInboundLineEntityV2> inputPreInboundLines,
                                                       String stagingNo, String warehouseId,
                                                       String companyCodeId, String plantId, String languageId,
                                                       String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<StagingLineEntityV2> stagingLineEntityList = new ArrayList<>();
        String preInboundNo = null;

        // Casecode needs to be created automatically by calling /{numberOfCases}/barcode") from StagingHeader
        Long numberOfCases = 1L;
        List<String> caseCodeList = stagingHeaderService.generateNumberRanges(numberOfCases, warehouseId, companyCodeId, plantId, languageId);
        if (caseCodeList == null || caseCodeList.isEmpty()) {
            throw new BadRequestException("CaseCode is not generated.");
        }

        String manufactureCode = null;
        String caseCodeForCaseConfirmation = null;
        List<CaseConfirmation> caseConfirmationList = new ArrayList<>();
        for (PreInboundLineEntityV2 newStagingLine : inputPreInboundLines) {
            log.info("newStagingLineEntity : " + newStagingLine);

            // Warehouse
            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
            Warehouse warehouse = idmasterService.getWarehouse(newStagingLine.getWarehouseId(),
                    companyCodeId, plantId, languageId,
                    authTokenForIDMasterService.getAccess_token());

            // Insert based on the number of casecodes
            for (String caseCode : caseCodeList) {
                StagingLineEntityV2 dbStagingLineEntity = new StagingLineEntityV2();
                BeanUtils.copyProperties(newStagingLine, dbStagingLineEntity, CommonUtils.getNullPropertyNames(newStagingLine));
                dbStagingLineEntity.setCaseCode(caseCode);
                dbStagingLineEntity.setPalletCode(caseCode);    //Copy CASE_CODE

                // STATUS_ID - Hard Coded Value "13"
                dbStagingLineEntity.setStatusId(13L);

                // LANG_ID
                dbStagingLineEntity.setLanguageId(languageId);

                // C_ID
                dbStagingLineEntity.setCompanyCode(companyCodeId);

                // PLANT_ID
                dbStagingLineEntity.setPlantId(plantId);

                dbStagingLineEntity.setOrderQty(newStagingLine.getOrderQty());

                /*
                 * Pass the C_ID,PLANT_ID,WH_ID,LANG_ID and ITM_CODE for each record of staging line table into inventory table and
                 * fetch the sum of INV_QTY+ ALLOC_QTY values and append in this field. If results are null append as Zero
                 */
                dbStagingLineEntity.setInventoryQuantity(
                        stagingLineV2Repository.getTotalQuantityNew(newStagingLine.getItemCode(),
                                warehouseId,
                                languageId,
                                plantId,
                                companyCodeId));

                //Pass ITM_CODE/SUPPLIER_CODE received in integration API into IMPARTNER table and fetch PARTNER_ITEM_BARCODE values. Values can be multiple
                String barcode = stagingLineV2Repository.getPartnerItemBarcode(newStagingLine.getItemCode(),
                        newStagingLine.getCompanyCode(),
                        newStagingLine.getPlantId(),
                        newStagingLine.getWarehouseId(),
                        newStagingLine.getManufacturerCode(),
                        newStagingLine.getLanguageId());
                //for interim only the following condition is used
                if (barcode == null) {
                    barcode = stagingLineV2Repository.getPartnerItemBarcode(newStagingLine.getItemCode(),
                            newStagingLine.getManufacturerCode());
                }
                dbStagingLineEntity.setPartner_item_barcode(barcode);

                IKeyValuePair description = stagingLineV2Repository.getDescription(companyCodeId,
                        languageId,
                        plantId,
                        warehouseId);

                statusDescription = stagingLineV2Repository.getStatusDescription(13L, languageId);
                dbStagingLineEntity.setStatusDescription(statusDescription);

                dbStagingLineEntity.setCompanyDescription(description.getCompanyDesc());
                dbStagingLineEntity.setPlantDescription(description.getPlantDesc());
                dbStagingLineEntity.setWarehouseDescription(description.getWarehouseDesc());
                dbStagingLineEntity.setStagingNo(stagingNo);
                dbStagingLineEntity.setDeletionIndicator(0L);
                dbStagingLineEntity.setCreatedBy(loginUserID);
                dbStagingLineEntity.setUpdatedBy(loginUserID);
                dbStagingLineEntity.setCreatedOn(new Date());
                dbStagingLineEntity.setUpdatedOn(new Date());
                stagingLineEntityList.add(dbStagingLineEntity);
                stagingLineV2Repository.save(dbStagingLineEntity);

                // PreInboundNo
                preInboundNo = dbStagingLineEntity.getPreInboundNo();

                caseCodeForCaseConfirmation = caseCode;

                //ManufactureCode
                manufactureCode = dbStagingLineEntity.getManufacturerCode();

                // CaseConfirmation preparation for creating caseConfirmation
                CaseConfirmation caseConfirmation = new CaseConfirmation();
                caseConfirmation.setWarehouseId(warehouseId);
                caseConfirmation.setPreInboundNo(preInboundNo);
                caseConfirmation.setRefDocNumber(dbStagingLineEntity.getRefDocNumber());
                caseConfirmation.setStagingNo(dbStagingLineEntity.getStagingNo());
                caseConfirmation.setPalletCode(dbStagingLineEntity.getPalletCode());
                caseConfirmation.setCaseCode(caseCode);
                caseConfirmation.setLineNo(dbStagingLineEntity.getLineNo());
                caseConfirmation.setItemCode(dbStagingLineEntity.getItemCode());
                caseConfirmation.setManufactureCode(dbStagingLineEntity.getManufacturerCode());
                caseConfirmationList.add(caseConfirmation);
            }
        }

        // Batch Insert
        if (!stagingLineEntityList.isEmpty()) {
//			List<StagingLineEntityV2> createdStagingLineEntityList = stagingLineV2Repository.saveAll(stagingLineEntityList);
//			log.info("created StagingLine records." + createdStagingLineEntityList);
            log.info("created StagingLine records." + stagingLineEntityList);

//			List<StagingLineV2> responseStagingLineList = new ArrayList<>();
//			for (StagingLineEntityV2 stagingLineEntity : createdStagingLineEntityList) {
//				responseStagingLineList.add(copyLineEntityToBeanV2 (stagingLineEntity));
//			}

            // Update PreInboundLines
            List<PreInboundLineEntityV2> preInboundLineList = preInboundLineService.getPreInboundLineV2(preInboundNo);
            statusDescription = stagingLineV2Repository.getStatusDescription(13L, languageId);
            preInboundLineList.stream().forEach(x -> {
                // STATUS_ID - Hard Coded Value "13"
                x.setStatusId(13L);
                x.setStatusDescription(statusDescription);
            });
            List<PreInboundLineEntityV2> updatedList = preInboundLineV2Repository.saveAll(preInboundLineList);
            log.info("updated PreInboundLineEntityV2 : " + updatedList);

            // Create CaseConfirmation
            List<StagingLineEntityV2> responseStagingLineEntityList =
                    caseConfirmation(caseConfirmationList, caseCodeForCaseConfirmation, manufactureCode,
                            companyCodeId, plantId, languageId, loginUserID);
            return responseStagingLineEntityList;
        }
        return null;
    }

//    /**
//     *
//     * @param warehouseId
//     * @param preInboundNo
//     * @param refDocNumber
//     * @param stagingNo
//     * @param palletCode
//     * @param caseCode
//     * @param lineNo
//     * @param itemCode
//     * @param loginUserID
//     * @param updateStagingLine
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public StagingLineEntityV2 updateStagingLine (String warehouseId,
//                                                  String preInboundNo, String refDocNumber, String stagingNo, String palletCode, String caseCode, Long lineNo,
//                                                  String itemCode, String loginUserID, String manufacturerCode, StagingLineEntityV2 updateStagingLine)
//            throws IllegalAccessException, InvocationTargetException {
//
//        StagingLineEntityV2 dbStagingLineV2 = getStagingLine(warehouseId, preInboundNo, refDocNumber, stagingNo, palletCode,
//                caseCode, lineNo, itemCode, manufacturerCode);
//
//        BeanUtils.copyProperties(updateStagingLine, dbStagingLineV2, CommonUtils.getNullPropertyNames(updateStagingLine));
//        dbStagingLineV2.setUpdatedBy(loginUserID);
//        dbStagingLineV2.setUpdatedOn(new Date());
//
//        return stagingLineV2Repository.save(dbStagingLineV2);
//    }

//    /**
//     *
//     * @param assignHHTUsers
//     * @param assignedUserId
//     * @param loginUserID
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public List<StagingLineEntityV2> assignHHTUser (List<AssignHHTUser> assignHHTUsers, String assignedUserId,
//                                                    String manufacturerCode, String loginUserID)
//            throws IllegalAccessException, InvocationTargetException {
//
//        List<StagingLineEntityV2> updatedStagingLineEntityList = new ArrayList<>();
//
//        for (AssignHHTUser assignHHTUser : assignHHTUsers) {
//
//            StagingLineEntityV2 dbStagingLineEntity = getStagingLine(assignHHTUser.getWarehouseId(),
//                    assignHHTUser.getPreInboundNo(),
//                    assignHHTUser.getRefDocNumber(),
//                    assignHHTUser.getStagingNo(),
//                    assignHHTUser.getPalletCode(),
//                    assignHHTUser.getCaseCode(),
//                    assignHHTUser.getLineNo(),
//                    assignHHTUser.getItemCode(),
//                    manufacturerCode);
//            dbStagingLineEntity.setAssignedUserId(assignedUserId);
//            dbStagingLineEntity.setUpdatedBy(loginUserID);
//            dbStagingLineEntity.setUpdatedOn(new Date());
//
//            StagingLineEntityV2 updatedStagingLineEntity = stagingLineV2Repository.save(dbStagingLineEntity);
//
//            updatedStagingLineEntityList.add(updatedStagingLineEntity);
//        }
//        return updatedStagingLineEntityList;
//    }

//    /**
//     *
//     * @param asnNumber
//     */
//    public void updateASN (String asnNumber) {
//        List<StagingLineEntityV2> StagingLineEntitys = getStagingLines();
//        StagingLineEntitys.forEach(stagLines -> stagLines.setReferenceField1(asnNumber));
//        stagingLineV2Repository.saveAll(StagingLineEntitys);
//    }

    /**
     * @param caseConfirmations
     * @param caseCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<StagingLineEntityV2> caseConfirmation(List<CaseConfirmation> caseConfirmations,
                                                      String caseCode, String manufacturerCode,
                                                      String companyCodeId, String plantId,
                                                      String languageId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        log.info("caseConfirmation--called----> : " + caseConfirmations);

        List<StagingLineEntityV2> updatedStagingLineEntityList = new ArrayList<>();

        for (CaseConfirmation caseConfirmation : caseConfirmations) {

            StagingLineEntityV2 dbStagingLineEntity = getStagingLine(caseConfirmation.getWarehouseId(),
                    companyCodeId, plantId, languageId,
                    caseConfirmation.getPreInboundNo(),
                    caseConfirmation.getRefDocNumber(),
                    caseConfirmation.getStagingNo(),
                    caseConfirmation.getPalletCode(),
                    caseConfirmation.getCaseCode(),
                    caseConfirmation.getLineNo(),
                    caseConfirmation.getItemCode(),
                    caseConfirmation.getManufactureCode());

            // update STATUS_ID value as 14
            dbStagingLineEntity.setStatusId(14L);
            dbStagingLineEntity.setCaseCode(caseCode);
            dbStagingLineEntity.setUpdatedBy(loginUserID);
            dbStagingLineEntity.setUpdatedOn(new Date());
            statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
            dbStagingLineEntity.setStatusDescription(statusDescription);
            StagingLineEntityV2 updatedStagingLineEntity = stagingLineV2Repository.save(dbStagingLineEntity);

            log.info("updatedStagingLineEntity------> : " + updatedStagingLineEntity);

            updatedStagingLineEntityList.add(updatedStagingLineEntity);

            if (updatedStagingLineEntity != null) {
                // STATUS updates

				 /* Pass WH_ID/PRE_IB_NO/REF_DOC_NO/STG_NO/IB_LINE_NO/ITM_CODE/CASE_CODE values in StagingLineEntity table and
				  validate STATUS_ID , if all the STATUS_ID values of the CASE_CODE are 14*/

                List<StagingLineEntityV2> stagingLineEntity =
                        stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndLineNoAndItemCodeAndCaseCodeAndManufacturerCodeAndDeletionIndicator(
                                languageId, companyCodeId, plantId, caseConfirmation.getWarehouseId(), caseConfirmation.getPreInboundNo(),
                                caseConfirmation.getRefDocNumber(), caseConfirmation.getStagingNo(), caseConfirmation.getLineNo(),
                                caseConfirmation.getItemCode(), caseCode, caseConfirmation.getManufactureCode(), 0L);

                boolean isStatus14 = false;

                List<Long> statusList = stagingLineEntity.stream().map(StagingLineEntity::getStatusId).collect(Collectors.toList());
                long statusIdCount = statusList.stream().filter(a -> a == 14L).count();

                log.info("status count : " + (statusIdCount == statusList.size()));

                isStatus14 = (statusIdCount == statusList.size());

                //-----logic for checking all records as 14 then only it should go to update header---issue--#5-------------
                if (!stagingLineEntity.isEmpty() && isStatus14) {

					 /* 1. Then pass WH_ID/PRE_IB_NO/REF_DOC_NO/STG_NO in STAGINGHEADER table and
					  update the STATUS_ID as 14,ST_CNF_BY and ST_CNF_ON fields*/

                    StagingHeaderV2 updateStagingHeader = new StagingHeaderV2();
                    updateStagingHeader.setStatusId(14L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
                    updateStagingHeader.setStatusDescription(statusDescription);

                    StagingHeaderV2 stagingHeader =
                            stagingHeaderService.updateStagingHeaderV2(companyCodeId, plantId, languageId,
                                    caseConfirmation.getWarehouseId(), caseConfirmation.getPreInboundNo(),
                                    caseConfirmation.getRefDocNumber(), caseConfirmation.getStagingNo(), loginUserID,
                                    updateStagingHeader);

                    log.info("stagingHeader : " + stagingHeader);


					 /* 2. Then Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE in INBOUNDLINE table and
					  updated STATUS_ID as 14*/

                    InboundLineV2 updateInboundLine = new InboundLineV2();
                    updateInboundLine.setStatusId(14L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
                    updateInboundLine.setStatusDescription(statusDescription);
                    InboundLineV2 inboundLine = inboundLineService.updateInboundLineV2(companyCodeId, plantId, languageId,
                            caseConfirmation.getWarehouseId(),
                            caseConfirmation.getRefDocNumber(),
                            caseConfirmation.getPreInboundNo(),
                            caseConfirmation.getLineNo(),
                            caseConfirmation.getItemCode(),
                            loginUserID, updateInboundLine);
                    log.info("inboundLine : " + inboundLine);
                }
            }
        }

        // Record Insertion in GRHEADER table
        if (!updatedStagingLineEntityList.isEmpty()) {

            StagingLineEntityV2 updatedStagingLineEntity = updatedStagingLineEntityList.get(0);

            log.info("updatedStagingLineEntity-----IN---GRHEADER---CREATION---> : " + updatedStagingLineEntity);

            GrHeaderV2 addGrHeader = new GrHeaderV2();
            BeanUtils.copyProperties(updatedStagingLineEntity, addGrHeader, CommonUtils.getNullPropertyNames(updatedStagingLineEntity));

            // GR_NO
            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
            long NUM_RAN_CODE = 5;
            String nextGRHeaderNumber = getNextRangeNumber(NUM_RAN_CODE, updatedStagingLineEntity.getWarehouseId(),
                    updatedStagingLineEntity.getCompanyCode(),
                    updatedStagingLineEntity.getPlantId(),
                    updatedStagingLineEntity.getLanguageId(),
                    authTokenForIDMasterService.getAccess_token());
            addGrHeader.setGoodsReceiptNo(nextGRHeaderNumber);

            // STATUS_ID
            addGrHeader.setStatusId(16L);
            statusDescription = stagingLineV2Repository.getStatusDescription(16L, languageId);
            addGrHeader.setStatusDescription(statusDescription);
            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            StatusId idStatus = idmasterService.getStatus(16L,
                    updatedStagingLineEntity.getWarehouseId(),
                    updatedStagingLineEntity.getLanguageId(),
                    authTokenForIDService.getAccess_token());
            addGrHeader.setReferenceField10(idStatus.getStatus());
            GrHeaderV2 createdGrHeader = grHeaderService.createGrHeaderV2(addGrHeader, loginUserID);
            log.info("createdGrHeader : " + createdGrHeader);
        }

        return updatedStagingLineEntityList;
    }

    /**
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param caseCode
     * @param loginUserID
     */
    public void deleteCases(String preInboundNo, Long lineNo, String itemCode, String caseCode, String manufacturerCode, String loginUserID) {

        List<Long> statusIds = new ArrayList<>();
        statusIds.add(13L);
        statusIds.add(14L);

        List<StagingLineEntityV2> StagingLineEntitys =
                stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndLineNoAndItemCodeAndStatusIdInAndManufacturerCodeAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        preInboundNo,
                        lineNo,
                        itemCode,
                        statusIds,
                        manufacturerCode,
                        0L
                );

        if (StagingLineEntitys == null) {
            throw new BadRequestException("Error in deleting Id: " +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",caseCode: " + caseCode +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        for (StagingLineEntityV2 StagingLineEntity : StagingLineEntitys) {

            StagingLineEntity.setDeletionIndicator(1L);
            StagingLineEntity.setUpdatedBy(loginUserID);
            StagingLineEntityV2 deletedStagingLineEntity = stagingLineV2Repository.save(StagingLineEntity);

            log.info("deletedStagingLineEntity : " + deletedStagingLineEntity);
        }
    }

    /**
     * @param stagingLineEntity
     * @return
     */
//	private StagingLineEntityV2 copyLineEntityToBean(StagingLineEntityV2 stagingLineEntity) {
//		StagingLineV2 stagingLine = new StagingLineV2();
//		BeanUtils.copyProperties(stagingLineEntity, stagingLine, CommonUtils.getNullPropertyNames(stagingLineEntity));
//		return stagingLine;
//	}

    /**
     * getStagingLineEntitys
     *
     * @return
     */
    public List<StagingLineEntityV2> getStagingLines() {

        List<StagingLineEntityV2> stagingLineEntityV2List = stagingLineV2Repository.findAll();
        stagingLineEntityV2List = stagingLineEntityV2List.stream().filter(n -> n.getDeletionIndicator() != null &&
                n.getDeletionIndicator() == 0).collect(Collectors.toList());

        return stagingLineEntityV2List;
    }

    /**
     * getStagingLineEntity
     *
     * @param palletCode
     * @return
     */
    public StagingLineEntityV2 getStagingLine(String companyCodeId, String plantId, String languageId,
                                              String warehouseId, String preInboundNo, String refDocNumber,
                                              String stagingNo, String palletCode, String caseCode,
                                              Long lineNo, String itemCode) {
        Optional<StagingLineEntityV2> stagingLineV2 = stagingLineV2Repository
                .findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndPalletCodeAndCaseCodeAndLineNoAndItemCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        stagingNo,
                        palletCode,
                        caseCode,
                        lineNo,
                        itemCode,
                        0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",stagingNo: " + stagingNo +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        return stagingLineV2.get();
    }

    /**
     * getStagingLineEntity
     *
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @return
     */
    public List<StagingLineEntityV2> getStagingLines(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                     String refDocNumber, String preInboundNo, Long lineNo,
                                                     String itemCode, String manufacturerCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository
                        .findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndManufacturerCodeAndDeletionIndicator(
                                languageId,
                                companyCodeId,
                                plantId,
                                warehouseId,
                                refDocNumber,
                                preInboundNo,
                                lineNo,
                                itemCode,
                                manufacturerCode,
                                0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",manufacturerCode: " + manufacturerCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    public List<StagingLineEntityV2> getStagingLines(String companyCodeId, String plantId, String languageId,
                                                     String warehouseId, String refDocNumber, String preInboundNo,
                                                     Long lineNo, String itemCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository
                        .findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndDeletionIndicator(
                                languageId,
                                companyCodeId,
                                plantId,
                                warehouseId,
                                refDocNumber,
                                preInboundNo,
                                lineNo,
                                itemCode,
                                0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public long getStagingLineByStatusId(String companyCodeId, String plantId, String warehouseId,
                                         String preInboundNo, String refDocNumber, String manufacturerCode) {
        long putAwayLineStatusIdCount = stagingLineV2Repository.getStagingLineCountByStatusId(
                companyCodeId, plantId, warehouseId,
                preInboundNo, refDocNumber, manufacturerCode);
        return putAwayLineStatusIdCount;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param caseCode
     * @return
     */
    public List<StagingLineEntityV2> getStagingLine(String companyCodeId, String plantId, String languageId,
                                                    String warehouseId, String refDocNumber, String preInboundNo,
                                                    Long lineNo, String itemCode, String caseCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndCaseCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        lineNo,
                        itemCode,
                        caseCode,
                        0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",caseCode: " + caseCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    public List<StagingLineEntityV2> getStagingLineV2(String companyCodeId, String plantId, String languageId,
                                                      String warehouseId, String refDocNumber, String preInboundNo,
                                                      Long lineNo, String itemCode, String caseCode) {
        List<StagingLineEntityV2> stagingLineV2 =
                stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndRefDocNumberAndPreInboundNoAndLineNoAndItemCodeAndCaseCodeAndDeletionIndicator(
                        languageId,
                        companyCodeId,
                        plantId,
                        warehouseId,
                        refDocNumber,
                        preInboundNo,
                        lineNo,
                        itemCode,
                        caseCode,
                        0L);
        if (stagingLineV2.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    ",caseCode: " + caseCode +
                    " doesn't exist.");
        }

        return stagingLineV2;
    }

    /**
     * @param searchStagingLine
     * @return
     * @throws ParseException
     */
    public List<StagingLineEntityV2> findStagingLine(SearchStagingLineV2 searchStagingLine)
            throws ParseException {
        StagingLineV2Specification spec = new StagingLineV2Specification(searchStagingLine);
        List<StagingLineEntityV2> searchResults = stagingLineV2Repository.findAll(spec);
        return searchResults;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param stagingNo
     * @param palletCode
     * @param caseCode
     * @param lineNo
     * @param itemCode
     * @param loginUserID
     * @param updateStagingLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StagingLineEntityV2 updateStagingLine(String companyCodeId, String plantId, String languageId,
                                                 String warehouseId, String preInboundNo, String refDocNumber,
                                                 String stagingNo, String palletCode, String caseCode, Long lineNo,
                                                 String itemCode, String loginUserID, StagingLineEntityV2 updateStagingLine)
            throws IllegalAccessException, InvocationTargetException {

        StagingLineEntityV2 dbStagingLineV2 = getStagingLine(companyCodeId, plantId, languageId, warehouseId,
                preInboundNo, refDocNumber, stagingNo, palletCode,
                caseCode, lineNo, itemCode);

        BeanUtils.copyProperties(updateStagingLine, dbStagingLineV2, CommonUtils.getNullPropertyNames(updateStagingLine));
        dbStagingLineV2.setUpdatedBy(loginUserID);
        dbStagingLineV2.setUpdatedOn(new Date());

        return stagingLineV2Repository.save(dbStagingLineV2);
    }

    /**
     * @param assignHHTUsers
     * @param assignedUserId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<StagingLineEntityV2> assignHHTUser(List<AssignHHTUser> assignHHTUsers, String companyCodeId, String plantId,
                                                   String languageId, String assignedUserId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        List<StagingLineEntityV2> updatedStagingLineEntityList = new ArrayList<>();

        for (AssignHHTUser assignHHTUser : assignHHTUsers) {

            StagingLineEntityV2 dbStagingLineEntity = getStagingLine(
                    companyCodeId,
                    plantId,
                    languageId,
                    assignHHTUser.getWarehouseId(),
                    assignHHTUser.getPreInboundNo(),
                    assignHHTUser.getRefDocNumber(),
                    assignHHTUser.getStagingNo(),
                    assignHHTUser.getPalletCode(),
                    assignHHTUser.getCaseCode(),
                    assignHHTUser.getLineNo(),
                    assignHHTUser.getItemCode());
            dbStagingLineEntity.setAssignedUserId(assignedUserId);
            dbStagingLineEntity.setUpdatedBy(loginUserID);
            dbStagingLineEntity.setUpdatedOn(new Date());

            StagingLineEntityV2 updatedStagingLineEntity = stagingLineV2Repository.save(dbStagingLineEntity);

            updatedStagingLineEntityList.add(updatedStagingLineEntity);
        }
        return updatedStagingLineEntityList;
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<StagingLineEntityV2> StagingLineEntitys = getStagingLines();
        StagingLineEntitys.stream().forEach(stagLines -> stagLines.setReferenceField1(asnNumber));
        stagingLineV2Repository.saveAll(StagingLineEntitys);
    }

    /**
     * @param caseConfirmations
     * @param caseCode
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<StagingLineEntityV2> caseConfirmation(List<CaseConfirmation> caseConfirmations,
                                                      String caseCode, String companyCodeId, String plantId,
                                                      String languageId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        log.info("caseConfirmation--called----> : " + caseConfirmations);

        List<StagingLineEntityV2> updatedStagingLineEntityList = new ArrayList<>();

        for (CaseConfirmation caseConfirmation : caseConfirmations) {

            StagingLineEntityV2 dbStagingLineEntity = getStagingLine(companyCodeId,
                    plantId,
                    languageId,
                    caseConfirmation.getWarehouseId(),
                    caseConfirmation.getPreInboundNo(),
                    caseConfirmation.getRefDocNumber(),
                    caseConfirmation.getStagingNo(),
                    caseConfirmation.getPalletCode(),
                    caseConfirmation.getCaseCode(),
                    caseConfirmation.getLineNo(),
                    caseConfirmation.getItemCode());

            // update STATUS_ID value as 14
            dbStagingLineEntity.setStatusId(14L);
            statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
            dbStagingLineEntity.setStatusDescription(statusDescription);
            dbStagingLineEntity.setCaseCode(caseCode);
            dbStagingLineEntity.setUpdatedBy(loginUserID);
            dbStagingLineEntity.setUpdatedOn(new Date());
            StagingLineEntityV2 updatedStagingLineEntity = stagingLineV2Repository.save(dbStagingLineEntity);

            log.info("updatedStagingLineEntity------> : " + updatedStagingLineEntity);

            updatedStagingLineEntityList.add(updatedStagingLineEntity);

            if (updatedStagingLineEntity != null) {
                // STATUS updates

				 /* Pass WH_ID/PRE_IB_NO/REF_DOC_NO/STG_NO/IB_LINE_NO/ITM_CODE/CASE_CODE values in StagingLineEntity table and
				  validate STATUS_ID , if all the STATUS_ID values of the CASE_CODE are 14*/

                List<StagingLineEntityV2> stagingLineEntity =
                        stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndLineNoAndItemCodeAndCaseCodeAndDeletionIndicator(
                                languageId, companyCodeId, plantId, caseConfirmation.getWarehouseId(), caseConfirmation.getPreInboundNo(),
                                caseConfirmation.getRefDocNumber(), caseConfirmation.getStagingNo(), caseConfirmation.getLineNo(),
                                caseConfirmation.getItemCode(), caseCode, 0L);

                boolean isStatus14 = false;

                List<Long> statusList = stagingLineEntity.stream().map(StagingLineEntity::getStatusId).collect(Collectors.toList());
                long statusIdCount = statusList.stream().filter(a -> a == 14L).count();

                log.info("status count : " + (statusIdCount == statusList.size()));

                isStatus14 = (statusIdCount == statusList.size());

                //-----logic for checking all records as 14 then only it should go to update header---issue--#5-------------
                if (!stagingLineEntity.isEmpty() && isStatus14) {

					 /* 1. Then pass WH_ID/PRE_IB_NO/REF_DOC_NO/STG_NO in STAGINGHEADER table and
					  update the STATUS_ID as 14,ST_CNF_BY and ST_CNF_ON fields*/

                    StagingHeaderV2 updateStagingHeader = new StagingHeaderV2();
                    updateStagingHeader.setStatusId(14L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
                    updateStagingHeader.setStatusDescription(statusDescription);
                    StagingHeaderV2 stagingHeader =
                            stagingHeaderService.updateStagingHeaderV2(
                                    companyCodeId,
                                    plantId,
                                    languageId,
                                    caseConfirmation.getWarehouseId(),
                                    caseConfirmation.getPreInboundNo(),
                                    caseConfirmation.getRefDocNumber(),
                                    caseConfirmation.getStagingNo(),
                                    loginUserID,
                                    updateStagingHeader);

                    log.info("stagingHeader : " + stagingHeader);


					 /* 2. Then Pass WH_ID/PRE_IB_NO/REF_DOC_NO/IB_LINE_NO/ITM_CODE in INBOUNDLINE table and
					  updated STATUS_ID as 14*/

                    InboundLineV2 updateInboundLine = new InboundLineV2();
                    updateInboundLine.setStatusId(14L);
                    statusDescription = stagingLineV2Repository.getStatusDescription(14L, languageId);
                    updateInboundLine.setStatusDescription(statusDescription);
                    InboundLineV2 inboundLine = inboundLineService.updateInboundLineV2(companyCodeId, plantId, languageId,
                            caseConfirmation.getWarehouseId(),
                            caseConfirmation.getRefDocNumber(),
                            caseConfirmation.getPreInboundNo(),
                            caseConfirmation.getLineNo(),
                            caseConfirmation.getItemCode(),
                            loginUserID, updateInboundLine);
                    log.info("inboundLine : " + inboundLine);
                }
            }
        }

        // Record Insertion in GRHEADER table
        if (!updatedStagingLineEntityList.isEmpty()) {

            StagingLineEntityV2 updatedStagingLineEntity = updatedStagingLineEntityList.get(0);

            log.info("updatedStagingLineEntity-----IN---GRHEADER---CREATION---> : " + updatedStagingLineEntity);

            GrHeaderV2 addGrHeader = new GrHeaderV2();
            BeanUtils.copyProperties(updatedStagingLineEntity, addGrHeader, CommonUtils.getNullPropertyNames(updatedStagingLineEntity));

            // GR_NO
            AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
            long NUM_RAN_CODE = 5;
            String nextGRHeaderNumber = getNextRangeNumber(NUM_RAN_CODE, updatedStagingLineEntity.getWarehouseId(),
                    updatedStagingLineEntity.getCompanyCode(),
                    updatedStagingLineEntity.getPlantId(),
                    updatedStagingLineEntity.getLanguageId(),
                    authTokenForIDMasterService.getAccess_token());
            addGrHeader.setGoodsReceiptNo(nextGRHeaderNumber);

            // STATUS_ID
            addGrHeader.setStatusId(16L);
            statusDescription = stagingLineV2Repository.getStatusDescription(16L, languageId);
            addGrHeader.setStatusDescription(statusDescription);

            AuthToken authTokenForIDService = authTokenService.getIDMasterServiceAuthToken();
            StatusId idStatus = idmasterService.getStatus(16L, updatedStagingLineEntity.getWarehouseId(),
                    updatedStagingLineEntity.getLanguageId(), authTokenForIDService.getAccess_token());
            addGrHeader.setReferenceField10(idStatus.getStatus());
            GrHeaderV2 createdGrHeader = grHeaderService.createGrHeaderV2(addGrHeader, loginUserID);
            log.info("createdGrHeader : " + createdGrHeader);
        }

        return updatedStagingLineEntityList;
    }


    /**
     * deleteStagingLineEntity
     *
     * @param loginUserID
     * @param palletCode
     */
    public void deleteStagingLine(String companyCode, String plantId, String languageId,
                                  String warehouseId, String preInboundNo, String refDocNumber,
                                  String stagingNo, String palletCode, String caseCode,
                                  Long lineNo, String itemCode, String loginUserID) {
        StagingLineEntityV2 StagingLineEntity = getStagingLine(companyCode, plantId, languageId, warehouseId,
                preInboundNo, refDocNumber, stagingNo, palletCode,
                caseCode, lineNo, itemCode);
        if (StagingLineEntity != null) {

            StagingLineEntity.setDeletionIndicator(1L);
            StagingLineEntity.setUpdatedBy(loginUserID);
            stagingLineV2Repository.save(StagingLineEntity);

        } else {

            throw new BadRequestException("Error in deleting Id:  warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",stagingNo: " + stagingNo +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }
    }

    /**
     * @param preInboundNo
     * @param lineNo
     * @param itemCode
     * @param caseCode
     * @param loginUserID
     */
    public void deleteCases(String preInboundNo, Long lineNo, String itemCode, String caseCode, String loginUserID) {

        List<Long> statusIds = new ArrayList<>();
        statusIds.add(13L);
        statusIds.add(14L);

        List<StagingLineEntityV2> StagingLineEntitys =
                stagingLineV2Repository.findByLanguageIdAndCompanyCodeAndPlantIdAndPreInboundNoAndLineNoAndItemCodeAndStatusIdInAndDeletionIndicator(
                        getLanguageId(),
                        getCompanyCode(),
                        getPlantId(),
                        preInboundNo,
                        lineNo,
                        itemCode,
                        statusIds,
                        0L
                );

        if (StagingLineEntitys == null) {
            throw new BadRequestException("Error in deleting Id: " +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",caseCode: " + caseCode +
                    ",lineNo: " + lineNo +
                    ",itemCode: " + itemCode +
                    " doesn't exist.");
        }

        for (StagingLineEntityV2 StagingLineEntity : StagingLineEntitys) {

            StagingLineEntity.setDeletionIndicator(1L);
            StagingLineEntity.setUpdatedBy(loginUserID);
            StagingLineEntityV2 deletedStagingLineEntity = stagingLineV2Repository.save(StagingLineEntity);

            log.info("deletedStagingLineEntity : " + deletedStagingLineEntity);
        }
    }


}
