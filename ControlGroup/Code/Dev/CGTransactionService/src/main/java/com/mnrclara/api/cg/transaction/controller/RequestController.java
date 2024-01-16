package com.mnrclara.api.cg.transaction.controller;


import com.mnrclara.api.cg.transaction.model.Requestid.AddRequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.FindRequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.RequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.UpdateRequestId;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.FindOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.OwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.UpdateOwnerShipRequest;
import com.mnrclara.api.cg.transaction.service.RequestIdService;
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
@Api(tags = {"RequestId"}, value = "RequestId Operations related to RequestIdController")// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "RequestId", description = "Operations related to RequestId")})
@RequestMapping("/requestId")
@RestController
public class RequestController {

    @Autowired
    private RequestIdService requestIdService;

    // GET ALL
    @ApiOperation(response = RequestId.class, value = "Get all RequestId details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<RequestId> requestIdList = requestIdService.getRequestIdAll();
        return new ResponseEntity<>(requestIdList, HttpStatus.OK);
    }

    // GET
    @ApiOperation(response = RequestId.class, value = "Get a RequestId") // label for swagger
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getRequestId(@PathVariable Long requestId) {
        List<RequestId> dbRequestId = requestIdService.getRequestId(requestId);
        log.info("RequestId : " + dbRequestId);
        return new ResponseEntity<>(dbRequestId, HttpStatus.OK);
    }

    // CREATE
    @ApiOperation(response = RequestId.class, value = "Create RequestId ") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> addRequestId(@Valid @RequestBody List<AddRequestId> newRequestId,
                                                   @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        List<RequestId> createdBSEffectiveControl =
                requestIdService.createRequestId(newRequestId, loginUserID);
        return new ResponseEntity<>(createdBSEffectiveControl, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(response = RequestId.class, value = "Update RequestId") // label for swagger
    @PatchMapping("/{requestId}")
    public ResponseEntity<?> patchRequestId(@PathVariable Long requestId, @RequestParam String loginUserID,
                                       @RequestBody List<AddRequestId> updateRequestId)
            throws IllegalAccessException, InvocationTargetException {

       List<RequestId> dbRequestId =
                requestIdService.updateRequestId(requestId, loginUserID, updateRequestId);
        return new ResponseEntity<>(dbRequestId, HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(response = RequestId.class, value = "Delete RequestId") // label for swagger
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteRequestId(@PathVariable Long requestId, @RequestParam Long id,@RequestParam String loginUserID) {
        requestIdService.deleteRequestId(id,requestId,loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SEARCH
    @ApiOperation(response = RequestId.class, value = "Find RequestId") // label for swagger
    @PostMapping("/find")
    public ResponseEntity<?> findRequestId(@Valid @RequestBody FindRequestId findRequestId) throws Exception {
        List<RequestId> createRequestId =
                requestIdService.findRequestId(findRequestId);
        return new ResponseEntity<>(createRequestId, HttpStatus.OK);
    }


}
