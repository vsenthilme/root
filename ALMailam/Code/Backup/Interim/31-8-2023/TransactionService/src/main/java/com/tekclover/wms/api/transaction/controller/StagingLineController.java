package com.tekclover.wms.api.transaction.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.validation.Valid;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.v2.AddPreInboundLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.SearchStagingLineV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineEntityV2;
import com.tekclover.wms.api.transaction.model.inbound.staging.v2.StagingLineV2;
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

import com.tekclover.wms.api.transaction.model.inbound.staging.AddStagingLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.AssignHHTUser;
import com.tekclover.wms.api.transaction.model.inbound.staging.CaseConfirmation;
import com.tekclover.wms.api.transaction.model.inbound.staging.SearchStagingLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLineEntity;
import com.tekclover.wms.api.transaction.model.inbound.staging.UpdateStagingLine;
import com.tekclover.wms.api.transaction.service.StagingLineService;
import com.tekclover.wms.api.transaction.service.StagingLineV2Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"StagingLine"}, value = "StagingLine  Operations related to StagingLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "StagingLine ", description = "Operations related to StagingLine ")})
@RequestMapping("/stagingline")
@RestController
public class StagingLineController {

    @Autowired
    StagingLineService staginglineService;

    @Autowired
    StagingLineV2Service stagingLineV2Service;

