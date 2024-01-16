package com.mnrclara.api.cg.setup.controller;

import com.mnrclara.api.cg.setup.model.status.AddStatusId;
import com.mnrclara.api.cg.setup.model.status.FindStatus;
import com.mnrclara.api.cg.setup.model.status.StatusId;
import com.mnrclara.api.cg.setup.model.status.UpdateStatusId;
import com.mnrclara.api.cg.setup.service.StatusIdService;
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
@CrossOrigin(origins = "*")
@Api(tags = {"Status"}, value = "Status Operations related to StatusController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Status", description = "Operations related to Status")})
@RequestMapping("/status")
@RestController
public class StatusController {

    @Autowired
    StatusIdService statusService;

    @ApiOperation(response = StatusId.class, value = "Get all Status details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<StatusId> statusIdList = statusService.getAllStatusId();
        return new ResponseEntity<>(statusIdList, HttpStatus.OK);
    }

    @ApiOperation(response = StatusId.class, value = "Get a Status") // label for swagger
    @GetMapping("/{statusId}")
    public ResponseEntity<?> getStatus(@PathVariable Long statusId, @RequestParam String languageId) {
        StatusId status = statusService.getStatusId(statusId, languageId);
        log.info("Status : " + status);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @ApiOperation(response = StatusId.class, value = "Create Status") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postStatus(@Valid @RequestBody AddStatusId newStatus, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        StatusId createdStatusId = statusService.createStatusId(newStatus, loginUserID);
        return new ResponseEntity<>(createdStatusId, HttpStatus.OK);
    }

    @ApiOperation(response = StatusId.class, value = "Update Status") // label for swagger
    @PatchMapping("/{statusId}")
    public ResponseEntity<?> patchStatus(@PathVariable Long statusId, @RequestParam String languageId, @RequestParam String loginUserID,
                                         @Valid @RequestBody UpdateStatusId updateStatusId)
            throws IllegalAccessException, InvocationTargetException {
        StatusId updatedStatusId = statusService.updateStatusId(statusId, languageId, loginUserID, updateStatusId);
        return new ResponseEntity<>(updatedStatusId, HttpStatus.OK);
    }

    @ApiOperation(response = StatusId.class, value = "Delete Status") // label for swagger
    @DeleteMapping("/{statusId}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long statusId, @RequestParam String languageId,
                                          @RequestParam String loginUserID) {
        statusService.deleteStatusId(statusId, languageId, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = StatusId.class, value = "Find Status") // label foe swagger
    @PostMapping("/findStatus")
    public ResponseEntity<?> findStatus(@Valid @RequestBody FindStatus findStatus) throws Exception {
        List<StatusId> createStatusId = statusService.findStatusId(findStatus);
        return new ResponseEntity<>(createStatusId, HttpStatus.OK);

    }

}