package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.cgentity.AddCgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.CgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.FindCgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.UpdateCgEntity;
import com.mnrclara.api.cg.setup.service.CgEntityService;
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
@Api(tags = {"CgEntity"}, value = "CgEntity Operations related to CgEntityController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "CgEntity", description = " Operations related to CgEntity")})
@RequestMapping("/cgentity")
@RestController
public class CgEntityController {

    @Autowired
    CgEntityService cgEntityService;

    //GET ALL ENTITIES
    @ApiOperation(response = CgEntity.class, value = "Get all Entities") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAllCgEntity() {
        List<CgEntity> cgEntityList = cgEntityService.getAllCgEntities();
        return new ResponseEntity<>(cgEntityList, HttpStatus.OK);
    }

    //GET ENTITY
    @ApiOperation(response = CgEntity.class, value = "Get a Entity") // label for swagger
    @GetMapping("/{entityId}")
    public ResponseEntity<?> getCgEntity(@PathVariable Long entityId, @RequestParam Long clientId,
                                         @RequestParam String companyId, @RequestParam String languageId) {
        CgEntity dbCgEntity = cgEntityService.getCgEntity(entityId, clientId, companyId, languageId);
        log.info("entity: " + dbCgEntity);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //CREATE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Create a Entity") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> createCgEntity(@Valid @RequestBody AddCgEntity addCgEntity, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        CgEntity dbCgEntity = cgEntityService.createCgEntity(addCgEntity, loginUserID);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //UPDATE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Update a Entity") // label for swagger
    @PatchMapping("/{entityId}")
    public ResponseEntity<?> updateCgEntity(@PathVariable Long entityId, @RequestParam Long clientId,
                                            @RequestParam String companyId, @RequestParam String languageId,
                                            @RequestParam String loginUserID, @Valid @RequestBody UpdateCgEntity updateCgEntity)
            throws IllegalAccessException, InvocationTargetException {
        CgEntity dbCgEntity = cgEntityService.updateCgEntity(entityId, clientId, companyId, languageId, loginUserID, updateCgEntity);
        return new ResponseEntity<>(dbCgEntity, HttpStatus.OK);
    }

    //DELETE ENTITY
    @ApiOperation(response = CgEntity.class, value = "Delete a Entity") // label for swagger
    @DeleteMapping("/{entityId}")
    public ResponseEntity<?> deleteCgEntity(@PathVariable Long entityId, @RequestParam Long clientId,
                                            @RequestParam String companyId, @RequestParam String languageId,
                                            @RequestParam String loginUserID) {
        cgEntityService.deleteCgEntity(entityId, clientId, companyId, languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIND ENTITY
    @ApiOperation(response = CgEntity.class, value = "Find a Entity") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findCgEntity(@Valid @RequestBody FindCgEntity findCgEntity) throws Exception {
        List<CgEntity> cgEntityList = cgEntityService.findCgEntity(findCgEntity);
        return new ResponseEntity<>(cgEntityList, HttpStatus.OK);
    }
}
