package com.tekclover.wms.core.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.tekclover.wms.core.model.warehouse.inbound.*;
import com.tekclover.wms.core.model.warehouse.outbound.ShipmentOrder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
//import com.google.gson.Gson;
import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.masters.ImBasicData1;
import com.tekclover.wms.core.model.masters.ImBasicData1Stream;
import com.tekclover.wms.core.model.masters.SearchImBasicData1;
import com.tekclover.wms.core.model.masters.StorageBinStream;
import com.tekclover.wms.core.model.transaction.*;
import com.tekclover.wms.core.repository.MongoTransactionRepository;
import com.tekclover.wms.core.util.CommonUtils;
import com.tekclover.wms.core.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    MongoTransactionRepository mongoTransactionRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate jdbcParamTemplate;

    /**
     * @return
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    /**
     * @return
     */
    private String getTransactionServiceApiUrl() {
        return propertiesConfig.getTransactionServiceUrl();
    }

    /*------------------------------ProcessInboundReceived-----------------------------------------------------------------*/
    // POST
    public PreInboundHeader processInboundReceived(String authToken) {
        InboundIntegrationHeader createdInboundIntegrationHeader = mongoTransactionRepository
                .findTopByOrderByOrderReceivedOnDesc();
        log.info("Latest InboundIntegrationHeader : " + createdInboundIntegrationHeader);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl()
                + "preinboundheader/" + createdInboundIntegrationHeader.getRefDocumentNo() + "/processInboundReceived");
        HttpEntity<?> entity = new HttpEntity<>(createdInboundIntegrationHeader, headers);
        ResponseEntity<PreInboundHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PreInboundHeader.class);
        return result.getBody();
    }

    // --------------------------------------------PreInboundHeader------------------------------------------------------------------------
    // GET ALL
    public PreInboundHeader[] getPreInboundHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, PreInboundHeader[].class);

