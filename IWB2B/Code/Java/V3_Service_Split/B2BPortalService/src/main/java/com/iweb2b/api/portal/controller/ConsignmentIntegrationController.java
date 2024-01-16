package com.iweb2b.api.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iweb2b.api.portal.model.consignment.dto.Consignment;
import com.iweb2b.api.portal.model.consignment.dto.ConsignmentImpl;
import com.iweb2b.api.portal.model.consignment.dto.CountInput;
import com.iweb2b.api.portal.model.consignment.dto.DashboardCountOutput;
import com.iweb2b.api.portal.model.consignment.dto.FindConsignment;
import com.iweb2b.api.portal.service.ConsignmentTrackingService;
import com.iweb2b.api.portal.service.IntegrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Consignment"}, value = "Consignment Operations related to ConsignmentController")
@SwaggerDefinition(tags = {@Tag(name = "ConsignmentTracking", description = "Operations related to Consignment")})
@RequestMapping("/consignment")
@RestController
public class ConsignmentIntegrationController {

    @Autowired
    IntegrationService integrationService;

    @Autowired
    ConsignmentTrackingService consignmentTrackingService;

//    //------------------- To fetch shipment tracking for client-----------------------------------------------------------------------
//    @ApiOperation(response = ConsignmentTracking.class, value = "Get a ConsignmentTracking") // label for swagger
//    @GetMapping("/{referenceNumber}/shipment")
//    public ResponseEntity<?> getConsignmentTracking(@PathVariable String referenceNumber) {
//        ConsignmentTracking dbConsignmentTracking = consignmentTrackingService.getConsignmentTracking(referenceNumber);
//        return new ResponseEntity<>(dbConsignmentTracking, HttpStatus.OK);
//    }
//
//    //--------------------------------------Shipping (AWB) label-----------------------------------------------------------------------
//    @ApiOperation(response = Optional.class, value = "Get a ClientLevel") // label for swagger
//    @GetMapping("/{referenceNumber}/shippingLabel")
//    public ResponseEntity<?> getShippingLabel(@PathVariable String referenceNumber) {
//        byte[] shippingLabelArr = integrationService.getShippingLabel(referenceNumber);
//        return new ResponseEntity<>(shippingLabelArr, HttpStatus.OK);
//    }
//
//    //--------------------------------------Webhook Endpoint-----------------------------------------------------------------------
//    @ApiOperation(response = ConsignmentWebhook.class, value = "Post Consignment Webhook") // label for swagger
//    @PostMapping("/webhook")
//    public ResponseEntity<?> listenWebhook(@RequestBody ConsignmentWebhook consignmentWebhook) throws Exception {
//        JNTResponse webhookResponse = integrationService.createConsignmentWebhook(consignmentWebhook);
//        return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
//    }
//
//    //--------------------------------------JNT Request Endpoint-----------------------------------------------------------------------
//    @ApiOperation(response = JNTOrderCreateRequest.class, value = "Get JNT Request") // label for swagger
//    @GetMapping("/jnt")
//    public ResponseEntity<?> postJNTRequest(@RequestParam String referenceNumber) throws Exception {
//        JNTResponse response = integrationService.postJNTRequest(referenceNumber);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @ApiOperation(response = JNTWebhookRequest.class, value = "Listen JNTWebhook") // label for swagger
//    @PostMapping("/jnt/webhook")
//    public ResponseEntity<?> listenJNTWebhook(@RequestBody JNTWebhookRequest jntWebhookRequest) throws Exception {
//        JNTWebhookEntity webhookResponse = integrationService.createJNTWebhook(jntWebhookRequest);
//        return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
//    }
//
//    @ApiOperation(response = JNTWebhookRequest.class, value = "Print Label") // label for swagger
//    @GetMapping("/jnt/{billCode}/printLabel")
//    public ResponseEntity<?> printLabel(@PathVariable String billCode) throws Exception {
//        JNTPrintLabelResponse printLabelResponse = integrationService.printLabel(billCode);
//        return new ResponseEntity<>(printLabelResponse, HttpStatus.OK);
//    }
//
//    @ApiOperation(response = JNTWebhookRequest.class, value = "Print Label") // label for swagger
//    @GetMapping("/jnt/{billCode}/pdf/printLabel")
//    public ResponseEntity<?> pdfPrintLabel(@PathVariable String billCode) throws Exception {
//        byte[] printLabelResponse = integrationService.pdfPrintLabel(billCode);
//        return new ResponseEntity<>(printLabelResponse, HttpStatus.OK);
//    }
//
//    @ApiOperation(response = JNTWebhookRequest.class, value = "Update Shipsy Order Event") // label for swagger
//    @PostMapping("/jnt/{referenceNumber}/eventUpdate")
//    public ResponseEntity<?> listenJNTWebhook(@RequestBody OrderStatusUpdate orderStatusUpdate, @RequestParam String event) throws Exception {
//        OrderStatusUpdateResponse response = integrationService.updateOrderInShipsy(orderStatusUpdate, event);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @ApiOperation(response = Consignment.class, value = "Get all HubCode Orders") // label for swagger
    @GetMapping("/{hubCode}")
    public ResponseEntity<?> getJNTRequest(@PathVariable String hubCode) throws Exception {
        List<Consignment> response = integrationService.getConsignments(hubCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    //--------------------------------------QatarPost-Request-Endpoint-----------------------------------------------------------------------
//    @ApiOperation(response = QPOrderCreateResponse.class, value = "Post QPOrderCreateRequest") // label for swagger
//    @GetMapping("/qp")
//    public ResponseEntity<?> postQPOrderRequest(@RequestParam String referenceNumber) throws Exception {
//        QPOrderCreateResponse response = integrationService.postQPRequest(referenceNumber);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @ApiOperation(response = JNTWebhookRequest.class, value = "Post QPWebhook")                // label for swagger
//    @PostMapping("/qp/webhook")
//    public ResponseEntity<?> listenQPWebhook(@RequestBody QPTrackingRequest qpTrackingRequest) throws Exception {
//        QPTrackingResponse webhookResponse = integrationService.listenQPWebhook(qpTrackingRequest);
//        return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
//    }

    //--------------------------------------DashBoard Count-----------------------------------------------------------------------

//	@ApiOperation(response = Optional.class, value = "Get JNT Dashboard Count") // label for swagger
//	@PostMapping("/dashboard/getJNTcount")
//	public ResponseEntity<?> getJNTDashboardCount(@RequestBody CountInput countInput) throws Exception {
//		Long dashboard = integrationService.getJNTCount(countInput);
//		return new ResponseEntity<>(dashboard, HttpStatus.OK);
//	}
//
//	@ApiOperation(response = Optional.class, value = "Get Boutiqaat Dashboard Count") // label for swagger
//	@PostMapping("/dashboard/getBoutiqaatCount")
//	public ResponseEntity<?> getBoutiqaatDashboardCount(@RequestBody CountInput countInput) throws Exception {
//		Long dashboard = integrationService.getBoutiqaatCount(countInput);
//		return new ResponseEntity<>(dashboard, HttpStatus.OK);
//	}

    @ApiOperation(response = DashboardCountOutput.class, value = "Get Dashboard Count") // label for swagger
    @PostMapping("/dashboard/getDashboardCount")
    public ResponseEntity<?> getBoutiqaatDashboardCount(@RequestBody CountInput countInput) throws Exception {
        DashboardCountOutput dashboard = integrationService.getDashboardCount(countInput);
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

    @ApiOperation(response = Consignment.class, value = "Search Consignment") // label for swagger
    @PostMapping("/findConsignment")
    public List<ConsignmentImpl> findConsignment(@RequestBody FindConsignment findConsignment)
            throws Exception {
        return integrationService.findConsignment(findConsignment);
    }

//    @ApiOperation(response = String.class, value = "Get JNT Failure Response Code") // label for swagger
//    @PostMapping("/jntFailureResponseCode")
//    public ResponseEntity<?> getJntFailureResponseCode(@RequestBody JNTFailureResponseInput jntFailureResponseDesc) throws Exception {
//        String jNTFailureResponseCode = integrationService.getJNTFailureResponseCode(jntFailureResponseDesc);
//        return new ResponseEntity<>(jNTFailureResponseCode, HttpStatus.OK);
//    }
}
