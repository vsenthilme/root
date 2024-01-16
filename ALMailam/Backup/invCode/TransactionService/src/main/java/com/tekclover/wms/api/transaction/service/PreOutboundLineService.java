package com.tekclover.wms.api.transaction.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.PreOutboundLineV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.SearchPreOutboundLineV2;
import com.tekclover.wms.api.transaction.repository.*;
import com.tekclover.wms.api.transaction.repository.specification.PreOutboundLineV2Specification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.AddPreOutboundLine;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.PreOutboundLine;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.SearchPreOutboundLine;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.UpdatePreOutboundLine;
import com.tekclover.wms.api.transaction.repository.specification.PreOutboundLineSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class PreOutboundLineService extends BaseService {

    @Autowired
    private PreOutboundLineRepository preOutboundLineRepository;

    //----------------------------------------------------------------------------------------
    @Autowired
    private PreOutboundLineV2Repository preOutboundLineV2Repository;
    @Autowired
    private StagingLineV2Repository stagingLineV2Repository;

    String statusDescription = null;
    //----------------------------------------------------------------------------------------

    /**
     * getPreOutboundLines
     *
     * @return
     */
    public List<PreOutboundLine> getPreOutboundLines() {
        List<PreOutboundLine> preOutboundLineList = preOutboundLineRepository.findAll();
        preOutboundLineList = preOutboundLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preOutboundLineList;
    }

    /**
     * getPreOutboundLine
     *
     * @param lineNumber
     * @return
     */
    public PreOutboundLine getPreOutboundLine(String languageId, String companyCodeId, String plantId, String warehouseId,
                                              String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode) {
        Optional<PreOutboundLine> preOutboundLine =
                preOutboundLineRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, preOutboundNo, partnerCode,
                        lineNumber, itemCode, 0L);
        if (!preOutboundLine.isEmpty()) {
            return preOutboundLine.get();
        }

        return null;
    }

    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @return
     */
    public List<PreOutboundLine> getPreOutboundLine(String languageId, String companyCodeId, String plantId, String warehouseId,
                                                    String refDocNumber, String preOutboundNo, String partnerCode) {
        List<PreOutboundLine> preOutboundLine =
                preOutboundLineRepository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, preOutboundNo, partnerCode, 0L);
        if (!preOutboundLine.isEmpty()) {
            return preOutboundLine;
        }

        return null;
    }

    /**
     * @param preOutboundNo
     * @return
     */
    private List<PreOutboundLine> getPreOutboundLine(String preOutboundNo) {
        List<PreOutboundLine> preOutboundLine = preOutboundLineRepository.findByPreOutboundNo(preOutboundNo);
        if (preOutboundLine != null) {
            return preOutboundLine;
        } else {
            throw new BadRequestException("The given PreOutboundLine ID : " + preOutboundNo + " doesn't exist.");
        }
    }


    /**
     * @param searchPreOutboundLine
     * @return
     * @throws ParseException
     */
    public List<PreOutboundLine> findPreOutboundLine(SearchPreOutboundLine searchPreOutboundLine)
            throws ParseException {
        PreOutboundLineSpecification spec = new PreOutboundLineSpecification(searchPreOutboundLine);
        List<PreOutboundLine> results = preOutboundLineRepository.findAll(spec);
        return results;
    }

    /**
     * createPreOutboundLine
     *
     * @param newPreOutboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundLine createPreOutboundLine(AddPreOutboundLine newPreOutboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundLine dbPreOutboundLine = new PreOutboundLine();
        log.info("newPreOutboundLine : " + newPreOutboundLine);
        BeanUtils.copyProperties(newPreOutboundLine, dbPreOutboundLine);
        dbPreOutboundLine.setDeletionIndicator(0L);
        dbPreOutboundLine.setCreatedBy(loginUserID);
        dbPreOutboundLine.setUpdatedBy(loginUserID);
        dbPreOutboundLine.setCreatedOn(new Date());
        dbPreOutboundLine.setUpdatedOn(new Date());
        return preOutboundLineRepository.save(dbPreOutboundLine);
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updatePreOutboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundLine updatePreOutboundLine(String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode,
                                                 String loginUserID, UpdatePreOutboundLine updatePreOutboundLine)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundLine dbPreOutboundLine = getPreOutboundLine(getLanguageId(), getCompanyCode(), getPlantId(), warehouseId,
                refDocNumber, preOutboundNo, partnerCode, lineNumber, itemCode);
        if (dbPreOutboundLine != null) {
            BeanUtils.copyProperties(updatePreOutboundLine, dbPreOutboundLine, CommonUtils.getNullPropertyNames(updatePreOutboundLine));
            dbPreOutboundLine.setUpdatedBy(loginUserID);
            dbPreOutboundLine.setUpdatedOn(new Date());
            return preOutboundLineRepository.save(dbPreOutboundLine);
        }
        return null;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param loginUserID
     * @param updatePreOutboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundLine updatePreOutboundLine(String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, String loginUserID,
                                                 UpdatePreOutboundLine updatePreOutboundLine)
            throws IllegalAccessException, InvocationTargetException {
        List<PreOutboundLine> dbPreOutboundLines = getPreOutboundLine(getLanguageId(), getCompanyCode(), getPlantId(), warehouseId, refDocNumber, preOutboundNo, partnerCode);
        for (PreOutboundLine dbPreOutboundLine : dbPreOutboundLines) {
            BeanUtils.copyProperties(updatePreOutboundLine, dbPreOutboundLine, CommonUtils.getNullPropertyNames(updatePreOutboundLine));
            dbPreOutboundLine.setUpdatedBy(loginUserID);
            dbPreOutboundLine.setUpdatedOn(new Date());
            dbPreOutboundLine = preOutboundLineRepository.save(dbPreOutboundLine);
        }
        return null;
    }


    /**
     * deletePreOutboundLine
     * @param loginUserID
     * @param lineNumber
     */
