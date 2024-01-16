package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.controlgroup.AddControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.ControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.FindControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.UpdateControlGroup;
import com.mnrclara.api.cg.setup.service.ControlGroupService;
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

import javax.naming.ldap.Control;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {" ControlGroup "}, value = " ControlGroup  Operations related to ControlGroupController ") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = " CompanyId ",description = "Operations related to CompanyId ")})
@RequestMapping("/controlgroup")
@RestController
public class ControlGroupController {

    @Autowired
    ControlGroupService controlGroupService;

    @ApiOperation(response = ControlGroup.class, value = "Get all ControlGroup details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ControlGroup> controlGroupList = controlGroupService.getAllControlGroup();
        return new ResponseEntity<>(controlGroupList, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroup.class, value = "Get a ControlGroup") // label for swagger
    @GetMapping("/{groupId}")
    public ResponseEntity<?> getControlGroup(@PathVariable Long groupId, @RequestParam String companyId,
                                             @RequestParam Long versionNumber,@RequestParam String languageId,
                                             @RequestParam Long groupTypeId) {
        ControlGroup controlGroup =
                controlGroupService.getControlGroup(companyId, languageId, groupId, groupTypeId,versionNumber);
        log.info("ControlGroup : " + controlGroup);
        return new ResponseEntity<>(controlGroup, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroup.class, value = "Create ControlGroup") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postControlGroup(@Valid @RequestBody AddControlGroup newControlGroup,
                                           @RequestParam String loginUserID) throws IllegalAccessException, InvocationTargetException {
        ControlGroup createdControlGroup = controlGroupService.createControlGroup(newControlGroup, loginUserID);
        return new ResponseEntity<>(createdControlGroup , HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroup.class, value = "Update ControlGroup") // label for swagger
    @PatchMapping("/{groupId}")
    public ResponseEntity<?> patchControlGroup(@PathVariable Long groupId, @RequestParam String companyId,
                                               @RequestParam String languageId, @RequestParam String loginUserID,
                                               @Valid @RequestBody UpdateControlGroup updateControlGroup,
                                               @RequestParam Long groupTypeId,@RequestParam Long versionNumber )
            throws IllegalAccessException, InvocationTargetException {

        ControlGroup updatedControlGroup =
                controlGroupService.updateControlGroup(companyId, languageId, groupId,versionNumber,
                        groupTypeId, loginUserID, updateControlGroup);
        return new ResponseEntity<>(updatedControlGroup, HttpStatus.OK);
    }

    @ApiOperation(response = ControlGroup.class, value = " Delete ControlGroup ") // label for swagger
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteControlGroup(@PathVariable Long groupId, @RequestParam String languageId,
                                                @RequestParam String companyId, @RequestParam Long groupTypeId,
                                                @RequestParam String loginUserID,@RequestParam Long versionNumber) {
        controlGroupService.deleteControlGroup(companyId, languageId,versionNumber,groupId,groupTypeId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Search
    @ApiOperation(response = ControlGroup.class, value = "Find ControlGroup") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findControlGroup(@Valid @RequestBody FindControlGroup findControlGroup) throws Exception {
        List<ControlGroup> findControlGroupId = controlGroupService.findControlGroup(findControlGroup);
        return new ResponseEntity<>(findControlGroupId, HttpStatus.OK);
    }
}
