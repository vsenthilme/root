package com.tekclover.wms.api.transaction.service;

import com.tekclover.wms.api.transaction.model.exceptionlog.ExceptionLog;
import com.tekclover.wms.api.transaction.repository.ExceptionLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExceptionLogService {

    @Autowired
    ExceptionLogRepository exceptionLogRepo;

    // Get All Exception Log Details
    public List<ExceptionLog> getAllExceptionLogs() {
        List<ExceptionLog> exceptionLogList = exceptionLogRepo.findAll();
        return exceptionLogList;
    }

}