//			return result.getBody();
            //Adding time to Date
            List<PreInboundHeader> obList = new ArrayList<>();
            for (PreInboundHeader preInboundHeader : result.getBody()) {

                obList.add(addingTimeWithDate(preInboundHeader));

            }
            return obList.toArray(new PreInboundHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public PreInboundHeader getPreInboundHeader(String preInboundNo, String warehouseId, String companyCode, String plantId, String languageId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PreInboundHeader.class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();
            return addingTimeWithDate(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public StagingHeader processASN(List<PreInboundLine> newPreInboundLine, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/processASN")
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(newPreInboundLine, headers);
            ResponseEntity<StagingHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, StagingHeader.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public PreInboundHeader[] getPreInboundHeaderWithStatusId(String warehouseId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/inboundconfirm")
                    .queryParam("warehouseId", warehouseId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, PreInboundHeader[].class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();

            //Adding time to Date
            List<PreInboundHeader> obList = new ArrayList<>();
            for (PreInboundHeader preInboundHeader : result.getBody()) {

                obList.add(addingTimeWithDate(preInboundHeader));

            }
            return obList.toArray(new PreInboundHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public PreInboundHeader createPreInboundHeader(PreInboundHeader newPreInboundHeader, String loginUserID,
                                                   String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPreInboundHeader, headers);
        ResponseEntity<PreInboundHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PreInboundHeader.class);
//		log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - findPreInboundHeader
    public PreInboundHeader[] findPreInboundHeader(SearchPreInboundHeader searchPreInboundHeader, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/findPreInboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPreInboundHeader, headers);
            ResponseEntity<PreInboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PreInboundHeader[].class);
            List<PreInboundHeader> obList = new ArrayList<>();
            for (PreInboundHeader preInboundHeader : result.getBody()) {

                obList.add(addingTimeWithDate(preInboundHeader));

            }
            return obList.toArray(new PreInboundHeader[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findPreInboundHeader - Stream
    public PreInboundHeader[] findPreInboundHeaderNew(SearchPreInboundHeader searchPreInboundHeader, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/findPreInboundHeaderNew");
            HttpEntity<?> entity = new HttpEntity<>(searchPreInboundHeader, headers);
            ResponseEntity<PreInboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PreInboundHeader[].class);
            List<PreInboundHeader> obList = new ArrayList<>();
            for (PreInboundHeader preInboundHeader : result.getBody()) {

                obList.add(addingTimeWithDate(preInboundHeader));

            }
            return obList.toArray(new PreInboundHeader[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PreInboundHeader addingTimeWithDate(PreInboundHeader preInboundHeader) throws ParseException {

        if (preInboundHeader.getRefDocDate() != null) {
            preInboundHeader.setRefDocDate(DateUtils.addTimeToDate(preInboundHeader.getRefDocDate(), 3));
        }

        if (preInboundHeader.getCreatedOn() != null) {
            preInboundHeader.setCreatedOn(DateUtils.addTimeToDate(preInboundHeader.getCreatedOn(), 3));
        }

        if (preInboundHeader.getUpdatedOn() != null) {
            preInboundHeader.setUpdatedOn(DateUtils.addTimeToDate(preInboundHeader.getUpdatedOn(), 3));
        }

        return preInboundHeader;
    }

    // PATCH
    public PreInboundHeader updatePreInboundHeader(String preInboundNo, String companyCode, String plantId, String languageId, String warehouseId, String loginUserID,
                                                   PreInboundHeader modifiedPreInboundHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedPreInboundHeader, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PreInboundHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PreInboundHeader.class);
//			log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePreInboundHeader(String preInboundNo, String warehouseId, String companyCode, String plantId, String languageId, String loginUserID,
                                          String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundheader/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("languageId", languageId)
                    .queryParam("plantId", plantId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
//			log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------PreInboundLine------------------------------------------------------------------------
    // GET ALL
    public PreInboundLine[] getPreInboundLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PreInboundLine[].class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();

            List<PreInboundLine> obList = new ArrayList<>();
            for (PreInboundLine preInboundLine : result.getBody()) {

                obList.add(addingTimeWithDatePreInboundLine(preInboundLine));

            }
            return obList.toArray(new PreInboundLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PreInboundLine addingTimeWithDatePreInboundLine(PreInboundLine preInboundLine) throws ParseException {

        if (preInboundLine.getExpectedArrivalDate() != null) {
            preInboundLine.setExpectedArrivalDate(DateUtils.addTimeToDate(preInboundLine.getExpectedArrivalDate(), 3));
        }

        if (preInboundLine.getCreatedOn() != null) {
            preInboundLine.setCreatedOn(DateUtils.addTimeToDate(preInboundLine.getCreatedOn(), 3));
        }

        if (preInboundLine.getUpdatedOn() != null) {
            preInboundLine.setUpdatedOn(DateUtils.addTimeToDate(preInboundLine.getUpdatedOn(), 3));
        }

        return preInboundLine;
    }

    // GET
    public PreInboundLine getPreInboundLine(String preInboundNo, String warehouseId, String refDocNumber, Long lineNo,
                                            String itemCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("lineNo", lineNo).queryParam("itemCode", itemCode);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PreInboundLine.class);
//			log.info("result : " + result.getStatusCode());
            return addingTimeWithDatePreInboundLine(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public PreInboundLine[] getPreInboundLine(String preInboundNo, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline/" + preInboundNo);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PreInboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PreInboundLine[].class);
            log.info("result : " + result.getStatusCode());

            List<PreInboundLine> obList = new ArrayList<>();
            for (PreInboundLine preInboundLine : result.getBody()) {

                obList.add(addingTimeWithDatePreInboundLine(preInboundLine));

            }
            return obList.toArray(new PreInboundLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public PreInboundLine createPreInboundLine(PreInboundLine newPreInboundLine, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline").queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPreInboundLine, headers);
        ResponseEntity<PreInboundLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PreInboundLine.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // CREATE - BOM
    public PreInboundLine[] createPreInboundLineBOM(String preInboundNo, String warehouseId, String companyCode,
                                                    String plantId, String languageId, String refDocNumber,
                                                    String itemCode, Long lineNo, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline/bom")
                .queryParam("preInboundNo", preInboundNo)
                .queryParam("companyCode", companyCode)
                .queryParam("plantId", plantId)
                .queryParam("languageId", languageId)
                .queryParam("warehouseId", warehouseId)
                .queryParam("refDocNumber", refDocNumber)
                .queryParam("itemCode", itemCode)
                .queryParam("lineNo", lineNo)
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<PreInboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PreInboundLine[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public PreInboundLine updatePreInboundLine(String companyCode, String plantId, String warehouseId, String languageId,
                                               String preInboundNo, String refDocNumber, Long lineNo, String itemCode,
                                               String loginUserID, PreInboundLine updatePreInboundLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(updatePreInboundLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PreInboundLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PreInboundLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePreInboundLine(String preInboundNo, String warehouseId, String companyCode, String plantId, String languageId, String refDocNumber, Long lineNo,
                                        String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preinboundline/" + preInboundNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------ContainerReceipt------------------------------------------------------------------------
    // GET ALL
    public ContainerReceipt[] getContainerReceipts(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ContainerReceipt[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ContainerReceipt[].class);
            log.info("result : " + result.getStatusCode());
//			return result.getBody();

            List<ContainerReceipt> obList = new ArrayList<>();
            for (ContainerReceipt containerReceipt : result.getBody()) {

                obList.add(addingTimeWithDateContainerReceipt(containerReceipt));

            }
            return obList.toArray(new ContainerReceipt[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public ContainerReceipt addingTimeWithDateContainerReceipt(ContainerReceipt containerReceipt) throws ParseException {

        if (containerReceipt.getContainerReceivedDate() != null) {
            containerReceipt.setContainerReceivedDate(DateUtils.addTimeToDate(containerReceipt.getContainerReceivedDate(), 3));
        }

        return containerReceipt;
    }

    // GET
    public ContainerReceipt getContainerReceipt(String preInboundNo, String refDocNumber, String containerReceiptNo,
                                                String loginUserID, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/" + containerReceiptNo)
                    .queryParam("preInboundNo", preInboundNo).queryParam("refDocNumber", refDocNumber)
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ContainerReceipt> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, ContainerReceipt.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateContainerReceipt(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public ContainerReceipt getContainerReceipt(String preInboundNo, String languageId, String companyCodeId, String plantId,
                                                String refDocNumber, String containerReceiptNo, String warehouseId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/" + containerReceiptNo)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("warehouseId", warehouseId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ContainerReceipt> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, ContainerReceipt.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateContainerReceipt(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findContainerReceipt
    public ContainerReceipt[] findContainerReceipt(SearchContainerReceipt searchContainerReceipt, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/findContainerReceipt");
            HttpEntity<?> entity = new HttpEntity<>(searchContainerReceipt, headers);
            ResponseEntity<ContainerReceipt[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, ContainerReceipt[].class);

            List<ContainerReceipt> obList = new ArrayList<>();
            for (ContainerReceipt containerReceipt : result.getBody()) {
//				log.info("Result containerReceipt---getContainerReceivedDate() :" + containerReceipt.getContainerReceivedDate());

                obList.add(addingTimeWithDateContainerReceipt(containerReceipt));
            }
            return obList.toArray(new ContainerReceipt[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findContainerReceipt - Streaming
    public ContainerReceipt[] findContainerReceiptNew(SearchContainerReceipt searchContainerReceipt, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/findContainerReceiptNew");
            HttpEntity<?> entity = new HttpEntity<>(searchContainerReceipt, headers);
            ResponseEntity<ContainerReceipt[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, ContainerReceipt[].class);

            List<ContainerReceipt> obList = new ArrayList<>();
            for (ContainerReceipt containerReceipt : result.getBody()) {
//				log.info("Result containerReceipt---getContainerReceivedDate() :" + containerReceipt.getContainerReceivedDate());

                obList.add(addingTimeWithDateContainerReceipt(containerReceipt));
            }
            return obList.toArray(new ContainerReceipt[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public ContainerReceipt createContainerReceipt(ContainerReceipt newContainerReceipt, String loginUserID,
                                                   String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newContainerReceipt, headers);
        ResponseEntity<ContainerReceipt> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, ContainerReceipt.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public ContainerReceipt updateContainerReceipt(String preInboundNo, String containerReceiptNo, String companyCodeId,
                                                   String languageId, String plantId, String warehouseId, String refDocNumber,
                                                   String loginUserID, ContainerReceipt modifiedContainerReceipt, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedContainerReceipt, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/" + containerReceiptNo)
                    .queryParam("loginUserID", loginUserID)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("warehouseId", warehouseId);

            ResponseEntity<ContainerReceipt> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, ContainerReceipt.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteContainerReceipt(String preInboundNo, String refDocNumber, String containerReceiptNo, String companyCodeId,
                                          String languageId, String plantId, String warehouseId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "containerreceipt/" + containerReceiptNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("languageId", languageId)
                    .queryParam("plantId", plantId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------InboundHeader------------------------------------------------------------------------
    // GET ALL
    public InboundHeader[] getInboundHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<InboundHeader> obList = new ArrayList<>();
            for (InboundHeader inboundHeader : result.getBody()) {

                obList.add(addingTimeWithDateInboundHeader(inboundHeader));

            }
            return obList.toArray(new InboundHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public InboundHeader addingTimeWithDateInboundHeader(InboundHeader inboundHeader) throws ParseException {

        if (inboundHeader.getCreatedOn() != null) {
            inboundHeader.setCreatedOn(DateUtils.addTimeToDate(inboundHeader.getCreatedOn(), 3));
        }

        if (inboundHeader.getConfirmedOn() != null) {
            inboundHeader.setConfirmedOn(DateUtils.addTimeToDate(inboundHeader.getConfirmedOn(), 3));
        }

        return inboundHeader;
    }

    // GET
    public InboundHeader getInboundHeader(String warehouseId, String companyCode, String plantId, String languageId,
                                          String refDocNumber, String preInboundNo, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/" + refDocNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundHeader.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateInboundHeader(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - Finder
    public InboundHeader[] findInboundHeader(SearchInboundHeader searchInboundHeader, String authToken)
            throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/findInboundHeader");
        HttpEntity<?> entity = new HttpEntity<>(searchInboundHeader, headers);
        ResponseEntity<InboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InboundHeader[].class);

        List<InboundHeader> inboundHeaderList = new ArrayList<>();
        for (InboundHeader inboundHeader : result.getBody()) {

            inboundHeaderList.add(addingTimeWithDateInboundHeader(inboundHeader));
        }
        return inboundHeaderList.toArray(new InboundHeader[inboundHeaderList.size()]);
    }

    // Find - findInboundHeader - Stream
    public InboundHeader[] findInboundHeaderNew(SearchInboundHeader searchInboundHeader, String authToken)
            throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/findInboundHeaderNew");
        HttpEntity<?> entity = new HttpEntity<>(searchInboundHeader, headers);
        ResponseEntity<InboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InboundHeader[].class);

        List<InboundHeader> inboundHeaderList = new ArrayList<>();
        for (InboundHeader inboundHeader : result.getBody()) {

            inboundHeaderList.add(addingTimeWithDateInboundHeader(inboundHeader));
        }
        return inboundHeaderList.toArray(new InboundHeader[inboundHeaderList.size()]);
    }

    // GET
    public InboundHeaderEntity[] getInboundHeaderWithStatusId(String warehouseId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/inboundconfirm")
                    .queryParam("warehouseId", warehouseId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundHeaderEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InboundHeaderEntity[].class);
            log.info("result : " + result.getStatusCode());
            List<InboundHeaderEntity> inboundHeaderList = new ArrayList<>();
            for (InboundHeaderEntity inboundHeader : result.getBody()) {

                if (inboundHeader.getCreatedOn() != null) {
                    inboundHeader.setCreatedOn(DateUtils.addTimeToDate(inboundHeader.getCreatedOn(), 3));
                }

                if (inboundHeader.getUpdatedOn() != null) {
                    inboundHeader.setUpdatedOn(DateUtils.addTimeToDate(inboundHeader.getUpdatedOn(), 3));
                }
                inboundHeaderList.add(inboundHeader);
            }
            return inboundHeaderList.toArray(new InboundHeaderEntity[inboundHeaderList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST
    public InboundHeader createInboundHeader(InboundHeader newInboundHeader, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInboundHeader, headers);
        ResponseEntity<InboundHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InboundHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - replaceASN
    public Boolean replaceASN(String refDocNumber, String preInboundNo, String asnNumber, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/replaceASN")
                .queryParam("refDocNumber", refDocNumber).queryParam("preInboundNo", preInboundNo)
                .queryParam("asnNumber", asnNumber);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                Boolean.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();

    }

    // PATCH
    public InboundHeader updateInboundHeader(String warehouseId, String companyCode, String plantId, String languageId,
                                             String refDocNumber, String preInboundNo,
                                             String loginUserID, InboundHeader modifiedInboundHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(modifiedInboundHeader, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/" + refDocNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<InboundHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, InboundHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public AXApiResponse updateInboundHeaderConfirm(String warehouseId, String companyCode, String plantId, String languageId,
                                                    String preInboundNo, String refDocNumber, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/confirmIndividual")
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<AXApiResponse> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                    AXApiResponse.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // DELETE
    public boolean deleteInboundHeader(String warehouseId, String refDocNumber, String preInboundNo, String loginUserID,
                                       String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundheader/" + refDocNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------InboundLine------------------------------------------------------------------------
    // GET ALL
    public InboundLine[] getInboundLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundLine[].class);
            log.info("result : " + result.getStatusCode());

            List<InboundLine> obList = new ArrayList<>();
            for (InboundLine inboundLine : result.getBody()) {

                obList.add(addingTimeWithDateInboundLine(inboundLine));

            }
            return obList.toArray(new InboundLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public InboundLine addingTimeWithDateInboundLine(InboundLine inboundLine) throws ParseException {

        if (inboundLine.getExpectedArrivalDate() != null) {
            inboundLine.setExpectedArrivalDate(DateUtils.addTimeToDate(inboundLine.getExpectedArrivalDate(), 3));
        }

        if (inboundLine.getCreatedOn() != null) {
            inboundLine.setCreatedOn(DateUtils.addTimeToDate(inboundLine.getCreatedOn(), 3));
        }

        if (inboundLine.getConfirmedOn() != null) {
            inboundLine.setConfirmedOn(DateUtils.addTimeToDate(inboundLine.getConfirmedOn(), 3));
        }

        return inboundLine;
    }

    // GET
    public InboundLine getInboundLine(String warehouseId, String companyCode, String plantId, String languageId,
                                      String refDocNumber, String preInboundNo, Long lineNo,
                                      String itemCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundline/" + lineNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo).queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundLine.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateInboundLine(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - Finder
    public InboundLine[] findInboundLine(SearchInboundLine searchInboundLine, String authToken)
            throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inboundline/findInboundLine");
        HttpEntity<?> entity = new HttpEntity<>(searchInboundLine, headers);
        ResponseEntity<InboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InboundLine[].class);

        List<InboundLine> obList = new ArrayList<>();
        for (InboundLine inboundLine : result.getBody()) {

            obList.add(addingTimeWithDateInboundLine(inboundLine));

        }
        return obList.toArray(new InboundLine[obList.size()]);
    }

    // POST
    public InboundLine createInboundLine(InboundLine newInboundLine, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "inboundline")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInboundLine, headers);
        ResponseEntity<InboundLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                InboundLine.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public InboundLine updateInboundLine(String warehouseId, String companyCode, String plantId,
                                         String languageId, String refDocNumber, String preInboundNo, Long lineNo,
                                         String itemCode, String loginUserID, InboundLine modifiedInboundLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedInboundLine, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundline/" + lineNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<InboundLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    InboundLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteInboundLine(String warehouseId, String companyCode, String plantId, String languageId, String refDocNumber, String preInboundNo, Long lineNo,
                                     String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inboundline/" + lineNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo).queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------StagingHeader------------------------------------------------------------------------
    // GET ALL
    public StagingHeader[] getStagingHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StagingHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StagingHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<StagingHeader> obList = new ArrayList<>();
            for (StagingHeader stagingHeader : result.getBody()) {

                obList.add(addingTimeWithDateStagingHeader(stagingHeader));

            }
            return obList.toArray(new StagingHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public StagingHeader addingTimeWithDateStagingHeader(StagingHeader stagingHeader) throws ParseException {

        if (stagingHeader.getConfirmedOn() != null) {
            stagingHeader.setConfirmedOn(DateUtils.addTimeToDate(stagingHeader.getConfirmedOn(), 3));
        }

        if (stagingHeader.getCreatedOn() != null) {
            stagingHeader.setCreatedOn(DateUtils.addTimeToDate(stagingHeader.getCreatedOn(), 3));
        }

        if (stagingHeader.getUpdatedOn() != null) {
            stagingHeader.setUpdatedOn(DateUtils.addTimeToDate(stagingHeader.getUpdatedOn(), 3));
        }

        return stagingHeader;
    }

    // GET
    public StagingHeader getStagingHeader(String warehouseId, String preInboundNo, String refDocNumber,
                                          String companyCode, String plantId, String languageId,
                                          String stagingNo, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/" + stagingNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("stagingNo", stagingNo);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StagingHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StagingHeader.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateStagingHeader(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - Find StagingHeader
    public StagingHeader[] findStagingHeader(SearchStagingHeader searchStagingHeader, String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/findStagingHeader");
        HttpEntity<?> entity = new HttpEntity<>(searchStagingHeader, headers);
        ResponseEntity<StagingHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, StagingHeader[].class);

        List<StagingHeader> stagingHeaderList = new ArrayList<>();
        for (StagingHeader stagingHeader : result.getBody()) {

            stagingHeaderList.add(addingTimeWithDateStagingHeader(stagingHeader));
        }
        return stagingHeaderList.toArray(new StagingHeader[stagingHeaderList.size()]);
    }

    // POST - Find StagingHeader - Stream
    public StagingHeader[] findStagingHeaderNew(SearchStagingHeader searchStagingHeader, String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/findStagingHeaderNew");
        HttpEntity<?> entity = new HttpEntity<>(searchStagingHeader, headers);
        ResponseEntity<StagingHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, StagingHeader[].class);

        List<StagingHeader> stagingHeaderList = new ArrayList<>();
        for (StagingHeader stagingHeader : result.getBody()) {

            stagingHeaderList.add(addingTimeWithDateStagingHeader(stagingHeader));
        }
        return stagingHeaderList.toArray(new StagingHeader[stagingHeaderList.size()]);
    }

    // POST
    public StagingHeader createStagingHeader(StagingHeader newStagingHeader, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newStagingHeader, headers);
        ResponseEntity<StagingHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, StagingHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public StagingHeader updateStagingHeader(String warehouseId, String preInboundNo, String refDocNumber,
                                             String stagingNo, String loginUserID, StagingHeader modifiedStagingHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedStagingHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/" + stagingNo)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo).queryParam("stagingNo", stagingNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<StagingHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, StagingHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteStagingHeader(String warehouseId, String preInboundNo, String refDocNumber, String stagingNo,
                                       String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/" + stagingNo)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("preInboundNo", preInboundNo).queryParam("stagingNo", stagingNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - /{numberOfCases}/barcode
    public String[] generateNumberRanges(Long numberOfCases, String warehouseId, String companyCode,
                                         String plantId, String languageId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingheader/" + numberOfCases + "/barcode")
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    String[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------StagingLine------------------------------------------------------------------------
    // GET ALL
    public StagingLineEntity[] getStagingLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StagingLineEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, StagingLineEntity[].class);
            log.info("result : " + result.getStatusCode());

            List<StagingLineEntity> stagingLineList = new ArrayList<>();
            for (StagingLineEntity stagingLine : result.getBody()) {

                stagingLineList.add(addingTimeWithDateStagingLineEntity(stagingLine));
            }
            return stagingLineList.toArray(new StagingLineEntity[stagingLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public StagingLineEntity addingTimeWithDateStagingLineEntity(StagingLineEntity stagingLine) throws ParseException {

        if (stagingLine.getCreatedOn() != null) {
            stagingLine.setCreatedOn(DateUtils.addTimeToDate(stagingLine.getCreatedOn(), 3));
        }

        if (stagingLine.getUpdatedOn() != null) {
            stagingLine.setUpdatedOn(DateUtils.addTimeToDate(stagingLine.getUpdatedOn(), 3));
        }

        if (stagingLine.getConfirmedOn() != null) {
            stagingLine.setConfirmedOn(DateUtils.addTimeToDate(stagingLine.getConfirmedOn(), 3));
        }

        return stagingLine;
    }

    // GET
    public StagingLineEntity getStagingLine(String companyCode, String plantId, String languageId,
                                            String warehouseId, String preInboundNo, String refDocNumber,
                                            String stagingNo, String palletCode, String caseCode,
                                            Long lineNo, String itemCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/" + lineNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("stagingNo", stagingNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("itemCode", itemCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StagingLineEntity> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StagingLineEntity.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateStagingLineEntity(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findStagingLine
    public StagingLineEntity[] findStagingLine(SearchStagingLine searchStagingLine, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/findStagingLine");
            HttpEntity<?> entity = new HttpEntity<>(searchStagingLine, headers);
            ResponseEntity<StagingLineEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, StagingLineEntity[].class);

            List<StagingLineEntity> stagingLineList = new ArrayList<>();
            for (StagingLineEntity stagingLine : result.getBody()) {

                stagingLineList.add(addingTimeWithDateStagingLineEntity(stagingLine));
            }
            return stagingLineList.toArray(new StagingLineEntity[stagingLineList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST
    public StagingLineEntity[] createStagingLine(List<StagingLine> newStagingLine, String loginUserID,
                                                 String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "stagingline")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newStagingLine, headers);
        ResponseEntity<StagingLineEntity[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, StagingLineEntity[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public StagingLineEntity updateStagingLine(String companyCode, String plantId, String languageId,
                                               String warehouseId, String preInboundNo, String refDocNumber,
                                               String stagingNo, String palletCode, String caseCode, Long lineNo,
                                               String itemCode, String loginUserID,
                                               StagingLineEntity modifiedStagingLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedStagingLine, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/" + lineNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("stagingNo", stagingNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<StagingLineEntity> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, StagingLineEntity.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public StagingLineEntity[] caseConfirmation(String companyCode, String plantId, String languageId,
                                                List<CaseConfirmation> caseConfirmations, String caseCode,
                                                String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(caseConfirmations, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/caseConfirmation")
                    .queryParam("caseCode", caseCode)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<StagingLineEntity[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, StagingLineEntity[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // DELETE
    public boolean deleteStagingLine(String companyCode, String plantId, String languageId,
                                     String warehouseId, String preInboundNo, String refDocNumber,
                                     String stagingNo, String palletCode, String caseCode,
                                     Long lineNo, String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/" + lineNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("stagingNo", stagingNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteCases(String preInboundNo, Long lineNo, String itemCode, String companyCode,
                               String plantId, String languageId, String caseCode, String loginUserID,
                               String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/" + lineNo + "/cases")
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("caseCode", caseCode)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // AssignHHTUser
    public StagingLineEntity[] assignHHTUser(List<AssignHHTUser> assignHHTUsers, String assignedUserId,
                                             String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(assignHHTUsers, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "stagingline/assignHHTUser")
                    .queryParam("assignedUserId", assignedUserId).queryParam("loginUserID", loginUserID);

            ResponseEntity<StagingLineEntity[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, StagingLineEntity[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------GrHeader------------------------------------------------------------------------
    // GET ALL
    public GrHeader[] getGrHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "grheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GrHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, GrHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<GrHeader> grHeaderList = new ArrayList<>();
            for (GrHeader grHeader : result.getBody()) {

                grHeaderList.add(addingTimeWithDateGrHeader(grHeader));
            }
            return grHeaderList.toArray(new GrHeader[grHeaderList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public GrHeader addingTimeWithDateGrHeader(GrHeader grHeader) throws ParseException {

        if (grHeader.getCreatedOn() != null) {
            grHeader.setCreatedOn(DateUtils.addTimeToDate(grHeader.getCreatedOn(), 3));
        }

        if (grHeader.getUpdatedOn() != null) {
            grHeader.setUpdatedOn(DateUtils.addTimeToDate(grHeader.getUpdatedOn(), 3));
        }

        if (grHeader.getConfirmedOn() != null) {
            grHeader.setConfirmedOn(DateUtils.addTimeToDate(grHeader.getConfirmedOn(), 3));
        }

        if (grHeader.getExpectedArrivalDate() != null) {
            grHeader.setExpectedArrivalDate(DateUtils.addTimeToDate(grHeader.getExpectedArrivalDate(), 3));
        }

        if (grHeader.getGoodsReceiptDate() != null) {
            grHeader.setGoodsReceiptDate(DateUtils.addTimeToDate(grHeader.getGoodsReceiptDate(), 3));
        }

        return grHeader;
    }

    // GET
    public GrHeader getGrHeader(String warehouseId, String preInboundNo, String refDocNumber, String stagingNo,
                                String goodsReceiptNo, String palletCode, String caseCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grheader/" + goodsReceiptNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("stagingNo", stagingNo)
                    .queryParam("palletCode", palletCode).queryParam("caseCode", caseCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GrHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    GrHeader.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateGrHeader(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - Finder GrHeader
    public GrHeader[] findGrHeader(SearchGrHeader searchGrHeader, String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "grheader/findGrHeader");
        HttpEntity<?> entity = new HttpEntity<>(searchGrHeader, headers);
        ResponseEntity<GrHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                GrHeader[].class);

        List<GrHeader> grHeaderList = new ArrayList<>();
        for (GrHeader grHeader : result.getBody()) {

            grHeaderList.add(addingTimeWithDateGrHeader(grHeader));
        }
        return grHeaderList.toArray(new GrHeader[grHeaderList.size()]);
    }

    // POST - Finder GrHeader -Stream JPA
    public GrHeader[] findGrHeaderNew(SearchGrHeader searchGrHeader, String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "grheader/findGrHeaderNew");
        HttpEntity<?> entity = new HttpEntity<>(searchGrHeader, headers);
        ResponseEntity<GrHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                GrHeader[].class);

        List<GrHeader> grHeaderList = new ArrayList<>();
        for (GrHeader grHeader : result.getBody()) {

            grHeaderList.add(addingTimeWithDateGrHeader(grHeader));
        }
        return grHeaderList.toArray(new GrHeader[grHeaderList.size()]);
    }


    // POST
    public GrHeader createGrHeader(GrHeader newGrHeader, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "grheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newGrHeader, headers);
        ResponseEntity<GrHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                GrHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public GrHeader updateGrHeader(String warehouseId, String preInboundNo, String refDocNumber, String stagingNo,
                                   String companyCode, String plantId, String languageId,
                                   String goodsReceiptNo, String palletCode, String caseCode,
                                   String loginUserID, GrHeader modifiedGrHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedGrHeader, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grheader/" + goodsReceiptNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("stagingNo", stagingNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<GrHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    GrHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteGrHeader(String warehouseId, String companyCode, String plantId, String languageId,
                                  String preInboundNo, String refDocNumber, String stagingNo,
                                  String goodsReceiptNo, String palletCode, String caseCode,
                                  String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grheader/" + goodsReceiptNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("stagingNo", stagingNo)
                    .queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------GrLine------------------------------------------------------------------------
    // GET ALL
    public GrLine[] getGrLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "grline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GrLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    GrLine[].class);
            log.info("result : " + result.getStatusCode());

            List<GrLine> grLineList = new ArrayList<>();
            for (GrLine grLine : result.getBody()) {

                grLineList.add(addingTimeWithDateGrLine(grLine));
            }
            return grLineList.toArray(new GrLine[grLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public GrLine addingTimeWithDateGrLine(GrLine grLine) throws ParseException {

        if (grLine.getCreatedOn() != null) {
            grLine.setCreatedOn(DateUtils.addTimeToDate(grLine.getCreatedOn(), 3));
        }

        if (grLine.getUpdatedOn() != null) {
            grLine.setUpdatedOn(DateUtils.addTimeToDate(grLine.getUpdatedOn(), 3));
        }

        if (grLine.getConfirmedOn() != null) {
            grLine.setConfirmedOn(DateUtils.addTimeToDate(grLine.getConfirmedOn(), 3));
        }

        if (grLine.getExpiryDate() != null) {
            grLine.setExpiryDate(DateUtils.addTimeToDate(grLine.getExpiryDate(), 3));
        }

        if (grLine.getManufacturerDate() != null) {
            grLine.setManufacturerDate(DateUtils.addTimeToDate(grLine.getManufacturerDate(), 3));
        }

        return grLine;
    }

    // GET
    public GrLine getGrLine(String warehouseId, String preInboundNo, String refDocNumber, String goodsReceiptNo, String companyCode,
                            String palletCode, String caseCode, String packBarcodes, Long lineNo, String itemCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grline/" + lineNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode).queryParam("caseCode", caseCode)
                    .queryParam("companyCode", companyCode)
                    .queryParam("packBarcodes", packBarcodes).queryParam("itemCode", itemCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GrLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    GrLine.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateGrLine(result.getBody());

        } catch (Exception e) {
            throw e;
        }
    }

    // GET
    public GrLine[] getGrLine(String preInboundNo, String refDocNumber, String packBarcodes, Long lineNo,
                              String itemCode, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grline/" + lineNo + "/putawayline")
                    .queryParam("preInboundNo", preInboundNo).queryParam("refDocNumber", refDocNumber)
                    .queryParam("packBarcodes", packBarcodes).queryParam("itemCode", itemCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GrLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    GrLine[].class);
            log.info("result : " + result.getStatusCode());

            List<GrLine> grLineList = new ArrayList<>();
            for (GrLine grLine : result.getBody()) {

                grLineList.add(addingTimeWithDateGrLine(grLine));
            }
            return grLineList.toArray(new GrLine[grLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - Finder GrLine
    public GrLine[] findGrLine(SearchGrLine searchGrLine, String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "grline/findGrLine");
        HttpEntity<?> entity = new HttpEntity<>(searchGrLine, headers);
        ResponseEntity<GrLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                GrLine[].class);

        List<GrLine> grLineList = new ArrayList<>();
        for (GrLine grLine : result.getBody()) {

            grLineList.add(addingTimeWithDateGrLine(grLine));
        }
        return grLineList.toArray(new GrLine[grLineList.size()]);
    }

    // POST
    public GrLine[] createGrLine(List<AddGrLine> newGrLine, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "grline")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newGrLine, headers);
        ResponseEntity<GrLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                GrLine[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public GrLine updateGrLine(String warehouseId, String preInboundNo, String refDocNumber, String goodsReceiptNo,
                               String palletCode, String caseCode, String packBarcodes, Long lineNo, String itemCode, String loginUserID,
                               GrLine modifiedGrLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedGrLine, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grline/" + lineNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode).queryParam("caseCode", caseCode)
                    .queryParam("packBarcodes", packBarcodes).queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<GrLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    GrLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteGrLine(String warehouseId, String preInboundNo, String refDocNumber,
                                String goodsReceiptNo, String companyCode, String plantId, String languageId,
                                String palletCode, String caseCode, String packBarcodes, Long lineNo,
                                String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grline/" + lineNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET PackBarcode
    public PackBarcode[] generatePackBarcode(String companyCode, String plantId, String languageId,
                                             Long acceptQty, Long damageQty, String warehouseId,
                                             String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "grline/packBarcode")
                    .queryParam("companyCode", companyCode)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("acceptQty", acceptQty)
                    .queryParam("damageQty", damageQty)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PackBarcode[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PackBarcode[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // --------------------------------------------PutAwayHeader------------------------------------------------------------------------
    // GET ALL
    public PutAwayHeader[] getPutAwayHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayHeader> putAwayHeaderList = new ArrayList<>();
            for (PutAwayHeader putAwayHeader : result.getBody()) {

                putAwayHeaderList.add(addingTimeWithDatePutAwayHeader(putAwayHeader));
            }
            return putAwayHeaderList.toArray(new PutAwayHeader[putAwayHeaderList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PutAwayHeader addingTimeWithDatePutAwayHeader(PutAwayHeader putAwayHeader) throws ParseException {

        if (putAwayHeader.getCreatedOn() != null) {
            putAwayHeader.setCreatedOn(DateUtils.addTimeToDate(putAwayHeader.getCreatedOn(), 3));
        }

        if (putAwayHeader.getUpdatedOn() != null) {
            putAwayHeader.setUpdatedOn(DateUtils.addTimeToDate(putAwayHeader.getUpdatedOn(), 3));
        }

        if (putAwayHeader.getConfirmedOn() != null) {
            putAwayHeader.setConfirmedOn(DateUtils.addTimeToDate(putAwayHeader.getConfirmedOn(), 3));
        }

        return putAwayHeader;
    }

    // GET
    public PutAwayHeader getPutAwayHeader(String warehouseId, String preInboundNo, String refDocNumber,
                                          String goodsReceiptNo, String palletCode, String caseCode, String packBarcodes, String putAwayNumber,
                                          String proposedStorageBin, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/" + putAwayNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode).queryParam("caseCode", caseCode)
                    .queryParam("packBarcodes", packBarcodes).queryParam("putAwayNumber", putAwayNumber)
                    .queryParam("proposedStorageBin", proposedStorageBin);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayHeader.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDatePutAwayHeader(result.getBody());

        } catch (Exception e) {
            throw e;
        }
    }

    //Find PutAwayHeader
    public PutAwayHeader[] findPutAwayHeader(SearchPutAwayHeader searchPutAwayHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/findPutAwayHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPutAwayHeader, headers);
            ResponseEntity<PutAwayHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayHeader> putAwayHeaderList = new ArrayList<>();
            for (PutAwayHeader putAwayHeader : result.getBody()) {

                putAwayHeaderList.add(addingTimeWithDatePutAwayHeader(putAwayHeader));
            }
            return putAwayHeaderList.toArray(new PutAwayHeader[putAwayHeaderList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    //Find PutAwayHeader - Stream
    public PutAwayHeader[] findPutAwayHeaderNew(SearchPutAwayHeader searchPutAwayHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/findPutAwayHeaderNew");
            HttpEntity<?> entity = new HttpEntity<>(searchPutAwayHeader, headers);
            ResponseEntity<PutAwayHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayHeader> putAwayHeaderList = new ArrayList<>();
            for (PutAwayHeader putAwayHeader : result.getBody()) {

                putAwayHeaderList.add(addingTimeWithDatePutAwayHeader(putAwayHeader));
            }
            return putAwayHeaderList.toArray(new PutAwayHeader[putAwayHeaderList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // GET - /{refDocNumber}/inboundreversal/asn
    public PutAwayHeader[] getPutAwayHeader(String refDocNumber, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    getTransactionServiceApiUrl() + "putawayheader/" + refDocNumber + "/inboundreversal/asn");

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayHeader> putAwayHeaderList = new ArrayList<>();
            for (PutAwayHeader putAwayHeader : result.getBody()) {

                putAwayHeaderList.add(addingTimeWithDatePutAwayHeader(putAwayHeader));
            }
            return putAwayHeaderList.toArray(new PutAwayHeader[putAwayHeaderList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public PutAwayHeader createPutAwayHeader(PutAwayHeader newPutAwayHeader, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPutAwayHeader, headers);
        ResponseEntity<PutAwayHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PutAwayHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public PutAwayHeader updatePutAwayHeader(String companyCodeId, String plantId, String languageId,
                                             String warehouseId, String preInboundNo, String refDocNumber,
                                             String goodsReceiptNo, String palletCode, String caseCode,
                                             String packBarcodes, String putAwayNumber, String proposedStorageBin,
                                             PutAwayHeader modifiedPutAwayHeader, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedPutAwayHeader, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/" + putAwayNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("putAwayNumber", putAwayNumber)
                    .queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PutAwayHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PutAwayHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH - /{refDocNumber}/reverse
    public PutAwayHeader[] updatePutAwayHeader(String companyCodeId, String plantId, String languageId, String warehouseId,
                                               String refDocNumber, String packBarcodes, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/" + refDocNumber + "/reverse")
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PutAwayHeader[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PutAwayHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePutAwayHeader(String warehouseId, String preInboundNo, String refDocNumber,
                                       String companyCodeId, String languageId, String plantId,
                                       String goodsReceiptNo, String palletCode, String caseCode,
                                       String packBarcodes, String putAwayNumber,
                                       String proposedStorageBin, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayheader/" + putAwayNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("palletCode", palletCode)
                    .queryParam("caseCode", caseCode)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("putAwayNumber", putAwayNumber)
                    .queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------PutAwayLine------------------------------------------------------------------------
    // GET ALL
    public PutAwayLine[] getPutAwayLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayLine[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayLine> putAwayLineList = new ArrayList<>();
            for (PutAwayLine putAwayLine : result.getBody()) {

                putAwayLineList.add(addingTimeWithDatePutAwayLine(putAwayLine));
            }
            return putAwayLineList.toArray(new PutAwayLine[putAwayLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PutAwayLine addingTimeWithDatePutAwayLine(PutAwayLine putAwayLine) throws ParseException {

        if (putAwayLine.getCreatedOn() != null) {
            putAwayLine.setCreatedOn(DateUtils.addTimeToDate(putAwayLine.getCreatedOn(), 3));
        }
        if (putAwayLine.getConfirmedOn() != null) {
            putAwayLine.setConfirmedOn(DateUtils.addTimeToDate(putAwayLine.getConfirmedOn(), 3));
        }
        if (putAwayLine.getUpdatedOn() != null) {
            putAwayLine.setUpdatedOn(DateUtils.addTimeToDate(putAwayLine.getUpdatedOn(), 3));
        }

        return putAwayLine;
    }

    // GET
    public PutAwayLine getPutAwayLine(String warehouseId, String goodsReceiptNo, String preInboundNo,
                                      String refDocNumber, String putAwayNumber, Long lineNo, String itemCode, String proposedStorageBin,
                                      String confirmedStorageBin, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline/" + confirmedStorageBin)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("putAwayNumber", putAwayNumber).queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode).queryParam("confirmedStorageBin", confirmedStorageBin)
                    .queryParam("proposedStorageBin", proposedStorageBin);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayLine.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDatePutAwayLine(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - /{refDocNumber}/inboundreversal/palletId
    public PutAwayLine[] getPutAwayLine(String refDocNumber, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    getTransactionServiceApiUrl() + "putawayline/" + refDocNumber + "/inboundreversal/palletId");

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PutAwayLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PutAwayLine[].class);
            log.info("result : " + result.getStatusCode());

            List<PutAwayLine> putAwayLineList = new ArrayList<>();
            for (PutAwayLine putAwayLine : result.getBody()) {

                putAwayLineList.add(addingTimeWithDatePutAwayLine(putAwayLine));
            }
            return putAwayLineList.toArray(new PutAwayLine[putAwayLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET ALL
    public PutAwayLine[] findPutAwayLine(SearchPutAwayLine searchPutAwayLine, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline/findPutAwayLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPutAwayLine, headers);
            ResponseEntity<PutAwayLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PutAwayLine[].class);

            List<PutAwayLine> putAwayLineList = new ArrayList<>();
            for (PutAwayLine putAwayLine : result.getBody()) {

                putAwayLineList.add(addingTimeWithDatePutAwayLine(putAwayLine));
            }
            return putAwayLineList.toArray(new PutAwayLine[putAwayLineList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST
    public PutAwayLine[] createPutAwayLine(List<AddPutAwayLine> newPutAwayLine, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline/confirm")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPutAwayLine, headers);
        ResponseEntity<PutAwayLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PutAwayLine[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public PutAwayLine updatePutAwayLine(String warehouseId, String goodsReceiptNo, String preInboundNo,
                                         String refDocNumber, String putAwayNumber, Long lineNo, String itemCode, String proposedStorageBin,
                                         String confirmedStorageBin, PutAwayLine modifiedPutAwayLine, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedPutAwayLine, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline/" + confirmedStorageBin)
                    .queryParam("warehouseId", warehouseId).queryParam("preInboundNo", preInboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("goodsReceiptNo", goodsReceiptNo)
                    .queryParam("putAwayNumber", putAwayNumber).queryParam("lineNo", lineNo)
                    .queryParam("itemCode", itemCode).queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PutAwayLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    PutAwayLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePutAwayLine(String languageId, String companyCodeId, String plantId, String warehouseId,
                                     String goodsReceiptNo, String preInboundNo, String refDocNumber, String putAwayNumber, Long lineNo,
                                     String itemCode, String proposedStorageBin, String confirmedStorageBin, String loginUserID,
                                     String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "putawayline/" + confirmedStorageBin)
                    .queryParam("languageId", languageId).queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId).queryParam("warehouseId", warehouseId)
                    .queryParam("preInboundNo", preInboundNo).queryParam("refDocNumber", refDocNumber)
                    .queryParam("goodsReceiptNo", goodsReceiptNo).queryParam("putAwayNumber", putAwayNumber)
                    .queryParam("lineNo", lineNo).queryParam("itemCode", itemCode)
                    .queryParam("confirmedStorageBin", confirmedStorageBin)
                    .queryParam("proposedStorageBin", proposedStorageBin).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------InventoryMovement------------------------------------------------------------------------
    // GET ALL
    public InventoryMovement[] getInventoryMovements(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InventoryMovement[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InventoryMovement[].class);
            log.info("result : " + result.getStatusCode());

            List<InventoryMovement> inventoryMovementList = new ArrayList<>();
            for (InventoryMovement inventoryMovement : result.getBody()) {

                inventoryMovementList.add(addingTimeWithDateInventoryMovement(inventoryMovement));
            }
            return inventoryMovementList.toArray(new InventoryMovement[inventoryMovementList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public InventoryMovement addingTimeWithDateInventoryMovement(InventoryMovement inventoryMovement) throws ParseException {

        if (inventoryMovement.getCreatedOn() != null) {
            inventoryMovement.setCreatedOn(DateUtils.addTimeToDate(inventoryMovement.getCreatedOn(), 3));
        }

        return inventoryMovement;
    }

    // GET
    public InventoryMovement getInventoryMovement(String warehouseId, Long movementType, Long submovementType,
                                                  String packBarcodes, String itemCode, String batchSerialNumber, String movementDocumentNo, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement/" + movementType)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("movementType", movementType)
                    .queryParam("submovementType", submovementType)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode)
                    .queryParam("batchSerialNumber", batchSerialNumber)
                    .queryParam("movementDocumentNo", movementDocumentNo);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InventoryMovement> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InventoryMovement.class);
            return addingTimeWithDateInventoryMovement(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findInventoryMovement
    public InventoryMovement[] findInventoryMovement(SearchInventoryMovement searchInventoryMovement, String authToken)
            throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement/findInventoryMovement");
            HttpEntity<?> entity = new HttpEntity<>(searchInventoryMovement, headers);
            ResponseEntity<InventoryMovement[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, InventoryMovement[].class);

            List<InventoryMovement> inventoryMovementList = new ArrayList<>();
            for (InventoryMovement inventoryMovement : result.getBody()) {

                inventoryMovementList.add(addingTimeWithDateInventoryMovement(inventoryMovement));
            }
            return inventoryMovementList.toArray(new InventoryMovement[inventoryMovementList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public InventoryMovement createInventoryMovement(InventoryMovement newInventoryMovement, String loginUserID,
                                                     String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInventoryMovement, headers);
        ResponseEntity<InventoryMovement> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InventoryMovement.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public InventoryMovement updateInventoryMovement(String warehouseId, Long movementType, Long submovementType,
                                                     String packBarcodes, String itemCode, String batchSerialNumber, String movementDocumentNo,
                                                     InventoryMovement modifiedInventoryMovement, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedInventoryMovement, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement/" + movementType)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("movementType", movementType)
                    .queryParam("submovementType", submovementType)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode)
                    .queryParam("batchSerialNumber", batchSerialNumber)
                    .queryParam("movementDocumentNo", movementDocumentNo).queryParam("loginUserID", loginUserID);

            ResponseEntity<InventoryMovement> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, InventoryMovement.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteInventoryMovement(String warehouseId, Long movementType, Long submovementType,
                                           String packBarcodes, String itemCode, String batchSerialNumber, String movementDocumentNo, String loginUserID,
                                           String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventorymovement/" + movementType)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("movementType", movementType)
                    .queryParam("submovementType", submovementType)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode)
                    .queryParam("batchSerialNumber", batchSerialNumber)
                    .queryParam("movementDocumentNo", movementDocumentNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------Inventory------------------------------------------------------------------------
    // GET ALL
    public Inventory[] getInventorys(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Inventory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());

            List<Inventory> inventoryList = new ArrayList<>();
            for (Inventory inventory : result.getBody()) {

                inventoryList.add(addingTimeWithDateInventory(inventory));
            }
            return inventoryList.toArray(new Inventory[inventoryList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public Inventory addingTimeWithDateInventory(Inventory inventory) throws ParseException {

        if (inventory.getCreatedOn() != null) {
            inventory.setCreatedOn(DateUtils.addTimeToDate(inventory.getCreatedOn(), 3));
        }

        if (inventory.getManufacturerDate() != null) {
            inventory.setManufacturerDate(DateUtils.addTimeToDate(inventory.getManufacturerDate(), 3));
        }

        if (inventory.getExpiryDate() != null) {
            inventory.setExpiryDate(DateUtils.addTimeToDate(inventory.getExpiryDate(), 3));
        }

        return inventory;
    }

    // GET
    public Inventory getInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin,
                                  Long stockTypeId, Long specialStockIndicatorId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/" + stockTypeId)
                    .queryParam("warehouseId", warehouseId).queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode).queryParam("storageBin", storageBin)
                    .queryParam("stockTypeId", stockTypeId)
                    .queryParam("specialStockIndicatorId", specialStockIndicatorId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Inventory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Inventory.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateInventory(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public Inventory getInventory(String warehouseId, String companyCodeId, String plantId, String languageId, String packBarcodes, String itemCode, String storageBin,
                                  String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/transfer")
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode).queryParam("storageBin", storageBin);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Inventory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Inventory.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateInventory(result.getBody());

        } catch (Exception e) {
            throw e;
        }
    }

    // POST - FinderQuery
    public Inventory[] findInventory(SearchInventory searchInventory, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/findInventory");
            HttpEntity<?> entity = new HttpEntity<>(searchInventory, headers);
            ResponseEntity<Inventory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());

            List<Inventory> inventoryList = new ArrayList<>();
            for (Inventory inventory : result.getBody()) {

                inventoryList.add(addingTimeWithDateInventory(inventory));
            }
            return inventoryList.toArray(new Inventory[inventoryList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - FinderQueryNew - SQL Query
    public Inventory[] findInventoryNew(SearchInventory searchInventory, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/findInventoryNew");
            HttpEntity<?> entity = new HttpEntity<>(searchInventory, headers);
            ResponseEntity<Inventory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, Inventory[].class);
            return result.getBody();
//			log.info("result : " + result.getStatusCode());

//			List<Inventory> inventoryList = new ArrayList<>();
//			for (Inventory inventory : result.getBody()) {
//
//				inventoryList.add(addingTimeWithDateInventory(inventory));
//			}
//			return inventoryList.toArray(new Inventory[inventoryList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - FinderQuery
    public Inventory[] getQuantityValidatedInventory(SearchInventory searchInventory, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/get-all-validated-inventory");
            HttpEntity<?> entity = new HttpEntity<>(searchInventory, headers);
            ResponseEntity<Inventory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());

            List<Inventory> inventoryList = new ArrayList<>();
            for (Inventory inventory : result.getBody()) {

                inventoryList.add(addingTimeWithDateInventory(inventory));
            }
            return inventoryList.toArray(new Inventory[inventoryList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PaginatedResponse<Inventory> findInventory(SearchInventory searchInventory, Integer pageNo, Integer pageSize,
                                                      String sortBy, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/findInventory/pagination")
                    .queryParam("pageNo", pageNo).queryParam("pageSize", pageSize).queryParam("sortBy", sortBy);

            HttpEntity<?> entity = new HttpEntity<>(searchInventory, headers);

            ParameterizedTypeReference<PaginatedResponse<Inventory>> responseType = new ParameterizedTypeReference<PaginatedResponse<Inventory>>() {
            };
            ResponseEntity<PaginatedResponse<Inventory>> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, responseType);

            return result.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public Inventory createInventory(Inventory newInventory, String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "inventory")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInventory, headers);
        ResponseEntity<Inventory> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                Inventory.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public Inventory updateInventory(String warehouseId, String companyCodeId, String plantId,
                                     String languageId, String packBarcodes, String itemCode, String storageBin,
                                     Long stockTypeId, Long specialStockIndicatorId, Inventory modifiedInventory,
                                     String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(modifiedInventory, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/" + stockTypeId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode)
                    .queryParam("storageBin", storageBin)
                    .queryParam("stockTypeId", stockTypeId)
                    .queryParam("specialStockIndicatorId", specialStockIndicatorId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<Inventory> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    Inventory.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteInventory(String warehouseId, String packBarcodes, String itemCode, String storageBin,
                                   Long stockTypeId, Long specialStockIndicatorId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inventory/" + stockTypeId)
                    .queryParam("warehouseId", warehouseId).queryParam("packBarcodes", packBarcodes)
                    .queryParam("itemCode", itemCode).queryParam("storageBin", storageBin)
                    .queryParam("stockTypeId", stockTypeId)
                    .queryParam("specialStockIndicatorId", specialStockIndicatorId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // --------------------------------------------InhouseTransferHeader------------------------------------------------------------------------
    // GET ALL
    public InhouseTransferHeader[] getInhouseTransferHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InhouseTransferHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InhouseTransferHeader[].class);

            List<InhouseTransferHeader> inhouseTransferHeaderList = new ArrayList<>();
            for (InhouseTransferHeader inhouseTransferHeader : result.getBody()) {

                inhouseTransferHeaderList.add(addingTimeWithDateInhouseTransferHeader(inhouseTransferHeader));
            }
            return inhouseTransferHeaderList.toArray(new InhouseTransferHeader[inhouseTransferHeaderList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public InhouseTransferHeader addingTimeWithDateInhouseTransferHeader(InhouseTransferHeader inhouseTransferHeader) throws ParseException {

        if (inhouseTransferHeader.getCreatedOn() != null) {
            inhouseTransferHeader.setCreatedOn(DateUtils.addTimeToDate(inhouseTransferHeader.getCreatedOn(), 3));
        }
        if (inhouseTransferHeader.getUpdatedOn() != null) {
            inhouseTransferHeader.setUpdatedOn(DateUtils.addTimeToDate(inhouseTransferHeader.getUpdatedOn(), 3));
        }

        return inhouseTransferHeader;
    }

    // GET
    public InhouseTransferHeader getInhouseTransferHeader(String warehouseId, String transferNumber,
                                                          Long transferTypeId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferheader/" + transferNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("transferNumber", transferNumber)
                    .queryParam("transferTypeId", transferTypeId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InhouseTransferHeader> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InhouseTransferHeader.class);

            return addingTimeWithDateInhouseTransferHeader(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - Find
    public InhouseTransferHeader[] findInHouseTransferHeader(SearchInhouseTransferHeader searchInHouseTransferHeader,
                                                             String authToken) throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferheader/findInHouseTransferHeader");
        HttpEntity<?> entity = new HttpEntity<>(searchInHouseTransferHeader, headers);
        ResponseEntity<InhouseTransferHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, InhouseTransferHeader[].class);

        List<InhouseTransferHeader> inhouseTransferHeaderList = new ArrayList<>();
        for (InhouseTransferHeader inhouseTransferHeader : result.getBody()) {

            inhouseTransferHeaderList.add(addingTimeWithDateInhouseTransferHeader(inhouseTransferHeader));
        }
        return inhouseTransferHeaderList.toArray(new InhouseTransferHeader[inhouseTransferHeaderList.size()]);
    }

    // POST - Find - Stream
    public InhouseTransferHeader[] findInHouseTransferHeaderNew(SearchInhouseTransferHeader searchInHouseTransferHeader,
                                                                String authToken) throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferheader/findInHouseTransferHeaderNew");
        HttpEntity<?> entity = new HttpEntity<>(searchInHouseTransferHeader, headers);
        ResponseEntity<InhouseTransferHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, InhouseTransferHeader[].class);

        List<InhouseTransferHeader> inhouseTransferHeaderList = new ArrayList<>();
        for (InhouseTransferHeader inhouseTransferHeader : result.getBody()) {

            inhouseTransferHeaderList.add(addingTimeWithDateInhouseTransferHeader(inhouseTransferHeader));
        }
        return inhouseTransferHeaderList.toArray(new InhouseTransferHeader[inhouseTransferHeaderList.size()]);
    }

    // POST
    public InhouseTransferHeader createInhouseTransferHeader(InhouseTransferHeader newInhouseTransferHeader,
                                                             String loginUserID, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferheader")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInhouseTransferHeader, headers);
        ResponseEntity<InhouseTransferHeader> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, InhouseTransferHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // --------------------------------------------InhouseTransferHeader------------------------------------------------------------------------
    // GET ALL
    public InhouseTransferLine[] getInhouseTransferLines(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferline");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InhouseTransferLine[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InhouseTransferLine[].class);
            log.info("result : " + result.getStatusCode());

            List<InhouseTransferLine> inhouseTransferLineList = new ArrayList<>();
            for (InhouseTransferLine inhouseTransferLine : result.getBody()) {

                inhouseTransferLineList.add(addingTimeWithDateInhouseTransferLine(inhouseTransferLine));
            }
            return inhouseTransferLineList.toArray(new InhouseTransferLine[inhouseTransferLineList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public InhouseTransferLine addingTimeWithDateInhouseTransferLine(InhouseTransferLine inhouseTransferLine) throws ParseException {

        if (inhouseTransferLine.getCreatedOn() != null) {
            inhouseTransferLine.setCreatedOn(DateUtils.addTimeToDate(inhouseTransferLine.getCreatedOn(), 3));
        }
        if (inhouseTransferLine.getUpdatedOn() != null) {
            inhouseTransferLine.setUpdatedOn(DateUtils.addTimeToDate(inhouseTransferLine.getUpdatedOn(), 3));
        }
        if (inhouseTransferLine.getConfirmedOn() != null) {
            inhouseTransferLine.setConfirmedOn(DateUtils.addTimeToDate(inhouseTransferLine.getConfirmedOn(), 3));
        }
        return inhouseTransferLine;
    }

    // GET
    public InhouseTransferLine getInhouseTransferLine(String warehouseId, String transferNumber, String sourceItemCode,
                                                      String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferline/" + transferNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("transferNumber", transferNumber)
                    .queryParam("sourceItemCode", sourceItemCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InhouseTransferLine> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InhouseTransferLine.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDateInhouseTransferLine(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - Find
    public InhouseTransferLine[] findInhouseTransferLine(SearchInhouseTransferLine searchInhouseTransferLine,
                                                         String authToken) throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferline/findInhouseTransferLine");
        HttpEntity<?> entity = new HttpEntity<>(searchInhouseTransferLine, headers);
        ResponseEntity<InhouseTransferLine[]> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, InhouseTransferLine[].class);

        List<InhouseTransferLine> inhouseTransferLineList = new ArrayList<>();
        for (InhouseTransferLine inhouseTransferLine : result.getBody()) {

            inhouseTransferLineList.add(addingTimeWithDateInhouseTransferLine(inhouseTransferLine));
        }
        return inhouseTransferLineList.toArray(new InhouseTransferLine[inhouseTransferLineList.size()]);
    }

    // POST
    public InhouseTransferLine createInhouseTransferLine(InhouseTransferLine newInhouseTransferLine, String loginUserID,
                                                         String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "inhousetransferline")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newInhouseTransferLine, headers);
        ResponseEntity<InhouseTransferLine> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, InhouseTransferLine.class);
        return result.getBody();
    }

    /*
     * -------------PreOutboundHeader----------------------------------------
     */
    // POST - findPreOutboundHeader
    public PreOutboundHeader[] findPreOutboundHeader(SearchPreOutboundHeader searchPreOutboundHeader,
                                                     String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preoutboundheader/findPreOutboundHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPreOutboundHeader, headers);
            ResponseEntity<PreOutboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PreOutboundHeader[].class);
            log.info("result : " + result.getBody());

            List<PreOutboundHeader> obList = new ArrayList<>();
            for (PreOutboundHeader obHeader : result.getBody()) {
                log.info("Result RefDocDate :" + obHeader.getRefDocDate());
                if (obHeader.getRefDocDate() != null) {
                    obHeader.setRefDocDate(DateUtils.addTimeToDate(obHeader.getRefDocDate(), 3));
                }
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PreOutboundHeader[obList.size()]);
        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findPreOutboundHeader - Stream
    public PreOutboundHeader[] findPreOutboundHeaderNew(SearchPreOutboundHeader searchPreOutboundHeader,
                                                        String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preoutboundheader/findPreOutboundHeaderNew");
            HttpEntity<?> entity = new HttpEntity<>(searchPreOutboundHeader, headers);
            ResponseEntity<PreOutboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PreOutboundHeader[].class);
            log.info("result : " + result.getBody());

            List<PreOutboundHeader> obList = new ArrayList<>();
            for (PreOutboundHeader obHeader : result.getBody()) {
                log.info("Result RefDocDate :" + obHeader.getRefDocDate());
                if (obHeader.getRefDocDate() != null) {
                    obHeader.setRefDocDate(DateUtils.addTimeToDate(obHeader.getRefDocDate(), 3));
                }
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PreOutboundHeader[obList.size()]);
        } catch (Exception e) {
            throw e;
        }
    }

    // -------------------------PreOutboundLine------------------------------------------------
    public PreOutboundLine[] findPreOutboundLine(SearchPreOutboundLine searchPreOutboundLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "preoutboundline/findPreOutboundLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPreOutboundLine, headers);
            ResponseEntity<PreOutboundLine[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PreOutboundLine[].class);
            log.info("result : " + result.getStatusCode());

            List<PreOutboundLine> obList = new ArrayList<>();
            for (PreOutboundLine obHeader : result.getBody()) {
                log.info("Result RefDocDate :" + obHeader.getRequiredDeliveryDate());
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PreOutboundLine[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // --------------------------OrderManagementLine----------------------------------------------------

    // POST - findOrderManagementLine
    public OrderManagementLine[] findOrderManagementLine(SearchOrderManagementLine searchOrderManagementLine,
                                                         String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/findOrderManagementLine");
            HttpEntity<?> entity = new HttpEntity<>(searchOrderManagementLine, headers);
            ResponseEntity<OrderManagementLine[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, OrderManagementLine[].class);
            log.info("result : " + result.getStatusCode());

            List<OrderManagementLine> obList = new ArrayList<>();
            for (OrderManagementLine obHeader : result.getBody()) {
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getPickupCreatedOn() != null) {
                    obHeader.setPickupCreatedOn(DateUtils.addTimeToDate(obHeader.getPickupCreatedOn(), 3));
                }
                if (obHeader.getPickupupdatedOn() != null) {
                    obHeader.setPickupupdatedOn(DateUtils.addTimeToDate(obHeader.getPickupupdatedOn(), 3));
                }
                if (obHeader.getReAllocatedOn() != null) {
                    obHeader.setReAllocatedOn(DateUtils.addTimeToDate(obHeader.getReAllocatedOn(), 3));
                }
                if (obHeader.getPickerAssignedOn() != null) {
                    obHeader.setPickerAssignedOn(DateUtils.addTimeToDate(obHeader.getPickerAssignedOn(), 3));
                }
                if (obHeader.getPickerReassignedOn() != null) {
                    obHeader.setPickerReassignedOn(DateUtils.addTimeToDate(obHeader.getPickerReassignedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OrderManagementLine[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findOrderManagementLine - Stream
    public OrderManagementLine[] findOrderManagementLineNew(SearchOrderManagementLine searchOrderManagementLine,
                                                            String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/findOrderManagementLineNew");
            HttpEntity<?> entity = new HttpEntity<>(searchOrderManagementLine, headers);
            ResponseEntity<OrderManagementLine[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, OrderManagementLine[].class);
            log.info("result : " + result.getStatusCode());

            List<OrderManagementLine> obList = new ArrayList<>();
            for (OrderManagementLine obHeader : result.getBody()) {
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getPickupCreatedOn() != null) {
                    obHeader.setPickupCreatedOn(DateUtils.addTimeToDate(obHeader.getPickupCreatedOn(), 3));
                }
                if (obHeader.getPickupupdatedOn() != null) {
                    obHeader.setPickupupdatedOn(DateUtils.addTimeToDate(obHeader.getPickupupdatedOn(), 3));
                }
                if (obHeader.getReAllocatedOn() != null) {
                    obHeader.setReAllocatedOn(DateUtils.addTimeToDate(obHeader.getReAllocatedOn(), 3));
                }
                if (obHeader.getPickerAssignedOn() != null) {
                    obHeader.setPickerAssignedOn(DateUtils.addTimeToDate(obHeader.getPickerAssignedOn(), 3));
                }
                if (obHeader.getPickerReassignedOn() != null) {
                    obHeader.setPickerReassignedOn(DateUtils.addTimeToDate(obHeader.getPickerReassignedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OrderManagementLine[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    public String updateRef9ANDRef10(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/updateRefFields");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    String.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public OrderManagementLine doUnAllocation(String warehouseId, String companyCodeId, String plantId, String languageId, String preOutboundNo, String refDocNumber,
                                              String partnerCode, Long lineNumber, String itemCode, String proposedStorageBin, String proposedPackBarCode,
                                              String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/unallocate")
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("companyCodeId", companyCodeId).queryParam("plantId", plantId).queryParam("languageId", languageId)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("itemCode", itemCode)
                    .queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("proposedPackBarCode", proposedPackBarCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderManagementLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, OrderManagementLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public OrderManagementLine doAllocation(String companyCodeId, String plantId, String languageId,
                                            String warehouseId, String preOutboundNo, String refDocNumber,
                                            String partnerCode, Long lineNumber, String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpClient client = HttpClients.createDefault();

            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/allocate")
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderManagementLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, OrderManagementLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public OrderManagementLine[] doAssignPicker(List<AssignPicker> assignPicker, String assignedPickerId,
                                                String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(assignPicker, headers);

            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/assignPicker")
                    .queryParam("assignedPickerId", assignedPickerId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderManagementLine[]> result = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.PATCH, entity, OrderManagementLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public OrderManagementLine updateOrderManagementLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                                         String partnerCode, Long lineNumber, String itemCode, String proposedStorageBin, String proposedPackCode,
                                                         String loginUserID, @Valid OrderManagementLine updateOrderMangementLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOrderMangementLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/" + refDocNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("partnerCode", partnerCode).queryParam("lineNumber", lineNumber)
                    .queryParam("itemCode", itemCode).queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("proposedPackCode", proposedPackCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<OrderManagementLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, OrderManagementLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteOrderManagementLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                             String partnerCode, Long lineNumber, String itemCode, String proposedStorageBin, String proposedPackCode,
                                             String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "ordermanagementline/" + refDocNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("partnerCode", partnerCode).queryParam("lineNumber", lineNumber)
                    .queryParam("itemCode", itemCode).queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("proposedPackCode", proposedPackCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*--------------------------PickupHeader----------------------------------------------------*/
    // POST - Finder
    public PickupHeader[] findPickupHeader(SearchPickupHeader searchPickupHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupheader/findPickupHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPickupHeader, headers);
            ResponseEntity<PickupHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PickupHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PickupHeader> obList = new ArrayList<>();
            for (PickupHeader obHeader : result.getBody()) {
                if (obHeader.getPickupReversedOn() != null) {
                    obHeader.setPickupReversedOn(DateUtils.addTimeToDate(obHeader.getPickupReversedOn(), 3));
                }
                if (obHeader.getPickupCreatedOn() != null) {
                    obHeader.setPickupCreatedOn(DateUtils.addTimeToDate(obHeader.getPickupCreatedOn(), 3));
                }
                if (obHeader.getPickUpdatedOn() != null) {
                    obHeader.setPickUpdatedOn(DateUtils.addTimeToDate(obHeader.getPickUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PickupHeader[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findPickupHeader - Stream
    public PickupHeader[] findPickupHeaderNew(SearchPickupHeader searchPickupHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupheader/findPickupHeaderNew");
            HttpEntity<?> entity = new HttpEntity<>(searchPickupHeader, headers);
            ResponseEntity<PickupHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PickupHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PickupHeader> obList = new ArrayList<>();
            for (PickupHeader obHeader : result.getBody()) {
                if (obHeader.getPickupReversedOn() != null) {
                    obHeader.setPickupReversedOn(DateUtils.addTimeToDate(obHeader.getPickupReversedOn(), 3));
                }
                if (obHeader.getPickupCreatedOn() != null) {
                    obHeader.setPickupCreatedOn(DateUtils.addTimeToDate(obHeader.getPickupCreatedOn(), 3));
                }
                if (obHeader.getPickUpdatedOn() != null) {
                    obHeader.setPickUpdatedOn(DateUtils.addTimeToDate(obHeader.getPickUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PickupHeader[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public PickupHeader updatePickupHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                           String partnerCode, String pickupNumber, Long lineNumber, String itemCode, String loginUserID,
                                           @Valid PickupHeader updatePickupHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updatePickupHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupheader/" + pickupNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PickupHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    PickupHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PickupHeader[] patchAssignedPickerIdInPickupHeader(String loginUserID,
                                                              @Valid List<UpdatePickupHeader> updatePickupHeaderList, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updatePickupHeaderList, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupheader/update-assigned-picker")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PickupHeader[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PickupHeader[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePickupHeader(String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode,
                                      String pickupNumber, Long lineNumber, String itemCode, String proposedStorageBin, String proposedPackCode,
                                      String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupheader/" + pickupNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("partnerCode", partnerCode).queryParam("lineNumber", lineNumber)
                    .queryParam("refDocNumber", refDocNumber).queryParam("itemCode", itemCode)
                    .queryParam("proposedStorageBin", proposedStorageBin)
                    .queryParam("proposedPackCode", proposedPackCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*--------------------------PickupLine----------------------------------------------------*/
    // GET
    public Inventory[] getAdditionalBins(String companyCodeId, String plantId, String languageId, String warehouseId,
                                         String itemCode, Long obOrdertypeId, String proposedPackBarCode,
                                         String proposedStorageBin, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupline/additionalBins")
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("itemCode", itemCode)
                    .queryParam("obOrdertypeId", obOrdertypeId)
                    .queryParam("proposedPackBarCode", proposedPackBarCode)
                    .queryParam("proposedStorageBin", proposedStorageBin);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Inventory[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, Inventory[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public PickupLine[] createPickupLine(@Valid List<AddPickupLine> newPickupLine, String loginUserID,
                                         String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "pickupline")
                .queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPickupLine, headers);
        ResponseEntity<PickupLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity,
                PickupLine[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - findPickupLine
    public PickupLine[] findPickupLine(SearchPickupLine searchPickupLine, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupline/findPickupLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPickupLine, headers);
            ResponseEntity<PickupLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PickupLine[].class);

            List<PickupLine> pickupLineList = new ArrayList<>();
            for (PickupLine pickupLine : result.getBody()) {
                if (pickupLine.getPickupCreatedOn() != null) {
                    pickupLine.setPickupCreatedOn(DateUtils.addTimeToDate(pickupLine.getPickupCreatedOn(), 3));
                }
                if (pickupLine.getPickupConfirmedOn() != null) {
                    pickupLine.setPickupConfirmedOn(DateUtils.addTimeToDate(pickupLine.getPickupConfirmedOn(), 3));
                }
                if (pickupLine.getPickupUpdatedOn() != null) {
                    pickupLine.setPickupUpdatedOn(DateUtils.addTimeToDate(pickupLine.getPickupUpdatedOn(), 3));
                }
                if (pickupLine.getPickupReversedOn() != null) {
                    pickupLine.setPickupReversedOn(DateUtils.addTimeToDate(pickupLine.getPickupReversedOn(), 3));
                }
                pickupLineList.add(pickupLine);
            }
            return pickupLineList.toArray(new PickupLine[pickupLineList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PickupLine updatePickupLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                       String partnerCode, Long lineNumber, String pickupNumber, String itemCode, String pickedStorageBin,
                                       String pickedPackCode, String actualHeNo, String loginUserID, @Valid PickupLine updatePickupLine,
                                       String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updatePickupLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupline/" + actualHeNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("pickupNumber", pickupNumber)
                    .queryParam("itemCode", itemCode).queryParam("pickedStorageBin", pickedStorageBin)
                    .queryParam("pickedPackCode", pickedPackCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<PickupLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    PickupLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePickupLine(String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode,
                                    Long lineNumber, String pickupNumber, String itemCode, String actualHeNo, String pickedStorageBin,
                                    String pickedPackCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "pickupline/" + actualHeNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("pickupNumber", pickupNumber)
                    .queryParam("itemCode", itemCode).queryParam("pickedStorageBin", pickedStorageBin)
                    .queryParam("pickedPackCode", pickedPackCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*-----------------------------QualityHeader---------------------------------------------------------*/

    // POST - CREATE QUALITY HEADER
    public QualityHeader createQualityHeader(@Valid QualityHeader newQualityHeader, String loginUserID,
                                             String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityheader")
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(newQualityHeader, headers);
            ResponseEntity<QualityHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, QualityHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findQualityHeader
    public QualityHeader[] findQualityHeader(SearchQualityHeader searchQualityHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityheader/findQualityHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchQualityHeader, headers);
            ResponseEntity<QualityHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, QualityHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<QualityHeader> obList = new ArrayList<>();
            for (QualityHeader obHeader : result.getBody()) {
                if (obHeader.getQualityConfirmedOn() != null) {
                    obHeader.setQualityConfirmedOn(DateUtils.addTimeToDate(obHeader.getQualityConfirmedOn(), 3));
                }
                if (obHeader.getQualityCreatedOn() != null) {
                    obHeader.setQualityCreatedOn(DateUtils.addTimeToDate(obHeader.getQualityCreatedOn(), 3));
                }
                if (obHeader.getQualityUpdatedOn() != null) {
                    obHeader.setQualityUpdatedOn(DateUtils.addTimeToDate(obHeader.getQualityUpdatedOn(), 3));
                }
                if (obHeader.getQualityReversedOn() != null) {
                    obHeader.setQualityReversedOn(DateUtils.addTimeToDate(obHeader.getQualityReversedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new QualityHeader[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findQualityHeader - Stream
    public QualityHeader[] findQualityHeaderNew(SearchQualityHeader searchQualityHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityheader/findQualityHeaderNew");
            HttpEntity<?> entity = new HttpEntity<>(searchQualityHeader, headers);
            ResponseEntity<QualityHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, QualityHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<QualityHeader> obList = new ArrayList<>();
            for (QualityHeader obHeader : result.getBody()) {
                if (obHeader.getQualityConfirmedOn() != null) {
                    obHeader.setQualityConfirmedOn(DateUtils.addTimeToDate(obHeader.getQualityConfirmedOn(), 3));
                }
                if (obHeader.getQualityCreatedOn() != null) {
                    obHeader.setQualityCreatedOn(DateUtils.addTimeToDate(obHeader.getQualityCreatedOn(), 3));
                }
                if (obHeader.getQualityUpdatedOn() != null) {
                    obHeader.setQualityUpdatedOn(DateUtils.addTimeToDate(obHeader.getQualityUpdatedOn(), 3));
                }
                if (obHeader.getQualityReversedOn() != null) {
                    obHeader.setQualityReversedOn(DateUtils.addTimeToDate(obHeader.getQualityReversedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new QualityHeader[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public QualityHeader updateQualityHeader(String warehouseId, String companyCodeId, String plantId, String languageId, String preOutboundNo, String refDocNumber,
                                             String partnerCode, String pickupNumber, String qualityInspectionNo, String actualHeNo, String loginUserID,
                                             @Valid QualityHeader updateQualityHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateQualityHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityheader/" + qualityInspectionNo)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("companyCodeId", companyCodeId).queryParam("plantId", plantId).queryParam("languageId", languageId)
                    .queryParam("pickupNumber", pickupNumber).queryParam("actualHeNo", actualHeNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<QualityHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, QualityHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteQualityHeader(String warehouseId, String companyCodeId, String plantId, String languageId,
                                       String preOutboundNo, String refDocNumber, String partnerCode, String pickupNumber,
                                       String qualityInspectionNo, String actualHeNo, String loginUserID,
                                       String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityheader/" + qualityInspectionNo)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("languageId", languageId)
                    .queryParam("plantId", plantId)
                    .queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode)
                    .queryParam("pickupNumber", pickupNumber)
                    .queryParam("actualHeNo", actualHeNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*-----------------------------QualityLine------------------------------------------------------------*/
    // POST - findQualityLine
    public QualityLine[] findQualityLine(SearchQualityLine searchQualityLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityline/findQualityLine");
            HttpEntity<?> entity = new HttpEntity<>(searchQualityLine, headers);
            ResponseEntity<QualityLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, QualityLine[].class);
            log.info("result : " + result.getStatusCode());

            List<QualityLine> obList = new ArrayList<>();
            for (QualityLine obHeader : result.getBody()) {
                if (obHeader.getQualityReversedOn() != null) {
                    obHeader.setQualityReversedOn(DateUtils.addTimeToDate(obHeader.getQualityReversedOn(), 3));
                }
                if (obHeader.getQualityCreatedOn() != null) {
                    obHeader.setQualityCreatedOn(DateUtils.addTimeToDate(obHeader.getQualityCreatedOn(), 3));
                }
                if (obHeader.getQualityUpdatedOn() != null) {
                    obHeader.setQualityUpdatedOn(DateUtils.addTimeToDate(obHeader.getQualityUpdatedOn(), 3));
                }
                if (obHeader.getQualityConfirmedOn() != null) {
                    obHeader.setQualityConfirmedOn(DateUtils.addTimeToDate(obHeader.getQualityConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new QualityLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST
    public QualityLine[] createQualityLine(@Valid List<AddQualityLine> newQualityLine, String loginUserID,
                                           String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityline").queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(newQualityLine, headers);
            ResponseEntity<QualityLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, QualityLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH
    public QualityLine updateQualityLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                         String partnerCode, Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID,
                                         @Valid QualityLine updateQualityLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateQualityLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityline/" + partnerCode)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("qualityInspectionNo", qualityInspectionNo)
                    .queryParam("itemCode", itemCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<QualityLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    QualityLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteQualityLine(String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode,
                                     Long lineNumber, String qualityInspectionNo, String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "qualityline/" + partnerCode)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("lineNumber", lineNumber).queryParam("qualityInspectionNo", qualityInspectionNo)
                    .queryParam("itemCode", itemCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * ----------------------OutboundHeader-----------------------------------------
     */
    // POST - findOutboundHeader
    public OutboundHeader[] findOutboundHeader(SearchOutboundHeader requestData, String authToken)
            throws ParseException {
        try {
            SearchOutboundHeaderModel requestDataForService = new SearchOutboundHeaderModel();
            BeanUtils.copyProperties(requestData, requestDataForService, CommonUtils.getNullPropertyNames(requestData));
            if (requestData.getStartDeliveryConfirmedOn() != null) {
                if (requestData.getStartDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getStartDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getStartDeliveryConfirmedOn()));
                }
            }
            Integer flag = 0;
            if (requestData.getEndDeliveryConfirmedOn() != null) {
                if (requestData.getEndDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getEndDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getEndDeliveryConfirmedOn()));
                    flag = 1;
                }
            }
            if (requestData.getStartOrderDate() != null) {
                requestDataForService
                        .setStartOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getStartOrderDate()));
            }
            if (requestData.getEndOrderDate() != null) {
                requestDataForService.setEndOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getEndOrderDate()));
            }
            if (requestData.getStartRequiredDeliveryDate() != null) {
                requestDataForService.setStartRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getStartRequiredDeliveryDate()));
            }
            if (requestData.getEndRequiredDeliveryDate() != null) {
                requestDataForService.setEndRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getEndRequiredDeliveryDate()));
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundheader/findOutboundHeader")
                    .queryParam("flag", flag);
            HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
            ResponseEntity<OutboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OutboundHeader[].class);
//			log.info("result : " + result.getBody());

            List<OutboundHeader> obList = new ArrayList<>();
            for (OutboundHeader obHeader : result.getBody()) {
                log.info("Result getDeliveryConfirmedOn :" + obHeader.getDeliveryConfirmedOn());
                if (obHeader.getRefDocDate() != null) {
                    obHeader.setRefDocDate(DateUtils.addTimeToDate(obHeader.getRefDocDate(), 3));
                }
                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }
                if (obHeader.getDeliveryConfirmedOn() != null) {
                    obHeader.setDeliveryConfirmedOn(DateUtils.addTimeToDate(obHeader.getDeliveryConfirmedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OutboundHeader[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findOutboundHeader
    public OutboundHeader[] findOutboundHeaderForRFD(SearchOutboundHeader requestData, String authToken)
            throws ParseException {
        try {
            SearchOutboundHeaderModel requestDataForService = new SearchOutboundHeaderModel();
            BeanUtils.copyProperties(requestData, requestDataForService, CommonUtils.getNullPropertyNames(requestData));
            if (requestData.getStartDeliveryConfirmedOn() != null) {
                if (requestData.getStartDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getStartDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getStartDeliveryConfirmedOn()));
                }
            }

            Integer flag = 0;
            if (requestData.getEndDeliveryConfirmedOn() != null) {
                if (requestData.getEndDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getEndDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getEndDeliveryConfirmedOn()));
                    flag = 1;
                }
            }

            if (requestData.getStartOrderDate() != null) {
                requestDataForService
                        .setStartOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getStartOrderDate()));
            }

            if (requestData.getEndOrderDate() != null) {
                requestDataForService.setEndOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getEndOrderDate()));
            }

            if (requestData.getStartRequiredDeliveryDate() != null) {
                requestDataForService.setStartRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getStartRequiredDeliveryDate()));
            }

            if (requestData.getEndRequiredDeliveryDate() != null) {
                requestDataForService.setEndRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getEndRequiredDeliveryDate()));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundheader/findOutboundHeader/rfd")
                    .queryParam("flag", flag);
            HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
            ResponseEntity<OutboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OutboundHeader[].class);

            List<OutboundHeader> obList = new ArrayList<>();
            for (OutboundHeader obHeader : result.getBody()) {
                log.info("Result getDeliveryConfirmedOn :" + obHeader.getDeliveryConfirmedOn());
                if (obHeader.getRefDocDate() != null) {
                    obHeader.setRefDocDate(DateUtils.addTimeToDate(obHeader.getRefDocDate(), 3));
                }

                if (obHeader.getRequiredDeliveryDate() != null) {
                    obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
                }

                if (obHeader.getDeliveryConfirmedOn() != null) {
                    obHeader.setDeliveryConfirmedOn(DateUtils.addTimeToDate(obHeader.getDeliveryConfirmedOn(), 3));
                }

                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }

                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OutboundHeader[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findOutboundHeadernew
    public OutboundHeader[] findOutboundHeaderNew(SearchOutboundHeader requestData, String authToken)
            throws ParseException {
        try {
            SearchOutboundHeaderModel requestDataForService = new SearchOutboundHeaderModel();
            BeanUtils.copyProperties(requestData, requestDataForService, CommonUtils.getNullPropertyNames(requestData));
            if (requestData.getStartDeliveryConfirmedOn() != null) {
                if (requestData.getStartDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getStartDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setStartDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getStartDeliveryConfirmedOn()));
                }
            }
            Integer flag = 0;
            if (requestData.getEndDeliveryConfirmedOn() != null) {
                if (requestData.getEndDeliveryConfirmedOn().length() < 11) {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToYYYYMMDD(requestData.getEndDeliveryConfirmedOn()));
                } else {
                    requestDataForService.setEndDeliveryConfirmedOn(
                            DateUtils.convertStringToDateWithTime(requestData.getEndDeliveryConfirmedOn()));
                    flag = 1;
                }
            }
            if (requestData.getStartOrderDate() != null) {
                requestDataForService
                        .setStartOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getStartOrderDate()));
            }
            if (requestData.getEndOrderDate() != null) {
                requestDataForService.setEndOrderDate(DateUtils.convertStringToYYYYMMDD(requestData.getEndOrderDate()));
            }
            if (requestData.getStartRequiredDeliveryDate() != null) {
                requestDataForService.setStartRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getStartRequiredDeliveryDate()));
            }
            if (requestData.getEndRequiredDeliveryDate() != null) {
                requestDataForService.setEndRequiredDeliveryDate(
                        DateUtils.convertStringToYYYYMMDD(requestData.getEndRequiredDeliveryDate()));
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundheader/findOutboundHeaderNew")
                    .queryParam("flag", flag);

            HttpEntity<?> entity = new HttpEntity<>(requestData, headers);
            ResponseEntity<OutboundHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OutboundHeader[].class);
            return result.getBody();

//			List<OutboundHeader> obList = new ArrayList<>();
//			for (OutboundHeader obHeader : result.getBody()) {
//				log.info("Result getDeliveryConfirmedOn :" + obHeader.getDeliveryConfirmedOn());
//				if(obHeader.getRefDocDate() != null) {
//					obHeader.setRefDocDate(DateUtils.addTimeToDate(obHeader.getRefDocDate(), 3));
//				}
//				if(obHeader.getRequiredDeliveryDate() != null) {
//					obHeader.setRequiredDeliveryDate(DateUtils.addTimeToDate(obHeader.getRequiredDeliveryDate(), 3));
//				}
//				if(obHeader.getDeliveryConfirmedOn() != null) {
//					obHeader.setDeliveryConfirmedOn(DateUtils.addTimeToDate(obHeader.getDeliveryConfirmedOn(), 3));
//				}
//				if(obHeader.getCreatedOn() != null) {
//					obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
//				}
//				if(obHeader.getUpdatedOn() != null) {
//					obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
//				}
//				obList.add(obHeader);
//			}
//			return obList.toArray(new OutboundHeader[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public OutboundHeader updateOutboundHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, @Valid OutboundHeader updateOutboundHeader, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOutboundHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundheader/" + preOutboundNo)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<OutboundHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, OutboundHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deleteOutboundHeader(String warehouseId, String preOutboundNo, String refDocNumber,
                                        String partnerCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundheader/" + preOutboundNo)
                    .queryParam("warehouseId", warehouseId).queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
     * ----------------------OutboundLine-------------------------------------------
     * ---------------
     */
    // GET - /outboundline/delivery/orderedLines
    public Long getCountofOrderedLines(String warehouseId, String preOutboundNo, String refDocNumber,
                                       String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/delivery/orderedLines")
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Long> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Long.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // GET - /outboundline/delivery/totalQuantity
    public Long getSumOfOrderedQty(String warehouseId, String preOutboundNo, String refDocNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/delivery/totalQuantity")
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Long> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Long.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // GET - /outboundline/delivery/deliveryLines
    public Long getDeliveryLines(String warehouseId, String preOutboundNo, String refDocNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/delivery/deliveryLines")
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Long> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Long.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // GET - /outboundline/delivery/confirmation
    public OutboundLine[] deliveryConfirmation(String warehouseId, String companyCodeId, String plantId, String languageId, String preOutboundNo, String refDocNumber,
                                               String partnerCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/delivery/confirmation")
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, OutboundLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findOutboundLine
    public OutboundLine[] findOutboundLine(SearchOutboundLine searchOutboundLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/findOutboundLine");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundLine, headers);
            ResponseEntity<OutboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OutboundLine[].class);
            log.info("result : " + result.getStatusCode());

            List<OutboundLine> obList = new ArrayList<>();
            for (OutboundLine obHeader : result.getBody()) {
                if (obHeader.getDeliveryConfirmedOn() != null) {
                    obHeader.setDeliveryConfirmedOn(DateUtils.addTimeToDate(obHeader.getDeliveryConfirmedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                if (obHeader.getReversedOn() != null) {
                    obHeader.setReversedOn(DateUtils.addTimeToDate(obHeader.getReversedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OutboundLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - findOutboundLine
    public OutboundLine[] findOutboundLineNew(SearchOutboundLine searchOutboundLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/findOutboundLine-new");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundLine, headers);
            ResponseEntity<OutboundLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OutboundLine[].class);
            log.info("result : " + result.getStatusCode());

            List<OutboundLine> obList = new ArrayList<>();
            for (OutboundLine obHeader : result.getBody()) {
                if (obHeader.getDeliveryConfirmedOn() != null) {
                    obHeader.setDeliveryConfirmedOn(DateUtils.addTimeToDate(obHeader.getDeliveryConfirmedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getUpdatedOn() != null) {
                    obHeader.setUpdatedOn(DateUtils.addTimeToDate(obHeader.getUpdatedOn(), 3));
                }
                if (obHeader.getReversedOn() != null) {
                    obHeader.setReversedOn(DateUtils.addTimeToDate(obHeader.getReversedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new OutboundLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - stock-movement-report-findOutboundLine
    public StockMovementReport[] findOutboundLineForStockMovement(SearchOutboundLine searchOutboundLine,
                                                                  String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/stock-movement-report/findOutboundLine");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundLine, headers);
            ResponseEntity<StockMovementReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, StockMovementReport[].class);
            log.info("result : " + result.getStatusCode());

            List<StockMovementReport> obList = new ArrayList<>();
            for (StockMovementReport obHeader : result.getBody()) {

                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new StockMovementReport[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - stock-movement-report-get all
//	public StockMovementReport[] getStockMovementReports(String authToken) throws ParseException {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "ClassicWMS RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//
//			UriComponentsBuilder builder = UriComponentsBuilder
//					.fromHttpUrl(getTransactionServiceApiUrl() + "reports/");
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<StockMovementReport[]> result = getRestTemplate().exchange(builder.toUriString(),
//					HttpMethod.GET, entity, StockMovementReport[].class);
//			log.info("result : " + result.getStatusCode());
//
//			List<StockMovementReport> obList = new ArrayList<>();
//			for (StockMovementReport obHeader : result.getBody()) {
//
//				if(obHeader.getConfirmedOn() != null) {
//					obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
//				}
//				obList.add(obHeader);
//			}
//			return obList.toArray(new StockMovementReport[obList.size()]);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
    public StockMovementReport[] getStockMovementReports(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/new");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StockMovementReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, StockMovementReport[].class);
            log.info("result : " + result.getStatusCode());

            List<StockMovementReport> obList = new ArrayList<>();
            for (StockMovementReport obHeader : result.getBody()) {

                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new StockMovementReport[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - Opening Stock Report renamed to Transaction History Report
    public InventoryStockReport[] getTransactionHistoryReport(FindImBasicData1 findImBasicData1, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/transactionHistoryReport");

            HttpEntity<?> entity = new HttpEntity<>(findImBasicData1, headers);
            ResponseEntity<InventoryStockReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, InventoryStockReport[].class);
            log.info("result : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH ----
    public OutboundLine updateOutboundLine(String warehouseId, String preOutboundNo, String refDocNumber,
                                           String companyCodeId, String plantId, String languageId,
                                           String partnerCode, Long lineNumber, String itemCode, String loginUserID,
                                           @Valid OutboundLine updateOutboundLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOutboundLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/" + lineNumber)
                    .queryParam("warehouseId", warehouseId)
                    .queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("partnerCode", partnerCode)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<OutboundLine> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity,
                    OutboundLine.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // PATCH ----
    public com.tekclover.wms.core.model.transaction.OutboundLine[] updateOutboundLines(String loginUserID,
                                                                                       @Valid List<UpdateOutboundLine> updateOutboundLine, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updateOutboundLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/lineNumbers")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<OutboundLine[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH, entity, OutboundLine[].class);
            log.info("result : " + result);
            return result.getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    // DELETE
    public boolean deleteOutboundLine(String warehouseId, String companyCodeId, String plantId, String languageId, String preOutboundNo, String refDocNumber, String partnerCode,
                                      Long lineNumber, String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/" + lineNumber)
                    .queryParam("warehouseId", warehouseId).queryParam("preOutboundNo", preOutboundNo)
                    .queryParam("refDocNumber", refDocNumber).queryParam("partnerCode", partnerCode)
                    .queryParam("companyCodeId", companyCodeId).queryParam("plantId", plantId).queryParam("languageId", languageId)
                    .queryParam("itemCode", itemCode).queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
     * ---------------------------------OutboundReversal----------------------------
     * -----------------------
     */
    // POST - findOutboundReversal
    public OutboundReversal[] findOutboundReversal(SearchOutboundReversal searchOutboundReversal, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundreversal/findOutboundReversal");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundReversal, headers);
            ResponseEntity<OutboundReversal[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, OutboundReversal[].class);
            log.info("result : " + result.getStatusCode());

            List<OutboundReversal> obList = new ArrayList<>();
            for (OutboundReversal obHeader : result.getBody()) {
                if (obHeader.getReversedOn() != null) {
                    obHeader.setReversedOn(DateUtils.addTimeToDate(obHeader.getReversedOn(), 3));
                }

                obList.add(obHeader);
            }
            return obList.toArray(new OutboundReversal[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // POST - findOutboundReversal - Stream
    public OutboundReversal[] findOutboundReversalNew(SearchOutboundReversal searchOutboundReversal, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundreversal/findOutboundReversalNew");
            HttpEntity<?> entity = new HttpEntity<>(searchOutboundReversal, headers);
            ResponseEntity<OutboundReversal[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, OutboundReversal[].class);
            log.info("result : " + result.getStatusCode());

            List<OutboundReversal> obList = new ArrayList<>();
            for (OutboundReversal obHeader : result.getBody()) {
                if (obHeader.getReversedOn() != null) {
                    obHeader.setReversedOn(DateUtils.addTimeToDate(obHeader.getReversedOn(), 3));
                }

                obList.add(obHeader);
            }
            return obList.toArray(new OutboundReversal[obList.size()]);

        } catch (Exception e) {
            throw e;
        }
    }

    // GET
    public OutboundReversal[] doReversal(String refDocNumber, String companyCodeId, String plantId,
                                         String languageId, String itemCode, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "outboundline/reversal/new")
                    .queryParam("refDocNumber", refDocNumber)
                    .queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId)
                    .queryParam("languageId", languageId)
                    .queryParam("itemCode", itemCode)
                    .queryParam("loginUserID", loginUserID);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundReversal[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, OutboundReversal[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*----------------------------------REPORTS----------------------------------------------------------*/

    // GET - STOCK REPORT
    public PaginatedResponse<StockReport> getStockReports(List<String> warehouseId, List<String> itemCode,
                                                          String itemText, String stockTypeText, Integer pageNo, Integer pageSize, String sortBy, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/stockReport")
                    .queryParam("warehouseId", warehouseId).queryParam("itemCode", itemCode)
                    .queryParam("itemText", itemText).queryParam("stockTypeText", stockTypeText)
                    .queryParam("pageNo", pageNo).queryParam("pageSize", pageSize).queryParam("sortBy", sortBy);
            HttpEntity<?> entity = new HttpEntity<>(headers);

//			ResponseEntity<StockReport[]> result =
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockReport[].class);

            ParameterizedTypeReference<PaginatedResponse<StockReport>> responseType = new ParameterizedTypeReference<PaginatedResponse<StockReport>>() {
            };
            ResponseEntity<PaginatedResponse<StockReport>> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, responseType);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public StockReport[] getAllStockReports(List<String> languageId, List<String> companyCodeId, List<String> plantId, List<String> warehouseId,
                                            List<String> itemCode, String itemText, String stockTypeText, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "reports/stockReport-all")
                    .queryParam("languageId", languageId).queryParam("companyCodeId", companyCodeId).queryParam("plantId", plantId)
                    .queryParam("warehouseId", warehouseId).queryParam("itemCode", itemCode)
                    .queryParam("itemText", itemText).queryParam("stockTypeText", stockTypeText);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StockReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, StockReport[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - INVENTORY REPORT
    public PaginatedResponse<InventoryReport> getInventoryReport(List<String> warehouseId, List<String> itemCode,
                                                                 String storageBin, String stockTypeText, List<String> stSectionIds, Integer pageNo, Integer pageSize,
                                                                 String sortBy, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/inventoryReport")
                    .queryParam("warehouseId", warehouseId).queryParam("itemCode", itemCode)
                    .queryParam("storageBin", storageBin).queryParam("stockTypeText", stockTypeText)
                    .queryParam("stSectionIds", stSectionIds).queryParam("pageNo", pageNo)
                    .queryParam("pageSize", pageSize).queryParam("sortBy", sortBy);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ParameterizedTypeReference<PaginatedResponse<InventoryReport>> responseType = new ParameterizedTypeReference<PaginatedResponse<InventoryReport>>() {
            };
            ResponseEntity<PaginatedResponse<InventoryReport>> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);

//			ResponseEntity<InventoryReport[]> result =
//					getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity, InventoryReport[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - StockMovementReport
    public StockMovementReport[] getStockMovementReport(String warehouseId, String itemCode, String fromCreatedOn,
                                                        String toCreatedOn, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/stockMovementReport")
                    .queryParam("warehouseId", warehouseId).queryParam("itemCode", itemCode)
                    .queryParam("fromCreatedOn", fromCreatedOn).queryParam("toCreatedOn", toCreatedOn);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<StockMovementReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, StockMovementReport[].class);

            List<StockMovementReport> stockMovementReportList = new ArrayList<>();
            for (StockMovementReport stockMovementReport : result.getBody()) {
                if (stockMovementReport.getConfirmedOn() != null) {
                    stockMovementReport
                            .setConfirmedOn(DateUtils.addTimeToDate(stockMovementReport.getConfirmedOn(), 3));
                    stockMovementReportList.add(stockMovementReport);
                }
            }
            return stockMovementReportList.toArray(new StockMovementReport[stockMovementReportList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - OrderStatusReport
    public OrderStatusReport[] getOrderStatusReport(SearchOrderStatusReport requestData, String authToken)
            throws ParseException {
        try {
            SearchOrderStatusModel requestDataForService = new SearchOrderStatusModel();
            BeanUtils.copyProperties(requestData, requestDataForService, CommonUtils.getNullPropertyNames(requestData));
            if (requestData.getFromDeliveryDate() != null) {
                requestDataForService
                        .setFromDeliveryDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDeliveryDate()));
            }
            if (requestData.getToDeliveryDate() != null) {
                requestDataForService
                        .setToDeliveryDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDeliveryDate()));
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/orderStatusReport");
            HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
            ResponseEntity<OrderStatusReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, OrderStatusReport[].class);

            List<OrderStatusReport> orderStatusReportList = new ArrayList<>();
            for (OrderStatusReport orderStatusReport : result.getBody()) {
                if (orderStatusReport.getDeliveryConfirmedOn() != null) {
                    orderStatusReport.setDeliveryConfirmedOn(
                            DateUtils.addTimeToDate(orderStatusReport.getDeliveryConfirmedOn(), 3));
                }

                if (orderStatusReport.getOrderReceivedDate() != null) {
                    orderStatusReport
                            .setOrderReceivedDate(DateUtils.addTimeToDate(orderStatusReport.getOrderReceivedDate(), 3));
                }

                if (orderStatusReport.getExpectedDeliveryDate() != null) {
                    orderStatusReport.setExpectedDeliveryDate(
                            DateUtils.addTimeToDate(orderStatusReport.getExpectedDeliveryDate(), 3));
                }
                orderStatusReportList.add(orderStatusReport);
            }
            log.info("orderStatusReportList--------> : " + orderStatusReportList);
            return orderStatusReportList.toArray(new OrderStatusReport[orderStatusReportList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - ShipmentDelivery
    public ShipmentDeliveryReport[] getShipmentDeliveryReport(String warehouseId, String fromDeliveryDate,
                                                              String toDeliveryDate, String storeCode, List<String> soType, String orderNumber, String authToken)
            throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/shipmentDelivery")
                    .queryParam("warehouseId", warehouseId).queryParam("fromDeliveryDate", fromDeliveryDate)
                    .queryParam("toDeliveryDate", toDeliveryDate).queryParam("storeCode", storeCode)
                    .queryParam("orderNumber", orderNumber).queryParam("soType", soType);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ShipmentDeliveryReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ShipmentDeliveryReport[].class);

            List<ShipmentDeliveryReport> shipmentDeliveryReportList = new ArrayList<>();
            for (ShipmentDeliveryReport shipmentDeliveryReport : result.getBody()) {
                if (shipmentDeliveryReport.getDeliveryDate() != null) {
                    shipmentDeliveryReport
                            .setDeliveryDate(DateUtils.addTimeToDate(shipmentDeliveryReport.getDeliveryDate(), 3));
                    shipmentDeliveryReportList.add(shipmentDeliveryReport);
                }
            }

            return shipmentDeliveryReportList.toArray(new ShipmentDeliveryReport[shipmentDeliveryReportList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - ShipmentDeliverySummary
    public ShipmentDeliverySummaryReport getShipmentDeliverySummaryReport(String fromDeliveryDate,
                                                                          String toDeliveryDate, List<String> customerCode, String warehouseId, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/shipmentDeliverySummary")
                    .queryParam("fromDeliveryDate", fromDeliveryDate).queryParam("toDeliveryDate", toDeliveryDate)
                    .queryParam("customerCode", customerCode).queryParam("warehouseId", warehouseId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ShipmentDeliverySummaryReport> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ShipmentDeliverySummaryReport.class);
            ShipmentDeliverySummaryReport summaryReport = result.getBody();

            for (ShipmentDeliverySummary shipmentDeliverySummary : summaryReport.getShipmentDeliverySummary()) {
                if (shipmentDeliverySummary.getExpectedDeliveryDate() != null) {
                    shipmentDeliverySummary.setExpectedDeliveryDate(
                            DateUtils.addTimeToDate(shipmentDeliverySummary.getExpectedDeliveryDate(), 3));
                }

                if (shipmentDeliverySummary.getDeliveryDateTime() != null) {
                    shipmentDeliverySummary.setDeliveryDateTime(
                            DateUtils.addTimeToDate(shipmentDeliverySummary.getDeliveryDateTime(), 3));
                }
            }
            return summaryReport;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - ShipmentDispatchSummary
    public ShipmentDispatchSummaryReport getShipmentDispatchSummaryReport(String fromDeliveryDate,
                                                                          String toDeliveryDate, List<String> customerCode, String warehouseId, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/shipmentDispatchSummary")
                    .queryParam("fromDeliveryDate", fromDeliveryDate).queryParam("toDeliveryDate", toDeliveryDate)
                    .queryParam("customerCode", customerCode).queryParam("warehouseId", warehouseId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ShipmentDispatchSummaryReport> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ShipmentDispatchSummaryReport.class);
            ShipmentDispatchSummaryReport summaryReport = result.getBody();
            for (ShipmentDispatch shipmentDispatch : summaryReport.getShipmentDispatch()) {
                for (ShipmentDispatchList list : shipmentDispatch.getShipmentDispatchList()) {
                    if (list.getOrderReceiptTime() != null) {
                        list.setOrderReceiptTime(DateUtils.addTimeToDate(list.getOrderReceiptTime(), 3));
                    }
                }
            }
            return summaryReport;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - ReceiptConfimation
    public ReceiptConfimationReport getReceiptConfimationReport(String asnNumber, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/receiptConfirmation")
                    .queryParam("asnNumber", asnNumber);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<ReceiptConfimationReport> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, ReceiptConfimationReport.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - MpbileDashboard
    public MobileDashboard getMobileDashboard(String warehouseId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/dashboard/mobile")
                    .queryParam("warehouseId", warehouseId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<MobileDashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, MobileDashboard.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - Opening Stock Report
    public InventoryStockReport[] getInventoryStockReport(FindImBasicData1 findImBasicData1, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/inventoryStock");

            HttpEntity<?> entity = new HttpEntity<>(findImBasicData1, headers);
            ResponseEntity<InventoryStockReport[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, InventoryStockReport[].class);
            log.info("result : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ---------------------------------PerpetualHeader----------------------------------------------------
    // GET ALL
    public PerpetualHeader[] getPerpetualHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PerpetualHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PerpetualHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PerpetualHeader> obList = new ArrayList<>();
            for (PerpetualHeader obHeader : result.getBody()) {

                obList.add(addingTimeWithDatePerpetualHeader(obHeader));
            }
            return obList.toArray(new PerpetualHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PerpetualHeader addingTimeWithDatePerpetualHeader(PerpetualHeader obHeader) throws ParseException {

        if (obHeader.getCountedOn() != null) {
            obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
        }
        if (obHeader.getCreatedOn() != null) {
            obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
        }
        if (obHeader.getConfirmedOn() != null) {
            obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
        }

        return obHeader;
    }

    public PerpetualHeader getPerpetualHeader(String warehouseId, String companyCodeId, String plantId, String languageId, Long cycleCountTypeId, String cycleCountNo,
                                              Long movementTypeId, Long subMovementTypeId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId).queryParam("languageId", languageId)
                    .queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("movementTypeId", movementTypeId).queryParam("subMovementTypeId", subMovementTypeId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PerpetualHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PerpetualHeader.class);
            log.info("result : " + result.getStatusCode());

            return addingTimeWithDatePerpetualHeader(result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND ALL - findPerpetualHeader
    public PerpetualHeaderEntity[] findPerpetualHeader(SearchPerpetualHeader searchPerpetualHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/findPerpetualHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPerpetualHeader, headers);
            ResponseEntity<PerpetualHeaderEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, PerpetualHeaderEntity[].class);
            log.info("result : " + result.getStatusCode());

            List<PerpetualHeaderEntity> obList = new ArrayList<>();
            for (PerpetualHeaderEntity obHeader : result.getBody()) {
                if (obHeader.getCountedOn() != null) {
                    obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PerpetualHeaderEntity[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - CREATE
    public PerpetualHeader createPerpetualHeader(@Valid AddPerpetualHeader newPerpetualHeader, String loginUserID,
                                                 String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader").queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPerpetualHeader, headers);
        ResponseEntity<PerpetualHeader> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PerpetualHeader.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - RUN
    public PerpetualLineEntity[] runPerpetualHeader(@Valid RunPerpetualHeader runPerpetualHeader, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/run");
        HttpEntity<?> entity = new HttpEntity<>(runPerpetualHeader, headers);
        ResponseEntity<PerpetualLineEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, PerpetualLineEntity[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - RUN - Stream
    public PerpetualLineEntity[] runPerpetualHeaderNew(@Valid RunPerpetualHeader runPerpetualHeader, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/runStream");
        HttpEntity<?> entity = new HttpEntity<>(runPerpetualHeader, headers);
        ResponseEntity<PerpetualLineEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                HttpMethod.POST, entity, PerpetualLineEntity[].class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // PATCH
    public PerpetualHeader updatePerpetualHeader(String warehouseId, String companyCodeId, String plantId, String languageId, Long cycleCountTypeId, String cycleCountNo,
                                                 Long movementTypeId, Long subMovementTypeId, String loginUserID,
                                                 @Valid UpdatePerpetualHeader updatePerpetualHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            HttpEntity<?> entity = new HttpEntity<>(updatePerpetualHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId).queryParam("languageId", languageId).queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("movementTypeId", movementTypeId).queryParam("subMovementTypeId", subMovementTypeId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PerpetualHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PerpetualHeader.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePerpetualHeader(String warehouseId, String companyCodeId, String plantId, String languageId, Long cycleCountTypeId, String cycleCountNo,
                                         Long movementTypeId, Long subMovementTypeId, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId)
                    .queryParam("plantId", plantId).queryParam("languageId", languageId).queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("movementTypeId", movementTypeId).queryParam("subMovementTypeId", subMovementTypeId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND ALL - findPerpetualLine
    public PerpetualLine[] findPerpetualLine(SearchPerpetualLine searchPerpetualLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualline/findPerpetualLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPerpetualLine, headers);
            ResponseEntity<PerpetualLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PerpetualLine[].class);
            log.info("result : " + result.getBody());

            List<PerpetualLine> obList = new ArrayList<>();
            for (PerpetualLine obHeader : result.getBody()) {
                if (obHeader.getCountedOn() != null) {
                    obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PerpetualLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND ALL - findPerpetualLineStream
    public PerpetualLine[] findPerpetualLineStream(SearchPerpetualLine searchPerpetualLine, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualline/findPerpetualLineStream");
            HttpEntity<?> entity = new HttpEntity<>(searchPerpetualLine, headers);
            ResponseEntity<PerpetualLine[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                    entity, PerpetualLine[].class);
            log.info("result : " + result.getBody());

            List<PerpetualLine> obList = new ArrayList<>();
            for (PerpetualLine obHeader : result.getBody()) {
                if (obHeader.getCountedOn() != null) {
                    obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PerpetualLine[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PerpetualLine[] updateAssingHHTUser(List<AssignHHTUserCC> assignHHTUser, String loginUserID,
                                               String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(assignHHTUser, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualline/assigingHHTUser")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PerpetualLine[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PerpetualLine[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PerpetualUpdateResponse updatePerpetualLine(String cycleCountNo,
                                                       List<UpdatePerpetualLine> updatePerpetualLine, String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(updatePerpetualLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "perpetualline/" + cycleCountNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PerpetualUpdateResponse> result = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.PATCH, entity, PerpetualUpdateResponse.class);
            log.info("result : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // ---------------------------------PeriodicHeader----------------------------------------------------
    // GET ALL
    public PeriodicHeaderEntity[] getPeriodicHeaders(String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PeriodicHeaderEntity[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, PeriodicHeaderEntity[].class);
            log.info("result : " + result.getStatusCode());

            List<PeriodicHeaderEntity> obList = new ArrayList<>();
            for (PeriodicHeaderEntity obHeader : result.getBody()) {

                obList.add(addingTimeWithDatePeriodicHeaderEntity(obHeader));
            }
            return obList.toArray(new PeriodicHeaderEntity[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Add Time to Date plus 3 Hours
    public PeriodicHeaderEntity addingTimeWithDatePeriodicHeaderEntity(PeriodicHeaderEntity obHeader) throws ParseException {

        if (obHeader.getConfirmedOn() != null) {
            obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
        }
        if (obHeader.getCreatedOn() != null) {
            obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
        }
        if (obHeader.getCountedOn() != null) {
            obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
        }

        return obHeader;
    }

    // GET
    public PeriodicHeader[] getPeriodicHeader(String warehouseId, Long cycleCountTypeId, String cycleCountNo,
                                              Long movementTypeId, Long subMovementTypeId, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("movementTypeId", movementTypeId).queryParam("subMovementTypeId", subMovementTypeId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PeriodicHeader[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, PeriodicHeader[].class);
            log.info("result : " + result.getStatusCode());

            List<PeriodicHeader> obList = new ArrayList<>();
            for (PeriodicHeader obHeader : result.getBody()) {
                if (obHeader.getConfirmedOn() != null) {
                    obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
                }
                if (obHeader.getCreatedOn() != null) {
                    obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
                }
                if (obHeader.getCountedOn() != null) {
                    obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
                }
                obList.add(obHeader);
            }
            return obList.toArray(new PeriodicHeader[obList.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND ALL - findPeriodicHeader
    public PeriodicHeaderEntity[] findPeriodicHeader(SearchPeriodicHeader searchPeriodicHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/findPeriodicHeader");
            HttpEntity<?> entity = new HttpEntity<>(searchPeriodicHeader, headers);
            ResponseEntity<PeriodicHeaderEntity[]> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, entity, PeriodicHeaderEntity[].class);

            List<PeriodicHeaderEntity> obList = new ArrayList<>();
            for (PeriodicHeaderEntity obHeader : result.getBody()) {
                obList.add(addingTimeWithDatePeriodicHeaderEntity(obHeader));
            }
            return obList.toArray(new PeriodicHeaderEntity[obList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND ALL - findPeriodicHeader -Stream
    public PeriodicHeader[] findPeriodicHeaderStream(SearchPeriodicHeader searchPeriodicHeader, String authToken) throws ParseException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/findPeriodicHeaderStream");
            HttpEntity<?> entity = new HttpEntity<>(searchPeriodicHeader, headers);
            ResponseEntity<PeriodicHeader[]> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, entity, PeriodicHeader[].class);

            Arrays.stream(result.getBody()).forEach(n -> {
                        if (n.getConfirmedOn() != null) {
                            try {
                                n.setConfirmedOn(DateUtils.addTimeToDate(n.getConfirmedOn(), 3));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (n.getCreatedOn() != null) {
                            try {
                                n.setCreatedOn(DateUtils.addTimeToDate(n.getCreatedOn(), 3));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (n.getCountedOn() != null) {
                            try {
                                n.setCountedOn(DateUtils.addTimeToDate(n.getCountedOn(), 3));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
//			List<PeriodicHeader> obList = new ArrayList<>();
//			for (PeriodicHeader obHeader : result.getBody()) {
//
//				if(obHeader.getConfirmedOn() != null) {
//					obHeader.setConfirmedOn(DateUtils.addTimeToDate(obHeader.getConfirmedOn(), 3));
//				}
//				if(obHeader.getCreatedOn() != null) {
//					obHeader.setCreatedOn(DateUtils.addTimeToDate(obHeader.getCreatedOn(), 3));
//				}
//				if(obHeader.getCountedOn() != null) {
//					obHeader.setCountedOn(DateUtils.addTimeToDate(obHeader.getCountedOn(), 3));
//				}
//
//				obList.add(obHeader);
//			}
//			return obList.toArray(new PeriodicHeader[obList.size()]);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // POST - CREATE
    public PeriodicHeaderEntity createPeriodicHeader(@Valid AddPeriodicHeader newPeriodicHeader, String loginUserID,
                                                     String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader").queryParam("loginUserID", loginUserID);
        HttpEntity<?> entity = new HttpEntity<>(newPeriodicHeader, headers);
        ResponseEntity<PeriodicHeaderEntity> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, PeriodicHeaderEntity.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // GET ALL
    public Page<?> runPeriodicHeader(String warehouseId, Integer pageNo, Integer pageSize, String sortBy,
                                     String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("ClientGeneral-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/run/pagination")
                    .queryParam("pageNo", pageNo).queryParam("pageSize", pageSize).queryParam("sortBy", sortBy)
                    .queryParam("warehouseId", warehouseId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ParameterizedTypeReference<PaginatedResponse<PeriodicLineEntity>> responseType =
                    new ParameterizedTypeReference<PaginatedResponse<PeriodicLineEntity>>() {
                    };
            ResponseEntity<PaginatedResponse<PeriodicLineEntity>> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, entity, responseType);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PeriodicHeader updatePeriodicHeader(String warehouseId, String companyCodeId, String plantId, String languageId, Long cycleCountTypeId, String cycleCountNo,
                                               String loginUserID, UpdatePeriodicHeader updatePeriodicHeader, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(updatePeriodicHeader, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId).queryParam("plantId", plantId)
                    .queryParam("languageId", languageId).queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("loginUserID", loginUserID);

            ResponseEntity<PeriodicHeader> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PeriodicHeader.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DELETE
    public boolean deletePeriodicHeader(String warehouseId, String companyCodeId, String plantId, String languageId, Long cycleCountTypeId, String cycleCountNo,
                                        String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicheader/" + cycleCountNo)
                    .queryParam("warehouseId", warehouseId).queryParam("companyCodeId", companyCodeId).queryParam("languageId", languageId)
                    .queryParam("plantId", plantId).queryParam("cycleCountTypeId", cycleCountTypeId)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.DELETE, entity,
                    String.class);
            log.info("result : " + result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //-------------------------------------PeriodicLine--------------------------------------------------------
    // FIND
    public PeriodicLine[] findPeriodicLine(SearchPeriodicLine searchPeriodicLine, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicline/findPeriodicLine");
            HttpEntity<?> entity = new HttpEntity<>(searchPeriodicLine, headers);
            ResponseEntity<PeriodicLine[]> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, entity, PeriodicLine[].class);
            List<PeriodicLine> periodicLineList = new ArrayList<>();
            for (PeriodicLine periodicLine : result.getBody()) {
                periodicLine.setCreatedOn(DateUtils.addTimeToDate(periodicLine.getCreatedOn(), 3));
                periodicLineList.add(periodicLine);
            }
            return periodicLineList.toArray(new PeriodicLine[periodicLineList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FIND - Stream
    public PeriodicLine[] findPeriodicLineNew(SearchPeriodicLine searchPeriodicLine, String authToken) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicline/findPeriodicLineStream");
            HttpEntity<?> entity = new HttpEntity<>(searchPeriodicLine, headers);
            ResponseEntity<PeriodicLine[]> result = getRestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, entity, PeriodicLine[].class);
            List<PeriodicLine> periodicLineList = new ArrayList<>();
            for (PeriodicLine periodicLine : result.getBody()) {
                periodicLine.setCreatedOn(DateUtils.addTimeToDate(periodicLine.getCreatedOn(), 3));
                periodicLineList.add(periodicLine);
            }
            return periodicLineList.toArray(new PeriodicLine[periodicLineList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PeriodicLine[] updatePeriodicLineAssingHHTUser(List<AssignHHTUserCC> assignHHTUser, String loginUserID,
                                                          String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(assignHHTUser, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicline/assigingHHTUser")
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PeriodicLine[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PeriodicLine[].class);
            log.info("result : " + result);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH
    public PeriodicUpdateResponse updatePeriodicLine(String cycleCountNo, List<UpdatePeriodicLine> updatePeriodicLine,
                                                     String loginUserID, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            HttpEntity<?> entity = new HttpEntity<>(updatePeriodicLine, headers);
            HttpClient client = HttpClients.createDefault();
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "periodicline/" + cycleCountNo)
                    .queryParam("loginUserID", loginUserID);
            ResponseEntity<PeriodicUpdateResponse> result = restTemplate.exchange(builder.toUriString(), HttpMethod.PATCH,
                    entity, PeriodicUpdateResponse.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param authToken
     */
    public void getInventoryReport(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/inventoryReport/schedule");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InventoryReport[] generateInventoryReport(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/inventoryReport/all");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InventoryReport[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InventoryReport[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//	/**
//	 * sendEmail
//	 * @param email
//	 * @param authToken
//	 * @return
//	 */
//	public String sendEmail (MultipartFile file,String authToken) throws Exception {
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			headers.add("User-Agent", "ClassicWMS RestTemplate");
//			headers.add("Authorization", "Bearer " + authToken);
//
//			MultiValueMap<String, Object> body
//					= new LinkedMultiValueMap<>();
//			body.add("file", file);
//
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body,headers);
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "/sendmail/attachment").queryParam("file", file);
//			ResponseEntity<String> result = getRestTemplate().postForEntity(builder.toUriString(), entity, String.class);
//			log.info("result : " + result.getStatusCode());
//			return result.getBody();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @param warehouseId
     * @param authToken
     * @return
     */
    public Dashboard getDashboardCount(String warehouseId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/dashboard/get-count")
                    .queryParam("warehouseId", warehouseId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Dashboard> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
                    Dashboard.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public FastSlowMovingDashboard[] getFastSlowMovingDashboard(FastSlowMovingDashboardRequest requestData,
                                                                String authToken) throws ParseException {
        try {
            FastSlowMovingDashboardRequestModel requestDataForService = new FastSlowMovingDashboardRequestModel();
            BeanUtils.copyProperties(requestData, requestDataForService, CommonUtils.getNullPropertyNames(requestData));
            if (requestData.getFromDate() != null) {
                requestDataForService.setFromDate(DateUtils.convertStringToYYYYMMDD(requestData.getFromDate()));
            }
            if (requestData.getToDate() != null) {
                requestDataForService.setToDate(DateUtils.convertStringToYYYYMMDD(requestData.getToDate()));
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "reports/dashboard/get-fast-slow-moving");

            HttpEntity<?> entity = new HttpEntity<>(requestDataForService, headers);
            ResponseEntity<FastSlowMovingDashboard[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, FastSlowMovingDashboard[].class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // -----------------------------------------Orders----------------------------------------------------------------
    // POST - SO
    public WarehouseApiResponse postASN(@Valid ASN asn, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/asn");
        HttpEntity<?> entity = new HttpEntity<>(asn, headers);
        ResponseEntity<WarehouseApiResponse> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    // POST - SO Return V2
    public WarehouseApiResponse postSOReturn(@Valid SaleOrderReturn saleOrderReturn, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/soReturn");
        HttpEntity<?> entity = new HttpEntity<>(saleOrderReturn, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }

    // POST - B2bTransferIn
    public WarehouseApiResponse postB2bTransferIn(@Valid B2bTransferIn b2bTransferIn, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/b2bTransferIn");
        HttpEntity<?> entity = new HttpEntity<>(b2bTransferIn, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    // POST - InterWarehouseTransferInV2
    public WarehouseApiResponse postInterWarehouseTransferIn(@Valid InterWarehouseTransferIn interWarehouseTransferIn, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/inbound/interWarehouseTransferIn");
        HttpEntity<?> entity = new HttpEntity<>(interWarehouseTransferIn, headers);
        ResponseEntity<WarehouseApiResponse> result =
                getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    //OUTBOUND

    // POST - SO
    public WarehouseApiResponse postSO(@Valid ShipmentOrder shipmenOrder, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "ClassicWMS RestTemplate");
        headers.add("Authorization", "Bearer " + authToken);
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(getTransactionServiceApiUrl() + "warehouse/outbound/so");
        HttpEntity<?> entity = new HttpEntity<>(shipmenOrder, headers);
        ResponseEntity<WarehouseApiResponse> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,
                entity, WarehouseApiResponse.class);
        log.info("result : " + result.getStatusCode());
        return result.getBody();
    }


    // GET - InboundOrder - OrderByID
    public InboundOrder getInboundOrderById(String orderId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/inbound/orders/" + orderId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundOrder> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundOrder.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - OutboundOrder - OrderByID
    public OutboundOrder getOutboundOrdersById(String orderId, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/outbound/orders/" + orderId);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundOrder> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, OutboundOrder.class);
            log.info("result : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - OutboundOrder - OrderByDate
    public OutboundOrder[] getOBOrderByDate(String orderStartDate, String orderEndDate, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/outbound/orders/byDate")
                    .queryParam("orderStartDate", orderStartDate).queryParam("orderEndDate", orderEndDate);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundOrder[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, OutboundOrder[].class);
            log.info("result : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - FAILED
    public InboundIntegrationLog[] getFailedInboundOrders(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/inbound/orders/failed");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundIntegrationLog[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, InboundIntegrationLog[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET - FAILED
    public OutboundIntegrationLog[] getFailedOutboundOrders(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/outbound/orders/failed");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundIntegrationLog[]> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.GET, entity, OutboundIntegrationLog[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InboundOrder[] getInboundOrders(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/inbound");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<InboundOrder[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, InboundOrder[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // GET
    public OutboundOrder[] getOBOrders(String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "ClassicWMS RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(getTransactionServiceApiUrl() + "orders/outbound");
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<OutboundOrder[]> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                    entity, OutboundOrder[].class);
            log.info("result : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //---------------Periodic-CSV-Writer----------------------------------------

    /**
     * @param newPeriodicLines
     * @throws IOException
     */
    public void createCSV(List<PeriodicLine> newPeriodicLines) throws IOException {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get("periodicLine.csv"));
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
        ) {
            String[] headerRecord = {"languageId", "companyCode", "plantId", "warehouseId", "cycleCountNo", "storageBin",
                    "itemCode", "packBarcodes", "variantCode", "variantSubCode", "batchSerialNumber", "stockTypeId", "specialStockIndicator",
                    "inventoryQuantity", "inventoryUom", "countedQty", "varianceQty", "cycleCounterId", "cycleCounterName", "statusId",
                    "cycleCountAction", "referenceNo", "approvalProcessId", "approvalLevel", "approverCode", "approvalStatus", "remarks",
                    "referenceField1", "referenceField2", "referenceField3", "referenceField4", "referenceField5", "referenceField6",
                    "referenceField7", "referenceField8", "referenceField9", "referenceField10", "deletionIndicator", "createdBy", "createdOn",
                    "confirmedBy", "confirmedOn", "countedBy", "countedOn"};
            csvWriter.writeNext(headerRecord);

            List<String[]> listArr = new ArrayList<>();
            newPeriodicLines.stream().forEach(pl -> {
                String[] sarr = toArray(pl);
                listArr.add(sarr);
            });

            csvWriter.writeAll(listArr);
        }
    }

    /**
     * @param periodicLine
     * @return
     */
    private String[] toArray(PeriodicLine periodicLine) {
        String[] strarr = new String[]{
                periodicLine.getLanguageId(),
                periodicLine.getCompanyCode(),
                periodicLine.getPlantId(),
                periodicLine.getWarehouseId(),
                periodicLine.getCycleCountNo(),
                periodicLine.getStorageBin(),
                periodicLine.getItemCode(),
                periodicLine.getPackBarcodes(),
                String.valueOf(periodicLine.getVariantCode()),
                periodicLine.getVariantSubCode(),
                periodicLine.getBatchSerialNumber(),
                String.valueOf(periodicLine.getStockTypeId()),
                periodicLine.getSpecialStockIndicator(),
                String.valueOf(periodicLine.getInventoryQuantity()),
                periodicLine.getInventoryUom(),
                String.valueOf(periodicLine.getCountedQty()),
                String.valueOf(periodicLine.getVarianceQty()),
                periodicLine.getCycleCounterId(),
                periodicLine.getCycleCounterName(),
                String.valueOf(periodicLine.getStatusId()),
                periodicLine.getCycleCountAction(),
                periodicLine.getReferenceNo(),
                String.valueOf(periodicLine.getApprovalProcessId()),
                periodicLine.getApprovalLevel(),
                periodicLine.getApproverCode(),
                periodicLine.getApprovalStatus(),
                periodicLine.getRemarks(),
                periodicLine.getReferenceField1(),
                periodicLine.getReferenceField2(),
                periodicLine.getReferenceField3(),
                periodicLine.getReferenceField4(),
                periodicLine.getReferenceField5(),
                periodicLine.getReferenceField6(),
                periodicLine.getReferenceField7(),
                periodicLine.getReferenceField8(),
                periodicLine.getReferenceField9(),
                periodicLine.getReferenceField10(),
                String.valueOf(periodicLine.getDeletionIndicator()),
                periodicLine.getCreatedBy(),
                String.valueOf(periodicLine.getCreatedOn()),
                periodicLine.getConfirmedBy(),
                String.valueOf(periodicLine.getConfirmedOn()),
                periodicLine.getCountedBy(),
                String.valueOf(periodicLine.getCountedOn())
        };
        return strarr;
    }

    //=================================================STREAMING===============================================================

    //	private final Gson gson = new Gson();
    //-------------------------------------------findInventoryMovementByStreaming-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findInventoryMovementByStreaming() {
        Stream<InventoryMovement2> inventoryMovement2 = streamInventoryMovement();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                inventoryMovement2.forEach(im -> {
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

    /**
     * warehouseId
     * itemCode
     * description
     * packBarcodes
     * storageBin
     * movementType
     * submovementType
     * refDocNumber
     * movementQty
     * inventoryUom
     * createdOn
     *
     * @return
     */
    public Stream<InventoryMovement2> streamInventoryMovement() {
        jdbcTemplate.setFetchSize(1000);
		/*
		 * "MVT_DOC_NO"
		 * ----------------
		 * String warehouseId,
			Long movementType,
			Long submovementType,
			String palletCode,
			String packBarcodes,
			String itemCode,
			String storageBin,
			String description,
			Double movementQty,
			String inventoryUom,
			String refDocNumber,
			Date createdOn
		 */
        Stream<InventoryMovement2> inventoryMovement2 = jdbcTemplate.queryForStream(
                "Select WH_ID, MVT_TYP_ID, SUB_MVT_TYP_ID, PAL_CODE, PACK_BARCODE, ITM_CODE, "
                        + "ST_BIN, TEXT, MVT_QTY, MVT_UOM, REF_DOC_NO, DATEADD(hour, 3, IM_CTD_ON) AS IM_CTD_ON, MVT_DOC_NO "
                        + "from tblinventorymovement "
                        + "where is_deleted = 0 ",
                (resultSet, rowNum) -> {
                    return new InventoryMovement2(
                            resultSet.getString("WH_ID"),
                            resultSet.getLong("MVT_TYP_ID"),
                            resultSet.getLong("SUB_MVT_TYP_ID"),
                            resultSet.getString("PAL_CODE"),
                            resultSet.getString("PACK_BARCODE"),
                            resultSet.getString("ITM_CODE"),
                            resultSet.getString("ST_BIN"),
                            resultSet.getString("TEXT"),
                            resultSet.getDouble("MVT_QTY"),
                            resultSet.getString("MVT_UOM"),
                            resultSet.getString("REF_DOC_NO"),
                            resultSet.getDate("IM_CTD_ON"),
                            resultSet.getString("MVT_DOC_NO")
                    );
                });
//		this.createdOn = DateUtils.addTimeToDate(createdOn, 3);

        return inventoryMovement2;
    }

    //-------------------------------------------findStreamInboundHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamInboundHeader() {
        Stream<InboundHeaderStream> inboundHeaderStream = streamInboundHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                inboundHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * Inbound Header
     * refDocNumber
     * statusId
     * inboundOrderTypeId
     * containerNo
     * createdOn
     * confirmedBy
     * confirmedOn
     *
     * @return
     */
    public Stream<InboundHeaderStream> streamInboundHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Inbound Header
         * String refDocNumber
         * Long statusId
         * Long inboundOrderTypeId
         * String containerNo
         * Date createdOn
         * String confirmedBy
         * Date confirmedOn
         */
        Stream<InboundHeaderStream> inboundHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, STATUS_ID, IB_ORD_TYP_ID, CONT_NO, CTD_ON, IB_CNF_BY, "
                        + "IB_CNF_ON "
                        + "from tblinboundheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new InboundHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getLong("IB_ORD_TYP_ID"),
                        resultSet.getString("CONT_NO"),
                        resultSet.getDate("CTD_ON"),
                        resultSet.getString("IB_CNF_BY"),
                        resultSet.getDate("IB_CNF_ON")
                ));
        return inboundHeaderStream;
    }
    //-------------------------------------------findStreamStagingHeader-------------------------------------------------------

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
    //-------------------------------------------findStreamOutboundHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamOutboundHeader() {
        Stream<OutboundHeaderStream> outboundHeaderStream = streamOutboundHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                outboundHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * Outbound Header
     * refDocNumber
     * partnerCode
     * referenceDocumentType
     * statusId
     * refDocDate
     * requiredDeliveryDate
     * referenceField1
     * referenceField7
     * referenceField8
     * referenceField9
     * referenceField10
     * deliveryConfirmedOn
     *
     * @return
     */
    public Stream<OutboundHeaderStream> streamOutboundHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Outbound Header
         * String refDocNumber
         * String partnerCode
         * String referenceDocumentType
         * Long statusId
         * Date refDocDate
         * Date requiredDeliveryDate
         * String referenceField1
         * String referenceField7
         * String referenceField8
         * String referenceField9
         * String referenceField10
         * Date deliveryConfirmedOn
         */
        Stream<OutboundHeaderStream> outboundHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, PARTNER_CODE, REF_DOC_TYP, STATUS_ID, REF_DOC_DATE, REQ_DEL_DATE, REF_FIELD_1, "
                        + "REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, DLV_CNF_ON "
                        + "from tbloutboundheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new OutboundHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PARTNER_CODE"),
                        resultSet.getString("REF_DOC_TYP"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getDate("REF_DOC_DATE"),
                        resultSet.getDate("REQ_DEL_DATE"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getString("REF_FIELD_7"),
                        resultSet.getString("REF_FIELD_8"),
                        resultSet.getString("REF_FIELD_9"),
                        resultSet.getString("REF_FIELD_10"),
                        resultSet.getDate("DLV_CNF_ON")
                ));
        return outboundHeaderStream;
    }
    //-------------------------------------------findStreamGrHeader-------------------------------------------------------

    /**
     * @return
     */
//	public StreamingResponseBody findStreamGrHeader() {
//		Stream<GrHeaderStream> outboundHeaderStream = streamGrHeader();
//		StreamingResponseBody responseBody = httpResponseOutputStream -> {
//			try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
//				JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
//				jsonGenerator.writeStartArray();
//				jsonGenerator.setCodec(new ObjectMapper());
//				outboundHeaderStream.forEach(im -> {
//					try {
//						jsonGenerator.writeObject(im);
//					} catch (IOException exception) {
//						log.error("exception occurred while writing object to stream", exception);
//					}
//				});
//				jsonGenerator.writeEndArray();
//				jsonGenerator.close();
//			} catch (Exception e) {
//				log.info("Exception occurred while publishing data", e);
//				e.printStackTrace();
//			}
//			log.info("finished streaming records");
//		};
//		return responseBody;
//	}

    //	private final Gson gson = new Gson();

//	public Stream<GrHeaderStream> streamGrHeader() {
    public List<GrHeaderStream> streamGrHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Gr Header
         * String languageId
         * String companyCodeId
         * String plantId
         * String warehouseId
         * String preInboundNo
         * String refDocNumber
         * String stagingNo
         * String goodsReceiptNo
         * String palletCode
         * String caseCode
         * Long inboundOrderTypeId
         * Long statusId
         * String grMethod
         * String containerReceiptNo
         * String dockAllocationNo
         * String containerNo
         * String vechicleNo
         * Date expectedArrivalDate
         * Date goodsReceiptDate
         * Long deletionIndicator
         * String referenceField1
         * String referenceField2
         * String referenceField3
         * String referenceField4
         * String referenceField5
         * String referenceField6
         * String referenceField7
         * String referenceField8
         * String referenceField9
         * String referenceField10
         * String createdBy
         * Date createdOn
         * String updatedBy
         * Date updatedOn
         * String confirmedBy
         * Date confirmedOn
         */

        List<GrHeaderStream> grHeaderStream = jdbcTemplate.query(
//		Stream<GrHeaderStream> grHeaderStream = jdbcTemplate.queryForStream(
                "Select LANG_ID, C_ID, PLANT_ID, WH_ID, PRE_IB_NO, REF_DOC_NO, "
                        + "STG_NO, GR_NO, PAL_CODE, CASE_CODE, IB_ORD_TYP_ID, STATUS_ID, "
                        + "GR_MTD, CONT_REC_NO, DOCK_ALL_NO, CONT_NO, VEH_NO, EA_DATE, "
                        + "GR_DATE, IS_DELETED, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, "
                        + "REF_FIELD_5, REF_FIELD_6, REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, "
                        + "GR_CTD_BY, GR_CTD_ON, GR_UTD_BY, GR_UTD_ON, GR_CNF_BY, GR_CNF_ON "
                        + "from tblgrheader "
//						+ "where IS_DELETED = 0 AND WH_ID in (?,?,?,?)",new Object[] {110,111,0,0},
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new GrHeaderStream(
                        resultSet.getString("LANG_ID"),
                        resultSet.getString("C_ID"),
                        resultSet.getString("PLANT_ID"),
                        resultSet.getString("WH_ID"),
                        resultSet.getString("PRE_IB_NO"),
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("STG_NO"),
                        resultSet.getString("GR_NO"),
                        resultSet.getString("PAL_CODE"),
                        resultSet.getString("CASE_CODE"),
                        resultSet.getLong("IB_ORD_TYP_ID"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("GR_MTD"),
                        resultSet.getString("CONT_REC_NO"),
                        resultSet.getString("DOCK_ALL_NO"),
                        resultSet.getString("CONT_NO"),
                        resultSet.getString("VEH_NO"),
                        resultSet.getDate("EA_DATE"),
                        resultSet.getDate("GR_DATE"),
                        resultSet.getLong("IS_DELETED"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getString("REF_FIELD_2"),
                        resultSet.getString("REF_FIELD_3"),
                        resultSet.getString("REF_FIELD_4"),
                        resultSet.getString("REF_FIELD_5"),
                        resultSet.getString("REF_FIELD_6"),
                        resultSet.getString("REF_FIELD_7"),
                        resultSet.getString("REF_FIELD_8"),
                        resultSet.getString("REF_FIELD_9"),
                        resultSet.getString("REF_FIELD_10"),
                        resultSet.getString("GR_CTD_BY"),
                        resultSet.getDate("GR_CTD_ON"),
                        resultSet.getString("GR_UTD_BY"),
                        resultSet.getDate("GR_UTD_ON"),
                        resultSet.getString("GR_CNF_BY"),
                        resultSet.getDate("GR_CNF_ON")
                ));
        return grHeaderStream;
    }

    //-------------------------------------------findStreamPutAwayHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamPutAwayHeader() {
        Stream<PutAwayHeaderStream> putAwayHeaderStream = streamPutAwayHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                putAwayHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * PutAway Header
     * refDocNumber
     * packBarcodes
     * putAwayNumber
     * proposedStorageBin
     * putAwayQuantity
     * proposedHandlingEquipment
     * statusId
     * referenceField5
     * createdBy
     * createdOn
     *
     * @return
     */
    public Stream<PutAwayHeaderStream> streamPutAwayHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * PutAway Header
         * String refDocNumber
         * String packBarcodes
         * String putAwayNumber
         * String proposedStorageBin
         * Double putAwayQuantity
         * String proposedHandlingEquipment
         * Long statusId
         * String referenceField5
         * String createdBy
         * Date createdOn
         */
        Stream<PutAwayHeaderStream> putAwayHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, PACK_BARCODE, PA_NO, PROP_ST_BIN, PA_QTY, PROP_HE_NO, "
                        + "STATUS_ID, REF_FIELD_5, PA_CTD_BY, PA_CTD_ON "
                        + "from tblputawayheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new PutAwayHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PACK_BARCODE"),
                        resultSet.getString("PA_NO"),
                        resultSet.getString("PROP_ST_BIN"),
                        resultSet.getDouble("PA_QTY"),
                        resultSet.getString("PROP_HE_NO"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("REF_FIELD_5"),
                        resultSet.getString("PA_CTD_BY"),
                        resultSet.getDate("PA_CTD_ON")
                ));
        return putAwayHeaderStream;
    }
    //-------------------------------------------findStreamPreInboundHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamPreInboundHeader() {
        Stream<PreInboundHeaderStream> preInboundHeaderStream = streamPreInboundHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                preInboundHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * PreInbound Header
     * preInboundNo
     * refDocNumber
     * inboundOrderTypeId
     * statusId
     * containerNo
     * refDocDate
     * createdBy
     *
     * @return
     */
    public Stream<PreInboundHeaderStream> streamPreInboundHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * PreInbound Header
         * String preInboundNo
         * String refDocNumber
         * Long inboundOrderTypeId
         * Long statusId
         * String containerNo
         * Date refDocDate
         * String createdBy
         */
        Stream<PreInboundHeaderStream> preInboundHeaderStream = jdbcTemplate.queryForStream(
                "Select PRE_IB_NO, REF_DOC_NO, IB_ORD_TYP_ID, STATUS_ID, CONT_NO, REF_DOC_DATE, "
                        + "CTD_BY "
                        + "from tblpreinboundheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new PreInboundHeaderStream(
                        resultSet.getString("PRE_IB_NO"),
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getLong("IB_ORD_TYP_ID"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("CONT_NO"),
                        resultSet.getDate("REF_DOC_DATE"),
                        resultSet.getString("CTD_BY")
                ));
        return preInboundHeaderStream;
    }

    //-------------------------------------------findStreamPreOutboundHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamPreOutboundHeader() {
        Stream<PreOutboundHeaderStream> preOutboundHeaderStream = streamPreOutboundHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                preOutboundHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * PreOutbound Header
     * refDocNumber
     * preOutboundNo
     * partnerCode
     * referenceDocumentType
     * statusId
     * refDocDate
     * requiredDeliveryDate
     * referenceField1
     *
     * @return
     */
    public Stream<PreOutboundHeaderStream> streamPreOutboundHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * PreOutbound Header
         * String refDocNumber
         * String preOutboundNo
         * String partnerCode
         * String referenceDocumentType
         * Long statusId
         * Date refDocDate
         * Date requiredDeliveryDate
         * String referenceField1
         */
        Stream<PreOutboundHeaderStream> preOutboundHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, PRE_OB_NO, PARTNER_CODE, REF_DOC_TYP, tpo.status_id, ts.STATUS_TEXT, REF_DOC_DATE, "
                        + "REQ_DEL_DATE, tpo.REF_FIELD_1, OB_ORD_TYP_ID "
                        + "from tblpreoutboundheader tpo join tblstatusid ts on ts.status_id = tpo.status_id and ts.lang_id = tpo.lang_id "
                        + "where tpo.IS_DELETED = 0 ",
                (resultSet, rowNum) -> new PreOutboundHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PRE_OB_NO"),
                        resultSet.getString("PARTNER_CODE"),
                        resultSet.getString("REF_DOC_TYP"),
                        resultSet.getString("STATUS_TEXT"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getDate("REF_DOC_DATE"),
                        resultSet.getDate("REQ_DEL_DATE"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getLong("OB_ORD_TYP_ID")
                ));
        return preOutboundHeaderStream;
    }

    //-------------------------------------------findStreamOrderManagementLine-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamOrderManagementLine() {
        Stream<OrderManagementLineStream> orderManagementLineStream = streamOrderManagementLine();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                orderManagementLineStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * OrderManagementLine
     * String preOutboundNo
     * String refDocNumber
     * String partnerCode
     * Long lineNumber
     * String itemCode
     * String proposedStorageBin
     * String proposedPackBarCode
     * Long outboundOrderTypeId
     * Long statusId
     * String description
     * Double orderQty
     * Double inventoryQty
     * Double allocatedQty
     * Date requiredDeliveryDate
     *
     * @return
     */
    public Stream<OrderManagementLineStream> streamOrderManagementLine() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Order Management Line
         * String preOutboundNo
         * String refDocNumber
         * String partnerCode
         * Long lineNumber
         * String itemCode
         * String proposedStorageBin
         * String proposedPackBarCode
         * Long outboundOrderTypeId
         * Long statusId
         * String description
         * Double orderQty
         * Double inventoryQty
         * Double allocatedQty
         * Date requiredDeliveryDate
         */
        Stream<OrderManagementLineStream> orderManagementLineStream = jdbcTemplate.queryForStream(
                "Select PRE_OB_NO, REF_DOC_NO, PARTNER_CODE, OB_LINE_NO, ITM_CODE, PROP_ST_BIN, "
                        + "PROP_PACK_BARCODE, OB_ORD_TYP_ID, STATUS_ID, ITEM_TEXT, ORD_QTY, INV_QTY, "
                        + "ALLOC_QTY, REQ_DEL_DATE "
                        + "from tblordermangementline "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new OrderManagementLineStream(
                        resultSet.getString("PRE_OB_NO"),
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PARTNER_CODE"),
                        resultSet.getLong("OB_LINE_NO"),
                        resultSet.getString("ITM_CODE"),
                        resultSet.getString("PROP_ST_BIN"),
                        resultSet.getString("PROP_PACK_BARCODE"),
                        resultSet.getLong("OB_ORD_TYP_ID"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("ITEM_TEXT"),
                        resultSet.getDouble("ORD_QTY"),
                        resultSet.getDouble("INV_QTY"),
                        resultSet.getDouble("ALLOC_QTY"),
                        resultSet.getDate("REQ_DEL_DATE")
                ));
        return orderManagementLineStream;
    }

    //-------------------------------------------findStreamPickupHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamPickupHeader() {
        Stream<PickupHeaderStream> pickupHeaderStream = streamPickupHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                pickupHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * Pickup Header Stream
     * String refDocNumber
     * String partnerCode
     * Long lineNumber
     * String itemCode
     * String proposedStorageBin
     * String proposedPackBarCode
     * Long outboundOrderTypeId
     * Double pickToQty
     * Long statusId
     * String assignedPickerId
     * String referenceField1
     * Date pickupCreatedOn
     *
     * @return
     */
    public Stream<PickupHeaderStream> streamPickupHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Pickup Header Stream
         * String refDocNumber
         * String partnerCode
         * Long lineNumber
         * String itemCode
         * String proposedStorageBin
         * String proposedPackBarCode
         * Long outboundOrderTypeId
         * Double pickToQty
         * Long statusId
         * String assignedPickerId
         * String referenceField1
         * Date pickupCreatedOn
         */
        Stream<PickupHeaderStream> pickupHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, PARTNER_CODE, OB_LINE_NO, ITM_CODE, PROP_ST_BIN, PROP_PACK_BARCODE, "
                        + "OB_ORD_TYP_ID, PICK_TO_QTY, STATUS_ID, ASS_PICKER_ID, REF_FIELD_1, PICK_CTD_ON "
                        + "from tblpickupheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new PickupHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PARTNER_CODE"),
                        resultSet.getLong("OB_LINE_NO"),
                        resultSet.getString("ITM_CODE"),
                        resultSet.getString("PROP_ST_BIN"),
                        resultSet.getString("PROP_PACK_BARCODE"),
                        resultSet.getLong("OB_ORD_TYP_ID"),
                        resultSet.getDouble("PICK_TO_QTY"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("ASS_PICKER_ID"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getDate("PICK_CTD_ON")
                ));
        return pickupHeaderStream;
    }

    //-------------------------------------------findStreamQualityHeader-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamQualityHeader() {
        Stream<QualityHeaderStream> qualityHeaderStream = streamQualityHeader();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                qualityHeaderStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * Quality Header Stream
     * String refDocNumber
     * String partnerCode
     * String qualityInspectionNo
     * String actualHeNo
     * Long statusId
     * String qcToQty
     * String manufacturerPartNo
     * String referenceField1
     * String referenceField2
     * String referenceField4
     * String qualityCreatedBy
     * Date qualityCreatedOn
     *
     * @return
     */
    public Stream<QualityHeaderStream> streamQualityHeader() {
        jdbcTemplate.setFetchSize(50);
        /**
         * Quality Header Stream
         * String refDocNumber
         * String partnerCode
         * String qualityInspectionNo
         * String actualHeNo
         * Long statusId
         * String qcToQty
         * String manufacturerPartNo
         * String referenceField1
         * String referenceField2
         * String referenceField4
         * String qualityCreatedBy
         * Date qualityCreatedOn
         */
        Stream<QualityHeaderStream> qualityHeaderStream = jdbcTemplate.queryForStream(
                "Select REF_DOC_NO, PARTNER_CODE, QC_NO, PICK_HE_NO, STATUS_ID, QC_TO_QTY, "
                        + "MFR_PART, REF_FIELD_1, REF_FIELD_2, REF_FIELD_4, QC_CTD_BY, QC_CTD_ON "
                        + "from tblqualityheader "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new QualityHeaderStream(
                        resultSet.getString("REF_DOC_NO"),
                        resultSet.getString("PARTNER_CODE"),
                        resultSet.getString("QC_NO"),
                        resultSet.getString("PICK_HE_NO"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("QC_TO_QTY"),
                        resultSet.getString("MFR_PART"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getString("REF_FIELD_2"),
                        resultSet.getString("REF_FIELD_4"),
                        resultSet.getString("QC_CTD_BY"),
                        resultSet.getDate("QC_CTD_ON")
                ));
        return qualityHeaderStream;
    }

    //-------------------------------------------findStreamImBasicData1-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamImBasicData1() {
        Stream<ImBasicData1Stream> imBasicData1Stream = streamImBasicData1();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                imBasicData1Stream.forEach(im -> {
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

    /**
     * imBasicData1 Stream
     * String uomId
     * String languageId
     * String companyCodeId
     * String plantId
     * String warehouseId
     * String itemCode
     * String description
     * String model
     * String specifications1
     * String specifications2
     * String eanUpcNo
     * String manufacturerPartNo
     * String hsnCode
     * Long   itemType
     * Long   itemGroup
     * Long   subItemGroup
     * String storageSectionId
     * Double totalStock
     * Double minimumStock
     * Double maximumStock
     * Double reorderLevel
     * Double replenishmentQty
     * Double safetyStock
     * Long   statusId
     * String referenceField1
     * String referenceField2
     * String referenceField3
     * String referenceField4
     * String referenceField5
     * String referenceField6
     * String referenceField7
     * String referenceField8
     * String referenceField9
     * String referenceField10
     * Long   deletionIndicator
     * String createdBy
     * Date   createdOn
     * String updatedBy
     * Date   updatedOn
     *
     * @return
     */
    public Stream<ImBasicData1Stream> streamImBasicData1() {
        jdbcTemplate.setFetchSize(1000);

        Stream<ImBasicData1Stream> imBasicData1Stream = jdbcTemplate.queryForStream(
                "Select UOM_ID, LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, "
                        + "TEXT, MODEL, SPEC_01, SPEC_02, EAN_UPC_NO, MFR_PART, HSN_CODE, ITM_TYP_ID, ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, TOT_STK, "
                        + "MIN_STK, MAX_STK, RE_ORD_LVL, REP_QTY, SAFTY_STCK, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, "
                        + "REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, CTD_BY,"
                        + "CTD_ON, UTD_BY, UTD_ON "
                        + "from tblimbasicdata1 "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new ImBasicData1Stream(
                        resultSet.getString("UOM_ID"),
                        resultSet.getString("LANG_ID"),
                        resultSet.getString("C_ID"),
                        resultSet.getString("PLANT_ID"),
                        resultSet.getString("WH_ID"),
                        resultSet.getString("ITM_CODE"),
                        resultSet.getString("TEXT"),
                        resultSet.getString("MODEL"),
                        resultSet.getString("SPEC_01"),
                        resultSet.getString("SPEC_02"),
                        resultSet.getString("EAN_UPC_NO"),
                        resultSet.getString("MFR_PART"),
                        resultSet.getString("HSN_CODE"),
                        resultSet.getLong("ITM_TYP_ID"),
                        resultSet.getLong("ITM_GRP_ID"),
                        resultSet.getLong("SUB_ITM_GRP_ID"),
                        resultSet.getString("ST_SEC_ID"),
                        resultSet.getDouble("TOT_STK"),
                        resultSet.getDouble("MIN_STK"),
                        resultSet.getDouble("MAX_STK"),
                        resultSet.getDouble("RE_ORD_LVL"),
                        resultSet.getDouble("REP_QTY"),
                        resultSet.getDouble("SAFTY_STCK"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("REF_FIELD_1"),
                        resultSet.getString("REF_FIELD_2"),
                        resultSet.getString("REF_FIELD_3"),
                        resultSet.getString("REF_FIELD_4"),
                        resultSet.getString("REF_FIELD_5"),
                        resultSet.getString("REF_FIELD_6"),
                        resultSet.getString("REF_FIELD_7"),
                        resultSet.getString("REF_FIELD_8"),
                        resultSet.getString("REF_FIELD_9"),
                        resultSet.getString("REF_FIELD_10"),
                        resultSet.getLong("IS_DELETED"),
                        resultSet.getString("CTD_BY"),
                        resultSet.getDate("CTD_ON"),
                        resultSet.getString("UTD_BY"),
                        resultSet.getDate("UTD_ON")
                ));
        return imBasicData1Stream;
    }

    //-------------------------------------------findStreamStorageBin-------------------------------------------------------

    /**
     * @return
     */
    public StreamingResponseBody findStreamStorageBin() {
        Stream<StorageBinStream> storageBinStream = streamStorageBin();
        StreamingResponseBody responseBody = httpResponseOutputStream -> {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
                JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
                jsonGenerator.writeStartArray();
                jsonGenerator.setCodec(new ObjectMapper());
                storageBinStream.forEach(im -> {
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
    //	private final Gson gson = new Gson();

    /**
     * storageBinStream
     * String storageBin
     * String warehouseId
     * Long floorId
     * String storageSectionId
     * String spanId
     * Long statusId
     * String createdBy
     * Date createdOn
     *
     * @return
     */
    public Stream<StorageBinStream> streamStorageBin() {
        jdbcTemplate.setFetchSize(50);
        /**
         * storageBinStream
         * String storageBin
         * String warehouseId
         * Long floorId
         * String storageSectionId
         * String spanId
         * Long statusId
         * String createdBy
         * Date createdOn
         */
        Stream<StorageBinStream> storageBinStream = jdbcTemplate.queryForStream(
                "Select ST_BIN, WH_ID, FL_ID, ST_SEC_ID, SPAN_ID, STATUS_ID, "
                        + "CTD_BY, CTD_ON "
                        + "from tblstoragebin "
                        + "where IS_DELETED = 0 ",
                (resultSet, rowNum) -> new StorageBinStream(
                        resultSet.getString("ST_BIN"),
                        resultSet.getString("WH_ID"),
                        resultSet.getLong("FL_ID"),
                        resultSet.getString("ST_SEC_ID"),
                        resultSet.getString("SPAN_ID"),
                        resultSet.getLong("STATUS_ID"),
                        resultSet.getString("CTD_BY"),
                        resultSet.getDate("CTD_ON")
                ));
        return storageBinStream;
    }

    public List<ImBasicData1> getAllImBasicData1(SearchImBasicData1 searchImBasicData1) {

        String sql = "Select UOM_ID, LANG_ID, C_ID, PLANT_ID, WH_ID, ITM_CODE, "
                + "TEXT, MODEL, SPEC_01, SPEC_02, EAN_UPC_NO, MFR_PART, HSN_CODE, ITM_TYP_ID, ITM_GRP_ID, SUB_ITM_GRP_ID, ST_SEC_ID, TOT_STK, "
                + "MIN_STK, MAX_STK, RE_ORD_LVL, REP_QTY, SAFTY_STCK, STATUS_ID, REF_FIELD_1, REF_FIELD_2, REF_FIELD_3, REF_FIELD_4, REF_FIELD_5, REF_FIELD_6, "
                + "REF_FIELD_7, REF_FIELD_8, REF_FIELD_9, REF_FIELD_10, IS_DELETED, CTD_BY,"
                + "CTD_ON, UTD_BY, UTD_ON "
                + "from tblimbasicdata1 "
                + "where IS_DELETED = 0 and wh_id in (:warehouseId) ";

        SqlParameterSource param = new MapSqlParameterSource("warehouseId", searchImBasicData1.getWarehouseId());
        jdbcParamTemplate.setCacheLimit(10000);
        List<ImBasicData1> imBasicData1List = jdbcParamTemplate.query(sql,
                param,
                (resultSet, i) -> {
                    return toImBasicData1(resultSet);
                });

//		List<ImBasicData1> imBasicData1List = jdbcParamTemplate.query(sql,
//												param,
//												BeanPropertyRowMapper.newInstance(ImBasicData1.class));
        return imBasicData1List;
    }

    private ImBasicData1 toImBasicData1(ResultSet resultSet) throws SQLException {
        ImBasicData1 imBasicData1 = new ImBasicData1();
        imBasicData1.setUomId(resultSet.getString("UOM_ID"));
        imBasicData1.setLanguageId(resultSet.getString("LANG_ID"));
        imBasicData1.setCompanyCodeId(resultSet.getString("C_ID"));
        imBasicData1.setPlantId(resultSet.getString("PLANT_ID"));
        imBasicData1.setWarehouseId(resultSet.getString("WH_ID"));
        imBasicData1.setItemCode(resultSet.getString("ITM_CODE"));
        imBasicData1.setDescription(resultSet.getString("TEXT"));
        imBasicData1.setModel(resultSet.getString("MODEL"));
        imBasicData1.setSpecifications1(resultSet.getString("SPEC_01"));
        imBasicData1.setSpecifications2(resultSet.getString("SPEC_02"));
        imBasicData1.setEanUpcNo(resultSet.getString("EAN_UPC_NO"));
        imBasicData1.setManufacturerPartNo(resultSet.getString("MFR_PART"));
        imBasicData1.setHsnCode(resultSet.getString("HSN_CODE"));
        imBasicData1.setItemType(resultSet.getLong("ITM_TYP_ID"));
        imBasicData1.setItemGroup(resultSet.getLong("ITM_GRP_ID"));
        imBasicData1.setSubItemGroup(resultSet.getLong("SUB_ITM_GRP_ID"));
        imBasicData1.setStorageSectionId(resultSet.getString("ST_SEC_ID"));
        imBasicData1.setTotalStock(resultSet.getDouble("TOT_STK"));
        imBasicData1.setMinimumStock(resultSet.getDouble("MIN_STK"));
        imBasicData1.setMaximumStock(resultSet.getDouble("MAX_STK"));
        imBasicData1.setReorderLevel(resultSet.getDouble("RE_ORD_LVL"));
        imBasicData1.setReplenishmentQty(resultSet.getDouble("REP_QTY"));
        imBasicData1.setSafetyStock(resultSet.getDouble("SAFTY_STCK"));
        imBasicData1.setStatusId(resultSet.getLong("STATUS_ID"));
        imBasicData1.setReferenceField1(resultSet.getString("REF_FIELD_1"));
        imBasicData1.setReferenceField2(resultSet.getString("REF_FIELD_2"));
        imBasicData1.setReferenceField3(resultSet.getString("REF_FIELD_3"));
        imBasicData1.setReferenceField4(resultSet.getString("REF_FIELD_4"));
        imBasicData1.setReferenceField5(resultSet.getString("REF_FIELD_5"));
        imBasicData1.setReferenceField6(resultSet.getString("REF_FIELD_6"));
        imBasicData1.setReferenceField7(resultSet.getString("REF_FIELD_7"));
        imBasicData1.setReferenceField8(resultSet.getString("REF_FIELD_8"));
        imBasicData1.setReferenceField9(resultSet.getString("REF_FIELD_9"));
        imBasicData1.setReferenceField10(resultSet.getString("REF_FIELD_10"));
        imBasicData1.setDeletionIndicator(resultSet.getLong("IS_DELETED"));
        imBasicData1.setCreatedBy(resultSet.getString("CTD_BY"));
        imBasicData1.setCreatedOn(resultSet.getDate("CTD_ON"));
        imBasicData1.setUpdatedBy(resultSet.getString("UTD_BY"));
        imBasicData1.setUpdatedOn(resultSet.getDate("UTD_ON"));

        return imBasicData1;
    }
}