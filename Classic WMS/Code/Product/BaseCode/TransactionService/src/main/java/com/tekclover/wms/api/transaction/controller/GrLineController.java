package com.tekclover.wms.api.transaction.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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

import com.tekclover.wms.api.transaction.model.inbound.gr.AddGrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.GrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.PackBarcode;
import com.tekclover.wms.api.transaction.model.inbound.gr.SearchGrLine;
import com.tekclover.wms.api.transaction.model.inbound.gr.UpdateGrLine;
import com.tekclover.wms.api.transaction.service.GrLineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"GrLine"}, value = "GrLine  Operations related to GrLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "GrLine ", description = "Operations related to GrLine ")})
@RequestMapping("/grline")
@RestController
public class GrLineController {

    @Autowired
    GrLineService grlineService;

    @ApiOperation(response = GrLine.class, value = "Get all GrLine details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<GrLine> grlineList = grlineService.getGrLines();
        return new ResponseEntity<>(grlineList, HttpStatus.OK);
    }

    @ApiOperation(response = GrLine.class, value = "Get a GrLine") // label for swagger 
    @GetMapping("/{lineNo}")
    public ResponseEntity<?> getGrLine(@PathVariable Long lineNo, @RequestParam String warehouseId, @RequestParam String companyCode,
                                       @RequestParam String plantId, @RequestParam String languageId,
                                       @RequestParam String preInboundNo, @RequestParam String refDocNumber, @RequestParam String goodsReceiptNo,
                                       @RequestParam String palletCode, @RequestParam String caseCode, @RequestParam String packBarcodes,
                                       @RequestParam String itemCode) {
        GrLine grline = grlineService.getGrLine(warehouseId, companyCode, plantId, languageId, preInboundNo,
                refDocNumber, goodsReceiptNo, palletCode, caseCode, packBarcodes, lineNo, itemCode);
        log.info("GrLine : " + grline);
        return new ResponseEntity<>(grline, HttpStatus.OK);
    }

    // PRE_IB_NO/REF_DOC_NO/PACK_BARCODE/IB_LINE_NO/ITM_CODE
    @ApiOperation(response = GrLine.class, value = "Get a GrLine") // label for swagger 
    @GetMapping("/{lineNo}/putawayline")
    public ResponseEntity<?> getGrLine(@PathVariable Long lineNo, @RequestParam String preInboundNo, @RequestParam String companyCode,
                                       @RequestParam String plantId, @RequestParam String languageId,
                                       @RequestParam String refDocNumber, @RequestParam String packBarcodes, @RequestParam String itemCode) {
        List<GrLine> grline = grlineService.getGrLine(preInboundNo, companyCode, plantId, languageId, refDocNumber, packBarcodes, lineNo, itemCode);
        log.info("GrLine : " + grline);
        return new ResponseEntity<>(grline, HttpStatus.OK);
    }

    @ApiOperation(response = GrLine.class, value = "Search GrLine") // label for swagger
    @PostMapping("/findGrLine")
    public List<GrLine> findGrLine(@RequestBody SearchGrLine searchGrLine)
            throws Exception {
        return grlineService.findGrLine(searchGrLine);
    }

    @ApiOperation(response = GrLine.class, value = "Create GrLine") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postGrLine(@Valid @RequestBody List<AddGrLine> newGrLine,
                                        @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<GrLine> createdGrLine = grlineService.createGrLine(newGrLine, loginUserID);
        return new ResponseEntity<>(createdGrLine, HttpStatus.OK);
    }

    @ApiOperation(response = GrLine.class, value = "Update GrLine") // label for swagger
    @PatchMapping("/{lineNo}")
    public ResponseEntity<?> patchGrLine(@PathVariable Long lineNo, @RequestParam String warehouseId, @RequestParam String companyCode,
                                         @RequestParam String plantId, @RequestParam String languageId,
                                         @RequestParam String preInboundNo, @RequestParam String refDocNumber, @RequestParam String goodsReceiptNo,
                                         @RequestParam String palletCode, @RequestParam String caseCode, @RequestParam String packBarcodes,
                                         @RequestParam String itemCode, @Valid @RequestBody UpdateGrLine updateGrLine,
                                         @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        GrLine createdGrLine =
                grlineService.updateGrLine(warehouseId, companyCode, plantId, languageId, preInboundNo, refDocNumber,
                        goodsReceiptNo, palletCode, caseCode, packBarcodes, lineNo, itemCode, loginUserID, updateGrLine);
        return new ResponseEntity<>(createdGrLine, HttpStatus.OK);
    }

    @ApiOperation(response = GrLine.class, value = "Delete GrLine") // label for swagger
    @DeleteMapping("/{lineNo}")
    public ResponseEntity<?> deleteGrLine(@PathVariable Long lineNo, @RequestParam String warehouseId, @RequestParam String companyCode,
                                          @RequestParam String plantId, @RequestParam String languageId, @RequestParam String preInboundNo,
                                          @RequestParam String refDocNumber, @RequestParam String goodsReceiptNo, @RequestParam String palletCode,
                                          @RequestParam String caseCode, @RequestParam String packBarcodes, @RequestParam String itemCode,
                                          @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        grlineService.deleteGrLine(warehouseId, preInboundNo, refDocNumber, goodsReceiptNo, palletCode, caseCode,
                companyCode, plantId, languageId, packBarcodes, lineNo, itemCode, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //-----------------PACK_BARCODE-GENERATION----------------------------------------------------------------------------
    @ApiOperation(response = GrLine.class, value = "Get a PackBarcode") // label for swagger 
    @GetMapping("/packBarcode")
    public ResponseEntity<?> getPackBarcode(@RequestParam Long acceptQty, @RequestParam Long damageQty, @RequestParam String companyCode,
                                            @RequestParam String plantId, @RequestParam String languageId,
                                            @RequestParam String warehouseId, @RequestParam String loginUserID) {
        List<PackBarcode> packBarcodes = grlineService.generatePackBarcode(acceptQty, damageQty, warehouseId, companyCode, plantId, languageId, loginUserID);
        log.info("packBarcodes : " + packBarcodes);
        return new ResponseEntity<>(packBarcodes, HttpStatus.OK);
    }
}