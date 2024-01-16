package com.mnrclara.api.cg.transaction.controller;


import com.mnrclara.api.cg.transaction.model.ownershiprequest.AddOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.FindOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.OwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.UpdateOwnerShipRequest;
import com.mnrclara.api.cg.transaction.service.OwnerShipRequestService;
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
@Api(tags = {"OwnerShipRequest"}, value = "OwnerShipRequest Operations related to OwnerShipRequestController")// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "OwnerShipRequest", description = "Operations related to OwnerShipRequest")})
@RequestMapping("/ownershiprequest")
@RestController
public class OwnerShipRequestController {


    @Autowired
    private OwnerShipRequestService ownerShipRequestService;

    // GET ALL
    @ApiOperation(response = OwnerShipRequest.class, value = "Get all OwnerShipRequest details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<OwnerShipRequest> ownerShipRequestList = ownerShipRequestService.getAllOwnerShipRequest();
        return new ResponseEntity<>(ownerShipRequestList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = OwnerShipRequest.class, value = "Get a OwnerShipRequest") // label for swagger
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getOwnerShipRequest(@PathVariable Long requestId, @RequestParam String languageId,
                                                 @RequestParam String companyId) {
        OwnerShipRequest ownerShipRequest = ownerShipRequestService.getOwnerShipRequest(requestId, companyId, languageId);
        log.info("OwnerShipRequest : " + ownerShipRequest);
        return new ResponseEntity<>(ownerShipRequest, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = OwnerShipRequest.class, value = "Create OwnerShipRequest") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addOwnerShipRequest(@Valid @RequestBody AddOwnerShipRequest newOwnerShipRequest,
                                                 @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        OwnerShipRequest createdOwnerShipRequest =
                ownerShipRequestService.createOwnerRequest(newOwnerShipRequest, loginUserID);
        return new ResponseEntity<>(createdOwnerShipRequest, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = OwnerShipRequest.class, value = "Update OwnerShipRequest") // label for swagger
    @PatchMapping("/{requestId}")
    public ResponseEntity<?> patchCity(@PathVariable Long requestId, @RequestParam String languageId,
                                       @RequestParam String loginUserID, @RequestParam String companyId,
                                       @RequestBody UpdateOwnerShipRequest updateOwnerShipRequest)
            throws IllegalAccessException, InvocationTargetException {

        OwnerShipRequest dbOwnerShipRequest =
                ownerShipRequestService.updateOwnerShipRequest(requestId, languageId, companyId, loginUserID, updateOwnerShipRequest);
        return new ResponseEntity<>(dbOwnerShipRequest, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = OwnerShipRequest.class, value = "Delete OwnerShipRequest") // label for swagger
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteCity(@PathVariable Long requestId, @RequestParam String companyId,
                                        @RequestParam String languageId, @RequestParam String loginUserID) {
        ownerShipRequestService.deleteOwnerShipRequest(requestId, companyId, languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = OwnerShipRequest.class, value = "Find OwnerShipRequest") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findOwnerShipRequest(@Valid @RequestBody FindOwnerShipRequest findOwnerShipRequest) throws Exception {
        List<OwnerShipRequest> createOwnerShipRequest =
                ownerShipRequestService.findOwnerShipRequest(findOwnerShipRequest);
        return new ResponseEntity<>(createOwnerShipRequest, HttpStatus.OK);
    }

//    // UPDATE
//    @ApiOperation(response = OwnerShipRequest.class, value = "Update OwnerShipRequest") // label for swagger
//    @PatchMapping("/updateRequestId")
//    public ResponseEntity<?> updateOwnerShipRequest(@RequestParam Long requestId, @RequestParam String languageId,
//                                       @RequestParam String loginUserID, @RequestParam String companyId,
//                                       @RequestBody AddOwnerShipRequest addOwnerShipRequest)
//            throws IllegalAccessException, InvocationTargetException {
//
//        OwnerShipRequest dbOwnerShipRequest =
//                ownerShipRequestService.updateShipRequestId(companyId,languageId,requestId,loginUserID,addOwnerShipRequest);
//        return new ResponseEntity<>(dbOwnerShipRequest, HttpStatus.OK);
//    }
}
