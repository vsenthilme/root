package com.tekclover.wms.api.transaction.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekclover.wms.api.transaction.model.inbound.AddInboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.InboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.InboundHeaderEntity;
import com.tekclover.wms.api.transaction.model.inbound.SearchInboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.UpdateInboundHeader;
import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundHeaderEntity;
import com.tekclover.wms.api.transaction.model.warehouse.inbound.confirmation.AXApiResponse;
import com.tekclover.wms.api.transaction.service.InboundHeaderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"InboundHeader"}, value = "InboundHeader  Operations related to InboundHeaderController")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "InboundHeader ", description = "Operations related to InboundHeader ")})
@RequestMapping("/inboundheader")
@RestController
public class InboundHeaderController {

    @Autowired
    InboundHeaderService inboundheaderService;

    @ApiOperation(response = InboundHeader.class, value = "Get all InboundHeader details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<InboundHeader> inboundheaderList = inboundheaderService.getInboundHeaders();
        return new ResponseEntity<>(inboundheaderList, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Get a InboundHeader") // label for swagger 
    @GetMapping("/{refDocNumber}")
    public ResponseEntity<?> getInboundHeader(@PathVariable String refDocNumber, @RequestParam String companyCode,
                                              @RequestParam String plantId, @RequestParam String languageId, @RequestParam String warehouseId,
                                              @RequestParam String preInboundNo) {
        InboundHeaderEntity inboundheader = inboundheaderService.getInboundHeader(warehouseId, companyCode, plantId, languageId, refDocNumber, preInboundNo);
        log.info("InboundHeader : " + inboundheader);
        return new ResponseEntity<>(inboundheader, HttpStatus.OK);
    }

    @ApiOperation(response = PreInboundHeaderEntity.class, value = "Get a PreInboundHeader") // label for swagger 
    @GetMapping("/inboundconfirm")
    public ResponseEntity<?> getPreInboundHeader(@RequestParam String warehouseId) {
        List<InboundHeaderEntity> inboundheader = inboundheaderService.getInboundHeaderWithStatusId(warehouseId);
        log.info("InboundHeader : " + inboundheader);
        return new ResponseEntity<>(inboundheader, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Search InboundHeader") // label for swagger
    @PostMapping("/findInboundHeader")
    public List<InboundHeader> findInboundHeader(@RequestBody SearchInboundHeader searchInboundHeader)
            throws Exception {
        return inboundheaderService.findInboundHeader(searchInboundHeader);
    }

    //Stream
    @ApiOperation(response = InboundHeader.class, value = "Search InboundHeader New") // label for swagger
    @PostMapping("/findInboundHeaderNew")
    public Stream<InboundHeader> findInboundHeaderNew(@RequestBody SearchInboundHeader searchInboundHeader)
            throws Exception {
        return inboundheaderService.findInboundHeaderNew(searchInboundHeader);
    }

    @ApiOperation(response = InboundHeader.class, value = "Create InboundHeader") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postInboundHeader(@Valid @RequestBody AddInboundHeader newInboundHeader,
                                               @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        InboundHeader createdInboundHeader = inboundheaderService.createInboundHeader(newInboundHeader, loginUserID);
        return new ResponseEntity<>(createdInboundHeader, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Create InboundHeader") // label for swagger
    @GetMapping("/replaceASN")
    public ResponseEntity<?> replaceASN(@RequestParam String refDocNumber, @RequestParam String preInboundNo,
                                        @RequestParam String companyCode, @RequestParam String plantId, @RequestParam String languageId,
                                        @RequestParam String asnNumber)
            throws IllegalAccessException, InvocationTargetException {
        inboundheaderService.replaceASN(refDocNumber, companyCode, plantId, languageId, preInboundNo, asnNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Update InboundHeader") // label for swagger
    @PatchMapping("/{refDocNumber}")
    public ResponseEntity<?> patchInboundHeader(@PathVariable String refDocNumber, @RequestParam String warehouseId,
                                                @RequestParam String companyCode, @RequestParam String plantId, @RequestParam String languageId,
                                                @RequestParam String preInboundNo, @Valid @RequestBody UpdateInboundHeader updateInboundHeader,
                                                @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        InboundHeader createdInboundHeader =
                inboundheaderService.updateInboundHeader(warehouseId, companyCode, plantId, languageId, refDocNumber, preInboundNo, loginUserID, updateInboundHeader);
        return new ResponseEntity<>(createdInboundHeader, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Inbound Header & Line Confirm") // label for swagger
    @GetMapping("/confirmIndividual")
    public ResponseEntity<?> updateInboundHeaderConfirm(@RequestParam String warehouseId, @RequestParam String preInboundNo,
                                                        @RequestParam String companyCode, @RequestParam String plantId, @RequestParam String languageId,
                                                        @RequestParam String refDocNumber, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        AXApiResponse createdInboundHeaderResponse =
                inboundheaderService.updateInboundHeaderConfirm(warehouseId, companyCode, plantId, languageId, preInboundNo, refDocNumber, loginUserID);
        return new ResponseEntity<>(createdInboundHeaderResponse, HttpStatus.OK);
    }

    @ApiOperation(response = InboundHeader.class, value = "Delete InboundHeader") // label for swagger
    @DeleteMapping("/{refDocNumber}")
    public ResponseEntity<?> deleteInboundHeader(@PathVariable String refDocNumber, @RequestParam String warehouseId,
                                                 @RequestParam String companyCode, @RequestParam String plantId, @RequestParam String languageId,
                                                 @RequestParam String preInboundNo, @RequestParam String loginUserID) {
        inboundheaderService.deleteInboundHeader(warehouseId, companyCode, plantId, languageId, refDocNumber, preInboundNo, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}