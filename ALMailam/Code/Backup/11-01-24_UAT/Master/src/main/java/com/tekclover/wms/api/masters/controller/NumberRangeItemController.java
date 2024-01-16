package com.tekclover.wms.api.masters.controller;


import com.tekclover.wms.api.masters.model.cyclecountscheduler.AddCycleCountScheduler;
import com.tekclover.wms.api.masters.model.cyclecountscheduler.CycleCountScheduler;
import com.tekclover.wms.api.masters.model.cyclecountscheduler.SearchCycleCountScheduler;
import com.tekclover.wms.api.masters.model.cyclecountscheduler.UpdateCycleCountScheduler;
import com.tekclover.wms.api.masters.model.numberrangeitem.AddNumberRangeItem;
import com.tekclover.wms.api.masters.model.numberrangeitem.NumberRangeItem;
import com.tekclover.wms.api.masters.model.numberrangeitem.SearchNumberRangeItem;
import com.tekclover.wms.api.masters.model.numberrangeitem.UpdateNumberRangeItem;
import com.tekclover.wms.api.masters.service.CycleCountSchedulerService;
import com.tekclover.wms.api.masters.service.NumberRangeItemService;
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
import java.text.ParseException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"NumberRangeItem"}, value = "NumberRangeItem  Operations related to NumberRangeItemController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "NumberRangeItem ",description = "Operations related to NumberRangeItem ")})
@RequestMapping("/numberrangeitem")
@RestController
public class NumberRangeItemController {

    @Autowired
    private NumberRangeItemService numberRangeItemService;

    @ApiOperation(response = NumberRangeItem.class, value = "Get all NumberRangeItem details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<NumberRangeItem> numberRangeItemList = numberRangeItemService.getAllNumberRangeItem();
        return new ResponseEntity<>(numberRangeItemList, HttpStatus.OK);
    }

    @ApiOperation(response = NumberRangeItem.class, value = "Get a NumberRangeItem") // label for swagger
    @GetMapping("/{itemTypeId}")
    public ResponseEntity<?> getNumberRangeItem(@PathVariable Long itemTypeId, @RequestParam String companyCodeId, @RequestParam String languageId,
                                                    @RequestParam String plantId, @RequestParam Long sequenceNo, @RequestParam String warehouseId) {
        NumberRangeItem dbNumberRangeItem = numberRangeItemService.getNumberRangeItem(warehouseId,companyCodeId,languageId,plantId,itemTypeId,sequenceNo);
        log.info("NumberRangeItem : " + dbNumberRangeItem);
        return new ResponseEntity<>(dbNumberRangeItem, HttpStatus.OK);
    }


    @ApiOperation(response = NumberRangeItem.class, value = "Create NumberRangeItem") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postNumberRangeItem(@Valid @RequestBody AddNumberRangeItem addNumberRangeItem, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        NumberRangeItem createdNumberRangeItem= numberRangeItemService.createNumberRangeItem(addNumberRangeItem, loginUserID);
        return new ResponseEntity<>(createdNumberRangeItem , HttpStatus.OK);
    }

    @ApiOperation(response = NumberRangeItem.class, value = "Update NumberRangeItem") // label for swagger
    @PatchMapping("/{itemTypeId}")
    public ResponseEntity<?> patchCycleCountScheduler(@PathVariable Long itemTypeId, @RequestParam String companyCodeId, @RequestParam String languageId, @RequestParam String plantId,
                                                      @RequestParam Long sequenceNo, @RequestParam String warehouseId,
                                                      @Valid @RequestBody UpdateNumberRangeItem updateNumberRangeItem, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        NumberRangeItem createdNumberRangeItem = numberRangeItemService.updateNumberRangeItem(companyCodeId,plantId,warehouseId,languageId,itemTypeId,sequenceNo,updateNumberRangeItem, loginUserID);
        return new ResponseEntity<>(createdNumberRangeItem , HttpStatus.OK);
    }

    @ApiOperation(response = NumberRangeItem.class, value = "Delete NumberRangeItem") // label for swagger
    @DeleteMapping("/{itemTypeId}")
    public ResponseEntity<?> deleteNumberRangeItem(@PathVariable Long itemTypeId, @RequestParam String companyCodeId, @RequestParam String languageId, @RequestParam String plantId,@RequestParam Long sequenceNo,
                                                   @RequestParam String warehouseId,@RequestParam String loginUserID) throws ParseException {
        numberRangeItemService.deleteNumberRangeItem(companyCodeId,languageId,plantId,warehouseId,sequenceNo,itemTypeId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = NumberRangeItem.class, value = "Find NumberRangeItem") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findNumberRangeItem(@Valid @RequestBody SearchNumberRangeItem searchNumberRangeItem) throws Exception {
        List<NumberRangeItem> createdNumberRangeItem = numberRangeItemService.findNumberRangeItem(searchNumberRangeItem);
        return new ResponseEntity<>(createdNumberRangeItem, HttpStatus.OK);
    }
}
