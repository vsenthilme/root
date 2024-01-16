package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.relationshipid.AddRelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.FindRelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.RelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.UpdateRelationShipId;
import com.mnrclara.api.cg.setup.service.RelationShipIdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Value
@Api(tags = {"RelationShipId"},value = "RelationShipId Operations related to RelationShipIdController")//label for swagger
@SwaggerDefinition(tags = {@Tag(name = "RelationShipId",description = "Operation Related to RelationShipId")})
@RequestMapping("/relationshipid")
@RestController
public class RelationShipIdController {

    @Autowired
    private RelationShipIdService relationShipIdService;


    @ApiOperation(response = RelationShipId.class, value = " Get all RelationShipId details ") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<RelationShipId> relationShipIdList = relationShipIdService.getAllRelationShip();
        return new ResponseEntity<>(relationShipIdList, HttpStatus.OK);
    }

    @ApiOperation(response = RelationShipId.class, value = "Get a RelationShipId") // label for swagger
    @GetMapping("/{relationShipId}")
    public ResponseEntity<?> getCountry(@PathVariable Long relationShipId, @RequestParam String companyId,
                                        @RequestParam String languageId) {
        RelationShipId dbRelationShipId =
                relationShipIdService.getRelationShipId(companyId,languageId,relationShipId);
        log.info("RelationShipId : " + dbRelationShipId);
        return new ResponseEntity<>(dbRelationShipId, HttpStatus.OK);
    }

    @ApiOperation(response = RelationShipId.class, value = "Create RelationShipId") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addRelationShipId(@Valid @RequestBody AddRelationShipId newRelationShipId,
                                               @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        RelationShipId createdRelationShipId =
                relationShipIdService.createRelationShip(newRelationShipId, loginUserID);
        return new ResponseEntity<>(createdRelationShipId , HttpStatus.OK);
    }

    @ApiOperation(response = RelationShipId.class, value = "Update RelationShipId") // label for swagger
    @PatchMapping("/{relationShipId}")
    public ResponseEntity<?> patchRelationShipId(@PathVariable Long relationShipId, @RequestParam String languageId,
                                          @RequestParam String loginUserID, @RequestParam String companyId,
                                          @Valid @RequestBody UpdateRelationShipId updateRelationShipId)
            throws IllegalAccessException, InvocationTargetException {

        RelationShipId dbRelationShipId =
                relationShipIdService.updateRelationShipId(companyId,languageId, relationShipId,loginUserID,updateRelationShipId);
        return new ResponseEntity<>(dbRelationShipId , HttpStatus.OK);
    }

    @ApiOperation(response = RelationShipId.class, value = "Delete RelationShipId") // label for swagger
    @DeleteMapping("/{relationShipId}")
    public ResponseEntity<?> deleteCountry(@PathVariable Long relationShipId, @RequestParam String companyId,
                                           @RequestParam String languageId, @RequestParam String loginUserID) {
        relationShipIdService.deleteRelationShipId(companyId,languageId,relationShipId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Search
    @ApiOperation(response = RelationShipId.class, value = "Find RelationShipId") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findRelationShipId(@Valid @RequestBody FindRelationShipId findRelationShipId) throws Exception {
        List<RelationShipId> createRelationShipId = relationShipIdService.findRelationShipId(findRelationShipId);
        return new ResponseEntity<>(createRelationShipId, HttpStatus.OK);
    }

}
