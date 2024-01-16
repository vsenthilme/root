package com.tekclover.wms.api.transaction.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekclover.wms.api.transaction.model.inbound.staging.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.auth.AuthToken;
import com.tekclover.wms.api.transaction.repository.StagingHeaderRepository;
import com.tekclover.wms.api.transaction.repository.StagingLineRepository;
import com.tekclover.wms.api.transaction.repository.specification.StagingHeaderSpecification;
import com.tekclover.wms.api.transaction.util.CommonUtils;
import com.tekclover.wms.api.transaction.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Service
public class StagingHeaderService extends BaseService {

    @Autowired
    private StagingHeaderRepository stagingHeaderRepository;

    @Autowired
    private StagingLineRepository stagingLineRepository;

    @Autowired
    private StagingLineService stagingLineService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * getStagingHeaders
     *
     * @return
     */
    public List<StagingHeader> getStagingHeaders() {
        List<StagingHeader> stagingHeaderList = stagingHeaderRepository.findAll();
        stagingHeaderList = stagingHeaderList.stream()
                .filter(n -> n.getDeletionIndicator() != null &&
                        n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return stagingHeaderList;
    }

    /**
     * getStagingHeader
     *
     * @param stagingNo
     * @return
     */
    public StagingHeader getStagingHeader(String warehouseId, String preInboundNo, String companyCode, String plantId,
                                          String languageId, String refDocNumber, String stagingNo) {
        log.info("Staging Header value : " + languageId + "," + companyCode
                + "," + plantId + "," + warehouseId + "," + refDocNumber + "," + preInboundNo + "," + stagingNo);

        Optional<StagingHeader> stagingHeader =
                stagingHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndStagingNoAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        stagingNo,
                        0L);
        log.info("stagingHeader : " + stagingHeader);
        if (stagingHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    ",stagingNo: " + stagingNo + " doesn't exist.");
        }

        return stagingHeader.get();
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @return
     */
    public List<StagingHeader> getStagingHeader(String warehouseId, String companyCode, String plantId,
                                                String languageId, String preInboundNo, String refDocNumber) {
        List<StagingHeader> stagingHeader =
                stagingHeaderRepository.findByLanguageIdAndCompanyCodeAndPlantIdAndWarehouseIdAndPreInboundNoAndRefDocNumberAndDeletionIndicator(
                        languageId,
                        companyCode,
                        plantId,
                        warehouseId,
                        preInboundNo,
                        refDocNumber,
                        0L);
        log.info("stagingHeader : " + stagingHeader);
        if (stagingHeader.isEmpty()) {
            throw new BadRequestException("The given values: warehouseId:" + warehouseId +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",refDocNumber: " + refDocNumber +
                    ",preInboundNo: " + preInboundNo +
                    " doesn't exist.");
        }
        return stagingHeader;
    }

    /**
     * @param numberOfCases
     * @param warehouseId
     * @param companyCode
     * @param languageId
     * @param plantId
     * @return
     */
    public List<String> generateNumberRanges(Long numberOfCases, String warehouseId, String companyCode, String languageId, String plantId) {
        AuthToken authTokenForIDMasterService = authTokenService.getIDMasterServiceAuthToken();
        List<String> numberRanges = new ArrayList<>();
        for (int i = 0; i < numberOfCases; i++) {
            String nextRangeNumber = getNextRangeNumber(4, warehouseId, companyCode, plantId, languageId, authTokenForIDMasterService.getAccess_token());
            numberRanges.add(nextRangeNumber);
        }
        return numberRanges;
    }

    /**
     * @param warehouseId
     * @return
     */
    public List<StagingHeader> getStagingHeaderCount(String warehouseId) {
        return stagingHeaderRepository.findByWarehouseIdAndStatusIdAndDeletionIndicator(warehouseId, 12L, 0L);
    }