    @ApiOperation(response = StagingLine.class, value = "Get all StagingLine details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<StagingLineEntity> staginglineList = staginglineService.getStagingLines();
        return new ResponseEntity<>(staginglineList, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Get a StagingLine") // label for swagger 
    @GetMapping("/{lineNo}")
    public ResponseEntity<?> getStagingLine(@PathVariable Long lineNo, @RequestParam String warehouseId,
                                            @RequestParam String refDocNumber, @RequestParam String stagingNo, @RequestParam String palletCode,
                                            @RequestParam String caseCode, @RequestParam String preInboundNo, @RequestParam String itemCode) {
        StagingLineEntity stagingline =
                staginglineService.getStagingLine(warehouseId, preInboundNo, refDocNumber, stagingNo, palletCode, caseCode, lineNo,
                        itemCode);
        log.info("StagingLine : " + stagingline);
        return new ResponseEntity<>(stagingline, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Get a StagingLine") // label for swagger 
    @GetMapping("/{lineNo}/inboundline")
    public ResponseEntity<?> getStagingLineForInboundLine(@PathVariable Long lineNo, @RequestParam String warehouseId,
                                                          @RequestParam String refDocNumber, @RequestParam String preInboundNo, @RequestParam String itemCode) {
        List<StagingLineEntity> stagingline =
                staginglineService.getStagingLine(warehouseId, refDocNumber, preInboundNo, lineNo, itemCode);
        log.info("StagingLine : " + stagingline);
        return new ResponseEntity<>(stagingline, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Search StagingLine") // label for swagger
    @PostMapping("/findStagingLine")
    public List<StagingLineEntity> findStagingLine(@RequestBody SearchStagingLine searchStagingLine) throws Exception {
        return staginglineService.findStagingLine(searchStagingLine);
    }

    @ApiOperation(response = StagingLine.class, value = "Create StagingLine") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postStagingLine(@Valid @RequestBody List<AddStagingLine> newStagingLine, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<StagingLine> createdStagingLine = staginglineService.createStagingLine(newStagingLine, loginUserID);
        return new ResponseEntity<>(createdStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Update StagingLine") // label for swagger
    @PatchMapping("/{lineNo}")
    public ResponseEntity<?> patchStagingLine(@PathVariable Long lineNo, @RequestParam String warehouseId,
                                              @RequestParam String refDocNumber, @RequestParam String stagingNo, @RequestParam String palletCode,
                                              @RequestParam String caseCode, @RequestParam String preInboundNo, @RequestParam String itemCode,
                                              @Valid @RequestBody UpdateStagingLine updateStagingLine, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StagingLineEntity createdStagingLine =
                staginglineService.updateStagingLine(warehouseId, preInboundNo, refDocNumber, stagingNo, palletCode,
                        caseCode, lineNo, itemCode, loginUserID, updateStagingLine);
        return new ResponseEntity<>(createdStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "AssignHHTUser StagingLine") // label for swagger
    @PatchMapping("/assignHHTUser")
    public ResponseEntity<?> assignHHTUser(@RequestBody List<AssignHHTUser> assignHHTUsers, @RequestParam String assignedUserId,
                                           @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        List<StagingLineEntity> updatedStagingLine =
                staginglineService.assignHHTUser(assignHHTUsers, assignedUserId, loginUserID);
        return new ResponseEntity<>(updatedStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Update StagingLine") // label for swagger
    @PatchMapping("/caseConfirmation")
    public ResponseEntity<?> patchStagingLineForCaseConfirmation(@RequestBody List<CaseConfirmation> caseConfirmations,
                                                                 @RequestParam String caseCode, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<StagingLineEntity> createdStagingLine =
                staginglineService.caseConfirmation(caseConfirmations, caseCode, loginUserID);
        return new ResponseEntity<>(createdStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Delete StagingLine") // label for swagger
    @DeleteMapping("/{lineNo}")
    public ResponseEntity<?> deleteStagingLine(@PathVariable Long lineNo, @RequestParam String warehouseId,
                                               @RequestParam String refDocNumber, @RequestParam String stagingNo, @RequestParam String palletCode,
                                               @RequestParam String caseCode, @RequestParam String preInboundNo, @RequestParam String itemCode, @RequestParam String loginUserID) {
        staginglineService.deleteStagingLine(warehouseId, preInboundNo, refDocNumber, stagingNo, palletCode,
                caseCode, lineNo, itemCode, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = StagingLine.class, value = "Delete StagingLine") // label for swagger
    @DeleteMapping("/{lineNo}/cases")
    public ResponseEntity<?> deleteCases(@PathVariable Long lineNo, @RequestParam String preInboundNo, @RequestParam String caseCode,
                                         @RequestParam String itemCode, @RequestParam String loginUserID) {
        staginglineService.deleteCases(preInboundNo, lineNo, itemCode, caseCode, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //====================================================V2======================================================//

//	@ApiOperation(response = StagingLineV2.class, value = "Create StagingLine V2") // label for swagger
//	@PostMapping("/v2")
//	public ResponseEntity<?> postStagingLineV2(@Valid @RequestBody List<AddPreInboundLineV2> newStagingLine,
//											   @RequestParam String warehouseId, @RequestParam String companyCodeId,
//											   @RequestParam String plantId, @RequestParam String languageId,
//											   @RequestParam String loginUserID)
//			throws IllegalAccessException, InvocationTargetException {
//		List<StagingLineEntityV2> createdStagingLine =
//				stagingLineV2Service.createStagingLine(newStagingLine, warehouseId, companyCodeId, plantId, languageId, loginUserID);
//		return new ResponseEntity<>(createdStagingLine , HttpStatus.OK);
//	}

    @ApiOperation(response = StagingLineV2.class, value = "Get a StagingLine V2") // label for swagger
    @GetMapping("/{lineNo}/v2")
    public ResponseEntity<?> getStagingLineV2(@PathVariable Long lineNo, @RequestParam String companyCode,
                                              @RequestParam String plantId, @RequestParam String languageId, @RequestParam String warehouseId,
                                              @RequestParam String refDocNumber, @RequestParam String stagingNo, @RequestParam String palletCode,
                                              @RequestParam String caseCode, @RequestParam String preInboundNo, @RequestParam String itemCode) {
        StagingLineEntity stagingline =
                stagingLineV2Service.getStagingLine(companyCode, plantId, languageId, warehouseId,
                        preInboundNo, refDocNumber, stagingNo, palletCode,
                        caseCode, lineNo, itemCode);
        log.info("StagingLine : " + stagingline);
        return new ResponseEntity<>(stagingline, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLineV2.class, value = "Get a StagingLine V2") // label for swagger
    @GetMapping("/{lineNo}/inboundline/v2")
    public ResponseEntity<?> getStagingLineForInboundLineV2(@PathVariable Long lineNo, @RequestParam String companyCode,
                                                            @RequestParam String plantId, @RequestParam String languageId,
                                                            @RequestParam String warehouseId, @RequestParam String refDocNumber,
                                                            @RequestParam String preInboundNo, @RequestParam String itemCode) {
        List<StagingLineEntityV2> stagingline =
                stagingLineV2Service.getStagingLines(companyCode, plantId, languageId, warehouseId, refDocNumber, preInboundNo, lineNo, itemCode);
        log.info("StagingLine : " + stagingline);
        return new ResponseEntity<>(stagingline, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLineV2.class, value = "Search StagingLine V2") // label for swagger
    @PostMapping("/findStagingLine/v2")
    public List<StagingLineEntityV2> findStagingLineV2(@RequestBody SearchStagingLineV2 searchStagingLine) throws Exception {
        return stagingLineV2Service.findStagingLine(searchStagingLine);
    }

    @ApiOperation(response = StagingLineV2.class, value = "Update StagingLine V2") // label for swagger
    @PatchMapping("/v2/{lineNo}")
    public ResponseEntity<?> patchStagingLineV2(@PathVariable Long lineNo, @RequestParam String warehouseId, @RequestParam String companyCode,
                                                @RequestParam String plantId, @RequestParam String languageId, @RequestParam String refDocNumber,
                                                @RequestParam String stagingNo, @RequestParam String palletCode, @RequestParam String caseCode,
                                                @RequestParam String preInboundNo, @RequestParam String itemCode,
                                                @Valid @RequestBody StagingLineEntityV2 updateStagingLine, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StagingLineEntity createdStagingLine =
                stagingLineV2Service.updateStagingLine(companyCode, plantId, languageId, warehouseId, preInboundNo, refDocNumber, stagingNo, palletCode,
                        caseCode, lineNo, itemCode, loginUserID, updateStagingLine);
        return new ResponseEntity<>(createdStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLineV2.class, value = "AssignHHTUser StagingLine") // label for swagger
    @PatchMapping("/assignHHTUser/v2")
    public ResponseEntity<?> assignHHTUserV2(@RequestBody List<AssignHHTUser> assignHHTUsers, @RequestParam String companyCode,
                                             @RequestParam String plantId, @RequestParam String languageId,
                                             @RequestParam String assignedUserId, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<StagingLineEntityV2> updatedStagingLine =
                stagingLineV2Service.assignHHTUser(assignHHTUsers, companyCode, plantId, languageId, assignedUserId, loginUserID);
        return new ResponseEntity<>(updatedStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLine.class, value = "Update StagingLine") // label for swagger
    @PatchMapping("/caseConfirmation/v2")
    public ResponseEntity<?> patchStagingLineForCaseConfirmationV2(@RequestBody List<CaseConfirmation> caseConfirmations, @RequestParam String companyCode,
                                                                   @RequestParam String plantId, @RequestParam String languageId,
                                                                   @RequestParam String caseCode, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        List<StagingLineEntityV2> createdStagingLine =
                stagingLineV2Service.caseConfirmation(caseConfirmations, caseCode, companyCode, plantId, languageId, loginUserID);
        return new ResponseEntity<>(createdStagingLine, HttpStatus.OK);
    }

    @ApiOperation(response = StagingLineV2.class, value = "Delete StagingLine V2") // label for swagger
    @DeleteMapping("/v2/{lineNo}")
    public ResponseEntity<?> deleteStagingLineV2(@PathVariable Long lineNo, @RequestParam String companyCode,
                                                 @RequestParam String plantId, @RequestParam String languageId,
                                                 @RequestParam String warehouseId, @RequestParam String refDocNumber,
                                                 @RequestParam String stagingNo, @RequestParam String palletCode,
                                                 @RequestParam String caseCode, @RequestParam String preInboundNo,
                                                 @RequestParam String itemCode, @RequestParam String loginUserID) {
        stagingLineV2Service.deleteStagingLine(companyCode, plantId, languageId, warehouseId,
                preInboundNo, refDocNumber, stagingNo, palletCode,
                caseCode, lineNo, itemCode, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = StagingLine.class, value = "Delete StagingLine") // label for swagger
    @DeleteMapping("/v2/{lineNo}/cases")
    public ResponseEntity<?> deleteCasesV2(@PathVariable Long lineNo, @RequestParam String preInboundNo, @RequestParam String caseCode,
                                           @RequestParam String itemCode, @RequestParam String loginUserID) {
        stagingLineV2Service.deleteCases(preInboundNo, lineNo, itemCode, caseCode, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}