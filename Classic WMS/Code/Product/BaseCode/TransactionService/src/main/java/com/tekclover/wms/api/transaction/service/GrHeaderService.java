package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.inbound.gr.AddGrHeader;
import com.tekclover.wms.api.transaction.model.inbound.gr.GrHeader;
import com.tekclover.wms.api.transaction.model.inbound.gr.GrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.SearchGrHeader;
import com.tekclover.wms.api.transaction.model.inbound.gr.UpdateGrHeader;
import com.tekclover.wms.api.transaction.repository.GrHeaderRepository;
import com.tekclover.wms.api.transaction.repository.GrLineRepository;
import com.tekclover.wms.api.transaction.repository.specification.GrHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GrHeaderService {

    @Autowired
    private GrHeaderRepository grHeaderRepository;

    @Autowired
    private GrLineRepository grLineRepository;

    @Autowired
    private GrLineService grLineService;

    /**
     * getGrHeaders
     *
     * @return
     */
    public List<GrHeader> getGrHeaders() {
        List<GrHeader> grHeaderList = grHeaderRepository.findAll();
        grHeaderList =
                grHeaderList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return grHeaderList;
    }

    /**
     * getGrHeader
     *
     * @param goodsReceiptNo
     * @return
     */
    public GrHeader getGrHeader(String warehouseId, String companyCode, String plantId, String languageId,
                                String preInboundNo, String refDocNumber, String stagingNo,
                                String goodsReceiptNo, String palletCode, String caseCode) {
        Optional<GrHeader> grHeader =
                grHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        stagingNo,
                        goodsReceiptNo,
                        palletCode,
                        caseCode,
                        0L);
        if (grHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",stagingNo: " + stagingNo +
                    ",palletCode: " + palletCode +
                    ",caseCode: " + caseCode +
                    ",goodsReceiptNo: " + goodsReceiptNo +
                    " doesn't exist.");
        }

        return grHeader.get();
    }

    /**
     * @param warehouseId
     * @param goodsReceiptNo
     * @param caseCode
     * @param refDocNumber
     * @return
     */
    public List<GrHeader> getGrHeader(String warehouseId, String companyCode, String plantId,
                                      String languageId, String goodsReceiptNo, String caseCode, String refDocNumber) {
        List<GrHeader> grHeader =
                grHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndGoodsReceiptNoAndCaseCodeAndRefDocNumberAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        goodsReceiptNo,
                        caseCode,
                        refDocNumber,
                        0L);
        if (grHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",goodsReceiptNo: " + goodsReceiptNo + "," +
                    ",caseCode: " + caseCode +
                    " doesn't exist.");
        }
        return grHeader;
    }

    /**
     *
     * @param refDocNumber
     * @param warehouseId
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param caseCode
     * @return
     */
    public List<GrHeader> getGrHeaderForReverse(String refDocNumber, String warehouseId,String companyCode, String plantId,
                                                String languageId, String preInboundNo, String caseCode) {
        List<GrHeader> grHeader =
                grHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndRefDocNumberAndWarehouseIdAndPreInboundNoAndCaseCodeAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        refDocNumber,
                        warehouseId,
                        preInboundNo,
                        caseCode,
                        0L);
        if (grHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",refDocNumber: " + refDocNumber +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",preInboundNo: " + preInboundNo +
                    ",caseCode: " + caseCode +
                    " doesn't exist.");
        }
        return grHeader;
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public List<GrHeader> getGrHeader(String warehouseId, String preInboundNo,String companyCode, String plantId,
                                      String languageId, String refDocNumber) {
        List<GrHeader> grHeader =
                grHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        0L);
        if (grHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ", refDocNumber: " + refDocNumber +
                    ", preInboundNo: " + preInboundNo +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    " doesn't exist.");
        }
        return grHeader;
    }

    /**
     *
     * @param searchGrHeader
     * @return
     * @throws Exception
     */
    public List<GrHeader> findGrHeader(SearchGrHeader searchGrHeader) throws Exception {
        searchGrHeader.setDeletionIndicator(0L);
        if (searchGrHeader.getStartCreatedOn() != null && searchGrHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchGrHeader.getStartCreatedOn(), searchGrHeader.getEndCreatedOn());
            searchGrHeader.setStartCreatedOn(dates[0]);
            searchGrHeader.setEndCreatedOn(dates[1]);
        }
        GrHeaderSpecification spec = new GrHeaderSpecification(searchGrHeader);
        List<GrHeader> results = grHeaderRepository.findAll(spec);
