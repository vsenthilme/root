package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.subgrouptype.AddSubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.FindSubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.SubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.UpdateSubGroupType;
import com.mnrclara.api.cg.setup.service.SubGroupTypeService;
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
@Api(tags = {"SubGroupType"}, value = "SubGroupType Operations related to SubGroupTypeController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "SubGroupType",description = "Operations related to SubGroupType")})
@RequestMapping("/subgrouptype")
@RestController
public class SubGroupTypeController {

    @Autowired
    SubGroupTypeService subGroupTypeService;

    // GET ALL
    @ApiOperation(response = SubGroupType.class, value = "Get all SubGroupType details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<SubGroupType> subGroupTypeList = subGroupTypeService.getAllSubgroupType();
        return new ResponseEntity<>(subGroupTypeList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = SubGroupType.class, value = "Get a SubGroup") // label for swagger
    @GetMapping("/{subGroupTypeId}")
    public ResponseEntity<?> getSubGroup(@PathVariable Long subGroupTypeId, @RequestParam String languageId,
                                         @RequestParam Long groupTypeId, @RequestParam String companyId,
                                         @RequestParam Long versionNumber) {
        SubGroupType dbSubGroupType =
                subGroupTypeService.getSubGroupType(subGroupTypeId, companyId, groupTypeId,versionNumber, languageId);
        log.info("SubGroup : " + dbSubGroupType);
        return new ResponseEntity<>(dbSubGroupType, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = SubGroupType.class, value = "Create SubGroupType") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addSubGroup(@Valid @RequestBody AddSubGroupType newSubGroup, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        SubGroupType createSubgroup =
                subGroupTypeService.createSubGroup(newSubGroup, loginUserID);
        return new ResponseEntity<>(createSubgroup , HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = SubGroupType.class, value = "Update SubGroupType") // label for swagger
    @PatchMapping("/{subGroupTypeId}")
    public ResponseEntity<?> patchSubGroup(@PathVariable Long subGroupTypeId, @RequestParam String languageId,
                                           @RequestParam Long groupTypeId, @RequestParam String loginUserID,
                                           @RequestParam String companyId,@RequestParam Long versionNumber,
                                           @RequestBody UpdateSubGroupType updateSubGroupType)
            throws IllegalAccessException, InvocationTargetException {

        SubGroupType updateSubGroupTypeId =
                subGroupTypeService.updateSubGroup(subGroupTypeId, groupTypeId, companyId,
                        languageId, loginUserID,versionNumber,updateSubGroupType);
        return new ResponseEntity<>(updateSubGroupTypeId, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = SubGroupType.class, value = "Delete SubGroup") // label for swagger
    @DeleteMapping("/{subGroupTypeId}")
    public ResponseEntity<?> deleteSubGroupType(@PathVariable Long subGroupTypeId, @RequestParam String companyId,
                                            @RequestParam Long groupTypeId,@RequestParam Long versionNumber,
                                            @RequestParam String languageId, @RequestParam String loginUserID) {
        subGroupTypeService.deleteSubGroup(subGroupTypeId, groupTypeId, companyId, languageId,versionNumber, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = SubGroupType.class, value = "Find SubGroupType") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findSubGroupType(@Valid @RequestBody FindSubGroupType findSubGroupType) throws Exception {
        List<SubGroupType> createSubGroupType = subGroupTypeService.findSubGroupType(findSubGroupType);
        return new ResponseEntity<>(createSubGroupType, HttpStatus.OK);
    }
}
