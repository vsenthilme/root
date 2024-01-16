package com.mnrclara.api.cg.transaction.controller;

import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.AddBSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.BSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.FindBSEffectiveControl;
import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.UpdateBSEffectiveControl;
import com.mnrclara.api.cg.transaction.service.BSEffectiveControlService;
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
@Api(tags = {"BSEffectiveControl"}, value = "BSEffectiveControl Operations related to BSEffectiveController")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "BSEffectiveControl", description = "Operations related to BSEffectiveControl")})
@RequestMapping("/bseffectivecontrol")
@RestController
public class BSEffectiveControlController {

    @Autowired
    private BSEffectiveControlService bsEffectiveControlService;

    // GET ALL
    @ApiOperation(response = BSEffectiveControl.class, value = "Get all BSEffectiveControl details")
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<BSEffectiveControl> bsEffectiveControlList =
                bsEffectiveControlService.getAllBsEffectiveControl();
        return new ResponseEntity<>(bsEffectiveControlList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = BSEffectiveControl.class, value = "Get a BSEffectiveControl") // label for swagger
    @GetMapping("/{validationId}")
    public ResponseEntity<?> getBSEffectiveControl(@PathVariable Long validationId, @RequestParam String languageId,
                                                   @RequestParam String companyId) {

        BSEffectiveControl bsEffectiveControl =
                bsEffectiveControlService.getBSEffectiveControl(companyId, languageId, validationId);
        log.info("BSEffectiveControl : " + bsEffectiveControl);
        return new ResponseEntity<>(bsEffectiveControl, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = BSEffectiveControl.class, value = "Create BSEffectiveControl") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addBSEffectiveControl(@Valid @RequestBody AddBSEffectiveControl newAddBSEffectiveControl,
                                                   @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        BSEffectiveControl createdBSEffectiveControl =
                bsEffectiveControlService.createBSEffectiveControl(newAddBSEffectiveControl, loginUserID);
        return new ResponseEntity<>(createdBSEffectiveControl, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = BSEffectiveControl.class, value = "Update BSEffectiveControl") // label for swagger
    @PatchMapping("/{validationId}")
    public ResponseEntity<?> patchBSEffectiveControl(@PathVariable Long validationId, @RequestParam String languageId,
                                                     @RequestParam String loginUserID, @RequestParam String companyId,
                                                     @RequestBody UpdateBSEffectiveControl updateBSEffectiveControl)
            throws IllegalAccessException, InvocationTargetException {

        BSEffectiveControl dbBSEffectiveControl =
                bsEffectiveControlService.updateBSEffectiveControl(companyId, languageId,
                        validationId, loginUserID, updateBSEffectiveControl);
        return new ResponseEntity<>(dbBSEffectiveControl, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = BSEffectiveControl.class, value = "Delete BSEffectiveControl") // label for swagger
    @DeleteMapping("/{validationId}")
    public ResponseEntity<?> deleteBSEffectiveControl(@PathVariable Long validationId, @RequestParam String companyId,
                                                      @RequestParam String languageId, @RequestParam String loginUserID) {
        bsEffectiveControlService.deleteBSEffectiveControl(companyId, languageId, validationId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = BSEffectiveControl.class, value = "Find BSEffectiveControl") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findBSEffectiveControl(@Valid @RequestBody FindBSEffectiveControl findBSEffectiveControl)
            throws Exception {
        List<BSEffectiveControl> createBSEffectiveControl =
                bsEffectiveControlService.findBSEffectiveControl(findBSEffectiveControl);
        return new ResponseEntity<>(createBSEffectiveControl, HttpStatus.OK);
    }
}
