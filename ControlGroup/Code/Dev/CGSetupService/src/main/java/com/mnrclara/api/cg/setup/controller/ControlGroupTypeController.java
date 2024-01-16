package com.mnrclara.api.cg.setup.controller;


import com.mnrclara.api.cg.setup.model.controlgrouptype.AddControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.ControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.FindControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.UpdateControlGroup;
import com.mnrclara.api.cg.setup.model.country.Country;
import com.mnrclara.api.cg.setup.repository.LanguageIdRepository;
import com.mnrclara.api.cg.setup.service.ControlGroupTypeService;
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
@Api(tags = {" ControlGroupType "}, value = " ControlGroupType Operations related to ControlGroupTypeController")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "ControlGroupType", description = "Operations related to ControlGroupType")})
@RequestMapping("/controlgrouptype")
@RestController
public class ControlGroupTypeController {

    @Autowired
    private LanguageIdRepository languageIdRepository;

    @Autowired
    ControlGroupTypeService controlGroupTypeService;

    @ApiOperation(response = ControlGroupType.class, value = "Get all ControlGroupType details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ControlGroupType> controlGroupTypes = controlGroupTypeService.getAllControlGroupType();
        return new ResponseEntity<>(controlGroupTypes, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroupType.class, value = "Get a ControlGroupType") // label for swagger
    @GetMapping("/{groupTypeId}")
    public ResponseEntity<?> getControlGroup(@PathVariable Long groupTypeId, @RequestParam String companyId,
                                             @RequestParam String languageId, @RequestParam Long versionNumber) {
        ControlGroupType controlGroupType =
                controlGroupTypeService.getControlGroupType(groupTypeId, companyId, languageId, versionNumber);
        log.info("ControlGroupType : " + controlGroupType);
        return new ResponseEntity<>(controlGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroupType.class, value = "Create ControlGroupType") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addControlGroupType(@Valid @RequestBody AddControlGroupType newControlGroupType,
                                                 @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        ControlGroupType createControlGroup =
                controlGroupTypeService.createControlGroupType(newControlGroupType, loginUserID);
        return new ResponseEntity<>(createControlGroup, HttpStatus.OK);
    }

    @ApiOperation(response = Country.class, value = "Update ControlGroup") // label for swagger
    @PatchMapping("/{groupTypeId}")
    public ResponseEntity<?> patchCountry(@PathVariable Long groupTypeId, @RequestParam String languageId,
                                          @RequestParam String loginUserID, @RequestParam String companyId,
                                          @Valid @RequestBody UpdateControlGroup updateControlGroup,
                                          @RequestParam Long versionNumber)
            throws IllegalAccessException, InvocationTargetException {

        ControlGroupType updateControlGroupType =
                controlGroupTypeService.updateControlGroupType(groupTypeId, languageId, companyId, loginUserID,
                        versionNumber, updateControlGroup);
        return new ResponseEntity<>(updateControlGroupType, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroupType.class, value = "Delete ControlGroupType") // label for swagger
    @DeleteMapping("/{groupTypeId}")
    public ResponseEntity<?> deleteControlGroupType(@PathVariable Long groupTypeId, @RequestParam String companyId,
                                                    @RequestParam String languageId, @RequestParam Long versionNumber,
                                                    @RequestParam String loginUserID) {
        controlGroupTypeService.deleteControlGroup(groupTypeId, companyId, languageId, versionNumber, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Search
    @ApiOperation(response = ControlGroupType.class, value = "Find ControlGroupType") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findControlGroupType(@Valid @RequestBody FindControlGroupType findControlGroupType) throws Exception {

        List<ControlGroupType> createdControlGroupType =
                controlGroupTypeService.findControlGroupType(findControlGroupType);
        return new ResponseEntity<>(createdControlGroupType, HttpStatus.OK);
    }
}
