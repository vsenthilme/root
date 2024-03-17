package com.tekclover.wms.api.transaction.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvException;
import com.tekclover.wms.api.transaction.config.PropertiesConfig;
import com.tekclover.wms.api.transaction.controller.exception.BadRequestException;
import com.tekclover.wms.api.transaction.model.errorlog.ErrorLog;
import com.tekclover.wms.api.transaction.model.errorlog.FindErrorLog;
import com.tekclover.wms.api.transaction.repository.ErrorLogRepository;
import com.tekclover.wms.api.transaction.repository.specification.ErrorLogSpecification;
import com.tekclover.wms.api.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    PropertiesConfig propertiesConfig;

    private Path fileStorageLocation = null;

    // Get All Exception Log Details
    public List<ErrorLog> getAllErrorLogs() {
        List<ErrorLog> errorLogList = errorLogRepository.findAll();
        log.info("allErrorLogs --> " + errorLogList);
        return errorLogList;
    }

    /**
     *
     * @param errorLogList
     * @throws IOException
     */
    public void writeLog(List<ErrorLog> errorLogList) throws IOException, CsvException {
        this.fileStorageLocation = Paths.get(propertiesConfig.getErrorlogFolderName()).toAbsolutePath().normalize();
        if (!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new BadRequestException(
                        "Could not create the directory where the uploaded files will be stored.");
            }
        }
        log.info("location : " + fileStorageLocation);

        FileWriter fout = new FileWriter(this.fileStorageLocation + "/error_log.csv", true);

        //using custom delimiter and quote character
        CSVWriter csvWriter = new CSVWriter(fout);

        List<ErrorLog> logList = parseCSVWithHeader(errorLogList);
        Long lines = parseCSVForLines();
        List<String[]> data = toStringArray(logList, lines);
        csvWriter.writeAll(data);
        csvWriter.close();

//        reader();
    }

    /**
     *
     * @param errorLogList
     * @return
     * @throws IOException
     */
    public List<ErrorLog> parseCSVWithHeader(List<ErrorLog> errorLogList) throws IOException {
//        CSVReader reader = new CSVReader(new FileReader(this.fileStorageLocation + "\\error_log.csv"));

//        HeaderColumnNameMappingStrategy<ErrorLog> beanStrategy = new HeaderColumnNameMappingStrategy<ErrorLog>();
//        beanStrategy.setType(ErrorLog.class);

//        CsvToBean<ErrorLog> csvToBean = new CsvToBeanBuilder(reader)
//                .withType(ErrorLog.class)
//                .withSeparator(',')
//                .withIgnoreLeadingWhiteSpace(true)
//                .withIgnoreEmptyLine(true)
//                .build();


//        CsvToBean<ErrorLog> csvToBean = new CsvToBean<ErrorLog>();
//        List<ErrorLog> logs = csvToBean.parse();
        List<ErrorLog> logs = new ArrayList<>();
        logs.addAll(errorLogList);
//        reader.close();

        return logs;
    }

    public Long parseCSVForLines() throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(this.fileStorageLocation + "\\error_log.csv"));

        List<String[]> rows = reader.readAll();

        Long size = 0L;
        for (String[] row : rows) {
            size++;
        }

//        CsvToBean<ErrorLog> csvToBean = new CsvToBeanBuilder(reader)
//                .withType(ErrorLog.class)
//                .withSeparator(',')
//                .withIgnoreLeadingWhiteSpace(true)
//                .withIgnoreEmptyLine(true)
//                .build();
//
//        List<ErrorLog> logs = csvToBean.parse();
//        Long size = logs.stream().count();
        return size;
    }

    /**
     *
     * @param els
     * @return
     */
    private static List<String[]> toStringArray(List<ErrorLog> els, Long lines) {

        List<String[]> records = new ArrayList<String[]>();
        if(lines < 1) {
            // adding header record
            records.add(new String[]{"orderId", "orderTypeId", "orderDate", "errorMessage", "languageId", "companyCode",
                    "plantId", "warehouseId", "refDocNumber", "itemCode", "manufacturerName", "referenceField1", "referenceField2",
                    "referenceField3", "referenceField4", "referenceField5", "referenceField6", "referenceField7", "referenceField8",
                    "referenceField8", "referenceField9", "referenceField10", "createdBy", "createdOn"});
        }

        Iterator<ErrorLog> it = els.iterator();
        while (it.hasNext()) {
            ErrorLog elog = it.next();
            records.add(new String[] {
                    String.valueOf(elog.getOrderId()),
                    elog.getOrderTypeId(),
                    DateUtils.convertDatetoString(elog.getOrderDate()),
                    elog.getErrorMessage(),
                    elog.getLanguageId(),
                    elog.getCompanyCodeId(),
                    elog.getPlantId(),
                    elog.getWarehouseId(),
                    elog.getRefDocNumber(),
                    elog.getItemCode(),
                    elog.getManufacturerName(),
                    elog.getReferenceField1(),
                    elog.getReferenceField2(),
                    elog.getReferenceField3(),
                    elog.getReferenceField4(),
                    elog.getReferenceField5(),
                    elog.getReferenceField6(),
                    elog.getReferenceField7(),
                    elog.getReferenceField7(),
                    elog.getReferenceField8(),
                    elog.getReferenceField9(),
                    elog.getReferenceField10(),
                    elog.getCreatedBy(),
                    DateUtils.convertDatetoString(elog.getCreatedOn())
            });
        }
        return records;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private List<ErrorLog> reader() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(this.fileStorageLocation + "\\error_log.csv"));
        HeaderColumnNameMappingStrategy<ErrorLog> beanStrategy = new HeaderColumnNameMappingStrategy<ErrorLog>();
        beanStrategy.setType(ErrorLog.class);
        CsvToBean<ErrorLog> csvToBean = new CsvToBean<ErrorLog>();
        List<ErrorLog> emps = csvToBean.parse();
        reader.close();
        return emps;
    }
    
    // Find Exception Log
    public List<ErrorLog> findErrorLogs(FindErrorLog findErrorLog) throws ParseException {

        if (findErrorLog.getFromOrderDate() != null && findErrorLog.getToOrderDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(findErrorLog.getFromOrderDate(), findErrorLog.getToOrderDate());
            findErrorLog.setFromOrderDate(dates[0]);
            findErrorLog.setToOrderDate(dates[1]);
        }

        ErrorLogSpecification spec = new ErrorLogSpecification(findErrorLog);
        List<ErrorLog> results = errorLogRepository.findAll(spec);
        log.info("found exceptionLogs --> " + results);
        return results;
    }
    
}
