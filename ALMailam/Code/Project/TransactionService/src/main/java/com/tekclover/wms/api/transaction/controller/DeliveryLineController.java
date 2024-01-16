package com.tekclover.wms.api.transaction.controller;

import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.AddDeliveryLine;
import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.DeliveryLine;
import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.SearchDeliveryLine;
import com.tekclover.wms.api.transaction.model.deliverymodule.deliveryline.UpdateDeliveryLine;
import com.tekclover.wms.api.transaction.repository.DeliveryLineRepository;
import com.tekclover.wms.api.transaction.service.DeliveryLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"DeliveryLine"}, value = "DeliveryLine  Operations related to DeliveryLineController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CountBar ",description = "Operations related to CountBar ")})
@RequestMapping("/deliveryline")
@RestController
public class DeliveryLineController {

    @Autowired
    private DeliveryLineService deliveryLineService;

    @Autowired
    private DeliveryLineRepository deliveryLineRepository;

    // Get All
    @ApiOperation(response = DeliveryLine.class, value = "Get all DeliveryLine details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<DeliveryLine> deliveryLineList = deliveryLineService.getAllDeliveryLine();
        return new ResponseEntity<>(deliveryLineList, HttpStatus.OK);
    }


    // Get DeliveryLine
    @ApiOperation(response = DeliveryLine.class, value = "Get a DeliveryLine") // label for swagger
    @GetMapping("/{deliveryNo}")
    public ResponseEntity<?> getDeliveryLine(@PathVariable String deliveryNo,@RequestParam String companyCodeId,
                                             @RequestParam String invoiceNumber,@RequestParam String refDocNumber,
                                             @RequestParam String languageId,@RequestParam String plantId,
                                             @RequestParam String itemCode,@RequestParam Long lineNo,
                                             @RequestParam String warehouseId){

        DeliveryLine deliveryLine=
                deliveryLineService.getDeliveryLine(companyCodeId,plantId,warehouseId,invoiceNumber,
                        refDocNumber,languageId,deliveryNo,itemCode,lineNo);

        log.info("deliveryLine");
        return new ResponseEntity<>(deliveryLine,HttpStatus.OK);
    }

    // Create DeliveryLine
    @ApiOperation(response = DeliveryLine.class, value = "Create DeliveryLine")
    @PostMapping("")
    public ResponseEntity<?> postDeliveryLine(@Valid @RequestBody AddDeliveryLine newDeliveryLine,
                                                @RequestParam String loginUserID) throws
            IllegalAccessException, InvocationTargetException {

        DeliveryLine createdDeliveryLine =
                deliveryLineService.createDeliveryLine(newDeliveryLine, loginUserID);
        return new ResponseEntity<>(createdDeliveryLine , HttpStatus.OK);
    }

    //Update DeliveryLine
    @ApiOperation(response = DeliveryLine.class, value = "Update DeliveryLine")// label for swagger
    @PatchMapping("/{deliveryNo}")
    public ResponseEntity<?> patchDeliveryLine(@RequestParam String companyCodeId, @RequestParam String plantId,
                                               @RequestParam String itemCode,@RequestParam Long lineNo,
                                               @RequestParam String invoiceNumber,@RequestParam String refDocNumber,
                                               @RequestParam String warehouseId,@RequestParam String loginUserID,
                                               @RequestParam String languageId, @PathVariable String deliveryNo,
                                               @Valid @RequestBody UpdateDeliveryLine updateDeliveryLine)
            throws IllegalAccessException,InvocationTargetException {

        DeliveryLine deliveryLine = deliveryLineService.UpdateDeliveryLine(companyCodeId,plantId,
                warehouseId, deliveryNo,languageId,itemCode,lineNo,invoiceNumber,refDocNumber,updateDeliveryLine,loginUserID);
        return new ResponseEntity<>(deliveryLine , HttpStatus.OK);

    }

    // Delete DeliveryLine
    @ApiOperation(response = DeliveryLine.class, value = "Delete DeliveryLine") // label for swagger
    @DeleteMapping("/{deliveryNo}")
    public ResponseEntity<?> deleteDeliveryLine(@PathVariable String deliveryNo,@RequestParam String companyCodeId,
                                                @RequestParam String plantId,@RequestParam String warehouseId,
                                                @RequestParam String itemCode,@RequestParam Long lineNo,
                                                @RequestParam String languageId,@RequestParam String loginUserID,
                                                @RequestParam String refDocNumber, @RequestParam String invoiceNumber){

        deliveryLineService.deleteDeliveryLine(companyCodeId,plantId,warehouseId,
                deliveryNo,refDocNumber,invoiceNumber,itemCode,lineNo,languageId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search DeliveryLine
    @ApiOperation(response = DeliveryLine.class, value = "Find DeliveryLine") // label for swagger
    @PostMapping("/findDeliveryLine")
    public ResponseEntity<?> findDeliveryLine(@Valid @RequestBody SearchDeliveryLine searchDeliveryLine) throws Exception {

        List<DeliveryLine> createdDeliveryLine =
                deliveryLineService.findDeliveryLine(searchDeliveryLine);
        return new ResponseEntity<>(createdDeliveryLine, HttpStatus.OK);

    }
}