//		log.info("results: " + results);
        return results;
    }

    /**
     * @param searchGrHeader
     * @return
     * @throws Exception
     */
    public Stream<GrHeader> findGrHeaderNew(SearchGrHeader searchGrHeader) throws Exception {
        searchGrHeader.setDeletionIndicator(0L);
        if (searchGrHeader.getStartCreatedOn() != null && searchGrHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchGrHeader.getStartCreatedOn(), searchGrHeader.getEndCreatedOn());
            searchGrHeader.setStartCreatedOn(dates[0]);
            searchGrHeader.setEndCreatedOn(dates[1]);
        }
        GrHeaderSpecification spec = new GrHeaderSpecification(searchGrHeader);
        Stream<GrHeader> results = grHeaderRepository.stream(spec, GrHeader.class);
        return results;
    }

    /**
     * createGrHeader
     *
     * @param newGrHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public GrHeader createGrHeader(AddGrHeader newGrHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<GrHeader> grheader =
                grHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndGoodsReceiptNoAndPalletCodeAndCaseCodeAndDeletionIndicator(
                        newGrHeader.getLanguageId(),
                        newGrHeader.getCompanyCode(),
                        newGrHeader.getPlantId(),
                        newGrHeader.getWarehouseId(),
                        newGrHeader.getPreInboundNo(),
                        newGrHeader.getRefDocNumber(),
                        newGrHeader.getStagingNo(),
                        newGrHeader.getGoodsReceiptNo(),
                        newGrHeader.getPalletCode(),
                        newGrHeader.getCaseCode(),
                        0L);
        if (!grheader.isEmpty()) {
            throw new BadRequestException("Record is getting duplicated with the given values");
        }
        GrHeader dbGrHeader = new GrHeader();
        log.info("newGrHeader : " + newGrHeader);
        BeanUtils.copyProperties(newGrHeader, dbGrHeader, CommonUtils.getNullPropertyNames(newGrHeader));

        dbGrHeader.setCompanyCode(newGrHeader.getCompanyCode());
        dbGrHeader.setPlantId(newGrHeader.getPlantId());
        dbGrHeader.setDeletionIndicator(0L);
        dbGrHeader.setCreatedBy(loginUserID);
        dbGrHeader.setUpdatedBy(loginUserID);
        dbGrHeader.setCreatedOn(new Date());
        dbGrHeader.setUpdatedOn(new Date());
        return grHeaderRepository.save(dbGrHeader);
    }

    /**
     *
     * @param warehouseId
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param refDocNumber
     * @param stagingNo
     * @param goodsReceiptNo
     * @param palletCode
     * @param caseCode
     * @param loginUserID
     * @param updateGrHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public GrHeader updateGrHeader(String warehouseId,String companyCode, String plantId, String languageId,
                                   String preInboundNo, String refDocNumber, String stagingNo, String goodsReceiptNo,
                                   String palletCode, String caseCode, String loginUserID, UpdateGrHeader updateGrHeader)
            throws IllegalAccessException, InvocationTargetException {
        GrHeader dbGrHeader = getGrHeader(warehouseId,companyCode,plantId,languageId, preInboundNo, refDocNumber,
                stagingNo, goodsReceiptNo, palletCode, caseCode);
        BeanUtils.copyProperties(updateGrHeader, dbGrHeader, CommonUtils.getNullPropertyNames(updateGrHeader));
        dbGrHeader.setUpdatedBy(loginUserID);
        dbGrHeader.setUpdatedOn(new Date());
        return grHeaderRepository.save(dbGrHeader);
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param statusId
     * @param loginUserID
     */
    public void updateGrHeader(String warehouseId, String companyCode, String plantId,
                               String languageId, String preInboundNo, String refDocNumber, Long lineNo,
                               String itemCode, Long statusId, String loginUserID) {
        List<GrHeader> grHeaderList = getGrHeader(warehouseId,companyCode,plantId,languageId, preInboundNo, refDocNumber);
        for (GrHeader dbGrHeader : grHeaderList) {
            dbGrHeader.setStatusId(statusId);
            dbGrHeader.setUpdatedBy(loginUserID);
            dbGrHeader.setUpdatedOn(new Date());
            grHeaderRepository.save(dbGrHeader);
        }

        // Line
        List<GrLine> grLineList = grLineService.getGrLineForUpdate(warehouseId,companyCode,plantId,languageId, preInboundNo, refDocNumber, lineNo, itemCode);
        for (GrLine grLine : grLineList) {
            grLine.setStatusId(statusId);
            grLine.setUpdatedBy(loginUserID);
            grLine.setUpdatedOn(new Date());
            grLineRepository.save(grLine);
        }
        log.info("GRHeader & Line updated..");
    }

    /**
     * Pass the selected REF_DOC_NO/PACK_BARCODE along with REF_DOC_NO/PRE_IB_NO/WH_ID/CASE_CODE values in GRLINE and GRHEADER tables
     * and update Status ID as 16
     *
     * @param refDocNumber
     * @param packBarcodes
     * @param warehouseId
     * @param preInboundNo
     * @param caseCode
     * @param loginUserID
     */
    public void updateGrHeader(String refDocNumber,String companyCode, String plantId,
                               String languageId, String packBarcodes, String warehouseId, String preInboundNo,
                               String caseCode, String loginUserID) {
        List<GrHeader> grHeaderList = getGrHeaderForReverse(refDocNumber, warehouseId,companyCode,plantId,languageId, preInboundNo, caseCode);
        for (GrHeader dbGrHeader : grHeaderList) {
            dbGrHeader.setStatusId(16L);
            dbGrHeader.setUpdatedBy(loginUserID);
            dbGrHeader.setUpdatedOn(new Date());
            dbGrHeader = grHeaderRepository.save(dbGrHeader);
            log.info("dbGrHeader updated : " + dbGrHeader);
        }

        List<GrLine> grLineList = grLineService.getGrLine(refDocNumber, packBarcodes, warehouseId, preInboundNo, caseCode);
        for (GrLine dbGrLine : grLineList) {
            dbGrLine.setStatusId(16L);
            dbGrLine.setUpdatedBy(loginUserID);
            dbGrLine.setUpdatedOn(new Date());
            dbGrLine = grLineRepository.save(dbGrLine);
            log.info("dbGrLine updated : " + dbGrLine);
        }
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<GrHeader> grHeaders = getGrHeaders();
        grHeaders.forEach(g -> g.setReferenceField1(asnNumber));
        grHeaderRepository.saveAll(grHeaders);
    }

    /**
     * deleteGrHeader
     *
     * @param loginUserID
     * @param goodsReceiptNo
     */
    public void deleteGrHeader(String warehouseId,String companyCode, String plantId,
                               String languageId, String preInboundNo, String refDocNumber,
                               String stagingNo, String goodsReceiptNo, String palletCode, String caseCode, String loginUserID) {
        GrHeader grHeader = getGrHeader(warehouseId,companyCode,plantId,languageId, preInboundNo, refDocNumber, stagingNo, goodsReceiptNo,
                palletCode, caseCode);
        if (grHeader != null) {
            grHeader.setDeletionIndicator(1L);
            grHeader.setUpdatedBy(loginUserID);
            grHeaderRepository.save(grHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + goodsReceiptNo);
        }
    }
}
