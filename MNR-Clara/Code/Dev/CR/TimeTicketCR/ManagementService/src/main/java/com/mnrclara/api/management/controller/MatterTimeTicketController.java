package com.mnrclara.api.management.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import com.mnrclara.api.management.model.dto.TimeKeeperBillingReport;
import com.mnrclara.api.management.model.dto.TimeKeeperBillingReportInput;
import com.mnrclara.api.management.model.mattertimeticket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mnrclara.api.management.model.dto.IMatterTimeTicket;
import com.mnrclara.api.management.service.MatterTimeTicketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = {"MatterTimeTicket"}, value = "MatterTimeTicket Operations related to MatterTimeTicketController")
// label for swagger
@SwaggerDefinition(tags = {@Tag(name = "MatterTimeTicket", description = "Operations related to MatterTimeTicket")})
@RequestMapping("/mattertimeticket")
@RestController
public class MatterTimeTicketController {

    @Autowired
    MatterTimeTicketService matterTimeTicketService;

    @ApiOperation(response = MatterTimeTicket.class, value = "Get all MatterTimeTicket details") // label for swagger
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<MatterTimeTicket> matterTimeTicketList = matterTimeTicketService.getMatterTimeTickets();
        return new ResponseEntity<>(matterTimeTicketList, HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Get a MatterTimeTicket") // label for swagger 
    @GetMapping("/{timeTicketNumber}")
    public ResponseEntity<?> getMatterTimeTicket(@PathVariable String timeTicketNumber) {
        MatterTimeTicket mattertimeticket = matterTimeTicketService.getMatterTimeTicket(timeTicketNumber);
        return new ResponseEntity<>(mattertimeticket, HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Get MatterTimeTickets by PrebillNumber")
    // label for swagger
    @GetMapping("/{preBillNumber}/preBillApprove")
    public ResponseEntity<?> getMatterTimeTicketForApprove(@PathVariable String preBillNumber) {
        List<IMatterTimeTicket> mattertimeticket =
                matterTimeTicketService.getMatterTimeTicketForApprove(preBillNumber);
        return new ResponseEntity<>(mattertimeticket, HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Search MatterTimeTicket") // label for swagger
    @PostMapping("/findMatterTimeTicket")
    public List<MatterTimeTicket> findMatterTimeTicket(@RequestBody SearchMatterTimeTicket searchMatterTimeTicket)
            throws ParseException {
        return matterTimeTicketService.findMatterTimeTicket(searchMatterTimeTicket);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Create MatterTimeTicket") // label for swagger
    @PostMapping("")
    public ResponseEntity<?> postMatterTimeTicket(@Valid @RequestBody AddMatterTimeTicket newMatterTimeTicket,
                                                  @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException, ParseException {
        MatterTimeTicket createdMatterTimeTicket =
                matterTimeTicketService.createMatterTimeTicket(newMatterTimeTicket, loginUserID);
        return new ResponseEntity<>(createdMatterTimeTicket, HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Create Bulk MatterTimeTicket") // label for swagger
    @PostMapping("/batch")
    public ResponseEntity<?> postBulkMatterTimeTicket(@Valid @RequestBody AddMatterTimeTicket[] newMatterTimeTicket,
                                                      @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        matterTimeTicketService.createBulkMatterTimeTickets(newMatterTimeTicket, loginUserID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Update MatterTimeTicket") // label for swagger
    @PatchMapping("/{timeTicketNumber}")
    public ResponseEntity<?> patchMatterTimeTicket(@PathVariable String timeTicketNumber,
                                                   @Valid @RequestBody UpdateMatterTimeTicket updateMatterTimeTicket, @RequestParam String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        MatterTimeTicket updatedMatterTimeTicket =
                matterTimeTicketService.updateMatterTimeTicket(timeTicketNumber, updateMatterTimeTicket, loginUserID);
        return new ResponseEntity<>(updatedMatterTimeTicket, HttpStatus.OK);
    }

    @ApiOperation(response = MatterTimeTicket.class, value = "Delete MatterTimeTicket") // label for swagger
    @DeleteMapping("/{timeTicketNumber}")
    public ResponseEntity<?> deleteMatterTimeTicket(@PathVariable String timeTicketNumber,
                                                    @RequestParam String loginUserID) {
        matterTimeTicketService.deleteMatterTimeTicket(timeTicketNumber, loginUserID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //TimeKeeper Billing Report
    @ApiOperation(response = TimeKeeperBillingReport.class, value = "Time Keeper Billing Report") // label for swagger
    @PostMapping("/findTimeKeeperBillingReport")
    public List<TimeKeeperBillingReport> findTimeKeeperBillingReport(@RequestBody TimeKeeperBillingReportInput timeKeeperBillingReportInput)
            throws ParseException {
        return matterTimeTicketService.findTimeKeeperBillingReport(timeKeeperBillingReportInput);
    }

    //TimeTicket Notification
    @ApiOperation(response = TimeTicketNotificationReport.class, value = "Time Keep Ticket Notification")
    // label for swagger
    @PostMapping("/findTimeTicketNotification")
    public List<TimeTicketNotificationReport> findTicketNotification(@RequestBody FindTimeTicketNotification findTimeTicketNotification)
            throws ParseException {
        return matterTimeTicketService.findTimeTicketNotification(findTimeTicketNotification);
    }

    //Search IMatter TimeTicket
    @ApiOperation(response = MatterTimeTicket.class, value = "Search IMatterTimeTicket") // label for swagger
    @PostMapping("/findIMatterTimeTicket")
    public ResponseEntity<?> findIMatterTimeTicket(@RequestBody SearchMatterTimeTicket searchMatterTimeTicket) throws Exception {
        List<IMatterTimeTicket> iMatterTimeTickets = matterTimeTicketService.searchMatterTimeTicket(searchMatterTimeTicket);
        return new ResponseEntity<>(iMatterTimeTickets, HttpStatus.OK);
    }
}