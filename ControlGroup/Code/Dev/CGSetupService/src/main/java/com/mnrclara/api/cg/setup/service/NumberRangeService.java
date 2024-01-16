package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.exception.DataNotFoundException;
import com.mnrclara.api.cg.setup.model.numberange.AddNumberRange;
import com.mnrclara.api.cg.setup.model.numberange.FindNumberRange;
import com.mnrclara.api.cg.setup.model.numberange.NumberRange;
import com.mnrclara.api.cg.setup.model.numberange.UpdateNumberRange;
import com.mnrclara.api.cg.setup.repository.NumberRangeRepository;
import com.mnrclara.api.cg.setup.repository.specification.NumberRangeSpecification;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NumberRangeService {

    @Autowired
    NumberRangeRepository numberRangeRepository;

    /**
     * @return
     */
    public List<NumberRange> getNumberRanges() {
        List<NumberRange> numberRangeList = numberRangeRepository.findAll();
        numberRangeList = numberRangeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return numberRangeList;
    }

    /**
     *
     * @param languageId
     * @param companyId
     * @param numberRangeCode
     * @param numberRangeObject
     * @return
     */
    public NumberRange getNumberRange(String languageId, String companyId, Long numberRangeCode, String numberRangeObject) {
        Optional<NumberRange> numberRange =
                numberRangeRepository.findByLanguageIdAndCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator(
                        languageId, companyId, numberRangeCode, numberRangeObject, 0l);
        if (numberRange.isEmpty()) {
            throw new BadRequestException("The given NumberRangeCode : " + numberRangeCode +
                    "languageId" + languageId +
                    "companyId" + companyId +
                    "numberRangeObject" + numberRangeObject + " doesn't exist.");
        }
        return numberRange.get();
    }

    /**
     *
     * @param newNumberRange
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public NumberRange createNumberRange(AddNumberRange newNumberRange, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<NumberRange> numberRange =
                numberRangeRepository.findByLanguageIdAndCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator(
                        newNumberRange.getLanguageId(),
                        newNumberRange.getCompanyId(),
                        newNumberRange.getNumberRangeCode(),
                        newNumberRange.getNumberRangeObject(),
                        0L);
        if (!numberRange.isEmpty()) {
            throw new BadRequestException("Record is getting duplicated with the given values");
        }
        NumberRange dbNumberRange = new NumberRange();
        BeanUtils.copyProperties(newNumberRange, dbNumberRange);
        dbNumberRange.setDeletionIndicator(0L);
        dbNumberRange.setCreatedBy(loginUserID);
        dbNumberRange.setUpdatedBy(loginUserID);
        dbNumberRange.setCreatedOn(new Date());
        dbNumberRange.setUpdatedOn(new Date());
        return numberRangeRepository.save(dbNumberRange);
    }

    /**
     *
     * @param numberRangeCode
     * @param companyId
     * @param languageId
     * @param numberRangeObject
     * @param loginUserID
     * @param updateNumberRange
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public NumberRange updateNumberRange(Long numberRangeCode, String companyId, String languageId, String numberRangeObject,
                                         String loginUserID, UpdateNumberRange updateNumberRange)
            throws IllegalAccessException, InvocationTargetException {
        NumberRange dbNumberRange = getNumberRange(languageId, companyId, numberRangeCode, numberRangeObject);
        BeanUtils.copyProperties(updateNumberRange, dbNumberRange, CommonUtils.getNullPropertyNames(updateNumberRange));
        dbNumberRange.setUpdatedBy(loginUserID);
        dbNumberRange.setUpdatedOn(new Date());
        return numberRangeRepository.save(dbNumberRange);
    }

    /**
     *
     * @param numberRangeCode
     * @param companyId
     * @param languageId
     * @param numberRangeObject
     * @param loginUserID
     */
    public void deleteNumberRange(Long numberRangeCode, String companyId, String languageId, String numberRangeObject, String loginUserID) {
        NumberRange numberRange = getNumberRange(languageId, companyId, numberRangeCode, numberRangeObject);
        if (numberRange != null) {
            numberRange.setDeletionIndicator(1L);
            numberRange.setUpdatedBy(loginUserID);
            numberRangeRepository.save(numberRange);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + numberRangeCode);
        }
    }

    /**
     *
     * @param findNumberRange
     * @return
     * @throws ParseException
     */
    public List<NumberRange> findNumberRange(FindNumberRange findNumberRange) throws ParseException {

        if (findNumberRange.getStartCreatedOn() != null && findNumberRange.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findNumberRange.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findNumberRange.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findNumberRange.setFromDate(dates[0]);
            findNumberRange.setToDate(dates[1]);
        }
        NumberRangeSpecification spec = new NumberRangeSpecification(findNumberRange);
        List<NumberRange> results = numberRangeRepository.findAll(spec);
        results = results.stream().filter(n->n.getDeletionIndicator()==0).collect(Collectors.toList());
        return results;

    }

    /**
     *
     * @param numberRangeCode
     * @return
     */
    public String getNextNumberRange (Long numberRangeCode,String numberRangeObject,String languageId, String companyId ) {
        log.info("numberRangeCode : " + numberRangeCode);
        Optional<NumberRange> numberRangeOpt = numberRangeRepository.findByNumberRangeCodeAndNumberRangeObjectAndLanguageIdAndCompanyId(numberRangeCode,numberRangeObject,languageId,companyId);
        if (numberRangeOpt.isEmpty()) {
            throw new DataNotFoundException("Record not found for given Range Code : " + numberRangeCode);
        }
        NumberRange currentNumberRange = numberRangeOpt.get();

        Optional<NumberRange> optNumberRange =
                numberRangeRepository.findByLanguageIdAndCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator(
                        languageId,
                        companyId,
                        numberRangeCode,
                        numberRangeObject,
                        0L);
        NumberRange numberRange = optNumberRange.get();
        log.info("Current record: " + numberRange);

        String strCurrentValue = numberRange.getNumberRangeCurrent();
        Long currentValue = 0L;
        if (strCurrentValue.startsWith("AL")) { // Increment logic for AuditLog Insert
            strCurrentValue = strCurrentValue.substring(2); // AL1000002
            currentValue = Long.valueOf(strCurrentValue);
            currentValue ++;
            strCurrentValue = "AL" + String.valueOf(currentValue);
            numberRange.setNumberRangeCurrent(strCurrentValue);
            log.info("currentValue of AL: " + currentValue);
        } else {
            currentValue = Long.valueOf(strCurrentValue);
            currentValue ++;
            log.info("currentValue : " + currentValue);
            numberRange.setNumberRangeCurrent(String.valueOf(currentValue));
            strCurrentValue = String.valueOf(currentValue);
        }
        log.info("New value numberRange: " + numberRange);
        numberRangeRepository.save(numberRange);
        log.info("New value has been updated in NumberRangeCurrent column");
        return strCurrentValue;
    }
}
