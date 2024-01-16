package com.tekclover.wms.api.transaction.controller;


import com.tekclover.wms.api.transaction.model.inbound.v2.InboundHeaderV2;
import com.tekclover.wms.api.transaction.service.InvoiceCancellationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@Validated
@Api(tags = {"SupplierInvoiceCancel"}, value = "SupplierInvoice  Operations related to SupplierInvoiceCancellation")// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "SupplierInvoice ", description = "Operations related to SupplierInvoice ")})
@RequestMapping("/invoice")
@RestController
public class InvoiceCancellationController {

    @Autowired
    InvoiceCancellationService invoiceCancellationService;

    @ApiOperation(response = InboundHeaderV2.class, value = "Replace SupplierInvoice")
    @GetMapping("/supplierInvoice/cancellation")
    public ResponseEntity<String> replaceSupplierInvoice(@RequestParam String companyCode, @RequestParam String languageId,
                                                         @RequestParam String plantId, @RequestParam String warehouseId, @RequestParam String oldInvoiceNo,
                                                         @RequestParam String newInvoiceNo, @RequestParam String loginUserId) throws ParseException {

            invoiceCancellationService.replaceSupplierInvoice(companyCode, languageId, plantId, warehouseId, oldInvoiceNo, newInvoiceNo, loginUserId);
            return ResponseEntity.ok("Invoice replacement successful.");
        }

}
