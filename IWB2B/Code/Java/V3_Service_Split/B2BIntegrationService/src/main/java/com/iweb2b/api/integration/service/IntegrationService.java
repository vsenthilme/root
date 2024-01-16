package com.iweb2b.api.integration.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iweb2b.api.integration.config.PropertiesConfig;
import com.iweb2b.api.integration.controller.exception.BadRequestException;
import com.iweb2b.api.integration.model.consignment.dto.Consignment;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentImpl;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentResponse;
import com.iweb2b.api.integration.model.consignment.dto.ConsignmentWebhook;
import com.iweb2b.api.integration.model.consignment.dto.CountInput;
import com.iweb2b.api.integration.model.consignment.dto.DashboardCountOutput;
import com.iweb2b.api.integration.model.consignment.dto.FindConsignment;
import com.iweb2b.api.integration.model.consignment.dto.Items;
import com.iweb2b.api.integration.model.consignment.dto.JNTResponse;
import com.iweb2b.api.integration.model.consignment.dto.OrderStatusUpdate;
import com.iweb2b.api.integration.model.consignment.dto.OrderStatusUpdateResponse;
import com.iweb2b.api.integration.model.consignment.dto.Pieces_Details;
import com.iweb2b.api.integration.model.consignment.dto.Receiver;
import com.iweb2b.api.integration.model.consignment.dto.Sender;
import com.iweb2b.api.integration.model.consignment.dto.jnt.Details;
import com.iweb2b.api.integration.model.consignment.dto.jnt.JNTOrderCreate;
import com.iweb2b.api.integration.model.consignment.dto.jnt.JNTOrderCreateRequest;
import com.iweb2b.api.integration.model.consignment.dto.jnt.JNTPrintLabelBzContent;
import com.iweb2b.api.integration.model.consignment.dto.jnt.JNTPrintLabelResponse;
import com.iweb2b.api.integration.model.consignment.dto.jnt.JNTWebhookRequest;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPOrder;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPOrderCreateResponse;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPToken;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPTokenResponse;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPTrackingRequest;
import com.iweb2b.api.integration.model.consignment.dto.qp.QPTrackingResponse;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentEntity;
import com.iweb2b.api.integration.model.consignment.entity.ConsignmentWebhookEntity;
import com.iweb2b.api.integration.model.consignment.entity.IConsignmentEntity;
import com.iweb2b.api.integration.model.consignment.entity.JNTWebhookEntity;
import com.iweb2b.api.integration.model.consignment.entity.PocImageListEntity;
import com.iweb2b.api.integration.model.consignment.entity.QualityCheckImageListEntity;
import com.iweb2b.api.integration.model.tracking.ConsignmentTracking;
import com.iweb2b.api.integration.repository.ConsignmentRepository;
import com.iweb2b.api.integration.repository.ConsignmentWebhookRepository;
import com.iweb2b.api.integration.repository.DestinationDetailRepository;
import com.iweb2b.api.integration.repository.JNTFailureResponseRepository;
import com.iweb2b.api.integration.repository.JNTWebhookRepository;
import com.iweb2b.api.integration.repository.OriginDetailRepository;
import com.iweb2b.api.integration.repository.PiecesDetailRepository;
import com.iweb2b.api.integration.repository.PocImageListRepository;
import com.iweb2b.api.integration.repository.QualityCheckImageListRepository;
import com.iweb2b.api.integration.repository.Specification.ConsignmentSpecification;
import com.iweb2b.api.integration.util.CommonUtils;
import com.iweb2b.api.integration.util.DateUtils;
import com.iweb2b.api.integration.util.JsonConvertor;
import com.iweb2b.api.integration.util.MD5Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IntegrationService {

    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired
    SoftDataUploadService softDataUploadService;

    @Autowired
    ConsignmentWebhookRepository consignmentWebhookRepository;

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    private PiecesDetailRepository piecesDetailRepository;

    @Autowired
    private DestinationDetailRepository destinationDetailRepository;

    @Autowired
    private OriginDetailRepository originDetailRepository;


    @Autowired
    PocImageListRepository pocImageListRepository;

    @Autowired
    QualityCheckImageListRepository qualityCheckImageListRepository;

    @Autowired
    JNTWebhookRepository jntWebhookRepository;

    @Autowired
    JNTFailureResponseRepository jntFailureResponseRepository;

    private String JNT_HUBCODE = "JT";
    private String QAP_HUBCODE = "QATARPOST";
    private final String PASS = "PASS";

    /**
     * Returns RestTemplate Object
     *
     * @return
     */
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Object Convertor
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter
                .setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    /**
     * POST - ClientSoftdataUpload
     *
     * @param newConsignment
     * @return
     */
    public ConsignmentResponse postClientSoftdataUpload(Consignment newConsignment) {
        // Invoke Shipsy API
        try {
            String authToken = propertiesConfig.getShipsyApiAuthtoken();
            String softdataUrl = propertiesConfig.getShipsyApiServer() + propertiesConfig.getShipsyApiSoftdataUpload();
            log.info("URL:" + softdataUrl);
            log.info("AuthToken:" + authToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "B2B_IW_PortalRestTemplate");
			//headers.add("Authorization", "Basic " + authToken);
            headers.add("api-key", authToken);
            HttpEntity<?> entity = new HttpEntity<>(newConsignment, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(softdataUrl);
            ResponseEntity<ConsignmentResponse> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, ConsignmentResponse.class);
            log.info("result:-------> : " + result);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // PATCH - Consignment status To Shipsy
    public OrderStatusUpdateResponse orderStatusUpdateToShipsy(OrderStatusUpdate orderStatusUpdate, String eventType) {
        try {
//			String HANDOVER_COURIER_PARTNER = "handover_courier_partner";
            String authToken = propertiesConfig.getShipsyApiAuthtoken();
            String orderStatusUpdateUrl = propertiesConfig.getShipsyApiServer() + propertiesConfig.getShipsyApiSoftdataUpdate();
            orderStatusUpdateUrl = orderStatusUpdateUrl + "/" + eventType;
            log.info("URL:" + orderStatusUpdateUrl);
            log.info("AuthToken:" + authToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "B2B_IW_Portal RestTemplate");
            //headers.add("Authorization", "Basic " + authToken);
            headers.add("api-key", authToken);

            HttpEntity<?> entity = new HttpEntity<>(orderStatusUpdate, headers);
            HttpClient client = HttpClients.createDefault();

            RestTemplate restTemplate = getRestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orderStatusUpdateUrl);

            ResponseEntity<OrderStatusUpdateResponse> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
                    entity, OrderStatusUpdateResponse.class);
            log.info("OrderStatusUpdateResponse------> : " + result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * POST - ClientSoftdataUpload
     *
     * @param refNumber
     * @return
     */
    public ConsignmentTracking getClientConsignmentTracking(String refNumber) {
        // Invoke Shipsy API
        String authToken = propertiesConfig.getShipsyApiAuthtoken();
        String consignmentTrackingUrl = propertiesConfig.getShipsyApiServer()
                + propertiesConfig.getShipsyApiConsignmentTracking();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "B2B_IW_PortalRestTemplate");
        //headers.add("Authorization", "Basic " + authToken);
        headers.add("api-key", authToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // https://app.shipsy.in/api/client/integration/consignment/track?reference_number=E12345678"
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(consignmentTrackingUrl + "?reference_number=" + refNumber);
        ResponseEntity<ConsignmentTracking> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET,
                entity, ConsignmentTracking.class);
        return result.getBody();
    }

    // ---------------------------------Shipping-Label-------------------------------------------------------------

    /**
     * @param refNumber
     * @return
     */
    public byte[] getShippingLabel(String refNumber) {
        // Invoke Shipsy API
        try {
            String authToken = propertiesConfig.getShipsyApiAuthtoken();
            String consignmentTrackingUrl = propertiesConfig.getShipsyApiServer()
                    + propertiesConfig.getShipsyApiLabelGen();
            RestTemplate restTemplate = getRestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("User-Agent", "B2B_IW_PortalRestTemplate");
            //headers.add("Authorization", "Basic " + authToken);
            headers.add("api-key", authToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            // https://demodashboardapi.shipsy.in/api/client/integration/consignment/shippinglabel/stream?reference_number=BLEND099
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(consignmentTrackingUrl + "?reference_number=" + refNumber);
//			ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
            ResponseEntity<byte[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // WEBHOOK
    /*
     * public String listenWebhook(String referenceNumber) { // Invoke Shipsy API
     * try { String authToken = propertiesConfig.getShipsyApiAuthtoken(); String
     * webhookUrl = propertiesConfig.getShipsyApiAsyadWebhook();
     *
     * HttpHeaders headers = new HttpHeaders();
     * headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
     * headers.add("User-Agent", "B2B_IW_PortalRestTemplate");
     * headers.add("Authorization", "Basic " + authToken); HttpEntity<?> entity =
     * new HttpEntity<>(headers);
     *
     * // https://apix.asyadexpress.com/v2/webhooks/incoming-integration?source=IW
     * UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(webhookUrl +
     * "?source=IW"); ResponseEntity<String> result =
     * getRestTemplate().exchange(builder.toUriString(), HttpMethod.GET, entity,
     * String.class); return result.getBody(); } catch (RestClientException e) {
     * e.printStackTrace(); } return referenceNumber; }
     */

    public ConsignmentWebhook listenWebhook(@RequestBody ConsignmentWebhook consignmentWebhook) {
        // Invoke Shipsy API
        try {
            String authToken = propertiesConfig.getShipsyApiAuthtoken();
            String webhookUrl = propertiesConfig.getShipsyApiAsyadWebhook();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "B2B_IW_PortalRestTemplate");
            //headers.add("Authorization", "Basic " + authToken);
            headers.add("api-key", authToken);
            HttpEntity<?> entity = new HttpEntity<>(consignmentWebhook, headers);

            // https://apix.asyadexpress.com/v2/webhooks/incoming-integration?source=IW
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(webhookUrl + "?source=IW");
            ResponseEntity<ConsignmentWebhook> result = getRestTemplate().exchange(builder.toUriString(),
                    HttpMethod.POST, entity, ConsignmentWebhook.class);

            return result.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * CreateJNTWebhook
     *
     * @param newJNTWebhookRequest
     * @return
     * @throws Exception
     */
    public JNTWebhookEntity createJNTWebhook(JNTWebhookRequest newJNTWebhookRequest) throws Exception {
        try {
            log.info("newJNTWebhookRequest : " + newJNTWebhookRequest);
            JNTWebhookEntity dbJNTWebhookEntity = new JNTWebhookEntity();
            dbJNTWebhookEntity.setBillCode(newJNTWebhookRequest.getBillCode());
            List<Details> details = newJNTWebhookRequest.getDetails();
            for (Details detail : details) {
                dbJNTWebhookEntity.setDescription(detail.getDesc());
                dbJNTWebhookEntity.setScanNetworkCity(detail.getScanNetworkCity());
                dbJNTWebhookEntity.setScanNetworkId(detail.getScanNetworkId());
                dbJNTWebhookEntity.setScanNetworkName(detail.getScanNetworkName());
                dbJNTWebhookEntity.setScanNetworkProvince(detail.getScanNetworkProvince());
                dbJNTWebhookEntity.setScanNetworkTypeName(detail.getScanNetworkTypeName());
                dbJNTWebhookEntity.setScanTime(detail.getScanTime());
                dbJNTWebhookEntity.setScanType(detail.getScanType());
                JNTWebhookEntity createdJNTWebhookEntity = jntWebhookRepository.save(dbJNTWebhookEntity);
                log.info("createdJNTWebhookEntity : " + createdJNTWebhookEntity);
                
                /*
                 * Send respective status to Shipsy
                 */
                String scanType = createdJNTWebhookEntity.getScanType();
                String shipsyStatusEvent = CommonUtils.getStatusMapping(scanType.toUpperCase());
                log.info("-----------shipsyStatusEvent-----> : " + createdJNTWebhookEntity.getScanType() + "," + shipsyStatusEvent);
                if (shipsyStatusEvent != null) {
                    OrderStatusUpdate orderStatusUpdate = new OrderStatusUpdate();
                    String referenceNumber = softDataUploadService.getConsigmentByJntBillCode(newJNTWebhookRequest.getBillCode());
                    log.info("referenceNumber & billCode : " + referenceNumber + "," + newJNTWebhookRequest.getBillCode());

                    // "scanTime":"2023-07-26 12:04:26"
                    log.info("--------------ScanTime()-------> : " + detail.getScanTime());
                    long milli = 0;
                    if (detail.getScanTime() == null) {
                        milli = new Date().getTime();
                    } else {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(detail.getScanTime());
                        log.info("convertStringToDate-------> : " + date.getTime());
                        milli = date.getTime();
                    }

                    orderStatusUpdate.setReference_number(referenceNumber);
                    orderStatusUpdate.setEvent_time_epoch(milli);
                    orderStatusUpdate.setWorker_code("");
                    orderStatusUpdate.setLat("");
                    orderStatusUpdate.setLng("");
                    orderStatusUpdate.setNotes("");

                    // Station sending/DC sending
                    if (shipsyStatusEvent.equalsIgnoreCase("intransit_to_hub")) {
                        orderStatusUpdate.setHub_code("JT");
                        orderStatusUpdate.setDestination_hub_code("JT");
                    }

                    // Abnormal Parcel Scan
                    if (shipsyStatusEvent.equalsIgnoreCase("attempted")) {
                    	/*
                    	 *  JNTWebhookEntity(jnt_webhook_id=2823, billCode=JTE000158664438, description=【Riyadh】[Riyadh Middle 1] has operated 
                    	 *  abnormal parcel scaning, the operator is [JTS201992-Muhannad Quhaim ahmed Hummad Al quhaim], abnormal reason is [Uncontactable], 
                    	 *  scanNetworkCity=Riyadh, scanNetworkId=192, scanNetworkName=Riyadh Middle 1, scanNetworkProvince=null, scanNetworkTypeName=网点, 
                    	 *  scanTime=2023-08-02 08:17:14, scanType=Abnormal parcel scan)
                    	 */
                    	
                    	String reason_code = getJNTFailureResponseCode (parseReason(detail.getDesc()));
                    	log.info("OrderStatusUpdate----attempted--matched--> reason_code : " + reason_code);
                        orderStatusUpdate.setReason_code(reason_code);
                    }
                    
                    // Delivery-Scan/accept event
					if (shipsyStatusEvent.equalsIgnoreCase("accept")) {
						orderStatusUpdate.setWorker_code("3PLRider");
					}
					log.info("OrderStatusUpdate----Request----> : " + orderStatusUpdate + "----event----->" + shipsyStatusEvent);
                    OrderStatusUpdateResponse response = orderStatusUpdateToShipsy(orderStatusUpdate, shipsyStatusEvent);
                    log.info("OrderStatusUpdateResponse : " + response);
                } else {
                    log.info("shipsyStatusEvent not found: " + shipsyStatusEvent);
                }
                return createdJNTWebhookEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * 
     * @param desc
     * @return
     */
    private String parseReason(String desc) {
//    	String desc = "【Riyadh】[Riyadh Middle 1] has operated \r\n"
//    			+ "    	 *  abnormal parcel scaning, the operator is [JTS201992-Muhannad Quhaim ahmed Hummad Al quhaim], abnormal reason is [Uncontactable]";
    	desc = desc.substring(desc.lastIndexOf("abnormal"), desc.length());
    	desc = desc.substring(desc.indexOf('[') + 1, desc.length());
    	desc = desc.substring(0, desc.indexOf(']'));
    	log.info("Desc: " + desc);
    	return desc;
	}


    /**
     * Update Order
     * @param orderStatusUpdate
     * @param shipsyStatusEvent
     * @return
     * @throws Exception
     */
    public OrderStatusUpdateResponse updateOrderInShipsy(OrderStatusUpdate orderStatusUpdate, String shipsyStatusEvent) throws Exception {
        try {
            /*
             * Send respective status to Shipsy
             */
//			String shipsyStatusEvent = CommonUtils.getStatusMapping(event);
            log.info("-----------shipsyStatusEvent-----> : " + shipsyStatusEvent);
            if (shipsyStatusEvent != null) {
//				OrderStatusUpdate orderStatusUpdate = new OrderStatusUpdate();
//				orderStatusUpdate.setReference_number(referenceNumber);
//				orderStatusUpdate.setWorker_code("JNT");
//				Long l = 1614093117015L;
//				orderStatusUpdate.setEvent_time_epoch(l);
//				Double d = null;
//				orderStatusUpdate.setLat(d);
//				orderStatusUpdate.setLng(d);
//				orderStatusUpdate.setNotes("");
                OrderStatusUpdateResponse response = orderStatusUpdateToShipsy(orderStatusUpdate, shipsyStatusEvent);
                log.info("OrderStatusUpdateResponse : " + response);
                return response;
            } else {
                log.info("shipsyStatusEvent not found: " + shipsyStatusEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * @param newConsignmentWebhook
     * @return
     * @throws Exception
     */
    public JNTResponse createConsignmentWebhook(ConsignmentWebhook newConsignmentWebhook) throws Exception {
        try {
            ConsignmentWebhookEntity dbConsignmentWebhookEntity = new ConsignmentWebhookEntity();
            BeanUtils.copyProperties(newConsignmentWebhook, dbConsignmentWebhookEntity,
                    CommonUtils.getNullPropertyNames(newConsignmentWebhook));
//			log.info("newConsignmentWebhook : " + newConsignmentWebhook);

            ConsignmentWebhookEntity createdConsignmentWebhookEntity = consignmentWebhookRepository.save(dbConsignmentWebhookEntity);
            log.info("createdConsignmentWebhookEntity ------->: " + createdConsignmentWebhookEntity);
//			log.info("Webhook Type() : " + createdConsignmentWebhookEntity.getType());
//			log.info("Webhook Hub_code() : " + createdConsignmentWebhookEntity.getHub_code());
            createdConsignmentWebhookEntity.setPoc_image_list(new HashSet<>());
            createdConsignmentWebhookEntity.setQuality_check_image_list(new HashSet<>());

            if (newConsignmentWebhook.getPoc_image_list() != null) {
                for (String newPocImageListEntity : newConsignmentWebhook.getPoc_image_list()) {
                    PocImageListEntity dbPocImageListEntity = new PocImageListEntity();
                    dbPocImageListEntity.setDescription(newPocImageListEntity);
                    dbPocImageListEntity
                            .setConsignment_webhook_id(createdConsignmentWebhookEntity.getConsignment_webhook_id());
                    createdConsignmentWebhookEntity.getPoc_image_list()
                            .add(pocImageListRepository.save(dbPocImageListEntity));
                }
            }

            if (newConsignmentWebhook.getQuality_check_image_list() != null) {
                for (String newQualityCheckImageListEntity : newConsignmentWebhook.getQuality_check_image_list()) {
                    QualityCheckImageListEntity dbQualityCheckImageListEntity = new QualityCheckImageListEntity();
                    dbQualityCheckImageListEntity.setDescription(newQualityCheckImageListEntity);
                    dbQualityCheckImageListEntity
                            .setConsignment_webhook_id(createdConsignmentWebhookEntity.getConsignment_webhook_id());
                    createdConsignmentWebhookEntity.getQuality_check_image_list()
                            .add(qualityCheckImageListRepository.save(dbQualityCheckImageListEntity));
                }
            }

            ConsignmentWebhook createdConsignmentWebhook = new ConsignmentWebhook();
            List<String> pocImageList = new ArrayList<>();
            List<String> qualityCheckImageList = new ArrayList<>();
            BeanUtils.copyProperties(createdConsignmentWebhookEntity, createdConsignmentWebhook,
                    CommonUtils.getNullPropertyNames(createdConsignmentWebhookEntity));
            for (PocImageListEntity newPocImageListEntity : createdConsignmentWebhookEntity.getPoc_image_list()) {
                String dbPocImageList = newPocImageListEntity.getDescription();
                pocImageList.add(dbPocImageList);
            }

            for (QualityCheckImageListEntity newQualityImageCheckList : createdConsignmentWebhookEntity
                    .getQuality_check_image_list()) {
                String dbQualityCheckImage = newQualityImageCheckList.getDescription();
                qualityCheckImageList.add(dbQualityCheckImage);
            }
            createdConsignmentWebhook.setPoc_image_list(pocImageList);
            createdConsignmentWebhook.setQuality_check_image_list(qualityCheckImageList);

            // Push the order to JNT
            // type = intransittohub, hub_code = JT
            String INTRANSIT_TO_HUB = "intransittohub";

            /*
             * -------------ROUTER-CONF-------------------------------------------------------------------------
             */
            if (createdConsignmentWebhook.getType().equalsIgnoreCase(INTRANSIT_TO_HUB) &&
            		createdConsignmentWebhook.getHub_code() != null && 
                    createdConsignmentWebhook.getHub_code().equalsIgnoreCase(JNT_HUBCODE)) {
            	log.info("Webhook Type() : " + createdConsignmentWebhook.getType());
                log.info("Webhook Hub_code() : " + createdConsignmentWebhook.getHub_code());
                
                ConsignmentEntity consignmentEntity = consignmentRepository.findConsigmentUniqueRecord(newConsignmentWebhook.getReference_number());
                log.info("consignmentFromLocalDB : " + consignmentEntity);
                
                // POST JNT Request
                JNTResponse objJNTResponse = null;
                if (consignmentEntity == null) { // It is from Shipsy
                    objJNTResponse = postJNTRequest(createdConsignmentWebhook.getReference_number());
                } else if (consignmentEntity != null && consignmentEntity.getAwb_3rd_Party() == null) {
                	objJNTResponse = postJNTRequest(createdConsignmentWebhook.getReference_number());
                } else if (consignmentEntity != null && consignmentEntity.getAwb_3rd_Party() != null) {
                	throw new BadRequestException("Consignment :" + createdConsignmentWebhook.getReference_number() + 
                			" cannot be pushed to JNT since it is already sent to JNT");
                }
                
                if (objJNTResponse.getMsg() != null && objJNTResponse.getMsg().equalsIgnoreCase("success")) {
//					String OUT_FOR_PICKUP = "out_for_pickup";
                    if (consignmentEntity == null) {
                        try {
                            Consignment consignment = softDataUploadService.getConsignmentFromShipsy(newConsignmentWebhook.getReference_number());
                            consignment.setScanType(INTRANSIT_TO_HUB);
                            ConsignmentResponse response = softDataUploadService.createConsignmentInLocal(consignment, "From Shipsy");
                            log.info("created Consignment in  LocalDB : " + response);
                        } catch (Exception e) {
                            log.info("ERROR: Create Consignment in  LocalDB : " + e.toString());
                        }
                    }

                    /*
                     * Update BillCode with the created Consignment
                     */
                    ConsignmentEntity updatedConsignmentEntity =
                            softDataUploadService.updateConsignment(createdConsignmentWebhook.getReference_number(),
                                    objJNTResponse.getData().getBillCode(), JNT_HUBCODE);
//					log.info("updatedConsignmentEntity--------> : " + updatedConsignmentEntity);

//                    log.info("-----------Updating the status to Shipsy--------> : ");
//                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(objJNTResponse.getData().getCreateOrderTime());
//                    log.info("create Order Date&Time-------> : " + date.getTime());
//                    long milli = date.getTime();
//
//                    OrderStatusUpdate orderStatusUpdate = new OrderStatusUpdate();
//                    orderStatusUpdate.setReference_number(createdConsignmentWebhook.getReference_number());
//                    orderStatusUpdate.setEvent_time_epoch(milli);
//                    orderStatusUpdate.setWorker_code("");
//                    orderStatusUpdate.setLat("");
//                    orderStatusUpdate.setLng("");
//                    orderStatusUpdate.setNotes(objJNTResponse.getData().getBillCode());
//                    orderStatusUpdate.setHub_code("JT");
//                    log.info("Updating the status to Shipsy---#2--------> : " + orderStatusUpdate);
//
//                    OrderStatusUpdateResponse response = orderStatusUpdateToShipsy(orderStatusUpdate, INTRANSIT_TO_HUB);
//                    log.info("OrderStatusUpdateResponse : " + response);
                } else {
                    objJNTResponse = new JNTResponse();
                    objJNTResponse.setMsg("Ref_Number:"
                            + createdConsignmentWebhook.getReference_number()
                            + " created and status is not moved to " + INTRANSIT_TO_HUB
                            + " to push it to JNT. Please change it.");
                }
                return objJNTResponse;
            } //----------------QATAR-API----------------------------------------------------------------------------------
            else if (createdConsignmentWebhook.getType().equalsIgnoreCase(INTRANSIT_TO_HUB) &&
                    createdConsignmentWebhook.getHub_code().equalsIgnoreCase(QAP_HUBCODE)) {
                QPOrderCreateResponse qpResponse = postQPRequest(createdConsignmentWebhook.getReference_number());
                log.info("QPOrderCreateResponse : " + qpResponse);
            }
            log.info("createdConsignmentWebhook : " + createdConsignmentWebhook);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    /**
     * @param referenceNumber
     * @return
     * @throws Exception
     */
    public JNTResponse postJNTRequest(String referenceNumber) throws Exception {
        Consignment dbConsignment = softDataUploadService.getConsignment(referenceNumber);
        log.info("###################Consignment------------ : " + dbConsignment);

        JNTOrderCreateRequest newJNTRequest = new JNTOrderCreateRequest();
        Sender newSender = new Sender();
        Receiver newReceiver = new Receiver();

        Date today = new Date(); // Dummy Date
//		newJNTRequest.setCustomerCode("J0086024173"); // J0086024173
        newJNTRequest.setCustomerCode(propertiesConfig.getJntCustomerCode()); // J0086024173

        String bzDigest = propertiesConfig.getJntBzSignature();
        newJNTRequest.setDigest(bzDigest);
        newJNTRequest.setServiceType("01");                                // Hard coded
        newJNTRequest.setOrderType("1");                                    // Hard coded
        newJNTRequest.setDeliveryType("04");                                // Hard coded
        newJNTRequest.setExpressType("EZKSA");                                // Hard coded
        newJNTRequest.setNetwork("");
        if (dbConsignment.getLength() != null) {
            newJNTRequest.setLength(Double.valueOf(dbConsignment.getLength()));
        }

        newJNTRequest.setSendStartTime(today);                    // Hard code
        newJNTRequest.setWeight(dbConsignment.getWeight());
        newJNTRequest.setRemark(dbConsignment.getNotes());
        newJNTRequest.setBillCode("");
        newJNTRequest.setBatchNumber("");
        newJNTRequest.setTxlogisticId(referenceNumber);        // AWB_Number
        newJNTRequest.setGoodsType("ITN4"); // ITN1
        newJNTRequest.setTotalQuantity(String.valueOf(dbConsignment.getNum_pieces()));

        // Receiver
        newReceiver.setReceiverIdNumber(dbConsignment.getCustomer_civil_id());
        if (dbConsignment.getDestination_details() != null) {
        	log.info("########dbConsignment.getDestination_details().getAddress_line_1()----- : " + dbConsignment.getDestination_details().getAddress_line_1());
        	if (dbConsignment.getDestination_details().getAddress_line_1() != null) {
        		newReceiver.setAddress(dbConsignment.getDestination_details().getAddress_line_1());
        	}
        	
        	if (dbConsignment.getDestination_details().getAddress_line_2() != null) {
        		newReceiver.setArea(dbConsignment.getDestination_details().getAddress_line_2());
        	}
        	
        	if (dbConsignment.getDestination_details().getAlternate_phone() != null) {
            	newReceiver.setPhone(dbConsignment.getDestination_details().getAlternate_phone());
            }
        	
        	if (dbConsignment.getDestination_details().getCity() != null) {
        		newReceiver.setCity(dbConsignment.getDestination_details().getCity());
                newReceiver.setMobile(dbConsignment.getDestination_details().getPhone());
        	}
        	
        	if (dbConsignment.getDestination_details().getPhone() != null) {
                newReceiver.setMobile(dbConsignment.getDestination_details().getPhone());
        	}
        	
        	if (dbConsignment.getDestination_details().getName() != null) {
        		newReceiver.setName(dbConsignment.getDestination_details().getName());
        	}
        	
        	if (dbConsignment.getDestination_details().getPincode() != null) {
        		newReceiver.setPostCode(dbConsignment.getDestination_details().getPincode());
        	}
        	
        	if (dbConsignment.getDestination_details().getState() != null) {
        		newReceiver.setProv(dbConsignment.getDestination_details().getState());
        	}
        	
        	if (dbConsignment.getDestination_details().getCountry() != null) {
                newReceiver.setCountryCode(dbConsignment.getDestination_details().getCountry().toUpperCase()); // Hardcoded as "KSA" during Testing
            }
        }

        if (newReceiver.getCountryCode() == null) {
        	newReceiver.setCountryCode("KSA");
        }
        newReceiver.setCompany("");
        newReceiver.setTown("");
        newReceiver.setStreet("");
        newReceiver.setMailBox("");

        // Sender
        newSender.setArea(dbConsignment.getOrigin_details().getAddress_line_2());
        newSender.setAddress(dbConsignment.getOrigin_details().getAddress_line_1());
        newSender.setTown("");
        newSender.setStreet("");
        newSender.setCity(dbConsignment.getOrigin_details().getCity());
        newSender.setMobile(dbConsignment.getOrigin_details().getPhone());
        newSender.setMailBox("");
        newSender.setPhone(dbConsignment.getOrigin_details().getAlternate_phone());

        if (dbConsignment.getOrigin_details().getCountry() != null) {
            newSender.setCountryCode(dbConsignment.getOrigin_details().getCountry().toUpperCase());
        }

        newSender.setName(dbConsignment.getOrigin_details().getName());
        newSender.setCompany("");
        newSender.setPostCode(dbConsignment.getOrigin_details().getPincode());
        newSender.setProv(dbConsignment.getOrigin_details().getState());

        if (dbConsignment.getCod_amount() != null) {
            Double codAmount = Double.valueOf(dbConsignment.getCod_amount());
            newJNTRequest.setItemsValue(codAmount);
            log.info("---------codAmount-----------> : " + codAmount);
            newJNTRequest.setPriceCurrency(dbConsignment.getCurrency());
        }

        if (dbConsignment.getWidth() != null) {
            newJNTRequest.setWidth(Double.valueOf(dbConsignment.getWidth()));
        }

        newJNTRequest.setOfferFee(0D);                            // Hard coded

        // List of Items
        List<Items> itemsList = new ArrayList<>();
        if (dbConsignment.getPieces_detail() != null) {
            for (Pieces_Details newPiecesDetails : dbConsignment.getPieces_detail()) {
                Items newItem = new Items();
                newItem.setNumber(1L); // Hard Coded
                newItem.setItemType("ITN4");
                newItem.setItemName(newPiecesDetails.getDescription());
                newItem.setPriceCurrency(dbConsignment.getCurrency());
                newItem.setItemValue(String.valueOf(dbConsignment.getDeclared_value()));
                newItem.setItemUrl("");
                newItem.setDesc(newPiecesDetails.getDescription());
                itemsList.add(newItem);
            }
        }

        newJNTRequest.setSendEndTime(today);
        newJNTRequest.setHeight(newJNTRequest.getHeight());
        newJNTRequest.setPayType("");
        newJNTRequest.setOperateType(1D);
        newJNTRequest.setPlatformName("");
        newJNTRequest.setCustomerAccount("");
        newJNTRequest.setIsUnpackEnabled(true); // Hard Coded

        // Parent - Set
        newJNTRequest.setReceiver(newReceiver);
        newJNTRequest.setSender(newSender);
        newJNTRequest.setItems(itemsList);

        JNTOrderCreate objJNTOrderCreate = new JNTOrderCreate();
        objJNTOrderCreate.setBizContent(newJNTRequest);
        return sendOrder2JNT(objJNTOrderCreate);
    }

    /**
     * @param objJNTOrderCreate
     * @throws Exception
     */
    private JNTResponse sendOrder2JNT(JNTOrderCreate objJNTOrderCreate) throws Exception {
        try {
            log.info("newJNTRequest: " + objJNTOrderCreate);
            String jntAddress = propertiesConfig.getJntAddress();
            String apiAccount = propertiesConfig.getJntApiAccount();
            String privateKey = propertiesConfig.getJntPrivateKey();
            String timestamp = String.valueOf(System.currentTimeMillis());

            log.info("jntAddress : " + jntAddress);
            log.info("apiAccount : " + apiAccount);
            log.info("privateKey : " + privateKey);

            String jsonString = JsonConvertor.toJsonString(objJNTOrderCreate.getBizContent());
            log.info("JSON String : " + jsonString);

            /*
             * Headers
             * -------------------------------
             * apiAccount=292508153084379141
             * digest=dv7y+k4Pth0asb/tf7IOtg==
             * timestamp=1638428570653
             * */

            String headerDigest = MD5Utils.convertToBase64NMD5(jsonString, privateKey);
            log.info("headerDigest : " + headerDigest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("apiAccount", apiAccount);
            headers.add("digest", headerDigest);
            headers.add("timestamp", timestamp);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("bizContent", jsonString);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(jntAddress);
            ResponseEntity<JNTResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, JNTResponse.class);
            log.info("JNT Response : " + result.getBody());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param hubCode
     * @return
     */
//    public List<Consignment> getConsignments(String hubCode) {
//        List<String> consignmentReferenceNumberList = consignmentRepository.getByHubcode(hubCode);
//        log.info("consignmentReferenceNumberList : " + consignmentReferenceNumberList);
//        ;
//        List<Consignment> consignmentList = new ArrayList<>();
//        if (!consignmentReferenceNumberList.isEmpty()) {
//            consignmentReferenceNumberList.stream().forEach(n -> {
//                ConsignmentEntity consignmentEntityList = consignmentRepository.findConsigmentUniqueRecord(n);
//                if (consignmentEntityList != null) {
////			consignmentEntityList.stream().forEach(c -> {
//                    Consignment consignment = new Consignment();
//                    BeanUtils.copyProperties(consignmentEntityList, consignment, CommonUtils.getNullPropertyNames(consignmentEntityList));
//
//                    OriginDetailsEntity originDetailsEntity = originDetailRepository
//                            .findByConsignmentId(consignmentEntityList.getConsignmentId());
//                    if (originDetailsEntity != null) {
//                        Origin_Details odetails = new Origin_Details();
//                        BeanUtils.copyProperties(originDetailsEntity, odetails,
//                                CommonUtils.getNullPropertyNames(originDetailsEntity));
//                        consignment.setOrigin_details(odetails);
//                    }
//
//                    DestinationDetailEntity destinationDetailsEntity = destinationDetailRepository
//                            .findByConsignmentId(consignmentEntityList.getConsignmentId());
//                    if (destinationDetailsEntity != null) {
//                        Destination_Details ddetails = new Destination_Details();
//                        BeanUtils.copyProperties(destinationDetailsEntity, ddetails,
//                                CommonUtils.getNullPropertyNames(destinationDetailsEntity));
//                        consignment.setDestination_details(ddetails);
//                    }
//
//                    List<PiecesDetailsEntity> piecesDetailsEntityList = piecesDetailRepository
//                            .findByConsignmentId(consignmentEntityList.getConsignmentId());
//                    Set<Pieces_Details> piecesDetailsList = new HashSet<>();
//                    piecesDetailsEntityList.stream().forEach(p -> {
//                        Pieces_Details pdetails = new Pieces_Details();
//                        BeanUtils.copyProperties(p, pdetails, CommonUtils.getNullPropertyNames(p));
//                        piecesDetailsList.add(pdetails);
//                    });
//                    consignment.setPieces_detail(piecesDetailsList);
//                    consignment.setScanType(consignmentRepository.getScanType(consignmentEntityList.getAwb_3rd_Party()));
//                    consignmentList.add(consignment);
//                    
//                    log.info("---------consignment-----> : " + consignment);
//                }
//            });
//            return consignmentList;
//        }
//        return consignmentList;
//    }
    
    /**
     * 
     * @param hubCode
     * @return
     */
    public List<Consignment> getConsignments(String hubCode) {
        List<String> consignmentReferenceNumberList = consignmentRepository.getByHubcode(hubCode);
        log.info("consignmentReferenceNumberList : " + consignmentReferenceNumberList);

        List<Consignment> consignmentList = new ArrayList<>();
        if (!consignmentReferenceNumberList.isEmpty()) {
//            List<IConsignmentEntity> iConsignmentEntity = consignmentRepository.findConsigmentByReferenceNumber(consignmentReferenceNumberList);
            List<IConsignmentEntity> iConsignmentEntity = consignmentRepository.findConsigmentByReferenceNumber(hubCode);
            iConsignmentEntity.stream().forEach(c -> {
                        if (c != null && c.getOrderType() != null) {
                            Consignment consignment = new Consignment();
                            consignment.setReference_number(c.getReferenceNumber());
                            consignment.setCustomer_reference_number(c.getCustomerReferenceNumber());
                            consignment.setStatus_description(c.getStatusDescription());
                            consignment.setAwb_3rd_Party(c.getAwb3rdParty());
                            consignment.setCreated_at(c.getCreatedAt());
                            consignment.setIs_awb_printed(c.getIsAwbPrinted());
                            consignment.setScanType(c.getScanType());
                            consignment.setOrderType(c.getOrderType());
                            consignment.setCustomer_code(c.getCustomerCode());
                            consignmentList.add(consignment);
                        }
                    }
            );
            return consignmentList;
        }
        return consignmentList;
    }

    /**
     * @param billCode
     * @return
     * @throws Exception
     */
    public JNTPrintLabelResponse printLabel(String billCode) throws Exception {
        try {
            log.info("printLabel: " + billCode);
            String customerCode = propertiesConfig.getJntCustomerCode();
            String bzSignature = propertiesConfig.getJntBzSignature();
            String printLabelAddress = propertiesConfig.getJntPrintLabelAddress();
            String apiAccount = propertiesConfig.getJntApiAccount();
            String privateKey = propertiesConfig.getJntPrivateKey();
            String timestamp = String.valueOf(System.currentTimeMillis());

            log.info("printLabelAddress : " + printLabelAddress);
            log.info("apiAccount : " + apiAccount);
            log.info("privateKey : " + privateKey);

            /*
             * {
             * 		"customerCode": "J0086024173",
             * 		"digest": "VdlpKaoq64AZ0yEsBkvt1A==",
             * 		"billCode": "UTE010005886251"
             * }
             */
            JNTPrintLabelBzContent bzContent = new JNTPrintLabelBzContent();
            bzContent.setCustomerCode(customerCode);
            bzContent.setDigest(bzSignature);
            bzContent.setBillCode(billCode);

            String jsonString = JsonConvertor.toJsonString(bzContent);
            log.info("JSON String : " + jsonString);
            String headerDigest = MD5Utils.convertToBase64NMD5(jsonString, privateKey);
            log.info("headerDigest : " + headerDigest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("apiAccount", apiAccount);
            headers.add("digest", headerDigest);
            headers.add("timestamp", timestamp);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("bizContent", jsonString);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(printLabelAddress);
            ResponseEntity<JNTPrintLabelResponse> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, JNTPrintLabelResponse.class);
            log.info("JNT Response : " + result.getBody());
            
            /*
             * Update the Flag to TRUE after printing
             */
            if (result != null) {
            	String referenceNumber = softDataUploadService.getConsigmentByJntBillCode(billCode);
            	ConsignmentEntity consignmentEntity = consignmentRepository.findConsigmentUniqueRecord(referenceNumber);
            	consignmentEntity.setIs_awb_printed(Boolean.TRUE);
            	consignmentRepository.save(consignmentEntity);          	
            }
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param billCode
     * @return
     * @throws Exception
     */
    public byte[] pdfPrintLabel(String billCode) throws Exception {
        try {
            log.info("printLabel: " + billCode);
            String customerCode = propertiesConfig.getJntCustomerCode();
            String bzSignature = propertiesConfig.getJntBzSignature();
            String printLabelAddress = propertiesConfig.getJntPdfPrintLabelAddress();
            String apiAccount = propertiesConfig.getJntApiAccount();
            String privateKey = propertiesConfig.getJntPrivateKey();
            String timestamp = String.valueOf(System.currentTimeMillis());

            log.info("printLabelAddress : " + printLabelAddress);
            log.info("apiAccount : " + apiAccount);
            log.info("privateKey : " + privateKey);

            /*
             * {
             * 		"customerCode": "J0086024173",
             * 		"digest": "VdlpKaoq64AZ0yEsBkvt1A==",
             * 		"billCode": "UTE010005886251"
             * }
             */
            JNTPrintLabelBzContent bzContent = new JNTPrintLabelBzContent();
            bzContent.setCustomerCode(customerCode);
            bzContent.setDigest(bzSignature);
            bzContent.setBillCode(billCode);

            String jsonString = JsonConvertor.toJsonString(bzContent);
            log.info("JSON String : " + jsonString);
            String headerDigest = MD5Utils.convertToBase64NMD5(jsonString, privateKey);
            log.info("headerDigest : " + headerDigest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("apiAccount", apiAccount);
            headers.add("digest", headerDigest);
            headers.add("timestamp", timestamp);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("bizContent", jsonString);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(printLabelAddress);
//			ResponseEntity<String> result = getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
            ResponseEntity<byte[]> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
            log.info("JNT Response : " + result.getBody());
            
            /*
             * Update the Flag to TRUE after printing
             */
            if (result != null) {
            	String referenceNumber = softDataUploadService.getConsigmentByJntBillCode(billCode);
            	ConsignmentEntity consignmentEntity = consignmentRepository.findConsigmentUniqueRecord(referenceNumber);
            	consignmentEntity.setIs_awb_printed(Boolean.TRUE);
            	consignmentRepository.save(consignmentEntity);          	
            }
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //====================================QATAR-APIs===========================================================

    /**
     * @return
     */
    public String generateQPToken() {
        /*
         * Authorization: Basic: <username>:<password>
         * Note: Username and password has to be base64 encoded
         */
        String credentials = propertiesConfig.getQpTokenUsername() + ":" + propertiesConfig.getQpTokenPassword();
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        String tokenUrl = propertiesConfig.getQpAddress() + propertiesConfig.getQpToken();
        ResponseEntity<QPTokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, QPTokenResponse.class);
        log.info("Access Token Response ---------" + response.getBody());

        HttpHeaders responseHeaders = response.getHeaders();
        log.info("Token : " + responseHeaders.getFirst("Token"));
        return responseHeaders.getFirst("Token");
    }

    /**
     * generateQPPartnerToken
     *
     * @return
     */
    public QPToken generateQPPartnerToken() {
        // Generate new Token whenever API call happens (validity only for 15Mins)
        String token = generateQPToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Token", token);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        /*
         * https://web.qatarpost.qa/shipmentapi/partner/authentication/get/authcode/IwEx
         */
        String partnerTokenUrl = propertiesConfig.getQpAddress() + propertiesConfig.getQpPartnerToken() + propertiesConfig.getQpTokenUsername();

        ResponseEntity<QPTokenResponse> response = restTemplate.exchange(partnerTokenUrl, HttpMethod.GET, request, QPTokenResponse.class);
        log.info("Partner Token Response ---------" + response.getBody());

        QPTokenResponse qpTokenResponse = response.getBody();
        log.info("Partner Token : " + qpTokenResponse.getData());

        QPToken qpToken = new QPToken();
        qpToken.setToken(token);
        qpToken.setPartnerToken(qpTokenResponse.getData());
        return qpToken;
    }

    /**
     * @param referenceNumber
     * @return
     * @throws Exception
     */
    public QPOrderCreateResponse postQPRequest(String referenceNumber) throws Exception {
        Consignment dbConsignment = softDataUploadService.getConsignment(referenceNumber);
        log.info("Consignment retrieved for QP: " + dbConsignment);

        List<QPOrder> qpOrderkList = new ArrayList<>();
        for (Pieces_Details newPiecesDetail : dbConsignment.getPieces_detail()) {
            QPOrder newQPOrder = new QPOrder();
            newQPOrder.setOrderID(referenceNumber);
            newQPOrder.setSubOrderID(referenceNumber);                                                    // Hard coded
            newQPOrder.setTrackingNumber(referenceNumber);                                                // Hard coded
            newQPOrder.setCustomerName(dbConsignment.getDestination_details().getName());
            newQPOrder.setCustomerMobile(dbConsignment.getDestination_details().getPhone());
            newQPOrder.setMerchantName(dbConsignment.getOrigin_details().getName());
            newQPOrder.setMerchantStore("");                                                            // Hard coded
            newQPOrder.setMerchantPhone(dbConsignment.getOrigin_details().getPhone());
//			newQPOrder.setDelivery_Zone(dbConsignment.getDestination_details().getPincode());
//			newQPOrder.setDelivery_Street(dbConsignment.getDestination_details().getAddress_line_1());
//			newQPOrder.setPickup_Zone(dbConsignment.getOrigin_details().getPincode());
//			newQPOrder.setPickup_Street(dbConsignment.getOrigin_details().getAddress_line_1());

            newQPOrder.setDelivery_Zone("1");
            newQPOrder.setDelivery_Street("1");
            newQPOrder.setDelivery_BuildingNo("1");                                                        // Hard coded
            newQPOrder.setDelivery_UnitNo("1");                                                            // Hard coded
            newQPOrder.setPickup_Zone("1");
            newQPOrder.setPickup_Street("1");
            newQPOrder.setPickup_Building("1");                                                            // Hard coded
            newQPOrder.setPickup_Unitno("1");                                                            // Hard coded
            newQPOrder.setLocation_Details(dbConsignment.getDestination_details().getAddress_line_1());
            newQPOrder.setDeliveryType("NextDayDelivery");                                                // Hard coded
            newQPOrder.setDeliveryScheduletype("NA");                                                    // Hard coded
            newQPOrder.setZoneType("Inside Doha");                                                        // Hard coded
            newQPOrder.setProductDiscription(newPiecesDetail.getDescription());
            newQPOrder.setWeight(newPiecesDetail.getWeight());
            if (newPiecesDetail.getDeclared_value() != null) {
                Long quantity = Long.valueOf(newPiecesDetail.getDeclared_value());
                newQPOrder.setQuantity(quantity);
            }

            newQPOrder.setTransectionType(1L);                                                            // Hard coded
            newQPOrder.setCurrentStatus(5L);                                                            // Hard coded

            var current_timestamp = new Date();
            newQPOrder.setCreatedDate(CommonUtils.toISOString(current_timestamp));
            qpOrderkList.add(newQPOrder);
        }

        return sendOrder2QP(qpOrderkList);
    }

    /**
     * @param qpTrackingRequest
     * @return
     * @throws Exception
     */
    public QPTrackingResponse listenQPWebhook(QPTrackingRequest qpTrackingRequest) throws Exception {
        try {
            log.info("QPTrackingRequest : " + qpTrackingRequest);

            // https://web.qatarpost.qa/shipmentapi/partner/order/create
            String trackingUrl = propertiesConfig.getQpAddress() + propertiesConfig.getQpTracking();
            log.info("trackingUrl : " + trackingUrl);

            QPToken qpToken = generateQPPartnerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Token", qpToken.getToken());
            headers.add("Partner-Token", qpToken.getPartnerToken());

            HttpEntity<?> entity = new HttpEntity<>(qpTrackingRequest, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(trackingUrl);
            ResponseEntity<QPTrackingResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QPTrackingResponse.class);
            log.info("QPTrackingResponse : " + result);
            return result.getBody();
//				/*
//				 * Send respective status to Shipsy
//				 */
//				String shipsyStatusEvent = CommonUtils.getStatusMapping(createdJNTWebhookEntity.getScanType());
//				log.info("shipsyStatusEvent : " + shipsyStatusEvent );
//				if (shipsyStatusEvent != null) {
//					OrderStatusUpdate orderStatusUpdate = new OrderStatusUpdate();
//					orderStatusUpdate.setReference_number(qpTrackingRequest.getBillCode());
//					orderStatusUpdate.setEvent_time_epoch(Instant.now().toEpochMilli());
//					orderStatusUpdate.setHub_code(JNT_HUBCODE);
//					orderStatusUpdate.setNotes("Updated by IWE");					
//					OrderStatusUpdateResponse response = orderStatusUpdateToShipsy(orderStatusUpdate, shipsyStatusEvent);
//					log.info("OrderStatusUpdateResponse : " + response );
//				} else {
//					log.info("shipsyStatusEvent : " + shipsyStatusEvent );
//				}
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public QPTrackingResponse scheduleQPWebhook() throws Exception {
        try {
            log.info("------------Calling----QP----TRACKING------API-----------");

            List<String> qpOrders = softDataUploadService.getConsigmentByQP();
            log.info("qpOrders : " + qpOrders);

            QPTrackingRequest qpTrackingRequest = new QPTrackingRequest();
            qpTrackingRequest.setPageNumber("1");
            qpTrackingRequest.setTackingNumbers(qpOrders);
            log.info("QPTrackingRequest : " + qpTrackingRequest);

            // https://web.qatarpost.qa/shipmentapi/partner/order/create
            String trackingUrl = propertiesConfig.getQpAddress() + propertiesConfig.getQpTracking();
            log.info("trackingUrl : " + trackingUrl);

            QPToken qpToken = generateQPPartnerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Token", qpToken.getToken());
            headers.add("Partner-Token", qpToken.getPartnerToken());

            HttpEntity<?> entity = new HttpEntity<>(qpTrackingRequest, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(trackingUrl);
            ResponseEntity<QPTrackingResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QPTrackingResponse.class);
            log.info("QPTrackingResponse : " + result);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param qpOrderList
     * @return
     * @throws Exception
     */
    private QPOrderCreateResponse sendOrder2QP(List<QPOrder> qpOrderList) throws Exception {
        try {
            log.info("qpOrderList: " + qpOrderList);

//			ObjectMapper Obj = new ObjectMapper();
//			String jsonStr = Obj.writeValueAsString(qpOrderList);
//			log.info("jsonStr : " + jsonStr);

            // https://web.qatarpost.qa/shipmentapi/partner/order/create
            String createUrl = propertiesConfig.getQpAddress() + propertiesConfig.getQpCreate();
            log.info("createUrl : " + createUrl);

            QPToken qpToken = generateQPPartnerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Token", qpToken.getToken());
            headers.add("Partner-Token", qpToken.getPartnerToken());
            log.info("orders------->" + qpOrderList);

            HttpEntity<?> entity = new HttpEntity<>(qpOrderList, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createUrl);
            ResponseEntity<QPOrderCreateResponse> result =
                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, QPOrderCreateResponse.class);
            log.info("QPOrderCreateResponse : " + result);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 
     * @return
     */
    private static QPOrder qpHardCodedOrder() {
        String json = "{\r\n"
                + " \"orderID\": \"1238967435\", \r\n"
                + " \"subOrderID\": \"567885\", \r\n"
                + " \"trackingNumber\": \"QPM1\", \r\n"
                + " \"customerName\": \"fahad\", \r\n"
                + " \"customerMobile\": \"987654321\", \r\n"
                + " \"MerchantName\" :\"Dynax\", \r\n"
                + " \"MerchantStore\":\"Dynax Store\", \r\n"
                + " \"MerchantPhone\":\"33015634\", \r\n"
                + " \"delivery_Zone\": \"2\", \r\n"
                + " \"delivery_Street\": \"3\", \r\n"
                + " \"delivery_BuildingNo\": \"4\", \r\n"
                + " \"delivery_UnitNo\": \"5\", \r\n"
                + " \"pickup_Zone\": \"2\", \r\n"
                + " \"pickup_Street\": \"67\",\r\n"
                + " \"pickup_Building\": \"2\",\r\n"
                + " \"pickup_Unitno\": \"s3\",\r\n"
                + " \"location_Details\": \"near to filafil round about\",\r\n"
                + " \"deliveryType\": \"homeDelivery\",\r\n"
                + " \"deliveryScheduletype\": \"2018-08-14T12:00:00.000Z-2018-08-14T14:00:00.000Z\",\r\n"
                + " \"zoneType\": \"Inside Doha\",\r\n"
                + " \"productDiscription\": \"Pen\",\r\n"
                + " \"weight\": 19.0,\r\n"
                + " \"quantity\": 20,\r\n"
                + " \"TransectionType\":1,\r\n"
                + " \"currentStatus\": 5,\r\n"
                + " \"createdDate\": \"2017-10-10T07:23:07.1622468+03:00\" \r\n"
                + " }";
        try {
            ObjectMapper mapper = new ObjectMapper();
            QPOrder qpOrder = mapper.readValue(json, QPOrder.class);
            log.info("qpOrder------->" + qpOrder);
            return qpOrder;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param countInput
     * @return
     * @throws java.text.ParseException
     */
    public DashboardCountOutput getDashboardCount(CountInput countInput)
            throws java.text.ParseException {

        Date fromCreatedOn = new Date();
        Date toCreatedOn = new Date();

        if (countInput.getFromCreatedOn() != null && countInput.getToCreatedOn() != null) {
            fromCreatedOn = DateUtils.convertStringToYYYYMMDD(countInput.getFromCreatedOn());
            toCreatedOn = DateUtils.convertStringToYYYYMMDD(countInput.getToCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(fromCreatedOn, toCreatedOn);

            fromCreatedOn = dates[0];
            toCreatedOn = dates[1];
        }

        DashboardCountOutput dbDashboardCount = new DashboardCountOutput();

        long awaitingJNTPassCount = consignmentRepository.getJNTCount(PASS,
                "1",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setJntPassCount(awaitingJNTPassCount);
        log.info("awaitingJNTPassCount : " + awaitingJNTPassCount);

        long awaitingJNTFailCount = consignmentRepository.getJNTCount("FAIL",
                "1",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setJntFailCount(awaitingJNTFailCount);
        log.info("awaitingJNTFailCount : " + awaitingJNTFailCount);

        long awaitingBoutiqaatPassCount = consignmentRepository.getBoutiqaatCount(PASS,
                "1",
                "BOQ",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setBoutiqaatPassCount(awaitingBoutiqaatPassCount);
        log.info("awaitingBoutiqaatPassCount : " + awaitingBoutiqaatPassCount);

        long awaitingBoutiqaatFailCount = consignmentRepository.getBoutiqaatCount("FAIL",
                "1",
                "BOQ",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setBoutiqaatFailCount(awaitingBoutiqaatFailCount);
        log.info("awaitingBoutiqaatFailCount : " + awaitingBoutiqaatFailCount);

        long awaitingiWintlPassCount = consignmentRepository.getWintlCount(PASS,
                "1",
                "IWINTL",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setIwintlPassCount(awaitingiWintlPassCount);
        log.info("awaitingiWintlPassCount : " + awaitingiWintlPassCount);

        long awaitingiWintlFailCount = consignmentRepository.getWintlCount("FAIL",
                "1",
                "IWINTL",
                fromCreatedOn, toCreatedOn);
        dbDashboardCount.setIwintlFailCount(awaitingiWintlFailCount);
        log.info("awaitingiWintlFailCount : " + awaitingiWintlFailCount);

        return dbDashboardCount;
    }

    /**
     * 
     * @param jntFailureResponseDesc
     * @return
     */
    public String getJNTFailureResponseCode(String desc) {
        String responseCode = null;
        if (desc != null) {
        	responseCode = jntFailureResponseRepository.getFailureResponseCode(desc);
        }
        return responseCode;
    }
    
//    public String getJNTFailureResponseCode(JNTFailureResponseInput jntFailureResponseDesc) {
//        String jntFailureResponseCode = null;
//        if (jntFailureResponseDesc.getJntFailureResponseDescription() != null) {
//            jntFailureResponseCode = jntFailureResponseRepository.getFailureResponseCode(jntFailureResponseDesc.getJntFailureResponseDescription());
//        }
//        return jntFailureResponseCode;
//    }
    
    public static void main(String[] args) {
    	/*
    	 *  JNTWebhookEntity(jnt_webhook_id=2823, billCode=JTE000158664438, description=【Riyadh】[Riyadh Middle 1] has operated 
    	 *  abnormal parcel scaning, the operator is [JTS201992-Muhannad Quhaim ahmed Hummad Al quhaim], abnormal reason is [Uncontactable], 
    	 *  scanNetworkCity=Riyadh, scanNetworkId=192, scanNetworkName=Riyadh Middle 1, scanNetworkProvince=null, scanNetworkTypeName=网点, 
    	 *  scanTime=2023-08-02 08:17:14, scanType=Abnormal parcel scan)
    	 */
    	String desc = "【Riyadh】[Riyadh Middle 1] has operated \r\n"
    			+ "    	 *  abnormal parcel scaning, the operator is [JTS201992-Muhannad Quhaim ahmed Hummad Al quhaim], abnormal reason is [Uncontactable]";
    	desc = desc.substring(desc.lastIndexOf("abnormal"),desc.length());
    	desc = desc.substring(desc.indexOf('[')+1, desc.length());
    	desc = desc.substring(0,desc.indexOf(']'));
    	log.info("Desc: " + desc);
    }

    /**
     * @param findConsignment
     * @return
     * @throws Exception
     */
    public Stream<ConsignmentEntity> findConsignmentNew(FindConsignment findConsignment) throws Exception {
        if (findConsignment.getStartDate() != null && findConsignment.getEndDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findConsignment.getStartDate(), findConsignment.getEndDate());
            findConsignment.setStartDate(dates[0]);
            findConsignment.setEndDate(dates[1]);
        }

        if (findConsignment.getScanType_3rd_Party() != null && !findConsignment.getScanType_3rd_Party().isEmpty()) {
            if (findConsignment.getScanType_3rd_Party().get(0) != null) {
                boolean readyToPrint = findConsignment.getScanType_3rd_Party().get(0).equalsIgnoreCase("ReadyToPrint");   //check Ready to Print
                if (readyToPrint) {
                    findConsignment.setScanType_3rd_Party(null);
                }
            }
        }

        ConsignmentSpecification spec = new ConsignmentSpecification(findConsignment);
        Stream<ConsignmentEntity> results = consignmentRepository.stream(spec, ConsignmentEntity.class);

        if (findConsignment.getScanType_3rd_Party() != null && !findConsignment.getScanType_3rd_Party().isEmpty()) {
            if (findConsignment.getScanType_3rd_Party().get(0) != null) {
                boolean readyToPrint = findConsignment.getScanType_3rd_Party().get(0).equalsIgnoreCase("ReadyToPrint");   //check Ready to Print
                if (readyToPrint) {
                    results = results.filter(n -> n.getScanType_3rd_Party() == null);
                    return results;
                }
            }
        }

        return results;
    }

    /**
     *
     * @param findConsignment
     * @return
     * @throws Exception
     */

    public List<ConsignmentImpl> findConsignment(FindConsignment findConsignment) throws Exception {
        if (findConsignment.getStartDate() != null && findConsignment.getEndDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findConsignment.getStartDate(), findConsignment.getEndDate());
            findConsignment.setStartDate(dates[0]);
            findConsignment.setEndDate(dates[1]);
        }

        if (findConsignment.getScanType() != null && !findConsignment.getScanType().isEmpty()) {
            if (findConsignment.getScanType().get(0) != null) {
                boolean readyToPrint = findConsignment.getScanType().get(0).equalsIgnoreCase("ReadyToPrint");   //check Ready to Print
                if (readyToPrint) {
                    findConsignment.setScanType(null);
                }
            }
        }

        if (findConsignment.getScanType() == null || findConsignment.getScanType().isEmpty()) {
            findConsignment.setScanType(null);
        }

        if (findConsignment.getConsignmentId() == null || findConsignment.getConsignmentId().isEmpty()) {
            findConsignment.setConsignmentId(null);
        }
        if (findConsignment.getReference_number() == null || findConsignment.getReference_number().isEmpty()) {
            findConsignment.setReference_number(null);
        }
        if (findConsignment.getCustomer_code() == null || findConsignment.getCustomer_code().isEmpty()) {
            findConsignment.setCustomer_code(null);
        }

        if (findConsignment.getService_type_id() == null || findConsignment.getService_type_id().isEmpty()) {
            findConsignment.setService_type_id(null);
        }
        if (findConsignment.getConsignment_type() == null || findConsignment.getConsignment_type().isEmpty()) {
            findConsignment.setConsignment_type(null);
        }
        if (findConsignment.getCustomer_reference_number() == null || findConsignment.getCustomer_reference_number().isEmpty()) {
            findConsignment.setCustomer_reference_number(null);
        }
        if (findConsignment.getCustomer_civil_id() == null || findConsignment.getCustomer_civil_id().isEmpty()) {
            findConsignment.setCustomer_civil_id(null);
        }
        if (findConsignment.getReceiver_civil_id() == null || findConsignment.getReceiver_civil_id().isEmpty()) {
            findConsignment.setReceiver_civil_id(null);
        }
        if (findConsignment.getAwb_3rd_Party() == null || findConsignment.getAwb_3rd_Party().isEmpty()) {
            findConsignment.setAwb_3rd_Party(null);
        }
        if (findConsignment.getHubCode_3rd_Party() == null || findConsignment.getHubCode_3rd_Party().isEmpty()) {
            findConsignment.setHubCode_3rd_Party(null);
        }
        if (findConsignment.getOrderType() == null || findConsignment.getOrderType().isEmpty()) {
            findConsignment.setOrderType(null);
        }
        if (findConsignment.getJntPushStatus() == null || findConsignment.getJntPushStatus().isEmpty()) {
            findConsignment.setJntPushStatus(null);
        }
        if (findConsignment.getBoutiqaatPushStatus() == null || findConsignment.getBoutiqaatPushStatus().isEmpty()) {
            findConsignment.setBoutiqaatPushStatus(null);
        }
        if (findConsignment.getScanType_3rd_Party() == null || findConsignment.getScanType_3rd_Party().isEmpty()) {
            findConsignment.setScanType_3rd_Party(null);
        }
        if (findConsignment.getHubCode() == null || findConsignment.getHubCode().isEmpty()) {
            findConsignment.setHubCode(null);
        }

        List<ConsignmentImpl> results = consignmentRepository.findConsignment(findConsignment.getAwb_3rd_Party(),
                                                                            findConsignment.getBoutiqaatPushStatus(),
                                                                            findConsignment.getConsignmentId(),
                                                                            findConsignment.getConsignment_type(),
                                                                            findConsignment.getCustomer_civil_id(),
                                                                            findConsignment.getCustomer_code(),
                                                                            findConsignment.getCustomer_reference_number(),
                                                                            findConsignment.getHubCode_3rd_Party(),
                                                                            findConsignment.getJntPushStatus(),
                                                                            findConsignment.getOrderType(),
                                                                            findConsignment.getReceiver_civil_id(),
                                                                            findConsignment.getReference_number(),
                                                                            findConsignment.getScanType_3rd_Party(),
                                                                            findConsignment.getService_type_id(),
                                                                            findConsignment.getScanType(),
                                                                            findConsignment.getHubCode(),
                                                                            findConsignment.getStartDate(),
                                                                            findConsignment.getEndDate());

        if (findConsignment.getScanType() != null && !findConsignment.getScanType().isEmpty()) {
            if (findConsignment.getScanType().get(0) != null) {
                boolean readyToPrint = findConsignment.getScanType().get(0).equalsIgnoreCase("ReadyToPrint");   //check Ready to Print
                if (readyToPrint) {
                    results = results.stream().filter(n -> n.getScanType() == null).collect(Collectors.toList());
                    return results;
                }
            }
        }

        return results;
    }
}