//	public void deletePreOutboundLine (String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode, String loginUserID) {
//		PreOutboundLine preOutboundLine = getPreOutboundLine(lineNumber);
//		if ( preOutboundLine != null) {
//			preOutboundLine.setDeletionIndicator(1L);
//			preOutboundLine.setUpdatedBy(loginUserID);
//			preOutboundLineRepository.save(preOutboundLine);
//		} else {
//			throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
//		}
//	}

//================================================================V2=================================================================================

    /**
     * getPreOutboundLines
     *
     * @return
     */
    public List<PreOutboundLineV2> getPreOutboundLinesV2() {
        List<PreOutboundLineV2> preOutboundLineList = preOutboundLineV2Repository.findAll();
        preOutboundLineList = preOutboundLineList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return preOutboundLineList;
    }

    /**
     * getPreOutboundLine
     *
     * @param lineNumber
     * @return
     */
    public PreOutboundLineV2 getPreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                  String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode) {
        Optional<PreOutboundLineV2> preOutboundLine =
                preOutboundLineV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, preOutboundNo, partnerCode,
                        lineNumber, itemCode, 0L);
        if (!preOutboundLine.isEmpty()) {
            return preOutboundLine.get();
        }

        return null;
    }

    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @return
     */
    public List<PreOutboundLineV2> getPreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                        String refDocNumber, String preOutboundNo, String partnerCode) {
        List<PreOutboundLineV2> preOutboundLine =
                preOutboundLineV2Repository.findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
                        languageId, companyCodeId, plantId, warehouseId, refDocNumber, preOutboundNo, partnerCode, 0L);
        if (!preOutboundLine.isEmpty()) {
            return preOutboundLine;
        }

        return null;
    }

    /**
     * @param preOutboundNo
     * @return
     */
    private List<PreOutboundLineV2> getPreOutboundLineV2(String preOutboundNo) {
        List<PreOutboundLineV2> preOutboundLine = preOutboundLineV2Repository.findByPreOutboundNo(preOutboundNo);
        if (preOutboundLine != null) {
            return preOutboundLine;
        } else {
            throw new BadRequestException("The given PreOutboundLine ID : " + preOutboundNo + " doesn't exist.");
        }
    }


    /**
     * @param searchPreOutboundLine
     * @return
     * @throws ParseException
     */
    public Stream<PreOutboundLineV2> findPreOutboundLineV2(SearchPreOutboundLineV2 searchPreOutboundLine)
            throws ParseException {
        PreOutboundLineV2Specification spec = new PreOutboundLineV2Specification(searchPreOutboundLine);
        Stream<PreOutboundLineV2> results = preOutboundLineV2Repository.stream(spec, PreOutboundLineV2.class);
        return results;
    }

    /**
     * createPreOutboundLine
     *
     * @param newPreOutboundLine
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundLineV2 createPreOutboundLineV2(PreOutboundLineV2 newPreOutboundLine, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundLineV2 dbPreOutboundLine = new PreOutboundLineV2();
        log.info("newPreOutboundLine : " + newPreOutboundLine);
        BeanUtils.copyProperties(newPreOutboundLine, dbPreOutboundLine);
        dbPreOutboundLine.setDeletionIndicator(0L);
        dbPreOutboundLine.setCreatedBy(loginUserID);
        dbPreOutboundLine.setUpdatedBy(loginUserID);
        dbPreOutboundLine.setCreatedOn(new Date());
        dbPreOutboundLine.setUpdatedOn(new Date());
        return preOutboundLineV2Repository.save(dbPreOutboundLine);
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param loginUserID
     * @param updatePreOutboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PreOutboundLineV2 updatePreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                     String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode,
                                                     String loginUserID, UpdatePreOutboundLine updatePreOutboundLine)
            throws IllegalAccessException, InvocationTargetException {
        PreOutboundLineV2 dbPreOutboundLine = getPreOutboundLineV2(companyCodeId, plantId, languageId, warehouseId,
                refDocNumber, preOutboundNo, partnerCode, lineNumber, itemCode);
        if (dbPreOutboundLine != null) {
            BeanUtils.copyProperties(updatePreOutboundLine, dbPreOutboundLine, CommonUtils.getNullPropertyNames(updatePreOutboundLine));
            dbPreOutboundLine.setUpdatedBy(loginUserID);
            dbPreOutboundLine.setUpdatedOn(new Date());
            return preOutboundLineV2Repository.save(dbPreOutboundLine);
        }
        return dbPreOutboundLine;
    }

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param loginUserID
     * @param updatePreOutboundLine
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PreOutboundLineV2> updatePreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId,
                                                           String refDocNumber, String preOutboundNo, String partnerCode, String loginUserID,
                                                           PreOutboundLineV2 updatePreOutboundLine)
            throws IllegalAccessException, InvocationTargetException {
        List<PreOutboundLineV2> dbPreOutboundLines = getPreOutboundLineV2(companyCodeId, plantId, languageId, warehouseId, refDocNumber, preOutboundNo, partnerCode);
        List<PreOutboundLineV2> updatedPreOutboundLine = new ArrayList<>();
        for (PreOutboundLineV2 dbPreOutboundLine : dbPreOutboundLines) {
            BeanUtils.copyProperties(updatePreOutboundLine, dbPreOutboundLine, CommonUtils.getNullPropertyNames(updatePreOutboundLine));
            dbPreOutboundLine.setUpdatedBy(loginUserID);
            dbPreOutboundLine.setUpdatedOn(new Date());
            dbPreOutboundLine = preOutboundLineV2Repository.save(dbPreOutboundLine);
            updatedPreOutboundLine.add(dbPreOutboundLine);
        }
        return updatedPreOutboundLine;
    }


    /**
     * deletePreOutboundLine
     *
     * @param loginUserID
     * @param lineNumber
     */
    public void deletePreOutboundLineV2(String companyCodeId, String plantId, String languageId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode, Long lineNumber, String itemCode, String loginUserID) {
        PreOutboundLineV2 preOutboundLine = getPreOutboundLineV2(companyCodeId, plantId, languageId, warehouseId, refDocNumber, preOutboundNo, partnerCode, lineNumber, itemCode);
        if (preOutboundLine != null) {
            preOutboundLine.setDeletionIndicator(1L);
            preOutboundLine.setUpdatedBy(loginUserID);
            preOutboundLineV2Repository.save(preOutboundLine);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + lineNumber);
        }
    }
}
