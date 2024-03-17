package com.tekclover.wms.api.transaction.controller;

import com.tekclover.wms.api.transaction.model.errorlog.ErrorLog;
import com.tekclover.wms.api.transaction.model.errorlog.FindErrorLog;
import com.tekclover.wms.api.transaction.service.ErrorLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Validated
@Api(tags = {"ErrorLog"}, value = "ErrorLog Operations related to ErrorLog")
@SwaggerDefinition(tags = {@Tag(name = "ErrorLog", description = "Operations related to ErrorLog")})
@RequestMapping("/errorlog")
@RestController
public class ErrorLogController {

    @Autowired
    private ErrorLogService errorLogService;

    // Get All Error Log Details
    @ApiOperation(response = ErrorLog.class, value = "Get All Exception Log Details")
    @GetMapping("/all")
    public ResponseEntity<?> getAllErrorLogDetails() {
        List<ErrorLog> errorLogList = errorLogService.getAllErrorLogs();
        return new ResponseEntity<>(errorLogList, HttpStatus.OK);
    }

    // Find Error Log
    @ApiOperation(response = ErrorLog.class, value = "Find Error Log")
    @PostMapping("/find")
    public ResponseEntity<?> findErrorLog(@RequestBody FindErrorLog findErrorLog) throws ParseException {
        List<ErrorLog> errorLogList = errorLogService.findErrorLogs(findErrorLog);
        return new ResponseEntity<>(errorLogList, HttpStatus.OK);
    }

}
