package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.clientcontrolgroup.AddClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.ClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.FindClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.UpdateClientControlGroup;
import com.mnrclara.api.cg.setup.service.ClientControlGroupService;
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
@Api(tags = {" ClientControlGroup "}, value = " ClientControlGroup Operations related to ClientControlGroupController ") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = " ClientControlGroup ",description = "Operations related to ClientControlGroup ")})
@RequestMapping("/clientcontrolgroup")
@RestController
public class ClientControlGroupController {

    @Autowired
    ClientControlGroupService clientControlGroupService;

    // GET ALL
    @ApiOperation(response = ClientControlGroup.class, value = "Get all ClientControlGroup details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<ClientControlGroup> clientControlGroupList = clientControlGroupService.getAllClientControlGroup();
        return new ResponseEntity<>(clientControlGroupList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = ClientControlGroup.class, value = "Get a ClientControlGroup") // label for swagger
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClient(@PathVariable Long clientId, @RequestParam String languageId,
                                       @RequestParam String companyId, @RequestParam Long subGroupTypeId,
                                       @RequestParam Long groupTypeId, @RequestParam Long versionNumber) {

        ClientControlGroup clientControlGroup =
                clientControlGroupService.getCLientControlGroup(companyId,languageId,clientId,
                        subGroupTypeId,groupTypeId,versionNumber);
        log.info("ClientControlGroup : " + clientControlGroup);
        return new ResponseEntity<>(clientControlGroup, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = ClientControlGroup.class, value = "Create clientControlGroup") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addClientControlGroup(@Valid @RequestBody AddClientControlGroup newClientControlGroup,
                                                   @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        ClientControlGroup createClientControlGroup =
                clientControlGroupService.createClientControlGroup(newClientControlGroup, loginUserID);
        return new ResponseEntity<>(createClientControlGroup, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = ClientControlGroup.class, value = "Update ClientControlGroup") // label for swagger
    @PatchMapping("/{clientId}")
    public ResponseEntity<?> patchClientControlGroup(@PathVariable Long clientId, @RequestParam String languageId,
                                                     @RequestParam String loginUserID, @RequestParam String companyId,
                                                     @RequestParam Long subGroupTypeId, @RequestParam Long groupTypeId,
                                                     @RequestBody UpdateClientControlGroup updateClientControlGroup,
                                                     @RequestParam Long versionNumber)
            throws IllegalAccessException, InvocationTargetException {

        ClientControlGroup updatedClientControlGroup =
                clientControlGroupService.updateClientControlGroup(companyId,languageId,clientId,versionNumber,subGroupTypeId,
                        groupTypeId,loginUserID,updateClientControlGroup);

        return new ResponseEntity<>(updatedClientControlGroup , HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = ClientControlGroup.class, value = "Delete ClientControlGroup") // label for swagger
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteControlGroup(@PathVariable Long clientId, @RequestParam String companyId,@RequestParam Long versionNumber,
                                                @RequestParam String languageId, @RequestParam Long subGroupTypeId,
                                                @RequestParam Long groupTypeId, @RequestParam String loginUserID) {

        clientControlGroupService.deleteClientControlGroup(companyId, languageId, clientId,versionNumber,
                subGroupTypeId, groupTypeId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = ClientControlGroup.class, value = "Find ClientControlGroup") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findClientControlGroup(@Valid @RequestBody FindClientControlGroup findClientControlGroup) throws Exception {
        List<ClientControlGroup> createClientControlGroup =
                clientControlGroupService.findClientControlGroup(findClientControlGroup);

        return new ResponseEntity<>(createClientControlGroup, HttpStatus.OK);
    }

}