    /**
     * @param searchStagingHeader
     * @return
     */
    public List<StagingHeader> findStagingHeader(SearchStagingHeader searchStagingHeader)
            throws Exception {
        if (searchStagingHeader.getStartCreatedOn() != null && searchStagingHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStagingHeader.getStartCreatedOn(), searchStagingHeader.getEndCreatedOn());
            searchStagingHeader.setStartCreatedOn(dates[0]);
            searchStagingHeader.setEndCreatedOn(dates[1]);
        }

        StagingHeaderSpecification spec = new StagingHeaderSpecification(searchStagingHeader);
        List<StagingHeader> results = stagingHeaderRepository.findAll(spec);
//		log.info("results: " + results);
        return results;
    }

    /**
     * @param searchStagingHeader
     * @return
     * @throws Exception
     */
    //Streaming
    public Stream<StagingHeader> findStagingHeaderNew(SearchStagingHeader searchStagingHeader)
            throws Exception {
        if (searchStagingHeader.getStartCreatedOn() != null && searchStagingHeader.getStartCreatedOn() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchStagingHeader.getStartCreatedOn(), searchStagingHeader.getEndCreatedOn());
            searchStagingHeader.setStartCreatedOn(dates[0]);
            searchStagingHeader.setEndCreatedOn(dates[1]);
        }

        StagingHeaderSpecification spec = new StagingHeaderSpecification(searchStagingHeader);
        Stream<StagingHeader> results = stagingHeaderRepository.stream(spec, StagingHeader.class);

        return results;
    }

    /**
     * createStagingHeader
     *
     * @param newStagingHeader
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StagingHeader createStagingHeader(AddStagingHeader newStagingHeader, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StagingHeader dbStagingHeader = new StagingHeader();
        log.info("newStagingHeader : " + newStagingHeader);
        BeanUtils.copyProperties(newStagingHeader, dbStagingHeader, CommonUtils.getNullPropertyNames(newStagingHeader));
        dbStagingHeader.setDeletionIndicator(0L);
        dbStagingHeader.setCreatedBy(loginUserID);
        dbStagingHeader.setUpdatedBy(loginUserID);
        dbStagingHeader.setCreatedOn(new Date());
        dbStagingHeader.setUpdatedOn(new Date());
        return stagingHeaderRepository.save(dbStagingHeader);
    }

    /**
     * updateStagingHeader
     *
     * @param loginUserID
     * @param stagingNo
     * @param updateStagingHeader
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StagingHeader updateStagingHeader(String warehouseId, String companyCode, String plantId,
                                             String languageId, String preInboundNo, String refDocNumber, String stagingNo,
                                             String loginUserID, UpdateStagingHeader updateStagingHeader)
            throws IllegalAccessException, InvocationTargetException {
        StagingHeader dbStagingHeader = getStagingHeader(warehouseId, preInboundNo, companyCode, plantId, languageId, refDocNumber, stagingNo);
        BeanUtils.copyProperties(updateStagingHeader, dbStagingHeader, CommonUtils.getNullPropertyNames(updateStagingHeader));
        dbStagingHeader.setUpdatedBy(loginUserID);
        dbStagingHeader.setUpdatedOn(new Date());
        return stagingHeaderRepository.save(dbStagingHeader);
    }

    /**
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param statusId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void updateStagingHeader(String warehouseId, String companyCode, String plantId,
                                    String languageId, String preInboundNo, String refDocNumber, Long lineNo,
                                    String itemCode, Long statusId, String loginUserID) throws IllegalAccessException, InvocationTargetException {
        List<StagingHeader> dbStagingHeaderList = getStagingHeader(warehouseId, companyCode, plantId, languageId, preInboundNo, refDocNumber);
        for (StagingHeader dbStagingHeader : dbStagingHeaderList) {
            dbStagingHeader.setStatusId(statusId);
            dbStagingHeader.setUpdatedBy(loginUserID);
            dbStagingHeader.setUpdatedOn(new Date());
            dbStagingHeader = stagingHeaderRepository.save(dbStagingHeader);
            log.info("dbStagingHeader : " + dbStagingHeader);
        }

        // Line
        List<StagingLineEntity> stagingLineEntityList = stagingLineService.getStagingLine(warehouseId, companyCode, plantId, languageId, refDocNumber, preInboundNo, lineNo, itemCode);
        for (StagingLineEntity stagingLineEntity : stagingLineEntityList) {
            stagingLineEntity.setStatusId(statusId);
            stagingLineEntity.setUpdatedBy(loginUserID);
            stagingLineEntity.setUpdatedOn(new Date());
            stagingLineEntity = stagingLineRepository.save(stagingLineEntity);
            log.info("stagingLineEntity : " + stagingLineEntity);
        }
        log.info("GRHeader & Line updated..");
    }

    /**
     * @param asnNumber
     */
    public void updateASN(String asnNumber) {
        List<StagingHeader> stagingHeaders = getStagingHeaders();
        stagingHeaders.forEach(stagHeaders -> stagHeaders.setReferenceField1(asnNumber));
        stagingHeaderRepository.saveAll(stagingHeaders);
    }

    /**
     * @param warehouseId
     * @param companyCode
     * @param plantId
     * @param languageId
     * @param preInboundNo
     * @param refDocNumber
     * @param stagingNo
     * @param loginUserID
     */
    public void deleteStagingHeader(String warehouseId, String companyCode, String plantId,
                                    String languageId, String preInboundNo, String refDocNumber, String stagingNo, String loginUserID) {
        StagingHeader stagingHeader = getStagingHeader(warehouseId, preInboundNo, companyCode, plantId, languageId, refDocNumber, stagingNo);
        if (stagingHeader != null) {
            stagingHeader.setDeletionIndicator(1L);
            stagingHeader.setUpdatedBy(loginUserID);
            stagingHeaderRepository.save(stagingHeader);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: warehouseId: " + warehouseId +
                    ",companyCodeId: " + companyCode +
                    ",languageId: " + languageId +
                    ",plantId: " + plantId +
                    ",refDocNumber: " + refDocNumber + "," +
                    ",preInboundNo: " + preInboundNo + "," +
                    ",stagingNo: " + stagingNo + " doesn't exist.");
        }
    }
    //-------------------------------------------Streaming-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamStagingHeader() {
        Stream<StagingHeaderStream> stagingHeaderStream = streamStagingHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                stagingHeaderStream.forEach(im -> {
                    try {
                        jsonGenerator.writeObject(im);
                    } catch (IOException exception) {
                        log.error("exception occurred while writing object to stream", exception);
                    }
                });
                jsonGenerator.writeEndArray();
                jsonGenerator.close();
            } catch (Exception e) {
                log.info("Exception occurred while publishing data", e);
                e.printStackTrace();
            }
            log.info("finished streaming records");
        };
        return responseBody;
    }
    // =======================================JDBCTemplate=======================================================

//	private final Gson gson = new Gson();

    /**
     * preInboundNo
     * refDocNumber
     * stagingNo
     * inboundOrderTypeId
     * statusId
     * createdBy
     * createdOn
     *
     * @return
     */
    public Stream<StagingHeaderStream> streamStagingHeader() {
        jdbcTemplate.setFetchSize(50);
		/*

		 * ----------------
		 * 	String preInboundNo;
			String refDocNumber;
			String stagingNo;
			Long inboundOrderTypeId;
			Long statusId;
			String createdBy;
			Date createdOn;
		 */
        Stream<StagingHeaderStream> stagingHeaderStream = jdbcTemplate.queryForStream(
                "Select PRE_IB_NO, REF_DOC_NO, STG_NO, IB_ORD_TYP_ID, STATUS_ID, ST_CTD_BY, "
                        + "ST_CTD_ON "
                        + "from tblstagingheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new StagingHeaderStream(
                        resultSet.getString("PRE_IB_NO"),
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("STG_NO"),
                        resultSet.getLong("IB_ORD_TYP_ID"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("ST_CTD_BY"),
                        resultSet.getDate("ST_CTD_ON")
                ));
        return stagingHeaderStream;
    }
}
