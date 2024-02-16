package com.mnrclara.api.crm.controller;

import com.mnrclara.api.crm.model.inquiry.AddInquiry;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.inquiry.SearchInquiry;
import com.mnrclara.api.crm.model.inquiry.UpdateInquiry;
import com.mnrclara.api.crm.model.wordpress.AddInquiryWebsite;
import com.mnrclara.api.crm.service.InquiryService;
import com.mnrclara.api.crm.service.InquiryWebsiteService;
import com.mnrclara.api.crm.service.NotificationService;
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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@Api(tags = {"Inquiry"}, value = "Inquiry Operations related to InquiryController") // label for swagger
@SwaggerDefinition(tags = {@Tag(name = "Inquiry", description = "Operations related to Inquiry")})
@RequestMapping("/inquiry")
@RestController
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    InquiryWebsiteService inquiryWebsiteService;

    @ApiOperation(response = Inquiry.class, value = "Get all Inquiry details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Inquiry> inquiryList = inquiryService.getInquiries();
        return new ResponseEntity<>(inquiryList, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Get a Inquiry") // label for swagger 
    @GetMapping("/{inquiryNumber}")
    public ResponseEntity<?> getInquiry(@PathVariable String inquiryNumber) {
        Inquiry inquiry = inquiryService.getInquiry(inquiryNumber);
        log.info("Inquiry : " + inquiry);
        if (inquiry != null) {
            return new ResponseEntity<>(inquiry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The given InquiryNumber : " + inquiryNumber + " doesn't exists", HttpStatus.OK);
        }
    }

    @ApiOperation(response = Long.class, value = "Get a Inquiry Count ") // label for swagger 
    @GetMapping("/{classId}/count")
    public ResponseEntity<?> getInquiryCunt(@PathVariable Long classId) {
        Long inquiryCount = inquiryService.getInquiryCountByClassId(classId);
        Map<String, Long> response = new HashMap<>();
        response.put("inquiryNumberCount", inquiryCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Search Inquiry") // label for swagger
    @PostMapping("/findInquiry")
    public List<Inquiry> findInquiry(@RequestBody SearchInquiry searchInquiry) throws ParseException {
        return inquiryService.findInquiries(searchInquiry);
    }

    @ApiOperation(response = Inquiry.class, value = "Create Inquiry") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postInquiry(@Valid @RequestBody AddInquiry newInquiry,
                                         @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryService.createInquiry(newInquiry, loginUserID);
        this.notificationService.saveNotifications(Arrays.asList("MEA","RM","MW"), null,
                "A new inquiry " + createdInquiry.getInquiryNumber() + " has been received on ",
                "Inquiry",
                createdInquiry.getCreatedOn(), createdInquiry.getCreatedBy());
//        return new ResponseEntity<>(new Inquiry(), HttpStatus.OK);
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Create Inquiry") // label for swagger
    @PostMapping("/external")
    public ResponseEntity<?> postInquiryWebsite(@Valid @RequestBody AddInquiryWebsite newInquiryWebsite)
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryWebsiteService.createInquiryWebsite(newInquiryWebsite);
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Assign Inquiry") // label for swagger
    @PatchMapping("/assignInquiry")
    public ResponseEntity<?> assignInquiry(@RequestParam String inquiryNumber,
                                           @RequestParam String loginUserID, @Valid @RequestBody UpdateInquiry updateInquiry)
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryService.assignInquiry(inquiryNumber, loginUserID, updateInquiry);
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Update Assign Inquiry") // label for swagger
    @PatchMapping("/{inquiryNumber}/assignInquiry")
    public ResponseEntity<?> patchAssignInquiry(@PathVariable String inquiryNumber,
                                                @RequestParam String loginUserID, @Valid @RequestBody UpdateInquiry updateInquiry)
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryService.updateAssignInquiry(inquiryNumber, loginUserID, updateInquiry);
        this.notificationService.saveNotifications(
                createdInquiry.getAssignedUserId() != null ? Arrays.asList(createdInquiry.getAssignedUserId()) : new ArrayList<>(),
                null,
                "Inquiry " + createdInquiry.getInquiryNumber() + " was assigned to you on ", "Inquiry Assign",
                createdInquiry.getUpdatedOn(), createdInquiry.getUpdatedBy());
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Update Assign Inquiry") // label for swagger
    @PatchMapping("/{inquiryNumber}/updateInquiryIntake")
    public ResponseEntity<?> patchInquiryIntake(@PathVariable String inquiryNumber,
                                                @RequestParam String loginUserID,
                                                @Valid @RequestBody UpdateInquiry updateInquiry)
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryService.updateInquiryIntake(inquiryNumber, loginUserID, updateInquiry);
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Update Assign Inquiry") // label for swagger
    @PatchMapping("/{inquiryNumber}/updateValiationStatus")
    public ResponseEntity<?> patchUpdateValiationStatus(@PathVariable String inquiryNumber,
                                                        @RequestParam Long status,
                                                        @RequestParam String loginUserID,
                                                        @Valid @RequestBody UpdateInquiry updateInquiry
    )
            throws IllegalAccessException, InvocationTargetException {
        Inquiry createdInquiry = inquiryService.updateValiationStatus(inquiryNumber, loginUserID, updateInquiry, status);
        return new ResponseEntity<>(createdInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Update Inquiry") // label for swagger
    @PatchMapping("/{inquiryNumber}")
    public ResponseEntity<?> patchInquiry(@PathVariable String inquiryNumber,
                                          @RequestParam String loginUserID,
                                          @Valid @RequestBody UpdateInquiry updateInquiry)
            throws IllegalAccessException, InvocationTargetException {
        updateInquiry.setStatusId(2L);
        Inquiry updatedInquiry = inquiryService.updateInquiry(inquiryNumber, loginUserID, updateInquiry);
        return new ResponseEntity<>(updatedInquiry, HttpStatus.OK);
    }

    @ApiOperation(response = Inquiry.class, value = "Delete Inquiry") // label for swagger
    @DeleteMapping("/{inquiryNumber}")
    public ResponseEntity<?> deleteInquiry(@PathVariable String inquiryNumber, @RequestParam String loginUserID) {
        boolean isDeleted = inquiryService.deleteInquiry(inquiryNumber, loginUserID);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isInquiryDeleted", isDeleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}